package com.example.ui

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AudioFile
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.CloudDone
import androidx.compose.material.icons.filled.CloudDownload
import androidx.compose.material.icons.filled.CloudUpload
import androidx.compose.material.icons.filled.CreateNewFolder
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.DriveFileMove
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.FolderOpen
import androidx.compose.material.icons.filled.FolderZip
import androidx.compose.material.icons.filled.HourglassBottom
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.LockOpen
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.UploadFile
import androidx.compose.material.icons.filled.VideoFile
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.EncryptionScriptEngine
import com.example.data.SecurityConfig
import com.example.data.VaultCategory
import com.example.data.VaultItemEntity
import com.example.ui.theme.CosmicTextPure
import com.example.ui.theme.CosmicTextSubtle
import com.example.ui.theme.PastelMint
import com.example.ui.theme.VaultTextMuted
import com.example.ui.theme.duolingoButton
import com.example.ui.theme.duolingoCard

@Composable
fun VaultDashboardAndFilesContent(
    activeVaultId: Int,
    isFilesOnlyMode: Boolean,
    allItems: List<VaultItemEntity>,
    filteredItems: List<VaultItemEntity>,
    selectedCategory: VaultCategory?,
    searchQuery: String,
    securityConfig: SecurityConfig,
    onSelectCategory: (VaultCategory?) -> Unit,
    onSearchQueryChange: (String) -> Unit,
    onToggleEncryptItemWithPin: (VaultItemEntity) -> Unit,
    onBatchEncryptAllWithPin: (Boolean) -> Unit,
    onInspectItem: (VaultItemEntity) -> Unit,
    onOpenOrPlayItem: (VaultItemEntity) -> Unit,
    onDeleteItem: (VaultItemEntity) -> Unit,
    onImportFromDeviceClick: () -> Unit,
    onCreateSecretFileClick: () -> Unit,
    onOpenScriptEngineClick: () -> Unit,
    onSwitchVaultModeClick: (Int) -> Unit,
    onLockVaultClick: () -> Unit,
    onOpenZipScannerToolClick: () -> Unit = {},
    onOpenMoriDownloaderClick: () -> Unit = {},
    onRenameFolder: (String, String) -> Unit = { _, _ -> },
    onDeleteFolder: (String) -> Unit = {},
    onMoveItemToFolder: (VaultItemEntity, String) -> Unit = { _, _ -> },
    onActivateCalculatorCamouflage: () -> Unit = {},
    isMascotOverlayVisible: Boolean = true,
    onSummonMascotOverlayClick: () -> Unit = {},
    onOpenFeedbackReportClick: () -> Unit = {},
    cloudBackupEntries: List<com.example.data.GoogleCloudVaultBackupEngine.CloudEncryptedFileEntry> = emptyList(),
    onBackupSingleItemToCloud: (VaultItemEntity) -> Unit = {},
    onOpenCloudBackupTabClick: () -> Unit = {},
    onToggleAntiScreenshot: (Boolean) -> Unit = {}
) {
    val totalCount = allItems.size
    val encryptedCount = remember(allItems) { allItems.count { it.isEncrypted } }
    val decryptedCount = totalCount - encryptedCount
    val totalBytes = remember(allItems) { allItems.sumOf { it.sizeBytes } }
    val formattedTotalSize = remember(totalBytes) { formatFileSize(totalBytes) }

    val isDecoyVault = SecurityConfig.isDecoyVault(activeVaultId)
    val displayVaultNum = SecurityConfig.displayVaultNumber(activeVaultId)
    val customVaultName = remember(securityConfig, displayVaultNum) {
        securityConfig.getVaultName(displayVaultNum)
    }
    val (p1, p2, p3) = securityConfig.getPinsForVault(displayVaultNum)
    val keyFingerprint = remember(p1, p2, p3) {
        EncryptionScriptEngine.compute3LayerKeyFingerprint(p1, p2, p3)
    }
    val isGoogleLoggedIn = securityConfig.isGoogleLinked && securityConfig.googleAccountEmail.isNotBlank()

    var showOnlyCloudFiles by remember { mutableStateOf(false) }
    var smartQuickFilter by remember { mutableStateOf("ALL") } // ALL, LOCKED, UNLOCKED, LARGE, MOV_VIDEO
    val cloudEntryByItemId = remember(cloudBackupEntries, displayVaultNum, allItems) {
        val map = mutableMapOf<Int, com.example.data.GoogleCloudVaultBackupEngine.CloudEncryptedFileEntry>()
        for (item in allItems) {
            val match = cloudBackupEntries.firstOrNull { entry ->
                entry.sourceVaultId == displayVaultNum &&
                    (entry.originalItemId == item.id ||
                        (entry.fileName.equals(item.name, ignoreCase = true) &&
                            entry.extension.equals(item.extension, ignoreCase = true)))
            }
            if (match != null) {
                map[item.id] = match
            }
        }
        map.toMap()
    }

    // Folder hierarchy & 1x-Click Full Name / 2x-Click Open state
    var currentOpenFolder by remember { mutableStateOf<String?>(null) }
    var expandedFullNameKey by remember { mutableStateOf<String?>(null) }
    val userCreatedFolders = remember { mutableStateListOf<String>() }
    var showCreateFolderDialog by remember { mutableStateOf(false) }
    var newFolderNameInput by remember { mutableStateOf("") }
    var folderBeingRenamed by remember { mutableStateOf<String?>(null) }
    var renameFolderInput by remember { mutableStateOf("") }
    var folderBeingDeleted by remember { mutableStateOf<String?>(null) }
    var itemBeingMoved by remember { mutableStateOf<VaultItemEntity?>(null) }
    var moveTargetFolderInput by remember { mutableStateOf("") }

    // Helper to normalize folder paths (supports nested ZIP folders like "Pack/Subfolder")
    fun normalizeFolderPath(raw: String?): String? {
        return raw
            ?.replace(" > ", "/")
            ?.replace('\\', '/')
            ?.removeSuffix(".zip")
            ?.removeSuffix(".ZIP")
            ?.trim('/')
            ?.trim()
            ?.takeIf { it.isNotEmpty() }
    }

    // Back button handler when inside a folder/subfolder
    BackHandler(enabled = currentOpenFolder != null) {
        val active = currentOpenFolder
        val parent = active?.substringBeforeLast('/', "")?.takeIf { it.isNotEmpty() }
        expandedFullNameKey = null
        currentOpenFolder = parent
    }

    // Collect all full folder paths (from imported ZIPs, moved items, or user-created folders)
    val allFolderNames = remember(allItems, userCreatedFolders.toList()) {
        val fromItems = allItems.mapNotNull { normalizeFolderPath(it.groupArchiveName) }
        val fromUser = userCreatedFolders.mapNotNull { normalizeFolderPath(it) }
        (fromItems + fromUser).distinct().sorted()
    }

    // Compute visible folders at the current hierarchy level (top-level folders at root, or direct subfolders inside an open folder)
    val visibleChildFolders = remember(allFolderNames, currentOpenFolder) {
        val active = normalizeFolderPath(currentOpenFolder)
        if (active == null) {
            allFolderNames
                .map { it.substringBefore('/') }
                .distinct()
                .sorted()
        } else {
            val prefix = "$active/"
            allFolderNames
                .filter { it.startsWith(prefix, ignoreCase = true) && it.length > prefix.length }
                .map { full ->
                    val remainder = full.substring(prefix.length).trim('/')
                    val nextSegment = remainder.substringBefore('/')
                    "$active/$nextSegment"
                }
                .distinct()
                .sorted()
        }
    }

    // Root only shows standalone files without a folder (unless user is actively typing a search query or filtering Cloud/Quick filter files)
    val displayedFiles = remember(filteredItems, currentOpenFolder, searchQuery, showOnlyCloudFiles, smartQuickFilter, cloudEntryByItemId) {
        val activeFolder = normalizeFolderPath(currentOpenFolder)
        val baseList = if (activeFolder == null) {
            if (searchQuery.isNotBlank() || showOnlyCloudFiles || smartQuickFilter != "ALL") {
                filteredItems
            } else {
                filteredItems.filter { normalizeFolderPath(it.groupArchiveName) == null }
            }
        } else {
            filteredItems.filter {
                normalizeFolderPath(it.groupArchiveName).equals(activeFolder, ignoreCase = true)
            }
        }
        val afterCloud = if (showOnlyCloudFiles) {
            baseList.filter { cloudEntryByItemId.containsKey(it.id) }
        } else {
            baseList
        }
        when (smartQuickFilter) {
            "LOCKED" -> afterCloud.filter { it.isEncrypted }
            "UNLOCKED" -> afterCloud.filter { !it.isEncrypted }
            "LARGE" -> afterCloud.filter { it.sizeBytes >= 5L * 1024L * 1024L }
            "MOV_VIDEO" -> afterCloud.filter {
                it.extension.equals("mov", ignoreCase = true) ||
                    it.extension.equals("mp4", ignoreCase = true) ||
                    it.category == VaultCategory.VIDEO.name
            }
            else -> afterCloud
        }
    }

    // Create Folder / Subfolder Dialog
    if (showCreateFolderDialog) {
        AlertDialog(
            onDismissRequest = { showCreateFolderDialog = false },
            containerColor = com.example.ui.theme.DuoPureWhite,
            title = {
                Text(
                    text = if (currentOpenFolder == null) "Buat Folder Brankas Baru" else "Buat Subfolder di '$currentOpenFolder'",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.ExtraBold,
                    color = CosmicTextPure
                )
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = if (currentOpenFolder == null) {
                            "Anda dapat memindahkan foto, video, atau hasil ekstrak ZIP ke dalam folder ini."
                        } else {
                            "Subfolder ini akan dibuat di dalam folder '$currentOpenFolder'."
                        },
                        style = MaterialTheme.typography.bodySmall,
                        color = CosmicTextSubtle
                    )
                    OutlinedTextField(
                        value = newFolderNameInput,
                        onValueChange = { newFolderNameInput = it },
                        label = { Text(if (currentOpenFolder == null) "Nama Folder Baru" else "Nama Subfolder Baru") },
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("create_folder_name_input")
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val cleaned = newFolderNameInput.removeSuffix(".zip").removeSuffix(".ZIP").trim('/').trim()
                        if (cleaned.isNotEmpty()) {
                            val active = normalizeFolderPath(currentOpenFolder)
                            val fullCreated = if (active != null) "$active/$cleaned" else cleaned
                            if (!userCreatedFolders.contains(fullCreated)) {
                                userCreatedFolders.add(fullCreated)
                            }
                        }
                        newFolderNameInput = ""
                        showCreateFolderDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = com.example.ui.theme.DuoFeatherGreen,
                        contentColor = Color.White
                    )
                ) {
                    Text("Buat Folder", fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                OutlinedButton(onClick = { showCreateFolderDialog = false }) {
                    Text("Batal")
                }
            }
        )
    }

    // Rename Folder Dialog
    folderBeingRenamed?.let { targetOldFolder ->
        AlertDialog(
            onDismissRequest = { folderBeingRenamed = null },
            containerColor = com.example.ui.theme.DuoPureWhite,
            title = {
                Text(
                    text = "Ganti Nama Folder / Playlist ZIP",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.ExtraBold,
                    color = CosmicTextPure
                )
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = "Folder '$targetOldFolder' dan playlist di Video Player akan diperbarui otomatis:",
                        style = MaterialTheme.typography.bodySmall,
                        color = CosmicTextSubtle
                    )
                    OutlinedTextField(
                        value = renameFolderInput,
                        onValueChange = { renameFolderInput = it },
                        label = { Text("Nama Folder Baru") },
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("dashboard_rename_folder_input")
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val cleaned = renameFolderInput.removeSuffix(".zip").trim()
                        if (cleaned.isNotEmpty()) {
                            onRenameFolder(targetOldFolder, cleaned)
                            val idx = userCreatedFolders.indexOf(targetOldFolder)
                            if (idx >= 0) {
                                userCreatedFolders[idx] = cleaned
                            }
                            if (currentOpenFolder == targetOldFolder) {
                                currentOpenFolder = cleaned
                            }
                        }
                        folderBeingRenamed = null
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = com.example.ui.theme.DuoMacawBlue,
                        contentColor = Color.White
                    ),
                    modifier = Modifier.testTag("dashboard_confirm_rename_folder_button")
                ) {
                    Text("Simpan Nama", fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                OutlinedButton(onClick = { folderBeingRenamed = null }) {
                    Text("Batal")
                }
            }
        )
    }

    // Delete Folder & Contents Confirmation Dialog
    folderBeingDeleted?.let { targetDeleteFolder ->
        val filesInsideCount = remember(allItems, targetDeleteFolder) {
            allItems.count {
                val itemFolder = normalizeFolderPath(it.groupArchiveName)
                itemFolder != null && (
                    itemFolder.equals(targetDeleteFolder, ignoreCase = true) ||
                        itemFolder.startsWith("$targetDeleteFolder/", ignoreCase = true)
                    )
            }
        }
        AlertDialog(
            onDismissRequest = { folderBeingDeleted = null },
            containerColor = com.example.ui.theme.DuoPureWhite,
            title = {
                Text(
                    text = "Hapus Folder '$targetDeleteFolder'?",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.ExtraBold,
                    color = com.example.ui.theme.DuoCardinalRedLip
                )
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = if (filesInsideCount > 0) {
                            "Folder '$targetDeleteFolder' beserta $filesInsideCount file/sub-file di dalamnya akan dihapus & di-shred secara permanen dari Brankas."
                        } else {
                            "Folder kosong '$targetDeleteFolder' akan dihapus dari daftar folder Brankas."
                        },
                        style = MaterialTheme.typography.bodySmall,
                        color = CosmicTextPure,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        onDeleteFolder(targetDeleteFolder)
                        userCreatedFolders.removeAll { created ->
                            val norm = normalizeFolderPath(created)
                            norm != null && (
                                norm.equals(targetDeleteFolder, ignoreCase = true) ||
                                    norm.startsWith("$targetDeleteFolder/", ignoreCase = true)
                                )
                        }
                        val active = normalizeFolderPath(currentOpenFolder)
                        if (active != null && (
                                active.equals(targetDeleteFolder, ignoreCase = true) ||
                                    active.startsWith("$targetDeleteFolder/", ignoreCase = true)
                            )
                        ) {
                            currentOpenFolder = targetDeleteFolder.substringBeforeLast('/', "").takeIf { it.isNotEmpty() }
                        }
                        expandedFullNameKey = null
                        folderBeingDeleted = null
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = com.example.ui.theme.DuoCardinalRed,
                        contentColor = Color.White
                    ),
                    modifier = Modifier.testTag("dashboard_confirm_delete_folder_button")
                ) {
                    Text("Ya, Hapus Folder", fontWeight = FontWeight.ExtraBold)
                }
            },
            dismissButton = {
                OutlinedButton(onClick = { folderBeingDeleted = null }) {
                    Text("Batal")
                }
            }
        )
    }

    // Move File to Another Folder Dialog
    itemBeingMoved?.let { targetItem ->
        AlertDialog(
            onDismissRequest = { itemBeingMoved = null },
            containerColor = com.example.ui.theme.DuoPureWhite,
            title = {
                Text(
                    text = "Pindahkan File Antar Folder",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.ExtraBold,
                    color = CosmicTextPure
                )
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(
                        text = "Pilih folder tujuan untuk '${targetItem.name}.${targetItem.extension}' atau ketik nama folder baru:",
                        style = MaterialTheme.typography.bodySmall,
                        color = CosmicTextSubtle
                    )
                    if (allFolderNames.isNotEmpty()) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .horizontalScroll(rememberScrollState()),
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            FilterChip(
                                selected = moveTargetFolderInput.isBlank(),
                                onClick = { moveTargetFolderInput = "" },
                                label = { Text("Tanpa Folder (Root)", fontWeight = FontWeight.Bold) }
                            )
                            allFolderNames.forEach { folder ->
                                FilterChip(
                                    selected = moveTargetFolderInput == folder,
                                    onClick = { moveTargetFolderInput = folder },
                                    label = { Text(folder, maxLines = 1, overflow = TextOverflow.Ellipsis) }
                                )
                            }
                        }
                    }
                    OutlinedTextField(
                        value = moveTargetFolderInput,
                        onValueChange = { moveTargetFolderInput = it },
                        label = { Text("Nama Folder Tujuan (Kosongkan untuk Root)") },
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("move_file_folder_input")
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val cleaned = moveTargetFolderInput.removeSuffix(".zip").trim()
                        if (cleaned.isNotEmpty() && !userCreatedFolders.contains(cleaned)) {
                            userCreatedFolders.add(cleaned)
                        }
                        onMoveItemToFolder(targetItem, cleaned)
                        itemBeingMoved = null
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = com.example.ui.theme.DuoFeatherGreen,
                        contentColor = Color.White
                    ),
                    modifier = Modifier.testTag("confirm_move_file_folder_button")
                ) {
                    Text("Pindahkan Sekarang", fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                OutlinedButton(onClick = { itemBeingMoved = null }) {
                    Text("Batal")
                }
            }
        )
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .testTag(if (isFilesOnlyMode) "files_screen_list" else "dashboard_screen_list"),
        contentPadding = PaddingValues(start = 18.dp, end = 18.dp, top = 12.dp, bottom = 112.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // 1. Minimalist Top Header (Mascot + Vault Badge + Calculator Camouflage + Report/Settings/Lock)
        item {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
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
                            mood = MascotMood.HAPPY_VIBES,
                            size = 42.dp
                        )
                        Column(modifier = Modifier.weight(1f)) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Text(
                                    text = "BRANKAS #$displayVaultNum • $customVaultName",
                                    style = MaterialTheme.typography.titleMedium,
                                    color = CosmicTextPure,
                                    fontWeight = FontWeight.ExtraBold,
                                    maxLines = 1,
                                    softWrap = false,
                                    overflow = TextOverflow.Ellipsis,
                                    modifier = Modifier
                                        .weight(1f, fill = false)
                                        .testTag("vault_header_title_text")
                                )
                                Surface(
                                    color = PastelMint.copy(alpha = 0.2f),
                                    shape = RoundedCornerShape(50),
                                    border = androidx.compose.foundation.BorderStroke(1.dp, PastelMint.copy(alpha = 0.6f))
                                ) {
                                    Text(
                                        text = if (isDecoyVault) "TERENKRIPSI" else "• TERENKRIPSI",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = PastelMint,
                                        fontWeight = FontWeight.Bold,
                                        maxLines = 1,
                                        softWrap = false,
                                        modifier = Modifier
                                            .padding(horizontal = 8.dp, vertical = 3.dp)
                                            .testTag("vault_header_encryption_badge")
                                    )
                                }
                            }
                            Text(
                                text = if (isDecoyVault) {
                                    "NYX2 - $keyFingerprint - AES-256-GCM • Cloud: ${cloudEntryByItemId.size} file"
                                } else {
                                    "NYX2 • $keyFingerprint • AES-256-GCM • Di Cloud: ${cloudEntryByItemId.size}"
                                },
                                style = MaterialTheme.typography.labelMedium,
                                color = CosmicTextSubtle,
                                maxLines = 1,
                                softWrap = false,
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier.testTag("vault_header_cipher_subtitle")
                            )
                        }
                    }

                    // 1-Click Instant Calculator Camouflage Button (#9)
                    Box(
                        modifier = Modifier
                            .duolingoButton(
                                faceColor = com.example.ui.theme.DuoFoxOrange,
                                lipColor = com.example.ui.theme.DuoFoxOrangeLip,
                                cornerRadius = 14.dp,
                                lipHeight = 3.dp
                            )
                            .instantTouchPress { onActivateCalculatorCamouflage() }
                            .padding(bottom = 3.dp)
                            .testTag("instant_calculator_camouflage_top_button")
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 11.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Calculate,
                                contentDescription = "Mode Kalkulator Kamuflase Instan 1-Klik",
                                tint = Color.White,
                                modifier = Modifier.size(16.dp)
                            )
                            Text(
                                text = "Samarkan",
                                style = MaterialTheme.typography.labelMedium,
                                color = Color.White,
                                fontWeight = FontWeight.ExtraBold,
                                maxLines = 1,
                                softWrap = false
                            )
                        }
                    }
                }

                // Balanced 3D Tactile Quick Action Row (Anti-SS Manual Toggle, Report, Kunci)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Manual Anti-Screenshot & Anti-Screen-Record Button in Vault Dashboard
                    Box(
                        modifier = Modifier
                            .weight(1.1f)
                            .duolingoButton(
                                faceColor = if (securityConfig.isAntiScreenshotEnabled) com.example.ui.theme.DuoFeatherGreen else com.example.ui.theme.DuoBeetlePurple,
                                lipColor = if (securityConfig.isAntiScreenshotEnabled) com.example.ui.theme.DuoFeatherGreenLip else com.example.ui.theme.DuoBeetlePurpleLip,
                                cornerRadius = 14.dp,
                                lipHeight = 3.dp
                            )
                            .instantTouchPress {
                                onToggleAntiScreenshot(!securityConfig.isAntiScreenshotEnabled)
                            }
                            .padding(bottom = 3.dp)
                            .testTag("vault_toggle_anti_screenshot_button"),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Security,
                                contentDescription = "Toggle Mode Anti-Screenshot & Anti-Rekam Layar",
                                tint = Color.White,
                                modifier = Modifier.size(14.dp)
                            )
                            Text(
                                text = if (securityConfig.isAntiScreenshotEnabled) "Anti-SS: AKTIF" else "Anti-SS: MATI",
                                style = MaterialTheme.typography.labelMedium,
                                color = Color.White,
                                fontWeight = FontWeight.ExtraBold,
                                maxLines = 1,
                                softWrap = false,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }

                    // 3D Duolingo Feedback & Report Center Top Button (Shows MATI if not logged into Google)
                    Box(
                        modifier = Modifier
                            .weight(1.1f)
                            .duolingoButton(
                                faceColor = if (isGoogleLoggedIn) com.example.ui.theme.DuoMacawBlue else com.example.ui.theme.DuoSwanBorder,
                                lipColor = if (isGoogleLoggedIn) com.example.ui.theme.DuoMacawBlueLip else com.example.ui.theme.DuoHareLip,
                                cornerRadius = 14.dp,
                                lipHeight = 3.dp
                            )
                            .instantTouchPress { onOpenFeedbackReportClick() }
                            .padding(bottom = 3.dp)
                            .testTag("open_feedback_report_top_button"),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                imageVector = if (isGoogleLoggedIn) Icons.Default.Info else Icons.Default.Lock,
                                contentDescription = "Saran & Report Center",
                                tint = if (isGoogleLoggedIn) Color.White else CosmicTextSubtle,
                                modifier = Modifier.size(14.dp)
                            )
                            Text(
                                text = if (isGoogleLoggedIn) "Saran & Report" else "Report (Mati)",
                                style = MaterialTheme.typography.labelMedium,
                                color = if (isGoogleLoggedIn) Color.White else CosmicTextSubtle,
                                fontWeight = FontWeight.ExtraBold,
                                maxLines = 1,
                                softWrap = false,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }

                    Box(
                        modifier = Modifier
                            .weight(0.75f)
                            .duolingoButton(
                                faceColor = com.example.ui.theme.DuoCardinalRed,
                                lipColor = com.example.ui.theme.DuoCardinalRedLip,
                                cornerRadius = 14.dp,
                                lipHeight = 3.dp
                            )
                            .instantTouchPress { onLockVaultClick() }
                            .padding(bottom = 3.dp)
                            .testTag("lock_vault_top_button"),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Lock,
                                contentDescription = "Kunci Brankas",
                                tint = Color.White,
                                modifier = Modifier.size(14.dp)
                            )
                            Text(
                                text = "Kunci",
                                style = MaterialTheme.typography.labelMedium,
                                color = Color.White,
                                fontWeight = FontWeight.ExtraBold,
                                maxLines = 1,
                                softWrap = false,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                }
            }
        }

        // 1B. Duolingo-Style Interactive Multi-Emotion Mascot Banner + 3D Holo Foil & Viral Hub Quick Row
        item {
            var showMascotAssetShowcase by remember { mutableStateOf(false) }
            var activeDashboardViralModal by remember { mutableStateOf<Int?>(null) }
            val activePose = com.example.ui.theme.AppThemeController.mascotActivityPose
            val activeAccessory = com.example.ui.theme.AppThemeController.mascotAccessory

            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Interactive3DHoloCardContainer(
                    holoType = ViralTrendsHubState.selectedHoloFoil,
                    cornerRadius = 22.dp,
                    maxTiltDegrees = 10f,
                    foilIntensity = 0.24f,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    NyxMascotCompanionBanner(
                        activeVaultId = displayVaultNum,
                        headline = if (totalCount == 0) {
                            "Halo! Ketuk aku untuk melihat ekspresi nangis ngejer, marah api, atau gembira!"
                        } else {
                            "$encryptedCount dari $totalCount file aman terkunci (.NYX) • ${allFolderNames.size} Folder"
                        },
                        subtitle = "Geser kartu untuk efek 3D Hologram Foil • Klik 1x nama penuh • Klik 2x buka",
                        mood = MascotMood.WAVE_HELLO,
                        onQuickActionClick = onOpenZipScannerToolClick,
                        quickActionLabel = "Pemindai ZIP"
                    )
                }

                // Quick Row: Photobooth Stiker 3D, Time Capsule, Arcanum Wrapped & Receiptify
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .duolingoButton(
                                faceColor = com.example.ui.theme.DuoBeetlePurple,
                                lipColor = com.example.ui.theme.DuoBeetlePurpleLip,
                                cornerRadius = 14.dp,
                                lipHeight = 3.dp
                            )
                            .instantTouchPress { activeDashboardViralModal = 0 }
                            .padding(bottom = 3.dp)
                            .padding(vertical = 8.dp, horizontal = 6.dp)
                            .testTag("dashboard_open_photobooth_button"),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(Icons.Default.PhotoCamera, contentDescription = null, tint = Color.White, modifier = Modifier.size(13.dp))
                            Text(
                                text = "Photobooth 3D",
                                fontSize = 9.5.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = Color.White,
                                maxLines = 1
                            )
                        }
                    }

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .duolingoButton(
                                faceColor = com.example.ui.theme.DuoMacawBlue,
                                lipColor = com.example.ui.theme.DuoMacawBlueLip,
                                cornerRadius = 14.dp,
                                lipHeight = 3.dp
                            )
                            .instantTouchPress { activeDashboardViralModal = 1 }
                            .padding(bottom = 3.dp)
                            .padding(vertical = 8.dp, horizontal = 6.dp)
                            .testTag("dashboard_open_time_capsule_button"),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(Icons.Default.HourglassBottom, contentDescription = null, tint = Color.White, modifier = Modifier.size(13.dp))
                            Text(
                                text = "Time Capsule (${VaultTimeCapsuleRepository.capsules.size})",
                                fontSize = 9.5.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = Color.White,
                                maxLines = 1
                            )
                        }
                    }

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .duolingoButton(
                                faceColor = com.example.ui.theme.DuoFeatherGreen,
                                lipColor = com.example.ui.theme.DuoFeatherGreenLip,
                                cornerRadius = 14.dp,
                                lipHeight = 3.dp
                            )
                            .instantTouchPress { activeDashboardViralModal = 2 }
                            .padding(bottom = 3.dp)
                            .padding(vertical = 8.dp, horizontal = 6.dp)
                            .testTag("dashboard_open_wrapped_receiptify_button"),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(Icons.Default.ReceiptLong, contentDescription = null, tint = Color.White, modifier = Modifier.size(13.dp))
                            Text(
                                text = "Wrapped/Struk",
                                fontSize = 9.5.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = Color.White,
                                maxLines = 1
                            )
                        }
                    }
                }

                activeDashboardViralModal?.let { modalTab ->
                    ViralTrendsFullModalDialog(
                        initialTab = modalTab,
                        activeVaultId = displayVaultNum,
                        allItems = allItems,
                        onDismiss = { activeDashboardViralModal = null },
                        onPostBanner = {}
                    )
                }

                // Quick Toggle Bar for 360° Cursor-Tracking Stage, Form Shield & Abundant 3D Pose Assets
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .duolingoButton(
                            faceColor = if (showMascotAssetShowcase) com.example.ui.theme.DuoBeetlePurple else com.example.ui.theme.DuoPureWhite,
                            lipColor = if (showMascotAssetShowcase) com.example.ui.theme.DuoBeetlePurpleLip else com.example.ui.theme.DuoHareLip,
                            cornerRadius = 16.dp,
                            lipHeight = 3.dp
                        )
                        .instantTouchPress { showMascotAssetShowcase = !showMascotAssetShowcase }
                        .padding(bottom = 3.dp)
                        .padding(horizontal = 14.dp, vertical = 10.dp)
                        .testTag("dashboard_toggle_mascot_3d_assets_button")
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
                            Icon(
                                imageVector = Icons.Default.Security,
                                contentDescription = null,
                                tint = if (showMascotAssetShowcase) Color.White else activePose.badgeColor,
                                modifier = Modifier.size(20.dp)
                            )
                            Column {
                                Text(
                                    text = "Studio Maskot Kursor 360° & Galeri 10+ Asset 3D",
                                    style = MaterialTheme.typography.labelLarge,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = if (showMascotAssetShowcase) Color.White else CosmicTextPure
                                )
                                Text(
                                    text = "Pose: ${activePose.displayName} • Ikut Kursor, Tidur, Menari, Baca & Perisai Formulir",
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = if (showMascotAssetShowcase) Color.White.copy(alpha = 0.9f) else CosmicTextSubtle,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(3.dp)
                        ) {
                            Text(
                                text = if (showMascotAssetShowcase) "Tutup" else "Buka Studio",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.ExtraBold,
                                color = if (showMascotAssetShowcase) Color.White else com.example.ui.theme.DuoBeetlePurple
                            )
                            Icon(
                                imageVector = if (showMascotAssetShowcase) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                                contentDescription = null,
                                tint = if (showMascotAssetShowcase) Color.White else com.example.ui.theme.DuoBeetlePurple,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }

                if (showMascotAssetShowcase) {
                    AbundantMascotAssetAndFormSandboxCard(
                        activePose = activePose,
                        activeAccessory = activeAccessory,
                        onSelectPoseAndAccessory = { pose, accessory ->
                            com.example.ui.theme.AppThemeController.mascotActivityPose = pose
                            com.example.ui.theme.AppThemeController.mascotAccessory = accessory
                            com.example.ui.theme.AppThemeController.mascotCosmicAuraEnabled = true
                            com.example.ui.theme.AppThemeController.poseTriggerNonce++
                        }
                    )
                }
            }
        }

        // 2. Duolingo Unit Header & Gamified Action Deck
        if (!isFilesOnlyMode) {
            item {
                MinimalistVaultOverviewCard(
                    activeVaultId = displayVaultNum,
                    formattedTotalSize = formattedTotalSize,
                    encryptedCount = encryptedCount,
                    decryptedCount = decryptedCount,
                    totalCount = totalCount,
                    isMascotOverlayVisible = isMascotOverlayVisible,
                    onSummonMascotOverlayClick = onSummonMascotOverlayClick,
                    onOpenFeedbackReportClick = onOpenFeedbackReportClick,
                    onEncryptAllClick = { onBatchEncryptAllWithPin(true) },
                    onDecryptAllClick = { onBatchEncryptAllWithPin(false) },
                    onImportFileClick = onImportFromDeviceClick,
                    onCreateFileClick = onCreateSecretFileClick,
                    onOpenZipScannerToolClick = onOpenZipScannerToolClick,
                    onOpenMoriDownloaderClick = onOpenMoriDownloaderClick,
                    onOpenScriptEngineClick = onOpenScriptEngineClick
                )
            }
        } else {
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(50.dp)
                            .duolingoButton(
                                faceColor = com.example.ui.theme.DuoFeatherGreen,
                                lipColor = com.example.ui.theme.DuoFeatherGreenLip,
                                cornerRadius = 16.dp,
                                lipHeight = 4.dp
                            )
                            .instantTouchPress { onImportFromDeviceClick() }
                            .padding(bottom = 4.dp)
                            .testTag("files_import_button"),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.UploadFile, contentDescription = null, tint = Color.White, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Impor File / ZIP", style = MaterialTheme.typography.labelLarge, color = Color.White)
                        }
                    }

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(50.dp)
                            .duolingoButton(
                                faceColor = com.example.ui.theme.DuoMacawBlue,
                                lipColor = com.example.ui.theme.DuoMacawBlueLip,
                                cornerRadius = 16.dp,
                                lipHeight = 4.dp
                            )
                            .instantTouchPress { onCreateSecretFileClick() }
                            .padding(bottom = 4.dp)
                            .testTag("files_create_button"),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Add, contentDescription = null, tint = Color.White, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Catatan Baru", style = MaterialTheme.typography.labelLarge, color = Color.White)
                        }
                    }
                }
            }
        }

        // 3. Smart Search & Quick Filter Bar (Nama, Ekstensi .mov/.mp4/.mp3, Ukuran MB/KB, Folder & Status)
        item {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = onSearchQueryChange,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("vault_search_input"),
                    placeholder = {
                        Text(
                            "Smart Search: cari nama, ekstensi (.mov/.mp4/.mp3), ukuran (MB/KB), folder...",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    },
                    leadingIcon = {
                        Icon(Icons.Default.Search, contentDescription = "Smart Search", tint = com.example.ui.theme.DuoMacawBlue)
                    },
                    trailingIcon = {
                        if (searchQuery.isNotEmpty()) {
                            IconButton(onClick = { onSearchQueryChange("") }) {
                                Icon(Icons.Default.Close, contentDescription = "Hapus pencarian")
                            }
                        }
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(18.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = com.example.ui.theme.DuoPureWhite,
                        unfocusedContainerColor = com.example.ui.theme.DuoPureWhite,
                        focusedBorderColor = com.example.ui.theme.DuoMacawBlue,
                        unfocusedBorderColor = com.example.ui.theme.DuoSwanBorder,
                        focusedTextColor = CosmicTextPure,
                        unfocusedTextColor = CosmicTextPure
                    )
                )

                // Baris Chip Filter Cepat Smart Search
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    FilterChip(
                        selected = selectedCategory == null && !showOnlyCloudFiles && smartQuickFilter == "ALL",
                        onClick = {
                            showOnlyCloudFiles = false
                            smartQuickFilter = "ALL"
                            onSelectCategory(null)
                        },
                        label = { Text("Semua ($totalCount)", fontWeight = FontWeight.Bold) },
                        leadingIcon = if (selectedCategory == null && !showOnlyCloudFiles && smartQuickFilter == "ALL") {
                            { Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(15.dp)) }
                        } else null,
                        shape = RoundedCornerShape(50),
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = com.example.ui.theme.DuoFeatherGreen,
                            selectedLabelColor = Color.White,
                            selectedLeadingIconColor = Color.White,
                            containerColor = com.example.ui.theme.DuoPureWhite,
                            labelColor = CosmicTextSubtle
                        ),
                        border = androidx.compose.foundation.BorderStroke(
                            2.dp,
                            if (selectedCategory == null && !showOnlyCloudFiles && smartQuickFilter == "ALL") com.example.ui.theme.DuoFeatherGreenLip else com.example.ui.theme.DuoSwanBorder
                        )
                    )

                    FilterChip(
                        selected = smartQuickFilter == "LOCKED",
                        onClick = {
                            smartQuickFilter = if (smartQuickFilter == "LOCKED") "ALL" else "LOCKED"
                        },
                        label = { Text("Terkunci NYX ($encryptedCount)", fontWeight = FontWeight.Bold) },
                        leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null, modifier = Modifier.size(14.dp)) },
                        shape = RoundedCornerShape(50),
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = com.example.ui.theme.DuoFoxOrange,
                            selectedLabelColor = Color.White,
                            containerColor = com.example.ui.theme.DuoPureWhite,
                            labelColor = com.example.ui.theme.DuoFoxOrangeLip
                        )
                    )

                    FilterChip(
                        selected = smartQuickFilter == "UNLOCKED",
                        onClick = {
                            smartQuickFilter = if (smartQuickFilter == "UNLOCKED") "ALL" else "UNLOCKED"
                        },
                        label = { Text("Terbuka ($decryptedCount)", fontWeight = FontWeight.Bold) },
                        leadingIcon = { Icon(Icons.Default.LockOpen, contentDescription = null, modifier = Modifier.size(14.dp)) },
                        shape = RoundedCornerShape(50),
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = com.example.ui.theme.DuoFeatherGreen,
                            selectedLabelColor = Color.White,
                            containerColor = com.example.ui.theme.DuoPureWhite,
                            labelColor = com.example.ui.theme.DuoFeatherGreenLip
                        )
                    )

                    FilterChip(
                        selected = smartQuickFilter == "MOV_VIDEO",
                        onClick = {
                            smartQuickFilter = if (smartQuickFilter == "MOV_VIDEO") "ALL" else "MOV_VIDEO"
                        },
                        label = { Text(".MOV / .MP4", fontWeight = FontWeight.Bold) },
                        leadingIcon = { Icon(Icons.Default.Movie, contentDescription = null, modifier = Modifier.size(14.dp)) },
                        shape = RoundedCornerShape(50),
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = com.example.ui.theme.DuoMacawBlue,
                            selectedLabelColor = Color.White,
                            containerColor = com.example.ui.theme.DuoPureWhite,
                            labelColor = com.example.ui.theme.DuoMacawBlueLip
                        )
                    )

                    FilterChip(
                        selected = smartQuickFilter == "LARGE",
                        onClick = {
                            smartQuickFilter = if (smartQuickFilter == "LARGE") "ALL" else "LARGE"
                        },
                        label = { Text("Ukuran Besar (>5MB)", fontWeight = FontWeight.Bold) },
                        leadingIcon = { Icon(Icons.Default.Inventory2, contentDescription = null, modifier = Modifier.size(14.dp)) },
                        shape = RoundedCornerShape(50),
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = com.example.ui.theme.DuoBeetlePurple,
                            selectedLabelColor = Color.White,
                            containerColor = com.example.ui.theme.DuoPureWhite,
                            labelColor = com.example.ui.theme.DuoBeetlePurpleLip
                        )
                    )

                    FilterChip(
                        selected = showOnlyCloudFiles,
                        onClick = {
                            if (isGoogleLoggedIn) {
                                showOnlyCloudFiles = !showOnlyCloudFiles
                            } else {
                                onOpenCloudBackupTabClick()
                            }
                        },
                        label = {
                            Text(
                                if (isGoogleLoggedIn) "Di Cloud (${cloudEntryByItemId.size})" else "Cloud (Mati • Login Google)",
                                fontWeight = FontWeight.ExtraBold
                            )
                        },
                        leadingIcon = {
                            Icon(
                                imageVector = if (isGoogleLoggedIn) Icons.Default.CloudDone else Icons.Default.Lock,
                                contentDescription = "Filter File di Cloud Google",
                                modifier = Modifier.size(15.dp)
                            )
                        },
                        shape = RoundedCornerShape(50),
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = com.example.ui.theme.DuoMacawBlue,
                            selectedLabelColor = Color.White,
                            selectedLeadingIconColor = Color.White,
                            containerColor = com.example.ui.theme.DuoPureWhite,
                            labelColor = if (isGoogleLoggedIn) com.example.ui.theme.DuoMacawBlueLip else CosmicTextSubtle
                        ),
                        border = androidx.compose.foundation.BorderStroke(
                            2.dp,
                            if (showOnlyCloudFiles) com.example.ui.theme.DuoMacawBlueLip else com.example.ui.theme.DuoSwanBorder
                        ),
                        modifier = Modifier.testTag("filter_chip_in_cloud")
                    )

                    FilterChip(
                        selected = false,
                        onClick = onOpenCloudBackupTabClick,
                        label = {
                            Text(
                                if (isGoogleLoggedIn) "Buka Tab Cloud Google →" else "Cloud Mati (Login Google) →",
                                fontWeight = FontWeight.ExtraBold
                            )
                        },
                        leadingIcon = {
                            Icon(
                                imageVector = if (isGoogleLoggedIn) Icons.Default.CloudUpload else Icons.Default.Lock,
                                contentDescription = "Buka Tab Cloud Google",
                                modifier = Modifier.size(15.dp)
                            )
                        },
                        shape = RoundedCornerShape(50),
                        colors = FilterChipDefaults.filterChipColors(
                            containerColor = com.example.ui.theme.DuoBeetlePurple.copy(alpha = 0.12f),
                            labelColor = com.example.ui.theme.DuoBeetlePurpleLip
                        ),
                        border = androidx.compose.foundation.BorderStroke(
                            1.5.dp,
                            com.example.ui.theme.DuoBeetlePurple
                        ),
                        modifier = Modifier.testTag("open_cloud_backup_tab_chip")
                    )

                    VaultCategory.entries.forEach { cat ->
                        val count = allItems.count { it.category == cat.name }
                        val isSelected = selectedCategory == cat
                        val catColor = when (cat) {
                            VaultCategory.VIDEO -> com.example.ui.theme.DuoMacawBlue
                            VaultCategory.AUDIO -> com.example.ui.theme.DuoBeetlePurpleLip
                            VaultCategory.IMAGE -> com.example.ui.theme.DuoCardinalRed
                            VaultCategory.DOCUMENT -> com.example.ui.theme.DuoFoxOrange
                            VaultCategory.ARCHIVE -> com.example.ui.theme.DuoFeatherGreen
                        }
                        FilterChip(
                            selected = isSelected,
                            onClick = { onSelectCategory(if (isSelected) null else cat) },
                            label = { Text("${cat.label} ($count)", fontWeight = FontWeight.Bold) },
                            shape = RoundedCornerShape(50),
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = catColor,
                                selectedLabelColor = Color.White,
                                containerColor = com.example.ui.theme.DuoPureWhite,
                                labelColor = catColor
                            ),
                            border = androidx.compose.foundation.BorderStroke(
                                2.dp,
                                if (isSelected) catColor else catColor.copy(alpha = 0.45f)
                            )
                        )
                    }
                }
            }
        }

        // 3B. Folder & ZIP Archive Hierarchy Section
        item {
            Column(
                modifier = Modifier.fillMaxWidth(),
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
                        Icon(
                            imageVector = if (currentOpenFolder == null) Icons.Default.Folder else Icons.Default.FolderOpen,
                            contentDescription = null,
                            tint = com.example.ui.theme.DuoFoxOrange,
                            modifier = Modifier.size(20.dp)
                        )
                        Text(
                            text = if (currentOpenFolder == null) {
                                "Folder & Hasil ZIP (${visibleChildFolders.size})"
                            } else {
                                "Folder: $currentOpenFolder"
                            },
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.ExtraBold,
                            color = CosmicTextPure,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }

                    FilterChip(
                        selected = false,
                        onClick = { showCreateFolderDialog = true },
                        label = {
                            Text(
                                text = if (currentOpenFolder == null) "+ Folder Baru" else "+ Subfolder",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                maxLines = 1
                            )
                        },
                        leadingIcon = {
                            Icon(Icons.Default.CreateNewFolder, contentDescription = null, modifier = Modifier.size(14.dp))
                        },
                        modifier = Modifier.testTag("create_new_folder_button")
                    )
                }

                // Prominent Back / Exit Folder Navigation Bar when inside a Folder
                if (currentOpenFolder != null) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .duolingoButton(
                                    faceColor = com.example.ui.theme.DuoMacawBlue,
                                    lipColor = com.example.ui.theme.DuoMacawBlueLip,
                                    cornerRadius = 14.dp,
                                    lipHeight = 3.dp
                                )
                                .instantTouchPress {
                                    val parent = currentOpenFolder?.substringBeforeLast('/', "")?.takeIf { it.isNotEmpty() }
                                    expandedFullNameKey = null
                                    currentOpenFolder = parent
                                }
                                .padding(bottom = 3.dp, start = 12.dp, end = 12.dp, top = 7.dp)
                                .testTag("folder_back_button")
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(5.dp),
                                modifier = Modifier.padding(bottom = 3.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                    contentDescription = "Kembali",
                                    tint = Color.White,
                                    modifier = Modifier.size(15.dp)
                                )
                                Text(
                                    text = if (currentOpenFolder?.contains('/') == true) "Kembali ke Induk" else "Kembali ke Awal",
                                    style = MaterialTheme.typography.labelMedium,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = Color.White,
                                    maxLines = 1
                                )
                            }
                        }

                        FilterChip(
                            selected = false,
                            onClick = {
                                renameFolderInput = currentOpenFolder ?: ""
                                folderBeingRenamed = currentOpenFolder
                            },
                            label = { Text("Ganti Nama Folder", fontSize = 11.sp, fontWeight = FontWeight.Bold) },
                            leadingIcon = {
                                Icon(Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(14.dp))
                            },
                            modifier = Modifier.testTag("breadcrumb_rename_folder_button")
                        )

                        FilterChip(
                            selected = false,
                            onClick = {
                                folderBeingDeleted = currentOpenFolder
                            },
                            label = {
                                Text(
                                    text = "Hapus Folder",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = com.example.ui.theme.DuoCardinalRedLip
                                )
                            },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = "Hapus Folder Ini",
                                    tint = com.example.ui.theme.DuoCardinalRed,
                                    modifier = Modifier.size(14.dp)
                                )
                            },
                            modifier = Modifier.testTag("breadcrumb_delete_folder_button")
                        )

                        if (currentOpenFolder?.contains('/') == true) {
                            FilterChip(
                                selected = true,
                                onClick = {
                                    expandedFullNameKey = null
                                    currentOpenFolder = null
                                },
                                label = { Text("Ke Beranda Folder", fontSize = 11.sp, fontWeight = FontWeight.Bold) },
                                leadingIcon = {
                                    Icon(Icons.Default.Close, contentDescription = null, modifier = Modifier.size(14.dp))
                                },
                                modifier = Modifier.testTag("breadcrumb_exit_folder_button")
                            )
                        }
                    }
                }
            }
        }

        if (visibleChildFolders.isNotEmpty()) {
            items(visibleChildFolders, key = { "folder_$it" }) { folderPath ->
                val folderItems = remember(allItems, folderPath) {
                    allItems.filter {
                        val itemFolder = normalizeFolderPath(it.groupArchiveName)
                        itemFolder != null && (
                            itemFolder.equals(folderPath, ignoreCase = true) ||
                                itemFolder.startsWith("$folderPath/", ignoreCase = true)
                            )
                    }
                }
                val subfolderCount = remember(allFolderNames, folderPath) {
                    val prefix = "$folderPath/"
                    allFolderNames
                        .filter { it.startsWith(prefix, ignoreCase = true) && it.length > prefix.length }
                        .map { it.substring(prefix.length).substringBefore('/') }
                        .distinct()
                        .size
                }
                val folderVideoCount = remember(folderItems) {
                    folderItems.count { it.category == VaultCategory.VIDEO.name }
                }
                val folderKey = "folder_$folderPath"
                val isFullNameExpanded = expandedFullNameKey == folderKey

                VaultFolderCard(
                    folderName = folderPath.substringAfterLast('/'),
                    fullFolderPath = folderPath,
                    itemCount = folderItems.size,
                    subfolderCount = subfolderCount,
                    videoCount = folderVideoCount,
                    totalBytes = folderItems.sumOf { it.sizeBytes },
                    isFullNameExpanded = isFullNameExpanded,
                    onSingleClickToggleName = {
                        expandedFullNameKey = if (isFullNameExpanded) null else folderKey
                    },
                    onDoubleClickOpenFolder = {
                        expandedFullNameKey = null
                        currentOpenFolder = folderPath
                    },
                    onRenameFolderClick = {
                        renameFolderInput = folderPath
                        folderBeingRenamed = folderPath
                    },
                    onDeleteFolderClick = {
                        folderBeingDeleted = folderPath
                    }
                )
            }
        }

        // 4. Minimalist Section Title with 1x-Click / 2x-Click Gesture Hint
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (currentOpenFolder == null) {
                        if (searchQuery.isNotBlank()) {
                            "Hasil Pencarian File (${displayedFiles.size})"
                        } else {
                            "File Mandiri di Luar Folder (${displayedFiles.size})"
                        }
                    } else {
                        "Isi File di Folder Ini (${displayedFiles.size})"
                    },
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = CosmicTextPure,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f, fill = false)
                )
                Text(
                    text = "1x Klik: Nama Penuh • 2x Klik: Buka",
                    style = MaterialTheme.typography.labelSmall,
                    color = VaultTextMuted,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1
                )
            }
        }

        // 5. Empty State or Fixed-Height File List
        if (displayedFiles.isEmpty()) {
            item {
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
                            .padding(26.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        CosmicDragonAvatar(
                            mood = MascotMood.HAPPY_VIBES,
                            size = 96.dp,
                            persona = MascotPersona.KAWAII_FOLDER
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = if (allItems.isEmpty()) {
                                "Brankas Masih Kosong"
                            } else if (currentOpenFolder != null) {
                                "Folder '$currentOpenFolder' Masih Kosong"
                            } else {
                                "Tidak Ada File yang Cocok"
                            },
                            style = MaterialTheme.typography.titleLarge,
                            color = CosmicTextPure,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Impor file atau arsip .ZIP (otomatis menjadi Folder & Playlist Video).",
                            style = MaterialTheme.typography.bodyMedium,
                            color = CosmicTextSubtle,
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
                                .instantTouchPress { onImportFromDeviceClick() }
                                .padding(bottom = 4.dp, start = 20.dp, end = 20.dp)
                                .testTag("empty_state_import_button"),
                            contentAlignment = Alignment.Center
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.UploadFile, contentDescription = null, tint = Color.White, modifier = Modifier.size(19.dp))
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Impor File / ZIP", style = MaterialTheme.typography.titleSmall, color = Color.White, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        } else {
            items(displayedFiles, key = { it.id }) { item ->
                val fileKey = "file_${item.id}"
                val isFullNameExpanded = expandedFullNameKey == fileKey
                val cloudEntry = cloudEntryByItemId[item.id]
                VaultFileCard(
                    item = item,
                    isFullNameExpanded = isFullNameExpanded,
                    isBackedUpInCloud = cloudEntry != null,
                    cloudEntryVaultLabel = cloudEntry?.let { "Brankas #${it.sourceVaultId} • ${it.sourceVaultName}" },
                    onSingleClickToggleName = {
                        expandedFullNameKey = if (isFullNameExpanded) null else fileKey
                    },
                    onToggleEncryptWithPin = { onToggleEncryptItemWithPin(item) },
                    onBackupToCloud = { onBackupSingleItemToCloud(item) },
                    onInspect = { onInspectItem(item) },
                    onOpenOrPlay = { onOpenOrPlayItem(item) },
                    onMoveToFolder = {
                        moveTargetFolderInput = item.groupArchiveName?.removeSuffix(".zip") ?: ""
                        itemBeingMoved = item
                    },
                    onDelete = { onDeleteItem(item) }
                )
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun VaultFolderCard(
    folderName: String,
    fullFolderPath: String = folderName,
    itemCount: Int,
    subfolderCount: Int = 0,
    videoCount: Int,
    totalBytes: Long,
    isFullNameExpanded: Boolean,
    onSingleClickToggleName: () -> Unit,
    onDoubleClickOpenFolder: () -> Unit,
    onRenameFolderClick: () -> Unit,
    onDeleteFolderClick: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("vault_folder_card_$fullFolderPath")
    ) {
        // Responsive min-height 74.dp box so long folder names NEVER stretch the card and larger fonts never clip!
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 74.dp)
                .duolingoCard(
                    backgroundColor = com.example.ui.theme.DuoPureWhite,
                    borderColor = com.example.ui.theme.DuoFoxOrange,
                    lipColor = com.example.ui.theme.DuoFoxOrangeLip,
                    cornerRadius = 22.dp,
                    borderWidth = 2.dp,
                    lipHeight = 4.dp
                )
                .combinedClickable(
                    onClick = onSingleClickToggleName,
                    onDoubleClick = onDoubleClickOpenFolder
                )
                .padding(bottom = 4.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(com.example.ui.theme.DuoFoxOrange.copy(alpha = 0.16f))
                        .border(2.dp, com.example.ui.theme.DuoFoxOrange, RoundedCornerShape(14.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.FolderZip,
                        contentDescription = "Folder Brankas",
                        tint = com.example.ui.theme.DuoFoxOrange,
                        modifier = Modifier.size(24.dp)
                    )
                }

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = folderName,
                        style = MaterialTheme.typography.titleMedium,
                        color = CosmicTextPure,
                        fontWeight = FontWeight.ExtraBold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = buildString {
                            append("$itemCount file")
                            if (subfolderCount > 0) append(" • $subfolderCount subfolder")
                            append(" • ${formatFileSize(totalBytes)}")
                            if (videoCount > 0) append(" • Playlist ($videoCount Video)")
                        },
                        style = MaterialTheme.typography.labelSmall,
                        color = CosmicTextSubtle,
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
                            .size(34.dp)
                            .clip(CircleShape)
                            .background(com.example.ui.theme.DuoMintSurface)
                            .border(1.5.dp, com.example.ui.theme.DuoSwanBorder, CircleShape)
                            .instantTouchPress { onRenameFolderClick() }
                            .testTag("rename_folder_button_$fullFolderPath"),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Ganti Nama Folder",
                            tint = CosmicTextPure,
                            modifier = Modifier.size(16.dp)
                        )
                    }

                    Box(
                        modifier = Modifier
                            .size(34.dp)
                            .clip(CircleShape)
                            .background(com.example.ui.theme.DuoCardinalRedSoft)
                            .border(1.5.dp, com.example.ui.theme.DuoCardinalRed, CircleShape)
                            .instantTouchPress { onDeleteFolderClick() }
                            .testTag("delete_folder_button_$fullFolderPath"),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Hapus Folder",
                            tint = com.example.ui.theme.DuoCardinalRedLip,
                            modifier = Modifier.size(16.dp)
                        )
                    }

                    Box(
                        modifier = Modifier
                            .duolingoButton(
                                faceColor = com.example.ui.theme.DuoFoxOrange,
                                lipColor = com.example.ui.theme.DuoFoxOrangeLip,
                                cornerRadius = 12.dp,
                                lipHeight = 3.dp
                            )
                            .instantTouchPress { onDoubleClickOpenFolder() }
                            .padding(bottom = 3.dp, start = 10.dp, end = 10.dp, top = 6.dp)
                            .testTag("open_folder_button_$fullFolderPath")
                    ) {
                        Text(
                            text = "2x Buka",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            maxLines = 1,
                            modifier = Modifier.padding(bottom = 3.dp)
                        )
                    }
                }
            }
        }

        // 1x Click Toggle Reveal Pill for Full Folder Name (1x Click again minimizes!)
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
                        onDoubleClick = onDoubleClickOpenFolder
                    )
                    .testTag("folder_full_name_pill_$fullFolderPath"),
                shape = RoundedCornerShape(14.dp),
                color = com.example.ui.theme.VaultObsidian,
                border = androidx.compose.foundation.BorderStroke(1.5.dp, com.example.ui.theme.DuoFoxOrange)
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
                            text = "Nama Lengkap Folder (Klik 1x lagi untuk minimize • Klik 2x untuk buka):",
                            fontSize = 10.sp,
                            color = com.example.ui.theme.VaultLime,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = fullFolderPath,
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.White,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun MinimalistVaultOverviewCard(
    activeVaultId: Int,
    formattedTotalSize: String,
    encryptedCount: Int,
    decryptedCount: Int,
    totalCount: Int,
    isMascotOverlayVisible: Boolean,
    onSummonMascotOverlayClick: () -> Unit,
    onOpenFeedbackReportClick: () -> Unit,
    onEncryptAllClick: () -> Unit,
    onDecryptAllClick: () -> Unit,
    onImportFileClick: () -> Unit,
    onCreateFileClick: () -> Unit,
    onOpenZipScannerToolClick: () -> Unit,
    onOpenMoriDownloaderClick: () -> Unit,
    onOpenScriptEngineClick: () -> Unit
) {
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
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .duolingoButton(
                        faceColor = com.example.ui.theme.DuoFeatherGreen,
                        lipColor = com.example.ui.theme.DuoFeatherGreenLip,
                        cornerRadius = 20.dp,
                        lipHeight = 4.dp
                    )
                    .padding(bottom = 4.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 14.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "UNIT BRANKAS $activeVaultId • PENYIMPANAN",
                            style = MaterialTheme.typography.labelMedium,
                            color = Color.White.copy(alpha = 0.92f),
                            fontWeight = FontWeight.Bold,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            text = formattedTotalSize,
                            style = MaterialTheme.typography.headlineMedium,
                            color = Color.White,
                            fontWeight = FontWeight.ExtraBold,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        Surface(
                            color = com.example.ui.theme.DuoPureWhite,
                            shape = RoundedCornerShape(14.dp),
                            border = androidx.compose.foundation.BorderStroke(2.dp, com.example.ui.theme.DuoFoxOrange)
                        ) {
                            Column(
                                modifier = Modifier.padding(horizontal = 9.dp, vertical = 5.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = "$encryptedCount",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = com.example.ui.theme.DuoFoxOrangeLip
                                )
                                Text(
                                    text = "Terkunci",
                                    fontSize = 9.5.sp,
                                    color = com.example.ui.theme.DuoFoxOrangeLip,
                                    fontWeight = FontWeight.Bold,
                                    maxLines = 1
                                )
                            }
                        }

                        Surface(
                            color = com.example.ui.theme.DuoPureWhite,
                            shape = RoundedCornerShape(14.dp),
                            border = androidx.compose.foundation.BorderStroke(2.dp, com.example.ui.theme.DuoMacawBlue)
                        ) {
                            Column(
                                modifier = Modifier.padding(horizontal = 9.dp, vertical = 5.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = "$decryptedCount",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = com.example.ui.theme.DuoMacawBlueLip
                                )
                                Text(
                                    text = "Terbuka",
                                    fontSize = 9.5.sp,
                                    color = com.example.ui.theme.DuoMacawBlueLip,
                                    fontWeight = FontWeight.Bold,
                                    maxLines = 1
                                )
                            }
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
                        .weight(1.25f)
                        .heightIn(min = 48.dp)
                        .duolingoButton(
                            faceColor = com.example.ui.theme.DuoMacawBlue,
                            lipColor = com.example.ui.theme.DuoMacawBlueLip,
                            cornerRadius = 16.dp,
                            lipHeight = 4.dp
                        )
                        .instantTouchPress { onImportFileClick() }
                        .padding(bottom = 4.dp, start = 8.dp, end = 8.dp, top = 6.dp)
                        .testTag("dashboard_import_button"),
                    contentAlignment = Alignment.Center
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.UploadFile, contentDescription = null, tint = Color.White, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(5.dp))
                        Text(
                            "Impor File / ZIP",
                            style = MaterialTheme.typography.titleSmall,
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .heightIn(min = 48.dp)
                        .duolingoButton(
                            faceColor = com.example.ui.theme.DuoBeeYellow,
                            lipColor = com.example.ui.theme.DuoBeeYellowLip,
                            cornerRadius = 16.dp,
                            lipHeight = 4.dp
                        )
                        .instantTouchPress { onCreateFileClick() }
                        .padding(bottom = 4.dp, start = 8.dp, end = 8.dp, top = 6.dp)
                        .testTag("dashboard_create_file_button"),
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(Icons.Default.Add, contentDescription = null, tint = com.example.ui.theme.DuoEelInk, modifier = Modifier.size(17.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            "Catatan",
                            style = MaterialTheme.typography.titleSmall,
                            color = com.example.ui.theme.DuoEelInk,
                            fontWeight = FontWeight.Bold,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .heightIn(min = 42.dp)
                        .duolingoButton(
                            faceColor = com.example.ui.theme.DuoCardinalRed,
                            lipColor = com.example.ui.theme.DuoCardinalRedLip,
                            cornerRadius = 14.dp,
                            lipHeight = 3.dp
                        )
                        .instantTouchPress { onEncryptAllClick() }
                        .padding(bottom = 3.dp, start = 4.dp, end = 4.dp)
                        .testTag("hero_encrypt_all_button"),
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(3.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Lock,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(13.dp)
                        )
                        Text(
                            text = "Kunci",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .heightIn(min = 42.dp)
                        .duolingoButton(
                            faceColor = com.example.ui.theme.DuoFeatherGreen,
                            lipColor = com.example.ui.theme.DuoFeatherGreenLip,
                            cornerRadius = 14.dp,
                            lipHeight = 3.dp
                        )
                        .instantTouchPress { onDecryptAllClick() }
                        .padding(bottom = 3.dp, start = 4.dp, end = 4.dp)
                        .testTag("hero_decrypt_all_button"),
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(3.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.LockOpen,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(13.dp)
                        )
                        Text(
                            text = "Buka",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }

                Box(
                    modifier = Modifier
                        .weight(1.1f)
                        .heightIn(min = 42.dp)
                        .duolingoButton(
                            faceColor = com.example.ui.theme.DuoFoxOrange,
                            lipColor = com.example.ui.theme.DuoFoxOrangeLip,
                            cornerRadius = 14.dp,
                            lipHeight = 3.dp
                        )
                        .instantTouchPress { onOpenZipScannerToolClick() }
                        .padding(bottom = 3.dp, start = 4.dp, end = 4.dp)
                        .testTag("dashboard_open_zip_scanner_tool_button"),
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(3.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.FolderZip,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(13.dp)
                        )
                        Text(
                            text = "ZIP Scan",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .heightIn(min = 42.dp)
                        .duolingoButton(
                            faceColor = com.example.ui.theme.DuoBeetlePurple,
                            lipColor = com.example.ui.theme.DuoBeetlePurpleLip,
                            cornerRadius = 14.dp,
                            lipHeight = 3.dp
                        )
                        .instantTouchPress { onOpenMoriDownloaderClick() }
                        .padding(bottom = 3.dp, start = 4.dp, end = 4.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(3.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.CloudDownload,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(13.dp)
                        )
                        Text(
                            text = "Mori",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }

            // Baris Tombol Maskot Overlay & Saran/Report Center (Terhubung ke Google Docs Anti-Spam)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .weight(1.1f)
                        .heightIn(min = 44.dp)
                        .duolingoButton(
                            faceColor = if (isMascotOverlayVisible) com.example.ui.theme.DuoBeeYellow else com.example.ui.theme.DuoFeatherGreen,
                            lipColor = if (isMascotOverlayVisible) com.example.ui.theme.DuoBeeYellowLip else com.example.ui.theme.DuoFeatherGreenLip,
                            cornerRadius = 16.dp,
                            lipHeight = 4.dp
                        )
                        .instantTouchPress { onSummonMascotOverlayClick() }
                        .padding(bottom = 4.dp, start = 6.dp, end = 6.dp)
                        .testTag("dashboard_mascot_overlay_button"),
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(5.dp)
                    ) {
                        Icon(
                            imageVector = if (isMascotOverlayVisible) Icons.Default.Folder else Icons.Default.Visibility,
                            contentDescription = "Maskot Overlay",
                            tint = if (isMascotOverlayVisible) com.example.ui.theme.DuoEelInk else Color.White,
                            modifier = Modifier.size(15.dp)
                        )
                        Text(
                            text = if (isMascotOverlayVisible) "Maskot Overlay" else "Munculkan Maskot",
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.ExtraBold,
                            color = if (isMascotOverlayVisible) com.example.ui.theme.DuoEelInk else Color.White,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }

                Box(
                    modifier = Modifier
                        .weight(1.15f)
                        .heightIn(min = 44.dp)
                        .duolingoButton(
                            faceColor = com.example.ui.theme.DuoBeetlePurple,
                            lipColor = com.example.ui.theme.DuoBeetlePurpleLip,
                            cornerRadius = 16.dp,
                            lipHeight = 4.dp
                        )
                        .instantTouchPress { onOpenFeedbackReportClick() }
                        .padding(bottom = 4.dp, start = 6.dp, end = 6.dp)
                        .testTag("dashboard_feedback_report_button"),
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(5.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Description,
                            contentDescription = "Saran & Report ke Google Docs",
                            tint = Color.White,
                            modifier = Modifier.size(15.dp)
                        )
                        Text(
                            text = "Saran & Report",
                            style = MaterialTheme.typography.labelLarge,
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
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun VaultFileCard(
    item: VaultItemEntity,
    isFullNameExpanded: Boolean,
    isBackedUpInCloud: Boolean = false,
    cloudEntryVaultLabel: String? = null,
    onSingleClickToggleName: () -> Unit,
    onToggleEncryptWithPin: () -> Unit,
    onBackupToCloud: () -> Unit = {},
    onInspect: () -> Unit,
    onOpenOrPlay: () -> Unit,
    onMoveToFolder: () -> Unit,
    onDelete: () -> Unit
) {
    val categoryEnum = VaultCategory.entries.find { it.name == item.category } ?: VaultCategory.DOCUMENT
    val openActionLabel = when (categoryEnum) {
        VaultCategory.VIDEO -> "Tonton"
        VaultCategory.IMAGE -> "Lihat"
        VaultCategory.AUDIO -> "Putar"
        else -> "Buka"
    }

    val catColor = when (categoryEnum) {
        VaultCategory.VIDEO -> com.example.ui.theme.DuoMacawBlue
        VaultCategory.AUDIO -> com.example.ui.theme.DuoBeetlePurpleLip
        VaultCategory.IMAGE -> com.example.ui.theme.DuoCardinalRed
        VaultCategory.DOCUMENT -> com.example.ui.theme.DuoFoxOrange
        VaultCategory.ARCHIVE -> com.example.ui.theme.DuoFeatherGreen
    }

    val fullFileName = "${item.name}.${item.extension}"
    val cleanFolderLabel = item.groupArchiveName?.removeSuffix(".zip")?.removeSuffix(".ZIP")?.trim()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("vault_file_card_${item.id}")
    ) {
        // Responsive card box that keeps filenames 100% readable on 720x1600px (320dp-360dp) and all screen sizes!
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 76.dp)
                .duolingoCard(
                    backgroundColor = com.example.ui.theme.DuoPureWhite,
                    borderColor = if (item.isEncrypted) com.example.ui.theme.DuoSwanBorder else com.example.ui.theme.DuoMacawBlue,
                    lipColor = if (item.isEncrypted) com.example.ui.theme.DuoHareLip else com.example.ui.theme.DuoMacawBlueLip,
                    cornerRadius = 22.dp,
                    borderWidth = 2.dp,
                    lipHeight = 4.dp
                )
                .combinedClickable(
                    onClick = onSingleClickToggleName,
                    onDoubleClick = onOpenOrPlay
                )
                .padding(bottom = 4.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 10.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Top Row: Category Icon + Full Readable Filename/Metadata + Primary Open/Play CTA
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // Left Category Icon Badge in Colorful Duolingo Squircle
                    Box(
                        modifier = Modifier
                            .size(42.dp)
                            .clip(RoundedCornerShape(14.dp))
                            .background(catColor.copy(alpha = 0.16f))
                            .border(2.dp, catColor, RoundedCornerShape(14.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = categoryIcon(categoryEnum),
                            contentDescription = categoryEnum.label,
                            tint = catColor,
                            modifier = Modifier.size(21.dp)
                        )
                    }

                    // Middle File Title & Metadata (Generous width even on 720x1600px!)
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = fullFileName,
                            style = MaterialTheme.typography.titleMedium,
                            color = CosmicTextPure,
                            fontWeight = FontWeight.Bold,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )

                        Spacer(modifier = Modifier.height(2.dp))

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(5.dp)
                        ) {
                            Text(
                                text = formatFileSize(item.sizeBytes),
                                style = MaterialTheme.typography.labelSmall,
                                color = CosmicTextSubtle,
                                maxLines = 1
                            )
                            Text(
                                text = "•",
                                style = MaterialTheme.typography.labelSmall,
                                color = CosmicTextSubtle
                            )
                            Text(
                                text = if (item.isEncrypted) "NYX2" else "Terbuka",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = if (item.isEncrypted) com.example.ui.theme.DuoFoxOrangeLip else com.example.ui.theme.DuoFeatherGreenLip,
                                maxLines = 1
                            )
                            if (isBackedUpInCloud) {
                                Text(
                                    text = "• DI CLOUD",
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = com.example.ui.theme.DuoMacawBlueLip,
                                    maxLines = 1,
                                    modifier = Modifier.testTag("file_cloud_badge_${item.id}")
                                )
                            }
                            if (!cleanFolderLabel.isNullOrBlank()) {
                                Text(
                                    text = "• Folder: $cleanFolderLabel",
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = com.example.ui.theme.DuoMacawBlueLip,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }
                    }

                    // Primary Open/Play Action Button
                    Box(
                        modifier = Modifier
                            .duolingoButton(
                                faceColor = if (item.isEncrypted) com.example.ui.theme.DuoMacawBlue else com.example.ui.theme.DuoFeatherGreen,
                                lipColor = if (item.isEncrypted) com.example.ui.theme.DuoMacawBlueLip else com.example.ui.theme.DuoFeatherGreenLip,
                                cornerRadius = 12.dp,
                                lipHeight = 3.dp
                            )
                            .instantTouchPress { onOpenOrPlay() }
                            .padding(bottom = 3.dp, start = 10.dp, end = 10.dp, top = 6.dp)
                            .testTag("open_or_play_file_${item.id}")
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(3.dp),
                            modifier = Modifier.padding(bottom = 3.dp)
                        ) {
                            Icon(
                                imageVector = if (categoryEnum == VaultCategory.VIDEO || categoryEnum == VaultCategory.AUDIO) {
                                    Icons.Default.PlayArrow
                                } else {
                                    Icons.Default.Visibility
                                },
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(14.dp)
                            )
                            Text(
                                text = openActionLabel,
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                maxLines = 1
                            )
                        }
                    }
                }

                // Bottom Compact Action Strip: Cloud Backup + Move Folder + Lock/Unlock + Info + Delete
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.End
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        // 1-Tap Cloud Google Encrypted Backup Button
                        Box(
                            modifier = Modifier
                                .size(30.dp)
                                .clip(CircleShape)
                                .background(
                                    (if (isBackedUpInCloud) com.example.ui.theme.DuoFeatherGreen else com.example.ui.theme.DuoMacawBlue).copy(alpha = 0.16f)
                                )
                                .border(
                                    1.5.dp,
                                    if (isBackedUpInCloud) com.example.ui.theme.DuoFeatherGreen else com.example.ui.theme.DuoMacawBlue,
                                    CircleShape
                                )
                                .instantTouchPress { onBackupToCloud() }
                                .testTag("cloud_backup_file_${item.id}"),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = if (isBackedUpInCloud) Icons.Default.CloudDone else Icons.Default.CloudUpload,
                                contentDescription = if (isBackedUpInCloud) "Sudah Terenkripsi di Cloud Google" else "Cadangkan File ke Cloud Google",
                                tint = if (isBackedUpInCloud) com.example.ui.theme.DuoFeatherGreenLip else com.example.ui.theme.DuoMacawBlueLip,
                                modifier = Modifier.size(14.dp)
                            )
                        }

                        Box(
                            modifier = Modifier
                                .size(30.dp)
                                .clip(CircleShape)
                                .background(com.example.ui.theme.DuoFoxOrange.copy(alpha = 0.15f))
                                .border(1.5.dp, com.example.ui.theme.DuoFoxOrange, CircleShape)
                                .instantTouchPress { onMoveToFolder() }
                                .testTag("move_folder_file_${item.id}"),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.DriveFileMove,
                                contentDescription = "Pindahkan ke Folder Lain",
                                tint = com.example.ui.theme.DuoFoxOrangeLip,
                                modifier = Modifier.size(14.dp)
                            )
                        }

                        Box(
                            modifier = Modifier
                                .size(30.dp)
                                .clip(CircleShape)
                                .background(
                                    (if (item.isEncrypted) com.example.ui.theme.DuoFeatherGreen else com.example.ui.theme.DuoFoxOrange).copy(alpha = 0.16f)
                                )
                                .border(
                                    1.5.dp,
                                    if (item.isEncrypted) com.example.ui.theme.DuoFeatherGreen else com.example.ui.theme.DuoFoxOrange,
                                    CircleShape
                                )
                                .instantTouchPress { onToggleEncryptWithPin() }
                                .testTag("toggle_encrypt_file_${item.id}"),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = if (item.isEncrypted) Icons.Default.LockOpen else Icons.Default.Lock,
                                contentDescription = if (item.isEncrypted) "Dekripsi dengan PIN" else "Enkripsi dengan PIN",
                                tint = if (item.isEncrypted) com.example.ui.theme.DuoFeatherGreenLip else com.example.ui.theme.DuoFoxOrangeLip,
                                modifier = Modifier.size(14.dp)
                            )
                        }

                        Box(
                            modifier = Modifier
                                .size(30.dp)
                                .clip(CircleShape)
                                .background(com.example.ui.theme.DuoMintSurface)
                                .border(1.5.dp, com.example.ui.theme.DuoSwanBorder, CircleShape)
                                .instantTouchPress { onInspect() },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Info,
                                contentDescription = "Detail File",
                                tint = CosmicTextSubtle,
                                modifier = Modifier.size(14.dp)
                            )
                        }

                        Box(
                            modifier = Modifier
                                .size(30.dp)
                                .clip(CircleShape)
                                .background(com.example.ui.theme.DuoCardinalRedSoft)
                                .border(1.5.dp, com.example.ui.theme.DuoCardinalRed, CircleShape)
                                .instantTouchPress { onDelete() }
                                .testTag("delete_file_${item.id}"),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Hapus File",
                                tint = com.example.ui.theme.DuoCardinalRedLip,
                                modifier = Modifier.size(14.dp)
                            )
                        }
                    }
                }
            }
        }

        // 1x Click Full Name Reveal Pill (Click 1x again to minimize, or Click 2x to open!)
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
                        onDoubleClick = onOpenOrPlay
                    )
                    .testTag("file_full_name_pill_${item.id}"),
                shape = RoundedCornerShape(14.dp),
                color = com.example.ui.theme.VaultObsidian,
                border = androidx.compose.foundation.BorderStroke(1.5.dp, catColor)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    Text(
                        text = "Nama Lengkap (Klik 1x lagi untuk minimize • Klik 2x untuk buka):",
                        fontSize = 10.sp,
                        color = com.example.ui.theme.VaultLime,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = fullFileName,
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.White,
                        fontWeight = FontWeight.ExtraBold
                    )
                    if (!cleanFolderLabel.isNullOrBlank()) {
                        Text(
                            text = "Folder / Playlist: $cleanFolderLabel",
                            fontSize = 10.sp,
                            color = Color.White.copy(alpha = 0.8f)
                        )
                    }
                    if (isBackedUpInCloud && !cloudEntryVaultLabel.isNullOrBlank()) {
                        Text(
                            text = "Tersimpan Terenkripsi di Cloud Google ($cloudEntryVaultLabel)",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = com.example.ui.theme.VaultLime
                        )
                    }
                }
            }
        }
    }
}

fun categoryIcon(category: VaultCategory): ImageVector {
    return when (category) {
        VaultCategory.IMAGE -> Icons.Default.Image
        VaultCategory.VIDEO -> Icons.Default.VideoFile
        VaultCategory.AUDIO -> Icons.Default.AudioFile
        VaultCategory.DOCUMENT -> Icons.Default.Description
        VaultCategory.ARCHIVE -> Icons.Default.FolderZip
    }
}

fun formatFileSize(bytes: Long): String {
    if (bytes <= 0L) return "0 B"
    val kb = bytes / 1024.0
    if (kb < 1024.0) return String.format(java.util.Locale.US, "%.1f KB", kb)
    val mb = kb / 1024.0
    if (mb < 1024.0) return String.format(java.util.Locale.US, "%.1f MB", mb)
    val gb = mb / 1024.0
    return String.format(java.util.Locale.US, "%.2f GB", gb)
}
