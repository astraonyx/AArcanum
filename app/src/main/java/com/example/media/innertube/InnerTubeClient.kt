package com.example.media.innertube

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder

data class InnerTubeSong(
    val id: String,
    val title: String,
    val artist: String,
    val album: String,
    val durationSec: Int,
    val thumbnailUrl: String,
    val streamUrl: String? = null
)

/**
 * Client independen untuk YouTube Music InnerTube API & Piped Stream Resolver.
 * Menggunakan java.net.HttpURLConnection & org.json bawaan Android
 * sehingga 100% bebas konflik dependensi pada Gradle dan Google AI Studio.
 */
object InnerTubeClient {

    private const val INNERTUBE_API_URL = "https://music.youtube.com/youtubei/v1"
    private const val USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36"

    /**
     * Mencari lagu dari YouTube Music InnerTube API berdasarkan query.
     */
    suspend fun search(query: String): List<InnerTubeSong> = withContext(Dispatchers.IO) {
        if (query.isBlank()) return@withContext emptyList()
        try {
            val url = URL("$INNERTUBE_API_URL/search?prettyPrint=false")
            val conn = (url.openConnection() as HttpURLConnection).apply {
                requestMethod = "POST"
                doOutput = true
                connectTimeout = 8000
                readTimeout = 8000
                setRequestProperty("Content-Type", "application/json")
                setRequestProperty("User-Agent", USER_AGENT)
                setRequestProperty("X-YouTube-Client-Name", "67")
                setRequestProperty("X-YouTube-Client-Version", "1.20231201.01.00")
                setRequestProperty("Origin", "https://music.youtube.com")
                setRequestProperty("Referer", "https://music.youtube.com")
            }

            val payload = JSONObject().apply {
                put("context", JSONObject().apply {
                    put("client", JSONObject().apply {
                        put("clientName", "WEB_REMIX")
                        put("clientVersion", "1.20231201.01.00")
                        put("hl", "id")
                        put("gl", "ID")
                    })
                })
                put("query", query)
                put("params", "Eg-KAQwIARAAGAAgACgAMABqChAMEA0QChADEAQQBQ%3D%3D") // Filter khusus musik/tracks
            }

            OutputStreamWriter(conn.outputStream).use { it.write(payload.toString()) }

            if (conn.responseCode in 200..299) {
                val responseText = conn.inputStream.bufferedReader().use { it.readText() }
                return@withContext parseSearchResults(responseText)
            }
        } catch (_: Exception) {
            // Fallback via Piped / Invidious jika InnerTube diblokir
        }

        // Fallback search via Piped API
        return@withContext searchViaPipedFallback(query)
    }

    /**
     * Mengambil rekomendasi / trending music.
     */
    suspend fun fetchTrending(): List<InnerTubeSong> = withContext(Dispatchers.IO) {
        try {
            val url = URL("$INNERTUBE_API_URL/browse?prettyPrint=false")
            val conn = (url.openConnection() as HttpURLConnection).apply {
                requestMethod = "POST"
                doOutput = true
                connectTimeout = 8000
                readTimeout = 8000
                setRequestProperty("Content-Type", "application/json")
                setRequestProperty("User-Agent", USER_AGENT)
                setRequestProperty("X-YouTube-Client-Name", "67")
                setRequestProperty("X-YouTube-Client-Version", "1.20231201.01.00")
            }

            val payload = JSONObject().apply {
                put("context", JSONObject().apply {
                    put("client", JSONObject().apply {
                        put("clientName", "WEB_REMIX")
                        put("clientVersion", "1.20231201.01.00")
                        put("hl", "id")
                        put("gl", "ID")
                    })
                })
                put("browseId", "FEmusic_charts")
            }

            OutputStreamWriter(conn.outputStream).use { it.write(payload.toString()) }

            if (conn.responseCode in 200..299) {
                val responseText = conn.inputStream.bufferedReader().use { it.readText() }
                val results = parseSearchResults(responseText)
                if (results.isNotEmpty()) return@withContext results
            }
        } catch (_: Exception) {}

        // Fallback curated trending
        return@withContext getCuratedTrendingFallback()
    }

    /**
     * Menyelesaikan URL stream audio asli (Opus/M4A) untuk diputar di MediaPlayer.
     */
    suspend fun resolveAudioStreamUrl(videoId: String): String = withContext(Dispatchers.IO) {
        // Coba via Piped API public instance
        val pipedInstances = listOf(
            "https://pipedapi.kavin.rocks",
            "https://api.piped.private.coffee",
            "https://pipedapi.leptons.xyz"
        )

        for (instance in pipedInstances) {
            try {
                val url = URL("$instance/streams/$videoId")
                val conn = (url.openConnection() as HttpURLConnection).apply {
                    requestMethod = "GET"
                    connectTimeout = 5000
                    readTimeout = 5000
                    setRequestProperty("User-Agent", USER_AGENT)
                }
                if (conn.responseCode in 200..299) {
                    val body = conn.inputStream.bufferedReader().use { it.readText() }
                    val json = JSONObject(body)
                    val audioStreams = json.optJSONArray("audioStreams")
                    if (audioStreams != null && audioStreams.length() > 0) {
                        // Cari stream audio dengan bitrate tertinggi
                        var bestUrl = ""
                        var maxBitrate = 0
                        for (i in 0 until audioStreams.length()) {
                            val stream = audioStreams.getJSONObject(i)
                            val bitrate = stream.optInt("bitrate", 0)
                            val streamUrl = stream.optString("url", "")
                            if (streamUrl.isNotBlank() && (bitrate > maxBitrate || bestUrl.isEmpty())) {
                                maxBitrate = bitrate
                                bestUrl = streamUrl
                            }
                        }
                        if (bestUrl.isNotBlank()) return@withContext bestUrl
                    }
                }
            } catch (_: Exception) {
                continue
            }
        }

        // Direct YouTube audio playback fallback URL
        return@withContext "https://music.youtube.com/watch?v=$videoId"
    }

    private fun parseSearchResults(jsonString: String): List<InnerTubeSong> {
        val list = mutableListOf<InnerTubeSong>()
        try {
            val root = JSONObject(jsonString)
            // Ekstrak token videoId, title, author secara rekursif
            extractMusicItems(root, list)
        } catch (_: Exception) {}
        return list
    }

    private fun extractMusicItems(obj: Any?, outList: MutableList<InnerTubeSong>) {
        when (obj) {
            is JSONObject -> {
                if (obj.has("musicResponsiveListItemRenderer")) {
                    val item = obj.getJSONObject("musicResponsiveListItemRenderer")
                    parseResponsiveItem(item)?.let { outList.add(it) }
                } else {
                    val keys = obj.keys()
                    while (keys.hasNext()) {
                        extractMusicItems(obj.get(keys.next()), outList)
                    }
                }
            }
            is JSONArray -> {
                for (i in 0 until obj.length()) {
                    extractMusicItems(obj.get(i), outList)
                }
            }
        }
    }

    private fun parseResponsiveItem(item: JSONObject): InnerTubeSong? {
        return try {
            val flexColumns = item.optJSONArray("flexColumns") ?: return null
            if (flexColumns.length() < 2) return null

            // Kolom 1: Title
            val titleCol = flexColumns.getJSONObject(0)
                .getJSONObject("musicResponsiveListItemFlexColumnRenderer")
                .getJSONObject("text")
                .getJSONArray("runs")
                .getJSONObject(0)
            val title = titleCol.getString("text")

            val navEndpoint = titleCol.optJSONObject("navigationEndpoint")
            val videoId = navEndpoint?.optJSONObject("watchEndpoint")?.optString("videoId", "")
                ?: item.optString("playlistItemData", "")
            if (videoId.isBlank()) return null

            // Kolom 2: Artist & Album
            val metaRuns = flexColumns.getJSONObject(1)
                .getJSONObject("musicResponsiveListItemFlexColumnRenderer")
                .getJSONObject("text")
                .getJSONArray("runs")

            var artist = "Artis Auralys"
            var album = "Single"
            if (metaRuns.length() > 0) {
                artist = metaRuns.getJSONObject(0).optString("text", artist)
            }
            if (metaRuns.length() > 2) {
                album = metaRuns.getJSONObject(2).optString("text", album)
            }

            val thumbUrl = "https://i.ytimg.com/vi/$videoId/hqdefault.jpg"

            InnerTubeSong(
                id = videoId,
                title = title,
                artist = artist,
                album = album,
                durationSec = 210,
                thumbnailUrl = thumbUrl
            )
        } catch (_: Exception) {
            null
        }
    }

    private fun searchViaPipedFallback(query: String): List<InnerTubeSong> {
        val list = mutableListOf<InnerTubeSong>()
        try {
            val encoded = URLEncoder.encode(query, "UTF-8")
            val url = URL("https://pipedapi.kavin.rocks/search?q=$encoded&filter=music_songs")
            val conn = (url.openConnection() as HttpURLConnection).apply {
                connectTimeout = 5000
                readTimeout = 5000
                setRequestProperty("User-Agent", USER_AGENT)
            }
            if (conn.responseCode in 200..299) {
                val text = conn.inputStream.bufferedReader().use { it.readText() }
                val root = JSONObject(text)
                val items = root.optJSONArray("items") ?: return emptyList()
                for (i in 0 until items.length()) {
                    val it = items.getJSONObject(i)
                    val urlStr = it.optString("url", "")
                    val vId = if (urlStr.contains("v=")) urlStr.substringAfter("v=") else urlStr.substringAfterLast("/")
                    if (vId.isNotBlank()) {
                        list.add(
                            InnerTubeSong(
                                id = vId,
                                title = it.optString("title", "Lagu"),
                                artist = it.optString("uploaderName", "Artis"),
                                album = "Online Stream",
                                durationSec = it.optInt("duration", 210),
                                thumbnailUrl = it.optString("thumbnail", "https://i.ytimg.com/vi/$vId/hqdefault.jpg")
                            )
                        )
                    }
                }
            }
        } catch (_: Exception) {}
        return list
    }

    private fun getCuratedTrendingFallback(): List<InnerTubeSong> {
        return listOf(
            InnerTubeSong("kJQP7kiw5Fk", "Despacito", "Luis Fonsi ft. Daddy Yankee", "VIDA", 282, "https://i.ytimg.com/vi/kJQP7kiw5Fk/hqdefault.jpg"),
            InnerTubeSong("fJ9rUzIMcZQ", "Bohemian Rhapsody", "Queen", "A Night at the Opera", 359, "https://i.ytimg.com/vi/fJ9rUzIMcZQ/hqdefault.jpg"),
            InnerTubeSong("JGwWNGJdvx8", "Shape of You", "Ed Sheeran", "÷ (Divide)", 234, "https://i.ytimg.com/vi/JGwWNGJdvx8/hqdefault.jpg"),
            InnerTubeSong("RgKAFK5djSk", "See You Again", "Wiz Khalifa ft. Charlie Puth", "Furious 7", 230, "https://i.ytimg.com/vi/RgKAFK5djSk/hqdefault.jpg"),
            InnerTubeSong("OPf0YbXqDm0", "Uptown Funk", "Mark Ronson ft. Bruno Mars", "Uptown Special", 270, "https://i.ytimg.com/vi/OPf0YbXqDm0/hqdefault.jpg")
        )
    }
}
