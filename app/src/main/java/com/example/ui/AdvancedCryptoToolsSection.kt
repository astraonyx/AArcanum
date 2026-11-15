package com.example.ui

import androidx.compose.foundation.background
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
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.LockOpen
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material.icons.filled.SwapVert
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.AdvancedCryptoLabEngine
import com.example.data.EnigmaCustomConfig
import com.example.data.EnigmaReflectorSpec
import com.example.data.EnigmaRotorSpec
import com.example.data.WavCamouflagePreset
import com.example.ui.theme.DuoBeeYellow
import com.example.ui.theme.DuoBeeYellowLip
import com.example.ui.theme.DuoBeetlePurple
import com.example.ui.theme.DuoBeetlePurpleLip
import com.example.ui.theme.DuoCardinalRed
import com.example.ui.theme.DuoCardinalRedLip
import com.example.ui.theme.DuoFeatherGreen
import com.example.ui.theme.DuoFeatherGreenLip
import com.example.ui.theme.DuoFoxOrange
import com.example.ui.theme.DuoFoxOrangeLip
import com.example.ui.theme.DuoHareLip
import com.example.ui.theme.DuoMacawBlue
import com.example.ui.theme.DuoMacawBlueLip
import com.example.ui.theme.DuoPureWhite
import com.example.ui.theme.DuoSwanBorder
import com.example.ui.theme.JetBrainsMonoFontFamily
import com.example.ui.theme.VaultCream
import com.example.ui.theme.VaultLime
import com.example.ui.theme.VaultObsidian
import com.example.ui.theme.VaultTextMuted
import com.example.ui.theme.duolingoButton
import com.example.ui.theme.duolingoCard

private val CryptoObsidianFace = Color(0xFF0F172A)
private val CryptoObsidianLip = Color(0xFF020617)

@Composable
private fun CryptoDuoChip(
    selected: Boolean,
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
    selectedFaceColor: Color = DuoFeatherGreen,
    selectedLipColor: Color = DuoFeatherGreenLip,
    selectedTextColor: Color = Color.White
) {
    val face = if (selected) selectedFaceColor else DuoPureWhite
    val lip = if (selected) selectedLipColor else DuoHareLip
    val txt = if (selected) selectedTextColor else VaultObsidian

    Box(
        modifier = modifier
            .height(38.dp)
            .duolingoButton(
                faceColor = face,
                lipColor = lip,
                cornerRadius = 14.dp,
                lipHeight = 4.dp
            )
            .instantTouchPress { onClick() }
            .padding(horizontal = 11.dp, vertical = 4.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            if (icon != null) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = txt,
                    modifier = Modifier.size(14.dp)
                )
            }
            Text(
                text = label,
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.ExtraBold,
                color = txt,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
private fun CryptoDuoActionButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
    faceColor: Color = DuoFeatherGreen,
    lipColor: Color = DuoFeatherGreenLip,
    textColor: Color = Color.White,
    height: Dp = 48.dp
) {
    Box(
        modifier = modifier
            .height(height)
            .duolingoButton(
                faceColor = faceColor,
                lipColor = lipColor,
                cornerRadius = 16.dp,
                lipHeight = 4.dp
            )
            .instantTouchPress { onClick() }
            .padding(horizontal = 12.dp, vertical = 4.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            if (icon != null) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = textColor,
                    modifier = Modifier.size(16.dp)
                )
            }
            Text(
                text = text,
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.ExtraBold,
                color = textColor,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
private fun cryptoDuoTextFieldColors() = OutlinedTextFieldDefaults.colors(
    focusedBorderColor = DuoMacawBlue,
    unfocusedBorderColor = DuoHareLip,
    focusedContainerColor = DuoPureWhite,
    unfocusedContainerColor = VaultCream.copy(alpha = 0.55f),
    focusedLabelColor = DuoMacawBlueLip,
    unfocusedLabelColor = VaultTextMuted
)

// =================================================================================================
// TAB 6: FITUR MORSE (TEXT <-> MORSE ENCODER/DECODER + CW AUDIO SYNTHESIZER + SIMPAN WAV LSB)
// =================================================================================================
@Composable
fun MorseCodeTransceiverSection(
    displayVaultNum: Int,
    onSaveMorseStegoWavToVault: (
        wavFileName: String,
        secretTitle: String,
        secretMessage: String,
        passcode: String
    ) -> Unit
) {
    var isTextToMorseMode by remember { mutableStateOf(true) }
    var plainTextInput by remember {
        mutableStateOf("SOS OPERASI MALAM NYX BRANKAS 01")
    }
    var morseCodeInput by remember {
        mutableStateOf(AdvancedCryptoLabEngine.encodeTextToMorse("SOS OPERASI MALAM NYX BRANKAS 01"))
    }
    var selectedWpm by remember { mutableIntStateOf(18) }
    var selectedFreqHz by remember { mutableIntStateOf(650) }
    var isPlayingMorse by remember { mutableStateOf(false) }
    var lsbSecretInsideMorse by remember {
        mutableStateOf("Pesan rahasia 1-Bit LSB di dalam gelombang bip kode Morse.")
    }
    var morseWavName by remember { mutableStateOf("Sinyal_Morse_CW_Stego") }

    DisposableEffect(Unit) {
        onDispose {
            AdvancedCryptoLabEngine.stopMorseCwAudio()
        }
    }

    val liveEncodedMorse = remember(plainTextInput) {
        AdvancedCryptoLabEngine.encodeTextToMorse(plainTextInput)
    }
    val liveDecodedText = remember(morseCodeInput) {
        AdvancedCryptoLabEngine.decodeMorseToText(morseCodeInput)
    }
    val activeMorseStream = if (isTextToMorseMode) liveEncodedMorse else morseCodeInput

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .duolingoCard(
                backgroundColor = DuoPureWhite,
                borderColor = DuoFoxOrange,
                lipColor = DuoFoxOrangeLip,
                cornerRadius = 26.dp,
                borderWidth = 2.dp,
                lipHeight = 6.dp
            )
            .padding(bottom = 6.dp)
            .testTag("morse_transceiver_card")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "TRANSCEIVER SANDI MORSE & SYNTHESIZER CW (.WAV)",
                        fontFamily = JetBrainsMonoFontFamily,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = DuoFoxOrangeLip
                    )
                    Text(
                        text = "Enkode/Dekode Morse + Suara Bip CW & Stego LSB",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.ExtraBold,
                        color = VaultObsidian
                    )
                }
                Box(
                    modifier = Modifier
                        .duolingoButton(
                            faceColor = DuoFoxOrange,
                            lipColor = DuoFoxOrangeLip,
                            cornerRadius = 50.dp,
                            lipHeight = 3.dp
                        )
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "$selectedWpm WPM • $selectedFreqHz Hz",
                        fontFamily = JetBrainsMonoFontFamily,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White
                    )
                }
            }

            // Mode Toggle: Teks -> Morse | Morse -> Teks
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                CryptoDuoChip(
                    selected = isTextToMorseMode,
                    label = "Teks → Sandi Morse",
                    icon = Icons.Default.GraphicEq,
                    selectedFaceColor = DuoFoxOrange,
                    selectedLipColor = DuoFoxOrangeLip,
                    onClick = { isTextToMorseMode = true },
                    modifier = Modifier
                        .weight(1f)
                        .testTag("morse_mode_encode_chip")
                )
                CryptoDuoChip(
                    selected = !isTextToMorseMode,
                    label = "Sandi Morse → Teks",
                    icon = Icons.Default.SwapVert,
                    selectedFaceColor = DuoMacawBlue,
                    selectedLipColor = DuoMacawBlueLip,
                    onClick = {
                        morseCodeInput = liveEncodedMorse.ifBlank { "... --- ..." }
                        isTextToMorseMode = false
                    },
                    modifier = Modifier
                        .weight(1f)
                        .testTag("morse_mode_decode_chip")
                )
            }

            if (isTextToMorseMode) {
                OutlinedTextField(
                    value = plainTextInput,
                    onValueChange = { plainTextInput = it },
                    label = { Text("Ketik Teks Pesan (Huruf, Angka, Tanda Baca)") },
                    minLines = 2,
                    maxLines = 4,
                    colors = cryptoDuoTextFieldColors(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("morse_plaintext_input"),
                    shape = RoundedCornerShape(16.dp)
                )
            } else {
                OutlinedTextField(
                    value = morseCodeInput,
                    onValueChange = { morseCodeInput = it },
                    label = { Text("Ketik Sandi Morse (Gunakan . dan - serta / untuk spasi kata)") },
                    minLines = 2,
                    maxLines = 4,
                    colors = cryptoDuoTextFieldColors(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("morse_code_raw_input"),
                    shape = RoundedCornerShape(16.dp)
                )

                // Tombol Telegraph Keyer Taktil 3D (DIT, DAH, SPASI, PEMISAH KATA, SOS)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    CryptoDuoChip(
                        selected = true,
                        label = "• DIT (.)",
                        selectedFaceColor = CryptoObsidianFace,
                        selectedLipColor = CryptoObsidianLip,
                        selectedTextColor = VaultLime,
                        onClick = { morseCodeInput += "." },
                        modifier = Modifier.testTag("morse_key_dit")
                    )
                    CryptoDuoChip(
                        selected = true,
                        label = "— DAH (-)",
                        selectedFaceColor = CryptoObsidianFace,
                        selectedLipColor = CryptoObsidianLip,
                        selectedTextColor = DuoBeeYellow,
                        onClick = { morseCodeInput += "-" },
                        modifier = Modifier.testTag("morse_key_dah")
                    )
                    CryptoDuoChip(
                        selected = false,
                        label = "Spasi Huruf",
                        onClick = { morseCodeInput += " " },
                        modifier = Modifier.testTag("morse_key_space")
                    )
                    CryptoDuoChip(
                        selected = false,
                        label = "/ Kata Baru",
                        onClick = { morseCodeInput += " / " },
                        modifier = Modifier.testTag("morse_key_word_sep")
                    )
                    CryptoDuoChip(
                        selected = false,
                        label = "Reset SOS",
                        onClick = { morseCodeInput = "... --- ..." },
                        modifier = Modifier.testTag("morse_key_sos")
                    )
                }
            }

            // Layar Output Morse / Teks Hasil Konversi (3D Dark Terminal Card)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .duolingoCard(
                        backgroundColor = CryptoObsidianFace,
                        borderColor = DuoFoxOrange,
                        lipColor = CryptoObsidianLip,
                        cornerRadius = 20.dp,
                        borderWidth = 2.dp,
                        lipHeight = 5.dp
                    )
                    .padding(bottom = 5.dp)
                    .testTag("morse_output_display_card")
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = if (isTextToMorseMode) "SINYAL SANDI MORSE ITU:" else "HASIL TERJEMAHAN TEKS:",
                            fontFamily = JetBrainsMonoFontFamily,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = DuoBeeYellow
                        )
                        Text(
                            text = "${activeMorseStream.count { it == '.' }} Dit • ${activeMorseStream.count { it == '-' }} Dah",
                            fontFamily = JetBrainsMonoFontFamily,
                            fontSize = 10.sp,
                            color = VaultLime
                        )
                    }

                    Text(
                        text = if (isTextToMorseMode) {
                            liveEncodedMorse.ifBlank { "(Ketik teks di atas)" }
                        } else {
                            liveDecodedText.ifBlank { "(Ketik sandi Morse di atas)" }
                        },
                        fontFamily = JetBrainsMonoFontFamily,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White,
                        modifier = Modifier.testTag("morse_result_text")
                    )

                    if (isTextToMorseMode && liveEncodedMorse.isNotBlank()) {
                        Text(
                            text = "Verifikasi Dekode Balik: ${AdvancedCryptoLabEngine.decodeMorseToText(liveEncodedMorse)}",
                            fontFamily = JetBrainsMonoFontFamily,
                            fontSize = 10.sp,
                            color = VaultLime
                        )
                    }
                }
            }

            // Pengaturan Kecepatan WPM & Frekuensi Nada CW (Hz)
            Text(
                text = "Kecepatan Ketukan (WPM) & Frekuensi Nada Bip CW (Hz):",
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.ExtraBold,
                color = VaultObsidian
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                listOf(12, 18, 24, 30).forEach { wpm ->
                    CryptoDuoChip(
                        selected = selectedWpm == wpm,
                        label = "$wpm WPM",
                        selectedFaceColor = DuoFoxOrange,
                        selectedLipColor = DuoFoxOrangeLip,
                        onClick = { selectedWpm = wpm }
                    )
                }
                listOf(520, 650, 780, 880).forEach { hz ->
                    CryptoDuoChip(
                        selected = selectedFreqHz == hz,
                        label = "$hz Hz",
                        selectedFaceColor = DuoBeetlePurple,
                        selectedLipColor = DuoBeetlePurpleLip,
                        onClick = { selectedFreqHz = hz }
                    )
                }
            }

            // Tombol Putar Suara Morse CW Langsung
            CryptoDuoActionButton(
                text = if (isPlayingMorse) "Hentikan Sinyal Suara Morse CW" else "Putar Suara Bip Morse CW ($selectedFreqHz Hz)",
                icon = if (isPlayingMorse) Icons.Default.Stop else Icons.Default.PlayArrow,
                faceColor = if (isPlayingMorse) DuoCardinalRed else DuoFoxOrange,
                lipColor = if (isPlayingMorse) DuoCardinalRedLip else DuoFoxOrangeLip,
                onClick = {
                    if (isPlayingMorse) {
                        AdvancedCryptoLabEngine.stopMorseCwAudio()
                        isPlayingMorse = false
                    } else {
                        AdvancedCryptoLabEngine.playMorseCwAudio(
                            morseCode = activeMorseStream,
                            wpm = selectedWpm,
                            frequencyHz = selectedFreqHz.toDouble()
                        )
                        isPlayingMorse = true
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("morse_play_cw_audio_button")
            )

            // Simpan Audio Morse + Stego LSB 1-Bit ke Brankas
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .duolingoCard(
                        backgroundColor = VaultCream,
                        borderColor = DuoFeatherGreen,
                        lipColor = DuoFeatherGreenLip,
                        cornerRadius = 20.dp,
                        borderWidth = 2.dp,
                        lipHeight = 4.dp
                    )
                    .padding(bottom = 4.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(
                        text = "Sembunyikan Pesan LSB di Dalam Rekaman Audio Morse (.WAV):",
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.ExtraBold,
                        color = VaultObsidian
                    )
                    OutlinedTextField(
                        value = lsbSecretInsideMorse,
                        onValueChange = { lsbSecretInsideMorse = it },
                        label = { Text("Pesan Rahasia Lapis Kedua (Di Dalam Bit LSB Audio Morse)") },
                        singleLine = true,
                        colors = cryptoDuoTextFieldColors(),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("morse_lsb_secret_input"),
                        shape = RoundedCornerShape(14.dp)
                    )
                    CryptoDuoActionButton(
                        text = "Simpan Audio Morse + LSB ke Brankas #$displayVaultNum",
                        icon = Icons.Default.GraphicEq,
                        faceColor = DuoFeatherGreen,
                        lipColor = DuoFeatherGreenLip,
                        onClick = {
                            AdvancedCryptoLabEngine.stopMorseCwAudio()
                            isPlayingMorse = false
                            onSaveMorseStegoWavToVault(
                                morseWavName,
                                "MORSE CW: ${activeMorseStream.take(28)}",
                                "Sandi Morse: $activeMorseStream\nTerjemahan: ${AdvancedCryptoLabEngine.decodeMorseToText(activeMorseStream)}\nRahasia LSB: $lsbSecretInsideMorse",
                                ""
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("morse_save_stego_wav_button")
                    )
                }
            }
        }
    }
}

// =================================================================================================
// TAB 7: ENIGMA FULL CUSTOM MACHINE SIMULATOR (8 ROTOR, RINGSTELLUNG, PLUGBOARD, LAMPBOARD)
// =================================================================================================
@Composable
fun EnigmaFullCustomSection() {
    var leftRotor by remember { mutableStateOf(EnigmaRotorSpec.ROTOR_I) }
    var middleRotor by remember { mutableStateOf(EnigmaRotorSpec.ROTOR_II) }
    var rightRotor by remember { mutableStateOf(EnigmaRotorSpec.ROTOR_III) }

    var leftPos by remember { mutableStateOf('A') }
    var middlePos by remember { mutableStateOf('A') }
    var rightPos by remember { mutableStateOf('A') }

    var leftRing by remember { mutableIntStateOf(1) }
    var middleRing by remember { mutableIntStateOf(1) }
    var rightRing by remember { mutableIntStateOf(1) }

    var selectedReflector by remember { mutableStateOf(EnigmaReflectorSpec.UKW_B) }
    var plugboardPairsInput by remember { mutableStateOf("AB CD EF GH IJ KL") }
    var customWiringInput by remember { mutableStateOf("") }
    var activeRotorSlotTab by remember { mutableIntStateOf(0) } // 0=Kiri, 1=Tengah, 2=Kanan

    var enigmaInputText by remember {
        mutableStateOf("OPERASI RAHASIA MALAM INI JAM DUA BELAS")
    }

    val currentConfig = remember(
        leftRotor, middleRotor, rightRotor,
        leftPos, middlePos, rightPos,
        leftRing, middleRing, rightRing,
        selectedReflector, plugboardPairsInput, customWiringInput
    ) {
        EnigmaCustomConfig(
            leftRotor = leftRotor,
            middleRotor = middleRotor,
            rightRotor = rightRotor,
            leftPosition = leftPos,
            middlePosition = middlePos,
            rightPosition = rightPos,
            leftRingSetting = leftRing,
            middleRingSetting = middleRing,
            rightRingSetting = rightRing,
            reflector = selectedReflector,
            plugboardPairs = plugboardPairsInput,
            customRightRotorWiring = customWiringInput
        )
    }

    val enigmaResult = remember(enigmaInputText, currentConfig) {
        AdvancedCryptoLabEngine.processEnigma(enigmaInputText, currentConfig)
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .duolingoCard(
                backgroundColor = DuoPureWhite,
                borderColor = DuoBeetlePurple,
                lipColor = DuoBeetlePurpleLip,
                cornerRadius = 26.dp,
                borderWidth = 2.dp,
                lipHeight = 6.dp
            )
            .padding(bottom = 6.dp)
            .testTag("enigma_full_custom_card")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "SIMULATOR MESIN ENIGMA M3/M4 FULL CUSTOM",
                        fontFamily = JetBrainsMonoFontFamily,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = DuoBeetlePurpleLip
                    )
                    Text(
                        text = "8 Rotor, Ringstellung, UKW & Steckerbrett",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.ExtraBold,
                        color = VaultObsidian
                    )
                }
                Box(
                    modifier = Modifier
                        .duolingoButton(
                            faceColor = DuoBeetlePurple,
                            lipColor = DuoBeetlePurpleLip,
                            cornerRadius = 50.dp,
                            lipHeight = 3.dp
                        )
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "${enigmaResult.activePlugboardCount} Stecker",
                        fontFamily = JetBrainsMonoFontFamily,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White
                    )
                }
            }

            // =========================================================================
            // 1. JENDELA ROTOR 3D (GRUNDSTELLUNG A-Z & RINGSTELLUNG 1-26)
            // =========================================================================
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .duolingoCard(
                        backgroundColor = CryptoObsidianFace,
                        borderColor = DuoBeetlePurple,
                        lipColor = CryptoObsidianLip,
                        cornerRadius = 22.dp,
                        borderWidth = 2.dp,
                        lipHeight = 5.dp
                    )
                    .padding(bottom = 5.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "JENDELA POSISI ROTOR AWAL → AKHIR: [$leftPos$middlePos$rightPos] → [${enigmaResult.finalLeftPos}${enigmaResult.finalMiddlePos}${enigmaResult.finalRightPos}]",
                            fontFamily = JetBrainsMonoFontFamily,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = VaultLime
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        EnigmaRotorWheelBox(
                            slotTitle = "KIRI (${leftRotor.label})",
                            posChar = leftPos,
                            ringNum = leftRing,
                            onPrevPos = { leftPos = if (leftPos == 'A') 'Z' else leftPos - 1 },
                            onNextPos = { leftPos = if (leftPos == 'Z') 'A' else leftPos + 1 },
                            onNextRing = { leftRing = (leftRing % 26) + 1 },
                            modifier = Modifier
                                .weight(1f)
                                .testTag("enigma_left_rotor_wheel")
                        )
                        EnigmaRotorWheelBox(
                            slotTitle = "TENGAH (${middleRotor.label})",
                            posChar = middlePos,
                            ringNum = middleRing,
                            onPrevPos = { middlePos = if (middlePos == 'A') 'Z' else middlePos - 1 },
                            onNextPos = { middlePos = if (middlePos == 'Z') 'A' else middlePos + 1 },
                            onNextRing = { middleRing = (middleRing % 26) + 1 },
                            modifier = Modifier
                                .weight(1f)
                                .testTag("enigma_middle_rotor_wheel")
                        )
                        EnigmaRotorWheelBox(
                            slotTitle = "KANAN (${rightRotor.label})",
                            posChar = rightPos,
                            ringNum = rightRing,
                            onPrevPos = { rightPos = if (rightPos == 'A') 'Z' else rightPos - 1 },
                            onNextPos = { rightPos = if (rightPos == 'Z') 'A' else rightPos + 1 },
                            onNextRing = { rightRing = (rightRing % 26) + 1 },
                            modifier = Modifier
                                .weight(1f)
                                .testTag("enigma_right_rotor_wheel")
                        )
                    }
                }
            }

            // =========================================================================
            // 2. PEMILIHAN 8 ROTOR (I - VIII) UNTUK SLOT KIRI, TENGAH, DAN KANAN
            // =========================================================================
            Text(
                text = "Kustomisasi Slot Rotor (Pilih Slot lalu Pilih Rotor I–VIII):",
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.ExtraBold,
                color = VaultObsidian
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                CryptoDuoChip(
                    selected = activeRotorSlotTab == 0,
                    label = "Slot Kiri: ${leftRotor.label}",
                    selectedFaceColor = DuoBeetlePurple,
                    selectedLipColor = DuoBeetlePurpleLip,
                    onClick = { activeRotorSlotTab = 0 },
                    modifier = Modifier.weight(1f)
                )
                CryptoDuoChip(
                    selected = activeRotorSlotTab == 1,
                    label = "Slot Tengah: ${middleRotor.label}",
                    selectedFaceColor = DuoBeetlePurple,
                    selectedLipColor = DuoBeetlePurpleLip,
                    onClick = { activeRotorSlotTab = 1 },
                    modifier = Modifier.weight(1f)
                )
                CryptoDuoChip(
                    selected = activeRotorSlotTab == 2,
                    label = "Slot Kanan: ${rightRotor.label}",
                    selectedFaceColor = DuoBeetlePurple,
                    selectedLipColor = DuoBeetlePurpleLip,
                    onClick = { activeRotorSlotTab = 2 },
                    modifier = Modifier.weight(1f)
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                EnigmaRotorSpec.entries.forEach { spec ->
                    val isSelected = when (activeRotorSlotTab) {
                        0 -> leftRotor == spec
                        1 -> middleRotor == spec
                        else -> rightRotor == spec
                    }
                    CryptoDuoChip(
                        selected = isSelected,
                        label = "${spec.label} (Notch ${spec.notchChars})",
                        selectedFaceColor = DuoMacawBlue,
                        selectedLipColor = DuoMacawBlueLip,
                        onClick = {
                            when (activeRotorSlotTab) {
                                0 -> leftRotor = spec
                                1 -> middleRotor = spec
                                else -> rightRotor = spec
                            }
                        },
                        modifier = Modifier.testTag("enigma_pick_rotor_${spec.name}")
                    )
                }
            }

            // =========================================================================
            // 3. PILIH REFLEKTOR (UKW-B / UKW-C / UKW-NYX) & PLUGBOARD (STECKERBRETT)
            // =========================================================================
            Text(
                text = "Pilih Reflektor (Umkehrwalze / UKW):",
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.ExtraBold,
                color = VaultObsidian
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                EnigmaReflectorSpec.entries.forEach { ukw ->
                    CryptoDuoChip(
                        selected = selectedReflector == ukw,
                        label = ukw.label,
                        selectedFaceColor = DuoFoxOrange,
                        selectedLipColor = DuoFoxOrangeLip,
                        onClick = { selectedReflector = ukw },
                        modifier = Modifier
                            .weight(1f)
                            .testTag("enigma_ukw_${ukw.name}")
                    )
                }
            }

            OutlinedTextField(
                value = plugboardPairsInput,
                onValueChange = { plugboardPairsInput = it.uppercase() },
                label = { Text("Plugboard / Steckerbrett Kustom (Pasangan 2 Huruf, misal: AB CD EF GH)") },
                singleLine = true,
                colors = cryptoDuoTextFieldColors(),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("enigma_plugboard_input"),
                shape = RoundedCornerShape(16.dp)
            )

            OutlinedTextField(
                value = customWiringInput,
                onValueChange = { customWiringInput = it.uppercase().take(26) },
                label = { Text("Kabel Rotor Kanan Kustom 26-Huruf (Opsional, kosongkan untuk standar)") },
                singleLine = true,
                colors = cryptoDuoTextFieldColors(),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("enigma_custom_wiring_input"),
                shape = RoundedCornerShape(16.dp)
            )

            // =========================================================================
            // 4. INPUT PESAN & PAPAN LAMPU ENIGMA (LAMPBOARD A-Z + HASIL RESIPROKAL)
            // =========================================================================
            OutlinedTextField(
                value = enigmaInputText,
                onValueChange = { enigmaInputText = it },
                label = { Text("Ketik Plaintext (Enkripsi) ATAU Ciphertext (Dekripsi Resiprokal)") },
                minLines = 2,
                maxLines = 4,
                colors = cryptoDuoTextFieldColors(),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("enigma_message_input"),
                shape = RoundedCornerShape(16.dp)
            )

            // Lampboard Visual & Output Ciphertext
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .duolingoCard(
                        backgroundColor = CryptoObsidianFace,
                        borderColor = DuoBeeYellow,
                        lipColor = CryptoObsidianLip,
                        cornerRadius = 22.dp,
                        borderWidth = 2.dp,
                        lipHeight = 5.dp
                    )
                    .padding(bottom = 5.dp)
                    .testTag("enigma_lampboard_output_card")
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "PAPAN LAMPU ENIGMA (LAMPBOARD) & KELUARAN SANDI:",
                            fontFamily = JetBrainsMonoFontFamily,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = DuoBeeYellow
                        )
                        Text(
                            text = "Lampu Terakhir: ${enigmaResult.lastLampChar ?: '-'}",
                            fontFamily = JetBrainsMonoFontFamily,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = VaultLime
                        )
                    }

                    // 3 Baris Lampu QWERTZ Jermanik
                    val lampRows = listOf("QWERTZUIO", "ASDFGHJK", "PYXCVBNML")
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(6.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        lampRows.forEach { rowStr ->
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                rowStr.forEach { ch ->
                                    val isLit = enigmaResult.lastLampChar == ch
                                    Box(
                                        modifier = Modifier
                                            .size(28.dp)
                                            .clip(CircleShape)
                                            .background(
                                                if (isLit) DuoBeeYellow else Color(0xFF1E293B)
                                            ),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = ch.toString(),
                                            fontFamily = JetBrainsMonoFontFamily,
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.ExtraBold,
                                            color = if (isLit) VaultObsidian else Color.White.copy(alpha = 0.7f)
                                        )
                                    }
                                }
                            }
                        }
                    }

                    Text(
                        text = enigmaResult.groupedOutputText.ifBlank { "(Ketik huruf A-Z di atas)" },
                        fontFamily = JetBrainsMonoFontFamily,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = DuoBeeYellow,
                        modifier = Modifier.testTag("enigma_output_text")
                    )

                    if (enigmaResult.stepTracePreview.isNotEmpty()) {
                        Text(
                            text = "Jejak Putaran Rotor: ${enigmaResult.stepTracePreview.take(5).joinToString(" • ")}",
                            fontFamily = JetBrainsMonoFontFamily,
                            fontSize = 9.sp,
                            color = Color.White.copy(alpha = 0.8f),
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }

            // Tombol Uji Dekripsi Resiprokal 1-Klik
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                CryptoDuoActionButton(
                    text = "Salin Hasil ke Input (Dekripsi Balik)",
                    icon = Icons.Default.SwapVert,
                    faceColor = DuoFeatherGreen,
                    lipColor = DuoFeatherGreenLip,
                    onClick = {
                        enigmaInputText = enigmaResult.outputText
                    },
                    modifier = Modifier
                        .weight(1f)
                        .testTag("enigma_reciprocal_swap_button")
                )
                CryptoDuoActionButton(
                    text = "Reset AAA",
                    icon = Icons.Default.Refresh,
                    faceColor = DuoMacawBlue,
                    lipColor = DuoMacawBlueLip,
                    onClick = {
                        leftPos = 'A'
                        middlePos = 'A'
                        rightPos = 'A'
                    },
                    modifier = Modifier.testTag("enigma_reset_positions_button")
                )
            }
        }
    }
}

@Composable
private fun EnigmaRotorWheelBox(
    slotTitle: String,
    posChar: Char,
    ringNum: Int,
    onPrevPos: () -> Unit,
    onNextPos: () -> Unit,
    onNextRing: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .duolingoCard(
                backgroundColor = Color(0xFF1E293B),
                borderColor = DuoSwanBorder.copy(alpha = 0.35f),
                lipColor = CryptoObsidianLip,
                cornerRadius = 16.dp,
                borderWidth = 1.dp,
                lipHeight = 4.dp
            )
            .padding(bottom = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text(
                text = slotTitle,
                fontFamily = JetBrainsMonoFontFamily,
                fontSize = 8.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color.White.copy(alpha = 0.8f),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(26.dp)
                        .duolingoButton(
                            faceColor = DuoPureWhite,
                            lipColor = DuoHareLip,
                            cornerRadius = 8.dp,
                            lipHeight = 2.dp
                        )
                        .instantTouchPress { onPrevPos() },
                    contentAlignment = Alignment.Center
                ) {
                    Text("-", fontWeight = FontWeight.ExtraBold, color = VaultObsidian)
                }

                Text(
                    text = posChar.toString(),
                    fontFamily = JetBrainsMonoFontFamily,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = DuoBeeYellow
                )

                Box(
                    modifier = Modifier
                        .size(26.dp)
                        .duolingoButton(
                            faceColor = DuoPureWhite,
                            lipColor = DuoHareLip,
                            cornerRadius = 8.dp,
                            lipHeight = 2.dp
                        )
                        .instantTouchPress { onNextPos() },
                    contentAlignment = Alignment.Center
                ) {
                    Text("+", fontWeight = FontWeight.ExtraBold, color = VaultObsidian)
                }
            }

            CryptoDuoChip(
                selected = true,
                label = "Ring $ringNum",
                selectedFaceColor = DuoBeetlePurple,
                selectedLipColor = DuoBeetlePurpleLip,
                onClick = onNextRing,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

// =================================================================================================
// TAB 8: 2 ALAT ENKRIPSI & DEKRIPSI TERKOMPLEKS DENGAN 30 SANDI HURUF & ANGKA (NYX-30 & TRANTOR-30)
// =================================================================================================
@Composable
fun Dual30CharComplexCiphersSection() {
    // 0 = Alat Enkripsi #1 (NYX-30 Hyper-Cascade Matrix Cipher)
    // 1 = Alat Enkripsi #2 (TRANTOR-30 Poly-Dimensional Lattice Cascade)
    var selectedEngineIdx by remember { mutableIntStateOf(0) }

    var key30Input by remember {
        mutableStateOf("NYX9A7B2C4D8E1F6G3H5J0K9L2M4N8")
    }
    var plainTextInput by remember {
        mutableStateOf("Dokumen Rahasia Negara: Koordinat Brankas Utama 06°10'S 106°49'E — Jangan Bocorkan!")
    }
    var cipherTextInput by remember {
        mutableStateOf(
            AdvancedCryptoLabEngine.encryptNyx30HyperCascade(
                "Dokumen Rahasia Negara: Koordinat Brankas Utama 06°10'S 106°49'E — Jangan Bocorkan!",
                "NYX9A7B2C4D8E1F6G3H5J0K9L2M4N8"
            )
        )
    }
    var decryptedOutput by remember {
        mutableStateOf("Dokumen Rahasia Negara: Koordinat Brankas Utama 06°10'S 106°49'E — Jangan Bocorkan!")
    }
    var operationBanner by remember {
        mutableStateOf("Siap: 30 Sandi Huruf & Angka aktif (30/30 karakter • 155-Bit Entropi).")
    }
    var isErrorBanner by remember { mutableStateOf(false) }

    val keyAnalysis = remember(key30Input) {
        AdvancedCryptoLabEngine.analyze30CharKey(key30Input)
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .duolingoCard(
                backgroundColor = DuoPureWhite,
                borderColor = DuoMacawBlue,
                lipColor = DuoMacawBlueLip,
                cornerRadius = 26.dp,
                borderWidth = 2.dp,
                lipHeight = 6.dp
            )
            .padding(bottom = 6.dp)
            .testTag("dual_30char_ciphers_card")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "2 ALAT ENKRIPSI & DEKRIPSI TERKOMPLEKS (30 SANDI HURUF & ANGKA)",
                        fontFamily = JetBrainsMonoFontFamily,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = DuoMacawBlueLip
                    )
                    Text(
                        text = "NYX-30 Hyper-Cascade & TRANTOR-30 Lattice",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.ExtraBold,
                        color = VaultObsidian
                    )
                }
                Box(
                    modifier = Modifier
                        .duolingoButton(
                            faceColor = if (keyAnalysis.isValidExact30) DuoFeatherGreen else DuoFoxOrange,
                            lipColor = if (keyAnalysis.isValidExact30) DuoFeatherGreenLip else DuoFoxOrangeLip,
                            cornerRadius = 50.dp,
                            lipHeight = 3.dp
                        )
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "${keyAnalysis.length}/30 • ${keyAnalysis.entropyBits}-Bit",
                        fontFamily = JetBrainsMonoFontFamily,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White
                    )
                }
            }

            // =========================================================================
            // PEMILIH 2 ALAT ENKRIPSI TERKOMPLEKS (NYX-30 vs TRANTOR-30)
            // =========================================================================
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                CryptoDuoChip(
                    selected = selectedEngineIdx == 0,
                    label = "Alat #1: NYX-30 Hyper-Cascade",
                    icon = Icons.Default.Shield,
                    selectedFaceColor = DuoMacawBlue,
                    selectedLipColor = DuoMacawBlueLip,
                    onClick = {
                        selectedEngineIdx = 0
                        val enc = AdvancedCryptoLabEngine.encryptNyx30HyperCascade(plainTextInput, key30Input)
                        cipherTextInput = enc
                        decryptedOutput = AdvancedCryptoLabEngine.decryptNyx30HyperCascade(enc, key30Input).getOrDefault("")
                        isErrorBanner = false
                        operationBanner = "Alat #1 Aktif: PBKDF2-SHA512 + 30-Round Feistel + ChaCha-ARX + AES-256-GCM."
                    },
                    modifier = Modifier
                        .weight(1f)
                        .testTag("select_cipher_tool_nyx30")
                )
                CryptoDuoChip(
                    selected = selectedEngineIdx == 1,
                    label = "Alat #2: TRANTOR-30 Lattice",
                    icon = Icons.Default.AutoAwesome,
                    selectedFaceColor = DuoBeetlePurple,
                    selectedLipColor = DuoBeetlePurpleLip,
                    onClick = {
                        selectedEngineIdx = 1
                        val enc = AdvancedCryptoLabEngine.encryptTrantor30Lattice(plainTextInput, key30Input)
                        cipherTextInput = enc
                        decryptedOutput = AdvancedCryptoLabEngine.decryptTrantor30Lattice(enc, key30Input).getOrDefault("")
                        isErrorBanner = false
                        operationBanner = "Alat #2 Aktif: Matriks 3x10 GF(256) S-Box + Vigenère-36 + Transposisi + HMAC-SHA256."
                    },
                    modifier = Modifier
                        .weight(1f)
                        .testTag("select_cipher_tool_trantor30")
                )
            }

            // Keterangan Arsitektur Alat yang Dipilih
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .duolingoCard(
                        backgroundColor = VaultCream,
                        borderColor = if (selectedEngineIdx == 0) DuoMacawBlue else DuoBeetlePurple,
                        lipColor = if (selectedEngineIdx == 0) DuoMacawBlueLip else DuoBeetlePurpleLip,
                        cornerRadius = 18.dp,
                        borderWidth = 2.dp,
                        lipHeight = 4.dp
                    )
                    .padding(bottom = 4.dp)
            ) {
                Column(
                    modifier = Modifier.padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = if (selectedEngineIdx == 0) {
                            "ARSITEKTUR ALAT #1 — NYX-30 HYPER-CASCADE MATRIX CIPHER:"
                        } else {
                            "ARSITEKTUR ALAT #2 — TRANTOR-30 POLY-DIMENSIONAL LATTICE CASCADE:"
                        },
                        fontFamily = JetBrainsMonoFontFamily,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = if (selectedEngineIdx == 0) DuoMacawBlueLip else DuoBeetlePurpleLip
                    )
                    Text(
                        text = if (selectedEngineIdx == 0) {
                            "• Lapis 1: PBKDF2-HMAC-SHA512 (6.000 putaran dari 30 Sandi Huruf & Angka)\n" +
                                "• Lapis 2: 30-Round Feistel Network Permutation (1 putaran per karakter K[1..30])\n" +
                                "• Lapis 3: ChaCha-ARX 256-Bit Stream Whitening + Lapis 4: AES-256-GCM Tag 128-Bit"
                        } else {
                            "• Baris Matriks 1 (Sandi 1..10): Substitusi Bijektif Galois Field GF(256) S-Box Dinamis\n" +
                                "• Baris Matriks 2 (Sandi 11..20): Poly-Alphabetic Vigenère-36 + Umpan Balik CBC Modular\n" +
                                "• Baris Matriks 3 (Sandi 21..30): Transposisi Kolom Matriks + Rotasi Bit ROL/ROR + HMAC-SHA256"
                        },
                        fontFamily = JetBrainsMonoFontFamily,
                        fontSize = 10.sp,
                        color = VaultObsidian
                    )
                }
            }

            // =========================================================================
            // STUDIO KUNCI 30 SANDI HURUF & ANGKA (A-Z, 0-9) + GENERATOR ACAK 1-KLIK
            // =========================================================================
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .duolingoCard(
                        backgroundColor = CryptoObsidianFace,
                        borderColor = DuoBeeYellow,
                        lipColor = CryptoObsidianLip,
                        cornerRadius = 20.dp,
                        borderWidth = 2.dp,
                        lipHeight = 5.dp
                    )
                    .padding(bottom = 5.dp)
                    .testTag("key_30_alphanumeric_studio_card")
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "KUNCI 30 SANDI HURUF & ANGKA (${keyAnalysis.length}/30):",
                            fontFamily = JetBrainsMonoFontFamily,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = DuoBeeYellow
                        )
                        Text(
                            text = "${keyAnalysis.letterCount} Huruf • ${keyAnalysis.digitCount} Angka",
                            fontFamily = JetBrainsMonoFontFamily,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = VaultLime
                        )
                    }

                    // Preview 5 Blok Sub-Key @ 6 Karakter
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        keyAnalysis.subKeysPreview.forEachIndexed { idx, block ->
                            Box(
                                modifier = Modifier
                                    .duolingoButton(
                                        faceColor = Color(0xFF1E293B),
                                        lipColor = CryptoObsidianLip,
                                        cornerRadius = 10.dp,
                                        lipHeight = 3.dp
                                    )
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = "K${idx + 1}:$block",
                                    fontFamily = JetBrainsMonoFontFamily,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = DuoBeeYellow
                                )
                            }
                        }
                    }

                    Text(
                        text = keyAnalysis.securityGrade,
                        fontFamily = JetBrainsMonoFontFamily,
                        fontSize = 9.sp,
                        color = Color.White.copy(alpha = 0.85f)
                    )
                }
            }

            OutlinedTextField(
                value = key30Input,
                onValueChange = { raw ->
                    key30Input = AdvancedCryptoLabEngine.sanitize30CharKeyInput(raw)
                },
                label = { Text("Masukkan 30 Sandi Huruf & Angka (A-Z, 0-9)") },
                singleLine = true,
                colors = cryptoDuoTextFieldColors(),
                leadingIcon = {
                    Icon(Icons.Default.Key, contentDescription = null, tint = DuoFoxOrange)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("key_30_alphanumeric_input"),
                shape = RoundedCornerShape(16.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                CryptoDuoActionButton(
                    text = "Acak 30 Sandi Huruf & Angka Baru",
                    icon = Icons.Default.Refresh,
                    faceColor = DuoBeeYellow,
                    lipColor = DuoBeeYellowLip,
                    textColor = VaultObsidian,
                    height = 42.dp,
                    onClick = {
                        val newKey = AdvancedCryptoLabEngine.generateRandom30CharAlphanumericKey()
                        key30Input = newKey
                        val enc = if (selectedEngineIdx == 0) {
                            AdvancedCryptoLabEngine.encryptNyx30HyperCascade(plainTextInput, newKey)
                        } else {
                            AdvancedCryptoLabEngine.encryptTrantor30Lattice(plainTextInput, newKey)
                        }
                        cipherTextInput = enc
                        decryptedOutput = plainTextInput
                        isErrorBanner = false
                        operationBanner = "30 Sandi Huruf & Angka baru berhasil dibangkitkan ($newKey)."
                    },
                    modifier = Modifier
                        .weight(1f)
                        .testTag("generate_random_30char_key_button")
                )
            }

            // =========================================================================
            // PANEL ENKRIPSI TEKS -> CIPHERTEXT
            // =========================================================================
            OutlinedTextField(
                value = plainTextInput,
                onValueChange = { plainTextInput = it },
                label = { Text("Teks Asli (Plaintext) untuk Dienkripsi dengan 30 Sandi") },
                minLines = 2,
                maxLines = 4,
                colors = cryptoDuoTextFieldColors(),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("cipher30_plaintext_input"),
                shape = RoundedCornerShape(16.dp)
            )

            CryptoDuoActionButton(
                text = if (selectedEngineIdx == 0) {
                    "Enkripsi dengan NYX-30 (30 Sandi Huruf & Angka)"
                } else {
                    "Enkripsi dengan TRANTOR-30 (30 Sandi Huruf & Angka)"
                },
                icon = Icons.Default.Lock,
                faceColor = if (selectedEngineIdx == 0) DuoMacawBlue else DuoBeetlePurple,
                lipColor = if (selectedEngineIdx == 0) DuoMacawBlueLip else DuoBeetlePurpleLip,
                height = 50.dp,
                onClick = {
                    val canonical = AdvancedCryptoLabEngine.deriveCanonical30CharKey(key30Input)
                    key30Input = canonical
                    val encrypted = if (selectedEngineIdx == 0) {
                        AdvancedCryptoLabEngine.encryptNyx30HyperCascade(plainTextInput, canonical)
                    } else {
                        AdvancedCryptoLabEngine.encryptTrantor30Lattice(plainTextInput, canonical)
                    }
                    cipherTextInput = encrypted
                    val verified = if (selectedEngineIdx == 0) {
                        AdvancedCryptoLabEngine.decryptNyx30HyperCascade(encrypted, canonical)
                    } else {
                        AdvancedCryptoLabEngine.decryptTrantor30Lattice(encrypted, canonical)
                    }
                    decryptedOutput = verified.getOrDefault("")
                    isErrorBanner = false
                    operationBanner = "Enkripsi ${if (selectedEngineIdx == 0) "NYX-30" else "TRANTOR-30"} sukses! Paket siap didekripsi."
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("cipher30_encrypt_submit_button")
            )

            // =========================================================================
            // PANEL DEKRIPSI CIPHERTEXT -> PLAINTEXT ASLI
            // =========================================================================
            OutlinedTextField(
                value = cipherTextInput,
                onValueChange = { cipherTextInput = it },
                label = { Text("Paket Ciphertext (NYX30::... atau TRN30::...) untuk Didekripsi") },
                minLines = 2,
                maxLines = 4,
                colors = cryptoDuoTextFieldColors(),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("cipher30_ciphertext_input"),
                shape = RoundedCornerShape(16.dp)
            )

            CryptoDuoActionButton(
                text = if (selectedEngineIdx == 0) {
                    "Dekripsi Paket NYX-30 dengan 30 Sandi"
                } else {
                    "Dekripsi Paket TRANTOR-30 dengan 30 Sandi"
                },
                icon = Icons.Default.LockOpen,
                faceColor = DuoFeatherGreen,
                lipColor = DuoFeatherGreenLip,
                height = 50.dp,
                onClick = {
                    val canonical = AdvancedCryptoLabEngine.deriveCanonical30CharKey(key30Input)
                    val trimmedPacket = cipherTextInput.trim()
                    val result = when {
                        trimmedPacket.startsWith("TRN30::") -> {
                            selectedEngineIdx = 1
                            AdvancedCryptoLabEngine.decryptTrantor30Lattice(trimmedPacket, canonical)
                        }
                        trimmedPacket.startsWith("NYX30::") -> {
                            selectedEngineIdx = 0
                            AdvancedCryptoLabEngine.decryptNyx30HyperCascade(trimmedPacket, canonical)
                        }
                        selectedEngineIdx == 0 ->
                            AdvancedCryptoLabEngine.decryptNyx30HyperCascade(trimmedPacket, canonical)
                        else ->
                            AdvancedCryptoLabEngine.decryptTrantor30Lattice(trimmedPacket, canonical)
                    }
                    result.fold(
                        onSuccess = { plain ->
                            decryptedOutput = plain
                            isErrorBanner = false
                            operationBanner = "Dekripsi Berhasil! Integritas 30 Sandi Huruf & Angka Terverifikasi 100%."
                        },
                        onFailure = { err ->
                            decryptedOutput = ""
                            isErrorBanner = true
                            operationBanner = err.message ?: "Dekripsi gagal: 30 Sandi tidak cocok."
                        }
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("cipher30_decrypt_submit_button")
            )

            // Kotak Hasil Dekripsi & Status Verifikasi
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .duolingoCard(
                        backgroundColor = if (isErrorBanner) DuoPureWhite else CryptoObsidianFace,
                        borderColor = if (isErrorBanner) DuoCardinalRed else DuoFeatherGreen,
                        lipColor = if (isErrorBanner) DuoCardinalRedLip else CryptoObsidianLip,
                        cornerRadius = 20.dp,
                        borderWidth = 2.dp,
                        lipHeight = 5.dp
                    )
                    .padding(bottom = 5.dp)
                    .testTag("cipher30_decrypted_result_card")
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(
                            imageVector = if (isErrorBanner) Icons.Default.Lock else Icons.Default.CheckCircle,
                            contentDescription = null,
                            tint = if (isErrorBanner) DuoCardinalRed else VaultLime,
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = operationBanner,
                            fontFamily = JetBrainsMonoFontFamily,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = if (isErrorBanner) DuoCardinalRed else VaultLime
                        )
                    }

                    if (decryptedOutput.isNotBlank()) {
                        Text(
                            text = "HASIL DEKRIPSI TEKS ASLI:",
                            fontFamily = JetBrainsMonoFontFamily,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            color = DuoBeeYellow
                        )
                        Text(
                            text = decryptedOutput,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color.White,
                            modifier = Modifier.testTag("cipher30_decrypted_output_text")
                        )
                    }
                }
            }
        }
    }
}
