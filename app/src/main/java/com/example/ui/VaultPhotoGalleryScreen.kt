package com.example.ui

import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Collections
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.GridOn
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.data.VaultCategory
import com.example.data.VaultItemEntity
import com.example.ui.theme.CosmicBorderHairline
import com.example.ui.theme.CosmicObsidian
import com.example.ui.theme.CosmicSurface
import com.example.ui.theme.CosmicTextMuted
import com.example.ui.theme.CosmicTextPure
import com.example.ui.theme.CosmicVoid
import com.example.ui.theme.DuoFeatherGreen
import com.example.ui.theme.DuoFeatherGreenLip
import com.example.ui.theme.DuoHareLip
import com.example.ui.theme.DuoMacawBlue
import com.example.ui.theme.DuoMacawBlueLip
import com.example.ui.theme.DuoPureWhite
import com.example.ui.theme.DuoSwanBorder
import com.example.ui.theme.PastelLavender
import com.example.ui.theme.PastelMint
import com.example.ui.theme.PastelRose
import com.example.ui.theme.PastelSky
import com.example.ui.theme.duolingoButton
import com.example.ui.theme.duolingoCard
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

private data class PhotoDateSection(
    val headerTitle: String,
    val subtitle: String,
    val photos: List<VaultItemEntity>
)

/**
 * Galeri Foto Gaya Google Photos:
 * - Grid rapat per tanggal/grup (2.dp spacing) dengan opsi kepadatan 3 atau 4 kolom.
 * - Smart Search & Chip Filter ringkas.
 * - Ketuk foto untuk membuka Google Photos Fullscreen Horizontal Swipe Viewer (Geser Kanan-Kiri, Zoom Cubit, Thumbnail Strip, & Bar Aksi Cepat).
 */
@Composable
fun VaultPhotoGalleryScreen(
    activeVaultId: Int,
    allItems: List<VaultItemEntity>,
    onOpenMediaItem: (VaultItemEntity) -> Unit,
    onRequestPinToggleEncrypt: (VaultItemEntity) -> Unit,
    onRequestExportOrShareWithBiometric: (VaultItemEntity, Boolean) -> Unit,
    onDeleteItem: (VaultItemEntity) -> Unit
) {
    val imageExtensions = remember { setOf("jpg", "jpeg", "png", "webp", "heic", "gif", "bmp", "svg") }
    val vaultPhotos = remember(allItems, activeVaultId) {
        allItems.filter { item ->
            item.vaultProfileId == activeVaultId &&
                (item.category == VaultCategory.IMAGE.name || item.extension.lowercase() in imageExtensions)
        }
    }

    var selectedAlbum by remember { mutableStateOf<String?>(null) }
    var photoSearchQuery by remember { mutableStateOf("") }
    var activeSmartFilter by remember { mutableStateOf("ALL") }
    var gridColumnsCount by remember { mutableIntStateOf(3) } // 3 or 4 columns like Google Photos pinch density

    val albums = remember(vaultPhotos) {
        vaultPhotos.mapNotNull { it.groupArchiveName?.ifBlank { null } }.distinct().sorted()
    }

    val displayedPhotos = remember(vaultPhotos, selectedAlbum, photoSearchQuery, activeSmartFilter) {
        val q = photoSearchQuery.trim()
        vaultPhotos.filter { item ->
            val matchAlbum = selectedAlbum == null || item.groupArchiveName.equals(selectedAlbum, ignoreCase = true)
            val sizeLabel = formatFileSize(item.sizeBytes)
            val matchQuery = q.isBlank() ||
                item.name.contains(q, ignoreCase = true) ||
                item.extension.contains(q, ignoreCase = true) ||
                "${item.name}.${item.extension}".contains(q, ignoreCase = true) ||
                sizeLabel.contains(q, ignoreCase = true) ||
                (item.groupArchiveName?.contains(q, ignoreCase = true) == true) ||
                (q.equals("terkunci", ignoreCase = true) && item.isEncrypted) ||
                (q.equals("terbuka", ignoreCase = true) && !item.isEncrypted)
            val matchChip = when (activeSmartFilter) {
                "OPEN" -> !item.isEncrypted
                "LOCKED" -> item.isEncrypted
                "LARGE" -> item.sizeBytes >= 2L * 1024L * 1024L
                "PNG" -> item.extension.equals("png", ignoreCase = true)
                "JPG" -> item.extension.equals("jpg", ignoreCase = true) || item.extension.equals("jpeg", ignoreCase = true)
                "GIF_WEBP" -> item.extension.equals("gif", ignoreCase = true) || item.extension.equals("webp", ignoreCase = true)
                else -> true
            }
            matchAlbum && matchQuery && matchChip
        }
    }

    // Group photos by date/timeline section like Google Photos
    val dateSections = remember(displayedPhotos) {
        val formatter = SimpleDateFormat("EEEE, d MMM yyyy", Locale("id", "ID"))
        displayedPhotos
            .groupBy { photo ->
                val f = File(photo.internalPlainPath)
                val lastMod = when {
                    f.exists() && f.lastModified() > 0L -> f.lastModified()
                    photo.addedAt > 0L -> photo.addedAt
                    else -> 0L
                }
                val dateLabel = if (lastMod > 0L) {
                    val diffDays = ((System.currentTimeMillis() - lastMod) / (1000L * 60L * 60L * 24L)).toInt()
                    when {
                        diffDays <= 0 -> "Hari Ini"
                        diffDays == 1 -> "Kemarin"
                        else -> formatter.format(Date(lastMod))
                    }
                } else {
                    "Koleksi Brankas"
                }
                dateLabel
            }
            .map { (dateTitle, itemsInDate) ->
                val albumHint = itemsInDate.mapNotNull { it.groupArchiveName?.takeIf { a -> a.isNotBlank() } }.distinct().joinToString(" • ")
                PhotoDateSection(
                    headerTitle = dateTitle,
                    subtitle = if (albumHint.isNotBlank()) "${itemsInDate.size} foto • $albumHint" else "${itemsInDate.size} foto",
                    photos = itemsInDate
                )
            }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .testTag("vault_photo_gallery_screen")
    ) {
        // --- 1. GOOGLE PHOTOS MINIMALIST TOP BAR & SEARCH ---
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 8.dp)
                .duolingoCard(
                    backgroundColor = CosmicObsidian,
                    borderColor = DuoSwanBorder,
                    lipColor = DuoHareLip,
                    cornerRadius = 20.dp,
                    borderWidth = 2.dp,
                    lipHeight = 3.dp
                )
                .padding(bottom = 3.dp)
                .padding(horizontal = 14.dp, vertical = 10.dp)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(34.dp)
                                .duolingoButton(
                                    faceColor = DuoMacawBlue,
                                    lipColor = DuoMacawBlueLip,
                                    cornerRadius = 10.dp,
                                    lipHeight = 2.dp
                                )
                                .padding(bottom = 2.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Collections,
                                contentDescription = null,
                                tint = DuoPureWhite,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                        val displayVaultNum = com.example.data.SecurityConfig.displayVaultNumber(activeVaultId)
                        Column {
                            Text(
                                text = "Google Photos Brankas #$displayVaultNum",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.ExtraBold,
                                color = CosmicTextPure
                            )
                            Text(
                                text = "${displayedPhotos.size} foto • Ketuk foto untuk geser slide kanan-kiri",
                                style = MaterialTheme.typography.labelSmall,
                                color = CosmicTextMuted
                            )
                        }
                    }

                    // Toggle 3x3 vs 4x4 Google Photos Grid Density
                    Box(
                        modifier = Modifier
                            .height(32.dp)
                            .duolingoButton(
                                faceColor = DuoFeatherGreen,
                                lipColor = DuoFeatherGreenLip,
                                cornerRadius = 10.dp,
                                lipHeight = 2.dp
                            )
                            .clickable {
                                gridColumnsCount = if (gridColumnsCount == 3) 4 else 3
                            }
                            .padding(bottom = 2.dp, start = 8.dp, end = 8.dp)
                            .testTag("gallery_toggle_grid_density_button"),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                imageVector = if (gridColumnsCount == 3) Icons.Default.GridOn else Icons.Default.GridView,
                                contentDescription = "Ubah Kerapatan Grid",
                                tint = DuoPureWhite,
                                modifier = Modifier.size(14.dp)
                            )
                            Text(
                                text = "${gridColumnsCount}x",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.ExtraBold,
                                color = DuoPureWhite
                            )
                        }
                    }
                }

                // Capsule Search Bar ala Google Photos
                OutlinedTextField(
                    value = photoSearchQuery,
                    onValueChange = { photoSearchQuery = it },
                    placeholder = {
                        Text(
                            "Cari di Foto (.png, .jpg, ukuran, album)...",
                            color = CosmicTextMuted,
                            style = MaterialTheme.typography.bodySmall,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    },
                    leadingIcon = {
                        Icon(Icons.Default.Search, contentDescription = "Cari", tint = PastelSky, modifier = Modifier.size(18.dp))
                    },
                    trailingIcon = {
                        if (photoSearchQuery.isNotBlank() || activeSmartFilter != "ALL") {
                            IconButton(
                                onClick = {
                                    photoSearchQuery = ""
                                    activeSmartFilter = "ALL"
                                },
                                modifier = Modifier.size(24.dp)
                            ) {
                                Icon(Icons.Default.Close, contentDescription = "Hapus", tint = CosmicTextMuted, modifier = Modifier.size(16.dp))
                            }
                        }
                    },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(46.dp)
                        .testTag("gallery_smart_search_input"),
                    shape = RoundedCornerShape(24.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = CosmicSurface,
                        unfocusedContainerColor = CosmicSurface,
                        focusedBorderColor = PastelSky,
                        unfocusedBorderColor = CosmicBorderHairline,
                        focusedTextColor = CosmicTextPure,
                        unfocusedTextColor = CosmicTextPure
                    )
                )

                // Compact Filter Chips + Album Pills in 1 Scrollable Row
                val filterChips = remember {
                    listOf(
                        "ALL" to "Semua",
                        "OPEN" to "Terbuka",
                        "LOCKED" to "Terkunci NYX",
                        "PNG" to ".PNG",
                        "JPG" to ".JPG",
                        "GIF_WEBP" to ".GIF/.WEBP",
                        "LARGE" to ">2MB"
                    )
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    filterChips.forEach { (key, label) ->
                        val selected = activeSmartFilter == key && selectedAlbum == null
                        Surface(
                            color = if (selected) PastelSky else CosmicSurface,
                            shape = RoundedCornerShape(12.dp),
                            border = androidx.compose.foundation.BorderStroke(
                                1.dp,
                                if (selected) PastelSky else CosmicBorderHairline
                            ),
                            modifier = Modifier
                                .clickable {
                                    selectedAlbum = null
                                    activeSmartFilter = if (activeSmartFilter == key && key != "ALL") "ALL" else key
                                }
                                .testTag("gallery_smart_chip_$key")
                        ) {
                            Text(
                                text = label,
                                style = MaterialTheme.typography.labelSmall,
                                color = if (selected) DuoPureWhite else CosmicTextPure,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                            )
                        }
                    }

                    albums.forEach { albumName ->
                        val isSelected = selectedAlbum == albumName
                        val count = vaultPhotos.count { it.groupArchiveName.equals(albumName, ignoreCase = true) }
                        Surface(
                            color = if (isSelected) PastelLavender else CosmicSurface,
                            shape = RoundedCornerShape(12.dp),
                            border = androidx.compose.foundation.BorderStroke(
                                1.dp,
                                if (isSelected) PastelLavender else CosmicBorderHairline
                            ),
                            modifier = Modifier.clickable {
                                selectedAlbum = if (isSelected) null else albumName
                            }
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Folder,
                                    contentDescription = null,
                                    tint = if (isSelected) DuoPureWhite else PastelLavender,
                                    modifier = Modifier.size(12.dp)
                                )
                                Text(
                                    text = "$albumName ($count)",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = if (isSelected) DuoPureWhite else CosmicTextPure,
                                    fontWeight = FontWeight.Bold,
                                    maxLines = 1
                                )
                            }
                        }
                    }
                }
            }
        }

        // --- 2. GOOGLE PHOTOS DATE-GROUPED TIGHT PHOTO GRID ---
        if (displayedPhotos.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    CosmicDragonAvatar(mood = MascotMood.IDLE_BREATHE, size = 84.dp)
                    Spacer(modifier = Modifier.height(10.dp))
                    val displayVaultNum = com.example.data.SecurityConfig.displayVaultNumber(activeVaultId)
                    Text(
                        text = if (photoSearchQuery.isNotBlank() || activeSmartFilter != "ALL") {
                            "Tidak ada foto yang cocok dengan filter"
                        } else {
                            "Belum Ada Foto di Brankas #$displayVaultNum"
                        },
                        style = MaterialTheme.typography.titleMedium,
                        color = CosmicTextPure,
                        fontWeight = FontWeight.ExtraBold
                    )
                    Text(
                        text = "Impor foto dari galeri perangkat atau unduh melalui Mori.",
                        style = MaterialTheme.typography.bodySmall,
                        color = CosmicTextMuted,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(gridColumnsCount),
                contentPadding = PaddingValues(start = 4.dp, end = 4.dp, top = 2.dp, bottom = 110.dp),
                horizontalArrangement = Arrangement.spacedBy(3.dp),
                verticalArrangement = Arrangement.spacedBy(3.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                dateSections.forEach { section ->
                    item(
                        key = "header_${section.headerTitle}",
                        span = { GridItemSpan(maxLineSpan) }
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 10.dp, vertical = 8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = section.headerTitle,
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.ExtraBold,
                                color = CosmicTextPure
                            )
                            Text(
                                text = section.subtitle,
                                style = MaterialTheme.typography.labelSmall,
                                color = CosmicTextMuted,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }

                    items(section.photos, key = { it.id }) { photo ->
                        GooglePhotosTileCard(
                            photo = photo,
                            onClick = {
                                if (photo.isEncrypted) {
                                    onRequestPinToggleEncrypt(photo)
                                } else {
                                    onOpenMediaItem(photo)
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun GooglePhotosTileCard(
    photo: VaultItemEntity,
    onClick: () -> Unit
) {
    val plainFile = remember(photo.internalPlainPath) { File(photo.internalPlainPath) }
    val bitmap = remember(photo.id, photo.isEncrypted) {
        if (!photo.isEncrypted && plainFile.exists()) {
            try {
                BitmapFactory.decodeFile(plainFile.absolutePath)
            } catch (_: Exception) {
                null
            }
        } else null
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .clip(RoundedCornerShape(4.dp))
            .background(CosmicObsidian)
            .border(0.5.dp, CosmicBorderHairline, RoundedCornerShape(4.dp))
            .clickable { onClick() }
            .testTag("photo_card_${photo.id}")
    ) {
        if (bitmap != null && !bitmap.isRecycled) {
            val rememberedImageBitmap = remember(bitmap) { bitmap.asImageBitmap() }
            Image(
                bitmap = rememberedImageBitmap,
                contentDescription = photo.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        } else if (!photo.isEncrypted && !photo.mediaUriString.isNullOrBlank()) {
            AsyncImage(
                model = photo.mediaUriString,
                contentDescription = photo.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        } else {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(CosmicSurface),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = if (photo.isEncrypted) Icons.Default.Lock else Icons.Default.Image,
                    contentDescription = null,
                    tint = if (photo.isEncrypted) PastelRose else PastelSky.copy(alpha = 0.65f),
                    modifier = Modifier.size(28.dp)
                )
            }
        }

        // Subtle Google Photos Top-Right Format / Lock Badge
        if (photo.isEncrypted) {
            Surface(
                color = Color.Black.copy(alpha = 0.68f),
                shape = CircleShape,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(5.dp)
                    .size(22.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = "Terenkripsi",
                        tint = PastelRose,
                        modifier = Modifier.size(12.dp)
                    )
                }
            }
        } else if (photo.extension.lowercase() in setOf("gif", "webp", "png")) {
            Surface(
                color = Color.Black.copy(alpha = 0.52f),
                shape = RoundedCornerShape(4.dp),
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(4.dp)
            ) {
                Text(
                    text = photo.extension.uppercase(),
                    fontSize = 8.sp,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                )
            }
        }

        // Subtle Bottom-Left Size Overlay
        Surface(
            color = Color.Black.copy(alpha = 0.50f),
            shape = RoundedCornerShape(topEnd = 6.dp),
            modifier = Modifier.align(Alignment.BottomStart)
        ) {
            Text(
                text = formatFileSize(photo.sizeBytes),
                fontSize = 8.sp,
                color = Color.White,
                fontWeight = FontWeight.SemiBold,
                maxLines = 1,
                modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp)
            )
        }
    }
}
