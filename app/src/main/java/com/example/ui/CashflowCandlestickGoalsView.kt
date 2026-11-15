package com.example.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
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
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.ShowChart
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
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
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.JetBrainsMonoFontFamily
import java.text.NumberFormat
import java.util.Locale
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

/**
 * Representasi 1 kolom Candlestick (Box-Box) + Bar Volume di bawahnya
 * untuk merangkum uang masuk (hijau), uang keluar (merah), saldo buka/tutup/tertinggi/terendah.
 */
data class CashflowCandlePoint(
    val id: String,
    val periodLabel: String,
    val openBalanceIdr: Long,
    val closeBalanceIdr: Long,
    val highBalanceIdr: Long,
    val lowBalanceIdr: Long,
    val moneyInIdr: Long,
    val moneyOutIdr: Long,
    val note: String
) {
    val isBullish: Boolean
        get() = closeBalanceIdr >= openBalanceIdr

    val totalVolumeIdr: Long
        get() = (moneyInIdr + moneyOutIdr).coerceAtLeast(abs(closeBalanceIdr - openBalanceIdr))

    val netChangeIdr: Long
        get() = closeBalanceIdr - openBalanceIdr

    val changePercent: Double
        get() {
            val base = openBalanceIdr.coerceAtLeast(1L).toDouble()
            return ((closeBalanceIdr - openBalanceIdr).toDouble() / base) * 100.0
        }
}

/**
 * Model data untuk Barang yang Mau Dibeli dari Tabungan (Savings Buy-Goal Item).
 * Setiap barang memiliki riwayat grafik Box-Box Candlestick & Volume tersendiri.
 */
data class SavingsBuyGoalItem(
    val id: String,
    val itemName: String,
    val itemCategory: String,
    val itemSpecsNote: String,
    val targetPriceIdr: Long,
    val savedAmountIdr: Long,
    val monthlyPlanIdr: Long,
    val accentHex: Long,
    val isPurchased: Boolean = false,
    val candleHistory: List<CashflowCandlePoint> = emptyList()
) {
    val progressRatio: Float
        get() = if (targetPriceIdr <= 0L) 1f else (savedAmountIdr.toFloat() / targetPriceIdr.toFloat()).coerceIn(0f, 1f)

    val progressPercent: Int
        get() = (progressRatio * 100f).toInt()

    val remainingAmountIdr: Long
        get() = (targetPriceIdr - savedAmountIdr).coerceAtLeast(0L)

    val estimatedMonthsLeft: Int
        get() {
            if (remainingAmountIdr <= 0L) return 0
            val monthly = monthlyPlanIdr.coerceAtLeast(100_000L)
            return ((remainingAmountIdr + monthly - 1L) / monthly).toInt()
        }
}

enum class CashflowChartTimeframe(val id: String, val label: String, val multiplier: Int) {
    DAILY("1D", "1H", 1),
    WEEKLY("1W", "1M", 2),
    MONTHLY("1M", "1B", 3)
}

/**
 * State repository untuk Tempat Duit Tabungan & Daftar Barang Impian (Goals)
 * di dalam BlackBox Sanctuary (dan terhubung dengan Brankas).
 */
object SanctuarySavingsAndGoalsRepository {

    val mainVaultCandles = mutableStateListOf(
        CashflowCandlePoint(
            id = "c1",
            periodLabel = "P-01",
            openBalanceIdr = 45_000_000L,
            closeBalanceIdr = 58_000_000L,
            highBalanceIdr = 60_500_000L,
            lowBalanceIdr = 43_000_000L,
            moneyInIdr = 16_000_000L,
            moneyOutIdr = 3_000_000L,
            note = "Setoran gaji & dividen awal ke Tempat Duit Tabungan"
        ),
        CashflowCandlePoint(
            id = "c2",
            periodLabel = "P-02",
            openBalanceIdr = 58_000_000L,
            closeBalanceIdr = 52_500_000L,
            highBalanceIdr = 59_000_000L,
            lowBalanceIdr = 50_000_000L,
            moneyInIdr = 2_500_000L,
            moneyOutIdr = 8_000_000L,
            note = "Penarikan DP perangkat & kebutuhan operasional"
        ),
        CashflowCandlePoint(
            id = "c3",
            periodLabel = "P-03",
            openBalanceIdr = 52_500_000L,
            closeBalanceIdr = 69_000_000L,
            highBalanceIdr = 71_000_000L,
            lowBalanceIdr = 52_000_000L,
            moneyInIdr = 19_000_000L,
            moneyOutIdr = 2_500_000L,
            note = "Bonus proyek siber masuk ke tabungan utama"
        ),
        CashflowCandlePoint(
            id = "c4",
            periodLabel = "P-04",
            openBalanceIdr = 69_000_000L,
            closeBalanceIdr = 84_000_000L,
            highBalanceIdr = 86_500_000L,
            lowBalanceIdr = 68_000_000L,
            moneyInIdr = 18_500_000L,
            moneyOutIdr = 3_500_000L,
            note = "Alokasi rutin ke kantong barang impian"
        ),
        CashflowCandlePoint(
            id = "c5",
            periodLabel = "P-05",
            openBalanceIdr = 84_000_000L,
            closeBalanceIdr = 76_000_000L,
            highBalanceIdr = 85_000_000L,
            lowBalanceIdr = 73_500_000L,
            moneyInIdr = 4_000_000L,
            moneyOutIdr = 12_000_000L,
            note = "Checkout pembelian aksesoris workstation"
        ),
        CashflowCandlePoint(
            id = "c6",
            periodLabel = "P-06",
            openBalanceIdr = 76_000_000L,
            closeBalanceIdr = 95_000_000L,
            highBalanceIdr = 97_000_000L,
            lowBalanceIdr = 75_500_000L,
            moneyInIdr = 22_000_000L,
            moneyOutIdr = 3_000_000L,
            note = "Setoran tabungan bulanan + cashback"
        ),
        CashflowCandlePoint(
            id = "c7",
            periodLabel = "P-07",
            openBalanceIdr = 95_000_000L,
            closeBalanceIdr = 112_000_000L,
            highBalanceIdr = 115_000_000L,
            lowBalanceIdr = 94_000_000L,
            moneyInIdr = 20_500_000L,
            moneyOutIdr = 3_500_000L,
            note = "Surplus hasil investasi cair ke tabungan"
        ),
        CashflowCandlePoint(
            id = "c8",
            periodLabel = "P-08",
            openBalanceIdr = 112_000_000L,
            closeBalanceIdr = 104_500_000L,
            highBalanceIdr = 113_000_000L,
            lowBalanceIdr = 101_000_000L,
            moneyInIdr = 3_000_000L,
            moneyOutIdr = 10_500_000L,
            note = "Pelunasan sebagian perangkat fotografi"
        ),
        CashflowCandlePoint(
            id = "c9",
            periodLabel = "P-09",
            openBalanceIdr = 104_500_000L,
            closeBalanceIdr = 128_000_000L,
            highBalanceIdr = 130_500_000L,
            lowBalanceIdr = 104_000_000L,
            moneyInIdr = 26_000_000L,
            moneyOutIdr = 2_500_000L,
            note = "Setoran besar target barang kuartal ini"
        ),
        CashflowCandlePoint(
            id = "c10",
            periodLabel = "P-10",
            openBalanceIdr = 128_000_000L,
            closeBalanceIdr = 148_500_000L,
            highBalanceIdr = 151_000_000L,
            lowBalanceIdr = 127_500_000L,
            moneyInIdr = 24_000_000L,
            moneyOutIdr = 3_500_000L,
            note = "Rekor saldo tertinggi Tempat Duit Tabungan"
        ),
        CashflowCandlePoint(
            id = "c11",
            periodLabel = "P-11",
            openBalanceIdr = 148_500_000L,
            closeBalanceIdr = 141_000_000L,
            highBalanceIdr = 149_500_000L,
            lowBalanceIdr = 138_000_000L,
            moneyInIdr = 2_500_000L,
            moneyOutIdr = 10_000_000L,
            note = "Penarikan dana darurat & perawatan gear"
        ),
        CashflowCandlePoint(
            id = "c12",
            periodLabel = "P-12",
            openBalanceIdr = 141_000_000L,
            closeBalanceIdr = 163_500_000L,
            highBalanceIdr = 166_000_000L,
            lowBalanceIdr = 140_000_000L,
            moneyInIdr = 25_500_000L,
            moneyOutIdr = 3_000_000L,
            note = "Setoran terbaru siap beli barang impian"
        )
    )

    val buyGoals = mutableStateListOf(
        SavingsBuyGoalItem(
            id = "buy_macbook_m4",
            itemName = "MacBook Pro 16\" M4 Max (64GB / 2TB)",
            itemCategory = "APPLE WORKSTATION",
            itemSpecsNote = "Space Black • Apple M4 Max 16-Core • Liquid Retina XDR",
            targetPriceIdr = 58_000_000L,
            savedAmountIdr = 45_500_000L,
            monthlyPlanIdr = 6_500_000L,
            accentHex = 0xFF0A84FF,
            candleHistory = listOf(
                CashflowCandlePoint("g1_1", "C-1", 5_000_000L, 14_000_000L, 15_000_000L, 5_000_000L, 9_000_000L, 0L, "Tabungan awal MacBook"),
                CashflowCandlePoint("g1_2", "C-2", 14_000_000L, 23_500_000L, 24_000_000L, 13_500_000L, 9_500_000L, 0L, "Setoran bulan ke-2"),
                CashflowCandlePoint("g1_3", "C-3", 23_500_000L, 20_500_000L, 24_000_000L, 19_500_000L, 1_000_000L, 4_000_000L, "Pinjam sebentar untuk perpanjangan lisensi"),
                CashflowCandlePoint("g1_4", "C-4", 20_500_000L, 33_000_000L, 34_500_000L, 20_500_000L, 12_500_000L, 0L, "Top-up bonus proyek"),
                CashflowCandlePoint("g1_5", "C-5", 33_000_000L, 45_500_000L, 46_500_000L, 33_000_000L, 12_500_000L, 0L, "Setoran ke-5 (78% tercapai)")
            )
        ),
        SavingsBuyGoalItem(
            id = "buy_rtx5090_rig",
            itemName = "Custom PC RTX 5090 32GB + OLED 4K 240Hz",
            itemCategory = "SETUP GAMING & AI",
            itemSpecsNote = "Ryzen 9 9950X3D • 128GB DDR5 • Custom Watercooling",
            targetPriceIdr = 75_000_000L,
            savedAmountIdr = 61_000_000L,
            monthlyPlanIdr = 7_000_000L,
            accentHex = 0xFF30D158,
            candleHistory = listOf(
                CashflowCandlePoint("g2_1", "C-1", 10_000_000L, 24_000_000L, 25_000_000L, 10_000_000L, 14_000_000L, 0L, "Tabungan komponen GPU"),
                CashflowCandlePoint("g2_2", "C-2", 24_000_000L, 39_000_000L, 40_000_000L, 24_000_000L, 15_000_000L, 0L, "Tabungan prosesor & motherboard"),
                CashflowCandlePoint("g2_3", "C-3", 39_000_000L, 35_000_000L, 39_500_000L, 34_000_000L, 1_000_000L, 5_000_000L, "Beli meja hidrolik terlebih dahulu"),
                CashflowCandlePoint("g2_4", "C-4", 35_000_000L, 49_000_000L, 50_000_000L, 35_000_000L, 14_000_000L, 0L, "Setoran tabungan monitor OLED"),
                CashflowCandlePoint("g2_5", "C-5", 49_000_000L, 61_000_000L, 62_000_000L, 49_000_000L, 12_000_000L, 0L, "Tinggal Rp 14 Juta lagi siap rakit!")
            )
        ),
        SavingsBuyGoalItem(
            id = "buy_sony_a7cr",
            itemName = "Kamera Sony A7CR + Lensa GM 35mm f/1.4",
            itemCategory = "FOTOGRAFI & LENSA",
            itemSpecsNote = "Full-Frame 61MP Compact • G-Master Prime Lens",
            targetPriceIdr = 64_000_000L,
            savedAmountIdr = 35_000_000L,
            monthlyPlanIdr = 5_000_000L,
            accentHex = 0xFFFF9F0A,
            candleHistory = listOf(
                CashflowCandlePoint("g3_1", "C-1", 4_000_000L, 12_000_000L, 12_500_000L, 4_000_000L, 8_000_000L, 0L, "Setoran awal kamera"),
                CashflowCandlePoint("g3_2", "C-2", 12_000_000L, 21_000_000L, 22_000_000L, 12_000_000L, 9_000_000L, 0L, "Tabungan body A7CR"),
                CashflowCandlePoint("g3_3", "C-3", 21_000_000L, 28_000_000L, 29_000_000L, 21_000_000L, 7_000_000L, 0L, "Setoran lensa GM"),
                CashflowCandlePoint("g3_4", "C-4", 28_000_000L, 25_000_000L, 28_500_000L, 24_500_000L, 0L, 3_000_000L, "Beli dry cabinet & tripod"),
                CashflowCandlePoint("g3_5", "C-5", 25_000_000L, 35_000_000L, 36_000_000L, 25_000_000L, 10_000_000L, 0L, "Setoran bulan berjalan")
            )
        ),
        SavingsBuyGoalItem(
            id = "buy_seiko_diver",
            itemName = "Seiko Prospex Marinemaster Automatic Limited",
            itemCategory = "JAM TANGAN & KOLEKSI",
            itemSpecsNote = "Caliber 6L37 • Sapphire Crystal • Titanium Case",
            targetPriceIdr = 22_000_000L,
            savedAmountIdr = 22_000_000L,
            monthlyPlanIdr = 4_000_000L,
            accentHex = 0xFFBF5AF2,
            candleHistory = listOf(
                CashflowCandlePoint("g4_1", "C-1", 5_000_000L, 11_000_000L, 11_500_000L, 5_000_000L, 6_000_000L, 0L, "Setoran ke-1"),
                CashflowCandlePoint("g4_2", "C-2", 11_000_000L, 17_000_000L, 17_500_000L, 11_000_000L, 6_000_000L, 0L, "Setoran ke-2"),
                CashflowCandlePoint("g4_3", "C-3", 17_000_000L, 22_000_000L, 22_500_000L, 17_000_000L, 5_000_000L, 0L, "Target 100% tercapai — Siap Beli!")
            )
        )
    )

    val currentVaultSavingsBalanceIdr: Long
        get() = mainVaultCandles.lastOrNull()?.closeBalanceIdr ?: 0L

    val totalMoneyInIdr: Long
        get() = mainVaultCandles.sumOf { it.moneyInIdr }

    val totalMoneyOutIdr: Long
        get() = mainVaultCandles.sumOf { it.moneyOutIdr }

    val totalAllocatedToBuyGoalsIdr: Long
        get() = buyGoals.sumOf { it.savedAmountIdr }

    val totalTargetBuyGoalsIdr: Long
        get() = buyGoals.sumOf { it.targetPriceIdr }

    fun recordVaultCashflowTransaction(
        isMoneyIn: Boolean,
        amountIdr: Long,
        note: String,
        targetGoalId: String? = null
    ) {
        val cleanAmount = amountIdr.coerceAtLeast(10_000L)
        val prevClose = currentVaultSavingsBalanceIdr
        val newClose = if (isMoneyIn) {
            prevClose + cleanAmount
        } else {
            (prevClose - cleanAmount).coerceAtLeast(0L)
        }
        val high = max(prevClose, newClose) + (cleanAmount / 8L)
        val low = (min(prevClose, newClose) - (cleanAmount / 10L)).coerceAtLeast(0L)
        val nextNum = mainVaultCandles.size + 1

        mainVaultCandles.add(
            CashflowCandlePoint(
                id = "c_${System.currentTimeMillis()}",
                periodLabel = "P-${nextNum.toString().padStart(2, '0')}",
                openBalanceIdr = prevClose,
                closeBalanceIdr = newClose,
                highBalanceIdr = high,
                lowBalanceIdr = low,
                moneyInIdr = if (isMoneyIn) cleanAmount else 0L,
                moneyOutIdr = if (!isMoneyIn) cleanAmount else 0L,
                note = note.ifBlank { if (isMoneyIn) "Setoran Tabungan Masuk" else "Penarikan / Pengeluaran Tabungan" }
            )
        )

        if (targetGoalId != null) {
            val idx = buyGoals.indexOfFirst { it.id == targetGoalId }
            if (idx >= 0) {
                mutateGoalSavings(
                    goalId = targetGoalId,
                    isDeposit = isMoneyIn,
                    amountIdr = cleanAmount,
                    note = note,
                    alsoRecordInMainVault = false
                )
            }
        }
    }

    fun mutateGoalSavings(
        goalId: String,
        isDeposit: Boolean,
        amountIdr: Long,
        note: String,
        alsoRecordInMainVault: Boolean = true
    ) {
        val idx = buyGoals.indexOfFirst { it.id == goalId }
        if (idx < 0) return
        val goal = buyGoals[idx]
        val cleanAmount = amountIdr.coerceAtLeast(10_000L)
        val prevSaved = goal.savedAmountIdr
        val nextSaved = if (isDeposit) {
            prevSaved + cleanAmount
        } else {
            (prevSaved - cleanAmount).coerceAtLeast(0L)
        }
        val high = max(prevSaved, nextSaved) + (cleanAmount / 10L)
        val low = (min(prevSaved, nextSaved) - (cleanAmount / 12L)).coerceAtLeast(0L)
        val stepNum = goal.candleHistory.size + 1
        val newCandle = CashflowCandlePoint(
            id = "${goal.id}_c_$stepNum",
            periodLabel = "C-$stepNum",
            openBalanceIdr = prevSaved,
            closeBalanceIdr = nextSaved,
            highBalanceIdr = high,
            lowBalanceIdr = low,
            moneyInIdr = if (isDeposit) cleanAmount else 0L,
            moneyOutIdr = if (!isDeposit) cleanAmount else 0L,
            note = note.ifBlank {
                if (isDeposit) "Setor tabungan untuk ${goal.itemName}"
                else "Kurangi alokasi ${goal.itemName}"
            }
        )
        buyGoals[idx] = goal.copy(
            savedAmountIdr = nextSaved,
            candleHistory = goal.candleHistory + newCandle
        )

        if (alsoRecordInMainVault) {
            recordVaultCashflowTransaction(
                isMoneyIn = isDeposit,
                amountIdr = cleanAmount,
                note = "${if (isDeposit) "[+Setor Barang]" else "[-Tarik Barang]"} ${goal.itemName}: $note",
                targetGoalId = null
            )
        }
    }

    fun addNewBuyGoal(
        itemName: String,
        itemCategory: String,
        itemSpecsNote: String,
        targetPriceIdr: Long,
        initialSavedIdr: Long,
        monthlyPlanIdr: Long
    ) {
        val cleanTarget = targetPriceIdr.coerceAtLeast(100_000L)
        val cleanInitial = initialSavedIdr.coerceIn(0L, cleanTarget)
        val cleanMonthly = monthlyPlanIdr.coerceAtLeast(50_000L)
        val palettePool = listOf(0xFF0A84FFL, 0xFF30D158L, 0xFFFF9F0AL, 0xFFBF5AF2L, 0xFFFF375FL, 0xFF5E5CE6L)
        val initialCandles = if (cleanInitial > 0L) {
            val mid = cleanInitial / 2L
            listOf(
                CashflowCandlePoint(
                    id = "init_1",
                    periodLabel = "C-1",
                    openBalanceIdr = 0L,
                    closeBalanceIdr = mid,
                    highBalanceIdr = mid + (mid / 10L),
                    lowBalanceIdr = 0L,
                    moneyInIdr = mid,
                    moneyOutIdr = 0L,
                    note = "Setoran pembuka $itemName"
                ),
                CashflowCandlePoint(
                    id = "init_2",
                    periodLabel = "C-2",
                    openBalanceIdr = mid,
                    closeBalanceIdr = cleanInitial,
                    highBalanceIdr = cleanInitial + (cleanInitial / 12L),
                    lowBalanceIdr = mid,
                    moneyInIdr = cleanInitial - mid,
                    moneyOutIdr = 0L,
                    note = "Alokasi awal $itemName"
                )
            )
        } else {
            listOf(
                CashflowCandlePoint(
                    id = "init_0",
                    periodLabel = "C-1",
                    openBalanceIdr = 0L,
                    closeBalanceIdr = 0L,
                    highBalanceIdr = 0L,
                    lowBalanceIdr = 0L,
                    moneyInIdr = 0L,
                    moneyOutIdr = 0L,
                    note = "Target barang dibuat, siap diisi tabungan"
                )
            )
        }

        buyGoals.add(
            0,
            SavingsBuyGoalItem(
                id = "buy_${System.currentTimeMillis()}",
                itemName = itemName.trim(),
                itemCategory = itemCategory.trim().ifBlank { "BARANG IMPIAN" },
                itemSpecsNote = itemSpecsNote.trim().ifBlank { "Target pembelian dari Tempat Duit Tabungan" },
                targetPriceIdr = cleanTarget,
                savedAmountIdr = cleanInitial,
                monthlyPlanIdr = cleanMonthly,
                accentHex = palettePool[buyGoals.size % palettePool.size],
                candleHistory = initialCandles
            )
        )
    }

    fun checkoutAndPurchaseGoal(goalId: String): Boolean {
        val idx = buyGoals.indexOfFirst { it.id == goalId }
        if (idx < 0) return false
        val goal = buyGoals[idx]
        buyGoals[idx] = goal.copy(
            isPurchased = !goal.isPurchased
        )
        if (!goal.isPurchased) {
            recordVaultCashflowTransaction(
                isMoneyIn = false,
                amountIdr = goal.targetPriceIdr,
                note = "CHECKOUT BELI BARANG: ${goal.itemName}",
                targetGoalId = null
            )
        }
        return true
    }

    fun deleteBuyGoal(goalId: String) {
        buyGoals.removeAll { it.id == goalId }
    }
}

private fun formatIdrCurrency(amount: Long): String {
    val nf = NumberFormat.getNumberInstance(Locale("id", "ID"))
    return "Rp ${nf.format(amount)}"
}

private fun formatIdrShort(amount: Long): String {
    val absVal = abs(amount)
    val sign = if (amount < 0L) "-" else ""
    return when {
        absVal >= 1_000_000_000L -> String.format(Locale.US, "%sRp %.2fM", sign, absVal / 1_000_000_000.0)
        absVal >= 1_000_000L -> String.format(Locale.US, "%sRp %.1fJt", sign, absVal / 1_000_000.0)
        absVal >= 1_000L -> String.format(Locale.US, "%sRp %dRb", sign, absVal / 1_000L)
        else -> "${sign}Rp $absVal"
    }
}

enum class SanctuaryLedgerSubViewMode {
    ALL,
    SAVINGS_VAULT_ONLY,
    BUY_GOALS_ONLY
}

/**
 * Reusable iOS 18.2 / iOS 19 Cupertino Inset Grouped Form Row
 * Replaces Android's boxy OutlinedTextField with a pure Apple Settings/Wallet table row.
 */
@Composable
fun IosCupertinoFieldRow(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    isNumeric: Boolean = false,
    accentColor: Color = Color(0xFF0A84FF),
    testTag: String? = null
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 14.dp, vertical = 11.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium,
            color = Color(0xFFF2F2F7),
            modifier = Modifier.weight(0.42f)
        )
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            singleLine = true,
            keyboardOptions = if (isNumeric) KeyboardOptions(keyboardType = KeyboardType.Number) else KeyboardOptions.Default,
            textStyle = TextStyle(
                color = accentColor,
                fontSize = 13.5.sp,
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.End
            ),
            cursorBrush = SolidColor(accentColor),
            modifier = Modifier
                .weight(0.58f)
                .then(if (testTag != null) Modifier.testTag(testTag) else Modifier),
            decorationBox = { innerTextField ->
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.CenterEnd
                ) {
                    if (value.isEmpty()) {
                        Text(
                            text = placeholder,
                            fontSize = 13.sp,
                            color = Color(0xFF636366),
                            textAlign = TextAlign.End
                        )
                    }
                    innerTextField()
                }
            }
        )
    }
}

/**
 * Circular Progress Ring inspired by Apple Watch Activity / Apple Savings Goals on iOS 18.
 */
@Composable
private fun IosCircularGoalRing(
    progress: Float,
    accentColor: Color,
    percentText: String,
    modifier: Modifier = Modifier
) {
    val animatedProgress = rememberGsapRollingFloat(progress.coerceIn(0f, 1f))
    Box(
        modifier = modifier.size(54.dp),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val strokeW = 5.5.dp.toPx()
            drawArc(
                color = accentColor.copy(alpha = 0.16f),
                startAngle = 0f,
                sweepAngle = 360f,
                useCenter = false,
                style = Stroke(width = strokeW, cap = StrokeCap.Round)
            )
            drawArc(
                brush = Brush.sweepGradient(
                    colors = listOf(accentColor.copy(alpha = 0.75f), accentColor, accentColor)
                ),
                startAngle = -90f,
                sweepAngle = (animatedProgress.coerceIn(0f, 1f) * 360f),
                useCenter = false,
                style = Stroke(width = strokeW, cap = StrokeCap.Round)
            )
        }
        Text(
            text = percentText,
            fontFamily = JetBrainsMonoFontFamily,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFFF2F2F7)
        )
    }
}

/**
 * Komponen Utama: Tempat Duit Tabungan + Daftar Goals Barang yang Mau Dibeli
 * dengan estetika 100% iOS 18.2 / iOS 19 Liquid Glass (Apple Stocks & Apple Savings).
 */
@Composable
fun SanctuarySavingsAndBuyGoalsSection(
    onPostBanner: (String) -> Unit,
    modifier: Modifier = Modifier,
    displayMode: SanctuaryLedgerSubViewMode = SanctuaryLedgerSubViewMode.ALL
) {
    var selectedTimeframe by remember { mutableStateOf(CashflowChartTimeframe.DAILY) }
    var showFibonacciLines by remember { mutableStateOf(true) }
    var showTrendlineChannel by remember { mutableStateOf(true) }
    var showMovingAverage by remember { mutableStateOf(true) }

    // Form Uang Masuk / Uang Keluar di Tempat Duit Tabungan
    var transactionNominalText by remember { mutableStateOf("5000000") }
    var transactionNoteText by remember { mutableStateOf("") }
    var selectedTargetGoalIdForTx by remember { mutableStateOf<String?>(null) }

    // Form Tambah Barang Impian Baru (Goals Barang yang Mau Dibeli)
    var showAddGoalModal by remember { mutableStateOf(false) }
    var newItemName by remember { mutableStateOf("") }
    var newItemCategory by remember { mutableStateOf("APPLE & GADGET") }
    var newItemSpecs by remember { mutableStateOf("") }
    var newItemPriceText by remember { mutableStateOf("25000000") }
    var newItemInitialSavedText by remember { mutableStateOf("5000000") }
    var newItemMonthlyPlanText by remember { mutableStateOf("2500000") }

    // Expanded per-goal chart & quick top-up state
    var expandedGoalChartIds by remember {
        mutableStateOf(setOf(SanctuarySavingsAndGoalsRepository.buyGoals.firstOrNull()?.id ?: ""))
    }

    val candles = SanctuarySavingsAndGoalsRepository.mainVaultCandles
    val buyGoals = SanctuarySavingsAndGoalsRepository.buyGoals
    val currentBalance = rememberGsapRollingLong(SanctuarySavingsAndGoalsRepository.currentVaultSavingsBalanceIdr)
    val totalIn = rememberGsapRollingLong(SanctuarySavingsAndGoalsRepository.totalMoneyInIdr)
    val totalOut = rememberGsapRollingLong(SanctuarySavingsAndGoalsRepository.totalMoneyOutIdr)
    val totalGoalSaved = rememberGsapRollingLong(SanctuarySavingsAndGoalsRepository.totalAllocatedToBuyGoalsIdr)
    val totalGoalTarget = SanctuarySavingsAndGoalsRepository.totalTargetBuyGoalsIdr

    val iosGlassBorderBrush = Brush.verticalGradient(
        colors = listOf(
            Color.White.copy(alpha = 0.20f),
            Color.White.copy(alpha = 0.05f)
        )
    )

    Column(
        modifier = modifier
            .fillMaxWidth()
            .testTag("sanctuary_savings_and_buy_goals_section"),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // ====================================================================
        // BAGIAN 1: APPLE STOCKS & APPLE SAVINGS ACCOUNT (TEMPAT DUIT TABUNGAN)
        // ====================================================================
        if (displayMode == SanctuaryLedgerSubViewMode.ALL ||
            displayMode == SanctuaryLedgerSubViewMode.SAVINGS_VAULT_ONLY
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .gsapStaggerReveal(index = 0, triggerKey = displayMode)
                    .clip(RoundedCornerShape(26.dp))
                    .background(Color(0xFF1C1C1E))
                    .border(0.7.dp, iosGlassBorderBrush, RoundedCornerShape(26.dp))
                    .padding(18.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // Header ala Apple Stocks iOS 18.2
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(42.dp)
                                .clip(RoundedCornerShape(13.dp))
                                .background(
                                    Brush.linearGradient(
                                        colors = listOf(Color(0xFF30D158), Color(0xFF0A84FF))
                                    )
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.AccountBalanceWallet,
                                contentDescription = "Apple Savings Vault",
                                tint = Color.White,
                                modifier = Modifier.size(22.dp)
                            )
                        }
                        Column(modifier = Modifier.weight(1f)) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Text(
                                    text = "VAULT•IDR",
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = Color(0xFFF2F2F7)
                                )
                                Text(
                                    text = "Apple Savings & Stocks",
                                    fontSize = 11.sp,
                                    color = Color(0xFF8E8E93)
                                )
                            }
                            Text(
                                text = formatIdrCurrency(currentBalance),
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFFF2F2F7)
                            )
                        }
                    }

                    val lastCandle = candles.lastOrNull()
                    val lastPct = lastCandle?.changePercent ?: 0.0
                    val isPositive = lastPct >= 0.0
                    Column(horizontalAlignment = Alignment.End) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(999.dp))
                                .background(
                                    if (isPositive) Color(0xFF30D158).copy(alpha = 0.20f)
                                    else Color(0xFFFF453A).copy(alpha = 0.20f)
                                )
                                .padding(horizontal = 10.dp, vertical = 5.dp)
                        ) {
                            Text(
                                text = String.format(
                                    Locale.US,
                                    "%s%.2f%%",
                                    if (isPositive) "+" else "",
                                    lastPct
                                ),
                                fontFamily = JetBrainsMonoFontFamily,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isPositive) Color(0xFF30D158) else Color(0xFFFF453A)
                            )
                        }
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "APY 4.55% • ${candles.size} Sesi",
                            fontSize = 10.sp,
                            color = Color(0xFF8E8E93)
                        )
                    }
                }

                // 3 Widget Ringkasan ala iOS 18 Health/Wallet Summary Pods
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    SummaryMetricBentoPill(
                        label = "Total Masuk",
                        valueText = formatIdrShort(totalIn),
                        accentColor = Color(0xFF30D158),
                        modifier = Modifier.weight(1f)
                    )
                    SummaryMetricBentoPill(
                        label = "Total Keluar",
                        valueText = formatIdrShort(totalOut),
                        accentColor = Color(0xFFFF453A),
                        modifier = Modifier.weight(1f)
                    )
                    SummaryMetricBentoPill(
                        label = "Goals Barang",
                        valueText = formatIdrShort(totalGoalSaved),
                        accentColor = Color(0xFF0A84FF),
                        modifier = Modifier.weight(1f)
                    )
                }

                // iOS Stocks Segmented Timeframe & Technical Overlay Pills
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Timeframe capsule group
                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(10.dp))
                            .background(Color(0xFF2C2C2E))
                            .padding(2.dp),
                        horizontalArrangement = Arrangement.spacedBy(2.dp)
                    ) {
                        CashflowChartTimeframe.entries.forEach { tf ->
                            val isSel = selectedTimeframe == tf
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(if (isSel) Color(0xFF48484A) else Color.Transparent)
                                    .clickable { selectedTimeframe = tf }
                                    .padding(horizontal = 10.dp, vertical = 5.dp)
                            ) {
                                Text(
                                    text = tf.label,
                                    fontSize = 11.sp,
                                    fontWeight = if (isSel) FontWeight.Bold else FontWeight.Medium,
                                    color = if (isSel) Color.White else Color(0xFF8E8E93)
                                )
                            }
                        }
                    }

                    ChartIndicatorToggleChip(
                        label = "Fib",
                        checked = showFibonacciLines,
                        accentColor = Color(0xFF5E5CE6),
                        onToggle = { showFibonacciLines = !showFibonacciLines }
                    )
                    ChartIndicatorToggleChip(
                        label = "Channel",
                        checked = showTrendlineChannel,
                        accentColor = Color(0xFF64D2FF),
                        onToggle = { showTrendlineChannel = !showTrendlineChannel }
                    )
                    ChartIndicatorToggleChip(
                        label = "SMA-3",
                        checked = showMovingAverage,
                        accentColor = Color(0xFFFF9F0A),
                        onToggle = { showMovingAverage = !showMovingAverage }
                    )
                }

                // GRAFIK BOX-BOX CANDLESTICK & BAR VOLUME UNTUK TEMPAT DUIT TABUNGAN
                CashflowCandlestickAndVolumeCanvas(
                    titleTicker = "VAULT•IDR",
                    candles = candles,
                    targetCeilingIdr = max(totalGoalTarget, currentBalance + 25_000_000L),
                    showFibonacci = showFibonacciLines,
                    showTrendChannel = showTrendlineChannel,
                    showMovingAverage = showMovingAverage,
                    chartHeightDp = 250,
                    testTagName = "main_savings_candlestick_chart"
                )

                // APPLE WALLET INSET GROUPED TRANSACTION SHEET
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "MUTASI CEPAT APPLE SAVINGS",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF8E8E93),
                        modifier = Modifier.padding(start = 4.dp)
                    )

                    // iOS Inset Grouped Table Container
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                            .background(Color(0xFF2C2C2E))
                    ) {
                        IosCupertinoFieldRow(
                            label = "Nominal (Rp)",
                            value = transactionNominalText,
                            onValueChange = { transactionNominalText = it.filter { ch -> ch.isDigit() } },
                            placeholder = "5000000",
                            isNumeric = true,
                            accentColor = Color(0xFF30D158)
                        )
                        HorizontalDivider(
                            thickness = 0.5.dp,
                            color = Color(0xFF38383A),
                            modifier = Modifier.padding(start = 14.dp)
                        )
                        IosCupertinoFieldRow(
                            label = "Catatan Mutasi",
                            value = transactionNoteText,
                            onValueChange = { transactionNoteText = it },
                            placeholder = "Setoran gaji / penarikan...",
                            isNumeric = false,
                            accentColor = Color(0xFFF2F2F7)
                        )
                    }

                    // Pilihan Alokasi Opsional ke Barang Impian (iOS Capsule Selector)
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Kantong:",
                            fontSize = 11.sp,
                            color = Color(0xFF8E8E93)
                        )
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(999.dp))
                                .background(
                                    if (selectedTargetGoalIdForTx == null) Color(0xFF0A84FF)
                                    else Color(0xFF2C2C2E)
                                )
                                .clickable { selectedTargetGoalIdForTx = null }
                                .padding(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = "Kas Utama",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color.White
                            )
                        }
                        buyGoals.forEach { goal ->
                            val isPicked = selectedTargetGoalIdForTx == goal.id
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(999.dp))
                                    .background(
                                        if (isPicked) Color(0xFF0A84FF)
                                        else Color(0xFF2C2C2E)
                                    )
                                    .clickable { selectedTargetGoalIdForTx = goal.id }
                                    .padding(horizontal = 12.dp, vertical = 6.dp)
                            ) {
                                Text(
                                    text = goal.itemName.take(18),
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = Color.White,
                                    maxLines = 1
                                )
                            }
                        }
                    }

                    // iOS Pill Action Buttons (+ Uang Masuk & - Uang Keluar)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Button(
                            onClick = {
                                val amount = transactionNominalText.toLongOrNull() ?: 1_000_000L
                                SanctuarySavingsAndGoalsRepository.recordVaultCashflowTransaction(
                                    isMoneyIn = true,
                                    amountIdr = amount,
                                    note = transactionNoteText.ifBlank { "Setoran Uang Masuk Tabungan" },
                                    targetGoalId = selectedTargetGoalIdForTx
                                )
                                transactionNoteText = ""
                                onPostBanner("Uang Masuk +${formatIdrShort(amount)} ditambahkan ke Grafik Tabungan!")
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF30D158)),
                            shape = RoundedCornerShape(14.dp),
                            modifier = Modifier
                                .weight(1f)
                                .height(46.dp)
                                .testTag("savings_add_money_in_button")
                        ) {
                            Icon(
                                imageVector = Icons.Default.ArrowUpward,
                                contentDescription = null,
                                tint = Color.Black,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Tambah Dana",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.Black
                            )
                        }

                        Button(
                            onClick = {
                                val amount = transactionNominalText.toLongOrNull() ?: 1_000_000L
                                SanctuarySavingsAndGoalsRepository.recordVaultCashflowTransaction(
                                    isMoneyIn = false,
                                    amountIdr = amount,
                                    note = transactionNoteText.ifBlank { "Penarikan / Pengeluaran Tabungan" },
                                    targetGoalId = selectedTargetGoalIdForTx
                                )
                                transactionNoteText = ""
                                onPostBanner("Uang Keluar -${formatIdrShort(amount)} dicatat di Grafik Tabungan!")
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF453A).copy(alpha = 0.20f)),
                            shape = RoundedCornerShape(14.dp),
                            modifier = Modifier
                                .weight(1f)
                                .height(46.dp)
                                .testTag("savings_add_money_out_button")
                        ) {
                            Icon(
                                imageVector = Icons.Default.ArrowDownward,
                                contentDescription = null,
                                tint = Color(0xFFFF453A),
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Tarik Dana",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFFFF453A)
                            )
                        }
                    }
                }
            }
        }

        // ====================================================================
        // BAGIAN 2: GOALS BARANG IMPIAN (iOS 18.2 APPLE SAVINGS GOAL PODS)
        // ====================================================================
        if (displayMode == SanctuaryLedgerSubViewMode.ALL ||
            displayMode == SanctuaryLedgerSubViewMode.BUY_GOALS_ONLY
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // Header Card untuk Goals Barang Impian
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(24.dp))
                        .background(Color(0xFF1C1C1E))
                        .border(0.7.dp, iosGlassBorderBrush, RoundedCornerShape(24.dp))
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(42.dp)
                                    .clip(RoundedCornerShape(13.dp))
                                    .background(Color(0xFF0A84FF).copy(alpha = 0.18f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.ShoppingBag,
                                    contentDescription = "Goals Barang Impian",
                                    tint = Color(0xFF0A84FF),
                                    modifier = Modifier.size(21.dp)
                                )
                            }
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "Wishlist & Target Barang",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFFF2F2F7)
                                )
                                Text(
                                    text = "${buyGoals.size} Barang • ${formatIdrShort(totalGoalSaved)} / ${formatIdrShort(totalGoalTarget)}",
                                    fontSize = 12.sp,
                                    color = Color(0xFF8E8E93)
                                )
                            }
                        }

                        // iOS Capsule Add Button
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(999.dp))
                                .background(Color(0xFF0A84FF))
                                .clickable { showAddGoalModal = !showAddGoalModal }
                                .padding(horizontal = 14.dp, vertical = 8.dp)
                                .testTag("toggle_add_buy_goal_button")
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Icon(
                                    imageVector = if (showAddGoalModal) Icons.Default.ExpandLess else Icons.Default.Add,
                                    contentDescription = "Tambah Barang",
                                    tint = Color.White,
                                    modifier = Modifier.size(16.dp)
                                )
                                Text(
                                    text = if (showAddGoalModal) "Tutup" else "Tambah",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            }
                        }
                    }

                    // iOS 18 Inset Grouped Sheet: Tambah Barang Impian Baru
                    AnimatedVisibility(
                        visible = showAddGoalModal,
                        enter = expandVertically() + fadeIn(),
                        exit = shrinkVertically() + fadeOut()
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 4.dp),
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Text(
                                text = "DETAIL TARGET BARANG BARU",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color(0xFF8E8E93),
                                modifier = Modifier.padding(start = 4.dp)
                            )

                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(16.dp))
                                    .background(Color(0xFF2C2C2E))
                            ) {
                                IosCupertinoFieldRow(
                                    label = "Nama Barang",
                                    value = newItemName,
                                    onValueChange = { newItemName = it },
                                    placeholder = "iPhone 16 Pro Max / Kamera",
                                    accentColor = Color.White,
                                    testTag = "input_new_buy_goal_name"
                                )
                                HorizontalDivider(thickness = 0.5.dp, color = Color(0xFF38383A), modifier = Modifier.padding(start = 14.dp))
                                IosCupertinoFieldRow(
                                    label = "Kategori",
                                    value = newItemCategory,
                                    onValueChange = { newItemCategory = it },
                                    placeholder = "GADGET & WORKSTATION",
                                    accentColor = Color(0xFF0A84FF)
                                )
                                HorizontalDivider(thickness = 0.5.dp, color = Color(0xFF38383A), modifier = Modifier.padding(start = 14.dp))
                                IosCupertinoFieldRow(
                                    label = "Spesifikasi",
                                    value = newItemSpecs,
                                    onValueChange = { newItemSpecs = it },
                                    placeholder = "Warna / Kapasitas / Toko",
                                    accentColor = Color(0xFFAEAEB2)
                                )
                                HorizontalDivider(thickness = 0.5.dp, color = Color(0xFF38383A), modifier = Modifier.padding(start = 14.dp))
                                IosCupertinoFieldRow(
                                    label = "Harga Barang (Rp)",
                                    value = newItemPriceText,
                                    onValueChange = { newItemPriceText = it.filter { ch -> ch.isDigit() } },
                                    placeholder = "25000000",
                                    isNumeric = true,
                                    accentColor = Color(0xFFFF9F0A),
                                    testTag = "input_new_buy_goal_price"
                                )
                                HorizontalDivider(thickness = 0.5.dp, color = Color(0xFF38383A), modifier = Modifier.padding(start = 14.dp))
                                IosCupertinoFieldRow(
                                    label = "Tabungan Awal (Rp)",
                                    value = newItemInitialSavedText,
                                    onValueChange = { newItemInitialSavedText = it.filter { ch -> ch.isDigit() } },
                                    placeholder = "5000000",
                                    isNumeric = true,
                                    accentColor = Color(0xFF30D158)
                                )
                                HorizontalDivider(thickness = 0.5.dp, color = Color(0xFF38383A), modifier = Modifier.padding(start = 14.dp))
                                IosCupertinoFieldRow(
                                    label = "Rencana / Bulan",
                                    value = newItemMonthlyPlanText,
                                    onValueChange = { newItemMonthlyPlanText = it.filter { ch -> ch.isDigit() } },
                                    placeholder = "2500000",
                                    isNumeric = true,
                                    accentColor = Color(0xFFBF5AF2)
                                )
                            }

                            Button(
                                onClick = {
                                    val cleanName = newItemName.trim().ifBlank {
                                        "Barang Impian #${buyGoals.size + 1}"
                                    }
                                    val price = newItemPriceText.toLongOrNull() ?: 25_000_000L
                                    val initial = newItemInitialSavedText.toLongOrNull() ?: 0L
                                    val monthly = newItemMonthlyPlanText.toLongOrNull() ?: 2_500_000L
                                    SanctuarySavingsAndGoalsRepository.addNewBuyGoal(
                                        itemName = cleanName,
                                        itemCategory = newItemCategory,
                                        itemSpecsNote = newItemSpecs,
                                        targetPriceIdr = price,
                                        initialSavedIdr = initial,
                                        monthlyPlanIdr = monthly
                                    )
                                    val newlyCreatedId = SanctuarySavingsAndGoalsRepository.buyGoals.firstOrNull()?.id
                                    if (newlyCreatedId != null) {
                                        expandedGoalChartIds = expandedGoalChartIds + newlyCreatedId
                                    }
                                    newItemName = ""
                                    newItemSpecs = ""
                                    showAddGoalModal = false
                                    onPostBanner("Barang '$cleanName' (${formatIdrCurrency(price)}) berhasil ditambahkan ke Goals Tabungan!")
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0A84FF)),
                                shape = RoundedCornerShape(14.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(46.dp)
                                    .testTag("submit_new_buy_goal_button")
                            ) {
                                Text(
                                    text = "Simpan ke Daftar Goals Barang",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            }
                        }
                    }
                }

                // Daftar Kartu Setiap Barang yang Mau Dibeli (iOS 18.2 Apple Savings Pods)
                buyGoals.forEachIndexed { goalIdx, goal ->
                    val accentColor = Color(goal.accentHex)
                    val isChartExpanded = goal.id in expandedGoalChartIds
                    val animatedGoalSaved = rememberGsapRollingLong(goal.savedAmountIdr)
                    var stepDepositText by remember(goal.id) {
                        mutableStateOf(goal.monthlyPlanIdr.toString())
                    }

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .gsapStaggerReveal(index = goalIdx + 1, triggerKey = displayMode)
                            .gsap3DPress(maxTiltDegrees = 6.5f, pressScale = 0.98f)
                            .clip(RoundedCornerShape(24.dp))
                            .background(Color(0xFF1C1C1E))
                            .border(0.7.dp, iosGlassBorderBrush, RoundedCornerShape(24.dp))
                            .padding(16.dp)
                            .testTag("buy_goal_card_${goal.id}"),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        // Top Row: Circular iOS Ring + Title/Specs + Action Buttons
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            IosCircularGoalRing(
                                progress = goal.progressRatio,
                                accentColor = if (goal.isPurchased) Color(0xFF30D158) else accentColor,
                                percentText = "${goal.progressPercent}%"
                            )

                            Column(modifier = Modifier.weight(1f)) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Text(
                                        text = goal.itemCategory,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = accentColor
                                    )
                                    if (goal.isPurchased) {
                                        Box(
                                            modifier = Modifier
                                                .clip(RoundedCornerShape(6.dp))
                                                .background(Color(0xFF30D158).copy(alpha = 0.20f))
                                                .padding(horizontal = 6.dp, vertical = 2.dp)
                                        ) {
                                            Text(
                                                text = "LUNAS",
                                                fontSize = 9.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = Color(0xFF30D158)
                                            )
                                        }
                                    } else if (goal.remainingAmountIdr == 0L) {
                                        Box(
                                            modifier = Modifier
                                                .clip(RoundedCornerShape(6.dp))
                                                .background(Color(0xFFFF9F0A).copy(alpha = 0.20f))
                                                .padding(horizontal = 6.dp, vertical = 2.dp)
                                        ) {
                                            Text(
                                                text = "SIAP BELI",
                                                fontSize = 9.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = Color(0xFFFF9F0A)
                                            )
                                        }
                                    }
                                }
                                Text(
                                    text = goal.itemName,
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFFF2F2F7),
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Text(
                                    text = goal.itemSpecsNote,
                                    fontSize = 11.sp,
                                    color = Color(0xFF8E8E93),
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
                                        .clip(RoundedCornerShape(999.dp))
                                        .background(
                                            if (isChartExpanded) accentColor.copy(alpha = 0.22f)
                                            else Color(0xFF2C2C2E)
                                        )
                                        .clickable {
                                            expandedGoalChartIds = if (isChartExpanded) {
                                                expandedGoalChartIds - goal.id
                                            } else {
                                                expandedGoalChartIds + goal.id
                                            }
                                        }
                                        .padding(horizontal = 10.dp, vertical = 6.dp)
                                        .testTag("toggle_goal_chart_${goal.id}")
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.ShowChart,
                                            contentDescription = "Grafik Candlestick Barang",
                                            tint = if (isChartExpanded) accentColor else Color(0xFFF2F2F7),
                                            modifier = Modifier.size(14.dp)
                                        )
                                        Text(
                                            text = if (isChartExpanded) "Tutup" else "Chart",
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.SemiBold,
                                            color = if (isChartExpanded) accentColor else Color(0xFFF2F2F7)
                                        )
                                    }
                                }

                                IconButton(
                                    onClick = {
                                        SanctuarySavingsAndGoalsRepository.deleteBuyGoal(goal.id)
                                        onPostBanner("Target barang '${goal.itemName}' dihapus dari daftar Goals.")
                                    },
                                    modifier = Modifier
                                        .size(30.dp)
                                        .clip(CircleShape)
                                        .background(Color(0xFF2C2C2E))
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Delete,
                                        contentDescription = "Hapus Barang",
                                        tint = Color(0xFFFF453A),
                                        modifier = Modifier.size(15.dp)
                                    )
                                }
                            }
                        }

                        // iOS Inset Grouped Financial Metrics Table for This Goal
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(14.dp))
                                .background(Color(0xFF2C2C2E))
                                .padding(horizontal = 14.dp, vertical = 10.dp),
                            verticalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("Dana Terkumpul", fontSize = 12.sp, color = Color(0xFF8E8E93))
                                Text(
                                    text = formatIdrCurrency(animatedGoalSaved),
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF30D158)
                                )
                            }
                            HorizontalDivider(thickness = 0.5.dp, color = Color(0xFF38383A))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("Harga Target", fontSize = 12.sp, color = Color(0xFF8E8E93))
                                Text(
                                    text = formatIdrCurrency(goal.targetPriceIdr),
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = Color(0xFFF2F2F7)
                                )
                            }
                            HorizontalDivider(thickness = 0.5.dp, color = Color(0xFF38383A))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = if (goal.remainingAmountIdr == 0L) "Status Dana" else "Sisa Kekurangan",
                                    fontSize = 12.sp,
                                    color = Color(0xFF8E8E93)
                                )
                                Text(
                                    text = if (goal.remainingAmountIdr == 0L) {
                                        "100% Tercapai • Siap Checkout"
                                    } else {
                                        "${formatIdrCurrency(goal.remainingAmountIdr)} (~${goal.estimatedMonthsLeft}x setor)"
                                    },
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = if (goal.remainingAmountIdr == 0L) Color(0xFFFF9F0A) else accentColor
                                )
                            }
                        }

                        // GRAFIK BOX-BOX CANDLESTICK & VOLUME KHUSUS UNTUK BARANG INI
                        AnimatedVisibility(
                            visible = isChartExpanded,
                            enter = expandVertically() + fadeIn(),
                            exit = shrinkVertically() + fadeOut()
                        ) {
                            CashflowCandlestickAndVolumeCanvas(
                                titleTicker = goal.itemName.take(18).uppercase(),
                                candles = goal.candleHistory,
                                targetCeilingIdr = goal.targetPriceIdr,
                                showFibonacci = showFibonacciLines,
                                showTrendChannel = showTrendlineChannel,
                                showMovingAverage = showMovingAverage,
                                chartHeightDp = 205,
                                testTagName = "goal_candlestick_chart_${goal.id}"
                            )
                        }

                        // iOS Cupertino Stepper & Quick Top-Up Row
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Compact iOS Pill Nominal Input
                            Box(
                                modifier = Modifier
                                    .weight(1.1f)
                                    .height(40.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(Color(0xFF2C2C2E))
                                    .padding(horizontal = 12.dp),
                                contentAlignment = Alignment.CenterStart
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Text("Rp", fontSize = 11.sp, color = Color(0xFF8E8E93))
                                    BasicTextField(
                                        value = stepDepositText,
                                        onValueChange = { stepDepositText = it.filter { ch -> ch.isDigit() } },
                                        singleLine = true,
                                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                        textStyle = TextStyle(
                                            color = Color.White,
                                            fontSize = 13.sp,
                                            fontWeight = FontWeight.SemiBold
                                        ),
                                        cursorBrush = SolidColor(accentColor),
                                        modifier = Modifier.fillMaxWidth()
                                    )
                                }
                            }

                            Button(
                                onClick = {
                                    val amt = stepDepositText.toLongOrNull() ?: goal.monthlyPlanIdr
                                    SanctuarySavingsAndGoalsRepository.mutateGoalSavings(
                                        goalId = goal.id,
                                        isDeposit = true,
                                        amountIdr = amt,
                                        note = "Setoran tabungan barang ${goal.itemName}"
                                    )
                                    onPostBanner("+${formatIdrShort(amt)} ditabung untuk '${goal.itemName}'!")
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF30D158)),
                                shape = RoundedCornerShape(12.dp),
                                contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 12.dp, vertical = 0.dp),
                                modifier = Modifier
                                    .weight(0.85f)
                                    .height(40.dp)
                            ) {
                                Text(
                                    text = "+ Tabung",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.Black
                                )
                            }

                            Button(
                                onClick = {
                                    val amt = stepDepositText.toLongOrNull() ?: (goal.monthlyPlanIdr / 2L)
                                    SanctuarySavingsAndGoalsRepository.mutateGoalSavings(
                                        goalId = goal.id,
                                        isDeposit = false,
                                        amountIdr = amt,
                                        note = "Penarikan dana dari kantong ${goal.itemName}"
                                    )
                                    onPostBanner("-${formatIdrShort(amt)} ditarik dari '${goal.itemName}'.")
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2C2C2E)),
                                shape = RoundedCornerShape(12.dp),
                                contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 10.dp, vertical = 0.dp),
                                modifier = Modifier
                                    .weight(0.75f)
                                    .height(40.dp)
                            ) {
                                Text(
                                    text = "- Tarik",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = Color(0xFFFF453A)
                                )
                            }

                            if (goal.remainingAmountIdr == 0L || goal.isPurchased) {
                                Button(
                                    onClick = {
                                        SanctuarySavingsAndGoalsRepository.checkoutAndPurchaseGoal(goal.id)
                                        val msg = if (!goal.isPurchased) {
                                            "Barang '${goal.itemName}' ditandai LUNAS DIBELI!"
                                        } else {
                                            "Status pembelian '${goal.itemName}' dikembalikan ke aktif."
                                        }
                                        onPostBanner(msg)
                                    },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = if (goal.isPurchased) Color(0xFF3A3A3C) else Color(0xFF0A84FF)
                                    ),
                                    shape = RoundedCornerShape(12.dp),
                                    contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 10.dp, vertical = 0.dp),
                                    modifier = Modifier
                                        .weight(0.85f)
                                        .height(40.dp)
                                ) {
                                    Text(
                                        text = if (goal.isPurchased) "Batal" else "Checkout",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
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

@Composable
private fun SummaryMetricBentoPill(
    label: String,
    valueText: String,
    accentColor: Color,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFF2C2C2E))
            .padding(horizontal = 12.dp, vertical = 10.dp),
        verticalArrangement = Arrangement.spacedBy(3.dp)
    ) {
        Text(
            text = label,
            fontSize = 10.sp,
            fontWeight = FontWeight.Medium,
            color = Color(0xFF8E8E93),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Text(
            text = valueText,
            fontSize = 13.5.sp,
            fontWeight = FontWeight.Bold,
            color = accentColor,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
private fun ChartIndicatorToggleChip(
    label: String,
    checked: Boolean,
    accentColor: Color,
    onToggle: () -> Unit
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(10.dp))
            .background(if (checked) accentColor.copy(alpha = 0.22f) else Color(0xFF2C2C2E))
            .clickable { onToggle() }
            .padding(horizontal = 10.dp, vertical = 6.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(6.dp)
                    .clip(CircleShape)
                    .background(if (checked) accentColor else Color(0xFF636366))
            )
            Text(
                text = label,
                fontSize = 10.5.sp,
                fontWeight = FontWeight.SemiBold,
                color = if (checked) Color.White else Color(0xFF8E8E93)
            )
        }
    }
}

/**
 * Kanvas Grafik Box-Box Candlestick + Histogram Volume Berdekatan + Fibonacci Retracement
 * + Channel Trendline + Header 2x2 OHLCV ala Apple Stocks iOS 18.2.
 */
@Composable
fun CashflowCandlestickAndVolumeCanvas(
    titleTicker: String,
    candles: List<CashflowCandlePoint>,
    targetCeilingIdr: Long,
    showFibonacci: Boolean,
    showTrendChannel: Boolean,
    showMovingAverage: Boolean,
    chartHeightDp: Int = 250,
    testTagName: String = "candlestick_chart_canvas"
) {
    val safeCandles = if (candles.isNotEmpty()) candles else listOf(
        CashflowCandlePoint("empty", "P-1", 0L, 0L, 0L, 0L, 0L, 0L, "Belum ada data")
    )
    var selectedCandleIdx by remember(safeCandles.size) {
        mutableIntStateOf((safeCandles.size - 1).coerceAtLeast(0))
    }
    val activeIdx = selectedCandleIdx.coerceIn(0, safeCandles.lastIndex)
    val selectedCandle = safeCandles[activeIdx]

    val minLow = safeCandles.minOf { it.lowBalanceIdr }.coerceAtLeast(0L)
    val maxHighRaw = max(safeCandles.maxOf { it.highBalanceIdr }, targetCeilingIdr)
    val maxHigh = max(maxHighRaw, minLow + 1_000_000L)
    val priceSpan = (maxHigh - minLow).coerceAtLeast(1_000_000L).toFloat()
    val maxVolume = safeCandles.maxOf { it.totalVolumeIdr }.coerceAtLeast(1_000_000L).toFloat()

    // GSAP Dynamic Candlestick & Volume Growth Progress (0f -> 1f with per-candle stagger)
    val chartGrowthProgress = rememberGsapRollingFloat(
        targetValue = 1f,
        durationMs = 680
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .background(Color(0xFF101012))
            .border(0.5.dp, Color.White.copy(alpha = 0.08f), RoundedCornerShape(18.dp))
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        // APPLE STOCKS iOS 18.2 OHLCV HEADER TABLE
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(14.dp))
                .background(Color(0xFF1C1C1E))
                .padding(horizontal = 12.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1.1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = "$titleTicker • ${selectedCandle.periodLabel}",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF0A84FF)
                    )
                    Text(
                        text = String.format(
                            Locale.US,
                            "%s%.2f%%",
                            if (selectedCandle.isBullish) "+" else "",
                            selectedCandle.changePercent
                        ),
                        fontFamily = JetBrainsMonoFontFamily,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (selectedCandle.isBullish) Color(0xFF30D158) else Color(0xFFFF453A)
                    )
                }
                Text(
                    text = formatIdrShort(selectedCandle.closeBalanceIdr),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFF2F2F7)
                )
                Text(
                    text = selectedCandle.note,
                    fontSize = 10.sp,
                    color = Color(0xFF8E8E93),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            // 2x2 Apple Stocks Mini Stat Grid (OPEN / HIGH / LOW / VOL)
            Column(
                modifier = Modifier.weight(0.95f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("OPEN", fontSize = 9.sp, color = Color(0xFF636366), fontWeight = FontWeight.Bold)
                    Text(formatIdrShort(selectedCandle.openBalanceIdr), fontFamily = JetBrainsMonoFontFamily, fontSize = 9.5.sp, color = Color(0xFFF2F2F7))
                    Text("HIGH", fontSize = 9.sp, color = Color(0xFF636366), fontWeight = FontWeight.Bold)
                    Text(formatIdrShort(selectedCandle.highBalanceIdr), fontFamily = JetBrainsMonoFontFamily, fontSize = 9.5.sp, color = Color(0xFF30D158))
                }
                HorizontalDivider(thickness = 0.5.dp, color = Color(0xFF2C2C2E))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("LOW", fontSize = 9.sp, color = Color(0xFF636366), fontWeight = FontWeight.Bold)
                    Text(formatIdrShort(selectedCandle.lowBalanceIdr), fontFamily = JetBrainsMonoFontFamily, fontSize = 9.5.sp, color = Color(0xFFFF453A))
                    Text("VOL", fontSize = 9.sp, color = Color(0xFF636366), fontWeight = FontWeight.Bold)
                    Text(formatIdrShort(selectedCandle.totalVolumeIdr), fontFamily = JetBrainsMonoFontFamily, fontSize = 9.5.sp, color = Color(0xFF64D2FF))
                }
            }
        }

        // KANVAS UTAMA: ATAS (72%) = CANDLESTICK BOX-BOX + FIBONACCI + TRENDLINE CHANNEL
        // BAWAH (28%) = BAR HISTOGRAM VOLUME BERDEKATAN + GARIS MA VOLUME
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(chartHeightDp.dp)
                .pointerInput(safeCandles.size) {
                    detectTapGestures { offset ->
                        val w = size.width.toFloat().coerceAtLeast(1f)
                        val count = safeCandles.size
                        val slotWidth = w / count.toFloat()
                        val tappedIndex = (offset.x / slotWidth).toInt().coerceIn(0, count - 1)
                        selectedCandleIdx = tappedIndex
                    }
                }
                .testTag(testTagName)
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val w = size.width
                val h = size.height
                val candleTopPad = 10.dp.toPx()
                val candleAreaBottom = h * 0.71f
                val candleAreaHeight = (candleAreaBottom - candleTopPad).coerceAtLeast(20f)

                val volTop = h * 0.76f
                val volBottom = h - 6.dp.toPx()
                val volHeight = (volBottom - volTop).coerceAtLeast(12f)

                fun priceToY(price: Long): Float {
                    val norm = ((price - minLow).toFloat() / priceSpan).coerceIn(0f, 1f)
                    return candleAreaBottom - (norm * candleAreaHeight)
                }

                // 1. Subtle iOS Stocks Grid Lines
                val vGridCount = 5
                for (i in 0..vGridCount) {
                    val gx = (w / vGridCount) * i
                    drawLine(
                        color = Color.White.copy(alpha = 0.04f),
                        start = Offset(gx, 0f),
                        end = Offset(gx, h),
                        strokeWidth = 1f
                    )
                }

                // 2. Garis Putus-Putus Fibonacci Retracement (iOS Indigo #5E5CE6)
                val fibLevels = listOf(0.0f, 0.236f, 0.382f, 0.50f, 0.618f, 0.786f, 1.0f)
                val dashEffect = PathEffect.dashPathEffect(floatArrayOf(8f, 6f), 0f)
                fibLevels.forEach { fib ->
                    val y = candleAreaBottom - (fib * candleAreaHeight)
                    drawLine(
                        color = if (showFibonacci) Color(0xFF5E5CE6).copy(alpha = 0.38f)
                        else Color.White.copy(alpha = 0.04f),
                        start = Offset(0f, y),
                        end = Offset(w, y),
                        strokeWidth = if (fib == 1.0f || fib == 0.618f) 1.5f else 0.9f,
                        pathEffect = dashEffect
                    )
                }

                // Hairline Separator between Candlesticks & Volume
                drawLine(
                    color = Color(0xFF2C2C2E),
                    start = Offset(0f, h * 0.735f),
                    end = Offset(w, h * 0.735f),
                    strokeWidth = 1f
                )

                val count = safeCandles.size
                val slotW = w / count.toFloat()
                val boxWidth = (slotW * 0.72f).coerceAtLeast(6f)

                // 3. Diagonal Ascending Trendline Channel (iOS Cyan #64D2FF)
                if (showTrendChannel && count >= 2) {
                    val firstC = safeCandles.first()
                    val lastC = safeCandles.last()
                    val xStart = slotW * 0.5f
                    val xEnd = w - slotW * 0.5f

                    val upperStartY = priceToY(firstC.highBalanceIdr)
                    val upperEndY = priceToY(lastC.highBalanceIdr)
                    val lowerStartY = priceToY(firstC.lowBalanceIdr)
                    val lowerEndY = priceToY(lastC.lowBalanceIdr)

                    drawLine(
                        color = Color(0xFF64D2FF).copy(alpha = 0.65f),
                        start = Offset(xStart, upperStartY),
                        end = Offset(xEnd, upperEndY),
                        strokeWidth = 2f
                    )
                    drawLine(
                        color = Color(0xFF64D2FF).copy(alpha = 0.65f),
                        start = Offset(xStart, lowerStartY),
                        end = Offset(xEnd, lowerEndY),
                        strokeWidth = 2f
                    )
                }

                // 4. Volume Histogram & Candlesticks (iOS Green #30D158 & iOS Red #FF453A)
                val maPoints = mutableListOf<Offset>()
                val volMaPoints = mutableListOf<Offset>()

                safeCandles.forEachIndexed { index, candle ->
                    val centerX = (index + 0.5f) * slotW
                    val isBull = candle.isBullish
                    val mainColor = if (isBull) Color(0xFF30D158) else Color(0xFFFF453A)
                    val staggerFraction = index.toFloat() / count.coerceAtLeast(1).toFloat()
                    val candleProgress = ((chartGrowthProgress * 1.32f) - (staggerFraction * 0.32f)).coerceIn(0f, 1f)

                    if (index == activeIdx) {
                        drawLine(
                            color = Color.White.copy(alpha = 0.45f),
                            start = Offset(centerX, 0f),
                            end = Offset(centerX, h),
                            strokeWidth = 1.2f,
                            pathEffect = dashEffect
                        )
                    }

                    // --- A. BAR HISTOGRAM VOLUME (GSAP Staggered Growth) ---
                    val volNorm = (candle.totalVolumeIdr.toFloat() / maxVolume).coerceIn(0.08f, 1f)
                    val barH = volNorm * volHeight * candleProgress
                    val barLeft = centerX - (slotW * 0.40f)
                    val barWidth = (slotW * 0.80f).coerceAtLeast(4f)
                    drawRoundRect(
                        color = mainColor.copy(alpha = if (index == activeIdx) 0.85f else 0.42f),
                        topLeft = Offset(barLeft, volBottom - barH),
                        size = Size(barWidth, barH),
                        cornerRadius = CornerRadius(2f, 2f)
                    )

                    val avgVolWindow = safeCandles.subList(max(0, index - 2), index + 1)
                        .map { it.totalVolumeIdr.toFloat() }
                        .average()
                        .toFloat()
                    val volMaY = volBottom - ((avgVolWindow / maxVolume).coerceIn(0.08f, 1f) * volHeight * candleProgress)
                    volMaPoints.add(Offset(centerX, volMaY))

                    // --- B. CANDLESTICK BOX & WICK (GSAP Staggered Growth) ---
                    val openY = priceToY(candle.openBalanceIdr)
                    val highY = openY + (priceToY(candle.highBalanceIdr) - openY) * candleProgress
                    val lowY = openY + (priceToY(candle.lowBalanceIdr) - openY) * candleProgress
                    val closeY = openY + (priceToY(candle.closeBalanceIdr) - openY) * candleProgress

                    drawLine(
                        color = mainColor,
                        start = Offset(centerX, highY),
                        end = Offset(centerX, lowY),
                        strokeWidth = 1.8f,
                        cap = StrokeCap.Round
                    )

                    val boxTop = min(openY, closeY)
                    val rawBoxHeight = abs(closeY - openY)
                    val boxHeight = max(rawBoxHeight, 6f * candleProgress.coerceAtLeast(0.2f))
                    val boxLeft = centerX - (boxWidth / 2f)

                    drawRoundRect(
                        color = mainColor,
                        topLeft = Offset(boxLeft, boxTop),
                        size = Size(boxWidth, boxHeight),
                        cornerRadius = CornerRadius(3f, 3f)
                    )
                    if (index == activeIdx) {
                        drawRoundRect(
                            color = Color.White,
                            topLeft = Offset(boxLeft, boxTop),
                            size = Size(boxWidth, boxHeight),
                            cornerRadius = CornerRadius(3f, 3f),
                            style = Stroke(width = 1.8f)
                        )
                    }

                    val windowSlice = safeCandles.subList(max(0, index - 2), index + 1)
                    val avgClose = windowSlice.map { it.closeBalanceIdr }.average().toLong()
                    maPoints.add(Offset(centerX, priceToY(avgClose)))
                }

                // 5. Moving Average Curves (iOS Orange #FF9F0A)
                if (showMovingAverage && maPoints.size >= 2) {
                    val maPath = Path().apply {
                        moveTo(maPoints.first().x, maPoints.first().y)
                        for (i in 1 until maPoints.size) {
                            lineTo(maPoints[i].x, maPoints[i].y)
                        }
                    }
                    drawPath(
                        path = maPath,
                        color = Color(0xFFFF9F0A),
                        style = Stroke(width = 2.2f, cap = StrokeCap.Round)
                    )
                }

                if (volMaPoints.size >= 2) {
                    val volMaPath = Path().apply {
                        moveTo(volMaPoints.first().x, volMaPoints.first().y)
                        for (i in 1 until volMaPoints.size) {
                            lineTo(volMaPoints[i].x, volMaPoints[i].y)
                        }
                    }
                    drawPath(
                        path = volMaPath,
                        color = Color(0xFF8E8E93).copy(alpha = 0.7f),
                        style = Stroke(width = 1.4f, cap = StrokeCap.Round)
                    )
                }
            }

            if (showFibonacci) {
                Column(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(end = 6.dp, top = 6.dp),
                    horizontalAlignment = Alignment.End,
                    verticalArrangement = Arrangement.spacedBy(3.dp)
                ) {
                    Surface(
                        color = Color(0xFF1C1C1E).copy(alpha = 0.88f),
                        shape = RoundedCornerShape(6.dp)
                    ) {
                        Text(
                            text = "100% Target: ${formatIdrShort(maxHigh)}",
                            fontFamily = JetBrainsMonoFontFamily,
                            fontSize = 8.sp,
                            color = Color(0xFF5E5CE6),
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                    Surface(
                        color = Color(0xFF1C1C1E).copy(alpha = 0.88f),
                        shape = RoundedCornerShape(6.dp)
                    ) {
                        Text(
                            text = "61.8% Fib: ${formatIdrShort(minLow + (priceSpan * 0.618f).toLong())}",
                            fontFamily = JetBrainsMonoFontFamily,
                            fontSize = 8.sp,
                            color = Color(0xFF64D2FF),
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }
            }
        }

        // iOS Scrubbing Period Pills
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            safeCandles.forEachIndexed { idx, candle ->
                val isSelected = idx == activeIdx
                val chipColor = if (candle.isBullish) Color(0xFF30D158) else Color(0xFFFF453A)
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(
                            if (isSelected) chipColor.copy(alpha = 0.22f)
                            else Color(0xFF1C1C1E)
                        )
                        .border(
                            0.5.dp,
                            if (isSelected) chipColor else Color.White.copy(alpha = 0.08f),
                            RoundedCornerShape(8.dp)
                        )
                        .clickable { selectedCandleIdx = idx }
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "${candle.periodLabel} (${if (candle.isBullish) "+" else ""}${formatIdrShort(candle.netChangeIdr)})",
                        fontFamily = JetBrainsMonoFontFamily,
                        fontSize = 8.5.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                        color = if (isSelected) Color.White else chipColor
                    )
                }
            }
        }
    }
}

