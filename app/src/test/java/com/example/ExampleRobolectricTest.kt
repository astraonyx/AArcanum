package com.example

import android.app.Application
import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.MoriDownloaderEngine
import com.example.data.MoriPlatform
import com.example.ui.VaultViewModel
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

    @Test
    fun `read string from context`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val appName = context.getString(R.string.app_name)
        assertEquals("Arcanum", appName)
    }

    @Test
    fun `alphanumeric qwerty pin input preserves letters without turning into zero`() {
        val app = ApplicationProvider.getApplicationContext<Application>()
        val viewModel = VaultViewModel(app)
        viewModel.lockVaultNow()

        // Pressing 5 letters on the security gate keeps exact letters before the 6th char triggers auto-verify
        listOf('Q', 'W', 'E', 'R', 'T').forEach { ch ->
            viewModel.onPinCharPressed(ch)
        }
        val entered = viewModel.securityState.value.enteredPin
        assertEquals("QWERT", entered)
        assertFalse(entered.contains('0'))
    }

    @Test
    fun `mori v4_5_1 supports 17 platforms including MediaFire and batch url extraction`() {
        val supportedPlatforms = MoriPlatform.entries.filter { it != MoriPlatform.DIRECT_WEB }
        assertTrue(supportedPlatforms.size >= 17)
        assertEquals(
            MoriPlatform.MEDIAFIRE,
            MoriDownloaderEngine.detectPlatformFromUrl("https://www.mediafire.com/file/abc123xyz/archive.zip/file")
        )

        val batchInput = """
            https://www.tiktok.com/@user/video/123
            https://www.mediafire.com/file/xyz987/sample.mp4
            https://youtu.be/dQw4w9WgXcQ
        """.trimIndent()
        val urls = MoriDownloaderEngine.extractBatchUrls(batchInput, maxLimit = 50)
        assertEquals(3, urls.size)
        assertTrue(urls[1].contains("mediafire.com"))
    }

    @Test
    fun `mori v4_5_1 populates multi-file preview items and toggles sub-file selection`() {
        val app = ApplicationProvider.getApplicationContext<Application>()
        val viewModel = VaultViewModel(app)

        val meta = MoriDownloaderEngine.resolveUrlMetadata("https://www.mediafire.com/folder/mori451/Arcanum_Pack")
        val populated = MoriDownloaderEngine.populateDefaultSubFiles(meta)
        assertTrue(populated.subFiles.size >= 4)
        assertTrue(populated.subFiles.any { it.isSelected })
        assertTrue(populated.subFiles.any { !it.isSelected })

        viewModel.selectMoriTargetDestination(com.example.data.MoriTargetDestination.PUBLIC_DOWNLOAD_ARCANUM)
        assertEquals(
            com.example.data.MoriTargetDestination.PUBLIC_DOWNLOAD_ARCANUM,
            viewModel.moriState.value.targetDestination
        )

        viewModel.selectMoriStressScenario(com.example.data.MoriStressScenario.EXTREME_STRESS_BUG_GUARD)
        assertEquals(
            com.example.data.MoriStressScenario.EXTREME_STRESS_BUG_GUARD,
            viewModel.moriState.value.stressScenario
        )
    }

    @Test
    fun `wrong 3-layer PIN silently routes to Decoy Vault without error message and masks vault number as 1`() {
        val app = ApplicationProvider.getApplicationContext<Application>()
        val viewModel = VaultViewModel(app)
        viewModel.lockVaultNow()

        // Layer 1 wrong PIN (6 chars) -> advances to PIN_LAYER_2 without error
        "987654".forEach { viewModel.onPinCharPressed(it) }
        assertEquals(com.example.ui.AuthStage.PIN_LAYER_2, viewModel.securityState.value.stage)
        assertEquals(null, viewModel.securityState.value.errorMessage)
        assertTrue(viewModel.securityState.value.isDecoyVaultSession)

        // Layer 2 wrong PIN (6 chars) -> advances to PIN_LAYER_3 without error
        "654321".forEach { viewModel.onPinCharPressed(it) }
        assertEquals(com.example.ui.AuthStage.PIN_LAYER_3, viewModel.securityState.value.stage)
        assertEquals(null, viewModel.securityState.value.errorMessage)

        // Layer 3 wrong PIN (6 chars) -> silently unlocks into DECOY_VAULT_ID (999) disguised as Brankas #1
        "000000".forEach { viewModel.onPinCharPressed(it) }
        assertEquals(com.example.data.SecurityConfig.DECOY_VAULT_ID, viewModel.activeVaultId.value)
        assertEquals(1, com.example.data.SecurityConfig.displayVaultNumber(viewModel.activeVaultId.value))
        assertEquals(null, viewModel.securityState.value.errorMessage)

        // Verify Intruder Honeypot Log recorded the exact 3 wrong PIN layers
        val latestIntruderLog = viewModel.intruderAndPanicState.value.intruderLogs.first()
        assertEquals("987654", latestIntruderLog.wrongPinLayer1)
        assertEquals("654321", latestIntruderLog.wrongPinLayer2)
        assertEquals("000000", latestIntruderLog.wrongPinLayer3)
    }

    @Test
    fun `png steganography encodes and decodes encrypted message and attached file inside valid png`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val targetPng = java.io.File(context.cacheDir, "test_stego_carrier.png")
        val attachedTemp = java.io.File(context.cacheDir, "seed_backup.txt")
        attachedTemp.writeText("Rahasia Dokumen Lampiran 12345")

        com.example.data.PngSteganographyEngine.createStegoPngFile(
            outputFile = targetPng,
            coverTheme = com.example.data.StegoCoverTheme.COSMIC_POSTCARD,
            visibleCaption = "Poster Galaksi",
            secretTitle = "Kunci Server Cadangan",
            secretMessage = "Seed phrase utama: alpha bravo charlie delta",
            passcode = "NYX777",
            attachedFile = attachedTemp,
            attachedDisplayName = "seed_backup",
            attachedExtension = "txt"
        )

        assertTrue(targetPng.exists())
        assertTrue(com.example.data.PngSteganographyEngine.hasStegoPayload(targetPng))

        // Decoding with wrong passcode should indicate passcode invalid
        val wrongPayload = com.example.data.PngSteganographyEngine.decodeStegoPngFile(targetPng, "WRONG1")
        assertTrue(wrongPayload.isStegoCarrier)
        assertFalse(wrongPayload.isPasscodeValid)

        // Decoding with right passcode should recover message and attached bytes
        val payload = com.example.data.PngSteganographyEngine.decodeStegoPngFile(targetPng, "NYX777")
        assertTrue(payload.isStegoCarrier)
        assertTrue(payload.isPasscodeValid)
        assertEquals("Kunci Server Cadangan", payload.secretTitle)
        assertEquals("Seed phrase utama: alpha bravo charlie delta", payload.secretMessage)
        assertEquals("seed_backup", payload.embeddedFileName)
        assertEquals("txt", payload.embeddedFileExtension)
        assertEquals("Rahasia Dokumen Lampiran 12345", String(payload.embeddedFileBytes!!, Charsets.UTF_8))
        targetPng.delete()
        attachedTemp.delete()
    }

    @Test
    fun `extreme panic motion switch immediately transitions active session to decoy vault without lock screen`() {
        val app = ApplicationProvider.getApplicationContext<Application>()
        val viewModel = VaultViewModel(app)

        viewModel.triggerExtremePanicSwitchToDecoyVault("TEST GUNCANGAN EKSTREM")
        assertEquals(com.example.data.SecurityConfig.DECOY_VAULT_ID, viewModel.activeVaultId.value)
        assertEquals(com.example.ui.AuthStage.UNLOCKED, viewModel.securityState.value.stage)
        assertTrue(viewModel.securityState.value.isDecoyVaultSession)
        assertEquals(null, viewModel.statusBannerMessage.value)
        assertEquals("TEST GUNCANGAN EKSTREM", viewModel.intruderAndPanicState.value.intruderLogs.first().triggerType)
    }

    @Test
    fun `stego studio embeds message into vault photo carrier and generates offline stego qr key card`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val sourcePhoto = java.io.File(context.cacheDir, "vault_photo_sample.png")
        val bmp = android.graphics.Bitmap.createBitmap(120, 90, android.graphics.Bitmap.Config.ARGB_8888)
        sourcePhoto.outputStream().use { bmp.compress(android.graphics.Bitmap.CompressFormat.PNG, 100, it) }
        bmp.recycle()

        val outputPhotoStego = java.io.File(context.cacheDir, "stego_from_vault_photo.png")
        com.example.data.PngSteganographyEngine.createStegoPngFile(
            outputFile = outputPhotoStego,
            coverTheme = com.example.data.StegoCoverTheme.BOTANICAL_ART,
            visibleCaption = "Foto Liburan",
            secretTitle = "Pesan di Dalam Foto Brankas",
            secretMessage = "Koordinat brankas cadangan tersimpan di dalam foto asli.",
            passcode = "FOTO99",
            sourceCarrierImageFile = sourcePhoto,
            sourceCarrierName = "vault_photo_sample.png",
            isQrKeyCardMode = false
        )

        val decodedPhotoStego = com.example.data.PngSteganographyEngine.decodeStegoPngFile(outputPhotoStego, "FOTO99")
        assertTrue(decodedPhotoStego.isStegoCarrier)
        assertTrue(decodedPhotoStego.isPasscodeValid)
        assertTrue(decodedPhotoStego.carrierSourceLabel.startsWith("VAULT_PHOTO::"))
        assertEquals("Koordinat brankas cadangan tersimpan di dalam foto asli.", decodedPhotoStego.secretMessage)

        val qrKeyCardFile = java.io.File(context.cacheDir, "stego_qr_key_card.png")
        com.example.data.PngSteganographyEngine.createStegoPngFile(
            outputFile = qrKeyCardFile,
            coverTheme = com.example.data.StegoCoverTheme.COSMIC_POSTCARD,
            visibleCaption = "Kartu Kunci QR",
            secretTitle = "Master Recovery QR #1",
            secretMessage = "SEED-NYX2-OFFLINE-KEY-778899",
            passcode = "",
            isQrKeyCardMode = true
        )

        val decodedQr = com.example.data.PngSteganographyEngine.decodeStegoPngFile(qrKeyCardFile, "")
        assertTrue(decodedQr.isStegoCarrier)
        assertTrue(decodedQr.isQrKeyCard)
        assertEquals("SEED-NYX2-OFFLINE-KEY-778899", decodedQr.secretMessage)

        val qrMatrix = com.example.data.PngSteganographyEngine.generateDeterministicQrMatrix("SEED-NYX2-OFFLINE-KEY-778899")
        assertEquals(21, qrMatrix.size)
        assertTrue(qrMatrix[0][0]) // Top-left finder corner

        val analysis = com.example.data.PngSteganographyEngine.analyzeStegoStealth(
            carrierItem = null,
            secretTitle = "Master Recovery QR #1",
            secretMessage = "SEED-NYX2-OFFLINE-KEY-778899",
            attachedItem = null,
            isQrKeyCardMode = true
        )
        assertEquals(0.00f, analysis.visualDiffPercent, 0.001f)
        assertTrue(analysis.stealthScorePercent >= 90)

        sourcePhoto.delete()
        outputPhotoStego.delete()
        qrKeyCardFile.delete()
    }

    @Test
    fun `wav audio lsb steganography embeds and decodes secret message and hidden photo in 16bit pcm wav`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val hiddenPhoto = java.io.File(context.cacheDir, "secret_photo_in_wav.png")
        val bmp = android.graphics.Bitmap.createBitmap(80, 60, android.graphics.Bitmap.Config.ARGB_8888)
        hiddenPhoto.outputStream().use { bmp.compress(android.graphics.Bitmap.CompressFormat.PNG, 100, it) }
        bmp.recycle()

        val outputWav = java.io.File(context.cacheDir, "rain_ambience_stego.wav")
        val wavSize = com.example.data.WavAudioSteganographyEngine.createStegoWavFile(
            outputFile = outputWav,
            preset = com.example.data.WavCamouflagePreset.RAINY_NIGHT_AMBIENCE,
            secretTitle = "Transmisi Audio LSB",
            secretMessage = "Pesan rahasia di dalam bit LSB gelombang suara hujan.",
            passcode = "WAV888",
            attachedFile = hiddenPhoto,
            attachedDisplayName = "Foto_Rahasia_Audio",
            attachedExtension = "png"
        )

        assertTrue(outputWav.exists())
        assertTrue(wavSize > 44L)

        // Wrong passcode must lock payload
        val lockedResult = com.example.data.WavAudioSteganographyEngine.decodeStegoWavFile(outputWav, "WRONG")
        assertTrue(lockedResult.isStegoCarrier)
        assertTrue(lockedResult.isWavAudioCarrier)
        assertFalse(lockedResult.isPasscodeValid)

        // Correct passcode must decode message & photo from 16-bit PCM LSB samples
        val unlockedResult = com.example.data.WavAudioSteganographyEngine.decodeStegoWavFile(outputWav, "WAV888")
        assertTrue(unlockedResult.isStegoCarrier)
        assertTrue(unlockedResult.isWavAudioCarrier)
        assertTrue(unlockedResult.isPasscodeValid)
        assertTrue(unlockedResult.isDualPhotoFusion)
        assertEquals("Transmisi Audio LSB", unlockedResult.secretTitle)
        assertEquals("Pesan rahasia di dalam bit LSB gelombang suara hujan.", unlockedResult.secretMessage)
        assertEquals("Foto_Rahasia_Audio", unlockedResult.embeddedFileName)
        assertTrue((unlockedResult.embeddedFileBytes?.size ?: 0) > 0)

        hiddenPhoto.delete()
        outputWav.delete()
    }

    @Test
    fun `core raw text wav lsb 1bit and 2bit encoding preserves audio fidelity with max 1 lsb delta`() {
        val baseWav = com.example.data.WavAudioSteganographyEngine.synthesizeCamouflageWavBytes(
            preset = com.example.data.WavCamouflagePreset.LOFI_WARM_CHORDS,
            durationSeconds = 1,
            minRequiredSamples = 4096
        )
        val rawSecret = "Pesan Teks Mentah Ultra-Stealth 1-Bit LSB (-90.3 dB) Tanpa Degradasi Suara!"

        // 1-Bit LSB mode
        val stego1BitWav = com.example.data.WavAudioSteganographyEngine.embedRawTextIntoWavLsb(
            wavBytes = baseWav,
            rawText = rawSecret,
            bitsPerSample = 1
        )
        val extracted1Bit = com.example.data.WavAudioSteganographyEngine.extractRawTextFromWavLsb(stego1BitWav)
        assertEquals(rawSecret, extracted1Bit)

        // Verify minimal audible degradation: every 16-bit sample differs by at most 1 LSB unit
        val (dataOffset, dataLen) = com.example.data.WavAudioSteganographyEngine.findPcmDataChunkOffset(baseWav)
        var pos = dataOffset
        while (pos + 1 < dataOffset + dataLen && pos + 1 < baseWav.size) {
            val origSample = ((baseWav[pos + 1].toInt() shl 8) or (baseWav[pos].toInt() and 0xFF)).toShort().toInt()
            val stegoSample = ((stego1BitWav[pos + 1].toInt() shl 8) or (stego1BitWav[pos].toInt() and 0xFF)).toShort().toInt()
            assertTrue("Sample delta must be <= 1 for 1-bit LSB", kotlin.math.abs(origSample - stegoSample) <= 1)
            pos += 2
        }

        // 2-Bit LSB mode
        val stego2BitWav = com.example.data.WavAudioSteganographyEngine.embedRawTextIntoWavLsb(
            wavBytes = baseWav,
            rawText = rawSecret,
            bitsPerSample = 2
        )
        val extracted2Bit = com.example.data.WavAudioSteganographyEngine.extractRawTextFromWavLsb(stego2BitWav)
        assertEquals(rawSecret, extracted2Bit)
    }

    @Test
    fun `morse code transceiver and cw wav synthesizer with embedded lsb roundtrip`() {
        val plain = "SOS NYX BRANKAS 2026"
        val morse = com.example.data.AdvancedCryptoLabEngine.encodeTextToMorse(plain)
        assertTrue(morse.startsWith("... --- ... /"))
        val decoded = com.example.data.AdvancedCryptoLabEngine.decodeMorseToText(morse)
        assertEquals(plain, decoded)

        val morseWavWithLsb = com.example.data.AdvancedCryptoLabEngine.synthesizeMorseWavBytes(
            morseCode = morse,
            wpm = 20,
            frequencyHz = 700.0,
            hiddenLsbText = "RAHASIA_DI_DALAM_BIP_MORSE"
        )
        val extractedFromMorseWav = com.example.data.WavAudioSteganographyEngine.extractRawTextFromWavLsb(morseWavWithLsb)
        assertEquals("RAHASIA_DI_DALAM_BIP_MORSE", extractedFromMorseWav)
    }

    @Test
    fun `enigma full custom machine simulator is 100 percent reciprocal across custom rotors rings and plugboard`() {
        val config = com.example.data.EnigmaCustomConfig(
            leftRotor = com.example.data.EnigmaRotorSpec.ROTOR_IV,
            middleRotor = com.example.data.EnigmaRotorSpec.ROTOR_VI,
            rightRotor = com.example.data.EnigmaRotorSpec.ROTOR_VIII,
            leftPosition = 'Q',
            middlePosition = 'M',
            rightPosition = 'Z',
            leftRingSetting = 7,
            middleRingSetting = 14,
            rightRingSetting = 21,
            reflector = com.example.data.EnigmaReflectorSpec.UKW_C,
            plugboardPairs = "AB CD EF GH IJ KL MN OP"
        )
        val originalMessage = "OPERASIRAHASIAMALAMINIDIBRANKASUTAMA"
        val encrypted = com.example.data.AdvancedCryptoLabEngine.processEnigma(originalMessage, config)
        assertFalse(encrypted.outputText == originalMessage)
        assertEquals(8, encrypted.activePlugboardCount)

        // Feeding ciphertext with identical initial config must recover exact plaintext
        val decrypted = com.example.data.AdvancedCryptoLabEngine.processEnigma(encrypted.outputText, config)
        assertEquals(originalMessage, decrypted.outputText)
    }

    @Test
    fun `nyx30 and trantor30 complex 30char alphanumeric ciphers encrypt and decrypt accurately and reject wrong keys`() {
        val randomKey30 = com.example.data.AdvancedCryptoLabEngine.generateRandom30CharAlphanumericKey()
        assertEquals(30, randomKey30.length)
        val analysis = com.example.data.AdvancedCryptoLabEngine.analyze30CharKey(randomKey30)
        assertTrue(analysis.isValidExact30)
        assertTrue(analysis.letterCount > 0 && analysis.digitCount > 0)

        val secretText = "Dokumen Rahasia Tingkat Tinggi: Protokol 30-Sandi Huruf & Angka 2026!"
        val wrongKey30 = "WRONGKEY00112233445566778899ZZ"

        // Tool #1: NYX-30 Hyper-Cascade Matrix Cipher
        val nyxCipher = com.example.data.AdvancedCryptoLabEngine.encryptNyx30HyperCascade(secretText, randomKey30)
        assertTrue(nyxCipher.startsWith("NYX30::"))
        val nyxDecrypted = com.example.data.AdvancedCryptoLabEngine.decryptNyx30HyperCascade(nyxCipher, randomKey30)
        assertTrue(nyxDecrypted.isSuccess)
        assertEquals(secretText, nyxDecrypted.getOrNull())
        val nyxWrong = com.example.data.AdvancedCryptoLabEngine.decryptNyx30HyperCascade(nyxCipher, wrongKey30)
        assertTrue(nyxWrong.isFailure)

        // Tool #2: TRANTOR-30 Poly-Dimensional Lattice Cascade
        val trantorCipher = com.example.data.AdvancedCryptoLabEngine.encryptTrantor30Lattice(secretText, randomKey30)
        assertTrue(trantorCipher.startsWith("TRN30::"))
        val trantorDecrypted = com.example.data.AdvancedCryptoLabEngine.decryptTrantor30Lattice(trantorCipher, randomKey30)
        assertTrue(trantorDecrypted.isSuccess)
        assertEquals(secretText, trantorDecrypted.getOrNull())
        val trantorWrong = com.example.data.AdvancedCryptoLabEngine.decryptTrantor30Lattice(trantorCipher, wrongKey30)
        assertTrue(trantorWrong.isFailure)
    }

    @Test
    fun `brankas setting 222222 unlocks to Umum tab and navigates to Pro Hub without locking out`() {
        val app = ApplicationProvider.getApplicationContext<Application>()
        val viewModel = VaultViewModel(app)
        viewModel.lockVaultNow()

        // Enter 3-layer Brankas Setting PIN (default 222222 - 222222 - 222222)
        "222222".forEach { viewModel.onPinCharPressed(it) }
        assertEquals(com.example.ui.AuthStage.PIN_LAYER_2, viewModel.securityState.value.stage)
        "222222".forEach { viewModel.onPinCharPressed(it) }
        assertEquals(com.example.ui.AuthStage.PIN_LAYER_3, viewModel.securityState.value.stage)
        "222222".forEach { viewModel.onPinCharPressed(it) }
        if (viewModel.securityState.value.stage == com.example.ui.AuthStage.BIOMETRIC_STEP) {
            viewModel.onBiometricAuthenticatedSuccess()
        }

        // Must unlock into isolated Brankas Setting session starting on Tab Umum (SCRIPT_ENGINE)
        assertEquals(com.example.ui.AuthStage.UNLOCKED, viewModel.securityState.value.stage)
        assertTrue(viewModel.securityState.value.isIsolatedSettingsSession)
        assertEquals(com.example.ui.VaultTab.SCRIPT_ENGINE, viewModel.activeTab.value)

        // Navigating to Pro Hub (VaultTab.PRO_STUDIO) must succeed and NOT kick back to PIN lock screen
        viewModel.selectTab(com.example.ui.VaultTab.PRO_STUDIO)
        assertEquals(com.example.ui.AuthStage.UNLOCKED, viewModel.securityState.value.stage)
        assertTrue(viewModel.securityState.value.isIsolatedSettingsSession)
        assertEquals(com.example.ui.VaultTab.PRO_STUDIO, viewModel.activeTab.value)

        // Navigating across Tema, Radar, ZIP Scan, and back to Umum must all stay unlocked in Brankas Setting
        viewModel.selectTab(com.example.ui.VaultTab.THEME_STUDIO)
        assertEquals(com.example.ui.VaultTab.THEME_STUDIO, viewModel.activeTab.value)
        viewModel.selectTab(com.example.ui.VaultTab.INTRUDER_PANIC_RADAR)
        assertEquals(com.example.ui.VaultTab.INTRUDER_PANIC_RADAR, viewModel.activeTab.value)
        viewModel.selectTab(com.example.ui.VaultTab.ZIP_SCANNER)
        assertEquals(com.example.ui.VaultTab.ZIP_SCANNER, viewModel.activeTab.value)
        viewModel.selectTab(com.example.ui.VaultTab.SCRIPT_ENGINE)
        assertEquals(com.example.ui.VaultTab.SCRIPT_ENGINE, viewModel.activeTab.value)
        assertEquals(com.example.ui.AuthStage.UNLOCKED, viewModel.securityState.value.stage)
    }

    @Test
    fun `external http link intent auto-opens Arcanum Browser in lockdown mode and blocks vault access until restart`() {
        com.example.ui.ExternalBrowserLockdownController.resetForTesting()
        assertFalse(com.example.ui.ExternalBrowserLockdownController.isExternalBrowserLockdownActive)

        // 1. Local content:// file URI must NOT be treated as a web browser link
        val localFileIntent = android.content.Intent(
            android.content.Intent.ACTION_VIEW,
            android.net.Uri.parse("content://com.android.providers.media.documents/document/image%3A101")
        )
        assertFalse(com.example.ui.ExternalBrowserLockdownController.isWebBrowserIntent(localFileIntent))

        // 2. External http/https link click outside the app MUST be detected and activate lockdown mode
        val externalLinkIntent = android.content.Intent(
            android.content.Intent.ACTION_VIEW,
            android.net.Uri.parse("http://example.com/news?utm_source=whatsapp&id=77")
        )
        assertTrue(com.example.ui.ExternalBrowserLockdownController.isWebBrowserIntent(externalLinkIntent))
        val extracted = com.example.ui.ExternalBrowserLockdownController.extractTargetUrlFromIntent(externalLinkIntent)
        org.junit.Assert.assertNotNull(extracted)
        com.example.ui.ExternalBrowserLockdownController.activateExternalBrowserLockdown(extracted!!)

        assertTrue(com.example.ui.ExternalBrowserLockdownController.isExternalBrowserLockdownActive)
        assertEquals(
            "https://example.com/news?id=77",
            com.example.ui.ExternalBrowserLockdownController.latestExternalBrowserUrl
        )
        assertTrue(com.example.ui.ExternalBrowserLockdownController.externalBrowserNonce >= 1)

        // Clean up singleton state after test
        com.example.ui.ExternalBrowserLockdownController.resetForTesting()
    }
}
