package com.example.ui.theme

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

// =========================================================================
// 1. GLOBAL 6-THEME ENGINE: DUOLINGO 3D, GLASSMORPHISM, NEUMORPHISM, MINIMALIST, FLAT DESIGN, BENTO GLOW
// =========================================================================

enum class AppVisualTheme(
    val id: String,
    val displayName: String,
    val subtitle: String,
    val badgeText: String
) {
    DUOLINGO(
        id = "DUOLINGO",
        displayName = "Duolingo 3D",
        subtitle = "Kartu taktil 3D dengan bibir bawah tebal, garis tegas & warna ceria",
        badgeText = "3D PLAYFUL"
    ),
    GLASSMORPHISM(
        id = "GLASSMORPHISM",
        displayName = "Glassmorphism",
        subtitle = "Permukaan kaca es buram berlapis dengan pantulan cahaya & aura neon",
        badgeText = "FROSTED GLASS"
    ),
    NEUMORPHISM(
        id = "NEUMORPHISM",
        displayName = "Neumorphism",
        subtitle = "Permukaan soft-extruded timbul dengan bayangan ganda dua arah halus",
        badgeText = "SOFT EXTRUDED"
    ),
    MINIMALIST(
        id = "MINIMALIST",
        displayName = "Minimalist",
        subtitle = "Kanvas bersih editorial berjarak lega dengan garis tepi hairline 0.8dp",
        badgeText = "ULTRA CLEAN"
    ),
    FLAT_DESIGN(
        id = "FLAT_DESIGN",
        displayName = "Flat Design",
        subtitle = "Blok warna 2D geometris kontras tinggi tanpa gradien & tanpa bayangan",
        badgeText = "2D FLAT"
    ),
    BENTO_GLOW(
        id = "BENTO_GLOW",
        displayName = "Bento Glow",
        subtitle = "Modular Bento squircle ala iOS 18, Linear & Arc dengan kilau rim atas",
        badgeText = "BENTO GLOW"
    );

    companion object {
        fun fromId(id: String?): AppVisualTheme {
            return entries.find { it.id.equals(id?.trim(), ignoreCase = true) } ?: DUOLINGO
        }
    }
}

enum class GsapMotionPreset(
    val id: String,
    val displayName: String,
    val subtitle: String,
    val badgeText: String,
    val defaultDurationMs: Int,
    val staggerStepMs: Long,
    val overshootFactor: Float
) {
    GSAP_ELASTIC_BOUNCE(
        id = "GSAP_ELASTIC_BOUNCE",
        displayName = "GSAP Elastic & Back.Out",
        subtitle = "Pantulan elastis & overshoot khas gsap.to(ease: 'back.out(1.7)')",
        badgeText = "ELASTIC OUT",
        defaultDurationMs = 540,
        staggerStepMs = 45L,
        overshootFactor = 1.72f
    ),
    GSAP_EXPO_CINEMATIC(
        id = "GSAP_EXPO_CINEMATIC",
        displayName = "GSAP Expo & Power4",
        subtitle = "Akselerasi cepat dengan pendaratan sinematik super mulus (expo.out)",
        badgeText = "EXPO OUT",
        defaultDurationMs = 480,
        staggerStepMs = 38L,
        overshootFactor = 1.15f
    ),
    IOS_LIQUID_SPRING(
        id = "IOS_LIQUID_SPRING",
        displayName = "iOS 18 Liquid Spring",
        subtitle = "Fisika pegas cair organik ala Apple Cupertino Human Interface",
        badgeText = "IOS SPRING",
        defaultDurationMs = 500,
        staggerStepMs = 40L,
        overshootFactor = 1.35f
    ),
    CYBER_MAGNETIC_3D(
        id = "CYBER_MAGNETIC_3D",
        displayName = "3D Magnetic Tilt & Flip",
        subtitle = "Rotasi perspektif 3D ekstra dalam pada sumbu X/Y saat disentuh & muncul",
        badgeText = "3D MAGNETIC",
        defaultDurationMs = 560,
        staggerStepMs = 50L,
        overshootFactor = 1.55f
    ),
    HYPER_STAGGER_WAVE(
        id = "HYPER_STAGGER_WAVE",
        displayName = "Cascade Stagger Wave",
        subtitle = "Gelombang urutan kartu muncul bertingkat dramatis ala timeline GSAP",
        badgeText = "WAVE STAGGER",
        defaultDurationMs = 620,
        staggerStepMs = 68L,
        overshootFactor = 1.65f
    ),
    TURBO_SNAPPY(
        id = "TURBO_SNAPPY",
        displayName = "Turbo Snappy 120Hz",
        subtitle = "Transisi mikro berkecepatan tinggi dengan respons taktil instan",
        badgeText = "TURBO 120HZ",
        defaultDurationMs = 310,
        staggerStepMs = 22L,
        overshootFactor = 1.25f
    ),
    GSAP_BOUNCE_GRAVITY(
        id = "GSAP_BOUNCE_GRAVITY",
        displayName = "Gravity Bounce.Out",
        subtitle = "Pantulan gravitasi fisik 4 tahap khas gsap.to(ease: 'bounce.out')",
        badgeText = "BOUNCE OUT",
        defaultDurationMs = 640,
        staggerStepMs = 52L,
        overshootFactor = 1.85f
    ),
    GSAP_VORTEX_SPIRAL_3D(
        id = "GSAP_VORTEX_SPIRAL_3D",
        displayName = "3D Vortex Spiral Flip",
        subtitle = "Pusaran rotasi 3D sumbu X+Y+Z disertai zoom sinematik",
        badgeText = "VORTEX 3D",
        defaultDurationMs = 590,
        staggerStepMs = 55L,
        overshootFactor = 2.05f
    ),
    GSAP_GLITCH_CYBER_SNAP(
        id = "GSAP_GLITCH_CYBER_SNAP",
        displayName = "Cyber Chromatic Glitch",
        subtitle = "Pergeseran kromatik RGB cepat dengan snap eksponensial tajam",
        badgeText = "CYBER GLITCH",
        defaultDurationMs = 390,
        staggerStepMs = 30L,
        overshootFactor = 1.60f
    ),
    GSAP_JELLY_SQUISH_MORPH(
        id = "GSAP_JELLY_SQUISH_MORPH",
        displayName = "Jelly Squish & Stretch",
        subtitle = "Deformasi kenyal X/Y berlawanan ala animasi karakter karet",
        badgeText = "JELLY MORPH",
        defaultDurationMs = 570,
        staggerStepMs = 46L,
        overshootFactor = 1.92f
    );

    companion object {
        fun fromId(id: String?): GsapMotionPreset {
            return entries.find { it.id.equals(id?.trim(), ignoreCase = true) } ?: GSAP_ELASTIC_BOUNCE
        }
    }
}

enum class GsapVisualEffectStyle(
    val id: String,
    val displayName: String,
    val badgeText: String,
    val subtitle: String,
    val primaryNeon: Color,
    val secondaryNeon: Color,
    val paletteColors: List<Color>
) {
    CYBER_PLASMA_PULSE(
        id = "CYBER_PLASMA_PULSE",
        displayName = "Adaptive Cyber Harmony",
        badgeText = "EYE-COMFORT NEON",
        subtitle = "Warna tombol harmonis nyaman di mata (mengikuti warna asli box + aksen sian-indigo lembut)",
        primaryNeon = Color(0xFF38BDF8),
        secondaryNeon = Color(0xFF6366F1),
        paletteColors = listOf(
            Color(0xFF38BDF8),
            Color(0xFF0284C7),
            Color(0xFF6366F1),
            Color(0xFF818CF8),
            Color(0xFF38BDF8)
        )
    ),
    MATRIX_EMERALD_SCAN(
        id = "MATRIX_EMERALD_SCAN",
        displayName = "Emerald Mint Tactical",
        badgeText = "EMERALD RADAR",
        subtitle = "Hijau zamrud & mint sejuk di mata dengan pemindai radar halus & sudut HUD",
        primaryNeon = Color(0xFF10B981),
        secondaryNeon = Color(0xFF06B6D4),
        paletteColors = listOf(
            Color(0xFF34D399),
            Color(0xFF10B981),
            Color(0xFF059669),
            Color(0xFF06B6D4),
            Color(0xFF34D399)
        )
    ),
    SOLAR_GOLD_SUPERNOVA(
        id = "SOLAR_GOLD_SUPERNOVA",
        displayName = "Obsidian Royal Gold",
        badgeText = "24K LUXURY",
        subtitle = "Kilau emas hangat 24K & amber elegan yang lembut di mata",
        primaryNeon = Color(0xFFF59E0B),
        secondaryNeon = Color(0xFFFBBF24),
        paletteColors = listOf(
            Color(0xFFFDE68A),
            Color(0xFFF59E0B),
            Color(0xFFD97706),
            Color(0xFFFBBF24),
            Color(0xFFFDE68A)
        )
    ),
    SAKURA_AURORA_WAVE(
        id = "SAKURA_AURORA_WAVE",
        displayName = "Sakura Aurora Pastel",
        badgeText = "PASTEL AURORA",
        subtitle = "Gradasi pastel sakura, lavender, & biru langit lembut anti-silau",
        primaryNeon = Color(0xFFF472B6),
        secondaryNeon = Color(0xFF818CF8),
        paletteColors = listOf(
            Color(0xFFF472B6),
            Color(0xFFC084FC),
            Color(0xFF818CF8),
            Color(0xFF38BDF8),
            Color(0xFFF472B6)
        )
    ),
    RAINBOW_ORBIT_LASER(
        id = "RAINBOW_ORBIT_LASER",
        displayName = "Selective Prism Accent",
        badgeText = "SOFT PRISM",
        subtitle = "Warna box tetap kalem dengan aksen pelangi pastel selektif pada garis tepi",
        primaryNeon = Color(0xFF38BDF8),
        secondaryNeon = Color(0xFFA855F7),
        paletteColors = listOf(
            Color(0xFF38BDF8),
            Color(0xFF818CF8),
            Color(0xFFC084FC),
            Color(0xFFF472B6),
            Color(0xFFFBBF24),
            Color(0xFF34D399),
            Color(0xFF38BDF8)
        )
    ),
    CHROMA_BRUTAL_GLITCH(
        id = "CHROMA_BRUTAL_GLITCH",
        displayName = "Dual-Tone Cyber Pulse",
        badgeText = "CYBER SPLIT",
        subtitle = "Garis tepi ganda sian-ungu modern dengan aksen mikro-glitch terkontrol",
        primaryNeon = Color(0xFF06B6D4),
        secondaryNeon = Color(0xFF8B5CF6),
        paletteColors = listOf(
            Color(0xFF06B6D4),
            Color(0xFF3B82F6),
            Color(0xFF8B5CF6),
            Color(0xFF06B6D4)
        )
    );

    val primaryColor: Color get() = primaryNeon
    val secondaryColor: Color get() = secondaryNeon

    companion object {
        fun fromId(id: String?): GsapVisualEffectStyle {
            if (id.isNullOrBlank()) return CYBER_PLASMA_PULSE
            return entries.find { it.id.equals(id.trim(), ignoreCase = true) } ?: CYBER_PLASMA_PULSE
        }
    }
}

enum class DynamicIslandLayoutMode(
    val id: String,
    val displayName: String,
    val subtitle: String,
    val shortBadge: String
) {
    DOCKED_PUSH_DOWN(
        id = "DOCKED_PUSH_DOWN",
        displayName = "Anti-Tutup (Dorong Bawah)",
        subtitle = "Berada di jalur atas tersendiri & mendorong konten ke bawah agar 0% menutupi layar",
        shortBadge = "ZERO OVERLAP"
    ),
    COMPACT_MINI_CHIP(
        id = "COMPACT_MINI_CHIP",
        displayName = "Kapsul Ramping 34dp",
        subtitle = "Mode bar super ramping hemat ruang yang tetap tidak menutupi elemen di bawahnya",
        shortBadge = "SLIM 34DP"
    ),
    FOLDABLE_RIBBON(
        id = "FOLDABLE_RIBBON",
        displayName = "Pita Lipat Atas",
        subtitle = "Dapat dilipat menjadi pita tipis di bibir atas layar kapan saja dengan 1 ketukan",
        shortBadge = "FOLD TAB"
    );

    companion object {
        fun fromId(id: String?): DynamicIslandLayoutMode {
            return entries.find { it.id.equals(id?.trim(), ignoreCase = true) } ?: DOCKED_PUSH_DOWN
        }
    }
}

enum class DynamicIslandShapeStyle(
    val id: String,
    val displayName: String,
    val subtitle: String,
    val shortBadge: String
) {
    PILL_CAPSULE(
        id = "PILL_CAPSULE",
        displayName = "Kapsul Kristal Aero",
        subtitle = "Kapsul kaca berlapis ganda dengan cincin turbin samping & pantulan prisma",
        shortBadge = "AERO CRYSTAL"
    ),
    CYBER_HEXAGON(
        id = "CYBER_HEXAGON",
        displayName = "Sayap Mecha Taktis",
        subtitle = "Poligon bersayap mekanikal ganda dengan ventilasi & baut titanium",
        shortBadge = "MECHA WING"
    ),
    BENTO_CHAMFER_BOX(
        id = "BENTO_CHAMFER_BOX",
        displayName = "Dek Bento 3D Timbul",
        subtitle = "Modul 3D timbul berlapis lip bawah & indikator matriks multi-warna",
        shortBadge = "3D BENTO DECK"
    ),
    TOP_NOTCH_DOCK(
        id = "TOP_NOTCH_DOCK",
        displayName = "Mahkota Kurva Cair",
        subtitle = "Menyatu di bibir atas layar dengan lekuk S-Curve organik berlapis",
        shortBadge = "CROWN DOCK"
    ),
    CLOUD_PUFF_RIBBON(
        id = "CLOUD_PUFF_RIBBON",
        displayName = "Awan Puff & Pita",
        subtitle = "Siluet awan bergelombang lembut dengan ornamen pita kawaii di kedua sisi",
        shortBadge = "CLOUD PUFF"
    ),
    RETRO_CASSETTE_TAPE(
        id = "RETRO_CASSETTE_TAPE",
        displayName = "Kaset Walkman 90s",
        subtitle = "Bentuk pita kaset klasik lengkap dengan 2 roda kumparan berputar real-time",
        shortBadge = "WALKMAN 90S"
    ),
    ROYAL_SCROLL_CREST(
        id = "ROYAL_SCROLL_CREST",
        displayName = "Gulungan Mahkota Sultan",
        subtitle = "Gulungan perkamen kerajaan dengan kepala mahkota emas di ujung kiri-kanan",
        shortBadge = "ROYAL SCROLL"
    ),
    ORGANIC_LEAF_POD(
        id = "ORGANIC_LEAF_POD",
        displayName = "Kapsul Daun Botani",
        subtitle = "Siluet daun organik asimetris dengan urat daun tropis bercahaya",
        shortBadge = "LEAF BOTANIC"
    ),
    STEAMPUNK_BRASS_GAUGE(
        id = "STEAMPUNK_BRASS_GAUGE",
        displayName = "Instrumen Roda Gigi",
        subtitle = "Panel perunggu klasik dengan roda gigi berputar & paku rivet di 4 sudut",
        shortBadge = "BRASS GEAR"
    ),
    CRYSTAL_PRISM_SHARD(
        id = "CRYSTAL_PRISM_SHARD",
        displayName = "Prisma Permata Berlian",
        subtitle = "Potongan kristal berlian multi-faset berkilau seperti permata safir",
        shortBadge = "DIAMOND PRISM"
    );

    val badgeText: String
        get() = shortBadge

    companion object {
        fun fromId(id: String?): DynamicIslandShapeStyle {
            return entries.find { it.id.equals(id?.trim(), ignoreCase = true) } ?: CLOUD_PUFF_RIBBON
        }
    }
}

enum class DynamicIslandColorPreset(
    val id: String,
    val displayName: String,
    val shortBadge: String,
    val bgHex: String,
    val borderHex: String,
    val glowHex: String,
    val isLightSurface: Boolean = false
) {
    PEARL_DAYLIGHT_GLASS(
        id = "PEARL_DAYLIGHT_GLASS",
        displayName = "Mutiara Cerah & Safir",
        shortBadge = "PEARL SKY",
        bgHex = "#F8FAFC",
        borderHex = "#0284C7",
        glowHex = "#38BDF8",
        isLightSurface = true
    ),
    MATCHA_CREAM_BOTANICAL(
        id = "MATCHA_CREAM_BOTANICAL",
        displayName = "Matcha Latte & Krem",
        shortBadge = "MATCHA CREAM",
        bgHex = "#F2FBF6",
        borderHex = "#16A34A",
        glowHex = "#84CC16",
        isLightSurface = true
    ),
    SAKURA_MILK_PASTEL(
        id = "SAKURA_MILK_PASTEL",
        displayName = "Sakura Pink & Susu",
        shortBadge = "SAKURA MILK",
        bgHex = "#FFF1F5",
        borderHex = "#EC4899",
        glowHex = "#F43F5E",
        isLightSurface = true
    ),
    RETRO_WALKMAN_AMBER(
        id = "RETRO_WALKMAN_AMBER",
        displayName = "Krem Kaset & Oranye 90s",
        shortBadge = "RETRO 90S",
        bgHex = "#FEF3C7",
        borderHex = "#EA580C",
        glowHex = "#F59E0B",
        isLightSurface = true
    ),
    ROYAL_IVORY_AURUM(
        id = "ROYAL_IVORY_AURUM",
        displayName = "Gading Putih & Emas 24K",
        shortBadge = "IVORY GOLD",
        bgHex = "#FFFBEB",
        borderHex = "#D97706",
        glowHex = "#FACC15",
        isLightSurface = true
    ),
    HOLO_OPAL_PRISM(
        id = "HOLO_OPAL_PRISM",
        displayName = "Opal Pelangi Holografik",
        shortBadge = "HOLO OPAL",
        bgHex = "#F3E8FF",
        borderHex = "#8B5CF6",
        glowHex = "#06B6D4",
        isLightSurface = true
    ),
    BENTO_INDIGO_GLOW(
        id = "BENTO_INDIGO_GLOW",
        displayName = "Safir Biru & Cyan Kristal",
        shortBadge = "SAPPHIRE BLUE",
        bgHex = "#1E293B",
        borderHex = "#38BDF8",
        glowHex = "#818CF8",
        isLightSurface = false
    ),
    EMERALD_MATRIX(
        id = "EMERALD_MATRIX",
        displayName = "Zamrud Hutan & Mint",
        shortBadge = "EMERALD MINT",
        bgHex = "#064E3B",
        borderHex = "#34D399",
        glowHex = "#2DD4BF",
        isLightSurface = false
    ),
    CRIMSON_STEALTH(
        id = "CRIMSON_STEALTH",
        displayName = "Beludru Ruby & Emas",
        shortBadge = "RUBY VELVET",
        bgHex = "#4C0519",
        borderHex = "#FB7185",
        glowHex = "#FBBF24",
        isLightSurface = false
    ),
    SULTAN_AURUM(
        id = "SULTAN_AURUM",
        displayName = "Perunggu & Mahkota Emas",
        shortBadge = "ROYAL BRONZE",
        bgHex = "#451A03",
        borderHex = "#FACC15",
        glowHex = "#FB923C",
        isLightSurface = false
    ),
    VIOLET_NEBULA(
        id = "VIOLET_NEBULA",
        displayName = "Galaksi Ungu & Magenta",
        shortBadge = "GALAXY PLUM",
        bgHex = "#2E1065",
        borderHex = "#C084FC",
        glowHex = "#F472B6",
        isLightSurface = false
    ),
    CUSTOM(
        id = "CUSTOM",
        displayName = "Kustom Warna & Material",
        shortBadge = "CUSTOM RGB",
        bgHex = "#F8FAFC",
        borderHex = "#0284C7",
        glowHex = "#38BDF8",
        isLightSurface = false
    );

    val bgColor: Color
        get() = AppThemeController.parseHexColor(bgHex, Color(0xFFF8FAFC))

    val borderColor: Color
        get() = AppThemeController.parseHexColor(borderHex, Color(0xFF0284C7))

    val glowColor: Color
        get() = AppThemeController.parseHexColor(glowHex, Color(0xFF38BDF8))

    companion object {
        fun fromId(id: String?): DynamicIslandColorPreset {
            return entries.find { it.id.equals(id?.trim(), ignoreCase = true) } ?: PEARL_DAYLIGHT_GLASS
        }
    }
}

// =========================================================================
// 2. GLOBAL COLOR PALETTE PRESETS + CUSTOM HEX/RGB ENGINE
// =========================================================================

enum class AppColorPalettePreset(
    val id: String,
    val displayName: String,
    val shortBadge: String,
    val primaryColor: Color,
    val primaryLipColor: Color,
    val softSurfaceColor: Color,
    val secondaryAccentColor: Color,
    val canvasTintColor: Color
) {
    DUOLINGO_GREEN(
        id = "DUOLINGO_GREEN",
        displayName = "Hijau Duolingo",
        shortBadge = "DEFAULT GREEN",
        primaryColor = Color(0xFF58CC02),
        primaryLipColor = Color(0xFF46A302),
        softSurfaceColor = Color(0xFFD7FFB8),
        secondaryAccentColor = Color(0xFF1CB0F6),
        canvasTintColor = Color(0xFFF6FBF4)
    ),
    CRIMSON_RED(
        id = "CRIMSON_RED",
        displayName = "Merah Karmin",
        shortBadge = "CRIMSON RED",
        primaryColor = Color(0xFFEF4444),
        primaryLipColor = Color(0xFFB91C1C),
        softSurfaceColor = Color(0xFFFEE2E2),
        secondaryAccentColor = Color(0xFFF97316),
        canvasTintColor = Color(0xFFFFF5F5)
    ),
    OCEAN_MACAW(
        id = "OCEAN_MACAW",
        displayName = "Biru Samudra",
        shortBadge = "OCEAN BLUE",
        primaryColor = Color(0xFF0EA5E9),
        primaryLipColor = Color(0xFF0369A1),
        softSurfaceColor = Color(0xFFE0F2FE),
        secondaryAccentColor = Color(0xFF6366F1),
        canvasTintColor = Color(0xFFF0F9FF)
    ),
    ROYAL_BEETLE(
        id = "ROYAL_BEETLE",
        displayName = "Ungu Kosmik",
        shortBadge = "ROYAL PURPLE",
        primaryColor = Color(0xFF9333EA),
        primaryLipColor = Color(0xFF6B21A8),
        softSurfaceColor = Color(0xFFF3E8FF),
        secondaryAccentColor = Color(0xFFEC4899),
        canvasTintColor = Color(0xFFFAF5FF)
    ),
    SOLAR_GOLD(
        id = "SOLAR_GOLD",
        displayName = "Emas Sultan",
        shortBadge = "SOLAR GOLD",
        primaryColor = Color(0xFFF59E0B),
        primaryLipColor = Color(0xFFB45309),
        softSurfaceColor = Color(0xFFFEF3C7),
        secondaryAccentColor = Color(0xFFEA580C),
        canvasTintColor = Color(0xFFFFFBEB)
    ),
    SAKURA_NEON(
        id = "SAKURA_NEON",
        displayName = "Pink Sakura",
        shortBadge = "SAKURA PINK",
        primaryColor = Color(0xFFEC4899),
        primaryLipColor = Color(0xFFBE185D),
        softSurfaceColor = Color(0xFFFCE7F3),
        secondaryAccentColor = Color(0xFF8B5CF6),
        canvasTintColor = Color(0xFFFDF2F8)
    ),
    CYBER_EMERALD(
        id = "CYBER_EMERALD",
        displayName = "Toska Zamrud",
        shortBadge = "CYBER MINT",
        primaryColor = Color(0xFF10B981),
        primaryLipColor = Color(0xFF047857),
        softSurfaceColor = Color(0xFFD1FAE5),
        secondaryAccentColor = Color(0xFF06B6D4),
        canvasTintColor = Color(0xFFECFDF5)
    ),
    CUSTOM(
        id = "CUSTOM",
        displayName = "Custom Bebas",
        shortBadge = "CUSTOM RGB",
        primaryColor = Color(0xFF58CC02),
        primaryLipColor = Color(0xFF46A302),
        softSurfaceColor = Color(0xFFD7FFB8),
        secondaryAccentColor = Color(0xFF1CB0F6),
        canvasTintColor = Color(0xFFF6FBF4)
    );

    companion object {
        fun fromId(id: String?): AppColorPalettePreset {
            return entries.find { it.id.equals(id?.trim(), ignoreCase = true) } ?: DUOLINGO_GREEN
        }
    }
}

// =========================================================================
// 3. MASCOT POSE, ACCESSORY & COSMIC AURA STUDIO STATE
// =========================================================================

enum class MascotActivityPose(
    val id: String,
    val displayName: String,
    val subtitle: String,
    val badgeColor: Color
) {
    AUTO_SMART(
        id = "AUTO_SMART",
        displayName = "Otomatis Pintar",
        subtitle = "Ikut aktivitas layar, media, unduhan & sensor secara otomatis",
        badgeColor = Color(0xFF58CC02)
    ),
    WATCHING_MEDIA(
        id = "WATCHING_MEDIA",
        displayName = "Ikut Nonton 3D",
        subtitle = "Pakai kacamata 3D & makan popcorn menonton konten layar",
        badgeColor = Color(0xFF0EA5E9)
    ),
    SLEEPING(
        id = "SLEEPING",
        displayName = "Tidur Pulas Zzz",
        subtitle = "Mata terpejam nyaman dengan gelembung tidur & balon Zzz",
        badgeColor = Color(0xFF8B5CF6)
    ),
    DANCING(
        id = "DANCING",
        displayName = "Menari Party DJ",
        subtitle = "Goyang ritmis dengan equalizer & partikel not musik",
        badgeColor = Color(0xFFEC4899)
    ),
    READING(
        id = "READING",
        displayName = "Baca Kitab Siber",
        subtitle = "Memegang buku hologram terbuka & mata membaca halaman",
        badgeColor = Color(0xFFF59E0B)
    ),
    SHIELD_GUARD(
        id = "SHIELD_GUARD",
        displayName = "Perisai Aegis",
        subtitle = "Menggenggam Perisai Heksagonal menjaga brankas",
        badgeColor = Color(0xFF10B981)
    ),
    ACROBATIC_SPIN(
        id = "ACROBATIC_SPIN",
        displayName = "Salto 360°",
        subtitle = "Melompat tinggi & berputar 360 derajat penuh energi",
        badgeColor = Color(0xFFEF4444)
    ),
    PETTING_BLISS(
        id = "PETTING_BLISS",
        displayName = "Elus Kepala Manja",
        subtitle = "Tangan lembut mengelus kepala maskot hingga berbunga-bunga",
        badgeColor = Color(0xFFF43F5E)
    ),
    EATING_BOBA_SNACK(
        id = "EATING_BOBA_SNACK",
        displayName = "Minum Boba & Kue",
        subtitle = "Memegang gelas boba mutiara & donat manis sambil mengunyah",
        badgeColor = Color(0xFFF97316)
    ),
    GAMING_PRO_PAD(
        id = "GAMING_PRO_PAD",
        displayName = "Main Game RGB",
        subtitle = "Memegang stik konsol gamepad RGB & fokus menekan tombol",
        badgeColor = Color(0xFF3B82F6)
    ),
    KATANA_SLASH(
        id = "KATANA_SLASH",
        displayName = "Jurus Pedang Neon",
        subtitle = "Mengayunkan katana plasma dengan efek kilatan tebasan",
        badgeColor = Color(0xFFE11D48)
    ),
    ARCANE_SPELLCAST(
        id = "ARCANE_SPELLCAST",
        displayName = "Rapal Mantra Sihir",
        subtitle = "Mengangkat tongkat kristal & memutar lingkaran rune kuno",
        badgeColor = Color(0xFFA855F7)
    ),
    GYM_DUMBBELL_LIFT(
        id = "GYM_DUMBBELL_LIFT",
        displayName = "Angkat Barbel Emas",
        subtitle = "Olahraga mengangkat barbel 100KG penuh semangat membara",
        badgeColor = Color(0xFFEAB308)
    ),
    COFFEE_CHILL(
        id = "COFFEE_CHILL",
        displayName = "Ngopi Santai",
        subtitle = "Menyeruput cangkir kopi hangat dengan kepulan uap harum",
        badgeColor = Color(0xFFB45309)
    ),
    BUBBLE_BATH_DUCK(
        id = "BUBBLE_BATH_DUCK",
        displayName = "Mandi Busa & Bebek",
        subtitle = "Main gelembung sabun berkilau bersama bebek karet kuning",
        badgeColor = Color(0xFF06B6D4)
    ),
    SELFIE_FLASH_POSE(
        id = "SELFIE_FLASH_POSE",
        displayName = "Foto Selfie Peace",
        subtitle = "Pose jari V-sign ceria dengan kamera & kilatan flash",
        badgeColor = Color(0xFF14B8A6)
    );

    companion object {
        fun fromId(id: String?): MascotActivityPose {
            return entries.find { it.id.equals(id?.trim(), ignoreCase = true) } ?: AUTO_SMART
        }
    }
}

enum class MascotAccessory(
    val id: String,
    val displayName: String,
    val badgeColor: Color,
    val themeCategory: String = "SEMUA"
) {
    NONE("NONE", "Tanpa Aksesoris", Color(0xFF64748B), "DEFAULT"),

    // Tema Siber & Mecha (11 item)
    CYBER_VR_VISOR("CYBER_VR_VISOR", "VR Visor Neon", Color(0xFF06B6D4), "SIBER"),
    MECHA_ANTENNA("MECHA_ANTENNA", "Antena Mecha", Color(0xFF3B82F6), "SIBER"),
    HACKER_ANON_MASK("HACKER_ANON_MASK", "Topeng Hacker", Color(0xFF10B981), "SIBER"),
    CYBER_MONOCLE("CYBER_MONOCLE", "Monokel Siber", Color(0xFF0EA5E9), "SIBER"),
    MINI_DRONE_ORBIT("MINI_DRONE_ORBIT", "Drone Pendamping", Color(0xFF6366F1), "SIBER"),
    MECHA_WINGS("MECHA_WINGS", "Sayap Mecha Jet", Color(0xFF2563EB), "SIBER"),
    CYBER_HEADPHONES("CYBER_HEADPHONES", "Headphone Musik", Color(0xFFEC4899), "SIBER"),
    GUNDAM_VFIN_HELMET("GUNDAM_VFIN_HELMET", "Helm Mecha V-Fin", Color(0xFF2563EB), "SIBER"),
    EXOSKELETON_HUD_HALO("EXOSKELETON_HUD_HALO", "Halo Radar Siber", Color(0xFF00F2FE), "SIBER"),
    CYBER_JETPACK_THRUSTERS("CYBER_JETPACK_THRUSTERS", "Jetpack Pendorong", Color(0xFFF97316), "SIBER"),

    // Tema Sultan & Kerajaan (9 item)
    ROYAL_CROWN("ROYAL_CROWN", "Mahkota Sultan", Color(0xFFF59E0B), "SULTAN"),
    DEAL_WITH_IT_SHADES("DEAL_WITH_IT_SHADES", "Kacamata Sultan", Color(0xFFD97706), "SULTAN"),
    GOLDEN_TOP_HAT("GOLDEN_TOP_HAT", "Topi Pesulap Emas", Color(0xFFB45309), "SULTAN"),
    ROYAL_MUSTACHE("ROYAL_MUSTACHE", "Kumis Bangsawan", Color(0xFF92400E), "SULTAN"),
    DIAMOND_TIARA("DIAMOND_TIARA", "Tiara Berlian", Color(0xFF38BDF8), "SULTAN"),
    DOLLAR_CHAIN_CROWN("DOLLAR_CHAIN_CROWN", "Medali Raja Emas", Color(0xFFEAB308), "SULTAN"),
    PIRATE_CAPTAIN_HAT("PIRATE_CAPTAIN_HAT", "Topi Kapten Laut", Color(0xFFDC2626), "SULTAN"),
    EMPEROR_DRAGON_CROWN("EMPEROR_DRAGON_CROWN", "Mahkota Kaisar Naga", Color(0xFFF59E0B), "SULTAN"),
    SULTAN_DIAMOND_TURBAN("SULTAN_DIAMOND_TURBAN", "Sorban Berlian Sultan", Color(0xFFEAB308), "SULTAN"),

    // Tema Fantasi, Anime & Kosmik (12 item)
    DRAGON_HORNS("DRAGON_HORNS", "Tanduk Naga Api", Color(0xFFEF4444), "FANTASI"),
    COSMIC_HALO("COSMIC_HALO", "Halo Bintang", Color(0xFF10B981), "FANTASI"),
    WIZARD_HAT("WIZARD_HAT", "Topi Penyihir", Color(0xFF8B5CF6), "FANTASI"),
    ANGEL_WINGS("ANGEL_WINGS", "Sayap Malaikat", Color(0xFF38BDF8), "FANTASI"),
    DEMON_WINGS("DEMON_WINGS", "Sayap Iblis", Color(0xFFDC2626), "FANTASI"),
    THIRD_EYE_CRYSTAL("THIRD_EYE_CRYSTAL", "Kristal Dahi", Color(0xFFA855F7), "FANTASI"),
    FAIRY_FLOWER_CROWN("FAIRY_FLOWER_CROWN", "Mahkota Bunga Peri", Color(0xFFEC4899), "FANTASI"),
    HOLOGRAM_TWIN_BLADES("HOLOGRAM_TWIN_BLADES", "Pedang Terbang Kembar", Color(0xFF8B5CF6), "FANTASI"),
    SERAPHIM_SIX_WINGS("SERAPHIM_SIX_WINGS", "6 Sayap Seraphim", Color(0xFF38BDF8), "FANTASI"),
    ABYSSAL_DEMON_LORD_HORNS("ABYSSAL_DEMON_LORD_HORNS", "Tanduk Raja Iblis", Color(0xFFE11D48), "FANTASI"),
    ELEMENTAL_ORBS_CROWN("ELEMENTAL_ORBS_CROWN", "Orbit 5 Bola Elemen", Color(0xFF10B981), "FANTASI"),
    KITSUNE_SPIRIT_MASK("KITSUNE_SPIRIT_MASK", "Topeng Rubah Kitsune", Color(0xFFEF4444), "ANIME"),
    SHOGUN_KABUTO_CREST("SHOGUN_KABUTO_CREST", "Helm Kabuto Shogun", Color(0xFFD97706), "ANIME"),

    // Tema Lucu & Kawaii (7 item)
    CAT_EARS_NEKO("CAT_EARS_NEKO", "Telinga Kucing", Color(0xFFEC4899), "LUCU"),
    BUNNY_EARS("BUNNY_EARS", "Telinga Kelinci", Color(0xFFF472B6), "LUCU"),
    KAWAII_RIBBON_BOW("KAWAII_RIBBON_BOW", "Pita Sakura", Color(0xFFF43F5E), "LUCU"),
    SUNFLOWER_SPROUT("SUNFLOWER_SPROUT", "Tunas Bunga", Color(0xFF84CC16), "LUCU"),
    BAMBOO_COPTER("BAMBOO_COPTER", "Baling Bambu", Color(0xFFFACC15), "LUCU"),
    GLASSES_3D("GLASSES_3D", "Kacamata Nonton 3D", Color(0xFF0EA5E9), "LUCU"),
    CHEF_TOQUE_CROWN("CHEF_TOQUE_CROWN", "Topi Master Koki", Color(0xFFF43F5E), "LUCU"),

    // 12 New Anime Collab, Super Hero & High Fantasy Accessories (52 Total)
    HANAFUDA_SUN_EARRINGS("HANAFUDA_SUN_EARRINGS", "Anting Hanafuda & Luka", Color(0xFFDC2626), "ANIME"),
    NEZUKO_BAMBOO_MUZZLE("NEZUKO_BAMBOO_MUZZLE", "Bambu & Pita Nezuko", Color(0xFF10B981), "ANIME"),
    AOT_SCOUT_HOOD_GOGGLES("AOT_SCOUT_HOOD_GOGGLES", "Tudung & Goggle AoT", Color(0xFF15803D), "ANIME"),
    BLEACH_HOLLOW_MASK("BLEACH_HOLLOW_MASK", "Topeng Hollow Vizard", Color(0xFFE11D48), "ANIME"),
    NARUTO_SHINOBI_HEADBAND("NARUTO_SHINOBI_HEADBAND", "Ikat Kepala Rikudou", Color(0xFFF97316), "ANIME"),
    LINK_CLICK_GOLDEN_EYES_GEAR("LINK_CLICK_GOLDEN_EYES_GEAR", "Lensa Waktu Link Click", Color(0xFFEAB308), "ANIME"),
    SOUL_LAND_SPIRIT_RINGS("SOUL_LAND_SPIRIT_RINGS", "Cincin Roh 100.000 Thn", Color(0xFF0284C7), "ANIME"),
    BTTH_EMPEROR_FLAME_CREST("BTTH_EMPEROR_FLAME_CREST", "Segel Kaisar Api BTTH", Color(0xFF9333EA), "ANIME"),
    HERO_IRON_HUD_HELMET("HERO_IRON_HUD_HELMET", "Helm Nano-Tech Hero", Color(0xFFEF4444), "HERO"),
    HERO_DARK_BAT_COWL("HERO_DARK_BAT_COWL", "Topeng Ksatria Malam", Color(0xFF334155), "HERO"),
    FANTASY_VALKYRIE_WING_HELM("FANTASY_VALKYRIE_WING_HELM", "Tiara Sayap Valkyrie", Color(0xFF38BDF8), "FANTASI"),
    FANTASY_LICH_SOUL_CROWN("FANTASY_LICH_SOUL_CROWN", "Mahkota Jiwa Lich", Color(0xFFA855F7), "FANTASI");

    companion object {
        fun fromId(id: String?): MascotAccessory {
            return entries.find { it.id.equals(id?.trim(), ignoreCase = true) } ?: NONE
        }
    }
}

enum class MascotBodySkin(
    val id: String,
    val displayName: String,
    val subtitle: String,
    val previewColor: Color,
    val secondaryColor: Color,
    val themeCategory: String = "SEMUA"
) {
    DEFAULT_MOOD("DEFAULT_MOOD", "Mood Otomatis", "Berubah dinamis mengikuti mood & elemen", Color(0xFF58CC02), Color(0xFF1CB0F6), "DEFAULT"),
    ROYAL_GOLD_24K("ROYAL_GOLD_24K", "Emas Murni 24K", "Logam emas berkilau dengan kemilau bintang", Color(0xFFFACC15), Color(0xFFB45309), "SULTAN"),
    DIAMOND_ICE_CRYSTAL("DIAMOND_ICE_CRYSTAL", "Kristal Berlian Es", "Faset berlian sian-es reflektif berkilau", Color(0xFF38BDF8), Color(0xFF0284C7), "FANTASI"),
    MECHA_TITANIUM("MECHA_TITANIUM", "Mecha Titanium", "Baja perak dengan jalur sirkuit siber neon", Color(0xFF94A3B8), Color(0xFF0EA5E9), "SIBER"),
    NEBULA_GALAXY("NEBULA_GALAXY", "Nebula Galaksi", "Ungu-biru kosmik dengan rasi bintang", Color(0xFF8B5CF6), Color(0xFFEC4899), "FANTASI"),
    LAVA_MAGMA("LAVA_MAGMA", "Lava Magma", "Batu vulkanik dengan retakan magma menyala", Color(0xFFEF4444), Color(0xFFF97316), "FANTASI"),
    CYBER_EMERALD("CYBER_EMERALD", "Zamrud Siber", "Hijau matriks dengan grid kode holografik", Color(0xFF10B981), Color(0xFF059669), "SIBER"),
    SAKURA_MOCHI("SAKURA_MOCHI", "Sakura Pastel", "Pink mochi lembut dengan kelopak sakura", Color(0xFFF472B6), Color(0xFFDB2777), "LUCU"),
    PHANTOM_HOLOGRAM("PHANTOM_HOLOGRAM", "Hologram Neon", "Sian futuristik dengan garis scanline", Color(0xFF06B6D4), Color(0xFF6366F1), "SIBER"),
    OBSIDIAN_STEALTH("OBSIDIAN_STEALTH", "Obsidian Gelap", "Karbon hitam taktis dengan aksen emas", Color(0xFF1E293B), Color(0xFFF59E0B), "SIBER"),
    RUBY_CRIMSON_GEM("RUBY_CRIMSON_GEM", "Kristal Ruby Merah", "Permata merah delima berkilau mewah", Color(0xFFE11D48), Color(0xFF881337), "SULTAN"),
    AMETHYST_ROYAL("AMETHYST_ROYAL", "Permata Kecubung", "Kristal ungu royal dengan kilau mistis", Color(0xFFA855F7), Color(0xFF581C87), "SULTAN"),
    SOLAR_SUNBURST("SOLAR_SUNBURST", "Plasma Matahari", "Inti surya keemasan dengan pijar korona", Color(0xFFF97316), Color(0xFFFACC15), "FANTASI"),
    BIOLUM_OCEAN("BIOLUM_OCEAN", "Laut Bioluminesensi", "Biru samudra dalam dengan cahaya neon", Color(0xFF0284C7), Color(0xFF2DD4BF), "FANTASI"),
    FOREST_SPIRIT("FOREST_SPIRIT", "Roh Hutan Giok", "Hijau daun rimba dengan partikel peri", Color(0xFF22C55E), Color(0xFF15803D), "FANTASI"),
    RAINBOW_PRISM_RGB("RAINBOW_PRISM_RGB", "Pelangi Prisma RGB", "Spektrum warna gamer RGB berkilau", Color(0xFFEC4899), Color(0xFF06B6D4), "SIBER"),
    STEAMPUNK_BRONZE("STEAMPUNK_BRONZE", "Perunggu Steampunk", "Logam tembaga-perunggu klasik mekanis", Color(0xFFD97706), Color(0xFF78350F), "SULTAN"),
    CELESTIAL_MARBLE("CELESTIAL_MARBLE", "Marmer Putih Dewa", "Batu pualam putih bersih berurat emas", Color(0xFFF8FAFC), Color(0xFFEAB308), "SULTAN"),
    VOID_SHADOW("VOID_SHADOW", "Bayangan Void", "Energi gelap antariksa bertepi ungu neon", Color(0xFF090D16), Color(0xFF7C3AED), "FANTASI"),
    COTTON_CANDY("COTTON_CANDY", "Permen Kapas", "Gradasi biru-pink manis ala gulali", Color(0xFFF9A8D4), Color(0xFF7DD3FC), "LUCU"),
    CARBON_KEVLAR("CARBON_KEVLAR", "Baja Kevlar Taktis", "Anyaman serat karbon militer tahan peluru", Color(0xFF334155), Color(0xFF10B981), "SIBER"),
    EMPEROR_JADE("EMPEROR_JADE", "Batu Giok Kaisar", "Giok hijau kerajaan dengan ukiran naga emas", Color(0xFF059669), Color(0xFFFACC15), "SULTAN"),
    // 12 New Complex Multi-Layered 3D Skins (34 Total)
    DRAGON_SCALE_RUBY("DRAGON_SCALE_RUBY", "Sisik Naga Merah", "Tekstur sisik naga berlapis emas & bara api", Color(0xFFDC2626), Color(0xFFFACC15), "FANTASI"),
    GUNDAM_AERO_COMPOSITE("GUNDAM_AERO_COMPOSITE", "Armor Komposit Mecha", "Pelat baja putih-biru-merah dengan panel line", Color(0xFFE2E8F0), Color(0xFF2563EB), "SIBER"),
    CYBERPUNK_NEON_CIRCUIT("CYBERPUNK_NEON_CIRCUIT", "Sirkuit Neon Siber", "Hitam matriks dengan jalur motherboard menyala", Color(0xFF090D16), Color(0xFF00F2FE), "SIBER"),
    CELESTIAL_SERAPH_FEATHER("CELESTIAL_SERAPH_FEATHER", "Bulu Cahaya Surga", "Tekstur sayap malaikat putih keemasan berlapis", Color(0xFFFEF9C3), Color(0xFF38BDF8), "FANTASI"),
    ABYSSAL_DEMON_CHITIN("ABYSSAL_DEMON_CHITIN", "Zirah Iblis Abyss", "Cangkang gelap dengan urat energi merah menyala", Color(0xFF31102F), Color(0xFFF43F5E), "FANTASI"),
    ROYAL_DAMASCUS_GOLD("ROYAL_DAMASCUS_GOLD", "Baja Damaskus Sultan", "Pola pusaran baja damaskus bersepuh emas 24K", Color(0xFF78350F), Color(0xFFFACC15), "SULTAN"),
    ASTRAL_CONSTELLATION_MAP("ASTRAL_CONSTELLATION_MAP", "Peta Rasi Bintang", "Biru malam antariksa dengan garis rasi zodiak", Color(0xFF1E1B4B), Color(0xFF38BDF8), "FANTASI"),
    SAMURAI_LACQUER_ARMOR("SAMURAI_LACQUER_ARMOR", "Perisai Pernis Shogun", "Anyaman pelat yoroi merah-hitam tradisional", Color(0xFF7F1D1D), Color(0xFFF59E0B), "ANIME"),
    GLACIER_AURORA_PRISM("GLACIER_AURORA_PRISM", "Es Aurora Kutub", "Kristal es berlapis gelombang aurora hijau-ungu", Color(0xFF0284C7), Color(0xFF10B981), "FANTASI"),
    THUNDER_PLASMA_COIL("THUNDER_PLASMA_COIL", "Inti Petir Tegangan", "Kuning-biru elektrik dengan busur petir aktif", Color(0xFFEAB308), Color(0xFF2563EB), "ANIME"),
    PLUSH_VELVET_QUILT("PLUSH_VELVET_QUILT", "Boneka Rajut Jahit", "Tekstur kain boneka jahitan silang pastel", Color(0xFFF472B6), Color(0xFFA855F7), "LUCU"),
    BIO_MECH_SYMBIOTE("BIO_MECH_SYMBIOTE", "Biomimetik Eksosuit", "Serat otot bionik perak-hijau futuristik", Color(0xFF065F46), Color(0xFF34D399), "SIBER"),
    // 10 New Anime Collab, Super Hero & Fantasy Skins (44 Total)
    SUN_BREATHING_SCAR_SKIN("SUN_BREATHING_SCAR_SKIN", "Cahaya Hinokami", "Pijar pernapasan matahari merah-emas membara", Color(0xFFDC2626), Color(0xFFFACC15), "ANIME"),
    TITAN_STEAM_MUSCLE_SKIN("TITAN_STEAM_MUSCLE_SKIN", "Energi Titan Colossal", "Serat energi uap panas merah-jingga kolosal", Color(0xFFB91C1C), Color(0xFFF97316), "ANIME"),
    HOLLOW_SPIRIT_REIATSU("HOLLOW_SPIRIT_REIATSU", "Reiatsu Hollow Hitam", "Energi spiritual Shinigami hitam-merah menyala", Color(0xFF111827), Color(0xFFE11D48), "ANIME"),
    NINE_TAILS_CHAKRA_CLOAK("NINE_TAILS_CHAKRA_CLOAK", "Mode Chakra Kurama", "Tubuh cahaya cakra emas menyala bermotif magatama", Color(0xFFFACC15), Color(0xFFF97316), "ANIME"),
    TIME_CHRONO_GRID_SKIN("TIME_CHRONO_GRID_SKIN", "Dimensi Foto Waktu", "Kisi-kisi waktu sian-emas ala studio Link Click", Color(0xFF0284C7), Color(0xFFFACC15), "ANIME"),
    SOUL_RING_DIVINE_GOLD("SOUL_RING_DIVINE_GOLD", "Cahaya Dewa Douluo", "Faset kristal biru samudra & rune emas Poseidon", Color(0xFF0EA5E9), Color(0xFFFDE047), "ANIME"),
    HEAVENLY_FLAME_LOTUS_SKIN("HEAVENLY_FLAME_LOTUS_SKIN", "Api Surgawi BTTH", "Perpaduan api giok, ungu & emas Kaisar Api", Color(0xFF10B981), Color(0xFFA855F7), "ANIME"),
    NANO_HEX_TITANIUM_RED("NANO_HEX_TITANIUM_RED", "Nano-Alloy Hero", "Kisi heksagonal nano-tech merah metalik & emas", Color(0xFFEF4444), Color(0xFFFACC15), "HERO"),
    VIBRANIUM_KINETIC_WEAVE("VIBRANIUM_KINETIC_WEAVE", "Serat Kinetik Hero", "Anyaman baja hitam dengan urat energi ungu", Color(0xFF1E1B4B), Color(0xFFA855F7), "HERO"),
    ELVEN_STARLIGHT_RUNE("ELVEN_STARLIGHT_RUNE", "Rune Cahaya Elf", "Hijau zamrud hutan kuno dengan sulur emas", Color(0xFF059669), Color(0xFFFEF08A), "FANTASI");

    companion object {
        fun fromId(id: String?): MascotBodySkin {
            return entries.find { it.id.equals(id?.trim(), ignoreCase = true) } ?: DEFAULT_MOOD
        }
    }
}

enum class MascotUpperOutfit(
    val id: String,
    val displayName: String,
    val subtitle: String,
    val badgeColor: Color,
    val themeCategory: String = "SEMUA"
) {
    NONE("NONE", "Tanpa Baju", "Tampilan tubuh murni tanpa pakaian", Color(0xFF64748B), "DEFAULT"),
    CYBER_HOODIE("CYBER_HOODIE", "Hoodie Siber", "Hoodie streetwear dengan tali neon & saku", Color(0xFF0EA5E9), "SIBER"),
    EXECUTIVE_TUXEDO("EXECUTIVE_TUXEDO", "Jas Tuksedo", "Jas hitam eksekutif, kemeja putih & dasi kupu", Color(0xFF1E293B), "SULTAN"),
    ROYAL_KING_CLOAK("ROYAL_KING_CLOAK", "Jubah Raja Sultan", "Jubah merah beludru berkerah bulu & permata", Color(0xFFEF4444), "SULTAN"),
    AEGIS_KNIGHT_ARMOR("AEGIS_KNIGHT_ARMOR", "Armor Ksatria", "Pelat dada baja perak-emas beremblem perisai", Color(0xFF3B82F6), "FANTASI"),
    SAMURAI_KIMONO("SAMURAI_KIMONO", "Kimono Samurai", "Jubah silang Jepang dengan sabuk obi emas", Color(0xFFDC2626), "ANIME"),
    DETECTIVE_TRENCH_VEST("DETECTIVE_TRENCH_VEST", "Rompi Detektif", "Rompi detektif klasik dengan dasi & lencana", Color(0xFFB45309), "MODERN"),
    COZY_KNIT_SWEATER("COZY_KNIT_SWEATER", "Sweater Rajut", "Sweater hangat motif berlian musim dingin", Color(0xFFEC4899), "LUCU"),
    ASTRONAUT_SUIT("ASTRONAUT_SUIT", "Baju Astronot", "Seragam antariksa dengan panel kontrol dada", Color(0xFF06B6D4), "SIBER"),
    ARCANUM_MAGE_ROBE("ARCANUM_MAGE_ROBE", "Jubah Penyihir", "Jubah mistis ungu dengan rune emas kosmik", Color(0xFF8B5CF6), "FANTASI"),
    SHINOBI_NINJA_VEST("SHINOBI_NINJA_VEST", "Rompi Ninja Shinobi", "Rompi tempur ninja dengan gulungan jurus", Color(0xFF10B981), "ANIME"),
    RED_CLOUD_CLOAK("RED_CLOUD_CLOAK", "Jubah Awan Merah", "Jubah hitam legendaris bermotif awan merah", Color(0xFFE11D48), "ANIME"),
    DRAGON_SCALE_ARMOR("DRAGON_SCALE_ARMOR", "Armor Sisik Naga", "Zirah tempur sisik naga api berkerah emas", Color(0xFFF97316), "FANTASI"),
    CELESTIAL_ANGEL_ROBE("CELESTIAL_ANGEL_ROBE", "Jubah Malaikat Suci", "Jubah putih surgawi dengan pita selendang emas", Color(0xFF38BDF8), "FANTASI"),
    VARSITY_BASEBALL_JACKET("VARSITY_BASEBALL_JACKET", "Jaket Varsity Kampus", "Jaket baseball dua warna dengan huruf A besar", Color(0xFF2563EB), "MODERN"),
    HYPEBEAST_STREET_TEE("HYPEBEAST_STREET_TEE", "Kaos Hypebeast", "Kaos oversize streetwear dengan rantai emas", Color(0xFFF59E0B), "MODERN"),
    BIKER_LEATHER_JACKET("BIKER_LEATHER_JACKET", "Jaket Kulit Biker", "Jaket kulit hitam resleting perak bergaya rock", Color(0xFF334155), "MODERN"),
    SCIENTIST_LAB_COAT("SCIENTIST_LAB_COAT", "Jas Lab Ilmuwan", "Jas putih profesor dengan pena & tabung reaksi", Color(0xFF14B8A6), "MODERN"),
    PRO_JERSEY_SPORT("PRO_JERSEY_SPORT", "Jersey Basket #23", "Jersey olahraga juara dengan nomor punggung emas", Color(0xFFEF4444), "MODERN"),
    GUNDAM_MECHA_CHEST("GUNDAM_MECHA_CHEST", "Zirah Mecha V-Fin", "Pelat dada robot tempur biru-merah-kuning", Color(0xFF3B82F6), "SIBER"),
    DINO_ONESIE_PAJAMA("DINO_ONESIE_PAJAMA", "Kostum Dino Lucu", "Piyama dinosaurus hijau imut dengan kancing perut", Color(0xFF84CC16), "LUCU"),
    MASTER_CHEF_APRON("MASTER_CHEF_APRON", "Baju Koki Master", "Seragam chef bintang lima dengan syal merah", Color(0xFFF43F5E), "LUCU"),
    // 12 New Complex Multi-Layered 3D Outfits (34 Total)
    GUNDAM_STRIKE_EXOSUIT("GUNDAM_STRIKE_EXOSUIT", "Zirah Mecha Strike", "Pauldron bahu roket, V-fin dada & inti reaktor", Color(0xFF2563EB), "SIBER"),
    ARCHANGEL_SERAPH_ARMOR("ARCHANGEL_SERAPH_ARMOR", "Zirah Malaikat Agung", "Pelat bahu sayap emas, pita suci & permata langit", Color(0xFF38BDF8), "FANTASI"),
    DEMON_OVERLORD_PLATE("DEMON_OVERLORD_PLATE", "Zirah Raja Iblis", "Bahu tanduk hitam-merah, rantai & inti magma", Color(0xFFDC2626), "FANTASI"),
    ROYAL_EMPEROR_HANFU("ROYAL_EMPEROR_HANFU", "Hanfu Kaisar Naga", "Jubah sutra berlapis sulaman naga emas & giok", Color(0xFFF59E0B), "SULTAN"),
    CYBERPUNK_EDGERUNNER_JACKET("CYBERPUNK_EDGERUNNER_JACKET", "Jaket Edgerunner LED", "Kerah tinggi menyala neon sian & sabuk taktis", Color(0xFF06B6D4), "SIBER"),
    PALADIN_CRUSADER_MANTLE("PALADIN_CRUSADER_MANTLE", "Mantel Suci Paladin", "Pelindung bahu salib emas & jubah putih kerajaan", Color(0xFFEAB308), "FANTASI"),
    PIRATE_ADMIRAL_COAT("PIRATE_ADMIRAL_COAT", "Mantel Laksamana Laut", "Jubah kapten merah-emas dengan epalet bahu rumbai", Color(0xFFB91C1C), "SULTAN"),
    PILOT_CAPTAIN_UNIFORM("PILOT_CAPTAIN_UNIFORM", "Seragam Kapten Pilot", "Jas penerbangan biru tua dengan lencana sayap emas", Color(0xFF1E3A8A), "MODERN"),
    MEDICAL_SURGEON_COAT("MEDICAL_SURGEON_COAT", "Jas Dokter Spesialis", "Jas medis putih bersih dengan stetoskop kalung", Color(0xFF10B981), "MODERN"),
    SHOGUN_YOROI_CUIRASS("SHOGUN_YOROI_CUIRASS", "Baju Zirah Shogun", "Pelat bambu samurai berlapis tali sutra merah", Color(0xFF991B1B), "ANIME"),
    STREETWEAR_TECH_PARKA("STREETWEAR_TECH_PARKA", "Parka Techwear Berlapis", "Jaket multi-kantong tali strap silang & buckle", Color(0xFF334155), "MODERN"),
    MAGICAL_IDOL_FRILL_DRESS("MAGICAL_IDOL_FRILL_DRESS", "Kostum Idol Bintang", "Pita berlapis renda bintang, permata hati & rumbai", Color(0xFFEC4899), "LUCU"),
    // 18 New Anime Collab, Super Hero & High-Fantasy Costumes with Distinct Silhouettes (52 Total)
    DEMON_SLAYER_CHECKERED_HAORI("DEMON_SLAYER_CHECKERED_HAORI", "Haori Tanjiro Demon Slayer", "Siluet lengan haori lebar motif kotak hijau-hitam & sabuk putih", Color(0xFF10B981), "ANIME"),
    FLAME_HASHIRA_CAPE("FLAME_HASHIRA_CAPE", "Mantel Pilar Api Rengoku", "Siluet jubah putih melebar bertepi kobaran lidah api merah-emas", Color(0xFFEF4444), "ANIME"),
    THUNDER_BREATH_HAORI("THUNDER_BREATH_HAORI", "Haori Petir Zenitsu", "Siluet kimono haori kuning-jingga melebar bermotif segitiga putih", Color(0xFFFACC15), "ANIME"),
    AOT_SURVEY_CORPS_CLOAK("AOT_SURVEY_CORPS_CLOAK", "Jubah Pasukan Pengintai AoT", "Siluet mantel tudung hijau Sayap Kebebasan & tabung ODM pinggang", Color(0xFF15803D), "ANIME"),
    BLEACH_BANKAI_SHIHAKUSHO("BLEACH_BANKAI_SHIHAKUSHO", "Haori Kapten Bankai Bleach", "Siluet jubah kapten putih berlapis shihakusho hitam & motif berlian", Color(0xFFE11D48), "ANIME"),
    NARUTO_HOKAGE_SAGE_CLOAK("NARUTO_HOKAGE_SAGE_CLOAK", "Jubah Hokage Rikudou Naruto", "Siluet mantel ekor api merah-putih berkerah tinggi & bola Gudoudama", Color(0xFFF97316), "ANIME"),
    UCHIHA_SUSANOO_ARMOR("UCHIHA_SUSANOO_ARMOR", "Zirah Tempur Klan Uchiha", "Siluet pelat bahu lebar merah tua klan Uchiha & tulang chakra", Color(0xFF991B1B), "ANIME"),
    LINK_CLICK_TIME_JACKET("LINK_CLICK_TIME_JACKET", "Jaket Studio Waktu Link Click", "Siluet hoodie asimetris hitam-putih Cheng Xiaoshi & Lu Guang", Color(0xFF0284C7), "ANIME"),
    SOUL_LAND_SEA_GOD_ARMOR("SOUL_LAND_SEA_GOD_ARMOR", "Zirah Dewa Laut Soul Land", "Siluet zirah emas-biru Tang San & 8 tombak laba-laba punggung", Color(0xFF0EA5E9), "ANIME"),
    BTTH_FLAME_EMPEROR_ROBE("BTTH_FLAME_EMPEROR_ROBE", "Jubah Kaisar Api BTTH", "Siluet jubah kultivator Xiao Yan, pita langit & penggaris raksasa", Color(0xFF9333EA), "ANIME"),
    HERO_NANO_IRON_ARMOR("HERO_NANO_IRON_ARMOR", "Zirah Nano-Tech Iron Hero", "Siluet zirah V-taper merah-emas, sayap nano & reaktor busur dada", Color(0xFFDC2626), "HERO"),
    HERO_SPIDER_SYMBIOTE_SUIT("HERO_SPIDER_SYMBIOTE_SUIT", "Kostum Laba-Laba Iron Spider", "Siluet jaring merah-biru dengan 4 cakar laba-laba emas mekanis", Color(0xFF2563EB), "HERO"),
    HERO_DARK_KNIGHT_CAPE("HERO_DARK_KNIGHT_CAPE", "Zirah Ksatria Kelelawar", "Siluet zirah taktis karbon, sabuk emas & sayap kelelawar bergerigi", Color(0xFF1E293B), "HERO"),
    HERO_THUNDER_ASGARD_PLATE("HERO_THUNDER_ASGARD_PLATE", "Zirah Dewa Petir Asgard", "Siluet pelat baja perak-biru,6 cakram petir & jubah merah megah", Color(0xFF0284C7), "HERO"),
    FANTASY_ELVEN_RANGER_CLOAK("FANTASY_ELVEN_RANGER_CLOAK", "Mantel Pemanah Elf Hutan", "Siluet bahu daun zamrud berlapis emas & tempat anak panah punggung", Color(0xFF059669), "FANTASI"),
    FANTASY_NECROMANCER_LICH_ROBE("FANTASY_NECROMANCER_LICH_ROBE", "Jubah Raja Lich Necromancer", "Siluet jubah koyak bayangan berkerah duri tulang & inti jiwa ungu", Color(0xFF7C3AED), "FANTASI"),
    FANTASY_VALKYRIE_DIVINE_DRESS("FANTASY_VALKYRIE_DIVINE_DRESS", "Zirah Valkyrie Bintang", "Siluet baju zirah sayap perak-emas dengan pita perang biru langit", Color(0xFF38BDF8), "FANTASI"),
    FANTASY_DRAGONSLAYER_BERSERKER("FANTASY_DRAGONSLAYER_BERSERKER", "Zirah Berserker Pembasmi Naga", "Siluet pelindung bahu taring naga raksasa & sabuk tengkorak api", Color(0xFFB91C1C), "FANTASI");

    companion object {
        fun fromId(id: String?): MascotUpperOutfit {
            return entries.find { it.id.equals(id?.trim(), ignoreCase = true) } ?: NONE
        }
    }
}

enum class MascotHandGloves(
    val id: String,
    val displayName: String,
    val subtitle: String,
    val badgeColor: Color,
    val themeCategory: String = "SEMUA"
) {
    DEFAULT_HANDS("DEFAULT_HANDS", "Tangan Polos", "Mengikuti warna material skin tubuh", Color(0xFF64748B), "DEFAULT"),
    CYBER_GAUNTLETS("CYBER_GAUNTLETS", "Gauntlet Siber", "Pelindung tangan mekanis berinti neon sian", Color(0xFF06B6D4), "SIBER"),
    BOXING_CHAMPS("BOXING_CHAMPS", "Sarung Tinju", "Sarung tinju juara merah besar berbantalan emas", Color(0xFFEF4444), "MODERN"),
    COSMIC_MAGIC_GLOVES("COSMIC_MAGIC_GLOVES", "Sihir Kosmik", "Sarung tangan ungu dengan cincin aura bintang", Color(0xFF8B5CF6), "FANTASI"),
    WHITE_BUTLER_GLOVES("WHITE_BUTLER_GLOVES", "Putih Butler", "Sarung tangan putih elegan dengan 3 jahitan", Color(0xFF475569), "SULTAN"),
    DRAGON_FLAME_CLAWS("DRAGON_FLAME_CLAWS", "Cakar Naga Api", "Tangan sisik merah dengan 3 cakar emas menyala", Color(0xFFF97316), "FANTASI"),
    MECHA_ROBOT_HANDS("MECHA_ROBOT_HANDS", "Tangan Mecha", "Sendi robotik titanium dengan indikator LED", Color(0xFF3B82F6), "SIBER"),
    SULTAN_GOLD_GAUNTLETS("SULTAN_GOLD_GAUNTLETS", "Emas Sultan", "Sarung tangan emas 24K bertahtakan permata", Color(0xFFF59E0B), "SULTAN"),
    COZY_WINTER_MITTENS("COZY_WINTER_MITTENS", "Mitten Rajut", "Sarung tangan rajut hangat dengan manset bulu", Color(0xFFEC4899), "LUCU"),
    PLASMA_WRIST_SHIELDS("PLASMA_WRIST_SHIELDS", "Perisai Energi", "Gelang perisai heksagonal energi di kedua tangan", Color(0xFF10B981), "SIBER"),
    NEKO_PAW_GLOVES("NEKO_PAW_GLOVES", "Tangan Bantalan Kucing", "Sarung tangan paw kucing lembut warna pink", Color(0xFFF472B6), "LUCU"),
    SHINOBI_NINJA_BRACERS("SHINOBI_NINJA_BRACERS", "Pelindung Lengan Ninja", "Perban ninja taktis dengan pelat besi shuriken", Color(0xFF1E293B), "ANIME"),
    FROST_CRYSTAL_GAUNTLETS("FROST_CRYSTAL_GAUNTLETS", "Gauntlet Es Abadi", "Sarung tangan kristal es biru dengan salju", Color(0xFF38BDF8), "FANTASI"),
    BLUE_FLAME_HANDS("BLUE_FLAME_HANDS", "Tangan Api Biru", "Aura kobaran api biru plasma di kedua tangan", Color(0xFF2563EB), "ANIME"),
    CHEF_OVEN_MITTS("CHEF_OVEN_MITTS", "Sarung Oven Koki", "Sarung tangan dapur motif kotak merah-putih", Color(0xFFE11D48), "LUCU"),
    PHARAOH_GOLD_BANDS("PHARAOH_GOLD_BANDS", "Gelang Emas Firaun", "Gelang kerajaan Mesir kuno dengan permata biru", Color(0xFFEAB308), "SULTAN"),
    GHOST_SKELETON_HANDS("GHOST_SKELETON_HANDS", "Tangan Tulang Neon", "Motif kerangka menyala dalam gelap", Color(0xFFA855F7), "FANTASI"),
    RACING_PRO_GLOVES("RACING_PRO_GLOVES", "Sarung Tangan Balap", "Grip kulit pembalap F1 merah-hitam sporty", Color(0xFFDC2626), "MODERN"),
    THUNDER_LIGHTNING_FISTS("THUNDER_LIGHTNING_FISTS", "Tinju Petir Zeus", "Aliran listrik petir kuning menyambar di tangan", Color(0xFFFACC15), "FANTASI"),
    SILVER_PALADIN_GAUNTLETS("SILVER_PALADIN_GAUNTLETS", "Gauntlet Ksatria Perak", "Baja paladin perak mengilap berukir salib", Color(0xFF94A3B8), "FANTASI"),
    DJ_NEON_EQUALIZER("DJ_NEON_EQUALIZER", "Sarung Tangan DJ", "Lampu LED equalizer musik menyala mengikuti beat", Color(0xFF06B6D4), "SIBER"),
    INFINITY_GEM_RINGS("INFINITY_GEM_RINGS", "Gauntlet 5 Permata", "Sarung tangan kosmik dengan 5 batu elemen", Color(0xFFD97706), "SULTAN"),
    // 12 New Complex Hand / Saputangan / Weapon Items (34 Total)
    ROYAL_SILK_HANDKERCHIEF("ROYAL_SILK_HANDKERCHIEF", "Saputangan Sutra Sultan", "Saputangan sutra merah-emas berkibar elegan", Color(0xFFF59E0B), "SULTAN"),
    NOBLE_LACE_HANDKERCHIEF("NOBLE_LACE_HANDKERCHIEF", "Saputangan Renda Bangsawan", "Saputangan bangsawan putih berbordir mawar biru", Color(0xFF38BDF8), "SULTAN"),
    DUAL_PLASMA_SABERS("DUAL_PLASMA_SABERS", "Pedang Kembar Plasma", "Sepasang pedang cahaya sian-magenta di tangan", Color(0xFF06B6D4), "SIBER"),
    PALADIN_SWORD_AND_SHIELD("PALADIN_SWORD_AND_SHIELD", "Pedang & Perisai Aegis", "Tangan kiri perisai emas, tangan kanan pedang suci", Color(0xFFEAB308), "FANTASI"),
    ELEMENTAL_FIRE_ICE_BLADES("ELEMENTAL_FIRE_ICE_BLADES", "Belati Api & Es", "Belati magma merah di kiri & belati es biru di kanan", Color(0xFFEF4444), "FANTASI"),
    ARCANUM_CRYSTAL_SCEPTER("ARCANUM_CRYSTAL_SCEPTER", "Tongkat Sihir & Orb", "Tongkat kristal bintang & bola mana melayang", Color(0xFFA855F7), "FANTASI"),
    MECHA_BUSTER_CANNONS("MECHA_BUSTER_CANNONS", "Meriam Lengan Mecha", "Sepasang meriam blaster plasma robot di lengan", Color(0xFF2563EB), "SIBER"),
    KITSUNE_FOLDING_FAN("KITSUNE_FOLDING_FAN", "Kipas Lipat Kitsune", "Kipas tradisional Jepang bermotif sakura emas", Color(0xFFEC4899), "ANIME"),
    SPIRIT_SHRINE_LANTERN("SPIRIT_SHRINE_LANTERN", "Lentera Roh Kuil", "Lentera terbang bercahaya biru-emas & jimat kertas", Color(0xFF10B981), "ANIME"),
    CYBER_EXOSKELETON_CLAWS("CYBER_EXOSKELETON_CLAWS", "Cakar Exoskeleton", "Jari artikulasi hidrolik siber berujung laser", Color(0xFF14B8A6), "SIBER"),
    SHINOBI_KUNAI_CHAIN("SHINOBI_KUNAI_CHAIN", "Kunai & Rantai Ninja", "Sepasang kunai tempur dengan rantai besi berkilau", Color(0xFF6366F1), "ANIME"),
    GOLDEN_CHEF_SPATULA_PAN("GOLDEN_CHEF_SPATULA_PAN", "Spatula Emas & Wajan", "Peralatan masak master chef emas berkilau", Color(0xFFF97316), "LUCU"),
    // 12 New Anime Collab, Super Hero & Fantasy Weapons / Hands (46 Total)
    NICHIRIN_SUN_KATANA("NICHIRIN_SUN_KATANA", "Katana Nichirin Hinokami", "Pedang hitam-merah Demon Slayer dengan roda api matahari", Color(0xFFEF4444), "ANIME"),
    AOT_ODM_DUAL_BLADES("AOT_ODM_DUAL_BLADES", "Pedang Kembar ODM AoT", "Sepasang pedang baja ultrahard dengan pelatuk kabel gas", Color(0xFF15803D), "ANIME"),
    BLEACH_ZANPAKUTO_BLADE("BLEACH_ZANPAKUTO_BLADE", "Pedang Besar Zangetsu", "Zanpakuto raksasa hitam-perak dengan aura reiatsu merah", Color(0xFFE11D48), "ANIME"),
    NARUTO_RASENGAN_SHURIKEN("NARUTO_RASENGAN_SHURIKEN", "Rasengan & Shuriken Cakra", "Bola spiral Rasengan biru di kanan & Rasenshuriken di kiri", Color(0xFF38BDF8), "ANIME"),
    LINK_CLICK_CAMERA_CLAP("LINK_CLICK_CAMERA_CLAP", "Kamera & Jam Link Click", "Kamera waktu polaroid di kanan & jam saku bercahaya di kiri", Color(0xFFEAB308), "ANIME"),
    SOUL_LAND_HAMMER_TRIDENT("SOUL_LAND_HAMMER_TRIDENT", "Palu Haotian & Trisula", "Trisula Emas Dewa Laut di kanan & Palu Langit Cerah di kiri", Color(0xFF0EA5E9), "ANIME"),
    BTTH_FLAME_LOTUS_RULER("BTTH_FLAME_LOTUS_RULER", "Teratai Api & Penggaris Xuan", "Teratai Api Buddha 3 warna & pedang Heavy Xuan Ruler hitam", Color(0xFF9333EA), "ANIME"),
    HERO_REPULSOR_GAUNTLETS("HERO_REPULSOR_GAUNTLETS", "Gauntlet Repulsor Nano", "Tangan baja merah-emas menembakkan inti plasma biru", Color(0xFFDC2626), "HERO"),
    HERO_WEB_SHOOTERS("HERO_WEB_SHOOTERS", "Jaring Laba-Laba Hero", "Sarung tangan merah-biru menembakkan jaring energi perak", Color(0xFF2563EB), "HERO"),
    HERO_MJOLNIR_AND_SHIELD("HERO_MJOLNIR_AND_SHIELD", "Palu Petir & Perisai Hero", "Palu petir Asgard di kanan & perisai bintang vibranium di kiri", Color(0xFF0284C7), "HERO"),
    FANTASY_ELVEN_LIGHT_BOW("FANTASY_ELVEN_LIGHT_BOW", "Busur Cahaya Elf", "Busur emas zamrud dengan anak panah bintang bercahaya", Color(0xFF10B981), "FANTASI"),
    FANTASY_NECRO_SCYTHE("FANTASY_NECRO_SCYTHE", "Sabit Jiwa Necromancer", "Sabit kematian berapi ungu & lentera roh tengkorak", Color(0xFFA855F7), "FANTASI");

    companion object {
        fun fromId(id: String?): MascotHandGloves {
            return entries.find { it.id.equals(id?.trim(), ignoreCase = true) } ?: DEFAULT_HANDS
        }
    }
}

enum class MascotRoomTheme(
    val id: String,
    val displayName: String,
    val subtitle: String,
    val wallTopColor: Color,
    val wallBottomColor: Color,
    val floorColor: Color,
    val trimColor: Color,
    val accentColor: Color
) {
    CYBER_GAMER_LOFT(
        id = "CYBER_GAMER_LOFT",
        displayName = "Studio Gamer Rainbow",
        subtitle = "Meja PC 3 monitor RGB, lampu neon sian-pink, sofa gaming & naga cyber",
        wallTopColor = Color(0xFF3B82F6),
        wallBottomColor = Color(0xFF6366F1),
        floorColor = Color(0xFF0EA5E9),
        trimColor = Color(0xFF00F2FE),
        accentColor = Color(0xFFEC4899)
    ),
    SULTAN_PENTHOUSE(
        id = "SULTAN_PENTHOUSE",
        displayName = "Istana Sultan Emas",
        subtitle = "Lemari jati emas 24K, sofa beludru merah, marmer & kucing hoki",
        wallTopColor = Color(0xFFF59E0B),
        wallBottomColor = Color(0xFFD97706),
        floorColor = Color(0xFFFEF08A),
        trimColor = Color(0xFFEF4444),
        accentColor = Color(0xFFFACC15)
    ),
    COSMIC_MAGE_TOWER(
        id = "COSMIC_MAGE_TOWER",
        displayName = "Menara Sihir Pelangi",
        subtitle = "Meja alkemis potion warna-warni, jendela galaksi & burung phoenix",
        wallTopColor = Color(0xFF8B5CF6),
        wallBottomColor = Color(0xFF7C3AED),
        floorColor = Color(0xFFC084FC),
        trimColor = Color(0xFFFACC15),
        accentColor = Color(0xFF38BDF8)
    ),
    SAKURA_KAWAII_ROOM(
        id = "SAKURA_KAWAII_ROOM",
        displayName = "Kamar Pastel Sakura",
        subtitle = "Sofa kucing empuk, meja kafe boba, pohon sakura & kelinci bulan",
        wallTopColor = Color(0xFFF472B6),
        wallBottomColor = Color(0xFFFB7185),
        floorColor = Color(0xFFFBCFE8),
        trimColor = Color(0xFFFACC15),
        accentColor = Color(0xFFEC4899)
    ),
    SAMURAI_DOJO(
        id = "SAMURAI_DOJO",
        displayName = "Dojo Kayu & Taman Zen",
        subtitle = "Rak katana emas, meja kotatsu hangat, tatami hijau & rubah kitsune",
        wallTopColor = Color(0xFFEF4444),
        wallBottomColor = Color(0xFFD97706),
        floorColor = Color(0xFF84CC16),
        trimColor = Color(0xFFFACC15),
        accentColor = Color(0xFFDC2626)
    ),
    ORBITAL_SPACE_CABIN(
        id = "ORBITAL_SPACE_CABIN",
        displayName = "Kabin Akuarium & Taman",
        subtitle = "Akuarium koral tropis, kapsul sian cerah, rumput zamrud & orb AI",
        wallTopColor = Color(0xFF06B6D4),
        wallBottomColor = Color(0xFF10B981),
        floorColor = Color(0xFF34D399),
        trimColor = Color(0xFFFACC15),
        accentColor = Color(0xFF22C55E)
    );

    companion object {
        fun fromId(id: String?): MascotRoomTheme {
            return entries.find { it.id.equals(id?.trim(), ignoreCase = true) } ?: CYBER_GAMER_LOFT
        }
    }
}

enum class MascotCostumePreset(
    val id: String,
    val displayName: String,
    val subtitle: String,
    val skin: MascotBodySkin,
    val outfit: MascotUpperOutfit,
    val gloves: MascotHandGloves,
    val accessory: MascotAccessory,
    val accentColor: Color
) {
    SULTAN_EMPEROR(
        id = "SULTAN_EMPEROR",
        displayName = "Sultan Raja Emas",
        subtitle = "Damaskus Emas • Hanfu Kaisar • Saputangan Sutra • Mahkota Naga",
        skin = MascotBodySkin.ROYAL_DAMASCUS_GOLD,
        outfit = MascotUpperOutfit.ROYAL_EMPEROR_HANFU,
        gloves = MascotHandGloves.ROYAL_SILK_HANDKERCHIEF,
        accessory = MascotAccessory.EMPEROR_DRAGON_CROWN,
        accentColor = Color(0xFFF59E0B)
    ),
    CYBER_NETRUNNER(
        id = "CYBER_NETRUNNER",
        displayName = "Cyber Edgerunner",
        subtitle = "Sirkuit Neon • Jaket Edgerunner • Pedang Plasma • VR Visor",
        skin = MascotBodySkin.CYBERPUNK_NEON_CIRCUIT,
        outfit = MascotUpperOutfit.CYBERPUNK_EDGERUNNER_JACKET,
        gloves = MascotHandGloves.DUAL_PLASMA_SABERS,
        accessory = MascotAccessory.CYBER_VR_VISOR,
        accentColor = Color(0xFF06B6D4)
    ),
    MECHA_PALADIN(
        id = "MECHA_PALADIN",
        displayName = "Gundam Strike Commander",
        subtitle = "Komposit Mecha • Zirah Strike • Meriam Buster • Helm V-Fin",
        skin = MascotBodySkin.GUNDAM_AERO_COMPOSITE,
        outfit = MascotUpperOutfit.GUNDAM_STRIKE_EXOSUIT,
        gloves = MascotHandGloves.MECHA_BUSTER_CANNONS,
        accessory = MascotAccessory.GUNDAM_VFIN_HELMET,
        accentColor = Color(0xFF3B82F6)
    ),
    COSMIC_ARCHMAGE(
        id = "COSMIC_ARCHMAGE",
        displayName = "Penyihir Kosmik Agung",
        subtitle = "Rasi Bintang • Jubah Rune • Tongkat & Orb • Orbit 5 Elemen",
        skin = MascotBodySkin.ASTRAL_CONSTELLATION_MAP,
        outfit = MascotUpperOutfit.ARCANUM_MAGE_ROBE,
        gloves = MascotHandGloves.ARCANUM_CRYSTAL_SCEPTER,
        accessory = MascotAccessory.ELEMENTAL_ORBS_CROWN,
        accentColor = Color(0xFF8B5CF6)
    ),
    DRAGON_OVERLORD(
        id = "DRAGON_OVERLORD",
        displayName = "Kaisar Naga Api",
        subtitle = "Sisik Naga Merah • Zirah Shogun • Belati Api & Es • Tanduk",
        skin = MascotBodySkin.DRAGON_SCALE_RUBY,
        outfit = MascotUpperOutfit.SHOGUN_YOROI_CUIRASS,
        gloves = MascotHandGloves.ELEMENTAL_FIRE_ICE_BLADES,
        accessory = MascotAccessory.ABYSSAL_DEMON_LORD_HORNS,
        accentColor = Color(0xFFEF4444)
    ),
    KAWAII_IDOL(
        id = "KAWAII_IDOL",
        displayName = "Kawaii Star Idol",
        subtitle = "Boneka Rajut • Kostum Idol • Kipas Kitsune • Telinga Kucing",
        skin = MascotBodySkin.PLUSH_VELVET_QUILT,
        outfit = MascotUpperOutfit.MAGICAL_IDOL_FRILL_DRESS,
        gloves = MascotHandGloves.KITSUNE_FOLDING_FAN,
        accessory = MascotAccessory.CAT_EARS_NEKO,
        accentColor = Color(0xFFEC4899)
    ),
    SECRET_AGENT_007(
        id = "SECRET_AGENT_007",
        displayName = "Bangsawan Eksekutif",
        subtitle = "Obsidian Gelap • Jas Tuksedo • Saputangan Renda • Monokel",
        skin = MascotBodySkin.OBSIDIAN_STEALTH,
        outfit = MascotUpperOutfit.EXECUTIVE_TUXEDO,
        gloves = MascotHandGloves.NOBLE_LACE_HANDKERCHIEF,
        accessory = MascotAccessory.CYBER_MONOCLE,
        accentColor = Color(0xFF1E293B)
    ),
    STARLIGHT_ANGEL(
        id = "STARLIGHT_ANGEL",
        displayName = "Seraphim Malaikat Agung",
        subtitle = "Bulu Surga • Zirah Malaikat • Pedang & Perisai • 6 Sayap",
        skin = MascotBodySkin.CELESTIAL_SERAPH_FEATHER,
        outfit = MascotUpperOutfit.ARCHANGEL_SERAPH_ARMOR,
        gloves = MascotHandGloves.PALADIN_SWORD_AND_SHIELD,
        accessory = MascotAccessory.SERAPHIM_SIX_WINGS,
        accentColor = Color(0xFF0EA5E9)
    ),
    AKATSUKI_SHINOBI(
        id = "AKATSUKI_SHINOBI",
        displayName = "Shinobi Kitsune",
        subtitle = "Bayangan Void • Jubah Awan Merah • Kunai Rantai • Topeng Kitsune",
        skin = MascotBodySkin.VOID_SHADOW,
        outfit = MascotUpperOutfit.RED_CLOUD_CLOAK,
        gloves = MascotHandGloves.SHINOBI_KUNAI_CHAIN,
        accessory = MascotAccessory.KITSUNE_SPIRIT_MASK,
        accentColor = Color(0xFFE11D48)
    ),
    HYPEBEAST_DJ(
        id = "HYPEBEAST_DJ",
        displayName = "Techwear DJ RGB",
        subtitle = "Pelangi RGB • Parka Techwear • Tangan DJ • Headphone",
        skin = MascotBodySkin.RAINBOW_PRISM_RGB,
        outfit = MascotUpperOutfit.STREETWEAR_TECH_PARKA,
        gloves = MascotHandGloves.DJ_NEON_EQUALIZER,
        accessory = MascotAccessory.CYBER_HEADPHONES,
        accentColor = Color(0xFFEC4899)
    ),
    GUNDAM_PILOT(
        id = "GUNDAM_PILOT",
        displayName = "Kapten Armada Langit",
        subtitle = "Kevlar Taktis • Seragam Pilot • Cakar Exoskeleton • Jetpack",
        skin = MascotBodySkin.CARBON_KEVLAR,
        outfit = MascotUpperOutfit.PILOT_CAPTAIN_UNIFORM,
        gloves = MascotHandGloves.CYBER_EXOSKELETON_CLAWS,
        accessory = MascotAccessory.CYBER_JETPACK_THRUSTERS,
        accentColor = Color(0xFF2563EB)
    ),
    DINO_CUTE_CHEF(
        id = "DINO_CUTE_CHEF",
        displayName = "Master Koki Bintang 5",
        subtitle = "Permen Kapas • Baju Koki • Spatula Emas • Topi Toque",
        skin = MascotBodySkin.COTTON_CANDY,
        outfit = MascotUpperOutfit.MASTER_CHEF_APRON,
        gloves = MascotHandGloves.GOLDEN_CHEF_SPATULA_PAN,
        accessory = MascotAccessory.CHEF_TOQUE_CROWN,
        accentColor = Color(0xFF84CC16)
    ),
    // 14 New Anime Collab, Super Hero & High-Fantasy 4-in-1 Costume Sets (26 Total)
    COLLAB_DEMON_SLAYER_TANJIRO(
        id = "COLLAB_DEMON_SLAYER_TANJIRO",
        displayName = "Demon Slayer: Hinokami Tanjiro",
        subtitle = "Haori Kotak Hijau • Katana Nichirin Api • Anting Hanafuda",
        skin = MascotBodySkin.SUN_BREATHING_SCAR_SKIN,
        outfit = MascotUpperOutfit.DEMON_SLAYER_CHECKERED_HAORI,
        gloves = MascotHandGloves.NICHIRIN_SUN_KATANA,
        accessory = MascotAccessory.HANAFUDA_SUN_EARRINGS,
        accentColor = Color(0xFF10B981)
    ),
    COLLAB_FLAME_HASHIRA_RENGOKU(
        id = "COLLAB_FLAME_HASHIRA_RENGOKU",
        displayName = "Demon Slayer: Pilar Api Rengoku",
        subtitle = "Mantel Kobaran Api • Katana Nichirin • Cahaya Hinokami",
        skin = MascotBodySkin.SOLAR_SUNBURST,
        outfit = MascotUpperOutfit.FLAME_HASHIRA_CAPE,
        gloves = MascotHandGloves.NICHIRIN_SUN_KATANA,
        accessory = MascotAccessory.HANAFUDA_SUN_EARRINGS,
        accentColor = Color(0xFFEF4444)
    ),
    COLLAB_AOT_SURVEY_CORPS(
        id = "COLLAB_AOT_SURVEY_CORPS",
        displayName = "Attack on Titan: Pasukan Pengintai",
        subtitle = "Jubah Sayap Kebebasan • Pedang Kembar ODM • Tudung & Goggle",
        skin = MascotBodySkin.TITAN_STEAM_MUSCLE_SKIN,
        outfit = MascotUpperOutfit.AOT_SURVEY_CORPS_CLOAK,
        gloves = MascotHandGloves.AOT_ODM_DUAL_BLADES,
        accessory = MascotAccessory.AOT_SCOUT_HOOD_GOGGLES,
        accentColor = Color(0xFF15803D)
    ),
    COLLAB_BLEACH_BANKAI_CAPTAIN(
        id = "COLLAB_BLEACH_BANKAI_CAPTAIN",
        displayName = "Bleach: Kapten Bankai Hollow",
        subtitle = "Haori Kapten & Shihakusho • Pedang Zangetsu • Topeng Hollow",
        skin = MascotBodySkin.HOLLOW_SPIRIT_REIATSU,
        outfit = MascotUpperOutfit.BLEACH_BANKAI_SHIHAKUSHO,
        gloves = MascotHandGloves.BLEACH_ZANPAKUTO_BLADE,
        accessory = MascotAccessory.BLEACH_HOLLOW_MASK,
        accentColor = Color(0xFFE11D48)
    ),
    COLLAB_NARUTO_HOKAGE_SAGE(
        id = "COLLAB_NARUTO_HOKAGE_SAGE",
        displayName = "Naruto: Hokage Mode Rikudou",
        subtitle = "Chakra Kurama • Jubah Hokage Api • Rasengan • Tanduk Rikudou",
        skin = MascotBodySkin.NINE_TAILS_CHAKRA_CLOAK,
        outfit = MascotUpperOutfit.NARUTO_HOKAGE_SAGE_CLOAK,
        gloves = MascotHandGloves.NARUTO_RASENGAN_SHURIKEN,
        accessory = MascotAccessory.NARUTO_SHINOBI_HEADBAND,
        accentColor = Color(0xFFF97316)
    ),
    COLLAB_NARUTO_UCHIHA_SUSANOO(
        id = "COLLAB_NARUTO_UCHIHA_SUSANOO",
        displayName = "Naruto: Ksatria Klan Uchiha",
        subtitle = "Zirah Tempur Uchiha • Kipas & Api Biru • Topeng Kitsune",
        skin = MascotBodySkin.SAMURAI_LACQUER_ARMOR,
        outfit = MascotUpperOutfit.UCHIHA_SUSANOO_ARMOR,
        gloves = MascotHandGloves.BLUE_FLAME_HANDS,
        accessory = MascotAccessory.NARUTO_SHINOBI_HEADBAND,
        accentColor = Color(0xFFDC2626)
    ),
    COLLAB_LINK_CLICK_TIME_AGENT(
        id = "COLLAB_LINK_CLICK_TIME_AGENT",
        displayName = "Link Click: Penjelajah Foto Waktu",
        subtitle = "Jaket Hitam-Putih Studio • Kamera & Jam • Lensa Mata Emas",
        skin = MascotBodySkin.TIME_CHRONO_GRID_SKIN,
        outfit = MascotUpperOutfit.LINK_CLICK_TIME_JACKET,
        gloves = MascotHandGloves.LINK_CLICK_CAMERA_CLAP,
        accessory = MascotAccessory.LINK_CLICK_GOLDEN_EYES_GEAR,
        accentColor = Color(0xFF0284C7)
    ),
    COLLAB_SOUL_LAND_TANG_SAN(
        id = "COLLAB_SOUL_LAND_TANG_SAN",
        displayName = "Soul Land: Dewa Laut Tang San",
        subtitle = "Zirah Poseidon 8 Tombak • Palu Haotian & Trisula • Cincin Roh",
        skin = MascotBodySkin.SOUL_RING_DIVINE_GOLD,
        outfit = MascotUpperOutfit.SOUL_LAND_SEA_GOD_ARMOR,
        gloves = MascotHandGloves.SOUL_LAND_HAMMER_TRIDENT,
        accessory = MascotAccessory.SOUL_LAND_SPIRIT_RINGS,
        accentColor = Color(0xFF0EA5E9)
    ),
    COLLAB_BTTH_FLAME_EMPEROR(
        id = "COLLAB_BTTH_FLAME_EMPEROR",
        displayName = "BTTH: Kaisar Api Xiao Yan",
        subtitle = "Jubah Yan Di • Teratai Api & Penggaris Xuan • Segel Kaisar Api",
        skin = MascotBodySkin.HEAVENLY_FLAME_LOTUS_SKIN,
        outfit = MascotUpperOutfit.BTTH_FLAME_EMPEROR_ROBE,
        gloves = MascotHandGloves.BTTH_FLAME_LOTUS_RULER,
        accessory = MascotAccessory.BTTH_EMPEROR_FLAME_CREST,
        accentColor = Color(0xFF9333EA)
    ),
    HERO_IRON_NANO_AVENGER(
        id = "HERO_IRON_NANO_AVENGER",
        displayName = "Super Hero: Iron Nano Avenger",
        subtitle = "Nano-Alloy Merah Emas • Armor Reaktor • Repulsor • Helm HUD",
        skin = MascotBodySkin.NANO_HEX_TITANIUM_RED,
        outfit = MascotUpperOutfit.HERO_NANO_IRON_ARMOR,
        gloves = MascotHandGloves.HERO_REPULSOR_GAUNTLETS,
        accessory = MascotAccessory.HERO_IRON_HUD_HELMET,
        accentColor = Color(0xFFDC2626)
    ),
    HERO_DARK_KNIGHT_GUARDIAN(
        id = "HERO_DARK_KNIGHT_GUARDIAN",
        displayName = "Super Hero: Ksatria Kelelawar",
        subtitle = "Serat Kinetik • Zirah Sayap Kelelawar • Gauntlet • Cowl Malam",
        skin = MascotBodySkin.VIBRANIUM_KINETIC_WEAVE,
        outfit = MascotUpperOutfit.HERO_DARK_KNIGHT_CAPE,
        gloves = MascotHandGloves.CYBER_GAUNTLETS,
        accessory = MascotAccessory.HERO_DARK_BAT_COWL,
        accentColor = Color(0xFF334155)
    ),
    HERO_ASGARD_THUNDER_LORD(
        id = "HERO_ASGARD_THUNDER_LORD",
        displayName = "Super Hero: Dewa Petir Asgard",
        subtitle = "Inti Petir • Zirah Asgard • Palu Mjolnir & Perisai • Sayap",
        skin = MascotBodySkin.THUNDER_PLASMA_COIL,
        outfit = MascotUpperOutfit.HERO_THUNDER_ASGARD_PLATE,
        gloves = MascotHandGloves.HERO_MJOLNIR_AND_SHIELD,
        accessory = MascotAccessory.FANTASY_VALKYRIE_WING_HELM,
        accentColor = Color(0xFF0284C7)
    ),
    FANTASY_ELVEN_FOREST_KING(
        id = "FANTASY_ELVEN_FOREST_KING",
        displayName = "Fantasi: Pangeran Pemanah Elf",
        subtitle = "Rune Cahaya Elf • Mantel Hutan Zamrud • Busur Cahaya • Tiara",
        skin = MascotBodySkin.ELVEN_STARLIGHT_RUNE,
        outfit = MascotUpperOutfit.FANTASY_ELVEN_RANGER_CLOAK,
        gloves = MascotHandGloves.FANTASY_ELVEN_LIGHT_BOW,
        accessory = MascotAccessory.FAIRY_FLOWER_CROWN,
        accentColor = Color(0xFF10B981)
    ),
    FANTASY_ABYSSAL_LICH_MONARCH(
        id = "FANTASY_ABYSSAL_LICH_MONARCH",
        displayName = "Fantasi: Raja Lich Necromancer",
        subtitle = "Zirah Abyss • Jubah Koyak Lich • Sabit Jiwa • Mahkota Jiwa",
        skin = MascotBodySkin.ABYSSAL_DEMON_CHITIN,
        outfit = MascotUpperOutfit.FANTASY_NECROMANCER_LICH_ROBE,
        gloves = MascotHandGloves.FANTASY_NECRO_SCYTHE,
        accessory = MascotAccessory.FANTASY_LICH_SOUL_CROWN,
        accentColor = Color(0xFF7C3AED)
    )
}

enum class MascotAuraStyle(
    val id: String,
    val displayName: String,
    val subtitle: String,
    val primaryColor: Color,
    val secondaryColor: Color,
    val themeCategory: String = "SEMUA"
) {
    NONE("NONE", "Tanpa Aura", "Hanya cahaya alami tubuh tanpa cincin aura", Color(0xFF64748B), Color(0xFF475569), "DEFAULT"),
    COSMIC_OCTAGON("COSMIC_OCTAGON", "Orbit Oktagon Kosmik", "Geometri suci oktagon & cincin plasma berputar", Color(0xFF8B5CF6), Color(0xFF06B6D4), "FANTASI"),
    SUPER_SAIYAN_GOLD("SUPER_SAIYAN_GOLD", "Super Aura Emas Kilat", "Kobaran energi emas menjulang & kilat biru", Color(0xFFFACC15), Color(0xFF38BDF8), "ANIME"),
    DRAGON_INFERNO("DRAGON_INFERNO", "Api Naga Membara", "Lidah api merah-emas berputar & bara terbang", Color(0xFFEF4444), Color(0xFFF97316), "FANTASI"),
    SHADOW_MONARCH("SHADOW_MONARCH", "Raja Bayangan Ungu", "Kabut energi gelap & segel rune nekromansi", Color(0xFF7C3AED), Color(0xFF311068), "ANIME"),
    CYBER_MATRIX_HEX("CYBER_MATRIX_HEX", "Hologram Matriks Siber", "Perisai heksagonal neon & kode digital hijau", Color(0xFF10B981), Color(0xFF06B6D4), "SIBER"),
    SAKURA_BLOSSOM_STORM("SAKURA_BLOSSOM_STORM", "Badai Kelopak Sakura", "Pusaran kelopak sakura pink & kilau peri", Color(0xFFEC4899), Color(0xFFF9A8D4), "LUCU"),
    THUNDER_GOD_ZEUS("THUNDER_GOD_ZEUS", "Petir Dewa Zeus", "Sambaran petir elektrik 360° & bola plasma", Color(0xFFEAB308), Color(0xFF38BDF8), "FANTASI"),
    FROST_BLIZZARD_ICE("FROST_BLIZZARD_ICE", "Badai Kristal Es", "Kristal salju berputar & kabut berlian biru", Color(0xFF38BDF8), Color(0xFFE0F2FE), "FANTASI"),
    SULTAN_WEALTH_RAIN("SULTAN_WEALTH_RAIN", "Hujan Emas & Berlian", "Koin emas 24K, permata & mahkota melayang", Color(0xFFF59E0B), Color(0xFF10B981), "SULTAN"),
    CELESTIAL_ANGEL_LIGHT("CELESTIAL_ANGEL_LIGHT", "Cahaya Suci Malaikat", "Sinar surga keemasan & bulu cahaya putih", Color(0xFFFDE047), Color(0xFF7DD3FC), "FANTASI"),
    VOID_BLACK_HOLE("VOID_BLACK_HOLE", "Singularity Void", "Cakram akresi lubang hitam & distorsi ruang", Color(0xFF9333EA), Color(0xFFF97316), "FANTASI"),
    RAINBOW_RGB_GAMER("RAINBOW_RGB_GAMER", "Spektrum RGB Gamer", "Gelombang neon pelangi equalizer berputar", Color(0xFFEC4899), Color(0xFF22D3EE), "SIBER"),
    ARCANE_RUNE_CIRCLE("ARCANE_RUNE_CIRCLE", "Segel Sihir Agung", "Lingkaran bintang heksagram & orb mana", Color(0xFFA855F7), Color(0xFFFACC15), "FANTASI"),
    LOVE_HEART_SYMPHONY("LOVE_HEART_SYMPHONY", "Simfoni Hati Kasih", "Orbit hati 3D berlapis & pita merah muda", Color(0xFFF43F5E), Color(0xFFF472B6), "LUCU"),
    EMERALD_FOREST_SPIRIT("EMERALD_FOREST_SPIRIT", "Roh Hutan Zamrud", "Daun zamrud bercahaya & kunang-kunang emas", Color(0xFF22C55E), Color(0xFFFACC15), "FANTASI"),
    // 6 New Anime Collab & Super Hero Auras (22 Total)
    WATER_SUN_BREATHING("WATER_SUN_BREATHING", "Napas Air & Hinokami", "Pusaran naga air biru berpadu roda api matahari", Color(0xFFEF4444), Color(0xFF0EA5E9), "ANIME"),
    SOUL_RINGS_ASCENSION("SOUL_RINGS_ASCENSION", "9 Cincin Roh Douluo", "Cincin roh 100.000 tahun merah-emas berputar megah", Color(0xFF0284C7), Color(0xFFFACC15), "ANIME"),
    HEAVENLY_FLAME_LOTUS("HEAVENLY_FLAME_LOTUS", "Teratai Api Surgawi BTTH", "Kelopak api giok-ungu-emas & petir tiga ribu", Color(0xFF10B981), Color(0xFFA855F7), "ANIME"),
    BANKAI_SPIRIT_PRESSURE("BANKAI_SPIRIT_PRESSURE", "Reiatsu Bankai Hitam-Merah", "Pilar energi spiritual hitam-merah & kilat Getsuga", Color(0xFFE11D48), Color(0xFF111827), "ANIME"),
    TIME_DIVE_CHRONO_RIFT("TIME_DIVE_CHRONO_RIFT", "Dimensi Waktu Link Click", "Roda gigi jam emas mundur & gelombang foto sian", Color(0xFF0284C7), Color(0xFFFACC15), "ANIME"),
    HERO_INFINITY_COSMIC("HERO_INFINITY_COSMIC", "Reaktor Nano Superhero", "Perisai energi heksagonal merah-emas & busur plasma", Color(0xFFDC2626), Color(0xFF38BDF8), "HERO");

    companion object {
        fun fromId(id: String?): MascotAuraStyle {
            return entries.find { it.id.equals(id?.trim(), ignoreCase = true) } ?: COSMIC_OCTAGON
        }
    }
}

enum class MascotEmotionPreset(
    val id: String,
    val displayName: String,
    val subtitle: String,
    val badgeColor: Color
) {
    AUTO_DYNAMIC("AUTO_DYNAMIC", "Otomatis Hidup", "Ekspresi berganti dinamis mengikuti situasi", Color(0xFF58CC02)),
    HAPPY_STAR_EYES("HAPPY_STAR_EYES", "Mata Bintang", "Pupil bintang emas berkilau & tawa bahagia", Color(0xFFFACC15)),
    LOVE_BUCIN("LOVE_BUCIN", "Jatuh Cinta", "Mata hati pink berdenyut & pipi merona", Color(0xFFEC4899)),
    LAUGHING_TEARS("LAUGHING_TEARS", "Ngakak Puas", "Mata menyipit > < keluar air mata bahagia", Color(0xFFFB923C)),
    COOL_BOSS_SMIRK("COOL_BOSS_SMIRK", "Tengil Bossy", "Tatapan cool setengah terpejam & senyum miring", Color(0xFF0EA5E9)),
    CRYING_FOUNTAIN("CRYING_FOUNTAIN", "Nangis Ngejer", "Air mata menyembur deras seperti air mancur", Color(0xFF38BDF8)),
    RAGE_INFERNO("RAGE_INFERNO", "Marah Meledak", "Mata bara api, alis tajam, taring & uap", Color(0xFFEF4444)),
    SHOCKED_JOLT("SHOCKED_JOLT", "Kaget Melotot", "Pupil mengecil kaget & mulut menganga lebar", Color(0xFFEAB308)),
    SHY_BLUSH("SHY_BLUSH", "Malu Kucing :3", "Garis rona pipi tebal & mulut kucing imut", Color(0xFFF472B6)),
    SLEEPY_SNOT_BUBBLE("SLEEPY_SNOT_BUBBLE", "Ngantuk Gelembung", "Mata terpejam dengan balon tidur & Zzz", Color(0xFF8B5CF6)),
    DIZZY_SPIRAL("DIZZY_SPIRAL", "Pusing Spiral", "Mata pusaran @ @ berputar & bintang pusing", Color(0xFFA855F7)),
    MONEY_SULTAN("MONEY_SULTAN", "Mata Duitan $", "Pupil lambang dolar emas $ berkilau", Color(0xFFF59E0B)),
    SUSPICIOUS_SQUINT("SUSPICIOUS_SQUINT", "Curiga Selidik", "Mata menyipit tajam -_- & alis terangkat", Color(0xFF64748B)),
    MIND_BLOWN_COSMIC("MIND_BLOWN_COSMIC", "Takjub Kosmik", "Mata galaksi berbinar & kembang api pikiran", Color(0xFF06B6D4)),
    ANGELIC_PUPPY("ANGELIC_PUPPY", "Polos Memelas", "Mata bulat besar berbinar 4 titik cahaya", Color(0xFF60A5FA)),
    DEVILISH_PRANK("DEVILISH_PRANK", "Jahil Licik", "Senyum menyeringai bertaring & alis nakal", Color(0xFFDC2626)),
    FREEZING_SHIVER("FREEZING_SHIVER", "Menggigil Beku", "Wajah biru dingin, gigi gemeretak & es", Color(0xFF0284C7)),
    FEVER_OVERHEAT("FEVER_OVERHEAT", "Demam Overheat", "Pipi merah panas, termometer & uap mengepul", Color(0xFFF43F5E)),
    CYBER_HUD_FOCUS("CYBER_HUD_FOCUS", "Fokus Radar HUD", "Bidikan target sian di mata & mulut serius", Color(0xFF10B981)),
    YUMMY_DROOL("YUMMY_DROOL", "Lahap Nikmat", "Pipi gembul mengunyah & bintang kelezatan", Color(0xFF84CC16));

    companion object {
        fun fromId(id: String?): MascotEmotionPreset {
            return entries.find { it.id.equals(id?.trim(), ignoreCase = true) } ?: AUTO_DYNAMIC
        }
    }
}

// =========================================================================
// 4. 9-PART MODULAR ROOM FURNITURE & DECORATION ENUMS (15 VARIATIONS EACH)
// =========================================================================

enum class MascotRoomClosetStyle(
    val id: String,
    val displayName: String,
    val subtitle: String,
    val bodyColor: Color,
    val trimColor: Color
) {
    CYBER_NEON_CLOSET("CYBER_NEON_CLOSET", "Lemari Kaca RGB", "Lemari biru elektrik dengan etalase baju warna-warni & neon", Color(0xFF2563EB), Color(0xFF00F2FE)),
    SULTAN_GOLD_ARMOIRE("SULTAN_GOLD_ARMOIRE", "Lemari Ukiran Emas", "Kayu jati madu berlapis ukiran emas 24K & permata ruby", Color(0xFFD97706), Color(0xFFFACC15)),
    SAKURA_KAWAII_WARDROBE("SAKURA_KAWAII_WARDROBE", "Lemari Pink Kucing", "Lemari berry pink ceria dengan atap telinga kucing & pita", Color(0xFFEC4899), Color(0xFFFDE047)),
    MECHA_HANGAR_LOCKER("MECHA_HANGAR_LOCKER", "Loker Zirah Gundam", "Dock armor biru-merah-kuning ala hangar robot tempur", Color(0xFF1D4ED8), Color(0xFFEF4444)),
    ARCANE_GRIMOIRE_CABINET("ARCANE_GRIMOIRE_CABINET", "Lemari Kitab Sihir", "Kayu ungu mistis penuh botol potion warna-warni & kristal", Color(0xFF7C3AED), Color(0xFFFACC15)),
    SAMURAI_SHOJI_CLOSET("SAMURAI_SHOJI_CLOSET", "Lemari Kayu Shoji", "Kayu merah vermilion Jepang dengan pintu shoji & emas", Color(0xFFDC2626), Color(0xFFFBBF24)),
    ORBITAL_CAPSULE_POD("ORBITAL_CAPSULE_POD", "Kapsul Kostum Toska", "Tabung kapsul toska cerah dengan cincin lampu zamrud", Color(0xFF0891B2), Color(0xFF4ADE80)),
    MINIMALIST_GLASS_CLOSET("MINIMALIST_GLASS_CLOSET", "Lemari Kayu Nordik", "Kayu maple hangat skandinavia dengan laci putih bersih", Color(0xFFF59E0B), Color(0xFF38BDF8)),
    ROYAL_VAULT_SAFE("ROYAL_VAULT_SAFE", "Brankas Emas Sultan", "Brankas emas murni berkilau dengan roda pemutar berlian", Color(0xFFEAB308), Color(0xFFEF4444)),
    GUNDAM_ARMOR_RACK("GUNDAM_ARMOR_RACK", "Etalase Mecha Biru", "Lemari komposit langit dengan gantungan senjata neon", Color(0xFF0284C7), Color(0xFFFACC15)),
    CRYSTAL_ICE_WARDROBE("CRYSTAL_ICE_WARDROBE", "Lemari Kristal Es", "Pahat es biru cerah berkilau dengan mahkota salju", Color(0xFF38BDF8), Color(0xFFE0F2FE)),
    DRAGON_EMPEROR_SHRINE("DRAGON_EMPEROR_SHRINE", "Lemari Kuil Naga", "Merah menyala kekaisaran dengan pilar naga emas 3D", Color(0xFFEF4444), Color(0xFFFDE047)),
    STEAMPUNK_GEAR_CABINET("STEAMPUNK_GEAR_CABINET", "Kabinet Roda Gigi", "Tembaga oranye terang dengan roda gigi emas berputar", Color(0xFFEA580C), Color(0xFFFACC15)),
    HOLOGRAM_QUANTUM_CUBE("HOLOGRAM_QUANTUM_CUBE", "Kubus Zamrud Neon", "Lemari kristal hijau Duolingo dengan cahaya matriks", Color(0xFF16A34A), Color(0xFF6EE7B7)),
    FAIRY_TREE_HOLLOW("FAIRY_TREE_HOLLOW", "Pohon Rumah Peri", "Batang pohon cokelat-hijau dengan atap daun & jamur merah", Color(0xFF65A30D), Color(0xFFF43F5E))
}

enum class MascotRoomFloorStyle(
    val id: String,
    val displayName: String,
    val subtitle: String,
    val primaryColor: Color,
    val gridLineColor: Color
) {
    CYBER_HEX_GRID("CYBER_HEX_GRID", "Ubin Biru Neon 3D", "Lantai ubin biru cerah dengan garis neon sian berkilau", Color(0xFF1D4ED8), Color(0xFF00F2FE)),
    ROYAL_MARBLE_GOLD("ROYAL_MARBLE_GOLD", "Marmer Krem & Emas", "Batu pualam gading istana mengilap dengan nat emas 24K", Color(0xFFFEF3C7), Color(0xFFF59E0B)),
    WARM_OAK_PARQUET("WARM_OAK_PARQUET", "Parket Kayu Madu", "Papan kayu jati madu hangat bertekstur serat kayu 3D", Color(0xFFD97706), Color(0xFF92400E)),
    SAMURAI_TATAMI_MAT("SAMURAI_TATAMI_MAT", "Tikar Tatami Hijau", "Anyaman tatami hijau apel segar dengan pita tepi merah", Color(0xFF65A30D), Color(0xFFEF4444)),
    COSMIC_STAR_GLASS("COSMIC_STAR_GLASS", "Kaca Ungu Bintang", "Lantai kristal ungu lavender dengan kilau rasi bintang", Color(0xFF6D28D9), Color(0xFFFACC15)),
    SAKURA_PINK_CARPET("SAKURA_PINK_CARPET", "Ubin Berry Sakura", "Lantai pastel pink strawberry dengan garis putih bersih", Color(0xFFF472B6), Color(0xFFFCE7F3)),
    MECHA_STEEL_DECK("MECHA_STEEL_DECK", "Dek Biru Baja Hangar", "Pelat baja biru langit dengan strip kuning-oranye", Color(0xFF0284C7), Color(0xFFFACC15)),
    FAIRY_GARDEN_GRASS("FAIRY_GARDEN_GRASS", "Rumput Hijau Duolingo", "Hamparan rumput hijau segar bertabur bunga warna-warni", Color(0xFF58CC02), Color(0xFFA3E635)),
    AQUARIUM_GLASS_FLOOR("AQUARIUM_GLASS_FLOOR", "Kaca Akuarium Toska", "Lantai air laut toska jernih dengan karang warna-warni", Color(0xFF06B6D4), Color(0xFF67E8F9)),
    LAVA_OBSIDIAN_TILES("LAVA_OBSIDIAN_TILES", "Ubin Magma Oranye", "Batu merah bata dengan aliran magma kuning-oranye", Color(0xFFDC2626), Color(0xFFFACC15)),
    BLACK_GOLD_CHECKER("BLACK_GOLD_CHECKER", "Catur Merah & Emas", "Ubin catur istana merah ruby & emas kuning berkilau", Color(0xFFE11D48), Color(0xFFFDE047)),
    RGB_DISCO_DANCEFLOOR("RGB_DISCO_DANCEFLOOR", "Lantai Dansa Pelangi", "Ubin disko magenta-sian-kuning menyala penuh warna", Color(0xFFDB2777), Color(0xFF00F2FE)),
    CLOUD_HEAVEN_MIST("CLOUD_HEAVEN_MIST", "Lantai Awan Biru", "Ubin awan biru langit cerah dengan semburat pelangi", Color(0xFF38BDF8), Color(0xFFFFFFFF)),
    ZEN_SAND_GARDEN("ZEN_SAND_GARDEN", "Pasir Emas Pantai", "Hamparan pasir keemasan hangat dengan gelombang biru", Color(0xFFFBBF24), Color(0xFF0EA5E9)),
    CYBER_MATRIX_CIRCUIT("CYBER_MATRIX_CIRCUIT", "Sirkuit Zamrud Cerah", "Lantai papan sirkuit hijau zamrud dengan jalur emas", Color(0xFF10B981), Color(0xFFFDE047))
}

enum class MascotRoomLampStyle(
    val id: String,
    val displayName: String,
    val subtitle: String,
    val glowColor: Color,
    val fixtureColor: Color
) {
    NEON_RGB_STRIP("NEON_RGB_STRIP", "Lampu Neon Pelangi", "Tabung lampu 3D sian-pink-kuning cerah di plafon", Color(0xFF00F2FE), Color(0xFFEC4899)),
    CRYSTAL_CHANDELIER("CRYSTAL_CHANDELIER", "Chandelier Emas Sultan", "Lampu gantung mahkota emas 3D dengan kristal berlian", Color(0xFFFACC15), Color(0xFFEF4444)),
    FLOATING_MAGIC_CANDLES("FLOATING_MAGIC_CANDLES", "Lilin Sihir Melayang", "Deretan lilin putih-emas 3D yang melayang menyala", Color(0xFFFDE047), Color(0xFF8B5CF6)),
    SAKURA_CLOUD_LAMP("SAKURA_CLOUD_LAMP", "Lampu Awan & Bintang", "Awan 3D gembul bercahaya dengan gantungan bintang emas", Color(0xFFF472B6), Color(0xFFFACC15)),
    JAPANESE_PAPER_LANTERN("JAPANESE_PAPER_LANTERN", "Lampion Merah Emas", "Lampion bundar 3D merah berumbai emas tradisional", Color(0xFFEF4444), Color(0xFFFACC15)),
    ORBITAL_HOLOGRAM_BEAM("ORBITAL_HOLOGRAM_BEAM", "Cincin Surya Toska", "Lampu cincin 3D memancarkan cahaya hijau-sian segar", Color(0xFF10B981), Color(0xFF06B6D4)),
    DISCO_PARTY_LASER("DISCO_PARTY_LASER", "Bola Disko Pelangi", "Bola kaca disko 3D berkilau dengan sorot warna-warni", Color(0xFFEC4899), Color(0xFF38BDF8)),
    STARLIGHT_MOON_PENDANT("STARLIGHT_MOON_PENDANT", "Lampu Bulan Sabit 3D", "Bulan sabit kuning emas 3D dengan bintang gantung", Color(0xFFFACC15), Color(0xFF3B82F6)),
    THUNDER_STORM_CLOUD("THUNDER_STORM_CLOUD", "Awan Petir Elektrik", "Awan biru-ungu 3D dengan kilatan petir kuning cerah", Color(0xFFFDE047), Color(0xFF6366F1)),
    AURORA_BOREALIS_WAVE("AURORA_BOREALIS_WAVE", "Tirai Cahaya Aurora", "Gelombang lampu gantung hijau zamrud & magenta", Color(0xFF4ADE80), Color(0xFFEC4899)),
    DRAGON_FIRE_BRAZIER("DRAGON_FIRE_BRAZIER", "Lentera Naga Emas", "Lampu gantung ukiran naga emas dengan bola api oranye", Color(0xFFF97316), Color(0xFFFACC15)),
    GALAXY_PLANETARIUM_ORB("GALAXY_PLANETARIUM_ORB", "Planet Saturnus 3D", "Lampu bola planet bercincin emas & bintang warna-warni", Color(0xFFC084FC), Color(0xFFFACC15)),
    MECHA_HANGAR_SPOTLIGHT("MECHA_HANGAR_SPOTLIGHT", "Sorot Stadium Biru", "Lampu sorot kembar biru-kuning ala panggung utama", Color(0xFF38BDF8), Color(0xFFFACC15)),
    FAIRY_FIREFLY_VINE("FAIRY_FIREFLY_VINE", "Sulur Bunga Cahaya", "Rangkaian daun hijau 3D dengan bunga lampu warna-warni", Color(0xFF84CC16), Color(0xFFF43F5E)),
    CYBER_HEXAGON_HONEYCOMB("CYBER_HEXAGON_HONEYCOMB", "Sarang Lebah RGB 3D", "Panel heksagon kuning-biru-pink ala studio kreator", Color(0xFFFACC15), Color(0xFF06B6D4))
}

enum class MascotRoomWallStyle(
    val id: String,
    val displayName: String,
    val subtitle: String,
    val topColor: Color,
    val bottomColor: Color,
    val accentColor: Color
) {
    CYBER_GAMER_WALL("CYBER_GAMER_WALL", "Dinding Biru Studio", "Wallpaper biru Duolingo cerah dengan panel kayu & bingkai neon", Color(0xFF2563EB), Color(0xFF1D4ED8), Color(0xFF00F2FE)),
    SULTAN_PALACE_PILLARS("SULTAN_PALACE_PILLARS", "Istana Kuning Sultan", "Dinding emas kerajaan hangat dengan lis merah ruby & pilar", Color(0xFFF59E0B), Color(0xFFD97706), Color(0xFFFEF08A)),
    COSMIC_NEBULA_WINDOW("COSMIC_NEBULA_WINDOW", "Kamar Ungu Bintang", "Dinding ungu pastel-royal cerah dengan dekorasi bintang emas", Color(0xFF7C3AED), Color(0xFF6D28D9), Color(0xFFFACC15)),
    SAKURA_PASTEL_WALL("SAKURA_PASTEL_WALL", "Dinding Pink Ceria", "Wallpaper garis strawberry pink & krem hangat bergaya kawaii", Color(0xFFF472B6), Color(0xFFEC4899), Color(0xFFFEF08A)),
    SAMURAI_DOJO_SHOJI("SAMURAI_DOJO_SHOJI", "Dinding Kayu & Kertas", "Panel kayu madu hangat Jepang dengan bingkai merah-emas", Color(0xFFEA580C), Color(0xFFC2410C), Color(0xFFFACC15)),
    ORBITAL_EARTH_VIEW("ORBITAL_EARTH_VIEW", "Dinding Langit Cerah", "Dinding biru langit segar dengan awan putih & pelangi", Color(0xFF0EA5E9), Color(0xFF0284C7), Color(0xFFFACC15)),
    LOFI_BRICK_POSTERS("LOFI_BRICK_POSTERS", "Bata Terakota Hangat", "Dinding bata oranye-merah hangat dengan poster warna-warni", Color(0xFFF97316), Color(0xFFEA580C), Color(0xFFFEF08A)),
    EMERALD_ENCHANTED_WALL("EMERALD_ENCHANTED_WALL", "Dinding Hijau Taman", "Dinding hijau apel Duolingo segar dengan sulur bunga", Color(0xFF10B981), Color(0xFF059669), Color(0xFFFACC15)),
    GUNDAM_MECHA_BAY("GUNDAM_MECHA_BAY", "Hanggar Biru-Merah", "Dinding markas mecha biru terang dengan garis kuning-merah", Color(0xFF3B82F6), Color(0xFF2563EB), Color(0xFFEF4444)),
    CRYSTAL_ICE_PALACE("CRYSTAL_ICE_PALACE", "Istana Biru Es", "Dinding kristal biru muda berkilau dengan ornamen perak", Color(0xFF38BDF8), Color(0xFF0284C7), Color(0xFFFFFFFF)),
    VOLCANIC_DRAGON_LAIR("VOLCANIC_DRAGON_LAIR", "Kastil Merah Naga", "Dinding merah kirmizi gagah dengan lis perisai emas", Color(0xFFEF4444), Color(0xFFDC2626), Color(0xFFFACC15)),
    ROYAL_LIBRARY_ARCHIVES("ROYAL_LIBRARY_ARCHIVES", "Ruang Baca Kayu Jati", "Dinding panel kayu cokelat keemasan penuh buku warna-warni", Color(0xFFB45309), Color(0xFF92400E), Color(0xFFFACC15)),
    DEEP_SEA_ATLANTIS("DEEP_SEA_ATLANTIS", "Dinding Laut Toska", "Dinding toska tropis cerah dengan kerang mutiara & koral", Color(0xFF06B6D4), Color(0xFF0891B2), Color(0xFFFDE047)),
    NEO_TOKYO_BILLBOARD("NEO_TOKYO_BILLBOARD", "Pop-Art Magenta", "Dinding ungu-pink pop-art ceria dengan lampu papan neon", Color(0xFFDB2777), Color(0xFF9333EA), Color(0xFF00F2FE)),
    MINIMALIST_NORDIC_WOOD("MINIMALIST_NORDIC_WOOD", "Krem & Kayu Ceria", "Dinding krem hangat dengan panel kayu madu & lis hijau", Color(0xFFFBBF24), Color(0xFFF59E0B), Color(0xFF58CC02))
}

enum class MascotRoomRugStyle(
    val id: String,
    val displayName: String,
    val subtitle: String,
    val mainColor: Color,
    val ringColor: Color
) {
    PORTAL_VORTEX_RUG("PORTAL_VORTEX_RUG", "Karpet Bintang Biru", "Karpet bundar biru-sian tebal berumbai emas dengan bintang", Color(0xFF2563EB), Color(0xFFFACC15)),
    ROYAL_PERSIAN_GOLD("ROYAL_PERSIAN_GOLD", "Permadani Merah Sultan", "Karpet merah beludru kerajaan dengan rumbai benang emas", Color(0xFFEF4444), Color(0xFFFACC15)),
    CAT_PAW_PLUSH_RUG("CAT_PAW_PLUSH_RUG", "Karpet Tapak Kucing", "Bantalan bulu empuk 3D berbentuk paw kucing pink-putih", Color(0xFFEC4899), Color(0xFFFFFFFF)),
    NINJA_SHURIKEN_MAT("NINJA_SHURIKEN_MAT", "Karpet Naga Emas", "Permadani oranye-merah berukir medali naga emas", Color(0xFFEA580C), Color(0xFFFDE047)),
    DJ_VINYL_DISC_RUG("DJ_VINYL_DISC_RUG", "Karpet Piringan Musik", "Karpet piringan vinyl ungu-pink dengan label kuning cerah", Color(0xFF7C3AED), Color(0xFFEC4899)),
    CYBER_HEX_SHIELD_PAD("CYBER_HEX_SHIELD_PAD", "Karpet Heksagon Hijau", "Matras 3D hijau zamrud dengan tepi sian berkilau", Color(0xFF10B981), Color(0xFF00F2FE)),
    SUNFLOWER_PETAL_RUG("SUNFLOWER_PETAL_RUG", "Karpet Bunga Matahari", "Karpet kelopak bunga kuning-oranye ceria 3D", Color(0xFFFACC15), Color(0xFFEA580C)),
    COZY_CLOUD_BED_MAT("COZY_CLOUD_BED_MAT", "Karpet Awan Pelangi", "Bantalan awan biru-putih empuk dengan tepi pelangi", Color(0xFF38BDF8), Color(0xFFF472B6)),
    ARCANE_RUNE_SEAL("ARCANE_RUNE_SEAL", "Karpet Kompas Sihir", "Permadani ungu terang dengan bintang heksagram emas", Color(0xFF8B5CF6), Color(0xFFFACC15)),
    KOI_POND_HOLOGRAM("KOI_POND_HOLOGRAM", "Karpet Kolam Ikan Koi", "Karpet biru air jernih dengan ikan koi oranye & daun teratai", Color(0xFF06B6D4), Color(0xFFF97316)),
    DRAGON_SCALE_MEDALLION("DRAGON_SCALE_MEDALLION", "Medali Perisai Raja", "Karpet perisai merah-emas tebal berumbai kerajaan", Color(0xFFDC2626), Color(0xFFFBBF24)),
    GUNDAM_LAUNCH_CATAPULT("GUNDAM_LAUNCH_CATAPULT", "Pad Hanggar Mecha", "Karpet biru-kuning bergaris landasan robot tempur", Color(0xFF0284C7), Color(0xFFFACC15)),
    DIAMOND_SNOWFLAKE_MAT("DIAMOND_SNOWFLAKE_MAT", "Karpet Kristal Salju", "Karpet biru es cerah dengan kepingan salju putih 3D", Color(0xFF0EA5E9), Color(0xFFE0F2FE)),
    SULTAN_BITCOIN_COIN("SULTAN_BITCOIN_COIN", "Karpet Koin Emas 24K", "Karpet bundar koin emas kuning berkilau dengan mahkota", Color(0xFFF59E0B), Color(0xFFFEF08A)),
    SAKURA_BLOSSOM_POND("SAKURA_BLOSSOM_POND", "Karpet Bunga Sakura", "Karpet kelopak sakura pink cerah dengan pusat emas", Color(0xFFF43F5E), Color(0xFFFDE047))
}

// NEW SLOT 6: DESK & GAMING / WORK STATION (15 Variations)
enum class MascotRoomDeskStyle(
    val id: String,
    val displayName: String,
    val subtitle: String,
    val primaryColor: Color,
    val accentColor: Color
) {
    CYBER_TRIPLE_MONITOR_PC("CYBER_TRIPLE_MONITOR_PC", "PC Gaming 3 Monitor", "Meja kayu-biru dengan 3 layar game warna-warni & PC kipas RGB", Color(0xFF2563EB), Color(0xFF00F2FE)),
    SULTAN_GOLD_EXECUTIVE("SULTAN_GOLD_EXECUTIVE", "Meja Kayu Jati Sultan", "Meja jati emas mewah dengan tumpukan emas & layar grafik", Color(0xFFD97706), Color(0xFFFACC15)),
    ALCHEMIST_POTION_TABLE("ALCHEMIST_POTION_TABLE", "Meja Ramuan Alkemis", "Meja kayu ungu dengan botol ramuan pink-hijau-biru & bola kristal", Color(0xFF7C3AED), Color(0xFF4ADE80)),
    DJ_TURNTABLE_BOOTH("DJ_TURNTABLE_BOOTH", "Meja DJ Party RGB", "Booth DJ pink-biru ceria dengan piringan hitam & lampu disko", Color(0xFFEC4899), Color(0xFF00F2FE)),
    RETRO_ARCADE_CABINET("RETRO_ARCADE_CABINET", "Mesin Dingdong Arcade", "Mesin game arcade merah-kuning-biru klasik dengan joystick 3D", Color(0xFFEF4444), Color(0xFFFACC15)),
    GUNDAM_WORKBENCH_DOCK("GUNDAM_WORKBENCH_DOCK", "Meja Rakit Gundam", "Meja bengkel biru-kuning dengan robot mini & peralatan", Color(0xFF0284C7), Color(0xFFFACC15)),
    KAWAII_BOBA_CAFE_BAR("KAWAII_BOBA_CAFE_BAR", "Meja Kafe Boba & Kue", "Meja strawberry pink dengan gelas boba, kue tar & mesin kopi", Color(0xFFF472B6), Color(0xFFFDE047)),
    SAMURAI_TEA_CALLIGRAPHY("SAMURAI_TEA_CALLIGRAPHY", "Meja Teh & Bonsai", "Meja kayu merah Jepang dengan teko teh hijau & pohon bonsai", Color(0xFFEA580C), Color(0xFF84CC16)),
    HOLOGRAM_COMMAND_CONSOLE("HOLOGRAM_COMMAND_CONSOLE", "Konsol Peta Bintang", "Meja toska futuristik dengan globe hologram bumi 3D", Color(0xFF0891B2), Color(0xFF4ADE80)),
    ROYAL_GRAND_PIANO("ROYAL_GRAND_PIANO", "Grand Piano Emas", "Piano konser putih-emas mewah dengan tuts & buku musik", Color(0xFFF59E0B), Color(0xFFFFFFFF)),
    STEAMPUNK_INVENTOR_DESK("STEAMPUNK_INVENTOR_DESK", "Meja Penemu Emas", "Meja kayu tembaga dengan teleskop emas, jam & roda gigi", Color(0xFFD97706), Color(0xFF38BDF8)),
    QUANTUM_SERVER_RACK("QUANTUM_SERVER_RACK", "Komputer Super Hijau", "Meja teknologi hijau Duolingo dengan tabung energi sian", Color(0xFF16A34A), Color(0xFF00F2FE)),
    FAIRY_BONSAI_TERRARIUM("FAIRY_BONSAI_TERRARIUM", "Meja Taman Bunga", "Meja kayu taman dengan pot bunga matahari & rumah kaca", Color(0xFF65A30D), Color(0xFFFACC15)),
    DRAGON_FORGE_ANVIL("DRAGON_FORGE_ANVIL", "Meja Tempa Ksatria", "Meja batu-emas dengan perisai merah, pedang & kristal api", Color(0xFFDC2626), Color(0xFFFACC15)),
    NONE("NONE", "Tanpa Meja Setup", "Ruangan lega tanpa meja di sisi kanan", Color(0xFF94A3B8), Color(0xFFCBD5E1))
}

// NEW SLOT 7: SOFA, BED & LOUNGE FURNITURE (15 Variations)
enum class MascotRoomBedSofaStyle(
    val id: String,
    val displayName: String,
    val subtitle: String,
    val primaryColor: Color,
    val accentColor: Color
) {
    GAMING_THRONE_CHAIR("GAMING_THRONE_CHAIR", "Sofa Gamer Biru-Pink", "Sofa gaming empuk biru cerah dengan bantal pink & kuning", Color(0xFF2563EB), Color(0xFFEC4899)),
    SULTAN_VELVET_SOFA("SULTAN_VELVET_SOFA", "Sofa Beludru Sultan", "Sofa istana merah ruby empuk dengan bingkai ukiran emas 24K", Color(0xFFEF4444), Color(0xFFFACC15)),
    CYBER_SLEEP_CAPSULE("CYBER_SLEEP_CAPSULE", "Pod Kasur Toska 3D", "Kasur kapsul bundar biru toska dengan bantal kuning cerah", Color(0xFF06B6D4), Color(0xFFFACC15)),
    ROYAL_CANOPY_BED("ROYAL_CANOPY_BED", "Kasur Raja Berkanopi", "Kasur istana ungu-emas dengan tirai sutra & bantal mahkota", Color(0xFF7C3AED), Color(0xFFFDE047)),
    JAPANESE_WARM_KOTATSU("JAPANESE_WARM_KOTATSU", "Kasur Kotatsu Hangat", "Meja selimut merah-emas hangat dengan bantal duduk & jeruk", Color(0xFFEA580C), Color(0xFFFACC15)),
    KAWAII_CAT_PLUSH_SOFA("KAWAII_CAT_PLUSH_SOFA", "Sofa Boneka Kucing", "Sofa gembul pink bertelinga kucing dengan bantal hati putih", Color(0xFFEC4899), Color(0xFFFFFFFF)),
    FLOATING_CLOUD_HAMMOCK("FLOATING_CLOUD_HAMMOCK", "Sofa Awan Pelangi", "Sofa awan putih-biru super empuk dengan bantal bintang emas", Color(0xFF38BDF8), Color(0xFFFACC15)),
    DRAGON_EMPEROR_THRONE("DRAGON_EMPEROR_THRONE", "Singgasana Emas Naga", "Tahta raja emas murni dengan bantalan beludru merah", Color(0xFFF59E0B), Color(0xFFEF4444)),
    MECHA_PILOT_COCKPIT_SEAT("MECHA_PILOT_COCKPIT_SEAT", "Sofa Mecha Gundam", "Sofa kokpit biru-merah-kuning bergaya robot pahlawan", Color(0xFF1D4ED8), Color(0xFFFACC15)),
    GALAXY_BEANBAG_LOUNGE("GALAXY_BEANBAG_LOUNGE", "Beanbag Ungu Bintang", "Sofa beanbag bulat ungu-pink dengan bantal bulan sabit", Color(0xFF8B5CF6), Color(0xFFF472B6)),
    FAIRY_FLOWER_SWING("FAIRY_FLOWER_SWING", "Sofa Kelopak Bunga", "Sofa hijau daun & kelopak mawar pink dengan bantal lebah", Color(0xFF10B981), Color(0xFFF43F5E)),
    PEARL_CLAMSHELL_BED("PEARL_CLAMSHELL_BED", "Sofa Kerang Mutiara", "Kerang laut biru langit terbuka dengan bantal mutiara berkilau", Color(0xFF0EA5E9), Color(0xFFFEF08A)),
    EXECUTIVE_LEATHER_LOUNGE("EXECUTIVE_LEATHER_LOUNGE", "Sofa Kulit Karamel", "Sofa kulit cokelat karamel hangat dengan bantal hijau mint", Color(0xFFD97706), Color(0xFF34D399)),
    ARCANE_LEVITATION_CUSHION("ARCANE_LEVITATION_CUSHION", "Sofa Sihir Melayang", "Sofa beludru biru-ungu dengan kaki emas & bantal bintang", Color(0xFF6366F1), Color(0xFFFACC15)),
    NONE("NONE", "Tanpa Sofa / Kasur", "Ruangan lega tanpa tempat duduk tambahan", Color(0xFF94A3B8), Color(0xFFCBD5E1))
}

// NEW SLOT 8: WINDOW, AQUARIUM, TROPHY SHELF & WALL DECOR (15 Variations)
enum class MascotRoomWindowDecorStyle(
    val id: String,
    val displayName: String,
    val subtitle: String,
    val primaryColor: Color,
    val accentColor: Color
) {
    COSMIC_GALAXY_WINDOW("COSMIC_GALAXY_WINDOW", "Jendela Langit & Planet", "Jendela berbingkai emas dengan tirai merah, planet & bintang", Color(0xFF3B82F6), Color(0xFFFACC15)),
    NEON_CORAL_AQUARIUM("NEON_CORAL_AQUARIUM", "Akuarium Ikan Tropis", "Akuarium kaca biru jernih dengan ikan badut oranye & terumbu karang", Color(0xFF06B6D4), Color(0xFFF97316)),
    GUNDAM_TROPHY_SHELF("GUNDAM_TROPHY_SHELF", "Rak Piala & Mainan", "Rak kayu-emas 3 tingkat berisi piala juara, robot & buku warna-warni", Color(0xFFD97706), Color(0xFFFACC15)),
    BIOPOD_FOREST_TERRARIUM("BIOPOD_FOREST_TERRARIUM", "Jendela Taman Bunga", "Jendela kaca hijau segar dengan tanaman pot & kupu-kupu", Color(0xFF10B981), Color(0xFFFACC15)),
    CYBERPUNK_HOLO_POSTER("CYBERPUNK_HOLO_POSTER", "Papan Neon Pelangi", "Bingkai pajangan dinding pink-biru menyala penuh ikon game", Color(0xFFEC4899), Color(0xFF00F2FE)),
    SAMURAI_KATANA_RACK("SAMURAI_KATANA_RACK", "Rak Pedang Emas", "Bingkai kayu merah-emas memajang 2 katana berkilau & kipas", Color(0xFFEF4444), Color(0xFFFACC15)),
    SAKURA_BLOSSOM_WINDOW("SAKURA_BLOSSOM_WINDOW", "Jendela Pohon Sakura", "Jendela melengkung pink dengan pohon sakura mekar & matahari", Color(0xFFF472B6), Color(0xFFFEF08A)),
    SULTAN_CRYPTO_TICKER("SULTAN_CRYPTO_TICKER", "Brankas Pajangan Emas", "Etalase dinding emas berisi mahkota, berlian biru & piala", Color(0xFFF59E0B), Color(0xFF38BDF8)),
    ARCANUM_PORTAL_MIRROR("ARCANUM_PORTAL_MIRROR", "Cermin Istana Pelangi", "Cermin lengkung berukir emas dengan kaca kilau ungu-biru", Color(0xFF8B5CF6), Color(0xFFFACC15)),
    GOLDEN_DRAGON_TAPESTRY("GOLDEN_DRAGON_TAPESTRY", "Lukisan Naga & Gunung", "Bingkai lukisan kerajaan berwarna cerah dengan naga emas", Color(0xFFEA580C), Color(0xFFFDE047)),
    STEAMPUNK_CLOCKWORK_GEAR("STEAMPUNK_CLOCKWORK_GEAR", "Jam Dinding Cuckoo 3D", "Jam dinding kayu-emas klasik dengan bandul & roda gigi", Color(0xFFB45309), Color(0xFFFACC15)),
    UNDERWATER_SUBMARINE_PORT("UNDERWATER_SUBMARINE_PORT", "Jendela Laut & Lumba", "Jendela bundar emas menghadap laut biru cerah & karang", Color(0xFF0284C7), Color(0xFFFACC15)),
    ELEMENTAL_WEAPON_ARMORY("ELEMENTAL_WEAPON_ARMORY", "Rak Perisai & Pedang", "Pajangan perisai ksatria biru-emas & pedang permata", Color(0xFF2563EB), Color(0xFFFACC15)),
    LOFI_NEON_VINYL_WALL("LOFI_NEON_VINYL_WALL", "Rak Piringan & Gitar", "Pajangan piringan musik warna-warni & headphone emas", Color(0xFF7C3AED), Color(0xFFEC4899)),
    NONE("NONE", "Tanpa Dekor Dinding", "Dinding bersih tanpa jendela atau rak tambahan", Color(0xFF94A3B8), Color(0xFFCBD5E1))
}

// NEW SLOT 9: MINI ROOM COMPANION PET (15 Variations)
enum class MascotRoomPetStyle(
    val id: String,
    val displayName: String,
    val subtitle: String,
    val primaryColor: Color,
    val secondaryColor: Color
) {
    BABY_CYBER_DRAGON("BABY_CYBER_DRAGON", "Bayi Naga Biru", "Naga mungil biru-emas di atas bantal merah yang ceria", Color(0xFF38BDF8), Color(0xFFFACC15)),
    GOLDEN_LUCKY_NEKO("GOLDEN_LUCKY_NEKO", "Kucing Hoki Emas", "Kucing emas Maneki Neko berkalung merah pembawa hoki", Color(0xFFFACC15), Color(0xFFEF4444)),
    CROWNED_SLIME_KING("CROWNED_SLIME_KING", "Raja Slime Pelangi", "Slime biru kenyal 3D memakai mahkota emas berpermata", Color(0xFF0EA5E9), Color(0xFFFACC15)),
    CELESTIAL_KITSUNE_FOX("CELESTIAL_KITSUNE_FOX", "Rubah Kitsune Imut", "Rubah oranye-putih bertelinga merah dengan kalung lonceng emas", Color(0xFFF97316), Color(0xFFFEF08A)),
    FLOATING_AI_ROBO_ORB("FLOATING_AI_ROBO_ORB", "Robot Bola Hijau", "Robot pendamping bulat hijau Duolingo dengan sayap sian", Color(0xFF58CC02), Color(0xFF00F2FE)),
    MINI_PHOENIX_CHICK("MINI_PHOENIX_CHICK", "Burung Phoenix Emas", "Burung merah-kuning ceria dengan jambul mahkota api", Color(0xFFEF4444), Color(0xFFFACC15)),
    PIXEL_ARCADE_GHOST("PIXEL_ARCADE_GHOST", "Bintang Bantal Pink", "Maskot bintang pink gembul bermahkota kecil", Color(0xFFEC4899), Color(0xFFFDE047)),
    CRYSTAL_FROST_TURTLE("CRYSTAL_FROST_TURTLE", "Kura-Kura Permata", "Kura-kura hijau-biru imut bercangkang kristal emas", Color(0xFF10B981), Color(0xFF38BDF8)),
    KAWAII_MOON_BUNNY("KAWAII_MOON_BUNNY", "Kelinci Mochi Pink", "Kelinci gembul strawberry pink dengan pita kuning", Color(0xFFF472B6), Color(0xFFFACC15)),
    MECHA_ROBO_PUPPY("MECHA_ROBO_PUPPY", "Anjing Robot Biru", "Anjing mecha biru-putih dengan telinga kuning & visor", Color(0xFF2563EB), Color(0xFFFACC15)),
    COSMIC_STAR_JELLYFISH("COSMIC_STAR_JELLYFISH", "Gurita Pelangi Lucu", "Gurita imut ungu-pink bermahkota bintang emas", Color(0xFFA855F7), Color(0xFFF472B6)),
    ANCIENT_RUNE_GOLEM("ANCIENT_RUNE_GOLEM", "Beruang Madu Emas", "Beruang teddy cokelat-emas menggemaskan berpita merah", Color(0xFFD97706), Color(0xFFEF4444)),
    FOREST_KODAMA_SPIRIT("FOREST_KODAMA_SPIRIT", "Peri Tunas Daun", "Makhluk hijau apel imut dengan bunga matahari di kepala", Color(0xFF84CC16), Color(0xFFFACC15)),
    EMPEROR_GOLD_DRAGON_PET("EMPEROR_GOLD_DRAGON_PET", "Naga Sultan Emas", "Naga emas 24K mungil memegang permata merah ruby", Color(0xFFF59E0B), Color(0xFFEF4444)),
    NONE("NONE", "Tanpa Pet Kamar", "Kamar tanpa hewan peliharaan pendamping", Color(0xFF94A3B8), Color(0xFFCBD5E1))
}

enum class AppGraphicsQualityMode(
    val id: String,
    val displayName: String,
    val badgeText: String,
    val subtitle: String,
    val tierLevel: Int,
    val effectMultiplier: Int,
    val fpsLabel: String,
    val accentColor: Color
) {
    CHAOS_ULTIMATE_LOW(
        id = "CHAOS_ULTIMATE_LOW",
        displayName = "Chaos Low",
        badgeText = "1/25x SMOOTH • ULTIMATE PRO MAX LOW",
        subtitle = "Versi Chaos ke-2: 25 kali lebih ringan di bawah Smooth! Tanpa beban shader/glow berlapis namun kurva gerak mikro GSAP tetap bervariasi, responsif, & super hemat baterai (Ultimate Pro Max Smooth Low).",
        tierLevel = 0,
        effectMultiplier = 0,
        fpsLabel = "1/25x Smooth • Max Low",
        accentColor = Color(0xFF14B8A6)
    ),
    SMOOTH(
        id = "SMOOTH",
        displayName = "Smooth",
        badgeText = "1x EFEK • SUPER RINGAN",
        subtitle = "Warna box & kartu 100% sama dengan Ultra, namun hanya 1x efek dasar statis tanpa animasi berulang (hemat baterai & 120 FPS).",
        tierLevel = 1,
        effectMultiplier = 1,
        fpsLabel = "1x Effect • 120 FPS",
        accentColor = Color(0xFF10B981)
    ),
    BALANCED(
        id = "BALANCED",
        displayName = "Balanced",
        badgeText = "3x EFEK • RESPONSIF",
        subtitle = "Warna box tetap sama + 3x lapisan efek GSAP: pegas sentuh halus, bibir 3D, dan kilau statis.",
        tierLevel = 2,
        effectMultiplier = 3,
        fpsLabel = "3x Effect • 90/120 FPS",
        accentColor = Color(0xFF0EA5E9)
    ),
    HIGH(
        id = "HIGH",
        displayName = "High",
        badgeText = "7x EFEK • DEFAULT AMAN",
        subtitle = "Mode Default Aman: Warna box tetap sama + 7x lapisan efek GSAP (Staggered Reveal, 3D Tilt, angka bergulir, & gelombang tekan) tanpa membebani HP.",
        tierLevel = 3,
        effectMultiplier = 7,
        fpsLabel = "7x Effect • Default High",
        accentColor = Color(0xFF6366F1)
    ),
    EXTREME(
        id = "EXTREME",
        displayName = "Extreme",
        badgeText = "12x EFEK • MAGNETIC 3D",
        subtitle = "Warna box tetap sama + 12x lapisan efek GSAP: tarikan magnetik 3D, sudut HUD taktis, & kilau tepi dinamis.",
        tierLevel = 4,
        effectMultiplier = 12,
        fpsLabel = "12x Effect • Neon 3D",
        accentColor = Color(0xFFF59E0B)
    ),
    ULTRA_GSAP(
        id = "ULTRA_GSAP",
        displayName = "Ultra GSAP",
        badgeText = "20x LIPAT EFEK GSAP MULUS",
        subtitle = "Warna box tetap sama & nyaman di mata, dengan 20x lipat lapisan efek GSAP super mulus tanpa partikel bulat: Liquid Silk Aurora Sweep, Squircle Contour Wave, Dual Harmonic Ribbon, Draggable Spring, & 3D Magnetic!",
        tierLevel = 5,
        effectMultiplier = 20,
        fpsLabel = "20x Effects • Silk GSAP",
        accentColor = Color(0xFF38BDF8)
    ),
    CHAOS_OVERDRIVE(
        id = "CHAOS_OVERDRIVE",
        displayName = "Chaos Brutal",
        badgeText = "100x BRUTAL • TES 3 DETIK & WATCHDOG",
        subtitle = "Versi Chaos ke-1: Overdrive GSAP 100x lipat ekstrem! Wajib peringatan sebelum aktif, bertahan 3 detik (bisa dikunci permanen jika HP kuat), dan otomatis pulih ke mode sebelumnya/Smooth jika keluar aplikasi atau Force Close.",
        tierLevel = 6,
        effectMultiplier = 100,
        fpsLabel = "100x Brutal • 3s Safety",
        accentColor = Color(0xFFF43F5E)
    );

    companion object {
        fun fromId(id: String?): AppGraphicsQualityMode {
            if (id.isNullOrBlank()) return HIGH
            val clean = id.trim().uppercase()
            // Safety rule: never cold-boot into CHAOS_OVERDRIVE from storage; fallback to SMOOTH
            if (clean == "CHAOS_OVERDRIVE") return SMOOTH
            return entries.find { it.id.equals(clean, ignoreCase = true) } ?: HIGH
        }
    }
}

data class BrowserDomainAliasRule(
    val id: String,
    val aliasDomain: String,
    val targetDomain: String,
    val isEnabled: Boolean = true
)

data class BrowserUrlResolution(
    val actualLoadUrl: String,
    val displayAliasUrl: String,
    val matchedRule: BrowserDomainAliasRule?
)

object ArcanumBrowserDomainAliasEngine {
    fun normalizeHost(rawInput: String): String {
        var cleaned = rawInput.trim().lowercase()
        if (cleaned.isEmpty()) return ""
        if (cleaned.startsWith("https://")) cleaned = cleaned.removePrefix("https://")
        if (cleaned.startsWith("http://")) cleaned = cleaned.removePrefix("http://")
        cleaned = cleaned.substringBefore('/').substringBefore('?').substringBefore('#')
        cleaned = cleaned.substringBefore(':').trim()
        if (cleaned.startsWith("www.")) cleaned = cleaned.removePrefix("www.")
        return cleaned
    }

    private fun extractSchemeHostAndSuffix(rawUrl: String): Triple<String, String, String> {
        val trimmed = rawUrl.trim()
        val withScheme = when {
            trimmed.startsWith("https://", ignoreCase = true) -> trimmed
            trimmed.startsWith("http://", ignoreCase = true) -> trimmed
            else -> "https://$trimmed"
        }
        val schemeEnd = withScheme.indexOf("://")
        val scheme = withScheme.substring(0, schemeEnd + 3)
        val remainder = withScheme.substring(schemeEnd + 3)
        val slashIdx = remainder.indexOfFirst { it == '/' || it == '?' || it == '#' }
        val hostPart = if (slashIdx >= 0) remainder.substring(0, slashIdx) else remainder
        val suffixPart = if (slashIdx >= 0) remainder.substring(slashIdx) else ""
        return Triple(scheme, hostPart.lowercase(), suffixPart)
    }

    private fun hostMatches(actualHost: String, ruleHost: String): Boolean {
        val cleanActual = actualHost.removePrefix("www.")
        val cleanRule = ruleHost.removePrefix("www.")
        if (cleanRule.isEmpty() || cleanActual.isEmpty()) return false
        return cleanActual == cleanRule || cleanActual.endsWith(".$cleanRule")
    }

    fun resolveForNavigation(
        rawUrl: String,
        isProUnlocked: Boolean,
        rules: List<BrowserDomainAliasRule>
    ): BrowserUrlResolution {
        val trimmed = rawUrl.trim()
        if (trimmed.isEmpty() || trimmed.equals("about:blank", ignoreCase = true)) {
            return BrowserUrlResolution(trimmed, trimmed, null)
        }
        if (!isProUnlocked || rules.isEmpty()) {
            return BrowserUrlResolution(trimmed, trimmed, null)
        }
        val (scheme, rawHost, suffix) = extractSchemeHostAndSuffix(trimmed)
        val activeRules = rules.filter { it.isEnabled && it.aliasDomain.isNotBlank() && it.targetDomain.isNotBlank() }

        // 1. User entered custom alias domain (e.g. g.start -> google.com)
        val aliasHit = activeRules.firstOrNull { hostMatches(rawHost, it.aliasDomain) }
        if (aliasHit != null) {
            val cleanAlias = normalizeHost(aliasHit.aliasDomain)
            val cleanTarget = normalizeHost(aliasHit.targetDomain)
            val cleanActualHost = rawHost.removePrefix("www.")
            val subPrefix = if (cleanActualHost.length > cleanAlias.length && cleanActualHost.endsWith(".$cleanAlias")) {
                cleanActualHost.removeSuffix(".$cleanAlias") + "."
            } else {
                ""
            }
            val actualUrl = "$scheme$subPrefix$cleanTarget$suffix"
            val displayUrl = "$scheme$subPrefix$cleanAlias$suffix"
            return BrowserUrlResolution(actualUrl, displayUrl, aliasHit)
        }

        // 2. User opened real target domain directly (e.g. google.com -> show custom alias g.start)
        val targetHit = activeRules.firstOrNull { hostMatches(rawHost, it.targetDomain) }
        if (targetHit != null) {
            val cleanAlias = normalizeHost(targetHit.aliasDomain)
            val cleanTarget = normalizeHost(targetHit.targetDomain)
            val cleanActualHost = rawHost.removePrefix("www.")
            val subPrefix = if (cleanActualHost.length > cleanTarget.length && cleanActualHost.endsWith(".$cleanTarget")) {
                cleanActualHost.removeSuffix(".$cleanTarget") + "."
            } else {
                ""
            }
            val actualUrl = "$scheme$rawHost$suffix"
            val displayUrl = "$scheme$subPrefix$cleanAlias$suffix"
            return BrowserUrlResolution(actualUrl, displayUrl, targetHit)
        }

        return BrowserUrlResolution(trimmed, trimmed, null)
    }

    fun maskUrlForDisplay(
        loadedUrl: String,
        isProUnlocked: Boolean,
        rules: List<BrowserDomainAliasRule>
    ): String = resolveForNavigation(loadedUrl, isProUnlocked, rules).displayAliasUrl
}

object AppThemeController {
    private const val PRO_WATCHDOG_PREFS = "arcanum_pro_watchdog_prefs"
    private const val KEY_PRO_UNLOCKED = "pro_mode_unlocked"
    private const val KEY_FONT_PRESET_ID = "active_font_preset_id"
    private const val KEY_CHAOS_WATCHDOG_ARMED = "chaos_watchdog_armed"
    private const val KEY_CHAOS_SAFE_FALLBACK_ID = "chaos_safe_fallback_id"
    private const val KEY_BROWSER_DOMAIN_ALIASES = "pro_browser_domain_aliases"
    private const val SECRET_PRO_ACCESS_CODE = "ASTRAONYXULTRAPRO123"
    const val OFFICIAL_PRO_WHATSAPP_CHANNEL_URL = "https://whatsapp.com/channel/0029Vb8NmRhCsU9Vi7VXc11o"

    private var _selectedThemeState by mutableStateOf(AppVisualTheme.DUOLINGO)
    var isSanctuaryBentoGlowLocked: Boolean by mutableStateOf(false)
    var isSanctuaryBrowserActive: Boolean by mutableStateOf(false)

    var activeTheme: AppVisualTheme
        get() = if (isSanctuaryBentoGlowLocked) AppVisualTheme.BENTO_GLOW else _selectedThemeState
        set(value) {
            _selectedThemeState = value
        }

    val userSelectedTheme: AppVisualTheme
        get() = _selectedThemeState

    var activePalette: AppColorPalettePreset by mutableStateOf(AppColorPalettePreset.DUOLINGO_GREEN)
    var customPrimaryHex: String by mutableStateOf("#EF4444")
    var customAccentHex: String by mutableStateOf("#3B82F6")

    // App-Wide 6 Exclusive Font Presets (Normal Google, Serif, Italian, Bold, Minecraft, The Cavas)
    var activeFontPreset: AppFontPreset by mutableStateOf(AppFontPreset.NORMAL_GOOGLE)
    var fontScaleMultiplier: Float by mutableStateOf(1.0f)

    // Default Graphics Mode is HIGH so newly downloaded apps never overload lower-end phones!
    var graphicsQualityMode: AppGraphicsQualityMode by mutableStateOf(AppGraphicsQualityMode.HIGH)
    var transparentButtonBoxEnabled: Boolean by mutableStateOf(false)
    var transparentButtonAlpha: Float by mutableStateOf(0.24f)
    var navBarOffsetXDp: Float by mutableStateOf(0f)
    var navBarOffsetYDp: Float by mutableStateOf(0f)

    // Pro Mode & Dual Chaos Watchdog States
    var isProModeUnlocked: Boolean by mutableStateOf(false)
    var proLiveFpsHudEnabled: Boolean by mutableStateOf(false)
    var proChaosRandomizeCurvePerTap: Boolean by mutableStateOf(false)
    var showChaosOverdriveWarningDialog: Boolean by mutableStateOf(false)
    var chaosPreviousSafeMode: AppGraphicsQualityMode by mutableStateOf(AppGraphicsQualityMode.HIGH)
    var chaosOverdriveCountdownDeciseconds: Int by mutableIntStateOf(0)
    var chaosOverdriveUnlimitedLocked: Boolean by mutableStateOf(false)
    var chaosStatusToastMessage: String? by mutableStateOf(null)

    // New Pro Suite States (Decoy Vault, Intruder Trap, Deep EXIF/Hex Scrubber, Dynamic Island & Equalizer Studio)
    var proDecoyVaultEnabled: Boolean by mutableStateOf(true)
    var proDecoyPinCode: String by mutableStateOf("000000")
    var proDecoyVaultTitle: String by mutableStateOf("Arsip Catatan Kuliah & Resep")
    var proDecoyFakeFilesCount: Int by mutableIntStateOf(12)

    var proIntruderTrapEnabled: Boolean by mutableStateOf(true)
    var proIntruderMaxFailedAttempts: Int by mutableIntStateOf(2)
    var proIntruderSilentSirenVibrate: Boolean by mutableStateOf(true)
    var proIntruderTrapLogs: List<String> by mutableStateOf(
        listOf(
            "[TRAP-01] PIN Salah (991204) • Gerbang Lapis 1 • Kamera Depan Siaga",
            "[TRAP-02] Percobaan Paksa 2x • Sensor Guncang Aktif • IP Lokal Dicatat"
        )
    )

    var proExifAutoScrubOnImport: Boolean by mutableStateOf(true)
    var proExifStripGpsAndSerial: Boolean by mutableStateOf(true)
    var proExifZeroFillThumbnailHeader: Boolean by mutableStateOf(true)

    // Pro Dynamic Island & 5-Band Custom Equalizer Studio States
    var proDynamicIslandCompactPreset: Int by mutableIntStateOf(1) // 0=Nano(104dp), 1=Mini iOS(134dp), 2=Split Droplet
    var proDynamicIslandMiniStyle: Int by mutableIntStateOf(0) // 0=⚡76, 1=Wave, 2=0:22, 3=82bpm
    var proEqualizerPresetName: String by mutableStateOf("Cyberpunk Club")
    var proEqualizerBand60Hz: Float by mutableFloatStateOf(0.82f)
    var proEqualizerBand230Hz: Float by mutableFloatStateOf(0.68f)
    var proEqualizerBand910Hz: Float by mutableFloatStateOf(0.56f)
    var proEqualizerBand3600Hz: Float by mutableFloatStateOf(0.76f)
    var proEqualizerBand14000Hz: Float by mutableFloatStateOf(0.88f)

    // Pro Feature #9: Arcanum Browser Custom Domain Alias / Redirect Rules (e.g. g.start -> google.com)
    var proBrowserDomainAliases: List<BrowserDomainAliasRule> by mutableStateOf(
        listOf(
            BrowserDomainAliasRule(
                id = "alias_default_gstart",
                aliasDomain = "g.start",
                targetDomain = "google.com",
                isEnabled = true
            )
        )
    )

    // App-Wide GSAP Motion & UI Animation Engine State (Controlled in Theme Tab & Settings)
    var activeGsapPreset: GsapMotionPreset by mutableStateOf(GsapMotionPreset.GSAP_ELASTIC_BOUNCE)
    var activeGsapVisualStyle: GsapVisualEffectStyle by mutableStateOf(GsapVisualEffectStyle.CYBER_PLASMA_PULSE)
    var gsapMotionGraphicBannerEnabled: Boolean by mutableStateOf(true)
    var gsapSpeedMultiplier: Float by mutableStateOf(1.0f)
    var gsap3DTiltIntensity: Float by mutableStateOf(1.0f)
    var gsapStaggerEnabled: Boolean by mutableStateOf(true)
    var gsapCounterRollEnabled: Boolean by mutableStateOf(true)
    var gsapLiveDemoTriggerNonce: Int by mutableIntStateOf(0)

    fun selectFontPreset(preset: AppFontPreset, context: android.content.Context? = null) {
        activeFontPreset = preset
        context?.getSharedPreferences(PRO_WATCHDOG_PREFS, android.content.Context.MODE_PRIVATE)
            ?.edit()
            ?.putString(KEY_FONT_PRESET_ID, preset.id)
            ?.apply()
    }

    fun verifyAndUnlockProMode(inputCode: String, context: android.content.Context? = null): Boolean {
        val matched = inputCode.trim().equals(SECRET_PRO_ACCESS_CODE, ignoreCase = true)
        if (matched) {
            isProModeUnlocked = true
            // Default Pro HUD to false (off) unless the user explicitly toggles it on
            proLiveFpsHudEnabled = false
            proChaosRandomizeCurvePerTap = true
            context?.getSharedPreferences(PRO_WATCHDOG_PREFS, android.content.Context.MODE_PRIVATE)
                ?.edit()
                ?.putBoolean(KEY_PRO_UNLOCKED, true)
                ?.apply()
        }
        return matched
    }

    fun lockProModeAgain(context: android.content.Context? = null) {
        isProModeUnlocked = false
        proLiveFpsHudEnabled = false
        context?.getSharedPreferences(PRO_WATCHDOG_PREFS, android.content.Context.MODE_PRIVATE)
            ?.edit()
            ?.putBoolean(KEY_PRO_UNLOCKED, false)
            ?.apply()
    }

    private fun saveProDomainAliases(context: android.content.Context?) {
        context ?: return
        try {
            val arr = org.json.JSONArray()
            proBrowserDomainAliases.forEach { rule ->
                val obj = org.json.JSONObject().apply {
                    put("id", rule.id)
                    put("alias", rule.aliasDomain)
                    put("target", rule.targetDomain)
                    put("enabled", rule.isEnabled)
                }
                arr.put(obj)
            }
            context.getSharedPreferences(PRO_WATCHDOG_PREFS, android.content.Context.MODE_PRIVATE)
                .edit()
                .putString(KEY_BROWSER_DOMAIN_ALIASES, arr.toString())
                .apply()
        } catch (_: Throwable) {
        }
    }

    fun addOrUpdateProDomainAlias(
        rawAliasDomain: String,
        rawTargetDomain: String,
        context: android.content.Context? = null,
        editRuleId: String? = null
    ): Boolean {
        val cleanAlias = ArcanumBrowserDomainAliasEngine.normalizeHost(rawAliasDomain)
        val cleanTarget = ArcanumBrowserDomainAliasEngine.normalizeHost(rawTargetDomain)
        if (cleanAlias.isBlank() || cleanTarget.isBlank() || cleanAlias == cleanTarget || !cleanAlias.contains('.') || !cleanTarget.contains('.')) {
            return false
        }
        val nextList = if (!editRuleId.isNullOrBlank()) {
            proBrowserDomainAliases.map {
                if (it.id == editRuleId) it.copy(aliasDomain = cleanAlias, targetDomain = cleanTarget, isEnabled = true) else it
            }
        } else {
            val withoutDuplicate = proBrowserDomainAliases.filterNot {
                it.aliasDomain.equals(cleanAlias, ignoreCase = true)
            }
            listOf(
                BrowserDomainAliasRule(
                    id = "alias_${System.currentTimeMillis()}",
                    aliasDomain = cleanAlias,
                    targetDomain = cleanTarget,
                    isEnabled = true
                )
            ) + withoutDuplicate
        }
        proBrowserDomainAliases = nextList
        saveProDomainAliases(context)
        return true
    }

    fun toggleProDomainAlias(ruleId: String, context: android.content.Context? = null) {
        proBrowserDomainAliases = proBrowserDomainAliases.map {
            if (it.id == ruleId) it.copy(isEnabled = !it.isEnabled) else it
        }
        saveProDomainAliases(context)
    }

    fun removeProDomainAlias(ruleId: String, context: android.content.Context? = null) {
        proBrowserDomainAliases = proBrowserDomainAliases.filterNot { it.id == ruleId }
        saveProDomainAliases(context)
    }

    fun requestGraphicsModeChange(
        targetMode: AppGraphicsQualityMode,
        context: android.content.Context? = null,
        onPersistSafeMode: (String) -> Unit = {}
    ) {
        if (targetMode == AppGraphicsQualityMode.CHAOS_OVERDRIVE) {
            if (graphicsQualityMode != AppGraphicsQualityMode.CHAOS_OVERDRIVE) {
                chaosPreviousSafeMode = graphicsQualityMode
            }
            showChaosOverdriveWarningDialog = true
        } else {
            chaosOverdriveCountdownDeciseconds = 0
            chaosOverdriveUnlimitedLocked = false
            context?.getSharedPreferences(PRO_WATCHDOG_PREFS, android.content.Context.MODE_PRIVATE)
                ?.edit()
                ?.putBoolean(KEY_CHAOS_WATCHDOG_ARMED, false)
                ?.putString(KEY_CHAOS_SAFE_FALLBACK_ID, targetMode.id)
                ?.apply()
            applyGraphicsQualityPreset(targetMode)
            onPersistSafeMode(targetMode.id)
        }
    }

    fun confirmStartChaosOverdrive(
        context: android.content.Context? = null,
        onPersistSafeFallback: (String) -> Unit = {}
    ) {
        showChaosOverdriveWarningDialog = false
        val fallback = if (chaosPreviousSafeMode == AppGraphicsQualityMode.CHAOS_OVERDRIVE) {
            AppGraphicsQualityMode.SMOOTH
        } else {
            chaosPreviousSafeMode
        }
        chaosPreviousSafeMode = fallback
        chaosOverdriveUnlimitedLocked = false
        chaosOverdriveCountdownDeciseconds = 30 // 3.0 seconds (30 x 100ms)
        // Arm crash/exit watchdog synchronously on disk so if force-close happens, next launch reverts to safe mode
        context?.getSharedPreferences(PRO_WATCHDOG_PREFS, android.content.Context.MODE_PRIVATE)
            ?.edit()
            ?.putBoolean(KEY_CHAOS_WATCHDOG_ARMED, true)
            ?.putString(KEY_CHAOS_SAFE_FALLBACK_ID, fallback.id)
            ?.commit()
        onPersistSafeFallback(fallback.id)
        applyGraphicsQualityPreset(AppGraphicsQualityMode.CHAOS_OVERDRIVE)
    }

    fun lockChaosOverdriveUnlimited() {
        chaosOverdriveUnlimitedLocked = true
        chaosStatusToastMessage = "Chaos Brutal Overdrive dikunci aktif tanpa batas! (Otomatis reset ke aman jika keluar aplikasi)."
    }

    fun revertFromChaosOverdriveToSafeMode(
        useSmoothFallback: Boolean = false,
        reasonMessage: String? = null,
        context: android.content.Context? = null,
        onPersistSafeMode: (String) -> Unit = {}
    ) {
        val targetSafe = if (useSmoothFallback || chaosPreviousSafeMode == AppGraphicsQualityMode.CHAOS_OVERDRIVE) {
            AppGraphicsQualityMode.SMOOTH
        } else {
            chaosPreviousSafeMode
        }
        chaosOverdriveCountdownDeciseconds = 0
        chaosOverdriveUnlimitedLocked = false
        showChaosOverdriveWarningDialog = false
        context?.getSharedPreferences(PRO_WATCHDOG_PREFS, android.content.Context.MODE_PRIVATE)
            ?.edit()
            ?.putBoolean(KEY_CHAOS_WATCHDOG_ARMED, false)
            ?.putString(KEY_CHAOS_SAFE_FALLBACK_ID, targetSafe.id)
            ?.apply()
        applyGraphicsQualityPreset(targetSafe)
        onPersistSafeMode(targetSafe.id)
        if (!reasonMessage.isNullOrBlank()) {
            chaosStatusToastMessage = reasonMessage
        }
    }

    fun checkBootWatchdogAndSyncPro(
        context: android.content.Context,
        onRevertPersistedMode: (String) -> Unit = {}
    ) {
        val prefs = context.getSharedPreferences(PRO_WATCHDOG_PREFS, android.content.Context.MODE_PRIVATE)
        isProModeUnlocked = prefs.getBoolean(KEY_PRO_UNLOCKED, false)
        val savedFontId = prefs.getString(KEY_FONT_PRESET_ID, null)
        if (!savedFontId.isNullOrBlank()) {
            activeFontPreset = AppFontPreset.fromId(savedFontId)
        }
        val savedAliasesJson = prefs.getString(KEY_BROWSER_DOMAIN_ALIASES, null)
        if (!savedAliasesJson.isNullOrBlank()) {
            try {
                val arr = org.json.JSONArray(savedAliasesJson)
                val parsed = buildList {
                    for (i in 0 until arr.length()) {
                        val obj = arr.optJSONObject(i) ?: continue
                        val alias = obj.optString("alias").trim()
                        val target = obj.optString("target").trim()
                        if (alias.isNotBlank() && target.isNotBlank()) {
                            add(
                                BrowserDomainAliasRule(
                                    id = obj.optString("id", "alias_$i"),
                                    aliasDomain = alias,
                                    targetDomain = target,
                                    isEnabled = obj.optBoolean("enabled", true)
                                )
                            )
                        }
                    }
                }
                proBrowserDomainAliases = parsed
            } catch (_: Throwable) {
            }
        }
        val wasWatchdogArmed = prefs.getBoolean(KEY_CHAOS_WATCHDOG_ARMED, false)
        if (wasWatchdogArmed || graphicsQualityMode == AppGraphicsQualityMode.CHAOS_OVERDRIVE) {
            val fallbackId = prefs.getString(KEY_CHAOS_SAFE_FALLBACK_ID, "SMOOTH") ?: "SMOOTH"
            val safeMode = if (fallbackId == "CHAOS_OVERDRIVE") {
                AppGraphicsQualityMode.SMOOTH
            } else {
                AppGraphicsQualityMode.fromId(fallbackId)
            }
            prefs.edit().putBoolean(KEY_CHAOS_WATCHDOG_ARMED, false).apply()
            chaosPreviousSafeMode = safeMode
            chaosOverdriveCountdownDeciseconds = 0
            chaosOverdriveUnlimitedLocked = false
            applyGraphicsQualityPreset(safeMode)
            onRevertPersistedMode(safeMode.id)
            chaosStatusToastMessage = "Proteksi Anti-Force-Close: Mode grafik dikembalikan ke ${safeMode.displayName}."
        }
    }

    fun applyGraphicsQualityPreset(mode: AppGraphicsQualityMode) {
        graphicsQualityMode = mode
        // IMPORTANT: Do NOT alter transparentButtonBoxEnabled or box colors when switching Smooth <-> Ultra!
        // Box colors stay 100% identical; only the GSAP effect density changes!
        when (mode) {
            AppGraphicsQualityMode.CHAOS_ULTIMATE_LOW -> {
                gsapStaggerEnabled = false
                gsapCounterRollEnabled = false
                gsap3DTiltIntensity = 0.0f
                gsapSpeedMultiplier = 2.50f
                proChaosRandomizeCurvePerTap = true
                activeGsapPreset = GsapMotionPreset.TURBO_SNAPPY
            }
            AppGraphicsQualityMode.SMOOTH -> {
                gsapStaggerEnabled = false
                gsapCounterRollEnabled = false
                gsap3DTiltIntensity = 0.0f
                gsapSpeedMultiplier = 1.85f
                activeGsapPreset = GsapMotionPreset.TURBO_SNAPPY
            }
            AppGraphicsQualityMode.BALANCED -> {
                gsapStaggerEnabled = false
                gsapCounterRollEnabled = true
                gsap3DTiltIntensity = 0.55f
                gsapSpeedMultiplier = 1.25f
                activeGsapPreset = GsapMotionPreset.IOS_LIQUID_SPRING
            }
            AppGraphicsQualityMode.HIGH -> {
                gsapStaggerEnabled = true
                gsapCounterRollEnabled = true
                gsap3DTiltIntensity = 1.0f
                gsapSpeedMultiplier = 1.0f
                activeGsapPreset = GsapMotionPreset.GSAP_EXPO_CINEMATIC
            }
            AppGraphicsQualityMode.EXTREME -> {
                gsapStaggerEnabled = true
                gsapCounterRollEnabled = true
                gsap3DTiltIntensity = 1.45f
                gsapSpeedMultiplier = 1.10f
                activeGsapPreset = GsapMotionPreset.CYBER_MAGNETIC_3D
            }
            AppGraphicsQualityMode.ULTRA_GSAP -> {
                gsapStaggerEnabled = true
                gsapCounterRollEnabled = true
                gsap3DTiltIntensity = 1.75f
                gsapSpeedMultiplier = 1.15f
                activeGsapPreset = GsapMotionPreset.GSAP_ELASTIC_BOUNCE
            }
            AppGraphicsQualityMode.CHAOS_OVERDRIVE -> {
                gsapStaggerEnabled = true
                gsapCounterRollEnabled = true
                gsap3DTiltIntensity = 2.40f
                gsapSpeedMultiplier = 1.65f
                proChaosRandomizeCurvePerTap = true
                activeGsapPreset = GsapMotionPreset.GSAP_VORTEX_SPIRAL_3D
            }
        }
        gsapLiveDemoTriggerNonce++
    }

    // Dynamic Island Non-Overlap Layout Mode, Sculptural Shape, & Material Color State
    // Default to minimal folded cutout pill when untouched & no active Arcanum notification
    var dynamicIslandLayoutMode: DynamicIslandLayoutMode by mutableStateOf(DynamicIslandLayoutMode.DOCKED_PUSH_DOWN)
    var isDynamicIslandFolded: Boolean by mutableStateOf(true)
    var proDynamicIslandMinimalCompact: Boolean by mutableStateOf(true)
    var dynamicIslandShape: DynamicIslandShapeStyle by mutableStateOf(DynamicIslandShapeStyle.CLOUD_PUFF_RIBBON)
    var dynamicIslandPreset: DynamicIslandColorPreset by mutableStateOf(DynamicIslandColorPreset.PEARL_DAYLIGHT_GLASS)
    var dynamicIslandBgHex: String by mutableStateOf("#F8FAFC")
    var dynamicIslandBorderHex: String by mutableStateOf("#0284C7")
    var dynamicIslandGlowHex: String by mutableStateOf("#38BDF8")

    val resolvedIslandBgColor: Color
        get() = parseHexColor(
            if (dynamicIslandPreset == DynamicIslandColorPreset.CUSTOM) dynamicIslandBgHex else dynamicIslandPreset.bgHex,
            Color(0xFFF8FAFC)
        )

    val resolvedIslandBorderColor: Color
        get() = parseHexColor(
            if (dynamicIslandPreset == DynamicIslandColorPreset.CUSTOM) dynamicIslandBorderHex else dynamicIslandPreset.borderHex,
            Color(0xFF0284C7)
        )

    val resolvedIslandGlowColor: Color
        get() = parseHexColor(
            if (dynamicIslandPreset == DynamicIslandColorPreset.CUSTOM) dynamicIslandGlowHex else dynamicIslandPreset.glowHex,
            Color(0xFF38BDF8)
        )

    val isIslandLightSurface: Boolean
        get() {
            val bg = resolvedIslandBgColor
            val luminance = 0.299f * bg.red + 0.587f * bg.green + 0.114f * bg.blue
            return luminance > 0.58f
        }

    fun applyDynamicIslandPreset(preset: DynamicIslandColorPreset) {
        dynamicIslandPreset = preset
        if (preset != DynamicIslandColorPreset.CUSTOM) {
            dynamicIslandBgHex = preset.bgHex
            dynamicIslandBorderHex = preset.borderHex
            dynamicIslandGlowHex = preset.glowHex
        }
    }

    // Mascot Studio & Wardrobe State
    var mascotActivityPose: MascotActivityPose by mutableStateOf(MascotActivityPose.AUTO_SMART)
    var mascotAccessory: MascotAccessory by mutableStateOf(MascotAccessory.NONE)
    var mascotSkin: MascotBodySkin by mutableStateOf(MascotBodySkin.DEFAULT_MOOD)
    var mascotOutfit: MascotUpperOutfit by mutableStateOf(MascotUpperOutfit.NONE)
    var mascotGloves: MascotHandGloves by mutableStateOf(MascotHandGloves.DEFAULT_HANDS)
    var mascotAura: MascotAuraStyle by mutableStateOf(MascotAuraStyle.COSMIC_OCTAGON)
    var mascotEmotion: MascotEmotionPreset by mutableStateOf(MascotEmotionPreset.AUTO_DYNAMIC)

    // 9-Part Modular Per-Part Room Customization State (Default to Colorful Duolingo Skeuomorphic Room!)
    var mascotRoomTheme: MascotRoomTheme by mutableStateOf(MascotRoomTheme.CYBER_GAMER_LOFT)
    var roomClosetStyle: MascotRoomClosetStyle by mutableStateOf(MascotRoomClosetStyle.SULTAN_GOLD_ARMOIRE)
    var roomFloorStyle: MascotRoomFloorStyle by mutableStateOf(MascotRoomFloorStyle.WARM_OAK_PARQUET)
    var roomLampStyle: MascotRoomLampStyle by mutableStateOf(MascotRoomLampStyle.CRYSTAL_CHANDELIER)
    var roomWallStyle: MascotRoomWallStyle by mutableStateOf(MascotRoomWallStyle.CYBER_GAMER_WALL)
    var roomRugStyle: MascotRoomRugStyle by mutableStateOf(MascotRoomRugStyle.ROYAL_PERSIAN_GOLD)
    var roomDeskStyle: MascotRoomDeskStyle by mutableStateOf(MascotRoomDeskStyle.CYBER_TRIPLE_MONITOR_PC)
    var roomBedSofaStyle: MascotRoomBedSofaStyle by mutableStateOf(MascotRoomBedSofaStyle.SULTAN_VELVET_SOFA)
    var roomWindowDecorStyle: MascotRoomWindowDecorStyle by mutableStateOf(MascotRoomWindowDecorStyle.NEON_CORAL_AQUARIUM)
    var roomPetStyle: MascotRoomPetStyle by mutableStateOf(MascotRoomPetStyle.GOLDEN_LUCKY_NEKO)

    var mascotCosmicAuraEnabled: Boolean by mutableStateOf(true)
    var isMediaWatchingActive: Boolean by mutableStateOf(false)
    var poseTriggerNonce: Int by mutableIntStateOf(0)

    fun applyRoomThemePreset(theme: MascotRoomTheme) {
        mascotRoomTheme = theme
        when (theme) {
            MascotRoomTheme.CYBER_GAMER_LOFT -> {
                roomWallStyle = MascotRoomWallStyle.CYBER_GAMER_WALL
                roomFloorStyle = MascotRoomFloorStyle.CYBER_HEX_GRID
                roomClosetStyle = MascotRoomClosetStyle.CYBER_NEON_CLOSET
                roomLampStyle = MascotRoomLampStyle.NEON_RGB_STRIP
                roomRugStyle = MascotRoomRugStyle.PORTAL_VORTEX_RUG
                roomDeskStyle = MascotRoomDeskStyle.CYBER_TRIPLE_MONITOR_PC
                roomBedSofaStyle = MascotRoomBedSofaStyle.GAMING_THRONE_CHAIR
                roomWindowDecorStyle = MascotRoomWindowDecorStyle.CYBERPUNK_HOLO_POSTER
                roomPetStyle = MascotRoomPetStyle.BABY_CYBER_DRAGON
            }
            MascotRoomTheme.SULTAN_PENTHOUSE -> {
                roomWallStyle = MascotRoomWallStyle.SULTAN_PALACE_PILLARS
                roomFloorStyle = MascotRoomFloorStyle.ROYAL_MARBLE_GOLD
                roomClosetStyle = MascotRoomClosetStyle.SULTAN_GOLD_ARMOIRE
                roomLampStyle = MascotRoomLampStyle.CRYSTAL_CHANDELIER
                roomRugStyle = MascotRoomRugStyle.ROYAL_PERSIAN_GOLD
                roomDeskStyle = MascotRoomDeskStyle.SULTAN_GOLD_EXECUTIVE
                roomBedSofaStyle = MascotRoomBedSofaStyle.SULTAN_VELVET_SOFA
                roomWindowDecorStyle = MascotRoomWindowDecorStyle.SULTAN_CRYPTO_TICKER
                roomPetStyle = MascotRoomPetStyle.GOLDEN_LUCKY_NEKO
            }
            MascotRoomTheme.COSMIC_MAGE_TOWER -> {
                roomWallStyle = MascotRoomWallStyle.COSMIC_NEBULA_WINDOW
                roomFloorStyle = MascotRoomFloorStyle.COSMIC_STAR_GLASS
                roomClosetStyle = MascotRoomClosetStyle.ARCANE_GRIMOIRE_CABINET
                roomLampStyle = MascotRoomLampStyle.FLOATING_MAGIC_CANDLES
                roomRugStyle = MascotRoomRugStyle.ARCANE_RUNE_SEAL
                roomDeskStyle = MascotRoomDeskStyle.ALCHEMIST_POTION_TABLE
                roomBedSofaStyle = MascotRoomBedSofaStyle.ROYAL_CANOPY_BED
                roomWindowDecorStyle = MascotRoomWindowDecorStyle.COSMIC_GALAXY_WINDOW
                roomPetStyle = MascotRoomPetStyle.MINI_PHOENIX_CHICK
            }
            MascotRoomTheme.SAKURA_KAWAII_ROOM -> {
                roomWallStyle = MascotRoomWallStyle.SAKURA_PASTEL_WALL
                roomFloorStyle = MascotRoomFloorStyle.SAKURA_PINK_CARPET
                roomClosetStyle = MascotRoomClosetStyle.SAKURA_KAWAII_WARDROBE
                roomLampStyle = MascotRoomLampStyle.SAKURA_CLOUD_LAMP
                roomRugStyle = MascotRoomRugStyle.CAT_PAW_PLUSH_RUG
                roomDeskStyle = MascotRoomDeskStyle.KAWAII_BOBA_CAFE_BAR
                roomBedSofaStyle = MascotRoomBedSofaStyle.KAWAII_CAT_PLUSH_SOFA
                roomWindowDecorStyle = MascotRoomWindowDecorStyle.SAKURA_BLOSSOM_WINDOW
                roomPetStyle = MascotRoomPetStyle.KAWAII_MOON_BUNNY
            }
            MascotRoomTheme.SAMURAI_DOJO -> {
                roomWallStyle = MascotRoomWallStyle.SAMURAI_DOJO_SHOJI
                roomFloorStyle = MascotRoomFloorStyle.SAMURAI_TATAMI_MAT
                roomClosetStyle = MascotRoomClosetStyle.SAMURAI_SHOJI_CLOSET
                roomLampStyle = MascotRoomLampStyle.JAPANESE_PAPER_LANTERN
                roomRugStyle = MascotRoomRugStyle.NINJA_SHURIKEN_MAT
                roomDeskStyle = MascotRoomDeskStyle.SAMURAI_TEA_CALLIGRAPHY
                roomBedSofaStyle = MascotRoomBedSofaStyle.JAPANESE_WARM_KOTATSU
                roomWindowDecorStyle = MascotRoomWindowDecorStyle.SAMURAI_KATANA_RACK
                roomPetStyle = MascotRoomPetStyle.CELESTIAL_KITSUNE_FOX
            }
            MascotRoomTheme.ORBITAL_SPACE_CABIN -> {
                roomWallStyle = MascotRoomWallStyle.ORBITAL_EARTH_VIEW
                roomFloorStyle = MascotRoomFloorStyle.MECHA_STEEL_DECK
                roomClosetStyle = MascotRoomClosetStyle.ORBITAL_CAPSULE_POD
                roomLampStyle = MascotRoomLampStyle.ORBITAL_HOLOGRAM_BEAM
                roomRugStyle = MascotRoomRugStyle.CYBER_HEX_SHIELD_PAD
                roomDeskStyle = MascotRoomDeskStyle.HOLOGRAM_COMMAND_CONSOLE
                roomBedSofaStyle = MascotRoomBedSofaStyle.CYBER_SLEEP_CAPSULE
                roomWindowDecorStyle = MascotRoomWindowDecorStyle.NEON_CORAL_AQUARIUM
                roomPetStyle = MascotRoomPetStyle.FLOATING_AI_ROBO_ORB
            }
        }
    }

    fun randomizeRoomParts() {
        roomWallStyle = MascotRoomWallStyle.entries.random()
        roomFloorStyle = MascotRoomFloorStyle.entries.random()
        roomClosetStyle = MascotRoomClosetStyle.entries.random()
        roomLampStyle = MascotRoomLampStyle.entries.random()
        roomRugStyle = MascotRoomRugStyle.entries.random()
        roomDeskStyle = MascotRoomDeskStyle.entries.filter { it != MascotRoomDeskStyle.NONE }.random()
        roomBedSofaStyle = MascotRoomBedSofaStyle.entries.filter { it != MascotRoomBedSofaStyle.NONE }.random()
        roomWindowDecorStyle = MascotRoomWindowDecorStyle.entries.filter { it != MascotRoomWindowDecorStyle.NONE }.random()
        roomPetStyle = MascotRoomPetStyle.entries.filter { it != MascotRoomPetStyle.NONE }.random()
    }

    fun applyCostumePreset(preset: MascotCostumePreset) {
        mascotSkin = preset.skin
        mascotOutfit = preset.outfit
        mascotGloves = preset.gloves
        mascotAccessory = preset.accessory
        poseTriggerNonce++
    }

    fun randomizeCostume() {
        val skins = MascotBodySkin.entries.filter { it != MascotBodySkin.DEFAULT_MOOD }
        val outfits = MascotUpperOutfit.entries.filter { it != MascotUpperOutfit.NONE }
        val glovesList = MascotHandGloves.entries.filter { it != MascotHandGloves.DEFAULT_HANDS }
        val accList = MascotAccessory.entries.filter { it != MascotAccessory.NONE }
        val auras = MascotAuraStyle.entries.filter { it != MascotAuraStyle.NONE }
        mascotSkin = skins.random()
        mascotOutfit = outfits.random()
        mascotGloves = glovesList.random()
        mascotAccessory = accList.random()
        mascotAura = auras.random()
        mascotEmotion = MascotEmotionPreset.entries.random()
        mascotCosmicAuraEnabled = true
        poseTriggerNonce++
    }

    fun resetCostumeToDefault() {
        mascotSkin = MascotBodySkin.DEFAULT_MOOD
        mascotOutfit = MascotUpperOutfit.NONE
        mascotGloves = MascotHandGloves.DEFAULT_HANDS
        mascotAccessory = MascotAccessory.NONE
        mascotAura = MascotAuraStyle.COSMIC_OCTAGON
        mascotEmotion = MascotEmotionPreset.AUTO_DYNAMIC
        mascotActivityPose = MascotActivityPose.AUTO_SMART
        poseTriggerNonce++
    }

    fun parseHexColor(hex: String, fallback: Color): Color {
        val cleaned = hex.trim().removePrefix("#")
        return try {
            when (cleaned.length) {
                6 -> {
                    val rgb = cleaned.toLong(16)
                    Color(
                        red = ((rgb shr 16) and 0xFF) / 255f,
                        green = ((rgb shr 8) and 0xFF) / 255f,
                        blue = (rgb and 0xFF) / 255f,
                        alpha = 1f
                    )
                }
                8 -> {
                    val argb = cleaned.toLong(16)
                    Color(
                        alpha = ((argb shr 24) and 0xFF) / 255f,
                        red = ((argb shr 16) and 0xFF) / 255f,
                        green = ((argb shr 8) and 0xFF) / 255f,
                        blue = (argb and 0xFF) / 255f
                    )
                }
                else -> fallback
            }
        } catch (_: Exception) {
            fallback
        }
    }

    fun darkenColor(color: Color, factor: Float = 0.76f): Color {
        return Color(
            red = (color.red * factor).coerceIn(0f, 1f),
            green = (color.green * factor).coerceIn(0f, 1f),
            blue = (color.blue * factor).coerceIn(0f, 1f),
            alpha = color.alpha
        )
    }

    fun lightenTint(color: Color, whiteMix: Float = 0.86f): Color {
        val inv = (1f - whiteMix).coerceIn(0f, 1f)
        return Color(
            red = (color.red * inv + 1f * whiteMix).coerceIn(0f, 1f),
            green = (color.green * inv + 1f * whiteMix).coerceIn(0f, 1f),
            blue = (color.blue * inv + 1f * whiteMix).coerceIn(0f, 1f),
            alpha = 1f
        )
    }

    fun lightenColor(color: Color, whiteMix: Float = 0.25f): Color = lightenTint(color, whiteMix)

    val resolvedPrimaryColor: Color
        get() = if (activePalette == AppColorPalettePreset.CUSTOM) {
            parseHexColor(customPrimaryHex, Color(0xFFEF4444))
        } else {
            activePalette.primaryColor
        }

    val resolvedPrimaryLipColor: Color
        get() = if (activePalette == AppColorPalettePreset.CUSTOM) {
            darkenColor(resolvedPrimaryColor, 0.74f)
        } else {
            activePalette.primaryLipColor
        }

    val resolvedSoftSurfaceColor: Color
        get() = if (activePalette == AppColorPalettePreset.CUSTOM) {
            lightenTint(resolvedPrimaryColor, 0.82f)
        } else {
            activePalette.softSurfaceColor
        }

    val resolvedCanvasTintColor: Color
        get() = if (activePalette == AppColorPalettePreset.CUSTOM) {
            lightenTint(resolvedPrimaryColor, 0.94f)
        } else {
            activePalette.canvasTintColor
        }

    val resolvedSecondaryAccent: Color
        get() = if (activePalette == AppColorPalettePreset.CUSTOM) {
            parseHexColor(customAccentHex, Color(0xFF3B82F6))
        } else {
            activePalette.secondaryAccentColor
        }
}

// =========================================================================
// SIGNATURE PRIMARY & ACCENT COLORS + 3D BOTTOM LIP SHADES (REACTIVE PALETTE)
// =========================================================================

val DuoFeatherGreen: Color
    get() = AppThemeController.resolvedPrimaryColor

val DuoFeatherGreenLip: Color
    get() = AppThemeController.resolvedPrimaryLipColor

val DuoFeatherGreenSoft: Color
    get() = AppThemeController.resolvedSoftSurfaceColor

val DuoMacawBlue = Color(0xFF1CB0F6)
val DuoMacawBlueLip = Color(0xFF1899D6)
val DuoMacawBlueSoft = Color(0xFFDDF4FF)

val DuoBeeYellow = Color(0xFFFFC800)
val DuoBeeYellowLip = Color(0xFFE5A800)
val DuoBeeYellowSoft = Color(0xFFFFF5D6)

val DuoFoxOrange = Color(0xFFFF9600)
val DuoFoxOrangeLip = Color(0xFFE57800)
val DuoFoxOrangeSoft = Color(0xFFFFEDD5)

val DuoBeetlePurple = Color(0xFFCE82FF)
val DuoBeetlePurpleLip = Color(0xFFA568CC)
val DuoBeetlePurpleSoft = Color(0xFFF3E8FF)

val DuoCardinalRed = Color(0xFFFF4B4B)
val DuoCardinalRedLip = Color(0xFFEA2B2B)
val DuoCardinalRedSoft = Color(0xFFFFDFE0)

// Dynamic Theme-Aware & Palette-Aware Surfaces & Borders
val DuoSnowCanvas: Color
    get() = when (AppThemeController.activeTheme) {
        AppVisualTheme.DUOLINGO -> AppThemeController.resolvedCanvasTintColor
        AppVisualTheme.GLASSMORPHISM -> AppThemeController.lightenTint(AppThemeController.resolvedPrimaryColor, 0.88f)
        AppVisualTheme.NEUMORPHISM -> AppThemeController.lightenTint(AppThemeController.resolvedPrimaryColor, 0.89f)
        AppVisualTheme.MINIMALIST -> Color(0xFFFAFAFA)
        AppVisualTheme.FLAT_DESIGN -> AppThemeController.lightenTint(AppThemeController.resolvedPrimaryColor, 0.83f)
        AppVisualTheme.BENTO_GLOW -> AppThemeController.lightenTint(AppThemeController.resolvedSecondaryAccent, 0.91f)
    }

val DuoPureWhite = Color(0xFFFFFFFF)

val DuoMintSurface: Color
    get() = when (AppThemeController.activeTheme) {
        AppVisualTheme.DUOLINGO -> AppThemeController.lightenTint(AppThemeController.resolvedPrimaryColor, 0.86f)
        AppVisualTheme.GLASSMORPHISM -> AppThemeController.lightenTint(AppThemeController.resolvedPrimaryColor, 0.80f)
        AppVisualTheme.NEUMORPHISM -> AppThemeController.lightenTint(AppThemeController.resolvedPrimaryColor, 0.86f)
        AppVisualTheme.MINIMALIST -> Color(0xFFF4F4F5)
        AppVisualTheme.FLAT_DESIGN -> AppThemeController.lightenTint(AppThemeController.resolvedPrimaryColor, 0.74f)
        AppVisualTheme.BENTO_GLOW -> AppThemeController.lightenTint(AppThemeController.resolvedPrimaryColor, 0.85f)
    }

val DuoSwanBorder: Color
    get() = when (AppThemeController.activeTheme) {
        AppVisualTheme.DUOLINGO -> AppThemeController.lightenTint(AppThemeController.resolvedPrimaryColor, 0.72f)
        AppVisualTheme.GLASSMORPHISM -> AppThemeController.lightenTint(AppThemeController.resolvedPrimaryColor, 0.58f)
        AppVisualTheme.NEUMORPHISM -> AppThemeController.lightenTint(AppThemeController.resolvedPrimaryLipColor, 0.72f)
        AppVisualTheme.MINIMALIST -> Color(0xFFD4D4D8)
        AppVisualTheme.FLAT_DESIGN -> AppThemeController.darkenColor(AppThemeController.resolvedPrimaryColor, 0.80f)
        AppVisualTheme.BENTO_GLOW -> AppThemeController.lightenTint(AppThemeController.resolvedSecondaryAccent, 0.64f)
    }

val DuoHareLip: Color
    get() = when (AppThemeController.activeTheme) {
        AppVisualTheme.DUOLINGO -> AppThemeController.lightenTint(AppThemeController.resolvedPrimaryLipColor, 0.64f)
        AppVisualTheme.GLASSMORPHISM -> AppThemeController.lightenTint(AppThemeController.resolvedPrimaryColor, 0.48f)
        AppVisualTheme.NEUMORPHISM -> AppThemeController.lightenTint(AppThemeController.resolvedPrimaryLipColor, 0.66f)
        AppVisualTheme.MINIMALIST -> Color(0xFFD4D4D8)
        AppVisualTheme.FLAT_DESIGN -> Color.Transparent
        AppVisualTheme.BENTO_GLOW -> AppThemeController.lightenTint(AppThemeController.resolvedSecondaryAccent, 0.56f)
    }

// High-Contrast Ink Hierarchy
val DuoEelInk = Color(0xFF2B303A)
val DuoWolfSubtle = Color(0xFF4B5563)
val DuoMutedInk = Color(0xFF64748B)

// =========================================================================
// MAPPED SEMANTIC TOKENS (Used across all screens — Reactive to Active Theme & Palette)
// =========================================================================

val CosmicVoid: Color
    get() = DuoSnowCanvas

val CosmicObsidian: Color
    get() = when (AppThemeController.activeTheme) {
        AppVisualTheme.DUOLINGO -> DuoPureWhite
        AppVisualTheme.GLASSMORPHISM -> AppThemeController.lightenTint(AppThemeController.resolvedPrimaryColor, 0.95f)
        AppVisualTheme.NEUMORPHISM -> AppThemeController.lightenTint(AppThemeController.resolvedPrimaryColor, 0.90f)
        AppVisualTheme.MINIMALIST -> Color(0xFFFFFFFF)
        AppVisualTheme.FLAT_DESIGN -> AppThemeController.lightenTint(AppThemeController.resolvedPrimaryColor, 0.93f)
        AppVisualTheme.BENTO_GLOW -> AppThemeController.lightenTint(AppThemeController.resolvedPrimaryColor, 0.96f)
    }

val CosmicSurface: Color
    get() = DuoMintSurface

val CosmicCardElevated: Color
    get() = CosmicObsidian

val CosmicBorder: Color
    get() = DuoSwanBorder

val CosmicBorderHairline: Color
    get() = when (AppThemeController.activeTheme) {
        AppVisualTheme.DUOLINGO -> AppThemeController.lightenTint(AppThemeController.resolvedPrimaryColor, 0.76f)
        AppVisualTheme.GLASSMORPHISM -> AppThemeController.lightenTint(AppThemeController.resolvedPrimaryColor, 0.66f)
        AppVisualTheme.NEUMORPHISM -> AppThemeController.lightenTint(AppThemeController.resolvedPrimaryLipColor, 0.76f)
        AppVisualTheme.MINIMALIST -> Color(0xFFE4E4E7)
        AppVisualTheme.FLAT_DESIGN -> AppThemeController.resolvedPrimaryColor
        AppVisualTheme.BENTO_GLOW -> AppThemeController.lightenTint(AppThemeController.resolvedSecondaryAccent, 0.68f)
    }

// Vibrant Accents
val NebulaViolet = DuoBeetlePurpleLip
val NebulaVioletSoft = DuoBeetlePurple
val NebulaIndigo = DuoMacawBlueLip
val StarlightCyan = DuoMacawBlue
val StarlightSky = Color(0xFF38BDF8)
val CosmicMagenta = DuoCardinalRed
val SolarGold = DuoFoxOrange
val StellarYellow = DuoBeeYellow
val AuroraEmerald: Color
    get() = DuoFeatherGreen

// Crisp Charcoal Text Hierarchy on Light Cards
val CosmicTextPure = DuoEelInk
val CosmicTextSubtle = DuoWolfSubtle
val CosmicTextMuted = DuoMutedInk
val CosmicTextDark = DuoEelInk

// Colorful Category & Pill Tokens
val PastelSky = DuoMacawBlue
val PastelSkySoft = DuoMacawBlueSoft
val PastelLavender = DuoBeetlePurpleLip
val PastelLavenderSoft = DuoBeetlePurpleSoft
val PastelPeach = DuoFoxOrange
val PastelPeachSoft = DuoFoxOrangeSoft
val PastelMint: Color
    get() = DuoFeatherGreenLip
val PastelMintSoft: Color
    get() = DuoFeatherGreenSoft
val PastelRose = DuoCardinalRedLip
val PastelRoseSoft = DuoCardinalRedSoft
val PastelYellow = DuoBeeYellowLip
val PastelYellowSoft = DuoBeeYellowSoft

// Tactile Card & Dock Surfaces
val CosmicGlass: Color
    get() = CosmicObsidian
val CosmicGlassSurface: Color
    get() = CosmicObsidian
val CosmicGlassSurfaceDark: Color
    get() = DuoMintSurface
val CosmicPillDock: Color
    get() = CosmicObsidian
val AstrodraconSapphire = DuoMacawBlue

// Theme & Palette-Aware Gradients
val CosmicGradientNebula: Brush
    get() = Brush.linearGradient(
        colors = listOf(DuoFeatherGreen, AppThemeController.resolvedSecondaryAccent)
    )
val CosmicGradientDragonAura: Brush
    get() = Brush.linearGradient(
        colors = listOf(DuoFeatherGreen, AppThemeController.resolvedSecondaryAccent, DuoBeetlePurple)
    )
val CosmicGradientFolderGold = Brush.linearGradient(
    colors = listOf(DuoBeeYellow, DuoFoxOrange)
)
val CosmicGradientDarkCard: Brush
    get() = Brush.verticalGradient(
        colors = listOf(CosmicObsidian, DuoMintSurface)
    )
val CosmicGradientGlassCard: Brush
    get() = when (AppThemeController.activeTheme) {
        AppVisualTheme.DUOLINGO -> Brush.verticalGradient(
            colors = listOf(DuoPureWhite, DuoSnowCanvas)
        )
        AppVisualTheme.GLASSMORPHISM -> Brush.linearGradient(
            colors = listOf(
                Color(0xF2FFFFFF),
                DuoMintSurface.copy(alpha = 0.85f),
                DuoSnowCanvas.copy(alpha = 0.92f)
            )
        )
        AppVisualTheme.NEUMORPHISM -> Brush.linearGradient(
            colors = listOf(
                Color.White.copy(alpha = 0.95f),
                CosmicObsidian,
                DuoMintSurface.copy(alpha = 0.90f)
            )
        )
        AppVisualTheme.MINIMALIST -> Brush.verticalGradient(
            colors = listOf(Color.White, Color.White)
        )
        AppVisualTheme.FLAT_DESIGN -> Brush.verticalGradient(
            colors = listOf(CosmicObsidian, CosmicObsidian)
        )
        AppVisualTheme.BENTO_GLOW -> Brush.verticalGradient(
            colors = listOf(
                Color.White,
                AppThemeController.lightenTint(AppThemeController.resolvedSecondaryAccent, 0.93f)
            )
        )
    }
val CosmicGradientGlassHighlight: Brush
    get() = Brush.linearGradient(
        colors = listOf(
            DuoFeatherGreen.copy(alpha = 0.55f),
            AppThemeController.resolvedSecondaryAccent.copy(alpha = 0.45f),
            DuoBeeYellow.copy(alpha = 0.45f)
        )
    )
val CosmicGradientCta: Brush
    get() = Brush.horizontalGradient(
        colors = listOf(DuoFeatherGreen, AppThemeController.resolvedSecondaryAccent)
    )
val CosmicGradientGoldCta = Brush.horizontalGradient(
    colors = listOf(DuoBeeYellow, DuoFoxOrange)
)
val CosmicGradientEmeraldCta: Brush
    get() = Brush.horizontalGradient(
        colors = listOf(DuoFeatherGreen, AppThemeController.resolvedSecondaryAccent)
    )
val CosmicGradientRoseCta = Brush.horizontalGradient(
    colors = listOf(DuoCardinalRed, DuoFoxOrange)
)
val CosmicNebulaMesh: Brush
    get() = when (AppThemeController.activeTheme) {
        AppVisualTheme.DUOLINGO -> Brush.verticalGradient(
            colors = listOf(DuoSnowCanvas, DuoPureWhite, DuoMintSurface)
        )
        AppVisualTheme.GLASSMORPHISM -> Brush.linearGradient(
            colors = listOf(
                AppThemeController.lightenTint(AppThemeController.resolvedPrimaryColor, 0.78f),
                Color(0xFFE0E7FF),
                AppThemeController.lightenTint(AppThemeController.resolvedSecondaryAccent, 0.80f),
                Color(0xFFFDF2F8)
            )
        )
        AppVisualTheme.NEUMORPHISM -> {
            val neuBase = AppThemeController.lightenTint(AppThemeController.resolvedPrimaryColor, 0.88f)
            Brush.verticalGradient(colors = listOf(neuBase, neuBase))
        }
        AppVisualTheme.MINIMALIST -> Brush.verticalGradient(
            colors = listOf(Color(0xFFFAFAFA), Color(0xFFF4F4F5))
        )
        AppVisualTheme.FLAT_DESIGN -> {
            val flatCanvas = AppThemeController.lightenTint(AppThemeController.resolvedPrimaryColor, 0.82f)
            Brush.verticalGradient(colors = listOf(flatCanvas, flatCanvas))
        }
        AppVisualTheme.BENTO_GLOW -> Brush.linearGradient(
            colors = listOf(
                AppThemeController.lightenTint(AppThemeController.resolvedSecondaryAccent, 0.86f),
                Color(0xFFF8FAFC),
                AppThemeController.lightenTint(AppThemeController.resolvedPrimaryColor, 0.87f)
            )
        )
    }

// Deep Cosmic Splash & Accent Tokens
val DuoGalaxyMidnight = Color(0xFF0B0F1F)
val DuoGalaxyDeepIndigo = Color(0xFF151C38)
val DuoGalaxyNebulaPurple = Color(0xFF9333EA)
val DuoGalaxyCyanGlow = Color(0xFF00F2FE)
val DuoGalaxyArgonPink = Color(0xFFFF3CAC)

// =========================================================================
// BACKWARD-COMPATIBILITY ALIASES (Mori, ZipScanner, ScriptEngine, SecurityGate)
// =========================================================================
val VaultObsidian = DuoEelInk
val VaultCharcoal = Color(0xFF374151)
val VaultSlate: Color
    get() = DuoSwanBorder
val VaultCream: Color
    get() = DuoSnowCanvas
val VaultPureWhite = DuoPureWhite

val VaultLime: Color
    get() = DuoFeatherGreen
val VaultLimeDark: Color
    get() = DuoFeatherGreenLip
val VaultMintCard: Color
    get() = DuoFeatherGreenSoft
val VaultMintAccent: Color
    get() = DuoFeatherGreen

val VaultAmber = DuoFoxOrange
val VaultCoral = DuoCardinalRed
val VaultLavender = DuoBeetlePurple
val VaultLavenderDeep = DuoBeetlePurpleLip

val VaultTextDark = DuoEelInk
val VaultTextMuted = DuoMutedInk
val VaultTextLight = DuoPureWhite
val VaultTextLightMuted = Color(0xFFE2E8F0)

// =========================================================================
// 6-THEME CARD & BUTTON MODIFIERS
// (DUOLINGO / GLASSMORPHISM / NEUMORPHISM / MINIMALIST / FLAT_DESIGN / BENTO_GLOW)
// =========================================================================

fun Modifier.duolingoCard(
    backgroundColor: Color = DuoPureWhite,
    borderColor: Color = DuoSwanBorder,
    lipColor: Color = DuoHareLip,
    cornerRadius: Dp = 22.dp,
    borderWidth: Dp = 2.dp,
    lipHeight: Dp = 4.dp
): Modifier = this
    .then(
        if (AppThemeController.graphicsQualityMode.tierLevel >= 3) {
            com.example.ui.ModifierGsapCardBridge.applyCardReveal()
        } else {
            Modifier
        }
    )
    .then(
        if (AppThemeController.graphicsQualityMode.tierLevel >= 1) {
            com.example.ui.ModifierGsapCardBridge.applyCardMotionGraphicAura(
                accentColor = borderColor,
                cornerRadius = cornerRadius
            )
        } else {
            Modifier
        }
    )
    .drawBehind {
    // Chaos V2 (Ultimate Pro Max Smooth Low — 25x below Smooth): single-pass featherlight draw with identical card colors
    if (AppThemeController.graphicsQualityMode == AppGraphicsQualityMode.CHAOS_ULTIMATE_LOW) {
        val r = cornerRadius.toPx()
        drawRoundRect(
            color = backgroundColor,
            topLeft = Offset.Zero,
            size = size,
            cornerRadius = CornerRadius(r, r)
        )
        drawRoundRect(
            color = borderColor.copy(alpha = 0.72f),
            topLeft = Offset.Zero,
            size = size,
            cornerRadius = CornerRadius(r, r),
            style = Stroke(width = 1.dp.toPx())
        )
        return@drawBehind
    }

    // Base card colors & theme geometry stay 100% consistent from Smooth (1x) to Ultra GSAP (20x)!
    when (AppThemeController.activeTheme) {
        AppVisualTheme.DUOLINGO -> {
            val r = cornerRadius.toPx()
            val lip = lipHeight.toPx()
            val stroke = borderWidth.toPx()

            // 1. Bottom 3D lip shadow
            drawRoundRect(
                color = lipColor,
                topLeft = Offset(0f, 0f),
                size = size,
                cornerRadius = CornerRadius(r, r)
            )

            // 2. Main top card face
            val faceHeight = (size.height - lip).coerceAtLeast(0f)
            drawRoundRect(
                color = backgroundColor,
                topLeft = Offset(0f, 0f),
                size = Size(size.width, faceHeight),
                cornerRadius = CornerRadius(r, r)
            )

            // 3. Crisp outline border around top face
            drawRoundRect(
                color = borderColor,
                topLeft = Offset(stroke / 2f, stroke / 2f),
                size = Size((size.width - stroke).coerceAtLeast(0f), (faceHeight - stroke).coerceAtLeast(0f)),
                cornerRadius = CornerRadius(r, r),
                style = Stroke(width = stroke)
            )
        }

        AppVisualTheme.GLASSMORPHISM -> {
            val r = (cornerRadius + 4.dp).toPx()
            val glowOffset = (lipHeight * 0.65f).toPx()
            val stroke = 1.6.dp.toPx()
            val accentTint = if (borderColor == DuoSwanBorder) {
                AppThemeController.resolvedPrimaryColor
            } else {
                borderColor
            }

            // 1. Soft ambient neon glass aura shadow
            drawRoundRect(
                color = accentTint.copy(alpha = 0.24f),
                topLeft = Offset(0f, glowOffset),
                size = Size(size.width, (size.height - glowOffset).coerceAtLeast(0f)),
                cornerRadius = CornerRadius(r, r)
            )

            // 2. Frosted translucent acrylic base with subtle color tint
            val faceHeight = (size.height - glowOffset * 0.35f).coerceAtLeast(0f)
            val glassBrush = Brush.linearGradient(
                colors = listOf(
                    Color.White.copy(alpha = 0.80f),
                    AppThemeController.lightenTint(accentTint, 0.84f).copy(alpha = 0.72f),
                    Color.White.copy(alpha = 0.84f)
                ),
                start = Offset(0f, 0f),
                end = Offset(size.width, faceHeight)
            )
            drawRoundRect(
                brush = glassBrush,
                topLeft = Offset(0f, 0f),
                size = Size(size.width, faceHeight),
                cornerRadius = CornerRadius(r, r)
            )

            // 3. Corner frosted aurora light orb inside glass card
            val orbRadius = (size.width * 0.38f).coerceAtLeast(16f)
            drawRoundRect(
                brush = Brush.radialGradient(
                    colors = listOf(
                        accentTint.copy(alpha = 0.20f),
                        Color.Transparent
                    ),
                    center = Offset(size.width * 0.88f, faceHeight * 0.16f),
                    radius = orbRadius
                ),
                topLeft = Offset(0f, 0f),
                size = Size(size.width, faceHeight),
                cornerRadius = CornerRadius(r, r)
            )

            // 4. Top-left frosted specular reflection band
            val sheenHeight = (faceHeight * 0.45f).coerceAtLeast(0f)
            if (sheenHeight > 4f) {
                drawRoundRect(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color.White.copy(alpha = 0.68f),
                            Color.White.copy(alpha = 0.0f)
                        )
                    ),
                    topLeft = Offset(stroke, stroke),
                    size = Size((size.width - stroke * 2f).coerceAtLeast(0f), sheenHeight),
                    cornerRadius = CornerRadius(r, r)
                )
            }

            // 5. Luminous dual-rim glass border
            drawRoundRect(
                brush = Brush.linearGradient(
                    colors = listOf(
                        Color.White.copy(alpha = 0.98f),
                        accentTint.copy(alpha = 0.78f),
                        AppThemeController.resolvedSecondaryAccent.copy(alpha = 0.62f)
                    )
                ),
                topLeft = Offset(stroke / 2f, stroke / 2f),
                size = Size((size.width - stroke).coerceAtLeast(0f), (faceHeight - stroke).coerceAtLeast(0f)),
                cornerRadius = CornerRadius(r, r),
                style = Stroke(width = stroke)
            )
        }

        AppVisualTheme.NEUMORPHISM -> {
            val r = (cornerRadius + 2.dp).toPx()
            val bevel = lipHeight.coerceIn(3.dp, 6.dp).toPx()
            val accentBase = if (borderColor == DuoSwanBorder) {
                AppThemeController.resolvedPrimaryColor
            } else {
                borderColor
            }
            val neuSurface = if (backgroundColor == DuoPureWhite || backgroundColor == CosmicObsidian) {
                AppThemeController.lightenTint(accentBase, 0.88f)
            } else {
                AppThemeController.lightenTint(backgroundColor, 0.35f)
            }
            val darkShadow = AppThemeController.darkenColor(neuSurface, 0.78f)
            val lightHighlight = Color.White.copy(alpha = 0.96f)

            val cardW = (size.width - bevel * 1.4f).coerceAtLeast(0f)
            val cardH = (size.height - bevel * 1.4f).coerceAtLeast(0f)
            val originX = bevel * 0.7f
            val originY = bevel * 0.7f

            // 1. Bottom-Right Soft Extruded Dark Shadow
            drawRoundRect(
                color = darkShadow.copy(alpha = 0.56f),
                topLeft = Offset(originX + bevel * 1.0f, originY + bevel * 1.0f),
                size = Size(cardW, cardH),
                cornerRadius = CornerRadius(r, r)
            )
            drawRoundRect(
                color = darkShadow.copy(alpha = 0.88f),
                topLeft = Offset(originX + bevel * 0.55f, originY + bevel * 0.55f),
                size = Size(cardW, cardH),
                cornerRadius = CornerRadius(r, r)
            )

            // 2. Top-Left Soft Extruded Pure Light Highlight
            drawRoundRect(
                color = lightHighlight,
                topLeft = Offset(0f, 0f),
                size = Size(cardW, cardH),
                cornerRadius = CornerRadius(r, r)
            )

            // 3. Soft Matte Tactile Clay/Plastic Card Face (Convex pillow gradient)
            drawRoundRect(
                brush = Brush.linearGradient(
                    colors = listOf(
                        AppThemeController.lightenTint(neuSurface, 0.42f),
                        neuSurface,
                        AppThemeController.darkenColor(neuSurface, 0.94f)
                    ),
                    start = Offset(originX, originY),
                    end = Offset(originX + cardW, originY + cardH)
                ),
                topLeft = Offset(originX, originY),
                size = Size(cardW, cardH),
                cornerRadius = CornerRadius(r, r)
            )

            // 4. Subtle Directional Bevel Inner Rim
            val rimStroke = 1.4.dp.toPx()
            drawRoundRect(
                brush = Brush.linearGradient(
                    colors = listOf(
                        Color.White.copy(alpha = 0.92f),
                        accentBase.copy(alpha = 0.32f),
                        darkShadow.copy(alpha = 0.65f)
                    ),
                    start = Offset(originX, originY),
                    end = Offset(originX + cardW, originY + cardH)
                ),
                topLeft = Offset(originX + rimStroke / 2f, originY + rimStroke / 2f),
                size = Size((cardW - rimStroke).coerceAtLeast(0f), (cardH - rimStroke).coerceAtLeast(0f)),
                cornerRadius = CornerRadius(r, r),
                style = Stroke(width = rimStroke)
            )
        }

        AppVisualTheme.MINIMALIST -> {
            // Ultra-Clean Editorial Minimalist: Crisp 10dp corners, zero bottom lip, 1dp hairline border + left accent bar
            val minCorner = (cornerRadius * 0.48f).coerceIn(6.dp, 12.dp).toPx()
            val hairline = 1.dp.toPx()
            val cleanBg = if (backgroundColor == DuoMintSurface) {
                Color(0xFFF4F4F5)
            } else {
                Color.White
            }
            val accentStroke = if (borderColor == DuoSwanBorder) {
                Color(0xFFD4D4D8)
            } else {
                borderColor.copy(alpha = 0.68f)
            }

            // 1. Pure Flat Editorial Surface (Full height, no 3D lip)
            drawRoundRect(
                color = cleanBg,
                topLeft = Offset.Zero,
                size = size,
                cornerRadius = CornerRadius(minCorner, minCorner)
            )

            // 2. Razor-thin 1dp hairline border
            drawRoundRect(
                color = accentStroke,
                topLeft = Offset(hairline / 2f, hairline / 2f),
                size = Size((size.width - hairline).coerceAtLeast(0f), (size.height - hairline).coerceAtLeast(0f)),
                cornerRadius = CornerRadius(minCorner, minCorner),
                style = Stroke(width = hairline)
            )

            // 3. Minimalist Editorial Left Accent Line
            if (size.height > 22.dp.toPx()) {
                val barColor = if (borderColor == DuoSwanBorder) {
                    AppThemeController.resolvedPrimaryColor.copy(alpha = 0.55f)
                } else {
                    borderColor
                }
                val barW = 3.2.dp.toPx()
                val barH = (size.height * 0.48f).coerceAtLeast(8f)
                drawRoundRect(
                    color = barColor,
                    topLeft = Offset(0f, (size.height - barH) * 0.5f),
                    size = Size(barW, barH),
                    cornerRadius = CornerRadius(barW, barW)
                )
            }
        }

        AppVisualTheme.FLAT_DESIGN -> {
            // Pure 2D Geometric Color-Blocked Flat Design: Zero shadow, zero lip, zero gradient, bold solid fill
            val flatCorner = (cornerRadius * 0.34f).coerceIn(4.dp, 8.dp).toPx()
            val accentBlock = if (borderColor == DuoSwanBorder) {
                AppThemeController.resolvedPrimaryColor
            } else {
                borderColor
            }
            val flatFill = if (backgroundColor == DuoPureWhite || backgroundColor == CosmicObsidian) {
                AppThemeController.lightenTint(accentBlock, 0.87f)
            } else {
                backgroundColor
            }
            val flatStripeH = 5.dp.toPx()

            // 1. Solid 2D Color-Blocked Surface
            drawRoundRect(
                color = flatFill,
                topLeft = Offset.Zero,
                size = size,
                cornerRadius = CornerRadius(flatCorner, flatCorner)
            )

            // 2. Top Geometric 2D Color-Block Header Band
            if (size.height > 24.dp.toPx()) {
                drawRoundRect(
                    color = accentBlock,
                    topLeft = Offset.Zero,
                    size = Size(size.width, flatStripeH),
                    cornerRadius = CornerRadius(flatCorner, flatCorner)
                )
            }
        }

        AppVisualTheme.BENTO_GLOW -> {
            // Modular Bento Grid Glow (iOS 18 Control Center, Linear App & Arc Browser)
            val r = (cornerRadius + 4.dp).toPx()
            val glowDrop = (lipHeight * 0.45f).toPx()
            val stroke = 1.5.dp.toPx()
            val bentoAccent = if (borderColor == DuoSwanBorder) {
                AppThemeController.resolvedPrimaryColor
            } else {
                borderColor
            }
            val secondaryGlow = AppThemeController.resolvedSecondaryAccent

            // 1. Soft Bento Ambient Elevation Glow
            drawRoundRect(
                color = bentoAccent.copy(alpha = 0.16f),
                topLeft = Offset(0f, glowDrop),
                size = Size(size.width, (size.height - glowDrop * 0.5f).coerceAtLeast(0f)),
                cornerRadius = CornerRadius(r, r)
            )

            // 2. Squircle Bento Module Base with subtle Linear/iOS vertical sheen
            val faceH = (size.height - glowDrop * 0.35f).coerceAtLeast(0f)
            val topTint = if (backgroundColor == DuoPureWhite || backgroundColor == CosmicObsidian) {
                Color.White
            } else {
                AppThemeController.lightenTint(backgroundColor, 0.25f)
            }
            val bottomTint = AppThemeController.lightenTint(bentoAccent, 0.91f)
            drawRoundRect(
                brush = Brush.verticalGradient(
                    colors = listOf(topTint, bottomTint),
                    startY = 0f,
                    endY = faceH
                ),
                topLeft = Offset.Zero,
                size = Size(size.width, faceH),
                cornerRadius = CornerRadius(r, r)
            )

            // 3. Arc Browser Radial Ambient Corner Glow inside the Bento Module
            val radialRadius = (size.width * 0.45f).coerceAtLeast(24f)
            drawRoundRect(
                brush = Brush.radialGradient(
                    colors = listOf(
                        bentoAccent.copy(alpha = 0.16f),
                        secondaryGlow.copy(alpha = 0.08f),
                        Color.Transparent
                    ),
                    center = Offset(size.width * 0.16f, 0f),
                    radius = radialRadius
                ),
                topLeft = Offset.Zero,
                size = Size(size.width, faceH),
                cornerRadius = CornerRadius(r, r)
            )

            // 4. Crisp Bento Squircle Perimeter Border
            drawRoundRect(
                brush = Brush.linearGradient(
                    colors = listOf(
                        bentoAccent.copy(alpha = 0.72f),
                        secondaryGlow.copy(alpha = 0.42f),
                        bentoAccent.copy(alpha = 0.28f)
                    ),
                    start = Offset(0f, 0f),
                    end = Offset(size.width, faceH)
                ),
                topLeft = Offset(stroke / 2f, stroke / 2f),
                size = Size((size.width - stroke).coerceAtLeast(0f), (faceH - stroke).coerceAtLeast(0f)),
                cornerRadius = CornerRadius(r, r),
                style = Stroke(width = stroke)
            )

            // 5. Signature Linear App Top-Edge Specular Rim Glow Highlight
            val rimWidth = (size.width * 0.72f).coerceAtLeast(0f)
            val rimHeight = 2.6.dp.toPx()
            if (rimWidth > 12f && faceH > 16f) {
                drawRoundRect(
                    brush = Brush.horizontalGradient(
                        colors = listOf(
                            Color.Transparent,
                            bentoAccent.copy(alpha = 0.95f),
                            secondaryGlow.copy(alpha = 0.90f),
                            Color.Transparent
                        ),
                        startX = size.width * 0.14f,
                        endX = size.width * 0.86f
                    ),
                    topLeft = Offset(size.width * 0.14f, 0.5.dp.toPx()),
                    size = Size(rimWidth, rimHeight),
                    cornerRadius = CornerRadius(rimHeight, rimHeight)
                )
            }
        }
    }
}

fun Modifier.duolingoButton(
    faceColor: Color = DuoFeatherGreen,
    lipColor: Color = DuoFeatherGreenLip,
    cornerRadius: Dp = 16.dp,
    lipHeight: Dp = 4.dp
): Modifier = this
    .then(
        // Unified button visual pipeline across Smooth (1x) -> Balanced (3x) -> High (7x) -> Extreme (12x) -> Ultra GSAP (20x):
        // Base box colors & selective rainbow/accent trims stay 100% identical across all 5 modes;
        // only the GSAP effect density scales 20x from Smooth to Ultra!
        com.example.ui.ModifierGsapCardBridge.applyUltraButtonOverlay(
            faceColor = faceColor,
            lipColor = lipColor,
            cornerRadius = cornerRadius,
            lipHeight = lipHeight
        )
    )

@Suppress("unused")
private fun Modifier.legacyThemeButtonFallback(
    faceColor: Color = DuoFeatherGreen,
    lipColor: Color = DuoFeatherGreenLip,
    cornerRadius: Dp = 16.dp,
    lipHeight: Dp = 4.dp
): Modifier = this.drawBehind {
                when (AppThemeController.activeTheme) {
                    AppVisualTheme.DUOLINGO -> {
                        val r = cornerRadius.toPx()
                        val lip = lipHeight.toPx()

                        // 1. Darker 3D bottom lip
                        drawRoundRect(
                            color = lipColor,
                            topLeft = Offset(0f, 0f),
                            size = size,
                            cornerRadius = CornerRadius(r, r)
                        )

                        // 2. Bright button face
                        val faceHeight = (size.height - lip).coerceAtLeast(0f)
                        drawRoundRect(
                            color = faceColor,
                            topLeft = Offset(0f, 0f),
                            size = Size(size.width, faceHeight),
                            cornerRadius = CornerRadius(r, r)
                        )

                        // 3. Subtle top inner specular highlight for juicy 3D feel
                        val glossHeight = (faceHeight * 0.26f).coerceAtLeast(0f)
                        if (glossHeight > 2f && size.width > 12f) {
                            drawRoundRect(
                                color = Color.White.copy(alpha = 0.16f),
                                topLeft = Offset(r * 0.35f, 2.dp.toPx()),
                                size = Size((size.width - r * 0.7f).coerceAtLeast(0f), glossHeight),
                                cornerRadius = CornerRadius(r * 0.6f, r * 0.6f)
                            )
                        }
                    }

        AppVisualTheme.GLASSMORPHISM -> {
            val r = (cornerRadius + 4.dp).toPx()
            val glow = (lipHeight * 0.6f).toPx()
            val stroke = 1.5.dp.toPx()

            // 1. Soft colored glass outer aura
            drawRoundRect(
                color = faceColor.copy(alpha = 0.34f),
                topLeft = Offset(0f, glow),
                size = Size(size.width, (size.height - glow).coerceAtLeast(0f)),
                cornerRadius = CornerRadius(r, r)
            )

            // 2. Luminous gradient button face
            val faceHeight = (size.height - glow * 0.45f).coerceAtLeast(0f)
            drawRoundRect(
                brush = Brush.linearGradient(
                    colors = listOf(
                        faceColor.copy(alpha = 0.92f),
                        lipColor.copy(alpha = 0.86f)
                    ),
                    start = Offset(0f, 0f),
                    end = Offset(size.width, faceHeight)
                ),
                topLeft = Offset(0f, 0f),
                size = Size(size.width, faceHeight),
                cornerRadius = CornerRadius(r, r)
            )

            // 3. Frosted top glass sheen
            val sheenHeight = (faceHeight * 0.45f).coerceAtLeast(0f)
            if (sheenHeight > 2f) {
                drawRoundRect(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color.White.copy(alpha = 0.46f),
                            Color.White.copy(alpha = 0.0f)
                        )
                    ),
                    topLeft = Offset(stroke, stroke),
                    size = Size((size.width - stroke * 2f).coerceAtLeast(0f), sheenHeight),
                    cornerRadius = CornerRadius(r, r)
                )
            }

            // 4. Crisp glass highlight border
            drawRoundRect(
                color = Color.White.copy(alpha = 0.82f),
                topLeft = Offset(stroke / 2f, stroke / 2f),
                size = Size((size.width - stroke).coerceAtLeast(0f), (faceHeight - stroke).coerceAtLeast(0f)),
                cornerRadius = CornerRadius(r, r),
                style = Stroke(width = stroke)
            )
        }

        AppVisualTheme.NEUMORPHISM -> {
            // Soft Extruded Tactile Pill Button with Top-Left Light Highlight & Bottom-Right Soft Shadow
            val r = (cornerRadius + 3.dp).toPx()
            val bevel = lipHeight.coerceIn(2.dp, 5.dp).toPx()
            val btnW = (size.width - bevel * 1.2f).coerceAtLeast(0f)
            val btnH = (size.height - bevel * 1.2f).coerceAtLeast(0f)
            val ox = bevel * 0.6f
            val oy = bevel * 0.6f

            // 1. Bottom-Right Soft Drop Shadow
            drawRoundRect(
                color = AppThemeController.darkenColor(lipColor, 0.70f).copy(alpha = 0.72f),
                topLeft = Offset(ox + bevel * 0.8f, oy + bevel * 0.8f),
                size = Size(btnW, btnH),
                cornerRadius = CornerRadius(r, r)
            )

            // 2. Top-Left Bright Bevel Highlight
            drawRoundRect(
                color = Color.White.copy(alpha = 0.94f),
                topLeft = Offset(0f, 0f),
                size = Size(btnW, btnH),
                cornerRadius = CornerRadius(r, r)
            )

            // 3. Soft Convex Extruded Button Face
            drawRoundRect(
                brush = Brush.linearGradient(
                    colors = listOf(
                        AppThemeController.lightenTint(faceColor, 0.18f),
                        faceColor,
                        lipColor
                    ),
                    start = Offset(ox, oy),
                    end = Offset(ox + btnW, oy + btnH)
                ),
                topLeft = Offset(ox, oy),
                size = Size(btnW, btnH),
                cornerRadius = CornerRadius(r, r)
            )

            // 4. Soft Inner Bevel Ring
            val rim = 1.2.dp.toPx()
            drawRoundRect(
                brush = Brush.linearGradient(
                    colors = listOf(Color.White.copy(alpha = 0.72f), lipColor.copy(alpha = 0.68f)),
                    start = Offset(ox, oy),
                    end = Offset(ox + btnW, oy + btnH)
                ),
                topLeft = Offset(ox + rim / 2f, oy + rim / 2f),
                size = Size((btnW - rim).coerceAtLeast(0f), (btnH - rim).coerceAtLeast(0f)),
                cornerRadius = CornerRadius(r, r),
                style = Stroke(width = rim)
            )
        }

        AppVisualTheme.MINIMALIST -> {
            // Ultra-Clean Minimalist Button: Zero 3D lip, crisp hairline border, refined corner
            val minCorner = (cornerRadius * 0.52f).coerceIn(6.dp, 11.dp).toPx()
            val hairline = 1.dp.toPx()
            drawRoundRect(
                color = faceColor,
                topLeft = Offset.Zero,
                size = size,
                cornerRadius = CornerRadius(minCorner, minCorner)
            )
            drawRoundRect(
                color = lipColor.copy(alpha = 0.65f),
                topLeft = Offset(hairline / 2f, hairline / 2f),
                size = Size((size.width - hairline).coerceAtLeast(0f), (size.height - hairline).coerceAtLeast(0f)),
                cornerRadius = CornerRadius(minCorner, minCorner),
                style = Stroke(width = hairline)
            )
        }

        AppVisualTheme.FLAT_DESIGN -> {
            // Pure 2D Flat Design Button: Solid 2D color block, zero lip, zero shadow, zero gradient, sharp geometric corner
            val flatCorner = (cornerRadius * 0.30f).coerceIn(4.dp, 8.dp).toPx()
            drawRoundRect(
                color = faceColor,
                topLeft = Offset.Zero,
                size = size,
                cornerRadius = CornerRadius(flatCorner, flatCorner)
            )
        }

        AppVisualTheme.BENTO_GLOW -> {
            // Bento Grid Glow Button (iOS 18 / Linear / Arc style squircle pill with top specular rim light)
            val r = (cornerRadius + 3.dp).toPx()
            val glowDrop = (lipHeight * 0.45f).toPx()
            val stroke = 1.2.dp.toPx()
            val faceH = (size.height - glowDrop * 0.35f).coerceAtLeast(0f)

            // 1. Soft Ambient Glow beneath button
            drawRoundRect(
                color = faceColor.copy(alpha = 0.28f),
                topLeft = Offset(0f, glowDrop),
                size = Size(size.width, (size.height - glowDrop * 0.5f).coerceAtLeast(0f)),
                cornerRadius = CornerRadius(r, r)
            )

            // 2. Smooth Vertical Sheen Bento Button Face
            drawRoundRect(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        AppThemeController.lightenTint(faceColor, 0.14f),
                        faceColor,
                        lipColor
                    ),
                    startY = 0f,
                    endY = faceH
                ),
                topLeft = Offset.Zero,
                size = Size(size.width, faceH),
                cornerRadius = CornerRadius(r, r)
            )

            // 3. Linear Top-Edge Specular Rim Highlight
            val rimW = (size.width * 0.68f).coerceAtLeast(0f)
            val rimH = 2.dp.toPx()
            if (rimW > 8f && faceH > 10f) {
                drawRoundRect(
                    brush = Brush.horizontalGradient(
                        colors = listOf(
                            Color.Transparent,
                            Color.White.copy(alpha = 0.85f),
                            Color.Transparent
                        ),
                        startX = size.width * 0.16f,
                        endX = size.width * 0.84f
                    ),
                    topLeft = Offset(size.width * 0.16f, 1.dp.toPx()),
                    size = Size(rimW, rimH),
                    cornerRadius = CornerRadius(rimH, rimH)
                )
            }

            // 4. Crisp Squircle Inner Rim
            drawRoundRect(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color.White.copy(alpha = 0.62f),
                        lipColor.copy(alpha = 0.50f)
                    )
                ),
                topLeft = Offset(stroke / 2f, stroke / 2f),
                size = Size((size.width - stroke).coerceAtLeast(0f), (faceH - stroke).coerceAtLeast(0f)),
                cornerRadius = CornerRadius(r, r),
                style = Stroke(width = stroke)
            )
        }
    }
}



