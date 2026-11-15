package com.example

import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Archive
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.CloudDownload
import androidx.compose.material.icons.filled.CloudUpload
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.FolderZip
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Radar
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.FileProvider
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.AuthStage
import com.example.ui.CreateVaultFileDialog
import com.example.ui.CryptoPinVerificationDialog
import com.example.ui.DuolingoGalaxyAmbientBackdrop
import com.example.ui.EncryptionScriptEngineScreen
import com.example.ui.FirstRunOnboardingWizardScreen
import com.example.ui.ForgotPinRecoveryDialog
import com.example.ui.IntruderAndPanicRadarScreen
import com.example.ui.InteractiveDraggableMascotOverlay
import com.example.ui.MoriDownloaderScreen
import com.example.ui.VaultStegoToolsScreen
import kotlin.math.abs
import kotlin.math.roundToInt
import kotlin.math.sqrt
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Star
import com.example.ui.theme.PastelSky
import com.example.ui.theme.PastelLavender
import com.example.ui.theme.PastelPeach
import com.example.ui.theme.CosmicPillDock
import com.example.ui.SecondaryBiometricReAuthDialog
import com.example.ui.SecurityGateScreen
import com.example.ui.VaultDashboardAndFilesContent
import com.example.ui.VaultFileInspectorSheet
import com.example.ui.VaultMediaPlayerScreen
import com.example.ui.VaultPhotoGalleryScreen
import com.example.ui.gsapMagneticMicroTouch
import androidx.compose.material.icons.filled.Collections
import com.example.ui.VaultRichMediaViewerOverlay
import com.example.ui.VaultTab
import com.example.ui.ExportPasswordDialog
import com.example.ui.ImportNyxPasswordDialog
import com.example.ui.ExportPasswordPromptState
import com.example.ui.ImportPasswordPromptState
import com.example.ui.VaultViewModel
import com.example.ui.ZipScannerToolScreen
import com.example.ui.instantTouchPress
import com.example.ui.theme.CosmicBorderHairline
import com.example.ui.theme.CosmicObsidian
import com.example.ui.theme.CosmicTextPure
import com.example.ui.theme.CosmicVoid
import com.example.ui.theme.CosmicNebulaMesh
import androidx.compose.ui.text.font.FontWeight
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.theme.NebulaViolet
import com.example.ui.theme.StarlightCyan
import com.example.ui.theme.VaultCream
import com.example.ui.theme.VaultLime
import com.example.ui.theme.VaultObsidian
import com.example.ui.theme.VaultPureWhite
import com.example.ui.theme.duolingoButton
import com.example.ui.theme.duolingoCard

class MainActivity : FragmentActivity() {
    private var latestIncomingIntent by mutableStateOf<Intent?>(null)
    private var incomingIntentNonce by mutableIntStateOf(0)

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        latestIncomingIntent = intent
        incomingIntentNonce++
        com.example.ui.ExternalBrowserLockdownController.extractTargetUrlFromIntent(intent)?.let { targetUrl ->
            com.example.ui.ExternalBrowserLockdownController.activateExternalBrowserLockdown(targetUrl)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        latestIncomingIntent = intent
        incomingIntentNonce++
        com.example.ui.ExternalBrowserLockdownController.extractTargetUrlFromIntent(intent)?.let { targetUrl ->
            com.example.ui.ExternalBrowserLockdownController.activateExternalBrowserLockdown(targetUrl)
        }

        setContent {
            val activeIntent = latestIncomingIntent
            val currentNonce = incomingIntentNonce

            val sharedText = if (activeIntent?.action == Intent.ACTION_SEND) {
                activeIntent.getStringExtra(Intent.EXTRA_TEXT)
            } else null

            val sharedStreamUri: Uri? = if (activeIntent?.action == Intent.ACTION_SEND) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    activeIntent.getParcelableExtra(Intent.EXTRA_STREAM, Uri::class.java)
                } else {
                    @Suppress("DEPRECATION")
                    activeIntent.getParcelableExtra(Intent.EXTRA_STREAM)
                }
            } else null

            val isWebBrowserViewIntent = com.example.ui.ExternalBrowserLockdownController.isWebBrowserIntent(activeIntent)

            val externalViewUri: Uri? = if (activeIntent?.action == Intent.ACTION_VIEW && !isWebBrowserViewIntent) {
                activeIntent.data
            } else null

            val externalViewMimeType: String? = if (activeIntent?.action == Intent.ACTION_VIEW && !isWebBrowserViewIntent) {
                activeIntent.type
            } else null

            MyApplicationTheme {
                TrantorVaultApp(
                    initialSharedText = sharedText,
                    initialSharedStreamUri = sharedStreamUri,
                    externalViewUri = externalViewUri,
                    externalViewMimeType = externalViewMimeType,
                    externalIntentNonce = currentNonce
                )
            }
        }
    }
}

@Composable
fun TrantorVaultApp(
    viewModel: VaultViewModel = viewModel(),
    initialSharedText: String? = null,
    initialSharedStreamUri: Uri? = null,
    externalViewUri: Uri? = null,
    externalViewMimeType: String? = null,
    externalIntentNonce: Int = 0
) {
    val context = LocalContext.current
    val lifecycleOwner = androidx.compose.ui.platform.LocalLifecycleOwner.current
    val securityState by viewModel.securityState.collectAsStateWithLifecycle()
    val securityConfig by viewModel.securityConfig.collectAsStateWithLifecycle()
    val activeVaultId by viewModel.activeVaultId.collectAsStateWithLifecycle()
    val activeTab by viewModel.activeTab.collectAsStateWithLifecycle()
    val allItems by viewModel.allItems.collectAsStateWithLifecycle()
    val filteredItems by viewModel.filteredItems.collectAsStateWithLifecycle()
    val selectedCategory by viewModel.selectedCategory.collectAsStateWithLifecycle()
    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
    val inspectingItem by viewModel.inspectingItem.collectAsStateWithLifecycle()
    val viewingMediaItem by viewModel.viewingMediaItem.collectAsStateWithLifecycle()
    val externalPreviewRequest by viewModel.externalPreviewRequest.collectAsStateWithLifecycle()
    val cryptoPinModalState by viewModel.cryptoPinModalState.collectAsStateWithLifecycle()
    val biometricExportModalState by viewModel.biometricExportModalState.collectAsStateWithLifecycle()
    val exportPasswordPromptState by viewModel.exportPasswordPromptState.collectAsStateWithLifecycle()
    val importPasswordPromptState by viewModel.importPasswordPromptState.collectAsStateWithLifecycle()
    val showForgotPinModal by viewModel.showForgotPinModal.collectAsStateWithLifecycle()
    val mediaState by viewModel.mediaState.collectAsStateWithLifecycle()
    val zipScannerState by viewModel.zipScannerState.collectAsStateWithLifecycle()
    val moriState by viewModel.moriState.collectAsStateWithLifecycle()
    val stegoState by viewModel.stegoState.collectAsStateWithLifecycle()
    val intruderAndPanicState by viewModel.intruderAndPanicState.collectAsStateWithLifecycle()
    val statusBannerMessage by viewModel.statusBannerMessage.collectAsStateWithLifecycle()
    val activeMediaFolder by viewModel.activeMediaFolder.collectAsStateWithLifecycle()
    val isCalculatorCamouflageActive by viewModel.isCalculatorCamouflageActive.collectAsStateWithLifecycle()
    val isCamouflageSessionBypassed by viewModel.isCamouflageSessionBypassed.collectAsStateWithLifecycle()
    val isMascotOverlayDismissedInCamouflage by viewModel.isMascotOverlayDismissedInCamouflage.collectAsStateWithLifecycle()
    val allVaultsItems by viewModel.allVaultsItems.collectAsStateWithLifecycle()
    val cloudBackupEntries by viewModel.cloudBackupEntries.collectAsStateWithLifecycle()

    DisposableEffect(lifecycleOwner, context) {
        val observer = androidx.lifecycle.LifecycleEventObserver { _, event ->
            if (event == androidx.lifecycle.Lifecycle.Event.ON_RESUME) {
                val pendingPin = com.example.ui.SystemMascotOverlayManager.consumePendingVerifiedPin()
                    ?: (context as? android.app.Activity)?.intent?.getStringExtra("EXTRA_OVERLAY_VERIFIED_PIN")?.also {
                        (context as? android.app.Activity)?.intent?.removeExtra("EXTRA_OVERLAY_VERIFIED_PIN")
                    }
                if (!pendingPin.isNullOrBlank()) {
                    viewModel.verifyMascotOverlayPinAndUnlock(pendingPin)
                }
            } else if (
                event == androidx.lifecycle.Lifecycle.Event.ON_STOP ||
                event == androidx.lifecycle.Lifecycle.Event.ON_PAUSE
            ) {
                // If user exits app or backgrounded while Chaos Brutal Overdrive is active, auto-revert to safe mode / Smooth!
                if (com.example.ui.theme.AppThemeController.graphicsQualityMode == com.example.ui.theme.AppGraphicsQualityMode.CHAOS_OVERDRIVE) {
                    com.example.ui.theme.AppThemeController.revertFromChaosOverdriveToSafeMode(
                        useSmoothFallback = false,
                        reasonMessage = "Keluar aplikasi terdeteksi: Chaos Brutal otomatis dikembalikan ke mode aman.",
                        context = context,
                        onPersistSafeMode = viewModel::setGraphicsQualityMode
                    )
                }
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    LaunchedEffect(Unit) {
        com.example.ui.ensureDefaultArcanumLauncherIdentityWhenDraftDisabled(context)
        com.example.ui.theme.AppThemeController.checkBootWatchdogAndSyncPro(
            context = context,
            onRevertPersistedMode = viewModel::setGraphicsQualityMode
        )
        viewModel.refreshCloudBackupEntries()
    }

    LaunchedEffect(
        securityConfig.visualThemeId,
        securityConfig.colorPaletteId,
        securityConfig.customPrimaryHex,
        securityConfig.customAccentHex,
        securityConfig.mascotAccessoryId,
        securityConfig.mascotPoseId,
        securityConfig.mascotCosmicAuraEnabled,
        securityConfig.mascotSkinId,
        securityConfig.mascotOutfitId,
        securityConfig.mascotGlovesId,
        securityConfig.dynamicIslandShapeId,
        securityConfig.dynamicIslandPresetId,
        securityConfig.dynamicIslandBgHex,
        securityConfig.dynamicIslandBorderHex,
        securityConfig.dynamicIslandGlowHex,
        securityConfig.graphicsQualityModeId,
        securityConfig.transparentButtonBoxEnabled
    ) {
        com.example.ui.theme.AppThemeController.activeTheme =
            com.example.ui.theme.AppVisualTheme.fromId(securityConfig.visualThemeId)
        if (com.example.ui.theme.AppThemeController.graphicsQualityMode != com.example.ui.theme.AppGraphicsQualityMode.CHAOS_OVERDRIVE) {
            com.example.ui.theme.AppThemeController.graphicsQualityMode =
                com.example.ui.theme.AppGraphicsQualityMode.fromId(securityConfig.graphicsQualityModeId)
        }
        com.example.ui.theme.AppThemeController.transparentButtonBoxEnabled =
            securityConfig.transparentButtonBoxEnabled
        com.example.ui.theme.AppThemeController.activePalette =
            com.example.ui.theme.AppColorPalettePreset.fromId(securityConfig.colorPaletteId)
        com.example.ui.theme.AppThemeController.customPrimaryHex = securityConfig.customPrimaryHex
        com.example.ui.theme.AppThemeController.customAccentHex = securityConfig.customAccentHex
        com.example.ui.theme.AppThemeController.mascotAccessory =
            com.example.ui.theme.MascotAccessory.fromId(securityConfig.mascotAccessoryId)
        com.example.ui.theme.AppThemeController.mascotActivityPose =
            com.example.ui.theme.MascotActivityPose.fromId(securityConfig.mascotPoseId)
        com.example.ui.theme.AppThemeController.mascotCosmicAuraEnabled =
            securityConfig.mascotCosmicAuraEnabled
        com.example.ui.theme.AppThemeController.mascotSkin =
            com.example.ui.theme.MascotBodySkin.fromId(securityConfig.mascotSkinId)
        com.example.ui.theme.AppThemeController.mascotOutfit =
            com.example.ui.theme.MascotUpperOutfit.fromId(securityConfig.mascotOutfitId)
        com.example.ui.theme.AppThemeController.mascotGloves =
            com.example.ui.theme.MascotHandGloves.fromId(securityConfig.mascotGlovesId)
        com.example.ui.theme.AppThemeController.dynamicIslandShape =
            com.example.ui.theme.DynamicIslandShapeStyle.fromId(securityConfig.dynamicIslandShapeId)
        com.example.ui.theme.AppThemeController.dynamicIslandPreset =
            com.example.ui.theme.DynamicIslandColorPreset.fromId(securityConfig.dynamicIslandPresetId)
        if (securityConfig.dynamicIslandBgHex.isNotBlank()) {
            com.example.ui.theme.AppThemeController.dynamicIslandBgHex = securityConfig.dynamicIslandBgHex
        }
        if (securityConfig.dynamicIslandBorderHex.isNotBlank()) {
            com.example.ui.theme.AppThemeController.dynamicIslandBorderHex = securityConfig.dynamicIslandBorderHex
        }
        if (securityConfig.dynamicIslandGlowHex.isNotBlank()) {
            com.example.ui.theme.AppThemeController.dynamicIslandGlowHex = securityConfig.dynamicIslandGlowHex
        }
    }

    LaunchedEffect(securityState.stage) {
        if (securityState.stage == AuthStage.UNLOCKED) {
            com.example.ui.theme.AppThemeController.proLiveFpsHudEnabled = false
        }
    }

    val isSanctuaryDarkSession = securityState.stage == AuthStage.UNLOCKED &&
        (securityState.isIsolatedSanctuarySession || activeTab == VaultTab.ISOLATED_SANCTUARY)

    LaunchedEffect(viewingMediaItem, mediaState.isPlaying, activeTab, securityState.isIsolatedSanctuarySession, securityState.stage) {
        com.example.ui.theme.AppThemeController.isMediaWatchingActive =
            viewingMediaItem != null ||
                mediaState.isPlaying ||
                activeTab == VaultTab.MEDIA_PLAYER ||
                activeTab == VaultTab.PHOTO_GALLERY
        com.example.ui.theme.AppThemeController.isSanctuaryBentoGlowLocked = isSanctuaryDarkSession
        if (!isSanctuaryDarkSession) {
            com.example.ui.theme.AppThemeController.isSanctuaryBrowserActive = false
        }
    }

    // Sync Android System Status Bar (Notch) & Bottom Navigation Bar to Pitch-Black (#050811) in BlackBox Sanctuary
    DisposableEffect(context, isSanctuaryDarkSession) {
        val window = (context as? android.app.Activity)?.window
        if (window != null) {
            val pitchBlack = android.graphics.Color.parseColor("#050811")
            val insetsController = androidx.core.view.WindowCompat.getInsetsController(window, window.decorView)
            if (isSanctuaryDarkSession) {
                @Suppress("DEPRECATION")
                window.statusBarColor = pitchBlack
                @Suppress("DEPRECATION")
                window.navigationBarColor = pitchBlack
                window.decorView.setBackgroundColor(pitchBlack)
                insetsController.isAppearanceLightStatusBars = false
                insetsController.isAppearanceLightNavigationBars = false
            } else {
                @Suppress("DEPRECATION")
                window.statusBarColor = android.graphics.Color.TRANSPARENT
                @Suppress("DEPRECATION")
                window.navigationBarColor = android.graphics.Color.TRANSPARENT
                insetsController.isAppearanceLightStatusBars = true
                insetsController.isAppearanceLightNavigationBars = true
            }
        }
        onDispose { }
    }

    // Global Anti-Screenshot & Anti-Screen-Record Mode (FLAG_SECURE across entire app window -> Black Screen on Record)
    DisposableEffect(context, securityConfig.isAntiScreenshotEnabled) {
        val window = (context as? android.app.Activity)?.window
        if (window != null) {
            if (securityConfig.isAntiScreenshotEnabled) {
                window.addFlags(android.view.WindowManager.LayoutParams.FLAG_SECURE)
            } else {
                window.clearFlags(android.view.WindowManager.LayoutParams.FLAG_SECURE)
            }
        }
        onDispose { }
    }

    var showCreateSecretDialog by remember { mutableStateOf(false) }
    var showFeedbackReportDialog by remember { mutableStateOf(false) }
    var isMascotOverlayVisible by remember { mutableStateOf(true) }
    var mascotResummonNonce by remember { mutableIntStateOf(0) }

    // Real Hardware Accelerometer Listener with Anti-False-Positive Multi-Jolt Filter
    // Ignores normal movement / watching videos; only triggers on consecutive extreme jolts
    DisposableEffect(
        context,
        securityState.stage,
        intruderAndPanicState.isPanicMotionEnabled,
        intruderAndPanicState.selectedPreset,
        activeVaultId
    ) {
        val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as? SensorManager
        val accelerometer = sensorManager?.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        val joltTimestamps = mutableListOf<Long>()
        var lastPeakTimestamp = 0L
        var lastUiTelemetryUpdate = 0L
        var lastTriggeredTimestamp = 0L

        val listener = object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent?) {
                if (event == null || event.values.size < 3) return
                val ax = event.values[0]
                val ay = event.values[1]
                val az = event.values[2]
                val magnitude = sqrt(ax * ax + ay * ay + az * az)
                val deltaMps2 = abs(magnitude - SensorManager.GRAVITY_EARTH)

                val now = System.currentTimeMillis()
                val preset = intruderAndPanicState.selectedPreset
                // Prune jolts older than 900ms window
                joltTimestamps.removeAll { now - it > 900L }

                if (deltaMps2 >= preset.thresholdDeltaMps2 && now - lastPeakTimestamp >= 160L) {
                    lastPeakTimestamp = now
                    joltTimestamps.add(now)
                }

                if (now - lastUiTelemetryUpdate >= 120L || joltTimestamps.isNotEmpty()) {
                    lastUiTelemetryUpdate = now
                    viewModel.onAccelerometerSample(deltaMps2, joltTimestamps.size)
                }

                if (intruderAndPanicState.isPanicMotionEnabled &&
                    securityState.stage == AuthStage.UNLOCKED &&
                    !com.example.data.SecurityConfig.isDecoyVault(activeVaultId) &&
                    joltTimestamps.size >= preset.requiredJoltsInWindow &&
                    now - lastTriggeredTimestamp > 2000L
                ) {
                    lastTriggeredTimestamp = now
                    joltTimestamps.clear()
                    viewModel.triggerExtremePanicSwitchToDecoyVault(
                        reasonLabel = "SENSOR GUNCANGAN EKSTREM (${preset.label})"
                    )
                }
            }

            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) = Unit
        }

        if (sensorManager != null && accelerometer != null && securityState.stage == AuthStage.UNLOCKED) {
            sensorManager.registerListener(listener, accelerometer, SensorManager.SENSOR_DELAY_GAME)
        }

        onDispose {
            sensorManager?.unregisterListener(listener)
        }
    }

    // Forward incoming Android Share Sheet links/media (like coflyn/Mori Quick Save) once unlocked
    LaunchedEffect(securityState.stage, initialSharedText, initialSharedStreamUri) {
        if (securityState.stage == AuthStage.UNLOCKED &&
            (!initialSharedText.isNullOrBlank() || initialSharedStreamUri != null)
        ) {
            viewModel.handleIncomingSharedIntent(initialSharedText, initialSharedStreamUri)
        }
    }

    val isExternalBrowserLocked = com.example.ui.ExternalBrowserLockdownController.isExternalBrowserLockdownActive
    val externalBrowserUrl = com.example.ui.ExternalBrowserLockdownController.latestExternalBrowserUrl
    val externalBrowserNonce = com.example.ui.ExternalBrowserLockdownController.externalBrowserNonce

    // If External Link Browser Lockdown is triggered, immediately lock any active vault state in the background
    LaunchedEffect(isExternalBrowserLocked, externalBrowserNonce) {
        if (isExternalBrowserLocked) {
            viewModel.lockVaultNow()
        }
    }

    // Immediate "Buka Dengan Arcanum" (ACTION_VIEW) Quick Preview trigger for local files (content:// or file://)
    LaunchedEffect(externalViewUri, externalIntentNonce) {
        if (externalViewUri != null) {
            val scheme = externalViewUri.scheme?.lowercase()
            if (scheme == "http" || scheme == "https" || scheme == "about") {
                com.example.ui.ExternalBrowserLockdownController.activateExternalBrowserLockdown(
                    externalViewUri.toString()
                )
            } else {
                viewModel.openExternalFileForQuickPreview(externalViewUri, externalViewMimeType)
            }
        }
    }

    // System Multi-Document Picker for importing files, media, or .ZIP archives
    val documentPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenMultipleDocuments()
    ) { uris ->
        if (uris.isNotEmpty()) {
            uris.forEach { uri ->
                try {
                    context.contentResolver.takePersistableUriPermission(
                        uri,
                        Intent.FLAG_GRANT_READ_URI_PERMISSION
                    )
                } catch (_: Exception) {
                    // VaultRepository copies bytes immediately into internal private storage
                }
            }
            viewModel.importUrisFromPicker(uris)
        }
    }

    // System Save-As Document Picker for exporting individual files from Media Player after secondary biometric auth
    val saveExportDocumentLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.CreateDocument("*/*")
    ) { targetUri ->
        if (targetUri != null) {
            viewModel.completeExportToDocumentUri(targetUri)
        }
    }

    // Forgot PIN / Password Security Recovery Dialog (Accessible from Lock Screen or Inside Vault)
    if (showForgotPinModal) {
        ForgotPinRecoveryDialog(
            securityConfig = securityConfig,
            onResetPinsAndUnlock = viewModel::resetPinsFromRecoveryAndUnlock,
            onDismiss = viewModel::dismissForgotPinModal
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(if (isSanctuaryDarkSession || isExternalBrowserLocked) Color(0xFF050811) else CosmicVoid)
    ) {
        when {
            // -1. External Link Browser Lockdown Mode (Auto-opens Arcanum Browser when a link is clicked outside the app;
            // strictly blocks PIN Gate & Vaults until the user Force Closes / Restarts the app!)
            isExternalBrowserLocked -> {
                androidx.compose.foundation.layout.Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color(0xFF050811))
                        .padding(top = 24.dp)
                        .testTag("external_browser_lockdown_root")
                ) {
                    if (statusBannerMessage != null) {
                        com.example.ui.LegacyVaultStatusBanner(
                            statusBannerMessage = statusBannerMessage,
                            isDarkMode = true,
                            onDismissStatusBanner = viewModel::dismissBanner,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                    com.example.ui.SanctuaryUnifiedBrowserScreen(
                        onCloseToSanctuary = {
                            viewModel.postBanner(
                                "Mode Browser Eksternal Terkunci • Untuk membuka PIN / Brankas, wajib Force Close atau Restart aplikasi Arcanum!"
                            )
                        },
                        onPostBanner = viewModel::postBanner,
                        initialExternalUrl = externalBrowserUrl,
                        externalUrlNonce = externalBrowserNonce,
                        isExternalLinkLockdownMode = true
                    )
                }
            }

            // 0. 1-Click Instant Calculator Camouflage Overlay (#9)
            isCalculatorCamouflageActive -> {
                val unlockPins = remember(securityConfig) {
                    buildSet {
                        for (vId in 1..securityConfig.vaultCount) {
                            val (p1, p2, p3) = securityConfig.getPinsForVault(vId)
                            add(p1)
                            add(p2)
                            add(p3)
                        }
                        val (sp1, sp2, sp3) = securityConfig.getSettingsPins()
                        add(sp1)
                        add(sp2)
                        add(sp3)
                    }
                }
                com.example.ui.InstantCalculatorCamouflageOverlay(
                    secretUnlockPins = unlockPins,
                    onExitCamouflageToVault = { viewModel.setCalculatorCamouflageActive(false) }
                )
            }

            // 1. First-Open Onboarding & Custom PIN + Fingerprint + Recovery Setup Wizard (Google Login is Optional)
            !securityConfig.isFirstRunCompleted -> {
                FirstRunOnboardingWizardScreen(
                    onCompleteSetup = { v1p1, v1p2, v1p3, v2p1, v2p2, v2p3, bio, q1, a1, q2, a2, q3, a3, gEmail, gName, funP1, funP2, funP3, aurP1, aurP2, aurP3 ->
                        viewModel.completeFirstRunOnboarding(
                            v1p1, v1p2, v1p3, v2p1, v2p2, v2p3, bio,
                            q1, a1, q2, a2, q3, a3, gEmail, gName,
                            funP1, funP2, funP3,
                            aurP1, aurP2, aurP3
                        )
                    }
                )
            }

            // 2. Auto-Redirect Target App Camouflage + Floating Mascot Overlay + In-Place PIN Overlay (DRAFT: Disembunyikan saat DRAFT_APP_CAMOUFLAGE_FEATURE_ENABLED == false)
            com.example.ui.DRAFT_APP_CAMOUFLAGE_FEATURE_ENABLED &&
                securityConfig.isCamouflageRedirectArmed &&
                !isCamouflageSessionBypassed &&
                securityState.stage != AuthStage.UNLOCKED -> {
                com.example.ui.CamouflageTargetAppHostWithMascotPinOverlay(
                    securityConfig = securityConfig,
                    isMascotDismissedByWrongPin = isMascotOverlayDismissedInCamouflage,
                    onSubmitOverlayPin = viewModel::verifyMascotOverlayPinAndUnlock,
                    onSecretResummonMascot = viewModel::resummonMascotInCamouflage
                )
            }

            // 3. 3-Layer 6-Digit PIN + Genuine Fingerprint Security Gate (Non-Overlapping Dynamic Island Header)
            securityState.stage != AuthStage.UNLOCKED -> {
                androidx.compose.foundation.layout.Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(CosmicVoid)
                        .padding(top = 24.dp)
                ) {
                    if (com.example.ui.theme.AppThemeController.isProModeUnlocked) {
                        com.example.ui.InteractiveDynamicIslandCapsule(
                            activeVaultId = com.example.data.SecurityConfig.displayVaultNumber(activeVaultId),
                            allItems = allItems,
                            isDownloading = false,
                            downloadProgressPercent = 0,
                            statusBannerMessage = statusBannerMessage,
                            onDismissStatusBanner = viewModel::dismissBanner,
                            isUnlocked = false,
                            authStageLabel = "Lapis PIN Aktif: ${securityState.stage.name} • Ketuk untuk detail",
                            onPostBanner = viewModel::postBanner,
                            modifier = Modifier.fillMaxWidth()
                        )
                    } else {
                        com.example.ui.LegacyVaultStatusBanner(
                            statusBannerMessage = statusBannerMessage,
                            isDarkMode = true,
                            onDismissStatusBanner = viewModel::dismissBanner,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth()
                    ) {
                        SecurityGateScreen(
                            securityState = securityState,
                            onDigitClick = viewModel::onPinDigitPressed,
                            onCharClick = viewModel::onPinCharPressed,
                            onBackspaceClick = viewModel::onPinBackspacePressed,
                            onClearClick = viewModel::onPinClearPressed,
                            onBiometricSuccess = viewModel::onBiometricAuthenticatedSuccess,
                            onBiometricFeedback = viewModel::onBiometricAuthenticationError,
                            onForgotPinClick = viewModel::openForgotPinModal
                        )
                    }
                }
            }

            // 3. Main Unlocked Vault Workspace
            else -> {
                // Back button behavior for overlays, isolated settings room, and secondary tabs
                BackHandler(
                    enabled = exportPasswordPromptState != null ||
                            importPasswordPromptState != null ||
                            biometricExportModalState != null ||
                            showForgotPinModal ||
                            viewingMediaItem != null ||
                            cryptoPinModalState != null ||
                            securityState.isIsolatedSettingsSession ||
                            securityState.isIsolatedFunZoneSession ||
                            securityState.isIsolatedSanctuarySession ||
                            securityState.isIsolatedAuralysSession ||
                            activeTab != VaultTab.DASHBOARD
                ) {
                    when {
                        exportPasswordPromptState != null -> viewModel.dismissExportPasswordModal()
                        importPasswordPromptState != null -> viewModel.dismissImportPasswordModal()
                        biometricExportModalState != null -> viewModel.dismissBiometricExportModal()
                        showForgotPinModal -> viewModel.dismissForgotPinModal()
                        cryptoPinModalState != null -> viewModel.dismissCryptoPinModal()
                        viewingMediaItem != null -> viewModel.openViewingMediaModal(null)
                        securityState.isIsolatedAuralysSession -> {
                            viewModel.lockVaultNow()
                        }
                        securityState.isIsolatedSanctuarySession -> {
                            viewModel.lockVaultNow()
                        }
                        securityState.isIsolatedFunZoneSession -> {
                            viewModel.lockVaultNow()
                        }
                        securityState.isIsolatedSettingsSession && activeTab != VaultTab.SCRIPT_ENGINE -> {
                            viewModel.selectTab(VaultTab.SCRIPT_ENGINE)
                        }
                        securityState.isIsolatedSettingsSession -> {
                            viewModel.lockVaultNow()
                        }
                        activeTab != VaultTab.DASHBOARD -> viewModel.selectTab(VaultTab.DASHBOARD)
                    }
                }

                val currentVisualTheme = com.example.ui.theme.AppThemeController.activeTheme
                val currentColorPalette = com.example.ui.theme.AppThemeController.activePalette

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    containerColor = if (isSanctuaryDarkSession) Color(0xFF050811) else CosmicVoid,
                    contentWindowInsets = WindowInsets.safeDrawing,
                    bottomBar = {
                        if (securityState.isIsolatedSanctuarySession || securityState.isIsolatedFunZoneSession || securityState.isIsolatedAuralysSession) {
                            // Isolated Sanctuary, Isolated Fun Zone & Isolated Auralys have their own internal navigation dock & lock button
                        } else if (securityState.isIsolatedSettingsSession) {
                            IsolatedSettingsBottomBar(
                                activeTab = activeTab,
                                onSelectTab = viewModel::selectTab,
                                onLockAndExit = viewModel::lockVaultNow
                            )
                        } else {
                            TrantorDockedBottomBar(
                                activeTab = activeTab,
                                onSelectTab = viewModel::selectTab
                            )
                        }
                    }
                ) { innerPadding ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                if (isSanctuaryDarkSession) {
                                    androidx.compose.ui.graphics.Brush.verticalGradient(
                                        listOf(
                                            Color(0xFF050811),
                                            Color(0xFF070B14),
                                            Color(0xFF050811)
                                        )
                                    )
                                } else {
                                    CosmicNebulaMesh
                                }
                            )
                            .padding(innerPadding)
                    ) {
                        // Subtle 6-Theme Ambient Backdrop (Skipped in Pitch-Black Sanctuary Mode)
                        if (!isSanctuaryDarkSession) {
                            DuolingoGalaxyAmbientBackdrop()
                        }
                        val _keepThemeSubscribed = currentVisualTheme.id + currentColorPalette.id

                        val effectiveTab = if (securityState.isIsolatedSanctuarySession) {
                            VaultTab.ISOLATED_SANCTUARY
                        } else if (securityState.isIsolatedFunZoneSession) {
                            VaultTab.ISOLATED_FUN_ZONE
                        } else if (securityState.isIsolatedAuralysSession) {
                            VaultTab.ISOLATED_AURALYS
                        } else if (securityState.isIsolatedSettingsSession) {
                            when (activeTab) {
                                VaultTab.PRO_STUDIO -> VaultTab.PRO_STUDIO
                                VaultTab.ZIP_SCANNER -> VaultTab.ZIP_SCANNER
                                VaultTab.INTRUDER_PANIC_RADAR -> VaultTab.INTRUDER_PANIC_RADAR
                                VaultTab.THEME_STUDIO -> VaultTab.THEME_STUDIO
                                VaultTab.APP_CAMOUFLAGE -> if (com.example.ui.DRAFT_APP_CAMOUFLAGE_FEATURE_ENABLED) {
                                    VaultTab.APP_CAMOUFLAGE
                                } else {
                                    VaultTab.SCRIPT_ENGINE
                                }
                                else -> VaultTab.SCRIPT_ENGINE
                            }
                        } else {
                            activeTab
                        }
                        androidx.compose.foundation.layout.Column(
                            modifier = Modifier.fillMaxSize()
                        ) {
                            // Chaos Overdrive 3s Safety Timer, Mandatory Warning Modal & Anti-Force-Close Watchdog
                            com.example.ui.ChaosOverdriveWarningAndWatchdogOverlay(
                                onPersistSafeMode = viewModel::setGraphicsQualityMode
                            )
                            // Pro Feature #3: Live FPS Telemetry & Cyber Security Audit HUD
                            com.example.ui.ProLiveFpsAndSecurityAuditHud()

                            // Dynamic Island is now a Pro feature (off inside Sanctuary Browser);
                            // Non-Pro users receive the classic LegacyVaultStatusBanner notification UI.
                            val isInBrowserWing = isSanctuaryDarkSession && com.example.ui.theme.AppThemeController.isSanctuaryBrowserActive
                            if (!isInBrowserWing) {
                                if (com.example.ui.theme.AppThemeController.isProModeUnlocked) {
                                    com.example.ui.InteractiveDynamicIslandCapsule(
                                        activeVaultId = com.example.data.SecurityConfig.displayVaultNumber(activeVaultId),
                                        allItems = allItems,
                                        isDownloading = moriState.isDownloading || moriState.isAnalyzing,
                                        downloadProgressPercent = (moriState.progressRatio * 100f).toInt().coerceIn(0, 100),
                                        downloadStatusText = moriState.statusLogLine,
                                        isScanningZip = zipScannerState.isScanning,
                                        scanStatusText = zipScannerState.progressMessage,
                                        isProcessingStegoOrCrypto = activeTab == VaultTab.STEGO_TOOLS && stegoState.decodedPayload != null,
                                        stegoStatusText = stegoState.decodeErrorMessage ?: "",
                                        isPlayingMedia = mediaState.isPlaying,
                                        statusBannerMessage = statusBannerMessage,
                                        onDismissStatusBanner = viewModel::dismissBanner,
                                        isUnlocked = true,
                                        onOpenThemeStudioClick = { viewModel.selectTab(VaultTab.THEME_STUDIO) },
                                        onPostBanner = viewModel::postBanner,
                                        modifier = Modifier.fillMaxWidth()
                                    )
                                } else {
                                    com.example.ui.LegacyVaultStatusBanner(
                                        statusBannerMessage = statusBannerMessage,
                                        isDownloading = moriState.isDownloading || moriState.isAnalyzing,
                                        downloadProgressPercent = (moriState.progressRatio * 100f).toInt().coerceIn(0, 100),
                                        downloadStatusText = moriState.statusLogLine,
                                        isScanningZip = zipScannerState.isScanning,
                                        scanStatusText = zipScannerState.progressMessage,
                                        isProcessingStegoOrCrypto = activeTab == VaultTab.STEGO_TOOLS && stegoState.decodedPayload != null,
                                        stegoStatusText = stegoState.decodeErrorMessage ?: "",
                                        isDarkMode = isSanctuaryDarkSession,
                                        onDismissStatusBanner = viewModel::dismissBanner,
                                        modifier = Modifier.fillMaxWidth()
                                    )
                                }
                            }
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .fillMaxWidth()
                            ) {
                        com.example.ui.GsapSharedMorphContainer(
                            targetState = effectiveTab,
                            modifier = Modifier.fillMaxSize(),
                            label = "main_workspace_gsap_morph"
                        ) { currentEffectiveTab ->
                        when (currentEffectiveTab) {
                            VaultTab.DASHBOARD -> {
                                VaultDashboardAndFilesContent(
                                    activeVaultId = activeVaultId,
                                    isFilesOnlyMode = false,
                                    allItems = allItems,
                                    filteredItems = filteredItems,
                                    selectedCategory = selectedCategory,
                                    searchQuery = searchQuery,
                                    securityConfig = securityConfig,
                                    onSelectCategory = viewModel::selectCategory,
                                    onSearchQueryChange = viewModel::updateSearchQuery,
                                    onToggleEncryptItemWithPin = { item ->
                                        viewModel.requestToggleEncryptionWithPin(item, openAfterDecrypt = false)
                                    },
                                    onBatchEncryptAllWithPin = viewModel::requestBatchEncryptionWithPin,
                                    onInspectItem = viewModel::inspectItem,
                                    onOpenOrPlayItem = viewModel::playOrViewItem,
                                    onDeleteItem = viewModel::deleteVaultItem,
                                    onImportFromDeviceClick = {
                                        documentPickerLauncher.launch(arrayOf("*/*"))
                                    },
                                    onCreateSecretFileClick = { showCreateSecretDialog = true },
                                    onOpenScriptEngineClick = { viewModel.selectTab(VaultTab.SCRIPT_ENGINE) },
                                    onSwitchVaultModeClick = viewModel::switchVaultModeWithLock,
                                    onLockVaultClick = viewModel::lockVaultNow,
                                    onOpenZipScannerToolClick = { viewModel.selectTab(VaultTab.ZIP_SCANNER) },
                                    onOpenMoriDownloaderClick = {
                                        viewModel.selectTab(VaultTab.MORI_DOWNLOADER)
                                    },
                                    onRenameFolder = viewModel::renameFolderOrPlaylist,
                                    onDeleteFolder = viewModel::deleteFolderOrPlaylist,
                                    onMoveItemToFolder = viewModel::moveMediaItemToPlaylist,
                                    onActivateCalculatorCamouflage = {
                                        viewModel.setCalculatorCamouflageActive(true)
                                    },
                                    isMascotOverlayVisible = isMascotOverlayVisible,
                                    onSummonMascotOverlayClick = {
                                        isMascotOverlayVisible = true
                                        mascotResummonNonce++
                                        viewModel.postBanner("Maskot Overlay ditampilkan kembali di layar!")
                                    },
                                    onOpenFeedbackReportClick = {
                                        showFeedbackReportDialog = true
                                    },
                                    cloudBackupEntries = cloudBackupEntries,
                                    onBackupSingleItemToCloud = { item ->
                                        viewModel.backupSingleItemToGoogleCloud(item)
                                    },
                                    onOpenCloudBackupTabClick = {
                                        viewModel.selectTab(VaultTab.CLOUD_BACKUP)
                                    },
                                    onToggleAntiScreenshot = viewModel::toggleAntiScreenshotMode
                                )
                            }

                            VaultTab.FILES -> {
                                VaultDashboardAndFilesContent(
                                    activeVaultId = activeVaultId,
                                    isFilesOnlyMode = true,
                                    allItems = allItems,
                                    filteredItems = filteredItems,
                                    selectedCategory = selectedCategory,
                                    searchQuery = searchQuery,
                                    securityConfig = securityConfig,
                                    onSelectCategory = viewModel::selectCategory,
                                    onSearchQueryChange = viewModel::updateSearchQuery,
                                    onToggleEncryptItemWithPin = { item ->
                                        viewModel.requestToggleEncryptionWithPin(item, openAfterDecrypt = false)
                                    },
                                    onBatchEncryptAllWithPin = viewModel::requestBatchEncryptionWithPin,
                                    onInspectItem = viewModel::inspectItem,
                                    onOpenOrPlayItem = viewModel::playOrViewItem,
                                    onDeleteItem = viewModel::deleteVaultItem,
                                    onImportFromDeviceClick = {
                                        documentPickerLauncher.launch(arrayOf("*/*"))
                                    },
                                    onCreateSecretFileClick = { showCreateSecretDialog = true },
                                    onOpenScriptEngineClick = { viewModel.selectTab(VaultTab.SCRIPT_ENGINE) },
                                    onSwitchVaultModeClick = viewModel::switchVaultModeWithLock,
                                    onLockVaultClick = viewModel::lockVaultNow,
                                    onOpenZipScannerToolClick = { viewModel.selectTab(VaultTab.ZIP_SCANNER) },
                                    onOpenMoriDownloaderClick = { viewModel.selectTab(VaultTab.MORI_DOWNLOADER) },
                                    onRenameFolder = viewModel::renameFolderOrPlaylist,
                                    onDeleteFolder = viewModel::deleteFolderOrPlaylist,
                                    onMoveItemToFolder = viewModel::moveMediaItemToPlaylist,
                                    onActivateCalculatorCamouflage = {
                                        viewModel.setCalculatorCamouflageActive(true)
                                    },
                                    isMascotOverlayVisible = isMascotOverlayVisible,
                                    onSummonMascotOverlayClick = {
                                        isMascotOverlayVisible = true
                                        mascotResummonNonce++
                                        viewModel.postBanner("Maskot Overlay ditampilkan kembali di layar!")
                                    },
                                    onOpenFeedbackReportClick = {
                                        showFeedbackReportDialog = true
                                    },
                                    cloudBackupEntries = cloudBackupEntries,
                                    onBackupSingleItemToCloud = { item ->
                                        viewModel.backupSingleItemToGoogleCloud(item)
                                    },
                                    onOpenCloudBackupTabClick = {
                                        viewModel.selectTab(VaultTab.CLOUD_BACKUP)
                                    },
                                    onToggleAntiScreenshot = viewModel::toggleAntiScreenshotMode
                                )
                            }

                            VaultTab.MORI_DOWNLOADER -> {
                                MoriDownloaderScreen(
                                    activeVaultId = com.example.data.SecurityConfig.displayVaultNumber(activeVaultId),
                                    moriState = moriState,
                                    allVaultItems = allItems,
                                    onUrlInputChange = viewModel::updateMoriUrlInput,
                                    onCustomFileNameChange = viewModel::updateMoriCustomFileName,
                                    onSelectExtractionMode = viewModel::selectMoriExtractionMode,
                                    onToggleEncryptImmediately = viewModel::toggleMoriEncryptImmediately,
                                    onAnalyzeLinkClick = viewModel::analyzeMoriLink,
                                    onStartDownloadToVault = viewModel::startMoriDownloadToVault,
                                    onTriggerOtaScraperUpdate = viewModel::triggerMoriOtaScraperUpdate,
                                    onOpenDownloadedItem = viewModel::playOrViewItem,
                                    onToggleEncryptItemWithPin = { item ->
                                        viewModel.requestToggleEncryptionWithPin(item, openAfterDecrypt = false)
                                    },
                                    onShareItemWithBiometric = { item ->
                                        viewModel.requestExportOrShareWithBiometric(item, isShareIntent = true)
                                    },
                                    onDeleteItem = viewModel::deleteVaultItem,
                                    onToggleBatchMode = viewModel::toggleMoriBatchMode,
                                    onSelectBatchFormatPreference = viewModel::selectMoriBatchFormatPreference,
                                    onSelectServer = viewModel::selectMoriServer,
                                    onToggleWifiOnlyGuard = viewModel::toggleMoriWifiOnlyGuard,
                                    onToggleCellularWarning = viewModel::toggleMoriCellularWarning,
                                    onSelectTargetDestination = viewModel::selectMoriTargetDestination,
                                    onSelectStressScenario = viewModel::selectMoriStressScenario,
                                    onShowSubFileModalChange = viewModel::setShowMoriSubFileModal,
                                    onToggleSubFileSelection = viewModel::toggleMoriSubFileSelection,
                                    onSelectAllSubFiles = viewModel::selectAllMoriSubFiles,
                                    onApplySubFileRangeSelection = viewModel::applyMoriSubFileRangeSelection,
                                    onTogglePauseQueueItem = viewModel::togglePauseBatchQueueItem,
                                    onRetryQueueItem = viewModel::retryBatchQueueItem,
                                    onCancelQueueItem = viewModel::cancelBatchQueueItem,
                                    onCancelActiveDownloadAndDeleteTemp = viewModel::cancelMoriDownloadAndCleanup,
                                    onRejectAndAutoDeleteDownloadedMedia = viewModel::rejectAndAutoDeleteMoriItem,
                                    onConfirmKeepDownloadedMedia = viewModel::confirmKeepMoriItem,
                                    onStartDirectFormatDownload = viewModel::startMoriDirectFormatDownload,
                                    onStartSingleSubFileDownload = viewModel::startMoriSingleSubFileDownload,
                                    onBackToToolsHub = { viewModel.selectTab(VaultTab.STEGO_TOOLS) }
                                )
                            }

                            VaultTab.ZIP_SCANNER -> {
                                ZipScannerToolScreen(
                                    activeVaultId = activeVaultId,
                                    scannerState = zipScannerState,
                                    onPickMultipleZipsOrFilesClick = {
                                        documentPickerLauncher.launch(arrayOf("*/*"))
                                    },
                                    onRunSimulatedMultiZipTestClick = viewModel::runSimulatedMultiZipScannerTest,
                                    onCommitCleanOnlyToVault = viewModel::commitStagedCleanItemsOnly,
                                    onCommitAllIncludingInfectedToVault = viewModel::commitStagedAllIncludingInfected,
                                    onOpenVirusWarningModal = viewModel::openVirusWarningModal,
                                    onDismissVirusWarningModal = viewModel::dismissVirusWarningModal,
                                    onClearScanReport = viewModel::clearZipScanReport,
                                    onToggleSelectEntryForExtraction = viewModel::toggleSelectZipEntryForExtraction,
                                    onSelectAllCleanEntriesForExtraction = viewModel::selectAllCleanZipEntriesForExtraction,
                                    onSelectAllEntriesForExtraction = viewModel::selectAllZipEntriesForExtraction,
                                    onCommitSelectedEntriesToVault = viewModel::commitSelectedZipEntriesToVault,
                                    onQuarantineInfectedEntriesToVaultLog = viewModel::quarantineInfectedZipEntriesToVaultLog,
                                    onDeleteQuarantineHistoryEntry = viewModel::deleteQuarantineHistoryEntry,
                                    onClearVaultQuarantineAndLogs = viewModel::clearVaultQuarantineAndLogs,
                                    onDecideSubClipAction = viewModel::decideSubClipAction,
                                    onRunActiveVaultDuplicateRecheck = viewModel::runActiveVaultDuplicateRecheck,
                                    onDeleteDuplicateVaultItemById = viewModel::deleteDuplicateVaultItemById,
                                    onDismissActiveVaultRecheckDialog = viewModel::dismissActiveVaultRecheckDialog
                                )
                            }

                            VaultTab.PHOTO_GALLERY -> {
                                VaultPhotoGalleryScreen(
                                    activeVaultId = activeVaultId,
                                    allItems = allItems,
                                    onOpenMediaItem = viewModel::playOrViewItem,
                                    onRequestPinToggleEncrypt = { item ->
                                        viewModel.requestToggleEncryptionWithPin(item, openAfterDecrypt = false)
                                    },
                                    onRequestExportOrShareWithBiometric = viewModel::requestExportOrShareWithBiometric,
                                    onDeleteItem = viewModel::deleteVaultItem
                                )
                            }

                            VaultTab.MEDIA_PLAYER -> {
                                VaultMediaPlayerScreen(
                                    activeVaultId = com.example.data.SecurityConfig.displayVaultNumber(activeVaultId),
                                    mediaState = mediaState,
                                    allItems = allItems,
                                    activeMediaFolder = activeMediaFolder,
                                    isFullscreenOverlayOpen = viewingMediaItem != null,
                                    onSelectMediaFolder = viewModel::selectMediaFolder,
                                    onMoveMediaItemToPlaylist = viewModel::moveMediaItemToPlaylist,
                                    onRenamePlaylistFolder = viewModel::renameFolderOrPlaylist,
                                    onSetHold2xSpeed = viewModel::setHold2xSpeed,
                                    onTogglePlayPause = viewModel::togglePlayPause,
                                    onSeekToSeconds = viewModel::seekToSeconds,
                                    onSeekToPositionMs = viewModel::seekToPositionMs,
                                    onUpdateActualDurationMs = viewModel::updateActiveTrackActualDurationMs,
                                    onSyncHardwarePositionMs = viewModel::syncHardwareVideoPositionMs,
                                    onSkipTrack = viewModel::skipTrack,
                                    onSelectOrOpenMedia = viewModel::selectAndPlayMediaTrack,
                                    onOpenFullscreenOverlay = viewModel::openFullscreenViewer,
                                    onRequestPinToggleEncrypt = { item ->
                                        viewModel.requestToggleEncryptionWithPin(item, openAfterDecrypt = false)
                                    },
                                    onBatchEncryptAllWithPin = viewModel::requestBatchEncryptionWithPin,
                                    onRequestExportOrShareWithBiometric = viewModel::requestExportOrShareWithBiometric,
                                    onCycleSpeed = viewModel::cyclePlaybackSpeed,
                                    onToggleRepeat = viewModel::toggleRepeatMode,
                                    onImportMediaOrZipClick = {
                                        documentPickerLauncher.launch(arrayOf("*/*"))
                                    }
                                )
                            }

                            VaultTab.STEGO_TOOLS -> {
                                VaultStegoToolsScreen(
                                    activeVaultId = activeVaultId,
                                    allItems = allItems,
                                    stegoState = stegoState,
                                    onCreateStegoPngClick = viewModel::createStegoPngInActiveVault,
                                    onCreateStegoWavClick = viewModel::createStegoWavInActiveVault,
                                    onDecodeStegoItemClick = viewModel::decodeStegoPngFromVaultItem,
                                    onRestoreEmbeddedFileClick = viewModel::restoreEmbeddedFileFromStego,
                                    onClearDecodedResult = viewModel::clearDecodedStegoResult,
                                    onOpenImageViewerClick = viewModel::playOrViewItem,
                                    onStripExifForItem = viewModel::stripExifForItem,
                                    onStripAllExifInVault = viewModel::stripExifForAllVaultImages,
                                    onActivateCalculatorCamouflage = {
                                        viewModel.setCalculatorCamouflageActive(true)
                                    },
                                    onSaveStarQrToVault = viewModel::saveQrBitmapBytesToActiveVault,
                                    onSaveConvertedFileToVault = viewModel::saveConvertedFileToActiveVault,
                                    moriState = moriState,
                                    onMoriUrlInputChange = viewModel::updateMoriUrlInput,
                                    onMoriCustomFileNameChange = viewModel::updateMoriCustomFileName,
                                    onMoriSelectExtractionMode = viewModel::selectMoriExtractionMode,
                                    onMoriToggleEncryptImmediately = viewModel::toggleMoriEncryptImmediately,
                                    onMoriAnalyzeLinkClick = viewModel::analyzeMoriLink,
                                    onMoriStartDownloadToVault = viewModel::startMoriDownloadToVault,
                                    onMoriTriggerOtaScraperUpdate = viewModel::triggerMoriOtaScraperUpdate,
                                    onMoriToggleEncryptItemWithPin = { item ->
                                        viewModel.requestToggleEncryptionWithPin(item, openAfterDecrypt = false)
                                    },
                                    onMoriShareItemWithBiometric = { item ->
                                        viewModel.requestExportOrShareWithBiometric(item, isShareIntent = true)
                                    },
                                    onMoriDeleteItem = viewModel::deleteVaultItem,
                                    onMoriToggleBatchMode = viewModel::toggleMoriBatchMode,
                                    onMoriSelectBatchFormatPreference = viewModel::selectMoriBatchFormatPreference,
                                    onMoriSelectServer = viewModel::selectMoriServer,
                                    onMoriToggleWifiOnlyGuard = viewModel::toggleMoriWifiOnlyGuard,
                                    onMoriToggleCellularWarning = viewModel::toggleMoriCellularWarning,
                                    onMoriSelectTargetDestination = viewModel::selectMoriTargetDestination,
                                    onMoriSelectStressScenario = viewModel::selectMoriStressScenario,
                                    onMoriShowSubFileModalChange = viewModel::setShowMoriSubFileModal,
                                    onMoriToggleSubFileSelection = viewModel::toggleMoriSubFileSelection,
                                    onMoriSelectAllSubFiles = viewModel::selectAllMoriSubFiles,
                                    onMoriApplySubFileRangeSelection = viewModel::applyMoriSubFileRangeSelection,
                                    onMoriTogglePauseQueueItem = viewModel::togglePauseBatchQueueItem,
                                    onMoriRetryQueueItem = viewModel::retryBatchQueueItem,
                                    onMoriCancelQueueItem = viewModel::cancelBatchQueueItem,
                                    onMoriCancelActiveDownloadAndDeleteTemp = viewModel::cancelMoriDownloadAndCleanup,
                                    onMoriRejectAndAutoDeleteDownloadedMedia = viewModel::rejectAndAutoDeleteMoriItem,
                                    onMoriConfirmKeepDownloadedMedia = viewModel::confirmKeepMoriItem,
                                    onMoriStartDirectFormatDownload = viewModel::startMoriDirectFormatDownload,
                                    onMoriStartSingleSubFileDownload = viewModel::startMoriSingleSubFileDownload
                                )
                            }

                            VaultTab.INTRUDER_PANIC_RADAR -> {
                                IntruderAndPanicRadarScreen(
                                    state = intruderAndPanicState,
                                    onTogglePanicMotionEnabled = viewModel::togglePanicMotionEnabled,
                                    onSelectPanicPreset = viewModel::selectPanicMotionPreset,
                                    onTestExtremePanicSwitchNow = {
                                        viewModel.triggerExtremePanicSwitchToDecoyVault(
                                            reasonLabel = "UJI COBA DARURAT (RADAR)"
                                        )
                                    },
                                    onSimulateIntruderEntry = viewModel::simulateIntruderTrapEntry,
                                    onDeleteIntruderLog = viewModel::deleteIntruderLog,
                                    onClearAllIntruderLogs = viewModel::clearAllIntruderLogs
                                )
                            }

                            VaultTab.CLOUD_BACKUP -> {
                                com.example.ui.GoogleCloudBackupTabScreen(
                                    activeVaultId = activeVaultId,
                                    securityConfig = securityConfig,
                                    activeVaultItems = allItems,
                                    allVaultsItems = allVaultsItems,
                                    cloudBackupEntries = cloudBackupEntries,
                                    onBackupSelectedIds = { selectedIds ->
                                        val selectedItems = allVaultsItems.filter { it.id in selectedIds }
                                        val overrideVaultId = selectedItems.firstOrNull()?.vaultProfileId
                                        viewModel.backupSelectedItemsToGoogleCloud(selectedItems, overrideVaultId)
                                    },
                                    onBackupEntireVault = { targetVId ->
                                        val vaultItems = allVaultsItems.filter { it.vaultProfileId == targetVId }
                                        viewModel.backupSelectedItemsToGoogleCloud(vaultItems, targetVId)
                                    },
                                    onRestoreCloudEntryToActiveVault = { entry ->
                                        viewModel.restoreSingleCloudEntryToActiveVault(entry)
                                    },
                                    onDeleteCloudEntry = { entry ->
                                        viewModel.deleteCloudBackupEntry(entry.cloudId)
                                    },
                                    onRefreshCloudEntries = {
                                        viewModel.refreshCloudBackupEntries()
                                    },
                                    onGoogleAccountSignedIn = { email, displayName ->
                                        viewModel.linkGoogleAccount(email, displayName)
                                    }
                                )
                            }

                            VaultTab.SCRIPT_ENGINE -> {
                                EncryptionScriptEngineScreen(
                                    activeVaultId = activeVaultId,
                                    securityConfig = securityConfig,
                                    onSaveScriptConfig = viewModel::saveScriptConfiguration,
                                    onSaveVaultPins = viewModel::saveVaultPins,
                                    onSaveSettingsPins = viewModel::saveSettingsPins,
                                    onSaveFunZonePins = viewModel::saveFunZonePins,
                                    onSaveSanctuaryPins = viewModel::saveSanctuaryPins,
                                    onSaveAuralysPins = viewModel::saveAuralysPins,
                                    onOpenSanctuaryClick = viewModel::openIsolatedSanctuaryDirectly,
                                    onOpenAuralysClick = viewModel::openIsolatedAuralysDirectly,
                                    onAddNewVault = { p1, p2, p3, customName -> viewModel.addNewVault(p1, p2, p3, customName) },
                                    onRenameVault = { vId, newName -> viewModel.renameVault(vId, newName) },
                                    onSaveRecoveryCredentials = viewModel::saveRecoveryCredentials,
                                    onToggleBiometricGate = viewModel::toggleBiometricGate,
                                    onLockVaultNow = viewModel::lockVaultNow,
                                    onOpenZipScannerClick = { viewModel.selectTab(VaultTab.ZIP_SCANNER) },
                                    onLinkGoogleAccount = { email -> viewModel.linkGoogleAccount(email, "Pengguna Arcanum") },
                                    onUnlinkGoogleAccount = { viewModel.unlinkGoogleAccount() },
                                    onBackupToCloud = { viewModel.backupActiveVaultToGoogleCloud(context) },
                                    onRestoreFromCloud = { file -> viewModel.restoreVaultFromEncryptedPackage(context, file) },
                                    onResetDecoyVault = viewModel::resetDecoyVaultFromSettings,
                                    onToggleAntiScreenshot = viewModel::toggleAntiScreenshotMode,
                                    onSelectVisualTheme = viewModel::setVisualTheme,
                                    onSelectGraphicsQualityMode = viewModel::setGraphicsQualityMode,
                                    onToggleTransparentButtonBox = viewModel::setTransparentButtonBox,
                                    onOpenThemeStudioClick = { viewModel.selectTab(VaultTab.THEME_STUDIO) },
                                    onOpenCamouflageTabClick = { viewModel.selectTab(VaultTab.APP_CAMOUFLAGE) },
                                    onOpenProStudioTabClick = { viewModel.selectTab(VaultTab.PRO_STUDIO) }
                                )
                            }

                            VaultTab.PRO_STUDIO -> {
                                com.example.ui.ProStudioDedicatedSettingsScreen(
                                    onPersistSafeMode = viewModel::setGraphicsQualityMode,
                                    onBackToSettingsClick = { viewModel.selectTab(VaultTab.SCRIPT_ENGINE) }
                                )
                            }

                            VaultTab.APP_CAMOUFLAGE -> {
                                com.example.ui.AppCamouflageAndRedirectScreen(
                                    securityConfig = securityConfig,
                                    onUpdateCamouflageConfig = viewModel::updateAppCamouflageConfig,
                                    onApplyAndRestartToCamouflage = viewModel::applyCamouflageAndRestartSession
                                )
                            }

                            VaultTab.THEME_STUDIO -> {
                                com.example.ui.ThemeAndMascotStudioScreen(
                                    securityConfig = securityConfig,
                                    isMascotOverlayVisible = isMascotOverlayVisible,
                                    onSetMascotOverlayVisible = { visible ->
                                        isMascotOverlayVisible = visible
                                    },
                                    onSelectVisualTheme = viewModel::setVisualTheme,
                                    onSelectColorPalette = viewModel::setColorPalette,
                                    onUpdateMascotStudio = viewModel::setMascotStudioConfig,
                                    onUpdateDynamicIslandConfig = viewModel::setDynamicIslandConfig
                                )
                            }

                            VaultTab.ISOLATED_FUN_ZONE -> {
                                com.example.ui.IsolatedFunZoneScreen(
                                    onLockAndExit = viewModel::lockVaultNow,
                                    onPostBanner = viewModel::postBanner
                                )
                            }

                            VaultTab.BRAIN_GAMES_HUB -> {
                                com.example.ui.IsolatedFunZoneScreen(
                                    onLockAndExit = { viewModel.selectTab(VaultTab.DASHBOARD) },
                                    onPostBanner = viewModel::postBanner
                                )
                            }

                            VaultTab.ISOLATED_SANCTUARY -> {
                                com.example.ui.IsolatedSanctuaryScreen(
                                    onLockAndExit = viewModel::lockVaultNow,
                                    onPostBanner = viewModel::postBanner
                                )
                            }

                            VaultTab.ISOLATED_AURALYS -> {
                                com.example.ui.AuralysMusicChamberScreen(
                                    vaultItems = allItems,
                                    onLockAndExit = viewModel::lockVaultNow,
                                    onPostBanner = viewModel::postBanner
                                )
                            }
                        }
                        }
                            }
                        }

                        // Interactive Draggable 3D Duolingo Mascot Overlay (Reacts to Downloads, Imports, Deletes, Stego & Clicks)
                        val activeTabReadableName = when (activeTab) {
                            VaultTab.DASHBOARD, VaultTab.FILES -> "BRANKAS"
                            VaultTab.MEDIA_PLAYER -> "MEDIA_PLAYER"
                            VaultTab.PHOTO_GALLERY -> "PHOTO_GALLERY"
                            VaultTab.MORI_DOWNLOADER -> "MORI STREAM"
                            VaultTab.CLOUD_BACKUP -> "CLOUD GOOGLE"
                            VaultTab.ZIP_SCANNER -> "ZIP SCAN"
                            VaultTab.STEGO_TOOLS -> "STEGO & KRIPTO"
                            VaultTab.INTRUDER_PANIC_RADAR -> "BRANKAS SETTING • RADAR"
                            VaultTab.SCRIPT_ENGINE -> "BRANKAS SETTING • UMUM"
                            VaultTab.PRO_STUDIO -> "BRANKAS SETTING • PRO HUB"
                            VaultTab.APP_CAMOUFLAGE -> "KAMUFLASE APP"
                            VaultTab.THEME_STUDIO -> "BRANKAS SETTING • TEMA"
                            VaultTab.ISOLATED_FUN_ZONE, VaultTab.BRAIN_GAMES_HUB -> "FUN ZONE 3D"
                            VaultTab.ISOLATED_SANCTUARY -> "BLACKBOX SANCTUARY"
                            VaultTab.ISOLATED_AURALYS -> "AURALYS MUSIC"
                        }
                        InteractiveDraggableMascotOverlay(
                            activeTabName = activeTabReadableName,
                            isMascotVisible = isMascotOverlayVisible,
                            resummonNonce = mascotResummonNonce,
                            onSetMascotVisible = { visible ->
                                isMascotOverlayVisible = visible
                                if (!visible) {
                                    viewModel.postBanner("Maskot disembunyikan. Tekan 'Maskot Overlay' di Beranda Brankas untuk memunculkan lagi.")
                                }
                            },
                            isDownloading = moriState.isDownloading || moriState.isAnalyzing,
                            downloadProgressPercent = (moriState.progressRatio * 100f).toInt().coerceIn(0, 100),
                            downloadStatusText = moriState.statusLogLine,
                            isScanningZip = zipScannerState.isScanning,
                            scanStatusText = zipScannerState.progressMessage,
                            isProcessingStegoOrCrypto = activeTab == VaultTab.STEGO_TOOLS && stegoState.decodedPayload != null,
                            stegoStatusText = stegoState.decodeErrorMessage ?: "",
                            latestBannerMessage = statusBannerMessage,
                            totalVaultFilesCount = allItems.size,
                            encryptedFilesCount = allItems.count { it.isEncrypted }
                        )
                    }
                }
            }
        }
    }

    // Inspect NYX2 Container Bottom Sheet
    inspectingItem?.let { item ->
        VaultFileInspectorSheet(
            item = item,
            onDismiss = { viewModel.inspectItem(null) },
            onToggleEncryptWithPin = {
                viewModel.requestToggleEncryptionWithPin(item, openAfterDecrypt = false)
            },
            onOpenOrPlayMedia = {
                viewModel.inspectItem(null)
                viewModel.playOrViewItem(item)
            },
            onDelete = {
                viewModel.deleteVaultItem(item)
                viewModel.inspectItem(null)
            }
        )
    }

    // Create New Secret Note / File Dialog
    if (showCreateSecretDialog) {
        CreateVaultFileDialog(
            onDismiss = { showCreateSecretDialog = false },
            onCreateFile = { title, ext, category, payload, encryptNow ->
                viewModel.createVaultNoteOrMediaFile(
                    fileName = title,
                    extension = ext,
                    category = category,
                    contentText = payload,
                    encryptImmediately = encryptNow
                )
            }
        )
    }

    // Arcanum — Feedback & Report Center (Mandatory Google Login Gate + Anti-Spam + Silent Auto-Submit)
    if (showFeedbackReportDialog) {
        com.example.ui.ArcanumFeedbackReportDialog(
            activeVaultId = com.example.data.SecurityConfig.displayVaultNumber(activeVaultId),
            isGoogleLoggedIn = securityConfig.isGoogleLinked && securityConfig.googleAccountEmail.isNotBlank(),
            loggedInGoogleName = securityConfig.googleAccountDisplayName,
            loggedInGoogleEmail = securityConfig.googleAccountEmail,
            onGoogleLoginSuccess = { email, displayName ->
                viewModel.linkGoogleAccount(email, displayName)
            },
            onGoogleLogoutClick = {
                viewModel.unlinkGoogleAccount()
            },
            onDismiss = { showFeedbackReportDialog = false },
            onSaveReportToVault = { fileName, markdownContent ->
                viewModel.createVaultNoteOrMediaFile(
                    fileName = fileName,
                    extension = "md",
                    category = com.example.data.VaultCategory.DOCUMENT,
                    contentText = markdownContent,
                    encryptImmediately = false
                )
            },
            onPostStatusBanner = viewModel::postBanner
        )
    }

    // Full-Screen Rich Media, Video & Document Viewer Overlay (With Google Photos Horizontal Swipe Pager)
    viewingMediaItem?.let { item ->
        val vaultImageItems = remember(allItems, activeVaultId) {
            val imgExts = setOf("jpg", "jpeg", "png", "webp", "heic", "gif", "bmp", "svg")
            allItems.filter { candidate ->
                candidate.vaultProfileId == activeVaultId &&
                    (candidate.category == com.example.data.VaultCategory.IMAGE.name ||
                        candidate.extension.lowercase() in imgExts)
            }
        }
        VaultRichMediaViewerOverlay(
            item = item,
            mediaState = mediaState,
            onClose = { viewModel.openViewingMediaModal(null) },
            onRequestReEncryptWithPin = {
                viewModel.openViewingMediaModal(null)
                viewModel.requestToggleEncryptionWithPin(item, openAfterDecrypt = false)
            },
            onRequestExportOrShareWithBiometric = { isShareIntent ->
                viewModel.requestExportOrShareWithBiometric(item, isShareIntent)
            },
            onToggleVideoPlayPause = viewModel::togglePlayPause,
            onSeekVideoSeconds = viewModel::seekToSeconds,
            onSeekVideoPositionMs = viewModel::seekToPositionMs,
            onUpdateActualDurationMs = viewModel::updateActiveTrackActualDurationMs,
            onSyncHardwarePositionMs = viewModel::syncHardwareVideoPositionMs,
            onSkipTrack = viewModel::skipTrack,
            onSetHold2xSpeed = viewModel::setHold2xSpeed,
            onCycleSpeed = viewModel::cyclePlaybackSpeed,
            photoGalleryItems = vaultImageItems,
            onSelectPhotoFromPager = { swipedPhoto ->
                viewModel.openViewingMediaModal(swipedPhoto)
            },
            onDeleteCurrentItem = { photoToDelete ->
                viewModel.deleteVaultItem(photoToDelete)
            }
        )
    }

    // PIN Verification Modal for Encrypting / Decrypting Files
    cryptoPinModalState?.let { modalState ->
        CryptoPinVerificationDialog(
            modalState = modalState,
            onDigitClick = viewModel::onCryptoModalPinDigit,
            onCharClick = viewModel::onCryptoModalPinChar,
            onBackspaceClick = viewModel::onCryptoModalPinBackspace,
            onDismiss = viewModel::dismissCryptoPinModal,
            onForgotPinClick = viewModel::openForgotPinModal
        )
    }

    // Secondary Biometric Re-Authentication Modal for Exporting or Sharing Files from Media Player
    biometricExportModalState?.let { exportState ->
        SecondaryBiometricReAuthDialog(
            modalState = exportState,
            onAuthenticated = {
                viewModel.onBiometricExportReAuthenticated()
            },
            onDismiss = viewModel::dismissBiometricExportModal
        )
    }

    // Password-Protected .nyx Export Dialog (Enforces custom password on exported .nyx packages)
    exportPasswordPromptState?.let { state: ExportPasswordPromptState ->
        ExportPasswordDialog(
            item = state.item,
            onDismiss = viewModel::dismissExportPasswordModal,
            onConfirmPassword = { password ->
                viewModel.confirmExportWithPassword(
                    password = password,
                    onShareFileReady = { nyxFile, mimeType ->
                        try {
                            val authority = "${context.packageName}.fileprovider"
                            val shareUri = androidx.core.content.FileProvider.getUriForFile(context, authority, nyxFile)
                            val sendIntent = Intent(Intent.ACTION_SEND).apply {
                                type = mimeType
                                putExtra(Intent.EXTRA_STREAM, shareUri)
                                putExtra(Intent.EXTRA_SUBJECT, nyxFile.name)
                                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                            }
                            val chooser = Intent.createChooser(sendIntent, "Bagikan Berkas .nyx Terproteksi")
                            context.startActivity(chooser)
                        } catch (_: Exception) {
                        }
                    },
                    onLaunchSystemSaveAs = { suggestedFileName, _ ->
                        saveExportDocumentLauncher.launch(suggestedFileName)
                    }
                )
            }
        )
    }

    // Password-Protected .nyx Import Dialog (Prompts for creator password when importing .nyx)
    importPasswordPromptState?.let { prompt: ImportPasswordPromptState ->
        ImportNyxPasswordDialog(
            fileName = prompt.fileName,
            errorMessage = prompt.errorMessage,
            onDismiss = viewModel::dismissImportPasswordModal,
            onConfirmPassword = viewModel::confirmImportPassword
        )
    }

    // Isolated External File Quick-Preview Overlay ("Buka Dengan Arcanum" -> Back routes directly to PIN Layer 1)
    externalPreviewRequest?.let { previewReq ->
        com.example.ui.ArcanumExternalFileQuickPreviewOverlay(
            request = previewReq,
            onSaveAndOverwriteToVault = {
                viewModel.saveExternalPreviewToVaultAndLockToPinGate()
            },
            onCloseAndRequirePinLock = {
                viewModel.closeExternalPreviewAndLockToPinGate()
            }
        )
    }
}

private data class InteractiveNavItemSpec(
    val label: String,
    val icon: ImageVector,
    val selected: Boolean,
    val activeColor: Color,
    val activeLipColor: Color,
    val onClick: () -> Unit,
    val tag: String
)

@Composable
private fun IsolatedSettingsBottomBar(
    activeTab: VaultTab,
    onSelectTab: (VaultTab) -> Unit,
    onLockAndExit: () -> Unit
) {
    val items = buildList {
        add(
            InteractiveNavItemSpec(
                label = "Umum",
                icon = Icons.Default.Settings,
                selected = activeTab == VaultTab.SCRIPT_ENGINE,
                activeColor = com.example.ui.theme.DuoBeetlePurple,
                activeLipColor = com.example.ui.theme.DuoBeetlePurpleLip,
                onClick = { onSelectTab(VaultTab.SCRIPT_ENGINE) },
                tag = "nav_tab_script_engine"
            )
        )
        if (com.example.ui.DRAFT_APP_CAMOUFLAGE_FEATURE_ENABLED) {
            add(
                InteractiveNavItemSpec(
                    label = "Kamuflase",
                    icon = Icons.Default.Security,
                    selected = activeTab == VaultTab.APP_CAMOUFLAGE,
                    activeColor = com.example.ui.theme.DuoMacawBlue,
                    activeLipColor = com.example.ui.theme.DuoMacawBlueLip,
                    onClick = { onSelectTab(VaultTab.APP_CAMOUFLAGE) },
                    tag = "nav_tab_app_camouflage"
                )
            )
        }
        add(
            InteractiveNavItemSpec(
                label = "Tema",
                icon = Icons.Default.Collections,
                selected = activeTab == VaultTab.THEME_STUDIO,
                activeColor = com.example.ui.theme.DuoFeatherGreen,
                activeLipColor = com.example.ui.theme.DuoFeatherGreenLip,
                onClick = { onSelectTab(VaultTab.THEME_STUDIO) },
                tag = "nav_tab_theme_studio"
            )
        )
        add(
            InteractiveNavItemSpec(
                label = "Radar",
                icon = Icons.Default.Radar,
                selected = activeTab == VaultTab.INTRUDER_PANIC_RADAR,
                activeColor = com.example.ui.theme.DuoFoxOrange,
                activeLipColor = com.example.ui.theme.DuoFoxOrangeLip,
                onClick = { onSelectTab(VaultTab.INTRUDER_PANIC_RADAR) },
                tag = "nav_tab_intruder_panic_radar"
            )
        )
        add(
            InteractiveNavItemSpec(
                label = "Pro Hub",
                icon = Icons.Default.Star,
                selected = activeTab == VaultTab.PRO_STUDIO,
                activeColor = com.example.ui.theme.DuoBeeYellow,
                activeLipColor = com.example.ui.theme.DuoBeeYellowLip,
                onClick = { onSelectTab(VaultTab.PRO_STUDIO) },
                tag = "nav_tab_pro_studio"
            )
        )
        add(
            InteractiveNavItemSpec(
                label = "ZIP Scan",
                icon = Icons.Default.FolderZip,
                selected = activeTab == VaultTab.ZIP_SCANNER,
                activeColor = com.example.ui.theme.DuoMacawBlue,
                activeLipColor = com.example.ui.theme.DuoMacawBlueLip,
                onClick = { onSelectTab(VaultTab.ZIP_SCANNER) },
                tag = "nav_tab_zip_scanner"
            )
        )
        add(
            InteractiveNavItemSpec(
                label = "Keluar",
                icon = Icons.Default.Close,
                selected = false,
                activeColor = com.example.ui.theme.DuoCardinalRed,
                activeLipColor = com.example.ui.theme.DuoCardinalRedLip,
                onClick = onLockAndExit,
                tag = "nav_tab_exit_isolated_settings"
            )
        )
    }

    GsapInteractiveGlidingBottomDock(
        items = items,
        allowHoldSlideOnLastItem = false
    )
}

@Composable
private fun TrantorDockedBottomBar(
    activeTab: VaultTab,
    onSelectTab: (VaultTab) -> Unit
) {
    val items = listOf(
        InteractiveNavItemSpec(
            label = "Brankas",
            icon = Icons.Default.Shield,
            selected = activeTab == VaultTab.DASHBOARD || activeTab == VaultTab.FILES,
            activeColor = com.example.ui.theme.DuoFeatherGreen,
            activeLipColor = com.example.ui.theme.DuoFeatherGreenLip,
            onClick = { onSelectTab(VaultTab.DASHBOARD) },
            tag = "nav_tab_dashboard"
        ),
        InteractiveNavItemSpec(
            label = "Media",
            icon = Icons.Default.PlayArrow,
            selected = activeTab == VaultTab.MEDIA_PLAYER,
            activeColor = com.example.ui.theme.DuoMacawBlue,
            activeLipColor = com.example.ui.theme.DuoMacawBlueLip,
            onClick = { onSelectTab(VaultTab.MEDIA_PLAYER) },
            tag = "nav_tab_media_player"
        ),
        InteractiveNavItemSpec(
            label = "Galeri",
            icon = Icons.Default.Collections,
            selected = activeTab == VaultTab.PHOTO_GALLERY,
            activeColor = com.example.ui.theme.DuoFoxOrange,
            activeLipColor = com.example.ui.theme.DuoFoxOrangeLip,
            onClick = { onSelectTab(VaultTab.PHOTO_GALLERY) },
            tag = "nav_tab_photo_gallery"
        ),
        InteractiveNavItemSpec(
            label = "Cloud",
            icon = Icons.Default.CloudUpload,
            selected = activeTab == VaultTab.CLOUD_BACKUP,
            activeColor = Color(0xFFEC4899),
            activeLipColor = Color(0xFFBE185D),
            onClick = { onSelectTab(VaultTab.CLOUD_BACKUP) },
            tag = "nav_tab_cloud_backup"
        ),
        InteractiveNavItemSpec(
            label = "ZIP",
            icon = Icons.Default.FolderZip,
            selected = activeTab == VaultTab.ZIP_SCANNER,
            activeColor = com.example.ui.theme.DuoBeeYellow,
            activeLipColor = com.example.ui.theme.DuoBeeYellowLip,
            onClick = { onSelectTab(VaultTab.ZIP_SCANNER) },
            tag = "nav_tab_vault_zip_scanner"
        ),
        InteractiveNavItemSpec(
            label = "Tools",
            icon = Icons.Default.Build,
            selected = activeTab == VaultTab.STEGO_TOOLS || activeTab == VaultTab.MORI_DOWNLOADER,
            activeColor = com.example.ui.theme.DuoBeetlePurple,
            activeLipColor = com.example.ui.theme.DuoBeetlePurpleLip,
            onClick = { onSelectTab(VaultTab.STEGO_TOOLS) },
            tag = "nav_tab_stego_tools"
        )
    )

    GsapInteractiveGlidingBottomDock(
        items = items,
        allowHoldSlideOnLastItem = false
    )
}

@Composable
private fun GsapInteractiveGlidingBottomDock(
    items: List<InteractiveNavItemSpec>,
    allowHoldSlideOnLastItem: Boolean
) {
    // Ensure navigation bar offset is permanently locked at 0f (never draggable/movable)
    LaunchedEffect(Unit) {
        com.example.ui.theme.AppThemeController.navBarOffsetXDp = 0f
        com.example.ui.theme.AppThemeController.navBarOffsetYDp = 0f
    }

    val selectedIndex = items.indexOfFirst { it.selected }.coerceAtLeast(0)
    val activeSpec = items.getOrElse(selectedIndex) { items.first() }

    androidx.compose.foundation.layout.Column(
        modifier = Modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .padding(horizontal = 12.dp, vertical = 6.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
                .duolingoCard(
                    backgroundColor = com.example.ui.theme.CosmicObsidian,
                    borderColor = activeSpec.activeColor,
                    lipColor = com.example.ui.theme.DuoHareLip,
                    cornerRadius = 22.dp,
                    borderWidth = 2.dp,
                    lipHeight = 4.dp
                )
                .padding(bottom = 4.dp, start = 8.dp, end = 8.dp),
            contentAlignment = Alignment.Center
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp, Alignment.CenterHorizontally),
                verticalAlignment = Alignment.CenterVertically
            ) {
                items.forEach { item ->
                    Box(
                        modifier = Modifier.weight(1f),
                        contentAlignment = Alignment.Center
                    ) {
                        BottomNavPillItem(
                            label = item.label,
                            icon = item.icon,
                            selected = item.selected,
                            activeColor = item.activeColor,
                            activeLipColor = item.activeLipColor,
                            onClick = item.onClick,
                            tag = item.tag
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun BottomNavPillItem(
    label: String,
    icon: ImageVector,
    selected: Boolean,
    activeColor: Color = com.example.ui.theme.DuoFeatherGreen,
    activeLipColor: Color = com.example.ui.theme.DuoFeatherGreenLip,
    onClick: () -> Unit,
    tag: String
) {
    val qualityMode = com.example.ui.theme.AppThemeController.graphicsQualityMode
    val boxShape = RoundedCornerShape(14.dp)

    val iconTint = when {
        selected && activeColor == com.example.ui.theme.DuoBeeYellow -> Color(0xFF1E293B)
        selected -> Color.White
        else -> activeColor
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(44.dp)
            .then(
                if (selected) {
                    Modifier
                        .duolingoButton(
                            faceColor = activeColor,
                            lipColor = activeLipColor,
                            cornerRadius = 14.dp,
                            lipHeight = 3.dp
                        )
                        .padding(bottom = 3.dp)
                } else {
                    Modifier
                        .clip(boxShape)
                        .background(activeColor.copy(alpha = 0.20f))
                        .border(1.5.dp, activeColor.copy(alpha = 0.65f), boxShape)
                }
            )
            .gsapMagneticMicroTouch(
                maxMagneticShiftDp = 0.dp,
                maxTiltDegrees = 0f,
                pressScale = 0.94f,
                burstColor = activeColor,
                onClick = onClick
            )
            .testTag(tag),
        contentAlignment = Alignment.Center
    ) {
        if (qualityMode == com.example.ui.theme.AppGraphicsQualityMode.SMOOTH) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = iconTint,
                modifier = Modifier.size(if (selected) 21.dp else 19.dp)
            )
        } else {
            com.example.ui.GsapMicroIconBounce(
                icon = icon,
                contentDescription = label,
                tint = iconTint,
                activeTrigger = selected,
                iconSize = if (selected) 21.dp else 19.dp
            )
        }
    }
}

