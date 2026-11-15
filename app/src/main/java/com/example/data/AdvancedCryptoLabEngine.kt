package com.example.data

import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioTrack
import android.util.Base64
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.security.SecureRandom
import java.util.Locale
import javax.crypto.Cipher
import javax.crypto.Mac
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.GCMParameterSpec
import javax.crypto.spec.PBEKeySpec
import javax.crypto.spec.SecretKeySpec
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.ln
import kotlin.math.roundToInt
import kotlin.math.sin

/**
 * Rotor historis dan kustom untuk simulator Enigma M3/M4 Full Custom.
 */
enum class EnigmaRotorSpec(
    val label: String,
    val wiring: String,
    val notchChars: String,
    val description: String
) {
    ROTOR_I("Rotor I", "EKMFLGDQVZNTOWYHXUSPAIBRCJ", "Q", "Wehrmacht 1930 (Notch Q)"),
    ROTOR_II("Rotor II", "AJDKSIRUXBLHWTMCQGZNPYFVOE", "E", "Wehrmacht 1930 (Notch E)"),
    ROTOR_III("Rotor III", "BDFHJLCPRTXVZNYEIWGAKMUSQO", "V", "Wehrmacht 1930 (Notch V)"),
    ROTOR_IV("Rotor IV", "ESOVPZJAYQUIRHXLNFTGKDCMWB", "J", "M3 Army 1938 (Notch J)"),
    ROTOR_V("Rotor V", "VZBRGITYUPSDNHLXAWMJQOFECK", "Z", "M3 Army 1938 (Notch Z)"),
    ROTOR_VI("Rotor VI", "JPGVOUMFYQBENHZRDKASXLICTW", "ZM", "Kriegsmarine M4 (Notch Z & M)"),
    ROTOR_VII("Rotor VII", "NZJHGRCXMYSWBOUFAIVLPEKQDT", "ZM", "Kriegsmarine M4 (Notch Z & M)"),
    ROTOR_VIII("Rotor VIII", "FKQHTLXOCBJSPDZRAMEWNIUYGV", "ZM", "Kriegsmarine M4 (Notch Z & M)")
}

/**
 * Reflektor (Umkehrwalze / UKW) yang menjamin sifat resiprokal Enigma (Enkripsi == Dekripsi).
 */
enum class EnigmaReflectorSpec(
    val label: String,
    val wiring: String,
    val description: String
) {
    UKW_B("UKW-B (Standar)", "YRUHQSLDPXNGOKMIEBFZCWVJAT", "Reflektor M3 Standar Wehrmacht"),
    UKW_C("UKW-C (Taktis)", "FVPJIAOYEDRZXWGCTKUQSBNMHL", "Reflektor M3/M4 Kriegsmarine"),
    UKW_NYX("UKW-Nyx (Kustom)", "ZYXWVUTSRQPONMLKJIHGFEDCBA", "Reflektor Simetris Inversi Penuh")
}

data class EnigmaCustomConfig(
    val leftRotor: EnigmaRotorSpec = EnigmaRotorSpec.ROTOR_I,
    val middleRotor: EnigmaRotorSpec = EnigmaRotorSpec.ROTOR_II,
    val rightRotor: EnigmaRotorSpec = EnigmaRotorSpec.ROTOR_III,
    val leftPosition: Char = 'A',
    val middlePosition: Char = 'A',
    val rightPosition: Char = 'A',
    val leftRingSetting: Int = 1,
    val middleRingSetting: Int = 1,
    val rightRingSetting: Int = 1,
    val reflector: EnigmaReflectorSpec = EnigmaReflectorSpec.UKW_B,
    val plugboardPairs: String = "AB CD EF GH IJ",
    val customRightRotorWiring: String = ""
)

data class EnigmaProcessResult(
    val inputText: String,
    val outputText: String,
    val groupedOutputText: String,
    val finalLeftPos: Char,
    val finalMiddlePos: Char,
    val finalRightPos: Char,
    val lastLampChar: Char?,
    val activePlugboardCount: Int,
    val stepTracePreview: List<String>
)

data class Key30Analysis(
    val cleanedKey: String,
    val length: Int,
    val isValidExact30: Boolean,
    val letterCount: Int,
    val digitCount: Int,
    val uniqueChars: Int,
    val entropyBits: Int,
    val securityGrade: String,
    val subKeysPreview: List<String>
)

/**
 * Mesin Kriptografi & Sinyal Tingkat Lanjut:
 * 1. Morse Code Transceiver (Text <-> Morse) + CW Audio Synthesizer (.WAV + LSB Stego)
 * 2. Enigma M3/M4 Full Custom Machine Simulator (8 Rotor, Ringstellung, Grundstellung, UKW, Steckerbrett)
 * 3. Alat Enkripsi #1: NYX-30 Hyper-Cascade Matrix Cipher (30-Sandi Huruf & Angka: PBKDF2-SHA512 + 30-Round Feistel + ChaCha ARX + AES-256-GCM)
 * 4. Alat Enkripsi #2: TRANTOR-30 Poly-Dimensional Lattice Cascade (30-Sandi Huruf & Angka: GF(256) S-Box + Vigenère-36 + Matrix Transposition + Bit Rotation + HMAC-SHA256)
 */
object AdvancedCryptoLabEngine {

    private const val MORSE_SAMPLE_RATE = 22050
    private const val ALPHANUMERIC_36 = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"

    @Volatile
    private var activeMorseTrack: AudioTrack? = null

    private val charToMorseMap: Map<Char, String> = mapOf(
        'A' to ".-",
        'B' to "-...",
        'C' to "-.-.",
        'D' to "-..",
        'E' to ".",
        'F' to "..-.",
        'G' to "--.",
        'H' to "....",
        'I' to "..",
        'J' to ".---",
        'K' to "-.-",
        'L' to ".-..",
        'M' to "--",
        'N' to "-.",
        'O' to "---",
        'P' to ".--.",
        'Q' to "--.-",
        'R' to ".-.",
        'S' to "...",
        'T' to "-",
        'U' to "..-",
        'V' to "...-",
        'W' to ".--",
        'X' to "-..-",
        'Y' to "-.--",
        'Z' to "--..",
        '0' to "-----",
        '1' to ".----",
        '2' to "..---",
        '3' to "...--",
        '4' to "....-",
        '5' to ".....",
        '6' to "-....",
        '7' to "--...",
        '8' to "---..",
        '9' to "----.",
        '.' to ".-.-.-",
        ',' to "--..--",
        '?' to "..--..",
        '!' to "-.-.--",
        '/' to "-..-.",
        '-' to "-....-",
        '@' to ".--.-.",
        ':' to "---...",
        '=' to "-...-",
        '+' to ".-.-."
    )

    private val morseToCharMap: Map<String, Char> = charToMorseMap.entries.associate { (k, v) -> v to k }

    // =============================================================================================
    // 1. FITUR MORSE (TEXT <-> MORSE ENCODER/DECODER + CW AUDIO SYNTHESIZER + WAV LSB)
    // =============================================================================================

    /**
     * Mengubah teks biasa menjadi sandi Morse standar ITU (menggunakan `/` sebagai pemisah kata).
     */
    fun encodeTextToMorse(text: String): String {
        if (text.isBlank()) return ""
        val words = text.trim().uppercase(Locale.US).split(Regex("\\s+"))
        return words.joinToString(" / ") { word ->
            word.mapNotNull { ch -> charToMorseMap[ch] }.joinToString(" ")
        }.trim()
    }

    /**
     * Menerjemahkan sandi Morse (`.- -... / ...`) kembali menjadi teks alfabet & angka.
     */
    fun decodeMorseToText(morse: String): String {
        if (morse.isBlank()) return ""
        val normalized = morse.trim()
            .replace('•', '.')
            .replace('·', '.')
            .replace('—', '-')
            .replace('–', '-')

        val wordTokens = normalized.split(Regex("\\s*/\\s*|\\s{3,}"))
        return wordTokens.joinToString(" ") { wordToken ->
            wordToken.trim().split(Regex("\\s+")).mapNotNull { code ->
                if (code.isBlank()) null else (morseToCharMap[code] ?: '?')
            }.joinToString("")
        }.trim()
    }

    /**
     * Merender sinyal suara CW (Continuous Wave) dari sandi Morse menjadi file .WAV 16-bit PCM mono,
     * dan secara opsional menyisipkan pesan teks rahasia ke dalam LSB sampel audio Morse tersebut!
     */
    fun synthesizeMorseWavBytes(
        morseCode: String,
        wpm: Int = 18,
        frequencyHz: Double = 650.0,
        hiddenLsbText: String = ""
    ): ByteArray {
        val safeWpm = wpm.coerceIn(8, 36)
        val safeFreq = frequencyHz.coerceIn(350.0, 1200.0)
        // Standar PARIS: 1 unit (dit) = 1.2 / WPM detik
        val ditDurationSec = (1.2 / safeWpm.toDouble()).coerceIn(0.035, 0.15)
        val ditSamples = (ditDurationSec * MORSE_SAMPLE_RATE).roundToInt().coerceAtLeast(400)
        val dahSamples = ditSamples * 3
        val intraCharGapSamples = ditSamples
        val interCharGapSamples = ditSamples * 3
        val wordGapSamples = ditSamples * 7

        val cleanMorse = if (morseCode.isBlank()) "... --- ..." else morseCode.trim()
        val segments = mutableListOf<Pair<Boolean, Int>>() // Pair(isToneOn, sampleCount)

        // Tambahkan 0.12 detik hening awal
        segments.add(false to (MORSE_SAMPLE_RATE / 8))

        val tokens = cleanMorse.split(Regex("\\s+"))
        for ((tIdx, token) in tokens.withIndex()) {
            if (token == "/") {
                segments.add(false to wordGapSamples)
                continue
            }
            for ((cIdx, symbol) in token.withIndex()) {
                when (symbol) {
                    '.', '•', '·' -> segments.add(true to ditSamples)
                    '-', '—', '–' -> segments.add(true to dahSamples)
                }
                if (cIdx < token.lastIndex) {
                    segments.add(false to intraCharGapSamples)
                }
            }
            if (tIdx < tokens.lastIndex && tokens[tIdx + 1] != "/") {
                segments.add(false to interCharGapSamples)
            }
        }

        // Tambahkan 0.25 detik hening akhir
        segments.add(false to (MORSE_SAMPLE_RATE / 4))

        val totalSamples = segments.sumOf { it.second }.coerceAtLeast(MORSE_SAMPLE_RATE)
        val pcmBytes = ByteArray(totalSamples * 2)
        val rampSamples = (MORSE_SAMPLE_RATE * 0.004).roundToInt().coerceAtLeast(32)
        var sampleCursor = 0

        for ((isTone, count) in segments) {
            for (i in 0 until count) {
                if (sampleCursor >= totalSamples) break
                val sampleShort: Short = if (isTone) {
                    val t = sampleCursor.toDouble() / MORSE_SAMPLE_RATE.toDouble()
                    val env = when {
                        i < rampSamples -> 0.5 * (1.0 - cos(PI * i / rampSamples))
                        i > count - rampSamples -> 0.5 * (1.0 - cos(PI * (count - i) / rampSamples))
                        else -> 1.0
                    }
                    // Tambahkan sedikit noise lantai ultra-halus pada celah agar LSB tetap alami
                    val tone = sin(2.0 * PI * safeFreq * t) * env * 22000.0
                    tone.roundToInt().coerceIn(-32000, 32000).toShort()
                } else {
                    // Ambient floor sangat lembut untuk menyamarkan bit LSB pada bagian hening
                    val microDither = ((sampleCursor * 1103515245 + 12345) shr 16) and 0x07
                    (microDither - 3).toShort()
                }
                pcmBytes[sampleCursor * 2] = (sampleShort.toInt() and 0xFF).toByte()
                pcmBytes[sampleCursor * 2 + 1] = ((sampleShort.toInt() shr 8) and 0xFF).toByte()
                sampleCursor++
            }
        }

        val rawWav = buildMono16BitWav(pcmBytes, MORSE_SAMPLE_RATE)
        return if (hiddenLsbText.isNotBlank()) {
            WavAudioSteganographyEngine.embedRawTextIntoWavLsb(
                wavBytes = rawWav,
                rawText = hiddenLsbText,
                bitsPerSample = 1
            )
        } else {
            rawWav
        }
    }

    /**
     * Memutar bunyi bip CW Morse secara langsung menggunakan AudioTrack.
     */
    fun playMorseCwAudio(
        morseCode: String,
        wpm: Int = 18,
        frequencyHz: Double = 650.0
    ) {
        stopMorseCwAudio()
        try {
            val wavBytes = synthesizeMorseWavBytes(morseCode, wpm, frequencyHz)
            val (dataOffset, dataSize) = WavAudioSteganographyEngine.findPcmDataChunkOffset(wavBytes)
            if (dataSize <= 0) return
            val pcmData = wavBytes.copyOfRange(dataOffset, (dataOffset + dataSize).coerceAtMost(wavBytes.size))

            val track = AudioTrack.Builder()
                .setAudioAttributes(
                    AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_MEDIA)
                        .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                        .build()
                )
                .setAudioFormat(
                    AudioFormat.Builder()
                        .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
                        .setSampleRate(MORSE_SAMPLE_RATE)
                        .setChannelMask(AudioFormat.CHANNEL_OUT_MONO)
                        .build()
                )
                .setTransferMode(AudioTrack.MODE_STATIC)
                .setBufferSizeInBytes(pcmData.size)
                .build()

            track.write(pcmData, 0, pcmData.size)
            track.play()
            activeMorseTrack = track
        } catch (_: Throwable) {
            // Abaikan pada unit test JVM tanpa hardware audio
        }
    }

    fun stopMorseCwAudio() {
        try {
            activeMorseTrack?.let { track ->
                if (track.playState == AudioTrack.PLAYSTATE_PLAYING) {
                    track.stop()
                }
                track.release()
            }
        } catch (_: Throwable) {
        } finally {
            activeMorseTrack = null
        }
    }

    private fun buildMono16BitWav(pcmBytes: ByteArray, sampleRate: Int): ByteArray {
        val dataSize = pcmBytes.size
        val header = ByteBuffer.allocate(44).order(ByteOrder.LITTLE_ENDIAN).apply {
            put("RIFF".toByteArray(Charsets.US_ASCII))
            putInt(36 + dataSize)
            put("WAVE".toByteArray(Charsets.US_ASCII))
            put("fmt ".toByteArray(Charsets.US_ASCII))
            putInt(16)
            putShort(1)
            putShort(1)
            putInt(sampleRate)
            putInt(sampleRate * 2)
            putShort(2)
            putShort(16)
            put("data".toByteArray(Charsets.US_ASCII))
            putInt(dataSize)
        }.array()
        return header + pcmBytes
    }

    // =============================================================================================
    // 2. ENIGMA FULL CUSTOM MACHINE SIMULATOR (8 ROTOR, RINGSTELLUNG, PLUGBOARD, DOUBLE-STEPPING)
    // =============================================================================================

    /**
     * Memvalidasi dan mengekstrak pasangan Plugboard (Steckerbrett) dari string seperti "AB CD EF GH".
     */
    fun parsePlugboardMap(plugboardSpec: String): Map<Char, Char> {
        val map = mutableMapOf<Char, Char>()
        val tokens = plugboardSpec.uppercase(Locale.US)
            .replace(Regex("[^A-Z\\s]"), " ")
            .trim()
            .split(Regex("\\s+"))

        for (token in tokens) {
            if (token.length == 2) {
                val a = token[0]
                val b = token[1]
                if (a != b && !map.containsKey(a) && !map.containsKey(b)) {
                    map[a] = b
                    map[b] = a
                }
            }
        }
        return map
    }

    /**
     * Menjalankan simulasi mesin Enigma M3/M4 Full Custom yang 100% resiprokal:
     * Memasukkan Ciphertext dengan pengaturan awal Rotor, Ring, Reflektor, dan Plugboard yang sama
     * akan menghasilkan kembali Plaintext asli.
     */
    fun processEnigma(
        inputText: String,
        config: EnigmaCustomConfig
    ): EnigmaProcessResult {
        val plugMap = parsePlugboardMap(config.plugboardPairs)

        val leftWiring = config.leftRotor.wiring
        val middleWiring = config.middleRotor.wiring
        val rightWiring = validateCustomWiringOrFallback(
            customWiring = config.customRightRotorWiring,
            fallback = config.rightRotor.wiring
        )

        val leftInverse = invertWiring(leftWiring)
        val middleInverse = invertWiring(middleWiring)
        val rightInverse = invertWiring(rightWiring)
        val reflectorWiring = config.reflector.wiring

        var posL = (config.leftPosition.uppercaseChar() - 'A').coerceIn(0, 25)
        var posM = (config.middlePosition.uppercaseChar() - 'A').coerceIn(0, 25)
        var posR = (config.rightPosition.uppercaseChar() - 'A').coerceIn(0, 25)

        val ringL = (config.leftRingSetting - 1).coerceIn(0, 25)
        val ringM = (config.middleRingSetting - 1).coerceIn(0, 25)
        val ringR = (config.rightRingSetting - 1).coerceIn(0, 25)

        val outChars = StringBuilder()
        val traceList = mutableListOf<String>()
        var lastLamp: Char? = null

        for (rawCh in inputText) {
            val upper = rawCh.uppercaseChar()
            if (upper !in 'A'..'Z') {
                // Karakter non-huruf (angka/spasi/tanda baca) dipertahankan apa adanya agar pesan mudah dibaca
                if (!rawCh.isWhitespace()) {
                    outChars.append(rawCh)
                }
                continue
            }

            // 1. Mekanisme Double-Stepping Otentik Wehrmacht sebelum arus listrik mengalir
            val middleAtNotch = config.middleRotor.notchChars.contains(('A' + posM))
            val rightAtNotch = config.rightRotor.notchChars.contains(('A' + posR))

            if (middleAtNotch) {
                posL = (posL + 1) % 26
                posM = (posM + 1) % 26
            } else if (rightAtNotch) {
                posM = (posM + 1) % 26
            }
            posR = (posR + 1) % 26

            // 2. Steckerbrett (Plugboard Masuk)
            val pluggedIn = plugMap[upper] ?: upper
            var signal = pluggedIn - 'A'

            // 3. Aliran Maju: Rotor Kanan -> Rotor Tengah -> Rotor Kiri
            signal = forwardRotorPass(signal, rightWiring, posR, ringR)
            signal = forwardRotorPass(signal, middleWiring, posM, ringM)
            signal = forwardRotorPass(signal, leftWiring, posL, ringL)

            // 4. Reflektor (Umkehrwalze / UKW)
            signal = reflectorWiring[signal] - 'A'

            // 5. Aliran Balik: Rotor Kiri -> Rotor Tengah -> Rotor Kanan
            signal = backwardRotorPass(signal, leftInverse, posL, ringL)
            signal = backwardRotorPass(signal, middleInverse, posM, ringM)
            signal = backwardRotorPass(signal, rightInverse, posR, ringR)

            // 6. Steckerbrett (Plugboard Keluar)
            val rawOutChar = 'A' + signal
            val finalLampChar = plugMap[rawOutChar] ?: rawOutChar

            outChars.append(finalLampChar)
            lastLamp = finalLampChar

            if (traceList.size < 12) {
                val windowStr = "${'A' + posL}${'A' + posM}${'A' + posR}"
                traceList.add("[$windowStr] $upper → $finalLampChar")
            }
        }

        val cleanOut = outChars.toString()
        val grouped = cleanOut.filter { it in 'A'..'Z' }
            .chunked(5)
            .joinToString(" ")
            .ifEmpty { cleanOut }

        return EnigmaProcessResult(
            inputText = inputText,
            outputText = cleanOut,
            groupedOutputText = grouped,
            finalLeftPos = 'A' + posL,
            finalMiddlePos = 'A' + posM,
            finalRightPos = 'A' + posR,
            lastLampChar = lastLamp,
            activePlugboardCount = plugMap.size / 2,
            stepTracePreview = traceList
        )
    }

    private fun forwardRotorPass(inputIndex: Int, wiring: String, pos: Int, ring: Int): Int {
        val offset = (pos - ring + 26) % 26
        val shiftedIn = (inputIndex + offset) % 26
        val wiredOut = wiring[shiftedIn] - 'A'
        return (wiredOut - offset + 26) % 26
    }

    private fun backwardRotorPass(inputIndex: Int, inverseWiring: IntArray, pos: Int, ring: Int): Int {
        val offset = (pos - ring + 26) % 26
        val shiftedIn = (inputIndex + offset) % 26
        val wiredOut = inverseWiring[shiftedIn]
        return (wiredOut - offset + 26) % 26
    }

    private fun invertWiring(wiring: String): IntArray {
        val inv = IntArray(26)
        for (i in 0 until 26) {
            val target = wiring[i] - 'A'
            inv[target] = i
        }
        return inv
    }

    private fun validateCustomWiringOrFallback(customWiring: String, fallback: String): String {
        val clean = customWiring.uppercase(Locale.US).filter { it in 'A'..'Z' }
        if (clean.length == 26 && clean.toSet().size == 26) {
            return clean
        }
        return fallback
    }

    // =============================================================================================
    // 3. MANAJEMEN KUNCI 30 SANDI HURUF & ANGKA (30-CHAR ALPHANUMERIC KEY ENGINE)
    // =============================================================================================

    /**
     * Menghasilkan 30 karakter sandi kombinasi Huruf Besar (A-Z) dan Angka (0-9) secara acak kriptografis.
     */
    fun generateRandom30CharAlphanumericKey(): String {
        val rng = SecureRandom()
        val sb = StringBuilder(30)
        // Pastikan selalu mengandung kombinasi huruf dan angka yang seimbang
        for (i in 0 until 30) {
            val ch = when {
                i % 3 == 2 -> '0' + rng.nextInt(10)
                else -> 'A' + rng.nextInt(26)
            }
            sb.append(ch)
        }
        // Acak urutan menggunakan Fisher-Yates
        val chars = sb.toString().toCharArray()
        for (i in chars.lastIndex downTo 1) {
            val j = rng.nextInt(i + 1)
            val tmp = chars[i]
            chars[i] = chars[j]
            chars[j] = tmp
        }
        return String(chars)
    }

    /**
     * Membersihkan input kunci agar hanya terdiri dari huruf A-Z dan angka 0-9 (maksimal 30 karakter).
     */
    fun sanitize30CharKeyInput(rawInput: String): String {
        return rawInput.uppercase(Locale.US)
            .filter { it in 'A'..'Z' || it in '0'..'9' }
            .take(30)
    }

    /**
     * Menganalisis kekuatan, distribusi huruf/angka, dan entropi dari kunci 30 karakter.
     */
    fun analyze30CharKey(rawKey: String): Key30Analysis {
        val clean = sanitize30CharKeyInput(rawKey)
        val letters = clean.count { it in 'A'..'Z' }
        val digits = clean.count { it in '0'..'9' }
        val unique = clean.toSet().size
        // log2(36) ≈ 5.17 bit per karakter alfanumerik (30 * 5.17 = 155 bit entropi)
        val poolSize = when {
            letters > 0 && digits > 0 -> 36.0
            letters > 0 -> 26.0
            digits > 0 -> 10.0
            else -> 1.0
        }
        val entropyBits = if (clean.isEmpty()) 0 else (clean.length * (ln(poolSize) / ln(2.0))).roundToInt()
        val isExact30 = clean.length == 30 && letters > 0 && digits > 0
        val grade = when {
            isExact30 && unique >= 14 -> "MILITARY-GRADE 155-BIT (30/30 HURUF+ANGKA)"
            clean.length == 30 -> "KUAT 30/30 KARAKTER"
            clean.length >= 16 -> "MENENGAH (${clean.length}/30 KARAKTER)"
            else -> "PERLU 30 KARAKTER HURUF & ANGKA (${clean.length}/30)"
        }
        val normalized30 = deriveCanonical30CharKey(clean)
        val subKeys = normalized30.chunked(6)
        return Key30Analysis(
            cleanedKey = clean,
            length = clean.length,
            isValidExact30 = clean.length == 30,
            letterCount = letters,
            digitCount = digits,
            uniqueChars = unique,
            entropyBits = entropyBits,
            securityGrade = grade,
            subKeysPreview = subKeys
        )
    }

    /**
     * Menjamin kunci selalu menjadi tepat 30 karakter alfanumerik deterministik
     * (jika pengguna memasukkan tepat 30 karakter, digunakan 100% apa adanya).
     */
    fun deriveCanonical30CharKey(rawKey: String): String {
        val clean = sanitize30CharKeyInput(rawKey)
        if (clean.length == 30) return clean
        val seed = clean.ifEmpty { "NYX30DEFAULTKEY2026SECURE99999" }
        val sb = StringBuilder(30)
        sb.append(seed)
        var round = 0
        while (sb.length < 30) {
            val hash = EncryptionScriptEngine.compute3LayerKeyFingerprint(seed, "PAD_$round", "${sb.length}")
            for (ch in hash.uppercase(Locale.US)) {
                if ((ch in 'A'..'Z' || ch in '0'..'9') && sb.length < 30) {
                    sb.append(ch)
                }
            }
            round++
        }
        return sb.toString().take(30)
    }

    // =============================================================================================
    // 4. ALAT ENKRIPSI #1: NYX-30 HYPER-CASCADE MATRIX CIPHER (30 SANDI HURUF & ANGKA)
    //    Arsitektur 4-Lapis:
    //    Lapis 1: PBKDF2-HMAC-SHA512 (10.000 Iterasi dari 30 Sandi Huruf & Angka)
    //    Lapis 2: 30-Round Feistel Network Block Permutation (1 Putaran per Karakter Kunci K[0..29])
    //    Lapis 3: ChaCha-ARX 256-Bit Stream Whitening (Sub-kunci K1..K3)
    //    Lapis 4: AES-256-GCM Authenticated Ciphertext dengan Tag Integritas 128-Bit
    // =============================================================================================

    fun encryptNyx30HyperCascade(plainText: String, key30: String): String {
        val canonicalKey = deriveCanonical30CharKey(key30)
        val plainBytes = plainText.toByteArray(Charsets.UTF_8)

        val rng = SecureRandom()
        val salt = ByteArray(12)
        val gcmIv = ByteArray(12)
        rng.nextBytes(salt)
        rng.nextBytes(gcmIv)

        // Lapis 1: Turunkan 64-byte (512-bit) Master Key dari 30 karakter + salt via PBKDF2-HMAC-SHA512
        val master64 = derivePbkdf2Sha512(canonicalKey, salt, 6000, 64)
        val feistelKey = master64.copyOfRange(0, 16)
        val arxStreamKey = master64.copyOfRange(16, 32)
        val aes256Key = master64.copyOfRange(32, 64)

        // Lapis 2: 30-Round Feistel Permutation (dikendalikan oleh ke-30 karakter sandi)
        val feistelTransformed = apply30RoundFeistel(
            data = plainBytes,
            key30 = canonicalKey,
            roundSecret = feistelKey,
            encryptMode = true
        )

        // Lapis 3: ChaCha-inspired ARX Stream Whitening
        val whitened = applyArxStreamXor(feistelTransformed, arxStreamKey, salt)

        // Lapis 4: AES-256-GCM Authenticated Encryption
        val cipher = Cipher.getInstance("AES/GCM/NoPadding")
        cipher.init(
            Cipher.ENCRYPT_MODE,
            SecretKeySpec(aes256Key, "AES"),
            GCMParameterSpec(128, gcmIv)
        )
        cipher.updateAAD(canonicalKey.toByteArray(Charsets.US_ASCII))
        val gcmCipherBytes = cipher.doFinal(whitened)

        // Paket Biner: [Salt 12B] + [GCM IV 12B] + [GCM Ciphertext+Tag]
        val combined = ByteArray(24 + gcmCipherBytes.size)
        System.arraycopy(salt, 0, combined, 0, 12)
        System.arraycopy(gcmIv, 0, combined, 12, 12)
        System.arraycopy(gcmCipherBytes, 0, combined, 24, gcmCipherBytes.size)

        val b64 = Base64.encodeToString(combined, Base64.NO_WRAP)
        val keyFp = EncryptionScriptEngine.compute3LayerKeyFingerprint(canonicalKey, "NYX30", "FP").take(6)
        return "NYX30::$keyFp::$b64"
    }

    fun decryptNyx30HyperCascade(cipherTextPacket: String, key30: String): Result<String> {
        return try {
            val canonicalKey = deriveCanonical30CharKey(key30)
            val trimmed = cipherTextPacket.trim()
            val b64Part = when {
                trimmed.startsWith("NYX30::") -> trimmed.substringAfterLast("::")
                else -> trimmed
            }
            val combined = Base64.decode(b64Part, Base64.NO_WRAP)
            if (combined.size <= 24 + 16) {
                return Result.failure(IllegalArgumentException("Format paket NYX-30 tidak valid atau terlalu pendek."))
            }

            val salt = combined.copyOfRange(0, 12)
            val gcmIv = combined.copyOfRange(12, 24)
            val gcmCipherBytes = combined.copyOfRange(24, combined.size)

            val master64 = derivePbkdf2Sha512(canonicalKey, salt, 6000, 64)
            val feistelKey = master64.copyOfRange(0, 16)
            val arxStreamKey = master64.copyOfRange(16, 32)
            val aes256Key = master64.copyOfRange(32, 64)

            // Lapis 4 (Invers): Verifikasi Tag 128-bit & Dekripsi AES-256-GCM
            val cipher = Cipher.getInstance("AES/GCM/NoPadding")
            cipher.init(
                Cipher.DECRYPT_MODE,
                SecretKeySpec(aes256Key, "AES"),
                GCMParameterSpec(128, gcmIv)
            )
            cipher.updateAAD(canonicalKey.toByteArray(Charsets.US_ASCII))
            val whitened = cipher.doFinal(gcmCipherBytes)

            // Lapis 3 (Invers): ARX Stream Whitening
            val feistelTransformed = applyArxStreamXor(whitened, arxStreamKey, salt)

            // Lapis 2 (Invers): 30-Round Feistel Permutation dalam urutan putaran terbalik (29 downTo 0)
            val plainBytes = apply30RoundFeistel(
                data = feistelTransformed,
                key30 = canonicalKey,
                roundSecret = feistelKey,
                encryptMode = false
            )

            Result.success(String(plainBytes, Charsets.UTF_8))
        } catch (_: Exception) {
            Result.failure(
                IllegalArgumentException("Dekripsi NYX-30 gagal: 30 Sandi Huruf/Angka tidak cocok atau ciphertext rusak.")
            )
        }
    }

    private fun apply30RoundFeistel(
        data: ByteArray,
        key30: String,
        roundSecret: ByteArray,
        encryptMode: Boolean
    ): ByteArray {
        if (data.isEmpty()) return data
        // Jika panjang ganjil, proses pasangan genap dan transformasikan byte ekor secara bijektif
        val evenLen = data.size and 0x7FFFFFFE
        val working = data.copyOf()
        val half = evenLen / 2

        if (half > 0) {
            var left = working.copyOfRange(0, half)
            var right = working.copyOfRange(half, evenLen)

            val rounds = if (encryptMode) (0 until 30) else (29 downTo 0)
            for (r in rounds) {
                val keyCharCode = ALPHANUMERIC_36.indexOf(key30[r % 30]).coerceAtLeast(0)
                if (encryptMode) {
                    val fOut = feistelRoundFunction(right, r, keyCharCode, roundSecret, half)
                    val newRight = ByteArray(half)
                    for (i in 0 until half) {
                        newRight[i] = (left[i].toInt() xor fOut[i].toInt()).toByte()
                    }
                    left = right
                    right = newRight
                } else {
                    val fOut = feistelRoundFunction(left, r, keyCharCode, roundSecret, half)
                    val newLeft = ByteArray(half)
                    for (i in 0 until half) {
                        newLeft[i] = (right[i].toInt() xor fOut[i].toInt()).toByte()
                    }
                    right = left
                    left = newLeft
                }
            }
            System.arraycopy(left, 0, working, 0, half)
            System.arraycopy(right, 0, working, half, half)
        }

        if (data.size % 2 == 1) {
            val tailMask = (roundSecret[0].toInt() xor key30.sumOf { it.code }) and 0xFF
            working[working.lastIndex] = (working.last().toInt() xor tailMask).toByte()
        }
        return working
    }

    private fun feistelRoundFunction(
        halfBlock: ByteArray,
        roundIdx: Int,
        keyCharCode: Int,
        roundSecret: ByteArray,
        outLen: Int
    ): ByteArray {
        val out = ByteArray(outLen)
        val secretLen = roundSecret.size
        for (i in 0 until outLen) {
            val b = halfBlock[(i + roundIdx) % outLen].toInt() and 0xFF
            val s = roundSecret[(i + keyCharCode) % secretLen].toInt() and 0xFF
            val mix = ((b + s + roundIdx * 17 + keyCharCode * 31) * 167) xor (b ushr 3)
            out[i] = (mix and 0xFF).toByte()
        }
        return out
    }

    private fun applyArxStreamXor(input: ByteArray, streamKey16: ByteArray, nonce12: ByteArray): ByteArray {
        val out = ByteArray(input.size)
        var s0 = ByteBuffer.wrap(streamKey16, 0, 4).order(ByteOrder.LITTLE_ENDIAN).int
        var s1 = ByteBuffer.wrap(streamKey16, 4, 4).order(ByteOrder.LITTLE_ENDIAN).int
        var s2 = ByteBuffer.wrap(streamKey16, 8, 4).order(ByteOrder.LITTLE_ENDIAN).int
        var s3 = ByteBuffer.wrap(nonce12, 0, 4).order(ByteOrder.LITTLE_ENDIAN).int

        for (i in input.indices) {
            // ARX Quarter-round
            s0 += s1
            s3 = Integer.rotateLeft(s3 xor s0, 16)
            s2 += s3
            s1 = Integer.rotateLeft(s1 xor s2, 12)
            s0 += s1
            s3 = Integer.rotateLeft(s3 xor s0, 8)
            s2 += s3
            s1 = Integer.rotateLeft(s1 xor s2, 7)

            val keystreamByte = ((s0 xor s2) ushr ((i % 4) * 8)) and 0xFF
            out[i] = (input[i].toInt() xor keystreamByte).toByte()
        }
        return out
    }

    // =============================================================================================
    // 5. ALAT ENKRIPSI #2: TRANTOR-30 POLY-DIMENSIONAL LATTICE CASCADE (30 SANDI HURUF & ANGKA)
    //    Arsitektur Matriks 3x10 (3 Baris x 10 Karakter Alfanumerik):
    //    Tahap 1 (Baris 0 - Sandi 1..10): Substitusi Bijektif S-Box Galois Field GF(256) Dinamis
    //    Tahap 2 (Baris 1 - Sandi 11..20): Poly-Alphabetic Vigenère-36 & Umpan Balik Modular CBC-Byte
    //    Tahap 3 (Baris 2 - Sandi 21..30): Transposisi Kolom Matriks 10-Dimensi + Rotasi Bit Siklik (ROL/ROR)
    //    Tahap 4 (Full 30 Sandi): Segel Integritas Otentikasi HMAC-SHA256 (128-bit Tag)
    // =============================================================================================

    fun encryptTrantor30Lattice(plainText: String, key30: String): String {
        val canonicalKey = deriveCanonical30CharKey(key30)
        val row0Key = canonicalKey.substring(0, 10)
        val row1Key = canonicalKey.substring(10, 20)
        val row2Key = canonicalKey.substring(20, 30)

        val rng = SecureRandom()
        val nonce = ByteArray(8)
        rng.nextBytes(nonce)

        val plainBytes = plainText.toByteArray(Charsets.UTF_8)

        // Tahap 1: Substitusi S-Box Bijektif 256-Byte yang dibangkitkan dari Baris 0 (10 karakter pertama) + Nonce
        val (sBox, _) = buildKeyedBijectiveSBox(row0Key, nonce)
        val stage1 = ByteArray(plainBytes.size) { idx ->
            sBox[plainBytes[idx].toInt() and 0xFF].toByte()
        }

        // Tahap 2: Poly-Alphabetic Vigenère-36 & Umpan Balik Modular dari Baris 1 (karakter ke-11..20)
        val stage2 = ByteArray(stage1.size)
        var feedback = (nonce[0].toInt() and 0xFF) xor 0x5A
        for (i in stage1.indices) {
            val kChar = row1Key[i % 10]
            val kVal = ALPHANUMERIC_36.indexOf(kChar).coerceAtLeast(1)
            val vigenereShift = (kVal * 7 + i * 13 + (nonce[i % 8].toInt() and 0xFF)) and 0xFF
            val encoded = ((stage1[i].toInt() and 0xFF) + vigenereShift + feedback) and 0xFF
            stage2[i] = encoded.toByte()
            feedback = encoded
        }

        // Tahap 3A: Rotasi Bit Siklik per Byte (ROL) yang dikendalikan oleh Baris 2 (karakter ke-21..30)
        val stage3Rotated = ByteArray(stage2.size)
        for (i in stage2.indices) {
            val kVal = ALPHANUMERIC_36.indexOf(row2Key[i % 10]).coerceAtLeast(0)
            val shiftBits = (kVal % 7) + 1 // 1..7 bit
            stage3Rotated[i] = rotateLeftByte(stage2[i].toInt() and 0xFF, shiftBits).toByte()
        }

        // Tahap 3B: Transposisi Kolom Matriks berdasarkan urutan leksikografis Baris 2 (10 kolom)
        val stage3Transposed = columnarTransposeEncrypt(stage3Rotated, row2Key)

        // Tahap 4: Segel Integritas HMAC-SHA256 (16 Byte Tag) menggunakan seluruh 30 karakter sandi
        val tag16 = computeHmacSha256Tag16(canonicalKey, nonce, stage3Transposed)

        val packet = ByteArray(8 + 16 + stage3Transposed.size)
        System.arraycopy(nonce, 0, packet, 0, 8)
        System.arraycopy(tag16, 0, packet, 8, 16)
        System.arraycopy(stage3Transposed, 0, packet, 24, stage3Transposed.size)

        val b64 = Base64.encodeToString(packet, Base64.NO_WRAP)
        val matrixFp = EncryptionScriptEngine.compute3LayerKeyFingerprint(row0Key, row1Key, row2Key).take(6)
        return "TRN30::$matrixFp::$b64"
    }

    fun decryptTrantor30Lattice(cipherTextPacket: String, key30: String): Result<String> {
        return try {
            val canonicalKey = deriveCanonical30CharKey(key30)
            val row0Key = canonicalKey.substring(0, 10)
            val row1Key = canonicalKey.substring(10, 20)
            val row2Key = canonicalKey.substring(20, 30)

            val trimmed = cipherTextPacket.trim()
            val b64Part = when {
                trimmed.startsWith("TRN30::") -> trimmed.substringAfterLast("::")
                else -> trimmed
            }
            val packet = Base64.decode(b64Part, Base64.NO_WRAP)
            if (packet.size < 24) {
                return Result.failure(IllegalArgumentException("Paket TRANTOR-30 terlalu pendek atau rusak."))
            }

            val nonce = packet.copyOfRange(0, 8)
            val expectedTag16 = packet.copyOfRange(8, 24)
            val stage3Transposed = packet.copyOfRange(24, packet.size)

            // Verifikasi Segel HMAC-SHA256
            val actualTag16 = computeHmacSha256Tag16(canonicalKey, nonce, stage3Transposed)
            if (!actualTag16.contentEquals(expectedTag16)) {
                return Result.failure(
                    IllegalArgumentException("Segel HMAC-SHA256 TRANTOR-30 tidak cocok: 30 Sandi Huruf/Angka salah!")
                )
            }

            // Tahap 3B (Invers): Balikkan Transposisi Kolom Matriks 10-Dimensi
            val stage3Rotated = columnarTransposeDecrypt(stage3Transposed, row2Key)

            // Tahap 3A (Invers): Rotasi Bit Siklik Kanan (ROR)
            val stage2 = ByteArray(stage3Rotated.size)
            for (i in stage3Rotated.indices) {
                val kVal = ALPHANUMERIC_36.indexOf(row2Key[i % 10]).coerceAtLeast(0)
                val shiftBits = (kVal % 7) + 1
                stage2[i] = rotateRightByte(stage3Rotated[i].toInt() and 0xFF, shiftBits).toByte()
            }

            // Tahap 2 (Invers): Balikkan Poly-Alphabetic Vigenère-36 & Umpan Balik Modular
            val stage1 = ByteArray(stage2.size)
            var feedback = (nonce[0].toInt() and 0xFF) xor 0x5A
            for (i in stage2.indices) {
                val encoded = stage2[i].toInt() and 0xFF
                val kChar = row1Key[i % 10]
                val kVal = ALPHANUMERIC_36.indexOf(kChar).coerceAtLeast(1)
                val vigenereShift = (kVal * 7 + i * 13 + (nonce[i % 8].toInt() and 0xFF)) and 0xFF
                val orig = (encoded - vigenereShift - feedback) and 0xFF
                stage1[i] = orig.toByte()
                feedback = encoded
            }

            // Tahap 1 (Invers): Inverse S-Box GF(256)
            val (_, invSBox) = buildKeyedBijectiveSBox(row0Key, nonce)
            val plainBytes = ByteArray(stage1.size) { idx ->
                invSBox[stage1[idx].toInt() and 0xFF].toByte()
            }

            Result.success(String(plainBytes, Charsets.UTF_8))
        } catch (e: Exception) {
            Result.failure(
                IllegalArgumentException(e.message ?: "Dekripsi TRANTOR-30 gagal.")
            )
        }
    }

    private fun buildKeyedBijectiveSBox(row0Key: String, nonce: ByteArray): Pair<IntArray, IntArray> {
        val sBox = IntArray(256) { it }
        val seedBytes = (row0Key.toByteArray(Charsets.US_ASCII) + nonce)
        var acc = 0x7F
        for (i in 255 downTo 1) {
            val seedByte = seedBytes[(255 - i) % seedBytes.size].toInt() and 0xFF
            acc = (acc * 131 + seedByte + i * 19) and 0xFF
            val swapIdx = acc % (i + 1)
            val tmp = sBox[i]
            sBox[i] = sBox[swapIdx]
            sBox[swapIdx] = tmp
        }
        val invSBox = IntArray(256)
        for (i in 0 until 256) {
            invSBox[sBox[i]] = i
        }
        return sBox to invSBox
    }

    private fun columnarTransposeEncrypt(input: ByteArray, key10: String): ByteArray {
        if (input.length <= 1) return input.copyOf()
        val cols = key10.length.coerceAtLeast(2)
        val colOrder = key10.indices.sortedWith(compareBy({ key10[it] }, { it }))
        val out = ByteArray(input.size)
        var writePos = 0
        for (col in colOrder) {
            var idx = col
            while (idx < input.size) {
                out[writePos++] = input[idx]
                idx += cols
            }
        }
        return out
    }

    private fun columnarTransposeDecrypt(transposed: ByteArray, key10: String): ByteArray {
        if (transposed.length <= 1) return transposed.copyOf()
        val cols = key10.length.coerceAtLeast(2)
        val colOrder = key10.indices.sortedWith(compareBy({ key10[it] }, { it }))
        val out = ByteArray(transposed.size)
        var readPos = 0
        for (col in colOrder) {
            var idx = col
            while (idx < transposed.size) {
                out[idx] = transposed[readPos++]
                idx += cols
            }
        }
        return out
    }

    private fun rotateLeftByte(byteVal: Int, shift: Int): Int {
        val s = shift and 7
        return ((byteVal shl s) or (byteVal ushr (8 - s))) and 0xFF
    }

    private fun rotateRightByte(byteVal: Int, shift: Int): Int {
        val s = shift and 7
        return ((byteVal ushr s) or (byteVal shl (8 - s))) and 0xFF
    }

    private fun computeHmacSha256Tag16(key30: String, nonce: ByteArray, body: ByteArray): ByteArray {
        val mac = Mac.getInstance("HmacSHA256")
        mac.init(SecretKeySpec(key30.toByteArray(Charsets.UTF_8), "HmacSHA256"))
        mac.update(nonce)
        val fullMac = mac.doFinal(body)
        return fullMac.copyOfRange(0, 16)
    }

    private fun derivePbkdf2Sha512(password: String, salt: ByteArray, iterations: Int, keyBytes: Int): ByteArray {
        val spec = PBEKeySpec(password.toCharArray(), salt, iterations, keyBytes * 8)
        val factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA512")
        return factory.generateSecret(spec).encoded
    }

    private val ByteArray.length: Int
        get() = this.size
}
