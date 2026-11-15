package com.example

import com.example.data.MoriDownloaderEngine
import com.example.data.MoriPlatform
import com.example.data.MtExplorerEngine
import com.example.data.MtExplorerScope
import com.example.ui.MascotMood
import com.example.ui.MascotPersona
import com.example.ui.VaultTab
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import java.io.File

class ExampleUnitTest {

    @Test
    fun testVaultTabConfiguration() {
        val tabs = VaultTab.entries
        assertTrue("VaultTab must contain PHOTO_GALLERY", tabs.contains(VaultTab.PHOTO_GALLERY))
        assertTrue("VaultTab must contain MEDIA_PLAYER", tabs.contains(VaultTab.MEDIA_PLAYER))
        assertTrue("VaultTab must contain MORI_DOWNLOADER", tabs.contains(VaultTab.MORI_DOWNLOADER))
        assertTrue("VaultTab must contain DASHBOARD", tabs.contains(VaultTab.DASHBOARD))
        assertTrue("VaultTab must contain SCRIPT_ENGINE", tabs.contains(VaultTab.SCRIPT_ENGINE))
    }

    @Test
    fun testCosmicDragonMascotEmotionsAndPersonas() {
        // Test all 8 dynamic emotional states are available
        val moods = MascotMood.entries
        assertTrue(moods.contains(MascotMood.IDLE_BREATHE))
        assertTrue(moods.contains(MascotMood.TOUCH_REACTION))
        assertTrue(moods.contains(MascotMood.PIN_SHIELD))
        assertTrue(moods.contains(MascotMood.ALERT_FIRE))
        assertTrue(moods.contains(MascotMood.SUCCESS_LEAP))
        assertTrue(moods.contains(MascotMood.DOWNLOAD_HUNTER))
        assertTrue(moods.contains(MascotMood.MT_INSPECTING))
        assertTrue(moods.contains(MascotMood.STEALTH_SHADOW))

        // Backward compatibility
        assertTrue(moods.contains(MascotMood.WAVE_HELLO))
        assertTrue(moods.contains(MascotMood.SHIELD_EYES))

        // Personas (Unified Kawaii File Folder Mascot)
        assertEquals("Folder Guard", MascotPersona.COSMIC_DRAGON.label)
        assertEquals("Folder Companion", MascotPersona.KAWAII_FOLDER.label)
    }

    @Test
    fun testMoriPlatformRecognition() {
        val tiktokPlatform = MoriDownloaderEngine.detectPlatformFromUrl("https://www.tiktok.com/@creator/video/123456789")
        assertEquals(MoriPlatform.TIKTOK, tiktokPlatform)

        val igPlatform = MoriDownloaderEngine.detectPlatformFromUrl("https://www.instagram.com/reel/Cx123456789/")
        assertEquals(MoriPlatform.INSTAGRAM, igPlatform)

        val ytPlatform = MoriDownloaderEngine.detectPlatformFromUrl("https://youtu.be/dQw4w9WgXcQ")
        assertEquals(MoriPlatform.YOUTUBE, ytPlatform)

        val xPlatform = MoriDownloaderEngine.detectPlatformFromUrl("https://x.com/astro/status/987654321")
        assertEquals(MoriPlatform.TWITTER_X, xPlatform)

        val gdrivePlatform = MoriDownloaderEngine.detectPlatformFromUrl("https://drive.google.com/drive/folders/1MoriArcanumPack")
        assertEquals(MoriPlatform.GOOGLE_DRIVE, gdrivePlatform)

        val pixeldrainPlatform = MoriDownloaderEngine.detectPlatformFromUrl("https://pixeldrain.com/l/mori451album")
        assertEquals(MoriPlatform.PIXELDRAIN, pixeldrainPlatform)
    }

    @Test
    fun testBrainPuzzleDashboardAnd27Games() {
        val games = com.example.ui.BrainGameId.entries
        assertTrue("Catalog must contain at least 27 brain & arcade games", games.size >= 27)

        val difficulties = com.example.ui.BrainGameDifficulty.entries
        assertEquals(4, difficulties.size)
        assertEquals("Mudah", com.example.ui.BrainGameDifficulty.MUDAH.label)
        assertEquals("Ekstrem", com.example.ui.BrainGameDifficulty.EKSTREM.label)

        val categories = com.example.ui.BrainGameCategory.entries
        assertTrue(categories.size >= 7)
    }

    @Test
    fun testMoriMultiFileSubFilesAndDestinations() {
        val destinations = com.example.data.MoriTargetDestination.entries
        assertEquals(3, destinations.size)
        assertTrue(destinations.contains(com.example.data.MoriTargetDestination.ACTIVE_VAULT))
        assertTrue(destinations.contains(com.example.data.MoriTargetDestination.PUBLIC_DOWNLOAD_ARCANUM))
        assertTrue(destinations.contains(com.example.data.MoriTargetDestination.BOTH_VAULT_AND_PUBLIC))

        val mfMeta = MoriDownloaderEngine.populateDefaultSubFiles(
            com.example.data.MoriResolvedMetadata(
                cleanUrl = "https://www.mediafire.com/folder/mori451/Arcanum_Pack",
                platform = MoriPlatform.MEDIAFIRE,
                title = "Arcanum_Pack",
                author = "@mori",
                durationSeconds = 20
            )
        )
        assertTrue("MediaFire metadata must populate multiple subFiles for < / > preview navigation", mfMeta.subFiles.size >= 4)
    }

    @Test
    fun testMtExplorerEngineHexDump() {
        val tempFile = File.createTempFile("test_mt_hex", ".bin").apply {
            writeBytes("ARCANUM_COSMIC_DRAGON_BYTE_TEST_12345".toByteArray())
            deleteOnExit()
        }

        val hex = MtExplorerEngine.readHexDump(tempFile, maxBytes = 64)
        assertNotNull(hex)
        assertTrue(hex.contains("00000000:"))
        assertTrue(hex.contains("ARCANUM_COSMIC_D"))
    }

    @Test
    fun testMtExplorerScopes() {
        val scopes = MtExplorerScope.entries
        assertEquals(2, scopes.size)
        assertTrue(scopes.contains(MtExplorerScope.DEVICE_ALL))
        assertTrue(scopes.contains(MtExplorerScope.VAULT_EXCLUSIVE))
    }

    @Test
    fun testSixThemesFreedomAndSanctuaryExclusiveBentoGlowLock() {
        val themes = com.example.ui.theme.AppVisualTheme.entries
        assertEquals("Theme collection must contain 6 enum entries", 6, themes.size)

        // Verify that outside Sanctuary, all 6 themes can be freely selected
        com.example.ui.theme.AppThemeController.isSanctuaryBentoGlowLocked = false
        for (theme in themes) {
            assertEquals(
                theme,
                com.example.ui.theme.AppVisualTheme.fromId(theme.id)
            )
            com.example.ui.theme.AppThemeController.activeTheme = theme
            assertEquals(
                theme,
                com.example.ui.theme.AppThemeController.activeTheme
            )
            assertNotNull(com.example.ui.theme.DuoSnowCanvas)
            assertNotNull(com.example.ui.theme.DuoMintSurface)
            assertNotNull(com.example.ui.theme.DuoSwanBorder)
            assertNotNull(com.example.ui.theme.CosmicGradientGlassCard)
        }

        // Verify that inside BlackBox Sanctuary, activeTheme is exclusively locked to BENTO_GLOW
        com.example.ui.theme.AppThemeController.activeTheme = com.example.ui.theme.AppVisualTheme.NEUMORPHISM
        com.example.ui.theme.AppThemeController.isSanctuaryBentoGlowLocked = true
        assertEquals(
            com.example.ui.theme.AppVisualTheme.BENTO_GLOW,
            com.example.ui.theme.AppThemeController.activeTheme
        )
        assertEquals(
            com.example.ui.theme.AppVisualTheme.NEUMORPHISM,
            com.example.ui.theme.AppThemeController.userSelectedTheme
        )
        com.example.ui.theme.AppThemeController.isSanctuaryBentoGlowLocked = false

        // Verify 10 Sculptural Dynamic Island shapes
        val shapes = com.example.ui.theme.DynamicIslandShapeStyle.entries
        assertEquals(10, shapes.size)
        assertTrue(shapes.contains(com.example.ui.theme.DynamicIslandShapeStyle.CLOUD_PUFF_RIBBON))
        assertTrue(shapes.contains(com.example.ui.theme.DynamicIslandShapeStyle.RETRO_CASSETTE_TAPE))
        assertTrue(shapes.contains(com.example.ui.theme.DynamicIslandShapeStyle.ROYAL_SCROLL_CREST))
        assertTrue(shapes.contains(com.example.ui.theme.DynamicIslandShapeStyle.STEAMPUNK_BRASS_GAUGE))

        // Verify 3 Non-Overlapping Layout Modes
        val layoutModes = com.example.ui.theme.DynamicIslandLayoutMode.entries
        assertEquals(3, layoutModes.size)

        // Verify 12 Dynamic Island color & material presets
        val presets = com.example.ui.theme.DynamicIslandColorPreset.entries
        assertEquals(12, presets.size)
        com.example.ui.theme.AppThemeController.applyDynamicIslandPreset(
            com.example.ui.theme.DynamicIslandColorPreset.PEARL_DAYLIGHT_GLASS
        )
        assertEquals("#F8FAFC", com.example.ui.theme.AppThemeController.dynamicIslandBgHex)
        assertEquals("#0284C7", com.example.ui.theme.AppThemeController.dynamicIslandBorderHex)
        assertEquals("#38BDF8", com.example.ui.theme.AppThemeController.dynamicIslandGlowHex)
        assertTrue(com.example.ui.theme.AppThemeController.isIslandLightSurface)

        // Verify 6 Exclusive Font Presets (Normal Google, Serif, Italian, Bold, Minecraft, The Cavas)
        val fontPresets = com.example.ui.theme.AppFontPreset.entries
        assertEquals(6, fontPresets.size)
        assertTrue(fontPresets.contains(com.example.ui.theme.AppFontPreset.NORMAL_GOOGLE))
        assertTrue(fontPresets.contains(com.example.ui.theme.AppFontPreset.SERIF))
        assertTrue(fontPresets.contains(com.example.ui.theme.AppFontPreset.ITALIAN))
        assertTrue(fontPresets.contains(com.example.ui.theme.AppFontPreset.BOLD))
        assertTrue(fontPresets.contains(com.example.ui.theme.AppFontPreset.MINECRAFT))
        assertTrue(fontPresets.contains(com.example.ui.theme.AppFontPreset.THE_CAVAS))
        for (fp in fontPresets) {
            com.example.ui.theme.AppThemeController.activeFontPreset = fp
            assertEquals(fp, com.example.ui.theme.AppThemeController.activeFontPreset)
        }

        // Verify 120 Interactive Micro-Features across 10 Categories
        val allMicro = com.example.ui.MicroFeaturesRegistry.allFeatures
        assertEquals(120, allMicro.size)
        assertEquals(10, com.example.ui.MicroFeatureCategory.entries.size)
        for (item in allMicro) {
            val out = com.example.ui.MicroFeaturesState.executeMicroFeature(item, item.defaultInput, item.defaultParam, 1)
            assertTrue("Micro-feature ${item.codeBadge} must return non-blank output", out.isNotBlank())
        }
    }

    @Test
    fun testExpandedAnimeHeroFantasyMascotWardrobe() {
        val outfits = com.example.ui.theme.MascotUpperOutfit.entries
        assertTrue("Upper outfits must contain at least 52 entries", outfits.size >= 52)
        assertTrue(outfits.contains(com.example.ui.theme.MascotUpperOutfit.DEMON_SLAYER_CHECKERED_HAORI))
        assertTrue(outfits.contains(com.example.ui.theme.MascotUpperOutfit.FLAME_HASHIRA_CAPE))
        assertTrue(outfits.contains(com.example.ui.theme.MascotUpperOutfit.THUNDER_BREATH_HAORI))
        assertTrue(outfits.contains(com.example.ui.theme.MascotUpperOutfit.AOT_SURVEY_CORPS_CLOAK))
        assertTrue(outfits.contains(com.example.ui.theme.MascotUpperOutfit.BLEACH_BANKAI_SHIHAKUSHO))
        assertTrue(outfits.contains(com.example.ui.theme.MascotUpperOutfit.NARUTO_HOKAGE_SAGE_CLOAK))
        assertTrue(outfits.contains(com.example.ui.theme.MascotUpperOutfit.UCHIHA_SUSANOO_ARMOR))
        assertTrue(outfits.contains(com.example.ui.theme.MascotUpperOutfit.LINK_CLICK_TIME_JACKET))
        assertTrue(outfits.contains(com.example.ui.theme.MascotUpperOutfit.SOUL_LAND_SEA_GOD_ARMOR))
        assertTrue(outfits.contains(com.example.ui.theme.MascotUpperOutfit.BTTH_FLAME_EMPEROR_ROBE))
        assertTrue(outfits.contains(com.example.ui.theme.MascotUpperOutfit.HERO_NANO_IRON_ARMOR))
        assertTrue(outfits.contains(com.example.ui.theme.MascotUpperOutfit.HERO_SPIDER_SYMBIOTE_SUIT))
        assertTrue(outfits.contains(com.example.ui.theme.MascotUpperOutfit.HERO_DARK_KNIGHT_CAPE))
        assertTrue(outfits.contains(com.example.ui.theme.MascotUpperOutfit.HERO_THUNDER_ASGARD_PLATE))
        assertTrue(outfits.contains(com.example.ui.theme.MascotUpperOutfit.FANTASY_ELVEN_RANGER_CLOAK))
        assertTrue(outfits.contains(com.example.ui.theme.MascotUpperOutfit.FANTASY_NECROMANCER_LICH_ROBE))
        assertTrue(outfits.contains(com.example.ui.theme.MascotUpperOutfit.FANTASY_VALKYRIE_DIVINE_DRESS))
        assertTrue(outfits.contains(com.example.ui.theme.MascotUpperOutfit.FANTASY_DRAGONSLAYER_BERSERKER))

        val presets = com.example.ui.theme.MascotCostumePreset.entries
        assertTrue("Costume presets must contain at least 26 entries", presets.size >= 26)
        assertTrue(presets.contains(com.example.ui.theme.MascotCostumePreset.COLLAB_DEMON_SLAYER_TANJIRO))
        assertTrue(presets.contains(com.example.ui.theme.MascotCostumePreset.COLLAB_AOT_SURVEY_CORPS))
        assertTrue(presets.contains(com.example.ui.theme.MascotCostumePreset.COLLAB_BLEACH_BANKAI_CAPTAIN))
        assertTrue(presets.contains(com.example.ui.theme.MascotCostumePreset.COLLAB_NARUTO_HOKAGE_SAGE))
        assertTrue(presets.contains(com.example.ui.theme.MascotCostumePreset.COLLAB_LINK_CLICK_TIME_AGENT))
        assertTrue(presets.contains(com.example.ui.theme.MascotCostumePreset.COLLAB_SOUL_LAND_TANG_SAN))
        assertTrue(presets.contains(com.example.ui.theme.MascotCostumePreset.COLLAB_BTTH_FLAME_EMPEROR))
        assertTrue(presets.contains(com.example.ui.theme.MascotCostumePreset.HERO_IRON_NANO_AVENGER))
        assertTrue(presets.contains(com.example.ui.theme.MascotCostumePreset.HERO_DARK_KNIGHT_GUARDIAN))
        assertTrue(presets.contains(com.example.ui.theme.MascotCostumePreset.FANTASY_ELVEN_FOREST_KING))

        val skins = com.example.ui.theme.MascotBodySkin.entries
        assertTrue("Mascot skins must contain at least 44 entries", skins.size >= 44)

        val gloves = com.example.ui.theme.MascotHandGloves.entries
        assertTrue("Mascot weapons/gloves must contain at least 46 entries", gloves.size >= 46)

        val accessories = com.example.ui.theme.MascotAccessory.entries
        assertTrue("Mascot accessories must contain at least 52 entries", accessories.size >= 52)

        val auras = com.example.ui.theme.MascotAuraStyle.entries
        assertTrue("Mascot auras must contain at least 22 entries", auras.size >= 22)
    }

    @Test
    fun testViralTrendsPhotoboothHoloTimeCapsuleAndWrapped() {
        val foils = com.example.ui.HoloFoilType.entries
        assertEquals(5, foils.size)
        assertTrue(foils.contains(com.example.ui.HoloFoilType.PRISMATIC_RAINBOW))
        assertTrue(foils.contains(com.example.ui.HoloFoilType.GOLD_STARLIGHT))
        assertTrue(foils.contains(com.example.ui.HoloFoilType.CYBER_MATRIX))
        assertTrue(foils.contains(com.example.ui.HoloFoilType.DIAMOND_SHATTER))
        assertTrue(foils.contains(com.example.ui.HoloFoilType.SAKURA_AURORA))

        val frames = com.example.ui.PhotoboothStickerFrame.entries
        assertEquals(6, frames.size)

        val badges = com.example.ui.PhotoboothStickerBadge.entries
        assertEquals(12, badges.size)

        val initialCount = com.example.ui.VaultTimeCapsuleRepository.capsules.size
        assertTrue(initialCount >= 2)
        com.example.ui.VaultTimeCapsuleRepository.addCapsule(
            title = "Unit Test Capsule",
            secretNote = "Secret Future Payload",
            linkedFileName = "Test.nyx",
            durationSeconds = 120L
        )
        assertEquals(initialCount + 1, com.example.ui.VaultTimeCapsuleRepository.capsules.size)
        val formattedZero = com.example.ui.VaultTimeCapsuleRepository.formatCountdown(0L)
        assertEquals("00h : 00j : 00m : 00d", formattedZero)

        com.example.ui.DynamicIslandNotificationCenter.recordNotification(
            title = "Test Alert",
            message = "Unduhan Mori selesai",
            category = com.example.ui.DynamicIslandStatusCategory.DOWNLOAD
        )
        assertTrue(com.example.ui.DynamicIslandNotificationCenter.notifications.isNotEmpty())
        assertEquals(
            "Unduhan Mori selesai",
            com.example.ui.DynamicIslandNotificationCenter.notifications.first().message
        )
    }

    @Test
    fun testIsolatedSanctuaryFiveWingsAndBraveTorBrowserEngine() {
        // 1. Verify all 5 Sanctuary Wings & VaultTab.ISOLATED_SANCTUARY exist
        assertTrue(VaultTab.entries.contains(VaultTab.ISOLATED_SANCTUARY))
        val wings = com.example.ui.SanctuaryWing.entries
        assertEquals(5, wings.size)
        assertTrue(wings.contains(com.example.ui.SanctuaryWing.SHADOW_LEDGER))
        assertTrue(wings.contains(com.example.ui.SanctuaryWing.DETECTIVE_BOARD))
        assertTrue(wings.contains(com.example.ui.SanctuaryWing.CYBER_DECK))
        assertTrue(wings.contains(com.example.ui.SanctuaryWing.MIDNIGHT_ZEN))
        assertTrue(wings.contains(com.example.ui.SanctuaryWing.ONION_SHIELD_BROWSER))

        // 2. Verify 3-Layer Sanctuary PIN config & matching (Default: 444444 - 444444 - 444444) & Fun Zone (333333 - 333333 - 333333)
        val cfg = com.example.data.SecurityConfig()
        assertEquals(Triple("444444", "444444", "444444"), cfg.getSanctuaryPins())
        assertTrue(cfg.isSanctuaryLayer1Match("444444"))
        assertTrue(cfg.isSanctuaryFullMatch("444444", "444444", "444444"))
        assertEquals(Triple("333333", "333333", "333333"), cfg.getFunZonePins())
        assertTrue(cfg.isFunZoneFullMatch("333333", "333333", "333333"))

        // 2b. Verify Sanctuary Tempat Duit Tabungan & Goals Barang yang Mau Dibeli + Candlestick Chart
        val initialMainBalance = com.example.ui.SanctuarySavingsAndGoalsRepository.currentVaultSavingsBalanceIdr
        assertTrue(initialMainBalance > 0L)
        com.example.ui.SanctuarySavingsAndGoalsRepository.recordVaultCashflowTransaction(
            isMoneyIn = true,
            amountIdr = 2_500_000L,
            note = "Setor Tabungan Uji"
        )
        assertEquals(initialMainBalance + 2_500_000L, com.example.ui.SanctuarySavingsAndGoalsRepository.currentVaultSavingsBalanceIdr)
        val initialGoalCount = com.example.ui.SanctuarySavingsAndGoalsRepository.buyGoals.size
        com.example.ui.SanctuarySavingsAndGoalsRepository.addNewBuyGoal(
            itemName = "Kamera Mirrorless",
            itemCategory = "FOTOGRAFI",
            itemSpecsNote = "Full Frame 61MP",
            targetPriceIdr = 15_000_000L,
            initialSavedIdr = 3_000_000L,
            monthlyPlanIdr = 2_000_000L
        )
        assertEquals(initialGoalCount + 1, com.example.ui.SanctuarySavingsAndGoalsRepository.buyGoals.size)
        val addedGoal = com.example.ui.SanctuarySavingsAndGoalsRepository.buyGoals.first { it.itemName == "Kamera Mirrorless" }
        assertTrue(addedGoal.candleHistory.isNotEmpty())
        com.example.ui.SanctuarySavingsAndGoalsRepository.mutateGoalSavings(
            goalId = addedGoal.id,
            isDeposit = true,
            amountIdr = 2_000_000L,
            note = "Cicilan Tabungan Kamera"
        )
        val updatedGoal = com.example.ui.SanctuarySavingsAndGoalsRepository.buyGoals.first { it.id == addedGoal.id }
        assertEquals(5_000_000L, updatedGoal.savedAmountIdr)

        // 3. Wing 1 (Shadow Ledger): Mnemonic 12/24 words & Compound Interest Calculator
        val words12 = com.example.ui.SanctuaryBlackBoxEngine.generateMnemonicWords(12)
        val words24 = com.example.ui.SanctuaryBlackBoxEngine.generateMnemonicWords(24)
        assertEquals(12, words12.size)
        assertEquals(24, words24.size)
        val compoundResult = com.example.ui.SanctuaryBlackBoxEngine.calculateCompoundFutureValue(
            principalIdr = 100_000_000L,
            monthlyContributionIdr = 5_000_000L,
            annualRatePercent = 10.0,
            years = 5
        )
        assertTrue("Compound future value must exceed principal + raw contributions", compoundResult > 400_000_000L)

        // 4. Wing 2 (Detective Board): Red-String nodes & links
        assertTrue(com.example.ui.SanctuaryBlackBoxEngine.detectiveNodes.size >= 4)
        assertTrue(com.example.ui.SanctuaryBlackBoxEngine.detectiveLinks.size >= 4)

        // 5. Wing 3 (Cyber Deck): 6-Cipher reciprocal roundtrip + CLI Terminal + Entropy Password
        for (algo in com.example.ui.CyberCipherAlgorithm.entries) {
            val sample = "SANCTUARY2026"
            val encoded = com.example.ui.SanctuaryBlackBoxEngine.transformCyberCipher(sample, algo, decodeMode = false, vigenereKey = "NYXKEY")
            assertTrue(encoded.isNotBlank())
            val decoded = com.example.ui.SanctuaryBlackBoxEngine.transformCyberCipher(encoded, algo, decodeMode = true, vigenereKey = "NYXKEY")
            assertEquals("Cipher ${algo.name} must round-trip accurately", sample, decoded)
        }
        val (forgedPass, entropyBits) = com.example.ui.SanctuaryBlackBoxEngine.generateHighEntropyPassword(24, true)
        assertEquals(24, forgedPass.length)
        assertTrue(entropyBits > 120.0)
        val scanOut = com.example.ui.SanctuaryBlackBoxEngine.executeTerminalCommand("scan")
        assertTrue(scanOut.any { it.contains("100% AMAN") })

        // 6. Wing 5 (Onion & Shield Browser — Brave Shields + Tor 3-Hop + Extensions + VPN/DoH)
        assertTrue(com.example.ui.SanctuaryBlackBoxEngine.isAdOrTrackerUrl("https://pagead2.googlesyndication.com/pagead/js/adsbygoogle.js"))
        assertTrue(com.example.ui.SanctuaryBlackBoxEngine.isAdOrTrackerUrl("https://connect.facebook.net/en_US/fbevents.js"))
        val upgradedUrl = com.example.ui.SanctuaryBlackBoxEngine.normalizeAndUpgradeUrl(
            rawInput = "http://example.com/article?id=42&utm_source=newsletter&fbclid=IwAR12345",
            httpsOnlyUpgrade = true,
            stripTrackers = true
        )
        assertTrue(upgradedUrl.startsWith("https://example.com/article"))
        assertTrue(upgradedUrl.contains("id=42"))
        assertTrue(!upgradedUrl.contains("utm_source"))
        assertTrue(!upgradedUrl.contains("fbclid"))

        val onionCircuit = com.example.ui.SanctuaryBlackBoxEngine.generateNewOnionCircuit(7)
        assertEquals(3, onionCircuit.size)
        assertTrue(onionCircuit[0].roleLabel.contains("Entry Guard"))
        assertTrue(onionCircuit[1].roleLabel.contains("Middle Relay"))
        assertTrue(onionCircuit[2].roleLabel.contains("Exit Node"))

        val combinedJs = com.example.ui.SanctuaryBlackBoxEngine.buildCombinedExtensionJavaScript()
        assertTrue(combinedJs.contains("doNotTrack"))
        assertTrue(combinedJs.contains("RTCPeerConnection"))

        // 7. Verify Arcanum Browser naming & Dynamic Island minimal-by-default & Pro gating
        assertEquals("Arcanum", com.example.ui.SanctuaryWing.ONION_SHIELD_BROWSER.shortTitle)
        assertEquals("Arcanum Browser", com.example.ui.SanctuaryWing.ONION_SHIELD_BROWSER.fullTitle)
        assertTrue(
            "Dynamic Island must default to most minimal folded pill",
            com.example.ui.theme.AppThemeController.isDynamicIslandFolded
        )

        val defaultRules = listOf(
            com.example.ui.theme.BrowserDomainAliasRule(
                id = "rule_g_start",
                aliasDomain = "g.start",
                targetDomain = "google.com",
                isEnabled = true
            ),
            com.example.ui.theme.BrowserDomainAliasRule(
                id = "rule_custom",
                aliasDomain = "vault.search",
                targetDomain = "duckduckgo.com",
                isEnabled = true
            )
        )
        // Non-Pro users must NOT rewrite custom domain aliases
        val nonProRes = com.example.ui.theme.ArcanumBrowserDomainAliasEngine.resolveForNavigation(
            rawUrl = "https://g.start/search?q=arcanum",
            isProUnlocked = false,
            rules = defaultRules
        )
        assertEquals("https://g.start/search?q=arcanum", nonProRes.actualLoadUrl)
        assertEquals(null, nonProRes.matchedRule)

        // Pro users: opening alias https://g.start/... loads real https://google.com/... while keeping https://g.start/... in address bar
        val proAliasRes = com.example.ui.theme.ArcanumBrowserDomainAliasEngine.resolveForNavigation(
            rawUrl = "https://g.start/search?q=arcanum",
            isProUnlocked = true,
            rules = defaultRules
        )
        assertEquals("https://google.com/search?q=arcanum", proAliasRes.actualLoadUrl)
        assertEquals("https://g.start/search?q=arcanum", proAliasRes.displayAliasUrl)
        assertNotNull(proAliasRes.matchedRule)

        // Pro users: opening target https://www.google.com/maps loads real target while masking display URL to https://g.start/maps
        val proTargetRes = com.example.ui.theme.ArcanumBrowserDomainAliasEngine.resolveForNavigation(
            rawUrl = "https://www.google.com/maps",
            isProUnlocked = true,
            rules = defaultRules
        )
        assertEquals("https://www.google.com/maps", proTargetRes.actualLoadUrl)
        assertEquals("https://g.start/maps", proTargetRes.displayAliasUrl)
        assertNotNull(proTargetRes.matchedRule)

        // Pro users: custom rule also works
        val customRes = com.example.ui.theme.ArcanumBrowserDomainAliasEngine.resolveForNavigation(
            rawUrl = "https://vault.search/?q=privacy",
            isProUnlocked = true,
            rules = defaultRules
        )
        assertEquals("https://duckduckgo.com/?q=privacy", customRes.actualLoadUrl)
        assertEquals("https://vault.search/?q=privacy", customRes.displayAliasUrl)

        // 8. Verify Arcanum Browser Desktop Mode URL rewriting & 1280px PC viewport script
        val ytDesktop = com.example.ui.SanctuaryBlackBoxEngine.rewriteUrlForDesktopMode(
            rawUrl = "https://m.youtube.com/watch?v=dQw4w9WgXcQ",
            desktopMode = true
        )
        assertEquals("https://www.youtube.com/watch?v=dQw4w9WgXcQ&app=desktop", ytDesktop)

        val ytMobileRestored = com.example.ui.SanctuaryBlackBoxEngine.rewriteUrlForDesktopMode(
            rawUrl = ytDesktop,
            desktopMode = false
        )
        assertEquals("https://www.youtube.com/watch?v=dQw4w9WgXcQ", ytMobileRestored)

        val wikiDesktop = com.example.ui.SanctuaryBlackBoxEngine.rewriteUrlForDesktopMode(
            rawUrl = "https://id.m.wikipedia.org/wiki/Indonesia",
            desktopMode = true
        )
        assertEquals("https://id.wikipedia.org/wiki/Indonesia", wikiDesktop)

        val desktopJs = com.example.ui.SanctuaryBlackBoxEngine.buildDesktopViewportJavaScript(desktopMode = true)
        assertTrue(desktopJs.contains("width=1280"))
        assertTrue(desktopJs.contains("Win32"))
    }

    @Test
    fun testUniversalDocumentViewerAndOfficeSampleGenerators() {
        assertEquals(com.example.ui.ArcanumDocFormat.PDF, com.example.ui.ArcanumDocFormat.fromExtension("pdf"))
        assertEquals(com.example.ui.ArcanumDocFormat.WORD, com.example.ui.ArcanumDocFormat.fromExtension("docx"))
        assertEquals(com.example.ui.ArcanumDocFormat.WORD, com.example.ui.ArcanumDocFormat.fromExtension("doc"))
        assertEquals(com.example.ui.ArcanumDocFormat.EXCEL, com.example.ui.ArcanumDocFormat.fromExtension("xlsx"))
        assertEquals(com.example.ui.ArcanumDocFormat.EXCEL, com.example.ui.ArcanumDocFormat.fromExtension("csv"))
        assertEquals(com.example.ui.ArcanumDocFormat.POWERPOINT, com.example.ui.ArcanumDocFormat.fromExtension("pptx"))
        assertEquals(com.example.ui.ArcanumDocFormat.TEXT_CODE, com.example.ui.ArcanumDocFormat.fromExtension("txt"))

        assertEquals("A", com.example.ui.ArcanumOfficeDocumentParser.excelColumnLabel(0))
        assertEquals("Z", com.example.ui.ArcanumOfficeDocumentParser.excelColumnLabel(25))
        assertEquals("AA", com.example.ui.ArcanumOfficeDocumentParser.excelColumnLabel(26))

        val tempDocx = File.createTempFile("arcanum_test_doc", ".docx").apply { deleteOnExit() }
        com.example.ui.ArcanumOfficeSampleGenerator.writeSampleDocxFile(tempDocx)
        assertTrue(tempDocx.exists() && tempDocx.length() > 100L)

        val tempXlsx = File.createTempFile("arcanum_test_sheet", ".xlsx").apply { deleteOnExit() }
        com.example.ui.ArcanumOfficeSampleGenerator.writeSampleXlsxFile(tempXlsx)
        assertTrue(tempXlsx.exists() && tempXlsx.length() > 100L)

        val tempPptx = File.createTempFile("arcanum_test_slides", ".pptx").apply { deleteOnExit() }
        com.example.ui.ArcanumOfficeSampleGenerator.writeSamplePptxFile(tempPptx)
        assertTrue(tempPptx.exists() && tempPptx.length() > 100L)

        val tempCsv = File.createTempFile("arcanum_test_table", ".csv").apply {
            writeText("Nama,Jumlah,Status\nKas Utama,15000000,AMAN\nCadangan,5000000,AKTIF")
            deleteOnExit()
        }
        val parsedCsvWorkbook = com.example.ui.ArcanumOfficeDocumentParser.parseExcelWorkbook(tempCsv, "Kas", "")
        assertEquals(1, parsedCsvWorkbook.sheets.size)
        assertEquals(3, parsedCsvWorkbook.sheets.first().rows.size)
        assertEquals("Kas Utama", parsedCsvWorkbook.sheets.first().rows[1][0])
    }

    @Test
    fun testAuralysMusicChamberIntegrationAndPinRouting() {
        // 1. VaultTab enum verification
        val tabs = VaultTab.entries
        assertTrue("VaultTab must contain ISOLATED_AURALYS", tabs.contains(VaultTab.ISOLATED_AURALYS))

        // 2. Candidate ID verification
        assertEquals(96, com.example.data.SecurityConfig.ISOLATED_AURALYS_CANDIDATE_ID)

        // 3. Default SecurityConfig verification (000000 - 000000 - 000000)
        val config = com.example.data.SecurityConfig()
        assertEquals("000000", config.auralysPin1)
        assertEquals("000000", config.auralysPin2)
        assertEquals("000000", config.auralysPin3)

        val (p1, p2, p3) = config.getAuralysPins()
        assertEquals("000000", p1)
        assertEquals("000000", p2)
        assertEquals("000000", p3)

        // 4. L1 & Full 3-Layer PIN Match verification
        assertTrue("L1 '000000' must match Auralys L1", config.isAuralysLayer1Match("000000"))
        assertTrue("L1 case-insensitive match", config.isAuralysLayer1Match("000000"))
        assertTrue(
            "Full 3-layer '000000-000000-000000' must match Auralys chamber",
            config.isAuralysFullMatch("000000", "000000", "000000")
        )
        assertTrue(
            "Wrong L3 must not match",
            !config.isAuralysFullMatch("000000", "000000", "999999")
        )

        // 5. Weak PIN Exception verification for 000000
        val defaultAllowed = com.example.data.EncryptionScriptEngine.checkWeakPinReason("000000", allowAuralysDefault = true)
        assertEquals("000000 must be allowed when allowAuralysDefault = true", null, defaultAllowed)

        val defaultBlocked = com.example.data.EncryptionScriptEngine.checkWeakPinReason("000000", allowAuralysDefault = false)
        assertNotNull("000000 must be flagged as weak pin when allowAuralysDefault = false", defaultBlocked)

        // 6. Collision detection verification
        val collisionWithAuralys = config.findPinCollisionReason(
            p1 = "000000",
            p2 = "000000",
            p3 = "000000",
            excludeAuralys = false
        )
        assertNotNull("Should detect collision with Auralys L1", collisionWithAuralys)
        assertTrue(collisionWithAuralys!!.contains("Auralys Music Chamber"))

        val collisionExcluded = config.findPinCollisionReason(
            p1 = "000000",
            p2 = "000000",
            p3 = "000000",
            excludeAuralys = true
        )
        assertEquals("When excluding Auralys, own pins do not collide with self", null, collisionExcluded)
    }

    @Test
    fun testAuralysAudioControllerAndScoping() {
        // 1. Online catalog verification
        val songs = com.example.media.AuralysAudioController.catalogOnlineSongs
        assertTrue("Auralys catalog must have online streaming songs", songs.isNotEmpty())
        val firstSong = songs.first()
        assertTrue(firstSong.title.isNotBlank())
        assertTrue(firstSong.artist.isNotBlank())
        assertTrue(firstSong.lyrics.isNotEmpty())

        // 2. Format time verification
        assertEquals("00:00", com.example.media.AuralysAudioController.formatTime(0))
        assertEquals("01:05", com.example.media.AuralysAudioController.formatTime(65))
        assertEquals("02:30", com.example.media.AuralysAudioController.formatTime(150))

        // 3. Vault Folder Audio Scoping verification
        val dummyItems = listOf(
            com.example.data.VaultItemEntity(
                id = 1,
                name = "song_folder_a.mp3",
                extension = "mp3",
                category = "AUDIO",
                sizeBytes = 2048L,
                groupArchiveName = "Musik Santai"
            ),
            com.example.data.VaultItemEntity(
                id = 2,
                name = "song_folder_b.wav",
                extension = "wav",
                category = "AUDIO",
                sizeBytes = 4096L,
                groupArchiveName = "Musik Olahraga"
            ),
            com.example.data.VaultItemEntity(
                id = 3,
                name = "image.png",
                extension = "png",
                category = "IMAGE",
                sizeBytes = 1024L,
                groupArchiveName = "Musik Santai"
            ),
            com.example.data.VaultItemEntity(
                id = 4,
                name = "song_root.flac",
                extension = "flac",
                category = "AUDIO",
                sizeBytes = 8192L,
                groupArchiveName = ""
            )
        )

        val santaiList = com.example.media.AuralysAudioController.loadVaultFolderAudio(dummyItems, "Musik Santai")
        assertEquals(1, santaiList.size)
        assertEquals("song_folder_a", santaiList.first().title)
        assertEquals(com.example.media.QueueSourceMode.VAULT_LOCAL_FOLDER, santaiList.first().sourceMode)

        val allAudioList = com.example.media.AuralysAudioController.loadVaultFolderAudio(dummyItems, "")
        assertEquals(3, allAudioList.size)

        val rootList = com.example.media.AuralysAudioController.loadVaultFolderAudio(dummyItems, "Root")
        assertEquals(1, rootList.size)
        assertEquals("song_root", rootList.first().title)

        val availableFolders = com.example.media.AuralysAudioController.getAvailableVaultAudioFolders(dummyItems)
        assertTrue(availableFolders.contains("Musik Santai"))
        assertTrue(availableFolders.contains("Musik Olahraga"))
        assertTrue(availableFolders.contains("Semua File Audio (Root)"))
    }
}





