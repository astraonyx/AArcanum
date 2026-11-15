package com.example.ui

import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import com.example.R
import com.example.ui.theme.AstrodraconSapphire
import com.example.ui.theme.PastelLavender
import com.example.ui.theme.PastelMint
import com.example.ui.theme.PastelPeach
import com.example.ui.theme.PastelSky
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import androidx.compose.ui.focus.onFocusChanged

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CloudUpload
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.CosmicBorderHairline
import com.example.ui.theme.CosmicGradientCta
import com.example.ui.theme.CosmicMagenta
import com.example.ui.theme.CosmicObsidian
import com.example.ui.theme.CosmicSurface
import com.example.ui.theme.CosmicTextMuted
import com.example.ui.theme.CosmicTextPure
import com.example.ui.theme.CosmicTextSubtle
import com.example.ui.theme.CosmicVoid
import com.example.ui.theme.JetBrainsMonoFontFamily
import com.example.ui.theme.NebulaViolet
import com.example.ui.theme.SolarGold
import com.example.ui.theme.StarlightCyan
import com.example.ui.theme.StellarYellow
import com.example.ui.theme.VaultCharcoal
import com.example.ui.theme.VaultCoral
import com.example.ui.theme.VaultLime
import com.example.ui.theme.VaultMintCard
import com.example.ui.theme.VaultObsidian
import com.example.ui.theme.VaultPureWhite
import com.example.ui.theme.VaultSlate
import com.example.ui.theme.duolingoButton
import com.example.ui.theme.duolingoCard
import kotlinx.coroutines.launch
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.sin

/**
 * 8 Emotional States for the Cosmic Celestial Dragon Mascot.
 * Also includes backward-compatible enum values for existing call sites.
 */
enum class MascotMood {
    IDLE_BREATHE,
    TOUCH_REACTION,
    PIN_SHIELD,
    ALERT_FIRE,
    SUCCESS_LEAP,
    DOWNLOAD_HUNTER,
    MT_INSPECTING,
    STEALTH_SHADOW,

    // Multi-Emotion & Multi-Color Interactive States
    CRYING_FOUNTAIN, // Biru Samudra — Nangis ngejer air mata nyembur ke atas lalu turun deras
    RAGE_INFERNO,    // Merah Membara — Marah besar dengan aura kobaran api merah-oranye
    SHOCKED_JOLT,    // Kuning Petir — Kaget tersengat kilat kuning
    SHY_BLUSH,       // Pink Sakura — Tersipu malu pipi merona
    SLEEPY_DREAM,    // Ungu Lavender — Mengantuk berat keluar gelembung Zzz

    // Backward-compatibility aliases
    WAVE_HELLO,
    GUARD_ALERT,
    SHIELD_EYES,
    HAPPY_VIBES
}

enum class MascotPersona(val label: String, val badgeColor: Color) {
    KAWAII_FOLDER("Folder Companion", Color(0xFFF59E0B)),
    COSMIC_DRAGON("Folder Guard", Color(0xFF58CC02)),
    AURORA_WYRM("Folder Explorer", Color(0xFF1CB0F6)),
    SOLAR_DRAKE("Folder Gold", Color(0xFFF59E0B)),
    NEBULA_PHANTOM("Folder Vault", Color(0xFFCE82FF)),

    // Backward-compatibility aliases (all unified to Kawaii File Folder styles)
    SHADOW_GUARDIAN("Folder Guard", Color(0xFF58CC02)),
    SPIRIT_FLAME("Folder Explorer", Color(0xFF1CB0F6)),
    KITSUNE_FOX("Folder Gold", Color(0xFFF59E0B)),
    CYBER_PHANTOM("Folder Vault", Color(0xFFCE82FF))
}

/**
 * Scroll-Safe 3D Tactile Button Press Trigger:
 * - Ketika ditekan, tombol amblas secara fisik ke bawah (translationY = +4dp sesuai bibir 3D)
 *   disertai efek pegas membal (Spring.DampingRatioMediumBouncy) dan getar haptik instan.
 * - Menggunakan MutableInteractionSource + clickable bawaan Compose yang otomatis
 *   membatalkan aksi tekan jika jari mulai menggeser layar (scroll), sehingga tombol
 *   tidak pernah terpencet secara tidak sengaja saat pengguna melakukan scroll.
 */
fun Modifier.instantTouchPress(
    enabled: Boolean = true,
    depressDepthDp: Dp = 4.dp,
    onPressInstant: () -> Unit
): Modifier = composed {
    val currentCallback by rememberUpdatedState(onPressInstant)
    val interactionSource = remember { MutableInteractionSource() }
    val haptic = androidx.compose.ui.platform.LocalHapticFeedback.current
    val qualityMode = com.example.ui.theme.AppThemeController.graphicsQualityMode

    // Mode Smooth (Level 1): Super ringan & instan tanpa beban animasi/shader
    if (qualityMode == com.example.ui.theme.AppGraphicsQualityMode.SMOOTH) {
        return@composed this.clickable(
            enabled = enabled,
            interactionSource = interactionSource,
            indication = null,
            onClick = { currentCallback() }
        )
    }

    val preset = com.example.ui.theme.AppThemeController.activeGsapPreset
    val tiltIntensity = com.example.ui.theme.AppThemeController.gsap3DTiltIntensity
    val isUltraOrExtreme = qualityMode == com.example.ui.theme.AppGraphicsQualityMode.ULTRA_GSAP ||
        qualityMode == com.example.ui.theme.AppGraphicsQualityMode.EXTREME
    var isPressed by remember { mutableStateOf(false) }
    var normTouchX by remember { androidx.compose.runtime.mutableFloatStateOf(0f) }
    var normTouchY by remember { androidx.compose.runtime.mutableFloatStateOf(0f) }
    var shockwaveNonce by remember { androidx.compose.runtime.mutableIntStateOf(0) }
    val shockwaveProgress = remember { androidx.compose.animation.core.Animatable(1f) }
    val reboundPop = remember { androidx.compose.animation.core.Animatable(0f) }

    val pressProgress by androidx.compose.animation.core.animateFloatAsState(
        targetValue = if (isPressed) 1f else 0f,
        animationSpec = spring(
            dampingRatio = when {
                qualityMode == com.example.ui.theme.AppGraphicsQualityMode.ULTRA_GSAP -> 0.42f
                preset == com.example.ui.theme.GsapMotionPreset.GSAP_ELASTIC_BOUNCE -> Spring.DampingRatioLowBouncy
                preset == com.example.ui.theme.GsapMotionPreset.CYBER_MAGNETIC_3D -> Spring.DampingRatioMediumBouncy
                preset == com.example.ui.theme.GsapMotionPreset.TURBO_SNAPPY -> Spring.DampingRatioNoBouncy
                else -> Spring.DampingRatioMediumBouncy
            },
            stiffness = when (preset) {
                com.example.ui.theme.GsapMotionPreset.TURBO_SNAPPY -> Spring.StiffnessHigh
                com.example.ui.theme.GsapMotionPreset.GSAP_ELASTIC_BOUNCE -> Spring.StiffnessMediumLow
                else -> Spring.StiffnessMedium
            }
        ),
        label = "tactile_3d_press"
    )

    androidx.compose.runtime.LaunchedEffect(shockwaveNonce) {
        if (shockwaveNonce > 0 && qualityMode.tierLevel >= 3) {
            shockwaveProgress.snapTo(0f)
            reboundPop.snapTo(1f)
            reboundPop.animateTo(
                targetValue = 0f,
                animationSpec = androidx.compose.animation.core.tween(
                    durationMillis = GsapEase.effectiveDurationMs(if (isUltraOrExtreme) 420 else 340),
                    easing = GsapEase.ElasticOutDefault
                )
            )
            shockwaveProgress.animateTo(
                targetValue = 1f,
                animationSpec = androidx.compose.animation.core.tween(
                    durationMillis = GsapEase.effectiveDurationMs(if (isUltraOrExtreme) 520 else 420),
                    easing = GsapEase.ExpoOut
                )
            )
        }
    }

    androidx.compose.runtime.LaunchedEffect(interactionSource) {
        interactionSource.interactions.collect { interaction ->
            when (interaction) {
                is androidx.compose.foundation.interaction.PressInteraction.Press -> {
                    isPressed = true
                    normTouchX = ((interaction.pressPosition.x % 220f) / 110f - 1f).coerceIn(-1f, 1f)
                    normTouchY = ((interaction.pressPosition.y % 120f) / 60f - 1f).coerceIn(-1f, 1f)
                    haptic.performHapticFeedback(
                        androidx.compose.ui.hapticfeedback.HapticFeedbackType.TextHandleMove
                    )
                }
                is androidx.compose.foundation.interaction.PressInteraction.Release -> {
                    isPressed = false
                    shockwaveNonce++
                }
                is androidx.compose.foundation.interaction.PressInteraction.Cancel -> {
                    isPressed = false
                }
            }
        }
    }

    val accentColor = com.example.ui.theme.AppThemeController.resolvedPrimaryColor
    val rainbowRingColors = remember {
        listOf(
            Color(0xFF00F2FE),
            Color(0xFFA855F7),
            Color(0xFFEC4899),
            Color(0xFFFF4B4B),
            Color(0xFFFFC800),
            Color(0xFF22C55E),
            Color(0xFF00F2FE)
        )
    }

    this
        .graphicsLayer {
            val sinkPx = depressDepthDp.toPx() * pressProgress
            val magBoost = if (isUltraOrExtreme) 1.65f else 1f
            val magShiftPx = 2.5.dp.toPx() * magBoost * pressProgress
            translationX = normTouchX * magShiftPx
            translationY = sinkPx + normTouchY * (magShiftPx * 0.55f)
            val popBoost = reboundPop.value * (if (isUltraOrExtreme) 0.052f else 0.032f)
            scaleX = 1f - (0.055f * pressProgress) + popBoost
            scaleY = 1f - (0.068f * pressProgress) + popBoost * 1.18f
            if (qualityMode.tierLevel >= 3) {
                val maxTilt = when {
                    qualityMode == com.example.ui.theme.AppGraphicsQualityMode.ULTRA_GSAP -> 13.5f
                    preset == com.example.ui.theme.GsapMotionPreset.CYBER_MAGNETIC_3D -> 11f
                    else -> 6.5f
                }
                val effectiveTilt = maxTilt * tiltIntensity
                rotationX = (-normTouchY * effectiveTilt * pressProgress).coerceIn(-22f, 22f)
                rotationY = (normTouchX * effectiveTilt * pressProgress).coerceIn(-22f, 22f)
                cameraDistance = 13f * density
            }
        }
        .drawWithContent {
            drawContent()
            // Press-hold neon rainbow flash rim in Extreme & Ultra GSAP modes
            if (pressProgress > 0.04f && isUltraOrExtreme) {
                val r = 14.dp.toPx()
                drawRoundRect(
                    brush = Brush.sweepGradient(colors = rainbowRingColors),
                    topLeft = Offset.Zero,
                    size = size,
                    cornerRadius = CornerRadius(r, r),
                    alpha = (pressProgress * 0.85f).coerceIn(0f, 1f),
                    style = Stroke(width = 2.6.dp.toPx())
                )
            }
            val sp = shockwaveProgress.value
            if (sp < 0.99f && qualityMode.tierLevel >= 3) {
                val cx = size.width * 0.5f + normTouchX * (size.width * 0.16f)
                val cy = size.height * 0.5f + normTouchY * (size.height * 0.16f)
                val maxR = size.width.coerceAtLeast(size.height) * (if (isUltraOrExtreme) 0.72f else 0.58f)
                val ringR = maxR * sp
                val fade = (1f - sp).coerceIn(0f, 1f)
                if (isUltraOrExtreme) {
                    drawCircle(
                        brush = Brush.sweepGradient(
                            colors = rainbowRingColors,
                            center = Offset(cx, cy)
                        ),
                        radius = ringR,
                        center = Offset(cx, cy),
                        alpha = fade * 0.78f,
                        style = Stroke(width = (2.8f * (1f - sp * 0.45f)) * density)
                    )
                    drawCircle(
                        color = Color(0xFF00F2FE).copy(alpha = fade * 0.52f),
                        radius = ringR * 0.65f,
                        center = Offset(cx, cy),
                        style = Stroke(width = (1.6f * (1f - sp * 0.5f)) * density)
                    )
                    for (i in 0 until 12) {
                        val angle = (i * (Math.PI / 6.0)).toFloat() + sp * 0.58f
                        val dist = ringR * 1.10f
                        val px = cx + kotlin.math.cos(angle) * dist
                        val py = cy + kotlin.math.sin(angle) * dist
                        val sparkColor = rainbowRingColors[i % rainbowRingColors.size]
                        drawCircle(
                            color = sparkColor.copy(alpha = fade * 0.90f),
                            radius = (2.7f * (1f - sp * 0.52f)).coerceAtLeast(0.8f) * density,
                            center = Offset(px, py)
                        )
                    }
                } else {
                    drawCircle(
                        color = accentColor.copy(alpha = fade * 0.36f),
                        radius = ringR,
                        center = Offset(cx, cy),
                        style = Stroke(width = (2.0f * (1f - sp * 0.5f)) * density)
                    )
                    for (i in 0 until 8) {
                        val angle = (i * (Math.PI / 4.0)).toFloat() + sp * 0.42f
                        val dist = ringR * 1.06f
                        val px = cx + kotlin.math.cos(angle) * dist
                        val py = cy + kotlin.math.sin(angle) * dist
                        drawCircle(
                            color = Color.White.copy(alpha = fade * 0.65f),
                            radius = (2.2f * (1f - sp * 0.55f)).coerceAtLeast(0.7f) * density,
                            center = Offset(px, py)
                        )
                    }
                }
            }
        }
        .clickable(
            enabled = enabled,
            interactionSource = interactionSource,
            indication = null,
            onClick = {
                haptic.performHapticFeedback(
                    androidx.compose.ui.hapticfeedback.HapticFeedbackType.LongPress
                )
                currentCallback()
            }
        )
}

/**
 * Scroll-Safe Tactile Click untuk kartu & chip di dalam scroll dengan efek membal pegas halus + haptik
 */
fun Modifier.fastZeroLatencyClick(
    enabled: Boolean = true,
    onClick: () -> Unit
): Modifier = composed {
    val currentCallback by rememberUpdatedState(onClick)
    val interactionSource = remember { MutableInteractionSource() }
    val haptic = androidx.compose.ui.platform.LocalHapticFeedback.current
    var isPressed by remember { mutableStateOf(false) }

    val pressProgress by androidx.compose.animation.core.animateFloatAsState(
        targetValue = if (isPressed) 1f else 0f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "card_spring_press"
    )

    androidx.compose.runtime.LaunchedEffect(interactionSource) {
        interactionSource.interactions.collect { interaction ->
            when (interaction) {
                is androidx.compose.foundation.interaction.PressInteraction.Press -> {
                    isPressed = true
                    haptic.performHapticFeedback(
                        androidx.compose.ui.hapticfeedback.HapticFeedbackType.TextHandleMove
                    )
                }
                is androidx.compose.foundation.interaction.PressInteraction.Release,
                is androidx.compose.foundation.interaction.PressInteraction.Cancel -> {
                    isPressed = false
                }
            }
        }
    }

    this
        .graphicsLayer {
            translationY = 2.5.dp.toPx() * pressProgress
            scaleX = 1f - (0.025f * pressProgress)
            scaleY = 1f - (0.025f * pressProgress)
        }
        .clickable(
            enabled = enabled,
            interactionSource = interactionSource,
            indication = null,
            onClick = { currentCallback() }
        )
}

/**
 * Clean Modern Minimalist Colorful Duolingo Galaxy Ambient Backdrop:
 * Menghadirkan atmosfer galaksi modern yang bersih dengan partikel bintang berkelip halus,
 * cincin orbit kosmik tipis, dan pendaran nebula warna-warni Duolingo (#58CC02, #1CB0F6, #CE82FF).
 */
@Composable
fun DuolingoGalaxyAmbientBackdrop(
    modifier: Modifier = Modifier
) {
    val infinite = rememberInfiniteTransition(label = "duolingo_galaxy_ambient")
    val orbitAngle by infinite.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 28000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "galaxy_orbit"
    )
    val twinklePhase by infinite.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 2200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "galaxy_twinkle"
    )

    val currentTheme = com.example.ui.theme.AppThemeController.activeTheme

    Canvas(modifier = modifier.fillMaxSize()) {
        val w = size.width
        val h = size.height

        when (currentTheme) {
            com.example.ui.theme.AppVisualTheme.DUOLINGO -> {
                // Soft Nebula Color Orbs + Playful 3D Galaxy Sparkles
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            com.example.ui.theme.DuoMacawBlue.copy(alpha = 0.10f + twinklePhase * 0.04f),
                            Color.Transparent
                        ),
                        center = Offset(w * 0.88f, h * 0.12f),
                        radius = w * 0.55f
                    ),
                    radius = w * 0.55f,
                    center = Offset(w * 0.88f, h * 0.12f)
                )
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            com.example.ui.theme.DuoBeetlePurple.copy(alpha = 0.09f + (1f - twinklePhase) * 0.04f),
                            Color.Transparent
                        ),
                        center = Offset(w * 0.12f, h * 0.45f),
                        radius = w * 0.50f
                    ),
                    radius = w * 0.50f,
                    center = Offset(w * 0.12f, h * 0.45f)
                )
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            com.example.ui.theme.DuoFeatherGreen.copy(alpha = 0.09f + twinklePhase * 0.03f),
                            Color.Transparent
                        ),
                        center = Offset(w * 0.82f, h * 0.82f),
                        radius = w * 0.52f
                    ),
                    radius = w * 0.52f,
                    center = Offset(w * 0.82f, h * 0.82f)
                )
                rotate(degrees = orbitAngle, pivot = Offset(w * 0.85f, h * 0.14f)) {
                    drawOval(
                        color = com.example.ui.theme.DuoMacawBlue.copy(alpha = 0.11f),
                        topLeft = Offset(w * 0.62f, h * 0.04f),
                        size = Size(w * 0.46f, h * 0.18f),
                        style = Stroke(width = 1.5.dp.toPx())
                    )
                }
                val starCoords = listOf(
                    Triple(0.10f, 0.08f, com.example.ui.theme.DuoMacawBlue),
                    Triple(0.92f, 0.22f, com.example.ui.theme.DuoBeetlePurple),
                    Triple(0.22f, 0.28f, com.example.ui.theme.DuoFeatherGreen),
                    Triple(0.78f, 0.36f, com.example.ui.theme.DuoFoxOrange),
                    Triple(0.08f, 0.64f, com.example.ui.theme.DuoMacawBlue),
                    Triple(0.90f, 0.68f, com.example.ui.theme.DuoFeatherGreen),
                    Triple(0.32f, 0.88f, com.example.ui.theme.DuoBeetlePurple)
                )
                starCoords.forEachIndexed { idx, (rx, ry, starColor) ->
                    val phaseShift = if (idx % 2 == 0) twinklePhase else (1f - twinklePhase)
                    val sx = w * rx
                    val sy = h * ry - (phaseShift * 6.dp.toPx())
                    val arm = (3.5.dp.toPx()) * (0.7f + phaseShift * 0.55f)
                    val alpha = 0.22f + phaseShift * 0.32f
                    drawLine(
                        color = starColor.copy(alpha = alpha),
                        start = Offset(sx - arm, sy),
                        end = Offset(sx + arm, sy),
                        strokeWidth = 1.8.dp.toPx(),
                        cap = StrokeCap.Round
                    )
                    drawLine(
                        color = starColor.copy(alpha = alpha),
                        start = Offset(sx, sy - arm),
                        end = Offset(sx, sy + arm),
                        strokeWidth = 1.8.dp.toPx(),
                        cap = StrokeCap.Round
                    )
                }
            }

            com.example.ui.theme.AppVisualTheme.GLASSMORPHISM -> {
                // Rich Translucent Frosted Bokeh Orbs & Specular Light Ribbons
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            com.example.ui.theme.DuoFeatherGreen.copy(alpha = 0.22f),
                            Color.Transparent
                        ),
                        center = Offset(w * 0.18f, h * 0.16f),
                        radius = w * 0.62f
                    ),
                    radius = w * 0.62f,
                    center = Offset(w * 0.18f, h * 0.16f)
                )
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            com.example.ui.theme.DuoMacawBlue.copy(alpha = 0.20f),
                            Color.Transparent
                        ),
                        center = Offset(w * 0.86f, h * 0.42f),
                        radius = w * 0.60f
                    ),
                    radius = w * 0.60f,
                    center = Offset(w * 0.86f, h * 0.42f)
                )
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            com.example.ui.theme.DuoBeetlePurple.copy(alpha = 0.18f),
                            Color.Transparent
                        ),
                        center = Offset(w * 0.30f, h * 0.80f),
                        radius = w * 0.58f
                    ),
                    radius = w * 0.58f,
                    center = Offset(w * 0.30f, h * 0.80f)
                )
                // Floating Frosted Glass Bokeh Rings
                for (b in 0..3) {
                    val bx = w * (0.20f + b * 0.22f)
                    val by = h * (0.18f + (b % 2) * 0.44f) + (twinklePhase - 0.5f) * 14f
                    val br = w * (0.11f + (b % 2) * 0.05f)
                    drawCircle(
                        color = Color.White.copy(alpha = 0.30f),
                        radius = br,
                        center = Offset(bx, by)
                    )
                    drawCircle(
                        color = Color.White.copy(alpha = 0.65f),
                        radius = br,
                        center = Offset(bx, by),
                        style = Stroke(width = 1.5.dp.toPx())
                    )
                }
            }

            com.example.ui.theme.AppVisualTheme.NEUMORPHISM -> {
                // Soft Extruded Tactile Concentric Bevel Rings & Recessed Wells
                val neuBase = com.example.ui.theme.DuoSnowCanvas
                val darkShadow = com.example.ui.theme.AppThemeController.darkenColor(neuBase, 0.86f)
                val wells = listOf(
                    Triple(0.86f, 0.12f, w * 0.22f),
                    Triple(0.12f, 0.52f, w * 0.18f),
                    Triple(0.84f, 0.84f, w * 0.20f)
                )
                wells.forEach { (rx, ry, rad) ->
                    val cx = w * rx
                    val cy = h * ry
                    drawCircle(
                        color = darkShadow.copy(alpha = 0.42f),
                        radius = rad,
                        center = Offset(cx + 5f, cy + 5f),
                        style = Stroke(width = 10.dp.toPx())
                    )
                    drawCircle(
                        color = Color.White.copy(alpha = 0.82f),
                        radius = rad,
                        center = Offset(cx - 5f, cy - 5f),
                        style = Stroke(width = 10.dp.toPx())
                    )
                }
            }

            com.example.ui.theme.AppVisualTheme.MINIMALIST -> {
                // Ultra-Clean Architectural Dot-Grid / Hairline Editorial Canvas
                val gridStep = 36.dp.toPx()
                val dotColor = Color(0xFFD4D4D8).copy(alpha = 0.45f)
                var gx = gridStep
                while (gx < w) {
                    var gy = gridStep
                    while (gy < h) {
                        drawCircle(
                            color = dotColor,
                            radius = 1.1.dp.toPx(),
                            center = Offset(gx, gy)
                        )
                        gy += gridStep
                    }
                    gx += gridStep
                }
            }

            com.example.ui.theme.AppVisualTheme.FLAT_DESIGN -> {
                // Bold 2D Geometric Color-Block Shapes (Zero gradient, zero blur, crisp 2D vectors)
                drawCircle(
                    color = com.example.ui.theme.DuoFeatherGreen.copy(alpha = 0.14f),
                    radius = w * 0.28f,
                    center = Offset(w * 0.92f, h * 0.10f)
                )
                drawRoundRect(
                    color = com.example.ui.theme.DuoMacawBlue.copy(alpha = 0.12f),
                    topLeft = Offset(-w * 0.08f, h * 0.42f),
                    size = Size(w * 0.34f, w * 0.34f),
                    cornerRadius = androidx.compose.ui.geometry.CornerRadius(16.dp.toPx(), 16.dp.toPx())
                )
                drawCircle(
                    color = com.example.ui.theme.DuoFoxOrange.copy(alpha = 0.12f),
                    radius = w * 0.22f,
                    center = Offset(w * 0.88f, h * 0.84f)
                )
            }

            com.example.ui.theme.AppVisualTheme.BENTO_GLOW -> {
                // Bento Grid Glow (iOS 18 Control Center + Linear App Bento Grid + Arc Browser Ambient Frame)
                val primaryGlow = com.example.ui.theme.DuoFeatherGreen
                val secondaryGlow = com.example.ui.theme.AppThemeController.resolvedSecondaryAccent

                // 1. Arc Browser Top-Left & Bottom-Right Radial Ambient Corner Glows
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            primaryGlow.copy(alpha = 0.20f + twinklePhase * 0.05f),
                            Color.Transparent
                        ),
                        center = Offset(w * 0.14f, h * 0.10f),
                        radius = w * 0.65f
                    ),
                    radius = w * 0.65f,
                    center = Offset(w * 0.14f, h * 0.10f)
                )
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            secondaryGlow.copy(alpha = 0.18f + (1f - twinklePhase) * 0.05f),
                            Color.Transparent
                        ),
                        center = Offset(w * 0.88f, h * 0.82f),
                        radius = w * 0.65f
                    ),
                    radius = w * 0.65f,
                    center = Offset(w * 0.88f, h * 0.82f)
                )

                // 2. Linear App Subtle Bento Architectural Grid Lines
                val cellSize = 64.dp.toPx()
                val gridLineColor = primaryGlow.copy(alpha = 0.06f)
                var gx = cellSize
                while (gx < w) {
                    drawLine(
                        color = gridLineColor,
                        start = Offset(gx, 0f),
                        end = Offset(gx, h),
                        strokeWidth = 1.dp.toPx()
                    )
                    gx += cellSize
                }
                var gy = cellSize
                while (gy < h) {
                    drawLine(
                        color = gridLineColor,
                        start = Offset(0f, gy),
                        end = Offset(w, gy),
                        strokeWidth = 1.dp.toPx()
                    )
                    gy += cellSize
                }

                // 3. Arc Browser Ambient Tinted Screen Frame Rim
                val frameInset = 5.dp.toPx()
                val frameCorner = 28.dp.toPx()
                drawRoundRect(
                    brush = Brush.linearGradient(
                        colors = listOf(
                            primaryGlow.copy(alpha = 0.36f),
                            secondaryGlow.copy(alpha = 0.26f),
                            com.example.ui.theme.DuoBeetlePurple.copy(alpha = 0.28f)
                        )
                    ),
                    topLeft = Offset(frameInset, frameInset),
                    size = Size((w - frameInset * 2f).coerceAtLeast(0f), (h - frameInset * 2f).coerceAtLeast(0f)),
                    cornerRadius = androidx.compose.ui.geometry.CornerRadius(frameCorner, frameCorner),
                    style = Stroke(width = 2.dp.toPx())
                )
            }
        }
    }
}



/**
 * Komponen Teks Pintar untuk Nama Folder / File / Judul Panjang:
 * - Otomatis memotong teks panjang (maxLines = 1, overflow = TextOverflow.Ellipsis).
 * - Jika ditekan & ditahan selama 1,5 detik (1500ms) tanpa menggeser layar (scroll),
 *   menampilkan Floating 3D Tooltip Bubble tepat di atas item berisi nama lengkap utuh.
 * - Ketukan singkat (tap) tetap diteruskan ke aksi utama jika disediakan.
 */
@Composable
fun TruncatedHoldTooltipText(
    text: String,
    modifier: Modifier = Modifier,
    style: TextStyle = MaterialTheme.typography.titleMedium,
    color: Color = CosmicTextPure,
    fontWeight: FontWeight? = FontWeight.Bold,
    maxLines: Int = 1,
    textAlign: TextAlign? = null,
    onTap: (() -> Unit)? = null,
    onClick: (() -> Unit)? = null
) {
    val scope = rememberCoroutineScope()
    val haptic = androidx.compose.ui.platform.LocalHapticFeedback.current
    val currentOnTap by rememberUpdatedState(onClick ?: onTap)
    var showFullNameBubble by remember { mutableStateOf(false) }

    androidx.compose.runtime.LaunchedEffect(showFullNameBubble) {
        if (showFullNameBubble) {
            kotlinx.coroutines.delay(4000L)
            showFullNameBubble = false
        }
    }

    Box(modifier = modifier) {
        if (showFullNameBubble) {
            androidx.compose.ui.window.Popup(
                alignment = Alignment.TopCenter,
                offset = androidx.compose.ui.unit.IntOffset(0, -88),
                onDismissRequest = { showFullNameBubble = false }
            ) {
                Box(
                    modifier = Modifier
                        .widthIn(min = 160.dp, max = 330.dp)
                        .duolingoCard(
                            backgroundColor = com.example.ui.theme.DuoPureWhite,
                            borderColor = com.example.ui.theme.DuoMacawBlue,
                            lipColor = com.example.ui.theme.DuoMacawBlueLip,
                            cornerRadius = 16.dp,
                            borderWidth = 2.dp,
                            lipHeight = 4.dp
                        )
                        .clickable { showFullNameBubble = false }
                        .padding(horizontal = 14.dp, vertical = 10.dp)
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(3.dp)) {
                        Text(
                            text = "NAMA LENGKAP (KETUK UNTUK TUTUP)",
                            fontSize = 9.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = com.example.ui.theme.DuoMacawBlueLip
                        )
                        Text(
                            text = text,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.ExtraBold,
                            color = com.example.ui.theme.DuoEelInk
                        )
                    }
                }
            }
        }

        Text(
            text = text,
            style = style,
            color = color,
            fontWeight = fontWeight,
            maxLines = maxLines,
            overflow = TextOverflow.Ellipsis,
            textAlign = textAlign,
            modifier = Modifier.pointerInput(text) {
                val touchSlop = viewConfiguration.touchSlop
                awaitEachGesture {
                    val down = awaitFirstDown(requireUnconsumed = false)
                    val downPos = down.position
                    var holdTriggered = false
                    val holdJob = scope.launch {
                        kotlinx.coroutines.delay(1500L)
                        holdTriggered = true
                        haptic.performHapticFeedback(
                            androidx.compose.ui.hapticfeedback.HapticFeedbackType.LongPress
                        )
                        showFullNameBubble = true
                    }

                    var movedPastSlop = false
                    var releasedCleanly = false
                    while (true) {
                        val event = awaitPointerEvent()
                        val change = event.changes.firstOrNull { it.id == down.id } ?: break
                        if (!change.pressed) {
                            releasedCleanly = !movedPastSlop && !change.isConsumed
                            if (holdTriggered) {
                                change.consume()
                            }
                            break
                        }
                        if ((change.position - downPos).getDistance() > touchSlop || change.isConsumed) {
                            movedPastSlop = true
                            holdJob.cancel()
                            break
                        }
                    }
                    holdJob.cancel()
                    if (releasedCleanly && !holdTriggered && currentOnTap != null) {
                        currentOnTap?.invoke()
                    }
                }
            }
        )
    }
}

enum class MascotElement(
    val label: String,
    val shortLabel: String,
    val badgeColor: Color,
    val lipColor: Color
) {
    LOVE("Elemen Kasih Sayang (Love)", "Love", Color(0xFFEC4899), Color(0xFFBE185D)),
    ELECTRIC("Elemen Energi Listrik", "Listrik", Color(0xFFEAB308), Color(0xFFA16207)),
    IDEA_BULB("Elemen Lampu Ide Pintar", "Lampu Ide", Color(0xFF58CC02), Color(0xFF46A302)),
    FIRE("Elemen Aura Api Membara", "Api", Color(0xFFEF4444), Color(0xFFB91C1C)),
    TEARS("Elemen Air Mata Deras", "Air Mata", Color(0xFF0EA5E9), Color(0xFF0369A1)),
    SHIELD("Elemen Perisai Siber", "Perisai", Color(0xFF8B5CF6), Color(0xFF6D28D9))
}

/**
 * Unified Kawaii File Folder Mascot Avatar:
 * Menampilkan karakter File Folder 3D bergaya Duolingo dengan:
 * - 6 Elemen Visual Dinamis (Love, Listrik, Lampu Ide, Api, Air Mata, Perisai)
 * - Indikator Kebutuhan di Atas Kepala (Overhead Need Bubble)
 * - Perubahan Warna Tubuh Otomatis mengikuti Elemen & Mood aktif
 * - Animasi 3D Ekspresi Wajah Dinamis (mata berkilau/hati/petir, mulut ekspresif, tangan melambai)
 */
@Composable
fun CosmicDragonAvatar(
    mood: MascotMood,
    modifier: Modifier = Modifier,
    size: Dp = 112.dp,
    persona: MascotPersona = MascotPersona.KAWAII_FOLDER,
    externalCheerKey: Int = 0,
    activeElement: MascotElement? = null,
    neededElement: MascotElement? = null,
    isPetting: Boolean = false
) {
    val scope = rememberCoroutineScope()
    val infiniteTransition = rememberInfiniteTransition(label = "kawaii_folder_anim")

    // 1. Breathing & Gentle Hover Bounce (0f..1f)
    val breathePhase by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1100, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "folder_breathe"
    )

    // 2. Hand Wave & Tilt Phase (-1f..1f)
    val wavePhase by infiniteTransition.animateFloat(
        initialValue = -1f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 620, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "folder_wave"
    )

    // 3. Natural Eye Blink Cycle
    val blinkHeightRatio by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = keyframes {
                durationMillis = 3600
                1f at 0
                1f at 2800
                0.08f at 2920
                1f at 3040
                1f at 3180
                0.08f at 3280
                1f at 3400
                1f at 3600
            },
            repeatMode = RepeatMode.Restart
        ),
        label = "folder_blink"
    )

    // 4. Eye Look Phase
    val lookPhase by infiniteTransition.animateFloat(
        initialValue = -1f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 2400, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "folder_look"
    )

    // 5. Continuous FX Particle Phase (0f..1f) for Love Hearts, Lightning, Idea Bulb, Fire, Tears & Shield
    val fxFlowPhase by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 850, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "folder_fx_flow"
    )

    // 6. Autonomous Random Emotion Cycling + Tap Emotion Switch
    val expressiveMoodPool = remember {
        listOf(
            MascotMood.CRYING_FOUNTAIN,
            MascotMood.RAGE_INFERNO,
            MascotMood.SUCCESS_LEAP,
            MascotMood.SHOCKED_JOLT,
            MascotMood.SHY_BLUSH,
            MascotMood.SLEEPY_DREAM
        )
    }
    var randomMoodOverride by remember { mutableStateOf<MascotMood?>(null) }
    var tapEmotionSeed by remember { mutableIntStateOf(0) }

    androidx.compose.runtime.LaunchedEffect(mood, tapEmotionSeed, activeElement) {
        if (mood == MascotMood.PIN_SHIELD || mood == MascotMood.SHIELD_EYES || activeElement != null) {
            randomMoodOverride = null
            return@LaunchedEffect
        }
        while (true) {
            kotlinx.coroutines.delay(4200L)
            randomMoodOverride = expressiveMoodPool.random()
        }
    }

    // 7. Tap / Cheer Burst Animation
    val tapCheerProgress = remember { Animatable(0f) }

    androidx.compose.runtime.LaunchedEffect(externalCheerKey, activeElement) {
        if (externalCheerKey > 0 || activeElement != null) {
            if (activeElement == null) {
                randomMoodOverride = expressiveMoodPool[externalCheerKey % expressiveMoodPool.size]
            }
            tapCheerProgress.snapTo(0f)
            tapCheerProgress.animateTo(
                targetValue = 1f,
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessLow
                )
            )
        }
    }

    Box(
        modifier = modifier
            .size(size)
            .instantTouchPress {
                tapEmotionSeed++
                randomMoodOverride = expressiveMoodPool[(tapEmotionSeed + (0..5).random()) % expressiveMoodPool.size]
                scope.launch {
                    tapCheerProgress.snapTo(0f)
                    tapCheerProgress.animateTo(
                        targetValue = 1f,
                        animationSpec = spring(
                            dampingRatio = Spring.DampingRatioMediumBouncy,
                            stiffness = Spring.StiffnessLow
                        )
                    )
                }
            }
            .testTag("nyx_mascot_avatar"),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val w = this.size.width
            val h = this.size.height
            val cx = w * 0.5f
            val cy = h * 0.56f
            val r = (w.coerceAtMost(h) * 0.40f)

            val baseMappedMood = when {
                isPetting || activeElement == MascotElement.LOVE -> MascotMood.SHY_BLUSH
                activeElement == MascotElement.ELECTRIC -> MascotMood.SHOCKED_JOLT
                activeElement == MascotElement.IDEA_BULB -> MascotMood.SUCCESS_LEAP
                activeElement == MascotElement.FIRE -> MascotMood.RAGE_INFERNO
                activeElement == MascotElement.TEARS -> MascotMood.CRYING_FOUNTAIN
                activeElement == MascotElement.SHIELD -> MascotMood.PIN_SHIELD
                mood == MascotMood.WAVE_HELLO -> MascotMood.IDLE_BREATHE
                mood == MascotMood.GUARD_ALERT -> MascotMood.RAGE_INFERNO
                mood == MascotMood.ALERT_FIRE -> MascotMood.RAGE_INFERNO
                mood == MascotMood.SHIELD_EYES -> MascotMood.PIN_SHIELD
                mood == MascotMood.HAPPY_VIBES -> MascotMood.SUCCESS_LEAP
                mood == MascotMood.STEALTH_SHADOW -> MascotMood.RAGE_INFERNO
                else -> mood
            }
            val effectiveMood = if (baseMappedMood == MascotMood.PIN_SHIELD || activeElement != null || isPetting) {
                baseMappedMood
            } else {
                randomMoodOverride ?: baseMappedMood
            }

            // Dynamic Multi-Color Palette Per Active Element & Emotion
            val bodyGradientColors: List<Color>
            val lipColor: Color
            val tabColors: List<Color>
            val handFootColor: Color
            val auraGlowColor: Color

            when {
                activeElement == MascotElement.LOVE || isPetting || effectiveMood == MascotMood.SHY_BLUSH -> {
                    bodyGradientColors = listOf(
                        Color(0xFFFCE7F3),
                        Color(0xFFF472B6),
                        Color(0xFFEC4899),
                        Color(0xFFBE185D)
                    )
                    lipColor = Color(0xFF9D174D)
                    tabColors = listOf(Color(0xFFF9A8D4), Color(0xFFDB2777))
                    handFootColor = Color(0xFFDB2777)
                    auraGlowColor = Color(0xFFF472B6)
                }
                activeElement == MascotElement.ELECTRIC || effectiveMood == MascotMood.SHOCKED_JOLT -> {
                    bodyGradientColors = listOf(
                        Color(0xFFFEF9C3),
                        Color(0xFFFDE047),
                        Color(0xFFEAB308),
                        Color(0xFFCA8A04)
                    )
                    lipColor = Color(0xFFA16207)
                    tabColors = listOf(Color(0xFF38BDF8), Color(0xFF0284C7))
                    handFootColor = Color(0xFFEAB308)
                    auraGlowColor = Color(0xFFFACC15)
                }
                activeElement == MascotElement.IDEA_BULB || effectiveMood == MascotMood.SUCCESS_LEAP -> {
                    bodyGradientColors = listOf(
                        Color(0xFFD9F99D),
                        Color(0xFF84CC16),
                        Color(0xFF58CC02),
                        Color(0xFF15803D)
                    )
                    lipColor = Color(0xFF14532D)
                    tabColors = listOf(Color(0xFFFDE047), Color(0xFFF59E0B))
                    handFootColor = Color(0xFF46A302)
                    auraGlowColor = Color(0xFF84CC16)
                }
                activeElement == MascotElement.FIRE || effectiveMood == MascotMood.RAGE_INFERNO -> {
                    bodyGradientColors = listOf(
                        Color(0xFFFCA5A5),
                        Color(0xFFEF4444),
                        Color(0xFFDC2626),
                        Color(0xFF991B1B)
                    )
                    lipColor = Color(0xFF7F1D1D)
                    tabColors = listOf(Color(0xFFF97316), Color(0xFFDC2626))
                    handFootColor = Color(0xFFEA580C)
                    auraGlowColor = Color(0xFFEF4444)
                }
                activeElement == MascotElement.TEARS || effectiveMood == MascotMood.CRYING_FOUNTAIN -> {
                    bodyGradientColors = listOf(
                        Color(0xFFBAE6FD),
                        Color(0xFF38BDF8),
                        Color(0xFF0284C7),
                        Color(0xFF0369A1)
                    )
                    lipColor = Color(0xFF075985)
                    tabColors = listOf(Color(0xFF7DD3FC), Color(0xFF0284C7))
                    handFootColor = Color(0xFF0284C7)
                    auraGlowColor = Color(0xFF38BDF8)
                }
                activeElement == MascotElement.SHIELD || effectiveMood == MascotMood.PIN_SHIELD -> {
                    bodyGradientColors = listOf(
                        Color(0xFFEDE9FE),
                        Color(0xFFA78BFA),
                        Color(0xFF7C3AED),
                        Color(0xFF5B21B6)
                    )
                    lipColor = Color(0xFF4C1D95)
                    tabColors = listOf(Color(0xFF38BDF8), Color(0xFF6366F1))
                    handFootColor = Color(0xFF6D28D9)
                    auraGlowColor = Color(0xFF8B5CF6)
                }
                effectiveMood == MascotMood.SLEEPY_DREAM -> {
                    bodyGradientColors = listOf(
                        Color(0xFFF3E8FF),
                        Color(0xFFC084FC),
                        Color(0xFF9333EA),
                        Color(0xFF6B21A8)
                    )
                    lipColor = Color(0xFF581C87)
                    tabColors = listOf(Color(0xFFD8B4FE), Color(0xFF7E22CE))
                    handFootColor = Color(0xFF7E22CE)
                    auraGlowColor = Color(0xFFA855F7)
                }
                else -> {
                    bodyGradientColors = listOf(
                        Color(0xFFFFE066),
                        Color(0xFFFBBF24),
                        Color(0xFFF59E0B),
                        Color(0xFFD97706)
                    )
                    lipColor = Color(0xFFB45309)
                    tabColors = listOf(Color(0xFF38BDF8), Color(0xFF0284C7))
                    handFootColor = Color(0xFFD97706)
                    auraGlowColor = persona.badgeColor
                }
            }

            val cheerSin = sin(tapCheerProgress.value * PI).toFloat().coerceAtLeast(0f)
            val continuousAngle = fxFlowPhase * (2f * PI.toFloat())
            val smoothBreathe = sin(breathePhase * PI.toFloat() - (PI.toFloat() * 0.5f)) * 0.5f + 0.5f

            val rageShakeX = when {
                activeElement == MascotElement.ELECTRIC || effectiveMood == MascotMood.SHOCKED_JOLT ->
                    sin(fxFlowPhase * PI * 10).toFloat() * (r * 0.05f)
                activeElement == MascotElement.FIRE || effectiveMood == MascotMood.RAGE_INFERNO ->
                    sin(fxFlowPhase * PI * 8).toFloat() * (r * 0.055f)
                activeElement == MascotElement.TEARS || effectiveMood == MascotMood.CRYING_FOUNTAIN ->
                    sin(fxFlowPhase * PI * 4).toFloat() * (r * 0.032f)
                else -> 0f
            }

            val jumpOffsetY = -smoothBreathe * r * 0.14f - cheerSin * r * 0.36f
            val stretchFactor = (1f + smoothBreathe * 0.045f + cheerSin * 0.16f).coerceIn(0.82f, 1.24f)
            val squashX = (1f / stretchFactor).coerceIn(0.82f, 1.22f)
            val squashY = stretchFactor
            val tiltDegrees = sin(wavePhase * (PI.toFloat() * 0.5f)) * 4.2f +
                (sin(tapCheerProgress.value * PI * 2).toFloat() * 12f) +
                (if (isPetting) sin(fxFlowPhase * PI * 4).toFloat() * 6f else 0f)

            val paperSecondaryLagY = cos(breathePhase * PI.toFloat()) * (r * 0.08f) - cheerSin * (r * 0.15f)
            val footPendulumSwing = sin(wavePhase * (PI.toFloat() * 0.5f) - 0.6f) * (r * 0.07f)

            val studioPose = com.example.ui.theme.AppThemeController.mascotActivityPose
            val studioAccessory = com.example.ui.theme.AppThemeController.mascotAccessory
            val studioCosmicAura = com.example.ui.theme.AppThemeController.mascotCosmicAuraEnabled
            val effectivePose = if (studioPose == com.example.ui.theme.MascotActivityPose.AUTO_SMART &&
                com.example.ui.theme.AppThemeController.isMediaWatchingActive
            ) {
                com.example.ui.theme.MascotActivityPose.WATCHING_MEDIA
            } else {
                studioPose
            }
            val continuousTimeSec = fxFlowPhase * 4f + breathePhase * 2f

            // 0A. Multi-Layered Dynamic Aura (16 Styles) + Emotion Aura Backdrop
            if (studioCosmicAura || com.example.ui.theme.AppThemeController.mascotAura != com.example.ui.theme.MascotAuraStyle.NONE) {
                drawMascotMultiLayeredCosmicAura(
                    cx = cx,
                    cy = cy,
                    r = r,
                    timeSec = continuousTimeSec,
                    primaryAuraColor = auraGlowColor,
                    secondaryAuraColor = com.example.ui.theme.AppThemeController.resolvedSecondaryAccent
                )
            }
            val badgeBrush = Brush.radialGradient(
                colors = listOf(
                    auraGlowColor.copy(alpha = 0.38f + cheerSin * 0.22f),
                    auraGlowColor.copy(alpha = 0.16f),
                    Color.Transparent
                ),
                center = Offset(cx, cy),
                radius = r * 1.55f
            )
            drawCircle(brush = badgeBrush, radius = r * 1.50f, center = Offset(cx, cy))

            // 0B. ELEMENT: FIRE AURA (When FIRE or RAGE_INFERNO)
            if (activeElement == MascotElement.FIRE || effectiveMood == MascotMood.RAGE_INFERNO) {
                for (flameIdx in 0..8) {
                    val angle = (flameIdx / 9f) * PI.toFloat() + PI.toFloat()
                    val flamePhase = (fxFlowPhase + flameIdx * 0.13f) % 1f
                    val baseX = cx + cos(angle) * (r * 0.92f)
                    val baseY = cy + sin(angle) * (r * 0.65f)
                    val flameTipY = baseY - (r * (0.45f + flamePhase * 0.55f))
                    val flameW = r * (0.28f * (1f - flamePhase * 0.45f))

                    val flamePath = Path().apply {
                        moveTo(baseX - flameW, baseY + r * 0.2f)
                        quadraticTo(
                            baseX - flameW * 0.6f,
                            (baseY + flameTipY) * 0.5f,
                            baseX + sin((flamePhase + flameIdx) * PI * 2).toFloat() * (r * 0.10f),
                            flameTipY
                        )
                        quadraticTo(
                            baseX + flameW * 0.6f,
                            (baseY + flameTipY) * 0.5f,
                            baseX + flameW,
                            baseY + r * 0.2f
                        )
                        close()
                    }
                    drawPath(
                        path = flamePath,
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                Color(0xFFFDE047).copy(alpha = 0.92f * (1f - flamePhase * 0.4f)),
                                Color(0xFFF97316).copy(alpha = 0.88f * (1f - flamePhase * 0.3f)),
                                Color(0xFFDC2626).copy(alpha = 0.82f)
                            ),
                            startY = flameTipY,
                            endY = baseY + r * 0.2f
                        )
                    )
                }
            }

            // Dynamic Floor Shadow
            val shadowScale = (1f - smoothBreathe * 0.25f - cheerSin * 0.40f).coerceIn(0.35f, 1.1f)
            drawOval(
                color = if (activeElement == MascotElement.TEARS || effectiveMood == MascotMood.CRYING_FOUNTAIN) {
                    Color(0xFF38BDF8).copy(alpha = 0.45f)
                } else {
                    Color.Black.copy(alpha = 0.16f * shadowScale)
                },
                topLeft = Offset(cx - r * 0.82f * shadowScale, h * 0.88f),
                size = Size(r * 1.64f * shadowScale, h * 0.08f)
            )

            val poseSpinBonus = when (effectivePose) {
                com.example.ui.theme.MascotActivityPose.ACROBATIC_SPIN -> fxFlowPhase * 360f
                com.example.ui.theme.MascotActivityPose.DANCING -> sin(fxFlowPhase * PI * 4).toFloat() * 10f
                com.example.ui.theme.MascotActivityPose.WATCHING_MEDIA -> sin(fxFlowPhase * PI * 2).toFloat() * 4f
                else -> 0f
            }

            val studioSkin = com.example.ui.theme.AppThemeController.mascotSkin
            val studioOutfit = com.example.ui.theme.AppThemeController.mascotOutfit
            val studioGloves = com.example.ui.theme.AppThemeController.mascotGloves
            val resolvedSkin = resolveMascotSkinPalette(
                skin = studioSkin,
                defaultBodyGradient = bodyGradientColors,
                defaultLip = lipColor,
                defaultTab = tabColors,
                defaultHand = handFootColor,
                defaultHandLip = lipColor
            )

            // Floating Hover Rings (No Legs / No Pants)
            drawMascotFloatingHoverRings(
                cx = cx,
                cy = cy,
                r = r,
                timeSec = continuousTimeSec,
                accentColor = auraGlowColor
            )

            // Kawaii File Folder Mascot Body & Anatomy (Floating without legs)
            withTransform({
                translate(left = rageShakeX, top = jumpOffsetY)
                rotate(degrees = tiltDegrees + poseSpinBonus, pivot = Offset(cx, cy + r * 0.2f))
                scale(scaleX = squashX, scaleY = squashY, pivot = Offset(cx, cy + r * 0.5f))
            }) {
                // 0. Back Wings & Royal/Mage Cape
                drawMascotBackAccessoriesAndCape(
                    cx = cx,
                    cy = cy,
                    r = r,
                    timeSec = continuousTimeSec,
                    accessory = studioAccessory,
                    outfit = studioOutfit
                )

                // 1. Folder Tab Peek
                val tabW = r * 0.86f
                val tabH = r * 0.50f
                val tabX = cx - r * 0.78f
                val tabY = cy - r * 0.88f + paperSecondaryLagY * 0.35f
                drawRoundRect(
                    brush = Brush.linearGradient(
                        resolvedSkin.tabColors,
                        start = Offset(tabX, tabY),
                        end = Offset(tabX + tabW, tabY + tabH)
                    ),
                    topLeft = Offset(tabX, tabY),
                    size = Size(tabW, tabH),
                    cornerRadius = CornerRadius(r * 0.16f, r * 0.16f)
                )

                // 2. Dual Document Paper Sheets
                rotate(degrees = -3.8f + tiltDegrees * 0.35f, pivot = Offset(cx, cy - r * 0.55f)) {
                    drawRoundRect(
                        color = Color(0xFFE0F2FE),
                        topLeft = Offset(cx - r * 0.38f, cy - r * 0.96f + paperSecondaryLagY * 1.15f),
                        size = Size(r * 0.96f, r * 0.44f),
                        cornerRadius = CornerRadius(r * 0.10f, r * 0.10f)
                    )
                }
                rotate(degrees = 2.4f - tiltDegrees * 0.28f, pivot = Offset(cx, cy - r * 0.55f)) {
                    val paperW = r * 1.06f
                    val paperH = r * 0.44f
                    val paperX = cx - r * 0.52f
                    val paperY = cy - r * 0.92f + paperSecondaryLagY
                    drawRoundRect(
                        color = Color.White,
                        topLeft = Offset(paperX, paperY),
                        size = Size(paperW, paperH),
                        cornerRadius = CornerRadius(r * 0.10f, r * 0.10f)
                    )
                    drawLine(
                        color = resolvedSkin.tabColors.first().copy(alpha = 0.58f),
                        start = Offset(paperX + r * 0.16f, paperY + r * 0.14f),
                        end = Offset(paperX + paperW - r * 0.16f, paperY + r * 0.14f),
                        strokeWidth = (w * 0.018f).coerceAtLeast(1.5f),
                        cap = StrokeCap.Round
                    )
                }

                // 4. Main Folder Squircle Body (Auto-Morphs Color by Skin / Element & Mood)
                val bodyW = r * 1.88f
                val bodyH = r * 1.52f
                val bodyX = cx - r * 0.94f
                val bodyY = cy - r * 0.68f
                val bodyRadius = r * 0.36f
                drawRoundRect(
                    color = resolvedSkin.lipColor,
                    topLeft = Offset(bodyX, bodyY + r * 0.08f),
                    size = Size(bodyW, bodyH),
                    cornerRadius = CornerRadius(bodyRadius, bodyRadius)
                )
                drawRoundRect(
                    brush = Brush.verticalGradient(
                        colors = resolvedSkin.bodyGradientColors,
                        startY = bodyY,
                        endY = bodyY + bodyH
                    ),
                    topLeft = Offset(bodyX, bodyY),
                    size = Size(bodyW, bodyH),
                    cornerRadius = CornerRadius(bodyRadius, bodyRadius)
                )

                // 4B. Skin Material Surface Texture
                drawMascotSkinSurfaceTexture(
                    bodyX = bodyX,
                    bodyY = bodyY,
                    bodyW = bodyW,
                    bodyH = bodyH,
                    r = r,
                    timeSec = continuousTimeSec,
                    skin = studioSkin
                )

                // 4C. Upper Outfit / Shirt / Cloak / Armor Layer (No Pants / No Legs)
                drawMascotUpperOutfitLayer(
                    bodyX = bodyX,
                    bodyY = bodyY,
                    bodyW = bodyW,
                    bodyH = bodyH,
                    cx = cx,
                    cy = cy,
                    r = r,
                    outfit = studioOutfit
                )

                // Top Rim Glossy Highlight
                val specularShift = (-tiltDegrees / 20f) * (r * 0.10f)
                drawRoundRect(
                    color = Color.White.copy(alpha = 0.46f),
                    topLeft = Offset(bodyX + r * 0.16f + specularShift, bodyY + r * 0.08f),
                    size = Size(bodyW - r * 0.32f, r * 0.14f),
                    cornerRadius = CornerRadius(r * 0.07f, r * 0.07f)
                )
                drawCircle(
                    color = Color.White.copy(alpha = 0.62f),
                    radius = r * 0.07f,
                    center = Offset(bodyX + r * 0.30f + specularShift, bodyY + r * 0.34f)
                )

                // 5. Eyes & Facial Expression + Elemental Eye Pupils
                val eyeW = r * 0.28f
                val fullEyeH = r * 0.38f
                val eyeH = (fullEyeH * blinkHeightRatio).coerceAtLeast(r * 0.04f)
                val smoothGaze = sin(lookPhase * (PI.toFloat() * 0.5f)) * (r * 0.055f)
                val leftEyeX = cx - r * 0.42f + smoothGaze
                val rightEyeX = cx + r * 0.42f + smoothGaze
                val eyeY = cy - r * 0.06f + cos(continuousAngle) * (r * 0.015f)

                val customEmotionDrawn = drawMascotExpressiveEmotionFace(
                    cx = cx,
                    cy = cy,
                    r = r,
                    w = w,
                    timeSec = continuousTimeSec,
                    leftEyeX = leftEyeX,
                    rightEyeX = rightEyeX,
                    eyeY = eyeY,
                    eyeW = eyeW,
                    eyeH = eyeH,
                    fullEyeH = fullEyeH,
                    emotion = com.example.ui.theme.AppThemeController.mascotEmotion
                )

                if (!customEmotionDrawn) {
                when {
                    activeElement == MascotElement.TEARS || effectiveMood == MascotMood.CRYING_FOUNTAIN -> {
                        val strokeW = (w * 0.036f).coerceAtLeast(2.6f)
                        val leftCryEye = Path().apply {
                            moveTo(leftEyeX - eyeW * 0.48f, eyeY - eyeH * 0.22f)
                            lineTo(leftEyeX + eyeW * 0.28f, eyeY)
                            lineTo(leftEyeX - eyeW * 0.48f, eyeY + eyeH * 0.22f)
                        }
                        val rightCryEye = Path().apply {
                            moveTo(rightEyeX + eyeW * 0.48f, eyeY - eyeH * 0.22f)
                            lineTo(rightEyeX - eyeW * 0.28f, eyeY)
                            lineTo(rightEyeX + eyeW * 0.48f, eyeY + eyeH * 0.22f)
                        }
                        drawPath(leftCryEye, color = Color(0xFF0F172A), style = Stroke(width = strokeW, cap = StrokeCap.Round))
                        drawPath(rightCryEye, color = Color(0xFF0F172A), style = Stroke(width = strokeW, cap = StrokeCap.Round))

                        val fountainStroke = (w * 0.055f).coerceAtLeast(3.8f)
                        val leftGeyser = Path().apply {
                            moveTo(leftEyeX, eyeY)
                            cubicTo(
                                leftEyeX - r * 0.22f, eyeY - r * 0.85f,
                                leftEyeX - r * 0.92f, eyeY - r * 0.55f,
                                leftEyeX - r * 1.05f, cy + r * 0.86f
                            )
                        }
                        val rightGeyser = Path().apply {
                            moveTo(rightEyeX, eyeY)
                            cubicTo(
                                rightEyeX + r * 0.22f, eyeY - r * 0.85f,
                                rightEyeX + r * 0.92f, eyeY - r * 0.55f,
                                rightEyeX + r * 1.05f, cy + r * 0.86f
                            )
                        }
                        drawPath(
                            path = leftGeyser,
                            brush = Brush.verticalGradient(
                                listOf(Color(0xFFE0F2FE), Color(0xFF38BDF8), Color(0xFF0284C7))
                            ),
                            style = Stroke(width = fountainStroke, cap = StrokeCap.Round)
                        )
                        drawPath(
                            path = rightGeyser,
                            brush = Brush.verticalGradient(
                                listOf(Color(0xFFE0F2FE), Color(0xFF38BDF8), Color(0xFF0284C7))
                            ),
                            style = Stroke(width = fountainStroke, cap = StrokeCap.Round)
                        )

                        for (dropIdx in 0..5) {
                            val t = (fxFlowPhase + dropIdx * 0.16f) % 1f
                            val arcY = eyeY - sin(t * PI).toFloat() * (r * 0.68f) + (t * t * r * 0.95f)
                            val leftDropX = leftEyeX - t * (r * 1.02f)
                            val rightDropX = rightEyeX + t * (r * 1.02f)
                            val dropR = (r * 0.08f) * (1f - t * 0.25f)
                            drawCircle(Color(0xFFE0F2FE), radius = dropR, center = Offset(leftDropX, arcY))
                            drawCircle(Color(0xFFE0F2FE), radius = dropR, center = Offset(rightDropX, arcY))
                        }
                    }

                    activeElement == MascotElement.FIRE || effectiveMood == MascotMood.RAGE_INFERNO -> {
                        drawOval(
                            color = Color(0xFF450A0A),
                            topLeft = Offset(leftEyeX - eyeW * 0.5f, eyeY - fullEyeH * 0.42f),
                            size = Size(eyeW, fullEyeH * 0.84f)
                        )
                        drawCircle(Color(0xFFFDE047), radius = eyeW * 0.24f, center = Offset(leftEyeX, eyeY - fullEyeH * 0.08f))

                        drawOval(
                            color = Color(0xFF450A0A),
                            topLeft = Offset(rightEyeX - eyeW * 0.5f, eyeY - fullEyeH * 0.42f),
                            size = Size(eyeW, fullEyeH * 0.84f)
                        )
                        drawCircle(Color(0xFFFDE047), radius = eyeW * 0.24f, center = Offset(rightEyeX, eyeY - fullEyeH * 0.08f))

                        val browStroke = (w * 0.040f).coerceAtLeast(3f)
                        drawLine(
                            color = Color(0xFF450A0A),
                            start = Offset(leftEyeX - eyeW * 0.65f, eyeY - fullEyeH * 0.62f),
                            end = Offset(leftEyeX + eyeW * 0.55f, eyeY - fullEyeH * 0.22f),
                            strokeWidth = browStroke,
                            cap = StrokeCap.Round
                        )
                        drawLine(
                            color = Color(0xFF450A0A),
                            start = Offset(rightEyeX + eyeW * 0.65f, eyeY - fullEyeH * 0.62f),
                            end = Offset(rightEyeX - eyeW * 0.55f, eyeY - fullEyeH * 0.22f),
                            strokeWidth = browStroke,
                            cap = StrokeCap.Round
                        )
                    }

                    effectiveMood == MascotMood.SLEEPY_DREAM -> {
                        val sleepyStroke = (w * 0.032f).coerceAtLeast(2.4f)
                        val leftSleep = Path().apply {
                            moveTo(leftEyeX - eyeW * 0.5f, eyeY - eyeH * 0.1f)
                            quadraticTo(leftEyeX, eyeY + fullEyeH * 0.35f, leftEyeX + eyeW * 0.5f, eyeY - eyeH * 0.1f)
                        }
                        val rightSleep = Path().apply {
                            moveTo(rightEyeX - eyeW * 0.5f, eyeY - eyeH * 0.1f)
                            quadraticTo(rightEyeX, eyeY + fullEyeH * 0.35f, rightEyeX + eyeW * 0.5f, eyeY - eyeH * 0.1f)
                        }
                        drawPath(leftSleep, color = Color(0xFF0F172A), style = Stroke(width = sleepyStroke, cap = StrokeCap.Round))
                        drawPath(rightSleep, color = Color(0xFF0F172A), style = Stroke(width = sleepyStroke, cap = StrokeCap.Round))
                    }

                    activeElement == MascotElement.SHIELD || effectiveMood == MascotMood.PIN_SHIELD -> {
                        val leftArch = Path().apply {
                            moveTo(leftEyeX - eyeW * 0.5f, eyeY)
                            quadraticTo(leftEyeX, eyeY - eyeH * 0.4f, leftEyeX + eyeW * 0.5f, eyeY)
                        }
                        drawPath(leftArch, color = Color(0xFF0F172A), style = Stroke(width = (w * 0.032f).coerceAtLeast(2.4f), cap = StrokeCap.Round))
                        drawRoundRect(
                            color = Color(0xFF38BDF8),
                            topLeft = Offset(rightEyeX - eyeW * 0.72f, eyeY - eyeH * 0.52f),
                            size = Size(eyeW * 1.44f, eyeH * 1.15f),
                            cornerRadius = CornerRadius(r * 0.12f, r * 0.12f)
                        )
                    }

                    else -> {
                        val shockScale = if (activeElement == MascotElement.ELECTRIC || effectiveMood == MascotMood.SHOCKED_JOLT) 1.22f else 1f
                        for (ex in listOf(leftEyeX, rightEyeX)) {
                            drawOval(
                                color = Color(0xFF0F172A),
                                topLeft = Offset(ex - eyeW * 0.5f * shockScale, eyeY - eyeH * 0.5f * shockScale),
                                size = Size(eyeW * shockScale, eyeH * shockScale)
                            )
                            drawOval(
                                color = when (activeElement) {
                                    MascotElement.LOVE -> Color(0xFF9D174D)
                                    MascotElement.ELECTRIC -> Color(0xFF0284C7)
                                    MascotElement.IDEA_BULB -> Color(0xFF15803D)
                                    else -> Color(0xFF1E3A8A)
                                },
                                topLeft = Offset(ex - eyeW * 0.42f, eyeY - eyeH * 0.40f),
                                size = Size(eyeW * 0.84f, eyeH * 0.80f)
                            )
                            if (blinkHeightRatio > 0.35f) {
                                if (activeElement == MascotElement.LOVE || isPetting) {
                                    // Sparkling Heart Pupil Highlight
                                    val hr = eyeW * 0.22f
                                    val heartPupil = Path().apply {
                                        moveTo(ex, eyeY + hr * 0.55f)
                                        cubicTo(ex - hr * 1.2f, eyeY - hr * 0.2f, ex - hr * 0.6f, eyeY - hr * 1.1f, ex, eyeY - hr * 0.35f)
                                        cubicTo(ex + hr * 0.6f, eyeY - hr * 1.1f, ex + hr * 1.2f, eyeY - hr * 0.2f, ex, eyeY + hr * 0.55f)
                                        close()
                                    }
                                    drawPath(heartPupil, color = Color(0xFFF9A8D4))
                                } else {
                                    drawCircle(Color.White, radius = eyeW * 0.24f, center = Offset(ex - eyeW * 0.14f, eyeY - eyeH * 0.22f))
                                    drawCircle(Color.White, radius = eyeW * 0.12f, center = Offset(ex + eyeW * 0.16f, eyeY + eyeH * 0.18f))
                                }
                            }
                        }
                    }
                }

                // 6. Blush Cheeks
                val isLoveBlush = activeElement == MascotElement.LOVE || isPetting || effectiveMood == MascotMood.SHY_BLUSH
                val blushW = if (isLoveBlush) r * 0.32f else r * 0.22f
                val blushH = if (isLoveBlush) r * 0.17f else r * 0.12f
                val blushAlpha = if (isLoveBlush) 0.88f else (0.48f + cheerSin * 0.35f)
                drawOval(
                    color = Color(0xFFF43F5E).copy(alpha = blushAlpha),
                    topLeft = Offset(cx - r * 0.68f, cy + r * 0.18f),
                    size = Size(blushW, blushH)
                )
                drawOval(
                    color = Color(0xFFF43F5E).copy(alpha = blushAlpha),
                    topLeft = Offset(cx + r * 0.46f, cy + r * 0.18f),
                    size = Size(blushW, blushH)
                )

                // 7. Expressive Mouth per Emotion & Element
                val mouthY = cy + r * 0.22f
                when {
                    activeElement == MascotElement.TEARS || effectiveMood == MascotMood.CRYING_FOUNTAIN -> {
                        val wailMouth = Path().apply {
                            moveTo(cx - r * 0.24f, mouthY + r * 0.18f)
                            quadraticTo(cx, mouthY - r * 0.16f, cx + r * 0.24f, mouthY + r * 0.18f)
                            close()
                        }
                        drawPath(wailMouth, color = Color(0xFF0F172A))
                        drawOval(
                            color = Color(0xFFF43F5E),
                            topLeft = Offset(cx - r * 0.12f, mouthY + r * 0.04f),
                            size = Size(r * 0.24f, r * 0.12f)
                        )
                    }
                    activeElement == MascotElement.FIRE || effectiveMood == MascotMood.RAGE_INFERNO -> {
                        val snarlPath = Path().apply {
                            moveTo(cx - r * 0.22f, mouthY + r * 0.14f)
                            quadraticTo(cx, mouthY - r * 0.12f, cx + r * 0.22f, mouthY + r * 0.14f)
                        }
                        drawPath(
                            snarlPath,
                            color = Color(0xFF450A0A),
                            style = Stroke(width = (w * 0.036f).coerceAtLeast(2.6f), cap = StrokeCap.Round)
                        )
                    }
                    effectiveMood == MascotMood.SHOCKED_JOLT || effectiveMood == MascotMood.SLEEPY_DREAM -> {
                        drawCircle(color = Color(0xFF0F172A), radius = r * 0.09f, center = Offset(cx, mouthY + r * 0.06f))
                    }
                    else -> {
                        if (activeElement != null || isPetting || effectiveMood == MascotMood.SUCCESS_LEAP || cheerSin > 0.15f) {
                            val openMouth = Path().apply {
                                moveTo(cx - r * 0.19f, mouthY)
                                quadraticTo(cx, mouthY + r * 0.24f, cx + r * 0.19f, mouthY)
                                close()
                            }
                            drawPath(openMouth, color = Color(0xFF0F172A))
                            drawCircle(color = Color(0xFFF43F5E), radius = r * 0.085f, center = Offset(cx, mouthY + r * 0.12f))
                        } else {
                            val smilePath = Path().apply {
                                moveTo(cx - r * 0.16f, mouthY)
                                quadraticTo(cx, mouthY + r * 0.16f, cx + r * 0.16f, mouthY)
                            }
                            drawPath(
                                smilePath,
                                color = Color(0xFF0F172A),
                                style = Stroke(width = (w * 0.030f).coerceAtLeast(2.4f), cap = StrokeCap.Round)
                            )
                        }
                    }
                }
                }

                // 8. Cute Waving Folder Hands (Left & Right) + Activity Pose Choreography
                val handWaveY = wavePhase * r * 0.14f
                val leftHandPos = when (effectivePose) {
                    com.example.ui.theme.MascotActivityPose.READING,
                    com.example.ui.theme.MascotActivityPose.GAMING_PRO_PAD -> Offset(cx - r * 0.56f, cy + r * 0.42f)
                    com.example.ui.theme.MascotActivityPose.WATCHING_MEDIA,
                    com.example.ui.theme.MascotActivityPose.EATING_BOBA_SNACK,
                    com.example.ui.theme.MascotActivityPose.COFFEE_CHILL -> Offset(cx - r * 0.86f, cy + r * 0.26f)
                    com.example.ui.theme.MascotActivityPose.DANCING -> Offset(cx - r * 1.10f, cy - r * 0.20f - handWaveY * 1.6f)
                    com.example.ui.theme.MascotActivityPose.GYM_DUMBBELL_LIFT -> Offset(
                        cx - r * 0.65f,
                        cy - r * 0.90f - abs(sin(continuousTimeSec * 4.2f)) * r * 0.28f
                    )
                    else -> Offset(cx - r * 1.04f, cy + r * 0.06f - handWaveY)
                }
                val rightHandPos = when (effectivePose) {
                    com.example.ui.theme.MascotActivityPose.READING,
                    com.example.ui.theme.MascotActivityPose.GAMING_PRO_PAD -> Offset(cx + r * 0.56f, cy + r * 0.42f)
                    com.example.ui.theme.MascotActivityPose.SHIELD_GUARD,
                    com.example.ui.theme.MascotActivityPose.KATANA_SLASH,
                    com.example.ui.theme.MascotActivityPose.ARCANE_SPELLCAST,
                    com.example.ui.theme.MascotActivityPose.SELFIE_FLASH_POSE -> Offset(cx + r * 0.88f, cy + r * 0.16f)
                    com.example.ui.theme.MascotActivityPose.WATCHING_MEDIA,
                    com.example.ui.theme.MascotActivityPose.EATING_BOBA_SNACK -> Offset(cx + r * 0.72f, cy + r * 0.16f - abs(handWaveY))
                    com.example.ui.theme.MascotActivityPose.DANCING -> Offset(cx + r * 1.10f, cy - r * 0.20f + handWaveY * 1.6f)
                    com.example.ui.theme.MascotActivityPose.GYM_DUMBBELL_LIFT -> Offset(
                        cx + r * 0.65f,
                        cy - r * 0.90f - abs(sin(continuousTimeSec * 4.2f)) * r * 0.28f
                    )
                    else -> Offset(cx + r * 1.04f, cy + r * 0.02f + handWaveY)
                }
                drawMascotCustomGlovedHands(
                    leftHandCenter = leftHandPos,
                    rightHandCenter = rightHandPos,
                    r = r,
                    defaultHandColor = resolvedSkin.handColor,
                    defaultHandLipColor = resolvedSkin.handLipColor,
                    gloves = studioGloves
                )

                // 8B. Accessories & Complex Pose Props
                drawMascotComplexAccessoriesAndPoses(
                    cx = cx,
                    cy = cy,
                    r = r,
                    timeSec = continuousTimeSec,
                    leftEyeX = leftEyeX,
                    rightEyeX = rightEyeX,
                    eyeY = eyeY,
                    leftHandCenter = leftHandPos,
                    rightHandCenter = rightHandPos,
                    activityPose = effectivePose,
                    accessory = studioAccessory
                )
            }

            // =========================================================================
            // 9. FOREGROUND ELEMENTAL FX OVERLAYS (LOVE / LISTRIK / LAMPU IDE / PERISAI)
            // =========================================================================

            // 9A. LOVE FX: Floating 3D Hearts Rising Around Mascot
            if (activeElement == MascotElement.LOVE || isPetting || effectiveMood == MascotMood.SHY_BLUSH) {
                val heartAnchors = listOf(
                    Offset(cx - r * 0.95f, cy - r * 0.35f),
                    Offset(cx + r * 0.95f, cy - r * 0.45f),
                    Offset(cx - r * 0.55f, cy - r * 0.95f),
                    Offset(cx + r * 0.62f, cy - r * 0.90f)
                )
                heartAnchors.forEachIndexed { idx, anchor ->
                    val phase = (fxFlowPhase + idx * 0.25f) % 1f
                    val hx = anchor.x + sin((phase + idx) * PI * 2).toFloat() * (r * 0.12f)
                    val hy = anchor.y - phase * (r * 0.45f)
                    val hs = (r * 0.18f) * (0.75f + sin(phase * PI).toFloat() * 0.45f)
                    val heartPath = Path().apply {
                        moveTo(hx, hy + hs * 0.65f)
                        cubicTo(hx - hs * 1.3f, hy - hs * 0.15f, hx - hs * 0.65f, hy - hs * 1.15f, hx, hy - hs * 0.35f)
                        cubicTo(hx + hs * 0.65f, hy - hs * 1.15f, hx + hs * 1.3f, hy - hs * 0.15f, hx, hy + hs * 0.65f)
                        close()
                    }
                    drawPath(
                        path = heartPath,
                        color = if (idx % 2 == 0) Color(0xFFEC4899) else Color(0xFFF43F5E)
                    )
                }
            }

            // 9B. LISTRIK FX: Crackling Electric Lightning Bolts & Plasma Sparks
            if (activeElement == MascotElement.ELECTRIC || effectiveMood == MascotMood.SHOCKED_JOLT) {
                val boltCenters = listOf(
                    Offset(cx - r * 1.05f, cy - r * 0.45f),
                    Offset(cx + r * 1.05f, cy - r * 0.40f),
                    Offset(cx, cy - r * 1.12f)
                )
                boltCenters.forEachIndexed { idx, bCenter ->
                    val pulse = sin((fxFlowPhase + idx * 0.33f) * PI * 2).toFloat() * 0.5f + 0.5f
                    val bw = r * (0.20f + pulse * 0.08f)
                    val bh = r * (0.38f + pulse * 0.10f)
                    val boltPath = Path().apply {
                        moveTo(bCenter.x + bw * 0.25f, bCenter.y - bh * 0.5f)
                        lineTo(bCenter.x - bw * 0.38f, bCenter.y + bh * 0.04f)
                        lineTo(bCenter.x + bw * 0.05f, bCenter.y + bh * 0.04f)
                        lineTo(bCenter.x - bw * 0.25f, bCenter.y + bh * 0.55f)
                        lineTo(bCenter.x + bw * 0.42f, bCenter.y - bh * 0.05f)
                        lineTo(bCenter.x - bw * 0.02f, bCenter.y - bh * 0.05f)
                        close()
                    }
                    drawPath(boltPath, color = Color(0xFFFACC15))
                    drawPath(
                        boltPath,
                        color = Color(0xFFFEF08A),
                        style = Stroke(width = (w * 0.015f).coerceAtLeast(1.2f))
                    )
                }
            }

            // 9C. LAMPU IDE FX: Glowing 3D Lightbulb Floating Above Head + Golden Rays
            if (activeElement == MascotElement.IDEA_BULB) {
                val bulbCenterX = cx
                val bulbCenterY = cy - r * 1.15f + jumpOffsetY * 0.5f
                val bulbR = r * 0.26f

                // Outer golden glow
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            Color(0xFFFDE047).copy(alpha = 0.85f),
                            Color(0xFFF59E0B).copy(alpha = 0.30f),
                            Color.Transparent
                        ),
                        center = Offset(bulbCenterX, bulbCenterY),
                        radius = bulbR * 2.4f
                    ),
                    radius = bulbR * 2.4f,
                    center = Offset(bulbCenterX, bulbCenterY)
                )

                // 6 Radiating Light Rays
                for (rayIdx in 0..5) {
                    val angle = (rayIdx / 6f) * PI.toFloat() + PI.toFloat()
                    val rStart = bulbR * 1.25f
                    val rEnd = bulbR * (1.75f + sin((fxFlowPhase + rayIdx * 0.15f) * PI * 2).toFloat() * 0.25f)
                    drawLine(
                        color = Color(0xFFFACC15),
                        start = Offset(bulbCenterX + cos(angle) * rStart, bulbCenterY + sin(angle) * rStart),
                        end = Offset(bulbCenterX + cos(angle) * rEnd, bulbCenterY + sin(angle) * rEnd),
                        strokeWidth = (w * 0.026f).coerceAtLeast(2f),
                        cap = StrokeCap.Round
                    )
                }

                // Bulb Glass Globe
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(Color(0xFFFEF9C3), Color(0xFFFACC15), Color(0xFFF59E0B)),
                        center = Offset(bulbCenterX - bulbR * 0.2f, bulbCenterY - bulbR * 0.2f),
                        radius = bulbR * 1.2f
                    ),
                    radius = bulbR,
                    center = Offset(bulbCenterX, bulbCenterY)
                )
                // Bulb Screw Base
                drawRoundRect(
                    color = Color(0xFF64748B),
                    topLeft = Offset(bulbCenterX - bulbR * 0.48f, bulbCenterY + bulbR * 0.72f),
                    size = Size(bulbR * 0.96f, bulbR * 0.46f),
                    cornerRadius = CornerRadius(bulbR * 0.18f, bulbR * 0.18f)
                )
                // Glowing Filament inside Bulb
                val filamentPath = Path().apply {
                    moveTo(bulbCenterX - bulbR * 0.28f, bulbCenterY + bulbR * 0.45f)
                    lineTo(bulbCenterX - bulbR * 0.18f, bulbCenterY - bulbR * 0.15f)
                    lineTo(bulbCenterX, bulbCenterY + bulbR * 0.05f)
                    lineTo(bulbCenterX + bulbR * 0.18f, bulbCenterY - bulbR * 0.15f)
                    lineTo(bulbCenterX + bulbR * 0.28f, bulbCenterY + bulbR * 0.45f)
                }
                drawPath(
                    filamentPath,
                    color = Color(0xFFB45309),
                    style = Stroke(width = (w * 0.020f).coerceAtLeast(1.5f), cap = StrokeCap.Round)
                )
            }

            // 9D. PERISAI FX: Rotating Hexagonal Cyber Shield Barrier
            if (activeElement == MascotElement.SHIELD || effectiveMood == MascotMood.PIN_SHIELD) {
                val hexR = r * 1.18f
                val hexPath = Path().apply {
                    for (i in 0..5) {
                        val a = (i * 60f - 30f) * (PI.toFloat() / 180f) + fxFlowPhase * 0.4f
                        val hx = cx + cos(a) * hexR
                        val hy = cy + sin(a) * hexR
                        if (i == 0) moveTo(hx, hy) else lineTo(hx, hy)
                    }
                    close()
                }
                drawPath(
                    path = hexPath,
                    color = Color(0xFF38BDF8).copy(alpha = 0.16f)
                )
                drawPath(
                    path = hexPath,
                    color = Color(0xFF8B5CF6).copy(alpha = 0.78f),
                    style = Stroke(width = (w * 0.028f).coerceAtLeast(2.2f), cap = StrokeCap.Round)
                )
            }

            // 9E. OVERHEAD NEED INDICATOR BUBBLE (When Mascot Needs Listrik / Love / Ide)
            if (neededElement != null && activeElement == null) {
                val bubbleCenterX = cx + r * 0.78f
                val bubbleCenterY = cy - r * 1.02f + sin(fxFlowPhase * PI * 2).toFloat() * (r * 0.08f)
                val bubbleR = r * 0.34f

                // Thought bubble trail dots
                drawCircle(Color.White, radius = r * 0.07f, center = Offset(cx + r * 0.38f, cy - r * 0.72f))
                drawCircle(Color.White, radius = r * 0.10f, center = Offset(cx + r * 0.52f, cy - r * 0.84f))

                // Main Need Bubble
                drawCircle(
                    color = neededElement.badgeColor,
                    radius = bubbleR * 1.12f,
                    center = Offset(bubbleCenterX, bubbleCenterY)
                )
                drawCircle(
                    color = Color.White,
                    radius = bubbleR,
                    center = Offset(bubbleCenterX, bubbleCenterY)
                )

                // Need Icon inside Bubble (Custom Vector Path)
                when (neededElement) {
                    MascotElement.ELECTRIC -> {
                        val bolt = Path().apply {
                            moveTo(bubbleCenterX + bubbleR * 0.15f, bubbleCenterY - bubbleR * 0.55f)
                            lineTo(bubbleCenterX - bubbleR * 0.35f, bubbleCenterY + bubbleR * 0.05f)
                            lineTo(bubbleCenterX + bubbleR * 0.05f, bubbleCenterY + bubbleR * 0.05f)
                            lineTo(bubbleCenterX - bubbleR * 0.18f, bubbleCenterY + bubbleR * 0.58f)
                            lineTo(bubbleCenterX + bubbleR * 0.38f, bubbleCenterY - bubbleR * 0.05f)
                            lineTo(bubbleCenterX - bubbleR * 0.02f, bubbleCenterY - bubbleR * 0.05f)
                            close()
                        }
                        drawPath(bolt, color = Color(0xFFEAB308))
                    }
                    MascotElement.LOVE -> {
                        val hs = bubbleR * 0.48f
                        val heart = Path().apply {
                            moveTo(bubbleCenterX, bubbleCenterY + hs * 0.65f)
                            cubicTo(bubbleCenterX - hs * 1.3f, bubbleCenterY - hs * 0.15f, bubbleCenterX - hs * 0.65f, bubbleCenterY - hs * 1.15f, bubbleCenterX, bubbleCenterY - hs * 0.35f)
                            cubicTo(bubbleCenterX + hs * 0.65f, bubbleCenterY - hs * 1.15f, bubbleCenterX + hs * 1.3f, bubbleCenterY - hs * 0.15f, bubbleCenterX, bubbleCenterY + hs * 0.65f)
                            close()
                        }
                        drawPath(heart, color = Color(0xFFEC4899))
                    }
                    else -> {
                        drawCircle(
                            color = Color(0xFFFACC15),
                            radius = bubbleR * 0.46f,
                            center = Offset(bubbleCenterX, bubbleCenterY - bubbleR * 0.08f)
                        )
                        drawRoundRect(
                            color = Color(0xFF475569),
                            topLeft = Offset(bubbleCenterX - bubbleR * 0.24f, bubbleCenterY + bubbleR * 0.26f),
                            size = Size(bubbleR * 0.48f, bubbleR * 0.24f),
                            cornerRadius = CornerRadius(4f, 4f)
                        )
                    }
                }
            }

            // 10. Ambient Emotion Particles
            val sparkleAlpha = if (cheerSin > 0.05f) 1f else (0.55f + 0.40f * breathePhase)
            val starRadius = (w * 0.044f) * (1f + cheerSin * 0.5f)
            val starPositions = listOf(
                Offset(w * 0.14f, h * (0.20f - breathePhase * 0.05f)),
                Offset(w * 0.86f, h * (0.18f - (1f - breathePhase) * 0.05f)),
                Offset(w * 0.88f, h * (0.72f - breathePhase * 0.04f)),
                Offset(w * 0.16f, h * (0.76f + breathePhase * 0.03f))
            )
            starPositions.forEachIndexed { idx, pos ->
                val starColor = when {
                    activeElement != null -> activeElement.badgeColor.copy(alpha = sparkleAlpha)
                    effectiveMood == MascotMood.CRYING_FOUNTAIN -> Color(0xFF38BDF8).copy(alpha = sparkleAlpha)
                    effectiveMood == MascotMood.RAGE_INFERNO -> if (idx % 2 == 0) Color(0xFFEF4444) else Color(0xFFF97316)
                    effectiveMood == MascotMood.SHOCKED_JOLT -> Color(0xFFFACC15)
                    effectiveMood == MascotMood.SHY_BLUSH -> Color(0xFFF472B6)
                    effectiveMood == MascotMood.SLEEPY_DREAM -> Color(0xFFC084FC)
                    else -> when (idx % 3) {
                        0 -> Color(0xFFF59E0B).copy(alpha = sparkleAlpha)
                        1 -> Color(0xFF1CB0F6).copy(alpha = sparkleAlpha)
                        else -> Color(0xFF58CC02).copy(alpha = sparkleAlpha)
                    }
                }
                drawLine(
                    color = starColor,
                    start = Offset(pos.x - starRadius, pos.y),
                    end = Offset(pos.x + starRadius, pos.y),
                    strokeWidth = (w * 0.024f).coerceAtLeast(1.5f),
                    cap = StrokeCap.Round
                )
                drawLine(
                    color = starColor,
                    start = Offset(pos.x, pos.y - starRadius),
                    end = Offset(pos.x, pos.y + starRadius),
                    strokeWidth = (w * 0.024f).coerceAtLeast(1.5f),
                    cap = StrokeCap.Round
                )
            }
        }
    }
}

/**
 * Backward-Compatibility Alias for NyxMascotAvatar (Always renders Kawaii File Folder)
 */
@Composable
fun NyxMascotAvatar(
    mood: MascotMood,
    modifier: Modifier = Modifier,
    size: Dp = 112.dp,
    persona: MascotPersona = MascotPersona.KAWAII_FOLDER,
    externalCheerKey: Int = 0,
    activeElement: MascotElement? = null,
    neededElement: MascotElement? = null,
    isPetting: Boolean = false
) {
    CosmicDragonAvatar(
        mood = mood,
        modifier = modifier,
        size = size,
        persona = persona,
        externalCheerKey = externalCheerKey,
        activeElement = activeElement,
        neededElement = neededElement,
        isPetting = isPetting
    )
}

/**
 * Smart Mascot Care & Companion Hub Banner:
 * Menampilkan maskot 3D interaktif dengan:
 * - Sistem Kebutuhan Maskot (Energi Listrik, Kasih Sayang Love, Ide Lampu)
 * - Smart Interaction lengkap (Elus/Usap, Ketuk, Tahan, Ketuk Ganda + Balon Dialog Pintar)
 * - Tombol Aksi Cepat untuk memberi energi Listrik, Love, menyalakan Lampu Ide, Api, Air Mata, & Perisai
 */
@Composable
fun CosmicDragonCompanionBanner(
    activeVaultId: Int,
    headline: String,
    subtitle: String,
    mood: MascotMood = MascotMood.SUCCESS_LEAP,
    onQuickActionClick: (() -> Unit)? = null,
    quickActionLabel: String? = null
) {
    var tapCounter by remember { mutableIntStateOf(0) }
    var activeElement by remember { mutableStateOf<MascotElement?>(null) }
    var isPettingMascot by remember { mutableStateOf(false) }

    // Sistem Kebutuhan Maskot (0..100)
    var electricEnergyNeed by remember { mutableIntStateOf(78) }
    var loveAffectionNeed by remember { mutableIntStateOf(74) }
    var ideaBulbNeed by remember { mutableIntStateOf(82) }
    var smartInteractionMessage by remember { mutableStateOf<String?>(null) }

    val moodSequence = remember {
        listOf(
            MascotMood.SUCCESS_LEAP,
            MascotMood.CRYING_FOUNTAIN,
            MascotMood.RAGE_INFERNO,
            MascotMood.SHOCKED_JOLT,
            MascotMood.SHY_BLUSH,
            MascotMood.SLEEPY_DREAM
        )
    }

    // Gradual Need Decay & Autonomous Emotion Cycling
    androidx.compose.runtime.LaunchedEffect(Unit) {
        while (true) {
            kotlinx.coroutines.delay(6500L)
            electricEnergyNeed = (electricEnergyNeed - 2).coerceAtLeast(25)
            loveAffectionNeed = (loveAffectionNeed - 2).coerceAtLeast(25)
            ideaBulbNeed = (ideaBulbNeed - 2).coerceAtLeast(25)
            if (activeElement == null) {
                tapCounter = (tapCounter + 1)
            }
        }
    }

    // Auto-clear temporary activeElement burst after 4.5 seconds so needs & autonomous reactions stay lively
    androidx.compose.runtime.LaunchedEffect(activeElement, tapCounter) {
        if (activeElement != null) {
            kotlinx.coroutines.delay(4500L)
            isPettingMascot = false
        }
    }

    val neededElement = remember(electricEnergyNeed, loveAffectionNeed, ideaBulbNeed) {
        when {
            electricEnergyNeed < 55 -> MascotElement.ELECTRIC
            loveAffectionNeed < 55 -> MascotElement.LOVE
            ideaBulbNeed < 55 -> MascotElement.IDEA_BULB
            else -> null
        }
    }

    val activeMood = if (tapCounter == 0) mood else moodSequence[tapCounter % moodSequence.size]
    val (emotionBadgeLabel, emotionBadgeColor, emotionQuip) = when {
        activeElement == MascotElement.LOVE || isPettingMascot -> Triple(
            "Elemen: Kasih Sayang Love (Pink)",
            Color(0xFFDB2777),
            smartInteractionMessage ?: "Nyaa~ Terima kasih sudah mengelus & memberi Love! Hatiku berbunga-bunga menjaga Brankas #$activeVaultId!"
        )
        activeElement == MascotElement.ELECTRIC -> Triple(
            "Elemen: Energi Listrik (Kuning)",
            Color(0xFFD97706),
            smartInteractionMessage ?: "BZZT! Daya Listrik 100% penuh! Mesin enkripsi & Mori Downloader melaju secepat kilat!"
        )
        activeElement == MascotElement.IDEA_BULB -> Triple(
            "Elemen: Lampu Ide Pintar (Hijau)",
            Color(0xFF16A34A),
            smartInteractionMessage ?: "TING! Lampu Ide menyala terang! Gunakan Smart Search & filter chip untuk temukan file dalam 0.1 detik!"
        )
        activeElement == MascotElement.FIRE -> Triple(
            "Elemen: Aura Api Membara (Merah)",
            Color(0xFFDC2626),
            smartInteractionMessage ?: "BURN! Aura api pelindung aktif membakar file duplikat & ancaman virus di Brankas #$activeVaultId!"
        )
        activeElement == MascotElement.TEARS -> Triple(
            "Elemen: Air Mata Deras (Biru)",
            Color(0xFF0284C7),
            smartInteractionMessage ?: "Huwaaa! Air mataku menyembur deras! Elus aku atau beri Love & Listrik supaya aku ceria lagi!"
        )
        activeElement == MascotElement.SHIELD -> Triple(
            "Elemen: Perisai Siber (Ungu)",
            Color(0xFF7C3AED),
            smartInteractionMessage ?: "SHIELD ON! Perisai heksagonal anti-screenshot & anti-intip mengunci rapat seluruh privasimu!"
        )
        neededElement == MascotElement.ELECTRIC -> Triple(
            "Butuh Energi Listrik ($electricEnergyNeed%)",
            Color(0xFFD97706),
            "Energi listrikku mulai turun! Tekan tombol Listrik atau tahan tubuhku untuk isi daya!"
        )
        neededElement == MascotElement.LOVE -> Triple(
            "Butuh Kasih Sayang ($loveAffectionNeed%)",
            Color(0xFFDB2777),
            "Aku butuh kasih sayang! Elus kepalaku atau tekan tombol Love ya!"
        )
        neededElement == MascotElement.IDEA_BULB -> Triple(
            "Butuh Inspirasi Ide ($ideaBulbNeed%)",
            Color(0xFF16A34A),
            "Nyalakan Lampu Ideku agar aku bisa memberi tips pintar Brankas & Mori!"
        )
        activeMood == MascotMood.CRYING_FOUNTAIN -> Triple(
            "Emosi: Nangis Ngejer (Biru)",
            Color(0xFF0284C7),
            "Huwaaa! Jangan lupa kunci berkas rahasiamu biar aku nggak nangis ngejer!"
        )
        activeMood == MascotMood.RAGE_INFERNO -> Triple(
            "Emosi: Marah Aura Api (Merah)",
            Color(0xFFDC2626),
            "GRRR! Aura api merahku siap membakar penyusup & file duplikat di Brankas #$activeVaultId!"
        )
        activeMood == MascotMood.SHOCKED_JOLT -> Triple(
            "Emosi: Kaget Petir (Kuning)",
            Color(0xFFD97706),
            "BZZT! Ketuk 1x, elus, atau tekan tombol elemen di bawah untuk berinteraksi denganku!"
        )
        activeMood == MascotMood.SHY_BLUSH -> Triple(
            "Emosi: Tersipu Malu (Pink)",
            Color(0xFFDB2777),
            "Hehe... Semua media video, musik, & foto kini punya 1 Player Utama yang rapi di Brankas #$activeVaultId!"
        )
        activeMood == MascotMood.SLEEPY_DREAM -> Triple(
            "Emosi: Mengantuk Zzz (Ungu)",
            Color(0xFF7E22CE),
            "Zzz... Aku tetap siaga menjaga brankasmu dari tangkapan layar & perekam layar..."
        )
        else -> Triple(
            "Smart Maskot Aktif (Hijau)",
            Color(0xFF16A34A),
            smartInteractionMessage ?: headline
        )
    }

    val currentPersona = MascotPersona.entries[tapCounter % 4]
    val activeHeadline = smartInteractionMessage ?: if (tapCounter == 0 && activeElement == null) headline else emotionQuip

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .duolingoCard(
                backgroundColor = com.example.ui.theme.DuoPureWhite,
                borderColor = emotionBadgeColor.copy(alpha = 0.65f),
                lipColor = com.example.ui.theme.DuoHareLip,
                cornerRadius = 24.dp,
                borderWidth = 2.dp,
                lipHeight = 4.dp
            )
            .padding(bottom = 4.dp)
            .testTag("nyx_mascot_companion_banner")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // Top Row: Interactive 3D Mascot Avatar + Smart Speech Bubble + Optional Quick Action
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Box(
                    modifier = Modifier
                        .pointerInput(Unit) {
                            awaitEachGesture {
                                val down = awaitFirstDown(requireUnconsumed = false)
                                val downTime = System.currentTimeMillis()
                                var draggedDistance = 0f
                                var isLongTriggered = false
                                do {
                                    val event = awaitPointerEvent()
                                    val change = event.changes.firstOrNull()
                                    if (change != null) {
                                        val delta = change.position - down.position
                                        draggedDistance = kotlin.math.hypot(delta.x, delta.y)
                                        if (draggedDistance > 18f) {
                                            // ELUS / USAP MASKOT -> Love + Kasih Sayang naik!
                                            isPettingMascot = true
                                            activeElement = MascotElement.LOVE
                                            loveAffectionNeed = (loveAffectionNeed + 4).coerceAtMost(100)
                                            smartInteractionMessage = "Elusan hangat diterima! Kasih Sayang naik jadi $loveAffectionNeed%!"
                                        } else if (!isLongTriggered && System.currentTimeMillis() - downTime > 520L) {
                                            // TAHAN MASKOT -> Overcharge Energi Listrik & Perisai!
                                            isLongTriggered = true
                                            activeElement = MascotElement.ELECTRIC
                                            electricEnergyNeed = (electricEnergyNeed + 18).coerceAtMost(100)
                                            tapCounter++
                                            smartInteractionMessage = "OVERCHARGE LISTRIK! Daya penuh $electricEnergyNeed% & Perisai aktif!"
                                        }
                                    }
                                } while (event.changes.any { it.pressed })

                                if (draggedDistance <= 18f && !isLongTriggered) {
                                    // KETUK CEPAT -> Ganti ekspresi & nyalakan Ide pintar
                                    tapCounter++
                                    ideaBulbNeed = (ideaBulbNeed + 8).coerceAtMost(100)
                                    if (tapCounter % 3 == 0) {
                                        activeElement = MascotElement.IDEA_BULB
                                        smartInteractionMessage = "TING! Ide Pintar: Ketuk tombol elemen di bawah untuk memicu efek 3D!"
                                    } else {
                                        smartInteractionMessage = null
                                    }
                                }
                            }
                        }
                ) {
                    CosmicDragonAvatar(
                        mood = activeMood,
                        size = 72.dp,
                        persona = currentPersona,
                        externalCheerKey = tapCounter,
                        activeElement = activeElement,
                        neededElement = neededElement,
                        isPetting = isPettingMascot
                    )
                }

                Surface(
                    modifier = Modifier.weight(1f),
                    color = com.example.ui.theme.DuoSnowCanvas,
                    shape = RoundedCornerShape(
                        topStart = 6.dp,
                        topEnd = 18.dp,
                        bottomEnd = 18.dp,
                        bottomStart = 18.dp
                    ),
                    border = androidx.compose.foundation.BorderStroke(
                        width = 2.dp,
                        color = emotionBadgeColor.copy(alpha = 0.75f)
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                        verticalArrangement = Arrangement.spacedBy(2.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Text(
                                text = emotionBadgeLabel,
                                style = MaterialTheme.typography.labelMedium,
                                color = emotionBadgeColor,
                                fontWeight = FontWeight.ExtraBold,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier.weight(1f, fill = false)
                            )
                            Text(
                                text = "• Elus / Ketuk / Tahan!",
                                style = MaterialTheme.typography.labelMedium,
                                color = CosmicTextMuted,
                                fontSize = 10.sp,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }

                        TruncatedHoldTooltipText(
                            text = activeHeadline,
                            style = MaterialTheme.typography.titleMedium,
                            color = CosmicTextPure,
                            fontWeight = FontWeight.Bold,
                            maxLines = 2,
                            onTap = { tapCounter++ }
                        )

                        TruncatedHoldTooltipText(
                            text = subtitle,
                            style = MaterialTheme.typography.bodyMedium.copy(fontSize = 11.sp),
                            color = CosmicTextMuted,
                            fontWeight = FontWeight.Normal,
                            maxLines = 1,
                            onTap = { tapCounter++ }
                        )
                    }
                }

                if (onQuickActionClick != null && quickActionLabel != null) {
                    Box(
                        modifier = Modifier
                            .duolingoButton(
                                faceColor = com.example.ui.theme.DuoFeatherGreen,
                                lipColor = com.example.ui.theme.DuoFeatherGreenLip,
                                cornerRadius = 16.dp,
                                lipHeight = 3.dp
                            )
                            .instantTouchPress { onQuickActionClick() }
                            .padding(bottom = 3.dp, start = 12.dp, end = 12.dp, top = 7.dp)
                    ) {
                        Text(
                            text = quickActionLabel,
                            style = MaterialTheme.typography.labelMedium,
                            fontSize = 11.sp,
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(bottom = 4.dp)
                        )
                    }
                }
            }

            // Middle Row: Sistem Kebutuhan Maskot (3 Needs Bars: Listrik, Love, Ide Lampu)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                MascotNeedMiniBar(
                    modifier = Modifier.weight(1f),
                    label = "Listrik",
                    percent = electricEnergyNeed,
                    barColor = Color(0xFFEAB308),
                    onClick = {
                        activeElement = MascotElement.ELECTRIC
                        electricEnergyNeed = (electricEnergyNeed + 22).coerceAtMost(100)
                        tapCounter++
                        smartInteractionMessage = "BZZT! Energi Listrik diisi ke $electricEnergyNeed%! Maskot makin gesit!"
                    }
                )
                MascotNeedMiniBar(
                    modifier = Modifier.weight(1f),
                    label = "Love",
                    percent = loveAffectionNeed,
                    barColor = Color(0xFFEC4899),
                    onClick = {
                        isPettingMascot = true
                        activeElement = MascotElement.LOVE
                        loveAffectionNeed = (loveAffectionNeed + 22).coerceAtMost(100)
                        tapCounter++
                        smartInteractionMessage = "KYAA! Kasih Sayang penuh ($loveAffectionNeed%)! Hati 3D bermekaran!"
                    }
                )
                MascotNeedMiniBar(
                    modifier = Modifier.weight(1f),
                    label = "Ide",
                    percent = ideaBulbNeed,
                    barColor = Color(0xFF58CC02),
                    onClick = {
                        activeElement = MascotElement.IDEA_BULB
                        ideaBulbNeed = (ideaBulbNeed + 22).coerceAtMost(100)
                        tapCounter++
                        smartInteractionMessage = "TING! Lampu Ide ($ideaBulbNeed%) menyala di atas kepala maskot!"
                    }
                )
            }

            // Bottom Row: 6 Quick Elemental & Smart Reaction Trigger Chips
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                MascotElement.entries.forEach { elem ->
                    val isSelected = activeElement == elem
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .duolingoButton(
                                faceColor = if (isSelected) elem.badgeColor else com.example.ui.theme.DuoSnowCanvas,
                                lipColor = if (isSelected) elem.lipColor else com.example.ui.theme.DuoHareLip,
                                cornerRadius = 10.dp,
                                lipHeight = 2.dp
                            )
                            .clickable {
                                if (activeElement == elem) {
                                    activeElement = null
                                    isPettingMascot = false
                                    smartInteractionMessage = null
                                } else {
                                    activeElement = elem
                                    isPettingMascot = (elem == MascotElement.LOVE)
                                    when (elem) {
                                        MascotElement.ELECTRIC -> electricEnergyNeed = (electricEnergyNeed + 25).coerceAtMost(100)
                                        MascotElement.LOVE -> loveAffectionNeed = (loveAffectionNeed + 25).coerceAtMost(100)
                                        MascotElement.IDEA_BULB -> ideaBulbNeed = (ideaBulbNeed + 25).coerceAtMost(100)
                                        else -> {}
                                    }
                                    tapCounter++
                                }
                            }
                            .padding(vertical = 5.dp, horizontal = 2.dp)
                            .padding(bottom = 2.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = elem.shortLabel,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = if (isSelected) Color.White else com.example.ui.theme.DuoEelInk,
                            maxLines = 1
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun MascotNeedMiniBar(
    modifier: Modifier = Modifier,
    label: String,
    percent: Int,
    barColor: Color,
    onClick: () -> Unit
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(10.dp))
            .background(com.example.ui.theme.DuoSnowCanvas)
            .border(1.5.dp, barColor.copy(alpha = 0.55f), RoundedCornerShape(10.dp))
            .clickable { onClick() }
            .padding(horizontal = 8.dp, vertical = 5.dp),
        verticalArrangement = Arrangement.spacedBy(3.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = label,
                fontSize = 10.sp,
                fontWeight = FontWeight.ExtraBold,
                color = com.example.ui.theme.DuoEelInk,
                maxLines = 1
            )
            Text(
                text = "$percent%",
                fontSize = 10.sp,
                fontWeight = FontWeight.ExtraBold,
                color = barColor
            )
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(6.dp)
                .clip(RoundedCornerShape(4.dp))
                .background(Color(0xFFE2E8F0))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(fraction = (percent / 100f).coerceIn(0.08f, 1f))
                    .height(6.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(barColor)
            )
        }
    }
}

/**
 * Backward-Compatibility Alias for NyxMascotCompanionBanner
 */
@Composable
fun NyxMascotCompanionBanner(
    activeVaultId: Int,
    headline: String,
    subtitle: String,
    mood: MascotMood = MascotMood.HAPPY_VIBES,
    onQuickActionClick: (() -> Unit)? = null,
    quickActionLabel: String? = null
) {
    CosmicDragonCompanionBanner(
        activeVaultId = activeVaultId,
        headline = headline,
        subtitle = subtitle,
        mood = mood,
        onQuickActionClick = onQuickActionClick,
        quickActionLabel = quickActionLabel
    )
}

/**
 * First-Open Setup Wizard dengan Maskot Naga Kosmik:
 * Konfigurasi Awal: 1 Brankas Utama (3-Lapis PIN 6 Digit) + 1 Ruang Pengaturan Terisolasi Mandiri (3-Lapis PIN 6 Digit).
 */
@Composable
fun FirstRunOnboardingWizardScreen(
    onCompleteSetup: (
        v1p1: String,
        v1p2: String,
        v1p3: String,
        v2p1: String,
        v2p2: String,
        v2p3: String,
        biometricEnabled: Boolean,
        recoveryQuestion1: String,
        recoveryAnswer1: String,
        recoveryQuestion2: String,
        recoveryAnswer2: String,
        recoveryQuestion3: String,
        recoveryAnswer3: String,
        googleEmail: String,
        googleDisplayName: String,
        funPin1: String,
        funPin2: String,
        funPin3: String,
        auralysPin1: String,
        auralysPin2: String,
        auralysPin3: String
    ) -> Unit
) {
    var currentStep by remember { mutableIntStateOf(0) }

    // Brankas Utama #1 (3 Lapis PIN 6 Digit)
    var v1Pin1 by remember { mutableStateOf("") }
    var v1Pin2 by remember { mutableStateOf("") }
    var v1Pin3 by remember { mutableStateOf("") }

    // Ruang Pengaturan Terisolasi (3 Lapis PIN 6 Digit Mandiri)
    var v2Pin1 by remember { mutableStateOf("") }
    var v2Pin2 by remember { mutableStateOf("") }
    var v2Pin3 by remember { mutableStateOf("") }

    // Ruang Terisolasi Fun Game & Arcade 3D (3 Lapis PIN 6 Digit Mandiri — Default Skip Setup: 333333-333333-333333)
    var funPin1 by remember { mutableStateOf("333333") }
    var funPin2 by remember { mutableStateOf("333333") }
    var funPin3 by remember { mutableStateOf("333333") }

    // Ruang Terisolasi Auralys Music Chamber (3 Lapis PIN 6 Digit Mandiri — Default Skip Setup: 000000-000000-000000)
    var auralysPin1 by remember { mutableStateOf("000000") }
    var auralysPin2 by remember { mutableStateOf("000000") }
    var auralysPin3 by remember { mutableStateOf("000000") }

    var biometricEnabled by remember { mutableStateOf(true) }
    var isFingerprintRegisteredInWizard by remember { mutableStateOf(false) }
    var stealthModeRule by remember { mutableStateOf(true) }
    var encryptedExportRule by remember { mutableStateOf(true) }
    var autoLockRule by remember { mutableStateOf(true) }
    var fingerprintStatusMessage by remember {
        mutableStateOf("Tempel & tahan jari pada sensor di bawah hingga 100% untuk mendaftarkan sidik jari.")
    }

    var recoveryQuestion1 by remember { mutableStateOf("Apa nama kota kelahiran Anda?") }
    var recoveryAnswer1 by remember { mutableStateOf("") }
    var recoveryQuestion2 by remember { mutableStateOf("Apa nama hewan peliharaan pertama Anda?") }
    var recoveryAnswer2 by remember { mutableStateOf("") }
    var recoveryQuestion3 by remember { mutableStateOf("Apa makanan favorit masa kecil Anda?") }
    var recoveryAnswer3 by remember { mutableStateOf("") }
    var googleEmail by remember { mutableStateOf("") }
    var googleDisplayName by remember { mutableStateOf("") }
    var showGoogleSignInModal by remember { mutableStateOf(false) }
    var pendingCompleteAfterLogin by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val stepTitles = listOf(
        "Sambutan",
        "PIN Brankas #1",
        "PIN Pengaturan",
        "Pemulihan",
        "Konfirmasi"
    )

    val context = LocalContext.current
    val gso = remember {
        GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .requestProfile()
            .build()
    }
    val googleSignInClient = remember(context) { GoogleSignIn.getClient(context, gso) }

    val executeCompleteSetupWithGoogle: (String, String) -> Unit = { finalEmail, finalName ->
        val cleanFinalEmail = finalEmail.trim().lowercase()
        if (v1Pin1.length != 6) v1Pin1 = "111111"
        if (v1Pin2.length != 6) v1Pin2 = "111111"
        if (v1Pin3.length != 6) v1Pin3 = "111111"
        if (v2Pin1.length != 6) v2Pin1 = "222222"
        if (v2Pin2.length != 6) v2Pin2 = "222222"
        if (v2Pin3.length != 6) v2Pin3 = "222222"
        if (funPin1.length != 6) funPin1 = "333333"
        if (funPin2.length != 6) funPin2 = "333333"
        if (funPin3.length != 6) funPin3 = "333333"
        if (auralysPin1.length != 6) auralysPin1 = "000000"
        if (auralysPin2.length != 6) auralysPin2 = "000000"
        if (auralysPin3.length != 6) auralysPin3 = "000000"
        if (recoveryAnswer1.isBlank()) recoveryAnswer1 = "Jakarta"
        if (recoveryAnswer2.isBlank()) recoveryAnswer2 = "Kucing"
        if (recoveryAnswer3.isBlank()) recoveryAnswer3 = "Nasi Goreng"
        val validEmail = if (cleanFinalEmail.contains("@") && cleanFinalEmail.contains(".")) cleanFinalEmail else ""
        val validName = if (validEmail.isNotBlank()) {
            finalName.trim().ifBlank { validEmail.substringBefore("@").ifBlank { "Arcanum User" } }
        } else ""
        onCompleteSetup(
            v1Pin1, v1Pin2, v1Pin3,
            v2Pin1, v2Pin2, v2Pin3,
            biometricEnabled,
            recoveryQuestion1.ifBlank { "Apa nama kota kelahiran Anda?" },
            recoveryAnswer1.trim(),
            recoveryQuestion2.ifBlank { "Apa nama hewan peliharaan pertama Anda?" },
            recoveryAnswer2.trim(),
            recoveryQuestion3.ifBlank { "Apa makanan favorit masa kecil Anda?" },
            recoveryAnswer3.trim(),
            validEmail,
            validName,
            funPin1, funPin2, funPin3,
            auralysPin1, auralysPin2, auralysPin3
        )
    }

    val googleSignInLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        try {
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            val account = task.getResult(ApiException::class.java)
            if (account != null && !account.email.isNullOrBlank()) {
                val resolvedEmail = account.email ?: ""
                val resolvedName = account.displayName ?: account.givenName ?: "Google User"
                googleEmail = resolvedEmail
                googleDisplayName = resolvedName
                errorMessage = null
                if (pendingCompleteAfterLogin) {
                    pendingCompleteAfterLogin = false
                    executeCompleteSetupWithGoogle(resolvedEmail, resolvedName)
                }
            } else {
                showGoogleSignInModal = true
            }
        } catch (_: Exception) {
            showGoogleSignInModal = true
        }
    }

    val triggerGoogleLoginFlow: (Boolean) -> Unit = { autoCompleteOnSuccess ->
        pendingCompleteAfterLogin = autoCompleteOnSuccess
        showGoogleSignInModal = true
    }

    // Animasi Sambutan Awal: Maskot tampil Full-Screen menyapa sebentar, lalu mengecil & pindah ke pojok kiri atas
    var isMascotGreetingFullscreen by remember { mutableStateOf(true) }
    androidx.compose.runtime.LaunchedEffect(Unit) {
        kotlinx.coroutines.delay(1650L)
        isMascotGreetingFullscreen = false
    }

    val mascotMoveProgress by androidx.compose.animation.core.animateFloatAsState(
        targetValue = if (isMascotGreetingFullscreen) 0f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioLowBouncy,
            stiffness = Spring.StiffnessMediumLow
        ),
        label = "mascot_shrink_to_top_left"
    )

    // Fungsi Skip Setup Instan: Auto-isi default Brankas 1 (111111-111111-111111), Pengaturan (222222-222222-222222), Fun Game (333333-333333-333333) & Auralys (000000-000000-000000)
    // Login Google bersifat opsional: jika belum login Google, fitur Cloud & Report otomatis mati total
    val triggerSkipSetupDefault: () -> Unit = {
        v1Pin1 = "111111"
        v1Pin2 = "111111"
        v1Pin3 = "111111"
        v2Pin1 = "222222"
        v2Pin2 = "222222"
        v2Pin3 = "222222"
        funPin1 = "333333"
        funPin2 = "333333"
        funPin3 = "333333"
        auralysPin1 = "000000"
        auralysPin2 = "000000"
        auralysPin3 = "000000"
        if (recoveryAnswer1.isBlank()) recoveryAnswer1 = "Jakarta"
        if (recoveryAnswer2.isBlank()) recoveryAnswer2 = "Kucing"
        if (recoveryAnswer3.isBlank()) recoveryAnswer3 = "Nasi Goreng"
        executeCompleteSetupWithGoogle(googleEmail, googleDisplayName)
    }

    if (showGoogleSignInModal) {
        ArcanumGoogleSignInDialog(
            currentEmail = googleEmail,
            currentName = googleDisplayName,
            isMandatoryGate = false,
            onDismiss = {
                showGoogleSignInModal = false
                pendingCompleteAfterLogin = false
            },
            onLaunchNativeGoogleSignIn = {
                showGoogleSignInModal = false
                try {
                    googleSignInLauncher.launch(googleSignInClient.signInIntent)
                } catch (_: Exception) {
                    showGoogleSignInModal = true
                }
            },
            onSignedIn = { chosenEmail, chosenName ->
                googleEmail = chosenEmail
                googleDisplayName = chosenName
                errorMessage = null
                showGoogleSignInModal = false
                if (pendingCompleteAfterLogin) {
                    pendingCompleteAfterLogin = false
                    executeCompleteSetupWithGoogle(chosenEmail, chosenName)
                }
            }
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(CosmicVoid)
            .windowInsetsPadding(WindowInsets.safeDrawing),
        contentAlignment = Alignment.TopCenter
    ) {
        DuolingoGalaxyAmbientBackdrop()

        // Konten Form Setup (muncul halus begitu maskot mengecil dan berpindah ke pojok kiri atas)
        Column(
            modifier = Modifier
                .fillMaxSize()
                .widthIn(max = 500.dp)
                .graphicsLayer {
                    alpha = mascotMoveProgress.coerceIn(0f, 1f)
                    translationY = (1f - mascotMoveProgress) * 28.dp.toPx()
                }
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 18.dp, vertical = 14.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Compact Top Progress Header Bar
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                for (step in 0..4) {
                    val isDone = step < currentStep
                    val isCurrent = step == currentStep
                    Column(
                        modifier = Modifier.weight(1f),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(8.dp)
                                .clip(RoundedCornerShape(50))
                                .background(
                                    when {
                                        isDone -> com.example.ui.theme.DuoFeatherGreen
                                        isCurrent -> com.example.ui.theme.DuoMacawBlue
                                        else -> com.example.ui.theme.DuoSwanBorder
                                    }
                                )
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = stepTitles[step],
                            fontSize = 10.sp,
                            fontWeight = if (isCurrent) FontWeight.ExtraBold else FontWeight.SemiBold,
                            color = when {
                                isCurrent -> com.example.ui.theme.DuoMacawBlueLip
                                isDone -> com.example.ui.theme.DuoFeatherGreenLip
                                else -> CosmicTextMuted
                            },
                            maxLines = 1
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Header Pojok Kiri Atas: Maskot yang sudah mengecil di pojok kiri + Info Langkah + Tombol Skip Setup Cepat
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .duolingoCard(
                        backgroundColor = com.example.ui.theme.DuoPureWhite,
                        borderColor = com.example.ui.theme.DuoSwanBorder,
                        lipColor = com.example.ui.theme.DuoHareLip,
                        cornerRadius = 20.dp,
                        borderWidth = 2.dp,
                        lipHeight = 4.dp
                    )
                    .padding(horizontal = 12.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                CosmicDragonAvatar(
                    mood = when (currentStep) {
                        0 -> MascotMood.WAVE_HELLO
                        1 -> MascotMood.PIN_SHIELD
                        2 -> MascotMood.STEALTH_SHADOW
                        else -> MascotMood.SUCCESS_LEAP
                    },
                    size = 58.dp,
                    externalCheerKey = currentStep + 1
                )

                Column(modifier = Modifier.weight(1f)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            color = com.example.ui.theme.DuoFeatherGreenSoft,
                            shape = RoundedCornerShape(50),
                            border = androidx.compose.foundation.BorderStroke(1.5.dp, com.example.ui.theme.DuoFeatherGreen)
                        ) {
                            Text(
                                text = "LANGKAH ${currentStep + 1}/5",
                                fontSize = 9.sp,
                                color = com.example.ui.theme.DuoFeatherGreenLip,
                                fontWeight = FontWeight.ExtraBold,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                            )
                        }

                        // Tombol Skip Setup di Pojok Kanan Header (bisa ditekan di langkah mana saja)
                        Box(
                            modifier = Modifier
                                .duolingoButton(
                                    faceColor = com.example.ui.theme.DuoFoxOrange,
                                    lipColor = com.example.ui.theme.DuoFoxOrangeLip,
                                    cornerRadius = 10.dp,
                                    lipHeight = 3.dp
                                )
                                .instantTouchPress { triggerSkipSetupDefault() }
                                .padding(bottom = 3.dp, start = 8.dp, end = 8.dp, top = 4.dp)
                                .testTag("onboarding_header_skip_button")
                        ) {
                            Text(
                                text = "Skip Setup",
                                fontSize = 10.sp,
                                color = Color.White,
                                fontWeight = FontWeight.ExtraBold,
                                modifier = Modifier.padding(bottom = 2.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = when (currentStep) {
                            0 -> "Halo! Pilih Isi Manual 6-Kotak atau Skip Setup Default"
                            1 -> "PIN / PW 6-Kotak (3 Lapis) — Brankas Utama #1"
                            2 -> "PIN / PW 6-Kotak (3 Lapis) — Ruang Pengaturan"
                            3 -> "Sidik Jari & 3 Pertanyaan Pemulihan"
                            else -> "Tinjau Konfigurasi & Masuk ke Brankas"
                        },
                        style = MaterialTheme.typography.titleSmall,
                        color = CosmicTextPure,
                        fontWeight = FontWeight.ExtraBold
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Mandatory Google Account Gate Card in Setup Wizard (Always visible so user can login anytime during setup)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .duolingoCard(
                        backgroundColor = if (googleEmail.isNotBlank()) com.example.ui.theme.DuoFeatherGreenSoft else com.example.ui.theme.DuoBeeYellowSoft,
                        borderColor = if (googleEmail.isNotBlank()) com.example.ui.theme.DuoFeatherGreen else com.example.ui.theme.DuoFoxOrange,
                        lipColor = if (googleEmail.isNotBlank()) com.example.ui.theme.DuoFeatherGreenLip else com.example.ui.theme.DuoFoxOrangeLip,
                        cornerRadius = 18.dp,
                        borderWidth = 2.dp,
                        lipHeight = 4.dp
                    )
                    .padding(bottom = 4.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 10.dp),
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
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(
                                    if (googleEmail.isNotBlank()) com.example.ui.theme.DuoFeatherGreen else com.example.ui.theme.DuoFoxOrange
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = if (googleEmail.isNotBlank()) Icons.Default.CheckCircle else Icons.Default.AccountCircle,
                                contentDescription = "Status Login Google",
                                tint = Color.White,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Column {
                            Text(
                                text = if (googleEmail.isNotBlank()) {
                                    "LOGIN GOOGLE AKTIF • CLOUD & REPORT TERBUKA"
                                } else {
                                    "LOGIN GOOGLE OPSIONAL (CLOUD & REPORT MATI JIKA SKIP)"
                                },
                                fontFamily = JetBrainsMonoFontFamily,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = if (googleEmail.isNotBlank()) com.example.ui.theme.DuoFeatherGreenLip else com.example.ui.theme.DuoFoxOrangeLip
                            )
                            Text(
                                text = if (googleEmail.isNotBlank()) {
                                    "$googleDisplayName ($googleEmail)"
                                } else {
                                    "Opsional: Tanpa login Google, fitur Cloud & Report/Saran mati total"
                                },
                                style = MaterialTheme.typography.labelSmall,
                                color = CosmicTextPure,
                                fontWeight = FontWeight.Bold,
                                maxLines = 1
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Box(
                        modifier = Modifier
                            .duolingoButton(
                                faceColor = if (googleEmail.isNotBlank()) com.example.ui.theme.DuoFeatherGreen else com.example.ui.theme.DuoMacawBlue,
                                lipColor = if (googleEmail.isNotBlank()) com.example.ui.theme.DuoFeatherGreenLip else com.example.ui.theme.DuoMacawBlueLip,
                                cornerRadius = 12.dp,
                                lipHeight = 3.dp
                            )
                            .instantTouchPress { triggerGoogleLoginFlow(false) }
                            .padding(bottom = 3.dp, start = 10.dp, end = 10.dp, top = 6.dp)
                            .testTag("onboarding_google_login_banner_button"),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = if (googleEmail.isNotBlank()) "Ganti Akun" else "Login Google",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color.White,
                            modifier = Modifier.padding(bottom = 3.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            when (currentStep) {
                0 -> {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .duolingoCard(
                                backgroundColor = com.example.ui.theme.DuoPureWhite,
                                borderColor = com.example.ui.theme.DuoSwanBorder,
                                lipColor = com.example.ui.theme.DuoHareLip,
                                cornerRadius = 24.dp,
                                borderWidth = 2.dp,
                                lipHeight = 5.dp
                            )
                            .padding(bottom = 5.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(18.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "Selamat Datang di Arcanum Vault",
                                style = MaterialTheme.typography.headlineSmall,
                                color = CosmicTextPure,
                                fontWeight = FontWeight.ExtraBold,
                                textAlign = TextAlign.Center
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "Atur PIN/Sandi 6-Kotak menggunakan keyboard bawaan HP Anda, atau klik Skip Setup untuk langsung masuk dengan PIN default!",
                                style = MaterialTheme.typography.bodySmall,
                                color = CosmicTextMuted,
                                textAlign = TextAlign.Center
                            )

                            Spacer(modifier = Modifier.height(14.dp))

                            FeatureHighlightRow(
                                title = "1. Input 6-Kotak dengan Keyboard Bawaan",
                                desc = "Ketuk kotak PIN untuk membuka keyboard bawaan (mendukung angka 0-9 & huruf A-Z)."
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            FeatureHighlightRow(
                                title = "2. Brankas #1 Default: 111111 - 111111 - 111111",
                                desc = "Jika menggunakan Skip Setup, PIN 3 lapis Brankas #1 otomatis diisi 111111-111111-111111."
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            FeatureHighlightRow(
                                title = "3. Pengaturan Default: 222222 - 222222 - 222222",
                                desc = "PIN masuk Pengaturan otomatis diisi 222222-222222-222222. Semua PIN Brankas & PIN Pengaturan bebas diubah kapan saja di menu Pengaturan!"
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            FeatureHighlightRow(
                                title = "4. Fun Game & Arcade Default: 333333 - 333333 - 333333",
                                desc = "Jika menggunakan Skip Setup, PIN 3 lapis Ruang Terisolasi Fun Game (Galaxy Tower Defense 200 Wave, Slot Gacha 3D, Tarot & Kapsul Waktu) otomatis diisi 333333-333333-333333!"
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            FeatureHighlightRow(
                                title = "5. Auralys Music Default: 000000 - 000000 - 000000",
                                desc = "Jika menggunakan Skip Setup, PIN 3 lapis Ruang Terisolasi Auralys Music Chamber otomatis diisi 000000-000000-000000!"
                            )

                            Spacer(modifier = Modifier.height(18.dp))

                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(50.dp)
                                    .duolingoButton(
                                        faceColor = com.example.ui.theme.DuoFeatherGreen,
                                        lipColor = com.example.ui.theme.DuoFeatherGreenLip,
                                        cornerRadius = 16.dp,
                                        lipHeight = 4.dp
                                    )
                                    .instantTouchPress { currentStep = 1 }
                                    .padding(bottom = 4.dp)
                                    .testTag("onboarding_start_button"),
                                contentAlignment = Alignment.Center
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Text(
                                        text = "Isi PIN / Sandi 6-Kotak Manual",
                                        style = MaterialTheme.typography.titleSmall,
                                        color = Color.White,
                                        fontWeight = FontWeight.ExtraBold
                                    )
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                        contentDescription = null,
                                        tint = Color.White,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            // Tombol Skip Setup Auto-Isi Default (Brankas 1: 111111-111111-111111, Pengaturan: 222222-222222-222222, Fun Game: 333333-333333-333333 & Auralys: 000000-000000-000000)
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(52.dp)
                                    .duolingoButton(
                                        faceColor = com.example.ui.theme.DuoMacawBlue,
                                        lipColor = com.example.ui.theme.DuoMacawBlueLip,
                                        cornerRadius = 16.dp,
                                        lipHeight = 4.dp
                                    )
                                    .instantTouchPress { triggerSkipSetupDefault() }
                                    .padding(bottom = 4.dp)
                                    .testTag("onboarding_quick_skip_button"),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "Skip Setup (Default: 111111, 222222, 333333 & 000000)",
                                    style = MaterialTheme.typography.labelLarge,
                                    color = Color.White,
                                    fontWeight = FontWeight.ExtraBold
                                )
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Default Skip Setup: Brankas #1 = 111111-111111-111111 • Pengaturan = 222222-222222-222222 • Fun Game = 333333-333333-333333 • Auralys = 000000-000000-000000 (Bisa diubah kapan saja di Pengaturan)",
                                style = MaterialTheme.typography.labelSmall,
                                color = CosmicTextMuted,
                                textAlign = TextAlign.Center,
                                fontSize = 10.sp
                            )
                        }
                    }
                }

                1 -> {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .duolingoCard(
                                backgroundColor = com.example.ui.theme.DuoPureWhite,
                                borderColor = com.example.ui.theme.DuoSwanBorder,
                                lipColor = com.example.ui.theme.DuoHareLip,
                                cornerRadius = 24.dp,
                                borderWidth = 2.dp,
                                lipHeight = 5.dp
                            )
                            .padding(bottom = 5.dp)
                    ) {
                        Column(modifier = Modifier.padding(18.dp)) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Surface(
                                    color = com.example.ui.theme.DuoMacawBlue.copy(alpha = 0.14f),
                                    shape = RoundedCornerShape(12.dp),
                                    modifier = Modifier.size(38.dp)
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Icon(
                                            imageVector = Icons.Default.Shield,
                                            contentDescription = null,
                                            tint = com.example.ui.theme.DuoMacawBlue,
                                            modifier = Modifier.size(20.dp)
                                        )
                                    }
                                }
                                Column {
                                    Text(
                                        text = "PIN / Sandi 6-Kotak — Brankas Utama #1",
                                        style = MaterialTheme.typography.titleMedium,
                                        color = CosmicTextPure,
                                        fontWeight = FontWeight.ExtraBold
                                    )
                                    Text(
                                        text = "Ketuk kotak di bawah untuk mengetik 6 karakter dengan keyboard bawaan.",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = CosmicTextMuted
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(14.dp))

                            OnboardingPinField(
                                label = "Brankas #1 • PIN Lapis 1 (Gerbang Utama)",
                                value = v1Pin1,
                                onValueChange = {
                                    v1Pin1 = it.uppercase().filter { ch -> ch.isDigit() || ch in 'A'..'Z' }.take(6)
                                },
                                accentColor = com.example.ui.theme.DuoMacawBlue,
                                tag = "setup_v1_pin1"
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            OnboardingPinField(
                                label = "Brankas #1 • PIN Lapis 2 (Subkey NYX2)",
                                value = v1Pin2,
                                onValueChange = {
                                    v1Pin2 = it.uppercase().filter { ch -> ch.isDigit() || ch in 'A'..'Z' }.take(6)
                                },
                                accentColor = com.example.ui.theme.DuoMacawBlue,
                                tag = "setup_v1_pin2"
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            OnboardingPinField(
                                label = "Brankas #1 • PIN Lapis 3 (Otorisasi Akhir)",
                                value = v1Pin3,
                                onValueChange = {
                                    v1Pin3 = it.uppercase().filter { ch -> ch.isDigit() || ch in 'A'..'Z' }.take(6)
                                },
                                accentColor = com.example.ui.theme.DuoMacawBlue,
                                tag = "setup_v1_pin3"
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            // Quick Auto-Fill Default Brankas #1 (111111 - 111111 - 111111)
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(42.dp)
                                    .duolingoCard(
                                        backgroundColor = com.example.ui.theme.DuoMacawBlueSoft,
                                        borderColor = com.example.ui.theme.DuoMacawBlue,
                                        lipColor = com.example.ui.theme.DuoMacawBlueLip,
                                        cornerRadius = 12.dp,
                                        borderWidth = 1.5.dp,
                                        lipHeight = 3.dp
                                    )
                                    .instantTouchPress {
                                        v1Pin1 = "111111"
                                        v1Pin2 = "111111"
                                        v1Pin3 = "111111"
                                        errorMessage = null
                                    }
                                    .padding(bottom = 3.dp)
                                    .testTag("onboarding_autofill_v1"),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "Isi Default Brankas #1 (111111 - 111111 - 111111)",
                                    style = MaterialTheme.typography.labelMedium,
                                    color = com.example.ui.theme.DuoMacawBlueLip,
                                    fontWeight = FontWeight.ExtraBold
                                )
                            }

                            if (errorMessage != null) {
                                Spacer(modifier = Modifier.height(10.dp))
                                Surface(
                                    color = com.example.ui.theme.DuoCardinalRedSoft,
                                    shape = RoundedCornerShape(12.dp),
                                    border = androidx.compose.foundation.BorderStroke(1.5.dp, com.example.ui.theme.DuoCardinalRed),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text(
                                        text = errorMessage!!,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = com.example.ui.theme.DuoCardinalRedLip,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(14.dp))

                            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                                OutlinedButton(
                                    onClick = {
                                        errorMessage = null
                                        currentStep = 0
                                    },
                                    modifier = Modifier
                                        .weight(0.38f)
                                        .height(46.dp),
                                    shape = RoundedCornerShape(14.dp),
                                    border = androidx.compose.foundation.BorderStroke(1.5.dp, com.example.ui.theme.DuoSwanBorder)
                                ) {
                                    Text("Kembali", color = CosmicTextPure, fontWeight = FontWeight.Bold)
                                }

                                Button(
                                    onClick = {
                                        if (v1Pin1.length != 6 || v1Pin2.length != 6 || v1Pin3.length != 6) {
                                            errorMessage = "Ketiga lapis PIN Brankas Utama wajib tepat 6 karakter!"
                                        } else {
                                            errorMessage = null
                                            currentStep = 2
                                        }
                                    },
                                    modifier = Modifier
                                        .weight(0.62f)
                                        .height(46.dp)
                                        .testTag("onboarding_next_v1"),
                                    shape = RoundedCornerShape(14.dp),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = com.example.ui.theme.DuoMacawBlue,
                                        contentColor = Color.White
                                    )
                                ) {
                                    Text(
                                        text = "Lanjut PIN Pengaturan",
                                        style = MaterialTheme.typography.labelLarge,
                                        fontWeight = FontWeight.ExtraBold
                                    )
                                }
                            }
                        }
                    }
                }

                2 -> {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .duolingoCard(
                                backgroundColor = com.example.ui.theme.DuoPureWhite,
                                borderColor = com.example.ui.theme.DuoSwanBorder,
                                lipColor = com.example.ui.theme.DuoHareLip,
                                cornerRadius = 24.dp,
                                borderWidth = 2.dp,
                                lipHeight = 5.dp
                            )
                            .padding(bottom = 5.dp)
                    ) {
                        Column(modifier = Modifier.padding(18.dp)) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Surface(
                                    color = com.example.ui.theme.DuoBeetlePurple.copy(alpha = 0.14f),
                                    shape = RoundedCornerShape(12.dp),
                                    modifier = Modifier.size(38.dp)
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Icon(
                                            imageVector = Icons.Default.Key,
                                            contentDescription = null,
                                            tint = com.example.ui.theme.DuoBeetlePurple,
                                            modifier = Modifier.size(20.dp)
                                        )
                                    }
                                }
                                Column {
                                    Text(
                                        text = "PIN / Sandi 6-Kotak — Ruang Pengaturan",
                                        style = MaterialTheme.typography.titleMedium,
                                        color = CosmicTextPure,
                                        fontWeight = FontWeight.ExtraBold
                                    )
                                    Text(
                                        text = "PIN masuk Pengaturan (default 222222-222222-222222). Harus berbeda dari Lapis 1 Brankas #1 ($v1Pin1).",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = CosmicTextMuted
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            Surface(
                                color = com.example.ui.theme.DuoBeetlePurple.copy(alpha = 0.08f),
                                shape = RoundedCornerShape(12.dp),
                                border = androidx.compose.foundation.BorderStroke(1.dp, com.example.ui.theme.DuoBeetlePurple.copy(alpha = 0.35f)),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = "Cara Membuka Ruang Pengaturan: Masukkan ke-3 lapis PIN ini pada layar kunci utama, atau klik tombol 'Pengaturan' di dalam brankas. PIN ini juga bisa diubah kapan saja di Pengaturan.",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = CosmicTextPure,
                                    modifier = Modifier.padding(10.dp)
                                )
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            OnboardingPinField(
                                label = "Pengaturan • PIN Lapis 1",
                                value = v2Pin1,
                                onValueChange = {
                                    v2Pin1 = it.uppercase().filter { ch -> ch.isDigit() || ch in 'A'..'Z' }.take(6)
                                },
                                accentColor = com.example.ui.theme.DuoBeetlePurple,
                                tag = "setup_v2_pin1"
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            OnboardingPinField(
                                label = "Pengaturan • PIN Lapis 2",
                                value = v2Pin2,
                                onValueChange = {
                                    v2Pin2 = it.uppercase().filter { ch -> ch.isDigit() || ch in 'A'..'Z' }.take(6)
                                },
                                accentColor = com.example.ui.theme.DuoBeetlePurple,
                                tag = "setup_v2_pin2"
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            OnboardingPinField(
                                label = "Pengaturan • PIN Lapis 3",
                                value = v2Pin3,
                                onValueChange = {
                                    v2Pin3 = it.uppercase().filter { ch -> ch.isDigit() || ch in 'A'..'Z' }.take(6)
                                },
                                accentColor = com.example.ui.theme.DuoBeetlePurple,
                                tag = "setup_v2_pin3"
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            // Quick Auto-Fill Default Pengaturan (222222 - 222222 - 222222)
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(42.dp)
                                    .duolingoCard(
                                        backgroundColor = com.example.ui.theme.DuoBeetlePurpleSoft,
                                        borderColor = com.example.ui.theme.DuoBeetlePurple,
                                        lipColor = com.example.ui.theme.DuoBeetlePurpleLip,
                                        cornerRadius = 12.dp,
                                        borderWidth = 1.5.dp,
                                        lipHeight = 3.dp
                                    )
                                    .instantTouchPress {
                                        val candidate1 = if (v1Pin1 == "222222") "999999" else "222222"
                                        v2Pin1 = candidate1
                                        v2Pin2 = "222222"
                                        v2Pin3 = "222222"
                                        errorMessage = null
                                    }
                                    .padding(bottom = 3.dp)
                                    .testTag("onboarding_autofill_v2"),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "Isi Default Pengaturan (222222 - 222222 - 222222)",
                                    style = MaterialTheme.typography.labelMedium,
                                    color = com.example.ui.theme.DuoBeetlePurpleLip,
                                    fontWeight = FontWeight.ExtraBold
                                )
                            }

                            Spacer(modifier = Modifier.height(14.dp))

                            // PIN Ruang Terisolasi Fun Game & Arcade 3D (Default Skip Setup: 333333 - 333333 - 333333)
                            Surface(
                                color = com.example.ui.theme.DuoFeatherGreen.copy(alpha = 0.08f),
                                shape = RoundedCornerShape(12.dp),
                                border = androidx.compose.foundation.BorderStroke(1.dp, com.example.ui.theme.DuoFeatherGreen.copy(alpha = 0.4f)),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(modifier = Modifier.padding(10.dp)) {
                                    Text(
                                        text = "PIN / Sandi 6-Kotak — Ruang Terisolasi Fun Game (Default Skip Setup: 333333-333333-333333)",
                                        style = MaterialTheme.typography.labelLarge,
                                        color = com.example.ui.theme.DuoFeatherGreenLip,
                                        fontWeight = FontWeight.ExtraBold
                                    )
                                    Text(
                                        text = "Ketik PIN 3-lapis ini di layar kunci utama untuk membuka Galaxy Tower Defense 200 Wave, Slot Gacha 3D, Tarot & Kapsul Waktu.",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = CosmicTextPure
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            OnboardingPinField(
                                label = "Fun Game • PIN Lapis 1",
                                value = funPin1,
                                onValueChange = {
                                    funPin1 = it.uppercase().filter { ch -> ch.isDigit() || ch in 'A'..'Z' }.take(6)
                                },
                                accentColor = com.example.ui.theme.DuoFeatherGreen,
                                tag = "setup_fun_pin1"
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                            OnboardingPinField(
                                label = "Fun Game • PIN Lapis 2",
                                value = funPin2,
                                onValueChange = {
                                    funPin2 = it.uppercase().filter { ch -> ch.isDigit() || ch in 'A'..'Z' }.take(6)
                                },
                                accentColor = com.example.ui.theme.DuoFeatherGreen,
                                tag = "setup_fun_pin2"
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                            OnboardingPinField(
                                label = "Fun Game • PIN Lapis 3",
                                value = funPin3,
                                onValueChange = {
                                    funPin3 = it.uppercase().filter { ch -> ch.isDigit() || ch in 'A'..'Z' }.take(6)
                                },
                                accentColor = com.example.ui.theme.DuoFeatherGreen,
                                tag = "setup_fun_pin3"
                            )

                            Spacer(modifier = Modifier.height(10.dp))

                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(42.dp)
                                    .duolingoCard(
                                        backgroundColor = com.example.ui.theme.DuoFeatherGreenSoft,
                                        borderColor = com.example.ui.theme.DuoFeatherGreen,
                                        lipColor = com.example.ui.theme.DuoFeatherGreenLip,
                                        cornerRadius = 12.dp,
                                        borderWidth = 1.5.dp,
                                        lipHeight = 3.dp
                                    )
                                    .instantTouchPress {
                                        funPin1 = "333333"
                                        funPin2 = "333333"
                                        funPin3 = "333333"
                                        errorMessage = null
                                    }
                                    .padding(bottom = 3.dp)
                                    .testTag("onboarding_autofill_fun_zone"),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "Isi Default Fun Game (333333 - 333333 - 333333)",
                                    style = MaterialTheme.typography.labelMedium,
                                    color = com.example.ui.theme.DuoFeatherGreenLip,
                                    fontWeight = FontWeight.ExtraBold
                                )
                            }

                            Spacer(modifier = Modifier.height(14.dp))

                            // PIN Ruang Terisolasi Auralys Music Chamber (Default Skip Setup: 000000 - 000000 - 000000)
                            Surface(
                                color = com.example.ui.AuralysAccentCyan.copy(alpha = 0.08f),
                                shape = RoundedCornerShape(12.dp),
                                border = androidx.compose.foundation.BorderStroke(1.dp, com.example.ui.AuralysAccentCyan.copy(alpha = 0.4f)),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(modifier = Modifier.padding(10.dp)) {
                                    Text(
                                        text = "PIN / Sandi 6-Kotak — Auralys Music Chamber (Default: 000000-000000-000000)",
                                        style = MaterialTheme.typography.labelLarge,
                                        color = com.example.ui.AuralysAccentCyan,
                                        fontWeight = FontWeight.ExtraBold
                                    )
                                    Text(
                                        text = "Ketik PIN 3-lapis ini di layar kunci utama untuk membuka Pemutar Musik Auralys 4.3.5, perpustakaan audio terenkripsi, lirik tersinkronisasi, dan streaming.",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = CosmicTextPure
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            OnboardingPinField(
                                label = "Auralys • PIN Lapis 1",
                                value = auralysPin1,
                                onValueChange = {
                                    auralysPin1 = it.uppercase().filter { ch -> ch.isDigit() || ch in 'A'..'Z' }.take(6)
                                },
                                accentColor = com.example.ui.AuralysAccentCyan,
                                tag = "setup_auralys_pin1"
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                            OnboardingPinField(
                                label = "Auralys • PIN Lapis 2",
                                value = auralysPin2,
                                onValueChange = {
                                    auralysPin2 = it.uppercase().filter { ch -> ch.isDigit() || ch in 'A'..'Z' }.take(6)
                                },
                                accentColor = com.example.ui.AuralysAccentCyan,
                                tag = "setup_auralys_pin2"
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                            OnboardingPinField(
                                label = "Auralys • PIN Lapis 3",
                                value = auralysPin3,
                                onValueChange = {
                                    auralysPin3 = it.uppercase().filter { ch -> ch.isDigit() || ch in 'A'..'Z' }.take(6)
                                },
                                accentColor = com.example.ui.AuralysAccentCyan,
                                tag = "setup_auralys_pin3"
                            )

                            Spacer(modifier = Modifier.height(10.dp))

                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(42.dp)
                                    .duolingoCard(
                                        backgroundColor = com.example.ui.AuralysSurfaceCard,
                                        borderColor = com.example.ui.AuralysAccentCyan,
                                        lipColor = com.example.ui.AuralysAccentCyan,
                                        cornerRadius = 12.dp,
                                        borderWidth = 1.5.dp,
                                        lipHeight = 3.dp
                                    )
                                    .instantTouchPress {
                                        auralysPin1 = "000000"
                                        auralysPin2 = "000000"
                                        auralysPin3 = "000000"
                                        errorMessage = null
                                    }
                                    .padding(bottom = 3.dp)
                                    .testTag("onboarding_autofill_auralys"),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "Isi Default Auralys (000000 - 000000 - 000000)",
                                    style = MaterialTheme.typography.labelMedium,
                                    color = com.example.ui.AuralysAccentCyan,
                                    fontWeight = FontWeight.ExtraBold
                                )
                            }

                            if (errorMessage != null) {
                                Spacer(modifier = Modifier.height(10.dp))
                                Surface(
                                    color = com.example.ui.theme.DuoCardinalRedSoft,
                                    shape = RoundedCornerShape(12.dp),
                                    border = androidx.compose.foundation.BorderStroke(1.5.dp, com.example.ui.theme.DuoCardinalRed),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text(
                                        text = errorMessage!!,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = com.example.ui.theme.DuoCardinalRedLip,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(14.dp))

                            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                                OutlinedButton(
                                    onClick = {
                                        errorMessage = null
                                        currentStep = 1
                                    },
                                    modifier = Modifier
                                        .weight(0.38f)
                                        .height(46.dp),
                                    shape = RoundedCornerShape(14.dp),
                                    border = androidx.compose.foundation.BorderStroke(1.5.dp, com.example.ui.theme.DuoSwanBorder)
                                ) {
                                    Text("Kembali", color = CosmicTextPure, fontWeight = FontWeight.Bold)
                                }

                                Button(
                                    onClick = {
                                        when {
                                            v2Pin1.length != 6 || v2Pin2.length != 6 || v2Pin3.length != 6 -> {
                                                errorMessage = "Ketiga lapis PIN Ruang Pengaturan wajib tepat 6 karakter!"
                                            }
                                            funPin1.length != 6 || funPin2.length != 6 || funPin3.length != 6 -> {
                                                errorMessage = "Ketiga lapis PIN Ruang Fun Game wajib tepat 6 karakter!"
                                            }
                                            auralysPin1.length != 6 || auralysPin2.length != 6 || auralysPin3.length != 6 -> {
                                                errorMessage = "Ketiga lapis PIN Auralys Music Chamber wajib tepat 6 karakter!"
                                            }
                                            v2Pin1.equals(v1Pin1, ignoreCase = true) -> {
                                                errorMessage = "PIN Lapis 1 Ruang Pengaturan tidak boleh bertabrakan dengan PIN Lapis 1 Brankas #1 ($v1Pin1)!"
                                            }
                                            funPin1.equals(v1Pin1, ignoreCase = true) -> {
                                                errorMessage = "PIN Lapis 1 Ruang Fun Game tidak boleh bertabrakan dengan PIN Lapis 1 Brankas #1 ($v1Pin1)!"
                                            }
                                            funPin1.equals(v2Pin1, ignoreCase = true) -> {
                                                errorMessage = "PIN Lapis 1 Ruang Fun Game tidak boleh bertabrakan dengan PIN Lapis 1 Pengaturan ($v2Pin1)!"
                                            }
                                            auralysPin1.equals(v1Pin1, ignoreCase = true) -> {
                                                errorMessage = "PIN Lapis 1 Auralys tidak boleh bertabrakan dengan PIN Lapis 1 Brankas #1 ($v1Pin1)!"
                                            }
                                            auralysPin1.equals(v2Pin1, ignoreCase = true) -> {
                                                errorMessage = "PIN Lapis 1 Auralys tidak boleh bertabrakan dengan PIN Lapis 1 Pengaturan ($v2Pin1)!"
                                            }
                                            auralysPin1.equals(funPin1, ignoreCase = true) -> {
                                                errorMessage = "PIN Lapis 1 Auralys tidak boleh bertabrakan dengan PIN Lapis 1 Fun Game ($funPin1)!"
                                            }
                                            else -> {
                                                errorMessage = null
                                                currentStep = 3
                                            }
                                        }
                                    },
                                    modifier = Modifier
                                        .weight(0.62f)
                                        .height(46.dp)
                                        .testTag("onboarding_next_v2"),
                                    shape = RoundedCornerShape(14.dp),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = com.example.ui.theme.DuoBeetlePurple,
                                        contentColor = Color.White
                                    )
                                ) {
                                    Text(
                                        text = "Lanjut Pemulihan",
                                        style = MaterialTheme.typography.labelLarge,
                                        fontWeight = FontWeight.ExtraBold
                                    )
                                }
                            }
                        }
                    }
                }

                3 -> {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .duolingoCard(
                                backgroundColor = com.example.ui.theme.DuoPureWhite,
                                borderColor = com.example.ui.theme.DuoSwanBorder,
                                lipColor = com.example.ui.theme.DuoHareLip,
                                cornerRadius = 24.dp,
                                borderWidth = 2.dp,
                                lipHeight = 5.dp
                            )
                            .padding(bottom = 5.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(18.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "Sidik Jari & 3 Pertanyaan Pemulihan",
                                style = MaterialTheme.typography.titleMedium,
                                color = CosmicTextPure,
                                fontWeight = FontWeight.ExtraBold,
                                textAlign = TextAlign.Center
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Digunakan untuk memulihkan PIN Brankas maupun PIN Ruang Pengaturan jika Anda lupa sandi.",
                                style = MaterialTheme.typography.bodySmall,
                                color = CosmicTextMuted,
                                textAlign = TextAlign.Center
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            // Pertanyaan 1
                            OutlinedTextField(
                                value = recoveryQuestion1,
                                onValueChange = { recoveryQuestion1 = it },
                                modifier = Modifier.fillMaxWidth(),
                                label = { Text("Pertanyaan Keamanan 1") },
                                singleLine = true,
                                shape = RoundedCornerShape(12.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedContainerColor = CosmicVoid,
                                    unfocusedContainerColor = CosmicVoid,
                                    focusedBorderColor = StarlightCyan,
                                    unfocusedBorderColor = CosmicSurface,
                                    focusedTextColor = CosmicTextPure,
                                    unfocusedTextColor = CosmicTextPure,
                                    focusedLabelColor = StarlightCyan,
                                    unfocusedLabelColor = CosmicTextMuted
                                )
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            OutlinedTextField(
                                value = recoveryAnswer1,
                                onValueChange = { recoveryAnswer1 = it },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("onboarding_recovery_answer_input"),
                                label = { Text("Jawaban Pertanyaan 1") },
                                placeholder = { Text("Jawaban 1...") },
                                singleLine = true,
                                shape = RoundedCornerShape(12.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedContainerColor = CosmicVoid,
                                    unfocusedContainerColor = CosmicVoid,
                                    focusedBorderColor = StarlightCyan,
                                    unfocusedBorderColor = CosmicSurface,
                                    focusedTextColor = CosmicTextPure,
                                    unfocusedTextColor = CosmicTextPure,
                                    focusedLabelColor = StarlightCyan,
                                    unfocusedLabelColor = CosmicTextMuted
                                )
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            // Pertanyaan 2
                            OutlinedTextField(
                                value = recoveryQuestion2,
                                onValueChange = { recoveryQuestion2 = it },
                                modifier = Modifier.fillMaxWidth(),
                                label = { Text("Pertanyaan Keamanan 2") },
                                singleLine = true,
                                shape = RoundedCornerShape(12.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedContainerColor = CosmicVoid,
                                    unfocusedContainerColor = CosmicVoid,
                                    focusedBorderColor = StarlightCyan,
                                    unfocusedBorderColor = CosmicSurface,
                                    focusedTextColor = CosmicTextPure,
                                    unfocusedTextColor = CosmicTextPure,
                                    focusedLabelColor = StarlightCyan,
                                    unfocusedLabelColor = CosmicTextMuted
                                )
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            OutlinedTextField(
                                value = recoveryAnswer2,
                                onValueChange = { recoveryAnswer2 = it },
                                modifier = Modifier.fillMaxWidth(),
                                label = { Text("Jawaban Pertanyaan 2") },
                                placeholder = { Text("Jawaban 2...") },
                                singleLine = true,
                                shape = RoundedCornerShape(12.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedContainerColor = CosmicVoid,
                                    unfocusedContainerColor = CosmicVoid,
                                    focusedBorderColor = StarlightCyan,
                                    unfocusedBorderColor = CosmicSurface,
                                    focusedTextColor = CosmicTextPure,
                                    unfocusedTextColor = CosmicTextPure,
                                    focusedLabelColor = StarlightCyan,
                                    unfocusedLabelColor = CosmicTextMuted
                                )
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            // Pertanyaan 3
                            OutlinedTextField(
                                value = recoveryQuestion3,
                                onValueChange = { recoveryQuestion3 = it },
                                modifier = Modifier.fillMaxWidth(),
                                label = { Text("Pertanyaan Keamanan 3") },
                                singleLine = true,
                                shape = RoundedCornerShape(12.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedContainerColor = CosmicVoid,
                                    unfocusedContainerColor = CosmicVoid,
                                    focusedBorderColor = StarlightCyan,
                                    unfocusedBorderColor = CosmicSurface,
                                    focusedTextColor = CosmicTextPure,
                                    unfocusedTextColor = CosmicTextPure,
                                    focusedLabelColor = StarlightCyan,
                                    unfocusedLabelColor = CosmicTextMuted
                                )
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            OutlinedTextField(
                                value = recoveryAnswer3,
                                onValueChange = { recoveryAnswer3 = it },
                                modifier = Modifier.fillMaxWidth(),
                                label = { Text("Jawaban Pertanyaan 3") },
                                placeholder = { Text("Jawaban 3...") },
                                singleLine = true,
                                shape = RoundedCornerShape(12.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedContainerColor = CosmicVoid,
                                    unfocusedContainerColor = CosmicVoid,
                                    focusedBorderColor = StarlightCyan,
                                    unfocusedBorderColor = CosmicSurface,
                                    focusedTextColor = CosmicTextPure,
                                    unfocusedTextColor = CosmicTextPure,
                                    focusedLabelColor = StarlightCyan,
                                    unfocusedLabelColor = CosmicTextMuted
                                )
                            )

                            Spacer(modifier = Modifier.height(10.dp))

                            // Biometric Switch Row
                            Surface(
                                color = CosmicSurface,
                                shape = RoundedCornerShape(14.dp),
                                modifier = Modifier.fillMaxWidth(),
                                border = androidx.compose.foundation.BorderStroke(1.dp, CosmicBorderHairline)
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 12.dp, vertical = 8.dp),
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
                                                text = "Sensor Sidik Jari Lapis 4",
                                                style = MaterialTheme.typography.titleSmall,
                                                color = CosmicTextPure,
                                                fontWeight = FontWeight.Bold
                                            )
                                            Text(
                                                text = if (isFingerprintRegisteredInWizard) {
                                                    "Sidik Jari Terdaftar (100%)"
                                                } else {
                                                    "Verifikasi sidik jari saat membuka ruang"
                                                },
                                                style = MaterialTheme.typography.bodySmall,
                                                color = CosmicTextMuted
                                            )
                                        }
                                    }
                                    Switch(
                                        checked = biometricEnabled,
                                        onCheckedChange = { biometricEnabled = it },
                                        colors = SwitchDefaults.colors(
                                            checkedThumbColor = StarlightCyan,
                                            checkedTrackColor = CosmicVoid
                                        )
                                    )
                                }
                            }

                            if (biometricEnabled) {
                                Spacer(modifier = Modifier.height(10.dp))
                                InteractiveFingerprintScannerPad(
                                    onScanCompleted = {
                                        isFingerprintRegisteredInWizard = true
                                        fingerprintStatusMessage = "Sidik jari berhasil direkam & diverifikasi (100%)!"
                                    },
                                    onPartialReleaseWarning = { msg ->
                                        fingerprintStatusMessage = msg
                                    }
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = fingerprintStatusMessage,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = if (isFingerprintRegisteredInWizard) com.example.ui.theme.DuoFeatherGreenLip else CosmicTextMuted,
                                    textAlign = TextAlign.Center
                                )
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            // Compact Google Cloud Backup Link Option
                            Surface(
                                color = CosmicSurface,
                                shape = RoundedCornerShape(14.dp),
                                modifier = Modifier.fillMaxWidth(),
                                border = androidx.compose.foundation.BorderStroke(
                                    1.dp,
                                    if (googleEmail.isNotBlank()) PastelMint else CosmicBorderHairline
                                )
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 12.dp, vertical = 10.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Icon(
                                            imageVector = if (googleEmail.isNotBlank()) Icons.Default.CheckCircle else Icons.Default.AccountCircle,
                                            contentDescription = null,
                                            tint = if (googleEmail.isNotBlank()) PastelMint else StarlightCyan,
                                            modifier = Modifier.size(22.dp)
                                        )
                                        Column {
                                            Text(
                                                text = if (googleEmail.isNotBlank()) googleEmail else "Akun Google (OPSIONAL)",
                                                style = MaterialTheme.typography.labelLarge,
                                                color = CosmicTextPure,
                                                fontWeight = FontWeight.Bold
                                            )
                                            Text(
                                                text = "Jika tidak login, fitur Cloud Backup & Report/Saran mati total",
                                                style = MaterialTheme.typography.labelSmall,
                                                color = CosmicTextMuted
                                            )
                                        }
                                    }
                                    OutlinedButton(
                                        onClick = { triggerGoogleLoginFlow(false) },
                                        shape = RoundedCornerShape(10.dp),
                                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                                        modifier = Modifier
                                            .height(34.dp)
                                            .testTag("onboarding_step3_google_login_button")
                                    ) {
                                        Text(
                                            text = if (googleEmail.isNotBlank()) "Ganti" else "Login Google",
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = StarlightCyan
                                        )
                                    }
                                }
                            }

                            if (errorMessage != null) {
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = errorMessage!!,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = com.example.ui.theme.DuoCardinalRedLip,
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            // Tombol Lewati Cepat Pertanyaan Pemulihan
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(40.dp)
                                    .duolingoCard(
                                        backgroundColor = com.example.ui.theme.DuoFeatherGreenSoft,
                                        borderColor = com.example.ui.theme.DuoFeatherGreen,
                                        lipColor = com.example.ui.theme.DuoFeatherGreenLip,
                                        cornerRadius = 12.dp,
                                        borderWidth = 1.5.dp,
                                        lipHeight = 3.dp
                                    )
                                    .instantTouchPress {
                                        if (recoveryAnswer1.isBlank()) recoveryAnswer1 = "Jakarta"
                                        if (recoveryAnswer2.isBlank()) recoveryAnswer2 = "Kucing"
                                        if (recoveryAnswer3.isBlank()) recoveryAnswer3 = "Nasi Goreng"
                                        errorMessage = null
                                        currentStep = 4
                                    }
                                    .padding(bottom = 3.dp)
                                    .testTag("onboarding_quick_skip_recovery"),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "Lewati Cepat & Auto-Isi Jawaban Pemulihan →",
                                    style = MaterialTheme.typography.labelMedium,
                                    color = com.example.ui.theme.DuoFeatherGreenLip,
                                    fontWeight = FontWeight.ExtraBold
                                )
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                                OutlinedButton(
                                    onClick = {
                                        errorMessage = null
                                        currentStep = 2
                                    },
                                    modifier = Modifier
                                        .weight(0.38f)
                                        .height(46.dp),
                                    shape = RoundedCornerShape(14.dp),
                                    border = androidx.compose.foundation.BorderStroke(1.5.dp, com.example.ui.theme.DuoSwanBorder)
                                ) {
                                    Text("Kembali", color = CosmicTextPure, fontWeight = FontWeight.Bold)
                                }

                                Button(
                                    onClick = {
                                        if (recoveryAnswer1.isBlank() || recoveryAnswer2.isBlank() || recoveryAnswer3.isBlank()) {
                                            errorMessage = "Ketiga jawaban pertanyaan keamanan wajib diisi untuk pemulihan!"
                                            return@Button
                                        }
                                        errorMessage = null
                                        currentStep = 4
                                    },
                                    modifier = Modifier
                                        .weight(0.62f)
                                        .height(46.dp)
                                        .testTag("onboarding_next_to_rules_button"),
                                    shape = RoundedCornerShape(14.dp),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = com.example.ui.theme.DuoFeatherGreen,
                                        contentColor = Color.White
                                    )
                                ) {
                                    Text(
                                        text = "Lanjut Konfirmasi →",
                                        style = MaterialTheme.typography.labelLarge,
                                        fontWeight = FontWeight.ExtraBold
                                    )
                                }
                            }
                        }
                    }
                }

                4 -> {
                    // Step 4: Ringkasan Konfigurasi & Privasi
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .duolingoCard(
                                backgroundColor = com.example.ui.theme.DuoPureWhite,
                                borderColor = com.example.ui.theme.DuoSwanBorder,
                                lipColor = com.example.ui.theme.DuoHareLip,
                                cornerRadius = 24.dp,
                                borderWidth = 2.dp,
                                lipHeight = 5.dp
                            )
                            .padding(bottom = 5.dp)
                    ) {
                        Column(modifier = Modifier.padding(18.dp)) {
                            Text(
                                text = "Ringkasan Setup & Proteksi",
                                style = MaterialTheme.typography.titleMedium,
                                color = CosmicTextPure,
                                fontWeight = FontWeight.ExtraBold
                            )
                            Text(
                                text = "Pastikan Anda mengingat kedua pasang PIN 6-digit 3 lapis di bawah ini:",
                                style = MaterialTheme.typography.bodySmall,
                                color = CosmicTextMuted
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            // Summary Box 1: Brankas Utama #1
                            Surface(
                                color = com.example.ui.theme.DuoMacawBlue.copy(alpha = 0.1f),
                                shape = RoundedCornerShape(14.dp),
                                border = androidx.compose.foundation.BorderStroke(1.5.dp, com.example.ui.theme.DuoMacawBlue),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(modifier = Modifier.padding(12.dp)) {
                                    Text(
                                        text = "RUANG 1: BRANKAS UTAMA #1",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.ExtraBold,
                                        color = com.example.ui.theme.DuoMacawBlueLip
                                    )
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(
                                        text = "Lapis 1: $v1Pin1  •  Lapis 2: $v1Pin2  •  Lapis 3: $v1Pin3",
                                        fontFamily = JetBrainsMonoFontFamily,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = CosmicTextPure
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            // Summary Box 2: Ruang Pengaturan Terisolasi
                            Surface(
                                color = com.example.ui.theme.DuoBeetlePurple.copy(alpha = 0.1f),
                                shape = RoundedCornerShape(14.dp),
                                border = androidx.compose.foundation.BorderStroke(1.5.dp, com.example.ui.theme.DuoBeetlePurple),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(modifier = Modifier.padding(12.dp)) {
                                    Text(
                                        text = "RUANG 2: PENGATURAN TERISOLASI (RAHASIA)",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.ExtraBold,
                                        color = com.example.ui.theme.DuoBeetlePurpleLip
                                    )
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(
                                        text = "Lapis 1: $v2Pin1  •  Lapis 2: $v2Pin2  •  Lapis 3: $v2Pin3",
                                        fontFamily = JetBrainsMonoFontFamily,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = CosmicTextPure
                                    )
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(
                                        text = "Masukkan 3 PIN ini di layar kunci utama untuk membuka pengaturan & menambah brankas baru.",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = CosmicTextMuted
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            // Summary Box 3: Ruang Fun Game & Arcade Terisolasi
                            Surface(
                                color = com.example.ui.theme.DuoFeatherGreen.copy(alpha = 0.1f),
                                shape = RoundedCornerShape(14.dp),
                                border = androidx.compose.foundation.BorderStroke(1.5.dp, com.example.ui.theme.DuoFeatherGreen),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(modifier = Modifier.padding(12.dp)) {
                                    Text(
                                        text = "RUANG 3: FUN GAME & ARCADE TERISOLASI 3D",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.ExtraBold,
                                        color = com.example.ui.theme.DuoFeatherGreenLip
                                    )
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(
                                        text = "Lapis 1: $funPin1  •  Lapis 2: $funPin2  •  Lapis 3: $funPin3",
                                        fontFamily = JetBrainsMonoFontFamily,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = CosmicTextPure
                                    )
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(
                                        text = "Default Skip Setup: 333333-333333-333333. Masukkan di layar kunci untuk masuk ke Galaxy TD 200 Wave, Slot Gacha 3D, Tarot & Kapsul Waktu.",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = CosmicTextMuted
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            // Summary Box 4: Ruang Auralys Music Chamber Terisolasi
                            Surface(
                                color = com.example.ui.AuralysAccentCyan.copy(alpha = 0.1f),
                                shape = RoundedCornerShape(14.dp),
                                border = androidx.compose.foundation.BorderStroke(1.5.dp, com.example.ui.AuralysAccentCyan),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(modifier = Modifier.padding(12.dp)) {
                                    Text(
                                        text = "RUANG 4: AURALYS MUSIC CHAMBER TERISOLASI",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.ExtraBold,
                                        color = com.example.ui.AuralysAccentCyan
                                    )
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(
                                        text = "Lapis 1: $auralysPin1  •  Lapis 2: $auralysPin2  •  Lapis 3: $auralysPin3",
                                        fontFamily = JetBrainsMonoFontFamily,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = CosmicTextPure
                                    )
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(
                                        text = "Default Skip Setup: 000000-000000-000000. Masukkan di layar kunci untuk membuka Pemutar Musik Auralys 4.3.5 & brankas audio.",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = CosmicTextMuted
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            // Compact Rules Toggles
                            CompactRuleSwitchRow(
                                title = "Mode Siluman (Stealth Pengaturan)",
                                subtitle = "Sembunyikan menu pengaturan dari brankas biasa",
                                checked = stealthModeRule,
                                onCheckedChange = { stealthModeRule = it }
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            CompactRuleSwitchRow(
                                title = "Ekspor Terenkripsi Wajib Sandi (.nyx)",
                                subtitle = "File yang diekspor wajib memakai sandi pembuat",
                                checked = encryptedExportRule,
                                onCheckedChange = { encryptedExportRule = it }
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            CompactRuleSwitchRow(
                                title = "Kunci Otomatis Saat Keluar Aplikasi",
                                subtitle = "Kunci segera saat layar mati atau berpindah aplikasi",
                                checked = autoLockRule,
                                onCheckedChange = { autoLockRule = it }
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            if (errorMessage != null) {
                                Text(
                                    text = errorMessage!!,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = com.example.ui.theme.DuoCardinalRedLip,
                                    fontWeight = FontWeight.ExtraBold
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                            }

                            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                                OutlinedButton(
                                    onClick = { currentStep = 3 },
                                    modifier = Modifier
                                        .weight(0.35f)
                                        .height(48.dp),
                                    shape = RoundedCornerShape(14.dp),
                                    border = androidx.compose.foundation.BorderStroke(1.5.dp, com.example.ui.theme.DuoSwanBorder)
                                ) {
                                    Text("Kembali", color = CosmicTextPure, fontWeight = FontWeight.Bold)
                                }

                                Button(
                                    onClick = {
                                        executeCompleteSetupWithGoogle(googleEmail, googleDisplayName)
                                    },
                                    modifier = Modifier
                                        .weight(0.65f)
                                        .height(48.dp)
                                        .testTag("onboarding_finish_button"),
                                    shape = RoundedCornerShape(14.dp),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = com.example.ui.theme.DuoFeatherGreen,
                                        contentColor = Color.White
                                    )
                                ) {
                                    Icon(Icons.Default.CheckCircle, contentDescription = null, tint = Color.White, modifier = Modifier.size(18.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = if (googleEmail.isBlank()) "Masuk Tanpa Google (Lokal)" else "Simpan & Buka Brankas",
                                        style = MaterialTheme.typography.labelLarge,
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

        // Overlay Sambutan Maskot Full-Screen yang mengecil & bergerak mulus ke pojok kiri atas
        if (mascotMoveProgress < 0.99f) {
            val overlayAlpha = (1f - mascotMoveProgress * 1.15f).coerceIn(0f, 1f)
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .graphicsLayer { alpha = overlayAlpha }
                    .background(CosmicVoid.copy(alpha = 0.94f * overlayAlpha))
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = { isMascotGreetingFullscreen = false }
                    )
                    .padding(24.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.graphicsLayer {
                        val scale = 1f - (0.65f * mascotMoveProgress)
                        scaleX = scale
                        scaleY = scale
                        translationX = -mascotMoveProgress * 130.dp.toPx()
                        translationY = -mascotMoveProgress * 250.dp.toPx()
                    }
                ) {
                    CosmicDragonAvatar(
                        mood = MascotMood.WAVE_HELLO,
                        size = 192.dp,
                        externalCheerKey = 99
                    )
                    Spacer(modifier = Modifier.height(18.dp))
                    Box(
                        modifier = Modifier
                            .widthIn(max = 340.dp)
                            .duolingoCard(
                                backgroundColor = com.example.ui.theme.DuoPureWhite,
                                borderColor = com.example.ui.theme.DuoFeatherGreen,
                                lipColor = com.example.ui.theme.DuoFeatherGreenLip,
                                cornerRadius = 22.dp,
                                borderWidth = 2.5.dp,
                                lipHeight = 5.dp
                            )
                            .padding(horizontal = 20.dp, vertical = 16.dp)
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "Halo! Aku Maskot Folder Brankasmu!",
                                style = MaterialTheme.typography.titleMedium,
                                color = CosmicTextPure,
                                fontWeight = FontWeight.ExtraBold,
                                textAlign = TextAlign.Center
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "Aku akan mengecil ke pojok kiri atas untuk menemanimu mengisi PIN 6-Kotak atau gunakan Skip Setup!",
                                style = MaterialTheme.typography.bodySmall,
                                color = CosmicTextMuted,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun CompactRuleSwitchRow(
    title: String,
    subtitle: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Surface(
        color = CosmicSurface.copy(alpha = 0.55f),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.fillMaxWidth(),
        border = androidx.compose.foundation.BorderStroke(1.dp, CosmicBorderHairline)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold,
                    color = CosmicTextPure
                )
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    fontSize = 11.sp,
                    color = CosmicTextMuted
                )
            }
            Switch(
                checked = checked,
                onCheckedChange = onCheckedChange,
                colors = SwitchDefaults.colors(
                    checkedThumbColor = StarlightCyan,
                    checkedTrackColor = CosmicVoid
                )
            )
        }
    }
}

@Composable
fun InteractiveFingerprintScannerPad(
    onScanCompleted: () -> Unit,
    onPartialReleaseWarning: (String) -> Unit
) {
    var scanProgress by remember { mutableStateOf(0f) }
    var isScanning by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    Box(
        modifier = Modifier
            .size(72.dp)
            .clip(CircleShape)
            .background(CosmicSurface)
            .border(2.dp, if (scanProgress >= 1f) StarlightCyan else CosmicBorderHairline, CircleShape)
            .pointerInput(Unit) {
                awaitEachGesture {
                    val down = awaitFirstDown(requireUnconsumed = false)
                    isScanning = true
                    down.consume()
                    scope.launch {
                        while (isScanning && scanProgress < 1f) {
                            kotlinx.coroutines.delay(40)
                            scanProgress = (scanProgress + 0.05f).coerceAtMost(1f)
                        }
                        if (scanProgress >= 1f) {
                            onScanCompleted()
                        }
                    }
                    waitForUpOrCancellation()
                    if (scanProgress < 1f) {
                        isScanning = false
                        scanProgress = 0f
                        onPartialReleaseWarning("Pelepasan jari terlalu cepat. Tahan hingga lingkaran penuh 100%.")
                    }
                }
            },
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawArc(
                color = StarlightCyan,
                startAngle = -90f,
                sweepAngle = 360f * scanProgress,
                useCenter = false,
                style = Stroke(width = 4.dp.toPx(), cap = StrokeCap.Round)
            )
        }
        Icon(
            imageVector = Icons.Default.Fingerprint,
            contentDescription = "Pindai Sidik Jari",
            tint = if (scanProgress >= 1f) StarlightCyan else CosmicTextPure,
            modifier = Modifier.size(36.dp)
        )
    }
}

@Composable
private fun FeatureHighlightRow(title: String, desc: String) {
    Surface(
        color = CosmicSurface,
        shape = RoundedCornerShape(14.dp),
        modifier = Modifier.fillMaxWidth(),
        border = androidx.compose.foundation.BorderStroke(1.dp, CosmicBorderHairline)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(RoundedCornerShape(9.dp))
                    .background(StarlightCyan),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Shield,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(18.dp)
                )
            }
            Column {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.ExtraBold,
                    color = CosmicTextPure
                )
                Text(
                    text = desc,
                    style = MaterialTheme.typography.bodySmall,
                    color = CosmicTextMuted
                )
            }
        }
    }
}

/**
 * Komponen Input PIN / Sandi 6-Kotak dengan Keyboard Bawaan Sistem (6-Box System Keyboard PIN/PW Input):
 * - Menampilkan 6 kotak 3D terpisah (1 karakter per kotak).
 * - Ketika diketuk, memicu keyboard bawaan Android (mendukung angka 0-9 dan huruf A-Z).
 */
@Composable
fun OnboardingPinField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    accentColor: Color = StarlightCyan,
    tag: String
) {
    var isFocused by remember { mutableStateOf(false) }
    val infinite = rememberInfiniteTransition(label = "pin_box_cursor")
    val cursorAlpha by infinite.animateFloat(
        initialValue = 1f,
        targetValue = 0.15f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 520, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "cursor_blink"
    )

    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelMedium,
                color = CosmicTextPure,
                fontWeight = FontWeight.ExtraBold
            )
            Text(
                text = "${value.length}/6 kotak",
                fontFamily = JetBrainsMonoFontFamily,
                fontSize = 11.sp,
                fontWeight = FontWeight.ExtraBold,
                color = if (value.length == 6) com.example.ui.theme.DuoFeatherGreenLip else accentColor
            )
        }
        Spacer(modifier = Modifier.height(6.dp))

        androidx.compose.foundation.text.BasicTextField(
            value = value,
            onValueChange = { raw ->
                val cleaned = raw.uppercase().filter { ch -> ch.isDigit() || ch in 'A'..'Z' }.take(6)
                onValueChange(cleaned)
            },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Ascii,
                capitalization = androidx.compose.ui.text.input.KeyboardCapitalization.Characters,
                imeAction = androidx.compose.ui.text.input.ImeAction.Next
            ),
            cursorBrush = androidx.compose.ui.graphics.SolidColor(accentColor),
            modifier = Modifier
                .fillMaxWidth()
                .onFocusChanged { isFocused = it.isFocused }
                .testTag(tag),
            decorationBox = { innerTextField ->
                Box(modifier = Modifier.fillMaxWidth()) {
                    // Hidden anchor for Android IME native keyboard connection
                    Box(
                        modifier = Modifier
                            .size(1.dp)
                            .graphicsLayer { alpha = 0f }
                    ) {
                        innerTextField()
                    }

                    // 6 Kotak Visual 3D Duolingo
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        for (boxIndex in 0 until 6) {
                            val charStr = value.getOrNull(boxIndex)?.toString().orEmpty()
                            val isFilled = charStr.isNotEmpty()
                            val isCurrentCursorBox = isFocused && (value.length == boxIndex || (value.length == 6 && boxIndex == 5))

                            val boxScale by androidx.compose.animation.core.animateFloatAsState(
                                targetValue = when {
                                    isCurrentCursorBox -> 1.05f
                                    isFilled -> 1.0f
                                    else -> 0.97f
                                },
                                animationSpec = spring(
                                    dampingRatio = Spring.DampingRatioMediumBouncy,
                                    stiffness = Spring.StiffnessMedium
                                ),
                                label = "pin_box_scale_$boxIndex"
                            )

                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .height(52.dp)
                                    .graphicsLayer {
                                        scaleX = boxScale
                                        scaleY = boxScale
                                    }
                                    .duolingoCard(
                                        backgroundColor = when {
                                            isFilled -> accentColor.copy(alpha = 0.12f)
                                            isCurrentCursorBox -> com.example.ui.theme.DuoPureWhite
                                            else -> CosmicVoid
                                        },
                                        borderColor = when {
                                            isCurrentCursorBox -> accentColor
                                            isFilled -> accentColor
                                            else -> com.example.ui.theme.DuoSwanBorder
                                        },
                                        lipColor = when {
                                            isCurrentCursorBox || isFilled -> accentColor
                                            else -> com.example.ui.theme.DuoHareLip
                                        },
                                        cornerRadius = 14.dp,
                                        borderWidth = if (isCurrentCursorBox) 2.5.dp else 2.dp,
                                        lipHeight = 4.dp
                                    )
                                    .padding(bottom = 4.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                if (isFilled) {
                                    Text(
                                        text = charStr,
                                        fontFamily = JetBrainsMonoFontFamily,
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.ExtraBold,
                                        color = CosmicTextPure,
                                        textAlign = TextAlign.Center
                                    )
                                } else if (isCurrentCursorBox) {
                                    Box(
                                        modifier = Modifier
                                            .width(2.5.dp)
                                            .height(22.dp)
                                            .graphicsLayer { alpha = cursorAlpha }
                                            .background(accentColor, RoundedCornerShape(2.dp))
                                    )
                                } else {
                                    Text(
                                        text = "•",
                                        fontFamily = JetBrainsMonoFontFamily,
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = CosmicTextMuted.copy(alpha = 0.45f)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        )
    }
}

/**
 * Dialog OAuth 2.0 Login Pihak Ketiga Google Terintegrasi Arcanum (Per-User Local Binding):
 * - Menggunakan Pemilih Akun Google Sistem Android (`AccountManager.newChooseAccountIntent`)
 *   serta Google Play Services OAuth (`GoogleSignInClient`)
 * - Dilengkapi Layar Persetujuan OAuth 2.0 Resmi ("Tautkan Akun Google ke Arcanum Vault")
 * - Menyimpan kredensial dan tautan data brankas secara lokal di perangkat masing-masing pengguna
 *   tanpa email developer bawaan, sehingga siap dipublikasikan untuk semua pengguna.
 */
@Composable
fun ArcanumGoogleSignInDialog(
    currentEmail: String = "",
    currentName: String = "",
    isMandatoryGate: Boolean = false,
    onDismiss: () -> Unit,
    onLaunchNativeGoogleSignIn: (() -> Unit)? = null,
    onSignedIn: (email: String, displayName: String) -> Unit
) {
    val context = androidx.compose.ui.platform.LocalContext.current
    val oauthPrefs = remember(context) {
        context.getSharedPreferences("arcanum_google_oauth_local", android.content.Context.MODE_PRIVATE)
    }

    // Ambil riwayat akun Google yang pernah ditautkan secara lokal di perangkat pengguna ini (bukan hardcode!)
    var savedDeviceAccounts by remember {
        val rawSet = oauthPrefs.getStringSet("linked_google_accounts", emptySet()).orEmpty()
        val parsed = rawSet.mapNotNull { entry ->
            val parts = entry.split("|", limit = 2)
            if (parts.size == 2 && parts[0].contains("@")) parts[0] to parts[1] else null
        }.toMutableList()
        // Cek juga jika ada akun GoogleSignIn terakhir dari Play Services di HP pengguna
        try {
            val lastAcct = com.google.android.gms.auth.api.signin.GoogleSignIn.getLastSignedInAccount(context)
            val lastEmail = lastAcct?.email?.trim().orEmpty()
            if (lastEmail.contains("@") && parsed.none { it.first.equals(lastEmail, ignoreCase = true) }) {
                val lastName = lastAcct?.displayName?.trim()?.ifBlank { lastEmail.substringBefore("@") }
                    ?: lastEmail.substringBefore("@")
                parsed.add(0, lastEmail to lastName)
            }
        } catch (_: Exception) {
        }
        mutableStateOf(parsed.toList())
    }

    var emailInput by remember(currentEmail) {
        mutableStateOf(currentEmail.trim())
    }
    var nameInput by remember(currentName) {
        mutableStateOf(currentName.trim())
    }
    // Step 1 = Pilih / Masukkan Akun Google Pihak Ketiga, Step 2 = Layar Persetujuan OAuth 2.0 Resmi Arcanum
    var oauthStep by remember { mutableStateOf(1) }
    var validationError by remember { mutableStateOf<String?>(null) }

    val saveAndCompleteOAuth: (String, String) -> Unit = { finalEmail, finalName ->
        val cleanEmail = finalEmail.trim().lowercase()
        val cleanName = finalName.trim().ifBlank {
            cleanEmail.substringBefore("@").replace(".", " ").replaceFirstChar { it.uppercase() }
        }
        val existingSet = oauthPrefs.getStringSet("linked_google_accounts", emptySet()).orEmpty().toMutableSet()
        existingSet.removeAll { it.startsWith("$cleanEmail|", ignoreCase = true) }
        existingSet.add("$cleanEmail|$cleanName")
        val tokenHash = java.util.UUID.nameUUIDFromBytes("arcanum_oauth_$cleanEmail".toByteArray()).toString()
        oauthPrefs.edit()
            .putStringSet("linked_google_accounts", existingSet)
            .putString("active_oauth_email", cleanEmail)
            .putString("active_oauth_name", cleanName)
            .putString("oauth_token_$cleanEmail", tokenHash)
            .putLong("oauth_linked_at_$cleanEmail", System.currentTimeMillis())
            .apply()
        onSignedIn(cleanEmail, cleanName)
    }

    // Launcher Pemilih Akun Google Sistem Android Resmi (AccountManager.newChooseAccountIntent)
    val systemAccountPickerLauncher = androidx.activity.compose.rememberLauncherForActivityResult(
        contract = androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult()
    ) { result ->
        val data = result.data
        val pickedAccountName = data?.getStringExtra(android.accounts.AccountManager.KEY_ACCOUNT_NAME)?.trim().orEmpty()
        if (pickedAccountName.contains("@")) {
            emailInput = pickedAccountName
            if (nameInput.isBlank()) {
                nameInput = pickedAccountName
                    .substringBefore("@")
                    .replace(".", " ")
                    .replace("_", " ")
                    .split(" ")
                    .filter { it.isNotBlank() }
                    .joinToString(" ") { part -> part.replaceFirstChar { it.uppercase() } }
                    .ifBlank { "Pengguna Google" }
            }
            validationError = null
            oauthStep = 2 // Langsung masuk ke layar Persetujuan OAuth 2.0 Resmi
        }
    }

    // Launcher Google Play Services SignIn Intent Resmi
    val playServicesSignInLauncher = androidx.activity.compose.rememberLauncherForActivityResult(
        contract = androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult()
    ) { result ->
        try {
            val task = com.google.android.gms.auth.api.signin.GoogleSignIn.getSignedInAccountFromIntent(result.data)
            val account = task.result
            val gEmail = account?.email?.trim().orEmpty()
            if (gEmail.contains("@")) {
                emailInput = gEmail
                nameInput = account?.displayName?.trim()?.ifBlank { gEmail.substringBefore("@") }
                    ?: gEmail.substringBefore("@")
                validationError = null
                oauthStep = 2
            }
        } catch (_: Exception) {
            // Jika emulator tidak memiliki Play Services SHA-1 terdaftar, arahkan ke System Account Picker / OAuth Input
            try {
                @Suppress("DEPRECATION")
                val intent = android.accounts.AccountManager.newChooseAccountIntent(
                    null,
                    null,
                    arrayOf("com.google"),
                    false,
                    null,
                    null,
                    null,
                    null
                )
                systemAccountPickerLauncher.launch(intent)
            } catch (_: Exception) {
                validationError = "Pilih akun Google perangkat atau ketik alamat akun Google Anda di bawah untuk lanjut ke OAuth 2.0."
            }
        }
    }

    androidx.compose.ui.window.Dialog(
        onDismissRequest = {
            if (!isMandatoryGate) onDismiss()
        },
        properties = androidx.compose.ui.window.DialogProperties(
            dismissOnBackPress = !isMandatoryGate,
            dismissOnClickOutside = !isMandatoryGate
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .duolingoCard(
                    backgroundColor = com.example.ui.theme.DuoPureWhite,
                    borderColor = com.example.ui.theme.DuoMacawBlue,
                    lipColor = com.example.ui.theme.DuoMacawBlueLip,
                    cornerRadius = 26.dp,
                    borderWidth = 2.5.dp,
                    lipHeight = 6.dp
                )
                .padding(bottom = 6.dp)
                .testTag("arcanum_google_signin_dialog")
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Header Resmi Google OAuth 2.0 Relying Party
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .background(com.example.ui.theme.DuoMacawBlue),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "G",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color.White
                        )
                    }
                    Column(modifier = Modifier.weight(1f)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = if (oauthStep == 1) "Login Pihak Ketiga Google" else "Otorisasi OAuth 2.0 Google",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.ExtraBold,
                                color = CosmicTextPure,
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(end = 6.dp)
                            )
                            Surface(
                                color = com.example.ui.theme.DuoFeatherGreenSoft,
                                shape = RoundedCornerShape(6.dp),
                                border = androidx.compose.foundation.BorderStroke(1.dp, com.example.ui.theme.DuoFeatherGreen)
                            ) {
                                Text(
                                    text = "LOKAL PER-USER",
                                    fontFamily = JetBrainsMonoFontFamily,
                                    fontSize = 8.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = com.example.ui.theme.DuoFeatherGreenLip,
                                    maxLines = 1,
                                    softWrap = false,
                                    modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp)
                                )
                            }
                        }
                        Text(
                            text = if (oauthStep == 1) {
                                "Gunakan akun Google masing-masing pengguna. Data brankas disimpan 100% lokal di perangkat Anda."
                            } else {
                                "Tinjau izin OAuth untuk menautkan akun Google Anda ke penyimpanan lokal Arcanum Vault."
                            },
                            style = MaterialTheme.typography.bodySmall,
                            color = CosmicTextMuted
                        )
                    }
                }

                if (oauthStep == 1) {
                    // Tombol 1: Pemilih Akun Google Sistem Android Resmi (AccountManager)
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .duolingoButton(
                                faceColor = com.example.ui.theme.DuoMacawBlue,
                                lipColor = com.example.ui.theme.DuoMacawBlueLip,
                                cornerRadius = 16.dp,
                                lipHeight = 4.dp
                            )
                            .instantTouchPress {
                                try {
                                    @Suppress("DEPRECATION")
                                    val pickerIntent = android.accounts.AccountManager.newChooseAccountIntent(
                                        null,
                                        null,
                                        arrayOf("com.google"),
                                        false,
                                        null,
                                        null,
                                        null,
                                        null
                                    )
                                    systemAccountPickerLauncher.launch(pickerIntent)
                                } catch (_: Exception) {
                                    if (onLaunchNativeGoogleSignIn != null) {
                                        onLaunchNativeGoogleSignIn()
                                    } else {
                                        validationError = "Pemilih sistem tidak tersedia di emulator ini. Silakan masukkan akun Google Anda di bawah."
                                    }
                                }
                            }
                            .padding(bottom = 4.dp)
                            .testTag("google_system_account_picker_button")
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 14.dp, vertical = 11.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.AccountCircle,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(22.dp)
                            )
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "Pilih Akun Google Sistem Android",
                                    style = MaterialTheme.typography.labelLarge,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = Color.White
                                )
                                Text(
                                    text = "Deteksi otomatis akun Google yang login di HP Anda",
                                    style = MaterialTheme.typography.labelSmall,
                                    fontSize = 10.sp,
                                    color = Color.White.copy(alpha = 0.9f)
                                )
                            }
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }

                    // Tombol 2: Google Play Services OAuth Sign-In Client
                    OutlinedButton(
                        onClick = {
                            try {
                                val gso = com.google.android.gms.auth.api.signin.GoogleSignInOptions.Builder(
                                    com.google.android.gms.auth.api.signin.GoogleSignInOptions.DEFAULT_SIGN_IN
                                ).requestEmail().requestProfile().build()
                                val client = com.google.android.gms.auth.api.signin.GoogleSignIn.getClient(context, gso)
                                playServicesSignInLauncher.launch(client.signInIntent)
                            } catch (_: Exception) {
                                onLaunchNativeGoogleSignIn?.invoke()
                            }
                        },
                        shape = RoundedCornerShape(14.dp),
                        border = androidx.compose.foundation.BorderStroke(1.5.dp, com.example.ui.theme.DuoMacawBlue),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("google_play_services_oauth_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = null,
                            tint = com.example.ui.theme.DuoMacawBlueLip,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Lanjutkan dengan Google Play OAuth 2.0",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = com.example.ui.theme.DuoMacawBlueLip
                        )
                    }

                    // Daftar Akun Google Pengguna yang Pernah Ditautkan di Perangkat Ini (Jika Ada)
                    if (savedDeviceAccounts.isNotEmpty()) {
                        Text(
                            text = "AKUN GOOGLE TERSIMPAN DI PERANGKAT INI:",
                            fontFamily = JetBrainsMonoFontFamily,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = com.example.ui.theme.DuoFeatherGreenLip
                        )
                        savedDeviceAccounts.forEachIndexed { idx, (savedEmail, savedName) ->
                            val isSelected = emailInput.equals(savedEmail, ignoreCase = true)
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .duolingoCard(
                                        backgroundColor = if (isSelected) com.example.ui.theme.DuoFeatherGreenSoft else CosmicVoid,
                                        borderColor = if (isSelected) com.example.ui.theme.DuoFeatherGreen else com.example.ui.theme.DuoSwanBorder,
                                        lipColor = if (isSelected) com.example.ui.theme.DuoFeatherGreenLip else com.example.ui.theme.DuoHareLip,
                                        cornerRadius = 14.dp,
                                        borderWidth = 2.dp,
                                        lipHeight = 3.dp
                                    )
                                    .instantTouchPress {
                                        emailInput = savedEmail
                                        nameInput = savedName
                                        validationError = null
                                        oauthStep = 2
                                    }
                                    .padding(bottom = 3.dp)
                                    .testTag("google_saved_account_$idx")
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 12.dp, vertical = 8.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = savedName,
                                            style = MaterialTheme.typography.labelLarge,
                                            fontWeight = FontWeight.ExtraBold,
                                            color = CosmicTextPure
                                        )
                                        Text(
                                            text = savedEmail,
                                            fontFamily = JetBrainsMonoFontFamily,
                                            fontSize = 11.sp,
                                            color = CosmicTextMuted
                                        )
                                    }
                                    Surface(
                                        color = com.example.ui.theme.DuoFeatherGreen,
                                        shape = RoundedCornerShape(8.dp)
                                    ) {
                                        Text(
                                            text = "Pilih",
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.ExtraBold,
                                            color = Color.White,
                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }

                    // Input Akun Google Pribadi Pengguna (Untuk tambah akun baru / login manual OAuth)
                    Text(
                        text = "ATAU MASUKKAN AKUN GOOGLE PRIBADI ANDA:",
                        fontFamily = JetBrainsMonoFontFamily,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = CosmicTextMuted
                    )

                    OutlinedTextField(
                        value = emailInput,
                        onValueChange = {
                            emailInput = it
                            validationError = null
                        },
                        label = { Text("Email Akun Google Anda") },
                        placeholder = { Text("nama.anda@gmail.com") },
                        singleLine = true,
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("google_signin_email_input")
                    )

                    OutlinedTextField(
                        value = nameInput,
                        onValueChange = {
                            nameInput = it
                            validationError = null
                        },
                        label = { Text("Nama Tampilan Profil (Opsional)") },
                        placeholder = { Text("Otomatis dari profil Google jika kosong") },
                        singleLine = true,
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("google_signin_name_input")
                    )

                    if (validationError != null) {
                        Text(
                            text = validationError!!,
                            style = MaterialTheme.typography.bodySmall,
                            color = com.example.ui.theme.DuoCardinalRedLip,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        if (!isMandatoryGate) {
                            OutlinedButton(
                                onClick = onDismiss,
                                modifier = Modifier
                                    .weight(0.36f)
                                    .height(46.dp),
                                shape = RoundedCornerShape(14.dp)
                            ) {
                                Text("Batal", fontWeight = FontWeight.Bold, color = CosmicTextPure)
                            }
                        }

                        Box(
                            modifier = Modifier
                                .weight(if (isMandatoryGate) 1f else 0.64f)
                                .height(48.dp)
                                .duolingoButton(
                                    faceColor = com.example.ui.theme.DuoFeatherGreen,
                                    lipColor = com.example.ui.theme.DuoFeatherGreenLip,
                                    cornerRadius = 14.dp,
                                    lipHeight = 4.dp
                                )
                                .instantTouchPress {
                                    val cleanEmail = emailInput.trim()
                                    if (!cleanEmail.contains("@") || !cleanEmail.contains(".") || cleanEmail.length < 6) {
                                        validationError = "Pilih akun dari sistem Android atau masukkan email Google Anda yang valid!"
                                    } else {
                                        if (nameInput.isBlank()) {
                                            nameInput = cleanEmail
                                                .substringBefore("@")
                                                .replace(".", " ")
                                                .replace("_", " ")
                                                .replaceFirstChar { it.uppercase() }
                                        }
                                        validationError = null
                                        oauthStep = 2
                                    }
                                }
                                .padding(bottom = 4.dp)
                                .testTag("google_signin_next_oauth_button"),
                            contentAlignment = Alignment.Center
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Text(
                                    text = "Lanjut ke Otorisasi OAuth 2.0 →",
                                    style = MaterialTheme.typography.labelLarge,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = Color.White
                                )
                            }
                        }
                    }
                } else {
                    // STEP 2: Layar Persetujuan OAuth 2.0 Pihak Ketiga Resmi ("Tautkan Google ke Arcanum")
                    val resolvedName = nameInput.trim().ifBlank {
                        emailInput.trim().substringBefore("@").replaceFirstChar { it.uppercase() }
                    }
                    Surface(
                        color = com.example.ui.theme.DuoMacawBlueSoft,
                        shape = RoundedCornerShape(16.dp),
                        border = androidx.compose.foundation.BorderStroke(1.5.dp, com.example.ui.theme.DuoMacawBlue),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier.padding(14.dp),
                            verticalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Text(
                                text = "AKUN GOOGLE YANG AKAN DITAUTKAN:",
                                fontFamily = JetBrainsMonoFontFamily,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = com.example.ui.theme.DuoMacawBlueLip
                            )
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(38.dp)
                                        .clip(CircleShape)
                                        .background(com.example.ui.theme.DuoFeatherGreen),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = resolvedName.take(1).uppercase(),
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.ExtraBold,
                                        color = Color.White
                                    )
                                }
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = resolvedName,
                                        style = MaterialTheme.typography.titleSmall,
                                        fontWeight = FontWeight.ExtraBold,
                                        color = CosmicTextPure
                                    )
                                    Text(
                                        text = emailInput.trim().lowercase(),
                                        fontFamily = JetBrainsMonoFontFamily,
                                        fontSize = 11.sp,
                                        color = CosmicTextMuted
                                    )
                                }
                            }
                        }
                    }

                    // Daftar Scope OAuth 2.0 Pihak Ketiga + Penyimpanan Lokal
                    Surface(
                        color = CosmicVoid,
                        shape = RoundedCornerShape(16.dp),
                        border = androidx.compose.foundation.BorderStroke(1.5.dp, com.example.ui.theme.DuoSwanBorder),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier.padding(12.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = "Arcanum Vault meminta izin akses pihak ketiga:",
                                style = MaterialTheme.typography.labelLarge,
                                fontWeight = FontWeight.ExtraBold,
                                color = CosmicTextPure
                            )
                            Text(
                                text = "• Profil & Alamat Email Google (openid, email, profile)",
                                style = MaterialTheme.typography.bodySmall,
                                color = CosmicTextPure,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "• Pengikatan Brankas Lokal Per-Akun: Semua file, video, dan sandi disimpan 100% lokal di HP masing-masing pengguna.",
                                style = MaterialTheme.typography.bodySmall,
                                color = com.example.ui.theme.DuoFeatherGreenLip,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "• Identitas Terverifikasi untuk Report Center & Pencadangan Brankas Pribadi.",
                                style = MaterialTheme.typography.bodySmall,
                                color = CosmicTextMuted
                            )
                        }
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        OutlinedButton(
                            onClick = { oauthStep = 1 },
                            modifier = Modifier
                                .weight(0.38f)
                                .height(48.dp),
                            shape = RoundedCornerShape(14.dp)
                        ) {
                            Text("Ganti Akun", fontWeight = FontWeight.Bold, color = CosmicTextPure)
                        }

                        Box(
                            modifier = Modifier
                                .weight(0.62f)
                                .height(48.dp)
                                .duolingoButton(
                                    faceColor = com.example.ui.theme.DuoFeatherGreen,
                                    lipColor = com.example.ui.theme.DuoFeatherGreenLip,
                                    cornerRadius = 14.dp,
                                    lipHeight = 4.dp
                                )
                                .instantTouchPress {
                                    saveAndCompleteOAuth(emailInput, resolvedName)
                                }
                                .padding(bottom = 4.dp)
                                .testTag("google_signin_confirm_button"),
                            contentAlignment = Alignment.Center
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.CheckCircle,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(18.dp)
                                )
                                Text(
                                    text = "Izinkan & Tautkan ke Arcanum",
                                    style = MaterialTheme.typography.labelLarge,
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

/**
 * Gerbang Wajib Login Akun Google Pihak Ketiga (Mandatory Google OAuth Gate):
 * Setiap pengguna yang mengunduh aplikasi wajib menautkan akun Google milik mereka sendiri
 * (data tersimpan secara lokal di perangkat masing-masing pengguna).
 */
@Composable
fun MandatoryGoogleVaultGateScreen(
    onGoogleSignedIn: (email: String, displayName: String) -> Unit
) {
    var showSignInDialog by remember { mutableStateOf(false) }

    if (showSignInDialog) {
        ArcanumGoogleSignInDialog(
            currentEmail = "",
            currentName = "",
            isMandatoryGate = true,
            onDismiss = { showSignInDialog = false },
            onSignedIn = { email, name ->
                showSignInDialog = false
                onGoogleSignedIn(email, name)
            }
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(CosmicVoid)
            .windowInsetsPadding(WindowInsets.safeDrawing)
            .testTag("mandatory_google_vault_gate_screen"),
        contentAlignment = Alignment.Center
    ) {
        DuolingoGalaxyAmbientBackdrop()

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .widthIn(max = 460.dp)
                .padding(22.dp)
                .duolingoCard(
                    backgroundColor = com.example.ui.theme.DuoPureWhite,
                    borderColor = com.example.ui.theme.DuoMacawBlue,
                    lipColor = com.example.ui.theme.DuoMacawBlueLip,
                    cornerRadius = 28.dp,
                    borderWidth = 2.5.dp,
                    lipHeight = 6.dp
                )
                .padding(bottom = 6.dp)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            CosmicDragonAvatar(
                mood = MascotMood.PIN_SHIELD,
                size = 92.dp,
                persona = MascotPersona.KAWAII_FOLDER
            )

            Surface(
                color = com.example.ui.theme.DuoMacawBlueSoft,
                shape = RoundedCornerShape(50),
                border = androidx.compose.foundation.BorderStroke(1.5.dp, com.example.ui.theme.DuoMacawBlue)
            ) {
                Text(
                    text = " LOGIN PIHAK KETIGA GOOGLE • DATA LOKAL PER-USER ",
                    fontFamily = JetBrainsMonoFontFamily,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = com.example.ui.theme.DuoMacawBlueLip,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                )
            }

            Text(
                text = "Tautkan Akun Google Anda ke Arcanum",
                style = MaterialTheme.typography.headlineSmall,
                color = CosmicTextPure,
                fontWeight = FontWeight.ExtraBold,
                textAlign = TextAlign.Center
            )

            Text(
                text = "Gunakan akun Google milik Anda sendiri melalui Pemilih Akun Sistem Android & OAuth 2.0. Seluruh file dan brankas disimpan 100% secara lokal di perangkat Anda.",
                style = MaterialTheme.typography.bodyMedium,
                color = CosmicTextMuted,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(4.dp))

            // Tombol Login Pihak Ketiga Google
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp)
                    .duolingoButton(
                        faceColor = com.example.ui.theme.DuoFeatherGreen,
                        lipColor = com.example.ui.theme.DuoFeatherGreenLip,
                        cornerRadius = 18.dp,
                        lipHeight = 5.dp
                    )
                    .instantTouchPress { showSignInDialog = true }
                    .padding(bottom = 5.dp)
                    .testTag("mandatory_google_gate_login_button"),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.AccountCircle,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(22.dp)
                    )
                    Text(
                        text = "Tautkan Akun Google Sekarang",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White
                    )
                }
            }
        }
    }
}



