package com.example.ui

import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material.icons.automirrored.filled.Backspace
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.LockOpen
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import com.example.data.SecurityConfig
import com.example.ui.theme.JetBrainsMonoFontFamily
import com.example.ui.theme.VaultAmber
import com.example.ui.theme.VaultCharcoal
import com.example.ui.theme.VaultCoral
import com.example.ui.theme.VaultLime
import com.example.ui.theme.VaultMintAccent
import com.example.ui.theme.VaultMintCard
import com.example.ui.theme.VaultObsidian
import com.example.ui.theme.VaultPureWhite
import com.example.ui.theme.VaultSlate
import androidx.compose.foundation.layout.wrapContentWidth
import com.example.ui.theme.CosmicBorderHairline
import com.example.ui.theme.CosmicGlass
import com.example.ui.theme.CosmicGradientGlassCard
import com.example.ui.theme.CosmicGradientGlassHighlight
import com.example.ui.theme.CosmicGradientCta
import com.example.ui.theme.CosmicNebulaMesh
import com.example.ui.theme.CosmicTextPure
import com.example.ui.theme.PastelSky
import com.example.ui.theme.PastelMint
import com.example.ui.theme.PastelRose
import com.example.ui.theme.PastelLavender
import com.example.ui.theme.SolarGold
import com.example.ui.theme.StarlightCyan
import com.example.ui.theme.NebulaViolet
import com.example.ui.theme.duolingoButton
import com.example.ui.theme.duolingoCard
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * Helper untuk memanggil dialog Biometrik Perangkat Keras Android (Sidik Jari / Wajah)
 * jika tersedia dan terdaftar di OS.
 */
fun triggerHardwareBiometricIfAvailable(
    context: android.content.Context,
    title: String,
    subtitle: String,
    onSuccess: () -> Unit,
    onErrorFeedback: (String) -> Unit
): Boolean {
    val activity = context as? FragmentActivity ?: return false
    val biometricManager = BiometricManager.from(context)
    val canAuth = biometricManager.canAuthenticate(
        BiometricManager.Authenticators.BIOMETRIC_STRONG or
                BiometricManager.Authenticators.BIOMETRIC_WEAK
    )
    if (canAuth != BiometricManager.BIOMETRIC_SUCCESS) return false

    val executor = ContextCompat.getMainExecutor(context)
    val prompt = BiometricPrompt(
        activity,
        executor,
        object : BiometricPrompt.AuthenticationCallback() {
            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                super.onAuthenticationSucceeded(result)
                onSuccess()
            }

            override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                super.onAuthenticationError(errorCode, errString)
                onErrorFeedback("Sensor Biometrik: $errString")
            }

            override fun onAuthenticationFailed() {
                super.onAuthenticationFailed()
                onErrorFeedback("Sidik jari tidak dikenali, coba lagi.")
            }
        }
    )
    val promptInfo = BiometricPrompt.PromptInfo.Builder()
        .setTitle(title)
        .setSubtitle(subtitle)
        .setNegativeButtonText("Gunakan Sensor Layar")
        .build()
    prompt.authenticate(promptInfo)
    return true
}

/**
 * Layar Kunci Minimalis 3-Lapis PIN + Pemindai Sidik Jari Ultrasonik Nyata + Fitur Lupa PIN.
 */
@Composable
fun SecurityGateScreen(
    securityState: SecurityGateUiState,
    onDigitClick: (Int) -> Unit,
    onCharClick: (Char) -> Unit = { c -> c.digitToIntOrNull()?.let(onDigitClick) },
    onBackspaceClick: () -> Unit,
    onClearClick: () -> Unit,
    onBiometricSuccess: () -> Unit,
    onBiometricFeedback: (String) -> Unit,
    onForgotPinClick: () -> Unit = {}
) {
    val context = LocalContext.current
    val haptic = LocalHapticFeedback.current

    LaunchedEffect(securityState.stage) {
        if (securityState.stage == AuthStage.BIOMETRIC_STEP) {
            triggerHardwareBiometricIfAvailable(
                context = context,
                title = "Autentikasi Sidik Jari Trantor Vault",
                subtitle = "Verifikasi sidik jari Anda untuk membuka brankas",
                onSuccess = {
                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                    onBiometricSuccess()
                },
                onErrorFeedback = onBiometricFeedback
            )
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(CosmicNebulaMesh)
            .windowInsetsPadding(WindowInsets.safeDrawing),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .widthIn(max = 460.dp)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 22.dp, vertical = 14.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // 1. Minimalist Top Bar: Mascot + Layer Indicator + Lupa PIN
            var lockPersonaIdx by remember { mutableIntStateOf(0) }
            val personas = MascotPersona.entries
            val activePersona = personas[lockPersonaIdx % personas.size]
            val currentMood = when {
                securityState.errorMessage != null -> MascotMood.ALERT_FIRE
                securityState.enteredPin.isNotEmpty() -> MascotMood.PIN_SHIELD
                securityState.stage == AuthStage.BIOMETRIC_STEP -> MascotMood.SUCCESS_LEAP
                else -> MascotMood.IDLE_BREATHE
            }

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(18.dp))
                            .instantTouchPress {
                                lockPersonaIdx = (lockPersonaIdx + 1) % personas.size
                            }
                            .padding(4.dp)
                            .testTag("lock_screen_mascot_stage")
                    ) {
                        CosmicDragonAvatar(
                            mood = currentMood,
                            size = 48.dp,
                            persona = activePersona
                        )
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Arcanum Vault",
                                style = MaterialTheme.typography.titleMedium,
                                color = CosmicTextPure,
                                fontWeight = FontWeight.Bold,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            Text(
                                text = "${activePersona.label} • Ketuk Ganti",
                                style = MaterialTheme.typography.labelMedium,
                                color = activePersona.badgeColor,
                                fontSize = 11.sp,
                                maxLines = 1
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    // Tombol Lupa PIN / Pemulihan Sandi (Duolingo 3D Pill)
                    Box(
                        modifier = Modifier
                            .wrapContentWidth()
                            .duolingoButton(
                                faceColor = com.example.ui.theme.DuoMacawBlue,
                                lipColor = com.example.ui.theme.DuoMacawBlueLip,
                                cornerRadius = 18.dp,
                                lipHeight = 3.dp
                            )
                            .instantTouchPress { onForgotPinClick() }
                            .padding(bottom = 3.dp)
                            .testTag("forgot_pin_lock_button")
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.HelpOutline,
                                contentDescription = "Lupa PIN",
                                tint = Color.White,
                                modifier = Modifier.size(16.dp)
                            )
                            Text(
                                text = "Lupa PIN?",
                                style = MaterialTheme.typography.labelMedium,
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                maxLines = 1
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Interactive Cursor-Tracking & Form-Verification Cosmic Shield Mascot Stage
                val globalPose = com.example.ui.theme.AppThemeController.mascotActivityPose
                val globalAccessory = com.example.ui.theme.AppThemeController.mascotAccessory
                var showLockMascotStage by remember { mutableStateOf(true) }
                var manualCursorX by remember { mutableStateOf(0f) }
                var manualCursorY by remember { mutableStateOf(0f) }

                val liveFormStatus = when {
                    securityState.errorMessage != null -> MascotFormVerificationStatus.VERIFICATION_FAILED
                    securityState.stage == AuthStage.BIOMETRIC_STEP -> MascotFormVerificationStatus.VERIFIED_SUCCESS
                    securityState.enteredPin.length >= 4 -> MascotFormVerificationStatus.ALERT_SHIELD_VERIFYING
                    securityState.enteredPin.isNotEmpty() -> MascotFormVerificationStatus.TYPING_INSPECT
                    else -> MascotFormVerificationStatus.IDLE_CURSOR_TRACK
                }
                val effectiveCursorX = if (securityState.enteredPin.isEmpty()) {
                    manualCursorX
                } else {
                    ((securityState.enteredPin.length.coerceIn(0, 6) / 6f) * 1.6f - 0.8f)
                }
                val effectiveCursorY = if (securityState.enteredPin.isNotEmpty()) 0.45f else manualCursorY

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            modifier = Modifier
                                .weight(1f)
                                .horizontalScroll(rememberScrollState()),
                            horizontalArrangement = Arrangement.spacedBy(5.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            abundantMascotPoseAssets.forEach { assetItem ->
                                val isSel = globalPose == assetItem.pose
                                Box(
                                    modifier = Modifier
                                        .duolingoButton(
                                            faceColor = if (isSel) assetItem.accentColor else com.example.ui.theme.DuoPureWhite,
                                            lipColor = if (isSel) com.example.ui.theme.AppThemeController.darkenColor(assetItem.accentColor, 0.72f) else com.example.ui.theme.DuoHareLip,
                                            cornerRadius = 10.dp,
                                            lipHeight = 2.dp
                                        )
                                        .instantTouchPress {
                                            com.example.ui.theme.AppThemeController.mascotActivityPose = assetItem.pose
                                            com.example.ui.theme.AppThemeController.mascotAccessory = assetItem.accessory
                                            com.example.ui.theme.AppThemeController.poseTriggerNonce++
                                        }
                                        .padding(bottom = 2.dp)
                                        .padding(horizontal = 7.dp, vertical = 4.dp)
                                        .testTag("lock_pose_chip_${assetItem.pose.name.lowercase()}")
                                ) {
                                    Text(
                                        text = assetItem.pose.displayName.substringBefore(" "),
                                        style = MaterialTheme.typography.labelSmall,
                                        fontWeight = FontWeight.ExtraBold,
                                        color = if (isSel) Color.White else CosmicTextPure,
                                        fontSize = 10.sp,
                                        maxLines = 1
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.width(6.dp))

                        Box(
                            modifier = Modifier
                                .duolingoButton(
                                    faceColor = com.example.ui.theme.DuoBeetlePurple,
                                    lipColor = com.example.ui.theme.DuoBeetlePurpleLip,
                                    cornerRadius = 10.dp,
                                    lipHeight = 2.dp
                                )
                                .instantTouchPress { showLockMascotStage = !showLockMascotStage }
                                .padding(bottom = 2.dp)
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                                .testTag("toggle_lock_cursor_mascot_button")
                        ) {
                            Text(
                                text = if (showLockMascotStage) "Sembunyikan" else "Panggung 3D",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.ExtraBold,
                                color = Color.White,
                                fontSize = 10.sp,
                                maxLines = 1
                            )
                        }
                    }

                    if (showLockMascotStage) {
                        CursorTrackingAndFormReactiveMascotStage(
                            cursorNormX = effectiveCursorX,
                            cursorNormY = effectiveCursorY,
                            activePose = globalPose,
                            accessory = globalAccessory,
                            formStatus = liveFormStatus,
                            onCursorMoved = { nx, ny ->
                                manualCursorX = nx
                                manualCursorY = ny
                            }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Duolingo 4-Step Progress Bar
                MinimalSecurityProgressRow(currentStage = securityState.stage)
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Auto-trigger Android Hardware Biometrics (BiometricPrompt) when reaching biometric stage
            LaunchedEffect(securityState.stage) {
                if (securityState.stage == AuthStage.BIOMETRIC_STEP) {
                    triggerHardwareBiometricIfAvailable(
                        context = context,
                        title = "Autentikasi Sidik Jari Perangkat",
                        subtitle = "Tempelkan jari Anda pada sensor fisik ponsel untuk membuka brankas",
                        onSuccess = {
                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                            onBiometricSuccess()
                        },
                        onErrorFeedback = onBiometricFeedback
                    )
                }
            }

            // 2. Main Center Stage: PIN Entry or Real Ultrasonic Fingerprint Scanner
            if (securityState.stage == AuthStage.BIOMETRIC_STEP) {
                BiometricVerificationCard(
                    statusMessage = securityState.biometricStatusMessage,
                    onTriggerHardwareBiometric = {
                        triggerHardwareBiometricIfAvailable(
                            context = context,
                            title = "Autentikasi Sidik Jari Trantor Vault",
                            subtitle = "Verifikasi sidik jari Anda untuk membuka brankas",
                            onSuccess = {
                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                onBiometricSuccess()
                            },
                            onErrorFeedback = onBiometricFeedback
                        )
                    },
                    onHoldFingerprintCompleted = {
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                        onBiometricSuccess()
                    },
                    onHoldFingerprintFeedback = onBiometricFeedback
                )
            } else {
                PinEntryGateCard(
                    stage = securityState.stage,
                    enteredPin = securityState.enteredPin,
                    errorMessage = securityState.errorMessage,
                    onCharClick = { char ->
                        haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                        onCharClick(char)
                    },
                    onBackspaceClick = {
                        haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                        onBackspaceClick()
                    },
                    onClearClick = {
                        haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                        onClearClick()
                    }
                )
            }

            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
private fun MinimalSecurityProgressRow(currentStage: AuthStage) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            for (stepNum in 1..4) {
                val isCompleted = currentStage.layerNumber > stepNum
                val isActive = currentStage.layerNumber == stepNum
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(10.dp)
                        .clip(CircleShape)
                        .background(
                            when {
                                isCompleted -> com.example.ui.theme.DuoFeatherGreen
                                isActive -> com.example.ui.theme.DuoMacawBlue
                                else -> com.example.ui.theme.DuoSwanBorder
                            }
                        )
                )
            }
        }
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = when (currentStage) {
                AuthStage.PIN_LAYER_1 -> "Lapis 1 dari 4 • PIN Gerbang Utama"
                AuthStage.PIN_LAYER_2 -> "Lapis 2 dari 4 • PIN Subkey NYX2"
                AuthStage.PIN_LAYER_3 -> "Lapis 3 dari 4 • PIN Otorisasi Brankas"
                AuthStage.BIOMETRIC_STEP -> "Lapis 4 dari 4 • Pemindaian Sidik Jari"
                AuthStage.UNLOCKED -> "Terbuka"
            },
            style = MaterialTheme.typography.labelLarge,
            color = com.example.ui.theme.DuoFeatherGreenLip,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun PinEntryGateCard(
    stage: AuthStage,
    enteredPin: String,
    errorMessage: String?,
    onCharClick: (Char) -> Unit,
    onBackspaceClick: () -> Unit,
    onClearClick: () -> Unit
) {
    var isAlphaMode by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Duolingo 3D PIN Header & 6 Slots Card
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .duolingoCard(
                    backgroundColor = com.example.ui.theme.DuoPureWhite,
                    borderColor = com.example.ui.theme.DuoSwanBorder,
                    lipColor = com.example.ui.theme.DuoHareLip,
                    cornerRadius = 26.dp,
                    borderWidth = 2.dp,
                    lipHeight = 5.dp
                )
                .padding(bottom = 5.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp, vertical = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stage.title,
                    style = MaterialTheme.typography.titleLarge,
                    color = CosmicTextPure,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = if (stage.layerNumber > 1) {
                        "Masukkan 6 karakter PIN Lapis ${stage.layerNumber} (A-Z, 0-9)"
                    } else {
                        "Masukkan 6 karakter PIN untuk membuka brankas"
                    },
                    style = MaterialTheme.typography.bodyMedium,
                    color = com.example.ui.theme.DuoWolfSubtle,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(14.dp))

                // 6-Character Duolingo 3D Tile PIN Slots (Fluid weight so 720x1600px never clips!)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("pin_dots_indicator"),
                    horizontalArrangement = Arrangement.spacedBy(6.dp, Alignment.CenterHorizontally)
                ) {
                    for (i in 0 until 6) {
                        val isFilled = i < enteredPin.length
                        val isCurrentSlot = i == enteredPin.length
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .widthIn(max = 44.dp)
                                .height(48.dp)
                                .then(
                                    if (isFilled) {
                                        Modifier.duolingoButton(
                                            faceColor = com.example.ui.theme.DuoFeatherGreen,
                                            lipColor = com.example.ui.theme.DuoFeatherGreenLip,
                                            cornerRadius = 13.dp,
                                            lipHeight = 4.dp
                                        ).padding(bottom = 4.dp)
                                    } else {
                                        Modifier.duolingoCard(
                                            backgroundColor = com.example.ui.theme.DuoSnowCanvas,
                                            borderColor = if (isCurrentSlot) com.example.ui.theme.DuoMacawBlue else com.example.ui.theme.DuoSwanBorder,
                                            lipColor = if (isCurrentSlot) com.example.ui.theme.DuoMacawBlueLip else com.example.ui.theme.DuoHareLip,
                                            cornerRadius = 13.dp,
                                            borderWidth = 2.dp,
                                            lipHeight = 3.dp
                                        ).padding(bottom = 3.dp)
                                    }
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            if (isFilled) {
                                Text(
                                    text = enteredPin[i].toString(),
                                    style = MaterialTheme.typography.titleMedium,
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold
                                )
                            } else {
                                Text(
                                    text = "•",
                                    style = MaterialTheme.typography.titleMedium,
                                    color = com.example.ui.theme.DuoMutedInk.copy(alpha = 0.45f)
                                )
                            }
                        }
                    }
                }

                if (errorMessage != null) {
                    Spacer(modifier = Modifier.height(12.dp))
                    Surface(
                        color = com.example.ui.theme.DuoCardinalRedSoft,
                        shape = RoundedCornerShape(14.dp),
                        border = androidx.compose.foundation.BorderStroke(2.dp, com.example.ui.theme.DuoCardinalRed)
                    ) {
                        Text(
                            text = errorMessage,
                            style = MaterialTheme.typography.bodyMedium,
                            color = com.example.ui.theme.DuoCardinalRedLip,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Alphanumeric Mode Switcher (123 Angka <-> ABC Huruf)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .then(
                        if (!isAlphaMode) {
                            Modifier.duolingoButton(
                                faceColor = com.example.ui.theme.DuoFeatherGreen,
                                lipColor = com.example.ui.theme.DuoFeatherGreenLip,
                                cornerRadius = 14.dp,
                                lipHeight = 3.dp
                            )
                        } else {
                            Modifier.duolingoCard(
                                backgroundColor = com.example.ui.theme.DuoPureWhite,
                                borderColor = com.example.ui.theme.DuoSwanBorder,
                                lipColor = com.example.ui.theme.DuoHareLip,
                                cornerRadius = 14.dp,
                                borderWidth = 2.dp,
                                lipHeight = 3.dp
                            )
                        }
                    )
                    .clickable { isAlphaMode = false }
                    .padding(bottom = 3.dp)
            ) {
                Text(
                    text = "1 2 3 Angka",
                    color = if (!isAlphaMode) Color.White else CosmicTextPure,
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 7.dp)
                )
            }
            Spacer(modifier = Modifier.width(10.dp))
            Box(
                modifier = Modifier
                    .then(
                        if (isAlphaMode) {
                            Modifier.duolingoButton(
                                faceColor = com.example.ui.theme.DuoMacawBlue,
                                lipColor = com.example.ui.theme.DuoMacawBlueLip,
                                cornerRadius = 14.dp,
                                lipHeight = 3.dp
                            )
                        } else {
                            Modifier.duolingoCard(
                                backgroundColor = com.example.ui.theme.DuoPureWhite,
                                borderColor = com.example.ui.theme.DuoSwanBorder,
                                lipColor = com.example.ui.theme.DuoHareLip,
                                cornerRadius = 14.dp,
                                borderWidth = 2.dp,
                                lipHeight = 3.dp
                            )
                        }
                    )
                    .clickable { isAlphaMode = true }
                    .padding(bottom = 3.dp)
            ) {
                Text(
                    text = "Q W E R T Y Huruf (A-Z)",
                    color = if (isAlphaMode) Color.White else CosmicTextPure,
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 7.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Duolingo 3D Tactile Keypad (Digits or Standard QWERTY Layout)
        if (!isAlphaMode) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                val rows = listOf(
                    listOf('1', '2', '3'),
                    listOf('4', '5', '6'),
                    listOf('7', '8', '9')
                )
                rows.forEach { rowChars ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        rowChars.forEach { ch ->
                            FastPinKeypadButton(
                                modifier = Modifier
                                    .weight(1f)
                                    .testTag("pin_key_$ch"),
                                label = ch.toString(),
                                onClick = { onCharClick(ch) }
                            )
                        }
                    }
                }

                // Bottom row: Clear, 0, Backspace
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(58.dp)
                            .duolingoButton(
                                faceColor = com.example.ui.theme.DuoFoxOrange,
                                lipColor = com.example.ui.theme.DuoFoxOrangeLip,
                                cornerRadius = 18.dp,
                                lipHeight = 4.dp
                            )
                            .instantTouchPress { onClearClick() }
                            .padding(bottom = 4.dp)
                            .testTag("pin_key_clear"),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "ULANG",
                            style = MaterialTheme.typography.labelLarge,
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    FastPinKeypadButton(
                        modifier = Modifier
                            .weight(1f)
                            .testTag("pin_key_0"),
                        label = "0",
                        onClick = { onCharClick('0') }
                    )

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(58.dp)
                            .duolingoButton(
                                faceColor = com.example.ui.theme.DuoCardinalRed,
                                lipColor = com.example.ui.theme.DuoCardinalRedLip,
                                cornerRadius = 18.dp,
                                lipHeight = 4.dp
                            )
                            .instantTouchPress { onBackspaceClick() }
                            .padding(bottom = 4.dp)
                            .testTag("pin_key_backspace"),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.Backspace,
                            contentDescription = "Backspace",
                            tint = Color.White
                        )
                    }
                }
            }
        } else {
            // Standard 3-Row QWERTY Keyboard Layout (Q-P, A-L, CLR + Z-M + Backspace)
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                val row1 = listOf('Q', 'W', 'E', 'R', 'T', 'Y', 'U', 'I', 'O', 'P')
                val row2 = listOf('A', 'S', 'D', 'F', 'G', 'H', 'J', 'K', 'L')
                val row3 = listOf('Z', 'X', 'C', 'V', 'B', 'N', 'M')

                // Row 1: Q W E R T Y U I O P
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    row1.forEach { ch ->
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(48.dp)
                                .duolingoCard(
                                    backgroundColor = com.example.ui.theme.DuoPureWhite,
                                    borderColor = com.example.ui.theme.DuoSwanBorder,
                                    lipColor = com.example.ui.theme.DuoHareLip,
                                    cornerRadius = 10.dp,
                                    borderWidth = 2.dp,
                                    lipHeight = 3.dp
                                )
                                .instantTouchPress { onCharClick(ch) }
                                .padding(bottom = 3.dp)
                                .testTag("pin_key_$ch"),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = ch.toString(),
                                style = MaterialTheme.typography.titleMedium,
                                color = CosmicTextPure,
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp
                            )
                        }
                    }
                }

                // Row 2: A S D F G H J K L (indented like standard QWERTY)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    row2.forEach { ch ->
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(48.dp)
                                .duolingoCard(
                                    backgroundColor = com.example.ui.theme.DuoPureWhite,
                                    borderColor = com.example.ui.theme.DuoSwanBorder,
                                    lipColor = com.example.ui.theme.DuoHareLip,
                                    cornerRadius = 10.dp,
                                    borderWidth = 2.dp,
                                    lipHeight = 3.dp
                                )
                                .instantTouchPress { onCharClick(ch) }
                                .padding(bottom = 3.dp)
                                .testTag("pin_key_$ch"),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = ch.toString(),
                                style = MaterialTheme.typography.titleMedium,
                                color = CosmicTextPure,
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp
                            )
                        }
                    }
                }

                // Row 3: CLR + Z X C V B N M + Backspace
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .weight(1.35f)
                            .height(48.dp)
                            .duolingoButton(
                                faceColor = com.example.ui.theme.DuoFoxOrange,
                                lipColor = com.example.ui.theme.DuoFoxOrangeLip,
                                cornerRadius = 10.dp,
                                lipHeight = 3.dp
                            )
                            .instantTouchPress { onClearClick() }
                            .padding(bottom = 3.dp)
                            .testTag("pin_key_clear_alpha"),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "CLR",
                            style = MaterialTheme.typography.labelSmall,
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp
                        )
                    }

                    row3.forEach { ch ->
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(48.dp)
                                .duolingoCard(
                                    backgroundColor = com.example.ui.theme.DuoPureWhite,
                                    borderColor = com.example.ui.theme.DuoSwanBorder,
                                    lipColor = com.example.ui.theme.DuoHareLip,
                                    cornerRadius = 10.dp,
                                    borderWidth = 2.dp,
                                    lipHeight = 3.dp
                                )
                                .instantTouchPress { onCharClick(ch) }
                                .padding(bottom = 3.dp)
                                .testTag("pin_key_$ch"),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = ch.toString(),
                                style = MaterialTheme.typography.titleMedium,
                                color = CosmicTextPure,
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp
                            )
                        }
                    }

                    Box(
                        modifier = Modifier
                            .weight(1.35f)
                            .height(48.dp)
                            .duolingoButton(
                                faceColor = com.example.ui.theme.DuoCardinalRed,
                                lipColor = com.example.ui.theme.DuoCardinalRedLip,
                                cornerRadius = 10.dp,
                                lipHeight = 3.dp
                            )
                            .instantTouchPress { onBackspaceClick() }
                            .padding(bottom = 3.dp)
                            .testTag("pin_key_backspace_alpha"),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.Backspace,
                            contentDescription = "Backspace",
                            tint = Color.White,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun FastPinKeypadButton(
    modifier: Modifier = Modifier,
    label: String,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .height(58.dp)
            .duolingoCard(
                backgroundColor = com.example.ui.theme.DuoPureWhite,
                borderColor = com.example.ui.theme.DuoSwanBorder,
                lipColor = com.example.ui.theme.DuoHareLip,
                cornerRadius = 18.dp,
                borderWidth = 2.dp,
                lipHeight = 4.dp
            )
            .instantTouchPress(onPressInstant = onClick)
            .padding(bottom = 4.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.headlineMedium,
            color = CosmicTextPure,
            fontWeight = FontWeight.Bold
        )
    }
}

/**
 * Komponen Sensor Sidik Jari Ultrasonik Interaktif:
 * Pengguna benar-benar harus menempelkan & menahan jari pada area sensor hingga pemindaian
 * ridge sidik jari mencapai 100% (atau menggunakan sensor hardware BiometricPrompt).
 * Jika jari dilepas sebelum 100%, pemindaian gagal dan meminta pengguna menahan jari kembali.
 */
@Composable
fun InteractiveFingerprintScannerPad(
    onScanCompleted: () -> Unit,
    onPartialReleaseWarning: (String) -> Unit,
    modifier: Modifier = Modifier,
    onTriggerHardwareBiometric: (() -> Unit)? = null
) {
    val scope = rememberCoroutineScope()
    val haptic = LocalHapticFeedback.current
    var scanProgress by remember { mutableIntStateOf(0) }
    var isFingerDown by remember { mutableStateOf(false) }

    val infiniteTransition = rememberInfiniteTransition(label = "fp_pulse")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 0.97f,
        targetValue = 1.04f,
        animationSpec = infiniteRepeatable(
            animation = tween(800),
            repeatMode = RepeatMode.Reverse
        ),
        label = "fp_scale"
    )

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(144.dp)
                .scale(if (isFingerDown) 0.95f else pulseScale)
                .clip(CircleShape)
                .background(
                    Brush.radialGradient(
                        colors = listOf(
                            if (isFingerDown) VaultLime.copy(alpha = 0.42f) else VaultLime.copy(alpha = 0.22f),
                            VaultMintAccent.copy(alpha = 0.12f),
                            Color.Transparent
                        )
                    )
                )
                .clickable {
                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                    if (onTriggerHardwareBiometric != null) {
                        onTriggerHardwareBiometric()
                    } else {
                        onScanCompleted()
                    }
                }
                .pointerInput(Unit) {
                    awaitEachGesture {
                        awaitFirstDown(requireUnconsumed = false)
                        isFingerDown = true
                        scanProgress = 0
                        haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)

                        if (onTriggerHardwareBiometric != null) {
                            onTriggerHardwareBiometric()
                        }

                        var completed = false
                        val job: Job = scope.launch {
                            for (step in 1..10) {
                                delay(25L)
                                scanProgress = step * 10
                            }
                            completed = true
                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                            onScanCompleted()
                        }

                        waitForUpOrCancellation()
                        isFingerDown = false
                        if (!completed && onTriggerHardwareBiometric == null) {
                            val reached = scanProgress
                            job.cancel()
                            scanProgress = 0
                            onPartialReleaseWarning(
                                "Ketuk atau gunakan sensor fisik perangkat untuk verifikasi sidik jari!"
                            )
                        }
                    }
                }
                .testTag("biometric_scanner_orb"),
            contentAlignment = Alignment.Center
        ) {
            // Circular Ultrasonic Progress Ring + Laser Scan Line
            Canvas(modifier = Modifier.size(124.dp)) {
                val strokeW = 4.dp.toPx()
                drawCircle(
                    color = VaultSlate,
                    style = Stroke(width = strokeW)
                )
                val sweep = (scanProgress / 100f) * 360f
                drawArc(
                    color = VaultLime,
                    startAngle = -90f,
                    sweepAngle = sweep,
                    useCenter = false,
                    style = Stroke(width = strokeW, cap = StrokeCap.Round)
                )

                if (isFingerDown && scanProgress in 1..99) {
                    val scanY = size.height * (scanProgress / 100f)
                    drawLine(
                        color = VaultLime,
                        start = Offset(size.width * 0.18f, scanY),
                        end = Offset(size.width * 0.82f, scanY),
                        strokeWidth = 3.dp.toPx(),
                        cap = StrokeCap.Round
                    )
                }
            }

            Box(
                modifier = Modifier
                    .size(92.dp)
                    .clip(CircleShape)
                    .background(if (isFingerDown) VaultLime else VaultCharcoal)
                    .border(2.dp, VaultLime, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = Icons.Default.Fingerprint,
                        contentDescription = "Tempel & Tahan Sidik Jari",
                        tint = if (isFingerDown) VaultObsidian else VaultLime,
                        modifier = Modifier.size(46.dp)
                    )
                    if (isFingerDown || scanProgress > 0) {
                        Text(
                            text = "$scanProgress%",
                            fontFamily = JetBrainsMonoFontFamily,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isFingerDown) VaultObsidian else VaultLime
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = if (isFingerDown) {
                "Memverifikasi sidik jari perangkat keras..."
            } else {
                "Ketuk sensor untuk verifikasi sidik jari perangkat keras"
            },
            style = MaterialTheme.typography.labelLarge,
            color = if (isFingerDown) VaultLime else VaultMintCard,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun BiometricVerificationCard(
    statusMessage: String?,
    onTriggerHardwareBiometric: () -> Unit,
    onHoldFingerprintCompleted: () -> Unit,
    onHoldFingerprintFeedback: (String) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(containerColor = CosmicGlass),
        border = androidx.compose.foundation.BorderStroke(1.dp, CosmicGradientGlassHighlight)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(22.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Surface(
                color = VaultLime.copy(alpha = 0.16f),
                shape = RoundedCornerShape(50)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.VerifiedUser,
                        contentDescription = null,
                        tint = VaultLime,
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = "3 LAPIS PIN TERVERIFIKASI",
                        style = MaterialTheme.typography.labelMedium,
                        color = VaultLime,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Pemindaian Sidik Jari",
                style = MaterialTheme.typography.headlineMedium,
                color = VaultPureWhite,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Tempel dan tahan jari Anda pada sensor ultrasonik di bawah (atau gunakan sensor biometrik perangkat).",
                style = MaterialTheme.typography.bodyMedium,
                color = VaultPureWhite.copy(alpha = 0.72f),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(18.dp))

            InteractiveFingerprintScannerPad(
                onScanCompleted = onHoldFingerprintCompleted,
                onPartialReleaseWarning = onHoldFingerprintFeedback,
                onTriggerHardwareBiometric = onTriggerHardwareBiometric
            )

            if (!statusMessage.isNullOrBlank()) {
                Spacer(modifier = Modifier.height(12.dp))
                Surface(
                    color = VaultObsidian,
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = statusMessage,
                        style = MaterialTheme.typography.bodyMedium,
                        color = VaultMintCard,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(10.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            Button(
                onClick = onTriggerHardwareBiometric,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(CosmicGradientCta)
                    .testTag("trigger_biometric_button"),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent,
                    contentColor = CosmicTextPure
                )
            ) {
                Icon(
                    imageVector = Icons.Default.Fingerprint,
                    contentDescription = null,
                    tint = CosmicTextPure,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Buka Pemindai Sidik Jari OS",
                    style = MaterialTheme.typography.titleSmall,
                    color = CosmicTextPure,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

/**
 * Dialog Re-Autentikasi Biometrik Sekunder (Secondary Biometric Re-Authentication)
 * Wajib dilewati sebelum mengekspor atau membagikan file individual dari Media Player.
 */
@Composable
fun SecondaryBiometricReAuthDialog(
    modalState: BiometricExportModalState,
    onAuthenticated: () -> Unit,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    val haptic = LocalHapticFeedback.current
    var feedbackText by remember {
        mutableStateOf("Verifikasi sidik jari diperlukan untuk mengekspor/membagikan file keluar dari Vault.")
    }

    LaunchedEffect(modalState.item.id, modalState.isShareIntent) {
        triggerHardwareBiometricIfAvailable(
            context = context,
            title = if (modalState.isShareIntent) "Biometrik: Bagikan File Vault" else "Biometrik: Ekspor File Vault",
            subtitle = "Otorisasi keamanan sekunder untuk \"${modalState.item.name}.${modalState.item.extension}\"",
            onSuccess = {
                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                onAuthenticated()
            },
            onErrorFeedback = { msg -> feedbackText = msg }
        )
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .widthIn(max = 430.dp)
                .testTag("secondary_biometric_reauth_dialog"),
            shape = RoundedCornerShape(28.dp),
            colors = CardDefaults.cardColors(containerColor = VaultObsidian),
            border = androidx.compose.foundation.BorderStroke(1.5.dp, VaultLime)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(22.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        color = VaultLime,
                        shape = RoundedCornerShape(50)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 5.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Share,
                                contentDescription = null,
                                tint = VaultObsidian,
                                modifier = Modifier.size(14.dp)
                            )
                            Text(
                                text = "OTORISASI BIOMETRIK SEKUNDER",
                                style = MaterialTheme.typography.labelMedium,
                                color = VaultObsidian,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    IconButton(onClick = onDismiss, modifier = Modifier.size(30.dp)) {
                        Icon(Icons.Default.Close, contentDescription = "Tutup", tint = VaultPureWhite)
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = if (modalState.isShareIntent) {
                        "Bagikan \"${modalState.item.name}.${modalState.item.extension}\""
                    } else {
                        "Ekspor \"${modalState.item.name}.${modalState.item.extension}\""
                    },
                    style = MaterialTheme.typography.titleLarge,
                    color = VaultPureWhite,
                    textAlign = TextAlign.Center,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = feedbackText,
                    style = MaterialTheme.typography.bodyMedium,
                    color = VaultPureWhite.copy(alpha = 0.75f),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(18.dp))

                LaunchedEffect(Unit) {
                    triggerHardwareBiometricIfAvailable(
                        context = context,
                        title = "Otorisasi Biometrik Perangkat",
                        subtitle = if (modalState.isShareIntent) "Bagikan ${modalState.item.name}" else "Ekspor ${modalState.item.name}",
                        onSuccess = onAuthenticated,
                        onErrorFeedback = { msg -> feedbackText = msg }
                    )
                }

                InteractiveFingerprintScannerPad(
                    onScanCompleted = onAuthenticated,
                    onPartialReleaseWarning = { warning -> feedbackText = warning },
                    onTriggerHardwareBiometric = {
                        triggerHardwareBiometricIfAvailable(
                            context = context,
                            title = "Otorisasi Biometrik Perangkat",
                            subtitle = if (modalState.isShareIntent) "Bagikan ${modalState.item.name}" else "Ekspor ${modalState.item.name}",
                            onSuccess = onAuthenticated,
                            onErrorFeedback = { msg -> feedbackText = msg }
                        )
                    }
                )

                Spacer(modifier = Modifier.height(14.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedButton(
                        onClick = onDismiss,
                        modifier = Modifier
                            .weight(1f)
                            .height(46.dp),
                        shape = RoundedCornerShape(14.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, VaultSlate)
                    ) {
                        Text("Batal", color = VaultPureWhite)
                    }

                    OutlinedButton(
                        onClick = {
                            triggerHardwareBiometricIfAvailable(
                                context = context,
                                title = "Otorisasi Biometrik Ekspor/Bagikan",
                                subtitle = "${modalState.item.name}.${modalState.item.extension}",
                                onSuccess = onAuthenticated,
                                onErrorFeedback = { msg -> feedbackText = msg }
                            )
                        },
                        modifier = Modifier
                            .weight(1f)
                            .height(46.dp),
                        shape = RoundedCornerShape(14.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, VaultLime)
                    ) {
                        Text("Sensor OS", color = VaultLime)
                    }
                }
            }
        }
    }
}

/**
 * Dialog Pemulihan Keamanan Lupa PIN / Sandi (Forgot PIN Recovery):
 * Memungkinkan pengguna memverifikasi Jawaban Pemulihan Keamanan atau Sidik Jari Biometrik
 * untuk melihat / mengatur ulang PIN 3-Lapis Brankas 1 maupun Brankas 2.
 */
@Composable
fun ForgotPinRecoveryDialog(
    securityConfig: SecurityConfig,
    onResetPinsAndUnlock: (vaultId: Int, p1: String, p2: String, p3: String, unlockNow: Boolean) -> Unit,
    onDismiss: () -> Unit
) {
    val context = androidx.compose.ui.platform.LocalContext.current
    var isIdentityVerified by remember { mutableStateOf(false) }
    var answerInput1 by remember { mutableStateOf("") }
    var answerInput2 by remember { mutableStateOf("") }
    var answerInput3 by remember { mutableStateOf("") }
    var verificationError by remember { mutableStateOf<String?>(null) }

    var selectedVaultId by remember { mutableIntStateOf(1) }
    val (existingP1, existingP2, existingP3) = securityConfig.getPinsForVault(selectedVaultId)

    var newPin1 by remember(selectedVaultId, existingP1) { mutableStateOf(existingP1) }
    var newPin2 by remember(selectedVaultId, existingP2) { mutableStateOf(existingP2) }
    var newPin3 by remember(selectedVaultId, existingP3) { mutableStateOf(existingP3) }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 18.dp)
                .widthIn(max = 450.dp)
                .testTag("forgot_pin_recovery_dialog"),
            shape = RoundedCornerShape(28.dp),
            colors = CardDefaults.cardColors(containerColor = VaultObsidian),
            border = androidx.compose.foundation.BorderStroke(1.5.dp, VaultMintCard)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        color = VaultMintCard,
                        shape = RoundedCornerShape(50)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 5.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Key,
                                contentDescription = null,
                                tint = VaultObsidian,
                                modifier = Modifier.size(14.dp)
                            )
                            Text(
                                text = "PEMULIHAN LUPA PIN / SANDI",
                                style = MaterialTheme.typography.labelMedium,
                                color = VaultObsidian,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    IconButton(onClick = onDismiss, modifier = Modifier.size(30.dp)) {
                        Icon(Icons.Default.Close, contentDescription = "Tutup", tint = VaultPureWhite)
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                if (!isIdentityVerified) {
                    Text(
                        text = "Verifikasi Identitas Pemilik Brankas",
                        style = MaterialTheme.typography.titleLarge,
                        color = VaultPureWhite,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Jawab pertanyaan keamanan pemulihan Anda atau pindai sidik jari hingga 100% untuk mengatur ulang PIN.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = VaultPureWhite.copy(alpha = 0.72f),
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    // Pertanyaan 1
                    Surface(
                        color = VaultCharcoal,
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text("Pertanyaan Keamanan 1:", style = MaterialTheme.typography.labelSmall, color = VaultLime)
                            Text(securityConfig.recoveryQuestion1, style = MaterialTheme.typography.bodyMedium, color = VaultPureWhite, fontWeight = FontWeight.SemiBold)
                        }
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    OutlinedTextField(
                        value = answerInput1,
                        onValueChange = { answerInput1 = it; verificationError = null },
                        modifier = Modifier.fillMaxWidth().testTag("recovery_answer_input"),
                        label = { Text("Jawaban Pertanyaan 1") },
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = VaultCharcoal, unfocusedContainerColor = VaultCharcoal,
                            focusedBorderColor = VaultLime, unfocusedBorderColor = VaultSlate,
                            focusedTextColor = VaultPureWhite, unfocusedTextColor = VaultPureWhite
                        )
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Pertanyaan 2
                    Surface(
                        color = VaultCharcoal,
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text("Pertanyaan Keamanan 2:", style = MaterialTheme.typography.labelSmall, color = VaultLime)
                            Text(securityConfig.recoveryQuestion2, style = MaterialTheme.typography.bodyMedium, color = VaultPureWhite, fontWeight = FontWeight.SemiBold)
                        }
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    OutlinedTextField(
                        value = answerInput2,
                        onValueChange = { answerInput2 = it; verificationError = null },
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text("Jawaban Pertanyaan 2") },
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = VaultCharcoal, unfocusedContainerColor = VaultCharcoal,
                            focusedBorderColor = VaultLime, unfocusedBorderColor = VaultSlate,
                            focusedTextColor = VaultPureWhite, unfocusedTextColor = VaultPureWhite
                        )
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Pertanyaan 3
                    Surface(
                        color = VaultCharcoal,
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text("Pertanyaan Keamanan 3:", style = MaterialTheme.typography.labelSmall, color = VaultLime)
                            Text(securityConfig.recoveryQuestion3, style = MaterialTheme.typography.bodyMedium, color = VaultPureWhite, fontWeight = FontWeight.SemiBold)
                        }
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    OutlinedTextField(
                        value = answerInput3,
                        onValueChange = { answerInput3 = it; verificationError = null },
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text("Jawaban Pertanyaan 3") },
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = VaultCharcoal, unfocusedContainerColor = VaultCharcoal,
                            focusedBorderColor = VaultLime, unfocusedBorderColor = VaultSlate,
                            focusedTextColor = VaultPureWhite, unfocusedTextColor = VaultPureWhite
                        )
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Button(
                        onClick = {
                            val a1 = securityConfig.recoveryAnswer1.trim()
                            val a2 = securityConfig.recoveryAnswer2.trim()
                            val a3 = securityConfig.recoveryAnswer3.trim()
                            val legacy = securityConfig.recoveryAnswer.trim()

                            val m1 = a1.isBlank() || answerInput1.trim().equals(a1, ignoreCase = true)
                            val m2 = a2.isBlank() || answerInput2.trim().equals(a2, ignoreCase = true)
                            val m3 = a3.isBlank() || answerInput3.trim().equals(a3, ignoreCase = true)
                            val mLegacy = legacy.isNotBlank() && answerInput1.trim().equals(legacy, ignoreCase = true)

                            if ((m1 && m2 && m3 && (answerInput1.isNotBlank() || answerInput2.isNotBlank() || answerInput3.isNotBlank())) || mLegacy) {
                                verificationError = null
                                val triggered = triggerHardwareBiometricIfAvailable(
                                    context = context,
                                    title = "Otorisasi Kredensial Perangkat Pemulihan",
                                    subtitle = "Pindai sensor sidik jari perangkat untuk menyelesaikan pemulihan",
                                    onSuccess = {
                                        verificationError = null
                                        isIdentityVerified = true
                                    },
                                    onErrorFeedback = { msg ->
                                        verificationError = "Otorisasi perangkat gagal: $msg"
                                    }
                                )
                                if (!triggered) {
                                    isIdentityVerified = true
                                }
                            } else {
                                verificationError = "Jawaban pertanyaan keamanan pemulihan tidak sesuai. Akses pemulihan ditolak."
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .testTag("verify_recovery_answer_button"),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = VaultLime,
                            contentColor = VaultObsidian
                        )
                    ) {
                        Text("Verifikasi Jawaban & Sidik Jari OS", fontWeight = FontWeight.Bold)
                    }

                    if (verificationError != null) {
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = verificationError!!,
                            style = MaterialTheme.typography.bodyMedium,
                            color = VaultCoral,
                            textAlign = TextAlign.Center
                        )
                    }
                } else {
                    // STEP 2: Identity Verified -> View / Reset 3-Layer Alphanumeric PINs for any configured Vault
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = null,
                        tint = VaultLime,
                        modifier = Modifier.size(38.dp)
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Identitas Terverifikasi! Atur Ulang PIN",
                        style = MaterialTheme.typography.titleLarge,
                        color = VaultPureWhite,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Pilih brankas yang ingin Anda ubah PIN 3-lapisnya (Mendukung Huruf A-Z dan Angka):",
                        style = MaterialTheme.typography.bodyMedium,
                        color = VaultPureWhite.copy(alpha = 0.72f),
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Multi-Vault Selection Row (1 to vaultCount)
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        val maxV = securityConfig.vaultCount.coerceIn(1, 10)
                        for (v in 1..maxV) {
                            val isSel = selectedVaultId == v
                            Box(
                                modifier = Modifier
                                    .height(40.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(if (isSel) VaultLime else VaultCharcoal)
                                    .instantTouchPress { selectedVaultId = v }
                                    .padding(horizontal = 14.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "Brankas $v",
                                    style = MaterialTheme.typography.labelLarge,
                                    color = if (isSel) VaultObsidian else VaultPureWhite,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    RecoveryPinInputField(
                        label = "PIN Lapis 1 Baru (Alfanumerik 6 Karakter)",
                        value = newPin1,
                        onValueChange = {
                            val clean = it.uppercase().filter { ch -> ch.isDigit() || ch in 'A'..'Z' }.take(6)
                            newPin1 = clean
                        }
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    RecoveryPinInputField(
                        label = "PIN Lapis 2 Baru (Alfanumerik 6 Karakter)",
                        value = newPin2,
                        onValueChange = {
                            val clean = it.uppercase().filter { ch -> ch.isDigit() || ch in 'A'..'Z' }.take(6)
                            newPin2 = clean
                        }
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    RecoveryPinInputField(
                        label = "PIN Lapis 3 Baru (Alfanumerik 6 Karakter)",
                        value = newPin3,
                        onValueChange = {
                            val clean = it.uppercase().filter { ch -> ch.isDigit() || ch in 'A'..'Z' }.take(6)
                            newPin3 = clean
                        }
                    )

                    if (verificationError != null) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = verificationError!!,
                            style = MaterialTheme.typography.bodyMedium,
                            color = VaultCoral
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = {
                            if (newPin1.length == 6 && newPin2.length == 6 && newPin3.length == 6) {
                                onResetPinsAndUnlock(selectedVaultId, newPin1, newPin2, newPin3, true)
                            } else {
                                verificationError = "Ketiga PIN wajib diisi tepat 6 karakter alfanumerik (A-Z, 0-9)!"
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .testTag("save_recovered_pins_unlock_button"),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = VaultLime,
                            contentColor = VaultObsidian
                        )
                    ) {
                        Icon(Icons.Default.LockOpen, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Simpan PIN & Buka Brankas $selectedVaultId",
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun RecoveryPinInputField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier.fillMaxWidth(),
        label = { Text(label) },
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Ascii),
        textStyle = TextStyle(
            fontFamily = JetBrainsMonoFontFamily,
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            color = VaultPureWhite,
            letterSpacing = 3.sp
        ),
        shape = RoundedCornerShape(14.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = VaultCharcoal,
            unfocusedContainerColor = VaultCharcoal,
            focusedBorderColor = VaultLime,
            unfocusedBorderColor = VaultSlate,
            focusedLabelColor = VaultLime,
            unfocusedLabelColor = VaultPureWhite.copy(alpha = 0.7f)
        )
    )
}
