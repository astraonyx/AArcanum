package com.example.ui

import android.media.MediaMetadataRetriever
import android.media.PlaybackParams
import android.net.Uri
import android.os.Build
import android.widget.VideoView
import androidx.activity.compose.BackHandler
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.platform.LocalContext
import com.example.ui.theme.VaultObsidian
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Comment
import androidx.compose.material.icons.automirrored.filled.PlaylistAdd
import androidx.compose.material.icons.automirrored.filled.PlaylistPlay
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.AspectRatio
import androidx.compose.material.icons.filled.BookmarkAdd
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ContentCut
import androidx.compose.material.icons.filled.CreateNewFolder
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.DriveFileMove
import androidx.compose.material.icons.filled.Equalizer
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.FastForward
import androidx.compose.material.icons.filled.FastRewind
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.Forward5
import androidx.compose.material.icons.filled.Fullscreen
import androidx.compose.material.icons.filled.Headphones
import androidx.compose.material.icons.filled.HighQuality
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.LockOpen
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.PlayCircleFilled
import androidx.compose.material.icons.filled.Repeat
import androidx.compose.material.icons.filled.RepeatOne
import androidx.compose.material.icons.filled.Replay5
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material.icons.filled.SkipPrevious
import androidx.compose.material.icons.filled.SmartDisplay
import androidx.compose.material.icons.filled.Sort
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Storage
import androidx.compose.material.icons.filled.Subtitles
import androidx.compose.material.icons.filled.ThumbDown
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.filled.UploadFile
import androidx.compose.material.icons.filled.VideoFile
import androidx.compose.material.icons.filled.VideoLibrary
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import kotlinx.coroutines.launch
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
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
import com.example.data.VaultCategory
import com.example.data.VaultItemEntity
import com.example.ui.theme.DuoBeetlePurple
import com.example.ui.theme.DuoEelInk
import com.example.ui.theme.DuoMacawBlue
import com.example.ui.theme.DuoMintSurface
import com.example.ui.theme.DuoMutedInk
import com.example.ui.theme.DuoPureWhite
import com.example.ui.theme.DuoSnowCanvas
import com.example.ui.theme.DuoSwanBorder
import com.example.ui.theme.JetBrainsMonoFontFamily
import com.example.ui.theme.VaultAmber
import com.example.ui.theme.VaultCoral
import com.example.ui.theme.VaultLime
import com.example.ui.theme.duolingoButton
import com.example.ui.theme.duolingoCard
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.math.abs
import kotlin.math.sin
import kotlinx.coroutines.delay

private val YouTubeRed = Color(0xFFFF0000)
private val YouTubeDarkCanvas = Color(0xFF0F0F0F)
private val YouTubeDarkChip = Color(0xFF272727)

private val YtCardWhite: Color
    get() = com.example.ui.theme.CosmicObsidian
private val YtChipBg: Color
    get() = DuoMintSurface
private val YtBorder: Color
    get() = DuoSwanBorder
private val YtInk = DuoEelInk
private val YtSubtle = DuoMutedInk
private val YtCyan = DuoMacawBlue
private val YtPurple = DuoBeetlePurple

private fun normalizePlaylistPath(raw: String?): String? {
    return raw
        ?.replace(" > ", "/")
        ?.replace('\\', '/')
        ?.removeSuffix(".zip")
        ?.removeSuffix(".ZIP")
        ?.trim('/')
        ?.trim()
        ?.takeIf { it.isNotEmpty() && it != "Semua Media" && it != "Root Brankas (Tanpa Folder)" }
}

private fun VaultItemEntity.playlistFolderLabel(): String {
    return normalizePlaylistPath(groupArchiveName) ?: "Tanpa Playlist (Root)"
}

private enum class MediaDeckSubTab(val label: String) {
    VIDEO_YOUTUBE("Video"),
    AUDIO_SONIC("Audio")
}

private enum class YouTubeDatabaseFilter(val label: String) {
    ALL("Semua"),
    UNLOCKED("Siap Putar"),
    ENCRYPTED("Terkunci NYX"),
    FORMAT_PRIMARY(".MOV / .MP4 / .MP3"),
    LARGE_FILE("Ukuran Besar"),
    FAVORITES("Disukai"),
    RECENT("Terbaru")
}

private enum class YouTubeDatabaseSort(val label: String) {
    NEWEST("Terbaru"),
    LARGEST("Ukuran Terbesar"),
    LONGEST("Durasi Terpanjang"),
    NAME_ASC("Nama A-Z")
}

private data class LocalVideoTimestampNote(
    val id: Long,
    val videoId: Int,
    val timestampSeconds: Int,
    val text: String,
    val author: String = "Penjaga Brankas"
)

/**
 * YouTube-Style Local Vault Video Player & Sonic Audio Deck
 * 100% Material Icons (Zero Emojis) + Complete YouTube Watch Page & Local Vault Database Browser.
 */
@Composable
fun VaultMediaPlayerScreen(
    activeVaultId: Int,
    mediaState: MediaPlayerUiState,
    allItems: List<VaultItemEntity>,
    activeMediaFolder: String?,
    onSelectMediaFolder: (String?) -> Unit,
    onMoveMediaItemToPlaylist: (VaultItemEntity, String) -> Unit,
    onRenamePlaylistFolder: (String, String) -> Unit = { _, _ -> },
    onSetHold2xSpeed: (Boolean) -> Unit,
    onTogglePlayPause: () -> Unit,
    onSeekToSeconds: (Int) -> Unit,
    onSeekToPositionMs: (Long) -> Unit = {},
    onUpdateActualDurationMs: (Long) -> Unit = {},
    onSkipTrack: (Boolean) -> Unit,
    onSelectOrOpenMedia: (VaultItemEntity) -> Unit,
    onOpenFullscreenOverlay: (VaultItemEntity) -> Unit,
    onRequestPinToggleEncrypt: (VaultItemEntity) -> Unit,
    onBatchEncryptAllWithPin: (Boolean) -> Unit = {},
    onRequestExportOrShareWithBiometric: (VaultItemEntity, Boolean) -> Unit = { _, _ -> },
    onCycleSpeed: () -> Unit,
    onToggleRepeat: () -> Unit,
    onImportMediaOrZipClick: () -> Unit,
    isFullscreenOverlayOpen: Boolean = false,
    onSyncHardwarePositionMs: (Long, Long) -> Unit = { _, _ -> }
) {
    val activePlaylistPath = remember(activeMediaFolder) { normalizePlaylistPath(activeMediaFolder) }
    val normalizedFolder = activePlaylistPath ?: "Semua Media"

    var expandedPlaylistNameKey by remember { mutableStateOf<String?>(null) }
    val userCreatedPlaylists = remember { mutableStateListOf<String>() }

    // Back button steps out of sub-playlist to parent playlist, or from playlist back to root
    BackHandler(enabled = activePlaylistPath != null) {
        val parent = activePlaylistPath?.substringBeforeLast('/', "")?.takeIf { it.isNotEmpty() }
        expandedPlaylistNameKey = null
        onSelectMediaFolder(parent)
    }

    val allMediaTracks = remember(allItems) {
        allItems.filter {
            it.category == VaultCategory.AUDIO.name || it.category == VaultCategory.VIDEO.name
        }
    }

    // Video Player strictly renders VIDEO items for its video playlists & video feed
    val allVideoTracks = remember(allMediaTracks) {
        allMediaTracks.filter { it.category == VaultCategory.VIDEO.name }
    }
    val lockedVideoCount = remember(allVideoTracks) {
        allVideoTracks.count { it.isEncrypted }
    }
    val unlockedVideoCount = remember(allVideoTracks) {
        allVideoTracks.count { !it.isEncrypted }
    }

    // Full paths of all Video Playlists (from ZIP folders containing videos + user-created playlists)
    val allVideoPlaylistPaths = remember(allVideoTracks, userCreatedPlaylists.toList()) {
        val fromVideos = allVideoTracks.mapNotNull { normalizePlaylistPath(it.groupArchiveName) }
        val fromUser = userCreatedPlaylists.mapNotNull { normalizePlaylistPath(it) }
        (fromVideos + fromUser).distinct().sorted()
    }

    // Visible playlists at current level: Top-level playlists at root, or nested Sub-Playlists when inside an open playlist
    val visibleChildVideoPlaylists = remember(allVideoPlaylistPaths, activePlaylistPath) {
        if (activePlaylistPath == null) {
            allVideoPlaylistPaths
                .map { it.substringBefore('/') }
                .distinct()
                .sorted()
        } else {
            val prefix = "$activePlaylistPath/"
            allVideoPlaylistPaths
                .filter { it.startsWith(prefix, ignoreCase = true) && it.length > prefix.length }
                .map { full ->
                    val remainder = full.substring(prefix.length).trim('/')
                    val nextSegment = remainder.substringBefore('/')
                    "$activePlaylistPath/$nextSegment"
                }
                .distinct()
                .sorted()
        }
    }

    val playlistFolders = remember(allVideoPlaylistPaths) {
        (listOf("Semua Media") + allVideoPlaylistPaths).distinct()
    }

    val folderFilteredTracks = remember(allMediaTracks, activePlaylistPath) {
        if (activePlaylistPath == null) {
            allMediaTracks
        } else {
            allMediaTracks.filter {
                normalizePlaylistPath(it.groupArchiveName).equals(activePlaylistPath, ignoreCase = true)
            }
        }
    }

    // Videos in current scope:
    // - At root (activePlaylistPath == null): only standalone videos without a folder (unless searching)
    // - Inside an open playlist (activePlaylistPath != null): videos directly inside that playlist
    val videoTracks = remember(allVideoTracks, activePlaylistPath) {
        if (activePlaylistPath == null) {
            allVideoTracks.filter { normalizePlaylistPath(it.groupArchiveName) == null }
        } else {
            allVideoTracks.filter {
                normalizePlaylistPath(it.groupArchiveName).equals(activePlaylistPath, ignoreCase = true)
            }
        }
    }
    val audioTracks = remember(folderFilteredTracks) {
        folderFilteredTracks.filter { it.category == VaultCategory.AUDIO.name }
    }

    var selectedSubTab by remember { mutableStateOf(MediaDeckSubTab.VIDEO_YOUTUBE) }
    var showCreatePlaylistModal by remember { mutableStateOf(false) }
    var createSubPlaylistParent by remember { mutableStateOf<String?>(null) }
    var showRenamePlaylistModal by remember { mutableStateOf(false) }
    var playlistTargetToRename by remember { mutableStateOf<String?>(null) }
    var renamePlaylistInput by remember { mutableStateOf("") }
    var newPlaylistInput by remember { mutableStateOf("") }
    var itemToMovePlaylist by remember { mutableStateOf<VaultItemEntity?>(null) }

    // Smart Search & Unified Video/Audio interactive states
    var isSearchExpanded by remember { mutableStateOf(true) }
    var searchQuery by remember { mutableStateOf("") }
    var selectedDbFilter by remember { mutableStateOf(YouTubeDatabaseFilter.ALL) }
    var selectedDbSort by remember { mutableStateOf(YouTubeDatabaseSort.NEWEST) }
    var isFullscreenCinemaActive by remember { mutableStateOf(false) }
    var showYouTubeSettingsSheet by remember { mutableStateOf(false) }
    var isDescriptionExpanded by remember { mutableStateOf(false) }
    var isCommentsExpanded by remember { mutableStateOf(false) }
    var isTheaterMode by remember { mutableStateOf(false) }
    var isAutoplayEnabled by remember { mutableStateOf(true) }
    var isSubtitlesHudEnabled by remember { mutableStateOf(false) }
    var showStatsForNerds by remember { mutableStateOf(false) }
    var isPlayerScreenLocked by remember { mutableStateOf(false) }
    var selectedVideoQualityLabel by remember { mutableStateOf("1080p HD (Asli Brankas)") }

    // Local Likes / Dislikes / Saved state per video ID
    val likedVideosMap = remember { mutableStateMapOf<Int, Boolean>() }
    val dislikedVideosMap = remember { mutableStateMapOf<Int, Boolean>() }
    val videoNotes = remember {
        mutableStateListOf(
            LocalVideoTimestampNote(
                id = 1L,
                videoId = -1,
                timestampSeconds = 0,
                text = "Awal pemutaran video dari database brankas lokal.",
                author = "Database Brankas"
            ),
            LocalVideoTimestampNote(
                id = 2L,
                videoId = -1,
                timestampSeconds = 15,
                text = "Poin penting pertama dalam rekaman video ini.",
                author = "Penjaga Brankas"
            )
        )
    }
    var newCommentInput by remember { mutableStateOf("") }

    val activeTrack = mediaState.currentTrack
    LaunchedEffect(activeTrack?.id) {
        if (activeTrack != null) {
            selectedSubTab = if (activeTrack.category == VaultCategory.VIDEO.name) {
                MediaDeckSubTab.VIDEO_YOUTUBE
            } else {
                MediaDeckSubTab.AUDIO_SONIC
            }
        }
    }

    val displayedVideoTrack = remember(activeTrack, videoTracks, allVideoTracks) {
        when {
            activeTrack?.category == VaultCategory.VIDEO.name -> activeTrack
            videoTracks.isNotEmpty() -> videoTracks.first()
            else -> allVideoTracks.firstOrNull()
        }
    }

    val displayedAudioTrack = remember(activeTrack, audioTracks) {
        if (activeTrack?.category == VaultCategory.AUDIO.name) activeTrack
        else audioTracks.firstOrNull()
    }

    // Smart Search filtered & sorted video feed (matches name, extension .mov/.mp4, formatted size MB/KB, and playlist/folder)
    val filteredVideoFeed = remember(
        videoTracks,
        allVideoTracks,
        activePlaylistPath,
        searchQuery,
        selectedDbFilter,
        selectedDbSort,
        likedVideosMap.toMap()
    ) {
        val q = searchQuery.trim()
        val sourceList = if (activePlaylistPath == null && (q.isNotBlank() || selectedDbFilter != YouTubeDatabaseFilter.ALL)) {
            allVideoTracks
        } else {
            videoTracks
        }
        val base = sourceList.filter { item ->
            val folderLabel = item.playlistFolderLabel()
            val sizeLabel = formatFileSize(item.sizeBytes)
            val matchesQuery = q.isBlank() ||
                item.name.contains(q, ignoreCase = true) ||
                item.extension.contains(q, ignoreCase = true) ||
                "${item.name}.${item.extension}".contains(q, ignoreCase = true) ||
                sizeLabel.contains(q, ignoreCase = true) ||
                folderLabel.contains(q, ignoreCase = true)

            val extLower = item.extension.lowercase(Locale.ROOT)
            val matchesFilter = when (selectedDbFilter) {
                YouTubeDatabaseFilter.ALL -> true
                YouTubeDatabaseFilter.UNLOCKED -> !item.isEncrypted
                YouTubeDatabaseFilter.ENCRYPTED -> item.isEncrypted
                YouTubeDatabaseFilter.FORMAT_PRIMARY -> extLower == "mov" || extLower == "mp4" || extLower == "mkv"
                YouTubeDatabaseFilter.LARGE_FILE -> item.sizeBytes >= 5L * 1024L * 1024L
                YouTubeDatabaseFilter.FAVORITES -> likedVideosMap[item.id] == true || folderLabel == "Favorit"
                YouTubeDatabaseFilter.RECENT -> true
            }
            matchesQuery && matchesFilter
        }

        when (selectedDbSort) {
            YouTubeDatabaseSort.NEWEST -> base.sortedByDescending { it.addedAt }
            YouTubeDatabaseSort.LARGEST -> base.sortedByDescending { it.sizeBytes }
            YouTubeDatabaseSort.LONGEST -> base.sortedByDescending { it.durationSeconds }
            YouTubeDatabaseSort.NAME_ASC -> base.sortedBy { it.name.lowercase(Locale.ROOT) }
        }
    }

    // Smart Search filtered & sorted audio feed (matches name, extension .mp3/.wav/.flac, formatted size MB/KB, and playlist/folder)
    val filteredAudioTracks = remember(
        audioTracks,
        searchQuery,
        selectedDbFilter,
        selectedDbSort
    ) {
        val q = searchQuery.trim()
        val base = audioTracks.filter { item ->
            val folderLabel = item.playlistFolderLabel()
            val sizeLabel = formatFileSize(item.sizeBytes)
            val matchesQuery = q.isBlank() ||
                item.name.contains(q, ignoreCase = true) ||
                item.extension.contains(q, ignoreCase = true) ||
                "${item.name}.${item.extension}".contains(q, ignoreCase = true) ||
                sizeLabel.contains(q, ignoreCase = true) ||
                folderLabel.contains(q, ignoreCase = true)

            val extLower = item.extension.lowercase(Locale.ROOT)
            val matchesFilter = when (selectedDbFilter) {
                YouTubeDatabaseFilter.ALL -> true
                YouTubeDatabaseFilter.UNLOCKED -> !item.isEncrypted
                YouTubeDatabaseFilter.ENCRYPTED -> item.isEncrypted
                YouTubeDatabaseFilter.FORMAT_PRIMARY -> extLower == "mp3" || extLower == "wav" || extLower == "flac" || extLower == "m4a"
                YouTubeDatabaseFilter.LARGE_FILE -> item.sizeBytes >= 3L * 1024L * 1024L
                YouTubeDatabaseFilter.FAVORITES -> folderLabel == "Favorit"
                YouTubeDatabaseFilter.RECENT -> true
            }
            matchesQuery && matchesFilter
        }

        when (selectedDbSort) {
            YouTubeDatabaseSort.NEWEST -> base.sortedByDescending { it.addedAt }
            YouTubeDatabaseSort.LARGEST -> base.sortedByDescending { it.sizeBytes }
            YouTubeDatabaseSort.LONGEST -> base.sortedByDescending { it.durationSeconds }
            YouTubeDatabaseSort.NAME_ASC -> base.sortedBy { it.name.lowercase(Locale.ROOT) }
        }
    }

    val infiniteTransition = rememberInfiniteTransition(label = "yt_equalizer")
    val eqPhase by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 6.283f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1100),
            repeatMode = RepeatMode.Restart
        ),
        label = "eqPhase"
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .testTag("media_player_list"),
        contentPadding = PaddingValues(bottom = 148.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        // 1. 6-THEME HEADER BAR WITH EXTRA-SMALL COMFORTABLE BUTTONS
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 6.dp)
                    .duolingoCard(
                        backgroundColor = YtCardWhite,
                        borderColor = YtBorder,
                        lipColor = com.example.ui.theme.DuoHareLip,
                        cornerRadius = 18.dp,
                        borderWidth = 2.dp,
                        lipHeight = 4.dp
                    )
                    .padding(bottom = 4.dp)
                    .padding(horizontal = 12.dp, vertical = 8.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Left: Compact Mode Switcher Pills (Video vs Audio) + DB Badge
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(5.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .clip(RoundedCornerShape(10.dp))
                                .background(YtChipBg)
                                .border(1.dp, YtBorder, RoundedCornerShape(10.dp))
                                .padding(2.dp),
                            horizontalArrangement = Arrangement.spacedBy(3.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            MediaDeckSubTab.entries.forEach { tab ->
                                val isSelected = selectedSubTab == tab
                                val count = if (tab == MediaDeckSubTab.VIDEO_YOUTUBE) videoTracks.size else audioTracks.size
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(
                                            if (isSelected) {
                                                if (tab == MediaDeckSubTab.VIDEO_YOUTUBE) YtCyan else VaultLime
                                            } else {
                                                Color.Transparent
                                            }
                                        )
                                        .clickable { selectedSubTab = tab }
                                        .padding(horizontal = 9.dp, vertical = 5.dp)
                                        .testTag("media_subtab_${tab.name.lowercase()}"),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                                    ) {
                                        Icon(
                                            imageVector = if (tab == MediaDeckSubTab.VIDEO_YOUTUBE) {
                                                Icons.Default.Movie
                                            } else {
                                                Icons.Default.Headphones
                                            },
                                            contentDescription = tab.label,
                                            tint = if (isSelected) Color.White else YtSubtle,
                                            modifier = Modifier.size(13.dp)
                                        )
                                        Text(
                                            text = "${tab.label} ($count)",
                                            fontSize = 11.sp,
                                            color = if (isSelected) Color.White else YtInk,
                                            fontWeight = FontWeight.ExtraBold
                                        )
                                    }
                                }
                            }
                        }

                        Surface(
                            color = YtChipBg,
                            shape = RoundedCornerShape(6.dp),
                            border = BorderStroke(1.dp, YtBorder)
                        ) {
                            Text(
                                text = "DB #$activeVaultId",
                                fontFamily = JetBrainsMonoFontFamily,
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                color = YtSubtle,
                                modifier = Modifier.padding(horizontal = 5.dp, vertical = 3.dp)
                            )
                        }
                    }

                    // Right: Extra-Small Action Buttons (Smart Search Toggle, Impor)
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(5.dp)
                    ) {
                        // Format Indicator Pill (.MOV / .MP4 / .MP3 Support)
                        Surface(
                            color = YtCyan.copy(alpha = 0.14f),
                            shape = RoundedCornerShape(8.dp),
                            border = BorderStroke(1.dp, YtCyan)
                        ) {
                            Text(
                                text = if (selectedSubTab == MediaDeckSubTab.VIDEO_YOUTUBE) "1 VIDEO PLAYER (.MOV/.MP4)" else "1 AUDIO PLAYER",
                                fontSize = 9.sp,
                                color = YtCyan,
                                fontWeight = FontWeight.ExtraBold,
                                modifier = Modifier.padding(horizontal = 7.dp, vertical = 4.dp)
                            )
                        }

                        // Extra-Small Search Button
                        Box(
                            modifier = Modifier
                                .size(28.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (isSearchExpanded) YtCyan.copy(alpha = 0.16f) else YtChipBg)
                                .border(1.dp, if (isSearchExpanded) YtCyan else YtBorder, RoundedCornerShape(8.dp))
                                .clickable { isSearchExpanded = !isSearchExpanded }
                                .testTag("yt_search_toggle_button"),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = if (isSearchExpanded) Icons.Default.Close else Icons.Default.Search,
                                contentDescription = "Smart Search Video & Audio",
                                tint = if (isSearchExpanded) YtCyan else YtInk,
                                modifier = Modifier.size(14.dp)
                            )
                        }

                        // Extra-Small Import Button
                        Box(
                            modifier = Modifier
                                .size(28.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(VaultLime.copy(alpha = 0.16f))
                                .border(1.dp, VaultLime.copy(alpha = 0.45f), RoundedCornerShape(8.dp))
                                .clickable { onImportMediaOrZipClick() }
                                .testTag("media_deck_import_button"),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.UploadFile,
                                contentDescription = "Impor Media ke Database",
                                tint = VaultLime,
                                modifier = Modifier.size(14.dp)
                            )
                        }
                    }
                }

                // Expandable Smart Search Input Bar (Video & Audio)
                androidx.compose.animation.AnimatedVisibility(visible = isSearchExpanded) {
                    Column {
                        Spacer(modifier = Modifier.height(6.dp))
                        OutlinedTextField(
                            value = searchQuery,
                            onValueChange = { searchQuery = it },
                            placeholder = {
                                Text(
                                    text = if (selectedSubTab == MediaDeckSubTab.VIDEO_YOUTUBE) {
                                        "Smart Search Video: nama, .mov/.mp4/.mkv, ukuran MB/KB, playlist..."
                                    } else {
                                        "Smart Search Audio: nama lagu, .mp3/.wav/.flac, ukuran MB/KB, folder..."
                                    },
                                    fontSize = 12.sp,
                                    color = YtSubtle,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.Search,
                                    contentDescription = null,
                                    tint = YtSubtle,
                                    modifier = Modifier.size(16.dp)
                                )
                            },
                            trailingIcon = {
                                if (searchQuery.isNotEmpty() || selectedDbFilter != YouTubeDatabaseFilter.ALL) {
                                    IconButton(
                                        onClick = {
                                            searchQuery = ""
                                            selectedDbFilter = YouTubeDatabaseFilter.ALL
                                        },
                                        modifier = Modifier.size(24.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Close,
                                            contentDescription = "Hapus pencarian",
                                            tint = YtSubtle,
                                            modifier = Modifier.size(14.dp)
                                        )
                                    }
                                }
                            },
                            singleLine = true,
                            shape = RoundedCornerShape(12.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = YtCyan,
                                unfocusedBorderColor = YtBorder,
                                focusedContainerColor = YtChipBg,
                                unfocusedContainerColor = YtChipBg,
                                focusedTextColor = YtInk,
                                unfocusedTextColor = YtInk
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("yt_database_search_input")
                        )
                    }
                }

                Spacer(modifier = Modifier.height(6.dp))

                // Compact Filter & Playlist Chips Row
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Auto Unlock All & Auto Lock Quick Action Chips in Video Player
                    if (lockedVideoCount > 0) {
                        item {
                            Surface(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .clickable { onBatchEncryptAllWithPin(false) }
                                    .testTag("yt_auto_unlock_all_button"),
                                color = VaultLime,
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 9.dp, vertical = 4.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.LockOpen,
                                        contentDescription = "Auto Unlock All Video",
                                        tint = Color.White,
                                        modifier = Modifier.size(13.dp)
                                    )
                                    Text(
                                        text = "Auto Unlock All ($lockedVideoCount)",
                                        fontSize = 11.sp,
                                        color = Color.White,
                                        fontWeight = FontWeight.ExtraBold
                                    )
                                }
                            }
                        }
                    }

                    if (unlockedVideoCount > 0) {
                        item {
                            Surface(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .clickable { onBatchEncryptAllWithPin(true) }
                                    .testTag("yt_auto_lock_all_button"),
                                color = VaultAmber,
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 9.dp, vertical = 4.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Lock,
                                        contentDescription = "Auto Lock Semua Video",
                                        tint = Color.White,
                                        modifier = Modifier.size(13.dp)
                                    )
                                    Text(
                                        text = "Kunci Semua ($unlockedVideoCount)",
                                        fontSize = 11.sp,
                                        color = Color.White,
                                        fontWeight = FontWeight.ExtraBold
                                    )
                                }
                            }
                        }
                    }

                    // Database Status Filter Chips
                    items(YouTubeDatabaseFilter.entries) { filter ->
                        val selected = selectedDbFilter == filter
                        Surface(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .clickable { selectedDbFilter = filter }
                                .testTag("yt_filter_chip_${filter.name.lowercase()}"),
                            color = if (selected) YtInk else YtChipBg,
                            shape = RoundedCornerShape(8.dp),
                            border = BorderStroke(
                                width = 1.dp,
                                color = if (selected) YtInk else YtBorder
                            )
                        ) {
                            Text(
                                text = filter.label,
                                fontSize = 11.sp,
                                color = if (selected) Color.White else YtInk,
                                fontWeight = if (selected) FontWeight.ExtraBold else FontWeight.SemiBold,
                                modifier = Modifier.padding(horizontal = 9.dp, vertical = 4.dp)
                            )
                        }
                    }

                    // + Buat Playlist / Sub-Playlist Baru Chip
                    item {
                        Surface(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .clickable {
                                    createSubPlaylistParent = activePlaylistPath
                                    showCreatePlaylistModal = true
                                }
                                .testTag("create_media_playlist_chip"),
                            color = YtCardWhite,
                            shape = RoundedCornerShape(8.dp),
                            border = BorderStroke(1.dp, YtCyan)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 9.dp, vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(3.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.CreateNewFolder,
                                    contentDescription = "Playlist Baru",
                                    tint = YtCyan,
                                    modifier = Modifier.size(13.dp)
                                )
                                Text(
                                    text = if (activePlaylistPath == null) "+ Playlist" else "+ Sub-Playlist",
                                    fontSize = 11.sp,
                                    color = YtCyan,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }
        }

        // 2. MAIN YOUTUBE PLAYER & WATCH PAGE OR SONIC AUDIO DECK
        if (selectedSubTab == MediaDeckSubTab.VIDEO_YOUTUBE) {
            // A. 16:9 YOUTUBE THEATER VIDEO STAGE (Edge-to-Edge Dark Theater Canvas)
            item {
                YouTubeStyleVideoStage(
                    videoItem = displayedVideoTrack,
                    mediaState = mediaState,
                    isFullscreenOverlayOpen = isFullscreenOverlayOpen || isFullscreenCinemaActive,
                    isTheaterMode = isTheaterMode,
                    isAutoplayEnabled = isAutoplayEnabled,
                    isSubtitlesHudEnabled = isSubtitlesHudEnabled,
                    showStatsForNerds = showStatsForNerds,
                    isPlayerScreenLocked = isPlayerScreenLocked,
                    selectedVideoQualityLabel = selectedVideoQualityLabel,
                    onToggleTheaterMode = { isTheaterMode = !isTheaterMode },
                    onToggleAutoplay = { isAutoplayEnabled = !isAutoplayEnabled },
                    onToggleSubtitlesHud = { isSubtitlesHudEnabled = !isSubtitlesHudEnabled },
                    onToggleStatsForNerds = { showStatsForNerds = !showStatsForNerds },
                    onTogglePlayerLock = { isPlayerScreenLocked = !isPlayerScreenLocked },
                    onOpenSettingsSheet = { showYouTubeSettingsSheet = true },
                    onSetHold2xSpeed = onSetHold2xSpeed,
                    onTogglePlayPause = {
                        if (displayedVideoTrack != null) {
                            if (mediaState.currentTrack?.id != displayedVideoTrack.id) {
                                onSelectOrOpenMedia(displayedVideoTrack)
                            } else {
                                onTogglePlayPause()
                            }
                        }
                    },
                    onSeekToSeconds = { sec ->
                        if (displayedVideoTrack != null && mediaState.currentTrack?.id != displayedVideoTrack.id) {
                            onSelectOrOpenMedia(displayedVideoTrack)
                        }
                        onSeekToSeconds(sec)
                    },
                    onSeekToPositionMs = { posMs ->
                        if (displayedVideoTrack != null && mediaState.currentTrack?.id != displayedVideoTrack.id) {
                            onSelectOrOpenMedia(displayedVideoTrack)
                        }
                        onSeekToPositionMs(posMs)
                    },
                    onUpdateActualDurationMs = onUpdateActualDurationMs,
                    onSyncHardwarePositionMs = onSyncHardwarePositionMs,
                    onSkipTrack = onSkipTrack,
                    onCycleSpeed = onCycleSpeed,
                    onToggleRepeat = onToggleRepeat,
                    onOpenFullscreen = {
                        if (displayedVideoTrack != null) {
                            if (displayedVideoTrack.isEncrypted) {
                                onRequestPinToggleEncrypt(displayedVideoTrack)
                            } else {
                                if (mediaState.currentTrack?.id != displayedVideoTrack.id) {
                                    onSelectOrOpenMedia(displayedVideoTrack)
                                }
                                isFullscreenCinemaActive = true
                            }
                        }
                    },
                    onRequestUnlockEncrypted = {
                        if (displayedVideoTrack != null) {
                            onRequestPinToggleEncrypt(displayedVideoTrack)
                        }
                    },
                    onOpenDatabaseClick = { isSearchExpanded = true },
                    onImportVideoClick = onImportMediaOrZipClick
                )
            }

            // B. YOUTUBE WATCH METADATA, CHANNEL / DATABASE BAR, ACTION PILLS & DESCRIPTION
            if (displayedVideoTrack != null) {
                item {
                    YouTubeWatchMetadataSection(
                        activeVaultId = activeVaultId,
                        videoItem = displayedVideoTrack,
                        totalDatabaseVideoCount = allVideoTracks.size,
                        mediaState = mediaState,
                        isLiked = likedVideosMap[displayedVideoTrack.id] == true,
                        isDisliked = dislikedVideosMap[displayedVideoTrack.id] == true,
                        isDescriptionExpanded = isDescriptionExpanded,
                        isCommentsExpanded = isCommentsExpanded,
                        videoNotes = videoNotes.filter {
                            it.videoId == displayedVideoTrack.id || it.videoId == -1
                        },
                        newCommentInput = newCommentInput,
                        onNewCommentInputChange = { newCommentInput = it },
                        onAddTimestampNote = { noteText ->
                            val clean = noteText.trim()
                            if (clean.isNotEmpty()) {
                                videoNotes.add(
                                    0,
                                    LocalVideoTimestampNote(
                                        id = System.currentTimeMillis(),
                                        videoId = displayedVideoTrack.id,
                                        timestampSeconds = mediaState.currentSeconds,
                                        text = clean
                                    )
                                )
                                newCommentInput = ""
                            }
                        },
                        onAddQuickClipBookmark = {
                            val sec = mediaState.currentSeconds
                            videoNotes.add(
                                0,
                                LocalVideoTimestampNote(
                                    id = System.currentTimeMillis(),
                                    videoId = displayedVideoTrack.id,
                                    timestampSeconds = sec,
                                    text = "Klip ditandai pada ${formatSeconds(sec)} (${displayedVideoTrack.name})"
                                )
                            )
                            isCommentsExpanded = true
                        },
                        onSeekToSeconds = onSeekToSeconds,
                        onToggleLike = {
                            val current = likedVideosMap[displayedVideoTrack.id] == true
                            likedVideosMap[displayedVideoTrack.id] = !current
                            if (!current) dislikedVideosMap[displayedVideoTrack.id] = false
                        },
                        onToggleDislike = {
                            val current = dislikedVideosMap[displayedVideoTrack.id] == true
                            dislikedVideosMap[displayedVideoTrack.id] = !current
                            if (!current) likedVideosMap[displayedVideoTrack.id] = false
                        },
                        onToggleDescription = { isDescriptionExpanded = !isDescriptionExpanded },
                        onToggleComments = { isCommentsExpanded = !isCommentsExpanded },
                        onOpenDatabaseModal = { isSearchExpanded = true },
                        onSaveToPlaylistClick = { itemToMovePlaylist = displayedVideoTrack },
                        onToggleEncryptPinClick = { onRequestPinToggleEncrypt(displayedVideoTrack) },
                        onShareClick = { onRequestExportOrShareWithBiometric(displayedVideoTrack, true) },
                        onDownloadExportClick = { onRequestExportOrShareWithBiometric(displayedVideoTrack, false) },
                        onOpenPlayerSettingsClick = { showYouTubeSettingsSheet = true },
                        onToggleStatsForNerds = { showStatsForNerds = !showStatsForNerds },
                        onImportVideoClick = onImportMediaOrZipClick
                    )
                }
            }

            // B2. HIERARCHICAL VIDEO PLAYLISTS & SUB-PLAYLISTS SECTION (1x Click Full Name, 2x Click Open, Back Button)
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
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
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.PlaylistPlay,
                                contentDescription = null,
                                tint = YtPurple,
                                modifier = Modifier.size(19.dp)
                            )
                            Text(
                                text = if (activePlaylistPath == null) {
                                    "Playlist Video & Folder ZIP (${visibleChildVideoPlaylists.size})"
                                } else {
                                    "Playlist Aktif: $activePlaylistPath"
                                },
                                style = MaterialTheme.typography.titleSmall,
                                color = YtInk,
                                fontWeight = FontWeight.ExtraBold,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }

                        Surface(
                            modifier = Modifier
                                .clip(RoundedCornerShape(10.dp))
                                .clickable {
                                    createSubPlaylistParent = activePlaylistPath
                                    showCreatePlaylistModal = true
                                }
                                .testTag("playlist_section_add_button"),
                            color = YtCyan.copy(alpha = 0.14f),
                            shape = RoundedCornerShape(10.dp),
                            border = BorderStroke(1.dp, YtCyan)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.CreateNewFolder,
                                    contentDescription = null,
                                    tint = YtCyan,
                                    modifier = Modifier.size(14.dp)
                                )
                                Text(
                                    text = if (activePlaylistPath == null) "+ Playlist" else "+ Sub-Playlist",
                                    fontSize = 11.sp,
                                    color = YtCyan,
                                    fontWeight = FontWeight.ExtraBold,
                                    maxLines = 1
                                )
                            }
                        }
                    }

                    // Prominent Back / Keluar Playlist Bar when inside a Playlist or Sub-Playlist
                    if (activePlaylistPath != null) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .horizontalScroll(rememberScrollState()),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Surface(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(12.dp))
                                    .clickable {
                                        val parent = activePlaylistPath.substringBeforeLast('/', "").takeIf { it.isNotEmpty() }
                                        expandedPlaylistNameKey = null
                                        onSelectMediaFolder(parent)
                                    }
                                    .testTag("playlist_back_button"),
                                color = YtCyan,
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 7.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(5.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                        contentDescription = "Kembali / Keluar Playlist",
                                        tint = Color.White,
                                        modifier = Modifier.size(15.dp)
                                    )
                                    Text(
                                        text = if (activePlaylistPath.contains('/')) {
                                            "Kembali ke Playlist Induk"
                                        } else {
                                            "Keluar Playlist"
                                        },
                                        fontSize = 11.sp,
                                        color = Color.White,
                                        fontWeight = FontWeight.ExtraBold,
                                        maxLines = 1
                                    )
                                }
                            }

                            if (activePlaylistPath.contains('/')) {
                                Surface(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(12.dp))
                                        .clickable {
                                            expandedPlaylistNameKey = null
                                            onSelectMediaFolder(null)
                                        }
                                        .testTag("playlist_exit_root_button"),
                                    color = YtCardWhite,
                                    shape = RoundedCornerShape(12.dp),
                                    border = BorderStroke(1.dp, YtBorder)
                                ) {
                                    Row(
                                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 7.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Close,
                                            contentDescription = "Semua Playlist",
                                            tint = YtInk,
                                            modifier = Modifier.size(14.dp)
                                        )
                                        Text(
                                            text = "Semua Playlist",
                                            fontSize = 11.sp,
                                            color = YtInk,
                                            fontWeight = FontWeight.Bold,
                                            maxLines = 1
                                        )
                                    }
                                }
                            }

                            Surface(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(12.dp))
                                    .clickable {
                                        playlistTargetToRename = activePlaylistPath
                                        renamePlaylistInput = activePlaylistPath
                                        showRenamePlaylistModal = true
                                    }
                                    .testTag("rename_active_playlist_chip"),
                                color = YtCardWhite,
                                shape = RoundedCornerShape(12.dp),
                                border = BorderStroke(1.dp, YtPurple)
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 7.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Tune,
                                        contentDescription = "Ganti Nama Playlist",
                                        tint = YtPurple,
                                        modifier = Modifier.size(14.dp)
                                    )
                                    Text(
                                        text = "Ganti Nama Playlist",
                                        fontSize = 11.sp,
                                        color = YtPurple,
                                        fontWeight = FontWeight.Bold,
                                        maxLines = 1
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Render Top-Level Playlists (at root) or Sub-Playlists (when inside a playlist)
            if (visibleChildVideoPlaylists.isNotEmpty()) {
                items(visibleChildVideoPlaylists, key = { "vid_playlist_$it" }) { playlistPath ->
                    val videosInTree = remember(allVideoTracks, playlistPath) {
                        allVideoTracks.filter {
                            val p = normalizePlaylistPath(it.groupArchiveName)
                            p != null && (
                                p.equals(playlistPath, ignoreCase = true) ||
                                    p.startsWith("$playlistPath/", ignoreCase = true)
                                )
                        }
                    }
                    val subPlaylistsCount = remember(allVideoPlaylistPaths, playlistPath) {
                        val prefix = "$playlistPath/"
                        allVideoPlaylistPaths
                            .filter { it.startsWith(prefix, ignoreCase = true) && it.length > prefix.length }
                            .map { it.substring(prefix.length).substringBefore('/') }
                            .distinct()
                            .size
                    }
                    val lockedInPlaylist = remember(videosInTree) { videosInTree.count { it.isEncrypted } }
                    val playlistKey = "vid_playlist_$playlistPath"
                    val isFullNameExpanded = expandedPlaylistNameKey == playlistKey

                    Box(modifier = Modifier.padding(horizontal = 16.dp)) {
                        VideoPlaylistFolderCard(
                            playlistDisplayName = playlistPath.substringAfterLast('/'),
                            fullPlaylistPath = playlistPath,
                            videoCount = videosInTree.size,
                            subPlaylistCount = subPlaylistsCount,
                            lockedCount = lockedInPlaylist,
                            totalBytes = videosInTree.sumOf { it.sizeBytes },
                            isFullNameExpanded = isFullNameExpanded,
                            onSingleClickToggleName = {
                                expandedPlaylistNameKey = if (isFullNameExpanded) null else playlistKey
                            },
                            onDoubleClickOpenPlaylist = {
                                expandedPlaylistNameKey = null
                                onSelectMediaFolder(playlistPath)
                            },
                            onRenamePlaylistClick = {
                                playlistTargetToRename = playlistPath
                                renamePlaylistInput = playlistPath
                                showRenamePlaylistModal = true
                            }
                        )
                    }
                }
            }

            // C. "BERIKUTNYA DARI DATABASE BRANKAS" HEADER & COMPACT SORT BAR
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 14.dp, vertical = 2.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(
                            imageVector = Icons.Default.VideoLibrary,
                            contentDescription = null,
                            tint = YtCyan,
                            modifier = Modifier.size(17.dp)
                        )
                        Column {
                            Text(
                                text = if (activePlaylistPath == null) {
                                    if (searchQuery.isNotBlank()) {
                                        "Hasil Pencarian Video (${filteredVideoFeed.size})"
                                    } else {
                                        "Video Mandiri Tanpa Playlist (${filteredVideoFeed.size})"
                                    }
                                } else {
                                    "Video di Playlist '${activePlaylistPath.substringAfterLast('/')}' (${filteredVideoFeed.size})"
                                },
                                style = MaterialTheme.typography.titleSmall,
                                color = YtInk,
                                fontWeight = FontWeight.ExtraBold,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            Text(
                                text = "Klik 1x Playlist: nama penuh • Klik 2x Playlist: buka isi video",
                                fontSize = 10.sp,
                                color = YtSubtle,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }

                    // Compact Cycle Sort Order Chip
                    Surface(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .clickable {
                                val entries = YouTubeDatabaseSort.entries
                                val nextIndex = (entries.indexOf(selectedDbSort) + 1) % entries.size
                                selectedDbSort = entries[nextIndex]
                            }
                            .testTag("yt_sort_chip"),
                        color = YtCardWhite,
                        shape = RoundedCornerShape(8.dp),
                        border = BorderStroke(1.dp, YtBorder)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Sort,
                                contentDescription = "Urutkan",
                                tint = YtInk,
                                modifier = Modifier.size(13.dp)
                            )
                            Text(
                                text = selectedDbSort.label,
                                fontSize = 11.sp,
                                color = YtInk,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }

            // D. YOUTUBE UP-NEXT VIDEO FEED CARDS (FROM LOCAL VAULT DATABASE)
            if (filteredVideoFeed.isEmpty()) {
                item {
                    Box(modifier = Modifier.padding(horizontal = 16.dp)) {
                        EmptyMediaCategoryCard(
                            title = if (activePlaylistPath == null && visibleChildVideoPlaylists.isNotEmpty() && searchQuery.isBlank()) {
                                "Video Tersimpan di Dalam Playlist"
                            } else if (activePlaylistPath != null) {
                                "Belum Ada Video di Playlist Ini"
                            } else {
                                "Tidak Ada Video yang Cocok di Database"
                            },
                            subtitle = if (activePlaylistPath == null && visibleChildVideoPlaylists.isNotEmpty() && searchQuery.isBlank()) {
                                "Klik 2x pada kartu Playlist di atas untuk membuka dan memutar video di dalamnya."
                            } else {
                                "Ketuk 'Impor Video' atau pindahkan video ke playlist ini untuk memutarnya."
                            },
                            onImportClick = onImportMediaOrZipClick
                        )
                    }
                }
            } else {
                itemsIndexed(filteredVideoFeed, key = { _, item -> "yt_feed_${item.id}" }) { index, item ->
                    val isSelected = item.id == displayedVideoTrack?.id
                    Box(modifier = Modifier.padding(horizontal = 16.dp)) {
                        YouTubeUpNextVideoCard(
                            index = index + 1,
                            activeVaultId = activeVaultId,
                            item = item,
                            isSelected = isSelected,
                            isPlaying = isSelected && mediaState.isPlaying,
                            isLiked = likedVideosMap[item.id] == true,
                            currentSeconds = if (isSelected) mediaState.currentSeconds else 0,
                            onPlayClick = { onSelectOrOpenMedia(item) },
                            onFullscreenClick = {
                                if (item.isEncrypted) {
                                    onRequestPinToggleEncrypt(item)
                                } else {
                                    onSelectOrOpenMedia(item)
                                    isFullscreenCinemaActive = true
                                }
                            },
                            onMoveToPlaylistClick = { itemToMovePlaylist = item },
                            onTogglePinLock = { onRequestPinToggleEncrypt(item) },
                            onShareBiometric = { onRequestExportOrShareWithBiometric(item, true) },
                            onExportBiometric = { onRequestExportOrShareWithBiometric(item, false) }
                        )
                    }
                }
            }
        } else {
            // SONIC AUDIO DECK MODE (1 Unified Audio Player — Spotify Proportions + Duolingo 3D)
            item {
                Box(modifier = Modifier.padding(horizontal = 16.dp)) {
                    SonicAudioDeckHeroCard(
                        audioItem = displayedAudioTrack,
                        mediaState = mediaState,
                        eqPhase = eqPhase,
                        onTogglePlayPause = {
                            if (displayedAudioTrack != null) {
                                if (mediaState.currentTrack?.id != displayedAudioTrack.id) {
                                    onSelectOrOpenMedia(displayedAudioTrack)
                                } else {
                                    onTogglePlayPause()
                                }
                            }
                        },
                        onSeekToSeconds = onSeekToSeconds,
                        onSkipTrack = onSkipTrack,
                        onCycleSpeed = onCycleSpeed,
                        onToggleRepeat = onToggleRepeat,
                        onImportClick = onImportMediaOrZipClick
                    )
                }
            }

            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (searchQuery.isNotBlank() || selectedDbFilter != YouTubeDatabaseFilter.ALL) {
                            "Hasil Smart Search Audio (${filteredAudioTracks.size}/${audioTracks.size})"
                        } else {
                            "Antrean Musik & Audio (${filteredAudioTracks.size})"
                        },
                        style = MaterialTheme.typography.titleMedium,
                        color = YtInk,
                        fontWeight = FontWeight.ExtraBold
                    )
                    Text(
                        text = "1 Audio Player Aktif",
                        style = MaterialTheme.typography.labelMedium,
                        color = VaultLime,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            if (filteredAudioTracks.isEmpty()) {
                item {
                    Box(modifier = Modifier.padding(horizontal = 16.dp)) {
                        EmptyMediaCategoryCard(
                            title = if (searchQuery.isNotBlank() || selectedDbFilter != YouTubeDatabaseFilter.ALL) {
                                "Tidak Ada Lagu yang Cocok dengan Filter"
                            } else {
                                "Belum Ada Lagu / Audio di Playlist Ini"
                            },
                            subtitle = "Impor file MP3, WAV, FLAC, atau M4A untuk memutar musik dengan equalizer.",
                            onImportClick = onImportMediaOrZipClick
                        )
                    }
                }
            } else {
                itemsIndexed(filteredAudioTracks, key = { _, item -> "aud_${item.id}" }) { index, item ->
                    val isSelected = item.id == displayedAudioTrack?.id
                    Box(modifier = Modifier.padding(horizontal = 16.dp)) {
                        MediaTrackRowCard(
                            index = index + 1,
                            item = item,
                            isSelected = isSelected,
                            isPlaying = isSelected && mediaState.isPlaying,
                            onPlayClick = { onSelectOrOpenMedia(item) },
                            onOpenViewerClick = {
                                if (item.isEncrypted) {
                                    onRequestPinToggleEncrypt(item)
                                } else {
                                    onSelectOrOpenMedia(item)
                                }
                            },
                            onMoveToPlaylistClick = { itemToMovePlaylist = item },
                            onTogglePinLock = { onRequestPinToggleEncrypt(item) },
                            onShareBiometric = { onRequestExportOrShareWithBiometric(item, true) },
                            onExportBiometric = { onRequestExportOrShareWithBiometric(item, false) }
                        )
                    }
                }
            }
        }
    }

    // FULLSCREEN MODE OF THE 1 UNIFIED VIDEO PLAYER (Supports .MOV, .MP4, .MKV, .WEBM)
    if (isFullscreenCinemaActive && displayedVideoTrack != null && !displayedVideoTrack.isEncrypted) {
        Dialog(
            onDismissRequest = { isFullscreenCinemaActive = false },
            properties = DialogProperties(
                usePlatformDefaultWidth = false,
                decorFitsSystemWindows = false
            )
        ) {
            CinemaVideoPlayerViewport(
                item = displayedVideoTrack,
                mediaState = mediaState,
                onClose = { isFullscreenCinemaActive = false },
                onRequestReEncryptWithPin = {
                    isFullscreenCinemaActive = false
                    onRequestPinToggleEncrypt(displayedVideoTrack)
                },
                onRequestExportOrShareWithBiometric = { isShare ->
                    onRequestExportOrShareWithBiometric(displayedVideoTrack, isShare)
                },
                onTogglePlayPause = onTogglePlayPause,
                onSeekSeconds = onSeekToSeconds,
                onSeekPositionMs = onSeekToPositionMs,
                onUpdateActualDurationMs = onUpdateActualDurationMs,
                onSyncHardwarePositionMs = onSyncHardwarePositionMs,
                onSkipTrack = onSkipTrack,
                onSetHold2xSpeed = onSetHold2xSpeed,
                onCycleSpeed = onCycleSpeed
            )
        }
    }

    // MODAL 2: YOUTUBE PLAYER SETTINGS BOTTOM SHEET / DIALOG (Quality, Speed, Repeat, Lock, Stats)
    if (showYouTubeSettingsSheet) {
        YouTubePlayerSettingsDialog(
            mediaState = mediaState,
            selectedQuality = selectedVideoQualityLabel,
            isAutoplayEnabled = isAutoplayEnabled,
            isSubtitlesHudEnabled = isSubtitlesHudEnabled,
            showStatsForNerds = showStatsForNerds,
            isPlayerScreenLocked = isPlayerScreenLocked,
            onSelectQuality = { selectedVideoQualityLabel = it },
            onCycleSpeed = onCycleSpeed,
            onToggleRepeat = onToggleRepeat,
            onToggleAutoplay = { isAutoplayEnabled = !isAutoplayEnabled },
            onToggleSubtitlesHud = { isSubtitlesHudEnabled = !isSubtitlesHudEnabled },
            onToggleStatsForNerds = { showStatsForNerds = !showStatsForNerds },
            onToggleLockScreen = {
                isPlayerScreenLocked = !isPlayerScreenLocked
                showYouTubeSettingsSheet = false
            },
            onDismiss = { showYouTubeSettingsSheet = false }
        )
    }

    // MODAL 3: BUAT PLAYLIST / SUB-PLAYLIST BARU
    if (showCreatePlaylistModal) {
        val parentPath = createSubPlaylistParent
        Dialog(onDismissRequest = { showCreatePlaylistModal = false }) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = YtCardWhite),
                border = BorderStroke(2.dp, YtBorder)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.PlaylistAdd,
                            contentDescription = null,
                            tint = VaultCoral
                        )
                        Text(
                            text = if (parentPath == null) "Buat Playlist Video Baru" else "Buat Sub-Playlist di '$parentPath'",
                            style = MaterialTheme.typography.titleMedium,
                            color = YtInk,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = if (parentPath == null) {
                            "Masukkan nama playlist baru (bisa berisi video maupun sub-playlist lagi di dalamnya):"
                        } else {
                            "Sub-playlist ini akan berada di dalam playlist '$parentPath':"
                        },
                        style = MaterialTheme.typography.bodySmall,
                        color = YtSubtle
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    OutlinedTextField(
                        value = newPlaylistInput,
                        onValueChange = { newPlaylistInput = it },
                        label = { Text(if (parentPath == null) "Nama Playlist" else "Nama Sub-Playlist") },
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = VaultCoral,
                            unfocusedBorderColor = YtBorder,
                            focusedTextColor = YtInk,
                            unfocusedTextColor = YtInk
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        Surface(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .clickable { showCreatePlaylistModal = false },
                            color = YtChipBg
                        ) {
                            Text(
                                text = "Batal",
                                style = MaterialTheme.typography.labelLarge,
                                color = YtInk,
                                modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Surface(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .clickable {
                                    val clean = newPlaylistInput.removeSuffix(".zip").removeSuffix(".ZIP").trim('/').trim()
                                    if (clean.isNotEmpty()) {
                                        val fullCreated = if (parentPath != null) "$parentPath/$clean" else clean
                                        if (!userCreatedPlaylists.contains(fullCreated)) {
                                            userCreatedPlaylists.add(fullCreated)
                                        }
                                        onSelectMediaFolder(fullCreated)
                                        newPlaylistInput = ""
                                        showCreatePlaylistModal = false
                                    }
                                },
                            color = VaultCoral
                        ) {
                            Text(
                                text = "Buat & Buka",
                                style = MaterialTheme.typography.labelLarge,
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp)
                            )
                        }
                    }
                }
            }
        }
    }

    // MODAL 4: SIMPAN VIDEO KE PLAYLIST / SUB-PLAYLIST
    itemToMovePlaylist?.let { targetItem ->
        Dialog(onDismissRequest = { itemToMovePlaylist = null }) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = YtCardWhite),
                border = BorderStroke(2.dp, YtBorder)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.PlaylistAdd,
                            contentDescription = null,
                            tint = VaultCoral
                        )
                        Text(
                            text = "Simpan ke Playlist / Sub-Playlist",
                            style = MaterialTheme.typography.titleMedium,
                            color = YtInk,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "${targetItem.name}.${targetItem.extension}",
                        style = MaterialTheme.typography.bodySmall,
                        color = YtSubtle
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    val moveChoices = remember(allVideoPlaylistPaths, activePlaylistPath) {
                        val base = mutableListOf("Root Brankas (Tanpa Folder)")
                        if (activePlaylistPath != null) base.add(activePlaylistPath)
                        base.addAll(allVideoPlaylistPaths)
                        base.distinct()
                    }

                    moveChoices.take(6).forEach { folder ->
                        val isRootOption = folder == "Root Brankas (Tanpa Folder)"
                        val currentNorm = normalizePlaylistPath(targetItem.groupArchiveName)
                        val isCurrent = if (isRootOption) currentNorm == null else currentNorm.equals(folder, ignoreCase = true)
                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .clickable {
                                    onMoveMediaItemToPlaylist(targetItem, if (isRootOption) "" else folder)
                                    itemToMovePlaylist = null
                                },
                            color = if (isCurrent) VaultCoral.copy(alpha = 0.14f) else YtChipBg,
                            border = BorderStroke(
                                width = 1.dp,
                                color = if (isCurrent) VaultCoral else YtBorder
                            )
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Folder,
                                        contentDescription = null,
                                        tint = if (isCurrent) VaultCoral else YtSubtle,
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Text(
                                        text = folder,
                                        style = MaterialTheme.typography.labelLarge,
                                        color = if (isCurrent) VaultCoral else YtInk,
                                        fontWeight = if (isCurrent) FontWeight.ExtraBold else FontWeight.Medium,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                }
                                if (isCurrent) {
                                    Icon(
                                        imageVector = Icons.Default.CheckCircle,
                                        contentDescription = "Aktif",
                                        tint = VaultCoral,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))
                    OutlinedTextField(
                        value = newPlaylistInput,
                        onValueChange = { newPlaylistInput = it },
                        label = {
                            Text(
                                if (activePlaylistPath != null) {
                                    "Ketik Playlist Baru (atau '$activePlaylistPath/NamaSub')"
                                } else {
                                    "Atau ketik Playlist / Sub-Playlist Baru (mis. Anime/Season 1)"
                                }
                            )
                        },
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = VaultCoral,
                            unfocusedBorderColor = YtBorder,
                            focusedTextColor = YtInk,
                            unfocusedTextColor = YtInk
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        Surface(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .clickable { itemToMovePlaylist = null },
                            color = YtChipBg
                        ) {
                            Text(
                                text = "Tutup",
                                style = MaterialTheme.typography.labelLarge,
                                color = YtInk,
                                modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp)
                            )
                        }
                        if (newPlaylistInput.trim().isNotEmpty()) {
                            Spacer(modifier = Modifier.width(8.dp))
                            Surface(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(12.dp))
                                    .clickable {
                                        val clean = newPlaylistInput.trim().trim('/')
                                        if (!userCreatedPlaylists.contains(clean)) {
                                            userCreatedPlaylists.add(clean)
                                        }
                                        onMoveMediaItemToPlaylist(targetItem, clean)
                                        newPlaylistInput = ""
                                        itemToMovePlaylist = null
                                    },
                                color = VaultCoral
                            ) {
                                Text(
                                    text = "Simpan ke '$newPlaylistInput'",
                                    style = MaterialTheme.typography.labelLarge,
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    // Modal Ganti Nama Playlist / Folder ZIP
    val targetFolderRename = playlistTargetToRename ?: activePlaylistPath
    if (showRenamePlaylistModal && targetFolderRename != null) {
        Dialog(onDismissRequest = { showRenamePlaylistModal = false }) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("rename_playlist_modal"),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = YtCardWhite),
                border = BorderStroke(2.dp, YtPurple)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        text = "Ganti Nama Playlist / Folder ZIP",
                        style = MaterialTheme.typography.titleMedium,
                        color = YtInk,
                        fontWeight = FontWeight.ExtraBold
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Mengubah nama '$targetFolderRename' untuk semua video dan sub-playlist di dalamnya.",
                        style = MaterialTheme.typography.bodySmall,
                        color = YtSubtle
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    OutlinedTextField(
                        value = renamePlaylistInput,
                        onValueChange = { renamePlaylistInput = it },
                        label = { Text("Nama Baru Playlist / Folder") },
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("rename_playlist_input")
                    )
                    Spacer(modifier = Modifier.height(14.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        Surface(
                            modifier = Modifier
                                .clip(RoundedCornerShape(10.dp))
                                .clickable { showRenamePlaylistModal = false },
                            color = YtChipBg
                        ) {
                            Text(
                                text = "Batal",
                                style = MaterialTheme.typography.labelLarge,
                                color = YtInk,
                                modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Surface(
                            modifier = Modifier
                                .clip(RoundedCornerShape(10.dp))
                                .clickable {
                                    val clean = renamePlaylistInput.removeSuffix(".zip").removeSuffix(".ZIP").trim('/').trim()
                                    if (clean.isNotEmpty()) {
                                        onRenamePlaylistFolder(targetFolderRename, clean)
                                        val idx = userCreatedPlaylists.indexOf(targetFolderRename)
                                        if (idx >= 0) {
                                            userCreatedPlaylists[idx] = clean
                                        }
                                        if (activePlaylistPath == targetFolderRename) {
                                            onSelectMediaFolder(clean)
                                        }
                                        showRenamePlaylistModal = false
                                    }
                                }
                                .testTag("confirm_rename_playlist_button"),
                            color = YtPurple
                        ) {
                            Text(
                                text = "Simpan Nama",
                                style = MaterialTheme.typography.labelLarge,
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun VideoPlaylistFolderCard(
    playlistDisplayName: String,
    fullPlaylistPath: String,
    videoCount: Int,
    subPlaylistCount: Int,
    lockedCount: Int,
    totalBytes: Long,
    isFullNameExpanded: Boolean,
    onSingleClickToggleName: () -> Unit,
    onDoubleClickOpenPlaylist: () -> Unit,
    onRenamePlaylistClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("video_playlist_card_$fullPlaylistPath")
    ) {
        // Strictly fixed-height 74.dp box so long playlist names never stretch vertically!
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(74.dp)
                .duolingoCard(
                    backgroundColor = YtCardWhite,
                    borderColor = YtCyan,
                    lipColor = YtCyan.copy(alpha = 0.7f),
                    cornerRadius = 20.dp,
                    borderWidth = 2.dp,
                    lipHeight = 4.dp
                )
                .combinedClickable(
                    onClick = onSingleClickToggleName,
                    onDoubleClick = onDoubleClickOpenPlaylist
                )
                .padding(bottom = 4.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 14.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .clip(RoundedCornerShape(13.dp))
                        .background(YtCyan.copy(alpha = 0.15f))
                        .border(1.5.dp, YtCyan, RoundedCornerShape(13.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.PlaylistPlay,
                        contentDescription = "Playlist Video",
                        tint = YtCyan,
                        modifier = Modifier.size(24.dp)
                    )
                }

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = playlistDisplayName,
                        style = MaterialTheme.typography.titleSmall,
                        color = YtInk,
                        fontWeight = FontWeight.ExtraBold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = buildString {
                            append("$videoCount Video")
                            if (subPlaylistCount > 0) append(" • $subPlaylistCount Sub-Playlist")
                            append(" • ${formatFileSize(totalBytes)}")
                            if (lockedCount > 0) append(" • $lockedCount Terkunci")
                        },
                        style = MaterialTheme.typography.labelSmall,
                        color = YtSubtle,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(YtChipBg)
                            .border(1.dp, YtBorder, CircleShape)
                            .clickable { onRenamePlaylistClick() }
                            .testTag("rename_video_playlist_$fullPlaylistPath"),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Tune,
                            contentDescription = "Ganti Nama Playlist",
                            tint = YtInk,
                            modifier = Modifier.size(15.dp)
                        )
                    }

                    Surface(
                        modifier = Modifier
                            .clip(RoundedCornerShape(10.dp))
                            .clickable { onDoubleClickOpenPlaylist() }
                            .testTag("open_video_playlist_$fullPlaylistPath"),
                        color = YtCyan,
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text(
                            text = "2x Buka",
                            fontSize = 11.sp,
                            color = Color.White,
                            fontWeight = FontWeight.ExtraBold,
                            maxLines = 1,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                        )
                    }
                }
            }
        }

        AnimatedVisibility(
            visible = isFullNameExpanded,
            enter = fadeIn() + expandVertically(),
            exit = fadeOut() + shrinkVertically()
        ) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp, start = 6.dp, end = 6.dp)
                    .combinedClickable(
                        onClick = onSingleClickToggleName,
                        onDoubleClick = onDoubleClickOpenPlaylist
                    )
                    .testTag("video_playlist_full_name_$fullPlaylistPath"),
                shape = RoundedCornerShape(12.dp),
                color = YouTubeDarkCanvas,
                border = BorderStroke(1.5.dp, YtCyan)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 8.dp)
                ) {
                    Text(
                        text = "Nama Penuh Playlist (Klik 1x lagi untuk minimize • Klik 2x untuk buka):",
                        fontSize = 10.sp,
                        color = VaultLime,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = fullPlaylistPath,
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.White,
                        fontWeight = FontWeight.ExtraBold
                    )
                }
            }
        }
    }
}

@Composable
private fun YouTubeStyleVideoStage(
    videoItem: VaultItemEntity?,
    mediaState: MediaPlayerUiState,
    isFullscreenOverlayOpen: Boolean = false,
    isTheaterMode: Boolean,
    isAutoplayEnabled: Boolean,
    isSubtitlesHudEnabled: Boolean,
    showStatsForNerds: Boolean,
    isPlayerScreenLocked: Boolean,
    selectedVideoQualityLabel: String,
    onToggleTheaterMode: () -> Unit,
    onToggleAutoplay: () -> Unit,
    onToggleSubtitlesHud: () -> Unit,
    onToggleStatsForNerds: () -> Unit,
    onTogglePlayerLock: () -> Unit,
    onOpenSettingsSheet: () -> Unit,
    onSetHold2xSpeed: (Boolean) -> Unit,
    onTogglePlayPause: () -> Unit,
    onSeekToSeconds: (Int) -> Unit,
    onSeekToPositionMs: (Long) -> Unit = {},
    onUpdateActualDurationMs: (Long) -> Unit = {},
    onSyncHardwarePositionMs: (Long, Long) -> Unit = { _, _ -> },
    onSkipTrack: (Boolean) -> Unit,
    onCycleSpeed: () -> Unit,
    onToggleRepeat: () -> Unit,
    onOpenFullscreen: () -> Unit,
    onRequestUnlockEncrypted: () -> Unit,
    onOpenDatabaseClick: () -> Unit,
    onImportVideoClick: () -> Unit
) {
    var showOverlayControls by remember { mutableStateOf(true) }
    var seekFeedbackText by remember { mutableStateOf<String?>(null) }
    var seekFeedbackSide by remember { mutableIntStateOf(0) } // -1 left, +1 right
    var seekFeedbackTrigger by remember { mutableIntStateOf(0) }
    var inlineAspectMode by remember(videoItem?.id) { mutableStateOf("AUTO") } // "AUTO", "16:9", "9:16", "4:3"
    var nativeVideoWidth by remember(videoItem?.id) { mutableIntStateOf(16) }
    var nativeVideoHeight by remember(videoItem?.id) { mutableIntStateOf(9) }

    val inlineContext = LocalContext.current
    val inlineAudioManager = remember(inlineContext) {
        inlineContext.getSystemService(android.content.Context.AUDIO_SERVICE) as? android.media.AudioManager
    }
    var inlineVolumePercent by remember {
        val maxV = (inlineAudioManager?.getStreamMaxVolume(android.media.AudioManager.STREAM_MUSIC) ?: 15).coerceAtLeast(1)
        val curV = inlineAudioManager?.getStreamVolume(android.media.AudioManager.STREAM_MUSIC) ?: (maxV / 2)
        mutableIntStateOf(((curV.toFloat() / maxV.toFloat()) * 100f).toInt().coerceIn(0, 100))
    }
    var inlineLoudnessEnhancer by remember(videoItem?.id) {
        mutableStateOf<android.media.audiofx.LoudnessEnhancer?>(null)
    }

    fun applyInlineVideoVolumePercent(targetPct: Int) {
        val clamped = targetPct.coerceIn(0, 400)
        inlineVolumePercent = clamped
        inlineAudioManager?.let { am ->
            val maxV = am.getStreamMaxVolume(android.media.AudioManager.STREAM_MUSIC).coerceAtLeast(1)
            val sysFrac = (clamped.coerceIn(0, 100) / 100f)
            val targetSysVol = (sysFrac * maxV).toInt().coerceIn(0, maxV)
            try {
                am.setStreamVolume(android.media.AudioManager.STREAM_MUSIC, targetSysVol, 0)
            } catch (_: Exception) {
            }
        }
        try {
            val enhancer = inlineLoudnessEnhancer
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

    DisposableEffect(videoItem?.id) {
        onDispose {
            try {
                inlineLoudnessEnhancer?.release()
            } catch (_: Exception) {
            }
            inlineLoudnessEnhancer = null
        }
    }

    LaunchedEffect(seekFeedbackTrigger) {
        if (seekFeedbackTrigger > 0) {
            delay(650L)
            seekFeedbackText = null
            seekFeedbackSide = 0
        }
    }

    val isNativePortrait = nativeVideoHeight > nativeVideoWidth && nativeVideoWidth > 0
    val effectiveAspectRatio = when {
        inlineAspectMode == "9:16" -> 9f / 16f
        inlineAspectMode == "16:9" -> 16f / 9f
        inlineAspectMode == "4:3" || isTheaterMode -> 4f / 3f
        isNativePortrait -> 9f / 16f
        else -> 16f / 9f
    }

    val currentChapterTitle = remember(mediaState.currentSeconds, mediaState.totalSeconds) {
        val total = mediaState.totalSeconds.coerceAtLeast(1)
        val ratio = mediaState.currentSeconds.toFloat() / total.toFloat()
        when {
            ratio < 0.25f -> "Bagian 1 • Pembuka Video"
            ratio < 0.75f -> "Bagian 2 • Inti Rekaman Brankas"
            else -> "Bagian 3 • Penutup & Ringkasan"
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(YouTubeDarkCanvas)
            .testTag("youtube_video_stage_card")
    ) {
        val latestMediaState by androidx.compose.runtime.rememberUpdatedState(mediaState)
        val inlineGestureScope = rememberCoroutineScope()
        // Adaptive 16:9 / 9:16 / 4:3 YouTube Player Viewport
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .then(
                    if (effectiveAspectRatio < 1f) {
                        Modifier
                            .heightIn(min = 280.dp, max = 460.dp)
                            .aspectRatio(effectiveAspectRatio, matchHeightConstraintsFirst = true)
                    } else {
                        Modifier.aspectRatio(effectiveAspectRatio)
                    }
                )
                .background(YouTubeDarkCanvas)
                .pointerInput(videoItem?.id, isPlayerScreenLocked) {
                    detectTapGestures(
                        onTap = {
                            showOverlayControls = !showOverlayControls
                        },
                        onDoubleTap = { offset ->
                            if (!isPlayerScreenLocked && videoItem != null && !videoItem.isEncrypted) {
                                val snap = latestMediaState
                                val isLeft = offset.x < size.width / 2f
                                val delta = if (isLeft) -10 else 10
                                val target = (snap.currentSeconds + delta)
                                    .coerceIn(0, snap.totalSeconds.coerceAtLeast(1))
                                onSeekToSeconds(target)
                                seekFeedbackSide = if (isLeft) -1 else 1
                                seekFeedbackText = if (isLeft) "-10 detik" else "+10 detik"
                                seekFeedbackTrigger++
                            }
                        },
                        onLongPress = {
                            if (!isPlayerScreenLocked && videoItem != null && !videoItem.isEncrypted) {
                                onSetHold2xSpeed(true)
                            }
                        },
                        onPress = {
                            val holdJob = inlineGestureScope.launch {
                                delay(210L)
                                if (!isPlayerScreenLocked && videoItem != null && !videoItem.isEncrypted) {
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
                },
            contentAlignment = Alignment.Center
        ) {
            if (videoItem == null) {
                // Empty Vault Video Database State inside 16:9 stage
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.padding(24.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.SmartDisplay,
                        contentDescription = null,
                        tint = YouTubeRed,
                        modifier = Modifier.size(54.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Database Video Brankas Kosong",
                        style = MaterialTheme.typography.titleMedium,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Buka database atau impor video lokal untuk mulai menonton",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.White.copy(alpha = 0.72f),
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(14.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        Surface(
                            modifier = Modifier
                                .clip(RoundedCornerShape(50))
                                .clickable { onOpenDatabaseClick() },
                            color = YouTubeDarkChip
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Storage,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(16.dp)
                                )
                                Text(
                                    text = "Buka Database",
                                    style = MaterialTheme.typography.labelLarge,
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                        Surface(
                            modifier = Modifier
                                .clip(RoundedCornerShape(50))
                                .clickable { onImportVideoClick() },
                            color = YouTubeRed
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.UploadFile,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(16.dp)
                                )
                                Text(
                                    text = "Impor Video",
                                    style = MaterialTheme.typography.labelLarge,
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            } else if (videoItem.isEncrypted) {
                // Encrypted Vault Video Prompt in Player Stage
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.padding(20.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(56.dp)
                            .clip(CircleShape)
                            .background(YouTubeRed.copy(alpha = 0.2f))
                            .border(1.5.dp, YouTubeRed, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Lock,
                            contentDescription = "Terkunci AES",
                            tint = YouTubeRed,
                            modifier = Modifier.size(28.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "${videoItem.name}.${videoItem.extension}",
                        style = MaterialTheme.typography.titleMedium,
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = "Video ini dikunci dengan enkripsi AES-256 di Database Brankas",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.White.copy(alpha = 0.72f),
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Surface(
                        modifier = Modifier
                            .clip(RoundedCornerShape(50))
                            .clickable { onRequestUnlockEncrypted() }
                            .testTag("unlock_inline_video_button"),
                        color = YouTubeRed
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 18.dp, vertical = 9.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.LockOpen,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(16.dp)
                            )
                            Text(
                                text = "Buka Kunci PIN & Putar",
                                style = MaterialTheme.typography.labelLarge,
                                color = Color.White,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            } else {
                // Active Unlocked VideoView Stream from Local Vault Database
                val plainFile = remember(videoItem.internalPlainPath) { File(videoItem.internalPlainPath) }
                var videoFailed by remember(videoItem.id, videoItem.internalPlainPath) { mutableStateOf(false) }
                var lastHandledSeekNonce by remember(videoItem.id) { mutableIntStateOf(mediaState.userSeekNonce) }
                var lastHandledReplayNonce by remember(videoItem.id) { mutableIntStateOf(mediaState.replayNonce) }
                var videoViewRef by remember(videoItem.id) { mutableStateOf<VideoView?>(null) }
                var mediaPlayerRef by remember(videoItem.id) { mutableStateOf<android.media.MediaPlayer?>(null) }
                val isThisVideoActive = mediaState.currentTrack == null || mediaState.currentTrack.id == videoItem.id

                // Poll real hardware VideoView position every 200ms while playing so seekbar never drifts
                LaunchedEffect(videoViewRef, isThisVideoActive, mediaState.isPlaying, isFullscreenOverlayOpen) {
                    val vv = videoViewRef
                    if (vv != null && isThisVideoActive && mediaState.isPlaying && !isFullscreenOverlayOpen) {
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

                // Dynamic speed update when user cycles speed or holds 2x
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

                if (!isFullscreenOverlayOpen && !videoFailed && (plainFile.exists() || !videoItem.mediaUriString.isNullOrBlank())) {
                    key(videoItem.id, videoItem.internalPlainPath, videoItem.mediaUriString) {
                        AndroidView(
                            factory = { ctx ->
                                VideoView(ctx).apply {
                                    videoViewRef = this
                                    setOnErrorListener { _, _, _ ->
                                        videoFailed = true
                                        true
                                    }
                                    try {
                                        if (plainFile.exists() && plainFile.length() > 0L) {
                                            setVideoURI(Uri.fromFile(plainFile))
                                        } else if (!videoItem.mediaUriString.isNullOrBlank()) {
                                            setVideoURI(Uri.parse(videoItem.mediaUriString))
                                        }
                                    } catch (_: Exception) {
                                        if (plainFile.exists()) {
                                            setVideoPath(plainFile.absolutePath)
                                        }
                                    }
                                    setOnPreparedListener { mp ->
                                        mediaPlayerRef = mp
                                        try {
                                            inlineLoudnessEnhancer?.release()
                                            val enhancer = android.media.audiofx.LoudnessEnhancer(mp.audioSessionId)
                                            inlineLoudnessEnhancer = enhancer
                                            applyInlineVideoVolumePercent(inlineVolumePercent)
                                        } catch (_: Exception) {
                                        }
                                        if (mp.videoWidth > 0 && mp.videoHeight > 0) {
                                            nativeVideoWidth = mp.videoWidth
                                            nativeVideoHeight = mp.videoHeight
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
                                        if (isThisVideoActive && mediaState.isPlaying) {
                                            start()
                                        } else if (resumeMs == 0) {
                                            seekTo(100)
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
                                    if (isThisVideoActive) {
                                        if (lastHandledReplayNonce != mediaState.replayNonce) {
                                            lastHandledReplayNonce = mediaState.replayNonce
                                            vv.seekTo(0)
                                            if (mediaState.isPlaying) vv.start()
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
                                    } else if (vv.isPlaying) {
                                        vv.pause()
                                    }
                                } catch (_: Exception) {
                                }
                            },
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                } else {
                    // Simulated Local Vault Cinema Stream Canvas (for seeded database tracks without raw external codec)
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.radialGradient(
                                    colors = listOf(
                                        Color(0xFF282828),
                                        YouTubeDarkCanvas
                                    )
                                )
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center,
                            modifier = Modifier.padding(16.dp)
                        ) {
                            NyxMascotAvatar(
                                mood = if (mediaState.isPlaying) MascotMood.HAPPY_VIBES else MascotMood.WAVE_HELLO,
                                size = 68.dp
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "${videoItem.name}.${videoItem.extension}",
                                style = MaterialTheme.typography.titleMedium,
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            Text(
                                text = "Database Brankas Lokal • ${formatFileSize(videoItem.sizeBytes)} • $selectedVideoQualityLabel",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.White.copy(alpha = 0.72f)
                            )
                        }
                    }
                }
            }

            // SUBTITLES / INFO CAPTION BAR (When CC is toggled on)
            if (isSubtitlesHudEnabled && videoItem != null && !videoItem.isEncrypted) {
                Surface(
                    color = Color.Black.copy(alpha = 0.78f),
                    shape = RoundedCornerShape(6.dp),
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = if (showOverlayControls) 58.dp else 18.dp, start = 24.dp, end = 24.dp)
                ) {
                    Text(
                        text = "[CC Database] ${videoItem.name}.${videoItem.extension} • $currentChapterTitle (${formatSeconds(mediaState.currentSeconds)})",
                        style = MaterialTheme.typography.labelMedium,
                        color = Color.White,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                    )
                }
            }

            // YOUTUBE "STATS FOR NERDS" TECHNICAL DATABASE OVERLAY
            if (showStatsForNerds && videoItem != null) {
                Surface(
                    color = Color.Black.copy(alpha = 0.84f),
                    shape = RoundedCornerShape(10.dp),
                    border = BorderStroke(1.dp, Color.White.copy(alpha = 0.2f)),
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(12.dp)
                        .widthIn(max = 310.dp)
                        .testTag("yt_stats_for_nerds_overlay")
                ) {
                    Column(modifier = Modifier.padding(10.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Statistik Database (Stats for Nerds)",
                                fontFamily = JetBrainsMonoFontFamily,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = YtCyan
                            )
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Tutup Statistik",
                                tint = Color.White,
                                modifier = Modifier
                                    .size(14.dp)
                                    .clickable { onToggleStatsForNerds() }
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "ID Rekaman DB : #${videoItem.id} (Brankas #${videoItem.vaultProfileId})",
                            fontFamily = JetBrainsMonoFontFamily,
                            fontSize = 10.sp,
                            color = Color.White
                        )
                        Text(
                            text = "Format & Codec: ${videoItem.extension.uppercase()} / H.264 AAC",
                            fontFamily = JetBrainsMonoFontFamily,
                            fontSize = 10.sp,
                            color = Color.White
                        )
                        Text(
                            text = "Kualitas Putar: $selectedVideoQualityLabel @ ${mediaState.playbackSpeed}x",
                            fontFamily = JetBrainsMonoFontFamily,
                            fontSize = 10.sp,
                            color = Color.White
                        )
                        Text(
                            text = "Ukuran & Status: ${formatFileSize(videoItem.sizeBytes)} • ${if (videoItem.isEncrypted) "AES-256 LOCKED" else "64-BIT >3GB STREAM READY"}",
                            fontFamily = JetBrainsMonoFontFamily,
                            fontSize = 10.sp,
                            color = VaultLime
                        )
                    }
                }
            }

            // DOUBLE-TAP SEEK FEEDBACK OVERLAY (-5s Left / +5s Right) — ZERO EMOJIS
            androidx.compose.animation.AnimatedVisibility(
                visible = seekFeedbackText != null,
                enter = fadeIn(tween(120)),
                exit = fadeOut(tween(180)),
                modifier = Modifier
                    .align(if (seekFeedbackSide < 0) Alignment.CenterStart else Alignment.CenterEnd)
                    .padding(horizontal = 28.dp)
            ) {
                Surface(
                    color = Color.Black.copy(alpha = 0.72f),
                    shape = CircleShape,
                    border = BorderStroke(1.5.dp, Color.White.copy(alpha = 0.35f))
                ) {
                    Column(
                        modifier = Modifier.padding(horizontal = 20.dp, vertical = 14.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = if (seekFeedbackSide < 0) Icons.Default.Replay5 else Icons.Default.Forward5,
                            contentDescription = seekFeedbackText,
                            tint = Color.White,
                            modifier = Modifier.size(28.dp)
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

            // HOLD FOR 2X SPEED PILL BADGE AT TOP CENTER — ZERO EMOJIS
            androidx.compose.animation.AnimatedVisibility(
                visible = mediaState.isHold2xActive,
                enter = fadeIn(tween(120)),
                exit = fadeOut(tween(180)),
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 12.dp)
            ) {
                Surface(
                    color = Color.Black.copy(alpha = 0.82f),
                    shape = RoundedCornerShape(50),
                    border = BorderStroke(1.dp, Color.White.copy(alpha = 0.3f))
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.FastForward,
                            contentDescription = "Kecepatan 2x",
                            tint = Color.White,
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = "2X CEPAT",
                            fontFamily = JetBrainsMonoFontFamily,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color.White
                        )
                    }
                }
            }

            // YOUTUBE HUD OVERLAY CONTROLS (Top Bar + Center Play/Skip + Bottom Scrubber)
            androidx.compose.animation.AnimatedVisibility(
                visible = showOverlayControls && videoItem != null,
                enter = fadeIn(tween(150)),
                exit = fadeOut(tween(200)),
                modifier = Modifier.fillMaxSize()
            ) {
                if (isPlayerScreenLocked) {
                    // Locked Screen Overlay (Prevents accidental taps while watching)
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.Black.copy(alpha = 0.35f)),
                        contentAlignment = Alignment.BottomCenter
                    ) {
                        Surface(
                            modifier = Modifier
                                .padding(bottom = 18.dp)
                                .clip(RoundedCornerShape(50))
                                .clickable { onTogglePlayerLock() }
                                .testTag("yt_unlock_screen_button"),
                            color = Color.Black.copy(alpha = 0.82f),
                            border = BorderStroke(1.dp, Color.White.copy(alpha = 0.45f))
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.LockOpen,
                                    contentDescription = "Buka Kunci Layar",
                                    tint = Color.White,
                                    modifier = Modifier.size(16.dp)
                                )
                                Text(
                                    text = "Layar Terkunci • Ketuk untuk Membuka",
                                    style = MaterialTheme.typography.labelMedium,
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
                            .background(Color.Black.copy(alpha = 0.20f)) // Ultra-ringan agar tidak mengganggu saat menonton
                    ) {
                        // Top Player Overlay Bar: Video Title + Autoplay + CC + Theater + Settings Gear
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .align(Alignment.TopCenter)
                                .padding(horizontal = 10.dp, vertical = 6.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                TruncatedHoldTooltipText(
                                    text = "${videoItem?.name ?: ""}.${videoItem?.extension ?: ""}",
                                    style = MaterialTheme.typography.labelMedium,
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                    maxLines = 1
                                )
                                Text(
                                    text = "${videoItem?.playlistFolderLabel() ?: "Database"} • $currentChapterTitle",
                                    fontSize = 9.sp,
                                    color = Color.White.copy(alpha = 0.75f),
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }

                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                // Autoplay Toggle Pill
                                Surface(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(50))
                                        .clickable { onToggleAutoplay() },
                                    color = if (isAutoplayEnabled) YouTubeRed.copy(alpha = 0.85f) else Color.Black.copy(alpha = 0.5f)
                                ) {
                                    Row(
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(3.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.PlayCircleFilled,
                                            contentDescription = "Autoplay",
                                            tint = Color.White,
                                            modifier = Modifier.size(12.dp)
                                        )
                                        Text(
                                            text = if (isAutoplayEnabled) "AUTO" else "OFF",
                                            fontFamily = JetBrainsMonoFontFamily,
                                            fontSize = 8.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color.White
                                        )
                                    }
                                }

                                // CC / Info Overlay Toggle
                                IconButton(
                                    onClick = onToggleSubtitlesHud,
                                    modifier = Modifier.size(26.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Subtitles,
                                        contentDescription = "Info Teks / CC",
                                        tint = if (isSubtitlesHudEnabled) YouTubeRed else Color.White,
                                        modifier = Modifier.size(15.dp)
                                    )
                                }

                                // Aspect Ratio Switcher (16:9 / 9:16 / 4:3)
                                Surface(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(50))
                                        .clickable {
                                            inlineAspectMode = when (inlineAspectMode) {
                                                "AUTO" -> "9:16"
                                                "9:16" -> "16:9"
                                                "16:9" -> "4:3"
                                                else -> "AUTO"
                                            }
                                            onToggleTheaterMode()
                                        }
                                        .testTag("yt_inline_aspect_ratio_button"),
                                    color = Color.Black.copy(alpha = 0.55f),
                                    border = BorderStroke(0.5.dp, Color.White.copy(alpha = 0.3f))
                                ) {
                                    Row(
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(3.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.AspectRatio,
                                            contentDescription = "Rasio Video",
                                            tint = VaultLime,
                                            modifier = Modifier.size(12.dp)
                                        )
                                        Text(
                                            text = when (inlineAspectMode) {
                                                "9:16" -> "9:16"
                                                "16:9" -> "16:9"
                                                "4:3" -> "4:3"
                                                else -> if (isNativePortrait) "9:16" else "16:9"
                                            },
                                            fontFamily = JetBrainsMonoFontFamily,
                                            fontSize = 8.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color.White
                                        )
                                    }
                                }

                                // Compact Settings Gear Icon
                                IconButton(
                                    onClick = onOpenSettingsSheet,
                                    modifier = Modifier
                                        .size(26.dp)
                                        .testTag("yt_player_settings_gear")
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Settings,
                                        contentDescription = "Pengaturan Pemutar Video",
                                        tint = Color.White,
                                        modifier = Modifier.size(15.dp)
                                    )
                                }
                            }
                        }

                        // Center Compact Transport Controls: Previous, Play/Pause, Next
                        Row(
                            modifier = Modifier.align(Alignment.Center),
                            horizontalArrangement = Arrangement.spacedBy(20.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            IconButton(
                                onClick = { onSkipTrack(false) },
                                modifier = Modifier
                                    .size(32.dp)
                                    .clip(CircleShape)
                                    .background(Color.Black.copy(alpha = 0.45f))
                            ) {
                                Icon(
                                    imageVector = Icons.Default.SkipPrevious,
                                    contentDescription = "Video Sebelumnya",
                                    tint = Color.White,
                                    modifier = Modifier.size(18.dp)
                                )
                            }

                            Surface(
                                modifier = Modifier
                                    .size(42.dp)
                                    .clip(CircleShape)
                                    .clickable { onTogglePlayPause() }
                                    .testTag("media_play_pause_button"),
                                color = Color.Black.copy(alpha = 0.55f),
                                border = BorderStroke(1.dp, Color.White.copy(alpha = 0.4f))
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(
                                        imageVector = if (mediaState.isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                                        contentDescription = if (mediaState.isPlaying) "Jeda" else "Putar",
                                        tint = Color.White,
                                        modifier = Modifier.size(22.dp)
                                    )
                                }
                            }

                            IconButton(
                                onClick = { onSkipTrack(true) },
                                modifier = Modifier
                                    .size(32.dp)
                                    .clip(CircleShape)
                                    .background(Color.Black.copy(alpha = 0.45f))
                            ) {
                                Icon(
                                    imageVector = Icons.Default.SkipNext,
                                    contentDescription = "Video Berikutnya",
                                    tint = Color.White,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }

                        // Bottom Ultra-Minimalist Timestamp (9.sp), Speed, Repeat & Fullscreen Controls
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .align(Alignment.BottomCenter)
                                .padding(horizontal = 10.dp, vertical = 4.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Surface(
                                color = Color.Black.copy(alpha = 0.52f),
                                shape = RoundedCornerShape(4.dp)
                            ) {
                                Text(
                                    text = formatDurationMsCompact(
                                        currentMs = mediaState.currentPositionMs,
                                        totalMs = mediaState.totalDurationMs
                                    ),
                                    fontFamily = JetBrainsMonoFontFamily,
                                    fontSize = 9.sp,
                                    color = Color.White,
                                    fontWeight = FontWeight.SemiBold,
                                    modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp)
                                )
                            }

                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(5.dp)
                            ) {
                                // Speed Pill
                                Surface(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(5.dp))
                                        .clickable { onCycleSpeed() }
                                        .testTag("media_speed_button"),
                                    color = Color.Black.copy(alpha = 0.55f),
                                    border = BorderStroke(0.5.dp, Color.White.copy(alpha = 0.25f))
                                ) {
                                    Text(
                                        text = if (mediaState.isHold2xActive) "2.0x" else "${mediaState.playbackSpeed}x",
                                        fontFamily = JetBrainsMonoFontFamily,
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White,
                                        modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp)
                                    )
                                }

                                // Repeat Mode Pill
                                Surface(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(5.dp))
                                        .clickable { onToggleRepeat() }
                                        .testTag("media_repeat_button"),
                                    color = Color.Black.copy(alpha = 0.55f),
                                    border = BorderStroke(0.5.dp, Color.White.copy(alpha = 0.25f))
                                ) {
                                    Row(
                                        modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(2.dp)
                                    ) {
                                        Icon(
                                            imageVector = when (mediaState.repeatMode) {
                                                PlaybackRepeatMode.REPEAT_ONE -> Icons.Default.RepeatOne
                                                PlaybackRepeatMode.AUTO_ADVANCE -> Icons.Default.SkipNext
                                                PlaybackRepeatMode.REPEAT_PLAYLIST -> Icons.Default.Repeat
                                            },
                                            contentDescription = "Mode Ulang",
                                            tint = Color.White,
                                            modifier = Modifier.size(10.dp)
                                        )
                                        Text(
                                            text = when (mediaState.repeatMode) {
                                                PlaybackRepeatMode.REPEAT_ONE -> "1x"
                                                PlaybackRepeatMode.AUTO_ADVANCE -> "Auto"
                                                PlaybackRepeatMode.REPEAT_PLAYLIST -> "All"
                                            },
                                            fontSize = 9.sp,
                                            color = Color.White
                                        )
                                    }
                                }

                                // Fullscreen Button
                                IconButton(
                                    onClick = onOpenFullscreen,
                                    modifier = Modifier
                                        .size(24.dp)
                                        .testTag("video_stage_fullscreen_button")
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Fullscreen,
                                        contentDescription = "Layar Penuh",
                                        tint = Color.White,
                                        modifier = Modifier.size(17.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // ULTRA-THIN YOUTUBE RED PROGRESS LINE AT BOTTOM EDGE (2.dp thin, non-intrusive, tap/drag to seek)
        if (videoItem != null) {
            val totalMsSafe = mediaState.totalDurationMs.coerceAtLeast(500L)
            val progressRatio = (mediaState.currentPositionMs.toFloat() / totalMsSafe.toFloat()).coerceIn(0f, 1f)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(if (showOverlayControls) 10.dp else 3.dp)
                    .background(YouTubeDarkCanvas)
                    .pointerInput(totalMsSafe) {
                        detectTapGestures { offset ->
                            val ratio = (offset.x / size.width.toFloat().coerceAtLeast(1f)).coerceIn(0f, 1f)
                            val targetMs = (ratio * totalMsSafe).toLong()
                            onSeekToPositionMs(targetMs)
                            onSeekToSeconds((targetMs / 1000L).toInt())
                        }
                    }
                    .testTag("yt_ultra_thin_red_scrubber"),
                contentAlignment = Alignment.CenterStart
            ) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val barHeight = if (showOverlayControls) 2.5.dp.toPx() else 1.8.dp.toPx()
                    val yCenter = size.height / 2f
                    // Background track
                    drawRect(
                        color = Color.White.copy(alpha = 0.22f),
                        topLeft = Offset(0f, yCenter - barHeight / 2f),
                        size = Size(size.width, barHeight)
                    )
                    // Active YouTube Red line
                    val activeW = (size.width * progressRatio).coerceIn(0f, size.width)
                    drawRect(
                        color = YouTubeRed,
                        topLeft = Offset(0f, yCenter - barHeight / 2f),
                        size = Size(activeW, barHeight)
                    )
                    // Tiny YouTube Red dot only when overlay controls are visible
                    if (showOverlayControls) {
                        drawCircle(
                            color = YouTubeRed,
                            radius = 3.5.dp.toPx(),
                            center = Offset(activeW.coerceIn(3.5.dp.toPx(), (size.width - 3.5.dp.toPx()).coerceAtLeast(3.5.dp.toPx())), yCenter)
                        )
                    }
                }
            }

            // 400% VIDEO VOLUME BOOSTER BAR (Exclusive to Video Player)
            Surface(
                color = Color(0xFF141824),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("inline_video_volume_booster_bar")
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.VolumeUp,
                        contentDescription = "Pengeras Suara Video 400%",
                        tint = if (inlineVolumePercent > 100) VaultLime else Color.White,
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = "SUARA ${inlineVolumePercent}%",
                        fontFamily = JetBrainsMonoFontFamily,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = if (inlineVolumePercent > 100) VaultLime else Color.White,
                        maxLines = 1,
                        softWrap = false
                    )
                    Slider(
                        value = inlineVolumePercent.toFloat(),
                        onValueChange = { newVal ->
                            applyInlineVideoVolumePercent(newVal.toInt())
                        },
                        valueRange = 0f..400f,
                        modifier = Modifier
                            .weight(1f)
                            .height(22.dp)
                            .testTag("inline_video_volume_booster_slider"),
                        colors = SliderDefaults.colors(
                            thumbColor = if (inlineVolumePercent > 100) VaultLime else Color.White,
                            activeTrackColor = if (inlineVolumePercent > 100) VaultLime else YouTubeRed,
                            inactiveTrackColor = Color.White.copy(alpha = 0.22f)
                        )
                    )
                    listOf(100, 200, 300, 400).forEach { pct ->
                        val active = inlineVolumePercent == pct
                        Surface(
                            modifier = Modifier
                                .clip(RoundedCornerShape(50))
                                .clickable { applyInlineVideoVolumePercent(pct) }
                                .testTag("inline_booster_${pct}_chip"),
                            color = if (active) VaultLime else Color.White.copy(alpha = 0.14f)
                        ) {
                            Text(
                                text = if (pct == 400) "400%" else "$pct%",
                                fontFamily = JetBrainsMonoFontFamily,
                                fontSize = 9.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = if (active) VaultObsidian else Color.White,
                                maxLines = 1,
                                softWrap = false,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

/**
 * YouTube Watch Page Metadata, Channel / Vault Database Owner Row,
 * Horizontal Action Pills (100% Material Icons, Zero Emojis),
 * Expandable Description + Chapters, and Expandable Timestamp Notes / Comments.
 */
@Composable
private fun YouTubeWatchMetadataSection(
    activeVaultId: Int,
    videoItem: VaultItemEntity,
    totalDatabaseVideoCount: Int,
    mediaState: MediaPlayerUiState,
    isLiked: Boolean,
    isDisliked: Boolean,
    isDescriptionExpanded: Boolean,
    isCommentsExpanded: Boolean,
    videoNotes: List<LocalVideoTimestampNote>,
    newCommentInput: String,
    onNewCommentInputChange: (String) -> Unit,
    onAddTimestampNote: (String) -> Unit,
    onAddQuickClipBookmark: () -> Unit,
    onSeekToSeconds: (Int) -> Unit,
    onToggleLike: () -> Unit,
    onToggleDislike: () -> Unit,
    onToggleDescription: () -> Unit,
    onToggleComments: () -> Unit,
    onOpenDatabaseModal: () -> Unit,
    onSaveToPlaylistClick: () -> Unit,
    onToggleEncryptPinClick: () -> Unit,
    onShareClick: () -> Unit,
    onDownloadExportClick: () -> Unit,
    onOpenPlayerSettingsClick: () -> Unit,
    onToggleStatsForNerds: () -> Unit,
    onImportVideoClick: () -> Unit
) {
    val dateFormatted = remember(videoItem.addedAt) {
        SimpleDateFormat("dd MMM yyyy", Locale("id", "ID")).format(Date(videoItem.addedAt))
    }
    val likeCount = remember(videoItem.id, isLiked) {
        val base = ((videoItem.id * 7) + 18).coerceAtLeast(12)
        if (isLiked) base + 1 else base
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(YtCardWhite)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // 1. Video Title + Metadata Summary Row (Click to Expand Description)
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onToggleDescription() }
        ) {
            TruncatedHoldTooltipText(
                text = "${videoItem.name}.${videoItem.extension}",
                style = MaterialTheme.typography.titleLarge,
                color = YtInk,
                fontWeight = FontWeight.ExtraBold,
                maxLines = 1,
                onClick = { onToggleDescription() }
            )
            Spacer(modifier = Modifier.height(4.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(
                    text = "${formatFileSize(videoItem.sizeBytes)} • $dateFormatted • Playlist: ${videoItem.playlistFolderLabel()}",
                    style = MaterialTheme.typography.bodySmall,
                    color = YtSubtle,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f, fill = false)
                )
                Text(
                    text = if (isDescriptionExpanded) "Sembunyikan" else "...selengkapnya",
                    style = MaterialTheme.typography.labelMedium,
                    color = YtInk,
                    fontWeight = FontWeight.ExtraBold,
                    maxLines = 1
                )
            }
        }

        // 2. Expandable YouTube Description & Vault Database Record Box + Chapters
        androidx.compose.animation.AnimatedVisibility(visible = isDescriptionExpanded) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = YtChipBg),
                border = BorderStroke(1.5.dp, YtBorder)
            ) {
                Column(
                    modifier = Modifier.padding(14.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // Top Stats Summary inside Description (YouTube Style)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "$likeCount",
                                style = MaterialTheme.typography.titleMedium,
                                color = YtInk,
                                fontWeight = FontWeight.ExtraBold
                            )
                            Text(
                                text = "Disukai",
                                style = MaterialTheme.typography.labelSmall,
                                color = YtSubtle
                            )
                        }
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = formatFileSize(videoItem.sizeBytes),
                                style = MaterialTheme.typography.titleMedium,
                                color = YtInk,
                                fontWeight = FontWeight.ExtraBold
                            )
                            Text(
                                text = "Ukuran File",
                                style = MaterialTheme.typography.labelSmall,
                                color = YtSubtle
                            )
                        }
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = videoItem.extension.uppercase(),
                                style = MaterialTheme.typography.titleMedium,
                                color = VaultCoral,
                                fontWeight = FontWeight.ExtraBold
                            )
                            Text(
                                text = "Format Video",
                                style = MaterialTheme.typography.labelSmall,
                                color = YtSubtle
                            )
                        }
                    }

                    HorizontalDivider(color = YtBorder)

                    Text(
                        text = "Informasi Rekaman Database Brankas Lokal:",
                        style = MaterialTheme.typography.labelLarge,
                        color = YtInk,
                        fontWeight = FontWeight.ExtraBold
                    )
                    Text(
                        text = buildString {
                            append("• Nama File: ${videoItem.name}.${videoItem.extension}\n")
                            append("• Folder / Playlist: ${videoItem.playlistFolderLabel()}\n")
                            append("• Status Keamanan: ${if (videoItem.isEncrypted) "Terenkripsi AES-256-GCM (.nyx)" else "Terbuka (Siap Diputar)"}\n")
                            append("• Lokasi Database: Brankas #$activeVaultId (ID Rekaman #${videoItem.id})\n")
                            if (videoItem.rawContentPreview.isNotBlank()) {
                                append("• Catatan File: ${videoItem.rawContentPreview}")
                            }
                        },
                        style = MaterialTheme.typography.bodySmall,
                        color = YtInk
                    )

                    // Interactive YouTube Video Chapters
                    Text(
                        text = "Bab Video (Ketuk untuk Melompat):",
                        style = MaterialTheme.typography.labelLarge,
                        color = YtInk,
                        fontWeight = FontWeight.ExtraBold
                    )
                    val totalSec = mediaState.totalSeconds.coerceAtLeast(30)
                    val chapters = listOf(
                        0 to "Pembuka Video",
                        (totalSec * 0.25f).toInt() to "Inti Rekaman Brankas",
                        (totalSec * 0.75f).toInt() to "Penutup & Ringkasan"
                    )
                    Row(
                        modifier = Modifier.horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        chapters.forEach { (sec, label) ->
                            Surface(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(10.dp))
                                    .clickable { onSeekToSeconds(sec) },
                                color = YtCardWhite,
                                border = BorderStroke(1.dp, YtBorder)
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Text(
                                        text = formatSeconds(sec),
                                        fontFamily = JetBrainsMonoFontFamily,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = YtCyan
                                    )
                                    Text(
                                        text = label,
                                        style = MaterialTheme.typography.labelMedium,
                                        color = YtInk
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // 3. YouTube Channel / Local Vault Database Owner Bar
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
                NyxMascotAvatar(
                    mood = if (mediaState.isPlaying) MascotMood.HAPPY_VIBES else MascotMood.WAVE_HELLO,
                    size = 40.dp
                )
                Column {
                    Text(
                        text = "Database Brankas #$activeVaultId",
                        style = MaterialTheme.typography.titleSmall,
                        color = YtInk,
                        fontWeight = FontWeight.ExtraBold
                    )
                    Text(
                        text = "$totalDatabaseVideoCount Video Lokal Tersimpan",
                        style = MaterialTheme.typography.labelSmall,
                        color = YtSubtle
                    )
                }
            }

            Row(horizontalArrangement = Arrangement.spacedBy(5.dp)) {
                // Compact Buka Database Button
                Surface(
                    modifier = Modifier
                        .clip(RoundedCornerShape(50))
                        .clickable { onOpenDatabaseModal() }
                        .testTag("yt_channel_open_database_button"),
                    color = YtInk
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.VideoLibrary,
                            contentDescription = "Buka Database Video",
                            tint = Color.White,
                            modifier = Modifier.size(13.dp)
                        )
                        Text(
                            text = "Buka Database",
                            fontSize = 11.sp,
                            color = Color.White,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }
                }

                // Compact Impor Video Button
                Surface(
                    modifier = Modifier
                        .clip(RoundedCornerShape(50))
                        .clickable { onImportVideoClick() },
                    color = YtChipBg,
                    border = BorderStroke(1.dp, YtBorder)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 9.dp, vertical = 5.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(3.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Impor Video",
                            tint = YtInk,
                            modifier = Modifier.size(13.dp)
                        )
                        Text(
                            text = "Impor",
                            fontSize = 11.sp,
                            color = YtInk,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        // 4. Compact Horizontal Scrollable Action Pills (100% Material Icons, Zero Emojis)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Pill 1: Compact Combined Like | Dislike Pill
            Surface(
                color = YtChipBg,
                shape = RoundedCornerShape(50),
                border = BorderStroke(1.dp, YtBorder)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        modifier = Modifier
                            .clickable { onToggleLike() }
                            .testTag("yt_like_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.ThumbUp,
                            contentDescription = "Suka",
                            tint = if (isLiked) VaultCoral else YtInk,
                            modifier = Modifier.size(14.dp)
                        )
                        Text(
                            text = "$likeCount",
                            fontSize = 11.sp,
                            color = if (isLiked) VaultCoral else YtInk,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Box(
                        modifier = Modifier
                            .width(1.dp)
                            .height(13.dp)
                            .background(YtBorder)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(
                        imageVector = Icons.Default.ThumbDown,
                        contentDescription = "Tidak Suka",
                        tint = if (isDisliked) VaultCoral else YtInk,
                        modifier = Modifier
                            .size(14.dp)
                            .clickable { onToggleDislike() }
                    )
                }
            }

            // Pill 2: Simpan ke Playlist
            YouTubeActionPill(
                icon = Icons.AutoMirrored.Filled.PlaylistAdd,
                label = "Simpan",
                onClick = onSaveToPlaylistClick
            )

            // Pill 3: Kunci / Buka Enkripsi PIN AES
            YouTubeActionPill(
                icon = if (videoItem.isEncrypted) Icons.Default.LockOpen else Icons.Default.Lock,
                label = if (videoItem.isEncrypted) "Buka AES" else "Kunci AES",
                accentColor = if (videoItem.isEncrypted) VaultAmber else YtInk,
                onClick = onToggleEncryptPinClick
            )

            // Pill 4: Bagikan (Biometric Share)
            YouTubeActionPill(
                icon = Icons.Default.Share,
                label = "Bagikan",
                onClick = onShareClick
            )

            // Pill 5: Unduh / Ekspor (.nyx)
            YouTubeActionPill(
                icon = Icons.Default.Download,
                label = "Ekspor .nyx",
                onClick = onDownloadExportClick
            )

            // Pill 6: Klip / Tandai Waktu
            YouTubeActionPill(
                icon = Icons.Default.ContentCut,
                label = "Klip Waktu",
                onClick = onAddQuickClipBookmark
            )

            // Pill 7: Pengaturan Pemutar Video
            YouTubeActionPill(
                icon = Icons.Default.Tune,
                label = "Kualitas & Speed",
                onClick = onOpenPlayerSettingsClick
            )

            // Pill 8: Statistik Database (Stats for Nerds)
            YouTubeActionPill(
                icon = Icons.Default.Analytics,
                label = "Statistik DB",
                onClick = onToggleStatsForNerds
            )
        }

        // 5. YouTube-Style Comments / Catatan Waktu Lokal Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onToggleComments() }
                .testTag("yt_comments_card"),
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = YtChipBg),
            border = BorderStroke(1.dp, YtBorder)
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.Comment,
                            contentDescription = null,
                            tint = YtInk,
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = "Catatan Waktu & Komentar Lokal (${videoNotes.size})",
                            style = MaterialTheme.typography.labelLarge,
                            color = YtInk,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }
                    Icon(
                        imageVector = if (isCommentsExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                        contentDescription = "Buka Catatan",
                        tint = YtSubtle
                    )
                }

                if (!isCommentsExpanded) {
                    val previewNote = videoNotes.firstOrNull()
                    if (previewNote != null) {
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "${formatSeconds(previewNote.timestampSeconds)} • ${previewNote.text}",
                            style = MaterialTheme.typography.bodySmall,
                            color = YtSubtle,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                } else {
                    Spacer(modifier = Modifier.height(10.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedTextField(
                            value = newCommentInput,
                            onValueChange = onNewCommentInputChange,
                            placeholder = {
                                Text(
                                    text = "Tulis catatan pada ${formatSeconds(mediaState.currentSeconds)}...",
                                    style = MaterialTheme.typography.bodySmall
                                )
                            },
                            singleLine = true,
                            shape = RoundedCornerShape(12.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = VaultCoral,
                                unfocusedBorderColor = YtBorder,
                                focusedContainerColor = YtCardWhite,
                                unfocusedContainerColor = YtCardWhite,
                                focusedTextColor = YtInk,
                                unfocusedTextColor = YtInk
                            ),
                            modifier = Modifier.weight(1f)
                        )
                        Surface(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .clickable { onAddTimestampNote(newCommentInput) },
                            color = VaultCoral
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 12.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.BookmarkAdd,
                                    contentDescription = "Simpan Catatan",
                                    tint = Color.White,
                                    modifier = Modifier.size(16.dp)
                                )
                                Text(
                                    text = "Simpan",
                                    style = MaterialTheme.typography.labelMedium,
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                    videoNotes.forEach { note ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(YtCardWhite)
                                .clickable { onSeekToSeconds(note.timestampSeconds) }
                                .padding(horizontal = 10.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Surface(
                                color = YtCyan.copy(alpha = 0.14f),
                                shape = RoundedCornerShape(6.dp)
                            ) {
                                Text(
                                    text = formatSeconds(note.timestampSeconds),
                                    fontFamily = JetBrainsMonoFontFamily,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = YtCyan,
                                    modifier = Modifier.padding(horizontal = 7.dp, vertical = 3.dp)
                                )
                            }
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = note.text,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = YtInk,
                                    fontWeight = FontWeight.Medium
                                )
                                Text(
                                    text = note.author,
                                    style = MaterialTheme.typography.labelSmall,
                                    color = YtSubtle
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
private fun YouTubeActionPill(
    icon: ImageVector,
    label: String,
    accentColor: Color = YtInk,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .clip(RoundedCornerShape(50))
            .clickable { onClick() },
        color = YtChipBg,
        shape = RoundedCornerShape(50),
        border = BorderStroke(1.dp, YtBorder)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = accentColor,
                modifier = Modifier.size(14.dp)
            )
            Text(
                text = label,
                fontSize = 11.sp,
                color = accentColor,
                fontWeight = FontWeight.ExtraBold
            )
        }
    }
}

/**
 * Authentic YouTube "Up Next" Video Card for the Local Vault Database Feed
 * Features a 16:9 thumbnail box with bottom-right duration badge, red progress bar,
 * encryption status badge, and quick action buttons.
 */
@Composable
private fun YouTubeUpNextVideoCard(
    index: Int,
    activeVaultId: Int,
    item: VaultItemEntity,
    isSelected: Boolean,
    isPlaying: Boolean,
    isLiked: Boolean,
    currentSeconds: Int,
    onPlayClick: () -> Unit,
    onFullscreenClick: () -> Unit,
    onMoveToPlaylistClick: () -> Unit,
    onTogglePinLock: () -> Unit,
    onShareBiometric: () -> Unit,
    onExportBiometric: () -> Unit
) {
    val plainFile = remember(item.internalPlainPath) { File(item.internalPlainPath) }
    val modelData: Any? = remember(plainFile, item.mediaUriString, item.isEncrypted) {
        if (item.isEncrypted) null
        else when {
            plainFile.exists() -> plainFile
            !item.mediaUriString.isNullOrBlank() -> Uri.parse(item.mediaUriString)
            else -> null
        }
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .duolingoCard(
                backgroundColor = YtCardWhite,
                borderColor = if (isSelected) VaultCoral else YtBorder,
                cornerRadius = 18.dp,
                lipHeight = 4.dp
            )
            .clickable { onPlayClick() }
            .testTag("media_track_row_${item.id}"),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = YtCardWhite)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // 16:9 YouTube Thumbnail Box
                Box(
                    modifier = Modifier
                        .width(148.dp)
                        .aspectRatio(16f / 9f)
                        .clip(RoundedCornerShape(12.dp))
                        .background(YouTubeDarkCanvas),
                    contentAlignment = Alignment.Center
                ) {
                    if (modelData != null) {
                        AsyncImage(
                            model = modelData,
                            contentDescription = item.name,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                    } else {
                        Icon(
                            imageVector = if (item.isEncrypted) Icons.Default.Lock else Icons.Default.Movie,
                            contentDescription = null,
                            tint = if (item.isEncrypted) VaultAmber else YouTubeRed,
                            modifier = Modifier.size(30.dp)
                        )
                    }

                    // Top-Left Index / Now Playing Badge
                    Surface(
                        color = if (isSelected) YouTubeRed else Color.Black.copy(alpha = 0.75f),
                        shape = RoundedCornerShape(6.dp),
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .padding(6.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(3.dp)
                        ) {
                            if (isSelected) {
                                Icon(
                                    imageVector = if (isPlaying) Icons.Default.Equalizer else Icons.Default.PlayArrow,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(11.dp)
                                )
                            }
                            Text(
                                text = if (isSelected) "DIPUTAR" else "#$index",
                                fontFamily = JetBrainsMonoFontFamily,
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    }

                    // Bottom-Right YouTube Duration Badge
                    Surface(
                        color = Color.Black.copy(alpha = 0.85f),
                        shape = RoundedCornerShape(5.dp),
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .padding(6.dp)
                    ) {
                        Text(
                            text = formatSeconds(item.durationSeconds.coerceAtLeast(15)),
                            fontFamily = JetBrainsMonoFontFamily,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp)
                        )
                    }

                    // Red Bottom Progress Strip on Thumbnail
                    val progressFraction = if (isSelected) {
                        (currentSeconds.toFloat() / item.durationSeconds.coerceAtLeast(1).toFloat()).coerceIn(0.08f, 1f)
                    } else {
                        0f
                    }
                    if (progressFraction > 0f) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(3.dp)
                                .align(Alignment.BottomStart)
                                .background(Color.White.copy(alpha = 0.3f))
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth(progressFraction)
                                    .fillMaxHeight()
                                    .background(YouTubeRed)
                            )
                        }
                    }
                }

                // Video Details & Quick Action Icons
                Column(modifier = Modifier.weight(1f)) {
                    TruncatedHoldTooltipText(
                        text = "${item.name}.${item.extension}",
                        style = MaterialTheme.typography.titleSmall,
                        color = YtInk,
                        fontWeight = FontWeight.ExtraBold,
                        maxLines = 1,
                        onClick = { onPlayClick() }
                    )
                    Spacer(modifier = Modifier.height(3.dp))
                    Text(
                        text = "Brankas #$activeVaultId • ${item.playlistFolderLabel()}",
                        style = MaterialTheme.typography.labelSmall,
                        color = YtSubtle,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = buildString {
                            append(formatFileSize(item.sizeBytes))
                            append(" • ")
                            append(if (item.isEncrypted) "Terkunci AES" else "Siap Putar")
                            if (isLiked) append(" • Disukai")
                        },
                        style = MaterialTheme.typography.labelSmall,
                        color = if (item.isEncrypted) VaultAmber else VaultLime,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    // Compact Action Icons Row (Zero Emojis)
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(5.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Fullscreen button
                        IconButton(
                            onClick = onFullscreenClick,
                            modifier = Modifier
                                .size(26.dp)
                                .clip(RoundedCornerShape(7.dp))
                                .background(YtChipBg)
                                .testTag("media_row_open_viewer_${item.id}")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Fullscreen,
                                contentDescription = "Layar Penuh",
                                tint = YtInk,
                                modifier = Modifier.size(14.dp)
                            )
                        }

                        // Move to Playlist
                        IconButton(
                            onClick = onMoveToPlaylistClick,
                            modifier = Modifier
                                .size(26.dp)
                                .clip(RoundedCornerShape(7.dp))
                                .background(YtChipBg)
                        ) {
                            Icon(
                                imageVector = Icons.Default.DriveFileMove,
                                contentDescription = "Simpan ke Playlist",
                                tint = YtCyan,
                                modifier = Modifier.size(13.dp)
                            )
                        }

                        // PIN Lock/Unlock
                        IconButton(
                            onClick = onTogglePinLock,
                            modifier = Modifier
                                .size(26.dp)
                                .clip(RoundedCornerShape(7.dp))
                                .background(YtChipBg)
                                .testTag("media_row_pin_lock_${item.id}")
                        ) {
                            Icon(
                                imageVector = if (item.isEncrypted) Icons.Default.Lock else Icons.Default.LockOpen,
                                contentDescription = "Kunci / Buka AES",
                                tint = if (item.isEncrypted) VaultAmber else VaultLime,
                                modifier = Modifier.size(13.dp)
                            )
                        }

                        // Share
                        IconButton(
                            onClick = onShareBiometric,
                            modifier = Modifier
                                .size(26.dp)
                                .clip(RoundedCornerShape(7.dp))
                                .background(YtChipBg)
                                .testTag("media_row_share_${item.id}")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Share,
                                contentDescription = "Bagikan",
                                tint = YtInk,
                                modifier = Modifier.size(13.dp)
                            )
                        }

                        // Export
                        IconButton(
                            onClick = onExportBiometric,
                            modifier = Modifier
                                .size(26.dp)
                                .clip(RoundedCornerShape(7.dp))
                                .background(YtChipBg)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Download,
                                contentDescription = "Ekspor",
                                tint = YtInk,
                                modifier = Modifier.size(13.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

/**
 * YouTube Vault Database Explorer Dialog ("Buka Database Video Brankas")
 * Allows browsing, searching, filtering, sorting, and opening any video stored in the local Vault database.
 */
@Composable
private fun YouTubeVaultDatabaseDialog(
    activeVaultId: Int,
    allVideoTracks: List<VaultItemEntity>,
    activeVideoId: Int?,
    isPlaying: Boolean,
    playlistFolders: List<String>,
    activeMediaFolder: String,
    onSelectMediaFolder: (String) -> Unit,
    onSelectAndPlayVideo: (VaultItemEntity) -> Unit,
    onOpenFullscreenVideo: (VaultItemEntity) -> Unit,
    onTogglePinEncrypt: (VaultItemEntity) -> Unit,
    onMoveToPlaylist: (VaultItemEntity) -> Unit,
    onImportNewVideoClick: () -> Unit,
    onDismiss: () -> Unit
) {
    var dbQuery by remember { mutableStateOf("") }
    var dbFilter by remember { mutableStateOf(YouTubeDatabaseFilter.ALL) }

    val totalSizeBytes = remember(allVideoTracks) { allVideoTracks.sumOf { it.sizeBytes } }
    val unlockedCount = remember(allVideoTracks) { allVideoTracks.count { !it.isEncrypted } }
    val encryptedCount = remember(allVideoTracks) { allVideoTracks.count { it.isEncrypted } }

    val filteredDbVideos = remember(allVideoTracks, dbQuery, dbFilter, activeMediaFolder) {
        allVideoTracks.filter { item ->
            val folderLabel = item.playlistFolderLabel()
            val folderMatch = activeMediaFolder == "Semua Media" || folderLabel == activeMediaFolder
            val queryMatch = dbQuery.isBlank() ||
                item.name.contains(dbQuery, ignoreCase = true) ||
                item.extension.contains(dbQuery, ignoreCase = true) ||
                folderLabel.contains(dbQuery, ignoreCase = true)
            val extLower = item.extension.lowercase(Locale.ROOT)
            val statusMatch = when (dbFilter) {
                YouTubeDatabaseFilter.ALL -> true
                YouTubeDatabaseFilter.UNLOCKED -> !item.isEncrypted
                YouTubeDatabaseFilter.ENCRYPTED -> item.isEncrypted
                YouTubeDatabaseFilter.FORMAT_PRIMARY -> extLower == "mov" || extLower == "mp4" || extLower == "mkv"
                YouTubeDatabaseFilter.LARGE_FILE -> item.sizeBytes >= 5L * 1024L * 1024L
                YouTubeDatabaseFilter.FAVORITES -> folderLabel == "Favorit"
                YouTubeDatabaseFilter.RECENT -> true
            }
            folderMatch && queryMatch && statusMatch
        }
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 24.dp)
                .widthIn(max = 560.dp)
                .testTag("yt_vault_database_dialog"),
            shape = RoundedCornerShape(26.dp),
            colors = CardDefaults.cardColors(containerColor = YtCardWhite),
            border = BorderStroke(2.dp, YtBorder)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(18.dp)
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(YouTubeRed),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Storage,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(22.dp)
                            )
                        }
                        Column {
                            Text(
                                text = "Database Video Brankas #$activeVaultId",
                                style = MaterialTheme.typography.titleMedium,
                                color = YtInk,
                                fontWeight = FontWeight.ExtraBold
                            )
                            Text(
                                text = "${allVideoTracks.size} Video • Total ${formatFileSize(totalSizeBytes)}",
                                style = MaterialTheme.typography.labelSmall,
                                color = YtSubtle
                            )
                        }
                    }

                    IconButton(onClick = onDismiss) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Tutup Database",
                            tint = YtInk
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Database Stats Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Surface(
                        modifier = Modifier.weight(1f),
                        color = YtChipBg,
                        shape = RoundedCornerShape(12.dp),
                        border = BorderStroke(1.dp, YtBorder)
                    ) {
                        Column(
                            modifier = Modifier.padding(10.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "${allVideoTracks.size}",
                                style = MaterialTheme.typography.titleMedium,
                                color = YtInk,
                                fontWeight = FontWeight.ExtraBold
                            )
                            Text(
                                text = "Total Video",
                                style = MaterialTheme.typography.labelSmall,
                                color = YtSubtle
                            )
                        }
                    }
                    Surface(
                        modifier = Modifier.weight(1f),
                        color = VaultLime.copy(alpha = 0.12f),
                        shape = RoundedCornerShape(12.dp),
                        border = BorderStroke(1.dp, VaultLime)
                    ) {
                        Column(
                            modifier = Modifier.padding(10.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "$unlockedCount",
                                style = MaterialTheme.typography.titleMedium,
                                color = VaultLime,
                                fontWeight = FontWeight.ExtraBold
                            )
                            Text(
                                text = "Siap Putar",
                                style = MaterialTheme.typography.labelSmall,
                                color = YtInk
                            )
                        }
                    }
                    Surface(
                        modifier = Modifier.weight(1f),
                        color = VaultAmber.copy(alpha = 0.14f),
                        shape = RoundedCornerShape(12.dp),
                        border = BorderStroke(1.dp, VaultAmber)
                    ) {
                        Column(
                            modifier = Modifier.padding(10.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "$encryptedCount",
                                style = MaterialTheme.typography.titleMedium,
                                color = VaultAmber,
                                fontWeight = FontWeight.ExtraBold
                            )
                            Text(
                                text = "Terkunci AES",
                                style = MaterialTheme.typography.labelSmall,
                                color = YtInk
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Search Inside Vault Video Database
                OutlinedTextField(
                    value = dbQuery,
                    onValueChange = { dbQuery = it },
                    placeholder = {
                        Text(
                            text = "Cari nama video di database brankas...",
                            style = MaterialTheme.typography.bodySmall
                        )
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = null,
                            tint = YtSubtle
                        )
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(14.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = VaultCoral,
                        unfocusedBorderColor = YtBorder,
                        focusedContainerColor = YtChipBg,
                        unfocusedContainerColor = YtChipBg,
                        focusedTextColor = YtInk,
                        unfocusedTextColor = YtInk
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Filter Chips Inside Database Modal
                LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    items(YouTubeDatabaseFilter.entries) { filter ->
                        val selected = dbFilter == filter
                        Surface(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .clickable { dbFilter = filter },
                            color = if (selected) YtInk else YtChipBg,
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(
                                text = filter.label,
                                style = MaterialTheme.typography.labelSmall,
                                color = if (selected) Color.White else YtInk,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                            )
                        }
                    }
                    items(playlistFolders) { folder ->
                        val selected = activeMediaFolder == folder
                        Surface(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .clickable { onSelectMediaFolder(folder) },
                            color = if (selected) VaultCoral else YtChipBg,
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(
                                text = folder,
                                style = MaterialTheme.typography.labelSmall,
                                color = if (selected) Color.White else YtInk,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Video List in Database
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 320.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    if (filteredDbVideos.isEmpty()) {
                        item {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(20.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Icon(
                                    imageVector = Icons.Default.VideoFile,
                                    contentDescription = null,
                                    tint = YtSubtle,
                                    modifier = Modifier.size(36.dp)
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = "Tidak ada video yang cocok dengan filter ini.",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = YtSubtle
                                )
                            }
                        }
                    } else {
                        items(filteredDbVideos, key = { "db_vid_${it.id}" }) { video ->
                            val isCurrent = video.id == activeVideoId
                            Surface(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(14.dp))
                                    .clickable { onSelectAndPlayVideo(video) },
                                color = if (isCurrent) VaultCoral.copy(alpha = 0.12f) else YtChipBg,
                                border = BorderStroke(
                                    width = 1.5.dp,
                                    color = if (isCurrent) VaultCoral else YtBorder
                                )
                            ) {
                                Row(
                                    modifier = Modifier.padding(10.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(width = 64.dp, height = 40.dp)
                                            .clip(RoundedCornerShape(8.dp))
                                            .background(YouTubeDarkCanvas),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            imageVector = when {
                                                video.isEncrypted -> Icons.Default.Lock
                                                isCurrent && isPlaying -> Icons.Default.Equalizer
                                                else -> Icons.Default.PlayArrow
                                            },
                                            contentDescription = null,
                                            tint = if (video.isEncrypted) VaultAmber else YouTubeRed,
                                            modifier = Modifier.size(20.dp)
                                        )
                                    }

                                    Column(modifier = Modifier.weight(1f)) {
                                        TruncatedHoldTooltipText(
                                            text = "${video.name}.${video.extension}",
                                            style = MaterialTheme.typography.labelLarge,
                                            color = YtInk,
                                            fontWeight = FontWeight.ExtraBold,
                                            maxLines = 1,
                                            onClick = { onSelectAndPlayVideo(video) }
                                        )
                                        Text(
                                            text = "${video.playlistFolderLabel()} • ${formatFileSize(video.sizeBytes)} • ${formatSeconds(video.durationSeconds.coerceAtLeast(15))}",
                                            style = MaterialTheme.typography.labelSmall,
                                            color = YtSubtle
                                        )
                                    }

                                    IconButton(
                                        onClick = { onTogglePinEncrypt(video) },
                                        modifier = Modifier.size(30.dp)
                                    ) {
                                        Icon(
                                            imageVector = if (video.isEncrypted) Icons.Default.Lock else Icons.Default.LockOpen,
                                            contentDescription = "Kunci / Buka",
                                            tint = if (video.isEncrypted) VaultAmber else VaultLime,
                                            modifier = Modifier.size(16.dp)
                                        )
                                    }

                                    IconButton(
                                        onClick = { onMoveToPlaylist(video) },
                                        modifier = Modifier.size(30.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.DriveFileMove,
                                            contentDescription = "Pindah Playlist",
                                            tint = YtCyan,
                                            modifier = Modifier.size(16.dp)
                                        )
                                    }

                                    IconButton(
                                        onClick = { onOpenFullscreenVideo(video) },
                                        modifier = Modifier.size(30.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Fullscreen,
                                            contentDescription = "Layar Penuh",
                                            tint = YtInk,
                                            modifier = Modifier.size(18.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Bottom Bar: Impor Video ke Database + Tutup
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .clickable { onImportNewVideoClick() },
                        color = VaultLime
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.UploadFile,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(16.dp)
                            )
                            Text(
                                text = "Impor Video Baru",
                                style = MaterialTheme.typography.labelLarge,
                                color = Color.White,
                                fontWeight = FontWeight.ExtraBold
                            )
                        }
                    }

                    Surface(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .clickable { onDismiss() },
                        color = YtChipBg
                    ) {
                        Text(
                            text = "Tutup",
                            style = MaterialTheme.typography.labelLarge,
                            color = YtInk,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp)
                        )
                    }
                }
            }
        }
    }
}

/**
 * YouTube Player Settings Sheet / Dialog
 * Controls Video Quality preset, Playback Speed, Repeat Mode, Autoplay, Subtitles HUD, Lock Screen, and Stats for Nerds.
 */
@Composable
private fun YouTubePlayerSettingsDialog(
    mediaState: MediaPlayerUiState,
    selectedQuality: String,
    isAutoplayEnabled: Boolean,
    isSubtitlesHudEnabled: Boolean,
    showStatsForNerds: Boolean,
    isPlayerScreenLocked: Boolean,
    onSelectQuality: (String) -> Unit,
    onCycleSpeed: () -> Unit,
    onToggleRepeat: () -> Unit,
    onToggleAutoplay: () -> Unit,
    onToggleSubtitlesHud: () -> Unit,
    onToggleStatsForNerds: () -> Unit,
    onToggleLockScreen: () -> Unit,
    onDismiss: () -> Unit
) {
    val qualityPresets = listOf(
        "1080p HD (Asli Brankas)",
        "720p HD (Hemat Baterai)",
        "480p Standar",
        "Otomatis (Dekoder Lokal)"
    )

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
                .testTag("yt_player_settings_dialog"),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = YtCardWhite),
            border = BorderStroke(2.dp, YtBorder)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(18.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = null,
                            tint = VaultCoral
                        )
                        Text(
                            text = "Pengaturan Pemutar Video",
                            style = MaterialTheme.typography.titleMedium,
                            color = YtInk,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }
                    IconButton(onClick = onDismiss, modifier = Modifier.size(28.dp)) {
                        Icon(Icons.Default.Close, contentDescription = "Tutup", tint = YtInk)
                    }
                }

                HorizontalDivider(color = YtBorder)

                // Quality Selector
                Text(
                    text = "Kualitas Video Database",
                    style = MaterialTheme.typography.labelLarge,
                    color = YtInk,
                    fontWeight = FontWeight.ExtraBold
                )
                qualityPresets.forEach { preset ->
                    val isSelected = selectedQuality == preset
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .clickable { onSelectQuality(preset) },
                        color = if (isSelected) VaultCoral.copy(alpha = 0.12f) else YtChipBg
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.HighQuality,
                                    contentDescription = null,
                                    tint = if (isSelected) VaultCoral else YtSubtle,
                                    modifier = Modifier.size(18.dp)
                                )
                                Text(
                                    text = preset,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = YtInk,
                                    fontWeight = if (isSelected) FontWeight.ExtraBold else FontWeight.Medium
                                )
                            }
                            if (isSelected) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = null,
                                    tint = VaultCoral,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }
                    }
                }

                // Speed & Repeat Controls
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Surface(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(12.dp))
                            .clickable { onCycleSpeed() },
                        color = YtChipBg,
                        border = BorderStroke(1.dp, YtBorder)
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Speed,
                                contentDescription = null,
                                tint = YtCyan,
                                modifier = Modifier.size(18.dp)
                            )
                            Column {
                                Text(
                                    text = "Kecepatan",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = YtSubtle
                                )
                                Text(
                                    text = "${mediaState.playbackSpeed}x (Ketuk Ubah)",
                                    style = MaterialTheme.typography.labelLarge,
                                    color = YtInk,
                                    fontWeight = FontWeight.ExtraBold
                                )
                            }
                        }
                    }

                    Surface(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(12.dp))
                            .clickable { onToggleRepeat() },
                        color = YtChipBg,
                        border = BorderStroke(1.dp, YtBorder)
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                imageVector = if (mediaState.repeatMode == PlaybackRepeatMode.REPEAT_ONE) {
                                    Icons.Default.RepeatOne
                                } else {
                                    Icons.Default.Repeat
                                },
                                contentDescription = null,
                                tint = YtPurple,
                                modifier = Modifier.size(18.dp)
                            )
                            Column {
                                Text(
                                    text = "Mode Putar",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = YtSubtle
                                )
                                Text(
                                    text = when (mediaState.repeatMode) {
                                        PlaybackRepeatMode.REPEAT_ONE -> "Ulangi 1 Video"
                                        PlaybackRepeatMode.AUTO_ADVANCE -> "Auto Lanjut"
                                        PlaybackRepeatMode.REPEAT_PLAYLIST -> "Ulangi Playlist"
                                    },
                                    style = MaterialTheme.typography.labelLarge,
                                    color = YtInk,
                                    fontWeight = FontWeight.ExtraBold
                                )
                            }
                        }
                    }
                }

                // Switches: Autoplay, CC/Subtitles, Stats for Nerds, Lock Screen
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Putar Otomatis Video Berikutnya",
                        style = MaterialTheme.typography.bodySmall,
                        color = YtInk,
                        fontWeight = FontWeight.SemiBold
                    )
                    Switch(
                        checked = isAutoplayEnabled,
                        onCheckedChange = { onToggleAutoplay() },
                        colors = SwitchDefaults.colors(checkedTrackColor = VaultCoral)
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Tampilkan Teks Info Video (CC)",
                        style = MaterialTheme.typography.bodySmall,
                        color = YtInk,
                        fontWeight = FontWeight.SemiBold
                    )
                    Switch(
                        checked = isSubtitlesHudEnabled,
                        onCheckedChange = { onToggleSubtitlesHud() },
                        colors = SwitchDefaults.colors(checkedTrackColor = VaultCoral)
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Statistik Database (Stats for Nerds)",
                        style = MaterialTheme.typography.bodySmall,
                        color = YtInk,
                        fontWeight = FontWeight.SemiBold
                    )
                    Switch(
                        checked = showStatsForNerds,
                        onCheckedChange = { onToggleStatsForNerds() },
                        colors = SwitchDefaults.colors(checkedTrackColor = YtCyan)
                    )
                }

                // Lock Screen Action
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .clickable { onToggleLockScreen() },
                    color = YtChipBg,
                    border = BorderStroke(1.dp, YtBorder)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Lock,
                            contentDescription = null,
                            tint = YtInk,
                            modifier = Modifier.size(18.dp)
                        )
                        Text(
                            text = if (isPlayerScreenLocked) "Buka Kunci Sentuhan Layar" else "Kunci Layar Pemutar (Cegah Sentuhan)",
                            style = MaterialTheme.typography.labelLarge,
                            color = YtInk,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

/**
 * Dedicated Sonic Audio Deck Hero Card — Spotify Layout & Proportions + Duolingo 3D Tactile Theme
 */
@Composable
private fun SonicAudioDeckHeroCard(
    audioItem: VaultItemEntity?,
    mediaState: MediaPlayerUiState,
    eqPhase: Float,
    onTogglePlayPause: () -> Unit,
    onSeekToSeconds: (Int) -> Unit,
    onSkipTrack: (Boolean) -> Unit,
    onCycleSpeed: () -> Unit,
    onToggleRepeat: () -> Unit,
    onImportClick: () -> Unit
) {
    var isLikedTrack by remember(audioItem?.id) { mutableStateOf(true) }
    var isShuffleActive by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .duolingoCard(
                backgroundColor = YtCardWhite,
                borderColor = YtBorder,
                cornerRadius = 28.dp,
                lipHeight = 6.dp
            )
            .padding(bottom = 6.dp)
            .testTag("sonic_audio_deck_card"),
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(containerColor = YtCardWhite)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 22.dp, vertical = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (audioItem == null) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(84.dp)
                            .duolingoButton(
                                faceColor = com.example.ui.theme.DuoFeatherGreen,
                                lipColor = com.example.ui.theme.DuoFeatherGreenLip,
                                cornerRadius = 24.dp,
                                lipHeight = 5.dp
                            )
                            .padding(bottom = 5.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Headphones,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(42.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(14.dp))
                    Text(
                        text = "Dek Audio Siap Memutar Musik",
                        style = MaterialTheme.typography.titleMedium,
                        color = YtInk,
                        fontWeight = FontWeight.ExtraBold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Impor file MP3, FLAC, WAV, atau M4A untuk memutar musik dengan tata letak Spotify.",
                        style = MaterialTheme.typography.bodySmall,
                        color = YtSubtle,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Box(
                        modifier = Modifier
                            .height(48.dp)
                            .duolingoButton(
                                faceColor = com.example.ui.theme.DuoFeatherGreen,
                                lipColor = com.example.ui.theme.DuoFeatherGreenLip,
                                cornerRadius = 16.dp,
                                lipHeight = 4.dp
                            )
                            .clickable { onImportClick() }
                            .padding(horizontal = 20.dp)
                            .padding(bottom = 4.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "+ Impor Lagu / Audio",
                            style = MaterialTheme.typography.labelLarge,
                            color = Color.White,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }
                }
            } else {
                // 1. SPOTIFY TOP HEADER BAR: Down Chevron + "MEMUTAR DARI PLAYLIST" + Format Pill
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .duolingoCard(
                                backgroundColor = DuoSnowCanvas,
                                borderColor = YtBorder,
                                cornerRadius = 12.dp,
                                lipHeight = 2.dp
                            )
                            .padding(bottom = 2.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.ExpandMore,
                            contentDescription = null,
                            tint = YtInk,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "MEMUTAR DARI BRANKAS",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.ExtraBold,
                            letterSpacing = 1.4.sp,
                            color = YtSubtle
                        )
                        Text(
                            text = audioItem.playlistFolderLabel(),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = YtInk,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }

                    Box(
                        modifier = Modifier
                            .duolingoButton(
                                faceColor = com.example.ui.theme.DuoFeatherGreen,
                                lipColor = com.example.ui.theme.DuoFeatherGreenLip,
                                cornerRadius = 10.dp,
                                lipHeight = 2.dp
                            )
                            .padding(horizontal = 9.dp, vertical = 4.dp)
                            .padding(bottom = 2.dp)
                    ) {
                        Text(
                            text = audioItem.extension.uppercase(),
                            fontFamily = JetBrainsMonoFontFamily,
                            fontSize = 10.sp,
                            color = Color.White,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

                // 2. SPOTIFY CENTERPIECE: LARGE 1:1 SQUARE 3D DUOLINGO ALBUM ARTWORK STAGE + LIVE SPECTRUM
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.86f)
                        .aspectRatio(1f)
                        .duolingoCard(
                            backgroundColor = DuoSnowCanvas,
                            borderColor = com.example.ui.theme.DuoFeatherGreen,
                            lipColor = com.example.ui.theme.DuoFeatherGreenLip,
                            cornerRadius = 28.dp,
                            borderWidth = 3.dp,
                            lipHeight = 7.dp
                        )
                        .padding(bottom = 7.dp)
                ) {
                    // Vibrant Duolingo Vinyl / Album Backdrop Canvas
                    Canvas(modifier = Modifier.fillMaxSize()) {
                        val w = size.width
                        val h = size.height
                        drawRect(
                            brush = Brush.linearGradient(
                                colors = listOf(
                                    Color(0xFFE9FCD4),
                                    Color(0xFFDDF4FF),
                                    Color(0xFFF3E5F5)
                                )
                            )
                        )
                        // Concentric Tactile Vinyl Grooves
                        drawCircle(
                            color = Color(0xFF58CC02).copy(alpha = 0.14f),
                            radius = w * 0.42f,
                            center = Offset(w * 0.5f, h * 0.44f)
                        )
                        drawCircle(
                            color = Color(0xFF1CB0F6).copy(alpha = 0.16f),
                            radius = w * 0.30f,
                            center = Offset(w * 0.5f, h * 0.44f)
                        )
                    }

                    // Center Mascot & Album Badge
                    Column(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(bottom = 28.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        NyxMascotAvatar(
                            mood = if (mediaState.isPlaying) MascotMood.HAPPY_VIBES else MascotMood.WAVE_HELLO,
                            size = 86.dp
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Box(
                            modifier = Modifier
                                .duolingoButton(
                                    faceColor = DuoMacawBlue,
                                    lipColor = com.example.ui.theme.DuoMacawBlueLip,
                                    cornerRadius = 999.dp,
                                    lipHeight = 2.dp
                                )
                                .padding(horizontal = 12.dp, vertical = 4.dp)
                                .padding(bottom = 2.dp)
                        ) {
                            Text(
                                text = if (mediaState.isPlaying) "NOW SPINNING • 320 KBPS" else "PAUSED • HI-FI VAULT",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = Color.White
                            )
                        }
                    }

                    // Bottom 24-Bar Equalizer Spectrum inside Album Art
                    Canvas(
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .fillMaxWidth()
                            .padding(horizontal = 18.dp, vertical = 14.dp)
                            .height(44.dp)
                    ) {
                        val barCount = 24
                        val spacing = 4.dp.toPx()
                        val totalSpacing = spacing * (barCount - 1)
                        val barWidth = (size.width - totalSpacing) / barCount

                        for (i in 0 until barCount) {
                            val normalized = if (mediaState.isPlaying) {
                                val wave = sin(eqPhase + i * 0.45f) * 0.4f + 0.55f
                                wave.coerceIn(0.15f, 0.98f)
                            } else {
                                0.18f + (i % 4) * 0.05f
                            }

                            val barHeight = size.height * normalized
                            val topY = size.height - barHeight
                            val barColor = when {
                                i % 3 == 0 -> com.example.ui.theme.DuoFeatherGreen
                                i % 2 == 0 -> DuoMacawBlue
                                else -> DuoBeetlePurple
                            }

                            drawRoundRect(
                                color = barColor,
                                topLeft = Offset(i * (barWidth + spacing), topY),
                                size = Size(barWidth, barHeight),
                                cornerRadius = CornerRadius(6.dp.toPx(), 6.dp.toPx())
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // 3. SPOTIFY TRACK TITLE, ARTIST/SUBTITLE & LIKE BUTTON ROW
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .padding(end = 12.dp)
                    ) {
                        TruncatedHoldTooltipText(
                            text = audioItem.name,
                            style = MaterialTheme.typography.titleLarge.copy(fontSize = 20.sp),
                            color = YtInk,
                            fontWeight = FontWeight.ExtraBold,
                            maxLines = 1
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "${audioItem.playlistFolderLabel()} • ${formatFileSize(audioItem.sizeBytes)}",
                            style = MaterialTheme.typography.bodyMedium,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = YtSubtle,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }

                    // Spotify Right-Aligned Like / Heart 3D Button
                    Box(
                        modifier = Modifier
                            .size(42.dp)
                            .duolingoButton(
                                faceColor = if (isLikedTrack) com.example.ui.theme.DuoFeatherGreen else DuoSnowCanvas,
                                lipColor = if (isLikedTrack) com.example.ui.theme.DuoFeatherGreenLip else com.example.ui.theme.DuoHareLip,
                                cornerRadius = 14.dp,
                                lipHeight = 3.dp
                            )
                            .clickable { isLikedTrack = !isLikedTrack }
                            .padding(bottom = 3.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = if (isLikedTrack) Icons.Default.CheckCircle else Icons.Default.ThumbUp,
                            contentDescription = "Disukai",
                            tint = if (isLikedTrack) Color.White else YtInk,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // 4. SPOTIFY SCRUBBER SLIDER + TIMECODE LABELS
                Slider(
                    value = mediaState.currentSeconds.toFloat(),
                    onValueChange = { onSeekToSeconds(it.toInt()) },
                    valueRange = 0f..mediaState.totalSeconds.coerceAtLeast(1).toFloat(),
                    colors = SliderDefaults.colors(
                        thumbColor = com.example.ui.theme.DuoFeatherGreen,
                        activeTrackColor = com.example.ui.theme.DuoFeatherGreen,
                        inactiveTrackColor = YtBorder
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = formatSeconds(mediaState.currentSeconds),
                        fontFamily = JetBrainsMonoFontFamily,
                        fontSize = 11.sp,
                        color = com.example.ui.theme.DuoFeatherGreenLip,
                        fontWeight = FontWeight.ExtraBold
                    )
                    Text(
                        text = formatSeconds(mediaState.totalSeconds),
                        fontFamily = JetBrainsMonoFontFamily,
                        fontSize = 11.sp,
                        color = YtSubtle,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                // 5. SPOTIFY 5-BUTTON TRANSPORT ROW: [Shuffle] [SkipPrev] [68dp 3D Play/Pause] [SkipNext] [Repeat]
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Shuffle / Mode 3D Button
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .duolingoButton(
                                faceColor = if (isShuffleActive) com.example.ui.theme.DuoFeatherGreen else DuoSnowCanvas,
                                lipColor = if (isShuffleActive) com.example.ui.theme.DuoFeatherGreenLip else com.example.ui.theme.DuoHareLip,
                                cornerRadius = 14.dp,
                                lipHeight = 3.dp
                            )
                            .clickable { isShuffleActive = !isShuffleActive }
                            .padding(bottom = 3.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Equalizer,
                            contentDescription = "Shuffle / Equalizer",
                            tint = if (isShuffleActive) Color.White else YtInk,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    // Skip Previous 3D Button
                    Box(
                        modifier = Modifier
                            .size(50.dp)
                            .duolingoButton(
                                faceColor = DuoPureWhite,
                                lipColor = com.example.ui.theme.DuoHareLip,
                                cornerRadius = 16.dp,
                                lipHeight = 4.dp
                            )
                            .border(2.dp, YtBorder, RoundedCornerShape(16.dp))
                            .clickable { onSkipTrack(false) }
                            .padding(bottom = 4.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.SkipPrevious,
                            contentDescription = "Sebelumnya",
                            tint = YtInk,
                            modifier = Modifier.size(28.dp)
                        )
                    }

                    // Center Spotify Large 68.dp Circular Duolingo 3D Play/Pause Button
                    Box(
                        modifier = Modifier
                            .size(68.dp)
                            .duolingoButton(
                                faceColor = com.example.ui.theme.DuoFeatherGreen,
                                lipColor = com.example.ui.theme.DuoFeatherGreenLip,
                                cornerRadius = 34.dp,
                                lipHeight = 5.dp
                            )
                            .clickable { onTogglePlayPause() }
                            .padding(bottom = 5.dp)
                            .testTag("audio_deck_play_pause"),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = if (mediaState.isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                            contentDescription = "Putar / Jeda",
                            tint = Color.White,
                            modifier = Modifier.size(34.dp)
                        )
                    }

                    // Skip Next 3D Button
                    Box(
                        modifier = Modifier
                            .size(50.dp)
                            .duolingoButton(
                                faceColor = DuoPureWhite,
                                lipColor = com.example.ui.theme.DuoHareLip,
                                cornerRadius = 16.dp,
                                lipHeight = 4.dp
                            )
                            .border(2.dp, YtBorder, RoundedCornerShape(16.dp))
                            .clickable { onSkipTrack(true) }
                            .padding(bottom = 4.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.SkipNext,
                            contentDescription = "Berikutnya",
                            tint = YtInk,
                            modifier = Modifier.size(28.dp)
                        )
                    }

                    // Repeat Mode 3D Button
                    val isRepeatActive = mediaState.repeatMode != PlaybackRepeatMode.AUTO_ADVANCE
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .duolingoButton(
                                faceColor = if (isRepeatActive) DuoMacawBlue else DuoSnowCanvas,
                                lipColor = if (isRepeatActive) com.example.ui.theme.DuoMacawBlueLip else com.example.ui.theme.DuoHareLip,
                                cornerRadius = 14.dp,
                                lipHeight = 3.dp
                            )
                            .clickable { onToggleRepeat() }
                            .padding(bottom = 3.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = if (mediaState.repeatMode == PlaybackRepeatMode.REPEAT_ONE) {
                                Icons.Default.RepeatOne
                            } else {
                                Icons.Default.Repeat
                            },
                            contentDescription = "Mode Ulang",
                            tint = if (isRepeatActive) Color.White else YtInk,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // 6. SPOTIFY BOTTOM UTILITY DOCK: [-5s] [Speed 1.0x] [Repeat Label] [+5s] [+ Impor]
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(38.dp)
                            .duolingoButton(
                                faceColor = DuoSnowCanvas,
                                lipColor = com.example.ui.theme.DuoHareLip,
                                cornerRadius = 12.dp,
                                lipHeight = 3.dp
                            )
                            .clickable {
                                onSeekToSeconds((mediaState.currentSeconds - 5).coerceAtLeast(0))
                            }
                            .padding(bottom = 3.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "-5s",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = YtInk
                        )
                    }

                    Box(
                        modifier = Modifier
                            .weight(1.1f)
                            .height(38.dp)
                            .duolingoButton(
                                faceColor = DuoMacawBlue,
                                lipColor = com.example.ui.theme.DuoMacawBlueLip,
                                cornerRadius = 12.dp,
                                lipHeight = 3.dp
                            )
                            .clickable { onCycleSpeed() }
                            .padding(bottom = 3.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "${mediaState.playbackSpeed}x SPEED",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color.White
                        )
                    }

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(38.dp)
                            .duolingoButton(
                                faceColor = DuoSnowCanvas,
                                lipColor = com.example.ui.theme.DuoHareLip,
                                cornerRadius = 12.dp,
                                lipHeight = 3.dp
                            )
                            .clickable {
                                onSeekToSeconds((mediaState.currentSeconds + 5).coerceAtMost(mediaState.totalSeconds))
                            }
                            .padding(bottom = 3.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "+5s",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = YtInk
                        )
                    }

                    Box(
                        modifier = Modifier
                            .weight(1.2f)
                            .height(38.dp)
                            .duolingoButton(
                                faceColor = DuoBeetlePurple,
                                lipColor = com.example.ui.theme.DuoBeetlePurpleLip,
                                cornerRadius = 12.dp,
                                lipHeight = 3.dp
                            )
                            .clickable { onImportClick() }
                            .padding(bottom = 3.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "+ IMPOR",
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

@Composable
private fun EmptyMediaCategoryCard(
    title: String,
    subtitle: String,
    onImportClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .duolingoCard(
                backgroundColor = YtCardWhite,
                borderColor = YtBorder,
                cornerRadius = 20.dp,
                lipHeight = 4.dp
            ),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = YtCardWhite)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                color = YtInk,
                fontWeight = FontWeight.ExtraBold,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = YtSubtle,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(12.dp))
            Surface(
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .clickable { onImportClick() },
                color = VaultCoral
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 9.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.UploadFile,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = "Impor ke Database",
                        style = MaterialTheme.typography.labelLarge,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
private fun MediaTrackRowCard(
    index: Int,
    item: VaultItemEntity,
    isSelected: Boolean,
    isPlaying: Boolean,
    onPlayClick: () -> Unit,
    onOpenViewerClick: () -> Unit,
    onMoveToPlaylistClick: () -> Unit,
    onTogglePinLock: () -> Unit,
    onShareBiometric: () -> Unit = {},
    onExportBiometric: () -> Unit = {}
) {
    val isVideo = item.category == VaultCategory.VIDEO.name

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .duolingoCard(
                backgroundColor = YtCardWhite,
                borderColor = if (isSelected) VaultLime else YtBorder,
                cornerRadius = 18.dp,
                lipHeight = 4.dp
            )
            .clickable { onPlayClick() }
            .testTag("media_track_row_${item.id}"),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = YtCardWhite)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.weight(1f),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(if (isSelected) VaultLime else YtChipBg),
                    contentAlignment = Alignment.Center
                ) {
                    if (isSelected) {
                        Icon(
                            imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                            contentDescription = null,
                            tint = Color.White
                        )
                    } else {
                        Icon(
                            imageVector = if (isVideo) Icons.Default.Movie else Icons.Default.MusicNote,
                            contentDescription = null,
                            tint = YtInk,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }

                Column(modifier = Modifier.weight(1f)) {
                    TruncatedHoldTooltipText(
                        text = "$index. ${item.name}.${item.extension}",
                        style = MaterialTheme.typography.titleSmall,
                        color = YtInk,
                        fontWeight = FontWeight.ExtraBold,
                        maxLines = 1,
                        onClick = { onPlayClick() }
                    )
                    Text(
                        text = buildString {
                            append(item.playlistFolderLabel())
                            append(" • ${formatSeconds(item.durationSeconds.coerceAtLeast(15))}")
                            append(" • ${formatFileSize(item.sizeBytes)}")
                        },
                        style = MaterialTheme.typography.bodySmall,
                        color = YtSubtle
                    )
                }
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                IconButton(
                    onClick = onMoveToPlaylistClick,
                    modifier = Modifier
                        .size(32.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(YtChipBg)
                ) {
                    Icon(
                        imageVector = Icons.Default.DriveFileMove,
                        contentDescription = "Pindah ke Playlist",
                        tint = YtCyan,
                        modifier = Modifier.size(16.dp)
                    )
                }

                IconButton(
                    onClick = onShareBiometric,
                    modifier = Modifier
                        .size(32.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(YtChipBg)
                        .testTag("media_row_share_${item.id}")
                ) {
                    Icon(
                        imageVector = Icons.Default.Share,
                        contentDescription = "Bagikan",
                        tint = YtInk,
                        modifier = Modifier.size(16.dp)
                    )
                }

                IconButton(
                    onClick = onOpenViewerClick,
                    modifier = Modifier
                        .size(32.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(YtChipBg)
                        .testTag("media_row_open_viewer_${item.id}")
                ) {
                    Icon(
                        imageVector = Icons.Default.Fullscreen,
                        contentDescription = "Layar Penuh",
                        tint = YtInk,
                        modifier = Modifier.size(17.dp)
                    )
                }

                IconButton(
                    onClick = onTogglePinLock,
                    modifier = Modifier
                        .size(32.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(YtChipBg)
                        .testTag("media_row_pin_lock_${item.id}")
                ) {
                    Icon(
                        imageVector = if (item.isEncrypted) Icons.Default.Lock else Icons.Default.LockOpen,
                        contentDescription = "PIN Kunci / Buka",
                        tint = if (item.isEncrypted) VaultAmber else VaultLime,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }
    }
}

fun extractLocalMediaDurationSeconds(path: String, fallbackSeconds: Int): Int {
    return try {
        val file = File(path)
        if (!file.exists()) return fallbackSeconds
        val retriever = MediaMetadataRetriever()
        retriever.setDataSource(file.absolutePath)
        val durationMs = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)?.toLongOrNull()
        retriever.release()
        if (durationMs != null && durationMs > 0L) {
            (durationMs / 1000L).toInt().coerceAtLeast(1)
        } else {
            fallbackSeconds
        }
    } catch (_: Exception) {
        fallbackSeconds
    }
}

fun formatSeconds(seconds: Int): String {
    val safe = seconds.coerceAtLeast(0)
    val mins = safe / 60
    val secs = safe % 60
    return String.format(Locale.US, "%02d:%02d", mins, secs)
}

/**
 * Format durasi ringkas dengan dukungan sub-detik (minimal 0.5 detik) untuk video pendek < 10 detik.
 */
fun formatDurationMsCompact(currentMs: Long, totalMs: Long): String {
    val safeTotalMs = totalMs.coerceAtLeast(500L)
    val safeCurMs = currentMs.coerceIn(0L, safeTotalMs)
    return if (safeTotalMs < 10_000L) {
        val curSecFloat = safeCurMs / 1000f
        val totSecFloat = safeTotalMs / 1000f
        String.format(Locale.US, "0:%04.1f / 0:%04.1f", curSecFloat, totSecFloat)
    } else {
        "${formatSeconds((safeCurMs / 1000L).toInt())} / ${formatSeconds((safeTotalMs / 1000L).toInt())}"
    }
}

