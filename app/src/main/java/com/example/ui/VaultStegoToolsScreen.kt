package com.example.ui

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.graphics.BitmapFactory
import androidx.compose.ui.platform.LocalContext
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AttachFile
import androidx.compose.material.icons.filled.AudioFile
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CleaningServices
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Compare
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.GpsOff
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Layers
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.LockOpen
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.CloudDownload
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.QrCode2
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.DecodedStegoPayload
import com.example.data.EncryptionScriptEngine
import com.example.data.MoriBatchFormatPreference
import com.example.data.MoriExtractionMode
import com.example.data.MoriStressScenario
import com.example.data.MoriSubFileItem
import com.example.data.MoriTargetDestination
import com.example.data.PngSteganographyEngine
import com.example.data.StegoCoverTheme
import com.example.data.VaultCategory
import com.example.data.VaultItemEntity
import com.example.data.WavAudioSteganographyEngine
import com.example.data.WavCamouflagePreset
import com.example.ui.theme.CosmicBorderHairline
import com.example.ui.theme.DuoBeeYellow
import com.example.ui.theme.DuoBeeYellowLip
import com.example.ui.theme.DuoBeeYellowSoft
import com.example.ui.theme.DuoBeetlePurple
import com.example.ui.theme.DuoBeetlePurpleLip
import com.example.ui.theme.DuoBeetlePurpleSoft
import com.example.ui.theme.DuoCardinalRed
import com.example.ui.theme.DuoCardinalRedLip
import com.example.ui.theme.DuoCardinalRedSoft
import com.example.ui.theme.DuoFeatherGreen
import com.example.ui.theme.DuoFeatherGreenLip
import com.example.ui.theme.DuoFeatherGreenSoft
import com.example.ui.theme.DuoFoxOrange
import com.example.ui.theme.DuoFoxOrangeLip
import com.example.ui.theme.DuoFoxOrangeSoft
import com.example.ui.theme.DuoHareLip
import com.example.ui.theme.DuoMacawBlue
import com.example.ui.theme.DuoMacawBlueLip
import com.example.ui.theme.DuoMacawBlueSoft
import com.example.ui.theme.DuoPureWhite
import com.example.ui.theme.DuoSwanBorder
import com.example.ui.theme.JetBrainsMonoFontFamily
import com.example.ui.theme.PastelMint
import com.example.ui.theme.VaultCoral
import com.example.ui.theme.VaultCream
import com.example.ui.theme.VaultLime
import com.example.ui.theme.VaultObsidian
import com.example.ui.theme.VaultPureWhite
import com.example.ui.theme.VaultTextMuted
import com.example.ui.theme.duolingoButton
import com.example.ui.theme.duolingoCard
import java.io.File
import java.util.Locale

data class StegoToolsUiState(
    val selectedDecodingItem: VaultItemEntity? = null,
    val decodedPayload: DecodedStegoPayload? = null,
    val decodeErrorMessage: String? = null
)

private val DuoObsidianChipColor = Color(0xFF0F172A)
private val DuoObsidianLipColor = Color(0xFF020617)

/**
 * Komponen Chip Taktil 3D ala Duolingo untuk menggantikan FilterChip datar.
 */
@Composable
private fun DuoTactileChip(
    selected: Boolean,
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
    selectedFaceColor: Color = DuoFeatherGreen,
    selectedLipColor: Color = DuoFeatherGreenLip,
    selectedTextColor: Color = Color.White,
    unselectedFaceColor: Color = DuoFeatherGreenSoft,
    unselectedLipColor: Color = DuoFeatherGreenLip,
    unselectedTextColor: Color = VaultObsidian
) {
    val face = if (selected) selectedFaceColor else unselectedFaceColor
    val lip = if (selected) selectedLipColor else unselectedLipColor
    val txt = if (selected) selectedTextColor else unselectedTextColor

    Box(
        modifier = modifier
            .height(42.dp)
            .then(
                if (selected) {
                    Modifier.duolingoButton(
                        faceColor = face,
                        lipColor = lip,
                        cornerRadius = 15.dp,
                        lipHeight = 4.dp
                    )
                } else {
                    Modifier.duolingoCard(
                        backgroundColor = face,
                        borderColor = selectedFaceColor.copy(alpha = 0.7f),
                        lipColor = lip,
                        cornerRadius = 15.dp,
                        borderWidth = 2.dp,
                        lipHeight = 4.dp
                    )
                }
            )
            .instantTouchPress { onClick() }
            .padding(bottom = 4.dp)
            .padding(horizontal = 12.dp, vertical = 4.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            if (icon != null) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = if (selected) txt else selectedLipColor,
                    modifier = Modifier.size(15.dp)
                )
            }
            Text(
                text = label,
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.ExtraBold,
                color = txt,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

/**
 * Komponen Tombol Taktil 3D ala Duolingo untuk menggantikan Button & OutlinedButton datar.
 */
@Composable
private fun DuoTactileActionButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
    faceColor: Color = DuoFeatherGreen,
    lipColor: Color = DuoFeatherGreenLip,
    textColor: Color = Color.White,
    height: Dp = 46.dp,
    enabled: Boolean = true
) {
    val effectiveFace = if (enabled) faceColor else DuoSwanBorder
    val effectiveLip = if (enabled) lipColor else DuoHareLip
    val effectiveText = if (enabled) textColor else VaultTextMuted

    Box(
        modifier = modifier
            .height(height)
            .duolingoButton(
                faceColor = effectiveFace,
                lipColor = effectiveLip,
                cornerRadius = 16.dp,
                lipHeight = 4.dp
            )
            .instantTouchPress(enabled = enabled) { onClick() }
            .padding(horizontal = 12.dp, vertical = 4.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            if (icon != null) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = effectiveText,
                    modifier = Modifier.size(16.dp)
                )
            }
            Text(
                text = text,
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.ExtraBold,
                color = effectiveText,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
private fun duoTextFieldColors() = OutlinedTextFieldDefaults.colors(
    focusedBorderColor = DuoMacawBlue,
    unfocusedBorderColor = DuoHareLip,
    focusedContainerColor = DuoPureWhite,
    unfocusedContainerColor = VaultCream.copy(alpha = 0.55f),
    focusedLabelColor = DuoMacawBlueLip,
    unfocusedLabelColor = VaultTextMuted
)

@Composable
fun VaultStegoToolsScreen(
    activeVaultId: Int,
    allItems: List<VaultItemEntity>,
    stegoState: StegoToolsUiState,
    onCreateStegoPngClick: (
        pngFileName: String,
        coverTheme: StegoCoverTheme,
        secretTitle: String,
        secretMessage: String,
        stegoPasscode: String,
        attachedItem: VaultItemEntity?,
        carrierPhotoItem: VaultItemEntity?,
        isQrKeyCardMode: Boolean
    ) -> Unit,
    onCreateStegoWavClick: (
        wavFileName: String,
        wavPreset: WavCamouflagePreset,
        secretTitle: String,
        secretMessage: String,
        stegoPasscode: String,
        attachedItem: VaultItemEntity?,
        carrierAudioItem: VaultItemEntity?
    ) -> Unit = { _, _, _, _, _, _, _ -> },
    onDecodeStegoItemClick: (VaultItemEntity, String) -> Unit,
    onRestoreEmbeddedFileClick: (DecodedStegoPayload) -> Unit,
    onStripExifForItem: (VaultItemEntity) -> Unit,
    onStripAllExifInVault: () -> Unit,
    onActivateCalculatorCamouflage: () -> Unit,
    onOpenImageViewerClick: (VaultItemEntity) -> Unit,
    onClearDecodedResult: () -> Unit,
    onSaveStarQrToVault: (String, ByteArray, String) -> Unit = { _, _, _ -> },
    onSaveConvertedFileToVault: (
        stagedFile: File,
        targetBaseName: String,
        targetExtension: String,
        category: VaultCategory,
        groupLabel: String,
        fallbackPreviewText: String,
        onSavedResult: (VaultItemEntity) -> Unit
    ) -> Unit = { _, _, _, _, _, _, _ -> },
    initialToolSubTab: Int = -1,
    onConsumeInitialToolSubTab: () -> Unit = {},
    moriState: MoriDownloaderUiState = MoriDownloaderUiState(),
    onMoriUrlInputChange: (String) -> Unit = {},
    onMoriCustomFileNameChange: (String) -> Unit = {},
    onMoriSelectExtractionMode: (MoriExtractionMode) -> Unit = {},
    onMoriToggleEncryptImmediately: (Boolean) -> Unit = {},
    onMoriAnalyzeLinkClick: () -> Unit = {},
    onMoriStartDownloadToVault: () -> Unit = {},
    onMoriTriggerOtaScraperUpdate: () -> Unit = {},
    onMoriToggleEncryptItemWithPin: (VaultItemEntity) -> Unit = {},
    onMoriShareItemWithBiometric: (VaultItemEntity) -> Unit = {},
    onMoriDeleteItem: (VaultItemEntity) -> Unit = {},
    onMoriToggleBatchMode: (Boolean) -> Unit = {},
    onMoriSelectBatchFormatPreference: (MoriBatchFormatPreference) -> Unit = {},
    onMoriSelectServer: (String) -> Unit = {},
    onMoriToggleWifiOnlyGuard: (Boolean) -> Unit = {},
    onMoriToggleCellularWarning: (Boolean) -> Unit = {},
    onMoriSelectTargetDestination: (MoriTargetDestination) -> Unit = {},
    onMoriSelectStressScenario: (MoriStressScenario) -> Unit = {},
    onMoriShowSubFileModalChange: (Boolean) -> Unit = {},
    onMoriToggleSubFileSelection: (String) -> Unit = {},
    onMoriSelectAllSubFiles: (Boolean) -> Unit = {},
    onMoriApplySubFileRangeSelection: (String) -> Unit = {},
    onMoriTogglePauseQueueItem: (Int) -> Unit = {},
    onMoriRetryQueueItem: (Int) -> Unit = {},
    onMoriCancelQueueItem: (Int) -> Unit = {},
    onMoriCancelActiveDownloadAndDeleteTemp: () -> Unit = {},
    onMoriRejectAndAutoDeleteDownloadedMedia: (VaultItemEntity) -> Unit = {},
    onMoriConfirmKeepDownloadedMedia: (VaultItemEntity) -> Unit = {},
    onMoriStartDirectFormatDownload: (MoriExtractionMode, String) -> Unit = { _, _ -> },
    onMoriStartSingleSubFileDownload: (MoriSubFileItem) -> Unit = {}
) {
    val displayVaultNum = if (activeVaultId <= 0) 1 else activeVaultId

    // Sub-tabs inside Tools Studio:
    // -1 = Arcanum Tools Category Icon Hub (Default entry view with Theme-Adaptive Icon Grid + Featured Mori v4.6.0 Hero Card)
    // 30 = Mori Stream Downloader v4.6.0 (16 Platforms, Batch MP3/Video, Pure Audio Auto-MP3, Shorts Canonicalizer)
    // 10 = Office Docs Converter (PDF <-> DOCX, XLSX, PPTX, TXT)
    // 11 = Vector & Image Converter (PNG/JPG <-> SVG Vector, WEBP, ICO, PDF)
    // 12 = Data & Code Converter (CSV <-> XLSX/JSON, Markdown <-> PDF/HTML, Base64)
    // 13 = Archive & Audio Converter (Multi-File -> ZIP, WAV <-> MP3/M4A)
    // 20..27 = 8 Mori v4.6.0 Utility Tools (Web Sniffer, SFL.GL Bypass, TikTok OSINT, Anime trace.moe, DoH Checker, Image/Audio/PDF Pro)
    // 0 = Stego Foto PNG (Side-by-Side Visual Comparison, Pixel X-Ray, Loupe 8x & 2 Foto dalam 1 File)
    // 9 = Studio QR Bintang AstraOnyx (Buat QR Bintang, Scan Kamera/Foto & Auto-Konversi QR Biasa -> QR AstraOnyx)
    // 5 = Stego Audio WAV LSB (Synthesizer Suara Kamuflase, Waveform & Bit-Scope Inspector + Player & Foto di Audio)
    // 1 = Bongkar Stego (PNG & Audio WAV LSB)
    // 4 = Kartu Kunci Stego-QR Offline
    // 2 = Pembersih EXIF & GPS (#8)
    // 3 = Kalkulator Kamuflase 1-Klik (#9)
    var activeModeTab by remember { mutableIntStateOf(initialToolSubTab) }

    LaunchedEffect(initialToolSubTab) {
        if (initialToolSubTab != -1) {
            activeModeTab = initialToolSubTab
            onConsumeInitialToolSubTab()
        }
    }

    androidx.activity.compose.BackHandler(enabled = activeModeTab != -1) {
        WavAudioSteganographyEngine.stopPreviewAudio()
        activeModeTab = -1
    }

    DisposableEffect(Unit) {
        onDispose {
            WavAudioSteganographyEngine.stopPreviewAudio()
        }
    }

    // Available image files in the active Vault
    val vaultImageCarriers = remember(allItems) {
        allItems.filter {
            it.category != VaultCategory.ARCHIVE.name &&
                PngSteganographyEngine.isImageExtension(it.extension)
        }
    }

    // Available audio files in the active Vault for WAV LSB Steganography
    val vaultAudioCarriers = remember(allItems) {
        allItems.filter {
            it.category == VaultCategory.AUDIO.name ||
                WavAudioSteganographyEngine.isAudioExtension(it.extension)
        }
    }

    // =========================================================================
    // STATE TAB 0: STEGO FOTO PNG
    // =========================================================================
    var useVaultPhotoCarrier by remember(vaultImageCarriers.size) {
        mutableStateOf(vaultImageCarriers.isNotEmpty())
    }
    var selectedCarrierPhotoId by remember(vaultImageCarriers) {
        mutableStateOf(vaultImageCarriers.firstOrNull()?.id)
    }
    val selectedCarrierPhoto = remember(vaultImageCarriers, selectedCarrierPhotoId, useVaultPhotoCarrier) {
        if (useVaultPhotoCarrier) {
            vaultImageCarriers.firstOrNull { it.id == selectedCarrierPhotoId } ?: vaultImageCarriers.firstOrNull()
        } else {
            null
        }
    }

    var pngFileNameInput by remember { mutableStateOf("Kartu_Pos_Aurora_2026") }
    var selectedTheme by remember { mutableStateOf(StegoCoverTheme.COSMIC_POSTCARD) }
    var secretTitleInput by remember { mutableStateOf("Catatan Koordinat & Kunci Cadangan") }
    var secretMessageInput by remember {
        mutableStateOf("Pesan rahasia ini disematkan di dalam gambar PNG. Jika dibuka dari galeri biasa, hanya gambar sampul yang terlihat!")
    }
    var stegoPasscodeInput by remember { mutableStateOf("") }

    var isDualPhotoFusionMode by remember { mutableStateOf(true) }
    val secondPhotoCandidates = remember(vaultImageCarriers, selectedCarrierPhoto?.id) {
        val others = vaultImageCarriers.filter { it.id != selectedCarrierPhoto?.id }
        if (others.isNotEmpty()) others else vaultImageCarriers
    }
    var selectedSecondPhotoId by remember(secondPhotoCandidates) {
        mutableStateOf(secondPhotoCandidates.firstOrNull()?.id)
    }
    var selectedAttachmentId by remember { mutableStateOf<Int?>(null) }

    var visualComparisonMode by remember { mutableIntStateOf(0) }
    var selectedLoupeHotspot by remember { mutableIntStateOf(2) }
    var isVisualAnalyzerExpanded by remember { mutableStateOf(false) }
    var isDualPhotoAccordionExpanded by remember { mutableStateOf(false) }

    // =========================================================================
    // STATE TAB 5: STEGO AUDIO WAV (LSB 16-BIT PCM)
    // =========================================================================
    var useVaultAudioCarrier by remember { mutableStateOf(false) }
    var selectedCarrierAudioId by remember(vaultAudioCarriers) {
        mutableStateOf(vaultAudioCarriers.firstOrNull()?.id)
    }
    val selectedCarrierAudio = remember(vaultAudioCarriers, selectedCarrierAudioId, useVaultAudioCarrier) {
        if (useVaultAudioCarrier) {
            vaultAudioCarriers.firstOrNull { it.id == selectedCarrierAudioId } ?: vaultAudioCarriers.firstOrNull()
        } else {
            null
        }
    }
    var selectedWavPreset by remember { mutableStateOf(WavCamouflagePreset.RAINY_NIGHT_AMBIENCE) }
    var wavFileNameInput by remember { mutableStateOf("Rekaman_Hujan_Malam_LSB") }
    var wavSecretTitleInput by remember { mutableStateOf("Pesan Frekuensi Audio Rahasia") }
    var wavSecretMessageInput by remember {
        mutableStateOf("Pesan rahasia ini disuntikkan ke bit LSB sampel 16-bit PCM file .WAV! Saat diputar di pemutar musik, hanya suara santai yang terdengar.")
    }
    var wavPasscodeInput by remember { mutableStateOf("") }
    var isPhotoInWavMode by remember { mutableStateOf(true) }
    var selectedPhotoForWavId by remember(vaultImageCarriers) {
        mutableStateOf(vaultImageCarriers.firstOrNull()?.id)
    }
    var selectedFileForWavId by remember { mutableStateOf<Int?>(null) }
    var wavComparisonMode by remember { mutableIntStateOf(0) } // 0 = Waveform Compare, 1 = 16-Bit LSB Bit-Scope
    var selectedWavHotspot by remember { mutableIntStateOf(0) }
    var activePlayingPreviewMode by remember { mutableStateOf<String?>(null) } // "ORIG", "STEGO", "DECODED"

    // =========================================================================
    // STATE TAB 4: GENERATOR KARTU KUNCI STEGO-QR OFFLINE
    // =========================================================================
    var qrCardFileNameInput by remember { mutableStateOf("Kartu_Kunci_QR_Nyx2") }
    var qrCardTitleInput by remember { mutableStateOf("Kunci Pemulihan Master Brankas") }
    var qrCardSecretInput by remember {
        mutableStateOf("SEED-NYX2-9941-ALPHA-OMEGA :: Cadangan Kunci Darurat Offline")
    }
    var qrCardPasscodeInput by remember { mutableStateOf("") }
    var qrCardTheme by remember { mutableStateOf(StegoCoverTheme.COSMIC_POSTCARD) }

    var decodePasscodeInput by remember { mutableStateOf("") }

    // Carrier items for Tab 1 (Bongkar): both PNG images AND WAV/Audio items
    val stegoCarrierItems = remember(allItems) {
        allItems.filter {
            it.category != VaultCategory.ARCHIVE.name && (
                PngSteganographyEngine.isImageExtension(it.extension) ||
                    WavAudioSteganographyEngine.isWavExtension(it.extension) ||
                    it.rawContentPreview.startsWith("WAV_STEGO_CARRIER::") ||
                    it.rawContentPreview.startsWith("PNG_STEGO_CARRIER::")
                )
        }
    }

    val attachableItems = remember(allItems, selectedCarrierPhoto?.id) {
        allItems.filter {
            it.category != VaultCategory.ARCHIVE.name && it.id != selectedCarrierPhoto?.id
        }.take(14)
    }

    val effectiveAttachedItem = remember(
        isDualPhotoFusionMode,
        secondPhotoCandidates,
        selectedSecondPhotoId,
        attachableItems,
        selectedAttachmentId
    ) {
        if (isDualPhotoFusionMode) {
            secondPhotoCandidates.firstOrNull { it.id == selectedSecondPhotoId }
        } else {
            attachableItems.firstOrNull { it.id == selectedAttachmentId }
        }
    }

    val effectiveWavAttachedItem = remember(
        isPhotoInWavMode,
        vaultImageCarriers,
        selectedPhotoForWavId,
        attachableItems,
        selectedFileForWavId
    ) {
        if (isPhotoInWavMode) {
            vaultImageCarriers.firstOrNull { it.id == selectedPhotoForWavId }
        } else {
            attachableItems.firstOrNull { it.id == selectedFileForWavId }
        }
    }

    // Live Image Stego Stealth Analysis
    val liveStealthAnalysis = remember(
        selectedCarrierPhoto,
        secretTitleInput,
        secretMessageInput,
        effectiveAttachedItem
    ) {
        PngSteganographyEngine.analyzeStegoStealth(
            carrierItem = selectedCarrierPhoto,
            secretTitle = secretTitleInput,
            secretMessage = secretMessageInput,
            attachedItem = effectiveAttachedItem,
            isQrKeyCardMode = false
        )
    }

    // Live WAV Audio LSB Stealth Analysis
    val liveWavAnalysis = remember(
        selectedCarrierAudio,
        selectedWavPreset,
        wavSecretTitleInput,
        wavSecretMessageInput,
        effectiveWavAttachedItem
    ) {
        WavAudioSteganographyEngine.analyzeWavStegoStealth(
            carrierAudioItem = selectedCarrierAudio,
            preset = selectedWavPreset,
            secretTitle = wavSecretTitleInput,
            secretMessage = wavSecretMessageInput,
            attachedItem = effectiveWavAttachedItem
        )
    }

    val wavBitScopeSample = remember(selectedWavPreset, wavSecretMessageInput, selectedWavHotspot) {
        WavAudioSteganographyEngine.generateWavBitScopeSample(
            preset = selectedWavPreset,
            secretMessage = wavSecretMessageInput,
            hotspotIndex = selectedWavHotspot
        )
    }

    // Real rendered Bitmap for Foto 1 (Sampul Carrier) & Foto 2 (Foto Rahasia Kedua)
    val carrierPreviewBitmap = remember(selectedCarrierPhoto?.id, selectedTheme, secretTitleInput) {
        PngSteganographyEngine.loadPreviewBitmapForVaultItem(
            item = selectedCarrierPhoto,
            fallbackTheme = selectedTheme,
            customCaption = secretTitleInput,
            isSecondHiddenPhotoBadge = false
        )
    }
    val secondPhotoEntity = remember(secondPhotoCandidates, selectedSecondPhotoId) {
        secondPhotoCandidates.firstOrNull { it.id == selectedSecondPhotoId }
    }
    val secondPhotoPreviewBitmap = remember(secondPhotoEntity?.id) {
        if (secondPhotoEntity != null) {
            PngSteganographyEngine.loadPreviewBitmapForVaultItem(
                item = secondPhotoEntity,
                fallbackTheme = StegoCoverTheme.BOTANICAL_ART,
                customCaption = secondPhotoEntity.name,
                isSecondHiddenPhotoBadge = true
            )
        } else {
            null
        }
    }
    val wavHiddenPhotoEntity = remember(vaultImageCarriers, selectedPhotoForWavId) {
        vaultImageCarriers.firstOrNull { it.id == selectedPhotoForWavId }
    }
    val wavHiddenPhotoBitmap = remember(wavHiddenPhotoEntity?.id) {
        if (wavHiddenPhotoEntity != null) {
            PngSteganographyEngine.loadPreviewBitmapForVaultItem(
                item = wavHiddenPhotoEntity,
                fallbackTheme = StegoCoverTheme.COSMIC_POSTCARD,
                customCaption = wavHiddenPhotoEntity.name,
                isSecondHiddenPhotoBadge = true
            )
        } else {
            null
        }
    }

    val loupeSample = remember(carrierPreviewBitmap, selectedLoupeHotspot) {
        PngSteganographyEngine.generatePixelLoupeSample(
            carrierBitmap = carrierPreviewBitmap,
            hotspotIndex = selectedLoupeHotspot
        )
    }

    val exifCandidateItems = remember(allItems) {
        allItems.filter {
            it.category == VaultCategory.IMAGE.name ||
                it.category == VaultCategory.VIDEO.name ||
                it.extension.lowercase() in setOf("jpg", "jpeg", "png", "webp", "mp4", "mkv")
        }
    }

    // Full-screen Mori v4.6.0 Workspace inside Tools Tab (avoids nested LazyColumn)
    if (activeModeTab == 30) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .testTag("stego_tools_mori_workspace_container")
        ) {
            Box(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
                ArcanumToolsWorkspaceBackHeader(
                    activeModeTab = 30,
                    onBackToIconHub = {
                        WavAudioSteganographyEngine.stopPreviewAudio()
                        activePlayingPreviewMode = null
                        activeModeTab = -1
                    }
                )
            }
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                MoriDownloaderScreen(
                    activeVaultId = displayVaultNum,
                    moriState = moriState,
                    allVaultItems = allItems,
                    onUrlInputChange = onMoriUrlInputChange,
                    onCustomFileNameChange = onMoriCustomFileNameChange,
                    onSelectExtractionMode = onMoriSelectExtractionMode,
                    onToggleEncryptImmediately = onMoriToggleEncryptImmediately,
                    onAnalyzeLinkClick = onMoriAnalyzeLinkClick,
                    onStartDownloadToVault = onMoriStartDownloadToVault,
                    onTriggerOtaScraperUpdate = onMoriTriggerOtaScraperUpdate,
                    onOpenDownloadedItem = onOpenImageViewerClick,
                    onToggleEncryptItemWithPin = onMoriToggleEncryptItemWithPin,
                    onShareItemWithBiometric = onMoriShareItemWithBiometric,
                    onDeleteItem = onMoriDeleteItem,
                    onToggleBatchMode = onMoriToggleBatchMode,
                    onSelectBatchFormatPreference = onMoriSelectBatchFormatPreference,
                    onSelectServer = onMoriSelectServer,
                    onToggleWifiOnlyGuard = onMoriToggleWifiOnlyGuard,
                    onToggleCellularWarning = onMoriToggleCellularWarning,
                    onSelectTargetDestination = onMoriSelectTargetDestination,
                    onSelectStressScenario = onMoriSelectStressScenario,
                    onShowSubFileModalChange = onMoriShowSubFileModalChange,
                    onToggleSubFileSelection = onMoriToggleSubFileSelection,
                    onSelectAllSubFiles = onMoriSelectAllSubFiles,
                    onApplySubFileRangeSelection = onMoriApplySubFileRangeSelection,
                    onTogglePauseQueueItem = onMoriTogglePauseQueueItem,
                    onRetryQueueItem = onMoriRetryQueueItem,
                    onCancelQueueItem = onMoriCancelQueueItem,
                    onCancelActiveDownloadAndDeleteTemp = onMoriCancelActiveDownloadAndDeleteTemp,
                    onRejectAndAutoDeleteDownloadedMedia = onMoriRejectAndAutoDeleteDownloadedMedia,
                    onConfirmKeepDownloadedMedia = onMoriConfirmKeepDownloadedMedia,
                    onStartDirectFormatDownload = onMoriStartDirectFormatDownload,
                    onStartSingleSubFileDownload = onMoriStartSingleSubFileDownload,
                    onBackToToolsHub = { activeModeTab = -1 }
                )
            }
        }
        return
    }

    val toolsLazyListState = androidx.compose.foundation.lazy.rememberLazyListState()
    LaunchedEffect(activeModeTab) {
        toolsLazyListState.scrollToItem(0)
    }

    LazyColumn(
        state = toolsLazyListState,
        modifier = Modifier
            .fillMaxSize()
            .testTag("stego_tools_screen"),
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 112.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // =========================================================================
        // MINIMALIST HEADER & COMPACT SUB-TABS / TOOLS ICON HUB SWITCHER
        // =========================================================================
        item {
            val activeCategoryGroup = when (activeModeTab) {
                -1 -> -1     // Tools Category Icon Hub
                10, 11, 12, 13 -> 4 // Universal Converter Studio
                30, in 20..27 -> 5  // Mori v4.6.0, Web Sniffer, OSINT & Media Utilities
                0, 5, 1 -> 0 // Stego Media
                9, 4 -> 1    // QR Studio
                6, 7, 8 -> 2 // Lab Sandi
                else -> 3    // Privasi & EXIF
            }

            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                // Row 1: Minimalist Title + Quick 1-Click Calculator Button
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .duolingoButton(
                                    faceColor = if (activeModeTab == -1) DuoFeatherGreen else DuoBeetlePurple,
                                    lipColor = if (activeModeTab == -1) DuoFeatherGreenLip else DuoBeetlePurpleLip,
                                    cornerRadius = 12.dp,
                                    lipHeight = 3.dp
                                )
                                .instantTouchPress {
                                    WavAudioSteganographyEngine.stopPreviewAudio()
                                    activeModeTab = -1
                                }
                                .padding(bottom = 3.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Shield,
                                contentDescription = "Hub Ikon Tools",
                                tint = DuoPureWhite,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Column {
                            Text(
                                text = "Arcanum Tools & Mori v4.6.0 Hub",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.ExtraBold,
                                color = VaultObsidian
                            )
                            Text(
                                text = "Mori v4.6.0 • Converter 16+ Format • 8 OSINT Tools • Stego • Star QR",
                                style = MaterialTheme.typography.bodySmall,
                                color = VaultTextMuted
                            )
                        }
                    }

                    Box(
                        modifier = Modifier
                            .height(38.dp)
                            .duolingoButton(
                                faceColor = DuoBeeYellow,
                                lipColor = DuoBeeYellowLip,
                                cornerRadius = 12.dp,
                                lipHeight = 3.dp
                            )
                            .instantTouchPress { onActivateCalculatorCamouflage() }
                            .padding(bottom = 3.dp)
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                            .testTag("quick_panic_calculator_header_button"),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Calculate,
                                contentDescription = "Kalkulator Darurat",
                                tint = VaultObsidian,
                                modifier = Modifier.size(15.dp)
                            )
                            Text(
                                text = "Kalkulator",
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.ExtraBold,
                                color = VaultObsidian,
                                maxLines = 1
                            )
                        }
                    }
                }

                // When inside a sub-tab, show prominent "← Semua Ikon Tools" Back Header + Quick Sub-Tab Switcher
                if (activeModeTab != -1) {
                    ArcanumToolsWorkspaceBackHeader(
                        activeModeTab = activeModeTab,
                        onBackToIconHub = {
                            WavAudioSteganographyEngine.stopPreviewAudio()
                            activePlayingPreviewMode = null
                            activeModeTab = -1
                        }
                    )

                    // Row 2: 6 Category Group Tabs (Scrollable for compact clarity)
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        val categoryGroups = listOf(
                            Triple(5, "Mori & OSINT", 30),
                            Triple(4, "Converter", 10),
                            Triple(0, "Stego", 0),
                            Triple(1, "Astra QR", 9),
                            Triple(2, "Sandi", 6),
                            Triple(3, "Privasi", 2)
                        )
                        categoryGroups.forEach { (catIdx, catLabel, defaultSubTab) ->
                            val isCatSelected = activeCategoryGroup == catIdx
                            val (face, lip) = when (catIdx) {
                                4 -> DuoFeatherGreen to DuoFeatherGreenLip
                                5 -> DuoBeetlePurple to DuoBeetlePurpleLip
                                0 -> DuoMacawBlue to DuoMacawBlueLip
                                1 -> Color(0xFF0EA5E9) to Color(0xFF0369A1)
                                2 -> DuoFoxOrange to DuoFoxOrangeLip
                                else -> DuoCardinalRed to DuoCardinalRedLip
                            }
                            Box(
                                modifier = Modifier
                                    .height(38.dp)
                                    .then(
                                        if (isCatSelected) {
                                            Modifier.duolingoButton(
                                                faceColor = face,
                                                lipColor = lip,
                                                cornerRadius = 12.dp,
                                                lipHeight = 3.dp
                                            )
                                        } else {
                                            Modifier.duolingoCard(
                                                backgroundColor = DuoPureWhite,
                                                borderColor = DuoSwanBorder,
                                                lipColor = DuoHareLip,
                                                cornerRadius = 12.dp,
                                                borderWidth = 1.5.dp,
                                                lipHeight = 3.dp
                                            )
                                        }
                                    )
                                    .instantTouchPress {
                                        WavAudioSteganographyEngine.stopPreviewAudio()
                                        activePlayingPreviewMode = null
                                        activeModeTab = defaultSubTab
                                    }
                                    .padding(bottom = 3.dp)
                                    .padding(horizontal = 12.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = catLabel,
                                    style = MaterialTheme.typography.labelMedium,
                                    fontSize = 10.5.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = if (isCatSelected) Color.White else VaultObsidian,
                                    maxLines = 1
                                )
                            }
                        }
                    }

                    // Row 3: Compact Sub-Tab Pills for Active Category
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        val tabs = listOf(
                            Triple(30, "Mori v4.6.0", Icons.Default.CloudDownload),
                            Triple(10, "Office Converter", Icons.Default.AttachFile),
                            Triple(11, "SVG & Image", Icons.Default.Image),
                            Triple(12, "CSV/JSON/MD", Icons.Default.Layers),
                            Triple(13, "ZIP & Audio", Icons.Default.AudioFile)
                        ) + MoriBuiltinToolId.entries.mapIndexed { idx, tool ->
                            Triple(20 + idx, tool.title, tool.icon)
                        } + listOf(
                            Triple(0, "Foto PNG", Icons.Default.AutoAwesome),
                            Triple(5, "Audio WAV", Icons.Default.GraphicEq),
                            Triple(1, "Bongkar (${stegoCarrierItems.size})", Icons.Default.LockOpen),
                            Triple(9, "QR Bintang 100%", Icons.Default.QrCode2),
                            Triple(4, "Kartu QR", Icons.Default.QrCode2),
                            Triple(6, "Morse CW", Icons.Default.MusicNote),
                            Triple(7, "Enigma", Icons.Default.Key),
                            Triple(8, "Sandi 30-Char", Icons.Default.Lock),
                            Triple(2, "Hapus EXIF", Icons.Default.GpsOff),
                            Triple(3, "Kalkulator", Icons.Default.Calculate)
                        )
                        tabs.forEachIndexed { chipOrder, (idx, label, icon) ->
                            val isSelected = activeModeTab == idx
                            val (selFace, selLip, pastelFace) = when (chipOrder % 6) {
                                0 -> Triple(DuoFeatherGreen, DuoFeatherGreenLip, DuoFeatherGreenSoft)
                                1 -> Triple(DuoMacawBlue, DuoMacawBlueLip, DuoMacawBlueSoft)
                                2 -> Triple(DuoFoxOrange, DuoFoxOrangeLip, DuoFoxOrangeSoft)
                                3 -> Triple(DuoBeetlePurple, DuoBeetlePurpleLip, DuoBeetlePurpleSoft)
                                4 -> Triple(DuoBeeYellow, DuoBeeYellowLip, DuoBeeYellowSoft)
                                else -> Triple(DuoCardinalRed, DuoCardinalRedLip, DuoCardinalRedSoft)
                            }
                            Box(
                                modifier = Modifier
                                    .height(36.dp)
                                    .then(
                                        if (isSelected) {
                                            Modifier.duolingoButton(
                                                faceColor = selFace,
                                                lipColor = selLip,
                                                cornerRadius = 11.dp,
                                                lipHeight = 3.dp
                                            )
                                        } else {
                                            Modifier.duolingoCard(
                                                backgroundColor = pastelFace.copy(alpha = 0.65f),
                                                borderColor = selFace.copy(alpha = 0.5f),
                                                lipColor = selLip.copy(alpha = 0.6f),
                                                cornerRadius = 11.dp,
                                                borderWidth = 1.5.dp,
                                                lipHeight = 3.dp
                                            )
                                        }
                                    )
                                    .instantTouchPress {
                                        WavAudioSteganographyEngine.stopPreviewAudio()
                                        activePlayingPreviewMode = null
                                        activeModeTab = idx
                                    }
                                    .padding(bottom = 3.dp)
                                    .padding(horizontal = 10.dp, vertical = 2.dp)
                                    .testTag("stego_subtab_$idx"),
                                contentAlignment = Alignment.Center
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Icon(
                                        imageVector = icon,
                                        contentDescription = null,
                                        tint = if (isSelected) (if (selFace == DuoBeeYellow) VaultObsidian else Color.White) else selLip,
                                        modifier = Modifier.size(13.dp)
                                    )
                                    Text(
                                        text = label,
                                        style = MaterialTheme.typography.labelSmall,
                                        fontWeight = FontWeight.ExtraBold,
                                        color = if (isSelected) (if (selFace == DuoBeeYellow) VaultObsidian else Color.White) else VaultObsidian,
                                        maxLines = 1
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // DEFAULT ENTRY MODE (-1): ARCANUM TOOLS CATEGORY ICON HUB
        if (activeModeTab == -1) {
            item {
                ArcanumToolsCategoryIconHub(
                    stegoCarrierCount = stegoCarrierItems.size,
                    exifCandidateCount = exifCandidateItems.size,
                    moriState = moriState,
                    onQuickPasteAndOpenMori = { pastedUrl ->
                        onMoriUrlInputChange(pastedUrl)
                        onMoriAnalyzeLinkClick()
                    },
                    onSelectToolSubTab = { chosenSubTab ->
                        WavAudioSteganographyEngine.stopPreviewAudio()
                        activePlayingPreviewMode = null
                        activeModeTab = chosenSubTab
                    }
                )
            }
        }

        // UNIVERSAL FILE & MEDIA CONVERTER STUDIO (Sub-tabs 10, 11, 12, 13)
        if (activeModeTab in 10..13) {
            item {
                ArcanumUniversalConverterStudioSection(
                    activeVaultId = displayVaultNum,
                    allItems = allItems,
                    initialSuiteTabId = activeModeTab,
                    onSelectSuiteTabId = { newSuiteTabId ->
                        activeModeTab = newSuiteTabId
                    },
                    onSaveConvertedToVault = onSaveConvertedFileToVault,
                    onOpenInstantPreview = onOpenImageViewerClick
                )
            }
        }

        // WEB SNIFFER, OSINT & MEDIA UTILITY TOOLS (Sub-tabs 20..27)
        if (activeModeTab in 20..27) {
            item {
                val localContext = LocalContext.current
                val chosenTool = MoriBuiltinToolId.entries.getOrNull(activeModeTab - 20)
                MoriBuiltin8ToolsSuiteCard(
                    initialSelectedTool = chosenTool,
                    onBackFromDirectTool = {
                        activeModeTab = -1
                    },
                    onSendUrlToExtractor = { extractedUrl ->
                        val clipboard = localContext.getSystemService(Context.CLIPBOARD_SERVICE) as? ClipboardManager
                        clipboard?.setPrimaryClip(ClipData.newPlainText("ArcanumToolUrl", extractedUrl))
                        onMoriUrlInputChange(extractedUrl)
                        activeModeTab = 30
                    },
                    onToolStatusMessage = { _ ->
                        // Status handled inside the tool workspace card
                    }
                )
            }
        }

        // Render AstraOnyx Star QR Studio ONLY on QR tabs (9 or 4)
        if (activeModeTab == 9 || activeModeTab == 4) {
            item {
                AstraOnyxStarQrStudioSection(
                    activeVaultId = activeVaultId,
                    allItems = allItems,
                    onSaveQrToVault = onSaveStarQrToVault
                )
            }
        }

        // ERROR / STATUS MESSAGE BANNER (3D DUOLINGO CARD)
        val statusMsg = stegoState.decodeErrorMessage
        if (!statusMsg.isNullOrBlank()) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .duolingoCard(
                            backgroundColor = DuoPureWhite,
                            borderColor = DuoCardinalRed,
                            lipColor = DuoCardinalRedLip,
                            cornerRadius = 20.dp,
                            borderWidth = 2.dp,
                            lipHeight = 4.dp
                        )
                        .padding(bottom = 4.dp)
                        .testTag("stego_status_banner")
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Lock,
                            contentDescription = null,
                            tint = DuoCardinalRed
                        )
                        Text(
                            text = statusMsg,
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = FontWeight.Bold,
                            color = VaultObsidian,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }

        when (activeModeTab) {
            -1, 10, 11, 12, 13, 9, in 20..27 -> {
                // Handled above (Tools Category Icon Hub, Universal Converter Studio, Web & OSINT Tools, and AstraOnyx Star QR Studio)
            }
            0 -> {
                // =========================================================================
                // MODE 0: STUDIO STEGANOGRAFI FOTO PNG + SIDE-BY-SIDE + PIXEL X-RAY + 2 FOTO
                // =========================================================================
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .duolingoCard(
                                backgroundColor = DuoPureWhite,
                                borderColor = DuoMacawBlue,
                                lipColor = DuoMacawBlueLip,
                                cornerRadius = 26.dp,
                                borderWidth = 2.dp,
                                lipHeight = 6.dp
                            )
                            .padding(bottom = 6.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(18.dp),
                            verticalArrangement = Arrangement.spacedBy(14.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "1. Pilih Foto 1 (Gambar Sampul Terlihat)",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = VaultObsidian,
                                    modifier = Modifier.weight(1f)
                                )
                                DuoTactileChip(
                                    selected = true,
                                    label = "Ke Audio WAV →",
                                    icon = Icons.Default.GraphicEq,
                                    selectedFaceColor = DuoFoxOrange,
                                    selectedLipColor = DuoFoxOrangeLip,
                                    onClick = { activeModeTab = 5 },
                                    modifier = Modifier.testTag("jump_to_wav_stego_button")
                                )
                            }

                            // 3D Duolingo Carrier Source Toggle: Vault Photo vs Built-in Poster Theme
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                DuoTactileChip(
                                    selected = useVaultPhotoCarrier,
                                    label = "Foto di Brankas (${vaultImageCarriers.size})",
                                    icon = Icons.Default.PhotoLibrary,
                                    selectedFaceColor = DuoMacawBlue,
                                    selectedLipColor = DuoMacawBlueLip,
                                    onClick = { useVaultPhotoCarrier = true },
                                    modifier = Modifier
                                        .weight(1f)
                                        .testTag("carrier_source_vault_photo_chip")
                                )

                                DuoTactileChip(
                                    selected = !useVaultPhotoCarrier,
                                    label = "Poster Bawaan (4)",
                                    icon = Icons.Default.Image,
                                    selectedFaceColor = DuoBeetlePurple,
                                    selectedLipColor = DuoBeetlePurpleLip,
                                    onClick = { useVaultPhotoCarrier = false },
                                    modifier = Modifier
                                        .weight(1f)
                                        .testTag("carrier_source_poster_chip")
                                )
                            }

                            if (useVaultPhotoCarrier && vaultImageCarriers.isNotEmpty()) {
                                Text(
                                    text = "Pilih Foto 1 (Sampul Utama) dari Brankas #$displayVaultNum:",
                                    style = MaterialTheme.typography.labelLarge,
                                    fontWeight = FontWeight.Bold,
                                    color = VaultObsidian
                                )
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .horizontalScroll(rememberScrollState()),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    vaultImageCarriers.forEach { photoItem ->
                                        val isChosen = selectedCarrierPhoto?.id == photoItem.id
                                        DuoTactileChip(
                                            selected = isChosen,
                                            label = "${photoItem.name}.${photoItem.extension} (${formatStegoBytes(photoItem.sizeBytes)})",
                                            icon = Icons.Default.Image,
                                            selectedFaceColor = DuoFeatherGreen,
                                            selectedLipColor = DuoFeatherGreenLip,
                                            onClick = {
                                                selectedCarrierPhotoId = photoItem.id
                                                pngFileNameInput = "${photoItem.name}_Stego"
                                            },
                                            modifier = Modifier.testTag("select_vault_photo_carrier_${photoItem.id}")
                                        )
                                    }
                                }
                            } else {
                                Text(
                                    text = "Pilih Desain Poster Sampul Bawaan (Taktil 3D):",
                                    style = MaterialTheme.typography.labelLarge,
                                    fontWeight = FontWeight.Bold,
                                    color = VaultObsidian
                                )
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .horizontalScroll(rememberScrollState()),
                                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                                ) {
                                    StegoCoverTheme.entries.forEach { theme ->
                                        val isPicked = selectedTheme == theme
                                        Box(
                                            modifier = Modifier
                                                .width(160.dp)
                                                .duolingoCard(
                                                    backgroundColor = Color(theme.primaryColorHex),
                                                    borderColor = if (isPicked) VaultLime else Color(theme.accentColorHex),
                                                    lipColor = Color(theme.secondaryColorHex),
                                                    cornerRadius = 18.dp,
                                                    borderWidth = if (isPicked) 3.dp else 2.dp,
                                                    lipHeight = 5.dp
                                                )
                                                .instantTouchPress { selectedTheme = theme }
                                                .padding(bottom = 5.dp)
                                                .padding(12.dp)
                                                .testTag("stego_theme_${theme.name}")
                                        ) {
                                            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                                Box(
                                                    modifier = Modifier
                                                        .size(12.dp)
                                                        .clip(CircleShape)
                                                        .background(Color(theme.accentColorHex))
                                                )
                                                Text(
                                                    text = theme.title,
                                                    style = MaterialTheme.typography.labelLarge,
                                                    fontWeight = FontWeight.ExtraBold,
                                                    color = Color.White,
                                                    maxLines = 1,
                                                    overflow = TextOverflow.Ellipsis
                                                )
                                                Text(
                                                    text = theme.subtitle,
                                                    style = MaterialTheme.typography.bodySmall,
                                                    color = Color.White.copy(alpha = 0.85f),
                                                    maxLines = 2,
                                                    overflow = TextOverflow.Ellipsis
                                                )
                                            }
                                        }
                                    }
                                }
                            }

                            // =========================================================================
                            // 2. SIDE-BY-SIDE VISUAL COMPARISON + PIXEL X-RAY + LOUPE 8X (3D DUO CARD)
                            // =========================================================================
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .duolingoCard(
                                        backgroundColor = VaultCream,
                                        borderColor = DuoMacawBlue,
                                        lipColor = DuoMacawBlueLip,
                                        cornerRadius = 22.dp,
                                        borderWidth = 2.dp,
                                        lipHeight = 5.dp
                                    )
                                    .padding(bottom = 5.dp)
                                    .testTag("stego_stealth_analyzer_card")
                            ) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(14.dp),
                                    verticalArrangement = Arrangement.spacedBy(10.dp)
                                ) {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clickable { isVisualAnalyzerExpanded = !isVisualAnalyzerExpanded },
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                                            modifier = Modifier.weight(1f)
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Compare,
                                                contentDescription = null,
                                                tint = DuoMacawBlue,
                                                modifier = Modifier.size(18.dp)
                                            )
                                            Text(
                                                text = if (isVisualAnalyzerExpanded) {
                                                    "Visual X-Ray & Loupe 8x (Ketuk Tutup)"
                                                } else {
                                                    "Visual X-Ray & Loupe 8x (Ketuk Buka)"
                                                },
                                                style = MaterialTheme.typography.labelLarge,
                                                fontWeight = FontWeight.ExtraBold,
                                                color = VaultObsidian,
                                                maxLines = 1,
                                                overflow = TextOverflow.Ellipsis
                                            )
                                        }
                                        Box(
                                            modifier = Modifier
                                                .duolingoButton(
                                                    faceColor = DuoFeatherGreen,
                                                    lipColor = DuoFeatherGreenLip,
                                                    cornerRadius = 50.dp,
                                                    lipHeight = 3.dp
                                                )
                                                .padding(horizontal = 10.dp, vertical = 4.dp)
                                        ) {
                                            Text(
                                                text = "Stealth ${liveStealthAnalysis.stealthScorePercent}%",
                                                style = MaterialTheme.typography.labelSmall,
                                                fontWeight = FontWeight.ExtraBold,
                                                color = Color.White,
                                                maxLines = 1
                                            )
                                        }
                                    }

                                    if (isVisualAnalyzerExpanded) {
                                    // 3 Mode Switcher 3D Pills: Normal RGB | Pixel X-Ray | Loupe 8x
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        val compModes = listOf(
                                            Triple(0, "Normal RGB", "visual_comp_mode_normal"),
                                            Triple(1, "Pixel X-Ray", "visual_comp_mode_xray"),
                                            Triple(2, "Loupe 8x", "visual_comp_mode_loupe")
                                        )
                                        compModes.forEach { (modeIdx, modeLabel, tag) ->
                                            DuoTactileChip(
                                                selected = visualComparisonMode == modeIdx,
                                                label = modeLabel,
                                                selectedFaceColor = DuoObsidianChipColor,
                                                selectedLipColor = DuoObsidianLipColor,
                                                selectedTextColor = VaultLime,
                                                onClick = { visualComparisonMode = modeIdx },
                                                modifier = Modifier
                                                    .weight(1f)
                                                    .testTag(tag)
                                            )
                                        }
                                    }

                                    // ACTUAL SIDE-BY-SIDE VISUAL IMAGE PREVIEW (ORIGINAL VS STEGO MODIFIED)
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .testTag("stego_side_by_side_visual_comparison"),
                                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                                    ) {
                                        // LEFT PANEL: GAMBAR ASLI (SEBELUM)
                                        Box(
                                            modifier = Modifier
                                                .weight(1f)
                                                .duolingoCard(
                                                    backgroundColor = DuoPureWhite,
                                                    borderColor = DuoSwanBorder,
                                                    lipColor = DuoHareLip,
                                                    cornerRadius = 18.dp,
                                                    borderWidth = 2.dp,
                                                    lipHeight = 4.dp
                                                )
                                                .padding(bottom = 4.dp)
                                        ) {
                                            Column(
                                                modifier = Modifier.padding(8.dp),
                                                verticalArrangement = Arrangement.spacedBy(6.dp)
                                            ) {
                                                Row(
                                                    modifier = Modifier.fillMaxWidth(),
                                                    horizontalArrangement = Arrangement.SpaceBetween,
                                                    verticalAlignment = Alignment.CenterVertically
                                                ) {
                                                    Text(
                                                        text = "GAMBAR ASLI",
                                                        fontFamily = JetBrainsMonoFontFamily,
                                                        fontSize = 9.sp,
                                                        fontWeight = FontWeight.ExtraBold,
                                                        color = VaultTextMuted,
                                                        maxLines = 1
                                                    )
                                                    Text(
                                                        text = formatStegoBytes(liveStealthAnalysis.carrierBaseBytes),
                                                        fontFamily = JetBrainsMonoFontFamily,
                                                        fontSize = 9.sp,
                                                        fontWeight = FontWeight.Bold,
                                                        color = VaultObsidian,
                                                        maxLines = 1
                                                    )
                                                }

                                                Box(
                                                    modifier = Modifier
                                                        .fillMaxWidth()
                                                        .height(118.dp)
                                                        .clip(RoundedCornerShape(12.dp))
                                                        .background(Color(0xFF0F172A))
                                                ) {
                                                    if (!carrierPreviewBitmap.isRecycled) {
                                                        Image(
                                                            bitmap = remember(carrierPreviewBitmap) {
                                                                carrierPreviewBitmap.asImageBitmap()
                                                            },
                                                            contentDescription = "Pratinjau Gambar Asli Sebelum Steganografi",
                                                            contentScale = ContentScale.Crop,
                                                            modifier = Modifier.fillMaxSize()
                                                        )
                                                    }

                                                    if (visualComparisonMode == 1) {
                                                        Canvas(modifier = Modifier.fillMaxSize()) {
                                                            val step = size.height / 6f
                                                            for (i in 1..5) {
                                                                drawLine(
                                                                    color = Color(0xFF38BDF8).copy(alpha = 0.35f),
                                                                    start = Offset(0f, i * step),
                                                                    end = Offset(size.width, i * step),
                                                                    strokeWidth = 1.5f
                                                                )
                                                            }
                                                        }
                                                        Surface(
                                                            color = Color(0xCC0F172A),
                                                            shape = RoundedCornerShape(6.dp),
                                                            modifier = Modifier
                                                                .align(Alignment.BottomStart)
                                                                .padding(6.dp)
                                                        ) {
                                                            Text(
                                                                text = "X-RAY: IEND MURNI",
                                                                fontFamily = JetBrainsMonoFontFamily,
                                                                fontSize = 8.sp,
                                                                fontWeight = FontWeight.ExtraBold,
                                                                color = Color(0xFF38BDF8),
                                                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                                            )
                                                        }
                                                    }
                                                }

                                                Text(
                                                    text = "Piksel RGB: 100% Asli • Artefak: 0%",
                                                    fontFamily = JetBrainsMonoFontFamily,
                                                    fontSize = 9.sp,
                                                    color = DuoFeatherGreenLip,
                                                    fontWeight = FontWeight.Bold,
                                                    maxLines = 1,
                                                    overflow = TextOverflow.Ellipsis
                                                )
                                            }
                                        }

                                        // RIGHT PANEL: GAMBAR HASIL STEGO (SESUDAH)
                                        Box(
                                            modifier = Modifier
                                                .weight(1f)
                                                .duolingoCard(
                                                    backgroundColor = DuoPureWhite,
                                                    borderColor = DuoBeetlePurple,
                                                    lipColor = DuoBeetlePurpleLip,
                                                    cornerRadius = 18.dp,
                                                    borderWidth = 2.dp,
                                                    lipHeight = 4.dp
                                                )
                                                .padding(bottom = 4.dp)
                                        ) {
                                            Column(
                                                modifier = Modifier.padding(8.dp),
                                                verticalArrangement = Arrangement.spacedBy(6.dp)
                                            ) {
                                                Row(
                                                    modifier = Modifier.fillMaxWidth(),
                                                    horizontalArrangement = Arrangement.SpaceBetween,
                                                    verticalAlignment = Alignment.CenterVertically
                                                ) {
                                                    Text(
                                                        text = "HASIL STEGO",
                                                        fontFamily = JetBrainsMonoFontFamily,
                                                        fontSize = 9.sp,
                                                        fontWeight = FontWeight.ExtraBold,
                                                        color = DuoBeetlePurpleLip,
                                                        maxLines = 1
                                                    )
                                                    Text(
                                                        text = formatStegoBytes(liveStealthAnalysis.estimatedTotalBytes),
                                                        fontFamily = JetBrainsMonoFontFamily,
                                                        fontSize = 9.sp,
                                                        fontWeight = FontWeight.Bold,
                                                        color = DuoBeetlePurpleLip,
                                                        maxLines = 1
                                                    )
                                                }

                                                Box(
                                                    modifier = Modifier
                                                        .fillMaxWidth()
                                                        .height(118.dp)
                                                        .clip(RoundedCornerShape(12.dp))
                                                        .background(Color(0xFF0F172A))
                                                ) {
                                                    if (!carrierPreviewBitmap.isRecycled) {
                                                        Image(
                                                            bitmap = remember(carrierPreviewBitmap) {
                                                                carrierPreviewBitmap.asImageBitmap()
                                                            },
                                                            contentDescription = "Pratinjau Gambar Sesudah Modifikasi Steganografi",
                                                            contentScale = ContentScale.Crop,
                                                            modifier = Modifier.fillMaxSize()
                                                        )
                                                    }

                                                    if (visualComparisonMode == 1) {
                                                        Canvas(modifier = Modifier.fillMaxSize()) {
                                                            val step = size.height / 6f
                                                            for (i in 1..5) {
                                                                drawLine(
                                                                    color = Color(0xFFA855F7).copy(alpha = 0.4f),
                                                                    start = Offset(0f, i * step),
                                                                    end = Offset(size.width, i * step),
                                                                    strokeWidth = 1.5f
                                                                )
                                                            }
                                                            val zoneTop = size.height * 0.68f
                                                            drawRect(
                                                                color = Color(0xAA58CC02),
                                                                topLeft = Offset(0f, zoneTop),
                                                                size = Size(size.width, size.height - zoneTop)
                                                            )
                                                        }
                                                        Surface(
                                                            color = Color(0xDD0F172A),
                                                            shape = RoundedCornerShape(6.dp),
                                                            modifier = Modifier
                                                                .align(Alignment.BottomStart)
                                                                .padding(6.dp)
                                                        ) {
                                                            Text(
                                                                text = "X-RAY: +${formatStegoBytes(liveStealthAnalysis.payloadBytes.toLong())} IEND",
                                                                fontFamily = JetBrainsMonoFontFamily,
                                                                fontSize = 8.sp,
                                                                fontWeight = FontWeight.ExtraBold,
                                                                color = VaultLime,
                                                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                                            )
                                                        }
                                                    } else if (isDualPhotoFusionMode && secondPhotoPreviewBitmap != null) {
                                                        Surface(
                                                            color = Color(0xCC0F172A),
                                                            shape = RoundedCornerShape(8.dp),
                                                            modifier = Modifier
                                                                .align(Alignment.BottomEnd)
                                                                .padding(5.dp)
                                                        ) {
                                                            Row(
                                                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp),
                                                                verticalAlignment = Alignment.CenterVertically,
                                                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                                                            ) {
                                                                Icon(
                                                                    imageVector = Icons.Default.Layers,
                                                                    contentDescription = null,
                                                                    tint = VaultLime,
                                                                    modifier = Modifier.size(11.dp)
                                                                )
                                                                Text(
                                                                    text = "2 Foto di 1 File",
                                                                    fontFamily = JetBrainsMonoFontFamily,
                                                                    fontSize = 8.sp,
                                                                    fontWeight = FontWeight.ExtraBold,
                                                                    color = VaultLime
                                                                )
                                                            }
                                                        }
                                                    }
                                                }

                                                Text(
                                                    text = "Diff Visual: 0.00% (+${liveStealthAnalysis.overheadPercent}%)",
                                                    fontFamily = JetBrainsMonoFontFamily,
                                                    fontSize = 9.sp,
                                                    color = DuoBeetlePurpleLip,
                                                    fontWeight = FontWeight.Bold,
                                                    maxLines = 1,
                                                    overflow = TextOverflow.Ellipsis
                                                )
                                            }
                                        }
                                    }

                                    // MODE-SPECIFIC INSPECTOR PANEL (X-RAY OR LOUPE 8X)
                                    if (visualComparisonMode == 1) {
                                        Box(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .duolingoCard(
                                                    backgroundColor = Color(0xFF0F172A),
                                                    borderColor = DuoMacawBlue,
                                                    lipColor = DuoObsidianLipColor,
                                                    cornerRadius = 16.dp,
                                                    borderWidth = 2.dp,
                                                    lipHeight = 4.dp
                                                )
                                                .padding(bottom = 4.dp)
                                                .testTag("pixel_xray_inspector_panel")
                                        ) {
                                            Column(
                                                modifier = Modifier.padding(12.dp),
                                                verticalArrangement = Arrangement.spacedBy(4.dp)
                                            ) {
                                                Text(
                                                    text = "PEMINDAI PIXEL X-RAY (ANALISIS BIT-PLANE & CHUNK PNG)",
                                                    fontFamily = JetBrainsMonoFontFamily,
                                                    fontSize = 10.sp,
                                                    fontWeight = FontWeight.ExtraBold,
                                                    color = VaultLime
                                                )
                                                Text(
                                                    text = "• Chunk Visual IDAT: Tidak tersentuh (ΔE RGB = 0.00, bebas artefak kompresi).",
                                                    fontFamily = JetBrainsMonoFontFamily,
                                                    fontSize = 10.sp,
                                                    color = Color.White.copy(alpha = 0.9f)
                                                )
                                                Text(
                                                    text = "• Zona Hijau X-Ray: Muatan Stego (+${formatStegoBytes(liveStealthAnalysis.payloadBytes.toLong())}) disuntikkan tepat setelah penutup [IEND].",
                                                    fontFamily = JetBrainsMonoFontFamily,
                                                    fontSize = 10.sp,
                                                    color = Color(0xFF38BDF8)
                                                )
                                            }
                                        }
                                    } else if (visualComparisonMode == 2) {
                                        Box(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .duolingoCard(
                                                    backgroundColor = Color(0xFF0F172A),
                                                    borderColor = DuoFeatherGreen,
                                                    lipColor = DuoObsidianLipColor,
                                                    cornerRadius = 16.dp,
                                                    borderWidth = 2.dp,
                                                    lipHeight = 4.dp
                                                )
                                                .padding(bottom = 4.dp)
                                                .testTag("pixel_loupe_8x_panel")
                                        ) {
                                            Column(
                                                modifier = Modifier.padding(12.dp),
                                                verticalArrangement = Arrangement.spacedBy(8.dp)
                                            ) {
                                                Row(
                                                    modifier = Modifier.fillMaxWidth(),
                                                    horizontalArrangement = Arrangement.SpaceBetween,
                                                    verticalAlignment = Alignment.CenterVertically
                                                ) {
                                                    Row(
                                                        verticalAlignment = Alignment.CenterVertically,
                                                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                                                    ) {
                                                        Icon(
                                                            imageVector = Icons.Default.Search,
                                                            contentDescription = null,
                                                            tint = VaultLime,
                                                            modifier = Modifier.size(15.dp)
                                                        )
                                                        Text(
                                                            text = "LOUPE 8X PIXEL INSPECTOR (${loupeSample.coordX}, ${loupeSample.coordY})",
                                                            fontFamily = JetBrainsMonoFontFamily,
                                                            fontSize = 10.sp,
                                                            fontWeight = FontWeight.ExtraBold,
                                                            color = VaultLime
                                                        )
                                                    }
                                                    Text(
                                                        text = "ΔE = 0.00",
                                                        fontFamily = JetBrainsMonoFontFamily,
                                                        fontSize = 10.sp,
                                                        fontWeight = FontWeight.ExtraBold,
                                                        color = Color(0xFF38BDF8)
                                                    )
                                                }

                                                Row(
                                                    modifier = Modifier.fillMaxWidth(),
                                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                                ) {
                                                    val hotspots = listOf(
                                                        0 to "Atas-Kiri",
                                                        1 to "Pusat",
                                                        2 to "Ekor Stego"
                                                    )
                                                    hotspots.forEach { (hIdx, hLabel) ->
                                                        DuoTactileChip(
                                                            selected = selectedLoupeHotspot == hIdx,
                                                            label = hLabel,
                                                            selectedFaceColor = DuoBeetlePurple,
                                                            selectedLipColor = DuoBeetlePurpleLip,
                                                            onClick = { selectedLoupeHotspot = hIdx },
                                                            modifier = Modifier.weight(1f)
                                                        )
                                                    }
                                                }

                                                Row(
                                                    modifier = Modifier.fillMaxWidth(),
                                                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                                                    verticalAlignment = Alignment.CenterVertically
                                                ) {
                                                    Column(
                                                        modifier = Modifier.weight(1f),
                                                        horizontalAlignment = Alignment.CenterHorizontally,
                                                        verticalArrangement = Arrangement.spacedBy(4.dp)
                                                    ) {
                                                        Text(
                                                            text = "Piksel Asli 8x (${loupeSample.originalHex})",
                                                            fontFamily = JetBrainsMonoFontFamily,
                                                            fontSize = 9.sp,
                                                            color = Color.White.copy(alpha = 0.85f),
                                                            maxLines = 1
                                                        )
                                                        PixelGrid5x5Canvas(
                                                            colors = loupeSample.gridColorsOriginal,
                                                            gridSize = 68.dp
                                                        )
                                                    }

                                                    Column(
                                                        modifier = Modifier.weight(1f),
                                                        horizontalAlignment = Alignment.CenterHorizontally,
                                                        verticalArrangement = Arrangement.spacedBy(4.dp)
                                                    ) {
                                                        Text(
                                                            text = "Piksel Stego 8x (${loupeSample.stegoHex})",
                                                            fontFamily = JetBrainsMonoFontFamily,
                                                            fontSize = 9.sp,
                                                            color = VaultLime,
                                                            maxLines = 1
                                                        )
                                                        PixelGrid5x5Canvas(
                                                            colors = loupeSample.gridColorsOriginal,
                                                            gridSize = 68.dp
                                                        )
                                                    }
                                                }

                                                Text(
                                                    text = "Bit-Plane LSB: ${loupeSample.lsbBitSignature} • Warna Hex Asli & Stego 100% Identik",
                                                    fontFamily = JetBrainsMonoFontFamily,
                                                    fontSize = 9.sp,
                                                    color = Color.White.copy(alpha = 0.78f),
                                                    maxLines = 1,
                                                    overflow = TextOverflow.Ellipsis
                                                )
                                            }
                                        }
                                    }

                                    Text(
                                        text = "${liveStealthAnalysis.statusLabel} — ${liveStealthAnalysis.carrierModeDescription}",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = VaultObsidian,
                                        fontWeight = FontWeight.Medium
                                    )
                                    }
                                }
                            }

                            // =========================================================================
                            // 3. DUA FOTO DALAM SATU FILE (DUAL-PHOTO FUSION ACCORDION CARD)
                            // =========================================================================
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .duolingoCard(
                                        backgroundColor = DuoPureWhite,
                                        borderColor = DuoBeetlePurple,
                                        lipColor = DuoBeetlePurpleLip,
                                        cornerRadius = 22.dp,
                                        borderWidth = 2.dp,
                                        lipHeight = 5.dp
                                    )
                                    .padding(bottom = 5.dp)
                                    .testTag("dual_photo_stego_fusion_card")
                            ) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(14.dp),
                                    verticalArrangement = Arrangement.spacedBy(10.dp)
                                ) {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clickable { isDualPhotoAccordionExpanded = !isDualPhotoAccordionExpanded },
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Layers,
                                            contentDescription = null,
                                            tint = DuoBeetlePurpleLip,
                                            modifier = Modifier.size(20.dp)
                                        )
                                        Column(modifier = Modifier.weight(1f)) {
                                            Text(
                                                text = if (isDualPhotoAccordionExpanded) {
                                                    "Sisipkan Foto ke-2 / Lampiran File (Ketuk Tutup)"
                                                } else {
                                                    "Sisipkan Foto ke-2 / Lampiran File (Ketuk Buka)"
                                                },
                                                style = MaterialTheme.typography.labelLarge,
                                                fontWeight = FontWeight.ExtraBold,
                                                color = VaultObsidian,
                                                maxLines = 1,
                                                overflow = TextOverflow.Ellipsis
                                            )
                                            Text(
                                                text = "Gabungkan 2 foto atau file rahasia ke dalam 1 gambar .PNG",
                                                style = MaterialTheme.typography.bodySmall,
                                                color = VaultTextMuted,
                                                maxLines = 1,
                                                overflow = TextOverflow.Ellipsis
                                            )
                                        }
                                    }

                                    if (isDualPhotoAccordionExpanded) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        DuoTactileChip(
                                            selected = isDualPhotoFusionMode,
                                            label = "2 Foto dalam 1 File",
                                            icon = Icons.Default.Layers,
                                            selectedFaceColor = DuoBeetlePurple,
                                            selectedLipColor = DuoBeetlePurpleLip,
                                            onClick = { isDualPhotoFusionMode = true },
                                            modifier = Modifier
                                                .weight(1f)
                                                .testTag("toggle_dual_photo_fusion_chip")
                                        )

                                        DuoTactileChip(
                                            selected = !isDualPhotoFusionMode,
                                            label = "Teks / File Umum",
                                            icon = Icons.Default.AttachFile,
                                            selectedFaceColor = DuoFoxOrange,
                                            selectedLipColor = DuoFoxOrangeLip,
                                            onClick = { isDualPhotoFusionMode = false },
                                            modifier = Modifier
                                                .weight(1f)
                                                .testTag("toggle_standard_attachment_chip")
                                        )
                                    }

                                    if (isDualPhotoFusionMode) {
                                        if (secondPhotoCandidates.isNotEmpty()) {
                                            Text(
                                                text = "Pilih Foto 2 (Foto Rahasia yang Disembunyikan di Dalam Foto 1):",
                                                style = MaterialTheme.typography.labelMedium,
                                                fontWeight = FontWeight.Bold,
                                                color = VaultObsidian
                                            )
                                            Row(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .horizontalScroll(rememberScrollState()),
                                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                                            ) {
                                                secondPhotoCandidates.forEach { secPhoto ->
                                                    DuoTactileChip(
                                                        selected = selectedSecondPhotoId == secPhoto.id,
                                                        label = "Foto 2: ${secPhoto.name}.${secPhoto.extension}",
                                                        icon = Icons.Default.PhotoLibrary,
                                                        selectedFaceColor = DuoBeetlePurple,
                                                        selectedLipColor = DuoBeetlePurpleLip,
                                                        onClick = { selectedSecondPhotoId = secPhoto.id },
                                                        modifier = Modifier.testTag("select_second_secret_photo_${secPhoto.id}")
                                                    )
                                                }
                                            }

                                            if (secondPhotoPreviewBitmap != null) {
                                                Box(
                                                    modifier = Modifier
                                                        .fillMaxWidth()
                                                        .duolingoCard(
                                                            backgroundColor = VaultCream,
                                                            borderColor = DuoSwanBorder,
                                                            lipColor = DuoHareLip,
                                                            cornerRadius = 16.dp,
                                                            borderWidth = 2.dp,
                                                            lipHeight = 4.dp
                                                        )
                                                        .padding(bottom = 4.dp)
                                                ) {
                                                    Row(
                                                        modifier = Modifier
                                                            .fillMaxWidth()
                                                            .padding(10.dp),
                                                        verticalAlignment = Alignment.CenterVertically,
                                                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                                                    ) {
                                                        if (!carrierPreviewBitmap.isRecycled) {
                                                            Image(
                                                                bitmap = remember(carrierPreviewBitmap) {
                                                                    carrierPreviewBitmap.asImageBitmap()
                                                                },
                                                                contentDescription = "Foto 1 Sampul",
                                                                contentScale = ContentScale.Crop,
                                                                modifier = Modifier
                                                                    .size(48.dp)
                                                                    .clip(RoundedCornerShape(10.dp))
                                                            )
                                                        }
                                                        Text(
                                                            text = "+",
                                                            style = MaterialTheme.typography.titleMedium,
                                                            fontWeight = FontWeight.ExtraBold,
                                                            color = DuoBeetlePurpleLip
                                                        )
                                                        if (!secondPhotoPreviewBitmap.isRecycled) {
                                                            Image(
                                                                bitmap = remember(secondPhotoPreviewBitmap) {
                                                                    secondPhotoPreviewBitmap.asImageBitmap()
                                                                },
                                                                contentDescription = "Foto 2 Rahasia",
                                                                contentScale = ContentScale.Crop,
                                                                modifier = Modifier
                                                                    .size(48.dp)
                                                                    .clip(RoundedCornerShape(10.dp))
                                                            )
                                                        }
                                                        Column(modifier = Modifier.weight(1f)) {
                                                            Text(
                                                                text = "2 Foto Menjadi 1 File .PNG",
                                                                style = MaterialTheme.typography.labelMedium,
                                                                fontWeight = FontWeight.ExtraBold,
                                                                color = VaultObsidian,
                                                                maxLines = 1,
                                                                overflow = TextOverflow.Ellipsis
                                                            )
                                                            Text(
                                                                text = "Sampul: ${selectedCarrierPhoto?.name ?: selectedTheme.title} • Isi: ${secondPhotoEntity?.name ?: "-"}",
                                                                style = MaterialTheme.typography.bodySmall,
                                                                color = DuoBeetlePurpleLip,
                                                                maxLines = 1,
                                                                overflow = TextOverflow.Ellipsis
                                                            )
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    } else if (attachableItems.isNotEmpty()) {
                                        Text(
                                            text = "Opsional: Sisipkan File Brankas Lain ke Dalam Gambar PNG",
                                            style = MaterialTheme.typography.labelMedium,
                                            fontWeight = FontWeight.Bold,
                                            color = VaultObsidian
                                        )
                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .horizontalScroll(rememberScrollState()),
                                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                                        ) {
                                            DuoTactileChip(
                                                selected = selectedAttachmentId == null,
                                                label = "Hanya Pesan Teks",
                                                selectedFaceColor = DuoFoxOrange,
                                                selectedLipColor = DuoFoxOrangeLip,
                                                onClick = { selectedAttachmentId = null }
                                            )
                                            attachableItems.forEach { candidate ->
                                                DuoTactileChip(
                                                    selected = selectedAttachmentId == candidate.id,
                                                    label = "${candidate.name}.${candidate.extension}",
                                                    icon = Icons.Default.AttachFile,
                                                    selectedFaceColor = DuoFoxOrange,
                                                    selectedLipColor = DuoFoxOrangeLip,
                                                    onClick = { selectedAttachmentId = candidate.id }
                                                )
                                            }
                                        }
                                    }
                                    }
                                }
                            }

                            OutlinedTextField(
                                value = pngFileNameInput,
                                onValueChange = { pngFileNameInput = it },
                                label = { Text("Nama File Gambar .PNG di Brankas") },
                                singleLine = true,
                                colors = duoTextFieldColors(),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("stego_png_filename_input"),
                                shape = RoundedCornerShape(16.dp)
                            )

                            OutlinedTextField(
                                value = secretTitleInput,
                                onValueChange = { secretTitleInput = it },
                                label = { Text("Judul Pesan Rahasia (Terenkripsi di dalam PNG)") },
                                singleLine = true,
                                colors = duoTextFieldColors(),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("stego_secret_title_input"),
                                shape = RoundedCornerShape(16.dp)
                            )

                            OutlinedTextField(
                                value = secretMessageInput,
                                onValueChange = { secretMessageInput = it },
                                label = { Text("Isi Pesan Teks / Catatan Rahasia di Dalam Gambar") },
                                minLines = 2,
                                maxLines = 4,
                                colors = duoTextFieldColors(),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("stego_secret_message_input"),
                                shape = RoundedCornerShape(16.dp)
                            )

                            OutlinedTextField(
                                value = stegoPasscodeInput,
                                onValueChange = { stegoPasscodeInput = it.uppercase().take(12) },
                                label = { Text("Sandi Buka Stego (Kosongkan jika tanpa sandi tambahan)") },
                                singleLine = true,
                                colors = duoTextFieldColors(),
                                leadingIcon = {
                                    Icon(Icons.Default.Key, contentDescription = null, tint = DuoMacawBlue)
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("stego_passcode_input"),
                                shape = RoundedCornerShape(16.dp)
                            )

                            DuoTactileActionButton(
                                text = when {
                                    isDualPhotoFusionMode && effectiveAttachedItem != null ->
                                        "Satukan 2 Foto ke Dalam 1 File .PNG"
                                    selectedCarrierPhoto != null ->
                                        "Sisipkan Pesan ke Foto Brankas (.PNG)"
                                    else ->
                                        "Sembunyikan ke Dalam Gambar .PNG"
                                },
                                icon = Icons.Default.AutoAwesome,
                                faceColor = DuoFeatherGreen,
                                lipColor = DuoFeatherGreenLip,
                                height = 54.dp,
                                onClick = {
                                    onCreateStegoPngClick(
                                        pngFileNameInput,
                                        selectedTheme,
                                        secretTitleInput,
                                        secretMessageInput,
                                        stegoPasscodeInput,
                                        effectiveAttachedItem,
                                        selectedCarrierPhoto,
                                        false
                                    )
                                    activeModeTab = 1
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("create_stego_png_submit_button")
                            )
                        }
                    }
                }
            }

            5 -> {
                // =========================================================================
                // MODE 5 (BARU): STUDIO STEGANOGRAFI AUDIO WAV (16-BIT PCM LSB ENCODING)
                // + SYNTHESIZER SUARA KAMUFLASE + WAVEFORM & BIT-SCOPE + PLAYER + FOTO DI AUDIO
                // =========================================================================
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .duolingoCard(
                                backgroundColor = DuoPureWhite,
                                borderColor = DuoFoxOrange,
                                lipColor = DuoFoxOrangeLip,
                                cornerRadius = 26.dp,
                                borderWidth = 2.dp,
                                lipHeight = 6.dp
                            )
                            .padding(bottom = 6.dp)
                            .testTag("wav_lsb_stego_studio_card")
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(18.dp),
                            verticalArrangement = Arrangement.spacedBy(14.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(46.dp)
                                        .duolingoButton(
                                            faceColor = DuoFoxOrange,
                                            lipColor = DuoFoxOrangeLip,
                                            cornerRadius = 14.dp,
                                            lipHeight = 3.dp
                                        )
                                        .padding(bottom = 3.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.GraphicEq,
                                        contentDescription = null,
                                        tint = Color.White,
                                        modifier = Modifier.size(26.dp)
                                    )
                                }
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = "Steganografi Audio .WAV (16-Bit PCM LSB)",
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.ExtraBold,
                                        color = VaultObsidian
                                    )
                                    Text(
                                        text = "Suntikkan pesan teks & foto rahasia ke bit LSB sampel gelombang suara .WAV tanpa mengubah suara asli!",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = VaultTextMuted
                                    )
                                }
                            }

                            Text(
                                text = "1. Pilih Sumber Suara Carrier (.WAV 16-Bit PCM)",
                                style = MaterialTheme.typography.labelLarge,
                                fontWeight = FontWeight.ExtraBold,
                                color = VaultObsidian
                            )

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                DuoTactileChip(
                                    selected = !useVaultAudioCarrier,
                                    label = "Synthesizer WAV (4 Tema)",
                                    icon = Icons.Default.MusicNote,
                                    selectedFaceColor = DuoFoxOrange,
                                    selectedLipColor = DuoFoxOrangeLip,
                                    onClick = { useVaultAudioCarrier = false },
                                    modifier = Modifier
                                        .weight(1f)
                                        .testTag("wav_carrier_source_synth_chip")
                                )

                                DuoTactileChip(
                                    selected = useVaultAudioCarrier,
                                    label = "Audio Brankas (${vaultAudioCarriers.size})",
                                    icon = Icons.Default.AudioFile,
                                    selectedFaceColor = DuoMacawBlue,
                                    selectedLipColor = DuoMacawBlueLip,
                                    onClick = { useVaultAudioCarrier = true },
                                    modifier = Modifier
                                        .weight(1f)
                                        .testTag("wav_carrier_source_vault_chip")
                                )
                            }

                            if (useVaultAudioCarrier && vaultAudioCarriers.isNotEmpty()) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .horizontalScroll(rememberScrollState()),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    vaultAudioCarriers.forEach { audioItem ->
                                        val chosen = selectedCarrierAudio?.id == audioItem.id
                                        DuoTactileChip(
                                            selected = chosen,
                                            label = "${audioItem.name}.${audioItem.extension} (${formatStegoBytes(audioItem.sizeBytes)})",
                                            icon = Icons.Default.AudioFile,
                                            selectedFaceColor = DuoMacawBlue,
                                            selectedLipColor = DuoMacawBlueLip,
                                            onClick = {
                                                selectedCarrierAudioId = audioItem.id
                                                wavFileNameInput = "${audioItem.name}_LSB"
                                            },
                                            modifier = Modifier.testTag("select_vault_audio_carrier_${audioItem.id}")
                                        )
                                    }
                                }
                            } else {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .horizontalScroll(rememberScrollState()),
                                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                                ) {
                                    WavCamouflagePreset.entries.forEach { preset ->
                                        val isPicked = selectedWavPreset == preset
                                        val accentColor = Color(preset.accentColorHex)
                                        Box(
                                            modifier = Modifier
                                                .width(168.dp)
                                                .duolingoCard(
                                                    backgroundColor = if (isPicked) Color(0xFF0F172A) else DuoPureWhite,
                                                    borderColor = if (isPicked) accentColor else DuoSwanBorder,
                                                    lipColor = if (isPicked) accentColor else DuoHareLip,
                                                    cornerRadius = 18.dp,
                                                    borderWidth = if (isPicked) 2.5.dp else 2.dp,
                                                    lipHeight = 5.dp
                                                )
                                                .instantTouchPress {
                                                    selectedWavPreset = preset
                                                    wavFileNameInput = when (preset) {
                                                        WavCamouflagePreset.RAINY_NIGHT_AMBIENCE -> "Rekaman_Hujan_Malam_LSB"
                                                        WavCamouflagePreset.LOFI_WARM_CHORDS -> "Akor_LoFi_Santai_LSB"
                                                        WavCamouflagePreset.COZY_CAFE_HUM -> "Ambience_Kafe_Hangat_LSB"
                                                        WavCamouflagePreset.ANALOG_VOICE_MEMO -> "Memo_Suara_Analog_LSB"
                                                    }
                                                }
                                                .padding(bottom = 5.dp)
                                                .padding(12.dp)
                                                .testTag("wav_preset_card_${preset.name}")
                                        ) {
                                            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                                Text(
                                                    text = preset.badgeLabel,
                                                    fontFamily = JetBrainsMonoFontFamily,
                                                    fontSize = 9.sp,
                                                    fontWeight = FontWeight.ExtraBold,
                                                    color = accentColor,
                                                    maxLines = 1
                                                )
                                                Text(
                                                    text = preset.title,
                                                    style = MaterialTheme.typography.labelLarge,
                                                    fontWeight = FontWeight.ExtraBold,
                                                    color = if (isPicked) Color.White else VaultObsidian,
                                                    maxLines = 1,
                                                    overflow = TextOverflow.Ellipsis
                                                )
                                                Text(
                                                    text = preset.subtitle,
                                                    style = MaterialTheme.typography.bodySmall,
                                                    color = if (isPicked) Color.White.copy(alpha = 0.82f) else VaultTextMuted,
                                                    maxLines = 2,
                                                    overflow = TextOverflow.Ellipsis
                                                )
                                            }
                                        }
                                    }
                                }
                            }

                            // =========================================================================
                            // 2. AUDIO WAVEFORM & LSB BIT-SCOPE INSPECTOR + BUILT-IN AUDIO PLAYER
                            // =========================================================================
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .duolingoCard(
                                        backgroundColor = VaultCream,
                                        borderColor = DuoFeatherGreen,
                                        lipColor = DuoFeatherGreenLip,
                                        cornerRadius = 22.dp,
                                        borderWidth = 2.dp,
                                        lipHeight = 5.dp
                                    )
                                    .padding(bottom = 5.dp)
                                    .testTag("wav_waveform_bitscope_card")
                            ) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(14.dp),
                                    verticalArrangement = Arrangement.spacedBy(10.dp)
                                ) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                                            modifier = Modifier.weight(1f)
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.GraphicEq,
                                                contentDescription = null,
                                                tint = DuoFeatherGreenLip,
                                                modifier = Modifier.size(18.dp)
                                            )
                                            Text(
                                                text = "2. Waveform & LSB Bit-Scope + Player",
                                                style = MaterialTheme.typography.labelLarge,
                                                fontWeight = FontWeight.ExtraBold,
                                                color = VaultObsidian,
                                                maxLines = 1,
                                                overflow = TextOverflow.Ellipsis
                                            )
                                        }
                                        Box(
                                            modifier = Modifier
                                                .duolingoButton(
                                                    faceColor = DuoFeatherGreen,
                                                    lipColor = DuoFeatherGreenLip,
                                                    cornerRadius = 50.dp,
                                                    lipHeight = 3.dp
                                                )
                                                .padding(horizontal = 10.dp, vertical = 4.dp)
                                        ) {
                                            Text(
                                                text = "SNR ${liveWavAnalysis.snrDb} dB • ${liveWavAnalysis.stealthScorePercent}%",
                                                style = MaterialTheme.typography.labelSmall,
                                                fontWeight = FontWeight.ExtraBold,
                                                color = Color.White,
                                                maxLines = 1
                                            )
                                        }
                                    }

                                    // Mode switcher: Gelombang Audio | LSB Bit-Scope 16-Bit
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        DuoTactileChip(
                                            selected = wavComparisonMode == 0,
                                            label = "Side-by-Side Gelombang",
                                            icon = Icons.Default.GraphicEq,
                                            selectedFaceColor = DuoObsidianChipColor,
                                            selectedLipColor = DuoObsidianLipColor,
                                            selectedTextColor = VaultLime,
                                            onClick = { wavComparisonMode = 0 },
                                            modifier = Modifier
                                                .weight(1f)
                                                .testTag("wav_comp_mode_waveform")
                                        )
                                        DuoTactileChip(
                                            selected = wavComparisonMode == 1,
                                            label = "LSB Bit-Scope 16-Bit",
                                            icon = Icons.Default.Search,
                                            selectedFaceColor = DuoObsidianChipColor,
                                            selectedLipColor = DuoObsidianLipColor,
                                            selectedTextColor = VaultLime,
                                            onClick = { wavComparisonMode = 1 },
                                            modifier = Modifier
                                                .weight(1f)
                                                .testTag("wav_comp_mode_bitscope")
                                        )
                                    }

                                    // SIDE-BY-SIDE WAVEFORM & DIRECT PLAYBACK CARDS
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .testTag("wav_side_by_side_comparison"),
                                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                                    ) {
                                        // LEFT: AUDIO ASLI (SEBELUM)
                                        Box(
                                            modifier = Modifier
                                                .weight(1f)
                                                .duolingoCard(
                                                    backgroundColor = DuoPureWhite,
                                                    borderColor = DuoSwanBorder,
                                                    lipColor = DuoHareLip,
                                                    cornerRadius = 18.dp,
                                                    borderWidth = 2.dp,
                                                    lipHeight = 4.dp
                                                )
                                                .padding(bottom = 4.dp)
                                        ) {
                                            Column(
                                                modifier = Modifier.padding(10.dp),
                                                verticalArrangement = Arrangement.spacedBy(6.dp)
                                            ) {
                                                Row(
                                                    modifier = Modifier.fillMaxWidth(),
                                                    horizontalArrangement = Arrangement.SpaceBetween
                                                ) {
                                                    Text(
                                                        text = "AUDIO ASLI",
                                                        fontFamily = JetBrainsMonoFontFamily,
                                                        fontSize = 9.sp,
                                                        fontWeight = FontWeight.ExtraBold,
                                                        color = VaultTextMuted
                                                    )
                                                    Text(
                                                        text = formatStegoBytes(liveWavAnalysis.carrierBaseBytes),
                                                        fontFamily = JetBrainsMonoFontFamily,
                                                        fontSize = 9.sp,
                                                        fontWeight = FontWeight.Bold,
                                                        color = VaultObsidian
                                                    )
                                                }

                                                AudioWaveformCanvas(
                                                    amplitudes = wavBitScopeSample.originalAmplitudes,
                                                    barColor = DuoMacawBlue,
                                                    highlightLsbFloor = false,
                                                    modifier = Modifier
                                                        .fillMaxWidth()
                                                        .height(64.dp)
                                                )

                                                DuoTactileActionButton(
                                                    text = if (activePlayingPreviewMode == "ORIG") "Stop Suara" else "Putar Asli",
                                                    icon = if (activePlayingPreviewMode == "ORIG") Icons.Default.Stop else Icons.Default.PlayArrow,
                                                    faceColor = if (activePlayingPreviewMode == "ORIG") DuoCardinalRed else DuoMacawBlue,
                                                    lipColor = if (activePlayingPreviewMode == "ORIG") DuoCardinalRedLip else DuoMacawBlueLip,
                                                    height = 38.dp,
                                                    onClick = {
                                                        if (activePlayingPreviewMode == "ORIG") {
                                                            WavAudioSteganographyEngine.stopPreviewAudio()
                                                            activePlayingPreviewMode = null
                                                        } else {
                                                            val f = selectedCarrierAudio?.internalPlainPath?.let { File(it) }
                                                            WavAudioSteganographyEngine.playPreviewAudio(
                                                                preset = selectedWavPreset,
                                                                existingWavFile = f,
                                                                isStegoModifiedPreview = false,
                                                                secretSeedText = wavSecretMessageInput
                                                            )
                                                            activePlayingPreviewMode = "ORIG"
                                                        }
                                                    },
                                                    modifier = Modifier
                                                        .fillMaxWidth()
                                                        .testTag("wav_play_original_button")
                                                )
                                            }
                                        }

                                        // RIGHT: AUDIO HASIL STEGO LSB (SESUDAH)
                                        Box(
                                            modifier = Modifier
                                                .weight(1f)
                                                .duolingoCard(
                                                    backgroundColor = DuoPureWhite,
                                                    borderColor = DuoFoxOrange,
                                                    lipColor = DuoFoxOrangeLip,
                                                    cornerRadius = 18.dp,
                                                    borderWidth = 2.dp,
                                                    lipHeight = 4.dp
                                                )
                                                .padding(bottom = 4.dp)
                                        ) {
                                            Column(
                                                modifier = Modifier.padding(10.dp),
                                                verticalArrangement = Arrangement.spacedBy(6.dp)
                                            ) {
                                                Row(
                                                    modifier = Modifier.fillMaxWidth(),
                                                    horizontalArrangement = Arrangement.SpaceBetween
                                                ) {
                                                    Text(
                                                        text = "STEGO LSB",
                                                        fontFamily = JetBrainsMonoFontFamily,
                                                        fontSize = 9.sp,
                                                        fontWeight = FontWeight.ExtraBold,
                                                        color = DuoFoxOrangeLip
                                                    )
                                                    Text(
                                                        text = formatStegoBytes(liveWavAnalysis.estimatedTotalBytes),
                                                        fontFamily = JetBrainsMonoFontFamily,
                                                        fontSize = 9.sp,
                                                        fontWeight = FontWeight.Bold,
                                                        color = DuoFoxOrangeLip
                                                    )
                                                }

                                                AudioWaveformCanvas(
                                                    amplitudes = wavBitScopeSample.stegoAmplitudes,
                                                    barColor = DuoFoxOrange,
                                                    highlightLsbFloor = true,
                                                    modifier = Modifier
                                                        .fillMaxWidth()
                                                        .height(64.dp)
                                                )

                                                DuoTactileActionButton(
                                                    text = if (activePlayingPreviewMode == "STEGO") "Stop Stego" else "Putar Stego",
                                                    icon = if (activePlayingPreviewMode == "STEGO") Icons.Default.Stop else Icons.Default.PlayArrow,
                                                    faceColor = if (activePlayingPreviewMode == "STEGO") DuoCardinalRed else DuoFoxOrange,
                                                    lipColor = if (activePlayingPreviewMode == "STEGO") DuoCardinalRedLip else DuoFoxOrangeLip,
                                                    height = 38.dp,
                                                    onClick = {
                                                        if (activePlayingPreviewMode == "STEGO") {
                                                            WavAudioSteganographyEngine.stopPreviewAudio()
                                                            activePlayingPreviewMode = null
                                                        } else {
                                                            val f = selectedCarrierAudio?.internalPlainPath?.let { File(it) }
                                                            WavAudioSteganographyEngine.playPreviewAudio(
                                                                preset = selectedWavPreset,
                                                                existingWavFile = f,
                                                                isStegoModifiedPreview = true,
                                                                secretSeedText = wavSecretMessageInput
                                                            )
                                                            activePlayingPreviewMode = "STEGO"
                                                        }
                                                    },
                                                    modifier = Modifier
                                                        .fillMaxWidth()
                                                        .testTag("wav_play_stego_button")
                                                )
                                            }
                                        }
                                    }

                                    // 16-BIT PCM LSB BIT-SCOPE INSPECTOR PANEL
                                    if (wavComparisonMode == 1) {
                                        Box(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .duolingoCard(
                                                    backgroundColor = Color(0xFF0F172A),
                                                    borderColor = DuoFeatherGreen,
                                                    lipColor = DuoObsidianLipColor,
                                                    cornerRadius = 16.dp,
                                                    borderWidth = 2.dp,
                                                    lipHeight = 4.dp
                                                )
                                                .padding(bottom = 4.dp)
                                                .testTag("wav_lsb_bitscope_inspector_panel")
                                        ) {
                                            Column(
                                                modifier = Modifier.padding(12.dp),
                                                verticalArrangement = Arrangement.spacedBy(8.dp)
                                            ) {
                                                Row(
                                                    modifier = Modifier.fillMaxWidth(),
                                                    horizontalArrangement = Arrangement.SpaceBetween,
                                                    verticalAlignment = Alignment.CenterVertically
                                                ) {
                                                    Text(
                                                        text = "16-BIT PCM LSB BIT-SCOPE (#${wavBitScopeSample.sampleIndex} @ ${wavBitScopeSample.timeOffsetLabel})",
                                                        fontFamily = JetBrainsMonoFontFamily,
                                                        fontSize = 10.sp,
                                                        fontWeight = FontWeight.ExtraBold,
                                                        color = VaultLime
                                                    )
                                                    Text(
                                                        text = "ΔNoise = 0.00 dB",
                                                        fontFamily = JetBrainsMonoFontFamily,
                                                        fontSize = 10.sp,
                                                        fontWeight = FontWeight.ExtraBold,
                                                        color = Color(0xFF38BDF8)
                                                    )
                                                }

                                                Row(
                                                    modifier = Modifier.fillMaxWidth(),
                                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                                ) {
                                                    val timeHotspots = listOf(
                                                        0 to "00:00.4s Header",
                                                        1 to "00:01.8s Body",
                                                        2 to "00:03.2s Ekor"
                                                    )
                                                    timeHotspots.forEach { (hIdx, hLabel) ->
                                                        DuoTactileChip(
                                                            selected = selectedWavHotspot == hIdx,
                                                            label = hLabel,
                                                            selectedFaceColor = DuoFoxOrange,
                                                            selectedLipColor = DuoFoxOrangeLip,
                                                            onClick = { selectedWavHotspot = hIdx },
                                                            modifier = Modifier.weight(1f)
                                                        )
                                                    }
                                                }

                                                Text(
                                                    text = "PCM Asli  (${wavBitScopeSample.originalPcmHex}): ${wavBitScopeSample.originalBinary16}",
                                                    fontFamily = JetBrainsMonoFontFamily,
                                                    fontSize = 10.sp,
                                                    color = Color.White.copy(alpha = 0.9f),
                                                    maxLines = 1
                                                )
                                                Text(
                                                    text = "PCM Stego (${wavBitScopeSample.stegoPcmHex}): ${wavBitScopeSample.stegoBinary16}",
                                                    fontFamily = JetBrainsMonoFontFamily,
                                                    fontSize = 10.sp,
                                                    fontWeight = FontWeight.ExtraBold,
                                                    color = VaultLime,
                                                    maxLines = 1
                                                )
                                                Text(
                                                    text = wavBitScopeSample.lsbDeltaDescription,
                                                    fontFamily = JetBrainsMonoFontFamily,
                                                    fontSize = 9.sp,
                                                    color = Color(0xFF38BDF8)
                                                )
                                            }
                                        }
                                    }

                                    Text(
                                        text = "${liveWavAnalysis.statusLabel} • Kapasitas Sampel LSB Terpakai: ${liveWavAnalysis.lsbSampleUsagePercent}%",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = VaultObsidian,
                                        fontWeight = FontWeight.Medium
                                    )
                                }
                            }

                            // =========================================================================
                            // 3. SEMBUNYIKAN FOTO ATAU FILE DI DALAM AUDIO WAV (PHOTO-IN-AUDIO FUSION)
                            // =========================================================================
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .duolingoCard(
                                        backgroundColor = DuoPureWhite,
                                        borderColor = DuoBeetlePurple,
                                        lipColor = DuoBeetlePurpleLip,
                                        cornerRadius = 22.dp,
                                        borderWidth = 2.dp,
                                        lipHeight = 5.dp
                                    )
                                    .padding(bottom = 5.dp)
                                    .testTag("photo_in_wav_audio_fusion_card")
                            ) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(14.dp),
                                    verticalArrangement = Arrangement.spacedBy(10.dp)
                                ) {
                                    Text(
                                        text = "3. Sembunyikan Foto / File di Dalam Suara .WAV",
                                        style = MaterialTheme.typography.labelLarge,
                                        fontWeight = FontWeight.ExtraBold,
                                        color = VaultObsidian
                                    )

                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        DuoTactileChip(
                                            selected = isPhotoInWavMode,
                                            label = "Foto di Dalam Audio",
                                            icon = Icons.Default.PhotoLibrary,
                                            selectedFaceColor = DuoBeetlePurple,
                                            selectedLipColor = DuoBeetlePurpleLip,
                                            onClick = { isPhotoInWavMode = true },
                                            modifier = Modifier
                                                .weight(1f)
                                                .testTag("toggle_photo_in_wav_chip")
                                        )
                                        DuoTactileChip(
                                            selected = !isPhotoInWavMode,
                                            label = "Hanya Teks / File Lain",
                                            icon = Icons.Default.AttachFile,
                                            selectedFaceColor = DuoMacawBlue,
                                            selectedLipColor = DuoMacawBlueLip,
                                            onClick = { isPhotoInWavMode = false },
                                            modifier = Modifier
                                                .weight(1f)
                                                .testTag("toggle_text_only_wav_chip")
                                        )
                                    }

                                    if (isPhotoInWavMode && vaultImageCarriers.isNotEmpty()) {
                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .horizontalScroll(rememberScrollState()),
                                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                                        ) {
                                            vaultImageCarriers.forEach { img ->
                                                DuoTactileChip(
                                                    selected = selectedPhotoForWavId == img.id,
                                                    label = "Foto: ${img.name}.${img.extension}",
                                                    icon = Icons.Default.Image,
                                                    selectedFaceColor = DuoBeetlePurple,
                                                    selectedLipColor = DuoBeetlePurpleLip,
                                                    onClick = { selectedPhotoForWavId = img.id },
                                                    modifier = Modifier.testTag("select_photo_for_wav_${img.id}")
                                                )
                                            }
                                        }

                                        if (wavHiddenPhotoBitmap != null) {
                                            Box(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .duolingoCard(
                                                        backgroundColor = VaultCream,
                                                        borderColor = DuoSwanBorder,
                                                        lipColor = DuoHareLip,
                                                        cornerRadius = 16.dp,
                                                        borderWidth = 2.dp,
                                                        lipHeight = 4.dp
                                                    )
                                                    .padding(bottom = 4.dp)
                                            ) {
                                                Row(
                                                    modifier = Modifier
                                                        .fillMaxWidth()
                                                        .padding(10.dp),
                                                    verticalAlignment = Alignment.CenterVertically,
                                                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                                                ) {
                                                    Box(
                                                        modifier = Modifier
                                                            .size(46.dp)
                                                            .duolingoButton(
                                                                faceColor = DuoFoxOrange,
                                                                lipColor = DuoFoxOrangeLip,
                                                                cornerRadius = 12.dp,
                                                                lipHeight = 3.dp
                                                            )
                                                            .padding(bottom = 3.dp),
                                                        contentAlignment = Alignment.Center
                                                    ) {
                                                        Icon(
                                                            imageVector = Icons.Default.GraphicEq,
                                                            contentDescription = null,
                                                            tint = Color.White
                                                        )
                                                    }
                                                    Text(
                                                        text = "+",
                                                        style = MaterialTheme.typography.titleMedium,
                                                        fontWeight = FontWeight.ExtraBold,
                                                        color = DuoBeetlePurpleLip
                                                    )
                                                    if (!wavHiddenPhotoBitmap.isRecycled) {
                                                        Image(
                                                            bitmap = remember(wavHiddenPhotoBitmap) {
                                                                wavHiddenPhotoBitmap.asImageBitmap()
                                                            },
                                                            contentDescription = "Foto Rahasia di Dalam Audio WAV",
                                                            contentScale = ContentScale.Crop,
                                                            modifier = Modifier
                                                                .size(46.dp)
                                                                .clip(RoundedCornerShape(10.dp))
                                                        )
                                                    }
                                                    Column(modifier = Modifier.weight(1f)) {
                                                        Text(
                                                            text = "Foto Disembunyikan di Gelombang .WAV",
                                                            style = MaterialTheme.typography.labelMedium,
                                                            fontWeight = FontWeight.ExtraBold,
                                                            color = VaultObsidian,
                                                            maxLines = 1,
                                                            overflow = TextOverflow.Ellipsis
                                                        )
                                                        Text(
                                                            text = "Suara: ${selectedCarrierAudio?.name ?: selectedWavPreset.title} • Foto: ${wavHiddenPhotoEntity?.name ?: "-"}",
                                                            style = MaterialTheme.typography.bodySmall,
                                                            color = DuoBeetlePurpleLip,
                                                            maxLines = 1,
                                                            overflow = TextOverflow.Ellipsis
                                                        )
                                                    }
                                                }
                                            }
                                        }
                                    } else if (!isPhotoInWavMode && attachableItems.isNotEmpty()) {
                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .horizontalScroll(rememberScrollState()),
                                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                                        ) {
                                            DuoTactileChip(
                                                selected = selectedFileForWavId == null,
                                                label = "Hanya Pesan Teks LSB",
                                                selectedFaceColor = DuoMacawBlue,
                                                selectedLipColor = DuoMacawBlueLip,
                                                onClick = { selectedFileForWavId = null }
                                            )
                                            attachableItems.forEach { candidate ->
                                                DuoTactileChip(
                                                    selected = selectedFileForWavId == candidate.id,
                                                    label = "${candidate.name}.${candidate.extension}",
                                                    icon = Icons.Default.AttachFile,
                                                    selectedFaceColor = DuoMacawBlue,
                                                    selectedLipColor = DuoMacawBlueLip,
                                                    onClick = { selectedFileForWavId = candidate.id }
                                                )
                                            }
                                        }
                                    }
                                }
                            }

                            OutlinedTextField(
                                value = wavFileNameInput,
                                onValueChange = { wavFileNameInput = it },
                                label = { Text("Nama File Audio .WAV di Brankas") },
                                singleLine = true,
                                colors = duoTextFieldColors(),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("stego_wav_filename_input"),
                                shape = RoundedCornerShape(16.dp)
                            )

                            OutlinedTextField(
                                value = wavSecretTitleInput,
                                onValueChange = { wavSecretTitleInput = it },
                                label = { Text("Judul Pesan Rahasia (Terenkripsi di LSB WAV)") },
                                singleLine = true,
                                colors = duoTextFieldColors(),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("stego_wav_secret_title_input"),
                                shape = RoundedCornerShape(16.dp)
                            )

                            OutlinedTextField(
                                value = wavSecretMessageInput,
                                onValueChange = { wavSecretMessageInput = it },
                                label = { Text("Isi Pesan Teks Rahasia di Dalam Gelombang Audio") },
                                minLines = 2,
                                maxLines = 4,
                                colors = duoTextFieldColors(),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("stego_wav_secret_message_input"),
                                shape = RoundedCornerShape(16.dp)
                            )

                            OutlinedTextField(
                                value = wavPasscodeInput,
                                onValueChange = { wavPasscodeInput = it.uppercase().take(12) },
                                label = { Text("Sandi Buka Audio Stego (Opsional)") },
                                singleLine = true,
                                colors = duoTextFieldColors(),
                                leadingIcon = {
                                    Icon(Icons.Default.Key, contentDescription = null, tint = DuoFoxOrange)
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("stego_wav_passcode_input"),
                                shape = RoundedCornerShape(16.dp)
                            )

                            // CORE RAW TEXT LSB (1-BIT -90.3 dB vs 2-BIT -84.3 dB) VERIFICATION BOX
                            var rawLsbBitsMode by remember { mutableIntStateOf(1) }
                            val liveRawLsbRoundtrip = remember(wavSecretMessageInput, rawLsbBitsMode) {
                                val sampleCarrier = WavAudioSteganographyEngine.synthesizeCamouflageWavBytes(
                                    preset = selectedWavPreset,
                                    durationSeconds = 1,
                                    minRequiredSamples = 4096
                                )
                                val embeddedWav = WavAudioSteganographyEngine.embedRawTextIntoWavLsb(
                                    wavBytes = sampleCarrier,
                                    rawText = wavSecretMessageInput.ifBlank { "RAW_LSB_OK" },
                                    bitsPerSample = rawLsbBitsMode
                                )
                                val extracted = WavAudioSteganographyEngine.extractRawTextFromWavLsb(embeddedWav).orEmpty()
                                extracted
                            }

                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .duolingoCard(
                                        backgroundColor = Color(0xFF0F172A),
                                        borderColor = DuoMacawBlue,
                                        lipColor = DuoObsidianLipColor,
                                        cornerRadius = 18.dp,
                                        borderWidth = 2.dp,
                                        lipHeight = 4.dp
                                    )
                                    .padding(bottom = 4.dp)
                                    .testTag("wav_raw_text_lsb_engine_card")
                            ) {
                                Column(
                                    modifier = Modifier.padding(12.dp),
                                    verticalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Text(
                                        text = "CORE RAW TEXT LSB ENGINE (DEGRADASI SUARA MINIMAL):",
                                        fontFamily = JetBrainsMonoFontFamily,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.ExtraBold,
                                        color = DuoBeeYellow
                                    )
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        DuoTactileChip(
                                            selected = rawLsbBitsMode == 1,
                                            label = "1-Bit LSB (-90.3 dB Ultra-Stealth)",
                                            selectedFaceColor = DuoFeatherGreen,
                                            selectedLipColor = DuoFeatherGreenLip,
                                            onClick = { rawLsbBitsMode = 1 },
                                            modifier = Modifier
                                                .weight(1f)
                                                .testTag("wav_raw_lsb_1bit_chip")
                                        )
                                        DuoTactileChip(
                                            selected = rawLsbBitsMode == 2,
                                            label = "2-Bit LSB (-84.3 dB High-Cap)",
                                            selectedFaceColor = DuoFoxOrange,
                                            selectedLipColor = DuoFoxOrangeLip,
                                            onClick = { rawLsbBitsMode = 2 },
                                            modifier = Modifier
                                                .weight(1f)
                                                .testTag("wav_raw_lsb_2bit_chip")
                                        )
                                    }
                                    Text(
                                        text = "Verifikasi Ekstraksi Langsung PCM LSB (${if (rawLsbBitsMode == 1) "8 sampel/byte • ΔAmp ±1/32768" else "4 sampel/byte • ΔAmp ±3/32768"}): \"${liveRawLsbRoundtrip.take(56)}\"",
                                        fontFamily = JetBrainsMonoFontFamily,
                                        fontSize = 9.sp,
                                        color = VaultLime
                                    )
                                }
                            }

                            DuoTactileActionButton(
                                text = if (isPhotoInWavMode && effectiveWavAttachedItem != null) {
                                    "Sembunyikan Teks & Foto ke Audio .WAV (LSB)"
                                } else {
                                    "Sembunyikan Pesan ke Audio .WAV (LSB)"
                                },
                                icon = Icons.Default.GraphicEq,
                                faceColor = DuoFeatherGreen,
                                lipColor = DuoFeatherGreenLip,
                                height = 54.dp,
                                onClick = {
                                    WavAudioSteganographyEngine.stopPreviewAudio()
                                    activePlayingPreviewMode = null
                                    onCreateStegoWavClick(
                                        wavFileNameInput,
                                        selectedWavPreset,
                                        wavSecretTitleInput,
                                        wavSecretMessageInput,
                                        wavPasscodeInput,
                                        effectiveWavAttachedItem,
                                        selectedCarrierAudio
                                    )
                                    activeModeTab = 1
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("create_stego_wav_submit_button")
                            )
                        }
                    }
                }
            }

            6 -> {
                // =========================================================================
                // MODE 6: FITUR MORSE (TEXT <-> MORSE + CW AUDIO SYNTHESIZER + WAV LSB)
                // =========================================================================
                item {
                    MorseCodeTransceiverSection(
                        displayVaultNum = displayVaultNum,
                        onSaveMorseStegoWavToVault = { wavName, title, msg, pass ->
                            onCreateStegoWavClick(
                                wavName,
                                WavCamouflagePreset.ANALOG_VOICE_MEMO,
                                title,
                                msg,
                                pass,
                                null,
                                null
                            )
                            activeModeTab = 1
                        }
                    )
                }
            }

            7 -> {
                // =========================================================================
                // MODE 7: ENIGMA FULL CUSTOM (8 ROTOR, RINGSTELLUNG, UKW, STECKERBRETT)
                // =========================================================================
                item {
                    EnigmaFullCustomSection()
                }
            }

            8 -> {
                // =========================================================================
                // MODE 8: 2 ALAT ENKRIPSI & DEKRIPSI TERKOMPLEKS (30 SANDI HURUF & ANGKA)
                // =========================================================================
                item {
                    Dual30CharComplexCiphersSection()
                }
            }

            1 -> {
                // =========================================================================
                // MODE 1: BONGKAR STEGO-PNG & STEGO-WAV LSB (SEMUA KARTU & TOMBOL 3D DUOLINGO)
                // =========================================================================
                stegoState.decodedPayload?.let { payload ->
                    item {
                        DecodedStegoResultCard(
                            item = stegoState.selectedDecodingItem,
                            payload = payload,
                            decodePasscodeInput = decodePasscodeInput,
                            onDecodePasscodeChange = { decodePasscodeInput = it.uppercase() },
                            onRetryUnlockWithPasscode = {
                                stegoState.selectedDecodingItem?.let { target ->
                                    onDecodeStegoItemClick(target, decodePasscodeInput)
                                }
                            },
                            onRestoreEmbeddedFile = { onRestoreEmbeddedFileClick(payload) },
                            onClose = {
                                WavAudioSteganographyEngine.stopPreviewAudio()
                                onClearDecodedResult()
                            }
                        )
                    }
                }

                if (stegoCarrierItems.isEmpty()) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .duolingoCard(
                                    backgroundColor = DuoPureWhite,
                                    borderColor = DuoMacawBlue,
                                    lipColor = DuoMacawBlueLip,
                                    cornerRadius = 24.dp,
                                    borderWidth = 2.dp,
                                    lipHeight = 5.dp
                                )
                                .padding(bottom = 5.dp)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(22.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Image,
                                    contentDescription = null,
                                    tint = DuoMacawBlue,
                                    modifier = Modifier.size(42.dp)
                                )
                                Text(
                                    text = "Belum Ada Media Stego (.PNG / .WAV) di Brankas #$displayVaultNum",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = VaultObsidian
                                )
                                Text(
                                    text = "Buat gambar Stego-PNG atau file suara Stego-WAV LSB terlebih dahulu untuk membongkar rahasianya.",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = VaultTextMuted
                                )
                                DuoTactileActionButton(
                                    text = "Buat Contoh Stego Sekarang",
                                    icon = Icons.Default.AutoAwesome,
                                    faceColor = DuoMacawBlue,
                                    lipColor = DuoMacawBlueLip,
                                    onClick = {
                                        onCreateStegoPngClick(
                                            "Kartu_Pos_Aurora_Rahasia",
                                            StegoCoverTheme.COSMIC_POSTCARD,
                                            "Koordinat & Sandi Cadangan",
                                            "Pesan ini tersembunyi di dalam file PNG! Orang luar hanya melihat gambar kartu pos biasa.",
                                            "",
                                            secondPhotoCandidates.firstOrNull() ?: attachableItems.firstOrNull(),
                                            vaultImageCarriers.firstOrNull(),
                                            false
                                        )
                                    }
                                )
                            }
                        }
                    }
                } else {
                    items(stegoCarrierItems, key = { it.id }) { carrierItem ->
                        val isWavItem = WavAudioSteganographyEngine.isWavExtension(carrierItem.extension) ||
                            carrierItem.rawContentPreview.startsWith("WAV_STEGO_CARRIER::")
                        val isStegoMarked = carrierItem.rawContentPreview.startsWith("PNG_STEGO_CARRIER::") ||
                            carrierItem.rawContentPreview.startsWith("WAV_STEGO_CARRIER::")
                        val isQrMarked = carrierItem.rawContentPreview.contains("::QR_CARD::")
                        val isDualPhotoMarked = carrierItem.rawContentPreview.contains("::DUAL_PHOTO::")
                        val isPhotoInAudioMarked = carrierItem.rawContentPreview.contains("::PHOTO_IN_AUDIO::")
                        val isPhotoMarked = carrierItem.rawContentPreview.contains("::PHOTO::")

                        val cardBorderColor = when {
                            isWavItem -> DuoFoxOrange
                            isStegoMarked -> DuoBeetlePurple
                            else -> DuoSwanBorder
                        }
                        val cardLipColor = when {
                            isWavItem -> DuoFoxOrangeLip
                            isStegoMarked -> DuoBeetlePurpleLip
                            else -> DuoHareLip
                        }

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .duolingoCard(
                                    backgroundColor = DuoPureWhite,
                                    borderColor = cardBorderColor,
                                    lipColor = cardLipColor,
                                    cornerRadius = 22.dp,
                                    borderWidth = 2.dp,
                                    lipHeight = 5.dp
                                )
                                .padding(bottom = 5.dp)
                                .testTag("stego_png_item_${carrierItem.name}")
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(14.dp),
                                verticalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(48.dp)
                                            .duolingoButton(
                                                faceColor = if (isWavItem) DuoFoxOrange else DuoBeetlePurple,
                                                lipColor = if (isWavItem) DuoFoxOrangeLip else DuoBeetlePurpleLip,
                                                cornerRadius = 14.dp,
                                                lipHeight = 3.dp
                                            )
                                            .padding(bottom = 3.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            imageVector = when {
                                                isWavItem -> Icons.Default.GraphicEq
                                                isDualPhotoMarked -> Icons.Default.Layers
                                                isQrMarked -> Icons.Default.QrCode2
                                                else -> Icons.Default.Image
                                            },
                                            contentDescription = null,
                                            tint = Color.White
                                        )
                                    }
                                    Column(modifier = Modifier.weight(1f)) {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                                        ) {
                                            Text(
                                                text = "${carrierItem.name}.${carrierItem.extension}",
                                                style = MaterialTheme.typography.titleMedium,
                                                fontWeight = FontWeight.ExtraBold,
                                                color = VaultObsidian,
                                                maxLines = 1,
                                                overflow = TextOverflow.Ellipsis,
                                                modifier = Modifier.weight(1f, fill = false)
                                            )
                                            if (isWavItem) {
                                                Box(
                                                    modifier = Modifier
                                                        .duolingoButton(
                                                            faceColor = DuoFoxOrange,
                                                            lipColor = DuoFoxOrangeLip,
                                                            cornerRadius = 50.dp,
                                                            lipHeight = 2.dp
                                                        )
                                                        .padding(horizontal = 7.dp, vertical = 2.dp)
                                                ) {
                                                    Text(
                                                        text = if (isPhotoInAudioMarked) "FOTO DI AUDIO" else "AUDIO WAV LSB",
                                                        fontFamily = JetBrainsMonoFontFamily,
                                                        fontSize = 8.sp,
                                                        fontWeight = FontWeight.ExtraBold,
                                                        color = Color.White,
                                                        maxLines = 1
                                                    )
                                                }
                                            } else if (isDualPhotoMarked) {
                                                Box(
                                                    modifier = Modifier
                                                        .duolingoButton(
                                                            faceColor = DuoBeetlePurple,
                                                            lipColor = DuoBeetlePurpleLip,
                                                            cornerRadius = 50.dp,
                                                            lipHeight = 2.dp
                                                        )
                                                        .padding(horizontal = 7.dp, vertical = 2.dp)
                                                ) {
                                                    Text(
                                                        text = "2 FOTO DI 1 FILE",
                                                        fontFamily = JetBrainsMonoFontFamily,
                                                        fontSize = 8.sp,
                                                        fontWeight = FontWeight.ExtraBold,
                                                        color = Color.White,
                                                        maxLines = 1
                                                    )
                                                }
                                            }
                                        }
                                        Text(
                                            text = when {
                                                isPhotoInAudioMarked -> "Stego Audio WAV LSB + Foto Rahasia • ${formatStegoBytes(carrierItem.sizeBytes)}"
                                                isWavItem -> "Gelombang Audio WAV LSB 16-Bit • ${formatStegoBytes(carrierItem.sizeBytes)}"
                                                isDualPhotoMarked -> "Dual-Photo Fusion (2 Foto dalam 1 File) • ${formatStegoBytes(carrierItem.sizeBytes)}"
                                                isQrMarked -> "Kartu Kunci Stego-QR Offline • ${formatStegoBytes(carrierItem.sizeBytes)}"
                                                isPhotoMarked -> "Foto Brankas Bersisipan Stego • ${formatStegoBytes(carrierItem.sizeBytes)}"
                                                isStegoMarked -> "Mengandung Muatan Steganografi NYX2 • ${formatStegoBytes(carrierItem.sizeBytes)}"
                                                else -> "Media Carrier • ${formatStegoBytes(carrierItem.sizeBytes)}"
                                            },
                                            style = MaterialTheme.typography.bodySmall,
                                            color = if (isWavItem) DuoFoxOrangeLip else if (isStegoMarked) DuoBeetlePurpleLip else VaultTextMuted,
                                            fontWeight = if (isStegoMarked || isWavItem) FontWeight.Bold else FontWeight.Normal,
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis
                                        )
                                    }
                                }

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    DuoTactileActionButton(
                                        text = when {
                                            isWavItem -> "Bongkar Audio WAV"
                                            isDualPhotoMarked -> "Buka 2 Foto & Rahasia"
                                            else -> "Bongkar Rahasia PNG"
                                        },
                                        icon = Icons.Default.LockOpen,
                                        faceColor = if (isWavItem) DuoFoxOrange else DuoBeetlePurple,
                                        lipColor = if (isWavItem) DuoFoxOrangeLip else DuoBeetlePurpleLip,
                                        height = 44.dp,
                                        onClick = { onDecodeStegoItemClick(carrierItem, decodePasscodeInput) },
                                        modifier = Modifier
                                            .weight(1.3f)
                                            .testTag("decode_stego_png_button_${carrierItem.name}")
                                    )

                                    DuoTactileActionButton(
                                        text = if (isWavItem) "Putar Audio" else "Lihat Gambar",
                                        icon = if (isWavItem) Icons.Default.PlayArrow else Icons.Default.Visibility,
                                        faceColor = DuoPureWhite,
                                        lipColor = DuoHareLip,
                                        textColor = VaultObsidian,
                                        height = 44.dp,
                                        onClick = { onOpenImageViewerClick(carrierItem) },
                                        modifier = Modifier.weight(1f)
                                    )
                                }
                            }
                        }
                    }
                }
            }

            2 -> {
                // =========================================================================
                // MODE 2: FEATURE #8 — PEMBERSIH METADATA GPS & EXIF FOTO (FULL 3D DUOLINGO)
                // =========================================================================
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .duolingoCard(
                                backgroundColor = DuoPureWhite,
                                borderColor = DuoFeatherGreen,
                                lipColor = DuoFeatherGreenLip,
                                cornerRadius = 24.dp,
                                borderWidth = 2.dp,
                                lipHeight = 5.dp
                            )
                            .padding(bottom = 5.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(18.dp),
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(44.dp)
                                        .duolingoButton(
                                            faceColor = DuoFeatherGreen,
                                            lipColor = DuoFeatherGreenLip,
                                            cornerRadius = 14.dp,
                                            lipHeight = 3.dp
                                        )
                                        .padding(bottom = 3.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.GpsOff,
                                        contentDescription = null,
                                        tint = Color.White,
                                        modifier = Modifier.size(24.dp)
                                    )
                                }
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = "Pembersih Metadata GPS & EXIF Foto (#8)",
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.ExtraBold,
                                        color = VaultObsidian
                                    )
                                    Text(
                                        text = "Hapus titik koordinat lokasi GPS, tipe kamera HP, lensa, dan jam pengambilan foto dari file di Brankas #$displayVaultNum.",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = VaultTextMuted
                                    )
                                }
                            }

                            val sanitizedCount = exifCandidateItems.count {
                                it.rawContentPreview.startsWith("EXIF_SANITIZED::") ||
                                    it.rawContentPreview.startsWith("EXIF_GPS_SANITIZED::")
                            }

                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .duolingoCard(
                                        backgroundColor = VaultCream,
                                        borderColor = DuoFeatherGreen,
                                        lipColor = DuoFeatherGreenLip,
                                        cornerRadius = 16.dp,
                                        borderWidth = 2.dp,
                                        lipHeight = 4.dp
                                    )
                                    .padding(bottom = 4.dp)
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(12.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "Status Sanitasi Brankas #$displayVaultNum",
                                        style = MaterialTheme.typography.labelLarge,
                                        fontWeight = FontWeight.Bold,
                                        color = VaultObsidian
                                    )
                                    Text(
                                        text = "$sanitizedCount / ${exifCandidateItems.size} Bersih EXIF",
                                        style = MaterialTheme.typography.labelLarge,
                                        fontWeight = FontWeight.ExtraBold,
                                        color = DuoFeatherGreenLip
                                    )
                                }
                            }

                            DuoTactileActionButton(
                                text = "Bersihkan Semua EXIF & GPS Sekaligus (1-Klik)",
                                icon = Icons.Default.CleaningServices,
                                faceColor = DuoFeatherGreen,
                                lipColor = DuoFeatherGreenLip,
                                height = 50.dp,
                                onClick = onStripAllExifInVault,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("strip_all_exif_vault_button")
                            )
                        }
                    }
                }

                items(exifCandidateItems, key = { "exif_${it.id}" }) { mediaItem ->
                    val isSanitized = mediaItem.rawContentPreview.startsWith("EXIF_SANITIZED::") ||
                        mediaItem.rawContentPreview.startsWith("EXIF_GPS_SANITIZED::")
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .duolingoCard(
                                backgroundColor = DuoPureWhite,
                                borderColor = if (isSanitized) DuoFeatherGreen else DuoFoxOrange,
                                lipColor = if (isSanitized) DuoFeatherGreenLip else DuoFoxOrangeLip,
                                cornerRadius = 20.dp,
                                borderWidth = 2.dp,
                                lipHeight = 4.dp
                            )
                            .padding(bottom = 4.dp)
                            .testTag("exif_item_card_${mediaItem.id}")
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(44.dp)
                                    .duolingoButton(
                                        faceColor = if (isSanitized) DuoFeatherGreen else DuoFoxOrange,
                                        lipColor = if (isSanitized) DuoFeatherGreenLip else DuoFoxOrangeLip,
                                        cornerRadius = 12.dp,
                                        lipHeight = 3.dp
                                    )
                                    .padding(bottom = 3.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = if (isSanitized) Icons.Default.CheckCircle else Icons.Default.GpsOff,
                                    contentDescription = null,
                                    tint = Color.White
                                )
                            }

                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "${mediaItem.name}.${mediaItem.extension}",
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = VaultObsidian,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Text(
                                    text = if (isSanitized) {
                                        "AMAN • GPS: 0.0000, 0.0000 • Kamera & Waktu Telah Dihapus"
                                    } else {
                                        "RAW EXIF • Berpotensi Menyimpan Tag GPS Lokasi & ID Perangkat"
                                    },
                                    style = MaterialTheme.typography.bodySmall,
                                    color = if (isSanitized) DuoFeatherGreenLip else DuoFoxOrangeLip,
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            DuoTactileActionButton(
                                text = if (isSanitized) "Bersih" else "Hapus EXIF",
                                faceColor = if (isSanitized) DuoFeatherGreen else DuoFoxOrange,
                                lipColor = if (isSanitized) DuoFeatherGreenLip else DuoFoxOrangeLip,
                                enabled = !isSanitized,
                                height = 40.dp,
                                onClick = { onStripExifForItem(mediaItem) },
                                modifier = Modifier.testTag("strip_exif_single_button_${mediaItem.id}")
                            )
                        }
                    }
                }
            }

            3 -> {
                // =========================================================================
                // MODE 3: FEATURE #9 — MODE KALKULATOR KAMUFLASE INSTAN 1-KLIK (3D DUOLINGO)
                // =========================================================================
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .duolingoCard(
                                backgroundColor = DuoPureWhite,
                                borderColor = DuoFoxOrange,
                                lipColor = DuoFoxOrangeLip,
                                cornerRadius = 26.dp,
                                borderWidth = 2.dp,
                                lipHeight = 5.dp
                            )
                            .padding(bottom = 5.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(20.dp),
                            verticalArrangement = Arrangement.spacedBy(14.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(54.dp)
                                        .duolingoButton(
                                            faceColor = DuoFoxOrange,
                                            lipColor = DuoFoxOrangeLip,
                                            cornerRadius = 16.dp,
                                            lipHeight = 4.dp
                                        )
                                        .padding(bottom = 4.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Calculate,
                                        contentDescription = null,
                                        tint = Color.White,
                                        modifier = Modifier.size(30.dp)
                                    )
                                }
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = "Mode Kalkulator Kamuflase Instan 1-Klik (#9)",
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.ExtraBold,
                                        color = VaultObsidian
                                    )
                                    Text(
                                        text = "Ubah seluruh layar Brankas menjadi aplikasi Kalkulator Ilmiah asli dalam 1 klik saat ada orang mendekat!",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = VaultTextMuted
                                    )
                                }
                            }

                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .duolingoCard(
                                        backgroundColor = VaultCream,
                                        borderColor = DuoSwanBorder,
                                        lipColor = DuoHareLip,
                                        cornerRadius = 18.dp,
                                        borderWidth = 2.dp,
                                        lipHeight = 4.dp
                                    )
                                    .padding(bottom = 4.dp)
                            ) {
                                Column(
                                    modifier = Modifier.padding(14.dp),
                                    verticalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Text(
                                        text = "Cara Kembali dari Kalkulator ke Brankas:",
                                        style = MaterialTheme.typography.labelLarge,
                                        fontWeight = FontWeight.ExtraBold,
                                        color = VaultObsidian
                                    )
                                    Text(
                                        text = "• Ketik 6 digit PIN Brankas Anda lalu tekan tombol '='",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = VaultObsidian
                                    )
                                    Text(
                                        text = "• Atau Tekan & Tahan (Long-Press) tombol 'AC' selama 1 detik.",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = VaultObsidian
                                    )
                                }
                            }

                            DuoTactileActionButton(
                                text = "Aktifkan Kalkulator Kamuflase Sekarang (1-Klik)",
                                icon = Icons.Default.Calculate,
                                faceColor = DuoFoxOrange,
                                lipColor = DuoFoxOrangeLip,
                                height = 54.dp,
                                onClick = onActivateCalculatorCamouflage,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("activate_calculator_camouflage_button")
                            )
                        }
                    }
                }
            }

            4 -> {
                // =========================================================================
                // MODE 4: GENERATOR KARTU KUNCI RAHASIA & STEGO QR OFFLINE (3D DUOLINGO)
                // =========================================================================
                item {
                    val qrSeed = remember(qrCardTitleInput, qrCardSecretInput, qrCardPasscodeInput) {
                        "$qrCardTitleInput::$qrCardSecretInput::${qrCardPasscodeInput.trim().uppercase()}"
                    }
                    val qrFingerprint = remember(qrSeed) {
                        EncryptionScriptEngine.compute3LayerKeyFingerprint(
                            qrCardTitleInput.ifBlank { "QR" },
                            qrCardSecretInput.take(32),
                            qrCardPasscodeInput.ifBlank { "OPEN" }
                        )
                    }

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .duolingoCard(
                                backgroundColor = DuoPureWhite,
                                borderColor = DuoBeeYellowLip,
                                lipColor = DuoBeeYellowLip,
                                cornerRadius = 26.dp,
                                borderWidth = 2.dp,
                                lipHeight = 5.dp
                            )
                            .padding(bottom = 5.dp)
                            .testTag("stego_qr_keycard_generator_card")
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(18.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(46.dp)
                                        .duolingoButton(
                                            faceColor = DuoBeeYellow,
                                            lipColor = DuoBeeYellowLip,
                                            cornerRadius = 14.dp,
                                            lipHeight = 3.dp
                                        )
                                        .padding(bottom = 3.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.QrCode2,
                                        contentDescription = null,
                                        tint = VaultObsidian,
                                        modifier = Modifier.size(26.dp)
                                    )
                                }
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = "Generator Kartu Kunci Rahasia & Stego QR Offline",
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.ExtraBold,
                                        color = VaultObsidian
                                    )
                                    Text(
                                        text = "Cetak kartu suvenir digital .PNG berisi matriks QR deterministik 21x21 & muatan steganografi terenkripsi 100% offline.",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = VaultTextMuted
                                    )
                                }
                            }

                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .duolingoCard(
                                        backgroundColor = Color(0xFF090D16),
                                        borderColor = Color(qrCardTheme.accentColorHex),
                                        lipColor = Color(qrCardTheme.primaryColorHex),
                                        cornerRadius = 22.dp,
                                        borderWidth = 2.dp,
                                        lipHeight = 5.dp
                                    )
                                    .padding(bottom = 5.dp)
                                    .padding(16.dp)
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(14.dp)
                                ) {
                                    Surface(
                                        color = Color.White,
                                        shape = RoundedCornerShape(14.dp),
                                        modifier = Modifier.size(116.dp)
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .fillMaxSize()
                                                .padding(8.dp),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            DeterministicStegoQrMatrixView(
                                                seedText = qrSeed,
                                                boxSize = 100.dp
                                            )
                                        }
                                    }

                                    Column(
                                        modifier = Modifier.weight(1f),
                                        verticalArrangement = Arrangement.spacedBy(5.dp)
                                    ) {
                                        Surface(
                                            color = Color(qrCardTheme.accentColorHex).copy(alpha = 0.22f),
                                            shape = RoundedCornerShape(50)
                                        ) {
                                            Text(
                                                text = "STEGO-QR OFFLINE CARD",
                                                fontFamily = JetBrainsMonoFontFamily,
                                                fontSize = 10.sp,
                                                fontWeight = FontWeight.ExtraBold,
                                                color = Color(qrCardTheme.accentColorHex),
                                                maxLines = 1,
                                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                            )
                                        }
                                        Text(
                                            text = qrCardTitleInput.ifBlank { "Kartu Kunci Rahasia" },
                                            style = MaterialTheme.typography.titleMedium,
                                            fontWeight = FontWeight.ExtraBold,
                                            color = Color.White,
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis
                                        )
                                        Text(
                                            text = "ID: ${qrFingerprint.take(12)}",
                                            fontFamily = JetBrainsMonoFontFamily,
                                            fontSize = 11.sp,
                                            color = Color.White.copy(alpha = 0.8f),
                                            maxLines = 1
                                        )
                                        Text(
                                            text = if (qrCardPasscodeInput.isBlank()) {
                                                "Terbuka di Brankas #$displayVaultNum"
                                            } else {
                                                "Terkunci Sandi: ${"*".repeat(qrCardPasscodeInput.length)}"
                                            },
                                            fontFamily = JetBrainsMonoFontFamily,
                                            fontSize = 10.sp,
                                            color = VaultLime,
                                            maxLines = 1
                                        )
                                    }
                                }
                            }

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .horizontalScroll(rememberScrollState()),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                StegoCoverTheme.entries.forEach { theme ->
                                    DuoTactileChip(
                                        selected = qrCardTheme == theme,
                                        label = theme.title,
                                        selectedFaceColor = DuoObsidianChipColor,
                                        selectedLipColor = DuoObsidianLipColor,
                                        selectedTextColor = VaultLime,
                                        onClick = { qrCardTheme = theme }
                                    )
                                }
                            }

                            OutlinedTextField(
                                value = qrCardFileNameInput,
                                onValueChange = { qrCardFileNameInput = it },
                                label = { Text("Nama File Kartu Kunci .PNG") },
                                singleLine = true,
                                colors = duoTextFieldColors(),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("qr_card_filename_input"),
                                shape = RoundedCornerShape(16.dp)
                            )

                            OutlinedTextField(
                                value = qrCardTitleInput,
                                onValueChange = { qrCardTitleInput = it },
                                label = { Text("Judul Kartu Kunci (Tampil di Kartu & Muatan)") },
                                singleLine = true,
                                colors = duoTextFieldColors(),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("qr_card_title_input"),
                                shape = RoundedCornerShape(16.dp)
                            )

                            OutlinedTextField(
                                value = qrCardSecretInput,
                                onValueChange = { qrCardSecretInput = it },
                                label = { Text("Kunci Pemulihan / Seed Phrase / Pesan Rahasia") },
                                minLines = 3,
                                maxLines = 5,
                                colors = duoTextFieldColors(),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("qr_card_secret_input"),
                                shape = RoundedCornerShape(16.dp)
                            )

                            OutlinedTextField(
                                value = qrCardPasscodeInput,
                                onValueChange = { qrCardPasscodeInput = it.uppercase().take(12) },
                                label = { Text("Sandi Kartu QR (Opsional)") },
                                singleLine = true,
                                colors = duoTextFieldColors(),
                                leadingIcon = {
                                    Icon(Icons.Default.Key, contentDescription = null, tint = DuoBeeYellowLip)
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("qr_card_passcode_input"),
                                shape = RoundedCornerShape(16.dp)
                            )

                            DuoTactileActionButton(
                                text = "Cetak & Simpan Kartu Kunci Stego-QR (.PNG)",
                                icon = Icons.Default.QrCode2,
                                faceColor = DuoBeeYellow,
                                lipColor = DuoBeeYellowLip,
                                textColor = VaultObsidian,
                                height = 54.dp,
                                onClick = {
                                    onCreateStegoPngClick(
                                        qrCardFileNameInput,
                                        qrCardTheme,
                                        qrCardTitleInput,
                                        qrCardSecretInput,
                                        qrCardPasscodeInput,
                                        null,
                                        null,
                                        true
                                    )
                                    activeModeTab = 1
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("create_stego_qr_card_submit_button")
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun AudioWaveformCanvas(
    amplitudes: List<Float>,
    barColor: Color,
    highlightLsbFloor: Boolean,
    modifier: Modifier = Modifier
) {
    Canvas(
        modifier = modifier
            .clip(RoundedCornerShape(10.dp))
            .background(Color(0xFF0F172A))
            .padding(horizontal = 6.dp, vertical = 6.dp)
    ) {
        val count = amplitudes.size.coerceAtLeast(1)
        val slotW = size.width / count.toFloat()
        val barW = (slotW * 0.62f).coerceAtLeast(2f)
        val centerY = size.height / 2f

        for (i in 0 until count) {
            val amp = amplitudes[i].coerceIn(0.12f, 0.96f)
            val barH = (size.height * amp).coerceAtLeast(4f)
            val left = i * slotW + (slotW - barW) / 2f
            val top = centerY - barH / 2f

            drawRoundRect(
                color = barColor,
                topLeft = Offset(left, top),
                size = Size(barW, barH),
                cornerRadius = CornerRadius(barW / 2f, barW / 2f)
            )

            // Tanda visual bit LSB di ujung bawah gelombang untuk mode Stego LSB
            if (highlightLsbFloor && i % 2 == 0) {
                drawRoundRect(
                    color = VaultLime,
                    topLeft = Offset(left, size.height - 4.dp.toPx()),
                    size = Size(barW, 3.dp.toPx()),
                    cornerRadius = CornerRadius(2f, 2f)
                )
            }
        }
    }
}

@Composable
private fun PixelGrid5x5Canvas(
    colors: List<Int>,
    gridSize: Dp
) {
    Canvas(
        modifier = Modifier
            .size(gridSize)
            .clip(RoundedCornerShape(8.dp))
            .border(1.dp, Color.White.copy(alpha = 0.35f), RoundedCornerShape(8.dp))
    ) {
        val cols = 5
        val cellW = size.width / cols.toFloat()
        val cellH = size.height / cols.toFloat()
        for (r in 0 until cols) {
            for (c in 0 until cols) {
                val idx = (r * cols + c).coerceIn(0, colors.lastIndex.coerceAtLeast(0))
                val argb = if (colors.isNotEmpty()) colors[idx] else 0xFF1E293B.toInt()
                drawRect(
                    color = Color(argb),
                    topLeft = Offset(c * cellW, r * cellH),
                    size = Size(cellW, cellH)
                )
                if (r == 2 && c == 2) {
                    drawRect(
                        color = Color(0xFF58CC02),
                        topLeft = Offset(c * cellW, r * cellH),
                        size = Size(cellW, cellH),
                        style = androidx.compose.ui.graphics.drawscope.Stroke(width = 2.5f)
                    )
                }
            }
        }
    }
}

@Composable
fun DeterministicStegoQrMatrixView(
    seedText: String,
    boxSize: Dp
) {
    val matrix = remember(seedText) {
        PngSteganographyEngine.generateDeterministicQrMatrix(seedText)
    }
    Canvas(modifier = Modifier.size(boxSize)) {
        val modules = matrix.size
        val cellW = size.width / modules.toFloat()
        val cellH = size.height / modules.toFloat()
        for (r in 0 until modules) {
            for (c in 0 until modules) {
                if (matrix[r][c]) {
                    drawRect(
                        color = Color(0xFF0F172A),
                        topLeft = Offset(c * cellW, r * cellH),
                        size = Size(cellW + 0.5f, cellH + 0.5f)
                    )
                }
            }
        }
    }
}

/**
 * Full-screen working Calculator Camouflage Overlay (#9) with 3D Duolingo Tactile Keys.
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun InstantCalculatorCamouflageOverlay(
    secretUnlockPins: Set<String>,
    onExitCamouflageToVault: () -> Unit
) {
    var displayValue by remember { mutableStateOf("0") }
    var expressionHistory by remember { mutableStateOf("") }
    var pendingOperator by remember { mutableStateOf<String?>(null) }
    var accumulator by remember { mutableStateOf<Double?>(null) }
    var resetDisplayOnNextDigit by remember { mutableStateOf(false) }

    fun onDigit(digit: String) {
        if (resetDisplayOnNextDigit || displayValue == "0") {
            displayValue = digit
            resetDisplayOnNextDigit = false
        } else if (displayValue.length < 14) {
            displayValue += digit
        }
    }

    fun evaluatePending(currentInput: Double): Double {
        val prev = accumulator ?: return currentInput
        return when (pendingOperator) {
            "+" -> prev + currentInput
            "−" -> prev - currentInput
            "×" -> prev * currentInput
            "÷" -> if (currentInput != 0.0) prev / currentInput else 0.0
            else -> currentInput
        }
    }

    fun formatCalcResult(value: Double): String {
        return if (value % 1.0 == 0.0) {
            value.toLong().toString()
        } else {
            String.format(Locale.US, "%.4f", value).trimEnd('0').trimEnd('.')
        }
    }

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .testTag("instant_calculator_camouflage_overlay"),
        color = Color(0xFF0F172A)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp, vertical = 24.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Kalkulator Standar",
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.White.copy(alpha = 0.7f),
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "DEG • RAD",
                    fontFamily = JetBrainsMonoFontFamily,
                    fontSize = 11.sp,
                    color = Color.White.copy(alpha = 0.45f)
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 18.dp),
                horizontalAlignment = Alignment.End
            ) {
                if (expressionHistory.isNotBlank()) {
                    Text(
                        text = expressionHistory,
                        fontFamily = JetBrainsMonoFontFamily,
                        fontSize = 16.sp,
                        color = Color.White.copy(alpha = 0.55f),
                        maxLines = 1
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                }
                Text(
                    text = displayValue,
                    fontFamily = JetBrainsMonoFontFamily,
                    fontSize = 46.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.White,
                    maxLines = 1,
                    modifier = Modifier.testTag("calculator_display_value")
                )
            }

            val rows = listOf(
                listOf("AC", "±", "%", "÷"),
                listOf("7", "8", "9", "×"),
                listOf("4", "5", "6", "−"),
                listOf("1", "2", "3", "+"),
                listOf("0", ".", "=")
            )

            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                rows.forEach { rowKeys ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        rowKeys.forEach { key ->
                            val isOperator = key in setOf("÷", "×", "−", "+", "=")
                            val isTopControl = key in setOf("AC", "±", "%")
                            val buttonBg = when {
                                isOperator -> DuoFoxOrange
                                isTopControl -> Color(0xFF334155)
                                else -> Color(0xFF1E293B)
                            }
                            val buttonLip = when {
                                isOperator -> DuoFoxOrangeLip
                                isTopControl -> Color(0xFF1E293B)
                                else -> Color(0xFF090D16)
                            }
                            val weight = if (key == "0") 2f else 1f

                            Box(
                                modifier = Modifier
                                    .weight(weight)
                                    .height(64.dp)
                                    .duolingoButton(
                                        faceColor = buttonBg,
                                        lipColor = buttonLip,
                                        cornerRadius = 20.dp,
                                        lipHeight = 4.dp
                                    )
                                    .combinedClickable(
                                        onClick = {
                                            when (key) {
                                                in "0".."9" -> onDigit(key)
                                                "." -> {
                                                    if (!displayValue.contains(".")) {
                                                        displayValue += "."
                                                    }
                                                }
                                                "AC" -> {
                                                    displayValue = "0"
                                                    expressionHistory = ""
                                                    accumulator = null
                                                    pendingOperator = null
                                                    resetDisplayOnNextDigit = false
                                                }
                                                "±" -> {
                                                    if (displayValue != "0") {
                                                        displayValue = if (displayValue.startsWith("-")) {
                                                            displayValue.removePrefix("-")
                                                        } else {
                                                            "-$displayValue"
                                                        }
                                                    }
                                                }
                                                "%" -> {
                                                    val v = displayValue.toDoubleOrNull() ?: 0.0
                                                    displayValue = formatCalcResult(v / 100.0)
                                                }
                                                "÷", "×", "−", "+" -> {
                                                    val current = displayValue.toDoubleOrNull() ?: 0.0
                                                    accumulator = if (accumulator != null && !resetDisplayOnNextDigit) {
                                                        evaluatePending(current)
                                                    } else {
                                                        current
                                                    }
                                                    pendingOperator = key
                                                    expressionHistory = "${formatCalcResult(accumulator ?: 0.0)} $key"
                                                    resetDisplayOnNextDigit = true
                                                }
                                                "=" -> {
                                                    if (displayValue in secretUnlockPins || displayValue == "111111") {
                                                        onExitCamouflageToVault()
                                                        return@combinedClickable
                                                    }
                                                    val current = displayValue.toDoubleOrNull() ?: 0.0
                                                    val result = evaluatePending(current)
                                                    expressionHistory = if (pendingOperator != null) {
                                                        "${formatCalcResult(accumulator ?: 0.0)} $pendingOperator ${formatCalcResult(current)} ="
                                                    } else {
                                                        "${formatCalcResult(current)} ="
                                                    }
                                                    displayValue = formatCalcResult(result)
                                                    accumulator = null
                                                    pendingOperator = null
                                                    resetDisplayOnNextDigit = true
                                                }
                                            }
                                        },
                                        onLongClick = {
                                            if (key == "AC") {
                                                onExitCamouflageToVault()
                                            }
                                        }
                                    )
                                    .padding(bottom = 4.dp)
                                    .testTag("calc_key_$key"),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = key,
                                    fontFamily = JetBrainsMonoFontFamily,
                                    fontSize = 22.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = Color.White
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun DecodedStegoResultCard(
    item: VaultItemEntity?,
    payload: DecodedStegoPayload,
    decodePasscodeInput: String,
    onDecodePasscodeChange: (String) -> Unit,
    onRetryUnlockWithPasscode: () -> Unit,
    onRestoreEmbeddedFile: () -> Unit,
    onClose: () -> Unit
) {
    val coverPhotoBitmap = remember(item?.id, payload.coverThemeName, payload.secretTitle, payload.isWavAudioCarrier) {
        if (payload.isWavAudioCarrier) {
            null
        } else {
            val theme = runCatching { StegoCoverTheme.valueOf(payload.coverThemeName) }
                .getOrDefault(StegoCoverTheme.COSMIC_POSTCARD)
            PngSteganographyEngine.loadPreviewBitmapForVaultItem(
                item = item,
                fallbackTheme = theme,
                customCaption = payload.secretTitle,
                isSecondHiddenPhotoBadge = false
            )
        }
    }
    val hiddenSecondPhotoBitmap = remember(payload.embeddedFileBytes, payload.embeddedFileExtension) {
        val bytes = payload.embeddedFileBytes
        if (bytes != null && bytes.isNotEmpty() &&
            (payload.isDualPhotoFusion || PngSteganographyEngine.isImageExtension(payload.embeddedFileExtension))
        ) {
            runCatching { BitmapFactory.decodeByteArray(bytes, 0, bytes.size) }.getOrNull()
        } else {
            null
        }
    }
    val wavPreset = remember(payload.wavPresetName) {
        runCatching { WavCamouflagePreset.valueOf(payload.wavPresetName) }
            .getOrDefault(WavCamouflagePreset.RAINY_NIGHT_AMBIENCE)
    }
    val decodedWaveformBars = remember(wavPreset, payload.secretMessage) {
        WavAudioSteganographyEngine.generateWavBitScopeSample(
            preset = wavPreset,
            secretMessage = payload.secretMessage,
            hotspotIndex = 1
        ).stegoAmplitudes
    }
    var isPlayingDecodedWav by remember { mutableStateOf(false) }
    var showEnlargedSecondPhoto by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .duolingoCard(
                backgroundColor = DuoPureWhite,
                borderColor = DuoFeatherGreen,
                lipColor = DuoFeatherGreenLip,
                cornerRadius = 24.dp,
                borderWidth = 2.dp,
                lipHeight = 6.dp
            )
            .padding(bottom = 6.dp)
            .testTag("decoded_stego_result_card")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(
                        imageVector = if (payload.isPasscodeValid) Icons.Default.CheckCircle else Icons.Default.Lock,
                        contentDescription = null,
                        tint = if (payload.isPasscodeValid) DuoFeatherGreen else VaultCoral
                    )
                    Text(
                        text = when {
                            !payload.isStegoCarrier && payload.isWavAudioCarrier ->
                                "Audio WAV Biasa (Tanpa Muatan LSB)"
                            !payload.isStegoCarrier ->
                                "Gambar PNG Biasa (Tanpa Muatan Rahasia)"
                            !payload.isPasscodeValid ->
                                "Media Stego Terkunci Sandi"
                            payload.isWavAudioCarrier && hiddenSecondPhotoBitmap != null ->
                                "Audio WAV LSB + Foto Rahasia Dibuka!"
                            payload.isWavAudioCarrier ->
                                "Pesan Rahasia Audio WAV LSB Dibongkar!"
                            hiddenSecondPhotoBitmap != null ->
                                "2 Foto dalam 1 File Berhasil Dibuka!"
                            payload.isQrKeyCard ->
                                "Kartu Kunci Stego-QR Berhasil Dibaca!"
                            else ->
                                "Isi Rahasia Berhasil Dibongkar!"
                        },
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.ExtraBold,
                        color = VaultObsidian,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                IconButton(onClick = onClose) {
                    Icon(Icons.Default.Close, contentDescription = "Tutup")
                }
            }

            if (payload.carrierSourceLabel.isNotBlank()) {
                val friendlyCarrier = when {
                    payload.carrierSourceLabel.startsWith("WAV_SYNTH::") ->
                        "Sumber Carrier: Synthesizer WAV LSB (${payload.carrierSourceLabel.removePrefix("WAV_SYNTH::")})"
                    payload.carrierSourceLabel.startsWith("VAULT_AUDIO::") ->
                        "Sumber Carrier: Audio Brankas (${payload.carrierSourceLabel.removePrefix("VAULT_AUDIO::")})"
                    payload.carrierSourceLabel.startsWith("VAULT_PHOTO::") ->
                        "Sumber Carrier: Foto Asli Brankas (${payload.carrierSourceLabel.removePrefix("VAULT_PHOTO::")})"
                    payload.carrierSourceLabel.startsWith("QR_CARD::") ->
                        "Sumber Carrier: Kartu Kunci Stego-QR Offline (21x21 Matrix)"
                    payload.carrierSourceLabel.startsWith("POSTER_THEME::") ->
                        "Sumber Carrier: Poster Digital (${payload.carrierSourceLabel.removePrefix("POSTER_THEME::")})"
                    else -> "Sumber Carrier: ${payload.carrierSourceLabel}"
                }
                Box(
                    modifier = Modifier
                        .duolingoButton(
                            faceColor = DuoMacawBlue,
                            lipColor = DuoMacawBlueLip,
                            cornerRadius = 50.dp,
                            lipHeight = 3.dp
                        )
                        .padding(horizontal = 12.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = friendlyCarrier,
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            if (!payload.isStegoCarrier) {
                Text(
                    text = "File ${item?.name ?: "ini"} adalah media standar dan tidak mengandung blok steganografi NYX2.",
                    style = MaterialTheme.typography.bodySmall,
                    color = VaultTextMuted
                )
            } else if (!payload.isPasscodeValid) {
                Text(
                    text = "Media ini dilindungi sandi steganografi. Masukkan sandi untuk mendekripsi pesan & file di dalamnya:",
                    style = MaterialTheme.typography.bodySmall,
                    color = VaultTextMuted
                )
                OutlinedTextField(
                    value = decodePasscodeInput,
                    onValueChange = onDecodePasscodeChange,
                    label = { Text("Masukkan Sandi Stego") },
                    singleLine = true,
                    colors = duoTextFieldColors(),
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp)
                )
                DuoTactileActionButton(
                    text = "Buka Kunci Stego",
                    icon = Icons.Default.LockOpen,
                    faceColor = DuoMacawBlue,
                    lipColor = DuoMacawBlueLip,
                    onClick = onRetryUnlockWithPasscode,
                    modifier = Modifier.fillMaxWidth()
                )
            } else {
                // =========================================================================
                // PEMUTAR AUDIO WAV LSB & PEMBONGKAR FOTO DI DALAM AUDIO WAV
                // =========================================================================
                if (payload.isWavAudioCarrier) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .duolingoCard(
                                backgroundColor = VaultCream,
                                borderColor = DuoFoxOrange,
                                lipColor = DuoFoxOrangeLip,
                                cornerRadius = 18.dp,
                                borderWidth = 2.dp,
                                lipHeight = 4.dp
                            )
                            .padding(bottom = 4.dp)
                            .testTag("decoded_wav_audio_player_box")
                    ) {
                        Column(
                            modifier = Modifier.padding(12.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "GELOMBANG AUDIO WAV LSB (${wavPreset.title.uppercase()})",
                                    fontFamily = JetBrainsMonoFontFamily,
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = DuoFoxOrangeLip
                                )
                                Text(
                                    text = "16-Bit PCM",
                                    fontFamily = JetBrainsMonoFontFamily,
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = DuoFeatherGreenLip
                                )
                            }

                            AudioWaveformCanvas(
                                amplitudes = decodedWaveformBars,
                                barColor = DuoFoxOrange,
                                highlightLsbFloor = true,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(58.dp)
                            )

                            DuoTactileActionButton(
                                text = if (isPlayingDecodedWav) "Hentikan Pemutaran WAV" else "Putar Suara Carrier .WAV",
                                icon = if (isPlayingDecodedWav) Icons.Default.Stop else Icons.Default.PlayArrow,
                                faceColor = if (isPlayingDecodedWav) DuoCardinalRed else DuoFoxOrange,
                                lipColor = if (isPlayingDecodedWav) DuoCardinalRedLip else DuoFoxOrangeLip,
                                height = 40.dp,
                                onClick = {
                                    if (isPlayingDecodedWav) {
                                        WavAudioSteganographyEngine.stopPreviewAudio()
                                        isPlayingDecodedWav = false
                                    } else {
                                        val wavFile = item?.internalPlainPath?.let { File(it) }
                                        WavAudioSteganographyEngine.playPreviewAudio(
                                            preset = wavPreset,
                                            existingWavFile = wavFile,
                                            isStegoModifiedPreview = true,
                                            secretSeedText = payload.secretMessage
                                        )
                                        isPlayingDecodedWav = true
                                    }
                                },
                                modifier = Modifier.fillMaxWidth()
                            )

                            if (hiddenSecondPhotoBitmap != null) {
                                Text(
                                    text = "FOTO RAHASIA YANG DISEMBUNYIKAN DI DALAM SUARA .WAV:",
                                    fontFamily = JetBrainsMonoFontFamily,
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = DuoBeetlePurpleLip
                                )
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(145.dp)
                                        .clip(RoundedCornerShape(14.dp))
                                        .background(Color(0xFF0F172A))
                                ) {
                                    if (!hiddenSecondPhotoBitmap.isRecycled) {
                                        Image(
                                            bitmap = remember(hiddenSecondPhotoBitmap) {
                                                hiddenSecondPhotoBitmap.asImageBitmap()
                                            },
                                            contentDescription = "Foto Rahasia dari Audio WAV",
                                            contentScale = ContentScale.Crop,
                                            modifier = Modifier.fillMaxSize()
                                        )
                                    }
                                }
                            }
                        }
                    }
                } else if (hiddenSecondPhotoBitmap != null && coverPhotoBitmap != null) {
                    // PEMBONGKAR 2 FOTO DALAM 1 FILE PNG (SIDE-BY-SIDE VIEWER FOR FOTO 1 & FOTO 2)
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .duolingoCard(
                                backgroundColor = VaultCream,
                                borderColor = DuoBeetlePurple,
                                lipColor = DuoBeetlePurpleLip,
                                cornerRadius = 18.dp,
                                borderWidth = 2.dp,
                                lipHeight = 4.dp
                            )
                            .padding(bottom = 4.dp)
                            .testTag("decoded_dual_photo_comparison_box")
                    ) {
                        Column(
                            modifier = Modifier.padding(12.dp),
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "PEMBONGKAR 2 FOTO DALAM 1 FILE",
                                    fontFamily = JetBrainsMonoFontFamily,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = DuoBeetlePurpleLip
                                )
                                DuoTactileChip(
                                    selected = showEnlargedSecondPhoto,
                                    label = if (showEnlargedSecondPhoto) "Side-by-Side" else "Perbesar Foto 2",
                                    selectedFaceColor = DuoBeetlePurple,
                                    selectedLipColor = DuoBeetlePurpleLip,
                                    onClick = { showEnlargedSecondPhoto = !showEnlargedSecondPhoto }
                                )
                            }

                            if (showEnlargedSecondPhoto) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(190.dp)
                                        .clip(RoundedCornerShape(14.dp))
                                        .background(Color(0xFF0F172A))
                                        .clickable { showEnlargedSecondPhoto = false }
                                ) {
                                    if (!hiddenSecondPhotoBitmap.isRecycled) {
                                        Image(
                                            bitmap = remember(hiddenSecondPhotoBitmap) {
                                                hiddenSecondPhotoBitmap.asImageBitmap()
                                            },
                                            contentDescription = "Foto Rahasia Kedua Diperbesar",
                                            contentScale = ContentScale.Crop,
                                            modifier = Modifier.fillMaxSize()
                                        )
                                    }
                                }
                            } else {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                                ) {
                                    Column(
                                        modifier = Modifier.weight(1f),
                                        verticalArrangement = Arrangement.spacedBy(4.dp)
                                    ) {
                                        Text(
                                            text = "FOTO 1 (SAMPUL LUAR)",
                                            fontFamily = JetBrainsMonoFontFamily,
                                            fontSize = 9.sp,
                                            fontWeight = FontWeight.ExtraBold,
                                            color = VaultTextMuted,
                                            maxLines = 1
                                        )
                                        Box(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .height(115.dp)
                                                .clip(RoundedCornerShape(12.dp))
                                                .background(Color(0xFF0F172A))
                                        ) {
                                            if (!coverPhotoBitmap.isRecycled) {
                                                Image(
                                                    bitmap = remember(coverPhotoBitmap) {
                                                        coverPhotoBitmap.asImageBitmap()
                                                    },
                                                    contentDescription = "Foto 1 Sampul Luar",
                                                    contentScale = ContentScale.Crop,
                                                    modifier = Modifier.fillMaxSize()
                                                )
                                            }
                                        }
                                        Text(
                                            text = item?.name ?: "Sampul.png",
                                            style = MaterialTheme.typography.bodySmall,
                                            fontWeight = FontWeight.Bold,
                                            color = VaultObsidian,
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis
                                        )
                                    }

                                    Column(
                                        modifier = Modifier.weight(1f),
                                        verticalArrangement = Arrangement.spacedBy(4.dp)
                                    ) {
                                        Text(
                                            text = "FOTO 2 (RAHASIA DI DALAM)",
                                            fontFamily = JetBrainsMonoFontFamily,
                                            fontSize = 9.sp,
                                            fontWeight = FontWeight.ExtraBold,
                                            color = DuoBeetlePurpleLip,
                                            maxLines = 1
                                        )
                                        Box(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .height(115.dp)
                                                .clip(RoundedCornerShape(12.dp))
                                                .background(Color(0xFF0F172A))
                                                .clickable { showEnlargedSecondPhoto = true }
                                        ) {
                                            if (!hiddenSecondPhotoBitmap.isRecycled) {
                                                Image(
                                                    bitmap = remember(hiddenSecondPhotoBitmap) {
                                                        hiddenSecondPhotoBitmap.asImageBitmap()
                                                    },
                                                    contentDescription = "Foto 2 Rahasia di Dalam",
                                                    contentScale = ContentScale.Crop,
                                                    modifier = Modifier.fillMaxSize()
                                                )
                                            }
                                        }
                                        Text(
                                            text = "${payload.embeddedFileName ?: "Foto_Kedua"}.${payload.embeddedFileExtension ?: "png"}",
                                            style = MaterialTheme.typography.bodySmall,
                                            fontWeight = FontWeight.Bold,
                                            color = DuoBeetlePurpleLip,
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                // SECRET TEXT MESSAGE BOX (3D DUOLINGO CARD)
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .duolingoCard(
                            backgroundColor = VaultCream,
                            borderColor = DuoSwanBorder,
                            lipColor = DuoHareLip,
                            cornerRadius = 18.dp,
                            borderWidth = 2.dp,
                            lipHeight = 4.dp
                        )
                        .padding(bottom = 4.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (payload.isQrKeyCard) {
                            Surface(
                                color = Color.White,
                                shape = RoundedCornerShape(12.dp),
                                border = androidx.compose.foundation.BorderStroke(1.dp, DuoSwanBorder),
                                modifier = Modifier.size(74.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(6.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    DeterministicStegoQrMatrixView(
                                        seedText = "${payload.secretTitle}::${payload.secretMessage}::${decodePasscodeInput.trim().uppercase()}",
                                        boxSize = 62.dp
                                    )
                                }
                            }
                        }

                        Column(
                            modifier = Modifier.weight(1f),
                            verticalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Text(
                                text = payload.secretTitle,
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.ExtraBold,
                                color = VaultObsidian
                            )
                            Text(
                                text = payload.secretMessage,
                                style = MaterialTheme.typography.bodyMedium,
                                color = VaultObsidian
                            )
                            Text(
                                text = "Dibuat: ${payload.createdAtLabel} • Checksum: ${payload.checksumHex}",
                                fontFamily = JetBrainsMonoFontFamily,
                                fontSize = 10.sp,
                                color = VaultTextMuted
                            )
                        }
                    }
                }

                if (payload.embeddedFileName != null && payload.embeddedFileBytes != null) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .duolingoCard(
                                backgroundColor = DuoPureWhite,
                                borderColor = DuoFeatherGreen,
                                lipColor = DuoFeatherGreenLip,
                                cornerRadius = 18.dp,
                                borderWidth = 2.dp,
                                lipHeight = 4.dp
                            )
                            .padding(bottom = 4.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(12.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = when {
                                    payload.isWavAudioCarrier && hiddenSecondPhotoBitmap != null ->
                                        "Foto di Dalam Audio WAV: ${payload.embeddedFileName}.${payload.embeddedFileExtension ?: "jpg"} (${formatStegoBytes(payload.embeddedFileBytes.size.toLong())})"
                                    hiddenSecondPhotoBitmap != null ->
                                        "Foto Rahasia Kedua: ${payload.embeddedFileName}.${payload.embeddedFileExtension ?: "png"} (${formatStegoBytes(payload.embeddedFileBytes.size.toLong())})"
                                    else ->
                                        "Lampiran File Tersembunyi: ${payload.embeddedFileName}.${payload.embeddedFileExtension ?: "bin"} (${formatStegoBytes(payload.embeddedFileBytes.size.toLong())})"
                                },
                                style = MaterialTheme.typography.labelLarge,
                                fontWeight = FontWeight.ExtraBold,
                                color = VaultObsidian
                            )
                            DuoTactileActionButton(
                                text = when {
                                    payload.isWavAudioCarrier && hiddenSecondPhotoBitmap != null ->
                                        "Pisahkan & Simpan Foto dari Audio ke Brankas"
                                    hiddenSecondPhotoBitmap != null ->
                                        "Pisahkan & Simpan Foto Kedua ke Galeri Brankas"
                                    else ->
                                        "Ekstrak & Simpan File Tersembunyi ke Brankas"
                                },
                                icon = if (hiddenSecondPhotoBitmap != null) Icons.Default.PhotoLibrary else Icons.Default.AttachFile,
                                faceColor = DuoFeatherGreen,
                                lipColor = DuoFeatherGreenLip,
                                onClick = onRestoreEmbeddedFile,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("restore_stego_embedded_file_button")
                            )
                        }
                    }
                }
            }
        }
    }
}

private fun formatStegoBytes(bytes: Long): String {
    if (bytes < 1024L) return "$bytes B"
    val kb = bytes / 1024.0
    if (kb < 1024.0) return String.format(Locale.US, "%.1f KB", kb)
    val mb = kb / 1024.0
    return String.format(Locale.US, "%.2f MB", mb)
}
