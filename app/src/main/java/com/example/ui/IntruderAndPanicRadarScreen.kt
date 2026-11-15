package com.example.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.filled.BugReport
import androidx.compose.material.icons.filled.DeleteSweep
import androidx.compose.material.icons.filled.EdgesensorHigh
import androidx.compose.material.icons.filled.GppMaybe
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Radar
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Vibration
import androidx.compose.material.icons.filled.WarningAmber
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.CosmicBorderHairline
import com.example.ui.theme.CosmicGlass
import com.example.ui.theme.CosmicTextPure
import com.example.ui.theme.CosmicTextSubtle
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
import com.example.ui.theme.PastelRose
import com.example.ui.theme.SolarGold
import com.example.ui.theme.StarlightCyan
import com.example.ui.theme.VaultCoral
import com.example.ui.theme.VaultCream
import com.example.ui.theme.VaultLime
import com.example.ui.theme.VaultObsidian
import com.example.ui.theme.VaultPureWhite
import com.example.ui.theme.VaultTextMuted
import com.example.ui.theme.duolingoButton
import com.example.ui.theme.duolingoCard
import java.util.UUID

enum class PanicMotionPreset(
    val label: String,
    val description: String,
    val thresholdDeltaMps2: Float,
    val requiredJoltsInWindow: Int
) {
    EXTREME_3X_VIDEO_SAFE(
        label = "3x Hentakan Ekstrem (Aman Nonton Video)",
        description = "Goyangan biasa saat nonton/rebahan diabaikan (<25 m/s²). Wajib 3x sentakan keras beruntun dalam 900ms.",
        thresholdDeltaMps2 = 15.5f, // ~25.3 m/s² total (>2.5G)
        requiredJoltsInWindow = 3
    ),
    ULTRA_HEAVY_4X_VEHICLE(
        label = "4x Hentakan Super Keras (Anti Guncangan Mobil)",
        description = "Ambang batas ekstra tinggi (>29 m/s²) dengan 4x sentakan cepat untuk perjalanan di kendaraan.",
        thresholdDeltaMps2 = 19.2f, // ~29.0 m/s² total (~3.0G)
        requiredJoltsInWindow = 4
    )
}

data class IntruderTrapLogEntry(
    val id: String = UUID.randomUUID().toString(),
    val timestampLabel: String,
    val triggerType: String,
    val wrongPinLayer1: String,
    val wrongPinLayer2: String,
    val wrongPinLayer3: String,
    val activitiesInDecoy: List<String> = emptyList()
)

data class IntruderAndPanicUiState(
    val isPanicMotionEnabled: Boolean = true,
    val selectedPreset: PanicMotionPreset = PanicMotionPreset.EXTREME_3X_VIDEO_SAFE,
    val liveDeltaMps2: Float = 0.4f,
    val peakDeltaMps2: Float = 1.8f,
    val currentJoltStreak: Int = 0,
    val intruderLogs: List<IntruderTrapLogEntry> = listOf(
        IntruderTrapLogEntry(
            timestampLabel = "Contoh Simulasi Awal • Brankas Palsu",
            triggerType = "PIN SALAH 3 LAPIS",
            wrongPinLayer1 = "123456",
            wrongPinLayer2 = "654321",
            wrongPinLayer3 = "000000",
            activitiesInDecoy = listOf(
                "Diarahkan diam-diam ke BRANKAS #1 (Palsu)",
                "Melihat daftar file umpan (Jadwal_Kegiatan_Mingguan.txt)"
            )
        )
    )
)

@Composable
fun IntruderAndPanicRadarScreen(
    state: IntruderAndPanicUiState,
    onTogglePanicMotionEnabled: (Boolean) -> Unit,
    onSelectPanicPreset: (PanicMotionPreset) -> Unit,
    onTestExtremePanicSwitchNow: () -> Unit,
    onSimulateIntruderEntry: () -> Unit,
    onDeleteIntruderLog: (String) -> Unit,
    onClearAllIntruderLogs: () -> Unit
) {
    val normalizedProgress = (state.liveDeltaMps2 / state.selectedPreset.thresholdDeltaMps2).coerceIn(0f, 1f)

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .testTag("intruder_and_panic_radar_screen"),
        contentPadding = PaddingValues(start = 18.dp, end = 18.dp, top = 12.dp, bottom = 120.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 1. Header Banner (3D Duolingo Pastel Card)
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .duolingoCard(
                        backgroundColor = DuoFoxOrangeSoft,
                        borderColor = DuoFoxOrange,
                        lipColor = DuoFoxOrangeLip,
                        cornerRadius = 24.dp,
                        borderWidth = 2.dp,
                        lipHeight = 5.dp
                    )
                    .padding(bottom = 5.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    NyxMascotAvatar(
                        mood = MascotMood.GUARD_ALERT,
                        persona = MascotPersona.SHADOW_GUARDIAN,
                        size = 48.dp
                    )
                    Column(modifier = Modifier.weight(1f)) {
                        Box(
                            modifier = Modifier
                                .duolingoButton(
                                    faceColor = DuoFoxOrange,
                                    lipColor = DuoFoxOrangeLip,
                                    cornerRadius = 10.dp,
                                    lipHeight = 3.dp
                                )
                                .padding(bottom = 3.dp)
                                .padding(horizontal = 8.dp, vertical = 3.dp)
                        ) {
                            Text(
                                text = "PUSAT RADAR PENYUSUP & DARURAT 3D",
                                style = MaterialTheme.typography.labelSmall,
                                color = Color.White,
                                fontWeight = FontWeight.ExtraBold
                            )
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Jebakan PIN Salah & Guncangan Ekstrem",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.ExtraBold,
                            color = CosmicTextPure
                        )
                        Text(
                            text = "Pantau siapa yang masuk ke Brankas Palsu & atur sensor guncangan ekstrem anti-salah-pencet.",
                            style = MaterialTheme.typography.bodySmall,
                            color = CosmicTextSubtle
                        )
                    }
                }
            }
        }

        // 1B. Busy 3D Duolingo Telemetry Ticker & G-Force Radar Bit-Matrix Gauge
        item {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                DuolingoLiveTelemetryTicker(
                    screenTitle = "TELEMETRI RADAR G-FORCE & JEBAKAN PENYUSUP 3D",
                    modeBadge = if (state.isPanicMotionEnabled) "RADAR ARMED" else "STANDBY",
                    metrics = listOf(
                        TelemetryMetricPill(
                            label = "DELTA G",
                            value = "${"%.1f".format(state.liveDeltaMps2)} m/s²",
                            accentColor = DuoFeatherGreen,
                            lipColor = DuoFeatherGreenLip,
                            softColor = DuoFeatherGreenSoft
                        ),
                        TelemetryMetricPill(
                            label = "PUNCAK G",
                            value = "${"%.1f".format(state.peakDeltaMps2)} m/s²",
                            accentColor = DuoFoxOrange,
                            lipColor = DuoFoxOrangeLip,
                            softColor = DuoFoxOrangeSoft
                        ),
                        TelemetryMetricPill(
                            label = "HENTAKAN",
                            value = "${state.currentJoltStreak}/${state.selectedPreset.requiredJoltsInWindow}X",
                            accentColor = DuoMacawBlue,
                            lipColor = DuoMacawBlueLip,
                            softColor = DuoMacawBlueSoft
                        ),
                        TelemetryMetricPill(
                            label = "TERJEBAK",
                            value = "${state.intruderLogs.size} PENYUSUP",
                            accentColor = DuoCardinalRed,
                            lipColor = DuoCardinalRedLip,
                            softColor = DuoCardinalRedSoft
                        )
                    ),
                    accentColor = DuoFoxOrange,
                    lipColor = DuoFoxOrangeLip,
                    softBgColor = DuoFoxOrangeSoft
                )

                DuolingoBitMatrixGaugeCard(
                    title = "Accelerometer G-Force & Decoy Trap Matrix",
                    subtitle = "Ambang Batas: >${state.selectedPreset.thresholdDeltaMps2.toInt() + 10} m/s² (${state.selectedPreset.requiredJoltsInWindow}x Hentakan dalam 900ms)",
                    activeBitLoadPercent = ((normalizedProgress * 75f).toInt() + 20).coerceIn(20, 100),
                    primaryColor = DuoFoxOrange,
                    lipColor = DuoFoxOrangeLip,
                    softSurfaceColor = DuoFoxOrangeSoft,
                    badgeLabels = listOf(
                        "ANTI-VIDEO-SHAKE",
                        "900MS WINDOW",
                        "SILENT DECOY #1",
                        "PIN TRAP LOG"
                    )
                )
            }
        }

        // 2. Modul Guncangan Ekstrem Cerdas (Anti-Video-Shake -> Langsung Pindah ke Brankas Palsu)
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .duolingoCard(
                        backgroundColor = DuoPureWhite,
                        borderColor = DuoSwanBorder,
                        lipColor = DuoHareLip,
                        cornerRadius = 26.dp,
                        borderWidth = 2.dp,
                        lipHeight = 5.dp
                    )
                    .padding(bottom = 5.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
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
                            Surface(
                                shape = CircleShape,
                                color = DuoMacawBlue.copy(alpha = 0.16f),
                                modifier = Modifier.size(40.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(
                                        imageVector = Icons.Default.EdgesensorHigh,
                                        contentDescription = null,
                                        tint = DuoMacawBlue
                                    )
                                }
                            }
                            Column {
                                Text(
                                    text = "Sensor Guncangan Ekstrem → Brankas Palsu",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = CosmicTextPure
                                )
                                Text(
                                    text = "Aman saat nonton video/rebahan. Hanya guncangan tidak wajar yang memicu.",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = CosmicTextSubtle
                                )
                            }
                        }

                        Switch(
                            checked = state.isPanicMotionEnabled,
                            onCheckedChange = onTogglePanicMotionEnabled,
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = Color.White,
                                checkedTrackColor = DuoFeatherGreen
                            ),
                            modifier = Modifier.testTag("panic_motion_switch_toggle")
                        )
                    }

                    // Penjelasan Anti-Bocor Saat Nonton
                    Surface(
                        color = PastelMint.copy(alpha = 0.25f),
                        shape = RoundedCornerShape(16.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, DuoFeatherGreen.copy(alpha = 0.5f)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier.padding(12.dp),
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text(
                                text = "Proteksi Anti-Salah-Pencet Saat Nonton Video:",
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.ExtraBold,
                                color = VaultObsidian
                            )
                            Text(
                                text = "• Goyangan biasa (1x–2x gerak atau miring saat nonton video) 100% DIABAIKAN.\n" +
                                    "• Hanya aktif jika HP diguncang keras & tidak wajar sebanyak ${state.selectedPreset.requiredJoltsInWindow}x beruntun (> ${state.selectedPreset.thresholdDeltaMps2.toInt() + 10} m/s²).\n" +
                                    "• Saat terpicu: Tidak kembali ke layar PIN, melainkan LANGSUNG bertukar ke BRANKAS #1 (Palsu) seketika!",
                                style = MaterialTheme.typography.bodySmall,
                                color = VaultObsidian
                            )
                        }
                    }

                    // Pilihan Preset Sensitivitas Ekstrem (3D Duolingo Pastel Chips)
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        PanicMotionPreset.entries.forEachIndexed { idx, preset ->
                            val selected = state.selectedPreset == preset
                            val (accent, lip, soft) = if (idx == 0) {
                                Triple(DuoMacawBlue, DuoMacawBlueLip, DuoMacawBlueSoft)
                            } else {
                                Triple(DuoBeetlePurple, DuoBeetlePurpleLip, DuoBeetlePurpleSoft)
                            }
                            Duolingo3DNavChip(
                                label = preset.label,
                                selected = selected,
                                onClick = { onSelectPanicPreset(preset) },
                                accentColor = accent,
                                lipColor = lip,
                                softPastelColor = soft
                            )
                        }
                    }

                    // Indikator Telemetri Sensor Real-Time
                    Surface(
                        color = VaultCream.copy(alpha = 0.65f),
                        shape = RoundedCornerShape(16.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, CosmicBorderHairline),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier.padding(12.dp),
                            verticalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "Getaran Saat Ini: ${"%.1f".format(state.liveDeltaMps2)} m/s² (Puncak: ${"%.1f".format(state.peakDeltaMps2)} m/s²)",
                                    fontFamily = JetBrainsMonoFontFamily,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = VaultObsidian
                                )
                                Text(
                                    text = "Hentakan: ${state.currentJoltStreak}/${state.selectedPreset.requiredJoltsInWindow}",
                                    fontFamily = JetBrainsMonoFontFamily,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = if (state.currentJoltStreak > 0) VaultCoral else DuoFeatherGreenLip
                                )
                            }
                            LinearProgressIndicator(
                                progress = { normalizedProgress },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(8.dp)
                                    .clip(RoundedCornerShape(50)),
                                color = if (normalizedProgress >= 0.85f) VaultCoral else DuoFeatherGreen,
                                trackColor = DuoSwanBorder
                            )
                            Text(
                                text = if (normalizedProgress < 0.85f) {
                                    "Status: Zona Aman Nonton / Harian (Tidak memicu perpindahan)"
                                } else {
                                    "Status: Hentakan Ekstrem Terdeteksi!"
                                },
                                style = MaterialTheme.typography.labelSmall,
                                color = VaultTextMuted
                            )
                        }
                    }

                    // Tombol Uji Guncangan Ekstrem -> Langsung Ganti ke Brankas Palsu
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .duolingoButton(
                                faceColor = DuoCardinalRed,
                                lipColor = DuoCardinalRedLip,
                                cornerRadius = 16.dp,
                                lipHeight = 4.dp
                            )
                            .instantTouchPress { onTestExtremePanicSwitchNow() }
                            .padding(bottom = 4.dp)
                            .testTag("test_extreme_panic_switch_button"),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(Icons.Default.Vibration, contentDescription = null, tint = Color.White, modifier = Modifier.size(18.dp))
                            Text(
                                text = "Uji Guncangan Ekstrem (Pindah ke Brankas Palsu)",
                                style = MaterialTheme.typography.labelLarge,
                                fontWeight = FontWeight.ExtraBold,
                                color = Color.White
                            )
                        }
                    }
                }
            }
        }

        // 3. Modul Jebakan Penyusup (Rekam Waktu, PIN 3-Lapis Salah & Aktivitas di Brankas Palsu)
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .duolingoCard(
                        backgroundColor = DuoBeetlePurpleSoft,
                        borderColor = DuoBeetlePurple,
                        lipColor = DuoBeetlePurpleLip,
                        cornerRadius = 26.dp,
                        borderWidth = 2.dp,
                        lipHeight = 5.dp
                    )
                    .padding(bottom = 5.dp)
                    .testTag("intruder_trap_logs_card")
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
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
                                    .size(40.dp)
                                    .duolingoButton(
                                        faceColor = DuoCardinalRed,
                                        lipColor = DuoCardinalRedLip,
                                        cornerRadius = 14.dp,
                                        lipHeight = 3.dp
                                    )
                                    .padding(bottom = 3.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.GppMaybe,
                                    contentDescription = null,
                                    tint = Color.White
                                )
                            }
                            Column {
                                Text(
                                    text = "Jebakan Penyusup (${state.intruderLogs.size} Rekaman)",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = VaultObsidian
                                )
                                Text(
                                    text = "Merekam waktu & kombinasi PIN salah yang masuk ke Brankas Palsu",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = CosmicTextSubtle
                                )
                            }
                        }

                        if (state.intruderLogs.isNotEmpty()) {
                            IconButton(onClick = onClearAllIntruderLogs) {
                                Icon(
                                    imageVector = Icons.Default.DeleteSweep,
                                    contentDescription = "Bersihkan Semua Log Penyusup",
                                    tint = VaultCoral
                                )
                            }
                        }
                    }

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(46.dp)
                            .duolingoButton(
                                faceColor = DuoBeetlePurple,
                                lipColor = DuoBeetlePurpleLip,
                                cornerRadius = 15.dp,
                                lipHeight = 4.dp
                            )
                            .instantTouchPress { onSimulateIntruderEntry() }
                            .padding(bottom = 4.dp)
                            .testTag("simulate_intruder_log_button"),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.BugReport,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(16.dp)
                            )
                            Text(
                                text = "Simulasikan Penyusup Masuk dengan PIN Salah",
                                style = MaterialTheme.typography.labelLarge,
                                fontWeight = FontWeight.ExtraBold,
                                color = Color.White
                            )
                        }
                    }

                    if (state.intruderLogs.isEmpty()) {
                        Surface(
                            color = VaultCream.copy(alpha = 0.65f),
                            shape = RoundedCornerShape(16.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(
                                modifier = Modifier.padding(18.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = "Belum Ada Penyusup yang Terjebak",
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = VaultObsidian
                                )
                                Text(
                                    text = "Setiap kali seseorang mengetik PIN 3-lapis yang salah dan masuk ke Brankas Palsu, PIN yang mereka coba dan waktu kejadiannya akan tercatat di sini.",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = VaultTextMuted,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    } else {
                        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                            state.intruderLogs.forEach { log ->
                                Surface(
                                    color = PastelRose.copy(alpha = 0.10f),
                                    shape = RoundedCornerShape(18.dp),
                                    border = androidx.compose.foundation.BorderStroke(1.5.dp, VaultCoral.copy(alpha = 0.45f)),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Column(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(14.dp),
                                        verticalArrangement = Arrangement.spacedBy(6.dp)
                                    ) {
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Surface(
                                                color = VaultCoral,
                                                shape = RoundedCornerShape(50)
                                            ) {
                                                Text(
                                                    text = log.triggerType,
                                                    style = MaterialTheme.typography.labelSmall,
                                                    fontWeight = FontWeight.ExtraBold,
                                                    color = Color.White,
                                                    modifier = Modifier.padding(horizontal = 9.dp, vertical = 3.dp)
                                                )
                                            }
                                            Text(
                                                text = log.timestampLabel,
                                                fontFamily = JetBrainsMonoFontFamily,
                                                fontSize = 11.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = VaultObsidian
                                            )
                                        }

                                        Surface(
                                            color = VaultPureWhite,
                                            shape = RoundedCornerShape(12.dp),
                                            border = androidx.compose.foundation.BorderStroke(1.dp, CosmicBorderHairline),
                                            modifier = Modifier.fillMaxWidth()
                                        ) {
                                            Column(
                                                modifier = Modifier.padding(10.dp),
                                                verticalArrangement = Arrangement.spacedBy(2.dp)
                                            ) {
                                                Text(
                                                    text = "PIN Salah yang Diketik Penyusup:",
                                                    style = MaterialTheme.typography.labelSmall,
                                                    color = VaultTextMuted
                                                )
                                                Text(
                                                    text = "Lapis 1: [${log.wrongPinLayer1}] • Lapis 2: [${log.wrongPinLayer2}] • Lapis 3: [${log.wrongPinLayer3}]",
                                                    fontFamily = JetBrainsMonoFontFamily,
                                                    fontSize = 12.sp,
                                                    fontWeight = FontWeight.ExtraBold,
                                                    color = VaultCoral
                                                )
                                            }
                                        }

                                        if (log.activitiesInDecoy.isNotEmpty()) {
                                            Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                                                Text(
                                                    text = "Jejak Aktivitas di Brankas Palsu:",
                                                    style = MaterialTheme.typography.labelSmall,
                                                    fontWeight = FontWeight.Bold,
                                                    color = VaultObsidian
                                                )
                                                log.activitiesInDecoy.forEach { act ->
                                                    Text(
                                                        text = "• $act",
                                                        style = MaterialTheme.typography.bodySmall,
                                                        color = VaultObsidian
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
        }
    }
}
