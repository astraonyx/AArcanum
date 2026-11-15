package com.example.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

private fun buildColorScheme(
    theme: AppVisualTheme,
    primary: Color,
    primarySoft: Color,
    secondary: Color,
    canvas: Color,
    surface: Color,
    mintSurface: Color,
    border: Color
) = when (theme) {
    AppVisualTheme.DUOLINGO -> lightColorScheme(
        primary = primary,
        onPrimary = DuoPureWhite,
        primaryContainer = primarySoft,
        onPrimaryContainer = DuoEelInk,
        secondary = secondary,
        onSecondary = DuoPureWhite,
        secondaryContainer = DuoMacawBlueSoft,
        onSecondaryContainer = DuoEelInk,
        tertiary = DuoFoxOrange,
        onTertiary = DuoPureWhite,
        tertiaryContainer = DuoBeeYellowSoft,
        onTertiaryContainer = DuoEelInk,
        background = canvas,
        onBackground = DuoEelInk,
        surface = surface,
        onSurface = DuoEelInk,
        surfaceVariant = mintSurface,
        onSurfaceVariant = DuoWolfSubtle,
        outline = border,
        error = DuoCardinalRed,
        onError = DuoPureWhite
    )

    AppVisualTheme.GLASSMORPHISM -> lightColorScheme(
        primary = primary,
        onPrimary = DuoPureWhite,
        primaryContainer = primarySoft,
        onPrimaryContainer = DuoEelInk,
        secondary = secondary,
        onSecondary = DuoPureWhite,
        secondaryContainer = Color(0xFFEDE9FE),
        onSecondaryContainer = DuoEelInk,
        tertiary = Color(0xFF06B6D4),
        onTertiary = DuoPureWhite,
        tertiaryContainer = Color(0xFFCFFAFE),
        onTertiaryContainer = DuoEelInk,
        background = canvas,
        onBackground = DuoEelInk,
        surface = surface,
        onSurface = DuoEelInk,
        surfaceVariant = mintSurface,
        onSurfaceVariant = DuoWolfSubtle,
        outline = border,
        error = DuoCardinalRed,
        onError = DuoPureWhite
    )

    AppVisualTheme.NEUMORPHISM -> lightColorScheme(
        primary = primary,
        onPrimary = Color.White,
        primaryContainer = primarySoft,
        onPrimaryContainer = DuoEelInk,
        secondary = secondary,
        onSecondary = Color.White,
        secondaryContainer = mintSurface,
        onSecondaryContainer = DuoEelInk,
        tertiary = DuoFoxOrange,
        onTertiary = Color.White,
        tertiaryContainer = DuoBeeYellowSoft,
        onTertiaryContainer = DuoEelInk,
        background = canvas,
        onBackground = DuoEelInk,
        surface = surface,
        onSurface = DuoEelInk,
        surfaceVariant = mintSurface,
        onSurfaceVariant = DuoWolfSubtle,
        outline = border,
        error = DuoCardinalRed,
        onError = DuoPureWhite
    )

    AppVisualTheme.MINIMALIST -> lightColorScheme(
        primary = primary,
        onPrimary = Color.White,
        primaryContainer = Color(0xFFF4F4F5),
        onPrimaryContainer = Color(0xFF18181B),
        secondary = secondary,
        onSecondary = Color.White,
        secondaryContainer = Color(0xFFF4F4F5),
        onSecondaryContainer = Color(0xFF18181B),
        tertiary = Color(0xFF52525B),
        onTertiary = Color.White,
        tertiaryContainer = Color(0xFFE4E4E7),
        onTertiaryContainer = Color(0xFF18181B),
        background = Color(0xFFFAFAFA),
        onBackground = Color(0xFF18181B),
        surface = Color.White,
        onSurface = Color(0xFF18181B),
        surfaceVariant = Color(0xFFF4F4F5),
        onSurfaceVariant = Color(0xFF52525B),
        outline = Color(0xFFE4E4E7),
        error = DuoCardinalRed,
        onError = DuoPureWhite
    )

    AppVisualTheme.FLAT_DESIGN -> lightColorScheme(
        primary = primary,
        onPrimary = Color.White,
        primaryContainer = primarySoft,
        onPrimaryContainer = Color(0xFF111827),
        secondary = secondary,
        onSecondary = Color.White,
        secondaryContainer = DuoMacawBlueSoft,
        onSecondaryContainer = Color(0xFF111827),
        tertiary = DuoFoxOrange,
        onTertiary = Color.White,
        tertiaryContainer = DuoBeeYellowSoft,
        onTertiaryContainer = Color(0xFF111827),
        background = canvas,
        onBackground = Color(0xFF111827),
        surface = surface,
        onSurface = Color(0xFF111827),
        surfaceVariant = mintSurface,
        onSurfaceVariant = Color(0xFF1F2937),
        outline = border,
        error = DuoCardinalRed,
        onError = DuoPureWhite
    )

    AppVisualTheme.BENTO_GLOW -> lightColorScheme(
        primary = primary,
        onPrimary = Color.White,
        primaryContainer = primarySoft,
        onPrimaryContainer = DuoEelInk,
        secondary = secondary,
        onSecondary = Color.White,
        secondaryContainer = AppThemeController.lightenTint(secondary, 0.88f),
        onSecondaryContainer = DuoEelInk,
        tertiary = DuoBeetlePurple,
        onTertiary = Color.White,
        tertiaryContainer = DuoBeetlePurpleSoft,
        onTertiaryContainer = DuoEelInk,
        background = canvas,
        onBackground = DuoEelInk,
        surface = surface,
        onSurface = DuoEelInk,
        surfaceVariant = mintSurface,
        onSurfaceVariant = DuoWolfSubtle,
        outline = border,
        error = DuoCardinalRed,
        onError = DuoPureWhite
    )
}

private fun buildShapes(theme: AppVisualTheme) = when (theme) {
    AppVisualTheme.DUOLINGO -> Shapes(
        extraSmall = RoundedCornerShape(10.dp),
        small = RoundedCornerShape(14.dp),
        medium = RoundedCornerShape(20.dp),
        large = RoundedCornerShape(26.dp),
        extraLarge = RoundedCornerShape(32.dp)
    )
    AppVisualTheme.GLASSMORPHISM -> Shapes(
        extraSmall = RoundedCornerShape(12.dp),
        small = RoundedCornerShape(18.dp),
        medium = RoundedCornerShape(24.dp),
        large = RoundedCornerShape(30.dp),
        extraLarge = RoundedCornerShape(36.dp)
    )
    AppVisualTheme.NEUMORPHISM -> Shapes(
        extraSmall = RoundedCornerShape(14.dp),
        small = RoundedCornerShape(18.dp),
        medium = RoundedCornerShape(22.dp),
        large = RoundedCornerShape(28.dp),
        extraLarge = RoundedCornerShape(34.dp)
    )
    AppVisualTheme.MINIMALIST -> Shapes(
        extraSmall = RoundedCornerShape(6.dp),
        small = RoundedCornerShape(8.dp),
        medium = RoundedCornerShape(10.dp),
        large = RoundedCornerShape(12.dp),
        extraLarge = RoundedCornerShape(16.dp)
    )
    AppVisualTheme.FLAT_DESIGN -> Shapes(
        extraSmall = RoundedCornerShape(4.dp),
        small = RoundedCornerShape(6.dp),
        medium = RoundedCornerShape(8.dp),
        large = RoundedCornerShape(8.dp),
        extraLarge = RoundedCornerShape(10.dp)
    )
    AppVisualTheme.BENTO_GLOW -> Shapes(
        extraSmall = RoundedCornerShape(12.dp),
        small = RoundedCornerShape(16.dp),
        medium = RoundedCornerShape(22.dp),
        large = RoundedCornerShape(28.dp),
        extraLarge = RoundedCornerShape(34.dp)
    )
}

@Composable
fun MyApplicationTheme(
    visualTheme: AppVisualTheme = AppThemeController.activeTheme,
    darkTheme: Boolean = false,
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    SideEffect {
        if (AppThemeController.activeTheme != visualTheme) {
            AppThemeController.activeTheme = visualTheme
        }
    }
    // Observe palette, custom hex & font states so MaterialTheme recomposes immediately on any theme/font change
    val _activePalette = AppThemeController.activePalette
    val _customPrimary = AppThemeController.customPrimaryHex
    val _customAccent = AppThemeController.customAccentHex
    val activeFontPreset = AppThemeController.activeFontPreset
    val fontScale = AppThemeController.fontScaleMultiplier

    val primary = DuoFeatherGreen
    val primarySoft = DuoFeatherGreenSoft
    val secondary = AppThemeController.resolvedSecondaryAccent
    val canvas = DuoSnowCanvas
    val surface = CosmicObsidian
    val mintSurface = DuoMintSurface
    val border = DuoSwanBorder

    MaterialTheme(
        colorScheme = buildColorScheme(
            theme = visualTheme,
            primary = primary,
            primarySoft = primarySoft,
            secondary = secondary,
            canvas = canvas,
            surface = surface,
            mintSurface = mintSurface,
            border = border
        ),
        typography = buildAppTypography(activeFontPreset, fontScale),
        shapes = buildShapes(visualTheme),
        content = content
    )
}
