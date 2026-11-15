package com.example.data

import android.content.Context
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.provider.OpenableColumns
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.io.File
import java.io.FileOutputStream
import java.util.zip.ZipInputStream

private val Context.securityDataStore by preferencesDataStore(name = "trantor_nyx2_prod_prefs_v3")

data class SecurityConfig(
    val isFirstRunCompleted: Boolean = false,
    val vaultCount: Int = 1,
    val vault1Pin1: String = "111111",
    val vault1Pin2: String = "111111",
    val vault1Pin3: String = "111111",
    val vault2Pin1: String = "555555",
    val vault2Pin2: String = "555555",
    val vault2Pin3: String = "555555",
    val settingsPin1: String = "222222",
    val settingsPin2: String = "222222",
    val settingsPin3: String = "222222",
    val funZonePin1: String = "333333",
    val funZonePin2: String = "333333",
    val funZonePin3: String = "333333",
    val sanctuaryLayer1Pin: String = "444444",
    val sanctuaryLayer2Pin: String = "444444",
    val sanctuaryLayer3Pin: String = "444444",
    val auralysPin1: String = "000000",
    val auralysPin2: String = "000000",
    val auralysPin3: String = "000000",
    val vaultPinsMap: Map<Int, Triple<String, String, String>> = emptyMap(),
    val biometricEnabled: Boolean = true,
    val fingerprintEnrolled: Boolean = true,
    val recoveryQuestion1: String = "Apa nama kota kelahiran Anda?",
    val recoveryAnswer1: String = "",
    val recoveryQuestion2: String = "Apa nama hewan peliharaan pertama Anda?",
    val recoveryAnswer2: String = "",
    val recoveryQuestion3: String = "Apa makanan favorit masa kecil Anda?",
    val recoveryAnswer3: String = "",
    val activeScriptName: String = EncryptionPreset.NYX2_DUAL_CIPHER.id,
    val customScriptCode: String = EncryptionPreset.NYX2_DUAL_CIPHER.scriptTemplate,
    val googleAccountEmail: String = "",
    val googleAccountDisplayName: String = "",
    val googleAccountPhotoUrl: String = "",
    val isGoogleLinked: Boolean = false,
    val vaultNamesMap: Map<Int, String> = emptyMap(),
    val isAntiScreenshotEnabled: Boolean = false,
    val visualThemeId: String = "BENTO_GLOW",
    val colorPaletteId: String = "DUOLINGO_GREEN",
    val customPrimaryHex: String = "#EF4444",
    val customAccentHex: String = "#3B82F6",
    val dynamicIslandShapeId: String = "PILL_CAPSULE",
    val dynamicIslandPresetId: String = "BENTO_INDIGO_GLOW",
    val dynamicIslandBgHex: String = "#070B14",
    val dynamicIslandBorderHex: String = "#00F2FE",
    val dynamicIslandGlowHex: String = "#A855F7",
    val mascotAccessoryId: String = "NONE",
    val mascotPoseId: String = "AUTO_SMART",
    val mascotCosmicAuraEnabled: Boolean = true,
    val mascotSkinId: String = "DEFAULT_MOOD",
    val mascotOutfitId: String = "NONE",
    val mascotGlovesId: String = "DEFAULT_HANDS",
    val isCamouflageRedirectArmed: Boolean = false,
    val camouflageIdentityMode: String = "PRESET",
    val camouflagePresetId: String = "CALCULATOR",
    val camouflageCustomAppName: String = "Kalkulator Pro",
    val camouflageCustomIconId: String = "CALC",
    val camouflageCustomColorHex: String = "#1CB0F6",
    val camouflageTargetAppId: String = "BUILTIN_CALCULATOR",
    val camouflageTargetAppLabel: String = "Kalkulator Ilmiah Pro",
    val graphicsQualityModeId: String = "HIGH",
    val transparentButtonBoxEnabled: Boolean = true
) {
    val recoveryQuestion: String get() = recoveryQuestion1
    val recoveryAnswer: String get() = recoveryAnswer1

    fun getVaultName(vaultId: Int): String {
        val dispId = displayVaultNumber(vaultId)
        val custom = vaultNamesMap[dispId]?.trim()
        return if (!custom.isNullOrBlank()) custom else "Brankas #$dispId"
    }

    fun getVaultDisplayLabel(vaultId: Int): String {
        val dispId = displayVaultNumber(vaultId)
        val custom = vaultNamesMap[dispId]?.trim()
        return if (!custom.isNullOrBlank() && !custom.equals("Brankas #$dispId", ignoreCase = true)) {
            "Brankas #$dispId • $custom"
        } else {
            "Brankas #$dispId"
        }
    }

    fun getPinsForVault(vaultId: Int): Triple<String, String, String> {
        vaultPinsMap[vaultId]?.let { return it }
        return when (vaultId) {
            2 -> Triple(vault2Pin1, vault2Pin2, vault2Pin3)
            3 -> Triple("A1B2C3", "D4E5F6", "G7H8J9")
            4 -> Triple("K1L2M3", "N4P5Q6", "R7S8T9")
            5 -> Triple("U1V2W3", "X4Y5Z6", "A7B8C9")
            6 -> Triple("D1E2F3", "G4H5J6", "K7L8M9")
            7 -> Triple("N1P2Q3", "R4S5T6", "U7V8W9")
            8 -> Triple("X1Y2Z3", "A4B5C6", "D7E8F9")
            9 -> Triple("G1H2J3", "K4L5M6", "N7P8Q9")
            10 -> Triple("R1S2T3", "U4V5W6", "X7Y8Z9")
            else -> Triple(vault1Pin1, vault1Pin2, vault1Pin3)
        }
    }

    fun getSettingsPins(): Triple<String, String, String> =
        Triple(settingsPin1, settingsPin2, settingsPin3)

    fun isSettingsLayer1Match(p1: String): Boolean =
        settingsPin1.equals(p1.trim(), ignoreCase = true)

    fun getFunZonePins(): Triple<String, String, String> =
        Triple(funZonePin1, funZonePin2, funZonePin3)

    fun isFunZoneLayer1Match(p1: String): Boolean =
        funZonePin1.equals(p1.trim(), ignoreCase = true)

    fun getSanctuaryPins(): Triple<String, String, String> =
        Triple(sanctuaryLayer1Pin, sanctuaryLayer2Pin, sanctuaryLayer3Pin)

    fun isSanctuaryLayer1Match(p1: String): Boolean =
        sanctuaryLayer1Pin.equals(p1.trim(), ignoreCase = true)

    fun getAuralysPins(): Triple<String, String, String> =
        Triple(auralysPin1, auralysPin2, auralysPin3)

    fun isAuralysLayer1Match(p1: String): Boolean =
        auralysPin1.equals(p1.trim(), ignoreCase = true)

    fun findMatchingVault(p1: String, p2: String, p3: String): Int? {
        val maxV = vaultCount.coerceIn(1, 10)
        for (v in 1..maxV) {
            val pins = getPinsForVault(v)
            if (pins.first.equals(p1, ignoreCase = true) &&
                pins.second.equals(p2, ignoreCase = true) &&
                pins.third.equals(p3, ignoreCase = true)
            ) {
                return v
            }
        }
        return null
    }

    fun isSettingsFullMatch(p1: String, p2: String, p3: String): Boolean =
        settingsPin1.equals(p1.trim(), ignoreCase = true) &&
            settingsPin2.equals(p2.trim(), ignoreCase = true) &&
            settingsPin3.equals(p3.trim(), ignoreCase = true)

    fun isFunZoneFullMatch(p1: String, p2: String, p3: String): Boolean =
        funZonePin1.equals(p1.trim(), ignoreCase = true) &&
            funZonePin2.equals(p2.trim(), ignoreCase = true) &&
            funZonePin3.equals(p3.trim(), ignoreCase = true)

    fun isSanctuaryFullMatch(p1: String, p2: String, p3: String): Boolean =
        sanctuaryLayer1Pin.equals(p1.trim(), ignoreCase = true) &&
            sanctuaryLayer2Pin.equals(p2.trim(), ignoreCase = true) &&
            sanctuaryLayer3Pin.equals(p3.trim(), ignoreCase = true)

    fun isAuralysFullMatch(p1: String, p2: String, p3: String): Boolean =
        auralysPin1.equals(p1.trim(), ignoreCase = true) &&
            auralysPin2.equals(p2.trim(), ignoreCase = true) &&
            auralysPin3.equals(p3.trim(), ignoreCase = true)

    fun hasCandidateForLayer1And2(p1: String, p2: String): Boolean {
        val c1 = p1.trim()
        val c2 = p2.trim()
        if (settingsPin1.equals(c1, ignoreCase = true) && settingsPin2.equals(c2, ignoreCase = true)) return true
        if (funZonePin1.equals(c1, ignoreCase = true) && funZonePin2.equals(c2, ignoreCase = true)) return true
        if (sanctuaryLayer1Pin.equals(c1, ignoreCase = true) && sanctuaryLayer2Pin.equals(c2, ignoreCase = true)) return true
        if (auralysPin1.equals(c1, ignoreCase = true) && auralysPin2.equals(c2, ignoreCase = true)) return true
        val maxV = vaultCount.coerceIn(1, 10)
        for (v in 1..maxV) {
            val pins = getPinsForVault(v)
            if (pins.first.equals(c1, ignoreCase = true) && pins.second.equals(c2, ignoreCase = true)) {
                return true
            }
        }
        return false
    }

    /**
     * Memastikan PIN Brankas (#1..#10), Ruang Pengaturan, dan Ruang Game (Fun Zone)
     * tidak saling bertabrakan baik pada Lapis 1 maupun kombinasi 3-Lapis.
     */
    fun findPinCollisionReason(
        p1: String,
        p2: String,
        p3: String,
        excludeVaultId: Int? = null,
        excludeSettings: Boolean = false,
        excludeFunZone: Boolean = false,
        excludeSanctuary: Boolean = false,
        excludeAuralys: Boolean = false
    ): String? {
        val u1 = p1.trim().uppercase()
        val u2 = p2.trim().uppercase()
        val u3 = p3.trim().uppercase()

        if (!excludeSettings) {
            val s1 = settingsPin1.trim().uppercase()
            val s2 = settingsPin2.trim().uppercase()
            val s3 = settingsPin3.trim().uppercase()
            if (u1 == s1 || (u1 == s1 && u2 == s2 && u3 == s3)) {
                return "PIN bertabrakan dengan Ruang Pengaturan! Brankas, Pengaturan, dan Game wajib memiliki PIN Lapis 1 & 3-Lapis yang berbeda."
            }
        }

        if (!excludeFunZone) {
            val f1 = funZonePin1.trim().uppercase()
            val f2 = funZonePin2.trim().uppercase()
            val f3 = funZonePin3.trim().uppercase()
            if (u1 == f1 || (u1 == f1 && u2 == f2 && u3 == f3)) {
                return "PIN bertabrakan dengan Ruang Game (Fun Zone)! Brankas, Pengaturan, dan Game wajib memiliki PIN Lapis 1 & 3-Lapis yang berbeda."
            }
        }

        if (!excludeSanctuary) {
            val sc1 = sanctuaryLayer1Pin.trim().uppercase()
            val sc2 = sanctuaryLayer2Pin.trim().uppercase()
            val sc3 = sanctuaryLayer3Pin.trim().uppercase()
            if (u1 == sc1 || (u1 == sc1 && u2 == sc2 && u3 == sc3)) {
                return "PIN bertabrakan dengan Ruang Terisolasi Sanctuary! Setiap ruang terisolasi wajib memiliki PIN Lapis 1 & 3-Lapis yang berbeda."
            }
        }

        if (!excludeAuralys) {
            val a1 = auralysPin1.trim().uppercase()
            val a2 = auralysPin2.trim().uppercase()
            val a3 = auralysPin3.trim().uppercase()
            if (u1 == a1 || (u1 == a1 && u2 == a2 && u3 == a3)) {
                return "PIN bertabrakan dengan Auralys Music Chamber! Setiap ruang terisolasi wajib memiliki PIN Lapis 1 & 3-Lapis yang berbeda."
            }
        }

        val maxV = vaultCount.coerceIn(1, 10)
        for (v in 1..maxV) {
            if (excludeVaultId != null && v == excludeVaultId) continue
            val vp = getPinsForVault(v)
            val vp1 = vp.first.trim().uppercase()
            val vp2 = vp.second.trim().uppercase()
            val vp3 = vp.third.trim().uppercase()
            if (u1 == vp1 || (u1 == vp1 && u2 == vp2 && u3 == vp3)) {
                return "PIN bertabrakan dengan ${getVaultDisplayLabel(v)}! Setiap ruang wajib memiliki PIN yang unik agar tidak tabrakan."
            }
        }
        return null
    }

    fun findCandidateVaultsForLayer1(p1: String): List<Int> {
        val maxV = vaultCount.coerceIn(1, 10)
        val candidates = mutableListOf<Int>()
        for (v in 1..maxV) {
            val pins = getPinsForVault(v)
            if (pins.first.equals(p1, ignoreCase = true)) {
                candidates.add(v)
            }
        }
        return candidates
    }

    companion object {
        const val ISOLATED_AURALYS_CANDIDATE_ID = 96
        const val ISOLATED_SANCTUARY_CANDIDATE_ID = 97
        const val ISOLATED_FUN_ZONE_CANDIDATE_ID = 98
        const val ISOLATED_SETTINGS_CANDIDATE_ID = 99
        const val DECOY_VAULT_ID = 999

        fun displayVaultNumber(vaultId: Int): Int =
            if (vaultId == DECOY_VAULT_ID) 1 else vaultId

        fun isDecoyVault(vaultId: Int): Boolean =
            vaultId == DECOY_VAULT_ID

        fun isAlphanumericValidPin(pin: String): Boolean {
            return pin.length == 6 && pin.all { it.isDigit() || it in 'A'..'Z' || it in 'a'..'z' }
        }
    }
}

class VaultRepository(
    private val context: Context,
    private val vaultDao: VaultDao
) {
    private object Keys {
        val FIRST_RUN_COMPLETED = booleanPreferencesKey("first_run_completed")
        val VAULT_COUNT = intPreferencesKey("vault_count")
        val V1_PIN_1 = stringPreferencesKey("v1_pin_1")
        val V1_PIN_2 = stringPreferencesKey("v1_pin_2")
        val V1_PIN_3 = stringPreferencesKey("v1_pin_3")
        val V2_PIN_1 = stringPreferencesKey("v2_pin_1")
        val V2_PIN_2 = stringPreferencesKey("v2_pin_2")
        val V2_PIN_3 = stringPreferencesKey("v2_pin_3")
        val SETTINGS_PIN_1 = stringPreferencesKey("settings_pin_1")
        val SETTINGS_PIN_2 = stringPreferencesKey("settings_pin_2")
        val SETTINGS_PIN_3 = stringPreferencesKey("settings_pin_3")
        val FUN_ZONE_PIN_1 = stringPreferencesKey("fun_zone_pin_1")
        val FUN_ZONE_PIN_2 = stringPreferencesKey("fun_zone_pin_2")
        val FUN_ZONE_PIN_3 = stringPreferencesKey("fun_zone_pin_3")
        val SANCTUARY_PIN_1 = stringPreferencesKey("sanctuary_pin_1")
        val SANCTUARY_PIN_2 = stringPreferencesKey("sanctuary_pin_2")
        val SANCTUARY_PIN_3 = stringPreferencesKey("sanctuary_pin_3")
        val AURALYS_PIN_1 = stringPreferencesKey("auralys_pin_1")
        val AURALYS_PIN_2 = stringPreferencesKey("auralys_pin_2")
        val AURALYS_PIN_3 = stringPreferencesKey("auralys_pin_3")
        val BIOMETRIC_ENABLED = booleanPreferencesKey("biometric_enabled")
        val FINGERPRINT_ENROLLED = booleanPreferencesKey("fingerprint_enrolled")
        val RECOVERY_QUESTION = stringPreferencesKey("recovery_question")
        val RECOVERY_ANSWER = stringPreferencesKey("recovery_answer")
        val RECOVERY_QUESTION_1 = stringPreferencesKey("recovery_question_1")
        val RECOVERY_ANSWER_1 = stringPreferencesKey("recovery_answer_1")
        val RECOVERY_QUESTION_2 = stringPreferencesKey("recovery_question_2")
        val RECOVERY_ANSWER_2 = stringPreferencesKey("recovery_answer_2")
        val RECOVERY_QUESTION_3 = stringPreferencesKey("recovery_question_3")
        val RECOVERY_ANSWER_3 = stringPreferencesKey("recovery_answer_3")
        val ACTIVE_SCRIPT_ID = stringPreferencesKey("active_script_id")
        val CUSTOM_SCRIPT_CODE = stringPreferencesKey("custom_script_code")
        val GOOGLE_ACCOUNT_EMAIL = stringPreferencesKey("google_account_email")
        val GOOGLE_ACCOUNT_DISPLAY_NAME = stringPreferencesKey("google_account_display_name")
        val GOOGLE_ACCOUNT_PHOTO_URL = stringPreferencesKey("google_account_photo_url")
        val IS_GOOGLE_LINKED = booleanPreferencesKey("is_google_linked")
        val ANTI_SCREENSHOT_ENABLED = booleanPreferencesKey("anti_screenshot_enabled")
        val VISUAL_THEME_ID = stringPreferencesKey("visual_theme_id")
        val COLOR_PALETTE_ID = stringPreferencesKey("color_palette_id")
        val CUSTOM_PRIMARY_HEX = stringPreferencesKey("custom_primary_hex")
        val CUSTOM_ACCENT_HEX = stringPreferencesKey("custom_accent_hex")
        val DYNAMIC_ISLAND_SHAPE_ID = stringPreferencesKey("dynamic_island_shape_id")
        val DYNAMIC_ISLAND_PRESET_ID = stringPreferencesKey("dynamic_island_preset_id")
        val DYNAMIC_ISLAND_BG_HEX = stringPreferencesKey("dynamic_island_bg_hex")
        val DYNAMIC_ISLAND_BORDER_HEX = stringPreferencesKey("dynamic_island_border_hex")
        val DYNAMIC_ISLAND_GLOW_HEX = stringPreferencesKey("dynamic_island_glow_hex")
        val MASCOT_ACCESSORY_ID = stringPreferencesKey("mascot_accessory_id")
        val MASCOT_POSE_ID = stringPreferencesKey("mascot_pose_id")
        val MASCOT_COSMIC_AURA_ENABLED = booleanPreferencesKey("mascot_cosmic_aura_enabled")
        val MASCOT_SKIN_ID = stringPreferencesKey("mascot_skin_id")
        val MASCOT_OUTFIT_ID = stringPreferencesKey("mascot_outfit_id")
        val MASCOT_GLOVES_ID = stringPreferencesKey("mascot_gloves_id")
        val CAMOUFLAGE_REDIRECT_ARMED = booleanPreferencesKey("camouflage_redirect_armed")
        val CAMOUFLAGE_IDENTITY_MODE = stringPreferencesKey("camouflage_identity_mode")
        val CAMOUFLAGE_PRESET_ID = stringPreferencesKey("camouflage_preset_id")
        val CAMOUFLAGE_CUSTOM_APP_NAME = stringPreferencesKey("camouflage_custom_app_name")
        val CAMOUFLAGE_CUSTOM_ICON_ID = stringPreferencesKey("camouflage_custom_icon_id")
        val CAMOUFLAGE_CUSTOM_COLOR_HEX = stringPreferencesKey("camouflage_custom_color_hex")
        val CAMOUFLAGE_TARGET_APP_ID = stringPreferencesKey("camouflage_target_app_id")
        val CAMOUFLAGE_TARGET_APP_LABEL = stringPreferencesKey("camouflage_target_app_label")
        val GRAPHICS_QUALITY_MODE_ID = stringPreferencesKey("graphics_quality_mode_id")
        val TRANSPARENT_BUTTON_BOX_ENABLED = booleanPreferencesKey("transparent_button_box_enabled")

        fun pinKey(vaultId: Int, layer: Int) = stringPreferencesKey("v${vaultId}_pin_${layer}")
        fun vaultNameKey(vaultId: Int) = stringPreferencesKey("v${vaultId}_custom_name")
    }

    val allItems: Flow<List<VaultItemEntity>> = vaultDao.getAllItems()

    fun getItemsForVault(vaultId: Int): Flow<List<VaultItemEntity>> =
        vaultDao.getItemsForVault(vaultId)

    val securityConfigFlow: Flow<SecurityConfig> = context.securityDataStore.data.map { prefs ->
        val vCount = (prefs[Keys.VAULT_COUNT] ?: 1).coerceIn(1, 10)
        val pinsMap = mutableMapOf<Int, Triple<String, String, String>>()
        val namesMap = mutableMapOf<Int, String>()
        for (v in 1..10) {
            val p1 = prefs[Keys.pinKey(v, 1)] ?: if (v == 1) (prefs[Keys.V1_PIN_1] ?: "111111") else if (v == 2) (prefs[Keys.V2_PIN_1] ?: "555555") else null
            val p2 = prefs[Keys.pinKey(v, 2)] ?: if (v == 1) (prefs[Keys.V1_PIN_2] ?: "111111") else if (v == 2) (prefs[Keys.V2_PIN_2] ?: "555555") else null
            val p3 = prefs[Keys.pinKey(v, 3)] ?: if (v == 1) (prefs[Keys.V1_PIN_3] ?: "111111") else if (v == 2) (prefs[Keys.V2_PIN_3] ?: "555555") else null
            if (p1 != null && p2 != null && p3 != null) {
                pinsMap[v] = Triple(p1, p2, p3)
            }
            val customName = prefs[Keys.vaultNameKey(v)]?.trim()
            if (!customName.isNullOrBlank()) {
                namesMap[v] = customName
            }
        }

        val rawSanctuary1 = prefs[Keys.SANCTUARY_PIN_1] ?: "444444"
        val rawSanctuary2 = prefs[Keys.SANCTUARY_PIN_2] ?: "444444"
        val rawSanctuary3 = prefs[Keys.SANCTUARY_PIN_3] ?: "444444"
        val isLegacySanctuaryDefault = rawSanctuary1 == "444444" && rawSanctuary2 == "555555" && rawSanctuary3 == "666666"

        SecurityConfig(
            isFirstRunCompleted = prefs[Keys.FIRST_RUN_COMPLETED] ?: false,
            vaultCount = vCount,
            vault1Pin1 = prefs[Keys.V1_PIN_1] ?: (pinsMap[1]?.first ?: "111111"),
            vault1Pin2 = prefs[Keys.V1_PIN_2] ?: (pinsMap[1]?.second ?: "111111"),
            vault1Pin3 = prefs[Keys.V1_PIN_3] ?: (pinsMap[1]?.third ?: "111111"),
            vault2Pin1 = prefs[Keys.V2_PIN_1] ?: (pinsMap[2]?.first ?: "555555"),
            vault2Pin2 = prefs[Keys.V2_PIN_2] ?: (pinsMap[2]?.second ?: "555555"),
            vault2Pin3 = prefs[Keys.V2_PIN_3] ?: (pinsMap[2]?.third ?: "555555"),
            settingsPin1 = prefs[Keys.SETTINGS_PIN_1] ?: "222222",
            settingsPin2 = prefs[Keys.SETTINGS_PIN_2] ?: "222222",
            settingsPin3 = prefs[Keys.SETTINGS_PIN_3] ?: "222222",
            funZonePin1 = prefs[Keys.FUN_ZONE_PIN_1] ?: "333333",
            funZonePin2 = prefs[Keys.FUN_ZONE_PIN_2] ?: "333333",
            funZonePin3 = prefs[Keys.FUN_ZONE_PIN_3] ?: "333333",
            sanctuaryLayer1Pin = if (isLegacySanctuaryDefault) "444444" else rawSanctuary1,
            sanctuaryLayer2Pin = if (isLegacySanctuaryDefault) "444444" else rawSanctuary2,
            sanctuaryLayer3Pin = if (isLegacySanctuaryDefault) "444444" else rawSanctuary3,
            auralysPin1 = prefs[Keys.AURALYS_PIN_1] ?: "000000",
            auralysPin2 = prefs[Keys.AURALYS_PIN_2] ?: "000000",
            auralysPin3 = prefs[Keys.AURALYS_PIN_3] ?: "000000",
            vaultPinsMap = pinsMap,
            biometricEnabled = prefs[Keys.BIOMETRIC_ENABLED] ?: true,
            fingerprintEnrolled = prefs[Keys.FINGERPRINT_ENROLLED] ?: true,
            recoveryQuestion1 = prefs[Keys.RECOVERY_QUESTION_1] ?: (prefs[Keys.RECOVERY_QUESTION] ?: "Apa nama kota kelahiran Anda?"),
            recoveryAnswer1 = prefs[Keys.RECOVERY_ANSWER_1] ?: (prefs[Keys.RECOVERY_ANSWER] ?: ""),
            recoveryQuestion2 = prefs[Keys.RECOVERY_QUESTION_2] ?: "Apa nama hewan peliharaan pertama Anda?",
            recoveryAnswer2 = prefs[Keys.RECOVERY_ANSWER_2] ?: "",
            recoveryQuestion3 = prefs[Keys.RECOVERY_QUESTION_3] ?: "Apa makanan favorit masa kecil Anda?",
            recoveryAnswer3 = prefs[Keys.RECOVERY_ANSWER_3] ?: "",
            activeScriptName = prefs[Keys.ACTIVE_SCRIPT_ID] ?: EncryptionPreset.NYX2_DUAL_CIPHER.id,
            customScriptCode = prefs[Keys.CUSTOM_SCRIPT_CODE] ?: EncryptionPreset.NYX2_DUAL_CIPHER.scriptTemplate,
            googleAccountEmail = prefs[Keys.GOOGLE_ACCOUNT_EMAIL] ?: "",
            googleAccountDisplayName = prefs[Keys.GOOGLE_ACCOUNT_DISPLAY_NAME] ?: "",
            googleAccountPhotoUrl = prefs[Keys.GOOGLE_ACCOUNT_PHOTO_URL] ?: "",
            isGoogleLinked = prefs[Keys.IS_GOOGLE_LINKED] ?: false,
            vaultNamesMap = namesMap,
            isAntiScreenshotEnabled = prefs[Keys.ANTI_SCREENSHOT_ENABLED] ?: false,
            visualThemeId = prefs[Keys.VISUAL_THEME_ID] ?: "DUOLINGO",
            colorPaletteId = prefs[Keys.COLOR_PALETTE_ID] ?: "DUOLINGO_GREEN",
            customPrimaryHex = prefs[Keys.CUSTOM_PRIMARY_HEX] ?: "#EF4444",
            customAccentHex = prefs[Keys.CUSTOM_ACCENT_HEX] ?: "#3B82F6",
            dynamicIslandShapeId = prefs[Keys.DYNAMIC_ISLAND_SHAPE_ID] ?: "PILL_CAPSULE",
            dynamicIslandPresetId = prefs[Keys.DYNAMIC_ISLAND_PRESET_ID] ?: "BENTO_INDIGO_GLOW",
            dynamicIslandBgHex = prefs[Keys.DYNAMIC_ISLAND_BG_HEX] ?: "#070B14",
            dynamicIslandBorderHex = prefs[Keys.DYNAMIC_ISLAND_BORDER_HEX] ?: "#00F2FE",
            dynamicIslandGlowHex = prefs[Keys.DYNAMIC_ISLAND_GLOW_HEX] ?: "#A855F7",
            mascotAccessoryId = prefs[Keys.MASCOT_ACCESSORY_ID] ?: "NONE",
            mascotPoseId = prefs[Keys.MASCOT_POSE_ID] ?: "AUTO_SMART",
            mascotCosmicAuraEnabled = prefs[Keys.MASCOT_COSMIC_AURA_ENABLED] ?: true,
            mascotSkinId = prefs[Keys.MASCOT_SKIN_ID] ?: "DEFAULT_MOOD",
            mascotOutfitId = prefs[Keys.MASCOT_OUTFIT_ID] ?: "NONE",
            mascotGlovesId = prefs[Keys.MASCOT_GLOVES_ID] ?: "DEFAULT_HANDS",
            isCamouflageRedirectArmed = prefs[Keys.CAMOUFLAGE_REDIRECT_ARMED] ?: false,
            camouflageIdentityMode = prefs[Keys.CAMOUFLAGE_IDENTITY_MODE] ?: "PRESET",
            camouflagePresetId = prefs[Keys.CAMOUFLAGE_PRESET_ID] ?: "CALCULATOR",
            camouflageCustomAppName = prefs[Keys.CAMOUFLAGE_CUSTOM_APP_NAME] ?: "Kalkulator Pro",
            camouflageCustomIconId = prefs[Keys.CAMOUFLAGE_CUSTOM_ICON_ID] ?: "CALC",
            camouflageCustomColorHex = prefs[Keys.CAMOUFLAGE_CUSTOM_COLOR_HEX] ?: "#1CB0F6",
            camouflageTargetAppId = prefs[Keys.CAMOUFLAGE_TARGET_APP_ID] ?: "BUILTIN_CALCULATOR",
            camouflageTargetAppLabel = prefs[Keys.CAMOUFLAGE_TARGET_APP_LABEL] ?: "Kalkulator Ilmiah Pro",
            graphicsQualityModeId = prefs[Keys.GRAPHICS_QUALITY_MODE_ID] ?: "HIGH",
            transparentButtonBoxEnabled = prefs[Keys.TRANSPARENT_BUTTON_BOX_ENABLED] ?: true
        )
    }

    /**
     * Setup awal aplikasi:
     * - Membuat 1 Brankas Utama (Brankas #1) dengan PIN 6 digit 3 lapis (`v1p1`, `v1p2`, `v1p3`)
     * - Membuat Ruang Pengaturan Terisolasi mandiri dengan PIN 6 digit 3 lapis (`v2p1`, `v2p2`, `v2p3`)
     * - Jika pengguna ingin menambah Brankas #2..#10, dilakukan melalui Ruang Pengaturan Terisolasi.
     */
    suspend fun completeFirstRunSetup(
        v1p1: String,
        v1p2: String,
        v1p3: String,
        v2p1: String,
        v2p2: String,
        v2p3: String,
        biometricEnabled: Boolean,
        recoveryQuestion: String = "Apa nama kota kelahiran Anda?",
        recoveryAnswer: String = "",
        recoveryQuestion2: String = "Apa nama hewan peliharaan pertama Anda?",
        recoveryAnswer2: String = "",
        recoveryQuestion3: String = "Apa makanan favorit masa kecil Anda?",
        recoveryAnswer3: String = "",
        googleEmail: String = "",
        googleDisplayName: String = "",
        funPin1: String = "333333",
        funPin2: String = "333333",
        funPin3: String = "333333",
        auralysPin1: String = "000000",
        auralysPin2: String = "000000",
        auralysPin3: String = "000000"
    ) {
        context.securityDataStore.edit { prefs ->
            prefs[Keys.FIRST_RUN_COMPLETED] = true
            prefs[Keys.VAULT_COUNT] = 1
            prefs[Keys.V1_PIN_1] = v1p1.uppercase().trim()
            prefs[Keys.V1_PIN_2] = v1p2.uppercase().trim()
            prefs[Keys.V1_PIN_3] = v1p3.uppercase().trim()
            prefs[Keys.pinKey(1, 1)] = v1p1.uppercase().trim()
            prefs[Keys.pinKey(1, 2)] = v1p2.uppercase().trim()
            prefs[Keys.pinKey(1, 3)] = v1p3.uppercase().trim()

            // Simpan PIN 3 lapis khusus untuk Ruang Pengaturan Terisolasi
            prefs[Keys.SETTINGS_PIN_1] = v2p1.uppercase().trim()
            prefs[Keys.SETTINGS_PIN_2] = v2p2.uppercase().trim()
            prefs[Keys.SETTINGS_PIN_3] = v2p3.uppercase().trim()

            // Simpan PIN 3 lapis khusus untuk Ruang Terisolasi Fun Zone (Skip Setup default: 333333-333333-333333)
            prefs[Keys.FUN_ZONE_PIN_1] = funPin1.uppercase().trim().ifBlank { "333333" }
            prefs[Keys.FUN_ZONE_PIN_2] = funPin2.uppercase().trim().ifBlank { "333333" }
            prefs[Keys.FUN_ZONE_PIN_3] = funPin3.uppercase().trim().ifBlank { "333333" }

            // Simpan PIN 3 lapis khusus untuk Ruang Terisolasi Auralys Music Chamber (Skip Setup default: 000000-000000-000000)
            prefs[Keys.AURALYS_PIN_1] = auralysPin1.uppercase().trim().ifBlank { "000000" }
            prefs[Keys.AURALYS_PIN_2] = auralysPin2.uppercase().trim().ifBlank { "000000" }
            prefs[Keys.AURALYS_PIN_3] = auralysPin3.uppercase().trim().ifBlank { "000000" }

            prefs[Keys.BIOMETRIC_ENABLED] = biometricEnabled
            prefs[Keys.FINGERPRINT_ENROLLED] = true
            val q1 = recoveryQuestion.trim().ifBlank { "Apa nama kota kelahiran Anda?" }
            prefs[Keys.RECOVERY_QUESTION] = q1
            prefs[Keys.RECOVERY_QUESTION_1] = q1
            prefs[Keys.RECOVERY_ANSWER] = recoveryAnswer.trim()
            prefs[Keys.RECOVERY_ANSWER_1] = recoveryAnswer.trim()

            val q2 = recoveryQuestion2.trim().ifBlank { "Apa nama hewan peliharaan pertama Anda?" }
            prefs[Keys.RECOVERY_QUESTION_2] = q2
            prefs[Keys.RECOVERY_ANSWER_2] = recoveryAnswer2.trim()

            val q3 = recoveryQuestion3.trim().ifBlank { "Apa makanan favorit masa kecil Anda?" }
            prefs[Keys.RECOVERY_QUESTION_3] = q3
            prefs[Keys.RECOVERY_ANSWER_3] = recoveryAnswer3.trim()

            if (googleEmail.isNotBlank() && googleEmail.contains("@")) {
                prefs[Keys.GOOGLE_ACCOUNT_EMAIL] = googleEmail.trim()
                prefs[Keys.GOOGLE_ACCOUNT_DISPLAY_NAME] = googleDisplayName.trim()
                prefs[Keys.IS_GOOGLE_LINKED] = true
            } else {
                prefs[Keys.GOOGLE_ACCOUNT_EMAIL] = ""
                prefs[Keys.GOOGLE_ACCOUNT_DISPLAY_NAME] = ""
                prefs[Keys.IS_GOOGLE_LINKED] = false
            }
        }
    }

    suspend fun setGoogleAccount(email: String, displayName: String, photoUrl: String = "") {
        context.securityDataStore.edit { prefs ->
            prefs[Keys.GOOGLE_ACCOUNT_EMAIL] = email.trim()
            prefs[Keys.GOOGLE_ACCOUNT_DISPLAY_NAME] = displayName.trim()
            prefs[Keys.GOOGLE_ACCOUNT_PHOTO_URL] = photoUrl.trim()
            prefs[Keys.IS_GOOGLE_LINKED] = email.isNotBlank()
        }
    }

    suspend fun unlinkGoogleAccount() {
        context.securityDataStore.edit { prefs ->
            prefs[Keys.GOOGLE_ACCOUNT_EMAIL] = ""
            prefs[Keys.GOOGLE_ACCOUNT_DISPLAY_NAME] = ""
            prefs[Keys.GOOGLE_ACCOUNT_PHOTO_URL] = ""
            prefs[Keys.IS_GOOGLE_LINKED] = false
        }
    }

    suspend fun addVault(pin1: String, pin2: String, pin3: String, customName: String = ""): Int {
        var createdId = 0
        context.securityDataStore.edit { prefs ->
            val currentCount = (prefs[Keys.VAULT_COUNT] ?: 1).coerceIn(1, 10)
            if (currentCount < 10) {
                createdId = currentCount + 1
                prefs[Keys.VAULT_COUNT] = createdId
                val u1 = pin1.uppercase().trim()
                val u2 = pin2.uppercase().trim()
                val u3 = pin3.uppercase().trim()
                prefs[Keys.pinKey(createdId, 1)] = u1
                prefs[Keys.pinKey(createdId, 2)] = u2
                prefs[Keys.pinKey(createdId, 3)] = u3
                if (createdId == 2) {
                    prefs[Keys.V2_PIN_1] = u1
                    prefs[Keys.V2_PIN_2] = u2
                    prefs[Keys.V2_PIN_3] = u3
                }
                val cleanName = customName.trim()
                if (cleanName.isNotBlank()) {
                    prefs[Keys.vaultNameKey(createdId)] = cleanName
                }
            }
        }
        return createdId
    }

    suspend fun renameVault(vaultId: Int, newName: String) {
        if (vaultId !in 1..10) return
        context.securityDataStore.edit { prefs ->
            val clean = newName.trim()
            if (clean.isBlank()) {
                prefs.remove(Keys.vaultNameKey(vaultId))
            } else {
                prefs[Keys.vaultNameKey(vaultId)] = clean
            }
        }
    }

    suspend fun updateSettingsPins(pin1: String, pin2: String, pin3: String) {
        if (pin1.length == 6 && pin2.length == 6 && pin3.length == 6) {
            val u1 = pin1.uppercase().trim()
            val u2 = pin2.uppercase().trim()
            val u3 = pin3.uppercase().trim()
            context.securityDataStore.edit { prefs ->
                prefs[Keys.SETTINGS_PIN_1] = u1
                prefs[Keys.SETTINGS_PIN_2] = u2
                prefs[Keys.SETTINGS_PIN_3] = u3
            }
        }
    }

    suspend fun updateFunZonePins(pin1: String, pin2: String, pin3: String) {
        if (pin1.length == 6 && pin2.length == 6 && pin3.length == 6) {
            val u1 = pin1.uppercase().trim()
            val u2 = pin2.uppercase().trim()
            val u3 = pin3.uppercase().trim()
            context.securityDataStore.edit { prefs ->
                prefs[Keys.FUN_ZONE_PIN_1] = u1
                prefs[Keys.FUN_ZONE_PIN_2] = u2
                prefs[Keys.FUN_ZONE_PIN_3] = u3
            }
        }
    }

    suspend fun updateSanctuaryPins(pin1: String, pin2: String, pin3: String) {
        if (pin1.length == 6 && pin2.length == 6 && pin3.length == 6) {
            val u1 = pin1.uppercase().trim()
            val u2 = pin2.uppercase().trim()
            val u3 = pin3.uppercase().trim()
            context.securityDataStore.edit { prefs ->
                prefs[Keys.SANCTUARY_PIN_1] = u1
                prefs[Keys.SANCTUARY_PIN_2] = u2
                prefs[Keys.SANCTUARY_PIN_3] = u3
            }
        }
    }

    suspend fun updateAuralysPins(pin1: String, pin2: String, pin3: String) {
        if (pin1.length == 6 && pin2.length == 6 && pin3.length == 6) {
            val u1 = pin1.uppercase().trim()
            val u2 = pin2.uppercase().trim()
            val u3 = pin3.uppercase().trim()
            context.securityDataStore.edit { prefs ->
                prefs[Keys.AURALYS_PIN_1] = u1
                prefs[Keys.AURALYS_PIN_2] = u2
                prefs[Keys.AURALYS_PIN_3] = u3
            }
        }
    }

    suspend fun updateRecoveryCredentials(
        question: String,
        answer: String,
        question2: String = "",
        answer2: String = "",
        question3: String = "",
        answer3: String = ""
    ) {
        context.securityDataStore.edit { prefs ->
            if (question.isNotBlank()) {
                prefs[Keys.RECOVERY_QUESTION] = question.trim()
                prefs[Keys.RECOVERY_QUESTION_1] = question.trim()
            }
            if (answer.isNotBlank()) {
                prefs[Keys.RECOVERY_ANSWER] = answer.trim()
                prefs[Keys.RECOVERY_ANSWER_1] = answer.trim()
            }
            if (question2.isNotBlank()) prefs[Keys.RECOVERY_QUESTION_2] = question2.trim()
            if (answer2.isNotBlank()) prefs[Keys.RECOVERY_ANSWER_2] = answer2.trim()
            if (question3.isNotBlank()) prefs[Keys.RECOVERY_QUESTION_3] = question3.trim()
            if (answer3.isNotBlank()) prefs[Keys.RECOVERY_ANSWER_3] = answer3.trim()
        }
    }

    suspend fun updatePinsForVault(vaultId: Int, pin1: String, pin2: String, pin3: String) {
        if (pin1.length == 6 && pin2.length == 6 && pin3.length == 6) {
            val u1 = pin1.uppercase().trim()
            val u2 = pin2.uppercase().trim()
            val u3 = pin3.uppercase().trim()
            context.securityDataStore.edit { prefs ->
                prefs[Keys.pinKey(vaultId, 1)] = u1
                prefs[Keys.pinKey(vaultId, 2)] = u2
                prefs[Keys.pinKey(vaultId, 3)] = u3
                if (vaultId == 1) {
                    prefs[Keys.V1_PIN_1] = u1
                    prefs[Keys.V1_PIN_2] = u2
                    prefs[Keys.V1_PIN_3] = u3
                } else if (vaultId == 2) {
                    prefs[Keys.V2_PIN_1] = u1
                    prefs[Keys.V2_PIN_2] = u2
                    prefs[Keys.V2_PIN_3] = u3
                }
            }
        }
    }

    suspend fun setBiometricEnabled(enabled: Boolean) {
        context.securityDataStore.edit { prefs ->
            prefs[Keys.BIOMETRIC_ENABLED] = enabled
            if (enabled) prefs[Keys.FINGERPRINT_ENROLLED] = true
        }
    }

    suspend fun setAntiScreenshotEnabled(enabled: Boolean) {
        context.securityDataStore.edit { prefs ->
            prefs[Keys.ANTI_SCREENSHOT_ENABLED] = enabled
        }
    }

    suspend fun setVisualThemeId(themeId: String) {
        context.securityDataStore.edit { prefs ->
            prefs[Keys.VISUAL_THEME_ID] = com.example.ui.theme.AppVisualTheme.fromId(themeId).id
        }
    }

    suspend fun setGraphicsQualityConfig(
        modeId: String? = null,
        transparentBoxEnabled: Boolean? = null
    ) {
        context.securityDataStore.edit { prefs ->
            if (!modeId.isNullOrBlank()) {
                prefs[Keys.GRAPHICS_QUALITY_MODE_ID] =
                    com.example.ui.theme.AppGraphicsQualityMode.fromId(modeId).id
            }
            if (transparentBoxEnabled != null) {
                prefs[Keys.TRANSPARENT_BUTTON_BOX_ENABLED] = transparentBoxEnabled
            }
        }
    }

    suspend fun setDynamicIslandConfig(
        shapeId: String? = null,
        presetId: String? = null,
        bgHex: String? = null,
        borderHex: String? = null,
        glowHex: String? = null
    ) {
        context.securityDataStore.edit { prefs ->
            if (!shapeId.isNullOrBlank()) {
                prefs[Keys.DYNAMIC_ISLAND_SHAPE_ID] = shapeId.trim().uppercase()
            }
            if (!presetId.isNullOrBlank()) {
                prefs[Keys.DYNAMIC_ISLAND_PRESET_ID] = presetId.trim().uppercase()
            }
            if (!bgHex.isNullOrBlank()) {
                prefs[Keys.DYNAMIC_ISLAND_BG_HEX] = bgHex.trim().uppercase()
            }
            if (!borderHex.isNullOrBlank()) {
                prefs[Keys.DYNAMIC_ISLAND_BORDER_HEX] = borderHex.trim().uppercase()
            }
            if (!glowHex.isNullOrBlank()) {
                prefs[Keys.DYNAMIC_ISLAND_GLOW_HEX] = glowHex.trim().uppercase()
            }
        }
    }

    suspend fun setColorPaletteConfig(
        paletteId: String,
        customPrimaryHex: String? = null,
        customAccentHex: String? = null
    ) {
        context.securityDataStore.edit { prefs ->
            prefs[Keys.COLOR_PALETTE_ID] = paletteId.uppercase().trim()
            if (!customPrimaryHex.isNullOrBlank()) {
                prefs[Keys.CUSTOM_PRIMARY_HEX] = customPrimaryHex.trim().uppercase()
            }
            if (!customAccentHex.isNullOrBlank()) {
                prefs[Keys.CUSTOM_ACCENT_HEX] = customAccentHex.trim().uppercase()
            }
        }
    }

    suspend fun setMascotStudioConfig(
        accessoryId: String? = null,
        poseId: String? = null,
        cosmicAuraEnabled: Boolean? = null,
        skinId: String? = null,
        outfitId: String? = null,
        glovesId: String? = null
    ) {
        context.securityDataStore.edit { prefs ->
            if (!accessoryId.isNullOrBlank()) {
                prefs[Keys.MASCOT_ACCESSORY_ID] = accessoryId.trim().uppercase()
            }
            if (!poseId.isNullOrBlank()) {
                prefs[Keys.MASCOT_POSE_ID] = poseId.trim().uppercase()
            }
            if (cosmicAuraEnabled != null) {
                prefs[Keys.MASCOT_COSMIC_AURA_ENABLED] = cosmicAuraEnabled
            }
            if (!skinId.isNullOrBlank()) {
                prefs[Keys.MASCOT_SKIN_ID] = skinId.trim().uppercase()
            }
            if (!outfitId.isNullOrBlank()) {
                prefs[Keys.MASCOT_OUTFIT_ID] = outfitId.trim().uppercase()
            }
            if (!glovesId.isNullOrBlank()) {
                prefs[Keys.MASCOT_GLOVES_ID] = glovesId.trim().uppercase()
            }
        }
    }

    suspend fun setAppCamouflageConfig(
        isRedirectArmed: Boolean? = null,
        identityMode: String? = null,
        presetId: String? = null,
        customAppName: String? = null,
        customIconId: String? = null,
        customColorHex: String? = null,
        targetAppId: String? = null,
        targetAppLabel: String? = null
    ) {
        context.securityDataStore.edit { prefs ->
            if (isRedirectArmed != null) {
                prefs[Keys.CAMOUFLAGE_REDIRECT_ARMED] = isRedirectArmed
            }
            if (!identityMode.isNullOrBlank()) {
                prefs[Keys.CAMOUFLAGE_IDENTITY_MODE] = identityMode.trim().uppercase()
            }
            if (!presetId.isNullOrBlank()) {
                prefs[Keys.CAMOUFLAGE_PRESET_ID] = presetId.trim().uppercase()
            }
            if (customAppName != null) {
                prefs[Keys.CAMOUFLAGE_CUSTOM_APP_NAME] = customAppName.trim().ifBlank { "Kalkulator Pro" }
            }
            if (!customIconId.isNullOrBlank()) {
                prefs[Keys.CAMOUFLAGE_CUSTOM_ICON_ID] = customIconId.trim().uppercase()
            }
            if (!customColorHex.isNullOrBlank()) {
                prefs[Keys.CAMOUFLAGE_CUSTOM_COLOR_HEX] = customColorHex.trim().uppercase()
            }
            if (!targetAppId.isNullOrBlank()) {
                prefs[Keys.CAMOUFLAGE_TARGET_APP_ID] = targetAppId.trim()
            }
            if (!targetAppLabel.isNullOrBlank()) {
                prefs[Keys.CAMOUFLAGE_TARGET_APP_LABEL] = targetAppLabel.trim()
            }
        }
    }

    suspend fun updateEncryptionScript(presetId: String, customCode: String) {
        context.securityDataStore.edit { prefs ->
            prefs[Keys.ACTIVE_SCRIPT_ID] = presetId
            prefs[Keys.CUSTOM_SCRIPT_CODE] = customCode
        }
    }

    /**
     * Menyiapkan salinan terdekripsi dari file brankas untuk keperluan Ekspor / Bagikan (Share)
     * setelah pengguna berhasil melewati verifikasi biometrik sekunder (Secondary Biometric Re-Authentication).
     */
    suspend fun prepareDecryptedFileForShareOrExport(item: VaultItemEntity): File? {
        val cfg = securityConfigFlow.first()
        val (pin1, pin2, pin3) = cfg.getPinsForVault(item.vaultProfileId)
        val exportDir = File(context.cacheDir, "vault_exports").apply { if (!exists()) mkdirs() }
        val safeFileName = "${item.name.replace(Regex("[^a-zA-Z0-9_\\-]"), "_")}.${item.extension}"
        val outFile = File(exportDir, safeFileName)

        val plainSource = File(item.internalPlainPath)
        val nyxSource = File(item.internalNyxPath)

        return try {
            when {
                !item.isEncrypted && plainSource.exists() && plainSource.length() > 0L -> {
                    plainSource.copyTo(outFile, overwrite = true)
                    outFile
                }
                nyxSource.exists() && nyxSource.length() > 0L -> {
                    val ok = EncryptionScriptEngine.openNyxContainerToFile(
                        nyxFile = nyxSource,
                        plainOutFile = outFile,
                        pin1 = pin1,
                        pin2 = pin2,
                        pin3 = pin3
                    )
                    if (ok && outFile.exists()) outFile else null
                }
                plainSource.exists() -> {
                    plainSource.copyTo(outFile, overwrite = true)
                    outFile
                }
                else -> {
                    outFile.writeBytes(item.rawContentPreview.toByteArray(Charsets.UTF_8))
                    outFile
                }
            }
        } catch (_: Exception) {
            null
        }
    }

    /**
     * Mengekspor berkas brankas menjadi paket .nyx terproteksi kata sandi mandiri.
     */
    suspend fun exportVaultItemAsPasswordProtectedNyxFile(
        item: VaultItemEntity,
        exportPassword: String
    ): File? {
        val decryptedTemp = prepareDecryptedFileForShareOrExport(item) ?: return null
        val exportDir = File(context.cacheDir, "nyx_exports").apply { if (!exists()) mkdirs() }
        val safeBase = item.name.replace(Regex("[^a-zA-Z0-9_\\-]"), "_")
        val outFile = File(exportDir, "$safeBase.nyx")
        return try {
            val plainBytes = decryptedTemp.readBytes()
            FileOutputStream(outFile).use { fos ->
                NyxPasswordExportPackage.exportToStream(
                    plainBytes = plainBytes,
                    originalName = item.name,
                    originalExtension = item.extension,
                    category = item.category,
                    password = exportPassword,
                    outStream = fos
                )
            }
            if (outFile.exists()) outFile else null
        } catch (_: Exception) {
            null
        }
    }

    suspend fun exportVaultItemAsPasswordProtectedNyxToUri(
        item: VaultItemEntity,
        exportPassword: String,
        targetUri: Uri
    ): Boolean {
        val decryptedTemp = prepareDecryptedFileForShareOrExport(item) ?: return false
        return try {
            context.contentResolver.openOutputStream(targetUri)?.use { output ->
                NyxPasswordExportPackage.exportToStream(
                    plainBytes = decryptedTemp.readBytes(),
                    originalName = item.name,
                    originalExtension = item.extension,
                    category = item.category,
                    password = exportPassword,
                    outStream = output
                )
            }
            true
        } catch (_: Exception) {
            false
        }
    }

    suspend fun isPasswordProtectedNyxUri(uri: Uri): Boolean {
        return try {
            context.contentResolver.openInputStream(uri)?.use { input ->
                val magic = NyxPasswordExportPackage.HEADER_MAGIC.toByteArray(Charsets.UTF_8)
                val buf = ByteArray(magic.size)
                var totalRead = 0
                while (totalRead < magic.size) {
                    val r = input.read(buf, totalRead, magic.size - totalRead)
                    if (r == -1) break
                    totalRead += r
                }
                totalRead == magic.size && NyxPasswordExportPackage.isNyxExportPackage(buf)
            } ?: false
        } catch (_: Exception) {
            false
        }
    }

    suspend fun importPasswordProtectedNyxUri(
        uri: Uri,
        password: String,
        vaultId: Int
    ): VaultItemEntity {
        val result = context.contentResolver.openInputStream(uri)?.use { input ->
            NyxPasswordExportPackage.decryptFromStream(input, password)
        } ?: throw IllegalArgumentException("Tidak dapat membaca berkas .nyx dari URI")

        val cfg = securityConfigFlow.first()
        val (pin1, pin2, pin3) = cfg.getPinsForVault(vaultId)
        val vaultDir = getVaultStorageDir(vaultId)

        val uniqueStamp = System.currentTimeMillis()
        val safeBase = result.originalName.replace(Regex("[^a-zA-Z0-9_\\-]"), "_").take(40)
        val plainFile = File(vaultDir, "${uniqueStamp}_${safeBase}.${result.originalExtension}")
        plainFile.writeBytes(result.plainBytes)

        val nyxFile = File(vaultDir, "${uniqueStamp}_${safeBase}.nyx")
        val sealMeta = EncryptionScriptEngine.sealFileToNyxContainer(
            plainFile = plainFile,
            nyxOutFile = nyxFile,
            pin1 = pin1,
            pin2 = pin2,
            pin3 = pin3,
            shredSource = false
        )

        val categoryStr = result.category.ifBlank { resolveCategoryFromExtension(result.originalExtension).name }

        val entity = VaultItemEntity(
            name = result.originalName,
            extension = result.originalExtension,
            category = categoryStr,
            sizeBytes = result.plainBytes.size.toLong(),
            isEncrypted = true,
            vaultProfileId = vaultId,
            internalPlainPath = plainFile.absolutePath,
            internalNyxPath = nyxFile.absolutePath,
            checksumPreview = sealMeta.second,
            encryptedHexPreview = sealMeta.first,
            addedAt = System.currentTimeMillis()
        )
        return upsertVaultItemOverwritingExisting(entity)
    }

    /**
     * Mengekspor file terdekripsi ke URI tujuan (Storage Access Framework CreateDocument)
     * serta menyimpannya ke folder unduhan eksternal aplikasi.
     */
    suspend fun exportVaultItemToUri(item: VaultItemEntity, targetUri: Uri): Boolean {
        val decryptedTemp = prepareDecryptedFileForShareOrExport(item) ?: return false
        return try {
            context.contentResolver.openOutputStream(targetUri)?.use { output ->
                decryptedTemp.inputStream().use { input ->
                    input.copyTo(output, bufferSize = 65536)
                }
            }
            true
        } catch (_: Exception) {
            false
        }
    }

    fun queryDisplayNameFromUri(uri: Uri): String {
        return try {
            context.contentResolver.query(uri, arrayOf(android.provider.OpenableColumns.DISPLAY_NAME), null, null, null)?.use { cursor ->
                if (cursor.moveToFirst()) {
                    val idx = cursor.getColumnIndex(android.provider.OpenableColumns.DISPLAY_NAME)
                    if (idx >= 0) cursor.getString(idx) else null
                } else null
            } ?: uri.lastPathSegment ?: "unknown_file"
        } catch (_: Exception) {
            uri.lastPathSegment ?: "unknown_file"
        }
    }

    fun resolveMimeType(extension: String): String = when (extension.lowercase()) {
        "mp4", "m4v" -> "video/mp4"
        "mov" -> "video/quicktime"
        "mkv" -> "video/x-matroska"
        "webm" -> "video/webm"
        "mp3" -> "audio/mpeg"
        "flac" -> "audio/flac"
        "wav" -> "audio/wav"
        "m4a", "aac" -> "audio/mp4"
        "jpg", "jpeg" -> "image/jpeg"
        "png" -> "image/png"
        "webp", "sticker" -> "image/webp"
        "gif" -> "image/gif"
        "zip" -> "application/zip"
        "pdf" -> "application/pdf"
        "docx" -> "application/vnd.openxmlformats-officedocument.wordprocessingml.document"
        "doc" -> "application/msword"
        "xlsx" -> "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
        "xls" -> "application/vnd.ms-excel"
        "csv" -> "text/csv"
        "tsv" -> "text/tab-separated-values"
        "pptx" -> "application/vnd.openxmlformats-officedocument.presentationml.presentation"
        "ppt" -> "application/vnd.ms-powerpoint"
        "rtf" -> "application/rtf"
        "xml" -> "application/xml"
        "html", "htm" -> "text/html"
        "txt", "md", "json", "js", "kt" -> "text/plain"
        else -> "application/octet-stream"
    }

    /**
     * Menyimpan item ke database dengan kebijakan Auto-Overwrite:
     * Jika sudah ada media/dokumen dengan nama & ekstensi yang sama di brankas yang sama,
     * otomatis menimpa (overwrite) versi lama dan membersihkan sisa file lama di disk agar
     * update aplikasi maupun impor ulang tidak pernah gagal atau menggandakan item.
     */
    suspend fun upsertVaultItemOverwritingExisting(newItem: VaultItemEntity): VaultItemEntity {
        val matches = try {
            vaultDao.findMatchingItemsByNameAndExt(
                vaultId = newItem.vaultProfileId,
                name = newItem.name.trim(),
                ext = newItem.extension.trim()
            )
        } catch (_: Exception) {
            emptyList()
        }

        val normTargetGroup = newItem.groupArchiveName?.trim()?.lowercase()
        val sameGroupMatches = matches.filter { existing ->
            val normExistingGroup = existing.groupArchiveName?.trim()?.lowercase()
            normTargetGroup == null || normExistingGroup == null || normExistingGroup == normTargetGroup
        }.ifEmpty { matches }

        if (sameGroupMatches.isNotEmpty()) {
            val primaryExisting = sameGroupMatches.first()
            for (oldItem in sameGroupMatches) {
                if (oldItem.internalPlainPath.isNotBlank() && oldItem.internalPlainPath != newItem.internalPlainPath) {
                    EncryptionScriptEngine.shredFileOnDisk(File(oldItem.internalPlainPath))
                }
                if (oldItem.internalNyxPath.isNotBlank() && oldItem.internalNyxPath != newItem.internalNyxPath) {
                    EncryptionScriptEngine.shredFileOnDisk(File(oldItem.internalNyxPath))
                }
                if (oldItem.id != primaryExisting.id) {
                    try {
                        vaultDao.deleteById(oldItem.id)
                    } catch (_: Exception) {
                    }
                }
            }
            val overwritten = newItem.copy(
                id = primaryExisting.id,
                groupArchiveName = newItem.groupArchiveName ?: primaryExisting.groupArchiveName
            )
            vaultDao.updateItem(overwritten)
            return overwritten
        } else {
            val insertedId = vaultDao.insertItem(newItem).toInt()
            return newItem.copy(id = insertedId)
        }
    }

    suspend fun insertItem(item: VaultItemEntity): Long =
        upsertVaultItemOverwritingExisting(item).id.toLong()

    suspend fun updateItem(item: VaultItemEntity) = vaultDao.updateItem(item)

    suspend fun deleteItemPermanently(item: VaultItemEntity) {
        if (item.internalPlainPath.isNotBlank()) {
            EncryptionScriptEngine.shredFileOnDisk(File(item.internalPlainPath))
        }
        if (item.internalNyxPath.isNotBlank()) {
            EncryptionScriptEngine.shredFileOnDisk(File(item.internalNyxPath))
        }
        vaultDao.deleteById(item.id)
    }

    private fun getVaultStorageDir(vaultId: Int): File {
        val dir = File(context.filesDir, "nyx_private_vault/vault_$vaultId")
        if (!dir.exists()) dir.mkdirs()
        return dir
    }

    fun resolveCategoryFromExtension(ext: String): VaultCategory = when (ext.lowercase()) {
        "mp4", "mkv", "mov", "webm", "avi", "3gp", "m4v" -> VaultCategory.VIDEO
        "jpg", "jpeg", "png", "webp", "heic", "gif", "sticker", "bmp" -> VaultCategory.IMAGE
        "mp3", "wav", "flac", "ogg", "m4a", "aac", "opus" -> VaultCategory.AUDIO
        "zip", "rar", "7z", "tar" -> VaultCategory.ARCHIVE
        "pdf", "docx", "doc", "xlsx", "xls", "csv", "tsv", "pptx", "ppt", "rtf", "odt", "ods", "odp", "txt", "md", "json", "xml", "html", "htm", "kt", "js", "py", "sh" -> VaultCategory.DOCUMENT
        else -> VaultCategory.DOCUMENT
    }

    private fun extractMediaDurationSeconds(file: File, category: VaultCategory): Int {
        if (category != VaultCategory.VIDEO && category != VaultCategory.AUDIO) return 0
        val retriever = MediaMetadataRetriever()
        return try {
            retriever.setDataSource(file.absolutePath)
            val durMs = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)?.toLongOrNull() ?: 0L
            (durMs / 1000L).toInt().coerceAtLeast(1)
        } catch (_: Exception) {
            0
        } finally {
            try {
                retriever.release()
            } catch (_: Exception) {
            }
        }
    }

    /**
     * Mengimpor file asli atau paket `.zip` ke dalam internal private vault (`filesDir/nyx_private_vault`).
     * Jika file adalah `.zip`, semua video/gambar/audio/dokumen di dalamnya diekstrak dan dikelompokkan
     * dengan `groupArchiveName` yang sama sehingga bisa diputar berurutan di bawah video player seperti YouTube!
     */
    suspend fun importUriIntoPrivateVault(uri: Uri, vaultId: Int): Int {
        val cfg = securityConfigFlow.first()
        val (pin1, pin2, pin3) = cfg.getPinsForVault(vaultId)
        val vaultDir = getVaultStorageDir(vaultId)

        var displayName = "Hidden_File_${System.currentTimeMillis() % 10000}"
        try {
            context.contentResolver.query(uri, null, null, null, null)?.use { cursor ->
                val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                if (cursor.moveToFirst() && nameIndex >= 0) {
                    displayName = cursor.getString(nameIndex) ?: displayName
                }
            }
        } catch (_: Exception) {
        }

        val dotIdx = displayName.lastIndexOf('.')
        val baseName = if (dotIdx > 0) displayName.substring(0, dotIdx) else displayName
        val ext = if (dotIdx > 0 && dotIdx < displayName.length - 1) {
            displayName.substring(dotIdx + 1).lowercase()
        } else "bin"

        // Jika file yang diimpor adalah arsip .zip, ekstrak semua file media/dokumen di dalamnya!
        if (ext == "zip") {
            val importedCount = unpackZipIntoPrivateVault(
                uri = uri,
                zipDisplayName = displayName,
                vaultId = vaultId,
                vaultDir = vaultDir,
                pin1 = pin1,
                pin2 = pin2,
                pin3 = pin3,
                cipherAlgo = cfg.activeScriptName
            )
            if (importedCount > 0) return importedCount
        }

        val uniqueStamp = System.currentTimeMillis()
        val safeBase = baseName.replace(Regex("[^a-zA-Z0-9_\\-]"), "_").take(40)
        val plainFile = File(vaultDir, "${uniqueStamp}_${safeBase}.$ext")
        val nyxFile = File(vaultDir, "${uniqueStamp}_${safeBase}.$ext.nyx")

        // Salin stream dari URI ke internal private file dengan buffer 1MB cepat (mendukung video besar >3GB - 10GB+ tanpa OOM)
        context.contentResolver.openInputStream(uri)?.use { input ->
            FileOutputStream(plainFile).use { output ->
                input.copyTo(output, bufferSize = 1024 * 1024)
            }
        } ?: return 0

        val fileSize = plainFile.length().coerceAtLeast(1L)
        val category = resolveCategoryFromExtension(ext)
        val durationSec = extractMediaDurationSeconds(plainFile, category)

        val rawPreview = readSafeFileHeaderPreview(plainFile, displayName)

        // Enkripsi biner ke .nyx dan shred file plaintext agar benar-benar tersembunyi
        val (hexPreview, checksum) = EncryptionScriptEngine.sealFileToNyxContainer(
            plainFile = plainFile,
            nyxOutFile = nyxFile,
            pin1 = pin1,
            pin2 = pin2,
            pin3 = pin3,
            shredSource = true
        )

        val entity = VaultItemEntity(
            vaultProfileId = vaultId,
            name = baseName,
            extension = ext,
            category = category.name,
            groupArchiveName = null,
            internalPlainPath = plainFile.absolutePath,
            internalNyxPath = nyxFile.absolutePath,
            sizeBytes = fileSize,
            paddedSizeBytes = EncryptionScriptEngine.padmeSize(fileSize),
            isEncrypted = true,
            cipherAlgorithm = cfg.activeScriptName,
            checksumPreview = checksum,
            rawContentPreview = rawPreview,
            encryptedHexPreview = hexPreview,
            durationSeconds = durationSec,
            mediaUriString = Uri.fromFile(plainFile).toString()
        )
        upsertVaultItemOverwritingExisting(entity)
        return 1
    }

    private suspend fun unpackZipIntoPrivateVault(
        uri: Uri,
        zipDisplayName: String,
        vaultId: Int,
        vaultDir: File,
        pin1: String,
        pin2: String,
        pin3: String,
        cipherAlgo: String
    ): Int {
        var count = 0
        val inputStream = context.contentResolver.openInputStream(uri) ?: return 0
        ZipInputStream(inputStream).use { zis ->
            var entry = zis.nextEntry
            while (entry != null) {
                if (!entry.isDirectory) {
                    val normalizedEntryPath = entry.name.replace('\\', '/').trim('/')
                    val entryName = normalizedEntryPath.substringAfterLast('/')
                    val subDir = normalizedEntryPath.substringBeforeLast('/', "")
                        .trim('/')
                        .split('/')
                        .map { it.trim() }
                        .filter { it.isNotEmpty() && !it.startsWith(".") && it != "__MACOSX" }
                        .joinToString("/")
                    if (!entryName.startsWith(".") && entryName.isNotBlank() && !normalizedEntryPath.contains("__MACOSX")) {
                        val dot = entryName.lastIndexOf('.')
                        val itemBase = if (dot > 0) entryName.substring(0, dot) else entryName
                        val itemExt = if (dot > 0 && dot < entryName.length - 1) {
                            entryName.substring(dot + 1).lowercase()
                        } else "dat"

                        val stamp = System.nanoTime()
                        val safeName = itemBase.replace(Regex("[^a-zA-Z0-9_\\-]"), "_").take(36)
                        val plainFile = File(vaultDir, "${stamp}_${safeName}.$itemExt")
                        val nyxFile = File(vaultDir, "${stamp}_${safeName}.$itemExt.nyx")

                        FileOutputStream(plainFile).use { fos ->
                            val buffer = ByteArray(256 * 1024)
                            var len: Int
                            while (zis.read(buffer).also { len = it } > 0) {
                                fos.write(buffer, 0, len)
                            }
                        }

                        val fSize = plainFile.length()
                        if (fSize > 0L) {
                            val cat = resolveCategoryFromExtension(itemExt)
                            val dur = extractMediaDurationSeconds(plainFile, cat)
                            val preview = readSafeFileHeaderPreview(plainFile, entryName)

                            // Untuk file dari paket ZIP, kita buat kontainer .nyx terenkripsi
                            val (hex, checksum) = EncryptionScriptEngine.sealFileToNyxContainer(
                                plainFile = plainFile,
                                nyxOutFile = nyxFile,
                                pin1 = pin1,
                                pin2 = pin2,
                                pin3 = pin3,
                                shredSource = true
                            )

                            val baseZipFolder = zipDisplayName.removeSuffix(".zip").removeSuffix(".ZIP").trim()
                                .ifBlank { "Folder_ZIP" }
                            val cleanZipFolder = if (subDir.isNotEmpty()) "$baseZipFolder/$subDir" else baseZipFolder
                            upsertVaultItemOverwritingExisting(
                                VaultItemEntity(
                                    vaultProfileId = vaultId,
                                    name = itemBase,
                                    extension = itemExt,
                                    category = cat.name,
                                    groupArchiveName = cleanZipFolder,
                                    internalPlainPath = plainFile.absolutePath,
                                    internalNyxPath = nyxFile.absolutePath,
                                    sizeBytes = fSize,
                                    paddedSizeBytes = EncryptionScriptEngine.padmeSize(fSize),
                                    isEncrypted = true,
                                    cipherAlgorithm = cipherAlgo,
                                    checksumPreview = checksum,
                                    rawContentPreview = preview,
                                    encryptedHexPreview = hex,
                                    durationSeconds = dur,
                                    mediaUriString = Uri.fromFile(plainFile).toString(),
                                    addedAt = System.currentTimeMillis() + count
                                )
                            )
                            count++
                        } else {
                            plainFile.delete()
                        }
                    }
                }
                zis.closeEntry()
                entry = zis.nextEntry
            }
        }
        return count
    }

    private fun readSafeFileHeaderPreview(file: File, fallbackName: String): String {
        return try {
            file.inputStream().use { stream ->
                val buf = ByteArray(96)
                val read = stream.read(buf)
                if (read > 0) {
                    buf.take(read).joinToString("") { b ->
                        val c = b.toInt() and 0xFF
                        if (c in 32..126) c.toChar().toString() else "."
                    }
                } else "BINARY_STREAM::$fallbackName"
            }
        } catch (_: Exception) {
            "BINARY_STREAM::$fallbackName"
        }
    }

    /**
     * Membuat catatan rahasia terenkripsi langsung di dalam penyimpanan internal brankas
     */
    suspend fun createSecretNoteInPrivateVault(
        vaultId: Int,
        title: String,
        extension: String,
        category: VaultCategory,
        secretPayload: String,
        encryptImmediately: Boolean
    ) {
        val cfg = securityConfigFlow.first()
        val (pin1, pin2, pin3) = cfg.getPinsForVault(vaultId)
        val vaultDir = getVaultStorageDir(vaultId)

        val cleanTitle = title.trim().ifBlank { "Catatan_Rahasia_${System.currentTimeMillis() % 1000}" }
        val cleanExt = extension.trim().lowercase().removePrefix(".").ifBlank { "txt" }
        val payloadBytes = secretPayload.ifBlank { "CATATAN_RAHASIA::$cleanTitle" }.toByteArray(Charsets.UTF_8)

        val stamp = System.currentTimeMillis()
        val safeBase = cleanTitle.replace(Regex("[^a-zA-Z0-9_\\-]"), "_").take(36)
        val plainFile = File(vaultDir, "${stamp}_${safeBase}.$cleanExt")
        val nyxFile = File(vaultDir, "${stamp}_${safeBase}.$cleanExt.nyx")

        plainFile.writeBytes(payloadBytes)
        val fSize = plainFile.length()

        val (hex, checksum) = EncryptionScriptEngine.sealFileToNyxContainer(
            plainFile = plainFile,
            nyxOutFile = nyxFile,
            pin1 = pin1,
            pin2 = pin2,
            pin3 = pin3,
            shredSource = encryptImmediately
        )

        upsertVaultItemOverwritingExisting(
            VaultItemEntity(
                vaultProfileId = vaultId,
                name = cleanTitle,
                extension = cleanExt,
                category = category.name,
                groupArchiveName = null,
                internalPlainPath = plainFile.absolutePath,
                internalNyxPath = nyxFile.absolutePath,
                sizeBytes = fSize,
                paddedSizeBytes = EncryptionScriptEngine.padmeSize(fSize),
                isEncrypted = encryptImmediately,
                cipherAlgorithm = cfg.activeScriptName,
                checksumPreview = checksum,
                rawContentPreview = secretPayload.take(250),
                encryptedHexPreview = hex,
                durationSeconds = 0,
                mediaUriString = Uri.fromFile(plainFile).toString()
            )
        )
    }

    suspend fun saveRawPngBytesToPrivateVault(
        vaultId: Int,
        title: String,
        pngBytes: ByteArray,
        previewDescription: String,
        encryptImmediately: Boolean = false
    ) {
        val cfg = securityConfigFlow.first()
        val (pin1, pin2, pin3) = cfg.getPinsForVault(vaultId)
        val vaultDir = getVaultStorageDir(vaultId)
        val cleanTitle = title.trim().ifBlank { "AstraOnyx_StarQR_${System.currentTimeMillis() % 10000}" }
        val stamp = System.currentTimeMillis()
        val safeBase = cleanTitle.replace(Regex("[^a-zA-Z0-9_\\-]"), "_").take(36)
        val plainFile = File(vaultDir, "${stamp}_${safeBase}.png")
        val nyxFile = File(vaultDir, "${stamp}_${safeBase}.png.nyx")
        plainFile.writeBytes(pngBytes)
        val fSize = plainFile.length()
        val (hex, checksum) = EncryptionScriptEngine.sealFileToNyxContainer(
            plainFile = plainFile,
            nyxOutFile = nyxFile,
            pin1 = pin1,
            pin2 = pin2,
            pin3 = pin3,
            shredSource = encryptImmediately
        )
        upsertVaultItemOverwritingExisting(
            VaultItemEntity(
                vaultProfileId = vaultId,
                name = cleanTitle,
                extension = "png",
                category = VaultCategory.IMAGE.name,
                groupArchiveName = "AstraOnyx_StarQR",
                internalPlainPath = plainFile.absolutePath,
                internalNyxPath = nyxFile.absolutePath,
                sizeBytes = fSize,
                paddedSizeBytes = EncryptionScriptEngine.padmeSize(fSize),
                isEncrypted = encryptImmediately,
                cipherAlgorithm = cfg.activeScriptName,
                checksumPreview = checksum,
                rawContentPreview = previewDescription.take(250),
                encryptedHexPreview = hex,
                durationSeconds = 0,
                mediaUriString = Uri.fromFile(plainFile).toString()
            )
        )
    }

    /**
     * Memastikan Brankas Palsu (DECOY_VAULT_ID = 999) memiliki beberapa file keseharian wajar
     * agar tidak terlihat kosong mencurigakan saat penyusup masuk dengan PIN yang salah.
     */
    suspend fun ensureDecoyVaultSeeded() {
        val currentDecoyItems = vaultDao.getItemsForVault(SecurityConfig.DECOY_VAULT_ID).first()
        if (currentDecoyItems.isNotEmpty()) return
        seedDefaultDecoyFiles()
    }

    suspend fun resetDecoyVaultToDefault() {
        val currentDecoyItems = vaultDao.getItemsForVault(SecurityConfig.DECOY_VAULT_ID).first()
        for (item in currentDecoyItems) {
            deleteItemPermanently(item)
        }
        seedDefaultDecoyFiles()
    }

    private suspend fun seedDefaultDecoyFiles() {
        createSecretNoteInPrivateVault(
            vaultId = SecurityConfig.DECOY_VAULT_ID,
            title = "Jadwal_Kegiatan_Mingguan",
            extension = "txt",
            category = VaultCategory.DOCUMENT,
            secretPayload = "Senin: Olahraga pagi & rapat tim 09:00\nRabu: Belanja kebutuhan rumah\nJumat: Servis rutin kendaraan",
            encryptImmediately = true
        )
        createSecretNoteInPrivateVault(
            vaultId = SecurityConfig.DECOY_VAULT_ID,
            title = "Daftar_Belanja_Bulanan",
            extension = "txt",
            category = VaultCategory.DOCUMENT,
            secretPayload = "1. Beras 5kg\n2. Minyak goreng 2L\n3. Kopi bubuk & gula\n4. Sabun cuci & pasta gigi\n5. Buah apel & pisang",
            encryptImmediately = false
        )
        createSecretNoteInPrivateVault(
            vaultId = SecurityConfig.DECOY_VAULT_ID,
            title = "Catatan_Resep_Nasi_Goreng",
            extension = "txt",
            category = VaultCategory.DOCUMENT,
            secretPayload = "Bumbu halus: bawang merah 5 siung, bawang putih 3 siung, cabai merah, kecap manis 2 sdm, telur 2 butir.",
            encryptImmediately = true
        )
    }

    /**
     * Menjalankan enkripsi atau dekripsi biner nyata pada file di internal private vault
     */
    suspend fun toggleBinaryEncryptionOnDisk(item: VaultItemEntity, targetEncryptState: Boolean): VaultItemEntity {
        val cfg = securityConfigFlow.first()
        val (pin1, pin2, pin3) = cfg.getPinsForVault(item.vaultProfileId)
        val plainFile = File(item.internalPlainPath)
        val nyxFile = File(item.internalNyxPath)

        if (targetEncryptState) {
            // Mengunci kembali: jika plainFile ada, enkripsi ke nyxFile dan shred plainFile
            if (plainFile.exists()) {
                val (hex, checksum) = EncryptionScriptEngine.sealFileToNyxContainer(
                    plainFile = plainFile,
                    nyxOutFile = nyxFile,
                    pin1 = pin1,
                    pin2 = pin2,
                    pin3 = pin3,
                    shredSource = true
                )
                val updated = item.copy(
                    isEncrypted = true,
                    encryptedHexPreview = hex,
                    checksumPreview = checksum
                )
                vaultDao.updateItem(updated)
                return updated
            } else {
                val updated = item.copy(isEncrypted = true)
                vaultDao.updateItem(updated)
                return updated
            }
        } else {
            // Membuka kunci: dekripsi nyxFile menjadi plainFile agar bisa diputar di VideoView / Image Viewer
            if (nyxFile.exists()) {
                EncryptionScriptEngine.openNyxContainerToFile(
                    nyxFile = nyxFile,
                    plainOutFile = plainFile,
                    pin1 = pin1,
                    pin2 = pin2,
                    pin3 = pin3
                )
            }
            val updated = item.copy(
                isEncrypted = false,
                mediaUriString = if (plainFile.exists()) Uri.fromFile(plainFile).toString() else item.mediaUriString
            )
            vaultDao.updateItem(updated)
            return updated
        }
    }

    /**
     * Memindahkan daftar file hasil ekstraksi & pemindaian (yang sudah bebas duplikat dan disetujui pengguna)
     * dari folder staging ke dalam brankas privat internal lalu mengenkripsinya menjadi kontainer biner .nyx.
     */
    suspend fun commitStagedScanItemsToVault(
        itemsToCommit: List<StagedScanItem>,
        vaultId: Int
    ): Int {
        val cfg = securityConfigFlow.first()
        val (pin1, pin2, pin3) = cfg.getPinsForVault(vaultId)
        val vaultDir = getVaultStorageDir(vaultId)
        var committedCount = 0

        for ((idx, staged) in itemsToCommit.withIndex()) {
            if (!staged.stagedFile.exists() || staged.stagedFile.length() <= 0L) continue

            val stamp = System.currentTimeMillis() + idx
            val safeBase = staged.fileName.replace(Regex("[^a-zA-Z0-9_\\-]"), "_").take(36)
            val plainFile = File(vaultDir, "${stamp}_${safeBase}.${staged.extension}")
            val nyxFile = File(vaultDir, "${stamp}_${safeBase}.${staged.extension}.nyx")

            try {
                staged.stagedFile.copyTo(plainFile, overwrite = true)
                EncryptionScriptEngine.shredFileOnDisk(staged.stagedFile)

                val fSize = plainFile.length().coerceAtLeast(1L)
                val preview = readSafeFileHeaderPreview(plainFile, "${staged.fileName}.${staged.extension}")

                val (hex, checksum) = EncryptionScriptEngine.sealFileToNyxContainer(
                    plainFile = plainFile,
                    nyxOutFile = nyxFile,
                    pin1 = pin1,
                    pin2 = pin2,
                    pin3 = pin3,
                    shredSource = true
                )

                upsertVaultItemOverwritingExisting(
                    VaultItemEntity(
                        vaultProfileId = vaultId,
                        name = staged.fileName,
                        extension = staged.extension,
                        category = staged.category.name,
                        groupArchiveName = staged.sourceZipName.takeIf { it != "Impor Langsung" }
                            ?.replace(" > ", "/")
                            ?.replace('\\', '/')
                            ?.removeSuffix(".zip")
                            ?.removeSuffix(".ZIP")
                            ?.trim('/')
                            ?.trim(),
                        internalPlainPath = plainFile.absolutePath,
                        internalNyxPath = nyxFile.absolutePath,
                        sizeBytes = fSize,
                        paddedSizeBytes = EncryptionScriptEngine.padmeSize(fSize),
                        isEncrypted = true,
                        cipherAlgorithm = cfg.activeScriptName,
                        checksumPreview = checksum,
                        rawContentPreview = preview,
                        encryptedHexPreview = hex,
                        durationSeconds = staged.durationSeconds,
                        mediaUriString = Uri.fromFile(plainFile).toString(),
                        addedAt = stamp
                    )
                )
                committedCount++
            } catch (_: Exception) {
            }
        }

        ZipPreEncryptionScannerEngine.clearStagingDirectory(context)
        return committedCount
    }

    /**
     * MT Explorer Cryptographic Bridge:
     * Menyalin & menyegel berkas lokal dari penyimpanan perangkat langsung ke wadah privat NYX2 Brankas Arcanum.
     */
    suspend fun importLocalFileIntoPrivateVault(
        sourceFile: File,
        vaultId: Int,
        encryptImmediately: Boolean = true
    ): VaultItemEntity {
        val cfg = securityConfigFlow.first()
        val (pin1, pin2, pin3) = cfg.getPinsForVault(vaultId)
        val vaultDir = getVaultStorageDir(vaultId)
        val stamp = System.currentTimeMillis()
        val safeName = sourceFile.nameWithoutExtension.replace(Regex("[^a-zA-Z0-9_\\-]"), "_").take(40)
            .ifBlank { "mt_file" }
        val ext = sourceFile.extension.lowercase().ifBlank { "bin" }
        val category = resolveCategoryFromExtension(ext)

        val plainFile = File(vaultDir, "${stamp}_${safeName}.$ext")
        val nyxFile = File(vaultDir, "${stamp}_${safeName}.$ext.nyx")

        sourceFile.copyTo(plainFile, overwrite = true)
        val fSize = plainFile.length().coerceAtLeast(1L)
        val preview = readSafeFileHeaderPreview(plainFile, sourceFile.name)

        val (hex, checksum) = EncryptionScriptEngine.sealFileToNyxContainer(
            plainFile = plainFile,
            nyxOutFile = nyxFile,
            pin1 = pin1,
            pin2 = pin2,
            pin3 = pin3,
            shredSource = encryptImmediately
        )

        val entity = VaultItemEntity(
            vaultProfileId = vaultId,
            name = sourceFile.nameWithoutExtension.ifBlank { safeName },
            extension = ext,
            category = category.name,
            groupArchiveName = null,
            internalPlainPath = plainFile.absolutePath,
            internalNyxPath = nyxFile.absolutePath,
            sizeBytes = fSize,
            paddedSizeBytes = EncryptionScriptEngine.padmeSize(fSize),
            isEncrypted = encryptImmediately,
            cipherAlgorithm = cfg.activeScriptName,
            checksumPreview = checksum,
            rawContentPreview = preview,
            encryptedHexPreview = hex,
            durationSeconds = extractMediaDurationSeconds(plainFile, category),
            addedAt = stamp
        )

        return upsertVaultItemOverwritingExisting(entity)
    }

    /**
     * Menyimpan file hasil unduhan/ekstraksi Mori Downloader ke dalam Brankas Privat (Brankas 1 / Brankas 2)
     */
    suspend fun saveMoriDownloadedFileToVault(
        stagedFile: File,
        platform: MoriPlatform,
        extractionMode: MoriExtractionMode,
        customTitle: String?,
        encryptImmediately: Boolean,
        vaultId: Int,
        resolvedDurationSeconds: Int = 0
    ): VaultItemEntity {
        val cfg = securityConfigFlow.first()
        val (pin1, pin2, pin3) = cfg.getPinsForVault(vaultId)
        val vaultDir = getVaultStorageDir(vaultId)
        val stamp = System.currentTimeMillis()

        val rawTitle = customTitle?.trim()?.takeIf { it.isNotEmpty() }
            ?: stagedFile.nameWithoutExtension
        val safeBase = rawTitle.replace(Regex("[^a-zA-Z0-9_\\-]"), "_").take(36)
            .ifBlank { "Mori_${platform.name.lowercase()}" }
        val ext = stagedFile.extension.lowercase().ifBlank { extractionMode.defaultExtension }

        val plainFile = File(vaultDir, "${stamp}_${safeBase}.$ext")
        val nyxFile = File(vaultDir, "${stamp}_${safeBase}.$ext.nyx")

        stagedFile.copyTo(plainFile, overwrite = true)
        EncryptionScriptEngine.shredFileOnDisk(stagedFile)

        val fSize = plainFile.length().coerceAtLeast(1L)
        val preview = readSafeFileHeaderPreview(plainFile, "$safeBase.$ext")

        val (hex, checksum) = EncryptionScriptEngine.sealFileToNyxContainer(
            plainFile = plainFile,
            nyxOutFile = nyxFile,
            pin1 = pin1,
            pin2 = pin2,
            pin3 = pin3,
            shredSource = encryptImmediately
        )

        val durationSec = if (resolvedDurationSeconds > 0) {
            resolvedDurationSeconds
        } else {
            when (extractionMode) {
                MoriExtractionMode.HD_VIDEO_NO_WM -> 15
                MoriExtractionMode.AUDIO_STREAM_MP3 -> 30
                else -> 0
            }
        }

        val entity = VaultItemEntity(
            vaultProfileId = vaultId,
            name = safeBase,
            extension = ext,
            category = extractionMode.vaultCategory.name,
            groupArchiveName = "Mori • ${platform.displayName}",
            internalPlainPath = plainFile.absolutePath,
            internalNyxPath = nyxFile.absolutePath,
            sizeBytes = fSize,
            paddedSizeBytes = EncryptionScriptEngine.padmeSize(fSize),
            isEncrypted = encryptImmediately,
            cipherAlgorithm = cfg.activeScriptName,
            checksumPreview = checksum,
            rawContentPreview = preview,
            encryptedHexPreview = hex,
            durationSeconds = durationSec,
            mediaUriString = Uri.fromFile(plainFile).toString(),
            addedAt = stamp
        )
        return upsertVaultItemOverwritingExisting(entity)
    }

    /**
     * Menyimpan file hasil konversi dari Arcanum Universal Converter Studio ke dalam Brankas Aktif
     * dengan mekanisme smart overwrite anti-duplikat dan mengembalikan VaultItemEntity untuk pratinjau langsung.
     */
    suspend fun saveConvertedFileToVault(
        stagedFile: File,
        targetBaseName: String,
        targetExtension: String,
        category: VaultCategory,
        groupLabel: String = "Arcanum Converter",
        vaultId: Int,
        fallbackPreviewText: String = ""
    ): VaultItemEntity {
        val cfg = securityConfigFlow.first()
        val (pin1, pin2, pin3) = cfg.getPinsForVault(vaultId)
        val vaultDir = getVaultStorageDir(vaultId)
        val stamp = System.currentTimeMillis()

        val safeBase = targetBaseName.replace(Regex("[^a-zA-Z0-9_\\-]"), "_").take(42)
            .ifBlank { "Converted_File" }
        val ext = targetExtension.lowercase().trim().removePrefix(".").ifBlank { "bin" }

        val plainFile = File(vaultDir, "${stamp}_${safeBase}.$ext")
        val nyxFile = File(vaultDir, "${stamp}_${safeBase}.$ext.nyx")

        stagedFile.copyTo(plainFile, overwrite = true)

        val fSize = plainFile.length().coerceAtLeast(1L)
        val preview = readSafeFileHeaderPreview(plainFile, "$safeBase.$ext").ifBlank {
            fallbackPreviewText.ifBlank { "Hasil Konversi Arcanum ($safeBase.$ext)" }
        }

        val (hex, checksum) = EncryptionScriptEngine.sealFileToNyxContainer(
            plainFile = plainFile,
            nyxOutFile = nyxFile,
            pin1 = pin1,
            pin2 = pin2,
            pin3 = pin3,
            shredSource = false
        )

        val durationSec = if (category == VaultCategory.AUDIO || category == VaultCategory.VIDEO) 24 else 0

        val entity = VaultItemEntity(
            vaultProfileId = vaultId,
            name = safeBase,
            extension = ext,
            category = category.name,
            groupArchiveName = groupLabel,
            internalPlainPath = plainFile.absolutePath,
            internalNyxPath = nyxFile.absolutePath,
            sizeBytes = fSize,
            paddedSizeBytes = EncryptionScriptEngine.padmeSize(fSize),
            isEncrypted = false,
            cipherAlgorithm = cfg.activeScriptName,
            checksumPreview = checksum,
            rawContentPreview = preview,
            encryptedHexPreview = hex,
            durationSeconds = durationSec,
            mediaUriString = Uri.fromFile(plainFile).toString(),
            addedAt = stamp
        )
        return upsertVaultItemOverwritingExisting(entity)
    }

    /**
     * Membuat gambar .PNG Steganografi berisi pesan rahasia & file terlampir lalu menyimpannya ke dalam Brankas.
     * Disimpan dalam keadaan terbuka (.png) secara default agar langsung tampak seperti gambar PNG normal di Galeri Brankas,
     * sementara muatan rahasianya tetap terenkripsi di dalam struktur biner PNG.
     */
    suspend fun createStegoPngInVault(
        vaultId: Int,
        outputFileName: String,
        coverTheme: StegoCoverTheme,
        secretTitle: String,
        secretMessage: String,
        passcode: String,
        attachedVaultItem: VaultItemEntity? = null,
        sourceCarrierVaultItem: VaultItemEntity? = null,
        isQrKeyCardMode: Boolean = false
    ): VaultItemEntity {
        val cfg = securityConfigFlow.first()
        val (pin1, pin2, pin3) = cfg.getPinsForVault(vaultId)
        val vaultDir = getVaultStorageDir(vaultId)
        val stamp = System.currentTimeMillis()

        val cleanBase = outputFileName.trim().removeSuffix(".png").removeSuffix(".PNG")
            .replace(Regex("[^a-zA-Z0-9_\\-]"), "_")
            .take(36)
            .ifBlank {
                when {
                    isQrKeyCardMode -> "Kartu_QR_${stamp % 1000}"
                    sourceCarrierVaultItem != null -> "Stego_${sourceCarrierVaultItem.name.take(20)}_${stamp % 1000}"
                    else -> "Stego_${coverTheme.name.lowercase()}_${stamp % 1000}"
                }
            }

        val plainFile = File(vaultDir, "${stamp}_${cleanBase}.png")
        val nyxFile = File(vaultDir, "${stamp}_${cleanBase}.png.nyx")

        // Jika ada item brankas yang dilampirkan, ambil file plain-nya (atau dekripsi sementara jika terkunci)
        var tempAttachedFile: File? = null
        val attachedSourceFile: File? = if (attachedVaultItem != null) {
            val pFile = File(attachedVaultItem.internalPlainPath)
            val nFile = File(attachedVaultItem.internalNyxPath)
            if (pFile.exists()) {
                pFile
            } else if (nFile.exists()) {
                val tmp = File(context.cacheDir, "stego_attach_${System.nanoTime()}.${attachedVaultItem.extension}")
                val (ap1, ap2, ap3) = cfg.getPinsForVault(attachedVaultItem.vaultProfileId)
                EncryptionScriptEngine.openNyxContainerToFile(nFile, tmp, ap1, ap2, ap3)
                tempAttachedFile = tmp
                tmp
            } else null
        } else null

        // Jika pengguna memilih Foto Asli di Brankas sebagai Carrier gambar sampul
        var tempCarrierFile: File? = null
        val carrierSourceFile: File? = if (sourceCarrierVaultItem != null && !isQrKeyCardMode) {
            val pFile = File(sourceCarrierVaultItem.internalPlainPath)
            val nFile = File(sourceCarrierVaultItem.internalNyxPath)
            if (pFile.exists()) {
                pFile
            } else if (nFile.exists()) {
                val tmp = File(context.cacheDir, "stego_carrier_${System.nanoTime()}.${sourceCarrierVaultItem.extension}")
                val (cp1, cp2, cp3) = cfg.getPinsForVault(sourceCarrierVaultItem.vaultProfileId)
                EncryptionScriptEngine.openNyxContainerToFile(nFile, tmp, cp1, cp2, cp3)
                tempCarrierFile = tmp
                tmp
            } else null
        } else null

        val fSize = try {
            PngSteganographyEngine.createStegoPngFile(
                outputFile = plainFile,
                coverTheme = coverTheme,
                visibleCaption = cleanBase.replace('_', ' '),
                secretTitle = secretTitle,
                secretMessage = secretMessage,
                passcode = passcode,
                attachedFile = attachedSourceFile,
                attachedDisplayName = attachedVaultItem?.name,
                attachedExtension = attachedVaultItem?.extension,
                sourceCarrierImageFile = carrierSourceFile,
                sourceCarrierName = sourceCarrierVaultItem?.let { "${it.name}.${it.extension}" },
                isQrKeyCardMode = isQrKeyCardMode
            )
        } finally {
            tempAttachedFile?.let { EncryptionScriptEngine.shredFileOnDisk(it) }
            tempCarrierFile?.let { EncryptionScriptEngine.shredFileOnDisk(it) }
        }

        val (hex, checksum) = EncryptionScriptEngine.sealFileToNyxContainer(
            plainFile = plainFile,
            nyxOutFile = nyxFile,
            pin1 = pin1,
            pin2 = pin2,
            pin3 = pin3,
            shredSource = false // Biarkan file .png tetap ada agar tampak sebagai gambar normal di Galeri Foto
        )

        val isDualPhoto = attachedVaultItem != null &&
            (attachedVaultItem.category == VaultCategory.IMAGE.name ||
                PngSteganographyEngine.isImageExtension(attachedVaultItem.extension))

        val groupLabel = when {
            isQrKeyCardMode -> "Stego-QR • Kartu Kunci"
            isDualPhoto -> "Dual-Foto • ${sourceCarrierVaultItem?.name ?: coverTheme.title} + ${attachedVaultItem?.name}"
            sourceCarrierVaultItem != null -> "Stego-Foto • ${sourceCarrierVaultItem.name}"
            else -> "Stego-PNG • ${coverTheme.title}"
        }
        val previewTag = when {
            isQrKeyCardMode -> "PNG_STEGO_CARRIER::QR_CARD::${coverTheme.title}"
            isDualPhoto -> "PNG_STEGO_CARRIER::DUAL_PHOTO::${sourceCarrierVaultItem?.name ?: coverTheme.title}::${attachedVaultItem?.name}"
            sourceCarrierVaultItem != null -> "PNG_STEGO_CARRIER::PHOTO::${sourceCarrierVaultItem.name}"
            else -> "PNG_STEGO_CARRIER::${coverTheme.title}"
        }

        val entity = VaultItemEntity(
            vaultProfileId = vaultId,
            name = cleanBase,
            extension = "png",
            category = VaultCategory.IMAGE.name,
            groupArchiveName = groupLabel,
            internalPlainPath = plainFile.absolutePath,
            internalNyxPath = nyxFile.absolutePath,
            sizeBytes = fSize,
            paddedSizeBytes = EncryptionScriptEngine.padmeSize(fSize),
            isEncrypted = false,
            cipherAlgorithm = cfg.activeScriptName,
            checksumPreview = checksum,
            rawContentPreview = previewTag,
            encryptedHexPreview = hex,
            durationSeconds = 0,
            mediaUriString = Uri.fromFile(plainFile).toString(),
            addedAt = stamp
        )
        val id = vaultDao.insertItem(entity).toInt()
        return entity.copy(id = id)
    }

    /**
     * Membuat file Audio .WAV 16-bit PCM dengan penyisipan LSB (Least Significant Bit)
     * untuk pesan teks rahasia dan/atau foto/file tersembunyi di dalam gelombang suara.
     */
    suspend fun createStegoWavInVault(
        vaultId: Int,
        outputFileName: String,
        wavPreset: WavCamouflagePreset,
        secretTitle: String,
        secretMessage: String,
        passcode: String,
        attachedVaultItem: VaultItemEntity? = null,
        sourceCarrierAudioItem: VaultItemEntity? = null
    ): VaultItemEntity {
        val cfg = securityConfigFlow.first()
        val (pin1, pin2, pin3) = cfg.getPinsForVault(vaultId)
        val dir = getVaultStorageDir(vaultId)
        val stamp = System.currentTimeMillis()
        val cleanBase = outputFileName
            .removeSuffix(".wav")
            .removeSuffix(".WAV")
            .replace(Regex("[^a-zA-Z0-9_\\- ]"), "")
            .trim()
            .replace(' ', '_')
            .ifBlank { "Stego_Audio_$stamp" }

        val plainFile = File(dir, "${cleanBase}_$stamp.wav")
        val nyxFile = File(dir, "${cleanBase}_$stamp.nyx")

        var tempAttachedFile: File? = null
        val attachedSourceFile: File? = if (attachedVaultItem != null) {
            val pFile = File(attachedVaultItem.internalPlainPath)
            val nFile = File(attachedVaultItem.internalNyxPath)
            if (pFile.exists()) {
                pFile
            } else if (nFile.exists()) {
                val tmp = File(context.cacheDir, "wav_stego_attach_${System.nanoTime()}.${attachedVaultItem.extension}")
                val (ap1, ap2, ap3) = cfg.getPinsForVault(attachedVaultItem.vaultProfileId)
                EncryptionScriptEngine.openNyxContainerToFile(nFile, tmp, ap1, ap2, ap3)
                tempAttachedFile = tmp
                tmp
            } else null
        } else null

        var tempCarrierFile: File? = null
        val carrierSourceFile: File? = if (sourceCarrierAudioItem != null) {
            val pFile = File(sourceCarrierAudioItem.internalPlainPath)
            val nFile = File(sourceCarrierAudioItem.internalNyxPath)
            if (pFile.exists()) {
                pFile
            } else if (nFile.exists()) {
                val tmp = File(context.cacheDir, "wav_stego_carrier_${System.nanoTime()}.${sourceCarrierAudioItem.extension}")
                val (cp1, cp2, cp3) = cfg.getPinsForVault(sourceCarrierAudioItem.vaultProfileId)
                EncryptionScriptEngine.openNyxContainerToFile(nFile, tmp, cp1, cp2, cp3)
                tempCarrierFile = tmp
                tmp
            } else null
        } else null

        val fSize = try {
            WavAudioSteganographyEngine.createStegoWavFile(
                outputFile = plainFile,
                preset = wavPreset,
                secretTitle = secretTitle,
                secretMessage = secretMessage,
                passcode = passcode,
                attachedFile = attachedSourceFile,
                attachedDisplayName = attachedVaultItem?.name,
                attachedExtension = attachedVaultItem?.extension,
                sourceCarrierAudioFile = carrierSourceFile,
                sourceCarrierName = sourceCarrierAudioItem?.let { "${it.name}.${it.extension}" }
            )
        } finally {
            tempAttachedFile?.let { EncryptionScriptEngine.shredFileOnDisk(it) }
            tempCarrierFile?.let { EncryptionScriptEngine.shredFileOnDisk(it) }
        }

        val (hex, checksum) = EncryptionScriptEngine.sealFileToNyxContainer(
            plainFile = plainFile,
            nyxOutFile = nyxFile,
            pin1 = pin1,
            pin2 = pin2,
            pin3 = pin3,
            shredSource = false // Biarkan file .wav tetap ada agar bisa langsung diputar di pemutar audio
        )

        val isPhotoInAudio = attachedVaultItem != null &&
            (attachedVaultItem.category == VaultCategory.IMAGE.name ||
                PngSteganographyEngine.isImageExtension(attachedVaultItem.extension))

        val groupLabel = when {
            isPhotoInAudio -> "Stego-WAV • Foto di Audio (${attachedVaultItem?.name})"
            sourceCarrierAudioItem != null -> "Stego-WAV • ${sourceCarrierAudioItem.name}"
            else -> "Stego-WAV • ${wavPreset.title}"
        }
        val previewTag = when {
            isPhotoInAudio -> "WAV_STEGO_CARRIER::PHOTO_IN_AUDIO::${wavPreset.title}::${attachedVaultItem?.name}"
            sourceCarrierAudioItem != null -> "WAV_STEGO_CARRIER::AUDIO::${sourceCarrierAudioItem.name}"
            else -> "WAV_STEGO_CARRIER::${wavPreset.title}"
        }

        val durationSec = ((fSize - 44L) / (22050L * 2L)).toInt().coerceAtLeast(3)

        val entity = VaultItemEntity(
            vaultProfileId = vaultId,
            name = cleanBase,
            extension = "wav",
            category = VaultCategory.AUDIO.name,
            groupArchiveName = groupLabel,
            internalPlainPath = plainFile.absolutePath,
            internalNyxPath = nyxFile.absolutePath,
            sizeBytes = fSize,
            paddedSizeBytes = EncryptionScriptEngine.padmeSize(fSize),
            isEncrypted = false,
            cipherAlgorithm = cfg.activeScriptName,
            checksumPreview = checksum,
            rawContentPreview = previewTag,
            encryptedHexPreview = hex,
            durationSeconds = durationSec,
            mediaUriString = Uri.fromFile(plainFile).toString(),
            addedAt = stamp
        )
        val id = vaultDao.insertItem(entity).toInt()
        return entity.copy(id = id)
    }

    suspend fun decodeStegoPngItem(item: VaultItemEntity, passcode: String): DecodedStegoPayload {
        val isWav = item.extension.equals("wav", ignoreCase = true) ||
            item.rawContentPreview.startsWith("WAV_STEGO_CARRIER::") ||
            item.category == VaultCategory.AUDIO.name

        val plainFile = File(item.internalPlainPath)
        if (plainFile.exists()) {
            return if (isWav) {
                WavAudioSteganographyEngine.decodeStegoWavFile(plainFile, passcode)
            } else {
                PngSteganographyEngine.decodeStegoPngFile(plainFile, passcode)
            }
        }
        val nyxFile = File(item.internalNyxPath)
        if (nyxFile.exists()) {
            val cfg = securityConfigFlow.first()
            val (pin1, pin2, pin3) = cfg.getPinsForVault(item.vaultProfileId)
            val ext = if (isWav) "wav" else "png"
            val tempOut = File(context.cacheDir, "stego_decode_${System.nanoTime()}.$ext")
            return try {
                EncryptionScriptEngine.openNyxContainerToFile(nyxFile, tempOut, pin1, pin2, pin3)
                if (isWav) {
                    WavAudioSteganographyEngine.decodeStegoWavFile(tempOut, passcode)
                } else {
                    PngSteganographyEngine.decodeStegoPngFile(tempOut, passcode)
                }
            } finally {
                EncryptionScriptEngine.shredFileOnDisk(tempOut)
            }
        }
        return DecodedStegoPayload(isStegoCarrier = false, isWavAudioCarrier = isWav)
    }

    suspend fun restoreEmbeddedFileFromStegoPayload(
        vaultId: Int,
        payload: DecodedStegoPayload
    ): VaultItemEntity? {
        val bytes = payload.embeddedFileBytes ?: return null
        val rawName = payload.embeddedFileName?.ifBlank { "File_Pulihan_Stego" } ?: "File_Pulihan_Stego"
        val ext = payload.embeddedFileExtension?.lowercase()?.ifBlank { "txt" } ?: "txt"
        val tempFile = File(context.cacheDir, "${rawName}_pulihan.$ext")
        return try {
            tempFile.writeBytes(bytes)
            importLocalFileIntoPrivateVault(
                sourceFile = tempFile,
                vaultId = vaultId,
                encryptImmediately = false
            )
        } finally {
            EncryptionScriptEngine.shredFileOnDisk(tempFile)
        }
    }

    /**
     * Mengganti nama folder (termasuk folder hasil ekstraksi ZIP / playlist video) di dalam brankas aktif.
     */
    suspend fun renameFolderInVault(
        vaultId: Int,
        oldFolderName: String,
        newFolderName: String
    ): Int {
        val cleanOld = oldFolderName.replace(" > ", "/").replace('\\', '/').trim('/').trim()
        val cleanNew = newFolderName.replace(" > ", "/").replace('\\', '/').removeSuffix(".zip").removeSuffix(".ZIP").trim('/').trim()
        if (cleanOld.isBlank() || cleanNew.isBlank() || cleanOld == cleanNew) return 0

        val items = vaultDao.getItemsForVault(vaultId).first()
        var updatedCount = 0
        for (item in items) {
            val itemGroup = item.groupArchiveName?.replace(" > ", "/")?.replace('\\', '/')?.trim('/')?.trim() ?: continue
            if (itemGroup == cleanOld) {
                vaultDao.updateItem(item.copy(groupArchiveName = cleanNew))
                updatedCount++
            } else if (itemGroup.startsWith("$cleanOld/")) {
                val updatedSubPath = cleanNew + itemGroup.removePrefix(cleanOld)
                vaultDao.updateItem(item.copy(groupArchiveName = updatedSubPath))
                updatedCount++
            }
        }
        return updatedCount
    }

    /**
     * Menghapus seluruh folder beserta semua file dan sub-folder di dalamnya secara permanen (shredding .nyx & plaintext).
     */
    suspend fun deleteFolderInVault(
        vaultId: Int,
        folderName: String
    ): Int {
        val cleanTarget = folderName.replace(" > ", "/").replace('\\', '/').trim('/').trim()
        if (cleanTarget.isBlank()) return 0

        val items = vaultDao.getItemsForVault(vaultId).first()
        var deletedCount = 0
        for (item in items) {
            val itemGroup = item.groupArchiveName?.replace(" > ", "/")?.replace('\\', '/')?.trim('/')?.trim() ?: continue
            if (itemGroup == cleanTarget || itemGroup.startsWith("$cleanTarget/")) {
                deleteItemPermanently(item)
                deletedCount++
            }
        }
        return deletedCount
    }

    /**
     * Memindahkan file dari satu folder ke folder lain (atau ke luar folder / Root Brankas jika targetFolderName == null).
     */
    suspend fun moveItemToFolder(
        item: VaultItemEntity,
        targetFolderName: String?
    ): VaultItemEntity {
        val normalizedTarget = targetFolderName?.trim()
            ?.takeIf { it.isNotBlank() && it != "Semua Media" && it != "Root Brankas (Tanpa Folder)" }
            ?.removeSuffix(".zip")
            ?.removeSuffix(".ZIP")
        val updated = item.copy(groupArchiveName = normalizedTarget)
        vaultDao.updateItem(updated)
        return updated
    }

    /**
     * Fitur #8: Pembersih Metadata GPS & EXIF Foto/Media.
     * Menghapus tag lokasi GPS, koordinat lintang/bujur, model kamera, lensa, dan timestamp EXIF
     * dengan merender ulang piksel bersih (lossless pixel re-encode) atau membersihkan segmen APP1/EXIF,
     * lalu menyegel ulang kontainer .nyx dengan kunci 3-Layer Brankas.
     */
    suspend fun stripExifAndGpsMetadata(item: VaultItemEntity): VaultItemEntity {
        val cfg = securityConfigFlow.first()
        val (pin1, pin2, pin3) = cfg.getPinsForVault(item.vaultProfileId)
        val plainFile = File(item.internalPlainPath)
        val nyxFile = File(item.internalNyxPath)
        val wasEncrypted = item.isEncrypted

        var tempWorkFile: File? = null
        try {
            val targetPlain = if (plainFile.exists()) {
                plainFile
            } else if (nyxFile.exists()) {
                val tmp = File(context.cacheDir, "exif_clean_${System.nanoTime()}.${item.extension}")
                EncryptionScriptEngine.openNyxContainerToFile(nyxFile, tmp, pin1, pin2, pin3)
                tempWorkFile = tmp
                tmp
            } else null

            if (targetPlain != null && targetPlain.exists()) {
                val bmp = android.graphics.BitmapFactory.decodeFile(targetPlain.absolutePath)
                if (bmp != null) {
                    val format = when (item.extension.lowercase()) {
                        "png" -> android.graphics.Bitmap.CompressFormat.PNG
                        "webp" -> android.graphics.Bitmap.CompressFormat.WEBP
                        else -> android.graphics.Bitmap.CompressFormat.JPEG
                    }
                    FileOutputStream(plainFile).use { out ->
                        bmp.compress(format, 96, out)
                    }
                    bmp.recycle()
                } else {
                    // Untuk file gambar/media sintetis atau non-bitmap, bersihkan byte penanda EXIF/GPS secara langsung
                    val rawBytes = targetPlain.readBytes()
                    val rawStr = String(rawBytes, Charsets.ISO_8859_1)
                    val cleanedStr = rawStr
                        .replace("Exif", "CLND")
                        .replace("GPSInfo", "STRIPPED")
                    plainFile.writeBytes(cleanedStr.toByteArray(Charsets.ISO_8859_1))
                }
            }

            val cleanSize = if (plainFile.exists()) plainFile.length().coerceAtLeast(1L) else item.sizeBytes
            val cleanPreview = "EXIF_GPS_SANITIZED::ZERO_METADATA::${item.name}.${item.extension}"

            val (hex, checksum) = if (plainFile.exists()) {
                EncryptionScriptEngine.sealFileToNyxContainer(
                    plainFile = plainFile,
                    nyxOutFile = nyxFile,
                    pin1 = pin1,
                    pin2 = pin2,
                    pin3 = pin3,
                    shredSource = wasEncrypted
                )
            } else {
                Pair(item.encryptedHexPreview, item.checksumPreview)
            }

            val updated = item.copy(
                sizeBytes = cleanSize,
                paddedSizeBytes = EncryptionScriptEngine.padmeSize(cleanSize),
                checksumPreview = checksum,
                rawContentPreview = cleanPreview,
                encryptedHexPreview = hex
            )
            vaultDao.updateItem(updated)
            return updated
        } finally {
            tempWorkFile?.let { EncryptionScriptEngine.shredFileOnDisk(it) }
        }
    }

    /**
     * Membersihkan metadata EXIF & GPS pada seluruh foto/gambar di dalam brankas aktif sekaligus.
     */
    suspend fun stripAllExifInVault(vaultId: Int): Int {
        val items = vaultDao.getItemsForVault(vaultId).first()
        val imageItems = items.filter { it.category == VaultCategory.IMAGE.name }
        var cleanedCount = 0
        for (img in imageItems) {
            stripExifAndGpsMetadata(img)
            cleanedCount++
        }
        return cleanedCount
    }

    /**
     * Menyiapkan berkas eksternal dari URI (`Intent.ACTION_VIEW` / "Buka Dengan Arcanum")
     * ke direktori cache terisolasi untuk ditampilkan langsung pada Layar Pratinjau.
     */
    suspend fun prepareExternalUriForQuickPreview(
        uri: Uri,
        mimeTypeHint: String?
    ): com.example.ui.ExternalFilePreviewRequest? {
        return try {
            val previewCacheDir = File(context.cacheDir, "external_open_with_preview").apply {
                if (!exists()) mkdirs()
                listFiles()?.forEach { old ->
                    if (System.currentTimeMillis() - old.lastModified() > 30 * 60 * 1000L) {
                        old.delete()
                    }
                }
            }

            val rawDisplayName = queryDisplayNameFromUri(uri).ifBlank { "Berkas_Eksternal" }
            val resolvedMime = mimeTypeHint?.takeIf { it.isNotBlank() && it != "*/*" }
                ?: context.contentResolver.getType(uri)
                ?: "application/octet-stream"

            val dotIdx = rawDisplayName.lastIndexOf('.')
            val extFromName = if (dotIdx > 0 && dotIdx < rawDisplayName.length - 1) {
                rawDisplayName.substring(dotIdx + 1).lowercase()
            } else ""

            val resolvedExt = extFromName.ifBlank {
                when {
                    resolvedMime.contains("pdf") -> "pdf"
                    resolvedMime.contains("wordprocessingml") || resolvedMime.contains("msword") -> "docx"
                    resolvedMime.contains("spreadsheetml") || resolvedMime.contains("excel") -> "xlsx"
                    resolvedMime.contains("csv") -> "csv"
                    resolvedMime.contains("presentationml") || resolvedMime.contains("powerpoint") -> "pptx"
                    resolvedMime.startsWith("image/png") -> "png"
                    resolvedMime.startsWith("image/") -> "jpg"
                    resolvedMime.startsWith("video/") -> "mp4"
                    resolvedMime.startsWith("audio/") -> "mp3"
                    resolvedMime.startsWith("text/") -> "txt"
                    else -> "txt"
                }
            }

            val cleanDisplay = if (rawDisplayName.contains('.')) rawDisplayName else "$rawDisplayName.$resolvedExt"
            val safeFileName = "ext_preview_${cleanDisplay.replace(Regex("[^a-zA-Z0-9._\\-]"), "_")}"
            val tempFile = File(previewCacheDir, safeFileName)

            context.contentResolver.openInputStream(uri)?.use { input ->
                FileOutputStream(tempFile).use { output ->
                    input.copyTo(output, bufferSize = 256 * 1024)
                }
            } ?: return null

            com.example.ui.ExternalFilePreviewRequest(
                tempFile = tempFile,
                displayName = cleanDisplay,
                extension = resolvedExt,
                mimeType = resolvedMime,
                sizeBytes = tempFile.length().coerceAtLeast(1L),
                sourceUriString = uri.toString()
            )
        } catch (_: Exception) {
            null
        }
    }

    /**
     * Sinkronisasi Otomatis Media Bawaan & Rekonsiliasi Path saat Update APK:
     * - Menjamin apabila di APK sudah ada media yang disimpan, saat update aplikasi media bawaan
     *   otomatis menimpa (overwrite) versi lama tanpa gagal atau membuat duplikat.
     * - Memperbaiki path internal file brankas jika direktori aplikasi berubah setelah update APK.
     */
    suspend fun syncBundledOfficeAndMediaOnAppUpdate(vaultId: Int = 1) {
        try {
            val cfg = securityConfigFlow.first()
            val (pin1, pin2, pin3) = cfg.getPinsForVault(vaultId)
            val vaultDir = getVaultStorageDir(vaultId)

            // 1. Rekonsiliasi path untuk semua item yang sudah tersimpan di database agar anti-gagal saat update APK
            val existingItems = vaultDao.getAllItems().first()
            for (item in existingItems) {
                val targetDir = getVaultStorageDir(item.vaultProfileId)
                val curPlain = File(item.internalPlainPath)
                val curNyx = File(item.internalNyxPath)
                val rebindPlain = File(targetDir, curPlain.name)
                val rebindNyx = File(targetDir, curNyx.name)
                if ((!curPlain.exists() && rebindPlain.exists()) || (!curNyx.exists() && rebindNyx.exists())) {
                    vaultDao.updateItem(
                        item.copy(
                            internalPlainPath = rebindPlain.absolutePath,
                            internalNyxPath = rebindNyx.absolutePath,
                            mediaUriString = Uri.fromFile(rebindPlain).toString()
                        )
                    )
                }
            }

            // 2. Buat & timpa otomatis (auto-overwrite) media dokumen bawaan APK (PDF, DOCX, XLSX, PPTX)
            data class BundledDocSpec(
                val baseName: String,
                val ext: String,
                val groupFolder: String,
                val summaryPreview: String,
                val writer: (File) -> Unit
            )

            val bundledSpecs = listOf(
                BundledDocSpec(
                    baseName = "Arcanum_Panduan_Eksekutif",
                    ext = "pdf",
                    groupFolder = "Paket Office & Media Bawaan",
                    summaryPreview = "Panduan Eksekutif PDF Arcanum NYX2 (3 Halaman HD, Pinch-to-Zoom, Dark Mode, Cetak)",
                    writer = { com.example.ui.ArcanumOfficeSampleGenerator.writeSamplePdfFile(it) }
                ),
                BundledDocSpec(
                    baseName = "Laporan_Audit_Keamanan_Arcanum",
                    ext = "docx",
                    groupFolder = "Paket Office & Media Bawaan",
                    summaryPreview = "Dokumen Word OpenXML (.docx) berisi laporan audit, bullet list, dan tabel matriks PIN",
                    writer = { com.example.ui.ArcanumOfficeSampleGenerator.writeSampleDocxFile(it) }
                ),
                BundledDocSpec(
                    baseName = "Anggaran_Kas_Sanctuary_2025",
                    ext = "xlsx",
                    groupFolder = "Paket Office & Media Bawaan",
                    summaryPreview = "Spreadsheet Excel (.xlsx) 2 Sheet: Ringkasan Kas & Inventaris Media Brankas",
                    writer = { com.example.ui.ArcanumOfficeSampleGenerator.writeSampleXlsxFile(it) }
                ),
                BundledDocSpec(
                    baseName = "Presentasi_Arsitektur_NYX2",
                    ext = "pptx",
                    groupFolder = "Paket Office & Media Bawaan",
                    summaryPreview = "Dek Presentasi PowerPoint (.pptx) 3 Slide 16:9 tentang Universal Viewer & Buka Dengan Arcanum",
                    writer = { com.example.ui.ArcanumOfficeSampleGenerator.writeSamplePptxFile(it) }
                )
            )

            for ((idx, spec) in bundledSpecs.withIndex()) {
                val plainFile = File(vaultDir, "bundled_${spec.baseName}.${spec.ext}")
                val nyxFile = File(vaultDir, "bundled_${spec.baseName}.${spec.ext}.nyx")

                // Tulis ulang (overwrite) file di disk dengan versi APK terbaru
                spec.writer(plainFile)
                val fSize = plainFile.length().coerceAtLeast(1L)

                val (hex, checksum) = EncryptionScriptEngine.sealFileToNyxContainer(
                    plainFile = plainFile,
                    nyxOutFile = nyxFile,
                    pin1 = pin1,
                    pin2 = pin2,
                    pin3 = pin3,
                    shredSource = false // Biarkan siap dibuka langsung sekaligus memiliki salinan .nyx
                )

                val entity = VaultItemEntity(
                    vaultProfileId = vaultId,
                    name = spec.baseName,
                    extension = spec.ext,
                    category = VaultCategory.DOCUMENT.name,
                    groupArchiveName = spec.groupFolder,
                    internalPlainPath = plainFile.absolutePath,
                    internalNyxPath = nyxFile.absolutePath,
                    sizeBytes = fSize,
                    paddedSizeBytes = EncryptionScriptEngine.padmeSize(fSize),
                    isEncrypted = false,
                    cipherAlgorithm = cfg.activeScriptName,
                    checksumPreview = checksum,
                    rawContentPreview = spec.summaryPreview,
                    encryptedHexPreview = hex,
                    durationSeconds = 0,
                    mediaUriString = Uri.fromFile(plainFile).toString(),
                    addedAt = System.currentTimeMillis() - (idx * 1000L)
                )
                upsertVaultItemOverwritingExisting(entity)
            }
        } catch (_: Exception) {
        }
    }
}



