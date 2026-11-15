package com.example.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import com.example.ui.theme.duolingoButton
import com.example.ui.theme.duolingoCard
import java.security.SecureRandom
import kotlin.math.abs

@Composable
fun BrainActionBtn(
    label: String,
    faceColor: Color = DuoFeatherGreen,
    lipColor: Color = DuoFeatherGreenLip,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .height(38.dp)
            .duolingoButton(
                faceColor = faceColor,
                lipColor = lipColor,
                cornerRadius = 12.dp,
                lipHeight = 3.dp
            )
            .clickable { onClick() }
            .padding(horizontal = 12.dp)
            .padding(bottom = 3.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            fontSize = 11.sp,
            fontWeight = FontWeight.ExtraBold,
            color = if (faceColor == DuoBeeYellow) DuoEelInk else Color.White,
            maxLines = 1
        )
    }
}

// =========================================================================
// 1. SUDOKU KLASIK (4x4, 6x6, 9x9)
// =========================================================================
@Composable
fun SudokuClassicGameView(
    difficulty: BrainGameDifficulty,
    onWin: (Int) -> Unit
) {
    val gridSize = when (difficulty) {
        BrainGameDifficulty.MUDAH -> 4
        BrainGameDifficulty.SEDANG -> 6
        else -> 9
    }
    val subRows = when (gridSize) {
        4 -> 2
        6 -> 2
        else -> 3
    }
    val subCols = when (gridSize) {
        4 -> 2
        6 -> 3
        else -> 3
    }
    val rng = remember { SecureRandom() }
    val solution = remember { mutableStateListOf<Int>() }
    val board = remember { mutableStateListOf<Int>() }
    val fixed = remember { mutableStateListOf<Boolean>() }
    var selectedCell by remember { mutableIntStateOf(0) }
    var mistakes by remember { mutableIntStateOf(0) }
    var solvedBanner by remember { mutableStateOf(false) }

    fun generateSudoku() {
        solution.clear()
        board.clear()
        fixed.clear()
        mistakes = 0
        solvedBanner = false
        selectedCell = 0
        val shift = rng.nextInt(gridSize)
        for (r in 0 until gridSize) {
            for (c in 0 until gridSize) {
                val v = ((r * subCols + r / subRows + c + shift) % gridSize) + 1
                solution.add(v)
            }
        }
        val blanksTarget = when (difficulty) {
            BrainGameDifficulty.MUDAH -> 6
            BrainGameDifficulty.SEDANG -> 14
            BrainGameDifficulty.SULIT -> 34
            BrainGameDifficulty.EKSTREM -> 46
        }
        val blankIndices = (0 until (gridSize * gridSize)).shuffled(rng).take(blanksTarget).toSet()
        for (i in 0 until (gridSize * gridSize)) {
            if (i in blankIndices) {
                board.add(0)
                fixed.add(false)
            } else {
                board.add(solution[i])
                fixed.add(true)
            }
        }
        selectedCell = blankIndices.firstOrNull() ?: 0
    }

    LaunchedEffect(difficulty) { generateSudoku() }

    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Grid ${gridSize}x$gridSize • Kesalahan: $mistakes",
                fontWeight = FontWeight.ExtraBold,
                fontSize = 12.sp,
                color = VaultObsidian
            )
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                BrainActionBtn("BANTUAN", DuoMacawBlue, DuoMacawBlueLip) {
                    if (selectedCell in board.indices && board[selectedCell] != solution[selectedCell]) {
                        board[selectedCell] = solution[selectedCell]
                        if (board.indices.all { board[it] == solution[it] } && !solvedBanner) {
                            solvedBanner = true
                            onWin((300 - mistakes * 25).coerceAtLeast(80))
                        }
                    }
                }
                BrainActionBtn(" PAPAN BARU", DuoFoxOrange, DuoFoxOrangeLip) { generateSudoku() }
            }
        }

        if (board.size == gridSize * gridSize) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(DuoEelInk, RoundedCornerShape(14.dp))
                    .padding(6.dp),
                verticalArrangement = Arrangement.spacedBy(3.dp)
            ) {
                for (r in 0 until gridSize) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(3.dp)
                    ) {
                        for (c in 0 until gridSize) {
                            val idx = r * gridSize + c
                            val v = board[idx]
                            val isFixed = fixed[idx]
                            val isSel = selectedCell == idx
                            val isWrong = v != 0 && v != solution[idx]
                            val blockAlt = ((r / subRows) + (c / subCols)) % 2 == 0
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .aspectRatio(1f)
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(
                                        when {
                                            isSel -> DuoBeeYellow
                                            isWrong -> DuoCardinalRedSoft
                                            blockAlt -> DuoPureWhite
                                            else -> Color(0xFFE8F2FF)
                                        }
                                    )
                                    .border(
                                        width = if (isSel) 2.dp else 1.dp,
                                        color = if (isSel) DuoFoxOrange else DuoSwanBorder,
                                        shape = RoundedCornerShape(6.dp)
                                    )
                                    .clickable { selectedCell = idx },
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = if (v == 0) "" else v.toString(),
                                    fontSize = if (gridSize == 9) 13.sp else 16.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = when {
                                        isWrong -> DuoCardinalRed
                                        isFixed -> DuoEelInk
                                        else -> DuoMacawBlueLip
                                    }
                                )
                            }
                        }
                    }
                }
            }

            // Number Pad 1..gridSize
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                for (num in 1..gridSize) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(42.dp)
                            .duolingoButton(
                                faceColor = DuoMacawBlue,
                                lipColor = DuoMacawBlueLip,
                                cornerRadius = 10.dp,
                                lipHeight = 3.dp
                            )
                            .clickable {
                                if (selectedCell in board.indices && !fixed[selectedCell]) {
                                    board[selectedCell] = num
                                    if (num != solution[selectedCell]) {
                                        mistakes++
                                    } else if (board.indices.all { board[it] == solution[it] } && !solvedBanner) {
                                        solvedBanner = true
                                        onWin((350 - mistakes * 25).coerceAtLeast(100))
                                    }
                                }
                            }
                            .padding(bottom = 3.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = num.toString(),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color.White
                        )
                    }
                }
            }
        }
    }
}

// =========================================================================
// 2. NONOGRAM PICROSS (5x5 .. 8x8)
// =========================================================================
@Composable
fun NonogramPicrossGameView(
    difficulty: BrainGameDifficulty,
    onWin: (Int) -> Unit
) {
    val size = when (difficulty) {
        BrainGameDifficulty.MUDAH -> 5
        BrainGameDifficulty.SEDANG -> 6
        BrainGameDifficulty.SULIT -> 7
        BrainGameDifficulty.EKSTREM -> 8
    }
    val rng = remember { SecureRandom() }
    val target = remember { mutableStateListOf<Boolean>() }
    val userState = remember { mutableStateListOf<Int>() } // 0=empty, 1=filled, 2=marked X
    var markXMode by remember { mutableStateOf(false) }
    var solved by remember { mutableStateOf(false) }

    fun newNonogram() {
        target.clear()
        userState.clear()
        solved = false
        for (i in 0 until size * size) {
            target.add(rng.nextInt(100) < 58)
            userState.add(0)
        }
        if (target.none { it }) target[0] = true
    }

    LaunchedEffect(difficulty) { newNonogram() }

    fun clueForLine(bits: List<Boolean>): String {
        val runs = mutableListOf<Int>()
        var cur = 0
        for (b in bits) {
            if (b) cur++ else if (cur > 0) {
                runs.add(cur)
                cur = 0
            }
        }
        if (cur > 0) runs.add(cur)
        return if (runs.isEmpty()) "0" else runs.joinToString(" ")
    }

    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            BrainActionBtn(
                label = if (markXMode) "MODE: TANDAI X" else "MODE: ISI PIKSEL",
                faceColor = if (markXMode) DuoFoxOrange else DuoBeetlePurple,
                lipColor = if (markXMode) DuoFoxOrangeLip else DuoBeetlePurpleLip
            ) { markXMode = !markXMode }

            BrainActionBtn("POLA BARU", DuoFeatherGreen, DuoFeatherGreenLip) { newNonogram() }
        }

        if (target.size == size * size) {
            // Column Clues Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Spacer(modifier = Modifier.width(48.dp))
                for (c in 0 until size) {
                    val colBits = (0 until size).map { r -> target[r * size + c] }
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .background(DuoBeetlePurpleSoft, RoundedCornerShape(6.dp))
                            .padding(vertical = 4.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = clueForLine(colBits).replace(" ", "\n"),
                            fontSize = 10.sp,
                            lineHeight = 11.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = DuoBeetlePurpleLip,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }

            // Rows with Row Clue + Cells
            for (r in 0 until size) {
                val rowBits = (0 until size).map { c -> target[r * size + c] }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .width(48.dp)
                            .height(36.dp)
                            .background(DuoBeetlePurpleSoft, RoundedCornerShape(6.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = clueForLine(rowBits),
                            fontSize = 10.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = DuoBeetlePurpleLip
                        )
                    }
                    for (c in 0 until size) {
                        val idx = r * size + c
                        val st = userState[idx]
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(36.dp)
                                .duolingoButton(
                                    faceColor = when (st) {
                                        1 -> DuoBeetlePurple
                                        2 -> DuoCardinalRedSoft
                                        else -> DuoSnowCanvas
                                    },
                                    lipColor = if (st == 1) DuoBeetlePurpleLip else DuoHareLip,
                                    cornerRadius = 8.dp,
                                    lipHeight = 2.dp
                                )
                                .clickable {
                                    userState[idx] = if (markXMode) {
                                        if (st == 2) 0 else 2
                                    } else {
                                        if (st == 1) 0 else 1
                                    }
                                    val winNow = (0 until size * size).all { i ->
                                        (userState[i] == 1) == target[i]
                                    }
                                    if (winNow && !solved) {
                                        solved = true
                                        onWin(320)
                                    }
                                }
                                .padding(bottom = 2.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            if (st == 2) {
                                Text("X", fontSize = 12.sp, fontWeight = FontWeight.ExtraBold, color = DuoCardinalRed)
                            }
                        }
                    }
                }
            }
        }
    }
}

// =========================================================================
// 3. MINESWEEPER LOGIKA BOM
// =========================================================================
@Composable
fun MinesweeperLogicGameView(
    difficulty: BrainGameDifficulty,
    onWin: (Int) -> Unit
) {
    val size = when (difficulty) {
        BrainGameDifficulty.MUDAH -> 6
        BrainGameDifficulty.SEDANG -> 7
        BrainGameDifficulty.SULIT -> 8
        BrainGameDifficulty.EKSTREM -> 9
    }
    val bombCount = when (difficulty) {
        BrainGameDifficulty.MUDAH -> 5
        BrainGameDifficulty.SEDANG -> 8
        BrainGameDifficulty.SULIT -> 12
        BrainGameDifficulty.EKSTREM -> 17
    }
    val rng = remember { SecureRandom() }
    val bombs = remember { mutableStateListOf<Boolean>() }
    val revealed = remember { mutableStateListOf<Boolean>() }
    val flagged = remember { mutableStateListOf<Boolean>() }
    var flagMode by remember { mutableStateOf(false) }
    var gameOver by remember { mutableStateOf(false) }
    var gameWon by remember { mutableStateOf(false) }

    fun resetMines() {
        bombs.clear()
        revealed.clear()
        flagged.clear()
        gameOver = false
        gameWon = false
        val bombIndices = (0 until size * size).shuffled(rng).take(bombCount).toSet()
        for (i in 0 until size * size) {
            bombs.add(i in bombIndices)
            revealed.add(false)
            flagged.add(false)
        }
    }

    LaunchedEffect(difficulty) { resetMines() }

    fun countNeighborBombs(r: Int, c: Int): Int {
        var count = 0
        for (dr in -1..1) {
            for (dc in -1..1) {
                if (dr == 0 && dc == 0) continue
                val nr = r + dr
                val nc = c + dc
                if (nr in 0 until size && nc in 0 until size && bombs[nr * size + nc]) count++
            }
        }
        return count
    }

    fun floodReveal(r: Int, c: Int) {
        if (r !in 0 until size || c !in 0 until size) return
        val idx = r * size + c
        if (revealed[idx] || flagged[idx]) return
        revealed[idx] = true
        if (countNeighborBombs(r, c) == 0 && !bombs[idx]) {
            for (dr in -1..1) {
                for (dc in -1..1) {
                    if (dr != 0 || dc != 0) floodReveal(r + dr, c + dc)
                }
            }
        }
    }

    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            BrainActionBtn(
                label = if (flagMode) "MODE: BENDERA" else "MODE: BUKA SEL",
                faceColor = if (flagMode) DuoFoxOrange else DuoFeatherGreen,
                lipColor = if (flagMode) DuoFoxOrangeLip else DuoFeatherGreenLip
            ) { flagMode = !flagMode }

            Text(
                text = "Bom: $bombCount • Bendera: ${flagged.count { it }}",
                fontSize = 11.sp,
                fontWeight = FontWeight.ExtraBold,
                color = VaultObsidian
            )

            BrainActionBtn("ULANG", DuoCardinalRed, DuoCardinalRedLip) { resetMines() }
        }

        if (bombs.size == size * size) {
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                for (r in 0 until size) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        for (c in 0 until size) {
                            val idx = r * size + c
                            val isRev = revealed[idx]
                            val isFlg = flagged[idx]
                            val isBomb = bombs[idx]
                            val nCount = countNeighborBombs(r, c)
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .aspectRatio(1f)
                                    .duolingoButton(
                                        faceColor = when {
                                            isRev && isBomb -> DuoCardinalRed
                                            isRev -> DuoPureWhite
                                            isFlg -> DuoBeeYellow
                                            else -> DuoMacawBlue
                                        },
                                        lipColor = when {
                                            isRev && isBomb -> DuoCardinalRedLip
                                            isRev -> DuoHareLip
                                            isFlg -> DuoBeeYellowLip
                                            else -> DuoMacawBlueLip
                                        },
                                        cornerRadius = 8.dp,
                                        lipHeight = 2.dp
                                    )
                                    .clickable {
                                        if (gameOver || gameWon) return@clickable
                                        if (flagMode) {
                                            if (!isRev) flagged[idx] = !flagged[idx]
                                        } else if (!isFlg) {
                                            if (isBomb) {
                                                // Safe first tap rescue
                                                if (revealed.none { it }) {
                                                    bombs[idx] = false
                                                    floodReveal(r, c)
                                                } else {
                                                    for (i in bombs.indices) if (bombs[i]) revealed[i] = true
                                                    gameOver = true
                                                }
                                            } else {
                                                floodReveal(r, c)
                                                val allSafeOpened = bombs.indices.all { i -> bombs[i] || revealed[i] }
                                                if (allSafeOpened) {
                                                    gameWon = true
                                                    onWin(360)
                                                }
                                            }
                                        }
                                    }
                                    .padding(bottom = 2.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = when {
                                        isRev && isBomb -> "*"
                                        isRev && nCount > 0 -> nCount.toString()
                                        !isRev && isFlg -> "F"
                                        else -> ""
                                    },
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = if (isRev && !isBomb) DuoEelInk else Color.White
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
// 4. NUMBER LINK (SAMBUNG ANGKA PASANGAN)
// =========================================================================
@Composable
fun NumberLinkPuzzleGameView(
    difficulty: BrainGameDifficulty,
    onWin: (Int) -> Unit
) {
    val size = when (difficulty) {
        BrainGameDifficulty.MUDAH -> 5
        BrainGameDifficulty.SEDANG -> 6
        else -> 7
    }
    var activeColorId by remember { mutableIntStateOf(1) }
    val grid = remember { mutableStateListOf<Int>() }
    val endpoints = remember { mutableStateListOf<Boolean>() }
    val palette = listOf(
        DuoSnowCanvas,
        DuoCardinalRed,
        DuoFeatherGreen,
        DuoMacawBlue,
        DuoFoxOrange,
        DuoBeetlePurple
    )

    fun setupNumberLink() {
        grid.clear()
        endpoints.clear()
        for (i in 0 until size * size) {
            grid.add(0)
            endpoints.add(false)
        }
        // Place solvable non-overlapping horizontal/L pairs
        val pairCount = minOf(5, size)
        for (p in 1..pairCount) {
            val row = p - 1
            val startIdx = row * size
            val endIdx = row * size + (size - 1)
            grid[startIdx] = p
            grid[endIdx] = p
            endpoints[startIdx] = true
            endpoints[endIdx] = true
        }
    }

    LaunchedEffect(difficulty) { setupNumberLink() }

    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                for (p in 1..minOf(5, size)) {
                    Box(
                        modifier = Modifier
                            .size(34.dp)
                            .clip(CircleShape)
                            .background(palette[p])
                            .border(if (activeColorId == p) 3.dp else 1.dp, DuoEelInk, CircleShape)
                            .clickable { activeColorId = p },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(p.toString(), color = Color.White, fontWeight = FontWeight.ExtraBold, fontSize = 12.sp)
                    }
                }
            }
            BrainActionBtn("RESET", DuoFoxOrange, DuoFoxOrangeLip) { setupNumberLink() }
        }

        Text(
            text = "Pilih angka warna di atas lalu ketuk sel jalur untuk menyambungkan semua pasangan & mengisi seluruh grid!",
            fontSize = 11.sp,
            color = DuoMutedInk
        )

        if (grid.size == size * size) {
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                for (r in 0 until size) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        for (c in 0 until size) {
                            val idx = r * size + c
                            val v = grid[idx]
                            val isEnd = endpoints[idx]
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .aspectRatio(1f)
                                    .duolingoButton(
                                        faceColor = palette.getOrElse(v) { DuoSnowCanvas },
                                        lipColor = if (v > 0) DuoEelInk else DuoHareLip,
                                        cornerRadius = 10.dp,
                                        lipHeight = 3.dp
                                    )
                                    .clickable {
                                        if (isEnd) {
                                            activeColorId = v
                                        } else {
                                            grid[idx] = if (grid[idx] == activeColorId) 0 else activeColorId
                                            if (grid.all { it > 0 }) {
                                                onWin(310)
                                            }
                                        }
                                    }
                                    .padding(bottom = 3.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                if (isEnd || v > 0) {
                                    Text(
                                        text = if (isEnd) "[$v]" else v.toString(),
                                        color = Color.White,
                                        fontWeight = FontWeight.ExtraBold,
                                        fontSize = 13.sp
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
// 5. 2048 MERGE PUZZLE
// =========================================================================
@Composable
fun Merge2048GameView(
    difficulty: BrainGameDifficulty,
    onWin: (Int) -> Unit
) {
    val size = if (difficulty == BrainGameDifficulty.EKSTREM) 5 else 4
    val rng = remember { SecureRandom() }
    val tiles = remember { mutableStateListOf<Int>() }
    var score by remember { mutableIntStateOf(0) }

    fun spawnTile() {
        val empties = tiles.indices.filter { tiles[it] == 0 }
        if (empties.isNotEmpty()) {
            val pick = empties[rng.nextInt(empties.size)]
            tiles[pick] = if (rng.nextInt(10) == 0) 4 else 2
        }
    }

    fun init2048() {
        tiles.clear()
        score = 0
        repeat(size * size) { tiles.add(0) }
        spawnTile()
        spawnTile()
    }

    LaunchedEffect(difficulty) { init2048() }

    fun slideLine(line: List<Int>): Pair<List<Int>, Int> {
        val nonZero = line.filter { it > 0 }.toMutableList()
        val result = mutableListOf<Int>()
        var gained = 0
        var i = 0
        while (i < nonZero.size) {
            if (i + 1 < nonZero.size && nonZero[i] == nonZero[i + 1]) {
                val merged = nonZero[i] * 2
                result.add(merged)
                gained += merged
                i += 2
            } else {
                result.add(nonZero[i])
                i++
            }
        }
        while (result.size < size) result.add(0)
        return result to gained
    }

    fun move(dir: Int) { // 0=Left, 1=Right, 2=Up, 3=Down
        if (tiles.size != size * size) return
        var changed = false
        var totalGain = 0
        for (idx in 0 until size) {
            val indices = when (dir) {
                0 -> (0 until size).map { c -> idx * size + c }
                1 -> (size - 1 downTo 0).map { c -> idx * size + c }
                2 -> (0 until size).map { r -> r * size + idx }
                else -> (size - 1 downTo 0).map { r -> r * size + idx }
            }
            val currentLine = indices.map { tiles[it] }
            val (newLine, gain) = slideLine(currentLine)
            totalGain += gain
            indices.forEachIndexed { pos, boardIdx ->
                if (tiles[boardIdx] != newLine[pos]) {
                    tiles[boardIdx] = newLine[pos]
                    changed = true
                }
            }
        }
        if (changed) {
            score += totalGain
            spawnTile()
            val maxTile = tiles.maxOrNull() ?: 0
            if (maxTile >= 128 && totalGain >= 64) {
                onWin(score)
            }
        }
    }

    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Skor 2048: $score • Ubin Tertinggi: ${tiles.maxOrNull() ?: 2}",
                fontWeight = FontWeight.ExtraBold,
                fontSize = 12.sp,
                color = VaultObsidian
            )
            BrainActionBtn("RESET", DuoFoxOrange, DuoFoxOrangeLip) { init2048() }
        }

        if (tiles.size == size * size) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(DuoSwanBorder, RoundedCornerShape(16.dp))
                    .padding(8.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                for (r in 0 until size) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        for (c in 0 until size) {
                            val v = tiles[r * size + c]
                            val (face, lip) = when {
                                v == 0 -> DuoSnowCanvas to DuoHareLip
                                v <= 4 -> DuoBeeYellow to DuoBeeYellowLip
                                v <= 16 -> DuoFoxOrange to DuoFoxOrangeLip
                                v <= 64 -> DuoCardinalRed to DuoCardinalRedLip
                                v <= 256 -> DuoFeatherGreen to DuoFeatherGreenLip
                                else -> DuoBeetlePurple to DuoBeetlePurpleLip
                            }
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .aspectRatio(1f)
                                    .duolingoButton(face, lip, cornerRadius = 10.dp, lipHeight = 3.dp)
                                    .padding(bottom = 3.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                if (v > 0) {
                                    Text(
                                        text = v.toString(),
                                        fontWeight = FontWeight.ExtraBold,
                                        fontSize = if (v >= 1024) 12.sp else 16.sp,
                                        color = if (v <= 4) DuoEelInk else Color.White
                                    )
                                }
                            }
                        }
                    }
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                BrainActionBtn("← KIRI", DuoMacawBlue, DuoMacawBlueLip, Modifier.weight(1f)) { move(0) }
                BrainActionBtn("↑ ATAS", DuoFeatherGreen, DuoFeatherGreenLip, Modifier.weight(1f)) { move(2) }
                BrainActionBtn("↓ BAWAH", DuoFeatherGreen, DuoFeatherGreenLip, Modifier.weight(1f)) { move(3) }
                BrainActionBtn("KANAN →", DuoMacawBlue, DuoMacawBlueLip, Modifier.weight(1f)) { move(1) }
            }
        }
    }
}

// =========================================================================
// 6. BLOCK BLAST PUZZLE (8x8)
// =========================================================================
@Composable
fun BlockBlastPuzzleGameView(
    difficulty: BrainGameDifficulty,
    onWin: (Int) -> Unit
) {
    val size = 8
    val board = remember { mutableStateListOf<Int>() }
    var score by remember { mutableIntStateOf(0) }
    var selectedShapeIdx by remember { mutableIntStateOf(0) }
    val shapes = remember {
        listOf(
            "Garis 3 Horizontal" to listOf(0 to 0, 0 to 1, 0 to 2),
            "Garis 3 Vertikal" to listOf(0 to 0, 1 to 0, 2 to 0),
            "Kotak 2x2" to listOf(0 to 0, 0 to 1, 1 to 0, 1 to 1),
            "Sudut L" to listOf(0 to 0, 1 to 0, 1 to 1),
            "Titik Tunggal" to listOf(0 to 0)
        )
    }

    fun resetBlast() {
        board.clear()
        score = 0
        repeat(size * size) { board.add(0) }
    }

    LaunchedEffect(difficulty) { resetBlast() }

    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Skor Block Blast: $score", fontWeight = FontWeight.ExtraBold, fontSize = 12.sp, color = VaultObsidian)
            BrainActionBtn("PAPANKAN ULANG", DuoFoxOrange, DuoFoxOrangeLip) { resetBlast() }
        }

        // Shape Selector
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            shapes.forEachIndexed { idx, (name, _) ->
                val isSel = selectedShapeIdx == idx
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(36.dp)
                        .duolingoButton(
                            faceColor = if (isSel) DuoMacawBlue else DuoSnowCanvas,
                            lipColor = if (isSel) DuoMacawBlueLip else DuoHareLip,
                            cornerRadius = 10.dp,
                            lipHeight = 2.dp
                        )
                        .clickable { selectedShapeIdx = idx }
                        .padding(bottom = 2.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = name.substringBefore(" "),
                        fontSize = 10.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = if (isSel) Color.White else DuoEelInk
                    )
                }
            }
        }

        if (board.size == size * size) {
            Column(verticalArrangement = Arrangement.spacedBy(3.dp)) {
                for (r in 0 until size) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(3.dp)
                    ) {
                        for (c in 0 until size) {
                            val filled = board[r * size + c] > 0
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .aspectRatio(1f)
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(if (filled) DuoMacawBlue else DuoSnowCanvas)
                                    .border(1.dp, DuoSwanBorder, RoundedCornerShape(6.dp))
                                    .clickable {
                                        val offsets = shapes[selectedShapeIdx].second
                                        val canPlace = offsets.all { (dr, dc) ->
                                            val nr = r + dr
                                            val nc = c + dc
                                            nr in 0 until size && nc in 0 until size && board[nr * size + nc] == 0
                                        }
                                        if (canPlace) {
                                            offsets.forEach { (dr, dc) ->
                                                board[(r + dr) * size + (c + dc)] = 1
                                            }
                                            score += offsets.size * 10
                                            // Check full rows & cols
                                            val fullRows = (0 until size).filter { row ->
                                                (0 until size).all { col -> board[row * size + col] > 0 }
                                            }
                                            val fullCols = (0 until size).filter { col ->
                                                (0 until size).all { row -> board[row * size + col] > 0 }
                                            }
                                            fullRows.forEach { row ->
                                                for (col in 0 until size) board[row * size + col] = 0
                                            }
                                            fullCols.forEach { col ->
                                                for (row in 0 until size) board[row * size + col] = 0
                                            }
                                            val linesCleared = fullRows.size + fullCols.size
                                            if (linesCleared > 0) {
                                                score += linesCleared * 120
                                                onWin(score)
                                            }
                                            selectedShapeIdx = (selectedShapeIdx + 1) % shapes.size
                                        }
                                    }
                            )
                        }
                    }
                }
            }
        }
    }
}

// =========================================================================
// 7. UNBLOCK ME (GESER BALOK INTI EMAS)
// =========================================================================
private data class SliderBlock(
    val id: Int,
    val row: Int,
    val col: Int,
    val length: Int,
    val horizontal: Boolean,
    val isTarget: Boolean = false
)

@Composable
fun UnblockMeSliderGameView(
    difficulty: BrainGameDifficulty,
    onWin: (Int) -> Unit
) {
    val blocks = remember { mutableStateListOf<SliderBlock>() }
    var selectedBlockId by remember { mutableIntStateOf(1) }
    var moves by remember { mutableIntStateOf(0) }

    fun initUnblock() {
        blocks.clear()
        moves = 0
        selectedBlockId = 1
        blocks.add(SliderBlock(1, row = 2, col = 0, length = 2, horizontal = true, isTarget = true))
        blocks.add(SliderBlock(2, row = 1, col = 2, length = 3, horizontal = false))
        blocks.add(SliderBlock(3, row = 0, col = 4, length = 3, horizontal = false))
        blocks.add(SliderBlock(4, row = 4, col = 1, length = 3, horizontal = true))
        if (difficulty.id >= 1) {
            blocks.add(SliderBlock(5, row = 0, col = 0, length = 2, horizontal = false))
        }
        if (difficulty.id >= 2) {
            blocks.add(SliderBlock(6, row = 3, col = 3, length = 2, horizontal = false))
        }
    }

    LaunchedEffect(difficulty) { initUnblock() }

    fun occupiedOwner(r: Int, c: Int): SliderBlock? {
        return blocks.firstOrNull { b ->
            if (b.horizontal) {
                r == b.row && c in b.col until (b.col + b.length)
            } else {
                c == b.col && r in b.row until (b.row + b.length)
            }
        }
    }

    fun stepBlock(delta: Int) {
        val idx = blocks.indexOfFirst { it.id == selectedBlockId }
        if (idx < 0) return
        val b = blocks[idx]
        val nr = if (b.horizontal) b.row else b.row + delta
        val nc = if (b.horizontal) b.col + delta else b.col
        if (b.horizontal && (nc < 0 || nc + b.length > 6)) return
        if (!b.horizontal && (nr < 0 || nr + b.length > 6)) return

        val checkCells = if (b.horizontal) {
            (nc until nc + b.length).map { b.row to it }
        } else {
            (nr until nr + b.length).map { it to b.col }
        }
        val free = checkCells.all { (cr, cc) ->
            val owner = occupiedOwner(cr, cc)
            owner == null || owner.id == b.id
        }
        if (free) {
            blocks[idx] = b.copy(row = nr, col = nc)
            moves++
            if (b.isTarget && nc + b.length == 6) {
                onWin((400 - moves * 10).coerceAtLeast(120))
            }
        }
    }

    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Langkah: $moves • Bebaskan Balok Emas [KEY] ke kanan baris ke-3!",
                fontSize = 11.sp,
                fontWeight = FontWeight.ExtraBold,
                color = VaultObsidian,
                modifier = Modifier.weight(1f)
            )
            BrainActionBtn("RESET", DuoFoxOrange, DuoFoxOrangeLip) { initUnblock() }
        }

        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            for (r in 0 until 6) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    for (c in 0 until 6) {
                        val owner = occupiedOwner(r, c)
                        val isSel = owner?.id == selectedBlockId
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .aspectRatio(1f)
                                .duolingoButton(
                                    faceColor = when {
                                        owner == null -> DuoSnowCanvas
                                        owner.isTarget -> DuoBeeYellow
                                        owner.horizontal -> DuoMacawBlue
                                        else -> DuoBeetlePurple
                                    },
                                    lipColor = when {
                                        owner == null -> DuoHareLip
                                        owner.isTarget -> DuoBeeYellowLip
                                        owner.horizontal -> DuoMacawBlueLip
                                        else -> DuoBeetlePurpleLip
                                    },
                                    cornerRadius = 8.dp,
                                    lipHeight = 2.dp
                                )
                                .border(
                                    width = if (isSel) 2.5.dp else 0.dp,
                                    color = if (isSel) DuoCardinalRed else Color.Transparent,
                                    shape = RoundedCornerShape(8.dp)
                                )
                                .clickable {
                                    if (owner != null) selectedBlockId = owner.id
                                }
                                .padding(bottom = 2.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            if (owner != null) {
                                Text(
                                    text = if (owner.isTarget) "KEY" else owner.id.toString(),
                                    fontSize = if (owner.isTarget) 9.sp else 12.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = if (owner.isTarget) DuoEelInk else Color.White
                                )
                            } else if (r == 2 && c == 5) {
                                Text("EXIT", fontSize = 8.sp, fontWeight = FontWeight.ExtraBold, color = DuoFeatherGreenLip)
                            }
                        }
                    }
                }
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            BrainActionBtn("← / ↑ MUNDUR", DuoMacawBlue, DuoMacawBlueLip, Modifier.weight(1f)) { stepBlock(-1) }
            BrainActionBtn("MAJU → / ↓", DuoFeatherGreen, DuoFeatherGreenLip, Modifier.weight(1f)) { stepBlock(1) }
        }
    }
}

// =========================================================================
// 8. TOWER OF HANOI (3..6 CAKRAM 3D)
// =========================================================================
@Composable
fun TowerOfHanoiGameView(
    difficulty: BrainGameDifficulty,
    onWin: (Int) -> Unit
) {
    val diskCount = when (difficulty) {
        BrainGameDifficulty.MUDAH -> 3
        BrainGameDifficulty.SEDANG -> 4
        BrainGameDifficulty.SULIT -> 5
        BrainGameDifficulty.EKSTREM -> 6
    }
    val pegs = remember {
        List(3) { mutableStateListOf<Int>() }
    }
    var selectedPeg by remember { mutableIntStateOf(-1) }
    var moves by remember { mutableIntStateOf(0) }

    fun resetHanoi() {
        pegs.forEach { it.clear() }
        for (d in diskCount downTo 1) pegs[0].add(d)
        selectedPeg = -1
        moves = 0
    }

    LaunchedEffect(difficulty) { resetHanoi() }

    val diskColors = listOf(
        DuoFeatherGreen, DuoMacawBlue, DuoBeeYellow, DuoFoxOrange, DuoCardinalRed, DuoBeetlePurple
    )

    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Cakram: $diskCount • Langkah: $moves (Optimal: ${(1 shl diskCount) - 1})",
                fontWeight = FontWeight.ExtraBold,
                fontSize = 12.sp,
                color = VaultObsidian
            )
            BrainActionBtn("ULANG", DuoFoxOrange, DuoFoxOrangeLip) { resetHanoi() }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(170.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            for (p in 0..2) {
                val isSel = selectedPeg == p
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxSize()
                        .duolingoCard(
                            backgroundColor = if (isSel) DuoBeeYellowSoft else DuoSnowCanvas,
                            borderColor = if (isSel) DuoFoxOrange else DuoSwanBorder,
                            lipColor = DuoHareLip,
                            cornerRadius = 14.dp,
                            borderWidth = 2.dp,
                            lipHeight = 3.dp
                        )
                        .clickable {
                            if (selectedPeg == -1) {
                                if (pegs[p].isNotEmpty()) selectedPeg = p
                            } else if (selectedPeg == p) {
                                selectedPeg = -1
                            } else {
                                val topDisk = pegs[selectedPeg].lastOrNull()
                                val targetTop = pegs[p].lastOrNull()
                                if (topDisk != null && (targetTop == null || topDisk < targetTop)) {
                                    pegs[selectedPeg].removeAt(pegs[selectedPeg].lastIndex)
                                    pegs[p].add(topDisk)
                                    moves++
                                    selectedPeg = -1
                                    if (pegs[2].size == diskCount) {
                                        onWin(420)
                                    }
                                } else {
                                    selectedPeg = p
                                }
                            }
                        }
                        .padding(8.dp),
                    verticalArrangement = Arrangement.Bottom,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    pegs[p].reversed().forEach { dSize ->
                        val frac = (0.38f + (dSize.toFloat() / diskCount) * 0.58f).coerceIn(0.35f, 0.98f)
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(frac)
                                .height(20.dp)
                                .padding(vertical = 2.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(diskColors[(dSize - 1) % diskColors.size]),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = dSize.toString(),
                                fontSize = 10.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = Color.White
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "TIANG ${p + 1}",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = DuoEelInk
                    )
                }
            }
        }
    }
}

// =========================================================================
// 9. WATER SORT (LABIRIN WARNA TABUNG 3D)
// =========================================================================
@Composable
fun WaterSortColorGameView(
    difficulty: BrainGameDifficulty,
    onWin: (Int) -> Unit
) {
    val colorCount = when (difficulty) {
        BrainGameDifficulty.MUDAH -> 3
        BrainGameDifficulty.SEDANG -> 4
        else -> 5
    }
    val tubeCount = colorCount + 2
    val rng = remember { SecureRandom() }
    val tubes = remember { List(7) { mutableStateListOf<Int>() } }
    var selectedTube by remember { mutableIntStateOf(-1) }
    var moves by remember { mutableIntStateOf(0) }

    val liquidColors = listOf(
        DuoCardinalRed, DuoFeatherGreen, DuoMacawBlue, DuoFoxOrange, DuoBeetlePurple
    )

    fun resetWaterSort() {
        tubes.forEach { it.clear() }
        selectedTube = -1
        moves = 0
        val pool = mutableListOf<Int>()
        for (c in 0 until colorCount) {
            repeat(4) { pool.add(c) }
        }
        pool.shuffle(rng)
        for (t in 0 until colorCount) {
            for (i in 0 until 4) {
                tubes[t].add(pool[t * 4 + i])
            }
        }
    }

    LaunchedEffect(difficulty) { resetWaterSort() }

    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Langkah Tuang: $moves", fontWeight = FontWeight.ExtraBold, fontSize = 12.sp, color = VaultObsidian)
            BrainActionBtn("ACAK TABUNG", DuoFoxOrange, DuoFoxOrangeLip) { resetWaterSort() }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(160.dp),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            for (t in 0 until tubeCount) {
                val isSel = selectedTube == t
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .duolingoCard(
                            backgroundColor = if (isSel) DuoBeeYellowSoft else DuoSnowCanvas,
                            borderColor = if (isSel) DuoFoxOrange else DuoSwanBorder,
                            lipColor = DuoHareLip,
                            cornerRadius = 14.dp,
                            borderWidth = 2.dp,
                            lipHeight = 3.dp
                        )
                        .clickable {
                            if (selectedTube == -1) {
                                if (tubes[t].isNotEmpty()) selectedTube = t
                            } else if (selectedTube == t) {
                                selectedTube = -1
                            } else {
                                val src = tubes[selectedTube]
                                val dst = tubes[t]
                                if (src.isNotEmpty() && dst.size < 4 && (dst.isEmpty() || dst.last() == src.last())) {
                                    val colorToMove = src.last()
                                    while (src.isNotEmpty() && src.last() == colorToMove && dst.size < 4) {
                                        dst.add(src.removeAt(src.lastIndex))
                                    }
                                    moves++
                                    selectedTube = -1
                                    val allSorted = (0 until tubeCount).all { idx ->
                                        val tb = tubes[idx]
                                        tb.isEmpty() || (tb.size == 4 && tb.distinct().size == 1)
                                    }
                                    if (allSorted) onWin(380)
                                } else {
                                    selectedTube = t
                                }
                            }
                        }
                        .padding(6.dp),
                    verticalArrangement = Arrangement.Bottom,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    tubes[t].reversed().forEach { cIdx ->
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(26.dp)
                                .padding(vertical = 2.dp)
                                .clip(RoundedCornerShape(6.dp))
                                .background(liquidColors[cIdx % liquidColors.size])
                        )
                    }
                }
            }
        }
    }
}

// =========================================================================
// 10. PIPE CONNECT (SAMBUNG JARINGAN PIPA AIR/ENERGI)
// =========================================================================
@Composable
fun PipeConnectGameView(
    difficulty: BrainGameDifficulty,
    onWin: (Int) -> Unit
) {
    val size = when (difficulty) {
        BrainGameDifficulty.MUDAH -> 4
        BrainGameDifficulty.SEDANG -> 5
        else -> 6
    }
    val rng = remember { SecureRandom() }
    val rotations = remember { mutableStateListOf<Int>() } // 0..3; 0 is aligned!
    var taps by remember { mutableIntStateOf(0) }

    fun resetPipes() {
        rotations.clear()
        taps = 0
        repeat(size * size) {
            rotations.add(rng.nextInt(3) + 1)
        }
    }

    LaunchedEffect(difficulty) { resetPipes() }

    val symbols = listOf("╋", "┳", "┫", "┻")

    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Putar semua pipa menjadi [╋] hijau tersambung! Putaran: $taps",
                fontWeight = FontWeight.ExtraBold,
                fontSize = 11.sp,
                color = VaultObsidian,
                modifier = Modifier.weight(1f)
            )
            BrainActionBtn("RESET", DuoFoxOrange, DuoFoxOrangeLip) { resetPipes() }
        }

        if (rotations.size == size * size) {
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                for (r in 0 until size) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        for (c in 0 until size) {
                            val idx = r * size + c
                            val rot = rotations[idx]
                            val aligned = rot == 0
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .aspectRatio(1f)
                                    .duolingoButton(
                                        faceColor = if (aligned) DuoFeatherGreen else DuoMacawBlue,
                                        lipColor = if (aligned) DuoFeatherGreenLip else DuoMacawBlueLip,
                                        cornerRadius = 10.dp,
                                        lipHeight = 3.dp
                                    )
                                    .clickable {
                                        rotations[idx] = (rotations[idx] + 1) % 4
                                        taps++
                                        if (rotations.all { it == 0 }) {
                                            onWin(340)
                                        }
                                    }
                                    .padding(bottom = 3.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = symbols[rot],
                                    fontSize = 18.sp,
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

// =========================================================================
// 11. SLIDING 15-PUZZLE (GESER UBIN ANGKA)
// =========================================================================
@Composable
fun Sliding15PuzzleGameView(
    difficulty: BrainGameDifficulty,
    onWin: (Int) -> Unit
) {
    val size = when (difficulty) {
        BrainGameDifficulty.MUDAH -> 3
        BrainGameDifficulty.SEDANG -> 4
        else -> 5
    }
    val rng = remember { SecureRandom() }
    val tiles = remember { mutableStateListOf<Int>() }
    var moves by remember { mutableIntStateOf(0) }

    fun resetSliding() {
        tiles.clear()
        moves = 0
        for (i in 1 until size * size) tiles.add(i)
        tiles.add(0)
        // Perform valid random blank swaps so the board is 100% solvable
        var blankIdx = size * size - 1
        repeat(size * size * 12) {
            val br = blankIdx / size
            val bc = blankIdx % size
            val neighbors = buildList {
                if (br > 0) add((br - 1) * size + bc)
                if (br < size - 1) add((br + 1) * size + bc)
                if (bc > 0) add(br * size + (bc - 1))
                if (bc < size - 1) add(br * size + (bc + 1))
            }
            val pick = neighbors[rng.nextInt(neighbors.size)]
            tiles[blankIdx] = tiles[pick]
            tiles[pick] = 0
            blankIdx = pick
        }
    }

    LaunchedEffect(difficulty) { resetSliding() }

    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Langkah Geser: $moves", fontWeight = FontWeight.ExtraBold, fontSize = 12.sp, color = VaultObsidian)
            BrainActionBtn("ACAK ULANG", DuoFoxOrange, DuoFoxOrangeLip) { resetSliding() }
        }

        if (tiles.size == size * size) {
            Column(verticalArrangement = Arrangement.spacedBy(5.dp)) {
                for (r in 0 until size) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(5.dp)
                    ) {
                        for (c in 0 until size) {
                            val idx = r * size + c
                            val v = tiles[idx]
                            val correctPos = v == idx + 1
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .aspectRatio(1f)
                                    .then(
                                        if (v == 0) {
                                            Modifier.background(DuoSnowCanvas, RoundedCornerShape(10.dp))
                                        } else {
                                            Modifier.duolingoButton(
                                                faceColor = if (correctPos) DuoFeatherGreen else DuoFoxOrange,
                                                lipColor = if (correctPos) DuoFeatherGreenLip else DuoFoxOrangeLip,
                                                cornerRadius = 10.dp,
                                                lipHeight = 3.dp
                                            )
                                        }
                                    )
                                    .clickable {
                                        val blankIdx = tiles.indexOf(0)
                                        val br = blankIdx / size
                                        val bc = blankIdx % size
                                        if (abs(br - r) + abs(bc - c) == 1) {
                                            tiles[blankIdx] = v
                                            tiles[idx] = 0
                                            moves++
                                            val solved = (0 until size * size - 1).all { tiles[it] == it + 1 }
                                            if (solved) onWin(360)
                                        }
                                    }
                                    .padding(bottom = 3.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                if (v > 0) {
                                    Text(
                                        text = v.toString(),
                                        fontSize = 15.sp,
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

// =========================================================================
// 12. LIGHTS OUT (MATIKAN LAMPU MATRIKS)
// =========================================================================
@Composable
fun LightsOutMatrixGameView(
    difficulty: BrainGameDifficulty,
    onWin: (Int) -> Unit
) {
    val size = when (difficulty) {
        BrainGameDifficulty.MUDAH -> 4
        BrainGameDifficulty.SEDANG -> 5
        else -> 6
    }
    val rng = remember { SecureRandom() }
    val lights = remember { mutableStateListOf<Boolean>() }
    var moves by remember { mutableIntStateOf(0) }

    fun toggleCellAndNeighbors(r: Int, c: Int) {
        val coords = listOf(r to c, (r - 1) to c, (r + 1) to c, r to (c - 1), r to (c + 1))
        for ((nr, nc) in coords) {
            if (nr in 0 until size && nc in 0 until size) {
                val i = nr * size + nc
                lights[i] = !lights[i]
            }
        }
    }

    fun resetLights() {
        lights.clear()
        moves = 0
        repeat(size * size) { lights.add(false) }
        val scrambles = 5 + difficulty.id * 4
        repeat(scrambles) {
            toggleCellAndNeighbors(rng.nextInt(size), rng.nextInt(size))
        }
        if (lights.none { it }) toggleCellAndNeighbors(0, 0)
    }

    LaunchedEffect(difficulty) { resetLights() }

    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Padamkan seluruh lampu kuning! Langkah: $moves",
                fontWeight = FontWeight.ExtraBold,
                fontSize = 11.sp,
                color = VaultObsidian
            )
            BrainActionBtn("RESET", DuoCardinalRed, DuoCardinalRedLip) { resetLights() }
        }

        if (lights.size == size * size) {
            Column(verticalArrangement = Arrangement.spacedBy(5.dp)) {
                for (r in 0 until size) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(5.dp)
                    ) {
                        for (c in 0 until size) {
                            val lit = lights[r * size + c]
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .aspectRatio(1f)
                                    .duolingoButton(
                                        faceColor = if (lit) DuoBeeYellow else VaultObsidian,
                                        lipColor = if (lit) DuoBeeYellowLip else DuoEelInk,
                                        cornerRadius = 10.dp,
                                        lipHeight = 3.dp
                                    )
                                    .clickable {
                                        toggleCellAndNeighbors(r, c)
                                        moves++
                                        if (lights.none { it }) {
                                            onWin(350)
                                        }
                                    }
                                    .padding(bottom = 3.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = if (lit) "ON" else "OFF",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = if (lit) DuoEelInk else DuoMutedInk
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
