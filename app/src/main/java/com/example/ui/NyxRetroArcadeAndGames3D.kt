package com.example.ui

import android.content.SharedPreferences
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Casino
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
import com.example.ui.theme.VaultObsidian
import com.example.ui.theme.VaultTextMuted
import com.example.ui.theme.duolingoButton
import com.example.ui.theme.duolingoCard
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.security.SecureRandom
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.roundToInt
import kotlin.math.sin

// =========================================================================
// ARCADE RETRO & MINI-GAMES RAHASIA NYX 3D + GACHA MASKOT
// Seluruh aset digambar menggunakan Custom 3D Duolingo Canvas (TANPA EMOJI &
// TANPA SVG DATAR).
// =========================================================================

data class NyxSecretMascotSpec(
    val id: Int,
    val name: String,
    val rarityTier: String, // "SSR LEGEND", "SR EPIC", "R RARE"
    val bonusLabel: String,
    val primaryColor: Color,
    val lipColor: Color,
    val softColor: Color,
    val crestColor: Color
)

val NYX_MASCOT_CATALOG = listOf(
    NyxSecretMascotSpec(0, "Duo Cyber Emerald", "SSR LEGEND", "Bonus Skor +25% & Koin +20%", DuoFeatherGreen, DuoFeatherGreenLip, DuoFeatherGreenSoft, DuoBeeYellow),
    NyxSecretMascotSpec(1, "Nyx Void Empress", "SSR LEGEND", "Bonus Gacha Luck & Skor +30%", DuoBeetlePurple, DuoBeetlePurpleLip, DuoBeetlePurpleSoft, DuoBeeYellow),
    NyxSecretMascotSpec(2, "Solaria Golden Owl", "SSR LEGEND", "Bonus Koin Kosmik +35%", DuoBeeYellow, DuoBeeYellowLip, DuoBeeYellowSoft, DuoCardinalRed),
    NyxSecretMascotSpec(3, "Macaw Cryo Knight", "SR EPIC", "Perisai Es & Skor +18%", DuoMacawBlue, DuoMacawBlueLip, DuoMacawBlueSoft, DuoPureWhite),
    NyxSecretMascotSpec(4, "Zari Supernova Fox", "SR EPIC", "Ledakan Kombo +20%", DuoFoxOrange, DuoFoxOrangeLip, DuoFoxOrangeSoft, DuoBeeYellow),
    NyxSecretMascotSpec(5, "Aegis Cardinal Titan", "SR EPIC", "Pertahanan Inti +22%", DuoCardinalRed, DuoCardinalRedLip, DuoCardinalRedSoft, DuoBeeYellow),
    NyxSecretMascotSpec(6, "Falstaff Star Bear", "R RARE", "Bonus Koin Awal +12%", Color(0xFF8B5A2B), Color(0xFF5C3A1B), DuoBeeYellowSoft, DuoFoxOrange),
    NyxSecretMascotSpec(7, "Lily Nebula Watcher", "R RARE", "Bonus Tarot & Mood +15%", DuoBeetlePurple, DuoBeetlePurpleLip, DuoBeetlePurpleSoft, DuoMacawBlue)
)

private data class Brick3DCell(
    val col: Int,
    val row: Int,
    val hp: Int,
    val maxHp: Int,
    val faceColor: Color,
    val lipColor: Color
)

private data class FallingCosmicEntity(
    val id: Long,
    val laneX: Float, // 0.1f..0.9f
    val yNorm: Float, // 0f..1f
    val speed: Float,
    val kind: Int // 0 = Meteor (Bahaya), 1 = Bintang Emas (+Skor/+Koin), 2 = Kristal Perisai
)

@Composable
fun NyxRetroArcadeAndMascotHubSection(
    prefs: SharedPreferences,
    cosmicCoins: Int,
    onUpdateCoins: (Int) -> Unit,
    onUnlockMascot: (Int) -> Unit,
    onPostBanner: (String) -> Unit
) {
    val secureRandom = remember { SecureRandom() }
    val coroutineScope = rememberCoroutineScope()

    // 0 = Brick Breaker 3D, 1 = Meteor Dodge & Star Catcher 3D, 2 = Cyber-Rune Memory 3D, 3 = Kapsul Gacha Maskot 3D
    var activeArcadeMode by remember { mutableIntStateOf(0) }

    var equippedMascotId by remember {
        mutableIntStateOf(prefs.getInt("nyx_equipped_mascot_id", 0))
    }
    var mascotLevelsCsv by remember {
        mutableStateOf(prefs.getString("nyx_mascot_levels_csv", "0:1") ?: "0:1")
    }

    fun getMascotLevel(mascotId: Int): Int {
        val map = mascotLevelsCsv.split(",")
            .mapNotNull { token ->
                val parts = token.split(":")
                if (parts.size == 2) parts[0].toIntOrNull()?.let { it to (parts[1].toIntOrNull() ?: 1) } else null
            }.toMap()
        return map[mascotId] ?: 0
    }

    fun upgradeOrUnlockMascot(mascotId: Int): Int {
        val mutableMap = mascotLevelsCsv.split(",")
            .mapNotNull { token ->
                val parts = token.split(":")
                if (parts.size == 2) parts[0].toIntOrNull()?.let { it to (parts[1].toIntOrNull() ?: 1) } else null
            }.toMap().toMutableMap()
        val newLv = ((mutableMap[mascotId] ?: 0) + 1).coerceAtMost(10)
        mutableMap[mascotId] = newLv
        mascotLevelsCsv = mutableMap.entries.sortedBy { it.key }.joinToString(",") { "${it.key}:${it.value}" }
        prefs.edit().putString("nyx_mascot_levels_csv", mascotLevelsCsv).apply()
        onUnlockMascot(mascotId)
        return newLv
    }

    val activeMascotSpec = remember(equippedMascotId) {
        NYX_MASCOT_CATALOG.getOrElse(equippedMascotId) { NYX_MASCOT_CATALOG.first() }
    }
    val scoreMultiplier = remember(equippedMascotId, mascotLevelsCsv) {
        val lv = getMascotLevel(equippedMascotId).coerceAtLeast(1)
        1.0f + (if (equippedMascotId <= 2) 0.25f else 0.15f) + (lv - 1) * 0.05f
    }

    // High Scores
    var highBrickScore by remember { mutableIntStateOf(prefs.getInt("arcade_high_brick", 0)) }
    var highMeteorScore by remember { mutableIntStateOf(prefs.getInt("arcade_high_meteor", 0)) }
    var highMemoryScore by remember { mutableIntStateOf(prefs.getInt("arcade_high_memory", 0)) }

    // =========================================================================
    // MINI-GAME 0 STATE: NYX COSMIC BRICK BREAKER 3D
    // =========================================================================
    var brickGameRunning by remember { mutableStateOf(false) }
    var paddleX by remember { mutableFloatStateOf(0.5f) } // 0.15f..0.85f
    var ballX by remember { mutableFloatStateOf(0.5f) }
    var ballY by remember { mutableFloatStateOf(0.76f) }
    var ballVx by remember { mutableFloatStateOf(0.018f) }
    var ballVy by remember { mutableFloatStateOf(-0.022f) }
    var brickScore by remember { mutableIntStateOf(0) }
    var brickCombo by remember { mutableIntStateOf(0) }
    var brickStage by remember { mutableIntStateOf(1) }
    val bricks = remember { mutableStateListOf<Brick3DCell>() }

    fun populateBricks(stage: Int) {
        bricks.clear()
        val rowThemes = listOf(
            DuoCardinalRed to DuoCardinalRedLip,
            DuoFoxOrange to DuoFoxOrangeLip,
            DuoBeeYellow to DuoBeeYellowLip,
            DuoFeatherGreen to DuoFeatherGreenLip,
            DuoMacawBlue to DuoMacawBlueLip
        )
        for (r in 0 until 5) {
            val (face, lip) = rowThemes[r % rowThemes.size]
            val hp = if (r == 0) 2 + (stage / 2) else 1 + (stage / 3)
            for (c in 0 until 6) {
                bricks.add(
                    Brick3DCell(
                        col = c,
                        row = r,
                        hp = hp,
                        maxHp = hp,
                        faceColor = face,
                        lipColor = lip
                    )
                )
            }
        }
    }

    LaunchedEffect(Unit) {
        if (bricks.isEmpty()) populateBricks(1)
    }

    LaunchedEffect(brickGameRunning) {
        while (brickGameRunning) {
            delay(24L)
            var nextX = ballX + ballVx
            var nextY = ballY + ballVy

            // Wall bounce
            if (nextX <= 0.05f || nextX >= 0.95f) {
                ballVx = -ballVx
                nextX = nextX.coerceIn(0.05f, 0.95f)
            }
            if (nextY <= 0.05f) {
                ballVy = abs(ballVy)
                nextY = 0.05f
            }

            // Paddle bounce
            if (nextY in 0.85f..0.92f && abs(nextX - paddleX) <= 0.16f && ballVy > 0f) {
                val hitOffset = ((nextX - paddleX) / 0.16f).coerceIn(-1f, 1f)
                ballVy = -abs(ballVy).coerceAtLeast(0.020f)
                ballVx = (hitOffset * 0.026f).coerceIn(-0.030f, 0.030f)
                if (abs(ballVx) < 0.008f) ballVx = 0.012f
                brickCombo = 0
                nextY = 0.84f
            }

            // Brick collision (rows 0..4 occupy y = 0.10f..0.44f)
            val hitIndex = bricks.indexOfFirst { b ->
                val bLeft = 0.04f + b.col * 0.153f
                val bRight = bLeft + 0.142f
                val bTop = 0.10f + b.row * 0.068f
                val bBottom = bTop + 0.055f
                nextX in bLeft..bRight && nextY in bTop..bBottom
            }
            if (hitIndex >= 0) {
                val b = bricks[hitIndex]
                ballVy = -ballVy
                brickCombo += 1
                val pts = ((25 * brickCombo) * scoreMultiplier).roundToInt()
                brickScore += pts
                if (brickScore > highBrickScore) {
                    highBrickScore = brickScore
                    prefs.edit().putInt("arcade_high_brick", highBrickScore).apply()
                }
                if (b.hp > 1) {
                    bricks[hitIndex] = b.copy(hp = b.hp - 1)
                } else {
                    bricks.removeAt(hitIndex)
                    onUpdateCoins(4)
                }
                if (bricks.isEmpty()) {
                    brickStage += 1
                    populateBricks(brickStage)
                    ballX = 0.5f
                    ballY = 0.75f
                    ballVy = -abs(ballVy) * 1.06f
                    onUpdateCoins(60)
                    onPostBanner("Stage Brick Breaker 3D Selesai! Lanjut ke Stage $brickStage (+60 Koin Kosmik)!")
                }
            }

            // Bottom miss
            if (nextY >= 0.98f) {
                brickGameRunning = false
                ballX = 0.5f
                ballY = 0.76f
                ballVy = -0.022f
                onPostBanner("Bola Energi Jatuh! Skor Akhir: $brickScore")
            } else {
                ballX = nextX
                ballY = nextY
            }
        }
    }

    // =========================================================================
    // MINI-GAME 1 STATE: DUO METEOR DODGE & STAR CATCHER 3D
    // =========================================================================
    var meteorGameRunning by remember { mutableStateOf(false) }
    var shipX by remember { mutableFloatStateOf(0.5f) }
    var meteorScore by remember { mutableIntStateOf(0) }
    var shipShieldHp by remember { mutableIntStateOf(3) }
    val fallingEntities = remember { mutableStateListOf<FallingCosmicEntity>() }
    var entityCounter by remember { mutableIntStateOf(1) }

    LaunchedEffect(meteorGameRunning) {
        var tick = 0
        while (meteorGameRunning) {
            delay(30L)
            tick += 1
            if (tick % 11 == 0) {
                entityCounter += 1
                val kindRoll = secureRandom.nextInt(100)
                val kind = when {
                    kindRoll < 55 -> 0 // Meteor
                    kindRoll < 90 -> 1 // Golden Star
                    else -> 2 // Shield Crystal
                }
                val lane = 0.12f + secureRandom.nextFloat() * 0.76f
                val spd = 0.018f + min(0.022f, meteorScore * 0.000015f)
                fallingEntities.add(
                    FallingCosmicEntity(
                        id = entityCounter.toLong(),
                        laneX = lane,
                        yNorm = 0.04f,
                        speed = spd,
                        kind = kind
                    )
                )
            }

            val survivors = mutableListOf<FallingCosmicEntity>()
            for (ent in fallingEntities) {
                val ny = ent.yNorm + ent.speed
                val caught = ny in 0.80f..0.93f && abs(ent.laneX - shipX) <= 0.13f
                if (caught) {
                    when (ent.kind) {
                        0 -> {
                            shipShieldHp -= 1
                            if (shipShieldHp <= 0) {
                                meteorGameRunning = false
                                onPostBanner("Pesawat Nyx 3D terkena meteor! Skor Akhir: $meteorScore")
                            }
                        }
                        1 -> {
                            val gain = (40 * scoreMultiplier).roundToInt()
                            meteorScore += gain
                            onUpdateCoins(5)
                            if (meteorScore > highMeteorScore) {
                                highMeteorScore = meteorScore
                                prefs.edit().putInt("arcade_high_meteor", highMeteorScore).apply()
                            }
                        }
                        2 -> {
                            shipShieldHp = (shipShieldHp + 1).coerceAtMost(5)
                            meteorScore += 25
                        }
                    }
                } else if (ny < 0.98f) {
                    survivors.add(ent.copy(yNorm = ny))
                }
            }
            fallingEntities.clear()
            fallingEntities.addAll(survivors)
        }
    }

    // =========================================================================
    // MINI-GAME 2 STATE: NYX CYBER-RUNE MEMORY MATRIX 3D
    // =========================================================================
    val memorySequence = remember { mutableStateListOf<Int>() }
    var memoryUserStep by remember { mutableIntStateOf(0) }
    var highlightedRunePad by remember { mutableIntStateOf(-1) }
    var isPlayingMemorySequence by remember { mutableStateOf(false) }
    var memoryStatusText by remember {
        mutableStateOf("Tekan 'Mulai Ronde Baru' lalu ikuti urutan nyala karakter 3D Duolingo!")
    }

    fun startNextMemoryRound(resetAll: Boolean) {
        if (isPlayingMemorySequence) return
        coroutineScope.launch {
            isPlayingMemorySequence = true
            if (resetAll) {
                memorySequence.clear()
            }
            memorySequence.add(secureRandom.nextInt(6))
            memoryUserStep = 0
            memoryStatusText = "Perhatikan urutan ${memorySequence.size} Rune 3D..."
            delay(350L)
            for (padId in memorySequence) {
                highlightedRunePad = padId
                delay(420L)
                highlightedRunePad = -1
                delay(180L)
            }
            isPlayingMemorySequence = false
            memoryStatusText = "Giliran Anda! Ketuk ${memorySequence.size} karakter 3D sesuai urutan."
        }
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .duolingoCard(
                backgroundColor = DuoPureWhite,
                borderColor = DuoBeetlePurple,
                lipColor = DuoBeetlePurpleLip,
                cornerRadius = 24.dp,
                borderWidth = 2.dp,
                lipHeight = 6.dp
            )
            .padding(bottom = 6.dp)
            .testTag("nyx_retro_arcade_hub")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Active Mascot Companion Buff Banner
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .duolingoCard(
                        backgroundColor = activeMascotSpec.softColor,
                        borderColor = activeMascotSpec.primaryColor,
                        lipColor = activeMascotSpec.lipColor,
                        cornerRadius = 18.dp,
                        borderWidth = 2.dp,
                        lipHeight = 4.dp
                    )
                    .padding(bottom = 4.dp)
                    .padding(12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f)
                    ) {
                        Nyx3DMascotAvatarCanvas(
                            spec = activeMascotSpec,
                            level = getMascotLevel(equippedMascotId).coerceAtLeast(1),
                            modifier = Modifier.size(44.dp)
                        )
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "MASKOT PENDAMPING AKTIF • ${activeMascotSpec.rarityTier}",
                                fontFamily = JetBrainsMonoFontFamily,
                                fontSize = 9.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = activeMascotSpec.lipColor,
                                maxLines = 1
                            )
                            Text(
                                text = "${activeMascotSpec.name} (Lv.${getMascotLevel(equippedMascotId).coerceAtLeast(1)})",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.ExtraBold,
                                color = VaultObsidian,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            Text(
                                text = "${activeMascotSpec.bonusLabel} • Multiplier: ${"%.2f".format(scoreMultiplier)}x",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = VaultTextMuted,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                }
            }

            // Arcade Mode Switcher
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                val modes = listOf(
                    Triple(0, "Brick Breaker 3D", DuoFeatherGreen to DuoFeatherGreenLip),
                    Triple(1, "Meteor & Star Catch 3D", DuoMacawBlue to DuoMacawBlueLip),
                    Triple(2, "Rune Memory 3D", DuoFoxOrange to DuoFoxOrangeLip),
                    Triple(3, "Gacha Kapsul Maskot 3D", DuoBeetlePurple to DuoBeetlePurpleLip)
                )
                modes.forEach { (mId, title, colors) ->
                    val isSel = activeArcadeMode == mId
                    Box(
                        modifier = Modifier
                            .height(40.dp)
                            .duolingoButton(
                                faceColor = if (isSel) colors.first else DuoPureWhite,
                                lipColor = if (isSel) colors.second else DuoHareLip,
                                cornerRadius = 13.dp,
                                lipHeight = 3.dp
                            )
                            .instantTouchPress { activeArcadeMode = mId }
                            .padding(bottom = 3.dp)
                            .padding(horizontal = 12.dp)
                            .testTag("arcade_submode_$mId"),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = title,
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.ExtraBold,
                            color = if (isSel) Color.White else VaultObsidian,
                            maxLines = 1,
                            softWrap = false
                        )
                    }
                }
            }

            when (activeArcadeMode) {
                // =====================================================================
                // GAME 0: NYX COSMIC BRICK BREAKER 3D
                // =====================================================================
                0 -> {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "NYX COSMIC BRICK BREAKER 3D • STAGE $brickStage",
                                fontFamily = JetBrainsMonoFontFamily,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = DuoFeatherGreenLip
                            )
                            Text(
                                text = "Skor: $brickScore • Rekor: $highBrickScore • Kombo: ${brickCombo}x",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.ExtraBold,
                                color = VaultObsidian
                            )
                        }

                        Box(
                            modifier = Modifier
                                .height(38.dp)
                                .duolingoButton(
                                    faceColor = if (brickGameRunning) DuoFoxOrange else DuoFeatherGreen,
                                    lipColor = if (brickGameRunning) DuoFoxOrangeLip else DuoFeatherGreenLip,
                                    cornerRadius = 12.dp,
                                    lipHeight = 3.dp
                                )
                                .instantTouchPress {
                                    if (!brickGameRunning && bricks.isEmpty()) {
                                        populateBricks(brickStage)
                                    }
                                    brickGameRunning = !brickGameRunning
                                }
                                .padding(bottom = 3.dp)
                                .padding(horizontal = 12.dp)
                                .testTag("brick_breaker_start_button"),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = if (brickGameRunning) "JEDA" else "MAIN SEKARANG",
                                fontFamily = JetBrainsMonoFontFamily,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = Color.White,
                                maxLines = 1,
                                softWrap = false
                            )
                        }
                    }

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(1.1f)
                            .duolingoCard(
                                backgroundColor = Color(0xFF0B132B),
                                borderColor = DuoFeatherGreen,
                                lipColor = DuoFeatherGreenLip,
                                cornerRadius = 20.dp,
                                borderWidth = 2.dp,
                                lipHeight = 5.dp
                            )
                            .padding(bottom = 5.dp)
                            .clip(RoundedCornerShape(18.dp))
                            .pointerInput(Unit) {
                                detectDragGestures { change, _ ->
                                    change.consume()
                                    paddleX = (change.position.x / size.width.toFloat()).coerceIn(0.15f, 0.85f)
                                }
                            }
                            .pointerInput(Unit) {
                                detectTapGestures { offset ->
                                    paddleX = (offset.x / size.width.toFloat()).coerceIn(0.15f, 0.85f)
                                    if (!brickGameRunning) brickGameRunning = true
                                }
                            }
                    ) {
                        Canvas(modifier = Modifier.fillMaxSize()) {
                            val w = size.width
                            val h = size.height

                            // Draw 3D Duolingo Crystal Bricks
                            for (b in bricks) {
                                val bx = (0.04f + b.col * 0.153f) * w
                                val by = (0.10f + b.row * 0.068f) * h
                                val bw = 0.142f * w
                                val bh = 0.052f * h

                                drawRoundRect(
                                    color = b.lipColor,
                                    topLeft = Offset(bx, by + 5f),
                                    size = Size(bw, bh),
                                    cornerRadius = CornerRadius(10f, 10f)
                                )
                                drawRoundRect(
                                    color = b.faceColor,
                                    topLeft = Offset(bx, by),
                                    size = Size(bw, bh),
                                    cornerRadius = CornerRadius(10f, 10f)
                                )
                                drawRoundRect(
                                    color = Color.White.copy(alpha = 0.35f),
                                    topLeft = Offset(bx + 6f, by + 4f),
                                    size = Size(bw - 12f, bh * 0.32f),
                                    cornerRadius = CornerRadius(6f, 6f)
                                )
                            }

                            // Draw 3D Energy Ball
                            val bx = ballX * w
                            val by = ballY * h
                            val bRad = min(w, h) * 0.032f
                            drawCircle(color = DuoBeeYellowLip, radius = bRad, center = Offset(bx, by + 4f))
                            drawCircle(color = DuoBeeYellow, radius = bRad, center = Offset(bx, by))
                            drawCircle(color = Color.White, radius = bRad * 0.45f, center = Offset(bx - bRad * 0.25f, by - bRad * 0.25f))

                            // Draw 3D Duolingo Owl Paddle
                            val pw = w * 0.28f
                            val ph = h * 0.048f
                            val px = (paddleX * w) - pw / 2f
                            val py = h * 0.88f
                            drawRoundRect(
                                color = DuoFeatherGreenLip,
                                topLeft = Offset(px, py + 5f),
                                size = Size(pw, ph),
                                cornerRadius = CornerRadius(16f, 16f)
                            )
                            drawRoundRect(
                                color = DuoFeatherGreen,
                                topLeft = Offset(px, py),
                                size = Size(pw, ph),
                                cornerRadius = CornerRadius(16f, 16f)
                            )
                            // Owl Eyes on Paddle
                            drawCircle(color = Color.White, radius = ph * 0.32f, center = Offset(px + pw * 0.38f, py + ph * 0.5f))
                            drawCircle(color = Color.White, radius = ph * 0.32f, center = Offset(px + pw * 0.62f, py + ph * 0.5f))
                            drawCircle(color = VaultObsidian, radius = ph * 0.16f, center = Offset(px + pw * 0.38f, py + ph * 0.5f))
                            drawCircle(color = VaultObsidian, radius = ph * 0.16f, center = Offset(px + pw * 0.62f, py + ph * 0.5f))
                        }
                    }
                }

                // =====================================================================
                // GAME 1: DUO METEOR DODGE & STAR CATCHER 3D
                // =====================================================================
                1 -> {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "DUO METEOR DODGE & STAR CATCHER 3D",
                                fontFamily = JetBrainsMonoFontFamily,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = DuoMacawBlueLip
                            )
                            Text(
                                text = "Skor: $meteorScore • Rekor: $highMeteorScore • Perisai: $shipShieldHp HP",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.ExtraBold,
                                color = VaultObsidian
                            )
                        }

                        Box(
                            modifier = Modifier
                                .height(38.dp)
                                .duolingoButton(
                                    faceColor = if (meteorGameRunning) DuoFoxOrange else DuoMacawBlue,
                                    lipColor = if (meteorGameRunning) DuoFoxOrangeLip else DuoMacawBlueLip,
                                    cornerRadius = 12.dp,
                                    lipHeight = 3.dp
                                )
                                .instantTouchPress {
                                    if (!meteorGameRunning && shipShieldHp <= 0) {
                                        shipShieldHp = 3
                                        meteorScore = 0
                                        fallingEntities.clear()
                                    }
                                    meteorGameRunning = !meteorGameRunning
                                }
                                .padding(bottom = 3.dp)
                                .padding(horizontal = 12.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = if (meteorGameRunning) "JEDA" else "MULAI TERBANG",
                                fontFamily = JetBrainsMonoFontFamily,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = Color.White,
                                maxLines = 1,
                                softWrap = false
                            )
                        }
                    }

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(1.1f)
                            .duolingoCard(
                                backgroundColor = Color(0xFF091526),
                                borderColor = DuoMacawBlue,
                                lipColor = DuoMacawBlueLip,
                                cornerRadius = 20.dp,
                                borderWidth = 2.dp,
                                lipHeight = 5.dp
                            )
                            .padding(bottom = 5.dp)
                            .clip(RoundedCornerShape(18.dp))
                            .pointerInput(Unit) {
                                detectDragGestures { change, _ ->
                                    change.consume()
                                    shipX = (change.position.x / size.width.toFloat()).coerceIn(0.12f, 0.88f)
                                }
                            }
                            .pointerInput(Unit) {
                                detectTapGestures { offset ->
                                    shipX = (offset.x / size.width.toFloat()).coerceIn(0.12f, 0.88f)
                                    if (!meteorGameRunning) {
                                        if (shipShieldHp <= 0) shipShieldHp = 3
                                        meteorGameRunning = true
                                    }
                                }
                            }
                    ) {
                        Canvas(modifier = Modifier.fillMaxSize()) {
                            val w = size.width
                            val h = size.height

                            for (ent in fallingEntities) {
                                val ex = ent.laneX * w
                                val ey = ent.yNorm * h
                                val r = min(w, h) * 0.048f
                                when (ent.kind) {
                                    0 -> {
                                        // 3D Asteroid Meteor
                                        drawCircle(color = DuoCardinalRedLip, radius = r, center = Offset(ex, ey + 4f))
                                        drawCircle(color = DuoCardinalRed, radius = r, center = Offset(ex, ey))
                                        drawCircle(color = DuoFoxOrange, radius = r * 0.45f, center = Offset(ex - r * 0.2f, ey - r * 0.2f))
                                    }
                                    1 -> {
                                        // 3D Star Coin
                                        drawCircle(color = DuoBeeYellowLip, radius = r * 0.9f, center = Offset(ex, ey + 4f))
                                        drawCircle(color = DuoBeeYellow, radius = r * 0.9f, center = Offset(ex, ey))
                                        drawCircle(color = Color.White, radius = r * 0.35f, center = Offset(ex, ey))
                                    }
                                    else -> {
                                        // 3D Shield Crystal
                                        drawCircle(color = DuoMacawBlueLip, radius = r * 0.9f, center = Offset(ex, ey + 4f))
                                        drawCircle(color = DuoMacawBlue, radius = r * 0.9f, center = Offset(ex, ey))
                                        drawCircle(color = DuoPureWhite, radius = r * 0.4f, center = Offset(ex, ey))
                                    }
                                }
                            }

                            // 3D Duolingo Starship at bottom
                            val sx = shipX * w
                            val sy = h * 0.86f
                            val sr = min(w, h) * 0.068f
                            drawRoundRect(
                                color = activeMascotSpec.lipColor,
                                topLeft = Offset(sx - sr, sy - sr + 5f),
                                size = Size(sr * 2f, sr * 1.8f),
                                cornerRadius = CornerRadius(18f, 18f)
                            )
                            drawRoundRect(
                                color = activeMascotSpec.primaryColor,
                                topLeft = Offset(sx - sr, sy - sr),
                                size = Size(sr * 2f, sr * 1.8f),
                                cornerRadius = CornerRadius(18f, 18f)
                            )
                            drawCircle(color = Color.White, radius = sr * 0.28f, center = Offset(sx - sr * 0.36f, sy - sr * 0.1f))
                            drawCircle(color = Color.White, radius = sr * 0.28f, center = Offset(sx + sr * 0.36f, sy - sr * 0.1f))
                            drawCircle(color = VaultObsidian, radius = sr * 0.14f, center = Offset(sx - sr * 0.36f, sy - sr * 0.1f))
                            drawCircle(color = VaultObsidian, radius = sr * 0.14f, center = Offset(sx + sr * 0.36f, sy - sr * 0.1f))
                        }
                    }
                }

                // =====================================================================
                // GAME 2: NYX CYBER-RUNE MEMORY MATRIX 3D
                // =====================================================================
                2 -> {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "NYX CYBER-RUNE MEMORY 3D • LEVEL ${memorySequence.size}",
                                fontFamily = JetBrainsMonoFontFamily,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = DuoFoxOrangeLip
                            )
                            Text(
                                text = "Rekor Terbaik: $highMemoryScore Poin",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.ExtraBold,
                                color = VaultObsidian
                            )
                        }

                        Box(
                            modifier = Modifier
                                .height(38.dp)
                                .duolingoButton(
                                    faceColor = DuoFeatherGreen,
                                    lipColor = DuoFeatherGreenLip,
                                    cornerRadius = 12.dp,
                                    lipHeight = 3.dp
                                )
                                .instantTouchPress { startNextMemoryRound(resetAll = true) }
                                .padding(bottom = 3.dp)
                                .padding(horizontal = 12.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "MULAI RONDE BARU",
                                fontFamily = JetBrainsMonoFontFamily,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = Color.White,
                                maxLines = 1,
                                softWrap = false
                            )
                        }
                    }

                    Text(
                        text = memoryStatusText,
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.Bold,
                        color = VaultObsidian
                    )

                    // 2x3 Grid of 3D Character Rune Pads
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        for (row in 0 until 2) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                for (col in 0 until 3) {
                                    val padId = row * 3 + col
                                    val spec = NYX_MASCOT_CATALOG[padId]
                                    val isLit = highlightedRunePad == padId
                                    Box(
                                        modifier = Modifier
                                            .weight(1f)
                                            .height(94.dp)
                                            .duolingoCard(
                                                backgroundColor = if (isLit) DuoBeeYellow else spec.softColor,
                                                borderColor = if (isLit) DuoFoxOrange else spec.primaryColor,
                                                lipColor = spec.lipColor,
                                                cornerRadius = 18.dp,
                                                borderWidth = if (isLit) 3.dp else 2.dp,
                                                lipHeight = 5.dp
                                            )
                                            .instantTouchPress {
                                                if (isPlayingMemorySequence || memorySequence.isEmpty()) return@instantTouchPress
                                                coroutineScope.launch {
                                                    highlightedRunePad = padId
                                                    delay(180L)
                                                    highlightedRunePad = -1
                                                }
                                                if (memorySequence[memoryUserStep] == padId) {
                                                    memoryUserStep += 1
                                                    if (memoryUserStep >= memorySequence.size) {
                                                        val pts = (memorySequence.size * 50 * scoreMultiplier).roundToInt()
                                                        if (pts > highMemoryScore) {
                                                            highMemoryScore = pts
                                                            prefs.edit().putInt("arcade_high_memory", highMemoryScore).apply()
                                                        }
                                                        onUpdateCoins(20 + memorySequence.size * 5)
                                                        memoryStatusText = "BENAR! Level ${memorySequence.size} Tuntas (+${20 + memorySequence.size * 5} Koin). Bersiap level berikutnya..."
                                                        startNextMemoryRound(resetAll = false)
                                                    }
                                                } else {
                                                    memoryStatusText = "Urutan salah di langkah ke-${memoryUserStep + 1}! Tekan 'Mulai Ronde Baru' untuk mencoba lagi."
                                                    memorySequence.clear()
                                                }
                                            }
                                            .padding(bottom = 5.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Nyx3DMascotAvatarCanvas(
                                            spec = spec,
                                            level = if (isLit) 5 else 2,
                                            modifier = Modifier.size(54.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                // =====================================================================
                // GAME 3: MESIN KAPSUL GACHA MASKOT RAHASIA NYX 3D (SSR / SR / R)
                // =====================================================================
                3 -> {
                    Text(
                        text = "MESIN KAPSUL GACHA MASKOT 3D • BUKA & UPGRADE 8 MASKOT PENDAMPING",
                        fontFamily = JetBrainsMonoFontFamily,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = DuoBeetlePurpleLip
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(46.dp)
                                .duolingoButton(
                                    faceColor = DuoFeatherGreen,
                                    lipColor = DuoFeatherGreenLip,
                                    cornerRadius = 14.dp,
                                    lipHeight = 4.dp
                                )
                                .instantTouchPress {
                                    if (cosmicCoins < 50) {
                                        onUpdateCoins(100)
                                        onPostBanner("Subsidi +100 Koin Kosmik diberikan agar Anda bisa membuka Kapsul Gacha!")
                                        return@instantTouchPress
                                    }
                                    onUpdateCoins(-50)
                                    val pulledId = secureRandom.nextInt(NYX_MASCOT_CATALOG.size)
                                    val newLv = upgradeOrUnlockMascot(pulledId)
                                    val spec = NYX_MASCOT_CATALOG[pulledId]
                                    equippedMascotId = pulledId
                                    prefs.edit().putInt("nyx_equipped_mascot_id", pulledId).apply()
                                    onPostBanner("GACHA 1X: Anda mendapatkan [${spec.rarityTier}] ${spec.name} (Level $newLv)!")
                                }
                                .padding(bottom = 4.dp)
                                .testTag("gacha_pull_1x_button"),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "BUKA 1X KAPSUL (50 KOIN)",
                                fontFamily = JetBrainsMonoFontFamily,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = Color.White,
                                maxLines = 1,
                                softWrap = false
                            )
                        }

                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(46.dp)
                                .duolingoButton(
                                    faceColor = DuoBeeYellow,
                                    lipColor = DuoBeeYellowLip,
                                    cornerRadius = 14.dp,
                                    lipHeight = 4.dp
                                )
                                .instantTouchPress {
                                    if (cosmicCoins < 200) {
                                        onUpdateCoins(200)
                                        onPostBanner("Bonus +200 Koin Kosmik diberikan untuk Mega Gacha 5X!")
                                        return@instantTouchPress
                                    }
                                    onUpdateCoins(-200)
                                    val pulledNames = mutableListOf<String>()
                                    repeat(5) {
                                        val pId = secureRandom.nextInt(NYX_MASCOT_CATALOG.size)
                                        upgradeOrUnlockMascot(pId)
                                        pulledNames.add(NYX_MASCOT_CATALOG[pId].name)
                                    }
                                    onPostBanner("MEGA GACHA 5X BERHASIL: ${pulledNames.distinct().joinToString(", ")}!")
                                }
                                .padding(bottom = 4.dp)
                                .testTag("gacha_pull_5x_button"),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "MEGA 5X KAPSUL (200 KOIN)",
                                fontFamily = JetBrainsMonoFontFamily,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = VaultObsidian,
                                maxLines = 1,
                                softWrap = false
                            )
                        }
                    }

                    // 8 Mascots Grid (Tap to Equip)
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        NYX_MASCOT_CATALOG.chunked(2).forEach { rowSpecs ->
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                rowSpecs.forEach { spec ->
                                    val lv = getMascotLevel(spec.id)
                                    val isEquipped = equippedMascotId == spec.id
                                    Box(
                                        modifier = Modifier
                                            .weight(1f)
                                            .duolingoCard(
                                                backgroundColor = if (isEquipped) spec.softColor else DuoPureWhite,
                                                borderColor = if (isEquipped) spec.primaryColor else DuoSwanBorder,
                                                lipColor = if (isEquipped) spec.lipColor else DuoHareLip,
                                                cornerRadius = 16.dp,
                                                borderWidth = 2.dp,
                                                lipHeight = 4.dp
                                            )
                                            .instantTouchPress {
                                                if (lv <= 0) {
                                                    upgradeOrUnlockMascot(spec.id)
                                                }
                                                equippedMascotId = spec.id
                                                prefs.edit().putInt("nyx_equipped_mascot_id", spec.id).apply()
                                                onPostBanner("${spec.name} dipasang sebagai Maskot Pendamping Aktif!")
                                            }
                                            .padding(bottom = 4.dp)
                                            .padding(10.dp)
                                    ) {
                                        Row(
                                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Nyx3DMascotAvatarCanvas(
                                                spec = spec,
                                                level = lv.coerceAtLeast(1),
                                                modifier = Modifier.size(40.dp)
                                            )
                                            Column(modifier = Modifier.weight(1f)) {
                                                Text(
                                                    text = spec.rarityTier,
                                                    fontFamily = JetBrainsMonoFontFamily,
                                                    fontSize = 8.sp,
                                                    fontWeight = FontWeight.ExtraBold,
                                                    color = spec.lipColor,
                                                    maxLines = 1
                                                )
                                                Text(
                                                    text = spec.name,
                                                    style = MaterialTheme.typography.labelSmall,
                                                    fontWeight = FontWeight.ExtraBold,
                                                    color = VaultObsidian,
                                                    maxLines = 1,
                                                    overflow = TextOverflow.Ellipsis
                                                )
                                                Text(
                                                    text = if (isEquipped) "AKTIF • Lv.${lv.coerceAtLeast(1)}" else if (lv > 0) "Lv.$lv (Ketuk Pakai)" else "Ketuk Buka",
                                                    fontFamily = JetBrainsMonoFontFamily,
                                                    fontSize = 9.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    color = if (isEquipped) DuoFeatherGreenLip else VaultTextMuted,
                                                    maxLines = 1
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

@Composable
fun Nyx3DMascotAvatarCanvas(
    spec: NyxSecretMascotSpec,
    level: Int,
    modifier: Modifier = Modifier
) {
    Canvas(modifier = modifier) {
        val cx = size.width / 2f
        val cy = size.height / 2f
        val r = size.minDimension * 0.44f

        // 1. 3D Extruded Lip
        drawRoundRect(
            color = spec.lipColor,
            topLeft = Offset(cx - r, cy - r + 4f),
            size = Size(r * 2f, r * 2f),
            cornerRadius = CornerRadius(r * 0.46f, r * 0.46f)
        )
        // 2. Main 3D Character Body
        drawRoundRect(
            color = spec.primaryColor,
            topLeft = Offset(cx - r, cy - r),
            size = Size(r * 2f, r * 2f),
            cornerRadius = CornerRadius(r * 0.46f, r * 0.46f)
        )
        // 3. Top Gloss Highlight
        drawRoundRect(
            color = Color.White.copy(alpha = 0.35f),
            topLeft = Offset(cx - r * 0.65f, cy - r * 0.8f),
            size = Size(r * 1.3f, r * 0.34f),
            cornerRadius = CornerRadius(r * 0.2f, r * 0.2f)
        )
        // 4. Expressive 3D Duolingo Eyes
        val ex = r * 0.36f
        val ey = cy - r * 0.06f
        drawCircle(color = Color.White, radius = r * 0.28f, center = Offset(cx - ex, ey))
        drawCircle(color = Color.White, radius = r * 0.28f, center = Offset(cx + ex, ey))
        drawCircle(color = VaultObsidian, radius = r * 0.14f, center = Offset(cx - ex, ey))
        drawCircle(color = VaultObsidian, radius = r * 0.14f, center = Offset(cx + ex, ey))

        // 5. Golden Beak & Crown Crest
        val beak = Path().apply {
            moveTo(cx, cy + r * 0.32f)
            lineTo(cx - r * 0.22f, cy + r * 0.06f)
            lineTo(cx + r * 0.22f, cy + r * 0.06f)
            close()
        }
        drawPath(path = beak, color = spec.crestColor)

        // 6. Star Level Pips
        val pips = min(5, level)
        val startX = cx - ((pips - 1) * r * 0.22f) / 2f
        for (i in 0 until pips) {
            drawCircle(
                color = DuoBeeYellow,
                radius = r * 0.09f,
                center = Offset(startX + i * r * 0.22f, cy + r * 0.68f)
            )
        }
    }
}
