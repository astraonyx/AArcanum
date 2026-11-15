package com.example.ui

import android.content.Context
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
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
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Casino
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FastForward
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.LockOpen
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Style
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
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
import org.json.JSONArray
import org.json.JSONObject
import java.security.SecureRandom
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.math.PI
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.hypot
import kotlin.math.max
import kotlin.math.min
import kotlin.math.roundToInt
import kotlin.math.sin

// =========================================================================
// RUANG TERISOLASI BARU (PIN 3-LAPIS KHUSUS): FUN ZONE 3D & KAPSUL WAKTU
// Seluruh aset karakter, tower, musuh, simbol slot, kartu tarot, dan mood
// digambar menggunakan Duolingo 3D Custom Canvas berlapis (TANPA EMOJI &
// TANPA SVG DATAR).
// =========================================================================

enum class DuoTowerType(
    val id: Int,
    val title: String,
    val roleLabel: String,
    val baseCost: Int,
    val baseDamage: Float,
    val baseRangeCells: Float,
    val cooldownTicks: Int,
    val primaryColor: Color,
    val lipColor: Color,
    val softColor: Color
) {
    LASER_OWL(
        id = 0,
        title = "Laser Owl 3D",
        roleLabel = "Sniper Cepat",
        baseCost = 60,
        baseDamage = 28f,
        baseRangeCells = 2.6f,
        cooldownTicks = 5,
        primaryColor = DuoFeatherGreen,
        lipColor = DuoFeatherGreenLip,
        softColor = DuoFeatherGreenSoft
    ),
    FROST_MACAW(
        id = 1,
        title = "Frost Macaw 3D",
        roleLabel = "Es Pelambat 55%",
        baseCost = 85,
        baseDamage = 20f,
        baseRangeCells = 2.4f,
        cooldownTicks = 7,
        primaryColor = DuoMacawBlue,
        lipColor = DuoMacawBlueLip,
        softColor = DuoMacawBlueSoft
    ),
    PYRO_FOX(
        id = 2,
        title = "Pyro Fox 3D",
        roleLabel = "Meriam Area AoE",
        baseCost = 110,
        baseDamage = 48f,
        baseRangeCells = 2.3f,
        cooldownTicks = 10,
        primaryColor = DuoFoxOrange,
        lipColor = DuoFoxOrangeLip,
        softColor = DuoFoxOrangeSoft
    ),
    TESLA_BEETLE(
        id = 3,
        title = "Tesla Beetle 3D",
        roleLabel = "Petir 4 Target",
        baseCost = 145,
        baseDamage = 42f,
        baseRangeCells = 2.7f,
        cooldownTicks = 8,
        primaryColor = DuoBeetlePurple,
        lipColor = DuoBeetlePurpleLip,
        softColor = DuoBeetlePurpleSoft
    ),
    SOLAR_BEE(
        id = 4,
        title = "Solar Bee 3D",
        roleLabel = "Penghasil Emas + Sinar",
        baseCost = 130,
        baseDamage = 32f,
        baseRangeCells = 2.5f,
        cooldownTicks = 7,
        primaryColor = DuoBeeYellow,
        lipColor = DuoBeeYellowLip,
        softColor = DuoBeeYellowSoft
    ),
    AEGIS_CARDINAL(
        id = 5,
        title = "Aegis Cardinal 3D",
        roleLabel = "Penghancur Boss",
        baseCost = 190,
        baseDamage = 115f,
        baseRangeCells = 3.2f,
        cooldownTicks = 12,
        primaryColor = DuoCardinalRed,
        lipColor = DuoCardinalRedLip,
        softColor = DuoCardinalRedSoft
    )
}

enum class DuoEnemyKind(
    val id: Int,
    val title: String,
    val hpMultiplier: Float,
    val speedCellsPerTick: Float,
    val armorReduction: Float,
    val rewardGold: Int,
    val bodyColor: Color,
    val lipColor: Color,
    val accentColor: Color,
    val isBoss: Boolean = false
) {
    ASTEROID_SLIME(
        id = 0,
        title = "Slime Kosmik",
        hpMultiplier = 1.0f,
        speedCellsPerTick = 0.085f,
        armorReduction = 0.0f,
        rewardGold = 12,
        bodyColor = DuoFeatherGreen,
        lipColor = DuoFeatherGreenLip,
        accentColor = DuoBeeYellow
    ),
    NEBULA_DRONE(
        id = 1,
        title = "Drone Cepat",
        hpMultiplier = 0.78f,
        speedCellsPerTick = 0.135f,
        armorReduction = 0.05f,
        rewardGold = 14,
        bodyColor = DuoMacawBlue,
        lipColor = DuoMacawBlueLip,
        accentColor = DuoPureWhite
    ),
    ARMOR_BEETLE(
        id = 2,
        title = "Kumbang Baja",
        hpMultiplier = 1.85f,
        speedCellsPerTick = 0.068f,
        armorReduction = 0.25f,
        rewardGold = 20,
        bodyColor = DuoBeetlePurple,
        lipColor = DuoBeetlePurpleLip,
        accentColor = DuoBeeYellow
    ),
    PLASMA_FOX_RAIDER(
        id = 3,
        title = "Raider Plasma",
        hpMultiplier = 1.35f,
        speedCellsPerTick = 0.11f,
        armorReduction = 0.10f,
        rewardGold = 18,
        bodyColor = DuoFoxOrange,
        lipColor = DuoFoxOrangeLip,
        accentColor = DuoPureWhite
    ),
    VOID_GOLEM(
        id = 4,
        title = "Golem Meteor",
        hpMultiplier = 3.1f,
        speedCellsPerTick = 0.052f,
        armorReduction = 0.32f,
        rewardGold = 32,
        bodyColor = Color(0xFF8B5A2B),
        lipColor = Color(0xFF5C3A1B),
        accentColor = DuoFoxOrange
    ),
    SOLAR_WYVERN(
        id = 5,
        title = "Naga Surya",
        hpMultiplier = 2.4f,
        speedCellsPerTick = 0.095f,
        armorReduction = 0.18f,
        rewardGold = 28,
        bodyColor = DuoBeeYellow,
        lipColor = DuoBeeYellowLip,
        accentColor = DuoCardinalRed
    ),
    QUANTUM_HYDRA_MINIBOSS(
        id = 6,
        title = "Mini-Boss Hydra",
        hpMultiplier = 6.2f,
        speedCellsPerTick = 0.058f,
        armorReduction = 0.28f,
        rewardGold = 80,
        bodyColor = DuoCardinalRed,
        lipColor = DuoCardinalRedLip,
        accentColor = DuoBeeYellow,
        isBoss = true
    ),
    CHRONO_LEVIATHAN_BOSS(
        id = 7,
        title = "Boss Leviathan",
        hpMultiplier = 12.5f,
        speedCellsPerTick = 0.046f,
        armorReduction = 0.35f,
        rewardGold = 180,
        bodyColor = Color(0xFF8B2CF5),
        lipColor = Color(0xFF4A0E9E),
        accentColor = DuoBeeYellow,
        isBoss = true
    )
}

data class PlacedDuoTower(
    val gridX: Int,
    val gridY: Int,
    val type: DuoTowerType,
    val level: Int = 1,
    val cooldownRemaining: Int = 0,
    val totalDamageDealt: Long = 0L
) {
    val effectiveDamage: Float
        get() = type.baseDamage * (1f + (level - 1) * 0.48f)

    val effectiveRange: Float
        get() = type.baseRangeCells + min(1.2f, (level - 1) * 0.12f)

    val upgradeCost: Int
        get() = (type.baseCost * (0.75f + level * 0.55f)).roundToInt()

    val sellRefund: Int
        get() = (type.baseCost * 0.7f * level).roundToInt()
}

data class ActiveDuoEnemy(
    val instanceId: Long,
    val kind: DuoEnemyKind,
    val maxHp: Float,
    val currentHp: Float,
    val pathProgress: Float, // 0f..(pathPoints.size - 1)
    val slowTicksRemaining: Int = 0
)

data class LaserShotFx(
    val fromGridX: Float,
    val fromGridY: Float,
    val toGridX: Float,
    val toGridY: Float,
    val color: Color,
    val ticksLeft: Int = 2
)

data class MoodCapsuleEntry(
    val id: Long,
    val title: String,
    val message: String,
    val moodIndex: Int, // 0..5
    val createdAtEpochMs: Long,
    val unlockAtEpochMs: Long
)

@Composable
fun IsolatedFunZoneScreen(
    onLockAndExit: () -> Unit,
    onPostBanner: (String) -> Unit
) {
    val context = LocalContext.current
    val prefs = remember(context) {
        context.getSharedPreferences("arcanum_isolated_fun_zone_prefs", Context.MODE_PRIVATE)
    }
    val secureRandom = remember { SecureRandom() }
    val coroutineScope = rememberCoroutineScope()

    // Sub-tabs inside Isolated Fun Zone:
    // 4 = Dashboard 27 Game Asah Otak, Strategi, Memori, Matematika & Arcade 3D (Default Hub)
    // 0 = Galaxy Tower Defense 3D (200 Wave + Endless)
    // 1 = Slot Gacha Kosmik 3D & Koleksi Maskot
    // 2 = Tarot Kosmik 3D
    // 3 = Kapsul Waktu Digital & Buku Harian Mood 3D
    var activeFunTab by remember { mutableIntStateOf(4) }

    // Shared Cosmic Coins & High Scores persisted in SharedPreferences
    var cosmicCoins by remember {
        mutableIntStateOf(prefs.getInt("cosmic_coins", 650))
    }
    var highestWaveReached by remember {
        mutableIntStateOf(prefs.getInt("td_highest_wave", 1))
    }
    var unlockedMascotCsv by remember {
        mutableStateOf(prefs.getString("unlocked_mascots", "0") ?: "0")
    }

    fun updateCoins(delta: Int) {
        cosmicCoins = (cosmicCoins + delta).coerceAtLeast(0)
        prefs.edit().putInt("cosmic_coins", cosmicCoins).apply()
    }

    fun unlockMascot(mascotId: Int) {
        val currentSet = unlockedMascotCsv.split(",")
            .mapNotNull { it.trim().toIntOrNull() }
            .toMutableSet()
        currentSet.add(mascotId)
        unlockedMascotCsv = currentSet.sorted().joinToString(",")
        prefs.edit().putString("unlocked_mascots", unlockedMascotCsv).apply()
    }

    // =========================================================================
    // STATE GALAXY TOWER DEFENSE 3D (WAVE 1..200 + ENDLESS)
    // =========================================================================
    val gridCols = 6
    val gridRows = 8
    val pathCells = remember {
        listOf(
            0 to 1, 1 to 1, 2 to 1, 3 to 1, 4 to 1,
            4 to 2, 4 to 3, 3 to 3, 2 to 3, 1 to 3,
            1 to 4, 1 to 5, 2 to 5, 3 to 5, 4 to 5,
            4 to 6, 3 to 6, 2 to 6, 1 to 6, 0 to 6
        )
    }
    val pathSet = remember(pathCells) { pathCells.toSet() }

    var tdWave by remember { mutableIntStateOf(1) }
    var tdGold by remember { mutableIntStateOf(260) }
    var tdBaseHp by remember { mutableIntStateOf(25) }
    var tdScore by remember { mutableLongStateOf(0L) }
    var isWaveRunning by remember { mutableStateOf(false) }
    var autoStartNextWave by remember { mutableStateOf(true) }
    var tdSpeedMultiplier by remember { mutableIntStateOf(1) } // 1, 2, 4
    var selectedTowerType by remember { mutableStateOf(DuoTowerType.LASER_OWL) }
    var selectedGridCell by remember { mutableStateOf<Pair<Int, Int>?>(2 to 2) }
    var overdriveTicksRemaining by remember { mutableIntStateOf(0) }
    var wave200ConqueredBanner by remember {
        mutableStateOf(prefs.getBoolean("wave_200_conquered", false))
    }

    val placedTowers = remember {
        mutableStateListOf(
            PlacedDuoTower(gridX = 2, gridY = 2, type = DuoTowerType.LASER_OWL, level = 1),
            PlacedDuoTower(gridX = 3, gridY = 4, type = DuoTowerType.FROST_MACAW, level = 1)
        )
    }
    val activeEnemies = remember { mutableStateListOf<ActiveDuoEnemy>() }
    val activeLasers = remember { mutableStateListOf<LaserShotFx>() }
    var enemiesRemainingToSpawn by remember { mutableIntStateOf(0) }
    var spawnTickCounter by remember { mutableIntStateOf(0) }
    var enemyIdCounter by remember { mutableLongStateOf(1L) }

    fun cellPositionOfProgress(progress: Float): Offset {
        val clamped = progress.coerceIn(0f, (pathCells.size - 1).toFloat())
        val idx = clamped.toInt().coerceIn(0, pathCells.size - 1)
        val nextIdx = (idx + 1).coerceIn(0, pathCells.size - 1)
        val frac = clamped - idx
        val (x1, y1) = pathCells[idx]
        val (x2, y2) = pathCells[nextIdx]
        return Offset(
            x = x1 + (x2 - x1) * frac,
            y = y1 + (y2 - y1) * frac
        )
    }

    fun startTowerDefenseWave(targetWave: Int = tdWave) {
        tdWave = targetWave.coerceAtLeast(1)
        if (tdBaseHp <= 0) {
            tdBaseHp = 25
            tdGold = max(tdGold, 240)
        }
        enemiesRemainingToSpawn = (6 + (tdWave * 0.45f).roundToInt()).coerceIn(6, 55)
        spawnTickCounter = 0
        isWaveRunning = true
    }

    fun jumpToCheckpointWave(checkpointWave: Int) {
        activeEnemies.clear()
        activeLasers.clear()
        tdWave = checkpointWave.coerceAtLeast(1)
        tdBaseHp = 25
        // Provide fair tactical starting gold scaled to the checkpoint wave so the player can upgrade & conquer up to Wave 200
        val scaledGold = 260 + checkpointWave * 55
        tdGold = max(tdGold, scaledGold)
        isWaveRunning = false
        onPostBanner("Lompat ke Checkpoint Wave $tdWave! Emas taktis disesuaikan: $tdGold Gold.")
    }

    // Game Loop Galaxy Tower Defense
    LaunchedEffect(isWaveRunning, tdSpeedMultiplier, tdWave) {
        while (isWaveRunning) {
            val tickDelayMs = when (tdSpeedMultiplier) {
                4 -> 32L
                2 -> 60L
                else -> 110L
            }
            delay(tickDelayMs)

            if (overdriveTicksRemaining > 0) {
                overdriveTicksRemaining -= 1
            }

            // 1. Spawn enemies for the active wave
            if (enemiesRemainingToSpawn > 0) {
                spawnTickCounter += 1
                val spawnInterval = if (tdWave >= 100) 4 else if (tdWave >= 40) 5 else 6
                if (spawnTickCounter >= spawnInterval) {
                    spawnTickCounter = 0
                    enemiesRemainingToSpawn -= 1
                    val isLastSpawnOfWave = enemiesRemainingToSpawn == 0
                    val chosenKind = when {
                        isLastSpawnOfWave && tdWave % 10 == 0 -> DuoEnemyKind.CHRONO_LEVIATHAN_BOSS
                        isLastSpawnOfWave && tdWave % 5 == 0 -> DuoEnemyKind.QUANTUM_HYDRA_MINIBOSS
                        else -> {
                            val pool = buildList {
                                add(DuoEnemyKind.ASTEROID_SLIME)
                                add(DuoEnemyKind.NEBULA_DRONE)
                                if (tdWave >= 3) add(DuoEnemyKind.ARMOR_BEETLE)
                                if (tdWave >= 6) add(DuoEnemyKind.PLASMA_FOX_RAIDER)
                                if (tdWave >= 12) add(DuoEnemyKind.VOID_GOLEM)
                                if (tdWave >= 20) add(DuoEnemyKind.SOLAR_WYVERN)
                            }
                            pool[secureRandom.nextInt(pool.size)]
                        }
                    }
                    // Smoothly balanced HP curve so Wave 1..200 is challenging yet conquerable with Level 5-10 towers
                    val baseWaveHp = 34f * (1f + tdWave * 0.24f + (tdWave * tdWave) * 0.0028f)
                    val finalHp = baseWaveHp * chosenKind.hpMultiplier
                    enemyIdCounter += 1L
                    activeEnemies.add(
                        ActiveDuoEnemy(
                            instanceId = enemyIdCounter,
                            kind = chosenKind,
                            maxHp = finalHp,
                            currentHp = finalHp,
                            pathProgress = 0f
                        )
                    )
                }
            }

            // 2. Move enemies along path
            val maxProgress = (pathCells.size - 1).toFloat()
            val survivingEnemies = mutableListOf<ActiveDuoEnemy>()
            for (enemy in activeEnemies) {
                val slowFactor = if (enemy.slowTicksRemaining > 0) 0.45f else 1.0f
                val waveSpeedBoost = 1f + min(0.65f, tdWave * 0.0032f)
                val newProgress = enemy.pathProgress + (enemy.kind.speedCellsPerTick * slowFactor * waveSpeedBoost)
                if (newProgress >= maxProgress) {
                    val hpLoss = if (enemy.kind.isBoss) 3 else 1
                    tdBaseHp = (tdBaseHp - hpLoss).coerceAtLeast(0)
                } else {
                    survivingEnemies.add(
                        enemy.copy(
                            pathProgress = newProgress,
                            slowTicksRemaining = (enemy.slowTicksRemaining - 1).coerceAtLeast(0)
                        )
                    )
                }
            }

            // 3. Fade existing laser beams
            val remainingLasers = activeLasers.mapNotNull { fx ->
                if (fx.ticksLeft > 1) fx.copy(ticksLeft = fx.ticksLeft - 1) else null
            }
            activeLasers.clear()
            activeLasers.addAll(remainingLasers)

            // 4. Check base defeat
            if (tdBaseHp <= 0) {
                activeEnemies.clear()
                isWaveRunning = false
                onPostBanner("Markas Kristal Hancur di Wave $tdWave! Tekan Mulai Wave untuk mencoba lagi.")
                break
            }

            // 5. Tower targeting & firing
            val mutableEnemies = survivingEnemies.toMutableList()
            for (tIdx in placedTowers.indices) {
                val tower = placedTowers[tIdx]
                val cdStep = if (overdriveTicksRemaining > 0) 2 else 1
                if (tower.cooldownRemaining > 0) {
                    placedTowers[tIdx] = tower.copy(
                        cooldownRemaining = (tower.cooldownRemaining - cdStep).coerceAtLeast(0)
                    )
                    continue
                }
                if (mutableEnemies.isEmpty()) continue

                // Find furthest enemy within range
                val range = tower.effectiveRange
                var bestEnemyIdx = -1
                var bestProgress = -1f
                for (eIdx in mutableEnemies.indices) {
                    val e = mutableEnemies[eIdx]
                    val pos = cellPositionOfProgress(e.pathProgress)
                    val dist = hypot(pos.x - tower.gridX, pos.y - tower.gridY)
                    if (dist <= range && e.pathProgress > bestProgress) {
                        bestProgress = e.pathProgress
                        bestEnemyIdx = eIdx
                    }
                }

                if (bestEnemyIdx >= 0) {
                    val primaryTarget = mutableEnemies[bestEnemyIdx]
                    val targetPos = cellPositionOfProgress(primaryTarget.pathProgress)
                    activeLasers.add(
                        LaserShotFx(
                            fromGridX = tower.gridX.toFloat(),
                            fromGridY = tower.gridY.toFloat(),
                            toGridX = targetPos.x,
                            toGridY = targetPos.y,
                            color = tower.type.primaryColor,
                            ticksLeft = 2
                        )
                    )

                    var totalDealtThisShot = 0L
                    when (tower.type) {
                        DuoTowerType.PYRO_FOX -> {
                            // Splash damage around primary target
                            for (i in mutableEnemies.indices) {
                                val candidate = mutableEnemies[i]
                                val cPos = cellPositionOfProgress(candidate.pathProgress)
                                if (hypot(cPos.x - targetPos.x, cPos.y - targetPos.y) <= 1.35f) {
                                    val dmg = tower.effectiveDamage * (1f - candidate.kind.armorReduction * 0.5f)
                                    totalDealtThisShot += dmg.roundToInt()
                                    mutableEnemies[i] = candidate.copy(currentHp = candidate.currentHp - dmg)
                                }
                            }
                        }
                        DuoTowerType.TESLA_BEETLE -> {
                            // Chain lightning up to 4 targets
                            val chainIndices = mutableEnemies.indices
                                .sortedByDescending { mutableEnemies[it].pathProgress }
                                .take(4)
                            for (i in chainIndices) {
                                val candidate = mutableEnemies[i]
                                val dmg = tower.effectiveDamage // Armor piercing
                                totalDealtThisShot += dmg.roundToInt()
                                mutableEnemies[i] = candidate.copy(currentHp = candidate.currentHp - dmg)
                            }
                        }
                        DuoTowerType.FROST_MACAW -> {
                            val dmg = tower.effectiveDamage * (1f - primaryTarget.kind.armorReduction)
                            totalDealtThisShot += dmg.roundToInt()
                            mutableEnemies[bestEnemyIdx] = primaryTarget.copy(
                                currentHp = primaryTarget.currentHp - dmg,
                                slowTicksRemaining = 18 + tower.level * 2
                            )
                        }
                        else -> {
                            val armorPierce = if (tower.type == DuoTowerType.AEGIS_CARDINAL) 0.1f else 1f
                            val dmg = tower.effectiveDamage * (1f - primaryTarget.kind.armorReduction * armorPierce)
                            totalDealtThisShot += dmg.roundToInt()
                            mutableEnemies[bestEnemyIdx] = primaryTarget.copy(
                                currentHp = primaryTarget.currentHp - dmg
                            )
                        }
                    }

                    placedTowers[tIdx] = tower.copy(
                        cooldownRemaining = tower.type.cooldownTicks,
                        totalDamageDealt = tower.totalDamageDealt + totalDealtThisShot
                    )
                }
            }

            // 6. Collect defeated enemies & award Gold + Score
            activeEnemies.clear()
            for (e in mutableEnemies) {
                if (e.currentHp <= 0f) {
                    val waveGoldBonus = (e.kind.rewardGold * (1f + tdWave * 0.04f)).roundToInt()
                    tdGold += waveGoldBonus
                    tdScore += waveGoldBonus * 10L
                } else {
                    activeEnemies.add(e)
                }
            }

            // 7. Check Wave Clear
            if (enemiesRemainingToSpawn == 0 && activeEnemies.isEmpty()) {
                val solarCount = placedTowers.sumOf { if (it.type == DuoTowerType.SOLAR_BEE) it.level else 0 }
                val waveClearGold = 35 + tdWave * 4 + solarCount * 18
                tdGold += waveClearGold
                updateCoins(15 + (tdWave / 5) * 5)

                if (tdWave > highestWaveReached) {
                    highestWaveReached = tdWave
                    prefs.edit().putInt("td_highest_wave", highestWaveReached).apply()
                }

                if (tdWave == 200 && !wave200ConqueredBanner) {
                    wave200ConqueredBanner = true
                    prefs.edit().putBoolean("wave_200_conquered", true).apply()
                    updateCoins(1000)
                    onPostBanner("LUAR BIASA! Anda menaklukkan WAVE 200 Galaxy Tower Defense! Bonus +1.000 Koin Kosmik & Mode Endless Terbuka!")
                }

                if (autoStartNextWave && tdBaseHp > 0) {
                    tdWave += 1
                    enemiesRemainingToSpawn = (6 + (tdWave * 0.45f).roundToInt()).coerceIn(6, 55)
                    spawnTickCounter = 0
                } else {
                    tdWave += 1
                    isWaveRunning = false
                }
            }
        }
    }

    // =========================================================================
    // STATE SLOT GACHA KOSMIK 3D & KOLEKSI MASKOT
    // =========================================================================
    var slotReel1 by remember { mutableIntStateOf(0) }
    var slotReel2 by remember { mutableIntStateOf(1) }
    var slotReel3 by remember { mutableIntStateOf(2) }
    var isSpinningSlot by remember { mutableStateOf(false) }
    var slotBetAmount by remember { mutableIntStateOf(25) }
    var slotResultHeadline by remember {
        mutableStateOf("Putar Mesin Slot 3D Duolingo murni acak (SecureRandom) untuk memenangkan Jackpot Koin Kosmik!")
    }

    // =========================================================================
    // STATE TAROT KOSMIK 3D
    // =========================================================================
    var selectedTarotCardIndex by remember { mutableIntStateOf(-1) }
    var drawnTarotArcanaId by remember { mutableIntStateOf(0) }
    var isTarotRevealed by remember { mutableStateOf(false) }

    // =========================================================================
    // STATE KAPSUL WAKTU DIGITAL & BUKU HARIAN MOOD RAHASIA
    // =========================================================================
    var capsuleTitleInput by remember { mutableStateOf("") }
    var capsuleMessageInput by remember { mutableStateOf("") }
    var selectedMoodIndex by remember { mutableIntStateOf(0) }
    var selectedLockDurationOption by remember { mutableIntStateOf(0) } // 0=Langsung, 1=1m, 2=1h, 3=24h, 4=7d
    var currentClockMs by remember { mutableLongStateOf(System.currentTimeMillis()) }

    val capsuleEntries = remember {
        mutableStateListOf<MoodCapsuleEntry>().apply {
            val rawJson = prefs.getString("mood_capsules_json", null)
            if (!rawJson.isNullOrBlank()) {
                runCatching {
                    val arr = JSONArray(rawJson)
                    for (i in 0 until arr.length()) {
                        val obj = arr.getJSONObject(i)
                        add(
                            MoodCapsuleEntry(
                                id = obj.optLong("id", i.toLong()),
                                title = obj.optString("title", "Catatan Rahasia"),
                                message = obj.optString("message", ""),
                                moodIndex = obj.optInt("moodIndex", 0),
                                createdAtEpochMs = obj.optLong("createdAtEpochMs", System.currentTimeMillis()),
                                unlockAtEpochMs = obj.optLong("unlockAtEpochMs", System.currentTimeMillis())
                            )
                        )
                    }
                }
            } else {
                val now = System.currentTimeMillis()
                add(
                    MoodCapsuleEntry(
                        id = 1L,
                        title = "Kapsul Perdana Ruang Fun Zone",
                        message = "Selamat datang di Ruang Terisolasi Fun Zone! Semua kapsul waktu dan buku harian mood di sini terisolasi dengan PIN 3-lapis khusus.",
                        moodIndex = 0,
                        createdAtEpochMs = now - 3600_000L,
                        unlockAtEpochMs = now
                    )
                )
            }
        }
    }

    fun persistCapsules() {
        val arr = JSONArray()
        for (item in capsuleEntries) {
            val obj = JSONObject()
            obj.put("id", item.id)
            obj.put("title", item.title)
            obj.put("message", item.message)
            obj.put("moodIndex", item.moodIndex)
            obj.put("createdAtEpochMs", item.createdAtEpochMs)
            obj.put("unlockAtEpochMs", item.unlockAtEpochMs)
            arr.put(obj)
        }
        prefs.edit().putString("mood_capsules_json", arr.toString()).apply()
    }

    LaunchedEffect(activeFunTab) {
        while (activeFunTab == 3) {
            currentClockMs = System.currentTimeMillis()
            delay(1000L)
        }
    }

    val funZoneNavTabs = remember {
        listOf(
            Triple(4, "Dashboard 27 Game", Icons.Default.Bolt),
            Triple(0, "Galaxy TD (200 Wave)", Icons.Default.Shield),
            Triple(1, "Slot Gacha & Maskot 3D", Icons.Default.Casino),
            Triple(2, "Tarot Kosmik 3D", Icons.Default.Style),
            Triple(3, "Kapsul Waktu & Mood 3D", Icons.Default.AutoAwesome)
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .testTag("isolated_fun_zone_screen")
    ) {
    LazyColumn(
        modifier = Modifier
            .weight(1f)
            .fillMaxWidth(),
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 24.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // =========================================================================
        // HEADER BANNER 3D DUOLINGO: RUANG TERISOLASI FUN ZONE & ARCADE
        // =========================================================================
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .duolingoCard(
                        backgroundColor = DuoBeetlePurple,
                        borderColor = DuoBeetlePurpleLip,
                        lipColor = DuoBeetlePurpleLip,
                        cornerRadius = 26.dp,
                        borderWidth = 2.dp,
                        lipHeight = 6.dp
                    )
                    .padding(bottom = 6.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
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
                                    .size(48.dp)
                                    .duolingoButton(
                                        faceColor = DuoBeeYellow,
                                        lipColor = DuoBeeYellowLip,
                                        cornerRadius = 15.dp,
                                        lipHeight = 4.dp
                                    )
                                    .padding(bottom = 4.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Duo3DMascotBadgeCanvas(
                                    mascotId = 0,
                                    modifier = Modifier.size(34.dp)
                                )
                            }
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "RUANG TERISOLASI PIN 3-LAPIS KHUSUS",
                                    fontFamily = JetBrainsMonoFontFamily,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = DuoBeeYellow,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Text(
                                    text = "Nyx Fun Zone 3D & Kapsul Waktu",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = Color.White,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }

                        Box(
                            modifier = Modifier
                                .height(38.dp)
                                .duolingoButton(
                                    faceColor = DuoCardinalRed,
                                    lipColor = DuoCardinalRedLip,
                                    cornerRadius = 14.dp,
                                    lipHeight = 4.dp
                                )
                                .instantTouchPress { onLockAndExit() }
                                .padding(horizontal = 12.dp, vertical = 4.dp)
                                .testTag("fun_zone_lock_exit_button"),
                            contentAlignment = Alignment.Center
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(5.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Lock,
                                    contentDescription = "Kunci Ruang Fun Zone",
                                    tint = Color.White,
                                    modifier = Modifier.size(15.dp)
                                )
                                Text(
                                    text = "Kunci",
                                    style = MaterialTheme.typography.labelMedium,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = Color.White,
                                    maxLines = 1,
                                    softWrap = false
                                )
                            }
                        }
                    }

                    // Status Bar Koin Kosmik, Rekor Wave 200 & Koleksi Maskot (GSAP Rolling Counters)
                    val animatedCosmicCoins = rememberGsapRollingLong(cosmicCoins.toLong())
                    val animatedHighestWave = rememberGsapRollingLong(highestWaveReached.toLong())
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .duolingoCard(
                                    backgroundColor = DuoPureWhite,
                                    borderColor = DuoBeeYellow,
                                    lipColor = DuoBeeYellowLip,
                                    cornerRadius = 14.dp,
                                    borderWidth = 2.dp,
                                    lipHeight = 3.dp
                                )
                                .padding(bottom = 3.dp)
                                .padding(horizontal = 10.dp, vertical = 7.dp)
                        ) {
                            Column {
                                Text(
                                    text = "KOIN KOSMIK",
                                    fontFamily = JetBrainsMonoFontFamily,
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = DuoFoxOrangeLip,
                                    maxLines = 1
                                )
                                Text(
                                    text = "$animatedCosmicCoins KOIN",
                                    fontFamily = JetBrainsMonoFontFamily,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = VaultObsidian,
                                    maxLines = 1
                                )
                            }
                        }

                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .duolingoCard(
                                    backgroundColor = DuoPureWhite,
                                    borderColor = DuoFeatherGreen,
                                    lipColor = DuoFeatherGreenLip,
                                    cornerRadius = 14.dp,
                                    borderWidth = 2.dp,
                                    lipHeight = 3.dp
                                )
                                .padding(bottom = 3.dp)
                                .padding(horizontal = 10.dp, vertical = 7.dp)
                        ) {
                            Column {
                                Text(
                                    text = "REKOR GALAXY TD",
                                    fontFamily = JetBrainsMonoFontFamily,
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = DuoFeatherGreenLip,
                                    maxLines = 1
                                )
                                Text(
                                    text = "WAVE $animatedHighestWave / 200+",
                                    fontFamily = JetBrainsMonoFontFamily,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = VaultObsidian,
                                    maxLines = 1
                                )
                            }
                        }
                    }

                    // Active Sub-Tab Title Indicator (Navigation moved to locked colorful icon-only bottom dock)
                    val activeTabLabel = funZoneNavTabs.firstOrNull { it.first == activeFunTab }?.second ?: "Dashboard 27 Game"
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color.Black.copy(alpha = 0.22f))
                            .padding(horizontal = 12.dp, vertical = 6.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "MODE AKTIF: ${activeTabLabel.uppercase()}",
                            fontFamily = JetBrainsMonoFontFamily,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = DuoBeeYellow
                        )
                        Text(
                            text = "Pilih ikon warna di navigasi bawah",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White.copy(alpha = 0.85f)
                        )
                    }
                }
            }
        }

        when (activeFunTab) {
            // =========================================================================
            // SUB-TAB 0: GALAXY TOWER DEFENSE 3D (WAVE 1-200 + ENDLESS)
            // =========================================================================
            0 -> {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .duolingoCard(
                                backgroundColor = DuoPureWhite,
                                borderColor = DuoFeatherGreen,
                                lipColor = DuoFeatherGreenLip,
                                cornerRadius = 24.dp,
                                borderWidth = 2.dp,
                                lipHeight = 6.dp
                            )
                            .padding(bottom = 6.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(14.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            // Top HUD Bar
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = if (tdWave <= 200) {
                                            "GALAXY TOWER DEFENSE 3D • TARGET WAVE 200"
                                        } else {
                                            "MODE ENDLESS KOSMIK • WAVE $tdWave"
                                        },
                                        fontFamily = JetBrainsMonoFontFamily,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.ExtraBold,
                                        color = DuoFeatherGreenLip,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                    Text(
                                        text = "Wave $tdWave / 200 • Gold: $tdGold • HP Inti: $tdBaseHp",
                                        style = MaterialTheme.typography.titleSmall,
                                        fontWeight = FontWeight.ExtraBold,
                                        color = VaultObsidian,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                }

                                Box(
                                    modifier = Modifier
                                        .height(34.dp)
                                        .duolingoButton(
                                            faceColor = DuoMacawBlue,
                                            lipColor = DuoMacawBlueLip,
                                            cornerRadius = 12.dp,
                                            lipHeight = 3.dp
                                        )
                                        .instantTouchPress {
                                            tdSpeedMultiplier = when (tdSpeedMultiplier) {
                                                1 -> 2
                                                2 -> 4
                                                else -> 1
                                            }
                                        }
                                        .padding(bottom = 3.dp)
                                        .padding(horizontal = 10.dp)
                                        .testTag("td_speed_toggle_button"),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "KECEPATAN ${tdSpeedMultiplier}X",
                                        fontFamily = JetBrainsMonoFontFamily,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.ExtraBold,
                                        color = Color.White,
                                        maxLines = 1,
                                        softWrap = false
                                    )
                                }
                            }

                            // Interactive 3D Duolingo Tactical Grid Canvas (6x8)
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .aspectRatio(6f / 7.5f)
                                    .duolingoCard(
                                        backgroundColor = Color(0xFF0F172A),
                                        borderColor = DuoMacawBlue,
                                        lipColor = DuoMacawBlueLip,
                                        cornerRadius = 20.dp,
                                        borderWidth = 2.dp,
                                        lipHeight = 5.dp
                                    )
                                    .padding(bottom = 5.dp)
                                    .clip(RoundedCornerShape(18.dp))
                                    .pointerInput(placedTowers.size, selectedTowerType, tdGold) {
                                        detectTapGestures { tapOffset ->
                                            val cellW = size.width / gridCols.toFloat()
                                            val cellH = size.height / gridRows.toFloat()
                                            val gx = (tapOffset.x / cellW).toInt().coerceIn(0, gridCols - 1)
                                            val gy = (tapOffset.y / cellH).toInt().coerceIn(0, gridRows - 1)
                                            selectedGridCell = gx to gy

                                            val existingIdx = placedTowers.indexOfFirst { it.gridX == gx && it.gridY == gy }
                                            if (existingIdx < 0 && (gx to gy) !in pathSet) {
                                                // Auto-build selected tower if enough Gold
                                                if (tdGold >= selectedTowerType.baseCost) {
                                                    tdGold -= selectedTowerType.baseCost
                                                    placedTowers.add(
                                                        PlacedDuoTower(
                                                            gridX = gx,
                                                            gridY = gy,
                                                            type = selectedTowerType,
                                                            level = 1
                                                        )
                                                    )
                                                } else {
                                                    onPostBanner("Gold belum cukup untuk membangun ${selectedTowerType.title} (Butuh ${selectedTowerType.baseCost} Gold).")
                                                }
                                            }
                                        }
                                    }
                                    .testTag("galaxy_td_canvas")
                            ) {
                                Canvas(modifier = Modifier.fillMaxSize()) {
                                    val cellW = size.width / gridCols.toFloat()
                                    val cellH = size.height / gridRows.toFloat()

                                    // 1. Draw 3D Tactical Grid Tiles
                                    for (gx in 0 until gridCols) {
                                        for (gy in 0 until gridRows) {
                                            val left = gx * cellW
                                            val top = gy * cellH
                                            val isPath = (gx to gy) in pathSet
                                            val isSelected = selectedGridCell == (gx to gy)

                                            drawRoundRect(
                                                color = if (isPath) Color(0xFF1E293B) else Color(0xFF152238),
                                                topLeft = Offset(left + 2f, top + 2f),
                                                size = Size(cellW - 4f, cellH - 4f),
                                                cornerRadius = CornerRadius(10f, 10f)
                                            )
                                            if (!isPath) {
                                                // Subtle 3D platform lip on buildable cells
                                                drawRoundRect(
                                                    color = Color(0xFF1E3A5F),
                                                    topLeft = Offset(left + 4f, top + 4f),
                                                    size = Size(cellW - 8f, cellH - 10f),
                                                    cornerRadius = CornerRadius(8f, 8f)
                                                )
                                            }
                                            if (isSelected) {
                                                drawRoundRect(
                                                    color = DuoBeeYellow,
                                                    topLeft = Offset(left + 2f, top + 2f),
                                                    size = Size(cellW - 4f, cellH - 4f),
                                                    cornerRadius = CornerRadius(10f, 10f),
                                                    style = Stroke(width = 3.dp.toPx())
                                                )
                                            }
                                        }
                                    }

                                    // 2. Draw Star Path Track
                                    val trackPath = Path()
                                    pathCells.forEachIndexed { idx, (px, py) ->
                                        val cx = (px + 0.5f) * cellW
                                        val cy = (py + 0.5f) * cellH
                                        if (idx == 0) trackPath.moveTo(cx, cy) else trackPath.lineTo(cx, cy)
                                    }
                                    drawPath(
                                        path = trackPath,
                                        color = DuoMacawBlue.copy(alpha = 0.35f),
                                        style = Stroke(width = 16f, cap = StrokeCap.Round, join = StrokeJoin.Round)
                                    )
                                    drawPath(
                                        path = trackPath,
                                        color = DuoBeeYellow.copy(alpha = 0.55f),
                                        style = Stroke(width = 3.5f, cap = StrokeCap.Round, join = StrokeJoin.Round)
                                    )

                                    // 3. Draw Spawn Portal & Base Core Crystal (3D Duolingo Vector)
                                    val (spawnX, spawnY) = pathCells.first()
                                    drawDuo3DPortal(
                                        center = Offset((spawnX + 0.5f) * cellW, (spawnY + 0.5f) * cellH),
                                        radius = min(cellW, cellH) * 0.36f,
                                        primaryColor = DuoBeetlePurple,
                                        lipColor = DuoBeetlePurpleLip
                                    )
                                    val (baseX, baseY) = pathCells.last()
                                    drawDuo3DCoreCrystal(
                                        center = Offset((baseX + 0.5f) * cellW, (baseY + 0.5f) * cellH),
                                        radius = min(cellW, cellH) * 0.38f
                                    )

                                    // 4. Draw Selected Tower Range Ring
                                    selectedGridCell?.let { (sx, sy) ->
                                        val selTower = placedTowers.firstOrNull { it.gridX == sx && it.gridY == sy }
                                        if (selTower != null) {
                                            drawCircle(
                                                color = selTower.type.primaryColor.copy(alpha = 0.16f),
                                                radius = selTower.effectiveRange * cellW,
                                                center = Offset((sx + 0.5f) * cellW, (sy + 0.5f) * cellH)
                                            )
                                            drawCircle(
                                                color = selTower.type.primaryColor.copy(alpha = 0.7f),
                                                radius = selTower.effectiveRange * cellW,
                                                center = Offset((sx + 0.5f) * cellW, (sy + 0.5f) * cellH),
                                                style = Stroke(width = 2.dp.toPx())
                                            )
                                        }
                                    }

                                    // 5. Draw Placed Duolingo 3D Towers
                                    for (tower in placedTowers) {
                                        val cx = (tower.gridX + 0.5f) * cellW
                                        val cy = (tower.gridY + 0.5f) * cellH
                                        drawDuo3DTowerSprite(
                                            center = Offset(cx, cy),
                                            radius = min(cellW, cellH) * 0.38f,
                                            towerType = tower.type,
                                            level = tower.level
                                        )
                                    }

                                    // 6. Draw Active 3D Enemies & Bosses + Health Bars
                                    for (enemy in activeEnemies) {
                                        val pos = cellPositionOfProgress(enemy.pathProgress)
                                        val cx = (pos.x + 0.5f) * cellW
                                        val cy = (pos.y + 0.5f) * cellH
                                        val eRadius = min(cellW, cellH) * (if (enemy.kind.isBoss) 0.42f else 0.30f)
                                        drawDuo3DEnemySprite(
                                            center = Offset(cx, cy),
                                            radius = eRadius,
                                            kind = enemy.kind,
                                            isSlowed = enemy.slowTicksRemaining > 0
                                        )

                                        // 3D Health bar above enemy
                                        val barW = cellW * (if (enemy.kind.isBoss) 0.85f else 0.65f)
                                        val barH = if (enemy.kind.isBoss) 7f else 5f
                                        val barLeft = cx - barW / 2f
                                        val barTop = cy - eRadius - 10f
                                        val hpRatio = (enemy.currentHp / enemy.maxHp).coerceIn(0f, 1f)
                                        drawRoundRect(
                                            color = Color(0xFF090D16),
                                            topLeft = Offset(barLeft, barTop),
                                            size = Size(barW, barH),
                                            cornerRadius = CornerRadius(4f, 4f)
                                        )
                                        drawRoundRect(
                                            color = if (hpRatio > 0.5f) DuoFeatherGreen else if (hpRatio > 0.25f) DuoBeeYellow else DuoCardinalRed,
                                            topLeft = Offset(barLeft, barTop),
                                            size = Size(barW * hpRatio, barH),
                                            cornerRadius = CornerRadius(4f, 4f)
                                        )
                                    }

                                    // 7. Draw Active Laser & Plasma Beams
                                    for (fx in activeLasers) {
                                        val start = Offset((fx.fromGridX + 0.5f) * cellW, (fx.fromGridY + 0.5f) * cellH)
                                        val end = Offset((fx.toGridX + 0.5f) * cellW, (fx.toGridY + 0.5f) * cellH)
                                        drawLine(
                                            color = fx.color.copy(alpha = 0.45f),
                                            start = start,
                                            end = end,
                                            strokeWidth = 10f,
                                            cap = StrokeCap.Round
                                        )
                                        drawLine(
                                            color = Color.White,
                                            start = start,
                                            end = end,
                                            strokeWidth = 3.5f,
                                            cap = StrokeCap.Round
                                        )
                                    }
                                }
                            }

                            // Selected Grid Cell Inspector (Upgrade / Sell / Build)
                            val inspectedCell = selectedGridCell
                            val inspectedTower = inspectedCell?.let { (gx, gy) ->
                                placedTowers.firstOrNull { it.gridX == gx && it.gridY == gy }
                            }

                            if (inspectedTower != null) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .duolingoCard(
                                            backgroundColor = inspectedTower.type.softColor,
                                            borderColor = inspectedTower.type.primaryColor,
                                            lipColor = inspectedTower.type.lipColor,
                                            cornerRadius = 16.dp,
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
                                        Column(modifier = Modifier.weight(1f)) {
                                            Text(
                                                text = "${inspectedTower.type.title} • LEVEL ${inspectedTower.level}",
                                                style = MaterialTheme.typography.labelLarge,
                                                fontWeight = FontWeight.ExtraBold,
                                                color = VaultObsidian,
                                                maxLines = 1,
                                                overflow = TextOverflow.Ellipsis
                                            )
                                            Text(
                                                text = "DMG: ${inspectedTower.effectiveDamage.roundToInt()} • Jarak: ${"%.1f".format(inspectedTower.effectiveRange)} • Total: ${inspectedTower.totalDamageDealt}",
                                                fontFamily = JetBrainsMonoFontFamily,
                                                fontSize = 10.sp,
                                                color = VaultTextMuted,
                                                maxLines = 1,
                                                overflow = TextOverflow.Ellipsis
                                            )
                                        }

                                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                            // Upgrade Button (up to Level 15)
                                            Box(
                                                modifier = Modifier
                                                    .height(36.dp)
                                                    .duolingoButton(
                                                        faceColor = DuoFeatherGreen,
                                                        lipColor = DuoFeatherGreenLip,
                                                        cornerRadius = 12.dp,
                                                        lipHeight = 3.dp
                                                    )
                                                    .instantTouchPress {
                                                        val idx = placedTowers.indexOf(inspectedTower)
                                                        if (idx >= 0) {
                                                            val cost = inspectedTower.upgradeCost
                                                            if (tdGold >= cost) {
                                                                tdGold -= cost
                                                                placedTowers[idx] = inspectedTower.copy(level = inspectedTower.level + 1)
                                                            } else {
                                                                onPostBanner("Butuh $cost Gold untuk upgrade ${inspectedTower.type.title}!")
                                                            }
                                                        }
                                                    }
                                                    .padding(bottom = 3.dp)
                                                    .padding(horizontal = 10.dp),
                                                contentAlignment = Alignment.Center
                                            ) {
                                                Text(
                                                    text = "UPGRADE (${inspectedTower.upgradeCost}G)",
                                                    fontFamily = JetBrainsMonoFontFamily,
                                                    fontSize = 10.sp,
                                                    fontWeight = FontWeight.ExtraBold,
                                                    color = Color.White,
                                                    maxLines = 1,
                                                    softWrap = false
                                                )
                                            }

                                            // Sell Button
                                            Box(
                                                modifier = Modifier
                                                    .height(36.dp)
                                                    .duolingoButton(
                                                        faceColor = DuoCardinalRed,
                                                        lipColor = DuoCardinalRedLip,
                                                        cornerRadius = 12.dp,
                                                        lipHeight = 3.dp
                                                    )
                                                    .instantTouchPress {
                                                        tdGold += inspectedTower.sellRefund
                                                        placedTowers.remove(inspectedTower)
                                                    }
                                                    .padding(bottom = 3.dp)
                                                    .padding(horizontal = 10.dp),
                                                contentAlignment = Alignment.Center
                                            ) {
                                                Text(
                                                    text = "JUAL (+${inspectedTower.sellRefund}G)",
                                                    fontFamily = JetBrainsMonoFontFamily,
                                                    fontSize = 10.sp,
                                                    fontWeight = FontWeight.ExtraBold,
                                                    color = Color.White,
                                                    maxLines = 1,
                                                    softWrap = false
                                                )
                                            }
                                        }
                                    }
                                }
                            }

                            // 6 Duolingo 3D Tower Selector Cards
                            Text(
                                text = "PILIH VARIASI PERTAHANAN 3D (KETUK PETAK KOSONG UNTUK MEMBANGUN):",
                                fontFamily = JetBrainsMonoFontFamily,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = VaultTextMuted
                            )

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .horizontalScroll(rememberScrollState()),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                DuoTowerType.entries.forEach { towerType ->
                                    val isSelected = selectedTowerType == towerType
                                    Box(
                                        modifier = Modifier
                                            .width(126.dp)
                                            .duolingoCard(
                                                backgroundColor = if (isSelected) towerType.softColor else DuoPureWhite,
                                                borderColor = if (isSelected) towerType.primaryColor else DuoSwanBorder,
                                                lipColor = if (isSelected) towerType.lipColor else DuoHareLip,
                                                cornerRadius = 16.dp,
                                                borderWidth = 2.dp,
                                                lipHeight = 4.dp
                                            )
                                            .instantTouchPress { selectedTowerType = towerType }
                                            .padding(bottom = 4.dp)
                                            .padding(10.dp)
                                            .testTag("select_tower_${towerType.id}")
                                    ) {
                                        Column(
                                            horizontalAlignment = Alignment.CenterHorizontally,
                                            verticalArrangement = Arrangement.spacedBy(4.dp),
                                            modifier = Modifier.fillMaxWidth()
                                        ) {
                                            Canvas(modifier = Modifier.size(38.dp)) {
                                                drawDuo3DTowerSprite(
                                                    center = Offset(size.width / 2f, size.height / 2f),
                                                    radius = size.minDimension * 0.42f,
                                                    towerType = towerType,
                                                    level = 1
                                                )
                                            }
                                            Text(
                                                text = towerType.title,
                                                style = MaterialTheme.typography.labelSmall,
                                                fontWeight = FontWeight.ExtraBold,
                                                color = VaultObsidian,
                                                maxLines = 1,
                                                overflow = TextOverflow.Ellipsis
                                            )
                                            Text(
                                                text = towerType.roleLabel,
                                                fontSize = 9.sp,
                                                color = VaultTextMuted,
                                                maxLines = 1,
                                                overflow = TextOverflow.Ellipsis
                                            )
                                            Text(
                                                text = "${towerType.baseCost} GOLD",
                                                fontFamily = JetBrainsMonoFontFamily,
                                                fontSize = 10.sp,
                                                fontWeight = FontWeight.ExtraBold,
                                                color = towerType.lipColor,
                                                maxLines = 1
                                            )
                                        }
                                    }
                                }
                            }

                            // Action Controls: Start/Pause Wave, Auto-Wave, Active Skills
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .weight(1.2f)
                                        .height(46.dp)
                                        .duolingoButton(
                                            faceColor = if (isWaveRunning) DuoFoxOrange else DuoFeatherGreen,
                                            lipColor = if (isWaveRunning) DuoFoxOrangeLip else DuoFeatherGreenLip,
                                            cornerRadius = 14.dp,
                                            lipHeight = 4.dp
                                        )
                                        .instantTouchPress {
                                            if (isWaveRunning) {
                                                isWaveRunning = false
                                            } else {
                                                startTowerDefenseWave(tdWave)
                                            }
                                        }
                                        .padding(bottom = 4.dp)
                                        .testTag("td_start_pause_wave_button"),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                                    ) {
                                        Icon(
                                            imageVector = if (isWaveRunning) Icons.Default.FastForward else Icons.Default.PlayArrow,
                                            contentDescription = null,
                                            tint = Color.White,
                                            modifier = Modifier.size(18.dp)
                                        )
                                        Text(
                                            text = if (isWaveRunning) "JEDA WAVE $tdWave" else "MULAI WAVE $tdWave",
                                            style = MaterialTheme.typography.labelLarge,
                                            fontWeight = FontWeight.ExtraBold,
                                            color = Color.White,
                                            maxLines = 1,
                                            softWrap = false
                                        )
                                    }
                                }

                                // Tactical Skill: Meteor Strike (100 Gold)
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(46.dp)
                                        .duolingoButton(
                                            faceColor = DuoCardinalRed,
                                            lipColor = DuoCardinalRedLip,
                                            cornerRadius = 14.dp,
                                            lipHeight = 4.dp
                                        )
                                        .instantTouchPress {
                                            if (tdGold >= 100 && activeEnemies.isNotEmpty()) {
                                                tdGold -= 100
                                                for (i in activeEnemies.indices) {
                                                    val e = activeEnemies[i]
                                                    activeEnemies[i] = e.copy(
                                                        currentHp = e.currentHp - (e.maxHp * 0.45f),
                                                        slowTicksRemaining = 20
                                                    )
                                                }
                                                onPostBanner("Hujan Meteor 3D menghantam seluruh musuh (-45% HP + Slow)!")
                                            } else {
                                                onPostBanner("Butuh 100 Gold & musuh aktif untuk memanggil Hujan Meteor 3D!")
                                            }
                                        }
                                        .padding(bottom = 4.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "METEOR (100G)",
                                        fontFamily = JetBrainsMonoFontFamily,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.ExtraBold,
                                        color = Color.White,
                                        maxLines = 1,
                                        softWrap = false
                                    )
                                }

                                // Tactical Skill: Overdrive 2x Speed (80 Gold)
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
                                            if (tdGold >= 80) {
                                                tdGold -= 80
                                                overdriveTicksRemaining = 60
                                                onPostBanner("Overdrive 2X Kecepatan Tembak aktif untuk seluruh Tower!")
                                            } else {
                                                onPostBanner("Butuh 80 Gold untuk mengaktifkan Overdrive 2X!")
                                            }
                                        }
                                        .padding(bottom = 4.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "OVERDRIVE (80G)",
                                        fontFamily = JetBrainsMonoFontFamily,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.ExtraBold,
                                        color = VaultObsidian,
                                        maxLines = 1,
                                        softWrap = false
                                    )
                                }
                            }

                            // Wave Checkpoint Selector (1 -> 50 -> 100 -> 150 -> 195 -> 200)
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .horizontalScroll(rememberScrollState()),
                                horizontalArrangement = Arrangement.spacedBy(6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "CHECKPOINT WAVE:",
                                    fontFamily = JetBrainsMonoFontFamily,
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = VaultTextMuted
                                )
                                listOf(1, 25, 50, 100, 150, 195, 200).forEach { cpWave ->
                                    Box(
                                        modifier = Modifier
                                            .height(30.dp)
                                            .duolingoButton(
                                                faceColor = if (tdWave == cpWave) DuoBeetlePurple else DuoBeetlePurpleSoft,
                                                lipColor = DuoBeetlePurpleLip,
                                                cornerRadius = 10.dp,
                                                lipHeight = 3.dp
                                            )
                                            .instantTouchPress { jumpToCheckpointWave(cpWave) }
                                            .padding(bottom = 3.dp)
                                            .padding(horizontal = 9.dp)
                                            .testTag("td_checkpoint_wave_$cpWave"),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = "Wave $cpWave",
                                            fontFamily = JetBrainsMonoFontFamily,
                                            fontSize = 9.sp,
                                            fontWeight = FontWeight.ExtraBold,
                                            color = if (tdWave == cpWave) Color.White else VaultObsidian,
                                            maxLines = 1,
                                            softWrap = false
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // =========================================================================
            // SUB-TAB 1: SLOT GACHA KOSMIK 3D & KOLEKSI MASKOT DUOLINGO
            // =========================================================================
            1 -> {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .duolingoCard(
                                backgroundColor = DuoPureWhite,
                                borderColor = DuoFoxOrange,
                                lipColor = DuoFoxOrangeLip,
                                cornerRadius = 24.dp,
                                borderWidth = 2.dp,
                                lipHeight = 6.dp
                            )
                            .padding(bottom = 6.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Text(
                                text = "MESIN SLOT GACHA KOSMIK 3D • MURNI ACAK (SECURERANDOM)",
                                fontFamily = JetBrainsMonoFontFamily,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = DuoFoxOrangeLip
                            )
                            Text(
                                text = "Putar 3 Reel Karakter 3D Duolingo & Menangkan Jackpot!",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.ExtraBold,
                                color = VaultObsidian
                            )

                            // 3D Slot Machine Frame with 3 Custom Canvas Reels
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .duolingoCard(
                                        backgroundColor = Color(0xFF1E1B4B),
                                        borderColor = DuoBeeYellow,
                                        lipColor = DuoBeeYellowLip,
                                        cornerRadius = 22.dp,
                                        borderWidth = 3.dp,
                                        lipHeight = 6.dp
                                    )
                                    .padding(bottom = 6.dp)
                                    .padding(16.dp)
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                                ) {
                                    listOf(slotReel1, slotReel2, slotReel3).forEachIndexed { idx, symbolId ->
                                        Box(
                                            modifier = Modifier
                                                .weight(1f)
                                                .height(112.dp)
                                                .duolingoCard(
                                                    backgroundColor = DuoPureWhite,
                                                    borderColor = DuoMacawBlue,
                                                    lipColor = DuoMacawBlueLip,
                                                    cornerRadius = 18.dp,
                                                    borderWidth = 2.dp,
                                                    lipHeight = 5.dp
                                                )
                                                .padding(bottom = 5.dp),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Duo3DSlotSymbolCanvas(
                                                symbolId = symbolId,
                                                modifier = Modifier
                                                    .size(72.dp)
                                                    .testTag("slot_reel_$idx")
                                            )
                                        }
                                    }
                                }
                            }

                            Text(
                                text = slotResultHeadline,
                                style = MaterialTheme.typography.bodySmall,
                                fontWeight = FontWeight.ExtraBold,
                                color = VaultObsidian
                            )

                            // Bet Amount Chips + Spin Button
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                listOf(10, 25, 50, 100).forEach { bet ->
                                    val isSelected = slotBetAmount == bet
                                    Box(
                                        modifier = Modifier
                                            .weight(1f)
                                            .height(38.dp)
                                            .duolingoButton(
                                                faceColor = if (isSelected) DuoBeeYellow else DuoBeeYellowSoft,
                                                lipColor = DuoBeeYellowLip,
                                                cornerRadius = 12.dp,
                                                lipHeight = 3.dp
                                            )
                                            .instantTouchPress { slotBetAmount = bet }
                                            .padding(bottom = 3.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = "$bet Koin",
                                            fontFamily = JetBrainsMonoFontFamily,
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.ExtraBold,
                                            color = VaultObsidian,
                                            maxLines = 1,
                                            softWrap = false
                                        )
                                    }
                                }
                            }

                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(50.dp)
                                    .duolingoButton(
                                        faceColor = DuoFeatherGreen,
                                        lipColor = DuoFeatherGreenLip,
                                        cornerRadius = 16.dp,
                                        lipHeight = 5.dp
                                    )
                                    .instantTouchPress {
                                        if (isSpinningSlot) return@instantTouchPress
                                        if (cosmicCoins < slotBetAmount) {
                                            updateCoins(100)
                                            slotResultHeadline = "Koin habis! Subsidi Kosmik +100 Koin diberikan otomatis agar Anda tetap bisa bermain."
                                            return@instantTouchPress
                                        }
                                        isSpinningSlot = true
                                        updateCoins(-slotBetAmount)
                                        coroutineScope.launch {
                                            repeat(12) {
                                                slotReel1 = secureRandom.nextInt(6)
                                                slotReel2 = secureRandom.nextInt(6)
                                                slotReel3 = secureRandom.nextInt(6)
                                                delay(55L)
                                            }
                                            val r1 = secureRandom.nextInt(6)
                                            val r2 = secureRandom.nextInt(6)
                                            val r3 = secureRandom.nextInt(6)
                                            slotReel1 = r1
                                            slotReel2 = r2
                                            slotReel3 = r3

                                            if (r1 == r2 && r2 == r3) {
                                                val mult = if (r1 == 0) 15 else 10
                                                val prize = slotBetAmount * mult
                                                updateCoins(prize)
                                                unlockMascot(r1)
                                                slotResultHeadline = "MEGA JACKPOT 3 KEMBAR! Anda memenangkan +$prize Koin Kosmik & membuka Maskot 3D #${r1 + 1}!"
                                            } else if (r1 == r2 || r2 == r3 || r1 == r3) {
                                                val prize = (slotBetAmount * 2.5f).roundToInt()
                                                updateCoins(prize)
                                                val unlockedId = if (r1 == r2) r1 else r3
                                                unlockMascot(unlockedId)
                                                slotResultHeadline = "MENANG 2 SIMBOL KEMBAR! Hadiah +$prize Koin Kosmik & Maskot 3D #${unlockedId + 1}!"
                                            } else {
                                                slotResultHeadline = "Belum kembar! Coba putar lagi untuk meraih Mega Jackpot 15X."
                                            }
                                            isSpinningSlot = false
                                        }
                                    }
                                    .padding(bottom = 5.dp)
                                    .testTag("slot_spin_button"),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = if (isSpinningSlot) "MEMUTAR REEL 3D..." else "PUTAR SLOT GACHA 3D ($slotBetAmount KOIN)",
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = Color.White,
                                    maxLines = 1,
                                    softWrap = false
                                )
                            }

                            // Mascot Gacha Collection Showcase
                            Text(
                                text = "KOLEKSI MASKOT 3D RAHASIA NYX (6 KARAKTER):",
                                fontFamily = JetBrainsMonoFontFamily,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = VaultTextMuted
                            )

                            val unlockedSet = remember(unlockedMascotCsv) {
                                unlockedMascotCsv.split(",").mapNotNull { it.trim().toIntOrNull() }.toSet()
                            }
                            val mascotNames = listOf(
                                "Owl Emerald",
                                "Nyx Starlight",
                                "Macaw Cryo",
                                "Fox Supernova",
                                "Beetle Tesla",
                                "Bear Aegis"
                            )

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .horizontalScroll(rememberScrollState()),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                for (mId in 0 until 6) {
                                    val isUnlocked = mId in unlockedSet
                                    Box(
                                        modifier = Modifier
                                            .width(114.dp)
                                            .duolingoCard(
                                                backgroundColor = if (isUnlocked) DuoFeatherGreenSoft else DuoPureWhite,
                                                borderColor = if (isUnlocked) DuoFeatherGreen else DuoSwanBorder,
                                                lipColor = if (isUnlocked) DuoFeatherGreenLip else DuoHareLip,
                                                cornerRadius = 16.dp,
                                                borderWidth = 2.dp,
                                                lipHeight = 4.dp
                                            )
                                            .padding(bottom = 4.dp)
                                            .padding(10.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Column(
                                            horizontalAlignment = Alignment.CenterHorizontally,
                                            verticalArrangement = Arrangement.spacedBy(4.dp)
                                        ) {
                                            Duo3DMascotBadgeCanvas(
                                                mascotId = mId,
                                                modifier = Modifier.size(44.dp)
                                            )
                                            Text(
                                                text = mascotNames[mId],
                                                style = MaterialTheme.typography.labelSmall,
                                                fontWeight = FontWeight.ExtraBold,
                                                color = VaultObsidian,
                                                maxLines = 1,
                                                overflow = TextOverflow.Ellipsis
                                            )
                                            Text(
                                                text = if (isUnlocked) "TERBUKA" else "TERKUNCI",
                                                fontFamily = JetBrainsMonoFontFamily,
                                                fontSize = 9.sp,
                                                fontWeight = FontWeight.ExtraBold,
                                                color = if (isUnlocked) DuoFeatherGreenLip else VaultTextMuted
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // =========================================================================
            // SUB-TAB 2: GACHA TAROT KOSMIK 3D
            // =========================================================================
            2 -> {
                item {
                    val tarotTitles = listOf(
                        "I • THE STAR OWL (KEBIJAKSANAAN)",
                        "II • THE GOLDEN SUN (KEBERUNTUNGAN)",
                        "III • THE CYBER WHEEL (MOMENTUM)",
                        "IV • THE AEGIS SHIELD (PERLINDUNGAN)",
                        "V • THE TWIN COMETS (SINERGI)",
                        "VI • THE EMERALD TOWER (KETANGGUHAN)"
                    )
                    val tarotFortunes = listOf(
                        "Insting analitis Anda sedang berada di puncak. Keputusan besar hari ini membawa hasil cemerlang (+60 Koin Kosmik).",
                        "Energi surya menyinari langkah Anda! Keberuntungan tak terduga hadir di tengah tantangan (+85 Koin Kosmik).",
                        "Roda kosmik berputar cepat. Waktu terbaik untuk memulai proyek kreatif baru (+70 Koin Kosmik).",
                        "Benteng privasi dan ketenangan batin Anda sangat kokoh. Fokus pada prioritas utama (+65 Koin Kosmik).",
                        "Kolaborasi dan ide segar berpadu sempurna. Dua peluang baik datang bersamaan (+75 Koin Kosmik).",
                        "Ketekunan Anda membangun fondasi yang tak tergoyahkan hingga Wave tertinggi (+90 Koin Kosmik)."
                    )

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .duolingoCard(
                                backgroundColor = DuoPureWhite,
                                borderColor = DuoMacawBlue,
                                lipColor = DuoMacawBlueLip,
                                cornerRadius = 24.dp,
                                borderWidth = 2.dp,
                                lipHeight = 6.dp
                            )
                            .padding(bottom = 6.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Text(
                                text = "GACHA TAROT KOSMIK 3D • RAMALAN & BUFF HARIAN",
                                fontFamily = JetBrainsMonoFontFamily,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = DuoMacawBlueLip
                            )
                            Text(
                                text = "Pilih 1 dari 3 Kartu Arcana 3D untuk Membuka Pesan Kosmik",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.ExtraBold,
                                color = VaultObsidian
                            )

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                for (cardSlot in 0..2) {
                                    val isChosen = selectedTarotCardIndex == cardSlot && isTarotRevealed
                                    Box(
                                        modifier = Modifier
                                            .weight(1f)
                                            .height(156.dp)
                                            .duolingoCard(
                                                backgroundColor = if (isChosen) DuoBeeYellowSoft else Color(0xFF1E1B4B),
                                                borderColor = if (isChosen) DuoBeeYellow else DuoBeetlePurple,
                                                lipColor = if (isChosen) DuoBeeYellowLip else DuoBeetlePurpleLip,
                                                cornerRadius = 18.dp,
                                                borderWidth = 2.dp,
                                                lipHeight = 5.dp
                                            )
                                            .instantTouchPress {
                                                selectedTarotCardIndex = cardSlot
                                                drawnTarotArcanaId = secureRandom.nextInt(tarotTitles.size)
                                                isTarotRevealed = true
                                                updateCoins(65)
                                            }
                                            .padding(bottom = 5.dp)
                                            .padding(10.dp)
                                            .testTag("tarot_card_slot_$cardSlot"),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Column(
                                            horizontalAlignment = Alignment.CenterHorizontally,
                                            verticalArrangement = Arrangement.spacedBy(6.dp)
                                        ) {
                                            Duo3DSlotSymbolCanvas(
                                                symbolId = if (isChosen) drawnTarotArcanaId else cardSlot,
                                                modifier = Modifier.size(64.dp)
                                            )
                                            Text(
                                                text = if (isChosen) "TERBUKA" else "ARCANA #${cardSlot + 1}",
                                                fontFamily = JetBrainsMonoFontFamily,
                                                fontSize = 10.sp,
                                                fontWeight = FontWeight.ExtraBold,
                                                color = if (isChosen) VaultObsidian else DuoBeeYellow,
                                                maxLines = 1
                                            )
                                        }
                                    }
                                }
                            }

                            if (isTarotRevealed) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .duolingoCard(
                                            backgroundColor = DuoMacawBlueSoft,
                                            borderColor = DuoMacawBlue,
                                            lipColor = DuoMacawBlueLip,
                                            cornerRadius = 16.dp,
                                            borderWidth = 2.dp,
                                            lipHeight = 4.dp
                                        )
                                        .padding(bottom = 4.dp)
                                        .padding(14.dp)
                                ) {
                                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                        Text(
                                            text = tarotTitles[drawnTarotArcanaId],
                                            style = MaterialTheme.typography.titleSmall,
                                            fontWeight = FontWeight.ExtraBold,
                                            color = DuoMacawBlueLip
                                        )
                                        Text(
                                            text = tarotFortunes[drawnTarotArcanaId],
                                            style = MaterialTheme.typography.bodySmall,
                                            fontWeight = FontWeight.Bold,
                                            color = VaultObsidian
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // =========================================================================
            // SUB-TAB 3: KAPSUL WAKTU DIGITAL & BUKU HARIAN MOOD RAHASIA 3D
            // =========================================================================
            3 -> {
                item {
                    val moodLabels = listOf("Gembira", "Semangat", "Tenang", "Fokus", "Lelah", "Galau")
                    val lockOptions = listOf(
                        "Buka Langsung" to 0L,
                        "Kunci 1 Menit" to 60_000L,
                        "Kunci 1 Jam" to 3_600_000L,
                        "Kunci 24 Jam" to 86_400_000L
                    )

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .duolingoCard(
                                backgroundColor = DuoPureWhite,
                                borderColor = DuoBeeYellow,
                                lipColor = DuoBeeYellowLip,
                                cornerRadius = 24.dp,
                                borderWidth = 2.dp,
                                lipHeight = 6.dp
                            )
                            .padding(bottom = 6.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Text(
                                text = "KAPSUL WAKTU DIGITAL & BUKU HARIAN MOOD 3D",
                                fontFamily = JetBrainsMonoFontFamily,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = DuoFoxOrangeLip
                            )
                            Text(
                                text = "Tulis Catatan Rahasia & Kunci dengan Timer Waktu",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.ExtraBold,
                                color = VaultObsidian
                            )

                            // 6 Duolingo 3D Mood Character Faces (Custom Canvas, Zero Emoji)
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .horizontalScroll(rememberScrollState()),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                moodLabels.forEachIndexed { mIdx, label ->
                                    val isSelected = selectedMoodIndex == mIdx
                                    Box(
                                        modifier = Modifier
                                            .width(86.dp)
                                            .duolingoCard(
                                                backgroundColor = if (isSelected) DuoBeeYellowSoft else DuoPureWhite,
                                                borderColor = if (isSelected) DuoFoxOrange else DuoSwanBorder,
                                                lipColor = if (isSelected) DuoFoxOrangeLip else DuoHareLip,
                                                cornerRadius = 14.dp,
                                                borderWidth = 2.dp,
                                                lipHeight = 3.dp
                                            )
                                            .instantTouchPress { selectedMoodIndex = mIdx }
                                            .padding(bottom = 3.dp)
                                            .padding(8.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Column(
                                            horizontalAlignment = Alignment.CenterHorizontally,
                                            verticalArrangement = Arrangement.spacedBy(4.dp)
                                        ) {
                                            Duo3DMoodFaceCanvas(
                                                moodIndex = mIdx,
                                                modifier = Modifier.size(34.dp)
                                            )
                                            Text(
                                                text = label,
                                                style = MaterialTheme.typography.labelSmall,
                                                fontWeight = FontWeight.ExtraBold,
                                                color = VaultObsidian,
                                                maxLines = 1
                                            )
                                        }
                                    }
                                }
                            }

                            OutlinedTextField(
                                value = capsuleTitleInput,
                                onValueChange = { capsuleTitleInput = it },
                                label = { Text("Judul Kapsul Waktu / Mood") },
                                singleLine = true,
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(14.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = DuoFoxOrange,
                                    unfocusedBorderColor = DuoSwanBorder,
                                    focusedTextColor = VaultObsidian,
                                    unfocusedTextColor = VaultObsidian
                                )
                            )

                            OutlinedTextField(
                                value = capsuleMessageInput,
                                onValueChange = { capsuleMessageInput = it },
                                label = { Text("Isi Pesan Masa Depan / Curhatan Rahasia") },
                                minLines = 2,
                                maxLines = 4,
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(14.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = DuoFoxOrange,
                                    unfocusedBorderColor = DuoSwanBorder,
                                    focusedTextColor = VaultObsidian,
                                    unfocusedTextColor = VaultObsidian
                                )
                            )

                            // Lock Duration Selector
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .horizontalScroll(rememberScrollState()),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                lockOptions.forEachIndexed { idx, (label, _) ->
                                    val isSelected = selectedLockDurationOption == idx
                                    Box(
                                        modifier = Modifier
                                            .height(36.dp)
                                            .duolingoButton(
                                                faceColor = if (isSelected) DuoBeetlePurple else DuoBeetlePurpleSoft,
                                                lipColor = DuoBeetlePurpleLip,
                                                cornerRadius = 12.dp,
                                                lipHeight = 3.dp
                                            )
                                            .instantTouchPress { selectedLockDurationOption = idx }
                                            .padding(bottom = 3.dp)
                                            .padding(horizontal = 10.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = label,
                                            fontFamily = JetBrainsMonoFontFamily,
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.ExtraBold,
                                            color = if (isSelected) Color.White else VaultObsidian,
                                            maxLines = 1,
                                            softWrap = false
                                        )
                                    }
                                }
                            }

                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(46.dp)
                                    .duolingoButton(
                                        faceColor = DuoFeatherGreen,
                                        lipColor = DuoFeatherGreenLip,
                                        cornerRadius = 14.dp,
                                        lipHeight = 4.dp
                                    )
                                    .instantTouchPress {
                                        val cleanTitle = capsuleTitleInput.trim().ifBlank { "Kapsul Mood #${capsuleEntries.size + 1}" }
                                        val cleanMsg = capsuleMessageInput.trim()
                                        if (cleanMsg.isBlank()) {
                                            onPostBanner("Isi pesan kapsul waktu tidak boleh kosong!")
                                            return@instantTouchPress
                                        }
                                        val now = System.currentTimeMillis()
                                        val lockDelta = lockOptions[selectedLockDurationOption].second
                                        capsuleEntries.add(
                                            0,
                                            MoodCapsuleEntry(
                                                id = now,
                                                title = cleanTitle,
                                                message = cleanMsg,
                                                moodIndex = selectedMoodIndex,
                                                createdAtEpochMs = now,
                                                unlockAtEpochMs = now + lockDelta
                                            )
                                        )
                                        persistCapsules()
                                        capsuleTitleInput = ""
                                        capsuleMessageInput = ""
                                        updateCoins(25)
                                        onPostBanner("Kapsul Waktu berhasil disegel (+25 Koin Kosmik)!")
                                    }
                                    .padding(bottom = 4.dp)
                                    .testTag("save_mood_capsule_button"),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "SEGEL KAPSUL WAKTU & SIMPAN (+25 KOIN)",
                                    style = MaterialTheme.typography.labelLarge,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = Color.White,
                                    maxLines = 1,
                                    softWrap = false
                                )
                            }

                            // List of Saved Capsules
                            capsuleEntries.forEach { entry ->
                                val isUnlocked = currentClockMs >= entry.unlockAtEpochMs
                                val remainingSec = ((entry.unlockAtEpochMs - currentClockMs) / 1000L).coerceAtLeast(0L)
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .duolingoCard(
                                            backgroundColor = if (isUnlocked) DuoFeatherGreenSoft else DuoBeetlePurpleSoft,
                                            borderColor = if (isUnlocked) DuoFeatherGreen else DuoBeetlePurple,
                                            lipColor = if (isUnlocked) DuoFeatherGreenLip else DuoBeetlePurpleLip,
                                            cornerRadius = 16.dp,
                                            borderWidth = 2.dp,
                                            lipHeight = 4.dp
                                        )
                                        .padding(bottom = 4.dp)
                                        .padding(12.dp)
                                ) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Duo3DMoodFaceCanvas(
                                            moodIndex = entry.moodIndex,
                                            modifier = Modifier.size(38.dp)
                                        )
                                        Column(modifier = Modifier.weight(1f)) {
                                            Text(
                                                text = "${entry.title} • Mood: ${moodLabels[entry.moodIndex.coerceIn(0, 5)]}",
                                                style = MaterialTheme.typography.labelLarge,
                                                fontWeight = FontWeight.ExtraBold,
                                                color = VaultObsidian,
                                                maxLines = 1,
                                                overflow = TextOverflow.Ellipsis
                                            )
                                            Text(
                                                text = if (isUnlocked) {
                                                    entry.message
                                                } else {
                                                    "TERSEGEL WAKTU • Terbuka otomatis dalam ${remainingSec} detik"
                                                },
                                                style = MaterialTheme.typography.bodySmall,
                                                fontWeight = FontWeight.Bold,
                                                color = if (isUnlocked) VaultObsidian else DuoBeetlePurpleLip
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // =========================================================================
            // SUB-TAB 4: DASHBOARD 27 GAME ASAH OTAK, PUZZLE, STRATEGI & ARCADE 3D
            // =========================================================================
            4 -> {
                item {
                    BrainPuzzleGamesDashboardHub(
                        prefs = prefs,
                        cosmicCoins = cosmicCoins,
                        onUpdateCoins = ::updateCoins,
                        onUnlockMascot = ::unlockMascot,
                        onPostBanner = onPostBanner,
                        onOpenTowerDefenseClick = { activeFunTab = 0 }
                    )
                }
            }
        }
    }

        // LOCKED COLORFUL ICON-ONLY BOTTOM NAVIGATION BAR FOR FUN ZONE
        val activeBorderColor = when (activeFunTab) {
            4 -> DuoFeatherGreen
            0 -> DuoMacawBlue
            1 -> DuoFoxOrange
            2 -> DuoBeetlePurple
            else -> DuoBeeYellow
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(horizontal = 12.dp, vertical = 6.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
                    .duolingoCard(
                        backgroundColor = com.example.ui.theme.CosmicObsidian,
                        borderColor = activeBorderColor,
                        lipColor = DuoHareLip,
                        cornerRadius = 22.dp,
                        borderWidth = 2.dp,
                        lipHeight = 4.dp
                    )
                    .padding(bottom = 4.dp, start = 8.dp, end = 8.dp)
                    .testTag("fun_zone_bottom_nav_dock"),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp, Alignment.CenterHorizontally),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    funZoneNavTabs.forEachIndexed { idx, (tabId, label, icon) ->
                        val isSelected = activeFunTab == tabId
                        val (faceColor, lipColor) = when (idx) {
                            0 -> DuoFeatherGreen to DuoFeatherGreenLip
                            1 -> DuoMacawBlue to DuoMacawBlueLip
                            2 -> DuoFoxOrange to DuoFoxOrangeLip
                            3 -> DuoBeetlePurple to DuoBeetlePurpleLip
                            else -> DuoBeeYellow to DuoBeeYellowLip
                        }
                        val boxShape = RoundedCornerShape(14.dp)
                        val iconTint = when {
                            isSelected && faceColor == DuoBeeYellow -> Color(0xFF1E293B)
                            isSelected -> Color.White
                            else -> faceColor
                        }
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(44.dp)
                                .then(
                                    if (isSelected) {
                                        Modifier
                                            .duolingoButton(
                                                faceColor = faceColor,
                                                lipColor = lipColor,
                                                cornerRadius = 14.dp,
                                                lipHeight = 3.dp
                                            )
                                            .padding(bottom = 3.dp)
                                    } else {
                                        Modifier
                                            .clip(boxShape)
                                            .background(faceColor.copy(alpha = 0.20f))
                                            .border(1.5.dp, faceColor.copy(alpha = 0.65f), boxShape)
                                    }
                                )
                                .instantTouchPress {
                                    activeFunTab = tabId
                                    com.example.ui.theme.AppThemeController.gsapLiveDemoTriggerNonce++
                                }
                                .testTag("fun_zone_tab_$tabId"),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = icon,
                                contentDescription = label,
                                tint = iconTint,
                                modifier = Modifier.size(if (isSelected) 21.dp else 19.dp)
                            )
                        }
                    }

                    // Exit / Lock Colorful Red Box inside Fun Zone Bottom Bar
                    val exitBoxShape = RoundedCornerShape(14.dp)
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(44.dp)
                            .clip(exitBoxShape)
                            .background(DuoCardinalRed.copy(alpha = 0.20f))
                            .border(1.5.dp, DuoCardinalRed.copy(alpha = 0.65f), exitBoxShape)
                            .instantTouchPress { onLockAndExit() }
                            .testTag("fun_zone_bottom_exit_tab"),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Keluar Fun Zone",
                            tint = DuoCardinalRed,
                            modifier = Modifier.size(19.dp)
                        )
                    }
                }
            }
        }
    }
}

// =========================================================================
// DUOLINGO 3D CUSTOM CANVAS RENDERERS (ZERO EMOJIS, ZERO FLAT SVGs)
// =========================================================================

private fun DrawScope.drawDuo3DPortal(
    center: Offset,
    radius: Float,
    primaryColor: Color,
    lipColor: Color
) {
    drawCircle(
        color = lipColor,
        radius = radius,
        center = center.copy(y = center.y + 4f)
    )
    drawCircle(
        color = primaryColor,
        radius = radius,
        center = center
    )
    drawCircle(
        color = DuoBeeYellow,
        radius = radius * 0.55f,
        center = center,
        style = Stroke(width = 3f)
    )
}

private fun DrawScope.drawDuo3DCoreCrystal(
    center: Offset,
    radius: Float
) {
    drawCircle(
        color = DuoMacawBlueLip,
        radius = radius,
        center = center.copy(y = center.y + 4f)
    )
    drawCircle(
        color = DuoMacawBlue,
        radius = radius,
        center = center
    )
    val diamond = Path().apply {
        moveTo(center.x, center.y - radius * 0.65f)
        lineTo(center.x + radius * 0.65f, center.y)
        lineTo(center.x, center.y + radius * 0.65f)
        lineTo(center.x - radius * 0.65f, center.y)
        close()
    }
    drawPath(path = diamond, color = DuoBeeYellow)
}

private fun DrawScope.drawDuo3DTowerSprite(
    center: Offset,
    radius: Float,
    towerType: DuoTowerType,
    level: Int
) {
    // 1. 3D Extruded Pedestal Lip
    drawRoundRect(
        color = towerType.lipColor,
        topLeft = Offset(center.x - radius, center.y - radius + 4f),
        size = Size(radius * 2f, radius * 2f),
        cornerRadius = CornerRadius(radius * 0.45f, radius * 0.45f)
    )
    // 2. Main 3D Character Body Face
    drawRoundRect(
        color = towerType.primaryColor,
        topLeft = Offset(center.x - radius, center.y - radius),
        size = Size(radius * 2f, radius * 2f),
        cornerRadius = CornerRadius(radius * 0.45f, radius * 0.45f)
    )
    // 3. Top Specular Gloss Pill
    drawRoundRect(
        color = Color.White.copy(alpha = 0.32f),
        topLeft = Offset(center.x - radius * 0.65f, center.y - radius * 0.8f),
        size = Size(radius * 1.3f, radius * 0.35f),
        cornerRadius = CornerRadius(radius * 0.2f, radius * 0.2f)
    )
    // 4. Duolingo Big Expressive 3D Eyes
    val eyeOffsetX = radius * 0.36f
    val eyeY = center.y - radius * 0.08f
    drawCircle(color = Color.White, radius = radius * 0.28f, center = Offset(center.x - eyeOffsetX, eyeY))
    drawCircle(color = Color.White, radius = radius * 0.28f, center = Offset(center.x + eyeOffsetX, eyeY))
    drawCircle(color = VaultObsidian, radius = radius * 0.14f, center = Offset(center.x - eyeOffsetX, eyeY))
    drawCircle(color = VaultObsidian, radius = radius * 0.14f, center = Offset(center.x + eyeOffsetX, eyeY))

    // 5. Beak / Crest / Crown per Tower Type
    val beak = Path().apply {
        moveTo(center.x, center.y + radius * 0.32f)
        lineTo(center.x - radius * 0.22f, center.y + radius * 0.05f)
        lineTo(center.x + radius * 0.22f, center.y + radius * 0.05f)
        close()
    }
    drawPath(
        path = beak,
        color = if (towerType == DuoTowerType.SOLAR_BEE) DuoCardinalRed else DuoBeeYellow
    )

    // 6. Upgrade Star Pips at Bottom
    val pipCount = min(5, level)
    val startX = center.x - ((pipCount - 1) * radius * 0.22f) / 2f
    for (i in 0 until pipCount) {
        drawCircle(
            color = DuoBeeYellow,
            radius = radius * 0.10f,
            center = Offset(startX + i * radius * 0.22f, center.y + radius * 0.68f)
        )
    }
}

private fun DrawScope.drawDuo3DEnemySprite(
    center: Offset,
    radius: Float,
    kind: DuoEnemyKind,
    isSlowed: Boolean
) {
    // 3D Shadow Lip
    drawCircle(
        color = kind.lipColor,
        radius = radius,
        center = center.copy(y = center.y + 4f)
    )
    // Main 3D Body
    drawCircle(
        color = if (isSlowed) DuoMacawBlue else kind.bodyColor,
        radius = radius,
        center = center
    )
    // Boss Horns / Crown
    if (kind.isBoss) {
        val crown = Path().apply {
            moveTo(center.x - radius * 0.7f, center.y - radius * 0.4f)
            lineTo(center.x - radius * 0.35f, center.y - radius * 1.05f)
            lineTo(center.x, center.y - radius * 0.55f)
            lineTo(center.x + radius * 0.35f, center.y - radius * 1.05f)
            lineTo(center.x + radius * 0.7f, center.y - radius * 0.4f)
            close()
        }
        drawPath(path = crown, color = DuoBeeYellow)
    }
    // Angry 3D Eyes
    val eyeX = radius * 0.34f
    drawCircle(color = kind.accentColor, radius = radius * 0.22f, center = Offset(center.x - eyeX, center.y - 2f))
    drawCircle(color = kind.accentColor, radius = radius * 0.22f, center = Offset(center.x + eyeX, center.y - 2f))
    drawCircle(color = VaultObsidian, radius = radius * 0.10f, center = Offset(center.x - eyeX, center.y - 2f))
    drawCircle(color = VaultObsidian, radius = radius * 0.10f, center = Offset(center.x + eyeX, center.y - 2f))
}

@Composable
private fun Duo3DMascotBadgeCanvas(
    mascotId: Int,
    modifier: Modifier = Modifier
) {
    val towerEquivalent = DuoTowerType.entries[mascotId.coerceIn(0, DuoTowerType.entries.size - 1)]
    Canvas(modifier = modifier) {
        drawDuo3DTowerSprite(
            center = Offset(size.width / 2f, size.height / 2f),
            radius = size.minDimension * 0.44f,
            towerType = towerEquivalent,
            level = (mascotId % 5) + 1
        )
    }
}

@Composable
private fun Duo3DSlotSymbolCanvas(
    symbolId: Int,
    modifier: Modifier = Modifier
) {
    val towerEquivalent = DuoTowerType.entries[symbolId.coerceIn(0, DuoTowerType.entries.size - 1)]
    Canvas(modifier = modifier) {
        val cx = size.width / 2f
        val cy = size.height / 2f
        val r = size.minDimension * 0.44f
        drawDuo3DTowerSprite(
            center = Offset(cx, cy),
            radius = r,
            towerType = towerEquivalent,
            level = 3
        )
    }
}

@Composable
private fun Duo3DMoodFaceCanvas(
    moodIndex: Int,
    modifier: Modifier = Modifier
) {
    val (faceColor, lipColor) = when (moodIndex % 6) {
        0 -> DuoFeatherGreen to DuoFeatherGreenLip
        1 -> DuoFoxOrange to DuoFoxOrangeLip
        2 -> DuoMacawBlue to DuoMacawBlueLip
        3 -> DuoBeeYellow to DuoBeeYellowLip
        4 -> DuoBeetlePurple to DuoBeetlePurpleLip
        else -> DuoCardinalRed to DuoCardinalRedLip
    }
    Canvas(modifier = modifier) {
        val cx = size.width / 2f
        val cy = size.height / 2f
        val r = size.minDimension * 0.44f

        drawCircle(color = lipColor, radius = r, center = Offset(cx, cy + 3.5f))
        drawCircle(color = faceColor, radius = r, center = Offset(cx, cy))
        drawCircle(color = Color.White, radius = r * 0.24f, center = Offset(cx - r * 0.34f, cy - r * 0.14f))
        drawCircle(color = Color.White, radius = r * 0.24f, center = Offset(cx + r * 0.34f, cy - r * 0.14f))
        drawCircle(color = VaultObsidian, radius = r * 0.12f, center = Offset(cx - r * 0.34f, cy - r * 0.14f))
        drawCircle(color = VaultObsidian, radius = r * 0.12f, center = Offset(cx + r * 0.34f, cy - r * 0.14f))

        // Expressive Mouth Curve
        val mouthPath = Path().apply {
            if (moodIndex <= 3) {
                moveTo(cx - r * 0.35f, cy + r * 0.22f)
                quadraticBezierTo(cx, cy + r * 0.55f, cx + r * 0.35f, cy + r * 0.22f)
            } else {
                moveTo(cx - r * 0.35f, cy + r * 0.42f)
                quadraticBezierTo(cx, cy + r * 0.12f, cx + r * 0.35f, cy + r * 0.42f)
            }
        }
        drawPath(
            path = mouthPath,
            color = VaultObsidian,
            style = Stroke(width = 3.dp.toPx(), cap = StrokeCap.Round)
        )
    }
}
