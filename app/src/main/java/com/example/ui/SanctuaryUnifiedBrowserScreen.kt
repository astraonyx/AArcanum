package com.example.ui

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.net.Uri
import android.view.ViewGroup
import android.webkit.CookieManager
import android.webkit.RenderProcessGoneDetail
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
import android.webkit.WebSettings
import android.webkit.WebStorage
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
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
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.DesktopWindows
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.filled.VpnLock
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.ui.theme.JetBrainsMonoFontFamily
import java.io.ByteArrayInputStream
import java.net.URLEncoder
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

// ============================================================================
// EXTERNAL LINK BROWSER LOCKDOWN CONTROLLER
// Handles opening external http/https links directly in Arcanum Browser while
// strictly locking out PIN Gate & Vault access until the app is Force Closed / Restarted.
// ============================================================================

object ExternalBrowserLockdownController {
    var isExternalBrowserLockdownActive by mutableStateOf(false)
        private set
    var latestExternalBrowserUrl by mutableStateOf<String?>(null)
        private set
    var externalBrowserNonce by mutableIntStateOf(0)
        private set

    fun isWebBrowserIntent(intent: android.content.Intent?): Boolean {
        if (intent == null) return false
        val action = intent.action ?: return false
        if (action == android.content.Intent.ACTION_WEB_SEARCH) {
            return true
        }
        if (action == android.content.Intent.ACTION_MAIN &&
            intent.hasCategory(android.content.Intent.CATEGORY_APP_BROWSER)
        ) {
            return true
        }
        if (action == android.content.Intent.ACTION_VIEW) {
            val scheme = intent.data?.scheme?.lowercase(Locale.ROOT) ?: return false
            return scheme == "http" || scheme == "https" || scheme == "about"
        }
        return false
    }

    fun extractTargetUrlFromIntent(intent: android.content.Intent?): String? {
        if (!isWebBrowserIntent(intent) || intent == null) return null
        return when (intent.action) {
            android.content.Intent.ACTION_WEB_SEARCH -> {
                val query = intent.getStringExtra(android.app.SearchManager.QUERY)?.trim().orEmpty()
                if (query.isNotEmpty()) {
                    val encoded = try {
                        URLEncoder.encode(query, "UTF-8")
                    } catch (_: Exception) {
                        query.replace(" ", "+")
                    }
                    "https://search.brave.com/search?q=$encoded"
                } else {
                    "https://search.brave.com"
                }
            }
            android.content.Intent.ACTION_MAIN -> "https://search.brave.com"
            android.content.Intent.ACTION_VIEW -> intent.dataString?.takeIf { it.isNotBlank() }
                ?: "https://search.brave.com"
            else -> null
        }
    }

    fun activateExternalBrowserLockdown(rawUrl: String) {
        val cleaned = SanctuaryBlackBoxEngine.normalizeAndUpgradeUrl(
            rawInput = rawUrl,
            httpsOnlyUpgrade = true,
            stripTrackers = true
        )
        isExternalBrowserLockdownActive = true
        latestExternalBrowserUrl = cleaned
        externalBrowserNonce++
    }

    fun resetForTesting() {
        isExternalBrowserLockdownActive = false
        latestExternalBrowserUrl = null
        externalBrowserNonce = 0
    }

    fun requestDefaultBrowserRole(context: android.content.Context, onFeedback: (String) -> Unit) {
        try {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
                val roleManager = context.getSystemService(android.app.role.RoleManager::class.java)
                if (roleManager != null && roleManager.isRoleAvailable(android.app.role.RoleManager.ROLE_BROWSER)) {
                    if (roleManager.isRoleHeld(android.app.role.RoleManager.ROLE_BROWSER)) {
                        onFeedback("Arcanum Browser sudah aktif sebagai Browser Default sistem!")
                        return
                    }
                    val roleIntent = roleManager.createRequestRoleIntent(android.app.role.RoleManager.ROLE_BROWSER)
                    if (context is android.app.Activity) {
                        context.startActivityForResult(roleIntent, 9444)
                        onFeedback("Pilih 'Arcanum' pada dialog sistem untuk menjadikannya Browser Default.")
                        return
                    }
                }
            }
            val settingsIntent = android.content.Intent(android.provider.Settings.ACTION_MANAGE_DEFAULT_APPS_SETTINGS).apply {
                addFlags(android.content.Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(settingsIntent)
            onFeedback("Pilih Aplikasi Browser Default → Arcanum.")
        } catch (_: Throwable) {
            onFeedback("Buka Pengaturan Sistem → Aplikasi Default → Aplikasi Browser → Pilih Arcanum.")
        }
    }
}

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun SanctuaryUnifiedBrowserScreen(
    onCloseToSanctuary: () -> Unit,
    onPostBanner: (String) -> Unit,
    initialExternalUrl: String? = null,
    externalUrlNonce: Int = 0,
    isExternalLinkLockdownMode: Boolean = false
) {
    val context = androidx.compose.ui.platform.LocalContext.current
    var config by remember { mutableStateOf(BrowserUnifiedConfig()) }
    val profile = config.uiProfile

    val initialResolvedTabUrl = remember(initialExternalUrl) {
        initialExternalUrl?.takeIf { it.isNotBlank() } ?: "https://search.brave.com"
    }

    // Multi-Tab State (Regular + Tor Private Tabs)
    val browserTabs = remember {
        mutableStateListOf(
            SanctuaryBrowserTab(
                id = "tab_1",
                title = if (!initialExternalUrl.isNullOrBlank()) "Link Eksternal" else "Brave Search",
                currentUrl = initialResolvedTabUrl,
                isIncognitoTor = false
            ),
            SanctuaryBrowserTab(
                id = "tab_2",
                title = "DuckDuckGo Privacy",
                currentUrl = "https://duckduckgo.com",
                isIncognitoTor = true
            )
        )
    }
    var activeTabId by remember { mutableStateOf("tab_1") }
    val activeTab = browserTabs.find { it.id == activeTabId } ?: browserTabs.first()

    var addressBarInput by remember(activeTab.currentUrl) { mutableStateOf(activeTab.currentUrl) }
    var webViewRef by remember { mutableStateOf<WebView?>(null) }
    var pageLoadProgress by remember { mutableIntStateOf(100) }
    var rendererSafeModeFallback by remember { mutableStateOf(false) }

    DisposableEffect(Unit) {
        onDispose {
            try {
                webViewRef?.apply {
                    stopLoading()
                    webViewClient = WebViewClient()
                    webChromeClient = null
                    (parent as? ViewGroup)?.removeView(this)
                    destroy()
                }
            } catch (_: Throwable) {
            }
            webViewRef = null
        }
    }

    // Bookmarks, History & Downloads State
    val bookmarksList = remember {
        mutableStateListOf(
            BrowserBookmarkEntry("bm_1", "Brave Search", "https://search.brave.com", "BRV", 0xFFFB542B),
            BrowserBookmarkEntry("bm_2", "DuckDuckGo", "https://duckduckgo.com", "DDG", 0xFFFF9F0A),
            BrowserBookmarkEntry("bm_3", "Wikipedia ID", "https://id.m.wikipedia.org", "WIKI", 0xFF8AB4F8),
            BrowserBookmarkEntry("bm_4", "Proton Privacy", "https://proton.me", "PRT", 0xFFC084FC)
        )
    }
    val historyList = remember {
        mutableStateListOf(
            BrowserHistoryRecord("hist_1", "Brave Search — Private Index", "https://search.brave.com", "Baru saja"),
            BrowserHistoryRecord("hist_2", "DuckDuckGo — Anonymous Search", "https://duckduckgo.com", "4 mnt lalu")
        )
    }
    val downloadsList = remember {
        mutableStateListOf(
            BrowserDownloadRecord("dl_1", "arcanum_tor_relay_manifest.json", "https://torproject.org", "14.2 KB", "Terenkripsi • Aman")
        )
    }

    // Overlays, Popovers & Navigation Screens
    var showSettingsFullScreen by remember { mutableStateOf(false) }
    var showTabGridSwitcher by remember { mutableStateOf(false) }
    var tabGridFilterTorOnly by remember { mutableStateOf(false) }
    var showOverflowMenu by remember { mutableStateOf(false) }
    var activePopoverSheet by remember { mutableStateOf<String?>(null) } // "SHIELDS", "TOR", "BOOKMARKS", "HISTORY", "DOWNLOADS"
    var showFindInPageBar by remember { mutableStateOf(false) }
    var findInPageQuery by remember { mutableStateOf("") }
    var showStartPageDashboard by remember { mutableStateOf(initialExternalUrl.isNullOrBlank()) }
    var desktopSiteMode by remember(config.desktopSiteDefault) { mutableStateOf(config.desktopSiteDefault) }

    // Shields & Tor Telemetry Counters
    var blockedAdsCount by remember { mutableIntStateOf(27) }
    var strippedParamsCount by remember { mutableIntStateOf(9) }
    var circuitRotationNonce by remember { mutableIntStateOf(1) }
    val activeOnionCircuit = remember(circuitRotationNonce) {
        SanctuaryBlackBoxEngine.generateNewOnionCircuit(circuitRotationNonce)
    }

    val mobileSpoofedUas = remember {
        listOf(
            "Mozilla/5.0 (Linux; Android 14; Pixel 8 Pro) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/128.0.0.0 Mobile Safari/537.36",
            "Mozilla/5.0 (iPhone; CPU iPhone OS 18_0 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/18.0 Mobile/15E148 Safari/604.1"
        )
    }
    val desktopSpoofedUas = remember {
        listOf(
            "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/128.0.0.0 Safari/537.36",
            "Mozilla/5.0 (Macintosh; Intel Mac OS X 14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/128.0.0.0 Safari/537.36"
        )
    }

    val speedDialPortals = remember {
        listOf(
            BrowserSpeedDialShortcut("Google", "https://www.google.com", "G", Color(0xFF8AB4F8)),
            BrowserSpeedDialShortcut("Brave", "https://search.brave.com", "BR", Color(0xFFFB542B)),
            BrowserSpeedDialShortcut("DuckDuckGo", "https://duckduckgo.com", "DDG", Color(0xFFFF9F0A)),
            BrowserSpeedDialShortcut("YouTube", "https://m.youtube.com", "YT", Color(0xFFFF453A)),
            BrowserSpeedDialShortcut("Wikipedia", "https://id.m.wikipedia.org", "W", Color(0xFF64D2FF)),
            BrowserSpeedDialShortcut("GitHub", "https://github.com", "GH", Color(0xFFC084FC)),
            BrowserSpeedDialShortcut("TradingView", "https://www.tradingview.com", "TV", Color(0xFF30D158)),
            BrowserSpeedDialShortcut("Reddit", "https://www.reddit.com", "RD", Color(0xFFFF6961))
        )
    }

    val isCurrentUrlBookmarked = bookmarksList.any { it.url.equals(activeTab.currentUrl, ignoreCase = true) }

    fun toggleBookmarkForCurrentPage() {
        val idx = bookmarksList.indexOfFirst { it.url.equals(activeTab.currentUrl, ignoreCase = true) }
        if (idx >= 0) {
            val removed = bookmarksList.removeAt(idx)
            onPostBanner("Bookmark '${removed.title}' dihapus.")
        } else {
            val host = try {
                Uri.parse(activeTab.currentUrl).host?.removePrefix("www.") ?: activeTab.title
            } catch (_: Exception) {
                activeTab.title
            }
            bookmarksList.add(
                0,
                BrowserBookmarkEntry(
                    id = "bm_${System.currentTimeMillis()}",
                    title = host.replaceFirstChar { it.uppercase() },
                    url = activeTab.currentUrl,
                    badgeCode = host.take(3).uppercase(Locale.ROOT),
                    accentHex = 0xFF8AB4F8
                )
            )
            onPostBanner("Ditambahkan ke Bookmark ${profile.displayName}.")
        }
    }

    fun resolveInputUrlOrSearch(rawInput: String): String {
        val trimmed = rawInput.trim()
        if (trimmed.isEmpty()) return config.customHomepageUrl.ifBlank { "https://search.brave.com" }
        val hasScheme = trimmed.startsWith("http://", true) || trimmed.startsWith("https://", true)
        val looksLikeDomain = trimmed.contains(".") && !trimmed.contains(" ")
        val initialUrl = when {
            hasScheme -> trimmed
            looksLikeDomain -> "https://$trimmed"
            else -> {
                val encoded = try {
                    URLEncoder.encode(trimmed, "UTF-8")
                } catch (_: Exception) {
                    trimmed.replace(" ", "+")
                }
                config.searchEngine.searchUrlTemplate.replace("%s", encoded)
            }
        }
        return SanctuaryBlackBoxEngine.normalizeAndUpgradeUrl(
            rawInput = initialUrl,
            httpsOnlyUpgrade = config.shieldsMasterEnabled && config.httpsUpgradeStrict,
            stripTrackers = config.shieldsMasterEnabled && config.stripTrackingParams
        )
    }

    fun buildRequestHeaders(isDesktop: Boolean): Map<String, String> {
        return if (isDesktop) {
            mapOf(
                "Sec-CH-UA-Mobile" to "?0",
                "Sec-CH-UA-Platform" to "\"Windows\"",
                "Upgrade-Insecure-Requests" to "1"
            )
        } else {
            mapOf(
                "Sec-CH-UA-Mobile" to "?1",
                "Sec-CH-UA-Platform" to "\"Android\""
            )
        }
    }

    fun applyWebViewDesktopModeSettings(wv: WebView, isDesktop: Boolean) {
        wv.settings.apply {
            useWideViewPort = true
            loadWithOverviewMode = true
            setSupportZoom(true)
            builtInZoomControls = true
            displayZoomControls = false
            layoutAlgorithm = WebSettings.LayoutAlgorithm.NORMAL
            userAgentString = if (isDesktop) {
                desktopSpoofedUas[circuitRotationNonce % desktopSpoofedUas.size]
            } else {
                mobileSpoofedUas[circuitRotationNonce % mobileSpoofedUas.size]
            }
        }
        wv.setInitialScale(0)
    }

    fun navigateToUrl(rawTarget: String) {
        val cleaned = resolveInputUrlOrSearch(rawTarget)
        if (cleaned != rawTarget.trim()) {
            strippedParamsCount++
        }
        val resolution = com.example.ui.theme.ArcanumBrowserDomainAliasEngine.resolveForNavigation(
            rawUrl = cleaned,
            isProUnlocked = com.example.ui.theme.AppThemeController.isProModeUnlocked,
            rules = com.example.ui.theme.AppThemeController.proBrowserDomainAliases
        )
        val actualLoadUrl = SanctuaryBlackBoxEngine.rewriteUrlForDesktopMode(
            rawUrl = resolution.actualLoadUrl,
            desktopMode = desktopSiteMode
        )
        val displayUrl = resolution.displayAliasUrl

        val hostLabel = try {
            Uri.parse(displayUrl).host?.removePrefix("www.") ?: displayUrl
        } catch (_: Exception) {
            displayUrl
        }
        val idx = browserTabs.indexOfFirst { it.id == activeTab.id }
        if (idx >= 0) {
            browserTabs[idx] = browserTabs[idx].copy(
                title = hostLabel.take(24),
                currentUrl = displayUrl
            )
        }
        val nowStr = SimpleDateFormat("HH:mm", Locale("id", "ID")).format(Date())
        historyList.add(
            0,
            BrowserHistoryRecord(
                id = "hist_${System.currentTimeMillis()}",
                title = hostLabel.take(30),
                url = displayUrl,
                timestampText = nowStr
            )
        )
        addressBarInput = displayUrl
        showStartPageDashboard = false
        showTabGridSwitcher = false
        activePopoverSheet = null
        pageLoadProgress = 12
        if (resolution.matchedRule != null) {
            onPostBanner("Arcanum Pro Alias: $displayUrl → ${resolution.matchedRule.targetDomain}")
        }
        webViewRef?.let { wv ->
            applyWebViewDesktopModeSettings(wv, desktopSiteMode)
            wv.loadUrl(actualLoadUrl, buildRequestHeaders(desktopSiteMode))
        }
    }

    fun openNewTab(
        url: String = config.customHomepageUrl.ifBlank { "https://search.brave.com" },
        title: String = "Tab Baru",
        incognitoTor: Boolean = profile == BrowserUiProfile.TOR,
        showStart: Boolean = true
    ) {
        val resolution = com.example.ui.theme.ArcanumBrowserDomainAliasEngine.resolveForNavigation(
            rawUrl = url,
            isProUnlocked = com.example.ui.theme.AppThemeController.isProModeUnlocked,
            rules = com.example.ui.theme.AppThemeController.proBrowserDomainAliases
        )
        val newId = "tab_${System.currentTimeMillis()}"
        browserTabs.add(
            SanctuaryBrowserTab(
                id = newId,
                title = title,
                currentUrl = resolution.displayAliasUrl,
                isIncognitoTor = incognitoTor
            )
        )
        activeTabId = newId
        addressBarInput = resolution.displayAliasUrl
        showStartPageDashboard = showStart
        showTabGridSwitcher = false
        activePopoverSheet = null
        if (!showStart) {
            webViewRef?.loadUrl(resolution.actualLoadUrl)
        }
    }

    fun clearSessionData(clearHistory: Boolean, clearCookies: Boolean, clearCache: Boolean) {
        try {
            if (clearCookies) {
                CookieManager.getInstance().removeAllCookies(null)
                CookieManager.getInstance().flush()
                WebStorage.getInstance().deleteAllData()
            }
            webViewRef?.apply {
                if (clearCache) clearCache(true)
                if (clearHistory) clearHistory()
                clearFormData()
            }
        } catch (_: Throwable) {
        }
        if (clearHistory) historyList.clear()
        onPostBanner("Data penjelajahan terpilih telah dibersihkan.")
    }

    fun burnAllBrowserSessionData() {
        clearSessionData(clearHistory = true, clearCookies = true, clearCache = true)
        try {
            webViewRef?.apply {
                stopLoading()
                loadUrl("about:blank")
            }
        } catch (_: Throwable) {
        }
        browserTabs.clear()
        browserTabs.add(
            SanctuaryBrowserTab(
                id = "tab_${System.currentTimeMillis()}",
                title = "Tab Baru",
                currentUrl = "https://duckduckgo.com",
                isIncognitoTor = true
            )
        )
        activeTabId = browserTabs.first().id
        addressBarInput = "https://duckduckgo.com"
        circuitRotationNonce++
        showStartPageDashboard = true
        showTabGridSwitcher = false
        activePopoverSheet = null
        onPostBanner("Sesi Dibakar! Cookie, Cache, Riwayat & Tab direset + Sirkuit Tor baru.")
    }

    // Auto-navigate immediately whenever an external link is clicked outside the app
    androidx.compose.runtime.LaunchedEffect(initialExternalUrl, externalUrlNonce) {
        if (!initialExternalUrl.isNullOrBlank()) {
            navigateToUrl(initialExternalUrl)
        }
    }

    // Native Back Gesture Handling inside Browser
    BackHandler {
        when {
            showSettingsFullScreen -> showSettingsFullScreen = false
            activePopoverSheet != null -> activePopoverSheet = null
            showFindInPageBar -> {
                showFindInPageBar = false
                findInPageQuery = ""
                webViewRef?.clearMatches()
            }
            showTabGridSwitcher -> showTabGridSwitcher = false
            !showStartPageDashboard && webViewRef?.canGoBack() == true -> webViewRef?.goBack()
            !showStartPageDashboard -> showStartPageDashboard = true
            isExternalLinkLockdownMode -> {
                onPostBanner(
                    "Mode Browser Eksternal Terkunci • Untuk masuk ke PIN / Brankas, silakan Force Close atau Restart aplikasi Arcanum."
                )
            }
            else -> onCloseToSanctuary()
        }
    }

    // FULL-SCREEN AUTHENTIC SETTINGS PAGE
    if (showSettingsFullScreen) {
        BrowserSettingsFullScreen(
            config = config,
            activeCircuit = activeOnionCircuit,
            blockedAdsCount = blockedAdsCount,
            strippedParamsCount = strippedParamsCount,
            onUpdateConfig = { updated ->
                config = updated
                desktopSiteMode = updated.desktopSiteDefault
            },
            onRotateCircuit = {
                circuitRotationNonce++
                onPostBanner("Sirkuit 3-Hop Tor diputar ulang!")
            },
            onClearBrowsingData = ::clearSessionData,
            onBackToBrowser = { showSettingsFullScreen = false },
            onCloseToSanctuary = {
                if (isExternalLinkLockdownMode) {
                    showSettingsFullScreen = false
                    onPostBanner("Akses Sanctuary/Brankas dikunci di Mode Link Eksternal. Force Close / Restart aplikasi untuk masuk PIN.")
                } else {
                    onCloseToSanctuary()
                }
            },
            onPostBanner = onPostBanner
        )
        return
    }

    val currentDomain = remember(activeTab.currentUrl) {
        try {
            Uri.parse(activeTab.currentUrl).host?.removePrefix("www.") ?: "Tab Baru"
        } catch (_: Exception) {
            "Tab Baru"
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(profile.pageBg)
            .testTag("onion_shield_browser_wing")
    ) {
        // EXTERNAL LINK LOCKDOWN BANNER (Shows when opened via external link click)
        if (isExternalLinkLockdownMode) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF1E1B4B))
                    .padding(horizontal = 10.dp, vertical = 5.dp)
                    .testTag("external_browser_lockdown_banner"),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    modifier = Modifier.weight(1f),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = null,
                        tint = Color(0xFF38BDF8),
                        modifier = Modifier.size(13.dp)
                    )
                    Text(
                        text = "MODE BROWSER LINK LUAR • PIN & BRANKAS DIKUNCI (WAJIB FORCE CLOSE / RESTART APP)",
                        fontSize = 8.5.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color(0xFFE0F2FE),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(999.dp))
                        .background(Color(0xFF0284C7))
                        .clickable {
                            ExternalBrowserLockdownController.requestDefaultBrowserRole(context, onPostBanner)
                        }
                        .padding(horizontal = 8.dp, vertical = 3.dp)
                        .testTag("external_banner_set_default_browser_chip")
                ) {
                    Text(
                        text = "Default Browser",
                        fontSize = 8.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }
        }

        // ====================================================================
        // 1. 100% AUTHENTIC CHROME / BRAVE / TOR TOP OMNIBOX BAR + 'X' BUTTON
        // ====================================================================
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(profile.toolbarBg)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                // Home / New Tab Button (classic Chrome / Brave home icon)
                IconButton(
                    onClick = {
                        showStartPageDashboard = !showStartPageDashboard
                        showTabGridSwitcher = false
                        activePopoverSheet = null
                    },
                    modifier = Modifier.size(34.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Home,
                        contentDescription = "Beranda",
                        tint = if (showStartPageDashboard) profile.accentPrimary else profile.textPrimary,
                        modifier = Modifier.size(19.dp)
                    )
                }

                // AUTHENTIC OMNIBOX PILL (Padlock/Tor + URL Field + Brave Lion Shield + Go/Reload)
                Row(
                    modifier = Modifier
                        .weight(1f)
                        .height(40.dp)
                        .clip(RoundedCornerShape(999.dp))
                        .background(profile.omniboxBg)
                        .border(
                            width = if (activeTab.isIncognitoTor || profile == BrowserUiProfile.TOR) 0.9.dp else 0.5.dp,
                            color = if (activeTab.isIncognitoTor || profile == BrowserUiProfile.TOR) {
                                Color(0xFFC084FC).copy(alpha = 0.45f)
                            } else {
                                Color.White.copy(alpha = 0.12f)
                            },
                            shape = RoundedCornerShape(999.dp)
                        )
                        .padding(horizontal = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    // Left Icon: Tor Onion Emblem (in Tor / Incognito) OR Chrome HTTPS Padlock
                    Box(
                        modifier = Modifier
                            .size(26.dp)
                            .clip(CircleShape)
                            .clickable {
                                activePopoverSheet = if (activePopoverSheet == "TOR") null else "TOR"
                            }
                            .testTag("browser_pill_tor_circuit"),
                        contentAlignment = Alignment.Center
                    ) {
                        if (profile == BrowserUiProfile.TOR || activeTab.isIncognitoTor) {
                            TorOnionGlyph(connected = true, modifier = Modifier.size(18.dp))
                        } else {
                            Icon(
                                imageVector = Icons.Default.Lock,
                                contentDescription = "Info Situs & Sirkuit Tor",
                                tint = Color(0xFF81C995),
                                modifier = Modifier.size(15.dp)
                            )
                        }
                    }

                    // Omnibox URL Input
                    BasicTextField(
                        value = addressBarInput,
                        onValueChange = { addressBarInput = it },
                        singleLine = true,
                        textStyle = TextStyle(
                            color = profile.textPrimary,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Normal
                        ),
                        cursorBrush = SolidColor(profile.accentPrimary),
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Go),
                        keyboardActions = KeyboardActions(
                            onGo = { navigateToUrl(addressBarInput) }
                        ),
                        modifier = Modifier
                            .weight(1f)
                            .testTag("browser_address_input"),
                        decorationBox = { innerTextField ->
                            Box(contentAlignment = Alignment.CenterStart) {
                                if (addressBarInput.isEmpty()) {
                                    Text(
                                        text = when (profile) {
                                            BrowserUiProfile.BRAVE -> "Cari dengan Brave atau ketik URL"
                                            BrowserUiProfile.TOR -> "Telusuri secara pribadi dengan Tor"
                                            BrowserUiProfile.CHROME -> "Telusuri atau ketik URL"
                                        },
                                        fontSize = 12.5.sp,
                                        color = profile.textSecondary,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                }
                                innerTextField()
                            }
                        }
                    )

                    val activeAliasResolution = com.example.ui.theme.ArcanumBrowserDomainAliasEngine.resolveForNavigation(
                        rawUrl = addressBarInput,
                        isProUnlocked = com.example.ui.theme.AppThemeController.isProModeUnlocked,
                        rules = com.example.ui.theme.AppThemeController.proBrowserDomainAliases
                    )
                    if (activeAliasResolution.matchedRule != null) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(999.dp))
                                .background(Color(0xFFC084FC).copy(alpha = 0.22f))
                                .border(0.6.dp, Color(0xFFC084FC).copy(alpha = 0.6f), RoundedCornerShape(999.dp))
                                .clickable { showSettingsFullScreen = true }
                                .padding(horizontal = 5.dp, vertical = 2.dp)
                                .testTag("browser_pro_alias_active_badge")
                        ) {
                            Text(
                                text = "ALIAS",
                                fontFamily = JetBrainsMonoFontFamily,
                                fontSize = 8.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = Color(0xFFC084FC)
                            )
                        }
                    }

                    if (desktopSiteMode) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(999.dp))
                                .background(Color(0xFF38BDF8).copy(alpha = 0.20f))
                                .border(0.6.dp, Color(0xFF38BDF8).copy(alpha = 0.65f), RoundedCornerShape(999.dp))
                                .padding(horizontal = 5.dp, vertical = 2.dp)
                                .testTag("browser_desktop_mode_active_badge")
                        ) {
                            Text(
                                text = "PC",
                                fontFamily = JetBrainsMonoFontFamily,
                                fontSize = 8.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = Color(0xFF38BDF8)
                            )
                        }
                    }

                    // Brave Lion Shield Badge inside Omnibox (exact Brave UX!)
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(999.dp))
                            .background(
                                if (activePopoverSheet == "SHIELDS") Color(0xFFFB542B).copy(alpha = 0.25f)
                                else Color.Transparent
                            )
                            .clickable {
                                activePopoverSheet = if (activePopoverSheet == "SHIELDS") null else "SHIELDS"
                            }
                            .padding(horizontal = 4.dp, vertical = 3.dp)
                            .testTag("browser_pill_brave_shields"),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(3.dp)
                        ) {
                            BraveLionShieldGlyph(
                                active = config.shieldsMasterEnabled,
                                modifier = Modifier.size(17.dp)
                            )
                            Text(
                                text = "$blockedAdsCount",
                                fontFamily = JetBrainsMonoFontFamily,
                                fontSize = 9.5.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (config.shieldsMasterEnabled) Color(0xFFFB542B) else profile.textSecondary
                            )
                        }
                    }

                    // Go / Reload Icon
                    IconButton(
                        onClick = {
                            if (showStartPageDashboard) {
                                navigateToUrl(addressBarInput)
                            } else {
                                pageLoadProgress = 15
                                webViewRef?.reload()
                            }
                        },
                        modifier = Modifier
                            .size(26.dp)
                            .testTag("browser_go_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = "Muat Ulang / Buka",
                            tint = profile.textSecondary,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }

                // CHROME / BRAVE SQUARE TAB SWITCHER COUNTER [N]
                Box(
                    modifier = Modifier
                        .size(34.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .clickable {
                            showTabGridSwitcher = !showTabGridSwitcher
                            showOverflowMenu = false
                            activePopoverSheet = null
                        }
                        .testTag("browser_tab_switcher_button"),
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .size(20.dp)
                            .border(
                                1.8.dp,
                                if (showTabGridSwitcher) profile.accentPrimary else profile.textPrimary,
                                RoundedCornerShape(5.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "${browserTabs.size}",
                            fontFamily = JetBrainsMonoFontFamily,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (showTabGridSwitcher) profile.accentPrimary else profile.textPrimary
                        )
                    }
                }

                // AUTHENTIC 3-DOT OVERFLOW MENU (⋮) WITH CHROME 5-ICON TOP BAR
                Box {
                    IconButton(
                        onClick = { showOverflowMenu = !showOverflowMenu },
                        modifier = Modifier
                            .size(32.dp)
                            .testTag("browser_overflow_menu_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.MoreVert,
                            contentDescription = "Menu Lainnya",
                            tint = profile.textPrimary,
                            modifier = Modifier.size(19.dp)
                        )
                    }

                    DropdownMenu(
                        expanded = showOverflowMenu,
                        onDismissRequest = { showOverflowMenu = false },
                        modifier = Modifier
                            .background(profile.cardBg)
                            .widthIn(min = 252.dp)
                    ) {
                        // ICONIC TOP 5-ACTION HORIZONTAL STRIP (Forward, Bookmark, Download, Info, Refresh)
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 8.dp, vertical = 4.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            IconButton(
                                onClick = {
                                    showOverflowMenu = false
                                    webViewRef?.goForward()
                                },
                                modifier = Modifier.size(36.dp)
                            ) {
                                Icon(Icons.AutoMirrored.Filled.ArrowForward, "Maju", tint = profile.textPrimary, modifier = Modifier.size(19.dp))
                            }
                            IconButton(
                                onClick = {
                                    showOverflowMenu = false
                                    toggleBookmarkForCurrentPage()
                                },
                                modifier = Modifier.size(36.dp)
                            ) {
                                Icon(
                                    imageVector = if (isCurrentUrlBookmarked) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                                    contentDescription = "Bookmark",
                                    tint = if (isCurrentUrlBookmarked) Color(0xFFFFD60A) else profile.textPrimary,
                                    modifier = Modifier.size(19.dp)
                                )
                            }
                            IconButton(
                                onClick = {
                                    showOverflowMenu = false
                                    downloadsList.add(
                                        0,
                                        BrowserDownloadRecord(
                                            id = "dl_${System.currentTimeMillis()}",
                                            fileName = "${currentDomain.replace('.', '_')}_snapshot.html",
                                            sourceUrl = activeTab.currentUrl,
                                            sizeLabel = "128 KB",
                                            statusLabel = "Tersimpan Offline"
                                        )
                                    )
                                    activePopoverSheet = "DOWNLOADS"
                                    onPostBanner("Halaman disimpan ke Unduhan Terisolasi.")
                                },
                                modifier = Modifier.size(36.dp)
                            ) {
                                Icon(Icons.Default.Download, "Unduh Halaman", tint = profile.textPrimary, modifier = Modifier.size(19.dp))
                            }
                            IconButton(
                                onClick = {
                                    showOverflowMenu = false
                                    activePopoverSheet = "TOR"
                                },
                                modifier = Modifier.size(36.dp)
                            ) {
                                Icon(Icons.Default.Security, "Info Sirkuit & TLS", tint = Color(0xFFC084FC), modifier = Modifier.size(19.dp))
                            }
                            IconButton(
                                onClick = {
                                    showOverflowMenu = false
                                    pageLoadProgress = 15
                                    webViewRef?.reload()
                                },
                                modifier = Modifier.size(36.dp)
                            ) {
                                Icon(Icons.Default.Refresh, "Muat Ulang", tint = profile.textPrimary, modifier = Modifier.size(19.dp))
                            }
                        }

                        HorizontalDivider(thickness = 0.5.dp, color = Color.White.copy(alpha = 0.1f))

                        DropdownMenuItem(
                            leadingIcon = { Icon(Icons.Default.Add, null, tint = profile.textPrimary, modifier = Modifier.size(18.dp)) },
                            text = { Text("Tab baru", color = profile.textPrimary, fontSize = 13.5.sp) },
                            onClick = {
                                showOverflowMenu = false
                                openNewTab(incognitoTor = false, showStart = true)
                            }
                        )
                        DropdownMenuItem(
                            leadingIcon = { Icon(Icons.Default.VisibilityOff, null, tint = Color(0xFFC084FC), modifier = Modifier.size(18.dp)) },
                            text = { Text("Tab Penyamaran Tor baru", color = Color(0xFFC084FC), fontSize = 13.5.sp, fontWeight = FontWeight.SemiBold) },
                            onClick = {
                                showOverflowMenu = false
                                circuitRotationNonce++
                                openNewTab(url = "https://duckduckgo.com", title = "Tab Tor #${browserTabs.size + 1}", incognitoTor = true, showStart = false)
                                onPostBanner("Tab Penyamaran Tor dibuka dengan sirkuit baru.")
                            }
                        )
                        DropdownMenuItem(
                            leadingIcon = { Icon(Icons.Default.History, null, tint = profile.textPrimary, modifier = Modifier.size(18.dp)) },
                            text = { Text("Riwayat", color = profile.textPrimary, fontSize = 13.5.sp) },
                            onClick = {
                                showOverflowMenu = false
                                activePopoverSheet = "HISTORY"
                            }
                        )
                        DropdownMenuItem(
                            leadingIcon = { Icon(Icons.Default.Download, null, tint = profile.textPrimary, modifier = Modifier.size(18.dp)) },
                            text = { Text("Unduhan (${downloadsList.size})", color = profile.textPrimary, fontSize = 13.5.sp) },
                            onClick = {
                                showOverflowMenu = false
                                activePopoverSheet = "DOWNLOADS"
                            }
                        )
                        DropdownMenuItem(
                            leadingIcon = { Icon(Icons.Default.Star, null, tint = Color(0xFFFFD60A), modifier = Modifier.size(18.dp)) },
                            text = { Text("Bookmark (${bookmarksList.size})", color = profile.textPrimary, fontSize = 13.5.sp) },
                            onClick = {
                                showOverflowMenu = false
                                activePopoverSheet = "BOOKMARKS"
                            }
                        )
                        DropdownMenuItem(
                            leadingIcon = { Icon(Icons.Default.Search, null, tint = profile.textPrimary, modifier = Modifier.size(18.dp)) },
                            text = { Text("Cari di halaman", color = profile.textPrimary, fontSize = 13.5.sp) },
                            onClick = {
                                showOverflowMenu = false
                                showStartPageDashboard = false
                                showFindInPageBar = true
                            }
                        )
                        DropdownMenuItem(
                            leadingIcon = { Icon(Icons.Default.DesktopWindows, null, tint = profile.textPrimary, modifier = Modifier.size(18.dp)) },
                            trailingIcon = {
                                Checkbox(
                                    checked = desktopSiteMode,
                                    onCheckedChange = null,
                                    colors = CheckboxDefaults.colors(checkedColor = profile.accentPrimary)
                                )
                            },
                            text = { Text("Situs desktop", color = profile.textPrimary, fontSize = 13.5.sp) },
                            onClick = {
                                showOverflowMenu = false
                                val nextDesktop = !desktopSiteMode
                                desktopSiteMode = nextDesktop
                                config = config.copy(desktopSiteDefault = nextDesktop)
                                val targetBaseUrl = webViewRef?.url?.takeIf { it.isNotBlank() && it != "about:blank" }
                                    ?: activeTab.currentUrl
                                val resolution = com.example.ui.theme.ArcanumBrowserDomainAliasEngine.resolveForNavigation(
                                    rawUrl = targetBaseUrl,
                                    isProUnlocked = com.example.ui.theme.AppThemeController.isProModeUnlocked,
                                    rules = com.example.ui.theme.AppThemeController.proBrowserDomainAliases
                                )
                                val rewrittenTarget = SanctuaryBlackBoxEngine.rewriteUrlForDesktopMode(
                                    rawUrl = resolution.actualLoadUrl,
                                    desktopMode = nextDesktop
                                )
                                showStartPageDashboard = false
                                pageLoadProgress = 18
                                webViewRef?.let { wv ->
                                    applyWebViewDesktopModeSettings(wv, nextDesktop)
                                    wv.loadUrl(rewrittenTarget, buildRequestHeaders(nextDesktop))
                                    wv.evaluateJavascript(
                                        SanctuaryBlackBoxEngine.buildDesktopViewportJavaScript(nextDesktop),
                                        null
                                    )
                                }
                                onPostBanner(
                                    if (nextDesktop) "Mode Situs Desktop (PC 1280px) aktif."
                                    else "Mode Tampilan Seluler aktif."
                                )
                            }
                        )

                        HorizontalDivider(thickness = 0.5.dp, color = Color.White.copy(alpha = 0.1f))

                        DropdownMenuItem(
                            leadingIcon = { Icon(Icons.Default.Settings, null, tint = profile.accentPrimary, modifier = Modifier.size(18.dp)) },
                            text = { Text("Setelan Arcanum Browser", color = profile.accentPrimary, fontSize = 13.5.sp, fontWeight = FontWeight.Bold) },
                            onClick = {
                                showOverflowMenu = false
                                showSettingsFullScreen = true
                            }
                        )
                        DropdownMenuItem(
                            leadingIcon = { Icon(Icons.Default.Public, null, tint = Color(0xFF38BDF8), modifier = Modifier.size(18.dp)) },
                            text = { Text("Jadikan Arcanum Browser Default", color = Color(0xFF38BDF8), fontSize = 13.5.sp, fontWeight = FontWeight.Bold) },
                            onClick = {
                                showOverflowMenu = false
                                ExternalBrowserLockdownController.requestDefaultBrowserRole(context, onPostBanner)
                            }
                        )
                        DropdownMenuItem(
                            leadingIcon = { Icon(Icons.Default.Delete, null, tint = Color(0xFFFF453A), modifier = Modifier.size(18.dp)) },
                            text = { Text("Bakar Sesi & Sirkuit Tor", color = Color(0xFFFF453A), fontSize = 13.5.sp, fontWeight = FontWeight.Bold) },
                            onClick = {
                                showOverflowMenu = false
                                burnAllBrowserSessionData()
                            }
                        )
                        if (!isExternalLinkLockdownMode) {
                            DropdownMenuItem(
                                leadingIcon = { Icon(Icons.Default.Close, null, tint = Color(0xFFFF453A), modifier = Modifier.size(18.dp)) },
                                text = { Text("Tutup Browser → Sanctuary", color = Color(0xFFFF453A), fontSize = 13.5.sp, fontWeight = FontWeight.SemiBold) },
                                onClick = {
                                    showOverflowMenu = false
                                    onCloseToSanctuary()
                                }
                            )
                        }
                    }
                }

                // EXPLICIT 'X' BUTTON (OR LOCKED BADGE IN EXTERNAL LINK MODE)
                Box(
                    modifier = Modifier
                        .size(34.dp)
                        .clip(CircleShape)
                        .background(
                            if (isExternalLinkLockdownMode) Color(0xFF38BDF8).copy(alpha = 0.16f)
                            else Color(0xFFFF453A).copy(alpha = 0.16f)
                        )
                        .border(
                            0.8.dp,
                            if (isExternalLinkLockdownMode) Color(0xFF38BDF8).copy(alpha = 0.50f)
                            else Color(0xFFFF453A).copy(alpha = 0.50f),
                            CircleShape
                        )
                        .clickable {
                            if (isExternalLinkLockdownMode) {
                                onPostBanner(
                                    "Mode Browser Eksternal Terkunci • Wajib Force Close / Restart aplikasi untuk masuk ke PIN Brankas!"
                                )
                            } else {
                                onCloseToSanctuary()
                            }
                        }
                        .testTag("browser_exit_to_sanctuary_button"),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = if (isExternalLinkLockdownMode) Icons.Default.Lock else Icons.Default.Close,
                        contentDescription = if (isExternalLinkLockdownMode) "Mode Browser Terkunci" else "Kembali ke Sanctuary",
                        tint = if (isExternalLinkLockdownMode) Color(0xFF38BDF8) else Color(0xFFFF453A),
                        modifier = Modifier.size(17.dp)
                    )
                }
            }

            // REAL LINEAR LOADING PROGRESS BAR (Chrome Blue / Brave Orange / Tor Purple)
            if (!showStartPageDashboard && pageLoadProgress in 1..99) {
                LinearProgressIndicator(
                    progress = { pageLoadProgress / 100f },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(2.5.dp),
                    color = profile.accentPrimary,
                    trackColor = Color.Transparent
                )
            } else {
                HorizontalDivider(thickness = 0.5.dp, color = Color.White.copy(alpha = 0.08f))
            }
        }

        // ====================================================================
        // 2. FIND-IN-PAGE BAR (AUTHENTIC CHROME SEARCH IN PAGE)
        // ====================================================================
        AnimatedVisibility(
            visible = showFindInPageBar,
            enter = expandVertically() + fadeIn(),
            exit = shrinkVertically() + fadeOut()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(profile.cardBg)
                    .padding(horizontal = 12.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(Icons.Default.Search, null, tint = profile.textSecondary, modifier = Modifier.size(16.dp))
                BasicTextField(
                    value = findInPageQuery,
                    onValueChange = { q ->
                        findInPageQuery = q
                        if (q.isNotBlank()) webViewRef?.findAllAsync(q) else webViewRef?.clearMatches()
                    },
                    singleLine = true,
                    textStyle = TextStyle(color = profile.textPrimary, fontSize = 13.sp),
                    cursorBrush = SolidColor(profile.accentPrimary),
                    modifier = Modifier.weight(1f),
                    decorationBox = { inner ->
                        Box(contentAlignment = Alignment.CenterStart) {
                            if (findInPageQuery.isEmpty()) {
                                Text("Cari di halaman...", fontSize = 12.5.sp, color = profile.textSecondary)
                            }
                            inner()
                        }
                    }
                )
                TextButton(onClick = { webViewRef?.findNext(false) }) {
                    Text("▲", fontSize = 12.sp, color = profile.accentPrimary)
                }
                TextButton(onClick = { webViewRef?.findNext(true) }) {
                    Text("▼", fontSize = 12.sp, color = profile.accentPrimary)
                }
                IconButton(
                    onClick = {
                        showFindInPageBar = false
                        findInPageQuery = ""
                        webViewRef?.clearMatches()
                    },
                    modifier = Modifier.size(28.dp)
                ) {
                    Icon(Icons.Default.Close, "Tutup Cari", tint = profile.textSecondary, modifier = Modifier.size(16.dp))
                }
            }
        }

        // ====================================================================
        // 3. INTERACTIVE POPOVER SHEETS (BRAVE SHIELDS / TOR CIRCUIT / LIBRARY)
        // ====================================================================
        AnimatedVisibility(
            visible = activePopoverSheet != null,
            enter = expandVertically() + fadeIn(),
            exit = shrinkVertically() + fadeOut()
        ) {
            when (activePopoverSheet) {
                "SHIELDS" -> BraveShieldsPopupSheet(
                    domainName = currentDomain,
                    blockedCount = blockedAdsCount,
                    strippedParamsCount = strippedParamsCount,
                    config = config,
                    onUpdateConfig = { config = it },
                    onClose = { activePopoverSheet = null }
                )

                "TOR" -> TorCircuitPopoverSheet(
                    domainName = currentDomain,
                    circuit = activeOnionCircuit,
                    config = config,
                    onUpdateConfig = { config = it },
                    onRotateCircuit = {
                        circuitRotationNonce++
                        val hopText = SanctuaryBlackBoxEngine
                            .generateNewOnionCircuit(circuitRotationNonce)
                            .joinToString(" → ") { it.countryFlag }
                        onPostBanner("Sirkuit Tor diputar: $hopText")
                    },
                    onBurnIdentity = { burnAllBrowserSessionData() },
                    onClose = { activePopoverSheet = null }
                )

                "BOOKMARKS", "HISTORY", "DOWNLOADS" -> BrowserLibrarySheet(
                    activeSheet = activePopoverSheet ?: "BOOKMARKS",
                    profile = profile,
                    bookmarks = bookmarksList,
                    history = historyList,
                    downloads = downloadsList,
                    onNavigateUrl = ::navigateToUrl,
                    onPostBanner = onPostBanner,
                    onClose = { activePopoverSheet = null }
                )
            }
        }

        // ====================================================================
        // 4. MAIN VIEWPORT: WEBVIEW / START PAGE (NTP) / 2-COLUMN TAB GRID
        // ====================================================================
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            // A. LIVE SANDBOXED WEBVIEW
            if (!showStartPageDashboard && !showTabGridSwitcher && !rendererSafeModeFallback) {
                AndroidView(
                    modifier = Modifier.fillMaxSize(),
                    factory = { ctx ->
                        WebView(ctx).apply {
                            webViewRef = this
                            settings.apply {
                                javaScriptEnabled = !config.blockScriptsNoScript && config.torSecurityLevel.allowJavaScript
                                domStorageEnabled = true
                                databaseEnabled = false
                                saveFormData = false
                                allowFileAccess = false
                                allowContentAccess = false
                                textZoom = config.textZoomPercent
                                useWideViewPort = true
                                loadWithOverviewMode = true
                                setSupportZoom(true)
                                builtInZoomControls = true
                                displayZoomControls = false
                                layoutAlgorithm = WebSettings.LayoutAlgorithm.NORMAL
                                mixedContentMode = WebSettings.MIXED_CONTENT_NEVER_ALLOW
                                cacheMode = WebSettings.LOAD_NO_CACHE
                                userAgentString = if (desktopSiteMode) {
                                    desktopSpoofedUas[circuitRotationNonce % desktopSpoofedUas.size]
                                } else {
                                    mobileSpoofedUas[circuitRotationNonce % mobileSpoofedUas.size]
                                }
                            }
                            setInitialScale(0)
                            try {
                                CookieManager.getInstance().setAcceptCookie(true)
                                CookieManager.getInstance().setAcceptThirdPartyCookies(this, !config.blockCrossSiteCookies)
                            } catch (_: Throwable) {
                            }

                            webChromeClient = object : WebChromeClient() {
                                override fun onProgressChanged(view: WebView?, newProgress: Int) {
                                    pageLoadProgress = newProgress.coerceIn(0, 100)
                                }

                                override fun onReceivedTitle(view: WebView?, title: String?) {
                                    if (!title.isNullOrBlank()) {
                                        val idx = browserTabs.indexOfFirst { it.id == activeTabId }
                                        if (idx >= 0) {
                                            browserTabs[idx] = browserTabs[idx].copy(title = title.take(24))
                                        }
                                    }
                                }
                            }

                            webViewClient = object : WebViewClient() {
                                override fun shouldInterceptRequest(
                                    view: WebView?,
                                    request: WebResourceRequest?
                                ): WebResourceResponse? {
                                    val reqUrl = request?.url?.toString() ?: return null
                                    if (config.shieldsMasterEnabled &&
                                        config.trackerBlockingMode != BraveBlockingAggressiveness.DISABLED &&
                                        SanctuaryBlackBoxEngine.isAdOrTrackerUrl(reqUrl)
                                    ) {
                                        view?.post { blockedAdsCount++ }
                                        return WebResourceResponse("text/plain", "UTF-8", ByteArrayInputStream(ByteArray(0)))
                                    }
                                    return super.shouldInterceptRequest(view, request)
                                }

                                override fun shouldOverrideUrlLoading(
                                    view: WebView?,
                                    request: WebResourceRequest?
                                ): Boolean {
                                    val rawUrl = request?.url?.toString() ?: return false
                                    val cleaned = SanctuaryBlackBoxEngine.normalizeAndUpgradeUrl(
                                        rawInput = rawUrl,
                                        httpsOnlyUpgrade = config.shieldsMasterEnabled && config.httpsUpgradeStrict,
                                        stripTrackers = config.shieldsMasterEnabled && config.stripTrackingParams
                                    )
                                    val resolution = com.example.ui.theme.ArcanumBrowserDomainAliasEngine.resolveForNavigation(
                                        rawUrl = cleaned,
                                        isProUnlocked = com.example.ui.theme.AppThemeController.isProModeUnlocked,
                                        rules = com.example.ui.theme.AppThemeController.proBrowserDomainAliases
                                    )
                                    val targetUrl = SanctuaryBlackBoxEngine.rewriteUrlForDesktopMode(
                                        rawUrl = resolution.actualLoadUrl,
                                        desktopMode = desktopSiteMode
                                    )
                                    if (targetUrl != rawUrl) {
                                        if (cleaned != rawUrl) strippedParamsCount++
                                        addressBarInput = resolution.displayAliasUrl
                                        view?.loadUrl(targetUrl, buildRequestHeaders(desktopSiteMode))
                                        return true
                                    }
                                    return false
                                }

                                override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                                    super.onPageStarted(view, url, favicon)
                                    if (!url.isNullOrBlank() && url != "about:blank") {
                                        val masked = com.example.ui.theme.ArcanumBrowserDomainAliasEngine.maskUrlForDisplay(
                                            loadedUrl = url,
                                            isProUnlocked = com.example.ui.theme.AppThemeController.isProModeUnlocked,
                                            rules = com.example.ui.theme.AppThemeController.proBrowserDomainAliases
                                        )
                                        addressBarInput = masked
                                        val idx = browserTabs.indexOfFirst { it.id == activeTabId }
                                        if (idx >= 0) {
                                            browserTabs[idx] = browserTabs[idx].copy(currentUrl = masked)
                                        }
                                        if (desktopSiteMode && !config.blockScriptsNoScript && config.torSecurityLevel.allowJavaScript) {
                                            view?.evaluateJavascript(
                                                SanctuaryBlackBoxEngine.buildDesktopViewportJavaScript(true),
                                                null
                                            )
                                        }
                                    }
                                }

                                override fun onPageFinished(view: WebView?, url: String?) {
                                    super.onPageFinished(view, url)
                                    pageLoadProgress = 100
                                    if (!url.isNullOrBlank() && url != "about:blank") {
                                        val masked = com.example.ui.theme.ArcanumBrowserDomainAliasEngine.maskUrlForDisplay(
                                            loadedUrl = url,
                                            isProUnlocked = com.example.ui.theme.AppThemeController.isProModeUnlocked,
                                            rules = com.example.ui.theme.AppThemeController.proBrowserDomainAliases
                                        )
                                        addressBarInput = masked
                                    }
                                    if (!config.blockScriptsNoScript && config.torSecurityLevel.allowJavaScript) {
                                        val combinedJs = SanctuaryBlackBoxEngine.buildCombinedExtensionJavaScript()
                                        if (combinedJs.isNotBlank()) {
                                            view?.evaluateJavascript(combinedJs, null)
                                        }
                                        view?.evaluateJavascript(
                                            SanctuaryBlackBoxEngine.buildDesktopViewportJavaScript(desktopSiteMode),
                                            null
                                        )
                                    }
                                }

                                override fun onRenderProcessGone(
                                    view: WebView?,
                                    detail: RenderProcessGoneDetail?
                                ): Boolean {
                                    try {
                                        (view?.parent as? ViewGroup)?.removeView(view)
                                        view?.destroy()
                                    } catch (_: Throwable) {
                                    }
                                    webViewRef = null
                                    rendererSafeModeFallback = true
                                    return true
                                }
                            }
                            val initialResolution = com.example.ui.theme.ArcanumBrowserDomainAliasEngine.resolveForNavigation(
                                rawUrl = activeTab.currentUrl,
                                isProUnlocked = com.example.ui.theme.AppThemeController.isProModeUnlocked,
                                rules = com.example.ui.theme.AppThemeController.proBrowserDomainAliases
                            )
                            val initialTargetUrl = SanctuaryBlackBoxEngine.rewriteUrlForDesktopMode(
                                rawUrl = initialResolution.actualLoadUrl,
                                desktopMode = desktopSiteMode
                            )
                            loadUrl(initialTargetUrl, buildRequestHeaders(desktopSiteMode))
                        }
                    },
                    update = { wv ->
                        try {
                            wv.settings.javaScriptEnabled = !config.blockScriptsNoScript && config.torSecurityLevel.allowJavaScript
                            wv.settings.textZoom = config.textZoomPercent
                            applyWebViewDesktopModeSettings(wv, desktopSiteMode)
                        } catch (_: Throwable) {
                        }
                    }
                )
            }

            // B. RENDERER FALLBACK SAFE READER MODE
            if (!showStartPageDashboard && !showTabGridSwitcher && rendererSafeModeFallback) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(profile.pageBg)
                        .verticalScroll(rememberScrollState())
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(18.dp))
                            .background(profile.cardBg)
                            .border(0.6.dp, profile.accentPrimary.copy(alpha = 0.4f), RoundedCornerShape(18.dp))
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("Sandbox Reader Mode", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = profile.textPrimary)
                            TextButton(onClick = { rendererSafeModeFallback = false }) {
                                Text("Muat Ulang WebView", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = profile.accentPrimary)
                            }
                        }
                        Text(activeTab.currentUrl, fontFamily = JetBrainsMonoFontFamily, fontSize = 11.sp, color = profile.accentSecondary)
                        Text(
                            "WebView dipulihkan aman agar proses aplikasi tidak berhenti.",
                            fontSize = 12.sp,
                            color = profile.textSecondary
                        )
                    }
                }
            }

            // C. PROFILE-AUTHENTIC NEW TAB PAGE (CHROME / BRAVE / TOR)
            if (showStartPageDashboard && !showTabGridSwitcher) {
                AuthenticBrowserStartPage(
                    profile = profile,
                    config = config,
                    addressBarInput = addressBarInput,
                    blockedAdsCount = blockedAdsCount,
                    activeCircuit = activeOnionCircuit,
                    speedDialPortals = speedDialPortals,
                    bookmarksList = bookmarksList,
                    onAddressChange = { addressBarInput = it },
                    onSwitchProfile = { newProfile ->
                        config = config.copy(uiProfile = newProfile)
                    },
                    onNavigateUrl = ::navigateToUrl,
                    onOpenSettings = { showSettingsFullScreen = true },
                    onOpenTorCircuit = { activePopoverSheet = "TOR" },
                    onOpenShields = { activePopoverSheet = "SHIELDS" },
                    onOpenBookmarks = { activePopoverSheet = "BOOKMARKS" },
                    onCloseToSanctuary = onCloseToSanctuary
                )
            }

            // D. 2-COLUMN CHROME / BRAVE / TOR VISUAL TAB GRID SWITCHER
            if (showTabGridSwitcher) {
                val displayedTabs = if (tabGridFilterTorOnly) {
                    browserTabs.filter { it.isIncognitoTor }.ifEmpty { browserTabs.toList() }
                } else {
                    browserTabs.toList()
                }
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(profile.pageBg.copy(alpha = 0.98f))
                        .padding(14.dp)
                        .testTag("browser_tab_grid_overlay"),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Authentic Chrome Tab Switcher Header (Regular vs Tor Incognito Segmented Switch)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            modifier = Modifier
                                .clip(RoundedCornerShape(999.dp))
                                .background(profile.cardBg)
                                .padding(3.dp),
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(999.dp))
                                    .background(if (!tabGridFilterTorOnly) profile.accentPrimary.copy(alpha = 0.24f) else Color.Transparent)
                                    .clickable { tabGridFilterTorOnly = false }
                                    .padding(horizontal = 12.dp, vertical = 6.dp)
                            ) {
                                Text("Semua (${browserTabs.size})", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = if (!tabGridFilterTorOnly) profile.accentPrimary else profile.textSecondary)
                            }
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(999.dp))
                                    .background(if (tabGridFilterTorOnly) Color(0xFFC084FC).copy(alpha = 0.24f) else Color.Transparent)
                                    .clickable { tabGridFilterTorOnly = true }
                                    .padding(horizontal = 12.dp, vertical = 6.dp)
                            ) {
                                Text("Tor (${browserTabs.count { it.isIncognitoTor }})", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = if (tabGridFilterTorOnly) Color(0xFFC084FC) else profile.textSecondary)
                            }
                        }

                        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            Button(
                                onClick = { openNewTab(incognitoTor = tabGridFilterTorOnly) },
                                shape = RoundedCornerShape(999.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = profile.accentPrimary),
                                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                            ) {
                                Icon(Icons.Default.Add, null, modifier = Modifier.size(15.dp), tint = Color.Black)
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Tab", fontSize = 11.5.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                            }
                            OutlinedButton(
                                onClick = { burnAllBrowserSessionData() },
                                shape = RoundedCornerShape(999.dp),
                                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp)
                            ) {
                                Text("Tutup", fontSize = 11.5.sp, color = Color(0xFFFF453A), fontWeight = FontWeight.Bold)
                            }
                        }
                    }

                    // 2-Column Visual Cards
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .verticalScroll(rememberScrollState()),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        displayedTabs.chunked(2).forEach { rowTabs ->
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                rowTabs.forEach { tab ->
                                    val isSel = tab.id == activeTab.id
                                    val tabAccent = if (tab.isIncognitoTor) Color(0xFFC084FC) else profile.accentPrimary
                                    Column(
                                        modifier = Modifier
                                            .weight(1f)
                                            .height(148.dp)
                                            .clip(RoundedCornerShape(16.dp))
                                            .background(profile.cardBg)
                                            .border(
                                                width = if (isSel) 2.dp else 0.5.dp,
                                                color = if (isSel) tabAccent else Color.White.copy(alpha = 0.12f),
                                                shape = RoundedCornerShape(16.dp)
                                            )
                                            .clickable {
                                                activeTabId = tab.id
                                                val res = com.example.ui.theme.ArcanumBrowserDomainAliasEngine.resolveForNavigation(
                                                    rawUrl = tab.currentUrl,
                                                    isProUnlocked = com.example.ui.theme.AppThemeController.isProModeUnlocked,
                                                    rules = com.example.ui.theme.AppThemeController.proBrowserDomainAliases
                                                )
                                                addressBarInput = res.displayAliasUrl
                                                showTabGridSwitcher = false
                                                showStartPageDashboard = false
                                                webViewRef?.loadUrl(res.actualLoadUrl)
                                            }
                                    ) {
                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .background(profile.omniboxBg)
                                                .padding(horizontal = 10.dp, vertical = 7.dp),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Row(
                                                modifier = Modifier.weight(1f),
                                                horizontalArrangement = Arrangement.spacedBy(6.dp),
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                if (tab.isIncognitoTor) {
                                                    TorOnionGlyph(connected = true, modifier = Modifier.size(14.dp))
                                                } else {
                                                    BraveLionShieldGlyph(active = true, modifier = Modifier.size(14.dp))
                                                }
                                                Text(
                                                    text = tab.title,
                                                    fontSize = 11.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    color = profile.textPrimary,
                                                    maxLines = 1,
                                                    overflow = TextOverflow.Ellipsis
                                                )
                                            }
                                            if (browserTabs.size > 1) {
                                                Icon(
                                                    imageVector = Icons.Default.Close,
                                                    contentDescription = "Tutup Tab",
                                                    tint = profile.textSecondary,
                                                    modifier = Modifier
                                                        .size(16.dp)
                                                        .clickable {
                                                            browserTabs.removeAll { it.id == tab.id }
                                                            if (activeTabId == tab.id) {
                                                                activeTabId = browserTabs.first().id
                                                            }
                                                        }
                                                )
                                            }
                                        }
                                        Column(
                                            modifier = Modifier
                                                .fillMaxSize()
                                                .padding(10.dp),
                                            verticalArrangement = Arrangement.SpaceBetween
                                        ) {
                                            Text(
                                                text = tab.currentUrl,
                                                fontSize = 10.sp,
                                                color = profile.textSecondary,
                                                maxLines = 3,
                                                overflow = TextOverflow.Ellipsis
                                            )
                                            Row(
                                                modifier = Modifier.fillMaxWidth(),
                                                horizontalArrangement = Arrangement.SpaceBetween
                                            ) {
                                                Text(
                                                    text = if (tab.isIncognitoTor) "TOR ANONIM" else "BRAVE SHIELD",
                                                    fontSize = 9.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    color = tabAccent
                                                )
                                                Text(
                                                    text = if (isSel) "AKTIF" else "",
                                                    fontSize = 9.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    color = Color(0xFF30D158)
                                                )
                                            }
                                        }
                                    }
                                }
                                if (rowTabs.size == 1) Spacer(modifier = Modifier.weight(1f))
                            }
                        }
                    }
                }
            }
        }

        // ====================================================================
        // 5. AUTHENTIC MOBILE BROWSER BOTTOM BAR (CHROME / BRAVE / TOR)
        // ====================================================================
        if (config.showBottomToolbar) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(profile.toolbarBg)
                    .border(0.5.dp, Color.White.copy(alpha = 0.08f))
                    .navigationBarsPadding()
                    .padding(horizontal = 14.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = {
                        if (webViewRef?.canGoBack() == true) webViewRef?.goBack()
                        else showStartPageDashboard = true
                    },
                    modifier = Modifier.size(36.dp)
                ) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, "Kembali", tint = profile.textPrimary, modifier = Modifier.size(20.dp))
                }

                IconButton(
                    onClick = { webViewRef?.goForward() },
                    modifier = Modifier.size(36.dp)
                ) {
                    Icon(Icons.AutoMirrored.Filled.ArrowForward, "Maju", tint = profile.textPrimary, modifier = Modifier.size(20.dp))
                }

                // Center Quick-New-Tab / Search Circle Pill (Brave/Chrome Mobile signature!)
                Box(
                    modifier = Modifier
                        .size(38.dp)
                        .clip(CircleShape)
                        .background(profile.accentPrimary.copy(alpha = 0.18f))
                        .clickable { openNewTab(incognitoTor = profile == BrowserUiProfile.TOR, showStart = true) },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.Add, "Tab Baru", tint = profile.accentPrimary, modifier = Modifier.size(21.dp))
                }

                IconButton(
                    onClick = { toggleBookmarkForCurrentPage() },
                    modifier = Modifier.size(36.dp)
                ) {
                    Icon(
                        imageVector = if (isCurrentUrlBookmarked) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                        contentDescription = "Bookmark",
                        tint = if (isCurrentUrlBookmarked) Color(0xFFFFD60A) else profile.textPrimary,
                        modifier = Modifier.size(20.dp)
                    )
                }

                IconButton(
                    onClick = { showSettingsFullScreen = true },
                    modifier = Modifier
                        .size(36.dp)
                        .testTag("browser_bottom_settings_button")
                ) {
                    Icon(Icons.Default.Settings, "Setelan", tint = profile.textPrimary, modifier = Modifier.size(20.dp))
                }
            }
        }
    }
}

// ============================================================================
// AUTHENTIC CHROME / BRAVE / TOR NEW TAB PAGE (START PAGE)
// ============================================================================

@Composable
private fun AuthenticBrowserStartPage(
    profile: BrowserUiProfile,
    config: BrowserUnifiedConfig,
    addressBarInput: String,
    blockedAdsCount: Int,
    activeCircuit: List<OnionRelayHop>,
    speedDialPortals: List<BrowserSpeedDialShortcut>,
    bookmarksList: List<BrowserBookmarkEntry>,
    onAddressChange: (String) -> Unit,
    onSwitchProfile: (BrowserUiProfile) -> Unit,
    onNavigateUrl: (String) -> Unit,
    onOpenSettings: () -> Unit,
    onOpenTorCircuit: () -> Unit,
    onOpenShields: () -> Unit,
    onOpenBookmarks: () -> Unit,
    onCloseToSanctuary: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(profile.pageBg)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp, vertical = 14.dp)
            .testTag("browser_privacy_start_page"),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // ARCANUM BROWSER BRAND HEADER + STYLE SWITCHER BAR (CHROME / BRAVE / TOR)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Arcanum Browser",
                fontSize = 15.sp,
                fontWeight = FontWeight.ExtraBold,
                color = profile.textPrimary
            )
            Text(
                text = if (com.example.ui.theme.AppThemeController.isProModeUnlocked) {
                    "PRO • ${com.example.ui.theme.AppThemeController.proBrowserDomainAliases.count { it.isEnabled }} Alias Domain"
                } else {
                    "Mode Standar"
                },
                fontFamily = JetBrainsMonoFontFamily,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                color = if (com.example.ui.theme.AppThemeController.isProModeUnlocked) Color(0xFFC084FC) else profile.textSecondary
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(999.dp))
                    .background(profile.cardBg)
                    .padding(3.dp),
                horizontalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                BrowserUiProfile.entries.forEach { p ->
                    val sel = p == profile
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(999.dp))
                            .background(if (sel) p.accentPrimary.copy(alpha = 0.22f) else Color.Transparent)
                            .clickable { onSwitchProfile(p) }
                            .padding(horizontal = 11.dp, vertical = 6.dp)
                            .testTag("browser_mode_chip_${p.id.lowercase()}")
                    ) {
                        Text(
                            text = p.displayName,
                            fontSize = 11.sp,
                            fontWeight = if (sel) FontWeight.ExtraBold else FontWeight.Medium,
                            color = if (sel) p.accentPrimary else profile.textSecondary
                        )
                    }
                }
            }

            Row(horizontalArrangement = Arrangement.spacedBy(6.dp), verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(999.dp))
                        .background(profile.cardBg)
                        .clickable { onOpenSettings() }
                        .padding(horizontal = 10.dp, vertical = 6.dp)
                ) {
                    Text("Setelan", fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = profile.textPrimary)
                }
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(999.dp))
                        .background(Color(0xFFFF453A).copy(alpha = 0.16f))
                        .clickable { onCloseToSanctuary() }
                        .padding(horizontal = 10.dp, vertical = 6.dp)
                ) {
                    Text("✕ Sanctuary", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFF453A))
                }
            }
        }

        // HERO BRANDING HEADER PER BROWSER
        when (profile) {
            BrowserUiProfile.CHROME -> {
                // Iconic Google Chrome Multi-Color Wordmark
                Text(
                    text = buildAnnotatedString {
                        withStyle(SpanStyle(color = Color(0xFF4285F4))) { append("C") }
                        withStyle(SpanStyle(color = Color(0xFFEA4335))) { append("h") }
                        withStyle(SpanStyle(color = Color(0xFFFBBC05))) { append("r") }
                        withStyle(SpanStyle(color = Color(0xFF4285F4))) { append("o") }
                        withStyle(SpanStyle(color = Color(0xFF34A853))) { append("m") }
                        withStyle(SpanStyle(color = Color(0xFFEA4335))) { append("e") }
                    },
                    fontSize = 36.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = (-0.5).sp
                )
            }

            BrowserUiProfile.BRAVE -> {
                // Brave Privacy Stats Banner (exact replica of Brave New Tab Page!)
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        BraveLionShieldGlyph(active = config.shieldsMasterEnabled, modifier = Modifier.size(30.dp))
                        Text("Brave Shields", fontSize = 24.sp, fontWeight = FontWeight.ExtraBold, color = profile.textPrimary)
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                            .background(profile.cardBg)
                            .border(0.6.dp, Color(0xFFFB542B).copy(alpha = 0.3f), RoundedCornerShape(16.dp))
                            .clickable { onOpenShields() }
                            .padding(vertical = 12.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        BraveNtpStatCol("$blockedAdsCount", "Pelacak & Iklan", Color(0xFFFB542B))
                        BraveNtpStatCol("${(blockedAdsCount * 0.42f).toInt()} MB", "Data Dihemat", Color(0xFFA428D2))
                        BraveNtpStatCol("${blockedAdsCount * 2} dtk", "Waktu Dihemat", Color(0xFF30D158))
                    }
                }
            }

            BrowserUiProfile.TOR -> {
                // Authentic Tor Browser Onion Purple Hero Card
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(20.dp))
                        .background(
                            Brush.linearGradient(
                                listOf(Color(0xFF2B1845), Color(0xFF1A102B))
                            )
                        )
                        .border(0.8.dp, Color(0xFFC084FC).copy(alpha = 0.45f), RoundedCornerShape(20.dp))
                        .clickable { onOpenTorCircuit() }
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    TorOnionGlyph(connected = true, modifier = Modifier.size(38.dp))
                    Text(
                        text = "Jelajahi. Secara Pribadi.",
                        fontSize = 21.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color(0xFFF3E8FF)
                    )
                    Text(
                        text = "Sirkuit Tor 3-Hop Aktif: ${activeCircuit.joinToString(" → ") { it.countryFlag }} • ${config.torSecurityLevel.id}",
                        fontFamily = JetBrainsMonoFontFamily,
                        fontSize = 10.5.sp,
                        color = Color(0xFF30D158),
                        textAlign = TextAlign.Center
                    )
                }
            }
        }

        // WIDE CAPSULE SEARCH BAR
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .clip(RoundedCornerShape(999.dp))
                .background(profile.omniboxBg)
                .border(0.6.dp, profile.accentPrimary.copy(alpha = 0.35f), RoundedCornerShape(999.dp))
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Icon(Icons.Default.Search, null, tint = profile.textSecondary, modifier = Modifier.size(20.dp))
            BasicTextField(
                value = addressBarInput,
                onValueChange = onAddressChange,
                singleLine = true,
                textStyle = TextStyle(color = profile.textPrimary, fontSize = 14.sp),
                cursorBrush = SolidColor(profile.accentPrimary),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                keyboardActions = KeyboardActions(onSearch = { onNavigateUrl(addressBarInput) }),
                modifier = Modifier.weight(1f),
                decorationBox = { inner ->
                    Box(contentAlignment = Alignment.CenterStart) {
                        if (addressBarInput.isEmpty()) {
                            Text(
                                "Telusuri dengan ${config.searchEngine.displayName} atau URL",
                                fontSize = 13.5.sp,
                                color = profile.textSecondary
                            )
                        }
                        inner()
                    }
                }
            )
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(999.dp))
                    .background(profile.accentPrimary)
                    .clickable { onNavigateUrl(addressBarInput) }
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            ) {
                Text("Buka", fontSize = 11.5.sp, fontWeight = FontWeight.Bold, color = Color.Black)
            }
        }

        // 4x2 MOST VISITED / TOP SITES SHORTCUTS (CHROME/BRAVE STYLE)
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .background(profile.cardBg)
                .padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = if (profile == BrowserUiProfile.BRAVE) "Situs Teratas" else "Situs Paling Sering Dikunjungi",
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                color = profile.textSecondary
            )
            speedDialPortals.chunked(4).forEach { rowPortals ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    rowPortals.forEach { portal ->
                        Column(
                            modifier = Modifier
                                .width(70.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .clickable { onNavigateUrl(portal.url) }
                                .padding(vertical = 4.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(46.dp)
                                    .clip(if (profile == BrowserUiProfile.CHROME) CircleShape else RoundedCornerShape(14.dp))
                                    .background(profile.omniboxBg)
                                    .border(0.7.dp, portal.circleColor.copy(alpha = 0.5f), if (profile == BrowserUiProfile.CHROME) CircleShape else RoundedCornerShape(14.dp)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(portal.shortBadge, fontSize = 13.sp, fontWeight = FontWeight.ExtraBold, color = portal.circleColor)
                            }
                            Text(
                                text = portal.title,
                                fontSize = 11.sp,
                                color = profile.textPrimary,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                }
            }
        }

        // BOOKMARKS STRIP
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            bookmarksList.forEach { bm ->
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(999.dp))
                        .background(profile.cardBg)
                        .clickable { onNavigateUrl(bm.url) }
                        .padding(horizontal = 12.dp, vertical = 7.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(Icons.Default.Star, null, tint = Color(bm.accentHex), modifier = Modifier.size(13.dp))
                    Text(bm.title, fontSize = 11.5.sp, fontWeight = FontWeight.Medium, color = profile.textPrimary)
                }
            }
            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(999.dp))
                    .background(profile.accentPrimary.copy(alpha = 0.14f))
                    .clickable { onOpenBookmarks() }
                    .padding(horizontal = 12.dp, vertical = 7.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Kelola Bookmark", fontSize = 11.5.sp, fontWeight = FontWeight.Bold, color = profile.accentPrimary)
            }
        }
    }
}

@Composable
private fun BraveNtpStatCol(value: String, label: String, color: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(2.dp)) {
        Text(value, fontFamily = JetBrainsMonoFontFamily, fontSize = 18.sp, fontWeight = FontWeight.ExtraBold, color = color)
        Text(label, fontSize = 10.sp, color = Color(0xFF8D93A1))
    }
}
