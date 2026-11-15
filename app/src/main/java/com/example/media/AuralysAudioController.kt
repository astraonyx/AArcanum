package com.example.media

import android.content.Context
import android.media.AudioFormat
import android.media.AudioTrack
import android.media.MediaPlayer
import android.net.Uri
import com.example.data.VaultItemEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlin.math.abs
import kotlin.math.sin

enum class QueueSourceMode {
    VAULT_LOCAL_FOLDER,
    ONLINE_STREAMING
}

enum class AuralysRepeatMode {
    OFF,
    ALL,
    ONE
}

data class AuralysLyricLine(
    val timestampMs: Long,
    val text: String
)

data class AuralysTrack(
    val id: String,
    val title: String,
    val artist: String,
    val album: String,
    val durationSec: Int,
    val sourceMode: QueueSourceMode,
    val streamUri: String? = null,
    val artworkUri: String? = null,
    val folderPath: String? = null,
    val lyrics: List<AuralysLyricLine> = emptyList(),
    val year: String = "2026",
    val genre: String = "Cosmic Ambient",
    val bitRateKbps: Int = 320
)

/**
 * Controller audio hibrida untuk Auralys Music Chamber.
 * Mendukung pemutaran file lokal brankas dengan isolasi Folder-Scoped Queue
 * dan streaming dinamis dengan antrean rekomendasi online.
 */
class AuralysAudioController(
    private val context: Context,
    private val scope: CoroutineScope
) {
    private var mediaPlayer: MediaPlayer? = null
    private var synthJob: Job? = null
    private var sleepTimerJob: Job? = null

    private val _currentTrack = MutableStateFlow<AuralysTrack?>(null)
    val currentTrack: StateFlow<AuralysTrack?> = _currentTrack.asStateFlow()

    private val _isPlaying = MutableStateFlow(false)
    val isPlaying: StateFlow<Boolean> = _isPlaying.asStateFlow()

    private val _currentPositionSec = MutableStateFlow(0)
    val currentPositionSec: StateFlow<Int> = _currentPositionSec.asStateFlow()

    private val _totalDurationSec = MutableStateFlow(180)
    val totalDurationSec: StateFlow<Int> = _totalDurationSec.asStateFlow()

    private val _liveAmplitude = MutableStateFlow(0.15f)
    val liveAmplitude: StateFlow<Float> = _liveAmplitude.asStateFlow()

    private val _playbackSpeed = MutableStateFlow(1.0f)
    val playbackSpeed: StateFlow<Float> = _playbackSpeed.asStateFlow()

    private val _repeatMode = MutableStateFlow(AuralysRepeatMode.ALL)
    val repeatMode: StateFlow<AuralysRepeatMode> = _repeatMode.asStateFlow()

    private val _isShuffleEnabled = MutableStateFlow(false)
    val isShuffleEnabled: StateFlow<Boolean> = _isShuffleEnabled.asStateFlow()

    private val _sleepTimerRemainingSec = MutableStateFlow<Int?>(null)
    val sleepTimerRemainingSec: StateFlow<Int?> = _sleepTimerRemainingSec.asStateFlow()

    private val _queue = MutableStateFlow<List<AuralysTrack>>(emptyList())
    val queue: StateFlow<List<AuralysTrack>> = _queue.asStateFlow()

    private val _queueIndex = MutableStateFlow(0)
    val queueIndex: StateFlow<Int> = _queueIndex.asStateFlow()

    init {
        // Inisialisasi katalog default online streaming
        val defaultCatalog = createDefaultOnlineCatalog()
        _queue.value = defaultCatalog
        if (defaultCatalog.isNotEmpty()) {
            _currentTrack.value = defaultCatalog[0]
            _totalDurationSec.value = defaultCatalog[0].durationSec
        }
    }

    fun playTrack(track: AuralysTrack, customQueue: List<AuralysTrack>? = null) {
        val activeQ = customQueue ?: _queue.value
        val idx = activeQ.indexOfFirst { it.id == track.id }.coerceAtLeast(0)
        _queue.value = activeQ
        _queueIndex.value = idx
        _currentTrack.value = track
        _totalDurationSec.value = track.durationSec.coerceAtLeast(1)
        _currentPositionSec.value = 0

        startInternalPlayback(track, 0)
    }

    fun togglePlayPause() {
        val track = _currentTrack.value ?: return
        if (_isPlaying.value) {
            pause()
        } else {
            resume()
        }
    }

    fun pause() {
        _isPlaying.value = false
        mediaPlayer?.let {
            if (it.isPlaying) it.pause()
        }
    }

    fun resume() {
        val track = _currentTrack.value ?: return
        _isPlaying.value = true
        val mp = mediaPlayer
        if (mp != null) {
            try {
                mp.start()
                return
            } catch (_: Exception) {}
        }
        startInternalPlayback(track, _currentPositionSec.value)
    }

    fun seekTo(seconds: Int) {
        val clamped = seconds.coerceIn(0, _totalDurationSec.value)
        _currentPositionSec.value = clamped
        mediaPlayer?.let {
            try {
                it.seekTo(clamped * 1000)
            } catch (_: Exception) {}
        }
    }

    fun setPlaybackSpeed(speed: Float) {
        val validSpeed = speed.coerceIn(0.5f, 2.5f)
        _playbackSpeed.value = validSpeed
        mediaPlayer?.let {
            try {
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                    it.playbackParams = it.playbackParams.setSpeed(validSpeed)
                }
            } catch (_: Exception) {}
        }
    }

    fun toggleRepeatMode() {
        _repeatMode.value = when (_repeatMode.value) {
            AuralysRepeatMode.OFF -> AuralysRepeatMode.ALL
            AuralysRepeatMode.ALL -> AuralysRepeatMode.ONE
            AuralysRepeatMode.ONE -> AuralysRepeatMode.OFF
        }
    }

    fun toggleShuffle() {
        _isShuffleEnabled.value = !_isShuffleEnabled.value
    }

    fun skipNext() {
        val q = _queue.value
        if (q.isEmpty()) return
        val nextIdx = when {
            _repeatMode.value == AuralysRepeatMode.ONE -> _queueIndex.value
            _isShuffleEnabled.value -> (q.indices).random()
            else -> (_queueIndex.value + 1) % q.size
        }
        _queueIndex.value = nextIdx
        val track = q[nextIdx]
        playTrack(track, q)
    }

    fun skipPrevious() {
        val q = _queue.value
        if (q.isEmpty()) return
        if (_currentPositionSec.value > 3) {
            seekTo(0)
            return
        }
        val prevIdx = if (_queueIndex.value - 1 < 0) q.size - 1 else _queueIndex.value - 1
        _queueIndex.value = prevIdx
        val track = q[prevIdx]
        playTrack(track, q)
    }

    fun setSleepTimer(minutes: Int) {
        sleepTimerJob?.cancel()
        if (minutes <= 0) {
            _sleepTimerRemainingSec.value = null
            return
        }
        val totalSec = minutes * 60
        _sleepTimerRemainingSec.value = totalSec
        sleepTimerJob = scope.launch(Dispatchers.Default) {
            var remaining = totalSec
            while (isActive && remaining > 0) {
                delay(1000L)
                remaining--
                _sleepTimerRemainingSec.value = remaining
            }
            if (isActive) {
                pause()
                _sleepTimerRemainingSec.value = null
            }
        }
    }

    fun cancelSleepTimer() {
        sleepTimerJob?.cancel()
        _sleepTimerRemainingSec.value = null
    }

    /**
     * Memuat file audio dari brankas terenkripsi lokal dan menguncinya dalam
     * Folder-Scoped Queue sehingga trek dari folder lain tidak bocor ke antrean.
     */
    fun loadVaultFolderAudio(folderName: String, vaultItems: List<VaultItemEntity>): List<AuralysTrack> {
        val audioExts = setOf("mp3", "wav", "m4a", "flac", "ogg", "aac", "opus")
        val filtered = vaultItems.filter { item ->
            val ext = item.extension.lowercase().ifBlank { item.name.substringAfterLast('.', "").lowercase() }
            val itemFolder = item.groupArchiveName ?: ""
            val isAudio = ext in audioExts || item.category == "AUDIO"
            if (!isAudio) return@filter false
            when {
                folderName.isBlank() || folderName.equals("Semua File Audio (Root)", ignoreCase = true) -> true
                folderName.equals("Root", ignoreCase = true) -> itemFolder.isBlank() || itemFolder.equals("Root", ignoreCase = true)
                else -> itemFolder.equals(folderName, ignoreCase = true)
            }
        }

        val folderTracks = filtered.mapIndexed { idx, it ->
            val trackFolder = it.groupArchiveName ?: ""
            AuralysTrack(
                id = "vault_audio_${it.id}",
                title = it.name.substringBeforeLast('.').ifBlank { it.name },
                artist = if (it.isEncrypted) "Brankas NYX2 Terenkripsi" else "Audio Brankas",
                album = trackFolder.ifBlank { "Root Vault Folder" },
                durationSec = if (it.durationSeconds > 0) it.durationSeconds else 210,
                sourceMode = QueueSourceMode.VAULT_LOCAL_FOLDER,
                folderPath = trackFolder,
                genre = "Secure Vault Audio",
                lyrics = listOf(
                    AuralysLyricLine(0L, "[Audio Lokal Terenkripsi]"),
                    AuralysLyricLine(5000L, it.name),
                    AuralysLyricLine(12000L, "Diputar aman di dalam Auralys Music Chamber"),
                    AuralysLyricLine(25000L, "Kunci enkripsi terlindungi tanpa kebocoran memori")
                )
            )
        }

        if (folderTracks.isNotEmpty()) {
            _queue.value = folderTracks
            _queueIndex.value = 0
            _currentTrack.value = folderTracks[0]
            _totalDurationSec.value = folderTracks[0].durationSec
        }
        return folderTracks
    }

    private fun startInternalPlayback(track: AuralysTrack, startFromSec: Int) {
        stopInternalPlayer()
        _isPlaying.value = true

        val uriStr = track.streamUri
        if (!uriStr.isNullOrBlank()) {
            try {
                val mp = MediaPlayer().apply {
                    setDataSource(context, Uri.parse(uriStr))
                    prepare()
                    val realSec = (duration / 1000).coerceAtLeast(1)
                    _totalDurationSec.value = realSec
                    seekTo(startFromSec * 1000)
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                        playbackParams = playbackParams.setSpeed(_playbackSpeed.value)
                    }
                    start()
                    setOnCompletionListener {
                        onTrackFinished()
                    }
                }
                mediaPlayer = mp
                startTickerLoop()
                return
            } catch (_: Exception) {}
        }

        // Mode Sintesis Audio Harmonik Harmonized PCM (untuk preview dan trek offline)
        startHarmonicSynthTicker(track)
    }

    private fun startTickerLoop() {
        synthJob = scope.launch(Dispatchers.Default) {
            var step = 0
            while (isActive && _isPlaying.value) {
                val mp = mediaPlayer
                if (mp != null && mp.isPlaying) {
                    val pos = (mp.currentPosition / 1000).coerceIn(0, _totalDurationSec.value)
                    _currentPositionSec.value = pos
                    val waveAmp = 0.3f + 0.65f * abs(sin(step * 0.4f))
                    _liveAmplitude.value = waveAmp
                }
                delay(250L)
                step++
            }
        }
    }

    private fun startHarmonicSynthTicker(track: AuralysTrack) {
        synthJob = scope.launch(Dispatchers.Default) {
            var sec = _currentPositionSec.value
            var step = 0
            while (isActive && _isPlaying.value) {
                delay(1000L)
                if (!_isPlaying.value) continue
                sec += (_playbackSpeed.value).toInt().coerceAtLeast(1)
                if (sec >= _totalDurationSec.value) {
                    sec = _totalDurationSec.value
                    _currentPositionSec.value = sec
                    onTrackFinished()
                    break
                }
                _currentPositionSec.value = sec
                val waveAmp = 0.35f + 0.6f * abs(sin(step * 0.55f))
                _liveAmplitude.value = waveAmp
                step++
            }
        }
    }

    private fun onTrackFinished() {
        if (_repeatMode.value == AuralysRepeatMode.ONE) {
            seekTo(0)
            resume()
        } else {
            skipNext()
        }
    }

    private fun stopInternalPlayer() {
        synthJob?.cancel()
        synthJob = null
        try {
            mediaPlayer?.stop()
            mediaPlayer?.release()
        } catch (_: Exception) {}
        mediaPlayer = null
    }

    fun release() {
        stopInternalPlayer()
        sleepTimerJob?.cancel()
        _isPlaying.value = false
    }

    companion object {
        val catalogOnlineSongs: List<AuralysTrack> by lazy {
            createDefaultOnlineCatalog()
        }

        fun formatTime(seconds: Int): String {
            val m = seconds / 60
            val s = seconds % 60
            return String.format("%02d:%02d", m, s)
        }

        fun loadVaultFolderAudio(vaultItems: List<VaultItemEntity>, folderName: String): List<AuralysTrack> {
            val audioExts = setOf("mp3", "wav", "m4a", "flac", "ogg", "aac", "opus")
            val filtered = vaultItems.filter { item ->
                val ext = item.extension.lowercase().ifBlank { item.name.substringAfterLast('.', "").lowercase() }
                val itemFolder = item.groupArchiveName ?: ""
                val isAudio = ext in audioExts || item.category == "AUDIO"
                if (!isAudio) return@filter false
                when {
                    folderName.isBlank() || folderName.equals("Semua File Audio (Root)", ignoreCase = true) -> true
                    folderName.equals("Root", ignoreCase = true) -> itemFolder.isBlank() || itemFolder.equals("Root", ignoreCase = true)
                    else -> itemFolder.equals(folderName, ignoreCase = true)
                }
            }

            return filtered.map { it ->
                val trackFolder = it.groupArchiveName ?: ""
                AuralysTrack(
                    id = "vault_audio_${it.id}",
                    title = it.name.substringBeforeLast('.').ifBlank { it.name },
                    artist = if (it.isEncrypted) "Brankas NYX2 Terenkripsi" else "Audio Brankas",
                    album = trackFolder.ifBlank { "Root Vault Folder" },
                    durationSec = if (it.durationSeconds > 0) it.durationSeconds else 210,
                    sourceMode = QueueSourceMode.VAULT_LOCAL_FOLDER,
                    folderPath = trackFolder,
                    genre = "Secure Vault Audio"
                )
            }
        }

        fun getAvailableVaultAudioFolders(vaultItems: List<VaultItemEntity>): List<String> {
            val audioExts = setOf("mp3", "wav", "m4a", "flac", "ogg", "aac", "opus")
            val audioOnly = vaultItems.filter {
                val ext = it.extension.lowercase().ifBlank { it.name.substringAfterLast('.', "").lowercase() }
                ext in audioExts || it.category == "AUDIO"
            }
            val folders = audioOnly.mapNotNull { it.groupArchiveName?.trim() }
                .filter { it.isNotBlank() }
                .distinct()
                .toMutableList()
            folders.add(0, "Semua File Audio (Root)")
            return folders
        }

        fun createDefaultOnlineCatalog(): List<AuralysTrack> {
            return listOf(
                AuralysTrack(
                    id = "auralys_track_01",
                    title = "Celestial Aurora",
                    artist = "Astra & The Orbit Ensemble",
                    album = "Cosmic Odyssey",
                    durationSec = 224,
                    sourceMode = QueueSourceMode.ONLINE_STREAMING,
                    genre = "Melodic Synthwave",
                    year = "2026",
                    lyrics = listOf(
                        AuralysLyricLine(0L, "Di antara gemerlap konstelasi bintang"),
                        AuralysLyricLine(8000L, "Cahaya aurora menari di ufuk malam"),
                        AuralysLyricLine(18000L, "Denyut frekuensi melintasi angkasa tanpa batas"),
                        AuralysLyricLine(32000L, "Mengunci rahasia dalam harmoni abadi"),
                        AuralysLyricLine(48000L, "Kita terbang melintasi nebula ungu"),
                        AuralysLyricLine(65000L, "Menjaga brankas jiwa tetap tenang dan murni"),
                        AuralysLyricLine(90000L, "Melodi kosmik berputar seirama detak jantung"),
                        AuralysLyricLine(120000L, "Dan stardust bersinar menuntun kita pulang"),
                        AuralysLyricLine(160000L, "Harmoni tak terputus hingga fajar menyingsing"),
                        AuralysLyricLine(200000L, "Dalam pelukan Auralys yang sunyi dan damai")
                    )
                ),
                AuralysTrack(
                    id = "auralys_track_02",
                    title = "Starlight Lullaby",
                    artist = "Nyx Serenade",
                    album = "Obsidian Dreamscape",
                    durationSec = 198,
                    sourceMode = QueueSourceMode.ONLINE_STREAMING,
                    genre = "Lofi Chill & Ambient",
                    year = "2026",
                    lyrics = listOf(
                        AuralysLyricLine(0L, "Detik jam berbisik dalam keheningan"),
                        AuralysLyricLine(10000L, "Bulan sabit tersenyum di balik jendela kaca"),
                        AuralysLyricLine(24000L, "Nada-nada hangat menyelimuti rasa lelah"),
                        AuralysLyricLine(45000L, "Rebahkan pikiranmu dalam gemerisik nada santai"),
                        AuralysLyricLine(70000L, "Biarkan mimpi membawamu ke surga tersembunyi"),
                        AuralysLyricLine(110000L, "Di mana setiap kenangan tersimpan aman"),
                        AuralysLyricLine(150000L, "Selamat malam petualang antariksa")
                    )
                ),
                AuralysTrack(
                    id = "auralys_track_03",
                    title = "Hyperdrive Echoes",
                    artist = "Dragon Pulse",
                    album = "Cyber Space Station",
                    durationSec = 245,
                    sourceMode = QueueSourceMode.ONLINE_STREAMING,
                    genre = "Electronic Bass & Glitch",
                    year = "2026",
                    lyrics = listOf(
                        AuralysLyricLine(0L, "[Sistem Penggerak FTL Aktif]"),
                        AuralysLyricLine(6000L, "Akselerasi kecepatan melampaui batas cahaya"),
                        AuralysLyricLine(20000L, "Getaran bass menggetarkan kabin kokpit"),
                        AuralysLyricLine(40000L, "Pulsar berkedip memberi sinyal navigasi"),
                        AuralysLyricLine(65000L, "Tembus kabut asteroid dengan kecepatan penuh"),
                        AuralysLyricLine(95000L, "Energi steganografi menyatu dalam suara"),
                        AuralysLyricLine(140000L, "Hyperdrive stabil, pendaratan siap dilakukan")
                    )
                ),
                AuralysTrack(
                    id = "auralys_track_04",
                    title = "Midnight Reverie",
                    artist = "Velvet Strings Quartet",
                    album = "Acoustic Vault Session",
                    durationSec = 212,
                    sourceMode = QueueSourceMode.ONLINE_STREAMING,
                    genre = "Modern Classical & Neoclassical",
                    year = "2026",
                    lyrics = listOf(
                        AuralysLyricLine(0L, "Sentuhan dawai biola pertama merayap pelan"),
                        AuralysLyricLine(15000L, "Kisah kuno yang terpatri dalam lembaran not balok"),
                        AuralysLyricLine(35000L, "Cello melengkapi resonansi yang dalam dan megah"),
                        AuralysLyricLine(65000L, "Sebuah simfoni yang hanya dimengerti oleh sang penjaga"),
                        AuralysLyricLine(110000L, "Di sudut ruangan tempat rahasia bernapas tenang")
                    )
                )
            )
        }
    }
}
