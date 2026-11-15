package com.example.ui

import android.net.Uri
import android.widget.VideoView
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AudioFile
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.CloudDownload
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Fullscreen
import androidx.compose.material.icons.filled.HighQuality
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.VideoFile
import androidx.compose.material.icons.filled.VolumeOff
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import coil.compose.AsyncImage
import com.example.data.MoriDownloadResult
import com.example.data.MoriPlatform
import com.example.data.MoriResolvedMetadata
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
import com.example.ui.theme.DuoMutedInk
import com.example.ui.theme.DuoPureWhite
import com.example.ui.theme.DuoSnowCanvas
import com.example.ui.theme.DuoSwanBorder
import com.example.ui.theme.duolingoButton
import com.example.ui.theme.duolingoCard
import kotlinx.coroutines.delay
import java.io.File
import kotlin.math.roundToInt

// =========================================================================
// PALET REAKTIF 6-TEMA UNTUK MORI DOWNLOADER
// =========================================================================
val MoriCreamBg: Color
    get() = DuoSnowCanvas
val MoriCardSurface: Color
    get() = com.example.ui.theme.CosmicObsidian
val MoriSoftRowBg: Color
    get() = com.example.ui.theme.DuoMintSurface
val MoriInkBlack: Color
    get() = DuoEelInk
val MoriMutedGray: Color
    get() = DuoMutedInk
val MoriBorderSubtle: Color
    get() = DuoSwanBorder
val MoriReadyMintBg: Color
    get() = com.example.ui.theme.DuoFeatherGreenSoft
val MoriReadyMintText: Color
    get() = DuoFeatherGreenLip
val MoriCancelRose: Color
    get() = DuoCardinalRed

data class MoriSupportedPlatformSpec(
    val title: String,
    val platform: MoriPlatform,
    val sampleUrl: String,
    val iconCode: Int,
    val badgeColor: Color,
    val badgeLipColor: Color
)

val MORI_SUPPORTED_PLATFORMS_GRID: List<MoriSupportedPlatformSpec>
    get() = listOf(
        MoriSupportedPlatformSpec("TIKTOK", MoriPlatform.TIKTOK, "https://www.tiktok.com/@diburen/video/7106594312292453675", 0, DuoMacawBlue, DuoMacawBlueLip),
        MoriSupportedPlatformSpec("INSTAGRAM", MoriPlatform.INSTAGRAM, "https://www.instagram.com/p/C9mori_carousel_slides/", 1, DuoBeetlePurple, DuoBeetlePurpleLip),
        MoriSupportedPlatformSpec("YOUTUBE", MoriPlatform.YOUTUBE, "https://www.youtube.com/watch?v=aqz-KE-bpKQ", 2, DuoCardinalRed, DuoCardinalRedLip),
        MoriSupportedPlatformSpec("TWITTER", MoriPlatform.TWITTER_X, "https://x.com/NASA/status/1777355895781613691", 3, DuoMacawBlue, DuoMacawBlueLip),
        MoriSupportedPlatformSpec("SPOTIFY", MoriPlatform.SPOTIFY, "https://open.spotify.com/track/4cOdK2wGLETKBW3PvgPWqT", 4, DuoFeatherGreen, DuoFeatherGreenLip),
        MoriSupportedPlatformSpec("BILIBILI", MoriPlatform.BILIBILI, "https://www.bilibili.tv/en/video/204958192", 5, DuoMacawBlue, DuoMacawBlueLip),
        MoriSupportedPlatformSpec("DOUYIN", MoriPlatform.DOUYIN, "https://www.douyin.com/video/7291847192837", 0, DuoFoxOrange, DuoFoxOrangeLip),
        MoriSupportedPlatformSpec("REDNOTE", MoriPlatform.REDNOTE, "https://www.xiaohongshu.com/explore/66918273", 6, DuoCardinalRed, DuoCardinalRedLip),
        MoriSupportedPlatformSpec("THREADS", MoriPlatform.THREADS, "https://www.threads.net/@mori/post/C8mori123", 7, DuoBeetlePurple, DuoBeetlePurpleLip),
        MoriSupportedPlatformSpec("PINTEREST", MoriPlatform.PINTEREST, "https://www.pinterest.com/pin/58194958192/", 8, DuoCardinalRed, DuoCardinalRedLip),
        MoriSupportedPlatformSpec("APPLE MUSIC", MoriPlatform.APPLE_MUSIC, "https://music.apple.com/us/album/mori-ost/1728391", 9, DuoCardinalRed, DuoCardinalRedLip),
        MoriSupportedPlatformSpec("FACEBOOK", MoriPlatform.FACEBOOK, "https://www.facebook.com/watch/?v=918273645", 10, DuoMacawBlue, DuoMacawBlueLip),
        MoriSupportedPlatformSpec("BANDCAMP", MoriPlatform.BANDCAMP, "https://bandcamp.com/track/mori-synthwave", 11, DuoMacawBlue, DuoMacawBlueLip),
        MoriSupportedPlatformSpec("PIXIV", MoriPlatform.PIXIV, "https://www.pixiv.net/en/artworks/118293847", 12, DuoMacawBlue, DuoMacawBlueLip),
        MoriSupportedPlatformSpec("MEDIAFIRE", MoriPlatform.MEDIAFIRE, "https://www.mediafire.com/folder/mori451bundle/Arcanum_Multi_Files", 13, DuoFeatherGreen, DuoFeatherGreenLip),
        MoriSupportedPlatformSpec("REDDIT", MoriPlatform.REDDIT, "https://www.reddit.com/r/anime/comments/mori451", 4, DuoFoxOrange, DuoFoxOrangeLip),
        MoriSupportedPlatformSpec("GOOGLE DRIVE", MoriPlatform.GOOGLE_DRIVE, "https://drive.google.com/drive/folders/1MoriArcanumPack", 13, DuoFeatherGreen, DuoFeatherGreenLip),
        MoriSupportedPlatformSpec("PIXELDRAIN", MoriPlatform.PIXELDRAIN, "https://pixeldrain.com/l/mori451album", 12, DuoBeetlePurple, DuoBeetlePurpleLip),
        MoriSupportedPlatformSpec("TERABOX", MoriPlatform.TERABOX, "https://www.terabox.com/s/1moriCloudBundle", 13, DuoMacawBlue, DuoMacawBlueLip),
        MoriSupportedPlatformSpec("CATBOX", MoriPlatform.CATBOX, "https://files.catbox.moe/mori_ost_pack.mp4", 4, DuoCardinalRed, DuoCardinalRedLip),
        MoriSupportedPlatformSpec("KRAKENFILES", MoriPlatform.KRAKENFILES, "https://krakenfiles.com/view/mori451/file.html", 5, DuoMacawBlue, DuoMacawBlueLip),
        MoriSupportedPlatformSpec("SFILE.MOBI", MoriPlatform.SFILE, "https://sfile.mobi/moriArcanumPack", 13, DuoFoxOrange, DuoFoxOrangeLip),
        MoriSupportedPlatformSpec("MEGA CLOUD", MoriPlatform.MEGA, "https://mega.nz/folder/mori451#arcanum", 13, DuoCardinalRed, DuoCardinalRedLip)
    )

// =========================================================================
// 1. MODAL "NEW VERSION AVAILABLE! v4.5.1" (DUOLINGO 3D THEME)
// =========================================================================
@Composable
fun MoriNewVersionAvailableModal(
    versionName: String = "v4.5.1",
    dontShowAgainChecked: Boolean,
    onToggleDontShowAgain: (Boolean) -> Unit,
    onUpdateClick: () -> Unit,
    onDismiss: () -> Unit
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.55f))
                .padding(horizontal = 20.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .duolingoCard(
                        backgroundColor = DuoPureWhite,
                        borderColor = DuoSwanBorder,
                        lipColor = DuoHareLip,
                        cornerRadius = 24.dp,
                        borderWidth = 2.dp,
                        lipHeight = 6.dp
                    )
                    .padding(bottom = 6.dp)
                    .testTag("mori_new_version_modal")
            ) {
                // Header
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "NEW VERSION AVAILABLE!",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = 1.sp,
                        color = DuoEelInk,
                        modifier = Modifier.weight(1f)
                    )
                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier.size(30.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close",
                            tint = DuoMutedInk
                        )
                    }
                }

                HorizontalDivider(thickness = 2.dp, color = DuoSwanBorder)

                // Body
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp, vertical = 24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Custom Canvas Party Popper
                    Canvas(modifier = Modifier.size(60.dp)) {
                        val w = size.width
                        val h = size.height
                        val cone = Path().apply {
                            moveTo(w * 0.14f, h * 0.86f)
                            lineTo(w * 0.42f, h * 0.36f)
                            lineTo(w * 0.66f, h * 0.60f)
                            close()
                        }
                        drawPath(path = cone, color = DuoBeeYellow)
                        drawPath(path = cone, color = DuoFoxOrangeLip, style = Stroke(width = 3.5f))

                        drawCircle(color = DuoBeetlePurple, radius = 6f, center = Offset(w * 0.62f, h * 0.22f))
                        drawCircle(color = DuoMacawBlue, radius = 6f, center = Offset(w * 0.82f, h * 0.42f))
                        drawCircle(color = DuoFeatherGreen, radius = 5.5f, center = Offset(w * 0.35f, h * 0.18f))
                        drawCircle(color = DuoCardinalRed, radius = 6f, center = Offset(w * 0.76f, h * 0.72f))
                        drawLine(
                            color = DuoMacawBlue,
                            start = Offset(w * 0.54f, h * 0.45f),
                            end = Offset(w * 0.82f, h * 0.25f),
                            strokeWidth = 5.5f,
                            cap = StrokeCap.Round
                        )
                        drawLine(
                            color = DuoFeatherGreen,
                            start = Offset(w * 0.48f, h * 0.38f),
                            end = Offset(w * 0.56f, h * 0.14f),
                            strokeWidth = 5.5f,
                            cap = StrokeCap.Round
                        )
                    }

                    Text(
                        text = "New version available! $versionName",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = DuoEelInk,
                        textAlign = TextAlign.Center
                    )

                    // Duolingo 3D Green UPDATE Button
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .duolingoButton(
                                faceColor = DuoFeatherGreen,
                                lipColor = DuoFeatherGreenLip,
                                cornerRadius = 16.dp,
                                lipHeight = 4.dp
                            )
                            .clickable {
                                onUpdateClick()
                                onDismiss()
                            }
                            .padding(bottom = 4.dp)
                            .testTag("mori_modal_update_confirm"),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "UPDATE NOW",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.ExtraBold,
                            letterSpacing = 1.sp,
                            color = Color.White
                        )
                    }
                }

                HorizontalDivider(
                    thickness = 2.dp,
                    color = DuoSwanBorder,
                    modifier = Modifier.padding(horizontal = 20.dp)
                )

                // Footer Checkbox
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onToggleDontShowAgain(!dontShowAgainChecked) }
                        .padding(horizontal = 16.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = dontShowAgainChecked,
                        onCheckedChange = onToggleDontShowAgain,
                        colors = CheckboxDefaults.colors(
                            checkedColor = DuoFeatherGreen,
                            uncheckedColor = DuoMutedInk
                        )
                    )
                    Text(
                        text = "Don't show again",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = DuoEelInk
                    )
                }
            }
        }
    }
}

// =========================================================================
// 2. MODAL "CHOOSE SERVER:" (DUOLINGO 3D BUTTONS)
// =========================================================================
@Composable
fun MoriChooseServerModal(
    onSelectServer1: () -> Unit,
    onSelectServer2: () -> Unit,
    onDismiss: () -> Unit
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.52f))
                .padding(horizontal = 22.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .duolingoCard(
                        backgroundColor = DuoPureWhite,
                        borderColor = DuoSwanBorder,
                        lipColor = DuoHareLip,
                        cornerRadius = 24.dp,
                        borderWidth = 2.dp,
                        lipHeight = 6.dp
                    )
                    .padding(22.dp)
                    .padding(bottom = 6.dp)
                    .testTag("mori_choose_server_modal"),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "CHOOSE SERVER:",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = 1.4.sp,
                    color = DuoEelInk
                )
                Text(
                    text = "Server 1: TikTokIO (HD Video · MP3 · Photo Slideshow)\nServer 2: TikDownloader (Full HD 1080p · MP3 · Photo Slideshow)",
                    fontSize = 13.sp,
                    lineHeight = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = DuoMutedInk,
                    textAlign = TextAlign.Center
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(50.dp)
                            .duolingoButton(
                                faceColor = DuoMacawBlue,
                                lipColor = DuoMacawBlueLip,
                                cornerRadius = 16.dp,
                                lipHeight = 4.dp
                            )
                            .clickable { onSelectServer2() }
                            .padding(bottom = 4.dp)
                            .testTag("mori_modal_server_2_button"),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "SERVER 2",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.ExtraBold,
                            letterSpacing = 1.sp,
                            color = Color.White
                        )
                    }

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(50.dp)
                            .duolingoButton(
                                faceColor = DuoFeatherGreen,
                                lipColor = DuoFeatherGreenLip,
                                cornerRadius = 16.dp,
                                lipHeight = 4.dp
                            )
                            .clickable { onSelectServer1() }
                            .padding(bottom = 4.dp)
                            .testTag("mori_modal_server_1_button"),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "SERVER 1",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.ExtraBold,
                            letterSpacing = 1.sp,
                            color = Color.White
                        )
                    }
                }
            }
        }
    }
}

// =========================================================================
// 3. MODAL "BATCH DOWNLOAD QUEUE" (DUOLINGO 3D THEME)
// =========================================================================
@Composable
fun MoriBatchDownloadQueueModal(
    rawBatchUrls: List<String>,
    batchQueueItems: List<MoriBatchQueueItem>,
    isAnalyzing: Boolean,
    onSelectReadyItem: (String) -> Unit,
    onStartAllBatchDownload: () -> Unit,
    onClose: () -> Unit
) {
    val displayLinks = remember(rawBatchUrls, batchQueueItems) {
        if (batchQueueItems.isNotEmpty()) {
            batchQueueItems.map { it.url }
        } else {
            rawBatchUrls
        }
    }
    val totalCount = displayLinks.size.coerceAtLeast(1)
    val readyCount = if (batchQueueItems.isNotEmpty()) {
        batchQueueItems.count { it.status == "READY" || it.status == "DONE" }
    } else {
        if (isAnalyzing) 0 else totalCount
    }

    Dialog(
        onDismissRequest = onClose,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.52f))
                .padding(horizontal = 20.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .duolingoCard(
                        backgroundColor = DuoPureWhite,
                        borderColor = DuoSwanBorder,
                        lipColor = DuoHareLip,
                        cornerRadius = 24.dp,
                        borderWidth = 2.dp,
                        lipHeight = 6.dp
                    )
                    .padding(22.dp)
                    .padding(bottom = 6.dp)
                    .testTag("mori_batch_queue_modal"),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // Header: BATCH DOWNLOAD QUEUE + Ready/Total Pill
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "BATCH DOWNLOAD\nQUEUE",
                        fontSize = 17.sp,
                        lineHeight = 22.sp,
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = 1.sp,
                        color = DuoEelInk,
                        modifier = Modifier.weight(1f)
                    )

                    Box(
                        modifier = Modifier
                            .duolingoButton(
                                faceColor = DuoMacawBlue,
                                lipColor = DuoMacawBlueLip,
                                cornerRadius = 12.dp,
                                lipHeight = 3.dp
                            )
                            .padding(horizontal = 14.dp, vertical = 6.dp)
                            .padding(bottom = 3.dp)
                    ) {
                        Text(
                            text = "$readyCount / $totalCount",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color.White
                        )
                    }
                }

                // Queue Items
                displayLinks.take(8).forEachIndexed { idx, link ->
                    val qItem = batchQueueItems.getOrNull(idx)
                    val isItemReady = qItem?.status == "READY" || qItem?.status == "DONE" || (!isAnalyzing && qItem == null)
                    val isItemError = qItem?.status == "ERROR"
                    val itemTitle = when {
                        !qItem?.resolvedTitle.isNullOrBlank() -> qItem!!.resolvedTitle
                        isAnalyzing -> "Menganalisis Link #${idx + 1}..."
                        else -> "Link #${idx + 1}"
                    }
                    val statusBadgeText = when {
                        qItem?.status == "DONE" -> "DONE"
                        isItemReady -> "READY"
                        isItemError -> "ERROR"
                        else -> "ANALYZING..."
                    }

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .duolingoCard(
                                backgroundColor = DuoSnowCanvas,
                                borderColor = when {
                                    isItemReady -> DuoFeatherGreen
                                    isItemError -> DuoCardinalRed
                                    else -> DuoSwanBorder
                                },
                                lipColor = when {
                                    isItemReady -> DuoFeatherGreenLip
                                    isItemError -> DuoCardinalRedLip
                                    else -> DuoHareLip
                                },
                                cornerRadius = 16.dp,
                                borderWidth = 2.dp,
                                lipHeight = 3.dp
                            )
                            .clickable { onSelectReadyItem(link) }
                            .padding(horizontal = 14.dp, vertical = 12.dp)
                            .padding(bottom = 3.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(end = 10.dp),
                                verticalArrangement = Arrangement.spacedBy(3.dp)
                            ) {
                                Text(
                                    text = itemTitle,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = DuoEelInk,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Text(
                                    text = if (!qItem?.statusDetail.isNullOrBlank()) {
                                        "${qItem!!.statusDetail} • $link"
                                    } else {
                                        link
                                    },
                                    fontSize = 12.sp,
                                    color = DuoMutedInk,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }

                            Box(
                                modifier = Modifier
                                    .duolingoButton(
                                        faceColor = when {
                                            isItemReady -> DuoFeatherGreen
                                            isItemError -> DuoCardinalRed
                                            else -> DuoFoxOrange
                                        },
                                        lipColor = when {
                                            isItemReady -> DuoFeatherGreenLip
                                            isItemError -> DuoCardinalRedLip
                                            else -> DuoFoxOrangeLip
                                        },
                                        cornerRadius = 10.dp,
                                        lipHeight = 2.dp
                                    )
                                    .padding(horizontal = 12.dp, vertical = 6.dp)
                                    .padding(bottom = 2.dp)
                            ) {
                                Text(
                                    text = statusBadgeText,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = Color.White,
                                    maxLines = 1,
                                    softWrap = false
                                )
                            }
                        }
                    }
                }

                // Footer Duolingo 3D Action Buttons
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 6.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp)
                            .duolingoButton(
                                faceColor = DuoPureWhite,
                                lipColor = DuoHareLip,
                                cornerRadius = 14.dp,
                                lipHeight = 4.dp
                            )
                            .border(2.dp, DuoSwanBorder, RoundedCornerShape(14.dp))
                            .clickable { onClose() }
                            .padding(bottom = 4.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "CLOSE",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = DuoEelInk
                        )
                    }

                    Box(
                        modifier = Modifier
                            .weight(1.3f)
                            .height(48.dp)
                            .duolingoButton(
                                faceColor = DuoFeatherGreen,
                                lipColor = DuoFeatherGreenLip,
                                cornerRadius = 14.dp,
                                lipHeight = 4.dp
                            )
                            .clickable {
                                onStartAllBatchDownload()
                                onClose()
                            }
                            .padding(bottom = 4.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "DOWNLOAD ALL",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color.White
                        )
                    }
                }
            }
        }
    }
}

// =========================================================================
// 4. INLINE VIDEO PREVIEW CARD + DUOLINGO 3D DOWNLOAD BUTTONS (720P / 1080P / MP3)
// =========================================================================
@Composable
fun MoriInlineVideoPreviewAndDownloadCard(
    previewMeta: MoriResolvedMetadata,
    lastDownloadResult: MoriDownloadResult?,
    onDownload720p: () -> Unit,
    onDownload1080p: () -> Unit,
    onDownloadMp3: () -> Unit,
    onOpenFullscreenPreview: () -> Unit,
    onDismissCard: () -> Unit,
    onOpenInMainPlayer: () -> Unit = {},
    onToggleSubFileSelection: (String) -> Unit = {},
    onDownloadSingleSubFile: (com.example.data.MoriSubFileItem) -> Unit = {},
    onDownloadSelectedSubFiles: () -> Unit = {}
) {
    var isPlaying by remember { mutableStateOf(true) }
    var isMuted by remember { mutableStateOf(false) }
    var currentSec by remember(previewMeta.cleanUrl) { mutableFloatStateOf(0f) }
    val subFiles = previewMeta.subFiles
    var activeSubIndex by remember(previewMeta.cleanUrl, subFiles.size) {
        androidx.compose.runtime.mutableIntStateOf(0)
    }
    val safeSubIndex = if (subFiles.isNotEmpty()) activeSubIndex.coerceIn(0, subFiles.lastIndex) else 0
    val currentSubFile = subFiles.getOrNull(safeSubIndex)

    val totalSec = remember(previewMeta.durationSeconds) {
        previewMeta.durationSeconds.coerceIn(5, 3600).toFloat()
    }

    LaunchedEffect(isPlaying, totalSec) {
        while (isPlaying) {
            delay(1000L)
            currentSec = if (currentSec + 1f >= totalSec) 0f else currentSec + 1f
        }
    }

    val localFile = remember(lastDownloadResult) {
        val path = lastDownloadResult?.createdEntity?.internalPlainPath.orEmpty()
        if (path.isNotBlank() && File(path).exists()) File(path) else null
    }

    val activeCoverUrl = currentSubFile?.thumbnailUrl
        ?: previewMeta.carouselImageUrls.getOrNull(safeSubIndex)
        ?: previewMeta.coverImageUrl

    val playableVideoUri = remember(localFile, currentSubFile?.streamUrl, previewMeta.directVideoUrl) {
        val candidateStream = currentSubFile?.streamUrl ?: previewMeta.directVideoUrl
        val isSubVideo = currentSubFile == null || currentSubFile.extension.lowercase() in listOf("mp4", "mkv", "webm", "mov")
        when {
            localFile != null && localFile.extension.lowercase() in listOf("mp4", "mkv", "webm", "mov") ->
                Uri.fromFile(localFile)
            isSubVideo && !candidateStream.isNullOrBlank() && candidateStream.startsWith("http") ->
                Uri.parse(candidateStream)
            else -> null
        }
    }

    val formattedCurrentTime = remember(currentSec) {
        val s = currentSec.toInt().coerceAtLeast(0)
        "%d:%02d".format(s / 60, s % 60)
    }
    val formattedTotalTime = remember(totalSec) {
        val s = totalSec.toInt().coerceAtLeast(1)
        "%d:%02d".format(s / 60, s % 60)
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .duolingoCard(
                backgroundColor = DuoPureWhite,
                borderColor = DuoSwanBorder,
                lipColor = DuoHareLip,
                cornerRadius = 24.dp,
                borderWidth = 2.dp,
                lipHeight = 6.dp
            )
            .padding(bottom = 12.dp)
            .testTag("mori_inline_preview_card")
    ) {
        // Video / Multi-File Preview Box
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 14.dp)
                .height(360.dp)
                .clip(RoundedCornerShape(18.dp))
                .background(Color(0xFF0E1117))
                .border(2.dp, DuoSwanBorder, RoundedCornerShape(18.dp))
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val w = size.width
                val h = size.height
                drawRect(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFF101824),
                            Color(0xFF283747),
                            Color(0xFF1B1F2B)
                        )
                    )
                )
                drawCircle(
                    color = Color(0xFF58CC02).copy(alpha = 0.18f),
                    radius = w * 0.36f,
                    center = Offset(w * 0.5f, h * 0.48f)
                )
            }

            if (!activeCoverUrl.isNullOrBlank()) {
                AsyncImage(
                    model = activeCoverUrl,
                    contentDescription = currentSubFile?.fileName ?: previewMeta.title,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }

            if (playableVideoUri != null) {
                AndroidView(
                    factory = { ctx ->
                        VideoView(ctx).apply {
                            setOnPreparedListener { mp ->
                                mp.isLooping = true
                                val vol = if (isMuted) 0f else 1f
                                mp.setVolume(vol, vol)
                                if (isPlaying) start()
                            }
                            setOnErrorListener { _, _, _ -> true }
                            setVideoURI(playableVideoUri)
                        }
                    },
                    update = { view ->
                        try {
                            if (isPlaying && !view.isPlaying) {
                                view.start()
                            } else if (!isPlaying && view.isPlaying) {
                                view.pause()
                            }
                        } catch (_: Exception) {
                        }
                    },
                    modifier = Modifier.fillMaxSize()
                )
            } else if (currentSubFile != null) {
                // Visual preview card inside player when viewing specific MediaFire/Slide sub-file
                Column(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(horizontal = 48.dp)
                        .background(Color.Black.copy(alpha = 0.65f), RoundedCornerShape(18.dp))
                        .border(2.dp, DuoMacawBlue, RoundedCornerShape(18.dp))
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = ".${currentSubFile.extension.uppercase()} • ${com.example.data.MoriDownloaderEngine.humanSize(currentSubFile.estimatedSizeBytes)}",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = DuoBeeYellow
                    )
                    Text(
                        text = "${currentSubFile.fileName}.${currentSubFile.extension}",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White,
                        textAlign = TextAlign.Center,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = currentSubFile.statusNote,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFA5ED6E),
                        textAlign = TextAlign.Center,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            // TOMBOL NAVIGASI KIRI (<) & KANAN (>) UNTUK GANTI-GANTI PREVIEW BANYAK FILE (MediaFire, TikTok Slide, IG, dll.)
            if (subFiles.size > 1) {
                // Left (<) Button
                Box(
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .padding(start = 10.dp)
                        .size(44.dp)
                        .duolingoButton(
                            faceColor = DuoMacawBlue,
                            lipColor = DuoMacawBlueLip,
                            cornerRadius = 14.dp,
                            lipHeight = 4.dp
                        )
                        .clickable {
                            activeSubIndex = if (safeSubIndex > 0) safeSubIndex - 1 else subFiles.lastIndex
                        }
                        .padding(bottom = 4.dp)
                        .testTag("mori_preview_prev_file_button"),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "<",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White
                    )
                }

                // Right (>) Button
                Box(
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .padding(end = 10.dp)
                        .size(44.dp)
                        .duolingoButton(
                            faceColor = DuoMacawBlue,
                            lipColor = DuoMacawBlueLip,
                            cornerRadius = 14.dp,
                            lipHeight = 4.dp
                        )
                        .clickable {
                            activeSubIndex = if (safeSubIndex < subFiles.lastIndex) safeSubIndex + 1 else 0
                        }
                        .padding(bottom = 4.dp)
                        .testTag("mori_preview_next_file_button"),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = ">",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White
                    )
                }
            }

            // Top-Left Platform & Multi-File Counter Pill
            Row(
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(10.dp)
                    .background(Color.Black.copy(alpha = 0.72f), RoundedCornerShape(12.dp))
                    .border(1.5.dp, DuoFeatherGreen, RoundedCornerShape(12.dp))
                    .padding(horizontal = 10.dp, vertical = 5.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(if (!previewMeta.directVideoUrl.isNullOrBlank()) DuoFeatherGreen else DuoFoxOrange)
                )
                Text(
                    text = if (subFiles.size > 1) {
                        "${previewMeta.platform.displayName.uppercase()} • FILE ${safeSubIndex + 1}/${subFiles.size}"
                    } else {
                        "${previewMeta.platform.displayName.uppercase()} • ${previewMeta.resolverSource.take(18)}"
                    },
                    fontSize = 10.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.White
                )
            }

            // Clean Bottom-Left Metadata Overlay (above video controls)
            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .fillMaxWidth()
                    .padding(start = 12.dp, end = 12.dp, bottom = 46.dp)
                    .background(Color.Black.copy(alpha = 0.65f), RoundedCornerShape(12.dp))
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                Text(
                    text = if (currentSubFile != null) {
                        "[${safeSubIndex + 1}/${subFiles.size}] ${currentSubFile.fileName}.${currentSubFile.extension}"
                    } else {
                        previewMeta.title.ifBlank { "Mori Media Stream" }
                    },
                    fontSize = 13.sp,
                    lineHeight = 17.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.White,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = if (currentSubFile != null) {
                        "${currentSubFile.statusNote} • ${com.example.data.MoriDownloaderEngine.humanSize(currentSubFile.estimatedSizeBytes)}"
                    } else {
                        "${previewMeta.author} • ${previewMeta.platform.badgeTag}"
                    },
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFA5ED6E),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            // Top-Right Duolingo 3D Red Close 'X' Button
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(10.dp)
                    .size(36.dp)
                    .duolingoButton(
                        faceColor = DuoCardinalRed,
                        lipColor = DuoCardinalRedLip,
                        cornerRadius = 12.dp,
                        lipHeight = 3.dp
                    )
                    .clickable { onDismissCard() }
                    .padding(bottom = 3.dp),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Close Preview",
                    tint = Color.White,
                    modifier = Modifier.size(18.dp)
                )
            }

            // Bottom Video Controls Overlay Bar
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                Color.Black.copy(alpha = 0.85f)
                            )
                        )
                    )
                    .padding(horizontal = 10.dp, vertical = 8.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                        contentDescription = "Play/Pause",
                        tint = DuoFeatherGreen,
                        modifier = Modifier
                            .size(24.dp)
                            .clickable { isPlaying = !isPlaying }
                    )

                    Text(
                        text = formattedCurrentTime,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White
                    )

                    Slider(
                        value = currentSec.coerceIn(0f, totalSec),
                        onValueChange = { currentSec = it },
                        valueRange = 0f..totalSec,
                        modifier = Modifier
                            .weight(1f)
                            .height(24.dp),
                        colors = SliderDefaults.colors(
                            thumbColor = DuoFeatherGreen,
                            activeTrackColor = DuoFeatherGreen,
                            inactiveTrackColor = Color.White.copy(alpha = 0.35f)
                        )
                    )

                    Text(
                        text = formattedTotalTime,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White
                    )

                    Icon(
                        imageVector = if (isMuted) Icons.Default.VolumeOff else Icons.Default.VolumeUp,
                        contentDescription = "Mute/Unmute",
                        tint = Color.White,
                        modifier = Modifier
                            .size(20.dp)
                            .clickable { isMuted = !isMuted }
                    )

                    Icon(
                        imageVector = Icons.Default.Fullscreen,
                        contentDescription = "Fullscreen",
                        tint = DuoMacawBlue,
                        modifier = Modifier
                            .size(22.dp)
                            .clickable { onOpenFullscreenPreview() }
                            .testTag("mori_quick_stream_preview_button")
                    )
                }
            }
        }

        // BAR NAVIGASI MULTI-FILE (< KIRI | PILIH FILE #N | KANAN >) DI BAWAH PLAYER
        if (subFiles.size > 1 && currentSubFile != null) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp)
                    .duolingoCard(
                        backgroundColor = DuoSnowCanvas,
                        borderColor = DuoMacawBlue,
                        lipColor = DuoMacawBlueLip,
                        cornerRadius = 16.dp,
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
                    // Tombol Kiri
                    Box(
                        modifier = Modifier
                            .height(36.dp)
                            .duolingoButton(
                                faceColor = DuoMacawBlue,
                                lipColor = DuoMacawBlueLip,
                                cornerRadius = 10.dp,
                                lipHeight = 3.dp
                            )
                            .clickable {
                                activeSubIndex = if (safeSubIndex > 0) safeSubIndex - 1 else subFiles.lastIndex
                            }
                            .padding(horizontal = 12.dp)
                            .padding(bottom = 3.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "< SEBELUMNYA",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color.White
                        )
                    }

                    Text(
                        text = "FILE ${safeSubIndex + 1} DARI ${subFiles.size}",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = DuoEelInk
                    )

                    // Tombol Kanan
                    Box(
                        modifier = Modifier
                            .height(36.dp)
                            .duolingoButton(
                                faceColor = DuoMacawBlue,
                                lipColor = DuoMacawBlueLip,
                                cornerRadius = 10.dp,
                                lipHeight = 3.dp
                            )
                            .clickable {
                                activeSubIndex = if (safeSubIndex < subFiles.lastIndex) safeSubIndex + 1 else 0
                            }
                            .padding(horizontal = 12.dp)
                            .padding(bottom = 3.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "BERIKUTNYA >",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color.White
                        )
                    }
                }

                // Info File Aktif + Checkbox Tandai + Tombol Unduh File Ini
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .height(40.dp)
                            .duolingoButton(
                                faceColor = if (currentSubFile.isSelected) DuoFeatherGreen else DuoPureWhite,
                                lipColor = if (currentSubFile.isSelected) DuoFeatherGreenLip else DuoHareLip,
                                cornerRadius = 12.dp,
                                lipHeight = 3.dp
                            )
                            .clickable { onToggleSubFileSelection(currentSubFile.id) }
                            .padding(horizontal = 10.dp)
                            .padding(bottom = 3.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = if (currentSubFile.isSelected) "TERPILIH" else "PILIH FILE",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = if (currentSubFile.isSelected) Color.White else DuoEelInk
                        )
                    }

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(40.dp)
                            .duolingoButton(
                                faceColor = DuoFoxOrange,
                                lipColor = DuoFoxOrangeLip,
                                cornerRadius = 12.dp,
                                lipHeight = 3.dp
                            )
                            .clickable { onDownloadSingleSubFile(currentSubFile) }
                            .padding(horizontal = 12.dp)
                            .padding(bottom = 3.dp)
                            .testTag("mori_download_active_subfile_button"),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "UNDUH FILE #${safeSubIndex + 1} (.${currentSubFile.extension.uppercase()})",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color.White,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }

                val selectedCount = subFiles.count { it.isSelected }
                if (selectedCount > 1) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(40.dp)
                            .duolingoButton(
                                faceColor = DuoBeetlePurple,
                                lipColor = DuoBeetlePurpleLip,
                                cornerRadius = 12.dp,
                                lipHeight = 3.dp
                            )
                            .clickable { onDownloadSelectedSubFiles() }
                            .padding(bottom = 3.dp)
                            .testTag("mori_download_all_selected_subfiles_button"),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "UNDUH $selectedCount FILE TERPILIH SEKALIGUS",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color.White
                        )
                    }
                }
            }
        }

        // Tactile Duolingo 3D Download Buttons (Download 1 -> 720P, Download 2 -> 1080P, Download 3 -> MP3)
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 4.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            if (lastDownloadResult != null) {
                MoriDuolingoDownloadButton(
                    leftLabel = "SIMPAN & BUKA DI PLAYER UTAMA",
                    rightBadge = lastDownloadResult.createdEntity.category.uppercase(),
                    faceColor = DuoFoxOrange,
                    lipColor = DuoFoxOrangeLip,
                    icon = Icons.Default.PlayArrow,
                    onClick = onOpenInMainPlayer,
                    testTag = "mori_open_in_main_player_button"
                )
            }

            MoriDuolingoDownloadButton(
                leftLabel = "Download 1",
                rightBadge = "720P",
                faceColor = DuoFeatherGreen,
                lipColor = DuoFeatherGreenLip,
                icon = Icons.Default.VideoFile,
                onClick = onDownload720p,
                testTag = "mori_download_row_720p"
            )

            MoriDuolingoDownloadButton(
                leftLabel = "Download 2",
                rightBadge = "1080P",
                faceColor = DuoMacawBlue,
                lipColor = DuoMacawBlueLip,
                icon = Icons.Default.HighQuality,
                onClick = onDownload1080p,
                testTag = "mori_download_row_1080p"
            )

            MoriDuolingoDownloadButton(
                leftLabel = "Download 3",
                rightBadge = "MP3",
                faceColor = DuoBeetlePurple,
                lipColor = DuoBeetlePurpleLip,
                icon = Icons.Default.AudioFile,
                onClick = onDownloadMp3,
                testTag = "mori_download_row_mp3"
            )
        }
    }
}

@Composable
private fun MoriDuolingoDownloadButton(
    leftLabel: String,
    rightBadge: String,
    faceColor: Color,
    lipColor: Color,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    onClick: () -> Unit,
    testTag: String
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(54.dp)
            .duolingoButton(
                faceColor = faceColor,
                lipColor = lipColor,
                cornerRadius = 16.dp,
                lipHeight = 4.dp
            )
            .clickable { onClick() }
            .padding(horizontal = 16.dp)
            .padding(bottom = 4.dp)
            .testTag(testTag),
        contentAlignment = Alignment.Center
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
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(20.dp)
                )
                Text(
                    text = leftLabel,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.White
                )
            }
            Surface(
                color = Color.Black.copy(alpha = 0.20f),
                shape = RoundedCornerShape(10.dp)
            ) {
                Text(
                    text = rightBadge,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.White,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 5.dp)
                )
            }
        }
    }
}

// =========================================================================
// 5. SUPPORTED PLATFORMS 2-COLUMN DUOLINGO 3D GRID
// =========================================================================
@Composable
fun MoriSupportedPlatforms2ColGrid(
    onSelectPlatform: (MoriSupportedPlatformSpec) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Text(
            text = "SUPPORTED PLATFORMS",
            fontSize = 12.sp,
            fontWeight = FontWeight.ExtraBold,
            letterSpacing = 2.sp,
            color = DuoMutedInk,
            modifier = Modifier.padding(top = 6.dp, bottom = 2.dp)
        )

        MORI_SUPPORTED_PLATFORMS_GRID.chunked(2).forEach { rowPair ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                rowPair.forEach { spec ->
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(56.dp)
                            .duolingoCard(
                                backgroundColor = DuoPureWhite,
                                borderColor = DuoSwanBorder,
                                lipColor = DuoHareLip,
                                cornerRadius = 16.dp,
                                borderWidth = 2.dp,
                                lipHeight = 4.dp
                            )
                            .clickable { onSelectPlatform(spec) }
                            .padding(horizontal = 12.dp)
                            .padding(bottom = 4.dp),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(34.dp)
                                    .duolingoButton(
                                        faceColor = spec.badgeColor,
                                        lipColor = spec.badgeLipColor,
                                        cornerRadius = 10.dp,
                                        lipHeight = 2.dp
                                    )
                                    .padding(bottom = 2.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                MoriPlatformBrandIconCanvas(
                                    iconCode = spec.iconCode,
                                    tintColor = Color.White,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                            Text(
                                text = spec.title,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.ExtraBold,
                                letterSpacing = 0.8.sp,
                                color = DuoEelInk,
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

@Composable
private fun MoriPlatformBrandIconCanvas(
    iconCode: Int,
    tintColor: Color = Color.White,
    modifier: Modifier = Modifier
) {
    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height
        val cx = w / 2f
        val cy = h / 2f
        when (iconCode) {
            0 -> {
                // TikTok / Douyin Music Note
                drawCircle(color = tintColor, radius = w * 0.22f, center = Offset(w * 0.36f, h * 0.72f))
                drawLine(tintColor, Offset(w * 0.52f, h * 0.72f), Offset(w * 0.52f, h * 0.16f), strokeWidth = 3.5f, cap = StrokeCap.Round)
                drawLine(tintColor, Offset(w * 0.52f, h * 0.16f), Offset(w * 0.80f, h * 0.32f), strokeWidth = 3.5f, cap = StrokeCap.Round)
            }
            1 -> {
                // Instagram Camera Outline
                drawRoundRect(
                    color = tintColor,
                    topLeft = Offset(w * 0.12f, h * 0.12f),
                    size = Size(w * 0.76f, h * 0.76f),
                    cornerRadius = CornerRadius(w * 0.22f, w * 0.22f),
                    style = Stroke(width = 3f)
                )
                drawCircle(color = tintColor, radius = w * 0.18f, center = Offset(cx, cy), style = Stroke(width = 3f))
                drawCircle(color = tintColor, radius = 2.2f, center = Offset(w * 0.70f, h * 0.30f))
            }
            2 -> {
                // YouTube Play Triangle
                val tri = Path().apply {
                    moveTo(w * 0.30f, h * 0.24f)
                    lineTo(w * 0.76f, cy)
                    lineTo(w * 0.30f, h * 0.76f)
                    close()
                }
                drawPath(path = tri, color = tintColor)
            }
            3 -> {
                // X / Twitter Cross
                drawLine(tintColor, Offset(w * 0.20f, h * 0.20f), Offset(w * 0.80f, h * 0.80f), strokeWidth = 3.5f, cap = StrokeCap.Round)
                drawLine(tintColor, Offset(w * 0.80f, h * 0.20f), Offset(w * 0.20f, h * 0.80f), strokeWidth = 3.5f, cap = StrokeCap.Round)
            }
            4 -> {
                // Sound Waves
                drawLine(tintColor, Offset(w * 0.22f, h * 0.34f), Offset(w * 0.78f, h * 0.34f), strokeWidth = 3f, cap = StrokeCap.Round)
                drawLine(tintColor, Offset(w * 0.28f, h * 0.52f), Offset(w * 0.72f, h * 0.52f), strokeWidth = 3f, cap = StrokeCap.Round)
                drawLine(tintColor, Offset(w * 0.34f, h * 0.70f), Offset(w * 0.66f, h * 0.70f), strokeWidth = 3f, cap = StrokeCap.Round)
            }
            5 -> {
                // Bilibili TV + Antennas
                drawLine(tintColor, Offset(w * 0.30f, h * 0.14f), Offset(w * 0.44f, h * 0.30f), strokeWidth = 2.8f, cap = StrokeCap.Round)
                drawLine(tintColor, Offset(w * 0.70f, h * 0.14f), Offset(w * 0.56f, h * 0.30f), strokeWidth = 2.8f, cap = StrokeCap.Round)
                drawRoundRect(
                    color = tintColor,
                    topLeft = Offset(w * 0.14f, h * 0.30f),
                    size = Size(w * 0.72f, h * 0.54f),
                    cornerRadius = CornerRadius(6f, 6f),
                    style = Stroke(width = 3f)
                )
            }
            else -> {
                drawCircle(color = tintColor, radius = w * 0.28f, center = Offset(cx, cy), style = Stroke(width = 3.2f))
                drawCircle(color = tintColor, radius = w * 0.10f, center = Offset(cx, cy))
            }
        }
    }
}

// =========================================================================
// 6. FLOATING "Downloads" OVERLAY DRAWER IN DUOLINGO 3D THEME
//    Supports both IDLE ("No active downloads / Active tasks will appear here")
//    and ACTIVE / DONE states!
// =========================================================================
@Composable
fun MoriFloatingDownloadsDrawer(
    isVisible: Boolean,
    isDownloading: Boolean,
    progressRatio: Float,
    activePlatformBadge: String,
    activeQualityBadge: String,
    activeTitle: String,
    lastDownloadResult: MoriDownloadResult?,
    onCancelOrRemove: () -> Unit,
    onKeepDownloaded: () -> Unit,
    onRejectAndDeleteDownloaded: () -> Unit,
    onCloseDrawer: () -> Unit,
    modifier: Modifier = Modifier
) {
    if (!isVisible) return

    val hasActiveOrFinishedTask = isDownloading || lastDownloadResult != null
    val pct = if (isDownloading) {
        (progressRatio * 100f).roundToInt().coerceIn(5, 99)
    } else if (lastDownloadResult != null) {
        100
    } else {
        0
    }
    val downloadedMb = "%.1f".format(2.0f * (pct / 100f))

    Column(
        modifier = modifier
            .fillMaxWidth()
            .duolingoCard(
                backgroundColor = DuoPureWhite,
                borderColor = DuoSwanBorder,
                lipColor = DuoHareLip,
                cornerRadius = 22.dp,
                borderWidth = 2.dp,
                lipHeight = 6.dp
            )
            .padding(16.dp)
            .padding(bottom = 6.dp)
            .testTag("mori_downloads_overlay_drawer"),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Drawer Header: Cloud Icon + "Downloads" + "IDLE" / "1 ACTIVE" / "1 DONE" Duolingo pill + Close X
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(30.dp)
                        .duolingoButton(
                            faceColor = if (hasActiveOrFinishedTask) DuoFeatherGreen else DuoMacawBlue,
                            lipColor = if (hasActiveOrFinishedTask) DuoFeatherGreenLip else DuoMacawBlueLip,
                            cornerRadius = 9.dp,
                            lipHeight = 2.dp
                        )
                        .padding(bottom = 2.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.CloudDownload,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(16.dp)
                    )
                }
                Text(
                    text = "Downloads",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = DuoEelInk
                )
                Box(
                    modifier = Modifier
                        .duolingoButton(
                            faceColor = when {
                                isDownloading -> DuoFeatherGreen
                                lastDownloadResult != null -> DuoMacawBlue
                                else -> DuoSnowCanvas
                            },
                            lipColor = when {
                                isDownloading -> DuoFeatherGreenLip
                                lastDownloadResult != null -> DuoMacawBlueLip
                                else -> DuoHareLip
                            },
                            cornerRadius = 8.dp,
                            lipHeight = 2.dp
                        )
                        .padding(horizontal = 9.dp, vertical = 3.dp)
                        .padding(bottom = 2.dp)
                ) {
                    Text(
                        text = when {
                            isDownloading -> "1 ACTIVE"
                            lastDownloadResult != null -> "1 DONE"
                            else -> "IDLE"
                        },
                        fontSize = 10.sp,
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = 0.6.sp,
                        color = if (hasActiveOrFinishedTask) Color.White else DuoMutedInk
                    )
                }
            }

            IconButton(
                onClick = onCloseDrawer,
                modifier = Modifier.size(28.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Close Downloads Drawer",
                    tint = DuoMutedInk,
                    modifier = Modifier.size(18.dp)
                )
            }
        }

        HorizontalDivider(thickness = 2.dp, color = DuoSwanBorder)

        if (!hasActiveOrFinishedTask) {
            // IDLE Empty State matching user's screenshot ("No active downloads / Active tasks will appear here")
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 22.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .duolingoCard(
                            backgroundColor = DuoSnowCanvas,
                            borderColor = DuoSwanBorder,
                            lipColor = DuoHareLip,
                            cornerRadius = 14.dp,
                            borderWidth = 2.dp,
                            lipHeight = 3.dp
                        )
                        .padding(bottom = 3.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Download,
                        contentDescription = null,
                        tint = DuoMutedInk,
                        modifier = Modifier.size(24.dp)
                    )
                }
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "No active downloads",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = DuoEelInk
                )
                Text(
                    text = "Active tasks will appear here",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = DuoMutedInk
                )
            }
        } else {
            // Active / Completed Download Item Card in Duolingo 3D Theme
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .duolingoCard(
                        backgroundColor = DuoSnowCanvas,
                        borderColor = DuoSwanBorder,
                        lipColor = DuoHareLip,
                        cornerRadius = 16.dp,
                        borderWidth = 2.dp,
                        lipHeight = 3.dp
                    )
                    .padding(12.dp)
                    .padding(bottom = 3.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .duolingoButton(
                                faceColor = DuoMacawBlue,
                                lipColor = DuoMacawBlueLip,
                                cornerRadius = 8.dp,
                                lipHeight = 2.dp
                            )
                            .padding(horizontal = 8.dp, vertical = 3.dp)
                            .padding(bottom = 2.dp)
                    ) {
                        Text(
                            text = "$activePlatformBadge · $activeQualityBadge",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color.White,
                            maxLines = 1,
                            softWrap = false
                        )
                    }

                    Text(
                        text = activeTitle.ifBlank { "#manhwa #manhwareccome..." },
                        fontSize = 12.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = DuoEelInk,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f)
                    )

                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Cancel Download",
                        tint = DuoCardinalRed,
                        modifier = Modifier
                            .size(18.dp)
                            .clickable { onCancelOrRemove() }
                            .testTag("mori_cancel_active_download_button")
                    )
                }

                // Duolingo Feather Green Progress Bar
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .clip(RoundedCornerShape(50))
                        .background(DuoSwanBorder)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(fraction = (pct / 100f).coerceIn(0.05f, 1f))
                            .height(8.dp)
                            .clip(RoundedCornerShape(50))
                            .background(DuoFeatherGreen)
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "$downloadedMb MB / 2.0 MB (201 KB/s)",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = DuoMutedInk
                    )
                    Text(
                        text = "$pct%",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = DuoFeatherGreenLip
                    )
                }

                // Tactile Duolingo 3D Buttons: SIMPAN & BUKA (Auto Opens Video Player) + SALAH / HAPUS
                if (lastDownloadResult != null && !isDownloading) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 4.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .weight(1.2f)
                                .height(42.dp)
                                .duolingoButton(
                                    faceColor = DuoFeatherGreen,
                                    lipColor = DuoFeatherGreenLip,
                                    cornerRadius = 12.dp,
                                    lipHeight = 3.dp
                                )
                                .clickable { onKeepDownloaded() }
                                .padding(bottom = 3.dp)
                                .testTag("mori_open_downloaded_item_button"),
                            contentAlignment = Alignment.Center
                        ) {
                            Row(
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(15.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "SIMPAN & BUKA",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = Color.White
                                )
                            }
                        }

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
                                .clickable { onRejectAndDeleteDownloaded() }
                                .padding(bottom = 3.dp)
                                .testTag("mori_reject_autodelete_media_button"),
                            contentAlignment = Alignment.Center
                        ) {
                            Row(
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.DeleteOutline,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(15.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "SALAH / HAPUS",
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
}
