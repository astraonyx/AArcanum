package com.example.ui

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckBox
import androidx.compose.material.icons.filled.CheckBoxOutlineBlank
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CloudDone
import androidx.compose.material.icons.filled.CloudDownload
import androidx.compose.material.icons.filled.CloudUpload
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.GoogleCloudVaultBackupEngine
import com.example.data.SecurityConfig
import com.example.data.VaultCategory
import com.example.data.VaultItemEntity
import com.example.ui.theme.CosmicTextMuted
import com.example.ui.theme.CosmicTextPure
import com.example.ui.theme.CosmicTextSubtle
import com.example.ui.theme.DuoBeeYellow
import com.example.ui.theme.DuoBeeYellowLip
import com.example.ui.theme.DuoBeetlePurple
import com.example.ui.theme.DuoBeetlePurpleLip
import com.example.ui.theme.DuoCardinalRed
import com.example.ui.theme.DuoCardinalRedLip
import com.example.ui.theme.DuoCardinalRedSoft
import com.example.ui.theme.DuoEelInk
import com.example.ui.theme.DuoFeatherGreen
import com.example.ui.theme.DuoFeatherGreenLip
import com.example.ui.theme.DuoFeatherGreenSoft
import com.example.ui.theme.DuoFoxOrange
import com.example.ui.theme.DuoFoxOrangeLip
import com.example.ui.theme.DuoHareLip
import com.example.ui.theme.DuoMacawBlue
import com.example.ui.theme.DuoMacawBlueLip
import com.example.ui.theme.DuoMacawBlueSoft
import com.example.ui.theme.DuoPureWhite
import com.example.ui.theme.DuoSwanBorder
import com.example.ui.theme.JetBrainsMonoFontFamily
import com.example.ui.theme.duolingoButton
import com.example.ui.theme.duolingoCard
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun GoogleCloudBackupTabScreen(
    activeVaultId: Int,
    securityConfig: SecurityConfig,
    activeVaultItems: List<VaultItemEntity>,
    allVaultsItems: List<VaultItemEntity>,
    cloudBackupEntries: List<GoogleCloudVaultBackupEngine.CloudEncryptedFileEntry>,
    onBackupSelectedIds: (Set<Int>) -> Unit,
    onBackupEntireVault: (Int) -> Unit,
    onRestoreCloudEntryToActiveVault: (GoogleCloudVaultBackupEngine.CloudEncryptedFileEntry) -> Unit,
    onDeleteCloudEntry: (GoogleCloudVaultBackupEngine.CloudEncryptedFileEntry) -> Unit,
    onRefreshCloudEntries: () -> Unit,
    onGoogleAccountSignedIn: (String, String) -> Unit
) {
    val displayVaultNum = SecurityConfig.displayVaultNumber(activeVaultId)
    val activeVaultLabel = securityConfig.getVaultDisplayLabel(displayVaultNum)
    val isGoogleLoggedIn = securityConfig.isGoogleLinked && securityConfig.googleAccountEmail.isNotBlank()

    // Selected item IDs for manual backup
    val selectedItemIds = remember { mutableStateListOf<Int>() }
    // Vault source filter in Cloud Explorer: 0 = Semua Brankas, 1..N = Brankas tertentu
    var selectedCloudSourceVaultFilter by remember { mutableIntStateOf(0) }
    // Target Vault selector for manual backup: default = activeVaultId
    var targetManualVaultId by remember(displayVaultNum) { mutableIntStateOf(displayVaultNum) }
    var showGoogleSignInModal by remember { mutableStateOf(false) }

    val itemsForManualSelection = remember(allVaultsItems, activeVaultItems, targetManualVaultId, displayVaultNum) {
        if (targetManualVaultId == displayVaultNum) {
            activeVaultItems
        } else {
            allVaultsItems.filter { it.vaultProfileId == targetManualVaultId }
        }
    }

    val filteredCloudEntries = remember(cloudBackupEntries, selectedCloudSourceVaultFilter) {
        if (selectedCloudSourceVaultFilter == 0) {
            cloudBackupEntries
        } else {
            cloudBackupEntries.filter { it.sourceVaultId == selectedCloudSourceVaultFilter }
        }
    }

    val backedUpKeysSet = remember(cloudBackupEntries) {
        cloudBackupEntries.map { "${it.sourceVaultId}_${it.originalItemId}_${it.fileName.lowercase()}.${it.extension.lowercase()}" }.toSet()
    }

    if (showGoogleSignInModal) {
        ArcanumGoogleSignInDialog(
            currentEmail = securityConfig.googleAccountEmail,
            currentName = securityConfig.googleAccountDisplayName,
            isMandatoryGate = false,
            onDismiss = { showGoogleSignInModal = false },
            onSignedIn = { email, name ->
                showGoogleSignInModal = false
                onGoogleAccountSignedIn(email, name)
            }
        )
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .testTag("google_cloud_backup_screen"),
        contentPadding = PaddingValues(start = 18.dp, end = 18.dp, top = 12.dp, bottom = 112.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // 1. Header Banner: Cloud Google Terenkripsi (.nyxcloud AES-256-GCM)
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .duolingoButton(
                        faceColor = if (isGoogleLoggedIn) DuoMacawBlue else DuoCardinalRed,
                        lipColor = if (isGoogleLoggedIn) DuoMacawBlueLip else DuoCardinalRedLip,
                        cornerRadius = 24.dp,
                        lipHeight = 5.dp
                    )
                    .padding(bottom = 5.dp)
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
                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(44.dp)
                                    .clip(CircleShape)
                                    .background(Color.White.copy(alpha = 0.22f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = if (isGoogleLoggedIn) Icons.Default.CloudDone else Icons.Default.Lock,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                            Column {
                                Text(
                                    text = if (isGoogleLoggedIn) "CADANGKAN FILE CLOUD GOOGLE" else "PENYIMPANAN CLOUD MATI TOTAL",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = Color.White
                                )
                                Text(
                                    text = if (isGoogleLoggedIn) {
                                        "Enkripsi Kontainer .nyxcloud AES-256-GCM • Pemetaan Lintas Brankas"
                                    } else {
                                        "Fitur Cloud Google dinonaktifkan penuh hingga Anda Login Google (Opsional)"
                                    },
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Color.White.copy(alpha = 0.92f),
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                        if (isGoogleLoggedIn) {
                            Box(
                                modifier = Modifier
                                    .size(38.dp)
                                    .clip(CircleShape)
                                    .background(Color.White.copy(alpha = 0.2f))
                                    .clickable { onRefreshCloudEntries() }
                                    .testTag("cloud_backup_refresh_button"),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Refresh,
                                    contentDescription = "Segarkan Cloud",
                                    tint = Color.White,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                    }

                    // Active Google Account Pill
                    Surface(
                        color = DuoPureWhite,
                        shape = RoundedCornerShape(16.dp),
                        border = BorderStroke(2.dp, if (isGoogleLoggedIn) DuoFeatherGreen else DuoCardinalRed)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 12.dp, vertical = 10.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                modifier = Modifier.weight(1f)
                            ) {
                                Icon(
                                    imageVector = if (isGoogleLoggedIn) Icons.Default.CheckCircle else Icons.Default.Lock,
                                    contentDescription = null,
                                    tint = if (isGoogleLoggedIn) DuoFeatherGreenLip else DuoCardinalRedLip,
                                    modifier = Modifier.size(20.dp)
                                )
                                Column {
                                    Text(
                                        text = if (isGoogleLoggedIn) {
                                            "AKUN GOOGLE CLOUD TERHUBUNG"
                                        } else {
                                            "FITUR MATI TOTAL • BELUM LOGIN GOOGLE"
                                        },
                                        fontFamily = JetBrainsMonoFontFamily,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.ExtraBold,
                                        color = if (isGoogleLoggedIn) DuoFeatherGreenLip else DuoCardinalRedLip
                                    )
                                    Text(
                                        text = if (isGoogleLoggedIn) {
                                            "${securityConfig.googleAccountDisplayName} (${securityConfig.googleAccountEmail})"
                                        } else {
                                            "Login Google bersifat opsional, namun wajib untuk mengaktifkan Cloud & Report"
                                        },
                                        style = MaterialTheme.typography.labelMedium,
                                        color = CosmicTextPure,
                                        fontWeight = FontWeight.ExtraBold,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                }
                            }

                            Box(
                                modifier = Modifier
                                    .duolingoButton(
                                        faceColor = DuoFeatherGreen,
                                        lipColor = DuoFeatherGreenLip,
                                        cornerRadius = 12.dp,
                                        lipHeight = 3.dp
                                    )
                                    .instantTouchPress { showGoogleSignInModal = true }
                                    .padding(bottom = 3.dp, start = 10.dp, end = 10.dp, top = 6.dp)
                                    .testTag("cloud_backup_switch_google_button")
                            ) {
                                Text(
                                    text = if (isGoogleLoggedIn) "Ganti Akun" else "Login Google",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = Color.White,
                                    modifier = Modifier.padding(bottom = 2.dp)
                                )
                            }
                        }
                    }
                }
            }
        }

        if (!isGoogleLoggedIn) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .duolingoCard(
                            backgroundColor = DuoPureWhite,
                            borderColor = DuoCardinalRed,
                            lipColor = DuoCardinalRedLip,
                            cornerRadius = 24.dp,
                            borderWidth = 2.dp,
                            lipHeight = 5.dp
                        )
                        .padding(bottom = 5.dp)
                        .testTag("cloud_backup_total_lockout_card")
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(22.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(64.dp)
                                .duolingoButton(
                                    faceColor = DuoCardinalRed,
                                    lipColor = DuoCardinalRedLip,
                                    cornerRadius = 20.dp,
                                    lipHeight = 4.dp
                                )
                                .padding(bottom = 4.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Lock,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(32.dp)
                            )
                        }
                        Surface(
                            color = DuoCardinalRedSoft,
                            shape = RoundedCornerShape(50),
                            border = BorderStroke(1.5.dp, DuoCardinalRed)
                        ) {
                            Text(
                                text = "FITUR PENYIMPANAN CLOUD GOOGLE MATI TOTAL",
                                fontFamily = JetBrainsMonoFontFamily,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = DuoCardinalRedLip,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 5.dp)
                            )
                        }
                        Text(
                            text = "Login Akun Google Diperlukan untuk Penyimpanan Cloud",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.ExtraBold,
                            color = CosmicTextPure,
                            textAlign = TextAlign.Center
                        )
                        Text(
                            text = "Login Google bersifat opsional untuk pemakaian brankas lokal. Namun jika Anda belum login Google, fitur Penyimpanan Cloud (.nyxcloud) dan Pusat Report & Saran dimatikan secara total demi keamanan.",
                            style = MaterialTheme.typography.bodySmall,
                            color = CosmicTextSubtle,
                            textAlign = TextAlign.Center
                        )
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
                                .instantTouchPress { showGoogleSignInModal = true }
                                .padding(bottom = 4.dp)
                                .testTag("cloud_lockout_login_google_button"),
                            contentAlignment = Alignment.Center
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.AccountCircle,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(20.dp)
                                )
                                Text(
                                    text = "Login Akun Google Sekarang untuk Aktifkan Cloud",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = Color.White
                                )
                            }
                        }
                    }
                }
            }
        } else {

        // 2. Manual Selection Card: Pilih Manual File atau Brankas yang Ingin Dicadangkan ke Cloud Google
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .duolingoCard(
                        backgroundColor = DuoPureWhite,
                        borderColor = DuoSwanBorder,
                        lipColor = DuoHareLip,
                        cornerRadius = 24.dp,
                        borderWidth = 2.dp,
                        lipHeight = 5.dp
                    )
                    .padding(bottom = 5.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.CloudUpload,
                            contentDescription = null,
                            tint = DuoFeatherGreenLip,
                            modifier = Modifier.size(22.dp)
                        )
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "1. Pilih Manual File atau Brankas untuk Dicadangkan",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.ExtraBold,
                                color = CosmicTextPure
                            )
                            Text(
                                text = "Semua file yang dicadangkan ke Cloud Google otomatis dienkripsi penuh (.nyxcloud AES-256-GCM).",
                                style = MaterialTheme.typography.bodySmall,
                                color = CosmicTextSubtle
                            )
                        }
                    }

                    // Pilih Brankas Sumber yang ingin dicadangkan
                    Text(
                        text = "PILIH BRANKAS SUMBER:",
                        fontFamily = JetBrainsMonoFontFamily,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = DuoMacawBlueLip
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        for (vId in 1..securityConfig.vaultCount) {
                            val vName = securityConfig.getVaultName(vId)
                            val countInV = allVaultsItems.count { it.vaultProfileId == vId }
                            val isSelected = targetManualVaultId == vId
                            FilterChip(
                                selected = isSelected,
                                onClick = {
                                    targetManualVaultId = vId
                                    selectedItemIds.clear()
                                },
                                label = {
                                    Text(
                                        text = "Brankas #$vId • $vName ($countInV file)",
                                        fontWeight = FontWeight.ExtraBold,
                                        fontSize = 11.sp
                                    )
                                },
                                leadingIcon = if (isSelected) {
                                    { Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(14.dp)) }
                                } else null,
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = DuoMacawBlue,
                                    selectedLabelColor = Color.White,
                                    selectedLeadingIconColor = Color.White,
                                    containerColor = DuoMacawBlueSoft,
                                    labelColor = DuoMacawBlueLip
                                ),
                                modifier = Modifier.testTag("cloud_select_source_vault_$vId")
                            )
                        }
                    }

                    // 1-Tap Button to Backup Entire Selected Vault
                    val targetVaultName = securityConfig.getVaultName(targetManualVaultId)
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .duolingoButton(
                                faceColor = DuoBeetlePurple,
                                lipColor = DuoBeetlePurpleLip,
                                cornerRadius = 16.dp,
                                lipHeight = 4.dp
                            )
                            .instantTouchPress {
                                onBackupEntireVault(targetManualVaultId)
                            }
                            .padding(bottom = 4.dp)
                            .testTag("cloud_backup_entire_vault_button"),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Shield,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(18.dp)
                            )
                            Text(
                                text = "Cadangkan Seluruh Brankas #$targetManualVaultId ($targetVaultName) (${itemsForManualSelection.size} File)",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = Color.White,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }

                    // Checklist Manual File di Brankas Terpilih
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "ATAU CENTANG MANUAL FILE DI BRANKAS #$targetManualVaultId (${selectedItemIds.size} dipilih):",
                            fontFamily = JetBrainsMonoFontFamily,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = CosmicTextMuted,
                            modifier = Modifier
                                .weight(1f)
                                .padding(end = 8.dp)
                        )
                        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            Surface(
                                color = DuoFeatherGreenSoft,
                                shape = RoundedCornerShape(8.dp),
                                border = BorderStroke(1.dp, DuoFeatherGreen),
                                modifier = Modifier
                                    .clickable {
                                        selectedItemIds.clear()
                                        selectedItemIds.addAll(itemsForManualSelection.map { it.id })
                                    }
                                    .testTag("cloud_select_all_files_button")
                            ) {
                                Text(
                                    text = "Pilih Semua",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = DuoFeatherGreenLip,
                                    maxLines = 1,
                                    softWrap = false,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                            if (selectedItemIds.isNotEmpty()) {
                                Surface(
                                    color = DuoCardinalRedSoft,
                                    shape = RoundedCornerShape(8.dp),
                                    border = BorderStroke(1.dp, DuoCardinalRed),
                                    modifier = Modifier.clickable { selectedItemIds.clear() }
                                ) {
                                    Text(
                                        text = "Reset",
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.ExtraBold,
                                        color = DuoCardinalRedLip,
                                        maxLines = 1,
                                        softWrap = false,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                    )
                                }
                            }
                        }
                    }

                    if (itemsForManualSelection.isEmpty()) {
                        Surface(
                            color = DuoMacawBlueSoft.copy(alpha = 0.5f),
                            shape = RoundedCornerShape(14.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "Brankas #$targetManualVaultId ($targetVaultName) belum memiliki file untuk dicadangkan.",
                                style = MaterialTheme.typography.bodySmall,
                                color = CosmicTextSubtle,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(14.dp)
                            )
                        }
                    } else {
                        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                            itemsForManualSelection.forEach { item ->
                                val isChecked = selectedItemIds.contains(item.id)
                                val alreadyInCloud = backedUpKeysSet.contains(
                                    "${item.vaultProfileId}_${item.id}_${item.name.lowercase()}.${item.extension.lowercase()}"
                                )
                                Surface(
                                    color = if (isChecked) DuoFeatherGreenSoft else DuoPureWhite,
                                    shape = RoundedCornerShape(14.dp),
                                    border = BorderStroke(
                                        1.5.dp,
                                        if (isChecked) DuoFeatherGreen else DuoSwanBorder
                                    ),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable {
                                            if (isChecked) selectedItemIds.remove(item.id)
                                            else selectedItemIds.add(item.id)
                                        }
                                        .testTag("cloud_manual_check_item_${item.id}")
                                ) {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(horizontal = 12.dp, vertical = 9.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                                            modifier = Modifier.weight(1f)
                                        ) {
                                            Icon(
                                                imageVector = if (isChecked) Icons.Default.CheckBox else Icons.Default.CheckBoxOutlineBlank,
                                                contentDescription = null,
                                                tint = if (isChecked) DuoFeatherGreenLip else CosmicTextSubtle,
                                                modifier = Modifier.size(20.dp)
                                            )
                                            Column(modifier = Modifier.weight(1f)) {
                                                Text(
                                                    text = "${item.name}.${item.extension}",
                                                    style = MaterialTheme.typography.labelLarge,
                                                    fontWeight = FontWeight.ExtraBold,
                                                    color = CosmicTextPure,
                                                    maxLines = 1,
                                                    overflow = TextOverflow.Ellipsis
                                                )
                                                Text(
                                                    text = "${formatFileSize(item.sizeBytes)} • Brankas #${item.vaultProfileId} ($targetVaultName)",
                                                    style = MaterialTheme.typography.labelSmall,
                                                    color = CosmicTextSubtle
                                                )
                                            }
                                        }
                                        if (alreadyInCloud) {
                                            Surface(
                                                color = DuoMacawBlueSoft,
                                                shape = RoundedCornerShape(50),
                                                border = BorderStroke(1.dp, DuoMacawBlue)
                                            ) {
                                                Text(
                                                    text = "DI CLOUD",
                                                    fontSize = 9.sp,
                                                    fontWeight = FontWeight.ExtraBold,
                                                    color = DuoMacawBlueLip,
                                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp)
                                .duolingoButton(
                                    faceColor = if (selectedItemIds.isNotEmpty()) DuoFeatherGreen else DuoSwanBorder,
                                    lipColor = if (selectedItemIds.isNotEmpty()) DuoFeatherGreenLip else DuoHareLip,
                                    cornerRadius = 16.dp,
                                    lipHeight = 4.dp
                                )
                                .instantTouchPress {
                                    if (selectedItemIds.isNotEmpty()) {
                                        onBackupSelectedIds(selectedItemIds.toSet())
                                        selectedItemIds.clear()
                                    }
                                }
                                .padding(bottom = 4.dp)
                                .testTag("cloud_backup_selected_files_button"),
                            contentAlignment = Alignment.Center
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.CloudUpload,
                                    contentDescription = null,
                                    tint = if (selectedItemIds.isNotEmpty()) Color.White else CosmicTextSubtle,
                                    modifier = Modifier.size(18.dp)
                                )
                                Text(
                                    text = if (selectedItemIds.isNotEmpty()) {
                                        "Enkripsi & Cadangkan ${selectedItemIds.size} File Terpilih ke Cloud Google"
                                    } else {
                                        "Pilih File di Atas untuk Dicadangkan Manual"
                                    },
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = if (selectedItemIds.isNotEmpty()) Color.White else CosmicTextSubtle
                                )
                            }
                        }
                    }
                }
            }
        }

        // 3. Filter Lintas Brankas & Daftar File Terenkripsi di Cloud Google
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
                    Text(
                        text = "2. Daftar File Terenkripsi di Cloud Google (${filteredCloudEntries.size})",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.ExtraBold,
                        color = CosmicTextPure,
                        modifier = Modifier
                            .weight(1f)
                            .padding(end = 8.dp)
                    )
                    Surface(
                        color = DuoFeatherGreenSoft,
                        shape = RoundedCornerShape(50),
                        border = BorderStroke(1.dp, DuoFeatherGreen)
                    ) {
                        Text(
                            text = "AES-256-GCM .NYXCLOUD",
                            fontFamily = JetBrainsMonoFontFamily,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = DuoFeatherGreenLip,
                            maxLines = 1,
                            softWrap = false,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                        )
                    }
                }

                Text(
                    text = "Setiap file di Cloud menampilkan asal Brankas (#1, #2, dst. beserta nama Brankas) agar tidak tertukar.",
                    style = MaterialTheme.typography.bodySmall,
                    color = CosmicTextSubtle
                )

                // Filter Chip Berdasarkan Asal Brankas di Cloud
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    FilterChip(
                        selected = selectedCloudSourceVaultFilter == 0,
                        onClick = { selectedCloudSourceVaultFilter = 0 },
                        label = {
                            Text(
                                "Semua Brankas (${cloudBackupEntries.size})",
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 11.sp
                            )
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = DuoFeatherGreen,
                            selectedLabelColor = Color.White,
                            containerColor = DuoPureWhite,
                            labelColor = CosmicTextPure
                        )
                    )

                    for (vId in 1..securityConfig.vaultCount) {
                        val vName = securityConfig.getVaultName(vId)
                        val countFromVault = cloudBackupEntries.count { it.sourceVaultId == vId }
                        FilterChip(
                            selected = selectedCloudSourceVaultFilter == vId,
                            onClick = {
                                selectedCloudSourceVaultFilter = if (selectedCloudSourceVaultFilter == vId) 0 else vId
                            },
                            label = {
                                Text(
                                    "Brankas #$vId • $vName ($countFromVault)",
                                    fontWeight = FontWeight.ExtraBold,
                                    fontSize = 11.sp
                                )
                            },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = DuoMacawBlue,
                                selectedLabelColor = Color.White,
                                containerColor = DuoPureWhite,
                                labelColor = DuoMacawBlueLip
                            ),
                            modifier = Modifier.testTag("cloud_filter_vault_$vId")
                        )
                    }
                }
            }
        }

        if (filteredCloudEntries.isEmpty()) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .duolingoCard(
                            backgroundColor = DuoPureWhite,
                            borderColor = DuoSwanBorder,
                            lipColor = DuoHareLip,
                            cornerRadius = 22.dp,
                            borderWidth = 2.dp,
                            lipHeight = 4.dp
                        )
                        .padding(24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.CloudUpload,
                            contentDescription = null,
                            tint = DuoMacawBlue,
                            modifier = Modifier.size(36.dp)
                        )
                        Text(
                            text = "Belum Ada File Terenkripsi di Cloud Google",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.ExtraBold,
                            color = CosmicTextPure
                        )
                        Text(
                            text = "Pilih file atau brankas pada bagian di atas untuk mencadangkan file terenkripsi (.nyxcloud) ke akun Google Anda.",
                            style = MaterialTheme.typography.bodySmall,
                            color = CosmicTextSubtle,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        } else {
            items(filteredCloudEntries, key = { it.cloudId }) { entry ->
                val resolvedVaultName = securityConfig.getVaultName(entry.sourceVaultId).ifBlank { entry.sourceVaultName }
                val dateFormatted = remember(entry.backedUpAtMillis) {
                    SimpleDateFormat("dd MMM yyyy • HH:mm", Locale.getDefault()).format(Date(entry.backedUpAtMillis))
                }
                val categoryEnum = VaultCategory.entries.find { it.name == entry.category } ?: VaultCategory.DOCUMENT

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .duolingoCard(
                            backgroundColor = DuoPureWhite,
                            borderColor = DuoMacawBlue,
                            lipColor = DuoMacawBlueLip,
                            cornerRadius = 20.dp,
                            borderWidth = 2.dp,
                            lipHeight = 4.dp
                        )
                        .padding(bottom = 4.dp)
                        .testTag("cloud_entry_card_${entry.cloudId}")
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // Top Row: Source Vault Origin Badge + Encrypted Status Badge
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Surface(
                                color = DuoBeeYellow.copy(alpha = 0.22f),
                                shape = RoundedCornerShape(50),
                                border = BorderStroke(1.5.dp, DuoFoxOrange),
                                modifier = Modifier
                                    .weight(1f, fill = false)
                                    .padding(end = 6.dp)
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(5.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Shield,
                                        contentDescription = null,
                                        tint = DuoFoxOrangeLip,
                                        modifier = Modifier.size(13.dp)
                                    )
                                    Text(
                                        text = "ASAL: BRANKAS #${entry.sourceVaultId} • ${resolvedVaultName.uppercase()}",
                                        fontFamily = JetBrainsMonoFontFamily,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.ExtraBold,
                                        color = DuoFoxOrangeLip,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis,
                                        modifier = Modifier.testTag("cloud_entry_vault_badge_${entry.cloudId}")
                                    )
                                }
                            }

                            Surface(
                                color = DuoFeatherGreenSoft,
                                shape = RoundedCornerShape(50),
                                border = BorderStroke(1.dp, DuoFeatherGreen)
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Lock,
                                        contentDescription = null,
                                        tint = DuoFeatherGreenLip,
                                        modifier = Modifier.size(12.dp)
                                    )
                                    Text(
                                        text = "TERENKRIPSI .NYXCLOUD",
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.ExtraBold,
                                        color = DuoFeatherGreenLip,
                                        maxLines = 1,
                                        softWrap = false
                                    )
                                }
                            }
                        }

                        // Middle Row: File Icon + Name + Details
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(42.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(DuoMacawBlueSoft)
                                    .border(1.5.dp, DuoMacawBlue, RoundedCornerShape(12.dp)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = categoryIcon(categoryEnum),
                                    contentDescription = null,
                                    tint = DuoMacawBlueLip,
                                    modifier = Modifier.size(22.dp)
                                )
                            }

                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "${entry.fileName}.${entry.extension}",
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = CosmicTextPure,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Text(
                                    text = "Ukuran Asli: ${formatFileSize(entry.plainSizeBytes)} • Cloud Cipher: ${formatFileSize(entry.encryptedSizeBytes)}",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = CosmicTextSubtle
                                )
                                Text(
                                    text = buildString {
                                        append("Akun: ${entry.googleAccountEmail} • $dateFormatted")
                                        if (!entry.groupArchiveName.isNullOrBlank()) {
                                            append(" • Folder: ${entry.groupArchiveName}")
                                        }
                                    },
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = DuoMacawBlueLip
                                )
                            }
                        }

                        // Bottom Action Row: Restore to Active Vault or Delete from Cloud
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .height(40.dp)
                                    .duolingoButton(
                                        faceColor = DuoFeatherGreen,
                                        lipColor = DuoFeatherGreenLip,
                                        cornerRadius = 12.dp,
                                        lipHeight = 3.dp
                                    )
                                    .instantTouchPress { onRestoreCloudEntryToActiveVault(entry) }
                                    .padding(bottom = 3.dp)
                                    .testTag("cloud_restore_entry_${entry.cloudId}"),
                                contentAlignment = Alignment.Center
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.CloudDownload,
                                        contentDescription = null,
                                        tint = Color.White,
                                        modifier = Modifier.size(15.dp)
                                    )
                                    Text(
                                        text = "Dekripsi & Pulihkan ke $activeVaultLabel",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.ExtraBold,
                                        color = Color.White,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                }
                            }

                            Box(
                                modifier = Modifier
                                    .height(40.dp)
                                    .duolingoButton(
                                        faceColor = DuoCardinalRed,
                                        lipColor = DuoCardinalRedLip,
                                        cornerRadius = 12.dp,
                                        lipHeight = 3.dp
                                    )
                                    .instantTouchPress { onDeleteCloudEntry(entry) }
                                    .padding(bottom = 3.dp, start = 12.dp, end = 12.dp)
                                    .testTag("cloud_delete_entry_${entry.cloudId}"),
                                contentAlignment = Alignment.Center
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Delete,
                                        contentDescription = "Hapus dari Cloud",
                                        tint = Color.White,
                                        modifier = Modifier.size(15.dp)
                                    )
                                    Text(
                                        text = "Hapus Cloud",
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
    }
}
