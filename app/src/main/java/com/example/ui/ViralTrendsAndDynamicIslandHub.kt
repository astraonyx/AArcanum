package com.example.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.draw.shadow
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AttachFile
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Casino
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.CloudDownload
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.FilterFrames
import androidx.compose.material.icons.filled.HourglassBottom
import androidx.compose.material.icons.filled.Label
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.LockOpen
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.SportsEsports
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.TouchApp
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.foundation.shape.GenericShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.material.icons.filled.Apps
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Timer
import com.example.ui.theme.DynamicIslandLayoutMode
import com.example.ui.theme.DynamicIslandShapeStyle
import kotlinx.coroutines.launch
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.data.VaultItemEntity
import com.example.ui.theme.AppThemeController
import com.example.ui.theme.CosmicTextPure
import com.example.ui.theme.CosmicTextSubtle
import com.example.ui.theme.DuoBeeYellow
import com.example.ui.theme.DuoBeeYellowLip
import com.example.ui.theme.DuoBeetlePurple
import com.example.ui.theme.DuoBeetlePurpleLip
import com.example.ui.theme.DuoCardinalRed
import com.example.ui.theme.DuoEelInk
import com.example.ui.theme.DuoFeatherGreen
import com.example.ui.theme.DuoFeatherGreenLip
import com.example.ui.theme.DuoFoxOrange
import com.example.ui.theme.DuoFoxOrangeLip
import com.example.ui.theme.DuoMacawBlue
import com.example.ui.theme.DuoMacawBlueLip
import com.example.ui.theme.DuoMintSurface
import com.example.ui.theme.DuoPureWhite
import com.example.ui.theme.DuoSwanBorder
import com.example.ui.theme.JetBrainsMonoFontFamily
import com.example.ui.theme.MascotActivityPose
import com.example.ui.theme.MascotCostumePreset
import com.example.ui.theme.duolingoButton
import com.example.ui.theme.duolingoCard
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.math.cos
import kotlin.math.sin

// ============================================================================
// 1. 3D HOLOGRAPHIC FOIL SHADER & INTERACTIVE TILT ENGINE (100% FREE UNLOCKED)
// ============================================================================

enum class HoloFoilType(
    val id: String,
    val displayName: String,
    val subtitle: String,
    val badgeColor: Color,
    val spectrumColors: List<Color>
) {
    PRISMATIC_RAINBOW(
        id = "PRISMATIC_RAINBOW",
        displayName = "Prismatic Rainbow",
        subtitle = "Kilau Pelangi TCG Pocket",
        badgeColor = Color(0xFFEC4899),
        spectrumColors = listOf(
            Color(0xFFFF007F),
            Color(0xFFFF8C00),
            Color(0xFFFFEA00),
            Color(0xFF00FF87),
            Color(0xFF00F2FE),
            Color(0xFF8B5CF6),
            Color(0xFFFF007F)
        )
    ),
    GOLD_STARLIGHT(
        id = "GOLD_STARLIGHT",
        displayName = "Gold Starlight",
        subtitle = "Foil Emas Bintang SSR",
        badgeColor = Color(0xFFF59E0B),
        spectrumColors = listOf(
            Color(0xFFFEF08A),
            Color(0xFFFACC15),
            Color(0xFFD97706),
            Color(0xFFFFFBEB),
            Color(0xFFF59E0B),
            Color(0xFFFEF08A)
        )
    ),
    CYBER_MATRIX(
        id = "CYBER_MATRIX",
        displayName = "Cyber Matrix",
        subtitle = "Grid Laser Neon Siber",
        badgeColor = Color(0xFF06B6D4),
        spectrumColors = listOf(
            Color(0xFF00F2FE),
            Color(0xFF38BDF8),
            Color(0xFFA855F7),
            Color(0xFF10B981),
            Color(0xFF00F2FE)
        )
    ),
    DIAMOND_SHATTER(
        id = "DIAMOND_SHATTER",
        displayName = "Diamond Shatter",
        subtitle = "Kristal Berlian Prismatik",
        badgeColor = Color(0xFF6366F1),
        spectrumColors = listOf(
            Color(0xFFE0E7FF),
            Color(0xFF818CF8),
            Color(0xFFC084FC),
            Color(0xFF38BDF8),
            Color(0xFFFFFFFF),
            Color(0xFF818CF8)
        )
    ),
    SAKURA_AURORA(
        id = "SAKURA_AURORA",
        displayName = "Sakura Aurora",
        subtitle = "Cahaya Aurora & Sakura",
        badgeColor = Color(0xFFF43F5E),
        spectrumColors = listOf(
            Color(0xFFFDA4AF),
            Color(0xFFF472B6),
            Color(0xFFC084FC),
            Color(0xFF67E8F9),
            Color(0xFFFDA4AF)
        )
    )
}

/**
 * Interactive 3D Holographic Foil Card wrapper that tilts in 3D when touched or dragged
 * and renders a moving rainbow/foil reflection overlay + geometric holo sparkles.
 */
@Composable
fun Interactive3DHoloCardContainer(
    holoType: HoloFoilType,
    cornerRadius: Dp = 20.dp,
    maxTiltDegrees: Float = 13f,
    foilIntensity: Float = 0.34f,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    var cardSize by remember { mutableStateOf(IntSize(1, 1)) }
    var targetNormX by remember { mutableFloatStateOf(0f) }
    var targetNormY by remember { mutableFloatStateOf(0f) }
    var isTouching by remember { mutableStateOf(false) }

    val animNormX by animateFloatAsState(
        targetValue = targetNormX,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "holoNormX"
    )
    val animNormY by animateFloatAsState(
        targetValue = targetNormY,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "holoNormY"
    )

    val infiniteTransition = rememberInfiniteTransition(label = "holoIdleShimmer")
    val idlePhase by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 6.2831855f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 4200, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "idlePhase"
    )

    val effectiveX = if (isTouching) animNormX else sin(idlePhase) * 0.28f
    val effectiveY = if (isTouching) animNormY else cos(idlePhase * 0.8f) * 0.22f

    Box(
        modifier = modifier
            .onSizeChanged { cardSize = it }
            .pointerInput(Unit) {
                detectTapGestures(
                    onPress = { offset ->
                        isTouching = true
                        val w = cardSize.width.coerceAtLeast(1).toFloat()
                        val h = cardSize.height.coerceAtLeast(1).toFloat()
                        targetNormX = ((offset.x / w) * 2f - 1f).coerceIn(-1f, 1f)
                        targetNormY = ((offset.y / h) * 2f - 1f).coerceIn(-1f, 1f)
                        tryAwaitRelease()
                        isTouching = false
                        targetNormX = 0f
                        targetNormY = 0f
                    }
                )
            }
            .pointerInput(Unit) {
                detectDragGestures(
                    onDragStart = { offset ->
                        isTouching = true
                        val w = cardSize.width.coerceAtLeast(1).toFloat()
                        val h = cardSize.height.coerceAtLeast(1).toFloat()
                        targetNormX = ((offset.x / w) * 2f - 1f).coerceIn(-1f, 1f)
                        targetNormY = ((offset.y / h) * 2f - 1f).coerceIn(-1f, 1f)
                    },
                    onDragEnd = {
                        isTouching = false
                        targetNormX = 0f
                        targetNormY = 0f
                    },
                    onDragCancel = {
                        isTouching = false
                        targetNormX = 0f
                        targetNormY = 0f
                    },
                    onDrag = { change, _ ->
                        change.consume()
                        val w = cardSize.width.coerceAtLeast(1).toFloat()
                        val h = cardSize.height.coerceAtLeast(1).toFloat()
                        targetNormX = ((change.position.x / w) * 2f - 1f).coerceIn(-1f, 1f)
                        targetNormY = ((change.position.y / h) * 2f - 1f).coerceIn(-1f, 1f)
                    }
                )
            }
            .graphicsLayer {
                rotationX = -effectiveY * maxTiltDegrees
                rotationY = effectiveX * maxTiltDegrees
                cameraDistance = 14f * density
            }
            .clip(RoundedCornerShape(cornerRadius))
            .drawWithContent {
                drawContent()

                val w = size.width
                val h = size.height
                val shiftX = (effectiveX + 1f) * 0.5f * w
                val shiftY = (effectiveY + 1f) * 0.5f * h
                val activeAlpha = if (isTouching) (foilIntensity * 1.35f).coerceAtMost(0.55f) else foilIntensity

                // 1. Diagonal Iridescent Spectrum Foil Beam
                drawRect(
                    brush = Brush.linearGradient(
                        colors = holoType.spectrumColors.map { it.copy(alpha = activeAlpha) },
                        start = Offset(shiftX - w * 0.6f, shiftY - h * 0.6f),
                        end = Offset(shiftX + w * 0.7f, shiftY + h * 0.7f)
                    )
                )

                // 2. Specular White Glare Beam following finger tilt
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            Color.White.copy(alpha = if (isTouching) 0.38f else 0.18f),
                            Color.White.copy(alpha = 0.06f),
                            Color.Transparent
                        ),
                        center = Offset(shiftX, shiftY),
                        radius = w.coerceAtLeast(h) * 0.55f
                    ),
                    center = Offset(shiftX, shiftY),
                    radius = w.coerceAtLeast(h) * 0.55f
                )

                // 3. Pattern Overlay per HoloFoilType
                when (holoType) {
                    HoloFoilType.DIAMOND_SHATTER, HoloFoilType.PRISMATIC_RAINBOW -> {
                        val cols = 6
                        val rows = 4
                        val cellW = w / cols
                        val cellH = h / rows
                        for (r in 0 until rows) {
                            for (c in 0 until cols) {
                                val cx = (c + 0.5f) * cellW + effectiveX * 6f
                                val cy = (r + 0.5f) * cellH + effectiveY * 6f
                                val dist = kotlin.math.hypot((cx - shiftX).toDouble(), (cy - shiftY).toDouble()).toFloat()
                                val facetAlpha = (1f - (dist / (w * 0.75f))).coerceIn(0.04f, 0.26f)
                                val diamond = Path().apply {
                                    moveTo(cx, cy - cellH * 0.28f)
                                    lineTo(cx + cellW * 0.28f, cy)
                                    lineTo(cx, cy + cellH * 0.28f)
                                    lineTo(cx - cellW * 0.28f, cy)
                                    close()
                                }
                                drawPath(
                                    path = diamond,
                                    color = Color.White.copy(alpha = facetAlpha),
                                    style = Stroke(width = 1.2f)
                                )
                            }
                        }
                    }
                    HoloFoilType.GOLD_STARLIGHT, HoloFoilType.SAKURA_AURORA -> {
                        for (i in 0..9) {
                            val px = ((i * 37 + 13) % 100) / 100f * w + effectiveX * (i % 3 + 1) * 4f
                            val py = ((i * 53 + 29) % 100) / 100f * h + effectiveY * (i % 3 + 1) * 4f
                            val starR = 4f + (i % 3) * 2.5f
                            drawLine(
                                color = Color.White.copy(alpha = 0.45f),
                                start = Offset(px - starR, py),
                                end = Offset(px + starR, py),
                                strokeWidth = 1.4f
                            )
                            drawLine(
                                color = Color.White.copy(alpha = 0.45f),
                                start = Offset(px, py - starR),
                                end = Offset(px, py + starR),
                                strokeWidth = 1.4f
                            )
                        }
                    }
                    HoloFoilType.CYBER_MATRIX -> {
                        val step = (w / 10f).coerceAtLeast(18f)
                        var x = 0f
                        while (x <= w) {
                            drawLine(
                                color = Color(0xFF00F2FE).copy(alpha = 0.14f),
                                start = Offset(x + effectiveX * 5f, 0f),
                                end = Offset(x - effectiveX * 5f, h),
                                strokeWidth = 1f
                            )
                            x += step
                        }
                    }
                }
            }
    ) {
        content()
    }
}

// ============================================================================
// 2. PHOTOBOOTH STIKER MASKOT & KARTU PROFIL 3D HOLOGRAM (100% FREE UNLOCKED)
// ============================================================================

enum class PhotoboothStickerFrame(
    val id: String,
    val displayName: String,
    val badgeLabel: String,
    val topBgColor: Color,
    val bottomBgColor: Color,
    val borderColor: Color,
    val accentColor: Color
) {
    ANIME_COLLAB_SSR(
        id = "ANIME_COLLAB_SSR",
        displayName = "SSR Anime Collab",
        badgeLabel = "SSR COLLAB",
        topBgColor = Color(0xFF1E1B4B),
        bottomBgColor = Color(0xFF0F172A),
        borderColor = Color(0xFFFACC15),
        accentColor = Color(0xFFEF4444)
    ),
    SUPER_HERO_ID(
        id = "SUPER_HERO_ID",
        displayName = "Avengers Hero ID",
        badgeLabel = "HERO LICENSE",
        topBgColor = Color(0xFF450A0A),
        bottomBgColor = Color(0xFF090D16),
        borderColor = Color(0xFF38BDF8),
        accentColor = Color(0xFFF59E0B)
    ),
    HIGH_FANTASY_GUILD(
        id = "HIGH_FANTASY_GUILD",
        displayName = "S-Rank Guild Card",
        badgeLabel = "S-RANK GUILD",
        topBgColor = Color(0xFF064E3B),
        bottomBgColor = Color(0xFF022C22),
        borderColor = Color(0xFFFDE047),
        accentColor = Color(0xFF10B981)
    ),
    CYBER_AGENT_PASS(
        id = "CYBER_AGENT_PASS",
        displayName = "Time-Dive Agent",
        badgeLabel = "CHRONO PASS",
        topBgColor = Color(0xFF082F49),
        bottomBgColor = Color(0xFF090D16),
        borderColor = Color(0xFF00F2FE),
        accentColor = Color(0xFFA855F7)
    ),
    KAWAII_POLAROID(
        id = "KAWAII_POLAROID",
        displayName = "Polaroid Stiker",
        badgeLabel = "CUTIE POLAROID",
        topBgColor = Color(0xFF831843),
        bottomBgColor = Color(0xFF3B0764),
        borderColor = Color(0xFFF472B6),
        accentColor = Color(0xFFFBBF24)
    ),
    ROYAL_SULTAN_VIP(
        id = "ROYAL_SULTAN_VIP",
        displayName = "Sultan Sovereign",
        badgeLabel = "VIP SULTAN",
        topBgColor = Color(0xFF1C1917),
        bottomBgColor = Color(0xFF090D16),
        borderColor = Color(0xFFF59E0B),
        accentColor = Color(0xFFFEF08A)
    )
}

enum class PhotoboothStickerBadge(
    val id: String,
    val label: String,
    val color: Color
) {
    SUN_BREATHING("SUN_BREATHING", "HINOKAMI", Color(0xFFEF4444)),
    SURVEY_WINGS("SURVEY_WINGS", "SCOUT CORPS", Color(0xFF10B981)),
    BANKAI_SOUL("BANKAI_SOUL", "BANKAI", Color(0xFF6366F1)),
    SIX_PATHS("SIX_PATHS", "HOKAGE SAGE", Color(0xFFF97316)),
    TIME_DIVER("TIME_DIVER", "LINK CLICK", Color(0xFF38BDF8)),
    SEA_GOD("SEA_GOD", "SOUL LAND", Color(0xFF0284C7)),
    FLAME_EMPEROR("FLAME_EMPEROR", "DOU DI EMPEROR", Color(0xFFA855F7)),
    NANO_HERO("NANO_HERO", "NANO AVENGER", Color(0xFFE11D48)),
    DARK_KNIGHT("DARK_KNIGHT", "DARK KNIGHT", Color(0xFF475569)),
    ELVEN_KING("ELVEN_KING", "ELVEN LORD", Color(0xFF059669)),
    VAULT_MASTER("VAULT_MASTER", "VAULT GUARD", Color(0xFF2563EB)),
    GRANDMASTER_IQ("GRANDMASTER_IQ", "IQ 200+", Color(0xFFD97706))
}

@Composable
fun PhotoboothBadgeVectorGlyph(
    tint: Color = Color.White,
    size: Dp = 11.dp
) {
    Canvas(modifier = Modifier.size(size)) {
        val w = this.size.width
        val h = this.size.height
        val path = Path().apply {
            moveTo(w * 0.5f, 0f)
            lineTo(w * 0.66f, h * 0.34f)
            lineTo(w, h * 0.5f)
            lineTo(w * 0.66f, h * 0.66f)
            lineTo(w * 0.5f, h)
            lineTo(w * 0.34f, h * 0.66f)
            lineTo(0f, h * 0.5f)
            lineTo(w * 0.34f, h * 0.34f)
            close()
        }
        drawPath(path = path, color = tint)
    }
}

object ViralTrendsHubState {
    var selectedHoloFoil by mutableStateOf(HoloFoilType.PRISMATIC_RAINBOW)
    var selectedPhotoboothFrame by mutableStateOf(PhotoboothStickerFrame.ANIME_COLLAB_SSR)
    var selectedStickerBadge by mutableStateOf(PhotoboothStickerBadge.SUN_BREATHING)
    var profileTitle by mutableStateOf("GUARDIAN ARCANUM")
    var profileMotto by mutableStateOf("Pelindung Brankas & Penjelajah Waktu")
    var savedStickersCount by mutableIntStateOf(3)
}

@Composable
fun MascotPhotoboothAndHoloProfileStudio(
    totalVaultFiles: Int = 12,
    onPostBanner: (String) -> Unit = {}
) {
    val activeHolo = ViralTrendsHubState.selectedHoloFoil
    val activeFrame = ViralTrendsHubState.selectedPhotoboothFrame
    val activeBadge = ViralTrendsHubState.selectedStickerBadge
    val activeOutfit = AppThemeController.mascotOutfit
    val activeSkin = AppThemeController.mascotSkin
    val activeGloves = AppThemeController.mascotGloves
    val activeAccessory = AppThemeController.mascotAccessory
    val activePose = AppThemeController.mascotActivityPose

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("mascot_photobooth_studio"),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // 1. LIVE 3D HOLOGRAPHIC FOIL PROFILE & STICKER CARD (TOUCH TO TILT)
        Interactive3DHoloCardContainer(
            holoType = activeHolo,
            cornerRadius = 24.dp,
            maxTiltDegrees = 14f,
            foilIntensity = 0.36f,
            modifier = Modifier
                .fillMaxWidth()
                .testTag("photobooth_3d_holo_card")
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.verticalGradient(
                            listOf(activeFrame.topBgColor, activeFrame.bottomBgColor)
                        )
                    )
                    .border(
                        width = 3.dp,
                        color = activeFrame.borderColor,
                        shape = RoundedCornerShape(24.dp)
                    )
                    .padding(16.dp)
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    // Top Collectible Card Header: Rarity + Frame Badge + Corner Sticker
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(activeFrame.borderColor)
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = activeFrame.badgeLabel,
                                    fontFamily = JetBrainsMonoFontFamily,
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = Color(0xFF090D16)
                                )
                            }
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(2.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                repeat(5) {
                                    Icon(
                                        imageVector = Icons.Default.Star,
                                        contentDescription = null,
                                        tint = Color(0xFFFACC15),
                                        modifier = Modifier.size(12.dp)
                                    )
                                }
                            }
                        }

                        // Corner Sticker Stamp
                        Row(
                            modifier = Modifier
                                .clip(RoundedCornerShape(10.dp))
                                .background(activeBadge.color)
                                .border(1.5.dp, Color.White, RoundedCornerShape(10.dp))
                                .padding(horizontal = 9.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            PhotoboothBadgeVectorGlyph(tint = Color.White, size = 10.dp)
                            Text(
                                text = activeBadge.label,
                                fontFamily = JetBrainsMonoFontFamily,
                                fontSize = 8.5.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = Color.White
                            )
                        }
                    }

                    // Center Stage: Mascot Sticker Preview + Character Stats Column
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(18.dp))
                            .background(Color.Black.copy(alpha = 0.35f))
                            .border(1.dp, activeFrame.borderColor.copy(alpha = 0.45f), RoundedCornerShape(18.dp))
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        // Sticker Cutout Frame with Live Mascot
                        Box(
                            modifier = Modifier
                                .size(118.dp)
                                .clip(RoundedCornerShape(18.dp))
                                .background(
                                    Brush.radialGradient(
                                        listOf(
                                            activeFrame.accentColor.copy(alpha = 0.42f),
                                            Color(0xFF090D16).copy(alpha = 0.85f)
                                        )
                                    )
                                )
                                .border(2.dp, Color.White.copy(alpha = 0.85f), RoundedCornerShape(18.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            NyxMascotAvatar(
                                mood = MascotMood.IDLE_BREATHE,
                                persona = MascotPersona.COSMIC_DRAGON,
                                size = 106.dp
                            )
                        }

                        // Profile Identity & Equipped Loadout Specs
                        Column(
                            modifier = Modifier.weight(1f),
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text(
                                text = ViralTrendsHubState.profileTitle,
                                style = MaterialTheme.typography.titleMedium,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = Color.White,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            Text(
                                text = "\"${ViralTrendsHubState.profileMotto}\"",
                                style = MaterialTheme.typography.bodySmall,
                                fontSize = 10.sp,
                                color = Color(0xFFE2E8F0),
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            HoloSpecChip(label = "KOSTUM", value = activeOutfit.displayName, accent = activeFrame.borderColor)
                            HoloSpecChip(label = "SENJATA", value = activeGloves.displayName, accent = activeFrame.accentColor)
                            HoloSpecChip(label = "KEPALA", value = activeAccessory.displayName, accent = activeHolo.badgeColor)
                        }
                    }

                    // Bottom Card Footer: Foil Badge + Touch Hint
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "FOIL: ${activeHolo.displayName.uppercase()} • GRATIS",
                            fontFamily = JetBrainsMonoFontFamily,
                            fontSize = 8.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFCBD5E1)
                        )
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.TouchApp,
                                contentDescription = null,
                                tint = activeFrame.borderColor,
                                modifier = Modifier.size(12.dp)
                            )
                            Text(
                                text = "Sentuh & Geser Kartu (Efek 3D)",
                                fontFamily = JetBrainsMonoFontFamily,
                                fontSize = 8.5.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = activeFrame.borderColor
                            )
                        }
                    }
                }
            }
        }

        // 2. CONTROLS CARD: HOLO FOIL TYPE, STICKER FRAME, CORNER STAMP & QUICK ACTIONS
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .duolingoCard(
                    backgroundColor = DuoPureWhite,
                    borderColor = DuoSwanBorder,
                    lipColor = DuoSwanBorder,
                    cornerRadius = 18.dp,
                    borderWidth = 2.dp,
                    lipHeight = 4.dp
                )
                .padding(bottom = 4.dp)
                .padding(14.dp)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.AutoAwesome,
                        contentDescription = null,
                        tint = DuoMacawBlue,
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = "1. Pilih Lapisan 3D Holographic Foil (Semua Gratis)",
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.ExtraBold,
                        color = DuoEelInk
                    )
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    HoloFoilType.entries.forEach { foil ->
                        val isSel = activeHolo == foil
                        Box(
                            modifier = Modifier
                                .duolingoButton(
                                    faceColor = if (isSel) foil.badgeColor else DuoMintSurface,
                                    lipColor = if (isSel) AppThemeController.darkenColor(foil.badgeColor, 0.65f) else DuoSwanBorder,
                                    cornerRadius = 10.dp,
                                    lipHeight = 3.dp
                                )
                                .instantTouchPress { ViralTrendsHubState.selectedHoloFoil = foil }
                                .padding(bottom = 3.dp)
                                .padding(horizontal = 10.dp, vertical = 6.dp)
                                .testTag("holo_foil_${foil.id.lowercase()}")
                        ) {
                            Text(
                                text = foil.displayName,
                                fontSize = 9.5.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = if (isSel) Color.White else DuoEelInk
                            )
                        }
                    }
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.FilterFrames,
                        contentDescription = null,
                        tint = DuoFeatherGreen,
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = "2. Pilih Bingkai Kartu Stiker Profil",
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.ExtraBold,
                        color = DuoEelInk
                    )
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    PhotoboothStickerFrame.entries.forEach { frame ->
                        val isSel = activeFrame == frame
                        Box(
                            modifier = Modifier
                                .duolingoButton(
                                    faceColor = if (isSel) frame.accentColor else DuoMintSurface,
                                    lipColor = if (isSel) AppThemeController.darkenColor(frame.accentColor, 0.65f) else DuoSwanBorder,
                                    cornerRadius = 10.dp,
                                    lipHeight = 3.dp
                                )
                                .instantTouchPress { ViralTrendsHubState.selectedPhotoboothFrame = frame }
                                .padding(bottom = 3.dp)
                                .padding(horizontal = 10.dp, vertical = 6.dp)
                                .testTag("photobooth_frame_${frame.id.lowercase()}")
                        ) {
                            Text(
                                text = frame.displayName,
                                fontSize = 9.5.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = if (isSel) Color.White else DuoEelInk
                            )
                        }
                    }
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Label,
                        contentDescription = null,
                        tint = DuoBeetlePurple,
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = "3. Pilih Stempel Stiker Sudut (12 Stiker Gratis)",
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.ExtraBold,
                        color = DuoEelInk
                    )
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    PhotoboothStickerBadge.entries.forEach { badge ->
                        val isSel = activeBadge == badge
                        Box(
                            modifier = Modifier
                                .duolingoButton(
                                    faceColor = if (isSel) badge.color else DuoMintSurface,
                                    lipColor = if (isSel) AppThemeController.darkenColor(badge.color, 0.65f) else DuoSwanBorder,
                                    cornerRadius = 10.dp,
                                    lipHeight = 3.dp
                                )
                                .instantTouchPress { ViralTrendsHubState.selectedStickerBadge = badge }
                                .padding(bottom = 3.dp)
                                .padding(horizontal = 10.dp, vertical = 6.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                PhotoboothBadgeVectorGlyph(
                                    tint = if (isSel) Color.White else badge.color,
                                    size = 9.dp
                                )
                                Text(
                                    text = badge.label,
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = if (isSel) Color.White else DuoEelInk
                                )
                            }
                        }
                    }
                }

                // Quick Preset & Pose Switcher for Photobooth
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Palette,
                        contentDescription = null,
                        tint = DuoFoxOrange,
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = "4. Ganti Cepat Kostum Collab & Pose Foto",
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.ExtraBold,
                        color = DuoEelInk
                    )
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    MascotCostumePreset.entries.forEach { preset ->
                        val isEquipped = activeOutfit == preset.outfit && activeSkin == preset.skin
                        Box(
                            modifier = Modifier
                                .duolingoButton(
                                    faceColor = if (isEquipped) preset.accentColor else DuoMintSurface,
                                    lipColor = if (isEquipped) AppThemeController.darkenColor(preset.accentColor, 0.65f) else DuoSwanBorder,
                                    cornerRadius = 10.dp,
                                    lipHeight = 3.dp
                                )
                                .instantTouchPress {
                                    AppThemeController.applyCostumePreset(preset)
                                }
                                .padding(bottom = 3.dp)
                                .padding(horizontal = 9.dp, vertical = 5.dp)
                        ) {
                            Text(
                                text = preset.displayName,
                                fontSize = 8.8.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = if (isEquipped) Color.White else DuoEelInk
                            )
                        }
                    }
                }

                // Editable Title & Motto
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = ViralTrendsHubState.profileTitle,
                        onValueChange = { ViralTrendsHubState.profileTitle = it.take(26) },
                        label = { Text("Judul Kartu Profil", fontSize = 10.sp) },
                        singleLine = true,
                        modifier = Modifier.weight(1f),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = DuoMacawBlue,
                            unfocusedBorderColor = DuoSwanBorder,
                            focusedTextColor = DuoEelInk,
                            unfocusedTextColor = DuoEelInk
                        )
                    )
                    OutlinedTextField(
                        value = ViralTrendsHubState.profileMotto,
                        onValueChange = { ViralTrendsHubState.profileMotto = it.take(45) },
                        label = { Text("Slogan / Kutipan", fontSize = 10.sp) },
                        singleLine = true,
                        modifier = Modifier.weight(1f),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = DuoMacawBlue,
                            unfocusedBorderColor = DuoSwanBorder,
                            focusedTextColor = DuoEelInk,
                            unfocusedTextColor = DuoEelInk
                        )
                    )
                }

                // Action Buttons: Shuffle Pose/Sticker & Save Sticker Card
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .duolingoButton(
                                faceColor = DuoBeetlePurple,
                                lipColor = DuoBeetlePurpleLip,
                                cornerRadius = 12.dp,
                                lipHeight = 4.dp
                            )
                            .instantTouchPress {
                                ViralTrendsHubState.selectedHoloFoil = HoloFoilType.entries.random()
                                ViralTrendsHubState.selectedPhotoboothFrame = PhotoboothStickerFrame.entries.random()
                                ViralTrendsHubState.selectedStickerBadge = PhotoboothStickerBadge.entries.random()
                                AppThemeController.mascotActivityPose = MascotActivityPose.entries.random()
                                AppThemeController.poseTriggerNonce++
                            }
                            .padding(bottom = 4.dp)
                            .padding(vertical = 10.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(5.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Casino,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(14.dp)
                            )
                            Text(
                                text = "Acak Gaya & Pose Foto",
                                fontSize = 10.5.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = Color.White
                            )
                        }
                    }

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .duolingoButton(
                                faceColor = DuoFeatherGreen,
                                lipColor = DuoFeatherGreenLip,
                                cornerRadius = 12.dp,
                                lipHeight = 4.dp
                            )
                            .instantTouchPress {
                                ViralTrendsHubState.savedStickersCount++
                                onPostBanner(
                                    "Kartu Profil 3D '${ViralTrendsHubState.profileTitle}' (${activeHolo.displayName}) disimpan!"
                                )
                            }
                            .padding(bottom = 4.dp)
                            .padding(vertical = 10.dp)
                            .testTag("photobooth_save_card_button"),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(5.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.CameraAlt,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(14.dp)
                            )
                            Text(
                                text = "Cetak Stiker (${ViralTrendsHubState.savedStickersCount})",
                                fontSize = 10.5.sp,
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

@Composable
private fun HoloSpecChip(
    label: String,
    value: String,
    accent: Color
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(6.dp))
            .background(Color.White.copy(alpha = 0.08f))
            .padding(horizontal = 7.dp, vertical = 3.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            fontFamily = JetBrainsMonoFontFamily,
            fontSize = 7.5.sp,
            fontWeight = FontWeight.Bold,
            color = accent
        )
        Text(
            text = value,
            fontSize = 8.5.sp,
            fontWeight = FontWeight.ExtraBold,
            color = Color.White,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

// ============================================================================
// 3. TIME CAPSULE LOCK & LIVE COUNTDOWN ENGINE
// ============================================================================

data class VaultTimeCapsuleEntry(
    val id: Int,
    val title: String,
    val secretNoteOrPayload: String,
    val linkedFileName: String?,
    val createdAtMillis: Long,
    val unlockAtMillis: Long,
    val isOpened: Boolean = false
)

object VaultTimeCapsuleRepository {
    private var nextId = 103
    val capsules = mutableStateListOf(
        VaultTimeCapsuleEntry(
            id = 101,
            title = "Kapsul Memori Anime & Resolusi",
            secretNoteOrPayload = "Selamat! Segel waktu telah terbuka. Tetap jaga rahasia brankas Arcanum dan koleksi kostum kolaborasimu!",
            linkedFileName = "Arcanum_Secret_Key_Backup.txt",
            createdAtMillis = System.currentTimeMillis() - 3600_000L,
            unlockAtMillis = System.currentTimeMillis() + 45_000L // Unlocks in 45 seconds for live demo!
        ),
        VaultTimeCapsuleEntry(
            id = 102,
            title = "Arsip Kapsul Masa Depan #2026",
            secretNoteOrPayload = "Kode cadangan spesial dan catatan pribadi masa depan yang disegel dengan kunci waktu.",
            linkedFileName = "Vault_Master_Archive.zip",
            createdAtMillis = System.currentTimeMillis() - 86400_000L,
            unlockAtMillis = System.currentTimeMillis() - 5_000L // Already unlocked so user can test opening immediately!
        )
    )

    fun addCapsule(
        title: String,
        secretNote: String,
        linkedFileName: String?,
        durationSeconds: Long
    ) {
        val now = System.currentTimeMillis()
        capsules.add(
            0,
            VaultTimeCapsuleEntry(
                id = nextId++,
                title = title.ifBlank { "Kapsul Waktu #${nextId}" },
                secretNoteOrPayload = secretNote.ifBlank { "Catatan rahasia terenkripsi di dalam Time Capsule." },
                linkedFileName = linkedFileName,
                createdAtMillis = now,
                unlockAtMillis = now + (durationSeconds * 1000L)
            )
        )
    }

    fun markOpened(id: Int) {
        val idx = capsules.indexOfFirst { it.id == id }
        if (idx >= 0) {
            capsules[idx] = capsules[idx].copy(isOpened = true)
        }
    }

    fun deleteCapsule(id: Int) {
        capsules.removeAll { it.id == id }
    }

    fun formatCountdown(remainingMillis: Long): String {
        if (remainingMillis <= 0L) return "00h : 00j : 00m : 00d"
        val totalSeconds = remainingMillis / 1000L
        val days = totalSeconds / 86400L
        val hours = (totalSeconds % 86400L) / 3600L
        val minutes = (totalSeconds % 3600L) / 60L
        val seconds = totalSeconds % 60L
        return String.format(Locale.US, "%02dh : %02dj : %02dm : %02dd", days, hours, minutes, seconds)
    }
}

@Composable
fun VaultTimeCapsuleStudioContent(
    vaultItems: List<VaultItemEntity> = emptyList(),
    onPostBanner: (String) -> Unit = {}
) {
    var nowMillis by remember { mutableLongStateOf(System.currentTimeMillis()) }
    LaunchedEffect(Unit) {
        while (true) {
            delay(1000L)
            nowMillis = System.currentTimeMillis()
        }
    }

    var newTitle by remember { mutableStateOf("") }
    var newSecretNote by remember { mutableStateOf("") }
    var selectedVaultFileName by remember { mutableStateOf<String?>(null) }
    var selectedDurationSec by remember { mutableLongStateOf(60L) } // Default 1 minute

    val durationPresets = listOf(
        10L to "10 Detik (Tes)",
        60L to "1 Menit",
        3600L to "1 Jam",
        86400L to "24 Jam",
        604800L to "7 Hari",
        2592000L to "30 Hari"
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("time_capsule_studio_content"),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // 1. CREATE NEW TIME CAPSULE CARD
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .duolingoCard(
                    backgroundColor = DuoPureWhite,
                    borderColor = DuoMacawBlue,
                    lipColor = DuoMacawBlueLip,
                    cornerRadius = 18.dp,
                    borderWidth = 2.dp,
                    lipHeight = 4.dp
                )
                .padding(bottom = 4.dp)
                .padding(14.dp)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.HourglassBottom,
                        contentDescription = "Time Capsule",
                        tint = DuoMacawBlue,
                        modifier = Modifier.size(20.dp)
                    )
                    Text(
                        text = "Buat Time Capsule Baru (Kunci Hitung Mundur)",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.ExtraBold,
                        color = DuoEelInk
                    )
                }

                OutlinedTextField(
                    value = newTitle,
                    onValueChange = { newTitle = it },
                    label = { Text("Nama Kapsul Waktu", fontSize = 11.sp) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = DuoMacawBlue,
                        unfocusedBorderColor = DuoSwanBorder,
                        focusedTextColor = DuoEelInk,
                        unfocusedTextColor = DuoEelInk
                    )
                )

                OutlinedTextField(
                    value = newSecretNote,
                    onValueChange = { newSecretNote = it },
                    label = { Text("Pesan Rahasia / Surat Masa Depan", fontSize = 11.sp) },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 2,
                    maxLines = 3,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = DuoMacawBlue,
                        unfocusedBorderColor = DuoSwanBorder,
                        focusedTextColor = DuoEelInk,
                        unfocusedTextColor = DuoEelInk
                    )
                )

                if (vaultItems.isNotEmpty()) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(5.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.AttachFile,
                            contentDescription = null,
                            tint = DuoMacawBlue,
                            modifier = Modifier.size(14.dp)
                        )
                        Text(
                            text = "Lampirkan File Brankas Aktif (Opsional):",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = CosmicTextSubtle
                        )
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        val isNone = selectedVaultFileName == null
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (isNone) DuoMacawBlue else DuoMintSurface)
                                .border(1.dp, DuoSwanBorder, RoundedCornerShape(8.dp))
                                .instantTouchPress { selectedVaultFileName = null }
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = "Tanpa File",
                                fontSize = 9.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = if (isNone) Color.White else DuoEelInk
                            )
                        }
                        vaultItems.take(8).forEach { vItem ->
                            val fullName = "${vItem.name}.${vItem.extension}"
                            val isSel = selectedVaultFileName == fullName
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(if (isSel) DuoFeatherGreen else DuoMintSurface)
                                    .border(1.dp, DuoSwanBorder, RoundedCornerShape(8.dp))
                                    .instantTouchPress { selectedVaultFileName = fullName }
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = fullName,
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = if (isSel) Color.White else DuoEelInk,
                                    maxLines = 1
                                )
                            }
                        }
                    }
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Schedule,
                        contentDescription = null,
                        tint = DuoFoxOrange,
                        modifier = Modifier.size(14.dp)
                    )
                    Text(
                        text = "Pilih Durasi Segel Hitung Mundur:",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = CosmicTextSubtle
                    )
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    durationPresets.forEach { (sec, label) ->
                        val isSel = selectedDurationSec == sec
                        Box(
                            modifier = Modifier
                                .duolingoButton(
                                    faceColor = if (isSel) DuoFoxOrange else DuoMintSurface,
                                    lipColor = if (isSel) DuoFoxOrangeLip else DuoSwanBorder,
                                    cornerRadius = 10.dp,
                                    lipHeight = 3.dp
                                )
                                .instantTouchPress { selectedDurationSec = sec }
                                .padding(bottom = 3.dp)
                                .padding(horizontal = 9.dp, vertical = 5.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Schedule,
                                    contentDescription = null,
                                    tint = if (isSel) Color.White else DuoFoxOrange,
                                    modifier = Modifier.size(11.dp)
                                )
                                Text(
                                    text = label,
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = if (isSel) Color.White else DuoEelInk
                                )
                            }
                        }
                    }
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .duolingoButton(
                            faceColor = DuoFeatherGreen,
                            lipColor = DuoFeatherGreenLip,
                            cornerRadius = 12.dp,
                            lipHeight = 4.dp
                        )
                        .instantTouchPress {
                            VaultTimeCapsuleRepository.addCapsule(
                                title = newTitle,
                                secretNote = newSecretNote,
                                linkedFileName = selectedVaultFileName,
                                durationSeconds = selectedDurationSec
                            )
                            onPostBanner("Time Capsule berhasil disegel! Hitung mundur dimulai.")
                            newTitle = ""
                            newSecretNote = ""
                        }
                        .padding(bottom = 4.dp)
                        .padding(vertical = 10.dp)
                        .testTag("create_time_capsule_button"),
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Lock,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(14.dp)
                        )
                        Text(
                            text = "Segel & Mulai Hitung Mundur Time Capsule",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color.White
                        )
                    }
                }
            }
        }

        // 2. LIST OF ACTIVE & READY TIME CAPSULES
        VaultTimeCapsuleRepository.capsules.forEach { capsule ->
            val remainingMs = (capsule.unlockAtMillis - nowMillis).coerceAtLeast(0L)
            val isReadyToUnlock = remainingMs <= 0L
            val borderCol = if (isReadyToUnlock) DuoFeatherGreen else DuoFoxOrange
            val lipCol = if (isReadyToUnlock) DuoFeatherGreenLip else DuoFoxOrangeLip

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .duolingoCard(
                        backgroundColor = DuoPureWhite,
                        borderColor = borderCol,
                        lipColor = lipCol,
                        cornerRadius = 16.dp,
                        borderWidth = 2.dp,
                        lipHeight = 4.dp
                    )
                    .padding(bottom = 4.dp)
                    .padding(12.dp)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(
                                imageVector = if (isReadyToUnlock) Icons.Default.LockOpen else Icons.Default.Lock,
                                contentDescription = "Status Segel",
                                tint = borderCol,
                                modifier = Modifier.size(18.dp)
                            )
                            Text(
                                text = capsule.title,
                                style = MaterialTheme.typography.labelLarge,
                                fontWeight = FontWeight.ExtraBold,
                                color = DuoEelInk,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }

                        IconButton(
                            onClick = { VaultTimeCapsuleRepository.deleteCapsule(capsule.id) },
                            modifier = Modifier.size(28.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Hapus Kapsul",
                                tint = DuoCardinalRed,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }

                    if (capsule.linkedFileName != null) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.AttachFile,
                                contentDescription = null,
                                tint = DuoMacawBlue,
                                modifier = Modifier.size(12.dp)
                            )
                            Text(
                                text = "File Terlampir: ${capsule.linkedFileName}",
                                fontFamily = JetBrainsMonoFontFamily,
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                color = DuoMacawBlue
                            )
                        }
                    }

                    // Live Digital Countdown Box
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .background(Color(0xFF090D16))
                            .border(1.dp, borderCol.copy(alpha = 0.7f), RoundedCornerShape(10.dp))
                            .padding(vertical = 8.dp, horizontal = 12.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = if (isReadyToUnlock) "SEGEL WAKTU TERBUKA" else "HITUNG MUNDUR BUKA SEGEL",
                                fontFamily = JetBrainsMonoFontFamily,
                                fontSize = 8.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isReadyToUnlock) Color(0xFF4ADE80) else Color(0xFFFBBF24)
                            )
                            Text(
                                text = VaultTimeCapsuleRepository.formatCountdown(remainingMs),
                                fontFamily = JetBrainsMonoFontFamily,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = Color.White
                            )
                        }
                    }

                    if (isReadyToUnlock) {
                        if (capsule.isOpened) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(DuoMintSurface)
                                    .border(1.dp, DuoFeatherGreen, RoundedCornerShape(10.dp))
                                    .padding(10.dp)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.LockOpen,
                                        contentDescription = null,
                                        tint = DuoFeatherGreen,
                                        modifier = Modifier.size(15.dp)
                                    )
                                    Text(
                                        text = "Isi Pesan: \"${capsule.secretNoteOrPayload}\"",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = DuoEelInk
                                    )
                                }
                            }
                        } else {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .duolingoButton(
                                        faceColor = DuoFeatherGreen,
                                        lipColor = DuoFeatherGreenLip,
                                        cornerRadius = 10.dp,
                                        lipHeight = 3.dp
                                    )
                                    .instantTouchPress {
                                        VaultTimeCapsuleRepository.markOpened(capsule.id)
                                        onPostBanner("Kapsul Waktu '${capsule.title}' berhasil dibuka!")
                                    }
                                    .padding(bottom = 3.dp)
                                    .padding(vertical = 8.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.LockOpen,
                                        contentDescription = null,
                                        tint = Color.White,
                                        modifier = Modifier.size(14.dp)
                                    )
                                    Text(
                                        text = "Buka Segel & Lihat Isi Kapsul Sekarang",
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.ExtraBold,
                                        color = Color.White
                                    )
                                }
                            }
                        }
                    } else {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(5.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Lock,
                                contentDescription = null,
                                tint = CosmicTextSubtle,
                                modifier = Modifier.size(12.dp)
                            )
                            Text(
                                text = "Isi pesan & file dikunci rapat hingga hitung mundur mencapai 00:00:00.",
                                fontSize = 9.5.sp,
                                color = CosmicTextSubtle
                            )
                        }
                    }
                }
            }
        }
    }
}

// ============================================================================
// 4. ARCANUM WRAPPED & STRUK KASIR RECEIPTIFY STUDIO
// ============================================================================

@Composable
fun ArcanumWrappedAndReceiptifyStudio(
    activeVaultId: Int,
    allItems: List<VaultItemEntity>,
    onPostBanner: (String) -> Unit = {}
) {
    // 0 = Arcanum Wrapped (3D Holo Story Card), 1 = Struk Kasir Receiptify (Thermal Receipt)
    var displayMode by remember { mutableIntStateOf(0) }
    var wrappedThemeIdx by remember { mutableIntStateOf(0) }

    val totalFiles = allItems.size
    val encryptedFiles = remember(allItems) { allItems.count { it.isEncrypted } }
    val totalBytes = remember(allItems) { allItems.sumOf { it.sizeBytes } }
    val totalBrainGames = BrainGameId.entries.size
    val totalOutfits = com.example.ui.theme.MascotUpperOutfit.entries.size
    val activeOutfit = AppThemeController.mascotOutfit
    val activeTheme = AppThemeController.activeTheme
    val activeCapsules = VaultTimeCapsuleRepository.capsules.size
    val dateString = remember {
        SimpleDateFormat("dd MMM yyyy • HH:mm", Locale.getDefault()).format(Date())
    }

    val wrappedPalettes = listOf(
        Triple("Spotify Neon", Color(0xFF0F172A), Color(0xFF10B981)),
        Triple("Anime Hinokami", Color(0xFF450A0A), Color(0xFFF97316)),
        Triple("Cyber Matrix", Color(0xFF082F49), Color(0xFF00F2FE)),
        Triple("Sultan Gold", Color(0xFF1C1917), Color(0xFFFACC15))
    )
    val currentWrappedPalette = wrappedPalettes[wrappedThemeIdx % wrappedPalettes.size]

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("arcanum_wrapped_receiptify_studio"),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Mode Switcher Bar: Arcanum Wrapped vs Struk Kasir Receiptify
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .duolingoButton(
                        faceColor = if (displayMode == 0) DuoBeetlePurple else DuoMintSurface,
                        lipColor = if (displayMode == 0) DuoBeetlePurpleLip else DuoSwanBorder,
                        cornerRadius = 12.dp,
                        lipHeight = 4.dp
                    )
                    .instantTouchPress { displayMode = 0 }
                    .padding(bottom = 4.dp)
                    .padding(vertical = 10.dp)
                    .testTag("mode_arcanum_wrapped_button"),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.AutoAwesome,
                        contentDescription = null,
                        tint = if (displayMode == 0) Color.White else DuoBeetlePurple,
                        modifier = Modifier.size(14.dp)
                    )
                    Text(
                        text = "Arcanum Wrapped 3D",
                        fontSize = 10.5.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = if (displayMode == 0) Color.White else DuoEelInk
                    )
                }
            }

            Box(
                modifier = Modifier
                    .weight(1f)
                    .duolingoButton(
                        faceColor = if (displayMode == 1) DuoFoxOrange else DuoMintSurface,
                        lipColor = if (displayMode == 1) DuoFoxOrangeLip else DuoSwanBorder,
                        cornerRadius = 12.dp,
                        lipHeight = 4.dp
                    )
                    .instantTouchPress { displayMode = 1 }
                    .padding(bottom = 4.dp)
                    .padding(vertical = 10.dp)
                    .testTag("mode_receiptify_struk_button"),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.ReceiptLong,
                        contentDescription = null,
                        tint = if (displayMode == 1) Color.White else DuoFoxOrange,
                        modifier = Modifier.size(14.dp)
                    )
                    Text(
                        text = "Struk Kasir Receiptify",
                        fontSize = 10.5.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = if (displayMode == 1) Color.White else DuoEelInk
                    )
                }
            }
        }

        if (displayMode == 0) {
            // MODE 1: ARCANUM WRAPPED STORY CARD WITH 3D HOLO TILT
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                wrappedPalettes.forEachIndexed { idx, (name, _, accent) ->
                    val isSel = wrappedThemeIdx == idx
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(if (isSel) accent else DuoMintSurface)
                            .border(1.dp, DuoSwanBorder, RoundedCornerShape(8.dp))
                            .instantTouchPress { wrappedThemeIdx = idx }
                            .padding(horizontal = 10.dp, vertical = 5.dp)
                    ) {
                        Text(
                            text = name,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = if (isSel) Color(0xFF090D16) else DuoEelInk
                        )
                    }
                }
            }

            Interactive3DHoloCardContainer(
                holoType = HoloFoilType.PRISMATIC_RAINBOW,
                cornerRadius = 24.dp,
                maxTiltDegrees = 12f,
                foilIntensity = 0.30f,
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.verticalGradient(
                                listOf(
                                    currentWrappedPalette.second,
                                    Color(0xFF090D16)
                                )
                            )
                        )
                        .border(2.5.dp, currentWrappedPalette.third, RoundedCornerShape(24.dp))
                        .padding(18.dp)
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "ARCANUM WRAPPED • 2026",
                                fontFamily = JetBrainsMonoFontFamily,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = currentWrappedPalette.third
                            )
                            Text(
                                text = "BRANKAS #$activeVaultId",
                                fontFamily = JetBrainsMonoFontFamily,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = Color.White
                            )
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(78.dp)
                                    .clip(CircleShape)
                                    .background(currentWrappedPalette.third.copy(alpha = 0.22f))
                                    .border(2.dp, currentWrappedPalette.third, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                NyxMascotAvatar(
                                    mood = MascotMood.IDLE_BREATHE,
                                    persona = MascotPersona.COSMIC_DRAGON,
                                    size = 68.dp
                                )
                            }
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "AURA PERSONA KAMU",
                                    fontFamily = JetBrainsMonoFontFamily,
                                    fontSize = 8.5.sp,
                                    color = Color(0xFF94A3B8)
                                )
                                Text(
                                    text = "S-Rank Sovereign",
                                    style = MaterialTheme.typography.titleLarge,
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = Color.White
                                )
                                Text(
                                    text = "Kostum Andalan: ${activeOutfit.displayName}",
                                    fontSize = 10.5.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = currentWrappedPalette.third
                                )
                            }
                        }

                        // 2x2 Bento Stat Grid inside Wrapped Card
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            WrappedStatBox(
                                title = "FILE TERENKRIPSI",
                                bigValue = "$encryptedFiles / $totalFiles",
                                subValue = "3-Layer AES-256",
                                accent = currentWrappedPalette.third,
                                modifier = Modifier.weight(1f)
                            )
                            WrappedStatBox(
                                title = "KOLEKSI KOSTUM",
                                bigValue = "$totalOutfits Set",
                                subValue = "100% Terbuka Gratis",
                                accent = Color(0xFFFACC15),
                                modifier = Modifier.weight(1f)
                            )
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            WrappedStatBox(
                                title = "BRAIN PUZZLE HUB",
                                bigValue = "$totalBrainGames Game",
                                subValue = "4 Level Kesulitan",
                                accent = Color(0xFF38BDF8),
                                modifier = Modifier.weight(1f)
                            )
                            WrappedStatBox(
                                title = "TIME CAPSULE",
                                bigValue = "$activeCapsules Segel",
                                subValue = "Tema: ${activeTheme.displayName}",
                                accent = Color(0xFFF472B6),
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }
            }
        } else {
            // MODE 2: STRUK KASIR RECEIPTIFY (THERMAL SUPERMARKET RECEIPT)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color(0xFFFAF8F5))
                    .border(1.5.dp, Color(0xFFCBD5E1), RoundedCornerShape(10.dp))
                    .testTag("receiptify_thermal_card")
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    // Top Zig-Zag Torn Receipt Edge
                    ReceiptZigZagEdge(isTop = true)

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 18.dp, vertical = 10.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(
                            text = "ARCANUM VAULT MART",
                            fontFamily = JetBrainsMonoFontFamily,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color(0xFF0F172A),
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )
                        Text(
                            text = "CABANG KOSMIK #0451 • TGL: $dateString",
                            fontFamily = JetBrainsMonoFontFamily,
                            fontSize = 8.5.sp,
                            color = Color(0xFF475569),
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )
                        Text(
                            text = "KASIR: MASKOT ${activeOutfit.displayName.uppercase()}",
                            fontFamily = JetBrainsMonoFontFamily,
                            fontSize = 8.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF334155),
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )

                        ReceiptDashedDivider()

                        ReceiptItemRow("QTY", "ITEM STATISTIK", "NILAI", isHeader = true)
                        ReceiptDashedDivider()

                        ReceiptItemRow("01", "BRANKAS AKTIF (#$activeVaultId)", "$totalFiles FILE")
                        ReceiptItemRow("02", "TERKUNCI 3-LAPIS AES", "$encryptedFiles FILE")
                        ReceiptItemRow("03", "KAPASITAS TERSIMPAN", "${(totalBytes / 1024L).coerceAtLeast(1L)} KB")
                        ReceiptItemRow("04", "KATALOG BRAIN GAMES", "$totalBrainGames GAME")
                        ReceiptItemRow("05", "KOSTUM ANIME/HERO/FANTASI", "$totalOutfits SET")
                        ReceiptItemRow("06", "TIME CAPSULE AKTIF", "$activeCapsules SEGEL")
                        ReceiptItemRow("07", "STYLE UI AKTIF", activeTheme.displayName.uppercase())
                        ReceiptItemRow("08", "HOLO FOIL SHADER", ViralTrendsHubState.selectedHoloFoil.displayName.uppercase())

                        ReceiptDashedDivider()
                        ReceiptItemRow("", "SUBTOTAL FITUR", "RP 999.999")
                        ReceiptItemRow("", "DISKON VIP (100% GRATIS)", "-RP 999.999")
                        ReceiptItemRow("", "TOTAL BAYAR KOIN/XP", "RP 0 (FREE)", isHeader = true)
                        ReceiptDashedDivider()

                        Spacer(modifier = Modifier.height(4.dp))

                        // Vector Barcode at the bottom of the Receipt
                        Canvas(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(38.dp)
                                .padding(horizontal = 12.dp)
                        ) {
                            val w = size.width
                            val h = size.height
                            var x = 0f
                            var barIdx = 0
                            while (x < w) {
                                val barW = when (barIdx % 5) {
                                    0 -> 4f
                                    1 -> 1.8f
                                    2 -> 3.2f
                                    3 -> 1.4f
                                    else -> 2.4f
                                }
                                if (barIdx % 2 == 0) {
                                    drawRect(
                                        color = Color(0xFF0F172A),
                                        topLeft = Offset(x, 0f),
                                        size = Size(barW.coerceAtMost(w - x), h)
                                    )
                                }
                                x += barW + 1.6f
                                barIdx++
                            }
                        }

                        Text(
                            text = "*ARC-2026-VAULT-$activeVaultId-RECEIPTIFY*",
                            fontFamily = JetBrainsMonoFontFamily,
                            fontSize = 8.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF475569),
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )
                        Text(
                            text = "TERIMA KASIH TELAH MENJAGA PRIVASI DI ARCANUM!",
                            fontFamily = JetBrainsMonoFontFamily,
                            fontSize = 8.sp,
                            color = Color(0xFF64748B),
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    // Bottom Zig-Zag Torn Receipt Edge
                    ReceiptZigZagEdge(isTop = false)
                }
            }
        }
    }
}

@Composable
private fun WrappedStatBox(
    title: String,
    bigValue: String,
    subValue: String,
    accent: Color,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(14.dp))
            .background(Color.White.copy(alpha = 0.08f))
            .border(1.dp, accent.copy(alpha = 0.45f), RoundedCornerShape(14.dp))
            .padding(10.dp),
        verticalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        Text(
            text = title,
            fontFamily = JetBrainsMonoFontFamily,
            fontSize = 7.5.sp,
            fontWeight = FontWeight.Bold,
            color = accent
        )
        Text(
            text = bigValue,
            fontSize = 15.sp,
            fontWeight = FontWeight.ExtraBold,
            color = Color.White
        )
        Text(
            text = subValue,
            fontSize = 8.5.sp,
            color = Color(0xFFCBD5E1)
        )
    }
}

@Composable
private fun ReceiptItemRow(
    qty: String,
    item: String,
    price: String,
    isHeader: Boolean = false
) {
    val weight = if (isHeader) FontWeight.ExtraBold else FontWeight.SemiBold
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        if (qty.isNotEmpty()) {
            Text(
                text = qty,
                fontFamily = JetBrainsMonoFontFamily,
                fontSize = 9.5.sp,
                fontWeight = weight,
                color = Color(0xFF0F172A),
                modifier = Modifier.width(26.dp)
            )
        }
        Text(
            text = item,
            fontFamily = JetBrainsMonoFontFamily,
            fontSize = 9.5.sp,
            fontWeight = weight,
            color = Color(0xFF0F172A),
            modifier = Modifier.weight(1f),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Text(
            text = price,
            fontFamily = JetBrainsMonoFontFamily,
            fontSize = 9.5.sp,
            fontWeight = weight,
            color = Color(0xFF0F172A)
        )
    }
}

@Composable
private fun ReceiptDashedDivider() {
    Text(
        text = "--------------------------------------------",
        fontFamily = JetBrainsMonoFontFamily,
        fontSize = 9.sp,
        color = Color(0xFF94A3B8),
        maxLines = 1,
        overflow = TextOverflow.Clip,
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
private fun ReceiptZigZagEdge(isTop: Boolean) {
    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .height(8.dp)
    ) {
        val w = size.width
        val h = size.height
        val toothW = 14f
        val path = Path().apply {
            if (isTop) {
                moveTo(0f, 0f)
                var x = 0f
                while (x < w) {
                    lineTo(x + toothW * 0.5f, h)
                    lineTo((x + toothW).coerceAtMost(w), 0f)
                    x += toothW
                }
                close()
            } else {
                moveTo(0f, h)
                var x = 0f
                while (x < w) {
                    lineTo(x + toothW * 0.5f, 0f)
                    lineTo((x + toothW).coerceAtMost(w), h)
                    x += toothW
                }
                close()
            }
        }
        drawPath(path, color = Color(0xFFCBD5E1))
    }
}

// ============================================================================
// 5. PERSISTENT FLOATING DYNAMIC ISLAND PILL + ANIMATED MASCOT MINI-SPRITE
//    & LIVE STATUS NOTIFICATIONS CENTER
// ============================================================================

enum class DynamicIslandStatusCategory(
    val label: String,
    val accentColor: Color
) {
    NOTIFICATION("NOTIFIKASI", Color(0xFF22C55E)),
    DOWNLOAD("UNDUHAN MORI", Color(0xFF00F2FE)),
    SCANNER("PEMINDAI ZIP", Color(0xFFF59E0B)),
    STEGO_CRYPTO("KRIPTO & STEGO", Color(0xFFA855F7)),
    MEDIA("MEDIA PLAYER", Color(0xFFEC4899)),
    TIME_CAPSULE("TIME CAPSULE", Color(0xFF38BDF8)),
    SECURITY("STATUS KEAMANAN", Color(0xFF10B981))
}

data class DynamicIslandStatusNotification(
    val id: Long,
    val title: String,
    val message: String,
    val category: DynamicIslandStatusCategory,
    val timestampText: String
)

object DynamicIslandNotificationCenter {
    private var nextId = 1L
    val notifications = mutableStateListOf(
        DynamicIslandStatusNotification(
            id = nextId++,
            title = "Perisai AES-256-GCM Aktif",
            message = "Kapsul Dynamic Island memantau status brankas & maskot secara real-time.",
            category = DynamicIslandStatusCategory.SECURITY,
            timestampText = "Baru saja"
        )
    )

    var lastRecordedBanner: String? = null

    fun recordNotification(
        title: String,
        message: String,
        category: DynamicIslandStatusCategory = DynamicIslandStatusCategory.NOTIFICATION
    ) {
        if (message.isBlank()) return
        val timeStr = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date())
        notifications.add(
            0,
            DynamicIslandStatusNotification(
                id = nextId++,
                title = title,
                message = message,
                category = category,
                timestampText = timeStr
            )
        )
        while (notifications.size > 12) {
            notifications.removeAt(notifications.lastIndex)
        }
    }

    fun clearNotifications() {
        notifications.clear()
    }
}

/**
 * Dedicated Animated Mascot Mini-Sprite rendered inside the floating Dynamic Island pill.
 * Features continuous sprite bobbing, blinking eyes, waving arms, flapping wings/cape,
 * orbiting aura particles, and reactive expressions when notifications arrive.
 */
@Composable
fun AnimatedMascotMiniSprite(
    isExcitedAlert: Boolean,
    isWorkingBusy: Boolean,
    isPlayingMusic: Boolean,
    size: Dp = 30.dp,
    modifier: Modifier = Modifier
) {
    val activeOutfit = AppThemeController.mascotOutfit
    val activeSkin = AppThemeController.mascotSkin
    val activeAccessory = AppThemeController.mascotAccessory

    val infiniteTransition = rememberInfiniteTransition(label = "miniSpriteAnim")
    val animPhase by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 6.2831855f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = if (isExcitedAlert || isWorkingBusy) 650 else 1300,
                easing = LinearEasing
            ),
            repeatMode = RepeatMode.Restart
        ),
        label = "spritePhase"
    )
    val blinkCycle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 2800, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "blinkCycle"
    )

    val bobY = sin(animPhase) * (if (isExcitedAlert) 2.6f else 1.5f)
    val armWave = cos(animPhase * 2f) * (if (isExcitedAlert || isPlayingMusic) 3.5f else 1.6f)
    val isBlinking = blinkCycle > 0.92f

    Box(
        modifier = modifier
            .size(size)
            .testTag("dynamic_island_mascot_mini_sprite"),
        contentAlignment = Alignment.Center
    ) {
        // Base Full Mascot Avatar scaled inside the mini-sprite stage
        NyxMascotAvatar(
            mood = when {
                isExcitedAlert -> MascotMood.SUCCESS_LEAP
                isWorkingBusy -> MascotMood.DOWNLOAD_HUNTER
                isPlayingMusic -> MascotMood.HAPPY_VIBES
                else -> MascotMood.IDLE_BREATHE
            },
            persona = MascotPersona.COSMIC_DRAGON,
            size = size * 0.88f,
            modifier = Modifier.graphicsLayer {
                translationY = bobY
                rotationZ = if (isPlayingMusic) sin(animPhase) * 6f else 0f
            }
        )

        // Overlay Mini-Sprite Pixel/Vector Effects (Flapping Wing/Cape Silhouette, Blink/Sparkle & Orbiting Particles)
        Canvas(modifier = Modifier.fillMaxSize()) {
            val w = this.size.width
            val h = this.size.height
            val cx = w * 0.5f
            val cy = h * 0.5f + bobY

            // 1. Orbiting Mini Sprite Aura Sparkles
            val orbitR = w * 0.44f
            for (i in 0..2) {
                val angle = animPhase + i * 2.094f
                val sx = cx + cos(angle) * orbitR
                val sy = cy + sin(angle) * (orbitR * 0.65f)
                drawCircle(
                    color = if (i % 2 == 0) activeOutfit.badgeColor else Color(0xFFFACC15),
                    radius = 1.8f,
                    center = Offset(sx, sy)
                )
            }

            // 2. Mini Waving Sprite Paws / Weapon Glint
            drawCircle(
                color = activeOutfit.badgeColor,
                radius = w * 0.08f,
                center = Offset(w * 0.14f, cy + armWave)
            )
            drawCircle(
                color = Color.White,
                radius = w * 0.08f,
                center = Offset(w * 0.86f, cy - armWave)
            )

            // 3. Excited Notification Exclamation / Music Note / Cyber Scan Visor Glint
            if (isExcitedAlert) {
                drawCircle(
                    color = Color(0xFFFACC15),
                    radius = w * 0.11f,
                    center = Offset(w * 0.84f, h * 0.16f)
                )
            } else if (isBlinking) {
                drawLine(
                    color = Color.White.copy(alpha = 0.7f),
                    start = Offset(w * 0.34f, cy - h * 0.06f),
                    end = Offset(w * 0.66f, cy - h * 0.06f),
                    strokeWidth = 1.5f
                )
            }
        }
    }
}

/**
 * Path-based morphing Shape for the Dynamic Island container.
 * Dynamically adjusts top/bottom corner radii, continuous cubic Bézier superellipse curvature,
 * and aspect-ratio-coupled geometry as the container morphs from the compact 'island' pill state (0f)
 * through the notification banner toast state (~0.45f) to the full expanded Live Activity state (1f).
 */
class DynamicIslandPathMorphShape(
    private val morphProgress: Float,
    private val notificationExpandProgress: Float = 0f,
    private val targetAspectRatio: Float = 6.2f,
    private val shapeStyle: DynamicIslandShapeStyle = DynamicIslandShapeStyle.PILL_CAPSULE
) : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        val path = createDynamicIslandMorphPath(
            size = size,
            density = density.density,
            morphProgress = morphProgress,
            notificationExpandProgress = notificationExpandProgress,
            targetAspectRatio = targetAspectRatio,
            shapeStyle = shapeStyle
        )
        return Outline.Generic(path)
    }
}

fun createDynamicIslandMorphPath(
    size: Size,
    density: Float,
    morphProgress: Float,
    notificationExpandProgress: Float = 0f,
    targetAspectRatio: Float = 6.2f,
    shapeStyle: DynamicIslandShapeStyle = DynamicIslandShapeStyle.PILL_CAPSULE
): Path {
    val w = size.width.coerceAtLeast(1f)
    val h = size.height.coerceAtLeast(1f)
    val t = morphProgress.coerceIn(0f, 1f)
    val notifT = notificationExpandProgress.coerceIn(0f, 1f)

    // Live aspect ratio (width / height) coupled with targetAspectRatio:
    // Compact pill ~ 6.2:1 .. 7.2:1, Notification toast ~ 5.2:1, Expanded platter ~ 1.65:1 .. 2.25:1
    val liveAspectRatio = (w / h).coerceIn(1f, 12f)
    val blendedAspectRatio = liveAspectRatio * 0.65f + targetAspectRatio.coerceIn(1f, 12f) * 0.35f
    // Normalized aspect ratio factor: 0f = high-aspect compact pill, 1f = low-aspect expanded notification platter
    val aspectMorphFactor = ((6.4f - blendedAspectRatio) / 4.4f).coerceIn(0f, 1f)
    val effectiveMorph = (t * 0.68f + aspectMorphFactor * 0.20f + notifT * 0.12f).coerceIn(0f, 1f)

    return Path().apply {
        when (shapeStyle) {
            DynamicIslandShapeStyle.CYBER_HEXAGON -> {
                val cut = (h * (0.30f * (1f - effectiveMorph) + 0.08f * effectiveMorph)).coerceIn(8f, h * 0.45f)
                val wing = (12f * (1f - effectiveMorph) + 5f * effectiveMorph) * (density * 0.45f).coerceAtLeast(1f)
                moveTo(cut + wing, 0f)
                lineTo(w - cut - wing, 0f)
                lineTo(w, cut)
                lineTo(w - wing * 0.5f, h * 0.5f)
                lineTo(w, h - cut)
                lineTo(w - cut - wing, h)
                lineTo(cut + wing, h)
                lineTo(0f, h - cut)
                lineTo(wing * 0.5f, h * 0.5f)
                lineTo(0f, cut)
                close()
            }
            DynamicIslandShapeStyle.RETRO_CASSETTE_TAPE -> {
                val notch = (h * (0.18f * (1f - effectiveMorph) + 0.08f * effectiveMorph)).coerceIn(6f, 20f)
                moveTo(notch, 0f)
                lineTo(w - notch, 0f)
                lineTo(w, notch)
                lineTo(w, h - notch)
                lineTo(w - notch * 1.35f, h)
                lineTo(notch * 1.35f, h)
                lineTo(0f, h - notch)
                lineTo(0f, notch)
                close()
            }
            DynamicIslandShapeStyle.STEAMPUNK_BRASS_GAUGE -> {
                val step = (h * (0.20f * (1f - effectiveMorph) + 0.09f * effectiveMorph)).coerceIn(7f, 18f)
                moveTo(step, 0f)
                lineTo(w - step, 0f)
                lineTo(w - step, step * 0.6f)
                lineTo(w, step)
                lineTo(w, h - step)
                lineTo(w - step, h - step * 0.6f)
                lineTo(w - step, h)
                lineTo(step, h)
                lineTo(step, h - step * 0.6f)
                lineTo(0f, h - step)
                lineTo(0f, step)
                lineTo(step, step * 0.6f)
                close()
            }
            DynamicIslandShapeStyle.CRYSTAL_PRISM_SHARD -> {
                val tip = (h * (0.34f * (1f - effectiveMorph) + 0.08f * effectiveMorph)).coerceIn(10f, 26f)
                moveTo(tip, 0f)
                lineTo(w - tip, 0f)
                lineTo(w, h * 0.5f)
                lineTo(w - tip, h)
                lineTo(tip, h)
                lineTo(0f, h * 0.5f)
                close()
            }
            else -> {
                // Continuous Path-based superellipse / squircle morphing contour:
                // Small 'island' state -> pure stadium pill (r = h / 2).
                // Notification banner & Expanded notification state -> iOS 18 G2 squircle (34dp..38dp)
                // with independent top and bottom corner radius adaptation and mid-transition liquid tension.
                val halfMin = (w.coerceAtMost(h) * 0.5f).coerceAtLeast(1f)
                val pillRadius = halfMin
                val notifTopRadius = (24f * density).coerceAtMost(halfMin)
                val notifBottomRadius = (28f * density).coerceAtMost(halfMin)
                val expandedTopRadius = when (shapeStyle) {
                    DynamicIslandShapeStyle.TOP_NOTCH_DOCK -> (10f * density).coerceAtMost(halfMin)
                    DynamicIslandShapeStyle.BENTO_CHAMFER_BOX -> (20f * density).coerceAtMost(halfMin)
                    else -> (36f * density).coerceAtMost(halfMin)
                }
                val expandedBottomRadius = when (shapeStyle) {
                    DynamicIslandShapeStyle.BENTO_CHAMFER_BOX -> (24f * density).coerceAtMost(halfMin)
                    DynamicIslandShapeStyle.ORGANIC_LEAF_POD -> (30f * density).coerceAtMost(halfMin)
                    else -> (38f * density).coerceAtMost(halfMin)
                }

                // Interpolate top & bottom corner radii smoothly across island -> notification -> expanded card
                val baseTopR = pillRadius * (1f - effectiveMorph) + expandedTopRadius * effectiveMorph
                val baseBottomR = pillRadius * (1f - effectiveMorph) + expandedBottomRadius * effectiveMorph
                val topR = (baseTopR * (1f - notifT * (1f - t) * 0.35f) +
                    notifTopRadius * (notifT * (1f - t) * 0.35f)).coerceIn(4f, halfMin)
                val bottomR = (baseBottomR * (1f - notifT * (1f - t) * 0.35f) +
                    notifBottomRadius * (notifT * (1f - t) * 0.35f)).coerceIn(4f, halfMin)

                // Cubic Bézier handle ratio:
                // 0.55228f = true circular capsule arc in compact 'island' mode;
                // 0.675f = Apple iOS 18 continuous superellipse squircle in expanded notification mode.
                val handleKappa = 0.5522848f + (0.675f - 0.5522848f) * effectiveMorph

                // Subtle liquid surface-tension bow during mid-morph (peaks at t = 0.5)
                val midMorphImpulse = sin(effectiveMorph * Math.PI.toFloat())
                val bottomLiquidSag = midMorphImpulse * (2.2f * density).coerceAtMost(h * 0.04f)

                val tr = topR.coerceAtMost(w * 0.5f).coerceAtMost(h * 0.5f)
                val br = bottomR.coerceAtMost(w * 0.5f).coerceAtMost(h * 0.5f)
                val kt = tr * handleKappa
                val kb = br * handleKappa

                // Start at top-left straight segment start
                moveTo(tr, 0f)
                // Top edge
                lineTo(w - tr, 0f)
                // Top-right corner via continuous cubic Bézier
                cubicTo(
                    w - tr + kt, 0f,
                    w, tr - kt,
                    w, tr
                )
                // Right edge
                lineTo(w, h - br)
                // Bottom-right corner via continuous cubic Bézier
                cubicTo(
                    w, h - br + kb,
                    w - br + kb, h,
                    w - br, h
                )
                // Bottom edge with subtle liquid morph tension
                if (bottomLiquidSag > 0.1f) {
                    cubicTo(
                        w * 0.68f, h + bottomLiquidSag * 0.25f,
                        w * 0.32f, h + bottomLiquidSag * 0.25f,
                        br, h
                    )
                } else {
                    lineTo(br, h)
                }
                // Bottom-left corner via continuous cubic Bézier
                cubicTo(
                    br - kb, h,
                    0f, h - br + kb,
                    0f, h - br
                )
                // Left edge
                lineTo(0f, tr)
                // Top-left corner via continuous cubic Bézier
                cubicTo(
                    0f, tr - kt,
                    tr - kt, 0f,
                    tr, 0f
                )
                close()
            }
        }
    }
}

fun buildDynamicIslandShape(
    shapeStyle: DynamicIslandShapeStyle,
    isExpanded: Boolean,
    morphProgress: Float = if (isExpanded) 1f else 0f,
    notificationExpandProgress: Float = 0f,
    targetAspectRatio: Float = if (isExpanded) 1.85f else 6.2f
): Shape {
    return DynamicIslandPathMorphShape(
        morphProgress = morphProgress,
        notificationExpandProgress = notificationExpandProgress,
        targetAspectRatio = targetAspectRatio,
        shapeStyle = shapeStyle
    )
}

@Composable
fun LegacyVaultStatusBanner(
    statusBannerMessage: String?,
    isDownloading: Boolean = false,
    downloadProgressPercent: Int = 0,
    downloadStatusText: String = "",
    isScanningZip: Boolean = false,
    scanStatusText: String = "",
    isProcessingStegoOrCrypto: Boolean = false,
    stegoStatusText: String = "",
    isDarkMode: Boolean = false,
    onDismissStatusBanner: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val activeMessage = when {
        !statusBannerMessage.isNullOrBlank() -> statusBannerMessage
        isScanningZip -> scanStatusText.ifBlank { "Memindai arsip ZIP..." }
        isDownloading -> downloadStatusText.ifBlank { "Mengunduh media ($downloadProgressPercent%)..." }
        isProcessingStegoOrCrypto -> stegoStatusText.ifBlank { "Memproses Steganografi & Kriptografi..." }
        else -> null
    }
    AnimatedVisibility(
        visible = !activeMessage.isNullOrBlank(),
        enter = expandVertically() + fadeIn(),
        exit = shrinkVertically() + fadeOut(),
        modifier = modifier
    ) {
        val bgColor = if (isDarkMode) Color(0xFF131A2B) else Color(0xFFF0FDF4)
        val borderColor = if (isDarkMode) Color(0xFF38BDF8) else Color(0xFF22C55E)
        val titleColor = if (isDarkMode) Color(0xFF38BDF8) else Color(0xFF15803D)
        val textColor = if (isDarkMode) Color(0xFFF1F5F9) else Color(0xFF0F172A)

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 6.dp)
                .clip(RoundedCornerShape(14.dp))
                .background(bgColor)
                .border(1.2.dp, borderColor.copy(alpha = 0.7f), RoundedCornerShape(14.dp))
                .padding(horizontal = 12.dp, vertical = 9.dp)
                .testTag("legacy_status_notification_banner"),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(28.dp)
                    .clip(CircleShape)
                    .background(borderColor.copy(alpha = 0.16f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Notifications,
                    contentDescription = "Notifikasi Sistem",
                    tint = titleColor,
                    modifier = Modifier.size(16.dp)
                )
            }
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Notifikasi Arcanum",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = titleColor
                )
                Text(
                    text = activeMessage ?: "",
                    fontSize = 11.5.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = textColor,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
            if (!statusBannerMessage.isNullOrBlank()) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Tutup Notifikasi",
                    tint = textColor.copy(alpha = 0.7f),
                    modifier = Modifier
                        .size(20.dp)
                        .clickable { onDismissStatusBanner() }
                        .testTag("legacy_status_dismiss_button")
                )
            }
        }
    }
}

@Composable
fun InteractiveDynamicIslandCapsule(
    activeVaultId: Int,
    allItems: List<VaultItemEntity>,
    isDownloading: Boolean,
    downloadProgressPercent: Int,
    downloadStatusText: String = "",
    isScanningZip: Boolean = false,
    scanStatusText: String = "",
    isProcessingStegoOrCrypto: Boolean = false,
    stegoStatusText: String = "",
    isPlayingMedia: Boolean = false,
    statusBannerMessage: String? = null,
    onDismissStatusBanner: () -> Unit = {},
    isUnlocked: Boolean = true,
    authStageLabel: String = "BRANKAS AKTIF",
    onOpenThemeStudioClick: () -> Unit = {},
    onOpenBrainGamesClick: () -> Unit = {},
    onOpenSanctuaryClick: () -> Unit = {},
    onPostBanner: (String) -> Unit = {},
    modifier: Modifier = Modifier
) {
    // 0 = Ultra-Mini iOS Pill (136dp x 28dp, Image 1 top / Image 4 & 5)
    // 1 = iOS Glowing Energy & Live Strip (292dp x 48dp, Image 1 bottom / Image 3 & 5 strips)
    // 2 = Expanded iOS Live Activity Widget Platter (340dp squircle, Images 1-6)
    var islandStage by remember { mutableIntStateOf(0) }
    val isExpanded = islandStage == 2
    val isEnergyStripMode = islandStage == 1

    // Compact size preset: 0 = Nano (106dp x 25dp), 1 = iOS Mini (134dp x 28dp, Default), 2 = Split Droplet (122dp + 28dp circle, Image 5)
    val compactSizePreset = AppThemeController.proDynamicIslandCompactPreset
    // Active compact style in Mini Pill: 0 = Energy Bolt 76 (Image 1), 1 = Spotify/Audio Wave (Image 4), 2 = Timer 0:22 (Image 5), 3 = Heart 82 bpm (Image 3)
    val compactMiniStyle = AppThemeController.proDynamicIslandMiniStyle
    // Active widget visual inside Expanded Platter: 0 = Energy Glow (Img 1), 1 = Equalizer Spectrum (Img 4), 2 = Activity Rings (Img 6), 3 = Sparkline Chart (Img 2)
    var expandedWidgetTab by remember { mutableIntStateOf(0) }

    // null = closed, 0 = Photobooth 3D, 1 = Time Capsule, 2 = Arcanum Wrapped & Receiptify
    var activeModalTab by remember { mutableStateOf<Int?>(null) }
    var showMicroFeaturesHubModal by remember { mutableStateOf(false) }
    var selectedMicroFeatureInitial by remember { mutableIntStateOf(1) }

    // Record incoming statusBannerMessage into DynamicIslandNotificationCenter
    LaunchedEffect(statusBannerMessage) {
        if (!statusBannerMessage.isNullOrBlank() &&
            statusBannerMessage != DynamicIslandNotificationCenter.lastRecordedBanner
        ) {
            DynamicIslandNotificationCenter.lastRecordedBanner = statusBannerMessage
            DynamicIslandNotificationCenter.recordNotification(
                title = "Notifikasi Sistem Arcanum",
                message = statusBannerMessage,
                category = DynamicIslandStatusCategory.NOTIFICATION
            )
        }
    }

    var nowMillis by remember { mutableLongStateOf(System.currentTimeMillis()) }
    LaunchedEffect(Unit) {
        while (true) {
            delay(1000L)
            nowMillis = System.currentTimeMillis()
        }
    }

    val activeOutfit = AppThemeController.mascotOutfit
    val islandShapeStyle = AppThemeController.dynamicIslandShape
    val isFolded = AppThemeController.isDynamicIslandFolded
    val isSanctuaryDarkMode = AppThemeController.isSanctuaryBentoGlowLocked
    val islandBorderColor = if (isSanctuaryDarkMode) Color(0xFF38BDF8) else AppThemeController.resolvedIslandBorderColor
    val islandGlowColor = if (isSanctuaryDarkMode) Color(0xFF6366F1) else AppThemeController.resolvedIslandGlowColor

    val nearestCapsule = VaultTimeCapsuleRepository.capsules
        .filter { !it.isOpened }
        .minByOrNull { it.unlockAtMillis }
    val nearestRemainingMs = nearestCapsule?.let { (it.unlockAtMillis - nowMillis).coerceAtLeast(0L) }

    val pulseTransition = rememberInfiniteTransition(label = "dynamicIslandPulse")
    val eqWave by pulseTransition.animateFloat(
        initialValue = 0.28f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 620, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "eqWave"
    )
    val glowPulse by pulseTransition.animateFloat(
        initialValue = 0.65f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1100, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "glowPulse"
    )
    val ornamentSpinDeg by pulseTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 6000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "ornamentSpinDeg"
    )

    val hasActiveBanner = !statusBannerMessage.isNullOrBlank()
    val isBusyWorking = isDownloading || isScanningZip || isProcessingStegoOrCrypto

    // Determine primary title, live status notification text, and dynamic rim color
    val (islandHeaderTitle, liveStatusNotificationText, activeStatusAccent) = when {
        hasActiveBanner -> Triple(
            "NOTIFIKASI BARU",
            statusBannerMessage ?: "",
            Color(0xFF30D158)
        )
        isScanningZip -> Triple(
            "PEMINDAI ZIP AKTIF",
            scanStatusText.ifBlank { "Memindai arsip dari ancaman..." },
            Color(0xFFFF9F0A)
        )
        isDownloading -> Triple(
            "MORI STREAM ($downloadProgressPercent%)",
            downloadStatusText.ifBlank { "Mengunduh & mengenkripsi media..." },
            Color(0xFF30D158)
        )
        isProcessingStegoOrCrypto -> Triple(
            "MESIN STEGO & KRIPTO",
            stegoStatusText.ifBlank { "Memproses enkripsi 3-Lapis..." },
            islandGlowColor
        )
        isPlayingMedia -> Triple(
            "MEDIA PLAYER BRANKAS",
            "Memutar media terenkripsi secara aman",
            Color(0xFFFF375F)
        )
        !isUnlocked -> Triple(
            "GERBANG KEAMANAN 3-LAPIS",
            authStageLabel,
            Color(0xFFFF9F0A)
        )
        isSanctuaryDarkMode -> Triple(
            "BLACKBOX SANCTUARY • PIN 444",
            "Full Dark Mode • Tabungan & Goals Box Chart",
            Color(0xFF38BDF8)
        )
        nearestRemainingMs != null && nearestRemainingMs > 0L -> {
            val totalSec = nearestRemainingMs / 1000L
            val m = (totalSec % 3600L) / 60L
            val s = totalSec % 60L
            Triple(
                "${activeOutfit.displayName} • Brankas #$activeVaultId",
                "${nearestCapsule.title} (${String.format(Locale.US, "%02dm:%02dd", m, s)}) • ${allItems.size} File",
                Color(0xFF30D158)
            )
        }
        nearestRemainingMs != null && nearestRemainingMs == 0L -> Triple(
            "TIME CAPSULE SIAP DIBUKA!",
            "Segel '${nearestCapsule.title}' telah selesai dihitung mundur",
            Color(0xFF30D158)
        )
        else -> Triple(
            "${activeOutfit.displayName} • Brankas #$activeVaultId",
            "${allItems.count { it.isEncrypted }}/${allItems.size} File • 120+ Fitur Aktif",
            Color(0xFF30D158)
        )
    }

    // GSAP Macro-Interaction & Micro-Interaction state drivers (Squash, Stretch, Liquid Pop):
    var dragStretchX by remember { mutableFloatStateOf(1f) }
    var dragStretchY by remember { mutableFloatStateOf(1f) }
    var tapImpulseNonce by remember { mutableIntStateOf(0) }
    val tapElasticScaleX = remember { androidx.compose.animation.core.Animatable(1f) }
    val tapElasticScaleY = remember { androidx.compose.animation.core.Animatable(1f) }

    // Auto-Minimal Rule: Always return Dynamic Island to its smallest minimal pill (isDynamicIslandFolded = true)
    // whenever untouched for 4 seconds AND no active notification or background task from Arcanum.
    LaunchedEffect(
        tapImpulseNonce,
        islandStage,
        isFolded,
        hasActiveBanner,
        isBusyWorking,
        isPlayingMedia,
        activeModalTab,
        showMicroFeaturesHubModal
    ) {
        val hasOpenModal = activeModalTab != null || showMicroFeaturesHubModal
        if (!hasActiveBanner && !isBusyWorking && !isPlayingMedia && !hasOpenModal) {
            if (!isFolded || islandStage != 0) {
                delay(4000L)
                islandStage = 0
                AppThemeController.isDynamicIslandFolded = true
            }
        }
    }

    LaunchedEffect(tapImpulseNonce, islandStage, hasActiveBanner, compactSizePreset, compactMiniStyle) {
        val boost = (AppThemeController.graphicsQualityMode.effectMultiplier.toFloat() / 8f).coerceIn(0.4f, 1.5f)
        val startX = when (islandStage) {
            2 -> 0.88f
            1 -> 1.14f
            else -> 0.86f
        }
        val startY = when (islandStage) {
            2 -> 1.12f
            1 -> 0.88f
            else -> 1.14f
        }
        tapElasticScaleX.snapTo(1f + (startX - 1f) * boost.coerceAtMost(1.35f))
        tapElasticScaleY.snapTo(1f + (startY - 1f) * boost.coerceAtMost(1.35f))
        launch {
            tapElasticScaleX.animateTo(
                targetValue = 1f,
                animationSpec = tween(
                    durationMillis = GsapEase.effectiveDurationMs(620),
                    easing = GsapEase.elasticOut(1.18f, 0.28f)
                )
            )
        }
        launch {
            tapElasticScaleY.animateTo(
                targetValue = 1f,
                animationSpec = tween(
                    durationMillis = GsapEase.effectiveDurationMs(620),
                    easing = GsapEase.elasticOut(1.18f, 0.28f)
                )
            )
        }
    }

    val targetMorphProgress = when {
        isExpanded -> 1.0f
        hasActiveBanner -> 0.46f
        isEnergyStripMode -> 0.36f
        isBusyWorking || isPlayingMedia -> 0.12f
        else -> 0.0f
    }
    val animatedMorphProgress by animateFloatAsState(
        targetValue = targetMorphProgress,
        animationSpec = spring(
            dampingRatio = 0.68f,
            stiffness = 380f
        ),
        label = "islandMorphProgress"
    )
    val animatedNotificationExpand by animateFloatAsState(
        targetValue = if (hasActiveBanner || isExpanded || isEnergyStripMode) 1f else 0f,
        animationSpec = spring(
            dampingRatio = 0.68f,
            stiffness = 390f
        ),
        label = "islandNotifExpandProgress"
    )

    // Ultra-compact default widths matching Image 1 (134dp x 28dp) and Nano mode (104dp x 25dp)
    val baseCompactWidthDp = when (compactSizePreset) {
        0 -> 104.dp // Nano Pill
        2 -> 118.dp // Split Droplet Main Pill (plus 28dp detached droplet on right)
        else -> 134.dp // Authentic iOS Mini Pill (Image 1 top)
    }
    val baseCompactHeightDp = when (compactSizePreset) {
        0 -> 25.dp
        else -> 28.dp
    }

    val animatedMinWidthDp by animateDpAsState(
        targetValue = when {
            isExpanded -> 338.dp
            hasActiveBanner -> 296.dp
            isEnergyStripMode -> 288.dp
            isBusyWorking || isPlayingMedia -> 168.dp
            else -> baseCompactWidthDp
        },
        animationSpec = spring(
            dampingRatio = 0.66f,
            stiffness = 380f
        ),
        label = "islandMinWidthDp"
    )
    val animatedMaxWidthDp by animateDpAsState(
        targetValue = when {
            isExpanded -> 480.dp
            hasActiveBanner -> 356.dp
            isEnergyStripMode -> 332.dp
            isBusyWorking || isPlayingMedia -> 210.dp
            else -> baseCompactWidthDp + 18.dp
        },
        animationSpec = spring(
            dampingRatio = 0.66f,
            stiffness = 380f
        ),
        label = "islandMaxWidthDp"
    )

    val currentIslandShape = remember(
        islandShapeStyle,
        animatedMorphProgress,
        animatedNotificationExpand,
        isExpanded
    ) {
        if (!isExpanded) {
            RoundedCornerShape(50)
        } else {
            RoundedCornerShape(28.dp)
        }
    }

    // Authentic iOS 18 OLED Black & System Accent Tokens (matching Reference Images 1-6)
    val iosOledBlack = Color(0xFF000000)
    val iosPlatterDark = Color(0xFF141416)
    val iosPlatterElevated = Color(0xFF242428)
    val iosBlue = Color(0xFF0A84FF)
    val iosGreen = Color(0xFF32D74B) // Exact iOS Charging Neon Green from Image 1
    val iosAmber = Color(0xFFFF9F0A)
    val iosPink = Color(0xFFFF375F)
    val iosPurple = Color(0xFFBF5AF2)
    val iosYellow = Color(0xFFFFD60A)
    val iosCyan = Color(0xFF64D2FF)
    val iosSubtleLabel = Color(0xFF8E8E93)

    val privacyDotColor = when {
        hasActiveBanner -> iosGreen
        isBusyWorking -> iosAmber
        isPlayingMedia -> iosPink
        isUnlocked -> iosGreen
        else -> iosAmber
    }

    val encryptedCount = allItems.count { it.isEncrypted }
    val animatedEncryptedCount = rememberGsapRollingLong(encryptedCount.toLong())
    val animatedTotalFilesCount = rememberGsapRollingLong(allItems.size.toLong())

    // Calculate live Energy/Protection Percentage (like "76" in Image 1)
    val energyPercentValue = when {
        isDownloading -> downloadProgressPercent.coerceIn(1, 100)
        allItems.isEmpty() -> 76 // Signature 76% from Image 1 when vault is empty/default
        else -> ((encryptedCount * 100) / allItems.size.coerceAtLeast(1)).coerceIn(76, 100)
    }
    val animatedEnergyPercent = rememberGsapRollingLong(energyPercentValue.toLong())
    val energyProgressFraction = rememberGsapRollingFloat((energyPercentValue / 100f).coerceIn(0.18f, 1f))

    val compactStatusSummaryText = when {
        hasActiveBanner -> statusBannerMessage ?: "Notifikasi Baru"
        isScanningZip -> "ZIP Scan"
        isDownloading -> "Mori $downloadProgressPercent%"
        isProcessingStegoOrCrypto -> "Stego Aktif"
        isPlayingMedia -> "Audio Aktif"
        !isUnlocked -> "Terkunci"
        else -> "Brankas #$activeVaultId • ${energyPercentValue}%"
    }

    // If folded into ultra-nano iOS camera pill (96dp x 24dp), show smallest minimal cutout at top center
    // Automatically expands whenever there is an active Arcanum notification/task or user touch
    if (isFolded && !hasActiveBanner && !isBusyWorking && !isPlayingMedia && islandStage == 0) {
        Box(
            modifier = modifier
                .fillMaxWidth()
                .background(if (isSanctuaryDarkMode) Color(0xFF050811) else Color.Transparent)
                .padding(vertical = 3.dp),
            contentAlignment = Alignment.TopCenter
        ) {
            Row(
                modifier = Modifier
                    .height(24.dp)
                    .width(96.dp)
                    .shadow(8.dp, RoundedCornerShape(50))
                    .clip(RoundedCornerShape(50))
                    .background(iosOledBlack)
                    .border(0.6.dp, Color.White.copy(alpha = 0.14f), RoundedCornerShape(50))
                    .gsapMagneticMicroTouch(
                        maxMagneticShiftDp = 3.dp,
                        maxTiltDegrees = 8f,
                        pressScale = 0.88f,
                        burstColor = iosGreen,
                        onClick = {
                            tapImpulseNonce++
                            AppThemeController.isDynamicIslandFolded = false
                            islandStage = 1
                        }
                    )
                    .padding(horizontal = 10.dp)
                    .testTag("dynamic_island_folded_ribbon"),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Icon(
                    imageVector = Icons.Default.Bolt,
                    contentDescription = "Buka Dynamic Island",
                    tint = iosGreen,
                    modifier = Modifier.size(13.dp)
                )
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF0B0F1A))
                        .border(0.8.dp, Color(0xFF1E293B), CircleShape)
                )
                Text(
                    text = "$animatedEnergyPercent",
                    fontFamily = JetBrainsMonoFontFamily,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = iosGreen
                )
            }
        }
        return
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(if (isSanctuaryDarkMode) Color(0xFF050811) else Color.Transparent)
            .padding(horizontal = 12.dp, vertical = 3.dp),
        contentAlignment = Alignment.TopCenter
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            // =========================================================================
            // MAIN iOS DYNAMIC ISLAND CAPSULE (Ultra-Mini Pill -> Energy Strip -> Widget Platter)
            // =========================================================================
            Box(
                modifier = Modifier
                    .then(
                        if (isExpanded) {
                            Modifier
                                .fillMaxWidth()
                                .widthIn(min = animatedMinWidthDp, max = animatedMaxWidthDp)
                        } else {
                            Modifier.widthIn(min = animatedMinWidthDp, max = animatedMaxWidthDp)
                        }
                    )
                    .gsapMacroElasticGesture(
                        isExpanded = isExpanded,
                        onExpandMacro = {
                            tapImpulseNonce++
                            islandStage = 2
                        },
                        onCollapseMacro = {
                            tapImpulseNonce++
                            islandStage = 0
                        },
                        onDragTensionChanged = { sx, sy ->
                            dragStretchX = sx
                            dragStretchY = sy
                        }
                    )
                    .graphicsLayer {
                        scaleX = tapElasticScaleX.value * dragStretchX
                        scaleY = tapElasticScaleY.value * dragStretchY
                    }
                    .shadow(
                        elevation = (10f + 10f * animatedMorphProgress).dp,
                        shape = currentIslandShape,
                        ambientColor = Color.Black,
                        spotColor = if (isEnergyStripMode) iosGreen else Color.Black
                    )
                    .clip(currentIslandShape)
                    .background(iosOledBlack)
                    .border(
                        width = if (isExpanded || isEnergyStripMode) 0.9.dp else 0.65.dp,
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                Color.White.copy(alpha = if (isExpanded) 0.22f else 0.13f),
                                if (isEnergyStripMode) iosGreen.copy(alpha = 0.35f)
                                else if (hasActiveBanner || isBusyWorking) activeStatusAccent.copy(alpha = 0.42f)
                                else Color.White.copy(alpha = 0.05f)
                            )
                        ),
                        shape = currentIslandShape
                    )
                    .animateContentSize(
                        animationSpec = spring(
                            dampingRatio = 0.68f,
                            stiffness = 380f
                        )
                    )
                    .gsapMagneticMicroTouch(
                        maxMagneticShiftDp = 3.5.dp,
                        maxTiltDegrees = 8f,
                        pressScale = 0.93f,
                        burstColor = iosGreen,
                        onClick = {
                            tapImpulseNonce++
                            // Cycle smoothly: Mini Pill (0) -> Energy Glow Strip (1) -> Full Live Activity Platter (2) -> Mini Pill (0)
                            islandStage = when (islandStage) {
                                0 -> 1
                                1 -> 2
                                else -> 0
                            }
                        }
                    )
                    .testTag("dynamic_island_capsule")
            ) {
                // Macro-Interaction Choreographed Content Morph between Compact Pill, Energy Strip, Notification & Platter
                val islandDisplayMode = when {
                    isExpanded -> 3
                    hasActiveBanner -> 2
                    isEnergyStripMode -> 1
                    else -> 0
                }
                GsapSharedMorphContainer(
                    targetState = islandDisplayMode,
                    label = "dynamic_island_macro_state_morph"
                ) { currentMode ->
                    when (currentMode) {
                        2 -> {
                            // =========================================================================
                            // STATE 2: iOS LIVE ACTIVITY NOTIFICATION BANNER TOAST
                            // =========================================================================
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 12.dp, vertical = 7.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(9.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(24.dp)
                                        .clip(CircleShape)
                                        .background(iosGreen.copy(alpha = 0.18f)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Bolt,
                                        contentDescription = null,
                                        tint = iosGreen,
                                        modifier = Modifier.size(15.dp)
                                    )
                                }

                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = islandHeaderTitle,
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = iosGreen,
                                        maxLines = 1
                                    )
                                    Text(
                                        text = liveStatusNotificationText,
                                        fontSize = 10.5.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = Color.White,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis,
                                        modifier = Modifier.testTag("dynamic_island_status_text")
                                    )
                                }

                                Row(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(50))
                                        .background(iosPlatterElevated)
                                        .gsapMagneticMicroTouch(
                                            maxMagneticShiftDp = 2.5.dp,
                                            pressScale = 0.90f,
                                            burstColor = iosGreen,
                                            onClick = {
                                                onDismissStatusBanner()
                                                islandStage = 0
                                                AppThemeController.isDynamicIslandFolded = true
                                            }
                                        )
                                        .padding(horizontal = 9.dp, vertical = 4.dp)
                                        .testTag("dynamic_island_dismiss_banner_button"),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(3.dp)
                                ) {
                                    GsapMicroIconBounce(
                                        icon = Icons.Default.Close,
                                        contentDescription = "Tutup Notifikasi",
                                        tint = Color.White,
                                        activeTrigger = statusBannerMessage,
                                        iconSize = 10.dp
                                    )
                                    Text(
                                        text = "Tutup",
                                        fontSize = 9.5.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = Color.White
                                    )
                                }
                            }
                        }

                        1 -> {
                            // =========================================================================
                            // STATE 1: REPLICA OF IMAGE 1 BOTTOM CAPSULE (GLOWING GREEN ENERGY BAR + ⚡76 PILL + ~15m)
                            // =========================================================================
                            Column(
                                modifier = Modifier
                                    .width(292.dp)
                                    .padding(horizontal = 14.dp, vertical = 9.dp),
                                verticalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                                ) {
                                    // Glowing Green Progress Track + Neon Pill Thumb (⚡76) with Radial Halo
                                    Box(
                                        modifier = Modifier
                                            .weight(1f)
                                            .height(24.dp),
                                        contentAlignment = Alignment.CenterStart
                                    ) {
                                        // Background dark track
                                        Box(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .height(6.dp)
                                                .clip(RoundedCornerShape(50))
                                                .background(Color(0xFF262629))
                                        )
                                        // Active Neon Green Bar
                                        val clampedFrac = energyProgressFraction.coerceIn(0.25f, 0.88f)
                                        Box(
                                            modifier = Modifier
                                                .fillMaxWidth(clampedFrac)
                                                .height(6.dp)
                                                .clip(RoundedCornerShape(50))
                                                .background(
                                                    Brush.horizontalGradient(
                                                        listOf(Color(0xFF28CD41), iosGreen, Color(0xFF66FF7F))
                                                    )
                                                )
                                        )
                                        // Radial Neon Green Glow Halo + ⚡76 Pill Thumb positioned along the bar
                                        Row(
                                            modifier = Modifier.fillMaxWidth(clampedFrac),
                                            horizontalArrangement = Arrangement.End,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Box(contentAlignment = Alignment.Center) {
                                                // Soft radial green glow halo (exact Image 1 effect)
                                                Canvas(modifier = Modifier.size(44.dp, 26.dp)) {
                                                    drawCircle(
                                                        brush = Brush.radialGradient(
                                                            colors = listOf(
                                                                iosGreen.copy(alpha = 0.58f * glowPulse),
                                                                iosGreen.copy(alpha = 0.20f * glowPulse),
                                                                Color.Transparent
                                                            )
                                                        ),
                                                        radius = size.maxDimension * 0.62f
                                                    )
                                                }
                                                Row(
                                                    modifier = Modifier
                                                        .clip(RoundedCornerShape(50))
                                                        .background(iosGreen)
                                                        .border(0.8.dp, Color.White.copy(alpha = 0.45f), RoundedCornerShape(50))
                                                        .padding(horizontal = 6.dp, vertical = 2.dp),
                                                    verticalAlignment = Alignment.CenterVertically,
                                                    horizontalArrangement = Arrangement.spacedBy(1.dp)
                                                ) {
                                                    Icon(
                                                        imageVector = Icons.Default.Bolt,
                                                        contentDescription = null,
                                                        tint = Color.White,
                                                        modifier = Modifier.size(11.dp)
                                                    )
                                                    Text(
                                                        text = "$animatedEnergyPercent",
                                                        fontFamily = JetBrainsMonoFontFamily,
                                                        fontSize = 10.sp,
                                                        fontWeight = FontWeight.ExtraBold,
                                                        color = Color.White
                                                    )
                                                }
                                            }
                                        }
                                    }

                                    // Right "~15m" Neon Green Label (exact Image 1 typography)
                                    Text(
                                        text = if (isDownloading) "${100 - downloadProgressPercent}s" else "~15m",
                                        fontFamily = JetBrainsMonoFontFamily,
                                        fontSize = 11.5.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = iosGreen,
                                        modifier = Modifier.testTag("dynamic_island_status_text")
                                    )
                                }

                                // Micro Quick-Action Row to jump to Full Widget Platter or Shrink back to Mini Pill
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = compactStatusSummaryText,
                                        fontSize = 8.5.sp,
                                        color = iosSubtleLabel,
                                        maxLines = 1
                                    )
                                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                        Text(
                                            text = "Perkecil",
                                            fontSize = 8.5.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = iosCyan,
                                            modifier = Modifier
                                                .clip(RoundedCornerShape(50))
                                                .background(Color.White.copy(alpha = 0.08f))
                                                .clickable {
                                                    tapImpulseNonce++
                                                    islandStage = 0
                                                }
                                                .padding(horizontal = 7.dp, vertical = 2.dp)
                                        )
                                        Text(
                                            text = "Buka Widget +",
                                            fontSize = 8.5.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = iosGreen,
                                            modifier = Modifier
                                                .clip(RoundedCornerShape(50))
                                                .background(iosGreen.copy(alpha = 0.14f))
                                                .clickable {
                                                    tapImpulseNonce++
                                                    islandStage = 2
                                                }
                                                .padding(horizontal = 7.dp, vertical = 2.dp)
                                        )
                                    }
                                }
                            }
                        }

                        0 -> {
                            // =========================================================================
                            // STATE 0: ULTRA-COMPACT iOS 18 MINI PILL (REPLICA OF IMAGE 1 TOP, IMAGE 4 & IMAGE 5)
                            // Default: 134dp x 28dp (or 104dp x 25dp Nano) — Pure #000000 Pitch Black!
                            // =========================================================================
                            Row(
                                modifier = Modifier
                                    .height(baseCompactHeightDp)
                                    .width(
                                        if (isBusyWorking || isPlayingMedia) 172.dp
                                        else baseCompactWidthDp
                                    )
                                    .padding(horizontal = 10.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                // LEFT SLOT: Clean iOS Live Icon (Green Bolt ⚡ / Spotify Wave / Timer Ring / Heart)
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    when {
                                        isScanningZip || isProcessingStegoOrCrypto -> {
                                            Icon(
                                                imageVector = Icons.Default.Security,
                                                contentDescription = null,
                                                tint = iosAmber,
                                                modifier = Modifier.size(13.dp)
                                            )
                                        }
                                        isPlayingMedia || compactMiniStyle == 1 -> {
                                            Box(
                                                modifier = Modifier
                                                    .size(15.dp)
                                                    .clip(CircleShape)
                                                    .background(iosGreen.copy(alpha = 0.22f)),
                                                contentAlignment = Alignment.Center
                                            ) {
                                                Icon(
                                                    imageVector = Icons.Default.GraphicEq,
                                                    contentDescription = null,
                                                    tint = iosGreen,
                                                    modifier = Modifier.size(11.dp)
                                                )
                                            }
                                        }
                                        compactMiniStyle == 2 -> {
                                            Icon(
                                                imageVector = Icons.Default.Timer,
                                                contentDescription = null,
                                                tint = iosAmber,
                                                modifier = Modifier.size(13.dp)
                                            )
                                        }
                                        compactMiniStyle == 3 -> {
                                            Icon(
                                                imageVector = Icons.Default.Favorite,
                                                contentDescription = null,
                                                tint = iosPink,
                                                modifier = Modifier.size(13.dp)
                                            )
                                        }
                                        else -> {
                                            // Exact Image 1 Top Left Neon-Green Lightning Bolt!
                                            Icon(
                                                imageVector = Icons.Default.Bolt,
                                                contentDescription = null,
                                                tint = iosGreen,
                                                modifier = Modifier
                                                    .size(15.dp)
                                                    .graphicsLayer {
                                                        val s = 0.94f + 0.08f * glowPulse
                                                        scaleX = s
                                                        scaleY = s
                                                    }
                                            )
                                        }
                                    }
                                }

                                // CENTER SLOT: Pure Pitch-Black iOS TrueDepth Cutout (subtle dark lens only)
                                Box(
                                    modifier = Modifier
                                        .size(9.dp)
                                        .clip(CircleShape)
                                        .background(Color(0xFF070A12))
                                        .border(0.75.dp, Color(0xFF161E2E), CircleShape),
                                    contentAlignment = Alignment.Center
                                ) {
                                    val glintAngleRad = Math.toRadians(ornamentSpinDeg.toDouble()).toFloat()
                                    Box(
                                        modifier = Modifier
                                            .size(2.5.dp)
                                            .graphicsLayer {
                                                translationX = cos(glintAngleRad) * 1.1f
                                                translationY = sin(glintAngleRad) * 1.1f
                                            }
                                            .clip(CircleShape)
                                            .background(Color(0xFF38BDF8).copy(alpha = 0.45f))
                                    )
                                }

                                // RIGHT SLOT: Crisp Metric ("76" in Neon Green like Image 1, or Equalizer like Image 4, or "0:22" like Image 5)
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    when {
                                        isPlayingMedia || compactMiniStyle == 1 -> {
                                            IosLiveWaveformBars(
                                                wavePhase = eqWave,
                                                barColor = iosGreen,
                                                barCount = 4,
                                                maxHeightDp = 11f
                                            )
                                            // Keep invisible 0dp status text node so automated testTag("dynamic_island_status_text") is always present
                                            Text(
                                                text = compactStatusSummaryText,
                                                fontSize = 1.sp,
                                                color = Color.Transparent,
                                                maxLines = 1,
                                                modifier = Modifier
                                                    .width(0.dp)
                                                    .testTag("dynamic_island_status_text")
                                            )
                                        }
                                        compactMiniStyle == 2 -> {
                                            val sec = ((nowMillis / 1000L) % 60L).toInt()
                                            Text(
                                                text = String.format(Locale.US, "0:%02d", sec),
                                                fontFamily = JetBrainsMonoFontFamily,
                                                fontSize = 10.5.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = iosAmber,
                                                maxLines = 1,
                                                modifier = Modifier.testTag("dynamic_island_status_text")
                                            )
                                        }
                                        compactMiniStyle == 3 -> {
                                            Text(
                                                text = "82 bpm",
                                                fontFamily = JetBrainsMonoFontFamily,
                                                fontSize = 9.5.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = iosPink,
                                                maxLines = 1,
                                                modifier = Modifier.testTag("dynamic_island_status_text")
                                            )
                                        }
                                        else -> {
                                            // Exact Image 1 Top Right Neon-Green "76" / Percentage!
                                            Text(
                                                text = "$animatedEnergyPercent",
                                                fontFamily = JetBrainsMonoFontFamily,
                                                fontSize = 11.5.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = iosGreen,
                                                maxLines = 1,
                                                modifier = Modifier.testTag("dynamic_island_status_text")
                                            )
                                        }
                                    }
                                }
                            }
                        }

                        else -> {
                            // =========================================================================
                            // STATE 3: EXPANDED iOS 18 LIVE ACTIVITY PLATTER (IMAGES 1–6 MULTI-WIDGET STUDIO)
                            // =========================================================================
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 15.dp, vertical = 13.dp),
                                verticalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                // A. Top iOS Live Activity Header: Squircle Mascot + Title/Status + Fold/Minimize Buttons
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .gsapStaggerReveal(
                                            index = 0,
                                            triggerKey = isExpanded,
                                            slideDp = 10.dp,
                                            enable3DPitch = true
                                        ),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .size(40.dp)
                                                .clip(RoundedCornerShape(12.dp))
                                                .background(iosPlatterDark)
                                                .border(1.dp, Color.White.copy(alpha = 0.14f), RoundedCornerShape(12.dp))
                                                .gsap3DPress(
                                                    maxTiltDegrees = 14f,
                                                    pressScale = 0.90f
                                                ),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            AnimatedMascotMiniSprite(
                                                isExcitedAlert = hasActiveBanner,
                                                isWorkingBusy = isBusyWorking,
                                                isPlayingMusic = isPlayingMedia,
                                                size = 30.dp
                                            )
                                        }

                                        Column(modifier = Modifier.weight(1f)) {
                                            Row(
                                                verticalAlignment = Alignment.CenterVertically,
                                                horizontalArrangement = Arrangement.spacedBy(5.dp)
                                            ) {
                                                Box(
                                                    modifier = Modifier
                                                        .size(6.dp)
                                                        .clip(CircleShape)
                                                        .background(privacyDotColor)
                                                )
                                                Text(
                                                    text = islandHeaderTitle,
                                                    fontSize = 11.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    color = Color.White,
                                                    maxLines = 1,
                                                    overflow = TextOverflow.Ellipsis
                                                )
                                            }
                                            Text(
                                                text = liveStatusNotificationText,
                                                fontSize = 10.sp,
                                                fontWeight = FontWeight.Medium,
                                                color = iosSubtleLabel,
                                                maxLines = 1,
                                                overflow = TextOverflow.Ellipsis,
                                                modifier = Modifier.testTag("dynamic_island_status_text")
                                            )
                                        }
                                    }

                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                                    ) {
                                        if (hasActiveBanner) {
                                            Box(
                                                modifier = Modifier
                                                    .clip(RoundedCornerShape(50))
                                                    .background(iosGreen)
                                                    .gsapMagneticMicroTouch(
                                                        maxMagneticShiftDp = 3.dp,
                                                        pressScale = 0.90f,
                                                        burstColor = iosGreen,
                                                        onClick = { onDismissStatusBanner() }
                                                    )
                                                    .padding(horizontal = 9.dp, vertical = 4.dp)
                                                    .testTag("dynamic_island_dismiss_banner_button")
                                            ) {
                                                Text(
                                                    text = "Tutup",
                                                    fontSize = 9.5.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    color = Color.Black
                                                )
                                            }
                                        }

                                        // Shrink to Mini Pill (Stage 0)
                                        Box(
                                            modifier = Modifier
                                                .size(26.dp)
                                                .clip(CircleShape)
                                                .background(iosPlatterElevated)
                                                .gsapMagneticMicroTouch(
                                                    maxMagneticShiftDp = 3.dp,
                                                    pressScale = 0.88f,
                                                    burstColor = iosGreen,
                                                    onClick = {
                                                        tapImpulseNonce++
                                                        islandStage = 0
                                                    }
                                                ),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Remove,
                                                contentDescription = "Mode Mini Pill",
                                                tint = iosGreen,
                                                modifier = Modifier.size(14.dp)
                                            )
                                        }

                                        // Fold into Ultra-Nano Cutout Ribbon
                                        Box(
                                            modifier = Modifier
                                                .size(26.dp)
                                                .clip(CircleShape)
                                                .background(iosPlatterDark)
                                                .gsapMagneticMicroTouch(
                                                    maxMagneticShiftDp = 3.dp,
                                                    pressScale = 0.88f,
                                                    burstColor = iosBlue,
                                                    onClick = {
                                                        tapImpulseNonce++
                                                        islandStage = 0
                                                        AppThemeController.isDynamicIslandFolded = true
                                                    }
                                                )
                                                .testTag("dynamic_island_fold_button"),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            GsapMicroIconBounce(
                                                icon = Icons.Default.KeyboardArrowUp,
                                                contentDescription = "Perkecil Dynamic Island",
                                                tint = Color.White,
                                                activeTrigger = isExpanded,
                                                iconSize = 15.dp
                                            )
                                        }
                                    }
                                }

                                // B. Interactive Size & Mini Style Preset Bar (Shrinks pill to Nano / Mini / Split Droplet)
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .horizontalScroll(rememberScrollState()),
                                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    val sizeOptions = listOf(
                                        Triple(0, "Nano (104dp)", iosCyan),
                                        Triple(1, "Mini iOS (134dp)", iosGreen),
                                        Triple(2, "Split Droplet", iosPurple)
                                    )
                                    sizeOptions.forEach { (presetIdx, label, accent) ->
                                        val selected = compactSizePreset == presetIdx
                                        Box(
                                            modifier = Modifier
                                                .clip(RoundedCornerShape(50))
                                                .background(if (selected) accent.copy(alpha = 0.22f) else iosPlatterDark)
                                                .border(
                                                    0.8.dp,
                                                    if (selected) accent else Color.White.copy(alpha = 0.1f),
                                                    RoundedCornerShape(50)
                                                )
                                                .clickable {
                                                    tapImpulseNonce++
                                                    AppThemeController.proDynamicIslandCompactPreset = presetIdx
                                                    islandStage = 0
                                                }
                                                .padding(horizontal = 8.dp, vertical = 4.dp)
                                        ) {
                                            Text(
                                                text = label,
                                                fontSize = 8.5.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = if (selected) accent else Color.White
                                            )
                                        }
                                    }

                                    val miniStyles = listOf(
                                        Triple(0, "⚡76", iosGreen),
                                        Triple(1, "Wave", iosGreen),
                                        Triple(2, "0:22", iosAmber),
                                        Triple(3, "82bpm", iosPink)
                                    )
                                    miniStyles.forEach { (styleIdx, label, accent) ->
                                        val selected = compactMiniStyle == styleIdx
                                        Box(
                                            modifier = Modifier
                                                .clip(RoundedCornerShape(50))
                                                .background(if (selected) accent.copy(alpha = 0.22f) else iosPlatterDark)
                                                .border(
                                                    0.8.dp,
                                                    if (selected) accent else Color.White.copy(alpha = 0.1f),
                                                    RoundedCornerShape(50)
                                                )
                                                .clickable {
                                                    tapImpulseNonce++
                                                    AppThemeController.proDynamicIslandMiniStyle = styleIdx
                                                    islandStage = 0
                                                }
                                                .padding(horizontal = 8.dp, vertical = 4.dp)
                                        ) {
                                            Text(
                                                text = label,
                                                fontSize = 8.5.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = if (selected) accent else iosSubtleLabel
                                            )
                                        }
                                    }
                                }

                                // C. Authentic iOS Live Activity Visual Deck (Switchable between Images 1, 2, 4, 6)
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    val widgetTabs = listOf(
                                        0 to "⚡ Energy",
                                        1 to "📊 Equalizer",
                                        2 to "⭕ Rings",
                                        3 to "📈 Chart"
                                    )
                                    widgetTabs.forEach { (tabIdx, title) ->
                                        val isSelected = expandedWidgetTab == tabIdx
                                        Box(
                                            modifier = Modifier
                                                .weight(1f)
                                                .clip(RoundedCornerShape(50))
                                                .background(if (isSelected) iosGreen.copy(alpha = 0.2f) else iosPlatterDark)
                                                .border(
                                                    0.8.dp,
                                                    if (isSelected) iosGreen else Color.White.copy(alpha = 0.08f),
                                                    RoundedCornerShape(50)
                                                )
                                                .clickable {
                                                    tapImpulseNonce++
                                                    expandedWidgetTab = tabIdx
                                                }
                                                .padding(vertical = 4.dp),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(
                                                text = title,
                                                fontSize = 8.5.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = if (isSelected) iosGreen else iosSubtleLabel
                                            )
                                        }
                                    }
                                }

                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(18.dp))
                                        .background(Color(0xFF09090B))
                                        .border(0.8.dp, Color.White.copy(alpha = 0.1f), RoundedCornerShape(18.dp))
                                        .padding(horizontal = 12.dp, vertical = 10.dp)
                                ) {
                                    when (expandedWidgetTab) {
                                        0 -> {
                                            // IMAGE 1: Glowing Neon-Green Energy Bar with ⚡76 Pill & Radial Halo
                                            Row(
                                                modifier = Modifier.fillMaxWidth(),
                                                verticalAlignment = Alignment.CenterVertically,
                                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                                            ) {
                                                Box(
                                                    modifier = Modifier
                                                        .weight(1f)
                                                        .height(26.dp),
                                                    contentAlignment = Alignment.CenterStart
                                                ) {
                                                    Box(
                                                        modifier = Modifier
                                                            .fillMaxWidth()
                                                            .height(6.dp)
                                                            .clip(RoundedCornerShape(50))
                                                            .background(Color(0xFF262629))
                                                    )
                                                    val frac = energyProgressFraction.coerceIn(0.25f, 0.88f)
                                                    Box(
                                                        modifier = Modifier
                                                            .fillMaxWidth(frac)
                                                            .height(6.dp)
                                                            .clip(RoundedCornerShape(50))
                                                            .background(
                                                                Brush.horizontalGradient(
                                                                    listOf(Color(0xFF28CD41), iosGreen, Color(0xFF66FF7F))
                                                                )
                                                            )
                                                    )
                                                    Row(
                                                        modifier = Modifier.fillMaxWidth(frac),
                                                        horizontalArrangement = Arrangement.End,
                                                        verticalAlignment = Alignment.CenterVertically
                                                    ) {
                                                        Box(contentAlignment = Alignment.Center) {
                                                            Canvas(modifier = Modifier.size(48.dp, 26.dp)) {
                                                                drawCircle(
                                                                    brush = Brush.radialGradient(
                                                                        colors = listOf(
                                                                            iosGreen.copy(alpha = 0.62f * glowPulse),
                                                                            iosGreen.copy(alpha = 0.22f * glowPulse),
                                                                            Color.Transparent
                                                                        )
                                                                    ),
                                                                    radius = size.maxDimension * 0.64f
                                                                )
                                                            }
                                                            Row(
                                                                modifier = Modifier
                                                                    .clip(RoundedCornerShape(50))
                                                                    .background(iosGreen)
                                                                    .padding(horizontal = 7.dp, vertical = 2.dp),
                                                                verticalAlignment = Alignment.CenterVertically,
                                                                horizontalArrangement = Arrangement.spacedBy(1.dp)
                                                            ) {
                                                                Icon(
                                                                    imageVector = Icons.Default.Bolt,
                                                                    contentDescription = null,
                                                                    tint = Color.White,
                                                                    modifier = Modifier.size(11.dp)
                                                                )
                                                                Text(
                                                                    text = "$animatedEnergyPercent",
                                                                    fontFamily = JetBrainsMonoFontFamily,
                                                                    fontSize = 10.sp,
                                                                    fontWeight = FontWeight.ExtraBold,
                                                                    color = Color.White
                                                                )
                                                            }
                                                        }
                                                    }
                                                }
                                                Text(
                                                    text = "~15m",
                                                    fontFamily = JetBrainsMonoFontFamily,
                                                    fontSize = 11.5.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    color = iosGreen
                                                )
                                            }
                                        }

                                        1 -> {
                                            // IMAGE 4: Golden/Yellow 22-Bar Bell-Curve Audio Equalizer Spectrum
                                            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                                                Row(
                                                    modifier = Modifier.fillMaxWidth(),
                                                    horizontalArrangement = Arrangement.SpaceBetween
                                                ) {
                                                    Text(
                                                        text = "15:36",
                                                        fontFamily = JetBrainsMonoFontFamily,
                                                        fontSize = 10.sp,
                                                        fontWeight = FontWeight.Bold,
                                                        color = iosYellow
                                                    )
                                                    Text(
                                                        text = "$animatedEncryptedCount/$animatedTotalFilesCount NYX2",
                                                        fontFamily = JetBrainsMonoFontFamily,
                                                        fontSize = 10.sp,
                                                        fontWeight = FontWeight.Bold,
                                                        color = iosYellow
                                                    )
                                                }
                                                Canvas(
                                                    modifier = Modifier
                                                        .fillMaxWidth()
                                                        .height(28.dp)
                                                ) {
                                                    val bars = 24
                                                    val gap = 4.dp.toPx()
                                                    val barW = ((size.width - gap * (bars - 1)) / bars).coerceAtLeast(2f)
                                                    for (i in 0 until bars) {
                                                        val centerDist = kotlin.math.abs(i - bars / 2f) / (bars / 2f)
                                                        val envelope = (1f - centerDist * 0.75f).coerceIn(0.2f, 1f)
                                                        val anim = 0.45f + 0.55f * kotlin.math.abs(sin(eqWave * 3.14f + i * 0.45f))
                                                        val h = size.height * envelope * anim
                                                        val active = i < (bars * 0.78f).toInt()
                                                        drawRoundRect(
                                                            color = if (active) iosYellow else iosYellow.copy(alpha = 0.25f),
                                                            topLeft = Offset(i * (barW + gap), size.height - h),
                                                            size = Size(barW, h),
                                                            cornerRadius = CornerRadius(barW / 2f, barW / 2f)
                                                        )
                                                    }
                                                }
                                            }
                                        }

                                        2 -> {
                                            // IMAGE 6: 4 Colorful iOS Activity Rings (DONE / NYX2 / STEGO / FILES)
                                            Row(
                                                modifier = Modifier.fillMaxWidth(),
                                                horizontalArrangement = Arrangement.SpaceEvenly,
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                val rings = listOf(
                                                    Triple("VAULT", iosPurple, 0.92f),
                                                    Triple("STEGO", iosYellow, 0.84f),
                                                    Triple("AUDIT", iosPink, 0.96f),
                                                    Triple("${allItems.size} FILE", iosGreen, 0.88f)
                                                )
                                                rings.forEach { (label, ringColor, sweepFrac) ->
                                                    Column(
                                                        horizontalAlignment = Alignment.CenterHorizontally,
                                                        verticalArrangement = Arrangement.spacedBy(3.dp)
                                                    ) {
                                                        Box(
                                                            modifier = Modifier.size(34.dp),
                                                            contentAlignment = Alignment.Center
                                                        ) {
                                                            Canvas(modifier = Modifier.fillMaxSize()) {
                                                                val stroke = 3.5.dp.toPx()
                                                                drawArc(
                                                                    color = ringColor.copy(alpha = 0.22f),
                                                                    startAngle = 0f,
                                                                    sweepAngle = 360f,
                                                                    useCenter = false,
                                                                    style = Stroke(width = stroke, cap = StrokeCap.Round)
                                                                )
                                                                drawArc(
                                                                    color = ringColor,
                                                                    startAngle = -90f,
                                                                    sweepAngle = 360f * sweepFrac * (0.85f + 0.15f * glowPulse),
                                                                    useCenter = false,
                                                                    style = Stroke(width = stroke, cap = StrokeCap.Round)
                                                                )
                                                            }
                                                            Box(
                                                                modifier = Modifier
                                                                    .size(6.dp)
                                                                    .clip(CircleShape)
                                                                    .background(ringColor)
                                                            )
                                                        }
                                                        Text(
                                                            text = label,
                                                            fontFamily = JetBrainsMonoFontFamily,
                                                            fontSize = 7.5.sp,
                                                            fontWeight = FontWeight.Bold,
                                                            color = ringColor
                                                        )
                                                    }
                                                }
                                            }
                                        }

                                        else -> {
                                            // IMAGE 2: Finance / Vault Neon-Green Sparkline & Bar Chart
                                            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                                Row(
                                                    modifier = Modifier.fillMaxWidth(),
                                                    horizontalArrangement = Arrangement.SpaceBetween,
                                                    verticalAlignment = Alignment.CenterVertically
                                                ) {
                                                    Text(
                                                        text = "ARCANUM TELEMETRY",
                                                        fontFamily = JetBrainsMonoFontFamily,
                                                        fontSize = 9.sp,
                                                        fontWeight = FontWeight.Bold,
                                                        color = Color.White
                                                    )
                                                    Text(
                                                        text = "+100% AES-GCM",
                                                        fontFamily = JetBrainsMonoFontFamily,
                                                        fontSize = 9.sp,
                                                        fontWeight = FontWeight.Bold,
                                                        color = iosGreen
                                                    )
                                                }
                                                Canvas(
                                                    modifier = Modifier
                                                        .fillMaxWidth()
                                                        .height(26.dp)
                                                ) {
                                                    val pts = listOf(0.45f, 0.52f, 0.38f, 0.68f, 0.30f, 0.74f, 0.62f, 0.85f, 0.78f, 0.92f)
                                                    val stepX = size.width / (pts.size - 1).coerceAtLeast(1)
                                                    val linePath = Path()
                                                    pts.forEachIndexed { idx, v ->
                                                        val x = idx * stepX
                                                        val y = size.height * (1f - (v * (0.85f + 0.12f * eqWave)).coerceIn(0.1f, 0.95f))
                                                        if (idx == 0) linePath.moveTo(x, y) else linePath.lineTo(x, y)
                                                    }
                                                    drawPath(
                                                        path = linePath,
                                                        color = iosGreen,
                                                        style = Stroke(width = 2.dp.toPx(), cap = StrokeCap.Round)
                                                    )
                                                }
                                            }
                                        }
                                    }
                                }

                                // D. iOS 18 Control Center 4-Pill Platter with Staggered Reveal & Magnetic Micro-Interactions
                                if (isUnlocked) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        IosExpandedControlPill(
                                            icon = Icons.Default.Apps,
                                            label = "120+ Fitur",
                                            accentColor = iosBlue,
                                            staggerIndex = 2,
                                            triggerKey = isExpanded,
                                            onClick = { showMicroFeaturesHubModal = true },
                                            modifier = Modifier
                                                .weight(1f)
                                                .testTag("dynamic_island_micro_features_pill")
                                        )
                                        IosExpandedControlPill(
                                            icon = Icons.Default.CameraAlt,
                                            label = "3D Studio",
                                            accentColor = iosPink,
                                            staggerIndex = 3,
                                            triggerKey = isExpanded,
                                            onClick = { activeModalTab = 0 },
                                            modifier = Modifier
                                                .weight(1f)
                                                .testTag("dynamic_island_photobooth_pill")
                                        )
                                        IosExpandedControlPill(
                                            icon = Icons.Default.HourglassBottom,
                                            label = "Kapsul",
                                            accentColor = iosPurple,
                                            staggerIndex = 4,
                                            triggerKey = isExpanded,
                                            onClick = { activeModalTab = 1 },
                                            modifier = Modifier
                                                .weight(1f)
                                                .testTag("dynamic_island_timecapsule_pill")
                                        )
                                        IosExpandedControlPill(
                                            icon = Icons.Default.ReceiptLong,
                                            label = "Struk",
                                            accentColor = iosGreen,
                                            staggerIndex = 5,
                                            triggerKey = isExpanded,
                                            onClick = { activeModalTab = 2 },
                                            modifier = Modifier
                                                .weight(1f)
                                                .testTag("dynamic_island_wrapped_pill")
                                        )
                                    }

                                    // E. Pinned Micro-Features & Costume Quick Platter (#141416 iOS Dark Glass)
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .gsapStaggerReveal(
                                                index = 6,
                                                triggerKey = isExpanded,
                                                slideDp = 14.dp,
                                                enable3DPitch = true
                                            )
                                            .clip(RoundedCornerShape(14.dp))
                                            .background(iosPlatterDark)
                                            .padding(horizontal = 10.dp, vertical = 7.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        Row(
                                            modifier = Modifier
                                                .weight(1f)
                                                .horizontalScroll(rememberScrollState()),
                                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            MicroFeaturesState.pinnedFeatureNumbers.forEach { num ->
                                                val feat = MicroFeaturesRegistry.allFeatures.find { it.number == num }
                                                if (feat != null) {
                                                    Row(
                                                        modifier = Modifier
                                                            .clip(RoundedCornerShape(50))
                                                            .background(iosPlatterElevated)
                                                            .gsapMagneticMicroTouch(
                                                                maxMagneticShiftDp = 3.dp,
                                                                pressScale = 0.91f,
                                                                burstColor = iosBlue,
                                                                onClick = {
                                                                    selectedMicroFeatureInitial = feat.number
                                                                    MicroFeaturesState.selectedFeatureNumber = feat.number
                                                                    showMicroFeaturesHubModal = true
                                                                }
                                                            )
                                                            .padding(horizontal = 8.dp, vertical = 4.dp),
                                                        verticalAlignment = Alignment.CenterVertically,
                                                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                                                    ) {
                                                        Text(
                                                            text = feat.codeBadge,
                                                            fontFamily = JetBrainsMonoFontFamily,
                                                            fontSize = 8.sp,
                                                            fontWeight = FontWeight.ExtraBold,
                                                            color = iosBlue
                                                        )
                                                        Text(
                                                            text = feat.title,
                                                            fontSize = 8.5.sp,
                                                            fontWeight = FontWeight.SemiBold,
                                                            color = Color.White,
                                                            maxLines = 1
                                                        )
                                                    }
                                                }
                                            }
                                        }

                                        // Random Mascot Outfit Pill
                                        Row(
                                            modifier = Modifier
                                                .clip(RoundedCornerShape(50))
                                                .background(iosBlue.copy(alpha = 0.22f))
                                                .border(0.8.dp, iosBlue, RoundedCornerShape(50))
                                                .gsapMagneticMicroTouch(
                                                    maxMagneticShiftDp = 3.5.dp,
                                                    pressScale = 0.90f,
                                                    burstColor = iosBlue,
                                                    onClick = {
                                                        val randomPreset = MascotCostumePreset.entries.random()
                                                        AppThemeController.applyCostumePreset(randomPreset)
                                                        onPostBanner("Kostum Maskot: ${randomPreset.displayName}")
                                                    }
                                                )
                                                .padding(horizontal = 8.dp, vertical = 4.dp),
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                                        ) {
                                            GsapMicroIconBounce(
                                                icon = Icons.Default.Casino,
                                                contentDescription = "Acak Kostum",
                                                tint = iosBlue,
                                                activeTrigger = activeOutfit,
                                                iconSize = 11.dp
                                            )
                                            Text(
                                                text = "Acak",
                                                fontSize = 8.5.sp,
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

            // =========================================================================
            // OPTIONAL DETACHED iOS CIRCLE DROPLET (IMAGE 5 SPLIT DROPLET HARDWARE STYLE)
            // =========================================================================
            if (islandStage == 0 && !hasActiveBanner && compactSizePreset == 2) {
                Spacer(modifier = Modifier.width(6.dp))
                Box(
                    modifier = Modifier
                        .size(28.dp)
                        .graphicsLayer {
                            scaleX = tapElasticScaleX.value
                            scaleY = tapElasticScaleY.value
                        }
                        .shadow(8.dp, CircleShape)
                        .clip(CircleShape)
                        .background(iosOledBlack)
                        .border(0.65.dp, Color.White.copy(alpha = 0.14f), CircleShape)
                        .gsapMagneticMicroTouch(
                            maxMagneticShiftDp = 3.dp,
                            pressScale = 0.88f,
                            burstColor = iosGreen,
                            onClick = {
                                tapImpulseNonce++
                                AppThemeController.proDynamicIslandMiniStyle = (compactMiniStyle + 1) % 4
                            }
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    AnimatedMascotMiniSprite(
                        isExcitedAlert = false,
                        isWorkingBusy = isBusyWorking,
                        isPlayingMusic = isPlayingMedia,
                        size = 18.dp
                    )
                }
            }
        }
    }

    // FULL VIRAL HUB DIALOG MODAL (LOCKED TO BENTO GRID GLOW)
    activeModalTab?.let { selectedModal ->
        ViralTrendsFullModalDialog(
            initialTab = selectedModal,
            activeVaultId = activeVaultId,
            allItems = allItems,
            onDismiss = { activeModalTab = null },
            onPostBanner = onPostBanner
        )
    }

    // 120+ INTERACTIVE MICRO-FEATURES SWISS-ARMY MODAL
    if (showMicroFeaturesHubModal) {
        MicroFeaturesSwissArmyDialog(
            initialFeatureNumber = selectedMicroFeatureInitial,
            onDismiss = { showMicroFeaturesHubModal = false },
            onPostBanner = onPostBanner
        )
    }
}

@Composable
private fun IosLiveWaveformBars(
    wavePhase: Float,
    barColor: Color,
    barCount: Int = 5,
    maxHeightDp: Float = 14f
) {
    val multipliers = remember { listOf(0.45f, 0.95f, 0.65f, 1.0f, 0.55f, 0.85f) }
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(2.2.dp),
        modifier = Modifier.height(maxHeightDp.dp)
    ) {
        for (i in 0 until barCount) {
            val baseMult = multipliers[i % multipliers.size]
            val animatedFrac = if (i % 2 == 0) {
                (0.28f + baseMult * wavePhase * 0.72f).coerceIn(0.25f, 1f)
            } else {
                (0.28f + baseMult * (1.25f - wavePhase) * 0.72f).coerceIn(0.25f, 1f)
            }
            Box(
                modifier = Modifier
                    .width(2.4.dp)
                    .height((maxHeightDp * animatedFrac).dp)
                    .clip(RoundedCornerShape(50))
                    .background(barColor)
            )
        }
    }
}

@Composable
private fun IosExpandedControlPill(
    icon: ImageVector,
    label: String,
    accentColor: Color,
    staggerIndex: Int = 0,
    triggerKey: Any? = Unit,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .gsapStaggerReveal(
                index = staggerIndex,
                triggerKey = triggerKey,
                slideDp = 16.dp,
                enable3DPitch = true
            )
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFF1C1C1E))
            .border(0.8.dp, Color.White.copy(alpha = 0.1f), RoundedCornerShape(16.dp))
            .gsapMagneticMicroTouch(
                maxMagneticShiftDp = 3.5.dp,
                maxTiltDegrees = 12f,
                pressScale = 0.91f,
                burstColor = accentColor,
                onClick = onClick
            )
            .padding(vertical = 10.dp, horizontal = 6.dp),
        horizontalArrangement = Arrangement.spacedBy(5.dp, Alignment.CenterHorizontally),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(20.dp)
                .clip(CircleShape)
                .background(accentColor.copy(alpha = 0.22f)),
            contentAlignment = Alignment.Center
        ) {
            GsapMicroIconBounce(
                icon = icon,
                contentDescription = label,
                tint = accentColor,
                activeTrigger = triggerKey,
                iconSize = 12.dp
            )
        }
        Text(
            text = label,
            fontSize = 10.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color.White,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
fun ViralTrendsFullModalDialog(
    initialTab: Int,
    activeVaultId: Int,
    allItems: List<VaultItemEntity>,
    onDismiss: () -> Unit,
    onPostBanner: (String) -> Unit
) {
    var currentTab by remember(initialTab) { mutableIntStateOf(initialTab) }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 12.dp, vertical = 18.dp)
                .clip(RoundedCornerShape(24.dp))
                .border(
                    width = 1.5.dp,
                    brush = Brush.linearGradient(
                        listOf(
                            AppThemeController.resolvedPrimaryColor,
                            AppThemeController.resolvedSecondaryAccent,
                            AppThemeController.resolvedIslandGlowColor
                        )
                    ),
                    shape = RoundedCornerShape(24.dp)
                ),
            color = com.example.ui.theme.DuoSnowCanvas
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(14.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Top Modal Header & Close Button (Bento Glow Header)
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
                            imageVector = when (currentTab) {
                                0 -> Icons.Default.CameraAlt
                                1 -> Icons.Default.HourglassBottom
                                else -> Icons.Default.ReceiptLong
                            },
                            contentDescription = null,
                            tint = AppThemeController.resolvedPrimaryColor,
                            modifier = Modifier.size(20.dp)
                        )
                        Column {
                            Text(
                                text = when (currentTab) {
                                    0 -> "Photobooth Stiker & Kartu 3D Foil"
                                    1 -> "Brankas Time Capsule"
                                    else -> "Arcanum Wrapped & Struk Receiptify"
                                },
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.ExtraBold,
                                color = DuoEelInk
                            )
                            Text(
                                text = "BENTO GRID GLOW ENGINE • DYNAMIC ISLAND STUDIO",
                                fontFamily = JetBrainsMonoFontFamily,
                                fontSize = 8.sp,
                                fontWeight = FontWeight.Bold,
                                color = CosmicTextSubtle
                            )
                        }
                    }

                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(DuoMintSurface)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Tutup",
                            tint = DuoEelInk,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }

                // 3-Tab Switcher inside Modal (With Vector Icons, Zero Emojis)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    val tabs = listOf(
                        Triple(0, "Photobooth 3D", Icons.Default.CameraAlt),
                        Triple(1, "Time Capsule", Icons.Default.HourglassBottom),
                        Triple(2, "Wrapped & Struk", Icons.Default.ReceiptLong)
                    )
                    tabs.forEach { (idx, title, tabIcon) ->
                        val isSel = currentTab == idx
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .duolingoButton(
                                    faceColor = if (isSel) DuoMacawBlue else DuoMintSurface,
                                    lipColor = if (isSel) DuoMacawBlueLip else DuoSwanBorder,
                                    cornerRadius = 11.dp,
                                    lipHeight = 3.dp
                                )
                                .instantTouchPress { currentTab = idx }
                                .padding(bottom = 3.dp)
                                .padding(vertical = 8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Icon(
                                    imageVector = tabIcon,
                                    contentDescription = null,
                                    tint = if (isSel) Color.White else DuoMacawBlue,
                                    modifier = Modifier.size(12.dp)
                                )
                                Text(
                                    text = title,
                                    fontSize = 9.2.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = if (isSel) Color.White else DuoEelInk,
                                    maxLines = 1
                                )
                            }
                        }
                    }
                }

                // Scrollable Modal Body
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    when (currentTab) {
                        0 -> MascotPhotoboothAndHoloProfileStudio(
                            totalVaultFiles = allItems.size,
                            onPostBanner = onPostBanner
                        )
                        1 -> VaultTimeCapsuleStudioContent(
                            vaultItems = allItems,
                            onPostBanner = onPostBanner
                        )
                        else -> ArcanumWrappedAndReceiptifyStudio(
                            activeVaultId = activeVaultId,
                            allItems = allItems,
                            onPostBanner = onPostBanner
                        )
                    }
                }
            }
        }
    }
}
