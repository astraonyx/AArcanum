package com.example.ui

import android.content.SharedPreferences
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
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
import com.example.ui.theme.DuoHareLip
import com.example.ui.theme.DuoMacawBlue
import com.example.ui.theme.DuoMacawBlueLip
import com.example.ui.theme.DuoMutedInk
import com.example.ui.theme.DuoPureWhite
import com.example.ui.theme.DuoSnowCanvas
import com.example.ui.theme.DuoSwanBorder
import com.example.ui.theme.JetBrainsMonoFontFamily
import com.example.ui.theme.VaultObsidian
import com.example.ui.theme.duolingoButton
import com.example.ui.theme.duolingoCard
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.security.SecureRandom
import kotlin.math.abs

// =========================================================================
// 13. CHESS TACTICS (PUZZLE SKAKMAT & TAKTIK CATUR)
// =========================================================================
private data class ChessTacticScenario(
    val title: String,
    val hint: String,
    val pieces: Map<Int, String>, // idx (0..63) -> "WK","WQ","WR","WB","WN","BK","BQ","BR","BP"
    val bestFrom: Int,
    val bestTo: Int
)

@Composable
fun ChessTacticsPuzzleGameView(
    difficulty: BrainGameDifficulty,
    onWin: (Int) -> Unit
) {
    val scenarios = remember {
        listOf(
            ChessTacticScenario(
                title = "Back-Rank Mate in 1 (Benteng Putih)",
                hint = "Gerakkan Benteng Putih (WR) dari d1 ke d8 untuk Skakmat!",
                pieces = mapOf(
                    6 to "BK", 13 to "BP", 14 to "BP", 15 to "BP",
                    59 to "WR", 62 to "WK"
                ),
                bestFrom = 59,
                bestTo = 3
            ),
            ChessTacticScenario(
                title = "Queen Kiss Mate in 1 (Menteri Putih)",
                hint = "Gerakkan Menteri Putih (WQ) ke f7 (dilindungi Kuda) untuk Skakmat!",
                pieces = mapOf(
                    4 to "BK", 13 to "BP", 29 to "WN", 47 to "WQ", 60 to "WK"
                ),
                bestFrom = 47,
                bestTo = 13
            ),
            ChessTacticScenario(
                title = "Royal Fork Kuda Taktis",
                hint = "Lompatkan Kuda Putih (WN) ke c7 untuk garpu Raja & Benteng Hitam!",
                pieces = mapOf(
                    0 to "BR", 4 to "BK", 27 to "WN", 60 to "WK"
                ),
                bestFrom = 27,
                bestTo = 10
            ),
            ChessTacticScenario(
                title = "Diagonal Sniper Skakmat",
                hint = "Luncurkan Menteri Putih (WQ) ke h7 yang dikawal Gajah Putih (WB)!",
                pieces = mapOf(
                    6 to "BK", 5 to "BR", 13 to "BP", 41 to "WB", 55 to "WQ", 60 to "WK"
                ),
                bestFrom = 55,
                bestTo = 15
            )
        )
    }
    var puzzleIndex by remember(difficulty) { mutableIntStateOf(difficulty.id % scenarios.size) }
    val activeScenario = scenarios[puzzleIndex % scenarios.size]
    val boardMap = remember { mutableStateListOf<String>() }
    var selectedSquare by remember { mutableIntStateOf(-1) }
    var statusMsg by remember { mutableStateOf(activeScenario.title) }

    fun loadScenario(idx: Int) {
        val sc = scenarios[idx % scenarios.size]
        boardMap.clear()
        for (i in 0 until 64) {
            boardMap.add(sc.pieces[i] ?: "")
        }
        selectedSquare = -1
        statusMsg = sc.title
    }

    LaunchedEffect(puzzleIndex, difficulty) {
        loadScenario(puzzleIndex)
    }

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = statusMsg,
                fontSize = 11.sp,
                fontWeight = FontWeight.ExtraBold,
                color = VaultObsidian,
                modifier = Modifier.weight(1f)
            )
            BrainActionBtn("PETUNJUK", DuoMacawBlue, DuoMacawBlueLip) {
                statusMsg = activeScenario.hint
                selectedSquare = activeScenario.bestFrom
            }
            Spacer(modifier = Modifier.size(6.dp))
            BrainActionBtn("SOAL LAIN", DuoFeatherGreen, DuoFeatherGreenLip) {
                puzzleIndex = (puzzleIndex + 1) % scenarios.size
            }
        }

        if (boardMap.size == 64) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(DuoEelInk, RoundedCornerShape(12.dp))
                    .padding(4.dp),
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                for (r in 0 until 8) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(2.dp)
                    ) {
                        for (c in 0 until 8) {
                            val idx = r * 8 + c
                            val piece = boardMap[idx]
                            val isLight = (r + c) % 2 == 0
                            val isSel = selectedSquare == idx
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .aspectRatio(1f)
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(
                                        when {
                                            isSel -> DuoBeeYellow
                                            isLight -> Color(0xFFF3F6FA)
                                            else -> Color(0xFF94A3B8)
                                        }
                                    )
                                    .clickable {
                                        if (selectedSquare == -1) {
                                            if (piece.startsWith("W")) selectedSquare = idx
                                        } else if (selectedSquare == idx) {
                                            selectedSquare = -1
                                        } else {
                                            if (selectedSquare == activeScenario.bestFrom && idx == activeScenario.bestTo) {
                                                boardMap[idx] = boardMap[selectedSquare]
                                                boardMap[selectedSquare] = ""
                                                selectedSquare = -1
                                                statusMsg = "SKAKMAT / TAKTIK TEPAT! Luar biasa!"
                                                onWin(420)
                                            } else if (piece.startsWith("W")) {
                                                selectedSquare = idx
                                            } else {
                                                statusMsg = "Langkah kurang tepat! Coba lagi atau tekan PETUNJUK."
                                                selectedSquare = -1
                                            }
                                        }
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                if (piece.isNotBlank()) {
                                    val isWhite = piece.startsWith("W")
                                    Box(
                                        modifier = Modifier
                                            .size(26.dp)
                                            .clip(CircleShape)
                                            .background(if (isWhite) DuoMacawBlue else VaultObsidian)
                                            .border(1.5.dp, Color.White, CircleShape),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = piece,
                                            fontSize = 9.sp,
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

// =========================================================================
// 14. REVERSI OTHELLO (8x8 LAWAN AI)
// =========================================================================
@Composable
fun ReversiOthelloGameView(
    difficulty: BrainGameDifficulty,
    onWin: (Int) -> Unit
) {
    val board = remember { mutableStateListOf<Int>() } // 0=empty, 1=Player(Green), 2=AI(Purple)
    var statusText by remember { mutableStateOf("Giliran Anda (Hijau) — Jepit bidak Ungu!") }

    fun initOthello() {
        board.clear()
        repeat(64) { board.add(0) }
        board[27] = 2
        board[28] = 1
        board[35] = 1
        board[36] = 2
        statusText = "Giliran Anda (Hijau)!"
    }

    LaunchedEffect(difficulty) { initOthello() }

    fun flipsForMove(b: List<Int>, r: Int, c: Int, player: Int): List<Int> {
        if (b[r * 8 + c] != 0) return emptyList()
        val opp = if (player == 1) 2 else 1
        val allFlips = mutableListOf<Int>()
        val dirs = listOf(-1 to -1, -1 to 0, -1 to 1, 0 to -1, 0 to 1, 1 to -1, 1 to 0, 1 to 1)
        for ((dr, dc) in dirs) {
            var nr = r + dr
            var nc = c + dc
            val line = mutableListOf<Int>()
            while (nr in 0..7 && nc in 0..7 && b[nr * 8 + nc] == opp) {
                line.add(nr * 8 + nc)
                nr += dr
                nc += dc
            }
            if (line.isNotEmpty() && nr in 0..7 && nc in 0..7 && b[nr * 8 + nc] == player) {
                allFlips.addAll(line)
            }
        }
        return allFlips
    }

    val pCount = board.count { it == 1 }
    val aiCount = board.count { it == 2 }

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Anda: $pCount • AI: $aiCount • $statusText",
                fontSize = 11.sp,
                fontWeight = FontWeight.ExtraBold,
                color = VaultObsidian,
                modifier = Modifier.weight(1f)
            )
            BrainActionBtn("RESET", DuoFoxOrange, DuoFoxOrangeLip) { initOthello() }
        }

        if (board.size == 64) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(DuoFeatherGreenLip, RoundedCornerShape(12.dp))
                    .padding(4.dp),
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                for (r in 0 until 8) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(2.dp)
                    ) {
                        for (c in 0 until 8) {
                            val idx = r * 8 + c
                            val v = board[idx]
                            val legalFlips = flipsForMove(board, r, c, 1)
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .aspectRatio(1f)
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(Color(0xFF14532D))
                                    .clickable {
                                        if (legalFlips.isNotEmpty()) {
                                            board[idx] = 1
                                            legalFlips.forEach { board[it] = 1 }
                                            // AI Counter Move
                                            val aiMoves = (0 until 64).mapNotNull { i ->
                                                val fl = flipsForMove(board, i / 8, i % 8, 2)
                                                if (fl.isNotEmpty()) i to fl else null
                                            }
                                            if (aiMoves.isNotEmpty()) {
                                                val chosen = aiMoves.maxByOrNull { (pos, fl) ->
                                                    val cornerBonus = if (pos in listOf(0, 7, 56, 63)) 10 else 0
                                                    fl.size + cornerBonus
                                                }!!
                                                board[chosen.first] = 2
                                                chosen.second.forEach { board[it] = 2 }
                                            }
                                            val myAfter = board.count { it == 1 }
                                            val aiAfter = board.count { it == 2 }
                                            if (myAfter >= 24 && myAfter > aiAfter) {
                                                onWin(myAfter * 12)
                                            }
                                        }
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                when {
                                    v == 1 -> Box(
                                        modifier = Modifier
                                            .size(22.dp)
                                            .clip(CircleShape)
                                            .background(DuoFeatherGreen)
                                            .border(1.5.dp, Color.White, CircleShape)
                                    )
                                    v == 2 -> Box(
                                        modifier = Modifier
                                            .size(22.dp)
                                            .clip(CircleShape)
                                            .background(DuoBeetlePurple)
                                            .border(1.5.dp, DuoBeeYellow, CircleShape)
                                    )
                                    legalFlips.isNotEmpty() -> Box(
                                        modifier = Modifier
                                            .size(8.dp)
                                            .clip(CircleShape)
                                            .background(DuoBeeYellow.copy(alpha = 0.7f))
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

// =========================================================================
// 15. CHECKERS DAM STRATEGI
// =========================================================================
@Composable
fun CheckersDamGameView(
    difficulty: BrainGameDifficulty,
    onWin: (Int) -> Unit
) {
    val board = remember { mutableStateListOf<Int>() } // 0=empty, 1=Player, 2=AI
    var selectedIdx by remember { mutableIntStateOf(-1) }
    var capturedByPlayer by remember { mutableIntStateOf(0) }

    fun initCheckers() {
        board.clear()
        selectedIdx = -1
        capturedByPlayer = 0
        for (r in 0 until 8) {
            for (c in 0 until 8) {
                val dark = (r + c) % 2 == 1
                val v = when {
                    dark && r in 0..1 -> 2
                    dark && r in 6..7 -> 1
                    else -> 0
                }
                board.add(v)
            }
        }
    }

    LaunchedEffect(difficulty) { initCheckers() }

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Bidak Lawan Tertangkap: $capturedByPlayer",
                fontWeight = FontWeight.ExtraBold,
                fontSize = 12.sp,
                color = VaultObsidian
            )
            BrainActionBtn("RESET PAPAN", DuoFoxOrange, DuoFoxOrangeLip) { initCheckers() }
        }

        if (board.size == 64) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(DuoEelInk, RoundedCornerShape(12.dp))
                    .padding(4.dp),
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                for (r in 0 until 8) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(2.dp)
                    ) {
                        for (c in 0 until 8) {
                            val idx = r * 8 + c
                            val v = board[idx]
                            val dark = (r + c) % 2 == 1
                            val isSel = selectedIdx == idx
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .aspectRatio(1f)
                                    .background(
                                        when {
                                            isSel -> DuoBeeYellow
                                            dark -> Color(0xFF334155)
                                            else -> Color(0xFFF1F5F9)
                                        },
                                        RoundedCornerShape(4.dp)
                                    )
                                    .clickable {
                                        if (v == 1) {
                                            selectedIdx = idx
                                        } else if (selectedIdx != -1 && v == 0 && dark) {
                                            val sr = selectedIdx / 8
                                            val sc = selectedIdx % 8
                                            val dr = r - sr
                                            val dc = c - sc
                                            if (abs(dr) == 1 && abs(dc) == 1) {
                                                board[idx] = 1
                                                board[selectedIdx] = 0
                                                selectedIdx = -1
                                            } else if (abs(dr) == 2 && abs(dc) == 2) {
                                                val mid = ((sr + r) / 2) * 8 + ((sc + c) / 2)
                                                if (board[mid] == 2) {
                                                    board[mid] = 0
                                                    board[idx] = 1
                                                    board[selectedIdx] = 0
                                                    selectedIdx = -1
                                                    capturedByPlayer++
                                                    if (capturedByPlayer >= 3 || board.none { it == 2 }) {
                                                        onWin(350 + capturedByPlayer * 40)
                                                    }
                                                }
                                            }
                                        }
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                if (v == 1 || v == 2) {
                                    Box(
                                        modifier = Modifier
                                            .size(22.dp)
                                            .clip(CircleShape)
                                            .background(if (v == 1) DuoCardinalRed else DuoMacawBlue)
                                            .border(2.dp, Color.White, CircleShape)
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

// =========================================================================
// 16. SIMON SAYS POLA INGATAN 3D
// =========================================================================
@Composable
fun SimonSaysPatternGameView(
    difficulty: BrainGameDifficulty,
    onWin: (Int) -> Unit
) {
    val scope = rememberCoroutineScope()
    val rng = remember { SecureRandom() }
    val seq = remember { mutableStateListOf<Int>() }
    var step by remember { mutableIntStateOf(0) }
    var litPad by remember { mutableIntStateOf(-1) }
    var playing by remember { mutableStateOf(false) }
    var status by remember { mutableStateOf("Tekan MULAI POLA lalu ikuti urutan nyala pad!") }

    fun nextRound(reset: Boolean) {
        if (playing) return
        scope.launch {
            playing = true
            if (reset) seq.clear()
            seq.add(rng.nextInt(6))
            step = 0
            status = "Perhatikan ${seq.size} nyala pad..."
            delay(300L)
            val flashDelay = (420L - difficulty.id * 70L).coerceAtLeast(180L)
            for (p in seq) {
                litPad = p
                delay(flashDelay)
                litPad = -1
                delay(140L)
            }
            playing = false
            status = "Giliran Anda! Ketuk ${seq.size} pad secara berurutan."
        }
    }

    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(status, fontSize = 11.sp, fontWeight = FontWeight.ExtraBold, color = VaultObsidian, modifier = Modifier.weight(1f))
            BrainActionBtn("MULAI POLA", DuoFeatherGreen, DuoFeatherGreenLip) { nextRound(reset = true) }
        }

        val padColors = listOf(
            DuoFeatherGreen to DuoFeatherGreenLip,
            DuoMacawBlue to DuoMacawBlueLip,
            DuoFoxOrange to DuoFoxOrangeLip,
            DuoBeetlePurple to DuoBeetlePurpleLip,
            DuoCardinalRed to DuoCardinalRedLip,
            DuoBeeYellow to DuoBeeYellowLip
        )
        for (row in 0..1) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                for (col in 0..2) {
                    val id = row * 3 + col
                    val isLit = litPad == id
                    val (c, lip) = padColors[id]
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(74.dp)
                            .duolingoButton(
                                faceColor = if (isLit) Color.White else c,
                                lipColor = lip,
                                cornerRadius = 16.dp,
                                lipHeight = 4.dp
                            )
                            .border(if (isLit) 3.dp else 0.dp, DuoBeeYellow, RoundedCornerShape(16.dp))
                            .clickable {
                                if (!playing && seq.isNotEmpty()) {
                                    if (seq[step] == id) {
                                        step++
                                        if (step >= seq.size) {
                                            onWin(seq.size * 80)
                                            nextRound(reset = false)
                                        }
                                    } else {
                                        status = "Salah urutan! Tekan MULAI POLA lagi."
                                        seq.clear()
                                    }
                                }
                            }
                            .padding(bottom = 4.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "PAD #${id + 1}",
                            fontWeight = FontWeight.ExtraBold,
                            color = if (isLit || c == DuoBeeYellow) DuoEelInk else Color.White
                        )
                    }
                }
            }
        }
    }
}

// =========================================================================
// 17. MEMORY MATRIX GRID (REKAM SEL BERCAHAYA)
// =========================================================================
@Composable
fun MemoryMatrixGridGameView(
    difficulty: BrainGameDifficulty,
    onWin: (Int) -> Unit
) {
    val size = when (difficulty) {
        BrainGameDifficulty.MUDAH -> 4
        BrainGameDifficulty.SEDANG -> 4
        BrainGameDifficulty.SULIT -> 5
        BrainGameDifficulty.EKSTREM -> 6
    }
    val targetCount = 4 + difficulty.id * 2
    val scope = rememberCoroutineScope()
    val rng = remember { SecureRandom() }
    val targets = remember { mutableStateListOf<Int>() }
    val picked = remember { mutableStateListOf<Int>() }
    var revealing by remember { mutableStateOf(false) }

    fun startMatrix() {
        scope.launch {
            picked.clear()
            targets.clear()
            targets.addAll((0 until size * size).shuffled(rng).take(targetCount))
            revealing = true
            delay(1300L)
            revealing = false
        }
    }

    LaunchedEffect(difficulty) { startMatrix() }

    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = if (revealing) "Hafalkan posisi ubin kuning!" else "Temukan ${picked.size} / $targetCount ubin!",
                fontWeight = FontWeight.ExtraBold,
                fontSize = 12.sp,
                color = VaultObsidian
            )
            BrainActionBtn("POLA BARU", DuoMacawBlue, DuoMacawBlueLip) { startMatrix() }
        }

        Column(verticalArrangement = Arrangement.spacedBy(5.dp)) {
            for (r in 0 until size) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(5.dp)) {
                    for (c in 0 until size) {
                        val idx = r * size + c
                        val isTarget = idx in targets
                        val isPicked = idx in picked
                        val showLit = (revealing && isTarget) || isPicked
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .aspectRatio(1f)
                                .duolingoButton(
                                    faceColor = if (showLit) DuoBeeYellow else DuoMacawBlue,
                                    lipColor = if (showLit) DuoBeeYellowLip else DuoMacawBlueLip,
                                    cornerRadius = 10.dp,
                                    lipHeight = 3.dp
                                )
                                .clickable {
                                    if (!revealing && !isPicked) {
                                        if (isTarget) {
                                            picked.add(idx)
                                            if (picked.size == targets.size) {
                                                onWin(340)
                                            }
                                        } else {
                                            startMatrix()
                                        }
                                    }
                                }
                                .padding(bottom = 3.dp)
                        )
                    }
                }
            }
        }
    }
}

// =========================================================================
// 18. KARTU PASANGAN SIMBOL (MEMORY MATCH)
// =========================================================================
@Composable
fun MemoryMatchPairsGameView(
    difficulty: BrainGameDifficulty,
    onWin: (Int) -> Unit
) {
    val pairCount = when (difficulty) {
        BrainGameDifficulty.MUDAH -> 6
        BrainGameDifficulty.SEDANG -> 8
        else -> 10
    }
    val scope = rememberCoroutineScope()
    val rng = remember { SecureRandom() }
    val cards = remember { mutableStateListOf<Int>() }
    val matched = remember { mutableStateListOf<Boolean>() }
    var firstPick by remember { mutableIntStateOf(-1) }
    var secondPick by remember { mutableIntStateOf(-1) }
    var moves by remember { mutableIntStateOf(0) }

    fun initPairs() {
        cards.clear()
        matched.clear()
        firstPick = -1
        secondPick = -1
        moves = 0
        val deck = (1..pairCount).flatMap { listOf(it, it) }.shuffled(rng)
        cards.addAll(deck)
        repeat(deck.size) { matched.add(false) }
    }

    LaunchedEffect(difficulty) { initPairs() }

    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Langkah: $moves • Pasangan: ${matched.count { it } / 2}/$pairCount", fontWeight = FontWeight.ExtraBold, fontSize = 12.sp)
            BrainActionBtn("ACAK KARTU", DuoFeatherGreen, DuoFeatherGreenLip) { initPairs() }
        }

        cards.indices.chunked(4).forEach { rowIndices ->
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                rowIndices.forEach { idx ->
                    val open = matched[idx] || idx == firstPick || idx == secondPick
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(56.dp)
                            .duolingoButton(
                                faceColor = if (open) DuoBeeYellow else DuoBeetlePurple,
                                lipColor = if (open) DuoBeeYellowLip else DuoBeetlePurpleLip,
                                cornerRadius = 12.dp,
                                lipHeight = 3.dp
                            )
                            .clickable {
                                if (!open && secondPick == -1) {
                                    if (firstPick == -1) {
                                        firstPick = idx
                                    } else {
                                        secondPick = idx
                                        moves++
                                        val a = firstPick
                                        val b = secondPick
                                        if (cards[a] == cards[b]) {
                                            matched[a] = true
                                            matched[b] = true
                                            firstPick = -1
                                            secondPick = -1
                                            if (matched.all { it }) onWin(360)
                                        } else {
                                            scope.launch {
                                                delay(550L)
                                                firstPick = -1
                                                secondPick = -1
                                            }
                                        }
                                    }
                                }
                            }
                            .padding(bottom = 3.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = if (open) "#${cards[idx]}" else "?",
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 14.sp,
                            color = if (open) DuoEelInk else Color.White
                        )
                    }
                }
            }
        }
    }
}

// =========================================================================
// 19. SCHULTE TABLE FOKUS PERIFER (1..N)
// =========================================================================
@Composable
fun SchulteTableFocusGameView(
    difficulty: BrainGameDifficulty,
    onWin: (Int) -> Unit
) {
    val size = when (difficulty) {
        BrainGameDifficulty.MUDAH -> 4
        BrainGameDifficulty.SEDANG -> 5
        else -> 6
    }
    val rng = remember { SecureRandom() }
    val numbers = remember { mutableStateListOf<Int>() }
    var nextTarget by remember { mutableIntStateOf(1) }

    fun resetSchulte() {
        numbers.clear()
        numbers.addAll((1..(size * size)).shuffled(rng))
        nextTarget = 1
    }

    LaunchedEffect(difficulty) { resetSchulte() }

    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Ketuk Angka Berikutnya: $nextTarget / ${size * size}",
                fontWeight = FontWeight.ExtraBold,
                fontSize = 13.sp,
                color = DuoCardinalRed
            )
            BrainActionBtn("ACAK TABEL", DuoFoxOrange, DuoFoxOrangeLip) { resetSchulte() }
        }

        if (numbers.size == size * size) {
            Column(verticalArrangement = Arrangement.spacedBy(5.dp)) {
                for (r in 0 until size) {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(5.dp)) {
                        for (c in 0 until size) {
                            val v = numbers[r * size + c]
                            val done = v < nextTarget
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .aspectRatio(1f)
                                    .duolingoButton(
                                        faceColor = if (done) DuoFeatherGreen else DuoPureWhite,
                                        lipColor = if (done) DuoFeatherGreenLip else DuoHareLip,
                                        cornerRadius = 10.dp,
                                        lipHeight = 3.dp
                                    )
                                    .border(1.5.dp, DuoSwanBorder, RoundedCornerShape(10.dp))
                                    .clickable {
                                        if (v == nextTarget) {
                                            if (nextTarget == size * size) {
                                                onWin(380)
                                                resetSchulte()
                                            } else {
                                                nextTarget++
                                            }
                                        }
                                    }
                                    .padding(bottom = 3.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = v.toString(),
                                    fontWeight = FontWeight.ExtraBold,
                                    fontSize = 14.sp,
                                    color = if (done) Color.White else DuoEelInk
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
// 20. MATH SPRINT HITUNG CEPAT
// =========================================================================
@Composable
fun MathSprintSpeedGameView(
    difficulty: BrainGameDifficulty,
    onWin: (Int) -> Unit
) {
    val rng = remember { SecureRandom() }
    var question by remember { mutableStateOf("") }
    var answer by remember { mutableIntStateOf(0) }
    val choices = remember { mutableStateListOf<Int>() }
    var streak by remember { mutableIntStateOf(0) }

    fun nextQuestion() {
        val maxN = 12 + difficulty.id * 15
        val a = rng.nextInt(maxN) + 2
        val b = rng.nextInt(maxN) + 2
        val op = rng.nextInt(3)
        when (op) {
            0 -> {
                question = "$a + $b = ?"
                answer = a + b
            }
            1 -> {
                question = "${a + b} - $a = ?"
                answer = b
            }
            else -> {
                val m1 = (a % 12) + 2
                val m2 = (b % 12) + 2
                question = "$m1 × $m2 = ?"
                answer = m1 * m2
            }
        }
        val set = mutableSetOf(answer)
        while (set.size < 4) {
            set.add(answer + (rng.nextInt(15) - 7).let { if (it == 0) 3 else it })
        }
        choices.clear()
        choices.addAll(set.shuffled(rng))
    }

    LaunchedEffect(difficulty) { nextQuestion() }

    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text(
            text = "Kombo Benar Beruntun: $streak (Capai 5x untuk Klaim Hadiah!)",
            fontWeight = FontWeight.ExtraBold,
            fontSize = 12.sp,
            color = VaultObsidian
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .duolingoCard(DuoCardinalRedSoft, DuoCardinalRed, DuoCardinalRedLip, 18.dp, 2.dp, 4.dp)
                .padding(bottom = 4.dp)
                .padding(20.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = question,
                fontFamily = JetBrainsMonoFontFamily,
                fontSize = 26.sp,
                fontWeight = FontWeight.ExtraBold,
                color = VaultObsidian
            )
        }
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            choices.forEach { opt ->
                BrainActionBtn(
                    label = opt.toString(),
                    faceColor = DuoMacawBlue,
                    lipColor = DuoMacawBlueLip,
                    modifier = Modifier.weight(1f)
                ) {
                    if (opt == answer) {
                        streak++
                        if (streak % 5 == 0) {
                            onWin(streak * 75)
                        }
                        nextQuestion()
                    } else {
                        streak = 0
                        nextQuestion()
                    }
                }
            }
        }
    }
}

// =========================================================================
// 21. 24 GAME MATEMATIKA
// =========================================================================
@Composable
fun Math24TargetGameView(
    difficulty: BrainGameDifficulty,
    onWin: (Int) -> Unit
) {
    val presets = remember {
        listOf(
            listOf(8, 3, 4, 1) to "(8 - 4) × (3 × 2) / 8 × 3 × 1 × 1 -> (8 × 3) × (4 ÷ 4)",
            listOf(6, 4, 2, 1) to "6 × 4 × (2 - 1) = 24",
            listOf(9, 8, 3, 1) to "8 × 3 × (9 ÷ 9) atau (9 - 1) × 3 = 24",
            listOf(10, 8, 4, 2) to "10 + 8 + 4 + 2 = 24",
            listOf(12, 6, 4, 2) to "12 × (6 - 4) × (2 ÷ 2) = 24"
        )
    }
    var idx by remember { mutableIntStateOf(0) }
    val nums = remember { mutableStateListOf<Int>() }
    var firstIdx by remember { mutableIntStateOf(-1) }
    var selectedOp by remember { mutableStateOf("+") }

    fun load24() {
        nums.clear()
        nums.addAll(presets[idx % presets.size].first)
        firstIdx = -1
    }

    LaunchedEffect(idx, difficulty) { load24() }

    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Gabungkan 4 kartu angka hingga tersisa angka 24!", fontSize = 11.sp, fontWeight = FontWeight.ExtraBold, modifier = Modifier.weight(1f))
            BrainActionBtn("RESET", DuoFoxOrange, DuoFoxOrangeLip) { load24() }
            Spacer(modifier = Modifier.size(6.dp))
            BrainActionBtn("SOAL BARU", DuoFeatherGreen, DuoFeatherGreenLip) { idx++ }
        }

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            nums.forEachIndexed { i, v ->
                val isSel = firstIdx == i
                BrainActionBtn(
                    label = v.toString(),
                    faceColor = if (isSel) DuoBeeYellow else DuoMacawBlue,
                    lipColor = if (isSel) DuoBeeYellowLip else DuoMacawBlueLip,
                    modifier = Modifier.weight(1f)
                ) {
                    if (firstIdx == -1) {
                        firstIdx = i
                    } else if (firstIdx == i) {
                        firstIdx = -1
                    } else {
                        val a = nums[firstIdx]
                        val b = nums[i]
                        val res = when (selectedOp) {
                            "+" -> a + b
                            "-" -> a - b
                            "×" -> a * b
                            else -> if (b != 0 && a % b == 0) a / b else a + b
                        }
                        val hi = maxOf(firstIdx, i)
                        val lo = minOf(firstIdx, i)
                        nums.removeAt(hi)
                        nums.removeAt(lo)
                        nums.add(res)
                        firstIdx = -1
                        if (nums.size == 1 && nums.first() == 24) {
                            onWin(400)
                        }
                    }
                }
            }
        }

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            listOf("+", "-", "×", "÷").forEach { op ->
                BrainActionBtn(
                    label = "Operator $op",
                    faceColor = if (selectedOp == op) DuoCardinalRed else DuoBeetlePurple,
                    lipColor = if (selectedOp == op) DuoCardinalRedLip else DuoBeetlePurpleLip,
                    modifier = Modifier.weight(1f)
                ) { selectedOp = op }
            }
        }
    }
}

// =========================================================================
// 22. MASTERMIND KODE RAHASIA (BULLS & COWS)
// =========================================================================
@Composable
fun MastermindCodeGameView(
    difficulty: BrainGameDifficulty,
    onWin: (Int) -> Unit
) {
    val rng = remember { SecureRandom() }
    val secret = remember { mutableStateListOf<Int>() }
    val currentGuess = remember { mutableStateListOf(1, 2, 3, 4) }
    val history = remember { mutableStateListOf<String>() }

    fun resetMastermind() {
        secret.clear()
        history.clear()
        secret.addAll((1..6).shuffled(rng).take(4))
    }

    LaunchedEffect(difficulty) { resetMastermind() }

    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Ketuk angka (1..6) lalu tekan CEK KODE!", fontSize = 11.sp, fontWeight = FontWeight.ExtraBold)
            BrainActionBtn("KODE BARU", DuoFoxOrange, DuoFoxOrangeLip) { resetMastermind() }
        }

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            for (i in 0..3) {
                BrainActionBtn(
                    label = "Slot ${i + 1}: [${currentGuess[i]}]",
                    faceColor = DuoBeetlePurple,
                    lipColor = DuoBeetlePurpleLip,
                    modifier = Modifier.weight(1f)
                ) {
                    currentGuess[i] = (currentGuess[i] % 6) + 1
                }
            }
        }

        BrainActionBtn("CEK KOMBINASI KODE", DuoFeatherGreen, DuoFeatherGreenLip, Modifier.fillMaxWidth()) {
            val bulls = (0..3).count { currentGuess[it] == secret[it] }
            val cows = currentGuess.distinct().count { it in secret } - bulls
            history.add(0, "Tebakan ${currentGuess.joinToString("")} -> $bulls Tepat Posisi (Bull), $cows Tepat Angka (Cow)")
            if (bulls == 4) {
                onWin(420)
            }
        }

        history.take(5).forEach { line ->
            Text(line, fontFamily = JetBrainsMonoFontFamily, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = VaultObsidian)
        }
    }
}

// =========================================================================
// 23. WORDLE KATA SANDI (5 HURUF)
// =========================================================================
@Composable
fun WordleSecretWordGameView(
    difficulty: BrainGameDifficulty,
    onWin: (Int) -> Unit
) {
    val words = remember { listOf("SANDI", "KUNCI", "LOGIK", "CIPTA", "RADAR", "PIXEL", "CYBER", " VAULT".trim()) }
    var target by remember(difficulty) { mutableStateOf(words[difficulty.id % words.size]) }
    var guessInput by remember { mutableStateOf("") }
    val guesses = remember { mutableStateListOf<String>() }

    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Tebak Kata Rahasia ${target.length} Huruf!", fontWeight = FontWeight.ExtraBold, fontSize = 12.sp)
            BrainActionBtn("GANTI KATA", DuoFoxOrange, DuoFoxOrangeLip) {
                guesses.clear()
                guessInput = ""
                target = words.random()
            }
        }

        guesses.forEach { g ->
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                for (i in target.indices) {
                    val ch = g.getOrNull(i) ?: ' '
                    val (bg, lip) = when {
                        ch == target[i] -> DuoFeatherGreen to DuoFeatherGreenLip
                        ch in target -> DuoBeeYellow to DuoBeeYellowLip
                        else -> DuoMutedInk to DuoEelInk
                    }
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(42.dp)
                            .duolingoButton(bg, lip, 10.dp, 3.dp)
                            .padding(bottom = 3.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(ch.toString(), fontWeight = FontWeight.ExtraBold, color = if (bg == DuoBeeYellow) DuoEelInk else Color.White)
                    }
                }
            }
        }

        OutlinedTextField(
            value = guessInput,
            onValueChange = { guessInput = it.uppercase().take(target.length) },
            label = { Text("Ketik ${target.length} Huruf (Contoh: SANDI)") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        BrainActionBtn("KIRIM TEBAKAN KATA", DuoFeatherGreen, DuoFeatherGreenLip, Modifier.fillMaxWidth()) {
            if (guessInput.length == target.length) {
                guesses.add(guessInput)
                if (guessInput == target) {
                    onWin(390)
                }
                guessInput = ""
            }
        }
    }
}

// =========================================================================
// 24. CRYPTOGRAM DEKRIPSI SANDI CAESAR
// =========================================================================
@Composable
fun CryptogramCipherGameView(
    difficulty: BrainGameDifficulty,
    onWin: (Int) -> Unit
) {
    val phrases = remember {
        listOf(
            "BRANKAS AMAN",
            "KUNCI RAHASIA",
            "LOGIKA CERDAS",
            "ENKRIPSI KUAT"
        )
    }
    var plain by remember(difficulty) { mutableStateOf(phrases[difficulty.id % phrases.size]) }
    val shift = remember(difficulty) { 3 + difficulty.id }
    val cipher = remember(plain, shift) {
        plain.map { ch ->
            if (ch in 'A'..'Z') 'A' + ((ch - 'A' + shift) % 26) else ch
        }.joinToString("")
    }
    var userDecode by remember { mutableStateOf("") }

    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Text(
            text = "Pesan Sandi (Caesar Shift +$shift): $cipher",
            fontFamily = JetBrainsMonoFontFamily,
            fontSize = 14.sp,
            fontWeight = FontWeight.ExtraBold,
            color = DuoBeetlePurpleLip
        )
        Text(
            text = "Petunjuk Huruf Pertama: '${cipher.first()}' -> '${plain.first()}'",
            fontSize = 11.sp,
            color = DuoMutedInk
        )
        OutlinedTextField(
            value = userDecode,
            onValueChange = { userDecode = it.uppercase() },
            label = { Text("Ketik Hasil Dekripsi") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            BrainActionBtn("VERIFIKASI SANDI", DuoFeatherGreen, DuoFeatherGreenLip, Modifier.weight(1f)) {
                if (userDecode.trim() == plain) {
                    onWin(420)
                }
            }
            BrainActionBtn("ISI OTOMATIS", DuoMacawBlue, DuoMacawBlueLip) {
                userDecode = plain
            }
        }
    }
}

// =========================================================================
// EMBEDDED LEGACY ARCADE 3D & GACHA MASKOT
// =========================================================================
@Composable
fun LegacyArcadeEmbeddedSection(
    initialMode: Int,
    prefs: SharedPreferences,
    cosmicCoins: Int,
    onUpdateCoins: (Int) -> Unit,
    onUnlockMascot: (Int) -> Unit,
    onPostBanner: (String) -> Unit
) {
    NyxRetroArcadeAndMascotHubSection(
        prefs = prefs,
        cosmicCoins = cosmicCoins,
        onUpdateCoins = onUpdateCoins,
        onUnlockMascot = onUnlockMascot,
        onPostBanner = onPostBanner
    )
}
