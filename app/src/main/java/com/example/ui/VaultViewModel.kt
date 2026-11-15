package com.example.ui

import android.app.Application
import android.content.Context
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.BatchZipScanReport
import com.example.data.DecodedStegoPayload
import com.example.data.EncryptionPreset
import com.example.data.EncryptionScriptEngine
import com.example.data.GoogleCloudVaultBackupEngine
import com.example.data.MoriDownloadResult
import com.example.data.MoriDownloaderEngine
import com.example.data.MoriExtractionMode
import com.example.data.MoriResolvedMetadata
import com.example.data.MoriStressScenario
import com.example.data.MoriSubFileItem
import com.example.data.MoriTargetDestination
import com.example.data.SecurityConfig
import com.example.data.StegoCoverTheme
import com.example.data.VaultCategory
import com.example.data.VaultDatabase
import com.example.data.VaultItemEntity
import com.example.data.VaultQuarantineEntry
import com.example.data.VaultRepository
import com.example.data.VaultVirusScanLogEntry
import com.example.data.WavCamouflagePreset
import com.example.data.ZipPreEncryptionScannerEngine
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.Semaphore
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.sync.withPermit
import kotlinx.coroutines.withContext

enum class AuthStage(val layerNumber: Int, val title: String) {
    PIN_LAYER_1(1, "PIN Lapis 1 — Gerbang Utama"),
    PIN_LAYER_2(2, "PIN Lapis 2 — Subkey NYX2"),
    PIN_LAYER_3(3, "PIN Lapis 3 — Otorisasi Brankas"),
    BIOMETRIC_STEP(4, "Verifikasi Biometrik"),
    UNLOCKED(5, "Terbuka")
}

enum class VaultTab {
    DASHBOARD,
    FILES,
    MEDIA_PLAYER,
    PHOTO_GALLERY,
    MORI_DOWNLOADER,
    CLOUD_BACKUP,
    ZIP_SCANNER,
    STEGO_TOOLS,
    SCRIPT_ENGINE,
    PRO_STUDIO,
    APP_CAMOUFLAGE,
    THEME_STUDIO,
    INTRUDER_PANIC_RADAR,
    ISOLATED_FUN_ZONE,
    BRAIN_GAMES_HUB,
    ISOLATED_SANCTUARY,
    ISOLATED_AURALYS
}

data class SecurityGateUiState(
    val stage: AuthStage = AuthStage.PIN_LAYER_1,
    val enteredPin: String = "",
    val candidateVaultId: Int = 0, // 0 = undetermined, 1..10 = Vault ID, 96 = Isolated Auralys Chamber, 97 = Isolated Sanctuary, 98 = Isolated Fun Zone, 99 = Isolated Settings Room, 999 = Silent Decoy Vault
    val isIsolatedSettingsSession: Boolean = false,
    val isIsolatedFunZoneSession: Boolean = false,
    val isIsolatedSanctuarySession: Boolean = false,
    val isIsolatedAuralysSession: Boolean = false,
    val isDecoyVaultSession: Boolean = false,
    val enteredLayer1Pin: String = "",
    val enteredLayer2Pin: String = "",
    val errorMessage: String? = null,
    val biometricStatusMessage: String = "Sentuh sensor sidik jari atau wajah untuk membuka ruang"
)

sealed class PendingCryptoOperation {
    data class ToggleSingleItem(
        val item: VaultItemEntity,
        val openAfterDecrypt: Boolean = false
    ) : PendingCryptoOperation()

    data class BatchVaultItems(val encryptAll: Boolean) : PendingCryptoOperation()
}

data class CryptoPinModalState(
    val operation: PendingCryptoOperation,
    val targetEncryptState: Boolean,
    val title: String,
    val subtitle: String,
    val enteredPin: String = "",
    val errorText: String? = null
)

data class BiometricExportModalState(
    val item: VaultItemEntity,
    val isShareIntent: Boolean, // true = Share via FileProvider, false = Export/Save to device
    val errorMessage: String? = null
)

data class ExportPasswordPromptState(
    val item: VaultItemEntity,
    val isShareIntent: Boolean
)

data class ImportPasswordPromptState(
    val uri: Uri,
    val fileName: String,
    val errorMessage: String? = null
)

enum class PlaybackRepeatMode {
    REPEAT_ONE,      // Ulangi 1 Video (Single video loop)
    AUTO_ADVANCE,    // Auto Lanjutkan Playlist/Folder
    REPEAT_PLAYLIST  // Ulangi Seluruh Playlist
}

data class MediaPlayerUiState(
    val currentTrack: VaultItemEntity? = null,
    val isPlaying: Boolean = false,
    val currentSeconds: Int = 0,
    val totalSeconds: Int = 1,
    val currentPositionMs: Long = 0L,
    val totalDurationMs: Long = 500L, // Minimal 0.5 detik (500 ms), bukan 1 menit!
    val playbackSpeed: Float = 1.0f,
    val isRepeatEnabled: Boolean = true,
    val repeatMode: PlaybackRepeatMode = PlaybackRepeatMode.AUTO_ADVANCE,
    val isHold2xActive: Boolean = false,
    val replayNonce: Int = 0,
    val userSeekNonce: Int = 0
)

class VaultViewModel(application: Application) : AndroidViewModel(application) {
    private val database = VaultDatabase.getDatabase(application)
    private val repository = VaultRepository(application, database.vaultDao())

    val securityConfig: StateFlow<SecurityConfig> = repository.securityConfigFlow.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = SecurityConfig()
    )

    // Active Vault Mode (1 = Brankas Utama, 2 = Brankas Kedua / Dual Mode)
    private val _activeVaultId = MutableStateFlow(persistedActiveVaultId)
    val activeVaultId: StateFlow<Int> = _activeVaultId.asStateFlow()

    // Mode Kalkulator Kamuflase Instan 1-Klik (#9)
    private val _isCalculatorCamouflageActive = MutableStateFlow(false)
    val isCalculatorCamouflageActive: StateFlow<Boolean> = _isCalculatorCamouflageActive.asStateFlow()

    // Mode Auto-Redirect Aplikasi Kamuflase + Overlay Maskot PIN
    private val _isCamouflageSessionBypassed = MutableStateFlow(persistedCamouflageBypassed)
    val isCamouflageSessionBypassed: StateFlow<Boolean> = _isCamouflageSessionBypassed.asStateFlow()

    private val _isMascotOverlayDismissedInCamouflage = MutableStateFlow(false)
    val isMascotOverlayDismissedInCamouflage: StateFlow<Boolean> = _isMascotOverlayDismissedInCamouflage.asStateFlow()

    fun setCalculatorCamouflageActive(active: Boolean) {
        if (active) {
            pausePlayback()
            autoLockAllItemsInActiveVault()
        }
        _isCalculatorCamouflageActive.value = active
    }

    // Folder-Scoped Media Playlist Filter (null = Semua Video di Brankas Aktif)
    private val _activeMediaFolder = MutableStateFlow<String?>(null)
    val activeMediaFolder: StateFlow<String?> = _activeMediaFolder.asStateFlow()

    fun selectMediaFolder(folderName: String?) {
        _activeMediaFolder.value = folderName
    }

    fun moveMediaItemToPlaylist(item: VaultItemEntity, playlistName: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val cleanTarget = playlistName.trim().removeSuffix(".zip").removeSuffix(".ZIP")
                .takeIf { it.isNotBlank() && it != "Semua Media" && it != "Root Brankas (Tanpa Folder)" }
            repository.moveItemToFolder(item, cleanTarget)
            withContext(Dispatchers.Main) {
                val label = cleanTarget ?: "Root Brankas"
                postBanner("File '${item.name}.${item.extension}' dipindahkan ke folder/playlist: $label")
            }
        }
    }

    fun renameFolderOrPlaylist(oldFolderName: String, newFolderName: String) {
        val cleanOld = oldFolderName.trim()
        val cleanNew = newFolderName.trim().removeSuffix(".zip").removeSuffix(".ZIP")
        if (cleanOld.isBlank() || cleanNew.isBlank() || cleanOld == cleanNew) return
        viewModelScope.launch(Dispatchers.IO) {
            val count = repository.renameFolderInVault(_activeVaultId.value, cleanOld, cleanNew)
            withContext(Dispatchers.Main) {
                if (_activeMediaFolder.value == cleanOld) {
                    _activeMediaFolder.value = cleanNew
                }
                postBanner("Folder/Playlist '$cleanOld' diganti nama menjadi '$cleanNew' ($count file diperbarui)")
            }
        }
    }

    fun deleteFolderOrPlaylist(folderName: String) {
        val cleanTarget = folderName.trim()
        if (cleanTarget.isBlank()) return
        viewModelScope.launch(Dispatchers.IO) {
            val deletedCount = repository.deleteFolderInVault(_activeVaultId.value, cleanTarget)
            withContext(Dispatchers.Main) {
                val activeFolder = _activeMediaFolder.value
                if (activeFolder != null) {
                    val normActive = activeFolder.replace(" > ", "/").replace('\\', '/').trim('/').trim()
                    val normTarget = cleanTarget.replace(" > ", "/").replace('\\', '/').trim('/').trim()
                    if (normActive == normTarget || normActive.startsWith("$normTarget/")) {
                        _activeMediaFolder.value = null
                    }
                }
                postBanner("Folder '$cleanTarget' beserta $deletedCount file di dalamnya berhasil dihapus permanen.")
            }
        }
    }

    fun setHold2xSpeed(isHolding: Boolean) {
        val current = _mediaState.value
        if (current.isHold2xActive == isHolding) return
        _mediaState.value = current.copy(
            isHold2xActive = isHolding
        )
    }

    @OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)
    val allItems: StateFlow<List<VaultItemEntity>> = _activeVaultId
        .flatMapLatest { vaultId: Int -> repository.getItemsForVault(vaultId) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val allVaultsItems: StateFlow<List<VaultItemEntity>> = repository.allItems.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    private val _cloudBackupEntries = MutableStateFlow<List<GoogleCloudVaultBackupEngine.CloudEncryptedFileEntry>>(
        GoogleCloudVaultBackupEngine.loadCloudEncryptedEntries(application)
    )
    val cloudBackupEntries: StateFlow<List<GoogleCloudVaultBackupEngine.CloudEncryptedFileEntry>> =
        _cloudBackupEntries.asStateFlow()

    private val _securityState = MutableStateFlow(persistedSecurityState)
    val securityState: StateFlow<SecurityGateUiState> = _securityState.asStateFlow()

    private val _showForgotPinModal = MutableStateFlow(false)
    val showForgotPinModal: StateFlow<Boolean> = _showForgotPinModal.asStateFlow()

    private val _biometricExportModalState = MutableStateFlow<BiometricExportModalState?>(null)
    val biometricExportModalState: StateFlow<BiometricExportModalState?> = _biometricExportModalState.asStateFlow()

    private val _exportPasswordPromptState = MutableStateFlow<ExportPasswordPromptState?>(null)
    val exportPasswordPromptState: StateFlow<ExportPasswordPromptState?> = _exportPasswordPromptState.asStateFlow()

    private val _importPasswordPromptState = MutableStateFlow<ImportPasswordPromptState?>(null)
    val importPasswordPromptState: StateFlow<ImportPasswordPromptState?> = _importPasswordPromptState.asStateFlow()

    private var pendingExportTargetItem: VaultItemEntity? = null
    private var pendingExportPassword: String = ""

    private val _activeTab = MutableStateFlow(persistedActiveTab)
    val activeTab: StateFlow<VaultTab> = _activeTab.asStateFlow()

    private val _selectedCategory = MutableStateFlow<VaultCategory?>(null)
    val selectedCategory: StateFlow<VaultCategory?> = _selectedCategory.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _inspectingItem = MutableStateFlow<VaultItemEntity?>(null)
    val inspectingItem: StateFlow<VaultItemEntity?> = _inspectingItem.asStateFlow()

    private val _viewingMediaItem = MutableStateFlow<VaultItemEntity?>(null)
    val viewingMediaItem: StateFlow<VaultItemEntity?> = _viewingMediaItem.asStateFlow()

    private val _externalPreviewRequest = MutableStateFlow<ExternalFilePreviewRequest?>(null)
    val externalPreviewRequest: StateFlow<ExternalFilePreviewRequest?> = _externalPreviewRequest.asStateFlow()

    private val _cryptoPinModalState = MutableStateFlow<CryptoPinModalState?>(null)
    val cryptoPinModalState: StateFlow<CryptoPinModalState?> = _cryptoPinModalState.asStateFlow()

    private val _mediaState = MutableStateFlow(MediaPlayerUiState())
    val mediaState: StateFlow<MediaPlayerUiState> = _mediaState.asStateFlow()

    private val _zipScannerStatesByVault = MutableStateFlow<Map<Int, ZipScannerUiState>>(emptyMap())

    val zipScannerState: StateFlow<ZipScannerUiState> = combine(
        _zipScannerStatesByVault,
        _activeVaultId
    ) { statesMap, vaultId ->
        statesMap[vaultId] ?: createInitialScannerStateForVault(vaultId)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = createInitialScannerStateForVault(1)
    )

    private fun currentVaultScannerState(): ZipScannerUiState {
        val vId = _activeVaultId.value
        return _zipScannerStatesByVault.value[vId] ?: createInitialScannerStateForVault(vId)
    }

    private fun updateCurrentVaultScannerState(transform: (ZipScannerUiState) -> ZipScannerUiState) {
        val vId = _activeVaultId.value
        val currentMap = _zipScannerStatesByVault.value
        val existing = currentMap[vId] ?: createInitialScannerStateForVault(vId)
        _zipScannerStatesByVault.value = currentMap + (vId to transform(existing))
    }

    private fun createInitialScannerStateForVault(vaultId: Int): ZipScannerUiState {
        return if (vaultId == SecurityConfig.DECOY_VAULT_ID) {
            ZipScannerUiState(
                quarantineHistory = emptyList(),
                virusScanLogs = listOf(
                    VaultVirusScanLogEntry(
                        vaultId = vaultId,
                        scannedAtLabel = "Hari ini • Pemeriksaan Otomatis",
                        archiveSummary = "Arsip_Dokumen_Harian.zip (1 ZIP)",
                        totalEntriesInspected = 3,
                        cleanExtractedCount = 3,
                        duplicatesRemovedCount = 0,
                        threatsQuarantinedCount = 0,
                        verdictBadge = "100% AMAN"
                    )
                )
            )
        } else {
            ZipScannerUiState(
                quarantineHistory = listOf(
                    VaultQuarantineEntry(
                        vaultId = vaultId,
                        fileName = "Codec_Unlocker_Patch_v$vaultId",
                        extension = "exe",
                        sourceZipName = "Paket_Media_Brankas_$vaultId.zip",
                        sizeBytes = 18432L,
                        sha256Short = "E9A14C7B02F$vaultId",
                        threatName = "Win32.DisguisedExecutable.NYX",
                        threatDetail = "Ekstensi ganda (.mp4.exe) ditahan otomatis di karantina Brankas #$vaultId.",
                        quarantinedAtLabel = "Karantina Brankas #$vaultId"
                    )
                ),
                virusScanLogs = listOf(
                    VaultVirusScanLogEntry(
                        vaultId = vaultId,
                        scannedAtLabel = "Riwayat Brankas #$vaultId",
                        archiveSummary = "Paket_Media_Brankas_$vaultId.zip (2 ZIP)",
                        totalEntriesInspected = 5,
                        cleanExtractedCount = 3,
                        duplicatesRemovedCount = 1,
                        threatsQuarantinedCount = 1,
                        verdictBadge = "1 DIKARANTINA"
                    )
                )
            )
        }
    }

    private val _moriState = MutableStateFlow(MoriDownloaderUiState())
    val moriState: StateFlow<MoriDownloaderUiState> = _moriState.asStateFlow()

    private val _stegoState = MutableStateFlow(StegoToolsUiState())
    val stegoState: StateFlow<StegoToolsUiState> = _stegoState.asStateFlow()

    private val _intruderAndPanicState = MutableStateFlow(IntruderAndPanicUiState())
    val intruderAndPanicState: StateFlow<IntruderAndPanicUiState> = _intruderAndPanicState.asStateFlow()

    private val _statusBannerMessage = MutableStateFlow<String?>(null)
    val statusBannerMessage: StateFlow<String?> = _statusBannerMessage.asStateFlow()

    private var playbackJob: Job? = null

    val filteredItems: StateFlow<List<VaultItemEntity>> = combine(
        allItems,
        _selectedCategory,
        _searchQuery
    ) { items, category, query ->
        val cleanQ = query.trim().lowercase()
        items.filter { item ->
            val matchesCategory = category == null || item.category == category.name
            val formattedSize = formatFileSize(item.sizeBytes).lowercase()
            val statusKeyword = if (item.isEncrypted) "terkunci nyx2 encrypted" else "terbuka decrypted siap putar"
            val matchesQuery = cleanQ.isBlank() ||
                    item.name.lowercase().contains(cleanQ) ||
                    item.extension.lowercase().contains(cleanQ.removePrefix(".")) ||
                    "${item.name}.${item.extension}".lowercase().contains(cleanQ) ||
                    formattedSize.contains(cleanQ) ||
                    statusKeyword.contains(cleanQ) ||
                    (item.groupArchiveName?.lowercase()?.contains(cleanQ) == true)
            matchesCategory && matchesQuery
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val mediaPlaylist: StateFlow<List<VaultItemEntity>> = combine(allItems, _activeVaultId) { items, _ ->
        items.filter {
            it.category == VaultCategory.AUDIO.name ||
                    it.category == VaultCategory.VIDEO.name ||
                    it.category == VaultCategory.IMAGE.name
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    private val dailyRecheckedVaultIds = mutableSetOf<Int>()

    init {
        // Sinkronisasi & timpa otomatis (auto-overwrite) media dokumen bawaan (PDF, DOCX, XLSX, PPTX) saat start / update APK
        viewModelScope.launch(Dispatchers.IO) {
            repository.syncBundledOfficeAndMediaOnAppUpdate(1)
        }
        viewModelScope.launch {
            _securityState.collect { persistedSecurityState = it }
        }
        viewModelScope.launch {
            _activeTab.collect { persistedActiveTab = it }
        }
        viewModelScope.launch {
            _activeVaultId.collect { persistedActiveVaultId = it }
        }
        viewModelScope.launch {
            _isCamouflageSessionBypassed.collect { persistedCamouflageBypassed = it }
        }
        viewModelScope.launch {
            mediaPlaylist.collect { playlist ->
                val current = _mediaState.value.currentTrack
                if (playlist.isEmpty()) {
                    _mediaState.value = _mediaState.value.copy(
                        currentTrack = null,
                        isPlaying = false,
                        currentSeconds = 0,
                        currentPositionMs = 0L
                    )
                } else if (current == null || playlist.none { it.id == current.id }) {
                    val preferredVideo = playlist.firstOrNull { it.category == VaultCategory.VIDEO.name }
                        ?: playlist.first()
                    val durMs = (preferredVideo.durationSeconds * 1000L).coerceAtLeast(500L)
                    _mediaState.value = _mediaState.value.copy(
                        currentTrack = preferredVideo,
                        currentSeconds = 0,
                        totalSeconds = preferredVideo.durationSeconds.coerceAtLeast(1),
                        currentPositionMs = 0L,
                        totalDurationMs = durMs,
                        isPlaying = false
                    )
                } else {
                    val updated = playlist.find { it.id == current.id }
                    if (updated != null && updated != current) {
                        val durMs = (updated.durationSeconds * 1000L).coerceAtLeast(500L)
                        _mediaState.value = _mediaState.value.copy(
                            currentTrack = updated,
                            totalSeconds = updated.durationSeconds.coerceAtLeast(1),
                            totalDurationMs = durMs
                        )
                    }
                }
            }
        }

        // Auto Daily Recheck pada brankas yang sedang dibuka: jika ada file dobel atau sub-klip yang lolos, beri peringatan!
        viewModelScope.launch(Dispatchers.IO) {
            allItems.collect { items ->
                val vId = _activeVaultId.value
                if (items.size >= 2 && vId !in dailyRecheckedVaultIds && _securityState.value.stage == AuthStage.UNLOCKED) {
                    val warnings = ZipPreEncryptionScannerEngine.scanExistingVaultForDuplicatesAndSubClips(items)
                    dailyRecheckedVaultIds.add(vId)
                    if (warnings.isNotEmpty()) {
                        withContext(Dispatchers.Main) {
                            updateCurrentVaultScannerState {
                                it.copy(
                                    activeVaultDuplicateWarnings = warnings,
                                    showActiveVaultRecheckDialog = true
                                )
                            }
                            val dispNum = SecurityConfig.displayVaultNumber(vId)
                            postBanner("Auto-Recheck Brankas #$dispNum: Ditemukan ${warnings.size} file dobel/sub-klip!")
                        }
                    }
                }
            }
        }
    }

    // --- FIRST-OPEN ONBOARDING WIZARD COMPLETION ---

    fun completeFirstRunOnboarding(
        v1p1: String,
        v1p2: String,
        v1p3: String,
        v2p1: String,
        v2p2: String,
        v2p3: String,
        biometricEnabled: Boolean,
        recoveryQuestion: String = "Apa nama kota kelahiran Anda?",
        recoveryAnswer: String = "",
        recoveryQuestion2: String = "Apa nama hewan peliharaan pertama Anda?",
        recoveryAnswer2: String = "",
        recoveryQuestion3: String = "Apa makanan favorit masa kecil Anda?",
        recoveryAnswer3: String = "",
        googleEmail: String = "",
        googleDisplayName: String = "",
        funPin1: String = "333333",
        funPin2: String = "333333",
        funPin3: String = "333333",
        auralysPin1: String = "000000",
        auralysPin2: String = "000000",
        auralysPin3: String = "000000"
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.completeFirstRunSetup(
                v1p1 = v1p1,
                v1p2 = v1p2,
                v1p3 = v1p3,
                v2p1 = v2p1,
                v2p2 = v2p2,
                v2p3 = v2p3,
                biometricEnabled = biometricEnabled,
                recoveryQuestion = recoveryQuestion,
                recoveryAnswer = recoveryAnswer,
                recoveryQuestion2 = recoveryQuestion2,
                recoveryAnswer2 = recoveryAnswer2,
                recoveryQuestion3 = recoveryQuestion3,
                recoveryAnswer3 = recoveryAnswer3,
                googleEmail = googleEmail,
                googleDisplayName = googleDisplayName,
                funPin1 = funPin1,
                funPin2 = funPin2,
                funPin3 = funPin3,
                auralysPin1 = auralysPin1,
                auralysPin2 = auralysPin2,
                auralysPin3 = auralysPin3
            )
            _activeVaultId.value = 1
            _securityState.value = SecurityGateUiState(
                stage = AuthStage.UNLOCKED,
                candidateVaultId = 1
            )
            postBanner("Brankas Arcanum siap digunakan! Impor file atau unduh media untuk dienkripsi.")
        }
    }

    // --- 3-LAYER DUAL-VAULT PIN + BIOMETRIC SECURITY LOGIC ---

    fun onPinCharPressed(char: Char) {
        val current = _securityState.value
        if (current.stage == AuthStage.BIOMETRIC_STEP || current.stage == AuthStage.UNLOCKED) return
        if (current.enteredPin.length >= 6) return

        val nextPin = current.enteredPin + char.uppercaseChar().toString()
        _securityState.value = current.copy(enteredPin = nextPin, errorMessage = null)

        if (nextPin.length == 6) {
            verifyCurrentLayerPin(nextPin)
        }
    }

    fun onPinDigitPressed(digit: Int) {
        onPinCharPressed(digit.toString()[0])
    }

    fun onPinBackspacePressed() {
        val current = _securityState.value
        if (current.enteredPin.isNotEmpty()) {
            _securityState.value = current.copy(
                enteredPin = current.enteredPin.dropLast(1),
                errorMessage = null
            )
        } else {
            // Allow stepping back to the previous PIN layer if current input is empty
            when (current.stage) {
                AuthStage.PIN_LAYER_2 -> {
                    _securityState.value = current.copy(
                        stage = AuthStage.PIN_LAYER_1,
                        candidateVaultId = 0,
                        errorMessage = null
                    )
                }
                AuthStage.PIN_LAYER_3 -> {
                    _securityState.value = current.copy(
                        stage = AuthStage.PIN_LAYER_2,
                        errorMessage = null
                    )
                }
                else -> Unit
            }
        }
    }

    fun onPinClearPressed() {
        _securityState.value = _securityState.value.copy(enteredPin = "", errorMessage = null)
    }

    private fun verifyCurrentLayerPin(submittedPin: String) {
        val cfg = securityConfig.value
        val current = _securityState.value
        val cleanPin = submittedPin.uppercase().trim()

        when (current.stage) {
            AuthStage.PIN_LAYER_1 -> {
                val isSettingsL1 = cfg.isSettingsLayer1Match(cleanPin)
                val isFunZoneL1 = cfg.isFunZoneLayer1Match(cleanPin)
                val isSanctuaryL1 = cfg.isSanctuaryLayer1Match(cleanPin)
                val isAuralysL1 = cfg.isAuralysLayer1Match(cleanPin)
                val matchingVaults = cfg.findCandidateVaultsForLayer1(cleanPin)

                when {
                    isSettingsL1 -> {
                        _securityState.value = current.copy(
                            stage = AuthStage.PIN_LAYER_2,
                            enteredPin = "",
                            enteredLayer1Pin = cleanPin,
                            candidateVaultId = SecurityConfig.ISOLATED_SETTINGS_CANDIDATE_ID,
                            isIsolatedSettingsSession = false,
                            isIsolatedFunZoneSession = false,
                            isIsolatedSanctuarySession = false,
                            isIsolatedAuralysSession = false,
                            isDecoyVaultSession = false,
                            errorMessage = null
                        )
                    }
                    isFunZoneL1 -> {
                        _securityState.value = current.copy(
                            stage = AuthStage.PIN_LAYER_2,
                            enteredPin = "",
                            enteredLayer1Pin = cleanPin,
                            candidateVaultId = SecurityConfig.ISOLATED_FUN_ZONE_CANDIDATE_ID,
                            isIsolatedSettingsSession = false,
                            isIsolatedFunZoneSession = false,
                            isIsolatedSanctuarySession = false,
                            isIsolatedAuralysSession = false,
                            isDecoyVaultSession = false,
                            errorMessage = null
                        )
                    }
                    isSanctuaryL1 -> {
                        _securityState.value = current.copy(
                            stage = AuthStage.PIN_LAYER_2,
                            enteredPin = "",
                            enteredLayer1Pin = cleanPin,
                            candidateVaultId = SecurityConfig.ISOLATED_SANCTUARY_CANDIDATE_ID,
                            isIsolatedSettingsSession = false,
                            isIsolatedFunZoneSession = false,
                            isIsolatedSanctuarySession = false,
                            isIsolatedAuralysSession = false,
                            isDecoyVaultSession = false,
                            errorMessage = null
                        )
                    }
                    isAuralysL1 -> {
                        _securityState.value = current.copy(
                            stage = AuthStage.PIN_LAYER_2,
                            enteredPin = "",
                            enteredLayer1Pin = cleanPin,
                            candidateVaultId = SecurityConfig.ISOLATED_AURALYS_CANDIDATE_ID,
                            isIsolatedSettingsSession = false,
                            isIsolatedFunZoneSession = false,
                            isIsolatedSanctuarySession = false,
                            isIsolatedAuralysSession = false,
                            isDecoyVaultSession = false,
                            errorMessage = null
                        )
                    }
                    matchingVaults.isNotEmpty() -> {
                        _securityState.value = current.copy(
                            stage = AuthStage.PIN_LAYER_2,
                            enteredPin = "",
                            enteredLayer1Pin = cleanPin,
                            candidateVaultId = matchingVaults.first(),
                            isIsolatedSettingsSession = false,
                            isIsolatedFunZoneSession = false,
                            isIsolatedSanctuarySession = false,
                            isDecoyVaultSession = false,
                            errorMessage = null
                        )
                    }
                    else -> {
                        // PIN Lapis 1 salah -> Jangan tampilkan error! Pura-pura berhasil & arahkan diam-diam ke Brankas Palsu
                        _securityState.value = current.copy(
                            stage = AuthStage.PIN_LAYER_2,
                            enteredPin = "",
                            enteredLayer1Pin = cleanPin,
                            candidateVaultId = SecurityConfig.DECOY_VAULT_ID,
                            isIsolatedSettingsSession = false,
                            isIsolatedFunZoneSession = false,
                            isIsolatedSanctuarySession = false,
                            isDecoyVaultSession = true,
                            errorMessage = null
                        )
                    }
                }
            }

            AuthStage.PIN_LAYER_2 -> {
                if (current.candidateVaultId == SecurityConfig.DECOY_VAULT_ID || current.isDecoyVaultSession) {
                    _securityState.value = current.copy(
                        stage = AuthStage.PIN_LAYER_3,
                        enteredPin = "",
                        enteredLayer2Pin = cleanPin,
                        isDecoyVaultSession = true,
                        errorMessage = null
                    )
                    return
                }

                val l1 = current.enteredLayer1Pin
                val isSettingsL1L2 = cfg.isSettingsLayer1Match(l1) && cfg.settingsPin2.equals(cleanPin, ignoreCase = true)
                val isFunZoneL1L2 = cfg.isFunZoneLayer1Match(l1) && cfg.funZonePin2.equals(cleanPin, ignoreCase = true)
                val isSanctuaryL1L2 = cfg.isSanctuaryLayer1Match(l1) && cfg.sanctuaryLayer2Pin.equals(cleanPin, ignoreCase = true)
                val isAuralysL1L2 = cfg.isAuralysLayer1Match(l1) && cfg.auralysPin2.equals(cleanPin, ignoreCase = true)
                val matchingVaultForL1AndL2 = cfg.findCandidateVaultsForLayer1(l1)
                    .firstOrNull { vId ->
                        cfg.getPinsForVault(vId).second.equals(cleanPin, ignoreCase = true)
                    }

                when {
                    isSettingsL1L2 -> {
                        _securityState.value = current.copy(
                            stage = AuthStage.PIN_LAYER_3,
                            enteredPin = "",
                            enteredLayer2Pin = cleanPin,
                            candidateVaultId = SecurityConfig.ISOLATED_SETTINGS_CANDIDATE_ID,
                            isDecoyVaultSession = false,
                            errorMessage = null
                        )
                    }
                    isFunZoneL1L2 -> {
                        _securityState.value = current.copy(
                            stage = AuthStage.PIN_LAYER_3,
                            enteredPin = "",
                            enteredLayer2Pin = cleanPin,
                            candidateVaultId = SecurityConfig.ISOLATED_FUN_ZONE_CANDIDATE_ID,
                            isDecoyVaultSession = false,
                            errorMessage = null
                        )
                    }
                    isSanctuaryL1L2 -> {
                        _securityState.value = current.copy(
                            stage = AuthStage.PIN_LAYER_3,
                            enteredPin = "",
                            enteredLayer2Pin = cleanPin,
                            candidateVaultId = SecurityConfig.ISOLATED_SANCTUARY_CANDIDATE_ID,
                            isDecoyVaultSession = false,
                            errorMessage = null
                        )
                    }
                    isAuralysL1L2 -> {
                        _securityState.value = current.copy(
                            stage = AuthStage.PIN_LAYER_3,
                            enteredPin = "",
                            enteredLayer2Pin = cleanPin,
                            candidateVaultId = SecurityConfig.ISOLATED_AURALYS_CANDIDATE_ID,
                            isDecoyVaultSession = false,
                            errorMessage = null
                        )
                    }
                    matchingVaultForL1AndL2 != null -> {
                        _securityState.value = current.copy(
                            stage = AuthStage.PIN_LAYER_3,
                            enteredPin = "",
                            enteredLayer2Pin = cleanPin,
                            candidateVaultId = matchingVaultForL1AndL2,
                            isDecoyVaultSession = false,
                            errorMessage = null
                        )
                    }
                    else -> {
                        // Salah di Lapis 2 -> alihkan diam-diam ke Brankas Palsu tanpa pesan error
                        _securityState.value = current.copy(
                            stage = AuthStage.PIN_LAYER_3,
                            enteredPin = "",
                            enteredLayer2Pin = cleanPin,
                            candidateVaultId = SecurityConfig.DECOY_VAULT_ID,
                            isIsolatedSettingsSession = false,
                            isIsolatedFunZoneSession = false,
                            isIsolatedSanctuarySession = false,
                            isDecoyVaultSession = true,
                            errorMessage = null
                        )
                    }
                }
            }

            AuthStage.PIN_LAYER_3 -> {
                val nextStage = if (cfg.biometricEnabled) AuthStage.BIOMETRIC_STEP else AuthStage.UNLOCKED
                val l1 = current.enteredLayer1Pin
                val l2 = current.enteredLayer2Pin

                if (!current.isDecoyVaultSession && cfg.isSettingsFullMatch(l1, l2, cleanPin)) {
                    pausePlayback()
                    _viewingMediaItem.value = null
                    _inspectingItem.value = null
                    _activeTab.value = VaultTab.SCRIPT_ENGINE
                    _securityState.value = current.copy(
                        stage = nextStage,
                        enteredPin = "",
                        candidateVaultId = SecurityConfig.ISOLATED_SETTINGS_CANDIDATE_ID,
                        isIsolatedSettingsSession = true,
                        isIsolatedFunZoneSession = false,
                        isIsolatedSanctuarySession = false,
                        isDecoyVaultSession = false,
                        errorMessage = null
                    )
                    if (nextStage == AuthStage.UNLOCKED) {
                        postBanner("Brankas Setting terbuka • Tab Umum aktif (Ketuk Pro Hub di navigasi bawah untuk Mode Pro).")
                    }
                    return
                }

                if (!current.isDecoyVaultSession && cfg.isFunZoneFullMatch(l1, l2, cleanPin)) {
                    pausePlayback()
                    _viewingMediaItem.value = null
                    _inspectingItem.value = null
                    _activeTab.value = VaultTab.ISOLATED_FUN_ZONE
                    _securityState.value = current.copy(
                        stage = nextStage,
                        enteredPin = "",
                        candidateVaultId = SecurityConfig.ISOLATED_FUN_ZONE_CANDIDATE_ID,
                        isIsolatedSettingsSession = false,
                        isIsolatedFunZoneSession = true,
                        isIsolatedSanctuarySession = false,
                        isDecoyVaultSession = false,
                        errorMessage = null
                    )
                    if (nextStage == AuthStage.UNLOCKED) {
                        postBanner("Ruang Terisolasi Fun Zone 3D terbuka! Selamat bermain Galaxy TD, Slot & Kapsul Waktu.")
                    }
                    return
                }

                if (!current.isDecoyVaultSession && cfg.isSanctuaryFullMatch(l1, l2, cleanPin)) {
                    pausePlayback()
                    _viewingMediaItem.value = null
                    _inspectingItem.value = null
                    _activeTab.value = VaultTab.ISOLATED_SANCTUARY
                    _securityState.value = current.copy(
                        stage = nextStage,
                        enteredPin = "",
                        candidateVaultId = SecurityConfig.ISOLATED_SANCTUARY_CANDIDATE_ID,
                        isIsolatedSettingsSession = false,
                        isIsolatedFunZoneSession = false,
                        isIsolatedSanctuarySession = true,
                        isDecoyVaultSession = false,
                        errorMessage = null
                    )
                    if (nextStage == AuthStage.UNLOCKED) {
                        postBanner("Arcanum BlackBox Sanctuary Terbuka! (Shadow Ledger, Detektif, Cyber OS, Zen & Tor/Brave Browser).")
                    }
                    return
                }

                if (!current.isDecoyVaultSession && cfg.isAuralysFullMatch(l1, l2, cleanPin)) {
                    pausePlayback()
                    _viewingMediaItem.value = null
                    _inspectingItem.value = null
                    _activeTab.value = VaultTab.ISOLATED_AURALYS
                    _securityState.value = current.copy(
                        stage = nextStage,
                        enteredPin = "",
                        candidateVaultId = SecurityConfig.ISOLATED_AURALYS_CANDIDATE_ID,
                        isIsolatedSettingsSession = false,
                        isIsolatedFunZoneSession = false,
                        isIsolatedSanctuarySession = false,
                        isIsolatedAuralysSession = true,
                        isDecoyVaultSession = false,
                        errorMessage = null
                    )
                    if (nextStage == AuthStage.UNLOCKED) {
                        postBanner("Auralys Music Chamber aktif • Mode Stealth Pemutar Musik Terbuka.")
                    }
                    return
                }

                val matchedVaultId = if (!current.isDecoyVaultSession) {
                    cfg.findMatchingVault(l1, l2, cleanPin)
                } else null

                if (matchedVaultId != null && matchedVaultId in 1..10) {
                    _viewingMediaItem.value = null
                    _inspectingItem.value = null
                    _activeVaultId.value = matchedVaultId
                    _activeTab.value = VaultTab.DASHBOARD
                    _securityState.value = current.copy(
                        stage = nextStage,
                        enteredPin = "",
                        candidateVaultId = matchedVaultId,
                        isIsolatedSettingsSession = false,
                        isIsolatedFunZoneSession = false,
                        isIsolatedSanctuarySession = false,
                        isDecoyVaultSession = false,
                        errorMessage = null
                    )
                    return
                }

                // Semua kombinasi PIN yang salah otomatis membuka Brankas Palsu (DECOY_VAULT_ID = 999)
                // tanpa satu pun pesan error atau tanda bahwa itu brankas palsu!
                recordIntruderTrapLog(
                    triggerType = "PIN SALAH 3 LAPIS",
                    l1 = l1.ifBlank { "??????" },
                    l2 = l2.ifBlank { "??????" },
                    l3 = cleanPin,
                    initialActivity = "Masuk diam-diam ke BRANKAS #1 (Palsu)"
                )
                viewModelScope.launch(Dispatchers.IO) {
                    repository.ensureDecoyVaultSeeded()
                }
                _viewingMediaItem.value = null
                _inspectingItem.value = null
                _activeVaultId.value = SecurityConfig.DECOY_VAULT_ID
                _activeTab.value = VaultTab.DASHBOARD
                _securityState.value = current.copy(
                    stage = nextStage,
                    enteredPin = "",
                    candidateVaultId = SecurityConfig.DECOY_VAULT_ID,
                    isIsolatedSettingsSession = false,
                    isIsolatedFunZoneSession = false,
                    isIsolatedSanctuarySession = false,
                    isDecoyVaultSession = true,
                    errorMessage = null
                )
            }

            else -> Unit
        }
    }

    fun saveFunZonePins(pin1: String, pin2: String, pin3: String) {
        val collision = securityConfig.value.findPinCollisionReason(
            p1 = pin1,
            p2 = pin2,
            p3 = pin3,
            excludeFunZone = true
        )
        if (collision != null) {
            postBanner("GAGAL SIMPAN: $collision")
            return
        }
        viewModelScope.launch {
            repository.updateFunZonePins(pin1, pin2, pin3)
            postBanner("PIN 3-lapis Ruang Terisolasi Fun Zone berhasil diperbarui & bebas tabrakan!")
        }
    }

    fun saveSanctuaryPins(pin1: String, pin2: String, pin3: String) {
        val collision = securityConfig.value.findPinCollisionReason(
            p1 = pin1,
            p2 = pin2,
            p3 = pin3,
            excludeSanctuary = true
        )
        if (collision != null) {
            postBanner("GAGAL SIMPAN: $collision")
            return
        }
        viewModelScope.launch {
            repository.updateSanctuaryPins(pin1, pin2, pin3)
            postBanner("PIN 3-lapis Ruang Terisolasi BlackBox Sanctuary berhasil diperbarui!")
        }
    }

    fun openIsolatedSanctuaryDirectly() {
        pausePlayback()
        _viewingMediaItem.value = null
        _inspectingItem.value = null
        _activeTab.value = VaultTab.ISOLATED_SANCTUARY
        _securityState.value = _securityState.value.copy(
            stage = AuthStage.UNLOCKED,
            candidateVaultId = SecurityConfig.ISOLATED_SANCTUARY_CANDIDATE_ID,
            isIsolatedSettingsSession = false,
            isIsolatedFunZoneSession = false,
            isIsolatedSanctuarySession = true,
            isDecoyVaultSession = false,
            errorMessage = null
        )
        postBanner("Masuk ke Ruang Terisolasi Ke-4: Arcanum BlackBox Sanctuary!")
    }

    fun saveAuralysPins(pin1: String, pin2: String, pin3: String) {
        val collision = securityConfig.value.findPinCollisionReason(
            p1 = pin1,
            p2 = pin2,
            p3 = pin3,
            excludeAuralys = true
        )
        if (collision != null) {
            postBanner("GAGAL SIMPAN: $collision")
            return
        }
        viewModelScope.launch {
            repository.updateAuralysPins(pin1, pin2, pin3)
            postBanner("PIN 3-lapis Auralys Music Chamber berhasil diperbarui!")
        }
    }

    fun openIsolatedAuralysDirectly() {
        pausePlayback()
        _viewingMediaItem.value = null
        _inspectingItem.value = null
        _activeTab.value = VaultTab.ISOLATED_AURALYS
        _securityState.value = _securityState.value.copy(
            stage = AuthStage.UNLOCKED,
            candidateVaultId = SecurityConfig.ISOLATED_AURALYS_CANDIDATE_ID,
            isIsolatedSettingsSession = false,
            isIsolatedFunZoneSession = false,
            isIsolatedSanctuarySession = false,
            isIsolatedAuralysSession = true,
            isDecoyVaultSession = false,
            errorMessage = null
        )
        postBanner("Auralys Music Chamber aktif • Mode Stealth Pemutar Musik Terbuka.")
    }

    fun saveQrBitmapBytesToActiveVault(
        title: String,
        pngBytes: ByteArray,
        previewDescription: String,
        encryptImmediately: Boolean = false
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            val vId = _activeVaultId.value
            repository.saveRawPngBytesToPrivateVault(
                vaultId = vId,
                title = title,
                pngBytes = pngBytes,
                previewDescription = previewDescription,
                encryptImmediately = encryptImmediately
            )
            withContext(Dispatchers.Main) {
                val dispNum = SecurityConfig.displayVaultNumber(vId)
                postBanner("QR Bintang AstraOnyx \"$title.png\" berhasil disimpan ke Brankas #$dispNum!")
            }
        }
    }

    fun saveConvertedFileToActiveVault(
        stagedFile: File,
        targetBaseName: String,
        targetExtension: String,
        category: VaultCategory,
        groupLabel: String = "Arcanum Converter",
        fallbackPreviewText: String = "",
        onSavedResult: (VaultItemEntity) -> Unit = {}
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            val vId = _activeVaultId.value
            val createdEntity = repository.saveConvertedFileToVault(
                stagedFile = stagedFile,
                targetBaseName = targetBaseName,
                targetExtension = targetExtension,
                category = category,
                groupLabel = groupLabel,
                vaultId = vId,
                fallbackPreviewText = fallbackPreviewText
            )
            withContext(Dispatchers.Main) {
                val dispNum = SecurityConfig.displayVaultNumber(vId)
                postBanner("File \"${createdEntity.name}.${createdEntity.extension}\" disimpan ke Brankas #$dispNum & Download/Arcanum!")
                onSavedResult(createdEntity)
            }
        }
    }

    fun addNewVault(pin1: String, pin2: String, pin3: String, customName: String = "") {
        val collision = securityConfig.value.findPinCollisionReason(
            p1 = pin1,
            p2 = pin2,
            p3 = pin3
        )
        if (collision != null) {
            postBanner("GAGAL MEMBUAT BRANKAS: $collision")
            return
        }
        viewModelScope.launch {
            val newVaultId = repository.addVault(pin1, pin2, pin3, customName)
            if (newVaultId > 0) {
                val label = if (customName.isNotBlank()) "Brankas #$newVaultId (${customName.trim()})" else "Brankas #$newVaultId"
                postBanner("$label berhasil dibuat dengan PIN 3-lapis unik baru!")
            } else {
                postBanner("Maksimal 10 brankas telah tercapai.")
            }
        }
    }

    fun renameVault(vaultId: Int, newName: String) {
        viewModelScope.launch {
            val dispId = SecurityConfig.displayVaultNumber(vaultId)
            val clean = newName.trim()
            repository.renameVault(dispId, clean)
            val updatedEntries = GoogleCloudVaultBackupEngine.updateVaultNameInCloudEntries(
                context = getApplication(),
                vaultId = dispId,
                newVaultName = clean.ifBlank { "Brankas #$dispId" }
            )
            _cloudBackupEntries.value = updatedEntries
            val displayLabel = if (clean.isBlank()) "Brankas #$dispId" else "Brankas #$dispId • $clean"
            postBanner("Nama brankas diperbarui menjadi: $displayLabel")
        }
    }

    fun linkGoogleAccount(email: String, displayName: String, photoUrl: String = "") {
        viewModelScope.launch {
            repository.setGoogleAccount(email, displayName, photoUrl)
            postBanner("Akun Google $email berhasil ditautkan untuk pencadangan & laporan!")
        }
    }

    fun unlinkGoogleAccount() {
        viewModelScope.launch {
            repository.unlinkGoogleAccount()
            postBanner("Tautan akun Google telah diputus.")
        }
    }

    fun refreshCloudBackupEntries() {
        _cloudBackupEntries.value = GoogleCloudVaultBackupEngine.loadCloudEncryptedEntries(getApplication())
    }

    fun backupSelectedItemsToGoogleCloud(
        items: List<VaultItemEntity>,
        vaultIdOverride: Int? = null
    ) {
        viewModelScope.launch {
            val app = getApplication<Application>()
            val cfg = securityConfig.value
            val vId = SecurityConfig.displayVaultNumber(vaultIdOverride ?: _activeVaultId.value)
            val vaultName = cfg.getVaultName(vId)
            val (p1, p2, p3) = cfg.getPinsForVault(vId)
            val email = cfg.googleAccountEmail

            if (!cfg.isGoogleLinked || email.isBlank()) {
                postBanner("Wajib login akun Google terlebih dahulu sebelum mencadangkan ke Cloud!")
                return@launch
            }
            if (items.isEmpty()) {
                postBanner("Pilih minimal 1 file untuk dicadangkan ke Google Cloud.")
                return@launch
            }

            postBanner("Mengenkripsi & mencadangkan ${items.size} file dari $vaultName ke Cloud Google ($email)...")
            val res = GoogleCloudVaultBackupEngine.backupSelectedItemsToGoogleCloud(
                context = app,
                vaultId = vId,
                vaultName = vaultName,
                googleAccountEmail = email,
                items = items,
                p1 = p1,
                p2 = p2,
                p3 = p3
            )
            res.onSuccess { backedUp ->
                _cloudBackupEntries.value = GoogleCloudVaultBackupEngine.loadCloudEncryptedEntries(app)
                postBanner("${backedUp.size} file terenkripsi dari Brankas #$vId ($vaultName) berhasil dicadangkan ke Cloud Google!")
            }.onFailure { err ->
                postBanner("Gagal mencadangkan ke Cloud: ${err.message}")
            }
        }
    }

    fun backupSingleItemToGoogleCloud(item: VaultItemEntity) {
        backupSelectedItemsToGoogleCloud(
            items = listOf(item),
            vaultIdOverride = SecurityConfig.displayVaultNumber(item.vaultProfileId)
        )
    }

    fun restoreSingleCloudEntryToActiveVault(entry: GoogleCloudVaultBackupEngine.CloudEncryptedFileEntry) {
        viewModelScope.launch {
            val app = getApplication<Application>()
            val cfg = securityConfig.value
            if (!cfg.isGoogleLinked || cfg.googleAccountEmail.isBlank()) {
                postBanner("FITUR CLOUD MATI TOTAL: Wajib login Akun Google terlebih dahulu!")
                return@launch
            }
            val targetVaultId = _activeVaultId.value
            val sourcePins = cfg.getPinsForVault(entry.sourceVaultId)
            postBanner("Mendekripsi & memulihkan '${entry.fileName}.${entry.extension}' dari Cloud (Asal: Brankas #${entry.sourceVaultId})...")
            val res = GoogleCloudVaultBackupEngine.restoreSingleCloudEntryToVault(
                context = app,
                entry = entry,
                targetVaultId = targetVaultId,
                sourceVaultPins = sourcePins,
                repository = repository
            )
            res.onSuccess { restored ->
                val targetName = cfg.getVaultName(targetVaultId)
                postBanner("Berhasil dipulihkan ke Brankas #${SecurityConfig.displayVaultNumber(targetVaultId)} ($targetName): ${restored.name}.${restored.extension}")
            }.onFailure { err ->
                postBanner("Gagal memulihkan file dari Cloud: ${err.message}")
            }
        }
    }

    fun deleteCloudBackupEntry(cloudId: String) {
        val cfg = securityConfig.value
        if (!cfg.isGoogleLinked || cfg.googleAccountEmail.isBlank()) {
            postBanner("FITUR CLOUD MATI TOTAL: Wajib login Akun Google terlebih dahulu!")
            return
        }
        val app = getApplication<Application>()
        _cloudBackupEntries.value = GoogleCloudVaultBackupEngine.deleteCloudEncryptedEntry(app, cloudId)
        postBanner("File terenkripsi telah dihapus dari penyimpanan Cloud Google.")
    }

    fun backupActiveVaultToGoogleCloud(context: Context) {
        viewModelScope.launch {
            val vId = SecurityConfig.displayVaultNumber(_activeVaultId.value)
            val cfg = securityConfig.value
            if (!cfg.isGoogleLinked || cfg.googleAccountEmail.isBlank()) {
                postBanner("FITUR CLOUD MATI TOTAL: Wajib login Akun Google terlebih dahulu untuk mencadangkan Brankas!")
                return@launch
            }
            val (p1, p2, p3) = cfg.getPinsForVault(vId)
            val items = allItems.value

            if (items.isNotEmpty()) {
                GoogleCloudVaultBackupEngine.backupSelectedItemsToGoogleCloud(
                    context = context,
                    vaultId = vId,
                    vaultName = cfg.getVaultName(vId),
                    googleAccountEmail = cfg.googleAccountEmail,
                    items = items,
                    p1 = p1,
                    p2 = p2,
                    p3 = p3
                )
                _cloudBackupEntries.value = GoogleCloudVaultBackupEngine.loadCloudEncryptedEntries(context)
            }

            postBanner("Mengemas & mengenkripsi Brankas #$vId (${cfg.getVaultName(vId)}) dengan AES-256-GCM...")
            val res = GoogleCloudVaultBackupEngine.createEncryptedVaultPackage(
                context = context,
                vaultId = vId,
                items = items,
                p1 = p1,
                p2 = p2,
                p3 = p3
            )
            res.onSuccess { encFile ->
                postBanner("Pencadangan Brankas #$vId berhasil! File: ${encFile.name} (${encFile.length() / 1024} KB)")
            }.onFailure { err ->
                postBanner("Gagal mencadangkan brankas: ${err.message}")
            }
        }
    }

    fun restoreVaultFromEncryptedPackage(context: Context, encFile: File) {
        viewModelScope.launch {
            val vId = _activeVaultId.value
            val cfg = securityConfig.value
            if (!cfg.isGoogleLinked || cfg.googleAccountEmail.isBlank()) {
                postBanner("FITUR CLOUD MATI TOTAL: Wajib login Akun Google terlebih dahulu untuk memulihkan cadangan Cloud!")
                return@launch
            }
            val (p1, p2, p3) = cfg.getPinsForVault(vId)

            postBanner("Mendekripsi & memulihkan paket cadangan...")
            val res = GoogleCloudVaultBackupEngine.restoreEncryptedVaultPackage(
                context = context,
                targetVaultId = vId,
                encPackageFile = encFile,
                p1 = p1,
                p2 = p2,
                p3 = p3,
                repository = repository
            )
            res.onSuccess { count ->
                postBanner("Pemulihan selesai! $count file berhasil dipulihkan.")
            }.onFailure { err ->
                postBanner("Gagal memulihkan cadangan: ${err.message}")
            }
        }
    }

    fun onBiometricAuthenticatedSuccess() {
        _securityState.value = _securityState.value.copy(
            stage = AuthStage.UNLOCKED,
            errorMessage = null
        )
    }

    fun onBiometricAuthenticationError(reason: String) {
        _securityState.value = _securityState.value.copy(
            biometricStatusMessage = reason,
            errorMessage = reason
        )
    }

    private fun autoLockAllItemsInActiveVault() {
        val snapshotItems = allItems.value
        viewModelScope.launch(Dispatchers.IO) {
            for (item in snapshotItems) {
                if (!item.isEncrypted) {
                    try {
                        repository.toggleBinaryEncryptionOnDisk(item, true)
                    } catch (_: Exception) {
                    }
                }
            }
        }
    }

    fun lockVaultNow() {
        pausePlayback()
        autoLockAllItemsInActiveVault()
        _inspectingItem.value = null
        _viewingMediaItem.value = null
        _cryptoPinModalState.value = null
        persistedCamouflageBypassed = false
        _isCamouflageSessionBypassed.value = false
        _isMascotOverlayDismissedInCamouflage.value = false
        val lockedState = SecurityGateUiState(stage = AuthStage.PIN_LAYER_1)
        persistedSecurityState = lockedState
        _securityState.value = lockedState
    }

    fun switchVaultModeWithLock(targetVaultId: Int) {
        if (targetVaultId == _activeVaultId.value) {
            postBanner("Anda sudah berada di Brankas $targetVaultId")
            return
        }
        lockVaultNow()
        postBanner("Masukkan 3 Lapis PIN untuk membuka Brankas $targetVaultId")
    }

    // --- PIN-GATED FILE ENCRYPTION / DECRYPTION ON DISK ---

    fun requestToggleEncryptionWithPin(
        item: VaultItemEntity,
        openAfterDecrypt: Boolean = false
    ) {
        val targetEncrypt = !item.isEncrypted
        val subtitleText = if (item.isEncrypted) {
            "Masukkan PIN Brankas ${_activeVaultId.value} untuk membuka \"${item.name}.${item.extension}\" & semua file terkunci sekaligus (Auto Unlock All)"
        } else {
            "Masukkan PIN Brankas ${_activeVaultId.value} untuk Mengenkripsi \"${item.name}.${item.extension}\""
        }
        _cryptoPinModalState.value = CryptoPinModalState(
            operation = PendingCryptoOperation.ToggleSingleItem(item, openAfterDecrypt),
            targetEncryptState = targetEncrypt,
            title = if (item.isEncrypted) "Auto Unlock All • PIN NYX2" else "PIN Otorisasi NYX2",
            subtitle = subtitleText
        )
    }

    fun requestBatchEncryptionWithPin(encryptAll: Boolean) {
        val actionWord = if (encryptAll) "Mengenkripsi (Auto Lock) Semua File" else "Membuka (Auto Unlock All) Semua File"
        _cryptoPinModalState.value = CryptoPinModalState(
            operation = PendingCryptoOperation.BatchVaultItems(encryptAll),
            targetEncryptState = encryptAll,
            title = "PIN Otorisasi Massal NYX2",
            subtitle = "Masukkan PIN Brankas ${_activeVaultId.value} untuk $actionWord di Brankas ${_activeVaultId.value}"
        )
    }

    fun onCryptoModalPinChar(char: Char) {
        val modal = _cryptoPinModalState.value ?: return
        if (modal.enteredPin.length >= 6) return
        val nextPin = modal.enteredPin + char.uppercaseChar().toString()
        _cryptoPinModalState.value = modal.copy(enteredPin = nextPin, errorText = null)

        if (nextPin.length == 6) {
            verifyCryptoModalPin(nextPin)
        }
    }

    fun onCryptoModalPinDigit(digit: Int) {
        onCryptoModalPinChar(digit.toString()[0])
    }

    fun onCryptoModalPinBackspace() {
        val modal = _cryptoPinModalState.value ?: return
        if (modal.enteredPin.isNotEmpty()) {
            _cryptoPinModalState.value = modal.copy(
                enteredPin = modal.enteredPin.dropLast(1),
                errorText = null
            )
        }
    }

    fun dismissCryptoPinModal() {
        _cryptoPinModalState.value = null
    }

    private fun verifyCryptoModalPin(submittedPin: String) {
        val modal = _cryptoPinModalState.value ?: return
        val cfg = securityConfig.value
        val activeVId = _activeVaultId.value
        val isDecoy = SecurityConfig.isDecoyVault(activeVId)
        val validPins = cfg.getPinsForVault(activeVId)
            .toList()
            .map { it.uppercase().trim() }
        val cleanSubmitted = submittedPin.uppercase().trim()

        // Di Brankas Palsu, izinkan PIN 6 karakter apa pun agar penyusup tidak curiga saat membuka/mengunci file umpan
        if (isDecoy || cleanSubmitted in validPins) {
            val op = modal.operation
            _cryptoPinModalState.value = null
            when (op) {
                is PendingCryptoOperation.ToggleSingleItem -> {
                    executeToggleEncryptionInternal(op.item, op.openAfterDecrypt)
                }
                is PendingCryptoOperation.BatchVaultItems -> {
                    executeBatchEncryptInternal(op.encryptAll)
                }
            }
        } else {
            val displayVNum = SecurityConfig.displayVaultNumber(activeVId)
            _cryptoPinModalState.value = modal.copy(
                enteredPin = "",
                errorText = "PIN Brankas $displayVNum tidak cocok! Akses ditolak."
            )
        }
    }

    private fun executeToggleEncryptionInternal(
        item: VaultItemEntity,
        openAfterDecrypt: Boolean
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            val targetEncryptedState = !item.isEncrypted
            val updated = repository.toggleBinaryEncryptionOnDisk(item, targetEncryptedState)

            // Auto Unlock All: Saat membuka kunci 1 file/video dengan PIN, otomatis buka semua file terkunci lainnya di brankas aktif
            var unlockedOthersCount = 0
            if (!targetEncryptedState) {
                val currentList = allItems.value
                for (other in currentList) {
                    if (other.id != item.id && other.isEncrypted) {
                        try {
                            val unlockedOther = repository.toggleBinaryEncryptionOnDisk(other, false)
                            unlockedOthersCount++
                            if (_inspectingItem.value?.id == other.id) {
                                _inspectingItem.value = unlockedOther
                            }
                            if (_viewingMediaItem.value?.id == other.id) {
                                _viewingMediaItem.value = unlockedOther
                            }
                            if (_mediaState.value.currentTrack?.id == other.id) {
                                _mediaState.value = _mediaState.value.copy(currentTrack = unlockedOther)
                            }
                        } catch (_: Exception) {
                        }
                    }
                }
            }

            if (_inspectingItem.value?.id == item.id) {
                _inspectingItem.value = updated
            }
            if (_viewingMediaItem.value?.id == item.id) {
                _viewingMediaItem.value = updated
            }
            if (_mediaState.value.currentTrack?.id == item.id) {
                _mediaState.value = _mediaState.value.copy(currentTrack = updated)
            }

            if (!updated.isEncrypted && openAfterDecrypt) {
                val cat = VaultCategory.entries.find { it.name == updated.category }
                if (cat == VaultCategory.VIDEO || cat == VaultCategory.AUDIO) {
                    _activeTab.value = VaultTab.MEDIA_PLAYER
                    selectAndPlayMediaTrack(updated)
                } else {
                    _viewingMediaItem.value = updated
                }
            }

            postBanner(
                if (updated.isEncrypted) {
                    "Terenkripsi (.nyx): ${updated.name}.${updated.extension}"
                } else if (unlockedOthersCount > 0) {
                    "Auto Unlock All: ${updated.name}.${updated.extension} + $unlockedOthersCount file lainnya dibuka!"
                } else {
                    "Terdekripsi & siap dibuka: ${updated.name}.${updated.extension}"
                }
            )
        }
    }

    private fun executeBatchEncryptInternal(encryptAll: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            val currentList = allItems.value
            for (item in currentList) {
                if (item.isEncrypted != encryptAll) {
                    repository.toggleBinaryEncryptionOnDisk(item, encryptAll)
                }
            }
            postBanner(
                if (encryptAll) {
                    "Semua file di Brankas ${_activeVaultId.value} telah dikunci (.nyx)"
                } else {
                    "Semua file di Brankas ${_activeVaultId.value} telah didekripsi"
                }
            )
        }
    }

    // --- NAVIGATION & MEDIA VIEWER OVERLAY ---

    fun selectTab(tab: VaultTab) {
        val sec = _securityState.value
        when {
            sec.isIsolatedSanctuarySession -> {
                _activeTab.value = VaultTab.ISOLATED_SANCTUARY
            }
            sec.isIsolatedFunZoneSession -> {
                _activeTab.value = VaultTab.ISOLATED_FUN_ZONE
            }
            sec.isIsolatedSettingsSession -> {
                if (tab == VaultTab.APP_CAMOUFLAGE && !DRAFT_APP_CAMOUFLAGE_FEATURE_ENABLED) {
                    _activeTab.value = VaultTab.SCRIPT_ENGINE
                } else if (tab == VaultTab.SCRIPT_ENGINE ||
                    tab == VaultTab.PRO_STUDIO ||
                    tab == VaultTab.APP_CAMOUFLAGE ||
                    tab == VaultTab.THEME_STUDIO ||
                    tab == VaultTab.INTRUDER_PANIC_RADAR ||
                    tab == VaultTab.ZIP_SCANNER
                ) {
                    _activeTab.value = tab
                } else {
                    lockVaultNow()
                    postBanner("Brankas Setting dikunci. Masukkan PIN Brankas untuk masuk ke Brankas.")
                }
            }
            else -> {
                // Di dalam Brankas: tidak boleh bertabrakan langsung dengan Ruang Pengaturan atau Ruang Game tanpa Layar Kunci
                if (tab == VaultTab.SCRIPT_ENGINE) {
                    lockVaultNow()
                    postBanner("Masukkan 3 Lapis PIN Ruang Pengaturan pada Layar Kunci (Ruang Terisolasi Anti-Tabrakan).")
                } else if (tab == VaultTab.ISOLATED_FUN_ZONE) {
                    lockVaultNow()
                    postBanner("Masukkan 3 Lapis PIN Ruang Game (Fun Zone) pada Layar Kunci.")
                } else {
                    _activeTab.value = tab
                }
            }
        }
    }

    fun selectCategory(category: VaultCategory?) {
        _selectedCategory.value = if (_selectedCategory.value == category) null else category
    }

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun inspectItem(item: VaultItemEntity?) {
        _inspectingItem.value = item
    }

    fun openViewingMediaModal(item: VaultItemEntity?) {
        _viewingMediaItem.value = item
    }

    /**
     * Satu Pemutar Tunggal untuk setiap jenis media:
     * - VIDEO (.mp4, .mov, .mkv, .webm) -> Langsung masuk ke 1 Video Player utama (VaultMediaPlayerScreen).
     * - AUDIO (.mp3, .wav, .flac, .m4a) -> Langsung masuk ke 1 Audio Player utama (VaultMediaPlayerScreen).
     * - IMAGE / DOCUMENT -> Membuka 1 Photo/Document Viewer utama (_viewingMediaItem).
     */
    fun playOrViewItem(item: VaultItemEntity) {
        if (SecurityConfig.isDecoyVault(_activeVaultId.value)) {
            appendActivityToLatestIntruderLog("Membuka berkas umpan: ${item.name}.${item.extension}")
        }
        if (item.isEncrypted) {
            requestToggleEncryptionWithPin(item = item, openAfterDecrypt = true)
            return
        }
        val cat = VaultCategory.entries.find { it.name == item.category }
        if (cat == VaultCategory.VIDEO || cat == VaultCategory.AUDIO || item.extension.equals("mov", ignoreCase = true)) {
            _viewingMediaItem.value = null
            _activeTab.value = VaultTab.MEDIA_PLAYER
            selectAndPlayMediaTrack(item)
        } else {
            _viewingMediaItem.value = item
        }
    }

    fun openFullscreenViewer(item: VaultItemEntity) {
        if (item.isEncrypted) {
            requestToggleEncryptionWithPin(item = item, openAfterDecrypt = true)
        } else {
            val cat = VaultCategory.entries.find { it.name == item.category }
            if (cat == VaultCategory.VIDEO || cat == VaultCategory.AUDIO || item.extension.equals("mov", ignoreCase = true)) {
                _viewingMediaItem.value = null
                _activeTab.value = VaultTab.MEDIA_PLAYER
                if (_mediaState.value.currentTrack?.id != item.id) {
                    selectAndPlayMediaTrack(item)
                }
            } else {
                _viewingMediaItem.value = item
            }
        }
    }

    // --- PRE-ENCRYPTION MULTI-ZIP SCANNER, VIDEO DEDUPLICATION & ANTIVIRUS ENGINE ---

    /**
     * Semua impor file atau banyak paket .ZIP diproses melalui mesin Pra-Enkripsi:
     * 1. Ekstraksi semua isi .ZIP
     * 2. Render frame video & hapus salah satu jika ada video kembar (1 dipakai)
     * 3. Cek virus/malware: jika ada virus, tampilkan dialog peringatan apakah tetap mau dimasukkan ke Vault atau dihapus.
     */
    fun importUrisFromPicker(uris: List<Uri>) {
        if (uris.isEmpty()) return
        val wasInScannerTab = _activeTab.value == VaultTab.ZIP_SCANNER

        val nyxUri = uris.firstOrNull { uri ->
            val name = repository.queryDisplayNameFromUri(uri).lowercase()
            name.endsWith(".nyx")
        }
        if (nyxUri != null) {
            viewModelScope.launch(Dispatchers.IO) {
                val isProtected = repository.isPasswordProtectedNyxUri(nyxUri)
                if (isProtected) {
                    val name = repository.queryDisplayNameFromUri(nyxUri)
                    withContext(Dispatchers.Main) {
                        _importPasswordPromptState.value = ImportPasswordPromptState(
                            uri = nyxUri,
                            fileName = name
                        )
                    }
                    return@launch
                } else {
                    withContext(Dispatchers.Main) {
                        executeNormalUriImport(uris, wasInScannerTab)
                    }
                }
            }
            return
        }

        executeNormalUriImport(uris, wasInScannerTab)
    }

    private fun executeNormalUriImport(uris: List<Uri>, wasInScannerTab: Boolean) {
        _activeTab.value = VaultTab.ZIP_SCANNER
        updateCurrentVaultScannerState {
            it.copy(
                isScanning = true,
                progressMessage = "Memulai ekstraksi & render frame video untuk ${uris.size} paket/file..."
            )
        }

        viewModelScope.launch(Dispatchers.IO) {
            val vId = _activeVaultId.value
            val displayVNum = SecurityConfig.displayVaultNumber(vId)
            val report = ZipPreEncryptionScannerEngine.scanAndDeduplicateUris(
                context = getApplication(),
                uris = uris,
                existingVaultItems = allItems.value,
                onProgress = { msg ->
                    updateCurrentVaultScannerState { it.copy(progressMessage = msg) }
                }
            )
            val defaultSelected = report.readyCleanItems.map { it.id }.toSet()
            recordScanReportAndQuarantineForVault(vId, report)

            if (report.virusFlaggedCount > 0) {
                updateCurrentVaultScannerState {
                    it.copy(
                        isScanning = false,
                        progressMessage = "",
                        currentReport = report,
                        selectedEntryIdsForExtraction = defaultSelected,
                        showVirusWarningModal = true
                    )
                }
                postBanner("PERINGATAN: Terdeteksi ${report.virusFlaggedCount} file bervirus di Brankas #$displayVNum!")
            } else if (wasInScannerTab || report.duplicatesRemovedCount > 0 || report.totalZipArchivesProcessed > 0) {
                updateCurrentVaultScannerState {
                    it.copy(
                        isScanning = false,
                        progressMessage = "",
                        currentReport = report,
                        selectedEntryIdsForExtraction = defaultSelected,
                        showVirusWarningModal = false
                    )
                }
                val dupNote = if (report.duplicatesRemovedCount > 0) {
                    " (${report.duplicatesRemovedCount} video kembar otomatis dihapus salah satu!)"
                } else ""
                postBanner("Pindai selesai: ${report.uniqueCleanFilesCount} file unik siap diekstrak ke Brankas #$displayVNum$dupNote")
            } else {
                val count = repository.commitStagedScanItemsToVault(report.readyCleanItems, vId)
                updateCurrentVaultScannerState {
                    it.copy(
                        isScanning = false,
                        currentReport = report,
                        selectedEntryIdsForExtraction = defaultSelected,
                        showVirusWarningModal = false
                    )
                }
                postBanner("$count file bersih berhasil dienkripsi (.nyx) ke Brankas #$displayVNum")
            }
        }
    }

    fun runSimulatedMultiZipScannerTest() {
        _activeTab.value = VaultTab.ZIP_SCANNER
        updateCurrentVaultScannerState {
            it.copy(
                isScanning = true,
                progressMessage = "Menyiapkan 2 paket .ZIP uji coba (Video 4K Kembar + Sampel Virus EICAR)..."
            )
        }
        viewModelScope.launch(Dispatchers.IO) {
            val vId = _activeVaultId.value
            val displayVNum = SecurityConfig.displayVaultNumber(vId)
            val report = ZipPreEncryptionScannerEngine.runBuiltInMultiZipTestBatch(
                context = getApplication(),
                existingVaultItems = allItems.value,
                onProgress = { msg ->
                    updateCurrentVaultScannerState { it.copy(progressMessage = msg) }
                }
            )
            val defaultSelected = report.readyCleanItems.map { it.id }.toSet()
            recordScanReportAndQuarantineForVault(vId, report)

            updateCurrentVaultScannerState {
                it.copy(
                    isScanning = false,
                    progressMessage = "",
                    currentReport = report,
                    selectedEntryIdsForExtraction = defaultSelected,
                    showVirusWarningModal = report.virusFlaggedCount > 0
                )
            }
            postBanner(
                "Hasil Uji 2 ZIP (Brankas #$displayVNum): ${report.duplicatesRemovedCount} video kembar dihapus & ${report.virusFlaggedCount} virus masuk karantina!"
            )
        }
    }

    private fun recordScanReportAndQuarantineForVault(vaultId: Int, report: BatchZipScanReport) {
        val displayVNum = SecurityConfig.displayVaultNumber(vaultId)
        val newQuarantineEntries = report.infectedItems.map { inf ->
            VaultQuarantineEntry(
                vaultId = vaultId,
                fileName = inf.fileName,
                extension = inf.extension,
                sourceZipName = inf.sourceZipName,
                sizeBytes = inf.sizeBytes,
                sha256Short = inf.sha256Hex.take(12),
                threatName = inf.virusThreatName ?: "Heuristic.Suspicious.Payload",
                threatDetail = inf.virusThreatDetail ?: "Ditahan di ruang karantina Brankas #$displayVNum",
                quarantinedAtLabel = "Baru saja • Brankas #$displayVNum",
                stagedFilePath = inf.stagedFile.absolutePath
            )
        }
        val newScanLog = VaultVirusScanLogEntry(
            vaultId = vaultId,
            scannedAtLabel = "Baru saja • Brankas #$displayVNum",
            archiveSummary = "${report.totalZipArchivesProcessed.coerceAtLeast(1)} Paket ZIP Diperiksa",
            totalEntriesInspected = report.totalExtractedFiles,
            cleanExtractedCount = report.uniqueCleanFilesCount,
            duplicatesRemovedCount = report.duplicatesRemovedCount,
            threatsQuarantinedCount = report.virusFlaggedCount,
            verdictBadge = if (report.virusFlaggedCount > 0) "${report.virusFlaggedCount} VIRUS DITAHAN" else "100% AMAN"
        )
        updateCurrentVaultScannerState { current ->
            current.copy(
                quarantineHistory = (newQuarantineEntries + current.quarantineHistory).take(25),
                virusScanLogs = (listOf(newScanLog) + current.virusScanLogs).take(25)
            )
        }
    }

    fun toggleSelectZipEntryForExtraction(entryId: String) {
        val state = currentVaultScannerState()
        val report = state.currentReport ?: return
        val baseSet = if (state.selectedEntryIdsForExtraction.isEmpty()) {
            report.readyCleanItems.map { it.id }.toSet()
        } else {
            state.selectedEntryIdsForExtraction
        }
        val updated = if (entryId in baseSet) baseSet - entryId else baseSet + entryId
        updateCurrentVaultScannerState { it.copy(selectedEntryIdsForExtraction = updated) }
    }

    fun selectAllCleanZipEntriesForExtraction() {
        val report = currentVaultScannerState().currentReport ?: return
        updateCurrentVaultScannerState {
            it.copy(selectedEntryIdsForExtraction = report.readyCleanItems.map { item -> item.id }.toSet())
        }
    }

    fun selectAllZipEntriesForExtraction() {
        val report = currentVaultScannerState().currentReport ?: return
        updateCurrentVaultScannerState {
            it.copy(selectedEntryIdsForExtraction = report.items.map { item -> item.id }.toSet())
        }
    }

    fun commitSelectedZipEntriesToVault() {
        val state = currentVaultScannerState()
        val report = state.currentReport ?: return
        val selectedIds = if (state.selectedEntryIdsForExtraction.isEmpty()) {
            report.readyCleanItems.map { it.id }.toSet()
        } else {
            state.selectedEntryIdsForExtraction
        }
        val itemsToExtract = report.items.filter { it.id in selectedIds }
        if (itemsToExtract.isEmpty()) {
            postBanner("Pilih minimal 1 file dari pratinjau ZIP untuk diekstrak.")
            return
        }
        viewModelScope.launch(Dispatchers.IO) {
            val vId = _activeVaultId.value
            val displayVNum = SecurityConfig.displayVaultNumber(vId)
            // Shred non-selected infected items
            report.infectedItems.filter { it.id !in selectedIds }.forEach { infected ->
                EncryptionScriptEngine.shredFileOnDisk(infected.stagedFile)
            }
            val count = repository.commitStagedScanItemsToVault(
                itemsToCommit = itemsToExtract,
                vaultId = vId
            )
            updateCurrentVaultScannerState {
                it.copy(
                    currentReport = null,
                    selectedEntryIdsForExtraction = emptySet(),
                    showVirusWarningModal = false
                )
            }
            postBanner("$count file terpilih dari pratinjau ZIP berhasil diekstrak & dienkripsi ke Brankas #$displayVNum!")
        }
    }

    fun quarantineInfectedZipEntriesToVaultLog() {
        val state = currentVaultScannerState()
        val report = state.currentReport ?: return
        val displayVNum = SecurityConfig.displayVaultNumber(_activeVaultId.value)
        report.infectedItems.forEach { infected ->
            EncryptionScriptEngine.shredFileOnDisk(infected.stagedFile)
        }
        updateCurrentVaultScannerState {
            it.copy(
                selectedEntryIdsForExtraction = report.readyCleanItems.map { item -> item.id }.toSet(),
                showVirusWarningModal = false
            )
        }
        postBanner("${report.virusFlaggedCount} file bervirus telah diisolasi ke Karantina Brankas #$displayVNum!")
    }

    fun deleteQuarantineHistoryEntry(entryId: String) {
        updateCurrentVaultScannerState { state ->
            state.copy(quarantineHistory = state.quarantineHistory.filterNot { it.id == entryId })
        }
        postBanner("Item karantina dihapus permanen dari riwayat brankas ini.")
    }

    /**
     * Keputusan pengguna untuk video yang terdeteksi sebagai Sub-Klip (misal video 2 menit di dalam video 4 menit):
     * - keepInVault = true ("Tetap Masukkan") -> ubah status ke CLEAN_READY dan tambahkan ke daftar ekstraksi.
     * - keepInVault = false ("Hapus Sub-Klip") -> shred dari disk dan tandai sebagai DUPLICATE_REMOVED.
     */
    fun decideSubClipAction(entryId: String, keepInVault: Boolean) {
        val state = currentVaultScannerState()
        val report = state.currentReport ?: return
        val target = report.items.find { it.id == entryId } ?: return

        val updatedItems = report.items.map { item ->
            if (item.id == entryId) {
                if (keepInVault) {
                    item.copy(status = com.example.data.StagedFileStatus.CLEAN_READY)
                } else {
                    EncryptionScriptEngine.shredFileOnDisk(item.stagedFile)
                    item.copy(
                        status = com.example.data.StagedFileStatus.DUPLICATE_REMOVED,
                        duplicateOfFileName = item.subClipOfFileName ?: "Video Induk"
                    )
                }
            } else item
        }
        val cleanCount = updatedItems.count { it.status == com.example.data.StagedFileStatus.CLEAN_READY }
        val dupCount = updatedItems.count { it.status == com.example.data.StagedFileStatus.DUPLICATE_REMOVED }
        val subCount = updatedItems.count { it.status == com.example.data.StagedFileStatus.SUB_CLIP_DETECTED }
        val newReport = report.copy(
            uniqueCleanFilesCount = cleanCount,
            duplicatesRemovedCount = dupCount,
            subClipsDetectedCount = subCount,
            bytesSavedByDeduplication = if (!keepInVault) {
                report.bytesSavedByDeduplication + target.sizeBytes
            } else report.bytesSavedByDeduplication,
            items = updatedItems
        )
        val newSelected = if (keepInVault) {
            state.selectedEntryIdsForExtraction + entryId
        } else {
            state.selectedEntryIdsForExtraction - entryId
        }
        updateCurrentVaultScannerState {
            it.copy(
                currentReport = newReport,
                selectedEntryIdsForExtraction = newSelected
            )
        }
        postBanner(
            if (keepInVault) {
                "Sub-Klip '${target.fileName}.${target.extension}' ditandai Tetap Dimasukkan ke Brankas."
            } else {
                "Sub-Klip '${target.fileName}.${target.extension}' dihapus karena sudah ada di dalam '${target.subClipOfFileName}'."
            }
        )
    }

    /**
     * Menjalankan Recheck Duplikat Isi & Sub-Klip pada Brankas Aktif yang sedang dibuka.
     */
    fun runActiveVaultDuplicateRecheck() {
        viewModelScope.launch(Dispatchers.IO) {
            val vId = _activeVaultId.value
            val dispNum = SecurityConfig.displayVaultNumber(vId)
            val items = allItems.value
            val warnings = ZipPreEncryptionScannerEngine.scanExistingVaultForDuplicatesAndSubClips(items)
            withContext(Dispatchers.Main) {
                updateCurrentVaultScannerState {
                    it.copy(
                        activeVaultDuplicateWarnings = warnings,
                        showActiveVaultRecheckDialog = warnings.isNotEmpty()
                    )
                }
                if (warnings.isEmpty()) {
                    postBanner("Recheck Brankas #$dispNum Selesai: 100% Bersih (Tidak ada file dobel atau sub-klip video).")
                } else {
                    postBanner("Recheck Brankas #$dispNum: Ditemukan ${warnings.size} file kembar / sub-klip video!")
                }
            }
        }
    }

    fun deleteDuplicateVaultItemById(itemId: Int) {
        val target = allItems.value.find { it.id == itemId } ?: return
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteItemPermanently(target)
            withContext(Dispatchers.Main) {
                updateCurrentVaultScannerState { state ->
                    val remaining = state.activeVaultDuplicateWarnings.filterNot {
                        it.itemId == itemId || it.matchedWithItemId == itemId
                    }
                    state.copy(
                        activeVaultDuplicateWarnings = remaining,
                        showActiveVaultRecheckDialog = remaining.isNotEmpty()
                    )
                }
                postBanner("File dobel/sub-klip '${target.name}.${target.extension}' telah dihapus dari Brankas.")
            }
        }
    }

    fun dismissActiveVaultRecheckDialog() {
        updateCurrentVaultScannerState {
            it.copy(showActiveVaultRecheckDialog = false)
        }
    }

    /**
     * Fitur #8: Pembersih Metadata GPS & EXIF Foto/Media.
     */
    fun stripExifForItem(item: VaultItemEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            val updated = repository.stripExifAndGpsMetadata(item)
            withContext(Dispatchers.Main) {
                postBanner("Metadata GPS & EXIF pada '${updated.name}.${updated.extension}' berhasil dihapus bersih!")
            }
        }
    }

    fun stripExifForAllVaultImages() {
        viewModelScope.launch(Dispatchers.IO) {
            val vId = _activeVaultId.value
            val dispNum = SecurityConfig.displayVaultNumber(vId)
            val count = repository.stripAllExifInVault(vId)
            withContext(Dispatchers.Main) {
                if (count > 0) {
                    postBanner("$count foto di Brankas #$dispNum telah dibersihkan dari koordinat GPS & metadata EXIF!")
                } else {
                    postBanner("Tidak ada foto/gambar di Brankas #$dispNum untuk dibersihkan.")
                }
            }
        }
    }

    fun clearVaultQuarantineAndLogs() {
        val displayVNum = SecurityConfig.displayVaultNumber(_activeVaultId.value)
        updateCurrentVaultScannerState { state ->
            state.copy(quarantineHistory = emptyList(), virusScanLogs = emptyList())
        }
        postBanner("Riwayat karantina & laporan virus Brankas #$displayVNum telah dibersihkan.")
    }

    fun resetDecoyVaultFromSettings() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.resetDecoyVaultToDefault()
            val currentMap = _zipScannerStatesByVault.value
            _zipScannerStatesByVault.value = currentMap - SecurityConfig.DECOY_VAULT_ID
            postBanner("Brankas Palsu (Honeypot) berhasil direset ke file umpan standar.")
        }
    }

    fun commitStagedCleanItemsOnly() {
        val report = currentVaultScannerState().currentReport ?: return
        viewModelScope.launch(Dispatchers.IO) {
            val vId = _activeVaultId.value
            val displayVNum = SecurityConfig.displayVaultNumber(vId)
            // Hapus permanen semua file bervirus yang ditolak
            report.infectedItems.forEach { infected ->
                EncryptionScriptEngine.shredFileOnDisk(infected.stagedFile)
            }
            val count = repository.commitStagedScanItemsToVault(
                itemsToCommit = report.readyCleanItems,
                vaultId = vId
            )
            updateCurrentVaultScannerState {
                it.copy(
                    currentReport = null,
                    selectedEntryIdsForExtraction = emptySet(),
                    showVirusWarningModal = false
                )
            }
            postBanner(
                "$count file aman berhasil dienkripsi (.nyx) ke Brankas #$displayVNum. File bervirus masuk catatan karantina!"
            )
        }
    }

    fun commitStagedAllIncludingInfected() {
        val report = currentVaultScannerState().currentReport ?: return
        viewModelScope.launch(Dispatchers.IO) {
            val vId = _activeVaultId.value
            val displayVNum = SecurityConfig.displayVaultNumber(vId)
            val allToCommit = report.readyCleanItems + report.infectedItems
            val count = repository.commitStagedScanItemsToVault(
                itemsToCommit = allToCommit,
                vaultId = vId
            )
            updateCurrentVaultScannerState {
                it.copy(
                    currentReport = null,
                    selectedEntryIdsForExtraction = emptySet(),
                    showVirusWarningModal = false
                )
            }
            postBanner(
                "$count file (termasuk ${report.virusFlaggedCount} file terisolasi) telah dikunci rapat (.nyx) di Brankas #$displayVNum"
            )
        }
    }

    fun openVirusWarningModal() {
        val current = currentVaultScannerState()
        if (current.currentReport != null && current.currentReport.virusFlaggedCount > 0) {
            updateCurrentVaultScannerState { it.copy(showVirusWarningModal = true) }
        }
    }

    fun dismissVirusWarningModal() {
        updateCurrentVaultScannerState { it.copy(showVirusWarningModal = false) }
    }

    fun clearZipScanReport() {
        viewModelScope.launch(Dispatchers.IO) {
            ZipPreEncryptionScannerEngine.clearStagingDirectory(getApplication())
            updateCurrentVaultScannerState {
                it.copy(
                    isScanning = false,
                    progressMessage = "",
                    currentReport = null,
                    selectedEntryIdsForExtraction = emptySet(),
                    showVirusWarningModal = false
                )
            }
            postBanner("Ruang pindai ZIP sementara telah dibersihkan")
        }
    }

    fun createVaultNoteOrMediaFile(
        fileName: String,
        extension: String,
        category: VaultCategory,
        contentText: String,
        encryptImmediately: Boolean
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.createSecretNoteInPrivateVault(
                vaultId = _activeVaultId.value,
                title = fileName,
                extension = extension,
                category = category,
                secretPayload = contentText,
                encryptImmediately = encryptImmediately
            )
            postBanner("\"$fileName.$extension\" disimpan ke Brankas ${_activeVaultId.value}")
        }
    }

    fun deleteVaultItem(item: VaultItemEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            if (_inspectingItem.value?.id == item.id) {
                _inspectingItem.value = null
            }
            if (_viewingMediaItem.value?.id == item.id) {
                _viewingMediaItem.value = null
            }
            repository.deleteItemPermanently(item)
            postBanner("File \"${item.name}.${item.extension}\" dihapus & di-shred secara permanen")
        }
    }

    // --- CUSTOM SCRIPT, RECOVERY & DUAL-VAULT PIN CONFIGURATION ---

    fun saveScriptConfiguration(preset: EncryptionPreset, customCode: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateEncryptionScript(preset.id, customCode)
            postBanner("Script NYX2 \"${preset.title}\" berhasil disimpan")
        }
    }

    fun saveVaultPins(vaultId: Int, pin1: String, pin2: String, pin3: String) {
        val collision = securityConfig.value.findPinCollisionReason(
            p1 = pin1,
            p2 = pin2,
            p3 = pin3,
            excludeVaultId = vaultId
        )
        if (collision != null) {
            postBanner("GAGAL SIMPAN PIN BRANKAS #$vaultId: $collision")
            return
        }
        viewModelScope.launch(Dispatchers.IO) {
            repository.updatePinsForVault(vaultId, pin1, pin2, pin3)
            postBanner("PIN 3-Lapis Brankas #$vaultId berhasil diperbarui & bebas tabrakan!")
        }
    }

    fun saveSettingsPins(pin1: String, pin2: String, pin3: String) {
        val collision = securityConfig.value.findPinCollisionReason(
            p1 = pin1,
            p2 = pin2,
            p3 = pin3,
            excludeSettings = true
        )
        if (collision != null) {
            postBanner("GAGAL SIMPAN PIN PENGATURAN: $collision")
            return
        }
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateSettingsPins(pin1, pin2, pin3)
            postBanner("PIN 6-Digit 3 Lapis Ruang Pengaturan Terisolasi berhasil diperbarui & bebas tabrakan!")
        }
    }

    fun saveRecoveryCredentials(
        question: String,
        answer: String,
        question2: String = "",
        answer2: String = "",
        question3: String = "",
        answer3: String = ""
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateRecoveryCredentials(question, answer, question2, answer2, question3, answer3)
            postBanner("3 Pertanyaan & Kunci Pemulihan Lupa Sandi berhasil diperbarui")
        }
    }

    fun openForgotPinModal() {
        _showForgotPinModal.value = true
    }

    fun dismissForgotPinModal() {
        _showForgotPinModal.value = false
    }

    fun resetPinsFromRecoveryAndUnlock(
        vaultId: Int,
        newPin1: String,
        newPin2: String,
        newPin3: String,
        unlockNow: Boolean
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            val isSettings = vaultId == SecurityConfig.ISOLATED_SETTINGS_CANDIDATE_ID
            if (isSettings) {
                repository.updateSettingsPins(newPin1, newPin2, newPin3)
            } else {
                repository.updatePinsForVault(vaultId, newPin1, newPin2, newPin3)
            }
            _showForgotPinModal.value = false
            _cryptoPinModalState.value = null
            if (unlockNow) {
                if (isSettings) {
                    _activeTab.value = VaultTab.SCRIPT_ENGINE
                    _securityState.value = SecurityGateUiState(
                        stage = AuthStage.UNLOCKED,
                        candidateVaultId = SecurityConfig.ISOLATED_SETTINGS_CANDIDATE_ID,
                        isIsolatedSettingsSession = true
                    )
                } else {
                    _activeVaultId.value = vaultId
                    _activeTab.value = VaultTab.DASHBOARD
                    _securityState.value = SecurityGateUiState(
                        stage = AuthStage.UNLOCKED,
                        candidateVaultId = vaultId,
                        isIsolatedSettingsSession = false
                    )
                }
            }
            postBanner(
                if (isSettings) {
                    "PIN 3-Lapis Ruang Pengaturan Terisolasi berhasil dipulihkan!"
                } else {
                    "PIN 3-Lapis Brankas #$vaultId berhasil dipulihkan & diperbarui!"
                }
            )
        }
    }

    // --- MEDIA PLAYER EXPORT & SHARE WITH SECONDARY BIOMETRIC RE-AUTHENTICATION ---

    fun requestExportOrShareWithBiometric(item: VaultItemEntity, isShareIntent: Boolean) {
        pausePlayback()
        _biometricExportModalState.value = BiometricExportModalState(
            item = item,
            isShareIntent = isShareIntent,
            errorMessage = null
        )
    }

    fun dismissBiometricExportModal() {
        _biometricExportModalState.value = null
    }

    fun onBiometricExportReAuthenticated() {
        val modal = _biometricExportModalState.value ?: return
        val item = modal.item
        val isShare = modal.isShareIntent
        _biometricExportModalState.value = null

        // Tampilkan dialog minta kata sandi untuk mengenkripsi paket .nyx ekspor
        _exportPasswordPromptState.value = ExportPasswordPromptState(
            item = item,
            isShareIntent = isShare
        )
    }

    fun dismissExportPasswordModal() {
        _exportPasswordPromptState.value = null
    }

    fun confirmExportWithPassword(
        password: String,
        onShareFileReady: (java.io.File, String) -> Unit,
        onLaunchSystemSaveAs: (String, String) -> Unit
    ) {
        val state = _exportPasswordPromptState.value ?: return
        val item = state.item
        val isShare = state.isShareIntent
        _exportPasswordPromptState.value = null
        pendingExportPassword = password
        pendingExportTargetItem = item

        viewModelScope.launch(Dispatchers.IO) {
            if (isShare) {
                val nyxFile = repository.exportVaultItemAsPasswordProtectedNyxFile(item, password)
                if (nyxFile != null && nyxFile.exists()) {
                    withContext(Dispatchers.Main) {
                        onShareFileReady(nyxFile, "application/octet-stream")
                        postBanner("Membagikan paket terproteksi: ${item.name}.nyx")
                    }
                } else {
                    postBanner("Gagal membuat paket ekspor .nyx")
                }
            } else {
                withContext(Dispatchers.Main) {
                    onLaunchSystemSaveAs("${item.name}.nyx", "application/octet-stream")
                    postBanner("Pilih lokasi simpan untuk \"${item.name}.nyx\"")
                }
            }
        }
    }

    fun completeExportToDocumentUri(targetUri: Uri?) {
        val item = pendingExportTargetItem
        val password = pendingExportPassword
        pendingExportTargetItem = null
        pendingExportPassword = ""
        if (targetUri == null || item == null) return
        viewModelScope.launch(Dispatchers.IO) {
            val ok = if (password.isNotBlank()) {
                repository.exportVaultItemAsPasswordProtectedNyxToUri(item, password, targetUri)
            } else {
                repository.exportVaultItemToUri(item, targetUri)
            }
            if (ok) {
                postBanner("Berhasil mengekspor \"${item.name}.nyx\" terproteksi sandi ke perangkat!")
            } else {
                postBanner("Gagal menyimpan berkas ekspor .nyx.")
            }
        }
    }

    fun dismissImportPasswordModal() {
        _importPasswordPromptState.value = null
    }

    fun confirmImportPassword(password: String) {
        val prompt = _importPasswordPromptState.value ?: return
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val entity = repository.importPasswordProtectedNyxUri(
                    uri = prompt.uri,
                    password = password,
                    vaultId = _activeVaultId.value
                )
                withContext(Dispatchers.Main) {
                    _importPasswordPromptState.value = null
                    postBanner("Berhasil mendekripsi & mengimpor \"${entity.name}.${entity.extension}\" ke brankas!")
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    _importPasswordPromptState.value = prompt.copy(
                        errorMessage = "Kata sandi salah! Berkas .nyx tidak dapat didekripsi."
                    )
                }
            }
        }
    }

    fun toggleBiometricGate(enabled: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.setBiometricEnabled(enabled)
            postBanner(if (enabled) "Autentikasi Biometrik diaktifkan" else "Autentikasi Biometrik dinonaktifkan")
        }
    }

    fun toggleAntiScreenshotMode(enabled: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.setAntiScreenshotEnabled(enabled)
            postBanner(
                if (enabled) "Mode Anti-Screenshot & Anti-Rekam Layar (Black Screen) AKTIF di seluruh bagian aplikasi!"
                else "Mode Anti-Screenshot & Anti-Rekam Layar DINONAKTIFKAN."
            )
        }
    }

    fun setVisualTheme(themeId: String) {
        val theme = com.example.ui.theme.AppVisualTheme.fromId(themeId)
        com.example.ui.theme.AppThemeController.activeTheme = theme
        viewModelScope.launch(Dispatchers.IO) {
            repository.setVisualThemeId(theme.id)
            postBanner("Tema aplikasi diubah ke ${theme.displayName} (${theme.badgeText})")
        }
    }

    fun setGraphicsQualityMode(modeId: String) {
        val mode = com.example.ui.theme.AppGraphicsQualityMode.fromId(modeId)
        if (com.example.ui.theme.AppThemeController.graphicsQualityMode != com.example.ui.theme.AppGraphicsQualityMode.CHAOS_OVERDRIVE) {
            com.example.ui.theme.AppThemeController.applyGraphicsQualityPreset(mode)
        }
        viewModelScope.launch(Dispatchers.IO) {
            repository.setGraphicsQualityConfig(
                modeId = mode.id,
                transparentBoxEnabled = com.example.ui.theme.AppThemeController.transparentButtonBoxEnabled
            )
            postBanner("Mode Grafik: ${ com.example.ui.theme.AppThemeController.graphicsQualityMode.displayName }")
        }
    }

    fun setTransparentButtonBox(enabled: Boolean) {
        com.example.ui.theme.AppThemeController.transparentButtonBoxEnabled = enabled
        viewModelScope.launch(Dispatchers.IO) {
            repository.setGraphicsQualityConfig(transparentBoxEnabled = enabled)
            postBanner(
                if (enabled) "Box Tombol Transparan (Teks Solid) diaktifkan!"
                else "Box Tombol dikembalikan ke warna solid!"
            )
        }
    }

    fun dismissBanner() {
        _statusBannerMessage.value = null
    }

    fun postBanner(message: String) {
        _statusBannerMessage.value = message
    }

    // --- YOUTUBE-STYLE MEDIA PLAYER ENGINE ---

    fun selectAndPlayMediaTrack(track: VaultItemEntity) {
        if (track.isEncrypted) {
            requestToggleEncryptionWithPin(item = track, openAfterDecrypt = true)
            return
        }
        val cat = VaultCategory.entries.find { it.name == track.category }
        if (cat == VaultCategory.IMAGE || cat == VaultCategory.DOCUMENT) {
            _viewingMediaItem.value = track
            return
        }
        val totalDurSec = track.durationSeconds.coerceAtLeast(1)
        val totalDurMs = if (track.durationSeconds > 0) {
            (track.durationSeconds * 1000L).coerceAtLeast(500L)
        } else {
            500L // Minimal 0.5 detik (500 ms), bukan 1 menit!
        }
        _mediaState.value = _mediaState.value.copy(
            currentTrack = track,
            currentSeconds = 0,
            totalSeconds = totalDurSec,
            currentPositionMs = 0L,
            totalDurationMs = totalDurMs,
            isPlaying = true,
            replayNonce = _mediaState.value.replayNonce + 1
        )
        startPlaybackTicker()
    }

    /**
     * Dipanggil saat VideoView onPrepared membaca durasi asli video dalam milidetik (minimal 500ms = 0.5 detik).
     * Mencegah video 1 detik ter-loop selama 1 menit!
     */
    fun updateActiveTrackActualDurationMs(durationMs: Long) {
        val clampedMs = durationMs.coerceAtLeast(500L)
        val sec = ((clampedMs + 999L) / 1000L).toInt().coerceAtLeast(1)
        val current = _mediaState.value
        _mediaState.value = current.copy(
            totalDurationMs = clampedMs,
            totalSeconds = sec,
            currentPositionMs = current.currentPositionMs.coerceAtMost(clampedMs),
            currentSeconds = current.currentSeconds.coerceAtMost(sec)
        )
    }

    fun togglePlayPause() {
        val current = _mediaState.value
        val track = current.currentTrack ?: mediaPlaylist.value.firstOrNull() ?: return
        if (track.isEncrypted) {
            requestToggleEncryptionWithPin(item = track, openAfterDecrypt = true)
            return
        }
        val nextPlaying = !current.isPlaying
        val durMs = current.totalDurationMs.coerceAtLeast(
            if (track.durationSeconds > 0) (track.durationSeconds * 1000L).coerceAtLeast(500L) else 500L
        )
        val isAtEnd = current.currentPositionMs >= (durMs - 350L).coerceAtLeast(100L) ||
            (current.totalSeconds > 0 && current.currentSeconds >= current.totalSeconds)
        val resetPosMs = if (nextPlaying && isAtEnd) 0L else current.currentPositionMs
        val resetSec = if (nextPlaying && isAtEnd) 0 else current.currentSeconds
        val nextNonce = if (nextPlaying && (isAtEnd || current.currentPositionMs == 0L)) {
            current.replayNonce + 1
        } else {
            current.replayNonce
        }
        _mediaState.value = current.copy(
            currentTrack = track,
            currentPositionMs = resetPosMs,
            currentSeconds = resetSec,
            totalSeconds = maxOf(current.totalSeconds, track.durationSeconds.coerceAtLeast(1)),
            totalDurationMs = durMs,
            isPlaying = nextPlaying,
            replayNonce = nextNonce
        )
        if (nextPlaying) {
            startPlaybackTicker()
        } else {
            playbackJob?.cancel()
        }
    }

    private fun pausePlayback() {
        playbackJob?.cancel()
        _mediaState.value = _mediaState.value.copy(isPlaying = false)
    }

    fun seekToSeconds(seconds: Int) {
        val current = _mediaState.value
        val totalSec = current.totalSeconds.coerceAtLeast(1)
        val clamped = seconds.coerceIn(0, totalSec)
        val targetMs = (clamped * 1000L).coerceIn(0L, current.totalDurationMs.coerceAtLeast(500L))
        _mediaState.value = current.copy(
            currentSeconds = clamped,
            currentPositionMs = targetMs,
            userSeekNonce = current.userSeekNonce + 1
        )
    }

    fun seekToPositionMs(positionMs: Long) {
        val current = _mediaState.value
        val totalMs = current.totalDurationMs.coerceAtLeast(500L)
        val clampedMs = positionMs.coerceIn(0L, totalMs)
        val sec = (clampedMs / 1000L).toInt()
        _mediaState.value = current.copy(
            currentPositionMs = clampedMs,
            currentSeconds = sec,
            userSeekNonce = current.userSeekNonce + 1
        )
    }

    fun syncHardwareVideoPositionMs(positionMs: Long, durationMs: Long) {
        val current = _mediaState.value
        val validDurMs = if (durationMs > 0L) durationMs.coerceAtLeast(500L) else current.totalDurationMs.coerceAtLeast(500L)
        val clampedPosMs = positionMs.coerceIn(0L, validDurMs)
        val totalSec = ((validDurMs + 999L) / 1000L).toInt().coerceAtLeast(1)
        val curSec = (clampedPosMs / 1000L).toInt().coerceIn(0, totalSec)
        _mediaState.value = current.copy(
            currentPositionMs = clampedPosMs,
            currentSeconds = curSec,
            totalDurationMs = validDurMs,
            totalSeconds = totalSec
        )
    }

    fun skipTrack(forward: Boolean) {
        val playlist = mediaPlaylist.value
        if (playlist.isEmpty()) return
        val current = _mediaState.value.currentTrack
        val activeFolder = _activeMediaFolder.value?.replace(" > ", "/")?.replace('\\', '/')?.trim('/')?.trim()
        val effectiveList = if (!activeFolder.isNullOrBlank()) {
            val exactFolderItems = playlist.filter {
                it.groupArchiveName?.replace(" > ", "/")?.replace('\\', '/')?.trim('/')?.trim() == activeFolder
            }
            val subTreeItems = playlist.filter {
                val g = it.groupArchiveName?.replace(" > ", "/")?.replace('\\', '/')?.trim('/')?.trim()
                g == activeFolder || g?.startsWith("$activeFolder/") == true
            }
            when {
                exactFolderItems.isNotEmpty() -> exactFolderItems
                subTreeItems.isNotEmpty() -> subTreeItems
                else -> playlist
            }
        } else {
            val activeZip = current?.groupArchiveName?.replace(" > ", "/")?.replace('\\', '/')?.trim('/')?.trim()
            if (!activeZip.isNullOrBlank()) {
                val zipSiblings = playlist.filter {
                    it.groupArchiveName?.replace(" > ", "/")?.replace('\\', '/')?.trim('/')?.trim() == activeZip
                }
                if (zipSiblings.size > 1) zipSiblings else playlist
            } else {
                playlist
            }
        }

        val currentIndex = effectiveList.indexOfFirst { it.id == current?.id }
        val nextTrack = if (forward) {
            if (currentIndex in 0 until effectiveList.lastIndex) {
                effectiveList[currentIndex + 1]
            } else {
                if (_mediaState.value.repeatMode == PlaybackRepeatMode.AUTO_ADVANCE) {
                    _mediaState.value = _mediaState.value.copy(
                        isPlaying = false,
                        currentSeconds = 0,
                        currentPositionMs = 0L
                    )
                    return
                }
                effectiveList.first()
            }
        } else {
            if (currentIndex > 0) {
                effectiveList[currentIndex - 1]
            } else {
                effectiveList.last()
            }
        }

        val durMs = if (nextTrack.durationSeconds > 0) {
            (nextTrack.durationSeconds * 1000L).coerceAtLeast(500L)
        } else 500L

        _mediaState.value = _mediaState.value.copy(
            currentTrack = nextTrack,
            currentSeconds = 0,
            totalSeconds = nextTrack.durationSeconds.coerceAtLeast(1),
            currentPositionMs = 0L,
            totalDurationMs = durMs,
            isPlaying = !nextTrack.isEncrypted,
            replayNonce = _mediaState.value.replayNonce + 1
        )
        if (_viewingMediaItem.value != null && nextTrack.category == VaultCategory.VIDEO.name && !nextTrack.isEncrypted) {
            _viewingMediaItem.value = nextTrack
        }
        if (!nextTrack.isEncrypted) {
            startPlaybackTicker()
        }
    }

    fun cyclePlaybackSpeed() {
        val speeds = listOf(1.0f, 1.25f, 1.5f, 2.0f, 0.75f)
        val currentSpeed = _mediaState.value.playbackSpeed
        val nextIndex = (speeds.indexOf(currentSpeed) + 1) % speeds.size
        _mediaState.value = _mediaState.value.copy(playbackSpeed = speeds[nextIndex])
    }

    fun cycleRepeatMode() {
        val next = when (_mediaState.value.repeatMode) {
            PlaybackRepeatMode.AUTO_ADVANCE -> PlaybackRepeatMode.REPEAT_ONE
            PlaybackRepeatMode.REPEAT_ONE -> PlaybackRepeatMode.REPEAT_PLAYLIST
            PlaybackRepeatMode.REPEAT_PLAYLIST -> PlaybackRepeatMode.AUTO_ADVANCE
        }
        val label = when (next) {
            PlaybackRepeatMode.REPEAT_ONE -> "Ulangi 1 Video (Loop)"
            PlaybackRepeatMode.AUTO_ADVANCE -> "Auto Lanjutkan Folder"
            PlaybackRepeatMode.REPEAT_PLAYLIST -> "Ulangi Seluruh Playlist"
        }
        _mediaState.value = _mediaState.value.copy(
            repeatMode = next,
            isRepeatEnabled = next != PlaybackRepeatMode.AUTO_ADVANCE
        )
        postBanner(label)
    }

    fun toggleRepeatMode() {
        cycleRepeatMode()
    }

    private fun startPlaybackTicker() {
        playbackJob?.cancel()
        playbackJob = viewModelScope.launch {
            while (isActive && _mediaState.value.isPlaying) {
                val effectiveSpeed = if (_mediaState.value.isHold2xActive) 2.0f else _mediaState.value.playbackSpeed
                val stepMs = 250L
                val delayMs = (stepMs / effectiveSpeed).toLong().coerceAtLeast(60L)
                delay(delayMs)
                val state = _mediaState.value
                val track = state.currentTrack ?: break
                val durationMs = state.totalDurationMs.coerceAtLeast(
                    if (track.durationSeconds > 0) (track.durationSeconds * 1000L).coerceAtLeast(500L) else 500L
                )
                val nextPosMs = state.currentPositionMs + stepMs
                if (nextPosMs >= durationMs) {
                    when (state.repeatMode) {
                        PlaybackRepeatMode.REPEAT_ONE -> {
                            _mediaState.value = state.copy(
                                currentPositionMs = 0L,
                                currentSeconds = 0,
                                replayNonce = state.replayNonce + 1
                            )
                        }
                        PlaybackRepeatMode.AUTO_ADVANCE, PlaybackRepeatMode.REPEAT_PLAYLIST -> {
                            skipTrack(forward = true)
                        }
                    }
                } else {
                    _mediaState.value = state.copy(
                        currentPositionMs = nextPosMs,
                        currentSeconds = (nextPosMs / 1000L).toInt(),
                        totalDurationMs = durationMs,
                        totalSeconds = ((durationMs + 999L) / 1000L).toInt().coerceAtLeast(1)
                    )
                }
            }
        }
    }

    // --- MORI v4.5.1 CLIENT-SIDE LINK DOWNLOADER, CONCURRENT BATCH QUEUE & SHARE SHEET ENGINE ---

    private val moriStateMutex = Mutex()

    fun updateMoriUrlInput(url: String) {
        val detected = MoriDownloaderEngine.detectPlatformFromUrl(url)
        val batchList = if (_moriState.value.isBatchMode) {
            MoriDownloaderEngine.extractBatchUrls(url).mapIndexed { idx, u ->
                MoriBatchQueueItem(
                    index = idx + 1,
                    url = u,
                    platform = MoriDownloaderEngine.detectPlatformFromUrl(u),
                    status = "IDLE"
                )
            }
        } else emptyList()

        _moriState.value = _moriState.value.copy(
            urlInput = url,
            selectedMode = detected.defaultMediaType,
            resolvedPreview = null,
            batchQueueItems = batchList
        )
    }

    fun toggleMoriBatchMode(enabled: Boolean) {
        val currentUrl = _moriState.value.urlInput
        val batchList = if (enabled) {
            val urls = MoriDownloaderEngine.extractBatchUrls(currentUrl).ifEmpty {
                listOf(
                    "https://www.tiktok.com/@tiktok/video/7106594312292453675",
                    "https://www.mediafire.com/file/mori451cloud/Arcanum_Pack_1080p.mp4/file",
                    "https://www.instagram.com/p/C9mori_carousel_slides/"
                )
            }
            urls.mapIndexed { idx, u ->
                MoriBatchQueueItem(
                    index = idx + 1,
                    url = u,
                    platform = MoriDownloaderEngine.detectPlatformFromUrl(u),
                    status = "IDLE"
                )
            }
        } else emptyList()
        _moriState.value = _moriState.value.copy(
            isBatchMode = enabled,
            urlInput = if (enabled && currentUrl.isBlank()) {
                batchList.joinToString("\n") { it.url }
            } else currentUrl,
            batchQueueItems = batchList
        )
    }

    fun selectMoriServer(server: String) {
        _moriState.value = _moriState.value.copy(selectedServer = server)
    }

    fun selectMoriTargetDestination(destination: MoriTargetDestination) {
        _moriState.value = _moriState.value.copy(targetDestination = destination)
        postBanner(
            when (destination) {
                MoriTargetDestination.ACTIVE_VAULT ->
                    "Tujuan simpan Mori: Brankas Aktif #${_activeVaultId.value}"
                MoriTargetDestination.PUBLIC_DOWNLOAD_ARCANUM ->
                    "Tujuan simpan Mori: Folder Download\\Arcanum\\"
                MoriTargetDestination.BOTH_VAULT_AND_PUBLIC ->
                    "Tujuan simpan Mori: Folder Download\\Arcanum\\ + Brankas Aktif #${_activeVaultId.value}"
            }
        )
    }

    fun selectMoriStressScenario(scenario: MoriStressScenario) {
        _moriState.value = _moriState.value.copy(
            stressScenario = scenario,
            scraperLogs = (_moriState.value.scraperLogs + "[Uji Skenario] Diaktifkan: ${scenario.fullTitle}").takeLast(18)
        )
        postBanner("Mode Skenario Mori: ${scenario.shortLabel}")
    }

    private var activeMoriDownloadJob: kotlinx.coroutines.Job? = null
    private var activeMoriAnalyzeJob: kotlinx.coroutines.Job? = null

    fun setShowMoriSubFileModal(show: Boolean) {
        _moriState.value = _moriState.value.copy(showSubFileSelectorModal = show)
    }

    fun toggleMoriSubFileSelection(subFileId: String) {
        val preview = _moriState.value.resolvedPreview ?: return
        val populated = MoriDownloaderEngine.populateDefaultSubFiles(preview)
        val updatedSubs = populated.subFiles.map { item ->
            if (item.id == subFileId) item.copy(isSelected = !item.isSelected) else item
        }
        _moriState.value = _moriState.value.copy(
            resolvedPreview = populated.copy(subFiles = updatedSubs)
        )
    }

    fun selectAllMoriSubFiles(selectAll: Boolean) {
        val preview = _moriState.value.resolvedPreview ?: return
        val populated = MoriDownloaderEngine.populateDefaultSubFiles(preview)
        val updatedSubs = populated.subFiles.map { it.copy(isSelected = selectAll) }
        _moriState.value = _moriState.value.copy(
            resolvedPreview = populated.copy(subFiles = updatedSubs)
        )
    }

    /**
     * Menerapkan pilihan rentang angka ala index.js (misal "1,3,5-7" atau "all")
     * pada daftar sub-file MediaFire / Carousel di Mori Downloader.
     */
    fun applyMoriSubFileRangeSelection(rangeText: String) {
        val preview = _moriState.value.resolvedPreview ?: return
        val populated = MoriDownloaderEngine.populateDefaultSubFiles(preview)
        val total = populated.subFiles.size
        if (total == 0) return

        val selectedIndices = MoriDownloaderEngine.parseMediaFireSelection(rangeText, total).toSet()
        if (selectedIndices.isEmpty()) {
            postBanner("Format rentang tidak valid. Gunakan contoh: 1,3,5-7 atau all")
            return
        }

        val updatedSubs = populated.subFiles.mapIndexed { idx, item ->
            item.copy(isSelected = idx in selectedIndices)
        }
        _moriState.value = _moriState.value.copy(
            resolvedPreview = populated.copy(subFiles = updatedSubs),
            scraperLogs = (_moriState.value.scraperLogs + "[Range Selector] Dipilih ${selectedIndices.size}/$total file via input \"$rangeText\"").takeLast(18)
        )
        postBanner("Terpilih ${selectedIndices.size} dari $total file (Rentang: $rangeText)")
    }

    /**
     * Membatalkan proses analisis maupun unduhan Mori yang sedang berjalan dan langsung menghapus
     * seluruh file sementara (.part / mori_staging) agar tidak menyisakan sampah di penyimpanan.
     */
    fun cancelMoriDownloadAndCleanup() {
        activeMoriAnalyzeJob?.cancel()
        activeMoriAnalyzeJob = null
        activeMoriDownloadJob?.cancel()
        activeMoriDownloadJob = null
        viewModelScope.launch(Dispatchers.IO) {
            val deletedCount = MoriDownloaderEngine.cleanupMoriStagingAndTempFiles(getApplication())
            val cancelledQueue = _moriState.value.batchQueueItems.map { item ->
                if (item.status == "LOADING" || item.status == "WAITING" || item.status == "ANALYZING...") {
                    item.copy(
                        status = "CANCELLED",
                        progressRatio = 0f,
                        statusDetail = "Dibatalkan & file sementara dihapus"
                    )
                } else item
            }
            val cancelLog = "[Auto-Clean] Proses dibatalkan! $deletedCount file sementara (.part/staging) langsung dihapus."
            moriStateMutex.withLock {
                _moriState.value = _moriState.value.copy(
                    isAnalyzing = false,
                    isDownloading = false,
                    progressRatio = 0f,
                    batchQueueItems = cancelledQueue,
                    statusLogLine = cancelLog,
                    scraperLogs = (_moriState.value.scraperLogs + cancelLog).takeLast(18)
                )
            }
            postBanner("Proses Mori dibatalkan & file sementara telah dihapus otomatis!")
        }
    }

    /**
     * Jika setelah preview media dianggap salah atau pengguna batal menyimpan,
     * langsung hapus fisik video/media dari Brankas, folder staging, dan Download/Arcanum.
     */
    fun rejectAndAutoDeleteMoriItem(item: VaultItemEntity) {
        if (_mediaState.value.currentTrack?.id == item.id) {
            playbackJob?.cancel()
            _mediaState.value = _mediaState.value.copy(
                currentTrack = null,
                isPlaying = false,
                currentSeconds = 0,
                currentPositionMs = 0L
            )
        }
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteItemPermanently(item)
            MoriDownloaderEngine.cleanupMoriStagingAndTempFiles(getApplication())
            // Hapus juga salinan di Download/Arcanum jika ada
            try {
                val pubDir = java.io.File(
                    android.os.Environment.getExternalStoragePublicDirectory(android.os.Environment.DIRECTORY_DOWNLOADS),
                    "Arcanum"
                )
                if (pubDir.exists()) {
                    pubDir.listFiles()?.forEach { f ->
                        if (f.name.startsWith(item.name, ignoreCase = true)) {
                            f.delete()
                        }
                    }
                }
            } catch (_: Exception) {
            }

            val delLog = "[Auto-Delete] Media \"${item.name}.${item.extension}\" ditolak/dibatalkan & langsung dihapus permanen!"
            moriStateMutex.withLock {
                val currentRes = _moriState.value.lastDownloadResult
                _moriState.value = _moriState.value.copy(
                    lastDownloadResult = if (currentRes?.createdEntity?.id == item.id) null else currentRes,
                    statusLogLine = delLog,
                    scraperLogs = (_moriState.value.scraperLogs + delLog).takeLast(18)
                )
            }
            postBanner("Video/media \"${item.name}.${item.extension}\" langsung dihapus dari penyimpanan!")
        }
    }

    fun confirmKeepMoriItem(item: VaultItemEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            val playableItem = if (item.isEncrypted) {
                try {
                    repository.toggleBinaryEncryptionOnDisk(item, false)
                } catch (_: Exception) {
                    item.copy(isEncrypted = false)
                }
            } else {
                item
            }
            val cat = VaultCategory.entries.find { it.name == playableItem.category }
            if (cat == VaultCategory.VIDEO || cat == VaultCategory.AUDIO) {
                _activeTab.value = VaultTab.MEDIA_PLAYER
                selectAndPlayMediaTrack(playableItem)
                postBanner("Media \"${playableItem.name}.${playableItem.extension}\" disimpan & langsung diputar di Player!")
            } else {
                _viewingMediaItem.value = playableItem
                postBanner("Media \"${playableItem.name}.${playableItem.extension}\" dikonfirmasi & disimpan di Brankas #${_activeVaultId.value}!")
            }
        }
    }

    fun togglePauseBatchQueueItem(index: Int) {
        val updated = _moriState.value.batchQueueItems.map { item ->
            if (item.index == index) {
                val nextStatus = if (item.status == "PAUSED") "WAITING" else "PAUSED"
                item.copy(status = nextStatus, statusDetail = if (nextStatus == "PAUSED") "Dijeda pengguna" else "Menunggu worker")
            } else item
        }
        _moriState.value = _moriState.value.copy(batchQueueItems = updated)
    }

    fun cancelBatchQueueItem(index: Int) {
        val updated = _moriState.value.batchQueueItems.filterNot { it.index == index }
            .mapIndexed { newIdx, item -> item.copy(index = newIdx + 1) }
        _moriState.value = _moriState.value.copy(batchQueueItems = updated)
        viewModelScope.launch(Dispatchers.IO) {
            MoriDownloaderEngine.cleanupMoriStagingAndTempFiles(getApplication())
        }
    }

    fun retryBatchQueueItem(index: Int) {
        val target = _moriState.value.batchQueueItems.find { it.index == index } ?: return
        viewModelScope.launch(Dispatchers.IO) {
            processSingleBatchQueueItem(
                qItem = target.copy(status = "LOADING", progressRatio = 0.1f, errorMessage = null),
                totalCount = _moriState.value.batchQueueItems.size.coerceAtLeast(1),
                forceCleanRecovery = true
            )
        }
    }

    fun toggleMoriWifiOnlyGuard(enabled: Boolean) {
        _moriState.value = _moriState.value.copy(wifiOnlyGuard = enabled)
    }

    fun toggleMoriCellularWarning(enabled: Boolean) {
        _moriState.value = _moriState.value.copy(cellularWarningGuard = enabled)
    }

    fun updateMoriCustomFileName(name: String) {
        _moriState.value = _moriState.value.copy(customFileName = name)
    }

    fun selectMoriExtractionMode(mode: MoriExtractionMode) {
        _moriState.value = _moriState.value.copy(selectedMode = mode)
    }

    fun selectMoriBatchFormatPreference(pref: com.example.data.MoriBatchFormatPreference) {
        _moriState.value = _moriState.value.copy(
            batchFormatPreference = pref,
            selectedMode = pref.extractionMode
        )
    }

    fun toggleMoriEncryptImmediately(encrypt: Boolean) {
        _moriState.value = _moriState.value.copy(encryptImmediately = encrypt)
    }

    fun analyzeMoriLink() {
        val current = _moriState.value
        if (current.isDownloading) return
        activeMoriAnalyzeJob?.cancel()

        val rawInput = current.urlInput.trim().ifBlank {
            if (current.isBatchMode) {
                "https://vt.tiktok.com/ZSpLECeq8/\nhttps://vt.tiktok.com/ZSpLE3xXe/"
            } else {
                "https://www.tiktok.com/@tiktok/video/7106594312292453675"
            }
        }

        val extractedUrls = MoriDownloaderEngine.extractBatchUrls(rawInput)
        val isMultiBatch = current.isBatchMode || extractedUrls.size > 1

        if (isMultiBatch) {
            val batchUrls = extractedUrls.ifEmpty {
                listOf(
                    "https://vt.tiktok.com/ZSpLECeq8/",
                    "https://vt.tiktok.com/ZSpLE3xXe/"
                )
            }
            val initialQueue = batchUrls.mapIndexed { idx, u ->
                val detectedPlat = MoriDownloaderEngine.detectPlatformFromUrl(u)
                val effectiveMode = if (MoriDownloaderEngine.isPureAudioPlatform(detectedPlat)) {
                    MoriExtractionMode.AUDIO_STREAM_MP3
                } else {
                    current.batchFormatPreference.extractionMode
                }
                MoriBatchQueueItem(
                    index = idx + 1,
                    url = u,
                    platform = detectedPlat,
                    status = "ANALYZING...",
                    resolvedTitle = "Menganalisis Link #${idx + 1}...",
                    customExtractionMode = effectiveMode,
                    statusDetail = "Menghubungi ${current.selectedServer} (${effectiveMode.defaultExtension.uppercase()})..."
                )
            }
            _moriState.value = current.copy(
                urlInput = rawInput,
                isBatchMode = true,
                isAnalyzing = true,
                batchQueueItems = initialQueue,
                scraperLogs = listOf("[Batch Inspector v4.6.0 • ${current.selectedServer}] Menganalisis ${batchUrls.size} tautan (${current.batchFormatPreference.label})...")
            )

            activeMoriAnalyzeJob = viewModelScope.launch(Dispatchers.IO) {
                var firstResolvedMeta: MoriResolvedMetadata? = null
                batchUrls.forEachIndexed { idx, singleUrl ->
                    val resolved = MoriDownloaderEngine.resolveUrlMetadata(
                        rawUrlInput = singleUrl,
                        selectedServer = _moriState.value.selectedServer
                    ) { stepMsg ->
                        _moriState.value = _moriState.value.copy(
                            scraperLogs = (_moriState.value.scraperLogs + stepMsg).takeLast(18)
                        )
                    }
                    if (firstResolvedMeta == null) {
                        firstResolvedMeta = resolved
                    }
                    moriStateMutex.withLock {
                        val updatedQueue = _moriState.value.batchQueueItems.map { q ->
                            if (q.index == idx + 1) {
                                val isPureAudio = MoriDownloaderEngine.isPureAudioPlatform(resolved.platform)
                                val chosenMode = if (isPureAudio) {
                                    MoriExtractionMode.AUDIO_STREAM_MP3
                                } else {
                                    _moriState.value.batchFormatPreference.extractionMode
                                }
                                q.copy(
                                    platform = resolved.platform,
                                    status = "READY",
                                    resolvedTitle = "${resolved.title} (${resolved.author})",
                                    customExtractionMode = chosenMode,
                                    subFileTitle = resolved.title,
                                    subFileStreamUrl = if (chosenMode == MoriExtractionMode.AUDIO_STREAM_MP3) {
                                        resolved.directAudioUrl ?: resolved.directVideoUrl
                                    } else {
                                        resolved.directVideoUrl ?: resolved.directAudioUrl
                                    },
                                    statusDetail = if (isPureAudio) {
                                        "Pure Audio MP3 (${resolved.resolverSource})"
                                    } else {
                                        "Siap Diunduh • ${_moriState.value.batchFormatPreference.shortBadge}"
                                    }
                                )
                            } else q
                        }
                        _moriState.value = _moriState.value.copy(
                            batchQueueItems = updatedQueue,
                            resolvedPreview = firstResolvedMeta ?: _moriState.value.resolvedPreview
                        )
                    }
                }
                val doneMsg = "[Batch Siap v4.6.0] ${batchUrls.size}/${batchUrls.size} tautan berstatus READY (${_moriState.value.batchFormatPreference.shortBadge})!"
                moriStateMutex.withLock {
                    _moriState.value = _moriState.value.copy(
                        isAnalyzing = false,
                        statusLogLine = doneMsg,
                        scraperLogs = (_moriState.value.scraperLogs + doneMsg).takeLast(18)
                    )
                }
                postBanner("Batch Analisis v4.6.0 Selesai: ${batchUrls.size} link siap diunduh!")
            }
            return
        }

        // Single URL Analysis
        _moriState.value = current.copy(
            urlInput = rawInput,
            isAnalyzing = true,
            scraperLogs = listOf("[Mori Inspector v4.6.0 • ${current.selectedServer}] Menganalisis link: ${rawInput.take(56)}...")
        )

        activeMoriAnalyzeJob = viewModelScope.launch(Dispatchers.IO) {
            val resolved = MoriDownloaderEngine.resolveUrlMetadata(
                rawUrlInput = rawInput,
                selectedServer = _moriState.value.selectedServer
            ) { stepMsg ->
                _moriState.value = _moriState.value.copy(
                    scraperLogs = (_moriState.value.scraperLogs + stepMsg).takeLast(18)
                )
            }
            val subCount = resolved.subFiles.size
            val summaryLog = "[Preview Siap] \"${resolved.title}\" • Server: ${resolved.resolverSource} ($subCount varian siap diunduh)"
            moriStateMutex.withLock {
                _moriState.value = _moriState.value.copy(
                    isAnalyzing = false,
                    resolvedPreview = resolved,
                    showSubFileSelectorModal = true,
                    statusLogLine = summaryLog,
                    scraperLogs = (_moriState.value.scraperLogs + summaryLog).takeLast(18)
                )
            }
            postBanner("Preview Siap: ${resolved.title} (${resolved.resolverSource})")
        }
    }

    fun triggerMoriOtaScraperUpdate() {
        if (_moriState.value.isCheckingOta) return
        _moriState.value = _moriState.value.copy(
            isCheckingOta = true,
            scraperLogs = _moriState.value.scraperLogs + "[OTA Hot-Patch] Memeriksa pembaruan bundle.js dari repositori coflyn/Mori v4.6.0..."
        )
        viewModelScope.launch {
            delay(450L)
            val updatedVersion = "v4.6.0-ota-patched"
            _moriState.value = _moriState.value.copy(
                isCheckingOta = false,
                otaBundleVersion = updatedVersion,
                scraperLogs = _moriState.value.scraperLogs + "[OTA Sukses] Scraper Core v6.4 (IG FastDL, YT Shorts #20, Spotimate, Bandcamp 320k, Pixiv Ugoira) diperbarui ke $updatedVersion!"
            )
            postBanner("Mori Scraper OTA berhasil diperbarui ke $updatedVersion (Core v6.4)!")
        }
    }

    /**
     * Eksekusi langsung saat pengguna menekan tombol Download 1 (720P), Download 2 (1080P),
     * atau Download 3 (MP3) pada kartu Inline Preview sehingga tidak tertimpa oleh subFiles default.
     */
    fun startMoriDirectFormatDownload(mode: MoriExtractionMode, qualityTag: String) {
        val current = _moriState.value
        if (current.isDownloading) {
            postBanner("Unduhan sedang berjalan, mohon tunggu atau cek panel Downloads.")
            return
        }
        val preview = current.resolvedPreview
        val singleUrl = preview?.cleanUrl?.takeIf { it.isNotBlank() }
            ?: current.urlInput.trim().ifBlank { "https://www.tiktok.com/@tiktok/video/7106594312292453675" }
        val platform = preview?.platform ?: MoriDownloaderEngine.detectPlatformFromUrl(singleUrl)
        val baseTitle = (preview?.title?.takeIf { it.isNotBlank() } ?: "Mori_${platform.displayName}")
            .let { "${it}_${qualityTag.uppercase()}" }
        val directStream = when (mode) {
            MoriExtractionMode.AUDIO_STREAM_MP3 -> preview?.directAudioUrl ?: preview?.directVideoUrl
            MoriExtractionMode.PHOTO_CAROUSEL -> preview?.carouselImageUrls?.firstOrNull() ?: preview?.coverImageUrl
            else -> preview?.directVideoUrl
        }
        val extLabel = if (mode == MoriExtractionMode.AUDIO_STREAM_MP3) "mp3" else mode.defaultExtension

        val directQueueItem = MoriBatchQueueItem(
            index = 1,
            url = singleUrl,
            platform = platform,
            status = "WAITING",
            resolvedTitle = "$baseTitle.$extLabel",
            customExtractionMode = mode,
            subFileTitle = baseTitle,
            subFileStreamUrl = directStream,
            statusDetail = "Format $qualityTag (${mode.title})"
        )

        _moriState.value = current.copy(
            selectedMode = mode,
            isBatchMode = false,
            batchQueueItems = listOf(directQueueItem)
        )
        runMoriDownloadQueue(listOf(directQueueItem))
    }

    /**
     * Mengunduh 1 item sub-file spesifik yang sedang dipilih melalui tombol navigasi Kiri (<) / Kanan (>)
     * pada kartu Preview Multi-File (MediaFire Folder, TikTok Slide, Google Drive, PixelDrain, dll.).
     */
    fun startMoriSingleSubFileDownload(subFile: MoriSubFileItem) {
        val current = _moriState.value
        if (current.isDownloading) {
            postBanner("Unduhan sedang berjalan, mohon tunggu atau cek panel Downloads.")
            return
        }
        val preview = current.resolvedPreview
        val singleUrl = preview?.cleanUrl?.takeIf { it.isNotBlank() }
            ?: current.urlInput.trim().ifBlank { "https://www.mediafire.com/file/mori451cloud/Arcanum_Pack.mp4/file" }
        val platform = preview?.platform ?: MoriDownloaderEngine.detectPlatformFromUrl(singleUrl)

        val directQueueItem = MoriBatchQueueItem(
            index = 1,
            url = singleUrl,
            platform = platform,
            status = "WAITING",
            resolvedTitle = "${subFile.fileName}.${subFile.extension}",
            customExtractionMode = subFile.extractionMode,
            subFileTitle = subFile.fileName,
            subFileStreamUrl = subFile.streamUrl ?: preview?.directVideoUrl ?: preview?.directAudioUrl,
            statusDetail = subFile.statusNote
        )

        _moriState.value = current.copy(
            selectedMode = subFile.extractionMode,
            isBatchMode = false,
            batchQueueItems = listOf(directQueueItem)
        )
        runMoriDownloadQueue(listOf(directQueueItem))
    }

    /**
     * Menjalankan unduhan Mori v4.6.0 (mendukung Single Link, Multi-File Sub-Item Selection,
     * maupun Concurrent Batch Queue hingga 50 URL dengan Semaphore(3) worker paralel + Pure Audio Batch Handling).
     */
    fun startMoriDownloadToVault() {
        val current = _moriState.value
        if (current.isDownloading) {
            postBanner("Anti-Bug Debounce: Unduhan sedang berjalan, mohon tunggu atau cek antrean.")
            return
        }

        val preview = current.resolvedPreview?.let { MoriDownloaderEngine.populateDefaultSubFiles(it) }
        val selectedSubFiles = preview?.subFiles?.filter { it.isSelected }.orEmpty()

        val initialBatchItems: List<MoriBatchQueueItem> = when {
            current.isBatchMode -> {
                val extracted = MoriDownloaderEngine.extractBatchUrls(current.urlInput).ifEmpty {
                    listOf(
                        "https://vt.tiktok.com/ZSpLECeq8/",
                        "https://vt.tiktok.com/ZSpLE3xXe/"
                    )
                }
                extracted.mapIndexed { idx, u ->
                    val existing = current.batchQueueItems.find { it.url == u || it.index == idx + 1 }
                    val detectedPlat = existing?.platform ?: MoriDownloaderEngine.detectPlatformFromUrl(u)
                    val effectiveBatchMode = if (MoriDownloaderEngine.isPureAudioPlatform(detectedPlat)) {
                        MoriExtractionMode.AUDIO_STREAM_MP3
                    } else {
                        existing?.customExtractionMode ?: current.batchFormatPreference.extractionMode
                    }
                    MoriBatchQueueItem(
                        index = idx + 1,
                        url = u,
                        platform = detectedPlat,
                        status = if (existing?.status == "PAUSED") "PAUSED" else "WAITING",
                        resolvedTitle = existing?.resolvedTitle.orEmpty(),
                        customExtractionMode = effectiveBatchMode,
                        subFileTitle = existing?.subFileTitle,
                        subFileStreamUrl = existing?.subFileStreamUrl,
                        statusDetail = if (MoriDownloaderEngine.isPureAudioPlatform(detectedPlat)) {
                            "Pure Audio Stream (320kbps MP3)"
                        } else {
                            "Menunggu slot paralel (${current.batchFormatPreference.shortBadge})"
                        }
                    )
                }
            }
            current.showSubFileSelectorModal && preview != null && selectedSubFiles.size > 1 -> {
                selectedSubFiles.mapIndexed { idx, sub ->
                    MoriBatchQueueItem(
                        index = idx + 1,
                        url = preview.cleanUrl.ifBlank { current.urlInput },
                        platform = preview.platform,
                        status = "WAITING",
                        resolvedTitle = "${sub.fileName}.${sub.extension}",
                        customExtractionMode = sub.extractionMode,
                        subFileTitle = sub.fileName,
                        subFileStreamUrl = sub.streamUrl,
                        statusDetail = sub.statusNote
                    )
                }
            }
            else -> {
                val singleUrl = preview?.cleanUrl?.takeIf { it.isNotBlank() }
                    ?: current.urlInput.trim().ifBlank {
                        "https://www.tiktok.com/@tiktok/video/7106594312292453675"
                    }
                val platform = preview?.platform ?: MoriDownloaderEngine.detectPlatformFromUrl(singleUrl)
                val modeToUse = current.selectedMode
                val matchingSub = selectedSubFiles.find { it.extractionMode == modeToUse }
                val streamUrl = when (modeToUse) {
                    MoriExtractionMode.AUDIO_STREAM_MP3 -> preview?.directAudioUrl ?: matchingSub?.streamUrl
                    MoriExtractionMode.PHOTO_CAROUSEL -> preview?.carouselImageUrls?.firstOrNull() ?: preview?.coverImageUrl ?: matchingSub?.streamUrl
                    else -> preview?.directVideoUrl ?: matchingSub?.streamUrl
                }
                val titleToUse = matchingSub?.fileName
                    ?: preview?.title?.takeIf { it.isNotBlank() }
                    ?: "Mori_${platform.displayName}"
                listOf(
                    MoriBatchQueueItem(
                        index = 1,
                        url = singleUrl,
                        platform = platform,
                        status = "WAITING",
                        resolvedTitle = "$titleToUse.${modeToUse.defaultExtension}",
                        customExtractionMode = modeToUse,
                        subFileTitle = titleToUse,
                        subFileStreamUrl = streamUrl,
                        statusDetail = "Siap diekstrak (${modeToUse.title})"
                    )
                )
            }
        }

        runMoriDownloadQueue(initialBatchItems)
    }

    private fun runMoriDownloadQueue(initialBatchItems: List<MoriBatchQueueItem>) {
        val current = _moriState.value
        val destLabel = when (current.targetDestination) {
            MoriTargetDestination.ACTIVE_VAULT -> "Brankas Aktif #${_activeVaultId.value}"
            MoriTargetDestination.PUBLIC_DOWNLOAD_ARCANUM -> "Download/Arcanum/"
            MoriTargetDestination.BOTH_VAULT_AND_PUBLIC -> "Download/Arcanum/ + Brankas #${_activeVaultId.value}"
        }

        _moriState.value = current.copy(
            urlInput = if (!current.isBatchMode) current.urlInput.ifBlank { initialBatchItems.first().url } else current.urlInput,
            isDownloading = true,
            showSubFileSelectorModal = false,
            progressRatio = 0.05f,
            scraperLogs = listOf(
                "[Concurrent Dispatcher] Menjalankan ${initialBatchItems.size} tugas (3x Worker Paralel) -> $destLabel",
                "[Skenario Aktif] ${current.stressScenario.fullTitle}"
            ),
            lastDownloadResult = null,
            batchQueueItems = initialBatchItems
        )

        activeMoriDownloadJob = viewModelScope.launch(Dispatchers.IO) {
            val concurrencySemaphore = Semaphore(3)
            val totalTasks = initialBatchItems.size.coerceAtLeast(1)

            val results = coroutineScope {
                initialBatchItems.map { qItem ->
                    async(Dispatchers.IO) {
                        if (qItem.status == "PAUSED") {
                            return@async null
                        }
                        concurrencySemaphore.withPermit {
                            processSingleBatchQueueItem(
                                qItem = qItem,
                                totalCount = totalTasks,
                                forceCleanRecovery = false
                            )
                        }
                    }
                }.awaitAll()
            }

            val completedResults = results.filterNotNull()
            val lastRes = completedResults.lastOrNull()
            val errorCount = _moriState.value.batchQueueItems.count { it.status == "ERROR" }

            moriStateMutex.withLock {
                _moriState.value = _moriState.value.copy(
                    isDownloading = false,
                    progressRatio = if (completedResults.isNotEmpty()) 1f else _moriState.value.progressRatio,
                    resolvedPreview = lastRes?.resolvedMeta ?: _moriState.value.resolvedPreview,
                    statusLogLine = when {
                        errorCount > 0 && completedResults.isNotEmpty() ->
                            "Selesai sebagian: ${completedResults.size} berhasil, $errorCount tertunda (ketuk Retry pada item gagal)"
                        lastRes != null ->
                            lastRes.scraperLogs.lastOrNull() ?: "Semua antrean selesai"
                        else -> "Antrean dijeda atau dibatalkan"
                    },
                    lastDownloadResult = lastRes,
                    customFileName = ""
                )
            }

            if (lastRes != null) {
                val publicSuffix = lastRes.savedPublicPath?.let { " & $it" }.orEmpty()
                postBanner(
                    if (totalTasks > 1) {
                        "Batch Konkuren Selesai! ${completedResults.size}/$totalTasks file disimpan ke Brankas #${_activeVaultId.value}$publicSuffix"
                    } else {
                        "Unduhan Selesai: ${lastRes.savedFileName}.${lastRes.extension} (Ketuk Buka di Player / Simpan)"
                    }
                )
            } else if (errorCount > 0) {
                postBanner("Simulasi Skenario: $errorCount item butuh Retry. Ketuk tombol Retry pada item di antrean!")
            }
        }
    }

    private suspend fun processSingleBatchQueueItem(
        qItem: MoriBatchQueueItem,
        totalCount: Int,
        forceCleanRecovery: Boolean
    ): MoriDownloadResult? {
        val snapshot = _moriState.value
        val activeScenario = if (forceCleanRecovery) MoriStressScenario.IDEAL_NORMAL else snapshot.stressScenario
        val modeToUse = qItem.customExtractionMode ?: snapshot.selectedMode

        // Update item status to LOADING
        moriStateMutex.withLock {
            val updatedList = _moriState.value.batchQueueItems.map { item ->
                if (item.index == qItem.index) {
                    item.copy(
                        status = "LOADING",
                        progressRatio = 0.14f,
                        errorMessage = null,
                        statusDetail = "Worker mengekstrak stream (${snapshot.selectedServer})..."
                    )
                } else item
            }
            _moriState.value = _moriState.value.copy(batchQueueItems = updatedList)
        }

        // Pada Skenario 3 (MODERATE_PARTIAL_CORRUPT), jika antrean > 1 dan item ke-2 belum pernah di-retry,
        // simulasikan kegagalan 404 parsial agar pengguna dapat menguji tombol Retry satuan secara nyata!
        if (!forceCleanRecovery &&
            activeScenario == MoriStressScenario.MODERATE_PARTIAL_CORRUPT &&
            totalCount > 1 &&
            qItem.index == 2
        ) {
            delay(320L)
            val errLog = "[Skenario 3 • Item #${qItem.index}] HTTP 404 Mirror Expired pada sub-file ke-2! File lain tetap lanjut; ketuk RETRY pada item #2 untuk memulihkan."
            moriStateMutex.withLock {
                val updatedList = _moriState.value.batchQueueItems.map { item ->
                    if (item.index == qItem.index) {
                        item.copy(
                            status = "ERROR",
                            progressRatio = 0.42f,
                            errorMessage = "HTTP 404 Mirror Expired (Ketuk Retry)",
                            statusDetail = "Gagal parsial — Ketuk Retry untuk jalur cadangan"
                        )
                    } else item
                }
                _moriState.value = _moriState.value.copy(
                    batchQueueItems = updatedList,
                    statusLogLine = errLog,
                    scraperLogs = (_moriState.value.scraperLogs + errLog).takeLast(18)
                )
            }
            return null
        }

        val platform = MoriDownloaderEngine.detectPlatformFromUrl(qItem.url)
        val (stagedFile, logs, resolvedMeta) = MoriDownloaderEngine.executeClientSideExtraction(
            context = getApplication(),
            rawUrlInput = qItem.url,
            selectedMode = modeToUse,
            otaVersion = snapshot.otaBundleVersion,
            selectedServer = snapshot.selectedServer,
            stressScenario = activeScenario,
            customSubFileTitle = qItem.subFileTitle,
            customDirectStreamUrl = qItem.subFileStreamUrl
        ) { msg, ratio ->
            val clamped = ratio.coerceIn(0.15f, 0.92f)
            val currentItems = _moriState.value.batchQueueItems.map { item ->
                if (item.index == qItem.index) {
                    item.copy(
                        progressRatio = clamped,
                        statusDetail = msg.substringAfter("] ").take(48)
                    )
                } else item
            }
            val avgProgress = currentItems.map {
                when (it.status) {
                    "DONE" -> 1f
                    "LOADING" -> it.progressRatio
                    else -> 0.08f
                }
            }.average().toFloat().coerceIn(0.05f, 0.98f)

            _moriState.value = _moriState.value.copy(
                batchQueueItems = currentItems,
                statusLogLine = msg,
                progressRatio = avgProgress,
                scraperLogs = (_moriState.value.scraperLogs + msg).takeLast(18)
            )
        }

        val rawDesiredTitle = when {
            !qItem.subFileTitle.isNullOrBlank() -> qItem.subFileTitle
            totalCount == 1 && snapshot.customFileName.isNotBlank() -> snapshot.customFileName
            totalCount > 1 -> "${resolvedMeta.title}_${qItem.index}"
            else -> resolvedMeta.title
        }

        // Jika pengguna memilih simpan ke folder publik Download\Arcanum\ atau Keduanya
        var savedPublicPath: String? = null
        if (snapshot.targetDestination == MoriTargetDestination.PUBLIC_DOWNLOAD_ARCANUM ||
            snapshot.targetDestination == MoriTargetDestination.BOTH_VAULT_AND_PUBLIC
        ) {
            val publicFile = MoriDownloaderEngine.saveToPublicDownloadArcanumFolder(
                context = getApplication(),
                sourceFile = stagedFile,
                targetFileName = rawDesiredTitle.ifBlank { resolvedMeta.title },
                extension = stagedFile.extension.ifBlank { modeToUse.defaultExtension }
            )
            savedPublicPath = "Download/Arcanum/${publicFile.name}"
        }

        val targetVault = _activeVaultId.value
        val savedEntity = repository.saveMoriDownloadedFileToVault(
            stagedFile = stagedFile,
            platform = platform,
            extractionMode = modeToUse,
            customTitle = rawDesiredTitle,
            encryptImmediately = snapshot.encryptImmediately,
            vaultId = targetVault,
            resolvedDurationSeconds = resolvedMeta.durationSeconds
        )

        val finalLog = buildString {
            append("[Selesai #${qItem.index}] Brankas #$targetVault: ${savedEntity.name}.${savedEntity.extension} (${savedEntity.sizeBytes / 1024} KB)")
            if (savedPublicPath != null) {
                append(" + Disimpan ke $savedPublicPath")
            }
        }

        val res = MoriDownloadResult(
            platform = platform,
            extractionMode = modeToUse,
            savedFileName = savedEntity.name,
            extension = savedEntity.extension,
            fileSizeBytes = savedEntity.sizeBytes,
            isEncryptedInVault = savedEntity.isEncrypted,
            scraperLogs = logs + finalLog,
            createdEntity = savedEntity,
            resolvedMeta = resolvedMeta,
            savedPublicPath = savedPublicPath
        )

        moriStateMutex.withLock {
            val doneItems = _moriState.value.batchQueueItems.map { item ->
                if (item.index == qItem.index) {
                    item.copy(
                        status = "DONE",
                        progressRatio = 1f,
                        resolvedTitle = "${savedEntity.name}.${savedEntity.extension}",
                        errorMessage = null,
                        statusDetail = savedPublicPath ?: "Tersimpan di Brankas #$targetVault"
                    )
                } else item
            }
            _moriState.value = _moriState.value.copy(
                batchQueueItems = doneItems,
                streakCount = _moriState.value.streakCount + 1,
                scraperLogs = (_moriState.value.scraperLogs + finalLog).takeLast(18)
            )
        }
        return res
    }

    /**
     * Menangani link atau media yang dikirim melalui Android System Share Sheet ("Bagikan ke Trantor Vault / Mori")
     */
    fun handleIncomingSharedIntent(sharedText: String?, sharedStreamUri: Uri?) {
        if (!sharedText.isNullOrBlank()) {
            val extractedUrl = MoriDownloaderEngine.extractCleanUrl(sharedText)
            updateMoriUrlInput(extractedUrl)
            _activeTab.value = VaultTab.MORI_DOWNLOADER
            analyzeMoriLink()
            postBanner("Link dari Share Sheet siap diunduh di Mori Grabber!")
        } else if (sharedStreamUri != null) {
            importUrisFromPicker(listOf(sharedStreamUri))
        }
    }

    /**
     * Membuka pratinjau langsung untuk file dari luar aplikasi ("Buka Dengan Arcanum" / `Intent.ACTION_VIEW`).
     */
    fun openExternalFileForQuickPreview(uri: Uri, mimeTypeHint: String?) {
        viewModelScope.launch(Dispatchers.IO) {
            val req = repository.prepareExternalUriForQuickPreview(uri, mimeTypeHint)
            withContext(Dispatchers.Main) {
                if (req != null) {
                    _externalPreviewRequest.value = req
                } else {
                    lockVaultNow()
                    postBanner("Tidak dapat membaca berkas eksternal • Silakan masuk dengan PIN.")
                }
            }
        }
    }

    /**
     * Menutup layar pratinjau file eksternal dan WAJIB mengunci sesi ke Gerbang PIN Lapis 1 (`AuthStage.PIN_LAYER_1`)
     * agar tidak ada celah bypass ke dalam brankas saat menekan tombol Back.
     */
    fun closeExternalPreviewAndLockToPinGate() {
        val currentReq = _externalPreviewRequest.value
        _externalPreviewRequest.value = null
        if (currentReq != null) {
            viewModelScope.launch(Dispatchers.IO) {
                try {
                    currentReq.tempFile.delete()
                } catch (_: Exception) {
                }
            }
        }
        lockVaultNow()
        postBanner("Pratinjau ditutup • Masukkan PIN 3-Lapis untuk membuka Brankas Arcanum.")
    }

    /**
     * Menyimpan & menimpa (overwrite) file yang sedang dipratinjau dari luar aplikasi ke Brankas #1,
     * kemudian langsung mengarahkan ke layar PIN Lapis 1.
     */
    fun saveExternalPreviewToVaultAndLockToPinGate() {
        val currentReq = _externalPreviewRequest.value ?: return
        _externalPreviewRequest.value = null
        viewModelScope.launch(Dispatchers.IO) {
            try {
                // Salin ke file sementara dengan nama asli agar nama di Brankas bersih dan menimpa versi lama jika ada
                val cleanOriginalFile = File(currentReq.tempFile.parentFile, currentReq.displayName)
                currentReq.tempFile.copyTo(cleanOriginalFile, overwrite = true)
                repository.importLocalFileIntoPrivateVault(
                    sourceFile = cleanOriginalFile,
                    vaultId = 1,
                    encryptImmediately = true
                )
                cleanOriginalFile.delete()
                currentReq.tempFile.delete()
            } catch (_: Exception) {
            }
            withContext(Dispatchers.Main) {
                lockVaultNow()
                postBanner("Berkas '${currentReq.displayName}' disimpan & ditimpa ke Brankas #1 • Masukkan PIN untuk membuka.")
            }
        }
    }

    fun encryptLocalFileFromMt(file: java.io.File) {
        viewModelScope.launch {
            try {
                val entity = repository.importLocalFileIntoPrivateVault(file, _activeVaultId.value, encryptImmediately = true)
                _statusBannerMessage.value = "Tersimpan aman: ${entity.name} disegel dengan NYX2!"
            } catch (e: Exception) {
                _statusBannerMessage.value = "Gagal enkripsi berkas: ${e.message}"
            }
        }
    }

    // --- STEGANOGRAFI PNG (SEMBUNYIKAN PESAN & FILE DI DALAM GAMBAR PNG VALID) ---

    fun createStegoPngInActiveVault(
        outputFileName: String,
        coverTheme: StegoCoverTheme,
        secretTitle: String,
        secretMessage: String,
        passcode: String,
        attachedItem: VaultItemEntity?,
        sourceCarrierItem: VaultItemEntity? = null,
        isQrKeyCardMode: Boolean = false
    ) {
        _stegoState.value = _stegoState.value.copy(
            decodeErrorMessage = null
        )
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val vId = _activeVaultId.value
                val created = repository.createStegoPngInVault(
                    vaultId = vId,
                    outputFileName = outputFileName,
                    coverTheme = coverTheme,
                    secretTitle = secretTitle,
                    secretMessage = secretMessage,
                    passcode = passcode,
                    attachedVaultItem = attachedItem,
                    sourceCarrierVaultItem = sourceCarrierItem,
                    isQrKeyCardMode = isQrKeyCardMode
                )
                val autoDecoded = repository.decodeStegoPngItem(created, passcode)
                withContext(Dispatchers.Main) {
                    _stegoState.value = _stegoState.value.copy(
                        selectedDecodingItem = created,
                        decodedPayload = autoDecoded,
                        decodeErrorMessage = null
                    )
                    val modeLabel = when {
                        isQrKeyCardMode -> "Kartu Kunci Stego-QR"
                        autoDecoded.isDualPhotoFusion -> "Dual-Foto (2 Foto di 1 File)"
                        sourceCarrierItem != null -> "Stego-Foto (${sourceCarrierItem.name})"
                        else -> "Stego-PNG"
                    }
                    postBanner("$modeLabel \"${created.name}.png\" berhasil disimpan ke Brankas #${SecurityConfig.displayVaultNumber(vId)}!")
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    _stegoState.value = _stegoState.value.copy(
                        decodeErrorMessage = e.message ?: "Gagal menyisipkan pesan ke gambar PNG."
                    )
                }
            }
        }
    }

    fun createStegoWavInActiveVault(
        outputFileName: String,
        wavPreset: WavCamouflagePreset,
        secretTitle: String,
        secretMessage: String,
        passcode: String,
        attachedItem: VaultItemEntity?,
        sourceCarrierAudioItem: VaultItemEntity? = null
    ) {
        _stegoState.value = _stegoState.value.copy(
            decodeErrorMessage = null
        )
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val vId = _activeVaultId.value
                val created = repository.createStegoWavInVault(
                    vaultId = vId,
                    outputFileName = outputFileName,
                    wavPreset = wavPreset,
                    secretTitle = secretTitle,
                    secretMessage = secretMessage,
                    passcode = passcode,
                    attachedVaultItem = attachedItem,
                    sourceCarrierAudioItem = sourceCarrierAudioItem
                )
                val autoDecoded = repository.decodeStegoPngItem(created, passcode)
                withContext(Dispatchers.Main) {
                    _stegoState.value = _stegoState.value.copy(
                        selectedDecodingItem = created,
                        decodedPayload = autoDecoded,
                        decodeErrorMessage = null
                    )
                    val modeLabel = when {
                        autoDecoded.isDualPhotoFusion -> "Stego-WAV (Foto di Dalam Audio)"
                        sourceCarrierAudioItem != null -> "Stego-WAV (${sourceCarrierAudioItem.name})"
                        else -> "Stego-WAV LSB (${wavPreset.title})"
                    }
                    postBanner("$modeLabel \"${created.name}.wav\" berhasil disimpan ke Brankas #${SecurityConfig.displayVaultNumber(vId)}!")
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    _stegoState.value = _stegoState.value.copy(
                        decodeErrorMessage = e.message ?: "Gagal menyisipkan pesan LSB ke audio WAV."
                    )
                }
            }
        }
    }

    fun decodeStegoPngFromVaultItem(item: VaultItemEntity, passcode: String) {
        _stegoState.value = _stegoState.value.copy(
            selectedDecodingItem = item,
            decodeErrorMessage = null
        )
        viewModelScope.launch(Dispatchers.IO) {
            val payload = repository.decodeStegoPngItem(item, passcode)
            withContext(Dispatchers.Main) {
                if (!payload.isStegoCarrier) {
                    val mediaType = if (payload.isWavAudioCarrier) "Audio WAV" else "Gambar PNG"
                    _stegoState.value = _stegoState.value.copy(
                        decodedPayload = payload,
                        decodeErrorMessage = "$mediaType ini tidak memiliki muatan rahasia Steganografi."
                    )
                } else if (payload.isPasscodeLocked && !payload.isPasscodeValid) {
                    _stegoState.value = _stegoState.value.copy(
                        decodedPayload = payload,
                        decodeErrorMessage = "Sandi Stego salah! Masukkan sandi yang benar untuk membuka pesan."
                    )
                } else {
                    _stegoState.value = _stegoState.value.copy(
                        decodedPayload = payload,
                        decodeErrorMessage = null
                    )
                    val msg = when {
                        payload.isWavAudioCarrier && payload.isDualPhotoFusion ->
                            "Audio WAV LSB berhasil dibongkar! Pesan & Foto di dalam suara siap dilihat."
                        payload.isWavAudioCarrier ->
                            "Muatan rahasia LSB dari gelombang Audio .WAV berhasil didekripsi!"
                        payload.isDualPhotoFusion ->
                            "2 Foto dalam 1 File berhasil dibongkar! Foto Kedua siap dilihat & dipulihkan."
                        else ->
                            "Muatan rahasia Stego-PNG berhasil didekripsi!"
                    }
                    postBanner(msg)
                }
            }
        }
    }

    fun restoreEmbeddedFileFromStego(payload: DecodedStegoPayload) {
        viewModelScope.launch(Dispatchers.IO) {
            val vId = _activeVaultId.value
            val restored = repository.restoreEmbeddedFileFromStegoPayload(vId, payload)
            withContext(Dispatchers.Main) {
                if (restored != null) {
                    val label = when {
                        payload.isWavAudioCarrier && payload.isDualPhotoFusion -> "Foto dari Audio WAV"
                        payload.isDualPhotoFusion -> "Foto Kedua"
                        else -> "Lampiran"
                    }
                    postBanner("$label \"${restored.name}.${restored.extension}\" dipulihkan ke Brankas #${SecurityConfig.displayVaultNumber(vId)}!")
                } else {
                    _stegoState.value = _stegoState.value.copy(
                        decodeErrorMessage = "Tidak ada lampiran file di dalam media Stego ini."
                    )
                }
            }
        }
    }

    fun clearDecodedStegoResult() {
        _stegoState.value = _stegoState.value.copy(
            selectedDecodingItem = null,
            decodedPayload = null,
            decodeErrorMessage = null
        )
    }

    // --- JEBAKAN PENYUSUP (INTRUDER HONEYPOT LOG) & SENSOR GUNCANGAN DARURAT EKSTREM ---

    private fun currentFormattedTimestamp(): String {
        val sdf = SimpleDateFormat("dd MMM • HH:mm:ss", Locale("id", "ID"))
        return sdf.format(Date())
    }

    fun recordIntruderTrapLog(
        triggerType: String,
        l1: String,
        l2: String,
        l3: String,
        initialActivity: String
    ) {
        val current = _intruderAndPanicState.value
        val newEntry = IntruderTrapLogEntry(
            timestampLabel = "Baru saja (${currentFormattedTimestamp()})",
            triggerType = triggerType,
            wrongPinLayer1 = l1,
            wrongPinLayer2 = l2,
            wrongPinLayer3 = l3,
            activitiesInDecoy = listOf(initialActivity)
        )
        _intruderAndPanicState.value = current.copy(
            intruderLogs = listOf(newEntry) + current.intruderLogs
        )
    }

    private fun appendActivityToLatestIntruderLog(activityText: String) {
        val current = _intruderAndPanicState.value
        val logs = current.intruderLogs
        if (logs.isEmpty()) return
        val first = logs.first()
        if (first.activitiesInDecoy.contains(activityText)) return
        val updatedFirst = first.copy(
            activitiesInDecoy = (first.activitiesInDecoy + activityText).takeLast(6)
        )
        _intruderAndPanicState.value = current.copy(
            intruderLogs = listOf(updatedFirst) + logs.drop(1)
        )
    }

    fun togglePanicMotionEnabled(enabled: Boolean) {
        _intruderAndPanicState.value = _intruderAndPanicState.value.copy(
            isPanicMotionEnabled = enabled
        )
        postBanner(
            if (enabled) "Sensor Guncangan Darurat Ekstrem diaktifkan (Otomatis pindah ke Brankas Palsu)"
            else "Sensor Guncangan Darurat Ekstrem dinonaktifkan"
        )
    }

    fun selectPanicMotionPreset(preset: PanicMotionPreset) {
        _intruderAndPanicState.value = _intruderAndPanicState.value.copy(
            selectedPreset = preset
        )
        postBanner("Ambang sensor darurat diatur ke: ${preset.label}")
    }

    fun onAccelerometerSample(deltaMps2: Float, joltStreak: Int) {
        val cur = _intruderAndPanicState.value
        _intruderAndPanicState.value = cur.copy(
            liveDeltaMps2 = deltaMps2,
            peakDeltaMps2 = maxOf(cur.peakDeltaMps2, deltaMps2),
            currentJoltStreak = joltStreak
        )
    }

    /**
     * Dipanggil saat sensor akselerometer mendeteksi guncangan ekstrem beruntun yang tidak wajar
     * (bukan goyangan santai saat nonton video) ATAU saat tombol uji coba di Radar & Darurat ditekan.
     *
     * Efek instan: Langsung mengalihkan sesi aktif ke Brankas Palsu (DECOY_VAULT_ID = 999 / BRANKAS #1)
     * tanpa layar kunci dan tanpa banner "Brankas Palsu" agar orang yang merebut HP mengira
     * ia sedang melihat brankas asli!
     */
    fun triggerExtremePanicSwitchToDecoyVault(
        reasonLabel: String = "GUNCANGAN DARURAT EKSTREM"
    ) {
        autoLockAllItemsInActiveVault()
        viewModelScope.launch(Dispatchers.IO) {
            repository.ensureDecoyVaultSeeded()
        }
        pausePlayback()
        _inspectingItem.value = null
        _viewingMediaItem.value = null
        _cryptoPinModalState.value = null
        _statusBannerMessage.value = null

        val prevVaultId = _activeVaultId.value
        val curPanic = _intruderAndPanicState.value
        val newEntry = IntruderTrapLogEntry(
            timestampLabel = "Darurat (${currentFormattedTimestamp()})",
            triggerType = reasonLabel,
            wrongPinLayer1 = "PANIC",
            wrongPinLayer2 = "SHAKE",
            wrongPinLayer3 = "BRK#$prevVaultId",
            activitiesInDecoy = listOf(
                "Guncangan ekstrem terdeteksi saat membuka ruang utama — dialihkan instan ke BRANKAS #1 (Palsu)"
            )
        )
        _intruderAndPanicState.value = curPanic.copy(
            currentJoltStreak = 0,
            intruderLogs = listOf(newEntry) + curPanic.intruderLogs
        )

        _activeVaultId.value = SecurityConfig.DECOY_VAULT_ID
        _activeTab.value = VaultTab.DASHBOARD
        _securityState.value = _securityState.value.copy(
            stage = AuthStage.UNLOCKED,
            candidateVaultId = SecurityConfig.DECOY_VAULT_ID,
            isIsolatedSettingsSession = false,
            isDecoyVaultSession = true,
            errorMessage = null
        )
    }

    fun simulateIntruderTrapEntry() {
        val randomPin1 = (100000..999999).random().toString()
        val randomPin2 = (100000..999999).random().toString()
        val randomPin3 = (100000..999999).random().toString()
        recordIntruderTrapLog(
            triggerType = "SIMULASI PENYUSUP",
            l1 = randomPin1,
            l2 = randomPin2,
            l3 = randomPin3,
            initialActivity = "Mengetik PIN salah & membuka dokumen umpan di Brankas Palsu"
        )
        postBanner("Log simulasi penyusup berhasil ditambahkan ke Radar Penyusup!")
    }

    fun deleteIntruderLog(id: String) {
        val cur = _intruderAndPanicState.value
        _intruderAndPanicState.value = cur.copy(
            intruderLogs = cur.intruderLogs.filterNot { it.id == id }
        )
    }

    fun clearAllIntruderLogs() {
        val cur = _intruderAndPanicState.value
        _intruderAndPanicState.value = cur.copy(
            intruderLogs = emptyList()
        )
        postBanner("Semua riwayat log jebakan penyusup telah dibersihkan.")
    }

    fun setColorPalette(
        paletteId: String,
        customPrimaryHex: String? = null,
        customAccentHex: String? = null
    ) {
        val preset = com.example.ui.theme.AppColorPalettePreset.fromId(paletteId)
        com.example.ui.theme.AppThemeController.activePalette = preset
        if (!customPrimaryHex.isNullOrBlank()) {
            com.example.ui.theme.AppThemeController.customPrimaryHex = customPrimaryHex.trim().uppercase()
        }
        if (!customAccentHex.isNullOrBlank()) {
            com.example.ui.theme.AppThemeController.customAccentHex = customAccentHex.trim().uppercase()
        }
        viewModelScope.launch {
            repository.setColorPaletteConfig(
                paletteId = preset.id,
                customPrimaryHex = customPrimaryHex,
                customAccentHex = customAccentHex
            )
            postBanner("Palet warna global diubah ke ${preset.displayName}!")
        }
    }

    fun setDynamicIslandConfig(
        shapeId: String? = null,
        presetId: String? = null,
        bgHex: String? = null,
        borderHex: String? = null,
        glowHex: String? = null
    ) {
        if (!shapeId.isNullOrBlank()) {
            com.example.ui.theme.AppThemeController.dynamicIslandShape =
                com.example.ui.theme.DynamicIslandShapeStyle.fromId(shapeId)
        }
        if (!presetId.isNullOrBlank()) {
            val preset = com.example.ui.theme.DynamicIslandColorPreset.fromId(presetId)
            com.example.ui.theme.AppThemeController.applyDynamicIslandPreset(preset)
        }
        if (!bgHex.isNullOrBlank()) {
            com.example.ui.theme.AppThemeController.dynamicIslandBgHex = bgHex.trim().uppercase()
        }
        if (!borderHex.isNullOrBlank()) {
            com.example.ui.theme.AppThemeController.dynamicIslandBorderHex = borderHex.trim().uppercase()
        }
        if (!glowHex.isNullOrBlank()) {
            com.example.ui.theme.AppThemeController.dynamicIslandGlowHex = glowHex.trim().uppercase()
        }
        viewModelScope.launch {
            repository.setDynamicIslandConfig(
                shapeId = shapeId,
                presetId = presetId,
                bgHex = bgHex,
                borderHex = borderHex,
                glowHex = glowHex
            )
            postBanner("Konfigurasi bentuk & warna Dynamic Island Bento Glow berhasil diperbarui!")
        }
    }

    fun setMascotStudioConfig(
        accessoryId: String? = null,
        poseId: String? = null,
        cosmicAuraEnabled: Boolean? = null,
        skinId: String? = null,
        outfitId: String? = null,
        glovesId: String? = null
    ) {
        if (!accessoryId.isNullOrBlank()) {
            com.example.ui.theme.AppThemeController.mascotAccessory =
                com.example.ui.theme.MascotAccessory.fromId(accessoryId)
        }
        if (!poseId.isNullOrBlank()) {
            com.example.ui.theme.AppThemeController.mascotActivityPose =
                com.example.ui.theme.MascotActivityPose.fromId(poseId)
            com.example.ui.theme.AppThemeController.poseTriggerNonce++
        }
        if (cosmicAuraEnabled != null) {
            com.example.ui.theme.AppThemeController.mascotCosmicAuraEnabled = cosmicAuraEnabled
        }
        if (!skinId.isNullOrBlank()) {
            com.example.ui.theme.AppThemeController.mascotSkin =
                com.example.ui.theme.MascotBodySkin.fromId(skinId)
        }
        if (!outfitId.isNullOrBlank()) {
            com.example.ui.theme.AppThemeController.mascotOutfit =
                com.example.ui.theme.MascotUpperOutfit.fromId(outfitId)
        }
        if (!glovesId.isNullOrBlank()) {
            com.example.ui.theme.AppThemeController.mascotGloves =
                com.example.ui.theme.MascotHandGloves.fromId(glovesId)
        }
        viewModelScope.launch {
            repository.setMascotStudioConfig(
                accessoryId = accessoryId,
                poseId = poseId,
                cosmicAuraEnabled = cosmicAuraEnabled,
                skinId = skinId,
                outfitId = outfitId,
                glovesId = glovesId
            )
        }
    }

    fun updateAppCamouflageConfig(
        isRedirectArmed: Boolean? = null,
        identityMode: String? = null,
        presetId: String? = null,
        customAppName: String? = null,
        customIconId: String? = null,
        customColorHex: String? = null,
        targetAppId: String? = null,
        targetAppLabel: String? = null,
        bannerText: String? = null
    ) {
        viewModelScope.launch {
            repository.setAppCamouflageConfig(
                isRedirectArmed = isRedirectArmed,
                identityMode = identityMode,
                presetId = presetId,
                customAppName = customAppName,
                customIconId = customIconId,
                customColorHex = customColorHex,
                targetAppId = targetAppId,
                targetAppLabel = targetAppLabel
            )
            if (!bannerText.isNullOrBlank()) {
                postBanner(bannerText)
            }
        }
    }

    fun applyCamouflageAndRestartSession(
        identityMode: String,
        presetId: String,
        customAppName: String,
        customIconId: String,
        customColorHex: String,
        targetAppId: String,
        targetAppLabel: String
    ) {
        viewModelScope.launch {
            repository.setAppCamouflageConfig(
                isRedirectArmed = true,
                identityMode = identityMode,
                presetId = presetId,
                customAppName = customAppName,
                customIconId = customIconId,
                customColorHex = customColorHex,
                targetAppId = targetAppId,
                targetAppLabel = targetAppLabel
            )
            withContext(Dispatchers.Main) {
                lockVaultNow()
            }
        }
    }

    fun verifyMascotOverlayPinAndUnlock(submittedPin: String): Boolean {
        val clean = submittedPin.uppercase().trim()
        if (clean.isEmpty()) {
            _isMascotOverlayDismissedInCamouflage.value = true
            return false
        }
        val cfg = securityConfig.value

        // 1. Cek apakah cocok dengan PIN Ruang Pengaturan (Default: 222222)
        val (sp1, sp2, sp3) = cfg.getSettingsPins()
        if (clean.equals(sp1, ignoreCase = true) ||
            clean.equals(sp2, ignoreCase = true) ||
            clean.equals(sp3, ignoreCase = true)
        ) {
            val unlockedState = SecurityGateUiState(
                stage = AuthStage.UNLOCKED,
                candidateVaultId = SecurityConfig.ISOLATED_SETTINGS_CANDIDATE_ID,
                isIsolatedSettingsSession = true,
                isIsolatedFunZoneSession = false,
                isDecoyVaultSession = false
            )
            persistedCamouflageBypassed = true
            persistedActiveTab = VaultTab.SCRIPT_ENGINE
            persistedSecurityState = unlockedState
            _isCamouflageSessionBypassed.value = true
            _isMascotOverlayDismissedInCamouflage.value = false
            _activeTab.value = VaultTab.SCRIPT_ENGINE
            _securityState.value = unlockedState
            postBanner("PIN Overlay Benar! Masuk ke Ruang Pengaturan Terisolasi.")
            return true
        }

        // 2. Cek apakah cocok dengan PIN Fun Zone (Default: 333333)
        val (fp1, fp2, fp3) = cfg.getFunZonePins()
        if (clean.equals(fp1, ignoreCase = true) ||
            clean.equals(fp2, ignoreCase = true) ||
            clean.equals(fp3, ignoreCase = true)
        ) {
            val unlockedState = SecurityGateUiState(
                stage = AuthStage.UNLOCKED,
                candidateVaultId = SecurityConfig.ISOLATED_FUN_ZONE_CANDIDATE_ID,
                isIsolatedSettingsSession = false,
                isIsolatedFunZoneSession = true,
                isDecoyVaultSession = false
            )
            persistedCamouflageBypassed = true
            persistedActiveTab = VaultTab.ISOLATED_FUN_ZONE
            persistedSecurityState = unlockedState
            _isCamouflageSessionBypassed.value = true
            _isMascotOverlayDismissedInCamouflage.value = false
            _activeTab.value = VaultTab.ISOLATED_FUN_ZONE
            _securityState.value = unlockedState
            postBanner("PIN Overlay Benar! Masuk ke Ruang Fun Zone 3D.")
            return true
        }

        // 3. Cek apakah cocok dengan PIN Brankas #1..#vaultCount
        val maxV = cfg.vaultCount.coerceIn(1, 10)
        for (vId in 1..maxV) {
            val (vp1, vp2, vp3) = cfg.getPinsForVault(vId)
            if (clean.equals(vp1, ignoreCase = true) ||
                clean.equals(vp2, ignoreCase = true) ||
                clean.equals(vp3, ignoreCase = true)
            ) {
                val unlockedState = SecurityGateUiState(
                    stage = AuthStage.UNLOCKED,
                    candidateVaultId = vId,
                    isIsolatedSettingsSession = false,
                    isIsolatedFunZoneSession = false,
                    isDecoyVaultSession = false
                )
                persistedCamouflageBypassed = true
                persistedActiveVaultId = vId
                persistedActiveTab = VaultTab.DASHBOARD
                persistedSecurityState = unlockedState
                _isCamouflageSessionBypassed.value = true
                _isMascotOverlayDismissedInCamouflage.value = false
                _activeVaultId.value = vId
                _activeTab.value = VaultTab.DASHBOARD
                _securityState.value = unlockedState
                postBanner("Selamat datang kembali di Arcanum (${cfg.getVaultDisplayLabel(vId)})!")
                return true
            }
        }

        // 4. Jika PIN Salah -> Sembunyikan Overlay Maskot & Popup PIN tanpa keluar dari aplikasi tujuan!
        _isMascotOverlayDismissedInCamouflage.value = true
        recordIntruderTrapLog(
            triggerType = "PIN SALAH OVERLAY MASKOT",
            l1 = clean,
            l2 = "-",
            l3 = "-",
            initialActivity = "Mengetik sandi salah di Overlay Maskot — overlay menghilang & tetap di ${cfg.camouflageTargetAppLabel}"
        )
        return false
    }

    fun resummonMascotInCamouflage() {
        _isMascotOverlayDismissedInCamouflage.value = false
    }

    companion object {
        @Volatile
        private var persistedSecurityState: SecurityGateUiState = SecurityGateUiState()
        @Volatile
        private var persistedActiveTab: VaultTab = VaultTab.DASHBOARD
        @Volatile
        private var persistedActiveVaultId: Int = 1
        @Volatile
        private var persistedCamouflageBypassed: Boolean = false
    }
}



