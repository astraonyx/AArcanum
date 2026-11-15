package com.example.media.lyrics

import com.example.media.AuralysLyricLine
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder

/**
 * Client untuk LRCLIB API (https://lrclib.net).
 * Mengambil lirik berformat synchronized timestamps [mm:ss.xx]
 * dan mem-parsing ke List<AuralysLyricLine> untuk scrolling otomatis.
 */
object LrcLibClient {

    private const val LRCLIB_BASE_URL = "https://lrclib.net/api"
    private const val USER_AGENT = "AuralysChamber/4.3.5 (Arcanum Secure Media Suite)"

    /**
     * Mengambil lirik sinkron berdasarkan judul lagu dan nama artis.
     */
    suspend fun getSyncedLyrics(
        trackTitle: String,
        artistName: String,
        durationSeconds: Int = 0
    ): List<AuralysLyricLine> = withContext(Dispatchers.IO) {
        if (trackTitle.isBlank()) return@withContext emptyList()

        try {
            val encodedTitle = URLEncoder.encode(trackTitle.trim(), "UTF-8")
            val encodedArtist = URLEncoder.encode(artistName.trim(), "UTF-8")
            val durationParam = if (durationSeconds > 0) "&duration=$durationSeconds" else ""
            val targetUrl = "$LRCLIB_BASE_URL/get?track_name=$encodedTitle&artist_name=$encodedArtist$durationParam"

            val conn = (URL(targetUrl).openConnection() as HttpURLConnection).apply {
                requestMethod = "GET"
                connectTimeout = 5000
                readTimeout = 5000
                setRequestProperty("User-Agent", USER_AGENT)
            }

            if (conn.responseCode in 200..299) {
                val body = conn.inputStream.bufferedReader().use { it.readText() }
                val json = JSONObject(body)
                val syncedLyricsStr = json.optString("syncedLyrics", "")
                if (syncedLyricsStr.isNotBlank()) {
                    val parsed = parseLrcString(syncedLyricsStr)
                    if (parsed.isNotEmpty()) return@withContext parsed
                }

                // Fallback ke plain lyrics jika synced tidak ada
                val plainLyrics = json.optString("plainLyrics", "")
                if (plainLyrics.isNotBlank()) {
                    return@withContext convertPlainToLines(plainLyrics)
                }
            }
        } catch (_: Exception) {}

        // Fallback search jika exact get gagal
        return@withContext searchLyricsFallback(trackTitle, artistName)
    }

    private fun searchLyricsFallback(title: String, artist: String): List<AuralysLyricLine> {
        try {
            val query = "$title $artist".trim()
            val encoded = URLEncoder.encode(query, "UTF-8")
            val url = URL("$LRCLIB_BASE_URL/search?q=$encoded")
            val conn = (url.openConnection() as HttpURLConnection).apply {
                requestMethod = "GET"
                connectTimeout = 5000
                readTimeout = 5000
                setRequestProperty("User-Agent", USER_AGENT)
            }
            if (conn.responseCode in 200..299) {
                val body = conn.inputStream.bufferedReader().use { it.readText() }
                val array = org.json.JSONArray(body)
                if (array.length() > 0) {
                    val first = array.getJSONObject(0)
                    val synced = first.optString("syncedLyrics", "")
                    if (synced.isNotBlank()) {
                        return parseLrcString(synced)
                    }
                    val plain = first.optString("plainLyrics", "")
                    if (plain.isNotBlank()) {
                        return convertPlainToLines(plain)
                    }
                }
            }
        } catch (_: Exception) {}
        return emptyList()
    }

    /**
     * Mem-parsing string LRC standar ([mm:ss.xx] teks) ke List<AuralysLyricLine>.
     */
    fun parseLrcString(lrcContent: String): List<AuralysLyricLine> {
        val lines = mutableListOf<AuralysLyricLine>()
        val lrcRegex = Regex("""\[(\d{1,2}):(\d{2})(?:\.(\d{1,3}))?\](.*)""")

        for (rawLine in lrcContent.lines()) {
            val match = lrcRegex.matchEntire(rawLine.trim()) ?: continue
            val min = match.groupValues[1].toLongOrNull() ?: 0L
            val sec = match.groupValues[2].toLongOrNull() ?: 0L
            val fracStr = match.groupValues[3].ifEmpty { "0" }
            val frac = when (fracStr.length) {
                1 -> fracStr.toLong() * 100
                2 -> fracStr.toLong() * 10
                else -> fracStr.take(3).toLong()
            }
            val text = match.groupValues[4].trim()
            val timestampMs = (min * 60 + sec) * 1000L + frac
            lines.add(AuralysLyricLine(timestampMs, text))
        }

        return lines.sortedBy { it.timestampMs }
    }

    private fun convertPlainToLines(plainText: String): List<AuralysLyricLine> {
        val lines = plainText.lines().filter { it.isNotBlank() }
        var currentMs = 0L
        return lines.map {
            val line = AuralysLyricLine(currentMs, it.trim())
            currentMs += 4000L // Estimasi jeda 4 detik per baris
            line
        }
    }
}
