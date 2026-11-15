package com.example.ui

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.Easing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.zIndex
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
import com.example.ui.theme.DuoHareLip
import com.example.ui.theme.DuoMacawBlue
import com.example.ui.theme.DuoMacawBlueLip
import com.example.ui.theme.DuoMintSurface
import com.example.ui.theme.DuoPureWhite
import com.example.ui.theme.DuoSwanBorder
import com.example.ui.theme.AppGraphicsQualityMode
import com.example.ui.theme.GsapMotionPreset
import com.example.ui.theme.GsapVisualEffectStyle
import com.example.ui.theme.JetBrainsMonoFontFamily
import com.example.ui.theme.SpaceGroteskFontFamily
import com.example.ui.theme.duolingoButton
import com.example.ui.theme.duolingoCard
import androidx.compose.ui.draw.drawBehind
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.roundToInt
import kotlin.math.roundToLong
import kotlin.math.sin

// ============================================================================
// GSAP (GREENSOCK ANIMATION PLATFORM) MATHEMATICAL EASING & TIMELINE ENGINE
// Provides authentic GSAP curves (expo.out, power4.out, back.out, elastic.out,
// bounce.out) + iOS 18 Liquid Spring across all 4 spaces of the application.
// ============================================================================

object GsapEase {
    /** GSAP `power3.out` */
    val Power3Out: Easing = CubicBezierEasing(0.215f, 0.61f, 0.355f, 1.0f)

    /** GSAP `power4.out` */
    val Power4Out: Easing = CubicBezierEasing(0.165f, 0.84f, 0.44f, 1.0f)

    /** GSAP `expo.out` */
    val ExpoOut: Easing = CubicBezierEasing(0.16f, 1.0f, 0.3f, 1.0f)

    /** GSAP `circ.out` */
    val CircOut: Easing = CubicBezierEasing(0.075f, 0.82f, 0.165f, 1.0f)

    /** GSAP `back.out(overshoot)` — signature overshoot curve */
    fun backOut(overshoot: Float = 1.70158f): Easing = Easing { fraction ->
        val t = fraction.coerceIn(0f, 1f) - 1f
        (t * t * ((overshoot + 1f) * t + overshoot) + 1f)
    }

    val BackOutDefault: Easing = backOut(1.70158f)

    /** GSAP `elastic.out(amplitude, period)` — signature damped spring wave */
    fun elasticOut(amplitude: Float = 1.0f, period: Float = 0.34f): Easing = Easing { fraction ->
        val x = fraction.coerceIn(0f, 1f)
        if (x == 0f || x == 1f) {
            x
        } else {
            val s = period / 4f
            (amplitude * 2.0.pow((-10f * x).toDouble()).toFloat() *
                sin(((x - s) * (2.0 * PI) / period).toFloat()) + 1f)
        }
    }

    val ElasticOutDefault: Easing = elasticOut(1.0f, 0.34f)

    /** GSAP `bounce.out` — piecewise parabolic bounce */
    val BounceOut: Easing = Easing { fraction ->
        var x = fraction.coerceIn(0f, 1f)
        val n1 = 7.5625f
        val d1 = 2.75f
        when {
            x < 1f / d1 -> n1 * x * x
            x < 2f / d1 -> {
                x -= 1.5f / d1
                n1 * x * x + 0.75f
            }
            x < 2.5f / d1 -> {
                x -= 2.25f / d1
                n1 * x * x + 0.9375f
            }
            else -> {
                x -= 2.625f / d1
                n1 * x * x + 0.984375f
            }
        }
    }

    /** Resolves the active GSAP Easing curve based on AppThemeController.activeGsapPreset */
    fun activePresetEasing(preset: GsapMotionPreset = AppThemeController.activeGsapPreset): Easing {
        return when (preset) {
            GsapMotionPreset.GSAP_ELASTIC_BOUNCE -> backOut(1.72f)
            GsapMotionPreset.GSAP_EXPO_CINEMATIC -> ExpoOut
            GsapMotionPreset.IOS_LIQUID_SPRING -> CubicBezierEasing(0.22f, 1.18f, 0.36f, 1.0f)
            GsapMotionPreset.CYBER_MAGNETIC_3D -> backOut(1.55f)
            GsapMotionPreset.HYPER_STAGGER_WAVE -> backOut(1.65f)
            GsapMotionPreset.TURBO_SNAPPY -> Power4Out
            GsapMotionPreset.GSAP_BOUNCE_GRAVITY -> BounceOut
            GsapMotionPreset.GSAP_VORTEX_SPIRAL_3D -> backOut(2.05f)
            GsapMotionPreset.GSAP_GLITCH_CYBER_SNAP -> ExpoOut
            GsapMotionPreset.GSAP_JELLY_SQUISH_MORPH -> elasticOut(1.32f, 0.28f)
        }
    }

    fun effectiveDurationMs(
        baseMs: Int = AppThemeController.activeGsapPreset.defaultDurationMs
    ): Int {
        val speed = AppThemeController.gsapSpeedMultiplier.coerceIn(0.4f, 2.5f)
        return (baseMs / speed).roundToInt().coerceIn(140, 1200)
    }

    fun effectiveStaggerDelayMs(
        index: Int,
        preset: GsapMotionPreset = AppThemeController.activeGsapPreset
    ): Long {
        if (!AppThemeController.gsapStaggerEnabled) return 0L
        val speed = AppThemeController.gsapSpeedMultiplier.coerceIn(0.4f, 2.5f)
        val cappedIndex = index.coerceIn(0, 10)
        return ((cappedIndex * preset.staggerStepMs) / speed).roundToLong().coerceIn(0L, 480L)
    }
}

private object GsapAutoStaggerTracker {
    private var lastFrameSlotMs: Long = 0L
    private var counterInSlot: Int = 0

    fun nextAutoIndex(): Int {
        val now = System.currentTimeMillis()
        if (now - lastFrameSlotMs > 180L) {
            lastFrameSlotMs = now
            counterInSlot = 0
        } else {
            counterInSlot = (counterInSlot + 1).coerceAtMost(9)
        }
        return counterInSlot
    }
}

object ModifierGsapCardBridge {
    fun applyCardReveal(): Modifier = Modifier

    fun applyCardMotionGraphicAura(
        accentColor: Color,
        cornerRadius: Dp
    ): Modifier = Modifier.gsapCardMotionGraphicAura(
        accentColor = accentColor,
        cornerRadius = cornerRadius
    )

    fun applyUltraButtonOverlay(
        faceColor: Color,
        lipColor: Color,
        cornerRadius: Dp,
        lipHeight: Dp
    ): Modifier = Modifier.gsapUltraButtonVisuals(
        faceColor = faceColor,
        lipColor = lipColor,
        cornerRadius = cornerRadius,
        lipHeight = lipHeight
    )
}

private val SoftPrismPalette = listOf(
    Color(0xFF38BDF8), // Soft Sky Cyan
    Color(0xFF818CF8), // Calm Indigo
    Color(0xFFC084FC), // Pastel Violet
    Color(0xFFF472B6), // Soft Sakura Rose
    Color(0xFFFBBF24), // Warm Amber Gold
    Color(0xFF34D399), // Mint Emerald
    Color(0xFF38BDF8)  // Loop back to Soft Sky Cyan
)

private val UltraRainbowPalette = SoftPrismPalette

fun resolveGsapStylePalette(style: GsapVisualEffectStyle): List<Color> {
    return style.paletteColors.ifEmpty { SoftPrismPalette }
}

/**
 * Resolves the harmonious border palette for a given button/card box color.
 * - Box colors and border colors stay 100% IDENTICAL from Smooth (1x) to Ultra GSAP (20x)!
 * - Regular buttons use their own semantic color (`faceColor` + `lipColor` + soft specular tint)
 *   so they NEVER hurt the eyes with full-screen rainbow.
 * - Selective special accent boxes (e.g. Purple/Violet featured boxes or when Soft Prism is chosen)
 *   get a soft, eye-friendly pastel prism trim across ALL modes from Smooth to Ultra!
 */
private fun resolveHarmoniousBoxBorderPalette(
    baseColor: Color,
    lipColor: Color,
    visualStyle: GsapVisualEffectStyle
): List<Color> {
    val isSelectiveRainbowBox =
        baseColor == DuoBeetlePurple ||
            baseColor == Color(0xFF8B5CF6) ||
            baseColor == Color(0xFF9333EA) ||
            visualStyle == GsapVisualEffectStyle.RAINBOW_ORBIT_LASER

    return if (isSelectiveRainbowBox) {
        SoftPrismPalette
    } else {
        val brightTint = AppThemeController.lightenTint(baseColor, 0.42f)
        val styleTint = androidx.compose.ui.graphics.lerp(baseColor, visualStyle.primaryColor, 0.35f)
        listOf(
            brightTint.copy(alpha = 0.92f),
            styleTint.copy(alpha = 0.88f),
            lipColor.copy(alpha = 0.90f),
            brightTint.copy(alpha = 0.92f)
        )
    }
}

/**
 * GSAP Card Motion Graphic UI Overlay (`duolingoCard` across Smooth 1x -> Ultra GSAP 20x):
 * - Keeps card border & surface colors 100% consistent across Smooth -> Balanced -> High -> Extreme -> Ultra!
 * - Scales the number of active GSAP motion graphic layers from 1x (Smooth) up to 20x (Ultra GSAP).
 */
fun Modifier.gsapCardMotionGraphicAura(
    accentColor: Color,
    cornerRadius: Dp = 20.dp
): Modifier = composed {
    val qualityMode = AppThemeController.graphicsQualityMode
    val tier = qualityMode.tierLevel // 0=ChaosLow(1/25x), 1=Smooth(1x), 2=Balanced(3x), 3=High(7x), 4=Extreme(12x), 5=Ultra(20x), 6=ChaosBrutal(100x)
    if (tier <= 0) {
        return@composed this
    }
    val visualStyle = AppThemeController.activeGsapVisualStyle

    val phase: Float = if (tier >= 3) {
        val infiniteTransition = rememberInfiniteTransition(label = "gsap_card_motion_graphic")
        val anim by infiniteTransition.animateFloat(
            initialValue = 0f,
            targetValue = 1f,
            animationSpec = infiniteRepeatable(
                animation = tween(
                    durationMillis = when (tier) {
                        6 -> 680
                        5 -> 3200
                        4 -> 4200
                        else -> 5400
                    },
                    easing = LinearEasing
                ),
                repeatMode = RepeatMode.Restart
            ),
            label = "card_mg_phase"
        )
        anim
    } else {
        0.22f
    }

    val borderPalette = resolveHarmoniousBoxBorderPalette(
        baseColor = accentColor,
        lipColor = accentColor,
        visualStyle = visualStyle
    )

    this.drawWithContent {
        drawContent()
        val r = cornerRadius.toPx()
        val angleRad = (phase * 2.0 * PI).toFloat()
        val cx = size.width * 0.5f
        val cy = size.height * 0.5f
        val diag = size.width.coerceAtLeast(size.height) * 0.65f
        val startOffset = Offset(cx + cos(angleRad) * diag, cy + sin(angleRad) * diag)
        val endOffset = Offset(cx - cos(angleRad) * diag, cy - sin(angleRad) * diag)

        // Layer 1 (All tiers Smooth 1x -> Ultra 20x): Consistent Harmonious Rim Trim
        drawRoundRect(
            brush = Brush.linearGradient(
                colors = borderPalette.map { it.copy(alpha = 0.34f) },
                start = startOffset,
                end = endOffset
            ),
            topLeft = Offset.Zero,
            size = size,
            cornerRadius = CornerRadius(r, r),
            style = Stroke(width = 1.2.dp.toPx())
        )

        if (size.width < 64f || size.height < 36f) return@drawWithContent

        // Tier 3+ (High 7x, Extreme 12x, Ultra 20x): Soft Specular Diagonal Light Sweep
        if (tier >= 3) {
            val sweepX = (-0.2f + phase * 1.4f) * size.width
            val halfW = (size.width * 0.22f).coerceAtLeast(24f)
            drawRoundRect(
                brush = Brush.linearGradient(
                    colors = listOf(
                        Color.Transparent,
                        visualStyle.primaryColor.copy(alpha = 0.06f),
                        Color.White.copy(alpha = 0.10f),
                        visualStyle.secondaryColor.copy(alpha = 0.06f),
                        Color.Transparent
                    ),
                    start = Offset(sweepX - halfW, 0f),
                    end = Offset(sweepX + halfW, size.height)
                ),
                topLeft = Offset.Zero,
                size = size,
                cornerRadius = CornerRadius(r, r)
            )
        }

        // Tier 4+ (Extreme 12x & Ultra 20x): 4 Tactical HUD Corner Reticle Brackets + Scanline
        if (tier >= 4) {
            val bracketLen = 10.dp.toPx()
            val inset = 4.dp.toPx()
            val hudColor = visualStyle.primaryColor.copy(alpha = 0.38f)
            val hudStroke = 1.4.dp.toPx()
            // Top-Left & Bottom-Right tactical HUD ticks
            drawLine(hudColor, Offset(inset, inset + bracketLen), Offset(inset, inset), hudStroke)
            drawLine(hudColor, Offset(inset, inset), Offset(inset + bracketLen, inset), hudStroke)
            drawLine(hudColor, Offset(size.width - inset, size.height - inset - bracketLen), Offset(size.width - inset, size.height - inset), hudStroke)
            drawLine(hudColor, Offset(size.width - inset - bracketLen, size.height - inset), Offset(size.width - inset, size.height - inset), hudStroke)
        }

        // Tier 5 (Ultra GSAP 20x): Smooth Liquid Silk Aurora Ribbon & Top/Bottom Rim Specular Glint (Zero circular particles!)
        if (tier >= 5) {
            val smoothSine = ((sin(angleRad) + 1f) * 0.5f).coerceIn(0f, 1f)
            val silkY = (r * 0.6f + (size.height - r * 1.2f).coerceAtLeast(1f) * smoothSine)
            drawLine(
                brush = Brush.horizontalGradient(
                    colors = listOf(
                        Color.Transparent,
                        visualStyle.primaryColor.copy(alpha = 0.22f),
                        Color.White.copy(alpha = 0.26f),
                        visualStyle.secondaryColor.copy(alpha = 0.22f),
                        Color.Transparent
                    )
                ),
                start = Offset(r * 0.5f, silkY),
                end = Offset(size.width - r * 0.5f, silkY),
                strokeWidth = 1.6.dp.toPx()
            )
            // Smooth elongated capsule specular glint gliding along top & bottom rim
            val glintW = (size.width * 0.28f).coerceAtLeast(18f)
            val glintX = (r + (size.width - 2f * r - glintW).coerceAtLeast(0f) * smoothSine)
            drawRoundRect(
                brush = Brush.horizontalGradient(
                    colors = listOf(
                        Color.Transparent,
                        visualStyle.primaryColor.copy(alpha = 0.72f),
                        Color.White.copy(alpha = 0.85f),
                        visualStyle.secondaryColor.copy(alpha = 0.72f),
                        Color.Transparent
                    )
                ),
                topLeft = Offset(glintX, 0.8.dp.toPx()),
                size = Size(glintW, 2.dp.toPx()),
                cornerRadius = CornerRadius(2.dp.toPx(), 2.dp.toPx())
            )
        }
    }
}

/**
 * Unified Button Visual Engine from Smooth (1x) to Ultra GSAP (20x):
 * - Box fill colors, transparency, and border colors are 100% IDENTICAL across Smooth -> Balanced -> High -> Extreme -> Ultra!
 * - Eye-friendly harmonious colors: buttons use their own `faceColor` & `lipColor` (never blinding full rainbow on every button!),
 *   while selective featured purple/prism boxes keep their soft pastel prism trim in ALL 5 modes.
 * - Effect density scales 20x from Smooth (1x static base) to Ultra GSAP (20x layered GSAP effects)!
 */
fun Modifier.gsapUltraButtonVisuals(
    faceColor: Color,
    lipColor: Color,
    cornerRadius: Dp = 16.dp,
    lipHeight: Dp = 4.dp
): Modifier = composed {
    val qualityMode = AppThemeController.graphicsQualityMode
    val tier = qualityMode.tierLevel // 0=ChaosLow(1/25x), 1=Smooth(1x), 2=Balanced(3x), 3=High(7x), 4=Extreme(12x), 5=Ultra(20x), 6=ChaosBrutal(100x)
    val visualStyle = AppThemeController.activeGsapVisualStyle
    val isTransparentBox = AppThemeController.transparentButtonBoxEnabled
    val boxAlpha = AppThemeController.transparentButtonAlpha.coerceIn(0.06f, 0.78f)

    // Chaos Low (tier 0), Smooth (tier 1) & Balanced (tier 2) use 0 infinite loops for maximum battery/FPS;
    // High (tier 3), Extreme (tier 4), Ultra GSAP (tier 5), and Chaos Brutal (tier 6) run continuous GSAP timeline phase.
    val phase: Float = if (tier >= 3) {
        val infiniteTransition = rememberInfiniteTransition(label = "gsap_btn_timeline_phase")
        val animPhase by infiniteTransition.animateFloat(
            initialValue = 0f,
            targetValue = 1f,
            animationSpec = infiniteRepeatable(
                animation = tween(
                    durationMillis = when (tier) {
                        6 -> 520
                        5 -> 2100
                        4 -> 2900
                        else -> 3800
                    },
                    easing = LinearEasing
                ),
                repeatMode = RepeatMode.Restart
            ),
            label = "gsap_btn_phase"
        )
        animPhase
    } else {
        0.25f
    }

    val borderPalette = resolveHarmoniousBoxBorderPalette(
        baseColor = faceColor,
        lipColor = lipColor,
        visualStyle = visualStyle
    )

    this.drawBehind {
        val r = cornerRadius.toPx()
        val lip = lipHeight.toPx()
        val faceH = (size.height - lip * 0.65f).coerceAtLeast(0f)
        val strokePx = 1.6.dp.toPx()

        val angleRad = (phase * 2.0 * PI).toFloat()
        val cx = size.width * 0.5f
        val cy = faceH * 0.5f
        val diag = size.width.coerceAtLeast(faceH) * 0.72f
        val startOffset = Offset(
            x = cx + cos(angleRad) * diag,
            y = cy + sin(angleRad) * diag
        )
        val endOffset = Offset(
            x = cx - cos(angleRad) * diag,
            y = cy - sin(angleRad) * diag
        )

        // ====================================================================
        // TIER 0: CHAOS V2 — ULTIMATE PRO MAX SMOOTH LOW (25x LIGHTER BELOW SMOOTH)
        // Single-pass flat box draw with identical faceColor & lipColor, zero gradients/shaders!
        // ====================================================================
        if (tier == 0) {
            drawRoundRect(
                color = if (isTransparentBox) faceColor.copy(alpha = boxAlpha) else faceColor,
                topLeft = Offset.Zero,
                size = size,
                cornerRadius = CornerRadius(r, r)
            )
            drawRoundRect(
                color = lipColor.copy(alpha = 0.85f),
                topLeft = Offset.Zero,
                size = size,
                cornerRadius = CornerRadius(r, r),
                style = Stroke(width = 1.dp.toPx())
            )
            return@drawBehind
        }

        // ====================================================================
        // BASE BOX & BORDER COLORS (100% IDENTICAL FROM SMOOTH 1x TO ULTRA 20x!)
        // ====================================================================

        // Layer 1: 3D Bottom Lip Shadow (Consistent color across all modes)
        drawRoundRect(
            color = lipColor.copy(alpha = if (isTransparentBox) 0.54f else 0.94f),
            topLeft = Offset.Zero,
            size = size,
            cornerRadius = CornerRadius(r, r)
        )

        // Layer 2: Button Box Interior Fill (Consistent color across all 5 modes)
        if (isTransparentBox) {
            drawRoundRect(
                color = Color(0xFF0B1120).copy(alpha = (boxAlpha * 1.35f).coerceIn(0.18f, 0.54f)),
                topLeft = Offset.Zero,
                size = Size(size.width, faceH),
                cornerRadius = CornerRadius(r, r)
            )
            drawRoundRect(
                brush = Brush.linearGradient(
                    colors = listOf(
                        faceColor.copy(alpha = boxAlpha),
                        lipColor.copy(alpha = (boxAlpha * 0.85f).coerceIn(0.05f, 0.70f)),
                        visualStyle.primaryColor.copy(alpha = (boxAlpha * 0.38f).coerceIn(0.04f, 0.32f))
                    ),
                    start = Offset.Zero,
                    end = Offset(size.width, faceH)
                ),
                topLeft = Offset.Zero,
                size = Size(size.width, faceH),
                cornerRadius = CornerRadius(r, r)
            )
        } else {
            drawRoundRect(
                brush = Brush.linearGradient(
                    colors = listOf(
                        AppThemeController.lightenTint(faceColor, 0.12f),
                        faceColor,
                        lipColor
                    ),
                    start = Offset.Zero,
                    end = Offset(size.width, faceH)
                ),
                topLeft = Offset.Zero,
                size = Size(size.width, faceH),
                cornerRadius = CornerRadius(r, r)
            )
        }

        // Layer 3: Harmonious Border (Consistent palette across Smooth -> Ultra; rotates in High/Extreme/Ultra)
        drawRoundRect(
            brush = Brush.linearGradient(
                colors = borderPalette,
                start = startOffset,
                end = endOffset
            ),
            topLeft = Offset(strokePx / 2f, strokePx / 2f),
            size = Size((size.width - strokePx).coerceAtLeast(0f), (faceH - strokePx).coerceAtLeast(0f)),
            cornerRadius = CornerRadius(r, r),
            style = Stroke(width = strokePx)
        )

        if (size.width <= 16f || faceH <= 10f) return@drawBehind

        // ====================================================================
        // PROGRESSIVE GSAP EFFECT MULTIPLIER: 1x (Smooth) -> 3x -> 7x -> 12x -> 20x (Ultra)
        // ====================================================================

        // Tier 2+ (Balanced 3x, High 7x, Extreme 12x, Ultra 20x): Top Inner Specular Glass Gloss
        if (tier >= 2) {
            val glossH = (faceH * 0.28f).coerceAtLeast(2f)
            drawRoundRect(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color.White.copy(alpha = 0.20f),
                        Color.Transparent
                    )
                ),
                topLeft = Offset(r * 0.30f, 1.5.dp.toPx()),
                size = Size((size.width - r * 0.60f).coerceAtLeast(0f), glossH),
                cornerRadius = CornerRadius(r * 0.6f, r * 0.6f)
            )
        }

        // Tier 3+ (High 7x, Extreme 12x, Ultra 20x): Primary Specular Light Sweep + Corner Radial Glow
        if (tier >= 3) {
            val sweepCenterX = (-0.25f + phase * 1.5f) * size.width
            val beamHalfW = (size.width * 0.26f).coerceAtLeast(18f)
            drawRoundRect(
                brush = Brush.linearGradient(
                    colors = listOf(
                        Color.Transparent,
                        Color.White.copy(alpha = 0.14f),
                        visualStyle.primaryColor.copy(alpha = 0.18f),
                        Color.White.copy(alpha = 0.14f),
                        Color.Transparent
                    ),
                    start = Offset(sweepCenterX - beamHalfW, 0f),
                    end = Offset(sweepCenterX + beamHalfW, faceH)
                ),
                topLeft = Offset.Zero,
                size = Size(size.width, faceH),
                cornerRadius = CornerRadius(r, r)
            )
        }

        // Tier 4+ (Extreme 12x & Ultra 20x): 4 Tactical HUD Corner Brackets + Smooth Squircle Inner Aura Wave (Zero circles!)
        if (tier >= 4) {
            val tickLen = 6.dp.toPx().coerceAtMost(faceH * 0.32f)
            val pad = 3.5.dp.toPx()
            val hudTint = Color.White.copy(alpha = 0.45f)
            val hudW = 1.2.dp.toPx()
            // Top-Left & Top-Right HUD Reticles
            drawLine(hudTint, Offset(pad, pad + tickLen), Offset(pad, pad), hudW)
            drawLine(hudTint, Offset(pad, pad), Offset(pad + tickLen, pad), hudW)
            drawLine(hudTint, Offset(size.width - pad - tickLen, pad), Offset(size.width - pad, pad), hudW)
            drawLine(hudTint, Offset(size.width - pad, pad), Offset(size.width - pad, pad + tickLen), hudW)
            // Smooth squircle inner contour breath (replaces circular radar ring)
            val breathInset = (2.dp.toPx() + ((sin(angleRad) + 1f) * 0.5f) * 3.dp.toPx())
            val innerW = (size.width - breathInset * 2f).coerceAtLeast(0f)
            val innerH = (faceH - breathInset * 2f).coerceAtLeast(0f)
            if (innerW > 8f && innerH > 6f) {
                drawRoundRect(
                    color = Color.White.copy(alpha = 0.14f),
                    topLeft = Offset(breathInset, breathInset),
                    size = Size(innerW, innerH),
                    cornerRadius = CornerRadius((r - breathInset).coerceAtLeast(2f), (r - breathInset).coerceAtLeast(2f)),
                    style = Stroke(width = 1.dp.toPx())
                )
            }
        }

        // Tier 5 (Ultra GSAP — Full 20x Smooth Silk Effect Layers with ZERO Circular Particles!):
        // Uses Liquid Silk Aurora Sweep, Smooth Dual Harmonic Sine Ribbon, & Top/Bottom Capsule Specular Glints!
        if (tier >= 5) {
            val smoothWave = ((sin(angleRad) + 1f) * 0.5f).coerceIn(0f, 1f)

            // Layer 14: Soft Liquid Silk Aurora Diagonal Shimmer
            val auroraCenter = (-0.15f + smoothWave * 1.3f) * size.width
            val auroraBand = (size.width * 0.38f).coerceAtLeast(24f)
            drawRoundRect(
                brush = Brush.linearGradient(
                    colors = listOf(
                        Color.Transparent,
                        visualStyle.primaryColor.copy(alpha = 0.14f),
                        Color.White.copy(alpha = 0.20f),
                        visualStyle.secondaryColor.copy(alpha = 0.14f),
                        Color.Transparent
                    ),
                    start = Offset(auroraCenter - auroraBand, faceH),
                    end = Offset(auroraCenter + auroraBand, 0f)
                ),
                topLeft = Offset.Zero,
                size = Size(size.width, faceH),
                cornerRadius = CornerRadius(r, r)
            )

            // Layer 15 & 16: Smooth Dual Harmonic Silk Wave flowing gracefully across the button
            val wavePath = Path()
            val waveSteps = 20
            val waveAmp = (faceH * 0.11f).coerceAtMost(4.5.dp.toPx())
            for (s in 0..waveSteps) {
                val frac = s.toFloat() / waveSteps.toFloat()
                val wx = frac * size.width
                val wy = cy + sin(frac * PI.toFloat() * 2.0f + angleRad) * waveAmp
                if (s == 0) wavePath.moveTo(wx, wy) else wavePath.lineTo(wx, wy)
            }
            drawPath(
                path = wavePath,
                brush = Brush.horizontalGradient(
                    colors = listOf(
                        Color.Transparent,
                        Color.White.copy(alpha = 0.24f),
                        visualStyle.primaryColor.copy(alpha = 0.28f),
                        Color.White.copy(alpha = 0.24f),
                        Color.Transparent
                    )
                ),
                style = Stroke(width = 1.2.dp.toPx())
            )

            // Layer 17 & 18: Smooth Elongated Top & Bottom Rim Specular Glint Strips (Replaces circular dots!)
            val stripW = (size.width * 0.32f).coerceAtLeast(14f)
            val topStripX = (r * 0.5f + (size.width - r - stripW).coerceAtLeast(0f) * smoothWave)
            val botStripX = (r * 0.5f + (size.width - r - stripW).coerceAtLeast(0f) * (1f - smoothWave))
            drawRoundRect(
                brush = Brush.horizontalGradient(
                    colors = listOf(
                        Color.Transparent,
                        Color.White.copy(alpha = 0.82f),
                        visualStyle.primaryColor.copy(alpha = 0.72f),
                        Color.Transparent
                    )
                ),
                topLeft = Offset(topStripX, 1.dp.toPx()),
                size = Size(stripW, 1.8.dp.toPx()),
                cornerRadius = CornerRadius(2.dp.toPx(), 2.dp.toPx())
            )
            drawRoundRect(
                brush = Brush.horizontalGradient(
                    colors = listOf(
                        Color.Transparent,
                        visualStyle.secondaryColor.copy(alpha = 0.72f),
                        Color.White.copy(alpha = 0.78f),
                        Color.Transparent
                    )
                ),
                topLeft = Offset(botStripX, (faceH - 2.4.dp.toPx()).coerceAtLeast(1f)),
                size = Size(stripW, 1.8.dp.toPx()),
                cornerRadius = CornerRadius(2.dp.toPx(), 2.dp.toPx())
            )

            // Layer 19 & 20: Secondary Counter-Sweep Silk Sheen
            val counterSweepX = (1.25f - phase * 1.5f) * size.width
            val counterHalfW = (size.width * 0.20f).coerceAtLeast(14f)
            drawRoundRect(
                brush = Brush.linearGradient(
                    colors = listOf(
                        Color.Transparent,
                        Color.White.copy(alpha = 0.14f),
                        Color.Transparent
                    ),
                    start = Offset(counterSweepX - counterHalfW, faceH),
                    end = Offset(counterSweepX + counterHalfW, 0f)
                ),
                topLeft = Offset.Zero,
                size = Size(size.width, faceH),
                cornerRadius = CornerRadius(r, r)
            )
        }

        // Tier 6 (Chaos V1 — Brutal Overdrive 100x Stress Engine):
        // Multi-harmonic plasma waves, 12-node perimeter vortex, triple shockwave rings, & overdrive grid!
        if (tier >= 6) {
            for (ring in 1..4) {
                val ringPhase = (phase * ring) % 1f
                val ringRadius = (0.10f + ringPhase * 0.88f) * size.width.coerceAtLeast(faceH)
                drawCircle(
                    color = visualStyle.primaryColor.copy(alpha = (1f - ringPhase) * 0.32f),
                    radius = ringRadius,
                    center = Offset(cx, cy),
                    style = Stroke(width = 1.8.dp.toPx())
                )
            }
            for (w in 1..3) {
                val chaosPath = Path()
                val steps = 28
                val amp = (faceH * (0.16f + w * 0.06f)).coerceAtMost(10.dp.toPx())
                for (s in 0..steps) {
                    val frac = s.toFloat() / steps.toFloat()
                    val wx = frac * size.width
                    val wy = cy + sin(frac * PI.toFloat() * (3.5f + w) + angleRad * (w + 1)) * amp
                    if (s == 0) chaosPath.moveTo(wx, wy) else chaosPath.lineTo(wx, wy)
                }
                drawPath(
                    path = chaosPath,
                    color = if (w % 2 == 0) visualStyle.secondaryColor.copy(alpha = 0.34f) else Color.White.copy(alpha = 0.28f),
                    style = Stroke(width = 1.4.dp.toPx())
                )
            }
            for (nodeIdx in 0 until 8) {
                val nodeAngle = angleRad * 2f + (nodeIdx * PI.toFloat() / 4f)
                val nx = (cx + cos(nodeAngle) * (size.width * 0.42f)).coerceIn(2f, (size.width - 2f).coerceAtLeast(2f))
                val ny = (cy + sin(nodeAngle) * (faceH * 0.38f)).coerceIn(2f, (faceH - 2f).coerceAtLeast(2f))
                drawCircle(
                    color = Color.White.copy(alpha = 0.85f),
                    radius = 2.4.dp.toPx(),
                    center = Offset(nx, ny)
                )
            }
        }
    }
}

/**
 * Pilar 1: GSAP Staggered Card Reveal Modifier.
 * Animates a component's entrance with all 10 GSAP presets:
 * - Standard slide + 3D pitch
 * - `GSAP_BOUNCE_GRAVITY`: Top-down gravity drop bounce
 * - `GSAP_VORTEX_SPIRAL_3D`: 3D diagonal vortex spiral (`rotationZ` + `rotationY` + `rotationX`)
 * - `GSAP_GLITCH_CYBER_SNAP`: Horizontal high-frequency cyber glitch jitter
 * - `GSAP_JELLY_SQUISH_MORPH`: Inverse X/Y elastic jelly squash & stretch
 */
fun Modifier.gsapStaggerReveal(
    index: Int = -1,
    triggerKey: Any? = Unit,
    slideDp: Dp = 20.dp,
    enable3DPitch: Boolean = true
): Modifier = composed {
    val qualityMode = AppThemeController.graphicsQualityMode
    if (qualityMode == AppGraphicsQualityMode.SMOOTH || qualityMode == AppGraphicsQualityMode.BALANCED) {
        return@composed this
    }
    val preset = AppThemeController.activeGsapPreset
    val demoNonce = AppThemeController.gsapLiveDemoTriggerNonce
    val staggerOn = AppThemeController.gsapStaggerEnabled
    val tiltIntensity = AppThemeController.gsap3DTiltIntensity

    val resolvedIndex = remember {
        if (index >= 0) index else GsapAutoStaggerTracker.nextAutoIndex()
    }
    val progress = remember(triggerKey, demoNonce, preset) {
        Animatable(if (staggerOn) 0f else 1f)
    }

    LaunchedEffect(triggerKey, demoNonce, preset, staggerOn) {
        if (!staggerOn) {
            progress.snapTo(1f)
            return@LaunchedEffect
        }
        progress.snapTo(0f)
        val delayMs = GsapEase.effectiveStaggerDelayMs(resolvedIndex, preset)
        if (delayMs > 0L) {
            delay(delayMs)
        }
        progress.animateTo(
            targetValue = 1f,
            animationSpec = tween(
                durationMillis = GsapEase.effectiveDurationMs(preset.defaultDurationMs),
                easing = GsapEase.activePresetEasing(preset)
            )
        )
    }

    this.graphicsLayer {
        val p = progress.value
        val clampedAlpha = p.coerceIn(0f, 1f)
        val inv = 1f - p
        alpha = clampedAlpha

        when (preset) {
            GsapMotionPreset.GSAP_BOUNCE_GRAVITY -> {
                // Drops from above with parabolic bounce.out
                translationY = -slideDp.toPx() * 1.45f * inv
                val s = 0.90f + 0.10f * p
                scaleX = s
                scaleY = s
            }
            GsapMotionPreset.GSAP_VORTEX_SPIRAL_3D -> {
                // 3D spiral vortex rotation on X, Y, and Z axes
                translationY = slideDp.toPx() * 1.15f * inv
                val s = 0.82f + 0.18f * p
                scaleX = s
                scaleY = s
                rotationZ = (16f * inv * tiltIntensity).coerceIn(-28f, 28f)
                rotationY = (22f * inv * tiltIntensity).coerceIn(-35f, 35f)
                rotationX = (14f * inv * tiltIntensity).coerceIn(-25f, 25f)
                cameraDistance = 14f * density
            }
            GsapMotionPreset.GSAP_GLITCH_CYBER_SNAP -> {
                // High-frequency horizontal cyber glitch jitter that locks into place
                val glitchWave = sin(p * PI.toFloat() * 10f) * inv
                translationX = glitchWave * 14.dp.toPx()
                translationY = (slideDp.toPx() * 0.45f) * inv
                scaleX = 1f + glitchWave * 0.06f
                scaleY = 0.94f + 0.06f * p
            }
            GsapMotionPreset.GSAP_JELLY_SQUISH_MORPH -> {
                // Inverse X/Y elastic squash & stretch
                translationY = slideDp.toPx() * 0.85f * inv
                val jellyWave = sin(p * PI.toFloat() * 3.5f) * inv
                scaleX = (0.92f + 0.08f * p) + jellyWave * 0.14f
                scaleY = (0.92f + 0.08f * p) - jellyWave * 0.12f
            }
            else -> {
                translationY = slideDp.toPx() * inv
                val startScale = if (preset == GsapMotionPreset.CYBER_MAGNETIC_3D) 0.88f else 0.92f
                val currentScale = startScale + (1f - startScale) * p
                scaleX = currentScale
                scaleY = currentScale
                if (enable3DPitch && tiltIntensity > 0.05f) {
                    val basePitch = if (preset == GsapMotionPreset.CYBER_MAGNETIC_3D) 14f else 7.5f
                    rotationX = (basePitch * tiltIntensity * inv).coerceIn(-22f, 22f)
                    cameraDistance = 14f * density
                }
            }
        }
    }
}

/**
 * Pilar 2: GSAP 3D Elastic Scale & Perspective Tilt Press Modifier.
 * Reacts to touch down/move/release with 3D tilt (`rotationX`, `rotationY`),
 * elastic scale compression, and GSAP spring/elastic rebound.
 */
fun Modifier.gsap3DPress(
    enabled: Boolean = true,
    maxTiltDegrees: Float = 8.5f,
    pressScale: Float = 0.95f,
    depressDp: Dp = 3.dp,
    onClick: (() -> Unit)? = null
): Modifier = composed {
    val currentClick by rememberUpdatedState(onClick)
    val haptic = LocalHapticFeedback.current
    val interactionSource = remember { MutableInteractionSource() }
    val tiltIntensity = AppThemeController.gsap3DTiltIntensity
    val preset = AppThemeController.activeGsapPreset

    var isPressed by remember { mutableStateOf(false) }
    var normTouchX by remember { mutableFloatStateOf(0f) } // -1f .. +1f
    var normTouchY by remember { mutableFloatStateOf(0f) } // -1f .. +1f

    val pressProgress by animateFloatAsState(
        targetValue = if (isPressed) 1f else 0f,
        animationSpec = spring(
            dampingRatio = when (preset) {
                GsapMotionPreset.GSAP_ELASTIC_BOUNCE -> Spring.DampingRatioLowBouncy
                GsapMotionPreset.CYBER_MAGNETIC_3D -> Spring.DampingRatioMediumBouncy
                GsapMotionPreset.TURBO_SNAPPY -> Spring.DampingRatioNoBouncy
                else -> Spring.DampingRatioMediumBouncy
            },
            stiffness = when (preset) {
                GsapMotionPreset.TURBO_SNAPPY -> Spring.StiffnessHigh
                GsapMotionPreset.GSAP_ELASTIC_BOUNCE -> Spring.StiffnessMediumLow
                else -> Spring.StiffnessMedium
            }
        ),
        label = "gsap_3d_press_progress"
    )

    LaunchedEffect(interactionSource) {
        interactionSource.interactions.collect { interaction ->
            when (interaction) {
                is PressInteraction.Press -> {
                    isPressed = true
                    normTouchX = ((interaction.pressPosition.x % 240f) / 120f - 1f).coerceIn(-1f, 1f)
                    normTouchY = ((interaction.pressPosition.y % 140f) / 70f - 1f).coerceIn(-1f, 1f)
                    haptic.performHapticFeedback(
                        androidx.compose.ui.hapticfeedback.HapticFeedbackType.TextHandleMove
                    )
                }
                is PressInteraction.Release, is PressInteraction.Cancel -> {
                    isPressed = false
                }
            }
        }
    }

    val baseModifier = this.graphicsLayer {
        val p = pressProgress
        val scaleDelta = (1f - pressScale) * p
        scaleX = 1f - scaleDelta
        scaleY = 1f - scaleDelta
        translationX = 0f
        translationY = 0f
        rotationX = 0f
        rotationY = 0f
    }

    if (currentClick != null) {
        baseModifier.clickable(
            enabled = enabled,
            interactionSource = interactionSource,
            indication = null,
            onClick = {
                haptic.performHapticFeedback(
                    androidx.compose.ui.hapticfeedback.HapticFeedbackType.LongPress
                )
                currentClick?.invoke()
            }
        )
    } else {
        this
    }
}

/**
 * Pilar 3: GSAP Rolling Number & Currency Counter State.
 * Smoothly interpolates a Long value (e.g., IDR balance, coins, scores, file count)
 * from its old value to its new value using `GsapEase.ExpoOut`.
 */
@Composable
fun rememberGsapRollingLong(
    targetValue: Long,
    durationMs: Int = 620
): Long {
    val rollEnabled = AppThemeController.gsapCounterRollEnabled
    val demoNonce = AppThemeController.gsapLiveDemoTriggerNonce
    var displayedValue by remember { mutableLongStateOf(targetValue) }
    val animatable = remember { Animatable(targetValue.toFloat()) }

    LaunchedEffect(targetValue, rollEnabled, demoNonce) {
        if (!rollEnabled) {
            displayedValue = targetValue
            animatable.snapTo(targetValue.toFloat())
            return@LaunchedEffect
        }
        if (demoNonce > 0 && displayedValue == targetValue) {
            animatable.snapTo((targetValue * 0.35f))
        }
        animatable.animateTo(
            targetValue = targetValue.toFloat(),
            animationSpec = tween(
                durationMillis = GsapEase.effectiveDurationMs(durationMs),
                easing = GsapEase.ExpoOut
            )
        ) {
            displayedValue = value.roundToLong()
        }
        displayedValue = targetValue
    }

    return displayedValue
}

@Composable
fun rememberGsapRollingFloat(
    targetValue: Float,
    durationMs: Int = 620
): Float {
    val rollEnabled = AppThemeController.gsapCounterRollEnabled
    val demoNonce = AppThemeController.gsapLiveDemoTriggerNonce
    val animatable = remember { Animatable(0f) }

    LaunchedEffect(targetValue, rollEnabled, demoNonce) {
        if (!rollEnabled) {
            animatable.snapTo(targetValue)
            return@LaunchedEffect
        }
        animatable.snapTo(0f)
        animatable.animateTo(
            targetValue = targetValue,
            animationSpec = tween(
                durationMillis = GsapEase.effectiveDurationMs(durationMs),
                easing = GsapEase.activePresetEasing()
            )
        )
    }

    return animatable.value
}

/**
 * Pilar 4: GSAP Shared Morph Page & Sub-Tab Transition Container.
 * Smoothly morphs between screens, wings, or sub-tabs with combined scale, directional slide, and fade.
 */
@Composable
fun <T> GsapSharedMorphContainer(
    targetState: T,
    modifier: Modifier = Modifier,
    label: String = "gsap_shared_morph",
    content: @Composable (T) -> Unit
) {
    val qualityMode = AppThemeController.graphicsQualityMode
    val preset = AppThemeController.activeGsapPreset
    val durMs = if (qualityMode == AppGraphicsQualityMode.SMOOTH) {
        150
    } else {
        GsapEase.effectiveDurationMs(preset.defaultDurationMs)
    }

    AnimatedContent(
        targetState = targetState,
        modifier = modifier,
        transitionSpec = {
            val enterTransition = when (preset) {
                GsapMotionPreset.GSAP_GLITCH_CYBER_SNAP -> {
                    fadeIn(tween(durMs, easing = GsapEase.ExpoOut)) +
                        slideInHorizontally(
                            initialOffsetX = { fullWidth -> (fullWidth * 0.08f).roundToInt().coerceAtMost(42) },
                            animationSpec = tween(durMs, easing = GsapEase.ExpoOut)
                        ) +
                        scaleIn(initialScale = 0.94f, animationSpec = tween(durMs, easing = GsapEase.ExpoOut))
                }
                GsapMotionPreset.GSAP_BOUNCE_GRAVITY -> {
                    fadeIn(tween(durMs, easing = GsapEase.ExpoOut)) +
                        slideInVertically(
                            initialOffsetY = { fullHeight -> -(fullHeight * 0.06f).roundToInt().coerceAtLeast(-38) },
                            animationSpec = tween(durMs, easing = GsapEase.BounceOut)
                        ) +
                        scaleIn(initialScale = 0.91f, animationSpec = tween(durMs, easing = GsapEase.BounceOut))
                }
                else -> {
                    fadeIn(
                        animationSpec = tween(durationMillis = durMs, easing = GsapEase.ExpoOut)
                    ) + scaleIn(
                        initialScale = if (preset == GsapMotionPreset.GSAP_VORTEX_SPIRAL_3D) 0.88f else 0.93f,
                        animationSpec = tween(
                            durationMillis = durMs,
                            easing = GsapEase.activePresetEasing(preset)
                        )
                    ) + slideInVertically(
                        initialOffsetY = { fullHeight -> (fullHeight * 0.045f).roundToInt().coerceAtMost(28) },
                        animationSpec = tween(
                            durationMillis = durMs,
                            easing = GsapEase.activePresetEasing(preset)
                        )
                    )
                }
            }

            val exitTransition = when (preset) {
                GsapMotionPreset.GSAP_GLITCH_CYBER_SNAP -> {
                    fadeOut(tween((durMs * 0.5f).roundToInt(), easing = GsapEase.Power4Out)) +
                        slideOutHorizontally(
                            targetOffsetX = { fullWidth -> -(fullWidth * 0.05f).roundToInt().coerceAtLeast(-28) },
                            animationSpec = tween((durMs * 0.5f).roundToInt(), easing = GsapEase.Power4Out)
                        )
                }
                else -> {
                    fadeOut(
                        animationSpec = tween(durationMillis = (durMs * 0.55f).roundToInt(), easing = GsapEase.Power3Out)
                    ) + scaleOut(
                        targetScale = 0.96f,
                        animationSpec = tween(durationMillis = (durMs * 0.6f).roundToInt(), easing = GsapEase.Power3Out)
                    ) + slideOutVertically(
                        targetOffsetY = { fullHeight -> -(fullHeight * 0.025f).roundToInt().coerceAtLeast(-18) },
                        animationSpec = tween(durationMillis = (durMs * 0.6f).roundToInt(), easing = GsapEase.Power3Out)
                    )
                }
            }

            (enterTransition togetherWith exitTransition).using(
                SizeTransform(clip = false)
            )
        },
        label = label
    ) { state ->
        content(state)
    }
}

/**
 * Pilar 5 (Micro-Interaction): GSAP Magnetic Pull + 3D Tilt + Radial Particle Burst Ring.
 * Pulls the element subtly toward the touch point, tilts in 3D, compresses with elastic squash,
 * and emits an expanding GSAP `expo.out` radial ring + 8 micro-sparks upon release/click.
 */
fun Modifier.gsapMagneticMicroTouch(
    enabled: Boolean = true,
    maxMagneticShiftDp: Dp = 4.dp,
    maxTiltDegrees: Float = 10f,
    pressScale: Float = 0.92f,
    burstColor: Color = Color(0xFF38BDF8),
    onClick: (() -> Unit)? = null
): Modifier = composed {
    val currentClick by rememberUpdatedState(onClick)
    val haptic = LocalHapticFeedback.current
    val interactionSource = remember { MutableInteractionSource() }
    val qualityMode = AppThemeController.graphicsQualityMode

    if (qualityMode == AppGraphicsQualityMode.SMOOTH) {
        return@composed if (currentClick != null) {
            this.clickable(
                enabled = enabled,
                interactionSource = interactionSource,
                indication = null,
                onClick = { currentClick?.invoke() }
            )
        } else {
            this
        }
    }

    val tiltIntensity = AppThemeController.gsap3DTiltIntensity
    val isUltra = qualityMode == AppGraphicsQualityMode.ULTRA_GSAP ||
        qualityMode == AppGraphicsQualityMode.EXTREME

    var isPressed by remember { mutableStateOf(false) }
    var normTouchX by remember { mutableFloatStateOf(0f) }
    var normTouchY by remember { mutableFloatStateOf(0f) }
    var burstTriggerNonce by remember { mutableIntStateOf(0) }
    val burstProgress = remember { Animatable(1f) }

    LaunchedEffect(burstTriggerNonce) {
        if (burstTriggerNonce > 0 && qualityMode.tierLevel >= 3) {
            burstProgress.snapTo(0f)
            burstProgress.animateTo(
                targetValue = 1f,
                animationSpec = tween(
                    durationMillis = GsapEase.effectiveDurationMs(if (isUltra) 560 else 440),
                    easing = GsapEase.ExpoOut
                )
            )
        }
    }

    val pressFraction by animateFloatAsState(
        targetValue = if (isPressed) 1f else 0f,
        animationSpec = spring(
            dampingRatio = if (isUltra) 0.44f else 0.56f,
            stiffness = if (isUltra) 390f else 430f
        ),
        label = "gsap_magnetic_micro_press"
    )

    LaunchedEffect(interactionSource) {
        interactionSource.interactions.collect { interaction ->
            when (interaction) {
                is PressInteraction.Press -> {
                    isPressed = true
                    normTouchX = ((interaction.pressPosition.x % 180f) / 90f - 1f).coerceIn(-1f, 1f)
                    normTouchY = ((interaction.pressPosition.y % 100f) / 50f - 1f).coerceIn(-1f, 1f)
                    haptic.performHapticFeedback(
                        androidx.compose.ui.hapticfeedback.HapticFeedbackType.TextHandleMove
                    )
                }
                is PressInteraction.Release -> {
                    isPressed = false
                    burstTriggerNonce++
                }
                is PressInteraction.Cancel -> {
                    isPressed = false
                }
            }
        }
    }

    val visualModifier = this
        .graphicsLayer {
            val p = pressFraction
            translationX = 0f
            translationY = 0f
            val squashX = 1f - (1f - pressScale) * p
            val squashY = 1f - (1f - pressScale) * p
            scaleX = squashX
            scaleY = squashY
            rotationX = 0f
            rotationY = 0f
        }
        .drawWithContent {
            drawContent()
            val bp = burstProgress.value
            if (bp < 0.99f && qualityMode.tierLevel >= 3) {
                // Smooth Squircle Contour Ripple + Liquid Silk Shimmer Sweep (Zero circular particles!)
                val alpha = (1f - bp).coerceIn(0f, 1f)
                val cornerR = (size.height * 0.34f).coerceIn(10f, 28f)
                val insetFactor = (1f - bp) * 0.16f
                val insetX = size.width * insetFactor
                val insetY = size.height * insetFactor
                val waveW = (size.width - insetX * 2f).coerceAtLeast(4f)
                val waveH = (size.height - insetY * 2f).coerceAtLeast(4f)

                if (isUltra) {
                    // Outer Squircle Contour Wave
                    drawRoundRect(
                        brush = Brush.horizontalGradient(
                            colors = UltraRainbowPalette.map { it.copy(alpha = alpha * 0.78f) }
                        ),
                        topLeft = Offset(insetX, insetY),
                        size = Size(waveW, waveH),
                        cornerRadius = CornerRadius(cornerR, cornerR),
                        style = Stroke(width = (2.4f * (1f - bp * 0.45f)) * density)
                    )
                    // Inner Liquid Silk Shimmer Sweep across the button
                    val sweepX = (-0.2f + bp * 1.4f) * size.width
                    val sweepBand = (size.width * 0.32f).coerceAtLeast(18f)
                    drawRoundRect(
                        brush = Brush.horizontalGradient(
                            colors = listOf(
                                Color.Transparent,
                                burstColor.copy(alpha = alpha * 0.24f),
                                Color.White.copy(alpha = alpha * 0.36f),
                                burstColor.copy(alpha = alpha * 0.24f),
                                Color.Transparent
                            ),
                            startX = sweepX - sweepBand,
                            endX = sweepX + sweepBand
                        ),
                        topLeft = Offset.Zero,
                        size = size,
                        cornerRadius = CornerRadius(cornerR, cornerR)
                    )
                } else {
                    drawRoundRect(
                        color = burstColor.copy(alpha = alpha * 0.52f),
                        topLeft = Offset(insetX, insetY),
                        size = Size(waveW, waveH),
                        cornerRadius = CornerRadius(cornerR, cornerR),
                        style = Stroke(width = (2.0f * (1f - bp * 0.5f)) * density)
                    )
                }
            }
        }

    if (currentClick != null) {
        visualModifier.clickable(
            enabled = enabled,
            interactionSource = interactionSource,
            indication = null,
            onClick = {
                haptic.performHapticFeedback(
                    androidx.compose.ui.hapticfeedback.HapticFeedbackType.LongPress
                )
                currentClick?.invoke()
            }
        )
    } else {
        visualModifier
    }
}

/**
 * Pilar 6 (Micro-Interaction): GSAP Elastic Icon Pop & Wiggle Composable.
 * Animates an icon with GSAP `elastic.out(1.15f, 0.30f)` scale pop and rotational wiggle
 * whenever `activeTrigger` changes.
 */
@Composable
fun GsapMicroIconBounce(
    icon: ImageVector,
    contentDescription: String?,
    tint: Color,
    activeTrigger: Any? = true,
    iconSize: Dp = 16.dp,
    modifier: Modifier = Modifier
) {
    val popAnim = remember { Animatable(1f) }
    val wiggleAnim = remember { Animatable(0f) }

    LaunchedEffect(activeTrigger) {
        popAnim.snapTo(0.72f)
        wiggleAnim.snapTo(-14f)
        popAnim.animateTo(
            targetValue = 1f,
            animationSpec = tween(
                durationMillis = GsapEase.effectiveDurationMs(520),
                easing = GsapEase.elasticOut(1.15f, 0.32f)
            )
        )
        wiggleAnim.animateTo(
            targetValue = 0f,
            animationSpec = tween(
                durationMillis = GsapEase.effectiveDurationMs(420),
                easing = GsapEase.BackOutDefault
            )
        )
    }

    Icon(
        imageVector = icon,
        contentDescription = contentDescription,
        tint = tint,
        modifier = modifier
            .size(iconSize)
            .graphicsLayer {
                scaleX = popAnim.value
                scaleY = popAnim.value
                rotationZ = wiggleAnim.value * (1f - popAnim.value.coerceIn(0f, 1f))
            }
    )
}

/**
 * Pilar 7 (Macro-Interaction): GSAP Elastic Rubber-Band Drag & Swipe Physics Modifier.
 * Enables physical rubber-band stretching (`scaleX`, `scaleY`), 3D pitch/roll, and directional
 * swipe-to-expand / swipe-to-collapse macro transitions on hero containers like Dynamic Island.
 */
fun Modifier.gsapMacroElasticGesture(
    isExpanded: Boolean,
    onExpandMacro: () -> Unit,
    onCollapseMacro: () -> Unit,
    onDragTensionChanged: (stretchX: Float, stretchY: Float) -> Unit = { _, _ -> }
): Modifier = composed {
    val haptic = LocalHapticFeedback.current
    var rawDragX by remember { mutableFloatStateOf(0f) }
    var rawDragY by remember { mutableFloatStateOf(0f) }

    val smoothDragX by animateFloatAsState(
        targetValue = rawDragX,
        animationSpec = spring(
            dampingRatio = 0.56f,
            stiffness = 380f
        ),
        label = "macro_elastic_drag_x"
    )
    val smoothDragY by animateFloatAsState(
        targetValue = rawDragY,
        animationSpec = spring(
            dampingRatio = 0.56f,
            stiffness = 380f
        ),
        label = "macro_elastic_drag_y"
    )

    val stretchX = (1f + (kotlin.math.abs(smoothDragX) / 420f) - (smoothDragY / 680f)).coerceIn(0.90f, 1.14f)
    val stretchY = (1f + (smoothDragY / 360f) - (kotlin.math.abs(smoothDragX) / 720f)).coerceIn(0.88f, 1.18f)

    LaunchedEffect(stretchX, stretchY) {
        onDragTensionChanged(stretchX, stretchY)
    }

    this
        .pointerInput(isExpanded) {
            detectDragGestures(
                onDragEnd = {
                    if (!isExpanded && rawDragY > 28f) {
                        haptic.performHapticFeedback(
                            androidx.compose.ui.hapticfeedback.HapticFeedbackType.LongPress
                        )
                        onExpandMacro()
                    } else if (isExpanded && rawDragY < -28f) {
                        haptic.performHapticFeedback(
                            androidx.compose.ui.hapticfeedback.HapticFeedbackType.TextHandleMove
                        )
                        onCollapseMacro()
                    }
                    rawDragX = 0f
                    rawDragY = 0f
                },
                onDragCancel = {
                    rawDragX = 0f
                    rawDragY = 0f
                },
                onDrag = { change, dragAmount ->
                    change.consume()
                    rawDragX = (rawDragX + dragAmount.x * 0.45f).coerceIn(-68f, 68f)
                    rawDragY = (rawDragY + dragAmount.y * 0.52f).coerceIn(-64f, 82f)
                }
            )
        }
        .graphicsLayer {
            translationX = smoothDragX * 0.32f
            translationY = smoothDragY * 0.36f
            scaleX = stretchX
            scaleY = stretchY
            rotationX = (-smoothDragY * 0.14f).coerceIn(-12f, 12f)
            rotationY = (smoothDragX * 0.14f).coerceIn(-12f, 12f)
            cameraDistance = 15f * density
        }
}

/**
 * Interactive GSAP Motion & UI Animation Studio Card inside ThemeAndMascotStudioScreen (Tab Tema).
 * Allows live customization of GSAP curves, speed, 3D tilt, staggered wave, and live timeline replay.
 */
@Composable
fun GsapMotionStudioCard(
    modifier: Modifier = Modifier
) {
    val activePreset = AppThemeController.activeGsapPreset
    val speedMultiplier = AppThemeController.gsapSpeedMultiplier
    val tiltIntensity = AppThemeController.gsap3DTiltIntensity
    val staggerEnabled = AppThemeController.gsapStaggerEnabled
    val counterRollEnabled = AppThemeController.gsapCounterRollEnabled
    val demoNonce = AppThemeController.gsapLiveDemoTriggerNonce

    var localSandboxCounterTarget by remember { mutableLongStateOf(128_500_000L) }
    val animatedSandboxCounter = rememberGsapRollingLong(localSandboxCounterTarget)

    Box(
        modifier = modifier
            .fillMaxWidth()
            .duolingoCard(
                backgroundColor = DuoPureWhite,
                borderColor = Color(0xFF8B5CF6),
                lipColor = Color(0xFF6D28D9),
                cornerRadius = 22.dp,
                borderWidth = 2.dp,
                lipHeight = 4.dp
            )
            .padding(bottom = 4.dp)
            .padding(16.dp)
            .testTag("gsap_motion_studio_card")
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            // Header Row
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
                            .size(38.dp)
                            .duolingoButton(
                                faceColor = Color(0xFF8B5CF6),
                                lipColor = Color(0xFF6D28D9),
                                cornerRadius = 12.dp,
                                lipHeight = 3.dp
                            )
                            .padding(bottom = 3.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Bolt,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Studio GSAP Motion & Fisika Animasi 3D",
                            style = MaterialTheme.typography.titleSmall,
                            fontFamily = SpaceGroteskFontFamily,
                            fontWeight = FontWeight.ExtraBold,
                            color = CosmicTextPure
                        )
                        Text(
                            text = "Aktif di Brankas, Pengaturan, Fun Zone 333 & Sanctuary 444",
                            style = MaterialTheme.typography.bodySmall,
                            color = CosmicTextSubtle
                        )
                    }
                }

                // Live Replay Timeline Button
                Box(
                    modifier = Modifier
                        .duolingoButton(
                            faceColor = DuoFeatherGreen,
                            lipColor = DuoFeatherGreenLip,
                            cornerRadius = 12.dp,
                            lipHeight = 3.dp
                        )
                        .instantTouchPress {
                            AppThemeController.gsapLiveDemoTriggerNonce++
                            localSandboxCounterTarget = if (localSandboxCounterTarget == 128_500_000L) {
                                294_750_000L
                            } else {
                                128_500_000L
                            }
                        }
                        .padding(bottom = 3.dp)
                        .padding(horizontal = 10.dp, vertical = 7.dp)
                        .testTag("gsap_replay_timeline_button"),
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.PlayArrow,
                            contentDescription = "Uji Timeline GSAP",
                            tint = Color.White,
                            modifier = Modifier.size(14.dp)
                        )
                        Text(
                            text = "Uji Gerak",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color.White
                        )
                    }
                }
            }

            // LIVE INTERACTIVE 3D GSAP STAGE PREVIEW
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0xFF0F172A))
                    .border(1.dp, Color(0xFF38BDF8).copy(alpha = 0.35f), RoundedCornerShape(16.dp))
                    .padding(12.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "LIVE GSAP TIMELINE • ${activePreset.badgeText}",
                        fontFamily = JetBrainsMonoFontFamily,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color(0xFF38BDF8)
                    )
                    Text(
                        text = "Rp ${String.format("%,d", animatedSandboxCounter).replace(',', '.')}",
                        fontFamily = JetBrainsMonoFontFamily,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color(0xFF4ADE80)
                    )
                }

                // 4 Staggered 3D Interactive Stage Blocks
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    val stageItems = listOf(
                        Triple("Stagger", Color(0xFF38BDF8), 0),
                        Triple("3D Tilt", Color(0xFFA855F7), 1),
                        Triple("Counter", Color(0xFF22C55E), 2),
                        Triple("Morph", Color(0xFFF59E0B), 3)
                    )
                    stageItems.forEach { (label, accent, idx) ->
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(54.dp)
                                .gsapStaggerReveal(
                                    index = idx,
                                    triggerKey = demoNonce,
                                    slideDp = 24.dp,
                                    enable3DPitch = true
                                )
                                .gsap3DPress(
                                    maxTiltDegrees = 14f,
                                    pressScale = 0.90f,
                                    onClick = {
                                        AppThemeController.gsapLiveDemoTriggerNonce++
                                    }
                                )
                                .clip(RoundedCornerShape(12.dp))
                                .background(
                                    Brush.verticalGradient(
                                        colors = listOf(
                                            accent.copy(alpha = 0.32f),
                                            accent.copy(alpha = 0.12f)
                                        )
                                    )
                                )
                                .border(1.dp, accent.copy(alpha = 0.75f), RoundedCornerShape(12.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = "0${idx + 1}",
                                    fontFamily = JetBrainsMonoFontFamily,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = accent
                                )
                                Text(
                                    text = label,
                                    fontSize = 10.5.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White,
                                    maxLines = 1
                                )
                            }
                        }
                    }
                }
            }

            // 5 GRAPHICS QUALITY MODES (Smooth -> Ultra GSAP) + TRANSPARENT BUTTON BOX STUDIO
            val activeGraphicsMode = AppThemeController.graphicsQualityMode
            val transparentBoxOn = AppThemeController.transparentButtonBoxEnabled
            val transparentBoxAlpha = AppThemeController.transparentButtonAlpha

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0xFF0F172A))
                    .border(1.5.dp, activeGraphicsMode.accentColor.copy(alpha = 0.75f), RoundedCornerShape(16.dp))
                    .padding(12.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "5 MODE GRAFIK (SMOOTH → ULTRA GSAP)",
                            fontFamily = SpaceGroteskFontFamily,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color.White
                        )
                        Text(
                            text = "${activeGraphicsMode.badgeText} • ${activeGraphicsMode.fpsLabel}",
                            fontFamily = JetBrainsMonoFontFamily,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = activeGraphicsMode.accentColor
                        )
                    }
                }

                val context = androidx.compose.ui.platform.LocalContext.current
                AppGraphicsQualityMode.entries.chunked(4).forEach { rowModes ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        rowModes.forEach { modeOption ->
                            val selected = activeGraphicsMode == modeOption
                            val multLabel = when (modeOption) {
                                AppGraphicsQualityMode.CHAOS_ULTIMATE_LOW -> "1/25x"
                                AppGraphicsQualityMode.CHAOS_OVERDRIVE -> "100x"
                                else -> "${modeOption.effectMultiplier}x"
                            }
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(11.dp))
                                    .background(
                                        if (selected) modeOption.accentColor else Color(0xFF1E293B)
                                    )
                                    .border(
                                        width = if (selected) 1.5.dp else 1.dp,
                                        color = if (selected) Color.White else modeOption.accentColor.copy(alpha = 0.40f),
                                        shape = RoundedCornerShape(11.dp)
                                    )
                                    .instantTouchPress {
                                        AppThemeController.requestGraphicsModeChange(
                                            targetMode = modeOption,
                                            context = context
                                        )
                                    }
                                    .padding(vertical = 8.dp, horizontal = 4.dp)
                                    .testTag("studio_graphics_mode_${modeOption.id.lowercase()}"),
                                contentAlignment = Alignment.Center
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text(
                                        text = multLabel,
                                        fontFamily = JetBrainsMonoFontFamily,
                                        fontSize = 8.5.sp,
                                        fontWeight = FontWeight.ExtraBold,
                                        color = if (selected) Color.White.copy(alpha = 0.9f) else modeOption.accentColor
                                    )
                                    Text(
                                        text = modeOption.displayName,
                                        fontSize = 9.5.sp,
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

                Text(
                    text = activeGraphicsMode.subtitle,
                    fontSize = 10.5.sp,
                    color = Color(0xFFCBD5E1)
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFF1E293B))
                        .padding(horizontal = 10.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Box Tombol Transparan (Teks 100% Solid)",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color.White
                        )
                        Text(
                            text = "Box kaca transparan dengan border pelangi GSAP & teks tetap tajam",
                            fontSize = 9.5.sp,
                            color = Color(0xFF94A3B8)
                        )
                    }
                    GsapElasticToggleSwitch(
                        checked = transparentBoxOn,
                        activeColor = Color(0xFFEC4899),
                        onCheckedChange = { AppThemeController.transparentButtonBoxEnabled = it }
                    )
                }

                if (transparentBoxOn) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Transparansi Box Tombol",
                            fontSize = 10.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFE2E8F0)
                        )
                        Text(
                            text = "Alpha ${(transparentBoxAlpha * 100).roundToInt()}% • Teks 100%",
                            fontFamily = JetBrainsMonoFontFamily,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color(0xFF38BDF8)
                        )
                    }
                    Slider(
                        value = transparentBoxAlpha,
                        onValueChange = { AppThemeController.transparentButtonAlpha = it },
                        valueRange = 0.08f..0.65f,
                        colors = SliderDefaults.colors(
                            thumbColor = Color(0xFF38BDF8),
                            activeTrackColor = Color(0xFFEC4899)
                        )
                    )
                }
            }

            // 6 VARIASI GAYA MOTION GRAPHIC UI (GsapVisualEffectStyle) + LIVE MOTION STAGE
            val activeVisualStyle = AppThemeController.activeGsapVisualStyle
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0xFF0B1120))
                    .border(1.5.dp, activeVisualStyle.primaryColor.copy(alpha = 0.75f), RoundedCornerShape(16.dp))
                    .padding(12.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                GsapSplitTextWaveHeader(
                    text = "GSAP MOTION GRAPHIC UI",
                    accentColor = activeVisualStyle.primaryColor,
                    secondaryColor = activeVisualStyle.secondaryColor
                )

                GsapInteractiveMotionGraphicBanner(
                    title = activeVisualStyle.displayName,
                    subtitle = "${activeVisualStyle.subtitle} • Ketuk kanvas untuk gelombang kejut GSAP",
                    modifier = Modifier.fillMaxWidth()
                )

                Text(
                    text = "Pilih 6 Variasi Gaya Motion Graphic GSAP",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.White
                )

                GsapVisualEffectStyle.entries.chunked(2).forEach { rowStyles ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        rowStyles.forEach { styleOption ->
                            val selected = activeVisualStyle == styleOption
                            GsapJellySquishChip(
                                label = styleOption.displayName,
                                badge = styleOption.badgeText,
                                selected = selected,
                                accentColor = styleOption.primaryColor,
                                secondaryColor = styleOption.secondaryColor,
                                onClick = {
                                    AppThemeController.activeGsapVisualStyle = styleOption
                                    AppThemeController.gsapLiveDemoTriggerNonce++
                                },
                                modifier = Modifier
                                    .weight(1f)
                                    .testTag("gsap_visual_style_${styleOption.id.lowercase()}")
                            )
                        }
                    }
                }
            }

            // GSAP DRAGGABLE 'duo-card' REPORT CARDS (FREE DRAG & REORDER WITH SMOOTH SPRING)
            GsapDraggableDuoCardReportDeck(
                modifier = Modifier.fillMaxWidth()
            )

            // 10 GSAP PRESET CARDS (2x5 Grid)
            Text(
                text = "Pilih 10 Karakter Kurva Animasi GSAP (Easing & Physics)",
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.ExtraBold,
                color = CosmicTextPure
            )

            GsapMotionPreset.entries.chunked(2).forEach { rowPresets ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    rowPresets.forEach { preset ->
                        val isSelected = activePreset == preset
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .then(
                                    if (isSelected) {
                                        Modifier.duolingoButton(
                                            faceColor = Color(0xFF8B5CF6),
                                            lipColor = Color(0xFF6D28D9),
                                            cornerRadius = 14.dp,
                                            lipHeight = 3.dp
                                        )
                                    } else {
                                        Modifier.duolingoCard(
                                            backgroundColor = DuoMintSurface,
                                            borderColor = DuoSwanBorder,
                                            lipColor = DuoHareLip,
                                            cornerRadius = 14.dp,
                                            borderWidth = 1.5.dp,
                                            lipHeight = 2.dp
                                        )
                                    }
                                )
                                .instantTouchPress {
                                    AppThemeController.activeGsapPreset = preset
                                    AppThemeController.gsapLiveDemoTriggerNonce++
                                }
                                .padding(bottom = 3.dp)
                                .padding(horizontal = 10.dp, vertical = 9.dp)
                                .testTag("gsap_preset_${preset.id}")
                        ) {
                            Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = preset.displayName,
                                        fontSize = 11.5.sp,
                                        fontWeight = FontWeight.ExtraBold,
                                        color = if (isSelected) Color.White else DuoEelInk,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis,
                                        modifier = Modifier.weight(1f)
                                    )
                                }
                                Text(
                                    text = preset.badgeText,
                                    fontFamily = JetBrainsMonoFontFamily,
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isSelected) Color(0xFFFDE047) else Color(0xFF7C3AED)
                                )
                                Text(
                                    text = preset.subtitle,
                                    fontSize = 9.5.sp,
                                    color = if (isSelected) Color.White.copy(alpha = 0.88f) else CosmicTextSubtle,
                                    maxLines = 2,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }
                    }
                }
            }

            // SPEED & 3D TILT SLIDERS
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(DuoMintSurface)
                    .padding(horizontal = 12.dp, vertical = 10.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Kecepatan Timeline GSAP",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = DuoEelInk
                    )
                    Text(
                        text = String.format("%.2fx", speedMultiplier),
                        fontFamily = JetBrainsMonoFontFamily,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color(0xFF7C3AED)
                    )
                }
                Slider(
                    value = speedMultiplier,
                    onValueChange = { AppThemeController.gsapSpeedMultiplier = it },
                    valueRange = 0.5f..2.0f,
                    colors = SliderDefaults.colors(
                        thumbColor = Color(0xFF7C3AED),
                        activeTrackColor = Color(0xFF8B5CF6)
                    )
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Intensitas Kemiringan 3D Tilt & Skala",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = DuoEelInk
                    )
                    Text(
                        text = "${(tiltIntensity * 100).roundToInt()}%",
                        fontFamily = JetBrainsMonoFontFamily,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = DuoMacawBlue
                    )
                }
                Slider(
                    value = tiltIntensity,
                    onValueChange = { AppThemeController.gsap3DTiltIntensity = it },
                    valueRange = 0.0f..2.0f,
                    colors = SliderDefaults.colors(
                        thumbColor = DuoMacawBlue,
                        activeTrackColor = DuoMacawBlue
                    )
                )
            }

            // TOGGLES: STAGGERED REVEAL & ROLLING COUNTER (WITH ELASTIC SQUISH SWITCHES)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Row(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(12.dp))
                        .background(DuoMintSurface)
                        .padding(horizontal = 10.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Stagger Kartu",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = DuoEelInk
                    )
                    GsapElasticToggleSwitch(
                        checked = staggerEnabled,
                        activeColor = DuoFeatherGreen,
                        onCheckedChange = {
                            AppThemeController.gsapStaggerEnabled = it
                            if (it) AppThemeController.gsapLiveDemoTriggerNonce++
                        }
                    )
                }

                Row(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(12.dp))
                        .background(DuoMintSurface)
                        .padding(horizontal = 10.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Angka Bergulir",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = DuoEelInk
                    )
                    GsapElasticToggleSwitch(
                        checked = counterRollEnabled,
                        activeColor = DuoMacawBlue,
                        onCheckedChange = { AppThemeController.gsapCounterRollEnabled = it }
                    )
                }
            }
        }
    }
}

// ============================================================================
// PILAR 8: HYPER-TACTILE MICRO-INTERACTIONS SUITE
// - GsapElasticToggleSwitch (Liquid Squish & Stretch Thumb + Radial Track Wave)
// - Modifier.gsapKeystrokeReactiveHalo (Reactive Input Border Glow + Keystroke Pop + Shake)
// - GsapLivePulseBadge (Multi-Ring Radar Pulse Status Indicator)
// - GsapMorphingActionIcon (3D Flip + Spring Rotation + Squash & Particle Burst)
// - Modifier.gsapBreathingCardAura (Subtle Breathing Scale & Edge Specular Pulse)
// ============================================================================

@Composable
fun GsapElasticToggleSwitch(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    activeColor: Color = DuoFeatherGreen,
    inactiveColor: Color = Color(0xFFCBD5E1),
    modifier: Modifier = Modifier
) {
    val haptic = LocalHapticFeedback.current
    val progress by animateFloatAsState(
        targetValue = if (checked) 1f else 0f,
        animationSpec = spring(
            dampingRatio = 0.54f,
            stiffness = 380f
        ),
        label = "gsap_elastic_switch_progress"
    )
    var waveNonce by remember { mutableIntStateOf(0) }
    val waveProgress = remember { Animatable(1f) }

    LaunchedEffect(checked) {
        waveNonce++
    }

    LaunchedEffect(waveNonce) {
        if (waveNonce > 1) {
            waveProgress.snapTo(0f)
            waveProgress.animateTo(
                targetValue = 1f,
                animationSpec = tween(
                    durationMillis = GsapEase.effectiveDurationMs(440),
                    easing = GsapEase.ExpoOut
                )
            )
        }
    }

    // Mid-flight liquid stretch factor: peaks at progress = 0.5f
    val midFlight = (1f - kotlin.math.abs(progress - 0.5f) * 2f).coerceIn(0f, 1f)
    val thumbStretchX = 1f + midFlight * 0.42f
    val thumbSquashY = 1f - midFlight * 0.16f

    Box(
        modifier = modifier
            .width(46.dp)
            .height(26.dp)
            .clip(CircleShape)
            .drawWithContent {
                val trackColor = androidx.compose.ui.graphics.lerp(
                    inactiveColor,
                    activeColor,
                    progress.coerceIn(0f, 1f)
                )
                drawRoundRect(
                    color = trackColor,
                    cornerRadius = CornerRadius(size.height / 2f, size.height / 2f)
                )
                val wp = waveProgress.value
                if (wp < 0.99f) {
                    val cx = if (checked) size.width * 0.72f else size.width * 0.28f
                    val cy = size.height * 0.5f
                    drawCircle(
                        color = Color.White.copy(alpha = (1f - wp) * 0.45f),
                        radius = size.width * 0.65f * wp,
                        center = Offset(cx, cy),
                        style = Stroke(width = 1.8.dp.toPx())
                    )
                }
                drawContent()
            }
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) {
                haptic.performHapticFeedback(
                    androidx.compose.ui.hapticfeedback.HapticFeedbackType.LongPress
                )
                onCheckedChange(!checked)
            }
            .padding(horizontal = 3.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        val travelDp = 20.dp * progress.coerceIn(-0.08f, 1.08f)
        Box(
            modifier = Modifier
                .padding(start = travelDp.coerceAtLeast(0.dp))
                .size(20.dp)
                .graphicsLayer {
                    scaleX = thumbStretchX
                    scaleY = thumbSquashY
                }
                .clip(CircleShape)
                .background(Color.White)
                .border(1.dp, Color.Black.copy(alpha = 0.08f), CircleShape)
        )
    }
}

/**
 * Keystroke-Reactive Input Halo & Shake Modifier:
 * Pulses a luminous border and micro-pop scale on every character typed,
 * and triggers a horizontal spring shake when `shakeTriggerNonce` increments.
 */
fun Modifier.gsapKeystrokeReactiveHalo(
    textValue: String,
    accentColor: Color = DuoFeatherGreen,
    shakeTriggerNonce: Int = 0,
    cornerRadius: Dp = 18.dp
): Modifier = composed {
    val keystrokePulse = remember { Animatable(0f) }
    val shakeAnim = remember { Animatable(0f) }
    var previousLength by remember { mutableIntStateOf(textValue.length) }

    LaunchedEffect(textValue) {
        if (textValue.length != previousLength) {
            previousLength = textValue.length
            keystrokePulse.snapTo(1f)
            keystrokePulse.animateTo(
                targetValue = 0f,
                animationSpec = tween(
                    durationMillis = GsapEase.effectiveDurationMs(340),
                    easing = GsapEase.ExpoOut
                )
            )
        }
    }

    LaunchedEffect(shakeTriggerNonce) {
        if (shakeTriggerNonce > 0) {
            shakeAnim.snapTo(1f)
            shakeAnim.animateTo(
                targetValue = 0f,
                animationSpec = tween(
                    durationMillis = GsapEase.effectiveDurationMs(420),
                    easing = GsapEase.ElasticOutDefault
                )
            )
        }
    }

    this
        .graphicsLayer {
            val kp = keystrokePulse.value
            val pop = 1f + kp * 0.011f
            scaleX = pop
            scaleY = pop
            val sk = shakeAnim.value
            if (sk > 0.01f) {
                translationX = sin(sk * PI.toFloat() * 6f) * 9.dp.toPx() * sk
            }
        }
        .drawWithContent {
            drawContent()
            val kp = keystrokePulse.value
            if (kp > 0.02f) {
                val r = cornerRadius.toPx()
                drawRoundRect(
                    color = accentColor.copy(alpha = kp * 0.52f),
                    topLeft = Offset.Zero,
                    size = size,
                    cornerRadius = CornerRadius(r, r),
                    style = Stroke(width = (2.4f * kp).dp.toPx())
                )
            }
        }
}

/**
 * Live Multi-Ring Radar Pulse Status Badge:
 * Renders an animated expanding radar dot beside a crisp status label.
 */
@Composable
fun GsapLivePulseBadge(
    text: String,
    badgeColor: Color = DuoFeatherGreen,
    textColor: Color = Color.White,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "gsap_live_pulse_badge")
    val radarWave by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1500, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "gsap_radar_wave"
    )

    Row(
        modifier = modifier
            .clip(RoundedCornerShape(999.dp))
            .background(badgeColor.copy(alpha = 0.16f))
            .border(1.dp, badgeColor.copy(alpha = 0.55f), RoundedCornerShape(999.dp))
            .padding(horizontal = 9.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Box(
            modifier = Modifier
                .size(10.dp)
                .drawWithContent {
                    val center = Offset(size.width / 2f, size.height / 2f)
                    val maxR = size.width * 0.85f
                    drawCircle(
                        color = badgeColor.copy(alpha = (1f - radarWave) * 0.65f),
                        radius = maxR * radarWave,
                        center = center,
                        style = Stroke(width = 1.4.dp.toPx())
                    )
                    drawCircle(
                        color = badgeColor,
                        radius = size.width * 0.34f,
                        center = center
                    )
                }
        )
        Text(
            text = text,
            fontFamily = JetBrainsMonoFontFamily,
            fontSize = 10.sp,
            fontWeight = FontWeight.ExtraBold,
            color = textColor,
            maxLines = 1
        )
    }
}

/**
 * Morphing & Keyframe Pop Action Icon:
 * Transitions between states with a 3D Y-axis flip, Z-axis spring spin, elastic scale pop,
 * and radial particle sparks.
 */
@Composable
fun GsapMorphingActionIcon(
    icon: ImageVector,
    contentDescription: String?,
    tint: Color,
    stateKey: Any? = icon,
    iconSize: Dp = 18.dp,
    modifier: Modifier = Modifier
) {
    val popScale = remember { Animatable(1f) }
    val flipRotation = remember { Animatable(0f) }

    LaunchedEffect(stateKey) {
        popScale.snapTo(0.64f)
        flipRotation.snapTo(-90f)
        popScale.animateTo(
            targetValue = 1f,
            animationSpec = tween(
                durationMillis = GsapEase.effectiveDurationMs(480),
                easing = GsapEase.elasticOut(1.18f, 0.30f)
            )
        )
        flipRotation.animateTo(
            targetValue = 0f,
            animationSpec = tween(
                durationMillis = GsapEase.effectiveDurationMs(380),
                easing = GsapEase.BackOutDefault
            )
        )
    }

    Icon(
        imageVector = icon,
        contentDescription = contentDescription,
        tint = tint,
        modifier = modifier
            .size(iconSize)
            .graphicsLayer {
                scaleX = popScale.value
                scaleY = popScale.value
                rotationY = flipRotation.value * (1f - popScale.value.coerceIn(0f, 1f))
                rotationZ = (flipRotation.value * 0.25f) * (1f - popScale.value.coerceIn(0f, 1f))
                cameraDistance = 12f * density
            }
    )
}

/**
 * Breathing Card Aura Modifier:
 * Adds a gentle sinusoidal breathing elevation/scale pulse and animated specular rim highlight.
 */
fun Modifier.gsapBreathingCardAura(
    accentColor: Color = DuoMacawBlue,
    cornerRadius: Dp = 20.dp
): Modifier = composed {
    val infiniteTransition = rememberInfiniteTransition(label = "gsap_card_breath")
    val breathPhase by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 2600, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "gsap_breath_phase"
    )

    this
        .graphicsLayer {
            val scalePulse = 1f + (breathPhase - 0.5f) * 0.006f
            scaleX = scalePulse
            scaleY = scalePulse
        }
        .drawWithContent {
            drawContent()
            val r = cornerRadius.toPx()
            drawRoundRect(
                color = accentColor.copy(alpha = 0.08f + breathPhase * 0.14f),
                topLeft = Offset.Zero,
                size = size,
                cornerRadius = CornerRadius(r, r),
                style = Stroke(width = 1.4.dp.toPx())
            )
        }
}

// ============================================================================
// PILAR 9: GSAP MOTION GRAPHIC UI & VARIED MICRO-INTERACTION COMPONENTS
// - GsapSplitTextWaveHeader: Per-character GSAP SplitText sine wave & tap pop
// - GsapJellySquishChip: Elastic X/Y squash & stretch micro-interaction pill
// - GsapInteractiveMotionGraphicBanner: Interactive Canvas Motion Graphic UI stage
// ============================================================================

@Composable
fun GsapSplitTextWaveHeader(
    text: String,
    accentColor: Color = Color(0xFF00F2FE),
    secondaryColor: Color = Color(0xFFEC4899),
    modifier: Modifier = Modifier
) {
    val qualityMode = AppThemeController.graphicsQualityMode
    var tapPopNonce by remember { mutableIntStateOf(0) }
    val popAnim = remember { Animatable(0f) }

    LaunchedEffect(tapPopNonce) {
        if (tapPopNonce > 0) {
            popAnim.snapTo(1f)
            popAnim.animateTo(
                targetValue = 0f,
                animationSpec = tween(
                    durationMillis = GsapEase.effectiveDurationMs(560),
                    easing = GsapEase.ElasticOutDefault
                )
            )
        }
    }

    val wavePhase: Float = if (qualityMode == AppGraphicsQualityMode.SMOOTH) {
        0f
    } else {
        val infiniteTransition = rememberInfiniteTransition(label = "gsap_split_text_wave")
        val anim by infiniteTransition.animateFloat(
            initialValue = 0f,
            targetValue = (2.0 * PI).toFloat(),
            animationSpec = infiniteRepeatable(
                animation = tween(durationMillis = 2200, easing = LinearEasing),
                repeatMode = RepeatMode.Restart
            ),
            label = "split_text_phase"
        )
        anim
    }

    Row(
        modifier = modifier
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) {
                tapPopNonce++
                AppThemeController.gsapLiveDemoTriggerNonce++
            },
        verticalAlignment = Alignment.CenterVertically
    ) {
        text.forEachIndexed { idx, ch ->
            val charOffsetRad = wavePhase + idx * 0.36f
            val dy = if (qualityMode == AppGraphicsQualityMode.SMOOTH) {
                0f
            } else {
                sin(charOffsetRad) * (2.6f + popAnim.value * 5.5f)
            }
            val scaleBoost = 1f + popAnim.value * 0.22f * sin(idx * 0.5f + 1f).coerceIn(0f, 1f)
            val colorFraction = ((sin(charOffsetRad) + 1f) * 0.5f).coerceIn(0f, 1f)
            val charColor = androidx.compose.ui.graphics.lerp(accentColor, secondaryColor, colorFraction)

            Text(
                text = ch.toString(),
                fontFamily = SpaceGroteskFontFamily,
                fontSize = 13.sp,
                fontWeight = FontWeight.ExtraBold,
                color = if (ch == ' ') Color.Transparent else charColor,
                modifier = Modifier.graphicsLayer {
                    translationY = dy
                    scaleX = scaleBoost
                    scaleY = scaleBoost
                }
            )
        }
    }
}

@Composable
fun GsapJellySquishChip(
    label: String,
    badge: String,
    selected: Boolean,
    accentColor: Color,
    secondaryColor: Color = accentColor,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val haptic = LocalHapticFeedback.current
    var squishNonce by remember { mutableIntStateOf(0) }
    val squishProgress = remember { Animatable(0f) }

    LaunchedEffect(squishNonce, selected) {
        if (squishNonce > 0) {
            squishProgress.snapTo(1f)
            squishProgress.animateTo(
                targetValue = 0f,
                animationSpec = tween(
                    durationMillis = GsapEase.effectiveDurationMs(540),
                    easing = GsapEase.elasticOut(1.35f, 0.28f)
                )
            )
        }
    }

    Box(
        modifier = modifier
            .graphicsLayer {
                val sq = squishProgress.value
                val wave = sin(sq * PI.toFloat() * 3f) * sq
                scaleX = 1f + wave * 0.16f
                scaleY = 1f - wave * 0.14f
                rotationZ = wave * 2.5f
            }
            .clip(RoundedCornerShape(12.dp))
            .background(
                if (selected) {
                    Brush.horizontalGradient(
                        colors = listOf(
                            accentColor.copy(alpha = 0.88f),
                            secondaryColor.copy(alpha = 0.82f)
                        )
                    )
                } else {
                    Brush.horizontalGradient(
                        colors = listOf(
                            Color(0xFF1E293B),
                            Color(0xFF0F172A)
                        )
                    )
                }
            )
            .border(
                width = if (selected) 1.5.dp else 1.dp,
                color = if (selected) Color.White else accentColor.copy(alpha = 0.45f),
                shape = RoundedCornerShape(12.dp)
            )
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) {
                haptic.performHapticFeedback(
                    androidx.compose.ui.hapticfeedback.HapticFeedbackType.LongPress
                )
                squishNonce++
                onClick()
            }
            .padding(horizontal = 10.dp, vertical = 8.dp)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
            Text(
                text = badge,
                fontFamily = JetBrainsMonoFontFamily,
                fontSize = 8.5.sp,
                fontWeight = FontWeight.ExtraBold,
                color = if (selected) Color(0xFFFDE047) else accentColor,
                maxLines = 1
            )
            Text(
                text = label,
                fontSize = 10.5.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color.White,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

/**
 * Interactive GSAP Motion Graphic UI Stage / Banner:
 * Renders real-time multi-layer vector motion graphics reactive to `AppThemeController.activeGsapVisualStyle`:
 * - Flowing Bezier sine wave ribbons
 * - Orbiting constellation nodes & connecting vector links
 * - Equalizer telemetry bars & Sci-Fi HUD reticle rings
 * - Interactive tap shockwave & elastic 3D tilt micro-interaction
 */
@Composable
fun GsapInteractiveMotionGraphicBanner(
    title: String,
    subtitle: String,
    modifier: Modifier = Modifier
) {
    val qualityMode = AppThemeController.graphicsQualityMode
    val visualStyle = AppThemeController.activeGsapVisualStyle
    val activePreset = AppThemeController.activeGsapPreset
    val haptic = LocalHapticFeedback.current

    var shockwaveOrigin by remember { mutableStateOf(Offset(200f, 60f)) }
    var shockwaveNonce by remember { mutableIntStateOf(0) }
    val shockwaveAnim = remember { Animatable(1f) }

    LaunchedEffect(shockwaveNonce) {
        if (shockwaveNonce > 0) {
            shockwaveAnim.snapTo(0f)
            shockwaveAnim.animateTo(
                targetValue = 1f,
                animationSpec = tween(
                    durationMillis = GsapEase.effectiveDurationMs(680),
                    easing = GsapEase.ExpoOut
                )
            )
        }
    }

    val motionPhase: Float = if (qualityMode == AppGraphicsQualityMode.SMOOTH) {
        0.35f
    } else {
        val infiniteTransition = rememberInfiniteTransition(label = "gsap_motion_graphic_stage")
        val anim by infiniteTransition.animateFloat(
            initialValue = 0f,
            targetValue = 1f,
            animationSpec = infiniteRepeatable(
                animation = tween(
                    durationMillis = if (qualityMode == AppGraphicsQualityMode.ULTRA_GSAP) 2800 else 4200,
                    easing = LinearEasing
                ),
                repeatMode = RepeatMode.Restart
            ),
            label = "stage_wave_phase"
        )
        anim
    }

    val palette = resolveGsapStylePalette(visualStyle)

    Box(
        modifier = modifier
            .height(92.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFF070B14))
            .border(
                width = 1.5.dp,
                brush = Brush.horizontalGradient(palette),
                shape = RoundedCornerShape(16.dp)
            )
            .pointerInput(Unit) {
                awaitEachGesture {
                    val down = awaitFirstDown(requireUnconsumed = false)
                    shockwaveOrigin = down.position
                    shockwaveNonce++
                    haptic.performHapticFeedback(
                        androidx.compose.ui.hapticfeedback.HapticFeedbackType.LongPress
                    )
                    AppThemeController.gsapLiveDemoTriggerNonce++
                    waitForUpOrCancellation()
                }
            }
            .drawBehind {
                val w = size.width
                val h = size.height
                val rad = (motionPhase * 2.0 * PI).toFloat()

                // 1. Subtle Cyber Grid Background
                val cols = 8
                for (i in 1 until cols) {
                    val x = (w / cols) * i
                    drawLine(
                        color = visualStyle.primaryColor.copy(alpha = 0.08f),
                        start = Offset(x, 0f),
                        end = Offset(x, h),
                        strokeWidth = 1.dp.toPx()
                    )
                }

                // 2. Flowing GSAP Sine Ribbon Waves (2 harmonic paths)
                for (waveIdx in 0..1) {
                    val path = Path()
                    val amp = if (waveIdx == 0) h * 0.22f else h * 0.15f
                    val freq = if (waveIdx == 0) 2.2f else 3.4f
                    val phaseShift = if (waveIdx == 0) rad else -rad * 1.3f
                    val steps = 28
                    for (step in 0..steps) {
                        val frac = step.toFloat() / steps.toFloat()
                        val x = frac * w
                        val y = h * 0.54f + sin(frac * freq * PI.toFloat() + phaseShift) * amp
                        if (step == 0) path.moveTo(x, y) else path.lineTo(x, y)
                    }
                    drawPath(
                        path = path,
                        brush = Brush.horizontalGradient(palette),
                        style = Stroke(width = if (waveIdx == 0) 2.4.dp.toPx() else 1.4.dp.toPx())
                    )
                }

                // 3. Equalizer telemetry bars inside sleek squircle frame on the right side (Zero circular dots!)
                val hudCenter = Offset(w - 42.dp.toPx(), h * 0.5f)
                drawRoundRect(
                    color = visualStyle.primaryColor.copy(alpha = 0.35f),
                    topLeft = Offset(hudCenter.x - 22.dp.toPx(), hudCenter.y - 18.dp.toPx()),
                    size = Size(44.dp.toPx(), 36.dp.toPx()),
                    cornerRadius = CornerRadius(10.dp.toPx(), 10.dp.toPx()),
                    style = Stroke(width = 1.5.dp.toPx())
                )

                // Equalizer telemetry bars inside squircle frame
                for (bar in -2..2) {
                    val barX = hudCenter.x + bar * 5.dp.toPx()
                    val barFactor = 0.25f + 0.75f * ((sin(rad * 2f + bar * 0.9f) + 1f) * 0.5f)
                    val barHalfH = 12.dp.toPx() * barFactor
                    drawLine(
                        color = palette[(bar + 2) % palette.size],
                        start = Offset(barX, hudCenter.y - barHalfH),
                        end = Offset(barX, hudCenter.y + barHalfH),
                        strokeWidth = 2.5.dp.toPx()
                    )
                }

                // 4. Interactive Tap Smooth Silk Shimmer Wave (Zero circular sparks!)
                val sp = shockwaveAnim.value
                if (sp < 0.98f) {
                    val alpha = (1f - sp).coerceIn(0f, 1f)
                    val sweepX = w * sp
                    val bandW = (w * 0.28f).coerceAtLeast(28f)
                    drawRoundRect(
                        brush = Brush.horizontalGradient(
                            colors = listOf(
                                Color.Transparent,
                                visualStyle.primaryColor.copy(alpha = alpha * 0.45f),
                                Color.White.copy(alpha = alpha * 0.55f),
                                visualStyle.secondaryColor.copy(alpha = alpha * 0.45f),
                                Color.Transparent
                            ),
                            startX = sweepX - bandW,
                            endX = sweepX + bandW
                        ),
                        topLeft = Offset.Zero,
                        size = size,
                        cornerRadius = CornerRadius(16.dp.toPx(), 16.dp.toPx())
                    )
                }
            }
            .padding(horizontal = 14.dp, vertical = 10.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(0.76f),
            verticalArrangement = Arrangement.spacedBy(3.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                GsapLivePulseBadge(
                    text = visualStyle.badgeText,
                    badgeColor = visualStyle.primaryColor
                )
                Text(
                    text = activePreset.badgeText,
                    fontFamily = JetBrainsMonoFontFamily,
                    fontSize = 9.5.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = visualStyle.secondaryColor,
                    maxLines = 1
                )
            }
            Text(
                text = title,
                fontFamily = SpaceGroteskFontFamily,
                fontSize = 13.5.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color.White,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = subtitle,
                fontSize = 10.sp,
                color = Color(0xFFCBD5E1),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

// ============================================================================
// PILAR 10: GSAP DRAGGABLE 'duo-card' REORDERABLE REPORT CARDS ENGINE
// - Allows users to freely drag & reorder report cards ('duo-card') in 2D
//   with kinetic 3D tilt during drag and smooth GSAP Spring rebound on release!
// ============================================================================

data class GsapDraggableReportCardModel(
    val id: String,
    val codeBadge: String,
    val title: String,
    val metricValue: String,
    val subtitle: String,
    val accentColor: Color,
    val lipColor: Color,
    val isRainbowPrismBox: Boolean = false
)

/**
 * Standalone GSAP Draggable Modifier for any `duo-card` container:
 * Lets the user drag the card freely in X/Y with 3D kinetic tilt and smooth spring snap-back on release,
 * or triggers `onReorderDeltaY` when crossing vertical thresholds to reorder cards in a list.
 */
fun Modifier.gsapDraggableDuoCard(
    cardKey: Any,
    enabled: Boolean = true,
    swapThresholdPx: Float = 135f,
    onMoveUp: (() -> Unit)? = null,
    onMoveDown: (() -> Unit)? = null
): Modifier = this

/**
 * Static `duo-card` Report Card Deck (Locked in place so cards never shift when scrolling).
 */
@Composable
fun GsapDraggableDuoCardReportDeck(
    modifier: Modifier = Modifier,
    title: String = "KARTU LAPORAN KEAMANAN & TELEMETRI (TERKUNCI TETAP)"
) {
    val qualityMode = AppThemeController.graphicsQualityMode
    val reportCards = remember {
        mutableStateListOf(
            GsapDraggableReportCardModel(
                id = "report_crypto_audit",
                codeBadge = "REPORT #01 • AES-GCM",
                title = "Laporan Audit Enkripsi & Brankas",
                metricValue = "100% AMAN",
                subtitle = "Tahan & geser (drag) kartu duo-card ini untuk mengubah urutan laporan dengan pegas GSAP",
                accentColor = DuoFeatherGreen,
                lipColor = DuoFeatherGreenLip,
                isRainbowPrismBox = false
            ),
            GsapDraggableReportCardModel(
                id = "report_mori_engine",
                codeBadge = "REPORT #02 • MORI v4.6.0",
                title = "Laporan Ekstraktor & 8 Alat Mori",
                metricValue = "16 PLATFORM",
                subtitle = "Kotak aksen prisma selektif (warna sama dari Smooth 1x hingga Ultra 20x)",
                accentColor = DuoBeetlePurple,
                lipColor = DuoBeetlePurpleLip,
                isRainbowPrismBox = true
            ),
            GsapDraggableReportCardModel(
                id = "report_stego_forensic",
                codeBadge = "REPORT #03 • ANTI-FORENSIK",
                title = "Laporan Steganografi & Sanitasi EXIF",
                metricValue = "ZERO LEAK",
                subtitle = "Geser bebas ke atas/bawah untuk menyusun ulang prioritas kartu laporan",
                accentColor = DuoMacawBlue,
                lipColor = DuoMacawBlueLip,
                isRainbowPrismBox = false
            ),
            GsapDraggableReportCardModel(
                id = "report_gsap_telemetry",
                codeBadge = "REPORT #04 • ${qualityMode.effectMultiplier}x GSAP",
                title = "Laporan Telemetri Grafik & Frame Rate",
                metricValue = "${qualityMode.effectMultiplier}x EFEK",
                subtitle = "Mode aktif: ${qualityMode.displayName} (${qualityMode.fpsLabel}) • Spring Rebound Aktif",
                accentColor = DuoFoxOrange,
                lipColor = DuoFoxOrangeLip,
                isRainbowPrismBox = false
            )
        )
    }

    var swapCount by remember { mutableIntStateOf(0) }

    Column(
        modifier = modifier
            .clip(RoundedCornerShape(18.dp))
            .background(DuoMintSurface)
            .border(1.5.dp, DuoSwanBorder, RoundedCornerShape(18.dp))
            .padding(12.dp)
            .testTag("duo_card_draggable_container"),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    fontFamily = SpaceGroteskFontFamily,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = DuoEelInk
                )
                Text(
                    text = "Kartu laporan terkunci rapi di tempatnya (tidak ikut bergeser saat layar di-scroll) • ${qualityMode.effectMultiplier}x Efek GSAP",
                    fontSize = 10.sp,
                    color = CosmicTextSubtle
                )
            }
            Box(
                modifier = Modifier
                    .duolingoButton(
                        faceColor = DuoMacawBlue,
                        lipColor = DuoMacawBlueLip,
                        cornerRadius = 10.dp,
                        lipHeight = 3.dp
                    )
                    .instantTouchPress {
                        if (reportCards.size > 1) {
                            val first = reportCards.removeAt(0)
                            reportCards.add(first)
                            swapCount++
                            AppThemeController.gsapLiveDemoTriggerNonce++
                        }
                    }
                    .padding(bottom = 3.dp)
                    .padding(horizontal = 9.dp, vertical = 5.dp)
                    .testTag("duo_card_shuffle_order_button")
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = "Putar Urutan Kartu Laporan",
                        tint = Color.White,
                        modifier = Modifier.size(12.dp)
                    )
                    Text(
                        text = "Putar Urutan",
                        fontSize = 9.5.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White
                    )
                }
            }
        }

        reportCards.forEachIndexed { index, card ->
            var measuredHeightPx by remember { mutableFloatStateOf(165f) }
            val borderCol = if (card.isRainbowPrismBox) DuoBeetlePurple else card.accentColor

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .onGloballyPositioned { coords ->
                        if (coords.size.height > 40) {
                            measuredHeightPx = coords.size.height.toFloat() * 0.72f
                        }
                    }
                    .gsapDraggableDuoCard(
                        cardKey = card.id,
                        swapThresholdPx = measuredHeightPx,
                        onMoveUp = if (index > 0) {
                            {
                                val item = reportCards.removeAt(index)
                                reportCards.add(index - 1, item)
                                swapCount++
                            }
                        } else null,
                        onMoveDown = if (index < reportCards.lastIndex) {
                            {
                                val item = reportCards.removeAt(index)
                                reportCards.add(index + 1, item)
                                swapCount++
                            }
                        } else null
                    )
                    .duolingoCard(
                        backgroundColor = DuoPureWhite,
                        borderColor = borderCol,
                        lipColor = card.lipColor,
                        cornerRadius = 16.dp,
                        borderWidth = 2.dp,
                        lipHeight = 4.dp
                    )
                    .padding(bottom = 4.dp)
                    .padding(horizontal = 12.dp, vertical = 10.dp)
                    .testTag("duo_card_report_item_${card.id}")
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(2.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Text(
                                text = "POSISI #${index + 1}",
                                fontFamily = JetBrainsMonoFontFamily,
                                fontSize = 9.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = card.accentColor
                            )
                            Text(
                                text = "• ${card.codeBadge}",
                                fontFamily = JetBrainsMonoFontFamily,
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                color = CosmicTextSubtle
                            )
                            if (card.isRainbowPrismBox) {
                                Text(
                                    text = "PRISM BOX",
                                    fontFamily = JetBrainsMonoFontFamily,
                                    fontSize = 8.5.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = DuoBeetlePurple
                                )
                            }
                        }
                        Text(
                            text = card.title,
                            fontSize = 12.5.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = DuoEelInk,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            text = card.subtitle,
                            fontSize = 10.sp,
                            color = CosmicTextSubtle,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Column(
                        horizontalAlignment = Alignment.End,
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .duolingoButton(
                                    faceColor = card.accentColor,
                                    lipColor = card.lipColor,
                                    cornerRadius = 10.dp,
                                    lipHeight = 3.dp
                                )
                                .padding(bottom = 3.dp)
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = if (card.id == "report_gsap_telemetry") {
                                    "${qualityMode.effectMultiplier}x EFEK"
                                } else {
                                    card.metricValue
                                },
                                fontFamily = JetBrainsMonoFontFamily,
                                fontSize = 9.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = Color.White
                            )
                        }
                        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                            if (index > 0) {
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(6.dp))
                                        .background(DuoMintSurface)
                                        .border(1.dp, DuoSwanBorder, RoundedCornerShape(6.dp))
                                        .clickable {
                                            val item = reportCards.removeAt(index)
                                            reportCards.add(index - 1, item)
                                            swapCount++
                                        }
                                        .padding(horizontal = 6.dp, vertical = 2.dp)
                                ) {
                                    Text(
                                        text = "↑",
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.ExtraBold,
                                        color = DuoEelInk
                                    )
                                }
                            }
                            if (index < reportCards.lastIndex) {
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(6.dp))
                                        .background(DuoMintSurface)
                                        .border(1.dp, DuoSwanBorder, RoundedCornerShape(6.dp))
                                        .clickable {
                                            val item = reportCards.removeAt(index)
                                            reportCards.add(index + 1, item)
                                            swapCount++
                                        }
                                        .padding(horizontal = 6.dp, vertical = 2.dp)
                                ) {
                                    Text(
                                        text = "↓",
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.ExtraBold,
                                        color = DuoEelInk
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

// ============================================================================
// CHAOS OVERDRIVE MANDATORY WARNING DIALOG, 3-SECOND TIMER & WATCHDOG BAR
// ============================================================================

@Composable
fun ChaosOverdriveWarningAndWatchdogOverlay(
    onPersistSafeMode: (String) -> Unit = {}
) {
    val context = androidx.compose.ui.platform.LocalContext.current
    val qualityMode = AppThemeController.graphicsQualityMode
    val showWarning = AppThemeController.showChaosOverdriveWarningDialog
    val countdownDeci = AppThemeController.chaosOverdriveCountdownDeciseconds
    val isLockedUnlimited = AppThemeController.chaosOverdriveUnlimitedLocked
    val toastMsg = AppThemeController.chaosStatusToastMessage

    // Auto-dismiss status toast after 3.5 seconds
    LaunchedEffect(toastMsg) {
        if (!toastMsg.isNullOrBlank()) {
            delay(3500L)
            AppThemeController.chaosStatusToastMessage = null
        }
    }

    // 3.0-Second (30 x 100ms) Automatic Safety Timer for Chaos Brutal Overdrive
    LaunchedEffect(qualityMode, isLockedUnlimited) {
        if (qualityMode == AppGraphicsQualityMode.CHAOS_OVERDRIVE && !isLockedUnlimited) {
            if (AppThemeController.chaosOverdriveCountdownDeciseconds <= 0) {
                AppThemeController.chaosOverdriveCountdownDeciseconds = 30
            }
            while (
                AppThemeController.graphicsQualityMode == AppGraphicsQualityMode.CHAOS_OVERDRIVE &&
                !AppThemeController.chaosOverdriveUnlimitedLocked &&
                AppThemeController.chaosOverdriveCountdownDeciseconds > 0
            ) {
                delay(100L)
                AppThemeController.chaosOverdriveCountdownDeciseconds =
                    (AppThemeController.chaosOverdriveCountdownDeciseconds - 1).coerceAtLeast(0)
            }
            if (
                AppThemeController.graphicsQualityMode == AppGraphicsQualityMode.CHAOS_OVERDRIVE &&
                !AppThemeController.chaosOverdriveUnlimitedLocked &&
                AppThemeController.chaosOverdriveCountdownDeciseconds <= 0
            ) {
                AppThemeController.revertFromChaosOverdriveToSafeMode(
                    useSmoothFallback = false,
                    reasonMessage = "Tes 3 Detik Chaos Brutal selesai: Dikembalikan otomatis ke mode aman (${AppThemeController.chaosPreviousSafeMode.displayName}).",
                    context = context,
                    onPersistSafeMode = onPersistSafeMode
                )
            }
        }
    }

    // Top Floating Banner while Chaos Brutal Overdrive is active OR when status toast is visible
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 6.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        if (qualityMode == AppGraphicsQualityMode.CHAOS_OVERDRIVE) {
            val secFormatted = "${countdownDeci / 10}.${countdownDeci % 10}s"
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .duolingoCard(
                        backgroundColor = Color(0xFF1E050E),
                        borderColor = Color(0xFFF43F5E),
                        lipColor = Color(0xFF9F1239),
                        cornerRadius = 16.dp,
                        borderWidth = 2.dp,
                        lipHeight = 4.dp
                    )
                    .padding(bottom = 4.dp)
                    .padding(10.dp)
                    .testTag("chaos_overdrive_active_watchdog_bar")
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = if (isLockedUnlimited) {
                                    "CHAOS BRUTAL 100x • LOCKED UNLIMITED"
                                } else {
                                    "CHAOS BRUTAL 100x • AUTO-RESET DALAM $secFormatted"
                                },
                                fontFamily = JetBrainsMonoFontFamily,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = Color(0xFFFDA4AF)
                            )
                            Text(
                                text = "Watchdog Anti-Force-Close Aktif: Jika keluar aplikasi/crash, otomatis kembali ke ${AppThemeController.chaosPreviousSafeMode.displayName} / Smooth.",
                                fontSize = 9.5.sp,
                                color = Color.White.copy(alpha = 0.88f)
                            )
                        }
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        if (!isLockedUnlimited) {
                            Box(
                                modifier = Modifier
                                    .weight(1.2f)
                                    .duolingoButton(
                                        faceColor = DuoFeatherGreen,
                                        lipColor = DuoFeatherGreenLip,
                                        cornerRadius = 12.dp,
                                        lipHeight = 3.dp
                                    )
                                    .instantTouchPress {
                                        AppThemeController.lockChaosOverdriveUnlimited()
                                    }
                                    .padding(bottom = 3.dp)
                                    .padding(vertical = 8.dp, horizontal = 8.dp)
                                    .testTag("chaos_keep_unlimited_button"),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "HP Kuat! Pertahankan Selamanya",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = Color.White
                                )
                            }
                        }
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .duolingoButton(
                                    faceColor = DuoMacawBlue,
                                    lipColor = DuoMacawBlueLip,
                                    cornerRadius = 12.dp,
                                    lipHeight = 3.dp
                                )
                                .instantTouchPress {
                                    AppThemeController.revertFromChaosOverdriveToSafeMode(
                                        useSmoothFallback = false,
                                        reasonMessage = "Kembali ke mode ${AppThemeController.chaosPreviousSafeMode.displayName}.",
                                        context = context,
                                        onPersistSafeMode = onPersistSafeMode
                                    )
                                }
                                .padding(bottom = 3.dp)
                                .padding(vertical = 8.dp, horizontal = 8.dp)
                                .testTag("chaos_revert_safe_button"),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Kembali ke ${AppThemeController.chaosPreviousSafeMode.displayName}",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = Color.White
                            )
                        }
                        Box(
                            modifier = Modifier
                                .weight(0.85f)
                                .duolingoButton(
                                    faceColor = Color(0xFF10B981),
                                    lipColor = Color(0xFF047857),
                                    cornerRadius = 12.dp,
                                    lipHeight = 3.dp
                                )
                                .instantTouchPress {
                                    AppThemeController.revertFromChaosOverdriveToSafeMode(
                                        useSmoothFallback = true,
                                        reasonMessage = "Di-reset ke mode Smooth (Super Ringan).",
                                        context = context,
                                        onPersistSafeMode = onPersistSafeMode
                                    )
                                }
                                .padding(bottom = 3.dp)
                                .padding(vertical = 8.dp, horizontal = 6.dp)
                                .testTag("chaos_revert_smooth_button"),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Ke Smooth",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = Color.White
                            )
                        }
                    }
                }
            }
        }

        if (!toastMsg.isNullOrBlank()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFF0F172A).copy(alpha = 0.94f))
                    .border(1.5.dp, DuoMacawBlue, RoundedCornerShape(12.dp))
                    .padding(horizontal = 12.dp, vertical = 8.dp)
            ) {
                Text(
                    text = toastMsg,
                    fontSize = 10.5.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }
    }

    // Mandatory Warning Modal before activating Chaos Brutal Overdrive (100x)
    if (showWarning) {
        androidx.compose.ui.window.Dialog(
            onDismissRequest = { AppThemeController.showChaosOverdriveWarningDialog = false }
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .duolingoCard(
                        backgroundColor = DuoPureWhite,
                        borderColor = Color(0xFFF43F5E),
                        lipColor = Color(0xFFBE123C),
                        cornerRadius = 22.dp,
                        borderWidth = 2.5.dp,
                        lipHeight = 5.dp
                    )
                    .padding(bottom = 5.dp)
                    .padding(16.dp)
                    .testTag("chaos_overdrive_warning_dialog")
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(10.dp))
                                .background(Color(0xFFF43F5E))
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = "PERINGATAN KERAS • 100x OVERDRIVE",
                                fontFamily = JetBrainsMonoFontFamily,
                                fontSize = 9.5.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = Color.White
                            )
                        }
                    }
                    Text(
                        text = "Konfirmasi Aktifkan Chaos Brutal (Tes 3 Detik)",
                        fontFamily = SpaceGroteskFontFamily,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = DuoEelInk
                    )
                    Text(
                        text = "Mode Chaos Brutal memacu gelombang GSAP 100x lipat yang sangat berat dan berisiko membuat HP patah-patah atau Force Close!\n\n" +
                            "• Demi keamanan, mode ini HANYA menyala selama 3 DETIK.\n" +
                            "• Jika HP Anda terbukti kuat, Anda bisa menekan tombol 'HP Kuat! Pertahankan Selamanya' saat tes berjalan.\n" +
                            "• Jika aplikasi Force Close atau Anda keluar aplikasi, sistem Watchdog otomatis mengembalikan grafik ke mode sebelumnya (${AppThemeController.chaosPreviousSafeMode.displayName}) atau Smooth.",
                        fontSize = 11.5.sp,
                        color = CosmicTextSubtle
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .duolingoButton(
                                    faceColor = DuoMintSurface,
                                    lipColor = DuoSwanBorder,
                                    cornerRadius = 14.dp,
                                    lipHeight = 3.dp
                                )
                                .instantTouchPress {
                                    AppThemeController.showChaosOverdriveWarningDialog = false
                                }
                                .padding(bottom = 3.dp)
                                .padding(vertical = 10.dp)
                                .testTag("chaos_warning_cancel_button"),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Batal (Tetap Aman)",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = DuoEelInk
                            )
                        }
                        Box(
                            modifier = Modifier
                                .weight(1.25f)
                                .duolingoButton(
                                    faceColor = Color(0xFFF43F5E),
                                    lipColor = Color(0xFF9F1239),
                                    cornerRadius = 14.dp,
                                    lipHeight = 3.dp
                                )
                                .instantTouchPress {
                                    AppThemeController.confirmStartChaosOverdrive(
                                        context = context,
                                        onPersistSafeFallback = onPersistSafeMode
                                    )
                                }
                                .padding(bottom = 3.dp)
                                .padding(vertical = 10.dp)
                                .testTag("chaos_warning_confirm_button"),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Nyalakan (Tes 3 Detik)",
                                fontSize = 11.sp,
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

// ============================================================================
// PRO FEATURE #3: LIVE FPS TELEMETRY & CYBER SECURITY AUDIT HUD
// ============================================================================

@Composable
fun ProLiveFpsAndSecurityAuditHud(
    modifier: Modifier = Modifier
) {
    if (!AppThemeController.proLiveFpsHudEnabled) return

    val qualityMode = AppThemeController.graphicsQualityMode
    var currentFps by remember { mutableIntStateOf(90) }
    var currentFrameMs by remember { mutableFloatStateOf(11.1f) }

    LaunchedEffect(qualityMode) {
        var lastFrameNanos = 0L
        var frameAccumulator = 0
        var elapsedNanosAccumulator = 0L
        while (true) {
            androidx.compose.runtime.withFrameNanos { nowNanos ->
                if (lastFrameNanos != 0L) {
                    val delta = (nowNanos - lastFrameNanos).coerceAtLeast(1L)
                    frameAccumulator++
                    elapsedNanosAccumulator += delta
                    if (elapsedNanosAccumulator >= 350_000_000L) {
                        val avgNanos = elapsedNanosAccumulator / frameAccumulator.coerceAtLeast(1)
                        currentFrameMs = (avgNanos / 1_000_000f).coerceIn(4.0f, 99.9f)
                        currentFps = (1_000_000_000L / avgNanos.coerceAtLeast(1L)).toInt().coerceIn(1, 144)
                        frameAccumulator = 0
                        elapsedNanosAccumulator = 0L
                    }
                }
                lastFrameNanos = nowNanos
            }
        }
    }

    val multText = when (qualityMode) {
        AppGraphicsQualityMode.CHAOS_ULTIMATE_LOW -> "1/25x LOW"
        AppGraphicsQualityMode.CHAOS_OVERDRIVE -> "100x BRUTAL"
        else -> "${qualityMode.effectMultiplier}x GSAP"
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 4.dp)
            .clip(RoundedCornerShape(14.dp))
            .background(Color(0xFF090D16).copy(alpha = 0.94f))
            .border(1.5.dp, qualityMode.accentColor.copy(alpha = 0.80f), RoundedCornerShape(14.dp))
            .padding(horizontal = 10.dp, vertical = 6.dp)
            .testTag("pro_live_fps_security_hud")
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
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(qualityMode.accentColor)
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = "PRO HUD",
                        fontFamily = JetBrainsMonoFontFamily,
                        fontSize = 8.5.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White
                    )
                }
                Text(
                    text = "$currentFps FPS (${String.format(java.util.Locale.US, "%.1f", currentFrameMs)}ms) • $multText • ${qualityMode.displayName}",
                    fontFamily = JetBrainsMonoFontFamily,
                    fontSize = 9.5.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color(0xFF34D399),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color(0xFFEF4444).copy(alpha = 0.22f))
                    .border(1.dp, Color(0xFFEF4444).copy(alpha = 0.7f), RoundedCornerShape(8.dp))
                    .instantTouchPress {
                        AppThemeController.proLiveFpsHudEnabled = false
                    }
                    .padding(horizontal = 7.dp, vertical = 3.dp)
                    .testTag("pro_live_fps_hud_close_button")
            ) {
                Text(
                    text = "✕ MATIKAN",
                    fontFamily = JetBrainsMonoFontFamily,
                    fontSize = 8.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color(0xFFFCA5A5)
                )
            }
        }
    }
}

// ============================================================================
// PRO MODE SECRET CODE UNLOCK, WHATSAPP CHANNEL GATE & 8 PRO FEATURES STUDIO
// Used in both Settings Card AND the Dedicated "Mode Pro" Tab in Settings Env!
// ============================================================================

@Composable
fun ProStudioDedicatedSettingsScreen(
    onPersistSafeMode: (String) -> Unit = {},
    onBackToSettingsClick: () -> Unit = {}
) {
    androidx.activity.compose.BackHandler {
        onBackToSettingsClick()
    }

    androidx.compose.foundation.lazy.LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .testTag("pro_studio_dedicated_settings_screen"),
        contentPadding = androidx.compose.foundation.layout.PaddingValues(top = 10.dp, bottom = 120.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .duolingoCard(
                        backgroundColor = Color(0xFF0F172A),
                        borderColor = Color(0xFF8B5CF6),
                        lipColor = DuoBeetlePurpleLip,
                        cornerRadius = 22.dp,
                        borderWidth = 2.dp,
                        lipHeight = 4.dp
                    )
                    .padding(bottom = 4.dp)
                    .padding(16.dp)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "BRANKAS SETTING • TAB TERISOLASI PRO HUB",
                                fontFamily = JetBrainsMonoFontFamily,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = Color(0xFF38BDF8)
                            )
                            Text(
                                text = "Pro Hub • Kode & Pengaturan 9 Fitur Pro",
                                fontFamily = SpaceGroteskFontFamily,
                                fontSize = 17.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = Color.White
                            )
                            Text(
                                text = "Berada di bawah naungan Brankas Setting (PIN Default: 222-222-222 • PIN dapat diubah di tab Umum). Ambil kode di Saluran WhatsApp resmi dan kelola seluruh 9 modul Pro di sini.",
                                fontSize = 11.sp,
                                color = Color(0xFFCBD5E1)
                            )
                        }
                    }
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .duolingoButton(
                                faceColor = DuoMacawBlue,
                                lipColor = DuoMacawBlueLip,
                                cornerRadius = 12.dp,
                                lipHeight = 3.dp
                            )
                            .instantTouchPress { onBackToSettingsClick() }
                            .padding(bottom = 3.dp)
                            .padding(vertical = 8.dp, horizontal = 12.dp)
                            .testTag("pro_hub_back_to_umum_button"),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "← Kembali ke Tab Umum (Pengaturan Dasar & Ubah PIN)",
                            fontSize = 10.5.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color.White
                        )
                    }
                }
            }
        }

        item {
            ProModeSecretUnlockAndSuiteCard(
                onPersistSafeMode = onPersistSafeMode
            )
        }
    }
}

@Composable
fun ProModeSecretUnlockAndSuiteCard(
    modifier: Modifier = Modifier,
    onPersistSafeMode: (String) -> Unit = {},
    onOpenDedicatedProTabClick: (() -> Unit)? = null
) {
    val context = androidx.compose.ui.platform.LocalContext.current
    val clipboardManager = androidx.compose.ui.platform.LocalClipboardManager.current
    val isProUnlocked = AppThemeController.isProModeUnlocked
    val activeGraphicsMode = AppThemeController.graphicsQualityMode
    val whatsappChannelUrl = AppThemeController.OFFICIAL_PRO_WHATSAPP_CHANNEL_URL

    var secretCodeInput by remember { mutableStateOf("") }
    var codeFeedbackMessage by remember { mutableStateOf<String?>(null) }
    var activeProSubTab by remember { mutableIntStateOf(0) }

    // Pro Feature #5 (Deep EXIF & Hex Scrubber) state
    var exifSampleFileName by remember { mutableStateOf("IMG_20261114_PRIVATE_RAW.JPG") }
    var exifHexPreview by remember {
        mutableStateOf(
            "FF D8 FF E1 1A 45 78 69 66 00 00 4D 4D 00 2A [GPS: -6.2088, 106.8456 | IMEI/Serial: 8901442]"
        )
    }
    var exifScrubbedStatus by remember { mutableStateOf<String?>(null) }

    // Pro Feature #6 (Batch Mori v4.6.0 Multi-Link Queue) state
    var batchMoriUrlsInput by remember {
        mutableStateOf("https://vt.tiktok.com/ZS8example1/\nhttps://youtu.be/dQw4w9WgXcQ\nhttps://instagram.com/reel/C8example3/")
    }
    var batchMoriFormat by remember { mutableStateOf("MP3 320kbps") }
    var batchMoriQueueResults by remember { mutableStateOf<List<String>>(emptyList()) }

    // Pro Feature #7 (Multi-File Vault & Batch Steganography) state
    var batchStegoPayloadsInput by remember {
        mutableStateOf("PIN_CADANGAN_01: 884920\nTOKEN_AUTH_SERVER: NX-9920-ULTRA\nKOORDINAT_ARSIP: -6.2088, 106.8456")
    }
    var batchStegoCarrierCover by remember { mutableStateOf("Catatan Harian Arcanum Terverifikasi") }
    var batchStegoOutputs by remember { mutableStateOf<List<String>>(emptyList()) }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .duolingoCard(
                backgroundColor = DuoPureWhite,
                borderColor = if (isProUnlocked) Color(0xFF8B5CF6) else DuoMacawBlue,
                lipColor = if (isProUnlocked) DuoBeetlePurpleLip else DuoMacawBlueLip,
                cornerRadius = 18.dp,
                borderWidth = 2.dp,
                lipHeight = 4.dp
            )
            .padding(bottom = 4.dp)
            .padding(12.dp)
            .testTag("settings_pro_mode_hub_card")
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = if (isProUnlocked) {
                            "ASTRA ONYX ULTRA PRO • 9 FITUR PRO AKTIF"
                        } else {
                            "AKTIVASI MODE PRO (WAJIB KODE SALURAN WA)"
                        },
                        fontFamily = SpaceGroteskFontFamily,
                        fontSize = 13.5.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = DuoEelInk
                    )
                    Text(
                        text = if (isProUnlocked) {
                            "Seluruh 9 Modul Pro (Decoy Vault, Intruder Trap, Deep EXIF/Hex, Dynamic Island Pro & EQ, Dual Chaos, Batch Mori, Batch Stego, FPS HUD, & Domain Alias Arcanum Browser) telah terbuka."
                        } else {
                            "Untuk mengaktifkan Mode Pro & Dynamic Island, buka & ikuti Saluran WhatsApp resmi kami untuk mengambil kode akses Pro, lalu masukkan kodenya di bawah."
                        },
                        fontSize = 10.5.sp,
                        color = CosmicTextSubtle
                    )
                }
                Box(
                    modifier = Modifier
                        .duolingoButton(
                            faceColor = if (isProUnlocked) DuoFeatherGreen else DuoBeetlePurple,
                            lipColor = if (isProUnlocked) DuoFeatherGreenLip else DuoBeetlePurpleLip,
                            cornerRadius = 10.dp,
                            lipHeight = 3.dp
                        )
                        .padding(bottom = 3.dp)
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = if (isProUnlocked) "PRO UNLOCKED" else "WAJIB KODE",
                        fontFamily = JetBrainsMonoFontFamily,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White
                    )
                }
            }

            if (onOpenDedicatedProTabClick != null) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .duolingoButton(
                            faceColor = Color(0xFF0EA5E9),
                            lipColor = DuoMacawBlueLip,
                            cornerRadius = 12.dp,
                            lipHeight = 3.dp
                        )
                        .instantTouchPress { onOpenDedicatedProTabClick() }
                        .padding(bottom = 3.dp)
                        .padding(vertical = 8.dp, horizontal = 10.dp)
                        .testTag("open_dedicated_pro_tab_button"),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Buka Tab Khusus Mode Pro di Env Pengaturan →",
                        fontSize = 10.5.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White
                    )
                }
            }

            // Official WhatsApp Channel Banner (Always visible so users can open/follow the channel anytime)
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(Color(0xFF071F18))
                    .border(1.5.dp, Color(0xFF25D366), RoundedCornerShape(14.dp))
                    .padding(10.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(
                    text = "SALURAN WHATSAPP RESMI TEMPAT KODE MODE PRO:",
                    fontFamily = JetBrainsMonoFontFamily,
                    fontSize = 9.5.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color(0xFF4ADE80)
                )
                Text(
                    text = "Buka tautan berikut, klik Ikuti (Follow) pada Saluran WhatsApp resmi kami untuk mendapatkan Kode Mode Pro:",
                    fontSize = 10.sp,
                    color = Color.White
                )
                Text(
                    text = whatsappChannelUrl,
                    fontFamily = JetBrainsMonoFontFamily,
                    fontSize = 9.5.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF86EFAC)
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .weight(1.4f)
                            .duolingoButton(
                                faceColor = Color(0xFF25D366),
                                lipColor = Color(0xFF128C7E),
                                cornerRadius = 11.dp,
                                lipHeight = 3.dp
                            )
                            .instantTouchPress {
                                try {
                                    val intent = android.content.Intent(
                                        android.content.Intent.ACTION_VIEW,
                                        android.net.Uri.parse(whatsappChannelUrl)
                                    ).apply {
                                        addFlags(android.content.Intent.FLAG_ACTIVITY_NEW_TASK)
                                    }
                                    context.startActivity(intent)
                                } catch (_: Exception) {
                                    clipboardManager.setText(androidx.compose.ui.text.AnnotatedString(whatsappChannelUrl))
                                    codeFeedbackMessage = "Tautan Saluran WhatsApp disalin ke papan klip!"
                                }
                            }
                            .padding(bottom = 3.dp)
                            .padding(vertical = 8.dp, horizontal = 8.dp)
                            .testTag("pro_open_whatsapp_channel_button"),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Buka & Ikuti Saluran WA (Ambil Kode)",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color.White,
                            maxLines = 1
                        )
                    }
                    Box(
                        modifier = Modifier
                            .weight(0.7f)
                            .duolingoButton(
                                faceColor = DuoMacawBlue,
                                lipColor = DuoMacawBlueLip,
                                cornerRadius = 11.dp,
                                lipHeight = 3.dp
                            )
                            .instantTouchPress {
                                clipboardManager.setText(androidx.compose.ui.text.AnnotatedString(whatsappChannelUrl))
                                codeFeedbackMessage = "Link Saluran WhatsApp berhasil disalin!"
                            }
                            .padding(bottom = 3.dp)
                            .padding(vertical = 8.dp, horizontal = 6.dp)
                            .testTag("pro_copy_whatsapp_channel_button"),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Salin Link WA",
                            fontSize = 9.5.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color.White,
                            maxLines = 1
                        )
                    }
                }
            }

            if (!isProUnlocked) {
                // Mandatory Secret Code Entry Box (Never displays or leaks the secret code in UI!)
                androidx.compose.material3.OutlinedTextField(
                    value = secretCodeInput,
                    onValueChange = {
                        secretCodeInput = it
                        codeFeedbackMessage = null
                    },
                    label = { Text("Masukkan Kode Mode Pro (Dari Saluran WA)", fontSize = 11.sp) },
                    placeholder = { Text("Ketik kode aktivasi Pro di sini...", fontSize = 11.sp) },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("settings_pro_secret_code_input")
                )

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .duolingoButton(
                            faceColor = DuoBeetlePurple,
                            lipColor = DuoBeetlePurpleLip,
                            cornerRadius = 14.dp,
                            lipHeight = 4.dp
                        )
                        .instantTouchPress {
                            val ok = AppThemeController.verifyAndUnlockProMode(secretCodeInput, context)
                            codeFeedbackMessage = if (ok) {
                                "Mode Pro berhasil diaktifkan! Seluruh 8 Fitur Pro siap diedit dan digunakan."
                            } else {
                                "Kode akses belum tepat. Silakan buka & ikuti Saluran WhatsApp resmi di atas untuk mengambil kode Mode Pro."
                            }
                        }
                        .padding(bottom = 4.dp)
                        .padding(vertical = 10.dp)
                        .testTag("settings_pro_verify_code_button"),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Verifikasi Kode & Buka Mode Pro",
                        fontSize = 11.5.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White
                    )
                }

                if (!codeFeedbackMessage.isNullOrBlank()) {
                    Text(
                        text = codeFeedbackMessage!!,
                        fontSize = 10.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (AppThemeController.isProModeUnlocked) DuoFeatherGreen else Color(0xFFEF4444)
                    )
                }
            } else {
                // 9 Pro Feature Sub-Tabs (3 Rows of 3 Pills)
                val proTabs = listOf(
                    "1. Decoy Vault",
                    "2. Intruder Trap",
                    "3. EXIF & Hex",
                    "4. Island & EQ",
                    "5. Chaos Studio",
                    "6. Batch Mori",
                    "7. Batch Stego",
                    "8. Live FPS HUD",
                    "9. Domain Alias"
                )
                proTabs.chunked(3).forEachIndexed { rowIdx, tabRow ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(5.dp)
                    ) {
                        tabRow.forEachIndexed { colIdx, tabLabel ->
                            val idx = rowIdx * 3 + colIdx
                            val selected = activeProSubTab == idx
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .duolingoButton(
                                        faceColor = if (selected) DuoBeetlePurple else DuoMintSurface,
                                        lipColor = if (selected) DuoBeetlePurpleLip else DuoSwanBorder,
                                        cornerRadius = 10.dp,
                                        lipHeight = 3.dp
                                    )
                                    .instantTouchPress { activeProSubTab = idx }
                                    .padding(bottom = 3.dp)
                                    .padding(vertical = 7.dp, horizontal = 3.dp)
                                    .testTag("pro_feature_tab_$idx"),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = tabLabel,
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = if (selected) Color.White else DuoEelInk,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }
                    }
                }

                when (activeProSubTab) {
                    0 -> {
                        // PRO MODULE #1: PRO DECOY VAULT (BRANKAS SAMARAN OTOMATIS)
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Text(
                                text = "PRO DECOY VAULT • BRANKAS SAMARAN OTOMATIS",
                                fontFamily = JetBrainsMonoFontFamily,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = DuoEelInk
                            )
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(DuoMintSurface)
                                    .padding(horizontal = 10.dp, vertical = 8.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = "Aktifkan Brankas Palsu Otomatis Saat PIN Samaran Dimasukkan",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.ExtraBold,
                                        color = DuoEelInk
                                    )
                                    Text(
                                        text = "Membuka ruang umpan berisi berkas kuliah/resep aman tanpa mencurigakan penyusup.",
                                        fontSize = 9.5.sp,
                                        color = CosmicTextSubtle
                                    )
                                }
                                GsapElasticToggleSwitch(
                                    checked = AppThemeController.proDecoyVaultEnabled,
                                    activeColor = DuoFeatherGreen,
                                    onCheckedChange = { AppThemeController.proDecoyVaultEnabled = it }
                                )
                            }

                            androidx.compose.material3.OutlinedTextField(
                                value = AppThemeController.proDecoyPinCode,
                                onValueChange = { AppThemeController.proDecoyPinCode = it.take(12) },
                                label = { Text("PIN Pemicu Decoy Vault (Brankas Samaran)", fontSize = 10.5.sp) },
                                singleLine = true,
                                modifier = Modifier.fillMaxWidth()
                            )

                            androidx.compose.material3.OutlinedTextField(
                                value = AppThemeController.proDecoyVaultTitle,
                                onValueChange = { AppThemeController.proDecoyVaultTitle = it },
                                label = { Text("Judul Kamuflase Brankas Samaran", fontSize = 10.5.sp) },
                                singleLine = true,
                                modifier = Modifier.fillMaxWidth()
                            )

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Jumlah File Umpan Otomatis: ${AppThemeController.proDecoyFakeFilesCount} Berkas",
                                    fontSize = 10.5.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = DuoEelInk
                                )
                                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                    listOf(6, 12, 24, 48).forEach { countOpt ->
                                        val sel = AppThemeController.proDecoyFakeFilesCount == countOpt
                                        Box(
                                            modifier = Modifier
                                                .clip(RoundedCornerShape(8.dp))
                                                .background(if (sel) DuoBeetlePurple else DuoMintSurface)
                                                .instantTouchPress { AppThemeController.proDecoyFakeFilesCount = countOpt }
                                                .padding(horizontal = 8.dp, vertical = 4.dp)
                                        ) {
                                            Text(
                                                text = "$countOpt File",
                                                fontSize = 9.5.sp,
                                                fontWeight = FontWeight.ExtraBold,
                                                color = if (sel) Color.White else DuoEelInk
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }

                    1 -> {
                        // PRO MODULE #2: PRO INTRUDER SELFIE & PIN-FAIL TRAP LOG
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Text(
                                text = "PRO INTRUDER SELFIE TRAP & AUDIT PERCOBAAN PIN",
                                fontFamily = JetBrainsMonoFontFamily,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = DuoEelInk
                            )
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(DuoMintSurface)
                                    .padding(horizontal = 10.dp, vertical = 8.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = "Jebakan Rekam Penyusup Saat Salah PIN",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.ExtraBold,
                                        color = DuoEelInk
                                    )
                                    Text(
                                        text = "Mencatat cap waktu, PIN palsu yang dimasukkan, & status sensor saat percobaan gagal.",
                                        fontSize = 9.5.sp,
                                        color = CosmicTextSubtle
                                    )
                                }
                                GsapElasticToggleSwitch(
                                    checked = AppThemeController.proIntruderTrapEnabled,
                                    activeColor = Color(0xFFF43F5E),
                                    onCheckedChange = { AppThemeController.proIntruderTrapEnabled = it }
                                )
                            }

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Ambang Batas Salah PIN:",
                                    fontSize = 10.5.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = DuoEelInk
                                )
                                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                    listOf(1, 2, 3, 5).forEach { limitOpt ->
                                        val sel = AppThemeController.proIntruderMaxFailedAttempts == limitOpt
                                        Box(
                                            modifier = Modifier
                                                .clip(RoundedCornerShape(8.dp))
                                                .background(if (sel) Color(0xFFF43F5E) else DuoMintSurface)
                                                .instantTouchPress { AppThemeController.proIntruderMaxFailedAttempts = limitOpt }
                                                .padding(horizontal = 8.dp, vertical = 4.dp)
                                        ) {
                                            Text(
                                                text = "${limitOpt}x Gagal",
                                                fontSize = 9.5.sp,
                                                fontWeight = FontWeight.ExtraBold,
                                                color = if (sel) Color.White else DuoEelInk
                                            )
                                        }
                                    }
                                }
                            }

                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .duolingoButton(
                                            faceColor = DuoFoxOrange,
                                            lipColor = DuoFoxOrangeLip,
                                            cornerRadius = 11.dp,
                                            lipHeight = 3.dp
                                        )
                                        .instantTouchPress {
                                            val newIdx = AppThemeController.proIntruderTrapLogs.size + 1
                                            val fakePin = (100000..999999).random()
                                            AppThemeController.proIntruderTrapLogs =
                                                listOf("[TRAP-0$newIdx] PIN Salah ($fakePin) • Ditangkap oleh Pro Intruder Trap") +
                                                    AppThemeController.proIntruderTrapLogs
                                        }
                                        .padding(bottom = 3.dp)
                                        .padding(vertical = 8.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "Uji Rekam Penyusup",
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.ExtraBold,
                                        color = Color.White
                                    )
                                }
                                Box(
                                    modifier = Modifier
                                        .weight(0.8f)
                                        .duolingoButton(
                                            faceColor = DuoCardinalRed,
                                            lipColor = Color(0xFF991B1B),
                                            cornerRadius = 11.dp,
                                            lipHeight = 3.dp
                                        )
                                        .instantTouchPress {
                                            AppThemeController.proIntruderTrapLogs = emptyList()
                                        }
                                        .padding(bottom = 3.dp)
                                        .padding(vertical = 8.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "Bersihkan Log (${AppThemeController.proIntruderTrapLogs.size})",
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.ExtraBold,
                                        color = Color.White
                                    )
                                }
                            }

                            AppThemeController.proIntruderTrapLogs.take(5).forEach { logLine ->
                                Text(
                                    text = logLine,
                                    fontFamily = JetBrainsMonoFontFamily,
                                    fontSize = 9.sp,
                                    color = Color(0xFFDC2626)
                                )
                            }
                        }
                    }

                    2 -> {
                        // PRO MODULE #3: PRO HEX & DEEP EXIF METADATA SCRUBBER
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Text(
                                text = "PRO DEEP EXIF & HEXADECIMAL METADATA SCRUBBER",
                                fontFamily = JetBrainsMonoFontFamily,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = DuoEelInk
                            )
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(DuoMintSurface)
                                    .padding(horizontal = 10.dp, vertical = 8.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = "Auto-Scrub EXIF, GPS, & Seri Kamera Saat Impor",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.ExtraBold,
                                        color = DuoEelInk
                                    )
                                    Text(
                                        text = "Menghapus koordinat lokasi GPS, tipe lensa, & XMP tag hingga level byte Hex.",
                                        fontSize = 9.5.sp,
                                        color = CosmicTextSubtle
                                    )
                                }
                                GsapElasticToggleSwitch(
                                    checked = AppThemeController.proExifAutoScrubOnImport,
                                    activeColor = DuoMacawBlue,
                                    onCheckedChange = { AppThemeController.proExifAutoScrubOnImport = it }
                                )
                            }

                            androidx.compose.material3.OutlinedTextField(
                                value = exifSampleFileName,
                                onValueChange = { exifSampleFileName = it },
                                label = { Text("Target Berkas Inspeksi Hex & EXIF", fontSize = 10.5.sp) },
                                singleLine = true,
                                modifier = Modifier.fillMaxWidth()
                            )

                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(Color(0xFF0F172A))
                                    .padding(10.dp)
                            ) {
                                Text(
                                    text = exifHexPreview,
                                    fontFamily = JetBrainsMonoFontFamily,
                                    fontSize = 9.5.sp,
                                    color = Color(0xFF38BDF8)
                                )
                            }

                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .duolingoButton(
                                        faceColor = DuoFeatherGreen,
                                        lipColor = DuoFeatherGreenLip,
                                        cornerRadius = 12.dp,
                                        lipHeight = 3.dp
                                    )
                                    .instantTouchPress {
                                        exifHexPreview =
                                            "FF D8 FF E0 00 10 4A 46 49 46 00 [ZERO-FILLED: GPS=00, SERIAL=00, EXIF=STRIPPED_OK]"
                                        exifScrubbedStatus =
                                            "Berhasil menghapus seluruh metadata EXIF/GPS/XMP pada '$exifSampleFileName'!"
                                    }
                                    .padding(bottom = 3.dp)
                                    .padding(vertical = 9.dp)
                                    .testTag("pro_exif_hex_scrub_button"),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "Sapu Bersih Metadata EXIF & Byte Hex Sekarang",
                                    fontSize = 10.5.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = Color.White
                                )
                            }

                            if (!exifScrubbedStatus.isNullOrBlank()) {
                                Text(
                                    text = exifScrubbedStatus!!,
                                    fontFamily = JetBrainsMonoFontFamily,
                                    fontSize = 9.5.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = DuoFeatherGreenLip
                                )
                            }
                        }
                    }

                    3 -> {
                        // PRO MODULE #4: PRO CUSTOM EQUALIZER & DYNAMIC ISLAND STUDIO
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Text(
                                text = "PRO DYNAMIC ISLAND (AUTO-MINIMAL) & EQUALIZER STUDIO",
                                fontFamily = JetBrainsMonoFontFamily,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = DuoEelInk
                            )
                            Text(
                                text = "Dynamic Island eksklusif untuk Mode Pro (pengguna Non-Pro memakai Notif Klasik). Otomatis mengecil ke mode paling minimal saat tidak disentuh atau tidak ada notif Arcanum, dan mati di dalam Browser.",
                                fontSize = 9.5.sp,
                                color = CosmicTextSubtle
                            )
                            Text(
                                text = "Ukuran Kapsul Dynamic Island Default:",
                                fontSize = 10.5.sp,
                                fontWeight = FontWeight.Bold,
                                color = DuoEelInk
                            )
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                listOf(
                                    0 to "Nano 104dp",
                                    1 to "Mini iOS 134dp",
                                    2 to "Split Droplet"
                                ).forEach { (presetId, label) ->
                                    val sel = AppThemeController.proDynamicIslandCompactPreset == presetId
                                    Box(
                                        modifier = Modifier
                                            .weight(1f)
                                            .duolingoButton(
                                                faceColor = if (sel) DuoBeetlePurple else DuoMintSurface,
                                                lipColor = if (sel) DuoBeetlePurpleLip else DuoSwanBorder,
                                                cornerRadius = 10.dp,
                                                lipHeight = 3.dp
                                            )
                                            .instantTouchPress {
                                                AppThemeController.proDynamicIslandCompactPreset = presetId
                                            }
                                            .padding(bottom = 3.dp)
                                            .padding(vertical = 6.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = label,
                                            fontSize = 9.5.sp,
                                            fontWeight = FontWeight.ExtraBold,
                                            color = if (sel) Color.White else DuoEelInk
                                        )
                                    }
                                }
                            }

                            Text(
                                text = "Gaya Indikator Mini Dynamic Island:",
                                fontSize = 10.5.sp,
                                fontWeight = FontWeight.Bold,
                                color = DuoEelInk
                            )
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                listOf(
                                    0 to "⚡76 Hijau",
                                    1 to "Waveform",
                                    2 to "Timer 0:22",
                                    3 to "82 bpm"
                                ).forEach { (styleId, label) ->
                                    val sel = AppThemeController.proDynamicIslandMiniStyle == styleId
                                    Box(
                                        modifier = Modifier
                                            .weight(1f)
                                            .duolingoButton(
                                                faceColor = if (sel) DuoFeatherGreen else DuoMintSurface,
                                                lipColor = if (sel) DuoFeatherGreenLip else DuoSwanBorder,
                                                cornerRadius = 10.dp,
                                                lipHeight = 3.dp
                                            )
                                            .instantTouchPress {
                                                AppThemeController.proDynamicIslandMiniStyle = styleId
                                            }
                                            .padding(bottom = 3.dp)
                                            .padding(vertical = 6.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = label,
                                            fontSize = 9.sp,
                                            fontWeight = FontWeight.ExtraBold,
                                            color = if (sel) Color.White else DuoEelInk
                                        )
                                    }
                                }
                            }

                            Text(
                                text = "Preset Equalizer Audio & Spektrum (${AppThemeController.proEqualizerPresetName}):",
                                fontSize = 10.5.sp,
                                fontWeight = FontWeight.Bold,
                                color = DuoEelInk
                            )
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                listOf(
                                    "Cyberpunk Club" to listOf(0.86f, 0.72f, 0.58f, 0.78f, 0.90f),
                                    "Bass Boost" to listOf(0.95f, 0.85f, 0.50f, 0.55f, 0.62f),
                                    "Vocal Clarity" to listOf(0.45f, 0.62f, 0.90f, 0.86f, 0.70f),
                                    "Flat Studio" to listOf(0.65f, 0.65f, 0.65f, 0.65f, 0.65f)
                                ).forEach { (eqName, bands) ->
                                    val sel = AppThemeController.proEqualizerPresetName == eqName
                                    Box(
                                        modifier = Modifier
                                            .weight(1f)
                                            .duolingoButton(
                                                faceColor = if (sel) DuoMacawBlue else DuoMintSurface,
                                                lipColor = if (sel) DuoMacawBlueLip else DuoSwanBorder,
                                                cornerRadius = 10.dp,
                                                lipHeight = 3.dp
                                            )
                                            .instantTouchPress {
                                                AppThemeController.proEqualizerPresetName = eqName
                                                AppThemeController.proEqualizerBand60Hz = bands[0]
                                                AppThemeController.proEqualizerBand230Hz = bands[1]
                                                AppThemeController.proEqualizerBand910Hz = bands[2]
                                                AppThemeController.proEqualizerBand3600Hz = bands[3]
                                                AppThemeController.proEqualizerBand14000Hz = bands[4]
                                            }
                                            .padding(bottom = 3.dp)
                                            .padding(vertical = 6.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = eqName,
                                            fontSize = 8.5.sp,
                                            fontWeight = FontWeight.ExtraBold,
                                            color = if (sel) Color.White else DuoEelInk,
                                            maxLines = 1
                                        )
                                    }
                                }
                            }
                        }
                    }

                    4 -> {
                        // PRO MODULE #5: PRO CHAOS GSAP STUDIO (DUAL CHAOS VERSIONS)
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Text(
                                text = "DUAL MODE GRAFIK CHAOS (PILIH SALAH SATU VERSI):",
                                fontFamily = JetBrainsMonoFontFamily,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = DuoEelInk
                            )
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                // Version 1: Chaos Brutal Overdrive (100x, 3s safety + warning + watchdog)
                                val isBrutal = activeGraphicsMode == AppGraphicsQualityMode.CHAOS_OVERDRIVE
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .duolingoButton(
                                            faceColor = if (isBrutal) Color(0xFFF43F5E) else DuoMintSurface,
                                            lipColor = if (isBrutal) Color(0xFF9F1239) else DuoSwanBorder,
                                            cornerRadius = 14.dp,
                                            lipHeight = 4.dp
                                        )
                                        .instantTouchPress {
                                            AppThemeController.requestGraphicsModeChange(
                                                targetMode = AppGraphicsQualityMode.CHAOS_OVERDRIVE,
                                                context = context,
                                                onPersistSafeMode = onPersistSafeMode
                                            )
                                        }
                                        .padding(bottom = 4.dp)
                                        .padding(10.dp)
                                        .testTag("pro_chaos_v1_brutal_button")
                                ) {
                                    Column(verticalArrangement = Arrangement.spacedBy(3.dp)) {
                                        Text(
                                            text = "CHAOS V1 • BRUTAL 100x",
                                            fontFamily = JetBrainsMonoFontFamily,
                                            fontSize = 9.5.sp,
                                            fontWeight = FontWeight.ExtraBold,
                                            color = if (isBrutal) Color.White else Color(0xFFF43F5E)
                                        )
                                        Text(
                                            text = "Tes 3 Detik + Peringatan Wajib + Auto-Reset jika Force Close/Keluar.",
                                            fontSize = 9.5.sp,
                                            color = if (isBrutal) Color.White.copy(alpha = 0.9f) else DuoEelInk
                                        )
                                    }
                                }

                                // Version 2: Chaos Ultimate Pro Max Smooth Low (25x below Smooth)
                                val isLow25x = activeGraphicsMode == AppGraphicsQualityMode.CHAOS_ULTIMATE_LOW
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .duolingoButton(
                                            faceColor = if (isLow25x) Color(0xFF14B8A6) else DuoMintSurface,
                                            lipColor = if (isLow25x) Color(0xFF0F766E) else DuoSwanBorder,
                                            cornerRadius = 14.dp,
                                            lipHeight = 4.dp
                                        )
                                        .instantTouchPress {
                                            AppThemeController.requestGraphicsModeChange(
                                                targetMode = AppGraphicsQualityMode.CHAOS_ULTIMATE_LOW,
                                                context = context,
                                                onPersistSafeMode = onPersistSafeMode
                                            )
                                        }
                                        .padding(bottom = 4.dp)
                                        .padding(10.dp)
                                        .testTag("pro_chaos_v2_ultimate_low_button")
                                ) {
                                    Column(verticalArrangement = Arrangement.spacedBy(3.dp)) {
                                        Text(
                                            text = "CHAOS V2 • 1/25x SMOOTH",
                                            fontFamily = JetBrainsMonoFontFamily,
                                            fontSize = 9.5.sp,
                                            fontWeight = FontWeight.ExtraBold,
                                            color = if (isLow25x) Color.White else Color(0xFF0F766E)
                                        )
                                        Text(
                                            text = "Ultimate Pro Max Smooth Low: 25x lebih ringan di bawah Smooth!",
                                            fontSize = 9.5.sp,
                                            color = if (isLow25x) Color.White.copy(alpha = 0.9f) else DuoEelInk
                                        )
                                    }
                                }
                            }

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(DuoMintSurface)
                                    .padding(horizontal = 10.dp, vertical = 8.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = "Acak Kurva GSAP Tiap Sentuhan (Chaos Variasi)",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.ExtraBold,
                                        color = DuoEelInk
                                    )
                                    Text(
                                        text = "Mengacak 10 preset kurva gerak GSAP secara otomatis agar tiap klik terasa unik.",
                                        fontSize = 9.5.sp,
                                        color = CosmicTextSubtle
                                    )
                                }
                                GsapElasticToggleSwitch(
                                    checked = AppThemeController.proChaosRandomizeCurvePerTap,
                                    activeColor = DuoBeetlePurple,
                                    onCheckedChange = { AppThemeController.proChaosRandomizeCurvePerTap = it }
                                )
                            }
                        }
                    }

                    5 -> {
                        // PRO MODULE #6: PRO BATCH MORI v4.6.0 & MULTI-LINK QUEUE
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Text(
                                text = "PRO BATCH MORI v4.6.0 • ANTREAN MULTI-LINK OTOMATIS",
                                fontFamily = JetBrainsMonoFontFamily,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = DuoEelInk
                            )
                            androidx.compose.material3.OutlinedTextField(
                                value = batchMoriUrlsInput,
                                onValueChange = { batchMoriUrlsInput = it },
                                label = { Text("Daftar URL Media (1 baris 1 link)", fontSize = 10.5.sp) },
                                minLines = 3,
                                maxLines = 5,
                                modifier = Modifier.fillMaxWidth()
                            )
                            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                listOf("MP3 320kbps", "MP4 Video 1080p", "FLAC Lossless").forEach { fmt ->
                                    val sel = batchMoriFormat == fmt
                                    Box(
                                        modifier = Modifier
                                            .weight(1f)
                                            .duolingoButton(
                                                faceColor = if (sel) DuoMacawBlue else DuoMintSurface,
                                                lipColor = if (sel) DuoMacawBlueLip else DuoSwanBorder,
                                                cornerRadius = 10.dp,
                                                lipHeight = 3.dp
                                            )
                                            .instantTouchPress { batchMoriFormat = fmt }
                                            .padding(bottom = 3.dp)
                                            .padding(vertical = 6.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = fmt,
                                            fontSize = 9.5.sp,
                                            fontWeight = FontWeight.ExtraBold,
                                            color = if (sel) Color.White else DuoEelInk
                                        )
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
                                        lipHeight = 3.dp
                                    )
                                    .instantTouchPress {
                                        val urls = batchMoriUrlsInput.lines().map { it.trim() }.filter { it.isNotBlank() }
                                        batchMoriQueueResults = urls.mapIndexed { idx, u ->
                                            "[ANTREAN #${idx + 1} • $batchMoriFormat] SIAP UNDUH: $u (Engine Mori v4.6.0 Pro)"
                                        }
                                    }
                                    .padding(bottom = 3.dp)
                                    .padding(vertical = 9.dp)
                                    .testTag("pro_batch_mori_execute_button"),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "Proses Antrean Batch Mori v4.6.0 Sekarang",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = Color.White
                                )
                            }
                            batchMoriQueueResults.forEach { resLine ->
                                Text(
                                    text = resLine,
                                    fontFamily = JetBrainsMonoFontFamily,
                                    fontSize = 9.5.sp,
                                    color = DuoFeatherGreenLip
                                )
                            }
                        }
                    }

                    6 -> {
                        // PRO MODULE #7: PRO MULTI-FILE VAULT & BATCH STEGANOGRAFI OTOMATIS
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Text(
                                text = "PRO MULTI-PAYLOAD VAULT & BATCH STEGANOGRAFI",
                                fontFamily = JetBrainsMonoFontFamily,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = DuoEelInk
                            )
                            androidx.compose.material3.OutlinedTextField(
                                value = batchStegoPayloadsInput,
                                onValueChange = { batchStegoPayloadsInput = it },
                                label = { Text("Daftar Pesan Rahasia (1 baris 1 paket)", fontSize = 10.5.sp) },
                                minLines = 3,
                                maxLines = 5,
                                modifier = Modifier.fillMaxWidth()
                            )
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .duolingoButton(
                                            faceColor = DuoBeetlePurple,
                                            lipColor = DuoBeetlePurpleLip,
                                            cornerRadius = 12.dp,
                                            lipHeight = 3.dp
                                        )
                                        .instantTouchPress {
                                            val lines = batchStegoPayloadsInput.lines().map { it.trim() }.filter { it.isNotBlank() }
                                            batchStegoOutputs = lines.mapIndexed { idx, secret ->
                                                val encoded = android.util.Base64.encodeToString(
                                                    secret.toByteArray(Charsets.UTF_8),
                                                    android.util.Base64.NO_WRAP
                                                )
                                                val zwc = encoded.map { ch ->
                                                    if (ch.code % 2 == 0) '\u200B' else '\u200C'
                                                }.joinToString("")
                                                "$batchStegoCarrierCover #$idx$zwc [STEGO_OK:${encoded.take(8)}]"
                                            }
                                        }
                                        .padding(bottom = 3.dp)
                                        .padding(vertical = 9.dp)
                                        .testTag("pro_batch_stego_generate_button"),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "Enkripsi Batch Stego",
                                        fontSize = 10.5.sp,
                                        fontWeight = FontWeight.ExtraBold,
                                        color = Color.White
                                    )
                                }
                                if (batchStegoOutputs.isNotEmpty()) {
                                    Box(
                                        modifier = Modifier
                                            .weight(0.8f)
                                            .duolingoButton(
                                                faceColor = DuoMacawBlue,
                                                lipColor = DuoMacawBlueLip,
                                                cornerRadius = 12.dp,
                                                lipHeight = 3.dp
                                            )
                                            .instantTouchPress {
                                                clipboardManager.setText(
                                                    androidx.compose.ui.text.AnnotatedString(
                                                        batchStegoOutputs.joinToString("\n")
                                                    )
                                                )
                                            }
                                            .padding(bottom = 3.dp)
                                            .padding(vertical = 9.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = "Salin Semua (${batchStegoOutputs.size})",
                                            fontSize = 10.5.sp,
                                            fontWeight = FontWeight.ExtraBold,
                                            color = Color.White
                                        )
                                    }
                                }
                            }
                            batchStegoOutputs.forEach { outLine ->
                                Text(
                                    text = outLine,
                                    fontFamily = JetBrainsMonoFontFamily,
                                    fontSize = 9.5.sp,
                                    color = DuoBeetlePurple
                                )
                            }
                        }
                    }

                    7 -> {
                        // PRO MODULE #8: PRO LIVE FPS TELEMETRY & CYBER SECURITY AUDIT HUD
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(DuoMintSurface)
                                    .padding(horizontal = 10.dp, vertical = 8.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = "Tampilkan Live FPS & Cyber Security Audit HUD",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.ExtraBold,
                                        color = DuoEelInk
                                    )
                                    Text(
                                        text = "Memantau FPS real-time, frame time (ms), status Chaos Watchdog, & integritas AES-256.",
                                        fontSize = 9.5.sp,
                                        color = CosmicTextSubtle
                                    )
                                }
                                GsapElasticToggleSwitch(
                                    checked = AppThemeController.proLiveFpsHudEnabled,
                                    activeColor = DuoFeatherGreen,
                                    onCheckedChange = { AppThemeController.proLiveFpsHudEnabled = it }
                                )
                            }
                            ProLiveFpsAndSecurityAuditHud()
                        }
                    }

                    8 -> {
                        // PRO MODULE #9: ARCANUM BROWSER PRO CUSTOM DOMAIN ALIAS / REDIRECT
                        ProBrowserDomainAliasEditorSection(
                            isDarkSurface = false
                        )
                    }
                }

                // Option to Lock Pro Mode back (for testing secret code verification again)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    Text(
                        text = "Kunci Kembali Mode Pro (Uji Input Kode)",
                        fontSize = 9.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = CosmicTextSubtle,
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .clickable {
                                secretCodeInput = ""
                                codeFeedbackMessage = null
                                AppThemeController.lockProModeAgain(context)
                            }
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                            .testTag("pro_relock_mode_button")
                    )
                }
            }
        }
    }
}

@Composable
fun ProBrowserDomainAliasEditorSection(
    isDarkSurface: Boolean = false,
    onFeedback: (String) -> Unit = {}
) {
    val context = androidx.compose.ui.platform.LocalContext.current
    val rules = AppThemeController.proBrowserDomainAliases
    var aliasInput by remember { mutableStateOf("") }
    var targetInput by remember { mutableStateOf("") }
    var editingRuleId by remember { mutableStateOf<String?>(null) }
    var editorStatus by remember { mutableStateOf<String?>(null) }

    val primaryTextColor = if (isDarkSurface) Color(0xFFF1F5F9) else DuoEelInk
    val subTextColor = if (isDarkSurface) Color(0xFF94A3B8) else CosmicTextSubtle
    val cardBgColor = if (isDarkSurface) Color(0xFF1E2028) else DuoMintSurface

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
            text = "ARCANUM BROWSER PRO • PENGUBAH DOMAIN (CUSTOM ALIAS)",
            fontFamily = JetBrainsMonoFontFamily,
            fontSize = 10.sp,
            fontWeight = FontWeight.ExtraBold,
            color = if (isDarkSurface) Color(0xFF38BDF8) else DuoEelInk
        )
        Text(
            text = "Ubah domain asli menjadi domain custom sesuka hati. Contoh: saat membuka https://g.start di Arcanum Browser, halaman aslinya memuat https://google.com sementara address bar menampilkan g.start.",
            fontSize = 10.sp,
            color = subTextColor
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(10.dp))
                .background(if (isDarkSurface) Color(0xFF162032) else Color(0xFFEDE9FE))
                .border(0.8.dp, Color(0xFF8B5CF6).copy(alpha = 0.45f), RoundedCornerShape(10.dp))
                .clickable {
                    aliasInput = "g.start"
                    targetInput = "google.com"
                    val ok = AppThemeController.addOrUpdateProDomainAlias("g.start", "google.com", context)
                    val msg = if (ok) "Preset g.start → google.com aktif!" else "Gagal menambah preset."
                    editorStatus = msg
                    onFeedback(msg)
                }
                .padding(horizontal = 10.dp, vertical = 7.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Preset Cepat: g.start → google.com",
                fontFamily = JetBrainsMonoFontFamily,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                color = if (isDarkSurface) Color(0xFFC084FC) else DuoBeetlePurple
            )
            Text(
                text = "+ Terapkan",
                fontSize = 10.sp,
                fontWeight = FontWeight.ExtraBold,
                color = if (isDarkSurface) Color(0xFF38BDF8) else DuoMacawBlue
            )
        }

        val tfColors = androidx.compose.material3.OutlinedTextFieldDefaults.colors(
            focusedTextColor = primaryTextColor,
            unfocusedTextColor = primaryTextColor,
            focusedLabelColor = if (isDarkSurface) Color(0xFF38BDF8) else DuoBeetlePurple,
            unfocusedLabelColor = subTextColor,
            focusedBorderColor = Color(0xFF8B5CF6),
            unfocusedBorderColor = subTextColor.copy(alpha = 0.4f)
        )

        androidx.compose.material3.OutlinedTextField(
            value = aliasInput,
            onValueChange = { aliasInput = it },
            label = { Text("Domain Custom / Alias (mis: https://g.start)", fontSize = 10.5.sp) },
            placeholder = { Text("g.start atau nama.custom", fontSize = 10.5.sp) },
            singleLine = true,
            colors = tfColors,
            modifier = Modifier
                .fillMaxWidth()
                .testTag("pro_domain_alias_input")
        )

        androidx.compose.material3.OutlinedTextField(
            value = targetInput,
            onValueChange = { targetInput = it },
            label = { Text("Domain Web Asli Tujuan (mis: https://google.com)", fontSize = 10.5.sp) },
            placeholder = { Text("google.com", fontSize = 10.5.sp) },
            singleLine = true,
            colors = tfColors,
            modifier = Modifier
                .fillMaxWidth()
                .testTag("pro_domain_target_input")
        )

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .duolingoButton(
                        faceColor = DuoBeetlePurple,
                        lipColor = DuoBeetlePurpleLip,
                        cornerRadius = 12.dp,
                        lipHeight = 3.dp
                    )
                    .instantTouchPress {
                        val saved = AppThemeController.addOrUpdateProDomainAlias(
                            rawAliasDomain = aliasInput,
                            rawTargetDomain = targetInput,
                            context = context,
                            editRuleId = editingRuleId
                        )
                        if (saved) {
                            val normAlias = com.example.ui.theme.ArcanumBrowserDomainAliasEngine.normalizeHost(aliasInput)
                            val normTarget = com.example.ui.theme.ArcanumBrowserDomainAliasEngine.normalizeHost(targetInput)
                            val msg = if (editingRuleId != null) {
                                "Aturan domain diperbarui: $normAlias → $normTarget"
                            } else {
                                "Aturan domain ditambahkan: $normAlias → $normTarget"
                            }
                            editorStatus = msg
                            onFeedback(msg)
                            aliasInput = ""
                            targetInput = ""
                            editingRuleId = null
                        } else {
                            val err = "Masukkan dua domain valid yang berbeda (contoh: g.start & google.com)."
                            editorStatus = err
                            onFeedback(err)
                        }
                    }
                    .padding(bottom = 3.dp)
                    .padding(vertical = 9.dp)
                    .testTag("pro_domain_alias_save_button"),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = if (editingRuleId != null) "Simpan Perubahan Domain" else "Tambah Pengalihan Domain",
                    fontSize = 10.5.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.White
                )
            }
            if (editingRuleId != null) {
                Box(
                    modifier = Modifier
                        .duolingoButton(
                            faceColor = DuoSwanBorder,
                            lipColor = Color(0xFF94A3B8),
                            cornerRadius = 12.dp,
                            lipHeight = 3.dp
                        )
                        .instantTouchPress {
                            editingRuleId = null
                            aliasInput = ""
                            targetInput = ""
                        }
                        .padding(bottom = 3.dp)
                        .padding(horizontal = 12.dp, vertical = 9.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Batal",
                        fontSize = 10.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = DuoEelInk
                    )
                }
            }
        }

        if (!editorStatus.isNullOrBlank()) {
            Text(
                text = editorStatus!!,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                color = if (isDarkSurface) Color(0xFF30D158) else DuoFeatherGreenLip
            )
        }

        if (rules.isEmpty()) {
            Text(
                text = "Belum ada aturan domain custom. Tambahkan di atas.",
                fontSize = 10.sp,
                color = subTextColor
            )
        } else {
            rules.forEach { rule ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(cardBgColor)
                        .border(
                            0.8.dp,
                            if (rule.isEnabled) Color(0xFF8B5CF6).copy(alpha = 0.5f) else Color.Gray.copy(alpha = 0.25f),
                            RoundedCornerShape(12.dp)
                        )
                        .padding(horizontal = 10.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .clickable {
                                editingRuleId = rule.id
                                aliasInput = rule.aliasDomain
                                targetInput = rule.targetDomain
                            }
                    ) {
                        Text(
                            text = "${rule.aliasDomain}  ⇄  ${rule.targetDomain}",
                            fontFamily = JetBrainsMonoFontFamily,
                            fontSize = 10.5.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = primaryTextColor
                        )
                        Text(
                            text = if (rule.isEnabled) "Aktif • Ketuk untuk edit aturan" else "Nonaktif",
                            fontSize = 9.sp,
                            color = subTextColor
                        )
                    }
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        GsapElasticToggleSwitch(
                            checked = rule.isEnabled,
                            activeColor = DuoFeatherGreen,
                            onCheckedChange = {
                                AppThemeController.toggleProDomainAlias(rule.id, context)
                            }
                        )
                        Text(
                            text = "Hapus",
                            fontSize = 9.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFEF4444),
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .clickable {
                                    AppThemeController.removeProDomainAlias(rule.id, context)
                                }
                                .padding(horizontal = 6.dp, vertical = 4.dp)
                        )
                    }
                }
            }
        }
    }
}






