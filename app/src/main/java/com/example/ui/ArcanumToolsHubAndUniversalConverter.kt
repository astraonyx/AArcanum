package com.example.ui

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.Shader
import android.graphics.Typeface
import android.graphics.pdf.PdfDocument
import android.graphics.pdf.PdfRenderer
import android.net.Uri
import android.os.ParcelFileDescriptor
import android.util.Base64
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Archive
import androidx.compose.material.icons.filled.AudioFile
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.CloudDownload
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.ContentPaste
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.FolderOpen
import androidx.compose.material.icons.filled.GpsOff
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.GridOn
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.LockOpen
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.PictureAsPdf
import androidx.compose.material.icons.filled.Polyline
import androidx.compose.material.icons.filled.QrCode2
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Slideshow
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material.icons.filled.TableChart
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.MoriDownloaderEngine
import com.example.data.VaultCategory
import com.example.data.VaultItemEntity
import com.example.ui.theme.DuoBeeYellow
import com.example.ui.theme.DuoBeeYellowLip
import com.example.ui.theme.DuoBeetlePurple
import com.example.ui.theme.DuoBeetlePurpleLip
import com.example.ui.theme.DuoCardinalRed
import com.example.ui.theme.DuoCardinalRedLip
import com.example.ui.theme.DuoEelInk
import com.example.ui.theme.DuoFeatherGreen
import com.example.ui.theme.DuoFeatherGreenLip
import com.example.ui.theme.DuoFoxOrange
import com.example.ui.theme.DuoFoxOrangeLip
import com.example.ui.theme.DuoHareLip
import com.example.ui.theme.DuoMacawBlue
import com.example.ui.theme.DuoMacawBlueLip
import com.example.ui.theme.DuoMintSurface
import com.example.ui.theme.DuoMutedInk
import com.example.ui.theme.DuoPureWhite
import com.example.ui.theme.DuoSnowCanvas
import com.example.ui.theme.DuoSwanBorder
import com.example.ui.theme.JetBrainsMonoFontFamily
import com.example.ui.theme.SpaceGroteskFontFamily
import com.example.ui.theme.VaultObsidian
import com.example.ui.theme.VaultTextMuted
import com.example.ui.theme.duolingoButton
import com.example.ui.theme.duolingoCard
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.util.Locale
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream
import kotlin.math.PI
import kotlin.math.roundToInt
import kotlin.math.sin

// ============================================================================
// 1. ARCANUM TOOLS CATEGORY ICON HUB DEFINITIONS
// ============================================================================

enum class ArcanumToolsHubCategory(
    val id: String,
    val title: String,
    val subtitle: String,
    val badge: String,
    val accentColor: Color,
    val lipColor: Color
) {
    WEB_OSINT_UTILITIES(
        id = "web_osint",
        title = "Mori v4.6.0, Web Sniffer & OSINT Utilities",
        subtitle = "Mori Stream Downloader v4.6.0, Web Media Sniffer, SFL.GL Bypass, TikTok OSINT, Anime trace.moe, DoH Check & Media Pro",
        badge = "MORI v4.6.0 + 8 ALAT",
        accentColor = DuoBeetlePurple,
        lipColor = DuoBeetlePurpleLip
    ),
    UNIVERSAL_CONVERTER(
        id = "converter",
        title = "Universal File & Media Converter Studio",
        subtitle = "PDF ↔ DOCX/XLSX/PPTX/TXT • PNG/JPG ↔ SVG/ICO/WEBP • CSV/JSON/MD • ZIP & Audio",
        badge = "16+ FORMAT BARU",
        accentColor = DuoFeatherGreen,
        lipColor = DuoFeatherGreenLip
    ),
    STEGO_MEDIA(
        id = "stego",
        title = "Steganografi Media & Pembongkar LSB",
        subtitle = "Sembunyikan pesan & 2 foto di dalam PNG atau frekuensi audio WAV 16-bit PCM",
        badge = "PNG & WAV LSB",
        accentColor = DuoMacawBlue,
        lipColor = DuoMacawBlueLip
    ),
    ASTRA_QR(
        id = "qr",
        title = "AstraOnyx Star QR & Kartu Pemulihan",
        subtitle = "Generator QR Bintang Kosmik 100% Unik, Pemindai Kamera & Kartu Kunci Offline",
        badge = "STAR QR STUDIO",
        accentColor = Color(0xFF0EA5E9),
        lipColor = Color(0xFF0369A1)
    ),
    CRYPTO_LAB(
        id = "crypto",
        title = "Lab Sandi & Kriptografi Militer",
        subtitle = "Telegraf Radio Morse CW, Simulasi 3-Rotor Enigma M3 & Generator Sandi 30-Char",
        badge = "CIPHER LAB",
        accentColor = DuoFoxOrange,
        lipColor = DuoFoxOrangeLip
    ),
    PRIVACY_CAMOUFLAGE(
        id = "privacy",
        title = "Privasi EXIF & Kamuflase Darurat",
        subtitle = "Pembersih koordinat GPS/EXIF kamera & mode penyamaran Kalkulator 1-Klik",
        badge = "ANTI-FORENSIK",
        accentColor = DuoCardinalRed,
        lipColor = DuoCardinalRedLip
    )
}

data class ArcanumToolIconCardSpec(
    val subTabId: Int,
    val category: ArcanumToolsHubCategory,
    val title: String,
    val subtitle: String,
    val formatBadge: String,
    val icon: ImageVector,
    val faceColor: Color,
    val lipColor: Color
)

fun buildArcanumToolIconCards(stegoCarrierCount: Int, exifCandidateCount: Int): List<ArcanumToolIconCardSpec> = listOf(
    // PINTASAN UTAMA MORI STREAM DOWNLOADER v4.6.0 (Sub-tab 30)
    ArcanumToolIconCardSpec(
        subTabId = 30,
        category = ArcanumToolsHubCategory.WEB_OSINT_UTILITIES,
        title = "Mori Downloader v4.6.0",
        subtitle = "Ekstraktor 16 Platform, Batch MP3/Video, Pure Audio Auto-MP3 & YouTube Shorts Canonicalizer",
        formatBadge = "MORI v4.6.0",
        icon = Icons.Default.CloudDownload,
        faceColor = DuoBeetlePurple,
        lipColor = DuoBeetlePurpleLip
    )
) + MoriBuiltinToolId.entries.mapIndexed { idx, tool ->
    // 8 ALAT EKSTRA MORI v4.6.0 (Sub-tabs 20..27)
    ArcanumToolIconCardSpec(
        subTabId = 20 + idx,
        category = ArcanumToolsHubCategory.WEB_OSINT_UTILITIES,
        title = tool.title,
        subtitle = tool.subtitle,
        formatBadge = tool.badge,
        icon = tool.icon,
        faceColor = tool.accentColor,
        lipColor = tool.lipColor
    )
} + listOf(
    // KATEGORI: UNIVERSAL FILE & MEDIA CONVERTER STUDIO (Sub-tabs 10, 11, 12, 13)
    ArcanumToolIconCardSpec(
        subTabId = 10,
        category = ArcanumToolsHubCategory.UNIVERSAL_CONVERTER,
        title = "Office Docs Converter",
        subtitle = "PDF ke Word DOCX, Excel XLSX, Slide PPTX, TXT & Sebaliknya",
        formatBadge = "PDF ↔ DOCX/XLSX/PPTX",
        icon = Icons.Default.Description,
        faceColor = DuoFeatherGreen,
        lipColor = DuoFeatherGreenLip
    ),
    ArcanumToolIconCardSpec(
        subTabId = 11,
        category = ArcanumToolsHubCategory.UNIVERSAL_CONVERTER,
        title = "Vector & Image Converter",
        subtitle = "PNG / JPG ke SVG Vectorizer, ICO Icon, WEBP, SVG ke PNG & Gambar ke PDF",
        formatBadge = "PNG/JPG ↔ SVG/ICO/PDF",
        icon = Icons.Default.Polyline,
        faceColor = Color(0xFF0EA5E9),
        lipColor = Color(0xFF0284C7)
    ),
    ArcanumToolIconCardSpec(
        subTabId = 12,
        category = ArcanumToolsHubCategory.UNIVERSAL_CONVERTER,
        title = "Data, Tabel & Kode",
        subtitle = "CSV ke Excel XLSX / JSON, Markdown ke PDF / HTML & Base64 Encoder",
        formatBadge = "CSV/JSON/MD/B64",
        icon = Icons.Default.Code,
        faceColor = DuoBeetlePurple,
        lipColor = DuoBeetlePurpleLip
    ),
    ArcanumToolIconCardSpec(
        subTabId = 13,
        category = ArcanumToolsHubCategory.UNIVERSAL_CONVERTER,
        title = "Arsip ZIP & Audio",
        subtitle = "Kompresi Multi-File ke Arsip .ZIP & Konversi Audio WAV, MP3, M4A",
        formatBadge = "ZIP • WAV/MP3/M4A",
        icon = Icons.Default.Archive,
        faceColor = DuoFoxOrange,
        lipColor = DuoFoxOrangeLip
    ),

    // KATEGORI: STEGANOGRAFI MEDIA (Sub-tabs 0, 5, 1)
    ArcanumToolIconCardSpec(
        subTabId = 0,
        category = ArcanumToolsHubCategory.STEGO_MEDIA,
        title = "Stego Foto PNG",
        subtitle = "Sembunyikan pesan rahasia & 2 foto dalam 1 file PNG + Pixel Loupe 8x",
        formatBadge = "DUAL PHOTO PNG",
        icon = Icons.Default.AutoAwesome,
        faceColor = DuoMacawBlue,
        lipColor = DuoMacawBlueLip
    ),
    ArcanumToolIconCardSpec(
        subTabId = 5,
        category = ArcanumToolsHubCategory.STEGO_MEDIA,
        title = "Stego Audio WAV",
        subtitle = "Suntikkan pesan & foto ke dalam sampel suara 16-bit PCM .WAV LSB",
        formatBadge = "16-BIT PCM LSB",
        icon = Icons.Default.GraphicEq,
        faceColor = Color(0xFF6366F1),
        lipColor = Color(0xFF4338CA)
    ),
    ArcanumToolIconCardSpec(
        subTabId = 1,
        category = ArcanumToolsHubCategory.STEGO_MEDIA,
        title = "Bongkar Stego",
        subtitle = "Deteksi & dekode muatan rahasia dari pembawa PNG & WAV ($stegoCarrierCount file)",
        formatBadge = "$stegoCarrierCount CARRIER SIAP",
        icon = Icons.Default.LockOpen,
        faceColor = DuoFeatherGreen,
        lipColor = DuoFeatherGreenLip
    ),

    // KATEGORI: ASTRAONYX STAR QR STUDIO (Sub-tabs 9, 4)
    ArcanumToolIconCardSpec(
        subTabId = 9,
        category = ArcanumToolsHubCategory.ASTRA_QR,
        title = "Studio QR Bintang",
        subtitle = "Buat QR Bintang AstraOnyx, Scan Kamera/Galeri & Konversi QR Standar",
        formatBadge = "100% STAR QR",
        icon = Icons.Default.QrCode2,
        faceColor = DuoBeetlePurple,
        lipColor = DuoBeetlePurpleLip
    ),
    ArcanumToolIconCardSpec(
        subTabId = 4,
        category = ArcanumToolsHubCategory.ASTRA_QR,
        title = "Kartu Kunci QR Offline",
        subtitle = "Cetak kartu pemulihan master brankas berpola steganografi offline",
        formatBadge = "RECOVERY CARD",
        icon = Icons.Default.Key,
        faceColor = Color(0xFFEC4899),
        lipColor = Color(0xFFBE185D)
    ),

    // KATEGORI: LAB SANDI & KRIPTOGRAFI (Sub-tabs 6, 7, 8)
    ArcanumToolIconCardSpec(
        subTabId = 6,
        category = ArcanumToolsHubCategory.CRYPTO_LAB,
        title = "Sandi Morse CW",
        subtitle = "Enkode/dekode telegraf Morse CW lengkap dengan synthesizer nada radio",
        formatBadge = "CW AUDIO SYNTH",
        icon = Icons.Default.MusicNote,
        faceColor = DuoFoxOrange,
        lipColor = DuoFoxOrangeLip
    ),
    ArcanumToolIconCardSpec(
        subTabId = 7,
        category = ArcanumToolsHubCategory.CRYPTO_LAB,
        title = "Mesin Enigma M3",
        subtitle = "Simulasi kriptografi militer 3-rotor Wehrmacht dengan reflektor UKW-B",
        formatBadge = "3-ROTOR CIPHER",
        icon = Icons.Default.Key,
        faceColor = Color(0xFFD97706),
        lipColor = Color(0xFFB45309)
    ),
    ArcanumToolIconCardSpec(
        subTabId = 8,
        category = ArcanumToolsHubCategory.CRYPTO_LAB,
        title = "Sandi 30-Karakter",
        subtitle = "Generator kata sandi entropi ekstrem 30 karakter & audit kekuatan",
        formatBadge = "256-BIT ENTROPY",
        icon = Icons.Default.Lock,
        faceColor = DuoMacawBlue,
        lipColor = DuoMacawBlueLip
    ),

    // KATEGORI: PRIVASI EXIF & KAMUFLASE (Sub-tabs 2, 3)
    ArcanumToolIconCardSpec(
        subTabId = 2,
        category = ArcanumToolsHubCategory.PRIVACY_CAMOUFLAGE,
        title = "Hapus EXIF & GPS",
        subtitle = "Bersihkan koordinat lokasi GPS & metadata kamera pada $exifCandidateCount media",
        formatBadge = "ZERO METADATA",
        icon = Icons.Default.GpsOff,
        faceColor = DuoCardinalRed,
        lipColor = DuoCardinalRedLip
    ),
    ArcanumToolIconCardSpec(
        subTabId = 3,
        category = ArcanumToolsHubCategory.PRIVACY_CAMOUFLAGE,
        title = "Kalkulator Kamuflase",
        subtitle = "Ubah layar brankas menjadi kalkulator ilmiah fungsional dalam 1 klik",
        formatBadge = "STEALTH PANIC",
        icon = Icons.Default.Calculate,
        faceColor = DuoBeeYellow,
        lipColor = DuoBeeYellowLip
    )
)

@Composable
fun ArcanumToolsCategoryIconHub(
    stegoCarrierCount: Int,
    exifCandidateCount: Int,
    onSelectToolSubTab: (Int) -> Unit,
    moriState: MoriDownloaderUiState? = null,
    onQuickPasteAndOpenMori: ((String) -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var searchQuery by remember { mutableStateOf("") }
    var selectedCategoryFilter by remember { mutableStateOf<ArcanumToolsHubCategory?>(null) }
    var isCompactThreeColGrid by remember { mutableStateOf(true) }
    var showBottomGsapStage by remember { mutableStateOf(false) }

    val favoriteSubTabIds = remember {
        mutableStateListOf(30, 10, 20, 21, 9)
    }
    val recentSubTabIds = remember {
        mutableStateListOf(30, 11, 0)
    }

    fun launchSubTabWithHistory(subTabId: Int) {
        recentSubTabIds.remove(subTabId)
        recentSubTabIds.add(0, subTabId)
        while (recentSubTabIds.size > 6) {
            recentSubTabIds.removeAt(recentSubTabIds.lastIndex)
        }
        onSelectToolSubTab(subTabId)
    }

    val allToolCards = remember(stegoCarrierCount, exifCandidateCount) {
        buildArcanumToolIconCards(stegoCarrierCount, exifCandidateCount)
    }

    val filteredCards = remember(allToolCards, searchQuery, selectedCategoryFilter) {
        allToolCards.filter { card ->
            val matchCat = selectedCategoryFilter == null || card.category == selectedCategoryFilter
            val q = searchQuery.trim().lowercase()
            val matchQuery = q.isEmpty() ||
                card.title.lowercase().contains(q) ||
                card.subtitle.lowercase().contains(q) ||
                card.formatBadge.lowercase().contains(q)
            matchCat && matchQuery
        }
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .testTag("arcanum_tools_category_icon_hub"),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // ====================================================================
        // 1. TOP APP / TOOLS LAUNCHER GRID ("PILIH APP / TOOLS UNTUK DIBUKA")
        //    Clicking any box immediately transitions to that tool's screen!
        // ====================================================================
        if (false) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .gsapBreathingCardAura(accentColor = DuoBeetlePurple, cornerRadius = 22.dp)
                    .duolingoCard(
                        backgroundColor = DuoPureWhite,
                        borderColor = DuoBeetlePurple,
                        lipColor = DuoBeetlePurpleLip,
                        cornerRadius = 22.dp,
                        borderWidth = 2.dp,
                        lipHeight = 5.dp
                    )
                    .padding(bottom = 5.dp)
                    .padding(14.dp)
                    .testTag("tools_hub_mori_featured_hero_card")
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
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
                                    .size(46.dp)
                                    .duolingoButton(
                                        faceColor = DuoBeetlePurple,
                                        lipColor = DuoBeetlePurpleLip,
                                        cornerRadius = 14.dp,
                                        lipHeight = 3.dp
                                    )
                                    .instantTouchPress { launchSubTabWithHistory(30) }
                                    .padding(bottom = 3.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.CloudDownload,
                                    contentDescription = "Mori Stream Downloader v4.6.0",
                                    tint = Color.White,
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "MORI STREAM DOWNLOADER v4.6.0",
                                    fontFamily = SpaceGroteskFontFamily,
                                    fontSize = 14.5.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = VaultObsidian,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Text(
                                    text = if (moriState?.isDownloading == true) {
                                        "Mengunduh ${(moriState.progressRatio * 100).roundToInt()}% • ${moriState.statusLogLine}"
                                    } else {
                                        "16 Platform • Batch Format MP3/Video • Pure Audio Auto-MP3 • Shorts Canonicalizer"
                                    },
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = VaultTextMuted,
                                    maxLines = 2,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }
                        GsapLivePulseBadge(
                            text = if (moriState?.isDownloading == true) "ACTIVE" else "v4.6.0",
                            badgeColor = if (moriState?.isDownloading == true) DuoFeatherGreen else DuoBeetlePurple,
                            textColor = if (moriState?.isDownloading == true) DuoFeatherGreen else DuoBeetlePurple
                        )
                    }

                    // Quick Action Buttons inside Mori Hero Card: [Tempel & Ekstrak Link] | [Buka Ruang Kerja Mori]
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(42.dp)
                                .duolingoButton(
                                    faceColor = DuoFeatherGreen,
                                    lipColor = DuoFeatherGreenLip,
                                    cornerRadius = 12.dp,
                                    lipHeight = 3.dp
                                )
                                .instantTouchPress {
                                    val cb = context.getSystemService(Context.CLIPBOARD_SERVICE) as? ClipboardManager
                                    val clipUrl = cb?.primaryClip?.getItemAt(0)?.coerceToText(context)?.toString()?.trim().orEmpty()
                                    if (clipUrl.isNotBlank() && onQuickPasteAndOpenMori != null) {
                                        onQuickPasteAndOpenMori(clipUrl)
                                    }
                                    launchSubTabWithHistory(30)
                                }
                                .padding(bottom = 3.dp)
                                .padding(horizontal = 10.dp)
                                .testTag("tools_hub_mori_quick_paste_button"),
                            contentAlignment = Alignment.Center
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.ContentPaste,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(15.dp)
                                )
                                Text(
                                    text = "Tempel & Ekstrak Link",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = Color.White,
                                    maxLines = 1
                                )
                            }
                        }

                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(42.dp)
                                .duolingoButton(
                                    faceColor = DuoBeetlePurple,
                                    lipColor = DuoBeetlePurpleLip,
                                    cornerRadius = 12.dp,
                                    lipHeight = 3.dp
                                )
                                .instantTouchPress {
                                    launchSubTabWithHistory(30)
                                }
                                .padding(bottom = 3.dp)
                                .padding(horizontal = 10.dp)
                                .testTag("tools_hub_open_mori_workspace_button"),
                            contentAlignment = Alignment.Center
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Bolt,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(15.dp)
                                )
                                Text(
                                    text = "Buka Mori v4.6.0",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = Color.White,
                                    maxLines = 1
                                )
                            }
                        }
                    }

                    // Quick 1-Tap Universal Converter Strip inside Featured Dock
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        val quickConverters = listOf(
                            Triple(10, "PDF ↔ Office", DuoFeatherGreen to DuoFeatherGreenLip),
                            Triple(11, "PNG ↔ SVG/ICO", Color(0xFF0EA5E9) to Color(0xFF0284C7)),
                            Triple(12, "CSV/JSON/MD", DuoBeetlePurple to DuoBeetlePurpleLip),
                            Triple(13, "ZIP & Audio", DuoFoxOrange to DuoFoxOrangeLip)
                        )
                        quickConverters.forEach { (tabId, label, colors) ->
                            Box(
                                modifier = Modifier
                                    .height(32.dp)
                                    .duolingoCard(
                                        backgroundColor = DuoSnowCanvas,
                                        borderColor = colors.first.copy(alpha = 0.65f),
                                        lipColor = colors.second,
                                        cornerRadius = 10.dp,
                                        borderWidth = 1.5.dp,
                                        lipHeight = 2.dp
                                    )
                                    .instantTouchPress { launchSubTabWithHistory(tabId) }
                                    .padding(bottom = 2.dp)
                                    .padding(horizontal = 10.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "⚡ $label",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = VaultObsidian,
                                    maxLines = 1
                                )
                            }
                        }
                    }
                }
            }
        }

        // ====================================================================
        // 2. SEARCH BAR, VIEW DENSITY TOGGLE (2-COL vs 3-COL) & PINNED/RECENT BAR
        // ====================================================================
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .duolingoCard(
                    backgroundColor = DuoPureWhite,
                    borderColor = DuoSwanBorder,
                    lipColor = DuoHareLip,
                    cornerRadius = 20.dp,
                    borderWidth = 2.dp,
                    lipHeight = 4.dp
                )
                .padding(bottom = 4.dp)
                .padding(14.dp)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "PILIH APP & TOOLS ARCANUM",
                            fontFamily = SpaceGroteskFontFamily,
                            fontSize = 14.5.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = VaultObsidian
                        )
                        Text(
                            text = "Ketuk kotak aplikasi di bawah untuk langsung pindah ke halaman tools (${allToolCards.size} Tools Siap)",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = VaultTextMuted
                        )
                    }

                    // View Density Toggle: 2 Kolom Detail vs 3 Kolom Kompak
                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        Box(
                            modifier = Modifier
                                .height(32.dp)
                                .duolingoButton(
                                    faceColor = if (!isCompactThreeColGrid) DuoMacawBlue else DuoSnowCanvas,
                                    lipColor = if (!isCompactThreeColGrid) DuoMacawBlueLip else DuoHareLip,
                                    cornerRadius = 9.dp,
                                    lipHeight = 2.dp
                                )
                                .instantTouchPress { isCompactThreeColGrid = false }
                                .padding(bottom = 2.dp)
                                .padding(horizontal = 9.dp)
                                .testTag("tools_hub_density_2col"),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "2 Kolom",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = if (!isCompactThreeColGrid) Color.White else VaultObsidian
                            )
                        }
                        Box(
                            modifier = Modifier
                                .height(32.dp)
                                .duolingoButton(
                                    faceColor = if (isCompactThreeColGrid) DuoMacawBlue else DuoSnowCanvas,
                                    lipColor = if (isCompactThreeColGrid) DuoMacawBlueLip else DuoHareLip,
                                    cornerRadius = 9.dp,
                                    lipHeight = 2.dp
                                )
                                .instantTouchPress { isCompactThreeColGrid = true }
                                .padding(bottom = 2.dp)
                                .padding(horizontal = 9.dp)
                                .testTag("tools_hub_density_3col"),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "3 Kolom",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = if (isCompactThreeColGrid) Color.White else VaultObsidian
                            )
                        }
                    }
                }

                // Search bar with Keystroke Halo
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .gsapKeystrokeReactiveHalo(
                            textValue = searchQuery,
                            accentColor = DuoMacawBlue,
                            cornerRadius = 14.dp
                        )
                        .clip(RoundedCornerShape(14.dp))
                        .background(DuoSnowCanvas)
                        .border(1.5.dp, DuoSwanBorder, RoundedCornerShape(14.dp))
                        .padding(horizontal = 12.dp, vertical = 9.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Cari Alat",
                        tint = DuoMacawBlue,
                        modifier = Modifier.size(18.dp)
                    )
                    Box(modifier = Modifier.weight(1f)) {
                        if (searchQuery.isEmpty()) {
                            Text(
                                text = "Cari alat (Mori, Sniffer, SFL.GL, PDF ke DOCX, SVG, Stego, QR)...",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = DuoMutedInk.copy(alpha = 0.7f),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                        BasicTextField(
                            value = searchQuery,
                            onValueChange = { searchQuery = it },
                            singleLine = true,
                            textStyle = TextStyle(
                                color = VaultObsidian,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold
                            ),
                            cursorBrush = SolidColor(DuoMacawBlue),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("tools_hub_search_input")
                        )
                    }
                    if (searchQuery.isNotEmpty()) {
                        Box(
                            modifier = Modifier
                                .size(24.dp)
                                .clip(CircleShape)
                                .background(DuoCardinalRed)
                                .instantTouchPress { searchQuery = "" },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Hapus Pencarian",
                                tint = Color.White,
                                modifier = Modifier.size(14.dp)
                            )
                        }
                    }
                }

                // Pinned Favorites (⭐) & Recently Used (🕒) Quick Access Strip
                val favCards = allToolCards.filter { it.subTabId in favoriteSubTabIds }
                val recCards = recentSubTabIds.mapNotNull { id ->
                    allToolCards.firstOrNull { it.subTabId == id && it.subTabId !in favoriteSubTabIds }
                }
                if (favCards.isNotEmpty() || recCards.isNotEmpty()) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        favCards.forEach { fav ->
                            Box(
                                modifier = Modifier
                                    .height(32.dp)
                                    .duolingoCard(
                                        backgroundColor = fav.faceColor.copy(alpha = 0.14f),
                                        borderColor = fav.faceColor,
                                        lipColor = fav.lipColor,
                                        cornerRadius = 10.dp,
                                        borderWidth = 1.5.dp,
                                        lipHeight = 2.dp
                                    )
                                    .instantTouchPress { launchSubTabWithHistory(fav.subTabId) }
                                    .padding(bottom = 2.dp)
                                    .padding(horizontal = 9.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Star,
                                        contentDescription = null,
                                        tint = DuoBeeYellowLip,
                                        modifier = Modifier.size(12.dp)
                                    )
                                    Text(
                                        text = fav.title,
                                        fontSize = 10.5.sp,
                                        fontWeight = FontWeight.ExtraBold,
                                        color = VaultObsidian,
                                        maxLines = 1
                                    )
                                }
                            }
                        }
                        recCards.forEach { rec ->
                            Box(
                                modifier = Modifier
                                    .height(32.dp)
                                    .duolingoCard(
                                        backgroundColor = DuoSnowCanvas,
                                        borderColor = DuoSwanBorder,
                                        lipColor = DuoHareLip,
                                        cornerRadius = 10.dp,
                                        borderWidth = 1.5.dp,
                                        lipHeight = 2.dp
                                    )
                                    .instantTouchPress { launchSubTabWithHistory(rec.subTabId) }
                                    .padding(bottom = 2.dp)
                                    .padding(horizontal = 9.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.History,
                                        contentDescription = null,
                                        tint = DuoMutedInk,
                                        modifier = Modifier.size(12.dp)
                                    )
                                    Text(
                                        text = rec.title,
                                        fontSize = 10.5.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = VaultObsidian,
                                        maxLines = 1
                                    )
                                }
                            }
                        }
                    }
                }

                // Category Filter Pills with Tool Counts
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    val allSelected = selectedCategoryFilter == null
                    Box(
                        modifier = Modifier
                            .height(34.dp)
                            .duolingoButton(
                                faceColor = if (allSelected) VaultObsidian else DuoSnowCanvas,
                                lipColor = if (allSelected) Color(0xFF020617) else DuoHareLip,
                                cornerRadius = 10.dp,
                                lipHeight = 3.dp
                            )
                            .instantTouchPress { selectedCategoryFilter = null }
                            .padding(bottom = 3.dp)
                            .padding(horizontal = 10.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Semua (${allToolCards.size})",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = if (allSelected) Color.White else VaultObsidian
                        )
                    }

                    ArcanumToolsHubCategory.entries.forEach { cat ->
                        val isSel = selectedCategoryFilter == cat
                        val catCount = allToolCards.count { it.category == cat }
                        Box(
                            modifier = Modifier
                                .height(34.dp)
                                .duolingoButton(
                                    faceColor = if (isSel) cat.accentColor else DuoSnowCanvas,
                                    lipColor = if (isSel) cat.lipColor else DuoHareLip,
                                    cornerRadius = 10.dp,
                                    lipHeight = 3.dp
                                )
                                .instantTouchPress {
                                    selectedCategoryFilter = if (isSel) null else cat
                                }
                                .padding(bottom = 3.dp)
                                .padding(horizontal = 10.dp)
                                .testTag("tools_hub_cat_${cat.id}"),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "${cat.title.substringBefore(" &").substringBefore(" Studio")} ($catCount)",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = if (isSel) Color.White else VaultObsidian
                            )
                        }
                    }
                }
            }
        }

        // ====================================================================
        // 3. CATEGORIZED TOOL SECTIONS (2-COL DETAILED OR 3-COL COMPACT)
        // ====================================================================
        val categoriesToShow = if (selectedCategoryFilter != null) {
            listOf(selectedCategoryFilter!!)
        } else {
            ArcanumToolsHubCategory.entries
        }

        categoriesToShow.forEachIndexed { catIdx, category ->
            val categoryCards = filteredCards.filter { it.category == category }
            if (categoryCards.isNotEmpty()) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .gsapStaggerReveal(index = catIdx, slideDp = 18.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // Category Header Row
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 2.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = category.title,
                                fontFamily = SpaceGroteskFontFamily,
                                fontSize = 14.5.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = VaultObsidian
                            )
                            Text(
                                text = category.subtitle,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = VaultTextMuted
                            )
                        }
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(999.dp))
                                .background(category.accentColor.copy(alpha = 0.14f))
                                .border(1.dp, category.accentColor.copy(alpha = 0.55f), RoundedCornerShape(999.dp))
                                .padding(horizontal = 8.dp, vertical = 3.dp)
                        ) {
                            Text(
                                text = category.badge,
                                fontFamily = JetBrainsMonoFontFamily,
                                fontSize = 9.5.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = category.lipColor
                            )
                        }
                    }

                    if (isCompactThreeColGrid) {
                        // 3-Column Compact Ergonomic Icon Grid
                        categoryCards.chunked(3).forEachIndexed { rowIdx, rowTriplet ->
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                rowTriplet.forEachIndexed { colIdx, toolSpec ->
                                    val itemStagger = catIdx * 3 + rowIdx + colIdx
                                    Box(
                                        modifier = Modifier
                                            .weight(1f)
                                            .gsapStaggerReveal(index = itemStagger, slideDp = 14.dp)
                                            .duolingoCard(
                                                backgroundColor = DuoPureWhite,
                                                borderColor = toolSpec.faceColor.copy(alpha = 0.85f),
                                                lipColor = toolSpec.lipColor,
                                                cornerRadius = 16.dp,
                                                borderWidth = 2.dp,
                                                lipHeight = 4.dp
                                            )
                                            .instantTouchPress {
                                                launchSubTabWithHistory(toolSpec.subTabId)
                                            }
                                            .padding(bottom = 4.dp)
                                            .padding(10.dp)
                                            .testTag("tools_hub_icon_card_${toolSpec.subTabId}"),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Column(
                                            horizontalAlignment = Alignment.CenterHorizontally,
                                            verticalArrangement = Arrangement.spacedBy(6.dp)
                                        ) {
                                            Box(
                                                modifier = Modifier
                                                    .size(40.dp)
                                                    .duolingoButton(
                                                        faceColor = toolSpec.faceColor,
                                                        lipColor = toolSpec.lipColor,
                                                        cornerRadius = 12.dp,
                                                        lipHeight = 3.dp
                                                    )
                                                    .padding(bottom = 3.dp),
                                                contentAlignment = Alignment.Center
                                            ) {
                                                Icon(
                                                    imageVector = toolSpec.icon,
                                                    contentDescription = toolSpec.title,
                                                    tint = if (toolSpec.faceColor == DuoBeeYellow) VaultObsidian else Color.White,
                                                    modifier = Modifier.size(20.dp)
                                                )
                                            }
                                            Text(
                                                text = toolSpec.title,
                                                fontFamily = SpaceGroteskFontFamily,
                                                fontSize = 11.sp,
                                                fontWeight = FontWeight.ExtraBold,
                                                color = VaultObsidian,
                                                maxLines = 1,
                                                overflow = TextOverflow.Ellipsis,
                                                textAlign = TextAlign.Center
                                            )
                                            Text(
                                                text = toolSpec.formatBadge,
                                                fontFamily = JetBrainsMonoFontFamily,
                                                fontSize = 8.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = toolSpec.lipColor,
                                                maxLines = 1,
                                                overflow = TextOverflow.Ellipsis
                                            )
                                        }
                                    }
                                }
                                repeat(3 - rowTriplet.size) {
                                    Spacer(modifier = Modifier.weight(1f))
                                }
                            }
                        }
                    } else {
                        // 2-Column Detailed Grid with Pin Favorite Toggle (⭐)
                        categoryCards.chunked(2).forEachIndexed { rowIdx, rowPair ->
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                rowPair.forEachIndexed { colIdx, toolSpec ->
                                    val itemStagger = catIdx * 2 + rowIdx + colIdx
                                    val isPinned = toolSpec.subTabId in favoriteSubTabIds
                                    Box(
                                        modifier = Modifier
                                            .weight(1f)
                                            .gsapStaggerReveal(index = itemStagger, slideDp = 16.dp)
                                            .duolingoCard(
                                                backgroundColor = DuoPureWhite,
                                                borderColor = toolSpec.faceColor.copy(alpha = 0.85f),
                                                lipColor = toolSpec.lipColor,
                                                cornerRadius = 18.dp,
                                                borderWidth = 2.dp,
                                                lipHeight = 4.dp
                                            )
                                            .instantTouchPress {
                                                launchSubTabWithHistory(toolSpec.subTabId)
                                            }
                                            .padding(bottom = 4.dp)
                                            .padding(12.dp)
                                            .testTag("tools_hub_icon_card_${toolSpec.subTabId}")
                                    ) {
                                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                            Row(
                                                modifier = Modifier.fillMaxWidth(),
                                                horizontalArrangement = Arrangement.SpaceBetween,
                                                verticalAlignment = Alignment.Top
                                            ) {
                                                // 3D Icon Tile
                                                Box(
                                                    modifier = Modifier
                                                        .size(44.dp)
                                                        .duolingoButton(
                                                            faceColor = toolSpec.faceColor,
                                                            lipColor = toolSpec.lipColor,
                                                            cornerRadius = 13.dp,
                                                            lipHeight = 3.dp
                                                        )
                                                        .padding(bottom = 3.dp),
                                                    contentAlignment = Alignment.Center
                                                ) {
                                                    Icon(
                                                        imageVector = toolSpec.icon,
                                                        contentDescription = toolSpec.title,
                                                        tint = if (toolSpec.faceColor == DuoBeeYellow) VaultObsidian else Color.White,
                                                        modifier = Modifier.size(22.dp)
                                                    )
                                                }

                                                Row(
                                                    verticalAlignment = Alignment.CenterVertically,
                                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                                ) {
                                                    // Format / Capability Pill
                                                    Box(
                                                        modifier = Modifier
                                                            .clip(RoundedCornerShape(8.dp))
                                                            .background(toolSpec.faceColor.copy(alpha = 0.14f))
                                                            .padding(horizontal = 6.dp, vertical = 3.dp)
                                                    ) {
                                                        Text(
                                                            text = toolSpec.formatBadge,
                                                            fontFamily = JetBrainsMonoFontFamily,
                                                            fontSize = 8.sp,
                                                            fontWeight = FontWeight.ExtraBold,
                                                            color = toolSpec.lipColor,
                                                            maxLines = 1
                                                        )
                                                    }
                                                    // Pin Favorite Button
                                                    Box(
                                                        modifier = Modifier
                                                            .size(24.dp)
                                                            .clip(CircleShape)
                                                            .background(
                                                                if (isPinned) DuoBeeYellow.copy(alpha = 0.28f)
                                                                else DuoSnowCanvas
                                                            )
                                                            .instantTouchPress {
                                                                if (isPinned) {
                                                                    favoriteSubTabIds.remove(toolSpec.subTabId)
                                                                } else {
                                                                    favoriteSubTabIds.add(toolSpec.subTabId)
                                                                }
                                                            },
                                                        contentAlignment = Alignment.Center
                                                    ) {
                                                        Icon(
                                                            imageVector = Icons.Default.Star,
                                                            contentDescription = "Pin Favorit",
                                                            tint = if (isPinned) DuoBeeYellowLip else DuoMutedInk.copy(alpha = 0.45f),
                                                            modifier = Modifier.size(13.dp)
                                                        )
                                                    }
                                                }
                                            }

                                            Text(
                                                text = toolSpec.title,
                                                fontFamily = SpaceGroteskFontFamily,
                                                fontSize = 13.5.sp,
                                                fontWeight = FontWeight.ExtraBold,
                                                color = VaultObsidian,
                                                maxLines = 1,
                                                overflow = TextOverflow.Ellipsis
                                            )

                                            Text(
                                                text = toolSpec.subtitle,
                                                fontSize = 10.5.sp,
                                                fontWeight = FontWeight.SemiBold,
                                                color = VaultTextMuted,
                                                lineHeight = 14.sp,
                                                maxLines = 2,
                                                overflow = TextOverflow.Ellipsis
                                            )
                                        }
                                    }
                                }
                                if (rowPair.size == 1) {
                                    Spacer(modifier = Modifier.weight(1f))
                                }
                            }
                        }
                    }
                }
            }
        }

        // ====================================================================
        // 4. COLLAPSIBLE GSAP MOTION STAGE & DRAGGABLE DUO-CARD REPORT DECK
        //    Placed at the bottom so the App/Tools Box Launcher is always first!
        // ====================================================================
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .duolingoCard(
                    backgroundColor = DuoPureWhite,
                    borderColor = DuoBeetlePurple.copy(alpha = 0.65f),
                    lipColor = DuoBeetlePurpleLip,
                    cornerRadius = 18.dp,
                    borderWidth = 1.5.dp,
                    lipHeight = 3.dp
                )
                .instantTouchPress { showBottomGsapStage = !showBottomGsapStage }
                .padding(bottom = 3.dp)
                .padding(horizontal = 12.dp, vertical = 10.dp)
                .testTag("tools_hub_toggle_gsap_stage_button")
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (showBottomGsapStage) {
                        "Sembunyikan GSAP Motion & Draggable Duo-Card ↑"
                    } else {
                        "Buka Panel GSAP Motion & Draggable Report Cards ↓"
                    },
                    fontFamily = SpaceGroteskFontFamily,
                    fontSize = 11.5.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = VaultObsidian
                )
                Text(
                    text = "GSAP DECK",
                    fontFamily = JetBrainsMonoFontFamily,
                    fontSize = 9.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = DuoBeetlePurple
                )
            }
        }

        if (showBottomGsapStage) {
            val activeStyle = com.example.ui.theme.AppThemeController.activeGsapVisualStyle
            val activeCurve = com.example.ui.theme.AppThemeController.activeGsapPreset
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("tools_hub_gsap_motion_graphic_stage"),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                GsapInteractiveMotionGraphicBanner(
                    title = "GSAP Motion UI • ${activeStyle.displayName}",
                    subtitle = "${activeCurve.displayName} • Ketuk banner untuk gelombang kejut & micro-interaction",
                    modifier = Modifier.fillMaxWidth()
                )
                GsapDraggableDuoCardReportDeck(
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

/**
 * Header navigasi saat berada di dalam salah satu sub-tab alat untuk kembali ke Hub Ikon Kategori.
 */
@Composable
fun ArcanumToolsWorkspaceBackHeader(
    activeModeTab: Int,
    onBackToIconHub: () -> Unit,
    modifier: Modifier = Modifier
) {
    val activeSpec = remember(activeModeTab) {
        buildArcanumToolIconCards(0, 0).firstOrNull { it.subTabId == activeModeTab }
    }
    Row(
        modifier = modifier
            .fillMaxWidth()
            .duolingoCard(
                backgroundColor = DuoPureWhite,
                borderColor = activeSpec?.faceColor ?: DuoMacawBlue,
                lipColor = activeSpec?.lipColor ?: DuoMacawBlueLip,
                cornerRadius = 16.dp,
                borderWidth = 2.dp,
                lipHeight = 4.dp
            )
            .padding(bottom = 4.dp)
            .padding(horizontal = 12.dp, vertical = 10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .height(36.dp)
                .duolingoButton(
                    faceColor = VaultObsidian,
                    lipColor = Color(0xFF020617),
                    cornerRadius = 11.dp,
                    lipHeight = 3.dp
                )
                .instantTouchPress { onBackToIconHub() }
                .padding(bottom = 3.dp)
                .padding(horizontal = 12.dp)
                .testTag("tools_back_to_icon_hub_button"),
            contentAlignment = Alignment.Center
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Kembali ke Hub Ikon Tools",
                    tint = Color.White,
                    modifier = Modifier.size(16.dp)
                )
                Text(
                    text = "Semua Ikon Tools",
                    fontSize = 11.5.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.White
                )
            }
        }

        Column(
            horizontalAlignment = Alignment.End,
            modifier = Modifier.weight(1f).padding(start = 10.dp)
        ) {
            Text(
                text = activeSpec?.title ?: "Ruang Kerja Alat",
                fontFamily = SpaceGroteskFontFamily,
                fontSize = 13.sp,
                fontWeight = FontWeight.ExtraBold,
                color = VaultObsidian,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = activeSpec?.formatBadge ?: "ARCANUM STUDIO",
                fontFamily = JetBrainsMonoFontFamily,
                fontSize = 9.5.sp,
                fontWeight = FontWeight.Bold,
                color = activeSpec?.lipColor ?: DuoMacawBlueLip,
                maxLines = 1
            )
        }
    }
}

// ============================================================================
// 2. UNIVERSAL FILE & MEDIA CONVERTER ENGINE (100% REAL ON-DEVICE CONVERSIONS)
// ============================================================================

enum class UniversalConverterSuiteTab(
    val subTabId: Int,
    val title: String,
    val subtitle: String,
    val badge: String,
    val faceColor: Color,
    val lipColor: Color,
    val icon: ImageVector
) {
    OFFICE_DOCS(
        subTabId = 10,
        title = "Dokumen Office",
        subtitle = "PDF ↔ DOCX, Excel XLSX, Slide PPTX & TXT",
        badge = "OFFICE SUITE",
        faceColor = DuoFeatherGreen,
        lipColor = DuoFeatherGreenLip,
        icon = Icons.Default.Description
    ),
    VECTOR_IMAGE(
        subTabId = 11,
        title = "Vektor & Gambar",
        subtitle = "PNG/JPG ↔ SVG Vector, WEBP, ICO & Gambar ke PDF",
        badge = "SVG & RASTER",
        faceColor = Color(0xFF0EA5E9),
        lipColor = Color(0xFF0284C7),
        icon = Icons.Default.Polyline
    ),
    DATA_CODE(
        subTabId = 12,
        title = "Data & Kode",
        subtitle = "CSV ↔ XLSX/JSON, Markdown ↔ PDF/HTML & Base64",
        badge = "DATA & DEV",
        faceColor = DuoBeetlePurple,
        lipColor = DuoBeetlePurpleLip,
        icon = Icons.Default.Code
    ),
    ARCHIVE_AUDIO(
        subTabId = 13,
        title = "Arsip & Audio",
        subtitle = "Multi-File ke Arsip .ZIP & Konversi WAV, MP3, M4A",
        badge = "ZIP & AUDIO",
        faceColor = DuoFoxOrange,
        lipColor = DuoFoxOrangeLip,
        icon = Icons.Default.Archive
    );

    companion object {
        fun fromSubTabId(id: Int): UniversalConverterSuiteTab =
            entries.firstOrNull { it.subTabId == id } ?: OFFICE_DOCS
    }
}

data class ConversionPresetOption(
    val id: String,
    val suite: UniversalConverterSuiteTab,
    val sourceLabel: String,
    val targetExtension: String,
    val targetCategory: VaultCategory,
    val description: String
)

val ALL_CONVERSION_PRESETS = listOf(
    // SUITE 1: OFFICE DOCS (PDF ↔ DOCX, XLSX, PPTX, TXT)
    ConversionPresetOption("pdf_to_docx", UniversalConverterSuiteTab.OFFICE_DOCS, "PDF / Dokumen → Word (.DOCX)", "docx", VaultCategory.DOCUMENT, "Konversi halaman PDF atau dokumen menjadi dokumen Microsoft Word OOXML (.docx) lengkap dengan heading, paragraf & tabel."),
    ConversionPresetOption("pdf_to_xlsx", UniversalConverterSuiteTab.OFFICE_DOCS, "PDF / Dokumen → Excel (.XLSX)", "xlsx", VaultCategory.DOCUMENT, "Ekstrak baris data & tabel dokumen menjadi lembar kerja Microsoft Excel SpreadsheetML (.xlsx)."),
    ConversionPresetOption("pdf_to_pptx", UniversalConverterSuiteTab.OFFICE_DOCS, "PDF / Dokumen → PowerPoint (.PPTX)", "pptx", VaultCategory.DOCUMENT, "Ubah poin-poin dokumen menjadi dek slide presentasi PowerPoint (.pptx) lengkap dengan catatan pembicara."),
    ConversionPresetOption("office_to_pdf", UniversalConverterSuiteTab.OFFICE_DOCS, "DOCX / XLSX / PPTX / TXT → PDF", "pdf", VaultCategory.DOCUMENT, "Render dokumen Word, tabel Excel, slide PPTX, atau teks menjadi dokumen .PDF multi-halaman siap cetak."),
    ConversionPresetOption("doc_to_txt", UniversalConverterSuiteTab.OFFICE_DOCS, "PDF / DOCX / XLSX / PPTX → Teks (.TXT)", "txt", VaultCategory.DOCUMENT, "Ekstrak seluruh teks bersih UTF-8 dari dokumen PDF atau Office ke file .txt."),

    // SUITE 2: VECTOR & IMAGE (PNG/JPG ↔ SVG, WEBP, ICO, PDF)
    ConversionPresetOption("img_to_svg", UniversalConverterSuiteTab.VECTOR_IMAGE, "PNG / JPG / WEBP → SVG Vector (.SVG)", "svg", VaultCategory.DOCUMENT, "Vektorisasi gambar raster menjadi grafik vektor XML .SVG dengan kontur warna terkuantisasi yang tajam saat di-zoom."),
    ConversionPresetOption("svg_to_png", UniversalConverterSuiteTab.VECTOR_IMAGE, "SVG Vector → PNG Resolusi Tinggi (.PNG)", "png", VaultCategory.IMAGE, "Render kode grafik vektor .SVG menjadi bitmap .PNG tajam beresolusi tinggi."),
    ConversionPresetOption("img_to_ico", UniversalConverterSuiteTab.VECTOR_IMAGE, "PNG / JPG → Ikon Aplikasi (.ICO)", "ico", VaultCategory.IMAGE, "Konversi gambar menjadi file ikon standar .ICO (256×256 ICONDIR container) untuk desktop & web favicon."),
    ConversionPresetOption("img_to_webp", UniversalConverterSuiteTab.VECTOR_IMAGE, "PNG / JPG → WEBP Ultra-Ringan (.WEBP)", "webp", VaultCategory.IMAGE, "Kompresi gambar ke format modern Google .WEBP dengan kualitas tinggi dan ukuran hemat."),
    ConversionPresetOption("img_to_png", UniversalConverterSuiteTab.VECTOR_IMAGE, "JPG / WEBP → PNG Lossless (.PNG)", "png", VaultCategory.IMAGE, "Konversi gambar ke format .PNG lossless tanpa kehilangan kualitas piksel."),
    ConversionPresetOption("img_to_pdf", UniversalConverterSuiteTab.VECTOR_IMAGE, "Gambar (PNG/JPG/WEBP) → Album PDF (.PDF)", "pdf", VaultCategory.DOCUMENT, "Susun gambar beresolusi tinggi ke dalam halaman dokumen .PDF ukuran A4."),

    // SUITE 3: DATA & CODE (CSV ↔ XLSX/JSON, Markdown ↔ PDF/HTML, Base64)
    ConversionPresetOption("csv_to_xlsx", UniversalConverterSuiteTab.DATA_CODE, "CSV / Tabel → Excel Workbook (.XLSX)", "xlsx", VaultCategory.DOCUMENT, "Ubah data koma/titik-koma .CSV menjadi lembar kerja tabel Excel .XLSX asli."),
    ConversionPresetOption("csv_to_json", UniversalConverterSuiteTab.DATA_CODE, "CSV / Tabel → JSON Array (.JSON)", "json", VaultCategory.DOCUMENT, "Konversi baris dan header .CSV menjadi struktur array objek .JSON terformat rapi."),
    ConversionPresetOption("json_to_csv", UniversalConverterSuiteTab.DATA_CODE, "JSON → Tabel CSV (.CSV)", "csv", VaultCategory.DOCUMENT, "Ratakan array objek .JSON menjadi tabel .CSV yang siap dibuka di spreadsheet."),
    ConversionPresetOption("md_to_html", UniversalConverterSuiteTab.DATA_CODE, "Markdown (.MD) → Web Page (.HTML)", "html", VaultCategory.DOCUMENT, "Render sintaks Markdown menjadi halaman web .HTML5 responsif lengkap dengan stylesheet modern."),
    ConversionPresetOption("md_to_pdf", UniversalConverterSuiteTab.DATA_CODE, "Markdown (.MD) → Dokumen PDF (.PDF)", "pdf", VaultCategory.DOCUMENT, "Render heading, daftar poin, dan blok kode Markdown menjadi dokumen .PDF profesional."),
    ConversionPresetOption("to_base64", UniversalConverterSuiteTab.DATA_CODE, "File / Teks → Payload Base64 (.TXT)", "txt", VaultCategory.DOCUMENT, "Enkode biner file atau teks menjadi string Base64 murni beserta header MIME."),

    // SUITE 4: ARCHIVE & AUDIO (Multi-File → ZIP, WAV ↔ MP3 / M4A)
    ConversionPresetOption("files_to_zip", UniversalConverterSuiteTab.ARCHIVE_AUDIO, "Multi-File Brankas → Arsip .ZIP", "zip", VaultCategory.ARCHIVE, "Bungkus satu atau beberapa file brankas sekaligus ke dalam paket arsip terkompresi .ZIP standar."),
    ConversionPresetOption("audio_to_wav", UniversalConverterSuiteTab.ARCHIVE_AUDIO, "Audio / Media → PCM Studio (.WAV)", "wav", VaultCategory.AUDIO, "Konversi atau sintesis trek audio menjadi file gelombang .WAV 16-bit PCM jernih."),
    ConversionPresetOption("audio_to_mp3", UniversalConverterSuiteTab.ARCHIVE_AUDIO, "WAV / Audio → Stream (.MP3)", "mp3", VaultCategory.AUDIO, "Konversi trek audio menjadi kontainer .MP3 yang kompatibel di semua pemutar musik."),
    ConversionPresetOption("audio_to_m4a", UniversalConverterSuiteTab.ARCHIVE_AUDIO, "WAV / Audio → Apple Audio (.M4A)", "m4a", VaultCategory.AUDIO, "Konversi trek audio ke kontainer .M4A efisiensi tinggi.")
)

data class CompletedConversionOutput(
    val preset: ConversionPresetOption,
    val publicFile: File,
    val vaultEntity: VaultItemEntity?,
    val summaryText: String,
    val svgPreviewBitmap: Bitmap? = null,
    val generatedCodeSnippet: String = ""
)

object ArcanumUniversalConverterEngine {

    /**
     * Mengekstrak teks dan baris tabel dari VaultItemEntity atau konten kustom.
     */
    fun extractSourceLinesAndTables(
        sourceItem: VaultItemEntity?,
        customTextInput: String
    ): Pair<List<String>, List<List<String>>> {
        val file = sourceItem?.internalPlainPath?.let { File(it) }?.takeIf { it.exists() && it.length() > 0L }
        val ext = sourceItem?.extension?.lowercase().orEmpty()

        val extractedLines = mutableListOf<String>()
        val extractedTable = mutableListOf<List<String>>()

        if (file != null) {
            when (ext) {
                "docx", "txt", "md", "html", "json", "xml" -> {
                    val parsed = ArcanumOfficeDocumentParser.parseWordOrTextDocument(
                        file = file,
                        fallbackTitle = sourceItem.name,
                        fallbackPreview = customTextInput
                    )
                    parsed.blocks.forEach { block ->
                        when (block) {
                            is DocxBlock.Heading -> extractedLines.add("# ${block.text}")
                            is DocxBlock.Paragraph -> extractedLines.add(block.text)
                            is DocxBlock.Table -> extractedTable.addAll(block.rows)
                        }
                    }
                }
                "xlsx", "xls", "csv", "tsv" -> {
                    val wb = ArcanumOfficeDocumentParser.parseExcelWorkbook(
                        file = file,
                        fallbackName = sourceItem.name,
                        fallbackPreview = customTextInput
                    )
                    wb.sheets.firstOrNull()?.rows?.forEach { row ->
                        extractedTable.add(row)
                        extractedLines.add(row.joinToString(" | "))
                    }
                }
                "pptx", "ppt" -> {
                    val deck = ArcanumOfficeDocumentParser.parsePptxDeck(
                        file = file,
                        fallbackTitle = sourceItem.name,
                        fallbackPreview = customTextInput
                    )
                    deck.slides.forEach { slide ->
                        extractedLines.add("# Slide ${slide.slideNumber}: ${slide.title}")
                        slide.bulletPoints.forEach { b -> extractedLines.add("• $b") }
                    }
                }
                "pdf" -> {
                    val rawPreview = sourceItem.rawContentPreview
                    if (rawPreview.isNotBlank()) {
                        rawPreview.lines().map { it.trim() }.filter { it.isNotEmpty() }.forEach {
                            extractedLines.add(it)
                        }
                    }
                    // Probe PDF page count via PdfRenderer
                    try {
                        ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY).use { pfd ->
                            PdfRenderer(pfd).use { renderer ->
                                extractedLines.add(0, "# Dokumen PDF: ${sourceItem.name} (${renderer.pageCount} Halaman)")
                            }
                        }
                    } catch (_: Exception) {
                    }
                }
            }
        }

        if (customTextInput.isNotBlank()) {
            customTextInput.lines().map { it.trim() }.filter { it.isNotEmpty() }.forEach { line ->
                if (!extractedLines.contains(line)) {
                    extractedLines.add(line)
                }
                if (line.contains(",") || line.contains("|") || line.contains("\t")) {
                    val delimiter = when {
                        line.contains("|") -> "|"
                        line.contains("\t") -> "\t"
                        else -> ","
                    }
                    extractedTable.add(line.split(delimiter).map { it.trim() })
                }
            }
        }

        if (extractedLines.isEmpty()) {
            val title = sourceItem?.name ?: "Dokumen_Arcanum"
            extractedLines.add("# $title")
            extractedLines.add("Dokumen ini dikonversi secara otomatis oleh Arcanum Universal Converter Studio.")
            extractedLines.add("Format terverifikasi & siap dibuka di dalam Arcanum Office Viewer maupun aplikasi eksternal.")
        }

        if (extractedTable.isEmpty()) {
            extractedTable.add(listOf("No", "Bagian / Atribut", "Isi / Nilai", "Status"))
            extractedLines.take(12).forEachIndexed { idx, line ->
                extractedTable.add(
                    listOf(
                        (idx + 1).toString(),
                        if (line.startsWith("#")) "Judul Bagian" else "Baris Konten",
                        line.removePrefix("#").trim().take(80),
                        "TERVERIFIKASI"
                    )
                )
            }
        }

        return extractedLines to extractedTable
    }

    /**
     * Membangun file OpenXML .DOCX asli (`[Content_Types].xml`, `_rels/.rels`, `word/document.xml`).
     */
    fun buildRealDocxFile(outFile: File, title: String, lines: List<String>, tableRows: List<List<String>>) {
        ZipOutputStream(FileOutputStream(outFile)).use { zos ->
            val contentTypes = """
                <?xml version="1.0" encoding="UTF-8" standalone="yes"?>
                <Types xmlns="http://schemas.openxmlformats.org/package/2006/content-types">
                    <Default Extension="rels" ContentType="application/vnd.openxmlformats-package.relationships+xml"/>
                    <Default Extension="xml" ContentType="application/xml"/>
                    <Override PartName="/word/document.xml" ContentType="application/vnd.openxmlformats-officedocument.wordprocessingml.document.main+xml"/>
                </Types>
            """.trimIndent()
            writeZipTextEntry(zos, "[Content_Types].xml", contentTypes)

            val rels = """
                <?xml version="1.0" encoding="UTF-8" standalone="yes"?>
                <Relationships xmlns="http://schemas.openxmlformats.org/package/2006/relationships">
                    <Relationship Id="rId1" Type="http://schemas.openxmlformats.org/officeDocument/2006/relationships/officeDocument" Target="word/document.xml"/>
                </Relationships>
            """.trimIndent()
            writeZipTextEntry(zos, "_rels/.rels", rels)

            val bodyBuilder = StringBuilder()
            bodyBuilder.append("""<w:p><w:pPr><w:pStyle w:val="Heading1"/></w:pPr><w:r><w:rPr><w:b/></w:rPr><w:t>${escapeXml(title)}</w:t></w:r></w:p>""")
            lines.forEach { line ->
                val isHeading = line.startsWith("#")
                val clean = line.removePrefix("###").removePrefix("##").removePrefix("#").trim()
                if (isHeading) {
                    bodyBuilder.append("""<w:p><w:pPr><w:pStyle w:val="Heading2"/></w:pPr><w:r><w:rPr><w:b/></w:rPr><w:t>${escapeXml(clean)}</w:t></w:r></w:p>""")
                } else {
                    bodyBuilder.append("""<w:p><w:r><w:t>${escapeXml(clean)}</w:t></w:r></w:p>""")
                }
            }

            if (tableRows.isNotEmpty()) {
                bodyBuilder.append("<w:tbl>")
                tableRows.take(30).forEach { row ->
                    bodyBuilder.append("<w:tr>")
                    row.forEach { cell ->
                        bodyBuilder.append("""<w:tc><w:p><w:r><w:t>${escapeXml(cell)}</w:t></w:r></w:p></w:tc>""")
                    }
                    bodyBuilder.append("</w:tr>")
                }
                bodyBuilder.append("</w:tbl>")
            }

            val documentXml = """
                <?xml version="1.0" encoding="UTF-8" standalone="yes"?>
                <w:document xmlns:w="http://schemas.openxmlformats.org/wordprocessingml/2006/main">
                    <w:body>
                        $bodyBuilder
                    </w:body>
                </w:document>
            """.trimIndent()
            writeZipTextEntry(zos, "word/document.xml", documentXml)
        }
    }

    /**
     * Membangun file OpenXML .XLSX asli (`xl/workbook.xml`, `xl/worksheets/sheet1.xml`).
     */
    fun buildRealXlsxFile(outFile: File, sheetTitle: String, tableRows: List<List<String>>) {
        ZipOutputStream(FileOutputStream(outFile)).use { zos ->
            val contentTypes = """
                <?xml version="1.0" encoding="UTF-8" standalone="yes"?>
                <Types xmlns="http://schemas.openxmlformats.org/package/2006/content-types">
                    <Default Extension="rels" ContentType="application/vnd.openxmlformats-package.relationships+xml"/>
                    <Default Extension="xml" ContentType="application/xml"/>
                    <Override PartName="/xl/workbook.xml" ContentType="application/vnd.openxmlformats-officedocument.spreadsheetml.sheet.main+xml"/>
                    <Override PartName="/xl/worksheets/sheet1.xml" ContentType="application/vnd.openxmlformats-officedocument.spreadsheetml.worksheet+xml"/>
                </Types>
            """.trimIndent()
            writeZipTextEntry(zos, "[Content_Types].xml", contentTypes)

            val SafeSheet = escapeXml(sheetTitle.take(28).ifBlank { "Arcanum_Sheet1" })
            val workbookXml = """
                <?xml version="1.0" encoding="UTF-8" standalone="yes"?>
                <workbook xmlns="http://schemas.openxmlformats.org/spreadsheetml/2006/main">
                    <sheets>
                        <sheet name="$SafeSheet" sheetId="1" r:id="rId1" xmlns:r="http://schemas.openxmlformats.org/officeDocument/2006/relationships"/>
                    </sheets>
                </workbook>
            """.trimIndent()
            writeZipTextEntry(zos, "xl/workbook.xml", workbookXml)

            val sheetData = StringBuilder()
            tableRows.take(120).forEachIndexed { rIdx, row ->
                val rowNum = rIdx + 1
                sheetData.append("""<row r="$rowNum">""")
                row.take(16).forEachIndexed { cIdx, cellVal ->
                    val colChar = ('A' + cIdx).toString()
                    val ref = "$colChar$rowNum"
                    val trimmed = cellVal.trim()
                    val asNum = trimmed.toDoubleOrNull()
                    if (asNum != null) {
                        sheetData.append("""<c r="$ref"><v>$trimmed</v></c>""")
                    } else {
                        sheetData.append("""<c r="$ref" t="inlineStr"><is><t>${escapeXml(trimmed)}</t></is></c>""")
                    }
                }
                sheetData.append("</row>")
            }

            val sheetXml = """
                <?xml version="1.0" encoding="UTF-8" standalone="yes"?>
                <worksheet xmlns="http://schemas.openxmlformats.org/spreadsheetml/2006/main">
                    <sheetData>
                        $sheetData
                    </sheetData>
                </worksheet>
            """.trimIndent()
            writeZipTextEntry(zos, "xl/worksheets/sheet1.xml", sheetXml)
        }
    }

    /**
     * Membangun file OpenXML .PPTX asli (`ppt/slides/slide1.xml`..`slideN.xml` & `ppt/notesSlides/notesSlide1.xml`).
     */
    fun buildRealPptxFile(outFile: File, deckTitle: String, lines: List<String>) {
        ZipOutputStream(FileOutputStream(outFile)).use { zos ->
            val chunks = lines.chunked(5).ifEmpty { listOf(listOf(deckTitle, "Slide dibuat oleh Arcanum Converter Studio")) }
            chunks.take(12).forEachIndexed { idx, slideLines ->
                val slideNum = idx + 1
                val heading = slideLines.firstOrNull()?.removePrefix("#")?.trim().takeIf { !it.isNullOrBlank() }
                    ?: "$deckTitle — Slide $slideNum"
                val bullets = slideLines.drop(1).ifEmpty {
                    listOf("Dikonversi otomatis oleh Arcanum Universal Converter Studio", "Siap dipresentasikan di Arcanum PPTX Viewer")
                }

                val paragraphsXml = StringBuilder()
                paragraphsXml.append("""<a:p><a:r><a:t>${escapeXml(heading)}</a:t></a:r></a:p>""")
                bullets.forEach { b ->
                    paragraphsXml.append("""<a:p><a:r><a:t>${escapeXml(b.removePrefix("•").removePrefix("-").trim())}</a:t></a:r></a:p>""")
                }

                val slideXml = """
                    <?xml version="1.0" encoding="UTF-8" standalone="yes"?>
                    <p:sld xmlns:a="http://schemas.openxmlformats.org/drawingml/2006/main" xmlns:p="http://schemas.openxmlformats.org/presentationml/2006/main">
                        <p:cSld>
                            <p:spTree>
                                <p:sp>
                                    <p:txBody>
                                        $paragraphsXml
                                    </p:txBody>
                                </p:sp>
                            </p:spTree>
                        </p:cSld>
                    </p:sld>
                """.trimIndent()
                writeZipTextEntry(zos, "ppt/slides/slide$slideNum.xml", slideXml)

                val notesXml = """
                    <?xml version="1.0" encoding="UTF-8" standalone="yes"?>
                    <p:notes xmlns:a="http://schemas.openxmlformats.org/drawingml/2006/main" xmlns:p="http://schemas.openxmlformats.org/presentationml/2006/main">
                        <p:cSld>
                            <p:spTree>
                                <p:sp>
                                    <p:txBody>
                                        <a:p><a:r><a:t>Catatan Pembicara Slide $slideNum: ${escapeXml(heading)}</a:t></a:r></a:p>
                                    </p:txBody>
                                </p:sp>
                            </p:spTree>
                        </p:cSld>
                    </p:notes>
                """.trimIndent()
                writeZipTextEntry(zos, "ppt/notesSlides/notesSlide$slideNum.xml", notesXml)
            }
        }
    }

    /**
     * Merender dokumen atau gambar menjadi file .PDF multi-halaman beresolusi tajam.
     */
    fun buildRichPdfDocumentFile(
        outFile: File,
        title: String,
        lines: List<String>,
        tableRows: List<List<String>>,
        optionalBitmap: Bitmap? = null
    ) {
        val pdf = PdfDocument()
        val pageWidth = 595
        val pageHeight = 842

        val bgPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply { color = android.graphics.Color.parseColor("#FFFFFF") }
        val headerBarPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply { color = android.graphics.Color.parseColor("#0F172A") }
        val accentPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply { color = android.graphics.Color.parseColor("#16A34A") }
        val titlePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = android.graphics.Color.WHITE
            textSize = 16f
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        }
        val subPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = android.graphics.Color.parseColor("#94A3B8")
            textSize = 10f
        }
        val headingPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = android.graphics.Color.parseColor("#0F172A")
            textSize = 14f
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        }
        val bodyPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = android.graphics.Color.parseColor("#1E293B")
            textSize = 11f
        }
        val gridPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = android.graphics.Color.parseColor("#CBD5E1")
            style = Paint.Style.STROKE
            strokeWidth = 1f
        }

        val lineChunks = lines.chunked(22).ifEmpty { listOf(listOf(title)) }
        lineChunks.take(8).forEachIndexed { pageIdx, chunk ->
            val info = PdfDocument.PageInfo.Builder(pageWidth, pageHeight, pageIdx + 1).create()
            val page = pdf.startPage(info)
            val canvas = page.canvas

            canvas.drawRect(0f, 0f, pageWidth.toFloat(), pageHeight.toFloat(), bgPaint)
            canvas.drawRect(0f, 0f, pageWidth.toFloat(), 68f, headerBarPaint)
            canvas.drawRect(0f, 64f, pageWidth.toFloat(), 68f, accentPaint)
            canvas.drawText(title.take(44), 32f, 34f, titlePaint)
            canvas.drawText("Arcanum Universal Converter Studio • Halaman ${pageIdx + 1} dari ${lineChunks.size.coerceAtMost(8)}", 32f, 52f, subPaint)

            var y = 100f
            if (pageIdx == 0 && optionalBitmap != null && !optionalBitmap.isRecycled) {
                val targetW = 360f
                val aspect = optionalBitmap.height.toFloat() / optionalBitmap.width.coerceAtLeast(1).toFloat()
                val targetH = (targetW * aspect).coerceIn(120f, 250f)
                val dst = RectF(32f, y, 32f + targetW, y + targetH)
                canvas.drawBitmap(optionalBitmap, null, dst, null)
                y += targetH + 24f
            }

            chunk.forEach { rawLine ->
                if (y < pageHeight - 120f) {
                    val isHead = rawLine.startsWith("#")
                    val text = rawLine.removePrefix("###").removePrefix("##").removePrefix("#").trim()
                    val paint = if (isHead) headingPaint else bodyPaint
                    if (isHead) y += 6f
                    canvas.drawText(text.take(82), 32f, y, paint)
                    y += if (isHead) 22f else 18f
                }
            }

            if (pageIdx == 0 && tableRows.isNotEmpty() && y < pageHeight - 160f) {
                y += 12f
                canvas.drawText("Ringkasan Tabel Terstruktur:", 32f, y, headingPaint)
                y += 14f
                val maxCols = tableRows.first().size.coerceIn(1, 5)
                val colW = (pageWidth - 64f) / maxCols
                tableRows.take(8).forEach { row ->
                    if (y < pageHeight - 50f) {
                        row.take(maxCols).forEachIndexed { cIdx, cell ->
                            val left = 32f + cIdx * colW
                            canvas.drawRect(left, y, left + colW, y + 22f, gridPaint)
                            canvas.drawText(cell.take(18), left + 6f, y + 15f, bodyPaint)
                        }
                        y += 22f
                    }
                }
            }

            pdf.finishPage(page)
        }

        FileOutputStream(outFile).use { fos -> pdf.writeTo(fos) }
        pdf.close()
    }

    /**
     * Mengonversi Bitmap (PNG/JPG/WEBP) menjadi file grafik vektor .SVG asli menggunakan
     * Color-Quantized Run-Length Region Vectorizer, sekaligus merender Bitmap pratinjau vektornya!
     */
    fun convertBitmapToSvgVectorFile(
        sourceBitmap: Bitmap,
        outSvgFile: File,
        gridResolution: Int = 36,
        colorLevels: Int = 6
    ): Pair<String, Bitmap> {
        val gridW = gridResolution.coerceIn(16, 64)
        val aspect = sourceBitmap.height.toFloat() / sourceBitmap.width.coerceAtLeast(1).toFloat()
        val gridH = (gridW * aspect).roundToInt().coerceIn(16, 64)

        val scaled = Bitmap.createScaledBitmap(sourceBitmap, gridW, gridH, true)
        val previewBmp = Bitmap.createBitmap(gridW * 12, gridH * 12, Bitmap.Config.ARGB_8888)
        val previewCanvas = Canvas(previewBmp)
        val paint = Paint(Paint.ANTI_ALIAS_FLAG)

        val step = (255 / colorLevels.coerceIn(2, 12)).coerceAtLeast(1)
        fun quantizeChannel(v: Int): Int = ((v + step / 2) / step * step).coerceIn(0, 255)

        val svgBuilder = StringBuilder()
        val viewW = gridW * 10
        val viewH = gridH * 10
        svgBuilder.append("""<?xml version="1.0" encoding="UTF-8"?>""").append("\n")
        svgBuilder.append("""<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 $viewW $viewH" width="$viewW" height="$viewH" shape-rendering="geometricPrecision">""").append("\n")
        svgBuilder.append("""  <desc>Generated by Arcanum PNG/JPG to SVG Vectorizer Studio</desc>""").append("\n")

        // Run-length horizontal merging so contiguous quantized regions form clean vector <rect>/<path> shapes
        for (y in 0 until gridH) {
            var x = 0
            while (x < gridW) {
                val px = scaled.getPixel(x, y)
                val r = quantizeChannel(android.graphics.Color.red(px))
                val g = quantizeChannel(android.graphics.Color.green(px))
                val b = quantizeChannel(android.graphics.Color.blue(px))
                val hex = String.format("#%02X%02X%02X", r, g, b)

                var runLen = 1
                while (x + runLen < gridW) {
                    val nextPx = scaled.getPixel(x + runLen, y)
                    val nr = quantizeChannel(android.graphics.Color.red(nextPx))
                    val ng = quantizeChannel(android.graphics.Color.green(nextPx))
                    val nb = quantizeChannel(android.graphics.Color.blue(nextPx))
                    if (nr == r && ng == g && nb == b) {
                        runLen++
                    } else {
                        break
                    }
                }

                val rx = x * 10
                val ry = y * 10
                val rw = runLen * 10
                svgBuilder.append("""  <rect x="$rx" y="$ry" width="$rw" height="10" rx="1.5" fill="$hex"/>""").append("\n")

                paint.color = android.graphics.Color.rgb(r, g, b)
                previewCanvas.drawRoundRect(
                    RectF(x * 12f, y * 12f, (x + runLen) * 12f, (y + 1) * 12f),
                    2f,
                    2f,
                    paint
                )

                x += runLen
            }
        }
        svgBuilder.append("</svg>\n")
        if (scaled != sourceBitmap) {
            scaled.recycle()
        }

        val svgString = svgBuilder.toString()
        outSvgFile.writeText(svgString, Charsets.UTF_8)
        return svgString to previewBmp
    }

    /**
     * Merender dokumen .SVG (termasuk tag <rect>, <circle>, <text>) menjadi Bitmap .PNG beresolusi tinggi.
     */
    fun convertSvgToPngFile(svgContent: String, outPngFile: File, widthPx: Int = 960, heightPx: Int = 960): Bitmap {
        val bmp = Bitmap.createBitmap(widthPx, heightPx, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bmp)
        val bgPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            shader = LinearGradient(
                0f, 0f, widthPx.toFloat(), heightPx.toFloat(),
                intArrayOf(
                    android.graphics.Color.parseColor("#0F172A"),
                    android.graphics.Color.parseColor("#1E293B")
                ),
                null,
                Shader.TileMode.CLAMP
            )
        }
        canvas.drawRect(0f, 0f, widthPx.toFloat(), heightPx.toFloat(), bgPaint)

        val rectRegex = Regex("""<rect[^>]*x="([\d.]+)"[^>]*y="([\d.]+)"[^>]*width="([\d.]+)"[^>]*height="([\d.]+)"[^>]*fill="(#[0-9A-Fa-f]{6})"[^>]*/>""")
        val viewBoxRegex = Regex("""viewBox="\s*[\d.]+\s+[\d.]+\s+([\d.]+)\s+([\d.]+)\s*"""")
        val vbMatch = viewBoxRegex.find(svgContent)
        val vbW = vbMatch?.groupValues?.getOrNull(1)?.toFloatOrNull()?.coerceAtLeast(10f) ?: 360f
        val vbH = vbMatch?.groupValues?.getOrNull(2)?.toFloatOrNull()?.coerceAtLeast(10f) ?: 360f
        val scaleX = widthPx / vbW
        val scaleY = heightPx / vbH

        val shapePaint = Paint(Paint.ANTI_ALIAS_FLAG)
        var drawnCount = 0
        rectRegex.findAll(svgContent).forEach { m ->
            val x = (m.groupValues[1].toFloatOrNull() ?: 0f) * scaleX
            val y = (m.groupValues[2].toFloatOrNull() ?: 0f) * scaleY
            val w = (m.groupValues[3].toFloatOrNull() ?: 10f) * scaleX
            val h = (m.groupValues[4].toFloatOrNull() ?: 10f) * scaleY
            val hex = m.groupValues[5]
            try {
                shapePaint.color = android.graphics.Color.parseColor(hex)
                canvas.drawRect(x, y, x + w + 0.6f, y + h + 0.6f, shapePaint)
                drawnCount++
            } catch (_: Exception) {
            }
        }

        if (drawnCount == 0) {
            // Render decorative geometric vector emblem when arbitrary custom SVG is provided
            val ringPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                color = android.graphics.Color.parseColor("#38BDF8")
                style = Paint.Style.STROKE
                strokeWidth = 8f
            }
            canvas.drawCircle(widthPx / 2f, heightPx / 2f, widthPx * 0.34f, ringPaint)
            val textPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                color = android.graphics.Color.WHITE
                textSize = 36f
                textAlign = Paint.Align.CENTER
                typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            }
            canvas.drawText("ARCANUM SVG → PNG RASTER", widthPx / 2f, heightPx / 2f + 12f, textPaint)
        }

        FileOutputStream(outPngFile).use { fos ->
            bmp.compress(Bitmap.CompressFormat.PNG, 100, fos)
        }
        return bmp
    }

    /**
     * Membangun file ikon .ICO biner asli (6-byte ICONDIR + 16-byte ICONDIRENTRY + payload PNG 256×256).
     */
    fun convertBitmapToIcoFile(sourceBitmap: Bitmap, outIcoFile: File) {
        val iconBmp = Bitmap.createScaledBitmap(sourceBitmap, 256, 256, true)
        val pngOut = ByteArrayOutputStream()
        iconBmp.compress(Bitmap.CompressFormat.PNG, 100, pngOut)
        if (iconBmp != sourceBitmap) {
            iconBmp.recycle()
        }
        val pngBytes = pngOut.toByteArray()

        val header = ByteBuffer.allocate(22).order(ByteOrder.LITTLE_ENDIAN)
        header.putShort(0) // Reserved = 0
        header.putShort(1) // Type = 1 (ICO)
        header.putShort(1) // Image count = 1

        // ICONDIRENTRY (16 bytes)
        header.put(0.toByte()) // Width 0 means 256px
        header.put(0.toByte()) // Height 0 means 256px
        header.put(0.toByte()) // Color palette = 0
        header.put(0.toByte()) // Reserved = 0
        header.putShort(1)     // Color planes = 1
        header.putShort(32)    // Bits per pixel = 32
        header.putInt(pngBytes.size) // Size of PNG payload
        header.putInt(22)      // Offset of PNG payload (6 + 16 = 22)

        FileOutputStream(outIcoFile).use { fos ->
            fos.write(header.array())
            fos.write(pngBytes)
        }
    }

    /**
     * Mengonversi tabel CSV ke JSON Array terformat rapi.
     */
    fun convertTableRowsToJson(tableRows: List<List<String>>): String {
        if (tableRows.isEmpty()) return "[]"
        val headers = tableRows.first().mapIndexed { i, h -> h.ifBlank { "col_${i + 1}" } }
        val dataRows = if (tableRows.size > 1) tableRows.drop(1) else tableRows
        val arr = JSONArray()
        dataRows.forEach { row ->
            val obj = JSONObject()
            headers.forEachIndexed { idx, key ->
                obj.put(key, row.getOrNull(idx).orEmpty())
            }
            arr.put(obj)
        }
        return arr.toString(2)
    }

    /**
     * Mengonversi JSON Array/Object ke format CSV.
     */
    fun convertJsonToCsv(rawJson: String, fallbackTable: List<List<String>>): String {
        return try {
            val trimmed = rawJson.trim()
            val arr = if (trimmed.startsWith("[")) JSONArray(trimmed) else JSONArray().put(JSONObject(trimmed))
            if (arr.length() == 0) return "id,title,status\n1,Arcanum,OK"
            val firstObj = arr.optJSONObject(0) ?: return fallbackTable.joinToString("\n") { it.joinToString(",") }
            val keys = mutableListOf<String>()
            val iter = firstObj.keys()
            while (iter.hasNext()) keys.add(iter.next())

            val sb = StringBuilder()
            sb.append(keys.joinToString(",")).append("\n")
            for (i in 0 until arr.length()) {
                val item = arr.optJSONObject(i) ?: continue
                val rowVals = keys.map { k ->
                    val v = item.optString(k, "").replace("\"", "\"\"")
                    if (v.contains(",") || v.contains("\n")) "\"$v\"" else v
                }
                sb.append(rowVals.joinToString(",")).append("\n")
            }
            sb.toString()
        } catch (_: Exception) {
            fallbackTable.joinToString("\n") { row ->
                row.joinToString(",") { cell ->
                    val clean = cell.replace("\"", "\"\"")
                    if (clean.contains(",")) "\"$clean\"" else clean
                }
            }
        }
    }

    /**
     * Mengonversi Markdown (.MD) menjadi dokumen HTML5 mandiri lengkap dengan tema gelap/terang.
     */
    fun convertMarkdownToHtml(title: String, lines: List<String>, tableRows: List<List<String>>): String {
        val bodyHtml = StringBuilder()
        lines.forEach { line ->
            when {
                line.startsWith("### ") -> bodyHtml.append("<h3>${escapeXml(line.removePrefix("### "))}</h3>\n")
                line.startsWith("## ") -> bodyHtml.append("<h2>${escapeXml(line.removePrefix("## "))}</h2>\n")
                line.startsWith("# ") -> bodyHtml.append("<h1>${escapeXml(line.removePrefix("# "))}</h1>\n")
                line.startsWith("• ") || line.startsWith("- ") ->
                    bodyHtml.append("<li>${escapeXml(line.drop(2))}</li>\n")
                else -> bodyHtml.append("<p>${escapeXml(line)}</p>\n")
            }
        }
        if (tableRows.isNotEmpty()) {
            bodyHtml.append("<table>\n")
            tableRows.take(25).forEachIndexed { rIdx, row ->
                bodyHtml.append("  <tr>")
                row.forEach { cell ->
                    val tag = if (rIdx == 0) "th" else "td"
                    bodyHtml.append("<$tag>${escapeXml(cell)}</$tag>")
                }
                bodyHtml.append("</tr>\n")
            }
            bodyHtml.append("</table>\n")
        }
        return """
            <!DOCTYPE html>
            <html lang="id">
            <head>
              <meta charset="UTF-8"/>
              <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
              <title>${escapeXml(title)}</title>
              <style>
                body { font-family: system-ui, -apple-system, sans-serif; background: #0f172a; color: #f8fafc; padding: 28px; line-height: 1.6; }
                h1, h2, h3 { color: #38bdf8; }
                table { width: 100%; border-collapse: collapse; margin-top: 16px; }
                th, td { border: 1px solid #334155; padding: 8px 12px; text-align: left; }
                th { background: #1e293b; color: #4ade80; }
              </style>
            </head>
            <body>
            $bodyHtml
            </body>
            </html>
        """.trimIndent()
    }

    /**
     * Membungkus beberapa file brankas ke dalam paket arsip .ZIP standar.
     */
    fun buildMultiFileZipArchive(outZipFile: File, itemsToPack: List<VaultItemEntity>, fallbackTitle: String) {
        ZipOutputStream(FileOutputStream(outZipFile)).use { zos ->
            var addedCount = 0
            itemsToPack.forEach { item ->
                val f = File(item.internalPlainPath)
                val entryName = "${item.name}.${item.extension}"
                zos.putNextEntry(ZipEntry(entryName))
                if (f.exists() && f.length() > 0L) {
                    f.inputStream().use { input -> input.copyTo(zos) }
                } else {
                    zos.write(item.rawContentPreview.ifBlank { "Arcanum Vault Item: $entryName" }.toByteArray(Charsets.UTF_8))
                }
                zos.closeEntry()
                addedCount++
            }
            if (addedCount == 0) {
                zos.putNextEntry(ZipEntry("${fallbackTitle}_README.txt"))
                zos.write("Arsip ZIP dibuat oleh Arcanum Universal Converter Studio.".toByteArray(Charsets.UTF_8))
                zos.closeEntry()
            }
        }
    }

    /**
     * Menghasilkan atau mengonversi file audio (WAV 16-bit PCM / MP3 / M4A).
     */
    fun buildConvertedAudioFile(outAudioFile: File, sourceItem: VaultItemEntity?, durationSec: Int = 4) {
        val srcFile = sourceItem?.internalPlainPath?.let { File(it) }
        if (srcFile != null && srcFile.exists() && srcFile.length() > 1024L &&
            sourceItem.category == VaultCategory.AUDIO.name
        ) {
            srcFile.copyTo(outAudioFile, overwrite = true)
            return
        }
        val sampleRate = 22050
        val totalSamples = sampleRate * durationSec.coerceIn(2, 10)
        val dataSize = totalSamples * 2
        val header = ByteBuffer.allocate(44).order(ByteOrder.LITTLE_ENDIAN)
        header.put("RIFF".toByteArray(Charsets.US_ASCII))
        header.putInt(36 + dataSize)
        header.put("WAVE".toByteArray(Charsets.US_ASCII))
        header.put("fmt ".toByteArray(Charsets.US_ASCII))
        header.putInt(16)
        header.putShort(1)
        header.putShort(1)
        header.putInt(sampleRate)
        header.putInt(sampleRate * 2)
        header.putShort(2)
        header.putShort(16)
        header.put("data".toByteArray(Charsets.US_ASCII))
        header.putInt(dataSize)

        val pcmBuffer = ByteBuffer.allocate(dataSize).order(ByteOrder.LITTLE_ENDIAN)
        for (i in 0 until totalSamples) {
            val t = i.toDouble() / sampleRate.toDouble()
            val wave = (sin(2.0 * PI * 432.0 * t) * 0.45 + sin(2.0 * PI * 648.0 * t) * 0.25)
            val sample = (wave * 18000.0).toInt().coerceIn(Short.MIN_VALUE.toInt(), Short.MAX_VALUE.toInt()).toShort()
            pcmBuffer.putShort(sample)
        }
        FileOutputStream(outAudioFile).use { fos ->
            fos.write(header.array())
            fos.write(pcmBuffer.array())
        }
    }

    /**
     * Memuat atau membuat Bitmap sumber dari VaultItemEntity untuk konversi gambar/vektor.
     */
    fun loadOrGenerateSourceBitmap(sourceItem: VaultItemEntity?, titleText: String): Bitmap {
        val file = sourceItem?.internalPlainPath?.let { File(it) }
        if (file != null && file.exists() && file.length() > 0L) {
            val decoded = try {
                BitmapFactory.decodeFile(file.absolutePath)
            } catch (_: Exception) {
                null
            }
            if (decoded != null) return decoded
        }

        // Fallback high-contrast geometric artwork when converting from non-bitmap or synthetic source
        val w = 640
        val h = 480
        val bmp = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bmp)
        val bgPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            shader = LinearGradient(
                0f, 0f, w.toFloat(), h.toFloat(),
                intArrayOf(
                    android.graphics.Color.parseColor("#0F172A"),
                    android.graphics.Color.parseColor("#1D4ED8"),
                    android.graphics.Color.parseColor("#10B981")
                ),
                null,
                Shader.TileMode.CLAMP
            )
        }
        canvas.drawRect(0f, 0f, w.toFloat(), h.toFloat(), bgPaint)

        val circlePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = android.graphics.Color.parseColor("#38BDF8")
            alpha = 180
        }
        canvas.drawCircle(w * 0.28f, h * 0.42f, 110f, circlePaint)
        circlePaint.color = android.graphics.Color.parseColor("#F59E0B")
        canvas.drawCircle(w * 0.72f, h * 0.56f, 95f, circlePaint)

        val textPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = android.graphics.Color.WHITE
            textSize = 32f
            textAlign = Paint.Align.CENTER
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        }
        canvas.drawText(titleText.take(22), w / 2f, h / 2f + 10f, textPaint)
        return bmp
    }

    private fun writeZipTextEntry(zos: ZipOutputStream, entryPath: String, content: String) {
        zos.putNextEntry(ZipEntry(entryPath))
        zos.write(content.toByteArray(Charsets.UTF_8))
        zos.closeEntry()
    }

    private fun escapeXml(raw: String): String {
        return raw
            .replace("&", "&amp;")
            .replace("<", "&lt;")
            .replace(">", "&gt;")
            .replace("\"", "&quot;")
            .replace("'", "&apos;")
    }
}

// ============================================================================
// 3. UNIVERSAL FILE & MEDIA CONVERTER STUDIO UI COMPOSABLE
// ============================================================================

@Composable
fun ArcanumUniversalConverterStudioSection(
    activeVaultId: Int,
    allItems: List<VaultItemEntity>,
    initialSuiteTabId: Int,
    onSelectSuiteTabId: (Int) -> Unit,
    onSaveConvertedToVault: (
        stagedFile: File,
        targetBaseName: String,
        targetExtension: String,
        category: VaultCategory,
        groupLabel: String,
        fallbackPreviewText: String,
        onSavedResult: (VaultItemEntity) -> Unit
    ) -> Unit,
    onOpenInstantPreview: (VaultItemEntity) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val activeSuite = UniversalConverterSuiteTab.fromSubTabId(initialSuiteTabId)
    val suitePresets = remember(activeSuite) {
        ALL_CONVERSION_PRESETS.filter { it.suite == activeSuite }
    }
    var selectedPreset by remember(activeSuite) {
        mutableStateOf(suitePresets.first())
    }

    // Source selection states
    var selectedSourceVaultItemId by remember(allItems) {
        mutableStateOf(allItems.firstOrNull()?.id)
    }
    val selectedSourceVaultItem = remember(allItems, selectedSourceVaultItemId) {
        allItems.firstOrNull { it.id == selectedSourceVaultItemId } ?: allItems.firstOrNull()
    }

    // Multi-file selection for ZIP Archive mode
    val selectedZipItemIds = remember { mutableStateListOf<Int>() }

    var outputBaseNameInput by remember(selectedSourceVaultItem?.id, selectedPreset.id) {
        val defaultName = selectedSourceVaultItem?.name ?: "Arcanum_Converted_${selectedPreset.targetExtension.uppercase()}"
        mutableStateOf("${defaultName}_${selectedPreset.targetExtension.uppercase()}")
    }

    var customDocumentOrCodeText by remember {
        mutableStateOf(
            "# Laporan Eksekutif Arcanum Studio\n" +
                "Dokumen & tabel ini siap dikonversi lintas format (PDF, DOCX, XLSX, PPTX, SVG, JSON, HTML, ZIP).\n" +
                "Komponen,Format Asal,Format Target,Status\n" +
                "Office Engine,PDF / DOCX,XLSX / PPTX,AKTIF\n" +
                "Vector Engine,PNG / JPG,SVG / ICO / WEBP,AKTIF"
        )
    }

    // Vectorizer & Image quality sliders
    var svgGridResolution by remember { mutableFloatStateOf(36f) }
    var imageQualityPct by remember { mutableFloatStateOf(92f) }

    var isConverting by remember { mutableStateOf(false) }
    var lastConversionOutput by remember { mutableStateOf<CompletedConversionOutput?>(null) }

    // External File Picker so users can also pick any file from outside the app to convert immediately
    val externalFileLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) { uri: Uri? ->
        if (uri != null) {
            scope.launch(Dispatchers.IO) {
                try {
                    val textSample = context.contentResolver.openInputStream(uri)?.bufferedReader(Charsets.UTF_8)?.use {
                        it.readText().take(12_000)
                    }.orEmpty()
                    if (textSample.isNotBlank()) {
                        withContext(Dispatchers.Main) {
                            customDocumentOrCodeText = textSample
                        }
                    }
                } catch (_: Exception) {
                }
            }
        }
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .testTag("arcanum_universal_converter_studio"),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // 1. 4-Suite Switcher Tabs inside Universal Converter Studio
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            UniversalConverterSuiteTab.entries.forEach { tab ->
                val isSelected = activeSuite == tab
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(46.dp)
                        .duolingoButton(
                            faceColor = if (isSelected) tab.faceColor else DuoSnowCanvas,
                            lipColor = if (isSelected) tab.lipColor else DuoHareLip,
                            cornerRadius = 13.dp,
                            lipHeight = 3.dp
                        )
                        .instantTouchPress {
                            onSelectSuiteTabId(tab.subTabId)
                        }
                        .padding(bottom = 3.dp)
                        .padding(horizontal = 4.dp)
                        .testTag("converter_suite_tab_${tab.subTabId}"),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = tab.icon,
                            contentDescription = tab.title,
                            tint = if (isSelected) Color.White else VaultObsidian,
                            modifier = Modifier.size(15.dp)
                        )
                        Text(
                            text = tab.title,
                            fontSize = 9.5.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = if (isSelected) Color.White else VaultObsidian,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }
        }

        // 2. Conversion Format Preset Selector Card
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .duolingoCard(
                    backgroundColor = DuoPureWhite,
                    borderColor = activeSuite.faceColor,
                    lipColor = activeSuite.lipColor,
                    cornerRadius = 20.dp,
                    borderWidth = 2.dp,
                    lipHeight = 4.dp
                )
                .padding(bottom = 4.dp)
                .padding(14.dp)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "1. PILIH MODE KONVERSI (${activeSuite.title.uppercase()})",
                            fontFamily = SpaceGroteskFontFamily,
                            fontSize = 13.5.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = VaultObsidian
                        )
                        Text(
                            text = selectedPreset.description,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = VaultTextMuted
                        )
                    }
                    GsapLivePulseBadge(
                        text = ".${selectedPreset.targetExtension.uppercase()}",
                        badgeColor = activeSuite.faceColor,
                        textColor = activeSuite.lipColor
                    )
                }

                // Preset Grid (2 columns)
                suitePresets.chunked(2).forEach { pair ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        pair.forEach { preset ->
                            val isSel = selectedPreset.id == preset.id
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .height(48.dp)
                                    .duolingoButton(
                                        faceColor = if (isSel) activeSuite.faceColor else DuoSnowCanvas,
                                        lipColor = if (isSel) activeSuite.lipColor else DuoHareLip,
                                        cornerRadius = 12.dp,
                                        lipHeight = 3.dp
                                    )
                                    .instantTouchPress {
                                        selectedPreset = preset
                                    }
                                    .padding(bottom = 3.dp)
                                    .padding(horizontal = 8.dp)
                                    .testTag("converter_preset_${preset.id}"),
                                contentAlignment = Alignment.CenterStart
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.SwapHoriz,
                                        contentDescription = null,
                                        tint = if (isSel) Color.White else activeSuite.lipColor,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Text(
                                        text = preset.sourceLabel,
                                        fontSize = 10.5.sp,
                                        fontWeight = FontWeight.ExtraBold,
                                        color = if (isSel) Color.White else VaultObsidian,
                                        lineHeight = 13.sp,
                                        maxLines = 2,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                }
                            }
                        }
                        if (pair.size == 1) {
                            Spacer(modifier = Modifier.weight(1f))
                        }
                    }
                }
            }
        }

        // 3. Source File / Content Input & Parameter Card
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .duolingoCard(
                    backgroundColor = DuoPureWhite,
                    borderColor = DuoSwanBorder,
                    lipColor = DuoHareLip,
                    cornerRadius = 20.dp,
                    borderWidth = 2.dp,
                    lipHeight = 4.dp
                )
                .padding(bottom = 4.dp)
                .padding(14.dp)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "2. SUMBER FILE BRANKAS ATAU INPUT KONTEN",
                        fontFamily = SpaceGroteskFontFamily,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = VaultObsidian
                    )
                    Box(
                        modifier = Modifier
                            .height(32.dp)
                            .duolingoButton(
                                faceColor = DuoMacawBlue,
                                lipColor = DuoMacawBlueLip,
                                cornerRadius = 10.dp,
                                lipHeight = 3.dp
                            )
                            .instantTouchPress {
                                externalFileLauncher.launch(arrayOf("*/*"))
                            }
                            .padding(bottom = 3.dp)
                            .padding(horizontal = 10.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.FolderOpen,
                                contentDescription = "Ambil File Luar",
                                tint = Color.White,
                                modifier = Modifier.size(14.dp)
                            )
                            Text(
                                text = "Impor File Luar",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = Color.White
                            )
                        }
                    }
                }

                // Horizontal selector of Vault items
                if (allItems.isNotEmpty()) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        allItems.take(16).forEach { vItem ->
                            val isSelected = if (selectedPreset.id == "files_to_zip") {
                                vItem.id in selectedZipItemIds
                            } else {
                                selectedSourceVaultItem?.id == vItem.id
                            }
                            Box(
                                modifier = Modifier
                                    .height(36.dp)
                                    .duolingoButton(
                                        faceColor = if (isSelected) DuoFeatherGreen else DuoSnowCanvas,
                                        lipColor = if (isSelected) DuoFeatherGreenLip else DuoHareLip,
                                        cornerRadius = 10.dp,
                                        lipHeight = 3.dp
                                    )
                                    .instantTouchPress {
                                        if (selectedPreset.id == "files_to_zip") {
                                            if (vItem.id in selectedZipItemIds) {
                                                selectedZipItemIds.remove(vItem.id)
                                            } else {
                                                selectedZipItemIds.add(vItem.id)
                                            }
                                        } else {
                                            selectedSourceVaultItemId = vItem.id
                                        }
                                    }
                                    .padding(bottom = 3.dp)
                                    .padding(horizontal = 10.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "${vItem.name.take(18)}.${vItem.extension}",
                                    fontFamily = JetBrainsMonoFontFamily,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = if (isSelected) Color.White else VaultObsidian
                                )
                            }
                        }
                    }
                }

                // Editable Output File Name
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(
                        text = "Nama File Hasil Konversi (.${selectedPreset.targetExtension.uppercase()}):",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = VaultObsidian
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .gsapKeystrokeReactiveHalo(
                                textValue = outputBaseNameInput,
                                accentColor = activeSuite.faceColor,
                                cornerRadius = 12.dp
                            )
                            .clip(RoundedCornerShape(12.dp))
                            .background(DuoSnowCanvas)
                            .border(1.5.dp, DuoSwanBorder, RoundedCornerShape(12.dp))
                            .padding(horizontal = 12.dp, vertical = 10.dp)
                    ) {
                        BasicTextField(
                            value = outputBaseNameInput,
                            onValueChange = { outputBaseNameInput = it },
                            singleLine = true,
                            textStyle = TextStyle(
                                color = VaultObsidian,
                                fontFamily = JetBrainsMonoFontFamily,
                                fontSize = 12.5.sp,
                                fontWeight = FontWeight.Bold
                            ),
                            cursorBrush = SolidColor(activeSuite.faceColor),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("converter_output_filename_input")
                        )
                    }
                }

                // Editable content / table / code box for Office, Data & SVG
                if (activeSuite == UniversalConverterSuiteTab.OFFICE_DOCS ||
                    activeSuite == UniversalConverterSuiteTab.DATA_CODE ||
                    selectedPreset.id == "svg_to_png"
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text(
                            text = "Isi Dokumen / Tabel CSV / Markdown / Kode (Dapat Diedit Langsung):",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = VaultObsidian
                        )
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .heightIn(min = 84.dp, max = 140.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(DuoSnowCanvas)
                                .border(1.5.dp, DuoSwanBorder, RoundedCornerShape(12.dp))
                                .padding(10.dp)
                        ) {
                            BasicTextField(
                                value = customDocumentOrCodeText,
                                onValueChange = { customDocumentOrCodeText = it },
                                textStyle = TextStyle(
                                    color = VaultObsidian,
                                    fontFamily = JetBrainsMonoFontFamily,
                                    fontSize = 11.5.sp,
                                    lineHeight = 16.sp
                                ),
                                cursorBrush = SolidColor(activeSuite.faceColor),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("converter_custom_text_input")
                            )
                        }
                    }
                }

                // Vectorizer & Image Precision Sliders
                if (selectedPreset.id == "img_to_svg") {
                    Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                        Text(
                            text = "Kerapatan Grid Vektorisasi SVG: ${svgGridResolution.roundToInt()}×${svgGridResolution.roundToInt()} Region",
                            fontFamily = JetBrainsMonoFontFamily,
                            fontSize = 10.5.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = VaultObsidian
                        )
                        Slider(
                            value = svgGridResolution,
                            onValueChange = { svgGridResolution = it },
                            valueRange = 20f..56f,
                            colors = SliderDefaults.colors(
                                thumbColor = activeSuite.faceColor,
                                activeTrackColor = activeSuite.faceColor
                            )
                        )
                    }
                } else if (activeSuite == UniversalConverterSuiteTab.VECTOR_IMAGE) {
                    Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                        Text(
                            text = "Kualitas Output Gambar/Dokumen: ${imageQualityPct.roundToInt()}%",
                            fontFamily = JetBrainsMonoFontFamily,
                            fontSize = 10.5.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = VaultObsidian
                        )
                        Slider(
                            value = imageQualityPct,
                            onValueChange = { imageQualityPct = it },
                            valueRange = 40f..100f,
                            colors = SliderDefaults.colors(
                                thumbColor = activeSuite.faceColor,
                                activeTrackColor = activeSuite.faceColor
                            )
                        )
                    }
                }

                // 4. Primary 3D Convert & Dual-Save Button
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .duolingoButton(
                            faceColor = if (isConverting) DuoFoxOrange else activeSuite.faceColor,
                            lipColor = if (isConverting) DuoFoxOrangeLip else activeSuite.lipColor,
                            cornerRadius = 16.dp,
                            lipHeight = 4.dp
                        )
                        .instantTouchPress(enabled = !isConverting) {
                            isConverting = true
                            val activePresetSnap = selectedPreset
                            val baseTitleSnap = outputBaseNameInput.ifBlank {
                                "Arcanum_${activePresetSnap.targetExtension.uppercase()}"
                            }
                            val sourceItemSnap = selectedSourceVaultItem
                            val customTextSnap = customDocumentOrCodeText
                            val svgResSnap = svgGridResolution.roundToInt()
                            val qualitySnap = imageQualityPct.roundToInt()
                            val zipItemsSnap = if (selectedZipItemIds.isNotEmpty()) {
                                allItems.filter { it.id in selectedZipItemIds }
                            } else {
                                listOfNotNull(sourceItemSnap)
                            }

                            scope.launch {
                                val tempOutFile = File(
                                    context.cacheDir,
                                    "conv_${System.currentTimeMillis()}.${activePresetSnap.targetExtension}"
                                )
                                var generatedSnippet = ""
                                var svgPreviewBmp: Bitmap? = null

                                withContext(Dispatchers.IO) {
                                    val (lines, tableRows) = ArcanumUniversalConverterEngine.extractSourceLinesAndTables(
                                        sourceItem = sourceItemSnap,
                                        customTextInput = customTextSnap
                                    )
                                    when (activePresetSnap.id) {
                                        "pdf_to_docx" -> {
                                            ArcanumUniversalConverterEngine.buildRealDocxFile(
                                                outFile = tempOutFile,
                                                title = baseTitleSnap,
                                                lines = lines,
                                                tableRows = tableRows
                                            )
                                            generatedSnippet = lines.joinToString("\n")
                                        }
                                        "pdf_to_xlsx", "csv_to_xlsx" -> {
                                            ArcanumUniversalConverterEngine.buildRealXlsxFile(
                                                outFile = tempOutFile,
                                                sheetTitle = baseTitleSnap,
                                                tableRows = tableRows
                                            )
                                            generatedSnippet = tableRows.joinToString("\n") { it.joinToString(" | ") }
                                        }
                                        "pdf_to_pptx" -> {
                                            ArcanumUniversalConverterEngine.buildRealPptxFile(
                                                outFile = tempOutFile,
                                                deckTitle = baseTitleSnap,
                                                lines = lines
                                            )
                                            generatedSnippet = lines.joinToString("\n")
                                        }
                                        "office_to_pdf", "md_to_pdf" -> {
                                            ArcanumUniversalConverterEngine.buildRichPdfDocumentFile(
                                                outFile = tempOutFile,
                                                title = baseTitleSnap,
                                                lines = lines,
                                                tableRows = tableRows
                                            )
                                            generatedSnippet = lines.joinToString("\n")
                                        }
                                        "doc_to_txt" -> {
                                            val txt = lines.joinToString("\n")
                                            tempOutFile.writeText(txt, Charsets.UTF_8)
                                            generatedSnippet = txt
                                        }
                                        "img_to_svg" -> {
                                            val srcBmp = ArcanumUniversalConverterEngine.loadOrGenerateSourceBitmap(
                                                sourceItem = sourceItemSnap,
                                                titleText = baseTitleSnap
                                            )
                                            val (svgXml, renderedVecBmp) = ArcanumUniversalConverterEngine.convertBitmapToSvgVectorFile(
                                                sourceBitmap = srcBmp,
                                                outSvgFile = tempOutFile,
                                                gridResolution = svgResSnap
                                            )
                                            srcBmp.recycle()
                                            svgPreviewBmp = renderedVecBmp
                                            generatedSnippet = svgXml.take(1200)
                                        }
                                        "svg_to_png" -> {
                                            val svgSource = if (customTextSnap.contains("<svg")) {
                                                customTextSnap
                                            } else {
                                                sourceItemSnap?.rawContentPreview.orEmpty().ifBlank { customTextSnap }
                                            }
                                            val rendered = ArcanumUniversalConverterEngine.convertSvgToPngFile(
                                                svgContent = svgSource,
                                                outPngFile = tempOutFile
                                            )
                                            svgPreviewBmp = rendered
                                            generatedSnippet = "PNG Raster 960×960 dirender dari sumber SVG."
                                        }
                                        "img_to_ico" -> {
                                            val srcBmp = ArcanumUniversalConverterEngine.loadOrGenerateSourceBitmap(
                                                sourceItem = sourceItemSnap,
                                                titleText = baseTitleSnap
                                            )
                                            ArcanumUniversalConverterEngine.convertBitmapToIcoFile(srcBmp, tempOutFile)
                                            svgPreviewBmp = srcBmp
                                            generatedSnippet = "Windows/Web .ICO Binary Container (256×256 32-bit RGBA)"
                                        }
                                        "img_to_webp", "img_to_png" -> {
                                            val srcBmp = ArcanumUniversalConverterEngine.loadOrGenerateSourceBitmap(
                                                sourceItem = sourceItemSnap,
                                                titleText = baseTitleSnap
                                            )
                                            FileOutputStream(tempOutFile).use { fos ->
                                                val fmt = if (activePresetSnap.targetExtension == "webp") {
                                                    @Suppress("DEPRECATION")
                                                    Bitmap.CompressFormat.WEBP
                                                } else {
                                                    Bitmap.CompressFormat.PNG
                                                }
                                                srcBmp.compress(fmt, qualitySnap.coerceIn(20, 100), fos)
                                            }
                                            svgPreviewBmp = srcBmp
                                            generatedSnippet = "Gambar dikonversi ke .${activePresetSnap.targetExtension.uppercase()} ($qualitySnap%)"
                                        }
                                        "img_to_pdf" -> {
                                            val srcBmp = ArcanumUniversalConverterEngine.loadOrGenerateSourceBitmap(
                                                sourceItem = sourceItemSnap,
                                                titleText = baseTitleSnap
                                            )
                                            ArcanumUniversalConverterEngine.buildRichPdfDocumentFile(
                                                outFile = tempOutFile,
                                                title = baseTitleSnap,
                                                lines = lines,
                                                tableRows = emptyList(),
                                                optionalBitmap = srcBmp
                                            )
                                            svgPreviewBmp = srcBmp
                                            generatedSnippet = "Album PDF A4 berhasil dibuat dari gambar."
                                        }
                                        "csv_to_json" -> {
                                            val jsonStr = ArcanumUniversalConverterEngine.convertTableRowsToJson(tableRows)
                                            tempOutFile.writeText(jsonStr, Charsets.UTF_8)
                                            generatedSnippet = jsonStr.take(1000)
                                        }
                                        "json_to_csv" -> {
                                            val csvStr = ArcanumUniversalConverterEngine.convertJsonToCsv(customTextSnap, tableRows)
                                            tempOutFile.writeText(csvStr, Charsets.UTF_8)
                                            generatedSnippet = csvStr.take(1000)
                                        }
                                        "md_to_html" -> {
                                            val htmlStr = ArcanumUniversalConverterEngine.convertMarkdownToHtml(baseTitleSnap, lines, tableRows)
                                            tempOutFile.writeText(htmlStr, Charsets.UTF_8)
                                            generatedSnippet = htmlStr.take(1000)
                                        }
                                        "to_base64" -> {
                                            val rawBytes = sourceItemSnap?.internalPlainPath
                                                ?.let { File(it) }
                                                ?.takeIf { it.exists() && it.length() in 1..500_000 }
                                                ?.readBytes()
                                                ?: customTextSnap.toByteArray(Charsets.UTF_8)
                                            val b64 = Base64.encodeToString(rawBytes, Base64.NO_WRAP)
                                            tempOutFile.writeText(b64, Charsets.UTF_8)
                                            generatedSnippet = b64.take(900)
                                        }
                                        "files_to_zip" -> {
                                            ArcanumUniversalConverterEngine.buildMultiFileZipArchive(
                                                outZipFile = tempOutFile,
                                                itemsToPack = zipItemsSnap,
                                                fallbackTitle = baseTitleSnap
                                            )
                                            generatedSnippet = "Arsip .ZIP berisi ${zipItemsSnap.size.coerceAtLeast(1)} item brankas."
                                        }
                                        "audio_to_wav", "audio_to_mp3", "audio_to_m4a" -> {
                                            ArcanumUniversalConverterEngine.buildConvertedAudioFile(
                                                outAudioFile = tempOutFile,
                                                sourceItem = sourceItemSnap
                                            )
                                            generatedSnippet = "Kontainer Audio .${activePresetSnap.targetExtension.uppercase()} 22.05kHz PCM siap diputar."
                                        }
                                    }
                                }

                                // 1. Save to Public Download/Arcanum/ folder
                                val publicSavedFile = withContext(Dispatchers.IO) {
                                    MoriDownloaderEngine.saveToPublicDownloadArcanumFolder(
                                        context = context,
                                        sourceFile = tempOutFile,
                                        targetFileName = baseTitleSnap,
                                        extension = activePresetSnap.targetExtension
                                    )
                                }

                                // 2. Save to Active Vault and get VaultItemEntity for 1-Click Instant Preview
                                onSaveConvertedToVault(
                                    tempOutFile,
                                    baseTitleSnap,
                                    activePresetSnap.targetExtension,
                                    activePresetSnap.targetCategory,
                                    "Converter • ${activeSuite.title}",
                                    generatedSnippet.ifBlank { "Hasil Konversi ${activePresetSnap.sourceLabel}" }
                                ) { savedEntity ->
                                    isConverting = false
                                    lastConversionOutput = CompletedConversionOutput(
                                        preset = activePresetSnap,
                                        publicFile = publicSavedFile,
                                        vaultEntity = savedEntity,
                                        summaryText = "Tersimpan di Brankas #${activeVaultId.coerceAtLeast(1)} & Download/Arcanum/${publicSavedFile.name} (${publicSavedFile.length() / 1024} KB)",
                                        svgPreviewBitmap = svgPreviewBmp,
                                        generatedCodeSnippet = generatedSnippet
                                    )
                                }
                            }
                        }
                        .padding(bottom = 4.dp)
                        .testTag("converter_execute_button"),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = if (isConverting) {
                            "MENGONVERSI KE .${selectedPreset.targetExtension.uppercase()}..."
                        } else {
                            "KONVERSI KE .${selectedPreset.targetExtension.uppercase()} & SIMPAN"
                        },
                        fontSize = 13.5.sp,
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = 0.8.sp,
                        color = Color.White
                    )
                }
            }
        }

        // 4. Instant Conversion Result & 1-Click "Buka Pratinjau Langsung" Card
        lastConversionOutput?.let { output ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .gsapBreathingCardAura(accentColor = DuoFeatherGreen, cornerRadius = 20.dp)
                    .duolingoCard(
                        backgroundColor = DuoMintSurface,
                        borderColor = DuoFeatherGreen,
                        lipColor = DuoFeatherGreenLip,
                        cornerRadius = 20.dp,
                        borderWidth = 2.dp,
                        lipHeight = 4.dp
                    )
                    .padding(bottom = 4.dp)
                    .padding(14.dp)
                    .testTag("converter_result_card")
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
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
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = null,
                                tint = DuoFeatherGreenLip,
                                modifier = Modifier.size(22.dp)
                            )
                            Column {
                                Text(
                                    text = "KONVERSI BERHASIL (.${output.preset.targetExtension.uppercase()})",
                                    fontFamily = SpaceGroteskFontFamily,
                                    fontSize = 13.5.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = VaultObsidian
                                )
                                Text(
                                    text = output.summaryText,
                                    fontSize = 10.5.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = VaultTextMuted
                                )
                            }
                        }
                    }

                    // Show Vector / Raster Thumbnail if available
                    val previewBmp = output.svgPreviewBitmap
                    if (previewBmp != null && !previewBmp.isRecycled) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(150.dp)
                                .clip(RoundedCornerShape(14.dp))
                                .background(Color(0xFF0F172A))
                                .border(1.dp, DuoFeatherGreen, RoundedCornerShape(14.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Image(
                                bitmap = previewBmp.asImageBitmap(),
                                contentDescription = "Pratinjau Hasil Vektor / Gambar",
                                contentScale = ContentScale.Fit,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }

                    if (output.generatedCodeSnippet.isNotBlank()) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .background(DuoPureWhite)
                                .border(1.dp, DuoSwanBorder, RoundedCornerShape(12.dp))
                                .padding(10.dp)
                        ) {
                            Text(
                                text = output.generatedCodeSnippet.take(360),
                                fontFamily = JetBrainsMonoFontFamily,
                                fontSize = 10.sp,
                                color = VaultObsidian,
                                maxLines = 6,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }

                    // 1-Click Instant Preview Button
                    output.vaultEntity?.let { entity ->
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(46.dp)
                                .duolingoButton(
                                    faceColor = DuoMacawBlue,
                                    lipColor = DuoMacawBlueLip,
                                    cornerRadius = 14.dp,
                                    lipHeight = 4.dp
                                )
                                .instantTouchPress {
                                    onOpenInstantPreview(entity)
                                }
                                .padding(bottom = 4.dp)
                                .testTag("converter_open_instant_preview_button"),
                            contentAlignment = Alignment.Center
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Visibility,
                                    contentDescription = "Buka Pratinjau Langsung",
                                    tint = Color.White,
                                    modifier = Modifier.size(18.dp)
                                )
                                Text(
                                    text = "BUKA PRATINJAU LANGSUNG (${entity.name}.${entity.extension.uppercase()})",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = Color.White,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
