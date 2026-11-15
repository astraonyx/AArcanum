package com.example.ui

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.pm.ActivityInfo
import android.graphics.Bitmap
import android.graphics.pdf.PdfRenderer
import android.media.AudioManager
import android.media.PlaybackParams
import android.net.Uri
import android.os.Build
import android.os.ParcelFileDescriptor
import android.view.WindowManager
import android.widget.VideoView
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Backspace
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.AspectRatio
import androidx.compose.material.icons.filled.BrightnessHigh
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.FastForward
import androidx.compose.material.icons.filled.FolderZip
import androidx.compose.material.icons.filled.Forward10
import androidx.compose.material.icons.filled.FullscreenExit
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.LockOpen
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Replay10
import androidx.compose.material.icons.filled.ScreenRotation
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material.icons.filled.SkipPrevious
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material.icons.filled.ZoomIn
import androidx.compose.material.icons.filled.ZoomOutMap
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.window.DialogWindowProvider
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import coil.compose.AsyncImage
import kotlinx.coroutines.launch
import com.example.data.VaultCategory
import com.example.data.VaultItemEntity
import com.example.ui.theme.JetBrainsMonoFontFamily
import com.example.ui.theme.VaultAmber
import com.example.ui.theme.VaultCharcoal
import com.example.ui.theme.VaultCoral
import com.example.ui.theme.VaultLime
import com.example.ui.theme.VaultMintCard
import com.example.ui.theme.VaultObsidian
import com.example.ui.theme.VaultPureWhite
import com.example.ui.theme.VaultSlate
import java.io.File
import kotlinx.coroutines.delay

private fun Context.findActivity(): Activity? {
    var ctx = this
    while (ctx is ContextWrapper) {
        if (ctx is Activity) return ctx
        ctx = ctx.baseContext
    }
    return null
}

/**
 * 1. Dialog Verifikasi PIN 6-Digit Saat Hendak Mengenkripsi atau Mendekripsi File
 * (Production Stealth — Tanpa hint PIN dan tanpa tombol autofill)
 */
@Composable
fun CryptoPinVerificationDialog(
    modalState: CryptoPinModalState,
    onDigitClick: (Int) -> Unit,
    onCharClick: (Char) -> Unit = { c -> c.digitToIntOrNull()?.let(onDigitClick) },
    onBackspaceClick: () -> Unit,
    onDismiss: () -> Unit,
    onForgotPinClick: () -> Unit = {}
) {
    var isAlphaMode by remember { mutableStateOf(false) }
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 18.dp)
                .widthIn(max = 440.dp),
            shape = RoundedCornerShape(30.dp),
            colors = CardDefaults.cardColors(containerColor = VaultObsidian),
            border = androidx.compose.foundation.BorderStroke(1.5.dp, VaultLime.copy(alpha = 0.5f))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        color = if (modalState.targetEncryptState) VaultLime else VaultMintCard,
                        shape = RoundedCornerShape(50)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 5.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(
                                imageVector = if (modalState.targetEncryptState) Icons.Default.Lock else Icons.Default.LockOpen,
                                contentDescription = null,
                                tint = VaultObsidian,
                                modifier = Modifier.size(14.dp)
                            )
                            Text(
                                text = if (modalState.targetEncryptState) "PIN ENKRIPSI NYX2" else "PIN DEKRIPSI NYX2",
                                style = MaterialTheme.typography.labelMedium,
                                color = VaultObsidian,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    IconButton(onClick = onDismiss, modifier = Modifier.size(30.dp)) {
                        Icon(Icons.Default.Close, contentDescription = "Batal", tint = VaultPureWhite)
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                NyxMascotAvatar(
                    mood = if (modalState.enteredPin.isNotEmpty()) MascotMood.SHIELD_EYES else MascotMood.GUARD_ALERT,
                    size = 64.dp
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = modalState.title,
                    style = MaterialTheme.typography.titleLarge,
                    color = VaultPureWhite,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = modalState.subtitle,
                    style = MaterialTheme.typography.bodyMedium,
                    color = VaultPureWhite.copy(alpha = 0.72f),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(14.dp))

                // 6-Digit PIN Dots
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally)
                ) {
                    for (i in 0 until 6) {
                        val isFilled = i < modalState.enteredPin.length
                        Box(
                            modifier = Modifier
                                .size(width = 40.dp, height = 46.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(if (isFilled) VaultLime else VaultCharcoal)
                                .border(
                                    width = 1.dp,
                                    color = if (isFilled) VaultLime else VaultSlate,
                                    shape = RoundedCornerShape(12.dp)
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            if (isFilled) {
                                Box(
                                    modifier = Modifier
                                        .size(11.dp)
                                        .clip(CircleShape)
                                        .background(VaultObsidian)
                                )
                            } else {
                                Text(
                                    text = "•",
                                    style = MaterialTheme.typography.titleMedium,
                                    color = VaultPureWhite.copy(alpha = 0.35f)
                                )
                            }
                        }
                    }
                }

                if (modalState.errorText != null) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = modalState.errorText,
                        style = MaterialTheme.typography.bodyMedium,
                        color = VaultCoral,
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                // 123 Angka / QWERTY Huruf Mode Switcher
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Surface(
                        color = if (!isAlphaMode) VaultLime else VaultCharcoal,
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .clickable { isAlphaMode = false }
                    ) {
                        Text(
                            text = "1 2 3 Angka",
                            color = if (!isAlphaMode) VaultObsidian else VaultPureWhite,
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp,
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Surface(
                        color = if (isAlphaMode) VaultLime else VaultCharcoal,
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .clickable { isAlphaMode = true }
                    ) {
                        Text(
                            text = "Q W E R T Y Huruf",
                            color = if (isAlphaMode) VaultObsidian else VaultPureWhite,
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp,
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                if (!isAlphaMode) {
                    // Fast Zero-Lag Numeric Keypad Grid
                    val rows = listOf(
                        listOf(1, 2, 3),
                        listOf(4, 5, 6),
                        listOf(7, 8, 9)
                    )
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        rows.forEach { row ->
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                row.forEach { digit ->
                                    Box(
                                        modifier = Modifier
                                            .weight(1f)
                                            .height(48.dp)
                                            .clip(RoundedCornerShape(14.dp))
                                            .background(VaultCharcoal)
                                            .instantTouchPress { onCharClick(digit.toString()[0]) }
                                            .testTag("crypto_modal_key_$digit"),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = digit.toString(),
                                            style = MaterialTheme.typography.titleLarge,
                                            color = VaultPureWhite
                                        )
                                    }
                                }
                            }
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .height(48.dp)
                                    .clip(RoundedCornerShape(14.dp))
                                    .background(VaultSlate)
                                    .instantTouchPress { onDismiss() },
                                contentAlignment = Alignment.Center
                            ) {
                                Text("BATAL", style = MaterialTheme.typography.labelLarge, color = VaultCoral)
                            }

                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .height(48.dp)
                                    .clip(RoundedCornerShape(14.dp))
                                    .background(VaultCharcoal)
                                    .instantTouchPress { onCharClick('0') }
                                    .testTag("crypto_modal_key_0"),
                                contentAlignment = Alignment.Center
                            ) {
                                Text("0", style = MaterialTheme.typography.titleLarge, color = VaultPureWhite)
                            }

                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .height(48.dp)
                                    .clip(RoundedCornerShape(14.dp))
                                    .background(VaultSlate)
                                    .instantTouchPress { onBackspaceClick() },
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.Backspace,
                                    contentDescription = "Hapus",
                                    tint = VaultPureWhite
                                )
                            }
                        }
                    }
                } else {
                    // Standard 3-Row QWERTY Keyboard Layout
                    val row1 = listOf('Q', 'W', 'E', 'R', 'T', 'Y', 'U', 'I', 'O', 'P')
                    val row2 = listOf('A', 'S', 'D', 'F', 'G', 'H', 'J', 'K', 'L')
                    val row3 = listOf('Z', 'X', 'C', 'V', 'B', 'N', 'M')

                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            row1.forEach { ch ->
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(44.dp)
                                        .clip(RoundedCornerShape(10.dp))
                                        .background(VaultCharcoal)
                                        .instantTouchPress { onCharClick(ch) }
                                        .testTag("crypto_modal_key_$ch"),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = ch.toString(),
                                        style = MaterialTheme.typography.titleMedium,
                                        color = VaultPureWhite,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 10.dp),
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            row2.forEach { ch ->
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(44.dp)
                                        .clip(RoundedCornerShape(10.dp))
                                        .background(VaultCharcoal)
                                        .instantTouchPress { onCharClick(ch) }
                                        .testTag("crypto_modal_key_$ch"),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = ch.toString(),
                                        style = MaterialTheme.typography.titleMedium,
                                        color = VaultPureWhite,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .weight(1.35f)
                                    .height(44.dp)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(VaultSlate)
                                    .instantTouchPress { onDismiss() },
                                contentAlignment = Alignment.Center
                            ) {
                                Text("BTL", style = MaterialTheme.typography.labelSmall, color = VaultCoral, fontWeight = FontWeight.Bold)
                            }

                            row3.forEach { ch ->
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(44.dp)
                                        .clip(RoundedCornerShape(10.dp))
                                        .background(VaultCharcoal)
                                        .instantTouchPress { onCharClick(ch) }
                                        .testTag("crypto_modal_key_$ch"),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = ch.toString(),
                                        style = MaterialTheme.typography.titleMedium,
                                        color = VaultPureWhite,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }

                            Box(
                                modifier = Modifier
                                    .weight(1.35f)
                                    .height(44.dp)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(VaultSlate)
                                    .instantTouchPress { onBackspaceClick() },
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.Backspace,
                                    contentDescription = "Hapus",
                                    tint = VaultPureWhite,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }
                    }
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 6.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(50))
                            .instantTouchPress {
                                onDismiss()
                                onForgotPinClick()
                            }
                            .padding(horizontal = 12.dp, vertical = 4.dp)
                            .testTag("crypto_modal_forgot_pin_button")
                    ) {
                        Text(
                            text = "Lupa PIN Otorisasi? Pulihkan di Sini",
                            style = MaterialTheme.typography.labelMedium,
                            color = VaultLime
                        )
                    }
                }
            }
        }
    }
}

/**
 * 2. Full-Screen Rich Media, Video (MP4/MOV), Image (JPG/PNG/WEBP/GIF/Sticker) & Document Viewer
 * - For VIDEO: True 100% Edge-to-Edge Immersive Cinema Fullscreen (9:16, 16:9, Auto-Rotate, Immersive System Bars)
 */
@Composable
fun VaultRichMediaViewerOverlay(
    item: VaultItemEntity,
    mediaState: MediaPlayerUiState,
    onClose: () -> Unit,
    onRequestReEncryptWithPin: () -> Unit,
    onRequestExportOrShareWithBiometric: (Boolean) -> Unit = {},
    onToggleVideoPlayPause: () -> Unit,
    onSeekVideoSeconds: (Int) -> Unit,
    onSeekVideoPositionMs: (Long) -> Unit = {},
    onUpdateActualDurationMs: (Long) -> Unit = {},
    onSyncHardwarePositionMs: (Long, Long) -> Unit = { _, _ -> },
    onSkipTrack: (Boolean) -> Unit = {},
    onSetHold2xSpeed: (Boolean) -> Unit = {},
    onCycleSpeed: () -> Unit,
    photoGalleryItems: List<VaultItemEntity> = emptyList(),
    onSelectPhotoFromPager: (VaultItemEntity) -> Unit = {},
    onDeleteCurrentItem: (VaultItemEntity) -> Unit = {}
) {
    val categoryEnum = VaultCategory.entries.find { it.name == item.category } ?: VaultCategory.DOCUMENT

    Dialog(
        onDismissRequest = onClose,
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            decorFitsSystemWindows = false
        )
    ) {
        if (categoryEnum == VaultCategory.VIDEO) {
            // TRUE EDGE-TO-EDGE IMMERSIVE CINEMA FULLSCREEN VIDEO PLAYER (16:9, 9:16, AUTO-ROTATE)
            CinemaVideoPlayerViewport(
                item = item,
                mediaState = mediaState,
                onClose = onClose,
                onRequestReEncryptWithPin = onRequestReEncryptWithPin,
                onRequestExportOrShareWithBiometric = onRequestExportOrShareWithBiometric,
                onTogglePlayPause = onToggleVideoPlayPause,
                onSeekSeconds = onSeekVideoSeconds,
                onSeekPositionMs = onSeekVideoPositionMs,
                onUpdateActualDurationMs = onUpdateActualDurationMs,
                onSyncHardwarePositionMs = onSyncHardwarePositionMs,
                onSkipTrack = onSkipTrack,
                onSetHold2xSpeed = onSetHold2xSpeed,
                onCycleSpeed = onCycleSpeed
            )
        } else {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(VaultObsidian)
                    .windowInsetsPadding(WindowInsets.safeDrawing)
                    .testTag("rich_media_viewer_overlay")
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .widthIn(max = 680.dp)
                        .align(Alignment.Center)
                        .padding(horizontal = 18.dp, vertical = 12.dp),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    // Top Viewer Header Bar
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Surface(
                                    color = VaultLime,
                                    shape = RoundedCornerShape(50)
                                ) {
                                    Text(
                                        text = item.extension.uppercase(),
                                        style = MaterialTheme.typography.labelMedium,
                                        color = VaultObsidian,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 3.dp)
                                    )
                                }
                                TruncatedHoldTooltipText(
                                    text = "${item.name}.${item.extension}",
                                    style = MaterialTheme.typography.titleLarge,
                                    color = VaultPureWhite
                                )
                            }
                            Text(
                                text = buildString {
                                    append("Terenkripsi • ${formatFileSize(item.sizeBytes)}")
                                    if (!item.groupArchiveName.isNullOrBlank()) {
                                        append(" • ZIP: ${item.groupArchiveName}")
                                    }
                                },
                                style = MaterialTheme.typography.labelMedium,
                                color = VaultMintCard
                            )
                        }

                        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            Surface(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(14.dp))
                                    .instantTouchPress { onRequestExportOrShareWithBiometric(true) }
                                    .testTag("viewer_share_biometric_button"),
                                color = VaultLime
                            ) {
                                Text(
                                    text = "Bagikan",
                                    style = MaterialTheme.typography.labelMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = VaultObsidian,
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp)
                                )
                            }

                            Surface(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(14.dp))
                                    .instantTouchPress { onRequestExportOrShareWithBiometric(false) }
                                    .testTag("viewer_export_biometric_button"),
                                color = VaultMintCard
                            ) {
                                Text(
                                    text = "Ekspor",
                                    style = MaterialTheme.typography.labelMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = VaultObsidian,
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp)
                                )
                            }

                            Surface(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(14.dp))
                                    .clickable { onRequestReEncryptWithPin() }
                                    .testTag("viewer_lock_again_button"),
                                color = VaultCharcoal,
                                border = BorderStroke(1.dp, VaultLime.copy(alpha = 0.5f))
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Lock,
                                        contentDescription = "Kunci Kembali",
                                        tint = VaultLime,
                                        modifier = Modifier.size(15.dp)
                                    )
                                    Text(
                                        text = "Kunci",
                                        style = MaterialTheme.typography.labelMedium,
                                        color = VaultLime
                                    )
                                }
                            }

                            Surface(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .clickable { onClose() }
                                    .testTag("viewer_close_button"),
                                color = VaultSlate
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(Icons.Default.Close, contentDescription = "Tutup", tint = VaultPureWhite)
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Center Viewport Based on File Type
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        when (categoryEnum) {
                            VaultCategory.IMAGE -> {
                                InteractiveImageAndStickerViewport(
                                    item = item,
                                    photoGalleryItems = photoGalleryItems,
                                    onSelectPhotoFromPager = onSelectPhotoFromPager,
                                    onRequestShare = { onRequestExportOrShareWithBiometric(true) },
                                    onRequestExport = { onRequestExportOrShareWithBiometric(false) },
                                    onRequestLock = onRequestReEncryptWithPin,
                                    onDeletePhoto = { targetPhoto ->
                                        onDeleteCurrentItem(targetPhoto)
                                        onClose()
                                    }
                                )
                            }

                            else -> {
                                DecryptedDocumentReaderViewport(item = item)
                            }
                        }
                    }
                }
            }
        }
    }
}

private enum class FullscreenOrientationMode(val label: String) {
    LANDSCAPE_16_9("Kunci Lanskap"),
    PORTRAIT_9_16("Kunci Potret"),
    AUTO_SENSOR("Auto Video")
}

private enum class FullscreenAspectRatioMode(val label: String) {
    FILL_SCREEN("Penuh (Tanpa Notch)"),
    AUTO_NATIVE("Rasio Asli"),
    WIDESCREEN_16_9("16:9"),
    VERTICAL_9_16("9:16")
}

@Composable
internal fun CinemaVideoPlayerViewport(
    item: VaultItemEntity,
    mediaState: MediaPlayerUiState,
    onClose: () -> Unit,
    onRequestReEncryptWithPin: () -> Unit,
    onRequestExportOrShareWithBiometric: (Boolean) -> Unit,
    onTogglePlayPause: () -> Unit,
    onSeekSeconds: (Int) -> Unit,
    onSeekPositionMs: (Long) -> Unit,
    onUpdateActualDurationMs: (Long) -> Unit,
    onSyncHardwarePositionMs: (Long, Long) -> Unit,
    onSkipTrack: (Boolean) -> Unit,
    onSetHold2xSpeed: (Boolean) -> Unit,
    onCycleSpeed: () -> Unit
) {
    val context = LocalContext.current
    val view = LocalView.current
    val gestureScope = rememberCoroutineScope()
    val currentMediaState by rememberUpdatedState(mediaState)
    val activity = remember(context) { context.findActivity() }
    val audioManager = remember(context) {
        context.getSystemService(Context.AUDIO_SERVICE) as? AudioManager
    }
    val plainFile = remember(item.internalPlainPath) { File(item.internalPlainPath) }
    var videoErrorFallback by remember(item.id, item.internalPlainPath) { mutableStateOf(false) }

    val youtubeRed = Color(0xFFFF0000)

    // Native video dimensions detected on MediaPlayer prepare
    var nativeVideoWidth by remember(item.id) { mutableIntStateOf(16) }
    var nativeVideoHeight by remember(item.id) { mutableIntStateOf(9) }
    var hasDetectedDimensions by remember(item.id) { mutableStateOf(false) }

    // Default to LANDSCAPE_16_9 lock when entering Fullscreen so it auto-rotates from Portrait and stays locked in Fullscreen!
    var orientationMode by remember { mutableStateOf(FullscreenOrientationMode.LANDSCAPE_16_9) }
    var aspectMode by remember { mutableStateOf(FullscreenAspectRatioMode.FILL_SCREEN) }
    var showOsdControls by remember { mutableStateOf(true) }
    var isScreenLocked by remember { mutableStateOf(false) }
    var lastInteractionTick by remember { mutableIntStateOf(0) }

    // Brightness & 400% Audio Booster vertical swipe HUD states
    var screenBrightness by remember {
        val currentAttr = activity?.window?.attributes?.screenBrightness ?: -1f
        mutableFloatStateOf(if (currentAttr in 0.05f..1.0f) currentAttr else 0.75f)
    }
    var audioVolumePercent by remember {
        val maxVol = (audioManager?.getStreamMaxVolume(AudioManager.STREAM_MUSIC) ?: 15).coerceAtLeast(1)
        val curVol = audioManager?.getStreamVolume(AudioManager.STREAM_MUSIC) ?: (maxVol / 2)
        mutableIntStateOf(((curVol.toFloat() / maxVol.toFloat()) * 100f).toInt().coerceIn(0, 100))
    }
    var loudnessEnhancerRef by remember(item.id) { mutableStateOf<android.media.audiofx.LoudnessEnhancer?>(null) }
    var gestureHudLabel by remember { mutableStateOf<String?>(null) }
    var gestureHudIsBrightness by remember { mutableStateOf(true) }
    var gestureHudTick by remember { mutableIntStateOf(0) }

    fun applyVideoVolumePercent(targetPct: Int) {
        val clamped = targetPct.coerceIn(0, 400)
        audioVolumePercent = clamped
        audioManager?.let { am ->
            val maxV = am.getStreamMaxVolume(AudioManager.STREAM_MUSIC).coerceAtLeast(1)
            val sysFrac = (clamped.coerceIn(0, 100) / 100f)
            val targetSysVol = (sysFrac * maxV).toInt().coerceIn(0, maxV)
            try {
                am.setStreamVolume(AudioManager.STREAM_MUSIC, targetSysVol, 0)
            } catch (_: Exception) {
            }
        }
        try {
            val enhancer = loudnessEnhancerRef
            if (enhancer != null) {
                if (clamped > 100) {
                    val gainMb = ((clamped - 100) * 8).coerceIn(0, 2400)
                    enhancer.setTargetGain(gainMb)
                    enhancer.enabled = true
                } else {
                    enhancer.setTargetGain(0)
                    enhancer.enabled = false
                }
            }
        } catch (_: Exception) {
        }
    }

    DisposableEffect(item.id) {
        onDispose {
            try {
                loudnessEnhancerRef?.release()
            } catch (_: Exception) {
            }
            loudnessEnhancerRef = null
        }
    }

    LaunchedEffect(gestureHudTick) {
        if (gestureHudTick > 0) {
            delay(850L)
            gestureHudLabel = null
        }
    }

    // Double-tap ±10s seek feedback state
    var seekFeedbackText by remember { mutableStateOf<String?>(null) }
    var seekFeedbackSide by remember { mutableIntStateOf(0) } // -1 left, +1 right
    var seekFeedbackTrigger by remember { mutableIntStateOf(0) }

    LaunchedEffect(seekFeedbackTrigger) {
        if (seekFeedbackTrigger > 0) {
            delay(650L)
            seekFeedbackText = null
            seekFeedbackSide = 0
        }
    }

    // Auto-hide OSD controls after 3.6 seconds while playing so fullscreen is 100% clean
    LaunchedEffect(showOsdControls, mediaState.isPlaying, lastInteractionTick, isScreenLocked) {
        if (showOsdControls && mediaState.isPlaying && !isScreenLocked) {
            delay(3600L)
            showOsdControls = false
        }
    }

    // True Edge-to-Edge Cutout-Free Fullscreen on BOTH Activity Window & Dialog Window + Lock Rotation Restoration
    DisposableEffect(activity, view) {
        val dialogWindow = (view.parent as? DialogWindowProvider)?.window
        val actWindow = activity?.window
        val prevCutoutMode = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            actWindow?.attributes?.layoutInDisplayCutoutMode
                ?: WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_DEFAULT
        } else 0

        fun applyTrueCutoutFreeImmersive(w: android.view.Window?) {
            if (w == null) return
            WindowCompat.setDecorFitsSystemWindows(w, false)
            w.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
            w.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                val lp = w.attributes
                lp.layoutInDisplayCutoutMode = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_ALWAYS
                } else {
                    WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES
                }
                w.attributes = lp
            }
            val controller = WindowCompat.getInsetsController(w, view)
            controller.systemBarsBehavior =
                WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            controller.hide(
                WindowInsetsCompat.Type.systemBars() or WindowInsetsCompat.Type.displayCutout()
            )
        }

        applyTrueCutoutFreeImmersive(actWindow)
        applyTrueCutoutFreeImmersive(dialogWindow)

        onDispose {
            onSetHold2xSpeed(false)
            actWindow?.let { w ->
                w.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
                w.clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    val lp = w.attributes
                    lp.layoutInDisplayCutoutMode = prevCutoutMode
                    lp.screenBrightness = WindowManager.LayoutParams.BRIGHTNESS_OVERRIDE_NONE
                    w.attributes = lp
                }
                WindowCompat.getInsetsController(w, view).show(WindowInsetsCompat.Type.systemBars())
            }
            // Saat keluar dari Fullscreen, otomatis kembalikan orientasi layar ke Portrait normal!
            activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        }
    }

    // Auto-rotate and LOCK orientation while inside Fullscreen (must exit fullscreen or tap switcher to rotate back)
    LaunchedEffect(activity, orientationMode, hasDetectedDimensions, nativeVideoWidth, nativeVideoHeight) {
        val act = activity ?: return@LaunchedEffect
        val isPortraitVideo = hasDetectedDimensions && nativeVideoHeight > nativeVideoWidth
        when (orientationMode) {
            FullscreenOrientationMode.LANDSCAPE_16_9 -> {
                act.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE
            }
            FullscreenOrientationMode.PORTRAIT_9_16 -> {
                act.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT
            }
            FullscreenOrientationMode.AUTO_SENSOR -> {
                // Kunci sesuai dimensi asli video (16:9 Lanskap atau 9:16 Potret) selama masih di Fullscreen
                act.requestedOrientation = if (isPortraitVideo) {
                    ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT
                } else {
                    ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE
                }
            }
        }
    }

    val targetAspectRatio: Float? = when (aspectMode) {
        FullscreenAspectRatioMode.FILL_SCREEN -> null
        FullscreenAspectRatioMode.WIDESCREEN_16_9 -> 16f / 9f
        FullscreenAspectRatioMode.VERTICAL_9_16 -> 9f / 16f
        FullscreenAspectRatioMode.AUTO_NATIVE -> {
            if (nativeVideoWidth > 0 && nativeVideoHeight > 0) {
                (nativeVideoWidth.toFloat() / nativeVideoHeight.toFloat()).coerceIn(0.4f, 2.6f)
            } else {
                null
            }
        }
    }

    var lastHandledReplayNonce by remember(item.id) { mutableIntStateOf(mediaState.replayNonce) }
    var lastHandledSeekNonce by remember(item.id) { mutableIntStateOf(mediaState.userSeekNonce) }
    var videoViewRef by remember(item.id) { mutableStateOf<VideoView?>(null) }
    var mediaPlayerRef by remember(item.id) { mutableStateOf<android.media.MediaPlayer?>(null) }

    LaunchedEffect(mediaState.replayNonce) {
        if (videoErrorFallback) {
            videoErrorFallback = false
        }
    }

    // Poll real hardware VideoView position every 200ms while playing in Fullscreen
    LaunchedEffect(videoViewRef, mediaState.isPlaying) {
        val vv = videoViewRef
        if (vv != null && mediaState.isPlaying) {
            while (true) {
                delay(200L)
                try {
                    if (vv.isPlaying) {
                        val pos = vv.currentPosition.toLong()
                        val dur = vv.duration.toLong()
                        if (dur > 0L) {
                            onSyncHardwarePositionMs(pos, dur)
                        }
                    }
                } catch (_: Exception) {
                }
            }
        }
    }

    // Dynamic speed update when user cycles speed or holds 2x in Fullscreen
    LaunchedEffect(mediaPlayerRef, mediaState.playbackSpeed, mediaState.isHold2xActive) {
        val mp = mediaPlayerRef ?: return@LaunchedEffect
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            try {
                val speed = if (mediaState.isHold2xActive) 2.0f else mediaState.playbackSpeed
                mp.playbackParams = PlaybackParams().setSpeed(speed)
            } catch (_: Exception) {
            }
        }
    }

    // Full-Bleed Edge-to-Edge Black Cinema Canvas (No Notch / No Cutout Margin)
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .pointerInput(item.id, isScreenLocked) {
                detectTapGestures(
                    onTap = {
                        showOsdControls = !showOsdControls
                        lastInteractionTick++
                    },
                    onDoubleTap = { offset ->
                        if (!isScreenLocked) {
                            val snapshot = currentMediaState
                            val isLeft = offset.x < size.width / 2f
                            val delta = if (isLeft) -10 else 10
                            val targetSec = (snapshot.currentSeconds + delta)
                                .coerceIn(0, snapshot.totalSeconds.coerceAtLeast(1))
                            onSeekSeconds(targetSec)
                            seekFeedbackSide = if (isLeft) -1 else 1
                            seekFeedbackText = if (isLeft) "-10 detik" else "+10 detik"
                            seekFeedbackTrigger++
                            lastInteractionTick++
                        }
                    },
                    onLongPress = {
                        if (!isScreenLocked) {
                            onSetHold2xSpeed(true)
                        }
                    },
                    onPress = {
                        val holdJob = gestureScope.launch {
                            delay(210L)
                            if (!isScreenLocked) {
                                onSetHold2xSpeed(true)
                            }
                        }
                        try {
                            tryAwaitRelease()
                        } finally {
                            holdJob.cancel()
                            onSetHold2xSpeed(false)
                        }
                    }
                )
            }
            .pointerInput(item.id, isScreenLocked) {
                var dragStartX = 0f
                detectVerticalDragGestures(
                    onDragStart = { startOffset ->
                        dragStartX = startOffset.x
                    },
                    onVerticalDrag = { change, dragAmount ->
                        if (!isScreenLocked && size.height > 0) {
                            change.consume()
                            val deltaFraction = (-dragAmount / (size.height * 0.75f)).coerceIn(-0.15f, 0.15f)
                            val isLeftSide = dragStartX < size.width / 2f
                            if (isLeftSide) {
                                val nextBrightness = (screenBrightness + deltaFraction).coerceIn(0.05f, 1.0f)
                                screenBrightness = nextBrightness
                                activity?.window?.let { w ->
                                    val lp = w.attributes
                                    lp.screenBrightness = nextBrightness
                                    w.attributes = lp
                                }
                                (view.parent as? DialogWindowProvider)?.window?.let { dw ->
                                    val lp = dw.attributes
                                    lp.screenBrightness = nextBrightness
                                    dw.attributes = lp
                                }
                                gestureHudIsBrightness = true
                                gestureHudLabel = "Kecerahan: ${(nextBrightness * 100).toInt()}%"
                                gestureHudTick++
                            } else {
                                val stepPct = (deltaFraction * 400f).toInt()
                                val nextPct = (audioVolumePercent + stepPct).coerceIn(0, 400)
                                applyVideoVolumePercent(nextPct)
                                gestureHudIsBrightness = false
                                gestureHudLabel = if (nextPct > 100) {
                                    "Booster Suara: $nextPct% (MAX 400%)"
                                } else {
                                    "Volume: $nextPct%"
                                }
                                gestureHudTick++
                            }
                        }
                    }
                )
            }
            .testTag("rich_media_viewer_overlay"),
        contentAlignment = Alignment.Center
    ) {
        // 1. Video Surface Container (Supports Penuh Tanpa Notch, Rasio Asli, 16:9, or 9:16)
        Box(
            modifier = if (targetAspectRatio == null) {
                Modifier.fillMaxSize()
            } else if (targetAspectRatio < 1f) {
                Modifier
                    .fillMaxHeight()
                    .aspectRatio(targetAspectRatio, matchHeightConstraintsFirst = true)
            } else {
                Modifier
                    .fillMaxWidth()
                    .aspectRatio(targetAspectRatio)
            },
            contentAlignment = Alignment.Center
        ) {
            if (!videoErrorFallback && (plainFile.exists() || !item.mediaUriString.isNullOrBlank())) {
                key(item.id, item.internalPlainPath, item.mediaUriString) {
                    AndroidView(
                        factory = { ctx ->
                            VideoView(ctx).apply {
                                videoViewRef = this
                                setOnErrorListener { _, _, _ ->
                                    videoErrorFallback = true
                                    true
                                }
                                try {
                                    if (plainFile.exists() && plainFile.length() > 0L) {
                                        setVideoURI(Uri.fromFile(plainFile))
                                    } else if (!item.mediaUriString.isNullOrBlank()) {
                                        setVideoURI(Uri.parse(item.mediaUriString))
                                    }
                                } catch (_: Exception) {
                                    if (plainFile.exists()) {
                                        setVideoPath(plainFile.absolutePath)
                                    }
                                }
                                setOnPreparedListener { mp ->
                                    mediaPlayerRef = mp
                                    try {
                                        loudnessEnhancerRef?.release()
                                        val enhancer = android.media.audiofx.LoudnessEnhancer(mp.audioSessionId)
                                        loudnessEnhancerRef = enhancer
                                        applyVideoVolumePercent(audioVolumePercent)
                                    } catch (_: Exception) {
                                    }
                                    if (mp.videoWidth > 0 && mp.videoHeight > 0) {
                                        nativeVideoWidth = mp.videoWidth
                                        nativeVideoHeight = mp.videoHeight
                                        hasDetectedDimensions = true
                                    }
                                    mp.isLooping = mediaState.repeatMode == PlaybackRepeatMode.REPEAT_ONE
                                    val actualDurMs = mp.duration.toLong().coerceAtLeast(500L)
                                    onUpdateActualDurationMs(actualDurMs)
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                        try {
                                            val speed = if (mediaState.isHold2xActive) 2.0f else mediaState.playbackSpeed
                                            mp.playbackParams = PlaybackParams().setSpeed(speed)
                                        } catch (_: Exception) {
                                        }
                                    }
                                    val resumeMs = mediaState.currentPositionMs.toInt().coerceAtLeast(0)
                                    if (resumeMs > 0) {
                                        seekTo(resumeMs)
                                    }
                                    if (mediaState.isPlaying) {
                                        start()
                                    }
                                }
                                setOnCompletionListener {
                                    if (mediaState.repeatMode == PlaybackRepeatMode.REPEAT_ONE) {
                                        seekTo(0)
                                        start()
                                    } else {
                                        onSkipTrack(true)
                                    }
                                }
                            }
                        },
                        update = { vv ->
                            videoViewRef = vv
                            try {
                                if (lastHandledReplayNonce != mediaState.replayNonce) {
                                    lastHandledReplayNonce = mediaState.replayNonce
                                    vv.seekTo(0)
                                    if (mediaState.isPlaying) {
                                        vv.start()
                                    }
                                } else if (lastHandledSeekNonce != mediaState.userSeekNonce) {
                                    lastHandledSeekNonce = mediaState.userSeekNonce
                                    val targetMs = mediaState.currentPositionMs.toInt().coerceAtLeast(0)
                                    vv.seekTo(targetMs)
                                }
                                if (mediaState.isPlaying && !vv.isPlaying) {
                                    vv.start()
                                } else if (!mediaState.isPlaying && vv.isPlaying) {
                                    vv.pause()
                                }
                            } catch (_: Exception) {
                            }
                        },
                        modifier = Modifier.fillMaxSize()
                    )
                }
            } else {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.padding(24.dp)
                ) {
                    NyxMascotAvatar(
                        mood = if (mediaState.isPlaying) MascotMood.HAPPY_VIBES else MascotMood.WAVE_HELLO,
                        size = 84.dp
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "${item.name}.${item.extension}",
                        style = MaterialTheme.typography.titleMedium,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Pemutar Video Layar Penuh • ${if (nativeVideoHeight > nativeVideoWidth) "9:16 Potret" else "16:9 Lanskap"}",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.White.copy(alpha = 0.72f)
                    )
                }
            }
        }

        // 2. Double-Tap ±10s Seek Ripple Feedback Badge
        AnimatedVisibility(
            visible = seekFeedbackText != null,
            enter = fadeIn(tween(120)),
            exit = fadeOut(tween(180)),
            modifier = Modifier
                .align(if (seekFeedbackSide < 0) Alignment.CenterStart else Alignment.CenterEnd)
                .padding(horizontal = 36.dp)
        ) {
            Surface(
                color = Color.Black.copy(alpha = 0.78f),
                shape = CircleShape,
                border = BorderStroke(1.5.dp, Color.White.copy(alpha = 0.4f))
            ) {
                Column(
                    modifier = Modifier.padding(horizontal = 22.dp, vertical = 16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = if (seekFeedbackSide < 0) Icons.Default.Replay10 else Icons.Default.Forward10,
                        contentDescription = seekFeedbackText,
                        tint = Color.White,
                        modifier = Modifier.size(30.dp)
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = seekFeedbackText ?: "",
                        style = MaterialTheme.typography.labelMedium,
                        color = Color.White,
                        fontWeight = FontWeight.ExtraBold
                    )
                }
            }
        }

        // 3. Hold for 2X Speed Pill at Top Center
        AnimatedVisibility(
            visible = mediaState.isHold2xActive,
            enter = fadeIn(tween(120)),
            exit = fadeOut(tween(180)),
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 18.dp)
        ) {
            Surface(
                color = Color.Black.copy(alpha = 0.85f),
                shape = RoundedCornerShape(50),
                border = BorderStroke(1.dp, VaultLime)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 7.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.FastForward,
                        contentDescription = "2X Cepat",
                        tint = VaultLime,
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = "2X CEPAT AKTIF",
                        fontFamily = JetBrainsMonoFontFamily,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White
                    )
                }
            }
        }

        // 3B. Brightness & Volume Vertical Swipe HUD Pill
        AnimatedVisibility(
            visible = gestureHudLabel != null,
            enter = fadeIn(tween(100)),
            exit = fadeOut(tween(180)),
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 56.dp)
        ) {
            Surface(
                color = Color.Black.copy(alpha = 0.85f),
                shape = RoundedCornerShape(50),
                border = BorderStroke(1.5.dp, VaultMintCard)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 18.dp, vertical = 9.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = if (gestureHudIsBrightness) Icons.Default.BrightnessHigh else Icons.Default.VolumeUp,
                        contentDescription = null,
                        tint = VaultLime,
                        modifier = Modifier.size(18.dp)
                    )
                    Text(
                        text = gestureHudLabel ?: "",
                        fontFamily = JetBrainsMonoFontFamily,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White
                    )
                }
            }
        }

        // 4. Fullscreen Cinema OSD Overlay Controls (Auto-hides while watching, tap screen to toggle)
        AnimatedVisibility(
            visible = showOsdControls,
            enter = fadeIn(tween(180)),
            exit = fadeOut(tween(220)),
            modifier = Modifier.fillMaxSize()
        ) {
            if (isScreenLocked) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.25f)),
                    contentAlignment = Alignment.BottomCenter
                ) {
                    Surface(
                        modifier = Modifier
                            .padding(bottom = 28.dp)
                            .clip(RoundedCornerShape(50))
                            .clickable {
                                isScreenLocked = false
                                lastInteractionTick++
                            }
                            .testTag("fullscreen_unlock_button"),
                        color = Color.Black.copy(alpha = 0.84f),
                        border = BorderStroke(1.5.dp, VaultLime)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 18.dp, vertical = 10.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.LockOpen,
                                contentDescription = "Buka Kunci Layar",
                                tint = VaultLime,
                                modifier = Modifier.size(18.dp)
                            )
                            Text(
                                text = "Layar Penuh Terkunci • Ketuk untuk Buka",
                                style = MaterialTheme.typography.labelLarge,
                                color = Color.White,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    Color.Black.copy(alpha = 0.72f),
                                    Color.Black.copy(alpha = 0.12f),
                                    Color.Black.copy(alpha = 0.12f),
                                    Color.Black.copy(alpha = 0.82f)
                                )
                            )
                        )
                ) {
                    // A. TOP CINEMA BAR: Back/Close + Title + Orientation (Locked Landscape / Portrait) + Aspect Ratio + Actions
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.TopCenter)
                            .padding(horizontal = 22.dp, vertical = 12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Surface(
                                modifier = Modifier
                                    .size(38.dp)
                                    .clip(CircleShape)
                                    .clickable { onClose() }
                                    .testTag("viewer_close_button"),
                                color = Color.White.copy(alpha = 0.16f)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                        contentDescription = "Keluar Layar Penuh",
                                        tint = Color.White,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                            }

                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "${item.name}.${item.extension}",
                                    style = MaterialTheme.typography.titleMedium,
                                    color = Color.White,
                                    fontWeight = FontWeight.ExtraBold,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Text(
                                    text = buildString {
                                        val ratioTag = if (nativeVideoHeight > nativeVideoWidth) "9:16 Vertikal" else "16:9 Lanskap"
                                        append("$ratioTag • ${orientationMode.label} • ${formatFileSize(item.sizeBytes)}")
                                    },
                                    fontFamily = JetBrainsMonoFontFamily,
                                    fontSize = 10.sp,
                                    color = VaultLime,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            // 1. Auto-Rotate / 16:9 Landscape / 9:16 Portrait Switcher Pill
                            Surface(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(50))
                                    .clickable {
                                        lastInteractionTick++
                                        orientationMode = when (orientationMode) {
                                            FullscreenOrientationMode.AUTO_SENSOR -> FullscreenOrientationMode.LANDSCAPE_16_9
                                            FullscreenOrientationMode.LANDSCAPE_16_9 -> FullscreenOrientationMode.PORTRAIT_9_16
                                            FullscreenOrientationMode.PORTRAIT_9_16 -> FullscreenOrientationMode.AUTO_SENSOR
                                        }
                                    }
                                    .testTag("fullscreen_orientation_mode_button"),
                                color = Color.White.copy(alpha = 0.18f),
                                border = BorderStroke(1.dp, VaultLime.copy(alpha = 0.7f))
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(5.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.ScreenRotation,
                                        contentDescription = "Mode Rotasi Layar",
                                        tint = VaultLime,
                                        modifier = Modifier.size(14.dp)
                                    )
                                    Text(
                                        text = orientationMode.label,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.ExtraBold,
                                        color = Color.White
                                    )
                                }
                            }

                            // 2. Aspect Ratio Switcher Pill (Asli / 16:9 / 9:16 / Penuh)
                            Surface(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(50))
                                    .clickable {
                                        lastInteractionTick++
                                        val modes = FullscreenAspectRatioMode.entries
                                        aspectMode = modes[(aspectMode.ordinal + 1) % modes.size]
                                    }
                                    .testTag("fullscreen_aspect_ratio_button"),
                                color = Color.White.copy(alpha = 0.18f),
                                border = BorderStroke(1.dp, Color.White.copy(alpha = 0.35f))
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.AspectRatio,
                                        contentDescription = "Rasio Layar",
                                        tint = Color.White,
                                        modifier = Modifier.size(14.dp)
                                    )
                                    Text(
                                        text = aspectMode.label,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.ExtraBold,
                                        color = Color.White
                                    )
                                }
                            }

                            // 3. Share, Export & Lock Buttons (Preserving existing testTags)
                            Surface(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(50))
                                    .instantTouchPress { onRequestExportOrShareWithBiometric(true) }
                                    .testTag("viewer_share_biometric_button"),
                                color = VaultLime
                            ) {
                                Text(
                                    text = "Bagikan",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = VaultObsidian,
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                                )
                            }

                            Surface(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(50))
                                    .instantTouchPress { onRequestExportOrShareWithBiometric(false) }
                                    .testTag("viewer_export_biometric_button"),
                                color = VaultMintCard
                            ) {
                                Text(
                                    text = "Ekspor",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = VaultObsidian,
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                                )
                            }

                            Surface(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(50))
                                    .clickable { onRequestReEncryptWithPin() }
                                    .testTag("viewer_lock_again_button"),
                                color = Color.White.copy(alpha = 0.16f),
                                border = BorderStroke(1.dp, VaultLime.copy(alpha = 0.6f))
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 9.dp, vertical = 6.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Lock,
                                        contentDescription = "Kunci AES",
                                        tint = VaultLime,
                                        modifier = Modifier.size(13.dp)
                                    )
                                    Text(
                                        text = "Kunci",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = VaultLime
                                    )
                                }
                            }
                        }
                    }

                    // B. CENTER TRANSPORT CONTROLS: Prev Track, -10s, Play/Pause, +10s, Next Track
                    Row(
                        modifier = Modifier.align(Alignment.Center),
                        horizontalArrangement = Arrangement.spacedBy(20.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(
                            onClick = {
                                lastInteractionTick++
                                onSkipTrack(false)
                            },
                            modifier = Modifier
                                .size(44.dp)
                                .clip(CircleShape)
                                .background(Color.Black.copy(alpha = 0.52f))
                        ) {
                            Icon(
                                imageVector = Icons.Default.SkipPrevious,
                                contentDescription = "Video Sebelumnya",
                                tint = Color.White,
                                modifier = Modifier.size(24.dp)
                            )
                        }

                        IconButton(
                            onClick = {
                                lastInteractionTick++
                                onSeekSeconds((mediaState.currentSeconds - 10).coerceAtLeast(0))
                            },
                            modifier = Modifier
                                .size(48.dp)
                                .clip(CircleShape)
                                .background(Color.Black.copy(alpha = 0.52f))
                        ) {
                            Icon(
                                imageVector = Icons.Default.Replay10,
                                contentDescription = "Mundur 10 detik",
                                tint = Color.White,
                                modifier = Modifier.size(26.dp)
                            )
                        }

                        Surface(
                            modifier = Modifier
                                .size(68.dp)
                                .clip(CircleShape)
                                .clickable {
                                    lastInteractionTick++
                                    onTogglePlayPause()
                                }
                                .testTag("video_viewer_play_pause"),
                            color = youtubeRed,
                            border = BorderStroke(2.dp, Color.White.copy(alpha = 0.45f))
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = if (mediaState.isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                                    contentDescription = "Putar / Jeda Video",
                                    tint = Color.White,
                                    modifier = Modifier.size(36.dp)
                                )
                            }
                        }

                        IconButton(
                            onClick = {
                                lastInteractionTick++
                                onSeekSeconds((mediaState.currentSeconds + 10).coerceAtMost(mediaState.totalSeconds.coerceAtLeast(1)))
                            },
                            modifier = Modifier
                                .size(48.dp)
                                .clip(CircleShape)
                                .background(Color.Black.copy(alpha = 0.52f))
                        ) {
                            Icon(
                                imageVector = Icons.Default.Forward10,
                                contentDescription = "Maju 10 detik",
                                tint = Color.White,
                                modifier = Modifier.size(26.dp)
                            )
                        }

                        IconButton(
                            onClick = {
                                lastInteractionTick++
                                onSkipTrack(true)
                            },
                            modifier = Modifier
                                .size(44.dp)
                                .clip(CircleShape)
                                .background(Color.Black.copy(alpha = 0.52f))
                        ) {
                            Icon(
                                imageVector = Icons.Default.SkipNext,
                                contentDescription = "Video Berikutnya",
                                tint = Color.White,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }

                    // C. BOTTOM CINEMA SCRUBBER & QUICK CONTROLS (400% Volume Booster, Time, Slider, Speed, Lock Screen, Quick 16:9 / 9:16, Exit Fullscreen)
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.BottomCenter)
                            .padding(horizontal = 22.dp, vertical = 12.dp)
                    ) {
                        // 400% Audio Booster Bar (Exclusive to Video Player)
                        Surface(
                            color = Color.Black.copy(alpha = 0.65f),
                            shape = RoundedCornerShape(14.dp),
                            border = BorderStroke(
                                1.dp,
                                if (audioVolumePercent > 100) VaultLime else Color.White.copy(alpha = 0.25f)
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 6.dp)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 12.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.VolumeUp,
                                    contentDescription = "Pengeras Suara 400%",
                                    tint = if (audioVolumePercent > 100) VaultLime else Color.White,
                                    modifier = Modifier.size(16.dp)
                                )
                                Text(
                                    text = "BOOSTER ${audioVolumePercent}%",
                                    fontFamily = JetBrainsMonoFontFamily,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = if (audioVolumePercent > 100) VaultLime else Color.White,
                                    maxLines = 1,
                                    softWrap = false
                                )
                                Slider(
                                    value = audioVolumePercent.toFloat(),
                                    onValueChange = { newVal ->
                                        lastInteractionTick++
                                        applyVideoVolumePercent(newVal.toInt())
                                    },
                                    valueRange = 0f..400f,
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(24.dp)
                                        .testTag("fullscreen_volume_booster_slider"),
                                    colors = SliderDefaults.colors(
                                        thumbColor = if (audioVolumePercent > 100) VaultLime else Color.White,
                                        activeTrackColor = if (audioVolumePercent > 100) VaultLime else youtubeRed,
                                        inactiveTrackColor = Color.White.copy(alpha = 0.25f)
                                    )
                                )
                                listOf(100, 200, 300, 400).forEach { presetPct ->
                                    val selected = audioVolumePercent == presetPct
                                    Surface(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(50))
                                            .clickable {
                                                lastInteractionTick++
                                                applyVideoVolumePercent(presetPct)
                                            }
                                            .testTag("fullscreen_booster_${presetPct}_chip"),
                                        color = if (selected) VaultLime else Color.White.copy(alpha = 0.16f)
                                    ) {
                                        Text(
                                            text = if (presetPct == 400) "400% MAX" else "$presetPct%",
                                            fontFamily = JetBrainsMonoFontFamily,
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.ExtraBold,
                                            color = if (selected) VaultObsidian else Color.White,
                                            maxLines = 1,
                                            softWrap = false,
                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                        )
                                    }
                                }
                            }
                        }

                        Slider(
                            value = mediaState.currentPositionMs.toFloat().coerceIn(
                                0f,
                                mediaState.totalDurationMs.coerceAtLeast(500L).toFloat()
                            ),
                            onValueChange = { newMs ->
                                lastInteractionTick++
                                onSeekPositionMs(newMs.toLong())
                            },
                            valueRange = 0f..mediaState.totalDurationMs.coerceAtLeast(500L).toFloat(),
                            colors = SliderDefaults.colors(
                                thumbColor = youtubeRed,
                                activeTrackColor = youtubeRed,
                                inactiveTrackColor = Color.White.copy(alpha = 0.30f)
                            )
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Live Time / Total Time
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Text(
                                    text = formatSeconds(mediaState.currentSeconds),
                                    fontFamily = JetBrainsMonoFontFamily,
                                    fontSize = 13.sp,
                                    color = youtubeRed,
                                    fontWeight = FontWeight.ExtraBold
                                )
                                Text(
                                    text = "/",
                                    fontFamily = JetBrainsMonoFontFamily,
                                    fontSize = 12.sp,
                                    color = Color.White.copy(alpha = 0.6f)
                                )
                                Text(
                                    text = formatSeconds(mediaState.totalSeconds),
                                    fontFamily = JetBrainsMonoFontFamily,
                                    fontSize = 13.sp,
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            // Right Bottom Cinema Controls: 16:9 Quick Button, 9:16 Quick Button, Speed, Screen Lock, Exit Fullscreen
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                // Direct 16:9 Mode Chip
                                Surface(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(50))
                                        .clickable {
                                            lastInteractionTick++
                                            orientationMode = FullscreenOrientationMode.LANDSCAPE_16_9
                                            aspectMode = FullscreenAspectRatioMode.WIDESCREEN_16_9
                                        }
                                        .testTag("fullscreen_quick_16_9_button"),
                                    color = if (orientationMode == FullscreenOrientationMode.LANDSCAPE_16_9) {
                                        youtubeRed
                                    } else {
                                        Color.White.copy(alpha = 0.16f)
                                    }
                                ) {
                                    Text(
                                        text = "16:9",
                                        fontFamily = JetBrainsMonoFontFamily,
                                        fontSize = 11.sp,
                                        color = Color.White,
                                        fontWeight = FontWeight.ExtraBold,
                                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                                    )
                                }

                                // Direct 9:16 Mode Chip
                                Surface(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(50))
                                        .clickable {
                                            lastInteractionTick++
                                            orientationMode = FullscreenOrientationMode.PORTRAIT_9_16
                                            aspectMode = FullscreenAspectRatioMode.VERTICAL_9_16
                                        }
                                        .testTag("fullscreen_quick_9_16_button"),
                                    color = if (orientationMode == FullscreenOrientationMode.PORTRAIT_9_16) {
                                        youtubeRed
                                    } else {
                                        Color.White.copy(alpha = 0.16f)
                                    }
                                ) {
                                    Text(
                                        text = "9:16",
                                        fontFamily = JetBrainsMonoFontFamily,
                                        fontSize = 11.sp,
                                        color = Color.White,
                                        fontWeight = FontWeight.ExtraBold,
                                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                                    )
                                }

                                // Speed Selector Pill
                                Surface(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(50))
                                        .clickable {
                                            lastInteractionTick++
                                            onCycleSpeed()
                                        },
                                    color = Color.White.copy(alpha = 0.16f)
                                ) {
                                    Text(
                                        text = "${mediaState.playbackSpeed}x",
                                        fontFamily = JetBrainsMonoFontFamily,
                                        fontSize = 11.sp,
                                        color = Color.White,
                                        fontWeight = FontWeight.ExtraBold,
                                        modifier = Modifier.padding(horizontal = 11.dp, vertical = 6.dp)
                                    )
                                }

                                // Lock Screen Touch Button
                                IconButton(
                                    onClick = { isScreenLocked = true },
                                    modifier = Modifier.size(34.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Lock,
                                        contentDescription = "Kunci Sentuhan Layar",
                                        tint = Color.White,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }

                                // Exit Fullscreen Button
                                IconButton(
                                    onClick = onClose,
                                    modifier = Modifier
                                        .size(36.dp)
                                        .clip(CircleShape)
                                        .background(Color.White.copy(alpha = 0.18f))
                                        .testTag("fullscreen_exit_button")
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.FullscreenExit,
                                        contentDescription = "Keluar Layar Penuh",
                                        tint = Color.White,
                                        modifier = Modifier.size(22.dp)
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

@Composable
private fun InteractiveImageAndStickerViewport(
    item: VaultItemEntity,
    photoGalleryItems: List<VaultItemEntity> = emptyList(),
    onSelectPhotoFromPager: (VaultItemEntity) -> Unit = {},
    onRequestShare: () -> Unit = {},
    onRequestExport: () -> Unit = {},
    onRequestLock: () -> Unit = {},
    onDeletePhoto: (VaultItemEntity) -> Unit = {}
) {
    val pagerPhotos = remember(photoGalleryItems, item.id) {
        val unlocked = photoGalleryItems.filter { !it.isEncrypted }
        if (unlocked.any { it.id == item.id }) {
            unlocked
        } else {
            listOf(item) + unlocked.filter { it.id != item.id }
        }
    }
    val initialPageIndex = remember(pagerPhotos, item.id) {
        pagerPhotos.indexOfFirst { it.id == item.id }.coerceAtLeast(0)
    }

    val pagerState = rememberPagerState(
        initialPage = initialPageIndex,
        pageCount = { pagerPhotos.size.coerceAtLeast(1) }
    )
    val coroutineScope = rememberCoroutineScope()

    var scale by remember(pagerState.currentPage) { mutableFloatStateOf(1f) }
    var offsetX by remember(pagerState.currentPage) { mutableFloatStateOf(0f) }
    var offsetY by remember(pagerState.currentPage) { mutableFloatStateOf(0f) }
    var showExifInfoCard by remember { mutableStateOf(false) }

    val currentPhoto = pagerPhotos.getOrNull(pagerState.currentPage) ?: item

    LaunchedEffect(pagerState.currentPage) {
        val pageItem = pagerPhotos.getOrNull(pagerState.currentPage)
        if (pageItem != null && pageItem.id != item.id) {
            onSelectPhotoFromPager(pageItem)
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        // 1. GOOGLE PHOTOS HORIZONTAL SWIPE VIEWPORT + PINCH-TO-ZOOM
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF0B0F19)),
            border = androidx.compose.foundation.BorderStroke(1.dp, VaultSlate.copy(alpha = 0.5f))
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                HorizontalPager(
                    state = pagerState,
                    userScrollEnabled = scale <= 1.05f,
                    modifier = Modifier
                        .fillMaxSize()
                        .testTag("google_photos_horizontal_pager")
                ) { pageIndex ->
                    val pagePhoto = pagerPhotos.getOrNull(pageIndex) ?: item
                    val plainFile = remember(pagePhoto.internalPlainPath) { File(pagePhoto.internalPlainPath) }
                    val isCurrentPage = pageIndex == pagerState.currentPage

                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .pointerInput(pagePhoto.id) {
                                detectTapGestures(
                                    onDoubleTap = {
                                        if (scale > 1.05f) {
                                            scale = 1f
                                            offsetX = 0f
                                            offsetY = 0f
                                        } else {
                                            scale = 2.4f
                                        }
                                    }
                                )
                            }
                            .then(
                                if (scale > 1.05f) {
                                    Modifier.pointerInput(pagePhoto.id, scale) {
                                        detectTransformGestures { _, pan, zoom, _ ->
                                            scale = (scale * zoom).coerceIn(1f, 4f)
                                            if (scale > 1.05f) {
                                                offsetX += pan.x
                                                offsetY += pan.y
                                            } else {
                                                offsetX = 0f
                                                offsetY = 0f
                                            }
                                        }
                                    }
                                } else {
                                    Modifier
                                }
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        val modelData: Any? = when {
                            plainFile.exists() -> plainFile
                            !pagePhoto.mediaUriString.isNullOrBlank() -> Uri.parse(pagePhoto.mediaUriString)
                            else -> null
                        }

                        if (modelData != null) {
                            AsyncImage(
                                model = modelData,
                                contentDescription = pagePhoto.name,
                                contentScale = ContentScale.Fit,
                                modifier = Modifier
                                    .fillMaxSize()
                                    .graphicsLayer(
                                        scaleX = if (isCurrentPage) scale else 1f,
                                        scaleY = if (isCurrentPage) scale else 1f,
                                        translationX = if (isCurrentPage) offsetX else 0f,
                                        translationY = if (isCurrentPage) offsetY else 0f
                                    )
                            )
                        }
                    }
                }

                // Top-Left Page Counter & Zoom Pill
                Surface(
                    color = Color.Black.copy(alpha = 0.72f),
                    shape = RoundedCornerShape(50),
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(12.dp)
                ) {
                    Text(
                        text = "${pagerState.currentPage + 1} / ${pagerPhotos.size} • ${currentPhoto.extension.uppercase()} • ${String.format(java.util.Locale.US, "%.1fx", scale)}",
                        fontFamily = JetBrainsMonoFontFamily,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = VaultLime,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 5.dp)
                    )
                }

                // Previous / Next Floating Navigation Buttons (For Quick Tap or Swipe)
                if (pagerPhotos.size > 1) {
                    if (pagerState.currentPage > 0) {
                        IconButton(
                            onClick = {
                                scale = 1f
                                offsetX = 0f
                                offsetY = 0f
                                coroutineScope.launch {
                                    pagerState.animateScrollToPage(pagerState.currentPage - 1)
                                }
                            },
                            modifier = Modifier
                                .align(Alignment.CenterStart)
                                .padding(start = 8.dp)
                                .size(38.dp)
                                .clip(CircleShape)
                                .background(Color.Black.copy(alpha = 0.55f))
                                .testTag("photo_pager_prev_button")
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                                contentDescription = "Foto Sebelumnya",
                                tint = Color.White
                            )
                        }
                    }
                    if (pagerState.currentPage < pagerPhotos.lastIndex) {
                        IconButton(
                            onClick = {
                                scale = 1f
                                offsetX = 0f
                                offsetY = 0f
                                coroutineScope.launch {
                                    pagerState.animateScrollToPage(pagerState.currentPage + 1)
                                }
                            },
                            modifier = Modifier
                                .align(Alignment.CenterEnd)
                                .padding(end = 8.dp)
                                .size(38.dp)
                                .clip(CircleShape)
                                .background(Color.Black.copy(alpha = 0.55f))
                                .testTag("photo_pager_next_button")
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                                contentDescription = "Foto Berikutnya",
                                tint = Color.White
                            )
                        }
                    }
                }

                // Optional EXIF / Photo Info Floating Card
                if (showExifInfoCard) {
                    Surface(
                        color = Color.Black.copy(alpha = 0.86f),
                        shape = RoundedCornerShape(16.dp),
                        border = BorderStroke(1.dp, VaultLime.copy(alpha = 0.5f)),
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .padding(12.dp)
                            .fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier.padding(12.dp),
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text(
                                text = "${currentPhoto.name}.${currentPhoto.extension}",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.ExtraBold,
                                color = Color.White
                            )
                            val formattedAddedDate = remember(currentPhoto.addedAt) {
                                if (currentPhoto.addedAt > 0L) {
                                    java.text.SimpleDateFormat("dd MMM yyyy, HH:mm", java.util.Locale("id", "ID"))
                                        .format(java.util.Date(currentPhoto.addedAt))
                                } else {
                                    "Baru saja"
                                }
                            }
                            Text(
                                text = "Ukuran: ${formatFileSize(currentPhoto.sizeBytes)} • Album: ${currentPhoto.groupArchiveName?.ifBlank { "Utama" } ?: "Utama"} • Ditambahkan: $formattedAddedDate",
                                style = MaterialTheme.typography.bodySmall,
                                color = VaultMintCard
                            )
                            Text(
                                text = "Geser kiri/kanan untuk pindah foto • Ketuk 2x atau tekan tombol Zoom untuk memperbesar",
                                style = MaterialTheme.typography.labelSmall,
                                color = Color.White.copy(alpha = 0.7f)
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // 2. BOTTOM THUMBNAIL STRIP (GOOGLE PHOTOS FILMSTRIP)
        if (pagerPhotos.size > 1) {
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp)
                    .testTag("photo_pager_thumbnail_strip"),
                horizontalArrangement = Arrangement.spacedBy(6.dp, Alignment.CenterHorizontally),
                verticalAlignment = Alignment.CenterVertically
            ) {
                itemsIndexed(pagerPhotos, key = { _, p -> p.id }) { idx, thumbPhoto ->
                    val isCurrent = idx == pagerState.currentPage
                    val thumbFile = remember(thumbPhoto.internalPlainPath) { File(thumbPhoto.internalPlainPath) }
                    val thumbModel: Any? = when {
                        thumbFile.exists() -> thumbFile
                        !thumbPhoto.mediaUriString.isNullOrBlank() -> Uri.parse(thumbPhoto.mediaUriString)
                        else -> null
                    }
                    Box(
                        modifier = Modifier
                            .size(if (isCurrent) 50.dp else 42.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .border(
                                width = if (isCurrent) 2.5.dp else 1.dp,
                                color = if (isCurrent) VaultLime else Color.White.copy(alpha = 0.3f),
                                shape = RoundedCornerShape(8.dp)
                            )
                            .clickable {
                                scale = 1f
                                offsetX = 0f
                                offsetY = 0f
                                coroutineScope.launch {
                                    pagerState.animateScrollToPage(idx)
                                }
                            }
                    ) {
                        if (thumbModel != null) {
                            AsyncImage(
                                model = thumbModel,
                                contentDescription = thumbPhoto.name,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize()
                            )
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
        }

        // 3. GOOGLE PHOTOS BOTTOM QUICK ACTION BAR
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = VaultCharcoal)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 10.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Share
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .clickable { onRequestShare() }
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Icon(Icons.Default.Share, contentDescription = "Bagikan", tint = VaultPureWhite, modifier = Modifier.size(20.dp))
                    Text("Bagikan", style = MaterialTheme.typography.labelSmall, color = VaultPureWhite)
                }

                // Zoom Toggle (1x / 2.2x)
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .clickable {
                            scale = if (scale < 2f) 2.2f else 1f
                            offsetX = 0f
                            offsetY = 0f
                        }
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                        .testTag("image_viewer_zoom_toggle")
                ) {
                    Icon(
                        imageVector = if (scale > 1f) Icons.Default.ZoomOutMap else Icons.Default.ZoomIn,
                        contentDescription = "Zoom",
                        tint = VaultLime,
                        modifier = Modifier.size(20.dp)
                    )
                    Text(
                        text = if (scale > 1f) "Reset 1x" else "Zoom 2x",
                        style = MaterialTheme.typography.labelSmall,
                        color = VaultLime
                    )
                }

                // Info EXIF
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .clickable { showExifInfoCard = !showExifInfoCard }
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                        .testTag("image_viewer_info_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = "Info Foto",
                        tint = if (showExifInfoCard) VaultLime else VaultPureWhite,
                        modifier = Modifier.size(20.dp)
                    )
                    Text("Info", style = MaterialTheme.typography.labelSmall, color = VaultPureWhite)
                }

                // Lock NYX2
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .clickable { onRequestLock() }
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Icon(Icons.Default.Lock, contentDescription = "Kunci", tint = VaultAmber, modifier = Modifier.size(20.dp))
                    Text("Kunci", style = MaterialTheme.typography.labelSmall, color = VaultAmber)
                }

                // Delete
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .clickable { onDeletePhoto(currentPhoto) }
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                        .testTag("image_viewer_delete_button")
                ) {
                    Icon(Icons.Default.Delete, contentDescription = "Hapus", tint = VaultCoral, modifier = Modifier.size(20.dp))
                    Text("Hapus", style = MaterialTheme.typography.labelSmall, color = VaultCoral)
                }
            }
        }
    }
}

@Composable
private fun DecryptedDocumentReaderViewport(item: VaultItemEntity) {
    ArcanumUniversalDocumentViewport(
        item = item,
        modifier = Modifier.fillMaxSize()
    )
}

