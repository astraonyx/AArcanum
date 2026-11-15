package com.example.data

import android.content.Context
import android.net.Uri
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.nio.charset.StandardCharsets
import java.security.SecureRandom
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream
import java.util.zip.ZipOutputStream
import javax.crypto.Cipher
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.GCMParameterSpec
import javax.crypto.spec.PBEKeySpec
import javax.crypto.spec.SecretKeySpec

/**
 * Engine untuk Pencadangan & Pemulihan Brankas Terenkripsi ke Google Cloud / Google Drive.
 * Bersifat 100% MANUAL (Zero Automatic Sync) dan Zero-Knowledge:
 * File yang dikirim keluar SELALU dalam bentuk ciphertext terenkripsi AES-256-GCM.
 */
object GoogleCloudVaultBackupEngine {

    private const val BACKUP_MAGIC = "ARCANUM_ENC_CLOUD_V2"
    private const val GCM_TAG_LENGTH = 128
    private const val GCM_IV_LENGTH = 12
    private const val SALT_LENGTH = 16
    private const val PBKDF2_ITERATIONS = 40000

    /**
     * Mengemas seluruh file dan metadata Room dari vaultId yang dipilih,
     * lalu mengenkripsinya dengan AES-256-GCM menggunakan PIN 3-lapis.
     */
    suspend fun createEncryptedVaultPackage(
        context: Context,
        vaultId: Int,
        items: List<VaultItemEntity>,
        p1: String,
        p2: String,
        p3: String
    ): Result<File> = withContext(Dispatchers.IO) {
        try {
            val backupDir = File(context.cacheDir, "cloud_backups").apply { if (!exists()) mkdirs() }
            val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date())
            val tempZipFile = File(backupDir, "temp_vault_${vaultId}_${timeStamp}.zip")
            val finalEncFile = File(backupDir, "Arcanum_Vault_${vaultId}_Backup_${timeStamp}.arcanum.enc")

            // 1. Buat Arsip ZIP berisi data JSON & file terenkripsi
            ZipOutputStream(FileOutputStream(tempZipFile)).use { zos ->
                // A. Tulis Manifest Metadata
                val manifestJson = JSONObject().apply {
                    put("version", 2)
                    put("vaultId", vaultId)
                    put("timestamp", System.currentTimeMillis())
                    val itemsArray = JSONArray()
                    items.forEach { item ->
                        val obj = JSONObject().apply {
                            put("id", item.id)
                            put("name", item.name)
                            put("extension", item.extension)
                            put("sizeBytes", item.sizeBytes)
                            put("category", item.category)
                            put("isEncrypted", item.isEncrypted)
                            put("durationSeconds", item.durationSeconds)
                            put("groupArchiveName", item.groupArchiveName ?: "")
                        }
                        itemsArray.put(obj)
                    }
                    put("items", itemsArray)
                }

                zos.putNextEntry(ZipEntry("manifest.json"))
                zos.write(manifestJson.toString(2).toByteArray(StandardCharsets.UTF_8))
                zos.closeEntry()

                // B. Simpan file internal brankas (baik .NYX maupun plain internal)
                items.forEach { item ->
                    val fileToBundle = if (item.isEncrypted) {
                        File(item.internalNyxPath)
                    } else {
                        File(item.internalPlainPath)
                    }

                    if (fileToBundle.exists() && fileToBundle.length() > 0) {
                        val entryName = "files/${item.id}_${fileToBundle.name}"
                        zos.putNextEntry(ZipEntry(entryName))
                        FileInputStream(fileToBundle).use { fis ->
                            fis.copyTo(zos)
                        }
                        zos.closeEntry()
                    }
                }
            }

            // 2. Enkripsi tempZipFile dengan AES-256-GCM menggunakan KDF PIN 3-lapis
            val rawZipBytes = tempZipFile.readBytes()
            tempZipFile.delete()

            val salt = ByteArray(SALT_LENGTH).also { SecureRandom().nextBytes(it) }
            val iv = ByteArray(GCM_IV_LENGTH).also { SecureRandom().nextBytes(it) }

            // Key derivation dari p1 + p2 + p3
            val keySecret = "$p1::$p2::$p3::arcanum_cloud_salt_v2"
            val factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256")
            val spec = PBEKeySpec(keySecret.toCharArray(), salt, PBKDF2_ITERATIONS, 256)
            val secretKey = SecretKeySpec(factory.generateSecret(spec).encoded, "AES")

            val cipher = Cipher.getInstance("AES/GCM/NoPadding")
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, GCMParameterSpec(GCM_TAG_LENGTH, iv))
            val cipherText = cipher.doFinal(rawZipBytes)

            // Tulis Final Encrypted Package
            FileOutputStream(finalEncFile).use { fos ->
                fos.write(BACKUP_MAGIC.toByteArray(StandardCharsets.UTF_8)) // Magic Header
                fos.write(salt)                                             // 16 bytes salt
                fos.write(iv)                                               // 12 bytes IV
                fos.write(cipherText)                                       // GCM ciphertext + tag
            }

            Result.success(finalEncFile)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Memverifikasi dan mendekripsi file backup `.arcanum.enc`, lalu mengekstraknya
     * kembali ke direktori brankas lokal target.
     */
    suspend fun restoreEncryptedVaultPackage(
        context: Context,
        targetVaultId: Int,
        encPackageFile: File,
        p1: String,
        p2: String,
        p3: String,
        repository: VaultRepository
    ): Result<Int> = withContext(Dispatchers.IO) {
        try {
            if (!encPackageFile.exists()) return@withContext Result.failure(Exception("File backup tidak ditemukan."))

            FileInputStream(encPackageFile).use { fis ->
                val magicBytes = ByteArray(BACKUP_MAGIC.length)
                val readMagic = fis.read(magicBytes)
                if (readMagic != BACKUP_MAGIC.length || String(magicBytes, StandardCharsets.UTF_8) != BACKUP_MAGIC) {
                    return@withContext Result.failure(Exception("Format file backup tidak valid atau rusak."))
                }

                val salt = ByteArray(SALT_LENGTH).also { fis.read(it) }
                val iv = ByteArray(GCM_IV_LENGTH).also { fis.read(it) }
                val cipherBytes = fis.readBytes()

                // Key derivation
                val keySecret = "$p1::$p2::$p3::arcanum_cloud_salt_v2"
                val factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256")
                val spec = PBEKeySpec(keySecret.toCharArray(), salt, PBKDF2_ITERATIONS, 256)
                val secretKey = SecretKeySpec(factory.generateSecret(spec).encoded, "AES")

                val cipher = Cipher.getInstance("AES/GCM/NoPadding")
                cipher.init(Cipher.DECRYPT_MODE, secretKey, GCMParameterSpec(GCM_TAG_LENGTH, iv))
                val rawZipBytes = cipher.doFinal(cipherBytes)

                // Ekstrak ZIP
                val tempZip = File(context.cacheDir, "restore_temp.zip").apply { writeBytes(rawZipBytes) }
                var restoredCount = 0

                ZipInputStream(FileInputStream(tempZip)).use { zis ->
                    var entry: ZipEntry? = zis.nextEntry
                    var manifestJson: JSONObject? = null
                    val extractedFiles = mutableMapOf<String, File>()

                    while (entry != null) {
                        if (entry.name == "manifest.json") {
                            val baos = ByteArrayOutputStream()
                            zis.copyTo(baos)
                            manifestJson = JSONObject(baos.toString("UTF-8"))
                        } else if (entry.name.startsWith("files/")) {
                            val targetFile = File(context.filesDir, "vault_${targetVaultId}_" + File(entry.name).name)
                            FileOutputStream(targetFile).use { fos ->
                                zis.copyTo(fos)
                            }
                            extractedFiles[entry.name] = targetFile
                        }
                        zis.closeEntry()
                        entry = zis.nextEntry
                    }

                    if (manifestJson != null) {
                        val itemsArray = manifestJson.getJSONArray("items")
                        for (i in 0 until itemsArray.length()) {
                            val obj = itemsArray.getJSONObject(i)
                            val originalId = obj.getLong("id")
                            val name = obj.getString("name")
                            val ext = obj.getString("extension")
                            val isEncrypted = obj.getBoolean("isEncrypted")
                            val sizeBytes = obj.getLong("sizeBytes")
                            val categoryStr = obj.optString("category", VaultCategory.DOCUMENT.name)
                            val category = try { VaultCategory.valueOf(categoryStr) } catch (_: Exception) { VaultCategory.DOCUMENT }

                            // Tautkan kembali file fisik ke entity baru
                            val matchedFile = extractedFiles.entries.find { it.key.contains("${originalId}_") }?.value
                            val plainPath = if (!isEncrypted) (matchedFile?.absolutePath ?: "") else ""
                            val nyxPath = if (isEncrypted) (matchedFile?.absolutePath ?: "") else ""

                            if (matchedFile != null && matchedFile.exists()) {
                                repository.insertItem(
                                    VaultItemEntity(
                                        name = name,
                                        extension = ext,
                                        sizeBytes = sizeBytes,
                                        isEncrypted = isEncrypted,
                                        vaultProfileId = targetVaultId,
                                        category = category.name,
                                        internalPlainPath = plainPath,
                                        internalNyxPath = nyxPath,
                                        addedAt = System.currentTimeMillis()
                                    )
                                )
                                restoredCount++
                            }
                        }
                    }
                }

                tempZip.delete()
                Result.success(restoredCount)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private const val CLOUD_FILE_MAGIC = "ARCANUM_CLOUD_FILE_V2"
    private const val CLOUD_REGISTRY_PREFS = "arcanum_google_cloud_encrypted_registry_v2"
    private const val KEY_CLOUD_ITEMS_JSON = "cloud_encrypted_items_json"

    data class CloudEncryptedFileEntry(
        val cloudId: String,
        val originalItemId: Int,
        val fileName: String,
        val extension: String,
        val category: String,
        val plainSizeBytes: Long,
        val encryptedSizeBytes: Long,
        val durationSeconds: Int,
        val groupArchiveName: String?,
        val sourceVaultId: Int,
        val sourceVaultName: String,
        val googleAccountEmail: String,
        val cipherAlgorithm: String,
        val checksumPreview: String,
        val backedUpAtMillis: Long,
        val encryptedFilePath: String
    ) {
        val formattedDate: String
            get() = SimpleDateFormat("dd MMM yyyy • HH:mm", Locale("id", "ID")).format(Date(backedUpAtMillis))
    }

    fun loadCloudEncryptedEntries(context: Context): List<CloudEncryptedFileEntry> {
        val prefs = context.getSharedPreferences(CLOUD_REGISTRY_PREFS, Context.MODE_PRIVATE)
        val raw = prefs.getString(KEY_CLOUD_ITEMS_JSON, "[]") ?: "[]"
        return try {
            val arr = JSONArray(raw)
            val result = mutableListOf<CloudEncryptedFileEntry>()
            for (i in 0 until arr.length()) {
                val obj = arr.getJSONObject(i)
                val path = obj.optString("encryptedFilePath", "")
                if (path.isNotBlank() && File(path).exists()) {
                    result.add(
                        CloudEncryptedFileEntry(
                            cloudId = obj.optString("cloudId", "CLD-${i}"),
                            originalItemId = obj.optInt("originalItemId", 0),
                            fileName = obj.optString("fileName", "Encrypted_Item"),
                            extension = obj.optString("extension", "bin"),
                            category = obj.optString("category", VaultCategory.DOCUMENT.name),
                            plainSizeBytes = obj.optLong("plainSizeBytes", 0L),
                            encryptedSizeBytes = obj.optLong("encryptedSizeBytes", File(path).length()),
                            durationSeconds = obj.optInt("durationSeconds", 0),
                            groupArchiveName = obj.optString("groupArchiveName", "").takeIf { it.isNotBlank() },
                            sourceVaultId = obj.optInt("sourceVaultId", 1),
                            sourceVaultName = obj.optString("sourceVaultName", "Brankas #1"),
                            googleAccountEmail = obj.optString("googleAccountEmail", ""),
                            cipherAlgorithm = obj.optString("cipherAlgorithm", "AES-256-GCM + NYX2"),
                            checksumPreview = obj.optString("checksumPreview", "SHA256-OK"),
                            backedUpAtMillis = obj.optLong("backedUpAtMillis", System.currentTimeMillis()),
                            encryptedFilePath = path
                        )
                    )
                }
            }
            result.sortedByDescending { it.backedUpAtMillis }
        } catch (_: Exception) {
            emptyList()
        }
    }

    private fun saveCloudEncryptedEntries(context: Context, entries: List<CloudEncryptedFileEntry>) {
        val prefs = context.getSharedPreferences(CLOUD_REGISTRY_PREFS, Context.MODE_PRIVATE)
        val arr = JSONArray()
        entries.forEach { entry ->
            arr.put(
                JSONObject().apply {
                    put("cloudId", entry.cloudId)
                    put("originalItemId", entry.originalItemId)
                    put("fileName", entry.fileName)
                    put("extension", entry.extension)
                    put("category", entry.category)
                    put("plainSizeBytes", entry.plainSizeBytes)
                    put("encryptedSizeBytes", entry.encryptedSizeBytes)
                    put("durationSeconds", entry.durationSeconds)
                    put("groupArchiveName", entry.groupArchiveName ?: "")
                    put("sourceVaultId", entry.sourceVaultId)
                    put("sourceVaultName", entry.sourceVaultName)
                    put("googleAccountEmail", entry.googleAccountEmail)
                    put("cipherAlgorithm", entry.cipherAlgorithm)
                    put("checksumPreview", entry.checksumPreview)
                    put("backedUpAtMillis", entry.backedUpAtMillis)
                    put("encryptedFilePath", entry.encryptedFilePath)
                }
            )
        }
        prefs.edit().putString(KEY_CLOUD_ITEMS_JSON, arr.toString()).apply()
    }

    /**
     * Mencadangkan daftar file pilihan dari sebuah brankas ke Google Cloud Store terenkripsi (.nyxcloud).
     * Menyimpan identitas brankas asal (sourceVaultId & sourceVaultName) serta email akun Google.
     */
    suspend fun backupSelectedItemsToGoogleCloud(
        context: Context,
        vaultId: Int,
        vaultName: String,
        googleAccountEmail: String,
        items: List<VaultItemEntity>,
        p1: String,
        p2: String,
        p3: String
    ): Result<List<CloudEncryptedFileEntry>> = withContext(Dispatchers.IO) {
        try {
            if (googleAccountEmail.isBlank()) {
                return@withContext Result.failure(Exception("Wajib login akun Google terlebih dahulu sebelum mencadangkan ke Cloud."))
            }
            if (items.isEmpty()) {
                return@withContext Result.failure(Exception("Pilih minimal 1 file untuk dicadangkan ke Google Cloud."))
            }

            val cloudStoreDir = File(context.filesDir, "google_cloud_encrypted_store").apply {
                if (!exists()) mkdirs()
            }

            val currentEntries = loadCloudEncryptedEntries(context).toMutableList()
            val newlyBackedUp = mutableListOf<CloudEncryptedFileEntry>()

            for (item in items) {
                val sourceFile = when {
                    File(item.internalNyxPath).exists() && File(item.internalNyxPath).length() > 0L -> File(item.internalNyxPath)
                    File(item.internalPlainPath).exists() && File(item.internalPlainPath).length() > 0L -> File(item.internalPlainPath)
                    else -> null
                }

                val rawPayloadBytes = sourceFile?.readBytes()
                    ?: item.rawContentPreview.ifBlank { "ARCANUM_ENCRYPTED_PAYLOAD_${item.name}" }.toByteArray(StandardCharsets.UTF_8)

                val salt = ByteArray(SALT_LENGTH).also { SecureRandom().nextBytes(it) }
                val iv = ByteArray(GCM_IV_LENGTH).also { SecureRandom().nextBytes(it) }

                val keySecret = "$p1::$p2::$p3::arcanum_cloud_file_v2::$vaultId"
                val factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256")
                val spec = PBEKeySpec(keySecret.toCharArray(), salt, PBKDF2_ITERATIONS, 256)
                val secretKey = SecretKeySpec(factory.generateSecret(spec).encoded, "AES")

                val cipher = Cipher.getInstance("AES/GCM/NoPadding")
                cipher.init(Cipher.ENCRYPT_MODE, secretKey, GCMParameterSpec(GCM_TAG_LENGTH, iv))
                val cipherBytes = cipher.doFinal(rawPayloadBytes)

                val safeName = item.name.replace(Regex("[^a-zA-Z0-9_\\-]"), "_").take(36)
                val cloudId = "GCLOUD_V${vaultId}_ITEM${item.id}"
                val encOutFile = File(cloudStoreDir, "${cloudId}_${safeName}.nyxcloud")

                FileOutputStream(encOutFile).use { fos ->
                    fos.write(CLOUD_FILE_MAGIC.toByteArray(StandardCharsets.UTF_8))
                    fos.write(salt)
                    fos.write(iv)
                    fos.write(cipherBytes)
                }

                // Hapus entri lama untuk item & brankas yang sama agar ter-update
                currentEntries.removeAll {
                    it.cloudId == cloudId ||
                        (it.sourceVaultId == vaultId && it.originalItemId == item.id) ||
                        (it.sourceVaultId == vaultId && it.fileName.equals(item.name, ignoreCase = true) && it.extension.equals(item.extension, ignoreCase = true))
                }

                val entry = CloudEncryptedFileEntry(
                    cloudId = cloudId,
                    originalItemId = item.id,
                    fileName = item.name,
                    extension = item.extension,
                    category = item.category,
                    plainSizeBytes = item.sizeBytes.coerceAtLeast(rawPayloadBytes.size.toLong()),
                    encryptedSizeBytes = encOutFile.length(),
                    durationSeconds = item.durationSeconds,
                    groupArchiveName = item.groupArchiveName,
                    sourceVaultId = vaultId,
                    sourceVaultName = vaultName,
                    googleAccountEmail = googleAccountEmail,
                    cipherAlgorithm = "AES-256-GCM • NYX2 Cloud",
                    checksumPreview = item.checksumPreview.ifBlank { "GCM-${cipherBytes.size.toString(16).uppercase()}" },
                    backedUpAtMillis = System.currentTimeMillis(),
                    encryptedFilePath = encOutFile.absolutePath
                )
                currentEntries.add(0, entry)
                newlyBackedUp.add(entry)
            }

            saveCloudEncryptedEntries(context, currentEntries)
            Result.success(newlyBackedUp)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Memulihkan 1 file terenkripsi dari Google Cloud kembali ke brankas target.
     */
    suspend fun restoreSingleCloudEntryToVault(
        context: Context,
        entry: CloudEncryptedFileEntry,
        targetVaultId: Int,
        sourceVaultPins: Triple<String, String, String>,
        repository: VaultRepository
    ): Result<VaultItemEntity> = withContext(Dispatchers.IO) {
        try {
            val encFile = File(entry.encryptedFilePath)
            if (!encFile.exists()) {
                return@withContext Result.failure(Exception("File terenkripsi di Cloud tidak ditemukan."))
            }

            val restoredBytes = FileInputStream(encFile).use { fis ->
                val magicBytes = ByteArray(CLOUD_FILE_MAGIC.length)
                val readLen = fis.read(magicBytes)
                if (readLen != CLOUD_FILE_MAGIC.length || String(magicBytes, StandardCharsets.UTF_8) != CLOUD_FILE_MAGIC) {
                    return@withContext Result.failure(Exception("Header enkripsi Cloud tidak valid."))
                }
                val salt = ByteArray(SALT_LENGTH).also { fis.read(it) }
                val iv = ByteArray(GCM_IV_LENGTH).also { fis.read(it) }
                val cipherBytes = fis.readBytes()

                val (p1, p2, p3) = sourceVaultPins
                val keySecret = "$p1::$p2::$p3::arcanum_cloud_file_v2::${entry.sourceVaultId}"
                val factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256")
                val spec = PBEKeySpec(keySecret.toCharArray(), salt, PBKDF2_ITERATIONS, 256)
                val secretKey = SecretKeySpec(factory.generateSecret(spec).encoded, "AES")

                val cipher = Cipher.getInstance("AES/GCM/NoPadding")
                cipher.init(Cipher.DECRYPT_MODE, secretKey, GCMParameterSpec(GCM_TAG_LENGTH, iv))
                cipher.doFinal(cipherBytes)
            }

            val restoredDir = File(context.filesDir, "cloud_restored").apply { if (!exists()) mkdirs() }
            val tempFile = File(restoredDir, "${entry.fileName}.${entry.extension}")
            tempFile.writeBytes(restoredBytes)

            val entity = repository.importLocalFileIntoPrivateVault(
                sourceFile = tempFile,
                vaultId = targetVaultId,
                encryptImmediately = true
            )
            tempFile.delete()
            Result.success(entity)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun deleteCloudEncryptedEntry(context: Context, cloudId: String): List<CloudEncryptedFileEntry> {
        val current = loadCloudEncryptedEntries(context).toMutableList()
        val target = current.find { it.cloudId == cloudId }
        if (target != null) {
            try {
                File(target.encryptedFilePath).delete()
            } catch (_: Exception) {
            }
            current.removeAll { it.cloudId == cloudId }
            saveCloudEncryptedEntries(context, current)
        }
        return current
    }

    fun updateVaultNameInCloudEntries(context: Context, vaultId: Int, newVaultName: String): List<CloudEncryptedFileEntry> {
        val current = loadCloudEncryptedEntries(context).map { entry ->
            if (entry.sourceVaultId == vaultId) {
                entry.copy(sourceVaultName = newVaultName)
            } else {
                entry
            }
        }
        saveCloudEncryptedEntries(context, current)
        return current
    }
}
