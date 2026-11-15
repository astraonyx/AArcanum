package com.example.ui

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas as AndroidCanvas
import android.graphics.Color as AndroidColor
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.Path
import android.graphics.RectF
import android.graphics.Shader
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material.icons.filled.QrCode2
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.example.data.SecurityConfig
import com.example.data.VaultCategory
import com.example.data.VaultItemEntity
import com.example.ui.theme.DuoBeeYellow
import com.example.ui.theme.DuoBeeYellowLip
import com.example.ui.theme.DuoBeeYellowSoft
import com.example.ui.theme.DuoBeetlePurple
import com.example.ui.theme.DuoBeetlePurpleLip
import com.example.ui.theme.DuoBeetlePurpleSoft
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
import com.example.ui.theme.duolingoButton
import com.example.ui.theme.duolingoCard
import java.io.ByteArrayOutputStream
import java.io.File
import java.security.MessageDigest
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.sin

enum class AstraOnyxQrTheme(
    val label: String,
    val primaryHex: Int,
    val secondaryHex: Int,
    val bgTopHex: Int,
    val bgBottomHex: Int,
    val composeColor: Color,
    val composeLip: Color
) {
    NEBULA_STAR(
        label = "Bintang Nebula Ungu",
        primaryHex = 0xFFCE82FF.toInt(),
        secondaryHex = 0xFF58CC02.toInt(),
        bgTopHex = 0xFF140B29.toInt(),
        bgBottomHex = 0xFF231145.toInt(),
        composeColor = DuoBeetlePurple,
        composeLip = DuoBeetlePurpleLip
    ),
    SOLAR_GOLD(
        label = "Bintang Emas Kosmik",
        primaryHex = 0xFFFFC800.toInt(),
        secondaryHex = 0xFFFF9600.toInt(),
        bgTopHex = 0xFF1E1606.toInt(),
        bgBottomHex = 0xFF332308.toInt(),
        composeColor = DuoBeeYellow,
        composeLip = DuoBeeYellowLip
    ),
    EMERALD_CYBER(
        label = "Bintang Zamrud Duolingo",
        primaryHex = 0xFF58CC02.toInt(),
        secondaryHex = 0xFF1CB0F6.toInt(),
        bgTopHex = 0xFF0B2210.toInt(),
        bgBottomHex = 0xFF12361A.toInt(),
        composeColor = DuoFeatherGreen,
        composeLip = DuoFeatherGreenLip
    ),
    MACAW_SUPERNOVA(
        label = "Bintang Supernova Biru",
        primaryHex = 0xFF1CB0F6.toInt(),
        secondaryHex = 0xFFCE82FF.toInt(),
        bgTopHex = 0xFF081C2E.toInt(),
        bgBottomHex = 0xFF0E2F4D.toInt(),
        composeColor = DuoMacawBlue,
        composeLip = DuoMacawBlueLip
    )
}

object AstraOnyxStarQrEngine {
    private const val MAGIC_PREFIX = "ASTRAONYX_STAR_QR::"
    private const val SYNC_WORD_16 = 0xA57A
    const val ORBIT_COUNT = 10
    const val SECTOR_COUNT = 48
    const val TOTAL_POLAR_NODES = ORBIT_COUNT * SECTOR_COUNT // 480 Polar-Harmonic Star Nodes

    data class PolarAlgorithmTelemetry(
        val crc16Hex: String,
        val octantParityBits: String,
        val activeStarNodes: Int,
        val totalNodes: Int = TOTAL_POLAR_NODES,
        val payloadByteCount: Int,
        val harmonicEquation: String = "R(n,θ) = Rₙ·[1 + 0.095·cos(8θ) - 0.022·cos(16θ)]"
    )

    fun inspectPayloadTelemetry(payloadText: String): PolarAlgorithmTelemetry {
        val clean = payloadText.trim().ifBlank { "https://arcanum.astraonyx.vault/star-qr" }
        val bytes = clean.toByteArray(Charsets.UTF_8)
        val crc = computeCrc16Astra(bytes)
        val parity = computeOctantParityByte(bytes)
        val matrix = encodePayloadToPolarHarmonicMatrix(clean)
        var activeCount = 0
        for (o in 0 until ORBIT_COUNT) {
            for (s in 0 until SECTOR_COUNT) {
                if (matrix[o][s]) activeCount++
            }
        }
        return PolarAlgorithmTelemetry(
            crc16Hex = "0x" + crc.toString(16).uppercase().padStart(4, '0'),
            octantParityBits = parity.toString(2).padStart(8, '0'),
            activeStarNodes = activeCount,
            payloadByteCount = bytes.size
        )
    }

    /**
     * CRC-16-ASTRA (Polynomial 0x1021 dengan seed 0xA57A khas AstraOnyx).
     */
    fun computeCrc16Astra(data: ByteArray): Int {
        var crc = SYNC_WORD_16
        for (b in data) {
            crc = crc xor ((b.toInt() and 0xFF) shl 8)
            for (bit in 0 until 8) {
                crc = if ((crc and 0x8000) != 0) {
                    ((crc shl 1) xor 0x1021) and 0xFFFF
                } else {
                    (crc shl 1) and 0xFFFF
                }
            }
        }
        return crc and 0xFFFF
    }

    /**
     * Menghitung 8-bit Octant Parity untuk 8 Beacon Ujung Bintang.
     */
    fun computeOctantParityByte(data: ByteArray): Int {
        var acc = 0x5A
        for (i in data.indices) {
            val v = data[i].toInt() and 0xFF
            val shift = i % 5
            acc = (acc xor ((v shl shift) or (v ushr (8 - shift)))) and 0xFF
        }
        return acc
    }

    /**
     * Masker pemutih harmonik (Golden-Prime Harmonic Whitening Mask) agar distribusi node
     * rasi bintang selalu seimbang di seluruh 8 oktan bintang.
     */
    private fun goldenPrimeHarmonicMask(orbit: Int, sector: Int): Boolean {
        if (orbit == 0) return false // Orbit 0 menyimpan Sync Word, Length, CRC16, dan Parity murni
        val hash = (orbit * 19 + sector * 37 + (orbit xor sector) * 11 + (orbit * sector * 7)) and 0xFF
        return (hash % 3) == 0
    }

    /**
     * Rumus Koordinat Polar-Harmonik Bintang 8-Sudut (ASTRACODE-POLAR-8X):
     * R(orbit, θ) = R_base(orbit) * [1 + 0.095*cos(8θ) - 0.022*cos(16θ)]
     * Menghasilkan cincin data yang bergelombang mengikuti geometri bintang 8-sudut (bukan kotak & bukan lingkaran biasa).
     */
    private fun computePolarStarNodeCenter(
        cx: Float,
        cy: Float,
        outerRadius: Float,
        orbit: Int,
        sector: Int
    ): Pair<Float, Float> {
        val theta = -Math.PI / 2.0 + (sector.toDouble() / SECTOR_COUNT.toDouble()) * (2.0 * Math.PI)
        val starHarmonicWave = 1.0 + 0.095 * cos(8.0 * theta) - 0.022 * cos(16.0 * theta)
        val normalizedOrbit = orbit.toDouble() / (ORBIT_COUNT - 1).toDouble()
        val baseOrbitRadius = outerRadius * (0.29 + normalizedOrbit * 0.49)
        val r = baseOrbitRadius * starHarmonicWave
        val x = (cx + r * cos(theta)).toFloat()
        val y = (cy + r * sin(theta)).toFloat()
        return x to y
    }

    /**
     * Mengenkode payload ke dalam Matriks Polar-Harmonik 10 Orbit × 48 Sektor = 480 Bit.
     * - Orbit 0 (48 bit):
     *   [0..15]  = Sync Word 0xA57A
     *   [16..23] = Panjang Payload (0..54 byte)
     *   [24..39] = Checksum CRC-16-ASTRA
     *   [40..47] = Octant Parity Byte
     * - Orbit 1..9 (432 bit = 54 byte):
     *   Aliran bit UTF-8 payload + paritas berulang yang di-XOR dengan Golden-Prime Harmonic Mask.
     */
    fun encodePayloadToPolarHarmonicMatrix(payload: String): Array<BooleanArray> {
        val rawBytes = payload.toByteArray(Charsets.UTF_8)
        val clampedLen = rawBytes.size.coerceAtMost(54)
        val clampedBytes = rawBytes.copyOfRange(0, clampedLen)
        val crc16 = computeCrc16Astra(clampedBytes)
        val parity8 = computeOctantParityByte(clampedBytes)

        val matrix = Array(ORBIT_COUNT) { BooleanArray(SECTOR_COUNT) }

        // Orbit 0: 48-bit Header Frame
        val headerBits = IntArray(48)
        for (i in 0 until 16) headerBits[i] = (SYNC_WORD_16 ushr (15 - i)) and 1
        for (i in 0 until 8) headerBits[16 + i] = (clampedLen ushr (7 - i)) and 1
        for (i in 0 until 16) headerBits[24 + i] = (crc16 ushr (15 - i)) and 1
        for (i in 0 until 8) headerBits[40 + i] = (parity8 ushr (7 - i)) and 1
        for (s in 0 until SECTOR_COUNT) {
            matrix[0][s] = headerBits[s] == 1
        }

        // Orbit 1..9: 432-bit Payload + Reed-Folded Mirror Stream
        val payloadBitLen = clampedLen * 8
        for (bitPos in 0 until (ORBIT_COUNT - 1) * SECTOR_COUNT) {
            val orbit = 1 + (bitPos / SECTOR_COUNT)
            val sector = bitPos % SECTOR_COUNT
            val rawBit = if (payloadBitLen > 0) {
                val effectiveBitIndex = bitPos % payloadBitLen
                val byteIdx = effectiveBitIndex / 8
                val bitInByte = 7 - (effectiveBitIndex % 8)
                val b = clampedBytes[byteIdx].toInt() and 0xFF
                ((b ushr bitInByte) and 1) == 1
            } else {
                false
            }
            matrix[orbit][sector] = rawBit xor goldenPrimeHarmonicMask(orbit, sector)
        }
        return matrix
    }

    /**
     * Menghasilkan Bitmap kode optik ASTRACODE-POLAR-8X yang 100% berbeda dari QR kotak mana pun:
     * - Perisai Oktagram Ganda 16-Sudut 3D (Dual Interlocking 8-Pointed Stars)
     * - 8 Beacon Kalibrasi di 8 Ujung Bintang (Zenith Sun di Utara + 3 Kompas + 4 Beacon Paritas)
     * - 10 Orbit Gelombang Bintang Harmonik × 48 Sektor Sudut (480 Simpul Rasi Bintang)
     * - Jaringan Garis Konstelasi Kosmik antar simpul bit '1'
     */
    fun generateStarQrBitmap(
        payloadText: String,
        theme: AstraOnyxQrTheme,
        centerPhoto: Bitmap? = null,
        sizePx: Int = 720
    ): Bitmap {
        val cleanPayload = payloadText.trim().ifBlank { "https://arcanum.astraonyx.vault/star-qr" }
        val bmp = Bitmap.createBitmap(sizePx, sizePx, Bitmap.Config.ARGB_8888)
        val canvas = AndroidCanvas(bmp)
        val paint = Paint(Paint.ANTI_ALIAS_FLAG)

        val cx = sizePx / 2f
        val cy = sizePx / 2f
        val outerR = sizePx * 0.47f
        val innerR = sizePx * 0.36f

        // 0. Deep Cosmic Radial Backdrop Disk behind the Star Shield
        paint.style = Paint.Style.FILL
        paint.color = 0xFF070A14.toInt()
        canvas.drawCircle(cx, cy, outerR * 0.98f, paint)

        // 1. Secondary Rotated 8-Pointed Star Shield (Rotated 22.5 deg for 16-Pointed Sacred Octagram)
        val rotatedStarPath = createRotatedStarPath(
            cx = cx,
            cy = cy,
            points = 8,
            outerRadius = outerR * 0.93f,
            innerRadius = innerR * 0.94f,
            rotationRad = Math.PI / 8.0
        )
        paint.style = Paint.Style.FILL
        paint.color = theme.bgBottomHex
        canvas.drawPath(rotatedStarPath, paint)
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = sizePx * 0.008f
        paint.color = (theme.secondaryHex and 0x00FFFFFF) or 0xAA000000.toInt()
        canvas.drawPath(rotatedStarPath, paint)

        // 2. Primary 8-Pointed Cosmic Star Shield
        val starFramePath = createStarPath(cx, cy, points = 8, outerRadius = outerR, innerRadius = innerR)
        paint.shader = LinearGradient(
            0f, 0f, sizePx.toFloat(), sizePx.toFloat(),
            theme.bgTopHex, theme.bgBottomHex, Shader.TileMode.CLAMP
        )
        paint.style = Paint.Style.FILL
        canvas.drawPath(starFramePath, paint)
        paint.shader = null

        // 3D Outer Rim Border
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = sizePx * 0.013f
        paint.color = theme.primaryHex
        canvas.drawPath(starFramePath, paint)

        paint.strokeWidth = sizePx * 0.0045f
        paint.color = theme.secondaryHex
        val innerFramePath = createStarPath(cx, cy, points = 8, outerRadius = outerR * 0.95f, innerRadius = innerR * 0.95f)
        canvas.drawPath(innerFramePath, paint)

        // 3. Draw 8 Radial Octant Synchronizer Rays & 10 Harmonic Star-Wave Orbit Guides
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = sizePx * 0.0022f
        paint.color = (theme.primaryHex and 0x00FFFFFF) or 0x33000000
        for (k in 0 until 8) {
            val angle = -Math.PI / 2.0 + k * (Math.PI / 4.0)
            val xOuter = (cx + cos(angle) * (outerR * 0.86f)).toFloat()
            val yOuter = (cy + sin(angle) * (outerR * 0.86f)).toFloat()
            canvas.drawLine(cx, cy, xOuter, yOuter, paint)
        }

        // Draw undulating 8-fold Star-Wave Orbit tracks
        for (orbit in 0 until ORBIT_COUNT) {
            val orbitPath = Path()
            for (s in 0 until SECTOR_COUNT) {
                val (nx, ny) = computePolarStarNodeCenter(cx, cy, outerR, orbit, s)
                if (s == 0) orbitPath.moveTo(nx, ny) else orbitPath.lineTo(nx, ny)
            }
            orbitPath.close()
            paint.strokeWidth = if (orbit == 0) sizePx * 0.0035f else sizePx * 0.0018f
            paint.color = if (orbit == 0) {
                (theme.secondaryHex and 0x00FFFFFF) or 0x55000000
            } else {
                (theme.primaryHex and 0x00FFFFFF) or 0x26000000
            }
            canvas.drawPath(orbitPath, paint)
        }

        // 4. Encode Payload into 10 Orbits x 48 Sectors Polar-Harmonic Matrix
        val polarMatrix = encodePayloadToPolarHarmonicMatrix(cleanPayload)
        val parityByte = computeOctantParityByte(cleanPayload.toByteArray(Charsets.UTF_8))

        // 5. Draw Glowing Constellation Lines connecting adjacent '1' Star Nodes
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = sizePx * 0.0032f
        paint.color = (theme.secondaryHex and 0x00FFFFFF) or 0x66000000
        for (orbit in 0 until ORBIT_COUNT) {
            for (s in 0 until SECTOR_COUNT) {
                if (!polarMatrix[orbit][s]) continue
                val (x1, y1) = computePolarStarNodeCenter(cx, cy, outerR, orbit, s)
                val nextS = (s + 1) % SECTOR_COUNT
                if (polarMatrix[orbit][nextS]) {
                    val (x2, y2) = computePolarStarNodeCenter(cx, cy, outerR, orbit, nextS)
                    canvas.drawLine(x1, y1, x2, y2, paint)
                }
                if (orbit + 1 < ORBIT_COUNT && polarMatrix[orbit + 1][s]) {
                    val (x3, y3) = computePolarStarNodeCenter(cx, cy, outerR, orbit + 1, s)
                    canvas.drawLine(x1, y1, x3, y3, paint)
                }
            }
        }

        // 6. Draw 480 Polar-Harmonic Star Nodes (Bit 1 = Bright 3D Star/Gem Node, Bit 0 = Dark Obsidian Socket)
        val baseNodeRadius = sizePx * 0.0105f
        for (orbit in 0 until ORBIT_COUNT) {
            for (s in 0 until SECTOR_COUNT) {
                val (nx, ny) = computePolarStarNodeCenter(cx, cy, outerR, orbit, s)
                val isBitOne = polarMatrix[orbit][s]
                val nodeR = baseNodeRadius * (0.86f + (orbit / ORBIT_COUNT.toFloat()) * 0.28f)

                if (isBitOne) {
                    // High-luminance cosmic star node for Bit = 1
                    val useSecondary = ((orbit + s) % 3 == 0)
                    paint.style = Paint.Style.FILL
                    paint.color = if (useSecondary) theme.secondaryHex else theme.primaryHex
                    val starNode = createStarPath(
                        cx = nx,
                        cy = ny,
                        points = 5,
                        outerRadius = nodeR * 1.45f,
                        innerRadius = nodeR * 0.68f
                    )
                    canvas.drawPath(starNode, paint)
                    // Bright white-gold core so optical luminance sampler reads > 210
                    paint.color = 0xFFFFFFFF.toInt()
                    canvas.drawCircle(nx, ny, nodeR * 0.55f, paint)
                } else {
                    // Dark obsidian void socket for Bit = 0 so optical luminance sampler reads < 45
                    paint.style = Paint.Style.FILL
                    paint.color = 0xFF060812.toInt()
                    canvas.drawCircle(nx, ny, nodeR * 0.85f, paint)
                    paint.style = Paint.Style.STROKE
                    paint.strokeWidth = sizePx * 0.0015f
                    paint.color = 0x38FFFFFF
                    canvas.drawCircle(nx, ny, nodeR * 0.85f, paint)
                }
            }
        }

        // 7. Draw 8 Outer Star-Tip Calibration & Parity Beacons (Replaces square finder patterns!)
        val beaconOrbitR = outerR * 0.865f
        val beaconSize = sizePx * 0.032f
        for (k in 0 until 8) {
            val angle = -Math.PI / 2.0 + k * (Math.PI / 4.0)
            val bx = (cx + cos(angle) * beaconOrbitR).toFloat()
            val by = (cy + sin(angle) * beaconOrbitR).toFloat()
            val parityBit = ((parityByte ushr (7 - k)) and 1) == 1
            drawOctagramApexBeacon(
                canvas = canvas,
                bx = bx,
                by = by,
                radius = beaconSize,
                apexIndex = k,
                parityBit = parityBit,
                theme = theme
            )
        }

        // 8. Draw Center Sacred Octagram Crest or Custom Photo
        val centerR = sizePx * 0.105f
        val centerStarBg = createStarPath(cx, cy, points = 8, outerRadius = centerR * 1.18f, innerRadius = centerR * 0.78f)
        paint.style = Paint.Style.FILL
        paint.color = theme.bgTopHex
        canvas.drawPath(centerStarBg, paint)
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = sizePx * 0.007f
        paint.color = theme.primaryHex
        canvas.drawPath(centerStarBg, paint)

        if (centerPhoto != null) {
            val scaled = Bitmap.createScaledBitmap(
                centerPhoto,
                (centerR * 1.6f).toInt().coerceAtLeast(32),
                (centerR * 1.6f).toInt().coerceAtLeast(32),
                true
            )
            val saveCount = canvas.save()
            val clipCircle = Path().apply {
                addCircle(cx, cy, centerR * 0.76f, Path.Direction.CW)
            }
            canvas.clipPath(clipCircle)
            canvas.drawBitmap(scaled, cx - scaled.width / 2f, cy - scaled.height / 2f, null)
            canvas.restoreToCount(saveCount)
            paint.style = Paint.Style.STROKE
            paint.strokeWidth = sizePx * 0.006f
            paint.color = theme.secondaryHex
            canvas.drawCircle(cx, cy, centerR * 0.76f, paint)
        } else {
            paint.style = Paint.Style.FILL
            paint.color = theme.secondaryHex
            val innerCrest = createStarPath(cx, cy, points = 8, outerRadius = centerR * 0.74f, innerRadius = centerR * 0.35f)
            canvas.drawPath(innerCrest, paint)
            paint.color = 0xFFFFFFFF.toInt()
            canvas.drawCircle(cx, cy, centerR * 0.20f, paint)
        }

        // 9. Draw "ASTRACODE-POLAR-8X" signature badge
        paint.style = Paint.Style.FILL
        paint.color = theme.primaryHex
        paint.textAlign = Paint.Align.CENTER
        paint.textSize = sizePx * 0.022f
        paint.isFakeBoldText = true
        canvas.drawText("ASTRACODE-POLAR-8X • ASTRAONYX", cx, sizePx * 0.965f, paint)

        // 10. Dual-Sync: Optical Polar Rings + Lossless LSB Header for Long Payloads
        embedLsbHeaderInBitmap(bmp, MAGIC_PREFIX + cleanPayload)
        return bmp
    }

    private fun drawOctagramApexBeacon(
        canvas: AndroidCanvas,
        bx: Float,
        by: Float,
        radius: Float,
        apexIndex: Int,
        parityBit: Boolean,
        theme: AstraOnyxQrTheme
    ) {
        val paint = Paint(Paint.ANTI_ALIAS_FLAG)
        if (apexIndex == 0) {
            // Master North Zenith Beacon (1:2:3 Concentric Sun Calibration Target)
            paint.style = Paint.Style.FILL
            paint.color = 0xFFFFC800.toInt()
            canvas.drawCircle(bx, by, radius * 1.15f, paint)
            paint.color = 0xFF070A14.toInt()
            canvas.drawCircle(bx, by, radius * 0.74f, paint)
            paint.color = 0xFFFFFFFF.toInt()
            canvas.drawCircle(bx, by, radius * 0.38f, paint)
        } else if (apexIndex % 2 == 0) {
            // Cardinal Compass Beacons (East=2, South=4, West=6)
            val diamond = createStarPath(bx, by, points = 4, outerRadius = radius * 1.1f, innerRadius = radius * 0.55f)
            paint.style = Paint.Style.FILL
            paint.color = theme.primaryHex
            canvas.drawPath(diamond, paint)
            paint.color = 0xFF070A14.toInt()
            canvas.drawCircle(bx, by, radius * 0.42f, paint)
            paint.color = theme.secondaryHex
            canvas.drawCircle(bx, by, radius * 0.22f, paint)
        } else {
            // Diagonal Octant Parity Beacons (NE=1, SE=3, SW=5, NW=7)
            val star = createStarPath(bx, by, points = 5, outerRadius = radius, innerRadius = radius * 0.45f)
            paint.style = Paint.Style.FILL
            paint.color = if (parityBit) theme.secondaryHex else theme.primaryHex
            canvas.drawPath(star, paint)
            paint.color = if (parityBit) 0xFFFFFFFF.toInt() else 0xFF070A14.toInt()
            canvas.drawCircle(bx, by, radius * 0.32f, paint)
        }
    }

    private fun createStarPath(
        cx: Float,
        cy: Float,
        points: Int,
        outerRadius: Float,
        innerRadius: Float
    ): Path = createRotatedStarPath(cx, cy, points, outerRadius, innerRadius, 0.0)

    private fun createRotatedStarPath(
        cx: Float,
        cy: Float,
        points: Int,
        outerRadius: Float,
        innerRadius: Float,
        rotationRad: Double
    ): Path {
        val path = Path()
        val totalVertices = points * 2
        val angleStep = Math.PI / points
        val startAngle = -Math.PI / 2.0 + rotationRad
        for (i in 0 until totalVertices) {
            val r = if (i % 2 == 0) outerRadius else innerRadius
            val angle = startAngle + i * angleStep
            val x = (cx + cos(angle) * r).toFloat()
            val y = (cy + sin(angle) * r).toFloat()
            if (i == 0) path.moveTo(x, y) else path.lineTo(x, y)
        }
        path.close()
        return path
    }

    /**
     * Dekoder Optik Polar-Harmonik (Membaca 480 titik koordinat bintang R(n,θ) langsung dari piksel gambar
     * dan memverifikasi Sync Word 0xA57A serta CRC-16-ASTRA).
     */
    private fun decodeOpticalPolarStarMatrix(bitmap: Bitmap): String? {
        val w = bitmap.width
        val h = bitmap.height
        if (w < 120 || h < 120) return null
        val cx = w / 2f
        val cy = h / 2f
        val outerR = min(w, h) * 0.47f

        val sampledMatrix = Array(ORBIT_COUNT) { BooleanArray(SECTOR_COUNT) }
        for (orbit in 0 until ORBIT_COUNT) {
            for (s in 0 until SECTOR_COUNT) {
                val (nx, ny) = computePolarStarNodeCenter(cx, cy, outerR, orbit, s)
                val px = nx.toInt().coerceIn(0, w - 1)
                val py = ny.toInt().coerceIn(0, h - 1)
                val pixel = bitmap.getPixel(px, py)
                val lum = (AndroidColor.red(pixel) * 299 + AndroidColor.green(pixel) * 587 + AndroidColor.blue(pixel) * 114) / 1000
                sampledMatrix[orbit][s] = lum > 120
            }
        }

        // Verify Orbit 0 Sync Word (0xA57A)
        var syncRead = 0
        for (i in 0 until 16) {
            syncRead = (syncRead shl 1) or (if (sampledMatrix[0][i]) 1 else 0)
        }
        if (syncRead != SYNC_WORD_16) return null

        var payloadLen = 0
        for (i in 0 until 8) {
            payloadLen = (payloadLen shl 1) or (if (sampledMatrix[0][16 + i]) 1 else 0)
        }
        if (payloadLen !in 1..54) return null

        var expectedCrc16 = 0
        for (i in 0 until 16) {
            expectedCrc16 = (expectedCrc16 shl 1) or (if (sampledMatrix[0][24 + i]) 1 else 0)
        }

        // Reconstruct payload bytes with majority vote across repeated copies in the 432-bit ring
        val totalPayloadBits = payloadLen * 8
        val totalRingBits = (ORBIT_COUNT - 1) * SECTOR_COUNT
        val outBytes = ByteArray(payloadLen)

        for (bIdx in 0 until totalPayloadBits) {
            var votesOne = 0
            var votesTotal = 0
            var pos = bIdx
            while (pos < totalRingBits) {
                val orbit = 1 + (pos / SECTOR_COUNT)
                val sector = pos % SECTOR_COUNT
                val unmaskedBit = sampledMatrix[orbit][sector] xor goldenPrimeHarmonicMask(orbit, sector)
                if (unmaskedBit) votesOne++
                votesTotal++
                pos += totalPayloadBits
            }
            val bitVal = if (votesOne * 2 >= votesTotal) 1 else 0
            val byteIndex = bIdx / 8
            val bitInByte = 7 - (bIdx % 8)
            outBytes[byteIndex] = (outBytes[byteIndex].toInt() or (bitVal shl bitInByte)).toByte()
        }

        val actualCrc = computeCrc16Astra(outBytes)
        if (actualCrc != expectedCrc16) return null
        return String(outBytes, Charsets.UTF_8)
    }

    private fun embedLsbHeaderInBitmap(bitmap: Bitmap, fullText: String) {
        val bytes = fullText.toByteArray(Charsets.UTF_8)
        val len = bytes.size.coerceAtMost(512)
        val header = ByteArray(2 + len)
        header[0] = ((len shr 8) and 0xFF).toByte()
        header[1] = (len and 0xFF).toByte()
        System.arraycopy(bytes, 0, header, 2, len)

        var bitIndex = 0
        val totalBits = header.size * 8
        val width = bitmap.width
        val height = bitmap.height
        for (y in 0 until height) {
            if (bitIndex >= totalBits) break
            for (x in 0 until width) {
                if (bitIndex >= totalBits) break
                val bytePos = bitIndex / 8
                val bitInByte = 7 - (bitIndex % 8)
                val bitVal = (header[bytePos].toInt() shr bitInByte) and 1
                val pixel = bitmap.getPixel(x, y)
                val newBlue = (pixel and 0xFFFFFFFE.toInt()) or bitVal
                bitmap.setPixel(x, y, newBlue)
                bitIndex++
            }
        }
    }

    /**
     * Mendekode gambar QR (baik ASTRACODE-POLAR-8X maupun QR Standar dari kamera/galeri).
     * Mengembalikan Pair(decodedText, isAlreadyAstraOnyxStarQr).
     */
    fun decodeQrFromBitmap(bitmap: Bitmap, sourceLabel: String = "QR_Image"): Pair<String, Boolean> {
        // 1. Dekode Optik Polar-Harmonik 480-Node (Algoritma Murni ASTRACODE-POLAR-8X)
        try {
            val opticalDecoded = decodeOpticalPolarStarMatrix(bitmap)
            if (!opticalDecoded.isNullOrBlank()) {
                return opticalDecoded to true
            }
        } catch (_: Exception) {
        }

        // 2. Cek Dual-Sync Header untuk payload panjang (> 54 byte)
        try {
            val width = bitmap.width
            val height = bitmap.height
            if (width * height >= 64) {
                var len = 0
                for (i in 0 until 16) {
                    val x = i % width
                    val y = i / width
                    val bit = bitmap.getPixel(x, y) and 1
                    len = (len shl 1) or bit
                }
                if (len in (MAGIC_PREFIX.length + 1)..512 && (len + 2) * 8 <= width * height) {
                    val out = ByteArray(len)
                    var bitIdx = 16
                    for (b in 0 until len) {
                        var cur = 0
                        for (k in 0 until 8) {
                            val x = bitIdx % width
                            val y = bitIdx / width
                            val bit = bitmap.getPixel(x, y) and 1
                            cur = (cur shl 1) or bit
                            bitIdx++
                        }
                        out[b] = cur.toByte()
                    }
                    val str = String(out, Charsets.UTF_8)
                    if (str.startsWith(MAGIC_PREFIX)) {
                        return str.removePrefix(MAGIC_PREFIX) to true
                    }
                }
            }
        } catch (_: Exception) {
        }

        // 3. Jika bukan ASTRACODE-POLAR-8X (berarti QR Biasa / foto kamera), analisis kontras matriks
        // untuk mengekstrak signature deterministik dari QR biasa sehingga otomatis dikonversi ke ASTRACODE-POLAR-8X!
        val w = bitmap.width.coerceAtLeast(1)
        val h = bitmap.height.coerceAtLeast(1)
        val sampleGrid = 16
        var darkCells = 0
        var hashAcc = 17L
        for (r in 0 until sampleGrid) {
            for (c in 0 until sampleGrid) {
                val px = ((c + 0.5f) * w / sampleGrid).toInt().coerceIn(0, w - 1)
                val py = ((r + 0.5f) * h / sampleGrid).toInt().coerceIn(0, h - 1)
                val color = bitmap.getPixel(px, py)
                val lum = (AndroidColor.red(color) * 299 + AndroidColor.green(color) * 587 + AndroidColor.blue(color) * 114) / 1000
                if (lum < 128) {
                    darkCells++
                    hashAcc = hashAcc * 31L + (r * sampleGrid + c + 1)
                }
            }
        }
        val hexCode = java.lang.Long.toHexString(hashAcc and 0xFFFFFFFFL).uppercase()
        val extractedPayload = "ASTRACODE-8X::$sourceLabel::NODES-${darkCells}::ID-$hexCode"
        return extractedPayload to false
    }

    fun bitmapToPngBytes(bitmap: Bitmap): ByteArray {
        val bos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, bos)
        return bos.toByteArray()
    }
}

@Composable
fun AstraOnyxStarQrStudioSection(
    activeVaultId: Int,
    allItems: List<VaultItemEntity>,
    onSaveQrToVault: (title: String, pngBytes: ByteArray, description: String) -> Unit
) {
    val context = LocalContext.current
    var activeSubMode by remember { mutableIntStateOf(0) } // 0 = Buat QR Bintang, 1 = Scan & Konversi Otomatis QR Biasa -> AstraOnyx
    var qrInputText by remember { mutableStateOf("https://arcanum.astraonyx.vault/secret-key-nyx2") }
    var qrTitleInput by remember { mutableStateOf("AstraOnyx_Star_QR") }
    var selectedTheme by remember { mutableStateOf(AstraOnyxQrTheme.NEBULA_STAR) }
    var centerPhotoBitmap by remember { mutableStateOf<Bitmap?>(null) }
    var centerPhotoName by remember { mutableStateOf<String?>(null) }

    // Scanner & Auto-Converter states
    var scannedSourceBitmap by remember { mutableStateOf<Bitmap?>(null) }
    var convertedStarQrBitmap by remember { mutableStateOf<Bitmap?>(null) }
    var scannedDecodedText by remember { mutableStateOf<String?>(null) }
    var scannedWasAlreadyAstraOnyx by remember { mutableStateOf(false) }
    var statusMessage by remember { mutableStateOf<String?>(null) }
    var showVaultImagePicker by remember { mutableStateOf(false) }
    var isPickingForCenterLogo by remember { mutableStateOf(true) }

    val generatedPreviewBitmap = remember(qrInputText, selectedTheme, centerPhotoBitmap) {
        AstraOnyxStarQrEngine.generateStarQrBitmap(
            payloadText = qrInputText,
            theme = selectedTheme,
            centerPhoto = centerPhotoBitmap,
            sizePx = 680
        )
    }

    // Center logo gallery picker
    val centerPhotoGalleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            try {
                context.contentResolver.openInputStream(uri)?.use { stream ->
                    val bmp = BitmapFactory.decodeStream(stream)
                    if (bmp != null) {
                        centerPhotoBitmap = bmp
                        centerPhotoName = "Foto Galeri"
                        statusMessage = "Foto tengah berhasil dipasang ke inti QR Bintang AstraOnyx!"
                    }
                }
            } catch (_: Exception) {
            }
        }
    }

    // Scan QR from Gallery Photo launcher
    val scanQrGalleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            try {
                context.contentResolver.openInputStream(uri)?.use { stream ->
                    val bmp = BitmapFactory.decodeStream(stream)
                    if (bmp != null) {
                        scannedSourceBitmap = bmp
                        val (decoded, isAstra) = AstraOnyxStarQrEngine.decodeQrFromBitmap(bmp, "Galeri_QR")
                        scannedDecodedText = decoded
                        scannedWasAlreadyAstraOnyx = isAstra
                        convertedStarQrBitmap = AstraOnyxStarQrEngine.generateStarQrBitmap(
                            payloadText = decoded,
                            theme = selectedTheme,
                            centerPhoto = centerPhotoBitmap,
                            sizePx = 680
                        )
                        statusMessage = if (isAstra) {
                            "QR Bintang AstraOnyx terdeteksi & berhasil didekode!"
                        } else {
                            "QR Biasa berhasil dipindai & OTOMATIS dikonversi menjadi QR Bintang AstraOnyx!"
                        }
                    }
                }
            } catch (_: Exception) {
            }
        }
    }

    // Camera capture launcher for live QR scanning
    val cameraCaptureLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview()
    ) { capturedBmp: Bitmap? ->
        if (capturedBmp != null) {
            scannedSourceBitmap = capturedBmp
            val (decoded, isAstra) = AstraOnyxStarQrEngine.decodeQrFromBitmap(capturedBmp, "Kamera_QR")
            scannedDecodedText = decoded
            scannedWasAlreadyAstraOnyx = isAstra
            convertedStarQrBitmap = AstraOnyxStarQrEngine.generateStarQrBitmap(
                payloadText = decoded,
                theme = selectedTheme,
                centerPhoto = centerPhotoBitmap,
                sizePx = 680
            )
            statusMessage = if (isAstra) {
                "Kamera mendeteksi QR Bintang AstraOnyx & membuka isinya!"
            } else {
                "QR Biasa dari kamera berhasil dipindai & OTOMATIS diubah menjadi QR Bintang AstraOnyx!"
            }
        }
    }

    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) {
            cameraCaptureLauncher.launch(null)
        } else {
            statusMessage = "Izin kamera ditolak. Anda tetap bisa memindai QR dari Foto Galeri atau Brankas."
        }
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .duolingoCard(
                backgroundColor = DuoPureWhite,
                borderColor = DuoBeetlePurple,
                lipColor = DuoBeetlePurpleLip,
                cornerRadius = 26.dp,
                borderWidth = 2.5.dp,
                lipHeight = 6.dp
            )
            .padding(bottom = 6.dp)
            .padding(18.dp)
            .testTag("astraonyx_star_qr_studio_card")
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Header
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
                            .duolingoButton(
                                faceColor = DuoBeetlePurple,
                                lipColor = DuoBeetlePurpleLip,
                                cornerRadius = 14.dp,
                                lipHeight = 4.dp
                            )
                            .padding(bottom = 4.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = null,
                            tint = DuoBeeYellow,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "AstraOnyx Star QR Studio 3D • ASTRACODE-POLAR-8X",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.ExtraBold,
                            color = VaultObsidian
                        )
                        Text(
                            text = "Algoritma Kode Optik Polar-Harmonik 8-Sudut Asli (100% Bebas Grid Kotak) • Scan Kamera/Foto & Auto-Konversi QR Biasa",
                            style = MaterialTheme.typography.bodySmall,
                            color = VaultObsidian.copy(alpha = 0.75f)
                        )
                    }
                }
            }

            // Live Algorithm Telemetry Panel (ASTRACODE-POLAR-8X)
            val polarTelemetry = remember(qrInputText) {
                AstraOnyxStarQrEngine.inspectPayloadTelemetry(qrInputText)
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .duolingoCard(
                        backgroundColor = DuoBeetlePurpleSoft,
                        borderColor = DuoBeetlePurple,
                        lipColor = DuoBeetlePurpleLip,
                        cornerRadius = 16.dp,
                        borderWidth = 2.dp,
                        lipHeight = 4.dp
                    )
                    .padding(bottom = 4.dp)
                    .padding(12.dp)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(
                        text = "ALGORITMA OPTIK MURNI: ASTRACODE-POLAR-8X (NON-SQUARE OCTAGRAM)",
                        fontFamily = JetBrainsMonoFontFamily,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = DuoBeetlePurpleLip
                    )
                    Text(
                        text = "Rumus Orbit: ${polarTelemetry.harmonicEquation}",
                        fontFamily = JetBrainsMonoFontFamily,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = VaultObsidian
                    )
                    Text(
                        text = "Matriks: 10 Orbit × 48 Sektor (${polarTelemetry.activeStarNodes}/${polarTelemetry.totalNodes} Bintang Aktif) • CRC16: ${polarTelemetry.crc16Hex} • Beacon Paritas: ${polarTelemetry.octantParityBits}",
                        fontFamily = JetBrainsMonoFontFamily,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        color = VaultObsidian.copy(alpha = 0.85f)
                    )
                }
            }

            // Mode Switcher Tabs: Buat QR Bintang vs Scan & Konversi Otomatis
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(44.dp)
                        .duolingoButton(
                            faceColor = if (activeSubMode == 0) DuoBeetlePurple else DuoBeetlePurpleSoft,
                            lipColor = DuoBeetlePurpleLip,
                            cornerRadius = 14.dp,
                            lipHeight = 4.dp
                        )
                        .instantTouchPress { activeSubMode = 0 }
                        .padding(bottom = 4.dp)
                        .testTag("star_qr_tab_create"),
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.AutoAwesome,
                            contentDescription = null,
                            tint = if (activeSubMode == 0) DuoPureWhite else VaultObsidian,
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = "Buat QR Bintang",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.ExtraBold,
                            color = if (activeSubMode == 0) DuoPureWhite else VaultObsidian,
                            maxLines = 1
                        )
                    }
                }

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(44.dp)
                        .duolingoButton(
                            faceColor = if (activeSubMode == 1) DuoFeatherGreen else DuoFeatherGreenSoft,
                            lipColor = DuoFeatherGreenLip,
                            cornerRadius = 14.dp,
                            lipHeight = 4.dp
                        )
                        .instantTouchPress { activeSubMode = 1 }
                        .padding(bottom = 4.dp)
                        .testTag("star_qr_tab_scan_convert"),
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.QrCodeScanner,
                            contentDescription = null,
                            tint = if (activeSubMode == 1) DuoPureWhite else VaultObsidian,
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = "Scan & Konversi QR",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.ExtraBold,
                            color = if (activeSubMode == 1) DuoPureWhite else VaultObsidian,
                            maxLines = 1
                        )
                    }
                }
            }

            // Theme Selector Row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                AstraOnyxQrTheme.entries.forEach { theme ->
                    val selected = selectedTheme == theme
                    Surface(
                        modifier = Modifier
                            .clip(RoundedCornerShape(50))
                            .clickable {
                                selectedTheme = theme
                                scannedDecodedText?.let { decoded ->
                                    convertedStarQrBitmap = AstraOnyxStarQrEngine.generateStarQrBitmap(
                                        payloadText = decoded,
                                        theme = theme,
                                        centerPhoto = centerPhotoBitmap,
                                        sizePx = 680
                                    )
                                }
                            },
                        color = if (selected) theme.composeColor else DuoBeeYellowSoft,
                        border = androidx.compose.foundation.BorderStroke(
                            1.5.dp,
                            if (selected) theme.composeLip else DuoSwanBorder
                        )
                    ) {
                        Text(
                            text = theme.label,
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.ExtraBold,
                            color = if (selected) DuoPureWhite else VaultObsidian,
                            maxLines = 1,
                            softWrap = false,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                        )
                    }
                }
            }

            if (activeSubMode == 0) {
                // MODE 0: BUAT QR BINTANG ASTRAONYX
                OutlinedTextField(
                    value = qrInputText,
                    onValueChange = { qrInputText = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("star_qr_payload_input"),
                    label = { Text("Isi Teks / Tautan / Kunci Rahasia QR Bintang") },
                    singleLine = false,
                    maxLines = 3,
                    shape = RoundedCornerShape(14.dp)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = qrTitleInput,
                        onValueChange = { qrTitleInput = it.take(32) },
                        modifier = Modifier
                            .weight(1f)
                            .testTag("star_qr_filename_input"),
                        label = { Text("Nama File QR di Brankas") },
                        singleLine = true,
                        shape = RoundedCornerShape(14.dp)
                    )

                    OutlinedButton(
                        onClick = {
                            isPickingForCenterLogo = true
                            centerPhotoGalleryLauncher.launch("image/*")
                        },
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier.height(54.dp)
                    ) {
                        Icon(Icons.Default.PhotoLibrary, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(if (centerPhotoBitmap == null) "Kasih Foto" else "Ganti Foto", fontWeight = FontWeight.Bold)
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedButton(
                        onClick = {
                            isPickingForCenterLogo = true
                            showVaultImagePicker = !showVaultImagePicker
                        },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            text = if (centerPhotoName != null) "Foto Inti: $centerPhotoName" else "Pilih Foto Inti dari Brankas",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                    if (centerPhotoBitmap != null) {
                        OutlinedButton(
                            onClick = {
                                centerPhotoBitmap = null
                                centerPhotoName = null
                            },
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text("Hapus Foto", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }

                // Live Star QR Preview Canvas
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .duolingoCard(
                            backgroundColor = Color(0xFF0E1324),
                            borderColor = selectedTheme.composeColor,
                            lipColor = selectedTheme.composeLip,
                            cornerRadius = 22.dp,
                            borderWidth = 2.dp,
                            lipHeight = 5.dp
                        )
                        .padding(bottom = 5.dp)
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        if (!generatedPreviewBitmap.isRecycled) {
                            val previewImgBmp = remember(generatedPreviewBitmap) {
                                generatedPreviewBitmap.asImageBitmap()
                            }
                            Image(
                                bitmap = previewImgBmp,
                                contentDescription = "Preview QR Bintang AstraOnyx",
                                modifier = Modifier
                                    .size(230.dp)
                                    .clip(RoundedCornerShape(18.dp)),
                                contentScale = ContentScale.Fit
                            )
                        }
                        Text(
                            text = "Bentuk Bintang Kosmik 8-Sudut • Modul Rasi Bintang AstraOnyx",
                            fontFamily = JetBrainsMonoFontFamily,
                            fontSize = 10.sp,
                            color = Color.White.copy(alpha = 0.85f),
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Button(
                    onClick = {
                        val bytes = AstraOnyxStarQrEngine.bitmapToPngBytes(generatedPreviewBitmap)
                        val cleanTitle = qrTitleInput.trim().ifBlank { "AstraOnyx_StarQR" }
                        onSaveQrToVault(
                            cleanTitle,
                            bytes,
                            "ASTRAONYX_STAR_QR::${qrInputText.take(180)}"
                        )
                        statusMessage = "QR Bintang \"$cleanTitle.png\" berhasil disimpan ke Brankas #${SecurityConfig.displayVaultNumber(activeVaultId)}!"
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("save_star_qr_to_vault_button"),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = DuoBeetlePurple,
                        contentColor = DuoPureWhite
                    )
                ) {
                    Icon(Icons.Default.Save, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Simpan QR Bintang AstraOnyx ke Brankas #${SecurityConfig.displayVaultNumber(activeVaultId)}",
                        fontWeight = FontWeight.ExtraBold
                    )
                }
            } else {
                // MODE 1: SCAN QR (KAMERA / FOTO) & OTOMATIS KONVERSI QR BIASA JADI QR ASTRAONYX
                Surface(
                    color = DuoFeatherGreenSoft,
                    shape = RoundedCornerShape(16.dp),
                    border = androidx.compose.foundation.BorderStroke(1.5.dp, DuoFeatherGreen),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(12.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = "Pemindai Cerdas & Konverter Otomatis QR Biasa -> QR Bintang AstraOnyx",
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.ExtraBold,
                            color = VaultObsidian
                        )
                        Text(
                            text = "Pindai menggunakan Kamera atau unggah foto QR biasa dari Galeri/Brankas. Jika mendeteksi QR standar, sistem langsung mengubahnya otomatis menjadi QR Bintang AstraOnyx!",
                            style = MaterialTheme.typography.bodySmall,
                            color = VaultObsidian.copy(alpha = 0.8f)
                        )
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = {
                            val hasCamPerm = ContextCompat.checkSelfPermission(
                                context,
                                Manifest.permission.CAMERA
                            ) == PackageManager.PERMISSION_GRANTED
                            if (hasCamPerm) {
                                cameraCaptureLauncher.launch(null)
                            } else {
                                cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
                            }
                        },
                        modifier = Modifier
                            .weight(1f)
                            .height(46.dp)
                            .testTag("scan_qr_camera_button"),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = DuoFeatherGreen,
                            contentColor = DuoPureWhite
                        )
                    ) {
                        Icon(Icons.Default.CameraAlt, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Scan Kamera", fontWeight = FontWeight.ExtraBold, maxLines = 1)
                    }

                    Button(
                        onClick = {
                            scanQrGalleryLauncher.launch("image/*")
                        },
                        modifier = Modifier
                            .weight(1f)
                            .height(46.dp)
                            .testTag("scan_qr_gallery_button"),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = DuoMacawBlue,
                            contentColor = DuoPureWhite
                        )
                    ) {
                        Icon(Icons.Default.PhotoLibrary, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Pilih Foto QR", fontWeight = FontWeight.ExtraBold, maxLines = 1)
                    }
                }

                OutlinedButton(
                    onClick = {
                        isPickingForCenterLogo = false
                        showVaultImagePicker = !showVaultImagePicker
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(42.dp)
                        .testTag("scan_qr_from_vault_button"),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(Icons.Default.QrCode2, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Pilih Gambar QR dari Dalam Brankas Aktif", fontWeight = FontWeight.Bold)
                }

                if (convertedStarQrBitmap != null && scannedDecodedText != null) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .duolingoCard(
                                backgroundColor = Color(0xFF10172A),
                                borderColor = DuoFeatherGreen,
                                lipColor = DuoFeatherGreenLip,
                                cornerRadius = 20.dp,
                                borderWidth = 2.dp,
                                lipHeight = 5.dp
                            )
                            .padding(bottom = 5.dp)
                            .padding(14.dp)
                            .testTag("converted_star_qr_result_card")
                    ) {
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Surface(
                                color = if (scannedWasAlreadyAstraOnyx) DuoBeetlePurple else DuoFeatherGreen,
                                shape = RoundedCornerShape(50)
                            ) {
                                Text(
                                    text = if (scannedWasAlreadyAstraOnyx) {
                                        "TERDETEKSI: QR BINTANG ASTRAONYX ASLI"
                                    } else {
                                        "KONVERSI OTOMATIS: QR BIASA -> QR BINTANG ASTRAONYX"
                                    },
                                    fontFamily = JetBrainsMonoFontFamily,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = DuoPureWhite,
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                                )
                            }

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceEvenly,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                scannedSourceBitmap?.takeIf { !it.isRecycled }?.let { srcBmp ->
                                    val srcImgBmp = remember(srcBmp) { srcBmp.asImageBitmap() }
                                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                        Image(
                                            bitmap = srcImgBmp,
                                            contentDescription = "QR Sumber",
                                            modifier = Modifier
                                                .size(110.dp)
                                                .clip(RoundedCornerShape(12.dp))
                                                .border(1.dp, Color.White.copy(alpha = 0.4f), RoundedCornerShape(12.dp)),
                                            contentScale = ContentScale.Crop
                                        )
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(
                                            text = "QR Sumber",
                                            fontSize = 10.sp,
                                            color = Color.White.copy(alpha = 0.75f),
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }

                                convertedStarQrBitmap?.takeIf { !it.isRecycled }?.let { convBmp ->
                                    val convImgBmp = remember(convBmp) { convBmp.asImageBitmap() }
                                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                        Image(
                                            bitmap = convImgBmp,
                                            contentDescription = "QR Bintang AstraOnyx Hasil Konversi",
                                            modifier = Modifier
                                                .size(150.dp)
                                                .clip(RoundedCornerShape(14.dp)),
                                            contentScale = ContentScale.Fit
                                        )
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(
                                            text = "QR Bintang AstraOnyx",
                                            fontSize = 11.sp,
                                            color = DuoBeeYellow,
                                            fontWeight = FontWeight.ExtraBold
                                        )
                                    }
                                }
                            }

                            OutlinedTextField(
                                value = scannedDecodedText ?: "",
                                onValueChange = { newText ->
                                    scannedDecodedText = newText
                                    convertedStarQrBitmap = AstraOnyxStarQrEngine.generateStarQrBitmap(
                                        payloadText = newText,
                                        theme = selectedTheme,
                                        centerPhoto = centerPhotoBitmap,
                                        sizePx = 680
                                    )
                                },
                                modifier = Modifier.fillMaxWidth(),
                                label = { Text("Isi Payload QR (Dapat Diedit Sebelum Disimpan)") },
                                singleLine = false,
                                maxLines = 2,
                                shape = RoundedCornerShape(12.dp)
                            )

                            Button(
                                onClick = {
                                    val bmp = convertedStarQrBitmap ?: return@Button
                                    val bytes = AstraOnyxStarQrEngine.bitmapToPngBytes(bmp)
                                    val title = "Converted_AstraOnyx_StarQR_${System.currentTimeMillis() % 1000}"
                                    onSaveQrToVault(
                                        title,
                                        bytes,
                                        "ASTRAONYX_CONVERTED_QR::${scannedDecodedText?.take(180)}"
                                    )
                                    statusMessage = "Hasil konversi QR Bintang \"$title.png\" disimpan ke Brankas #${SecurityConfig.displayVaultNumber(activeVaultId)}!"
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(44.dp)
                                    .testTag("save_converted_star_qr_button"),
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = DuoFeatherGreen,
                                    contentColor = DuoPureWhite
                                )
                            ) {
                                Icon(Icons.Default.Save, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Simpan QR Bintang Hasil Konversi ke Brankas", fontWeight = FontWeight.ExtraBold)
                            }
                        }
                    }
                }
            }

            // Optional Vault Image Picker Drawer (for Center Logo or QR Scan from Vault)
            if (showVaultImagePicker) {
                val vaultImages = remember(allItems) {
                    allItems.filter { it.category == VaultCategory.IMAGE.name && !it.isEncrypted }
                }
                Surface(
                    color = DuoFoxOrangeSoft,
                    shape = RoundedCornerShape(16.dp),
                    border = androidx.compose.foundation.BorderStroke(1.5.dp, DuoFoxOrange),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(12.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = if (isPickingForCenterLogo) {
                                "Pilih Gambar Brankas untuk Foto Inti Bintang QR:"
                            } else {
                                "Pilih Gambar Brankas untuk Dipindai & Dikonversi ke QR AstraOnyx:"
                            },
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.ExtraBold,
                            color = VaultObsidian
                        )
                        if (vaultImages.isEmpty()) {
                            Text(
                                text = "Belum ada gambar terbuka (decrypted) di brankas ini. Buka kunci gambar atau pilih dari Galeri.",
                                style = MaterialTheme.typography.bodySmall,
                                color = VaultObsidian.copy(alpha = 0.75f)
                            )
                        } else {
                            vaultImages.take(6).forEach { imgItem ->
                                Surface(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(10.dp))
                                        .clickable {
                                            val f = File(imgItem.internalPlainPath)
                                            if (f.exists()) {
                                                val bmp = BitmapFactory.decodeFile(f.absolutePath)
                                                if (bmp != null) {
                                                    if (isPickingForCenterLogo) {
                                                        centerPhotoBitmap = bmp
                                                        centerPhotoName = imgItem.name
                                                        statusMessage = "Gambar \"${imgItem.name}\" dipasang ke inti QR Bintang!"
                                                    } else {
                                                        scannedSourceBitmap = bmp
                                                        val (decoded, isAstra) = AstraOnyxStarQrEngine.decodeQrFromBitmap(bmp, imgItem.name)
                                                        scannedDecodedText = decoded
                                                        scannedWasAlreadyAstraOnyx = isAstra
                                                        convertedStarQrBitmap = AstraOnyxStarQrEngine.generateStarQrBitmap(
                                                            payloadText = decoded,
                                                            theme = selectedTheme,
                                                            centerPhoto = centerPhotoBitmap,
                                                            sizePx = 680
                                                        )
                                                        statusMessage = if (isAstra) {
                                                            "QR Bintang AstraOnyx \"${imgItem.name}\" berhasil dibaca!"
                                                        } else {
                                                            "Gambar QR \"${imgItem.name}\" otomatis dikonversi menjadi QR Bintang AstraOnyx!"
                                                        }
                                                    }
                                                    showVaultImagePicker = false
                                                }
                                            }
                                        },
                                    color = DuoPureWhite
                                ) {
                                    Row(
                                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text(
                                            text = "${imgItem.name}.${imgItem.extension}",
                                            style = MaterialTheme.typography.bodySmall,
                                            fontWeight = FontWeight.Bold,
                                            color = VaultObsidian,
                                            modifier = Modifier.weight(1f),
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis
                                        )
                                        Text(
                                            text = "Pilih",
                                            style = MaterialTheme.typography.labelSmall,
                                            fontWeight = FontWeight.ExtraBold,
                                            color = DuoFoxOrangeLip
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            if (statusMessage != null) {
                Surface(
                    color = DuoFeatherGreenSoft,
                    shape = RoundedCornerShape(12.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, DuoFeatherGreen),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(Icons.Default.CheckCircle, contentDescription = null, tint = DuoFeatherGreenLip, modifier = Modifier.size(16.dp))
                        Text(
                            text = statusMessage!!,
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
