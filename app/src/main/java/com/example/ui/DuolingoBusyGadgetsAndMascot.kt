package com.example.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.CloudDownload
import androidx.compose.material.icons.filled.DeleteSweep
import androidx.compose.material.icons.filled.EnhancedEncryption
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.Memory
import androidx.compose.material.icons.filled.Minimize
import androidx.compose.material.icons.filled.OpenWith
import androidx.compose.material.icons.filled.Radar
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.runtime.withFrameNanos
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.CosmicTextPure
import com.example.ui.theme.CosmicTextSubtle
import com.example.ui.theme.DuoBeeYellow
import com.example.ui.theme.DuoBeeYellowLip
import com.example.ui.theme.DuoBeeYellowSoft
import com.example.ui.theme.DuoBeetlePurple
import com.example.ui.theme.DuoBeetlePurpleLip
import com.example.ui.theme.DuoBeetlePurpleSoft
import com.example.ui.theme.DuoCardinalRed
import com.example.ui.theme.DuoCardinalRedLip
import com.example.ui.theme.DuoCardinalRedSoft
import com.example.ui.theme.DuoEelInk
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
import com.example.ui.theme.AppThemeController
import com.example.ui.theme.DuoPureWhite
import com.example.ui.theme.DuoSwanBorder
import com.example.ui.theme.JetBrainsMonoFontFamily
import com.example.ui.theme.MascotAccessory
import com.example.ui.theme.MascotActivityPose
import com.example.ui.theme.duolingoButton
import com.example.ui.theme.duolingoCard
import androidx.compose.ui.graphics.drawscope.DrawScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.exp
import kotlin.math.roundToInt
import kotlin.math.sin
import kotlin.math.sqrt

// ============================================================================
// 1. PHYSICS-DRIVEN 3D KAWAII FILE FOLDER MASCOT ENGINE
// ============================================================================

enum class MascotMoodState(
    val primaryColor: Color,
    val lipColor: Color,
    val softBgColor: Color
) {
    IDLE(
        primaryColor = Color(0xFFF59E0B),
        lipColor = Color(0xFFB45309),
        softBgColor = DuoBeeYellowSoft
    ),
    GRABBED(
        primaryColor = DuoBeeYellow,
        lipColor = DuoBeeYellowLip,
        softBgColor = DuoBeeYellowSoft
    ),
    CLICK_EXCITED(
        primaryColor = DuoFeatherGreen,
        lipColor = DuoFeatherGreenLip,
        softBgColor = DuoFeatherGreenSoft
    ),
    DOWNLOADING(
        primaryColor = DuoMacawBlue,
        lipColor = DuoMacawBlueLip,
        softBgColor = DuoMacawBlueSoft
    ),
    SCANNING_IMPORT(
        primaryColor = DuoFeatherGreen,
        lipColor = DuoFeatherGreenLip,
        softBgColor = DuoFeatherGreenSoft
    ),
    ENCRYPTING_STEGO(
        primaryColor = DuoBeetlePurple,
        lipColor = DuoBeetlePurpleLip,
        softBgColor = DuoBeetlePurpleSoft
    ),
    DELETING_CLEANUP(
        primaryColor = DuoCardinalRed,
        lipColor = DuoCardinalRedLip,
        softBgColor = DuoCardinalRedSoft
    ),
    SURPRISE_PEEK(
        primaryColor = DuoFoxOrange,
        lipColor = DuoFoxOrangeLip,
        softBgColor = DuoFoxOrangeSoft
    )
}

enum class SurpriseIdlePose {
    HAPPY_BOUNCE,
    CRYING_FOUNTAIN,
    RAGE_INFERNO,
    SHOCKED_JOLT,
    SHY_BLUSH,
    SLEEPY_DREAM
}

/**
 * Upgraded Physics-Driven 3D Kawaii File Folder Mascot Overlay:
 * - Replaces Nyx-Owl completely with the classic 3D Kawaii File Folder Mascot, upgraded with
 *   real-time 60fps spring, pendulum, inertia, and secondary follow-through physics.
 * - NO dialog or speech bubble: strictly the 3D File Folder mascot character overlay itself.
 * - Hold and drag ("tahan dan geser") smoothly anywhere on screen, with throw inertia & elastic wall bounce.
 * - Conservation-of-volume squash & stretch, velocity-coupled pendulum tilt, dangling feet,
 *   secondary lagging document papers, and smooth eye gaze tracking.
 */
@Composable
fun InteractiveDraggableMascotOverlay(
    activeTabName: String,
    isMascotVisible: Boolean = true,
    resummonNonce: Int = 0,
    onSetMascotVisible: (Boolean) -> Unit = {},
    isDownloading: Boolean = false,
    downloadProgressPercent: Int = 0,
    downloadStatusText: String = "",
    isScanningZip: Boolean = false,
    scanStatusText: String = "",
    isProcessingStegoOrCrypto: Boolean = false,
    stegoStatusText: String = "",
    latestBannerMessage: String? = null,
    totalVaultFilesCount: Int = 0,
    encryptedFilesCount: Int = 0,
    modifier: Modifier = Modifier
) {
    if (!isMascotVisible) return

    val density = LocalDensity.current
    val coroutineScope = rememberCoroutineScope()

    // Reduced by 0.3 (30% smaller than 92.dp -> 64.dp)
    val mascotSizeDp = 64.dp
    val mascotSizePx = with(density) { mascotSizeDp.toPx() }
    val defaultRightMarginPx = with(density) { 14.dp.toPx() }
    val defaultBottomMarginPx = with(density) { 108.dp.toPx() }
    val screenEdgePaddingPx = with(density) { 6.dp.toPx() }

    // Primary Position & Velocity State (Pixels & Pixels/sec)
    var offsetX by remember { mutableFloatStateOf(Float.NaN) }
    var offsetY by remember { mutableFloatStateOf(Float.NaN) }
    var velX by remember { mutableFloatStateOf(0f) }
    var velY by remember { mutableFloatStateOf(0f) }
    var isGrabbed by remember { mutableStateOf(false) }
    var isActivelyDragging by remember { mutableStateOf(false) }
    var showHideConfirmDialog by remember { mutableStateOf(false) }

    // Real-Time Coupled Physics State (Updated at 60fps via withFrameNanos)
    var continuousTimeSec by remember { mutableFloatStateOf(0f) }
    var bodyTiltDeg by remember { mutableFloatStateOf(0f) }
    var bodyTiltVel by remember { mutableFloatStateOf(0f) }
    var squashDeform by remember { mutableFloatStateOf(0f) } // >0 = tall stretch, <0 = wide squash
    var squashVel by remember { mutableFloatStateOf(0f) }

    // Secondary Follow-Through Physics (Overlapping Action for Papers, Hands, Feet & Gaze)
    var paperLagY by remember { mutableFloatStateOf(0f) }
    var paperLagVelY by remember { mutableFloatStateOf(0f) }
    var limbLagX by remember { mutableFloatStateOf(0f) }
    var limbLagVelX by remember { mutableFloatStateOf(0f) }
    var limbLagY by remember { mutableFloatStateOf(0f) }
    var limbLagVelY by remember { mutableFloatStateOf(0f) }
    var eyeGazeX by remember { mutableFloatStateOf(0f) }
    var eyeGazeY by remember { mutableFloatStateOf(0f) }
    var targetSaccadeX by remember { mutableFloatStateOf(0f) }
    var targetSaccadeY by remember { mutableFloatStateOf(0f) }

    // Click & Surprise Emotion States
    var clickComboCount by remember { mutableIntStateOf(0) }
    var isClickJumping by remember { mutableStateOf(false) }
    var activeSurprisePose by remember { mutableStateOf<SurpriseIdlePose?>(null) }
    var transientBannerEventMood by remember { mutableStateOf<MascotMoodState?>(null) }
    val clickBurstAnim = remember { Animatable(0f) }

    // Smart Care & Elemental States for Floating Mascot
    var activeFloatingElement by remember { mutableStateOf<MascotElement?>(null) }
    var showSmartCareHud by remember { mutableStateOf(false) }
    var electricNeed by remember { mutableIntStateOf(80) }
    var loveNeed by remember { mutableIntStateOf(76) }
    var ideaNeed by remember { mutableIntStateOf(84) }
    var smartBubbleLine by remember {
        mutableStateOf("Halo! Ketuk aku untuk buka Smart Care & elemen 3D, atau seret aku ke mana saja!")
    }

    LaunchedEffect(Unit) {
        while (true) {
            delay(8000L)
            electricNeed = (electricNeed - 2).coerceAtLeast(25)
            loveNeed = (loveNeed - 2).coerceAtLeast(25)
            ideaNeed = (ideaNeed - 2).coerceAtLeast(25)
        }
    }

    LaunchedEffect(activeTabName, isDownloading, isScanningZip, isProcessingStegoOrCrypto, latestBannerMessage) {
        when {
            isDownloading -> {
                activeFloatingElement = MascotElement.ELECTRIC
                smartBubbleLine = "BZZT! Mori sedang mengunduh media ($downloadProgressPercent%)! Daya listrikku menyala penuh!"
            }
            isProcessingStegoOrCrypto -> {
                activeFloatingElement = MascotElement.SHIELD
                smartBubbleLine = "SHIELD ON! Enkripsi & Perisai PIN sedang mengunci berkas rahasiamu!"
            }
            isScanningZip -> {
                activeFloatingElement = MascotElement.FIRE
                smartBubbleLine = "BURN! Memindai & membakar ancaman virus dari arsip ZIP!"
            }
            activeTabName == "MEDIA_PLAYER" || activeTabName == "PHOTO_GALLERY" || AppThemeController.isMediaWatchingActive -> {
                smartBubbleLine = "Mode Ikut Nonton aktif! Aku ikut melihat media & galeri bersamamu!"
            }
            !latestBannerMessage.isNullOrBlank() -> {
                val lower = latestBannerMessage.lowercase()
                when {
                    lower.contains("mori") || lower.contains("unduh") -> {
                        activeFloatingElement = MascotElement.ELECTRIC
                        smartBubbleLine = "Mori siap! Pilih 720P, 1080P, atau MP3 untuk simpan ke Brankas!"
                    }
                    lower.contains("pin") || lower.contains("anti-screenshot") || lower.contains("enkripsi") -> {
                        activeFloatingElement = MascotElement.SHIELD
                        smartBubbleLine = "Perisai Siber aktif menjaga privasi layar & PIN Brankas!"
                    }
                    lower.contains("hapus") || lower.contains("gagal") || lower.contains("error") -> {
                        activeFloatingElement = MascotElement.TEARS
                        smartBubbleLine = "Huwaaa! Jangan sedih, elus aku atau tekan Love ya!"
                    }
                    else -> {
                        activeFloatingElement = MascotElement.IDEA_BULB
                        smartBubbleLine = latestBannerMessage
                    }
                }
            }
        }
    }

    val floatingNeededElement = remember(electricNeed, loveNeed, ideaNeed) {
        when {
            electricNeed < 55 -> MascotElement.ELECTRIC
            loveNeed < 55 -> MascotElement.LOVE
            ideaNeed < 55 -> MascotElement.IDEA_BULB
            else -> null
        }
    }

    // Detect file add / delete / encrypt / strip EXIF events from statusBannerMessage
    LaunchedEffect(latestBannerMessage) {
        val msg = latestBannerMessage?.trim()
        if (!msg.isNullOrEmpty()) {
            val lower = msg.lowercase()
            transientBannerEventMood = when {
                lower.contains("hapus") || lower.contains("karantina") || lower.contains("bersih") ->
                    MascotMoodState.DELETING_CLEANUP
                lower.contains("unduh") || lower.contains("mori") || lower.contains("download") ->
                    MascotMoodState.DOWNLOADING
                lower.contains("impor") || lower.contains("ekstrak") || lower.contains("zip") || lower.contains("tambah") ->
                    MascotMoodState.SCANNING_IMPORT
                lower.contains("stego") || lower.contains("enkripsi") || lower.contains("dekripsi") || lower.contains("pin") || lower.contains("wav") ->
                    MascotMoodState.ENCRYPTING_STEGO
                else -> MascotMoodState.CLICK_EXCITED
            }
            // Give a physics jump impulse when a file operation occurs!
            squashVel += 3.2f
            paperLagVelY -= 180f
            delay(4000L)
            transientBannerEventMood = null
        }
    }

    // Random surprise emotion & natural eye saccade generator
    LaunchedEffect(isGrabbed, isDownloading, isScanningZip, isProcessingStegoOrCrypto, isClickJumping) {
        while (true) {
            delay(2600L)
            // Smooth natural eye saccade target
            targetSaccadeX = ((-10..10).random() / 10f)
            targetSaccadeY = ((-6..6).random() / 10f)
            delay(3800L)
            if (!isGrabbed && !isDownloading && !isScanningZip && !isProcessingStegoOrCrypto && !isClickJumping) {
                val poses = SurpriseIdlePose.entries
                val nextPose = poses[(System.currentTimeMillis() % poses.size).toInt()]
                activeSurprisePose = nextPose
                squashVel += 2.2f
                delay(4200L)
                activeSurprisePose = null
            }
        }
    }

    val currentMood: MascotMoodState = when {
        isGrabbed -> MascotMoodState.GRABBED
        isClickJumping -> MascotMoodState.CLICK_EXCITED
        isDownloading -> MascotMoodState.DOWNLOADING
        isScanningZip -> MascotMoodState.SCANNING_IMPORT
        isProcessingStegoOrCrypto -> MascotMoodState.ENCRYPTING_STEGO
        transientBannerEventMood != null -> transientBannerEventMood!!
        activeSurprisePose != null -> MascotMoodState.SURPRISE_PEEK
        else -> MascotMoodState.IDLE
    }

    BoxWithConstraints(
        modifier = modifier.fillMaxSize()
    ) {
        val maxW = constraints.maxWidth.toFloat()
        val maxH = constraints.maxHeight.toFloat()

        val minX = screenEdgePaddingPx
        val maxX = (maxW - mascotSizePx - screenEdgePaddingPx).coerceAtLeast(minX)
        val minY = screenEdgePaddingPx
        val maxY = (maxH - mascotSizePx - screenEdgePaddingPx).coerceAtLeast(minY)

        val defaultX = (maxW - mascotSizePx - defaultRightMarginPx).coerceIn(minX, maxX)
        val defaultY = (maxH - mascotSizePx - defaultBottomMarginPx).coerceIn(minY, maxY)

        // Bottom "Box X" Dismiss Target Zone Geometry
        val dismissBoxWidthPx = with(density) { 168.dp.toPx() }
        val dismissBoxHeightPx = with(density) { 68.dp.toPx() }
        val dismissBoxBottomMarginPx = with(density) { 18.dp.toPx() }
        val dismissCenterX = maxW * 0.5f
        val dismissCenterY = (maxH - dismissBoxBottomMarginPx - dismissBoxHeightPx * 0.5f).coerceAtLeast(minY)

        val safeX = if (offsetX.isNaN()) defaultX else offsetX.coerceIn(minX, maxX)
        val safeY = if (offsetY.isNaN()) defaultY else offsetY.coerceIn(minY, maxY)

        val mascotCenterX = safeX + mascotSizePx * 0.5f
        val mascotCenterY = safeY + mascotSizePx * 0.5f

        val isHoveringDismissBox = isActivelyDragging &&
            abs(mascotCenterX - dismissCenterX) <= (dismissBoxWidthPx * 0.62f) &&
            abs(mascotCenterY - dismissCenterY) <= (dismissBoxHeightPx * 0.85f)

        val grabbedScale = 1f

        val dismissBoxScale by animateFloatAsState(
            targetValue = if (isHoveringDismissBox) 1.15f else 1f,
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness = Spring.StiffnessMedium
            ),
            label = "dismiss_box_scale"
        )

        LaunchedEffect(maxW, maxH) {
            if (maxW > 0f && maxH > 0f) {
                if (offsetX.isNaN() || offsetY.isNaN()) {
                    offsetX = defaultX
                    offsetY = defaultY
                } else {
                    offsetX = offsetX.coerceIn(minX, maxX)
                    offsetY = offsetY.coerceIn(minY, maxY)
                }
            }
        }

        // Resummon animation when user clicks "Maskot Overlay" on Beranda Brankas
        LaunchedEffect(resummonNonce) {
            if (resummonNonce > 0 && maxW > 0f && maxH > 0f) {
                offsetX = defaultX
                offsetY = defaultY
                velX = 0f
                velY = 0f
                squashDeform = 0f
                squashVel = 0f
                bodyTiltDeg = 0f
                bodyTiltVel = 0f
                paperLagVelY = -120f
                activeSurprisePose = SurpriseIdlePose.HAPPY_BOUNCE
                delay(2800L)
                activeSurprisePose = null
            }
        }

        // 60FPS Real-Time Animation Solver (1:1 Finger Tracking, Zero Stretch/Melar)
        LaunchedEffect(minX, maxX, minY, maxY) {
            var lastNanos = 0L
            while (true) {
                withFrameNanos { nowNanos ->
                    val dt = if (lastNanos == 0L) 0.016f else ((nowNanos - lastNanos) / 1_000_000_000f).coerceIn(0.001f, 0.045f)
                    lastNanos = nowNanos
                    continuousTimeSec += dt

                    // No free-flight drift: mascot stays glued 1:1 where finger drags/releases it
                    velX = 0f
                    velY = 0f
                    bodyTiltDeg = 0f
                    bodyTiltVel = 0f
                    squashDeform = 0f
                    squashVel = 0f

                    // Subtle internal paper & limb motion without stretching outer mascot body
                    val targetPaperY = if (isDownloading || isScanningZip || isProcessingStegoOrCrypto) {
                        sin(continuousTimeSec * 9.5f) * 5.0f
                    } else {
                        sin(continuousTimeSec * 3.1f - 0.65f) * 2.0f
                    }
                    val paperAccel = (targetPaperY - paperLagY) * 140f - paperLagVelY * 11f
                    paperLagVelY += paperAccel * dt
                    paperLagY = (paperLagY + paperLagVelY * dt).coerceIn(-12f, 12f)

                    limbLagX = 0f
                    limbLagY = 0f
                    limbLagVelX = 0f
                    limbLagVelY = 0f

                    // Smooth Eye Gaze Tracking
                    val eyeSmooth = (1f - exp(-10f * dt)).coerceIn(0f, 1f)
                    eyeGazeX += (targetSaccadeX - eyeGazeX) * eyeSmooth
                    eyeGazeY += (targetSaccadeY - eyeGazeY) * eyeSmooth
                }
            }
        }

        val hoverBobPx = if (isGrabbed || isActivelyDragging) 0f else sin(continuousTimeSec * 2.8f) * with(density) { 2.dp.toPx() }

        // Fixed 1:1 scale (Zero squash & stretch / tidak melar)
        val dynamicScaleY = 1f
        val dynamicScaleX = 1f

        // Bottom "Box X" Target Zone when dragging mascot down
        AnimatedVisibility(
            visible = isActivelyDragging,
            enter = fadeIn(tween(150)),
            exit = fadeOut(tween(180)),
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 18.dp)
        ) {
            Box(
                modifier = Modifier
                    .graphicsLayer {
                        scaleX = dismissBoxScale
                        scaleY = dismissBoxScale
                    }
                    .duolingoCard(
                        backgroundColor = if (isHoveringDismissBox) DuoCardinalRed else DuoCardinalRedSoft,
                        borderColor = if (isHoveringDismissBox) DuoPureWhite else DuoCardinalRed,
                        lipColor = DuoCardinalRedLip,
                        cornerRadius = 22.dp,
                        borderWidth = 2.5.dp,
                        lipHeight = 5.dp
                    )
                    .padding(horizontal = 18.dp, vertical = 10.dp)
                    .testTag("mascot_dismiss_box_x"),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.padding(bottom = 3.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(if (isHoveringDismissBox) DuoPureWhite else DuoCardinalRed)
                            .border(2.dp, DuoPureWhite, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Box X Sembunyikan Maskot",
                            tint = if (isHoveringDismissBox) DuoCardinalRed else DuoPureWhite,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                    Column {
                        Text(
                            text = if (isHoveringDismissBox) "LEPAS DI BOX X" else "SERET KE BOX X",
                            fontFamily = JetBrainsMonoFontFamily,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = if (isHoveringDismissBox) DuoPureWhite else DuoCardinalRedLip
                        )
                        Text(
                            text = if (isHoveringDismissBox) "Pilih Sembunyikan Maskot" else "Sembunyikan Maskot Overlay",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isHoveringDismissBox) DuoPureWhite.copy(alpha = 0.92f) else DuoEelInk
                        )
                    }
                }
            }
        }

        // Expandable Smart Care & Elemental HUD Popup near the Floating Mascot
        if (showSmartCareHud && !isActivelyDragging) {
            val hudWidthPx = with(density) { 256.dp.toPx() }
            val hudHeightPx = with(density) { 175.dp.toPx() }
            val hudX = (safeX - hudWidthPx + mascotSizePx).coerceIn(minX, (maxW - hudWidthPx - minX).coerceAtLeast(minX))
            val hudY = (safeY - hudHeightPx - with(density) { 8.dp.toPx() }).let { candidateY ->
                if (candidateY < minY) (safeY + mascotSizePx + with(density) { 8.dp.toPx() }).coerceAtMost(maxH - hudHeightPx)
                else candidateY
            }

            Box(
                modifier = Modifier
                    .offset { IntOffset(hudX.roundToInt(), hudY.roundToInt()) }
                    .width(256.dp)
                    .duolingoCard(
                        backgroundColor = DuoPureWhite,
                        borderColor = activeFloatingElement?.badgeColor ?: DuoFeatherGreen,
                        lipColor = DuoHareLip,
                        cornerRadius = 20.dp,
                        borderWidth = 2.dp,
                        lipHeight = 4.dp
                    )
                    .padding(12.dp)
                    .padding(bottom = 4.dp)
                    .testTag("mascot_smart_care_hud")
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = activeFloatingElement?.label ?: "Smart Maskot Care",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = activeFloatingElement?.badgeColor ?: DuoFeatherGreen,
                            maxLines = 1,
                            modifier = Modifier.weight(1f)
                        )
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Tutup Smart HUD",
                            tint = DuoEelInk,
                            modifier = Modifier
                                .size(16.dp)
                                .clickable { showSmartCareHud = false }
                        )
                    }

                    Text(
                        text = smartBubbleLine,
                        fontSize = 11.sp,
                        lineHeight = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = DuoEelInk,
                        maxLines = 2
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        listOf(
                            Triple("DAYA $electricNeed%", Color(0xFFEAB308), MascotElement.ELECTRIC),
                            Triple("LOVE $loveNeed%", Color(0xFFEC4899), MascotElement.LOVE),
                            Triple("IDE $ideaNeed%", Color(0xFF58CC02), MascotElement.IDEA_BULB)
                        ).forEach { (txt, col, elem) ->
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(col.copy(alpha = 0.14f))
                                    .border(1.dp, col, RoundedCornerShape(8.dp))
                                    .clickable {
                                        activeFloatingElement = elem
                                        when (elem) {
                                            MascotElement.ELECTRIC -> {
                                                electricNeed = (electricNeed + 20).coerceAtMost(100)
                                                smartBubbleLine = "Energi Listrik diisi ke $electricNeed%!"
                                            }
                                            MascotElement.LOVE -> {
                                                loveNeed = (loveNeed + 20).coerceAtMost(100)
                                                smartBubbleLine = "Kasih Sayang Love penuh ($loveNeed%)!"
                                            }
                                            else -> {
                                                ideaNeed = (ideaNeed + 20).coerceAtMost(100)
                                                smartBubbleLine = "Lampu Ide Pintar menyala ($ideaNeed%)!"
                                            }
                                        }
                                    }
                                    .padding(vertical = 4.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = txt,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = DuoEelInk
                                )
                            }
                        }
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        MascotElement.entries.forEach { elem ->
                            val selected = activeFloatingElement == elem
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .duolingoButton(
                                        faceColor = if (selected) elem.badgeColor else DuoPureWhite,
                                        lipColor = if (selected) elem.lipColor else DuoHareLip,
                                        cornerRadius = 8.dp,
                                        lipHeight = 2.dp
                                    )
                                    .clickable {
                                        activeFloatingElement = if (selected) null else elem
                                        when (elem) {
                                            MascotElement.LOVE -> {
                                                loveNeed = (loveNeed + 20).coerceAtMost(100)
                                                smartBubbleLine = "Hati 3D bermekaran! Aku makin sayang padamu!"
                                            }
                                            MascotElement.ELECTRIC -> {
                                                electricNeed = (electricNeed + 20).coerceAtMost(100)
                                                smartBubbleLine = "Petir energi penuh! Siap kebut unduhan & enkripsi!"
                                            }
                                            MascotElement.IDEA_BULB -> {
                                                ideaNeed = (ideaNeed + 20).coerceAtMost(100)
                                                smartBubbleLine = "Lampu Ide menyala! Coba fitur Smart Search & QR Unik!"
                                            }
                                            MascotElement.FIRE -> {
                                                smartBubbleLine = "Aura Api Membara siap membakar file duplikat!"
                                            }
                                            MascotElement.TEARS -> {
                                                smartBubbleLine = "Air mata deras menyembur! Elus aku supaya ceria lagi!"
                                            }
                                            MascotElement.SHIELD -> {
                                                smartBubbleLine = "Perisai Heksagonal aktif melindungi Brankas & PIN!"
                                            }
                                        }
                                    }
                                    .padding(vertical = 4.dp)
                                    .padding(bottom = 2.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = elem.shortLabel.take(4),
                                    fontSize = 8.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = if (selected) Color.White else DuoEelInk,
                                    maxLines = 1
                                )
                            }
                        }
                    }

                    // Quick Activity Pose Switcher Row inside Smart Care HUD
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        MascotActivityPose.entries.forEach { pose ->
                            val isPoseSelected = AppThemeController.mascotActivityPose == pose
                            Box(
                                modifier = Modifier
                                    .duolingoButton(
                                        faceColor = if (isPoseSelected) pose.badgeColor else DuoPureWhite,
                                        lipColor = if (isPoseSelected) AppThemeController.darkenColor(pose.badgeColor, 0.72f) else DuoHareLip,
                                        cornerRadius = 8.dp,
                                        lipHeight = 2.dp
                                    )
                                    .clickable {
                                        AppThemeController.mascotActivityPose = pose
                                        AppThemeController.poseTriggerNonce++
                                        smartBubbleLine = "Pose Maskot: ${pose.displayName} — ${pose.subtitle}"
                                    }
                                    .padding(horizontal = 7.dp, vertical = 4.dp)
                                    .padding(bottom = 2.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = pose.displayName,
                                    fontSize = 8.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = if (isPoseSelected) Color.White else DuoEelInk,
                                    maxLines = 1
                                )
                            }
                        }
                    }
                }
            }
        }

        // Pure 3D Kawaii File Folder Mascot Overlay
        Box(
            modifier = Modifier
                .offset {
                    IntOffset(
                        x = safeX.roundToInt(),
                        y = (safeY + hoverBobPx).roundToInt()
                    )
                }
                .size(mascotSizeDp)
                .graphicsLayer {
                    scaleX = grabbedScale * dynamicScaleX
                    scaleY = grabbedScale * dynamicScaleY
                    rotationZ = 0f
                }
                .pointerInput(maxX, maxY, dismissCenterX, dismissCenterY) {
                    detectDragGestures(
                        onDragStart = {
                            isGrabbed = true
                            isActivelyDragging = true
                            loveNeed = (loveNeed + 5).coerceAtMost(100)
                            if (offsetX.isNaN()) offsetX = defaultX
                            if (offsetY.isNaN()) offsetY = defaultY
                            velX = 0f
                            velY = 0f
                        },
                        onDragEnd = {
                            val endCenterX = (if (offsetX.isNaN()) defaultX else offsetX) + mascotSizePx * 0.5f
                            val endCenterY = (if (offsetY.isNaN()) defaultY else offsetY) + mascotSizePx * 0.5f
                            val droppedInBoxX =
                                abs(endCenterX - dismissCenterX) <= (dismissBoxWidthPx * 0.62f) &&
                                    abs(endCenterY - dismissCenterY) <= (dismissBoxHeightPx * 0.85f)

                            isGrabbed = false
                            isActivelyDragging = false
                            velX = 0f
                            velY = 0f

                            if (droppedInBoxX) {
                                showHideConfirmDialog = true
                            }
                        },
                        onDragCancel = {
                            isGrabbed = false
                            isActivelyDragging = false
                            velX = 0f
                            velY = 0f
                        },
                        onDrag = { change, dragAmount ->
                            change.consume()
                            val currentX = if (offsetX.isNaN()) defaultX else offsetX
                            val currentY = if (offsetY.isNaN()) defaultY else offsetY
                            offsetX = (currentX + dragAmount.x).coerceIn(minX, maxX)
                            offsetY = (currentY + dragAmount.y).coerceIn(minY, maxY)
                            velX = 0f
                            velY = 0f
                        }
                    )
                }
                .pointerInput(Unit) {
                    detectTapGestures(
                        onPress = {
                            isGrabbed = true
                            val released = tryAwaitRelease()
                            isGrabbed = false
                            if (released) {
                                squashVel += 3.6f // Spring upward on release!
                            }
                        },
                        onLongPress = {
                            activeFloatingElement = MascotElement.ELECTRIC
                            electricNeed = (electricNeed + 25).coerceAtMost(100)
                            showSmartCareHud = true
                            smartBubbleLine = "OVERCHARGE! Tahan lama mengisi penuh Energi Listrik ($electricNeed%)!"
                        },
                        onTap = {
                            clickComboCount++
                            val poses = SurpriseIdlePose.entries
                            activeSurprisePose = poses[clickComboCount % poses.size]
                            val elems = MascotElement.entries
                            activeFloatingElement = elems[clickComboCount % elems.size]
                            showSmartCareHud = !showSmartCareHud
                            isClickJumping = true
                            // Physics impulse: upward hop + angular kick + paper flutter
                            velY = -420f
                            velX = if (clickComboCount % 2 == 0) 140f else -140f
                            bodyTiltVel = if (clickComboCount % 2 == 0) 280f else -280f
                            squashVel = 4.2f
                            paperLagVelY = -240f
                            coroutineScope.launch {
                                launch {
                                    clickBurstAnim.snapTo(0.08f)
                                    clickBurstAnim.animateTo(1f, tween(580, easing = FastOutSlowInEasing))
                                    clickBurstAnim.snapTo(0f)
                                }
                                delay(3200L)
                                isClickJumping = false
                            }
                        },
                        onDoubleTap = {
                            clickComboCount += 2
                            activeFloatingElement = MascotElement.IDEA_BULB
                            ideaNeed = (ideaNeed + 25).coerceAtMost(100)
                            showSmartCareHud = true
                            smartBubbleLine = "TING! Ketuk Ganda menyalakan Lampu Ide Pintar ($ideaNeed%)!"
                            val poses = SurpriseIdlePose.entries
                            activeSurprisePose = poses[clickComboCount % poses.size]
                            // Super physics bounce + spin kick
                            velY = -620f
                            squashVel = 5.5f
                            bodyTiltVel = 520f
                            paperLagVelY = -320f
                        }
                    )
                }
                .testTag("interactive_mascot_overlay"),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .testTag("mascot_avatar_canvas")
            ) {
                val effectiveActivityPose = when {
                    AppThemeController.mascotActivityPose != MascotActivityPose.AUTO_SMART ->
                        AppThemeController.mascotActivityPose
                    activeTabName == "MEDIA_PLAYER" || activeTabName == "PHOTO_GALLERY" || AppThemeController.isMediaWatchingActive ->
                        MascotActivityPose.WATCHING_MEDIA
                    isProcessingStegoOrCrypto ->
                        MascotActivityPose.SHIELD_GUARD
                    else ->
                        MascotActivityPose.AUTO_SMART
                }
                PhysicsKawaiiFolderMascotCanvas(
                    mood = if (isHoveringDismissBox) MascotMoodState.SURPRISE_PEEK else currentMood,
                    surprisePose = if (isHoveringDismissBox) SurpriseIdlePose.SHOCKED_JOLT else activeSurprisePose,
                    timeSec = continuousTimeSec,
                    bodyTiltDeg = bodyTiltDeg,
                    paperLagY = paperLagY,
                    limbLagX = limbLagX,
                    limbLagY = limbLagY,
                    eyeGazeX = eyeGazeX,
                    eyeGazeY = eyeGazeY,
                    isGrabbed = isGrabbed,
                    downloadProgressPercent = if (isDownloading) downloadProgressPercent else null,
                    clickBurstProgress = clickBurstAnim.value,
                    activeElement = activeFloatingElement,
                    neededElement = floatingNeededElement,
                    activityPose = effectiveActivityPose,
                    accessory = AppThemeController.mascotAccessory,
                    cosmicAuraEnabled = AppThemeController.mascotCosmicAuraEnabled
                )
            }
        }

        // Confirmation Dialog when Mascot is Dropped into Bottom "Box X"
        if (showHideConfirmDialog) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.55f))
                    .clickable {
                        showHideConfirmDialog = false
                        offsetX = defaultX
                        offsetY = defaultY
                        velY = -320f
                        squashVel = 3.5f
                    }
                    .testTag("mascot_hide_confirm_backdrop"),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .padding(horizontal = 24.dp)
                        .widthIn(max = 360.dp)
                        .duolingoCard(
                            backgroundColor = DuoPureWhite,
                            borderColor = DuoCardinalRed,
                            lipColor = DuoCardinalRedLip,
                            cornerRadius = 26.dp,
                            borderWidth = 2.5.dp,
                            lipHeight = 6.dp
                        )
                        .clickable(enabled = false) {}
                        .padding(20.dp)
                        .testTag("mascot_hide_confirm_dialog")
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier.padding(bottom = 4.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(52.dp)
                                .duolingoButton(
                                    faceColor = DuoCardinalRedSoft,
                                    lipColor = DuoCardinalRed,
                                    cornerRadius = 18.dp,
                                    lipHeight = 4.dp
                                )
                                .padding(bottom = 4.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Sembunyikan Maskot",
                                tint = DuoCardinalRed,
                                modifier = Modifier.size(26.dp)
                            )
                        }

                        Text(
                            text = "Sembunyikan Maskot Overlay?",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.ExtraBold,
                            color = DuoEelInk
                        )

                        Text(
                            text = "Maskot folder dapat disembunyikan sementara dari layar. Jika ingin memunculkannya kembali, tekan tombol 'Maskot Overlay' di Beranda Brankas.",
                            style = MaterialTheme.typography.bodySmall,
                            color = CosmicTextSubtle,
                            modifier = Modifier.padding(horizontal = 4.dp)
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            // Pilihan 1: Tidak (Tetap Tampilkan)
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .height(46.dp)
                                    .duolingoButton(
                                        faceColor = DuoFeatherGreen,
                                        lipColor = DuoFeatherGreenLip,
                                        cornerRadius = 15.dp,
                                        lipHeight = 4.dp
                                    )
                                    .clickable {
                                        showHideConfirmDialog = false
                                        offsetX = defaultX
                                        offsetY = defaultY
                                        velY = -340f
                                        squashVel = 3.8f
                                    }
                                    .padding(bottom = 4.dp)
                                    .testTag("mascot_hide_cancel_button"),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "Tidak",
                                    style = MaterialTheme.typography.labelLarge,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = Color.White
                                )
                            }

                            // Pilihan 2: Ya, Sembunyikan Maskot
                            Box(
                                modifier = Modifier
                                    .weight(1.2f)
                                    .height(46.dp)
                                    .duolingoButton(
                                        faceColor = DuoCardinalRed,
                                        lipColor = DuoCardinalRedLip,
                                        cornerRadius = 15.dp,
                                        lipHeight = 4.dp
                                    )
                                    .clickable {
                                        showHideConfirmDialog = false
                                        offsetX = defaultX
                                        offsetY = defaultY
                                        onSetMascotVisible(false)
                                    }
                                    .padding(bottom = 4.dp)
                                    .testTag("mascot_hide_confirm_button"),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "Ya, Sembunyikan",
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
 * Shared Physics-Driven 3D Kawaii File Folder Mascot Renderer:
 * Draws the classic File Folder mascot upgraded with smooth multi-jointed physics:
 * - Articulated back tab + dual sliding document sheets with secondary spring lag
 * - Pendulum dangling 3D shoes/feet that swing with inertia and kick when held
 * - 3D squircle folder body with dynamic specular highlight that shifts with tilt angle
 * - Expressive saccadic + velocity-tracking eyes, natural double-blink, and multi-emotion FX
 *   (parabolic gravity tear fountain, buoyancy flame aura, lightning jolt, sakura blush, sleepy Zzz)
 * - Spring-jointed 3D hands with harmonic wave & velocity drag follow-through
 */
@Composable
internal fun PhysicsKawaiiFolderMascotCanvas(
    mood: MascotMoodState,
    surprisePose: SurpriseIdlePose?,
    timeSec: Float,
    bodyTiltDeg: Float,
    paperLagY: Float,
    limbLagX: Float,
    limbLagY: Float,
    eyeGazeX: Float,
    eyeGazeY: Float,
    isGrabbed: Boolean,
    downloadProgressPercent: Int?,
    clickBurstProgress: Float,
    activeElement: MascotElement? = null,
    neededElement: MascotElement? = null,
    activityPose: MascotActivityPose = AppThemeController.mascotActivityPose,
    accessory: MascotAccessory = AppThemeController.mascotAccessory,
    cosmicAuraEnabled: Boolean = AppThemeController.mascotCosmicAuraEnabled
) {
    // Natural double-blink every 3.6 seconds using smooth cosine curve
    val blinkCycle = timeSec % 3.6f
    val blinkHeightRatio = when {
        activityPose == MascotActivityPose.SLEEPING -> 0.08f
        blinkCycle in 2.80f..2.98f -> {
            val t = (blinkCycle - 2.80f) / 0.18f
            (1f - sin(t * PI).toFloat() * 0.92f).coerceIn(0.08f, 1f)
        }
        blinkCycle in 3.08f..3.24f -> {
            val t = (blinkCycle - 3.08f) / 0.16f
            (1f - sin(t * PI).toFloat() * 0.92f).coerceIn(0.08f, 1f)
        }
        else -> 1f
    }

    Canvas(modifier = Modifier.fillMaxSize()) {
        val w = size.width
        val h = size.height
        val cx = w * 0.5f
        val cy = h * 0.55f
        val r = (w.coerceAtMost(h) * 0.40f)

        // Determine Dynamic Color Palette from Active Element, Mood & Surprise Pose
        val bodyGradientColors: List<Color>
        val lipColor: Color
        val tabColors: List<Color>
        val handFootColor: Color
        val handFootLipColor: Color
        val auraGlowColor: Color

        when {
            activeElement == MascotElement.LOVE || surprisePose == SurpriseIdlePose.SHY_BLUSH -> {
                bodyGradientColors = listOf(Color(0xFFFCE7F3), Color(0xFFF472B6), Color(0xFFEC4899), Color(0xFFBE185D))
                lipColor = Color(0xFF9D174D)
                tabColors = listOf(Color(0xFFF9A8D4), Color(0xFFDB2777))
                handFootColor = Color(0xFFF472B6)
                handFootLipColor = Color(0xFFBE185D)
                auraGlowColor = Color(0xFFF472B6)
            }
            activeElement == MascotElement.ELECTRIC || surprisePose == SurpriseIdlePose.SHOCKED_JOLT -> {
                bodyGradientColors = listOf(Color(0xFFFEF9C3), Color(0xFFFDE047), Color(0xFFEAB308), Color(0xFFCA8A04))
                lipColor = Color(0xFFA16207)
                tabColors = listOf(Color(0xFF38BDF8), Color(0xFF0284C7))
                handFootColor = Color(0xFFEAB308)
                handFootLipColor = Color(0xFFA16207)
                auraGlowColor = Color(0xFFFACC15)
            }
            activeElement == MascotElement.IDEA_BULB || mood == MascotMoodState.SCANNING_IMPORT || mood == MascotMoodState.CLICK_EXCITED || surprisePose == SurpriseIdlePose.HAPPY_BOUNCE -> {
                bodyGradientColors = listOf(Color(0xFFBBF7D0), Color(0xFF4ADE80), DuoFeatherGreen, DuoFeatherGreenLip)
                lipColor = Color(0xFF14532D)
                tabColors = listOf(Color(0xFFFDE047), Color(0xFFF59E0B))
                handFootColor = DuoFeatherGreen
                handFootLipColor = DuoFeatherGreenLip
                auraGlowColor = DuoFeatherGreen
            }
            activeElement == MascotElement.FIRE || surprisePose == SurpriseIdlePose.RAGE_INFERNO || mood == MascotMoodState.DELETING_CLEANUP -> {
                bodyGradientColors = listOf(Color(0xFFFCA5A5), Color(0xFFEF4444), Color(0xFFDC2626), Color(0xFF991B1B))
                lipColor = Color(0xFF7F1D1D)
                tabColors = listOf(Color(0xFFF97316), Color(0xFFDC2626))
                handFootColor = Color(0xFFEA580C)
                handFootLipColor = Color(0xFF9A3412)
                auraGlowColor = Color(0xFFEF4444)
            }
            activeElement == MascotElement.TEARS || surprisePose == SurpriseIdlePose.CRYING_FOUNTAIN -> {
                bodyGradientColors = listOf(Color(0xFFBAE6FD), Color(0xFF38BDF8), Color(0xFF0284C7), Color(0xFF0369A1))
                lipColor = Color(0xFF075985)
                tabColors = listOf(Color(0xFF7DD3FC), Color(0xFF0284C7))
                handFootColor = Color(0xFF38BDF8)
                handFootLipColor = Color(0xFF0369A1)
                auraGlowColor = Color(0xFF38BDF8)
            }
            activeElement == MascotElement.SHIELD || surprisePose == SurpriseIdlePose.SLEEPY_DREAM || mood == MascotMoodState.ENCRYPTING_STEGO -> {
                bodyGradientColors = listOf(Color(0xFFF3E8FF), Color(0xFFC084FC), Color(0xFF9333EA), Color(0xFF6B21A8))
                lipColor = Color(0xFF581C87)
                tabColors = listOf(Color(0xFFD8B4FE), Color(0xFF7E22CE))
                handFootColor = Color(0xFFA855F7)
                handFootLipColor = Color(0xFF6B21A8)
                auraGlowColor = Color(0xFFA855F7)
            }
            mood == MascotMoodState.DOWNLOADING -> {
                bodyGradientColors = listOf(Color(0xFFE0F2FE), Color(0xFF38BDF8), DuoMacawBlue, DuoMacawBlueLip)
                lipColor = Color(0xFF0369A1)
                tabColors = listOf(DuoBeeYellow, DuoFoxOrange)
                handFootColor = DuoMacawBlue
                handFootLipColor = DuoMacawBlueLip
                auraGlowColor = DuoMacawBlue
            }
            else -> {
                // Classic Warm Golden Kawaii Folder
                bodyGradientColors = listOf(Color(0xFFFFE680), Color(0xFFFBBF24), Color(0xFFF59E0B), Color(0xFFD97706))
                lipColor = Color(0xFFB45309)
                tabColors = listOf(Color(0xFF38BDF8), Color(0xFF0284C7))
                handFootColor = Color(0xFFF59E0B)
                handFootLipColor = Color(0xFFB45309)
                auraGlowColor = Color(0xFFF59E0B)
            }
        }

        val activeSkin = AppThemeController.mascotSkin
        val activeOutfit = AppThemeController.mascotOutfit
        val activeGloves = AppThemeController.mascotGloves
        val resolvedSkin = resolveMascotSkinPalette(
            skin = activeSkin,
            defaultBodyGradient = bodyGradientColors,
            defaultLip = lipColor,
            defaultTab = tabColors,
            defaultHand = handFootColor,
            defaultHandLip = handFootLipColor
        )
        val finalBodyGradient = resolvedSkin.bodyGradientColors
        val finalLipColor = resolvedSkin.lipColor
        val finalTabColors = resolvedSkin.tabColors
        val finalHandColor = resolvedSkin.handColor
        val finalHandLipColor = resolvedSkin.handLipColor

        // 0A. Multi-Layered Dynamic Aura (16 Styles) + Soft Radial Emotion Aura
        if (cosmicAuraEnabled || AppThemeController.mascotAura != com.example.ui.theme.MascotAuraStyle.NONE) {
            drawMascotMultiLayeredCosmicAura(
                cx = cx,
                cy = cy,
                r = r,
                timeSec = timeSec,
                primaryAuraColor = auraGlowColor,
                secondaryAuraColor = AppThemeController.resolvedSecondaryAccent
            )
        }

        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(
                    auraGlowColor.copy(alpha = if (isGrabbed) 0.45f else 0.28f),
                    auraGlowColor.copy(alpha = 0.10f),
                    Color.Transparent
                ),
                center = Offset(cx, cy),
                radius = r * 1.45f
            ),
            radius = r * 1.42f,
            center = Offset(cx, cy)
        )

        if (downloadProgressPercent != null) {
            val sweep = (downloadProgressPercent.coerceIn(5, 100) / 100f) * 360f
            drawArc(
                color = DuoBeeYellow,
                startAngle = -90f,
                sweepAngle = sweep,
                useCenter = false,
                topLeft = Offset(w * 0.04f, h * 0.04f),
                size = Size(w * 0.92f, h * 0.92f),
                style = Stroke(width = 6.5f, cap = StrokeCap.Round)
            )
        }

        // 0B. Buoyancy Flame Aura when RAGE_INFERNO or DELETING_CLEANUP
        if (surprisePose == SurpriseIdlePose.RAGE_INFERNO || mood == MascotMoodState.DELETING_CLEANUP) {
            for (flameIdx in 0..7) {
                val angle = (flameIdx / 8f) * PI.toFloat() + PI.toFloat()
                val flamePhase = (timeSec * 1.4f + flameIdx * 0.13f) % 1f
                val baseX = cx + cos(angle) * (r * 0.90f)
                val baseY = cy + sin(angle) * (r * 0.62f)
                val flameTipY = baseY - (r * (0.42f + flamePhase * 0.52f))
                val flameW = r * (0.26f * (1f - flamePhase * 0.45f))
                val flamePath = Path().apply {
                    moveTo(baseX - flameW, baseY + r * 0.18f)
                    quadraticTo(
                        baseX - flameW * 0.5f,
                        (baseY + flameTipY) * 0.5f,
                        baseX + sin((flamePhase + flameIdx) * PI * 2).toFloat() * (r * 0.10f),
                        flameTipY
                    )
                    quadraticTo(
                        baseX + flameW * 0.5f,
                        (baseY + flameTipY) * 0.5f,
                        baseX + flameW,
                        baseY + r * 0.18f
                    )
                    close()
                }
                drawPath(
                    path = flamePath,
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFFFDE047).copy(alpha = 0.9f * (1f - flamePhase * 0.4f)),
                            Color(0xFFF97316).copy(alpha = 0.85f * (1f - flamePhase * 0.3f)),
                            Color(0xFFDC2626).copy(alpha = 0.8f)
                        ),
                        startY = flameTipY,
                        endY = baseY + r * 0.18f
                    )
                )
            }
        }

        // 1. Dynamic Ground Shadow (Scales inversely with height / jump)
        val shadowScale = (1f - (if (isGrabbed) 0.28f else 0f) + sin(timeSec * 3.1f) * 0.08f).coerceIn(0.45f, 1.15f)
        drawOval(
            color = if (surprisePose == SurpriseIdlePose.CRYING_FOUNTAIN) {
                Color(0xFF38BDF8).copy(alpha = 0.42f)
            } else {
                lipColor.copy(alpha = 0.28f * shadowScale)
            },
            topLeft = Offset(cx - r * 0.80f * shadowScale, h * 0.86f),
            size = Size(r * 1.60f * shadowScale, h * 0.09f)
        )

        // 2. Floating Mascot Anatomy (No Legs / No Pants) + Levitation Hover Rings
        val isDancingPose = activityPose == MascotActivityPose.DANCING
        drawMascotFloatingHoverRings(
            cx = cx,
            cy = cy,
            r = r,
            timeSec = timeSec,
            accentColor = auraGlowColor
        )

        val poseSpinAngle = when (activityPose) {
            MascotActivityPose.ACROBATIC_SPIN -> (timeSec * 240f) % 360f
            MascotActivityPose.DANCING -> sin(timeSec * 8.5f) * 9f
            MascotActivityPose.WATCHING_MEDIA -> sin(timeSec * 2.2f) * 3.5f
            MascotActivityPose.SLEEPING -> -4f + sin(timeSec * 1.6f) * 2f
            else -> 0f
        }
        val poseVerticalOffset = when (activityPose) {
            MascotActivityPose.ACROBATIC_SPIN -> -abs(sin(timeSec * 4.2f)) * (r * 0.28f)
            MascotActivityPose.DANCING -> -abs(sin(timeSec * 8.5f)) * (r * 0.16f)
            MascotActivityPose.SLEEPING -> sin(timeSec * 1.8f) * (r * 0.08f)
            else -> 0f
        }

        withTransform({
            translate(left = 0f, top = poseVerticalOffset)
            rotate(degrees = poseSpinAngle, pivot = Offset(cx, cy))
        }) {
            // Back Wings & Royal/Mage Cape Layer (Behind Folder Body)
            drawMascotBackAccessoriesAndCape(
                cx = cx,
                cy = cy,
                r = r,
                timeSec = timeSec,
                accessory = accessory,
                outfit = activeOutfit
            )

            // 3. Back Folder Tab & Multi-Sheet Document Papers with Secondary Follow-Through Lag
            val tabW = r * 0.88f
            val tabH = r * 0.52f
            val tabX = cx - r * 0.82f
            val tabY = cy - r * 0.90f + paperLagY * 0.35f
            drawRoundRect(
                color = DuoPureWhite,
                topLeft = Offset(tabX - r * 0.04f, tabY - r * 0.04f),
                size = Size(tabW + r * 0.08f, tabH + r * 0.08f),
                cornerRadius = CornerRadius(r * 0.18f, r * 0.18f)
            )
            drawRoundRect(
                brush = Brush.linearGradient(
                    finalTabColors,
                    start = Offset(tabX, tabY),
                    end = Offset(tabX + tabW, tabY + tabH)
                ),
                topLeft = Offset(tabX, tabY),
                size = Size(tabW, tabH),
                cornerRadius = CornerRadius(r * 0.16f, r * 0.16f)
            )

        // Back Document Sheet #1 (Tilted Pastel Sheet with Secondary Spring Lag)
        val docTilt1 = -4.5f + (limbLagX * 0.45f) + sin(timeSec * 3.4f) * 2.2f
        rotate(degrees = docTilt1, pivot = Offset(cx, cy - r * 0.55f)) {
            val p1W = r * 0.98f
            val p1H = r * 0.46f
            val p1X = cx - r * 0.38f
            val p1Y = cy - r * 0.97f + paperLagY * 1.15f
            drawRoundRect(
                color = Color(0xFFE0F2FE),
                topLeft = Offset(p1X, p1Y),
                size = Size(p1W, p1H),
                cornerRadius = CornerRadius(r * 0.11f, r * 0.11f)
            )
        }

        // Front Document Sheet #2 (Crisp White Document with Header Lines & Secondary Spring Lag)
        val docTilt2 = 2.8f - (limbLagX * 0.35f) + cos(timeSec * 3.1f) * 1.8f
        rotate(degrees = docTilt2, pivot = Offset(cx, cy - r * 0.55f)) {
            val paperW = r * 1.08f
            val paperH = r * 0.46f
            val paperX = cx - r * 0.52f
            val paperY = cy - r * 0.93f + paperLagY
            drawRoundRect(
                color = Color.White,
                topLeft = Offset(paperX, paperY),
                size = Size(paperW, paperH),
                cornerRadius = CornerRadius(r * 0.11f, r * 0.11f)
            )
            // Document colored header lines
            drawLine(
                color = tabColors.first().copy(alpha = 0.65f),
                start = Offset(paperX + r * 0.15f, paperY + r * 0.13f),
                end = Offset(paperX + paperW - r * 0.15f, paperY + r * 0.13f),
                strokeWidth = (w * 0.020f).coerceAtLeast(2f),
                cap = StrokeCap.Round
            )
            drawLine(
                color = lipColor.copy(alpha = 0.35f),
                start = Offset(paperX + r * 0.15f, paperY + r * 0.24f),
                end = Offset(paperX + paperW - r * 0.38f, paperY + r * 0.24f),
                strokeWidth = (w * 0.016f).coerceAtLeast(1.5f),
                cap = StrokeCap.Round
            )
        }

        // 4. Main 3D Kawaii Folder Squircle Body (With Crisp White Sticker Rim & Chunky 3D Lip)
        val bodyW = r * 1.88f
        val bodyH = r * 1.52f
        val bodyX = cx - r * 0.94f
        val bodyY = cy - r * 0.68f
        val bodyRadius = r * 0.36f

        // Crisp white sticker border for high visibility on any background
        drawRoundRect(
            color = DuoPureWhite,
            topLeft = Offset(bodyX - r * 0.05f, bodyY - r * 0.05f),
            size = Size(bodyW + r * 0.10f, bodyH + r * 0.18f),
            cornerRadius = CornerRadius(bodyRadius + r * 0.04f, bodyRadius + r * 0.04f)
        )
        // Chunky 3D Bottom Lip of Folder Body
        drawRoundRect(
            color = finalLipColor,
            topLeft = Offset(bodyX, bodyY + r * 0.09f),
            size = Size(bodyW, bodyH),
            cornerRadius = CornerRadius(bodyRadius, bodyRadius)
        )
        // Main Gradient Folder Body (Skin Material Aware)
        drawRoundRect(
            brush = Brush.verticalGradient(
                colors = finalBodyGradient,
                startY = bodyY,
                endY = bodyY + bodyH
            ),
            topLeft = Offset(bodyX, bodyY),
            size = Size(bodyW, bodyH),
            cornerRadius = CornerRadius(bodyRadius, bodyRadius)
        )

        // 4B. Custom Skin Material Surface Texture (Gold, Diamond, Mecha, Nebula, Lava, etc.)
        drawMascotSkinSurfaceTexture(
            bodyX = bodyX,
            bodyY = bodyY,
            bodyW = bodyW,
            bodyH = bodyH,
            r = r,
            timeSec = timeSec,
            skin = activeSkin
        )

        // 4C. Upper Outfit / Shirt / Cloak / Armor Layer (No Pants because Mascot has No Legs!)
        drawMascotUpperOutfitLayer(
            bodyX = bodyX,
            bodyY = bodyY,
            bodyW = bodyW,
            bodyH = bodyH,
            cx = cx,
            cy = cy,
            r = r,
            outfit = activeOutfit
        )

        // Subtle Folder Pocket Crease Line near bottom (when no outfit covers it)
        if (activeOutfit == com.example.ui.theme.MascotUpperOutfit.NONE) {
            drawLine(
                color = finalLipColor.copy(alpha = 0.22f),
                start = Offset(bodyX + r * 0.20f, bodyY + bodyH - r * 0.18f),
                end = Offset(bodyX + bodyW - r * 0.20f, bodyY + bodyH - r * 0.18f),
                strokeWidth = (w * 0.018f).coerceAtLeast(1.5f),
                cap = StrokeCap.Round
            )
        }

        // Dynamic Specular Gloss Highlight (Shifts smoothly opposite to bodyTiltDeg!)
        val glossShiftX = (-bodyTiltDeg / 30f) * (r * 0.14f)
        drawRoundRect(
            color = Color.White.copy(alpha = 0.48f),
            topLeft = Offset(bodyX + r * 0.18f + glossShiftX, bodyY + r * 0.08f),
            size = Size(bodyW - r * 0.36f, r * 0.14f),
            cornerRadius = CornerRadius(r * 0.07f, r * 0.07f)
        )
        drawCircle(
            color = Color.White.copy(alpha = 0.65f),
            radius = r * 0.075f,
            center = Offset(bodyX + r * 0.28f + glossShiftX, bodyY + r * 0.33f)
        )

        // 5. Expressive Eyes, Saccadic + Velocity Gaze Tracking & Tear Geysers
        val eyeW = r * 0.28f
        val fullEyeH = r * 0.38f
        val eyeH = (fullEyeH * blinkHeightRatio).coerceAtLeast(r * 0.04f)
        val gazeOffsetX = eyeGazeX * (r * 0.09f)
        val gazeOffsetY = eyeGazeY * (r * 0.07f)
        val leftEyeX = cx - r * 0.42f + gazeOffsetX
        val rightEyeX = cx + r * 0.42f + gazeOffsetX
        val eyeY = cy - r * 0.06f + gazeOffsetY

        val customEmotionDrawn = drawMascotExpressiveEmotionFace(
            cx = cx,
            cy = cy,
            r = r,
            w = w,
            timeSec = timeSec,
            leftEyeX = leftEyeX,
            rightEyeX = rightEyeX,
            eyeY = eyeY,
            eyeW = eyeW,
            eyeH = eyeH,
            fullEyeH = fullEyeH,
            emotion = AppThemeController.mascotEmotion
        )

        if (!customEmotionDrawn) {
        when {
            surprisePose == SurpriseIdlePose.CRYING_FOUNTAIN -> {
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

                // Parabolic Gravity Tear Geysers
                val fountainStroke = (w * 0.052f).coerceAtLeast(3.6f)
                val leftGeyser = Path().apply {
                    moveTo(leftEyeX, eyeY)
                    cubicTo(
                        leftEyeX - r * 0.22f, eyeY - r * 0.82f,
                        leftEyeX - r * 0.90f, eyeY - r * 0.52f,
                        leftEyeX - r * 1.05f, cy + r * 0.84f
                    )
                }
                val rightGeyser = Path().apply {
                    moveTo(rightEyeX, eyeY)
                    cubicTo(
                        rightEyeX + r * 0.22f, eyeY - r * 0.82f,
                        rightEyeX + r * 0.90f, eyeY - r * 0.52f,
                        rightEyeX + r * 1.05f, cy + r * 0.84f
                    )
                }
                drawPath(
                    path = leftGeyser,
                    brush = Brush.verticalGradient(listOf(Color(0xFFE0F2FE), Color(0xFF38BDF8), Color(0xFF0284C7))),
                    style = Stroke(width = fountainStroke, cap = StrokeCap.Round)
                )
                drawPath(
                    path = rightGeyser,
                    brush = Brush.verticalGradient(listOf(Color(0xFFE0F2FE), Color(0xFF38BDF8), Color(0xFF0284C7))),
                    style = Stroke(width = fountainStroke, cap = StrokeCap.Round)
                )
                for (dropIdx in 0..4) {
                    val t = (timeSec * 1.3f + dropIdx * 0.2f) % 1f
                    // Parabolic projectile motion y = v0*t - 0.5*g*t^2
                    val arcY = eyeY - sin(t * PI).toFloat() * (r * 0.65f) + (t * t * r * 0.92f)
                    val leftDropX = leftEyeX - t * (r * 1.0f)
                    val rightDropX = rightEyeX + t * (r * 1.0f)
                    val dropR = (r * 0.075f) * (1f - t * 0.25f)
                    drawCircle(Color(0xFFE0F2FE), radius = dropR, center = Offset(leftDropX, arcY))
                    drawCircle(Color(0xFFE0F2FE), radius = dropR, center = Offset(rightDropX, arcY))
                }
            }

            surprisePose == SurpriseIdlePose.RAGE_INFERNO || mood == MascotMoodState.DELETING_CLEANUP -> {
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

            surprisePose == SurpriseIdlePose.SLEEPY_DREAM -> {
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

            surprisePose == SurpriseIdlePose.HAPPY_BOUNCE || surprisePose == SurpriseIdlePose.SHY_BLUSH || mood == MascotMoodState.CLICK_EXCITED -> {
                val leftArch = Path().apply {
                    moveTo(leftEyeX - eyeW * 0.5f, eyeY + eyeH * 0.15f)
                    quadraticTo(leftEyeX, eyeY - eyeH * 0.55f, leftEyeX + eyeW * 0.5f, eyeY + eyeH * 0.15f)
                }
                val rightArch = Path().apply {
                    moveTo(rightEyeX - eyeW * 0.5f, eyeY + eyeH * 0.15f)
                    quadraticTo(rightEyeX, eyeY - eyeH * 0.55f, rightEyeX + eyeW * 0.5f, eyeY + eyeH * 0.15f)
                }
                val archStroke = (w * 0.034f).coerceAtLeast(2.5f)
                drawPath(leftArch, color = Color(0xFF0F172A), style = Stroke(width = archStroke, cap = StrokeCap.Round))
                drawPath(rightArch, color = Color(0xFF0F172A), style = Stroke(width = archStroke, cap = StrokeCap.Round))
            }

            else -> {
                val shockScale = if (surprisePose == SurpriseIdlePose.SHOCKED_JOLT || isGrabbed) 1.18f else 1f
                drawOval(
                    color = Color(0xFF0F172A),
                    topLeft = Offset(leftEyeX - eyeW * 0.5f * shockScale, eyeY - eyeH * 0.5f * shockScale),
                    size = Size(eyeW * shockScale, eyeH * shockScale)
                )
                drawOval(
                    color = Color(0xFF1E3A8A),
                    topLeft = Offset(leftEyeX - eyeW * 0.42f, eyeY - eyeH * 0.40f),
                    size = Size(eyeW * 0.84f, eyeH * 0.80f)
                )
                if (blinkHeightRatio > 0.35f) {
                    drawCircle(Color.White, radius = eyeW * 0.25f, center = Offset(leftEyeX - eyeW * 0.14f + gazeOffsetX * 0.25f, eyeY - eyeH * 0.22f))
                    drawCircle(Color.White, radius = eyeW * 0.12f, center = Offset(leftEyeX + eyeW * 0.16f, eyeY + eyeH * 0.18f))
                }

                drawOval(
                    color = Color(0xFF0F172A),
                    topLeft = Offset(rightEyeX - eyeW * 0.5f * shockScale, eyeY - eyeH * 0.5f * shockScale),
                    size = Size(eyeW * shockScale, eyeH * shockScale)
                )
                drawOval(
                    color = Color(0xFF1E3A8A),
                    topLeft = Offset(rightEyeX - eyeW * 0.42f, eyeY - eyeH * 0.40f),
                    size = Size(eyeW * 0.84f, eyeH * 0.80f)
                )
                if (blinkHeightRatio > 0.35f) {
                    drawCircle(Color.White, radius = eyeW * 0.25f, center = Offset(rightEyeX - eyeW * 0.14f + gazeOffsetX * 0.25f, eyeY - eyeH * 0.22f))
                    drawCircle(Color.White, radius = eyeW * 0.12f, center = Offset(rightEyeX + eyeW * 0.16f, eyeY + eyeH * 0.18f))
                }
            }
        }

        // 6. Rosy Blush Cheeks
        val blushW = if (surprisePose == SurpriseIdlePose.SHY_BLUSH) r * 0.30f else r * 0.23f
        val blushH = if (surprisePose == SurpriseIdlePose.SHY_BLUSH) r * 0.16f else r * 0.12f
        val blushAlpha = if (surprisePose == SurpriseIdlePose.SHY_BLUSH || isGrabbed) 0.82f else 0.52f
        drawOval(
            color = Color(0xFFF43F5E).copy(alpha = blushAlpha),
            topLeft = Offset(cx - r * 0.68f + gazeOffsetX * 0.5f, cy + r * 0.18f),
            size = Size(blushW, blushH)
        )
        drawOval(
            color = Color(0xFFF43F5E).copy(alpha = blushAlpha),
            topLeft = Offset(cx + r * 0.46f + gazeOffsetX * 0.5f, cy + r * 0.18f),
            size = Size(blushW, blushH)
        )

        // 7. Expressive Kawaii Mouth
        val mouthY = cy + r * 0.22f + gazeOffsetY * 0.5f
        when {
            surprisePose == SurpriseIdlePose.CRYING_FOUNTAIN -> {
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
            surprisePose == SurpriseIdlePose.RAGE_INFERNO || mood == MascotMoodState.DELETING_CLEANUP -> {
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
            surprisePose == SurpriseIdlePose.SHOCKED_JOLT || surprisePose == SurpriseIdlePose.SLEEPY_DREAM || isGrabbed -> {
                drawCircle(color = Color(0xFF0F172A), radius = r * 0.095f, center = Offset(cx + gazeOffsetX * 0.4f, mouthY + r * 0.05f))
            }
            mood == MascotMoodState.CLICK_EXCITED || surprisePose == SurpriseIdlePose.HAPPY_BOUNCE -> {
                val openMouth = Path().apply {
                    moveTo(cx - r * 0.19f, mouthY)
                    quadraticTo(cx, mouthY + r * 0.24f, cx + r * 0.19f, mouthY)
                    close()
                }
                drawPath(openMouth, color = Color(0xFF0F172A))
                drawCircle(color = Color(0xFFF43F5E), radius = r * 0.085f, center = Offset(cx, mouthY + r * 0.12f))
            }
            else -> {
                val smilePath = Path().apply {
                    moveTo(cx - r * 0.16f + gazeOffsetX * 0.4f, mouthY)
                    quadraticTo(cx + gazeOffsetX * 0.4f, mouthY + r * 0.16f, cx + r * 0.16f + gazeOffsetX * 0.4f, mouthY)
                }
                drawPath(
                    smilePath,
                    color = Color(0xFF0F172A),
                    style = Stroke(width = (w * 0.030f).coerceAtLeast(2.4f), cap = StrokeCap.Round)
                )
            }
        }
        }

        // 8. Spring-Jointed 3D Folder Hands (Smooth Harmonic Wave + Pose Choreography)
        val harmonicWave = sin(timeSec * (if (isGrabbed) 11f else if (isDancingPose) 8.5f else 4.2f)) + 0.28f * sin(timeSec * 8.4f)
        val leftHandCenter = when (activityPose) {
            MascotActivityPose.DANCING -> Offset(
                x = cx - r * 1.12f + cos(timeSec * 8.5f) * (r * 0.12f),
                y = cy - r * 0.22f - sin(timeSec * 8.5f) * (r * 0.26f)
            )
            MascotActivityPose.READING, MascotActivityPose.GAMING_PRO_PAD -> Offset(
                x = cx - r * 0.56f,
                y = cy + r * 0.44f + sin(timeSec * 3.2f) * (r * 0.04f)
            )
            MascotActivityPose.WATCHING_MEDIA, MascotActivityPose.EATING_BOBA_SNACK, MascotActivityPose.COFFEE_CHILL -> Offset(
                x = cx - r * 0.86f,
                y = cy + r * 0.28f + sin(timeSec * 4.5f) * (r * 0.05f)
            )
            MascotActivityPose.GYM_DUMBBELL_LIFT -> Offset(
                x = cx - r * 0.65f,
                y = cy - r * 0.90f - abs(sin(timeSec * 4.2f)) * r * 0.28f
            )
            else -> Offset(
                x = cx - r * 1.05f + limbLagX * 0.9f,
                y = cy + r * 0.06f - harmonicWave * (r * 0.13f) + limbLagY * 1.15f
            )
        }
        val rightHandCenter = when (activityPose) {
            MascotActivityPose.DANCING -> Offset(
                x = cx + r * 1.12f - cos(timeSec * 8.5f) * (r * 0.12f),
                y = cy - r * 0.22f + sin(timeSec * 8.5f) * (r * 0.26f)
            )
            MascotActivityPose.READING, MascotActivityPose.GAMING_PRO_PAD -> Offset(
                x = cx + r * 0.56f,
                y = cy + r * 0.44f + sin(timeSec * 3.2f) * (r * 0.04f)
            )
            MascotActivityPose.SHIELD_GUARD, MascotActivityPose.KATANA_SLASH, MascotActivityPose.ARCANE_SPELLCAST, MascotActivityPose.SELFIE_FLASH_POSE -> Offset(
                x = cx + r * 0.88f,
                y = cy + r * 0.16f + sin(timeSec * 3.0f) * (r * 0.05f)
            )
            MascotActivityPose.WATCHING_MEDIA, MascotActivityPose.EATING_BOBA_SNACK -> Offset(
                x = cx + r * 0.72f,
                y = cy + r * 0.18f - abs(sin(timeSec * 5.2f)) * (r * 0.14f)
            )
            MascotActivityPose.GYM_DUMBBELL_LIFT -> Offset(
                x = cx + r * 0.65f,
                y = cy - r * 0.90f - abs(sin(timeSec * 4.2f)) * r * 0.28f
            )
            else -> Offset(
                x = cx + r * 1.05f + limbLagX * 0.9f,
                y = cy + r * 0.04f + harmonicWave * (r * 0.13f) + limbLagY * 1.15f
            )
        }

        // 8A. Floating Hands with Custom Gloves (10 Glove Styles)
        drawMascotCustomGlovedHands(
            leftHandCenter = leftHandCenter,
            rightHandCenter = rightHandCenter,
            r = r,
            defaultHandColor = finalHandColor,
            defaultHandLipColor = finalHandLipColor,
            gloves = activeGloves
        )

        // 8B. Complex Accessories & Activity Props (Watching 3D Glasses + Popcorn, Reading Cyber-Book, Aegis Shield, Sleeping Zzz, Dancing Notes, Crown, Wizard Hat, Headphones, Halo)
        drawMascotComplexAccessoriesAndPoses(
            cx = cx,
            cy = cy,
            r = r,
            timeSec = timeSec,
            leftEyeX = leftEyeX,
            rightEyeX = rightEyeX,
            eyeY = eyeY,
            leftHandCenter = leftHandCenter,
            rightHandCenter = rightHandCenter,
            activityPose = activityPose,
            accessory = accessory
        )
        } // end withTransform for poseSpinAngle & poseVerticalOffset

        // Foreground Elemental FX Overlays (LOVE / LISTRIK / LAMPU IDE / PERISAI)
        val fxFlowPhase = (timeSec * 0.9f) % 1f
        if (activeElement == MascotElement.LOVE || surprisePose == SurpriseIdlePose.SHY_BLUSH) {
            val heartAnchors = listOf(
                Offset(cx - r * 0.92f, cy - r * 0.35f),
                Offset(cx + r * 0.92f, cy - r * 0.45f),
                Offset(cx, cy - r * 1.05f)
            )
            heartAnchors.forEachIndexed { idx, anchor ->
                val phase = (fxFlowPhase + idx * 0.33f) % 1f
                val hx = anchor.x + sin((phase + idx) * PI * 2).toFloat() * (r * 0.10f)
                val hy = anchor.y - phase * (r * 0.38f)
                val hs = (r * 0.20f) * (0.75f + sin(phase * PI).toFloat() * 0.4f)
                val heartPath = Path().apply {
                    moveTo(hx, hy + hs * 0.65f)
                    cubicTo(hx - hs * 1.3f, hy - hs * 0.15f, hx - hs * 0.65f, hy - hs * 1.15f, hx, hy - hs * 0.35f)
                    cubicTo(hx + hs * 0.65f, hy - hs * 1.15f, hx + hs * 1.3f, hy - hs * 0.15f, hx, hy + hs * 0.65f)
                    close()
                }
                drawPath(heartPath, color = if (idx % 2 == 0) Color(0xFFEC4899) else Color(0xFFF43F5E))
            }
        }

        if (activeElement == MascotElement.ELECTRIC || surprisePose == SurpriseIdlePose.SHOCKED_JOLT) {
            val boltCenters = listOf(
                Offset(cx - r * 1.02f, cy - r * 0.45f),
                Offset(cx + r * 1.02f, cy - r * 0.40f),
                Offset(cx, cy - r * 1.10f)
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
            }
        }

        if (activeElement == MascotElement.IDEA_BULB) {
            val bulbCenterX = cx
            val bulbCenterY = cy - r * 1.12f
            val bulbR = r * 0.26f
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(Color(0xFFFDE047).copy(alpha = 0.85f), Color.Transparent),
                    center = Offset(bulbCenterX, bulbCenterY),
                    radius = bulbR * 2.2f
                ),
                radius = bulbR * 2.2f,
                center = Offset(bulbCenterX, bulbCenterY)
            )
            drawCircle(
                color = Color(0xFFFACC15),
                radius = bulbR,
                center = Offset(bulbCenterX, bulbCenterY)
            )
            drawRoundRect(
                color = Color(0xFF64748B),
                topLeft = Offset(bulbCenterX - bulbR * 0.46f, bulbCenterY + bulbR * 0.70f),
                size = Size(bulbR * 0.92f, bulbR * 0.42f),
                cornerRadius = CornerRadius(bulbR * 0.16f, bulbR * 0.16f)
            )
        }

        if (activeElement == MascotElement.SHIELD || mood == MascotMoodState.ENCRYPTING_STEGO) {
            val hexR = r * 1.16f
            val hexPath = Path().apply {
                for (i in 0..5) {
                    val a = (i * 60f - 30f) * (PI.toFloat() / 180f) + fxFlowPhase * 0.5f
                    val hx = cx + cos(a) * hexR
                    val hy = cy + sin(a) * hexR
                    if (i == 0) moveTo(hx, hy) else lineTo(hx, hy)
                }
                close()
            }
            drawPath(
                path = hexPath,
                color = Color(0xFF8B5CF6).copy(alpha = 0.75f),
                style = Stroke(width = (w * 0.030f).coerceAtLeast(2f), cap = StrokeCap.Round)
            )
        }

        if (neededElement != null && activeElement == null) {
            val bubbleCenterX = cx + r * 0.78f
            val bubbleCenterY = cy - r * 1.00f
            val bubbleR = r * 0.32f
            drawCircle(color = neededElement.badgeColor, radius = bubbleR * 1.12f, center = Offset(bubbleCenterX, bubbleCenterY))
            drawCircle(color = Color.White, radius = bubbleR, center = Offset(bubbleCenterX, bubbleCenterY))
            drawCircle(color = neededElement.badgeColor, radius = bubbleR * 0.45f, center = Offset(bubbleCenterX, bubbleCenterY))
        }

        // 9. Ambient Floating Sparkles & Click Burst Confetti
        val sparkleBreath = 0.5f + 0.5f * sin(timeSec * 3.5f)
        val starRadius = (w * 0.042f) * (0.85f + 0.3f * sparkleBreath)
        val starPositions = listOf(
            Offset(w * 0.13f, h * (0.20f - sparkleBreath * 0.04f)),
            Offset(w * 0.87f, h * (0.18f - (1f - sparkleBreath) * 0.04f)),
            Offset(w * 0.88f, h * (0.72f - sparkleBreath * 0.03f)),
            Offset(w * 0.14f, h * (0.75f + sparkleBreath * 0.03f))
        )
        starPositions.forEachIndexed { idx, pos ->
            val starColor = when (idx % 3) {
                0 -> DuoBeeYellow
                1 -> DuoMacawBlue
                else -> DuoFeatherGreen
            }
            drawLine(
                color = starColor.copy(alpha = 0.78f),
                start = Offset(pos.x - starRadius, pos.y),
                end = Offset(pos.x + starRadius, pos.y),
                strokeWidth = (w * 0.022f).coerceAtLeast(1.5f),
                cap = StrokeCap.Round
            )
            drawLine(
                color = starColor.copy(alpha = 0.78f),
                start = Offset(pos.x, pos.y - starRadius),
                end = Offset(pos.x, pos.y + starRadius),
                strokeWidth = (w * 0.022f).coerceAtLeast(1.5f),
                cap = StrokeCap.Round
            )
        }

        if (clickBurstProgress > 0.02f) {
            val burstRadius = w * (0.34f + clickBurstProgress * 0.56f)
            val alpha = (1f - clickBurstProgress).coerceIn(0f, 1f)
            for (i in 0 until 8) {
                val angleRad = ((i * 45f + timeSec * 120f) * (PI / 180.0)).toFloat()
                val bx = cx + cos(angleRad) * burstRadius
                val by = cy + sin(angleRad) * burstRadius
                val c = when (i % 4) {
                    0 -> DuoBeeYellow
                    1 -> DuoFeatherGreen
                    2 -> DuoMacawBlue
                    else -> DuoCardinalRed
                }
                drawCircle(
                    color = c.copy(alpha = alpha),
                    radius = 5.5f * (1f - clickBurstProgress * 0.35f),
                    center = Offset(bx, by)
                )
            }
        }
    }
}

/**
 * Shared Multi-Layered Cosmic Aura Renderer for Mascot:
 * Renders counter-rotating sacred geometry octagon, tilted orbital plasma ring with starlight beads,
 * and pulsing nebula rings around the mascot body.
 */
internal fun DrawScope.drawMascotMultiLayeredCosmicAura(
    cx: Float,
    cy: Float,
    r: Float,
    timeSec: Float,
    primaryAuraColor: Color,
    secondaryAuraColor: Color
) {
    drawMascotDynamicAuraLayer(
        cx = cx,
        cy = cy,
        r = r,
        timeSec = timeSec,
        auraStyle = AppThemeController.mascotAura,
        defaultPrimary = primaryAuraColor,
        defaultSecondary = secondaryAuraColor
    )
}

/**
 * Shared Complex Accessories & Activity Poses Renderer for Mascot:
 * Renders 3D Cinema Glasses + Popcorn Bucket (Ikut Nonton), Open Cyber-Book (Membaca),
 * Hexagonal Aegis Shield (Pegang Perisai), Musical Groove Notes (Menari),
 * Sleeping Zzz Dream Bubbles (Tidur), Acrobatic Spin Trails, plus Royal Crown,
 * Wizard Hat, Cyber Headphones, and Cosmic Halo.
 */
internal fun DrawScope.drawMascotComplexAccessoriesAndPoses(
    cx: Float,
    cy: Float,
    r: Float,
    timeSec: Float,
    leftEyeX: Float,
    rightEyeX: Float,
    eyeY: Float,
    leftHandCenter: Offset,
    rightHandCenter: Offset,
    activityPose: MascotActivityPose,
    accessory: MascotAccessory
) {
    // 1. ACCESSORIES ON HEAD / EYES
    val show3DGlasses = accessory == MascotAccessory.GLASSES_3D || activityPose == MascotActivityPose.WATCHING_MEDIA
    if (show3DGlasses) {
        val frameW = r * 1.44f
        val frameH = r * 0.44f
        val frameX = cx - frameW * 0.5f
        val frameY = eyeY - frameH * 0.52f

        // White 3D Cinema Glasses Frame
        drawRoundRect(
            color = Color.White,
            topLeft = Offset(frameX, frameY),
            size = Size(frameW, frameH),
            cornerRadius = CornerRadius(r * 0.10f, r * 0.10f)
        )
        drawRoundRect(
            color = Color(0xFF0F172A),
            topLeft = Offset(frameX, frameY),
            size = Size(frameW, frameH),
            cornerRadius = CornerRadius(r * 0.10f, r * 0.10f),
            style = Stroke(width = (r * 0.045f).coerceAtLeast(2f))
        )
        // Left Red Anaglyph Lens
        drawRoundRect(
            color = Color(0xFFEF4444).copy(alpha = 0.76f),
            topLeft = Offset(frameX + r * 0.08f, frameY + r * 0.06f),
            size = Size(frameW * 0.41f, frameH - r * 0.12f),
            cornerRadius = CornerRadius(r * 0.06f, r * 0.06f)
        )
        // Right Cyan Anaglyph Lens
        drawRoundRect(
            color = Color(0xFF06B6D4).copy(alpha = 0.76f),
            topLeft = Offset(cx + r * 0.05f, frameY + r * 0.06f),
            size = Size(frameW * 0.41f, frameH - r * 0.12f),
            cornerRadius = CornerRadius(r * 0.06f, r * 0.06f)
        )
    }

    if (accessory == MascotAccessory.CYBER_HEADPHONES || activityPose == MascotActivityPose.DANCING) {
        // Headband Arc over Folder Top
        drawArc(
            color = Color(0xFF1E293B),
            startAngle = 180f,
            sweepAngle = 180f,
            useCenter = false,
            topLeft = Offset(cx - r * 1.04f, cy - r * 1.08f),
            size = Size(r * 2.08f, r * 1.15f),
            style = Stroke(width = (r * 0.11f).coerceAtLeast(3.5f), cap = StrokeCap.Round)
        )
        // Left & Right Glowing Earcups
        val cupColor = DuoFeatherGreen
        for (side in listOf(-1f, 1f)) {
            val cupX = cx + side * (r * 1.02f) - r * 0.12f
            val cupY = cy - r * 0.36f
            drawRoundRect(
                color = Color(0xFF0F172A),
                topLeft = Offset(cupX, cupY),
                size = Size(r * 0.24f, r * 0.48f),
                cornerRadius = CornerRadius(r * 0.10f, r * 0.10f)
            )
            drawRoundRect(
                color = cupColor,
                topLeft = Offset(cupX + r * 0.04f, cupY + r * 0.08f),
                size = Size(r * 0.16f, r * 0.32f),
                cornerRadius = CornerRadius(r * 0.08f, r * 0.08f)
            )
        }
    }

    if (accessory == MascotAccessory.ROYAL_CROWN) {
        val crownW = r * 0.96f
        val crownH = r * 0.48f
        val crownX = cx - crownW * 0.5f
        val crownY = cy - r * 1.24f
        val crownPath = Path().apply {
            moveTo(crownX, crownY + crownH)
            lineTo(crownX + crownW * 0.08f, crownY + crownH * 0.15f)
            lineTo(crownX + crownW * 0.30f, crownY + crownH * 0.55f)
            lineTo(cx, crownY)
            lineTo(crownX + crownW * 0.70f, crownY + crownH * 0.55f)
            lineTo(crownX + crownW * 0.92f, crownY + crownH * 0.15f)
            lineTo(crownX + crownW, crownY + crownH)
            close()
        }
        drawPath(
            path = crownPath,
            brush = Brush.verticalGradient(listOf(Color(0xFFFDE047), Color(0xFFF59E0B), Color(0xFFB45309)))
        )
        drawPath(
            path = crownPath,
            color = Color(0xFF78350F),
            style = Stroke(width = (r * 0.04f).coerceAtLeast(1.8f))
        )
        // Crown Gems
        drawCircle(color = Color(0xFFEF4444), radius = r * 0.07f, center = Offset(cx, crownY + crownH * 0.65f))
        drawCircle(color = Color(0xFF0EA5E9), radius = r * 0.055f, center = Offset(crownX + crownW * 0.25f, crownY + crownH * 0.72f))
        drawCircle(color = Color(0xFF10B981), radius = r * 0.055f, center = Offset(crownX + crownW * 0.75f, crownY + crownH * 0.72f))
    }

    if (accessory == MascotAccessory.WIZARD_HAT) {
        val hatBrimW = r * 1.36f
        val hatTopY = cy - r * 1.48f
        val hatBaseY = cy - r * 0.82f
        val hatPath = Path().apply {
            moveTo(cx - hatBrimW * 0.36f, hatBaseY)
            quadraticTo(cx - r * 0.12f, (hatBaseY + hatTopY) * 0.5f, cx + r * 0.22f, hatTopY)
            quadraticTo(cx + r * 0.26f, (hatBaseY + hatTopY) * 0.55f, cx + hatBrimW * 0.36f, hatBaseY)
            close()
        }
        drawPath(
            path = hatPath,
            brush = Brush.verticalGradient(listOf(Color(0xFF6366F1), Color(0xFF4338CA), Color(0xFF312E81)))
        )
        drawOval(
            color = Color(0xFF312E81),
            topLeft = Offset(cx - hatBrimW * 0.5f, hatBaseY - r * 0.09f),
            size = Size(hatBrimW, r * 0.20f)
        )
        drawCircle(color = DuoBeeYellow, radius = r * 0.08f, center = Offset(cx + r * 0.22f, hatTopY))
    }

    if (accessory == MascotAccessory.COSMIC_HALO) {
        val haloY = cy - r * 1.18f + sin(timeSec * 3.2f) * (r * 0.05f)
        drawOval(
            brush = Brush.horizontalGradient(listOf(DuoBeeYellow, DuoFeatherGreen, DuoMacawBlue, DuoBeeYellow)),
            topLeft = Offset(cx - r * 0.68f, haloY),
            size = Size(r * 1.36f, r * 0.24f),
            style = Stroke(width = (r * 0.075f).coerceAtLeast(3f))
        )
    }

    // Render all 20+ Extended Head/Face/Floating Accessories
    drawMascotExtendedAccessories(
        cx = cx,
        cy = cy,
        r = r,
        timeSec = timeSec,
        leftEyeX = leftEyeX,
        rightEyeX = rightEyeX,
        eyeY = eyeY,
        accessory = accessory
    )

    // 2. ACTIVITY POSE PROPS & PARTICLES
    when (activityPose) {
        MascotActivityPose.WATCHING_MEDIA -> {
            // Striped Red & White Cinema Popcorn Bucket held by Mascot
            val bucketW = r * 0.46f
            val bucketH = r * 0.48f
            val bx = leftHandCenter.x - bucketW * 0.35f
            val by = leftHandCenter.y - bucketH * 0.45f
            // Fluffy Golden Popcorn Kernels on top
            for (k in 0..3) {
                drawCircle(
                    color = Color(0xFFFDE047),
                    radius = r * 0.11f,
                    center = Offset(bx + bucketW * (0.2f + k * 0.2f), by)
                )
            }
            // Bucket Body
            drawRoundRect(
                color = Color.White,
                topLeft = Offset(bx, by),
                size = Size(bucketW, bucketH),
                cornerRadius = CornerRadius(r * 0.06f, r * 0.06f)
            )
            drawRoundRect(
                color = Color(0xFFEF4444),
                topLeft = Offset(bx + bucketW * 0.22f, by),
                size = Size(bucketW * 0.22f, bucketH)
            )
            drawRoundRect(
                color = Color(0xFFEF4444),
                topLeft = Offset(bx + bucketW * 0.62f, by),
                size = Size(bucketW * 0.22f, bucketH)
            )
        }

        MascotActivityPose.READING -> {
            // Open 3D Cyber-Book held in front of chest
            val bookW = r * 1.16f
            val bookH = r * 0.56f
            val bookX = cx - bookW * 0.5f
            val bookY = cy + r * 0.18f + sin(timeSec * 3.0f) * (r * 0.03f)

            // Outer Book Cover
            drawRoundRect(
                color = DuoMacawBlueLip,
                topLeft = Offset(bookX - r * 0.04f, bookY + r * 0.04f),
                size = Size(bookW + r * 0.08f, bookH),
                cornerRadius = CornerRadius(r * 0.08f, r * 0.08f)
            )
            // Left & Right White Pages
            drawRoundRect(
                color = Color.White,
                topLeft = Offset(bookX, bookY),
                size = Size(bookW * 0.49f, bookH * 0.92f),
                cornerRadius = CornerRadius(r * 0.06f, r * 0.06f)
            )
            drawRoundRect(
                color = Color(0xFFF8FAFC),
                topLeft = Offset(cx + bookW * 0.01f, bookY),
                size = Size(bookW * 0.49f, bookH * 0.92f),
                cornerRadius = CornerRadius(r * 0.06f, r * 0.06f)
            )
            // Text lines on pages
            for (line in 1..3) {
                val ly = bookY + (bookH * 0.22f * line)
                drawLine(
                    color = DuoMacawBlue.copy(alpha = 0.65f),
                    start = Offset(bookX + r * 0.08f, ly),
                    end = Offset(cx - r * 0.08f, ly),
                    strokeWidth = (r * 0.035f).coerceAtLeast(1.5f),
                    cap = StrokeCap.Round
                )
                drawLine(
                    color = DuoFeatherGreen.copy(alpha = 0.65f),
                    start = Offset(cx + r * 0.08f, ly),
                    end = Offset(bookX + bookW - r * 0.08f, ly),
                    strokeWidth = (r * 0.035f).coerceAtLeast(1.5f),
                    cap = StrokeCap.Round
                )
            }
        }

        MascotActivityPose.SHIELD_GUARD -> {
            // 3D Glowing Hexagonal Aegis Shield held in Right Hand
            val sx = rightHandCenter.x
            val sy = rightHandCenter.y + r * 0.06f
            val shieldR = r * 0.56f
            val shieldPath = Path().apply {
                for (i in 0..5) {
                    val a = (i * 60f - 30f) * (PI.toFloat() / 180f)
                    val px = sx + cos(a) * shieldR
                    val py = sy + sin(a) * shieldR
                    if (i == 0) moveTo(px, py) else lineTo(px, py)
                }
                close()
            }
            drawPath(
                path = shieldPath,
                brush = Brush.verticalGradient(
                    colors = listOf(DuoFeatherGreen, DuoMacawBlue, Color(0xFF1E3A8A)),
                    startY = sy - shieldR,
                    endY = sy + shieldR
                )
            )
            drawPath(
                path = shieldPath,
                color = DuoBeeYellow,
                style = Stroke(width = (r * 0.065f).coerceAtLeast(2.5f))
            )
            // Shield Cross Emblem
            drawLine(
                color = Color.White,
                start = Offset(sx, sy - shieldR * 0.5f),
                end = Offset(sx, sy + shieldR * 0.5f),
                strokeWidth = (r * 0.06f).coerceAtLeast(2f),
                cap = StrokeCap.Round
            )
            drawLine(
                color = Color.White,
                start = Offset(sx - shieldR * 0.45f, sy),
                end = Offset(sx + shieldR * 0.45f, sy),
                strokeWidth = (r * 0.06f).coerceAtLeast(2f),
                cap = StrokeCap.Round
            )
        }

        MascotActivityPose.SLEEPING -> {
            // Floating Sleepy Dream Bubbles & Zzz Shapes Rising Above Mascot
            for (zIdx in 0..2) {
                val zPhase = (timeSec * 0.45f + zIdx * 0.33f) % 1f
                val zx = cx + r * (0.45f + zPhase * 0.55f)
                val zy = cy - r * (0.65f + zPhase * 0.75f)
                val zs = r * (0.14f + zPhase * 0.12f)
                val alpha = (1f - zPhase * 0.65f).coerceIn(0.2f, 1f)
                val zPath = Path().apply {
                    moveTo(zx - zs * 0.5f, zy - zs * 0.5f)
                    lineTo(zx + zs * 0.5f, zy - zs * 0.5f)
                    lineTo(zx - zs * 0.5f, zy + zs * 0.5f)
                    lineTo(zx + zs * 0.5f, zy + zs * 0.5f)
                }
                drawPath(
                    path = zPath,
                    color = Color(0xFF8B5CF6).copy(alpha = alpha),
                    style = Stroke(width = (r * 0.05f).coerceAtLeast(2f), cap = StrokeCap.Round)
                )
            }
        }

        MascotActivityPose.DANCING -> {
            // Floating Musical Groove Notes Around Mascot
            val noteOffsets = listOf(-1f to 0f, 1f to 0.33f, -0.85f to 0.66f)
            noteOffsets.forEachIndexed { idx, (sideX, phaseShift) ->
                val nPhase = (timeSec * 0.75f + phaseShift) % 1f
                val nx = cx + sideX * r * (1.05f + sin(nPhase * PI * 2).toFloat() * 0.14f)
                val ny = cy - r * (0.20f + nPhase * 0.85f)
                val noteColor = when (idx % 3) {
                    0 -> Color(0xFFEC4899)
                    1 -> DuoFeatherGreen
                    else -> DuoMacawBlue
                }
                drawCircle(
                    color = noteColor,
                    radius = r * 0.09f,
                    center = Offset(nx, ny)
                )
                drawLine(
                    color = noteColor,
                    start = Offset(nx + r * 0.07f, ny),
                    end = Offset(nx + r * 0.07f, ny - r * 0.26f),
                    strokeWidth = (r * 0.045f).coerceAtLeast(2f),
                    cap = StrokeCap.Round
                )
                drawLine(
                    color = noteColor,
                    start = Offset(nx + r * 0.07f, ny - r * 0.26f),
                    end = Offset(nx + r * 0.20f, ny - r * 0.19f),
                    strokeWidth = (r * 0.045f).coerceAtLeast(2f),
                    cap = StrokeCap.Round
                )
            }
        }

        MascotActivityPose.ACROBATIC_SPIN -> {
            // Kinetic Spin Speed Arcs Around Mascot
            drawArc(
                color = DuoBeeYellow.copy(alpha = 0.75f),
                startAngle = (timeSec * 360f) % 360f,
                sweepAngle = 110f,
                useCenter = false,
                topLeft = Offset(cx - r * 1.26f, cy - r * 1.26f),
                size = Size(r * 2.52f, r * 2.52f),
                style = Stroke(width = (r * 0.06f).coerceAtLeast(2.2f), cap = StrokeCap.Round)
            )
            drawArc(
                color = DuoCardinalRed.copy(alpha = 0.70f),
                startAngle = ((timeSec * 360f) + 180f) % 360f,
                sweepAngle = 110f,
                useCenter = false,
                topLeft = Offset(cx - r * 1.26f, cy - r * 1.26f),
                size = Size(r * 2.52f, r * 2.52f),
                style = Stroke(width = (r * 0.06f).coerceAtLeast(2.2f), cap = StrokeCap.Round)
            )
        }

        else -> {
            drawMascotExtraInteractivePoses(
                cx = cx,
                cy = cy,
                r = r,
                timeSec = timeSec,
                leftHandCenter = leftHandCenter,
                rightHandCenter = rightHandCenter,
                activityPose = activityPose
            )
        }
    }
}

// ============================================================================
// 2. BUSY 3D DUOLINGO TELEMETRY TICKER, BIT-MATRIX GAUGE & 3D CHIPS
// ============================================================================

data class TelemetryMetricPill(
    val label: String,
    val value: String,
    val accentColor: Color,
    val lipColor: Color,
    val softColor: Color
)

/**
 * High-density 3D Duolingo Live Telemetry Ticker & Status Ribbon.
 * Makes Tools screens look lively, busy, and packed with tactile security readouts.
 */
@Composable
fun DuolingoLiveTelemetryTicker(
    screenTitle: String,
    modeBadge: String,
    metrics: List<TelemetryMetricPill>,
    accentColor: Color = DuoFeatherGreen,
    lipColor: Color = DuoFeatherGreenLip,
    softBgColor: Color = DuoFeatherGreenSoft,
    modifier: Modifier = Modifier
) {
    var tickCounter by remember { mutableIntStateOf(0) }
    var turboMode by remember { mutableStateOf(false) }

    LaunchedEffect(turboMode) {
        while (true) {
            delay(if (turboMode) 450L else 1200L)
            tickCounter = (tickCounter + 1) % 9999
        }
    }

    val liveThroughput = remember(tickCounter, turboMode) {
        val base = if (turboMode) 940 else 420
        val jitter = (tickCounter * 37) % 160
        "${base + jitter} KB/s"
    }

    val liveEntropy = remember(tickCounter) {
        val frac = 91 + (tickCounter % 9)
        "7.$frac b/B"
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .duolingoCard(
                backgroundColor = DuoPureWhite,
                borderColor = accentColor,
                lipColor = lipColor,
                cornerRadius = 20.dp,
                borderWidth = 2.dp,
                lipHeight = 4.dp
            )
            .padding(bottom = 4.dp)
            .padding(horizontal = 12.dp, vertical = 10.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Top Row: Blinking 3D LED + Screen Telemetry Title + Interactive Turbo Pill
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
                // Blinking 3D LED
                Box(
                    modifier = Modifier
                        .size(12.dp)
                        .clip(CircleShape)
                        .background(if (tickCounter % 2 == 0) accentColor else DuoBeeYellow)
                        .border(1.5.dp, lipColor, CircleShape)
                )
                Column {
                    Text(
                        text = screenTitle,
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.ExtraBold,
                        color = CosmicTextPure,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = "TELEMETRI AKTIF #$tickCounter • $liveThroughput • ENTROPI $liveEntropy",
                        style = MaterialTheme.typography.labelSmall,
                        fontFamily = JetBrainsMonoFontFamily,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        color = CosmicTextSubtle
                    )
                }
            }

            // Interactive 3D Turbo / Mode Badge
            Box(
                modifier = Modifier
                    .instantTouchPress { turboMode = !turboMode }
                    .duolingoButton(
                        faceColor = if (turboMode) DuoFoxOrange else accentColor,
                        lipColor = if (turboMode) DuoFoxOrangeLip else lipColor,
                        cornerRadius = 12.dp,
                        lipHeight = 3.dp
                    )
                    .padding(bottom = 3.dp)
                    .padding(horizontal = 10.dp, vertical = 5.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector = if (turboMode) Icons.Default.Bolt else Icons.Default.Speed,
                        contentDescription = null,
                        tint = DuoPureWhite,
                        modifier = Modifier.size(12.dp)
                    )
                    Text(
                        text = if (turboMode) "TURBO ON" else modeBadge,
                        style = MaterialTheme.typography.labelSmall,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = DuoPureWhite
                    )
                }
            }
        }

        // Scrollable Row of Pastel 3D Telemetry Pills
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            metrics.forEach { metric ->
                Box(
                    modifier = Modifier
                        .duolingoCard(
                            backgroundColor = metric.softColor,
                            borderColor = metric.accentColor,
                            lipColor = metric.lipColor,
                            cornerRadius = 12.dp,
                            borderWidth = 1.5.dp,
                            lipHeight = 3.dp
                        )
                        .padding(bottom = 3.dp)
                        .padding(horizontal = 9.dp, vertical = 5.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(5.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(6.dp)
                                .clip(CircleShape)
                                .background(metric.accentColor)
                        )
                        Text(
                            text = "${metric.label}:",
                            style = MaterialTheme.typography.labelSmall,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            color = CosmicTextSubtle
                        )
                        Text(
                            text = metric.value,
                            style = MaterialTheme.typography.labelSmall,
                            fontFamily = JetBrainsMonoFontFamily,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = metric.lipColor
                        )
                    }
                }
            }
        }
    }
}

/**
 * Interactive 3D Bit-Matrix Gauge & Entropy Oscilloscope Card.
 * Renders an animated grid of pastel/neon 3D cells + chunky progress bar + scramble button.
 */
@Composable
fun DuolingoBitMatrixGaugeCard(
    title: String,
    subtitle: String,
    activeBitLoadPercent: Int,
    primaryColor: Color = DuoMacawBlue,
    lipColor: Color = DuoMacawBlueLip,
    softSurfaceColor: Color = DuoMacawBlueSoft,
    badgeLabels: List<String> = listOf("AES-256-GCM", "LSB-MATRIX", "CRC32-OK", "ZERO-TRUST"),
    modifier: Modifier = Modifier
) {
    var matrixSeed by remember { mutableIntStateOf(1) }

    LaunchedEffect(Unit) {
        while (true) {
            delay(950L)
            matrixSeed = (matrixSeed + 1) % 1024
        }
    }

    val safePercent = activeBitLoadPercent.coerceIn(8, 100)

    Column(
        modifier = modifier
            .fillMaxWidth()
            .duolingoCard(
                backgroundColor = softSurfaceColor,
                borderColor = primaryColor,
                lipColor = lipColor,
                cornerRadius = 20.dp,
                borderWidth = 2.dp,
                lipHeight = 4.dp
            )
            .padding(bottom = 4.dp)
            .padding(12.dp),
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
                Box(
                    modifier = Modifier
                        .size(34.dp)
                        .duolingoButton(
                            faceColor = primaryColor,
                            lipColor = lipColor,
                            cornerRadius = 11.dp,
                            lipHeight = 3.dp
                        )
                        .padding(bottom = 3.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Memory,
                        contentDescription = null,
                        tint = DuoPureWhite,
                        modifier = Modifier.size(18.dp)
                    )
                }
                Column {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.ExtraBold,
                        color = DuoEelInk
                    )
                    Text(
                        text = subtitle,
                        style = MaterialTheme.typography.labelSmall,
                        fontSize = 10.sp,
                        color = CosmicTextSubtle
                    )
                }
            }

            // Interactive Scramble / Refresh Bit-Matrix 3D Button
            Box(
                modifier = Modifier
                    .instantTouchPress { matrixSeed += 17 }
                    .duolingoButton(
                        faceColor = DuoPureWhite,
                        lipColor = lipColor,
                        cornerRadius = 12.dp,
                        lipHeight = 3.dp
                    )
                    .padding(bottom = 3.dp)
                    .padding(horizontal = 9.dp, vertical = 5.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = "Acak Bit Matrix",
                        tint = lipColor,
                        modifier = Modifier.size(12.dp)
                    )
                    Text(
                        text = "$safePercent% LOAD",
                        style = MaterialTheme.typography.labelSmall,
                        fontFamily = JetBrainsMonoFontFamily,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = lipColor
                    )
                }
            }
        }

        // Animated 12x3 Bit-Matrix Grid Canvas inside a crisp 3D White Well
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(44.dp)
                .duolingoCard(
                    backgroundColor = DuoPureWhite,
                    borderColor = DuoSwanBorder,
                    lipColor = DuoHareLip,
                    cornerRadius = 14.dp,
                    borderWidth = 1.5.dp,
                    lipHeight = 3.dp
                )
                .padding(bottom = 3.dp)
                .padding(horizontal = 8.dp, vertical = 6.dp)
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val cols = 16
                val rows = 3
                val gap = 4.dp.toPx()
                val cellW = ((size.width - gap * (cols - 1)) / cols).coerceAtLeast(4f)
                val cellH = ((size.height - gap * (rows - 1)) / rows).coerceAtLeast(4f)

                for (r in 0 until rows) {
                    for (c in 0 until cols) {
                        val hash = ((c * 31 + r * 17 + matrixSeed * 13) % 100)
                        val isActive = hash < safePercent
                        val cellColor = if (!isActive) {
                            DuoSwanBorder.copy(alpha = 0.55f)
                        } else {
                            when ((c + r + matrixSeed) % 5) {
                                0 -> DuoFeatherGreen
                                1 -> DuoMacawBlue
                                2 -> DuoBeetlePurple
                                3 -> DuoFoxOrange
                                else -> DuoBeeYellow
                            }
                        }
                        drawRoundRect(
                            color = cellColor,
                            topLeft = Offset(c * (cellW + gap), r * (cellH + gap)),
                            size = Size(cellW, cellH),
                            cornerRadius = CornerRadius(4f, 4f)
                        )
                    }
                }
            }
        }

        // Bottom Row of 3D Tactile Badges
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            badgeLabels.forEachIndexed { idx, badge ->
                val (bg, lip) = when (idx % 4) {
                    0 -> DuoFeatherGreen to DuoFeatherGreenLip
                    1 -> DuoMacawBlue to DuoMacawBlueLip
                    2 -> DuoBeetlePurple to DuoBeetlePurpleLip
                    else -> DuoFoxOrange to DuoFoxOrangeLip
                }
                Box(
                    modifier = Modifier
                        .duolingoButton(
                            faceColor = bg,
                            lipColor = lip,
                            cornerRadius = 10.dp,
                            lipHeight = 3.dp
                        )
                        .padding(bottom = 3.dp)
                        .padding(horizontal = 8.dp, vertical = 3.dp)
                ) {
                    Text(
                        text = badge,
                        style = MaterialTheme.typography.labelSmall,
                        fontFamily = JetBrainsMonoFontFamily,
                        fontSize = 8.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = DuoPureWhite
                    )
                }
            }
        }
    }
}

/**
 * 3D Tactile Duolingo Navigation / Filter Chip with Pastel Unselected Face & Vibrant Selected Face.
 */
@Composable
fun Duolingo3DNavChip(
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
    accentColor: Color = DuoFeatherGreen,
    lipColor: Color = DuoFeatherGreenLip,
    softPastelColor: Color = DuoFeatherGreenSoft,
    icon: ImageVector? = null,
    badgeText: String? = null,
    testTag: String? = null,
    modifier: Modifier = Modifier
) {
    val baseModifier = if (testTag != null) modifier.testTag(testTag) else modifier

    Box(
        modifier = baseModifier
            .instantTouchPress { onClick() }
            .then(
                if (selected) {
                    Modifier.duolingoButton(
                        faceColor = accentColor,
                        lipColor = lipColor,
                        cornerRadius = 16.dp,
                        lipHeight = 4.dp
                    )
                } else {
                    Modifier.duolingoCard(
                        backgroundColor = softPastelColor,
                        borderColor = accentColor.copy(alpha = 0.65f),
                        lipColor = lipColor.copy(alpha = 0.75f),
                        cornerRadius = 16.dp,
                        borderWidth = 2.dp,
                        lipHeight = 4.dp
                    )
                }
            )
            .padding(bottom = 4.dp)
            .padding(horizontal = 13.dp, vertical = 8.dp),
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
                    tint = if (selected) DuoPureWhite else lipColor,
                    modifier = Modifier.size(15.dp)
                )
            }
            Text(
                text = label,
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.ExtraBold,
                color = if (selected) DuoPureWhite else DuoEelInk
            )
            if (!badgeText.isNullOrBlank()) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(if (selected) DuoPureWhite.copy(alpha = 0.25f) else DuoPureWhite)
                        .padding(horizontal = 5.dp, vertical = 1.dp)
                ) {
                    Text(
                        text = badgeText,
                        style = MaterialTheme.typography.labelSmall,
                        fontFamily = JetBrainsMonoFontFamily,
                        fontSize = 8.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = if (selected) DuoPureWhite else lipColor
                    )
                }
            }
        }
    }
}
