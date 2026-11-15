package com.example.ui

import android.content.SharedPreferences
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Extension
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
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
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
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
import com.example.ui.theme.DuoEelInk
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
import com.example.ui.theme.DuoMutedInk
import com.example.ui.theme.DuoPureWhite
import com.example.ui.theme.DuoSnowCanvas
import com.example.ui.theme.DuoSwanBorder
import com.example.ui.theme.JetBrainsMonoFontFamily
import com.example.ui.theme.VaultObsidian
import com.example.ui.theme.VaultTextMuted
import com.example.ui.theme.duolingoButton
import com.example.ui.theme.duolingoCard
import kotlin.math.roundToInt

enum class BrainGameDifficulty(
    val id: Int,
    val label: String,
    val rewardMultiplier: Float,
    private val rawBadgeColor: Color,
    private val rawLipColor: Color
) {
    MUDAH(0, "Mudah", 1.0f, Color(0xFF58CC02), Color(0xFF46A302)),
    SEDANG(1, "Sedang", 1.5f, DuoMacawBlue, DuoMacawBlueLip),
    SULIT(2, "Sulit", 2.2f, DuoFoxOrange, DuoFoxOrangeLip),
    EKSTREM(3, "Ekstrem", 3.5f, DuoCardinalRed, DuoCardinalRedLip);

    val badgeColor: Color
        get() = if (this == MUDAH) DuoFeatherGreen else rawBadgeColor
    val lipColor: Color
        get() = if (this == MUDAH) DuoFeatherGreenLip else rawLipColor
}

enum class BrainGameCategory(
    val id: String,
    val label: String,
    private val rawFaceColor: Color,
    private val rawLipColor: Color
) {
    ALL("ALL", "Semua (27)", Color(0xFF58CC02), Color(0xFF46A302)),
    LOGIC_NUMBERS("LOGIC", "Logika & Angka", DuoMacawBlue, DuoMacawBlueLip),
    BLOCKS_MAZE("BLOCKS", "Balok & Labirin", DuoFoxOrange, DuoFoxOrangeLip),
    BOARD_STRATEGY("STRATEGY", "Strategi Papan", DuoBeetlePurple, DuoBeetlePurpleLip),
    MEMORY_FOCUS("MEMORY", "Memori & Fokus", DuoBeeYellow, DuoBeeYellowLip),
    MATH_CIPHER("CIPHER", "Matematika & Sandi", DuoCardinalRed, DuoCardinalRedLip),
    ARCADE_3D("ARCADE", "Arcade & Gacha 3D", Color(0xFF58CC02), Color(0xFF46A302));

    val faceColor: Color
        get() = if (this == ALL || this == ARCADE_3D) DuoFeatherGreen else rawFaceColor
    val lipColor: Color
        get() = if (this == ALL || this == ARCADE_3D) DuoFeatherGreenLip else rawLipColor
}

enum class BrainGameId(
    val key: String,
    val title: String,
    val subtitle: String,
    val category: BrainGameCategory,
    private val rawPrimaryColor: Color,
    private val rawLipColor: Color,
    private val rawSoftColor: Color,
    val baseCoinReward: Int,
    val baseXpReward: Int
) {
    // 1. Logika & Angka (4 Games)
    SUDOKU(
        "sudoku",
        "Sudoku Klasik",
        "Susun angka unik tiap baris, kolom & blok",
        BrainGameCategory.LOGIC_NUMBERS,
        DuoMacawBlue,
        DuoMacawBlueLip,
        DuoMacawBlueSoft,
        35,
        50
    ),
    NONOGRAM(
        "nonogram",
        "Nonogram Picross",
        "Lukis piksel rahasia dari petunjuk angka",
        BrainGameCategory.LOGIC_NUMBERS,
        DuoBeetlePurple,
        DuoBeetlePurpleLip,
        DuoBeetlePurpleSoft,
        35,
        50
    ),
    MINESWEEPER(
        "minesweeper",
        "Minesweeper Bom",
        "Deteksi logika ranjau & pasang bendera aman",
        BrainGameCategory.LOGIC_NUMBERS,
        DuoCardinalRed,
        DuoCardinalRedLip,
        DuoCardinalRedSoft,
        30,
        45
    ),
    NUMBER_LINK(
        "number_link",
        "Number Link",
        "Sambungkan pasangan angka tanpa tabrakan",
        BrainGameCategory.LOGIC_NUMBERS,
        DuoFeatherGreen,
        DuoFeatherGreenLip,
        DuoFeatherGreenSoft,
        30,
        45
    ),

    // 2. Balok & Labirin (8 Games)
    MERGE_2048(
        "merge_2048",
        "2048 Merge",
        "Gabungkan ubin kelipatan 2 menuju 2048+",
        BrainGameCategory.BLOCKS_MAZE,
        DuoFoxOrange,
        DuoFoxOrangeLip,
        DuoFoxOrangeSoft,
        30,
        45
    ),
    BLOCK_BLAST(
        "block_blast",
        "Block Blast Puzzle",
        "Susun balok 3D & bersihkan baris/kolom penuh",
        BrainGameCategory.BLOCKS_MAZE,
        DuoMacawBlue,
        DuoMacawBlueLip,
        DuoMacawBlueSoft,
        35,
        50
    ),
    UNBLOCK_ME(
        "unblock_me",
        "Unblock Me Balok",
        "Geser balok penghalang & bebaskan Inti Emas",
        BrainGameCategory.BLOCKS_MAZE,
        DuoBeeYellow,
        DuoBeeYellowLip,
        DuoBeeYellowSoft,
        30,
        45
    ),
    TOWER_OF_HANOI(
        "tower_hanoi",
        "Tower of Hanoi",
        "Pindahkan menara cakram dalam langkah minimum",
        BrainGameCategory.BLOCKS_MAZE,
        DuoBeetlePurple,
        DuoBeetlePurpleLip,
        DuoBeetlePurpleSoft,
        35,
        50
    ),
    WATER_SORT(
        "water_sort",
        "Water Sort Warna",
        "Tuang & pisahkan cairan warna di tabung 3D",
        BrainGameCategory.BLOCKS_MAZE,
        DuoFeatherGreen,
        DuoFeatherGreenLip,
        DuoFeatherGreenSoft,
        30,
        45
    ),
    PIPE_CONNECT(
        "pipe_connect",
        "Pipe Connect Pipa",
        "Putar jaringan pipa agar aliran energi nyambung",
        BrainGameCategory.BLOCKS_MAZE,
        DuoMacawBlue,
        DuoMacawBlueLip,
        DuoMacawBlueSoft,
        30,
        45
    ),
    SLIDING_15(
        "sliding_15",
        "Sliding 15-Puzzle",
        "Geser ubin angka acak kembali berurutan",
        BrainGameCategory.BLOCKS_MAZE,
        DuoFoxOrange,
        DuoFoxOrangeLip,
        DuoFoxOrangeSoft,
        30,
        45
    ),
    LIGHTS_OUT(
        "lights_out",
        "Lights Out Lampu",
        "Matikan seluruh lampu matriks dengan pola logis",
        BrainGameCategory.BLOCKS_MAZE,
        DuoCardinalRed,
        DuoCardinalRedLip,
        DuoCardinalRedSoft,
        30,
        45
    ),

    // 3. Strategi Papan (3 Games)
    CHESS_TACTICS(
        "chess_tactics",
        "Chess Tactics Catur",
        "Pecahkan taktik skakmat & tangkapan menteri",
        BrainGameCategory.BOARD_STRATEGY,
        DuoBeetlePurple,
        DuoBeetlePurpleLip,
        DuoBeetlePurpleSoft,
        45,
        65
    ),
    REVERSI_OTHELLO(
        "reversi_othello",
        "Reversi Othello",
        "Jepit bidak lawan secara diagonal & ortogonal",
        BrainGameCategory.BOARD_STRATEGY,
        DuoFeatherGreen,
        DuoFeatherGreenLip,
        DuoFeatherGreenSoft,
        40,
        60
    ),
    CHECKERS_DAM(
        "checkers_dam",
        "Checkers Dam",
        "Lompati bidak lawan & promosikan Raja Dam",
        BrainGameCategory.BOARD_STRATEGY,
        DuoCardinalRed,
        DuoCardinalRedLip,
        DuoCardinalRedSoft,
        40,
        60
    ),

    // 4. Memori & Fokus (4 Games)
    SIMON_SAYS(
        "simon_says",
        "Simon Says Pola",
        "Hafalkan & ulangi urutan nyala pad karakter 3D",
        BrainGameCategory.MEMORY_FOCUS,
        DuoFoxOrange,
        DuoFoxOrangeLip,
        DuoFoxOrangeSoft,
        25,
        40
    ),
    MEMORY_MATRIX(
        "memory_matrix",
        "Memory Matrix Grid",
        "Rekam posisi sel bercahaya dalam 1.5 detik",
        BrainGameCategory.MEMORY_FOCUS,
        DuoMacawBlue,
        DuoMacawBlueLip,
        DuoMacawBlueSoft,
        30,
        45
    ),
    MEMORY_MATCH(
        "memory_match",
        "Kartu Pasangan",
        "Balik & cocokkan kartu simbol kripto kembar",
        BrainGameCategory.MEMORY_FOCUS,
        DuoFeatherGreen,
        DuoFeatherGreenLip,
        DuoFeatherGreenSoft,
        25,
        40
    ),
    SCHULTE_TABLE(
        "schulte_table",
        "Schulte Table Fokus",
        "Latih fokus perifer ketuk angka 1..N tercepat",
        BrainGameCategory.MEMORY_FOCUS,
        DuoBeeYellow,
        DuoBeeYellowLip,
        DuoBeeYellowSoft,
        30,
        45
    ),

    // 5. Matematika Cepat & Kriptografi (5 Games)
    MATH_SPRINT(
        "math_sprint",
        "Math Sprint Cepat",
        "Hitung berantai melawan waktu & raih kombo",
        BrainGameCategory.MATH_CIPHER,
        DuoCardinalRed,
        DuoCardinalRedLip,
        DuoCardinalRedSoft,
        30,
        45
    ),
    MATH_24_GAME(
        "math_24",
        "24 Game Matematika",
        "Olah 4 angka dengan + - × ÷ agar tepat 24",
        BrainGameCategory.MATH_CIPHER,
        DuoFoxOrange,
        DuoFoxOrangeLip,
        DuoFoxOrangeSoft,
        35,
        50
    ),
    MASTERMIND(
        "mastermind",
        "Mastermind Kode",
        "Pecahkan kode rahasia dari petunjuk Bull & Cow",
        BrainGameCategory.MATH_CIPHER,
        DuoBeetlePurple,
        DuoBeetlePurpleLip,
        DuoBeetlePurpleSoft,
        35,
        50
    ),
    WORDLE_SANDI(
        "wordle_sandi",
        "Wordle Kata Sandi",
        "Tebak kata rahasia 5 huruf dalam 6 langkah",
        BrainGameCategory.MATH_CIPHER,
        DuoFeatherGreen,
        DuoFeatherGreenLip,
        DuoFeatherGreenSoft,
        35,
        50
    ),
    CRYPTOGRAM(
        "cryptogram",
        "Cryptogram Sandi",
        "Dekripsi sandi substitusi huruf rahasia",
        BrainGameCategory.MATH_CIPHER,
        DuoMacawBlue,
        DuoMacawBlueLip,
        DuoMacawBlueSoft,
        40,
        60
    ),

    // 6. Arcade 3D & Gacha (3 Games)
    BRICK_BREAKER_3D(
        "brick_breaker_3d",
        "Brick Breaker 3D",
        "Hancurkan kristal kosmik dengan paddle 3D",
        BrainGameCategory.ARCADE_3D,
        DuoFeatherGreen,
        DuoFeatherGreenLip,
        DuoFeatherGreenSoft,
        30,
        40
    ),
    METEOR_DODGE_3D(
        "meteor_dodge_3d",
        "Meteor & Star 3D",
        "Hindari asteroid & tangkap bintang emas",
        BrainGameCategory.ARCADE_3D,
        DuoMacawBlue,
        DuoMacawBlueLip,
        DuoMacawBlueSoft,
        30,
        40
    ),
    MASCOT_GACHA_3D(
        "mascot_gacha_3d",
        "Kapsul Gacha Maskot",
        "Buka & upgrade level maskot pendamping SSR",
        BrainGameCategory.ARCADE_3D,
        DuoBeetlePurple,
        DuoBeetlePurpleLip,
        DuoBeetlePurpleSoft,
        20,
        30
    );

    private val isPrimaryGreenSlot: Boolean
        get() = this == NUMBER_LINK ||
            this == WATER_SORT ||
            this == REVERSI_OTHELLO ||
            this == MEMORY_MATCH ||
            this == WORDLE_SANDI ||
            this == BRICK_BREAKER_3D

    val primaryColor: Color
        get() = if (isPrimaryGreenSlot) DuoFeatherGreen else rawPrimaryColor

    val lipColor: Color
        get() = if (isPrimaryGreenSlot) DuoFeatherGreenLip else rawLipColor

    val softColor: Color
        get() = if (isPrimaryGreenSlot) DuoFeatherGreenSoft else rawSoftColor
}

@Composable
fun BrainPuzzleGamesDashboardHub(
    prefs: SharedPreferences,
    cosmicCoins: Int,
    onUpdateCoins: (Int) -> Unit,
    onUnlockMascot: (Int) -> Unit,
    onPostBanner: (String) -> Unit,
    onOpenTowerDefenseClick: (() -> Unit)? = null
) {
    var selectedGame by remember { mutableStateOf<BrainGameId?>(null) }
    var selectedDifficulty by remember { mutableStateOf(BrainGameDifficulty.MUDAH) }
    var selectedCategory by remember { mutableStateOf(BrainGameCategory.ALL) }
    var searchQuery by remember { mutableStateOf("") }
    var scoreUpdateNonce by remember { mutableIntStateOf(0) }

    var mascotXp by remember {
        mutableIntStateOf(prefs.getInt("brain_mascot_xp", 120))
    }
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
        val newLv = ((mutableMap[mascotId] ?: 0) + 1).coerceAtMost(20)
        mutableMap[mascotId] = newLv
        mascotLevelsCsv = mutableMap.entries.sortedBy { it.key }.joinToString(",") { "${it.key}:${it.value}" }
        prefs.edit().putString("nyx_mascot_levels_csv", mascotLevelsCsv).apply()
        onUnlockMascot(mascotId)
        return newLv
    }

    val activeMascotSpec = remember(equippedMascotId) {
        NYX_MASCOT_CATALOG.getOrElse(equippedMascotId) { NYX_MASCOT_CATALOG.first() }
    }
    val activeMascotLevel = remember(equippedMascotId, mascotLevelsCsv) {
        getMascotLevel(equippedMascotId).coerceAtLeast(1)
    }
    val scoreMultiplier = remember(equippedMascotId, activeMascotLevel, selectedDifficulty) {
        val mascotBonus = 1.0f + (if (equippedMascotId <= 2) 0.25f else 0.15f) + (activeMascotLevel - 1) * 0.05f
        mascotBonus * selectedDifficulty.rewardMultiplier
    }

    fun getHighScore(game: BrainGameId): Int {
        return prefs.getInt("brain_high_${game.key}", 0)
    }

    fun getBestDiffCleared(game: BrainGameId): Int {
        return prefs.getInt("brain_best_diff_${game.key}", -1)
    }

    fun recordWinAndAward(game: BrainGameId, rawScore: Int, stageLabel: String = "Selesai") {
        val boostedScore = (rawScore * scoreMultiplier).roundToInt().coerceAtLeast(10)
        val prevHigh = getHighScore(game)
        if (boostedScore > prevHigh) {
            prefs.edit().putInt("brain_high_${game.key}", boostedScore).apply()
        }
        val prevDiff = getBestDiffCleared(game)
        if (selectedDifficulty.id > prevDiff) {
            prefs.edit().putInt("brain_best_diff_${game.key}", selectedDifficulty.id).apply()
        }
        val coinsEarned = (game.baseCoinReward * selectedDifficulty.rewardMultiplier).roundToInt().coerceAtLeast(5)
        val xpEarned = (game.baseXpReward * selectedDifficulty.rewardMultiplier).roundToInt().coerceAtLeast(10)

        onUpdateCoins(coinsEarned)
        val newXp = mascotXp + xpEarned
        val prevBrainLv = mascotXp / 250
        val newBrainLv = newXp / 250
        mascotXp = newXp
        prefs.edit().putInt("brain_mascot_xp", mascotXp).apply()
        if (newBrainLv > prevBrainLv) {
            val newMascotLv = upgradeOrUnlockMascot(equippedMascotId)
            onPostBanner("LEVEL UP MASKOT! ${activeMascotSpec.name} naik ke Lv.$newMascotLv (+${coinsEarned} Koin & +${xpEarned} XP)!")
        } else {
            onPostBanner("${game.title} ($stageLabel • ${selectedDifficulty.label}): Skor $boostedScore • +$coinsEarned Koin & +$xpEarned XP Maskot!")
        }
        scoreUpdateNonce++
    }

    // Back button returns from active game to the Game Dashboard
    BackHandler(enabled = selectedGame != null) {
        selectedGame = null
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("brain_puzzle_games_hub"),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        if (selectedGame == null) {
            // =========================================================================
            // TAMPILAN 1: GAME DASHBOARD PEMILIHAN 27 GAME ASAH OTAK & ARCADE
            // =========================================================================
            val totalHighScore = remember(scoreUpdateNonce) {
                BrainGameId.entries.sumOf { getHighScore(it) }
            }
            val clearedGamesCount = remember(scoreUpdateNonce) {
                BrainGameId.entries.count { getHighScore(it) > 0 }
            }
            val brainRankTitle = remember(mascotXp, clearedGamesCount) {
                when {
                    mascotXp >= 2500 || clearedGamesCount >= 20 -> "GRANDMASTER ARCANUM IQ 180+"
                    mascotXp >= 1200 || clearedGamesCount >= 12 -> "MASTER KRIPTOGRAFER IQ 150"
                    mascotXp >= 500 || clearedGamesCount >= 5 -> "STRATEGIS LOGIKA IQ 130"
                    else -> "PENJELAJAH PUZZLE IQ 110"
                }
            }

            // 1. Header Banner: Maskot Aktif, Koin Kosmik, Brain XP & Statistik
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
                    .padding(16.dp)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.weight(1f)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(54.dp)
                                    .duolingoButton(
                                        faceColor = activeMascotSpec.softColor,
                                        lipColor = activeMascotSpec.lipColor,
                                        cornerRadius = 16.dp,
                                        lipHeight = 4.dp
                                    )
                                    .border(2.dp, activeMascotSpec.primaryColor, RoundedCornerShape(16.dp))
                                    .padding(bottom = 4.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Nyx3DMascotAvatarCanvas(
                                    spec = activeMascotSpec,
                                    level = activeMascotLevel,
                                    modifier = Modifier.size(40.dp)
                                )
                            }
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "GAME DASHBOARD • $brainRankTitle",
                                    fontFamily = JetBrainsMonoFontFamily,
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = DuoBeetlePurpleLip,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Text(
                                    text = "Pusat 27 Game Asah Otak & Puzzle 3D",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = VaultObsidian,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Text(
                                    text = "Maskot: ${activeMascotSpec.name} (Lv.$activeMascotLevel) • XP: $mascotXp • Bonus ${(scoreMultiplier * 100).roundToInt()}%",
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = VaultTextMuted,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }
                    }

                    // Bar Statistik Ringkas (Koin, Total Skor, Game Selesai)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        DashboardStatMiniCard(
                            label = "KOIN MASKOT",
                            value = "$cosmicCoins",
                            faceColor = DuoBeeYellowSoft,
                            borderColor = DuoBeeYellow,
                            lipColor = DuoBeeYellowLip,
                            modifier = Modifier.weight(1f)
                        )
                        DashboardStatMiniCard(
                            label = "TOTAL SKOR",
                            value = "$totalHighScore",
                            faceColor = DuoFeatherGreenSoft,
                            borderColor = DuoFeatherGreen,
                            lipColor = DuoFeatherGreenLip,
                            modifier = Modifier.weight(1f)
                        )
                        DashboardStatMiniCard(
                            label = " DITAKLUKKAN",
                            value = "$clearedGamesCount / ${BrainGameId.entries.size}",
                            faceColor = DuoMacawBlueSoft,
                            borderColor = DuoMacawBlue,
                            lipColor = DuoMacawBlueLip,
                            modifier = Modifier.weight(1f)
                        )
                    }

                    // Pemilih Default Level Kesulitan di Dashboard
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(
                            text = "Level:",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = VaultObsidian
                        )
                        BrainGameDifficulty.entries.forEach { diff ->
                            val isSelected = selectedDifficulty == diff
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .height(34.dp)
                                    .duolingoButton(
                                        faceColor = if (isSelected) diff.badgeColor else DuoSnowCanvas,
                                        lipColor = if (isSelected) diff.lipColor else DuoHareLip,
                                        cornerRadius = 10.dp,
                                        lipHeight = 3.dp
                                    )
                                    .clickable { selectedDifficulty = diff }
                                    .padding(bottom = 3.dp)
                                    .testTag("dashboard_diff_${diff.id}"),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "${diff.label} (${diff.rewardMultiplier}x)",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = if (isSelected) Color.White else DuoEelInk,
                                    maxLines = 1
                                )
                            }
                        }
                    }

                    // Pencarian & Filter Kategori
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        placeholder = {
                            Text(
                                "Cari game (misal: Sudoku, 2048, Catur, Sandi, Memori, Hanoi)...",
                                fontSize = 12.sp
                            )
                        },
                        leadingIcon = {
                            Icon(Icons.Default.Search, contentDescription = null, tint = DuoBeetlePurple)
                        },
                        singleLine = true,
                        shape = RoundedCornerShape(14.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = DuoBeetlePurple,
                            unfocusedBorderColor = DuoSwanBorder,
                            focusedContainerColor = DuoSnowCanvas,
                            unfocusedContainerColor = DuoSnowCanvas
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("game_dashboard_search_input")
                    )

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        BrainGameCategory.entries.forEach { cat ->
                            val isSel = selectedCategory == cat
                            Box(
                                modifier = Modifier
                                    .height(38.dp)
                                    .duolingoButton(
                                        faceColor = if (isSel) cat.faceColor else DuoPureWhite,
                                        lipColor = if (isSel) cat.lipColor else DuoHareLip,
                                        cornerRadius = 12.dp,
                                        lipHeight = 3.dp
                                    )
                                    .border(
                                        width = 1.5.dp,
                                        color = if (isSel) cat.lipColor else DuoSwanBorder,
                                        shape = RoundedCornerShape(12.dp)
                                    )
                                    .clickable { selectedCategory = cat }
                                    .padding(horizontal = 12.dp)
                                    .padding(bottom = 3.dp)
                                    .testTag("game_cat_${cat.id}"),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = cat.label,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = if (isSel) {
                                        if (cat.faceColor == DuoBeeYellow) DuoEelInk else Color.White
                                    } else DuoEelInk,
                                    maxLines = 1
                                )
                            }
                        }
                    }
                }
            }

            // Optional Quick Launch Card for Galaxy Tower Defense 200 Wave if callback provided
            if (onOpenTowerDefenseClick != null && (selectedCategory == BrainGameCategory.ALL || selectedCategory == BrainGameCategory.ARCADE_3D)) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .duolingoCard(
                            backgroundColor = DuoFeatherGreenSoft,
                            borderColor = DuoFeatherGreen,
                            lipColor = DuoFeatherGreenLip,
                            cornerRadius = 20.dp,
                            borderWidth = 2.dp,
                            lipHeight = 5.dp
                        )
                        .clickable { onOpenTowerDefenseClick() }
                        .padding(bottom = 5.dp)
                        .padding(14.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "SPESIAL STRATEGI PERTAHANAN • 200 WAVE + ENDLESS",
                                fontFamily = JetBrainsMonoFontFamily,
                                fontSize = 9.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = DuoFeatherGreenLip
                            )
                            Text(
                                text = "Galaxy Tower Defense 3D",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.ExtraBold,
                                color = VaultObsidian
                            )
                            Text(
                                text = "Bangun 6 menara karakter 3D, aktifkan Overdrive, dan taklukkan Boss Leviathan!",
                                style = MaterialTheme.typography.bodySmall,
                                fontSize = 11.sp,
                                color = VaultTextMuted
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
                                .padding(horizontal = 12.dp)
                                .padding(bottom = 3.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "BUKA TD",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = Color.White
                            )
                        }
                    }
                }
            }

            // 2. Grid 2-Kolom Daftar Game (27 Game Asah Otak, Strategi, Memori, Matematika, Kriptografi & Arcade)
            val filteredGames = remember(selectedCategory, searchQuery, scoreUpdateNonce) {
                BrainGameId.entries.filter { g ->
                    val matchesCategory = selectedCategory == BrainGameCategory.ALL || g.category == selectedCategory
                    val matchesSearch = searchQuery.isBlank() ||
                        g.title.contains(searchQuery, ignoreCase = true) ||
                        g.subtitle.contains(searchQuery, ignoreCase = true) ||
                        g.category.label.contains(searchQuery, ignoreCase = true)
                    matchesCategory && matchesSearch
                }
            }

            filteredGames.chunked(2).forEach { rowPair ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    rowPair.forEach { game ->
                        val high = getHighScore(game)
                        val bestDiff = getBestDiffCleared(game)
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .duolingoCard(
                                    backgroundColor = DuoPureWhite,
                                    borderColor = game.primaryColor,
                                    lipColor = game.lipColor,
                                    cornerRadius = 20.dp,
                                    borderWidth = 2.dp,
                                    lipHeight = 5.dp
                                )
                                .clickable { selectedGame = game }
                                .padding(bottom = 5.dp)
                                .padding(12.dp)
                                .testTag("game_card_${game.key}")
                        ) {
                            Column(
                                modifier = Modifier.fillMaxWidth(),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.Top
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(46.dp)
                                            .duolingoButton(
                                                faceColor = game.softColor,
                                                lipColor = game.lipColor,
                                                cornerRadius = 14.dp,
                                                lipHeight = 3.dp
                                            )
                                            .border(1.5.dp, game.primaryColor, RoundedCornerShape(14.dp))
                                            .padding(bottom = 3.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        BrainGame3DIconCanvas(
                                            game = game,
                                            modifier = Modifier.size(30.dp)
                                        )
                                    }

                                    Column(horizontalAlignment = Alignment.End) {
                                        Surface(
                                            color = game.softColor,
                                            shape = RoundedCornerShape(8.dp),
                                            border = androidx.compose.foundation.BorderStroke(1.dp, game.primaryColor)
                                        ) {
                                            Text(
                                                text = "+${(game.baseCoinReward * selectedDifficulty.rewardMultiplier).roundToInt()} Koin",
                                                fontSize = 9.sp,
                                                fontWeight = FontWeight.ExtraBold,
                                                color = game.lipColor,
                                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                            )
                                        }
                                        if (bestDiff >= 0) {
                                            Spacer(modifier = Modifier.height(3.dp))
                                            val diffObj = BrainGameDifficulty.entries.getOrElse(bestDiff) { BrainGameDifficulty.MUDAH }
                                            Surface(
                                                color = diffObj.badgeColor,
                                                shape = RoundedCornerShape(6.dp)
                                            ) {
                                                Text(
                                                    text = diffObj.label.uppercase(),
                                                    fontSize = 8.sp,
                                                    fontWeight = FontWeight.ExtraBold,
                                                    color = Color.White,
                                                    modifier = Modifier.padding(horizontal = 5.dp, vertical = 1.dp)
                                                )
                                            }
                                        }
                                    }
                                }

                                Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                                    Text(
                                        text = game.title,
                                        style = MaterialTheme.typography.titleSmall,
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.ExtraBold,
                                        color = VaultObsidian,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                    Text(
                                        text = game.subtitle,
                                        style = MaterialTheme.typography.bodySmall,
                                        fontSize = 10.sp,
                                        lineHeight = 13.sp,
                                        color = VaultTextMuted,
                                        maxLines = 2,
                                        minLines = 2,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                }

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = if (high > 0) "Rekor: $high" else "Belum dimainkan",
                                        fontFamily = JetBrainsMonoFontFamily,
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.ExtraBold,
                                        color = if (high > 0) DuoFeatherGreenLip else DuoMutedInk,
                                        maxLines = 1
                                    )

                                    Box(
                                        modifier = Modifier
                                            .height(30.dp)
                                            .duolingoButton(
                                                faceColor = game.primaryColor,
                                                lipColor = game.lipColor,
                                                cornerRadius = 10.dp,
                                                lipHeight = 3.dp
                                            )
                                            .padding(horizontal = 10.dp)
                                            .padding(bottom = 3.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = "MAIN",
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.ExtraBold,
                                            color = if (game.primaryColor == DuoBeeYellow) DuoEelInk else Color.White
                                        )
                                    }
                                }
                            }
                        }
                    }
                    if (rowPair.size == 1) {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }
        } else {
            // =========================================================================
            // TAMPILAN 2: LAYAR PERMAINAN PENUH + TOMBOL KEMBALI KE DASHBOARD
            // =========================================================================
            val activeGame = selectedGame!!
            val currentHighScore = remember(activeGame, scoreUpdateNonce) { getHighScore(activeGame) }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .duolingoCard(
                        backgroundColor = DuoPureWhite,
                        borderColor = activeGame.primaryColor,
                        lipColor = activeGame.lipColor,
                        cornerRadius = 24.dp,
                        borderWidth = 2.dp,
                        lipHeight = 6.dp
                    )
                    .padding(bottom = 6.dp)
                    .padding(14.dp)
                    .testTag("active_game_container_${activeGame.key}")
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Top Bar: Tombol Kembali ke Dashboard + Judul Game + Saldo Koin
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .height(40.dp)
                                .duolingoButton(
                                    faceColor = DuoCardinalRed,
                                    lipColor = DuoCardinalRedLip,
                                    cornerRadius = 13.dp,
                                    lipHeight = 3.dp
                                )
                                .clickable { selectedGame = null }
                                .padding(horizontal = 12.dp)
                                .padding(bottom = 3.dp)
                                .testTag("game_back_to_dashboard_button"),
                            contentAlignment = Alignment.Center
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(5.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                    contentDescription = "Kembali ke Dashboard",
                                    tint = Color.White,
                                    modifier = Modifier.size(16.dp)
                                )
                                Text(
                                    text = "DASHBOARD",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = Color.White
                                )
                            }
                        }

                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .padding(horizontal = 8.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = activeGame.title,
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.ExtraBold,
                                color = VaultObsidian,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            Text(
                                text = "Rekor: $currentHighScore • Koin: $cosmicCoins",
                                fontFamily = JetBrainsMonoFontFamily,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = activeGame.lipColor,
                                maxLines = 1
                            )
                        }

                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .duolingoButton(
                                    faceColor = activeGame.softColor,
                                    lipColor = activeGame.lipColor,
                                    cornerRadius = 12.dp,
                                    lipHeight = 3.dp
                                )
                                .padding(bottom = 3.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            BrainGame3DIconCanvas(
                                game = activeGame,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }

                    // Pemilih 4 Level Kesulitan di Dalam Game (Mudah, Sedang, Sulit, Ekstrem)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        BrainGameDifficulty.entries.forEach { diff ->
                            val isSel = selectedDifficulty == diff
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .height(34.dp)
                                    .duolingoButton(
                                        faceColor = if (isSel) diff.badgeColor else DuoSnowCanvas,
                                        lipColor = if (isSel) diff.lipColor else DuoHareLip,
                                        cornerRadius = 10.dp,
                                        lipHeight = 3.dp
                                    )
                                    .clickable { selectedDifficulty = diff }
                                    .padding(bottom = 3.dp)
                                    .testTag("in_game_diff_${diff.id}"),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "${diff.label} (${diff.rewardMultiplier}x)",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = if (isSel) Color.White else DuoEelInk,
                                    maxLines = 1
                                )
                            }
                        }
                    }

                    // Konten Permainan Interaktif Sesuai Game yang Dipilih
                    when (activeGame) {
                        BrainGameId.SUDOKU -> SudokuClassicGameView(selectedDifficulty, onWin = { sc -> recordWinAndAward(activeGame, sc, "Sudoku") })
                        BrainGameId.NONOGRAM -> NonogramPicrossGameView(selectedDifficulty, onWin = { sc -> recordWinAndAward(activeGame, sc, "Nonogram") })
                        BrainGameId.MINESWEEPER -> MinesweeperLogicGameView(selectedDifficulty, onWin = { sc -> recordWinAndAward(activeGame, sc, "Minesweeper") })
                        BrainGameId.NUMBER_LINK -> NumberLinkPuzzleGameView(selectedDifficulty, onWin = { sc -> recordWinAndAward(activeGame, sc, "Number Link") })
                        BrainGameId.MERGE_2048 -> Merge2048GameView(selectedDifficulty, onWin = { sc -> recordWinAndAward(activeGame, sc, "2048") })
                        BrainGameId.BLOCK_BLAST -> BlockBlastPuzzleGameView(selectedDifficulty, onWin = { sc -> recordWinAndAward(activeGame, sc, "Block Blast") })
                        BrainGameId.UNBLOCK_ME -> UnblockMeSliderGameView(selectedDifficulty, onWin = { sc -> recordWinAndAward(activeGame, sc, "Unblock Me") })
                        BrainGameId.TOWER_OF_HANOI -> TowerOfHanoiGameView(selectedDifficulty, onWin = { sc -> recordWinAndAward(activeGame, sc, "Hanoi") })
                        BrainGameId.WATER_SORT -> WaterSortColorGameView(selectedDifficulty, onWin = { sc -> recordWinAndAward(activeGame, sc, "Water Sort") })
                        BrainGameId.PIPE_CONNECT -> PipeConnectGameView(selectedDifficulty, onWin = { sc -> recordWinAndAward(activeGame, sc, "Pipe Connect") })
                        BrainGameId.SLIDING_15 -> Sliding15PuzzleGameView(selectedDifficulty, onWin = { sc -> recordWinAndAward(activeGame, sc, "15-Puzzle") })
                        BrainGameId.LIGHTS_OUT -> LightsOutMatrixGameView(selectedDifficulty, onWin = { sc -> recordWinAndAward(activeGame, sc, "Lights Out") })
                        BrainGameId.CHESS_TACTICS -> ChessTacticsPuzzleGameView(selectedDifficulty, onWin = { sc -> recordWinAndAward(activeGame, sc, "Skakmat Taktis") })
                        BrainGameId.REVERSI_OTHELLO -> ReversiOthelloGameView(selectedDifficulty, onWin = { sc -> recordWinAndAward(activeGame, sc, "Othello") })
                        BrainGameId.CHECKERS_DAM -> CheckersDamGameView(selectedDifficulty, onWin = { sc -> recordWinAndAward(activeGame, sc, "Dam") })
                        BrainGameId.SIMON_SAYS -> SimonSaysPatternGameView(selectedDifficulty, onWin = { sc -> recordWinAndAward(activeGame, sc, "Simon Says") })
                        BrainGameId.MEMORY_MATRIX -> MemoryMatrixGridGameView(selectedDifficulty, onWin = { sc -> recordWinAndAward(activeGame, sc, "Memory Matrix") })
                        BrainGameId.MEMORY_MATCH -> MemoryMatchPairsGameView(selectedDifficulty, onWin = { sc -> recordWinAndAward(activeGame, sc, "Kartu Pasangan") })
                        BrainGameId.SCHULTE_TABLE -> SchulteTableFocusGameView(selectedDifficulty, onWin = { sc -> recordWinAndAward(activeGame, sc, "Schulte Fokus") })
                        BrainGameId.MATH_SPRINT -> MathSprintSpeedGameView(selectedDifficulty, onWin = { sc -> recordWinAndAward(activeGame, sc, "Math Sprint") })
                        BrainGameId.MATH_24_GAME -> Math24TargetGameView(selectedDifficulty, onWin = { sc -> recordWinAndAward(activeGame, sc, "24 Game") })
                        BrainGameId.MASTERMIND -> MastermindCodeGameView(selectedDifficulty, onWin = { sc -> recordWinAndAward(activeGame, sc, "Mastermind") })
                        BrainGameId.WORDLE_SANDI -> WordleSecretWordGameView(selectedDifficulty, onWin = { sc -> recordWinAndAward(activeGame, sc, "Wordle Sandi") })
                        BrainGameId.CRYPTOGRAM -> CryptogramCipherGameView(selectedDifficulty, onWin = { sc -> recordWinAndAward(activeGame, sc, "Cryptogram") })
                        BrainGameId.BRICK_BREAKER_3D,
                        BrainGameId.METEOR_DODGE_3D,
                        BrainGameId.MASCOT_GACHA_3D -> {
                            val initialSubMode = when (activeGame) {
                                BrainGameId.BRICK_BREAKER_3D -> 0
                                BrainGameId.METEOR_DODGE_3D -> 1
                                else -> 3
                            }
                            LegacyArcadeEmbeddedSection(
                                initialMode = initialSubMode,
                                prefs = prefs,
                                cosmicCoins = cosmicCoins,
                                onUpdateCoins = onUpdateCoins,
                                onUnlockMascot = onUnlockMascot,
                                onPostBanner = onPostBanner
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun DashboardStatMiniCard(
    label: String,
    value: String,
    faceColor: Color,
    borderColor: Color,
    lipColor: Color,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .duolingoCard(
                backgroundColor = faceColor,
                borderColor = borderColor,
                lipColor = lipColor,
                cornerRadius = 14.dp,
                borderWidth = 2.dp,
                lipHeight = 3.dp
            )
            .padding(bottom = 3.dp)
            .padding(horizontal = 10.dp, vertical = 7.dp)
    ) {
        Column {
            Text(
                text = label,
                fontFamily = JetBrainsMonoFontFamily,
                fontSize = 8.sp,
                fontWeight = FontWeight.ExtraBold,
                color = lipColor,
                maxLines = 1
            )
            Text(
                text = value,
                fontFamily = JetBrainsMonoFontFamily,
                fontSize = 12.sp,
                fontWeight = FontWeight.ExtraBold,
                color = VaultObsidian,
                maxLines = 1
            )
        }
    }
}

@Composable
fun BrainGame3DIconCanvas(
    game: BrainGameId,
    modifier: Modifier = Modifier
) {
    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height
        val c = game.primaryColor
        val lip = game.lipColor
        when (game.category) {
            BrainGameCategory.LOGIC_NUMBERS -> {
                // 3D Grid Icon
                val cell = w * 0.28f
                for (r in 0..1) {
                    for (col in 0..1) {
                        val x = w * 0.18f + col * (cell + w * 0.08f)
                        val y = h * 0.18f + r * (cell + h * 0.08f)
                        drawRoundRect(lip, Offset(x, y + 3f), Size(cell, cell), CornerRadius(6f, 6f))
                        drawRoundRect(if ((r + col) % 2 == 0) c else DuoBeeYellow, Offset(x, y), Size(cell, cell), CornerRadius(6f, 6f))
                    }
                }
            }
            BrainGameCategory.BLOCKS_MAZE -> {
                // 3D Puzzle Blocks
                drawRoundRect(lip, Offset(w * 0.14f, h * 0.22f + 4f), Size(w * 0.72f, h * 0.26f), CornerRadius(8f, 8f))
                drawRoundRect(c, Offset(w * 0.14f, h * 0.22f), Size(w * 0.72f, h * 0.26f), CornerRadius(8f, 8f))
                drawRoundRect(DuoFoxOrangeLip, Offset(w * 0.26f, h * 0.54f + 4f), Size(w * 0.48f, h * 0.26f), CornerRadius(8f, 8f))
                drawRoundRect(DuoFoxOrange, Offset(w * 0.26f, h * 0.54f), Size(w * 0.48f, h * 0.26f), CornerRadius(8f, 8f))
            }
            BrainGameCategory.BOARD_STRATEGY -> {
                // Crown / Strategy Piece 3D
                val path = Path().apply {
                    moveTo(w * 0.16f, h * 0.78f)
                    lineTo(w * 0.84f, h * 0.78f)
                    lineTo(w * 0.78f, h * 0.32f)
                    lineTo(w * 0.58f, h * 0.52f)
                    lineTo(w * 0.50f, h * 0.22f)
                    lineTo(w * 0.42f, h * 0.52f)
                    lineTo(w * 0.22f, h * 0.32f)
                    close()
                }
                drawPath(path, lip)
                drawPath(path, c)
                drawCircle(DuoBeeYellow, radius = w * 0.08f, center = Offset(w * 0.5f, h * 0.20f))
            }
            BrainGameCategory.MEMORY_FOCUS -> {
                // Glowing Brain / Star Matrix
                drawCircle(lip, radius = w * 0.36f, center = Offset(w * 0.5f, h * 0.5f + 4f))
                drawCircle(c, radius = w * 0.36f, center = Offset(w * 0.5f, h * 0.5f))
                drawCircle(Color.White, radius = w * 0.16f, center = Offset(w * 0.5f, h * 0.5f))
            }
            BrainGameCategory.MATH_CIPHER -> {
                // Cipher Key / Math Shield
                drawRoundRect(lip, Offset(w * 0.18f, h * 0.18f + 4f), Size(w * 0.64f, h * 0.64f), CornerRadius(12f, 12f))
                drawRoundRect(c, Offset(w * 0.18f, h * 0.18f), Size(w * 0.64f, h * 0.64f), CornerRadius(12f, 12f))
                drawLine(Color.White, Offset(w * 0.34f, h * 0.5f), Offset(w * 0.66f, h * 0.5f), strokeWidth = 4f, cap = StrokeCap.Round)
                drawLine(Color.White, Offset(w * 0.5f, h * 0.34f), Offset(w * 0.5f, h * 0.66f), strokeWidth = 4f, cap = StrokeCap.Round)
            }
            else -> {
                // Arcade Crystal
                drawCircle(lip, radius = w * 0.38f, center = Offset(w * 0.5f, h * 0.5f + 4f))
                drawCircle(c, radius = w * 0.38f, center = Offset(w * 0.5f, h * 0.5f))
                drawCircle(DuoBeeYellow, radius = w * 0.18f, center = Offset(w * 0.5f, h * 0.5f))
            }
        }
    }
}
