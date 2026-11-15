package com.example.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.R

// ============================================================================
// LOCAL BUNDLED GOOGLE FONTS (app/src/main/res/font/*.ttf)
// ============================================================================

val RawFredokaFont = FontFamily(
    Font(R.font.fredoka, FontWeight.Normal),
    Font(R.font.fredoka, FontWeight.Medium),
    Font(R.font.fredoka, FontWeight.SemiBold),
    Font(R.font.fredoka, FontWeight.Bold),
    Font(R.font.fredoka, FontWeight.ExtraBold)
)

val RawNunitoFont = FontFamily(
    Font(R.font.nunito, FontWeight.Normal),
    Font(R.font.nunito, FontWeight.Medium),
    Font(R.font.nunito, FontWeight.SemiBold),
    Font(R.font.nunito, FontWeight.Bold),
    Font(R.font.nunito, FontWeight.ExtraBold)
)

val RawQuicksandFont = FontFamily(
    Font(R.font.quicksand, FontWeight.Normal),
    Font(R.font.quicksand, FontWeight.Medium),
    Font(R.font.quicksand, FontWeight.SemiBold),
    Font(R.font.quicksand, FontWeight.Bold),
    Font(R.font.quicksand, FontWeight.ExtraBold)
)

val RawComfortaaFont = FontFamily(
    Font(R.font.comfortaa, FontWeight.Normal),
    Font(R.font.comfortaa, FontWeight.Medium),
    Font(R.font.comfortaa, FontWeight.SemiBold),
    Font(R.font.comfortaa, FontWeight.Bold),
    Font(R.font.comfortaa, FontWeight.ExtraBold)
)

val RawOutfitFont = FontFamily(
    Font(R.font.outfit, FontWeight.Normal),
    Font(R.font.outfit, FontWeight.Medium),
    Font(R.font.outfit, FontWeight.SemiBold),
    Font(R.font.outfit, FontWeight.Bold),
    Font(R.font.outfit, FontWeight.ExtraBold)
)

val RawPoppinsFont = FontFamily(
    Font(R.font.poppins, FontWeight.Normal),
    Font(R.font.poppins, FontWeight.Medium),
    Font(R.font.poppins, FontWeight.SemiBold),
    Font(R.font.poppins, FontWeight.Bold),
    Font(R.font.poppins, FontWeight.ExtraBold)
)

val RawPlayfairDisplayFont = FontFamily(
    Font(R.font.playfair_display, FontWeight.Normal),
    Font(R.font.playfair_display, FontWeight.Medium),
    Font(R.font.playfair_display, FontWeight.SemiBold),
    Font(R.font.playfair_display, FontWeight.Bold),
    Font(R.font.playfair_display, FontWeight.ExtraBold)
)

val RawRighteousFont = FontFamily(
    Font(R.font.righteous, FontWeight.Normal),
    Font(R.font.righteous, FontWeight.Medium),
    Font(R.font.righteous, FontWeight.SemiBold),
    Font(R.font.righteous, FontWeight.Bold),
    Font(R.font.righteous, FontWeight.ExtraBold)
)

val RawPlusJakartaSansFont = FontFamily(
    Font(R.font.plus_jakarta_sans, FontWeight.Normal),
    Font(R.font.plus_jakarta_sans, FontWeight.Medium),
    Font(R.font.plus_jakarta_sans, FontWeight.SemiBold),
    Font(R.font.plus_jakarta_sans, FontWeight.Bold),
    Font(R.font.plus_jakarta_sans, FontWeight.ExtraBold)
)

val RawSpaceGroteskFont = FontFamily(
    Font(R.font.space_grotesk, FontWeight.Normal),
    Font(R.font.space_grotesk, FontWeight.Medium),
    Font(R.font.space_grotesk, FontWeight.SemiBold),
    Font(R.font.space_grotesk, FontWeight.Bold),
    Font(R.font.space_grotesk, FontWeight.ExtraBold)
)

val RawJetBrainsMonoFont = FontFamily(
    Font(R.font.jetbrains_mono, FontWeight.Normal),
    Font(R.font.jetbrains_mono, FontWeight.Medium),
    Font(R.font.jetbrains_mono, FontWeight.SemiBold),
    Font(R.font.jetbrains_mono, FontWeight.Bold),
    Font(R.font.jetbrains_mono, FontWeight.ExtraBold)
)

val RawMerriweatherFont = FontFamily(
    Font(R.font.merriweather, FontWeight.Normal),
    Font(R.font.merriweather, FontWeight.Medium),
    Font(R.font.merriweather, FontWeight.SemiBold),
    Font(R.font.merriweather, FontWeight.Bold),
    Font(R.font.merriweather, FontWeight.ExtraBold)
)

val RawItaliannoFont = FontFamily(
    Font(R.font.italianno, FontWeight.Normal),
    Font(R.font.italianno, FontWeight.Medium),
    Font(R.font.italianno, FontWeight.SemiBold),
    Font(R.font.italianno, FontWeight.Bold),
    Font(R.font.italianno, FontWeight.ExtraBold)
)

val RawMontserratFont = FontFamily(
    Font(R.font.montserrat, FontWeight.Normal),
    Font(R.font.montserrat, FontWeight.Medium),
    Font(R.font.montserrat, FontWeight.SemiBold),
    Font(R.font.montserrat, FontWeight.Bold),
    Font(R.font.montserrat, FontWeight.ExtraBold)
)

val RawSilkscreenFont = FontFamily(
    Font(R.font.silkscreen, FontWeight.Normal),
    Font(R.font.silkscreen, FontWeight.Medium),
    Font(R.font.silkscreen, FontWeight.SemiBold),
    Font(R.font.silkscreen, FontWeight.Bold),
    Font(R.font.silkscreen, FontWeight.ExtraBold)
)

val RawCinzelDecorativeFont = FontFamily(
    Font(R.font.cinzel_decorative, FontWeight.Normal),
    Font(R.font.cinzel_decorative, FontWeight.Medium),
    Font(R.font.cinzel_decorative, FontWeight.SemiBold),
    Font(R.font.cinzel_decorative, FontWeight.Bold),
    Font(R.font.cinzel_decorative, FontWeight.ExtraBold)
)

val RawCormorantGaramondFont = FontFamily(
    Font(R.font.cormorant_garamond, FontWeight.Normal),
    Font(R.font.cormorant_garamond, FontWeight.Medium),
    Font(R.font.cormorant_garamond, FontWeight.SemiBold),
    Font(R.font.cormorant_garamond, FontWeight.Bold),
    Font(R.font.cormorant_garamond, FontWeight.ExtraBold)
)

// ============================================================================
// 6 EXCLUSIVE APP FONT PRESETS (NORMAL GOOGLE, SERIF, ITALIAN, BOLD, MINECRAFT, THE CAVAS)
// ============================================================================

enum class AppFontPreset(
    val id: String,
    val displayName: String,
    val subtitle: String,
    val badgeText: String,
    val samplePreview: String
) {
    NORMAL_GOOGLE(
        id = "NORMAL_GOOGLE",
        displayName = "Normal Google",
        subtitle = "Font standar Google modern yang bersih, jernih, dan sangat nyaman dibaca",
        badgeText = "GOOGLE SANS",
        samplePreview = "Standar Google Bersih & Jernih"
    ),
    SERIF(
        id = "SERIF",
        displayName = "Serif",
        subtitle = "Tipografi serif klasik formal dengan guratan tegas dan kontras elegan",
        badgeText = "CLASSIC SERIF",
        samplePreview = "Koleksi Arsip Klasik Formal"
    ),
    ITALIAN(
        id = "ITALIAN",
        displayName = "Italian",
        subtitle = "Gaya kaligrafi miring khas Italia yang anggun, luwes, dan berseni",
        badgeText = "ITALIAN SCRIPT",
        samplePreview = "Eleganza Artistica Italiana"
    ),
    BOLD(
        id = "BOLD",
        displayName = "Bold",
        subtitle = "Karakter huruf ekstra tebal, padat, tegas, dan mendominasi layar",
        badgeText = "HEAVY BOLD",
        samplePreview = "TEGAS TEBAL & BERKARAKTER"
    ),
    MINECRAFT(
        id = "MINECRAFT",
        displayName = "Minecraft",
        subtitle = "Font piksel kotak-kotak 8-bit retro ikonik ala dunia blok Minecraft",
        badgeText = "8-BIT PIXEL",
        samplePreview = "CRAFT BLOCK PIXEL 8-BIT"
    ),
    THE_CAVAS(
        id = "THE_CAVAS",
        displayName = "The Cavas",
        subtitle = "Modern & Elegant — serif editorial mewah dengan lekuk ligatur artistik",
        badgeText = "MODERN & ELEGANT",
        samplePreview = "THE CAVAS • Modern & Elegant"
    );

    val headlineFont: FontFamily
        get() = when (this) {
            NORMAL_GOOGLE -> RawPlusJakartaSansFont
            SERIF -> RawPlayfairDisplayFont
            ITALIAN -> RawItaliannoFont
            BOLD -> RawRighteousFont
            MINECRAFT -> RawSilkscreenFont
            THE_CAVAS -> RawCinzelDecorativeFont
        }

    val bodyFont: FontFamily
        get() = when (this) {
            NORMAL_GOOGLE -> RawPlusJakartaSansFont
            SERIF -> RawMerriweatherFont
            ITALIAN -> RawCormorantGaramondFont
            BOLD -> RawMontserratFont
            MINECRAFT -> RawSilkscreenFont
            THE_CAVAS -> RawCormorantGaramondFont
        }

    val monoAccentFont: FontFamily
        get() = when (this) {
            MINECRAFT -> RawSilkscreenFont
            THE_CAVAS -> RawCinzelDecorativeFont
            BOLD -> RawMontserratFont
            else -> RawJetBrainsMonoFont
        }

    companion object {
        val PLAYFUL_ROUNDED: AppFontPreset get() = NORMAL_GOOGLE

        fun fromId(id: String?): AppFontPreset {
            val clean = id?.trim()?.uppercase() ?: return NORMAL_GOOGLE
            return entries.find { it.id.equals(clean, ignoreCase = true) } ?: NORMAL_GOOGLE
        }
    }
}

// Dynamic Font Getters so all screens in the app automatically adopt the user's selected font preset
val SpaceGroteskFontFamily: FontFamily
    get() = AppThemeController.activeFontPreset.headlineFont

val PlusJakartaSansFontFamily: FontFamily
    get() = AppThemeController.activeFontPreset.bodyFont

val JetBrainsMonoFontFamily: FontFamily
    get() = AppThemeController.activeFontPreset.monoAccentFont

val FredokaFontFamily: FontFamily
    get() = AppThemeController.activeFontPreset.headlineFont

val NunitoFontFamily: FontFamily
    get() = AppThemeController.activeFontPreset.bodyFont

fun buildAppTypography(
    preset: AppFontPreset = AppThemeController.activeFontPreset,
    scaleMultiplier: Float = AppThemeController.fontScaleMultiplier
): Typography {
    val safeScale = scaleMultiplier.coerceIn(0.85f, 1.25f)
    val head = preset.headlineFont
    val body = preset.bodyFont
    return Typography(
        displayLarge = TextStyle(
            fontFamily = head,
            fontWeight = FontWeight.Bold,
            fontSize = (34f * safeScale).sp,
            lineHeight = (40f * safeScale).sp,
            letterSpacing = (-0.3).sp
        ),
        displayMedium = TextStyle(
            fontFamily = head,
            fontWeight = FontWeight.Bold,
            fontSize = (28f * safeScale).sp,
            lineHeight = (34f * safeScale).sp,
            letterSpacing = (-0.2).sp
        ),
        displaySmall = TextStyle(
            fontFamily = head,
            fontWeight = FontWeight.Bold,
            fontSize = (24f * safeScale).sp,
            lineHeight = (30f * safeScale).sp,
            letterSpacing = (-0.15).sp
        ),
        headlineLarge = TextStyle(
            fontFamily = head,
            fontWeight = FontWeight.Bold,
            fontSize = (22f * safeScale).sp,
            lineHeight = (28f * safeScale).sp,
            letterSpacing = (-0.1).sp
        ),
        headlineMedium = TextStyle(
            fontFamily = head,
            fontWeight = FontWeight.Bold,
            fontSize = (19f * safeScale).sp,
            lineHeight = (25f * safeScale).sp,
            letterSpacing = (-0.05).sp
        ),
        headlineSmall = TextStyle(
            fontFamily = head,
            fontWeight = FontWeight.SemiBold,
            fontSize = (17f * safeScale).sp,
            lineHeight = (23f * safeScale).sp,
            letterSpacing = 0.sp
        ),
        titleLarge = TextStyle(
            fontFamily = head,
            fontWeight = FontWeight.Bold,
            fontSize = (17f * safeScale).sp,
            lineHeight = (23f * safeScale).sp,
            letterSpacing = 0.sp
        ),
        titleMedium = TextStyle(
            fontFamily = head,
            fontWeight = FontWeight.Bold,
            fontSize = (15f * safeScale).sp,
            lineHeight = (21f * safeScale).sp,
            letterSpacing = 0.05.sp
        ),
        titleSmall = TextStyle(
            fontFamily = head,
            fontWeight = FontWeight.Bold,
            fontSize = (13.5f * safeScale).sp,
            lineHeight = (19f * safeScale).sp,
            letterSpacing = 0.1.sp
        ),
        bodyLarge = TextStyle(
            fontFamily = body,
            fontWeight = FontWeight.Medium,
            fontSize = (14.5f * safeScale).sp,
            lineHeight = (21f * safeScale).sp,
            letterSpacing = 0.1.sp
        ),
        bodyMedium = TextStyle(
            fontFamily = body,
            fontWeight = FontWeight.Medium,
            fontSize = (13f * safeScale).sp,
            lineHeight = (18.5f * safeScale).sp,
            letterSpacing = 0.15.sp
        ),
        bodySmall = TextStyle(
            fontFamily = body,
            fontWeight = FontWeight.Medium,
            fontSize = (11.5f * safeScale).sp,
            lineHeight = (16.5f * safeScale).sp,
            letterSpacing = 0.2.sp
        ),
        labelLarge = TextStyle(
            fontFamily = head,
            fontWeight = FontWeight.Bold,
            fontSize = (12.5f * safeScale).sp,
            lineHeight = (17f * safeScale).sp,
            letterSpacing = 0.25.sp
        ),
        labelMedium = TextStyle(
            fontFamily = body,
            fontWeight = FontWeight.Bold,
            fontSize = (11f * safeScale).sp,
            lineHeight = (15f * safeScale).sp,
            letterSpacing = 0.2.sp
        ),
        labelSmall = TextStyle(
            fontFamily = body,
            fontWeight = FontWeight.SemiBold,
            fontSize = (10f * safeScale).sp,
            lineHeight = (14f * safeScale).sp,
            letterSpacing = 0.25.sp
        )
    )
}

val Typography: Typography
    get() = buildAppTypography()
