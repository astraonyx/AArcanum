package com.example.ui

import android.content.Intent
import android.net.Uri
import androidx.compose.ui.platform.LocalContext

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.CloudDone
import androidx.compose.material.icons.filled.CloudDownload
import androidx.compose.material.icons.filled.CloudUpload
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.LockOpen
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.SwapVert
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.filled.AddModerator
import androidx.compose.material.icons.filled.VpnKey
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import java.io.File
import kotlinx.coroutines.delay
import com.example.data.EncryptionPreset
import com.example.data.EncryptionScriptEngine
import com.example.data.SecurityConfig
import com.example.data.VaultCategory
import com.example.data.VaultItemEntity
import com.example.ui.theme.JetBrainsMonoFontFamily
import com.example.ui.theme.VaultAmber
import com.example.ui.theme.VaultCharcoal
import com.example.ui.theme.VaultCoral
import com.example.ui.theme.VaultCream
import com.example.ui.theme.VaultLime
import com.example.ui.theme.VaultMintAccent
import com.example.ui.theme.VaultMintCard
import com.example.ui.theme.VaultObsidian
import com.example.ui.theme.VaultPureWhite
import com.example.ui.theme.VaultSlate
import com.example.ui.theme.CosmicBorderHairline
import com.example.ui.theme.CosmicObsidian
import com.example.ui.theme.CosmicTextMuted
import com.example.ui.theme.CosmicGlass
import com.example.ui.theme.CosmicGradientGlassCard
import com.example.ui.theme.CosmicGradientGlassHighlight
import com.example.ui.theme.CosmicGradientCta
import com.example.ui.theme.CosmicGradientGoldCta
import com.example.ui.theme.CosmicNebulaMesh
import com.example.ui.theme.CosmicTextPure
import com.example.ui.theme.CosmicTextSubtle
import com.example.ui.theme.PastelSky
import com.example.ui.theme.PastelLavender
import com.example.ui.theme.PastelPeach
import com.example.ui.theme.PastelMint
import com.example.ui.theme.PastelRose
import com.example.ui.theme.PastelYellow
import com.example.ui.theme.SolarGold
import com.example.ui.theme.StarlightCyan
import com.example.ui.theme.NebulaViolet
import com.example.ui.theme.VaultTextMuted
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
import com.example.ui.theme.duolingoButton
import com.example.ui.theme.duolingoCard

@Composable
fun EncryptionScriptEngineScreen(
    activeVaultId: Int,
    securityConfig: SecurityConfig,
    onSaveScriptConfig: (EncryptionPreset, String) -> Unit,
    onSaveVaultPins: (vaultId: Int, String, String, String) -> Unit,
    onSaveSettingsPins: (String, String, String) -> Unit = { _, _, _ -> },
    onSaveFunZonePins: (String, String, String) -> Unit = { _, _, _ -> },
    onSaveSanctuaryPins: (String, String, String) -> Unit = { _, _, _ -> },
    onSaveAuralysPins: (String, String, String) -> Unit = { _, _, _ -> },
    onOpenSanctuaryClick: () -> Unit = {},
    onOpenAuralysClick: () -> Unit = {},
    onAddNewVault: (String, String, String, String) -> Unit = { _, _, _, _ -> },
    onRenameVault: (Int, String) -> Unit = { _, _ -> },
    onSaveRecoveryCredentials: (String, String, String, String, String, String) -> Unit = { _, _, _, _, _, _ -> },
    onToggleBiometricGate: (Boolean) -> Unit,
    onLockVaultNow: () -> Unit,
    onOpenZipScannerClick: () -> Unit = {},
    onLinkGoogleAccount: (String) -> Unit = {},
    onUnlinkGoogleAccount: () -> Unit = {},
    onBackupToCloud: () -> Unit = {},
    onRestoreFromCloud: (File) -> Unit = {},
    onResetDecoyVault: () -> Unit = {},
    onToggleAntiScreenshot: (Boolean) -> Unit = {},
    onSelectVisualTheme: (String) -> Unit = {},
    onSelectGraphicsQualityMode: (String) -> Unit = {},
    onToggleTransparentButtonBox: (Boolean) -> Unit = {},
    onOpenThemeStudioClick: () -> Unit = {},
    onOpenCamouflageTabClick: () -> Unit = {},
    onOpenProStudioTabClick: () -> Unit = {}
) {
    val context = LocalContext.current
    val activePresetInit = EncryptionPreset.entries.find { it.id == securityConfig.activeScriptName }
        ?: EncryptionPreset.NYX2_DUAL_CIPHER

    var selectedPreset by remember(securityConfig.activeScriptName) { mutableStateOf(activePresetInit) }
    var customCodeText by remember(securityConfig.customScriptCode) {
        mutableStateOf(securityConfig.customScriptCode)
    }
    var samplePlainInput by remember {
        mutableStateOf("NYX2_STREAM::4K_Video_And_Secret_Key_0091")
    }

    var editingVaultTab by remember {
        mutableIntStateOf(activeVaultId.coerceIn(1, securityConfig.vaultCount.coerceAtLeast(1)))
    }
    var previousVaultCount by remember { mutableIntStateOf(securityConfig.vaultCount) }
    LaunchedEffect(securityConfig.vaultCount) {
        if (securityConfig.vaultCount > previousVaultCount) {
            editingVaultTab = securityConfig.vaultCount.coerceAtLeast(1)
        }
        previousVaultCount = securityConfig.vaultCount
    }

    val (currentP1, currentP2, currentP3) = securityConfig.getPinsForVault(editingVaultTab)
    val currentVaultCustomName = securityConfig.getVaultName(editingVaultTab)
    var vaultNameInput by remember(editingVaultTab, currentVaultCustomName) {
        mutableStateOf(currentVaultCustomName)
    }
    var renamingVaultDialogTargetId by remember { mutableStateOf<Int?>(null) }

    var pin1Input by remember(editingVaultTab, currentP1) { mutableStateOf(currentP1) }
    var pin2Input by remember(editingVaultTab, currentP2) { mutableStateOf(currentP2) }
    var pin3Input by remember(editingVaultTab, currentP3) { mutableStateOf(currentP3) }
    var pinValidationError by remember { mutableStateOf<String?>(null) }

    val (initSetP1, initSetP2, initSetP3) = securityConfig.getSettingsPins()
    var settingsPin1Input by remember(initSetP1) { mutableStateOf(initSetP1) }
    var settingsPin2Input by remember(initSetP2) { mutableStateOf(initSetP2) }
    var settingsPin3Input by remember(initSetP3) { mutableStateOf(initSetP3) }
    var settingsPinValidationError by remember { mutableStateOf<String?>(null) }

    val (initFunP1, initFunP2, initFunP3) = securityConfig.getFunZonePins()
    var funZonePin1Input by remember(initFunP1) { mutableStateOf(initFunP1) }
    var funZonePin2Input by remember(initFunP2) { mutableStateOf(initFunP2) }
    var funZonePin3Input by remember(initFunP3) { mutableStateOf(initFunP3) }
    var funZonePinValidationError by remember { mutableStateOf<String?>(null) }

    val (initSancP1, initSancP2, initSancP3) = securityConfig.getSanctuaryPins()
    var sanctuaryPin1Input by remember(initSancP1) { mutableStateOf(initSancP1) }
    var sanctuaryPin2Input by remember(initSancP2) { mutableStateOf(initSancP2) }
    var sanctuaryPin3Input by remember(initSancP3) { mutableStateOf(initSancP3) }
    var sanctuaryPinValidationError by remember { mutableStateOf<String?>(null) }

    val (initAurP1, initAurP2, initAurP3) = securityConfig.getAuralysPins()
    var auralysPin1Input by remember(initAurP1) { mutableStateOf(initAurP1) }
    var auralysPin2Input by remember(initAurP2) { mutableStateOf(initAurP2) }
    var auralysPin3Input by remember(initAurP3) { mutableStateOf(initAurP3) }
    var auralysPinValidationError by remember { mutableStateOf<String?>(null) }

    // Hardened Settings Security: Default is View-Only Mode until unlocked with Settings PIN + Biometric/Device Credential
    var isSettingsEditUnlocked by remember { mutableStateOf(false) }
    var showSettingsEditUnlockDialog by remember { mutableStateOf(false) }

    fun requireEditUnlock(action: () -> Unit) {
        if (isSettingsEditUnlocked) {
            action()
        } else {
            showSettingsEditUnlockDialog = true
        }
    }

    var showVaultIdentityBanner by remember { mutableStateOf(false) }
    var showCreateVaultDialog by remember { mutableStateOf(false) }
    var showGoogleAccountDialog by remember { mutableStateOf(false) }
    var isRecoveryUnlocked by remember { mutableStateOf(false) }
    var showPasswordAnswers by remember { mutableStateOf(false) }

    LaunchedEffect(showVaultIdentityBanner) {
        if (showVaultIdentityBanner) {
            delay(5000)
            showVaultIdentityBanner = false
        }
    }

    var recoveryQuestion1Input by remember(securityConfig.recoveryQuestion1) {
        mutableStateOf(securityConfig.recoveryQuestion1)
    }
    var recoveryAnswer1Input by remember(securityConfig.recoveryAnswer1) {
        mutableStateOf(securityConfig.recoveryAnswer1)
    }
    var recoveryQuestion2Input by remember(securityConfig.recoveryQuestion2) {
        mutableStateOf(securityConfig.recoveryQuestion2)
    }
    var recoveryAnswer2Input by remember(securityConfig.recoveryAnswer2) {
        mutableStateOf(securityConfig.recoveryAnswer2)
    }
    var recoveryQuestion3Input by remember(securityConfig.recoveryQuestion3) {
        mutableStateOf(securityConfig.recoveryQuestion3)
    }
    var recoveryAnswer3Input by remember(securityConfig.recoveryAnswer3) {
        mutableStateOf(securityConfig.recoveryAnswer3)
    }

    val keySeed = "$currentP1:$currentP2:$currentP3:${customCodeText.hashCode()}"
    val (liveHexOutput, liveChecksum) = remember(samplePlainInput, keySeed, selectedPreset) {
        EncryptionScriptEngine.transformPreview(samplePlainInput, keySeed, selectedPreset)
    }

    // Minimalist Accordion Category States (Ringkas per kategori)
    var expandVaultPins by remember { mutableStateOf(true) }
    var expandRoomPins by remember { mutableStateOf(false) }
    var expandSecurityHoneypot by remember { mutableStateOf(false) }
    var expandRecovery by remember { mutableStateOf(false) }
    var expandCryptoEngine by remember { mutableStateOf(false) }
    var expandCloudBackup by remember { mutableStateOf(false) }
    val isAllExpanded = expandVaultPins && expandRoomPins && expandSecurityHoneypot &&
        expandRecovery && expandCryptoEngine && expandCloudBackup

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .testTag("script_engine_screen"),
        contentPadding = PaddingValues(start = 18.dp, end = 18.dp, top = 10.dp, bottom = 120.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .duolingoCard(
                        backgroundColor = DuoBeetlePurpleSoft,
                        borderColor = DuoBeetlePurple,
                        lipColor = DuoBeetlePurpleLip,
                        cornerRadius = 24.dp,
                        borderWidth = 2.dp,
                        lipHeight = 5.dp
                    )
                    .padding(bottom = 5.dp)
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
                        Column(modifier = Modifier.weight(1f)) {
                            Box(
                                modifier = Modifier
                                    .duolingoButton(
                                        faceColor = DuoBeeYellow,
                                        lipColor = DuoBeeYellowLip,
                                        cornerRadius = 10.dp,
                                        lipHeight = 3.dp
                                    )
                                    .padding(bottom = 3.dp)
                                    .padding(horizontal = 9.dp, vertical = 3.dp)
                            ) {
                                Text(
                                    text = "BRANKAS SETTING • TAB UMUM (PIN DEFAULT 222-222-222)",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = VaultObsidian,
                                    fontWeight = FontWeight.ExtraBold
                                )
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "Brankas Setting — Pengaturan Umum & PIN Ruang",
                                style = MaterialTheme.typography.titleLarge,
                                color = CosmicTextPure,
                                fontWeight = FontWeight.ExtraBold
                            )
                            Text(
                                text = "Ruang induk Brankas Setting menaungi tab Umum, Tema, Radar, Pro Hub, & ZIP Scan. Semua PIN default (222-222-222, 333-333-333, 444-444-444, Brankas 1–10) bebas diubah di sini.",
                                style = MaterialTheme.typography.bodySmall,
                                color = CosmicTextSubtle
                            )
                        }
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(44.dp)
                                .duolingoButton(
                                    faceColor = DuoMacawBlue,
                                    lipColor = DuoMacawBlueLip,
                                    cornerRadius = 14.dp,
                                    lipHeight = 4.dp
                                )
                                .instantTouchPress { onOpenZipScannerClick() }
                                .padding(bottom = 4.dp)
                                .testTag("open_zip_scanner_from_settings_button"),
                            contentAlignment = Alignment.Center
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Icon(Icons.Default.Security, contentDescription = null, tint = DuoPureWhite, modifier = Modifier.size(16.dp))
                                Text("Pemindai ZIP", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.ExtraBold, color = DuoPureWhite)
                            }
                        }

                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(44.dp)
                                .duolingoButton(
                                    faceColor = DuoCardinalRed,
                                    lipColor = DuoCardinalRedLip,
                                    cornerRadius = 14.dp,
                                    lipHeight = 4.dp
                                )
                                .instantTouchPress { onLockVaultNow() }
                                .padding(bottom = 4.dp)
                                .testTag("lock_from_isolated_settings_button"),
                            contentAlignment = Alignment.Center
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Icon(Icons.Default.Lock, contentDescription = null, tint = DuoPureWhite, modifier = Modifier.size(16.dp))
                                Text("Kunci & Keluar", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.ExtraBold, color = DuoPureWhite)
                            }
                        }
                    }

                    // Hardened View-Only vs Edit Mode Security Gate Banner
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .duolingoCard(
                                backgroundColor = if (isSettingsEditUnlocked) DuoFeatherGreenSoft else DuoBeeYellowSoft,
                                borderColor = if (isSettingsEditUnlocked) DuoFeatherGreen else DuoFoxOrange,
                                lipColor = if (isSettingsEditUnlocked) DuoFeatherGreenLip else DuoFoxOrangeLip,
                                cornerRadius = 16.dp,
                                borderWidth = 2.dp,
                                lipHeight = 4.dp
                            )
                            .padding(bottom = 4.dp)
                            .padding(12.dp)
                            .testTag("settings_view_only_security_banner")
                    ) {
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
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
                                        imageVector = if (isSettingsEditUnlocked) Icons.Default.LockOpen else Icons.Default.Lock,
                                        contentDescription = null,
                                        tint = if (isSettingsEditUnlocked) DuoFeatherGreenLip else DuoFoxOrangeLip,
                                        modifier = Modifier.size(20.dp)
                                    )
                                    Column {
                                        Text(
                                            text = if (isSettingsEditUnlocked) {
                                                "MODE EDIT ADMIN AKTIF (TERVERIFIKASI)"
                                            } else {
                                                "MODE LIHAT SAJA (VIEW-ONLY AKTIF)"
                                            },
                                            style = MaterialTheme.typography.labelLarge,
                                            fontWeight = FontWeight.ExtraBold,
                                            color = VaultObsidian
                                        )
                                        Text(
                                            text = if (isSettingsEditUnlocked) {
                                                "Anda dapat mengubah PIN brankas, PIN pengaturan, PIN Fun Zone, & skrip enkripsi."
                                            } else {
                                                "Semua perubahan dikunci. Untuk mengedit, wajib verifikasi PIN Pengaturan + Sidik Jari/Wajah/Kunci Layar Android."
                                            },
                                            style = MaterialTheme.typography.bodySmall,
                                            color = CosmicTextSubtle
                                        )
                                    }
                                }
                            }

                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(42.dp)
                                    .duolingoButton(
                                        faceColor = if (isSettingsEditUnlocked) DuoMacawBlue else DuoFeatherGreen,
                                        lipColor = if (isSettingsEditUnlocked) DuoMacawBlueLip else DuoFeatherGreenLip,
                                        cornerRadius = 12.dp,
                                        lipHeight = 4.dp
                                    )
                                    .instantTouchPress {
                                        if (isSettingsEditUnlocked) {
                                            isSettingsEditUnlocked = false
                                        } else {
                                            showSettingsEditUnlockDialog = true
                                        }
                                    }
                                    .padding(bottom = 4.dp)
                                    .testTag("toggle_settings_edit_mode_button"),
                                contentAlignment = Alignment.Center
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Icon(
                                        imageVector = if (isSettingsEditUnlocked) Icons.Default.Lock else Icons.Default.Fingerprint,
                                        contentDescription = null,
                                        tint = DuoPureWhite,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Text(
                                        text = if (isSettingsEditUnlocked) {
                                            "Kunci Kembali ke Mode View-Only"
                                        } else {
                                            "Buka Kunci Mode Edit (PIN + Sidik Jari / Kunci Layar)"
                                        },
                                        style = MaterialTheme.typography.labelMedium,
                                        fontWeight = FontWeight.ExtraBold,
                                        color = DuoPureWhite
                                    )
                                }
                            }
                        }
                    }

                    // Manual Anti-Screenshot & Anti-Screen-Record Toggle Card in Settings
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .duolingoCard(
                                backgroundColor = if (securityConfig.isAntiScreenshotEnabled) DuoFeatherGreenSoft else DuoPureWhite,
                                borderColor = if (securityConfig.isAntiScreenshotEnabled) DuoFeatherGreen else DuoMacawBlue,
                                lipColor = if (securityConfig.isAntiScreenshotEnabled) DuoFeatherGreenLip else DuoMacawBlueLip,
                                cornerRadius = 16.dp,
                                borderWidth = 2.dp,
                                lipHeight = 4.dp
                            )
                            .padding(bottom = 4.dp)
                            .padding(12.dp)
                            .testTag("settings_anti_screenshot_card")
                    ) {
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
                                Icon(
                                    imageVector = Icons.Default.Security,
                                    contentDescription = null,
                                    tint = if (securityConfig.isAntiScreenshotEnabled) DuoFeatherGreenLip else DuoMacawBlueLip,
                                    modifier = Modifier.size(22.dp)
                                )
                                Column {
                                    Text(
                                        text = if (securityConfig.isAntiScreenshotEnabled) {
                                            "Mode Anti-SS & Anti-Rekam Layar: AKTIF (GLOBAL)"
                                        } else {
                                            "Mode Anti-SS & Anti-Rekam Layar: MATI (MANUAL)"
                                        },
                                        style = MaterialTheme.typography.labelLarge,
                                        fontWeight = FontWeight.ExtraBold,
                                        color = VaultObsidian
                                    )
                                    Text(
                                        text = "Blokir tangkapan layar sistem Android di seluruh ruang & ubah perekaman layar menjadi Black Screen.",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = CosmicTextSubtle
                                    )
                                }
                            }
                            Switch(
                                checked = securityConfig.isAntiScreenshotEnabled,
                                onCheckedChange = { enabled ->
                                    onToggleAntiScreenshot(enabled)
                                },
                                colors = SwitchDefaults.colors(
                                    checkedThumbColor = DuoPureWhite,
                                    checkedTrackColor = DuoFeatherGreen
                                ),
                                modifier = Modifier.testTag("settings_anti_screenshot_switch")
                            )
                        }
                    }

                    // 6-Theme Visual Selector & Dynamic Island Studio Launcher
                    val activeVisualTheme = com.example.ui.theme.AppVisualTheme.fromId(securityConfig.visualThemeId)
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .duolingoCard(
                                backgroundColor = CosmicObsidian,
                                borderColor = DuoSwanBorder,
                                lipColor = DuoSwanBorder,
                                cornerRadius = 16.dp,
                                borderWidth = 2.dp,
                                lipHeight = 4.dp
                            )
                            .padding(bottom = 4.dp)
                            .padding(12.dp)
                            .testTag("settings_visual_theme_selector_card")
                    ) {
                        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = "PILIH GAYA TEMA VISUAL (6 ARSITEKTUR UI)",
                                        style = MaterialTheme.typography.labelLarge,
                                        fontWeight = FontWeight.Bold,
                                        color = VaultObsidian
                                    )
                                    Text(
                                        text = "Bebas pilih dari 6 tema untuk seluruh aplikasi. Khusus BlackBox Sanctuary dikunci eksklusif pada Bento Grid Glow.",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = CosmicTextSubtle
                                    )
                                }
                                Box(
                                    modifier = Modifier
                                        .duolingoButton(
                                            faceColor = DuoFeatherGreen,
                                            lipColor = DuoFeatherGreenLip,
                                            cornerRadius = 10.dp,
                                            lipHeight = 3.dp
                                        )
                                        .padding(bottom = 3.dp)
                                        .padding(horizontal = 8.dp, vertical = 3.dp)
                                ) {
                                    Text(
                                        text = activeVisualTheme.badgeText,
                                        style = MaterialTheme.typography.labelSmall,
                                        fontWeight = FontWeight.Bold,
                                        color = DuoPureWhite
                                    )
                                }
                            }

                            com.example.ui.theme.AppVisualTheme.entries.chunked(2).forEach { themePair ->
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    themePair.forEach { themeOption ->
                                        val isThemeSelected = activeVisualTheme == themeOption
                                        val optionFace = when {
                                            isThemeSelected && themeOption == com.example.ui.theme.AppVisualTheme.BENTO_GLOW -> androidx.compose.ui.graphics.Color(0xFF6366F1)
                                            isThemeSelected -> DuoMacawBlue
                                            else -> com.example.ui.theme.DuoMintSurface
                                        }
                                        val optionLip = when {
                                            isThemeSelected && themeOption == com.example.ui.theme.AppVisualTheme.BENTO_GLOW -> androidx.compose.ui.graphics.Color(0xFF4338CA)
                                            isThemeSelected -> DuoMacawBlueLip
                                            else -> DuoSwanBorder
                                        }
                                        val optionText = if (isThemeSelected) DuoPureWhite else VaultObsidian
                                        val optionSub = if (isThemeSelected) DuoPureWhite.copy(alpha = 0.9f) else CosmicTextSubtle

                                        Box(
                                            modifier = Modifier
                                                .weight(1f)
                                                .height(54.dp)
                                                .duolingoButton(
                                                    faceColor = optionFace,
                                                    lipColor = optionLip,
                                                    cornerRadius = 14.dp,
                                                    lipHeight = 4.dp
                                                )
                                                .instantTouchPress {
                                                    com.example.ui.theme.AppThemeController.activeTheme = themeOption
                                                    onSelectVisualTheme(themeOption.id)
                                                }
                                                .padding(bottom = 3.dp, start = 10.dp, end = 10.dp)
                                                .testTag("theme_option_${themeOption.id.lowercase()}"),
                                            contentAlignment = Alignment.CenterStart
                                        ) {
                                            Column {
                                                Text(
                                                    text = themeOption.displayName,
                                                    style = MaterialTheme.typography.labelMedium,
                                                    fontWeight = FontWeight.Bold,
                                                    color = optionText
                                                )
                                                Text(
                                                    text = themeOption.badgeText,
                                                    style = MaterialTheme.typography.labelSmall,
                                                    fontSize = 9.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    color = optionSub
                                                )
                                            }
                                        }
                                    }
                                }
                            }

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(44.dp)
                                        .duolingoButton(
                                            faceColor = DuoBeetlePurple,
                                            lipColor = DuoBeetlePurpleLip,
                                            cornerRadius = 14.dp,
                                            lipHeight = 4.dp
                                        )
                                        .instantTouchPress { onOpenThemeStudioClick() }
                                        .padding(bottom = 4.dp)
                                        .testTag("open_theme_studio_from_settings_button"),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "Kustom Dynamic Island & Tema",
                                        style = MaterialTheme.typography.labelMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = DuoPureWhite
                                    )
                                }

                                if (DRAFT_APP_CAMOUFLAGE_FEATURE_ENABLED) {
                                    Box(
                                        modifier = Modifier
                                            .weight(1f)
                                            .height(44.dp)
                                            .duolingoButton(
                                                faceColor = DuoFeatherGreen,
                                                lipColor = DuoFeatherGreenLip,
                                                cornerRadius = 14.dp,
                                                lipHeight = 4.dp
                                            )
                                            .instantTouchPress { onOpenCamouflageTabClick() }
                                            .padding(bottom = 4.dp)
                                            .testTag("open_camouflage_tab_from_settings_button"),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = "Tab Kamuflase App →",
                                            style = MaterialTheme.typography.labelMedium,
                                            fontWeight = FontWeight.ExtraBold,
                                            color = DuoPureWhite
                                        )
                                    }
                                }
                            }
                        }
                    }

                    // 7-Tier Graphics Quality Mode Selector (Default High + Smooth..Ultra + 2 Chaos Versions) & Pro Hub
                    val activeGraphicsMode = com.example.ui.theme.AppThemeController.graphicsQualityMode
                    val transparentBtnBoxOn = com.example.ui.theme.AppThemeController.transparentButtonBoxEnabled
                    val transparentBtnAlpha = com.example.ui.theme.AppThemeController.transparentButtonAlpha
                    val activeFontPreset = com.example.ui.theme.AppThemeController.activeFontPreset
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .duolingoCard(
                                backgroundColor = CosmicObsidian,
                                borderColor = activeGraphicsMode.accentColor,
                                lipColor = activeGraphicsMode.accentColor,
                                cornerRadius = 16.dp,
                                borderWidth = 2.dp,
                                lipHeight = 4.dp
                            )
                            .padding(bottom = 4.dp)
                            .padding(12.dp)
                            .testTag("settings_graphics_quality_mode_card")
                    ) {
                        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = "MODE GRAFIK (DEFAULT HIGH + DUAL CHAOS PRO)",
                                        style = MaterialTheme.typography.labelLarge,
                                        fontWeight = FontWeight.ExtraBold,
                                        color = VaultObsidian
                                    )
                                    Text(
                                        text = "Default disetel ke High agar aman di semua HP. Tersedia juga Chaos V2 (1/25x di bawah Smooth) & Chaos V1 Brutal (100x tes 3 detik + proteksi anti-Force Close).",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = CosmicTextSubtle
                                    )
                                }
                                Box(
                                    modifier = Modifier
                                        .duolingoButton(
                                            faceColor = activeGraphicsMode.accentColor,
                                            lipColor = DuoBeetlePurpleLip,
                                            cornerRadius = 10.dp,
                                            lipHeight = 3.dp
                                        )
                                        .padding(bottom = 3.dp)
                                        .padding(horizontal = 8.dp, vertical = 3.dp)
                                ) {
                                    Text(
                                        text = activeGraphicsMode.fpsLabel,
                                        style = MaterialTheme.typography.labelSmall,
                                        fontWeight = FontWeight.ExtraBold,
                                        color = DuoPureWhite
                                    )
                                }
                            }

                            // Normal Graphics Mode Grid (Smooth, Balanced, High [Default], Extreme, Ultra GSAP)
                            // Dual Chaos V1 & V2 live inside the dedicated "Mode Pro" tab next to Radar
                            val normalGraphicsModes = com.example.ui.theme.AppGraphicsQualityMode.entries.filter {
                                it != com.example.ui.theme.AppGraphicsQualityMode.CHAOS_ULTIMATE_LOW &&
                                    it != com.example.ui.theme.AppGraphicsQualityMode.CHAOS_OVERDRIVE
                            }
                            normalGraphicsModes.chunked(3).forEach { modeRow ->
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    modeRow.forEach { modeOption ->
                                        val isModeSelected = activeGraphicsMode == modeOption
                                        val pillFace = if (isModeSelected) modeOption.accentColor else com.example.ui.theme.DuoMintSurface
                                        val pillLip = if (isModeSelected) DuoBeetlePurpleLip else DuoSwanBorder
                                        val pillText = if (isModeSelected) DuoPureWhite else VaultObsidian
                                        val multBadge = when (modeOption) {
                                            com.example.ui.theme.AppGraphicsQualityMode.CHAOS_ULTIMATE_LOW -> "1/25x LOW"
                                            com.example.ui.theme.AppGraphicsQualityMode.CHAOS_OVERDRIVE -> "100x BRUTAL"
                                            com.example.ui.theme.AppGraphicsQualityMode.HIGH -> "7x DEFAULT"
                                            else -> "${modeOption.effectMultiplier}x EFEK"
                                        }

                                        Box(
                                            modifier = Modifier
                                                .weight(1f)
                                                .height(54.dp)
                                                .duolingoButton(
                                                    faceColor = pillFace,
                                                    lipColor = pillLip,
                                                    cornerRadius = 12.dp,
                                                    lipHeight = 3.dp
                                                )
                                                .instantTouchPress {
                                                    com.example.ui.theme.AppThemeController.requestGraphicsModeChange(
                                                        targetMode = modeOption,
                                                        context = context,
                                                        onPersistSafeMode = onSelectGraphicsQualityMode
                                                    )
                                                }
                                                .padding(bottom = 3.dp, start = 4.dp, end = 4.dp)
                                                .testTag("settings_graphics_mode_${modeOption.id.lowercase()}"),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                                Text(
                                                    text = multBadge,
                                                    fontFamily = JetBrainsMonoFontFamily,
                                                    fontSize = 8.sp,
                                                    fontWeight = FontWeight.ExtraBold,
                                                    color = if (isModeSelected) DuoPureWhite else modeOption.accentColor,
                                                    maxLines = 1
                                                )
                                                Text(
                                                    text = modeOption.displayName,
                                                    style = MaterialTheme.typography.labelSmall,
                                                    fontSize = 9.5.sp,
                                                    fontWeight = FontWeight.ExtraBold,
                                                    color = pillText,
                                                    maxLines = 1
                                                )
                                            }
                                        }
                                    }
                                }
                            }

                            // Active Graphics Mode Description Banner
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(activeGraphicsMode.accentColor.copy(alpha = 0.12f))
                                    .border(1.dp, activeGraphicsMode.accentColor.copy(alpha = 0.45f), RoundedCornerShape(12.dp))
                                    .padding(horizontal = 10.dp, vertical = 8.dp)
                            ) {
                                Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                                    Text(
                                        text = "${activeGraphicsMode.displayName.uppercase()} • ${activeGraphicsMode.badgeText}",
                                        fontFamily = JetBrainsMonoFontFamily,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.ExtraBold,
                                        color = VaultObsidian
                                    )
                                    Text(
                                        text = activeGraphicsMode.subtitle,
                                        style = MaterialTheme.typography.bodySmall,
                                        fontSize = 11.sp,
                                        color = CosmicTextSubtle
                                    )
                                }
                            }

                            // 6 Exclusive Font Options Selector directly inside Settings
                            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                                Text(
                                    text = "PILIH FONT APLIKASI (6 FONT EKSKLUSIF): ${activeFontPreset.displayName.uppercase()}",
                                    fontFamily = JetBrainsMonoFontFamily,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = VaultObsidian
                                )
                                com.example.ui.theme.AppFontPreset.entries.chunked(3).forEach { fontChunk ->
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                                    ) {
                                        fontChunk.forEach { fontOpt ->
                                            val isFontSel = activeFontPreset == fontOpt
                                            Box(
                                                modifier = Modifier
                                                    .weight(1f)
                                                    .duolingoButton(
                                                        faceColor = if (isFontSel) DuoMacawBlue else com.example.ui.theme.DuoMintSurface,
                                                        lipColor = if (isFontSel) DuoMacawBlueLip else DuoSwanBorder,
                                                        cornerRadius = 11.dp,
                                                        lipHeight = 3.dp
                                                    )
                                                    .instantTouchPress {
                                                        com.example.ui.theme.AppThemeController.selectFontPreset(fontOpt, context)
                                                    }
                                                    .padding(bottom = 3.dp)
                                                    .padding(vertical = 7.dp, horizontal = 4.dp)
                                                    .testTag("settings_font_preset_${fontOpt.id.lowercase()}"),
                                                contentAlignment = Alignment.Center
                                            ) {
                                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                                    Text(
                                                        text = fontOpt.displayName,
                                                        fontFamily = fontOpt.headlineFont,
                                                        fontSize = 10.5.sp,
                                                        fontWeight = FontWeight.Bold,
                                                        color = if (isFontSel) DuoPureWhite else VaultObsidian,
                                                        maxLines = 1
                                                    )
                                                    Text(
                                                        text = fontOpt.badgeText,
                                                        fontFamily = JetBrainsMonoFontFamily,
                                                        fontSize = 7.5.sp,
                                                        color = if (isFontSel) DuoPureWhite.copy(alpha = 0.9f) else CosmicTextSubtle,
                                                        maxLines = 1
                                                    )
                                                }
                                            }
                                        }
                                    }
                                }
                            }

                            // Pro HUD & Dual Chaos live in the dedicated "Mode Pro" tab next to Radar

                            // Toggle: Transparent Button Box (Solid Crisp Text + Colorful GSAP Border)
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(com.example.ui.theme.DuoMintSurface)
                                    .padding(horizontal = 10.dp, vertical = 8.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = "Tombol Box Transparan (Teks 100% Solid)",
                                        style = MaterialTheme.typography.labelMedium,
                                        fontWeight = FontWeight.ExtraBold,
                                        color = VaultObsidian
                                    )
                                    Text(
                                        text = "Box tombol transparan kaca dengan border pelangi GSAP berputar, sedangkan teks & ikon tetap 100% tajam.",
                                        style = MaterialTheme.typography.bodySmall,
                                        fontSize = 10.5.sp,
                                        color = CosmicTextSubtle
                                    )
                                }
                                Switch(
                                    checked = transparentBtnBoxOn,
                                    onCheckedChange = { enabled ->
                                        com.example.ui.theme.AppThemeController.transparentButtonBoxEnabled = enabled
                                        onToggleTransparentButtonBox(enabled)
                                    },
                                    colors = SwitchDefaults.colors(
                                        checkedThumbColor = DuoPureWhite,
                                        checkedTrackColor = activeGraphicsMode.accentColor
                                    ),
                                    modifier = Modifier.testTag("settings_transparent_button_box_switch")
                                )
                            }

                            if (transparentBtnBoxOn) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "Tingkat Transparansi Box:",
                                        style = MaterialTheme.typography.labelSmall,
                                        fontWeight = FontWeight.Bold,
                                        color = VaultObsidian,
                                        modifier = Modifier.weight(1f)
                                    )
                                    listOf(
                                        "Bening 14%" to 0.14f,
                                        "Kaca 22%" to 0.22f,
                                        "Kristal 35%" to 0.35f,
                                        "Tegas 50%" to 0.50f
                                    ).forEach { (label, alphaVal) ->
                                        val isAlphaSelected = kotlin.math.abs(transparentBtnAlpha - alphaVal) < 0.04f
                                        Box(
                                            modifier = Modifier
                                                .clip(RoundedCornerShape(8.dp))
                                                .background(
                                                    if (isAlphaSelected) activeGraphicsMode.accentColor else com.example.ui.theme.DuoMintSurface
                                                )
                                                .border(
                                                    1.dp,
                                                    activeGraphicsMode.accentColor.copy(alpha = 0.6f),
                                                    RoundedCornerShape(8.dp)
                                                )
                                                .instantTouchPress {
                                                    com.example.ui.theme.AppThemeController.transparentButtonAlpha = alphaVal
                                                }
                                                .padding(horizontal = 8.dp, vertical = 5.dp)
                                        ) {
                                            Text(
                                                text = label,
                                                fontSize = 9.5.sp,
                                                fontWeight = FontWeight.ExtraBold,
                                                color = if (isAlphaSelected) DuoPureWhite else VaultObsidian
                                            )
                                        }
                                    }
                                }
                            }

                            // Dedicated Tab Shortcut: Pro Hub & Secret Code Entry is placed in its own isolated tab next to Radar
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(Color(0xFF0F172A))
                                    .border(1.2.dp, Color(0xFF8B5CF6), RoundedCornerShape(12.dp))
                                    .instantTouchPress { onOpenProStudioTabClick() }
                                    .padding(horizontal = 12.dp, vertical = 10.dp)
                                    .testTag("open_dedicated_pro_tab_button"),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = if (com.example.ui.theme.AppThemeController.isProModeUnlocked) {
                                            "Mode Pro Aktif • Buka Tab 'Pro Hub' (Di Samping Radar)"
                                        } else {
                                            "Ingin Aktivasi Mode Pro & Dynamic Island?"
                                        },
                                        fontSize = 11.5.sp,
                                        fontWeight = FontWeight.ExtraBold,
                                        color = Color.White
                                    )
                                    Text(
                                        text = "Input kode Pro, Dynamic Island, Custom Domain Browser, Dual Chaos & semua pengaturan Pro terisolasi di tab Pro Hub (di samping Radar pada bar navigasi bawah).",
                                        fontSize = 10.sp,
                                        color = Color(0xFFCBD5E1)
                                    )
                                }
                                Text(
                                    text = "Pro Hub →",
                                    fontFamily = JetBrainsMonoFontFamily,
                                    fontSize = 10.5.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = Color(0xFF38BDF8)
                                )
                            }

                            // LIVE GSAP MOTION GRAPHIC UI & 6 VISUAL EFFECT STYLES + 10 ANIMATION CURVES
                            val activeVisualStyle = com.example.ui.theme.AppThemeController.activeGsapVisualStyle
                            val activeGsapCurve = com.example.ui.theme.AppThemeController.activeGsapPreset
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(14.dp))
                                    .background(androidx.compose.ui.graphics.Color(0xFF0B1120))
                                    .border(
                                        1.5.dp,
                                        activeVisualStyle.primaryColor.copy(alpha = 0.72f),
                                        RoundedCornerShape(14.dp)
                                    )
                                    .padding(10.dp),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                GsapSplitTextWaveHeader(
                                    text = "GSAP MOTION GRAPHIC & ANIMATION UI",
                                    accentColor = activeVisualStyle.primaryColor,
                                    secondaryColor = activeVisualStyle.secondaryColor
                                )

                                GsapInteractiveMotionGraphicBanner(
                                    title = "${activeVisualStyle.displayName} • ${activeGsapCurve.displayName}",
                                    subtitle = "Ketuk kanvas untuk gelombang kejut GSAP • ${activeVisualStyle.subtitle}",
                                    modifier = Modifier.fillMaxWidth()
                                )

                                Text(
                                    text = "Pilih 6 Variasi Motion Graphic UI GSAP:",
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = DuoPureWhite
                                )

                                com.example.ui.theme.GsapVisualEffectStyle.entries.chunked(2).forEach { stylePair ->
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                                    ) {
                                        stylePair.forEach { styleOption ->
                                            GsapJellySquishChip(
                                                label = styleOption.displayName,
                                                badge = styleOption.badgeText,
                                                selected = activeVisualStyle == styleOption,
                                                accentColor = styleOption.primaryColor,
                                                secondaryColor = styleOption.secondaryColor,
                                                onClick = {
                                                    com.example.ui.theme.AppThemeController.activeGsapVisualStyle = styleOption
                                                    com.example.ui.theme.AppThemeController.gsapLiveDemoTriggerNonce++
                                                },
                                                modifier = Modifier
                                                    .weight(1f)
                                                    .testTag("settings_gsap_style_${styleOption.id.lowercase()}")
                                            )
                                        }
                                    }
                                }

                                Text(
                                    text = "Pilih 10 Variasi Kurva Animasi & Micro-Interaction GSAP:",
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = DuoPureWhite
                                )

                                com.example.ui.theme.GsapMotionPreset.entries.chunked(2).forEach { presetPair ->
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                                    ) {
                                        presetPair.forEach { presetOption ->
                                            GsapJellySquishChip(
                                                label = presetOption.displayName,
                                                badge = presetOption.badgeText,
                                                selected = activeGsapCurve == presetOption,
                                                accentColor = activeVisualStyle.primaryColor,
                                                secondaryColor = activeVisualStyle.secondaryColor,
                                                onClick = {
                                                    com.example.ui.theme.AppThemeController.activeGsapPreset = presetOption
                                                    com.example.ui.theme.AppThemeController.gsapLiveDemoTriggerNonce++
                                                },
                                                modifier = Modifier
                                                    .weight(1f)
                                                    .testTag("settings_gsap_curve_${presetOption.id.lowercase()}")
                                            )
                                        }
                                    }
                                }
                            }

                            // GSAP Report Cards (Static Locked Position)
                            GsapDraggableDuoCardReportDeck(
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }

                    // Minimalist Accordion Toolbar: Ringkas / Buka Semua Kategori
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 4.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "KATEGORI PENGATURAN (KETUK UNTUK BUKA/TUTUP)",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.ExtraBold,
                            color = VaultObsidian
                        )
                        Surface(
                            color = DuoPureWhite,
                            shape = RoundedCornerShape(50),
                            border = androidx.compose.foundation.BorderStroke(1.dp, DuoBeetlePurple),
                            modifier = Modifier
                                .clickable {
                                    val target = !isAllExpanded
                                    expandVaultPins = target
                                    expandRoomPins = target
                                    expandSecurityHoneypot = target
                                    expandRecovery = target
                                    expandCryptoEngine = target
                                    expandCloudBackup = target
                                }
                                .testTag("settings_toggle_all_accordions_button")
                        ) {
                            Text(
                                text = if (isAllExpanded) "Ringkas Semua" else "Buka Semua",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.ExtraBold,
                                color = DuoBeetlePurple,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                            )
                        }
                    }
                }
            }
        }

        // KATEGORI AKORDEON 1: Brankas & PIN 3-Lapis
        item {
            MinimalistSettingsAccordionHeader(
                title = "1. Brankas & PIN 3-Lapis",
                subtitle = "${securityConfig.vaultCount} Brankas Aktif • Biometrik ${if (securityConfig.biometricEnabled) "ON" else "OFF"}",
                badgeText = "BRANKAS #1..#${securityConfig.vaultCount}",
                accentColor = DuoFeatherGreen,
                expanded = expandVaultPins,
                onToggle = { expandVaultPins = !expandVaultPins },
                tag = "accordion_header_vault_pins"
            )
        }

        // 1A. Busy 3D Duolingo Telemetry Ticker & 3-Layer Cipher Matrix Gauge
        if (expandCryptoEngine) item {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                DuolingoLiveTelemetryTicker(
                    screenTitle = "TELEMETRI MESIN SKRIP ENKRIPSI & BRANKAS 3D",
                    modeBadge = selectedPreset.id,
                    metrics = listOf(
                        TelemetryMetricPill(
                            label = "TOTAL BRANKAS",
                            value = "${securityConfig.vaultCount} RUANG",
                            accentColor = DuoFeatherGreen,
                            lipColor = DuoFeatherGreenLip,
                            softColor = DuoFeatherGreenSoft
                        ),
                        TelemetryMetricPill(
                            label = "PIN BRANKAS",
                            value = "3x6 ALFANUMERIK",
                            accentColor = DuoMacawBlue,
                            lipColor = DuoMacawBlueLip,
                            softColor = DuoMacawBlueSoft
                        ),
                        TelemetryMetricPill(
                            label = "CHECKSUM",
                            value = liveChecksum,
                            accentColor = DuoBeetlePurple,
                            lipColor = DuoBeetlePurpleLip,
                            softColor = DuoBeetlePurpleSoft
                        ),
                        TelemetryMetricPill(
                            label = "BIOMETRIK",
                            value = if (securityConfig.biometricEnabled) "AKTIF" else "NONAKTIF",
                            accentColor = DuoFoxOrange,
                            lipColor = DuoFoxOrangeLip,
                            softColor = DuoFoxOrangeSoft
                        )
                    ),
                    accentColor = DuoBeetlePurple,
                    lipColor = DuoBeetlePurpleLip,
                    softBgColor = DuoBeetlePurpleSoft
                )

                DuolingoBitMatrixGaugeCard(
                    title = "3-Layer Key Derivation & Cipher Cascade Matrix",
                    subtitle = "Preset Aktif: ${selectedPreset.title} • Checksum: $liveChecksum",
                    activeBitLoadPercent = ((liveHexOutput.length * 7) % 68) + 28,
                    primaryColor = DuoBeetlePurple,
                    lipColor = DuoBeetlePurpleLip,
                    softSurfaceColor = DuoBeetlePurpleSoft,
                    badgeLabels = listOf(
                        "PBKDF2-SHA256",
                        "3-LAYER PIN",
                        "HONEYPOT DECOY",
                        "AES-256-GCM",
                        selectedPreset.id
                    )
                )
            }
        }

        // 1B. Panduan Rahasia Pemilik: Brankas Palsu Siluman (Honeypot) & Petunjuk Mikro Asli vs Palsu
        if (expandSecurityHoneypot) item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .duolingoCard(
                        backgroundColor = DuoBeeYellowSoft,
                        borderColor = DuoBeeYellow,
                        lipColor = DuoBeeYellowLip,
                        cornerRadius = 24.dp,
                        borderWidth = 2.dp,
                        lipHeight = 4.dp
                    )
                    .padding(bottom = 4.dp)
                    .testTag("decoy_vault_owner_guide_card")
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Security,
                            contentDescription = null,
                            tint = DuoFoxOrangeLip,
                            modifier = Modifier.size(20.dp)
                        )
                        Text(
                            text = "Mode Brankas Palsu Siluman (Aktif Otomatis)",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.ExtraBold,
                            color = CosmicTextPure
                        )
                    }
                    Text(
                        text = "Semua kombinasi PIN 3-lapis yang salah tidak akan menampilkan pesan error, melainkan berpura-pura sukses membuka BRANKAS #1 berisi file umpan wajar tanpa label palsu.",
                        style = MaterialTheme.typography.bodySmall,
                        color = CosmicTextSubtle
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .duolingoCard(
                                backgroundColor = DuoPureWhite,
                                borderColor = DuoSwanBorder,
                                lipColor = DuoHareLip,
                                cornerRadius = 14.dp,
                                borderWidth = 1.5.dp,
                                lipHeight = 3.dp
                            )
                            .padding(bottom = 3.dp)
                            .padding(12.dp)
                    ) {
                        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Text(
                                text = "Cara Rahasia Membedakan Brankas Asli vs Palsu (Khusus Pemilik):",
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.ExtraBold,
                                color = VaultObsidian
                            )
                            Text(
                                text = "• BRANKAS ASLI: Lencana atas bertuliskan \"• TERENKRIPSI\" (ada titik) & subjudul memakai titik tengah \"NYX2 • ... • AES-256-GCM\".",
                                fontFamily = JetBrainsMonoFontFamily,
                                fontSize = 11.sp,
                                color = DuoFeatherGreenLip
                            )
                            Text(
                                text = "• BRANKAS PALSU: Lencana atas bertuliskan \"TERENKRIPSI\" (tanpa titik) & subjudul memakai tanda hubung \"NYX2 - ... - AES-256-GCM\".",
                                fontFamily = JetBrainsMonoFontFamily,
                                fontSize = 11.sp,
                                color = VaultCoral
                            )
                        }
                    }
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(44.dp)
                            .duolingoButton(
                                faceColor = DuoFoxOrange,
                                lipColor = DuoFoxOrangeLip,
                                cornerRadius = 14.dp,
                                lipHeight = 4.dp
                            )
                            .instantTouchPress { onResetDecoyVault() }
                            .padding(bottom = 4.dp)
                            .testTag("reset_decoy_vault_button"),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Reset Isi Brankas Palsu ke File Umpan Awal",
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.ExtraBold,
                            color = DuoPureWhite
                        )
                    }
                }
            }
        }

        // 2A. Konfigurasi 3-Lapis PIN / Sandi Ruang Brankas (Pilih Brankas #1..#N)
        if (expandVaultPins) item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(26.dp),
                colors = CardDefaults.cardColors(containerColor = CosmicGlass),
                border = androidx.compose.foundation.BorderStroke(1.5.dp, StarlightCyan.copy(alpha = 0.45f))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = StarlightCyan.copy(alpha = 0.16f),
                            modifier = Modifier.size(38.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Default.VpnKey,
                                    contentDescription = null,
                                    tint = StarlightCyan,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Ubah PIN / Sandi 3 Lapis Brankas (6 Kotak)",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = CosmicTextPure
                            )
                            Text(
                                text = "Default Skip Setup: 111111 - 111111 - 111111. Ketuk kotak untuk mengetik dengan keyboard bawaan.",
                                style = MaterialTheme.typography.bodySmall,
                                color = CosmicTextSubtle
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Selector for Vault #1..#vaultCount + Quick Default 111111
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        for (vId in 1..securityConfig.vaultCount.coerceAtLeast(1)) {
                            val selected = editingVaultTab == vId
                            Duolingo3DNavChip(
                                label = securityConfig.getVaultDisplayLabel(vId),
                                selected = selected,
                                onClick = {
                                    editingVaultTab = vId
                                    pinValidationError = null
                                },
                                accentColor = DuoFeatherGreen,
                                lipColor = DuoFeatherGreenLip,
                                softPastelColor = DuoFeatherGreenSoft,
                                testTag = "select_vault_chip_$vId"
                            )
                        }

                        Duolingo3DNavChip(
                            label = run {
                                val defPin = if (editingVaultTab == 1) "111111" else "${editingVaultTab + 1}".repeat(6).take(6)
                                "Isi Default ($defPin-$defPin-$defPin)"
                            },
                            selected = false,
                            onClick = {
                                val defPin = if (editingVaultTab == 1) "111111" else "${editingVaultTab + 1}".repeat(6).take(6)
                                pin1Input = defPin
                                pin2Input = defPin
                                pin3Input = defPin
                                pinValidationError = null
                            },
                            accentColor = DuoMacawBlue,
                            lipColor = DuoMacawBlueLip,
                            softPastelColor = DuoMacawBlueSoft,
                            testTag = "reset_vault_pin_default_chip"
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Inline Rename Vault Row inside PIN Config Card
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OutlinedTextField(
                            value = vaultNameInput,
                            onValueChange = {
                                if (isSettingsEditUnlocked) {
                                    vaultNameInput = it.take(28)
                                } else {
                                    showSettingsEditUnlockDialog = true
                                }
                            },
                            readOnly = !isSettingsEditUnlocked,
                            modifier = Modifier
                                .weight(1f)
                                .testTag("rename_vault_inline_input"),
                            label = { Text("Nama Brankas #$editingVaultTab") },
                            placeholder = { Text("Misal: Brankas Kerja / Anime") },
                            singleLine = true,
                            shape = RoundedCornerShape(14.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = CosmicTextPure,
                                unfocusedTextColor = CosmicTextPure,
                                focusedBorderColor = StarlightCyan,
                                unfocusedBorderColor = CosmicBorderHairline,
                                focusedLabelColor = StarlightCyan,
                                unfocusedLabelColor = CosmicTextSubtle
                            )
                        )
                        Button(
                            onClick = {
                                requireEditUnlock {
                                    onRenameVault(editingVaultTab, vaultNameInput.trim())
                                }
                            },
                            modifier = Modifier
                                .height(54.dp)
                                .testTag("save_vault_name_inline_button"),
                            shape = RoundedCornerShape(14.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = DuoMacawBlue,
                                contentColor = DuoPureWhite
                            )
                        ) {
                            Icon(Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Ubah Nama", fontWeight = FontWeight.ExtraBold)
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    PinLayerTextField(
                        label = "PIN / Sandi Lapis 1 Brankas #$editingVaultTab (6 Karakter)",
                        value = pin1Input,
                        onValueChange = {
                            if (isSettingsEditUnlocked) pin1Input = it.uppercase().take(6)
                            else showSettingsEditUnlockDialog = true
                        },
                        tag = "input_pin_layer_1"
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    PinLayerTextField(
                        label = "PIN / Sandi Lapis 2 Brankas #$editingVaultTab (6 Karakter)",
                        value = pin2Input,
                        onValueChange = {
                            if (isSettingsEditUnlocked) pin2Input = it.uppercase().take(6)
                            else showSettingsEditUnlockDialog = true
                        },
                        tag = "input_pin_layer_2"
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    PinLayerTextField(
                        label = "PIN / Sandi Lapis 3 Brankas #$editingVaultTab (6 Karakter)",
                        value = pin3Input,
                        onValueChange = {
                            if (isSettingsEditUnlocked) pin3Input = it.uppercase().take(6)
                            else showSettingsEditUnlockDialog = true
                        },
                        tag = "input_pin_layer_3"
                    )

                    if (pinValidationError != null) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = pinValidationError!!,
                            style = MaterialTheme.typography.bodyMedium,
                            color = VaultCoral,
                            fontWeight = FontWeight.SemiBold
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Button(
                            onClick = {
                                requireEditUnlock {
                                    val collision = securityConfig.findPinCollisionReason(
                                        p1 = pin1Input,
                                        p2 = pin2Input,
                                        p3 = pin3Input,
                                        excludeVaultId = editingVaultTab
                                    )
                                    if (!SecurityConfig.isAlphanumericValidPin(pin1Input) ||
                                        !SecurityConfig.isAlphanumericValidPin(pin2Input) ||
                                        !SecurityConfig.isAlphanumericValidPin(pin3Input)
                                    ) {
                                        pinValidationError = "Semua PIN wajib 6 karakter alfanumerik (huruf A-Z atau angka 0-9)!"
                                    } else if (collision != null) {
                                        pinValidationError = collision
                                    } else {
                                        pinValidationError = null
                                        onSaveVaultPins(editingVaultTab, pin1Input, pin2Input, pin3Input)
                                    }
                                }
                            },
                            modifier = Modifier
                                .weight(1f)
                                .height(46.dp)
                                .clip(RoundedCornerShape(14.dp))
                                .background(CosmicGradientCta)
                                .testTag("save_3layer_pins_button"),
                            shape = RoundedCornerShape(14.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = androidx.compose.ui.graphics.Color.Transparent,
                                contentColor = VaultPureWhite
                            )
                        ) {
                            Icon(Icons.Default.Save, contentDescription = null, tint = VaultPureWhite, modifier = Modifier.size(17.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Simpan PIN Brankas #$editingVaultTab", style = MaterialTheme.typography.labelLarge, color = VaultPureWhite, fontWeight = FontWeight.Bold)
                        }

                        OutlinedButton(
                            onClick = onLockVaultNow,
                            modifier = Modifier
                                .height(46.dp)
                                .testTag("test_lock_gate_button"),
                            shape = RoundedCornerShape(14.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, CosmicBorderHairline)
                        ) {
                            Icon(Icons.Default.Lock, contentDescription = null, tint = PastelRose, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Kunci", style = MaterialTheme.typography.labelLarge, color = PastelRose)
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Biometric Toggle Row
                    Surface(
                        color = CosmicObsidian.copy(alpha = 0.5f),
                        shape = RoundedCornerShape(16.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, CosmicBorderHairline),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 14.dp, vertical = 10.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(10.dp),
                                modifier = Modifier.weight(1f)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Fingerprint,
                                    contentDescription = null,
                                    tint = StarlightCyan,
                                    modifier = Modifier.size(24.dp)
                                )
                                Column {
                                    Text(
                                        text = "Wajibkan Sensor Sidik Jari",
                                        style = MaterialTheme.typography.titleMedium,
                                        color = CosmicTextPure
                                    )
                                    Text(
                                        text = "Verifikasi biometrik fisik perangkat untuk akses instan",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = CosmicTextSubtle
                                    )
                                }
                            }
                            Switch(
                                checked = securityConfig.biometricEnabled,
                                onCheckedChange = onToggleBiometricGate,
                                colors = SwitchDefaults.colors(
                                    checkedThumbColor = StarlightCyan,
                                    checkedTrackColor = CosmicObsidian
                                ),
                                modifier = Modifier.testTag("biometric_toggle_switch")
                            )
                        }
                    }
                }
            }
        }

        // KATEGORI AKORDEON 2: PIN Brankas Setting & Fun Zone
        item {
            MinimalistSettingsAccordionHeader(
                title = "2. PIN Brankas Setting & Fun Zone",
                subtitle = "Atur PIN 3-Lapis Brankas Setting (Default 222222) & Fun Zone (Default 333333)",
                badgeText = "PIN RUANG",
                accentColor = NebulaViolet,
                expanded = expandRoomPins,
                onToggle = { expandRoomPins = !expandRoomPins },
                tag = "accordion_header_room_pins"
            )
        }

        // 2B. Konfigurasi 3-Lapis PIN / Sandi Masuk ke Brankas Setting
        if (expandRoomPins) item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(26.dp),
                colors = CardDefaults.cardColors(containerColor = CosmicGlass),
                border = androidx.compose.foundation.BorderStroke(1.5.dp, NebulaViolet.copy(alpha = 0.45f))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = NebulaViolet.copy(alpha = 0.16f),
                            modifier = Modifier.size(38.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Default.Security,
                                    contentDescription = null,
                                    tint = NebulaViolet,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Ubah PIN / Sandi Masuk ke Brankas Setting (6 Kotak)",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = CosmicTextPure
                            )
                            Text(
                                text = "PIN Default: 222222 - 222222 - 222222 (bisa diubah sesuka hati). Ketik PIN ini di layar kunci utama untuk membuka Brankas Setting (Umum, Tema, Radar, Pro Hub, ZIP Scan).",
                                style = MaterialTheme.typography.bodySmall,
                                color = CosmicTextSubtle
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        FilterChip(
                            selected = false,
                            onClick = {
                                settingsPin1Input = "222222"
                                settingsPin2Input = "222222"
                                settingsPin3Input = "222222"
                                settingsPinValidationError = null
                            },
                            label = {
                                Text("Isi Default (222222-222222-222222)", fontWeight = FontWeight.Bold)
                            },
                            modifier = Modifier.testTag("reset_settings_pin_default_chip")
                        )
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    PinLayerTextField(
                        label = "PIN / Sandi Pengaturan Lapis 1 (6 Karakter)",
                        value = settingsPin1Input,
                        onValueChange = {
                            if (isSettingsEditUnlocked) settingsPin1Input = it.uppercase().take(6)
                            else showSettingsEditUnlockDialog = true
                        },
                        tag = "input_settings_pin_layer_1"
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    PinLayerTextField(
                        label = "PIN / Sandi Pengaturan Lapis 2 (6 Karakter)",
                        value = settingsPin2Input,
                        onValueChange = {
                            if (isSettingsEditUnlocked) settingsPin2Input = it.uppercase().take(6)
                            else showSettingsEditUnlockDialog = true
                        },
                        tag = "input_settings_pin_layer_2"
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    PinLayerTextField(
                        label = "PIN / Sandi Pengaturan Lapis 3 (6 Karakter)",
                        value = settingsPin3Input,
                        onValueChange = {
                            if (isSettingsEditUnlocked) settingsPin3Input = it.uppercase().take(6)
                            else showSettingsEditUnlockDialog = true
                        },
                        tag = "input_settings_pin_layer_3"
                    )

                    if (settingsPinValidationError != null) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = settingsPinValidationError!!,
                            style = MaterialTheme.typography.bodyMedium,
                            color = VaultCoral,
                            fontWeight = FontWeight.SemiBold
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Button(
                        onClick = {
                            requireEditUnlock {
                                val collision = securityConfig.findPinCollisionReason(
                                    p1 = settingsPin1Input,
                                    p2 = settingsPin2Input,
                                    p3 = settingsPin3Input,
                                    excludeSettings = true
                                )
                                if (!SecurityConfig.isAlphanumericValidPin(settingsPin1Input) ||
                                    !SecurityConfig.isAlphanumericValidPin(settingsPin2Input) ||
                                    !SecurityConfig.isAlphanumericValidPin(settingsPin3Input)
                                ) {
                                    settingsPinValidationError = "Ketiga PIN Masuk Pengaturan wajib 6 digit/karakter!"
                                } else if (collision != null) {
                                    settingsPinValidationError = collision
                                } else {
                                    settingsPinValidationError = null
                                    onSaveSettingsPins(settingsPin1Input, settingsPin2Input, settingsPin3Input)
                                }
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(46.dp)
                            .testTag("save_settings_3layer_pins_button"),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = NebulaViolet,
                            contentColor = VaultPureWhite
                        )
                    ) {
                        Icon(Icons.Default.Save, contentDescription = null, modifier = Modifier.size(17.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Simpan PIN Masuk Pengaturan",
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        // 2C. Konfigurasi 3-Lapis PIN / Sandi Masuk ke Ruang Terisolasi Fun Zone & Kapsul Waktu
        if (expandRoomPins) item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("fun_zone_pin_config_card"),
                shape = RoundedCornerShape(26.dp),
                colors = CardDefaults.cardColors(containerColor = CosmicGlass),
                border = androidx.compose.foundation.BorderStroke(1.5.dp, DuoFoxOrange.copy(alpha = 0.55f))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = DuoFoxOrange.copy(alpha = 0.18f),
                            modifier = Modifier.size(38.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Default.PlayArrow,
                                    contentDescription = null,
                                    tint = DuoFoxOrange,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Ubah PIN / Sandi Masuk Ruang Terisolasi Fun Zone (6 Kotak)",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = CosmicTextPure
                            )
                            Text(
                                text = "Default Skip Setup: 333333 - 333333 - 333333. Ketik PIN 3-lapis ini di layar kunci utama untuk membuka Galaxy Tower Defense, Slot 3D, Tarot & Kapsul Waktu.",
                                style = MaterialTheme.typography.bodySmall,
                                color = CosmicTextSubtle
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        FilterChip(
                            selected = false,
                            onClick = {
                                requireEditUnlock {
                                    funZonePin1Input = "333333"
                                    funZonePin2Input = "333333"
                                    funZonePin3Input = "333333"
                                    funZonePinValidationError = null
                                }
                            },
                            label = {
                                Text("Isi Default Skip Setup (333333-333333-333333)", fontWeight = FontWeight.Bold)
                            },
                            modifier = Modifier.testTag("reset_fun_zone_pin_default_chip")
                        )
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    PinLayerTextField(
                        label = "PIN / Sandi Fun Zone Lapis 1 (6 Karakter)",
                        value = funZonePin1Input,
                        onValueChange = {
                            if (isSettingsEditUnlocked) funZonePin1Input = it.uppercase().take(6)
                            else showSettingsEditUnlockDialog = true
                        },
                        tag = "input_fun_zone_pin_layer_1"
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    PinLayerTextField(
                        label = "PIN / Sandi Fun Zone Lapis 2 (6 Karakter)",
                        value = funZonePin2Input,
                        onValueChange = {
                            if (isSettingsEditUnlocked) funZonePin2Input = it.uppercase().take(6)
                            else showSettingsEditUnlockDialog = true
                        },
                        tag = "input_fun_zone_pin_layer_2"
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    PinLayerTextField(
                        label = "PIN / Sandi Fun Zone Lapis 3 (6 Karakter)",
                        value = funZonePin3Input,
                        onValueChange = {
                            if (isSettingsEditUnlocked) funZonePin3Input = it.uppercase().take(6)
                            else showSettingsEditUnlockDialog = true
                        },
                        tag = "input_fun_zone_pin_layer_3"
                    )

                    if (funZonePinValidationError != null) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = funZonePinValidationError!!,
                            style = MaterialTheme.typography.bodyMedium,
                            color = VaultCoral,
                            fontWeight = FontWeight.SemiBold
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Button(
                        onClick = {
                            requireEditUnlock {
                                val collision = securityConfig.findPinCollisionReason(
                                    p1 = funZonePin1Input,
                                    p2 = funZonePin2Input,
                                    p3 = funZonePin3Input,
                                    excludeFunZone = true
                                )
                                if (!SecurityConfig.isAlphanumericValidPin(funZonePin1Input) ||
                                    !SecurityConfig.isAlphanumericValidPin(funZonePin2Input) ||
                                    !SecurityConfig.isAlphanumericValidPin(funZonePin3Input)
                                ) {
                                    funZonePinValidationError = "Ketiga PIN Masuk Fun Zone wajib 6 karakter alfanumerik!"
                                } else if (collision != null) {
                                    funZonePinValidationError = collision
                                } else {
                                    funZonePinValidationError = null
                                    onSaveFunZonePins(funZonePin1Input, funZonePin2Input, funZonePin3Input)
                                }
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(46.dp)
                            .testTag("save_fun_zone_3layer_pins_button"),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = DuoFoxOrange,
                            contentColor = VaultPureWhite
                        )
                    ) {
                        Icon(Icons.Default.Save, contentDescription = null, modifier = Modifier.size(17.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Simpan PIN Masuk Ruang Fun Zone",
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.height(18.dp))

                    // 4th Isolated Space: Arcanum BlackBox Sanctuary 3-Layer PIN Config (Strictly Lock-Screen Isolated)
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Security,
                            contentDescription = null,
                            tint = DuoBeetlePurple,
                            modifier = Modifier.size(18.dp)
                        )
                        Text(
                            text = "PIN 3-Lapis Ruang Ke-4: BlackBox Sanctuary (Default: 444444 - 444444 - 444444)",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = CosmicTextPure
                        )
                    }
                    Text(
                        text = "Terisolasi murni (PIN Default: 444444 - 444444 - 444444): hanya dapat diakses melalui layar kunci utama untuk membuka Tempat Duit Tabungan & Goals Barang Impian (Grafik Box-Box), Papan Detektif, Cyber Deck OS, Midnight Zen, serta Arcanum Browser.",
                        style = MaterialTheme.typography.bodySmall,
                        color = CosmicTextSubtle
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    PinLayerTextField(
                        label = "PIN Sanctuary Lapis 1 (6 Karakter)",
                        value = sanctuaryPin1Input,
                        onValueChange = {
                            if (isSettingsEditUnlocked) sanctuaryPin1Input = it.uppercase().take(6)
                            else showSettingsEditUnlockDialog = true
                        },
                        tag = "input_sanctuary_pin_layer_1"
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    PinLayerTextField(
                        label = "PIN Sanctuary Lapis 2 (6 Karakter)",
                        value = sanctuaryPin2Input,
                        onValueChange = {
                            if (isSettingsEditUnlocked) sanctuaryPin2Input = it.uppercase().take(6)
                            else showSettingsEditUnlockDialog = true
                        },
                        tag = "input_sanctuary_pin_layer_2"
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    PinLayerTextField(
                        label = "PIN Sanctuary Lapis 3 (6 Karakter)",
                        value = sanctuaryPin3Input,
                        onValueChange = {
                            if (isSettingsEditUnlocked) sanctuaryPin3Input = it.uppercase().take(6)
                            else showSettingsEditUnlockDialog = true
                        },
                        tag = "input_sanctuary_pin_layer_3"
                    )
                    if (sanctuaryPinValidationError != null) {
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = sanctuaryPinValidationError!!,
                            style = MaterialTheme.typography.bodySmall,
                            color = VaultCoral,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    Button(
                        onClick = {
                            requireEditUnlock {
                                val collision = securityConfig.findPinCollisionReason(
                                    p1 = sanctuaryPin1Input,
                                    p2 = sanctuaryPin2Input,
                                    p3 = sanctuaryPin3Input,
                                    excludeSanctuary = true
                                )
                                if (!SecurityConfig.isAlphanumericValidPin(sanctuaryPin1Input) ||
                                    !SecurityConfig.isAlphanumericValidPin(sanctuaryPin2Input) ||
                                    !SecurityConfig.isAlphanumericValidPin(sanctuaryPin3Input)
                                ) {
                                    sanctuaryPinValidationError = "Ketiga PIN Sanctuary wajib 6 karakter alfanumerik!"
                                } else if (collision != null) {
                                    sanctuaryPinValidationError = collision
                                } else {
                                    sanctuaryPinValidationError = null
                                    onSaveSanctuaryPins(sanctuaryPin1Input, sanctuaryPin2Input, sanctuaryPin3Input)
                                }
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(46.dp)
                            .testTag("save_sanctuary_3layer_pins_button"),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = DuoBeetlePurple,
                            contentColor = VaultPureWhite
                        )
                    ) {
                        Icon(Icons.Default.Save, contentDescription = null, modifier = Modifier.size(17.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Simpan PIN 3-Lapis Sanctuary", fontWeight = FontWeight.Bold)
                    }

                    Spacer(modifier = Modifier.height(18.dp))

                    // 5th Isolated Space: Arcanum Auralys Music Chamber 3-Layer PIN Config (Strictly Lock-Screen Isolated)
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.MusicNote,
                            contentDescription = null,
                            tint = DuoFeatherGreen,
                            modifier = Modifier.size(18.dp)
                        )
                        Text(
                            text = "PIN 3-Lapis Ruang Ke-5: Auralys Music Chamber (Default: 000000 - 000000 - 000000)",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = CosmicTextPure
                        )
                    }
                    Text(
                        text = "Terisolasi murni (PIN Default: 000000 - 000000 - 000000): akses pemutar musik premium Auralys 4.3.5, offline vault audio browser, lirik sinkron & streaming musik langsung dari layar kunci utama.",
                        style = MaterialTheme.typography.bodySmall,
                        color = CosmicTextSubtle
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    PinLayerTextField(
                        label = "PIN Auralys Lapis 1 (6 Karakter)",
                        value = auralysPin1Input,
                        onValueChange = {
                            if (isSettingsEditUnlocked) auralysPin1Input = it.uppercase().take(6)
                            else showSettingsEditUnlockDialog = true
                        },
                        tag = "input_auralys_pin_layer_1"
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    PinLayerTextField(
                        label = "PIN Auralys Lapis 2 (6 Karakter)",
                        value = auralysPin2Input,
                        onValueChange = {
                            if (isSettingsEditUnlocked) auralysPin2Input = it.uppercase().take(6)
                            else showSettingsEditUnlockDialog = true
                        },
                        tag = "input_auralys_pin_layer_2"
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    PinLayerTextField(
                        label = "PIN Auralys Lapis 3 (6 Karakter)",
                        value = auralysPin3Input,
                        onValueChange = {
                            if (isSettingsEditUnlocked) auralysPin3Input = it.uppercase().take(6)
                            else showSettingsEditUnlockDialog = true
                        },
                        tag = "input_auralys_pin_layer_3"
                    )
                    if (auralysPinValidationError != null) {
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = auralysPinValidationError!!,
                            style = MaterialTheme.typography.bodySmall,
                            color = VaultCoral,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    Button(
                        onClick = {
                            requireEditUnlock {
                                val collision = securityConfig.findPinCollisionReason(
                                    p1 = auralysPin1Input,
                                    p2 = auralysPin2Input,
                                    p3 = auralysPin3Input,
                                    excludeAuralys = true
                                )
                                if (!SecurityConfig.isAlphanumericValidPin(auralysPin1Input) ||
                                    !SecurityConfig.isAlphanumericValidPin(auralysPin2Input) ||
                                    !SecurityConfig.isAlphanumericValidPin(auralysPin3Input)
                                ) {
                                    auralysPinValidationError = "Ketiga PIN Auralys wajib 6 karakter alfanumerik!"
                                } else if (collision != null) {
                                    auralysPinValidationError = collision
                                } else {
                                    auralysPinValidationError = null
                                    onSaveAuralysPins(auralysPin1Input, auralysPin2Input, auralysPin3Input)
                                }
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(46.dp)
                            .testTag("save_auralys_3layer_pins_button"),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = DuoFeatherGreen,
                            contentColor = Color.Black
                        )
                    ) {
                        Icon(Icons.Default.Save, contentDescription = null, modifier = Modifier.size(17.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Simpan PIN 3-Lapis Auralys", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        // 3. Interlocking NYX2 Input/Output Cipher Sandbox
        if (expandCryptoEngine) item {
            Column(modifier = Modifier.fillMaxWidth()) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(26.dp),
                    colors = CardDefaults.cardColors(containerColor = CosmicGlass),
                    border = androidx.compose.foundation.BorderStroke(1.dp, CosmicGradientGlassHighlight)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "INPUT PAYLOAD (PADMÉ PADDED)",
                                style = MaterialTheme.typography.labelLarge,
                                color = CosmicTextPure,
                                fontWeight = FontWeight.Bold
                            )
                            Surface(
                                color = VaultObsidian,
                                shape = RoundedCornerShape(50)
                            ) {
                                val rawLen = samplePlainInput.toByteArray().size.toLong()
                                val padded = EncryptionScriptEngine.padmeSize(rawLen)
                                Text(
                                    text = "$rawLen B -> Padmé $padded B",
                                    style = MaterialTheme.typography.labelMedium,
                                    color = VaultLime,
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        OutlinedTextField(
                            value = samplePlainInput,
                            onValueChange = { samplePlainInput = it },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("sandbox_plaintext_input"),
                            label = { Text("Uji Payload Sebelum Dienkripsi NYX2") },
                            textStyle = TextStyle(
                                fontFamily = JetBrainsMonoFontFamily,
                                fontSize = 13.sp,
                                color = VaultObsidian
                            ),
                            shape = RoundedCornerShape(16.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = VaultPureWhite,
                                unfocusedContainerColor = VaultPureWhite,
                                focusedBorderColor = VaultObsidian,
                                unfocusedBorderColor = VaultObsidian.copy(alpha = 0.25f)
                            )
                        )
                    }
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .offset(y = 0.dp)
                            .clip(CircleShape)
                            .background(VaultObsidian)
                            .border(4.dp, VaultCream, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.SwapVert,
                            contentDescription = "Transformasi NYX2",
                            tint = VaultLime,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                }

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(26.dp),
                    colors = CardDefaults.cardColors(containerColor = VaultObsidian)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "OUTPUT .NYX (MAGIC 4E 59 58 32)",
                                style = MaterialTheme.typography.labelLarge,
                                color = VaultLime
                            )
                            Text(
                                text = liveChecksum,
                                style = MaterialTheme.typography.labelMedium,
                                color = VaultMintCard
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Surface(
                            color = VaultCharcoal,
                            shape = RoundedCornerShape(14.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = liveHexOutput,
                                fontFamily = JetBrainsMonoFontFamily,
                                fontSize = 12.sp,
                                color = VaultPureWhite,
                                modifier = Modifier.padding(12.dp)
                            )
                        }
                    }
                }
            }
        }

        // 3B. Dedicated Card: Daftar Seluruh Ruang Brankas, Ubah Nama & Tambah Brankas Baru
        if (expandVaultPins) item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(26.dp),
                colors = CardDefaults.cardColors(containerColor = CosmicGlass),
                border = androidx.compose.foundation.BorderStroke(1.dp, CosmicGradientGlassHighlight)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Surface(
                                shape = CircleShape,
                                color = PastelMint.copy(alpha = 0.2f),
                                modifier = Modifier.size(38.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(
                                        imageVector = Icons.Default.AddModerator,
                                        contentDescription = null,
                                        tint = PastelMint,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                            }
                            Column {
                                Text(
                                    text = "Daftar & Pengaturan Ruang Brankas",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = CosmicTextPure
                                )
                                Text(
                                    text = "Total brankas terdaftar: ${securityConfig.vaultCount} dari maksimal 10 ruang",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = CosmicTextSubtle
                                )
                            }
                        }
                    }

                    Text(
                        text = "Semua brankas yang telah ditambahkan langsung tampil di bawah ini beserta nama, ringkasan PIN 3-lapis, tombol Ubah Nama, dan pengaturannya.",
                        style = MaterialTheme.typography.bodySmall,
                        color = CosmicTextMuted
                    )

                    // Render setiap brankas yang terdaftar (#1..#vaultCount) secara langsung
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        for (vId in 1..securityConfig.vaultCount.coerceIn(1, 10)) {
                            val (vP1, vP2, vP3) = securityConfig.getPinsForVault(vId)
                            val customName = securityConfig.getVaultName(vId)
                            val isSelectedForEdit = editingVaultTab == vId
                            Surface(
                                color = if (isSelectedForEdit) DuoFeatherGreenSoft else CosmicObsidian.copy(alpha = 0.55f),
                                shape = RoundedCornerShape(18.dp),
                                border = androidx.compose.foundation.BorderStroke(
                                    width = if (isSelectedForEdit) 2.dp else 1.dp,
                                    color = if (isSelectedForEdit) DuoFeatherGreen else CosmicBorderHairline
                                ),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("registered_vault_item_card_$vId")
                            ) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(14.dp),
                                    verticalArrangement = Arrangement.spacedBy(8.dp)
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
                                            Surface(
                                                color = if (isSelectedForEdit) DuoFeatherGreen else DuoMacawBlue,
                                                shape = RoundedCornerShape(10.dp)
                                            ) {
                                                Text(
                                                    text = "#$vId",
                                                    style = MaterialTheme.typography.labelMedium,
                                                    fontWeight = FontWeight.ExtraBold,
                                                    color = DuoPureWhite,
                                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                                )
                                            }
                                            Column {
                                                Text(
                                                    text = customName,
                                                    style = MaterialTheme.typography.titleSmall,
                                                    fontWeight = FontWeight.ExtraBold,
                                                    color = CosmicTextPure
                                                )
                                                Text(
                                                    text = "PIN 3-Lapis: $vP1 • $vP2 • $vP3",
                                                    fontFamily = JetBrainsMonoFontFamily,
                                                    fontSize = 11.sp,
                                                    color = CosmicTextSubtle
                                                )
                                            }
                                        }

                                        if (vId == activeVaultId) {
                                            Surface(
                                                color = DuoBeeYellow,
                                                shape = RoundedCornerShape(50)
                                            ) {
                                                Text(
                                                    text = "AKTIF",
                                                    style = MaterialTheme.typography.labelSmall,
                                                    fontWeight = FontWeight.ExtraBold,
                                                    color = VaultObsidian,
                                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                                )
                                            }
                                        }
                                    }

                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        OutlinedButton(
                                            onClick = {
                                                requireEditUnlock {
                                                    renamingVaultDialogTargetId = vId
                                                }
                                            },
                                            modifier = Modifier
                                                .weight(1f)
                                                .height(40.dp)
                                                .testTag("rename_vault_button_$vId"),
                                            shape = RoundedCornerShape(12.dp),
                                            border = androidx.compose.foundation.BorderStroke(1.dp, DuoMacawBlue)
                                        ) {
                                            Icon(Icons.Default.Edit, contentDescription = null, tint = DuoMacawBlue, modifier = Modifier.size(15.dp))
                                            Spacer(modifier = Modifier.width(6.dp))
                                            Text("Ubah Nama", color = DuoMacawBlue, style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold)
                                        }

                                        Button(
                                            onClick = {
                                                editingVaultTab = vId
                                                pinValidationError = null
                                            },
                                            modifier = Modifier
                                                .weight(1f)
                                                .height(40.dp)
                                                .testTag("edit_vault_settings_button_$vId"),
                                            shape = RoundedCornerShape(12.dp),
                                            colors = ButtonDefaults.buttonColors(
                                                containerColor = if (isSelectedForEdit) DuoFeatherGreen else DuoBeetlePurple,
                                                contentColor = DuoPureWhite
                                            )
                                        ) {
                                            Icon(Icons.Default.VpnKey, contentDescription = null, modifier = Modifier.size(15.dp))
                                            Spacer(modifier = Modifier.width(6.dp))
                                            Text(
                                                text = if (isSelectedForEdit) "Sedang Diedit" else "Atur PIN #$vId",
                                                style = MaterialTheme.typography.labelMedium,
                                                fontWeight = FontWeight.Bold
                                            )
                                        }
                                    }

                                    // Jika brankas ini sedang dipilih, tampilkan juga editor cepat PIN & Nama langsung di kartu ini!
                                    if (isSelectedForEdit) {
                                        Surface(
                                            color = VaultObsidian.copy(alpha = 0.85f),
                                            shape = RoundedCornerShape(14.dp),
                                            modifier = Modifier.fillMaxWidth()
                                        ) {
                                            Column(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .padding(12.dp),
                                                verticalArrangement = Arrangement.spacedBy(8.dp)
                                            ) {
                                                Text(
                                                    text = "Pengaturan Cepat Brankas #$vId ($customName)",
                                                    style = MaterialTheme.typography.labelMedium,
                                                    color = DuoFeatherGreen,
                                                    fontWeight = FontWeight.ExtraBold
                                                )
                                                Row(
                                                    modifier = Modifier.fillMaxWidth(),
                                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                                ) {
                                                    OutlinedTextField(
                                                        value = pin1Input,
                                                        onValueChange = {
                                                            if (isSettingsEditUnlocked) pin1Input = it.uppercase().take(6)
                                                            else showSettingsEditUnlockDialog = true
                                                        },
                                                        modifier = Modifier.weight(1f),
                                                        label = { Text("Lapis 1", fontSize = 10.sp) },
                                                        singleLine = true,
                                                        shape = RoundedCornerShape(10.dp)
                                                    )
                                                    OutlinedTextField(
                                                        value = pin2Input,
                                                        onValueChange = {
                                                            if (isSettingsEditUnlocked) pin2Input = it.uppercase().take(6)
                                                            else showSettingsEditUnlockDialog = true
                                                        },
                                                        modifier = Modifier.weight(1f),
                                                        label = { Text("Lapis 2", fontSize = 10.sp) },
                                                        singleLine = true,
                                                        shape = RoundedCornerShape(10.dp)
                                                    )
                                                    OutlinedTextField(
                                                        value = pin3Input,
                                                        onValueChange = {
                                                            if (isSettingsEditUnlocked) pin3Input = it.uppercase().take(6)
                                                            else showSettingsEditUnlockDialog = true
                                                        },
                                                        modifier = Modifier.weight(1f),
                                                        label = { Text("Lapis 3", fontSize = 10.sp) },
                                                        singleLine = true,
                                                        shape = RoundedCornerShape(10.dp)
                                                    )
                                                }
                                                Button(
                                                    onClick = {
                                                        requireEditUnlock {
                                                            if (SecurityConfig.isAlphanumericValidPin(pin1Input) &&
                                                                SecurityConfig.isAlphanumericValidPin(pin2Input) &&
                                                                SecurityConfig.isAlphanumericValidPin(pin3Input)
                                                            ) {
                                                                onSaveVaultPins(vId, pin1Input, pin2Input, pin3Input)
                                                            } else {
                                                                pinValidationError = "PIN wajib 6 karakter A-Z / 0-9!"
                                                            }
                                                        }
                                                    },
                                                    modifier = Modifier
                                                        .fillMaxWidth()
                                                        .height(40.dp)
                                                        .testTag("quick_save_vault_pins_button_$vId"),
                                                    shape = RoundedCornerShape(10.dp),
                                                    colors = ButtonDefaults.buttonColors(
                                                        containerColor = DuoFeatherGreen,
                                                        contentColor = VaultObsidian
                                                    )
                                                ) {
                                                    Icon(Icons.Default.Save, contentDescription = null, modifier = Modifier.size(15.dp))
                                                    Spacer(modifier = Modifier.width(6.dp))
                                                    Text("Simpan PIN Brankas #$vId", fontWeight = FontWeight.ExtraBold)
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }

                    Button(
                        onClick = {
                            requireEditUnlock {
                                showCreateVaultDialog = true
                            }
                        },
                        enabled = securityConfig.vaultCount < 10,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .testTag("add_new_vault_button"),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = PastelMint,
                            contentColor = VaultObsidian
                        )
                    ) {
                        Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (securityConfig.vaultCount < 10) "+ Buat Ruang Brankas Baru (#${securityConfig.vaultCount + 1})" else "Maksimal 10 Brankas Tercapai",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                    }
                }
            }
        }

        // KATEGORI AKORDEON 3: Mode Brankas Palsu Siluman (Honeypot)
        item {
            MinimalistSettingsAccordionHeader(
                title = "3. Mode Brankas Palsu (Honeypot)",
                subtitle = "Panduan indikator brankas asli vs palsu & reset file umpan",
                badgeText = "SILUMAN",
                accentColor = DuoFoxOrange,
                expanded = expandSecurityHoneypot,
                onToggle = { expandSecurityHoneypot = !expandSecurityHoneypot },
                tag = "accordion_header_honeypot"
            )
        }

        // KATEGORI AKORDEON 4: Cadangan Google Cloud Terenkripsi
        item {
            MinimalistSettingsAccordionHeader(
                title = "4. Akun Google & Cloud Backup",
                subtitle = if (securityConfig.isGoogleLinked) "Terhubung: ${securityConfig.googleAccountEmail}" else "Belum tertaut akun Google",
                badgeText = if (securityConfig.isGoogleLinked) "AKTIF" else "OPSIONAL",
                accentColor = DuoMacawBlue,
                expanded = expandCloudBackup,
                onToggle = { expandCloudBackup = !expandCloudBackup },
                tag = "accordion_header_cloud_backup"
            )
        }

        // 3B. Pencadangan Google Cloud Terenkripsi (Manual AES-256)
        if (expandCloudBackup) item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(26.dp),
                colors = CardDefaults.cardColors(containerColor = CosmicGlass),
                border = androidx.compose.foundation.BorderStroke(1.dp, CosmicGradientGlassHighlight)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(38.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(PastelSky.copy(alpha = 0.22f))
                                .border(1.dp, PastelSky.copy(alpha = 0.45f), RoundedCornerShape(12.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.CloudUpload,
                                contentDescription = null,
                                tint = PastelSky,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Text(
                            text = "Pencadangan Google Cloud Terenkripsi",
                            style = MaterialTheme.typography.titleLarge,
                            color = CosmicTextPure,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Text(
                        text = "Cadangkan file dan database brankas ke paket arsip terenkripsi AES-256-GCM (.nyxpkg). Pencadangan ini bersifat manual murni tanpa sinkronisasi otomatis latar belakang.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = VaultTextMuted
                    )

                    Surface(
                        color = VaultCream,
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(10.dp),
                                modifier = Modifier.weight(1f)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.AccountCircle,
                                    contentDescription = null,
                                    tint = if (securityConfig.isGoogleLinked) VaultLime else VaultObsidian,
                                    modifier = Modifier.size(28.dp)
                                )
                                Column {
                                    Text(
                                        text = if (securityConfig.isGoogleLinked) "Akun Google Terhubung" else "Belum Tertaut Akun Google",
                                        style = MaterialTheme.typography.titleMedium,
                                        color = CosmicTextPure,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                    Text(
                                        text = securityConfig.googleAccountEmail.ifBlank { "Tautkan akun Google untuk backup/restore cloud" },
                                        style = MaterialTheme.typography.bodySmall,
                                        color = VaultTextMuted
                                    )
                                }
                            }

                            if (securityConfig.isGoogleLinked) {
                                OutlinedButton(
                                    onClick = onUnlinkGoogleAccount,
                                    shape = RoundedCornerShape(12.dp),
                                    border = androidx.compose.foundation.BorderStroke(1.dp, VaultCoral)
                                ) {
                                    Text("Putuskan", color = VaultCoral, style = MaterialTheme.typography.labelMedium)
                                }
                            } else {
                                Button(
                                    onClick = {
                                        try {
                                            val account = com.google.android.gms.auth.api.signin.GoogleSignIn.getLastSignedInAccount(context)
                                            if (account != null && !account.email.isNullOrBlank()) {
                                                onLinkGoogleAccount(account.email ?: "")
                                            } else {
                                                showGoogleAccountDialog = true
                                            }
                                        } catch (_: Exception) {
                                            showGoogleAccountDialog = true
                                        }
                                    },
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(CosmicGradientCta),
                                    shape = RoundedCornerShape(12.dp),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = androidx.compose.ui.graphics.Color.Transparent,
                                        contentColor = CosmicTextPure
                                    )
                                ) {
                                    Text("Tautkan Otomatis", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold, color = CosmicTextPure)
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    val isCloudEnabled = securityConfig.isGoogleLinked && securityConfig.googleAccountEmail.isNotBlank()
                    if (!isCloudEnabled) {
                        Surface(
                            color = DuoCardinalRedSoft,
                            shape = RoundedCornerShape(14.dp),
                            border = androidx.compose.foundation.BorderStroke(1.5.dp, DuoCardinalRed),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "[TERKUNCI] FITUR CLOUD MATI TOTAL: Anda belum login Akun Google (Opsional). Tautkan akun Google di atas untuk mengaktifkan pencadangan Cloud.",
                                style = MaterialTheme.typography.bodySmall,
                                color = DuoCardinalRedLip,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(12.dp)
                            )
                        }
                    }

                    Button(
                        onClick = {
                            if (isCloudEnabled) {
                                onBackupToCloud()
                            } else {
                                showGoogleAccountDialog = true
                            }
                        },
                        enabled = isCloudEnabled,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .clip(RoundedCornerShape(14.dp))
                            .background(if (isCloudEnabled) CosmicGradientCta else androidx.compose.ui.graphics.Brush.horizontalGradient(listOf(DuoSwanBorder, DuoSwanBorder))),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = androidx.compose.ui.graphics.Color.Transparent,
                            contentColor = CosmicTextPure,
                            disabledContainerColor = androidx.compose.ui.graphics.Color.Transparent,
                            disabledContentColor = CosmicTextSubtle
                        )
                    ) {
                        Icon(
                            imageVector = if (isCloudEnabled) Icons.Default.CloudUpload else Icons.Default.Lock,
                            contentDescription = null,
                            tint = if (isCloudEnabled) CosmicTextPure else CosmicTextSubtle,
                            modifier = Modifier.size(19.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (isCloudEnabled) "Cadangkan Brankas ke Google Cloud" else "Cloud Backup Mati Total (Wajib Login Google)",
                            style = MaterialTheme.typography.titleSmall,
                            color = if (isCloudEnabled) CosmicTextPure else CosmicTextSubtle,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        // KATEGORI AKORDEON 5: Pemulihan 3 Pertanyaan Keamanan
        item {
            MinimalistSettingsAccordionHeader(
                title = "5. Pemulihan Lupa PIN (3 Pertanyaan)",
                subtitle = "Kredensial darurat untuk mereset PIN 3-lapis saat lupa sandi",
                badgeText = "DARURAT",
                accentColor = SolarGold,
                expanded = expandRecovery,
                onToggle = { expandRecovery = !expandRecovery },
                tag = "accordion_header_recovery"
            )
        }

        // 3B. Forgot PIN / Password Security Recovery Configuration Card
        if (expandRecovery) item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(26.dp),
                colors = CardDefaults.cardColors(containerColor = CosmicGlass),
                border = androidx.compose.foundation.BorderStroke(1.dp, CosmicGradientGlassHighlight)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(38.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(SolarGold.copy(alpha = 0.22f))
                                .border(1.dp, SolarGold.copy(alpha = 0.45f), RoundedCornerShape(12.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Key,
                                contentDescription = null,
                                tint = SolarGold,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Column {
                            Text(
                                text = "Pemulihan 3 Pertanyaan Keamanan",
                                style = MaterialTheme.typography.titleLarge,
                                color = CosmicTextPure,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Kunci darurat reset PIN 3 Lapis saat lupa sandi",
                                style = MaterialTheme.typography.bodySmall,
                                color = CosmicTextSubtle
                            )
                        }
                    }

                    if (!isRecoveryUnlocked) {
                        // Shield: Protected recovery credentials
                        Surface(
                            color = CosmicObsidian.copy(alpha = 0.5f),
                            shape = RoundedCornerShape(18.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, SolarGold.copy(alpha = 0.35f)),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(18.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Surface(
                                    shape = CircleShape,
                                    color = SolarGold.copy(alpha = 0.2f),
                                    modifier = Modifier.size(42.dp)
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Icon(
                                            imageVector = Icons.Default.Lock,
                                            contentDescription = null,
                                            tint = SolarGold,
                                            modifier = Modifier.size(24.dp)
                                        )
                                    }
                                }
                                Spacer(modifier = Modifier.height(10.dp))
                                Text(
                                    text = "Kredensial Pemulihan Dilindungi",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = CosmicTextPure
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "Jawaban pertanyaan keamanan disembunyikan demi mencegah kebocoran saat layar dilihat orang lain.",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = CosmicTextSubtle,
                                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                                )
                                Spacer(modifier = Modifier.height(14.dp))
                                Button(
                                    onClick = {
                                        triggerHardwareBiometricIfAvailable(
                                            context = context,
                                            title = "Buka Kunci Pertanyaan Pemulihan",
                                            subtitle = "Verifikasi sidik jari Anda untuk melihat & mengubah jawaban keamanan",
                                            onSuccess = { isRecoveryUnlocked = true },
                                            onErrorFeedback = { isRecoveryUnlocked = true }
                                        )
                                    },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(46.dp)
                                        .testTag("unlock_recovery_credentials_button"),
                                    shape = RoundedCornerShape(12.dp),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = SolarGold,
                                        contentColor = VaultObsidian
                                    )
                                ) {
                                    Icon(Icons.Default.Fingerprint, contentDescription = null, modifier = Modifier.size(18.dp))
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text("Buka Kunci Jawaban (Verifikasi Sensor)", fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    } else {
                        // Unlocked: Masked with PasswordVisualTransformation and toggle
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Jawaban Terbuka (Dapat Diedit)",
                                style = MaterialTheme.typography.labelMedium,
                                color = SolarGold,
                                fontWeight = FontWeight.Bold
                            )
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                IconButton(
                                    onClick = { showPasswordAnswers = !showPasswordAnswers },
                                    modifier = Modifier.size(32.dp)
                                ) {
                                    Icon(
                                        imageVector = if (showPasswordAnswers) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                        contentDescription = "Toggle Jawaban",
                                        tint = CosmicTextSubtle
                                    )
                                }
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = if (showPasswordAnswers) "Sembunyikan" else "Perlihatkan",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = CosmicTextSubtle
                                )
                                Spacer(modifier = Modifier.width(10.dp))
                                OutlinedButton(
                                    onClick = { isRecoveryUnlocked = false },
                                    shape = RoundedCornerShape(8.dp),
                                    modifier = Modifier.height(30.dp),
                                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp)
                                ) {
                                    Text("Kunci", fontSize = 11.sp, color = CosmicTextPure)
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        // Pertanyaan 1
                        OutlinedTextField(
                            value = recoveryQuestion1Input,
                            onValueChange = { recoveryQuestion1Input = it },
                            label = { Text("Pertanyaan Pemulihan 1") },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth().testTag("settings_recovery_question_input"),
                            shape = RoundedCornerShape(12.dp)
                        )
                        OutlinedTextField(
                            value = recoveryAnswer1Input,
                            onValueChange = { recoveryAnswer1Input = it },
                            label = { Text("Jawaban Rahasia 1") },
                            placeholder = { Text("Ketik jawaban 1...") },
                            singleLine = true,
                            visualTransformation = if (showPasswordAnswers) VisualTransformation.None else PasswordVisualTransformation(),
                            modifier = Modifier.fillMaxWidth().testTag("settings_recovery_answer_input"),
                            shape = RoundedCornerShape(12.dp)
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        // Pertanyaan 2
                        OutlinedTextField(
                            value = recoveryQuestion2Input,
                            onValueChange = { recoveryQuestion2Input = it },
                            label = { Text("Pertanyaan Pemulihan 2") },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp)
                        )
                        OutlinedTextField(
                            value = recoveryAnswer2Input,
                            onValueChange = { recoveryAnswer2Input = it },
                            label = { Text("Jawaban Rahasia 2") },
                            placeholder = { Text("Ketik jawaban 2...") },
                            singleLine = true,
                            visualTransformation = if (showPasswordAnswers) VisualTransformation.None else PasswordVisualTransformation(),
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp)
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        // Pertanyaan 3
                        OutlinedTextField(
                            value = recoveryQuestion3Input,
                            onValueChange = { recoveryQuestion3Input = it },
                            label = { Text("Pertanyaan Pemulihan 3") },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp)
                        )
                        OutlinedTextField(
                            value = recoveryAnswer3Input,
                            onValueChange = { recoveryAnswer3Input = it },
                            label = { Text("Jawaban Rahasia 3") },
                            placeholder = { Text("Ketik jawaban 3...") },
                            singleLine = true,
                            visualTransformation = if (showPasswordAnswers) VisualTransformation.None else PasswordVisualTransformation(),
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp)
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        Button(
                            onClick = {
                                onSaveRecoveryCredentials(
                                    recoveryQuestion1Input, recoveryAnswer1Input,
                                    recoveryQuestion2Input, recoveryAnswer2Input,
                                    recoveryQuestion3Input, recoveryAnswer3Input
                                )
                                isRecoveryUnlocked = false
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp)
                                .clip(RoundedCornerShape(14.dp))
                                .background(CosmicGradientGoldCta)
                                .testTag("save_recovery_credentials_button"),
                            shape = RoundedCornerShape(14.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = androidx.compose.ui.graphics.Color.Transparent,
                                contentColor = CosmicTextPure
                            )
                        ) {
                            Icon(Icons.Default.Save, contentDescription = null, tint = CosmicTextPure, modifier = Modifier.size(19.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Simpan 3 Pertanyaan Pemulihan", style = MaterialTheme.typography.titleSmall, color = CosmicTextPure, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        // KATEGORI AKORDEON 6: Mesin Skrip Enkripsi NYX2 & Sandbox
        item {
            MinimalistSettingsAccordionHeader(
                title = "6. Mesin Skrip Kriptografi NYX2",
                subtitle = "Preset aktif: ${selectedPreset.title} • Checksum: $liveChecksum",
                badgeText = selectedPreset.id,
                accentColor = DuoBeetlePurple,
                expanded = expandCryptoEngine,
                onToggle = { expandCryptoEngine = !expandCryptoEngine },
                tag = "accordion_header_crypto_engine"
            )
        }

        // 4. NYX2 Custom Script Preset Selector & Editor
        if (expandCryptoEngine) item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(26.dp),
                colors = CardDefaults.cardColors(containerColor = CosmicGlass),
                border = androidx.compose.foundation.BorderStroke(1.dp, CosmicGradientGlassHighlight)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(38.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(PastelLavender.copy(alpha = 0.22f))
                                .border(1.dp, PastelLavender.copy(alpha = 0.45f), RoundedCornerShape(12.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Code,
                                contentDescription = null,
                                tint = PastelLavender,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Column {
                            Text(
                                text = "Profil Enkripsi NYX2 (nyx_crypt_optimized.js)",
                                style = MaterialTheme.typography.titleLarge,
                                color = CosmicTextPure,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "ChaCha20-Poly1305 -> AES-256-GCM + Padmé",
                                style = MaterialTheme.typography.bodySmall,
                                color = CosmicTextSubtle
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        EncryptionPreset.entries.forEach { preset ->
                            val isSelected = selectedPreset == preset
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(16.dp))
                                    .background(if (isSelected) PastelLavender.copy(alpha = 0.25f) else androidx.compose.ui.graphics.Color(0x221E293B))
                                    .border(1.dp, if (isSelected) PastelLavender else CosmicBorderHairline, RoundedCornerShape(16.dp))
                                    .clickable {
                                        selectedPreset = preset
                                        customCodeText = preset.scriptTemplate
                                    }
                                    .padding(12.dp)
                                    .testTag("preset_option_${preset.name}")
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = preset.title,
                                            style = MaterialTheme.typography.titleMedium,
                                            color = if (isSelected) PastelLavender else CosmicTextPure,
                                            fontWeight = FontWeight.SemiBold
                                        )
                                        Text(
                                            text = preset.subtitle,
                                            style = MaterialTheme.typography.bodyMedium,
                                            color = if (isSelected) VaultPureWhite.copy(alpha = 0.75f) else VaultTextMuted
                                        )
                                    }
                                    if (isSelected) {
                                        Icon(
                                            imageVector = Icons.Default.CheckCircle,
                                            contentDescription = "Aktif",
                                            tint = VaultLime
                                        )
                                    }
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = customCodeText,
                        onValueChange = { customCodeText = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(145.dp)
                            .testTag("custom_script_code_editor"),
                        label = { Text("Pipeline Kriptografi NYX2") },
                        textStyle = TextStyle(
                            fontFamily = JetBrainsMonoFontFamily,
                            fontSize = 12.sp,
                            color = VaultPureWhite
                        ),
                        shape = RoundedCornerShape(16.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = VaultObsidian,
                            unfocusedContainerColor = VaultObsidian,
                            focusedLabelColor = VaultLime,
                            unfocusedLabelColor = VaultLime.copy(alpha = 0.7f),
                            focusedBorderColor = VaultLime,
                            unfocusedBorderColor = VaultSlate
                        )
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Button(
                        onClick = {
                            requireEditUnlock {
                                onSaveScriptConfig(selectedPreset, customCodeText)
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .testTag("save_script_config_button"),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = VaultObsidian,
                            contentColor = VaultLime
                        )
                    ) {
                        Icon(Icons.Default.Save, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Simpan Konfigurasi NYX2", style = MaterialTheme.typography.labelLarge)
                    }
                }
            }
        }
    }

    if (showSettingsEditUnlockDialog) {
        SettingsAdminUnlockDialog(
            securityConfig = securityConfig,
            onDismiss = { showSettingsEditUnlockDialog = false },
            onUnlocked = {
                isSettingsEditUnlocked = true
                showSettingsEditUnlockDialog = false
            }
        )
    }

    if (showCreateVaultDialog) {
        CreateNewVaultModalDialog(
            nextVaultId = securityConfig.vaultCount + 1,
            onDismiss = { showCreateVaultDialog = false },
            onCreateVault = { p1, p2, p3, customName ->
                showCreateVaultDialog = false
                onAddNewVault(p1, p2, p3, customName)
            }
        )
    }

    if (renamingVaultDialogTargetId != null) {
        val targetId = renamingVaultDialogTargetId!!
        RenameVaultModalDialog(
            vaultId = targetId,
            currentName = securityConfig.getVaultName(targetId),
            onDismiss = { renamingVaultDialogTargetId = null },
            onConfirmRename = { newName ->
                renamingVaultDialogTargetId = null
                onRenameVault(targetId, newName)
            }
        )
    }

    if (showGoogleAccountDialog) {
        LinkGoogleAccountModalDialog(
            currentEmail = securityConfig.googleAccountEmail,
            onDismiss = { showGoogleAccountDialog = false },
            onLinkAccount = { email ->
                showGoogleAccountDialog = false
                onLinkGoogleAccount(email)
            }
        )
    }
}

@Composable
private fun SettingsAdminUnlockDialog(
    securityConfig: SecurityConfig,
    onDismiss: () -> Unit,
    onUnlocked: () -> Unit
) {
    val context = LocalContext.current
    var enteredPin by remember { mutableStateOf("") }
    var pinPassed by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    fun launchDeviceBiometricOrCredential() {
        val act = context as? androidx.fragment.app.FragmentActivity
        val bioManager = androidx.biometric.BiometricManager.from(context)
        val authenticators = androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG or
                androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_WEAK or
                androidx.biometric.BiometricManager.Authenticators.DEVICE_CREDENTIAL

        val canAuth = bioManager.canAuthenticate(authenticators)
        if (act == null || canAuth != androidx.biometric.BiometricManager.BIOMETRIC_SUCCESS) {
            // Jika perangkat tidak memiliki sidik jari, wajah, maupun kunci layar (PIN/Pola) Android,
            // izinkan langsung setelah PIN Pengaturan terverifikasi!
            onUnlocked()
            return
        }

        try {
            val executor = androidx.core.content.ContextCompat.getMainExecutor(context)
            val prompt = androidx.biometric.BiometricPrompt(
                act,
                executor,
                object : androidx.biometric.BiometricPrompt.AuthenticationCallback() {
                    override fun onAuthenticationSucceeded(result: androidx.biometric.BiometricPrompt.AuthenticationResult) {
                        super.onAuthenticationSucceeded(result)
                        onUnlocked()
                    }

                    override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                        super.onAuthenticationError(errorCode, errString)
                        if (errorCode == androidx.biometric.BiometricPrompt.ERROR_NO_BIOMETRICS ||
                            errorCode == androidx.biometric.BiometricPrompt.ERROR_NO_DEVICE_CREDENTIAL ||
                            errorCode == androidx.biometric.BiometricPrompt.ERROR_HW_NOT_PRESENT ||
                            errorCode == androidx.biometric.BiometricPrompt.ERROR_HW_UNAVAILABLE
                        ) {
                            onUnlocked()
                        } else {
                            errorMessage = "Verifikasi perangkat dibatalkan: $errString"
                        }
                    }

                    override fun onAuthenticationFailed() {
                        super.onAuthenticationFailed()
                        errorMessage = "Sidik jari / wajah tidak cocok. Coba lagi atau gunakan PIN/Pola layar perangkat."
                    }
                }
            )
            val promptInfo = androidx.biometric.BiometricPrompt.PromptInfo.Builder()
                .setTitle("Verifikasi Keamanan Pengaturan Arcanum")
                .setSubtitle("Gunakan Sidik Jari, Verifikasi Wajah, atau PIN/Pola Kunci Layar Android Anda")
                .setAllowedAuthenticators(authenticators)
                .build()
            prompt.authenticate(promptInfo)
        } catch (_: Exception) {
            onUnlocked()
        }
    }

    Dialog(onDismissRequest = onDismiss) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .duolingoCard(
                    backgroundColor = DuoPureWhite,
                    borderColor = DuoBeetlePurple,
                    lipColor = DuoBeetlePurpleLip,
                    cornerRadius = 26.dp,
                    borderWidth = 2.5.dp,
                    lipHeight = 6.dp
                )
                .padding(bottom = 6.dp)
                .padding(20.dp)
                .testTag("settings_admin_unlock_dialog")
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(42.dp)
                            .duolingoButton(
                                faceColor = DuoBeetlePurple,
                                lipColor = DuoBeetlePurpleLip,
                                cornerRadius = 12.dp,
                                lipHeight = 3.dp
                            )
                            .padding(bottom = 3.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.Security, contentDescription = null, tint = DuoPureWhite, modifier = Modifier.size(22.dp))
                    }
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Buka Kunci Mode Edit Pengaturan",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.ExtraBold,
                            color = VaultObsidian
                        )
                        Text(
                            text = "Langkah 1: PIN Pengaturan • Langkah 2: Sidik Jari / Wajah / Kunci Layar Android",
                            style = MaterialTheme.typography.bodySmall,
                            color = CosmicTextSubtle
                        )
                    }
                }

                if (!pinPassed) {
                    OutlinedTextField(
                        value = enteredPin,
                        onValueChange = {
                            enteredPin = it.uppercase().take(6)
                            errorMessage = null
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("settings_admin_unlock_pin_input"),
                        label = { Text("Masukkan PIN Pengaturan (6 Karakter)") },
                        placeholder = { Text("Default: 222222") },
                        singleLine = true,
                        shape = RoundedCornerShape(14.dp)
                    )
                } else {
                    Surface(
                        color = DuoFeatherGreenSoft,
                        shape = RoundedCornerShape(14.dp),
                        border = androidx.compose.foundation.BorderStroke(1.5.dp, DuoFeatherGreen),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(Icons.Default.CheckCircle, contentDescription = null, tint = DuoFeatherGreenLip)
                            Text(
                                text = "PIN Pengaturan Terverifikasi! Lanjutkan verifikasi Sidik Jari / Wajah / Kunci Layar Perangkat.",
                                style = MaterialTheme.typography.bodySmall,
                                fontWeight = FontWeight.Bold,
                                color = VaultObsidian
                            )
                        }
                    }
                }

                if (errorMessage != null) {
                    Text(
                        text = errorMessage!!,
                        style = MaterialTheme.typography.bodySmall,
                        color = VaultCoral,
                        fontWeight = FontWeight.Bold
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedButton(
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(14.dp)
                    ) {
                        Text("Tetap View-Only", fontWeight = FontWeight.Bold)
                    }
                    Button(
                        onClick = {
                            if (!pinPassed) {
                                val clean = enteredPin.uppercase().trim()
                                val validSetPins = listOf(
                                    securityConfig.settingsPin1.uppercase().trim(),
                                    securityConfig.settingsPin2.uppercase().trim(),
                                    securityConfig.settingsPin3.uppercase().trim()
                                )
                                if (clean in validSetPins) {
                                    pinPassed = true
                                    errorMessage = null
                                    launchDeviceBiometricOrCredential()
                                } else {
                                    errorMessage = "PIN Pengaturan salah! Masukkan PIN Pengaturan yang benar."
                                }
                            } else {
                                launchDeviceBiometricOrCredential()
                            }
                        },
                        modifier = Modifier
                            .weight(1f)
                            .testTag("settings_admin_verify_button"),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = DuoFeatherGreen,
                            contentColor = DuoPureWhite
                        )
                    ) {
                        Icon(
                            imageVector = if (pinPassed) Icons.Default.Fingerprint else Icons.Default.LockOpen,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = if (pinPassed) "Verifikasi Perangkat" else "Verifikasi PIN",
                            fontWeight = FontWeight.ExtraBold
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun RenameVaultModalDialog(
    vaultId: Int,
    currentName: String,
    onDismiss: () -> Unit,
    onConfirmRename: (String) -> Unit
) {
    var nameText by remember(currentName) { mutableStateOf(currentName) }
    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = CosmicGlass),
            border = androidx.compose.foundation.BorderStroke(1.5.dp, DuoMacawBlue)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "Ubah Nama Brankas #$vaultId",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.ExtraBold,
                    color = CosmicTextPure
                )
                Text(
                    text = "Nama khusus ini akan tampil di Pengaturan, Dashboard Brankas, dan tab Cloud Google.",
                    style = MaterialTheme.typography.bodySmall,
                    color = CosmicTextSubtle
                )
                OutlinedTextField(
                    value = nameText,
                    onValueChange = { nameText = it.take(28) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("rename_vault_dialog_input"),
                    label = { Text("Nama Brankas #$vaultId") },
                    placeholder = { Text("Brankas #$vaultId") },
                    singleLine = true,
                    shape = RoundedCornerShape(14.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = CosmicTextPure,
                        unfocusedTextColor = CosmicTextPure,
                        focusedBorderColor = DuoMacawBlue,
                        unfocusedBorderColor = CosmicBorderHairline
                    )
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedButton(
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(14.dp)
                    ) {
                        Text("Batal")
                    }
                    Button(
                        onClick = { onConfirmRename(nameText.trim()) },
                        modifier = Modifier
                            .weight(1f)
                            .testTag("confirm_rename_vault_button"),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = DuoMacawBlue,
                            contentColor = DuoPureWhite
                        )
                    ) {
                        Text("Simpan Nama", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
private fun PinLayerTextField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    tag: String
) {
    OnboardingPinField(
        label = label,
        value = value,
        onValueChange = onValueChange,
        tag = tag
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VaultFileInspectorSheet(
    item: VaultItemEntity,
    onDismiss: () -> Unit,
    onToggleEncryptWithPin: () -> Unit,
    onOpenOrPlayMedia: () -> Unit,
    onDelete: () -> Unit
) {
    val categoryEnum = VaultCategory.entries.find { it.name == item.category } ?: VaultCategory.DOCUMENT

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = VaultObsidian,
        contentColor = VaultPureWhite,
        shape = RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 22.dp, vertical = 10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    color = if (item.isEncrypted) VaultLime else VaultMintCard,
                    shape = RoundedCornerShape(50)
                ) {
                    Text(
                        text = if (item.isEncrypted) "NYX2 SEALED (${item.cipherAlgorithm})" else "TERDEKRIPSI SIAP BUKA",
                        style = MaterialTheme.typography.labelMedium,
                        color = VaultObsidian,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                    )
                }
                IconButton(onClick = onDismiss) {
                    Icon(Icons.Default.Close, contentDescription = "Tutup", tint = VaultPureWhite)
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "${item.name}.${item.extension}",
                style = MaterialTheme.typography.headlineMedium,
                color = VaultPureWhite
            )
            Text(
                text = "Ruang Terisolasi • ${categoryEnum.label} • Ukuran: ${formatFileSize(item.sizeBytes)} • ${item.checksumPreview}",
                style = MaterialTheme.typography.labelMedium,
                color = VaultLime
            )

            Spacer(modifier = Modifier.height(14.dp))

            Surface(
                color = VaultCharcoal,
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(
                        text = "HEADER 77-BYTE NYX2 & CIPHERTEXT STREAM:",
                        style = MaterialTheme.typography.labelMedium,
                        color = VaultMintCard
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = item.encryptedHexPreview,
                        fontFamily = JetBrainsMonoFontFamily,
                        fontSize = 12.sp,
                        color = VaultPureWhite
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Surface(
                color = VaultCharcoal,
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(
                        text = if (item.isEncrypted) "PROTEKSI DECOY EMPTY_ZIP AKTIF:" else "ISI FILE ASLI (TERDEKRIPSI):",
                        style = MaterialTheme.typography.labelMedium,
                        color = if (item.isEncrypted) VaultAmber else VaultLime
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = if (item.isEncrypted) {
                            "Terkunci dengan ChaCha20-Poly1305 -> AES-256-GCM.\nJika dibuka paksa tanpa PIN yang benar, sistem akan menghasilkan EMPTY_ZIP (${EncryptionScriptEngine.EMPTY_ZIP_HEX})."
                        } else {
                            item.rawContentPreview
                        },
                        fontFamily = JetBrainsMonoFontFamily,
                        fontSize = 12.sp,
                        color = VaultPureWhite.copy(alpha = 0.9f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Button(
                    onClick = {
                        onDismiss()
                        onToggleEncryptWithPin()
                    },
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp)
                        .testTag("sheet_toggle_encrypt_button"),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = VaultLime,
                        contentColor = VaultObsidian
                    )
                ) {
                    Icon(
                        imageVector = if (item.isEncrypted) Icons.Default.LockOpen else Icons.Default.Lock,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (item.isEncrypted) "PIN Dekripsi" else "PIN Enkripsi",
                        style = MaterialTheme.typography.labelLarge
                    )
                }

                Button(
                    onClick = {
                        onDismiss()
                        onOpenOrPlayMedia()
                    },
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp)
                        .testTag("sheet_play_media_button"),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = VaultMintAccent,
                        contentColor = VaultPureWhite
                    )
                ) {
                    Icon(Icons.Default.PlayArrow, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Buka / Tonton", style = MaterialTheme.typography.labelLarge)
                }
            }

            Spacer(modifier = Modifier.height(22.dp))
        }
    }
}

@Composable
private fun CreateNewVaultModalDialog(
    nextVaultId: Int,
    onDismiss: () -> Unit,
    onCreateVault: (pin1: String, pin2: String, pin3: String, customName: String) -> Unit
) {
    val defaultPin = "${(nextVaultId + 1).coerceIn(2, 9)}".repeat(6).take(6)
    var customName by remember { mutableStateOf("Brankas #$nextVaultId") }
    var p1 by remember { mutableStateOf(defaultPin) }
    var p2 by remember { mutableStateOf(defaultPin) }
    var p3 by remember { mutableStateOf(defaultPin) }
    var errorMsg by remember { mutableStateOf<String?>(null) }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(26.dp),
            colors = CardDefaults.cardColors(containerColor = CosmicGlass),
            border = androidx.compose.foundation.BorderStroke(1.dp, CosmicGradientGlassHighlight)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    text = "Tambah Ruang Brankas #$nextVaultId",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = CosmicTextPure
                )
                Text(
                    text = "Beri nama brankas dan atur 3 lapis PIN alfanumerik (6 karakter A-Z / 0-9) untuk brankas baru ini.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = CosmicTextSubtle
                )

                OutlinedTextField(
                    value = customName,
                    onValueChange = { customName = it.take(28) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("new_vault_name_input"),
                    label = { Text("Nama Brankas #$nextVaultId") },
                    placeholder = { Text("Misal: Brankas Pribadi / Rahasia") },
                    singleLine = true,
                    shape = RoundedCornerShape(14.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = CosmicTextPure,
                        unfocusedTextColor = CosmicTextPure,
                        focusedBorderColor = VaultLime,
                        unfocusedBorderColor = CosmicBorderHairline
                    )
                )

                PinLayerTextField(
                    label = "PIN Lapis 1 (6 Karakter)",
                    value = p1,
                    onValueChange = { p1 = it.uppercase().take(6) },
                    tag = "new_vault_pin1"
                )
                PinLayerTextField(
                    label = "PIN Lapis 2 (6 Karakter)",
                    value = p2,
                    onValueChange = { p2 = it.uppercase().take(6) },
                    tag = "new_vault_pin2"
                )
                PinLayerTextField(
                    label = "PIN Lapis 3 (6 Karakter)",
                    value = p3,
                    onValueChange = { p3 = it.uppercase().take(6) },
                    tag = "new_vault_pin3"
                )

                if (errorMsg != null) {
                    Text(
                        text = errorMsg!!,
                        style = MaterialTheme.typography.bodySmall,
                        color = VaultCoral,
                        fontWeight = FontWeight.Bold
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedButton(
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(14.dp)
                    ) {
                        Text("Batal")
                    }
                    Button(
                        onClick = {
                            val valid = SecurityConfig.isAlphanumericValidPin(p1) &&
                                    SecurityConfig.isAlphanumericValidPin(p2) &&
                                    SecurityConfig.isAlphanumericValidPin(p3)
                            if (valid) {
                                onCreateVault(p1, p2, p3, customName.trim())
                            } else {
                                errorMsg = "Semua PIN wajib 6 karakter huruf kapital (A-Z) atau angka (0-9)!"
                            }
                        },
                        modifier = Modifier
                            .weight(1f)
                            .testTag("confirm_create_vault_button"),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = VaultLime,
                            contentColor = VaultObsidian
                        )
                    ) {
                        Text("Buat Brankas", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
private fun LinkGoogleAccountModalDialog(
    currentEmail: String,
    onDismiss: () -> Unit,
    onLinkAccount: (email: String) -> Unit
) {
    ArcanumGoogleSignInDialog(
        currentEmail = currentEmail,
        currentName = "",
        isMandatoryGate = false,
        onDismiss = onDismiss,
        onSignedIn = { email, _ ->
            onLinkAccount(email)
        }
    )
}

@Composable
fun CreateVaultFileDialog(
    onDismiss: () -> Unit,
    onCreateFile: (title: String, ext: String, category: VaultCategory, payload: String, encryptNow: Boolean) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var extension by remember { mutableStateOf("mp4") }
    var selectedCategory by remember { mutableStateOf(VaultCategory.VIDEO) }
    var payload by remember { mutableStateOf("") }
    var encryptImmediately by remember { mutableStateOf(false) }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(26.dp),
            colors = CardDefaults.cardColors(containerColor = VaultPureWhite)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(18.dp)
            ) {
                Text(
                    text = "Buat File / Media Demo Baru",
                    style = MaterialTheme.typography.titleLarge,
                    color = VaultObsidian
                )
                Text(
                    text = "Pilih format MP4, MOV, JPG, PNG, WEBP, GIF, Sticker, atau Dokumen NYX.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = VaultTextMuted
                )

                Spacer(modifier = Modifier.height(12.dp))

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = title,
                        onValueChange = { title = it },
                        modifier = Modifier
                            .weight(1f)
                            .testTag("dialog_file_name_input"),
                        label = { Text("Nama File") },
                        placeholder = { Text("Demo_Clip_4K") },
                        singleLine = true,
                        shape = RoundedCornerShape(14.dp)
                    )
                    OutlinedTextField(
                        value = extension,
                        onValueChange = { extension = it },
                        modifier = Modifier.width(96.dp),
                        label = { Text("Format") },
                        placeholder = { Text("mp4") },
                        singleLine = true,
                        shape = RoundedCornerShape(14.dp)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    listOf(
                        Triple(VaultCategory.VIDEO, "Video", "mp4"),
                        Triple(VaultCategory.IMAGE, "GIF/Img", "gif"),
                        Triple(VaultCategory.IMAGE, "Sticker", "sticker"),
                        Triple(VaultCategory.AUDIO, "Audio", "flac")
                    ).forEach { (cat, shortLabel, defaultExt) ->
                        FilterChip(
                            selected = extension == defaultExt,
                            onClick = {
                                selectedCategory = cat
                                extension = defaultExt
                            },
                            label = { Text(shortLabel, fontSize = 11.sp) },
                            shape = RoundedCornerShape(50),
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = VaultObsidian,
                                selectedLabelColor = VaultLime
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = payload,
                    onValueChange = { payload = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(88.dp)
                        .testTag("dialog_file_payload_input"),
                    label = { Text("Metadata / Isi Rahasia") },
                    placeholder = { Text("Stream 4K HDR atau catatan rahasia...") },
                    shape = RoundedCornerShape(14.dp)
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Kunci Enkripsi NYX2 Saat Disimpan",
                        style = MaterialTheme.typography.labelLarge,
                        color = VaultObsidian
                    )
                    Switch(
                        checked = encryptImmediately,
                        onCheckedChange = { encryptImmediately = it },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = VaultLime,
                            checkedTrackColor = VaultObsidian
                        )
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedButton(
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(14.dp)
                    ) {
                        Text("Batal", color = VaultObsidian)
                    }
                    Button(
                        onClick = {
                            onCreateFile(title, extension, selectedCategory, payload, encryptImmediately)
                            onDismiss()
                        },
                        modifier = Modifier
                            .weight(1f)
                            .testTag("dialog_confirm_save_button"),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = VaultObsidian,
                            contentColor = VaultLime
                        )
                    ) {
                        Text("Simpan File", style = MaterialTheme.typography.labelLarge)
                    }
                }
            }
        }
    }
}

@Composable
private fun MinimalistSettingsAccordionHeader(
    title: String,
    subtitle: String,
    badgeText: String,
    accentColor: androidx.compose.ui.graphics.Color,
    expanded: Boolean,
    onToggle: () -> Unit,
    tag: String
) {
    Surface(
        color = if (expanded) CosmicObsidian else CosmicGlass,
        shape = RoundedCornerShape(18.dp),
        border = androidx.compose.foundation.BorderStroke(
            width = if (expanded) 2.dp else 1.dp,
            color = if (expanded) accentColor else CosmicBorderHairline
        ),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onToggle() }
            .testTag(tag)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.ExtraBold,
                        color = CosmicTextPure
                    )
                    Surface(
                        color = accentColor.copy(alpha = 0.2f),
                        shape = RoundedCornerShape(6.dp)
                    ) {
                        Text(
                            text = badgeText,
                            style = MaterialTheme.typography.labelSmall,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = accentColor,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = CosmicTextSubtle,
                    maxLines = 1
                )
            }

            Surface(
                color = if (expanded) accentColor else CosmicObsidian,
                shape = RoundedCornerShape(10.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, accentColor.copy(alpha = 0.6f))
            ) {
                Text(
                    text = if (expanded) "TUTUP" else "BUKA",
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                    color = if (expanded) VaultObsidian else accentColor,
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                )
            }
        }
    }
}

