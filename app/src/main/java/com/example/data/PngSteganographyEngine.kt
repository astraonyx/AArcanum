package com.example.data

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.Shader
import android.util.Base64
import java.io.ByteArrayOutputStream
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.math.max
import kotlin.math.min

enum class StegoCoverTheme(
    val title: String,
    val subtitle: String,
    val primaryColorHex: Int,
    val secondaryColorHex: Int,
    val accentColorHex: Int
) {
    COSMIC_POSTCARD(
        title = "Aurora Galaksi",
        subtitle = "Ilustrasi Langit Malam & Bintang",
        primaryColorHex = 0xFF0F172A.toInt(),
        secondaryColorHex = 0xFF311042.toInt(),
        accentColorHex = 0xFF38BDF8.toInt()
    ),
    BOTANICAL_ART(
        title = "Taman Emerald",
        subtitle = "Poster Tanaman Minimalis",
        primaryColorHex = 0xFF064E3B.toInt(),
        secondaryColorHex = 0xFF047857.toInt(),
        accentColorHex = 0xFFA7F3D0.toInt()
    ),
    GEOMETRIC_MINIMAL(
        title = "Geometri Senja",
        subtitle = "Seni Abstrak Oranye & Ungu",
        primaryColorHex = 0xFF4C1D95.toInt(),
        secondaryColorHex = 0xFF9D174D.toInt(),
        accentColorHex = 0xFFFBBF24.toInt()
    ),
    DAILY_RECEIPT_NOTE(
        title = "Kartu Catatan Harian",
        subtitle = "Tampak Seperti Gambar Memo Biasa",
        primaryColorHex = 0xFF1E293B.toInt(),
        secondaryColorHex = 0xFF334155.toInt(),
        accentColorHex = 0xFF58CC02.toInt()
    )
}

data class StegoStealthAnalysis(
    val stealthScorePercent: Int,
    val carrierBaseBytes: Long,
    val payloadBytes: Int,
    val estimatedTotalBytes: Long,
    val overheadPercent: Float,
    val visualDiffPercent: Float,
    val statusLabel: String,
    val carrierModeDescription: String
)

data class PixelLoupeSample(
    val regionLabel: String,
    val coordX: Int,
    val coordY: Int,
    val originalHex: String,
    val stegoHex: String,
    val lsbBitSignature: String,
    val gridColorsOriginal: List<Int>,
    val gridColorsXRay: List<Int>,
    val deltaE: Float = 0.00f
)

data class DecodedStegoPayload(
    val isStegoCarrier: Boolean,
    val isPasscodeLocked: Boolean = false,
    val isPasscodeValid: Boolean = true,
    val coverThemeName: String = "",
    val carrierSourceLabel: String = "",
    val isQrKeyCard: Boolean = false,
    val isDualPhotoFusion: Boolean = false,
    val isWavAudioCarrier: Boolean = false,
    val wavPresetName: String = "",
    val secretTitle: String = "",
    val secretMessage: String = "",
    val embeddedFileName: String? = null,
    val embeddedFileExtension: String? = null,
    val embeddedFileBytes: ByteArray? = null,
    val createdAtLabel: String = "",
    val payloadSizeBytes: Int = 0,
    val checksumHex: String = ""
)

object PngSteganographyEngine {

    private const val STEGO_MAGIC_HEADER = "\n%%NYX2_STEGO_PNG_V1_START%%\n"
    private const val STEGO_MAGIC_FOOTER = "\n%%NYX2_STEGO_PNG_V1_END%%"

    private val IMAGE_EXTENSIONS = setOf("png", "jpg", "jpeg", "webp")

    fun isImageExtension(ext: String?): Boolean {
        return ext?.trim()?.lowercase() in IMAGE_EXTENSIONS
    }

    /**
     * Menghitung analisis kapasitas & tingkat kamuflase (Stego Stealth Analyzer) secara real-time
     * untuk membandingkan kondisi foto asli vs hasil penyisipan pesan steganografi / foto kedua.
     */
    fun analyzeStegoStealth(
        carrierItem: VaultItemEntity?,
        secretTitle: String,
        secretMessage: String,
        attachedItem: VaultItemEntity?,
        isQrKeyCardMode: Boolean = false
    ): StegoStealthAnalysis {
        val baseBytes = when {
            isQrKeyCardMode -> 38_400L
            carrierItem != null && carrierItem.sizeBytes > 0L -> max(carrierItem.sizeBytes, 24_000L)
            else -> 42_500L
        }
        val textRawBytes = (secretTitle.toByteArray(Charsets.UTF_8).size +
            secretMessage.toByteArray(Charsets.UTF_8).size)
        val attachedRawBytes = attachedItem?.sizeBytes?.toInt()?.coerceAtMost(2 * 1024 * 1024) ?: 0
        // Base64 expansion (~1.34x) + metadata header/footer (~240 bytes)
        val totalPayloadBytes = ((textRawBytes + attachedRawBytes) * 4 / 3) + 240
        val estimatedTotal = baseBytes + totalPayloadBytes.toLong()
        val overheadRatio = (totalPayloadBytes.toFloat() / baseBytes.toFloat()) * 100f
        val roundedOverhead = (overheadRatio * 100f).toInt() / 100f

        val stealthScore = when {
            overheadRatio <= 3f -> 99
            overheadRatio <= 10f -> 97
            overheadRatio <= 25f -> 94
            overheadRatio <= 60f -> 89
            else -> 84
        }

        val isDualPhoto = attachedItem != null &&
            (attachedItem.category == VaultCategory.IMAGE.name || isImageExtension(attachedItem.extension))

        val status = when {
            isDualPhoto -> "DUAL-PHOTO FUSION • 2 Foto di 1 File • 0.00% Distorsi"
            stealthScore >= 97 -> "SANGAT SILUMAN • 0.00% Distorsi Piksel"
            stealthScore >= 90 -> "AMAN & STABIL • Piksel Visual Identik"
            else -> "MUATAN BESAR • Tetap Valid di Galeri"
        }

        val carrierDesc = when {
            isQrKeyCardMode -> "Kartu Stego-QR Offline (480x320)"
            carrierItem != null && isDualPhoto ->
                "2 Foto: ${carrierItem.name}.${carrierItem.extension} + ${attachedItem?.name}.${attachedItem?.extension}"
            carrierItem != null -> "Foto Brankas: ${carrierItem.name}.${carrierItem.extension}"
            else -> "Poster Digital Bawaan (480x320)"
        }

        return StegoStealthAnalysis(
            stealthScorePercent = stealthScore,
            carrierBaseBytes = baseBytes,
            payloadBytes = totalPayloadBytes,
            estimatedTotalBytes = estimatedTotal,
            overheadPercent = roundedOverhead,
            visualDiffPercent = 0.00f,
            statusLabel = status,
            carrierModeDescription = carrierDesc
        )
    }

    /**
     * Menghasilkan sampel kisi piksel 5x5 untuk Kaca Pembesar 8x (Loupe 8x) dan mode Pixel X-Ray
     * guna membandingkan piksel gambar Asli vs gambar Hasil Stego.
     */
    fun generatePixelLoupeSample(
        carrierBitmap: Bitmap,
        hotspotIndex: Int
    ): PixelLoupeSample {
        val w = max(carrierBitmap.width, 10)
        val h = max(carrierBitmap.height, 10)

        val (regionName, cx, cy) = when (hotspotIndex) {
            0 -> Triple("Sudut Atas-Kiri (Header IDAT)", (w * 0.16f).toInt().coerceIn(2, w - 3), (h * 0.18f).toInt().coerceIn(2, h - 3))
            1 -> Triple("Pusat Fokus Visual (Tengah)", (w * 0.50f).toInt().coerceIn(2, w - 3), (h * 0.50f).toInt().coerceIn(2, h - 3))
            else -> Triple("Zona Ekor Kontainer (Bawah-Kanan)", (w * 0.82f).toInt().coerceIn(2, w - 3), (h * 0.82f).toInt().coerceIn(2, h - 3))
        }

        val origGrid = ArrayList<Int>(25)
        val xrayGrid = ArrayList<Int>(25)

        for (dy in -2..2) {
            for (dx in -2..2) {
                val px = (cx + dx).coerceIn(0, w - 1)
                val py = (cy + dy).coerceIn(0, h - 1)
                val c = carrierBitmap.getPixel(px, py)
                origGrid.add(c)

                val r = Color.red(c)
                val g = Color.green(c)
                val b = Color.blue(c)
                // Amplifikasi Bit-Plane LSB (3 bit terbawah diperkuat ke spektrum Sian/Hijau Sinar-X)
                val xr = ((r and 0x07) shl 5).coerceIn(16, 255)
                val xg = (((g and 0x07) shl 5) xor 0x58).coerceIn(48, 255)
                val xb = (((b and 0x07) shl 5) or 0x70).coerceIn(80, 255)
                xrayGrid.add(Color.rgb(xr, xg, xb))
            }
        }

        val centerColor = origGrid[12]
        val hexStr = String.format(
            Locale.US,
            "#%02X%02X%02X",
            Color.red(centerColor),
            Color.green(centerColor),
            Color.blue(centerColor)
        )
        val rBit = Color.red(centerColor) and 1
        val gBit = Color.green(centerColor) and 1
        val bBit = Color.blue(centerColor) and 1

        return PixelLoupeSample(
            regionLabel = regionName,
            coordX = cx,
            coordY = cy,
            originalHex = hexStr,
            stegoHex = hexStr,
            lsbBitSignature = "LSB[R:$rBit G:$gBit B:$bBit] • Δ=0",
            gridColorsOriginal = origGrid,
            gridColorsXRay = xrayGrid,
            deltaE = 0.00f
        )
    }

    /**
     * Merender Bitmap pratinjau untuk item foto di Brankas (atau tema sampul) agar dapat
     * ditampilkan berdampingan pada komponen Side-by-Side Visual Comparison di UI.
     */
    fun loadPreviewBitmapForVaultItem(
        item: VaultItemEntity?,
        fallbackTheme: StegoCoverTheme,
        customCaption: String = "",
        isSecondHiddenPhotoBadge: Boolean = false
    ): Bitmap {
        if (item != null) {
            val plainFile = File(item.internalPlainPath)
            if (plainFile.exists()) {
                try {
                    val opts = BitmapFactory.Options().apply {
                        inSampleSize = 2
                        inPreferredConfig = Bitmap.Config.ARGB_8888
                    }
                    val decoded = BitmapFactory.decodeFile(plainFile.absolutePath, opts)
                    if (decoded != null) {
                        return decoded
                    }
                } catch (_: Exception) {
                    // Lanjut ke fallback visual
                }
            }
            return renderSyntheticPhotoPreviewBitmap(
                photoTitle = "${item.name}.${item.extension}",
                subtitle = if (isSecondHiddenPhotoBadge) {
                    "FOTO KEDUA (TERSEMBUNYI DI DALAM FILE)"
                } else {
                    "FOTO UTAMA BRANKAS (#${SecurityConfig.displayVaultNumber(item.vaultProfileId)})"
                },
                theme = fallbackTheme,
                isSecondPhoto = isSecondHiddenPhotoBadge
            )
        }
        return renderPosterThemeBitmap(
            coverTheme = fallbackTheme,
            visibleCaption = customCaption.ifBlank { fallbackTheme.title }
        )
    }

    private fun renderSyntheticPhotoPreviewBitmap(
        photoTitle: String,
        subtitle: String,
        theme: StegoCoverTheme,
        isSecondPhoto: Boolean
    ): Bitmap {
        val width = 360
        val height = 240
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)

        val c1 = if (isSecondPhoto) 0xFF311042.toInt() else theme.primaryColorHex
        val c2 = if (isSecondPhoto) 0xFF064E3B.toInt() else theme.secondaryColorHex
        val accent = if (isSecondPhoto) 0xFFFBBF24.toInt() else theme.accentColorHex

        val bgPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            shader = LinearGradient(
                0f, 0f, width.toFloat(), height.toFloat(),
                c1, c2, Shader.TileMode.CLAMP
            )
        }
        canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), bgPaint)

        val orbPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = accent
            alpha = 55
        }
        canvas.drawCircle(width * 0.78f, height * 0.28f, 58f, orbPaint)
        canvas.drawCircle(width * 0.22f, height * 0.76f, 44f, orbPaint)

        val borderPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = accent
            style = Paint.Style.STROKE
            strokeWidth = 2.5f
            alpha = 190
        }
        canvas.drawRoundRect(RectF(14f, 14f, width - 14f, height - 14f), 18f, 18f, borderPaint)

        val badgePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = accent
            textSize = 11f
            isFakeBoldText = true
        }
        val titlePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.WHITE
            textSize = 18f
            isFakeBoldText = true
        }
        val subPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.LTGRAY
            textSize = 12f
        }
        canvas.drawText(subtitle.take(36), 28f, 58f, badgePaint)
        canvas.drawText(photoTitle.take(22), 28f, 108f, titlePaint)
        canvas.drawText(
            if (isSecondPhoto) "Terenkripsi SHA-256 di Ekor PNG" else "Piksel RGB Utuh • 0.00% Artefak",
            28f,
            146f,
            subPaint
        )
        canvas.drawText("Arcanum Stegano Studio", 28f, 186f, subPaint)
        return bitmap
    }

    /**
     * Menghasilkan matriks QR deterministik 21x21 (lengkap dengan 3 Finder Pattern 7x7 + Timing Pattern)
     * dari seed teks rahasia agar dapat dirender di Pratinjau UI maupun di Kartu Kunci .PNG Offline.
     */
    fun generateDeterministicQrMatrix(seedText: String): Array<BooleanArray> {
        val size = 21
        val matrix = Array(size) { BooleanArray(size) }
        val reserved = Array(size) { BooleanArray(size) }

        fun placeFinderPattern(startR: Int, startC: Int) {
            for (r in 0 until 7) {
                for (c in 0 until 7) {
                    val rr = startR + r
                    val cc = startC + c
                    if (rr in 0 until size && cc in 0 until size) {
                        val isBorder = r == 0 || r == 6 || c == 0 || c == 6
                        val isInnerBox = r in 2..4 && c in 2..4
                        matrix[rr][cc] = isBorder || isInnerBox
                        reserved[rr][cc] = true
                    }
                }
            }
            for (r in -1..7) {
                for (c in -1..7) {
                    val rr = startR + r
                    val cc = startC + c
                    if (rr in 0 until size && cc in 0 until size && !reserved[rr][cc]) {
                        matrix[rr][cc] = false
                        reserved[rr][cc] = true
                    }
                }
            }
        }

        placeFinderPattern(0, 0)
        placeFinderPattern(0, size - 7)
        placeFinderPattern(size - 7, 0)

        for (i in 8 until size - 8) {
            matrix[6][i] = (i % 2 == 0)
            reserved[6][i] = true
            matrix[i][6] = (i % 2 == 0)
            reserved[i][6] = true
        }

        val digest = java.security.MessageDigest.getInstance("SHA-256")
            .digest(seedText.ifBlank { "NYX2_DEFAULT_QR_SEED" }.toByteArray(Charsets.UTF_8))

        var bitIdx = 0
        for (r in 0 until size) {
            for (c in 0 until size) {
                if (!reserved[r][c]) {
                    val byteVal = digest[(bitIdx / 8) % digest.size].toInt() and 0xFF
                    val shift = bitIdx % 8
                    val bit = ((byteVal ushr shift) xor ((r * 31 + c * 17) and 1)) and 1
                    matrix[r][c] = (bit == 1)
                    bitIdx++
                }
            }
        }
        return matrix
    }

    /**
     * Membuat file gambar .PNG valid yang dapat dibuka di semua galeri foto,
     * mendukung penyisipan ke:
     * 1) Foto Asli di Brankas (sourceCarrierImageFile),
     * 2) Dual-Photo Stego Fusion (2 Foto dalam 1 File ketika attachedFile adalah gambar),
     * 3) Kartu Kunci Stego-QR Offline (isQrKeyCardMode = true), atau
     * 4) Tema Poster Bawaan (coverTheme).
     */
    fun createStegoPngFile(
        outputFile: File,
        coverTheme: StegoCoverTheme,
        visibleCaption: String,
        secretTitle: String,
        secretMessage: String,
        passcode: String,
        attachedFile: File? = null,
        attachedDisplayName: String? = null,
        attachedExtension: String? = null,
        sourceCarrierImageFile: File? = null,
        sourceCarrierName: String? = null,
        isQrKeyCardMode: Boolean = false
    ): Long {
        val cleanPass = passcode.trim().uppercase()
        val checksum = EncryptionScriptEngine.compute3LayerKeyFingerprint(
            secretTitle.ifBlank { "STEGO" },
            secretMessage.take(32),
            attachedDisplayName ?: "MSG"
        )
        val dateLabel = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault()).format(Date())

        // 1. Siapkan Bitmap dari Foto Asli Brankas (Foto 1), Kartu QR Offline, atau Poster Bawaan
        val renderedBitmap: Bitmap = when {
            isQrKeyCardMode -> {
                renderQrKeyCardBitmap(
                    coverTheme = coverTheme,
                    cardTitle = secretTitle.ifBlank { visibleCaption },
                    checksum = checksum,
                    dateLabel = dateLabel,
                    qrSeed = "$secretTitle::$secretMessage::$cleanPass"
                )
            }
            sourceCarrierImageFile != null && sourceCarrierImageFile.exists() -> {
                decodeOrRenderCarrierPhotoBitmap(
                    sourceFile = sourceCarrierImageFile,
                    carrierName = sourceCarrierName ?: visibleCaption,
                    fallbackTheme = coverTheme
                )
            }
            else -> {
                renderPosterThemeBitmap(
                    coverTheme = coverTheme,
                    visibleCaption = visibleCaption
                )
            }
        }

        val pngOut = ByteArrayOutputStream()
        renderedBitmap.compress(Bitmap.CompressFormat.PNG, 100, pngOut)
        renderedBitmap.recycle()

        val pngBytes = pngOut.toByteArray()

        // 2. Siapkan muatan rahasia & Foto Kedua (jika lampiran adalah foto/gambar)
        val isSecondPhoto = isImageExtension(attachedExtension)
        val keySeed = "NYX2_STEGO::$cleanPass"
        val attachedBytes: ByteArray? = if (attachedFile != null && attachedFile.exists() && attachedFile.length() <= 2 * 1024 * 1024) {
            val rawBytes = attachedFile.readBytes()
            if (isSecondPhoto) {
                // Pastikan Foto Kedua memiliki stream bitmap PNG/JPG valid agar saat dibuka di app langsung tampil visualnya
                val testDecode = try {
                    BitmapFactory.decodeByteArray(rawBytes, 0, rawBytes.size)
                } catch (_: Exception) {
                    null
                }
                if (testDecode != null) {
                    testDecode.recycle()
                    rawBytes
                } else {
                    val synthBmp = renderSyntheticPhotoPreviewBitmap(
                        photoTitle = "${attachedDisplayName ?: "Foto_Rahasia"}.${attachedExtension ?: "png"}",
                        subtitle = "FOTO KEDUA • TERSEMBUNYI DI DALAM FILE",
                        theme = coverTheme,
                        isSecondPhoto = true
                    )
                    val out = ByteArrayOutputStream()
                    synthBmp.compress(Bitmap.CompressFormat.PNG, 100, out)
                    synthBmp.recycle()
                    out.toByteArray()
                }
            } else {
                rawBytes
            }
        } else {
            null
        }

        val attachedBase64 = if (attachedBytes != null) {
            Base64.encodeToString(xorBytesWithSeed(attachedBytes, keySeed), Base64.NO_WRAP)
        } else {
            ""
        }

        val encryptedMsgBase64 = Base64.encodeToString(
            xorBytesWithSeed(secretMessage.toByteArray(Charsets.UTF_8), keySeed),
            Base64.NO_WRAP
        )
        val encryptedTitleBase64 = Base64.encodeToString(
            xorBytesWithSeed(secretTitle.ifBlank { "Pesan Rahasia" }.toByteArray(Charsets.UTF_8), keySeed),
            Base64.NO_WRAP
        )
        val passHash = if (cleanPass.isBlank()) "NONE" else EncryptionScriptEngine.compute3LayerKeyFingerprint(cleanPass, cleanPass, cleanPass)

        val carrierSourceLabel = when {
            isQrKeyCardMode -> "QR_CARD::Kartu Kunci Stego-QR"
            isSecondPhoto && sourceCarrierName != null ->
                "DUAL_PHOTO::$sourceCarrierName + ${attachedDisplayName ?: "Foto2"}.${attachedExtension ?: "png"}"
            isSecondPhoto ->
                "DUAL_PHOTO::${coverTheme.title} + ${attachedDisplayName ?: "Foto2"}.${attachedExtension ?: "png"}"
            sourceCarrierName != null -> "VAULT_PHOTO::$sourceCarrierName"
            else -> "POSTER_THEME::${coverTheme.title}"
        }

        val stegoMetadataBlock = buildString {
            append(STEGO_MAGIC_HEADER)
            append("THEME=").append(coverTheme.name).append("\n")
            append("CARRIER_SOURCE=").append(carrierSourceLabel.replace("\n", "")).append("\n")
            append("QR_MODE=").append(if (isQrKeyCardMode) "1" else "0").append("\n")
            append("DUAL_IMAGE_MODE=").append(if (isSecondPhoto && attachedBytes != null) "1" else "0").append("\n")
            append("PASSHASH=").append(passHash).append("\n")
            append("DATE=").append(dateLabel).append("\n")
            append("CHECKSUM=").append(checksum).append("\n")
            append("TITLE_B64=").append(encryptedTitleBase64).append("\n")
            append("MSG_B64=").append(encryptedMsgBase64).append("\n")
            append("FILE_NAME=").append(attachedDisplayName?.replace("\n", "") ?: "").append("\n")
            append("FILE_EXT=").append(attachedExtension?.replace("\n", "") ?: "").append("\n")
            append("FILE_B64=").append(attachedBase64)
            append(STEGO_MAGIC_FOOTER)
        }

        val trailerBytes = stegoMetadataBlock.toByteArray(Charsets.UTF_8)

        outputFile.parentFile?.mkdirs()
        outputFile.outputStream().use { fos ->
            fos.write(pngBytes)
            fos.write(trailerBytes)
        }
        return outputFile.length()
    }

    private fun renderPosterThemeBitmap(
        coverTheme: StegoCoverTheme,
        visibleCaption: String
    ): Bitmap {
        val width = 480
        val height = 320
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)

        val bgPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            shader = LinearGradient(
                0f, 0f, width.toFloat(), height.toFloat(),
                coverTheme.primaryColorHex,
                coverTheme.secondaryColorHex,
                Shader.TileMode.CLAMP
            )
        }
        canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), bgPaint)

        val circlePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = coverTheme.accentColorHex
            alpha = 48
        }
        canvas.drawCircle(width * 0.82f, height * 0.26f, 88f, circlePaint)
        canvas.drawCircle(width * 0.18f, height * 0.78f, 64f, circlePaint)

        val cardPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.WHITE
            alpha = 26
        }
        canvas.drawRoundRect(
            RectF(28f, 28f, width - 28f, height - 28f),
            28f,
            28f,
            cardPaint
        )

        val titlePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.WHITE
            textSize = 26f
            isFakeBoldText = true
        }
        val subPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = coverTheme.accentColorHex
            textSize = 16f
        }
        val cleanVisible = visibleCaption.trim().ifBlank { coverTheme.title }
        canvas.drawText(cleanVisible.take(26), 52f, height * 0.48f, titlePaint)
        canvas.drawText(coverTheme.subtitle, 52f, height * 0.60f, subPaint)
        canvas.drawText("PNG Digital Artwork • 480x320", 52f, height * 0.76f, subPaint)
        return bitmap
    }

    private fun decodeOrRenderCarrierPhotoBitmap(
        sourceFile: File,
        carrierName: String,
        fallbackTheme: StegoCoverTheme
    ): Bitmap {
        try {
            val boundsOpts = BitmapFactory.Options().apply { inJustDecodeBounds = true }
            BitmapFactory.decodeFile(sourceFile.absolutePath, boundsOpts)
            if (boundsOpts.outWidth > 0 && boundsOpts.outHeight > 0) {
                var sampleSize = 1
                val maxDim = max(boundsOpts.outWidth, boundsOpts.outHeight)
                while (maxDim / sampleSize > 960) {
                    sampleSize *= 2
                }
                val decodeOpts = BitmapFactory.Options().apply {
                    inSampleSize = sampleSize
                    inPreferredConfig = Bitmap.Config.ARGB_8888
                }
                val decoded = BitmapFactory.decodeFile(sourceFile.absolutePath, decodeOpts)
                if (decoded != null) {
                    return decoded
                }
            }
        } catch (_: Exception) {
            // Fall through to rendered photo carrier representation
        }

        val width = 480
        val height = 320
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)

        val bgPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            shader = LinearGradient(
                0f, 0f, width.toFloat(), height.toFloat(),
                0xFF0F172A.toInt(),
                fallbackTheme.secondaryColorHex,
                Shader.TileMode.CLAMP
            )
        }
        canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), bgPaint)

        val framePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = fallbackTheme.accentColorHex
            style = Paint.Style.STROKE
            strokeWidth = 3f
            alpha = 180
        }
        canvas.drawRoundRect(RectF(20f, 20f, width - 20f, height - 20f), 22f, 22f, framePaint)

        val badgePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = fallbackTheme.accentColorHex
            textSize = 14f
            isFakeBoldText = true
        }
        val titlePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.WHITE
            textSize = 24f
            isFakeBoldText = true
        }
        val subPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.LTGRAY
            textSize = 15f
        }
        canvas.drawText("FOTO BRANKAS • CARRIER STEGO ASLI", 44f, 86f, badgePaint)
        canvas.drawText(carrierName.take(26), 44f, 140f, titlePaint)
        canvas.drawText("0.00% Distorsi Piksel • Lossless PNG", 44f, 182f, subPaint)
        canvas.drawText("Disegel dengan NYX2 Stego Studio", 44f, 224f, subPaint)
        return bitmap
    }

    private fun renderQrKeyCardBitmap(
        coverTheme: StegoCoverTheme,
        cardTitle: String,
        checksum: String,
        dateLabel: String,
        qrSeed: String
    ): Bitmap {
        val width = 480
        val height = 320
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)

        val bgPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            shader = LinearGradient(
                0f, 0f, width.toFloat(), height.toFloat(),
                0xFF090D16.toInt(),
                coverTheme.primaryColorHex,
                Shader.TileMode.CLAMP
            )
        }
        canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), bgPaint)

        val borderPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = coverTheme.accentColorHex
            style = Paint.Style.STROKE
            strokeWidth = 3f
        }
        canvas.drawRoundRect(RectF(16f, 16f, width - 16f, height - 16f), 24f, 24f, borderPaint)

        val qrBoxLeft = 34f
        val qrBoxTop = 44f
        val qrBoxSize = 210f
        val whiteCardPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply { color = Color.WHITE }
        canvas.drawRoundRect(
            RectF(qrBoxLeft, qrBoxTop, qrBoxLeft + qrBoxSize, qrBoxTop + qrBoxSize),
            16f,
            16f,
            whiteCardPaint
        )

        val matrix = generateDeterministicQrMatrix(qrSeed)
        val modules = matrix.size
        val padding = 12f
        val cellSize = (qrBoxSize - padding * 2f) / modules.toFloat()
        val darkPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply { color = 0xFF0F172A.toInt() }

        for (r in 0 until modules) {
            for (c in 0 until modules) {
                if (matrix[r][c]) {
                    val left = qrBoxLeft + padding + c * cellSize
                    val top = qrBoxTop + padding + r * cellSize
                    canvas.drawRect(left, top, left + cellSize + 0.4f, top + cellSize + 0.4f, darkPaint)
                }
            }
        }

        val textLeft = 262f
        val headerPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = coverTheme.accentColorHex
            textSize = 13f
            isFakeBoldText = true
        }
        val titlePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.WHITE
            textSize = 20f
            isFakeBoldText = true
        }
        val monoPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.LTGRAY
            textSize = 13f
        }

        canvas.drawText("STEGO-QR KEY CARD", textLeft, 72f, headerPaint)
        canvas.drawText(cardTitle.trim().ifBlank { "Kunci Rahasia" }.take(16), textLeft, 106f, titlePaint)
        canvas.drawText("ID: ${checksum.take(12)}", textLeft, 144f, monoPaint)
        canvas.drawText(dateLabel, textLeft, 174f, monoPaint)
        canvas.drawText("100% OFFLINE STEGO", textLeft, 214f, headerPaint)
        canvas.drawText("Buka di Stego Studio", textLeft, 240f, monoPaint)

        return bitmap
    }

    /**
     * Memeriksa apakah file .PNG memiliki muatan steganografi NYX2_STEGO_PNG_V1
     */
    fun hasStegoPayload(file: File): Boolean {
        if (!file.exists() || file.length() <= 128L) return false
        return try {
            val content = file.readBytes().toString(Charsets.UTF_8)
            content.contains(STEGO_MAGIC_HEADER) && content.contains(STEGO_MAGIC_FOOTER)
        } catch (_: Exception) {
            false
        }
    }

    /**
     * Membongkar dan mendekripsi isi rahasia (serta Foto Kedua jika Dual-Photo) dari dalam gambar .PNG
     */
    fun decodeStegoPngFile(file: File, passcode: String = ""): DecodedStegoPayload {
        if (!file.exists()) return DecodedStegoPayload(isStegoCarrier = false)
        return try {
            val rawText = file.readBytes().toString(Charsets.UTF_8)
            val startIdx = rawText.indexOf(STEGO_MAGIC_HEADER)
            val endIdx = rawText.indexOf(STEGO_MAGIC_FOOTER)
            if (startIdx < 0 || endIdx <= startIdx) {
                return DecodedStegoPayload(isStegoCarrier = false)
            }

            val body = rawText.substring(startIdx + STEGO_MAGIC_HEADER.length, endIdx)
            val map = mutableMapOf<String, String>()
            body.lines().forEach { line ->
                val eq = line.indexOf('=')
                if (eq > 0) {
                    map[line.substring(0, eq).trim()] = line.substring(eq + 1).trim()
                }
            }

            val storedPassHash = map["PASSHASH"] ?: "NONE"
            val carrierSource = map["CARRIER_SOURCE"] ?: ""
            val isQrCard = (map["QR_MODE"] == "1") || carrierSource.startsWith("QR_CARD::")
            val fileExt = map["FILE_EXT"]?.takeIf { it.isNotBlank() }
            val isDualPhoto = (map["DUAL_IMAGE_MODE"] == "1") ||
                carrierSource.startsWith("DUAL_PHOTO::") ||
                isImageExtension(fileExt)
            val cleanPass = passcode.trim().uppercase()
            val requiresPass = storedPassHash != "NONE"
            if (requiresPass) {
                val inputHash = if (cleanPass.isBlank()) "" else EncryptionScriptEngine.compute3LayerKeyFingerprint(cleanPass, cleanPass, cleanPass)
                if (inputHash != storedPassHash) {
                    return DecodedStegoPayload(
                        isStegoCarrier = true,
                        isPasscodeLocked = true,
                        isPasscodeValid = false,
                        coverThemeName = map["THEME"] ?: "",
                        carrierSourceLabel = carrierSource,
                        isQrKeyCard = isQrCard,
                        isDualPhotoFusion = isDualPhoto,
                        createdAtLabel = map["DATE"] ?: "",
                        checksumHex = map["CHECKSUM"] ?: ""
                    )
                }
            }

            val keySeed = "NYX2_STEGO::$cleanPass"
            val titleBytes = Base64.decode(map["TITLE_B64"] ?: "", Base64.DEFAULT)
            val msgBytes = Base64.decode(map["MSG_B64"] ?: "", Base64.DEFAULT)
            val decodedTitle = xorBytesWithSeed(titleBytes, keySeed).toString(Charsets.UTF_8)
            val decodedMsg = xorBytesWithSeed(msgBytes, keySeed).toString(Charsets.UTF_8)

            val fileName = map["FILE_NAME"]?.takeIf { it.isNotBlank() }
            val fileB64 = map["FILE_B64"]?.takeIf { it.isNotBlank() }
            val decodedFileBytes = if (fileB64 != null) {
                xorBytesWithSeed(Base64.decode(fileB64, Base64.DEFAULT), keySeed)
            } else null

            DecodedStegoPayload(
                isStegoCarrier = true,
                isPasscodeLocked = requiresPass,
                isPasscodeValid = true,
                coverThemeName = map["THEME"] ?: "COSMIC_POSTCARD",
                carrierSourceLabel = carrierSource,
                isQrKeyCard = isQrCard,
                isDualPhotoFusion = isDualPhoto && decodedFileBytes != null,
                secretTitle = decodedTitle,
                secretMessage = decodedMsg,
                embeddedFileName = fileName,
                embeddedFileExtension = fileExt,
                embeddedFileBytes = decodedFileBytes,
                createdAtLabel = map["DATE"] ?: "Baru saja",
                payloadSizeBytes = msgBytes.size + (decodedFileBytes?.size ?: 0),
                checksumHex = map["CHECKSUM"] ?: "NYX2-OK"
            )
        } catch (_: Exception) {
            DecodedStegoPayload(isStegoCarrier = false)
        }
    }

    private fun xorBytesWithSeed(input: ByteArray, seed: String): ByteArray {
        val keyBytes = java.security.MessageDigest.getInstance("SHA-256")
            .digest(seed.toByteArray(Charsets.UTF_8))
        val out = ByteArray(input.size)
        for (i in input.indices) {
            out[i] = (input[i].toInt() xor keyBytes[i % keyBytes.size].toInt()).toByte()
        }
        return out
    }
}

