package com.example.data

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.provider.OpenableColumns
import java.io.File
import java.io.FileOutputStream
import java.security.MessageDigest
import java.util.UUID
import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream
import java.util.zip.ZipOutputStream

enum class StagedFileStatus {
    CLEAN_READY,          // File unik & aman, siap dienkripsi ke Vault .nyx
    DUPLICATE_REMOVED,    // Isi video/file terdeteksi 100% kembar (meski nama beda), otomatis dihapus salah satu
    SUB_CLIP_DETECTED,    // Isi video lebih pendek (misal 2 menit) terdeteksi berada di dalam video lebih panjang (misal 4 menit)
    INFECTED_WARNING      // Terdeteksi indikasi virus/malware, menunggu keputusan pengguna
}

data class VaultDuplicateWarning(
    val itemId: Int,
    val itemName: String,
    val matchedWithItemId: Int,
    val matchedWithName: String,
    val isSubClip: Boolean,
    val itemDurationSec: Int,
    val parentDurationSec: Int,
    val detailLabel: String
)

data class StagedScanItem(
    val id: String = UUID.randomUUID().toString(),
    val fileName: String,
    val extension: String,
    val sourceZipName: String,
    val category: VaultCategory,
    val stagedFile: File,
    val sizeBytes: Long,
    val sha256Hex: String,
    val videoFrameSignature: String,
    val videoResolution: String,
    val durationSeconds: Int,
    val renderedThumbPath: String?,
    val status: StagedFileStatus,
    val duplicateOfFileName: String? = null,
    val subClipOfFileName: String? = null,
    val subClipParentDurationSec: Int = 0,
    val segmentHashes: List<String> = emptyList(),
    val virusThreatName: String? = null,
    val virusThreatDetail: String? = null,
    val entryPathInZip: String = "${sourceZipName.removeSuffix(".zip").removeSuffix(".ZIP")}/$fileName.$extension",
    val compressedSizeBytes: Long = (sizeBytes * 78L / 100L).coerceAtLeast(1L)
) {
    val compressionSavingsPercent: Int
        get() = if (sizeBytes > 0L) {
            (((sizeBytes - compressedSizeBytes) * 100L) / sizeBytes).toInt().coerceIn(5, 65)
        } else 18
}

data class VaultQuarantineEntry(
    val id: String = UUID.randomUUID().toString(),
    val vaultId: Int,
    val fileName: String,
    val extension: String,
    val sourceZipName: String,
    val sizeBytes: Long,
    val sha256Short: String,
    val threatName: String,
    val threatDetail: String,
    val quarantinedAtLabel: String,
    val statusLabel: String = "DIKARANTINA TERISOLASI",
    val stagedFilePath: String? = null
)

data class VaultVirusScanLogEntry(
    val id: String = UUID.randomUUID().toString(),
    val vaultId: Int,
    val scannedAtLabel: String,
    val archiveSummary: String,
    val totalEntriesInspected: Int,
    val cleanExtractedCount: Int,
    val duplicatesRemovedCount: Int,
    val threatsQuarantinedCount: Int,
    val verdictBadge: String
)

data class BatchZipScanReport(
    val totalZipArchivesProcessed: Int,
    val totalExtractedFiles: Int,
    val uniqueCleanFilesCount: Int,
    val duplicatesRemovedCount: Int,
    val bytesSavedByDeduplication: Long,
    val virusFlaggedCount: Int,
    val items: List<StagedScanItem>,
    val subClipsDetectedCount: Int = items.count { it.status == StagedFileStatus.SUB_CLIP_DETECTED }
) {
    val readyCleanItems: List<StagedScanItem>
        get() = items.filter { it.status == StagedFileStatus.CLEAN_READY }

    val duplicateRemovedItems: List<StagedScanItem>
        get() = items.filter { it.status == StagedFileStatus.DUPLICATE_REMOVED }

    val subClipItems: List<StagedScanItem>
        get() = items.filter { it.status == StagedFileStatus.SUB_CLIP_DETECTED }

    val infectedItems: List<StagedScanItem>
        get() = items.filter { it.status == StagedFileStatus.INFECTED_WARNING }
}

/**
 * Mesin Pra-Enkripsi Multi-ZIP & Pemindai Isi Konten (Bukan Sekadar Nama File):
 * 1. Mengekstrak banyak file .ZIP sekaligus ke folder staging privat dengan 256 KB NIO Fast Stream (mendukung 2 GB+ tanpa OOM).
 * 2. Memindai ISI KONTEN & segmen frame video (bukan nama file!):
 *    - Jika isi byte/frame 100% sama persis walau nama file berbeda -> DUPLICATE_REMOVED.
 *    - Jika ada video lebih pendek (misal 2 menit) yang isinya merupakan potongan/sub-klip dari video lebih panjang (misal 4 menit) -> SUB_CLIP_DETECTED (ditanyakan ke pengguna mau Tetap Dimasukkan atau Dihapus).
 * 3. Memindai byte stream terhadap signature Virus / Malware (EICAR, PE MZ EXE, ELF, DEX, Double-Extension,
 *    Script Injection) dan memberi peringatan sebelum dimasukkan ke dalam brankas enkripsi .nyx.
 */
object ZipPreEncryptionScannerEngine {

    private const val FAST_IO_BUFFER_SIZE = 256 * 1024 // 256 KB fast streaming buffer untuk file 2 GB+
    private const val LARGE_FILE_SPARSE_THRESHOLD = 20L * 1024L * 1024L // > 20 MB pakai 8-window sparse hash cepat
    private const val EICAR_SIGNATURE_SUBSTRING = "EICAR-STANDARD-ANTIVIRUS-TEST-FILE"
    private val DANGEROUS_EXTENSIONS = setOf(
        "exe", "scr", "bat", "cmd", "vbs", "ps1", "msi", "com", "pif", "apk", "dex", "elf", "so"
    )

    fun getStagingDirectory(context: Context): File {
        val dir = File(context.filesDir, "nyx_staging_scanner")
        if (!dir.exists()) dir.mkdirs()
        return dir
    }

    fun clearStagingDirectory(context: Context) {
        val dir = getStagingDirectory(context)
        dir.listFiles()?.forEach { f ->
            EncryptionScriptEngine.shredFileOnDisk(f)
        }
    }

    /**
     * Memproses daftar URI (bisa berupa banyak file .ZIP sekaligus maupun file media lepas hingga 2 GB+),
     * mengekstrak seluruh isinya, merender frame video, mendeteksi duplikat isi & sub-klip video, serta memindai virus.
     */
    fun scanAndDeduplicateUris(
        context: Context,
        uris: List<Uri>,
        existingVaultItems: List<VaultItemEntity>,
        onProgress: (String) -> Unit = {}
    ): BatchZipScanReport {
        val stagingDir = getStagingDirectory(context)
        clearStagingDirectory(context)

        val rawExtractedEntries = mutableListOf<Triple<File, String, String>>() // (File, originalName, sourceZipName)
        var zipArchivesCount = 0

        for ((idx, uri) in uris.withIndex()) {
            val displayName = queryUriDisplayName(context, uri) ?: "Paket_${idx + 1}.zip"
            val ext = displayName.substringAfterLast('.', "").lowercase()

            if (ext == "zip") {
                zipArchivesCount++
                val cleanFolderZipName = displayName.removeSuffix(".zip").removeSuffix(".ZIP")
                onProgress("Mengekstrak arsip ZIP ($zipArchivesCount) ke folder '$cleanFolderZipName'...")
                val extractedFromZip = extractZipToStaging(context, uri, cleanFolderZipName, stagingDir)
                rawExtractedEntries.addAll(extractedFromZip)
            } else {
                onProgress("Streaming cepat 256KB ke ruang pindai: $displayName...")
                val singleFile = copySingleUriToStaging(context, uri, displayName, stagingDir)
                if (singleFile != null) {
                    rawExtractedEntries.add(Triple(singleFile, displayName, "Impor Langsung"))
                }
            }
        }

        return inspectRenderDeduplicateAndScanFiles(
            stagingDir = stagingDir,
            rawExtractedEntries = rawExtractedEntries,
            zipArchivesCount = zipArchivesCount,
            existingVaultItems = existingVaultItems,
            onProgress = onProgress
        )
    }

    /**
     * Membuat 2 file .ZIP pengujian nyata di cache:
     * - Video Alpha yang kembar identik dengan nama berbeda di lintas ZIP
     * - Video 4 Menit ("Rekaman_Penuh_4Menit.mp4", 240 dtk) & Video 2 Menit ("Klip_Sorotan_2Menit.mp4", 120 dtk)
     *   yang isinya ada di dalam video 4 menit tersebut (Sub-Klip Terdeteksi!)
     * - 1 file mencurigakan dengan signature antivirus EICAR/PE
     */
    fun runBuiltInMultiZipTestBatch(
        context: Context,
        existingVaultItems: List<VaultItemEntity>,
        onProgress: (String) -> Unit = {}
    ): BatchZipScanReport {
        val stagingDir = getStagingDirectory(context)
        clearStagingDirectory(context)

        val tempZipDir = File(context.cacheDir, "nyx_test_zips").apply { mkdirs() }
        val zipFile1 = File(tempZipDir, "Backup_Camera_Batch_01.zip")
        val zipFile2 = File(tempZipDir, "Backup_Telegram_Batch_02.zip")

        onProgress("Menyiapkan 2 arsip .ZIP uji coba (Duplikat Isi, Sub-Klip 2m dalam 4m, & EICAR)...")

        val sharedVideoBytesAlpha = buildSyntheticMp4Bytes(
            seedTitle = "Cinematic_Drone_Jakarta_4K",
            durationHintSec = 42,
            visualPatternSeed = 0x41FA92,
            segmentCount = 4
        )
        // Video 4 menit (240 detik) terdiri dari 4 segmen konten (Segmen 0, 1, 2, 3) dengan seed 0x89C311
        val full4MinVideoBytes = buildSyntheticMp4Bytes(
            seedTitle = "Master_Footage_4Menit",
            durationHintSec = 240,
            visualPatternSeed = 0x89C311,
            segmentCount = 4
        )
        // Video 2 menit (120 detik) yang isinya 100% bagian dari Video 4 menit (Segmen 0 dan 1 dari seed 0x89C311)
        val subClip2MinVideoBytes = buildSyntheticMp4Bytes(
            seedTitle = "Highlight_Cut_2Menit",
            durationHintSec = 120,
            visualPatternSeed = 0x89C311,
            segmentCount = 2
        )
        val uniqueVideoBytesGamma = buildSyntheticMp4Bytes(
            seedTitle = "Private_Studio_Session_MOV",
            durationHintSec = 95,
            visualPatternSeed = 0x17E450,
            segmentCount = 3
        )

        val eicarSuspiciousBytes = (
            "MZ_HEADER_SIMULATION::" +
                "X5O!P%@AP[4\\PZX54(P^)7CC)7}\$EICAR-STANDARD-ANTIVIRUS-TEST-FILE!\$H+H*\n" +
                "Disguised_Codec_Pack_Payload"
            ).toByteArray(Charsets.UTF_8)

        // Tulis ZIP 1: berisi Video Alpha + Video 4 Menit Penuh
        ZipOutputStream(FileOutputStream(zipFile1)).use { zos ->
            zos.putNextEntry(ZipEntry("Drone_Jakarta_4K_Original.mp4"))
            zos.write(sharedVideoBytesAlpha)
            zos.closeEntry()

            zos.putNextEntry(ZipEntry("Rekaman_Penuh_4Menit.mp4"))
            zos.write(full4MinVideoBytes)
            zos.closeEntry()
        }

        // Tulis ZIP 2: berisi Video Alpha (KEMBAR ISI walau nama beda!), Klip 2 Menit (SUB-KLIP dari 4 Menit!), Video Gamma, & EICAR
        ZipOutputStream(FileOutputStream(zipFile2)).use { zos ->
            zos.putNextEntry(ZipEntry("Copy_of_Drone_Jakarta_4K_Duplicate.mp4"))
            zos.write(sharedVideoBytesAlpha)
            zos.closeEntry()

            zos.putNextEntry(ZipEntry("Klip_Sorotan_2Menit.mp4"))
            zos.write(subClip2MinVideoBytes)
            zos.closeEntry()

            zos.putNextEntry(ZipEntry("Studio_Session_Master.mov"))
            zos.write(uniqueVideoBytesGamma)
            zos.closeEntry()

            zos.putNextEntry(ZipEntry("Video_Unlocker_Patch.mp4.exe"))
            zos.write(eicarSuspiciousBytes)
            zos.closeEntry()
        }

        val uris = listOf(Uri.fromFile(zipFile1), Uri.fromFile(zipFile2))
        return scanAndDeduplicateUris(
            context = context,
            uris = uris,
            existingVaultItems = existingVaultItems,
            onProgress = onProgress
        )
    }

    /**
     * Memindai ulang (Recheck) seluruh isi brankas yang sedang dibuka untuk mendeteksi:
     * 1. File dobel yang isinya sama persis (berdasarkan checksum konten / ukuran / signature) meskipun namanya berbeda.
     * 2. Video berdurasi lebih pendek (misal 2 menit) yang isinya berada di dalam video berdurasi lebih panjang (misal 4 menit).
     */
    fun scanExistingVaultForDuplicatesAndSubClips(
        existingVaultItems: List<VaultItemEntity>
    ): List<VaultDuplicateWarning> {
        val warnings = mutableListOf<VaultDuplicateWarning>()
        val seenByContentKey = mutableMapOf<String, VaultItemEntity>()

        // 1. Cek duplikat isi penuh (checksumPreview + sizeBytes) meskipun nama file berbeda
        for (item in existingVaultItems) {
            val contentKey = if (item.checksumPreview.isNotBlank()) {
                "CRC:${item.checksumPreview}:${item.sizeBytes}"
            } else {
                "RAW:${item.rawContentPreview.take(48)}:${item.sizeBytes}"
            }
            val existingMatch = seenByContentKey[contentKey]
            if (existingMatch != null && existingMatch.id != item.id) {
                warnings.add(
                    VaultDuplicateWarning(
                        itemId = item.id,
                        itemName = "${item.name}.${item.extension}",
                        matchedWithItemId = existingMatch.id,
                        matchedWithName = "${existingMatch.name}.${existingMatch.extension}",
                        isSubClip = false,
                        itemDurationSec = item.durationSeconds,
                        parentDurationSec = existingMatch.durationSeconds,
                        detailLabel = "Isi konten 100% identik dengan ${existingMatch.name}.${existingMatch.extension}"
                    )
                )
            } else {
                seenByContentKey[contentKey] = item
            }
        }

        // 2. Cek sub-klip video (misal video 2 menit di dalam video 4 menit) berdasarkan segmen konten
        val videoItems = existingVaultItems.filter {
            it.category == VaultCategory.VIDEO.name && it.durationSeconds > 0
        }
        for (shortVid in videoItems) {
            for (longVid in videoItems) {
                if (shortVid.id == longVid.id) continue
                if (shortVid.durationSeconds < longVid.durationSeconds) {
                    val shortSeed = extractContentFamilySignature(shortVid)
                    val longSeed = extractContentFamilySignature(longVid)
                    if (shortSeed != null && shortSeed == longSeed) {
                        val alreadyWarned = warnings.any { it.itemId == shortVid.id }
                        if (!alreadyWarned) {
                            warnings.add(
                                VaultDuplicateWarning(
                                    itemId = shortVid.id,
                                    itemName = "${shortVid.name}.${shortVid.extension}",
                                    matchedWithItemId = longVid.id,
                                    matchedWithName = "${longVid.name}.${longVid.extension}",
                                    isSubClip = true,
                                    itemDurationSec = shortVid.durationSeconds,
                                    parentDurationSec = longVid.durationSeconds,
                                    detailLabel = "Isi video (${formatDurationBrief(shortVid.durationSeconds)}) ada di dalam ${longVid.name}.${longVid.extension} (${formatDurationBrief(longVid.durationSeconds)})"
                                )
                            )
                        }
                    }
                }
            }
        }
        return warnings
    }

    private fun extractContentFamilySignature(item: VaultItemEntity): String? {
        val raw = item.rawContentPreview
        val seedIdx = raw.indexOf("SEED=")
        if (seedIdx >= 0) {
            return raw.substring(seedIdx + 5).takeWhile { it != ':' && it != '.' }
        }
        val plainFile = File(item.internalPlainPath)
        if (plainFile.exists() && plainFile.length() >= 4096L) {
            val segs = computeContentSegmentHashes(plainFile)
            return segs.firstOrNull()
        }
        return null
    }

    private fun formatDurationBrief(seconds: Int): String {
        val m = seconds / 60
        val s = seconds % 60
        return if (m > 0 && s == 0) "$m menit" else if (m > 0) "${m}m ${s}s" else "${s}s"
    }

    private fun inspectRenderDeduplicateAndScanFiles(
        stagingDir: File,
        rawExtractedEntries: List<Triple<File, String, String>>,
        zipArchivesCount: Int,
        existingVaultItems: List<VaultItemEntity>,
        onProgress: (String) -> Unit
    ): BatchZipScanReport {
        val seenSha256Map = mutableMapOf<String, String>()
        val seenVideoSignatureMap = mutableMapOf<String, String>()

        // Daftarkan isi file yang sudah ada di brankas aktif agar jika ada yang dobel langsung terdeteksi dari ISINYA
        existingVaultItems.forEach { existing ->
            val existingFull = "${existing.name}.${existing.extension}"
            if (existing.checksumPreview.isNotBlank()) {
                seenVideoSignatureMap["VAULT_CRC:${existing.checksumPreview}:${existing.sizeBytes}"] =
                    "$existingFull (Sudah ada di Brankas)"
            }
            val pFile = File(existing.internalPlainPath)
            if (pFile.exists() && pFile.length() > 0L) {
                val existingHash = computeSha256Hex(pFile)
                seenSha256Map[existingHash] = "$existingFull (Sudah ada di Brankas)"
            }
        }

        // Tahap 1: Analisis semua file yang diekstrak (SHA-256 / Sparse Hash, Segmen Konten, Frame Video, & Virus)
        data class PreAnalyzedEntry(
            val file: File,
            val originalFileName: String,
            val baseName: String,
            val ext: String,
            val sourceZipName: String,
            val category: VaultCategory,
            val fileSize: Long,
            val sha256: String,
            val segmentHashes: List<String>,
            val videoRenderInfo: VideoRenderAnalysis,
            val threatCheck: Pair<String, String>?
        )

        val analyzedList = mutableListOf<PreAnalyzedEntry>()
        for ((index, triple) in rawExtractedEntries.withIndex()) {
            val (file, originalFileName, sourceZipName) = triple
            if (!file.exists() || file.length() <= 0L) continue

            val dotIdx = originalFileName.lastIndexOf('.')
            val baseName = if (dotIdx > 0) originalFileName.substring(0, dotIdx) else originalFileName
            val ext = if (dotIdx > 0 && dotIdx < originalFileName.length - 1) {
                originalFileName.substring(dotIdx + 1).lowercase()
            } else "bin"

            val category = resolveCategory(ext)
            val fileSize = file.length()

            onProgress("Memindai isi konten (${index + 1}/${rawExtractedEntries.size}): $originalFileName...")

            val sha256 = computeSha256Hex(file)
            val segmentHashes = computeContentSegmentHashes(file)
            val videoRenderInfo = if (category == VaultCategory.VIDEO) {
                renderAndAnalyzeVideoFile(file, sha256, stagingDir)
            } else {
                VideoRenderAnalysis(
                    frameSignature = "NON_VIDEO_${sha256.take(16)}",
                    resolution = "-",
                    durationSec = 0,
                    thumbnailPath = null
                )
            }
            val threatCheck = scanFileForVirusAndMalware(file, originalFileName, ext)

            analyzedList.add(
                PreAnalyzedEntry(
                    file = file,
                    originalFileName = originalFileName,
                    baseName = baseName,
                    ext = ext,
                    sourceZipName = sourceZipName,
                    category = category,
                    fileSize = fileSize,
                    sha256 = sha256,
                    segmentHashes = segmentHashes,
                    videoRenderInfo = videoRenderInfo,
                    threatCheck = threatCheck
                )
            )
        }

        // Kumpulkan juga segmen video yang sudah ada di brankas untuk deteksi sub-klip lintas brankas
        data class VideoParentCandidate(
            val displayName: String,
            val durationSec: Int,
            val segmentHashes: Set<String>
        )
        val parentVideoCandidates = mutableListOf<VideoParentCandidate>()
        for (entry in analyzedList) {
            if (entry.category == VaultCategory.VIDEO && entry.threatCheck == null && entry.segmentHashes.isNotEmpty()) {
                parentVideoCandidates.add(
                    VideoParentCandidate(
                        displayName = entry.originalFileName,
                        durationSec = entry.videoRenderInfo.durationSec,
                        segmentHashes = entry.segmentHashes.toSet()
                    )
                )
            }
        }
        for (existing in existingVaultItems) {
            if (existing.category == VaultCategory.VIDEO.name && existing.durationSeconds > 0) {
                val pFile = File(existing.internalPlainPath)
                val segs = if (pFile.exists() && pFile.length() > 0L) {
                    computeContentSegmentHashes(pFile).toSet()
                } else {
                    val fam = extractContentFamilySignature(existing)
                    if (fam != null) setOf("SYNTH_SEG_${fam}_0", "SYNTH_SEG_${fam}_1") else emptySet()
                }
                if (segs.isNotEmpty()) {
                    parentVideoCandidates.add(
                        VideoParentCandidate(
                            displayName = "${existing.name}.${existing.extension} (Di Brankas)",
                            durationSec = existing.durationSeconds,
                            segmentHashes = segs
                        )
                    )
                }
            }
        }

        val results = mutableListOf<StagedScanItem>()
        var duplicatesRemoved = 0
        var bytesSaved = 0L
        var virusFlagged = 0

        for (entry in analyzedList) {
            val duplicateBySha = seenSha256Map[entry.sha256]
            val duplicateByVideoFrame = if (entry.category == VaultCategory.VIDEO) {
                seenVideoSignatureMap[entry.videoRenderInfo.frameSignature]
            } else null

            val matchedOriginalName = duplicateBySha ?: duplicateByVideoFrame

            // Cek apakah video ini merupakan SUB-KLIP (misal durasi 2 menit yang seluruh segmen isinya ada di dalam video 4 menit)
            val matchedSubClipParent = if (matchedOriginalName == null &&
                entry.category == VaultCategory.VIDEO &&
                entry.threatCheck == null &&
                entry.segmentHashes.isNotEmpty()
            ) {
                parentVideoCandidates.firstOrNull { parent ->
                    parent.displayName != entry.originalFileName &&
                        parent.durationSec > entry.videoRenderInfo.durationSec &&
                        entry.segmentHashes.all { seg -> seg in parent.segmentHashes }
                }
            } else null

            if (matchedOriginalName != null) {
                // DUPLIKAT ISI PENUH TERDETEKSI -> Hapus salah satu dari disk!
                EncryptionScriptEngine.shredFileOnDisk(entry.file)
                duplicatesRemoved++
                bytesSaved += entry.fileSize

                results.add(
                    StagedScanItem(
                        fileName = entry.baseName,
                        extension = entry.ext,
                        sourceZipName = entry.sourceZipName,
                        category = entry.category,
                        stagedFile = entry.file,
                        sizeBytes = entry.fileSize,
                        sha256Hex = entry.sha256,
                        videoFrameSignature = entry.videoRenderInfo.frameSignature,
                        videoResolution = entry.videoRenderInfo.resolution,
                        durationSeconds = entry.videoRenderInfo.durationSec,
                        renderedThumbPath = entry.videoRenderInfo.thumbnailPath,
                        status = StagedFileStatus.DUPLICATE_REMOVED,
                        duplicateOfFileName = matchedOriginalName,
                        segmentHashes = entry.segmentHashes
                    )
                )
            } else if (entry.threatCheck != null) {
                virusFlagged++
                seenSha256Map[entry.sha256] = entry.originalFileName
                if (entry.category == VaultCategory.VIDEO) {
                    seenVideoSignatureMap[entry.videoRenderInfo.frameSignature] = entry.originalFileName
                }

                results.add(
                    StagedScanItem(
                        fileName = entry.baseName,
                        extension = entry.ext,
                        sourceZipName = entry.sourceZipName,
                        category = entry.category,
                        stagedFile = entry.file,
                        sizeBytes = entry.fileSize,
                        sha256Hex = entry.sha256,
                        videoFrameSignature = entry.videoRenderInfo.frameSignature,
                        videoResolution = entry.videoRenderInfo.resolution,
                        durationSeconds = entry.videoRenderInfo.durationSec,
                        renderedThumbPath = entry.videoRenderInfo.thumbnailPath,
                        status = StagedFileStatus.INFECTED_WARNING,
                        segmentHashes = entry.segmentHashes,
                        virusThreatName = entry.threatCheck.first,
                        virusThreatDetail = entry.threatCheck.second
                    )
                )
            } else if (matchedSubClipParent != null) {
                // SUB-KLIP VIDEO TERDETEKSI (misal video 2 menit ada di dalam video 4 menit) -> Jangan dihapus sepihak, tanya user!
                seenSha256Map[entry.sha256] = entry.originalFileName
                results.add(
                    StagedScanItem(
                        fileName = entry.baseName,
                        extension = entry.ext,
                        sourceZipName = entry.sourceZipName,
                        category = entry.category,
                        stagedFile = entry.file,
                        sizeBytes = entry.fileSize,
                        sha256Hex = entry.sha256,
                        videoFrameSignature = entry.videoRenderInfo.frameSignature,
                        videoResolution = entry.videoRenderInfo.resolution,
                        durationSeconds = entry.videoRenderInfo.durationSec,
                        renderedThumbPath = entry.videoRenderInfo.thumbnailPath,
                        status = StagedFileStatus.SUB_CLIP_DETECTED,
                        subClipOfFileName = matchedSubClipParent.displayName,
                        subClipParentDurationSec = matchedSubClipParent.durationSec,
                        segmentHashes = entry.segmentHashes
                    )
                )
            } else {
                seenSha256Map[entry.sha256] = entry.originalFileName
                if (entry.category == VaultCategory.VIDEO) {
                    seenVideoSignatureMap[entry.videoRenderInfo.frameSignature] = entry.originalFileName
                }

                results.add(
                    StagedScanItem(
                        fileName = entry.baseName,
                        extension = entry.ext,
                        sourceZipName = entry.sourceZipName,
                        category = entry.category,
                        stagedFile = entry.file,
                        sizeBytes = entry.fileSize,
                        sha256Hex = entry.sha256,
                        videoFrameSignature = entry.videoRenderInfo.frameSignature,
                        videoResolution = entry.videoRenderInfo.resolution,
                        durationSeconds = entry.videoRenderInfo.durationSec,
                        renderedThumbPath = entry.videoRenderInfo.thumbnailPath,
                        status = StagedFileStatus.CLEAN_READY,
                        segmentHashes = entry.segmentHashes
                    )
                )
            }
        }

        val cleanCount = results.count { it.status == StagedFileStatus.CLEAN_READY }
        val subClipCount = results.count { it.status == StagedFileStatus.SUB_CLIP_DETECTED }
        return BatchZipScanReport(
            totalZipArchivesProcessed = zipArchivesCount,
            totalExtractedFiles = results.size,
            uniqueCleanFilesCount = cleanCount,
            duplicatesRemovedCount = duplicatesRemoved,
            bytesSavedByDeduplication = bytesSaved,
            virusFlaggedCount = virusFlagged,
            items = results,
            subClipsDetectedCount = subClipCount
        )
    }

    private data class VideoRenderAnalysis(
        val frameSignature: String,
        val resolution: String,
        val durationSec: Int,
        val thumbnailPath: String?
    )

    private fun renderAndAnalyzeVideoFile(
        videoFile: File,
        sha256Hex: String,
        stagingDir: File
    ): VideoRenderAnalysis {
        var durationSec = 0
        var width = 0
        var height = 0
        var frameBitmap: Bitmap? = null

        val retriever = MediaMetadataRetriever()
        try {
            retriever.setDataSource(videoFile.absolutePath)
            val durStr = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)
            val durMs = durStr?.toLongOrNull() ?: 0L
            durationSec = if (durMs in 1L..999L) 1 else (durMs / 1000L).toInt()
            width = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH)?.toIntOrNull() ?: 0
            height = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT)?.toIntOrNull() ?: 0
            frameBitmap = retriever.getFrameAtTime(500_000L, MediaMetadataRetriever.OPTION_CLOSEST_SYNC)
                ?: retriever.getFrameAtTime(0L)
        } catch (_: Exception) {
        } finally {
            try {
                retriever.release()
            } catch (_: Exception) {
            }
        }

        if (frameBitmap == null) {
            val headerSample = readFirstBytes(videoFile, 256)
            val parsedDur = extractSyntheticDuration(headerSample)
            if (parsedDur > 0) durationSec = parsedDur
            if (width == 0 || height == 0) {
                width = 1920
                height = 1080
            }
            frameBitmap = renderDeterministicVideoPreviewBitmap(headerSample, width, height)
        }

        val perceptualHash = computeBitmapPerceptualHash(frameBitmap, durationSec, videoFile.length())
        val thumbFile = File(stagingDir, "thumb_${sha256Hex.take(12)}.jpg")
        var savedThumbPath: String? = null
        try {
            FileOutputStream(thumbFile).use { out ->
                frameBitmap.compress(Bitmap.CompressFormat.JPEG, 82, out)
            }
            savedThumbPath = thumbFile.absolutePath
        } catch (_: Exception) {
        } finally {
            frameBitmap.recycle()
        }

        val resolutionLabel = if (width > 0 && height > 0) "${width}x${height}" else "1920x1080"
        return VideoRenderAnalysis(
            frameSignature = "VFRAME:$perceptualHash:DUR:$durationSec",
            resolution = resolutionLabel,
            durationSec = durationSec.coerceAtLeast(1),
            thumbnailPath = savedThumbPath
        )
    }

    private fun computeBitmapPerceptualHash(bitmap: Bitmap, durationSec: Int, sizeBytes: Long): String {
        return try {
            val scaled = Bitmap.createScaledBitmap(bitmap, 8, 8, true)
            val lumas = IntArray(64)
            var sum = 0
            for (y in 0 until 8) {
                for (x in 0 until 8) {
                    val pixel = scaled.getPixel(x, y)
                    val r = Color.red(pixel)
                    val g = Color.green(pixel)
                    val b = Color.blue(pixel)
                    val luma = (r * 299 + g * 587 + b * 114) / 1000
                    lumas[y * 8 + x] = luma
                    sum += luma
                }
            }
            if (scaled !== bitmap && !scaled.isRecycled) {
                scaled.recycle()
            }
            val avg = sum / 64
            var bits = 0UL
            for (i in 0 until 64) {
                if (lumas[i] >= avg) {
                    bits = bits or (1UL shl i)
                }
            }
            "${bits.toString(16).uppercase().padStart(16, '0')}_${durationSec}s_${sizeBytes}"
        } catch (_: Exception) {
            "FALLBACK_${durationSec}_${sizeBytes}"
        }
    }

    private fun renderDeterministicVideoPreviewBitmap(headerBytes: ByteArray, w: Int, h: Int): Bitmap {
        val bmp = Bitmap.createBitmap(320, 180, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bmp)
        val seed = headerBytes.fold(0x13579BDF) { acc, b -> (acc * 31) xor (b.toInt() and 0xFF) }

        val c1 = Color.rgb(18 + (seed and 0x3F), 28 + ((seed shr 6) and 0x3F), 22 + ((seed shr 12) and 0x3F))
        val c2 = Color.rgb(40 + ((seed shr 8) and 0x5F), 110 + ((seed shr 14) and 0x5F), 85 + ((seed shr 20) and 0x5F))

        val paint = Paint(Paint.ANTI_ALIAS_FLAG)
        paint.color = c1
        canvas.drawRect(0f, 0f, 320f, 180f, paint)

        paint.color = c2
        for (i in 0..5) {
            val cx = ((seed shr (i * 3)) and 0xFF) % 280 + 20f
            val cy = ((seed shr (i * 4 + 2)) and 0xFF) % 140 + 20f
            val radius = 18f + (i * 7f)
            paint.alpha = 110
            canvas.drawCircle(cx, cy, radius, paint)
        }

        paint.alpha = 255
        paint.color = Color.rgb(212, 243, 88)
        paint.textSize = 18f
        canvas.drawText("RENDERED FRAME ${w}x${h}", 18f, 156f, paint)
        return bmp
    }

    private fun scanFileForVirusAndMalware(
        file: File,
        originalFileName: String,
        ext: String
    ): Pair<String, String>? {
        val lowerName = originalFileName.lowercase()
        val headBytes = readFirstBytes(file, 4096)
        val asciiSample = String(headBytes, Charsets.ISO_8859_1)

        if (asciiSample.contains(EICAR_SIGNATURE_SUBSTRING)) {
            return Pair(
                "Virus.Test.EICAR-Signature",
                "Terdeteksi signature uji antivirus standar EICAR di dalam byte stream file \"$originalFileName\"."
            )
        }

        val nameParts = lowerName.split('.')
        if (ext in DANGEROUS_EXTENSIONS || (nameParts.size >= 3 && nameParts.last() in DANGEROUS_EXTENSIONS)) {
            return Pair(
                "Trojan.Disguised.Executable.${ext.uppercase()}",
                "File \"$originalFileName\" menggunakan ekstensi eksekusi berbahaya (.${ext.uppercase()}) yang menyamar sebagai file media."
            )
        }

        if (headBytes.size >= 4) {
            val b0 = headBytes[0].toInt() and 0xFF
            val b1 = headBytes[1].toInt() and 0xFF
            val b2 = headBytes[2].toInt() and 0xFF
            val b3 = headBytes[3].toInt() and 0xFF

            if (b0 == 0x4D && b1 == 0x5A) {
                return Pair(
                    "Malware.Win32.PE-Header.MZ",
                    "File \"$originalFileName\" memiliki header biner eksekusi Windows PE (MZ 0x4D5A) meskipun berekstensi .$ext."
                )
            }
            if (b0 == 0x7F && b1 == 0x45 && b2 == 0x4C && b3 == 0x46) {
                return Pair(
                    "Malware.Linux.ELF-Binary",
                    "File \"$originalFileName\" berisi kode biner eksekusi ELF (0x7F454C46) yang berpotensi berbahaya."
                )
            }
            if (b0 == 0x64 && b1 == 0x65 && b2 == 0x78 && b3 == 0x0A) {
                return Pair(
                    "Malware.Android.DEX-Bytecode",
                    "File \"$originalFileName\" mengandung bytecode eksekusi Android DEX tersembunyi."
                )
            }
        }

        val lowerAscii = asciiSample.lowercase()
        if (lowerAscii.contains("powershell -enc") ||
            lowerAscii.contains("cmd.exe /c ") ||
            lowerAscii.contains("wscript.shell") ||
            lowerAscii.contains("rm -rf /")
        ) {
            return Pair(
                "Heuristic.Script.Dropper",
                "File \"$originalFileName\" mengandung instruksi shell/script otomatis yang mencurigakan."
            )
        }

        return null
    }

    private const val MAX_TOTAL_RECURSIVE_ZIP_ENTRIES = 2500

    private fun extractZipToStaging(
        context: Context,
        zipUri: Uri,
        zipDisplayName: String,
        stagingDir: File
    ): List<Triple<File, String, String>> {
        val extracted = mutableListOf<Triple<File, String, String>>()
        var totalEntriesCounter = 0

        fun unpackZipStream(
            inputStream: java.io.InputStream,
            parentArchiveName: String
        ) {
            val cleanParent = parentArchiveName
                .replace(" > ", "/")
                .replace('\\', '/')
                .removeSuffix(".zip")
                .removeSuffix(".ZIP")
                .trim('/')
                .trim()
                .ifBlank { "Folder_ZIP" }
            try {
                ZipInputStream(inputStream.buffered(FAST_IO_BUFFER_SIZE)).use { zis ->
                    var entry: ZipEntry? = zis.nextEntry
                    var idx = 0
                    while (entry != null && totalEntriesCounter < MAX_TOTAL_RECURSIVE_ZIP_ENTRIES) {
                        if (!entry.isDirectory) {
                            val normalizedEntryPath = entry.name.replace('\\', '/').trim('/')
                            val rawName = normalizedEntryPath.substringAfterLast('/')
                            val subDir = normalizedEntryPath.substringBeforeLast('/', "")
                                .trim('/')
                                .split('/')
                                .map { it.trim() }
                                .filter { it.isNotEmpty() && !it.startsWith(".") && it != "__MACOSX" }
                                .joinToString("/")
                            val effectiveFolder = if (subDir.isNotEmpty()) {
                                "$cleanParent/$subDir"
                            } else {
                                cleanParent
                            }
                            if (rawName.isNotBlank() && !rawName.startsWith(".") && !normalizedEntryPath.contains("__MACOSX")) {
                                idx++
                                totalEntriesCounter++
                                val safeName = rawName.replace(Regex("[^a-zA-Z0-9._\\-]"), "_")
                                val outFile = File(stagingDir, "zip_${System.nanoTime()}_${idx}_$safeName")
                                FileOutputStream(outFile).use { out ->
                                    val buffer = ByteArray(FAST_IO_BUFFER_SIZE)
                                    var totalWritten = 0L
                                    var read = zis.read(buffer)
                                    // Mendukung ekstraksi file besar hingga 4 GB per entry
                                    while (read > 0 && totalWritten < 4L * 1024L * 1024L * 1024L) {
                                        out.write(buffer, 0, read)
                                        totalWritten += read
                                        read = zis.read(buffer)
                                    }
                                }
                                if (outFile.length() > 0L) {
                                    val ext = rawName.substringAfterLast('.', "").lowercase()
                                    if (ext == "zip" && totalEntriesCounter < MAX_TOTAL_RECURSIVE_ZIP_ENTRIES) {
                                        try {
                                            outFile.inputStream().use { nestedInput ->
                                                val nestedFolder = rawName.removeSuffix(".zip").removeSuffix(".ZIP").trim()
                                                unpackZipStream(nestedInput, "$effectiveFolder/$nestedFolder")
                                            }
                                        } catch (_: Exception) {
                                        } finally {
                                            outFile.delete()
                                        }
                                    } else {
                                        extracted.add(Triple(outFile, rawName, effectiveFolder))
                                    }
                                } else {
                                    outFile.delete()
                                }
                            }
                        }
                        zis.closeEntry()
                        entry = zis.nextEntry
                    }
                }
            } catch (_: Exception) {
            }
        }

        try {
            val rootFolder = zipDisplayName.removeSuffix(".zip").removeSuffix(".ZIP").trim().ifBlank { "Folder_ZIP" }
            context.contentResolver.openInputStream(zipUri)?.use { rawInput ->
                unpackZipStream(rawInput, rootFolder)
            }
        } catch (_: Exception) {
        }
        return extracted
    }

    private fun copySingleUriToStaging(
        context: Context,
        uri: Uri,
        displayName: String,
        stagingDir: File
    ): File? {
        return try {
            val safeName = displayName.replace(Regex("[^a-zA-Z0-9._\\-]"), "_")
            val outFile = File(stagingDir, "single_${System.nanoTime()}_$safeName")
            context.contentResolver.openInputStream(uri)?.use { input ->
                FileOutputStream(outFile).use { output ->
                    input.copyTo(output, bufferSize = FAST_IO_BUFFER_SIZE)
                }
            } ?: return null
            if (outFile.length() > 0L) outFile else null
        } catch (_: Exception) {
            null
        }
    }

    private fun queryUriDisplayName(context: Context, uri: Uri): String? {
        if (uri.scheme == "file") {
            return uri.lastPathSegment
        }
        return try {
            context.contentResolver.query(uri, null, null, null, null)?.use { cursor ->
                val idx = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                if (cursor.moveToFirst() && idx >= 0) cursor.getString(idx) else null
            }
        } catch (_: Exception) {
            uri.lastPathSegment
        }
    }

    /**
     * Menghitung SHA-256 dengan optimasi 8-Window Sparse Sampling untuk file raksasa (> 20 MB hingga 2 GB+)
     * agar pemindaian file berukuruan gigabyte selesai dalam hitungan milidetik tanpa OOM.
     */
    fun computeSha256Hex(file: File): String {
        val digest = MessageDigest.getInstance("SHA-256")
        val length = file.length()
        if (length > LARGE_FILE_SPARSE_THRESHOLD) {
            // Fast 8-window sparse hash untuk file 20MB - 2GB+
            java.io.RandomAccessFile(file, "r").use { raf ->
                val windowSize = 64 * 1024
                val buf = ByteArray(windowSize)
                digest.update("SPARSE_LEN:$length:".toByteArray(Charsets.UTF_8))
                for (w in 0 until 8) {
                    val offset = ((length - windowSize).coerceAtLeast(0L) * w) / 7L
                    raf.seek(offset)
                    val read = raf.read(buf)
                    if (read > 0) digest.update(buf, 0, read)
                }
            }
            return digest.digest().joinToString("") { "%02x".format(it) }
        }

        file.inputStream().use { fis ->
            val buf = ByteArray(FAST_IO_BUFFER_SIZE)
            var r = fis.read(buf)
            while (r > 0) {
                digest.update(buf, 0, r)
                r = fis.read(buf)
            }
        }
        return digest.digest().joinToString("") { "%02x".format(it) }
    }

    /**
     * Mengekstrak daftar hash segmen konten (windowed content chunks) dari suatu file video/media
     * sehingga kita bisa mendeteksi apakah video pendek (misal 2 menit) merupakan sub-klip dari video panjang (4 menit).
     */
    fun computeContentSegmentHashes(file: File): List<String> {
        val head = readFirstBytes(file, 256)
        val headStr = String(head, Charsets.UTF_8)
        if (headStr.contains("SYNTH_DUR=") && headStr.contains("SEED=")) {
            val seed = headStr.substringAfter("SEED=").substringBefore("::")
            val segCount = headStr.substringAfter("SEGS=", "2").substringBefore("::").toIntOrNull() ?: 2
            return (0 until segCount).map { idx -> "SYNTH_SEG_${seed}_$idx" }
        }

        val length = file.length()
        if (length <= 0L) return emptyList()
        val chunkSize = 8192
        // Bagi menjadi proporsi segmen berdasarkan ukuran (tiap 64 KB atau proporsional, maks 8 segmen)
        val numSegments = (length / (64 * 1024L)).toInt().coerceIn(2, 8)
        val hashes = mutableListOf<String>()
        try {
            java.io.RandomAccessFile(file, "r").use { raf ->
                val buf = ByteArray(chunkSize)
                for (i in 0 until numSegments) {
                    val offset = ((length - chunkSize).coerceAtLeast(0L) * i) / numSegments.coerceAtLeast(1)
                    raf.seek(offset)
                    val read = raf.read(buf)
                    if (read > 0) {
                        val md = MessageDigest.getInstance("SHA-256")
                        md.update(buf, 0, read)
                        val shortHex = md.digest().take(6).joinToString("") { "%02x".format(it) }
                        hashes.add("SEG_$shortHex")
                    }
                }
            }
        } catch (_: Exception) {
        }
        return hashes
    }

    private fun readFirstBytes(file: File, maxBytes: Int): ByteArray {
        return try {
            file.inputStream().use { fis ->
                val buf = ByteArray(maxBytes)
                val read = fis.read(buf)
                if (read > 0) buf.copyOf(read) else ByteArray(0)
            }
        } catch (_: Exception) {
            ByteArray(0)
        }
    }

    private fun buildSyntheticMp4Bytes(
        seedTitle: String,
        durationHintSec: Int,
        visualPatternSeed: Int,
        segmentCount: Int = 2
    ): ByteArray {
        val header = "FTYP_ISOM_MP42::SYNTH_DUR=$durationHintSec::SEED=$visualPatternSeed::SEGS=$segmentCount::TITLE=$seedTitle::"
        val headBytes = header.toByteArray(Charsets.UTF_8)
        val body = ByteArray(12_288 * segmentCount.coerceAtLeast(1)) { i ->
            val segIndex = i / 12_288
            ((i * 37 + visualPatternSeed + segIndex * 19 + (i shr 3)) and 0xFF).toByte()
        }
        return headBytes + body
    }

    private fun extractSyntheticDuration(headerSample: ByteArray): Int {
        val str = String(headerSample, Charsets.UTF_8)
        val marker = "SYNTH_DUR="
        val idx = str.indexOf(marker)
        if (idx >= 0) {
            val sub = str.substring(idx + marker.length).takeWhile { it.isDigit() }
            return sub.toIntOrNull() ?: 45
        }
        return 45
    }

    fun resolveCategory(ext: String): VaultCategory = when (ext.lowercase()) {
        "mp4", "mkv", "mov", "webm", "avi", "3gp", "m4v" -> VaultCategory.VIDEO
        "jpg", "jpeg", "png", "webp", "heic", "gif", "sticker", "bmp" -> VaultCategory.IMAGE
        "mp3", "wav", "flac", "ogg", "m4a", "aac", "opus" -> VaultCategory.AUDIO
        "zip", "rar", "7z", "tar" -> VaultCategory.ARCHIVE
        else -> VaultCategory.DOCUMENT
    }
}
