package com.example.ui

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BugReport
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DeleteSweep
import androidx.compose.material.icons.filled.EnhancedEncryption
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.FilterNone
import androidx.compose.material.icons.filled.FolderZip
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.MovieFilter
import androidx.compose.material.icons.filled.Science
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.VideoFile
import androidx.compose.material.icons.filled.WarningAmber
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import coil.compose.AsyncImage
import com.example.data.BatchZipScanReport
import com.example.data.SecurityConfig
import com.example.data.StagedFileStatus
import com.example.data.StagedScanItem
import com.example.data.VaultCategory
import com.example.data.VaultQuarantineEntry
import com.example.data.VaultVirusScanLogEntry
import com.example.ui.theme.CosmicBorderHairline
import com.example.ui.theme.CosmicObsidian
import com.example.ui.theme.CosmicTextMuted
import com.example.ui.theme.CosmicTextPure
import com.example.ui.theme.CosmicTextSubtle
import com.example.ui.theme.CosmicVoid
import com.example.ui.theme.DuoBeeYellow
import com.example.ui.theme.DuoBeeYellowLip
import com.example.ui.theme.DuoBeeYellowSoft
import com.example.ui.theme.DuoBeetlePurple
import com.example.ui.theme.DuoBeetlePurpleLip
import com.example.ui.theme.DuoBeetlePurpleSoft
import com.example.ui.theme.DuoCardinalRed
import com.example.ui.theme.DuoCardinalRedLip
import com.example.ui.theme.DuoCardinalRedSoft
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
import com.example.ui.theme.DuoPureWhite
import com.example.ui.theme.DuoSwanBorder
import com.example.ui.theme.JetBrainsMonoFontFamily
import com.example.ui.theme.PastelMint
import com.example.ui.theme.StarlightCyan
import com.example.ui.theme.VaultAmber
import com.example.ui.theme.VaultCharcoal
import com.example.ui.theme.VaultCoral
import com.example.ui.theme.VaultCream
import com.example.ui.theme.VaultLime
import com.example.ui.theme.VaultMintAccent
import com.example.ui.theme.VaultMintCard
import com.example.ui.theme.VaultObsidian
import com.example.ui.theme.VaultPureWhite
import com.example.ui.theme.VaultTextMuted
import com.example.ui.theme.duolingoButton
import com.example.ui.theme.duolingoCard
import java.io.File

data class ZipScannerUiState(
    val isScanning: Boolean = false,
    val progressMessage: String = "",
    val currentReport: BatchZipScanReport? = null,
    val showVirusWarningModal: Boolean = false,
    val selectedEntryIdsForExtraction: Set<String> = emptySet(),
    val quarantineHistory: List<VaultQuarantineEntry> = emptyList(),
    val virusScanLogs: List<VaultVirusScanLogEntry> = emptyList(),
    val activeVaultDuplicateWarnings: List<com.example.data.VaultDuplicateWarning> = emptyList(),
    val showActiveVaultRecheckDialog: Boolean = false
)

@Composable
fun ZipScannerToolScreen(
    activeVaultId: Int,
    scannerState: ZipScannerUiState,
    onPickMultipleZipsOrFilesClick: () -> Unit,
    onRunSimulatedMultiZipTestClick: () -> Unit,
    onCommitCleanOnlyToVault: () -> Unit,
    onCommitAllIncludingInfectedToVault: () -> Unit,
    onOpenVirusWarningModal: () -> Unit,
    onDismissVirusWarningModal: () -> Unit,
    onClearScanReport: () -> Unit,
    onToggleSelectEntryForExtraction: (String) -> Unit = {},
    onSelectAllCleanEntriesForExtraction: () -> Unit = {},
    onSelectAllEntriesForExtraction: () -> Unit = {},
    onCommitSelectedEntriesToVault: () -> Unit = onCommitCleanOnlyToVault,
    onQuarantineInfectedEntriesToVaultLog: () -> Unit = {},
    onDeleteQuarantineHistoryEntry: (String) -> Unit = {},
    onClearVaultQuarantineAndLogs: () -> Unit = {},
    onDecideSubClipAction: (String, Boolean) -> Unit = { _, _ -> },
    onRunActiveVaultDuplicateRecheck: () -> Unit = {},
    onDeleteDuplicateVaultItemById: (Int) -> Unit = {},
    onDismissActiveVaultRecheckDialog: () -> Unit = {}
) {
    val displayVaultNum = SecurityConfig.displayVaultNumber(activeVaultId)
    val report = scannerState.currentReport
    var activeMainSectionTab by remember { mutableIntStateOf(0) } // 0 = Pemindai, 1 = Karantina, 2 = Log
    var selectedFilterTab by remember { mutableIntStateOf(0) }
    var isSelectiveTreeExpanded by remember { mutableStateOf(false) }

    val displayedItems = remember(report, selectedFilterTab) {
        if (report == null) emptyList()
        else when (selectedFilterTab) {
            1 -> report.readyCleanItems
            2 -> report.duplicateRemovedItems
            3 -> report.subClipItems
            4 -> report.infectedItems
            else -> report.items
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .testTag("zip_scanner_tool_screen"),
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 12.dp, bottom = 112.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // 1. 6-Theme Header Bar
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .duolingoCard(
                        backgroundColor = CosmicObsidian,
                        borderColor = DuoSwanBorder,
                        lipColor = DuoHareLip,
                        cornerRadius = 18.dp,
                        borderWidth = 2.dp,
                        lipHeight = 4.dp
                    )
                    .padding(horizontal = 12.dp, vertical = 10.dp)
                    .padding(bottom = 4.dp),
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
                            .duolingoButton(
                                faceColor = DuoMacawBlue,
                                lipColor = DuoMacawBlueLip,
                                cornerRadius = 12.dp,
                                lipHeight = 3.dp
                            )
                            .padding(bottom = 3.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.FolderZip,
                            contentDescription = null,
                            tint = DuoPureWhite,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Column {
                        Text(
                            text = "ZIP & Video Scanner",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.ExtraBold,
                            color = CosmicTextPure
                        )
                        Text(
                            text = "SHA-256 • De-duplikasi Frame • Antivirus",
                            style = MaterialTheme.typography.bodySmall,
                            color = CosmicTextMuted
                        )
                    }
                }

                Box(
                    modifier = Modifier
                        .duolingoCard(
                            backgroundColor = CosmicObsidian,
                            borderColor = DuoFeatherGreen,
                            lipColor = DuoFeatherGreenLip,
                            cornerRadius = 12.dp,
                            borderWidth = 2.dp,
                            lipHeight = 3.dp
                        )
                        .padding(bottom = 3.dp)
                        .padding(horizontal = 10.dp, vertical = 5.dp)
                ) {
                    Text(
                        text = "BRANKAS #$displayVaultNum",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.ExtraBold,
                        color = DuoFeatherGreenLip
                    )
                }
            }
        }

        // 2. Clean Minimalist Action Card (No Long Text)
        item {
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
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // Primary Pick Button
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .duolingoButton(
                                faceColor = if (!scannerState.isScanning) DuoFeatherGreen else DuoSwanBorder,
                                lipColor = if (!scannerState.isScanning) DuoFeatherGreenLip else DuoHareLip,
                                cornerRadius = 14.dp,
                                lipHeight = 4.dp
                            )
                            .instantTouchPress(enabled = !scannerState.isScanning) {
                                onPickMultipleZipsOrFilesClick()
                            }
                            .padding(bottom = 4.dp)
                            .testTag("scanner_pick_multi_zip_button"),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.FolderZip,
                                contentDescription = null,
                                tint = DuoPureWhite,
                                modifier = Modifier.size(19.dp)
                            )
                            Text(
                                text = "Pilih File .ZIP / Video",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.ExtraBold,
                                color = DuoPureWhite
                            )
                        }
                    }

                    // Secondary Compact Action Row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
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
                                .instantTouchPress(enabled = !scannerState.isScanning) {
                                    onRunSimulatedMultiZipTestClick()
                                }
                                .padding(bottom = 3.dp, start = 6.dp, end = 6.dp)
                                .testTag("scanner_run_test_batch_button"),
                            contentAlignment = Alignment.Center
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(5.dp)
                            ) {
                                Icon(Icons.Default.Science, contentDescription = null, tint = DuoPureWhite, modifier = Modifier.size(15.dp))
                                Text(
                                    text = "Simulasi Uji ZIP",
                                    style = MaterialTheme.typography.labelMedium,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = DuoPureWhite,
                                    maxLines = 1
                                )
                            }
                        }

                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(42.dp)
                                .duolingoButton(
                                    faceColor = DuoFoxOrange,
                                    lipColor = DuoFoxOrangeLip,
                                    cornerRadius = 12.dp,
                                    lipHeight = 3.dp
                                )
                                .instantTouchPress(enabled = !scannerState.isScanning) {
                                    onRunActiveVaultDuplicateRecheck()
                                }
                                .padding(bottom = 3.dp, start = 6.dp, end = 6.dp)
                                .testTag("scanner_recheck_active_vault_button"),
                            contentAlignment = Alignment.Center
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(5.dp)
                            ) {
                                Icon(Icons.Default.FilterNone, contentDescription = null, tint = DuoPureWhite, modifier = Modifier.size(15.dp))
                                Text(
                                    text = "Cek Duplikat #$displayVaultNum",
                                    style = MaterialTheme.typography.labelMedium,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = DuoPureWhite,
                                    maxLines = 1
                                )
                            }
                        }
                    }
                }
            }
        }

        // 3. Minimalist 3-Tab Segmented Selector (Hasil Scan | Karantina | Log Scan)
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                val sectionTabs = listOf(
                    Triple(0, "Hasil Scan (${report?.items?.size ?: 0})", DuoMacawBlue to DuoMacawBlueLip),
                    Triple(1, "Karantina (${scannerState.quarantineHistory.size})", DuoCardinalRed to DuoCardinalRedLip),
                    Triple(2, "Log (${scannerState.virusScanLogs.size})", DuoBeetlePurple to DuoBeetlePurpleLip)
                )
                sectionTabs.forEach { (idx, title, colors) ->
                    val isSelected = activeMainSectionTab == idx
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(40.dp)
                            .then(
                                if (isSelected) {
                                    Modifier.duolingoButton(
                                        faceColor = colors.first,
                                        lipColor = colors.second,
                                        cornerRadius = 12.dp,
                                        lipHeight = 3.dp
                                    )
                                } else {
                                    Modifier.duolingoCard(
                                        backgroundColor = CosmicObsidian,
                                        borderColor = DuoSwanBorder,
                                        lipColor = DuoHareLip,
                                        cornerRadius = 12.dp,
                                        borderWidth = 1.5.dp,
                                        lipHeight = 3.dp
                                    )
                                }
                            )
                            .instantTouchPress { activeMainSectionTab = idx }
                            .padding(bottom = 3.dp)
                            .testTag(
                                when (idx) {
                                    1 -> "tab_quarantine_vault_chip"
                                    2 -> "tab_virus_logs_vault_chip"
                                    else -> "tab_scan_results_chip"
                                }
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = title,
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.ExtraBold,
                            color = if (isSelected) DuoPureWhite else CosmicTextPure,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }
        }

        // 4. Active Scan Progress Indicator
        if (scannerState.isScanning) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .duolingoCard(
                            backgroundColor = DuoFeatherGreenSoft,
                            borderColor = DuoFeatherGreen,
                            lipColor = DuoFeatherGreenLip,
                            cornerRadius = 18.dp
                        )
                        .padding(bottom = 4.dp)
                        .padding(14.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        CircularProgressIndicator(
                            color = DuoFeatherGreenLip,
                            strokeWidth = 3.dp,
                            modifier = Modifier.size(26.dp)
                        )
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Memindai Arsip ZIP...",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.ExtraBold,
                                color = VaultObsidian
                            )
                            Text(
                                text = scannerState.progressMessage.ifBlank { "Mengekstrak & memeriksa signature..." },
                                style = MaterialTheme.typography.bodySmall,
                                color = CosmicTextSubtle,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                }
            }
        }

        // 5. TAB 0: HASIL SCAN (Compact Summary + Accordion List)
        if (activeMainSectionTab == 0) {
            if (report == null && !scannerState.isScanning) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .duolingoCard(
                                backgroundColor = CosmicObsidian,
                                borderColor = DuoSwanBorder,
                                lipColor = DuoHareLip,
                                cornerRadius = 18.dp
                            )
                            .padding(bottom = 4.dp)
                            .padding(20.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.FolderZip,
                                contentDescription = null,
                                tint = CosmicTextMuted,
                                modifier = Modifier.size(32.dp)
                            )
                            Text(
                                text = "Belum Ada Arsip Dipindai",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.ExtraBold,
                                color = CosmicTextPure
                            )
                            Text(
                                text = "Pilih file .ZIP atau jalankan Simulasi Uji ZIP di atas.",
                                style = MaterialTheme.typography.bodySmall,
                                color = CosmicTextMuted,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }

            if (report != null && !scannerState.isScanning) {
                // Compact Summary & Commit Card
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .duolingoCard(
                                backgroundColor = CosmicObsidian,
                                borderColor = DuoMacawBlue,
                                lipColor = DuoMacawBlueLip,
                                cornerRadius = 20.dp
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
                                Text(
                                    text = "${report.totalZipArchivesProcessed} ZIP • ${report.totalExtractedFiles} File Diperiksa",
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = CosmicTextPure
                                )
                                IconButton(
                                    onClick = onClearScanReport,
                                    modifier = Modifier.size(26.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Close,
                                        contentDescription = "Bersihkan",
                                        tint = CosmicTextMuted,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            }

                            // 3 Compact Stat Pills
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                ScannerMetricPill(
                                    modifier = Modifier.weight(1f),
                                    count = report.uniqueCleanFilesCount.toString(),
                                    title = "Aman",
                                    subtitle = "Siap Simpan",
                                    bgColor = DuoFeatherGreenSoft,
                                    textColor = VaultObsidian
                                )
                                ScannerMetricPill(
                                    modifier = Modifier.weight(1f),
                                    count = report.duplicatesRemovedCount.toString(),
                                    title = "Duplikat",
                                    subtitle = formatFileSize(report.bytesSavedByDeduplication),
                                    bgColor = DuoBeeYellowSoft,
                                    textColor = VaultObsidian
                                )
                                ScannerMetricPill(
                                    modifier = Modifier.weight(1f),
                                    count = report.virusFlaggedCount.toString(),
                                    title = "Ancaman",
                                    subtitle = if (report.virusFlaggedCount > 0) "Waspada" else "Bersih",
                                    bgColor = if (report.virusFlaggedCount > 0) DuoCardinalRed else DuoMintSurfaceCompat(),
                                    textColor = if (report.virusFlaggedCount > 0) DuoPureWhite else VaultObsidian
                                )
                            }

                            if (report.virusFlaggedCount > 0) {
                                Surface(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(12.dp))
                                        .clickable { onOpenVirusWarningModal() }
                                        .testTag("virus_alert_banner_button"),
                                    color = DuoCardinalRedSoft,
                                    border = androidx.compose.foundation.BorderStroke(1.5.dp, DuoCardinalRed)
                                ) {
                                    Row(
                                        modifier = Modifier.padding(10.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.WarningAmber,
                                            contentDescription = null,
                                            tint = DuoCardinalRed,
                                            modifier = Modifier.size(20.dp)
                                        )
                                        Text(
                                            text = "${report.virusFlaggedCount} ancaman terdeteksi — Ketuk untuk tinjau tindakan",
                                            style = MaterialTheme.typography.labelMedium,
                                            fontWeight = FontWeight.ExtraBold,
                                            color = VaultObsidian,
                                            modifier = Modifier.weight(1f)
                                        )
                                    }
                                }
                            }

                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(46.dp)
                                    .duolingoButton(
                                        faceColor = if (report.virusFlaggedCount > 0) DuoCardinalRed else DuoFeatherGreen,
                                        lipColor = if (report.virusFlaggedCount > 0) DuoCardinalRedLip else DuoFeatherGreenLip,
                                        cornerRadius = 14.dp,
                                        lipHeight = 4.dp
                                    )
                                    .instantTouchPress(
                                        enabled = report.uniqueCleanFilesCount > 0 || report.virusFlaggedCount > 0
                                    ) {
                                        if (report.virusFlaggedCount > 0) {
                                            onOpenVirusWarningModal()
                                        } else {
                                            onCommitCleanOnlyToVault()
                                        }
                                    }
                                    .padding(bottom = 4.dp)
                                    .testTag("commit_staged_to_vault_button"),
                                contentAlignment = Alignment.Center
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.EnhancedEncryption,
                                        contentDescription = null,
                                        tint = DuoPureWhite,
                                        modifier = Modifier.size(17.dp)
                                    )
                                    Text(
                                        text = if (report.virusFlaggedCount > 0) {
                                            "Konfirmasi & Enkripsi ke Brankas #$displayVaultNum"
                                        } else {
                                            "Simpan ${report.uniqueCleanFilesCount} File Aman ke Brankas #$displayVaultNum"
                                        },
                                        style = MaterialTheme.typography.labelLarge,
                                        fontWeight = FontWeight.ExtraBold,
                                        color = DuoPureWhite
                                    )
                                }
                            }
                        }
                    }
                }

                // Selective Extraction Accordion Card
                item {
                    ZipPreExtractionAccordionCard(
                        displayVaultNum = displayVaultNum,
                        report = report,
                        expanded = isSelectiveTreeExpanded,
                        onToggleExpand = { isSelectiveTreeExpanded = !isSelectiveTreeExpanded },
                        selectedEntryIds = scannerState.selectedEntryIdsForExtraction,
                        onToggleSelectEntry = onToggleSelectEntryForExtraction,
                        onSelectAllClean = onSelectAllCleanEntriesForExtraction,
                        onSelectAll = onSelectAllEntriesForExtraction,
                        onExtractSelectedClick = onCommitSelectedEntriesToVault,
                        onQuarantineInfectedClick = onQuarantineInfectedEntriesToVaultLog
                    )
                }

                // Compact Filter Pills
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        val tabs = listOf(
                            0 to "Semua (${report.items.size})",
                            1 to "Aman (${report.uniqueCleanFilesCount})",
                            2 to "Kembar (${report.duplicatesRemovedCount})",
                            3 to "Sub-Klip (${report.subClipsDetectedCount})",
                            4 to "Virus (${report.virusFlaggedCount})"
                        )
                        tabs.forEachIndexed { chipOrder, (idx, label) ->
                            val (accent, lip, soft) = when (chipOrder) {
                                0 -> Triple(DuoMacawBlue, DuoMacawBlueLip, DuoMacawBlueSoft)
                                1 -> Triple(DuoFeatherGreen, DuoFeatherGreenLip, DuoFeatherGreenSoft)
                                2 -> Triple(DuoFoxOrange, DuoFoxOrangeLip, DuoFoxOrangeSoft)
                                3 -> Triple(DuoBeetlePurple, DuoBeetlePurpleLip, DuoBeetlePurpleSoft)
                                else -> Triple(DuoCardinalRed, DuoCardinalRedLip, DuoCardinalRedSoft)
                            }
                            Duolingo3DNavChip(
                                label = label,
                                selected = selectedFilterTab == idx,
                                onClick = { selectedFilterTab = idx },
                                accentColor = accent,
                                lipColor = lip,
                                softPastelColor = soft
                            )
                        }
                    }
                }

                // Clean Accordion Items
                items(displayedItems, key = { it.id }) { item ->
                    StagedScanAccordionCard(
                        item = item,
                        onDecideSubClipAction = onDecideSubClipAction
                    )
                }
            }
        }

        // 6. TAB 1 & TAB 2: KARANTINA & LOG SCAN (Minimalist Accordion Card)
        if (activeMainSectionTab == 1 || activeMainSectionTab == 2) {
            item {
                PerVaultQuarantineAndVirusReportCard(
                    displayVaultNum = displayVaultNum,
                    selectedHistoryTab = if (activeMainSectionTab == 1) 0 else 1,
                    onSelectHistoryTab = { subIdx -> activeMainSectionTab = if (subIdx == 0) 1 else 2 },
                    quarantineHistory = scannerState.quarantineHistory,
                    virusScanLogs = scannerState.virusScanLogs,
                    onDeleteQuarantineItem = onDeleteQuarantineHistoryEntry,
                    onClearAllHistory = onClearVaultQuarantineAndLogs
                )
            }
        }
    }

    if (scannerState.showVirusWarningModal && report != null && report.infectedItems.isNotEmpty()) {
        VirusWarningConfirmationDialog(
            activeVaultId = displayVaultNum,
            report = report,
            onCommitCleanOnly = onCommitCleanOnlyToVault,
            onCommitIncludingInfected = onCommitAllIncludingInfectedToVault,
            onCancelAndClearAll = onClearScanReport,
            onDismissDialogOnly = onDismissVirusWarningModal
        )
    }

    if (scannerState.showActiveVaultRecheckDialog && scannerState.activeVaultDuplicateWarnings.isNotEmpty()) {
        ActiveVaultDuplicateRecheckDialog(
            displayVaultNum = displayVaultNum,
            warnings = scannerState.activeVaultDuplicateWarnings,
            onDeleteDuplicateItem = onDeleteDuplicateVaultItemById,
            onDismissKeepAll = onDismissActiveVaultRecheckDialog
        )
    }
}

@Composable
private fun DuoMintSurfaceCompat(): Color = com.example.ui.theme.DuoMintSurface

@Composable
private fun ScannerMetricPill(
    modifier: Modifier = Modifier,
    count: String,
    title: String,
    subtitle: String,
    bgColor: Color,
    textColor: Color
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(14.dp))
            .background(bgColor)
            .padding(vertical = 8.dp, horizontal = 6.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = count,
            style = MaterialTheme.typography.titleLarge,
            color = textColor,
            fontWeight = FontWeight.ExtraBold
        )
        Text(
            text = title,
            style = MaterialTheme.typography.labelSmall,
            color = textColor,
            fontWeight = FontWeight.Bold,
            maxLines = 1
        )
        Text(
            text = subtitle,
            style = MaterialTheme.typography.labelSmall,
            color = textColor.copy(alpha = 0.75f),
            fontSize = 9.sp,
            maxLines = 1
        )
    }
}

/**
 * Minimalist Accordion Card for each scanned ZIP entry:
 * Collapsed by default (unless Sub-Clip decision or Virus warning requires immediate attention),
 * expanding smoothly on tap to show SHA-256, frame signature, and detailed status.
 */
@Composable
private fun StagedScanAccordionCard(
    item: StagedScanItem,
    onDecideSubClipAction: (String, Boolean) -> Unit = { _, _ -> }
) {
    var expanded by remember(item.id) {
        mutableStateOf(
            item.status == StagedFileStatus.SUB_CLIP_DETECTED ||
                item.status == StagedFileStatus.INFECTED_WARNING
        )
    }

    val (cardBg, borderColor, lipColor) = when (item.status) {
        StagedFileStatus.CLEAN_READY -> Triple(CosmicObsidian, DuoFeatherGreen, DuoFeatherGreenLip)
        StagedFileStatus.DUPLICATE_REMOVED -> Triple(CosmicObsidian, DuoBeeYellow, DuoBeeYellowLip)
        StagedFileStatus.SUB_CLIP_DETECTED -> Triple(DuoMacawBlueSoft, DuoMacawBlue, DuoMacawBlueLip)
        StagedFileStatus.INFECTED_WARNING -> Triple(DuoCardinalRedSoft, DuoCardinalRed, DuoCardinalRedLip)
    }

    val badgeText = when (item.status) {
        StagedFileStatus.CLEAN_READY -> "AMAN"
        StagedFileStatus.DUPLICATE_REMOVED -> "KEMBAR"
        StagedFileStatus.SUB_CLIP_DETECTED -> "SUB-KLIP"
        StagedFileStatus.INFECTED_WARNING -> "VIRUS"
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .duolingoCard(
                backgroundColor = cardBg,
                borderColor = borderColor,
                lipColor = lipColor,
                cornerRadius = 16.dp,
                borderWidth = 2.dp,
                lipHeight = 3.dp
            )
            .clickable { expanded = !expanded }
            .padding(bottom = 3.dp)
            .testTag("staged_scan_card_${item.fileName}")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                val thumbFile = remember(item.renderedThumbPath) { item.renderedThumbPath?.let { File(it) } }
                Box(
                    modifier = Modifier
                        .size(width = 48.dp, height = 38.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(VaultObsidian),
                    contentAlignment = Alignment.Center
                ) {
                    if (thumbFile != null && thumbFile.exists()) {
                        AsyncImage(
                            model = thumbFile,
                            contentDescription = item.fileName,
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        Icon(
                            imageVector = if (item.category == VaultCategory.VIDEO) Icons.Default.VideoFile else Icons.Default.FolderZip,
                            contentDescription = null,
                            tint = borderColor,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "${item.fileName}.${item.extension}",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.ExtraBold,
                        color = CosmicTextPure,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = "${formatFileSize(item.sizeBytes)} • ${item.sourceZipName}",
                        style = MaterialTheme.typography.bodySmall,
                        fontSize = 11.sp,
                        color = CosmicTextMuted,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Surface(
                    color = borderColor.copy(alpha = 0.16f),
                    shape = RoundedCornerShape(50),
                    border = androidx.compose.foundation.BorderStroke(1.dp, borderColor)
                ) {
                    Text(
                        text = badgeText,
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.ExtraBold,
                        color = VaultObsidian,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                    )
                }

                Icon(
                    imageVector = if (expanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                    contentDescription = null,
                    tint = CosmicTextMuted,
                    modifier = Modifier.size(18.dp)
                )
            }

            AnimatedVisibility(visible = expanded) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = "SHA-256: ${item.sha256Hex.take(20)}... • Path: ${item.entryPathInZip}",
                        fontFamily = JetBrainsMonoFontFamily,
                        fontSize = 10.sp,
                        color = CosmicTextSubtle
                    )
                    if (item.category == VaultCategory.VIDEO) {
                        Text(
                            text = "Frame ID: ${item.videoFrameSignature.take(24)} (${item.videoResolution} • ${item.durationSeconds}s)",
                            fontFamily = JetBrainsMonoFontFamily,
                            fontSize = 10.sp,
                            color = VaultMintAccent
                        )
                    }

                    if (item.status == StagedFileStatus.DUPLICATE_REMOVED && item.duplicateOfFileName != null) {
                        Text(
                            text = "Identik dengan \"${item.duplicateOfFileName}\" — salinan kembar dilewati otomatis.",
                            style = MaterialTheme.typography.bodySmall,
                            color = VaultObsidian
                        )
                    }

                    if (item.status == StagedFileStatus.SUB_CLIP_DETECTED) {
                        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                            Text(
                                text = "Sub-klip dari \"${item.subClipOfFileName}\" (${item.subClipParentDurationSec / 60}m ${item.subClipParentDurationSec % 60}s).",
                                style = MaterialTheme.typography.bodySmall,
                                fontWeight = FontWeight.SemiBold,
                                color = VaultObsidian
                            )
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Button(
                                    onClick = { onDecideSubClipAction(item.id, true) },
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(36.dp)
                                        .testTag("subclip_keep_button_${item.fileName}"),
                                    shape = RoundedCornerShape(10.dp),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = DuoFeatherGreen,
                                        contentColor = Color.White
                                    ),
                                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp)
                                ) {
                                    Text("Simpan", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                }
                                Button(
                                    onClick = { onDecideSubClipAction(item.id, false) },
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(36.dp)
                                        .testTag("subclip_delete_button_${item.fileName}"),
                                    shape = RoundedCornerShape(10.dp),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = VaultCoral,
                                        contentColor = Color.White
                                    ),
                                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp)
                                ) {
                                    Text("Hapus Sub-Klip", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }

                    if (item.status == StagedFileStatus.INFECTED_WARNING) {
                        Text(
                            text = "${item.virusThreatName ?: "Heuristic.Malware"}: ${item.virusThreatDetail ?: "Kode mencurigakan terdeteksi."}",
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = FontWeight.Bold,
                            color = VaultCoral
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ZipPreExtractionAccordionCard(
    displayVaultNum: Int,
    report: BatchZipScanReport,
    expanded: Boolean,
    onToggleExpand: () -> Unit,
    selectedEntryIds: Set<String>,
    onToggleSelectEntry: (String) -> Unit,
    onSelectAllClean: () -> Unit,
    onSelectAll: () -> Unit,
    onExtractSelectedClick: () -> Unit,
    onQuarantineInfectedClick: () -> Unit
) {
    val effectiveSelectedIds = if (selectedEntryIds.isEmpty()) {
        report.readyCleanItems.map { it.id }.toSet()
    } else {
        selectedEntryIds
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .duolingoCard(
                backgroundColor = CosmicObsidian,
                borderColor = StarlightCyan,
                lipColor = DuoMacawBlueLip,
                cornerRadius = 18.dp,
                borderWidth = 2.dp,
                lipHeight = 3.dp
            )
            .padding(bottom = 3.dp)
            .testTag("zip_pre_extraction_preview_card")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onToggleExpand() },
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(
                        imageVector = Icons.Default.FolderZip,
                        contentDescription = null,
                        tint = StarlightCyan,
                        modifier = Modifier.size(18.dp)
                    )
                    Column {
                        Text(
                            text = "Pilih File Dalam ZIP (${effectiveSelectedIds.size}/${report.items.size})",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.ExtraBold,
                            color = CosmicTextPure
                        )
                        Text(
                            text = "Ketuk untuk memilih item spesifik sebelum diekstrak",
                            style = MaterialTheme.typography.labelSmall,
                            color = CosmicTextMuted
                        )
                    }
                }
                Icon(
                    imageVector = if (expanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                    contentDescription = null,
                    tint = CosmicTextMuted
                )
            }

            AnimatedVisibility(visible = expanded) {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        FilterChip(
                            selected = false,
                            onClick = onSelectAllClean,
                            label = { Text("Aman Saja (${report.uniqueCleanFilesCount})", fontWeight = FontWeight.Bold) }
                        )
                        FilterChip(
                            selected = false,
                            onClick = onSelectAll,
                            label = { Text("Semua (${report.items.size})", fontWeight = FontWeight.Bold) }
                        )
                        if (report.virusFlaggedCount > 0) {
                            FilterChip(
                                selected = false,
                                onClick = onQuarantineInfectedClick,
                                label = {
                                    Text(
                                        "Karantina (${report.virusFlaggedCount})",
                                        fontWeight = FontWeight.Bold,
                                        color = VaultCoral
                                    )
                                }
                            )
                        }
                    }

                    report.items.forEach { entry ->
                        val isSelected = entry.id in effectiveSelectedIds
                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .clickable { onToggleSelectEntry(entry.id) }
                                .testTag("zip_preview_entry_${entry.fileName}"),
                            color = if (isSelected) StarlightCyan.copy(alpha = 0.12f) else VaultCream.copy(alpha = 0.5f),
                            shape = RoundedCornerShape(12.dp),
                            border = androidx.compose.foundation.BorderStroke(
                                1.dp,
                                if (isSelected) StarlightCyan else CosmicBorderHairline
                            )
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 10.dp, vertical = 8.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.CheckCircle,
                                    contentDescription = null,
                                    tint = if (isSelected) StarlightCyan else CosmicTextMuted.copy(alpha = 0.35f),
                                    modifier = Modifier.size(18.dp)
                                )
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = entry.entryPathInZip,
                                        fontFamily = JetBrainsMonoFontFamily,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = CosmicTextPure,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                    Text(
                                        text = "${formatFileSize(entry.compressedSizeBytes)} → ${formatFileSize(entry.sizeBytes)}",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = CosmicTextMuted
                                    )
                                }
                            }
                        }
                    }

                    Button(
                        onClick = onExtractSelectedClick,
                        enabled = effectiveSelectedIds.isNotEmpty(),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(44.dp)
                            .testTag("extract_selected_zip_entries_button"),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = DuoFeatherGreen,
                            contentColor = Color.White
                        )
                    ) {
                        Icon(Icons.Default.EnhancedEncryption, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Ekstrak ${effectiveSelectedIds.size} File ke Brankas #$displayVaultNum",
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
private fun ActiveVaultDuplicateRecheckDialog(
    displayVaultNum: Int,
    warnings: List<com.example.data.VaultDuplicateWarning>,
    onDeleteDuplicateItem: (Int) -> Unit,
    onDismissKeepAll: () -> Unit
) {
    Dialog(
        onDismissRequest = onDismissKeepAll,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 18.dp)
                .widthIn(max = 460.dp)
                .testTag("active_vault_recheck_dialog"),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = VaultPureWhite),
            border = androidx.compose.foundation.BorderStroke(2.dp, StarlightCyan)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(18.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    text = "Cek Duplikat Brankas #$displayVaultNum (${warnings.size})",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.ExtraBold,
                    color = VaultObsidian
                )
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    warnings.take(4).forEach { w ->
                        Surface(
                            color = VaultCream.copy(alpha = 0.65f),
                            shape = RoundedCornerShape(12.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, StarlightCyan.copy(alpha = 0.5f)),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(10.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = if (w.isSubClip) "SUB-KLIP: ${w.itemName}" else "DOBEL: ${w.itemName}",
                                        style = MaterialTheme.typography.labelLarge,
                                        fontWeight = FontWeight.ExtraBold,
                                        color = VaultObsidian
                                    )
                                    Text(
                                        text = w.detailLabel,
                                        style = MaterialTheme.typography.bodySmall,
                                        fontSize = 11.sp,
                                        color = VaultTextMuted
                                    )
                                }
                                Button(
                                    onClick = { onDeleteDuplicateItem(w.itemId) },
                                    shape = RoundedCornerShape(10.dp),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = VaultCoral,
                                        contentColor = Color.White
                                    ),
                                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                                    modifier = Modifier.height(34.dp)
                                ) {
                                    Text("Hapus", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }

                Button(
                    onClick = onDismissKeepAll,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(44.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = DuoFeatherGreen,
                        contentColor = Color.White
                    )
                ) {
                    Text("Tutup", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun VirusWarningConfirmationDialog(
    activeVaultId: Int,
    report: BatchZipScanReport,
    onCommitCleanOnly: () -> Unit,
    onCommitIncludingInfected: () -> Unit,
    onCancelAndClearAll: () -> Unit,
    onDismissDialogOnly: () -> Unit
) {
    val infectedList = report.infectedItems

    Dialog(
        onDismissRequest = onDismissDialogOnly,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 18.dp)
                .widthIn(max = 460.dp)
                .testTag("virus_warning_confirmation_dialog"),
            shape = RoundedCornerShape(26.dp),
            colors = CardDefaults.cardColors(containerColor = VaultObsidian),
            border = androidx.compose.foundation.BorderStroke(2.dp, VaultCoral)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(18.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    text = "${infectedList.size} Ancaman Terdeteksi",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.ExtraBold,
                    color = VaultPureWhite,
                    textAlign = TextAlign.Center
                )

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    infectedList.take(3).forEach { infected ->
                        Surface(
                            color = VaultCharcoal,
                            shape = RoundedCornerShape(12.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, VaultCoral.copy(alpha = 0.6f)),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(10.dp)) {
                                Text(
                                    text = "${infected.fileName}.${infected.extension}",
                                    style = MaterialTheme.typography.titleSmall,
                                    color = VaultCoral,
                                    fontWeight = FontWeight.Bold,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Text(
                                    text = "${infected.virusThreatName} • ${infected.sourceZipName}",
                                    fontFamily = JetBrainsMonoFontFamily,
                                    fontSize = 10.sp,
                                    color = VaultAmber
                                )
                            }
                        }
                    }
                }

                Button(
                    onClick = onCommitCleanOnly,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(46.dp)
                        .testTag("virus_dialog_clean_only_button"),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = VaultLime,
                        contentColor = VaultObsidian
                    )
                ) {
                    Icon(Icons.Default.Security, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Simpan ${report.uniqueCleanFilesCount} File Aman Saja",
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Bold
                    )
                }

                Button(
                    onClick = onCommitIncludingInfected,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(46.dp)
                        .testTag("virus_dialog_keep_infected_button"),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = VaultCoral,
                        contentColor = VaultPureWhite
                    )
                ) {
                    Icon(Icons.Default.Lock, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Enkripsi Semua ke Brankas (.NYX)",
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Bold
                    )
                }

                OutlinedButton(
                    onClick = onCancelAndClearAll,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(42.dp)
                        .testTag("virus_dialog_cancel_all_button"),
                    shape = RoundedCornerShape(14.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, VaultPureWhite.copy(alpha = 0.4f))
                ) {
                    Text(
                        text = "Batalkan Semua",
                        style = MaterialTheme.typography.labelLarge,
                        color = VaultPureWhite
                    )
                }
            }
        }
    }
}

@Composable
private fun PerVaultQuarantineAndVirusReportCard(
    displayVaultNum: Int,
    selectedHistoryTab: Int,
    onSelectHistoryTab: (Int) -> Unit,
    quarantineHistory: List<VaultQuarantineEntry>,
    virusScanLogs: List<VaultVirusScanLogEntry>,
    onDeleteQuarantineItem: (String) -> Unit,
    onClearAllHistory: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .duolingoCard(
                backgroundColor = CosmicObsidian,
                borderColor = DuoSwanBorder,
                lipColor = DuoHareLip,
                cornerRadius = 18.dp
            )
            .padding(bottom = 4.dp)
            .testTag("per_vault_quarantine_and_virus_report_card")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (selectedHistoryTab == 0) {
                        "Karantina Brankas #$displayVaultNum (${quarantineHistory.size})"
                    } else {
                        "Log Pemindaian Brankas #$displayVaultNum (${virusScanLogs.size})"
                    },
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.ExtraBold,
                    color = CosmicTextPure
                )

                if (quarantineHistory.isNotEmpty() || virusScanLogs.isNotEmpty()) {
                    IconButton(
                        onClick = onClearAllHistory,
                        modifier = Modifier.size(28.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.DeleteSweep,
                            contentDescription = "Bersihkan Riwayat",
                            tint = VaultCoral,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }

            if (selectedHistoryTab == 0) {
                if (quarantineHistory.isEmpty()) {
                    Text(
                        text = "Karantina Brankas #$displayVaultNum bersih (0 ancaman).",
                        style = MaterialTheme.typography.bodySmall,
                        color = CosmicTextMuted
                    )
                } else {
                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        quarantineHistory.forEach { qItem ->
                            Surface(
                                color = DuoCardinalRedSoft.copy(alpha = 0.55f),
                                shape = RoundedCornerShape(12.dp),
                                border = androidx.compose.foundation.BorderStroke(1.dp, VaultCoral.copy(alpha = 0.45f)),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(10.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.BugReport,
                                        contentDescription = null,
                                        tint = VaultCoral,
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = "${qItem.fileName}.${qItem.extension}",
                                            style = MaterialTheme.typography.labelLarge,
                                            fontWeight = FontWeight.ExtraBold,
                                            color = VaultObsidian
                                        )
                                        Text(
                                            text = "${qItem.threatName} • ${qItem.sourceZipName}",
                                            fontFamily = JetBrainsMonoFontFamily,
                                            fontSize = 10.sp,
                                            color = VaultCoral
                                        )
                                    }
                                    IconButton(
                                        onClick = { onDeleteQuarantineItem(qItem.id) },
                                        modifier = Modifier.size(26.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Close,
                                            contentDescription = "Hapus",
                                            tint = VaultCoral,
                                            modifier = Modifier.size(16.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            } else {
                if (virusScanLogs.isEmpty()) {
                    Text(
                        text = "Belum ada log pemindaian ZIP di Brankas #$displayVaultNum.",
                        style = MaterialTheme.typography.bodySmall,
                        color = CosmicTextMuted
                    )
                } else {
                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        virusScanLogs.forEach { log ->
                            Surface(
                                color = VaultCream.copy(alpha = 0.65f),
                                shape = RoundedCornerShape(12.dp),
                                border = androidx.compose.foundation.BorderStroke(1.dp, CosmicBorderHairline),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(10.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = log.archiveSummary,
                                            style = MaterialTheme.typography.labelLarge,
                                            fontWeight = FontWeight.Bold,
                                            color = CosmicTextPure
                                        )
                                        Text(
                                            text = "Aman: ${log.cleanExtractedCount} • Duplikat: ${log.duplicatesRemovedCount} • Virus: ${log.threatsQuarantinedCount}",
                                            style = MaterialTheme.typography.bodySmall,
                                            fontSize = 11.sp,
                                            color = CosmicTextMuted
                                        )
                                    }
                                    Surface(
                                        color = if (log.threatsQuarantinedCount > 0) VaultCoral else DuoFeatherGreen,
                                        shape = RoundedCornerShape(50)
                                    ) {
                                        Text(
                                            text = log.verdictBadge,
                                            style = MaterialTheme.typography.labelSmall,
                                            fontWeight = FontWeight.Bold,
                                            color = Color.White,
                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
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
