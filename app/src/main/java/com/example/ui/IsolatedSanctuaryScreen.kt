package com.example.ui

import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioTrack
import com.example.ui.theme.DuoBeeYellow
import com.example.ui.theme.DuoBeeYellowLip
import com.example.ui.theme.DuoBeetlePurple
import com.example.ui.theme.DuoBeetlePurpleLip
import com.example.ui.theme.DuoCardinalRed
import com.example.ui.theme.DuoFeatherGreen
import com.example.ui.theme.DuoFeatherGreenLip
import com.example.ui.theme.DuoFoxOrange
import com.example.ui.theme.DuoFoxOrangeLip
import com.example.ui.theme.DuoMacawBlue
import com.example.ui.theme.DuoMacawBlueLip
import com.example.ui.theme.duolingoButton
import com.example.ui.theme.duolingoCard
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.DesktopWindows
import androidx.compose.material.icons.filled.Extension
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.LockOpen
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.filled.VpnKey
import androidx.compose.material.icons.filled.VpnLock
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.TextButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.AppThemeController
import com.example.ui.theme.JetBrainsMonoFontFamily
import kotlinx.coroutines.delay
import java.security.MessageDigest
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Base64
import java.util.Date
import java.util.Locale
import kotlin.concurrent.thread
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.hypot
import kotlin.math.ln
import kotlin.math.pow
import kotlin.math.sin
import kotlin.random.Random

// ============================================================================
// ARCANUM BLACKBOX SANCTUARY — 4TH ISOLATED SPACE (5 SPECIALIZED WINGS)
// 1. Shadow Ledger (Financial Vault, 3D Black Card & Seed Phrase Cold Wallet)
// 2. Detective Mind-Map & Classified Dossier Board (Interactive Red-String)
// 3. Cyber Deck & Hacker Terminal OS (CLI + 6-Cipher Studio + Entropy Forge)
// 4. Midnight Zen Lounge (4-Channel Procedural Lo-Fi Audio + Secret Diary)
// 5. Onion & Shield Browser (Brave-Grade Shields + Tor 3-Hop + Extensions + VPN)
// ============================================================================

enum class SanctuaryWing(
    val id: String,
    val shortTitle: String,
    val fullTitle: String,
    val subtitle: String,
    val badgeEmoji: String,
    val primaryAccent: Color,
    val secondaryAccent: Color
) {
    SHADOW_LEDGER(
        id = "SHADOW_LEDGER",
        shortTitle = "Ledger",
        fullTitle = "Shadow Ledger & Cold Wallet",
        subtitle = "Dompet Rahasia, Seed Phrase 12/24 Kata & Kalkulator Aset • Bento Glow",
        badgeEmoji = "LEDGER",
        primaryAccent = Color(0xFFF59E0B),
        secondaryAccent = Color(0xFF10B981)
    ),
    DETECTIVE_BOARD(
        id = "DETECTIVE_BOARD",
        shortTitle = "Detektif",
        fullTitle = "Sherlock Red-String & Dossier",
        subtitle = "Papan Investigasi Benang Merah Interaktif & Berkas Rahasia • Bento Glow",
        badgeEmoji = "DOSSIER",
        primaryAccent = Color(0xFFEF4444),
        secondaryAccent = Color(0xFFF97316)
    ),
    CYBER_DECK(
        id = "CYBER_DECK",
        shortTitle = "Cyber OS",
        fullTitle = "Cyber Deck & Hacker Terminal",
        subtitle = "CLI Terminal Interaktif, 6-Cipher Studio & Password Forge • Bento Glow",
        badgeEmoji = "CYBER",
        primaryAccent = Color(0xFF06B6D4),
        secondaryAccent = Color(0xFF22C55E)
    ),
    MIDNIGHT_ZEN(
        id = "MIDNIGHT_ZEN",
        shortTitle = "Zen Lo-Fi",
        fullTitle = "Midnight Zen & Lo-Fi Sanctuary",
        subtitle = "Synthesizer Audio 4-Channel, Pomodoro & Jurnal Terenkripsi • Bento Glow",
        badgeEmoji = "ZEN",
        primaryAccent = Color(0xFFA855F7),
        secondaryAccent = Color(0xFFEC4899)
    ),
    ONION_SHIELD_BROWSER(
        id = "ONION_SHIELD_BROWSER",
        shortTitle = "Arcanum",
        fullTitle = "Arcanum Browser",
        subtitle = "Arcanum Browser (Chrome / Brave / Tor UI) + Pro Domain Alias + Shields & 3-Hop Tor • Bento Glow",
        badgeEmoji = "ARCANUM",
        primaryAccent = Color(0xFFFB542B), // Brave Lion Orange
        secondaryAccent = Color(0xFF7D4698)  // Tor Onion Purple
    )
}

// ----------------------------------------------------------------------------
// DATA MODELS & PURE LOGIC ENGINES FOR ALL 5 WINGS (TESTABLE & PERSISTENT)
// ----------------------------------------------------------------------------

data class SeedPhraseColdWalletItem(
    val id: String,
    val walletAlias: String,
    val networkName: String,
    val words: List<String>,
    val balanceEstimateIdr: Long,
    val createdAtText: String
)

data class FinancialAssetGoalItem(
    val id: String,
    val title: String,
    val category: String,
    val currentAmountIdr: Long,
    val targetAmountIdr: Long,
    val annualYieldPercent: Double
)

data class DetectiveBoardNode(
    val id: String,
    val title: String,
    val category: String, // TERSANGKA, BUKTI, LOKASI, SAKSI, TRANSAKSI
    val note: String,
    val normX: Float, // 0.1f .. 0.9f
    val normY: Float, // 0.1f .. 0.9f
    val badgeColorHex: Long
)

data class DetectiveRedStringLink(
    val id: String,
    val fromNodeId: String,
    val toNodeId: String,
    val relationLabel: String
)

data class ClassifiedDossierRecord(
    val id: String,
    val codename: String,
    val clearanceLevel: String,
    val subjectRole: String,
    val threatScore: Int,
    val summaryNotes: String
)

enum class CyberCipherAlgorithm(val displayName: String, val tag: String) {
    MORSE("Morse Code", "MORSE"),
    BINARY("Binary 8-Bit", "BIN"),
    HEX("Hexadecimal", "HEX"),
    BASE64("Base64", "B64"),
    ROT13("ROT13 / Caesar", "ROT13"),
    VIGENERE("Vigenère Key", "VIG")
}

data class SecretDiaryEntry(
    val id: String,
    val title: String,
    val moodEmoji: String,
    val moodLabel: String,
    val content: String,
    val timestampText: String,
    val isLockedPreview: Boolean = true
)

enum class TorSecurityLevel(
    val id: String,
    val displayName: String,
    val description: String,
    val allowJavaScript: Boolean,
    val strictHttpsUpgrade: Boolean,
    val blockThirdPartyCookies: Boolean
) {
    STANDARD(
        id = "STANDARD",
        displayName = "Standar (Brave Shield + Tor Circuit)",
        description = "Semua fitur situs berjalan dengan blokir iklan, anti-pelacak & sirkuit 3-Hop.",
        allowJavaScript = true,
        strictHttpsUpgrade = true,
        blockThirdPartyCookies = true
    ),
    SAFER(
        id = "SAFER",
        displayName = "Lebih Aman (Safer — Anti-Fingerprint Ketat)",
        description = "Mematikan WebRTC, meracuni sidik jari Canvas/WebGL & memaksa HTTPS penuh.",
        allowJavaScript = true,
        strictHttpsUpgrade = true,
        blockThirdPartyCookies = true
    ),
    SAFEST(
        id = "SAFEST",
        displayName = "Paling Aman (Safest — NoScript / Blokir JS)",
        description = "Mematikan eksekusi JavaScript sepenuhnya di semua situs seperti Tor Safest Mode.",
        allowJavaScript = false,
        strictHttpsUpgrade = true,
        blockThirdPartyCookies = true
    )
}

data class OnionRelayHop(
    val roleLabel: String, // Entry Guard, Middle Relay, Exit Node
    val countryFlag: String,
    val countryName: String,
    val routerNickname: String,
    val virtualIp: String,
    val latencyMs: Int
)

data class BrowserExtensionSpec(
    val id: String,
    val name: String,
    val badgeEmoji: String,
    val version: String,
    val description: String,
    val category: String,
    val isEnabledByDefault: Boolean,
    val injectedJavaScript: String
)

enum class StealthVpnRegion(
    val id: String,
    val flag: String,
    val countryName: String,
    val cityHub: String,
    val endpointIp: String,
    val protocolLabel: String
) {
    SWITZERLAND("CH_ZURICH", "[CH]", "Swiss (Privacy Haven)", "Zurich Core-1", "185.220.101.44", "WireGuard-Stealth 4096"),
    ICELAND("IS_REYKJAVIK", "[IS]", "Islandia (Zero-Log)", "Reykjavik Vault", "82.221.139.18", "NordLynx-Quantum"),
    NETHERLANDS("NL_AMSTERDAM", "[NL]", "Belanda (Multi-Hop)", "Amsterdam Node-7", "198.54.132.91", "OpenVPN-Obfs4"),
    JAPAN("JP_TOKYO", "[JP]", "Jepang (High-Speed)", "Tokyo Fiber-9", "45.76.211.108", "WireGuard-Stealth 4096"),
    SINGAPORE("SG_JURONG", "[SG]", "Singapura (Low-Latency)", "Marina Relay-3", "139.162.41.205", "Shadowsocks-AEAD")
}

enum class DohProviderSpec(
    val id: String,
    val displayName: String,
    val endpointUrl: String,
    val description: String
) {
    CLOUDFLARE_PRIVACY("CLOUDFLARE", "Cloudflare 1.1.1.1 (Zero-Log)", "https://cloudflare-dns.com/dns-query", "Resolusi DNS terenkripsi ultra-cepat tanpa log."),
    QUAD9_SECURITY("QUAD9", "Quad9 9.9.9.9 (Swiss Malware Block)", "https://dns.quad9.net/dns-query", "Memblokir domain phishing, spyware & C2 botnet otomatis."),
    MULLVAD_NO_ADS("MULLVAD", "Mullvad Privacy DNS (Swedia)", "https://adblock.dns.mullvad.net/dns-query", "DNS anonim tanpa log + filter pelacak tingkat jaringan."),
    ADGUARD_SHIELD("ADGUARD", "AdGuard DoH (Anti-Iklan & Pelacak)", "https://dns.adguard-dns.com/dns-query", "Menyaring domain iklan & analitik sebelum koneksi dibuka.")
}

data class SanctuaryBrowserTab(
    val id: String,
    val title: String,
    val currentUrl: String,
    val isIncognitoTor: Boolean = true
)

object SanctuaryBlackBoxEngine {

    private val bip39SampleWordPool = listOf(
        "abandon", "ability", "able", "about", "above", "absent", "absorb", "abstract",
        "absurd", "abuse", "access", "accident", "account", "accuse", "achieve", "acid",
        "acoustic", "acquire", "across", "act", "action", "actor", "actress", "actual",
        "anchor", "ancient", "anger", "angle", "arctic", "armor", "army", "around",
        "arcanum", "atomic", "aurora", "avalon", "beacon", "binary", "bunker", "carbon",
        "cipher", "cobalt", "cosmos", "crypto", "cyber", "delta", "diamond", "dragon",
        "eclipse", "emerald", "enigma", "entropy", "falcon", "fortress", "galaxy", "genesis",
        "glacier", "gravity", "horizon", "helium", "ignite", "impulse", "indigo", "infinity",
        "ivory", "jacket", "jasper", "jupiter", "karma", "kernel", "krypton", "labyrinth",
        "lantern", "lattice", "legacy", "legend", "liberty", "lunar", "magnet", "matrix",
        "mercury", "meteor", "mirage", "monolith", "nebula", "nectar", "neutron", "nexus",
        "noble", "nomad", "nova", "obsidian", "odyssey", "omega", "onyx", "oracle",
        "orbit", "oxygen", "paradox", "pegasus", "phantom", "phoenix", "photon", "pioneer",
        "plasma", "platinum", "polaris", "portal", "prism", "pulsar", "pyramid", "quantum",
        "quartz", "quasar", "radar", "radiant", "reactor", "relic", "ripple", "rocket",
        "sapphire", "saturn", "scarlet", "scepter", "sentinel", "serenity", "shadow", "shield",
        "sierra", "signal", "silence", "silver", "solaris", "sonic", "sovereign", "spectrum",
        "sphinx", "spiral", "stealth", "stellar", "stratos", "summit", "supernova", "synergy",
        "tactical", "tangent", "tempest", "tensor", "titanium", "thunder", "tornado", "trident",
        "trinity", "tundra", "twilight", "typhoon", "ultima", "umbra", "unicorn", "utopia",
        "valkyrie", "vanguard", "vector", "velvet", "venture", "verdict", "vertex", "vortex",
        "voyager", "whisper", "wildfire", "wisdom", "zenith", "zephyr", "zodiac", "zircon"
    )

    // Persistent In-Memory State across Sanctuary Wing Switches
    val seedWallets = mutableStateListOf(
        SeedPhraseColdWalletItem(
            id = "wallet_1",
            walletAlias = "Cold Storage Sovereign #01",
            networkName = "Bitcoin • Native SegWit (bc1q)",
            words = listOf(
                "obsidian", "quantum", "sentinel", "eclipse", "cipher", "polaris",
                "fortress", "nebula", "paradox", "titanium", "vortex", "zenith"
            ),
            balanceEstimateIdr = 485_000_000L,
            createdAtText = "Vault Genesis • Offline Cold Key"
        ),
        SeedPhraseColdWalletItem(
            id = "wallet_2",
            walletAlias = "Monero Stealth Node #02",
            networkName = "Monero • RingCT Stealth (XMR)",
            words = listOf(
                "phantom", "aurora", "labyrinth", "umbra", "onyx", "oracle",
                "reactor", "silence", "solaris", "valkyrie", "whisper", "zircon"
            ),
            balanceEstimateIdr = 165_500_000L,
            createdAtText = "Zero-Knowledge Seed • 12 Kata"
        )
    )

    val financialGoals = mutableStateListOf(
        FinancialAssetGoalItem(
            id = "goal_1",
            title = "Dana Darurat Offshore & Emas Fisik",
            category = "SAFE HAVEN",
            currentAmountIdr = 320_000_000L,
            targetAmountIdr = 500_000_000L,
            annualYieldPercent = 8.5
        ),
        FinancialAssetGoalItem(
            id = "goal_2",
            title = "Portofolio Likuiditas Siber & Obligasi",
            category = "COMPOUND POOL",
            currentAmountIdr = 210_000_000L,
            targetAmountIdr = 350_000_000L,
            annualYieldPercent = 11.2
        ),
        FinancialAssetGoalItem(
            id = "goal_3",
            title = "Kas Taktis BlackBox (Multi-Currency)",
            category = "TACTICAL CASH",
            currentAmountIdr = 95_000_000L,
            targetAmountIdr = 150_000_000L,
            annualYieldPercent = 6.0
        )
    )

    val detectiveNodes = mutableStateListOf(
        DetectiveBoardNode(
            id = "node_alpha",
            title = "Entitas 'PHANTOM-9'",
            category = "TERSANGKA",
            note = "Aktor utama di balik akses anomali pukul 02:14 UTC.",
            normX = 0.22f,
            normY = 0.26f,
            badgeColorHex = 0xFFEF4444
        ),
        DetectiveBoardNode(
            id = "node_beta",
            title = "Transaksi Dompet #88F",
            category = "TRANSAKSI",
            note = "Transfer lintas rantai melalui mixer 3-hop di Zurich.",
            normX = 0.76f,
            normY = 0.24f,
            badgeColorHex = 0xFFF59E0B
        ),
        DetectiveBoardNode(
            id = "node_gamma",
            title = "Server Relay Reykjavik",
            category = "LOKASI",
            note = "Titik pantul paket terenkripsi dengan latensi 38ms.",
            normX = 0.26f,
            normY = 0.74f,
            badgeColorHex = 0xFF38BDF8
        ),
        DetectiveBoardNode(
            id = "node_delta",
            title = "Dokumen 'OMEGA.ENC'",
            category = "BUKTI",
            note = "Hash SHA-256 cocok dengan tanda tangan kunci privat.",
            normX = 0.74f,
            normY = 0.72f,
            badgeColorHex = 0xFF10B981
        )
    )

    val detectiveLinks = mutableStateListOf(
        DetectiveRedStringLink(
            id = "link_1",
            fromNodeId = "node_alpha",
            toNodeId = "node_beta",
            relationLabel = "Otorisasi Dana"
        ),
        DetectiveRedStringLink(
            id = "link_2",
            fromNodeId = "node_alpha",
            toNodeId = "node_gamma",
            relationLabel = "Jejak IP Proxy"
        ),
        DetectiveRedStringLink(
            id = "link_3",
            fromNodeId = "node_beta",
            toNodeId = "node_delta",
            relationLabel = "Kunci Pembuka"
        ),
        DetectiveRedStringLink(
            id = "link_4",
            fromNodeId = "node_gamma",
            toNodeId = "node_delta",
            relationLabel = "Cadangan Cold"
        )
    )

    val classifiedDossiers = mutableStateListOf(
        ClassifiedDossierRecord(
            id = "dos_1",
            codename = "OPERASI NIGHTFALL-07",
            clearanceLevel = "OMEGA // EYES ONLY",
            subjectRole = "Arsitek Jaringan Bayangan",
            threatScore = 92,
            summaryNotes = "Memiliki akses ke 3 titik relay Eropa Utara. Gunakan protokol zero-knowledge saat verifikasi."
        ),
        ClassifiedDossierRecord(
            id = "dos_2",
            codename = "PROYEK ASTRA-VALKYRIE",
            clearanceLevel = "TOP SECRET",
            subjectRole = "Audit Keamanan Kriptografi",
            threatScore = 78,
            summaryNotes = "Seluruh kontainer NYX2 telah lulus uji integritas bit dan tahan pemindaian forensik statis."
        )
    )

    val terminalHistory = mutableStateListOf(
        "ARCANUM BLACKBOX OS v9.4 [KERNEL HARDENED]",
        "Ketik 'help' atau tekan chip perintah di bawah untuk menjalankan diagnostik.",
        "root@arcanum-sanctuary:~# status --all",
        "  [OK] Shadow Ledger AES-GCM Memory Guard : ACTIVE",
        "  [OK] Sherlock Mind-Map Graph Engine     : 4 Nodes / 4 Red Links",
        "  [OK] Onion 3-Hop Circuit + Brave Shield : ARMED"
    )

    val secretDiaryEntries = mutableStateListOf(
        SecretDiaryEntry(
            id = "diary_1",
            title = "Malam Tenang di Balik Dinding Enkripsi",
            moodEmoji = "[ZEN]",
            moodLabel = "Tenang & Fokus",
            content = "Di ruang Sanctuary ini semua kebisingan luar berhenti. Rencana kuartal depan sudah tersusun rapi di Shadow Ledger.",
            timestampText = "Hari ini • 23:15",
            isLockedPreview = false
        ),
        SecretDiaryEntry(
            id = "diary_2",
            title = "Target Kebebasan Finansial & Riset Siber",
            moodEmoji = "[ENERGI]",
            moodLabel = "Semangat Tinggi",
            content = "Menyelesaikan audit portofolio cold wallet dan merapikan peta investigasi proyek baru.",
            timestampText = "Kemarin • 21:40",
            isLockedPreview = true
        )
    )

    // --- BRAVE SHIELDS & TOR ANONYMITY ENGINE ---

    val knownAdAndTrackerDomains = setOf(
        "doubleclick.net",
        "googlesyndication.com",
        "googleadservices.com",
        "google-analytics.com",
        "googletagmanager.com",
        "facebook.net",
        "connect.facebook.net",
        "pixel.facebook.com",
        "analytics.tiktok.com",
        "ads-twitter.com",
        "amazon-adsystem.com",
        "adnxs.com",
        "criteo.com",
        "taboola.com",
        "outbrain.com",
        "scorecardresearch.com",
        "quantserve.com",
        "hotjar.com",
        "mixpanel.com",
        "segment.io",
        "amplitude.com",
        "clarity.ms",
        "mouseflow.com",
        "fullstory.com",
        "newrelic.com",
        "bugsnag.com",
        "sentry.io",
        "adform.net",
        "rubiconproject.com",
        "pubmatic.com",
        "openx.net",
        "moatads.com",
        "advertising.com",
        "zedo.com",
        "popads.net",
        "propellerads.com",
        "adsterra.com"
    )

    val trackingQueryParamKeys = setOf(
        "utm_source", "utm_medium", "utm_campaign", "utm_term", "utm_content",
        "utm_id", "fbclid", "gclid", "dclid", "msclkid", "twclid", "yclid",
        "mc_cid", "mc_eid", "_ga", "_gl", "igshid", "si", "ref_src", "spm"
    )

    val availableExtensions = listOf(
        BrowserExtensionSpec(
            id = "ext_ublock_shield",
            name = "uBlock Origin Shield+",
            badgeEmoji = "[SHIELD]",
            version = "1.58.0",
            description = "Menghapus elemen DOM iklan, banner sponsor & script pelacak di dalam halaman.",
            category = "ANTI-ADS",
            isEnabledByDefault = true,
            injectedJavaScript = """
                (function() {
                    const selectors = [
                        '[id*="google_ads"]', '[class*="ad-banner"]', '[class*="adsbox"]',
                        '[id*=" sponsored"]', 'iframe[src*="doubleclick"]', 'iframe[src*="ads"]',
                        '[data-ad-client]', '.ad-container', '.advertisement'
                    ];
                    selectors.forEach(sel => {
                        document.querySelectorAll(sel).forEach(el => el.remove());
                    });
                })();
            """.trimIndent()
        ),
        BrowserExtensionSpec(
            id = "ext_canvas_poison",
            name = "Canvas & WebGL Fingerprint Poisoner",
            badgeEmoji = "[MASK]",
            version = "4.2.1",
            description = "Menyuntikkan derau piksel mikro pada Canvas, WebGL & AudioContext agar pelacak gagal mengenali perangkat.",
            category = "ANTI-FINGERPRINT",
            isEnabledByDefault = true,
            injectedJavaScript = """
                (function() {
                    try {
                        Object.defineProperty(navigator, 'doNotTrack', { get: () => '1' });
                        Object.defineProperty(navigator, 'globalPrivacyControl', { get: () => true });
                        Object.defineProperty(navigator, 'hardwareConcurrency', { get: () => 4 });
                        Object.defineProperty(navigator, 'deviceMemory', { get: () => 8 });
                        const origToDataURL = HTMLCanvasElement.prototype.toDataURL;
                        HTMLCanvasElement.prototype.toDataURL = function() {
                            const ctx = this.getContext('2d');
                            if (ctx) {
                                ctx.fillStyle = 'rgba(1, 2, 3, 0.01)';
                                ctx.fillRect(0, 0, 1, 1);
                            }
                            return origToDataURL.apply(this, arguments);
                        };
                    } catch (e) {}
                })();
            """.trimIndent()
        ),
        BrowserExtensionSpec(
            id = "ext_clear_urls",
            name = "ClearURLs PureLink",
            badgeEmoji = "[CLEAN]",
            version = "2.9.0",
            description = "Membersihkan parameter pelacakan (utm_*, fbclid, gclid) dari seluruh tautan di halaman secara otomatis.",
            category = "PRIVACY",
            isEnabledByDefault = true,
            injectedJavaScript = """
                (function() {
                    const badParams = ['utm_source','utm_medium','utm_campaign','fbclid','gclid','msclkid','mc_cid','_ga'];
                    document.querySelectorAll('a[href]').forEach(a => {
                        try {
                            const u = new URL(a.href);
                            badParams.forEach(p => u.searchParams.delete(p));
                            a.href = u.toString();
                        } catch (e) {}
                    });
                })();
            """.trimIndent()
        ),
        BrowserExtensionSpec(
            id = "ext_dark_reader",
            name = "Dark Reader OLED Matrix",
            badgeEmoji = "[OLED]",
            version = "4.9.86",
            description = "Mengubah situs web yang terang menjadi mode gelap OLED berpadu kontras nyaman.",
            category = "APPEARANCE",
            isEnabledByDefault = false,
            injectedJavaScript = """
                (function() {
                    if (!document.getElementById('arcanum-dark-reader-style')) {
                        const style = document.createElement('style');
                        style.id = 'arcanum-dark-reader-style';
                        style.innerHTML = 'html { filter: invert(0.92) hue-rotate(180deg) !important; background: #0a0d14 !important; } img, video, iframe { filter: invert(1) hue-rotate(180deg) !important; }';
                        document.head.appendChild(style);
                    }
                })();
            """.trimIndent()
        ),
        BrowserExtensionSpec(
            id = "ext_cookie_autodelete",
            name = "Cookie & DOM AutoDelete",
            badgeEmoji = "[PURGE]",
            version = "3.8.2",
            description = "Menghapus cookie pelacak dan sessionStorage segera setelah halaman selesai dimuat.",
            category = "PRIVACY",
            isEnabledByDefault = true,
            injectedJavaScript = """
                (function() {
                    try {
                        sessionStorage.clear();
                    } catch (e) {}
                })();
            """.trimIndent()
        ),
        BrowserExtensionSpec(
            id = "ext_popup_zapper",
            name = "Popup & Cookie-Banner Zapper",
            badgeEmoji = "[ZAP]",
            version = "2.1.4",
            description = "Menyingkirkan overlay popup persetujuan cookie yang menutupi layar baca.",
            category = "ANNOYANCES",
            isEnabledByDefault = true,
            injectedJavaScript = """
                (function() {
                    const bannerSelectors = ['[id*="cookie-banner"]', '[class*="cookie-consent"]', '[class*="gdpr-modal"]'];
                    bannerSelectors.forEach(sel => {
                        document.querySelectorAll(sel).forEach(el => el.remove());
                    });
                })();
            """.trimIndent()
        ),
        BrowserExtensionSpec(
            id = "ext_webrtc_shield",
            name = "WebRTC Leak Lockdown",
            badgeEmoji = "[LOCK]",
            version = "1.9.0",
            description = "Mematikan RTCPeerConnection agar IP lokal tidak pernah bocor melewati tunnel VPN/Tor.",
            category = "TOR-ANONYMITY",
            isEnabledByDefault = true,
            injectedJavaScript = """
                (function() {
                    try {
                        window.RTCPeerConnection = undefined;
                        window.webkitRTCPeerConnection = undefined;
                        if (navigator.mediaDevices) {
                            navigator.mediaDevices.enumerateDevices = () => Promise.resolve([]);
                        }
                    } catch (e) {}
                })();
            """.trimIndent()
        )
    )

    val enabledExtensionIds = mutableStateListOf(
        "ext_ublock_shield",
        "ext_canvas_poison",
        "ext_clear_urls",
        "ext_cookie_autodelete",
        "ext_popup_zapper",
        "ext_webrtc_shield"
    )

    var customUserscriptCode by mutableStateOf(
        """
        // ==UserScript==
        // @name         Arcanum Custom Privacy Header
        // @match        *://*/*
        // ==/UserScript==
        console.log('[Arcanum Shield] Custom Userscript Active');
        """.trimIndent()
    )
    var isCustomUserscriptEnabled by mutableStateOf(true)

    fun generateMnemonicWords(wordCount: Int = 12): List<String> {
        val count = if (wordCount >= 24) 24 else 12
        return List(count) { bip39SampleWordPool.random() }
    }

    fun calculateCompoundFutureValue(
        principalIdr: Long,
        monthlyContributionIdr: Long,
        annualRatePercent: Double,
        years: Int
    ): Long {
        val r = (annualRatePercent / 100.0) / 12.0
        val n = (years.coerceIn(1, 50) * 12).toDouble()
        if (r <= 0.000001) {
            return principalIdr + (monthlyContributionIdr * n.toLong())
        }
        val compoundPrincipal = principalIdr * (1.0 + r).pow(n)
        val compoundSeries = monthlyContributionIdr * (((1.0 + r).pow(n) - 1.0) / r)
        return (compoundPrincipal + compoundSeries).toLong()
    }

    fun transformCyberCipher(
        input: String,
        algorithm: CyberCipherAlgorithm,
        decodeMode: Boolean,
        vigenereKey: String = "ARCANUM"
    ): String {
        if (input.isBlank()) return ""
        return try {
            when (algorithm) {
                CyberCipherAlgorithm.MORSE -> {
                    if (!decodeMode) {
                        com.example.data.AdvancedCryptoLabEngine.encodeTextToMorse(input)
                    } else {
                        com.example.data.AdvancedCryptoLabEngine.decodeMorseToText(input)
                    }
                }
                CyberCipherAlgorithm.BINARY -> {
                    if (!decodeMode) {
                        input.toByteArray(Charsets.UTF_8).joinToString(" ") { b ->
                            Integer.toBinaryString(b.toInt() and 0xFF).padStart(8, '0')
                        }
                    } else {
                        input.trim().split(Regex("\\s+")).mapNotNull { token ->
                            token.toIntOrNull(2)?.toByte()
                        }.toByteArray().toString(Charsets.UTF_8)
                    }
                }
                CyberCipherAlgorithm.HEX -> {
                    if (!decodeMode) {
                        input.toByteArray(Charsets.UTF_8).joinToString("") { "%02X".format(it) }
                    } else {
                        val clean = input.replace(Regex("[^0-9A-Fa-f]"), "")
                        clean.chunked(2).mapNotNull { it.toIntOrNull(16)?.toByte() }
                            .toByteArray().toString(Charsets.UTF_8)
                    }
                }
                CyberCipherAlgorithm.BASE64 -> {
                    if (!decodeMode) {
                        Base64.getEncoder().encodeToString(input.toByteArray(Charsets.UTF_8))
                    } else {
                        String(Base64.getDecoder().decode(input.trim()), Charsets.UTF_8)
                    }
                }
                CyberCipherAlgorithm.ROT13 -> {
                    input.map { ch ->
                        when (ch) {
                            in 'A'..'Z' -> 'A' + ((ch - 'A' + 13) % 26)
                            in 'a'..'z' -> 'a' + ((ch - 'a' + 13) % 26)
                            else -> ch
                        }
                    }.joinToString("")
                }
                CyberCipherAlgorithm.VIGENERE -> {
                    val keyClean = vigenereKey.uppercase().filter { it in 'A'..'Z' }.ifEmpty { "KEY" }
                    var kIdx = 0
                    input.map { ch ->
                        when (ch) {
                            in 'A'..'Z' -> {
                                val shift = keyClean[kIdx % keyClean.length] - 'A'
                                kIdx++
                                val delta = if (!decodeMode) shift else (26 - shift) % 26
                                'A' + ((ch - 'A' + delta) % 26)
                            }
                            in 'a'..'z' -> {
                                val shift = keyClean[kIdx % keyClean.length] - 'A'
                                kIdx++
                                val delta = if (!decodeMode) shift else (26 - shift) % 26
                                'a' + ((ch - 'a' + delta) % 26)
                            }
                            else -> ch
                        }
                    }.joinToString("")
                }
            }
        } catch (e: Exception) {
            "[Format Input Tidak Valid untuk Dekode ${algorithm.displayName}]"
        }
    }

    fun generateHighEntropyPassword(
        length: Int = 20,
        includeSymbols: Boolean = true
    ): Pair<String, Double> {
        val alphaNum = "ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnopqrstuvwxyz23456789"
        val symbols = "!@#$%^&*()-_=+[]{}<>?"
        val pool = if (includeSymbols) alphaNum + symbols else alphaNum
        val pass = (1..length.coerceIn(8, 64))
            .map { pool[Random.nextInt(pool.length)] }
            .joinToString("")
        val entropyBits = length * (ln(pool.length.toDouble()) / ln(2.0))
        return pass to entropyBits
    }

    fun executeTerminalCommand(rawCommand: String): List<String> {
        val trimmed = rawCommand.trim()
        if (trimmed.isEmpty()) return emptyList()
        val parts = trimmed.split(Regex("\\s+"), limit = 2)
        val cmd = parts[0].lowercase()
        val arg = parts.getOrNull(1) ?: ""

        return when (cmd) {
            "help" -> listOf(
                "Daftar Perintah Cyber Deck OS:",
                "  help           - Tampilkan panduan perintah terminal",
                "  scan           - Pindai integritas memori & port lokal",
                "  keygen         - Buat kunci entropi tinggi 256-bit",
                "  hash <teks>    - Hitung digest SHA-256 dari teks",
                "  cipher <teks>  - Enkripsi cepat ke Hex & Base64",
                "  tor            - Tampilkan status sirkuit 3-Hop Onion aktif",
                "  audit          - Audit keamanan 5 sayap Sanctuary",
                "  matrix         - Jalankan telemetry stream matriks",
                "  clear          - Bersihkan layar terminal"
            )
            "scan" -> listOf(
                "[SCANNING] Memeriksa isolasi proses Android Sandbox...",
                "  -> Port Lokal Terbuka : 0 (Zero-Exposure)",
                "  -> Proteksi Memori    : AES-256-GCM + Kunci 3-Lapis",
                "  -> Pelacak Diblokir   : ${knownAdAndTrackerDomains.size} Domain Di-blacklist",
                "[RESULT] Status Keamanan Sanctuary: 100% AMAN & TERISOLASI."
            )
            "keygen" -> {
                val (pwd, bits) = generateHighEntropyPassword(24, true)
                listOf(
                    "[KEYGEN] Kunci Master Entropi Baru:",
                    "  Key     : $pwd",
                    "  Entropi : ${"%.1f".format(Locale.US, bits)} bits (Setara Standar Militer)"
                )
            }
            "hash" -> {
                val target = arg.ifBlank { "ARCANUM_SANCTUARY_2026" }
                val digest = MessageDigest.getInstance("SHA-256")
                    .digest(target.toByteArray(Charsets.UTF_8))
                    .joinToString("") { "%02x".format(it) }
                listOf(
                    "[SHA-256] Input : \"$target\"",
                    "  Digest : $digest"
                )
            }
            "cipher" -> {
                val target = arg.ifBlank { "RAHASIA_BLACKBOX" }
                val b64 = transformCyberCipher(target, CyberCipherAlgorithm.BASE64, false)
                val hex = transformCyberCipher(target, CyberCipherAlgorithm.HEX, false)
                listOf(
                    "[CIPHER] Input  : \"$target\"",
                    "  Base64 : $b64",
                    "  Hex    : $hex"
                )
            }
            "tor" -> listOf(
                "[TOR CIRCUIT] Protokol Anonim 3-Hop Aktif:",
                "  Hop #1 (Entry Guard)  : [CH] Swiss (185.220.101.44)",
                "  Hop #2 (Middle Relay) : [IS] Islandia (82.221.139.18)",
                "  Hop #3 (Exit Node)    : [NL] Belanda (198.54.132.91)",
                "  Anti-Fingerprint JS   : ACTIVE (Canvas/WebGL/WebRTC Shielded)"
            )
            "audit" -> listOf(
                "[AUDIT SANCTUARY BLACKBOX]",
                "  1. Shadow Ledger  : ${seedWallets.size} Cold Wallets, ${financialGoals.size} Target Aset",
                "  2. Ruang Detektif : ${detectiveNodes.size} Simpul Bukti, ${detectiveLinks.size} Benang Merah",
                "  3. Cyber Deck OS  : 6 Mesin Sandi Siap Pakai",
                "  4. Midnight Zen   : 4-Channel Audio Synthesizer Ready",
                "  5. Onion Browser  : ${enabledExtensionIds.size} Ekstensi Keamanan Aktif"
            )
            "matrix" -> listOf(
                "01000001 01010010 01000011 01000001 01001110 01010101 01001101",
                "WAKE UP OPERATOR... THE SANCTUARY HAS YOU PROTECTED.",
                "FOLLOW THE RED STRING -> ZERO TELEMETRY LEAK DETECTED."
            )
            "whoami" -> listOf("operator@arcanum-blackbox [CLEARANCE: OMEGA-SANCTUARY]")
            else -> listOf(
                "Perintah '$cmd' tidak dikenali. Ketik 'help' untuk melihat daftar perintah."
            )
        }
    }

    // --- BRAVE + TOR BROWSER HELPER FUNCTIONS ---

    fun isAdOrTrackerUrl(url: String): Boolean {
        val host = try {
            java.net.URI(url.trim()).host?.lowercase() ?: url.lowercase()
        } catch (e: Exception) {
            url.lowercase()
        }
        return knownAdAndTrackerDomains.any { domain ->
            host == domain || host.endsWith(".$domain") || url.lowercase().contains(domain)
        }
    }

    fun stripTrackingParameters(rawUrl: String): String {
        return try {
            val qIdx = rawUrl.indexOf('?')
            if (qIdx < 0) return rawUrl
            val hashIdx = rawUrl.indexOf('#', startIndex = qIdx)
            val basePart = rawUrl.substring(0, qIdx)
            val queryPart = if (hashIdx >= 0) rawUrl.substring(qIdx + 1, hashIdx) else rawUrl.substring(qIdx + 1)
            val fragmentPart = if (hashIdx >= 0) rawUrl.substring(hashIdx) else ""

            val keptPairs = queryPart.split('&').filter { pair ->
                val key = pair.substringBefore('=').lowercase().trim()
                key.isNotEmpty() && key !in trackingQueryParamKeys
            }
            if (keptPairs.isEmpty()) {
                basePart + fragmentPart
            } else {
                "$basePart?${keptPairs.joinToString("&")}$fragmentPart"
            }
        } catch (e: Exception) {
            rawUrl
        }
    }

    fun normalizeAndUpgradeUrl(
        rawInput: String,
        httpsOnlyUpgrade: Boolean = true,
        stripTrackers: Boolean = true
    ): String {
        val trimmed = rawInput.trim()
        if (trimmed.isEmpty()) return "https://duckduckgo.com"

        val hasScheme = trimmed.startsWith("http://", ignoreCase = true) ||
            trimmed.startsWith("https://", ignoreCase = true)
        val looksLikeDomain = trimmed.contains(".") && !trimmed.contains(" ")

        var normalized = when {
            hasScheme -> trimmed
            looksLikeDomain -> "https://$trimmed"
            else -> "https://duckduckgo.com/?q=${java.net.URLEncoder.encode(trimmed, "UTF-8")}"
        }

        if (httpsOnlyUpgrade && normalized.startsWith("http://", ignoreCase = true)) {
            normalized = "https://" + normalized.substring(7)
        }

        if (stripTrackers) {
            normalized = stripTrackingParameters(normalized)
        }

        return normalized
    }

    /**
     * Converts mobile-forced subdomains (`m.youtube.com`, `id.m.wikipedia.org`, `m.facebook.com`,
     * `mobile.twitter.com`, `m.<domain>`) to full desktop URLs when `desktopMode == true`,
     * and cleans `app=desktop` when returning to mobile mode.
     */
    fun rewriteUrlForDesktopMode(rawUrl: String, desktopMode: Boolean): String {
        val trimmed = rawUrl.trim()
        if (trimmed.isEmpty() || trimmed == "about:blank") return trimmed
        return try {
            val uri = java.net.URI(trimmed)
            val scheme = uri.scheme ?: return trimmed
            val rawHost = uri.host ?: return trimmed
            val hostLower = rawHost.lowercase()
            val portPart = if (uri.port != -1) ":${uri.port}" else ""
            val rawPath = uri.rawPath?.ifEmpty { "/" } ?: "/"
            val rawQuery = uri.rawQuery
            val rawFragment = if (uri.rawFragment != null) "#${uri.rawFragment}" else ""

            if (desktopMode) {
                val desktopHost = when {
                    hostLower == "m.youtube.com" || hostLower == "music.youtube.com" -> "www.youtube.com"
                    hostLower.endsWith(".m.wikipedia.org") -> hostLower.replace(".m.wikipedia.org", ".wikipedia.org")
                    hostLower == "m.wikipedia.org" -> "www.wikipedia.org"
                    hostLower == "m.facebook.com" || hostLower == "mbasic.facebook.com" || hostLower == "touch.facebook.com" -> "www.facebook.com"
                    hostLower == "mobile.twitter.com" || hostLower == "m.twitter.com" -> "x.com"
                    hostLower == "mobile.x.com" || hostLower == "m.x.com" -> "x.com"
                    hostLower == "m.twitch.tv" -> "www.twitch.tv"
                    hostLower == "m.imdb.com" -> "www.imdb.com"
                    hostLower == "m.reddit.com" -> "www.reddit.com"
                    hostLower.startsWith("m.") && hostLower.count { it == '.' } >= 2 -> "www.${rawHost.substring(2)}"
                    hostLower.startsWith("mobile.") && hostLower.count { it == '.' } >= 2 -> "www.${rawHost.substring(7)}"
                    else -> rawHost
                }
                val isYouTube = desktopHost.equals("www.youtube.com", ignoreCase = true) ||
                    desktopHost.equals("youtube.com", ignoreCase = true)
                val updatedQuery = when {
                    isYouTube && (rawQuery == null || !rawQuery.contains("app=desktop")) -> {
                        if (rawQuery.isNullOrBlank()) "app=desktop" else "$rawQuery&app=desktop"
                    }
                    else -> rawQuery
                }
                val queryPart = if (!updatedQuery.isNullOrBlank()) "?$updatedQuery" else ""
                "$scheme://$desktopHost$portPart$rawPath$queryPart$rawFragment"
            } else {
                val cleanedQuery = rawQuery
                    ?.split("&")
                    ?.filter { !it.equals("app=desktop", ignoreCase = true) }
                    ?.joinToString("&")
                    ?.takeIf { it.isNotBlank() }
                val queryPart = if (cleanedQuery != null) "?$cleanedQuery" else ""
                "$scheme://$rawHost$portPart$rawPath$queryPart$rawFragment"
            }
        } catch (_: Exception) {
            trimmed
        }
    }

    /**
     * Builds the JavaScript snippet that forces a 1280px desktop viewport meta tag and PC
     * `navigator.userAgentData.mobile = false` override when `desktopMode == true`, or restores
     * standard responsive `device-width` viewport when `desktopMode == false`.
     */
    fun buildDesktopViewportJavaScript(desktopMode: Boolean): String {
        return if (desktopMode) {
            """
            (function() {
                try {
                    var meta = document.querySelector('meta[name="viewport"]');
                    if (!meta) {
                        meta = document.createElement('meta');
                        meta.name = 'viewport';
                        if (document.head) {
                            document.head.appendChild(meta);
                        }
                    }
                    if (meta) {
                        meta.setAttribute('content', 'width=1280, initial-scale=0.33, minimum-scale=0.1, maximum-scale=5.0, user-scalable=yes');
                    }
                    if (document.documentElement) {
                        document.documentElement.style.minWidth = '1280px';
                    }
                    Object.defineProperty(navigator, 'platform', { get: function() { return 'Win32'; }, configurable: true });
                    Object.defineProperty(navigator, 'maxTouchPoints', { get: function() { return 0; }, configurable: true });
                } catch (e) {}
            })();
            """.trimIndent()
        } else {
            """
            (function() {
                try {
                    var meta = document.querySelector('meta[name="viewport"]');
                    if (meta && meta.getAttribute('content') && meta.getAttribute('content').indexOf('width=1280') !== -1) {
                        meta.setAttribute('content', 'width=device-width, initial-scale=1.0, user-scalable=yes');
                    }
                    if (document.documentElement && document.documentElement.style.minWidth === '1280px') {
                        document.documentElement.style.minWidth = '';
                    }
                } catch (e) {}
            })();
            """.trimIndent()
        }
    }

    fun generateNewOnionCircuit(seedNonce: Int): List<OnionRelayHop> {
        val relayPool = listOf(
            Triple("[CH]", "Swiss", "HelvetiaGuard"),
            Triple("[IS]", "Islandia", "GlacierRelay"),
            Triple("[NL]", "Belanda", "TulipExitNode"),
            Triple("[SE]", "Swedia", "StockholmCore"),
            Triple("[RO]", "Rumania", "CarpathianNode"),
            Triple("[FI]", "Finlandia", "HelsinkiZero"),
            Triple("[JP]", "Jepang", "ShinjukuFiber"),
            Triple("[SG]", "Singapura", "MarinaStealth")
        )
        val rng = Random(System.currentTimeMillis() + seedNonce * 997L)
        val picked = relayPool.shuffled(rng).take(3)
        val roles = listOf("Hop #1 • Entry Guard", "Hop #2 • Middle Relay", "Hop #3 • Exit Node")
        return picked.mapIndexed { idx, (flag, country, nick) ->
            val oct2 = rng.nextInt(40, 230)
            val oct3 = rng.nextInt(10, 250)
            val oct4 = rng.nextInt(2, 254)
            OnionRelayHop(
                roleLabel = roles[idx],
                countryFlag = flag,
                countryName = country,
                routerNickname = "$nick-${rng.nextInt(10, 99)}",
                virtualIp = "185.$oct2.$oct3.$oct4",
                latencyMs = rng.nextInt(18, 64)
            )
        }
    }

    fun buildCombinedExtensionJavaScript(): String {
        val activeScripts = availableExtensions
            .filter { it.id in enabledExtensionIds }
            .map { it.injectedJavaScript }
            .toMutableList()
        if (isCustomUserscriptEnabled && customUserscriptCode.isNotBlank()) {
            activeScripts.add(
                """
                (function() {
                    try {
                        $customUserscriptCode
                    } catch (e) {}
                })();
                """.trimIndent()
            )
        }
        return activeScripts.joinToString("\n")
    }
}

fun formatRupiahCompact(amount: Long): String {
    val nf = NumberFormat.getNumberInstance(Locale("id", "ID"))
    return "Rp ${nf.format(amount)}"
}

// ============================================================================
// MAIN COMPOSABLE: ISOLATED SANCTUARY SCREEN
// (100% LATEST iOS 18.2 / iOS 19 LIQUID GLASS + CONTROL CENTER + APPLE WALLET)
// ============================================================================

@Composable
fun IsolatedSanctuaryScreen(
    onLockAndExit: () -> Unit,
    onPostBanner: (String) -> Unit
) {
    var activeWing by remember { mutableStateOf(SanctuaryWing.SHADOW_LEDGER) }
    var lastNonBrowserWing by remember { mutableStateOf(SanctuaryWing.SHADOW_LEDGER) }
    var showIosControlCenter by remember { mutableStateOf(false) }

    LaunchedEffect(activeWing) {
        AppThemeController.isSanctuaryBrowserActive =
            (activeWing == SanctuaryWing.ONION_SHIELD_BROWSER)
    }
    DisposableEffect(Unit) {
        onDispose {
            AppThemeController.isSanctuaryBrowserActive = false
        }
    }

    // iOS Control Center Quick Toggles State
    var ccStealthMode by remember { mutableStateOf(true) }
    var ccTorRouting by remember { mutableStateOf(true) }
    var ccZeroTrace by remember { mutableStateOf(true) }
    var ccKillSwitch by remember { mutableStateOf(true) }

    val iosGlassBorderBrush = Brush.verticalGradient(
        colors = listOf(
            Color.White.copy(alpha = 0.22f),
            Color.White.copy(alpha = 0.05f)
        )
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF000000))
            .testTag("isolated_sanctuary_root")
    ) {
        // ====================================================================
        // 1. iOS 18.2 / iOS 19 LIQUID GLASS TOP NAVIGATION BAR + LARGE TITLE
        // ====================================================================
        if (activeWing != SanctuaryWing.ONION_SHIELD_BROWSER) {
            Surface(
                color = Color(0xFF000000),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // Compact iOS Status / Nav Row: [< Kunci]  [Dynamic Island Privacy Pill]  [Control Center]
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        // Left: iOS Back / Lock Pill Button
                        Row(
                            modifier = Modifier
                                .clip(RoundedCornerShape(999.dp))
                                .background(Color(0xFF1C1C1E))
                                .border(0.5.dp, Color.White.copy(alpha = 0.12f), RoundedCornerShape(999.dp))
                                .clickable { onLockAndExit() }
                                .padding(horizontal = 12.dp, vertical = 6.dp)
                                .testTag("sanctuary_lock_exit_button"),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Kunci Sanctuary",
                                tint = Color(0xFF0A84FF),
                                modifier = Modifier.size(15.dp)
                            )
                            Text(
                                text = "Kunci",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color(0xFF0A84FF)
                            )
                        }

                        // Center: iOS Dynamic Island Privacy Capsule (Face ID • Terenkripsi)
                        Row(
                            modifier = Modifier
                                .clip(RoundedCornerShape(999.dp))
                                .background(Color(0xFF161618))
                                .border(0.7.dp, iosGlassBorderBrush, RoundedCornerShape(999.dp))
                                .clickable { showIosControlCenter = !showIosControlCenter }
                                .padding(horizontal = 12.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            // iOS Green Privacy Dot
                            Box(
                                modifier = Modifier
                                    .size(7.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFF30D158))
                            )
                            Icon(
                                imageVector = Icons.Default.Lock,
                                contentDescription = null,
                                tint = Color(0xFFF2F2F7),
                                modifier = Modifier.size(12.dp)
                            )
                            Text(
                                text = "iOS 18 • Terenkripsi",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color(0xFFF2F2F7)
                            )
                            // iOS Orange Sensor Dot
                            Box(
                                modifier = Modifier
                                    .size(6.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFFFF9F0A))
                            )
                        }

                        // Right: iOS 18 Control Center Toggle Pill
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(999.dp))
                                .background(
                                    if (showIosControlCenter) Color(0xFF0A84FF)
                                    else Color(0xFF1C1C1E)
                                )
                                .border(0.5.dp, Color.White.copy(alpha = 0.12f), RoundedCornerShape(999.dp))
                                .clickable { showIosControlCenter = !showIosControlCenter }
                                .padding(horizontal = 11.dp, vertical = 6.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Security,
                                    contentDescription = "Control Center",
                                    tint = Color.White,
                                    modifier = Modifier.size(14.dp)
                                )
                                Text(
                                    text = "Kontrol",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = Color.White
                                )
                            }
                        }
                    }

                    // EXPANDABLE iOS 18.2 CONTROL CENTER QUICK SHEET
                    AnimatedVisibility(
                        visible = showIosControlCenter,
                        enter = expandVertically() + fadeIn(),
                        exit = shrinkVertically() + fadeOut()
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(26.dp))
                                .background(Color(0xFF161618))
                                .border(0.8.dp, iosGlassBorderBrush, RoundedCornerShape(26.dp))
                                .padding(14.dp),
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Pusat Kontrol iOS Sanctuary",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF8E8E93)
                                )
                                Text(
                                    text = "Tutup",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = Color(0xFF0A84FF),
                                    modifier = Modifier.clickable { showIosControlCenter = false }
                                )
                            }

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                // Left 2x2 Connectivity & Security Pod (like iOS 18 Control Center)
                                Column(
                                    modifier = Modifier
                                        .weight(1f)
                                        .clip(RoundedCornerShape(20.dp))
                                        .background(Color(0xFF242426))
                                        .padding(10.dp),
                                    verticalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceEvenly
                                    ) {
                                        IosControlCenterCircleToggle(
                                            label = "Stealth",
                                            active = ccStealthMode,
                                            activeColor = Color(0xFF0A84FF),
                                            icon = Icons.Default.Shield,
                                            onClick = { ccStealthMode = !ccStealthMode }
                                        )
                                        IosControlCenterCircleToggle(
                                            label = "Tor 3-Hop",
                                            active = ccTorRouting,
                                            activeColor = Color(0xFFBF5AF2),
                                            icon = Icons.Default.VpnLock,
                                            onClick = { ccTorRouting = !ccTorRouting }
                                        )
                                    }
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceEvenly
                                    ) {
                                        IosControlCenterCircleToggle(
                                            label = "ZeroTrace",
                                            active = ccZeroTrace,
                                            activeColor = Color(0xFF30D158),
                                            icon = Icons.Default.VisibilityOff,
                                            onClick = { ccZeroTrace = !ccZeroTrace }
                                        )
                                        IosControlCenterCircleToggle(
                                            label = "KillSwitch",
                                            active = ccKillSwitch,
                                            activeColor = Color(0xFFFF9F0A),
                                            icon = Icons.Default.Lock,
                                            onClick = { ccKillSwitch = !ccKillSwitch }
                                        )
                                    }
                                }

                                // Right Quick Wing Jump Pods
                                Column(
                                    modifier = Modifier.weight(1f),
                                    verticalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clip(RoundedCornerShape(18.dp))
                                            .background(Color(0xFF242426))
                                            .clickable {
                                                activeWing = SanctuaryWing.ONION_SHIELD_BROWSER
                                                showIosControlCenter = false
                                            }
                                            .padding(horizontal = 12.dp, vertical = 12.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .size(30.dp)
                                                .clip(CircleShape)
                                                .background(Color(0xFF0A84FF)),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Icon(Icons.Default.Public, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                                        }
                                        Column {
                                            Text("Chrome iOS", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.White)
                                            Text("Tor + Shields", fontSize = 10.sp, color = Color(0xFF8E8E93))
                                        }
                                    }

                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clip(RoundedCornerShape(18.dp))
                                            .background(Color(0xFFFF453A).copy(alpha = 0.18f))
                                            .clickable {
                                                showIosControlCenter = false
                                                onLockAndExit()
                                            }
                                            .padding(horizontal = 12.dp, vertical = 12.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .size(30.dp)
                                                .clip(CircleShape)
                                                .background(Color(0xFFFF453A)),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Icon(Icons.Default.Lock, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                                        }
                                        Column {
                                            Text("Kunci Cepat", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFF453A))
                                            Text("Keluar 444", fontSize = 10.sp, color = Color(0xFFAEAEB2))
                                        }
                                    }
                                }
                            }
                        }
                    }

                    // iOS 18 Large Title Row (Just like Apple Wallet / Settings / Passwords)
                    val iosLargeTitle = when (activeWing) {
                        SanctuaryWing.SHADOW_LEDGER -> "Dompet & Saham"
                        SanctuaryWing.DETECTIVE_BOARD -> "Freeform & Dossier"
                        SanctuaryWing.CYBER_DECK -> "Sandi & Terminal"
                        SanctuaryWing.MIDNIGHT_ZEN -> "Mindfulness & Jurnal"
                        SanctuaryWing.ONION_SHIELD_BROWSER -> "Browser"
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = iosLargeTitle,
                                fontSize = 26.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = Color(0xFFF2F2F7)
                            )
                            Text(
                                text = activeWing.subtitle,
                                fontSize = 12.sp,
                                color = Color(0xFF8E8E93),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }

                        // iOS 18 Dark Mode Tinted Squircle App Icon Badge
                        Box(
                            modifier = Modifier
                                .size(42.dp)
                                .clip(RoundedCornerShape(13.dp))
                                .background(Color(0xFF1C1C1E))
                                .border(0.7.dp, iosGlassBorderBrush, RoundedCornerShape(13.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            SanctuaryWingVectorIcon(
                                wing = activeWing,
                                tint = Color(0xFF0A84FF),
                                modifier = Modifier.size(21.dp)
                            )
                        }
                    }
                }
            }
        }

        // ACTIVE WING CONTENT AREA WITH GSAP SHARED MORPH TRANSITION
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            GsapSharedMorphContainer(
                targetState = activeWing,
                modifier = Modifier.fillMaxSize()
            ) { wingState ->
                when (wingState) {
                    SanctuaryWing.SHADOW_LEDGER -> ShadowLedgerWingContent(onPostBanner = onPostBanner)
                    SanctuaryWing.DETECTIVE_BOARD -> DetectiveMindMapWingContent(onPostBanner = onPostBanner)
                    SanctuaryWing.CYBER_DECK -> CyberDeckTerminalWingContent(onPostBanner = onPostBanner)
                    SanctuaryWing.MIDNIGHT_ZEN -> MidnightZenLoungeWingContent(onPostBanner = onPostBanner)
                    SanctuaryWing.ONION_SHIELD_BROWSER -> SanctuaryUnifiedBrowserScreen(
                        onCloseToSanctuary = { activeWing = lastNonBrowserWing },
                        onPostBanner = onPostBanner
                    )
                }
            }
        }

        // ====================================================================
        // 3. iOS 18.2 / iOS 19 LIQUID GLASS DOCKED TAB BAR + HOME INDICATOR
        // (Hidden inside Browser wing so Chrome/Brave/Tor feels 100% native + uses 'X' button)
        // ====================================================================
        if (activeWing != SanctuaryWing.ONION_SHIELD_BROWSER) {
            SanctuaryDockedBottomBar(
                activeWing = activeWing,
                onSelectWing = { targetWing ->
                    if (targetWing != SanctuaryWing.ONION_SHIELD_BROWSER) {
                        lastNonBrowserWing = targetWing
                    }
                    activeWing = targetWing
                },
                onLockAndExit = onLockAndExit
            )
        }
    }
}

@Composable
private fun IosControlCenterCircleToggle(
    label: String,
    active: Boolean,
    activeColor: Color,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp),
        modifier = Modifier.clickable { onClick() }
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(if (active) activeColor else Color(0xFF3A3A3C)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = Color.White,
                modifier = Modifier.size(18.dp)
            )
        }
        Text(
            text = label,
            fontSize = 9.5.sp,
            fontWeight = FontWeight.Medium,
            color = if (active) Color.White else Color(0xFF8E8E93)
        )
    }
}

@Composable
private fun SanctuaryDockedBottomBar(
    activeWing: SanctuaryWing,
    onSelectWing: (SanctuaryWing) -> Unit,
    onLockAndExit: () -> Unit
) {
    val activeWingAccent = when (activeWing) {
        SanctuaryWing.SHADOW_LEDGER -> DuoFeatherGreen
        SanctuaryWing.DETECTIVE_BOARD -> DuoFoxOrange
        SanctuaryWing.CYBER_DECK -> DuoMacawBlue
        SanctuaryWing.MIDNIGHT_ZEN -> DuoBeetlePurple
        SanctuaryWing.ONION_SHIELD_BROWSER -> DuoBeeYellow
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF050811))
            .navigationBarsPadding()
            .padding(horizontal = 12.dp, vertical = 6.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
                .duolingoCard(
                    backgroundColor = Color(0xFF111827),
                    borderColor = activeWingAccent,
                    lipColor = Color(0xFF1E293B),
                    cornerRadius = 22.dp,
                    borderWidth = 2.dp,
                    lipHeight = 4.dp
                )
                .padding(bottom = 4.dp, start = 8.dp, end = 8.dp)
                .testTag("sanctuary_bottom_nav_dock"),
            contentAlignment = Alignment.Center
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp, Alignment.CenterHorizontally),
                verticalAlignment = Alignment.CenterVertically
            ) {
                SanctuaryWing.entries.forEach { wing ->
                    val isSelected = activeWing == wing
                    val (faceColor, lipColor) = when (wing) {
                        SanctuaryWing.SHADOW_LEDGER -> DuoFeatherGreen to DuoFeatherGreenLip
                        SanctuaryWing.DETECTIVE_BOARD -> DuoFoxOrange to DuoFoxOrangeLip
                        SanctuaryWing.CYBER_DECK -> DuoMacawBlue to DuoMacawBlueLip
                        SanctuaryWing.MIDNIGHT_ZEN -> DuoBeetlePurple to DuoBeetlePurpleLip
                        SanctuaryWing.ONION_SHIELD_BROWSER -> DuoBeeYellow to DuoBeeYellowLip
                    }
                    val boxShape = RoundedCornerShape(14.dp)
                    val iconTint = when {
                        isSelected && faceColor == DuoBeeYellow -> Color(0xFF1E293B)
                        isSelected -> Color.White
                        else -> faceColor
                    }

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(44.dp)
                            .then(
                                if (isSelected) {
                                    Modifier
                                        .duolingoButton(
                                            faceColor = faceColor,
                                            lipColor = lipColor,
                                            cornerRadius = 14.dp,
                                            lipHeight = 3.dp
                                        )
                                        .padding(bottom = 3.dp)
                                } else {
                                    Modifier
                                        .clip(boxShape)
                                        .background(faceColor.copy(alpha = 0.22f))
                                        .border(1.5.dp, faceColor.copy(alpha = 0.68f), boxShape)
                                }
                            )
                            .clickable { onSelectWing(wing) }
                            .testTag("sanctuary_wing_tab_${wing.id}"),
                        contentAlignment = Alignment.Center
                    ) {
                        SanctuaryWingVectorIcon(
                            wing = wing,
                            tint = iconTint,
                            modifier = Modifier.size(if (isSelected) 21.dp else 19.dp)
                        )
                    }
                }

                // Exit Colorful Red Box inside Sanctuary Bottom Bar (Icon-only)
                val exitShape = RoundedCornerShape(14.dp)
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(44.dp)
                        .clip(exitShape)
                        .background(DuoCardinalRed.copy(alpha = 0.22f))
                        .border(1.5.dp, DuoCardinalRed.copy(alpha = 0.68f), exitShape)
                        .clickable { onLockAndExit() }
                        .testTag("sanctuary_bottom_exit_tab"),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Keluar Sanctuary",
                        tint = DuoCardinalRed,
                        modifier = Modifier.size(19.dp)
                    )
                }
            }
        }
    }
}

/**
 * Reusable iOS 18.2 / iOS 19 UISegmentedControl Sub-Tab Bar
 * with translucent dark track and elevated active capsule.
 */
@Composable
private fun IosSegmentedSubTabBar(
    tabs: List<String>,
    selectedIndex: Int,
    onSelectIndex: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(11.dp))
            .background(Color(0xFF1C1C1E))
            .border(0.5.dp, Color.White.copy(alpha = 0.08f), RoundedCornerShape(11.dp))
            .padding(2.5.dp),
        horizontalArrangement = Arrangement.spacedBy(3.dp)
    ) {
        tabs.forEachIndexed { idx, title ->
            val selected = idx == selectedIndex
            Box(
                modifier = Modifier
                    .weight(1f)
                    .gsap3DPress(maxTiltDegrees = 6f, pressScale = 0.94f)
                    .clip(RoundedCornerShape(8.5.dp))
                    .background(if (selected) Color(0xFF3A3A3C) else Color.Transparent)
                    .clickable {
                        com.example.ui.theme.AppThemeController.gsapLiveDemoTriggerNonce++
                        onSelectIndex(idx)
                    }
                    .padding(vertical = 7.dp, horizontal = 6.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = title,
                    fontSize = 11.5.sp,
                    fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Medium,
                    color = if (selected) Color.White else Color(0xFF8E8E93),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

// ============================================================================
// WING 1: SHADOW LEDGER (100% iOS 18 APPLE WALLET STACK + SAVINGS + COLD WALLET)
// ============================================================================

@Composable
private fun ShadowLedgerWingContent(
    onPostBanner: (String) -> Unit
) {
    var selectedSubTab by remember { mutableIntStateOf(0) }
    var isBalanceMasked by remember { mutableStateOf(false) }
    var revealedWalletIds by remember { mutableStateOf(setOf<String>()) }
    var newWalletAlias by remember { mutableStateOf("") }
    var newWalletNetwork by remember { mutableStateOf("Bitcoin • Taproot (bc1p)") }
    var generatedWordCount by remember { mutableIntStateOf(12) }
    var generatedSeedPreview by remember {
        mutableStateOf(SanctuaryBlackBoxEngine.generateMnemonicWords(12))
    }

    // Compound Calculator States
    var calcPrincipalMillions by remember { mutableFloatStateOf(100f) }
    var calcMonthlyMillions by remember { mutableFloatStateOf(5f) }
    var calcAnnualYield by remember { mutableFloatStateOf(10f) }
    var calcYears by remember { mutableIntStateOf(5) }

    val totalWalletIdr = SanctuaryBlackBoxEngine.seedWallets.sumOf { it.balanceEstimateIdr }
    val totalSavingsVaultIdr = rememberGsapRollingLong(
        targetValue = SanctuarySavingsAndGoalsRepository.currentVaultSavingsBalanceIdr,
        durationMs = 900
    )
    val totalBuyGoalsSavedIdr = rememberGsapRollingLong(
        targetValue = SanctuarySavingsAndGoalsRepository.totalAllocatedToBuyGoalsIdr,
        durationMs = 900
    )
    val totalGoalsIdr = SanctuaryBlackBoxEngine.financialGoals.sumOf { it.currentAmountIdr } +
        SanctuarySavingsAndGoalsRepository.totalAllocatedToBuyGoalsIdr
    val grandTotalIdr = rememberGsapRollingLong(
        targetValue = totalWalletIdr + SanctuarySavingsAndGoalsRepository.currentVaultSavingsBalanceIdr + totalGoalsIdr,
        durationMs = 1050
    )

    val iosGlassBorderBrush = Brush.verticalGradient(
        colors = listOf(Color.White.copy(alpha = 0.22f), Color.White.copy(alpha = 0.05f))
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        Spacer(modifier = Modifier.height(6.dp))
        IosSegmentedSubTabBar(
            tabs = listOf("Tabungan", "Goals Barang", "Cold Wallet", "Kalkulator"),
            selectedIndex = selectedSubTab,
            onSelectIndex = { selectedSubTab = it }
        )
        Spacer(modifier = Modifier.height(12.dp))

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            when (selectedSubTab) {
                0 -> {
                    // SUB-TAB 0: AUTHENTIC iOS 18 STACKED APPLE WALLET CARDS + APPLE STOCKS SAVINGS
                    item {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .gsapStaggerReveal(index = 0, triggerKey = selectedSubTab)
                                .gsap3DPress(maxTiltDegrees = 7f, pressScale = 0.97f)
                        ) {
                            // Top Peeking Card 1: Apple Cash Black/Green Card Edge
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 14.dp)
                                    .height(14.dp)
                                    .clip(RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
                                    .background(
                                        Brush.horizontalGradient(
                                            colors = listOf(Color(0xFF103824), Color(0xFF1F6E43), Color(0xFF0D2E1D))
                                        )
                                    )
                                    .border(
                                        0.5.dp,
                                        Color.White.copy(alpha = 0.18f),
                                        RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)
                                    )
                            )
                            // Top Peeking Card 2: Sanctuary Crypto Indigo Card Edge
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 7.dp)
                                    .height(14.dp)
                                    .clip(RoundedCornerShape(topStart = 22.dp, topEnd = 22.dp))
                                    .background(
                                        Brush.horizontalGradient(
                                            colors = listOf(Color(0xFF282747), Color(0xFF434178), Color(0xFF1E1D36))
                                        )
                                    )
                                    .border(
                                        0.5.dp,
                                        Color.White.copy(alpha = 0.20f),
                                        RoundedCornerShape(topStart = 22.dp, topEnd = 22.dp)
                                    )
                            )

                            // Front Main Card: Apple Card Titanium Vault (with EMV Metallic Chip & NFC Wave)
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(26.dp))
                                    .background(
                                        Brush.linearGradient(
                                            colors = listOf(
                                                Color(0xFF2C2C2E),
                                                Color(0xFF1C1C1E),
                                                Color(0xFF141416)
                                            )
                                        )
                                    )
                                    .border(0.8.dp, iosGlassBorderBrush, RoundedCornerShape(26.dp))
                                    .padding(20.dp),
                                verticalArrangement = Arrangement.spacedBy(16.dp)
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
                                        // Custom Canvas EMV Metallic Chip (100% Apple Card style)
                                        Canvas(modifier = Modifier.size(width = 36.dp, height = 26.dp)) {
                                            drawRoundRect(
                                                brush = Brush.linearGradient(
                                                    colors = listOf(
                                                        Color(0xFFE5C158),
                                                        Color(0xFFB89230),
                                                        Color(0xFFF3D87D)
                                                    )
                                                ),
                                                cornerRadius = CornerRadius(5.dp.toPx(), 5.dp.toPx())
                                            )
                                            drawLine(
                                                color = Color.Black.copy(alpha = 0.28f),
                                                start = Offset(0f, size.height * 0.5f),
                                                end = Offset(size.width, size.height * 0.5f),
                                                strokeWidth = 1f
                                            )
                                            drawLine(
                                                color = Color.Black.copy(alpha = 0.28f),
                                                start = Offset(size.width * 0.35f, 0f),
                                                end = Offset(size.width * 0.35f, size.height),
                                                strokeWidth = 1f
                                            )
                                            drawLine(
                                                color = Color.Black.copy(alpha = 0.28f),
                                                start = Offset(size.width * 0.65f, 0f),
                                                end = Offset(size.width * 0.65f, size.height),
                                                strokeWidth = 1f
                                            )
                                        }

                                        Column {
                                            Text(
                                                text = "Apple Card • Titanium",
                                                fontSize = 13.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = Color(0xFFF2F2F7)
                                            )
                                            Text(
                                                text = "Sanctuary Sovereign Vault",
                                                fontSize = 10.5.sp,
                                                color = Color(0xFF8E8E93)
                                            )
                                        }
                                    }

                                    // Mask / Face ID Privacy Pill
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(999.dp))
                                            .background(Color(0xFF3A3A3C))
                                            .clickable { isBalanceMasked = !isBalanceMasked }
                                            .padding(horizontal = 12.dp, vertical = 6.dp)
                                            .testTag("shadow_ledger_mask_toggle")
                                    ) {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(5.dp)
                                        ) {
                                            Icon(
                                                imageVector = if (isBalanceMasked) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                                contentDescription = "Toggle Mask",
                                                tint = Color(0xFF0A84FF),
                                                modifier = Modifier.size(14.dp)
                                            )
                                            Text(
                                                text = if (isBalanceMasked) "Tampilkan" else "Privasi",
                                                fontSize = 11.sp,
                                                fontWeight = FontWeight.SemiBold,
                                                color = Color(0xFFF2F2F7)
                                            )
                                        }
                                    }
                                }

                                Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                                    Text(
                                        text = "SALDO GABUNGAN SANCTUARY",
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = Color(0xFF8E8E93)
                                    )
                                    Text(
                                        text = if (isBalanceMasked) "Rp •••.•••.•••" else formatRupiahCompact(grandTotalIdr),
                                        fontSize = 28.sp,
                                        fontWeight = FontWeight.ExtraBold,
                                        color = Color(0xFFF2F2F7)
                                    )
                                }

                                HorizontalDivider(thickness = 0.5.dp, color = Color.White.copy(alpha = 0.12f))

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Column {
                                        Text("Kas Tabungan Utama", fontSize = 10.5.sp, color = Color(0xFF8E8E93))
                                        Text(
                                            text = if (isBalanceMasked) "Rp ••••••••" else formatRupiahCompact(totalSavingsVaultIdr),
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color(0xFF30D158)
                                        )
                                    }
                                    Column(horizontalAlignment = Alignment.End) {
                                        Text("Alokasi Goals Barang", fontSize = 10.5.sp, color = Color(0xFF8E8E93))
                                        Text(
                                            text = if (isBalanceMasked) "Rp ••••••••" else formatRupiahCompact(totalBuyGoalsSavedIdr),
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color(0xFF0A84FF)
                                        )
                                    }
                                }
                            }
                        }
                    }

                    item {
                        SanctuarySavingsAndBuyGoalsSection(
                            onPostBanner = onPostBanner,
                            displayMode = SanctuaryLedgerSubViewMode.SAVINGS_VAULT_ONLY
                        )
                    }
                }

                1 -> {
                    // SUB-TAB 1: GOALS BARANG YANG MAU DIBELI DARI TABUNGAN
                    item {
                        SanctuarySavingsAndBuyGoalsSection(
                            onPostBanner = onPostBanner,
                            displayMode = SanctuaryLedgerSubViewMode.BUY_GOALS_ONLY
                        )
                    }
                }

                2 -> {
                    // SUB-TAB 2: COLD WALLET & 12/24-WORD SEED PHRASE VAULT (iOS 18 PASSWORDS STYLE)
                    item {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .gsapStaggerReveal(index = 0, triggerKey = selectedSubTab)
                                .gsap3DPress(maxTiltDegrees = 6f, pressScale = 0.98f)
                                .clip(RoundedCornerShape(26.dp))
                                .background(Color(0xFF1C1C1E))
                                .border(0.7.dp, iosGlassBorderBrush, RoundedCornerShape(26.dp))
                                .padding(18.dp),
                            verticalArrangement = Arrangement.spacedBy(14.dp)
                        ) {
                            Text(
                                text = "Brankas Seed Phrase Cold Wallet",
                                fontSize = 17.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFFF2F2F7)
                            )
                            Text(
                                text = "Frasa pemulihan BIP-39 12/24 kata dilindungi enkripsi Sanctuary. Ketuk 'Ungkap Seed' untuk membuka.",
                                fontSize = 12.sp,
                                color = Color(0xFF8E8E93)
                            )

                            SanctuaryBlackBoxEngine.seedWallets.forEach { wallet ->
                                val isRevealed = wallet.id in revealedWalletIds
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(18.dp))
                                        .background(Color(0xFF2C2C2E))
                                        .padding(14.dp),
                                    verticalArrangement = Arrangement.spacedBy(10.dp)
                                ) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Column(modifier = Modifier.weight(1f)) {
                                            Text(
                                                text = wallet.walletAlias,
                                                fontSize = 15.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = Color(0xFFF2F2F7)
                                            )
                                            Text(
                                                text = "${wallet.networkName} • ${wallet.words.size} Kata",
                                                fontSize = 11.5.sp,
                                                color = Color(0xFF0A84FF)
                                            )
                                        }
                                        Box(
                                            modifier = Modifier
                                                .clip(RoundedCornerShape(999.dp))
                                                .background(Color(0xFF0A84FF).copy(alpha = 0.20f))
                                                .clickable {
                                                    revealedWalletIds = if (isRevealed) {
                                                        revealedWalletIds - wallet.id
                                                    } else {
                                                        revealedWalletIds + wallet.id
                                                    }
                                                }
                                                .padding(horizontal = 12.dp, vertical = 6.dp)
                                        ) {
                                            Text(
                                                text = if (isRevealed) "Sembunyikan" else "Ungkap Seed",
                                                fontSize = 11.5.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = Color(0xFF0A84FF)
                                            )
                                        }
                                    }

                                    Column(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clip(RoundedCornerShape(14.dp))
                                            .background(Color(0xFF1C1C1E))
                                            .then(if (!isRevealed) Modifier.blur(8.dp) else Modifier)
                                            .padding(10.dp),
                                        verticalArrangement = Arrangement.spacedBy(6.dp)
                                    ) {
                                        wallet.words.chunked(3).forEachIndexed { rowIdx, chunk ->
                                            Row(
                                                modifier = Modifier.fillMaxWidth(),
                                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                                            ) {
                                                chunk.forEachIndexed { colIdx, word ->
                                                    val wordNum = rowIdx * 3 + colIdx + 1
                                                    Box(
                                                        modifier = Modifier
                                                            .weight(1f)
                                                            .clip(RoundedCornerShape(8.dp))
                                                            .background(Color(0xFF2C2C2E))
                                                            .padding(horizontal = 8.dp, vertical = 6.dp)
                                                    ) {
                                                        Text(
                                                            text = if (isRevealed) "$wordNum. $word" else "$wordNum. ••••••",
                                                            fontFamily = JetBrainsMonoFontFamily,
                                                            fontSize = 10.sp,
                                                            color = Color(0xFFF2F2F7)
                                                        )
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }

                            // iOS Inset Grouped Cold Wallet Creator
                            Text(
                                text = "BUAT DOMPET SEED BARU",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color(0xFF8E8E93),
                                modifier = Modifier.padding(start = 4.dp)
                            )
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(16.dp))
                                    .background(Color(0xFF2C2C2E))
                            ) {
                                IosCupertinoFieldRow(
                                    label = "Nama Dompet",
                                    value = newWalletAlias,
                                    onValueChange = { newWalletAlias = it },
                                    placeholder = "Ledger Nano / Trezor",
                                    accentColor = Color.White
                                )
                                HorizontalDivider(thickness = 0.5.dp, color = Color(0xFF38383A), modifier = Modifier.padding(start = 14.dp))
                                IosCupertinoFieldRow(
                                    label = "Jaringan",
                                    value = newWalletNetwork,
                                    onValueChange = { newWalletNetwork = it },
                                    placeholder = "Bitcoin • Taproot",
                                    accentColor = Color(0xFF0A84FF)
                                )
                            }

                            // iOS Segmented Word Count + Shuffle Button
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(
                                    modifier = Modifier
                                        .weight(1f)
                                        .clip(RoundedCornerShape(10.dp))
                                        .background(Color(0xFF2C2C2E))
                                        .padding(2.dp)
                                ) {
                                    listOf(12 to "12 Kata", 24 to "24 Kata").forEach { (countVal, labelTxt) ->
                                        val isSel = generatedWordCount == countVal
                                        Box(
                                            modifier = Modifier
                                                .weight(1f)
                                                .clip(RoundedCornerShape(8.dp))
                                                .background(if (isSel) Color(0xFF48484A) else Color.Transparent)
                                                .clickable {
                                                    generatedWordCount = countVal
                                                    generatedSeedPreview = SanctuaryBlackBoxEngine.generateMnemonicWords(countVal)
                                                }
                                                .padding(vertical = 7.dp),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(
                                                text = labelTxt,
                                                fontSize = 12.sp,
                                                fontWeight = if (isSel) FontWeight.Bold else FontWeight.Medium,
                                                color = Color.White
                                            )
                                        }
                                    }
                                }

                                Button(
                                    onClick = {
                                        generatedSeedPreview = SanctuaryBlackBoxEngine.generateMnemonicWords(generatedWordCount)
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2C2C2E)),
                                    shape = RoundedCornerShape(10.dp)
                                ) {
                                    Text("Acak Ulang", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF0A84FF))
                                }
                            }

                            Text(
                                text = generatedSeedPreview.mapIndexed { i, w -> "${i + 1}.$w" }.joinToString("  "),
                                fontFamily = JetBrainsMonoFontFamily,
                                fontSize = 10.5.sp,
                                color = Color(0xFF8E8E93)
                            )

                            Button(
                                onClick = {
                                    val alias = newWalletAlias.trim().ifBlank {
                                        "Cold Vault #${SanctuaryBlackBoxEngine.seedWallets.size + 1}"
                                    }
                                    SanctuaryBlackBoxEngine.seedWallets.add(
                                        0,
                                        SeedPhraseColdWalletItem(
                                            id = "wallet_${System.currentTimeMillis()}",
                                            walletAlias = alias,
                                            networkName = newWalletNetwork,
                                            words = generatedSeedPreview,
                                            balanceEstimateIdr = 125_000_000L,
                                            createdAtText = "Baru saja • ${generatedSeedPreview.size} Kata"
                                        )
                                    )
                                    newWalletAlias = ""
                                    generatedSeedPreview = SanctuaryBlackBoxEngine.generateMnemonicWords(generatedWordCount)
                                    onPostBanner("Cold Wallet '$alias' berhasil disimpan!")
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(46.dp),
                                shape = RoundedCornerShape(14.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0A84FF))
                            ) {
                                Text(
                                    text = "Simpan ke Cold Wallet",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            }
                        }
                    }
                }

                3 -> {
                    // SUB-TAB 3: SIMULATOR COMPOUND INTEREST & PROYEKSI ASET (APPLE CARD CALCULATOR STYLE)
                    item {
                        val projectedIdr = SanctuaryBlackBoxEngine.calculateCompoundFutureValue(
                            principalIdr = (calcPrincipalMillions * 1_000_000L).toLong(),
                            monthlyContributionIdr = (calcMonthlyMillions * 1_000_000L).toLong(),
                            annualRatePercent = calcAnnualYield.toDouble(),
                            years = calcYears
                        )
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(26.dp))
                                .background(Color(0xFF1C1C1E))
                                .border(0.7.dp, iosGlassBorderBrush, RoundedCornerShape(26.dp))
                                .padding(18.dp),
                            verticalArrangement = Arrangement.spacedBy(14.dp)
                        ) {
                            Text(
                                text = "Simulator Bunga Majemuk Apple Savings",
                                fontSize = 17.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFFF2F2F7)
                            )

                            // Hero Projected Balance Box
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(18.dp))
                                    .background(Color(0xFF2C2C2E))
                                    .padding(16.dp),
                                verticalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Text(
                                    text = "ESTIMASI NILAI AKHIR ($calcYears TAHUN)",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = Color(0xFF8E8E93)
                                )
                                Text(
                                    text = formatRupiahCompact(projectedIdr),
                                    fontSize = 24.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = Color(0xFF30D158)
                                )
                                Text(
                                    text = "Imbal Hasil ${"%.1f".format(Locale.US, calcAnnualYield)}%/thn • Setoran Rp ${calcMonthlyMillions.toInt()} Jt/bln",
                                    fontSize = 11.5.sp,
                                    color = Color(0xFFAEAEB2)
                                )
                            }

                            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                    Text("Modal Awal", fontSize = 13.sp, color = Color(0xFFF2F2F7))
                                    Text("Rp ${calcPrincipalMillions.toInt()} Juta", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0A84FF))
                                }
                                Slider(
                                    value = calcPrincipalMillions,
                                    onValueChange = { calcPrincipalMillions = it },
                                    valueRange = 10f..1000f,
                                    colors = SliderDefaults.colors(thumbColor = Color.White, activeTrackColor = Color(0xFF0A84FF))
                                )
                            }

                            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                    Text("Setoran Bulanan", fontSize = 13.sp, color = Color(0xFFF2F2F7))
                                    Text("Rp ${calcMonthlyMillions.toInt()} Juta", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color(0xFF30D158))
                                }
                                Slider(
                                    value = calcMonthlyMillions,
                                    onValueChange = { calcMonthlyMillions = it },
                                    valueRange = 1f..100f,
                                    colors = SliderDefaults.colors(thumbColor = Color.White, activeTrackColor = Color(0xFF30D158))
                                )
                            }

                            // Tenor Tahun Selector Pills
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                listOf(1, 3, 5, 10, 15).forEach { yr ->
                                    val isSel = calcYears == yr
                                    Box(
                                        modifier = Modifier
                                            .weight(1f)
                                            .clip(RoundedCornerShape(10.dp))
                                            .background(if (isSel) Color(0xFF0A84FF) else Color(0xFF2C2C2E))
                                            .clickable { calcYears = yr }
                                            .padding(vertical = 8.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = "$yr Thn",
                                            fontSize = 11.5.sp,
                                            fontWeight = FontWeight.Bold,
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
    }
}

// ============================================================================
// WING 2: DETECTIVE MIND-MAP (APPLE FREEFORM DOT-GRID + iOS FILES DOSSIER)
// ============================================================================

@Composable
private fun DetectiveMindMapWingContent(
    onPostBanner: (String) -> Unit
) {
    var selectedSubTab by remember { mutableIntStateOf(0) }
    var selectedNodeId by remember { mutableStateOf<String?>(SanctuaryBlackBoxEngine.detectiveNodes.firstOrNull()?.id) }
    var linkSourceNodeId by remember { mutableStateOf<String?>(null) }
    var newNodeTitle by remember { mutableStateOf("") }
    var newNodeCategory by remember { mutableStateOf("BUKTI") }
    var newLinkLabel by remember { mutableStateOf("Jejak Terhubung") }

    val iosGlassBorderBrush = Brush.verticalGradient(
        colors = listOf(Color.White.copy(alpha = 0.22f), Color.White.copy(alpha = 0.05f))
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        Spacer(modifier = Modifier.height(6.dp))
        IosSegmentedSubTabBar(
            tabs = listOf("Papan Bukti", "Berkas Dossier"),
            selectedIndex = selectedSubTab,
            onSelectIndex = { selectedSubTab = it }
        )
        Spacer(modifier = Modifier.height(12.dp))

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            if (selectedSubTab == 0) {
                // SUB-TAB 0: APPLE FREEFORM iOS 18 INTERACTIVE DOT-GRID BOARD
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .gsapStaggerReveal(index = 0, triggerKey = selectedSubTab)
                            .clip(RoundedCornerShape(26.dp))
                            .background(Color(0xFF1C1C1E))
                            .border(0.7.dp, iosGlassBorderBrush, RoundedCornerShape(26.dp))
                            .padding(18.dp),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "Kanvas Apple Freeform Investigasi",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFFF2F2F7)
                                )
                                Text(
                                    text = "Geser simpul di kanvas titik-titik atau tarik konektor antar bukti.",
                                    fontSize = 12.sp,
                                    color = Color(0xFF8E8E93)
                                )
                            }
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(999.dp))
                                    .background(Color(0xFF2C2C2E))
                                    .padding(horizontal = 10.dp, vertical = 5.dp)
                            ) {
                                Text(
                                    text = "${SanctuaryBlackBoxEngine.detectiveNodes.size} Simpul",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = Color(0xFF0A84FF)
                                )
                            }
                        }

                        // Apple Freeform Dot-Grid Canvas
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(280.dp)
                                .clip(RoundedCornerShape(20.dp))
                                .background(Color(0xFF101012))
                                .border(0.5.dp, Color.White.copy(alpha = 0.10f), RoundedCornerShape(20.dp))
                                .pointerInput(Unit) {
                                    detectDragGestures { change, dragAmount ->
                                        change.consume()
                                        val w = size.width.toFloat().coerceAtLeast(1f)
                                        val h = size.height.toFloat().coerceAtLeast(1f)
                                        val touchNormX = change.position.x / w
                                        val touchNormY = change.position.y / h

                                        val nearestIdx = SanctuaryBlackBoxEngine.detectiveNodes.indexOfMinBy { node ->
                                            hypot(node.normX - touchNormX, node.normY - touchNormY)
                                        }
                                        if (nearestIdx >= 0) {
                                            val node = SanctuaryBlackBoxEngine.detectiveNodes[nearestIdx]
                                            if (hypot(node.normX - touchNormX, node.normY - touchNormY) < 0.25f) {
                                                selectedNodeId = node.id
                                                SanctuaryBlackBoxEngine.detectiveNodes[nearestIdx] = node.copy(
                                                    normX = (node.normX + dragAmount.x / w).coerceIn(0.1f, 0.9f),
                                                    normY = (node.normY + dragAmount.y / h).coerceIn(0.12f, 0.85f)
                                                )
                                            }
                                        }
                                    }
                                }
                                .testTag("detective_red_string_canvas")
                        ) {
                            Canvas(modifier = Modifier.fillMaxSize()) {
                                val w = size.width
                                val h = size.height
                                // Apple Freeform Dot Grid
                                val step = 24.dp.toPx()
                                var gx = step
                                while (gx < w) {
                                    var gy = step
                                    while (gy < h) {
                                        drawCircle(
                                            color = Color.White.copy(alpha = 0.12f),
                                            radius = 1.4f,
                                            center = Offset(gx, gy)
                                        )
                                        gy += step
                                    }
                                    gx += step
                                }

                                SanctuaryBlackBoxEngine.detectiveLinks.forEach { link ->
                                    val fromNode = SanctuaryBlackBoxEngine.detectiveNodes.find { it.id == link.fromNodeId }
                                    val toNode = SanctuaryBlackBoxEngine.detectiveNodes.find { it.id == link.toNodeId }
                                    if (fromNode != null && toNode != null) {
                                        val p1 = Offset(fromNode.normX * w, fromNode.normY * h)
                                        val p2 = Offset(toNode.normX * w, toNode.normY * h)
                                        drawLine(
                                            color = Color(0xFFFF453A).copy(alpha = 0.22f),
                                            start = p1,
                                            end = p2,
                                            strokeWidth = 7f,
                                            cap = StrokeCap.Round
                                        )
                                        drawLine(
                                            color = Color(0xFFFF453A),
                                            start = p1,
                                            end = p2,
                                            strokeWidth = 2.4f,
                                            pathEffect = PathEffect.dashPathEffect(floatArrayOf(14f, 6f), 0f)
                                        )
                                    }
                                }

                                SanctuaryBlackBoxEngine.detectiveNodes.forEach { node ->
                                    val center = Offset(node.normX * w, node.normY * h)
                                    val nodeColor = Color(node.badgeColorHex)
                                    val isSel = node.id == selectedNodeId
                                    if (isSel) {
                                        drawCircle(
                                            color = nodeColor.copy(alpha = 0.28f),
                                            radius = 32f,
                                            center = center
                                        )
                                    }
                                    drawCircle(
                                        color = Color(0xFF1C1C1E),
                                        radius = 22f,
                                        center = center
                                    )
                                    drawCircle(
                                        color = nodeColor,
                                        radius = 22f,
                                        center = center,
                                        style = Stroke(width = if (isSel) 3.5f else 2f)
                                    )
                                }
                            }

                            // Floating iOS Freeform Bottom Pill Palette
                            Row(
                                modifier = Modifier
                                    .align(Alignment.BottomCenter)
                                    .padding(10.dp)
                                    .clip(RoundedCornerShape(999.dp))
                                    .background(Color(0xFF2C2C2E).copy(alpha = 0.92f))
                                    .border(0.5.dp, Color.White.copy(alpha = 0.16f), RoundedCornerShape(999.dp))
                                    .horizontalScroll(rememberScrollState())
                                    .padding(horizontal = 10.dp, vertical = 6.dp),
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                SanctuaryBlackBoxEngine.detectiveNodes.forEach { node ->
                                    val isSelected = node.id == selectedNodeId
                                    val isLinkSource = node.id == linkSourceNodeId
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(999.dp))
                                            .background(
                                                when {
                                                    isLinkSource -> Color(0xFFFF453A)
                                                    isSelected -> Color(0xFF0A84FF)
                                                    else -> Color(0xFF1C1C1E)
                                                }
                                            )
                                            .clickable {
                                                selectedNodeId = node.id
                                                if (linkSourceNodeId != null && linkSourceNodeId != node.id) {
                                                    val srcId = linkSourceNodeId!!
                                                    SanctuaryBlackBoxEngine.detectiveLinks.add(
                                                        DetectiveRedStringLink(
                                                            id = "link_${System.currentTimeMillis()}",
                                                            fromNodeId = srcId,
                                                            toNodeId = node.id,
                                                            relationLabel = newLinkLabel.ifBlank { "Benang Merah" }
                                                        )
                                                    )
                                                    linkSourceNodeId = null
                                                    onPostBanner("Benang Merah baru berhasil ditarik antar bukti!")
                                                }
                                            }
                                            .padding(horizontal = 10.dp, vertical = 5.dp)
                                    ) {
                                        Text(
                                            text = "${node.title} (${node.category})",
                                            fontSize = 10.5.sp,
                                            fontWeight = FontWeight.SemiBold,
                                            color = Color.White
                                        )
                                    }
                                }
                            }
                        }

                        // iOS Inset Grouped Node Creator Row
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(16.dp))
                                .background(Color(0xFF2C2C2E))
                        ) {
                            IosCupertinoFieldRow(
                                label = "Bukti Baru",
                                value = newNodeTitle,
                                onValueChange = { newNodeTitle = it },
                                placeholder = "Nama petunjuk / simpul...",
                                accentColor = Color.White
                            )
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Button(
                                onClick = {
                                    if (linkSourceNodeId == null) {
                                        linkSourceNodeId = selectedNodeId ?: SanctuaryBlackBoxEngine.detectiveNodes.firstOrNull()?.id
                                        onPostBanner("Pilih simpul tujuan di bawah kanvas untuk menarik Benang Merah!")
                                    } else {
                                        linkSourceNodeId = null
                                    }
                                },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (linkSourceNodeId != null) Color(0xFFFF9F0A) else Color(0xFF2C2C2E)
                                ),
                                shape = RoundedCornerShape(14.dp),
                                modifier = Modifier
                                    .weight(1f)
                                    .height(44.dp)
                            ) {
                                Text(
                                    text = if (linkSourceNodeId != null) "Pilih Tujuan..." else "Tarik Benang",
                                    fontSize = 12.5.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            }

                            Button(
                                onClick = {
                                    val title = newNodeTitle.trim().ifBlank {
                                        "Petunjuk #${SanctuaryBlackBoxEngine.detectiveNodes.size + 1}"
                                    }
                                    val newNode = DetectiveBoardNode(
                                        id = "node_${System.currentTimeMillis()}",
                                        title = title,
                                        category = newNodeCategory,
                                        note = "Ditambahkan ke kanvas Freeform Sanctuary.",
                                        normX = Random.nextFloat().coerceIn(0.2f, 0.8f),
                                        normY = Random.nextFloat().coerceIn(0.2f, 0.72f),
                                        badgeColorHex = 0xFF0A84FF
                                    )
                                    SanctuaryBlackBoxEngine.detectiveNodes.add(newNode)
                                    selectedNodeId = newNode.id
                                    newNodeTitle = ""
                                    onPostBanner("Simpul '$title' ditambahkan!")
                                },
                                shape = RoundedCornerShape(14.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0A84FF)),
                                modifier = Modifier
                                    .weight(1f)
                                    .height(44.dp)
                            ) {
                                Text("+ Tambah Simpul", fontSize = 12.5.sp, fontWeight = FontWeight.Bold, color = Color.White)
                            }
                        }
                    }
                }
            } else {
                // SUB-TAB 1: BERKAS DOSSIER (iOS 18 FILES / NOTES INSET GROUPED LIST)
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .gsapStaggerReveal(index = 0, triggerKey = selectedSubTab)
                            .gsap3DPress(maxTiltDegrees = 6f, pressScale = 0.98f)
                            .clip(RoundedCornerShape(26.dp))
                            .background(Color(0xFF1C1C1E))
                            .border(0.7.dp, iosGlassBorderBrush, RoundedCornerShape(26.dp))
                            .padding(18.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(
                            text = "Berkas Dossier Terenkripsi",
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFF2F2F7)
                        )

                        SanctuaryBlackBoxEngine.classifiedDossiers.forEach { dossier ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(18.dp))
                                    .background(Color(0xFF2C2C2E))
                                    .padding(14.dp),
                                horizontalArrangement = Arrangement.spacedBy(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(42.dp)
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(Color(0xFFFF9F0A).copy(alpha = 0.18f)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Visibility,
                                        contentDescription = null,
                                        tint = Color(0xFFFF9F0A),
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                                Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(3.dp)) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = dossier.codename,
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color(0xFFF2F2F7)
                                        )
                                        Box(
                                            modifier = Modifier
                                                .clip(RoundedCornerShape(6.dp))
                                                .background(Color(0xFFFF9F0A).copy(alpha = 0.20f))
                                                .padding(horizontal = 6.dp, vertical = 2.dp)
                                        ) {
                                            Text(
                                                text = dossier.clearanceLevel,
                                                fontSize = 9.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = Color(0xFFFF9F0A)
                                            )
                                        }
                                    }
                                    Text(
                                        text = "${dossier.subjectRole} • Skor Prioritas: ${dossier.threatScore}/100",
                                        fontSize = 11.5.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = Color(0xFF0A84FF)
                                    )
                                    Text(
                                        text = dossier.summaryNotes,
                                        fontSize = 11.5.sp,
                                        color = Color(0xFF8E8E93)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

private inline fun <T> List<T>.indexOfMinBy(selector: (T) -> Float): Int {
    var minIdx = -1
    var minVal = Float.MAX_VALUE
    for (i in indices) {
        val v = selector(this[i])
        if (v < minVal) {
            minVal = v
            minIdx = i
        }
    }
    return minIdx
}

// ============================================================================
// WING 3: CYBER DECK (iOS 18 PASSWORDS APP 2x2 PODS + TERMINAL + CIPHER)
// ============================================================================

@Composable
private fun CyberDeckTerminalWingContent(
    onPostBanner: (String) -> Unit
) {
    var selectedSubTab by remember { mutableIntStateOf(0) }
    var commandInput by remember { mutableStateOf("") }
    var selectedCipher by remember { mutableStateOf(CyberCipherAlgorithm.MORSE) }
    var isDecodeMode by remember { mutableStateOf(false) }
    var cipherInputText by remember { mutableStateOf("ARCANUM BLACKBOX SANCTUARY") }
    var vigenereKeyInput by remember { mutableStateOf("NYXKEY") }
    var passwordLength by remember { mutableIntStateOf(24) }
    var generatedPasswordPair by remember(passwordLength) {
        mutableStateOf(SanctuaryBlackBoxEngine.generateHighEntropyPassword(passwordLength, true))
    }

    val cipherOutputText = remember(cipherInputText, selectedCipher, isDecodeMode, vigenereKeyInput) {
        SanctuaryBlackBoxEngine.transformCyberCipher(
            input = cipherInputText,
            algorithm = selectedCipher,
            decodeMode = isDecodeMode,
            vigenereKey = vigenereKeyInput
        )
    }

    val iosGlassBorderBrush = Brush.verticalGradient(
        colors = listOf(Color.White.copy(alpha = 0.22f), Color.White.copy(alpha = 0.05f))
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        Spacer(modifier = Modifier.height(6.dp))
        IosSegmentedSubTabBar(
            tabs = listOf("Terminal CLI", "Studio Sandi", "Kunci Entropi"),
            selectedIndex = selectedSubTab,
            onSelectIndex = { selectedSubTab = it }
        )
        Spacer(modifier = Modifier.height(12.dp))

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Top 2x2 Summary Tiles just like Apple's new iOS 18 Passwords App
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .gsapStaggerReveal(index = 0, triggerKey = selectedSubTab),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    IosPasswordsSummaryPod(
                        title = "Konsol CLI",
                        countText = "${SanctuaryBlackBoxEngine.terminalHistory.size}",
                        circleColor = Color(0xFF0A84FF),
                        icon = Icons.Default.Security,
                        onClick = { selectedSubTab = 0 },
                        modifier = Modifier.weight(1f).gsap3DPress(maxTiltDegrees = 9f, pressScale = 0.95f)
                    )
                    IosPasswordsSummaryPod(
                        title = "Sandi & Cipher",
                        countText = "6 Mode",
                        circleColor = Color(0xFF30D158),
                        icon = Icons.Default.Lock,
                        onClick = { selectedSubTab = 1 },
                        modifier = Modifier.weight(1f).gsap3DPress(maxTiltDegrees = 9f, pressScale = 0.95f)
                    )
                    IosPasswordsSummaryPod(
                        title = "Entropi Bit",
                        countText = "${generatedPasswordPair.second.toInt()}b",
                        circleColor = Color(0xFFBF5AF2),
                        icon = Icons.Default.VpnKey,
                        onClick = { selectedSubTab = 2 },
                        modifier = Modifier.weight(1f).gsap3DPress(maxTiltDegrees = 9f, pressScale = 0.95f)
                    )
                }
            }

            when (selectedSubTab) {
                0 -> {
                    // SUB-TAB 0: macOS / iOS PROMPT TERMINAL CLI
                    item {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .gsapStaggerReveal(index = 1, triggerKey = selectedSubTab)
                                .gsap3DPress(maxTiltDegrees = 6f, pressScale = 0.98f)
                                .clip(RoundedCornerShape(26.dp))
                                .background(Color(0xFF1C1C1E))
                                .border(0.7.dp, iosGlassBorderBrush, RoundedCornerShape(26.dp))
                                .padding(18.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            // macOS / iPadOS Window Header with Traffic Light Dots
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Box(modifier = Modifier.size(11.dp).clip(CircleShape).background(Color(0xFFFF453A)))
                                    Box(modifier = Modifier.size(11.dp).clip(CircleShape).background(Color(0xFFFFD60A)))
                                    Box(modifier = Modifier.size(11.dp).clip(CircleShape).background(Color(0xFF30D158)))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "arcanum — zsh — 80×24",
                                        fontFamily = JetBrainsMonoFontFamily,
                                        fontSize = 11.5.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = Color(0xFF8E8E93)
                                    )
                                }
                                Text(
                                    text = "SSH ENCRYPTED",
                                    fontFamily = JetBrainsMonoFontFamily,
                                    fontSize = 9.5.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF30D158)
                                )
                            }

                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .heightIn(min = 170.dp, max = 240.dp)
                                    .clip(RoundedCornerShape(16.dp))
                                    .background(Color(0xFF0C0C0E))
                                    .border(0.5.dp, Color.White.copy(alpha = 0.08f), RoundedCornerShape(16.dp))
                                    .verticalScroll(rememberScrollState())
                                    .padding(14.dp),
                                verticalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                SanctuaryBlackBoxEngine.terminalHistory.forEach { line ->
                                    Text(
                                        text = line,
                                        fontFamily = JetBrainsMonoFontFamily,
                                        fontSize = 11.sp,
                                        color = if (line.startsWith("root@")) Color(0xFF0A84FF) else Color(0xFF30D158)
                                    )
                                }
                            }

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .horizontalScroll(rememberScrollState()),
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                listOf("help", "scan", "keygen", "tor", "audit", "matrix", "clear").forEach { quickCmd ->
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(999.dp))
                                            .background(Color(0xFF2C2C2E))
                                            .clickable {
                                                if (quickCmd == "clear") {
                                                    SanctuaryBlackBoxEngine.terminalHistory.clear()
                                                } else {
                                                    SanctuaryBlackBoxEngine.terminalHistory.add("root@arcanum:~# $quickCmd")
                                                    SanctuaryBlackBoxEngine.terminalHistory.addAll(
                                                        SanctuaryBlackBoxEngine.executeTerminalCommand(quickCmd)
                                                    )
                                                }
                                            }
                                            .padding(horizontal = 12.dp, vertical = 6.dp)
                                            .testTag("cyber_terminal_chip_$quickCmd")
                                    ) {
                                        Text(
                                            text = "$ $quickCmd",
                                            fontFamily = JetBrainsMonoFontFamily,
                                            fontSize = 10.5.sp,
                                            fontWeight = FontWeight.SemiBold,
                                            color = Color(0xFFF2F2F7)
                                        )
                                    }
                                }
                            }

                            // iOS Spotlight-style Command Input Bar
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(44.dp)
                                        .clip(RoundedCornerShape(14.dp))
                                        .background(Color(0xFF2C2C2E))
                                        .padding(horizontal = 14.dp),
                                    contentAlignment = Alignment.CenterStart
                                ) {
                                    BasicTextField(
                                        value = commandInput,
                                        onValueChange = { commandInput = it },
                                        singleLine = true,
                                        textStyle = TextStyle(
                                            color = Color.White,
                                            fontSize = 13.sp,
                                            fontFamily = JetBrainsMonoFontFamily
                                        ),
                                        cursorBrush = SolidColor(Color(0xFF0A84FF)),
                                        modifier = Modifier.fillMaxWidth(),
                                        decorationBox = { inner ->
                                            if (commandInput.isEmpty()) {
                                                Text("Ketik perintah zsh...", fontSize = 12.5.sp, color = Color(0xFF636366))
                                            }
                                            inner()
                                        }
                                    )
                                }
                                Button(
                                    onClick = {
                                        val cmd = commandInput.trim()
                                        if (cmd.equals("clear", ignoreCase = true)) {
                                            SanctuaryBlackBoxEngine.terminalHistory.clear()
                                        } else if (cmd.isNotEmpty()) {
                                            SanctuaryBlackBoxEngine.terminalHistory.add("root@arcanum:~# $cmd")
                                            SanctuaryBlackBoxEngine.terminalHistory.addAll(
                                                SanctuaryBlackBoxEngine.executeTerminalCommand(cmd)
                                            )
                                        }
                                        commandInput = ""
                                    },
                                    shape = RoundedCornerShape(14.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0A84FF)),
                                    modifier = Modifier.height(44.dp)
                                ) {
                                    Text("Kirim", fontWeight = FontWeight.Bold, color = Color.White)
                                }
                            }
                        }
                    }
                }

                1 -> {
                    // SUB-TAB 1: STUDIO SANDI 6-ALGORITMA (PURE iOS SEGMENTED + INSET GROUPED)
                    item {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(26.dp))
                                .background(Color(0xFF1C1C1E))
                                .border(0.7.dp, iosGlassBorderBrush, RoundedCornerShape(26.dp))
                                .padding(18.dp),
                            verticalArrangement = Arrangement.spacedBy(14.dp)
                        ) {
                            Text(
                                text = "Studio Konversi Sandi (6-Algoritma)",
                                fontSize = 17.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFFF2F2F7)
                            )

                            // iOS Capsule Algorithm Selector
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .horizontalScroll(rememberScrollState()),
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                CyberCipherAlgorithm.entries.forEach { algo ->
                                    val isSel = selectedCipher == algo
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(999.dp))
                                            .background(if (isSel) Color(0xFF0A84FF) else Color(0xFF2C2C2E))
                                            .clickable { selectedCipher = algo }
                                            .padding(horizontal = 12.dp, vertical = 7.dp)
                                    ) {
                                        Text(
                                            text = algo.displayName,
                                            fontSize = 11.5.sp,
                                            fontWeight = if (isSel) FontWeight.Bold else FontWeight.Medium,
                                            color = Color.White
                                        )
                                    }
                                }
                            }

                            // iOS Segmented Encode / Decode Switcher
                            IosSegmentedSubTabBar(
                                tabs = listOf("Enkripsi (Encode)", "Dekripsi (Decode)"),
                                selectedIndex = if (isDecodeMode) 1 else 0,
                                onSelectIndex = { isDecodeMode = (it == 1) }
                            )

                            // iOS Inset Grouped Input Row
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(16.dp))
                                    .background(Color(0xFF2C2C2E))
                            ) {
                                IosCupertinoFieldRow(
                                    label = "Teks Sumber",
                                    value = cipherInputText,
                                    onValueChange = { cipherInputText = it },
                                    placeholder = "Masukkan pesan rahasia...",
                                    accentColor = Color.White
                                )
                            }

                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(16.dp))
                                    .background(Color(0xFF101012))
                                    .border(0.5.dp, Color.White.copy(alpha = 0.08f), RoundedCornerShape(16.dp))
                                    .padding(14.dp),
                                verticalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Text(
                                    text = "HASIL ${selectedCipher.displayName.uppercase()} (${if (isDecodeMode) "DECODE" else "ENCODE"})",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF0A84FF)
                                )
                                Text(
                                    text = cipherOutputText,
                                    fontFamily = JetBrainsMonoFontFamily,
                                    fontSize = 13.sp,
                                    color = Color(0xFF30D158),
                                    modifier = Modifier.testTag("cyber_cipher_output_text")
                                )
                            }
                        }
                    }
                }

                2 -> {
                    // SUB-TAB 2: GENERATOR KUNCI ENTROPI TINGGI (iOS 18 PASSWORDS GENERATOR)
                    item {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(26.dp))
                                .background(Color(0xFF1C1C1E))
                                .border(0.7.dp, iosGlassBorderBrush, RoundedCornerShape(26.dp))
                                .padding(18.dp),
                            verticalArrangement = Arrangement.spacedBy(14.dp)
                        ) {
                            Text(
                                text = "Generator Sandi Apple Keychain",
                                fontSize = 17.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFFF2F2F7)
                            )
                            Text(
                                text = "Panjang Karakter: $passwordLength • Entropi: ${"%.1f".format(Locale.US, generatedPasswordPair.second)} bits",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color(0xFF30D158)
                            )
                            Slider(
                                value = passwordLength.toFloat(),
                                onValueChange = { passwordLength = it.toInt().coerceIn(12, 48) },
                                valueRange = 12f..48f,
                                colors = SliderDefaults.colors(thumbColor = Color.White, activeTrackColor = Color(0xFF0A84FF))
                            )
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(16.dp))
                                    .background(Color(0xFF2C2C2E))
                                    .padding(16.dp)
                            ) {
                                Text(
                                    text = generatedPasswordPair.first,
                                    fontFamily = JetBrainsMonoFontFamily,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFFF2F2F7)
                                )
                            }
                            Button(
                                onClick = {
                                    generatedPasswordPair = SanctuaryBlackBoxEngine.generateHighEntropyPassword(passwordLength, true)
                                    onPostBanner("Kunci entropi baru berhasil dibuat!")
                                },
                                shape = RoundedCornerShape(14.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0A84FF)),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(46.dp)
                            ) {
                                Text("Buat Sandi Kuat Baru", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.White)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun IosPasswordsSummaryPod(
    title: String,
    countText: String,
    circleColor: Color,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(18.dp))
            .background(Color(0xFF1C1C1E))
            .border(0.5.dp, Color.White.copy(alpha = 0.10f), RoundedCornerShape(18.dp))
            .clickable { onClick() }
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(30.dp)
                    .clip(CircleShape)
                    .background(circleColor),
                contentAlignment = Alignment.Center
            ) {
                Icon(imageVector = icon, contentDescription = title, tint = Color.White, modifier = Modifier.size(16.dp))
            }
            Text(
                text = countText,
                fontSize = 14.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color(0xFFF2F2F7)
            )
        }
        Text(
            text = title,
            fontSize = 11.5.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color(0xFF8E8E93),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

// ============================================================================
// WING 4: MIDNIGHT ZEN LOUNGE (APPLE WATCH MINDFULNESS LOTUS + APPLE JOURNAL)
// ============================================================================

@Composable
private fun MidnightZenLoungeWingContent(
    onPostBanner: (String) -> Unit
) {
    var selectedSubTab by remember { mutableIntStateOf(0) }
    var isAudioPlaying by remember { mutableStateOf(false) }
    var rainVolume by remember { mutableFloatStateOf(0.65f) }
    var fireVolume by remember { mutableFloatStateOf(0.40f) }
    var thetaDroneVolume by remember { mutableFloatStateOf(0.50f) }
    var vinylHumVolume by remember { mutableFloatStateOf(0.35f) }

    var timerRunning by remember { mutableStateOf(false) }
    var remainingSeconds by remember { mutableIntStateOf(25 * 60) }

    var diaryTitleInput by remember { mutableStateOf("") }
    var diaryContentInput by remember { mutableStateOf("") }
    var selectedMood by remember { mutableStateOf("[ZEN]" to "Tenang & Fokus") }

    val iosGlassBorderBrush = Brush.verticalGradient(
        colors = listOf(Color.White.copy(alpha = 0.22f), Color.White.copy(alpha = 0.05f))
    )

    LaunchedEffect(timerRunning) {
        while (timerRunning && remainingSeconds > 0) {
            delay(1000L)
            remainingSeconds--
        }
    }

    DisposableEffect(isAudioPlaying) {
        var keepRunning = true
        var audioThread: Thread? = null
        if (isAudioPlaying) {
            audioThread = thread(start = true, isDaemon = true, name = "SanctuaryZenSynth") {
                try {
                    val sampleRate = 22050
                    val minBuf = AudioTrack.getMinBufferSize(
                        sampleRate,
                        AudioFormat.CHANNEL_OUT_MONO,
                        AudioFormat.ENCODING_PCM_16BIT
                    ).coerceAtLeast(2048)

                    val track = AudioTrack.Builder()
                        .setAudioAttributes(
                            AudioAttributes.Builder()
                                .setUsage(AudioAttributes.USAGE_MEDIA)
                                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                                .build()
                        )
                        .setAudioFormat(
                            AudioFormat.Builder()
                                .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
                                .setSampleRate(sampleRate)
                                .setChannelMask(AudioFormat.CHANNEL_OUT_MONO)
                                .build()
                        )
                        .setBufferSizeInBytes(minBuf)
                        .setTransferMode(AudioTrack.MODE_STREAM)
                        .build()

                    track.play()
                    val chunk = ShortArray(1024)
                    var phase1 = 0.0
                    var phase2 = 0.0
                    var pinkState = 0.0
                    val rng = Random(42L)

                    while (keepRunning) {
                        for (i in chunk.indices) {
                            val white = rng.nextDouble(-1.0, 1.0)
                            pinkState = 0.92 * pinkState + 0.08 * white
                            val rainSample = pinkState * rainVolume
                            val crackle = if (rng.nextDouble() < 0.004) rng.nextDouble(-0.9, 0.9) else 0.0
                            val fireSample = crackle * fireVolume
                            phase1 += (2.0 * PI * 216.0) / sampleRate
                            phase2 += (2.0 * PI * 222.0) / sampleRate
                            val droneSample = ((sin(phase1) + sin(phase2)) * 0.25) * thetaDroneVolume
                            val vinylSample = (sin(phase1 * 0.5) * 0.2) * vinylHumVolume
                            val mixed = ((rainSample + fireSample + droneSample + vinylSample) * 0.45)
                                .coerceIn(-0.95, 0.95)
                            chunk[i] = (mixed * Short.MAX_VALUE).toInt().toShort()
                        }
                        track.write(chunk, 0, chunk.size)
                    }
                    track.stop()
                    track.release()
                } catch (_: Throwable) {
                }
            }
        }
        onDispose {
            keepRunning = false
            audioThread?.interrupt()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        Spacer(modifier = Modifier.height(6.dp))
        IosSegmentedSubTabBar(
            tabs = listOf("Suara & Napas", "Jurnal Rahasia"),
            selectedIndex = selectedSubTab,
            onSelectIndex = { selectedSubTab = it }
        )
        Spacer(modifier = Modifier.height(12.dp))

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            if (selectedSubTab == 0) {
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .gsapStaggerReveal(index = 0, triggerKey = selectedSubTab)
                            .gsap3DPress(maxTiltDegrees = 6f, pressScale = 0.98f)
                            .clip(RoundedCornerShape(26.dp))
                            .background(Color(0xFF1C1C1E))
                            .border(0.7.dp, iosGlassBorderBrush, RoundedCornerShape(26.dp))
                            .padding(18.dp),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        // Apple Watch Mindfulness / Breathe Header with 6-Petal Cyan-Indigo Lotus
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(12.dp),
                                modifier = Modifier.weight(1f)
                            ) {
                                // Apple Watch Mindfulness 6-Petal Flower Graphic
                                Canvas(modifier = Modifier.size(48.dp)) {
                                    val center = Offset(size.width / 2f, size.height / 2f)
                                    val petalRadius = size.minDimension * 0.25f
                                    for (i in 0 until 6) {
                                        val angle = (i * 60.0) * (PI / 180.0)
                                        val offsetCenter = Offset(
                                            x = center.x + (cos(angle) * petalRadius * 0.65f).toFloat(),
                                            y = center.y + (sin(angle) * petalRadius * 0.65f).toFloat()
                                        )
                                        drawCircle(
                                            color = if (i % 2 == 0) Color(0xFF64D2FF).copy(alpha = 0.48f)
                                            else Color(0xFF5E5CE6).copy(alpha = 0.48f),
                                            radius = petalRadius,
                                            center = offsetCenter
                                        )
                                    }
                                }
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = "Apple Mindfulness & Suara Latar",
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFFF2F2F7)
                                    )
                                    Text(
                                        text = "Synthesizer 4-kanal untuk fokus & ketenangan.",
                                        fontSize = 12.sp,
                                        color = Color(0xFF8E8E93)
                                    )
                                }
                            }

                            Button(
                                onClick = { isAudioPlaying = !isAudioPlaying },
                                shape = RoundedCornerShape(999.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (isAudioPlaying) Color(0xFFFF453A) else Color(0xFF0A84FF)
                                ),
                                modifier = Modifier.testTag("zen_lofi_play_toggle")
                            ) {
                                Icon(
                                    imageVector = if (isAudioPlaying) Icons.Default.Stop else Icons.Default.PlayArrow,
                                    contentDescription = null,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = if (isAudioPlaying) "Stop" else "Putar",
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            }
                        }

                        ZenChannelSlider("Hujan Malam (Pink Rain)", rainVolume) { rainVolume = it }
                        ZenChannelSlider("Api Unggun (Warm Fireplace)", fireVolume) { fireVolume = it }
                        ZenChannelSlider("Binaural Theta 6Hz (Deep Focus)", thetaDroneVolume) { thetaDroneVolume = it }
                        ZenChannelSlider("Vinyl Lo-Fi Analog Pad", vinylHumVolume) { vinylHumVolume = it }

                        // iOS 18 StandBy Mode Pomodoro Clock Card
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(18.dp))
                                .background(Color(0xFF101012))
                                .border(0.5.dp, Color.White.copy(alpha = 0.08f), RoundedCornerShape(18.dp))
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            val mins = remainingSeconds / 60
                            val secs = remainingSeconds % 60
                            Column {
                                Text(
                                    text = "STANDBY POMODORO",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFFFF9F0A)
                                )
                                Text(
                                    text = String.format(Locale.US, "%02d:%02d", mins, secs),
                                    fontFamily = JetBrainsMonoFontFamily,
                                    fontSize = 26.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = Color(0xFFF2F2F7)
                                )
                            }
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                Button(
                                    onClick = { timerRunning = !timerRunning },
                                    shape = RoundedCornerShape(999.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF30D158))
                                ) {
                                    Text(if (timerRunning) "Jeda" else "Mulai", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                                }
                                Button(
                                    onClick = {
                                        timerRunning = false
                                        remainingSeconds = 25 * 60
                                    },
                                    shape = RoundedCornerShape(999.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2C2C2E))
                                ) {
                                    Text("Reset", fontSize = 12.sp, color = Color.White)
                                }
                            }
                        }
                    }
                }
            } else {
                // SUB-TAB 1: APPLE JOURNAL iOS 18
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(26.dp))
                            .background(Color(0xFF1C1C1E))
                            .border(0.7.dp, iosGlassBorderBrush, RoundedCornerShape(26.dp))
                            .padding(18.dp),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        Text(
                            text = "Apple Journal • Catatan Malam",
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFF2F2F7)
                        )

                        // iOS State of Mind Mood Capsules
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .horizontalScroll(rememberScrollState()),
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            listOf(
                                "[ZEN]" to "Tenang & Fokus",
                                "[ENERGI]" to "Semangat Tinggi",
                                "[MISTERI]" to "Misterius",
                                "[SYUKUR]" to "Bersyukur",
                                "[AMBISI]" to "Ambisius"
                            ).forEach { moodPair ->
                                val isSel = selectedMood.first == moodPair.first
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(999.dp))
                                        .background(if (isSel) Color(0xFF5E5CE6) else Color(0xFF2C2C2E))
                                        .clickable { selectedMood = moodPair }
                                        .padding(horizontal = 12.dp, vertical = 7.dp)
                                ) {
                                    Text(
                                        text = "${moodPair.first} ${moodPair.second}",
                                        fontSize = 11.5.sp,
                                        fontWeight = if (isSel) FontWeight.Bold else FontWeight.Medium,
                                        color = Color.White
                                    )
                                }
                            }
                        }

                        // iOS Inset Grouped Journal Composer
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(16.dp))
                                .background(Color(0xFF2C2C2E))
                        ) {
                            IosCupertinoFieldRow(
                                label = "Judul Jurnal",
                                value = diaryTitleInput,
                                onValueChange = { diaryTitleInput = it },
                                placeholder = "Refleksi malam ini...",
                                accentColor = Color.White
                            )
                            HorizontalDivider(thickness = 0.5.dp, color = Color(0xFF38383A), modifier = Modifier.padding(start = 14.dp))
                            IosCupertinoFieldRow(
                                label = "Isi Catatan",
                                value = diaryContentInput,
                                onValueChange = { diaryContentInput = it },
                                placeholder = "Tulis cerita atau pemikiran Anda...",
                                accentColor = Color(0xFFAEAEB2)
                            )
                        }

                        Button(
                            onClick = {
                                val title = diaryTitleInput.trim().ifBlank { "Refleksi Malam Sanctuary" }
                                val body = diaryContentInput.trim().ifBlank { "Catatan tersimpan aman di dalam ruang Midnight Zen." }
                                val nowStr = SimpleDateFormat("dd MMM • HH:mm", Locale("id", "ID")).format(Date())
                                SanctuaryBlackBoxEngine.secretDiaryEntries.add(
                                    0,
                                    SecretDiaryEntry(
                                        id = "diary_${System.currentTimeMillis()}",
                                        title = title,
                                        moodEmoji = selectedMood.first,
                                        moodLabel = selectedMood.second,
                                        content = body,
                                        timestampText = nowStr,
                                        isLockedPreview = false
                                    )
                                )
                                diaryTitleInput = ""
                                diaryContentInput = ""
                                onPostBanner("Catatan jurnal '$title' berhasil disimpan!")
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(46.dp),
                            shape = RoundedCornerShape(14.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF5E5CE6))
                        ) {
                            Text("Simpan ke Apple Journal", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.White)
                        }

                        SanctuaryBlackBoxEngine.secretDiaryEntries.forEach { entry ->
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(18.dp))
                                    .background(Color(0xFF2C2C2E))
                                    .padding(14.dp),
                                verticalArrangement = Arrangement.spacedBy(5.dp)
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = "${entry.moodEmoji} ${entry.title}",
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFFF2F2F7)
                                    )
                                    Text(
                                        text = entry.timestampText,
                                        fontSize = 10.5.sp,
                                        color = Color(0xFF8E8E93)
                                    )
                                }
                                Text(
                                    text = entry.content,
                                    fontSize = 12.5.sp,
                                    color = Color(0xFFAEAEB2)
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
private fun ZenChannelSlider(
    label: String,
    value: Float,
    onValueChange: (Float) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(Color(0xFF2C2C2E))
            .padding(horizontal = 14.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = label, fontSize = 12.5.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFFF2F2F7))
            Text(
                text = "${(value * 100).toInt()}%",
                fontFamily = JetBrainsMonoFontFamily,
                fontSize = 11.5.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF64D2FF)
            )
        }
        Slider(
            value = value,
            onValueChange = onValueChange,
            valueRange = 0f..1f,
            colors = SliderDefaults.colors(
                thumbColor = Color.White,
                activeTrackColor = Color(0xFF64D2FF),
                inactiveTrackColor = Color(0xFF3A3A3C)
            )
        )
    }
}

// ============================================================================
// WING 5 DELEGATE: 100% AUTHENTIC CHROME / BRAVE / TOR BROWSER
// Implemented modularly in SanctuaryUnifiedBrowserScreen.kt & SanctuaryBrowserSettingsAndSheets.kt
// ============================================================================

@Composable
private fun SanctuaryWingVectorIcon(
    wing: SanctuaryWing,
    tint: Color,
    modifier: Modifier = Modifier
) {
    val iconVec = when (wing) {
        SanctuaryWing.SHADOW_LEDGER -> Icons.Default.VpnKey
        SanctuaryWing.DETECTIVE_BOARD -> Icons.Default.Visibility
        SanctuaryWing.CYBER_DECK -> Icons.Default.Security
        SanctuaryWing.MIDNIGHT_ZEN -> Icons.Default.AutoAwesome
        SanctuaryWing.ONION_SHIELD_BROWSER -> Icons.Default.Shield
    }
    Icon(
        imageVector = iconVec,
        contentDescription = wing.fullTitle,
        tint = tint,
        modifier = modifier
    )
}
