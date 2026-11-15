package com.example.ui

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.filled.GpsFixed
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.theme.AppThemeController
import com.example.ui.theme.CosmicObsidian
import com.example.ui.theme.CosmicTextPure
import com.example.ui.theme.CosmicTextSubtle
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
import com.example.ui.theme.DuoPureWhite
import com.example.ui.theme.DuoSwanBorder
import com.example.ui.theme.JetBrainsMonoFontFamily
import com.example.ui.theme.MascotAccessory
import com.example.ui.theme.MascotActivityPose
import com.example.ui.theme.duolingoButton
import com.example.ui.theme.duolingoCard
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.roundToInt
import kotlin.math.sin

// ============================================================================
// 1. FORM VERIFICATION & CURSOR-TRACKING STATE MODEL
// ============================================================================

enum class MascotFormVerificationStatus(
    val id: String,
    val title: String,
    val subtitle: String,
    val badgeColor: Color,
    val auraLayers: Int
) {
    IDLE_CURSOR_TRACK(
        id = "IDLE_CURSOR_TRACK",
        title = "Lacak Kursor 360°",
        subtitle = "Mata, alis, kepala & tangan maskot mengikuti arah kursor/sentuhan",
        badgeColor = Color(0xFF0EA5E9),
        auraLayers = 2
    ),
    TYPING_INSPECT(
        id = "TYPING_INSPECT",
        title = "Baca Ketikan Formulir",
        subtitle = "Mata maskot membaca setiap karakter yang diketik pada kolom formulir",
        badgeColor = Color(0xFFF59E0B),
        auraLayers = 2
    ),
    ALERT_SHIELD_VERIFYING(
        id = "ALERT_SHIELD_VERIFYING",
        title = "Siaga Perisai Kosmik",
        subtitle = "Pose siaga mengangkat Perisai Heksagonal & 4 lapis aura kosmik berputar",
        badgeColor = Color(0xFF8B5CF6),
        auraLayers = 4
    ),
    VERIFIED_SUCCESS(
        id = "VERIFIED_SUCCESS",
        title = "Verifikasi Berhasil",
        subtitle = "Perisai zamrud kosmik mekar dengan bintang selebrasi & konfeti",
        badgeColor = Color(0xFF10B981),
        auraLayers = 4
    ),
    VERIFICATION_FAILED(
        id = "VERIFICATION_FAILED",
        title = "Verifikasi Ditolak!",
        subtitle = "Perisai merah menyala menahan akses dengan kilatan petir pertahanan",
        badgeColor = Color(0xFFEF4444),
        auraLayers = 4
    )
}

data class MascotPoseAssetCardItem(
    val id: String,
    val title: String,
    val subtitle: String,
    val pose: MascotActivityPose,
    val accessory: MascotAccessory,
    val illustrationResId: Int,
    val vectorBadgeResId: Int,
    val accentColor: Color
)

val abundantMascotPoseAssets: List<MascotPoseAssetCardItem> = listOf(
    MascotPoseAssetCardItem(
        id = "asset_watching",
        title = "Ikut Nonton 3D",
        subtitle = "Kacamata sinema merah-sian & ember popcorn mengikuti layar",
        pose = MascotActivityPose.WATCHING_MEDIA,
        accessory = MascotAccessory.GLASSES_3D,
        illustrationResId = R.drawable.img_mascot_pose_watching,
        vectorBadgeResId = R.drawable.ic_mascot_badge_cinema_3d,
        accentColor = Color(0xFF0EA5E9)
    ),
    MascotPoseAssetCardItem(
        id = "asset_sleeping",
        title = "Tidur Kosmik Zzz",
        subtitle = "Tertidur pulas di atas awan nebula dengan balon mimpi Zzz",
        pose = MascotActivityPose.SLEEPING,
        accessory = MascotAccessory.WIZARD_HAT,
        illustrationResId = R.drawable.img_mascot_pose_sleeping,
        vectorBadgeResId = R.drawable.ic_mascot_badge_sleep_moon,
        accentColor = Color(0xFF8B5CF6)
    ),
    MascotPoseAssetCardItem(
        id = "asset_dancing",
        title = "Menari Groove Neon",
        subtitle = "Goyang ritmis memakai headphone siber & partikel not musik",
        pose = MascotActivityPose.DANCING,
        accessory = MascotAccessory.CYBER_HEADPHONES,
        illustrationResId = R.drawable.img_mascot_pose_dancing,
        vectorBadgeResId = R.drawable.ic_mascot_badge_groove_music,
        accentColor = Color(0xFFEC4899)
    ),
    MascotPoseAssetCardItem(
        id = "asset_reading",
        title = "Membaca Buku Siber",
        subtitle = "Memegang kitab kode holografik & mata melirik baris demi baris",
        pose = MascotActivityPose.READING,
        accessory = MascotAccessory.COSMIC_HALO,
        illustrationResId = R.drawable.img_mascot_pose_reading,
        vectorBadgeResId = R.drawable.ic_mascot_badge_cyber_book,
        accentColor = Color(0xFFF59E0B)
    ),
    MascotPoseAssetCardItem(
        id = "asset_shield",
        title = "Siaga Perisai Aegis",
        subtitle = "Perisai Heksagonal NYX2 & Aura Kosmik Berlapis penangkal penyusup",
        pose = MascotActivityPose.SHIELD_GUARD,
        accessory = MascotAccessory.ROYAL_CROWN,
        illustrationResId = R.drawable.img_mascot_pose_shield,
        vectorBadgeResId = R.drawable.ic_mascot_badge_aegis_shield,
        accentColor = Color(0xFF10B981)
    )
)

// ============================================================================
// 2. CURSOR-TRACKING & FORM-REACTIVE MULTI-LAYER COSMIC MASCOT STAGE
// ============================================================================

@Composable
fun CursorTrackingAndFormReactiveMascotStage(
    cursorNormX: Float,
    cursorNormY: Float,
    activePose: MascotActivityPose,
    accessory: MascotAccessory,
    formStatus: MascotFormVerificationStatus,
    onCursorMoved: (normX: Float, normY: Float) -> Unit,
    modifier: Modifier = Modifier
) {
    val infinite = rememberInfiniteTransition(label = "cursor_form_mascot_anim")
    val continuousPhase by infinite.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 4000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "continuous_phase"
    )
    val pulsePhase by infinite.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse_phase"
    )

    val smoothGazeX by animateFloatAsState(
        targetValue = cursorNormX.coerceIn(-1f, 1f),
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioLowBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "smooth_gaze_x"
    )
    val smoothGazeY by animateFloatAsState(
        targetValue = cursorNormY.coerceIn(-1f, 1f),
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioLowBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "smooth_gaze_y"
    )

    val timeSec = continuousPhase * 12f

    // Determine effective pose & mood based on form verification status
    val effectivePose = when (formStatus) {
        MascotFormVerificationStatus.ALERT_SHIELD_VERIFYING -> MascotActivityPose.SHIELD_GUARD
        MascotFormVerificationStatus.TYPING_INSPECT ->
            if (activePose == MascotActivityPose.AUTO_SMART) MascotActivityPose.READING else activePose
        MascotFormVerificationStatus.VERIFIED_SUCCESS ->
            if (activePose == MascotActivityPose.AUTO_SMART) MascotActivityPose.DANCING else activePose
        else -> if (activePose == MascotActivityPose.AUTO_SMART) MascotActivityPose.WATCHING_MEDIA else activePose
    }

    val effectiveMood = when (formStatus) {
        MascotFormVerificationStatus.VERIFIED_SUCCESS -> MascotMoodState.CLICK_EXCITED
        MascotFormVerificationStatus.VERIFICATION_FAILED -> MascotMoodState.DELETING_CLEANUP
        MascotFormVerificationStatus.ALERT_SHIELD_VERIFYING -> MascotMoodState.ENCRYPTING_STEGO
        MascotFormVerificationStatus.TYPING_INSPECT -> MascotMoodState.SCANNING_IMPORT
        MascotFormVerificationStatus.IDLE_CURSOR_TRACK -> MascotMoodState.IDLE
    }

    val effectiveElement = when (formStatus) {
        MascotFormVerificationStatus.ALERT_SHIELD_VERIFYING -> MascotElement.SHIELD
        MascotFormVerificationStatus.VERIFIED_SUCCESS -> MascotElement.IDEA_BULB
        MascotFormVerificationStatus.VERIFICATION_FAILED -> MascotElement.FIRE
        MascotFormVerificationStatus.TYPING_INSPECT -> MascotElement.ELECTRIC
        MascotFormVerificationStatus.IDLE_CURSOR_TRACK -> null
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(236.dp)
            .duolingoCard(
                backgroundColor = Color(0xFF0F172A),
                borderColor = formStatus.badgeColor,
                lipColor = AppThemeController.darkenColor(formStatus.badgeColor, 0.68f),
                cornerRadius = 22.dp,
                borderWidth = 2.5.dp,
                lipHeight = 5.dp
            )
            .padding(bottom = 5.dp)
            .clip(RoundedCornerShape(20.dp))
            .pointerInput(Unit) {
                detectDragGestures { change, _ ->
                    change.consume()
                    val nx = ((change.position.x / size.width.coerceAtLeast(1)) * 2f - 1f).coerceIn(-1f, 1f)
                    val ny = ((change.position.y / size.height.coerceAtLeast(1)) * 2f - 1f).coerceIn(-1f, 1f)
                    onCursorMoved(nx, ny)
                }
            }
            .pointerInput(Unit) {
                detectTapGestures { pos ->
                    val nx = ((pos.x / size.width.coerceAtLeast(1)) * 2f - 1f).coerceIn(-1f, 1f)
                    val ny = ((pos.y / size.height.coerceAtLeast(1)) * 2f - 1f).coerceIn(-1f, 1f)
                    onCursorMoved(nx, ny)
                }
            }
            .testTag("mascot_cursor_tracking_stage")
    ) {
        // 1. Deep Multi-Layered Cosmic Shield Aura Canvas (Reacts to Form Verification Status!)
        Canvas(modifier = Modifier.fillMaxSize()) {
            val w = size.width
            val h = size.height
            val cx = w * 0.5f
            val cy = h * 0.54f
            val baseR = (w.coerceAtMost(h) * 0.34f)

            // Ambient Starfield & Grid Lines Following Cursor
            val cursorPxX = ((smoothGazeX + 1f) * 0.5f) * w
            val cursorPxY = ((smoothGazeY + 1f) * 0.5f) * h

            // Laser Gaze Guidance Beam from Mascot to Cursor Target
            drawLine(
                brush = Brush.linearGradient(
                    colors = listOf(
                        formStatus.badgeColor.copy(alpha = 0.45f),
                        DuoBeeYellow.copy(alpha = 0.65f)
                    ),
                    start = Offset(cx, cy),
                    end = Offset(cursorPxX, cursorPxY)
                ),
                start = Offset(cx, cy),
                end = Offset(cursorPxX, cursorPxY),
                strokeWidth = 2.dp.toPx(),
                cap = StrokeCap.Round
            )

            // Layer 1..4: Multi-Layered Sacred Geometry & Shield Barrier Rings
            for (layer in 1..formStatus.auraLayers) {
                val layerRadius = baseR * (0.95f + layer * 0.24f + pulsePhase * 0.05f)
                val speedMultiplier = if (formStatus == MascotFormVerificationStatus.ALERT_SHIELD_VERIFYING) 2.2f else 1f
                val dir = if (layer % 2 == 0) 1f else -1f
                val angleDeg = (continuousPhase * 360f * speedMultiplier * dir + layer * 35f) % 360f

                rotate(degrees = angleDeg, pivot = Offset(cx, cy)) {
                    val sides = when (layer) {
                        1 -> 6 // Hexagonal Aegis Core
                        2 -> 8 // Sacred Octagon
                        3 -> 12 // Dodecagon Star Ring
                        else -> 6 // Outer Fortress Shield Ring
                    }
                    val polyPath = Path().apply {
                        for (i in 0 until sides) {
                            val rad = (i * (360f / sides)) * (PI.toFloat() / 180f)
                            val px = cx + cos(rad) * layerRadius
                            val py = cy + sin(rad) * layerRadius
                            if (i == 0) moveTo(px, py) else lineTo(px, py)
                        }
                        close()
                    }
                    val ringColor = when (layer % 3) {
                        1 -> formStatus.badgeColor
                        2 -> DuoFeatherGreen
                        else -> DuoBeeYellow
                    }
                    drawPath(
                        path = polyPath,
                        color = ringColor.copy(alpha = (0.48f - layer * 0.08f).coerceAtLeast(0.16f)),
                        style = Stroke(width = (3.2f - layer * 0.4f).coerceAtLeast(1.4f).dp.toPx())
                    )
                }
            }

            // Interactive Glowing Cursor Reticle Target
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(
                        DuoBeeYellow.copy(alpha = 0.85f),
                        formStatus.badgeColor.copy(alpha = 0.35f),
                        Color.Transparent
                    ),
                    center = Offset(cursorPxX, cursorPxY),
                    radius = 24.dp.toPx()
                ),
                radius = 24.dp.toPx(),
                center = Offset(cursorPxX, cursorPxY)
            )
            drawCircle(
                color = Color.White,
                radius = 6.dp.toPx(),
                center = Offset(cursorPxX, cursorPxY)
            )
            drawCircle(
                color = formStatus.badgeColor,
                radius = 11.dp.toPx(),
                center = Offset(cursorPxX, cursorPxY),
                style = Stroke(width = 2.dp.toPx())
            )
        }

        // 2. Center 3D Mascot Rendering (Follows smoothGazeX / smoothGazeY & Form Verification State)
        Box(
            modifier = Modifier
                .size(162.dp)
                .align(Alignment.Center)
        ) {
            PhysicsKawaiiFolderMascotCanvas(
                mood = effectiveMood,
                surprisePose = when (formStatus) {
                    MascotFormVerificationStatus.VERIFIED_SUCCESS -> SurpriseIdlePose.HAPPY_BOUNCE
                    MascotFormVerificationStatus.VERIFICATION_FAILED -> SurpriseIdlePose.RAGE_INFERNO
                    else -> null
                },
                timeSec = timeSec,
                bodyTiltDeg = smoothGazeX * 14f,
                paperLagY = smoothGazeY * 6f,
                limbLagX = smoothGazeX * 8f,
                limbLagY = smoothGazeY * 6f,
                eyeGazeX = smoothGazeX * 2.4f,
                eyeGazeY = smoothGazeY * 2.2f,
                isGrabbed = false,
                downloadProgressPercent = if (formStatus == MascotFormVerificationStatus.ALERT_SHIELD_VERIFYING) {
                    ((continuousPhase * 100f).toInt().coerceIn(15, 95))
                } else null,
                clickBurstProgress = if (formStatus == MascotFormVerificationStatus.VERIFIED_SUCCESS) pulsePhase else 0f,
                activeElement = effectiveElement,
                neededElement = null,
                activityPose = effectivePose,
                accessory = accessory,
                cosmicAuraEnabled = true
            )
        }

        // 3. Top Telemetry Overlay Pill (Cursor Coordinates & Verification Shield Status)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopCenter)
                .padding(horizontal = 12.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color.Black.copy(alpha = 0.55f))
                    .border(1.dp, formStatus.badgeColor.copy(alpha = 0.7f), RoundedCornerShape(10.dp))
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.GpsFixed,
                        contentDescription = null,
                        tint = DuoBeeYellow,
                        modifier = Modifier.size(13.dp)
                    )
                    Text(
                        text = "KURSOR X:${String.format("%.2f", smoothGazeX)} Y:${String.format("%.2f", smoothGazeY)}",
                        fontFamily = JetBrainsMonoFontFamily,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White
                    )
                }
            }

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(10.dp))
                    .background(formStatus.badgeColor.copy(alpha = 0.88f))
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Text(
                    text = "${formStatus.title.uppercase()} • ${formStatus.auraLayers} LAPIS AURA",
                    fontFamily = JetBrainsMonoFontFamily,
                    fontSize = 9.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.White
                )
            }
        }

        // 4. Bottom Instruction Hint
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 8.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(Color.Black.copy(alpha = 0.55f))
                .padding(horizontal = 10.dp, vertical = 4.dp)
        ) {
            Text(
                text = "Sentuh/geser kursor kuning di panggung atau ketik di formulir bawah untuk melihat respons maskot",
                fontSize = 9.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White.copy(alpha = 0.9f),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

// ============================================================================
// 3. COMPLETE INTERACTIVE SHOWCASE: CURSOR TRACKING + FORM SHIELD + ABUNDANT ASSETS
// ============================================================================

@Composable
fun AbundantMascotAssetAndFormSandboxCard(
    activePose: MascotActivityPose,
    activeAccessory: MascotAccessory,
    onSelectPoseAndAccessory: (pose: MascotActivityPose, accessory: MascotAccessory) -> Unit,
    modifier: Modifier = Modifier
) {
    val scope = rememberCoroutineScope()
    var cursorNormX by remember { mutableFloatStateOf(0.28f) }
    var cursorNormY by remember { mutableFloatStateOf(-0.18f) }
    var formStatus by remember { mutableStateOf(MascotFormVerificationStatus.IDLE_CURSOR_TRACK) }
    var formInputText by remember { mutableStateOf("") }
    var selectedAssetId by remember { mutableStateOf(abundantMascotPoseAssets.first().id) }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .testTag("abundant_mascot_asset_and_form_sandbox"),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // 1. Interactive Cursor-Tracking & Form-Reactive Multi-Layer Cosmic Stage
        CursorTrackingAndFormReactiveMascotStage(
            cursorNormX = cursorNormX,
            cursorNormY = cursorNormY,
            activePose = activePose,
            accessory = activeAccessory,
            formStatus = formStatus,
            onCursorMoved = { nx, ny ->
                cursorNormX = nx
                cursorNormY = ny
                if (formStatus != MascotFormVerificationStatus.ALERT_SHIELD_VERIFYING) {
                    formStatus = MascotFormVerificationStatus.IDLE_CURSOR_TRACK
                }
            }
        )

        // 2. Form Verification & Shield Response Simulator Card
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .duolingoCard(
                    backgroundColor = CosmicObsidian,
                    borderColor = formStatus.badgeColor,
                    lipColor = AppThemeController.darkenColor(formStatus.badgeColor, 0.74f),
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
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Security,
                            contentDescription = null,
                            tint = formStatus.badgeColor,
                            modifier = Modifier.size(20.dp)
                        )
                        Column {
                            Text(
                                text = "Simulasi Respons Verifikasi Formulir & Perisai Kosmik",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.ExtraBold,
                                color = CosmicTextPure
                            )
                            Text(
                                text = formStatus.subtitle,
                                style = MaterialTheme.typography.bodySmall,
                                color = CosmicTextSubtle
                            )
                        }
                    }
                }

                // Form Input that drives Mascot Eye Gaze along text length!
                OutlinedTextField(
                    value = formInputText,
                    onValueChange = { newText ->
                        formInputText = newText
                        // Move mascot eye gaze horizontally from left (-0.85) to right (+0.85) as user types!
                        val progress = (newText.length.coerceAtMost(24) / 24f)
                        cursorNormX = (-0.85f + progress * 1.70f).coerceIn(-0.9f, 0.9f)
                        cursorNormY = 0.42f // Look down toward the input field!
                        formStatus = if (newText.isBlank()) {
                            MascotFormVerificationStatus.IDLE_CURSOR_TRACK
                        } else {
                            MascotFormVerificationStatus.TYPING_INSPECT
                        }
                    },
                    label = { Text("Ketik Kode / PIN / Kata Kunci (Mata Maskot Mengikuti Ketikan)") },
                    placeholder = { Text("Contoh: NYX2-AEGIS-777") },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = formStatus.badgeColor,
                        unfocusedBorderColor = DuoSwanBorder
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("mascot_form_verification_input")
                )

                // Quick Verification State Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Button 1: Pose Siaga Perisai Kosmik (Verifying)
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
                                formStatus = MascotFormVerificationStatus.ALERT_SHIELD_VERIFYING
                                onSelectPoseAndAccessory(
                                    MascotActivityPose.SHIELD_GUARD,
                                    MascotAccessory.ROYAL_CROWN
                                )
                                cursorNormX = 0f
                                cursorNormY = 0f
                            }
                            .padding(bottom = 3.dp)
                            .testTag("mascot_form_btn_shield_alert"),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Siaga Perisai",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color.White,
                            maxLines = 1
                        )
                    }

                    // Button 2: Verifikasi Berhasil
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
                                scope.launch {
                                    formStatus = MascotFormVerificationStatus.ALERT_SHIELD_VERIFYING
                                    onSelectPoseAndAccessory(
                                        MascotActivityPose.SHIELD_GUARD,
                                        MascotAccessory.COSMIC_HALO
                                    )
                                    delay(650L)
                                    formStatus = MascotFormVerificationStatus.VERIFIED_SUCCESS
                                }
                            }
                            .padding(bottom = 3.dp)
                            .testTag("mascot_form_btn_verified_ok"),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Verifikasi OK",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color.White,
                            maxLines = 1
                        )
                    }

                    // Button 3: Verifikasi Gagal / Ditolak
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(42.dp)
                            .duolingoButton(
                                faceColor = DuoCardinalRed,
                                lipColor = DuoCardinalRedLip,
                                cornerRadius = 12.dp,
                                lipHeight = 3.dp
                            )
                            .instantTouchPress {
                                formStatus = MascotFormVerificationStatus.VERIFICATION_FAILED
                                onSelectPoseAndAccessory(
                                    MascotActivityPose.SHIELD_GUARD,
                                    MascotAccessory.ROYAL_CROWN
                                )
                            }
                            .padding(bottom = 3.dp)
                            .testTag("mascot_form_btn_verified_fail"),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Tolak / Alert",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color.White,
                            maxLines = 1
                        )
                    }
                }
            }
        }

        // 3. Abundant 3D Visual Assets & Pose Gallery Cards (5 Generated 3D Illustrations + 5 Vector Badges + Classic Mascot Assets)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .duolingoCard(
                    backgroundColor = CosmicObsidian,
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
                            text = "Koleksi Asset Visual 3D & Pose Maskot Lengkap",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.ExtraBold,
                            color = CosmicTextPure
                        )
                        Text(
                            text = "Ketuk kartu ilustrasi 3D di bawah untuk menerapkan paket Pose, Aksesoris, & Lencana Vektor.",
                            style = MaterialTheme.typography.bodySmall,
                            color = CosmicTextSubtle
                        )
                    }
                }

                // Horizontal Gallery of 3D Illustrated Pose Cards
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    abundantMascotPoseAssets.forEach { item ->
                        val isSelected = selectedAssetId == item.id && activePose == item.pose
                        Box(
                            modifier = Modifier
                                .width(172.dp)
                                .duolingoCard(
                                    backgroundColor = if (isSelected) item.accentColor.copy(alpha = 0.14f) else DuoPureWhite,
                                    borderColor = if (isSelected) item.accentColor else DuoSwanBorder,
                                    lipColor = if (isSelected) AppThemeController.darkenColor(item.accentColor, 0.72f) else DuoHareLip,
                                    cornerRadius = 18.dp,
                                    borderWidth = if (isSelected) 2.5.dp else 1.8.dp,
                                    lipHeight = 4.dp
                                )
                                .instantTouchPress {
                                    selectedAssetId = item.id
                                    onSelectPoseAndAccessory(item.pose, item.accessory)
                                    if (item.pose == MascotActivityPose.SHIELD_GUARD) {
                                        formStatus = MascotFormVerificationStatus.ALERT_SHIELD_VERIFYING
                                    }
                                }
                                .padding(bottom = 4.dp)
                                .padding(10.dp)
                                .testTag("mascot_asset_card_${item.id}")
                        ) {
                            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(116.dp)
                                        .clip(RoundedCornerShape(14.dp))
                                        .border(1.5.dp, item.accentColor.copy(alpha = 0.55f), RoundedCornerShape(14.dp))
                                ) {
                                    Image(
                                        painter = painterResource(id = item.illustrationResId),
                                        contentDescription = item.title,
                                        contentScale = ContentScale.Crop,
                                        modifier = Modifier.fillMaxSize()
                                    )
                                    // Vector Emblem Badge in Top-Right Corner
                                    Box(
                                        modifier = Modifier
                                            .align(Alignment.TopEnd)
                                            .padding(6.dp)
                                            .size(30.dp)
                                            .clip(CircleShape)
                                            .background(Color(0xD90F172A))
                                            .border(1.5.dp, item.accentColor, CircleShape)
                                            .padding(4.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Image(
                                            painter = painterResource(id = item.vectorBadgeResId),
                                            contentDescription = null,
                                            modifier = Modifier.fillMaxSize()
                                        )
                                    }
                                }

                                Text(
                                    text = item.title,
                                    style = MaterialTheme.typography.labelLarge,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = DuoEelInk,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Text(
                                    text = item.subtitle,
                                    style = MaterialTheme.typography.labelSmall,
                                    fontSize = 10.sp,
                                    color = CosmicTextSubtle,
                                    maxLines = 2,
                                    overflow = TextOverflow.Ellipsis
                                )

                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(30.dp)
                                        .duolingoButton(
                                            faceColor = item.accentColor,
                                            lipColor = AppThemeController.darkenColor(item.accentColor, 0.72f),
                                            cornerRadius = 10.dp,
                                            lipHeight = 3.dp
                                        )
                                        .padding(bottom = 3.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = if (isSelected) "AKTIF DI MASKOT" else "PAKAI POSE & ASSET",
                                        fontFamily = JetBrainsMonoFontFamily,
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.ExtraBold,
                                        color = Color.White
                                    )
                                }
                            }
                        }
                    }
                }

                // Row of Classic & Companion Sticker Assets
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val classicDrawables = listOf(
                        Triple(R.drawable.mascot_kawaii_folder, "Folder 3D", MascotActivityPose.AUTO_SMART),
                        Triple(R.drawable.ic_mascot_badge_aegis_shield, "Aegis Shield", MascotActivityPose.SHIELD_GUARD),
                        Triple(R.drawable.ic_mascot_badge_cyber_book, "Kitab Siber", MascotActivityPose.READING),
                        Triple(R.drawable.ic_mascot_badge_cinema_3d, "Sinema 3D", MascotActivityPose.WATCHING_MEDIA),
                        Triple(R.drawable.ic_mascot_badge_groove_music, "Groove Musik", MascotActivityPose.DANCING),
                        Triple(R.drawable.ic_mascot_badge_sleep_moon, "Mimpi Bulan", MascotActivityPose.SLEEPING),
                        Triple(R.drawable.astrodracon_orbit_duo, "Orbit Kosmik", MascotActivityPose.ACROBATIC_SPIN),
                        Triple(R.drawable.astrodracon_sparkle, "Bintang Nebula", MascotActivityPose.DANCING)
                    )
                    classicDrawables.forEach { (resId, label, poseTarget) ->
                        Box(
                            modifier = Modifier
                                .duolingoCard(
                                    backgroundColor = DuoMintSurface,
                                    borderColor = DuoSwanBorder,
                                    lipColor = DuoHareLip,
                                    cornerRadius = 14.dp,
                                    borderWidth = 1.5.dp,
                                    lipHeight = 3.dp
                                )
                                .instantTouchPress {
                                    onSelectPoseAndAccessory(poseTarget, activeAccessory)
                                }
                                .padding(bottom = 3.dp)
                                .padding(horizontal = 10.dp, vertical = 6.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Image(
                                    painter = painterResource(id = resId),
                                    contentDescription = label,
                                    modifier = Modifier.size(22.dp)
                                )
                                Text(
                                    text = label,
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = DuoEelInk
                                )
                            }
                        }
                    }
                }
            }
        }

        // 4. Built-in Wardrobe Studio (25+ Accessories, 10 Skins, 10 Upper Outfits, 10 Hand Gloves)
        MascotWardrobeStudioCard(
            activeAccessory = AppThemeController.mascotAccessory,
            activeSkin = AppThemeController.mascotSkin,
            activeOutfit = AppThemeController.mascotOutfit,
            activeGloves = AppThemeController.mascotGloves,
            onSelectAccessory = { acc ->
                AppThemeController.mascotAccessory = acc
                AppThemeController.poseTriggerNonce++
                onSelectPoseAndAccessory(activePose, acc)
            },
            onSelectSkin = { skin ->
                AppThemeController.mascotSkin = skin
                AppThemeController.poseTriggerNonce++
            },
            onSelectOutfit = { outfit ->
                AppThemeController.mascotOutfit = outfit
                AppThemeController.poseTriggerNonce++
            },
            onSelectGloves = { gloves ->
                AppThemeController.mascotGloves = gloves
                AppThemeController.poseTriggerNonce++
            },
            onApplyPreset = { preset ->
                AppThemeController.applyCostumePreset(preset)
                onSelectPoseAndAccessory(activePose, preset.accessory)
            },
            onRandomizeAll = {
                AppThemeController.randomizeCostume()
                onSelectPoseAndAccessory(activePose, AppThemeController.mascotAccessory)
            },
            onResetDefault = {
                AppThemeController.resetCostumeToDefault()
                onSelectPoseAndAccessory(activePose, MascotAccessory.NONE)
            }
        )
    }
}

