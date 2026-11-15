package com.example.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.QueueMusic
import androidx.compose.material.icons.filled.Album
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.CompassCalibration
import androidx.compose.material.icons.filled.DownloadDone
import androidx.compose.material.icons.filled.Equalizer
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.FolderSpecial
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.Headphones
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.LibraryMusic
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Lyrics
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Repeat
import androidx.compose.material.icons.filled.RepeatOne
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Shuffle
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material.icons.filled.SkipPrevious
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.Whatshot
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.VaultItemEntity
import com.example.media.AuralysAudioController
import com.example.media.AuralysRepeatMode
import com.example.media.AuralysTrack
import com.example.media.QueueSourceMode
import kotlinx.coroutines.launch
import kotlin.math.abs
import kotlin.math.sin

enum class AuralysNavTab(val label: String, val icon: ImageVector) {
    HOME("Home", Icons.Default.Home),
    EXPLORE("Explore", Icons.Default.CompassCalibration),
    LIBRARY("Library", Icons.Default.LibraryMusic),
    SEARCH("Search", Icons.Default.Search)
}

val AuralysDarkBg = Color(0xFF0A0E17)
val AuralysCardBg = Color(0xFF131A29)
val AuralysSurfaceCard = Color(0xFF131A29)
val AuralysCardBorder = Color(0xFF1E293B)
val AuralysAccentCyan = Color(0xFF00F2FE)
val AuralysAccentIndigo = Color(0xFF4FACFE)
val AuralysTextMuted = Color(0xFF94A3B8)
val AuralysTextPrimary = Color(0xFFF8FAFC)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AuralysMusicChamberScreen(
    vaultItems: List<VaultItemEntity>,
    onLockAndExit: () -> Unit,
    onPostBanner: (String) -> Unit
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val controller = remember { AuralysAudioController(context, coroutineScope) }

    val currentTrack by controller.currentTrack.collectAsState()
    val isPlaying by controller.isPlaying.collectAsState()
    val currentPosSec by controller.currentPositionSec.collectAsState()
    val totalDurSec by controller.totalDurationSec.collectAsState()
    val liveAmplitude by controller.liveAmplitude.collectAsState()
    val playbackSpeed by controller.playbackSpeed.collectAsState()
    val repeatMode by controller.repeatMode.collectAsState()
    val isShuffle by controller.isShuffleEnabled.collectAsState()
    val sleepTimerSec by controller.sleepTimerRemainingSec.collectAsState()
    val queue by controller.queue.collectAsState()
    val queueIndex by controller.queueIndex.collectAsState()

    var activeTab by remember { mutableStateOf(AuralysNavTab.HOME) }
    var isNowPlayingExpanded by remember { mutableStateOf(false) }
    var showSpeedModal by remember { mutableStateOf(false) }
    var showSleepTimerModal by remember { mutableStateOf(false) }
    var showQueueDrawer by remember { mutableStateOf(false) }
    var showLyricsSheet by remember { mutableStateOf(false) }

    // State pencarian
    var searchQuery by remember { mutableStateOf("") }
    var favoriteTrackIds by remember { mutableStateOf(setOf("auralys_track_01")) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(AuralysDarkBg)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.statusBars)
        ) {
            // Header Utama Auralys
            AuralysTopBar(
                activeTab = activeTab,
                onLockAndExit = onLockAndExit
            )

            // Konten Tab Utama
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                when (activeTab) {
                    AuralysNavTab.HOME -> AuralysHomeContent(
                        queue = queue,
                        currentTrack = currentTrack,
                        onPlayTrack = { track -> controller.playTrack(track) }
                    )
                    AuralysNavTab.EXPLORE -> AuralysExploreContent(
                        onGenreClick = { genre ->
                            onPostBanner("Memuat koleksi genre: $genre")
                        },
                        onPlayTrack = { track -> controller.playTrack(track) }
                    )
                    AuralysNavTab.LIBRARY -> AuralysLibraryContent(
                        vaultItems = vaultItems,
                        onLoadVaultFolder = { folder ->
                            val tracks = controller.loadVaultFolderAudio(folder, vaultItems)
                            onPostBanner("Memuat ${tracks.size} audio brankas dari folder '$folder' (Folder-Scoped Queue)")
                        },
                        onPlayTrack = { track -> controller.playTrack(track) }
                    )
                    AuralysNavTab.SEARCH -> AuralysSearchContent(
                        query = searchQuery,
                        onQueryChange = { searchQuery = it },
                        queue = queue,
                        onPlayTrack = { track -> controller.playTrack(track) }
                    )
                }
            }

            // Persistent Bottom Mini-Player Bar
            if (currentTrack != null) {
                AuralysMiniPlayerBar(
                    track = currentTrack!!,
                    isPlaying = isPlaying,
                    currentPosSec = currentPosSec,
                    totalDurSec = totalDurSec,
                    onTogglePlay = { controller.togglePlayPause() },
                    onSkipNext = { controller.skipNext() },
                    onExpandClick = { isNowPlayingExpanded = true }
                )
            }

            // Bottom Navigation Bar
            AuralysBottomNavigationBar(
                activeTab = activeTab,
                onTabSelect = { activeTab = it }
            )
        }

        // Full-Screen Expanding Now-Playing Sheet
        AnimatedVisibility(
            visible = isNowPlayingExpanded && currentTrack != null,
            enter = slideInVertically(initialOffsetY = { it }, animationSpec = tween(350, easing = FastOutSlowInEasing)) + fadeIn(),
            exit = slideOutVertically(targetOffsetY = { it }, animationSpec = tween(300, easing = FastOutSlowInEasing)) + fadeOut()
        ) {
            AuralysExpandedNowPlayingSheet(
                track = currentTrack!!,
                isPlaying = isPlaying,
                currentPosSec = currentPosSec,
                totalDurSec = totalDurSec,
                liveAmplitude = liveAmplitude,
                playbackSpeed = playbackSpeed,
                repeatMode = repeatMode,
                isShuffle = isShuffle,
                sleepTimerSec = sleepTimerSec,
                isFavorite = favoriteTrackIds.contains(currentTrack?.id),
                onToggleFavorite = {
                    val id = currentTrack?.id ?: return@AuralysExpandedNowPlayingSheet
                    favoriteTrackIds = if (favoriteTrackIds.contains(id)) {
                        favoriteTrackIds - id
                    } else {
                        favoriteTrackIds + id
                    }
                },
                onTogglePlay = { controller.togglePlayPause() },
                onSkipNext = { controller.skipNext() },
                onSkipPrev = { controller.skipPrevious() },
                onSeek = { controller.seekTo(it) },
                onToggleRepeat = { controller.toggleRepeatMode() },
                onToggleShuffle = { controller.toggleShuffle() },
                onOpenSpeedModal = { showSpeedModal = true },
                onOpenSleepTimer = { showSleepTimerModal = true },
                onOpenLyrics = { showLyricsSheet = true },
                onOpenQueue = { showQueueDrawer = true },
                onCollapse = { isNowPlayingExpanded = false }
            )
        }

        // Sheet Lirik Berjalan (Synchronized Lyrics)
        if (showLyricsSheet && currentTrack != null) {
            AuralysLyricsBottomSheet(
                track = currentTrack!!,
                currentPositionMs = currentPosSec * 1000L,
                onDismiss = { showLyricsSheet = false }
            )
        }

        // Drawer Antrean Pemutaran (Up Next Queue)
        if (showQueueDrawer) {
            AuralysQueueBottomSheet(
                queue = queue,
                currentIndex = queueIndex,
                onSelectTrack = { track -> controller.playTrack(track) },
                onDismiss = { showQueueDrawer = false }
            )
        }

        // Dialog Pengaturan Kecepatan Audio
        if (showSpeedModal) {
            AuralysSpeedDialog(
                currentSpeed = playbackSpeed,
                onSelectSpeed = { speed ->
                    controller.setPlaybackSpeed(speed)
                    showSpeedModal = false
                    onPostBanner("Kecepatan audio diatur ke ${speed}x")
                },
                onDismiss = { showSpeedModal = false }
            )
        }

        // Dialog Sleep Timer
        if (showSleepTimerModal) {
            AuralysSleepTimerDialog(
                remainingSec = sleepTimerSec,
                onSetTimer = { mins ->
                    controller.setSleepTimer(mins)
                    showSleepTimerModal = false
                    onPostBanner(if (mins > 0) "Sleep timer disetel: $mins menit" else "Sleep timer dibatalkan")
                },
                onDismiss = { showSleepTimerModal = false }
            )
        }
    }
}

@Composable
private fun AuralysTopBar(
    activeTab: AuralysNavTab,
    onLockAndExit: () -> Unit
) {
    var showProfileMenu by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(Brush.linearGradient(listOf(AuralysAccentCyan, AuralysAccentIndigo))),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Headphones,
                    contentDescription = null,
                    tint = Color.Black,
                    modifier = Modifier.size(20.dp)
                )
            }
            Column {
                Text(
                    text = "Auralys",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = AuralysTextPrimary
                )
                Text(
                    text = "Music Chamber • 4.3.5",
                    fontSize = 10.sp,
                    color = AuralysAccentCyan
                )
            }
        }

        Box {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Surface(
                    shape = RoundedCornerShape(999.dp),
                    color = AuralysCardBg,
                    border = androidx.compose.foundation.BorderStroke(1.dp, AuralysCardBorder),
                    modifier = Modifier.clickable { onLockAndExit() }
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Lock,
                            contentDescription = "Stealth Lock",
                            tint = AuralysTextMuted,
                            modifier = Modifier.size(14.dp)
                        )
                        Text(
                            text = "Lock",
                            fontSize = 11.sp,
                            color = AuralysTextMuted,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }

                IconButton(
                    onClick = { showProfileMenu = true },
                    modifier = Modifier.size(36.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(30.dp)
                            .clip(CircleShape)
                            .background(AuralysCardBorder),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "A",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = AuralysTextPrimary
                        )
                    }
                }
            }

            DropdownMenu(
                expanded = showProfileMenu,
                onDismissRequest = { showProfileMenu = false }
            ) {
                DropdownMenuItem(
                    text = { Text("Kunci Sesi & Kembali ke Gate") },
                    leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
                    onClick = {
                        showProfileMenu = false
                        onLockAndExit()
                    }
                )
            }
        }
    }
}

@Composable
private fun AuralysHomeContent(
    queue: List<AuralysTrack>,
    currentTrack: AuralysTrack?,
    onPlayTrack: (AuralysTrack) -> Unit
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(bottom = 90.dp)
    ) {
        // Hero Banner Quick Pick
        if (queue.isNotEmpty()) {
            val featured = queue.first()
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .clickable { onPlayTrack(featured) },
                colors = CardDefaults.cardColors(containerColor = AuralysCardBg),
                border = androidx.compose.foundation.BorderStroke(1.dp, AuralysCardBorder)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(72.dp)
                            .clip(RoundedCornerShape(14.dp))
                            .background(Brush.linearGradient(listOf(AuralysAccentIndigo, Color(0xFF6B21A8)))),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.GraphicEq,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(36.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(14.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "QUICK PICK UNTUK ANDA",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = AuralysAccentCyan,
                            letterSpacing = 1.sp
                        )
                        Text(
                            text = featured.title,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = AuralysTextPrimary,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            text = "${featured.artist} • ${featured.album}",
                            fontSize = 12.sp,
                            color = AuralysTextMuted,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(AuralysAccentCyan),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.PlayArrow,
                            contentDescription = "Play",
                            tint = Color.Black,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            }
        }

        // Section: Rekomendasi Teratas
        Text(
            text = "Dengarkan Lagi & Rekomendasi",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = AuralysTextPrimary,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )

        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(queue) { track ->
                AuralysTrackCardSquare(
                    track = track,
                    isCurrent = currentTrack?.id == track.id,
                    onClick = { onPlayTrack(track) }
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Section: Koleksi Trek Lengkap
        Text(
            text = "Daftar Putar Utama",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = AuralysTextPrimary,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )

        Column(modifier = Modifier.padding(horizontal = 16.dp)) {
            queue.forEach { track ->
                AuralysTrackRowItem(
                    track = track,
                    isCurrent = currentTrack?.id == track.id,
                    onClick = { onPlayTrack(track) }
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

@Composable
private fun AuralysExploreContent(
    onGenreClick: (String) -> Unit,
    onPlayTrack: (AuralysTrack) -> Unit
) {
    val genres = listOf(
        "Chill & Lo-Fi" to Color(0xFF0284C7),
        "Cosmic Synth" to Color(0xFF7C3AED),
        "Cyber Ambient" to Color(0xFF059669),
        "Deep Focus" to Color(0xFFD97706),
        "Midnight Acoustic" to Color(0xFFE11D48),
        "Bass & Glitch" to Color(0xFF4F46E5)
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
            .padding(bottom = 80.dp)
    ) {
        Text(
            text = "Jelajahi Mood & Genre",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = AuralysTextPrimary
        )
        Text(
            text = "Temukan frekuensi suara sesuai atmosfer suasana hati Anda",
            fontSize = 12.sp,
            color = AuralysTextMuted,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Grid 2 Kolom Mood & Genre
        genres.chunked(2).forEach { row ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 10.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                row.forEach { (genreName, cardColor) ->
                    Card(
                        modifier = Modifier
                            .weight(1f)
                            .height(76.dp)
                            .clip(RoundedCornerShape(14.dp))
                            .clickable { onGenreClick(genreName) },
                        colors = CardDefaults.cardColors(containerColor = cardColor.copy(alpha = 0.25f)),
                        border = androidx.compose.foundation.BorderStroke(1.dp, cardColor.copy(alpha = 0.5f))
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(12.dp),
                            contentAlignment = Alignment.BottomStart
                        ) {
                            Text(
                                text = genreName,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Tangga Lagu Global & Trending",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = AuralysTextPrimary,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        AuralysAudioController.createDefaultOnlineCatalog().take(3).forEach { tr ->
            AuralysTrackRowItem(
                track = tr,
                isCurrent = false,
                onClick = { onPlayTrack(tr) }
            )
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
private fun AuralysLibraryContent(
    vaultItems: List<VaultItemEntity>,
    onLoadVaultFolder: (String) -> Unit,
    onPlayTrack: (AuralysTrack) -> Unit
) {
    val audioExts = setOf("mp3", "wav", "m4a", "flac", "ogg", "aac", "opus")
    val vaultAudioItems = vaultItems.filter {
        val ext = it.extension.lowercase().ifBlank { it.name.substringAfterLast('.', "").lowercase() }
        ext in audioExts || it.category == "AUDIO"
    }

    val folders: List<String> = vaultAudioItems.map { (it.groupArchiveName ?: "").ifBlank { "Root" } }.distinct()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
            .padding(bottom = 80.dp)
    ) {
        Text(
            text = "Koleksi & Audio Brankas",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = AuralysTextPrimary
        )
        Text(
            text = "Integrasi audio lokal terenkripsi NYX2 dengan pemutar musik Auralys",
            fontSize = 12.sp,
            color = AuralysTextMuted,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Tombol Folder Scoped Queue
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            colors = CardDefaults.cardColors(containerColor = AuralysCardBg),
            border = androidx.compose.foundation.BorderStroke(1.dp, AuralysAccentCyan.copy(alpha = 0.35f))
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.FolderSpecial,
                        contentDescription = null,
                        tint = AuralysAccentCyan,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Folder-Scoped Queue (Isolasi Folder)",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = AuralysTextPrimary
                    )
                }
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "Pilih folder brankas untuk mengunci antrean pemutaran hanya pada file audio folder tersebut tanpa kebocoran trek.",
                    fontSize = 11.sp,
                    color = AuralysTextMuted
                )
                Spacer(modifier = Modifier.height(10.dp))

                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(folders) { folderName: String ->
                        Surface(
                            shape = RoundedCornerShape(999.dp),
                            color = AuralysCardBorder,
                            modifier = Modifier.clickable { onLoadVaultFolder(folderName) }
                        ) {
                            Text(
                                text = "📁 $folderName",
                                fontSize = 11.sp,
                                color = AuralysAccentCyan,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                            )
                        }
                    }
                }
            }
        }

        Text(
            text = "Daftar File Audio di Brankas (${vaultAudioItems.size} Berkas)",
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = AuralysTextPrimary,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        if (vaultAudioItems.isEmpty()) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp),
                shape = RoundedCornerShape(14.dp),
                color = AuralysCardBg,
                border = androidx.compose.foundation.BorderStroke(1.dp, AuralysCardBorder)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Default.MusicNote,
                        contentDescription = null,
                        tint = AuralysTextMuted,
                        modifier = Modifier.size(32.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Belum ada berkas audio di brankas aktif",
                        fontSize = 12.sp,
                        color = AuralysTextMuted
                    )
                }
            }
        } else {
            vaultAudioItems.forEach { item ->
                val itemFolder = (item.groupArchiveName ?: "").ifBlank { "Brankas Utama" }
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .clickable {
                            val track = AuralysTrack(
                                id = "vault_${item.id}",
                                title = item.name.substringBeforeLast('.').ifBlank { item.name },
                                artist = if (item.isEncrypted) "Terenkripsi NYX2" else "Audio Bersih",
                                album = itemFolder,
                                durationSec = if (item.durationSeconds > 0) item.durationSeconds else 180,
                                sourceMode = QueueSourceMode.VAULT_LOCAL_FOLDER,
                                folderPath = itemFolder
                            )
                            onPlayTrack(track)
                        },
                    colors = CardDefaults.cardColors(containerColor = AuralysCardBg),
                    border = androidx.compose.foundation.BorderStroke(1.dp, AuralysCardBorder)
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color(0xFF0F766E)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Headphones,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = item.name,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = AuralysTextPrimary,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            Text(
                                text = "Folder: $itemFolder • ${if (item.isEncrypted) "NYX2 Encrypted" else "Decrypted"}",
                                fontSize = 11.sp,
                                color = AuralysAccentCyan
                            )
                        }
                        Icon(
                            imageVector = Icons.Default.PlayArrow,
                            contentDescription = "Play",
                            tint = AuralysAccentCyan,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun AuralysSearchContent(
    query: String,
    onQueryChange: (String) -> Unit,
    queue: List<AuralysTrack>,
    onPlayTrack: (AuralysTrack) -> Unit
) {
    val filtered = remember(query, queue) {
        if (query.isBlank()) queue else {
            queue.filter {
                it.title.contains(query, ignoreCase = true) ||
                    it.artist.contains(query, ignoreCase = true) ||
                    it.album.contains(query, ignoreCase = true)
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .padding(bottom = 80.dp)
    ) {
        // Kotak Input Pencarian
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            color = AuralysCardBg,
            border = androidx.compose.foundation.BorderStroke(1.dp, AuralysCardBorder)
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 14.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search",
                    tint = AuralysTextMuted,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))
                BasicTextField(
                    value = query,
                    onValueChange = onQueryChange,
                    singleLine = true,
                    textStyle = MaterialTheme.typography.bodyMedium.copy(color = AuralysTextPrimary),
                    cursorBrush = SolidColor(AuralysAccentCyan),
                    modifier = Modifier.weight(1f),
                    decorationBox = { innerTextField ->
                        if (query.isEmpty()) {
                            Text(
                                text = "Cari lagu, artis, atau album...",
                                color = AuralysTextMuted,
                                fontSize = 14.sp
                            )
                        }
                        innerTextField()
                    }
                )
                if (query.isNotEmpty()) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Clear",
                        tint = AuralysTextMuted,
                        modifier = Modifier
                            .size(18.dp)
                            .clickable { onQueryChange("") }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = if (query.isBlank()) "Saran Populer" else "Hasil Pencarian (${filtered.size})",
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = AuralysTextPrimary,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(filtered) { track ->
                AuralysTrackRowItem(
                    track = track,
                    isCurrent = false,
                    onClick = { onPlayTrack(track) }
                )
            }
        }
    }
}

@Composable
private fun AuralysTrackCardSquare(
    track: AuralysTrack,
    isCurrent: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .width(136.dp)
            .clip(RoundedCornerShape(16.dp))
            .clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = AuralysCardBg),
        border = androidx.compose.foundation.BorderStroke(
            1.dp,
            if (isCurrent) AuralysAccentCyan else AuralysCardBorder
        )
    ) {
        Column(modifier = Modifier.padding(10.dp)) {
            Box(
                modifier = Modifier
                    .size(116.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Brush.linearGradient(listOf(Color(0xFF1E293B), Color(0xFF0F172A)))),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Album,
                    contentDescription = null,
                    tint = if (isCurrent) AuralysAccentCyan else AuralysTextMuted,
                    modifier = Modifier.size(48.dp)
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = track.title,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = AuralysTextPrimary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = track.artist,
                fontSize = 11.sp,
                color = AuralysTextMuted,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
private fun AuralysTrackRowItem(
    track: AuralysTrack,
    isCurrent: Boolean,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        color = if (isCurrent) AuralysAccentCyan.copy(alpha = 0.08f) else AuralysCardBg,
        border = androidx.compose.foundation.BorderStroke(
            1.dp,
            if (isCurrent) AuralysAccentCyan.copy(alpha = 0.5f) else AuralysCardBorder
        )
    ) {
        Row(
            modifier = Modifier.padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color(0xFF1E293B)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = if (isCurrent) Icons.Default.GraphicEq else Icons.Default.MusicNote,
                    contentDescription = null,
                    tint = if (isCurrent) AuralysAccentCyan else AuralysTextMuted,
                    modifier = Modifier.size(22.dp)
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = track.title,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isCurrent) AuralysAccentCyan else AuralysTextPrimary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = "${track.artist} • ${track.album}",
                    fontSize = 11.sp,
                    color = AuralysTextMuted,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            Text(
                text = formatSeconds(track.durationSec),
                fontSize = 11.sp,
                color = AuralysTextMuted
            )
        }
    }
}

@Composable
private fun AuralysMiniPlayerBar(
    track: AuralysTrack,
    isPlaying: Boolean,
    currentPosSec: Int,
    totalDurSec: Int,
    onTogglePlay: () -> Unit,
    onSkipNext: () -> Unit,
    onExpandClick: () -> Unit
) {
    val progress = (currentPosSec.toFloat() / totalDurSec.coerceAtLeast(1).toFloat()).coerceIn(0f, 1f)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 4.dp)
            .clip(RoundedCornerShape(16.dp))
            .clickable { onExpandClick() },
        colors = CardDefaults.cardColors(containerColor = Color(0xFF161F33)),
        border = androidx.compose.foundation.BorderStroke(1.dp, AuralysCardBorder)
    ) {
        Column {
            Row(
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(Brush.linearGradient(listOf(AuralysAccentCyan, AuralysAccentIndigo))),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.GraphicEq,
                        contentDescription = null,
                        tint = Color.Black,
                        modifier = Modifier.size(20.dp)
                    )
                }

                Spacer(modifier = Modifier.width(10.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = track.title,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = AuralysTextPrimary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = track.artist,
                        fontSize = 11.sp,
                        color = AuralysTextMuted,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                IconButton(onClick = onTogglePlay, modifier = Modifier.size(36.dp)) {
                    Icon(
                        imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                        contentDescription = "Play/Pause",
                        tint = AuralysTextPrimary,
                        modifier = Modifier.size(22.dp)
                    )
                }

                IconButton(onClick = onSkipNext, modifier = Modifier.size(36.dp)) {
                    Icon(
                        imageVector = Icons.Default.SkipNext,
                        contentDescription = "Skip Next",
                        tint = AuralysTextMuted,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(2.5.dp),
                color = AuralysAccentCyan,
                trackColor = Color.Transparent
            )
        }
    }
}

@Composable
private fun AuralysBottomNavigationBar(
    activeTab: AuralysNavTab,
    onTabSelect: (AuralysNavTab) -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .windowInsetsPadding(WindowInsets.navigationBars),
        color = Color(0xFF0F1523),
        border = androidx.compose.foundation.BorderStroke(0.5.dp, AuralysCardBorder)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 6.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            AuralysNavTab.entries.forEach { tab ->
                val selected = activeTab == tab
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .clickable { onTabSelect(tab) }
                        .padding(horizontal = 16.dp, vertical = 4.dp)
                ) {
                    Icon(
                        imageVector = tab.icon,
                        contentDescription = tab.label,
                        tint = if (selected) AuralysAccentCyan else AuralysTextMuted,
                        modifier = Modifier.size(22.dp)
                    )
                    Text(
                        text = tab.label,
                        fontSize = 10.sp,
                        fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal,
                        color = if (selected) AuralysAccentCyan else AuralysTextMuted
                    )
                }
            }
        }
    }
}

@Composable
private fun AuralysExpandedNowPlayingSheet(
    track: AuralysTrack,
    isPlaying: Boolean,
    currentPosSec: Int,
    totalDurSec: Int,
    liveAmplitude: Float,
    playbackSpeed: Float,
    repeatMode: AuralysRepeatMode,
    isShuffle: Boolean,
    sleepTimerSec: Int?,
    isFavorite: Boolean,
    onToggleFavorite: () -> Unit,
    onTogglePlay: () -> Unit,
    onSkipNext: () -> Unit,
    onSkipPrev: () -> Unit,
    onSeek: (Int) -> Unit,
    onToggleRepeat: () -> Unit,
    onToggleShuffle: () -> Unit,
    onOpenSpeedModal: () -> Unit,
    onOpenSleepTimer: () -> Unit,
    onOpenLyrics: () -> Unit,
    onOpenQueue: () -> Unit,
    onCollapse: () -> Unit
) {
    val progress = (currentPosSec.toFloat() / totalDurSec.coerceAtLeast(1).toFloat()).coerceIn(0f, 1f)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(Color(0xFF131D33), AuralysDarkBg)))
            .windowInsetsPadding(WindowInsets.statusBars)
            .windowInsetsPadding(WindowInsets.navigationBars)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Header Top Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onCollapse) {
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowDown,
                        contentDescription = "Collapse",
                        tint = Color.White,
                        modifier = Modifier.size(32.dp)
                    )
                }

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = if (track.sourceMode == QueueSourceMode.VAULT_LOCAL_FOLDER) "AUDIO BRANKAS TERISOLASI" else "STREAMING SEKARANG",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = AuralysAccentCyan,
                        letterSpacing = 1.sp
                    )
                    Text(
                        text = track.album,
                        fontSize = 13.sp,
                        color = AuralysTextMuted,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                IconButton(onClick = onOpenQueue) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.QueueMusic,
                        contentDescription = "Queue",
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

            // Big Squircle Album Artwork
            Box(
                modifier = Modifier
                    .size(280.dp)
                    .clip(RoundedCornerShape(32.dp))
                    .background(Brush.linearGradient(listOf(Color(0xFF1E293B), Color(0xFF0F172A)))),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Album,
                    contentDescription = null,
                    tint = AuralysAccentIndigo.copy(alpha = 0.7f),
                    modifier = Modifier.size(140.dp)
                )

                // Visualizer Waveform Overlay
                AuralysWaveformVisualizer(
                    amplitude = liveAmplitude,
                    isPlaying = isPlaying,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp)
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 16.dp)
                )
            }

            // Track Title & Favorite Button
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = track.title,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = track.artist,
                        fontSize = 14.sp,
                        color = AuralysTextMuted,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                IconButton(onClick = onToggleFavorite) {
                    Icon(
                        imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = "Favorite",
                        tint = if (isFavorite) Color(0xFFEF4444) else AuralysTextMuted,
                        modifier = Modifier.size(26.dp)
                    )
                }
            }

            // Scrubber Slider
            Column(modifier = Modifier.fillMaxWidth()) {
                Slider(
                    value = progress,
                    onValueChange = { newProg ->
                        val targetSec = (newProg * totalDurSec).toInt()
                        onSeek(targetSec)
                    },
                    colors = SliderDefaults.colors(
                        thumbColor = AuralysAccentCyan,
                        activeTrackColor = AuralysAccentCyan,
                        inactiveTrackColor = AuralysCardBorder
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = formatSeconds(currentPosSec),
                        fontSize = 11.sp,
                        color = AuralysTextMuted
                    )
                    Text(
                        text = formatSeconds(totalDurSec),
                        fontSize = 11.sp,
                        color = AuralysTextMuted
                    )
                }
            }

            // Main Playback Controls Deck
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onToggleShuffle) {
                    Icon(
                        imageVector = Icons.Default.Shuffle,
                        contentDescription = "Shuffle",
                        tint = if (isShuffle) AuralysAccentCyan else AuralysTextMuted,
                        modifier = Modifier.size(24.dp)
                    )
                }

                IconButton(onClick = onSkipPrev) {
                    Icon(
                        imageVector = Icons.Default.SkipPrevious,
                        contentDescription = "Previous",
                        tint = Color.White,
                        modifier = Modifier.size(36.dp)
                    )
                }

                // Play / Pause Primary Action
                Box(
                    modifier = Modifier
                        .size(68.dp)
                        .clip(CircleShape)
                        .background(Brush.linearGradient(listOf(AuralysAccentCyan, AuralysAccentIndigo)))
                        .clickable { onTogglePlay() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                        contentDescription = "Play/Pause",
                        tint = Color.Black,
                        modifier = Modifier.size(36.dp)
                    )
                }

                IconButton(onClick = onSkipNext) {
                    Icon(
                        imageVector = Icons.Default.SkipNext,
                        contentDescription = "Next",
                        tint = Color.White,
                        modifier = Modifier.size(36.dp)
                    )
                }

                IconButton(onClick = onToggleRepeat) {
                    Icon(
                        imageVector = when (repeatMode) {
                            AuralysRepeatMode.ONE -> Icons.Default.RepeatOne
                            else -> Icons.Default.Repeat
                        },
                        contentDescription = "Repeat",
                        tint = if (repeatMode != AuralysRepeatMode.OFF) AuralysAccentCyan else AuralysTextMuted,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

            // Extra Action Pills (Lyrics, Speed, Timer)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Tombol Lirik Berjalan
                Surface(
                    shape = RoundedCornerShape(999.dp),
                    color = AuralysCardBg,
                    border = androidx.compose.foundation.BorderStroke(1.dp, AuralysCardBorder),
                    modifier = Modifier.clickable { onOpenLyrics() }
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(Icons.Default.Lyrics, contentDescription = null, tint = AuralysAccentCyan, modifier = Modifier.size(16.dp))
                        Text("Lirik", fontSize = 11.sp, color = AuralysTextPrimary)
                    }
                }

                // Tombol Kecepatan
                Surface(
                    shape = RoundedCornerShape(999.dp),
                    color = AuralysCardBg,
                    border = androidx.compose.foundation.BorderStroke(1.dp, AuralysCardBorder),
                    modifier = Modifier.clickable { onOpenSpeedModal() }
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(Icons.Default.Speed, contentDescription = null, tint = AuralysAccentCyan, modifier = Modifier.size(16.dp))
                        Text("${playbackSpeed}x", fontSize = 11.sp, color = AuralysTextPrimary)
                    }
                }

                // Tombol Sleep Timer
                Surface(
                    shape = RoundedCornerShape(999.dp),
                    color = AuralysCardBg,
                    border = androidx.compose.foundation.BorderStroke(1.dp, AuralysCardBorder),
                    modifier = Modifier.clickable { onOpenSleepTimer() }
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(Icons.Default.Timer, contentDescription = null, tint = if (sleepTimerSec != null) AuralysAccentCyan else AuralysTextMuted, modifier = Modifier.size(16.dp))
                        Text(
                            text = if (sleepTimerSec != null) formatSeconds(sleepTimerSec) else "Timer",
                            fontSize = 11.sp,
                            color = if (sleepTimerSec != null) AuralysAccentCyan else AuralysTextPrimary
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun AuralysWaveformVisualizer(
    amplitude: Float,
    isPlaying: Boolean,
    modifier: Modifier = Modifier
) {
    val barCount = 24
    val transition = rememberInfiniteTransition(label = "waveform")
    val phase by transition.animateFloat(
        initialValue = 0f,
        targetValue = 6.28f,
        animationSpec = infiniteRepeatable(tween(1400, easing = FastOutSlowInEasing), RepeatMode.Restart),
        label = "phase"
    )

    Canvas(modifier = modifier) {
        val widthPerBar = size.width / (barCount * 2f)
        val maxH = size.height * 0.85f

        for (i in 0 until barCount) {
            val wave = if (isPlaying) {
                abs(sin(phase + (i * 0.35f))) * amplitude
            } else 0.15f

            val barHeight = (maxH * wave).coerceAtLeast(4.dp.toPx())
            val x = i * (widthPerBar * 2f) + widthPerBar * 0.5f
            val top = (size.height - barHeight) / 2f

            drawRoundRect(
                color = AuralysAccentCyan.copy(alpha = 0.85f),
                topLeft = Offset(x, top),
                size = androidx.compose.ui.geometry.Size(widthPerBar, barHeight),
                cornerRadius = androidx.compose.ui.geometry.CornerRadius(4f, 4f)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AuralysLyricsBottomSheet(
    track: AuralysTrack,
    currentPositionMs: Long,
    onDismiss: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val listState = rememberLazyListState()

    // Cari baris lirik aktif berdasarkan timestamp
    val activeIdx = track.lyrics.indexOfLast { it.timestampMs <= currentPositionMs }.coerceAtLeast(0)

    LaunchedEffect(activeIdx) {
        if (activeIdx >= 0 && activeIdx < track.lyrics.size) {
            listState.animateScrollToItem(activeIdx)
        }
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = Color(0xFF0F1523)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.7f)
                .padding(20.dp)
        ) {
            Text(
                text = "Lirik Berjalan (Synchronized Lyrics)",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = AuralysAccentCyan
            )
            Text(
                text = track.title,
                fontSize = 12.sp,
                color = AuralysTextMuted,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            if (track.lyrics.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Lirik belum tersedia untuk lagu ini",
                        color = AuralysTextMuted,
                        fontSize = 13.sp
                    )
                }
            } else {
                LazyColumn(
                    state = listState,
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    itemsIndexed(track.lyrics) { idx, line ->
                        val isActive = idx == activeIdx
                        Text(
                            text = line.text,
                            fontSize = if (isActive) 18.sp else 14.sp,
                            fontWeight = if (isActive) FontWeight.Bold else FontWeight.Normal,
                            color = if (isActive) AuralysAccentCyan else AuralysTextMuted,
                            lineHeight = 24.sp
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AuralysQueueBottomSheet(
    queue: List<AuralysTrack>,
    currentIndex: Int,
    onSelectTrack: (AuralysTrack) -> Unit,
    onDismiss: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = Color(0xFF0F1523)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.65f)
                .padding(20.dp)
        ) {
            Text(
                text = "Antrean Pemutaran (Up Next)",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = AuralysTextPrimary
            )
            Text(
                text = "${queue.size} lagu dalam antrean",
                fontSize = 12.sp,
                color = AuralysTextMuted,
                modifier = Modifier.padding(bottom = 14.dp)
            )

            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                itemsIndexed(queue) { idx, track ->
                    AuralysTrackRowItem(
                        track = track,
                        isCurrent = idx == currentIndex,
                        onClick = {
                            onSelectTrack(track)
                            onDismiss()
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun AuralysSpeedDialog(
    currentSpeed: Float,
    onSelectSpeed: (Float) -> Unit,
    onDismiss: () -> Unit
) {
    val speeds = listOf(0.75f, 1.0f, 1.25f, 1.5f, 2.0f)

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .clickable { onDismiss() }
            .background(Color.Black.copy(alpha = 0.6f)),
        color = Color.Transparent
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Card(
                modifier = Modifier
                    .width(280.dp)
                    .clip(RoundedCornerShape(20.dp)),
                colors = CardDefaults.cardColors(containerColor = AuralysCardBg),
                border = androidx.compose.foundation.BorderStroke(1.dp, AuralysCardBorder)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        text = "Kecepatan Pemutaran",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = AuralysTextPrimary
                    )
                    Spacer(modifier = Modifier.height(14.dp))
                    speeds.forEach { sp ->
                        val isSelected = currentSpeed == sp
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(10.dp))
                                .clickable { onSelectSpeed(sp) }
                                .padding(vertical = 10.dp, horizontal = 12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "${sp}x",
                                fontSize = 14.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                color = if (isSelected) AuralysAccentCyan else AuralysTextPrimary
                            )
                            if (isSelected) {
                                Text(
                                    text = "Aktif",
                                    fontSize = 11.sp,
                                    color = AuralysAccentCyan
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
private fun AuralysSleepTimerDialog(
    remainingSec: Int?,
    onSetTimer: (Int) -> Unit,
    onDismiss: () -> Unit
) {
    val timerOptions = listOf(15, 30, 45, 60)

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .clickable { onDismiss() }
            .background(Color.Black.copy(alpha = 0.6f)),
        color = Color.Transparent
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Card(
                modifier = Modifier
                    .width(280.dp)
                    .clip(RoundedCornerShape(20.dp)),
                colors = CardDefaults.cardColors(containerColor = AuralysCardBg),
                border = androidx.compose.foundation.BorderStroke(1.dp, AuralysCardBorder)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        text = "Sleep Timer",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = AuralysTextPrimary
                    )
                    Text(
                        text = if (remainingSec != null) "Sisa: ${formatSeconds(remainingSec)}" else "Hentikan musik otomatis",
                        fontSize = 12.sp,
                        color = AuralysAccentCyan,
                        modifier = Modifier.padding(bottom = 14.dp)
                    )

                    timerOptions.forEach { mins ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(10.dp))
                                .clickable { onSetTimer(mins) }
                                .padding(vertical = 10.dp, horizontal = 12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "$mins Menit",
                                fontSize = 14.sp,
                                color = AuralysTextPrimary
                            )
                        }
                    }

                    if (remainingSec != null) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Batalkan Timer",
                            fontSize = 13.sp,
                            color = Color(0xFFEF4444),
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onSetTimer(0) }
                                .padding(vertical = 10.dp, horizontal = 12.dp)
                        )
                    }
                }
            }
        }
    }
}
