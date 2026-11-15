package com.example.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.Canvas
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
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.DesktopWindows
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Extension
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.VpnKey
import androidx.compose.material.icons.filled.VpnLock
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.JetBrainsMonoFontFamily

// ============================================================================
// AUTHENTIC BROWSER MODELS, PALETTES & SETTINGS FOR CHROME + BRAVE + TOR
// ============================================================================

enum class BrowserUiProfile(
    val id: String,
    val displayName: String,
    val tagline: String,
    val toolbarBg: Color,
    val omniboxBg: Color,
    val pageBg: Color,
    val cardBg: Color,
    val accentPrimary: Color,
    val accentSecondary: Color,
    val textPrimary: Color,
    val textSecondary: Color
) {
    CHROME(
        id = "CHROME",
        displayName = "Chrome",
        tagline = "Material You • Fast & Sync Ready",
        toolbarBg = Color(0xFF202124),
        omniboxBg = Color(0xFF303134),
        pageBg = Color(0xFF202124),
        cardBg = Color(0xFF292A2D),
        accentPrimary = Color(0xFF8AB4F8),
        accentSecondary = Color(0xFF81C995),
        textPrimary = Color(0xFFE8EAED),
        textSecondary = Color(0xFF9AA0A6)
    ),
    BRAVE(
        id = "BRAVE",
        displayName = "Brave",
        tagline = "Shields Lion • Aggressive AdBlock",
        toolbarBg = Color(0xFF17191E),
        omniboxBg = Color(0xFF23262E),
        pageBg = Color(0xFF12141A),
        cardBg = Color(0xFF1E2028),
        accentPrimary = Color(0xFFFB542B),
        accentSecondary = Color(0xFFA428D2),
        textPrimary = Color(0xFFF0F2F5),
        textSecondary = Color(0xFF8D93A1)
    ),
    TOR(
        id = "TOR",
        displayName = "Tor",
        tagline = "Onion 3-Hop • Zero-Trace Circuit",
        toolbarBg = Color(0xFF191224),
        omniboxBg = Color(0xFF261D35),
        pageBg = Color(0xFF120D1B),
        cardBg = Color(0xFF20182E),
        accentPrimary = Color(0xFFC084FC),
        accentSecondary = Color(0xFF30D158),
        textPrimary = Color(0xFFF3E8FF),
        textSecondary = Color(0xFFA799B7)
    )
}

enum class BrowserSearchEngine(
    val id: String,
    val displayName: String,
    val searchUrlTemplate: String,
    val badge: String
) {
    DUCKDUCKGO("DDG", "DuckDuckGo", "https://duckduckgo.com/?q=%s", "DDG"),
    BRAVE_SEARCH("BRAVE", "Brave Search", "https://search.brave.com/search?q=%s", "BRV"),
    GOOGLE("GOOGLE", "Google", "https://www.google.com/search?q=%s", "G"),
    STARTPAGE("STARTPAGE", "Startpage Privacy", "https://www.startpage.com/do/search?q=%s", "SP"),
    DUCKDUCKGO_ONION("DDG_ONION", "DuckDuckGo Onion v3", "https://duckduckgogg42xjoc72x3sjasowoarfbgcmvfimaftt6twagswzczad.onion/?q=%s", "TOR")
}

enum class BraveBlockingAggressiveness(val label: String, val desc: String) {
    AGGRESSIVE("Agresif", "Blokir pelacak, iklan pihak pertama & sidik jari ketat"),
    STANDARD("Standar", "Blokir iklan & pelacak pihak ketiga tanpa merusak situs"),
    DISABLED("Nonaktif", "Izinkan semua elemen halaman dimuat")
}

enum class TorBridgeMode(val label: String, val desc: String) {
    NONE("Tidak Ada (Sirkuit Standar)", "Koneksi langsung ke Entry Guard Tor"),
    SNOWFLAKE("Snowflake (WebRTC)", "Menyamarkan lalu lintas Tor sebagai panggilan WebRTC"),
    OBFS4("obfs4 (Built-in Bridge)", "Menghindari sensor DPI jaringan dengan obfuskasi acak"),
    MEEK_AZURE("meek-azure (Domain Fronting)", "Rute penyamaran melalui TLS cloud CDN")
}

data class BrowserBookmarkEntry(
    val id: String,
    val title: String,
    val url: String,
    val badgeCode: String,
    val accentHex: Long
)

data class BrowserHistoryRecord(
    val id: String,
    val title: String,
    val url: String,
    val timestampText: String
)

data class BrowserDownloadRecord(
    val id: String,
    val fileName: String,
    val sourceUrl: String,
    val sizeLabel: String,
    val statusLabel: String
)

data class BrowserSpeedDialShortcut(
    val title: String,
    val url: String,
    val shortBadge: String,
    val circleColor: Color
)

data class BrowserUnifiedConfig(
    val uiProfile: BrowserUiProfile = BrowserUiProfile.BRAVE,
    val searchEngine: BrowserSearchEngine = BrowserSearchEngine.BRAVE_SEARCH,
    val homepageEnabled: Boolean = true,
    val customHomepageUrl: String = "https://search.brave.com",
    val showBottomToolbar: Boolean = true,
    val autofillAndPasswordsEnabled: Boolean = true,
    val shieldsMasterEnabled: Boolean = true,
    val trackerBlockingMode: BraveBlockingAggressiveness = BraveBlockingAggressiveness.AGGRESSIVE,
    val httpsUpgradeStrict: Boolean = true,
    val blockScriptsNoScript: Boolean = false,
    val blockFingerprintingStrict: Boolean = true,
    val blockCrossSiteCookies: Boolean = true,
    val forgetfulBrowsing: Boolean = true,
    val stripTrackingParams: Boolean = true,
    val torSecurityLevel: TorSecurityLevel = TorSecurityLevel.SAFER,
    val torBridgeMode: TorBridgeMode = TorBridgeMode.OBFS4,
    val stealthVpnEnabled: Boolean = true,
    val vpnRegion: StealthVpnRegion = StealthVpnRegion.SWITZERLAND,
    val vpnKillSwitch: Boolean = true,
    val doubleHopVpn: Boolean = true,
    val dohProvider: DohProviderSpec = DohProviderSpec.QUAD9_SECURITY,
    val textZoomPercent: Int = 100,
    val desktopSiteDefault: Boolean = false
)

// Custom Authentic Brand Vectors (Brave Lion Shield & Tor Onion Emblem)
@Composable
fun BraveLionShieldGlyph(
    active: Boolean,
    modifier: Modifier = Modifier
) {
    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height
        val shieldPath = Path().apply {
            moveTo(w * 0.5f, h * 0.06f)
            lineTo(w * 0.90f, h * 0.22f)
            lineTo(w * 0.84f, h * 0.65f)
            lineTo(w * 0.50f, h * 0.95f)
            lineTo(w * 0.16f, h * 0.65f)
            lineTo(w * 0.10f, h * 0.22f)
            close()
        }
        drawPath(
            path = shieldPath,
            brush = if (active) {
                Brush.verticalGradient(listOf(Color(0xFFFB542B), Color(0xFFFF1B2D)))
            } else {
                SolidColor(Color(0xFF5F6368))
            }
        )
        // Stylized lion mane / nose geometric accents
        val innerCrown = Path().apply {
            moveTo(w * 0.30f, h * 0.35f)
            lineTo(w * 0.50f, h * 0.25f)
            lineTo(w * 0.70f, h * 0.35f)
            lineTo(w * 0.62f, h * 0.60f)
            lineTo(w * 0.50f, h * 0.74f)
            lineTo(w * 0.38f, h * 0.60f)
            close()
        }
        drawPath(
            path = innerCrown,
            color = Color.White.copy(alpha = 0.92f),
            style = Stroke(width = w * 0.075f, cap = StrokeCap.Round)
        )
    }
}

@Composable
fun TorOnionGlyph(
    connected: Boolean,
    modifier: Modifier = Modifier
) {
    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height
        val cx = w * 0.5f
        val cy = h * 0.54f
        val r = w.coerceAtMost(h) * 0.38f
        val ringColor = if (connected) Color(0xFFC084FC) else Color(0xFF7D7D85)
        // Outer circle + onion stem
        drawCircle(
            color = ringColor.copy(alpha = 0.20f),
            radius = r,
            center = Offset(cx, cy)
        )
        drawCircle(
            color = ringColor,
            radius = r,
            center = Offset(cx, cy),
            style = Stroke(width = w * 0.08f)
        )
        drawCircle(
            color = ringColor,
            radius = r * 0.55f,
            center = Offset(cx, cy),
            style = Stroke(width = w * 0.07f)
        )
        drawLine(
            color = Color(0xFF30D158),
            start = Offset(cx, cy - r * 0.95f),
            end = Offset(cx + w * 0.08f, h * 0.08f),
            strokeWidth = w * 0.09f,
            cap = StrokeCap.Round
        )
    }
}

// ============================================================================
// 1. AUTHENTIC CHROME / BRAVE / TOR FULL-SCREEN SETTINGS ("SETELAN")
// ============================================================================

@Composable
fun BrowserSettingsFullScreen(
    config: BrowserUnifiedConfig,
    activeCircuit: List<OnionRelayHop>,
    blockedAdsCount: Int,
    strippedParamsCount: Int,
    onUpdateConfig: (BrowserUnifiedConfig) -> Unit,
    onRotateCircuit: () -> Unit,
    onClearBrowsingData: (clearHistory: Boolean, clearCookies: Boolean, clearCache: Boolean) -> Unit,
    onBackToBrowser: () -> Unit,
    onCloseToSanctuary: () -> Unit,
    onPostBanner: (String) -> Unit
) {
    val profile = config.uiProfile
    var searchQuery by remember { mutableStateOf("") }
    var showSearchField by remember { mutableStateOf(false) }

    // Interactive expandable sub-pickers (authentic Android Chrome/Brave/Tor Preference Panels)
    var expandedSection by remember { mutableStateOf<String?>(null) }
    var showClearDataDialog by remember { mutableStateOf(false) }

    var clearHistCheck by remember { mutableStateOf(true) }
    var clearCookiesCheck by remember { mutableStateOf(true) }
    var clearCacheCheck by remember { mutableStateOf(true) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(profile.pageBg)
            .navigationBarsPadding()
            .testTag("browser_settings_fullscreen")
    ) {
        // Authentic Material Top App Bar (← Setelan    [Search] [X Sanctuary])
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(profile.toolbarBg)
                .padding(horizontal = 8.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            IconButton(
                onClick = onBackToBrowser,
                modifier = Modifier.size(44.dp)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Kembali ke Browser",
                    tint = profile.textPrimary
                )
            }

            if (showSearchField) {
                BasicTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    singleLine = true,
                    textStyle = TextStyle(color = profile.textPrimary, fontSize = 15.sp),
                    cursorBrush = SolidColor(profile.accentPrimary),
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(999.dp))
                        .background(profile.omniboxBg)
                        .padding(horizontal = 14.dp, vertical = 8.dp),
                    decorationBox = { inner ->
                        Box(contentAlignment = Alignment.CenterStart) {
                            if (searchQuery.isEmpty()) {
                                Text("Cari setelan...", color = profile.textSecondary, fontSize = 14.sp)
                            }
                            inner()
                        }
                    }
                )
            } else {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Setelan Arcanum",
                        fontSize = 19.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = profile.textPrimary
                    )
                    Text(
                        text = "Arcanum Browser • ${profile.displayName} UI & Tor/Brave Core",
                        fontSize = 11.sp,
                        color = profile.textSecondary
                    )
                }
            }

            IconButton(
                onClick = {
                    showSearchField = !showSearchField
                    if (!showSearchField) searchQuery = ""
                },
                modifier = Modifier.size(40.dp)
            ) {
                Icon(
                    imageVector = if (showSearchField) Icons.Default.Close else Icons.Default.Search,
                    contentDescription = "Cari Setelan",
                    tint = profile.textPrimary
                )
            }

            // Explicit X button to return directly to Sanctuary
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFFF453A).copy(alpha = 0.18f))
                    .border(0.8.dp, Color(0xFFFF453A).copy(alpha = 0.55f), CircleShape)
                    .clickable { onCloseToSanctuary() }
                    .testTag("browser_settings_exit_to_sanctuary"),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Tutup Browser & Kembali ke Sanctuary",
                    tint = Color(0xFFFF453A),
                    modifier = Modifier.size(18.dp)
                )
            }
        }

        HorizontalDivider(thickness = 0.5.dp, color = Color.White.copy(alpha = 0.09f))

        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(bottom = 24.dp)
        ) {
            // 1. PROFILE & ENGINE SWITCHER BANNER (CHROME SYNC / BRAVE PROFILE / TOR IDENTITY)
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .clip(RoundedCornerShape(18.dp))
                    .background(profile.cardBg)
                    .border(0.6.dp, profile.accentPrimary.copy(alpha = 0.35f), RoundedCornerShape(18.dp))
                    .padding(14.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(42.dp)
                            .clip(CircleShape)
                            .background(profile.accentPrimary.copy(alpha = 0.18f)),
                        contentAlignment = Alignment.Center
                    ) {
                        when (profile) {
                            BrowserUiProfile.BRAVE -> BraveLionShieldGlyph(
                                active = config.shieldsMasterEnabled,
                                modifier = Modifier.size(24.dp)
                            )
                            BrowserUiProfile.TOR -> TorOnionGlyph(
                                connected = true,
                                modifier = Modifier.size(26.dp)
                            )
                            BrowserUiProfile.CHROME -> Icon(
                                imageVector = Icons.Default.Public,
                                contentDescription = null,
                                tint = profile.accentPrimary,
                                modifier = Modifier.size(22.dp)
                            )
                        }
                    }
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Arcanum Browser • Profil ${profile.displayName}",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = profile.textPrimary
                        )
                        Text(
                            text = "$blockedAdsCount iklan/pelacak diblokir • $strippedParamsCount URL dibersihkan",
                            fontSize = 11.sp,
                            color = profile.textSecondary
                        )
                    }
                }

                // 3-Engine Quick Identity Switcher
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    BrowserUiProfile.entries.forEach { p ->
                        val isSel = p == profile
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(12.dp))
                                .background(
                                    if (isSel) p.accentPrimary.copy(alpha = 0.22f)
                                    else profile.omniboxBg
                                )
                                .border(
                                    width = if (isSel) 1.dp else 0.5.dp,
                                    color = if (isSel) p.accentPrimary else Color.White.copy(alpha = 0.08f),
                                    shape = RoundedCornerShape(12.dp)
                                )
                                .clickable {
                                    onUpdateConfig(config.copy(uiProfile = p))
                                    onPostBanner("Gaya UI Browser dialihkan ke: ${p.displayName}")
                                }
                                .padding(vertical = 8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = p.displayName,
                                fontSize = 12.sp,
                                fontWeight = if (isSel) FontWeight.ExtraBold else FontWeight.Medium,
                                color = if (isSel) p.accentPrimary else profile.textSecondary
                            )
                        }
                    }
                }
            }

            fun matchesFilter(title: String, subtitle: String = ""): Boolean {
                if (searchQuery.isBlank()) return true
                val q = searchQuery.trim().lowercase()
                return title.lowercase().contains(q) || subtitle.lowercase().contains(q)
            }

            // ----------------------------------------------------------------
            // SECTION: DASAR (BASICS — CHROME STYLE)
            // ----------------------------------------------------------------
            val context = androidx.compose.ui.platform.LocalContext.current
            SettingsCategoryHeader("Dasar & Browser Default Sistem", profile.accentPrimary)

            if (matchesFilter("Browser default", "auto buka link luar force close restart pin")) {
                SettingsListItem(
                    icon = Icons.Default.Public,
                    title = "Jadikan Arcanum Browser Default (Auto-Buka Link Luar)",
                    subtitle = "Ketuk untuk menjadikan Arcanum sebagai browser utama saat membuka link dari luar aplikasi. Saat terbuka dari link luar, akses PIN & Brankas otomatis dikunci sampai aplikasi di-Force Close / Restart.",
                    textPrimary = Color(0xFF38BDF8),
                    textSecondary = profile.textSecondary,
                    onClick = {
                        ExternalBrowserLockdownController.requestDefaultBrowserRole(context, onPostBanner)
                    }
                )
            }

            if (matchesFilter("Mesin telusur", config.searchEngine.displayName)) {
                SettingsListItem(
                    icon = Icons.Default.Search,
                    title = "Mesin telusur",
                    subtitle = config.searchEngine.displayName,
                    textPrimary = profile.textPrimary,
                    textSecondary = profile.textSecondary,
                    onClick = {
                        expandedSection = if (expandedSection == "SEARCH") null else "SEARCH"
                    }
                )
                AnimatedVisibility(visible = expandedSection == "SEARCH") {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(profile.cardBg)
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                    ) {
                        BrowserSearchEngine.entries.forEach { se ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        onUpdateConfig(config.copy(searchEngine = se))
                                        expandedSection = null
                                        onPostBanner("Mesin telusur diatur ke: ${se.displayName}")
                                    }
                                    .padding(vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                RadioButton(
                                    selected = config.searchEngine == se,
                                    onClick = {
                                        onUpdateConfig(config.copy(searchEngine = se))
                                        expandedSection = null
                                    },
                                    colors = RadioButtonDefaults.colors(selectedColor = profile.accentPrimary)
                                )
                                Text(se.displayName, color = profile.textPrimary, fontSize = 13.sp)
                            }
                        }
                    }
                }
            }

            if (matchesFilter("Halaman beranda", config.customHomepageUrl)) {
                SettingsSwitchItem(
                    icon = Icons.Default.Home,
                    title = "Halaman beranda",
                    subtitle = if (config.homepageEnabled) config.customHomepageUrl else "Nonaktif (Gunakan Tab Baru)",
                    checked = config.homepageEnabled,
                    accentColor = profile.accentPrimary,
                    textPrimary = profile.textPrimary,
                    textSecondary = profile.textSecondary,
                    onCheckedChange = { onUpdateConfig(config.copy(homepageEnabled = it)) },
                    onRowClick = {
                        expandedSection = if (expandedSection == "HOMEPAGE") null else "HOMEPAGE"
                    }
                )
                AnimatedVisibility(visible = expandedSection == "HOMEPAGE") {
                    OutlinedTextField(
                        value = config.customHomepageUrl,
                        onValueChange = { onUpdateConfig(config.copy(customHomepageUrl = it)) },
                        label = { Text("URL Halaman Beranda", fontSize = 11.sp) },
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 6.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = profile.textPrimary,
                            unfocusedTextColor = profile.textPrimary,
                            focusedBorderColor = profile.accentPrimary,
                            unfocusedBorderColor = Color.White.copy(alpha = 0.2f)
                        )
                    )
                }
            }

            if (matchesFilter("Sandi & Isi Otomatis", "Zero-Knowledge Sanctuary Autofill")) {
                SettingsSwitchItem(
                    icon = Icons.Default.VpnKey,
                    title = "Sandi & Pengisian Otomatis",
                    subtitle = "Isolasi sandi di Brankas Sanctuary tanpa sinkronisasi cloud eksternal",
                    checked = config.autofillAndPasswordsEnabled,
                    accentColor = profile.accentPrimary,
                    textPrimary = profile.textPrimary,
                    textSecondary = profile.textSecondary,
                    onCheckedChange = {
                        onUpdateConfig(config.copy(autofillAndPasswordsEnabled = it))
                    }
                )
            }

            if (matchesFilter("Bilah navigasi bawah", "Chrome/Brave Bottom Toolbar")) {
                SettingsSwitchItem(
                    icon = Icons.Default.Language,
                    title = "Bilah navigasi bawah",
                    subtitle = "Tampilkan tombol Maju, Mundur, Tab & Menu di bagian bawah",
                    checked = config.showBottomToolbar,
                    accentColor = profile.accentPrimary,
                    textPrimary = profile.textPrimary,
                    textSecondary = profile.textSecondary,
                    onCheckedChange = { onUpdateConfig(config.copy(showBottomToolbar = it)) }
                )
            }

            HorizontalDivider(
                modifier = Modifier.padding(vertical = 8.dp),
                thickness = 0.5.dp,
                color = Color.White.copy(alpha = 0.08f)
            )

            // ----------------------------------------------------------------
            // SECTION: ARCANUM PRO • PENGUBAH DOMAIN (CUSTOM DOMAIN ALIAS)
            // ----------------------------------------------------------------
            SettingsCategoryHeader("Arcanum Pro • Pengubah Domain", Color(0xFFC084FC))

            val isProUnlocked = com.example.ui.theme.AppThemeController.isProModeUnlocked
            val activeAliasRules = com.example.ui.theme.AppThemeController.proBrowserDomainAliases
            if (matchesFilter("Pengubah Domain Custom Alias", "google.com g.start Pro redirect")) {
                SettingsListItem(
                    icon = Icons.Default.Public,
                    title = "Pengubah Domain Custom (Domain Alias)",
                    subtitle = if (isProUnlocked) {
                        val activeCount = activeAliasRules.count { it.isEnabled }
                        "$activeCount aturan aktif (contoh: g.start → google.com) • Ketuk untuk kelola"
                    } else {
                        "Terkunci (Khusus Mode Pro) — Aktifkan di PIN 222-222-222 tab Mode Pro"
                    },
                    textPrimary = profile.textPrimary,
                    textSecondary = if (isProUnlocked) Color(0xFF30D158) else Color(0xFFFF9F0A),
                    onClick = {
                        expandedSection = if (expandedSection == "DOMAIN_ALIAS") null else "DOMAIN_ALIAS"
                    }
                )
                AnimatedVisibility(visible = expandedSection == "DOMAIN_ALIAS") {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 6.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(profile.cardBg)
                            .border(0.7.dp, Color(0xFFC084FC).copy(alpha = 0.45f), RoundedCornerShape(16.dp))
                            .padding(14.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        if (isProUnlocked) {
                            ProBrowserDomainAliasEditorSection(
                                isDarkSurface = true,
                                onFeedback = onPostBanner
                            )
                        } else {
                            Text(
                                text = "Fitur Eksklusif Mode Pro",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFFFF9F0A)
                            )
                            Text(
                                text = "Pengalihan & penyamaran domain (misal buka https://g.start yang aslinya memuat https://google.com) hanya tersedia setelah Mode Pro diaktifkan lewat kode di Pengaturan Terisolasi (PIN 222-222-222 → Tab Mode Pro di samping Radar).",
                                fontSize = 11.5.sp,
                                color = profile.textSecondary
                            )
                        }
                    }
                }
            }

            HorizontalDivider(
                modifier = Modifier.padding(vertical = 8.dp),
                thickness = 0.5.dp,
                color = Color.White.copy(alpha = 0.08f)
            )

            // ----------------------------------------------------------------
            // SECTION: BRAVE SHIELDS & PRIVASI
            // ----------------------------------------------------------------
            SettingsCategoryHeader("Brave Shields & Privasi", Color(0xFFFB542B))

            if (matchesFilter("Perisai Brave Master", "Blokir iklan & pelacak otomatis")) {
                SettingsSwitchItem(
                    icon = Icons.Default.Shield,
                    title = "Brave Shields Global",
                    subtitle = if (config.shieldsMasterEnabled) "Aktif — $blockedAdsCount pelacak telah diblokir" else "Nonaktif",
                    checked = config.shieldsMasterEnabled,
                    accentColor = Color(0xFFFB542B),
                    textPrimary = profile.textPrimary,
                    textSecondary = profile.textSecondary,
                    onCheckedChange = { onUpdateConfig(config.copy(shieldsMasterEnabled = it)) }
                )
            }

            if (matchesFilter("Pemblokiran pelacak & iklan", config.trackerBlockingMode.label)) {
                SettingsListItem(
                    icon = Icons.Default.Security,
                    title = "Pemblokiran pelacak & iklan",
                    subtitle = config.trackerBlockingMode.label,
                    textPrimary = profile.textPrimary,
                    textSecondary = profile.textSecondary,
                    onClick = {
                        expandedSection = if (expandedSection == "TRACKERS") null else "TRACKERS"
                    }
                )
                AnimatedVisibility(visible = expandedSection == "TRACKERS") {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(profile.cardBg)
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                    ) {
                        BraveBlockingAggressiveness.entries.forEach { mode ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        onUpdateConfig(config.copy(trackerBlockingMode = mode))
                                        expandedSection = null
                                    }
                                    .padding(vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                RadioButton(
                                    selected = config.trackerBlockingMode == mode,
                                    onClick = {
                                        onUpdateConfig(config.copy(trackerBlockingMode = mode))
                                        expandedSection = null
                                    },
                                    colors = RadioButtonDefaults.colors(selectedColor = Color(0xFFFB542B))
                                )
                                Column {
                                    Text(mode.label, color = profile.textPrimary, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                                    Text(mode.desc, color = profile.textSecondary, fontSize = 11.sp)
                                }
                            }
                        }
                    }
                }
            }

            if (matchesFilter("Upgrade koneksi ke HTTPS", "Paksa HTTPS-Only")) {
                SettingsSwitchItem(
                    icon = Icons.Default.Lock,
                    title = "Upgrade koneksi ke HTTPS (Strict)",
                    subtitle = "Otomatis meningkatkan HTTP ke koneksi TLS terenkripsi",
                    checked = config.httpsUpgradeStrict,
                    accentColor = Color(0xFFFB542B),
                    textPrimary = profile.textPrimary,
                    textSecondary = profile.textSecondary,
                    onCheckedChange = { onUpdateConfig(config.copy(httpsUpgradeStrict = it)) }
                )
            }

            if (matchesFilter("Blokir sidik jari (Fingerprinting)", "Canvas & WebGL Poisoning")) {
                SettingsSwitchItem(
                    icon = Icons.Default.Security,
                    title = "Blokir sidik jari perangkat (Strict)",
                    subtitle = "Menyamarkan Canvas, WebGL, AudioContext & GPU browser",
                    checked = config.blockFingerprintingStrict,
                    accentColor = Color(0xFFFB542B),
                    textPrimary = profile.textPrimary,
                    textSecondary = profile.textSecondary,
                    onCheckedChange = { onUpdateConfig(config.copy(blockFingerprintingStrict = it)) }
                )
            }

            if (matchesFilter("Forgetful Browsing & ClearURLs", "Hapus jejak otomatis")) {
                SettingsSwitchItem(
                    icon = Icons.Default.Delete,
                    title = "Forgetful Browsing (Hapus saat tutup situs)",
                    subtitle = "Bersihkan cookie, localStorage & parameter pelacak URL otomatis",
                    checked = config.forgetfulBrowsing,
                    accentColor = Color(0xFFFB542B),
                    textPrimary = profile.textPrimary,
                    textSecondary = profile.textSecondary,
                    onCheckedChange = {
                        onUpdateConfig(config.copy(forgetfulBrowsing = it, stripTrackingParams = it))
                    }
                )
            }

            if (matchesFilter("Hapus data penjelajahan", "Clear browsing data")) {
                SettingsListItem(
                    icon = Icons.Default.History,
                    title = "Hapus data penjelajahan",
                    subtitle = "Hapus riwayat, cookie, gambar cache & data situs",
                    textPrimary = Color(0xFFFF453A),
                    textSecondary = profile.textSecondary,
                    onClick = { showClearDataDialog = !showClearDataDialog }
                )
                AnimatedVisibility(visible = showClearDataDialog) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 6.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(profile.cardBg)
                            .border(0.6.dp, Color(0xFFFF453A).copy(alpha = 0.4f), RoundedCornerShape(16.dp))
                            .padding(14.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text("Hapus Data Penjelajahan", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = profile.textPrimary)
                        ClearDataCheckRow("Riwayat penjelajahan", clearHistCheck, profile.textPrimary) { clearHistCheck = it }
                        ClearDataCheckRow("Cookie & data situs", clearCookiesCheck, profile.textPrimary) { clearCookiesCheck = it }
                        ClearDataCheckRow("Gambar & file dalam cache", clearCacheCheck, profile.textPrimary) { clearCacheCheck = it }
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            TextButton(onClick = { showClearDataDialog = false }) {
                                Text("Batal", color = profile.textSecondary)
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            Button(
                                onClick = {
                                    onClearBrowsingData(clearHistCheck, clearCookiesCheck, clearCacheCheck)
                                    showClearDataDialog = false
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF453A))
                            ) {
                                Text("Hapus Data", color = Color.White, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }

            HorizontalDivider(
                modifier = Modifier.padding(vertical = 8.dp),
                thickness = 0.5.dp,
                color = Color.White.copy(alpha = 0.08f)
            )

            // ----------------------------------------------------------------
            // SECTION: JARINGAN TOR & ANONIMITAS (TOR BROWSER SETTINGS)
            // ----------------------------------------------------------------
            SettingsCategoryHeader("Jaringan Tor & Anonimitas", Color(0xFFC084FC))

            if (matchesFilter("Tingkat Keamanan Tor", config.torSecurityLevel.displayName)) {
                SettingsListItem(
                    icon = Icons.Default.VpnLock,
                    title = "Tingkat Keamanan Tor (Security Level)",
                    subtitle = config.torSecurityLevel.displayName,
                    textPrimary = profile.textPrimary,
                    textSecondary = profile.textSecondary,
                    onClick = {
                        expandedSection = if (expandedSection == "TOR_SEC") null else "TOR_SEC"
                    }
                )
                AnimatedVisibility(visible = expandedSection == "TOR_SEC") {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(profile.cardBg)
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                    ) {
                        TorSecurityLevel.entries.forEach { lvl ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        onUpdateConfig(
                                            config.copy(
                                                torSecurityLevel = lvl,
                                                blockScriptsNoScript = !lvl.allowJavaScript
                                            )
                                        )
                                        expandedSection = null
                                        onPostBanner("Keamanan Tor diatur ke: ${lvl.displayName}")
                                    }
                                    .padding(vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                RadioButton(
                                    selected = config.torSecurityLevel == lvl,
                                    onClick = {
                                        onUpdateConfig(
                                            config.copy(
                                                torSecurityLevel = lvl,
                                                blockScriptsNoScript = !lvl.allowJavaScript
                                            )
                                        )
                                        expandedSection = null
                                    },
                                    colors = RadioButtonDefaults.colors(selectedColor = Color(0xFFC084FC))
                                )
                                Column {
                                    Text(lvl.displayName, color = profile.textPrimary, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                                    Text(lvl.description, color = profile.textSecondary, fontSize = 11.sp)
                                }
                            }
                        }
                    }
                }
            }

            if (matchesFilter("Jembatan Tor (Bridges)", config.torBridgeMode.label)) {
                SettingsListItem(
                    icon = Icons.Default.Public,
                    title = "Konfigurasi Jembatan Tor (Bridges)",
                    subtitle = config.torBridgeMode.label,
                    textPrimary = profile.textPrimary,
                    textSecondary = profile.textSecondary,
                    onClick = {
                        expandedSection = if (expandedSection == "TOR_BRIDGE") null else "TOR_BRIDGE"
                    }
                )
                AnimatedVisibility(visible = expandedSection == "TOR_BRIDGE") {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(profile.cardBg)
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                    ) {
                        TorBridgeMode.entries.forEach { bridge ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        onUpdateConfig(config.copy(torBridgeMode = bridge))
                                        expandedSection = null
                                    }
                                    .padding(vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                RadioButton(
                                    selected = config.torBridgeMode == bridge,
                                    onClick = {
                                        onUpdateConfig(config.copy(torBridgeMode = bridge))
                                        expandedSection = null
                                    },
                                    colors = RadioButtonDefaults.colors(selectedColor = Color(0xFFC084FC))
                                )
                                Column {
                                    Text(bridge.label, color = profile.textPrimary, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                                    Text(bridge.desc, color = profile.textSecondary, fontSize = 11.sp)
                                }
                            }
                        }
                    }
                }
            }

            if (matchesFilter("Sirkuit 3-Hop & VPN Stealth", config.vpnRegion.countryName)) {
                SettingsListItem(
                    icon = Icons.Default.VpnLock,
                    title = "Sirkuit 3-Hop Tor & Wilayah VPN",
                    subtitle = "${activeCircuit.joinToString(" → ") { it.countryFlag }} • ${config.vpnRegion.countryName} (${config.dohProvider.id})",
                    textPrimary = profile.textPrimary,
                    textSecondary = profile.textSecondary,
                    onClick = {
                        expandedSection = if (expandedSection == "VPN_DOH") null else "VPN_DOH"
                    }
                )
                AnimatedVisibility(visible = expandedSection == "VPN_DOH") {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(profile.cardBg)
                            .padding(14.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("Node Exit VPN (${config.vpnRegion.flag})", color = profile.textPrimary, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                            TextButton(onClick = onRotateCircuit) {
                                Text("Putar Sirkuit Baru", color = Color(0xFFC084FC), fontSize = 12.sp)
                            }
                        }
                        StealthVpnRegion.entries.forEach { region ->
                            val sel = config.vpnRegion == region
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(if (sel) Color(0xFFC084FC).copy(alpha = 0.2f) else profile.omniboxBg)
                                    .clickable { onUpdateConfig(config.copy(vpnRegion = region)) }
                                    .padding(10.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("${region.flag} ${region.countryName}", color = profile.textPrimary, fontSize = 12.sp)
                                Text(region.protocolLabel, color = Color(0xFF30D158), fontSize = 10.sp, fontFamily = JetBrainsMonoFontFamily)
                            }
                        }
                        Text("DNS-over-HTTPS (DoH)", color = profile.textSecondary, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        DohProviderSpec.entries.forEach { doh ->
                            val sel = config.dohProvider == doh
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(if (sel) Color(0xFF30D158).copy(alpha = 0.18f) else profile.omniboxBg)
                                    .clickable { onUpdateConfig(config.copy(dohProvider = doh)) }
                                    .padding(10.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(doh.displayName, color = profile.textPrimary, fontSize = 12.sp)
                                if (sel) Icon(Icons.Default.Check, null, tint = Color(0xFF30D158), modifier = Modifier.size(16.dp))
                            }
                        }
                    }
                }
            }

            HorizontalDivider(
                modifier = Modifier.padding(vertical = 8.dp),
                thickness = 0.5.dp,
                color = Color.White.copy(alpha = 0.08f)
            )

            // ----------------------------------------------------------------
            // SECTION: EKSTENSI, SETELAN SITUS & AKSESIBILITAS
            // ----------------------------------------------------------------
            SettingsCategoryHeader("Ekstensi, Setelan Situs & Aksesibilitas", profile.accentSecondary)

            if (matchesFilter("Ekstensi & Userscript", "uBlock Origin, ClearURLs, Dark Reader")) {
                SettingsListItem(
                    icon = Icons.Default.Extension,
                    title = "Ekstensi Keamanan (${SanctuaryBlackBoxEngine.enabledExtensionIds.size}/${SanctuaryBlackBoxEngine.availableExtensions.size})",
                    subtitle = "uBlock Origin Shield+, Canvas Poisoner, ClearURLs, WebRTC Lock",
                    textPrimary = profile.textPrimary,
                    textSecondary = profile.textSecondary,
                    onClick = {
                        expandedSection = if (expandedSection == "EXTENSIONS") null else "EXTENSIONS"
                    }
                )
                AnimatedVisibility(visible = expandedSection == "EXTENSIONS") {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(profile.cardBg)
                            .padding(14.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        SanctuaryBlackBoxEngine.availableExtensions.forEach { ext ->
                            val enabled = ext.id in SanctuaryBlackBoxEngine.enabledExtensionIds
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(profile.omniboxBg)
                                    .padding(horizontal = 12.dp, vertical = 8.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text("${ext.name} v${ext.version}", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = profile.textPrimary)
                                    Text(ext.description, fontSize = 10.sp, color = profile.textSecondary)
                                }
                                Switch(
                                    checked = enabled,
                                    onCheckedChange = { checked ->
                                        if (checked) {
                                            if (ext.id !in SanctuaryBlackBoxEngine.enabledExtensionIds) {
                                                SanctuaryBlackBoxEngine.enabledExtensionIds.add(ext.id)
                                            }
                                        } else {
                                            SanctuaryBlackBoxEngine.enabledExtensionIds.remove(ext.id)
                                        }
                                    }
                                )
                            }
                        }
                    }
                }
            }

            if (matchesFilter("Situs Desktop Bawaan", "Minta tampilan desktop")) {
                SettingsSwitchItem(
                    icon = Icons.Default.DesktopWindows,
                    title = "Situs Desktop Bawaan",
                    subtitle = "Selalu minta versi desktop saat membuka tab baru",
                    checked = config.desktopSiteDefault,
                    accentColor = profile.accentPrimary,
                    textPrimary = profile.textPrimary,
                    textSecondary = profile.textSecondary,
                    onCheckedChange = { onUpdateConfig(config.copy(desktopSiteDefault = it)) }
                )
            }

            if (matchesFilter("Skala Teks & Aksesibilitas", "${config.textZoomPercent}%")) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 10.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Skala Teks Halaman Web", fontSize = 14.sp, color = profile.textPrimary, fontWeight = FontWeight.Medium)
                        Text("${config.textZoomPercent}%", fontSize = 13.sp, color = profile.accentPrimary, fontFamily = JetBrainsMonoFontFamily)
                    }
                    Slider(
                        value = config.textZoomPercent.toFloat(),
                        onValueChange = {
                            onUpdateConfig(config.copy(textZoomPercent = it.toInt().coerceIn(75, 160)))
                        },
                        valueRange = 75f..160f,
                        colors = SliderDefaults.colors(
                            thumbColor = profile.accentPrimary,
                            activeTrackColor = profile.accentPrimary
                        )
                    )
                }
            }

            if (matchesFilter("Tentang Browser Sanctuary", "Chromium + Brave Core + Tor Onion v3")) {
                SettingsListItem(
                    icon = Icons.Default.Settings,
                    title = "Tentang Browser Sanctuary",
                    subtitle = "Arcanum Unified Browser v128.0 • Brave Shields + Tor Onion 3-Hop",
                    textPrimary = profile.textPrimary,
                    textSecondary = profile.textSecondary,
                    onClick = {
                        onPostBanner("Browser Sanctuary v128.0 — Chromium Sandbox Terisolasi Penuh.")
                    }
                )
            }
        }
    }
}

@Composable
private fun SettingsCategoryHeader(title: String, color: Color) {
    Text(
        text = title,
        fontSize = 12.sp,
        fontWeight = FontWeight.Bold,
        color = color,
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
    )
}

@Composable
private fun SettingsListItem(
    icon: ImageVector,
    title: String,
    subtitle: String,
    textPrimary: Color,
    textSecondary: Color,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Icon(icon, contentDescription = null, tint = textSecondary, modifier = Modifier.size(22.dp))
        Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
            Text(title, fontSize = 14.5.sp, fontWeight = FontWeight.Normal, color = textPrimary)
            Text(subtitle, fontSize = 12.sp, color = textSecondary, maxLines = 2, overflow = TextOverflow.Ellipsis)
        }
        Icon(Icons.Default.KeyboardArrowRight, contentDescription = null, tint = textSecondary, modifier = Modifier.size(18.dp))
    }
}

@Composable
private fun SettingsSwitchItem(
    icon: ImageVector,
    title: String,
    subtitle: String,
    checked: Boolean,
    accentColor: Color,
    textPrimary: Color,
    textSecondary: Color,
    onCheckedChange: (Boolean) -> Unit,
    onRowClick: (() -> Unit)? = null
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onRowClick?.invoke() ?: onCheckedChange(!checked) }
            .padding(horizontal = 16.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Icon(icon, contentDescription = null, tint = textSecondary, modifier = Modifier.size(22.dp))
        Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
            Text(title, fontSize = 14.5.sp, fontWeight = FontWeight.Normal, color = textPrimary)
            Text(subtitle, fontSize = 12.sp, color = textSecondary, maxLines = 2, overflow = TextOverflow.Ellipsis)
        }
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                checkedTrackColor = accentColor
            )
        )
    }
}

@Composable
private fun ClearDataCheckRow(
    label: String,
    checked: Boolean,
    textColor: Color,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onCheckedChange(!checked) },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = CheckboxDefaults.colors(checkedColor = Color(0xFFFF453A))
        )
        Text(label, fontSize = 13.sp, color = textColor)
    }
}

// ============================================================================
// 2. AUTHENTIC BRAVE SHIELDS POPUP PANEL
// ============================================================================

@Composable
fun BraveShieldsPopupSheet(
    domainName: String,
    blockedCount: Int,
    strippedParamsCount: Int,
    config: BrowserUnifiedConfig,
    onUpdateConfig: (BrowserUnifiedConfig) -> Unit,
    onClose: () -> Unit
) {
    var showAdvanced by remember { mutableStateOf(true) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF17191E))
            .border(0.6.dp, Color(0xFFFB542B).copy(alpha = 0.4f))
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                BraveLionShieldGlyph(active = config.shieldsMasterEnabled, modifier = Modifier.size(28.dp))
                Column {
                    Text(
                        text = domainName.ifBlank { "Situs Aktif" },
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = if (config.shieldsMasterEnabled) "Shields AKTIF melindungi situs ini" else "Shields NONAKTIF sementara",
                        fontSize = 11.sp,
                        color = if (config.shieldsMasterEnabled) Color(0xFFFB542B) else Color(0xFF8D93A1)
                    )
                }
            }
            Switch(
                checked = config.shieldsMasterEnabled,
                onCheckedChange = { onUpdateConfig(config.copy(shieldsMasterEnabled = it)) },
                colors = SwitchDefaults.colors(
                    checkedThumbColor = Color.White,
                    checkedTrackColor = Color(0xFFFB542B)
                )
            )
        }

        // Giant Brave Blocked Trackers Stat Strip
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(14.dp))
                .background(Color(0xFF23262E))
                .padding(horizontal = 14.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Text(
                text = "$blockedCount",
                fontFamily = JetBrainsMonoFontFamily,
                fontSize = 26.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color(0xFFFB542B)
            )
            Column(modifier = Modifier.weight(1f)) {
                Text("Pelacak & iklan diblokir", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color.White)
                Text("+$strippedParamsCount parameter URL pelacakan dibersihkan", fontSize = 11.sp, color = Color(0xFF8D93A1))
            }
            TextButton(onClick = onClose) {
                Text("Tutup", color = Color(0xFF8AB4F8), fontSize = 12.sp)
            }
        }

        // Accordion Toggle: Advanced Controls
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(10.dp))
                .clickable { showAdvanced = !showAdvanced }
                .padding(vertical = 4.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Kontrol Lanjutan Brave Shields", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFF8D93A1))
            Icon(
                imageVector = if (showAdvanced) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                contentDescription = null,
                tint = Color(0xFF8D93A1),
                modifier = Modifier.size(18.dp)
            )
        }

        AnimatedVisibility(visible = showAdvanced) {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                // Aggressiveness Pills
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    BraveBlockingAggressiveness.entries.forEach { mode ->
                        val sel = config.trackerBlockingMode == mode
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(10.dp))
                                .background(if (sel) Color(0xFFFB542B).copy(alpha = 0.24f) else Color(0xFF23262E))
                                .border(0.8.dp, if (sel) Color(0xFFFB542B) else Color.Transparent, RoundedCornerShape(10.dp))
                                .clickable { onUpdateConfig(config.copy(trackerBlockingMode = mode)) }
                                .padding(vertical = 7.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(mode.label, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = if (sel) Color(0xFFFB542B) else Color.White)
                        }
                    }
                }

                ShieldMiniToggle("Upgrade Koneksi ke HTTPS (Strict)", config.httpsUpgradeStrict) {
                    onUpdateConfig(config.copy(httpsUpgradeStrict = it))
                }
                ShieldMiniToggle("Blokir Skrip Halaman (NoScript)", config.blockScriptsNoScript) {
                    onUpdateConfig(config.copy(blockScriptsNoScript = it))
                }
                ShieldMiniToggle("Racuni Sidik Jari Canvas & WebGL", config.blockFingerprintingStrict) {
                    onUpdateConfig(config.copy(blockFingerprintingStrict = it))
                }
                ShieldMiniToggle("Blokir Cookie Lintas-Situs", config.blockCrossSiteCookies) {
                    onUpdateConfig(config.copy(blockCrossSiteCookies = it))
                }
            }
        }
    }
}

@Composable
private fun ShieldMiniToggle(
    label: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(Color(0xFF23262E))
            .padding(horizontal = 12.dp, vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(label, fontSize = 12.sp, color = Color(0xFFF0F2F5), modifier = Modifier.weight(1f))
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(checkedThumbColor = Color.White, checkedTrackColor = Color(0xFFFB542B))
        )
    }
}

// ============================================================================
// 3. AUTHENTIC TOR CIRCUIT & IDENTITY TREE POPOVER
// ============================================================================

@Composable
fun TorCircuitPopoverSheet(
    domainName: String,
    circuit: List<OnionRelayHop>,
    config: BrowserUnifiedConfig,
    onUpdateConfig: (BrowserUnifiedConfig) -> Unit,
    onRotateCircuit: () -> Unit,
    onBurnIdentity: () -> Unit,
    onClose: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF191224))
            .border(0.6.dp, Color(0xFFC084FC).copy(alpha = 0.45f))
            .verticalScroll(rememberScrollState())
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
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                TorOnionGlyph(connected = true, modifier = Modifier.size(26.dp))
                Column {
                    Text("Sirkuit Tor untuk Situs Ini", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color(0xFFF3E8FF))
                    Text("Jembatan: ${config.torBridgeMode.label}", fontSize = 11.sp, color = Color(0xFF30D158))
                }
            }
            TextButton(onClick = onClose) {
                Text("Tutup", color = Color(0xFFC084FC), fontSize = 12.sp)
            }
        }

        // Authentic Tor Vertical Relay Tree: [This Browser] -> [Guard] -> [Middle] -> [Exit] -> [Destination]
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(14.dp))
                .background(Color(0xFF261D35))
                .padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            TorTreeNodeRow(badge = "LOKAL", title = "Browser Sanctuary Ini", subtitle = "Enkripsi 3-Lapis AES/Onion", accent = Color(0xFF8AB4F8), isLast = false)
            circuit.forEach { hop ->
                TorTreeNodeRow(
                    badge = hop.countryFlag,
                    title = "${hop.countryName} (${hop.routerNickname})",
                    subtitle = "${hop.roleLabel} • ${hop.virtualIp} • ${hop.latencyMs}ms",
                    accent = Color(0xFFC084FC),
                    isLast = false
                )
            }
            TorTreeNodeRow(
                badge = "TUJUAN",
                title = domainName.ifBlank { "Situs Tujuan" },
                subtitle = "Exit Node Terisolasi • DoH ${config.dohProvider.id}",
                accent = Color(0xFF30D158),
                isLast = true
            )
        }

        // Tor Security Level Quick Chips
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            TorSecurityLevel.entries.forEach { level ->
                val sel = config.torSecurityLevel == level
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(10.dp))
                        .background(if (sel) Color(0xFFC084FC).copy(alpha = 0.25f) else Color(0xFF261D35))
                        .border(0.8.dp, if (sel) Color(0xFFC084FC) else Color.Transparent, RoundedCornerShape(10.dp))
                        .clickable {
                            onUpdateConfig(
                                config.copy(
                                    torSecurityLevel = level,
                                    blockScriptsNoScript = !level.allowJavaScript
                                )
                            )
                        }
                        .padding(vertical = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = level.id,
                        fontSize = 10.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (sel) Color(0xFFC084FC) else Color(0xFFA799B7)
                    )
                }
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(
                onClick = onRotateCircuit,
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF7D4698)),
                contentPadding = PaddingValues(vertical = 8.dp)
            ) {
                Icon(Icons.Default.Refresh, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("Sirkuit Baru", fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }
            OutlinedButton(
                onClick = onBurnIdentity,
                modifier = Modifier.weight(1f),
                contentPadding = PaddingValues(vertical = 8.dp)
            ) {
                Text("Identitas Baru", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFF453A))
            }
        }
    }
}

@Composable
private fun TorTreeNodeRow(
    badge: String,
    title: String,
    subtitle: String,
    accent: Color,
    isLast: Boolean
) {
    Row(
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Box(
                modifier = Modifier
                    .size(10.dp)
                    .clip(CircleShape)
                    .background(accent)
            )
            if (!isLast) {
                Box(
                    modifier = Modifier
                        .width(2.dp)
                        .height(22.dp)
                        .background(accent.copy(alpha = 0.4f))
                )
            }
        }
        Column(modifier = Modifier.weight(1f)) {
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp), verticalAlignment = Alignment.CenterVertically) {
                Text(badge, fontFamily = JetBrainsMonoFontFamily, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = accent)
                Text(title, fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = Color.White, maxLines = 1, overflow = TextOverflow.Ellipsis)
            }
            Text(subtitle, fontSize = 10.sp, color = Color(0xFFA799B7), maxLines = 1, overflow = TextOverflow.Ellipsis)
        }
    }
}

// ============================================================================
// 4. BOOKMARKS, HISTORY & DOWNLOADS UNIFIED SHEET
// ============================================================================

@Composable
fun BrowserLibrarySheet(
    activeSheet: String, // "BOOKMARKS", "HISTORY", "DOWNLOADS"
    profile: BrowserUiProfile,
    bookmarks: MutableList<BrowserBookmarkEntry>,
    history: MutableList<BrowserHistoryRecord>,
    downloads: MutableList<BrowserDownloadRecord>,
    onNavigateUrl: (String) -> Unit,
    onPostBanner: (String) -> Unit,
    onClose: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(max = 290.dp)
            .background(profile.toolbarBg)
            .border(0.5.dp, Color.White.copy(alpha = 0.12f))
            .verticalScroll(rememberScrollState())
            .padding(14.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = when (activeSheet) {
                    "BOOKMARKS" -> "Bookmark (${bookmarks.size})"
                    "DOWNLOADS" -> "Unduhan Terisolasi (${downloads.size})"
                    else -> "Riwayat Penjelajahan (${history.size})"
                },
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = profile.textPrimary
            )
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                if (activeSheet == "HISTORY" && history.isNotEmpty()) {
                    TextButton(onClick = {
                        history.clear()
                        onPostBanner("Riwayat penjelajahan dibersihkan.")
                    }) {
                        Text("Hapus Semua", fontSize = 11.sp, color = Color(0xFFFF453A))
                    }
                }
                TextButton(onClick = onClose) {
                    Text("Tutup", fontSize = 11.sp, color = profile.accentPrimary)
                }
            }
        }

        when (activeSheet) {
            "BOOKMARKS" -> {
                if (bookmarks.isEmpty()) {
                    Text("Belum ada bookmark tersimpan.", fontSize = 12.sp, color = profile.textSecondary)
                } else {
                    bookmarks.forEach { bm ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .background(profile.cardBg)
                                .clickable {
                                    onClose()
                                    onNavigateUrl(bm.url)
                                }
                                .padding(horizontal = 12.dp, vertical = 9.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                modifier = Modifier.weight(1f),
                                horizontalArrangement = Arrangement.spacedBy(10.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(Icons.Default.Bookmark, null, tint = Color(bm.accentHex), modifier = Modifier.size(16.dp))
                                Column {
                                    Text(bm.title, fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = profile.textPrimary, maxLines = 1, overflow = TextOverflow.Ellipsis)
                                    Text(bm.url, fontSize = 11.sp, color = profile.textSecondary, maxLines = 1, overflow = TextOverflow.Ellipsis)
                                }
                            }
                            IconButton(onClick = { bookmarks.removeAll { it.id == bm.id } }, modifier = Modifier.size(28.dp)) {
                                Icon(Icons.Default.Delete, "Hapus", tint = Color(0xFFFF453A), modifier = Modifier.size(16.dp))
                            }
                        }
                    }
                }
            }

            "DOWNLOADS" -> {
                if (downloads.isEmpty()) {
                    Text("Belum ada berkas yang diunduh di sesi ini.", fontSize = 12.sp, color = profile.textSecondary)
                } else {
                    downloads.forEach { dl ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .background(profile.cardBg)
                                .padding(horizontal = 12.dp, vertical = 9.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                modifier = Modifier.weight(1f),
                                horizontalArrangement = Arrangement.spacedBy(10.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(Icons.Default.Download, null, tint = profile.accentSecondary, modifier = Modifier.size(18.dp))
                                Column {
                                    Text(dl.fileName, fontSize = 12.5.sp, fontWeight = FontWeight.SemiBold, color = profile.textPrimary, maxLines = 1, overflow = TextOverflow.Ellipsis)
                                    Text("${dl.sizeLabel} • ${dl.statusLabel}", fontSize = 10.5.sp, color = profile.textSecondary)
                                }
                            }
                            IconButton(onClick = { downloads.removeAll { it.id == dl.id } }, modifier = Modifier.size(28.dp)) {
                                Icon(Icons.Default.Delete, "Hapus", tint = Color(0xFFFF453A), modifier = Modifier.size(16.dp))
                            }
                        }
                    }
                }
            }

            else -> {
                if (history.isEmpty()) {
                    Text("Belum ada riwayat penjelajahan.", fontSize = 12.sp, color = profile.textSecondary)
                } else {
                    history.forEach { item ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .background(profile.cardBg)
                                .clickable {
                                    onClose()
                                    onNavigateUrl(item.url)
                                }
                                .padding(horizontal = 12.dp, vertical = 9.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(item.title, fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = profile.textPrimary, maxLines = 1, overflow = TextOverflow.Ellipsis)
                                Text(item.url, fontSize = 11.sp, color = profile.textSecondary, maxLines = 1, overflow = TextOverflow.Ellipsis)
                            }
                            Text(item.timestampText, fontSize = 10.sp, color = profile.textSecondary)
                        }
                    }
                }
            }
        }
    }
}
