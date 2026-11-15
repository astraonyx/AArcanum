package com.example.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Shuffle
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.AppThemeController
import com.example.ui.theme.CosmicObsidian
import com.example.ui.theme.CosmicTextPure
import com.example.ui.theme.CosmicTextSubtle
import com.example.ui.theme.DuoBeeYellow
import com.example.ui.theme.DuoBeetlePurple
import com.example.ui.theme.DuoCardinalRed
import com.example.ui.theme.DuoEelInk
import com.example.ui.theme.DuoFeatherGreen
import com.example.ui.theme.DuoFeatherGreenLip
import com.example.ui.theme.DuoFoxOrange
import com.example.ui.theme.DuoFoxOrangeLip
import com.example.ui.theme.DuoHareLip
import com.example.ui.theme.DuoMacawBlue
import com.example.ui.theme.DuoMacawBlueLip
import com.example.ui.theme.DuoMintSurface
import com.example.ui.theme.DuoPureWhite
import com.example.ui.theme.DuoSwanBorder
import com.example.ui.theme.JetBrainsMonoFontFamily
import com.example.ui.theme.MascotAccessory
import com.example.ui.theme.MascotActivityPose
import com.example.ui.theme.MascotAuraStyle
import com.example.ui.theme.MascotBodySkin
import com.example.ui.theme.MascotCostumePreset
import com.example.ui.theme.MascotEmotionPreset
import com.example.ui.theme.MascotHandGloves
import com.example.ui.theme.MascotRoomBedSofaStyle
import com.example.ui.theme.MascotRoomClosetStyle
import com.example.ui.theme.MascotRoomDeskStyle
import com.example.ui.theme.MascotRoomFloorStyle
import com.example.ui.theme.MascotRoomLampStyle
import com.example.ui.theme.MascotRoomPetStyle
import com.example.ui.theme.MascotRoomRugStyle
import com.example.ui.theme.MascotRoomTheme
import com.example.ui.theme.MascotRoomWallStyle
import com.example.ui.theme.MascotRoomWindowDecorStyle
import com.example.ui.theme.MascotUpperOutfit
import com.example.ui.theme.duolingoButton
import com.example.ui.theme.duolingoCard
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.sin

// ============================================================================
// 1. SKIN MATERIAL COLOR & TEXTURE ENGINE (34 DETAILED 3D SKINS — 0 DUPLICATES)
// ============================================================================

internal data class MascotResolvedSkinPalette(
    val bodyGradientColors: List<Color>,
    val lipColor: Color,
    val tabColors: List<Color>,
    val handColor: Color,
    val handLipColor: Color
)

internal fun resolveMascotSkinPalette(
    skin: MascotBodySkin,
    defaultBodyGradient: List<Color>,
    defaultLip: Color,
    defaultTab: List<Color>,
    defaultHand: Color,
    defaultHandLip: Color
): MascotResolvedSkinPalette {
    return when (skin) {
        MascotBodySkin.DEFAULT_MOOD -> MascotResolvedSkinPalette(
            bodyGradientColors = defaultBodyGradient,
            lipColor = defaultLip,
            tabColors = defaultTab,
            handColor = defaultHand,
            handLipColor = defaultHandLip
        )
        MascotBodySkin.ROYAL_GOLD_24K -> MascotResolvedSkinPalette(
            bodyGradientColors = listOf(Color(0xFFFEF08A), Color(0xFFFACC15), Color(0xFFD97706)),
            lipColor = Color(0xFF92400E),
            tabColors = listOf(Color(0xFFF59E0B), Color(0xFFB45309)),
            handColor = Color(0xFFFACC15),
            handLipColor = Color(0xFFB45309)
        )
        MascotBodySkin.DIAMOND_ICE_CRYSTAL -> MascotResolvedSkinPalette(
            bodyGradientColors = listOf(Color(0xFFE0F2FE), Color(0xFF7DD3FC), Color(0xFF0284C7)),
            lipColor = Color(0xFF0369A1),
            tabColors = listOf(Color(0xFF38BDF8), Color(0xFF0284C7)),
            handColor = Color(0xFF7DD3FC),
            handLipColor = Color(0xFF0284C7)
        )
        MascotBodySkin.MECHA_TITANIUM -> MascotResolvedSkinPalette(
            bodyGradientColors = listOf(Color(0xFFF1F5F9), Color(0xFF94A3B8), Color(0xFF475569)),
            lipColor = Color(0xFF1E293B),
            tabColors = listOf(Color(0xFF0EA5E9), Color(0xFF0369A1)),
            handColor = Color(0xFFCBD5E1),
            handLipColor = Color(0xFF334155)
        )
        MascotBodySkin.NEBULA_GALAXY -> MascotResolvedSkinPalette(
            bodyGradientColors = listOf(Color(0xFFC084FC), Color(0xFF7C3AED), Color(0xFF311068)),
            lipColor = Color(0xFF1E1B4B),
            tabColors = listOf(Color(0xFFEC4899), Color(0xFF6366F1)),
            handColor = Color(0xFFA855F7),
            handLipColor = Color(0xFF4C1D95)
        )
        MascotBodySkin.LAVA_MAGMA -> MascotResolvedSkinPalette(
            bodyGradientColors = listOf(Color(0xFFFB923C), Color(0xFFDC2626), Color(0xFF450A0A)),
            lipColor = Color(0xFF270505),
            tabColors = listOf(Color(0xFFFACC15), Color(0xFFEA580C)),
            handColor = Color(0xFFEF4444),
            handLipColor = Color(0xFF7F1D1D)
        )
        MascotBodySkin.CYBER_EMERALD -> MascotResolvedSkinPalette(
            bodyGradientColors = listOf(Color(0xFF6EE7B7), Color(0xFF10B981), Color(0xFF065F46)),
            lipColor = Color(0xFF064E3B),
            tabColors = listOf(Color(0xFF34D399), Color(0xFF059669)),
            handColor = Color(0xFF10B981),
            handLipColor = Color(0xFF047857)
        )
        MascotBodySkin.SAKURA_MOCHI -> MascotResolvedSkinPalette(
            bodyGradientColors = listOf(Color(0xFFFCE7F3), Color(0xFFF472B6), Color(0xFFDB2777)),
            lipColor = Color(0xFF9D174D),
            tabColors = listOf(Color(0xFFF9A8D4), Color(0xFFEC4899)),
            handColor = Color(0xFFF9A8D4),
            handLipColor = Color(0xFFBE185D)
        )
        MascotBodySkin.PHANTOM_HOLOGRAM -> MascotResolvedSkinPalette(
            bodyGradientColors = listOf(Color(0xFFA5F3FC), Color(0xFF22D3EE), Color(0xFF4F46E5)),
            lipColor = Color(0xFF312E81),
            tabColors = listOf(Color(0xFF06B6D4), Color(0xFF6366F1)),
            handColor = Color(0xFF67E8F9),
            handLipColor = Color(0xFF0E7490)
        )
        MascotBodySkin.OBSIDIAN_STEALTH -> MascotResolvedSkinPalette(
            bodyGradientColors = listOf(Color(0xFF475569), Color(0xFF1E293B), Color(0xFF0F172A)),
            lipColor = Color(0xFF020617),
            tabColors = listOf(Color(0xFFF59E0B), Color(0xFFB45309)),
            handColor = Color(0xFF334155),
            handLipColor = Color(0xFF0F172A)
        )
        MascotBodySkin.RUBY_CRIMSON_GEM -> MascotResolvedSkinPalette(
            bodyGradientColors = listOf(Color(0xFFFECDD3), Color(0xFFE11D48), Color(0xFF881337)),
            lipColor = Color(0xFF4C0519),
            tabColors = listOf(Color(0xFFFB7185), Color(0xFFBE123C)),
            handColor = Color(0xFFF43F5E),
            handLipColor = Color(0xFF9F1239)
        )
        MascotBodySkin.AMETHYST_ROYAL -> MascotResolvedSkinPalette(
            bodyGradientColors = listOf(Color(0xFFE9D5FF), Color(0xFF9333EA), Color(0xFF4C1D95)),
            lipColor = Color(0xFF2E1065),
            tabColors = listOf(Color(0xFFC084FC), Color(0xFF7E22CE)),
            handColor = Color(0xFFA855F7),
            handLipColor = Color(0xFF581C87)
        )
        MascotBodySkin.SOLAR_SUNBURST -> MascotResolvedSkinPalette(
            bodyGradientColors = listOf(Color(0xFFFEF08A), Color(0xFFF97316), Color(0xFFC2410C)),
            lipColor = Color(0xFF7C2D12),
            tabColors = listOf(Color(0xFFFDE047), Color(0xFFEA580C)),
            handColor = Color(0xFFFB923C),
            handLipColor = Color(0xFF9A3412)
        )
        MascotBodySkin.BIOLUM_OCEAN -> MascotResolvedSkinPalette(
            bodyGradientColors = listOf(Color(0xFF99F6E4), Color(0xFF0D9488), Color(0xFF1E3A8A)),
            lipColor = Color(0xFF172554),
            tabColors = listOf(Color(0xFF2DD4BF), Color(0xFF0284C7)),
            handColor = Color(0xFF14B8A6),
            handLipColor = Color(0xFF115E59)
        )
        MascotBodySkin.FOREST_SPIRIT -> MascotResolvedSkinPalette(
            bodyGradientColors = listOf(Color(0xFFBBF7D0), Color(0xFF22C55E), Color(0xFF14532D)),
            lipColor = Color(0xFF052E16),
            tabColors = listOf(Color(0xFF4ADE80), Color(0xFF15803D)),
            handColor = Color(0xFF22C55E),
            handLipColor = Color(0xFF166534)
        )
        MascotBodySkin.RAINBOW_PRISM_RGB -> MascotResolvedSkinPalette(
            bodyGradientColors = listOf(Color(0xFFEC4899), Color(0xFF8B5CF6), Color(0xFF06B6D4)),
            lipColor = Color(0xFF312E81),
            tabColors = listOf(Color(0xFFFACC15), Color(0xFFEC4899)),
            handColor = Color(0xFF38BDF8),
            handLipColor = Color(0xFF4F46E5)
        )
        MascotBodySkin.STEAMPUNK_BRONZE -> MascotResolvedSkinPalette(
            bodyGradientColors = listOf(Color(0xFFFDE68A), Color(0xFFD97706), Color(0xFF78350F)),
            lipColor = Color(0xFF451A03),
            tabColors = listOf(Color(0xFFF59E0B), Color(0xFF92400E)),
            handColor = Color(0xFFD97706),
            handLipColor = Color(0xFF78350F)
        )
        MascotBodySkin.CELESTIAL_MARBLE -> MascotResolvedSkinPalette(
            bodyGradientColors = listOf(Color(0xFFFFFFFF), Color(0xFFF1F5F9), Color(0xFFCBD5E1)),
            lipColor = Color(0xFF64748B),
            tabColors = listOf(Color(0xFFFDE047), Color(0xFFD97706)),
            handColor = Color(0xFFF8FAFC),
            handLipColor = Color(0xFF94A3B8)
        )
        MascotBodySkin.VOID_SHADOW -> MascotResolvedSkinPalette(
            bodyGradientColors = listOf(Color(0xFF4C1D95), Color(0xFF1E1B4B), Color(0xFF090D16)),
            lipColor = Color(0xFF020617),
            tabColors = listOf(Color(0xFFA855F7), Color(0xFF3B0764)),
            handColor = Color(0xFF311068),
            handLipColor = Color(0xFF090D16)
        )
        MascotBodySkin.COTTON_CANDY -> MascotResolvedSkinPalette(
            bodyGradientColors = listOf(Color(0xFFFBCFE8), Color(0xFFF472B6), Color(0xFF38BDF8)),
            lipColor = Color(0xFF0284C7),
            tabColors = listOf(Color(0xFF7DD3FC), Color(0xFFEC4899)),
            handColor = Color(0xFFF9A8D4),
            handLipColor = Color(0xFF0284C7)
        )
        MascotBodySkin.CARBON_KEVLAR -> MascotResolvedSkinPalette(
            bodyGradientColors = listOf(Color(0xFF64748B), Color(0xFF334155), Color(0xFF0F172A)),
            lipColor = Color(0xFF020617),
            tabColors = listOf(Color(0xFF38BDF8), Color(0xFF1E293B)),
            handColor = Color(0xFF475569),
            handLipColor = Color(0xFF0F172A)
        )
        MascotBodySkin.EMPEROR_JADE -> MascotResolvedSkinPalette(
            bodyGradientColors = listOf(Color(0xFFA7F3D0), Color(0xFF059669), Color(0xFF064E3B)),
            lipColor = Color(0xFF022C22),
            tabColors = listOf(Color(0xFFFACC15), Color(0xFF047857)),
            handColor = Color(0xFF10B981),
            handLipColor = Color(0xFF064E3B)
        )
        MascotBodySkin.DRAGON_SCALE_RUBY -> MascotResolvedSkinPalette(
            bodyGradientColors = listOf(Color(0xFFFCA5A5), Color(0xFFDC2626), Color(0xFF7F1D1D)),
            lipColor = Color(0xFF450A0A),
            tabColors = listOf(Color(0xFFFACC15), Color(0xFFB91C1C)),
            handColor = Color(0xFFEF4444),
            handLipColor = Color(0xFF7F1D1D)
        )
        MascotBodySkin.GUNDAM_AERO_COMPOSITE -> MascotResolvedSkinPalette(
            bodyGradientColors = listOf(Color(0xFFF8FAFC), Color(0xFF3B82F6), Color(0xFF1D4ED8)),
            lipColor = Color(0xFF1E3A8A),
            tabColors = listOf(Color(0xFFFACC15), Color(0xFFEF4444)),
            handColor = Color(0xFF60A5FA),
            handLipColor = Color(0xFF1E40AF)
        )
        MascotBodySkin.CYBERPUNK_NEON_CIRCUIT -> MascotResolvedSkinPalette(
            bodyGradientColors = listOf(Color(0xFF22D3EE), Color(0xFF0F172A), Color(0xFF020617)),
            lipColor = Color(0xFF00F2FE),
            tabColors = listOf(Color(0xFFFACC15), Color(0xFF06B6D4)),
            handColor = Color(0xFF1E293B),
            handLipColor = Color(0xFF00F2FE)
        )
        MascotBodySkin.CELESTIAL_SERAPH_FEATHER -> MascotResolvedSkinPalette(
            bodyGradientColors = listOf(Color(0xFFFFFFFF), Color(0xFFFEF08A), Color(0xFFF59E0B)),
            lipColor = Color(0xFFB45309),
            tabColors = listOf(Color(0xFF38BDF8), Color(0xFFFACC15)),
            handColor = Color(0xFFFEF9C3),
            handLipColor = Color(0xFFD97706)
        )
        MascotBodySkin.ABYSSAL_DEMON_CHITIN -> MascotResolvedSkinPalette(
            bodyGradientColors = listOf(Color(0xFF991B1B), Color(0xFF31102F), Color(0xFF090D16)),
            lipColor = Color(0xFF450A0A),
            tabColors = listOf(Color(0xFFEF4444), Color(0xFF7F1D1D)),
            handColor = Color(0xFF7F1D1D),
            handLipColor = Color(0xFF090D16)
        )
        MascotBodySkin.ROYAL_DAMASCUS_GOLD -> MascotResolvedSkinPalette(
            bodyGradientColors = listOf(Color(0xFFE2E8F0), Color(0xFF64748B), Color(0xFF1E293B)),
            lipColor = Color(0xFF0F172A),
            tabColors = listOf(Color(0xFF94A3B8), Color(0xFF334155)),
            handColor = Color(0xFF94A3B8),
            handLipColor = Color(0xFF1E293B)
        )
        MascotBodySkin.ASTRAL_CONSTELLATION_MAP -> MascotResolvedSkinPalette(
            bodyGradientColors = listOf(Color(0xFF818CF8), Color(0xFF312E81), Color(0xFF0F172A)),
            lipColor = Color(0xFF1E1B4B),
            tabColors = listOf(Color(0xFFFDE047), Color(0xFF4F46E5)),
            handColor = Color(0xFF6366F1),
            handLipColor = Color(0xFF312E81)
        )
        MascotBodySkin.SAMURAI_LACQUER_ARMOR -> MascotResolvedSkinPalette(
            bodyGradientColors = listOf(Color(0xFFEF4444), Color(0xFF7F1D1D), Color(0xFF111827)),
            lipColor = Color(0xFF090D16),
            tabColors = listOf(Color(0xFFFACC15), Color(0xFF991B1B)),
            handColor = Color(0xFFB91C1C),
            handLipColor = Color(0xFF111827)
        )
        MascotBodySkin.GLACIER_AURORA_PRISM -> MascotResolvedSkinPalette(
            bodyGradientColors = listOf(Color(0xFF6EE7B7), Color(0xFF38BDF8), Color(0xFF8B5CF6)),
            lipColor = Color(0xFF1E3A8A),
            tabColors = listOf(Color(0xFF34D399), Color(0xFF6366F1)),
            handColor = Color(0xFF38BDF8),
            handLipColor = Color(0xFF1D4ED8)
        )
        MascotBodySkin.THUNDER_PLASMA_COIL -> MascotResolvedSkinPalette(
            bodyGradientColors = listOf(Color(0xFFFEF08A), Color(0xFF38BDF8), Color(0xFF1E3A8A)),
            lipColor = Color(0xFF172554),
            tabColors = listOf(Color(0xFFFACC15), Color(0xFF0284C7)),
            handColor = Color(0xFF38BDF8),
            handLipColor = Color(0xFF1E40AF)
        )
        MascotBodySkin.PLUSH_VELVET_QUILT -> MascotResolvedSkinPalette(
            bodyGradientColors = listOf(Color(0xFFF472B6), Color(0xFFBE185D), Color(0xFF831843)),
            lipColor = Color(0xFF500724),
            tabColors = listOf(Color(0xFFFACC15), Color(0xFF9D174D)),
            handColor = Color(0xFFEC4899),
            handLipColor = Color(0xFF831843)
        )
        MascotBodySkin.BIO_MECH_SYMBIOTE -> MascotResolvedSkinPalette(
            bodyGradientColors = listOf(Color(0xFF4ADE80), Color(0xFF065F46), Color(0xFF0F172A)),
            lipColor = Color(0xFF022C22),
            tabColors = listOf(Color(0xFF22C55E), Color(0xFF064E3B)),
            handColor = Color(0xFF10B981),
            handLipColor = Color(0xFF064E3B)
        )
        MascotBodySkin.SUN_BREATHING_SCAR_SKIN -> MascotResolvedSkinPalette(
            bodyGradientColors = listOf(Color(0xFFFEF08A), Color(0xFFF97316), Color(0xFFDC2626)),
            lipColor = Color(0xFF7F1D1D),
            tabColors = listOf(Color(0xFFEF4444), Color(0xFF991B1B)),
            handColor = Color(0xFFF97316),
            handLipColor = Color(0xFF991B1B)
        )
        MascotBodySkin.TITAN_STEAM_MUSCLE_SKIN -> MascotResolvedSkinPalette(
            bodyGradientColors = listOf(Color(0xFFFCA5A5), Color(0xFFDC2626), Color(0xFF7F1D1D)),
            lipColor = Color(0xFF450A0A),
            tabColors = listOf(Color(0xFFF97316), Color(0xFF991B1B)),
            handColor = Color(0xFFEF4444),
            handLipColor = Color(0xFF7F1D1D)
        )
        MascotBodySkin.HOLLOW_SPIRIT_REIATSU -> MascotResolvedSkinPalette(
            bodyGradientColors = listOf(Color(0xFF1E293B), Color(0xFF090D16), Color(0xFF881337)),
            lipColor = Color(0xFFE11D48),
            tabColors = listOf(Color(0xFFF43F5E), Color(0xFF111827)),
            handColor = Color(0xFFE11D48),
            handLipColor = Color(0xFF4C0519)
        )
        MascotBodySkin.NINE_TAILS_CHAKRA_CLOAK -> MascotResolvedSkinPalette(
            bodyGradientColors = listOf(Color(0xFFFEF08A), Color(0xFFFACC15), Color(0xFFEA580C)),
            lipColor = Color(0xFF9A3412),
            tabColors = listOf(Color(0xFFFDE047), Color(0xFFD97706)),
            handColor = Color(0xFFFACC15),
            handLipColor = Color(0xFFB45309)
        )
        MascotBodySkin.TIME_CHRONO_GRID_SKIN -> MascotResolvedSkinPalette(
            bodyGradientColors = listOf(Color(0xFFE0F2FE), Color(0xFF0284C7), Color(0xFF0F172A)),
            lipColor = Color(0xFF1E3A8A),
            tabColors = listOf(Color(0xFFFACC15), Color(0xFF0369A1)),
            handColor = Color(0xFF38BDF8),
            handLipColor = Color(0xFF0369A1)
        )
        MascotBodySkin.SOUL_RING_DIVINE_GOLD -> MascotResolvedSkinPalette(
            bodyGradientColors = listOf(Color(0xFFFEF9C3), Color(0xFF38BDF8), Color(0xFF0369A1)),
            lipColor = Color(0xFF1E3A8A),
            tabColors = listOf(Color(0xFFFACC15), Color(0xFF0284C7)),
            handColor = Color(0xFFFACC15),
            handLipColor = Color(0xFFB45309)
        )
        MascotBodySkin.HEAVENLY_FLAME_LOTUS_SKIN -> MascotResolvedSkinPalette(
            bodyGradientColors = listOf(Color(0xFF6EE7B7), Color(0xFFA855F7), Color(0xFF4C1D95)),
            lipColor = Color(0xFF2E1065),
            tabColors = listOf(Color(0xFF10B981), Color(0xFF7C3AED)),
            handColor = Color(0xFF34D399),
            handLipColor = Color(0xFF065F46)
        )
        MascotBodySkin.NANO_HEX_TITANIUM_RED -> MascotResolvedSkinPalette(
            bodyGradientColors = listOf(Color(0xFFEF4444), Color(0xFFB91C1C), Color(0xFF7F1D1D)),
            lipColor = Color(0xFFF59E0B),
            tabColors = listOf(Color(0xFFFACC15), Color(0xFFD97706)),
            handColor = Color(0xFFDC2626),
            handLipColor = Color(0xFFB45309)
        )
        MascotBodySkin.VIBRANIUM_KINETIC_WEAVE -> MascotResolvedSkinPalette(
            bodyGradientColors = listOf(Color(0xFF334155), Color(0xFF0F172A), Color(0xFF3B0764)),
            lipColor = Color(0xFFA855F7),
            tabColors = listOf(Color(0xFFC084FC), Color(0xFF1E1B4B)),
            handColor = Color(0xFF1E293B),
            handLipColor = Color(0xFF7E22CE)
        )
        MascotBodySkin.ELVEN_STARLIGHT_RUNE -> MascotResolvedSkinPalette(
            bodyGradientColors = listOf(Color(0xFFA7F3D0), Color(0xFF10B981), Color(0xFF064E3B)),
            lipColor = Color(0xFF022C22),
            tabColors = listOf(Color(0xFFFEF08A), Color(0xFF059669)),
            handColor = Color(0xFF34D399),
            handLipColor = Color(0xFF047857)
        )
    }
}

// ============================================================================
// 2. 16 RADICALLY DISTINCT MULTI-LAYERED MASCOT AURA RENDERER (0 SHARED ORBITS!)
// ============================================================================

internal fun DrawScope.drawMascotDynamicAuraLayer(
    cx: Float,
    cy: Float,
    r: Float,
    timeSec: Float,
    auraStyle: MascotAuraStyle,
    defaultPrimary: Color,
    defaultSecondary: Color
) {
    if (auraStyle == MascotAuraStyle.NONE) return

    val pulse = 0.5f + 0.5f * sin(timeSec * 3.0f)
    val pCol = auraStyle.primaryColor
    val sCol = auraStyle.secondaryColor

    // Soft outer ambient halo tailored to each aura's palette
    drawCircle(
        brush = Brush.radialGradient(
            colors = listOf(
                pCol.copy(alpha = 0.30f + pulse * 0.12f),
                sCol.copy(alpha = 0.14f),
                Color.Transparent
            ),
            center = Offset(cx, cy),
            radius = r * 1.68f
        ),
        radius = r * 1.68f,
        center = Offset(cx, cy)
    )

    when (auraStyle) {
        MascotAuraStyle.NONE -> Unit

        MascotAuraStyle.COSMIC_OCTAGON -> {
            // 1. Twin Counter-Rotating 3D Gyroscope Astrolabe Rings + 4 Corner Prism Satellites + Ringed Planet Saturn
            val outerOrbitR = r * (1.30f + pulse * 0.05f)
            rotate(degrees = (timeSec * 28f) % 360f, pivot = Offset(cx, cy)) {
                val octPath = Path().apply {
                    for (i in 0 until 8) {
                        val angle = (i * 45f) * (PI.toFloat() / 180f)
                        val px = cx + cos(angle) * outerOrbitR
                        val py = cy + sin(angle) * outerOrbitR
                        if (i == 0) moveTo(px, py) else lineTo(px, py)
                    }
                    close()
                }
                drawPath(
                    path = octPath,
                    brush = Brush.sweepGradient(
                        listOf(defaultPrimary, defaultSecondary, DuoBeeYellow, defaultPrimary),
                        center = Offset(cx, cy)
                    ),
                    style = Stroke(width = (r * 0.045f).coerceAtLeast(2f), cap = StrokeCap.Round)
                )
            }
            // Tilted X-Cross Gyroscope Elliptical Orbits
            for (tilt in listOf(-32f, 32f)) {
                rotate(degrees = tilt + (timeSec * 40f * (if (tilt > 0) 1f else -1f)) % 360f, pivot = Offset(cx, cy)) {
                    drawOval(
                        brush = Brush.linearGradient(listOf(defaultSecondary, DuoBeeYellow, defaultPrimary)),
                        topLeft = Offset(cx - r * 1.48f, cy - r * 0.44f),
                        size = Size(r * 2.96f, r * 0.88f),
                        style = Stroke(width = (r * 0.035f).coerceAtLeast(1.6f))
                    )
                }
            }
            // Miniature Ringed Planet Saturn at Top-Left & Crescent Moon at Bottom-Right
            val satX = cx - r * 1.18f
            val satY = cy - r * 0.92f
            drawCircle(color = Color(0xFFF59E0B), radius = r * 0.16f, center = Offset(satX, satY))
            rotate(degrees = -24f, pivot = Offset(satX, satY)) {
                drawOval(
                    color = Color(0xFFFEF08A),
                    topLeft = Offset(satX - r * 0.32f, satY - r * 0.07f),
                    size = Size(r * 0.64f, r * 0.14f),
                    style = Stroke(width = (r * 0.035f).coerceAtLeast(1.6f))
                )
            }
            val moonX = cx + r * 1.18f
            val moonY = cy + r * 0.86f
            drawCircle(color = Color(0xFF38BDF8), radius = r * 0.15f, center = Offset(moonX, moonY))
            drawCircle(color = Color(0xFF0F172A), radius = r * 0.12f, center = Offset(moonX + r * 0.06f, moonY - r * 0.04f))
        }

        MascotAuraStyle.SUPER_SAIYAN_GOLD -> {
            // 2. Violent Upward-Erupting Golden Ki Pillar + Cracked Ground Shockwave Crater + Rising Pebbles + Blue Bio-Lightning
            val groundY = cy + r * 1.06f
            drawOval(
                color = Color(0xFFF59E0B).copy(alpha = 0.55f),
                topLeft = Offset(cx - r * 1.38f, groundY - r * 0.18f),
                size = Size(r * 2.76f, r * 0.36f)
            )
            drawOval(
                color = Color(0xFFFEF08A),
                topLeft = Offset(cx - r * 1.38f, groundY - r * 0.18f),
                size = Size(r * 2.76f, r * 0.36f),
                style = Stroke(width = (r * 0.045f).coerceAtLeast(2f))
            )
            // Twin Flaming Golden Ki Shroud Erupting Vertically (Pointed Crown)
            for (layer in 0..1) {
                val scale = if (layer == 0) 1.40f else 1.18f
                val col = if (layer == 0) Color(0xFFF59E0B).copy(alpha = 0.50f) else Color(0xFFFEF08A).copy(alpha = 0.78f)
                val kiPath = Path().apply {
                    moveTo(cx - r * 1.12f * scale, groundY)
                    lineTo(cx - r * 1.28f * scale, cy + r * 0.20f)
                    lineTo(cx - r * 0.96f * scale, cy + r * 0.30f)
                    lineTo(cx - r * 1.22f * scale, cy - r * 0.45f)
                    lineTo(cx - r * 0.82f * scale, cy - r * 0.32f)
                    lineTo(cx - r * 0.92f * scale, cy - r * 1.15f)
                    lineTo(cx - r * 0.42f * scale, cy - r * 0.88f)
                    lineTo(cx, cy - r * (1.48f + pulse * 0.18f) * scale * 0.82f)
                    lineTo(cx + r * 0.42f * scale, cy - r * 0.88f)
                    lineTo(cx + r * 0.92f * scale, cy - r * 1.15f)
                    lineTo(cx + r * 0.82f * scale, cy - r * 0.32f)
                    lineTo(cx + r * 1.22f * scale, cy - r * 0.45f)
                    lineTo(cx + r * 0.96f * scale, cy + r * 0.30f)
                    lineTo(cx + r * 1.28f * scale, cy + r * 0.20f)
                    lineTo(cx + r * 1.12f * scale, groundY)
                    close()
                }
                drawPath(kiPath, color = col)
                drawPath(kiPath, color = Color.White.copy(alpha = 0.72f), style = Stroke(width = 2f))
            }
            // 6 Levitating Crater Rocks rising upward along the sides
            for (rk in 0..5) {
                val side = if (rk % 2 == 0) -1f else 1f
                val rx = cx + side * r * (1.05f + (rk % 3) * 0.16f)
                val rise = ((timeSec * 55f + rk * 29f) % (r * 2.1f))
                val ry = groundY - rise
                drawRoundRect(
                    color = Color(0xFF78350F),
                    topLeft = Offset(rx - r * 0.08f, ry - r * 0.07f),
                    size = Size(r * 0.16f, r * 0.14f),
                    cornerRadius = CornerRadius(3f, 3f)
                )
                drawRoundRect(
                    color = Color(0xFFFEF08A),
                    topLeft = Offset(rx - r * 0.08f, ry - r * 0.07f),
                    size = Size(r * 0.16f, r * 0.14f),
                    cornerRadius = CornerRadius(3f, 3f),
                    style = Stroke(width = 1.4f)
                )
            }
            // Snapping Blue Bio-Electric Arcs
            for (side in listOf(-1f, 1f)) {
                val bolt = Path().apply {
                    moveTo(cx + side * r * 1.15f, cy - r * 0.75f)
                    lineTo(cx + side * r * 0.85f, cy - r * 0.25f)
                    lineTo(cx + side * r * 1.22f, cy + r * 0.08f)
                    lineTo(cx + side * r * 0.92f, cy + r * 0.62f)
                }
                drawPath(bolt, color = Color(0xFF38BDF8), style = Stroke(width = 2.8f, cap = StrokeCap.Round))
            }
        }

        MascotAuraStyle.DRAGON_INFERNO -> {
            // 3. Coiling S-Curve Eastern Fire Dragon Spirit with Glowing Dragon Head, Horns, Whiskers & Flaming Tail!
            val dragonSpine = Path().apply {
                moveTo(cx - r * 1.25f, cy + r * 0.95f)
                cubicTo(
                    cx - r * 1.65f, cy + r * 0.10f,
                    cx + r * 1.65f, cy - r * 0.10f,
                    cx + r * 1.12f, cy - r * 0.96f
                )
            }
            // Outer Crimson-Orange Flame Body of the Coiling Dragon
            drawPath(
                path = dragonSpine,
                brush = Brush.verticalGradient(listOf(Color(0xFFFEF08A), Color(0xFFF97316), Color(0xFFDC2626))),
                style = Stroke(width = (r * 0.28f).coerceAtLeast(8f), cap = StrokeCap.Round)
            )
            // Inner Golden Dragon Spine & Dorsal Scales
            drawPath(
                path = dragonSpine,
                color = Color(0xFFFEF08A),
                style = Stroke(width = (r * 0.11f).coerceAtLeast(3.5f), cap = StrokeCap.Round)
            )
            // Fire Dragon Head at Upper-Right (cx + r * 1.12f, cy - r * 0.96f)
            val headX = cx + r * 1.12f
            val headY = cy - r * 0.96f
            // Golden Dragon Antler Horns
            drawLine(Color(0xFFFACC15), Offset(headX - r * 0.06f, headY - r * 0.12f), Offset(headX - r * 0.28f, headY - r * 0.36f), strokeWidth = 3.2f, cap = StrokeCap.Round)
            drawLine(Color(0xFFFACC15), Offset(headX + r * 0.06f, headY - r * 0.12f), Offset(headX + r * 0.22f, headY - r * 0.38f), strokeWidth = 3.2f, cap = StrokeCap.Round)
            // Dragon Snout & Head
            drawRoundRect(
                color = Color(0xFFDC2626),
                topLeft = Offset(headX - r * 0.22f, headY - r * 0.18f),
                size = Size(r * 0.44f, r * 0.34f),
                cornerRadius = CornerRadius(r * 0.12f, r * 0.12f)
            )
            drawRoundRect(
                color = Color(0xFFFACC15),
                topLeft = Offset(headX - r * 0.22f, headY - r * 0.18f),
                size = Size(r * 0.44f, r * 0.34f),
                cornerRadius = CornerRadius(r * 0.12f, r * 0.12f),
                style = Stroke(width = 2.2f)
            )
            // Glowing Yellow Dragon Eyes & Flame Whiskers
            drawCircle(Color(0xFFFEF08A), radius = r * 0.055f, center = Offset(headX - r * 0.08f, headY - r * 0.02f))
            drawCircle(Color(0xFFFEF08A), radius = r * 0.055f, center = Offset(headX + r * 0.08f, headY - r * 0.02f))
            drawLine(Color(0xFFF97316), Offset(headX - r * 0.20f, headY + r * 0.08f), Offset(headX - r * 0.42f, headY + r * 0.22f), strokeWidth = 2.4f, cap = StrokeCap.Round)
            drawLine(Color(0xFFF97316), Offset(headX + r * 0.20f, headY + r * 0.08f), Offset(headX + r * 0.42f, headY + r * 0.22f), strokeWidth = 2.4f, cap = StrokeCap.Round)
        }

        MascotAuraStyle.SHADOW_MONARCH -> {
            // 4. Solo Leveling Abyssal Shadow Domain: Boiling Black-Purple Puddle + 3 Horned Shadow Soldiers Behind + Slash Rifts!
            val puddleY = cy + r * 1.04f
            drawOval(
                color = Color(0xFF090D16),
                topLeft = Offset(cx - r * 1.48f, puddleY - r * 0.22f),
                size = Size(r * 2.96f, r * 0.44f)
            )
            drawOval(
                color = Color(0xFFA855F7),
                topLeft = Offset(cx - r * 1.48f, puddleY - r * 0.22f),
                size = Size(r * 2.96f, r * 0.44f),
                style = Stroke(width = 2.6f)
            )
            // 2 Horned Shadow Knight Silhouettes Standing Guard on Left & Right Behind Mascot
            for (side in listOf(-1f, 1f)) {
                val kx = cx + side * r * 1.14f
                val ky = cy - r * 0.42f + sin(timeSec * 2.5f + side) * (r * 0.06f)
                // Shadow Knight Plume & Horns
                val knightHead = Path().apply {
                    moveTo(kx - r * 0.22f, ky + r * 0.85f)
                    lineTo(kx - r * 0.18f, ky - r * 0.10f)
                    lineTo(kx - r * 0.28f, ky - r * 0.42f)
                    lineTo(kx, ky - r * 0.22f)
                    lineTo(kx + r * 0.28f, ky - r * 0.42f)
                    lineTo(kx + r * 0.18f, ky - r * 0.10f)
                    lineTo(kx + r * 0.22f, ky + r * 0.85f)
                    close()
                }
                drawPath(knightHead, color = Color(0xFF090D16).copy(alpha = 0.88f))
                drawPath(knightHead, color = Color(0xFFA855F7), style = Stroke(width = 2f))
                // Glowing Violet Monarch Eyes with Trailing Light Streak
                drawCircle(Color(0xFFD8B4FE), radius = r * 0.048f, center = Offset(kx - r * 0.07f, ky - r * 0.04f))
                drawCircle(Color(0xFFD8B4FE), radius = r * 0.048f, center = Offset(kx + r * 0.07f, ky - r * 0.04f))
                drawLine(Color(0xFFC084FC), Offset(kx + r * 0.07f, ky - r * 0.04f), Offset(kx + side * r * 0.26f, ky - r * 0.16f), strokeWidth = 2.2f, cap = StrokeCap.Round)
            }
            // Rising Abyssal Smoke Tendrils from the Shadow Puddle
            for (t in -2..2) {
                val tx = cx + t * (r * 0.52f)
                val wave = sin(timeSec * 3.5f + t) * (r * 0.12f)
                val smoke = Path().apply {
                    moveTo(tx, puddleY)
                    quadraticTo(tx + wave, cy + r * 0.45f, tx - wave * 0.6f, cy - r * 0.15f)
                }
                drawPath(smoke, color = Color(0xFF9333EA).copy(alpha = 0.65f), style = Stroke(width = (r * 0.08f).coerceAtLeast(3f), cap = StrokeCap.Round))
            }
        }

        MascotAuraStyle.CYBER_MATRIX_HEX -> {
            // 5. 3D Holographic Hacker HUD: Left & Right Floating Terminal Screens + Cascading Digital Rain + Radar Crosshair
            for (colIdx in -4..4) {
                val mx = cx + colIdx * (r * 0.32f)
                val my = cy - r * 1.35f + ((timeSec * 75f + abs(colIdx) * 41f) % (r * 2.4f))
                drawLine(
                    color = Color(0xFF10B981).copy(alpha = 0.72f),
                    start = Offset(mx, my),
                    end = Offset(mx, my + r * 0.32f),
                    strokeWidth = (r * 0.042f).coerceAtLeast(2f),
                    cap = StrokeCap.Round
                )
                drawCircle(Color(0xFFA7F3D0), radius = r * 0.032f, center = Offset(mx, my + r * 0.32f))
            }
            // 2 Floating Holographic Terminal Windows on Left & Right Sides
            for (side in listOf(-1f, 1f)) {
                val wx = if (side < 0) cx - r * 1.48f else cx + r * 0.88f
                val wy = cy - r * 0.52f + sin(timeSec * 2.4f + side) * (r * 0.06f)
                val ww = r * 0.60f
                val wh = r * 0.46f
                drawRoundRect(
                    color = Color(0xFF064E3B).copy(alpha = 0.82f),
                    topLeft = Offset(wx, wy),
                    size = Size(ww, wh),
                    cornerRadius = CornerRadius(6f, 6f)
                )
                drawRoundRect(
                    color = Color(0xFF34D399),
                    topLeft = Offset(wx, wy),
                    size = Size(ww, wh),
                    cornerRadius = CornerRadius(6f, 6f),
                    style = Stroke(width = 1.8f)
                )
                // Terminal code lines inside hologram screen
                for (ln in 0..2) {
                    val ly = wy + r * (0.12f + ln * 0.11f)
                    drawLine(
                        color = if (ln == 0) Color(0xFFFACC15) else Color(0xFF6EE7B7),
                        start = Offset(wx + r * 0.08f, ly),
                        end = Offset(wx + ww * (if (ln == 1) 0.65f else 0.85f), ly),
                        strokeWidth = 2f,
                        cap = StrokeCap.Round
                    )
                }
            }
            // Corner HUD Bracket Locks
            val bDist = r * 1.22f
            val bLen = r * 0.24f
            for (sx in listOf(-1f, 1f)) {
                for (sy in listOf(-1f, 1f)) {
                    val bx = cx + sx * bDist
                    val by = cy + sy * bDist
                    drawLine(Color(0xFF00F2FE), Offset(bx, by), Offset(bx - sx * bLen, by), strokeWidth = 2.6f)
                    drawLine(Color(0xFF00F2FE), Offset(bx, by), Offset(bx, by - sy * bLen), strokeWidth = 2.6f)
                }
            }
        }

        MascotAuraStyle.SAKURA_BLOSSOM_STORM -> {
            // 6. Sweeping Diagonal Spring Wind Gust Ribbons + Top Blooming Cherry Branch + 5-Petal Notched Blossoms
            for (wIdx in -1..1) {
                val yOff = wIdx * (r * 0.46f)
                val windRibbon = Path().apply {
                    moveTo(cx - r * 1.48f, cy - r * 0.55f + yOff)
                    cubicTo(
                        cx - r * 0.45f, cy - r * 1.05f + yOff,
                        cx + r * 0.45f, cy + r * 0.25f + yOff,
                        cx + r * 1.48f, cy - r * 0.15f + yOff
                    )
                }
                drawPath(
                    path = windRibbon,
                    brush = Brush.horizontalGradient(listOf(Color.Transparent, Color(0xFFFBCFE8), Color(0xFFF472B6), Color.Transparent)),
                    style = Stroke(width = (r * 0.055f).coerceAtLeast(2.2f), cap = StrokeCap.Round)
                )
            }
            // Blooming Sakura Tree Branch Silhouette at Top-Left
            drawLine(
                color = Color(0xFF5D4037),
                start = Offset(cx - r * 1.45f, cy - r * 1.15f),
                end = Offset(cx - r * 0.58f, cy - r * 0.92f),
                strokeWidth = (r * 0.07f).coerceAtLeast(3f),
                cap = StrokeCap.Round
            )
            // 6 Full 5-Petal Cherry Blossoms Drifting Along the Diagonal Wind Stream
            for (bIdx in 0..5) {
                val t = ((timeSec * 0.28f + bIdx * 0.17f) % 1f)
                val fx = cx - r * 1.35f + t * (r * 2.70f)
                val fy = cy - r * 0.85f + sin(t * 6.28f + bIdx) * (r * 0.58f) + (bIdx % 3) * (r * 0.35f)
                for (pet in 0..4) {
                    val pa = (pet * 72f + timeSec * 45f) * (PI.toFloat() / 180f)
                    drawCircle(
                        color = if (bIdx % 2 == 0) Color(0xFFF472B6) else Color(0xFFFBCFE8),
                        radius = r * 0.065f,
                        center = Offset(fx + cos(pa) * r * 0.075f, fy + sin(pa) * r * 0.075f)
                    )
                }
                drawCircle(Color(0xFFFACC15), radius = r * 0.038f, center = Offset(fx, fy))
            }
        }

        MascotAuraStyle.THUNDER_GOD_ZEUS -> {
            // 7. Overhead Storm Cloud Canopy + Raijin Taiko Thunder Drum Arch + Sky-to-Ground Jagged Lightning Bolts!
            val cloudY = cy - r * 1.18f
            // Connecting Red Raijin Ribbon Arch Behind Shoulders
            drawArc(
                color = Color(0xFFEF4444),
                startAngle = 165f,
                sweepAngle = 210f,
                useCenter = false,
                topLeft = Offset(cx - r * 1.32f, cy - r * 1.10f),
                size = Size(r * 2.64f, r * 2.20f),
                style = Stroke(width = (r * 0.055f).coerceAtLeast(2.4f), cap = StrokeCap.Round)
            )
            // 5 Raijin Taiko Thunder Drums along Upper Arch
            for (dIdx in 0..4) {
                val a = (190f + dIdx * 40f) * (PI.toFloat() / 180f)
                val dx = cx + cos(a) * (r * 1.28f)
                val dy = cy + sin(a) * (r * 1.12f)
                drawCircle(color = Color(0xFFFEF08A), radius = r * 0.15f, center = Offset(dx, dy))
                drawCircle(color = Color(0xFFB45309), radius = r * 0.15f, center = Offset(dx, dy), style = Stroke(width = 2.4f))
                // 3 Tomoe comma dots inside drum
                for (tm in 0..2) {
                    val ta = (tm * 120f + timeSec * 80f) * (PI.toFloat() / 180f)
                    drawCircle(Color(0xFF0F172A), radius = r * 0.035f, center = Offset(dx + cos(ta) * r * 0.065f, dy + sin(ta) * r * 0.065f))
                }
            }
            // Rolling Dark Thunderclouds Overhead with Golden Rim
            for (cOff in listOf(-0.55f, 0f, 0.55f)) {
                drawOval(
                    color = Color(0xFF1E293B),
                    topLeft = Offset(cx + cOff * r - r * 0.48f, cloudY - r * 0.22f),
                    size = Size(r * 0.96f, r * 0.42f)
                )
                drawOval(
                    color = Color(0xFFFACC15),
                    topLeft = Offset(cx + cOff * r - r * 0.48f, cloudY - r * 0.22f),
                    size = Size(r * 0.96f, r * 0.42f),
                    style = Stroke(width = 1.8f)
                )
            }
            // Twin Jagged Lightning Bolts Striking Downward on Left & Right
            for (side in listOf(-1f, 1f)) {
                val lx = cx + side * r * 1.12f
                val bolt = Path().apply {
                    moveTo(cx + side * r * 0.72f, cloudY + r * 0.15f)
                    lineTo(lx + side * r * 0.18f, cy - r * 0.35f)
                    lineTo(lx - side * r * 0.10f, cy - r * 0.15f)
                    lineTo(lx + side * r * 0.24f, cy + r * 0.88f)
                }
                drawPath(bolt, color = Color(0xFFFEF08A), style = Stroke(width = (r * 0.065f).coerceAtLeast(2.8f), cap = StrokeCap.Round))
            }
        }

        MascotAuraStyle.FROST_BLIZZARD_ICE -> {
            // 8. Giant 6-Branch Fractal Snowflake Mandala Behind + Erupting Glacier Ice Spikes at Base!
            rotate(degrees = (timeSec * 14f) % 360f, pivot = Offset(cx, cy)) {
                for (branch in 0..5) {
                    val a = (branch * 60f) * (PI.toFloat() / 180f)
                    val ex = cx + cos(a) * (r * 1.44f)
                    val ey = cy + sin(a) * (r * 1.44f)
                    drawLine(Color(0xFFE0F2FE), Offset(cx, cy), Offset(ex, ey), strokeWidth = (r * 0.05f).coerceAtLeast(2.2f), cap = StrokeCap.Round)
                    // Snowflake chevron V-branches at 65% and 88% radius
                    for (frac in listOf(0.68f, 0.90f)) {
                        val bx = cx + cos(a) * (r * 1.44f * frac)
                        val by = cy + sin(a) * (r * 1.44f * frac)
                        for (wing in listOf(-0.55f, 0.55f)) {
                            val vx = bx + cos(a + wing) * (r * 0.26f)
                            val vy = by + sin(a + wing) * (r * 0.26f)
                            drawLine(Color(0xFF38BDF8), Offset(bx, by), Offset(vx, vy), strokeWidth = 2.2f, cap = StrokeCap.Round)
                        }
                    }
                }
            }
            // Jagged Translucent Glacier Icebergs Jutting Up from Left & Right Ground
            val baseIceY = cy + r * 1.05f
            for (side in listOf(-1f, 1f)) {
                val ix = cx + side * r * 1.05f
                val glacier = Path().apply {
                    moveTo(ix - r * 0.28f, baseIceY)
                    lineTo(ix - r * 0.08f, cy + r * 0.22f)
                    lineTo(ix + r * 0.12f, cy + r * 0.52f)
                    lineTo(ix + side * r * 0.26f, cy + r * 0.05f)
                    lineTo(ix + r * 0.34f, baseIceY)
                    close()
                }
                drawPath(glacier, brush = Brush.verticalGradient(listOf(Color.White, Color(0xFF7DD3FC), Color(0xFF0284C7))))
                drawPath(glacier, color = Color.White, style = Stroke(width = 1.8f))
            }
        }

        MascotAuraStyle.SULTAN_WEALTH_RAIN -> {
            // 9. Sultan Gold Bullion Vault Base + Vertical Falling Rain of Gold Coins, Dollar Bills & Rubies!
            val vaultBaseY = cy + r * 0.96f
            // Stacked 3D Gold Bullion Bars at Left & Right Base
            for (side in listOf(-1f, 1f)) {
                val bx = cx + side * r * 1.02f - r * 0.22f
                drawRoundRect(Color(0xFFB45309), Offset(bx, vaultBaseY - r * 0.14f), Size(r * 0.44f, r * 0.20f), CornerRadius(4f, 4f))
                drawRoundRect(Color(0xFFFACC15), Offset(bx, vaultBaseY - r * 0.18f), Size(r * 0.44f, r * 0.17f), CornerRadius(4f, 4f))
                drawRoundRect(Color(0xFFFEF08A), Offset(bx + r * 0.06f, vaultBaseY - r * 0.32f), Size(r * 0.32f, r * 0.15f), CornerRadius(4f, 4f))
            }
            // Vertical Falling Rain of Coins, Green Banknotes & Cut Diamonds from the Sky
            for (drop in 0..7) {
                val dx = cx - r * 1.28f + drop * (r * 0.36f)
                val dy = cy - r * 1.35f + ((timeSec * 65f + drop * 43f) % (r * 2.35f))
                when (drop % 3) {
                    0 -> {
                        // 24K Gold Coin
                        drawCircle(Color(0xFFB45309), radius = r * 0.13f, center = Offset(dx, dy + 2f))
                        drawCircle(Color(0xFFFACC15), radius = r * 0.12f, center = Offset(dx, dy))
                        drawCircle(Color(0xFFFEF08A), radius = r * 0.07f, center = Offset(dx, dy), style = Stroke(width = 1.4f))
                    }
                    1 -> {
                        // Green Sultan Banknote Bill
                        rotate(degrees = sin(timeSec * 3f + drop) * 24f, pivot = Offset(dx, dy)) {
                            drawRoundRect(Color(0xFF16A34A), Offset(dx - r * 0.15f, dy - r * 0.08f), Size(r * 0.30f, r * 0.16f), CornerRadius(3f, 3f))
                            drawRoundRect(Color(0xFFBBF7D0), Offset(dx - r * 0.15f, dy - r * 0.08f), Size(r * 0.30f, r * 0.16f), CornerRadius(3f, 3f), style = Stroke(width = 1.5f))
                            drawCircle(Color(0xFFBBF7D0), radius = r * 0.04f, center = Offset(dx, dy))
                        }
                    }
                    else -> {
                        // Faceted Blue Diamond / Ruby Gem
                        val gem = Path().apply {
                            moveTo(dx, dy - r * 0.12f)
                            lineTo(dx + r * 0.11f, dy)
                            lineTo(dx, dy + r * 0.12f)
                            lineTo(dx - r * 0.11f, dy)
                            close()
                        }
                        drawPath(gem, color = if (drop % 2 == 0) Color(0xFF38BDF8) else Color(0xFFF43F5E))
                        drawPath(gem, color = Color.White, style = Stroke(width = 1.4f))
                    }
                }
            }
        }

        MascotAuraStyle.CELESTIAL_ANGEL_LIGHT -> {
            // 10. Descending Heaven God-Rays from Top + Intersecting Seraphim Ophanim Eye-Wheels + Cathedral Halo
            for (beam in -2..2) {
                val topX = cx + beam * (r * 0.26f)
                val botX = cx + beam * (r * 0.62f)
                drawLine(
                    brush = Brush.verticalGradient(listOf(Color(0xFFFEF08A).copy(alpha = 0.72f), Color.Transparent)),
                    start = Offset(topX, cy - r * 1.55f),
                    end = Offset(botX, cy + r * 1.20f),
                    strokeWidth = (r * 0.18f).coerceAtLeast(6f),
                    cap = StrokeCap.Round
                )
            }
            // Twin Intersecting Golden Ophanim Wheels with All-Seeing Eyes
            for (tilt in listOf(-26f, 26f)) {
                rotate(degrees = tilt + (timeSec * 18f) % 360f, pivot = Offset(cx, cy - r * 0.10f)) {
                    drawOval(
                        color = Color(0xFFFACC15),
                        topLeft = Offset(cx - r * 1.42f, cy - r * 0.52f),
                        size = Size(r * 2.84f, r * 0.84f),
                        style = Stroke(width = (r * 0.055f).coerceAtLeast(2.4f))
                    )
                    for (eyeIdx in 0..3) {
                        val ea = (eyeIdx * 90f) * (PI.toFloat() / 180f)
                        val ex = cx + cos(ea) * (r * 1.42f)
                        val ey = cy - r * 0.10f + sin(ea) * (r * 0.42f)
                        drawCircle(Color.White, radius = r * 0.065f, center = Offset(ex, ey))
                        drawCircle(Color(0xFF0284C7), radius = r * 0.032f, center = Offset(ex, ey))
                    }
                }
            }
            // Crown Halo Ring Overhead
            drawOval(
                color = Color(0xFFFEF08A),
                topLeft = Offset(cx - r * 0.65f, cy - r * 1.32f),
                size = Size(r * 1.30f, r * 0.26f),
                style = Stroke(width = (r * 0.06f).coerceAtLeast(2.5f))
            )
        }

        MascotAuraStyle.VOID_BLACK_HOLE -> {
            // 11. Interstellar Gargantua Gravitational Lensing: Warped Spacetime Grid + Photon Sphere + Relativistic Accretion Disk
            for (g in -2..2) {
                val curve = Path().apply {
                    moveTo(cx - r * 1.50f, cy + g * (r * 0.42f))
                    quadraticTo(cx, cy + g * (r * 0.10f), cx + r * 1.50f, cy + g * (r * 0.42f))
                }
                drawPath(curve, color = Color(0xFF9333EA).copy(alpha = 0.42f), style = Stroke(width = 1.5f))
            }
            // Event Horizon Singularity & Bright White Photon Ring
            drawCircle(color = Color(0xFF05070E), radius = r * 1.24f, center = Offset(cx, cy))
            drawCircle(color = Color(0xFFFEF08A), radius = r * 1.25f, center = Offset(cx, cy), style = Stroke(width = (r * 0.055f).coerceAtLeast(2.4f)))
            // Tilted Relativistic Accretion Disk
            rotate(degrees = -16f, pivot = Offset(cx, cy)) {
                drawOval(
                    brush = Brush.sweepGradient(
                        listOf(Color(0xFFF97316), Color(0xFFFEF08A), Color(0xFFEC4899), Color(0xFF9333EA), Color(0xFFF97316)),
                        center = Offset(cx, cy)
                    ),
                    topLeft = Offset(cx - r * 1.62f, cy - r * 0.36f),
                    size = Size(r * 3.24f, r * 0.72f),
                    style = Stroke(width = (r * 0.12f).coerceAtLeast(4.5f))
                )
            }
        }

        MascotAuraStyle.RAINBOW_RGB_GAMER -> {
            // 12. Neon Arcade Audio Visualizer Equalizer Bars + Floating Pixel Game Icons!
            val eqCols = listOf(
                Color(0xFFEF4444), Color(0xFFF97316), Color(0xFFFACC15),
                Color(0xFF22C55E), Color(0xFF06B6D4), Color(0xFF3B82F6),
                Color(0xFF8B5CF6), Color(0xFFEC4899)
            )
            val eqBaseY = cy + r * 1.08f
            for (b in 0..11) {
                val side = if (b < 6) -1f else 1f
                val idx = b % 6
                val bx = if (side < 0) cx - r * 1.44f + idx * (r * 0.14f) else cx + r * 0.72f + idx * (r * 0.14f)
                val barH = r * (0.35f + 0.55f * abs(sin(timeSec * 6.5f + b * 0.7f)))
                val barCol = eqCols[b % eqCols.size]
                drawRoundRect(
                    color = barCol,
                    topLeft = Offset(bx, eqBaseY - barH),
                    size = Size(r * 0.10f, barH),
                    cornerRadius = CornerRadius(3f, 3f)
                )
                // Floating Peak Cap Block above each equalizer bar
                drawRoundRect(
                    color = Color.White,
                    topLeft = Offset(bx, eqBaseY - barH - r * 0.10f),
                    size = Size(r * 0.10f, r * 0.06f),
                    cornerRadius = CornerRadius(2f, 2f)
                )
            }
            // Top Neon RGB Prism Arch
            drawArc(
                brush = Brush.horizontalGradient(eqCols),
                startAngle = 195f,
                sweepAngle = 150f,
                useCenter = false,
                topLeft = Offset(cx - r * 1.38f, cy - r * 1.32f),
                size = Size(r * 2.76f, r * 1.80f),
                style = Stroke(width = (r * 0.065f).coerceAtLeast(2.8f), cap = StrokeCap.Round)
            )
        }

        MascotAuraStyle.ARCANE_RUNE_CIRCLE -> {
            // 13. Grand Mage Triple Spell Array: Central Solomon Hexagram + Twin Side Caster Circles + Arcane Links!
            val mainR = r * 1.30f
            drawCircle(color = Color(0xFFA855F7), radius = mainR, center = Offset(cx, cy), style = Stroke(width = 2.2f))
            rotate(degrees = (timeSec * 24f) % 360f, pivot = Offset(cx, cy)) {
                for (t in 0..1) {
                    val tri = Path().apply {
                        for (i in 0..2) {
                            val a = (i * 120f + t * 60f) * (PI.toFloat() / 180f)
                            val px = cx + cos(a) * mainR
                            val py = cy + sin(a) * mainR
                            if (i == 0) moveTo(px, py) else lineTo(px, py)
                        }
                        close()
                    }
                    drawPath(tri, color = Color(0xFFFACC15), style = Stroke(width = 1.8f))
                }
            }
            // Twin Smaller Satellite Spell Circles on Left & Right Hands
            for (side in listOf(-1f, 1f)) {
                val sx = cx + side * r * 1.26f
                val sy = cy - r * 0.12f + sin(timeSec * 3f + side) * (r * 0.08f)
                val subR = r * 0.34f
                drawCircle(color = Color(0xFF00F2FE), radius = subR, center = Offset(sx, sy), style = Stroke(width = 2.2f))
                rotate(degrees = -(timeSec * 60f) % 360f, pivot = Offset(sx, sy)) {
                    drawRect(
                        color = Color(0xFFFEF08A),
                        topLeft = Offset(sx - subR * 0.68f, sy - subR * 0.68f),
                        size = Size(subR * 1.36f, subR * 1.36f),
                        style = Stroke(width = 1.6f)
                    )
                }
            }
        }

        MascotAuraStyle.LOVE_HEART_SYMPHONY -> {
            // 14. Idol Stage Musical Staff Ribbon + Winged Cupid Hearts + Sparkling Notes!
            for (lineIdx in -1..1) {
                val staff = Path().apply {
                    moveTo(cx - r * 1.48f, cy + r * 0.45f + lineIdx * (r * 0.10f))
                    cubicTo(
                        cx - r * 0.55f, cy - r * 1.15f + lineIdx * (r * 0.10f),
                        cx + r * 0.55f, cy + r * 1.15f + lineIdx * (r * 0.10f),
                        cx + r * 1.48f, cy - r * 0.45f + lineIdx * (r * 0.10f)
                    )
                }
                drawPath(staff, color = Color(0xFFFBCFE8).copy(alpha = 0.78f), style = Stroke(width = 1.8f))
            }
            // Twin Winged Cupid Hearts on Left & Right
            for (side in listOf(-1f, 1f)) {
                val hx = cx + side * r * 1.18f
                val hy = cy - r * 0.38f + sin(timeSec * 3.8f + side) * (r * 0.10f)
                val hs = r * 0.18f
                // Cute white mini angel wings on heart
                drawOval(Color.White, Offset(hx - hs * 1.65f, hy - hs * 0.55f), Size(hs * 0.95f, hs * 0.55f))
                drawOval(Color.White, Offset(hx + hs * 0.70f, hy - hs * 0.55f), Size(hs * 0.95f, hs * 0.55f))
                val heart = Path().apply {
                    moveTo(hx, hy + hs * 0.68f)
                    cubicTo(hx - hs * 1.35f, hy - hs * 0.15f, hx - hs * 0.65f, hy - hs * 1.18f, hx, hy - hs * 0.35f)
                    cubicTo(hx + hs * 0.65f, hy - hs * 1.18f, hx + hs * 1.35f, hy - hs * 0.15f, hx, hy + hs * 0.68f)
                    close()
                }
                drawPath(heart, color = Color(0xFFF43F5E))
                drawPath(heart, color = Color(0xFFFEF08A), style = Stroke(width = 1.6f))
            }
            // Floating Musical Eighth-Notes
            for (nIdx in 0..3) {
                val nx = cx + (nIdx - 1.5f) * (r * 0.72f)
                val ny = cy - r * 1.05f + (nIdx % 2) * (r * 1.85f)
                drawCircle(Color(0xFFFACC15), radius = r * 0.065f, center = Offset(nx, ny))
                drawLine(Color(0xFFFACC15), Offset(nx + r * 0.055f, ny), Offset(nx + r * 0.055f, ny - r * 0.22f), strokeWidth = 2.4f)
                drawLine(Color(0xFFFACC15), Offset(nx + r * 0.055f, ny - r * 0.22f), Offset(nx + r * 0.16f, ny - r * 0.15f), strokeWidth = 2.4f)
            }
        }

        MascotAuraStyle.EMERALD_FOREST_SPIRIT -> {
            // 15. Ancient Yggdrasil World-Tree Canopy & Winding Botanical Vines with Blooming Flowers & Kodama Orbs!
            for (side in listOf(-1f, 1f)) {
                val vinePath = Path().apply {
                    moveTo(cx + side * r * 0.85f, cy + r * 1.05f)
                    cubicTo(
                        cx + side * r * 1.55f, cy + r * 0.35f,
                        cx + side * r * 0.95f, cy - r * 0.35f,
                        cx + side * r * 0.35f, cy - r * 1.25f
                    )
                }
                drawPath(vinePath, color = Color(0xFF15803D), style = Stroke(width = (r * 0.065f).coerceAtLeast(2.6f), cap = StrokeCap.Round))
                // Glowing Emerald Leaves & Golden Flowers along each vine
                for (lf in 0..3) {
                    val ly = cy + r * (0.72f - lf * 0.48f)
                    val lx = cx + side * r * (1.16f - (lf % 2) * 0.18f)
                    drawOval(
                        color = Color(0xFF4ADE80),
                        topLeft = Offset(lx - r * 0.11f, ly - r * 0.06f),
                        size = Size(r * 0.22f, r * 0.12f)
                    )
                    if (lf % 2 == 0) {
                        drawCircle(Color(0xFFFEF08A), radius = r * 0.055f, center = Offset(lx + side * r * 0.10f, ly - r * 0.06f))
                    }
                }
            }
            // Floating Forest Fairy Lantern Orbs
            for (sp in 0..4) {
                val sx = cx + (sp - 2) * (r * 0.56f)
                val sy = cy - r * 1.15f + sin(timeSec * 3.2f + sp * 1.4f) * (r * 0.16f)
                drawCircle(Color(0xFFBBF7D0).copy(alpha = 0.55f), radius = r * 0.11f, center = Offset(sx, sy))
                drawCircle(Color(0xFFFEF08A), radius = r * 0.05f, center = Offset(sx, sy))
            }
        }

        MascotAuraStyle.WATER_SUN_BREATHING -> {
            // 16. Demon Slayer Dual Breathing: Hinokami Sun Fire Wheel (Left-to-Top) + Constant Flux Water Dragon Wave (Right-to-Bottom)
            rotate(degrees = timeSec * 45f, pivot = Offset(cx, cy)) {
                drawArc(
                    brush = Brush.sweepGradient(listOf(Color(0xFFFEF08A), Color(0xFFF97316), Color(0xFFDC2626), Color.Transparent)),
                    startAngle = 0f,
                    sweepAngle = 165f,
                    useCenter = false,
                    topLeft = Offset(cx - r * 1.38f, cy - r * 1.38f),
                    size = Size(r * 2.76f, r * 2.76f),
                    style = Stroke(width = (r * 0.16f).coerceAtLeast(5f), cap = StrokeCap.Round)
                )
                drawArc(
                    brush = Brush.sweepGradient(listOf(Color.White, Color(0xFF38BDF8), Color(0xFF0284C7), Color.Transparent)),
                    startAngle = 180f,
                    sweepAngle = 165f,
                    useCenter = false,
                    topLeft = Offset(cx - r * 1.38f, cy - r * 1.38f),
                    size = Size(r * 2.76f, r * 2.76f),
                    style = Stroke(width = (r * 0.16f).coerceAtLeast(5f), cap = StrokeCap.Round)
                )
            }
        }

        MascotAuraStyle.SOUL_RINGS_ASCENSION -> {
            // 17. Soul Land (Douluo Dalu) 9 Spirit Rings: Yellow, Purple, Black, and 100,000-Year Crimson-Gold Rings Levitating Vertically
            val ringCols = listOf(
                Color(0xFFFACC15),
                Color(0xFFA855F7),
                Color(0xFF1E293B),
                Color(0xFFEF4444),
                Color(0xFFFDE047)
            )
            ringCols.forEachIndexed { idx, col ->
                val ry = cy + r * (0.78f - idx * 0.36f) + sin(timeSec * 3.5f + idx) * (r * 0.06f)
                val rw = r * (2.65f - idx * 0.14f)
                val rh = r * 0.42f
                drawOval(
                    color = col.copy(alpha = 0.85f),
                    topLeft = Offset(cx - rw * 0.5f, ry - rh * 0.5f),
                    size = Size(rw, rh),
                    style = Stroke(width = (r * 0.055f).coerceAtLeast(2.4f))
                )
            }
        }

        MascotAuraStyle.HEAVENLY_FLAME_LOTUS -> {
            // 18. BTTH (Battle Through the Heavens) Buddha's Angry Fire Lotus: Rotating 8-Petal Jade/Purple/Gold Flame Lotus
            for (petal in 0..7) {
                val angle = petal * 45f + timeSec * 28f
                val pCol = when (petal % 3) {
                    0 -> Color(0xFF10B981)
                    1 -> Color(0xFFA855F7)
                    else -> Color(0xFFFACC15)
                }
                rotate(degrees = angle, pivot = Offset(cx, cy)) {
                    val lotusPetal = Path().apply {
                        moveTo(cx, cy - r * 0.85f)
                        quadraticTo(cx + r * 0.36f, cy - r * 1.25f, cx, cy - r * 1.56f)
                        quadraticTo(cx - r * 0.36f, cy - r * 1.25f, cx, cy - r * 0.85f)
                        close()
                    }
                    drawPath(lotusPetal, color = pCol.copy(alpha = 0.65f))
                    drawPath(lotusPetal, color = Color.White, style = Stroke(width = 1.6f))
                }
            }
        }

        MascotAuraStyle.BANKAI_SPIRIT_PRESSURE -> {
            // 19. Bleach Hollow Bankai Spiritual Pressure: Jagged Black-Crimson Getsuga Tenshou Slashes
            for (slash in 0..5) {
                val a = slash * 60f - timeSec * 55f
                rotate(degrees = a, pivot = Offset(cx, cy)) {
                    val crescent = Path().apply {
                        moveTo(cx - r * 0.45f, cy - r * 1.22f)
                        quadraticTo(cx, cy - r * 1.64f, cx + r * 0.45f, cy - r * 1.22f)
                        quadraticTo(cx, cy - r * 1.38f, cx - r * 0.45f, cy - r * 1.22f)
                        close()
                    }
                    drawPath(crescent, color = Color(0xFF111827))
                    drawPath(crescent, color = Color(0xFFE11D48), style = Stroke(width = 2.4f))
                }
            }
        }

        MascotAuraStyle.TIME_DIVE_CHRONO_RIFT -> {
            // 20. Link Click Time-Dive Clockface: Roman ClockTicks & Counter-Rotating Golden Watch Hands
            val clockR = r * 1.42f
            drawCircle(
                color = Color(0xFF0284C7).copy(alpha = 0.52f),
                radius = clockR,
                center = Offset(cx, cy),
                style = Stroke(width = 2.6f)
            )
            for (tick in 0..11) {
                val rad = Math.toRadians((tick * 30).toDouble())
                val tx1 = cx + cos(rad).toFloat() * (clockR * 0.88f)
                val ty1 = cy + sin(rad).toFloat() * (clockR * 0.88f)
                val tx2 = cx + cos(rad).toFloat() * clockR
                val ty2 = cy + sin(rad).toFloat() * clockR
                drawLine(Color(0xFFFACC15), Offset(tx1, ty1), Offset(tx2, ty2), strokeWidth = if (tick % 3 == 0) 3.2f else 1.6f)
            }
            val handA1 = Math.toRadians((-timeSec * 60f).toDouble())
            val handA2 = Math.toRadians((-timeSec * 15f).toDouble())
            drawLine(Color(0xFFFACC15), Offset(cx, cy), Offset(cx + cos(handA1).toFloat() * clockR * 0.82f, cy + sin(handA1).toFloat() * clockR * 0.82f), strokeWidth = 2.8f, cap = StrokeCap.Round)
            drawLine(Color(0xFF38BDF8), Offset(cx, cy), Offset(cx + cos(handA2).toFloat() * clockR * 0.58f, cy + sin(handA2).toFloat() * clockR * 0.58f), strokeWidth = 3.6f, cap = StrokeCap.Round)
        }

        MascotAuraStyle.HERO_INFINITY_COSMIC -> {
            // 21. Superhero Nano-Reactor Hexagonal Energy Shield & Floating Infinity Stones
            val stoneCols = listOf(
                Color(0xFF3B82F6),
                Color(0xFFFACC15),
                Color(0xFFEF4444),
                Color(0xFFA855F7),
                Color(0xFF22C55E),
                Color(0xFFF97316)
            )
            stoneCols.forEachIndexed { idx, sCol ->
                val rad = Math.toRadians((idx * 60f + timeSec * 40f).toDouble())
                val sx = cx + cos(rad).toFloat() * (r * 1.40f)
                val sy = cy + sin(rad).toFloat() * (r * 1.15f)
                drawCircle(sCol.copy(alpha = 0.45f), radius = r * 0.16f, center = Offset(sx, sy))
                drawCircle(sCol, radius = r * 0.09f, center = Offset(sx, sy))
                drawCircle(Color.White, radius = r * 0.04f, center = Offset(sx, sy))
            }
        }
    }
}

/**
 * Menggambar cincin levitasi kosmik di bawah maskot karena maskot melayang tanpa kaki/celana.
 */
internal fun DrawScope.drawMascotFloatingHoverRings(
    cx: Float,
    cy: Float,
    r: Float,
    timeSec: Float,
    accentColor: Color
) {
    val hoverY = cy + r * 0.90f
    val pulse = 0.5f + 0.5f * sin(timeSec * 3.6f)
    val outerW = r * (1.26f + pulse * 0.14f)
    val outerH = r * 0.22f
    drawOval(
        color = accentColor.copy(alpha = 0.34f),
        topLeft = Offset(cx - outerW * 0.5f, hoverY - outerH * 0.5f),
        size = Size(outerW, outerH),
        style = Stroke(width = (r * 0.032f).coerceAtLeast(1.6f))
    )
    val innerW = r * (0.82f - pulse * 0.10f)
    val innerH = r * 0.13f
    drawOval(
        color = DuoBeeYellow.copy(alpha = 0.45f),
        topLeft = Offset(cx - innerW * 0.5f, hoverY + r * 0.06f - innerH * 0.5f),
        size = Size(innerW, innerH),
        style = Stroke(width = (r * 0.024f).coerceAtLeast(1.2f))
    )
}

/**
 * Menggambar sayap berlapis detail tinggi, jetpack, pedang belakang, atau jubah belakang sebelum tubuh folder digambar.
 */
internal fun DrawScope.drawMascotBackAccessoriesAndCape(
    cx: Float,
    cy: Float,
    r: Float,
    timeSec: Float,
    accessory: MascotAccessory,
    outfit: MascotUpperOutfit
) {
    val wingFlap = sin(timeSec * 5.2f) * 11f
    val sway = sin(timeSec * 3.0f) * (r * 0.09f)

    // 1. JUBAH BELAKANG BERLAPIS 3D UNTUK KOSTUM KERAJAAN / ANIME / RPG / MECHA / HERO
    val hasBackCape = outfit == MascotUpperOutfit.ROYAL_KING_CLOAK ||
        outfit == MascotUpperOutfit.ARCANUM_MAGE_ROBE ||
        outfit == MascotUpperOutfit.RED_CLOUD_CLOAK ||
        outfit == MascotUpperOutfit.CELESTIAL_ANGEL_ROBE ||
        outfit == MascotUpperOutfit.ARCHANGEL_SERAPH_ARMOR ||
        outfit == MascotUpperOutfit.DEMON_OVERLORD_PLATE ||
        outfit == MascotUpperOutfit.ROYAL_EMPEROR_HANFU ||
        outfit == MascotUpperOutfit.PALADIN_CRUSADER_MANTLE ||
        outfit == MascotUpperOutfit.PIRATE_ADMIRAL_COAT ||
        outfit == MascotUpperOutfit.FLAME_HASHIRA_CAPE ||
        outfit == MascotUpperOutfit.AOT_SURVEY_CORPS_CLOAK ||
        outfit == MascotUpperOutfit.BLEACH_BANKAI_SHIHAKUSHO ||
        outfit == MascotUpperOutfit.NARUTO_HOKAGE_SAGE_CLOAK ||
        outfit == MascotUpperOutfit.HERO_THUNDER_ASGARD_PLATE ||
        outfit == MascotUpperOutfit.FANTASY_ELVEN_RANGER_CLOAK ||
        outfit == MascotUpperOutfit.FANTASY_NECROMANCER_LICH_ROBE ||
        outfit == MascotUpperOutfit.FANTASY_VALKYRIE_DIVINE_DRESS

    if (hasBackCape) {
        val capeColor = when (outfit) {
            MascotUpperOutfit.ROYAL_KING_CLOAK, MascotUpperOutfit.HERO_THUNDER_ASGARD_PLATE -> Color(0xFFB91C1C)
            MascotUpperOutfit.ARCANUM_MAGE_ROBE, MascotUpperOutfit.FANTASY_NECROMANCER_LICH_ROBE -> Color(0xFF3B0764)
            MascotUpperOutfit.RED_CLOUD_CLOAK -> Color(0xFF111827)
            MascotUpperOutfit.ARCHANGEL_SERAPH_ARMOR -> Color(0xFFFEF08A)
            MascotUpperOutfit.DEMON_OVERLORD_PLATE -> Color(0xFF450A0A)
            MascotUpperOutfit.ROYAL_EMPEROR_HANFU -> Color(0xFF7F1D1D)
            MascotUpperOutfit.PALADIN_CRUSADER_MANTLE, MascotUpperOutfit.FANTASY_VALKYRIE_DIVINE_DRESS -> Color(0xFF1E3A8A)
            MascotUpperOutfit.PIRATE_ADMIRAL_COAT -> Color(0xFF0F172A)
            MascotUpperOutfit.FLAME_HASHIRA_CAPE, MascotUpperOutfit.BLEACH_BANKAI_SHIHAKUSHO, MascotUpperOutfit.NARUTO_HOKAGE_SAGE_CLOAK -> Color(0xFFF8FAFC)
            MascotUpperOutfit.AOT_SURVEY_CORPS_CLOAK, MascotUpperOutfit.FANTASY_ELVEN_RANGER_CLOAK -> Color(0xFF14532D)
            else -> Color(0xFFE0F2FE)
        }
        val capeTrim = when (outfit) {
            MascotUpperOutfit.RED_CLOUD_CLOAK, MascotUpperOutfit.DEMON_OVERLORD_PLATE, MascotUpperOutfit.FLAME_HASHIRA_CAPE, MascotUpperOutfit.NARUTO_HOKAGE_SAGE_CLOAK -> Color(0xFFEF4444)
            MascotUpperOutfit.ARCHANGEL_SERAPH_ARMOR, MascotUpperOutfit.FANTASY_VALKYRIE_DIVINE_DRESS -> Color(0xFF38BDF8)
            MascotUpperOutfit.BLEACH_BANKAI_SHIHAKUSHO -> Color(0xFF0F172A)
            MascotUpperOutfit.FANTASY_NECROMANCER_LICH_ROBE -> Color(0xFFA855F7)
            else -> Color(0xFFFACC15)
        }
        val capePath = Path().apply {
            moveTo(cx - r * 0.92f, cy - r * 0.22f)
            lineTo(cx - r * 1.42f + sway, cy + r * 0.92f)
            if (outfit == MascotUpperOutfit.FLAME_HASHIRA_CAPE || outfit == MascotUpperOutfit.NARUTO_HOKAGE_SAGE_CLOAK || outfit == MascotUpperOutfit.FANTASY_NECROMANCER_LICH_ROBE) {
                // Jagged Flame / Tattered Tailcoat Silhouette at Bottom
                lineTo(cx - r * 0.85f + sway, cy + r * 0.76f)
                lineTo(cx - r * 0.42f + sway, cy + r * 1.05f)
                lineTo(cx + sway, cy + r * 0.78f)
                lineTo(cx + r * 0.42f + sway, cy + r * 1.05f)
                lineTo(cx + r * 0.85f + sway, cy + r * 0.76f)
                lineTo(cx + r * 1.42f + sway, cy + r * 0.92f)
            } else {
                quadraticTo(cx + sway, cy + r * 1.08f, cx + r * 1.42f + sway, cy + r * 0.92f)
            }
            lineTo(cx + r * 0.92f, cy - r * 0.22f)
            close()
        }
        drawPath(path = capePath, color = capeColor)
        drawPath(
            path = capePath,
            color = capeTrim,
            style = Stroke(width = (r * 0.050f).coerceAtLeast(2.2f))
        )
    }

    // Special Silhouette 1B: Dark Knight Scalloped Bat-Wing Cape
    if (outfit == MascotUpperOutfit.HERO_DARK_KNIGHT_CAPE) {
        val batCape = Path().apply {
            moveTo(cx - r * 0.95f, cy - r * 0.20f)
            lineTo(cx - r * 1.56f + sway, cy + r * 0.86f)
            quadraticTo(cx - r * 1.05f + sway, cy + r * 0.65f, cx - r * 0.65f + sway, cy + r * 0.96f)
            quadraticTo(cx + sway, cy + r * 0.70f, cx + r * 0.65f + sway, cy + r * 0.96f)
            quadraticTo(cx + r * 1.05f + sway, cy + r * 0.65f, cx + r * 1.56f + sway, cy + r * 0.86f)
            lineTo(cx + r * 0.95f, cy - r * 0.20f)
            close()
        }
        drawPath(batCape, color = Color(0xFF0F172A))
        drawPath(batCape, color = Color(0xFF334155), style = Stroke(width = 2.4f))
    }

    // Special Silhouette 1C: Soul Land (Douluo Dalu) Tang San's Eight Spider Lances Extending From Back!
    if (outfit == MascotUpperOutfit.SOUL_LAND_SEA_GOD_ARMOR) {
        for (side in listOf(-1f, 1f)) {
            val angles = listOf(-42f, -14f, 14f, 42f)
            angles.forEachIndexed { idx, a ->
                rotate(degrees = side * (a + wingFlap * 0.45f), pivot = Offset(cx + side * r * 0.60f, cy)) {
                    val lance = Path().apply {
                        moveTo(cx + side * r * 0.60f, cy - r * 0.06f)
                        lineTo(cx + side * r * 1.48f, cy - r * 0.48f)
                        lineTo(cx + side * r * 1.82f, cy + r * 0.08f)
                        lineTo(cx + side * r * 1.38f, cy - r * 0.26f)
                        close()
                    }
                    drawPath(
                        lance,
                        brush = Brush.linearGradient(
                            listOf(
                                if (idx % 2 == 0) Color(0xFF9333EA) else Color(0xFF0EA5E9),
                                Color(0xFFFACC15)
                            )
                        )
                    )
                    drawPath(lance, color = Color(0xFFFEF08A), style = Stroke(width = 1.6f))
                }
            }
        }
    }

    // Special Silhouette 1D: Iron Spider 4 Articulated Golden Mechanical Legs Extending From Back!
    if (outfit == MascotUpperOutfit.HERO_SPIDER_SYMBIOTE_SUIT || outfit == MascotUpperOutfit.HERO_NANO_IRON_ARMOR) {
        for (side in listOf(-1f, 1f)) {
            for (leg in listOf(-28f, 24f)) {
                rotate(degrees = side * (leg + wingFlap * 0.4f), pivot = Offset(cx + side * r * 0.65f, cy - r * 0.1f)) {
                    val pincer = Path().apply {
                        moveTo(cx + side * r * 0.65f, cy - r * 0.1f)
                        lineTo(cx + side * r * 1.45f, cy - r * 0.62f)
                        lineTo(cx + side * r * 1.72f, cy - r * 0.12f)
                    }
                    drawPath(pincer, color = Color(0xFFFACC15), style = Stroke(width = (r * 0.065f).coerceAtLeast(2.8f), cap = StrokeCap.Round))
                    drawCircle(Color(0xFFEF4444), radius = r * 0.06f, center = Offset(cx + side * r * 1.45f, cy - r * 0.62f))
                }
            }
        }
    }

    // Special Silhouette 1E: BTTH Xiao Yan's Giant Heavy Xuan Ruler Strapped Diagonally + Jade Fire Wings!
    if (outfit == MascotUpperOutfit.BTTH_FLAME_EMPEROR_ROBE) {
        // Diagonal Giant Black Heavy Xuan Ruler Behind Shoulder
        rotate(degrees = 34f, pivot = Offset(cx, cy)) {
            drawRoundRect(
                color = Color(0xFF18181B),
                topLeft = Offset(cx - r * 0.24f, cy - r * 1.48f),
                size = Size(r * 0.48f, r * 2.40f),
                cornerRadius = CornerRadius(r * 0.06f, r * 0.06f)
            )
            drawRoundRect(
                color = Color(0xFFF97316),
                topLeft = Offset(cx - r * 0.24f, cy - r * 1.48f),
                size = Size(r * 0.48f, r * 2.40f),
                cornerRadius = CornerRadius(r * 0.06f, r * 0.06f),
                style = Stroke(width = 2.4f)
            )
        }
        // Translucent Jade-Purple Flame Wings
        for (side in listOf(-1f, 1f)) {
            rotate(degrees = side * (12f + wingFlap), pivot = Offset(cx + side * r * 0.70f, cy - r * 0.10f)) {
                val fireWing = Path().apply {
                    moveTo(cx + side * r * 0.70f, cy - r * 0.12f)
                    quadraticTo(cx + side * r * 1.68f, cy - r * 0.85f, cx + side * r * 1.62f, cy + r * 0.05f)
                    quadraticTo(cx + side * r * 1.20f, cy + r * 0.36f, cx + side * r * 0.72f, cy + r * 0.20f)
                    close()
                }
                drawPath(fireWing, brush = Brush.linearGradient(listOf(Color(0xFF10B981).copy(alpha = 0.75f), Color(0xFFA855F7).copy(alpha = 0.75f))))
                drawPath(fireWing, color = Color(0xFFFEF08A), style = Stroke(width = 1.8f))
            }
        }
    }

    // Special Silhouette 1F: Naruto Hokage 6 Floating Black Truth-Seeking Orbs (Gudoudama) Behind Back!
    if (outfit == MascotUpperOutfit.NARUTO_HOKAGE_SAGE_CLOAK) {
        for (orbIdx in 0..5) {
            val angleRad = Math.toRadians((orbIdx * 36f - 180f + sin(timeSec * 3f + orbIdx) * 6f).toDouble())
            val ox = cx + cos(angleRad).toFloat() * (r * 1.32f)
            val oy = cy - r * 0.15f + sin(angleRad).toFloat() * (r * 1.05f)
            drawCircle(Color(0xFFF97316).copy(alpha = 0.55f), radius = r * 0.14f, center = Offset(ox, oy))
            drawCircle(Color(0xFF090D16), radius = r * 0.10f, center = Offset(ox, oy))
        }
    }

    // Gundam Back Vernier Pack for GUNDAM_STRIKE_EXOSUIT or GUNDAM_MECHA_CHEST
    if (outfit == MascotUpperOutfit.GUNDAM_STRIKE_EXOSUIT || outfit == MascotUpperOutfit.GUNDAM_MECHA_CHEST) {
        for (side in listOf(-1f, 1f)) {
            val saberAngle = side * 32f
            rotate(degrees = saberAngle, pivot = Offset(cx + side * r * 0.62f, cy - r * 0.28f)) {
                drawRoundRect(
                    color = Color(0xFFF8FAFC),
                    topLeft = Offset(cx + side * r * 0.56f, cy - r * 0.86f),
                    size = Size(r * 0.13f, r * 0.55f),
                    cornerRadius = CornerRadius(r * 0.04f, r * 0.04f)
                )
                drawRoundRect(
                    color = Color(0xFFEF4444),
                    topLeft = Offset(cx + side * r * 0.55f, cy - r * 0.88f),
                    size = Size(r * 0.15f, r * 0.14f),
                    cornerRadius = CornerRadius(r * 0.04f, r * 0.04f)
                )
            }
        }
    }

    // 2. SAYAP MALAIKAT 3-LAPIS BULU DETAIL TINGGI (ANGEL_WINGS)
    if (accessory == MascotAccessory.ANGEL_WINGS) {
        for (side in listOf(-1f, 1f)) {
            rotate(degrees = side * (12f + wingFlap), pivot = Offset(cx + side * r * 0.7f, cy - r * 0.1f)) {
                for (featherLayer in 0..2) {
                    val spread = featherLayer * 0.18f
                    val wingPath = Path().apply {
                        moveTo(cx + side * r * 0.68f, cy - r * 0.12f + featherLayer * r * 0.08f)
                        quadraticTo(
                            cx + side * r * (1.68f - spread * 0.5f),
                            cy - r * (0.85f - spread * 1.4f),
                            cx + side * r * (1.58f - spread),
                            cy + r * (0.04f + spread * 0.9f)
                        )
                        quadraticTo(
                            cx + side * r * 1.15f,
                            cy + r * (0.35f + spread * 0.5f),
                            cx + side * r * 0.70f,
                            cy + r * 0.24f
                        )
                        close()
                    }
                    drawPath(
                        path = wingPath,
                        brush = Brush.verticalGradient(listOf(Color.White, Color(0xFFE0F2FE), Color(0xFF7DD3FC)))
                    )
                    drawPath(
                        path = wingPath,
                        color = if (featherLayer == 0) Color(0xFFFACC15) else Color(0xFF38BDF8),
                        style = Stroke(width = (r * 0.032f).coerceAtLeast(1.5f))
                    )
                }
            }
        }
    }

    // 3. SAYAP ENAM SERAPHIM SUCI EMAS (SERAPHIM_SIX_WINGS)
    if (accessory == MascotAccessory.SERAPHIM_SIX_WINGS) {
        for (side in listOf(-1f, 1f)) {
            val pairAngles = listOf(-28f, 4f, 34f)
            pairAngles.forEachIndexed { idx, baseAngle ->
                rotate(
                    degrees = side * (baseAngle + wingFlap * (0.7f + idx * 0.2f)),
                    pivot = Offset(cx + side * r * 0.65f, cy - r * 0.05f)
                ) {
                    val wingPath = Path().apply {
                        moveTo(cx + side * r * 0.65f, cy - r * 0.08f)
                        quadraticTo(cx + side * r * 1.72f, cy - r * 0.65f, cx + side * r * 1.66f, cy - r * 0.05f)
                        quadraticTo(cx + side * r * 1.22f, cy + r * 0.28f, cx + side * r * 0.68f, cy + r * 0.16f)
                        close()
                    }
                    drawPath(
                        path = wingPath,
                        brush = Brush.linearGradient(listOf(Color(0xFFFFFFFF), Color(0xFFFEF08A), Color(0xFFF59E0B)))
                    )
                    drawPath(
                        path = wingPath,
                        color = Color(0xFFD97706),
                        style = Stroke(width = (r * 0.030f).coerceAtLeast(1.5f))
                    )
                }
            }
        }
    }

    // 4. SAYAP IBLIS NAGA BERTULANG & CAKAR EMAS (DEMON_WINGS)
    if (accessory == MascotAccessory.DEMON_WINGS) {
        for (side in listOf(-1f, 1f)) {
            rotate(degrees = side * (10f + wingFlap), pivot = Offset(cx + side * r * 0.7f, cy - r * 0.1f)) {
                val batWing = Path().apply {
                    moveTo(cx + side * r * 0.70f, cy - r * 0.15f)
                    lineTo(cx + side * r * 1.56f, cy - r * 0.78f)
                    lineTo(cx + side * r * 1.64f, cy - r * 0.05f)
                    quadraticTo(cx + side * r * 1.38f, cy + r * 0.05f, cx + side * r * 1.32f, cy + r * 0.36f)
                    quadraticTo(cx + side * r * 1.05f, cy + r * 0.20f, cx + side * r * 0.98f, cy + r * 0.48f)
                    lineTo(cx + side * r * 0.72f, cy + r * 0.22f)
                    close()
                }
                drawPath(
                    path = batWing,
                    brush = Brush.verticalGradient(listOf(Color(0xFFDC2626), Color(0xFF7F1D1D), Color(0xFF270505)))
                )
                drawPath(
                    path = batWing,
                    color = Color(0xFFF97316),
                    style = Stroke(width = (r * 0.038f).coerceAtLeast(1.8f))
                )
                drawLine(
                    color = Color(0xFFFACC15),
                    start = Offset(cx + side * r * 0.70f, cy - r * 0.15f),
                    end = Offset(cx + side * r * 1.32f, cy + r * 0.36f),
                    strokeWidth = (r * 0.03f).coerceAtLeast(1.5f)
                )
                drawCircle(
                    color = Color(0xFFFDE047),
                    radius = r * 0.07f,
                    center = Offset(cx + side * r * 1.56f, cy - r * 0.78f)
                )
            }
        }
    }

    // 5. SAYAP MECHA GUNDAM MULTI-VERNIER THRUSTER (MECHA_WINGS)
    if (accessory == MascotAccessory.MECHA_WINGS) {
        for (side in listOf(-1f, 1f)) {
            rotate(degrees = side * (6f + wingFlap * 0.5f), pivot = Offset(cx + side * r * 0.75f, cy - r * 0.1f)) {
                for (fin in 0..1) {
                    val finOffset = fin * r * 0.24f
                    val bladePath = Path().apply {
                        moveTo(cx + side * r * 0.72f, cy - r * 0.26f + finOffset)
                        lineTo(cx + side * r * 1.66f, cy - r * 0.64f + finOffset * 1.3f)
                        lineTo(cx + side * r * 1.52f, cy - r * 0.16f + finOffset * 1.3f)
                        lineTo(cx + side * r * 0.75f, cy + r * 0.12f + finOffset)
                        close()
                    }
                    drawPath(path = bladePath, color = if (fin == 0) Color(0xFF1E293B) else Color(0xFF2563EB))
                    drawPath(
                        path = bladePath,
                        color = Color(0xFF00F2FE),
                        style = Stroke(width = (r * 0.035f).coerceAtLeast(1.6f))
                    )
                }
                drawOval(
                    brush = Brush.verticalGradient(listOf(Color(0xFF00F2FE), Color(0xFF3B82F6), Color.Transparent)),
                    topLeft = Offset(cx + side * r * 1.18f - r * 0.10f, cy + r * 0.18f),
                    size = Size(r * 0.20f, r * 0.44f)
                )
            }
        }
    }

    // 6. JETPACK TURBO PLASMA THRUSTERS (CYBER_JETPACK_THRUSTERS)
    if (accessory == MascotAccessory.CYBER_JETPACK_THRUSTERS) {
        val thrustLen = r * (0.42f + (sin(timeSec * 14f) * 0.5f + 0.5f) * 0.22f)
        for (side in listOf(-1f, 1f)) {
            val podX = cx + side * r * 0.96f
            val podY = cy - r * 0.18f
            // Thruster cylinder pod
            drawRoundRect(
                brush = Brush.verticalGradient(listOf(Color(0xFF475569), Color(0xFF0F172A))),
                topLeft = Offset(podX - r * 0.18f, podY),
                size = Size(r * 0.36f, r * 0.64f),
                cornerRadius = CornerRadius(r * 0.10f, r * 0.10f)
            )
            drawRoundRect(
                color = Color(0xFFF97316),
                topLeft = Offset(podX - r * 0.18f, podY),
                size = Size(r * 0.36f, r * 0.64f),
                cornerRadius = CornerRadius(r * 0.10f, r * 0.10f),
                style = Stroke(width = 2.2f)
            )
            // Twin plasma flame cones under pods
            val flamePath = Path().apply {
                moveTo(podX - r * 0.14f, podY + r * 0.62f)
                lineTo(podX, podY + r * 0.62f + thrustLen)
                lineTo(podX + r * 0.14f, podY + r * 0.62f)
                close()
            }
            drawPath(
                path = flamePath,
                brush = Brush.verticalGradient(listOf(Color(0xFFFEF08A), Color(0xFFF97316), Color(0xFF00F2FE)))
            )
        }
    }

    // 7. PEDANG KEMBAR HOLOGRAM MELAYANG DI PUNGGUNG (HOLOGRAM_TWIN_BLADES)
    if (accessory == MascotAccessory.HOLOGRAM_TWIN_BLADES) {
        for (side in listOf(-1f, 1f)) {
            rotate(degrees = side * 38f, pivot = Offset(cx, cy)) {
                val bladePath = Path().apply {
                    moveTo(cx - r * 0.08f, cy + r * 0.85f)
                    lineTo(cx - r * 0.11f, cy - r * 1.30f)
                    lineTo(cx, cy - r * 1.54f)
                    lineTo(cx + r * 0.11f, cy - r * 1.30f)
                    lineTo(cx + r * 0.08f, cy + r * 0.85f)
                    close()
                }
                drawPath(
                    path = bladePath,
                    brush = Brush.verticalGradient(listOf(Color(0xFF00F2FE), Color(0xFF38BDF8), Color(0xFFEC4899)))
                )
                drawPath(bladePath, color = Color.White, style = Stroke(width = 1.8f))
                // Crossguard hilt
                drawRoundRect(
                    color = Color(0xFFFACC15),
                    topLeft = Offset(cx - r * 0.24f, cy - r * 0.88f),
                    size = Size(r * 0.48f, r * 0.09f),
                    cornerRadius = CornerRadius(4f, 4f)
                )
            }
        }
    }
}

/**
 * Menggambar tekstur permukaan 3D penuh (full-surface edge-to-edge texture) di atas tubuh folder
 * tanpa mengubah bentuk dasar karakter maskot (34 tekstur unik — 0 duplikat!).
 */
internal fun DrawScope.drawMascotSkinSurfaceTexture(
    bodyX: Float,
    bodyY: Float,
    bodyW: Float,
    bodyH: Float,
    r: Float,
    timeSec: Float,
    skin: MascotBodySkin
) {
    if (skin == MascotBodySkin.DEFAULT_MOOD) return

    val cx = bodyX + bodyW * 0.5f
    val cy = bodyY + bodyH * 0.5f
    val bodyCorner = CornerRadius(r * 0.30f, r * 0.30f)
    val bodyClip = Path().apply {
        addRoundRect(
            RoundRect(
                rect = Rect(Offset(bodyX, bodyY), Size(bodyW, bodyH)),
                cornerRadius = bodyCorner
            )
        )
    }

    clipPath(bodyClip) {
        when (skin) {
            MascotBodySkin.DEFAULT_MOOD -> Unit

            MascotBodySkin.ROYAL_GOLD_24K -> {
                // Full-Surface Brushed 24K Gold Bullion Grain + Multi-Band Diagonal Sheen + 6 Diamond Sparkles
                for (g in 0..8) {
                    val gy = bodyY + bodyH * (g / 8f)
                    drawLine(
                        color = Color(0xFFFEF08A).copy(alpha = 0.26f),
                        start = Offset(bodyX, gy),
                        end = Offset(bodyX + bodyW, gy),
                        strokeWidth = 1.6f
                    )
                }
                for (band in 0..2) {
                    val shift = band * (bodyW * 0.32f)
                    drawLine(
                        color = Color.White.copy(alpha = if (band == 1) 0.52f else 0.30f),
                        start = Offset(bodyX - r * 0.2f + shift, bodyY + bodyH),
                        end = Offset(bodyX + bodyW * 0.45f + shift, bodyY),
                        strokeWidth = if (band == 1) r * 0.16f else r * 0.08f,
                        cap = StrokeCap.Round
                    )
                }
                drawRoundRect(
                    color = Color(0xFFFEF08A).copy(alpha = 0.85f),
                    topLeft = Offset(bodyX + r * 0.08f, bodyY + r * 0.08f),
                    size = Size(bodyW - r * 0.16f, bodyH - r * 0.16f),
                    cornerRadius = CornerRadius(r * 0.24f, r * 0.24f),
                    style = Stroke(width = 2.4f)
                )
                val starPts = listOf(
                    Offset(bodyX + bodyW * 0.18f, bodyY + bodyH * 0.22f),
                    Offset(bodyX + bodyW * 0.82f, bodyY + bodyH * 0.22f),
                    Offset(bodyX + bodyW * 0.22f, bodyY + bodyH * 0.78f),
                    Offset(bodyX + bodyW * 0.78f, bodyY + bodyH * 0.78f)
                )
                starPts.forEach { sp ->
                    drawLine(Color.White, Offset(sp.x - r * 0.08f, sp.y), Offset(sp.x + r * 0.08f, sp.y), strokeWidth = 2.2f, cap = StrokeCap.Round)
                    drawLine(Color.White, Offset(sp.x, sp.y - r * 0.08f), Offset(sp.x, sp.y + r * 0.08f), strokeWidth = 2.2f, cap = StrokeCap.Round)
                }
            }

            MascotBodySkin.SOLAR_SUNBURST -> {
                // Full-Body Concentric Solar Flare Plasma Rings + 16 Radiating Sunburst Rays + Granulation Cells
                for (ring in 1..3) {
                    drawCircle(
                        color = Color(0xFFFEF08A).copy(alpha = 0.34f),
                        radius = r * (0.28f * ring),
                        center = Offset(cx, cy),
                        style = Stroke(width = 2.4f)
                    )
                }
                for (i in 0 until 16) {
                    val a = (i * 22.5f + timeSec * 12f) * (PI.toFloat() / 180f)
                    drawLine(
                        color = if (i % 2 == 0) Color(0xFFFEF08A).copy(alpha = 0.62f) else Color(0xFFF97316).copy(alpha = 0.45f),
                        start = Offset(cx + cos(a) * r * 0.16f, cy + sin(a) * r * 0.16f),
                        end = Offset(cx + cos(a) * r * 1.15f, cy + sin(a) * r * 1.15f),
                        strokeWidth = if (i % 2 == 0) 3.2f else 1.8f,
                        cap = StrokeCap.Round
                    )
                }
            }

            MascotBodySkin.DIAMOND_ICE_CRYSTAL -> {
                // Full-Surface Voronoi / Delaunay Polygonal Diamond Crystal Mesh Across Entire Body
                val facetStroke = (r * 0.028f).coerceAtLeast(1.6f)
                for (row in 0..3) {
                    for (col in 0..3) {
                        val x0 = bodyX + col * (bodyW * 0.25f)
                        val y0 = bodyY + row * (bodyH * 0.25f)
                        val x1 = x0 + bodyW * 0.25f
                        val y1 = y0 + bodyH * 0.25f
                        val tri = Path().apply {
                            moveTo(x0, y0)
                            lineTo(x1, y0)
                            lineTo(if ((row + col) % 2 == 0) x1 else x0, y1)
                            close()
                        }
                        drawPath(
                            path = tri,
                            color = if ((row + col) % 2 == 0) Color.White.copy(alpha = 0.22f) else Color(0xFF38BDF8).copy(alpha = 0.18f)
                        )
                        drawPath(tri, color = Color.White.copy(alpha = 0.72f), style = Stroke(width = facetStroke))
                    }
                }
            }

            MascotBodySkin.RUBY_CRIMSON_GEM -> {
                // Full-Surface Step-Cut Octagonal Emerald/Ruby Gemstone Bevel Facets + Corner Miters
                for (step in 1..3) {
                    val insetX = bodyW * (0.09f * step)
                    val insetY = bodyH * (0.09f * step)
                    drawRoundRect(
                        color = if (step % 2 == 1) Color(0xFFFECDD3).copy(alpha = 0.68f) else Color(0xFF881337).copy(alpha = 0.55f),
                        topLeft = Offset(bodyX + insetX, bodyY + insetY),
                        size = Size(bodyW - insetX * 2f, bodyH - insetY * 2f),
                        cornerRadius = CornerRadius(r * 0.14f, r * 0.14f),
                        style = Stroke(width = 2.0f)
                    )
                }
                drawLine(Color(0xFFFECDD3).copy(alpha = 0.7f), Offset(bodyX, bodyY), Offset(bodyX + bodyW * 0.27f, bodyY + bodyH * 0.27f), strokeWidth = 2f)
                drawLine(Color(0xFFFECDD3).copy(alpha = 0.7f), Offset(bodyX + bodyW, bodyY), Offset(bodyX + bodyW * 0.73f, bodyY + bodyH * 0.27f), strokeWidth = 2f)
                drawLine(Color(0xFFFECDD3).copy(alpha = 0.7f), Offset(bodyX, bodyY + bodyH), Offset(bodyX + bodyW * 0.27f, bodyY + bodyH * 0.73f), strokeWidth = 2f)
                drawLine(Color(0xFFFECDD3).copy(alpha = 0.7f), Offset(bodyX + bodyW, bodyY + bodyH), Offset(bodyX + bodyW * 0.73f, bodyY + bodyH * 0.73f), strokeWidth = 2f)
            }

            MascotBodySkin.AMETHYST_ROYAL -> {
                // Full-Surface Raw Crystalline Amethyst Geode Agate Bands & Pointed Quartz Prism Clusters
                for (ring in 1..3) {
                    drawOval(
                        color = Color(0xFFE9D5FF).copy(alpha = 0.35f),
                        topLeft = Offset(bodyX + ring * r * 0.12f, bodyY + ring * r * 0.10f),
                        size = Size(bodyW - ring * r * 0.24f, bodyH - ring * r * 0.20f),
                        style = Stroke(width = 2.2f)
                    )
                }
                for (idx in 0..7) {
                    val sx = bodyX + bodyW * (0.12f + idx * 0.11f)
                    val shardH = bodyH * (0.22f + (idx % 3) * 0.08f)
                    val shard = Path().apply {
                        moveTo(sx - r * 0.08f, bodyY + bodyH)
                        lineTo(sx, bodyY + bodyH - shardH)
                        lineTo(sx + r * 0.08f, bodyY + bodyH)
                        close()
                    }
                    drawPath(shard, color = if (idx % 2 == 0) Color(0xFFE9D5FF).copy(alpha = 0.65f) else Color(0xFFA855F7).copy(alpha = 0.65f))
                    drawPath(shard, color = Color.White, style = Stroke(width = 1.2f))
                }
            }

            MascotBodySkin.MECHA_TITANIUM -> {
                // Full-Surface Brushed Titanium Mecha Hull Plates + Panel Seams + 12 Hex Rivets + Caution Stripes
                val seamCol = Color(0xFF0F172A).copy(alpha = 0.65f)
                drawLine(seamCol, Offset(bodyX, bodyY + bodyH * 0.34f), Offset(bodyX + bodyW, bodyY + bodyH * 0.34f), strokeWidth = 2.2f)
                drawLine(seamCol, Offset(bodyX, bodyY + bodyH * 0.68f), Offset(bodyX + bodyW, bodyY + bodyH * 0.68f), strokeWidth = 2.2f)
                drawLine(seamCol, Offset(bodyX + bodyW * 0.28f, bodyY), Offset(bodyX + bodyW * 0.28f, bodyY + bodyH), strokeWidth = 2.2f)
                drawLine(seamCol, Offset(bodyX + bodyW * 0.72f, bodyY), Offset(bodyX + bodyW * 0.72f, bodyY + bodyH), strokeWidth = 2.2f)
                // Glowing Cyan Reactor Conduits
                val traceColor = Color(0xFF00F2FE).copy(alpha = 0.85f)
                drawLine(traceColor, Offset(bodyX + r * 0.10f, cy), Offset(bodyX + bodyW - r * 0.10f, cy), strokeWidth = 2.4f)
                // Yellow & Black Hazard Chevron Stripes at Bottom Corners
                for (hIdx in 0..4) {
                    val hx = bodyX + hIdx * (r * 0.14f)
                    drawLine(Color(0xFFFACC15), Offset(hx, bodyY + bodyH), Offset(hx + r * 0.10f, bodyY + bodyH - r * 0.18f), strokeWidth = 3f)
                }
                for (rx in listOf(0.12f, 0.36f, 0.64f, 0.88f)) {
                    for (ry in listOf(0.16f, 0.82f)) {
                        drawCircle(Color(0xFF334155), radius = r * 0.036f, center = Offset(bodyX + bodyW * rx, bodyY + bodyH * ry))
                    }
                }
            }

            MascotBodySkin.CARBON_KEVLAR -> {
                // Full-Surface Dense 2x2 Twill Carbon Fiber Weave Cross-Hatching + Yellow Kevlar Reinforced Stitching
                val step = (r * 0.18f).coerceAtLeast(5f)
                var x = bodyX - bodyH
                while (x < bodyX + bodyW + bodyH) {
                    drawLine(Color.White.copy(alpha = 0.16f), Offset(x, bodyY), Offset(x + bodyH, bodyY + bodyH), strokeWidth = 2.2f)
                    drawLine(Color.Black.copy(alpha = 0.32f), Offset(x + step * 0.5f, bodyY), Offset(x - bodyH + step * 0.5f, bodyY + bodyH), strokeWidth = 2.0f)
                    x += step
                }
                // Yellow Kevlar Dashed Seam Lines along Left & Right Edges
                for (d in 0..6) {
                    val dy = bodyY + d * (bodyH / 6.5f)
                    drawLine(Color(0xFFFACC15), Offset(bodyX + r * 0.12f, dy), Offset(bodyX + r * 0.12f, dy + r * 0.08f), strokeWidth = 2.2f, cap = StrokeCap.Round)
                    drawLine(Color(0xFFFACC15), Offset(bodyX + bodyW - r * 0.12f, dy), Offset(bodyX + bodyW - r * 0.12f, dy + r * 0.08f), strokeWidth = 2.2f, cap = StrokeCap.Round)
                }
            }

            MascotBodySkin.STEAMPUNK_BRONZE -> {
                // Full-Surface Victorian Riveted Brass Plates + Copper Steam Pipes + 5 Interlocking Clockwork Gears
                drawLine(Color(0xFFB45309), Offset(bodyX, bodyY + bodyH * 0.25f), Offset(bodyX + bodyW, bodyY + bodyH * 0.25f), strokeWidth = 4f)
                drawLine(Color(0xFFFDE047), Offset(bodyX, bodyY + bodyH * 0.24f), Offset(bodyX + bodyW, bodyY + bodyH * 0.24f), strokeWidth = 1.6f)
                val gearCenters = listOf(
                    Triple(bodyX + bodyW * 0.20f, bodyY + bodyH * 0.28f, r * 0.18f),
                    Triple(bodyX + bodyW * 0.38f, bodyY + bodyH * 0.42f, r * 0.13f),
                    Triple(bodyX + bodyW * 0.80f, bodyY + bodyH * 0.28f, r * 0.18f),
                    Triple(bodyX + bodyW * 0.62f, bodyY + bodyH * 0.42f, r * 0.13f),
                    Triple(cx, bodyY + bodyH * 0.76f, r * 0.20f)
                )
                gearCenters.forEachIndexed { idx, (gx, gy, gr) ->
                    val rot = (timeSec * 35f * (if (idx % 2 == 0) 1f else -1f)) % 360f
                    rotate(degrees = rot, pivot = Offset(gx, gy)) {
                        drawCircle(Color(0xFFFDE047).copy(alpha = 0.72f), radius = gr, center = Offset(gx, gy), style = Stroke(width = 3.2f))
                        for (t in 0..5) {
                            val a = (t * 60f) * (PI.toFloat() / 180f)
                            drawLine(Color(0xFFFDE047), Offset(gx + cos(a) * gr * 0.5f, gy + sin(a) * gr * 0.5f), Offset(gx + cos(a) * gr * 1.25f, gy + sin(a) * gr * 1.25f), strokeWidth = 2.6f)
                        }
                        drawCircle(Color(0xFF451A03), radius = gr * 0.36f, center = Offset(gx, gy))
                    }
                }
            }

            MascotBodySkin.NEBULA_GALAXY -> {
                // Full-Surface Milky Way Spiral Nebula Clouds + 18 Twinkling Stars + Connected Constellation Lines
                drawOval(
                    brush = Brush.radialGradient(
                        listOf(Color(0xFFEC4899).copy(alpha = 0.48f), Color(0xFF8B5CF6).copy(alpha = 0.28f), Color.Transparent),
                        center = Offset(bodyX + bodyW * 0.32f, bodyY + bodyH * 0.38f),
                        radius = r * 0.75f
                    ),
                    topLeft = Offset(bodyX, bodyY),
                    size = Size(bodyW, bodyH)
                )
                drawOval(
                    brush = Brush.radialGradient(
                        listOf(Color(0xFF00F2FE).copy(alpha = 0.45f), Color.Transparent),
                        center = Offset(bodyX + bodyW * 0.72f, bodyY + bodyH * 0.68f),
                        radius = r * 0.65f
                    ),
                    topLeft = Offset(bodyX, bodyY),
                    size = Size(bodyW, bodyH)
                )
                val stars = listOf(
                    Offset(0.15f, 0.20f), Offset(0.32f, 0.14f), Offset(0.52f, 0.24f),
                    Offset(0.74f, 0.18f), Offset(0.86f, 0.34f), Offset(0.20f, 0.68f),
                    Offset(0.42f, 0.78f), Offset(0.66f, 0.72f), Offset(0.84f, 0.80f)
                ).map { Offset(bodyX + bodyW * it.x, bodyY + bodyH * it.y) }
                for (i in 0 until stars.size - 1) {
                    if (i != 4) {
                        drawLine(Color.White.copy(alpha = 0.48f), stars[i], stars[i + 1], strokeWidth = 1.5f)
                    }
                }
                stars.forEachIndexed { idx, st ->
                    drawCircle(if (idx % 2 == 0) Color.White else DuoBeeYellow, radius = r * 0.042f, center = st)
                }
            }

            MascotBodySkin.VOID_SHADOW -> {
                // Full-Surface Abyssal Dark Matter Swirl + Jagged Purple Dimensional Rifts + Watching Void Eyes
                for (ring in 1..4) {
                    drawOval(
                        color = if (ring % 2 == 0) Color(0xFFA855F7).copy(alpha = 0.48f) else Color(0xFFEC4899).copy(alpha = 0.36f),
                        topLeft = Offset(cx - r * (0.24f * ring), cy - r * (0.18f * ring)),
                        size = Size(r * (0.48f * ring), r * (0.36f * ring)),
                        style = Stroke(width = 2.0f)
                    )
                }
                for (side in listOf(-1f, 1f)) {
                    val ex = cx + side * bodyW * 0.30f
                    val ey = bodyY + bodyH * 0.24f
                    drawOval(Color(0xFFC084FC), Offset(ex - r * 0.09f, ey - r * 0.045f), Size(r * 0.18f, r * 0.09f))
                    drawCircle(Color(0xFF090D16), radius = r * 0.028f, center = Offset(ex, ey))
                }
            }

            MascotBodySkin.BIOLUM_OCEAN -> {
                // Full-Surface Underwater Caustic Light Wave Mesh + Bioluminescent Plankton & Rising Bubbles
                for (wRow in 0..4) {
                    val wy = bodyY + bodyH * (0.15f + wRow * 0.18f)
                    val wave = Path().apply {
                        moveTo(bodyX, wy)
                        quadraticTo(bodyX + bodyW * 0.25f, wy - r * 0.10f, cx, wy)
                        quadraticTo(bodyX + bodyW * 0.75f, wy + r * 0.10f, bodyX + bodyW, wy)
                    }
                    drawPath(wave, color = Color(0xFF99F6E4).copy(alpha = 0.48f), style = Stroke(width = 2.0f))
                }
                for (b in 0..7) {
                    val bx = bodyX + bodyW * (0.12f + b * 0.11f)
                    val by = bodyY + bodyH * (0.22f + (b % 3) * 0.28f)
                    drawCircle(Color(0xFF5EEAD4).copy(alpha = 0.78f), radius = r * (0.045f + (b % 2) * 0.025f), center = Offset(bx, by), style = Stroke(width = 1.6f))
                    drawCircle(Color.White, radius = r * 0.018f, center = Offset(bx - r * 0.015f, by - r * 0.015f))
                }
            }

            MascotBodySkin.LAVA_MAGMA -> {
                // Full-Surface Cracked Volcanic Basalt Plates with Glowing Molten Magma Rivers Across Entire Body!
                val magmaGlow = Color(0xFFF97316)
                val magmaCore = Color(0xFFFEF08A)
                val fissures = listOf(
                    Pair(Offset(bodyX, bodyY + bodyH * 0.28f), Offset(cx, cy)),
                    Pair(Offset(cx, cy), Offset(bodyX + bodyW, bodyY + bodyH * 0.22f)),
                    Pair(Offset(cx, cy), Offset(bodyX + bodyW * 0.25f, bodyY + bodyH)),
                    Pair(Offset(cx, cy), Offset(bodyX + bodyW * 0.78f, bodyY + bodyH)),
                    Pair(Offset(bodyX + bodyW * 0.30f, bodyY), Offset(bodyX + bodyW * 0.18f, bodyY + bodyH * 0.62f)),
                    Pair(Offset(bodyX + bodyW * 0.72f, bodyY), Offset(bodyX + bodyW * 0.84f, bodyY + bodyH * 0.66f))
                )
                fissures.forEach { (p1, p2) ->
                    drawLine(magmaGlow, p1, p2, strokeWidth = (r * 0.075f).coerceAtLeast(3.2f), cap = StrokeCap.Round)
                    drawLine(magmaCore, p1, p2, strokeWidth = (r * 0.032f).coerceAtLeast(1.6f), cap = StrokeCap.Round)
                }
            }

            MascotBodySkin.CYBER_EMERALD -> {
                // Full-Surface Emerald Matrix Digital Rain Grid & Hex Data Nodes Across Entire Body
                for (col in 1..6) {
                    val gx = bodyX + bodyW * (col / 7f)
                    drawLine(Color(0xFF6EE7B7).copy(alpha = 0.38f), Offset(gx, bodyY), Offset(gx, bodyY + bodyH), strokeWidth = 1.6f)
                    for (row in 1..4) {
                        val gy = bodyY + bodyH * (row / 5f)
                        if ((col + row) % 2 == 0) {
                            drawRoundRect(
                                color = Color(0xFFA7F3D0).copy(alpha = 0.75f),
                                topLeft = Offset(gx - r * 0.04f, gy - r * 0.06f),
                                size = Size(r * 0.08f, r * 0.12f),
                                cornerRadius = CornerRadius(2f, 2f)
                            )
                        }
                    }
                }
            }

            MascotBodySkin.RAINBOW_PRISM_RGB -> {
                // Full-Surface Iridescent Rainbow Prism Diagonal Foil Bands + Shimmering Diamond Grid
                val rainbowCols = listOf(
                    Color(0xFFEF4444), Color(0xFFFACC15), Color(0xFF22C55E),
                    Color(0xFF00F2FE), Color(0xFF8B5CF6), Color(0xFFEC4899)
                )
                for (b in 0..5) {
                    val yOff = bodyY + b * (bodyH / 5.5f)
                    drawLine(
                        color = rainbowCols[b].copy(alpha = 0.46f),
                        start = Offset(bodyX, yOff),
                        end = Offset(bodyX + bodyW, yOff - bodyH * 0.25f),
                        strokeWidth = r * 0.16f
                    )
                }
                for (s in 0..5) {
                    val sx = bodyX + bodyW * (0.15f + s * 0.14f)
                    val sy = bodyY + bodyH * (if (s % 2 == 0) 0.26f else 0.74f)
                    drawCircle(Color.White.copy(alpha = 0.85f), radius = r * 0.045f, center = Offset(sx, sy))
                }
            }

            MascotBodySkin.SAKURA_MOCHI -> {
                // Full-Surface Japanese Sakura Mochi Texture with 6 Full 5-Petal Cherry Blossoms & Leaf Accents
                val blossomPositions = listOf(
                    Offset(0.18f, 0.22f), Offset(0.82f, 0.20f),
                    Offset(0.14f, 0.68f), Offset(0.86f, 0.66f),
                    Offset(0.38f, 0.82f), Offset(0.64f, 0.18f)
                )
                blossomPositions.forEach { rel ->
                    val fx = bodyX + bodyW * rel.x
                    val fy = bodyY + bodyH * rel.y
                    for (p in 0..4) {
                        val a = (p * 72f) * (PI.toFloat() / 180f)
                        drawCircle(Color.White.copy(alpha = 0.82f), radius = r * 0.048f, center = Offset(fx + cos(a) * r * 0.062f, fy + sin(a) * r * 0.062f))
                    }
                    drawCircle(Color(0xFFFACC15), radius = r * 0.030f, center = Offset(fx, fy))
                }
            }

            MascotBodySkin.COTTON_CANDY -> {
                // Full-Surface Swirled Pastel Pink & Blue Cotton Candy Waves + 18 Rainbow Sugar Sprinkles!
                for (w in 0..3) {
                    val wy = bodyY + bodyH * (0.18f + w * 0.22f)
                    val swirl = Path().apply {
                        moveTo(bodyX, wy)
                        cubicTo(bodyX + bodyW * 0.33f, wy - r * 0.16f, bodyX + bodyW * 0.66f, wy + r * 0.16f, bodyX + bodyW, wy)
                    }
                    drawPath(
                        path = swirl,
                        color = if (w % 2 == 0) Color(0xFF7DD3FC).copy(alpha = 0.55f) else Color(0xFFFBCFE8).copy(alpha = 0.65f),
                        style = Stroke(width = r * 0.12f, cap = StrokeCap.Round)
                    )
                }
                val sprinkleCols = listOf(Color.White, Color(0xFFFEF08A), Color(0xFF38BDF8), Color(0xFF4ADE80))
                for (s in 0..13) {
                    val sx = bodyX + bodyW * (0.10f + (s % 7) * 0.13f)
                    val sy = bodyY + bodyH * (0.16f + (s / 7) * 0.62f + (s % 3) * 0.06f)
                    drawLine(sprinkleCols[s % 4], Offset(sx - r * 0.045f, sy - r * 0.025f), Offset(sx + r * 0.045f, sy + r * 0.025f), strokeWidth = 3f, cap = StrokeCap.Round)
                }
            }

            MascotBodySkin.FOREST_SPIRIT -> {
                // Full-Surface Ancient Wood Bark Grain Rings + Winding Emerald Vines & Glowing Forest Spores
                for (ring in 1..4) {
                    drawArc(
                        color = Color(0xFF064E3B).copy(alpha = 0.42f),
                        startAngle = 0f,
                        sweepAngle = 180f,
                        useCenter = false,
                        topLeft = Offset(bodyX + ring * r * 0.10f, bodyY - bodyH * 0.2f + ring * r * 0.16f),
                        size = Size(bodyW - ring * r * 0.20f, bodyH * 0.8f),
                        style = Stroke(width = 2.0f)
                    )
                }
                for (side in listOf(-1f, 1f)) {
                    val vx = cx + side * bodyW * 0.34f
                    drawLine(Color(0xFF86EFAC), Offset(vx, bodyY + bodyH), Offset(vx - side * r * 0.18f, bodyY + r * 0.12f), strokeWidth = 2.6f, cap = StrokeCap.Round)
                    for (lf in 0..2) {
                        val ly = bodyY + bodyH * (0.25f + lf * 0.25f)
                        drawOval(Color(0xFFFEF08A), Offset(vx - r * 0.08f, ly), Size(r * 0.16f, r * 0.09f))
                    }
                }
            }

            MascotBodySkin.PHANTOM_HOLOGRAM -> {
                // Full-Surface 3D Sci-Fi Wireframe Grid (Horizontal + Vertical Mesh) + Animated Scanlines + Glitch Bars
                val wireCol = Color.White.copy(alpha = 0.35f)
                for (v in 1..5) {
                    val vx = bodyX + bodyW * (v / 6f)
                    drawLine(wireCol, Offset(vx, bodyY), Offset(vx, bodyY + bodyH), strokeWidth = 1.4f)
                }
                val scanOffset = (timeSec * 22f) % (bodyH * 0.20f)
                for (hIdx in 0..4) {
                    val sy = (bodyY + hIdx * (bodyH * 0.20f) + scanOffset).coerceAtMost(bodyY + bodyH)
                    drawLine(Color(0xFF00F2FE).copy(alpha = 0.68f), Offset(bodyX, sy), Offset(bodyX + bodyW, sy), strokeWidth = 1.8f)
                }
            }

            MascotBodySkin.OBSIDIAN_STEALTH -> {
                // Full-Surface F-117 Stealth Angular Radar-Absorbent Diamond Facet Tiles + Amber Tactical HUD Lines
                val tileStroke = Color(0xFFF59E0B).copy(alpha = 0.72f)
                for (i in 0..3) {
                    val xOff = bodyX + i * (bodyW * 0.33f)
                    drawLine(tileStroke, Offset(xOff, bodyY), Offset(xOff + bodyW * 0.33f, bodyY + bodyH), strokeWidth = 1.8f)
                    drawLine(tileStroke, Offset(xOff + bodyW * 0.33f, bodyY), Offset(xOff, bodyY + bodyH), strokeWidth = 1.8f)
                }
                drawRoundRect(
                    color = Color(0xFFF59E0B),
                    topLeft = Offset(bodyX + r * 0.07f, bodyY + r * 0.07f),
                    size = Size(bodyW - r * 0.14f, bodyH - r * 0.14f),
                    cornerRadius = CornerRadius(r * 0.24f, r * 0.24f),
                    style = Stroke(width = 2.2f)
                )
            }

            MascotBodySkin.CELESTIAL_MARBLE -> {
                // Full-Surface Carrara White Marble with Branching Grey & 24K Gold Kintsugi Veins Across Entire Body
                for (vIdx in 0..2) {
                    val startX = bodyX + bodyW * (0.16f + vIdx * 0.32f)
                    val kintsugi = Path().apply {
                        moveTo(startX, bodyY)
                        quadraticTo(startX + r * 0.22f, bodyY + bodyH * 0.38f, startX - r * 0.16f, bodyY + bodyH * 0.68f)
                        lineTo(startX + r * 0.12f, bodyY + bodyH)
                    }
                    drawPath(kintsugi, color = Color(0xFF94A3B8).copy(alpha = 0.45f), style = Stroke(width = 4.2f, cap = StrokeCap.Round))
                    drawPath(kintsugi, color = Color(0xFFF59E0B).copy(alpha = 0.90f), style = Stroke(width = 2.0f, cap = StrokeCap.Round))
                }
            }

            MascotBodySkin.EMPEROR_JADE -> {
                // Full-Surface Imperial Carved Jade with Chinese Ruyi Cloud Scrolls & Gold Fretwork Border
                drawRoundRect(
                    color = Color(0xFFFACC15).copy(alpha = 0.88f),
                    topLeft = Offset(bodyX + r * 0.08f, bodyY + r * 0.08f),
                    size = Size(bodyW - r * 0.16f, bodyH - r * 0.16f),
                    cornerRadius = CornerRadius(r * 0.22f, r * 0.22f),
                    style = Stroke(width = 2.4f)
                )
                for (row in 0..1) {
                    for (col in 0..2) {
                        val jx = bodyX + bodyW * (0.20f + col * 0.30f)
                        val jy = bodyY + bodyH * (0.28f + row * 0.44f)
                        drawArc(
                            color = Color(0xFFA7F3D0).copy(alpha = 0.68f),
                            startAngle = 0f,
                            sweepAngle = 240f,
                            useCenter = false,
                            topLeft = Offset(jx - r * 0.12f, jy - r * 0.10f),
                            size = Size(r * 0.24f, r * 0.20f),
                            style = Stroke(width = 2.0f, cap = StrokeCap.Round)
                        )
                    }
                }
            }

            MascotBodySkin.DRAGON_SCALE_RUBY -> {
                // Full-Surface Dense 5x6 Overlapping 3D Dragon Scale Mail with 24K Gold Rims Covering Entire Body!
                val scaleRim = Color(0xFFFACC15).copy(alpha = 0.82f)
                val scaleHighlight = Color(0xFFFECDD3).copy(alpha = 0.35f)
                for (row in 0..4) {
                    for (col in 0..5) {
                        val sx = bodyX + col * (bodyW / 5.2f) + (row % 2) * (bodyW / 10.4f)
                        val sy = bodyY + row * (bodyH / 4.6f)
                        drawArc(
                            color = scaleHighlight,
                            startAngle = 10f,
                            sweepAngle = 160f,
                            useCenter = true,
                            topLeft = Offset(sx - r * 0.16f, sy - r * 0.10f),
                            size = Size(r * 0.32f, r * 0.24f)
                        )
                        drawArc(
                            color = scaleRim,
                            startAngle = 10f,
                            sweepAngle = 160f,
                            useCenter = false,
                            topLeft = Offset(sx - r * 0.16f, sy - r * 0.10f),
                            size = Size(r * 0.32f, r * 0.24f),
                            style = Stroke(width = 1.8f, cap = StrokeCap.Round)
                        )
                    }
                }
            }

            MascotBodySkin.GUNDAM_AERO_COMPOSITE -> {
                // Full-Surface RX-78 Gundam Tri-Color Armor Panels + Recessed Panel Lines + Yellow Exhaust Louvers
                val panelCol = Color(0xFF1E3A8A).copy(alpha = 0.85f)
                // Upper Blue Mecha Shoulder/Chest Blocks on Left & Right
                drawRect(Color(0xFF2563EB).copy(alpha = 0.42f), Offset(bodyX, bodyY), Size(bodyW * 0.26f, bodyH * 0.45f))
                drawRect(Color(0xFF2563EB).copy(alpha = 0.42f), Offset(bodyX + bodyW * 0.74f, bodyY), Size(bodyW * 0.26f, bodyH * 0.45f))
                // Lower Red Mecha Waist Trim
                drawRect(Color(0xFFEF4444).copy(alpha = 0.48f), Offset(bodyX, bodyY + bodyH * 0.80f), Size(bodyW, bodyH * 0.20f))
                drawLine(panelCol, Offset(bodyX + bodyW * 0.26f, bodyY), Offset(bodyX + bodyW * 0.26f, bodyY + bodyH), strokeWidth = 2.0f)
                drawLine(panelCol, Offset(bodyX + bodyW * 0.74f, bodyY), Offset(bodyX + bodyW * 0.74f, bodyY + bodyH), strokeWidth = 2.0f)
                for (side in listOf(-1f, 1f)) {
                    val vx = cx + side * bodyW * 0.36f - r * 0.09f
                    drawRoundRect(Color(0xFFFACC15), Offset(vx, bodyY + bodyH * 0.18f), Size(r * 0.18f, r * 0.11f), CornerRadius(3f, 3f))
                    drawRoundRect(Color(0xFF22C55E), Offset(vx, bodyY + bodyH * 0.34f), Size(r * 0.18f, r * 0.06f), CornerRadius(2f, 2f))
                }
            }

            MascotBodySkin.CYBERPUNK_NEON_CIRCUIT -> {
                // Full-Surface Gold & Cyan Motherboard PCB Traces + Solder Pads + IC Microprocessor Pins Across Entire Body
                val pcbTrace = Color(0xFF00F2FE)
                val pcbGold = Color(0xFFFACC15)
                for (row in 1..4) {
                    val ty = bodyY + bodyH * (row / 5f)
                    for (side in listOf(-1f, 1f)) {
                        val edgeX = if (side < 0) bodyX else bodyX + bodyW
                        val bendX = cx + side * bodyW * (0.18f + (row % 2) * 0.10f)
                        drawLine(if (row % 2 == 0) pcbTrace else pcbGold, Offset(edgeX, ty), Offset(bendX, ty), strokeWidth = 2.0f)
                        drawLine(if (row % 2 == 0) pcbTrace else pcbGold, Offset(bendX, ty), Offset(bendX, ty + r * 0.12f), strokeWidth = 2.0f)
                        drawCircle(pcbGold, radius = r * 0.042f, center = Offset(bendX, ty + r * 0.12f))
                        drawCircle( Color(0xFF090D16), radius = r * 0.018f, center = Offset(bendX, ty + r * 0.12f))
                    }
                }
            }

            MascotBodySkin.CELESTIAL_SERAPH_FEATHER -> {
                // Full-Surface Overlapping Angelic Swan/Seraph Plumage Rows Covering Entire Body!
                for (row in 0..4) {
                    for (col in 0..4) {
                        val fx = bodyX + col * (bodyW / 4.2f) + (row % 2) * (bodyW / 8.4f)
                        val fy = bodyY + row * (bodyH / 4.4f)
                        drawArc(
                            color = Color.White.copy(alpha = 0.65f),
                            startAngle = 15f,
                            sweepAngle = 150f,
                            useCenter = false,
                            topLeft = Offset(fx - r * 0.18f, fy - r * 0.10f),
                            size = Size(r * 0.36f, r * 0.24f),
                            style = Stroke(width = 2.4f, cap = StrokeCap.Round)
                        )
                        drawArc(
                            color = Color(0xFFFACC15).copy(alpha = 0.72f),
                            startAngle = 35f,
                            sweepAngle = 110f,
                            useCenter = false,
                            topLeft = Offset(fx - r * 0.14f, fy - r * 0.06f),
                            size = Size(r * 0.28f, r * 0.18f),
                            style = Stroke(width = 1.5f, cap = StrokeCap.Round)
                        )
                    }
                }
            }

            MascotBodySkin.ABYSSAL_DEMON_CHITIN -> {
                // Full-Surface Biomechanical Demon Exoskeleton Rib Plates & Hellfire Spine Fissures
                for (rib in 0..4) {
                    val ry = bodyY + bodyH * (0.14f + rib * 0.18f)
                    val leftRib = Path().apply {
                        moveTo(bodyX, ry)
                        quadraticTo(bodyX + bodyW * 0.25f, ry - r * 0.08f, cx - r * 0.06f, ry + r * 0.10f)
                    }
                    val rightRib = Path().apply {
                        moveTo(bodyX + bodyW, ry)
                        quadraticTo(bodyX + bodyW * 0.75f, ry - r * 0.08f, cx + r * 0.06f, ry + r * 0.10f)
                    }
                    drawPath(leftRib, color = Color(0xFFEF4444).copy(alpha = 0.82f), style = Stroke(width = 2.8f, cap = StrokeCap.Round))
                    drawPath(rightRib, color = Color(0xFFEF4444).copy(alpha = 0.82f), style = Stroke(width = 2.8f, cap = StrokeCap.Round))
                }
                drawLine(Color(0xFFFEF08A), Offset(cx, bodyY), Offset(cx, bodyY + bodyH), strokeWidth = 2.4f)
            }

            MascotBodySkin.ROYAL_DAMASCUS_GOLD -> {
                // Full-Surface Forged Valyrian Damascus Steel — 8 Hypnotic Wavy Steel & Gold Wood-Grain Layers!
                for (wIdx in 0..7) {
                    val wy = bodyY + bodyH * (0.08f + wIdx * 0.12f)
                    val wave = Path().apply {
                        moveTo(bodyX, wy)
                        cubicTo(
                            bodyX + bodyW * 0.25f, wy - r * 0.14f,
                            bodyX + bodyW * 0.50f, wy + r * 0.14f,
                            bodyX + bodyW * 0.75f, wy - r * 0.10f
                        )
                        lineTo(bodyX + bodyW, wy)
                    }
                    drawPath(
                        path = wave,
                        color = if (wIdx % 2 == 0) Color(0xFFFACC15).copy(alpha = 0.62f) else Color(0xFFF8FAFC).copy(alpha = 0.42f),
                        style = Stroke(width = 2.0f, cap = StrokeCap.Round)
                    )
                }
            }

            MascotBodySkin.ASTRAL_CONSTELLATION_MAP -> {
                // Full-Surface Celestial Star-Chart Globe Grid (Latitude/Longitude) + Astrolabe Rings + Zodiac Stars
                val gridGold = Color(0xFFFDE047).copy(alpha = 0.42f)
                for (lat in 1..3) {
                    val ly = bodyY + bodyH * (lat / 4f)
                    drawLine(gridGold, Offset(bodyX, ly), Offset(bodyX + bodyW, ly), strokeWidth = 1.4f)
                }
                for (lon in 1..3) {
                    val lx = bodyX + bodyW * (lon / 4f)
                    drawLine(gridGold, Offset(lx, bodyY), Offset(lx, bodyY + bodyH), strokeWidth = 1.4f)
                }
                drawCircle(Color(0xFFFDE047).copy(alpha = 0.65f), radius = r * 0.64f, center = Offset(cx, cy), style = Stroke(width = 1.8f))
                drawCircle(Color(0xFF38BDF8).copy(alpha = 0.55f), radius = r * 0.44f, center = Offset(cx, cy), style = Stroke(width = 1.5f))
            }

            MascotBodySkin.SAMURAI_LACQUER_ARMOR -> {
                // Full-Surface Japanese Kozane Laminar Armor — Horizontal Lacquer Plates + Gold Silk Odoshi X-Lacing!
                for (row in 0..3) {
                    val py = bodyY + row * (bodyH * 0.25f)
                    drawLine(Color(0xFF090D16).copy(alpha = 0.75f), Offset(bodyX, py), Offset(bodyX + bodyW, py), strokeWidth = 2.6f)
                    for (col in 0..5) {
                        val lx = bodyX + bodyW * (0.10f + col * 0.16f)
                        drawLine(Color(0xFFFACC15), Offset(lx - r * 0.04f, py - r * 0.06f), Offset(lx + r * 0.04f, py + r * 0.06f), strokeWidth = 2.2f, cap = StrokeCap.Round)
                        drawLine(Color(0xFFFACC15), Offset(lx + r * 0.04f, py - r * 0.06f), Offset(lx - r * 0.04f, py + r * 0.06f), strokeWidth = 2.2f, cap = StrokeCap.Round)
                    }
                }
            }

            MascotBodySkin.GLACIER_AURORA_PRISM -> {
                // Full-Surface Multi-Layered Northern Lights Borealis Curtains Waving Across Entire Body
                for (band in 0..3) {
                    val by = bodyY + bodyH * (0.20f + band * 0.20f)
                    val auroraPath = Path().apply {
                        moveTo(bodyX, by)
                        cubicTo(
                            cx - r * 0.35f, by - r * 0.22f,
                            cx + r * 0.35f, by + r * 0.22f,
                            bodyX + bodyW, by - r * 0.08f
                        )
                    }
                    drawPath(
                        path = auroraPath,
                        brush = Brush.horizontalGradient(listOf(Color(0xFF6EE7B7), Color(0xFF38BDF8), Color(0xFFF472B6), Color(0xFFFDE047))),
                        style = Stroke(width = (r * 0.09f).coerceAtLeast(3.2f), cap = StrokeCap.Round)
                    )
                }
            }

            MascotBodySkin.THUNDER_PLASMA_COIL -> {
                // Full-Surface Tesla Plasma Globe — Central Electrode Core with 8 Branching Lightning Filaments to Body Edges
                for (b in 0..7) {
                    val a = (b * 45f + timeSec * 25f) * (PI.toFloat() / 180f)
                    val midX = cx + cos(a + 0.25f) * (r * 0.45f)
                    val midY = cy + sin(a + 0.25f) * (r * 0.45f)
                    val endX = cx + cos(a) * (r * 0.95f)
                    val endY = cy + sin(a) * (r * 0.95f)
                    val filament = Path().apply {
                        moveTo(cx, cy)
                        lineTo(midX, midY)
                        lineTo(endX, endY)
                    }
                    drawPath(filament, color = if (b % 2 == 0) Color(0xFFFEF08A) else Color(0xFF38BDF8), style = Stroke(width = 2.4f, cap = StrokeCap.Round))
                }
            }

            MascotBodySkin.PLUSH_VELVET_QUILT -> {
                // Full-Surface Luxury Diamond-Quilted Plush Velvet Fabric with Golden Pearl Buttons at Every Intersection!
                val stitchCol = Color(0xFFFBCFE8).copy(alpha = 0.62f)
                val step = (bodyW / 3.5f).coerceAtLeast(10f)
                var d = -bodyH
                while (d < bodyW + bodyH) {
                    drawLine(stitchCol, Offset(bodyX + d, bodyY), Offset(bodyX + d + bodyH, bodyY + bodyH), strokeWidth = 1.6f)
                    drawLine(stitchCol, Offset(bodyX + d, bodyY + bodyH), Offset(bodyX + d + bodyH, bodyY), strokeWidth = 1.6f)
                    d += step
                }
                for (row in 1..2) {
                    for (col in 1..3) {
                        val px = bodyX + col * (bodyW * 0.25f)
                        val py = bodyY + row * (bodyH * 0.33f)
                        drawCircle(Color(0xFFFACC15), radius = r * 0.038f, center = Offset(px, py))
                    }
                }
            }

            MascotBodySkin.BIO_MECH_SYMBIOTE -> {
                // Full-Surface Alien Symbiote Bio-Webbing Tendrils & Pulsing Neon Bio-Nodes Across Entire Body
                val tendril = Color(0xFF4ADE80)
                for (i in 0..5) {
                    val startX = bodyX + (i % 3) * (bodyW * 0.45f)
                    val startY = if (i < 3) bodyY else bodyY + bodyH
                    val web = Path().apply {
                        moveTo(startX, startY)
                        quadraticTo(cx + (i - 2.5f) * r * 0.18f, cy, bodyX + bodyW * ((i + 1) % 3) * 0.45f, bodyY + bodyH * 0.5f)
                    }
                    drawPath(web, color = tendril.copy(alpha = 0.78f), style = Stroke(width = 2.4f, cap = StrokeCap.Round))
                }
                for (side in listOf(-1f, 1f)) {
                    drawCircle(Color(0xFFFEF08A), radius = r * 0.055f, center = Offset(cx + side * bodyW * 0.28f, bodyY + bodyH * 0.28f))
                    drawCircle(Color(0xFF10B981), radius = r * 0.030f, center = Offset(cx + side * bodyW * 0.28f, bodyY + bodyH * 0.28f))
                }
            }

            MascotBodySkin.SUN_BREATHING_SCAR_SKIN -> {
                // Demon Slayer Hinokami Kagura Sun Flame Wave Pattern Across Body
                for (wave in 0..3) {
                    val wy = bodyY + bodyH * (0.20f + wave * 0.20f)
                    val flameWave = Path().apply {
                        moveTo(bodyX, wy)
                        quadraticTo(cx - bodyW * 0.25f, wy - r * 0.16f, cx, wy)
                        quadraticTo(cx + bodyW * 0.25f, wy + r * 0.16f, bodyX + bodyW, wy)
                    }
                    drawPath(flameWave, color = Color(0xFFFEF08A).copy(alpha = 0.65f), style = Stroke(width = 2.6f, cap = StrokeCap.Round))
                }
            }

            MascotBodySkin.TITAN_STEAM_MUSCLE_SKIN -> {
                // Attack on Titan Colossal Steam & Striated Muscle Energy Lines
                val striationCol = Color(0xFFFEF08A).copy(alpha = 0.55f)
                for (i in -3..3) {
                    val sx = cx + i * (bodyW * 0.13f)
                    drawLine(striationCol, Offset(sx, bodyY + r * 0.08f), Offset(sx + i * r * 0.05f, bodyY + bodyH - r * 0.08f), strokeWidth = 2.2f)
                }
            }

            MascotBodySkin.HOLLOW_SPIRIT_REIATSU -> {
                // Bleach Hollow Black-Red Spiritual Pressure Lightning Cracks
                for (side in listOf(-1f, 1f)) {
                    val crack = Path().apply {
                        moveTo(cx + side * bodyW * 0.15f, bodyY)
                        lineTo(cx + side * bodyW * 0.35f, cy - r * 0.10f)
                        lineTo(cx + side * bodyW * 0.18f, cy + r * 0.18f)
                        lineTo(cx + side * bodyW * 0.40f, bodyY + bodyH)
                    }
                    drawPath(crack, color = Color(0xFFF43F5E), style = Stroke(width = 2.6f, cap = StrokeCap.Round))
                }
            }

            MascotBodySkin.NINE_TAILS_CHAKRA_CLOAK -> {
                // Naruto Kurama Six Paths Chakra Seal Lines & Black Magatama Symbols
                drawCircle(Color(0xFF090D16), radius = r * 0.16f, center = Offset(cx, cy + r * 0.15f), style = Stroke(width = 2.8f))
                for (m in -1..1) {
                    drawCircle(Color(0xFF090D16), radius = r * 0.055f, center = Offset(cx + m * r * 0.34f, bodyY + bodyH * 0.25f))
                }
            }

            MascotBodySkin.TIME_CHRONO_GRID_SKIN -> {
                // Link Click Golden Clockwork Rings & Time-Coordinate Grid
                val gridCol = Color(0xFFFACC15).copy(alpha = 0.50f)
                drawCircle(gridCol, radius = r * 0.48f, center = Offset(cx, cy), style = Stroke(width = 2.0f))
                drawLine(gridCol, Offset(bodyX, cy), Offset(bodyX + bodyW, cy), strokeWidth = 1.6f)
                drawLine(gridCol, Offset(cx, bodyY), Offset(cx, bodyY + bodyH), strokeWidth = 1.6f)
            }

            MascotBodySkin.SOUL_RING_DIVINE_GOLD -> {
                // Soul Land Divine Sea God Golden Trident Runes & Azure Ripples
                val runeCol = Color(0xFFFEF08A).copy(alpha = 0.72f)
                drawOval(runeCol, Offset(cx - bodyW * 0.36f, cy - r * 0.22f), Size(bodyW * 0.72f, r * 0.44f), style = Stroke(width = 2.2f))
                drawCircle(Color.White, radius = r * 0.06f, center = Offset(cx, bodyY + bodyH * 0.22f))
            }

            MascotBodySkin.HEAVENLY_FLAME_LOTUS_SKIN -> {
                // BTTH Three-Color Heavenly Flame Swirls (Jade, Violet, Gold)
                drawArc(Color(0xFFFEF08A).copy(alpha = 0.7f), 0f, 180f, false, Offset(bodyX + r * 0.15f, bodyY + r * 0.15f), Size(bodyW - r * 0.30f, bodyH * 0.6f), style = Stroke(width = 2.4f))
                drawArc(Color(0xFF6EE7B7).copy(alpha = 0.7f), 180f, 180f, false, Offset(bodyX + r * 0.22f, bodyY + r * 0.25f), Size(bodyW - r * 0.44f, bodyH * 0.6f), style = Stroke(width = 2.4f))
            }

            MascotBodySkin.NANO_HEX_TITANIUM_RED -> {
                // Superhero Iron Nano-Tech Gold Panel Lines & Hexagonal Matrix
                val panelCol = Color(0xFFFACC15).copy(alpha = 0.75f)
                drawLine(panelCol, Offset(bodyX + bodyW * 0.22f, bodyY), Offset(cx - r * 0.15f, bodyY + bodyH), strokeWidth = 2.2f)
                drawLine(panelCol, Offset(bodyX + bodyW * 0.78f, bodyY), Offset(cx + r * 0.15f, bodyY + bodyH), strokeWidth = 2.2f)
            }

            MascotBodySkin.VIBRANIUM_KINETIC_WEAVE -> {
                // Superhero Vibranium Kinetic Purple Chevron Pulse Lines
                for (row in 0..2) {
                    val vy = bodyY + bodyH * (0.24f + row * 0.24f)
                    val chev = Path().apply {
                        moveTo(cx - bodyW * 0.34f, vy - r * 0.08f)
                        lineTo(cx, vy + r * 0.08f)
                        lineTo(cx + bodyW * 0.34f, vy - r * 0.08f)
                    }
                    drawPath(chev, color = Color(0xFFC084FC), style = Stroke(width = 2.2f, cap = StrokeCap.Round))
                }
            }

            MascotBodySkin.ELVEN_STARLIGHT_RUNE -> {
                // High-Fantasy Elven Golden Botanical Filigree Vines
                val goldVine = Color(0xFFFEF08A).copy(alpha = 0.75f)
                drawArc(goldVine, 30f, 120f, false, Offset(bodyX + r * 0.12f, bodyY + r * 0.12f), Size(bodyW - r * 0.24f, bodyH * 0.75f), style = Stroke(width = 2.2f))
                drawCircle(Color.White, radius = r * 0.05f, center = Offset(cx, bodyY + bodyH * 0.25f))
            }
        }
    }
}

// ============================================================================
// 3. UPPER OUTFIT / SHIRT / ARMOR RENDERER (34 DETAILED 3D OUTFITS — 0 DUPLICATES)
// ============================================================================

internal fun DrawScope.drawMascotUpperOutfitLayer(
    bodyX: Float,
    bodyY: Float,
    bodyW: Float,
    bodyH: Float,
    cx: Float,
    cy: Float,
    r: Float,
    outfit: MascotUpperOutfit
) {
    if (outfit == MascotUpperOutfit.NONE) return

    val shirtTopY = cy + r * 0.30f
    val shirtBottomY = bodyY + bodyH
    val shirtH = (shirtBottomY - shirtTopY).coerceAtLeast(r * 0.40f)
    val shirtRadius = r * 0.30f

    // Helper for 3D Shoulder Pauldrons extending beyond the torso silhouette
    fun drawShoulderPauldrons(
        mainColor: Color,
        trimColor: Color,
        spiked: Boolean = false
    ) {
        for (side in listOf(-1f, 1f)) {
            val px = cx + side * (bodyW * 0.48f)
            val py = shirtTopY + r * 0.06f
            drawRoundRect(
                color = mainColor,
                topLeft = Offset(px - r * 0.22f, py - r * 0.14f),
                size = Size(r * 0.44f, r * 0.28f),
                cornerRadius = CornerRadius(r * 0.10f, r * 0.10f)
            )
            drawRoundRect(
                color = trimColor,
                topLeft = Offset(px - r * 0.22f, py - r * 0.14f),
                size = Size(r * 0.44f, r * 0.28f),
                cornerRadius = CornerRadius(r * 0.10f, r * 0.10f),
                style = Stroke(width = (r * 0.036f).coerceAtLeast(1.8f))
            )
            if (spiked) {
                val spike = Path().apply {
                    moveTo(px + side * r * 0.06f, py - r * 0.14f)
                    lineTo(px + side * r * 0.32f, py - r * 0.34f)
                    lineTo(px + side * r * 0.20f, py + r * 0.02f)
                    close()
                }
                drawPath(spike, color = trimColor)
            }
        }
    }

    when (outfit) {
        MascotUpperOutfit.NONE -> Unit

        MascotUpperOutfit.CYBER_HOODIE -> {
            drawRoundRect(
                brush = Brush.verticalGradient(listOf(Color(0xFF0284C7), Color(0xFF0369A1))),
                topLeft = Offset(bodyX, shirtTopY),
                size = Size(bodyW, shirtH),
                cornerRadius = CornerRadius(shirtRadius, shirtRadius)
            )
            // Raised 3D Tech Hood Collar
            drawRoundRect(
                color = Color(0xFF0F172A),
                topLeft = Offset(cx - r * 0.56f, shirtTopY - r * 0.08f),
                size = Size(r * 1.12f, r * 0.16f),
                cornerRadius = CornerRadius(r * 0.08f, r * 0.08f)
            )
            drawLine(Color(0xFF00F2FE), Offset(cx - r * 0.28f, shirtTopY), Offset(cx - r * 0.14f, shirtTopY + shirtH * 0.52f), strokeWidth = (r * 0.05f).coerceAtLeast(2.2f), cap = StrokeCap.Round)
            drawLine(Color(0xFF00F2FE), Offset(cx + r * 0.28f, shirtTopY), Offset(cx + r * 0.14f, shirtTopY + shirtH * 0.52f), strokeWidth = (r * 0.05f).coerceAtLeast(2.2f), cap = StrokeCap.Round)
            drawRoundRect(
                color = Color(0xFF075985),
                topLeft = Offset(cx - r * 0.48f, shirtTopY + shirtH * 0.42f),
                size = Size(r * 0.96f, shirtH * 0.46f),
                cornerRadius = CornerRadius(r * 0.12f, r * 0.12f)
            )
        }

        MascotUpperOutfit.EXECUTIVE_TUXEDO -> {
            drawRoundRect(
                color = Color(0xFF0F172A),
                topLeft = Offset(bodyX, shirtTopY),
                size = Size(bodyW, shirtH),
                cornerRadius = CornerRadius(shirtRadius, shirtRadius)
            )
            val shirtTriangle = Path().apply {
                moveTo(cx - r * 0.38f, shirtTopY)
                lineTo(cx + r * 0.38f, shirtTopY)
                lineTo(cx, shirtTopY + shirtH * 0.92f)
                close()
            }
            drawPath(path = shirtTriangle, color = Color.White)
            drawLine(Color(0xFF334155), Offset(cx - r * 0.38f, shirtTopY), Offset(cx, shirtTopY + shirtH * 0.92f), strokeWidth = 3f)
            drawLine(Color(0xFF334155), Offset(cx + r * 0.38f, shirtTopY), Offset(cx, shirtTopY + shirtH * 0.92f), strokeWidth = 3f)
            // Gold pocket handkerchief square
            val pocketSq = Path().apply {
                moveTo(cx + r * 0.36f, shirtTopY + shirtH * 0.42f)
                lineTo(cx + r * 0.48f, shirtTopY + shirtH * 0.26f)
                lineTo(cx + r * 0.58f, shirtTopY + shirtH * 0.42f)
                close()
            }
            drawPath(pocketSq, color = Color(0xFFFACC15))
            val bowY = shirtTopY + r * 0.09f
            val bowPath = Path().apply {
                moveTo(cx, bowY)
                lineTo(cx - r * 0.24f, bowY - r * 0.11f)
                lineTo(cx - r * 0.24f, bowY + r * 0.11f)
                close()
                moveTo(cx, bowY)
                lineTo(cx + r * 0.24f, bowY - r * 0.11f)
                lineTo(cx + r * 0.24f, bowY + r * 0.11f)
                close()
            }
            drawPath(path = bowPath, color = Color(0xFFEF4444))
            drawCircle(color = Color(0xFFFACC15), radius = r * 0.055f, center = Offset(cx, bowY))
        }

        MascotUpperOutfit.ROYAL_KING_CLOAK -> {
            drawRoundRect(
                brush = Brush.verticalGradient(listOf(Color(0xFFDC2626), Color(0xFF991B1B))),
                topLeft = Offset(bodyX, shirtTopY),
                size = Size(bodyW, shirtH),
                cornerRadius = CornerRadius(shirtRadius, shirtRadius)
            )
            drawShoulderPauldrons(mainColor = Color(0xFFFACC15), trimColor = Color(0xFFB45309))
            // Royal white ermine fur collar with black spots
            drawRoundRect(
                color = Color.White,
                topLeft = Offset(bodyX - r * 0.04f, shirtTopY - r * 0.06f),
                size = Size(bodyW + r * 0.08f, r * 0.22f),
                cornerRadius = CornerRadius(r * 0.11f, r * 0.11f)
            )
            for (spotX in listOf(-0.6f, -0.35f, 0.35f, 0.6f)) {
                drawCircle(Color(0xFF0F172A), radius = r * 0.030f, center = Offset(cx + r * spotX, shirtTopY + r * 0.05f))
            }
            drawCircle(color = Color(0xFFFACC15), radius = r * 0.15f, center = Offset(cx, shirtTopY + r * 0.09f))
            drawCircle(color = Color(0xFF10B981), radius = r * 0.08f, center = Offset(cx, shirtTopY + r * 0.09f))
        }

        MascotUpperOutfit.AEGIS_KNIGHT_ARMOR -> {
            drawRoundRect(
                brush = Brush.verticalGradient(listOf(Color(0xFFF8FAFC), Color(0xFF94A3B8), Color(0xFF475569))),
                topLeft = Offset(bodyX, shirtTopY),
                size = Size(bodyW, shirtH),
                cornerRadius = CornerRadius(shirtRadius, shirtRadius)
            )
            drawShoulderPauldrons(mainColor = Color(0xFFE2E8F0), trimColor = Color(0xFFF59E0B))
            drawRoundRect(
                color = Color(0xFFF59E0B),
                topLeft = Offset(bodyX + r * 0.05f, shirtTopY + r * 0.03f),
                size = Size(bodyW - r * 0.10f, shirtH - r * 0.06f),
                cornerRadius = CornerRadius(shirtRadius, shirtRadius),
                style = Stroke(width = (r * 0.045f).coerceAtLeast(2.2f))
            )
            drawLine(Color(0xFFFACC15), Offset(cx, shirtTopY + r * 0.04f), Offset(cx, shirtBottomY - r * 0.04f), strokeWidth = 3.5f)
            drawCircle(color = Color(0xFF0EA5E9), radius = r * 0.13f, center = Offset(cx, shirtTopY + shirtH * 0.48f))
            drawCircle(color = Color.White, radius = r * 0.06f, center = Offset(cx, shirtTopY + shirtH * 0.48f))
        }

        MascotUpperOutfit.SAMURAI_KIMONO -> {
            drawRoundRect(
                color = Color(0xFF1E1B4B),
                topLeft = Offset(bodyX, shirtTopY),
                size = Size(bodyW, shirtH),
                cornerRadius = CornerRadius(shirtRadius, shirtRadius)
            )
            val lapelW = (r * 0.07f).coerceAtLeast(2.8f)
            drawLine(Color(0xFFEF4444), Offset(cx - r * 0.40f, shirtTopY), Offset(cx + r * 0.20f, shirtBottomY - r * 0.05f), strokeWidth = lapelW)
            drawLine(Color(0xFFFACC15), Offset(cx + r * 0.40f, shirtTopY), Offset(cx, shirtTopY + shirtH * 0.56f), strokeWidth = lapelW)
            // Gold Haori Coat Shoulder Wings + Obi Belt Knot
            drawShoulderPauldrons(mainColor = Color(0xFF312E81), trimColor = Color(0xFFFACC15))
            drawRoundRect(
                color = Color(0xFFF59E0B),
                topLeft = Offset(bodyX + r * 0.05f, shirtBottomY - r * 0.22f),
                size = Size(bodyW - r * 0.10f, r * 0.14f),
                cornerRadius = CornerRadius(r * 0.06f, r * 0.06f)
            )
            drawCircle(color = Color(0xFFEF4444), radius = r * 0.07f, center = Offset(cx, shirtBottomY - r * 0.15f))
        }

        MascotUpperOutfit.DETECTIVE_TRENCH_VEST -> {
            drawRoundRect(
                color = Color(0xFFB45309),
                topLeft = Offset(bodyX, shirtTopY),
                size = Size(bodyW, shirtH),
                cornerRadius = CornerRadius(shirtRadius, shirtRadius)
            )
            // Tartan Capelet Shoulder Flaps + Silk Tie + Pocket Watch Chain
            drawShoulderPauldrons(mainColor = Color(0xFF92400E), trimColor = Color(0xFFFDE68A))
            drawLine(Color(0xFF451A03), Offset(cx, shirtTopY + r * 0.04f), Offset(cx, shirtBottomY - r * 0.06f), strokeWidth = (r * 0.10f).coerceAtLeast(3.2f), cap = StrokeCap.Round)
            drawArc(
                color = Color(0xFFFACC15),
                startAngle = 10f,
                sweepAngle = 150f,
                useCenter = false,
                topLeft = Offset(cx, shirtTopY + shirtH * 0.30f),
                size = Size(r * 0.44f, r * 0.24f),
                style = Stroke(width = 2.0f)
            )
            drawCircle(color = Color(0xFFFACC15), radius = r * 0.095f, center = Offset(cx + r * 0.42f, shirtTopY + shirtH * 0.45f))
        }

        MascotUpperOutfit.COZY_KNIT_SWEATER -> {
            drawRoundRect(
                color = Color(0xFFDB2777),
                topLeft = Offset(bodyX, shirtTopY),
                size = Size(bodyW, shirtH),
                cornerRadius = CornerRadius(shirtRadius, shirtRadius)
            )
            // Chunky Ribbed Turtleneck Collar
            drawRoundRect(
                color = Color(0xFFF472B6),
                topLeft = Offset(cx - r * 0.52f, shirtTopY - r * 0.08f),
                size = Size(r * 1.04f, r * 0.16f),
                cornerRadius = CornerRadius(r * 0.08f, r * 0.08f)
            )
            for (dx in listOf(-0.44f, 0f, 0.44f)) {
                val px = cx + r * dx
                val py = shirtTopY + shirtH * 0.54f
                val dPath = Path().apply {
                    moveTo(px, py - r * 0.11f)
                    lineTo(px + r * 0.11f, py)
                    lineTo(px, py + r * 0.11f)
                    lineTo(px - r * 0.11f, py)
                    close()
                }
                drawPath(path = dPath, color = Color(0xFFFCE7F3))
            }
        }

        MascotUpperOutfit.ASTRONAUT_SUIT -> {
            drawRoundRect(
                color = Color(0xFFF8FAFC),
                topLeft = Offset(bodyX, shirtTopY),
                size = Size(bodyW, shirtH),
                cornerRadius = CornerRadius(shirtRadius, shirtRadius)
            )
            // Metallic Helmet Locking Ring + Oxygen Chest Control Module
            drawShoulderPauldrons(mainColor = Color(0xFFE2E8F0), trimColor = Color(0xFF0284C7))
            drawLine(Color(0xFF0284C7), Offset(bodyX + r * 0.10f, shirtTopY + r * 0.02f), Offset(bodyX + bodyW - r * 0.10f, shirtTopY + r * 0.02f), strokeWidth = (r * 0.06f).coerceAtLeast(2.6f), cap = StrokeCap.Round)
            drawRoundRect(
                color = Color(0xFF1E293B),
                topLeft = Offset(cx - r * 0.36f, shirtTopY + shirtH * 0.26f),
                size = Size(r * 0.72f, shirtH * 0.56f),
                cornerRadius = CornerRadius(r * 0.08f, r * 0.08f)
            )
            drawCircle(color = Color(0xFFEF4444), radius = r * 0.05f, center = Offset(cx - r * 0.16f, shirtTopY + shirtH * 0.54f))
            drawCircle(color = Color(0xFF10B981), radius = r * 0.05f, center = Offset(cx + r * 0.16f, shirtTopY + shirtH * 0.54f))
        }

        MascotUpperOutfit.ARCANUM_MAGE_ROBE -> {
            drawRoundRect(
                brush = Brush.verticalGradient(listOf(Color(0xFF6D28D9), Color(0xFF3B0764))),
                topLeft = Offset(bodyX, shirtTopY),
                size = Size(bodyW, shirtH),
                cornerRadius = CornerRadius(shirtRadius, shirtRadius)
            )
            drawShoulderPauldrons(mainColor = Color(0xFF4C1D95), trimColor = Color(0xFFFACC15))
            drawLine(Color(0xFFFACC15), Offset(cx, shirtTopY), Offset(cx, shirtBottomY - r * 0.04f), strokeWidth = (r * 0.055f).coerceAtLeast(2.4f))
            drawCircle(color = Color(0xFFFDE047), radius = r * 0.12f, center = Offset(cx, shirtTopY + shirtH * 0.45f))
            drawCircle(color = Color(0xFF38BDF8), radius = r * 0.065f, center = Offset(cx, shirtTopY + shirtH * 0.45f))
        }

        MascotUpperOutfit.SHINOBI_NINJA_VEST -> {
            drawRoundRect(
                color = Color(0xFF15803D),
                topLeft = Offset(bodyX, shirtTopY),
                size = Size(bodyW, shirtH),
                cornerRadius = CornerRadius(shirtRadius, shirtRadius)
            )
            // High Flak Jacket Collar + Scroll Pouches + Kunai Holster
            drawRoundRect(
                color = Color(0xFF14532D),
                topLeft = Offset(cx - r * 0.54f, shirtTopY - r * 0.09f),
                size = Size(r * 1.08f, r * 0.15f),
                cornerRadius = CornerRadius(r * 0.06f, r * 0.06f)
            )
            drawLine(Color(0xFF052E16), Offset(cx, shirtTopY), Offset(cx, shirtBottomY), strokeWidth = (r * 0.055f).coerceAtLeast(2.2f))
            for (side in listOf(-1f, 1f)) {
                drawRoundRect(
                    color = Color(0xFF166534),
                    topLeft = Offset(cx + side * r * 0.38f - r * 0.16f, shirtTopY + shirtH * 0.25f),
                    size = Size(r * 0.32f, shirtH * 0.52f),
                    cornerRadius = CornerRadius(r * 0.06f, r * 0.06f)
                )
            }
        }

        MascotUpperOutfit.RED_CLOUD_CLOAK -> {
            drawRoundRect(
                color = Color(0xFF111827),
                topLeft = Offset(bodyX, shirtTopY),
                size = Size(bodyW, shirtH),
                cornerRadius = CornerRadius(shirtRadius, shirtRadius)
            )
            // High Chin-Guard Akatsuki Collar with Red Lining
            drawRoundRect(
                color = Color(0xFF090D16),
                topLeft = Offset(cx - r * 0.58f, shirtTopY - r * 0.11f),
                size = Size(r * 1.16f, r * 0.18f),
                cornerRadius = CornerRadius(r * 0.06f, r * 0.06f)
            )
            drawRoundRect(
                color = Color(0xFFEF4444),
                topLeft = Offset(cx - r * 0.58f, shirtTopY - r * 0.11f),
                size = Size(r * 1.16f, r * 0.18f),
                cornerRadius = CornerRadius(r * 0.06f, r * 0.06f),
                style = Stroke(width = 2.0f)
            )
            for (dx in listOf(-0.36f, 0.36f)) {
                drawCircle(color = Color(0xFFEF4444), radius = r * 0.11f, center = Offset(cx + r * dx, shirtTopY + shirtH * 0.55f))
                drawCircle(color = Color.White, radius = r * 0.11f, center = Offset(cx + r * dx, shirtTopY + shirtH * 0.55f), style = Stroke(width = 1.8f))
            }
        }

        MascotUpperOutfit.DRAGON_SCALE_ARMOR -> {
            drawRoundRect(
                brush = Brush.verticalGradient(listOf(Color(0xFFDC2626), Color(0xFF7F1D1D))),
                topLeft = Offset(bodyX, shirtTopY),
                size = Size(bodyW, shirtH),
                cornerRadius = CornerRadius(shirtRadius, shirtRadius)
            )
            drawShoulderPauldrons(mainColor = Color(0xFF991B1B), trimColor = Color(0xFFFACC15), spiked = true)
            drawCircle(color = Color(0xFFFACC15), radius = r * 0.16f, center = Offset(cx, shirtTopY + shirtH * 0.50f))
            drawCircle(color = Color(0xFFFEF08A), radius = r * 0.09f, center = Offset(cx, shirtTopY + shirtH * 0.50f))
        }

        MascotUpperOutfit.CELESTIAL_ANGEL_ROBE -> {
            drawRoundRect(
                brush = Brush.verticalGradient(listOf(Color.White, Color(0xFFE0F2FE))),
                topLeft = Offset(bodyX, shirtTopY),
                size = Size(bodyW, shirtH),
                cornerRadius = CornerRadius(shirtRadius, shirtRadius)
            )
            drawShoulderPauldrons(mainColor = Color.White, trimColor = Color(0xFFFACC15))
            drawRoundRect(
                color = Color(0xFFFACC15),
                topLeft = Offset(bodyX + r * 0.05f, shirtTopY + r * 0.03f),
                size = Size(bodyW - r * 0.10f, shirtH - r * 0.06f),
                cornerRadius = CornerRadius(shirtRadius, shirtRadius),
                style = Stroke(width = (r * 0.042f).coerceAtLeast(2f))
            )
            drawCircle(color = Color(0xFF38BDF8), radius = r * 0.11f, center = Offset(cx, shirtTopY + shirtH * 0.45f))
        }

        MascotUpperOutfit.VARSITY_BASEBALL_JACKET -> {
            drawRoundRect(
                color = Color(0xFF2563EB),
                topLeft = Offset(bodyX, shirtTopY),
                size = Size(bodyW, shirtH),
                cornerRadius = CornerRadius(shirtRadius, shirtRadius)
            )
            // White Leather Sleeves / Shoulders + Striped Ribbed Hem
            drawShoulderPauldrons(mainColor = Color.White, trimColor = Color(0xFF2563EB))
            for (bIdx in 0..2) {
                drawCircle(color = Color.White, radius = r * 0.038f, center = Offset(cx, shirtTopY + shirtH * (0.25f + bIdx * 0.25f)))
            }
            drawCircle(color = Color(0xFFFACC15), radius = r * 0.12f, center = Offset(cx - r * 0.36f, shirtTopY + shirtH * 0.48f))
        }

        MascotUpperOutfit.HYPEBEAST_STREET_TEE -> {
            drawRoundRect(
                color = Color(0xFF18181B),
                topLeft = Offset(bodyX, shirtTopY),
                size = Size(bodyW, shirtH),
                cornerRadius = CornerRadius(shirtRadius, shirtRadius)
            )
            // Crossbody Tactical Bag Strap + Neon Buckle
            drawLine(Color(0xFF22C55E), Offset(bodyX + r * 0.18f, shirtTopY), Offset(bodyX + bodyW - r * 0.18f, shirtBottomY), strokeWidth = (r * 0.09f).coerceAtLeast(3.2f), cap = StrokeCap.Round)
            drawRoundRect(
                color = Color(0xFFFACC15),
                topLeft = Offset(cx - r * 0.28f, shirtTopY + shirtH * 0.35f),
                size = Size(r * 0.56f, r * 0.17f),
                cornerRadius = CornerRadius(r * 0.04f, r * 0.04f)
            )
        }

        MascotUpperOutfit.BIKER_LEATHER_JACKET -> {
            drawRoundRect(
                color = Color(0xFF1E293B),
                topLeft = Offset(bodyX, shirtTopY),
                size = Size(bodyW, shirtH),
                cornerRadius = CornerRadius(shirtRadius, shirtRadius)
            )
            drawShoulderPauldrons(mainColor = Color(0xFF0F172A), trimColor = Color(0xFFCBD5E1), spiked = true)
            drawLine(Color(0xFFE2E8F0), Offset(cx - r * 0.26f, shirtTopY), Offset(cx + r * 0.14f, shirtBottomY - r * 0.05f), strokeWidth = (r * 0.05f).coerceAtLeast(2.2f))
            drawCircle(color = Color(0xFFCBD5E1), radius = r * 0.05f, center = Offset(cx - r * 0.42f, shirtTopY + r * 0.12f))
            drawCircle(color = Color(0xFFCBD5E1), radius = r * 0.05f, center = Offset(cx + r * 0.42f, shirtTopY + r * 0.12f))
        }

        MascotUpperOutfit.SCIENTIST_LAB_COAT -> {
            drawRoundRect(
                color = Color.White,
                topLeft = Offset(bodyX, shirtTopY),
                size = Size(bodyW, shirtH),
                cornerRadius = CornerRadius(shirtRadius, shirtRadius)
            )
            drawRoundRect(
                color = Color(0xFF06B6D4),
                topLeft = Offset(cx - r * 0.18f, shirtTopY),
                size = Size(r * 0.36f, shirtH),
                cornerRadius = CornerRadius(r * 0.04f, r * 0.04f)
            )
            // Pocket with Glowing Green & Pink Test Tubes sticking out
            drawLine(Color(0xFF22C55E), Offset(cx + r * 0.34f, shirtTopY + shirtH * 0.20f), Offset(cx + r * 0.34f, shirtTopY + shirtH * 0.40f), strokeWidth = 3.2f, cap = StrokeCap.Round)
            drawLine(Color(0xFFEC4899), Offset(cx + r * 0.44f, shirtTopY + shirtH * 0.22f), Offset(cx + r * 0.44f, shirtTopY + shirtH * 0.40f), strokeWidth = 3.2f, cap = StrokeCap.Round)
            drawRoundRect(
                color = Color(0xFF0284C7),
                topLeft = Offset(cx + r * 0.26f, shirtTopY + shirtH * 0.35f),
                size = Size(r * 0.28f, r * 0.20f),
                cornerRadius = CornerRadius(r * 0.04f, r * 0.04f)
            )
        }

        MascotUpperOutfit.PRO_JERSEY_SPORT -> {
            drawRoundRect(
                brush = Brush.horizontalGradient(listOf(Color(0xFFDC2626), Color(0xFFFACC15), Color(0xFFDC2626))),
                topLeft = Offset(bodyX, shirtTopY),
                size = Size(bodyW, shirtH),
                cornerRadius = CornerRadius(shirtRadius, shirtRadius)
            )
            // Gold Championship Medal on Ribbon
            drawLine(Color(0xFF2563EB), Offset(cx - r * 0.20f, shirtTopY), Offset(cx, shirtTopY + shirtH * 0.46f), strokeWidth = 2.5f)
            drawLine(Color(0xFF2563EB), Offset(cx + r * 0.20f, shirtTopY), Offset(cx, shirtTopY + shirtH * 0.46f), strokeWidth = 2.5f)
            drawCircle(color = Color.White, radius = r * 0.14f, center = Offset(cx, shirtTopY + shirtH * 0.52f))
            drawCircle(color = Color(0xFF991B1B), radius = r * 0.075f, center = Offset(cx, shirtTopY + shirtH * 0.52f))
        }

        MascotUpperOutfit.GUNDAM_MECHA_CHEST -> {
            drawRoundRect(
                color = Color(0xFF2563EB),
                topLeft = Offset(bodyX, shirtTopY),
                size = Size(bodyW, shirtH),
                cornerRadius = CornerRadius(shirtRadius, shirtRadius)
            )
            drawShoulderPauldrons(mainColor = Color(0xFFF8FAFC), trimColor = Color(0xFFEF4444))
            for (side in listOf(-1f, 1f)) {
                drawRoundRect(
                    color = Color(0xFFFACC15),
                    topLeft = Offset(cx + side * r * 0.38f - r * 0.17f, shirtTopY + shirtH * 0.30f),
                    size = Size(r * 0.34f, shirtH * 0.38f),
                    cornerRadius = CornerRadius(r * 0.05f, r * 0.05f)
                )
            }
            drawCircle(color = Color(0xFF22C55E), radius = r * 0.11f, center = Offset(cx, shirtTopY + shirtH * 0.50f))
        }

        MascotUpperOutfit.DINO_ONESIE_PAJAMA -> {
            drawRoundRect(
                color = Color(0xFF4ADE80),
                topLeft = Offset(bodyX, shirtTopY),
                size = Size(bodyW, shirtH),
                cornerRadius = CornerRadius(shirtRadius, shirtRadius)
            )
            // Cute Yellow Dorsal Scales on Sides + Cream Belly Patch
            for (side in listOf(-1f, 1f)) {
                drawCircle(Color(0xFFFACC15), radius = r * 0.08f, center = Offset(cx + side * bodyW * 0.50f, shirtTopY + shirtH * 0.35f))
                drawCircle(Color(0xFFFACC15), radius = r * 0.07f, center = Offset(cx + side * bodyW * 0.50f, shirtTopY + shirtH * 0.70f))
            }
            drawRoundRect(
                color = Color(0xFFFEF9C3),
                topLeft = Offset(cx - r * 0.44f, shirtTopY + shirtH * 0.14f),
                size = Size(r * 0.88f, shirtH * 0.76f),
                cornerRadius = CornerRadius(r * 0.20f, r * 0.20f)
            )
        }

        MascotUpperOutfit.MASTER_CHEF_APRON -> {
            drawRoundRect(
                color = Color.White,
                topLeft = Offset(bodyX, shirtTopY),
                size = Size(bodyW, shirtH),
                cornerRadius = CornerRadius(shirtRadius, shirtRadius)
            )
            // Red Chef Neckerchief Cravat + Double-Breasted Buttons
            val cravat = Path().apply {
                moveTo(cx - r * 0.24f, shirtTopY)
                lineTo(cx + r * 0.24f, shirtTopY)
                lineTo(cx, shirtTopY + r * 0.24f)
                close()
            }
            drawPath(cravat, color = Color(0xFFEF4444))
            for (side in listOf(-0.22f, 0.22f)) {
                drawCircle(color = Color(0xFF1E293B), radius = r * 0.042f, center = Offset(cx + r * side, shirtTopY + shirtH * 0.45f))
                drawCircle(color = Color(0xFF1E293B), radius = r * 0.042f, center = Offset(cx + r * side, shirtTopY + shirtH * 0.72f))
            }
        }

        // --- 12 NEW COMPLEX 3D OUTFITS ---

        MascotUpperOutfit.GUNDAM_STRIKE_EXOSUIT -> {
            // Heavy Wing-Zero Gundam Chest Armor with Twin Pauldron Cannons & GN Reactor
            drawRoundRect(
                brush = Brush.verticalGradient(listOf(Color(0xFF1D4ED8), Color(0xFF1E3A8A))),
                topLeft = Offset(bodyX, shirtTopY),
                size = Size(bodyW, shirtH),
                cornerRadius = CornerRadius(shirtRadius, shirtRadius)
            )
            drawShoulderPauldrons(mainColor = Color(0xFFF8FAFC), trimColor = Color(0xFFFACC15), spiked = true)
            // Red Cockpit Hatch + Glowing Emerald GN Drive Sphere
            drawRoundRect(
                color = Color(0xFFEF4444),
                topLeft = Offset(cx - r * 0.22f, shirtTopY + r * 0.04f),
                size = Size(r * 0.44f, shirtH - r * 0.08f),
                cornerRadius = CornerRadius(r * 0.08f, r * 0.08f)
            )
            drawCircle(color = Color(0xFF10B981), radius = r * 0.14f, center = Offset(cx, shirtTopY + shirtH * 0.48f))
            drawCircle(color = Color(0xFFA7F3D0), radius = r * 0.07f, center = Offset(cx, shirtTopY + shirtH * 0.48f))
        }

        MascotUpperOutfit.ARCHANGEL_SERAPH_ARMOR -> {
            // 24K Gold & Ivory Seraphim Winged Breastplate with Sapphire Cross Core
            drawRoundRect(
                brush = Brush.verticalGradient(listOf(Color(0xFFFEF9C3), Color(0xFFFACC15), Color(0xFFD97706))),
                topLeft = Offset(bodyX, shirtTopY),
                size = Size(bodyW, shirtH),
                cornerRadius = CornerRadius(shirtRadius, shirtRadius)
            )
            drawShoulderPauldrons(mainColor = Color(0xFFFEF08A), trimColor = Color(0xFF38BDF8))
            drawLine(Color.White, Offset(cx - r * 0.36f, shirtTopY + shirtH * 0.50f), Offset(cx + r * 0.36f, shirtTopY + shirtH * 0.50f), strokeWidth = 3.2f)
            drawLine(Color.White, Offset(cx, shirtTopY + r * 0.06f), Offset(cx, shirtBottomY - r * 0.06f), strokeWidth = 3.2f)
            drawCircle(color = Color(0xFF0284C7), radius = r * 0.13f, center = Offset(cx, shirtTopY + shirtH * 0.50f))
        }

        MascotUpperOutfit.DEMON_OVERLORD_PLATE -> {
            // Abyssal Spiked Demon King Armor with Hellfire Rib Core
            drawRoundRect(
                brush = Brush.verticalGradient(listOf(Color(0xFF450A0A), Color(0xFF090D16))),
                topLeft = Offset(bodyX, shirtTopY),
                size = Size(bodyW, shirtH),
                cornerRadius = CornerRadius(shirtRadius, shirtRadius)
            )
            drawShoulderPauldrons(mainColor = Color(0xFF7F1D1D), trimColor = Color(0xFFEF4444), spiked = true)
            for (ribY in listOf(0.32f, 0.64f)) {
                drawLine(Color(0xFFEF4444), Offset(cx - r * 0.44f, shirtTopY + shirtH * ribY), Offset(cx + r * 0.44f, shirtTopY + shirtH * ribY), strokeWidth = 2.6f, cap = StrokeCap.Round)
            }
            drawCircle(color = Color(0xFFF97316), radius = r * 0.13f, center = Offset(cx, shirtTopY + shirtH * 0.48f))
            drawCircle(color = Color(0xFFFEF08A), radius = r * 0.06f, center = Offset(cx, shirtTopY + shirtH * 0.48f))
        }

        MascotUpperOutfit.ROYAL_EMPEROR_HANFU -> {
            // Imperial Crimson & Gold Silk Hanfu with Jade Belt Medallion
            drawRoundRect(
                brush = Brush.verticalGradient(listOf(Color(0xFFB91C1C), Color(0xFF7F1D1D))),
                topLeft = Offset(bodyX, shirtTopY),
                size = Size(bodyW, shirtH),
                cornerRadius = CornerRadius(shirtRadius, shirtRadius)
            )
            // Crossed Gold Brocade Collar + Jade Belt
            drawLine(Color(0xFFFACC15), Offset(cx - r * 0.42f, shirtTopY), Offset(cx + r * 0.18f, shirtBottomY - r * 0.18f), strokeWidth = 3.8f)
            drawLine(Color(0xFFFACC15), Offset(cx + r * 0.42f, shirtTopY), Offset(cx, shirtTopY + shirtH * 0.48f), strokeWidth = 3.8f)
            drawRoundRect(
                color = Color(0xFFFDE047),
                topLeft = Offset(bodyX + r * 0.04f, shirtBottomY - r * 0.20f),
                size = Size(bodyW - r * 0.08f, r * 0.13f),
                cornerRadius = CornerRadius(r * 0.06f, r * 0.06f)
            )
            drawCircle(color = Color(0xFF10B981), radius = r * 0.09f, center = Offset(cx, shirtBottomY - r * 0.135f))
        }

        MascotUpperOutfit.CYBERPUNK_EDGERUNNER_JACKET -> {
            // Neon Yellow Edgerunner Jacket with Glowing Cyan High LED Collar
            drawRoundRect(
                color = Color(0xFFFACC15),
                topLeft = Offset(bodyX, shirtTopY),
                size = Size(bodyW, shirtH),
                cornerRadius = CornerRadius(shirtRadius, shirtRadius)
            )
            drawRoundRect(
                color = Color(0xFF0F172A),
                topLeft = Offset(cx - r * 0.60f, shirtTopY - r * 0.12f),
                size = Size(r * 1.20f, r * 0.20f),
                cornerRadius = CornerRadius(r * 0.08f, r * 0.08f)
            )
            drawRoundRect(
                color = Color(0xFF00F2FE),
                topLeft = Offset(cx - r * 0.60f, shirtTopY - r * 0.12f),
                size = Size(r * 1.20f, r * 0.20f),
                cornerRadius = CornerRadius(r * 0.08f, r * 0.08f),
                style = Stroke(width = 2.4f)
            )
            drawRoundRect(
                color = Color(0xFF0F172A),
                topLeft = Offset(cx - r * 0.22f, shirtTopY + r * 0.08f),
                size = Size(r * 0.44f, shirtH - r * 0.08f)
            )
            drawLine(Color(0xFF00F2FE), Offset(cx, shirtTopY + r * 0.10f), Offset(cx, shirtBottomY - r * 0.04f), strokeWidth = 2.6f)
        }

        MascotUpperOutfit.PALADIN_CRUSADER_MANTLE -> {
            // Royal Blue & Gold Paladin Mantle with Winged Sun Crest
            drawRoundRect(
                brush = Brush.verticalGradient(listOf(Color(0xFF2563EB), Color(0xFF1E3A8A))),
                topLeft = Offset(bodyX, shirtTopY),
                size = Size(bodyW, shirtH),
                cornerRadius = CornerRadius(shirtRadius, shirtRadius)
            )
            drawShoulderPauldrons(mainColor = Color(0xFFFACC15), trimColor = Color.White)
            val sunDiamond = Path().apply {
                moveTo(cx, shirtTopY + r * 0.08f)
                lineTo(cx + r * 0.18f, shirtTopY + shirtH * 0.50f)
                lineTo(cx, shirtBottomY - r * 0.08f)
                lineTo(cx - r * 0.18f, shirtTopY + shirtH * 0.50f)
                close()
            }
            drawPath(sunDiamond, color = Color(0xFFFDE047))
        }

        MascotUpperOutfit.PIRATE_ADMIRAL_COAT -> {
            // Crimson & Obsidian Pirate Admiral Coat with Gold Epaulettes & Sash
            drawRoundRect(
                color = Color(0xFF7F1D1D),
                topLeft = Offset(bodyX, shirtTopY),
                size = Size(bodyW, shirtH),
                cornerRadius = CornerRadius(shirtRadius, shirtRadius)
            )
            drawShoulderPauldrons(mainColor = Color(0xFFFACC15), trimColor = Color(0xFFB45309))
            // Diagonal Gold Baldric Belt + Brass Buckle
            drawLine(Color(0xFFFACC15), Offset(bodyX + r * 0.16f, shirtTopY), Offset(bodyX + bodyW - r * 0.16f, shirtBottomY), strokeWidth = (r * 0.085f).coerceAtLeast(3f))
            drawRoundRect(
                color = Color(0xFFFEF08A),
                topLeft = Offset(cx - r * 0.12f, shirtTopY + shirtH * 0.40f),
                size = Size(r * 0.24f, r * 0.18f),
                style = Stroke(width = 2.4f)
            )
        }

        MascotUpperOutfit.PILOT_CAPTAIN_UNIFORM -> {
            // Navy Airline Captain Suit with Golden Pilot Wings Badge & 4-Stripe Epaulettes
            drawRoundRect(
                color = Color(0xFF0F172A),
                topLeft = Offset(bodyX, shirtTopY),
                size = Size(bodyW, shirtH),
                cornerRadius = CornerRadius(shirtRadius, shirtRadius)
            )
            drawShoulderPauldrons(mainColor = Color(0xFF1E293B), trimColor = Color(0xFFFACC15))
            // White shirt V-neck + black tie + golden pilot wing emblem
            val vNeck = Path().apply {
                moveTo(cx - r * 0.26f, shirtTopY)
                lineTo(cx + r * 0.26f, shirtTopY)
                lineTo(cx, shirtTopY + shirtH * 0.65f)
                close()
            }
            drawPath(vNeck, color = Color.White)
            drawLine(Color(0xFF0F172A), Offset(cx, shirtTopY + r * 0.04f), Offset(cx, shirtTopY + shirtH * 0.62f), strokeWidth = 3.2f)
            drawLine(Color(0xFFFACC15), Offset(cx + r * 0.24f, shirtTopY + shirtH * 0.38f), Offset(cx + r * 0.56f, shirtTopY + shirtH * 0.34f), strokeWidth = 3.0f, cap = StrokeCap.Round)
        }

        MascotUpperOutfit.MEDICAL_SURGEON_COAT -> {
            // Teal Scrubs + White Doctor Coat + Stethoscope Draped Around Neck
            drawRoundRect(
                color = Color.White,
                topLeft = Offset(bodyX, shirtTopY),
                size = Size(bodyW, shirtH),
                cornerRadius = CornerRadius(shirtRadius, shirtRadius)
            )
            drawRoundRect(
                color = Color(0xFF14B8A6),
                topLeft = Offset(cx - r * 0.22f, shirtTopY),
                size = Size(r * 0.44f, shirtH)
            )
            // Stethoscope tubing & chestpiece
            drawArc(
                color = Color(0xFF334155),
                startAngle = 0f,
                sweepAngle = 180f,
                useCenter = false,
                topLeft = Offset(cx - r * 0.42f, shirtTopY - r * 0.06f),
                size = Size(r * 0.84f, shirtH * 0.72f),
                style = Stroke(width = 3.0f, cap = StrokeCap.Round)
            )
            drawCircle(color = Color(0xFFE2E8F0), radius = r * 0.085f, center = Offset(cx + r * 0.36f, shirtTopY + shirtH * 0.55f))
        }

        MascotUpperOutfit.SHOGUN_YOROI_CUIRASS -> {
            // Sengoku Shogun Lamellar Do-Maru Armor with Sode Shoulder Shields & Gold Clan Crest
            drawRoundRect(
                brush = Brush.verticalGradient(listOf(Color(0xFF991B1B), Color(0xFF450A0A))),
                topLeft = Offset(bodyX, shirtTopY),
                size = Size(bodyW, shirtH),
                cornerRadius = CornerRadius(shirtRadius, shirtRadius)
            )
            drawShoulderPauldrons(mainColor = Color(0xFFB91C1C), trimColor = Color(0xFFFACC15))
            for (band in 1..2) {
                val by = shirtTopY + shirtH * (band / 3f)
                drawLine(Color(0xFFFACC15), Offset(bodyX + r * 0.08f, by), Offset(bodyX + bodyW - r * 0.08f, by), strokeWidth = 2.4f)
            }
            drawCircle(color = Color(0xFFFACC15), radius = r * 0.12f, center = Offset(cx, shirtTopY + shirtH * 0.48f), style = Stroke(width = 2.5f))
        }

        MascotUpperOutfit.STREETWEAR_TECH_PARKA -> {
            // Multi-Pocket Cyber-Acronym Techwear Parka with Neon Buckles & Straps
            drawRoundRect(
                color = Color(0xFF18181B),
                topLeft = Offset(bodyX, shirtTopY),
                size = Size(bodyW, shirtH),
                cornerRadius = CornerRadius(shirtRadius, shirtRadius)
            )
            drawShoulderPauldrons(mainColor = Color(0xFF27272A), trimColor = Color(0xFF10B981))
            for (side in listOf(-1f, 1f)) {
                drawRoundRect(
                    color = Color(0xFF3F3F46),
                    topLeft = Offset(cx + side * r * 0.36f - r * 0.18f, shirtTopY + shirtH * 0.26f),
                    size = Size(r * 0.36f, shirtH * 0.54f),
                    cornerRadius = CornerRadius(r * 0.06f, r * 0.06f)
                )
                drawLine(Color(0xFF10B981), Offset(cx + side * r * 0.36f - r * 0.14f, shirtTopY + shirtH * 0.45f), Offset(cx + side * r * 0.36f + r * 0.14f, shirtTopY + shirtH * 0.45f), strokeWidth = 2.6f)
            }
        }

        MascotUpperOutfit.MAGICAL_IDOL_FRILL_DRESS -> {
            // Starlight Idol Costume with Frilled Collar, Ribbon Bow & Star Brooch
            drawRoundRect(
                brush = Brush.verticalGradient(listOf(Color(0xFFF472B6), Color(0xFFDB2777))),
                topLeft = Offset(bodyX, shirtTopY),
                size = Size(bodyW, shirtH),
                cornerRadius = CornerRadius(shirtRadius, shirtRadius)
            )
            // White Frill Scallops along top edge
            for (f in -3..3) {
                drawCircle(Color.White, radius = r * 0.09f, center = Offset(cx + f * r * 0.22f, shirtTopY + r * 0.02f))
            }
            // Big Gold Star Brooch with Ribbon Wings
            val bowY = shirtTopY + shirtH * 0.46f
            val bow = Path().apply {
                moveTo(cx, bowY)
                lineTo(cx - r * 0.32f, bowY - r * 0.14f)
                lineTo(cx - r * 0.32f, bowY + r * 0.14f)
                close()
                moveTo(cx, bowY)
                lineTo(cx + r * 0.32f, bowY - r * 0.14f)
                lineTo(cx + r * 0.32f, bowY + r * 0.14f)
                close()
            }
            drawPath(bow, color = Color(0xFF38BDF8))
            drawCircle(color = Color(0xFFFACC15), radius = r * 0.11f, center = Offset(cx, bowY))
        }

        // ====================================================================
        // 18 NEW ANIME COLLAB, SUPER HERO & FANTASY OUTFITS WITH DISTINCT SHAPES
        // ====================================================================

        MascotUpperOutfit.DEMON_SLAYER_CHECKERED_HAORI -> {
            // DISTINCT SILHOUETTE: Wide Flared Kimono Haori Wing-Sleeves + Ichimatsu Green/Black Checkerboard + Corps Uniform
            // 1. Inner black Gakuran Demon Slayer Corps uniform + white belt
            drawRoundRect(
                color = Color(0xFF18181B),
                topLeft = Offset(bodyX, shirtTopY),
                size = Size(bodyW, shirtH),
                cornerRadius = CornerRadius(shirtRadius, shirtRadius)
            )
            drawLine(Color.White, Offset(cx, shirtTopY), Offset(cx, shirtBottomY), strokeWidth = 2.4f)
            drawRoundRect(
                color = Color.White,
                topLeft = Offset(cx - r * 0.34f, shirtBottomY - r * 0.18f),
                size = Size(r * 0.68f, r * 0.11f),
                cornerRadius = CornerRadius(4f, 4f)
            )
            // 2. Left & Right Wide Flared Haori Panels extending beyond bodyW with Green/Black Checkered Squares
            for (side in listOf(-1f, 1f)) {
                val haoriWing = Path().apply {
                    moveTo(cx + side * r * 0.18f, shirtTopY - r * 0.04f)
                    lineTo(cx + side * (bodyW * 0.50f + r * 0.28f), shirtTopY + r * 0.06f)
                    lineTo(cx + side * (bodyW * 0.50f + r * 0.36f), shirtBottomY + r * 0.06f)
                    lineTo(cx + side * r * 0.22f, shirtBottomY + r * 0.04f)
                    close()
                }
                drawPath(haoriWing, color = Color(0xFF10B981))
                // Draw black checkerboard blocks inside each haori side
                for (row in 0..2) {
                    val ry = shirtTopY + row * (shirtH * 0.33f)
                    val rx = if (side < 0) cx - bodyW * 0.48f + (row % 2) * r * 0.18f else cx + r * 0.24f + (row % 2) * r * 0.18f
                    drawRect(
                        color = Color(0xFF090D16),
                        topLeft = Offset(rx, ry),
                        size = Size(r * 0.22f, shirtH * 0.30f)
                    )
                }
                drawPath(haoriWing, color = Color(0xFF090D16), style = Stroke(width = 2.2f))
            }
        }

        MascotUpperOutfit.FLAME_HASHIRA_CAPE -> {
            // DISTINCT SILHOUETTE: Billowing White Flame-Edge Hashira Mantle with Scalloped Red-Yellow Fire Hem
            drawRoundRect(
                color = Color(0xFF18181B),
                topLeft = Offset(bodyX, shirtTopY),
                size = Size(bodyW, shirtH),
                cornerRadius = CornerRadius(shirtRadius, shirtRadius)
            )
            val flameMantle = Path().apply {
                moveTo(bodyX - r * 0.16f, shirtTopY - r * 0.04f)
                lineTo(bodyX + bodyW + r * 0.16f, shirtTopY - r * 0.04f)
                lineTo(bodyX + bodyW + r * 0.34f, shirtBottomY + r * 0.08f)
                // Jagged flame tips along bottom
                lineTo(cx + r * 0.52f, shirtBottomY - r * 0.12f)
                lineTo(cx + r * 0.24f, shirtBottomY + r * 0.10f)
                lineTo(cx, shirtBottomY - r * 0.14f)
                lineTo(cx - r * 0.24f, shirtBottomY + r * 0.10f)
                lineTo(cx - r * 0.52f, shirtBottomY - r * 0.12f)
                lineTo(bodyX - r * 0.34f, shirtBottomY + r * 0.08f)
                close()
            }
            drawPath(
                flameMantle,
                brush = Brush.verticalGradient(
                    listOf(Color.White, Color(0xFFFEF08A), Color(0xFFF97316), Color(0xFFDC2626))
                )
            )
            drawPath(flameMantle, color = Color(0xFFDC2626), style = Stroke(width = 2.4f))
            drawCircle(color = Color(0xFFFACC15), radius = r * 0.11f, center = Offset(cx, shirtTopY + r * 0.10f))
        }

        MascotUpperOutfit.THUNDER_BREATH_HAORI -> {
            // DISTINCT SILHOUETTE: Wide Winged Yellow-Orange Haori with White Uroko Triangles
            for (side in listOf(-1f, 1f)) {
                val wingSleeve = Path().apply {
                    moveTo(cx + side * r * 0.10f, shirtTopY)
                    lineTo(cx + side * (bodyW * 0.5f + r * 0.34f), shirtTopY + r * 0.08f)
                    lineTo(cx + side * (bodyW * 0.5f + r * 0.24f), shirtBottomY + r * 0.04f)
                    lineTo(cx + side * r * 0.10f, shirtBottomY)
                    close()
                }
                drawPath(
                    wingSleeve,
                    brush = Brush.verticalGradient(listOf(Color(0xFFFACC15), Color(0xFFF97316)))
                )
                drawPath(wingSleeve, color = Color(0xFFB45309), style = Stroke(width = 2.0f))
                // White triangles on sleeve
                val triX = cx + side * (bodyW * 0.38f)
                val triY = shirtTopY + shirtH * 0.50f
                val tri = Path().apply {
                    moveTo(triX, triY - r * 0.12f)
                    lineTo(triX - r * 0.11f, triY + r * 0.09f)
                    lineTo(triX + r * 0.11f, triY + r * 0.09f)
                    close()
                }
                drawPath(tri, color = Color.White)
            }
            // Center dark uniform & white belt
            drawRoundRect(
                color = Color(0xFF27272A),
                topLeft = Offset(cx - r * 0.22f, shirtTopY),
                size = Size(r * 0.44f, shirtH)
            )
        }

        MascotUpperOutfit.AOT_SURVEY_CORPS_CLOAK -> {
            // DISTINCT SILHOUETTE: Asymmetric Draped Green Scout Cloak + Protruding Waist ODM Gas Cylinders & Harness
            // 1. Protruding metallic ODM Gear gas tanks on left & right hips
            for (side in listOf(-1f, 1f)) {
                rotate(degrees = side * -18f, pivot = Offset(cx + side * (bodyW * 0.5f), shirtBottomY - r * 0.15f)) {
                    drawRoundRect(
                        color = Color(0xFF94A3B8),
                        topLeft = Offset(cx + side * (bodyW * 0.46f) - r * 0.16f, shirtBottomY - r * 0.34f),
                        size = Size(r * 0.32f, r * 0.42f),
                        cornerRadius = CornerRadius(r * 0.06f, r * 0.06f)
                    )
                    drawRoundRect(
                        color = Color(0xFF334155),
                        topLeft = Offset(cx + side * (bodyW * 0.46f) - r * 0.16f, shirtBottomY - r * 0.34f),
                        size = Size(r * 0.32f, r * 0.42f),
                        cornerRadius = CornerRadius(r * 0.06f, r * 0.06f),
                        style = Stroke(width = 2.0f)
                    )
                }
            }
            // 2. White shirt + Brown leather cropped jacket + Crossed ODM harness straps
            drawRoundRect(
                color = Color(0xFFF1F5F9),
                topLeft = Offset(bodyX, shirtTopY),
                size = Size(bodyW, shirtH),
                cornerRadius = CornerRadius(shirtRadius, shirtRadius)
            )
            drawLine(Color(0xFF451A03), Offset(bodyX + r * 0.18f, shirtTopY), Offset(bodyX + bodyW - r * 0.18f, shirtBottomY), strokeWidth = 3.0f)
            drawLine(Color(0xFF451A03), Offset(bodyX + bodyW - r * 0.18f, shirtTopY), Offset(bodyX + r * 0.18f, shirtBottomY), strokeWidth = 3.0f)
            // 3. Draped Green Survey Corps Shoulder Capelet with pointed front V-hem & Wings of Freedom crest
            val scoutCloak = Path().apply {
                moveTo(bodyX - r * 0.18f, shirtTopY - r * 0.06f)
                lineTo(bodyX + bodyW + r * 0.18f, shirtTopY - r * 0.06f)
                lineTo(bodyX + bodyW + r * 0.24f, shirtTopY + shirtH * 0.56f)
                lineTo(cx, shirtTopY + shirtH * 0.78f)
                lineTo(bodyX - r * 0.24f, shirtTopY + shirtH * 0.56f)
                close()
            }
            drawPath(scoutCloak, color = Color(0xFF15803D))
            drawPath(scoutCloak, color = Color(0xFF052E16), style = Stroke(width = 2.4f))
            // Wings of Freedom Blue & White Shield Emblem
            drawCircle(color = Color(0xFF1D4ED8), radius = r * 0.12f, center = Offset(cx - r * 0.10f, shirtTopY + shirtH * 0.34f))
            drawCircle(color = Color.White, radius = r * 0.12f, center = Offset(cx + r * 0.10f, shirtTopY + shirtH * 0.34f))
        }

        MascotUpperOutfit.BLEACH_BANKAI_SHIHAKUSHO -> {
            // DISTINCT SILHOUETTE: Black Shihakusho V-Neck + Wide-Shouldered White Captain Haori with Diamond Hem
            drawRoundRect(
                color = Color(0xFF090D16),
                topLeft = Offset(bodyX, shirtTopY),
                size = Size(bodyW, shirtH),
                cornerRadius = CornerRadius(shirtRadius, shirtRadius)
            )
            // White kimono inner collar & crimson Bankai chest strap
            drawLine(Color.White, Offset(cx - r * 0.28f, shirtTopY), Offset(cx, shirtTopY + shirtH * 0.55f), strokeWidth = 3.2f)
            drawLine(Color.White, Offset(cx + r * 0.28f, shirtTopY), Offset(cx, shirtTopY + shirtH * 0.55f), strokeWidth = 3.2f)
            drawLine(Color(0xFFE11D48), Offset(bodyX + r * 0.20f, shirtTopY), Offset(bodyX + bodyW - r * 0.20f, shirtBottomY), strokeWidth = 3.8f)
            // Flared White Captain Haori Outer Coat on Left & Right
            for (side in listOf(-1f, 1f)) {
                val haoriCoat = Path().apply {
                    moveTo(cx + side * r * 0.24f, shirtTopY - r * 0.05f)
                    lineTo(cx + side * (bodyW * 0.5f + r * 0.24f), shirtTopY - r * 0.02f)
                    lineTo(cx + side * (bodyW * 0.5f + r * 0.32f), shirtBottomY + r * 0.06f)
                    lineTo(cx + side * r * 0.28f, shirtBottomY + r * 0.04f)
                    close()
                }
                drawPath(haoriCoat, color = Color.White)
                drawPath(haoriCoat, color = Color(0xFF1E293B), style = Stroke(width = 2.0f))
                // Black Gotei 13 diamond pattern along bottom hem
                val dx = cx + side * (bodyW * 0.42f)
                val dy = shirtBottomY - r * 0.08f
                drawCircle(color = Color(0xFF090D16), radius = r * 0.065f, center = Offset(dx, dy))
            }
        }

        MascotUpperOutfit.NARUTO_HOKAGE_SAGE_CLOAK -> {
            // DISTINCT SILHOUETTE: High-Collared White Hokage Tailcoat with Flaming Red Hem & Six Paths Magatama
            // 1. High popped Hokage collar behind neck
            drawRoundRect(
                color = Color.White,
                topLeft = Offset(cx - r * 0.68f, shirtTopY - r * 0.16f),
                size = Size(r * 1.36f, r * 0.22f),
                cornerRadius = CornerRadius(r * 0.08f, r * 0.08f)
            )
            drawRoundRect(
                color = Color(0xFFEF4444),
                topLeft = Offset(cx - r * 0.68f, shirtTopY - r * 0.16f),
                size = Size(r * 1.36f, r * 0.22f),
                cornerRadius = CornerRadius(r * 0.08f, r * 0.08f),
                style = Stroke(width = 2.2f)
            )
            // 2. Inner Orange/Black Six Paths Jacket
            drawRoundRect(
                brush = Brush.verticalGradient(listOf(Color(0xFFF97316), Color(0xFFEA580C))),
                topLeft = Offset(bodyX, shirtTopY),
                size = Size(bodyW, shirtH),
                cornerRadius = CornerRadius(shirtRadius, shirtRadius)
            )
            // 6 Black Six-Paths Magatama around collar
            for (m in -2..2) {
                drawCircle(Color(0xFF090D16), radius = r * 0.045f, center = Offset(cx + m * r * 0.18f, shirtTopY + r * 0.12f))
            }
            // 3. Flared White Hokage Coat Flaps with Red Flame Triangles
            for (side in listOf(-1f, 1f)) {
                val coatWing = Path().apply {
                    moveTo(cx + side * r * 0.26f, shirtTopY)
                    lineTo(cx + side * (bodyW * 0.5f + r * 0.22f), shirtTopY + r * 0.06f)
                    lineTo(cx + side * (bodyW * 0.5f + r * 0.36f), shirtBottomY + r * 0.06f)
                    lineTo(cx + side * r * 0.24f, shirtBottomY + r * 0.04f)
                    close()
                }
                drawPath(coatWing, color = Color.White)
                drawPath(coatWing, color = Color(0xFFEF4444), style = Stroke(width = 2.2f))
            }
        }

        MascotUpperOutfit.UCHIHA_SUSANOO_ARMOR -> {
            // DISTINCT SILHOUETTE: Wide Layered Crimson Samurai Sode Shoulder Plates + High Uchiha Collar & Fan Crest
            drawRoundRect(
                color = Color(0xFF1E1B4B),
                topLeft = Offset(bodyX, shirtTopY),
                size = Size(bodyW, shirtH),
                cornerRadius = CornerRadius(shirtRadius, shirtRadius)
            )
            // Tiered Crimson Madara Armor Chest & Wide Sode Shoulder Shields
            for (side in listOf(-1f, 1f)) {
                val sodeShield = Path().apply {
                    moveTo(cx + side * (bodyW * 0.36f), shirtTopY - r * 0.08f)
                    lineTo(cx + side * (bodyW * 0.5f + r * 0.38f), shirtTopY + r * 0.04f)
                    lineTo(cx + side * (bodyW * 0.5f + r * 0.30f), shirtBottomY - r * 0.06f)
                    lineTo(cx + side * (bodyW * 0.32f), shirtBottomY - r * 0.12f)
                    close()
                }
                drawPath(sodeShield, color = Color(0xFF991B1B))
                drawPath(sodeShield, color = Color(0xFFFACC15), style = Stroke(width = 2.2f))
            }
            // Center Crimson Cuirass + Red/White Uchiha Fan Emblem
            drawRoundRect(
                color = Color(0xFFB91C1C),
                topLeft = Offset(cx - r * 0.38f, shirtTopY + r * 0.04f),
                size = Size(r * 0.76f, shirtH - r * 0.08f),
                cornerRadius = CornerRadius(r * 0.10f, r * 0.10f)
            )
            drawArc(Color(0xFFEF4444), 180f, 180f, true, Offset(cx - r * 0.16f, shirtTopY + shirtH * 0.25f), Size(r * 0.32f, r * 0.32f))
            drawArc(Color.White, 0f, 180f, true, Offset(cx - r * 0.16f, shirtTopY + shirtH * 0.25f), Size(r * 0.32f, r * 0.32f))
        }

        MascotUpperOutfit.LINK_CLICK_TIME_JACKET -> {
            // DISTINCT SILHOUETTE: Split Black & White Contrast Hoodie Jacket (Cheng Xiaoshi & Lu Guang) + Hanging Vintage Camera
            // Left Half: Crisp White Hoodie with Blue Trim | Right Half: Obsidian Black Jacket with Gold Trim
            val leftHalf = Path().apply {
                moveTo(cx, shirtTopY)
                lineTo(bodyX - r * 0.16f, shirtTopY + r * 0.04f)
                lineTo(bodyX - r * 0.22f, shirtBottomY)
                lineTo(cx, shirtBottomY)
                close()
            }
            val rightHalf = Path().apply {
                moveTo(cx, shirtTopY)
                lineTo(bodyX + bodyW + r * 0.16f, shirtTopY + r * 0.04f)
                lineTo(bodyX + bodyW + r * 0.22f, shirtBottomY)
                lineTo(cx, shirtBottomY)
                close()
            }
            drawPath(leftHalf, color = Color(0xFFF8FAFC))
            drawPath(leftHalf, color = Color(0xFF0284C7), style = Stroke(width = 2.4f))
            drawPath(rightHalf, color = Color(0xFF0F172A))
            drawPath(rightHalf, color = Color(0xFFFACC15), style = Stroke(width = 2.4f))
            // Vintage Time-Photo Camera hanging on chest strap
            drawLine(Color(0xFF38BDF8), Offset(cx - r * 0.26f, shirtTopY), Offset(cx, shirtTopY + shirtH * 0.52f), strokeWidth = 2.4f)
            drawLine(Color(0xFFFACC15), Offset(cx + r * 0.26f, shirtTopY), Offset(cx, shirtTopY + shirtH * 0.52f), strokeWidth = 2.4f)
            drawRoundRect(
                color = Color(0xFF1E293B),
                topLeft = Offset(cx - r * 0.22f, shirtTopY + shirtH * 0.44f),
                size = Size(r * 0.44f, r * 0.30f),
                cornerRadius = CornerRadius(4f, 4f)
            )
            drawCircle(color = Color(0xFF38BDF8), radius = r * 0.10f, center = Offset(cx, shirtTopY + shirtH * 0.59f))
            drawCircle(color = Color(0xFFFEF08A), radius = r * 0.045f, center = Offset(cx, shirtTopY + shirtH * 0.59f))
        }

        MascotUpperOutfit.SOUL_LAND_SEA_GOD_ARMOR -> {
            // DISTINCT SILHOUETTE: Winged Golden-Azure Sea God Poseidon Armor with Fin Pauldrons & Sapphire Core
            val armorShape = Path().apply {
                moveTo(bodyX - r * 0.28f, shirtTopY - r * 0.08f)
                lineTo(bodyX + bodyW + r * 0.28f, shirtTopY - r * 0.08f)
                lineTo(bodyX + bodyW + r * 0.14f, shirtBottomY + r * 0.06f)
                lineTo(cx, shirtBottomY + r * 0.16f)
                lineTo(bodyX - r * 0.14f, shirtBottomY + r * 0.06f)
                close()
            }
            drawPath(
                armorShape,
                brush = Brush.verticalGradient(listOf(Color(0xFFFEF08A), Color(0xFFF59E0B), Color(0xFF0284C7)))
            )
            drawPath(armorShape, color = Color(0xFF38BDF8), style = Stroke(width = 2.6f))
            drawShoulderPauldrons(mainColor = Color(0xFFFACC15), trimColor = Color(0xFF0EA5E9), spiked = true)
            // Sea God Trident Crest in Center
            drawCircle(color = Color(0xFF0284C7), radius = r * 0.16f, center = Offset(cx, shirtTopY + shirtH * 0.48f))
            drawCircle(color = Color(0xFFFEF08A), radius = r * 0.08f, center = Offset(cx, shirtTopY + shirtH * 0.48f))
        }

        MascotUpperOutfit.BTTH_FLAME_EMPEROR_ROBE -> {
            // DISTINCT SILHOUETTE: Xiao Yan Xianxia Cultivation Robe with Floating Celestial Ribbons (Piaodai) & Lotus Core
            // Floating Celestial Silk Ribbons looping outside both shoulders
            for (side in listOf(-1f, 1f)) {
                val ribbon = Path().apply {
                    moveTo(cx + side * (bodyW * 0.42f), shirtTopY)
                    cubicTo(
                        cx + side * (bodyW * 0.5f + r * 0.52f), shirtTopY - r * 0.36f,
                        cx + side * (bodyW * 0.5f + r * 0.48f), shirtBottomY,
                        cx + side * (bodyW * 0.42f), shirtBottomY + r * 0.08f
                    )
                }
                drawPath(ribbon, color = Color(0xFFA855F7), style = Stroke(width = (r * 0.08f).coerceAtLeast(3.2f), cap = StrokeCap.Round))
                drawPath(ribbon, color = Color(0xFFFEF08A), style = Stroke(width = 1.5f, cap = StrokeCap.Round))
            }
            drawRoundRect(
                brush = Brush.verticalGradient(listOf(Color(0xFF18181B), Color(0xFF3B0764))),
                topLeft = Offset(bodyX, shirtTopY),
                size = Size(bodyW, shirtH),
                cornerRadius = CornerRadius(shirtRadius, shirtRadius)
            )
            // Crossed Gold & Jade Flame Lapels + Lotus Waist Seal
            drawLine(Color(0xFFFACC15), Offset(cx - r * 0.42f, shirtTopY), Offset(cx + r * 0.22f, shirtBottomY), strokeWidth = 3.6f)
            drawLine(Color(0xFF10B981), Offset(cx + r * 0.42f, shirtTopY), Offset(cx, shirtTopY + shirtH * 0.52f), strokeWidth = 3.6f)
            drawCircle(color = Color(0xFF10B981), radius = r * 0.13f, center = Offset(cx, shirtBottomY - r * 0.14f))
            drawCircle(color = Color(0xFFFEF08A), radius = r * 0.06f, center = Offset(cx, shirtBottomY - r * 0.14f))
        }

        MascotUpperOutfit.HERO_NANO_IRON_ARMOR -> {
            // DISTINCT SILHOUETTE: V-Tapered Angular Nano-Tech Superhero Chestplate + Glowing Triangular Arc Reactor
            val ironPlate = Path().apply {
                moveTo(bodyX - r * 0.24f, shirtTopY - r * 0.06f)
                lineTo(bodyX + bodyW + r * 0.24f, shirtTopY - r * 0.06f)
                lineTo(bodyX + bodyW + r * 0.06f, shirtBottomY + r * 0.04f)
                lineTo(cx, shirtBottomY + r * 0.14f)
                lineTo(bodyX - r * 0.06f, shirtBottomY + r * 0.04f)
                close()
            }
            drawPath(ironPlate, brush = Brush.verticalGradient(listOf(Color(0xFFEF4444), Color(0xFF991B1B))))
            drawPath(ironPlate, color = Color(0xFFFACC15), style = Stroke(width = 2.8f))
            drawShoulderPauldrons(mainColor = Color(0xFFDC2626), trimColor = Color(0xFFFACC15))
            // Glowing Cyan Triangular Arc Reactor in Center Chest
            val reactor = Path().apply {
                moveTo(cx - r * 0.18f, shirtTopY + shirtH * 0.30f)
                lineTo(cx + r * 0.18f, shirtTopY + shirtH * 0.30f)
                lineTo(cx, shirtTopY + shirtH * 0.68f)
                close()
            }
            drawPath(reactor, color = Color(0xFF00F2FE))
            drawPath(reactor, color = Color.White, style = Stroke(width = 2.0f))
        }

        MascotUpperOutfit.HERO_SPIDER_SYMBIOTE_SUIT -> {
            // DISTINCT SILHOUETTE: Crimson-Blue Iron Spider Suit with Giant Golden Spider Crest Spanning Chest
            drawRoundRect(
                brush = Brush.verticalGradient(listOf(Color(0xFFDC2626), Color(0xFF1D4ED8))),
                topLeft = Offset(bodyX, shirtTopY),
                size = Size(bodyW, shirtH),
                cornerRadius = CornerRadius(shirtRadius, shirtRadius)
            )
            // Golden Iron-Spider Emblem with Long Legs Reaching Edges
            drawCircle(color = Color(0xFFFACC15), radius = r * 0.11f, center = Offset(cx, shirtTopY + shirtH * 0.48f))
            for (side in listOf(-1f, 1f)) {
                drawLine(Color(0xFFFACC15), Offset(cx, shirtTopY + shirtH * 0.48f), Offset(cx + side * r * 0.48f, shirtTopY + r * 0.06f), strokeWidth = 2.6f)
                drawLine(Color(0xFFFACC15), Offset(cx, shirtTopY + shirtH * 0.48f), Offset(cx + side * r * 0.52f, shirtTopY + shirtH * 0.48f), strokeWidth = 2.6f)
                drawLine(Color(0xFFFACC15), Offset(cx, shirtTopY + shirtH * 0.48f), Offset(cx + side * r * 0.46f, shirtBottomY - r * 0.04f), strokeWidth = 2.6f)
            }
        }

        MascotUpperOutfit.HERO_DARK_KNIGHT_CAPE -> {
            // DISTINCT SILHOUETTE: Tactical Carbon-Armored Vigilante Plate + Bat Emblem + Golden Utility Belt Capsules
            drawRoundRect(
                color = Color(0xFF1E293B),
                topLeft = Offset(bodyX, shirtTopY),
                size = Size(bodyW, shirtH),
                cornerRadius = CornerRadius(shirtRadius, shirtRadius)
            )
            drawShoulderPauldrons(mainColor = Color(0xFF0F172A), trimColor = Color(0xFF475569), spiked = true)
            // Bat Wing Chest Crest
            val batCrest = Path().apply {
                moveTo(cx, shirtTopY + shirtH * 0.26f)
                lineTo(cx + r * 0.42f, shirtTopY + shirtH * 0.18f)
                lineTo(cx + r * 0.26f, shirtTopY + shirtH * 0.48f)
                lineTo(cx, shirtTopY + shirtH * 0.56f)
                lineTo(cx - r * 0.26f, shirtTopY + shirtH * 0.48f)
                lineTo(cx - r * 0.42f, shirtTopY + shirtH * 0.18f)
                close()
            }
            drawPath(batCrest, color = Color(0xFF090D16))
            drawPath(batCrest, color = Color(0xFFFACC15), style = Stroke(width = 1.8f))
            // Golden Tactical Utility Belt with 5 Pouches
            for (p in -2..2) {
                drawRoundRect(
                    color = Color(0xFFFACC15),
                    topLeft = Offset(cx + p * r * 0.20f - r * 0.07f, shirtBottomY - r * 0.18f),
                    size = Size(r * 0.14f, r * 0.12f),
                    cornerRadius = CornerRadius(3f, 3f)
                )
            }
        }

        MascotUpperOutfit.HERO_THUNDER_ASGARD_PLATE -> {
            // DISTINCT SILHOUETTE: Asgardian Thunder God Scale Cuirass + 6 Glowing Lightning Discs + Winged Pauldrons
            drawRoundRect(
                brush = Brush.verticalGradient(listOf(Color(0xFF1E293B), Color(0xFF0F172A))),
                topLeft = Offset(bodyX, shirtTopY),
                size = Size(bodyW, shirtH),
                cornerRadius = CornerRadius(shirtRadius, shirtRadius)
            )
            drawShoulderPauldrons(mainColor = Color(0xFFE2E8F0), trimColor = Color(0xFF38BDF8), spiked = true)
            for (row in 0..1) {
                for (side in listOf(-1f, 1f)) {
                    val dx = cx + side * r * (0.28f - row * 0.05f)
                    val dy = shirtTopY + shirtH * (0.34f + row * 0.34f)
                    drawCircle(color = Color(0xFFE2E8F0), radius = r * 0.11f, center = Offset(dx, dy))
                    drawCircle(color = Color(0xFF00F2FE), radius = r * 0.06f, center = Offset(dx, dy))
                }
            }
        }

        MascotUpperOutfit.FANTASY_ELVEN_RANGER_CLOAK -> {
            // DISTINCT SILHOUETTE: Layered Emerald Leaf Mantle + Golden Filigree Trim + Leather Quiver Strap
            val leafMantle = Path().apply {
                moveTo(bodyX - r * 0.22f, shirtTopY)
                quadraticTo(cx, shirtTopY - r * 0.10f, bodyX + bodyW + r * 0.22f, shirtTopY)
                lineTo(bodyX + bodyW + r * 0.18f, shirtBottomY)
                quadraticTo(cx, shirtBottomY + r * 0.14f, bodyX - r * 0.18f, shirtBottomY)
                close()
            }
            drawPath(leafMantle, brush = Brush.verticalGradient(listOf(Color(0xFF059669), Color(0xFF064E3B))))
            drawPath(leafMantle, color = Color(0xFFFACC15), style = Stroke(width = 2.4f))
            drawLine(Color(0xFF78350F), Offset(bodyX + r * 0.16f, shirtTopY), Offset(bodyX + bodyW - r * 0.16f, shirtBottomY), strokeWidth = 3.5f)
            drawCircle(color = Color(0xFF4ADE80), radius = r * 0.12f, center = Offset(cx, shirtTopY + r * 0.10f))
            drawCircle(color = Color(0xFFFEF08A), radius = r * 0.12f, center = Offset(cx, shirtTopY + r * 0.10f), style = Stroke(width = 2.0f))
        }

        MascotUpperOutfit.FANTASY_NECROMANCER_LICH_ROBE -> {
            // DISTINCT SILHOUETTE: Bone-Spiked Necromancer Mantle with Glowing Violet Soul-Cage Ribs
            drawRoundRect(
                color = Color(0xFF1E1B4B),
                topLeft = Offset(bodyX, shirtTopY),
                size = Size(bodyW, shirtH),
                cornerRadius = CornerRadius(shirtRadius, shirtRadius)
            )
            drawShoulderPauldrons(mainColor = Color(0xFFE2E8F0), trimColor = Color(0xFFA855F7), spiked = true)
            for (rib in 0..1) {
                val ry = shirtTopY + shirtH * (0.35f + rib * 0.30f)
                drawLine(Color(0xFFE2E8F0), Offset(cx - r * 0.38f, ry), Offset(cx + r * 0.38f, ry), strokeWidth = 3.0f, cap = StrokeCap.Round)
            }
            drawCircle(color = Color(0xFFA855F7), radius = r * 0.14f, center = Offset(cx, shirtTopY + shirtH * 0.50f))
            drawCircle(color = Color.White, radius = r * 0.06f, center = Offset(cx, shirtTopY + shirtH * 0.50f))
        }

        MascotUpperOutfit.FANTASY_VALKYRIE_DIVINE_DRESS -> {
            // DISTINCT SILHOUETTE: Winged Silver-Gold Valkyrie Breastplate with Pleated Sky-Blue Battle Tassets
            drawRoundRect(
                brush = Brush.verticalGradient(listOf(Color(0xFFF8FAFC), Color(0xFF38BDF8), Color(0xFF0284C7))),
                topLeft = Offset(bodyX, shirtTopY),
                size = Size(bodyW, shirtH),
                cornerRadius = CornerRadius(shirtRadius, shirtRadius)
            )
            drawShoulderPauldrons(mainColor = Color.White, trimColor = Color(0xFFFACC15), spiked = true)
            // Golden Winged Chest Crest
            val wingCrest = Path().apply {
                moveTo(cx, shirtTopY + shirtH * 0.52f)
                lineTo(cx - r * 0.44f, shirtTopY + r * 0.08f)
                lineTo(cx, shirtTopY + shirtH * 0.28f)
                lineTo(cx + r * 0.44f, shirtTopY + r * 0.08f)
                close()
            }
            drawPath(wingCrest, color = Color(0xFFFACC15))
        }

        MascotUpperOutfit.FANTASY_DRAGONSLAYER_BERSERKER -> {
            // DISTINCT SILHOUETTE: Giant Curved White Dragon-Fang Shoulder Horns + Magma Scale Harness
            drawRoundRect(
                brush = Brush.verticalGradient(listOf(Color(0xFF7F1D1D), Color(0xFF18181B))),
                topLeft = Offset(bodyX, shirtTopY),
                size = Size(bodyW, shirtH),
                cornerRadius = CornerRadius(shirtRadius, shirtRadius)
            )
            // Giant White Dragon Fangs curving upward from both shoulders
            for (side in listOf(-1f, 1f)) {
                val fang = Path().apply {
                    moveTo(cx + side * (bodyW * 0.36f), shirtTopY + r * 0.12f)
                    lineTo(cx + side * (bodyW * 0.5f + r * 0.38f), shirtTopY - r * 0.28f)
                    lineTo(cx + side * (bodyW * 0.5f + r * 0.16f), shirtTopY + r * 0.22f)
                    close()
                }
                drawPath(fang, color = Color(0xFFFEF9C3))
                drawPath(fang, color = Color(0xFFD97706), style = Stroke(width = 2.0f))
            }
            drawCircle(color = Color(0xFFF97316), radius = r * 0.15f, center = Offset(cx, shirtBottomY - r * 0.16f))
            drawCircle(color = Color(0xFFFEF08A), radius = r * 0.07f, center = Offset(cx, shirtBottomY - r * 0.16f))
        }
    }
}

// ============================================================================
// 4. FLOATING HANDS, SAPUTANGAN & WEAPONS RENDERER (34 UNIQUE ITEMS — 0 DUPLICATES!)
// ============================================================================

internal fun DrawScope.drawMascotCustomGlovedHands(
    leftHandCenter: Offset,
    rightHandCenter: Offset,
    r: Float,
    defaultHandColor: Color,
    defaultHandLipColor: Color,
    gloves: MascotHandGloves
) {
    val hands = listOf(leftHandCenter, rightHandCenter)
    when (gloves) {
        MascotHandGloves.DEFAULT_HANDS -> {
            hands.forEach { hc ->
                drawCircle(color = defaultHandLipColor, radius = r * 0.17f, center = Offset(hc.x, hc.y + r * 0.04f))
                drawCircle(color = defaultHandColor, radius = r * 0.16f, center = hc)
            }
        }

        MascotHandGloves.CYBER_GAUNTLETS -> {
            // Hexagonal Cyber Gauntlet with Cyan Thruster Core & Knuckle Guards
            hands.forEach { hc ->
                drawCircle(color = Color(0xFF0F172A), radius = r * 0.21f, center = hc)
                drawCircle(color = Color(0xFF06B6D4), radius = r * 0.20f, center = hc, style = Stroke(width = (r * 0.045f).coerceAtLeast(2f)))
                drawCircle(color = Color(0xFF00F2FE), radius = r * 0.095f, center = hc)
                drawCircle(color = Color.White, radius = r * 0.04f, center = hc)
            }
        }

        MascotHandGloves.DJ_NEON_EQUALIZER -> {
            // DJ Turntable Gloves with 3 Vertical RGB Audio Spectrum Bars on each hand
            hands.forEach { hc ->
                drawRoundRect(
                    color = Color(0xFF1E1B4B),
                    topLeft = Offset(hc.x - r * 0.20f, hc.y - r * 0.20f),
                    size = Size(r * 0.40f, r * 0.40f),
                    cornerRadius = CornerRadius(r * 0.10f, r * 0.10f)
                )
                val barCols = listOf(Color(0xFFEC4899), Color(0xFFFACC15), Color(0xFF00F2FE))
                barCols.forEachIndexed { bIdx, col ->
                    val bx = hc.x + (bIdx - 1) * r * 0.09f
                    val bh = if (bIdx == 1) r * 0.24f else r * 0.15f
                    drawLine(col, Offset(bx, hc.y + bh * 0.5f), Offset(bx, hc.y - bh * 0.5f), strokeWidth = 3.2f, cap = StrokeCap.Round)
                }
            }
        }

        MascotHandGloves.BOXING_CHAMPS -> {
            // Heavyweight Padded Red Boxing Gloves with Golden Championship Wrist Belt
            hands.forEach { hc ->
                drawCircle(color = Color(0xFF991B1B), radius = r * 0.23f, center = Offset(hc.x, hc.y + r * 0.04f))
                drawCircle(color = Color(0xFFEF4444), radius = r * 0.22f, center = hc)
                drawCircle(color = Color.White.copy(alpha = 0.65f), radius = r * 0.07f, center = Offset(hc.x - r * 0.06f, hc.y - r * 0.06f))
                drawRoundRect(
                    color = Color(0xFFFACC15),
                    topLeft = Offset(hc.x - r * 0.13f, hc.y + r * 0.10f),
                    size = Size(r * 0.26f, r * 0.09f),
                    cornerRadius = CornerRadius(r * 0.03f, r * 0.03f)
                )
            }
        }

        MascotHandGloves.CHEF_OVEN_MITTS -> {
            // Quilted Checkered Bakery Oven Mitts with Distinct Thumb Lobe
            hands.forEachIndexed { idx, hc ->
                val dir = if (idx == 0) 1f else -1f
                drawCircle(color = Color(0xFFEA580C), radius = r * 0.11f, center = Offset(hc.x + dir * r * 0.15f, hc.y - r * 0.04f))
                drawRoundRect(
                    color = Color(0xFFF97316),
                    topLeft = Offset(hc.x - r * 0.17f, hc.y - r * 0.20f),
                    size = Size(r * 0.34f, r * 0.40f),
                    cornerRadius = CornerRadius(r * 0.16f, r * 0.16f)
                )
                drawLine(Color(0xFFFEF08A), Offset(hc.x - r * 0.11f, hc.y), Offset(hc.x + r * 0.11f, hc.y), strokeWidth = 1.8f)
                drawLine(Color(0xFFFEF08A), Offset(hc.x, hc.y - r * 0.12f), Offset(hc.x, hc.y + r * 0.12f), strokeWidth = 1.8f)
            }
        }

        MascotHandGloves.COSMIC_MAGIC_GLOVES -> {
            // Arcane Runic Hands with Orbiting Violet Hexagram Seal
            hands.forEach { hc ->
                drawCircle(color = Color(0xFFA855F7).copy(alpha = 0.48f), radius = r * 0.28f, center = hc)
                drawCircle(color = Color(0xFF6B21A8), radius = r * 0.18f, center = hc)
                drawCircle(color = Color(0xFFFDE047), radius = r * 0.22f, center = hc, style = Stroke(width = 1.8f))
                drawCircle(color = Color.White, radius = r * 0.075f, center = hc)
            }
        }

        MascotHandGloves.BLUE_FLAME_HANDS -> {
            // Blazing Azure Necromancer Flame Tongues Rising from Palms
            hands.forEach { hc ->
                val flame = Path().apply {
                    moveTo(hc.x - r * 0.20f, hc.y + r * 0.06f)
                    quadraticTo(hc.x - r * 0.22f, hc.y - r * 0.28f, hc.x, hc.y - r * 0.42f)
                    quadraticTo(hc.x + r * 0.22f, hc.y - r * 0.28f, hc.x + r * 0.20f, hc.y + r * 0.06f)
                    close()
                }
                drawPath(flame, brush = Brush.verticalGradient(listOf(Color(0xFFE0F2FE), Color(0xFF38BDF8), Color(0xFF0284C7))))
                drawCircle(color = Color(0xFF0284C7), radius = r * 0.17f, center = hc)
                drawCircle(color = Color(0xFFE0F2FE), radius = r * 0.08f, center = hc)
            }
        }

        MascotHandGloves.THUNDER_LIGHTNING_FISTS -> {
            // Zeus Thunder Fists with Jagged High-Voltage Lightning Arcs
            hands.forEach { hc ->
                drawCircle(color = Color(0xFFCA8A04), radius = r * 0.19f, center = hc)
                drawCircle(color = Color(0xFFFEF08A), radius = r * 0.14f, center = hc)
                val bolt = Path().apply {
                    moveTo(hc.x - r * 0.26f, hc.y - r * 0.18f)
                    lineTo(hc.x + r * 0.04f, hc.y - r * 0.04f)
                    lineTo(hc.x - r * 0.06f, hc.y + r * 0.06f)
                    lineTo(hc.x + r * 0.26f, hc.y + r * 0.20f)
                }
                drawPath(bolt, color = Color(0xFF38BDF8), style = Stroke(width = 2.6f, cap = StrokeCap.Round))
            }
        }

        MascotHandGloves.WHITE_BUTLER_GLOVES -> {
            // Crisp Royal Butler Silk Gloves with Gold Cuff Button & 3 Seam Lines
            hands.forEach { hc ->
                drawCircle(color = Color(0xFF94A3B8), radius = r * 0.19f, center = Offset(hc.x, hc.y + r * 0.03f))
                drawCircle(color = Color.White, radius = r * 0.18f, center = hc)
                for (dx in listOf(-0.055f, 0f, 0.055f)) {
                    drawLine(Color(0xFF475569), Offset(hc.x + r * dx, hc.y - r * 0.07f), Offset(hc.x + r * dx, hc.y + r * 0.05f), strokeWidth = 1.8f, cap = StrokeCap.Round)
                }
                drawCircle(color = Color(0xFFFACC15), radius = r * 0.04f, center = Offset(hc.x, hc.y + r * 0.12f))
            }
        }

        MascotHandGloves.GHOST_SKELETON_HANDS -> {
            // Spooky X-Ray Bone Carpals & 4 Articulated Finger Bones
            hands.forEach { hc ->
                drawCircle(color = Color(0xFF090D16), radius = r * 0.20f, center = hc)
                drawCircle(color = Color(0xFFF1F5F9), radius = r * 0.10f, center = Offset(hc.x, hc.y + r * 0.04f))
                for (fIdx in -1..1) {
                    val fx = hc.x + fIdx * r * 0.08f
                    drawLine(Color.White, Offset(fx, hc.y + r * 0.02f), Offset(fx + fIdx * r * 0.04f, hc.y - r * 0.22f), strokeWidth = 2.8f, cap = StrokeCap.Round)
                }
            }
        }

        MascotHandGloves.DRAGON_FLAME_CLAWS -> {
            // Crimson Scaled Dragon Gauntlets with 3 Curved Golden Talons
            hands.forEachIndexed { idx, hc ->
                val dir = if (idx == 0) -1f else 1f
                drawCircle(color = Color(0xFFDC2626), radius = r * 0.19f, center = hc)
                drawCircle(color = Color(0xFFFACC15), radius = r * 0.19f, center = hc, style = Stroke(width = 2f))
                for (cIdx in -1..1) {
                    val cxTip = hc.x + dir * r * 0.30f
                    val cyTip = hc.y + cIdx * r * 0.10f
                    drawLine(Color(0xFFFACC15), Offset(hc.x + dir * r * 0.10f, hc.y + cIdx * r * 0.06f), Offset(cxTip, cyTip), strokeWidth = (r * 0.05f).coerceAtLeast(2.2f), cap = StrokeCap.Round)
                }
            }
        }

        MascotHandGloves.SHINOBI_NINJA_BRACERS -> {
            // Armored Shinobi Tekko Bracers Holding 4-Point Steel Shuriken
            hands.forEach { hc ->
                drawCircle(color = Color(0xFF1E293B), radius = r * 0.18f, center = hc)
                val starR = r * 0.26f
                val shuriken = Path().apply {
                    moveTo(hc.x, hc.y - starR)
                    lineTo(hc.x + starR * 0.28f, hc.y - starR * 0.28f)
                    lineTo(hc.x + starR, hc.y)
                    lineTo(hc.x + starR * 0.28f, hc.y + starR * 0.28f)
                    lineTo(hc.x, hc.y + starR)
                    lineTo(hc.x - starR * 0.28f, hc.y + starR * 0.28f)
                    lineTo(hc.x - starR, hc.y)
                    lineTo(hc.x - starR * 0.28f, hc.y - starR * 0.28f)
                    close()
                }
                drawPath(shuriken, color = Color(0xFFE2E8F0))
                drawCircle(color = Color(0xFF0F172A), radius = r * 0.055f, center = hc)
            }
        }

        MascotHandGloves.MECHA_ROBOT_HANDS -> {
            // Blocky Gundam Manipulator Hands with Articulated Hydraulic Fingers
            hands.forEach { hc ->
                drawRoundRect(
                    color = Color(0xFF334155),
                    topLeft = Offset(hc.x - r * 0.20f, hc.y - r * 0.20f),
                    size = Size(r * 0.40f, r * 0.40f),
                    cornerRadius = CornerRadius(r * 0.06f, r * 0.06f)
                )
                drawRoundRect(
                    color = Color(0xFFF8FAFC),
                    topLeft = Offset(hc.x - r * 0.14f, hc.y - r * 0.14f),
                    size = Size(r * 0.28f, r * 0.20f),
                    cornerRadius = CornerRadius(r * 0.04f, r * 0.04f)
                )
                drawCircle(color = Color(0xFF00F2FE), radius = r * 0.06f, center = hc)
            }
        }

        MascotHandGloves.SILVER_PALADIN_GAUNTLETS -> {
            // Winged Silver Crusader Gauntlets with Golden Cross Crest
            hands.forEach { hc ->
                drawCircle(color = Color(0xFFCBD5E1), radius = r * 0.20f, center = hc)
                drawCircle(color = Color(0xFFFACC15), radius = r * 0.20f, center = hc, style = Stroke(width = 2.4f))
                drawLine(Color(0xFFF59E0B), Offset(hc.x - r * 0.12f, hc.y), Offset(hc.x + r * 0.12f, hc.y), strokeWidth = 2.8f)
                drawLine(Color(0xFFF59E0B), Offset(hc.x, hc.y - r * 0.12f), Offset(hc.x, hc.y + r * 0.12f), strokeWidth = 2.8f)
            }
        }

        MascotHandGloves.FROST_CRYSTAL_GAUNTLETS -> {
            // Sharp Sub-Zero Ice Spike Gauntlets
            hands.forEach { hc ->
                val ice = Path().apply {
                    moveTo(hc.x, hc.y - r * 0.28f)
                    lineTo(hc.x + r * 0.22f, hc.y)
                    lineTo(hc.x, hc.y + r * 0.26f)
                    lineTo(hc.x - r * 0.22f, hc.y)
                    close()
                }
                drawPath(ice, brush = Brush.verticalGradient(listOf(Color.White, Color(0xFF7DD3FC), Color(0xFF0284C7))))
                drawPath(ice, color = Color.White, style = Stroke(width = 1.8f))
            }
        }

        MascotHandGloves.SULTAN_GOLD_GAUNTLETS -> {
            // Solid 24K Gold Bullion Gauntlets with Giant Center Emerald Cut Gem
            hands.forEach { hc ->
                drawRoundRect(
                    brush = Brush.verticalGradient(listOf(Color(0xFFFEF08A), Color(0xFFFACC15), Color(0xFFB45309))),
                    topLeft = Offset(hc.x - r * 0.20f, hc.y - r * 0.20f),
                    size = Size(r * 0.40f, r * 0.40f),
                    cornerRadius = CornerRadius(r * 0.12f, r * 0.12f)
                )
                drawRoundRect(
                    color = Color(0xFF10B981),
                    topLeft = Offset(hc.x - r * 0.09f, hc.y - r * 0.09f),
                    size = Size(r * 0.18f, r * 0.18f),
                    cornerRadius = CornerRadius(r * 0.04f, r * 0.04f)
                )
            }
        }

        MascotHandGloves.PHARAOH_GOLD_BANDS -> {
            // Ancient Egyptian Golden Bracers with Lapis Lazuli Stripes & Ruby Scarab
            hands.forEach { hc ->
                drawCircle(color = defaultHandColor, radius = r * 0.16f, center = hc)
                drawRoundRect(
                    color = Color(0xFFF59E0B),
                    topLeft = Offset(hc.x - r * 0.20f, hc.y - r * 0.08f),
                    size = Size(r * 0.40f, r * 0.18f),
                    cornerRadius = CornerRadius(r * 0.06f, r * 0.06f)
                )
                drawLine(Color(0xFF1D4ED8), Offset(hc.x - r * 0.18f, hc.y), Offset(hc.x + r * 0.18f, hc.y), strokeWidth = 3.0f)
                drawCircle(color = Color(0xFFEF4444), radius = r * 0.055f, center = hc)
            }
        }

        MascotHandGloves.INFINITY_GEM_RINGS -> {
            // Cosmic Titan Gauntlet with 6 Glowing Infinity Stones
            hands.forEach { hc ->
                drawCircle(color = Color(0xFFB45309), radius = r * 0.21f, center = Offset(hc.x, hc.y + r * 0.03f))
                drawCircle(color = Color(0xFFFACC15), radius = r * 0.20f, center = hc)
                val gemColors = listOf(Color(0xFFEF4444), Color(0xFF3B82F6), Color(0xFF22C55E), Color(0xFFA855F7), Color(0xFFF97316))
                gemColors.forEachIndexed { gIdx, gCol ->
                    val gx = hc.x + (gIdx - 2) * (r * 0.055f)
                    val gy = hc.y - r * 0.05f + abs(gIdx - 2) * (r * 0.02f)
                    drawCircle(color = gCol, radius = r * 0.038f, center = Offset(gx, gy))
                }
                drawCircle(color = Color(0xFFFEF08A), radius = r * 0.055f, center = Offset(hc.x, hc.y + r * 0.06f))
            }
        }

        MascotHandGloves.COZY_WINTER_MITTENS -> {
            // Knitted Winter Snow Mittens with Fluffy White Fur Cuff
            hands.forEach { hc ->
                drawCircle(color = Color(0xFFEC4899), radius = r * 0.19f, center = hc)
                drawRoundRect(
                    color = Color.White,
                    topLeft = Offset(hc.x - r * 0.20f, hc.y + r * 0.06f),
                    size = Size(r * 0.40f, r * 0.12f),
                    cornerRadius = CornerRadius(r * 0.06f, r * 0.06f)
                )
            }
        }

        MascotHandGloves.NEKO_PAW_GLOVES -> {
            // Fluffy Cat Paw Gloves with Pink Toe Beans
            hands.forEach { hc ->
                drawCircle(color = Color(0xFFDB2777), radius = r * 0.20f, center = Offset(hc.x, hc.y + r * 0.03f))
                drawCircle(color = Color(0xFFF472B6), radius = r * 0.19f, center = hc)
                drawCircle(color = Color.White, radius = r * 0.08f, center = Offset(hc.x, hc.y + r * 0.03f))
                for (dx in listOf(-0.075f, 0f, 0.075f)) {
                    drawCircle(color = Color.White, radius = r * 0.035f, center = Offset(hc.x + r * dx, hc.y - r * 0.075f))
                }
            }
        }

        MascotHandGloves.PLASMA_WRIST_SHIELDS -> {
            // Projected Emerald Energy Buckler Shields on Both Wrists
            hands.forEach { hc ->
                drawCircle(color = defaultHandColor, radius = r * 0.15f, center = hc)
                drawCircle(color = Color(0xFF10B981).copy(alpha = 0.35f), radius = r * 0.28f, center = hc)
                drawCircle(color = Color(0xFF34D399), radius = r * 0.28f, center = hc, style = Stroke(width = 2.6f))
            }
        }

        MascotHandGloves.RACING_PRO_GLOVES -> {
            // Formula-1 Carbon-Grip Racing Gloves with Checkered Wrist Strap
            hands.forEach { hc ->
                drawCircle(color = Color(0xFF1E293B), radius = r * 0.19f, center = hc)
                drawArc(
                    color = Color(0xFFEF4444),
                    startAngle = 180f,
                    sweepAngle = 180f,
                    useCenter = true,
                    topLeft = Offset(hc.x - r * 0.19f, hc.y - r * 0.19f),
                    size = Size(r * 0.38f, r * 0.38f)
                )
                drawCircle(color = Color.White, radius = r * 0.06f, center = hc)
            }
        }

        // --- 12 NEW SAPUTANGAN, WEAPONS, SHIELDS, FANS & CLAWS ---

        MascotHandGloves.ROYAL_SILK_HANDKERCHIEF -> {
            // Saputangan Sutra Sultan: White gloved hands + flowing crimson-gold silk handkerchief draped from right hand
            hands.forEach { hc ->
                drawCircle(color = Color.White, radius = r * 0.17f, center = hc)
                drawCircle(color = Color(0xFFFACC15), radius = r * 0.17f, center = hc, style = Stroke(width = 2f))
            }
            val rh = rightHandCenter
            val hankyPath = Path().apply {
                moveTo(rh.x, rh.y + r * 0.06f)
                quadraticTo(rh.x - r * 0.26f, rh.y + r * 0.38f, rh.x - r * 0.16f, rh.y + r * 0.66f)
                lineTo(rh.x + r * 0.04f, rh.y + r * 0.54f)
                lineTo(rh.x + r * 0.22f, rh.y + r * 0.68f)
                quadraticTo(rh.x + r * 0.28f, rh.y + r * 0.36f, rh.x, rh.y + r * 0.06f)
                close()
            }
            drawPath(hankyPath, brush = Brush.verticalGradient(listOf(Color(0xFFEF4444), Color(0xFFB91C1C))))
            drawPath(hankyPath, color = Color(0xFFFACC15), style = Stroke(width = 2.2f))
        }

        MascotHandGloves.NOBLE_LACE_HANDKERCHIEF -> {
            // Saputangan Renda Bangsawan: Lavender silk gloves + frilled pastel lace handkerchief with rose embroidery
            hands.forEach { hc ->
                drawCircle(color = Color(0xFFFCE7F3), radius = r * 0.17f, center = hc)
            }
            val lh = leftHandCenter
            val lacePath = Path().apply {
                moveTo(lh.x, lh.y + r * 0.05f)
                lineTo(lh.x - r * 0.25f, lh.y + r * 0.56f)
                quadraticTo(lh.x, lh.y + r * 0.72f, lh.x + r * 0.25f, lh.y + r * 0.56f)
                close()
            }
            drawPath(lacePath, color = Color.White)
            drawPath(lacePath, color = Color(0xFFF472B6), style = Stroke(width = 2.2f))
            drawCircle(color = Color(0xFFE11D48), radius = r * 0.06f, center = Offset(lh.x, lh.y + r * 0.42f))
        }

        MascotHandGloves.DUAL_PLASMA_SABERS -> {
            // Twin Glowing Cyan & Magenta Laser Energy Blades Held in Both Hands
            hands.forEachIndexed { idx, hc ->
                val bladeCol = if (idx == 0) Color(0xFF00F2FE) else Color(0xFFEC4899)
                val tilt = if (idx == 0) -22f else 22f
                rotate(degrees = tilt, pivot = hc) {
                    drawLine(bladeCol.copy(alpha = 0.45f), Offset(hc.x, hc.y), Offset(hc.x, hc.y - r * 0.96f), strokeWidth = (r * 0.14f).coerceAtLeast(5f), cap = StrokeCap.Round)
                    drawLine(Color.White, Offset(hc.x, hc.y), Offset(hc.x, hc.y - r * 0.94f), strokeWidth = (r * 0.06f).coerceAtLeast(2.5f), cap = StrokeCap.Round)
                    drawRoundRect(
                        color = Color(0xFF334155),
                        topLeft = Offset(hc.x - r * 0.07f, hc.y - r * 0.10f),
                        size = Size(r * 0.14f, r * 0.28f),
                        cornerRadius = CornerRadius(4f, 4f)
                    )
                }
                drawCircle(color = Color(0xFF1E293B), radius = r * 0.16f, center = hc)
            }
        }

        MascotHandGloves.PALADIN_SWORD_AND_SHIELD -> {
            // Right Hand: Golden Excalibur Sword | Left Hand: Royal Heuristic Knight Shield
            val rh = rightHandCenter
            drawLine(Color(0xFFE2E8F0), Offset(rh.x, rh.y), Offset(rh.x + r * 0.12f, rh.y - r * 0.92f), strokeWidth = (r * 0.09f).coerceAtLeast(3.5f), cap = StrokeCap.Round)
            drawLine(Color(0xFFFACC15), Offset(rh.x - r * 0.20f, rh.y - r * 0.08f), Offset(rh.x + r * 0.24f, rh.y - r * 0.04f), strokeWidth = 3.5f, cap = StrokeCap.Round)
            drawCircle(color = Color(0xFFFACC15), radius = r * 0.16f, center = rh)

            val lh = leftHandCenter
            val shield = Path().apply {
                moveTo(lh.x - r * 0.28f, lh.y - r * 0.26f)
                lineTo(lh.x + r * 0.28f, lh.y - r * 0.26f)
                lineTo(lh.x + r * 0.24f, lh.y + r * 0.10f)
                lineTo(lh.x, lh.y + r * 0.34f)
                lineTo(lh.x - r * 0.24f, lh.y + r * 0.10f)
                close()
            }
            drawPath(shield, brush = Brush.verticalGradient(listOf(Color(0xFF2563EB), Color(0xFF1E3A8A))))
            drawPath(shield, color = Color(0xFFFACC15), style = Stroke(width = 2.6f))
            drawLine(Color(0xFFFACC15), Offset(lh.x, lh.y - r * 0.18f), Offset(lh.x, lh.y + r * 0.22f), strokeWidth = 2.6f)
            drawLine(Color(0xFFFACC15), Offset(lh.x - r * 0.16f, lh.y - r * 0.02f), Offset(lh.x + r * 0.16f, lh.y - r * 0.02f), strokeWidth = 2.6f)
        }

        MascotHandGloves.ELEMENTAL_FIRE_ICE_BLADES -> {
            // Left Hand: Blazing Magma Dagger | Right Hand: Sub-Zero Frost Crystal Blade
            val lh = leftHandCenter
            val fireBlade = Path().apply {
                moveTo(lh.x - r * 0.10f, lh.y)
                lineTo(lh.x - r * 0.18f, lh.y - r * 0.82f)
                lineTo(lh.x + r * 0.12f, lh.y)
                close()
            }
            drawPath(fireBlade, brush = Brush.verticalGradient(listOf(Color(0xFFFEF08A), Color(0xFFF97316), Color(0xFFDC2626))))
            drawCircle(color = Color(0xFFDC2626), radius = r * 0.16f, center = lh)

            val rh = rightHandCenter
            val iceBlade = Path().apply {
                moveTo(rh.x - r * 0.12f, rh.y)
                lineTo(rh.x + r * 0.18f, rh.y - r * 0.82f)
                lineTo(rh.x + r * 0.10f, rh.y)
                close()
            }
            drawPath(iceBlade, brush = Brush.verticalGradient(listOf(Color.White, Color(0xFF38BDF8), Color(0xFF0284C7))))
            drawCircle(color = Color(0xFF0284C7), radius = r * 0.16f, center = rh)
        }

        MascotHandGloves.ARCANUM_CRYSTAL_SCEPTER -> {
            // Right Hand: Winged Arcane Crystal Staff | Left Hand: Levitating Spell Orb
            val rh = rightHandCenter
            drawLine(Color(0xFFD97706), Offset(rh.x, rh.y + r * 0.38f), Offset(rh.x, rh.y - r * 0.72f), strokeWidth = 3.5f, cap = StrokeCap.Round)
            drawCircle(color = Color(0xFFA855F7), radius = r * 0.16f, center = Offset(rh.x, rh.y - r * 0.78f))
            drawCircle(color = Color(0xFFFEF08A), radius = r * 0.16f, center = Offset(rh.x, rh.y - r * 0.78f), style = Stroke(width = 2f))
            drawCircle(color = defaultHandColor, radius = r * 0.15f, center = rh)

            val lh = leftHandCenter
            drawCircle(color = defaultHandColor, radius = r * 0.15f, center = lh)
            drawCircle(color = Color(0xFF00F2FE).copy(alpha = 0.65f), radius = r * 0.20f, center = Offset(lh.x, lh.y - r * 0.30f))
            drawCircle(color = Color.White, radius = r * 0.09f, center = Offset(lh.x, lh.y - r * 0.30f))
        }

        MascotHandGloves.MECHA_BUSTER_CANNONS -> {
            // Twin Heavy Arm-Mounted Plasma Buster Cannons with Glowing Muzzle Charge
            hands.forEach { hc ->
                drawRoundRect(
                    color = Color(0xFF1E3A8A),
                    topLeft = Offset(hc.x - r * 0.24f, hc.y - r * 0.22f),
                    size = Size(r * 0.48f, r * 0.44f),
                    cornerRadius = CornerRadius(r * 0.10f, r * 0.10f)
                )
                drawCircle(color = Color(0xFF0F172A), radius = r * 0.15f, center = hc)
                drawCircle(color = Color(0xFF00F2FE), radius = r * 0.10f, center = hc)
                drawCircle(color = Color.White, radius = r * 0.045f, center = hc)
            }
        }

        MascotHandGloves.KITSUNE_FOLDING_FAN -> {
            // Kipas Lipat Kitsune Emas & Sakura held gracefully in Right Hand
            hands.forEach { hc ->
                drawCircle(color = defaultHandColor, radius = r * 0.15f, center = hc)
            }
            val rh = rightHandCenter
            drawArc(
                brush = Brush.verticalGradient(listOf(Color(0xFFF43F5E), Color(0xFFFACC15))),
                startAngle = 200f,
                sweepAngle = 140f,
                useCenter = true,
                topLeft = Offset(rh.x - r * 0.42f, rh.y - r * 0.56f),
                size = Size(r * 0.84f, r * 0.84f)
            )
            drawArc(
                color = Color(0xFFFEF08A),
                startAngle = 200f,
                sweepAngle = 140f,
                useCenter = true,
                topLeft = Offset(rh.x - r * 0.42f, rh.y - r * 0.56f),
                size = Size(r * 0.84f, r * 0.84f),
                style = Stroke(width = 2.0f)
            )
        }

        MascotHandGloves.SPIRIT_SHRINE_LANTERN -> {
            // Glowing Paper Shrine Lantern Hanging from Bamboo Pole in Right Hand
            hands.forEach { hc ->
                drawCircle(color = defaultHandColor, radius = r * 0.15f, center = hc)
            }
            val rh = rightHandCenter
            drawLine(Color(0xFF78350F), Offset(rh.x, rh.y), Offset(rh.x + r * 0.24f, rh.y - r * 0.34f), strokeWidth = 2.8f)
            val lx = rh.x + r * 0.24f
            val ly = rh.y + r * 0.14f
            drawLine(Color(0xFFFACC15), Offset(lx, rh.y - r * 0.34f), Offset(lx, ly - r * 0.18f), strokeWidth = 1.8f)
            drawOval(
                brush = Brush.radialGradient(listOf(Color(0xFFFEF08A), Color(0xFFF97316), Color(0xFFDC2626)), center = Offset(lx, ly), radius = r * 0.24f),
                topLeft = Offset(lx - r * 0.16f, ly - r * 0.20f),
                size = Size(r * 0.32f, r * 0.40f)
            )
        }

        MascotHandGloves.CYBER_EXOSKELETON_CLAWS -> {
            // 3 Articulated Neon-Green Cybernetic Mantis Talons per Hand
            hands.forEachIndexed { idx, hc ->
                val dir = if (idx == 0) -1f else 1f
                drawRoundRect(
                    color = Color(0xFF0F172A),
                    topLeft = Offset(hc.x - r * 0.18f, hc.y - r * 0.18f),
                    size = Size(r * 0.36f, r * 0.36f),
                    cornerRadius = CornerRadius(r * 0.08f, r * 0.08f)
                )
                for (c in -1..1) {
                    val blade = Path().apply {
                        moveTo(hc.x + dir * r * 0.12f, hc.y + c * r * 0.08f)
                        lineTo(hc.x + dir * r * 0.42f, hc.y + c * r * 0.14f - r * 0.08f)
                        lineTo(hc.x + dir * r * 0.18f, hc.y + c * r * 0.08f + r * 0.05f)
                        close()
                    }
                    drawPath(blade, color = Color(0xFF10B981))
                }
            }
        }

        MascotHandGloves.SHINOBI_KUNAI_CHAIN -> {
            // Twin Ninja Kunai Daggers Connected by a Mystic Chain
            hands.forEachIndexed { idx, hc ->
                val dir = if (idx == 0) -1f else 1f
                drawCircle(color = Color(0xFF1E293B), radius = r * 0.16f, center = hc)
                val kunai = Path().apply {
                    moveTo(hc.x, hc.y - r * 0.12f)
                    lineTo(hc.x + dir * r * 0.14f, hc.y - r * 0.56f)
                    lineTo(hc.x - dir * r * 0.10f, hc.y - r * 0.12f)
                    close()
                }
                drawPath(kunai, color = Color(0xFFE2E8F0))
                drawCircle(color = Color(0xFFEF4444), radius = r * 0.06f, center = Offset(hc.x, hc.y + r * 0.18f), style = Stroke(width = 2.2f))
            }
        }

        MascotHandGloves.GOLDEN_CHEF_SPATULA_PAN -> {
            // Right Hand: 24K Golden Spatula | Left Hand: Sizzling Frying Pan with Sunny-Side Egg
            val rh = rightHandCenter
            drawLine(Color(0xFFFACC15), Offset(rh.x, rh.y), Offset(rh.x, rh.y - r * 0.56f), strokeWidth = 3.2f)
            drawRoundRect(
                color = Color(0xFFFEF08A),
                topLeft = Offset(rh.x - r * 0.14f, rh.y - r * 0.78f),
                size = Size(r * 0.28f, r * 0.24f),
                cornerRadius = CornerRadius(4f, 4f)
            )
            drawCircle(color = defaultHandColor, radius = r * 0.15f, center = rh)

            val lh = leftHandCenter
            drawCircle(color = Color(0xFF1E293B), radius = r * 0.24f, center = Offset(lh.x - r * 0.12f, lh.y - r * 0.10f))
            drawCircle(color = Color.White, radius = r * 0.12f, center = Offset(lh.x - r * 0.12f, lh.y - r * 0.10f))
            drawCircle(color = Color(0xFFF59E0B), radius = r * 0.06f, center = Offset(lh.x - r * 0.12f, lh.y - r * 0.10f))
        }

        // ====================================================================
        // 12 NEW ANIME COLLAB, SUPER HERO & FANTASY WEAPONS / HANDS
        // ====================================================================

        MascotHandGloves.NICHIRIN_SUN_KATANA -> {
            // Demon Slayer Black-Crimson Sun Breathing Nichirin Katana with Flaming Tsuba & Fire Swirl
            val rh = rightHandCenter
            rotate(degrees = 18f, pivot = rh) {
                drawLine(Color(0xFF090D16), Offset(rh.x, rh.y), Offset(rh.x, rh.y - r * 1.05f), strokeWidth = (r * 0.09f).coerceAtLeast(3.5f), cap = StrokeCap.Round)
                drawLine(Color(0xFFEF4444), Offset(rh.x + r * 0.02f, rh.y), Offset(rh.x + r * 0.02f, rh.y - r * 1.02f), strokeWidth = 2.0f)
                // Flame Wheel Arc around Blade
                drawArc(
                    brush = Brush.sweepGradient(listOf(Color(0xFFFEF08A), Color(0xFFF97316), Color(0xFFDC2626), Color.Transparent)),
                    startAngle = 200f,
                    sweepAngle = 200f,
                    useCenter = false,
                    topLeft = Offset(rh.x - r * 0.42f, rh.y - r * 0.92f),
                    size = Size(r * 0.84f, r * 0.84f),
                    style = Stroke(width = (r * 0.10f).coerceAtLeast(3.8f), cap = StrokeCap.Round)
                )
                drawOval(Color(0xFFFACC15), Offset(rh.x - r * 0.16f, rh.y - r * 0.06f), Size(r * 0.32f, r * 0.10f))
            }
            drawCircle(color = defaultHandColor, radius = r * 0.15f, center = rh)
            drawCircle(color = defaultHandColor, radius = r * 0.15f, center = leftHandCenter)
        }

        MascotHandGloves.AOT_ODM_DUAL_BLADES -> {
            // Attack on Titan Dual Ultrahard Steel Snap-Blades with Trigger Handles & Wire Grapple Lines
            hands.forEachIndexed { idx, hc ->
                val dir = if (idx == 0) -1f else 1f
                rotate(degrees = dir * 16f, pivot = hc) {
                    val blade = Path().apply {
                        moveTo(hc.x - r * 0.05f, hc.y)
                        lineTo(hc.x - r * 0.06f, hc.y - r * 0.98f)
                        lineTo(hc.x + r * 0.06f, hc.y - r * 0.88f)
                        lineTo(hc.x + r * 0.05f, hc.y)
                        close()
                    }
                    drawPath(blade, color = Color(0xFFE2E8F0))
                    // Segment snap-marks along ODM blade
                    for (seg in 1..3) {
                        val sy = hc.y - r * (seg * 0.22f)
                        drawLine(Color(0xFF475569), Offset(hc.x - r * 0.06f, sy), Offset(hc.x + r * 0.05f, sy - r * 0.04f), strokeWidth = 1.6f)
                    }
                    // Trigger Grip Guard
                    drawRoundRect(
                        color = Color(0xFF1E293B),
                        topLeft = Offset(hc.x - r * 0.12f, hc.y - r * 0.08f),
                        size = Size(r * 0.24f, r * 0.18f),
                        cornerRadius = CornerRadius(3f, 3f)
                    )
                }
                drawCircle(color = defaultHandColor, radius = r * 0.15f, center = hc)
            }
        }

        MascotHandGloves.BLEACH_ZANPAKUTO_BLADE -> {
            // Bleach Oversized Zangetsu Cleaver Sword + Black-Red Reiatsu & Wrapping Cloth
            val rh = rightHandCenter
            rotate(degrees = 14f, pivot = rh) {
                val cleaver = Path().apply {
                    moveTo(rh.x - r * 0.06f, rh.y)
                    lineTo(rh.x - r * 0.10f, rh.y - r * 1.12f)
                    lineTo(rh.x + r * 0.22f, rh.y - r * 0.92f)
                    lineTo(rh.x + r * 0.12f, rh.y)
                    close()
                }
                drawPath(cleaver, brush = Brush.horizontalGradient(listOf(Color(0xFF090D16), Color(0xFFE2E8F0))))
                drawPath(cleaver, color = Color(0xFFE11D48), style = Stroke(width = 2.0f))
                // Flowing White Hilt Cloth Bandage
                val cloth = Path().apply {
                    moveTo(rh.x, rh.y + r * 0.12f)
                    quadraticTo(rh.x + r * 0.30f, rh.y + r * 0.35f, rh.x + r * 0.15f, rh.y + r * 0.56f)
                }
                drawPath(cloth, color = Color.White, style = Stroke(width = 3.2f, cap = StrokeCap.Round))
            }
            drawCircle(color = defaultHandColor, radius = r * 0.15f, center = rh)
            drawCircle(color = Color(0xFFE11D48), radius = r * 0.18f, center = leftHandCenter)
        }

        MascotHandGloves.NARUTO_RASENGAN_SHURIKEN -> {
            // Right Hand: Spiraling Cyan Rasengan Sphere | Left Hand: 4-Bladed Wind Rasenshuriken
            val rh = rightHandCenter
            drawCircle(color = defaultHandColor, radius = r * 0.15f, center = rh)
            val rasenganC = Offset(rh.x, rh.y - r * 0.28f)
            drawCircle(color = Color(0xFF00F2FE).copy(alpha = 0.55f), radius = r * 0.30f, center = rasenganC)
            drawCircle(color = Color(0xFF38BDF8), radius = r * 0.20f, center = rasenganC)
            drawCircle(color = Color.White, radius = r * 0.10f, center = rasenganC)

            val lh = leftHandCenter
            drawCircle(color = defaultHandColor, radius = r * 0.15f, center = lh)
            val shurikenC = Offset(lh.x, lh.y - r * 0.28f)
            for (b in 0..3) {
                rotate(degrees = b * 90f, pivot = shurikenC) {
                    val wing = Path().apply {
                        moveTo(shurikenC.x, shurikenC.y - r * 0.08f)
                        quadraticTo(shurikenC.x + r * 0.22f, shurikenC.y - r * 0.26f, shurikenC.x, shurikenC.y - r * 0.42f)
                        quadraticTo(shurikenC.x - r * 0.10f, shurikenC.y - r * 0.22f, shurikenC.x, shurikenC.y - r * 0.08f)
                        close()
                    }
                    drawPath(wing, color = Color(0xFFE0F2FE))
                    drawPath(wing, color = Color(0xFF0284C7), style = Stroke(width = 1.6f))
                }
            }
            drawCircle(color = Color(0xFFFEF08A), radius = r * 0.11f, center = shurikenC)
        }

        MascotHandGloves.LINK_CLICK_CAMERA_CLAP -> {
            // Right Hand: Glowing Time-Dive Camera | Left Hand: Golden Pocket Watch & Blue Time Ripple
            val rh = rightHandCenter
            drawCircle(color = defaultHandColor, radius = r * 0.15f, center = rh)
            drawRoundRect(
                color = Color(0xFF0F172A),
                topLeft = Offset(rh.x - r * 0.22f, rh.y - r * 0.34f),
                size = Size(r * 0.44f, r * 0.30f),
                cornerRadius = CornerRadius(4f, 4f)
            )
            drawCircle(color = Color(0xFF00F2FE), radius = r * 0.10f, center = Offset(rh.x, rh.y - r * 0.19f))
            drawCircle(color = Color.White, radius = r * 0.045f, center = Offset(rh.x, rh.y - r * 0.19f))

            val lh = leftHandCenter
            drawCircle(color = defaultHandColor, radius = r * 0.15f, center = lh)
            drawCircle(color = Color(0xFFFACC15), radius = r * 0.22f, center = Offset(lh.x, lh.y - r * 0.24f))
            drawCircle(color = Color(0xFF0F172A), radius = r * 0.17f, center = Offset(lh.x, lh.y - r * 0.24f))
            drawLine(Color(0xFF00F2FE), Offset(lh.x, lh.y - r * 0.24f), Offset(lh.x, lh.y - r * 0.36f), strokeWidth = 2.2f)
            drawLine(Color(0xFFFACC15), Offset(lh.x, lh.y - r * 0.24f), Offset(lh.x + r * 0.10f, lh.y - r * 0.24f), strokeWidth = 2.2f)
        }

        MascotHandGloves.SOUL_LAND_HAMMER_TRIDENT -> {
            // Right Hand: Golden Sea God Trident (Poseidon) | Left Hand: Black-Red Clear Sky Hammer (Haotian)
            val rh = rightHandCenter
            drawLine(Color(0xFFFACC15), Offset(rh.x, rh.y + r * 0.30f), Offset(rh.x, rh.y - r * 0.96f), strokeWidth = 3.6f, cap = StrokeCap.Round)
            val tridentProngs = Path().apply {
                moveTo(rh.x - r * 0.22f, rh.y - r * 0.86f)
                lineTo(rh.x - r * 0.18f, rh.y - r * 0.62f)
                lineTo(rh.x + r * 0.18f, rh.y - r * 0.62f)
                lineTo(rh.x + r * 0.22f, rh.y - r * 0.86f)
            }
            drawPath(tridentProngs, color = Color(0xFFFACC15), style = Stroke(width = 3.2f, cap = StrokeCap.Round))
            drawCircle(color = Color(0xFF00F2FE), radius = r * 0.08f, center = Offset(rh.x, rh.y - r * 0.72f))
            drawCircle(color = defaultHandColor, radius = r * 0.15f, center = rh)

            val lh = leftHandCenter
            drawLine(Color(0xFF334155), Offset(lh.x, lh.y + r * 0.20f), Offset(lh.x, lh.y - r * 0.56f), strokeWidth = 3.6f)
            drawRoundRect(
                color = Color(0xFF090D16),
                topLeft = Offset(lh.x - r * 0.28f, lh.y - r * 0.86f),
                size = Size(r * 0.56f, r * 0.36f),
                cornerRadius = CornerRadius(6f, 6f)
            )
            drawRoundRect(
                color = Color(0xFFEF4444),
                topLeft = Offset(lh.x - r * 0.28f, lh.y - r * 0.86f),
                size = Size(r * 0.56f, r * 0.36f),
                cornerRadius = CornerRadius(6f, 6f),
                style = Stroke(width = 2.2f)
            )
            drawCircle(color = defaultHandColor, radius = r * 0.15f, center = lh)
        }

        MascotHandGloves.BTTH_FLAME_LOTUS_RULER -> {
            // Left Hand: 3-Color Buddha's Angry Fire Lotus | Right Hand: Black-Magma Heavy Xuan Ruler
            val lh = leftHandCenter
            drawCircle(color = defaultHandColor, radius = r * 0.15f, center = lh)
            val lotusCenter = Offset(lh.x, lh.y - r * 0.28f)
            for (p in 0..5) {
                rotate(degrees = p * 60f, pivot = lotusCenter) {
                    drawOval(
                        color = if (p % 2 == 0) Color(0xFF10B981) else Color(0xFFA855F7),
                        topLeft = Offset(lotusCenter.x - r * 0.09f, lotusCenter.y - r * 0.32f),
                        size = Size(r * 0.18f, r * 0.28f)
                    )
                }
            }
            drawCircle(color = Color(0xFFFEF08A), radius = r * 0.10f, center = lotusCenter)

            val rh = rightHandCenter
            rotate(degrees = 16f, pivot = rh) {
                drawRoundRect(
                    color = Color(0xFF18181B),
                    topLeft = Offset(rh.x - r * 0.14f, rh.y - r * 0.96f),
                    size = Size(r * 0.28f, r * 0.98f),
                    cornerRadius = CornerRadius(4f, 4f)
                )
                drawRoundRect(
                    color = Color(0xFFF97316),
                    topLeft = Offset(rh.x - r * 0.14f, rh.y - r * 0.96f),
                    size = Size(r * 0.28f, r * 0.98f),
                    cornerRadius = CornerRadius(4f, 4f),
                    style = Stroke(width = 2.2f)
                )
            }
            drawCircle(color = defaultHandColor, radius = r * 0.15f, center = rh)
        }

        MascotHandGloves.HERO_REPULSOR_GAUNTLETS -> {
            // Red-Gold Iron Nano Gauntlets with Glowing Blue Palm Repulsor Nodes
            hands.forEach { hc ->
                drawCircle(color = Color(0xFFDC2626), radius = r * 0.21f, center = hc)
                drawCircle(color = Color(0xFFFACC15), radius = r * 0.21f, center = hc, style = Stroke(width = 2.6f))
                drawCircle(color = Color(0xFF00F2FE), radius = r * 0.11f, center = hc)
                drawCircle(color = Color.White, radius = r * 0.055f, center = hc)
            }
        }

        MascotHandGloves.HERO_WEB_SHOOTERS -> {
            // Red Spider-Gloves Firing Twin Silver Web Strands Upward
            hands.forEachIndexed { idx, hc ->
                val dir = if (idx == 0) -1f else 1f
                drawLine(Color(0xFFE2E8F0), hc, Offset(hc.x + dir * r * 0.45f, hc.y - r * 0.85f), strokeWidth = 2.6f)
                drawCircle(color = Color(0xFFDC2626), radius = r * 0.18f, center = hc)
                drawCircle(color = Color(0xFF1E293B), radius = r * 0.07f, center = hc)
            }
        }

        MascotHandGloves.HERO_MJOLNIR_AND_SHIELD -> {
            // Right Hand: Asgardian Mjolnir Hammer with Lightning | Left Hand: Star Vibranium Round Shield
            val rh = rightHandCenter
            drawLine(Color(0xFF78350F), Offset(rh.x, rh.y + r * 0.18f), Offset(rh.x, rh.y - r * 0.48f), strokeWidth = 3.6f)
            drawRoundRect(
                color = Color(0xFF94A3B8),
                topLeft = Offset(rh.x - r * 0.24f, rh.y - r * 0.74f),
                size = Size(r * 0.48f, r * 0.30f),
                cornerRadius = CornerRadius(5f, 5f)
            )
            drawRoundRect(
                color = Color(0xFF00F2FE),
                topLeft = Offset(rh.x - r * 0.24f, rh.y - r * 0.74f),
                size = Size(r * 0.48f, r * 0.30f),
                cornerRadius = CornerRadius(5f, 5f),
                style = Stroke(width = 2.0f)
            )
            drawCircle(color = defaultHandColor, radius = r * 0.15f, center = rh)

            val lh = leftHandCenter
            drawCircle(color = Color(0xFFDC2626), radius = r * 0.28f, center = lh)
            drawCircle(color = Color.White, radius = r * 0.20f, center = lh)
            drawCircle(color = Color(0xFF1D4ED8), radius = r * 0.12f, center = lh)
            drawCircle(color = Color.White, radius = r * 0.055f, center = lh)
        }

        MascotHandGloves.FANTASY_ELVEN_LIGHT_BOW -> {
            // Left Hand: Golden Elven Greatbow | Right Hand: Glowing Starlight Mana Arrow
            val lh = leftHandCenter
            drawArc(
                color = Color(0xFFFACC15),
                startAngle = 110f,
                sweepAngle = 140f,
                useCenter = false,
                topLeft = Offset(lh.x - r * 0.34f, lh.y - r * 0.62f),
                size = Size(r * 0.68f, r * 1.24f),
                style = Stroke(width = 3.6f, cap = StrokeCap.Round)
            )
            drawLine(Color(0xFF6EE7B7), Offset(lh.x - r * 0.12f, lh.y - r * 0.56f), Offset(lh.x - r * 0.12f, lh.y + r * 0.56f), strokeWidth = 1.8f)
            drawCircle(color = defaultHandColor, radius = r * 0.15f, center = lh)

            val rh = rightHandCenter
            drawLine(Color(0xFF00F2FE), Offset(rh.x - r * 0.36f, rh.y - r * 0.24f), Offset(rh.x + r * 0.24f, rh.y + r * 0.12f), strokeWidth = 3.0f, cap = StrokeCap.Round)
            drawCircle(color = defaultHandColor, radius = r * 0.15f, center = rh)
        }

        MascotHandGloves.FANTASY_NECRO_SCYTHE -> {
            // Right Hand: Giant Abyssal Necromancer Scythe with Violet Flame Blade | Left Hand: Soul Lantern
            val rh = rightHandCenter
            drawLine(Color(0xFF1E1B4B), Offset(rh.x, rh.y + r * 0.35f), Offset(rh.x, rh.y - r * 0.95f), strokeWidth = 3.8f, cap = StrokeCap.Round)
            val scytheBlade = Path().apply {
                moveTo(rh.x, rh.y - r * 0.92f)
                quadraticTo(rh.x - r * 0.64f, rh.y - r * 1.02f, rh.x - r * 0.78f, rh.y - r * 0.52f)
                quadraticTo(rh.x - r * 0.42f, rh.y - r * 0.76f, rh.x, rh.y - r * 0.74f)
                close()
            }
            drawPath(scytheBlade, brush = Brush.horizontalGradient(listOf(Color(0xFFA855F7), Color(0xFFE2E8F0))))
            drawCircle(color = defaultHandColor, radius = r * 0.15f, center = rh)

            val lh = leftHandCenter
            drawCircle(color = defaultHandColor, radius = r * 0.15f, center = lh)
            drawCircle(color = Color(0xFFA855F7).copy(alpha = 0.65f), radius = r * 0.20f, center = Offset(lh.x, lh.y - r * 0.28f))
            drawCircle(color = Color(0xFFFEF08A), radius = r * 0.08f, center = Offset(lh.x, lh.y - r * 0.28f))
        }
    }
}

// ============================================================================
// 5. HYPER-DETAILED 40 HEAD / FACE / FLOATING ACCESSORIES RENDERER
// ============================================================================

internal fun DrawScope.drawMascotExtendedAccessories(
    cx: Float,
    cy: Float,
    r: Float,
    timeSec: Float,
    leftEyeX: Float,
    rightEyeX: Float,
    eyeY: Float,
    accessory: MascotAccessory
) {
    when (accessory) {
        MascotAccessory.CYBER_VR_VISOR -> {
            val visorW = r * 1.58f
            val visorH = r * 0.46f
            val vx = cx - visorW * 0.5f
            val vy = eyeY - visorH * 0.52f
            drawRoundRect(
                color = Color(0xFF0F172A),
                topLeft = Offset(vx - r * 0.08f, vy + r * 0.06f),
                size = Size(visorW + r * 0.16f, visorH - r * 0.12f),
                cornerRadius = CornerRadius(r * 0.10f, r * 0.10f)
            )
            drawRoundRect(
                brush = Brush.horizontalGradient(listOf(Color(0xFF00F2FE), Color(0xFF3B82F6), Color(0xFFEC4899))),
                topLeft = Offset(vx, vy),
                size = Size(visorW, visorH),
                cornerRadius = CornerRadius(r * 0.15f, r * 0.15f)
            )
            drawRoundRect(
                color = Color.White,
                topLeft = Offset(vx, vy),
                size = Size(visorW, visorH),
                cornerRadius = CornerRadius(r * 0.15f, r * 0.15f),
                style = Stroke(width = (r * 0.042f).coerceAtLeast(2f))
            )
            val scanX = vx + r * 0.20f + ((sin(timeSec * 4f) * 0.5f + 0.5f) * (visorW - r * 0.40f))
            drawLine(
                color = Color.White.copy(alpha = 0.85f),
                start = Offset(vx + r * 0.16f, eyeY),
                end = Offset(vx + visorW - r * 0.16f, eyeY),
                strokeWidth = (r * 0.026f).coerceAtLeast(1.5f),
                cap = StrokeCap.Round
            )
            drawCircle(color = Color(0xFFFEF08A), radius = r * 0.055f, center = Offset(scanX, eyeY))
        }

        MascotAccessory.MECHA_ANTENNA -> {
            val crestY = cy - r * 0.78f
            val backFinPath = Path().apply {
                moveTo(cx, crestY + r * 0.10f)
                lineTo(cx - r * 0.96f, crestY - r * 0.26f)
                lineTo(cx - r * 0.72f, crestY + r * 0.02f)
                lineTo(cx + r * 0.72f, crestY + r * 0.02f)
                lineTo(cx + r * 0.96f, crestY - r * 0.26f)
                close()
            }
            drawPath(path = backFinPath, color = Color(0xFFF8FAFC))
            val vFinPath = Path().apply {
                moveTo(cx, crestY + r * 0.14f)
                lineTo(cx - r * 0.88f, crestY - r * 0.52f)
                lineTo(cx - r * 0.66f, crestY - r * 0.16f)
                lineTo(cx, crestY + r * 0.02f)
                lineTo(cx + r * 0.66f, crestY - r * 0.16f)
                lineTo(cx + r * 0.88f, crestY - r * 0.52f)
                close()
            }
            drawPath(
                path = vFinPath,
                brush = Brush.verticalGradient(listOf(Color(0xFFFEF08A), Color(0xFFFACC15), Color(0xFFD97706)))
            )
            drawPath(path = vFinPath, color = Color(0xFF78350F), style = Stroke(width = 2f))
            drawRoundRect(
                color = Color(0xFFDC2626),
                topLeft = Offset(cx - r * 0.13f, crestY - r * 0.08f),
                size = Size(r * 0.26f, r * 0.24f),
                cornerRadius = CornerRadius(r * 0.05f, r * 0.05f)
            )
            drawCircle(color = Color(0xFF10B981), radius = r * 0.08f, center = Offset(cx, crestY + r * 0.04f))
        }

        MascotAccessory.HACKER_ANON_MASK -> {
            val maskTop = eyeY + r * 0.10f
            val maskPath = Path().apply {
                moveTo(cx - r * 0.78f, maskTop)
                lineTo(cx + r * 0.78f, maskTop)
                lineTo(cx + r * 0.60f, cy + r * 0.58f)
                lineTo(cx, cy + r * 0.72f)
                lineTo(cx - r * 0.60f, cy + r * 0.58f)
                close()
            }
            drawPath(path = maskPath, color = Color(0xFF090D16))
            drawPath(
                path = maskPath,
                color = Color(0xFF10B981),
                style = Stroke(width = (r * 0.042f).coerceAtLeast(2f))
            )
            for (tIdx in -2..2) {
                val tx = cx + tIdx * (r * 0.15f)
                val ty = cy + r * 0.36f + (2 - abs(tIdx)) * (r * 0.03f)
                drawLine(
                    color = Color(0xFF34D399),
                    start = Offset(tx, ty - r * 0.07f),
                    end = Offset(tx, ty + r * 0.07f),
                    strokeWidth = (r * 0.045f).coerceAtLeast(2.2f),
                    cap = StrokeCap.Round
                )
            }
        }

        MascotAccessory.CYBER_MONOCLE -> {
            val mr = r * 0.30f
            val chainPath = Path().apply {
                moveTo(rightEyeX + mr * 0.8f, eyeY + mr * 0.5f)
                quadraticTo(rightEyeX + r * 0.48f, cy + r * 0.45f, rightEyeX + r * 0.15f, cy + r * 0.62f)
            }
            drawPath(chainPath, color = Color(0xFFFACC15), style = Stroke(width = 2.2f, cap = StrokeCap.Round))
            drawCircle(color = Color(0xFF0EA5E9).copy(alpha = 0.42f), radius = mr, center = Offset(rightEyeX, eyeY))
            drawCircle(color = Color(0xFFFACC15), radius = mr, center = Offset(rightEyeX, eyeY), style = Stroke(width = (r * 0.052f).coerceAtLeast(2.4f)))
            drawLine(Color(0xFF00F2FE), Offset(rightEyeX - mr * 1.25f, eyeY), Offset(rightEyeX + mr * 1.25f, eyeY), strokeWidth = 1.8f)
            drawLine(Color(0xFF00F2FE), Offset(rightEyeX, eyeY - mr * 1.25f), Offset(rightEyeX, eyeY + mr * 1.25f), strokeWidth = 1.8f)
        }

        MascotAccessory.MINI_DRONE_ORBIT -> {
            val droneX = cx + r * 0.98f + cos(timeSec * 3.2f) * (r * 0.14f)
            val droneY = cy - r * 0.94f + sin(timeSec * 4.4f) * (r * 0.12f)
            val beam = Path().apply {
                moveTo(droneX, droneY + r * 0.12f)
                lineTo(droneX - r * 0.24f, droneY + r * 0.55f)
                lineTo(droneX + r * 0.24f, droneY + r * 0.55f)
                close()
            }
            drawPath(beam, color = Color(0xFF00F2FE).copy(alpha = 0.28f))
            drawLine(
                color = Color(0xFF94A3B8),
                start = Offset(droneX - r * 0.34f, droneY - r * 0.18f),
                end = Offset(droneX + r * 0.34f, droneY - r * 0.18f),
                strokeWidth = (r * 0.045f).coerceAtLeast(2.2f),
                cap = StrokeCap.Round
            )
            drawCircle(color = Color(0xFF1E293B), radius = r * 0.22f, center = Offset(droneX, droneY))
            drawCircle(color = Color(0xFF00F2FE), radius = r * 0.10f, center = Offset(droneX, droneY))
            drawCircle(color = Color.White, radius = r * 0.04f, center = Offset(droneX - r * 0.03f, droneY - r * 0.03f))
        }

        MascotAccessory.DEAL_WITH_IT_SHADES -> {
            val gw = r * 1.52f
            val gh = r * 0.38f
            val gx = cx - gw * 0.5f
            val gy = eyeY - gh * 0.45f
            drawLine(
                color = Color(0xFFFACC15),
                start = Offset(gx - r * 0.06f, gy + r * 0.04f),
                end = Offset(gx + gw + r * 0.06f, gy + r * 0.04f),
                strokeWidth = (r * 0.065f).coerceAtLeast(2.8f),
                cap = StrokeCap.Round
            )
            for (lx in listOf(leftEyeX, rightEyeX)) {
                drawRoundRect(
                    color = Color(0xFF090D16),
                    topLeft = Offset(lx - r * 0.34f, gy),
                    size = Size(r * 0.68f, gh),
                    cornerRadius = CornerRadius(r * 0.06f, r * 0.06f)
                )
                drawRoundRect(
                    color = Color(0xFFFACC15),
                    topLeft = Offset(lx - r * 0.34f, gy),
                    size = Size(r * 0.68f, gh),
                    cornerRadius = CornerRadius(r * 0.06f, r * 0.06f),
                    style = Stroke(width = 2f)
                )
                drawLine(Color.White, Offset(lx - r * 0.22f, gy + gh * 0.22f), Offset(lx - r * 0.06f, gy + gh * 0.74f), strokeWidth = (r * 0.05f).coerceAtLeast(2.2f), cap = StrokeCap.Round)
                drawLine(Color.White, Offset(lx - r * 0.04f, gy + gh * 0.22f), Offset(lx + r * 0.08f, gy + gh * 0.62f), strokeWidth = (r * 0.035f).coerceAtLeast(1.6f), cap = StrokeCap.Round)
            }
        }

        MascotAccessory.GOLDEN_TOP_HAT -> {
            val brimW = r * 1.38f
            val baseY = cy - r * 0.80f
            val topH = r * 0.62f
            drawRoundRect(
                brush = Brush.verticalGradient(listOf(Color(0xFFFEF08A), Color(0xFFFACC15), Color(0xFFB45309))),
                topLeft = Offset(cx - r * 0.48f, baseY - topH),
                size = Size(r * 0.96f, topH),
                cornerRadius = CornerRadius(r * 0.09f, r * 0.09f)
            )
            drawRoundRect(
                color = Color(0xFFDC2626),
                topLeft = Offset(cx - r * 0.48f, baseY - r * 0.16f),
                size = Size(r * 0.96f, r * 0.14f)
            )
            drawRoundRect(
                color = Color(0xFFFEF08A),
                topLeft = Offset(cx - r * 0.12f, baseY - r * 0.17f),
                size = Size(r * 0.24f, r * 0.16f),
                style = Stroke(width = 2.5f)
            )
            drawRoundRect(
                color = Color(0xFF92400E),
                topLeft = Offset(cx - brimW * 0.5f, baseY - r * 0.04f),
                size = Size(brimW, r * 0.13f),
                cornerRadius = CornerRadius(r * 0.06f, r * 0.06f)
            )
        }

        MascotAccessory.ROYAL_MUSTACHE -> {
            val my = cy + r * 0.14f
            for (side in listOf(-1f, 1f)) {
                val mPath = Path().apply {
                    moveTo(cx, my)
                    quadraticTo(cx + side * r * 0.30f, my - r * 0.14f, cx + side * r * 0.56f, my + r * 0.04f)
                    quadraticTo(cx + side * r * 0.70f, my - r * 0.08f, cx + side * r * 0.60f, my - r * 0.19f)
                }
                drawPath(
                    path = mPath,
                    color = Color(0xFF451A03),
                    style = Stroke(width = (r * 0.075f).coerceAtLeast(3.2f), cap = StrokeCap.Round)
                )
            }
        }

        MascotAccessory.DIAMOND_TIARA -> {
            val ty = cy - r * 0.84f
            val tiaraPath = Path().apply {
                moveTo(cx - r * 0.62f, ty)
                lineTo(cx - r * 0.42f, ty - r * 0.22f)
                lineTo(cx - r * 0.22f, ty - r * 0.12f)
                lineTo(cx, ty - r * 0.48f)
                lineTo(cx + r * 0.22f, ty - r * 0.12f)
                lineTo(cx + r * 0.42f, ty - r * 0.22f)
                lineTo(cx + r * 0.62f, ty)
                close()
            }
            drawPath(path = tiaraPath, brush = Brush.verticalGradient(listOf(Color.White, Color(0xFFBAE6FD))))
            drawPath(path = tiaraPath, color = Color(0xFF0284C7), style = Stroke(width = (r * 0.038f).coerceAtLeast(1.8f)))
            drawCircle(color = Color(0xFF38BDF8), radius = r * 0.085f, center = Offset(cx, ty - r * 0.20f))
            drawCircle(color = Color(0xFFEC4899), radius = r * 0.055f, center = Offset(cx - r * 0.32f, ty - r * 0.10f))
            drawCircle(color = Color(0xFFEC4899), radius = r * 0.055f, center = Offset(cx + r * 0.32f, ty - r * 0.10f))
        }

        MascotAccessory.DOLLAR_CHAIN_CROWN -> {
            val medY = cy + r * 0.46f
            drawArc(
                color = Color(0xFFFACC15),
                startAngle = 15f,
                sweepAngle = 150f,
                useCenter = false,
                topLeft = Offset(cx - r * 0.68f, cy + r * 0.02f),
                size = Size(r * 1.36f, r * 0.48f),
                style = Stroke(width = (r * 0.075f).coerceAtLeast(3.2f), cap = StrokeCap.Round)
            )
            drawCircle(color = Color(0xFFB45309), radius = r * 0.19f, center = Offset(cx, medY + r * 0.03f))
            drawCircle(color = Color(0xFFFACC15), radius = r * 0.18f, center = Offset(cx, medY))
            drawCircle(color = Color(0xFF10B981), radius = r * 0.09f, center = Offset(cx, medY))
        }

        MascotAccessory.DRAGON_HORNS -> {
            val baseY = cy - r * 0.72f
            for (side in listOf(-1f, 1f)) {
                val hornPath = Path().apply {
                    moveTo(cx + side * r * 0.34f, baseY)
                    quadraticTo(cx + side * r * 0.94f, baseY - r * 0.30f, cx + side * r * 0.80f, baseY - r * 0.76f)
                    quadraticTo(cx + side * r * 0.50f, baseY - r * 0.36f, cx + side * r * 0.16f, baseY)
                    close()
                }
                drawPath(path = hornPath, brush = Brush.verticalGradient(listOf(Color(0xFFFEF08A), Color(0xFFEF4444), Color(0xFF7F1D1D))))
                drawPath(path = hornPath, color = Color(0xFF450A0A), style = Stroke(width = 2f))
            }
        }

        MascotAccessory.THIRD_EYE_CRYSTAL -> {
            val fy = eyeY - r * 0.34f
            drawCircle(color = Color(0xFFFACC15).copy(alpha = 0.45f), radius = r * 0.22f, center = Offset(cx, fy))
            val gemPath = Path().apply {
                moveTo(cx, fy - r * 0.18f)
                lineTo(cx + r * 0.13f, fy)
                lineTo(cx, fy + r * 0.18f)
                lineTo(cx - r * 0.13f, fy)
                close()
            }
            drawPath(path = gemPath, color = Color(0xFFA855F7))
            drawPath(path = gemPath, color = Color(0xFFFDE047), style = Stroke(width = (r * 0.035f).coerceAtLeast(1.8f)))
            drawCircle(color = Color.White, radius = r * 0.04f, center = Offset(cx - r * 0.03f, fy - r * 0.05f))
        }

        MascotAccessory.CAT_EARS_NEKO -> {
            val baseY = cy - r * 0.66f
            for (side in listOf(-1f, 1f)) {
                val outerEar = Path().apply {
                    moveTo(cx + side * r * 0.20f, baseY)
                    lineTo(cx + side * r * 0.66f, baseY - r * 0.54f)
                    lineTo(cx + side * r * 0.82f, baseY + r * 0.06f)
                    close()
                }
                drawPath(path = outerEar, color = Color(0xFF1E293B))
                val innerEar = Path().apply {
                    moveTo(cx + side * r * 0.30f, baseY - r * 0.02f)
                    lineTo(cx + side * r * 0.62f, baseY - r * 0.40f)
                    lineTo(cx + side * r * 0.72f, baseY + r * 0.02f)
                    close()
                }
                drawPath(path = innerEar, color = Color(0xFFF472B6))
            }
        }

        MascotAccessory.BUNNY_EARS -> {
            val baseY = cy - r * 0.70f
            for (side in listOf(-1f, 1f)) {
                val ex = cx + side * r * 0.38f
                drawRoundRect(
                    color = Color.White,
                    topLeft = Offset(ex - r * 0.15f, baseY - r * 0.70f),
                    size = Size(r * 0.30f, r * 0.74f),
                    cornerRadius = CornerRadius(r * 0.15f, r * 0.15f)
                )
                drawRoundRect(
                    color = Color(0xFFF472B6),
                    topLeft = Offset(ex - r * 0.09f, baseY - r * 0.58f),
                    size = Size(r * 0.18f, r * 0.54f),
                    cornerRadius = CornerRadius(r * 0.09f, r * 0.09f)
                )
            }
        }

        MascotAccessory.KAWAII_RIBBON_BOW -> {
            val bx = cx + r * 0.52f
            val by = cy - r * 0.74f
            val bowPath = Path().apply {
                moveTo(bx, by)
                lineTo(bx - r * 0.34f, by - r * 0.20f)
                lineTo(bx - r * 0.28f, by + r * 0.20f)
                close()
                moveTo(bx, by)
                lineTo(bx + r * 0.34f, by - r * 0.20f)
                lineTo(bx + r * 0.28f, by + r * 0.20f)
                close()
            }
            drawPath(path = bowPath, color = Color(0xFFF43F5E))
            drawPath(path = bowPath, color = Color.White, style = Stroke(width = 2f))
            drawCircle(color = Color(0xFFFACC15), radius = r * 0.095f, center = Offset(bx, by))
        }

        MascotAccessory.SUNFLOWER_SPROUT -> {
            val topY = cy - r * 0.78f
            drawLine(
                color = Color(0xFF16A34A),
                start = Offset(cx, topY),
                end = Offset(cx, topY - r * 0.42f),
                strokeWidth = (r * 0.055f).coerceAtLeast(2.4f),
                cap = StrokeCap.Round
            )
            drawOval(
                color = Color(0xFF22C55E),
                topLeft = Offset(cx, topY - r * 0.32f),
                size = Size(r * 0.28f, r * 0.15f)
            )
            val flowerCy = topY - r * 0.46f
            for (p in 0..7) {
                val a = (p * 45f) * (PI.toFloat() / 180f)
                drawCircle(
                    color = Color(0xFFFACC15),
                    radius = r * 0.07f,
                    center = Offset(cx + cos(a) * (r * 0.12f), flowerCy + sin(a) * (r * 0.12f))
                )
            }
            drawCircle(color = Color(0xFF78350F), radius = r * 0.085f, center = Offset(cx, flowerCy))
        }

        MascotAccessory.BAMBOO_COPTER -> {
            val topY = cy - r * 0.78f
            drawLine(
                color = Color(0xFFEAB308),
                start = Offset(cx, topY),
                end = Offset(cx, topY - r * 0.36f),
                strokeWidth = (r * 0.055f).coerceAtLeast(2.4f),
                cap = StrokeCap.Round
            )
            val bladeW = r * 0.78f
            drawOval(
                color = Color(0xFFFDE047).copy(alpha = 0.45f),
                topLeft = Offset(cx - bladeW, topY - r * 0.42f),
                size = Size(bladeW * 2f, r * 0.14f)
            )
            val spinW = r * (0.76f * cos(timeSec * 16f)).coerceIn(-r * 0.76f, r * 0.76f)
            drawLine(
                color = Color(0xFFFACC15),
                start = Offset(cx - spinW, topY - r * 0.35f),
                end = Offset(cx + spinW, topY - r * 0.35f),
                strokeWidth = (r * 0.06f).coerceAtLeast(2.8f),
                cap = StrokeCap.Round
            )
            drawCircle(color = Color(0xFFEF4444), radius = r * 0.08f, center = Offset(cx, topY - r * 0.35f))
        }

        MascotAccessory.PIRATE_CAPTAIN_HAT -> {
            val baseY = cy - r * 0.78f
            val hatPath = Path().apply {
                moveTo(cx - r * 0.96f, baseY)
                quadraticTo(cx, baseY - r * 0.68f, cx + r * 0.96f, baseY)
                close()
            }
            drawPath(path = hatPath, color = Color(0xFF0F172A))
            drawPath(path = hatPath, color = Color(0xFFFACC15), style = Stroke(width = (r * 0.045f).coerceAtLeast(2f)))
            drawLine(Color.White, Offset(cx - r * 0.14f, baseY - r * 0.12f), Offset(cx + r * 0.14f, baseY - r * 0.32f), strokeWidth = 2.5f)
            drawLine(Color.White, Offset(cx + r * 0.14f, baseY - r * 0.12f), Offset(cx - r * 0.14f, baseY - r * 0.32f), strokeWidth = 2.5f)
            drawCircle(color = Color.White, radius = r * 0.10f, center = Offset(cx, baseY - r * 0.24f))
        }

        MascotAccessory.FAIRY_FLOWER_CROWN -> {
            val baseY = cy - r * 0.76f
            val colors = listOf(Color(0xFFF472B6), Color(0xFFFACC15), Color(0xFF38BDF8), Color(0xFFA855F7), Color(0xFF4ADE80))
            drawLine(Color(0xFF15803D), Offset(cx - r * 0.65f, baseY + r * 0.04f), Offset(cx + r * 0.65f, baseY + r * 0.04f), strokeWidth = 3f)
            for (idx in -2..2) {
                val fx = cx + idx * r * 0.27f
                val fy = baseY + abs(idx) * r * 0.03f
                drawCircle(color = colors[idx + 2], radius = r * 0.125f, center = Offset(fx, fy))
                drawCircle(color = Color.White, radius = r * 0.055f, center = Offset(fx, fy))
            }
        }

        // --- NEW HEAD / FACE / FLOATING COMPLEX ACCESSORIES ---

        MascotAccessory.GUNDAM_VFIN_HELMET -> {
            // Full Gundam Head Vulcan Pod + Cheek Vents + Quad Golden V-Fin Crest
            val crestY = cy - r * 0.80f
            for (side in listOf(-1f, 1f)) {
                // Side vulcan ear pods
                drawRoundRect(
                    color = Color(0xFFF8FAFC),
                    topLeft = Offset(cx + side * r * 0.86f - r * 0.10f, cy - r * 0.36f),
                    size = Size(r * 0.20f, r * 0.42f),
                    cornerRadius = CornerRadius(r * 0.06f, r * 0.06f)
                )
                drawCircle(color = Color(0xFFFACC15), radius = r * 0.045f, center = Offset(cx + side * r * 0.86f, cy - r * 0.24f))
            }
            val quadFin = Path().apply {
                moveTo(cx, crestY + r * 0.12f)
                lineTo(cx - r * 1.04f, crestY - r * 0.56f)
                lineTo(cx - r * 0.74f, crestY - r * 0.12f)
                lineTo(cx, crestY + r * 0.04f)
                lineTo(cx + r * 0.74f, crestY - r * 0.12f)
                lineTo(cx + r * 1.04f, crestY - r * 0.56f)
                close()
            }
            drawPath(quadFin, brush = Brush.verticalGradient(listOf(Color(0xFFFEF08A), Color(0xFFFACC15))))
            drawRoundRect(
                color = Color(0xFFEF4444),
                topLeft = Offset(cx - r * 0.15f, crestY - r * 0.10f),
                size = Size(r * 0.30f, r * 0.26f),
                cornerRadius = CornerRadius(r * 0.05f, r * 0.05f)
            )
            drawCircle(color = Color(0xFF00F2FE), radius = r * 0.085f, center = Offset(cx, crestY + r * 0.03f))
        }

        MascotAccessory.EXOSKELETON_HUD_HALO -> {
            // Rotating Cybernetic Hexagon Halo with Floating HUD Telemetry Crosshairs
            val haloY = cy - r * 0.94f
            drawOval(
                color = Color(0xFF10B981),
                topLeft = Offset(cx - r * 0.82f, haloY - r * 0.18f),
                size = Size(r * 1.64f, r * 0.36f),
                style = Stroke(width = 3.0f)
            )
            for (i in 0..5) {
                val a = (i * 60f + timeSec * 50f) * (PI.toFloat() / 180f)
                val hx = cx + cos(a) * r * 0.82f
                val hy = haloY + sin(a) * r * 0.18f
                drawCircle(color = Color(0xFF00F2FE), radius = r * 0.06f, center = Offset(hx, hy))
            }
        }

        MascotAccessory.EMPEROR_DRAGON_CROWN -> {
            // Imperial Golden Dragon Crown with Hanging Pearl Tassels (Mian旒)
            val baseY = cy - r * 0.80f
            drawRoundRect(
                brush = Brush.verticalGradient(listOf(Color(0xFFFEF08A), Color(0xFFD97706))),
                topLeft = Offset(cx - r * 0.64f, baseY - r * 0.38f),
                size = Size(r * 1.28f, r * 0.38f),
                cornerRadius = CornerRadius(r * 0.08f, r * 0.08f)
            )
            drawRoundRect(
                color = Color(0xFF1E293B),
                topLeft = Offset(cx - r * 0.78f, baseY - r * 0.44f),
                size = Size(r * 1.56f, r * 0.10f),
                cornerRadius = CornerRadius(r * 0.04f, r * 0.04f)
            )
            // Hanging pearl bead strands on left and right
            for (side in listOf(-0.68f, -0.50f, 0.50f, 0.68f)) {
                val bx = cx + r * side
                for (b in 0..2) {
                    drawCircle(color = Color(0xFFFef9C3), radius = r * 0.035f, center = Offset(bx, baseY - r * 0.30f + b * r * 0.11f))
                }
            }
            drawCircle(color = Color(0xFFEF4444), radius = r * 0.10f, center = Offset(cx, baseY - r * 0.19f))
        }

        MascotAccessory.ABYSSAL_DEMON_LORD_HORNS -> {
            // Double-Curved Obsidian Demon Overlord Horns + Floating Hellfire Crown Core
            val baseY = cy - r * 0.74f
            for (side in listOf(-1f, 1f)) {
                val horn = Path().apply {
                    moveTo(cx + side * r * 0.28f, baseY)
                    cubicTo(
                        cx + side * r * 1.12f,
                        baseY - r * 0.10f,
                        cx + side * r * 1.18f,
                        baseY - r * 0.72f,
                        cx + side * r * 0.62f,
                        baseY - r * 0.90f
                    )
                    cubicTo(
                        cx + side * r * 0.84f,
                        baseY - r * 0.54f,
                        cx + side * r * 0.72f,
                        baseY - r * 0.20f,
                        cx + side * r * 0.12f,
                        baseY
                    )
                    close()
                }
                drawPath(horn, brush = Brush.verticalGradient(listOf(Color(0xFFEF4444), Color(0xFF450A0A), Color(0xFF090D16))))
                drawPath(horn, color = Color(0xFFF97316), style = Stroke(width = 1.8f))
            }
            drawCircle(color = Color(0xFFEF4444), radius = r * 0.12f, center = Offset(cx, baseY - r * 0.46f))
            drawCircle(color = Color(0xFFFEF08A), radius = r * 0.06f, center = Offset(cx, baseY - r * 0.46f))
        }

        MascotAccessory.KITSUNE_SPIRIT_MASK -> {
            // White & Crimson Japanese Kitsune Mask Worn Tilted on Side of Head
            val mx = cx + r * 0.66f
            val my = cy - r * 0.62f
            rotate(degrees = 22f, pivot = Offset(mx, my)) {
                drawCircle(color = Color.White, radius = r * 0.30f, center = Offset(mx, my))
                drawCircle(color = Color(0xFFEF4444), radius = r * 0.30f, center = Offset(mx, my), style = Stroke(width = 2.2f))
                // Fox ears on mask
                for (s in listOf(-1f, 1f)) {
                    val ear = Path().apply {
                        moveTo(mx + s * r * 0.08f, my - r * 0.24f)
                        lineTo(mx + s * r * 0.26f, my - r * 0.48f)
                        lineTo(mx + s * r * 0.28f, my - r * 0.14f)
                        close()
                    }
                    drawPath(ear, color = Color(0xFFEF4444))
                }
                drawLine(Color(0xFF0F172A), Offset(mx - r * 0.16f, my - r * 0.04f), Offset(mx - r * 0.04f, my), strokeWidth = 2.4f, cap = StrokeCap.Round)
                drawLine(Color(0xFF0F172A), Offset(mx + r * 0.16f, my - r * 0.04f), Offset(mx + r * 0.04f, my), strokeWidth = 2.4f, cap = StrokeCap.Round)
            }
        }

        MascotAccessory.SHOGUN_KABUTO_CREST -> {
            // Samurai Kabuto Helmet with Golden Crescent Moon Maedate Crest
            val baseY = cy - r * 0.76f
            drawArc(
                color = Color(0xFF111827),
                startAngle = 180f,
                sweepAngle = 180f,
                useCenter = true,
                topLeft = Offset(cx - r * 0.84f, baseY - r * 0.36f),
                size = Size(r * 1.68f, r * 0.72f)
            )
            // Golden Crescent Horns
            val crescent = Path().apply {
                moveTo(cx, baseY - r * 0.18f)
                quadraticTo(cx - r * 0.76f, baseY - r * 0.24f, cx - r * 0.86f, baseY - r * 0.84f)
                quadraticTo(cx - r * 0.48f, baseY - r * 0.38f, cx, baseY - r * 0.30f)
                quadraticTo(cx + r * 0.48f, baseY - r * 0.38f, cx + r * 0.86f, baseY - r * 0.84f)
                quadraticTo(cx + r * 0.76f, baseY - r * 0.24f, cx, baseY - r * 0.18f)
                close()
            }
            drawPath(crescent, brush = Brush.verticalGradient(listOf(Color(0xFFFEF08A), Color(0xFFF59E0B))))
            drawCircle(color = Color(0xFFEF4444), radius = r * 0.10f, center = Offset(cx, baseY - r * 0.22f))
        }

        MascotAccessory.ELEMENTAL_ORBS_CROWN -> {
            // 5 Orbiting Elemental Magatama Orbs (Fire, Water, Wind, Lightning, Void)
            val orbCols = listOf(Color(0xFFEF4444), Color(0xFF38BDF8), Color(0xFF22C55E), Color(0xFFFACC15), Color(0xFFA855F7))
            orbCols.forEachIndexed { idx, col ->
                val a = ((idx * 72f + timeSec * 45f) * (PI.toFloat() / 180f))
                val ox = cx + cos(a) * r * 1.02f
                val oy = (cy - r * 0.65f) + sin(a) * r * 0.28f
                drawCircle(color = col.copy(alpha = 0.45f), radius = r * 0.15f, center = Offset(ox, oy))
                drawCircle(color = col, radius = r * 0.10f, center = Offset(ox, oy))
                drawCircle(color = Color.White, radius = r * 0.04f, center = Offset(ox - r * 0.02f, oy - r * 0.02f))
            }
        }

        MascotAccessory.SULTAN_DIAMOND_TURBAN -> {
            // Royal Sultan Silk Turban with Giant Sapphire Brooch & White Peacock Plume
            val baseY = cy - r * 0.74f
            drawOval(
                brush = Brush.verticalGradient(listOf(Color(0xFFFEF08A), Color(0xFFF59E0B))),
                topLeft = Offset(cx - r * 0.76f, baseY - r * 0.46f),
                size = Size(r * 1.52f, r * 0.56f)
            )
            // White feather plume rising from center
            val plume = Path().apply {
                moveTo(cx, baseY - r * 0.30f)
                quadraticTo(cx + r * 0.32f, baseY - r * 0.88f, cx + r * 0.08f, baseY - r * 0.96f)
                quadraticTo(cx - r * 0.18f, baseY - r * 0.72f, cx, baseY - r * 0.30f)
                close()
            }
            drawPath(plume, color = Color.White)
            drawCircle(color = Color(0xFF0284C7), radius = r * 0.13f, center = Offset(cx, baseY - r * 0.22f))
            drawCircle(color = Color.White, radius = r * 0.13f, center = Offset(cx, baseY - r * 0.22f), style = Stroke(width = 2.0f))
        }

        MascotAccessory.CHEF_TOQUE_CROWN -> {
            // Tall Puffy French Master Chef Toque Hat with Golden Ribbon Band
            val baseY = cy - r * 0.78f
            for (puff in -1..1) {
                drawCircle(color = Color.White, radius = r * 0.28f, center = Offset(cx + puff * r * 0.26f, baseY - r * 0.42f))
            }
            drawRoundRect(
                color = Color.White,
                topLeft = Offset(cx - r * 0.48f, baseY - r * 0.36f),
                size = Size(r * 0.96f, r * 0.38f),
                cornerRadius = CornerRadius(r * 0.08f, r * 0.08f)
            )
            drawRoundRect(
                color = Color(0xFFFACC15),
                topLeft = Offset(cx - r * 0.48f, baseY - r * 0.10f),
                size = Size(r * 0.96f, r * 0.10f)
            )
        }

        // ====================================================================
        // 12 NEW ANIME COLLAB, SUPER HERO & FANTASY HEAD / FACE ACCESSORIES
        // ====================================================================

        MascotAccessory.HANAFUDA_SUN_EARRINGS -> {
            // Demon Slayer Tanjiro's Rising Sun Hanafuda Earrings + Crimson Demon Slayer Mark on Forehead
            val scarPath = Path().apply {
                moveTo(leftEyeX - r * 0.28f, cy - r * 0.62f)
                lineTo(leftEyeX + r * 0.04f, cy - r * 0.48f)
                lineTo(leftEyeX - r * 0.12f, cy - r * 0.36f)
                close()
            }
            drawPath(scarPath, color = Color(0xFFDC2626))
            for (side in listOf(-1f, 1f)) {
                val ex = cx + side * r * 0.88f
                val ey = cy + r * 0.04f
                drawRoundRect(
                    color = Color.White,
                    topLeft = Offset(ex - r * 0.11f, ey),
                    size = Size(r * 0.22f, r * 0.38f),
                    cornerRadius = CornerRadius(3f, 3f)
                )
                drawRoundRect(
                    color = Color(0xFF090D16),
                    topLeft = Offset(ex - r * 0.11f, ey),
                    size = Size(r * 0.22f, r * 0.38f),
                    cornerRadius = CornerRadius(3f, 3f),
                    style = Stroke(width = 1.8f)
                )
                drawCircle(color = Color(0xFFEF4444), radius = r * 0.065f, center = Offset(ex, ey + r * 0.14f))
            }
        }

        MascotAccessory.NEZUKO_BAMBOO_MUZZLE -> {
            // Demon Slayer Nezuko's Green Bamboo Mouthpiece + Red Tie Ribbon + Pink Hair Ribbon
            val my = cy + r * 0.20f
            drawLine(Color(0xFFEF4444), Offset(cx - r * 0.78f, my - r * 0.04f), Offset(cx + r * 0.78f, my - r * 0.04f), strokeWidth = 2.6f)
            drawRoundRect(
                brush = Brush.verticalGradient(listOf(Color(0xFF4ADE80), Color(0xFF15803D))),
                topLeft = Offset(cx - r * 0.36f, my - r * 0.11f),
                size = Size(r * 0.72f, r * 0.22f),
                cornerRadius = CornerRadius(r * 0.08f, r * 0.08f)
            )
            drawLine(Color(0xFF052E16), Offset(cx - r * 0.16f, my - r * 0.11f), Offset(cx - r * 0.16f, my + r * 0.11f), strokeWidth = 2.2f)
            drawLine(Color(0xFF052E16), Offset(cx + r * 0.16f, my - r * 0.11f), Offset(cx + r * 0.16f, my + r * 0.11f), strokeWidth = 2.2f)
            // Pink Ribbon on Left Forehead
            drawCircle(color = Color(0xFFF472B6), radius = r * 0.12f, center = Offset(cx - r * 0.52f, cy - r * 0.68f))
        }

        MascotAccessory.AOT_SCOUT_HOOD_GOGGLES -> {
            // Attack on Titan Hange/Scout Steampunk Brass Goggles on Forehead + Green Scout Hood Rim
            val gy = cy - r * 0.56f
            drawLine(Color(0xFF451A03), Offset(cx - r * 0.80f, gy), Offset(cx + r * 0.80f, gy), strokeWidth = 3.5f)
            for (side in listOf(-1f, 1f)) {
                val lx = cx + side * r * 0.30f
                drawCircle(color = Color(0xFFD97706), radius = r * 0.20f, center = Offset(lx, gy))
                drawCircle(color = Color(0xFF38BDF8), radius = r * 0.13f, center = Offset(lx, gy))
                drawCircle(color = Color.White, radius = r * 0.05f, center = Offset(lx - r * 0.04f, gy - r * 0.04f))
            }
        }

        MascotAccessory.BLEACH_HOLLOW_MASK -> {
            // Bleach Ichigo's White Bone Hollow Vizard Mask (Half-Face Left Side with Crimson Stripes & Teeth)
            val maskHalf = Path().apply {
                moveTo(cx, cy - r * 0.74f)
                quadraticTo(cx - r * 0.86f, cy - r * 0.68f, cx - r * 0.82f, cy + r * 0.36f)
                quadraticTo(cx - r * 0.40f, cy + r * 0.52f, cx, cy + r * 0.38f)
                close()
            }
            drawPath(maskHalf, color = Color(0xFFF8FAFC))
            drawPath(maskHalf, color = Color(0xFF090D16), style = Stroke(width = 2.2f))
            // Crimson Hollow War-Paint Stripes on Left Mask Side
            for (s in 0..2) {
                val sy = cy - r * (0.52f - s * 0.24f)
                drawLine(Color(0xFFE11D48), Offset(cx - r * 0.70f, sy), Offset(cx - r * 0.14f, sy + r * 0.08f), strokeWidth = 3.2f, cap = StrokeCap.Round)
            }
            // Glowing Hollow Eye on Left Eye
            drawCircle(color = Color(0xFF090D16), radius = r * 0.18f, center = Offset(leftEyeX, eyeY))
            drawCircle(color = Color(0xFFFACC15), radius = r * 0.09f, center = Offset(leftEyeX, eyeY))
        }

        MascotAccessory.NARUTO_SHINOBI_HEADBAND -> {
            // Naruto Konoha Metal Forehead Protector + Six Paths Sage Horns
            val bandY = cy - r * 0.56f
            drawRoundRect(
                color = Color(0xFF1E293B),
                topLeft = Offset(cx - r * 0.82f, bandY - r * 0.12f),
                size = Size(r * 1.64f, r * 0.24f),
                cornerRadius = CornerRadius(r * 0.06f, r * 0.06f)
            )
            drawRoundRect(
                color = Color(0xFFE2E8F0),
                topLeft = Offset(cx - r * 0.42f, bandY - r * 0.09f),
                size = Size(r * 0.84f, r * 0.18f),
                cornerRadius = CornerRadius(r * 0.04f, r * 0.04f)
            )
            // Red Konoha Leaf Swirl + Six Paths Chakra Horns
            drawCircle(color = Color(0xFFEF4444), radius = r * 0.055f, center = Offset(cx, bandY), style = Stroke(width = 2.0f))
            for (side in listOf(-1f, 1f)) {
                val horn = Path().apply {
                    moveTo(cx + side * r * 0.36f, bandY - r * 0.12f)
                    lineTo(cx + side * r * 0.56f, bandY - r * 0.48f)
                    lineTo(cx + side * r * 0.18f, bandY - r * 0.12f)
                    close()
                }
                drawPath(horn, color = Color(0xFFFEF08A))
            }
        }

        MascotAccessory.LINK_CLICK_GOLDEN_EYES_GEAR -> {
            // Link Click Time-Dive Golden Glowing Eye Rings + Floating Roman Clock Crown Above Head
            for (ex in listOf(leftEyeX, rightEyeX)) {
                drawCircle(color = Color(0xFFFACC15), radius = r * 0.22f, center = Offset(ex, eyeY), style = Stroke(width = 2.8f))
                drawCircle(color = Color(0xFF00F2FE), radius = r * 0.14f, center = Offset(ex, eyeY), style = Stroke(width = 1.8f))
            }
            val crownY = cy - r * 0.90f
            drawCircle(color = Color(0xFFFACC15), radius = r * 0.24f, center = Offset(cx, crownY), style = Stroke(width = 2.4f))
            drawLine(Color(0xFF00F2FE), Offset(cx, crownY), Offset(cx, crownY - r * 0.16f), strokeWidth = 2.2f)
            drawLine(Color(0xFFFACC15), Offset(cx, crownY), Offset(cx + r * 0.14f, crownY), strokeWidth = 2.2f)
        }

        MascotAccessory.SOUL_LAND_SPIRIT_RINGS -> {
            // Soul Land Sea God Golden Trident Forehead Mark + 100,000-Year Crimson-Gold Spirit Halo
            val fy = cy - r * 0.48f
            drawLine(Color(0xFFFACC15), Offset(cx, fy - r * 0.14f), Offset(cx, fy + r * 0.10f), strokeWidth = 2.8f)
            drawCircle(color = Color(0xFF00F2FE), radius = r * 0.06f, center = Offset(cx, fy))
            drawOval(
                color = Color(0xFFEF4444),
                topLeft = Offset(cx - r * 0.92f, cy - r * 1.02f),
                size = Size(r * 1.84f, r * 0.34f),
                style = Stroke(width = 3.2f)
            )
            drawOval(
                color = Color(0xFFFACC15),
                topLeft = Offset(cx - r * 0.80f, cy - r * 0.96f),
                size = Size(r * 1.60f, r * 0.24f),
                style = Stroke(width = 2.0f)
            )
        }

        MascotAccessory.BTTH_EMPEROR_FLAME_CREST -> {
            // BTTH Xiao Yan's Flame Emperor Multi-Colored Fire Lotus Forehead Seal & Jade Flame Crown
            val fy = cy - r * 0.50f
            val flameSeal = Path().apply {
                moveTo(cx, fy - r * 0.22f)
                quadraticTo(cx + r * 0.18f, fy, cx, fy + r * 0.14f)
                quadraticTo(cx - r * 0.18f, fy, cx, fy - r * 0.22f)
                close()
            }
            drawPath(flameSeal, brush = Brush.verticalGradient(listOf(Color(0xFFFEF08A), Color(0xFF10B981), Color(0xFFA855F7))))
            drawCircle(color = Color.White, radius = r * 0.045f, center = Offset(cx, fy - r * 0.02f))
        }

        MascotAccessory.HERO_IRON_HUD_HELMET -> {
            // Superhero Red-Gold Nano Faceplate Crest & Glowing Cyan LED Hero Eyes
            val crest = Path().apply {
                moveTo(cx - r * 0.56f, cy - r * 0.72f)
                lineTo(cx + r * 0.56f, cy - r * 0.72f)
                lineTo(cx + r * 0.36f, cy - r * 0.36f)
                lineTo(cx, cy - r * 0.26f)
                lineTo(cx - r * 0.36f, cy - r * 0.36f)
                close()
            }
            drawPath(crest, brush = Brush.verticalGradient(listOf(Color(0xFFFACC15), Color(0xFFDC2626))))
            drawPath(crest, color = Color(0xFF7F1D1D), style = Stroke(width = 2.0f))
            for (ex in listOf(leftEyeX, rightEyeX)) {
                drawRoundRect(
                    color = Color(0xFF00F2FE),
                    topLeft = Offset(ex - r * 0.22f, eyeY - r * 0.08f),
                    size = Size(r * 0.44f, r * 0.14f),
                    cornerRadius = CornerRadius(4f, 4f)
                )
            }
        }

        MascotAccessory.HERO_DARK_BAT_COWL -> {
            // Superhero Pointed Bat-Ears Tactical Cowl + Glowing White Detective Eyes
            val baseY = cy - r * 0.68f
            for (side in listOf(-1f, 1f)) {
                val batEar = Path().apply {
                    moveTo(cx + side * r * 0.42f, baseY)
                    lineTo(cx + side * r * 0.68f, baseY - r * 0.64f)
                    lineTo(cx + side * r * 0.78f, baseY + r * 0.06f)
                    close()
                }
                drawPath(batEar, color = Color(0xFF0F172A))
                drawPath(batEar, color = Color(0xFF475569), style = Stroke(width = 1.8f))
            }
            for (ex in listOf(leftEyeX, rightEyeX)) {
                val eyeTri = Path().apply {
                    moveTo(ex - r * 0.20f, eyeY - r * 0.06f)
                    lineTo(ex + r * 0.20f, eyeY - r * 0.02f)
                    lineTo(ex, eyeY + r * 0.10f)
                    close()
                }
                drawPath(eyeTri, color = Color.White)
            }
        }

        MascotAccessory.FANTASY_VALKYRIE_WING_HELM -> {
            // High-Fantasy Golden Tiara with Twin Feathered White Valkyrie Head-Wings
            val baseY = cy - r * 0.72f
            for (side in listOf(-1f, 1f)) {
                val headWing = Path().apply {
                    moveTo(cx + side * r * 0.48f, baseY)
                    quadraticTo(cx + side * r * 1.18f, baseY - r * 0.64f, cx + side * r * 1.10f, baseY - r * 0.06f)
                    quadraticTo(cx + side * r * 0.82f, baseY + r * 0.12f, cx + side * r * 0.52f, baseY + r * 0.08f)
                    close()
                }
                drawPath(headWing, color = Color.White)
                drawPath(headWing, color = Color(0xFF38BDF8), style = Stroke(width = 2.0f))
            }
            drawCircle(color = Color(0xFFFACC15), radius = r * 0.15f, center = Offset(cx, baseY))
            drawCircle(color = Color(0xFF0284C7), radius = r * 0.08f, center = Offset(cx, baseY))
        }

        MascotAccessory.FANTASY_LICH_SOUL_CROWN -> {
            // High-Fantasy Obsidian Bone Spikes Crown + Floating Violet Necromancer Soul Flame
            val baseY = cy - r * 0.76f
            val crown = Path().apply {
                moveTo(cx - r * 0.66f, baseY)
                lineTo(cx - r * 0.52f, baseY - r * 0.46f)
                lineTo(cx - r * 0.24f, baseY - r * 0.16f)
                lineTo(cx, baseY - r * 0.62f)
                lineTo(cx + r * 0.24f, baseY - r * 0.16f)
                lineTo(cx + r * 0.52f, baseY - r * 0.46f)
                lineTo(cx + r * 0.66f, baseY)
                close()
            }
            drawPath(crown, color = Color(0xFF090D16))
            drawPath(crown, color = Color(0xFFA855F7), style = Stroke(width = 2.2f))
            drawCircle(color = Color(0xFFC084FC), radius = r * 0.11f, center = Offset(cx, baseY - r * 0.24f))
        }

        else -> Unit
    }
}

// ============================================================================
// 6. 20 HYPER-EXPRESSIVE EMOTIONS FACE RENDERER
// ============================================================================

internal fun DrawScope.drawMascotExpressiveEmotionFace(
    cx: Float,
    cy: Float,
    r: Float,
    w: Float,
    timeSec: Float,
    leftEyeX: Float,
    rightEyeX: Float,
    eyeY: Float,
    eyeW: Float,
    eyeH: Float,
    fullEyeH: Float,
    emotion: MascotEmotionPreset
): Boolean {
    if (emotion == MascotEmotionPreset.AUTO_DYNAMIC) return false

    val mouthY = cy + r * 0.22f
    val strokeBold = (w * 0.034f).coerceAtLeast(2.4f)

    // Blush Cheeks (Customizable per emotion)
    val blushColor = when (emotion) {
        MascotEmotionPreset.FREEZING_SHIVER -> Color(0xFF38BDF8)
        MascotEmotionPreset.FEVER_OVERHEAT, MascotEmotionPreset.LOVE_BUCIN, MascotEmotionPreset.SHY_BLUSH -> Color(0xFFF43F5E)
        else -> Color(0xFFF472B6)
    }
    val blushAlpha = when (emotion) {
        MascotEmotionPreset.LOVE_BUCIN, MascotEmotionPreset.SHY_BLUSH, MascotEmotionPreset.FEVER_OVERHEAT -> 0.88f
        else -> 0.55f
    }
    drawOval(
        color = blushColor.copy(alpha = blushAlpha),
        topLeft = Offset(cx - r * 0.70f, cy + r * 0.17f),
        size = Size(r * 0.28f, r * 0.15f)
    )
    drawOval(
        color = blushColor.copy(alpha = blushAlpha),
        topLeft = Offset(cx + r * 0.42f, cy + r * 0.17f),
        size = Size(r * 0.28f, r * 0.15f)
    )

    when (emotion) {
        MascotEmotionPreset.AUTO_DYNAMIC -> return false

        MascotEmotionPreset.HAPPY_STAR_EYES -> {
            for (ex in listOf(leftEyeX, rightEyeX)) {
                drawOval(Color(0xFF0F172A), Offset(ex - eyeW * 0.55f, eyeY - fullEyeH * 0.52f), Size(eyeW * 1.1f, fullEyeH * 1.04f))
                // 4-point Golden Star Pupil
                val starR = eyeW * 0.45f
                val starPath = Path().apply {
                    moveTo(ex, eyeY - starR)
                    lineTo(ex + starR * 0.32f, eyeY - starR * 0.32f)
                    lineTo(ex + starR, eyeY)
                    lineTo(ex + starR * 0.32f, eyeY + starR * 0.32f)
                    lineTo(ex, eyeY + starR)
                    lineTo(ex - starR * 0.32f, eyeY + starR * 0.32f)
                    lineTo(ex - starR, eyeY)
                    lineTo(ex - starR * 0.32f, eyeY - starR * 0.32f)
                    close()
                }
                drawPath(starPath, color = Color(0xFFFACC15))
            }
            val openMouth = Path().apply {
                moveTo(cx - r * 0.22f, mouthY - r * 0.02f)
                quadraticTo(cx, mouthY + r * 0.28f, cx + r * 0.22f, mouthY - r * 0.02f)
                close()
            }
            drawPath(openMouth, color = Color(0xFF0F172A))
            drawCircle(Color(0xFFF43F5E), radius = r * 0.09f, center = Offset(cx, mouthY + r * 0.11f))
        }

        MascotEmotionPreset.LOVE_BUCIN -> {
            val pulse = 1f + sin(timeSec * 8f) * 0.15f
            for (ex in listOf(leftEyeX, rightEyeX)) {
                val hs = eyeW * 0.56f * pulse
                val heart = Path().apply {
                    moveTo(ex, eyeY + hs * 0.65f)
                    cubicTo(ex - hs * 1.3f, eyeY - hs * 0.15f, ex - hs * 0.65f, eyeY - hs * 1.15f, ex, eyeY - hs * 0.35f)
                    cubicTo(ex + hs * 0.65f, eyeY - hs * 1.15f, ex + hs * 1.3f, eyeY - hs * 0.15f, ex, eyeY + hs * 0.65f)
                    close()
                }
                drawPath(heart, color = Color(0xFFE11D48))
                drawPath(heart, color = Color(0xFFFCE7F3), style = Stroke(width = 1.8f))
            }
            val catMouth = Path().apply {
                moveTo(cx - r * 0.20f, mouthY + r * 0.04f)
                quadraticTo(cx - r * 0.10f, mouthY + r * 0.16f, cx, mouthY + r * 0.04f)
                quadraticTo(cx + r * 0.10f, mouthY + r * 0.16f, cx + r * 0.20f, mouthY + r * 0.04f)
            }
            drawPath(catMouth, color = Color(0xFF0F172A), style = Stroke(width = strokeBold, cap = StrokeCap.Round))
        }

        MascotEmotionPreset.LAUGHING_TEARS -> {
            // > < Laughing eyes + joy tear drops
            val leftLaugh = Path().apply {
                moveTo(leftEyeX - eyeW * 0.5f, eyeY - eyeH * 0.3f)
                lineTo(leftEyeX + eyeW * 0.35f, eyeY)
                lineTo(leftEyeX - eyeW * 0.5f, eyeY + eyeH * 0.3f)
            }
            val rightLaugh = Path().apply {
                moveTo(rightEyeX + eyeW * 0.5f, eyeY - eyeH * 0.3f)
                lineTo(rightEyeX - eyeW * 0.35f, eyeY)
                lineTo(rightEyeX + eyeW * 0.5f, eyeY + eyeH * 0.3f)
            }
            drawPath(leftLaugh, color = Color(0xFF0F172A), style = Stroke(width = strokeBold, cap = StrokeCap.Round))
            drawPath(rightLaugh, color = Color(0xFF0F172A), style = Stroke(width = strokeBold, cap = StrokeCap.Round))
            drawCircle(Color(0xFF38BDF8), radius = r * 0.075f, center = Offset(leftEyeX - eyeW * 0.55f, eyeY + r * 0.08f))
            drawCircle(Color(0xFF38BDF8), radius = r * 0.075f, center = Offset(rightEyeX + eyeW * 0.55f, eyeY + r * 0.08f))
            val bigLaugh = Path().apply {
                moveTo(cx - r * 0.25f, mouthY - r * 0.02f)
                quadraticTo(cx, mouthY + r * 0.32f, cx + r * 0.25f, mouthY - r * 0.02f)
                close()
            }
            drawPath(bigLaugh, color = Color(0xFF0F172A))
            drawCircle(Color(0xFFF43F5E), radius = r * 0.10f, center = Offset(cx, mouthY + r * 0.13f))
        }

        MascotEmotionPreset.COOL_BOSS_SMIRK -> {
            for (ex in listOf(leftEyeX, rightEyeX)) {
                drawOval(Color(0xFF0F172A), Offset(ex - eyeW * 0.5f, eyeY - fullEyeH * 0.22f), Size(eyeW, fullEyeH * 0.55f))
                drawLine(Color(0xFF0F172A), Offset(ex - eyeW * 0.6f, eyeY - fullEyeH * 0.22f), Offset(ex + eyeW * 0.6f, eyeY - fullEyeH * 0.22f), strokeWidth = strokeBold, cap = StrokeCap.Round)
                drawCircle(Color.White, radius = eyeW * 0.18f, center = Offset(ex - eyeW * 0.1f, eyeY))
            }
            val smirk = Path().apply {
                moveTo(cx - r * 0.16f, mouthY + r * 0.08f)
                quadraticTo(cx + r * 0.06f, mouthY + r * 0.12f, cx + r * 0.22f, mouthY - r * 0.04f)
            }
            drawPath(smirk, color = Color(0xFF0F172A), style = Stroke(width = strokeBold, cap = StrokeCap.Round))
        }

        MascotEmotionPreset.CRYING_FOUNTAIN -> {
            for (side in listOf(-1f to leftEyeX, 1f to rightEyeX)) {
                val ex = side.second
                drawLine(Color(0xFF0F172A), Offset(ex - eyeW * 0.5f, eyeY), Offset(ex + eyeW * 0.5f, eyeY), strokeWidth = strokeBold, cap = StrokeCap.Round)
                val stream = Path().apply {
                    moveTo(ex, eyeY)
                    quadraticTo(ex + side.first * r * 0.45f, eyeY - r * 0.45f, ex + side.first * r * 0.65f, cy + r * 0.75f)
                }
                drawPath(stream, color = Color(0xFF38BDF8), style = Stroke(width = (r * 0.11f).coerceAtLeast(3.5f), cap = StrokeCap.Round))
            }
            val wail = Path().apply {
                moveTo(cx - r * 0.22f, mouthY + r * 0.16f)
                quadraticTo(cx, mouthY - r * 0.14f, cx + r * 0.22f, mouthY + r * 0.16f)
                close()
            }
            drawPath(wail, color = Color(0xFF0F172A))
        }

        MascotEmotionPreset.RAGE_INFERNO -> {
            for (side in listOf(-1f to leftEyeX, 1f to rightEyeX)) {
                val ex = side.second
                drawOval(Color(0xFF450A0A), Offset(ex - eyeW * 0.5f, eyeY - fullEyeH * 0.42f), Size(eyeW, fullEyeH * 0.84f))
                drawCircle(Color(0xFFFACC15), radius = eyeW * 0.26f, center = Offset(ex, eyeY))
                drawLine(
                    color = Color(0xFF450A0A),
                    start = Offset(ex - side.first * eyeW * 0.55f, eyeY - fullEyeH * 0.20f),
                    end = Offset(ex + side.first * eyeW * 0.65f, eyeY - fullEyeH * 0.62f),
                    strokeWidth = strokeBold * 1.2f,
                    cap = StrokeCap.Round
                )
            }
            val snarl = Path().apply {
                moveTo(cx - r * 0.22f, mouthY + r * 0.14f)
                quadraticTo(cx, mouthY - r * 0.12f, cx + r * 0.22f, mouthY + r * 0.14f)
            }
            drawPath(snarl, color = Color(0xFF450A0A), style = Stroke(width = strokeBold, cap = StrokeCap.Round))
        }

        MascotEmotionPreset.SHOCKED_JOLT -> {
            for (ex in listOf(leftEyeX, rightEyeX)) {
                drawCircle(Color.White, radius = eyeW * 0.68f, center = Offset(ex, eyeY))
                drawCircle(Color(0xFF0F172A), radius = eyeW * 0.68f, center = Offset(ex, eyeY), style = Stroke(width = 2.2f))
                drawCircle(Color(0xFF0F172A), radius = eyeW * 0.20f, center = Offset(ex, eyeY))
            }
            drawOval(Color(0xFF0F172A), Offset(cx - r * 0.12f, mouthY - r * 0.04f), Size(r * 0.24f, r * 0.30f))
        }

        MascotEmotionPreset.SHY_BLUSH -> {
            for (ex in listOf(leftEyeX, rightEyeX)) {
                drawOval(Color(0xFF0F172A), Offset(ex - eyeW * 0.48f, eyeY - eyeH * 0.48f), Size(eyeW * 0.96f, eyeH * 0.96f))
                drawCircle(Color.White, radius = eyeW * 0.24f, center = Offset(ex - eyeW * 0.12f, eyeY - eyeH * 0.18f))
            }
            val catMouth = Path().apply {
                moveTo(cx - r * 0.18f, mouthY + r * 0.04f)
                quadraticTo(cx - r * 0.09f, mouthY + r * 0.15f, cx, mouthY + r * 0.04f)
                quadraticTo(cx + r * 0.09f, mouthY + r * 0.15f, cx + r * 0.18f, mouthY + r * 0.04f)
            }
            drawPath(catMouth, color = Color(0xFF0F172A), style = Stroke(width = strokeBold, cap = StrokeCap.Round))
        }

        MascotEmotionPreset.SLEEPY_SNOT_BUBBLE -> {
            for (ex in listOf(leftEyeX, rightEyeX)) {
                val arc = Path().apply {
                    moveTo(ex - eyeW * 0.5f, eyeY)
                    quadraticTo(ex, eyeY + fullEyeH * 0.38f, ex + eyeW * 0.5f, eyeY)
                }
                drawPath(arc, color = Color(0xFF0F172A), style = Stroke(width = strokeBold, cap = StrokeCap.Round))
            }
            val bubbleR = r * (0.16f + (sin(timeSec * 3f) * 0.5f + 0.5f) * 0.12f)
            drawCircle(Color(0xFFBAE6FD).copy(alpha = 0.65f), radius = bubbleR, center = Offset(cx + r * 0.22f, mouthY - r * 0.04f))
            drawCircle(Color(0xFF38BDF8), radius = bubbleR, center = Offset(cx + r * 0.22f, mouthY - r * 0.04f), style = Stroke(width = 1.8f))
        }

        MascotEmotionPreset.DIZZY_SPIRAL -> {
            for (ex in listOf(leftEyeX, rightEyeX)) {
                rotate(degrees = (timeSec * 180f) % 360f, pivot = Offset(ex, eyeY)) {
                    drawCircle(Color(0xFF0F172A), radius = eyeW * 0.55f, center = Offset(ex, eyeY), style = Stroke(width = 2.2f))
                    drawArc(
                        color = Color(0xFFA855F7),
                        startAngle = 0f,
                        sweepAngle = 270f,
                        useCenter = false,
                        topLeft = Offset(ex - eyeW * 0.35f, eyeY - eyeW * 0.35f),
                        size = Size(eyeW * 0.70f, eyeW * 0.70f),
                        style = Stroke(width = 2.4f, cap = StrokeCap.Round)
                    )
                }
            }
            val wavy = Path().apply {
                moveTo(cx - r * 0.20f, mouthY + r * 0.06f)
                quadraticTo(cx - r * 0.10f, mouthY - r * 0.04f, cx, mouthY + r * 0.06f)
                quadraticTo(cx + r * 0.10f, mouthY + r * 0.16f, cx + r * 0.20f, mouthY + r * 0.06f)
            }
            drawPath(wavy, color = Color(0xFF0F172A), style = Stroke(width = strokeBold, cap = StrokeCap.Round))
        }

        MascotEmotionPreset.MONEY_SULTAN -> {
            for (ex in listOf(leftEyeX, rightEyeX)) {
                drawCircle(Color(0xFFB45309), radius = eyeW * 0.58f, center = Offset(ex, eyeY))
                drawCircle(Color(0xFFFACC15), radius = eyeW * 0.52f, center = Offset(ex, eyeY))
                drawLine(Color(0xFF78350F), Offset(ex, eyeY - eyeW * 0.36f), Offset(ex, eyeY + eyeW * 0.36f), strokeWidth = 2.5f)
                drawCircle(Color(0xFF78350F), radius = eyeW * 0.20f, center = Offset(ex, eyeY), style = Stroke(width = 2.2f))
            }
            val grin = Path().apply {
                moveTo(cx - r * 0.22f, mouthY)
                quadraticTo(cx, mouthY + r * 0.24f, cx + r * 0.22f, mouthY)
                close()
            }
            drawPath(grin, color = Color(0xFF0F172A))
        }

        MascotEmotionPreset.SUSPICIOUS_SQUINT -> {
            for (ex in listOf(leftEyeX, rightEyeX)) {
                drawLine(Color(0xFF0F172A), Offset(ex - eyeW * 0.55f, eyeY), Offset(ex + eyeW * 0.55f, eyeY), strokeWidth = strokeBold * 1.2f, cap = StrokeCap.Round)
                drawCircle(Color(0xFF0EA5E9), radius = eyeW * 0.18f, center = Offset(ex + eyeW * 0.15f, eyeY + r * 0.03f))
            }
            drawLine(Color(0xFF0F172A), Offset(cx - r * 0.14f, mouthY + r * 0.06f), Offset(cx + r * 0.14f, mouthY + r * 0.04f), strokeWidth = strokeBold, cap = StrokeCap.Round)
        }

        MascotEmotionPreset.MIND_BLOWN_COSMIC -> {
            for (ex in listOf(leftEyeX, rightEyeX)) {
                drawCircle(Color(0xFF311068), radius = eyeW * 0.60f, center = Offset(ex, eyeY))
                drawCircle(Color(0xFF00F2FE), radius = eyeW * 0.32f, center = Offset(ex, eyeY))
                drawCircle(Color.White, radius = eyeW * 0.14f, center = Offset(ex, eyeY))
            }
            drawCircle(Color(0xFF0F172A), radius = r * 0.11f, center = Offset(cx, mouthY + r * 0.06f))
        }

        MascotEmotionPreset.ANGELIC_PUPPY -> {
            for (ex in listOf(leftEyeX, rightEyeX)) {
                drawCircle(Color(0xFF0F172A), radius = eyeW * 0.62f, center = Offset(ex, eyeY))
                drawCircle(Color.White, radius = eyeW * 0.26f, center = Offset(ex - eyeW * 0.14f, eyeY - eyeW * 0.16f))
                drawCircle(Color.White, radius = eyeW * 0.14f, center = Offset(ex + eyeW * 0.18f, eyeY + eyeW * 0.16f))
                drawCircle(Color(0xFF38BDF8), radius = eyeW * 0.10f, center = Offset(ex - eyeW * 0.16f, eyeY + eyeW * 0.18f))
            }
            val tinySmile = Path().apply {
                moveTo(cx - r * 0.12f, mouthY + r * 0.04f)
                quadraticTo(cx, mouthY + r * 0.14f, cx + r * 0.12f, mouthY + r * 0.04f)
            }
            drawPath(tinySmile, color = Color(0xFF0F172A), style = Stroke(width = strokeBold, cap = StrokeCap.Round))
        }

        MascotEmotionPreset.DEVILISH_PRANK -> {
            for (side in listOf(-1f to leftEyeX, 1f to rightEyeX)) {
                val ex = side.second
                drawOval(Color(0xFF0F172A), Offset(ex - eyeW * 0.5f, eyeY - fullEyeH * 0.35f), Size(eyeW, fullEyeH * 0.70f))
                drawCircle(Color(0xFFEF4444), radius = eyeW * 0.22f, center = Offset(ex, eyeY))
            }
            val grin = Path().apply {
                moveTo(cx - r * 0.24f, mouthY)
                quadraticTo(cx, mouthY + r * 0.22f, cx + r * 0.24f, mouthY)
                close()
            }
            drawPath(grin, color = Color(0xFF0F172A))
            // Two little white fangs
            drawCircle(Color.White, radius = r * 0.038f, center = Offset(cx - r * 0.12f, mouthY + r * 0.03f))
            drawCircle(Color.White, radius = r * 0.038f, center = Offset(cx + r * 0.12f, mouthY + r * 0.03f))
        }

        MascotEmotionPreset.FREEZING_SHIVER -> {
            for (ex in listOf(leftEyeX, rightEyeX)) {
                drawOval(Color(0xFF0F172A), Offset(ex - eyeW * 0.45f, eyeY - eyeH * 0.45f), Size(eyeW * 0.9f, eyeH * 0.9f))
                drawCircle(Color(0xFFE0F2FE), radius = eyeW * 0.22f, center = Offset(ex, eyeY - eyeH * 0.12f))
            }
            // Chattering teeth rectangle
            drawRoundRect(
                color = Color.White,
                topLeft = Offset(cx - r * 0.20f, mouthY),
                size = Size(r * 0.40f, r * 0.14f),
                cornerRadius = CornerRadius(r * 0.04f, r * 0.04f)
            )
            drawRoundRect(
                color = Color(0xFF0F172A),
                topLeft = Offset(cx - r * 0.20f, mouthY),
                size = Size(r * 0.40f, r * 0.14f),
                cornerRadius = CornerRadius(r * 0.04f, r * 0.04f),
                style = Stroke(width = 2f)
            )
        }

        MascotEmotionPreset.FEVER_OVERHEAT -> {
            for (ex in listOf(leftEyeX, rightEyeX)) {
                drawOval(Color(0xFF0F172A), Offset(ex - eyeW * 0.45f, eyeY - eyeH * 0.35f), Size(eyeW * 0.9f, eyeH * 0.7f))
            }
            // Thermometer sticking out of mouth
            drawLine(
                color = Color.White,
                start = Offset(cx, mouthY + r * 0.06f),
                end = Offset(cx + r * 0.36f, mouthY - r * 0.14f),
                strokeWidth = (r * 0.06f).coerceAtLeast(2.6f),
                cap = StrokeCap.Round
            )
            drawCircle(Color(0xFFEF4444), radius = r * 0.06f, center = Offset(cx + r * 0.36f, mouthY - r * 0.14f))
        }

        MascotEmotionPreset.CYBER_HUD_FOCUS -> {
            for (ex in listOf(leftEyeX, rightEyeX)) {
                drawOval(Color(0xFF090D16), Offset(ex - eyeW * 0.52f, eyeY - fullEyeH * 0.45f), Size(eyeW * 1.04f, fullEyeH * 0.9f))
                drawCircle(Color(0xFF10B981), radius = eyeW * 0.34f, center = Offset(ex, eyeY), style = Stroke(width = 2f))
                drawCircle(Color(0xFF00F2FE), radius = eyeW * 0.12f, center = Offset(ex, eyeY))
            }
            drawLine(Color(0xFF0F172A), Offset(cx - r * 0.15f, mouthY + r * 0.05f), Offset(cx + r * 0.15f, mouthY + r * 0.05f), strokeWidth = strokeBold, cap = StrokeCap.Round)
        }

        MascotEmotionPreset.YUMMY_DROOL -> {
            for (ex in listOf(leftEyeX, rightEyeX)) {
                val happyArc = Path().apply {
                    moveTo(ex - eyeW * 0.5f, eyeY + eyeH * 0.1f)
                    quadraticTo(ex, eyeY - fullEyeH * 0.45f, ex + eyeW * 0.5f, eyeY + eyeH * 0.1f)
                }
                drawPath(happyArc, color = Color(0xFF0F172A), style = Stroke(width = strokeBold, cap = StrokeCap.Round))
            }
            val smile = Path().apply {
                moveTo(cx - r * 0.18f, mouthY)
                quadraticTo(cx, mouthY + r * 0.20f, cx + r * 0.18f, mouthY)
            }
            drawPath(smile, color = Color(0xFF0F172A), style = Stroke(width = strokeBold, cap = StrokeCap.Round))
            drawOval(Color(0xFF38BDF8), Offset(cx + r * 0.10f, mouthY + r * 0.06f), Size(r * 0.07f, r * 0.14f))
        }
    }
    return true
}

// ============================================================================
// 7. NEW INTERACTIVE POSE PROPS RENDERER (BOBA, GAMEPAD, KATANA, BARBELL, ETC.)
// ============================================================================

internal fun DrawScope.drawMascotExtraInteractivePoses(
    cx: Float,
    cy: Float,
    r: Float,
    timeSec: Float,
    leftHandCenter: Offset,
    rightHandCenter: Offset,
    activityPose: MascotActivityPose
) {
    when (activityPose) {
        MascotActivityPose.PETTING_BLISS -> {
            // Soft floating hand petting top of mascot's head + hearts
            val petX = cx + sin(timeSec * 6f) * (r * 0.28f)
            val petY = cy - r * 0.96f
            drawRoundRect(
                color = Color(0xFFFDE68A),
                topLeft = Offset(petX - r * 0.24f, petY - r * 0.12f),
                size = Size(r * 0.48f, r * 0.24f),
                cornerRadius = CornerRadius(r * 0.12f, r * 0.12f)
            )
            drawCircle(color = Color(0xFFF43F5E), radius = r * 0.10f, center = Offset(cx - r * 0.65f, petY - r * 0.15f))
            drawCircle(color = Color(0xFFEC4899), radius = r * 0.08f, center = Offset(cx + r * 0.65f, petY - r * 0.10f))
        }

        MascotActivityPose.EATING_BOBA_SNACK -> {
            // Boba Milk Tea Cup with Tapioca Pearls & Straw in Right Hand + Pink Donut in Left Hand
            val bx = rightHandCenter.x - r * 0.18f
            val by = rightHandCenter.y - r * 0.26f
            drawLine(
                color = Color(0xFFEC4899),
                start = Offset(bx + r * 0.18f, by - r * 0.18f),
                end = Offset(bx + r * 0.18f, by + r * 0.20f),
                strokeWidth = (r * 0.055f).coerceAtLeast(2.4f),
                cap = StrokeCap.Round
            )
            drawRoundRect(
                color = Color(0xFFFDE68A),
                topLeft = Offset(bx, by),
                size = Size(r * 0.36f, r * 0.46f),
                cornerRadius = CornerRadius(r * 0.06f, r * 0.06f)
            )
            for (p in 0..2) {
                drawCircle(Color(0xFF451A03), radius = r * 0.038f, center = Offset(bx + r * (0.09f + p * 0.09f), by + r * 0.38f))
            }
            // Pink frosted donut in left hand
            drawCircle(Color(0xFFD97706), radius = r * 0.20f, center = leftHandCenter)
            drawCircle(Color(0xFFF472B6), radius = r * 0.16f, center = leftHandCenter)
            drawCircle(Color(0xFF0F172A), radius = r * 0.06f, center = leftHandCenter)
        }

        MascotActivityPose.GAMING_PRO_PAD -> {
            val gx = cx - r * 0.46f
            val gy = cy + r * 0.26f + sin(timeSec * 10f) * (r * 0.03f)
            drawRoundRect(
                color = Color(0xFF1E293B),
                topLeft = Offset(gx, gy),
                size = Size(r * 0.92f, r * 0.42f),
                cornerRadius = CornerRadius(r * 0.16f, r * 0.16f)
            )
            drawRoundRect(
                color = Color(0xFF00F2FE),
                topLeft = Offset(gx, gy),
                size = Size(r * 0.92f, r * 0.42f),
                cornerRadius = CornerRadius(r * 0.16f, r * 0.16f),
                style = Stroke(width = 2.2f)
            )
            drawCircle(Color(0xFFFACC15), radius = r * 0.06f, center = Offset(gx + r * 0.22f, gy + r * 0.21f))
            drawCircle(Color(0xFFEF4444), radius = r * 0.05f, center = Offset(gx + r * 0.68f, gy + r * 0.16f))
            drawCircle(Color(0xFF22C55E), radius = r * 0.05f, center = Offset(gx + r * 0.76f, gy + r * 0.24f))
        }

        MascotActivityPose.KATANA_SLASH -> {
            val sx = rightHandCenter.x
            val sy = rightHandCenter.y
            val slashAngle = sin(timeSec * 6f) * 28f - 35f
            rotate(degrees = slashAngle, pivot = Offset(sx, sy)) {
                drawLine(
                    brush = Brush.verticalGradient(listOf(Color.White, Color(0xFF00F2FE), Color(0xFFEF4444))),
                    start = Offset(sx, sy),
                    end = Offset(sx + r * 0.15f, sy - r * 1.15f),
                    strokeWidth = (r * 0.075f).coerceAtLeast(3.2f),
                    cap = StrokeCap.Round
                )
                drawRoundRect(
                    color = Color(0xFFFACC15),
                    topLeft = Offset(sx - r * 0.14f, sy - r * 0.05f),
                    size = Size(r * 0.28f, r * 0.09f),
                    cornerRadius = CornerRadius(4f, 4f)
                )
            }
        }

        MascotActivityPose.ARCANE_SPELLCAST -> {
            val sx = rightHandCenter.x + r * 0.08f
            val sy = rightHandCenter.y
            drawLine(
                color = Color(0xFF78350F),
                start = Offset(sx, sy + r * 0.45f),
                end = Offset(sx, sy - r * 0.65f),
                strokeWidth = (r * 0.06f).coerceAtLeast(2.6f),
                cap = StrokeCap.Round
            )
            drawCircle(Color(0xFFA855F7).copy(alpha = 0.45f), radius = r * 0.24f, center = Offset(sx, sy - r * 0.72f))
            drawCircle(Color(0xFF00F2FE), radius = r * 0.12f, center = Offset(sx, sy - r * 0.72f))
        }

        MascotActivityPose.GYM_DUMBBELL_LIFT -> {
            val liftY = cy - r * 0.92f - abs(sin(timeSec * 4.5f)) * (r * 0.22f)
            drawLine(
                color = Color(0xFFCBD5E1),
                start = Offset(cx - r * 0.85f, liftY),
                end = Offset(cx + r * 0.85f, liftY),
                strokeWidth = (r * 0.065f).coerceAtLeast(2.8f),
                cap = StrokeCap.Round
            )
            for (side in listOf(-1f, 1f)) {
                drawRoundRect(
                    color = Color(0xFFFACC15),
                    topLeft = Offset(cx + side * r * 0.76f - r * 0.12f, liftY - r * 0.22f),
                    size = Size(r * 0.24f, r * 0.44f),
                    cornerRadius = CornerRadius(r * 0.06f, r * 0.06f)
                )
            }
        }

        MascotActivityPose.COFFEE_CHILL -> {
            val mx = rightHandCenter.x - r * 0.16f
            val my = rightHandCenter.y - r * 0.16f
            drawRoundRect(
                color = Color.White,
                topLeft = Offset(mx, my),
                size = Size(r * 0.34f, r * 0.36f),
                cornerRadius = CornerRadius(r * 0.06f, r * 0.06f)
            )
            drawRoundRect(
                color = Color(0xFF78350F),
                topLeft = Offset(mx + r * 0.04f, my + r * 0.06f),
                size = Size(r * 0.26f, r * 0.10f),
                cornerRadius = CornerRadius(r * 0.03f, r * 0.03f)
            )
        }

        MascotActivityPose.BUBBLE_BATH_DUCK -> {
            // Cute yellow rubber duck on left + floating bubbles
            val dx = leftHandCenter.x
            val dy = leftHandCenter.y - r * 0.08f
            drawCircle(Color(0xFFFACC15), radius = r * 0.16f, center = Offset(dx, dy))
            drawCircle(Color(0xFFFACC15), radius = r * 0.11f, center = Offset(dx + r * 0.08f, dy - r * 0.14f))
            drawCircle(Color(0xFFF97316), radius = r * 0.045f, center = Offset(dx + r * 0.18f, dy - r * 0.14f))
            for (b in 0..3) {
                val bx = cx + (b - 1.5f) * (r * 0.45f)
                val by = cy - r * 0.55f - ((timeSec * 30f + b * 18f) % (r * 0.55f))
                drawCircle(Color(0xFFE0F2FE).copy(alpha = 0.65f), radius = r * 0.11f, center = Offset(bx, by))
                drawCircle(Color.White, radius = r * 0.11f, center = Offset(bx, by), style = Stroke(width = 1.6f))
            }
        }

        MascotActivityPose.SELFIE_FLASH_POSE -> {
            val px = rightHandCenter.x - r * 0.12f
            val py = rightHandCenter.y - r * 0.22f
            drawRoundRect(
                color = Color(0xFF0F172A),
                topLeft = Offset(px, py),
                size = Size(r * 0.26f, r * 0.42f),
                cornerRadius = CornerRadius(r * 0.05f, r * 0.05f)
            )
            val flashAlpha = (sin(timeSec * 6f) * 0.5f + 0.5f)
            drawCircle(Color.White.copy(alpha = flashAlpha), radius = r * (0.14f + flashAlpha * 0.20f), center = Offset(px + r * 0.13f, py + r * 0.10f))
        }

        else -> Unit
    }
}

// ============================================================================
// 8. COLORFUL DUOLINGO 3D & SKEUOMORPHIC INTERACTIVE MASCOT ROOM DIORAMA
// (WALL + FLOOR + WINDOW/DECOR + CLOSET + DESK/PC + BED/SOFA + RUG + LAMP + PET)
// ============================================================================

internal fun DrawScope.drawModularMascotRoomCanvas(
    wallStyle: MascotRoomWallStyle,
    floorStyle: MascotRoomFloorStyle,
    closetStyle: MascotRoomClosetStyle,
    lampStyle: MascotRoomLampStyle,
    rugStyle: MascotRoomRugStyle,
    deskStyle: MascotRoomDeskStyle = AppThemeController.roomDeskStyle,
    bedSofaStyle: MascotRoomBedSofaStyle = AppThemeController.roomBedSofaStyle,
    windowDecorStyle: MascotRoomWindowDecorStyle = AppThemeController.roomWindowDecorStyle,
    petStyle: MascotRoomPetStyle = AppThemeController.roomPetStyle
) {
    val w = size.width
    val h = size.height
    val floorTop = h * 0.63f
    val leftCornerX = w * 0.16f
    val rightCornerX = w * 0.84f

    // ------------------------------------------------------------------------
    // 1. 3D SKEUOMORPHIC DIORAMA WALLS WITH 5 DISTINCT ARCHITECTURAL STYLES
    // ------------------------------------------------------------------------
    val wallLight = AppThemeController.lightenColor(wallStyle.topColor, 0.18f)
    val wallMid = wallStyle.topColor
    val wallDeep = wallStyle.bottomColor
    val wallShadow = AppThemeController.darkenColor(wallStyle.bottomColor, 0.74f)

    // Center Back Wall
    drawRect(
        brush = Brush.verticalGradient(listOf(wallLight, wallMid, wallDeep)),
        topLeft = Offset(leftCornerX, 0f),
        size = Size(rightCornerX - leftCornerX, floorTop)
    )
    // Left Perspective Side Wall
    val leftWallPath = Path().apply {
        moveTo(0f, 0f)
        lineTo(leftCornerX, h * 0.05f)
        lineTo(leftCornerX, floorTop)
        lineTo(0f, floorTop + h * 0.10f)
        close()
    }
    drawPath(
        path = leftWallPath,
        brush = Brush.verticalGradient(
            listOf(AppThemeController.lightenColor(wallStyle.topColor, 0.26f), wallStyle.bottomColor)
        )
    )
    // Right Perspective Side Wall
    val rightWallPath = Path().apply {
        moveTo(rightCornerX, h * 0.05f)
        lineTo(w, 0f)
        lineTo(w, floorTop + h * 0.10f)
        lineTo(rightCornerX, floorTop)
        close()
    }
    drawPath(
        path = rightWallPath,
        brush = Brush.verticalGradient(listOf(wallStyle.topColor, wallShadow))
    )

    val dadoY = floorTop * 0.68f
    when (wallStyle.ordinal % 5) {
        0 -> {
            // Style 0: Royal Striped Wallpaper + Wood Wainscoting Panels
            for (i in 0..6) {
                val sx = leftCornerX + (rightCornerX - leftCornerX) * (i / 6f)
                drawLine(Color.White.copy(alpha = 0.16f), Offset(sx, h * 0.05f), Offset(sx, floorTop), strokeWidth = 2.5f)
            }
            drawRect(
                color = AppThemeController.darkenColor(wallStyle.bottomColor, 0.82f),
                topLeft = Offset(leftCornerX, dadoY),
                size = Size(rightCornerX - leftCornerX, floorTop - dadoY)
            )
            drawLine(wallStyle.accentColor, Offset(leftCornerX, dadoY), Offset(rightCornerX, dadoY), strokeWidth = 3.5f)
        }
        1 -> {
            // Style 1: Cyberpunk Hexagonal Armor Plates & Neon Circuit Traces
            for (rIdx in 0..2) {
                val ry = h * (0.14f + rIdx * 0.17f)
                drawLine(wallStyle.accentColor.copy(alpha = 0.35f), Offset(leftCornerX, ry), Offset(rightCornerX, ry), strokeWidth = 1.8f)
                for (cIdx in 0..4) {
                    val cxNode = leftCornerX + (rightCornerX - leftCornerX) * ((cIdx + 0.5f) / 5f)
                    drawCircle(wallStyle.accentColor, radius = 3f, center = Offset(cxNode, ry))
                }
            }
        }
        2 -> {
            // Style 2: Japanese Kumiko Shoji Lattice Grid & Timber Beams
            for (v in 1..5) {
                val vx = leftCornerX + (rightCornerX - leftCornerX) * (v / 6f)
                drawLine(wallStyle.accentColor.copy(alpha = 0.55f), Offset(vx, h * 0.05f), Offset(vx, floorTop), strokeWidth = 3f)
            }
            for (hBar in 1..3) {
                val hy = floorTop * (hBar / 4f)
                drawLine(wallStyle.accentColor.copy(alpha = 0.55f), Offset(leftCornerX, hy), Offset(rightCornerX, hy), strokeWidth = 2.5f)
            }
        }
        3 -> {
            // Style 3: Medieval / Castle Staggered Stone Masonry Bricks
            val rows = 5
            val rowH = floorTop / rows
            for (rIdx in 0 until rows) {
                val ry = rIdx * rowH
                drawLine(Color.Black.copy(alpha = 0.25f), Offset(leftCornerX, ry), Offset(rightCornerX, ry), strokeWidth = 2f)
                val offsetShift = if (rIdx % 2 == 0) 0f else 0.5f
                for (bIdx in 1..4) {
                    val bx = leftCornerX + (rightCornerX - leftCornerX) * ((bIdx + offsetShift) / 5f)
                    if (bx < rightCornerX) {
                        drawLine(Color.Black.copy(alpha = 0.25f), Offset(bx, ry), Offset(bx, ry + rowH), strokeWidth = 2f)
                    }
                }
            }
        }
        else -> {
            // Style 4: Kawaii Scalloped Cloud & Starry Tufted Padded Wall
            for (rIdx in 0..2) {
                for (cIdx in 0..4) {
                    val sx = leftCornerX + (rightCornerX - leftCornerX) * ((cIdx + 0.5f) / 5f)
                    val sy = h * (0.14f + rIdx * 0.16f)
                    drawCircle(wallStyle.accentColor.copy(alpha = 0.55f), radius = 3.2f, center = Offset(sx, sy))
                }
            }
            for (sc in 0..7) {
                val scX = leftCornerX + (rightCornerX - leftCornerX) * (sc / 8f)
                val scW = (rightCornerX - leftCornerX) / 8f
                drawOval(wallStyle.accentColor.copy(alpha = 0.40f), Offset(scX, dadoY - 6f), Size(scW, floorTop - dadoY + 10f))
            }
        }
    }

    // 3D Corner Pillars
    drawLine(color = Color.Black.copy(alpha = 0.24f), start = Offset(leftCornerX, h * 0.05f), end = Offset(leftCornerX, floorTop), strokeWidth = 3f)
    drawLine(color = Color.Black.copy(alpha = 0.24f), start = Offset(rightCornerX, h * 0.05f), end = Offset(rightCornerX, floorTop), strokeWidth = 3f)

    // Colorful Party Bunting Flags at Top Left of Back Wall
    val buntingColors = listOf(Color(0xFF58CC02), Color(0xFFFFC800), Color(0xFFFF4B4B), Color(0xFF1CB0F6), Color(0xFFCE82FF))
    for (b in 0..4) {
        val bx = leftCornerX + (w * 0.06f) + b * (w * 0.048f)
        val by = h * 0.07f + kotlin.math.sin(b * 0.8f) * 3f
        val flag = Path().apply {
            moveTo(bx, by)
            lineTo(bx + w * 0.036f, by)
            lineTo(bx + w * 0.018f, by + h * 0.065f)
            close()
        }
        drawPath(flag, color = buntingColors[b % buntingColors.size])
        drawPath(flag, color = Color.White.copy(alpha = 0.65f), style = Stroke(width = 1.2f))
    }

    // ------------------------------------------------------------------------
    // 2. 3D SKEUOMORPHIC FLOOR WITH 5 DISTINCT SURFACE GEOMETRIES
    // ------------------------------------------------------------------------
    val floorLight = AppThemeController.lightenColor(floorStyle.primaryColor, 0.16f)
    val floorMid = floorStyle.primaryColor
    val floorDark = AppThemeController.darkenColor(floorStyle.primaryColor, 0.72f)

    val floorPoly = Path().apply {
        moveTo(leftCornerX, floorTop)
        lineTo(rightCornerX, floorTop)
        lineTo(w, floorTop + h * 0.10f)
        lineTo(w, h)
        lineTo(0f, h)
        lineTo(0f, floorTop + h * 0.10f)
        close()
    }
    drawPath(
        path = floorPoly,
        brush = Brush.verticalGradient(colors = listOf(floorLight, floorMid, floorDark), startY = floorTop, endY = h)
    )

    when (floorStyle.ordinal % 5) {
        0 -> {
            // Floor 0: Perspective Hardwood Planks with Bevel Highlights
            for (i in 0..8) {
                val topX = leftCornerX + (rightCornerX - leftCornerX) * (i / 8f)
                val botX = (topX - w * 0.5f) * 1.65f + w * 0.5f
                drawLine(floorStyle.gridLineColor.copy(alpha = 0.55f), Offset(topX, floorTop), Offset(botX, h), strokeWidth = 2.2f)
                drawLine(Color.White.copy(alpha = 0.22f), Offset(topX + 2f, floorTop), Offset(botX + 2f, h), strokeWidth = 1.2f)
            }
            for (rIdx in 1..3) {
                val ry = floorTop + (h - floorTop) * (rIdx / 4f)
                drawLine(floorStyle.gridLineColor.copy(alpha = 0.38f), Offset(0f, ry), Offset(w, ry), strokeWidth = 1.6f)
            }
        }
        1 -> {
            // Floor 1: Grand Royal Checkered Marble Chessboard Tiles
            val rows = 3
            val cols = 8
            val rowH = (h - floorTop) / rows
            val colW = w / cols
            for (rIdx in 0 until rows) {
                for (cIdx in 0 until cols) {
                    if ((rIdx + cIdx) % 2 == 0) {
                        drawRect(
                            color = floorStyle.gridLineColor.copy(alpha = 0.35f),
                            topLeft = Offset(cIdx * colW, floorTop + rIdx * rowH),
                            size = Size(colW, rowH)
                        )
                    }
                }
            }
        }
        2 -> {
            // Floor 2: Sci-Fi Hexagonal Honeycomb Energy Deck
            for (rIdx in 0..2) {
                val ry = floorTop + (h - floorTop) * ((rIdx + 0.5f) / 3f)
                for (cIdx in 0..6) {
                    val rx = w * ((cIdx + (rIdx % 2) * 0.5f) / 7f)
                    drawOval(
                        color = floorStyle.gridLineColor.copy(alpha = 0.55f),
                        topLeft = Offset(rx - 14f, ry - 6f),
                        size = Size(28f, 12f),
                        style = Stroke(width = 1.8f)
                    )
                }
            }
        }
        3 -> {
            // Floor 3: Japanese Tatami Woven Straw Mats with Dark Cloth Borders
            val midY = floorTop + (h - floorTop) * 0.5f
            drawLine(floorStyle.gridLineColor, Offset(0f, midY), Offset(w, midY), strokeWidth = 4f)
            for (cIdx in 1..3) {
                val vx = w * (cIdx / 4f)
                drawLine(floorStyle.gridLineColor, Offset(vx, floorTop), Offset(vx, h), strokeWidth = 4f)
            }
        }
        else -> {
            // Floor 4: Zen Koi Pond / Lava / Cloud Ripples & Stepping Stones
            for (s in 0..3) {
                val sx = w * (0.20f + s * 0.20f)
                val sy = floorTop + (h - floorTop) * (0.35f + (s % 2) * 0.30f)
                drawOval(floorStyle.gridLineColor.copy(alpha = 0.55f), Offset(sx - 18f, sy - 8f), Size(36f, 16f), style = Stroke(width = 2f))
                drawOval(Color.White.copy(alpha = 0.30f), Offset(sx - 10f, sy - 4f), Size(20f, 8f))
            }
        }
    }

    // Chunky 3D Baseboard Molding along Wall-Floor Junction
    drawRoundRect(
        color = AppThemeController.darkenColor(wallStyle.accentColor, 0.65f),
        topLeft = Offset(leftCornerX - 2f, floorTop - 5f),
        size = Size(rightCornerX - leftCornerX + 4f, 8f),
        cornerRadius = CornerRadius(3f, 3f)
    )
    drawRoundRect(
        color = wallStyle.accentColor,
        topLeft = Offset(leftCornerX - 2f, floorTop - 7f),
        size = Size(rightCornerX - leftCornerX + 4f, 6f),
        cornerRadius = CornerRadius(3f, 3f)
    )

    // ------------------------------------------------------------------------
    // 3. 3D SKEUOMORPHIC WINDOW, AQUARIUM & TROPHY SHELF (UPPER RIGHT)
    // ------------------------------------------------------------------------
    if (windowDecorStyle != MascotRoomWindowDecorStyle.NONE) {
        val winX = w * 0.55f
        val winY = h * 0.12f
        val winW = w * 0.22f
        val winH = h * 0.34f
        val frameLip = AppThemeController.darkenColor(windowDecorStyle.accentColor, 0.62f)

        drawRoundRect(Color.Black.copy(alpha = 0.28f), Offset(winX + 3f, winY + 5f), Size(winW, winH), CornerRadius(12f, 12f))
        drawRoundRect(frameLip, Offset(winX - 3f, winY - 1f), Size(winW + 6f, winH + 6f), CornerRadius(12f, 12f))
        drawRoundRect(windowDecorStyle.accentColor, Offset(winX - 3f, winY - 3f), Size(winW + 6f, winH + 4f), CornerRadius(12f, 12f))
        drawRoundRect(
            brush = Brush.verticalGradient(
                listOf(
                    AppThemeController.lightenColor(windowDecorStyle.primaryColor, 0.25f),
                    windowDecorStyle.primaryColor,
                    AppThemeController.darkenColor(windowDecorStyle.primaryColor, 0.72f)
                )
            ),
            topLeft = Offset(winX + 2f, winY + 2f),
            size = Size(winW - 4f, winH - 4f),
            cornerRadius = CornerRadius(9f, 9f)
        )

        when (windowDecorStyle.ordinal % 4) {
            1 -> {
                // Coral Reef Aquarium with Clownfish & Bubbles
                drawRoundRect(Color(0xFFFDE047), Offset(winX + 3f, winY + winH - 10f), Size(winW - 6f, 7f), CornerRadius(4f, 4f))
                drawLine(Color(0xFFEC4899), Offset(winX + winW * 0.22f, winY + winH - 8f), Offset(winX + winW * 0.26f, winY + winH * 0.56f), strokeWidth = 4f, cap = StrokeCap.Round)
                drawLine(Color(0xFF58CC02), Offset(winX + winW * 0.76f, winY + winH - 8f), Offset(winX + winW * 0.72f, winY + winH * 0.52f), strokeWidth = 4f, cap = StrokeCap.Round)
                val f1x = winX + winW * 0.38f
                val f1y = winY + winH * 0.42f
                drawOval(Color(0xFFF97316), Offset(f1x - 6f, f1y - 4f), Size(12f, 8f))
                drawLine(Color.White, Offset(f1x, f1y - 4f), Offset(f1x, f1y + 4f), strokeWidth = 2f)
                drawOval(Color(0xFFFACC15), Offset(winX + winW * 0.62f, winY + winH * 0.58f), Size(10f, 7f))
                drawCircle(Color.White.copy(alpha = 0.85f), radius = 2.4f, center = Offset(winX + winW * 0.52f, winY + winH * 0.25f))
            }
            2 -> {
                // Multi-Tier Collector Shelf (Trophy Cup + Books + Katana)
                val shelf1Y = winY + winH * 0.48f
                val shelf2Y = winY + winH * 0.82f
                drawLine(windowDecorStyle.accentColor, Offset(winX + 3f, shelf1Y), Offset(winX + winW - 3f, shelf1Y), strokeWidth = 3f)
                drawLine(windowDecorStyle.accentColor, Offset(winX + 3f, shelf2Y), Offset(winX + winW - 3f, shelf2Y), strokeWidth = 3f)
                drawRoundRect(Color(0xFFFACC15), Offset(winX + winW * 0.20f, shelf1Y - 12f), Size(9f, 10f), CornerRadius(3f, 3f))
                drawCircle(Color(0xFF00F2FE), radius = 4.5f, center = Offset(winX + winW * 0.54f, shelf1Y - 6f))
                drawRoundRect(Color(0xFFEF4444), Offset(winX + winW * 0.74f, shelf1Y - 11f), Size(8f, 10f), CornerRadius(2f, 2f))
                val bookColors = listOf(Color(0xFF58CC02), Color(0xFFFF4B4B), Color(0xFFFFC800), Color(0xFF1CB0F6))
                for (bk in 0..3) {
                    drawRoundRect(bookColors[bk], Offset(winX + 5f + bk * 5.5f, shelf2Y - 12f), Size(4.5f, 11f), CornerRadius(1.5f, 1.5f))
                }
            }
            3 -> {
                // Cyber Crypto / Neon Vinyl Wall Board
                drawCircle(windowDecorStyle.accentColor, radius = winH * 0.26f, center = Offset(winX + winW * 0.35f, winY + winH * 0.50f))
                drawCircle(Color(0xFF0F172A), radius = winH * 0.09f, center = Offset(winX + winW * 0.35f, winY + winH * 0.50f))
                val chartPath = Path().apply {
                    moveTo(winX + winW * 0.55f, winY + winH * 0.72f)
                    lineTo(winX + winW * 0.68f, winY + winH * 0.48f)
                    lineTo(winX + winW * 0.78f, winY + winH * 0.58f)
                    lineTo(winX + winW * 0.90f, winY + winH * 0.25f)
                }
                drawPath(chartPath, color = Color(0xFF22C55E), style = Stroke(width = 2.5f, cap = StrokeCap.Round))
            }
            else -> {
                // Panoramic Arched Window with Ringed Planet/Sun, Clouds & Curtains
                drawCircle(Color(0xFFFFC800), radius = 6.5f, center = Offset(winX + winW * 0.68f, winY + winH * 0.28f))
                drawLine(Color(0xFFFDE047), Offset(winX + winW * 0.52f, winY + winH * 0.32f), Offset(winX + winW * 0.84f, winY + winH * 0.24f), strokeWidth = 2f)
                drawCircle(Color.White.copy(alpha = 0.92f), radius = 5.5f, center = Offset(winX + winW * 0.34f, winY + winH * 0.62f))
                drawCircle(Color.White.copy(alpha = 0.92f), radius = 4.5f, center = Offset(winX + winW * 0.44f, winY + winH * 0.64f))
                drawLine(windowDecorStyle.accentColor, Offset(winX + winW * 0.5f, winY + 2f), Offset(winX + winW * 0.5f, winY + winH - 2f), strokeWidth = 2f)
                drawLine(windowDecorStyle.accentColor, Offset(winX + 2f, winY + winH * 0.52f), Offset(winX + winW - 2f, winY + winH * 0.52f), strokeWidth = 2f)
            }
        }
        drawRoundRect(windowDecorStyle.accentColor, Offset(winX - 5f, winY + winH - 1f), Size(winW + 10f, 5f), CornerRadius(4f, 4f))
    }

    // ------------------------------------------------------------------------
    // 4. 3D WARDROBE CLOSET WITH 6 DISTINCT ARCHITECTURAL SILHOUETTES (LEFT)
    // ------------------------------------------------------------------------
    val closetX = w * 0.025f
    val closetY = h * 0.12f
    val closetW = w * 0.195f
    val closetH = h * 0.56f
    val closetSideDepth = w * 0.026f
    val closetDarkBody = AppThemeController.darkenColor(closetStyle.bodyColor, 0.68f)
    val closetLightBody = AppThemeController.lightenColor(closetStyle.bodyColor, 0.20f)
    val closetTrimDark = AppThemeController.darkenColor(closetStyle.trimColor, 0.66f)

    // Floor Contact Shadow under Closet
    drawOval(
        color = Color.Black.copy(alpha = 0.35f),
        topLeft = Offset(closetX - 2f, closetY + closetH - 4f),
        size = Size(closetW + closetSideDepth + 8f, 14f)
    )

    when (closetStyle.ordinal % 6) {
        0 -> {
            // STYLE 0: Royal Double-Door Armoire with Crown Roof, Hanging Clothes & 2 Drawers
            drawRoundRect(closetTrimDark, Offset(closetX + 4f, closetY + closetH - 3f), Size(8f, 8f), CornerRadius(3f, 3f))
            drawRoundRect(closetTrimDark, Offset(closetX + closetW - 10f, closetY + closetH - 3f), Size(8f, 8f), CornerRadius(3f, 3f))
            drawRoundRect(closetDarkBody, Offset(closetX + closetSideDepth, closetY + 4f), Size(closetW, closetH - 4f), CornerRadius(10f, 10f))
            drawRoundRect(
                brush = Brush.verticalGradient(listOf(closetLightBody, closetStyle.bodyColor)),
                topLeft = Offset(closetX, closetY),
                size = Size(closetW, closetH - 3f),
                cornerRadius = CornerRadius(10f, 10f)
            )
            drawRoundRect(closetStyle.trimColor, Offset(closetX, closetY), Size(closetW, closetH - 3f), CornerRadius(10f, 10f), style = Stroke(width = 2.6f))
            // Crown Molding Roof + Ruby Gem
            drawRoundRect(closetStyle.trimColor, Offset(closetX - 3f, closetY - 6f), Size(closetW + 6f, 7f), CornerRadius(5f, 5f))
            drawCircle(Color(0xFFFF4B4B), radius = 4f, center = Offset(closetX + closetW * 0.5f, closetY - 3f))
            // Hanging Clothes Showcase Window
            val innerX = closetX + 5f
            val innerY = closetY + 9f
            val innerW = closetW - 10f
            val innerH = closetH * 0.56f
            drawRoundRect(closetDarkBody, Offset(innerX, innerY), Size(innerW, innerH), CornerRadius(6f, 6f))
            drawLine(Color(0xFFFACC15), Offset(innerX + 3f, innerY + 6f), Offset(innerX + innerW - 3f, innerY + 6f), strokeWidth = 2.2f)
            val clothColors = listOf(Color(0xFF58CC02), Color(0xFF1CB0F6), Color(0xFFFF4B4B), Color(0xFFFFC800))
            for (cIdx in 0..3) {
                val cxCloth = innerX + 4f + cIdx * ((innerW - 8f) / 4f)
                val cw = ((innerW - 14f) / 4f).coerceAtLeast(4f)
                drawRoundRect(clothColors[cIdx], Offset(cxCloth, innerY + 9f), Size(cw, innerH * 0.54f), CornerRadius(3f, 3f))
            }
            drawLine(closetStyle.trimColor, Offset(innerX + innerW * 0.5f, innerY), Offset(innerX + innerW * 0.5f, innerY + innerH), strokeWidth = 2f)
            // 2 Bottom Drawers
            for (dIdx in 0..1) {
                val dy = innerY + innerH + 5f + dIdx * (closetH * 0.14f)
                drawRoundRect(closetStyle.trimColor, Offset(innerX, dy), Size(innerW, closetH * 0.10f), CornerRadius(4f, 4f))
                drawCircle(Color.White, radius = 2.2f, center = Offset(innerX + innerW * 0.5f, dy + closetH * 0.05f))
            }
        }
        1 -> {
            // STYLE 1: Sci-Fi Hexagonal Mecha Hangar Bay with Hazard Stripes & Plasma Pillars
            val hexPath = Path().apply {
                moveTo(closetX + closetW * 0.18f, closetY)
                lineTo(closetX + closetW * 0.82f, closetY)
                lineTo(closetX + closetW, closetY + closetH * 0.14f)
                lineTo(closetX + closetW, closetY + closetH * 0.88f)
                lineTo(closetX + closetW * 0.84f, closetY + closetH)
                lineTo(closetX + closetW * 0.16f, closetY + closetH)
                lineTo(closetX, closetY + closetH * 0.88f)
                lineTo(closetX, closetY + closetH * 0.14f)
                close()
            }
            drawPath(hexPath, brush = Brush.verticalGradient(listOf(closetLightBody, closetStyle.bodyColor, closetDarkBody)))
            drawPath(hexPath, color = closetStyle.trimColor, style = Stroke(width = 2.8f))
            // Glowing Left & Right Vertical Plasma Tubes
            drawRoundRect(closetStyle.trimColor, Offset(closetX + 4f, closetY + closetH * 0.18f), Size(4.5f, closetH * 0.64f), CornerRadius(3f, 3f))
            drawRoundRect(closetStyle.trimColor, Offset(closetX + closetW - 8.5f, closetY + closetH * 0.18f), Size(4.5f, closetH * 0.64f), CornerRadius(3f, 3f))
            // Mecha Chest Armor Display on Rack inside Hangar
            val cxM = closetX + closetW * 0.5f
            val cyM = closetY + closetH * 0.46f
            val armorP = Path().apply {
                moveTo(cxM - closetW * 0.26f, cyM - closetH * 0.14f)
                lineTo(cxM + closetW * 0.26f, cyM - closetH * 0.14f)
                lineTo(cxM + closetW * 0.18f, cyM + closetH * 0.12f)
                lineTo(cxM, cyM + closetH * 0.18f)
                lineTo(cxM - closetW * 0.18f, cyM + closetH * 0.12f)
                close()
            }
            drawPath(armorP, color = closetStyle.trimColor)
            drawCircle(Color.White, radius = 4f, center = Offset(cxM, cyM - 2f))
            // Yellow Hazard Base Bar
            drawRoundRect(Color(0xFFFACC15), Offset(closetX + closetW * 0.16f, closetY + closetH - 9f), Size(closetW * 0.68f, 6f), CornerRadius(2f, 2f))
        }
        2 -> {
            // STYLE 2: Cylindrical Glass Hologram Capsule Tube with Floating Rings & Levitating Crown
            val tubeX = closetX + closetW * 0.08f
            val tubeW = closetW * 0.84f
            // Top & Bottom Metallic Capsule Domes
            drawOval(closetStyle.trimColor, Offset(tubeX - 3f, closetY - 2f), Size(tubeW + 6f, closetH * 0.18f))
            drawOval(closetStyle.trimColor, Offset(tubeX - 3f, closetY + closetH * 0.84f), Size(tubeW + 6f, closetH * 0.16f))
            // Translucent Glass Cylinder Body
            drawRoundRect(
                brush = Brush.horizontalGradient(
                    listOf(
                        closetStyle.trimColor.copy(alpha = 0.55f),
                        closetLightBody.copy(alpha = 0.35f),
                        Color.White.copy(alpha = 0.45f),
                        closetStyle.trimColor.copy(alpha = 0.55f)
                    )
                ),
                topLeft = Offset(tubeX, closetY + closetH * 0.08f),
                size = Size(tubeW, closetH * 0.84f),
                cornerRadius = CornerRadius(14f, 14f)
            )
            drawRoundRect(closetStyle.trimColor, Offset(tubeX, closetY + closetH * 0.08f), Size(tubeW, closetH * 0.84f), CornerRadius(14f, 14f), style = Stroke(width = 2.2f))
            // 3 Floating Holographic Energy Rings inside Tube
            for (rIdx in 0..2) {
                val ry = closetY + closetH * (0.28f + rIdx * 0.22f)
                drawOval(Color.White.copy(alpha = 0.75f), Offset(tubeX + 5f, ry), Size(tubeW - 10f, 9f), style = Stroke(width = 2f))
            }
            // Levitating Golden Outfit / Gem Core inside Capsule
            drawCircle(Color(0xFFFACC15), radius = tubeW * 0.22f, center = Offset(tubeX + tubeW * 0.5f, closetY + closetH * 0.48f))
            drawCircle(Color.White, radius = tubeW * 0.10f, center = Offset(tubeX + tubeW * 0.46f, closetY + closetH * 0.46f))
        }
        3 -> {
            // STYLE 3: Japanese Pagoda Tansu Cabinet with Upturned Roof Eaves & Shoji Lattice Doors
            // Curved Pagoda Roof Eaves
            val roofPath = Path().apply {
                moveTo(closetX - 7f, closetY + 8f)
                quadraticTo(closetX + closetW * 0.5f, closetY - 12f, closetX + closetW + 7f, closetY + 8f)
                lineTo(closetX + closetW + 2f, closetY + 13f)
                lineTo(closetX - 2f, closetY + 13f)
                close()
            }
            drawPath(roofPath, color = closetStyle.trimColor)
            drawPath(roofPath, color = closetTrimDark, style = Stroke(width = 1.8f))
            // Wooden Tansu Body
            drawRoundRect(closetStyle.bodyColor, Offset(closetX + 2f, closetY + 11f), Size(closetW - 4f, closetH - 14f), CornerRadius(6f, 6f))
            drawRoundRect(closetStyle.trimColor, Offset(closetX + 2f, closetY + 11f), Size(closetW - 4f, closetH - 14f), CornerRadius(6f, 6f), style = Stroke(width = 2.4f))
            // Cream Paper Shoji Sliding Doors with Kumiko Lattice Grid
            val shojiX = closetX + 6f
            val shojiY = closetY + 17f
            val shojiW = closetW - 12f
            val shojiH = closetH * 0.52f
            drawRoundRect(Color(0xFFFFFBEB), Offset(shojiX, shojiY), Size(shojiW, shojiH), CornerRadius(4f, 4f))
            drawLine(closetStyle.trimColor, Offset(shojiX + shojiW * 0.5f, shojiY), Offset(shojiX + shojiW * 0.5f, shojiY + shojiH), strokeWidth = 2.2f)
            for (g in 1..2) {
                val gy = shojiY + shojiH * (g / 3f)
                drawLine(closetStyle.trimColor, Offset(shojiX, gy), Offset(shojiX + shojiW, gy), strokeWidth = 1.6f)
            }
            // Stepped Tansu Drawers at Bottom
            drawRoundRect(closetStyle.trimColor, Offset(shojiX, shojiY + shojiH + 6f), Size(shojiW * 0.46f, closetH * 0.16f), CornerRadius(3f, 3f))
            drawRoundRect(closetStyle.trimColor, Offset(shojiX + shojiW * 0.54f, shojiY + shojiH + 6f), Size(shojiW * 0.46f, closetH * 0.16f), CornerRadius(3f, 3f))
        }
        4 -> {
            // STYLE 4: Sultan / Pirate Heavy Vault Safe with Spinning Combination Wheel & Gold Bars
            drawRoundRect(closetDarkBody, Offset(closetX, closetY + 4f), Size(closetW, closetH - 4f), CornerRadius(14f, 14f))
            drawRoundRect(
                brush = Brush.verticalGradient(listOf(closetLightBody, closetStyle.bodyColor, closetDarkBody)),
                topLeft = Offset(closetX, closetY),
                size = Size(closetW, closetH - 4f),
                cornerRadius = CornerRadius(14f, 14f)
            )
            drawRoundRect(closetStyle.trimColor, Offset(closetX, closetY), Size(closetW, closetH - 4f), CornerRadius(14f, 14f), style = Stroke(width = 3.2f))
            // Heavy Vault Door Inner Rim + Corner Rivets
            drawRoundRect(closetTrimDark, Offset(closetX + 6f, closetY + 8f), Size(closetW - 12f, closetH * 0.68f), CornerRadius(8f, 8f), style = Stroke(width = 2f))
            // Giant 4-Spoke Golden Vault Combination Wheel in Center
            val wheelCx = closetX + closetW * 0.5f
            val wheelCy = closetY + closetH * 0.42f
            val wheelR = closetW * 0.28f
            drawCircle(closetTrimDark, radius = wheelR + 2f, center = Offset(wheelCx, wheelCy + 2f))
            drawCircle(Color(0xFFFACC15), radius = wheelR, center = Offset(wheelCx, wheelCy), style = Stroke(width = 3.5f))
            for (ang in listOf(0f, 45f, 90f, 135f)) {
                val rad = Math.toRadians(ang.toDouble())
                val dx = kotlin.math.cos(rad).toFloat() * wheelR
                val dy = kotlin.math.sin(rad).toFloat() * wheelR
                drawLine(Color(0xFFFACC15), Offset(wheelCx - dx, wheelCy - dy), Offset(wheelCx + dx, wheelCy + dy), strokeWidth = 2.4f)
            }
            drawCircle(Color(0xFFEF4444), radius = 4f, center = Offset(wheelCx, wheelCy))
            // Stacked Gold Bullion Bars at Bottom of Vault
            drawRoundRect(Color(0xFFFACC15), Offset(closetX + 7f, closetY + closetH - 16f), Size(closetW * 0.40f, 8f), CornerRadius(2f, 2f))
            drawRoundRect(Color(0xFFFDE047), Offset(closetX + closetW * 0.50f, closetY + closetH - 16f), Size(closetW * 0.40f, 8f), CornerRadius(2f, 2f))
        }
        else -> {
            // STYLE 5: Gothic / Kawaii Cloud Arched Spire Wardrobe with Tiered Display Pedestals
            val archPath = Path().apply {
                moveTo(closetX, closetY + closetH)
                lineTo(closetX, closetY + closetH * 0.26f)
                quadraticTo(closetX + closetW * 0.5f, closetY - closetH * 0.10f, closetX + closetW, closetY + closetH * 0.26f)
                lineTo(closetX + closetW, closetY + closetH)
                close()
            }
            drawPath(archPath, brush = Brush.verticalGradient(listOf(closetLightBody, closetStyle.bodyColor, closetDarkBody)))
            drawPath(archPath, color = closetStyle.trimColor, style = Stroke(width = 2.8f))
            // Top Star / Bat Finial Crest
            drawCircle(closetStyle.trimColor, radius = 5.5f, center = Offset(closetX + closetW * 0.5f, closetY + 4f))
            drawCircle(Color.White, radius = 2.5f, center = Offset(closetX + closetW * 0.5f, closetY + 4f))
            // 3 Arched Showcase Shelves inside
            for (sIdx in 0..2) {
                val sy = closetY + closetH * (0.34f + sIdx * 0.22f)
                drawLine(closetStyle.trimColor, Offset(closetX + 6f, sy), Offset(closetX + closetW - 6f, sy), strokeWidth = 2.4f)
                drawCircle(
                    color = if (sIdx == 0) Color(0xFFF43F5E) else if (sIdx == 1) Color(0xFF38BDF8) else Color(0xFFFACC15),
                    radius = 4.5f,
                    center = Offset(closetX + closetW * 0.5f, sy - 6f)
                )
            }
        }
    }

    // ------------------------------------------------------------------------
    // 5. 3D SOFA & SULTAN BED WITH 5 DISTINCT FURNITURE SILHOUETTES (LOWER LEFT)
    // ------------------------------------------------------------------------
    if (bedSofaStyle != MascotRoomBedSofaStyle.NONE) {
        val sofaX = w * 0.20f
        val sofaY = h * 0.45f
        val sofaW = w * 0.21f
        val sofaH = h * 0.27f
        val sofaDark = AppThemeController.darkenColor(bedSofaStyle.primaryColor, 0.66f)
        val sofaLight = AppThemeController.lightenColor(bedSofaStyle.primaryColor, 0.22f)
        val accentDark = AppThemeController.darkenColor(bedSofaStyle.accentColor, 0.68f)

        drawOval(Color.Black.copy(alpha = 0.32f), Offset(sofaX - 4f, sofaY + sofaH - 6f), Size(sofaW + 8f, 12f))

        when (bedSofaStyle.ordinal % 5) {
            1 -> {
                // SOFA 1: Futuristic Cyber Pod Gaming Chair with Bucket Wings & Neon Trim
                val chairCx = sofaX + sofaW * 0.5f
                drawLine(sofaDark, Offset(chairCx, sofaY + sofaH * 0.72f), Offset(chairCx, sofaY + sofaH), strokeWidth = 5f)
                drawLine(bedSofaStyle.accentColor, Offset(chairCx - sofaW * 0.32f, sofaY + sofaH - 2f), Offset(chairCx + sofaW * 0.32f, sofaY + sofaH - 2f), strokeWidth = 3.5f, cap = StrokeCap.Round)
                val podPath = Path().apply {
                    moveTo(chairCx - sofaW * 0.24f, sofaY)
                    lineTo(chairCx + sofaW * 0.24f, sofaY)
                    lineTo(chairCx + sofaW * 0.42f, sofaY + sofaH * 0.45f)
                    lineTo(chairCx + sofaW * 0.34f, sofaY + sofaH * 0.78f)
                    lineTo(chairCx - sofaW * 0.34f, sofaY + sofaH * 0.78f)
                    lineTo(chairCx - sofaW * 0.42f, sofaY + sofaH * 0.45f)
                    close()
                }
                drawPath(podPath, brush = Brush.verticalGradient(listOf(sofaLight, bedSofaStyle.primaryColor, sofaDark)))
                drawPath(podPath, color = bedSofaStyle.accentColor, style = Stroke(width = 2.4f))
                drawRoundRect(bedSofaStyle.accentColor, Offset(chairCx - sofaW * 0.16f, sofaY + 5f), Size(sofaW * 0.32f, 6f), CornerRadius(3f, 3f))
            }
            2 -> {
                // SOFA 2: Giant Kawaii Cat / Bear Plush Beanbag with Ears & Whiskers
                for (side in listOf(-1f, 1f)) {
                    val earP = Path().apply {
                        val ex = sofaX + sofaW * 0.5f + side * sofaW * 0.28f
                        moveTo(ex - sofaW * 0.12f, sofaY + sofaH * 0.28f)
                        lineTo(ex + side * sofaW * 0.06f, sofaY - 4f)
                        lineTo(ex + sofaW * 0.12f, sofaY + sofaH * 0.28f)
                        close()
                    }
                    drawPath(earP, color = bedSofaStyle.accentColor)
                }
                drawOval(sofaDark, Offset(sofaX, sofaY + sofaH * 0.18f + 3f), Size(sofaW, sofaH * 0.76f))
                drawOval(brush = Brush.radialGradient(listOf(sofaLight, bedSofaStyle.primaryColor)), topLeft = Offset(sofaX, sofaY + sofaH * 0.16f), size = Size(sofaW, sofaH * 0.74f))
                drawCircle(Color(0xFF0F172A), radius = 2.4f, center = Offset(sofaX + sofaW * 0.36f, sofaY + sofaH * 0.48f))
                drawCircle(Color(0xFF0F172A), radius = 2.4f, center = Offset(sofaX + sofaW * 0.64f, sofaY + sofaH * 0.48f))
                drawOval(Color(0xFFF472B6), Offset(sofaX + sofaW * 0.45f, sofaY + sofaH * 0.52f), Size(sofaW * 0.10f, 4f))
            }
            3 -> {
                // SOFA 3: Four-Poster Royal Canopy Bed with Tall Posts & Draped Valance
                drawLine(bedSofaStyle.accentColor, Offset(sofaX + 3f, sofaY - 6f), Offset(sofaX + 3f, sofaY + sofaH), strokeWidth = 3.2f)
                drawLine(bedSofaStyle.accentColor, Offset(sofaX + sofaW - 3f, sofaY - 6f), Offset(sofaX + sofaW - 3f, sofaY + sofaH), strokeWidth = 3.2f)
                drawRoundRect(bedSofaStyle.accentColor, Offset(sofaX, sofaY - 8f), Size(sofaW, 7f), CornerRadius(3f, 3f))
                drawRoundRect(bedSofaStyle.primaryColor, Offset(sofaX + 4f, sofaY + sofaH * 0.48f), Size(sofaW - 8f, sofaH * 0.44f), CornerRadius(7f, 7f))
                drawRoundRect(Color.White, Offset(sofaX + 8f, sofaY + sofaH * 0.38f), Size(sofaW * 0.36f, sofaH * 0.18f), CornerRadius(5f, 5f))
            }
            4 -> {
                // SOFA 4: Japanese Tatami Floor Futon with Round Bolster & Low Tea Roll
                drawRoundRect(sofaDark, Offset(sofaX - 2f, sofaY + sofaH * 0.56f), Size(sofaW + 4f, sofaH * 0.36f), CornerRadius(6f, 6f))
                drawRoundRect(bedSofaStyle.primaryColor, Offset(sofaX, sofaY + sofaH * 0.46f), Size(sofaW, sofaH * 0.36f), CornerRadius(8f, 8f))
                drawRoundRect(bedSofaStyle.accentColor, Offset(sofaX + sofaW * 0.25f, sofaY + sofaH * 0.46f), Size(sofaW * 0.50f, sofaH * 0.36f), CornerRadius(4f, 4f))
                drawOval(Color.White, Offset(sofaX + 6f, sofaY + sofaH * 0.34f), Size(sofaW * 0.34f, sofaH * 0.20f))
            }
            else -> {
                // SOFA 0: Classic Royal Tufted Chesterfield Sofa with Rolled Armrests & 2 Pillows
                drawRoundRect(sofaDark, Offset(sofaX + sofaW * 0.06f, sofaY + 3f), Size(sofaW * 0.88f, sofaH * 0.66f), CornerRadius(12f, 12f))
                drawRoundRect(brush = Brush.verticalGradient(listOf(sofaLight, bedSofaStyle.primaryColor)), topLeft = Offset(sofaX + sofaW * 0.06f, sofaY), size = Size(sofaW * 0.88f, sofaH * 0.62f), cornerRadius = CornerRadius(12f, 12f))
                drawRoundRect(bedSofaStyle.accentColor, Offset(sofaX + sofaW * 0.06f, sofaY), Size(sofaW * 0.88f, sofaH * 0.62f), CornerRadius(12f, 12f), style = Stroke(width = 2.2f))
                for (armX in listOf(sofaX - 2f, sofaX + sofaW * 0.82f + 2f)) {
                    drawRoundRect(bedSofaStyle.primaryColor, Offset(armX, sofaY + sofaH * 0.34f), Size(sofaW * 0.18f, sofaH * 0.52f), CornerRadius(8f, 8f))
                }
                drawRoundRect(sofaDark, Offset(sofaX + 2f, sofaY + sofaH * 0.52f), Size(sofaW - 4f, sofaH * 0.42f), CornerRadius(9f, 9f))
                drawRoundRect(bedSofaStyle.accentColor, Offset(sofaX + sofaW * 0.16f, sofaY + sofaH * 0.28f), Size(sofaW * 0.28f, sofaH * 0.26f), CornerRadius(5f, 5f))
                drawRoundRect(Color(0xFF38BDF8), Offset(sofaX + sofaW * 0.54f, sofaY + sofaH * 0.28f), Size(sofaW * 0.28f, sofaH * 0.26f), CornerRadius(5f, 5f))
            }
        }
    }

    // ------------------------------------------------------------------------
    // 6. 3D DESK, PC, ARCADE & DJ BOOTH WITH 5 DISTINCT SHAPES (RIGHT SIDE)
    // ------------------------------------------------------------------------
    if (deskStyle != MascotRoomDeskStyle.NONE) {
        val deskX = w * 0.74f
        val deskY = h * 0.33f
        val deskW = w * 0.235f
        val deskH = h * 0.38f
        val deskDark = AppThemeController.darkenColor(deskStyle.primaryColor, 0.65f)
        val deskLight = AppThemeController.lightenColor(deskStyle.primaryColor, 0.22f)
        val tableTopY = deskY + deskH * 0.46f

        drawOval(Color.Black.copy(alpha = 0.34f), Offset(deskX - 3f, deskY + deskH - 5f), Size(deskW + 6f, 12f))

        when (deskStyle.ordinal % 5) {
            1 -> {
                // DESK 1: Tall Retro Stand-Up Arcade Cabinet with Glowing Marquee & Joystick
                val arcX = deskX + deskW * 0.12f
                val arcW = deskW * 0.76f
                drawRoundRect(deskDark, Offset(arcX + 3f, deskY), Size(arcW, deskH), CornerRadius(8f, 8f))
                drawRoundRect(brush = Brush.verticalGradient(listOf(deskLight, deskStyle.primaryColor, deskDark)), topLeft = Offset(arcX, deskY), size = Size(arcW, deskH - 2f), cornerRadius = CornerRadius(8f, 8f))
                drawRoundRect(deskStyle.accentColor, Offset(arcX, deskY), Size(arcW, deskH - 2f), CornerRadius(8f, 8f), style = Stroke(width = 2.4f))
                // Top Glowing Arcade Marquee Header
                drawRoundRect(Color(0xFFFACC15), Offset(arcX + 3f, deskY + 3f), Size(arcW - 6f, deskH * 0.16f), CornerRadius(4f, 4f))
                // CRT Arcade Screen
                drawRoundRect(Color(0xFF0F172A), Offset(arcX + 5f, deskY + deskH * 0.22f), Size(arcW - 10f, deskH * 0.30f), CornerRadius(4f, 4f))
                drawCircle(Color(0xFF58CC02), radius = 3.5f, center = Offset(arcX + arcW * 0.38f, deskY + deskH * 0.36f))
                drawCircle(Color(0xFFFF4B4B), radius = 3.5f, center = Offset(arcX + arcW * 0.65f, deskY + deskH * 0.36f))
                // Joystick Deck + Coin Slot
                drawRoundRect(deskStyle.accentColor, Offset(arcX - 2f, deskY + deskH * 0.54f), Size(arcW + 4f, 6f), CornerRadius(3f, 3f))
                drawCircle(Color(0xFFEF4444), radius = 3f, center = Offset(arcX + arcW * 0.30f, deskY + deskH * 0.49f))
                drawRoundRect(Color(0xFFFACC15), Offset(arcX + arcW * 0.38f, deskY + deskH * 0.72f), Size(arcW * 0.24f, deskH * 0.16f), CornerRadius(2f, 2f))
            }
            2 -> {
                // DESK 2: Alchemist / Wizard Potion Workbench with Bubbling Flasks & Open Grimoire
                drawRoundRect(deskDark, Offset(deskX + 4f, tableTopY + 5f), Size(deskW * 0.16f, deskH * 0.46f), CornerRadius(3f, 3f))
                drawRoundRect(deskDark, Offset(deskX + deskW * 0.78f, tableTopY + 5f), Size(deskW * 0.16f, deskH * 0.46f), CornerRadius(3f, 3f))
                drawRoundRect(deskStyle.primaryColor, Offset(deskX - 2f, tableTopY), Size(deskW + 4f, 8f), CornerRadius(4f, 4f))
                // Round Alchemist Boiling Flask + Green Mana Vial + Open Spellbook on Table
                drawCircle(Color(0xFFEC4899), radius = 8.5f, center = Offset(deskX + deskW * 0.26f, tableTopY - 8f))
                drawRect(Color.White, Offset(deskX + deskW * 0.26f - 2.5f, tableTopY - 21f), Size(5f, 6f))
                drawCircle(Color(0xFF58CC02), radius = 6.5f, center = Offset(deskX + deskW * 0.55f, tableTopY - 6f))
                drawRoundRect(Color(0xFFFDE047), Offset(deskX + deskW * 0.72f, tableTopY - 8f), Size(deskW * 0.24f, 8f), CornerRadius(2f, 2f))
            }
            3 -> {
                // DESK 3: Dual DJ Turntable Mixer Booth with Spinning Vinyl Decks & Studio Speakers
                drawRoundRect(deskDark, Offset(deskX, tableTopY), Size(deskW, deskH * 0.50f), CornerRadius(6f, 6f))
                drawRoundRect(deskStyle.accentColor, Offset(deskX, tableTopY), Size(deskW, deskH * 0.50f), CornerRadius(6f, 6f), style = Stroke(width = 2f))
                // Left & Right Vinyl Turntable Platters on Top
                for (px in listOf(deskX + deskW * 0.26f, deskX + deskW * 0.74f)) {
                    drawOval(Color(0xFF0F172A), Offset(px - 10f, tableTopY - 6f), Size(20f, 10f))
                    drawOval(deskStyle.accentColor, Offset(px - 4f, tableTopY - 3f), Size(8f, 4f))
                }
                // Equalizer Bars on Front Panel of DJ Booth
                for (eq in 0..4) {
                    val eqX = deskX + 6f + eq * ((deskW - 14f) / 5f)
                    drawRoundRect(deskStyle.accentColor, Offset(eqX, tableTopY + 8f), Size(4f, deskH * 0.28f), CornerRadius(2f, 2f))
                }
            }
            else -> {
                // DESK 0 & 4: Triple/Ultrawide RGB Gaming PC Setup with Glass Tower & Boba Cup
                drawRect(deskDark, Offset(deskX + deskW * 0.38f, deskY + deskH * 0.38f), Size(deskW * 0.14f, deskH * 0.10f))
                val monX = deskX + deskW * 0.04f
                val monW = deskW * 0.82f
                val monH = deskH * 0.41f
                drawRoundRect(deskStyle.accentColor, Offset(monX, deskY), Size(monW, monH), CornerRadius(8f, 8f))
                drawRoundRect(Color(0xFF0F172A), Offset(monX + 3f, deskY + 3f), Size(monW - 6f, monH - 6f), CornerRadius(5f, 5f))
                val barColors = listOf(Color(0xFF58CC02), Color(0xFFFFC800), Color(0xFFFF4B4B), Color(0xFF00F2FE))
                for (bIdx in 0..3) {
                    val bx = monX + 7f + bIdx * ((monW - 16f) / 4f)
                    val bh = (monH - 12f) * (0.45f + (bIdx % 2) * 0.30f)
                    drawRoundRect(barColors[bIdx], Offset(bx, deskY + monH - 5f - bh), Size(5f, bh), CornerRadius(2f, 2f))
                }
                // Tempered Glass RGB PC Tower under Desk
                val pcX = deskX + deskW * 0.62f
                val pcY = tableTopY + 5f
                val pcW = deskW * 0.34f
                val pcH = deskH * 0.47f
                drawRoundRect(deskDark, Offset(pcX, pcY), Size(pcW, pcH), CornerRadius(5f, 5f))
                drawRoundRect(deskStyle.accentColor, Offset(pcX, pcY), Size(pcW, pcH), CornerRadius(5f, 5f), style = Stroke(width = 1.8f))
                for (fIdx in 0..2) {
                    drawCircle(barColors[fIdx], radius = 3f, center = Offset(pcX + pcW * 0.5f, pcY + pcH * (0.22f + fIdx * 0.28f)), style = Stroke(width = 1.6f))
                }
                drawRoundRect(deskDark, Offset(deskX + 3f, tableTopY + 6f), Size(deskW * 0.15f, deskH * 0.46f), CornerRadius(3f, 3f))
                drawRoundRect(brush = Brush.horizontalGradient(listOf(deskLight, deskStyle.primaryColor)), topLeft = Offset(deskX - 2f, tableTopY), size = Size(deskW + 4f, 7f), cornerRadius = CornerRadius(4f, 4f))
            }
        }
    }

    // ------------------------------------------------------------------------
    // 7. 3D CENTER RUG WITH 6 RADICALLY DIFFERENT SHAPES & PATTERNS (CENTER)
    // ------------------------------------------------------------------------
    val rugW = w * 0.46f
    val rugH = (h - floorTop) * 0.68f
    val rugX = (w - rugW) * 0.5f
    val rugTop = floorTop + (h - floorTop - rugH) * 0.38f
    val rugCx = w * 0.5f
    val rugCy = rugTop + rugH * 0.5f
    val rugDark = AppThemeController.darkenColor(rugStyle.mainColor, 0.62f)
    val rugLight = AppThemeController.lightenColor(rugStyle.mainColor, 0.22f)

    when (rugStyle.ordinal % 6) {
        0 -> {
            // RUG 0: Ornate Rectangular 3D Perspective Persian/Sultan Carpet with Golden Fringes
            for (tIdx in 0..4) {
                val ty = rugTop + rugH * (0.20f + tIdx * 0.15f)
                drawLine(rugStyle.ringColor, Offset(rugX - 9f, ty), Offset(rugX + 4f, ty), strokeWidth = 2.4f, cap = StrokeCap.Round)
                drawLine(rugStyle.ringColor, Offset(rugX + rugW - 4f, ty), Offset(rugX + rugW + 9f, ty), strokeWidth = 2.4f, cap = StrokeCap.Round)
            }
            val persianPoly = Path().apply {
                moveTo(rugX + rugW * 0.08f, rugTop)
                lineTo(rugX + rugW * 0.92f, rugTop)
                lineTo(rugX + rugW, rugTop + rugH)
                lineTo(rugX, rugTop + rugH)
                close()
            }
            drawPath(persianPoly, brush = Brush.verticalGradient(listOf(rugLight, rugStyle.mainColor, rugDark), startY = rugTop, endY = rugTop + rugH))
            drawPath(persianPoly, color = rugStyle.ringColor, style = Stroke(width = 3.2f))
            // Inner Diamond Medallion
            val diamond = Path().apply {
                moveTo(rugCx, rugTop + rugH * 0.18f)
                lineTo(rugCx + rugW * 0.26f, rugCy)
                lineTo(rugCx, rugTop + rugH * 0.82f)
                lineTo(rugCx - rugW * 0.26f, rugCy)
                close()
            }
            drawPath(diamond, color = rugStyle.ringColor.copy(alpha = 0.80f), style = Stroke(width = 2.2f))
            drawCircle(Color.White.copy(alpha = 0.80f), radius = rugH * 0.12f, center = Offset(rugCx, rugCy))
        }
        1 -> {
            // RUG 1: Arcane Magic Hexagram Star Circle Mat with Glowing Rune Nodes
            drawOval(rugDark, Offset(rugX, rugTop + 4f), Size(rugW, rugH))
            drawOval(brush = Brush.radialGradient(listOf(rugLight, rugStyle.mainColor, rugDark), center = Offset(rugCx, rugCy), radius = rugW * 0.52f), topLeft = Offset(rugX, rugTop), size = Size(rugW, rugH))
            drawOval(rugStyle.ringColor, Offset(rugX + 2f, rugTop + 1.5f), Size(rugW - 4f, rugH - 3f), style = Stroke(width = 3f))
            // 6-Point Star inside Oval
            val starP = Path()
            for (i in 0..5) {
                val a1 = Math.toRadians((i * 60).toDouble())
                val a2 = Math.toRadians(((i + 2) * 60).toDouble())
                val x1 = rugCx + kotlin.math.cos(a1).toFloat() * (rugW * 0.36f)
                val y1 = rugCy + kotlin.math.sin(a1).toFloat() * (rugH * 0.36f)
                val x2 = rugCx + kotlin.math.cos(a2).toFloat() * (rugW * 0.36f)
                val y2 = rugCy + kotlin.math.sin(a2).toFloat() * (rugH * 0.36f)
                starP.moveTo(x1, y1)
                starP.lineTo(x2, y2)
                drawCircle(Color.White, radius = 2.5f, center = Offset(x1, y1))
            }
            drawPath(starP, color = rugStyle.ringColor, style = Stroke(width = 2f))
        }
        2 -> {
            // RUG 2: Fluffy 8-Petal Sakura / Scalloped Cloud Plush Rug
            for (i in 0..7) {
                val ang = Math.toRadians((i * 45).toDouble())
                val px = rugCx + kotlin.math.cos(ang).toFloat() * (rugW * 0.30f)
                val py = rugCy + kotlin.math.sin(ang).toFloat() * (rugH * 0.28f)
                drawOval(rugDark, Offset(px - rugW * 0.20f, py - rugH * 0.22f + 3.5f), Size(rugW * 0.40f, rugH * 0.44f))
                drawOval(rugStyle.mainColor, Offset(px - rugW * 0.20f, py - rugH * 0.22f), Size(rugW * 0.40f, rugH * 0.44f))
                drawOval(rugStyle.ringColor, Offset(px - rugW * 0.20f, py - rugH * 0.22f), Size(rugW * 0.40f, rugH * 0.44f), style = Stroke(width = 1.6f))
            }
            drawOval(rugLight, Offset(rugCx - rugW * 0.28f, rugCy - rugH * 0.28f), Size(rugW * 0.56f, rugH * 0.56f))
            drawCircle(rugStyle.ringColor, radius = rugH * 0.16f, center = Offset(rugCx, rugCy))
        }
        3 -> {
            // RUG 3: Hexagonal Cyber Mecha Helipad Mat with Hazard Corner Chevrons
            val hexRug = Path().apply {
                for (i in 0..5) {
                    val ang = Math.toRadians((i * 60).toDouble())
                    val hx = rugCx + kotlin.math.cos(ang).toFloat() * (rugW * 0.48f)
                    val hy = rugCy + kotlin.math.sin(ang).toFloat() * (rugH * 0.48f)
                    if (i == 0) moveTo(hx, hy) else lineTo(hx, hy)
                }
                close()
            }
            drawPath(hexRug, brush = Brush.radialGradient(listOf(rugLight, rugStyle.mainColor, rugDark), center = Offset(rugCx, rugCy), radius = rugW * 0.5f))
            drawPath(hexRug, color = rugStyle.ringColor, style = Stroke(width = 3.2f))
            drawLine(rugStyle.ringColor, Offset(rugCx - rugW * 0.18f, rugCy), Offset(rugCx + rugW * 0.18f, rugCy), strokeWidth = 3f)
            drawLine(rugStyle.ringColor, Offset(rugCx, rugCy - rugH * 0.22f), Offset(rugCx, rugCy + rugH * 0.22f), strokeWidth = 3f)
        }
        4 -> {
            // RUG 4: Giant Cute Cat Paw Plush Rug with 4 Pink/Gold Toe Beans!
            // 4 Upper Toe Bean Pads
            for (t in 0..3) {
                val tx = rugCx + (t - 1.5f) * (rugW * 0.21f)
                val ty = rugTop + rugH * (if (t == 1 || t == 2) 0.18f else 0.28f)
                drawOval(rugDark, Offset(tx - rugW * 0.08f, ty - rugH * 0.12f + 3f), Size(rugW * 0.16f, rugH * 0.24f))
                drawOval(rugStyle.mainColor, Offset(tx - rugW * 0.08f, ty - rugH * 0.12f), Size(rugW * 0.16f, rugH * 0.24f))
                drawOval(rugStyle.ringColor, Offset(tx - rugW * 0.05f, ty - rugH * 0.07f), Size(rugW * 0.10f, rugH * 0.14f))
            }
            // Main Lower Palm Pad
            drawOval(rugDark, Offset(rugCx - rugW * 0.34f, rugTop + rugH * 0.36f + 3.5f), Size(rugW * 0.68f, rugH * 0.58f))
            drawOval(rugStyle.mainColor, Offset(rugCx - rugW * 0.34f, rugTop + rugH * 0.36f), Size(rugW * 0.68f, rugH * 0.58f))
            drawOval(rugStyle.ringColor, Offset(rugCx - rugW * 0.20f, rugTop + rugH * 0.48f), Size(rugW * 0.40f, rugH * 0.34f))
        }
        else -> {
            // RUG 5: 8-Bit Retro Pixel Checkerboard / VIP Runway Mat
            drawRoundRect(rugDark, Offset(rugX, rugTop + 4f), Size(rugW, rugH), CornerRadius(10f, 10f))
            drawRoundRect(rugStyle.mainColor, Offset(rugX, rugTop), Size(rugW, rugH), CornerRadius(10f, 10f))
            val cols = 6
            val rows = 2
            val cw = (rugW - 12f) / cols
            val rh = (rugH - 8f) / rows
            for (r in 0 until rows) {
                for (c in 0 until cols) {
                    if ((r + c) % 2 == 0) {
                        drawRoundRect(
                            color = rugStyle.ringColor.copy(alpha = 0.72f),
                            topLeft = Offset(rugX + 6f + c * cw, rugTop + 4f + r * rh),
                            size = Size(cw, rh),
                            cornerRadius = CornerRadius(3f, 3f)
                        )
                    }
                }
            }
            drawRoundRect(rugStyle.ringColor, Offset(rugX, rugTop), Size(rugW, rugH), CornerRadius(10f, 10f), style = Stroke(width = 2.8f))
        }
    }

    // ------------------------------------------------------------------------
    // 8. 3D MINI COMPANION PET WITH 4 DISTINCT SPECIES SILHOUETTES (FOREGROUND)
    // ------------------------------------------------------------------------
    if (petStyle != MascotRoomPetStyle.NONE) {
        val petCx = w * 0.66f
        val petCy = floorTop + (h - floorTop) * 0.36f
        val petR = (h * 0.072f).coerceAtLeast(11f)
        val petDark = AppThemeController.darkenColor(petStyle.primaryColor, 0.68f)
        val petLight = AppThemeController.lightenColor(petStyle.primaryColor, 0.25f)

        // Royal Velvet Pet Cushion Pad underneath Pet
        drawOval(Color.Black.copy(alpha = 0.32f), Offset(petCx - petR * 1.35f, petCy + petR * 0.52f), Size(petR * 2.7f, petR * 0.72f))
        drawOval(Color(0xFFB91C1C), Offset(petCx - petR * 1.25f, petCy + petR * 0.38f), Size(petR * 2.5f, petR * 0.65f))
        drawOval(Color(0xFFFACC15), Offset(petCx - petR * 1.25f, petCy + petR * 0.38f), Size(petR * 2.5f, petR * 0.65f), style = Stroke(width = 2f))

        when (petStyle.ordinal % 4) {
            1 -> {
                // PET 1: Winged Baby Dragon with Horns & Bat Wings
                for (side in listOf(-1f, 1f)) {
                    val wing = Path().apply {
                        moveTo(petCx + side * petR * 0.6f, petCy)
                        lineTo(petCx + side * petR * 1.55f, petCy - petR * 0.75f)
                        lineTo(petCx + side * petR * 1.25f, petCy + petR * 0.35f)
                        close()
                    }
                    drawPath(wing, color = petStyle.secondaryColor)
                }
            }
            2 -> {
                // PET 2: Floating Cyber Drone Bot with Twin Antennae & Halo Ring
                drawOval(petStyle.secondaryColor, Offset(petCx - petR * 1.3f, petCy - petR * 0.2f), Size(petR * 2.6f, petR * 0.55f), style = Stroke(width = 2f))
                drawLine(petStyle.secondaryColor, Offset(petCx, petCy - petR), Offset(petCx, petCy - petR * 1.45f), strokeWidth = 2.2f)
                drawCircle(Color(0xFF00F2FE), radius = 3f, center = Offset(petCx, petCy - petR * 1.48f))
            }
            else -> {
                // PET 0 & 3: Cute Chibi Fox/Cat/Slime Spirit with Pointed Ears & Crown
                for (side in listOf(-1f, 1f)) {
                    val earPath = Path().apply {
                        moveTo(petCx + side * petR * 0.55f, petCy - petR * 0.35f)
                        lineTo(petCx + side * petR * 1.05f, petCy - petR * 1.20f)
                        lineTo(petCx + side * petR * 0.12f, petCy - petR * 0.78f)
                        close()
                    }
                    drawPath(earPath, color = petStyle.secondaryColor)
                }
            }
        }

        drawCircle(petDark, radius = petR, center = Offset(petCx, petCy + 2.5f))
        drawCircle(brush = Brush.verticalGradient(listOf(petLight, petStyle.primaryColor)), radius = petR, center = Offset(petCx, petCy))
        drawCircle(petStyle.secondaryColor, radius = petR, center = Offset(petCx, petCy), style = Stroke(width = 1.8f))
        drawCircle(Color(0xFF0F172A), radius = petR * 0.19f, center = Offset(petCx - petR * 0.34f, petCy - petR * 0.04f))
        drawCircle(Color(0xFF0F172A), radius = petR * 0.19f, center = Offset(petCx + petR * 0.34f, petCy - petR * 0.04f))
        drawCircle(Color.White, radius = petR * 0.08f, center = Offset(petCx - petR * 0.38f, petCy - petR * 0.10f))
        drawCircle(Color.White, radius = petR * 0.08f, center = Offset(petCx + petR * 0.30f, petCy - petR * 0.10f))
    }

    // ------------------------------------------------------------------------
    // 9. 3D CEILING LAMP / CHANDELIER WITH 5 DISTINCT FIXTURE SHAPES (TOP)
    // ------------------------------------------------------------------------
    val lampCx = w * 0.5f
    val conePath = Path().apply {
        moveTo(lampCx - w * 0.09f, h * 0.06f)
        lineTo(lampCx - w * 0.30f, floorTop + 14f)
        lineTo(lampCx + w * 0.30f, floorTop + 14f)
        lineTo(lampCx + w * 0.09f, h * 0.06f)
        close()
    }
    drawPath(conePath, brush = Brush.verticalGradient(listOf(lampStyle.glowColor.copy(alpha = 0.26f), Color.Transparent)))

    when (lampStyle.ordinal % 5) {
        1 -> {
            // LAMP 1: Sci-Fi UFO Saucer Disc Emitter with Neon Rings
            drawOval(lampStyle.fixtureColor, Offset(lampCx - w * 0.14f, h * 0.02f), Size(w * 0.28f, h * 0.07f))
            drawOval(lampStyle.glowColor, Offset(lampCx - w * 0.09f, h * 0.04f), Size(w * 0.18f, h * 0.05f))
        }
        2 -> {
            // LAMP 2: 3 Hanging Japanese Paper Chochin Lanterns
            for (bOffset in listOf(-w * 0.09f, 0f, w * 0.09f)) {
                val bx = lampCx + bOffset
                val by = if (bOffset == 0f) h * 0.09f else h * 0.075f
                drawLine(lampStyle.fixtureColor, Offset(bx, 0f), Offset(bx, by - 6f), strokeWidth = 2f)
                drawOval(lampStyle.glowColor, Offset(bx - 7.5f, by - 6f), Size(15f, 13f))
                drawOval(lampStyle.fixtureColor, Offset(bx - 7.5f, by - 6f), Size(15f, 13f), style = Stroke(width = 1.6f))
            }
        }
        3 -> {
            // LAMP 3: Floating Fluffy Cloud & Crescent Moon Nightlight
            drawCircle(Color.White, radius = 9f, center = Offset(lampCx - 6f, h * 0.065f))
            drawCircle(Color.White, radius = 11f, center = Offset(lampCx + 3f, h * 0.055f))
            drawCircle(lampStyle.glowColor, radius = 6.5f, center = Offset(lampCx + 12f, h * 0.055f))
        }
        else -> {
            // LAMP 0 & 4: Royal 3-Bulb Crystal Chandelier
            drawRoundRect(lampStyle.fixtureColor, Offset(w * 0.28f, h * 0.01f), Size(w * 0.44f, 5f), CornerRadius(3f, 3f))
            for (bOffset in listOf(-w * 0.08f, 0f, w * 0.08f)) {
                val bx = lampCx + bOffset
                val by = if (bOffset == 0f) h * 0.095f else h * 0.08f
                drawLine(Color(0xFFFACC15), Offset(bx, h * 0.03f), Offset(bx, by), strokeWidth = 2f)
                drawCircle(color = lampStyle.fixtureColor, radius = if (bOffset == 0f) 9.5f else 7f, center = Offset(bx, by))
                drawCircle(color = lampStyle.glowColor, radius = if (bOffset == 0f) 6f else 4.5f, center = Offset(bx, by))
                drawCircle(color = Color.White, radius = 2.2f, center = Offset(bx - 1.5f, by - 1.5f))
            }
        }
    }
}

// ============================================================================
// 9. MINIMALIST & SPACE-EFFICIENT "KAMAR GANTI" TAB SYSTEM + COMPACT 4-COL GRID
// ============================================================================

@Composable
fun MascotWardrobeStudioCard(
    activeAccessory: MascotAccessory,
    activeSkin: MascotBodySkin,
    activeOutfit: MascotUpperOutfit,
    activeGloves: MascotHandGloves,
    onSelectAccessory: (MascotAccessory) -> Unit,
    onSelectSkin: (MascotBodySkin) -> Unit,
    onSelectOutfit: (MascotUpperOutfit) -> Unit,
    onSelectGloves: (MascotHandGloves) -> Unit,
    onApplyPreset: (MascotCostumePreset) -> Unit,
    onRandomizeAll: () -> Unit,
    onResetDefault: () -> Unit,
    isMascotOverlayVisible: Boolean = true,
    onSetMascotOverlayVisible: ((Boolean) -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    // Primary 4-Group Tab Architecture:
    // 0 = Lemari Kostum (Baju 34, Skin 34, Tangan 34, Aksesoris 40, Set 12)
    // 1 = Aura & Efek (Aura 16)
    // 2 = Emosi & Interaksi (Emosi 20, Interaksi 16)
    // 3 = Dekor Kamar (9 Kategori × 15 + Preset 6)
    var primaryGroupTab by remember { mutableIntStateOf(0) }

    // Sub-tabs within each primary group:
    // Group 0 (Lemari): 0=Baju(34), 1=Skin(34), 2=Tangan(34), 3=Aksesoris(40), 4=Set(12)
    var closetCategoryTab by remember { mutableIntStateOf(0) }
    // Group 2 (Emosi & Interaksi): 0=Emosi(20), 1=Interaksi(16)
    var expressionSubTab by remember { mutableIntStateOf(0) }
    // Group 3 (Kamar): 0=Lemari, 1=Meja PC, 2=Sofa/Kasur, 3=Jendela/Rak, 4=Pet Kamar, 5=Lantai, 6=Lampu, 7=Dinding, 8=Karpet, 9=Preset
    var roomPartSubTab by remember { mutableIntStateOf(0) }

    val configuration = LocalConfiguration.current
    val isCompactPhone = configuration.screenWidthDp <= 370
    var globalThemeFilter by remember { mutableStateOf("SEMUA") }
    var isSearchExpanded by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }
    var isGridExpandedFull by remember { mutableStateOf(false) }
    var gridColumnCount by remember(isCompactPhone) { mutableIntStateOf(if (isCompactPhone) 3 else 4) }

    val activeAura = AppThemeController.mascotAura
    val activeEmotion = AppThemeController.mascotEmotion
    val activePose = AppThemeController.mascotActivityPose
    val wallStyle = AppThemeController.roomWallStyle
    val floorStyle = AppThemeController.roomFloorStyle
    val closetStyle = AppThemeController.roomClosetStyle
    val lampStyle = AppThemeController.roomLampStyle
    val rugStyle = AppThemeController.roomRugStyle
    val deskStyle = AppThemeController.roomDeskStyle
    val bedSofaStyle = AppThemeController.roomBedSofaStyle
    val windowDecorStyle = AppThemeController.roomWindowDecorStyle
    val petStyle = AppThemeController.roomPetStyle

    Box(
        modifier = modifier
            .fillMaxWidth()
            .duolingoCard(
                backgroundColor = CosmicObsidian,
                borderColor = wallStyle.accentColor,
                lipColor = AppThemeController.darkenColor(wallStyle.accentColor, 0.72f),
                cornerRadius = 20.dp,
                borderWidth = 2.dp,
                lipHeight = 4.dp
            )
            .padding(bottom = 4.dp)
            .padding(10.dp)
            .testTag("mascot_wardrobe_studio_card")
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(7.dp)) {
            // 1. ULTRA-MINIMALIST HEADER BAR (Title + Search Toggle + Grid Density + Acak + Reset + Overlay)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = "Kamar Ganti",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.ExtraBold,
                        color = CosmicTextPure,
                        maxLines = 1
                    )
                    Box(
                        modifier = Modifier
                            .weight(1f, fill = false)
                            .clip(RoundedCornerShape(6.dp))
                            .background(DuoMintSurface)
                            .border(1.dp, DuoSwanBorder, RoundedCornerShape(6.dp))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = "${activeOutfit.displayName} • ${activeAccessory.displayName}",
                            fontSize = 8.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = CosmicTextSubtle,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    // Toggle Search Input (0dp height when collapsed!)
                    Box(
                        modifier = Modifier
                            .duolingoButton(
                                faceColor = if (isSearchExpanded || searchQuery.isNotEmpty()) DuoMacawBlue else DuoMintSurface,
                                lipColor = if (isSearchExpanded || searchQuery.isNotEmpty()) DuoMacawBlueLip else DuoHareLip,
                                cornerRadius = 8.dp,
                                lipHeight = 2.dp
                            )
                            .instantTouchPress {
                                isSearchExpanded = !isSearchExpanded
                                if (!isSearchExpanded) searchQuery = ""
                            }
                            .padding(bottom = 2.dp)
                            .padding(horizontal = 6.dp, vertical = 4.dp)
                            .testTag("wardrobe_toggle_search_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Cari",
                            tint = if (isSearchExpanded || searchQuery.isNotEmpty()) Color.White else DuoEelInk,
                            modifier = Modifier.size(12.dp)
                        )
                    }

                    // Toggle 2x / 3x / 4x Grid Density for 720x1600px & all phone sizes
                    Box(
                        modifier = Modifier
                            .duolingoButton(
                                faceColor = DuoMintSurface,
                                lipColor = DuoHareLip,
                                cornerRadius = 8.dp,
                                lipHeight = 2.dp
                            )
                            .instantTouchPress {
                                gridColumnCount = when (gridColumnCount) {
                                    4 -> 3
                                    3 -> 2
                                    else -> 4
                                }
                            }
                            .padding(bottom = 2.dp)
                            .padding(horizontal = 6.dp, vertical = 4.dp)
                            .testTag("wardrobe_grid_density_button")
                    ) {
                        Text(
                            text = "${gridColumnCount}x",
                            fontFamily = JetBrainsMonoFontFamily,
                            fontSize = 8.5.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = DuoEelInk
                        )
                    }

                    // Acak Semua
                    Box(
                        modifier = Modifier
                            .duolingoButton(
                                faceColor = DuoFoxOrange,
                                lipColor = DuoFoxOrangeLip,
                                cornerRadius = 8.dp,
                                lipHeight = 2.dp
                            )
                            .instantTouchPress { onRandomizeAll() }
                            .padding(bottom = 2.dp)
                            .padding(horizontal = 7.dp, vertical = 4.dp)
                            .testTag("wardrobe_randomize_button")
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(2.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Shuffle,
                                contentDescription = "Acak",
                                tint = Color.White,
                                modifier = Modifier.size(11.dp)
                            )
                            Text(
                                text = "Acak",
                                fontSize = 8.5.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = Color.White
                            )
                        }
                    }

                    // Reset Polos
                    Box(
                        modifier = Modifier
                            .duolingoButton(
                                faceColor = DuoPureWhite,
                                lipColor = DuoHareLip,
                                cornerRadius = 8.dp,
                                lipHeight = 2.dp
                            )
                            .instantTouchPress { onResetDefault() }
                            .padding(bottom = 2.dp)
                            .padding(horizontal = 6.dp, vertical = 4.dp)
                            .testTag("wardrobe_reset_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = "Reset",
                            tint = DuoEelInk,
                            modifier = Modifier.size(12.dp)
                        )
                    }

                    if (onSetMascotOverlayVisible != null) {
                        Box(
                            modifier = Modifier
                                .duolingoButton(
                                    faceColor = if (isMascotOverlayVisible) DuoFeatherGreen else DuoMintSurface,
                                    lipColor = if (isMascotOverlayVisible) DuoFeatherGreenLip else DuoHareLip,
                                    cornerRadius = 8.dp,
                                    lipHeight = 2.dp
                                )
                                .instantTouchPress { onSetMascotOverlayVisible(!isMascotOverlayVisible) }
                                .padding(bottom = 2.dp)
                                .padding(horizontal = 6.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = if (isMascotOverlayVisible) "ON" else "OFF",
                                fontFamily = JetBrainsMonoFontFamily,
                                fontSize = 8.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = if (isMascotOverlayVisible) Color.White else DuoEelInk
                            )
                        }
                    }
                }
            }

            // Collapsible Search Bar (Responsive heightIn so text is never clipped on 720x1600px)
            if (isSearchExpanded) {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = {
                        Text(
                            text = "Cari baju, skin, saputangan, senjata, aksesoris, perabot...",
                            fontSize = 11.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = null,
                            tint = DuoMacawBlue,
                            modifier = Modifier.size(16.dp)
                        )
                    },
                    trailingIcon = {
                        if (searchQuery.isNotEmpty()) {
                            IconButton(onClick = { searchQuery = "" }) {
                                Icon(
                                    imageVector = Icons.Default.Clear,
                                    contentDescription = "Hapus",
                                    tint = CosmicTextSubtle,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(10.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = DuoMacawBlue,
                        unfocusedBorderColor = DuoSwanBorder,
                        focusedContainerColor = DuoPureWhite,
                        unfocusedContainerColor = DuoPureWhite
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 48.dp)
                        .testTag("wardrobe_search_input")
                )
            }

            // 2. COMPACT INTERACTIVE ROOM STAGE (176.dp height with 9 Furniture Categories + Pet + Quick Actions)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(176.dp)
                    .duolingoCard(
                        backgroundColor = wallStyle.topColor,
                        borderColor = wallStyle.accentColor,
                        lipColor = AppThemeController.darkenColor(wallStyle.accentColor, 0.68f),
                        cornerRadius = 16.dp,
                        borderWidth = 2.dp,
                        lipHeight = 3.dp
                    )
                    .padding(bottom = 3.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .testTag("mascot_interactive_room_stage")
            ) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    drawModularMascotRoomCanvas(
                        wallStyle = wallStyle,
                        floorStyle = floorStyle,
                        closetStyle = closetStyle,
                        lampStyle = lampStyle,
                        rugStyle = rugStyle,
                        deskStyle = deskStyle,
                        bedSofaStyle = bedSofaStyle,
                        windowDecorStyle = windowDecorStyle,
                        petStyle = petStyle
                    )
                }

                // Top Micro-HUD inside Stage: Quick Room Part, Pet & Aura Badges
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 6.dp, vertical = 5.dp)
                        .align(Alignment.TopCenter),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Box(
                            modifier = Modifier
                                .weight(1f, fill = false)
                                .clip(RoundedCornerShape(6.dp))
                                .background(Color.Black.copy(alpha = 0.58f))
                                .border(1.dp, lampStyle.glowColor.copy(alpha = 0.8f), RoundedCornerShape(6.dp))
                                .instantTouchPress { primaryGroupTab = 1 }
                                .padding(horizontal = 6.dp, vertical = 2.5.dp)
                        ) {
                            Text(
                                text = "AURA: ${activeAura.displayName}",
                                fontSize = 8.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = Color.White,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                        Box(
                            modifier = Modifier
                                .weight(1f, fill = false)
                                .clip(RoundedCornerShape(6.dp))
                                .background(Color.Black.copy(alpha = 0.58f))
                                .border(1.dp, petStyle.secondaryColor.copy(alpha = 0.8f), RoundedCornerShape(6.dp))
                                .instantTouchPress {
                                    primaryGroupTab = 3
                                    roomPartSubTab = 4
                                }
                                .padding(horizontal = 6.dp, vertical = 2.5.dp)
                        ) {
                            Text(
                                text = "PET: ${petStyle.displayName}",
                                fontSize = 8.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = Color.White,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(wallStyle.accentColor)
                            .instantTouchPress { primaryGroupTab = 3 }
                            .padding(horizontal = 7.dp, vertical = 2.5.dp)
                            .testTag("toggle_room_theme_picker_button")
                    ) {
                        Text(
                            text = "Perabot",
                            fontSize = 8.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color.White,
                            maxLines = 1
                        )
                    }
                }

                // Center Live Mascot Avatar
                Box(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(bottom = 12.dp)
                ) {
                    CosmicDragonAvatar(
                        mood = MascotMood.SUCCESS_LEAP,
                        size = 116.dp,
                        externalCheerKey = AppThemeController.poseTriggerNonce
                    )
                }

                // Bottom Quick-Action Micro Strip inside Stage
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.BottomCenter)
                        .background(Color.Black.copy(alpha = 0.64f))
                        .horizontalScroll(rememberScrollState())
                        .padding(horizontal = 5.dp, vertical = 4.dp),
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val quickActions = listOf(
                        "Elus" to MascotActivityPose.PETTING_BLISS,
                        "Boba" to MascotActivityPose.EATING_BOBA_SNACK,
                        "Game" to MascotActivityPose.GAMING_PRO_PAD,
                        "Pedang" to MascotActivityPose.KATANA_SLASH,
                        "Sihir" to MascotActivityPose.ARCANE_SPELLCAST,
                        "Gym" to MascotActivityPose.GYM_DUMBBELL_LIFT,
                        "Ngopi" to MascotActivityPose.COFFEE_CHILL,
                        "Mandi" to MascotActivityPose.BUBBLE_BATH_DUCK,
                        "Selfie" to MascotActivityPose.SELFIE_FLASH_POSE,
                        "Party" to MascotActivityPose.DANCING,
                        "Nonton" to MascotActivityPose.WATCHING_MEDIA,
                        "Tidur" to MascotActivityPose.SLEEPING
                    )
                    quickActions.forEach { (label, pose) ->
                        val isActivePose = activePose == pose
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(
                                    if (isActivePose) pose.badgeColor
                                    else Color.White.copy(alpha = 0.15f)
                                )
                                .instantTouchPress {
                                    AppThemeController.mascotActivityPose = pose
                                    AppThemeController.poseTriggerNonce++
                                }
                                .padding(horizontal = 6.dp, vertical = 2.5.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = label,
                                fontSize = 8.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = Color.White,
                                maxLines = 1
                            )
                        }
                    }
                }
            }

            // 3. TIER-1: 4 CLEAN FULL-WIDTH SEGMENTED TABS (Fluid heightIn for 720x1600px)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                val totalWardrobeCount = MascotUpperOutfit.entries.size +
                    MascotBodySkin.entries.size +
                    MascotHandGloves.entries.size +
                    MascotAccessory.entries.size +
                    MascotCostumePreset.entries.size
                val primaryTabs = listOf(
                    Triple(0, if (isCompactPhone) "Kostum" else "Kostum ($totalWardrobeCount)", DuoMacawBlue),
                    Triple(1, if (isCompactPhone) "Aura" else "Aura (${MascotAuraStyle.entries.size})", Color(0xFF06B6D4)),
                    Triple(2, "Ekspresi", Color(0xFFEC4899)),
                    Triple(3, if (isCompactPhone) "Kamar" else "Kamar (135)", Color(0xFF10B981))
                )
                primaryTabs.forEach { (idx, label, color) ->
                    val isSel = primaryGroupTab == idx
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .heightIn(min = 32.dp)
                            .duolingoButton(
                                faceColor = if (isSel) color else DuoMintSurface,
                                lipColor = if (isSel) AppThemeController.darkenColor(color, 0.72f) else DuoHareLip,
                                cornerRadius = 9.dp,
                                lipHeight = 2.dp
                            )
                            .instantTouchPress { primaryGroupTab = idx }
                            .padding(bottom = 2.dp)
                            .padding(horizontal = 3.dp, vertical = 5.dp)
                            .testTag("kamar_ganti_primary_tab_$idx"),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = label,
                            fontSize = 8.2.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = if (isSel) Color.White else DuoEelInk,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }

            // 4. TIER-2: SUB-CATEGORY PILLS + THEME FILTER PILLS (ULTRA-COMPACT)
            val q = searchQuery.trim().lowercase()

            when (primaryGroupTab) {
                0 -> {
                    // GROUP 0: LEMARI KOSTUM (5 Sub-Tabs: Baju 52, Skin 44, Tangan/Senjata 46, Aksesoris 52, Set 26)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        val subTabs = listOf(
                            Triple(0, if (isCompactPhone) "Baju" else "Baju (${MascotUpperOutfit.entries.size})", "wardrobe_tab_0"),
                            Triple(1, if (isCompactPhone) "Skin" else "Skin (${MascotBodySkin.entries.size})", "wardrobe_tab_1"),
                            Triple(2, if (isCompactPhone) "Tangan" else "Tangan (${MascotHandGloves.entries.size})", "wardrobe_tab_2"),
                            Triple(3, if (isCompactPhone) "Aksesori" else "Aksesoris (${MascotAccessory.entries.size})", "wardrobe_tab_3"),
                            Triple(4, if (isCompactPhone) "Set" else "Set (${MascotCostumePreset.entries.size})", "wardrobe_tab_8")
                        )
                        subTabs.forEach { (sIdx, sLabel, sTag) ->
                            val isSubSel = closetCategoryTab == sIdx
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .heightIn(min = 28.dp)
                                    .clip(RoundedCornerShape(7.dp))
                                    .background(if (isSubSel) DuoEelInk else DuoPureWhite)
                                    .border(
                                        1.dp,
                                        if (isSubSel) DuoEelInk else DuoSwanBorder,
                                        RoundedCornerShape(7.dp)
                                    )
                                    .instantTouchPress { closetCategoryTab = sIdx }
                                    .padding(horizontal = 2.dp, vertical = 4.dp)
                                    .testTag(sTag),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = sLabel,
                                    fontSize = 7.6.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = if (isSubSel) Color.White else DuoEelInk,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }
                    }

                    // Compact Theme Filter Strip (Only for Baju, Skin, Tangan, Aksesoris)
                    if (closetCategoryTab in 0..3) {
                        CompactThemeFilterRow(
                            selectedFilter = globalThemeFilter,
                            onSelectFilter = { globalThemeFilter = it }
                        )
                    }

                    // Compact Bounded Grid for the active Closet Sub-Tab
                    when (closetCategoryTab) {
                        0 -> {
                            val filteredOutfits = remember(globalThemeFilter, q) {
                                MascotUpperOutfit.entries.filter { item ->
                                    val matchesTheme = globalThemeFilter == "SEMUA" ||
                                        item == MascotUpperOutfit.NONE ||
                                        item.themeCategory == globalThemeFilter
                                    val matchesQuery = q.isEmpty() ||
                                        item.displayName.lowercase().contains(q) ||
                                        item.subtitle.lowercase().contains(q) ||
                                        item.themeCategory.lowercase().contains(q)
                                    matchesTheme && matchesQuery
                                }
                            }
                            CompactWardrobeGridContainer(
                                items = filteredOutfits,
                                columns = gridColumnCount,
                                isExpandedFull = isGridExpandedFull,
                                onToggleExpand = { isGridExpandedFull = !isGridExpandedFull },
                                equippedLabel = "${activeOutfit.displayName} — ${activeOutfit.subtitle}"
                            ) { outfitItem ->
                                WardrobeGridSlotCard(
                                    title = outfitItem.displayName,
                                    subtitle = outfitItem.subtitle,
                                    categoryTag = outfitItem.themeCategory,
                                    primaryColor = outfitItem.badgeColor,
                                    secondaryColor = AppThemeController.darkenColor(outfitItem.badgeColor, 0.68f),
                                    isSelected = activeOutfit == outfitItem,
                                    compactMode = gridColumnCount == 4,
                                    variantIndex = outfitItem.ordinal,
                                    onClick = { onSelectOutfit(outfitItem) },
                                    testTag = "mascot_outfit_chip_${outfitItem.id.lowercase()}",
                                    modifier = Modifier.weight(1f)
                                )
                            }
                        }

                        1 -> {
                            val filteredSkins = remember(globalThemeFilter, q) {
                                MascotBodySkin.entries.filter { item ->
                                    val matchesTheme = globalThemeFilter == "SEMUA" ||
                                        item == MascotBodySkin.DEFAULT_MOOD ||
                                        item.themeCategory == globalThemeFilter
                                    val matchesQuery = q.isEmpty() ||
                                        item.displayName.lowercase().contains(q) ||
                                        item.subtitle.lowercase().contains(q) ||
                                        item.themeCategory.lowercase().contains(q)
                                    matchesTheme && matchesQuery
                                }
                            }
                            CompactWardrobeGridContainer(
                                items = filteredSkins,
                                columns = gridColumnCount,
                                isExpandedFull = isGridExpandedFull,
                                onToggleExpand = { isGridExpandedFull = !isGridExpandedFull },
                                equippedLabel = "${activeSkin.displayName} — ${activeSkin.subtitle}"
                            ) { skinItem ->
                                WardrobeGridSlotCard(
                                    title = skinItem.displayName,
                                    subtitle = skinItem.subtitle,
                                    categoryTag = "SKIN",
                                    primaryColor = skinItem.previewColor,
                                    secondaryColor = skinItem.secondaryColor,
                                    isSelected = activeSkin == skinItem,
                                    compactMode = gridColumnCount == 4,
                                    variantIndex = skinItem.ordinal,
                                    onClick = { onSelectSkin(skinItem) },
                                    testTag = "mascot_skin_chip_${skinItem.id.lowercase()}",
                                    modifier = Modifier.weight(1f)
                                )
                            }
                        }

                        2 -> {
                            val filteredGloves = remember(globalThemeFilter, q) {
                                MascotHandGloves.entries.filter { item ->
                                    val matchesTheme = globalThemeFilter == "SEMUA" ||
                                        item == MascotHandGloves.DEFAULT_HANDS ||
                                        item.themeCategory == globalThemeFilter
                                    val matchesQuery = q.isEmpty() ||
                                        item.displayName.lowercase().contains(q) ||
                                        item.subtitle.lowercase().contains(q) ||
                                        item.themeCategory.lowercase().contains(q)
                                    matchesTheme && matchesQuery
                                }
                            }
                            CompactWardrobeGridContainer(
                                items = filteredGloves,
                                columns = gridColumnCount,
                                isExpandedFull = isGridExpandedFull,
                                onToggleExpand = { isGridExpandedFull = !isGridExpandedFull },
                                equippedLabel = "${activeGloves.displayName} — ${activeGloves.subtitle}"
                            ) { gloveItem ->
                                WardrobeGridSlotCard(
                                    title = gloveItem.displayName,
                                    subtitle = gloveItem.subtitle,
                                    categoryTag = gloveItem.themeCategory,
                                    primaryColor = gloveItem.badgeColor,
                                    secondaryColor = AppThemeController.darkenColor(gloveItem.badgeColor, 0.68f),
                                    isSelected = activeGloves == gloveItem,
                                    compactMode = gridColumnCount == 4,
                                    variantIndex = gloveItem.ordinal,
                                    onClick = { onSelectGloves(gloveItem) },
                                    testTag = "mascot_gloves_chip_${gloveItem.id.lowercase()}",
                                    modifier = Modifier.weight(1f)
                                )
                            }
                        }

                        3 -> {
                            val filteredAccessories = remember(globalThemeFilter, q) {
                                MascotAccessory.entries.filter { item ->
                                    val matchesTheme = globalThemeFilter == "SEMUA" ||
                                        item == MascotAccessory.NONE ||
                                        item.themeCategory == globalThemeFilter
                                    val matchesQuery = q.isEmpty() ||
                                        item.displayName.lowercase().contains(q) ||
                                        item.themeCategory.lowercase().contains(q)
                                    matchesTheme && matchesQuery
                                }
                            }
                            CompactWardrobeGridContainer(
                                items = filteredAccessories,
                                columns = gridColumnCount,
                                isExpandedFull = isGridExpandedFull,
                                onToggleExpand = { isGridExpandedFull = !isGridExpandedFull },
                                equippedLabel = "${activeAccessory.displayName} (Gaya ${activeAccessory.themeCategory})"
                            ) { acc ->
                                WardrobeGridSlotCard(
                                    title = acc.displayName,
                                    subtitle = "Gaya ${acc.themeCategory}",
                                    categoryTag = acc.themeCategory,
                                    primaryColor = acc.badgeColor,
                                    secondaryColor = AppThemeController.darkenColor(acc.badgeColor, 0.68f),
                                    isSelected = activeAccessory == acc,
                                    compactMode = gridColumnCount == 4,
                                    variantIndex = acc.ordinal,
                                    onClick = { onSelectAccessory(acc) },
                                    testTag = "mascot_accessory_chip_${acc.id.lowercase()}",
                                    modifier = Modifier.weight(1f)
                                )
                            }
                        }

                        else -> {
                            CompactWardrobeGridContainer(
                                items = MascotCostumePreset.entries,
                                columns = if (isCompactPhone) 2 else 3,
                                isExpandedFull = isGridExpandedFull,
                                onToggleExpand = { isGridExpandedFull = !isGridExpandedFull },
                                equippedLabel = "Pilih Set Kostum 4-in-1 (Baju + Skin + Tangan + Aksesoris)"
                            ) { preset ->
                                val isPresetActive = activeSkin == preset.skin &&
                                    activeOutfit == preset.outfit &&
                                    activeGloves == preset.gloves &&
                                    activeAccessory == preset.accessory
                                WardrobeGridSlotCard(
                                    title = preset.displayName,
                                    subtitle = preset.subtitle,
                                    categoryTag = "SET",
                                    primaryColor = preset.accentColor,
                                    secondaryColor = AppThemeController.darkenColor(preset.accentColor, 0.68f),
                                    isSelected = isPresetActive,
                                    compactMode = false,
                                    variantIndex = preset.ordinal,
                                    onClick = { onApplyPreset(preset) },
                                    testTag = "wardrobe_preset_${preset.id.lowercase()}",
                                    modifier = Modifier.weight(1f)
                                )
                            }
                        }
                    }
                }

                1 -> {
                    // GROUP 1: 16 AURA KOSMIK & ELEMEN BERLAPIS
                    CompactThemeFilterRow(
                        selectedFilter = globalThemeFilter,
                        onSelectFilter = { globalThemeFilter = it }
                    )
                    val filteredAuras = remember(globalThemeFilter, q) {
                        MascotAuraStyle.entries.filter { item ->
                            val matchesTheme = globalThemeFilter == "SEMUA" ||
                                item == MascotAuraStyle.NONE ||
                                item.themeCategory == globalThemeFilter
                            val matchesQuery = q.isEmpty() ||
                                item.displayName.lowercase().contains(q) ||
                                item.subtitle.lowercase().contains(q)
                            matchesTheme && matchesQuery
                        }
                    }
                    CompactWardrobeGridContainer(
                        items = filteredAuras,
                        columns = gridColumnCount,
                        isExpandedFull = isGridExpandedFull,
                        onToggleExpand = { isGridExpandedFull = !isGridExpandedFull },
                        equippedLabel = "${activeAura.displayName} — ${activeAura.subtitle}"
                    ) { auraItem ->
                        WardrobeGridSlotCard(
                            title = auraItem.displayName,
                            subtitle = auraItem.subtitle,
                            categoryTag = "AURA",
                            primaryColor = auraItem.primaryColor,
                            secondaryColor = auraItem.secondaryColor,
                            isSelected = activeAura == auraItem,
                            compactMode = gridColumnCount == 4,
                            variantIndex = auraItem.ordinal,
                            onClick = {
                                AppThemeController.mascotAura = auraItem
                                AppThemeController.mascotCosmicAuraEnabled = (auraItem != MascotAuraStyle.NONE)
                                AppThemeController.poseTriggerNonce++
                            },
                            testTag = "mascot_aura_chip_${auraItem.id.lowercase()}",
                            modifier = Modifier.weight(1f)
                        )
                    }
                }

                2 -> {
                    // GROUP 2: EMOSI WAJAH (20) & INTERAKSI POSE (16)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(5.dp)
                    ) {
                        val expTabs = listOf(
                            0 to "Emosi Wajah (20)",
                            1 to "Pose & Interaksi (16)"
                        )
                        expTabs.forEach { (eIdx, eLabel) ->
                            val isExpSel = expressionSubTab == eIdx
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .heightIn(min = 28.dp)
                                    .clip(RoundedCornerShape(7.dp))
                                    .background(if (isExpSel) DuoEelInk else DuoPureWhite)
                                    .border(1.dp, if (isExpSel) DuoEelInk else DuoSwanBorder, RoundedCornerShape(7.dp))
                                    .instantTouchPress { expressionSubTab = eIdx }
                                    .padding(horizontal = 4.dp, vertical = 4.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = eLabel,
                                    fontSize = 8.2.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = if (isExpSel) Color.White else DuoEelInk,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }
                    }

                    if (expressionSubTab == 0) {
                        CompactWardrobeGridContainer(
                            items = MascotEmotionPreset.entries,
                            columns = gridColumnCount,
                            isExpandedFull = isGridExpandedFull,
                            onToggleExpand = { isGridExpandedFull = !isGridExpandedFull },
                            equippedLabel = "${activeEmotion.displayName} — ${activeEmotion.subtitle}"
                        ) { emo ->
                            WardrobeGridSlotCard(
                                title = emo.displayName,
                                subtitle = emo.subtitle,
                                categoryTag = "EMOSI",
                                primaryColor = emo.badgeColor,
                                secondaryColor = AppThemeController.darkenColor(emo.badgeColor, 0.68f),
                                isSelected = activeEmotion == emo,
                                compactMode = gridColumnCount == 4,
                                variantIndex = emo.ordinal,
                                onClick = {
                                    AppThemeController.mascotEmotion = emo
                                    AppThemeController.poseTriggerNonce++
                                },
                                testTag = "mascot_emotion_chip_${emo.id.lowercase()}",
                                modifier = Modifier.weight(1f)
                            )
                        }
                    } else {
                        CompactWardrobeGridContainer(
                            items = MascotActivityPose.entries,
                            columns = gridColumnCount,
                            isExpandedFull = isGridExpandedFull,
                            onToggleExpand = { isGridExpandedFull = !isGridExpandedFull },
                            equippedLabel = "${activePose.displayName} — ${activePose.subtitle}"
                        ) { pose ->
                            WardrobeGridSlotCard(
                                title = pose.displayName,
                                subtitle = pose.subtitle,
                                categoryTag = "AKSI",
                                primaryColor = pose.badgeColor,
                                secondaryColor = AppThemeController.darkenColor(pose.badgeColor, 0.68f),
                                isSelected = activePose == pose,
                                compactMode = gridColumnCount == 4,
                                variantIndex = pose.ordinal,
                                onClick = {
                                    AppThemeController.mascotActivityPose = pose
                                    AppThemeController.poseTriggerNonce++
                                },
                                testTag = "mascot_pose_${pose.id.lowercase()}",
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }

                else -> {
                    // GROUP 3: DEKOR KAMAR PER BAGIAN (9 KATEGORI PERABOT × 15 OPSI + PRESET KAMAR)
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        val partTabs = listOf(
                            "Lemari (15)",
                            "Meja & PC (15)",
                            "Sofa & Kasur (15)",
                            "Jendela & Rak (15)",
                            "Pet Kamar (15)",
                            "Lantai (15)",
                            "Lampu (15)",
                            "Dinding (15)",
                            "Karpet (15)",
                            "Preset (6)"
                        )
                        partTabs.forEachIndexed { pIdx, pLabel ->
                            val isPartSel = roomPartSubTab == pIdx
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(7.dp))
                                    .background(if (isPartSel) DuoEelInk else DuoPureWhite)
                                    .border(1.dp, if (isPartSel) DuoEelInk else DuoSwanBorder, RoundedCornerShape(7.dp))
                                    .instantTouchPress { roomPartSubTab = pIdx }
                                    .padding(horizontal = 8.dp, vertical = 5.dp)
                                    .testTag("room_part_subtab_$pIdx")
                            ) {
                                Text(
                                    text = pLabel,
                                    fontSize = 8.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = if (isPartSel) Color.White else DuoEelInk
                                )
                            }
                        }
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(7.dp))
                                .background(DuoFoxOrange)
                                .instantTouchPress { AppThemeController.randomizeRoomParts() }
                                .padding(horizontal = 8.dp, vertical = 5.dp)
                                .testTag("room_randomize_all_parts_button")
                        ) {
                            Text(
                                text = "Acak 9 Perabot",
                                fontSize = 8.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = Color.White
                            )
                        }
                    }

                    when (roomPartSubTab) {
                        0 -> CompactWardrobeGridContainer(
                            items = MascotRoomClosetStyle.entries,
                            columns = gridColumnCount,
                            isExpandedFull = isGridExpandedFull,
                            onToggleExpand = { isGridExpandedFull = !isGridExpandedFull },
                            equippedLabel = "Lemari: ${closetStyle.displayName} — ${closetStyle.subtitle}"
                        ) { item ->
                            WardrobeGridSlotCard(
                                title = item.displayName,
                                subtitle = item.subtitle,
                                categoryTag = "LEMARI",
                                primaryColor = item.trimColor,
                                secondaryColor = item.bodyColor,
                                isSelected = closetStyle == item,
                                compactMode = gridColumnCount == 4,
                                variantIndex = item.ordinal,
                                onClick = { AppThemeController.roomClosetStyle = item },
                                testTag = "room_closet_${item.id.lowercase()}",
                                modifier = Modifier.weight(1f)
                            )
                        }
                        1 -> CompactWardrobeGridContainer(
                            items = MascotRoomDeskStyle.entries,
                            columns = gridColumnCount,
                            isExpandedFull = isGridExpandedFull,
                            onToggleExpand = { isGridExpandedFull = !isGridExpandedFull },
                            equippedLabel = "Meja & PC: ${deskStyle.displayName} — ${deskStyle.subtitle}"
                        ) { item ->
                            WardrobeGridSlotCard(
                                title = item.displayName,
                                subtitle = item.subtitle,
                                categoryTag = "MEJA/PC",
                                primaryColor = item.accentColor,
                                secondaryColor = item.primaryColor,
                                isSelected = deskStyle == item,
                                compactMode = gridColumnCount == 4,
                                variantIndex = item.ordinal,
                                onClick = { AppThemeController.roomDeskStyle = item },
                                testTag = "room_desk_${item.id.lowercase()}",
                                modifier = Modifier.weight(1f)
                            )
                        }
                        2 -> CompactWardrobeGridContainer(
                            items = MascotRoomBedSofaStyle.entries,
                            columns = gridColumnCount,
                            isExpandedFull = isGridExpandedFull,
                            onToggleExpand = { isGridExpandedFull = !isGridExpandedFull },
                            equippedLabel = "Sofa & Kasur: ${bedSofaStyle.displayName} — ${bedSofaStyle.subtitle}"
                        ) { item ->
                            WardrobeGridSlotCard(
                                title = item.displayName,
                                subtitle = item.subtitle,
                                categoryTag = "SOFA",
                                primaryColor = item.accentColor,
                                secondaryColor = item.primaryColor,
                                isSelected = bedSofaStyle == item,
                                compactMode = gridColumnCount == 4,
                                variantIndex = item.ordinal,
                                onClick = { AppThemeController.roomBedSofaStyle = item },
                                testTag = "room_bedsofa_${item.id.lowercase()}",
                                modifier = Modifier.weight(1f)
                            )
                        }
                        3 -> CompactWardrobeGridContainer(
                            items = MascotRoomWindowDecorStyle.entries,
                            columns = gridColumnCount,
                            isExpandedFull = isGridExpandedFull,
                            onToggleExpand = { isGridExpandedFull = !isGridExpandedFull },
                            equippedLabel = "Jendela & Rak: ${windowDecorStyle.displayName} — ${windowDecorStyle.subtitle}"
                        ) { item ->
                            WardrobeGridSlotCard(
                                title = item.displayName,
                                subtitle = item.subtitle,
                                categoryTag = "DEKOR",
                                primaryColor = item.accentColor,
                                secondaryColor = item.primaryColor,
                                isSelected = windowDecorStyle == item,
                                compactMode = gridColumnCount == 4,
                                variantIndex = item.ordinal,
                                onClick = { AppThemeController.roomWindowDecorStyle = item },
                                testTag = "room_window_${item.id.lowercase()}",
                                modifier = Modifier.weight(1f)
                            )
                        }
                        4 -> CompactWardrobeGridContainer(
                            items = MascotRoomPetStyle.entries,
                            columns = gridColumnCount,
                            isExpandedFull = isGridExpandedFull,
                            onToggleExpand = { isGridExpandedFull = !isGridExpandedFull },
                            equippedLabel = "Pet Kamar: ${petStyle.displayName} — ${petStyle.subtitle}"
                        ) { item ->
                            WardrobeGridSlotCard(
                                title = item.displayName,
                                subtitle = item.subtitle,
                                categoryTag = "PET",
                                primaryColor = item.primaryColor,
                                secondaryColor = item.secondaryColor,
                                isSelected = petStyle == item,
                                compactMode = gridColumnCount == 4,
                                variantIndex = item.ordinal,
                                onClick = { AppThemeController.roomPetStyle = item },
                                testTag = "room_pet_${item.id.lowercase()}",
                                modifier = Modifier.weight(1f)
                            )
                        }
                        5 -> CompactWardrobeGridContainer(
                            items = MascotRoomFloorStyle.entries,
                            columns = gridColumnCount,
                            isExpandedFull = isGridExpandedFull,
                            onToggleExpand = { isGridExpandedFull = !isGridExpandedFull },
                            equippedLabel = "Lantai: ${floorStyle.displayName} — ${floorStyle.subtitle}"
                        ) { item ->
                            WardrobeGridSlotCard(
                                title = item.displayName,
                                subtitle = item.subtitle,
                                categoryTag = "LANTAI",
                                primaryColor = item.gridLineColor,
                                secondaryColor = item.primaryColor,
                                isSelected = floorStyle == item,
                                compactMode = gridColumnCount == 4,
                                variantIndex = item.ordinal,
                                onClick = { AppThemeController.roomFloorStyle = item },
                                testTag = "room_floor_${item.id.lowercase()}",
                                modifier = Modifier.weight(1f)
                            )
                        }
                        6 -> CompactWardrobeGridContainer(
                            items = MascotRoomLampStyle.entries,
                            columns = gridColumnCount,
                            isExpandedFull = isGridExpandedFull,
                            onToggleExpand = { isGridExpandedFull = !isGridExpandedFull },
                            equippedLabel = "Lampu: ${lampStyle.displayName} — ${lampStyle.subtitle}"
                        ) { item ->
                            WardrobeGridSlotCard(
                                title = item.displayName,
                                subtitle = item.subtitle,
                                categoryTag = "LAMPU",
                                primaryColor = item.glowColor,
                                secondaryColor = item.fixtureColor,
                                isSelected = lampStyle == item,
                                compactMode = gridColumnCount == 4,
                                variantIndex = item.ordinal,
                                onClick = { AppThemeController.roomLampStyle = item },
                                testTag = "room_lamp_${item.id.lowercase()}",
                                modifier = Modifier.weight(1f)
                            )
                        }
                        7 -> CompactWardrobeGridContainer(
                            items = MascotRoomWallStyle.entries,
                            columns = gridColumnCount,
                            isExpandedFull = isGridExpandedFull,
                            onToggleExpand = { isGridExpandedFull = !isGridExpandedFull },
                            equippedLabel = "Dinding: ${wallStyle.displayName} — ${wallStyle.subtitle}"
                        ) { item ->
                            WardrobeGridSlotCard(
                                title = item.displayName,
                                subtitle = item.subtitle,
                                categoryTag = "DINDING",
                                primaryColor = item.accentColor,
                                secondaryColor = item.topColor,
                                isSelected = wallStyle == item,
                                compactMode = gridColumnCount == 4,
                                variantIndex = item.ordinal,
                                onClick = { AppThemeController.roomWallStyle = item },
                                testTag = "room_wall_${item.id.lowercase()}",
                                modifier = Modifier.weight(1f)
                            )
                        }
                        8 -> CompactWardrobeGridContainer(
                            items = MascotRoomRugStyle.entries,
                            columns = gridColumnCount,
                            isExpandedFull = isGridExpandedFull,
                            onToggleExpand = { isGridExpandedFull = !isGridExpandedFull },
                            equippedLabel = "Karpet: ${rugStyle.displayName} — ${rugStyle.subtitle}"
                        ) { item ->
                            WardrobeGridSlotCard(
                                title = item.displayName,
                                subtitle = item.subtitle,
                                categoryTag = "KARPET",
                                primaryColor = item.ringColor,
                                secondaryColor = item.mainColor,
                                isSelected = rugStyle == item,
                                compactMode = gridColumnCount == 4,
                                variantIndex = item.ordinal,
                                onClick = { AppThemeController.roomRugStyle = item },
                                testTag = "room_rug_${item.id.lowercase()}",
                                modifier = Modifier.weight(1f)
                            )
                        }
                        else -> CompactWardrobeGridContainer(
                            items = MascotRoomTheme.entries,
                            columns = if (isCompactPhone) 2 else 3,
                            isExpandedFull = isGridExpandedFull,
                            onToggleExpand = { isGridExpandedFull = !isGridExpandedFull },
                            equippedLabel = "Preset Kamar: ${AppThemeController.mascotRoomTheme.displayName}"
                        ) { room ->
                            WardrobeGridSlotCard(
                                title = room.displayName,
                                subtitle = room.subtitle,
                                categoryTag = "FULL SET",
                                primaryColor = room.accentColor,
                                secondaryColor = room.wallTopColor,
                                isSelected = AppThemeController.mascotRoomTheme == room,
                                compactMode = false,
                                variantIndex = room.ordinal,
                                onClick = { AppThemeController.applyRoomThemePreset(room) },
                                testTag = "room_theme_${room.id.lowercase()}",
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun CompactThemeFilterRow(
    selectedFilter: String,
    onSelectFilter: (String) -> Unit
) {
    val themeFilters = listOf(
        "SEMUA" to "Semua",
        "ANIME" to "Anime Collab",
        "HERO" to "Super Hero",
        "FANTASI" to "Fantasi",
        "SIBER" to "Siber",
        "SULTAN" to "Sultan",
        "MODERN" to "Kasual",
        "LUCU" to "Lucu"
    )
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        themeFilters.forEach { (tfKey, tfLabel) ->
            val isFilterSel = selectedFilter == tfKey
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(6.dp))
                    .background(if (isFilterSel) DuoMacawBlue else DuoMintSurface)
                    .border(
                        1.dp,
                        if (isFilterSel) DuoMacawBlueLip else DuoSwanBorder,
                        RoundedCornerShape(6.dp)
                    )
                    .instantTouchPress { onSelectFilter(tfKey) }
                    .padding(horizontal = 7.dp, vertical = 3.dp)
            ) {
                Text(
                    text = tfLabel,
                    fontFamily = JetBrainsMonoFontFamily,
                    fontSize = 7.8.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = if (isFilterSel) Color.White else DuoEelInk
                )
            }
        }
    }
}

/**
 * Bounded Compact Grid Drawer with Status Footer & Expand/Collapse Toggle
 * Prevents 22–28 items from stretching the screen while keeping every item 1-tap accessible.
 */
@Composable
private fun <T> CompactWardrobeGridContainer(
    items: List<T>,
    columns: Int,
    isExpandedFull: Boolean,
    onToggleExpand: () -> Unit,
    equippedLabel: String,
    itemContent: @Composable androidx.compose.foundation.layout.RowScope.(T) -> Unit
) {
    val gridScrollState = rememberScrollState()
    Column(verticalArrangement = Arrangement.spacedBy(5.dp)) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .then(
                    if (isExpandedFull) Modifier
                    else Modifier
                        .heightIn(max = 196.dp)
                        .verticalScroll(gridScrollState)
                )
                .clip(RoundedCornerShape(12.dp))
                .background(DuoMintSurface.copy(alpha = 0.55f))
                .border(1.dp, DuoSwanBorder, RoundedCornerShape(12.dp))
                .padding(5.dp)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(5.dp)) {
                items.chunked(columns).forEach { rowItems ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(5.dp)
                    ) {
                        rowItems.forEach { item ->
                            itemContent(item)
                        }
                        repeat(columns - rowItems.size) {
                            Spacer(modifier = Modifier.weight(1f))
                        }
                    }
                }
            }
        }

        // Minimalist Status & Expand Toggle Footer Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 2.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "AKTIF: $equippedLabel",
                fontSize = 8.5.sp,
                fontWeight = FontWeight.Bold,
                color = CosmicTextSubtle,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(1f)
            )
            if (items.size > columns * 3) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(DuoMintSurface)
                        .border(1.dp, DuoSwanBorder, RoundedCornerShape(6.dp))
                        .instantTouchPress { onToggleExpand() }
                        .padding(horizontal = 7.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = if (isExpandedFull) "Ringkas (${items.size})" else "Semua (${items.size})",
                        fontFamily = JetBrainsMonoFontFamily,
                        fontSize = 7.8.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = DuoMacawBlue
                    )
                }
            }
        }
    }
}

/**
 * Kartu Slot Lemari Grid 4-Kolom / 3-Kolom / 2-Kolom Bergaya Duolingo 3D Colorful & Skeuomorphic.
 * Menampilkan ilustrasi ikon 3D skeuomorphic mini yang unik per variasi item (`variantIndex`).
 */
@Composable
private fun WardrobeGridSlotCard(
    title: String,
    subtitle: String,
    categoryTag: String,
    primaryColor: Color,
    secondaryColor: Color,
    isSelected: Boolean,
    compactMode: Boolean = true,
    variantIndex: Int = 0,
    onClick: () -> Unit,
    testTag: String,
    modifier: Modifier = Modifier
) {
    val cardFaceColor = if (isSelected) {
        primaryColor
    } else {
        Color(
            red = primaryColor.red * 0.22f + 0.78f,
            green = primaryColor.green * 0.22f + 0.78f,
            blue = primaryColor.blue * 0.22f + 0.78f,
            alpha = 1f
        )
    }
    val cardLipColor = if (isSelected) {
        AppThemeController.darkenColor(primaryColor, 0.64f)
    } else {
        primaryColor.copy(alpha = 0.85f)
    }

    Box(
        modifier = modifier
            .heightIn(min = if (compactMode) 48.dp else 56.dp)
            .duolingoButton(
                faceColor = cardFaceColor,
                lipColor = cardLipColor,
                cornerRadius = if (compactMode) 11.dp else 13.dp,
                lipHeight = if (isSelected) 4.dp else 3.dp
            )
            .instantTouchPress { onClick() }
            .padding(bottom = if (isSelected) 4.dp else 3.dp)
            .padding(
                horizontal = if (compactMode) 5.dp else 6.dp,
                vertical = if (compactMode) 5.dp else 6.dp
            )
            .testTag(testTag)
    ) {
        if (compactMode) {
            Column(
                verticalArrangement = Arrangement.spacedBy(3.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Canvas(modifier = Modifier.size(20.dp)) {
                        drawSkeuomorphicWardrobeSlotIcon(
                            categoryTag = categoryTag,
                            primaryColor = primaryColor,
                            secondaryColor = secondaryColor,
                            isSelected = isSelected,
                            variantIndex = variantIndex
                        )
                    }
                    if (isSelected) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = "Dipakai",
                            tint = Color.White,
                            modifier = Modifier.size(12.dp)
                        )
                    } else {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(4.dp))
                                .background(primaryColor.copy(alpha = 0.18f))
                                .padding(horizontal = 3.5.dp, vertical = 1.dp)
                        ) {
                            Text(
                                text = categoryTag.take(5),
                                fontFamily = JetBrainsMonoFontFamily,
                                fontSize = 5.8.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = AppThemeController.darkenColor(primaryColor, 0.58f),
                                maxLines = 1
                            )
                        }
                    }
                }
                Text(
                    text = title,
                    style = MaterialTheme.typography.labelSmall,
                    fontSize = 8.2.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = if (isSelected) Color.White else Color(0xFF1E293B),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        } else {
            Column(
                verticalArrangement = Arrangement.spacedBy(3.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Canvas(modifier = Modifier.size(24.dp)) {
                        drawSkeuomorphicWardrobeSlotIcon(
                            categoryTag = categoryTag,
                            primaryColor = primaryColor,
                            secondaryColor = secondaryColor,
                            isSelected = isSelected,
                            variantIndex = variantIndex
                        )
                    }
                    if (isSelected) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = "Dipakai",
                            tint = Color.White,
                            modifier = Modifier.size(13.dp)
                        )
                    } else {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(4.dp))
                                .background(primaryColor.copy(alpha = 0.18f))
                                .padding(horizontal = 4.dp, vertical = 1.dp)
                        ) {
                            Text(
                                text = categoryTag,
                                fontFamily = JetBrainsMonoFontFamily,
                                fontSize = 6.2.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = AppThemeController.darkenColor(primaryColor, 0.58f),
                                maxLines = 1
                            )
                        }
                    }
                }
                Text(
                    text = title,
                    style = MaterialTheme.typography.labelSmall,
                    fontSize = 9.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = if (isSelected) Color.White else Color(0xFF1E293B),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.fillMaxWidth()
                )
                Text(
                    text = if (isSelected) "AKTIF" else subtitle,
                    style = MaterialTheme.typography.labelSmall,
                    fontSize = 7.2.sp,
                    fontWeight = if (isSelected) FontWeight.ExtraBold else FontWeight.SemiBold,
                    color = if (isSelected) Color.White.copy(alpha = 0.94f) else Color(0xFF475569),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

/**
 * Draws a rich 3D Duolingo-Skeuomorphic miniature preview icon inside each wardrobe/room slot card
 * with distinct silhouettes/textures based on `variantIndex`.
 */
private fun DrawScope.drawSkeuomorphicWardrobeSlotIcon(
    categoryTag: String,
    primaryColor: Color,
    secondaryColor: Color,
    isSelected: Boolean,
    variantIndex: Int = 0
) {
    val w = size.width
    val h = size.height
    val cx = w * 0.5f
    val cy = h * 0.5f
    val mainCol = if (isSelected) Color.White else primaryColor
    val subCol = if (isSelected) Color(0xFFFEF08A) else secondaryColor
    val lipCol = AppThemeController.darkenColor(primaryColor, 0.58f)

    // Soft 3D Badge Pedestal Background
    drawRoundRect(
        color = lipCol.copy(alpha = 0.45f),
        topLeft = Offset(1f, 2.5f),
        size = Size(w - 2f, h - 2.5f),
        cornerRadius = CornerRadius(6f, 6f)
    )
    drawRoundRect(
        brush = Brush.verticalGradient(
            listOf(
                AppThemeController.lightenColor(primaryColor, 0.25f),
                primaryColor,
                AppThemeController.darkenColor(primaryColor, 0.72f)
            )
        ),
        topLeft = Offset(1f, 1f),
        size = Size(w - 2f, h - 3.5f),
        cornerRadius = CornerRadius(6f, 6f)
    )

    when (categoryTag) {
        "LEMARI" -> {
            when (variantIndex % 6) {
                0 -> {
                    // Royal Double-Door Armoire
                    drawRoundRect(subCol, Offset(w * 0.22f, h * 0.16f), Size(w * 0.56f, h * 0.64f), CornerRadius(3f, 3f))
                    drawLine(lipCol, Offset(cx, h * 0.18f), Offset(cx, h * 0.78f), strokeWidth = 1.5f)
                    drawCircle(Color(0xFFFACC15), radius = 1.6f, center = Offset(cx - 2.2f, cy))
                    drawCircle(Color(0xFFFACC15), radius = 1.6f, center = Offset(cx + 2.2f, cy))
                }
                1 -> {
                    // Hexagonal Mecha Hangar Bay
                    val p = Path().apply {
                        moveTo(w * 0.30f, h * 0.16f)
                        lineTo(w * 0.70f, h * 0.16f)
                        lineTo(w * 0.82f, cy)
                        lineTo(w * 0.70f, h * 0.80f)
                        lineTo(w * 0.30f, h * 0.80f)
                        lineTo(w * 0.18f, cy)
                        close()
                    }
                    drawPath(p, color = subCol)
                    drawCircle(Color(0xFF00F2FE), radius = 2.4f, center = Offset(cx, cy))
                }
                2 -> {
                    // Cylindrical Glass Capsule Tube
                    drawOval(Color.White, Offset(w * 0.26f, h * 0.14f), Size(w * 0.48f, h * 0.16f))
                    drawRoundRect(Color.White.copy(alpha = 0.65f), Offset(w * 0.26f, h * 0.20f), Size(w * 0.48f, h * 0.56f), CornerRadius(6f, 6f))
                    drawOval(Color(0xFFFACC15), Offset(w * 0.30f, h * 0.42f), Size(w * 0.40f, h * 0.14f), style = Stroke(width = 1.6f))
                }
                3 -> {
                    // Japanese Pagoda Roof Cabinet
                    val roof = Path().apply {
                        moveTo(w * 0.12f, h * 0.30f)
                        quadraticTo(cx, h * 0.08f, w * 0.88f, h * 0.30f)
                        close()
                    }
                    drawPath(roof, color = Color(0xFFFACC15))
                    drawRoundRect(Color(0xFFFFFBEB), Offset(w * 0.24f, h * 0.30f), Size(w * 0.52f, h * 0.48f), CornerRadius(2f, 2f))
                    drawLine(lipCol, Offset(cx, h * 0.30f), Offset(cx, h * 0.78f), strokeWidth = 1.4f)
                }
                4 -> {
                    // Vault Safe with Combination Wheel
                    drawRoundRect(subCol, Offset(w * 0.20f, h * 0.18f), Size(w * 0.60f, h * 0.60f), CornerRadius(4f, 4f))
                    drawCircle(Color(0xFFFACC15), radius = w * 0.18f, center = Offset(cx, cy), style = Stroke(width = 1.8f))
                    drawLine(Color(0xFFFACC15), Offset(cx - 3.5f, cy), Offset(cx + 3.5f, cy), strokeWidth = 1.5f)
                    drawLine(Color(0xFFFACC15), Offset(cx, cy - 3.5f), Offset(cx, cy + 3.5f), strokeWidth = 1.5f)
                }
                else -> {
                    // Gothic / Cloud Pointed Arch Closet
                    val arch = Path().apply {
                        moveTo(w * 0.22f, h * 0.80f)
                        lineTo(w * 0.22f, h * 0.38f)
                        quadraticTo(cx, h * 0.10f, w * 0.78f, h * 0.38f)
                        lineTo(w * 0.78f, h * 0.80f)
                        close()
                    }
                    drawPath(arch, color = subCol)
                    drawCircle(Color.White, radius = 2.2f, center = Offset(cx, h * 0.46f))
                }
            }
        }
        "KARPET" -> {
            when (variantIndex % 6) {
                0 -> {
                    // Rectangular Persian Carpet with Fringes
                    drawRect(subCol, Offset(w * 0.20f, h * 0.28f), Size(w * 0.60f, h * 0.42f))
                    drawRect(Color(0xFFFACC15), Offset(w * 0.24f, h * 0.32f), Size(w * 0.52f, h * 0.34f), style = Stroke(width = 1.4f))
                    drawLine(Color.White, Offset(w * 0.12f, h * 0.36f), Offset(w * 0.20f, h * 0.36f), strokeWidth = 1.5f)
                    drawLine(Color.White, Offset(w * 0.80f, h * 0.36f), Offset(w * 0.88f, h * 0.36f), strokeWidth = 1.5f)
                }
                1 -> {
                    // Magic Star Rune Circle
                    drawCircle(subCol, radius = w * 0.32f, center = Offset(cx, cy))
                    drawCircle(Color.White, radius = w * 0.28f, center = Offset(cx, cy), style = Stroke(width = 1.4f))
                    drawCircle(Color(0xFFFACC15), radius = 2.5f, center = Offset(cx, cy))
                }
                2 -> {
                    // Fluffy Flower / Cloud Scalloped Rug
                    for (a in 0..5) {
                        val rad = Math.toRadians((a * 60).toDouble())
                        drawCircle(Color.White, radius = w * 0.14f, center = Offset(cx + kotlin.math.cos(rad).toFloat() * 4f, cy + kotlin.math.sin(rad).toFloat() * 4f))
                    }
                    drawCircle(Color(0xFFFACC15), radius = w * 0.14f, center = Offset(cx, cy))
                }
                3 -> {
                    // Hexagonal Cyber Pad
                    val hex = Path().apply {
                        for (i in 0..5) {
                            val rad = Math.toRadians((i * 60).toDouble())
                            val hx = cx + kotlin.math.cos(rad).toFloat() * (w * 0.32f)
                            val hy = cy + kotlin.math.sin(rad).toFloat() * (h * 0.30f)
                            if (i == 0) moveTo(hx, hy) else lineTo(hx, hy)
                        }
                        close()
                    }
                    drawPath(hex, color = subCol)
                    drawPath(hex, color = Color.White, style = Stroke(width = 1.5f))
                }
                4 -> {
                    // Cute Cat Paw Rug with Toe Beans
                    drawOval(Color.White, Offset(w * 0.24f, h * 0.42f), Size(w * 0.52f, h * 0.36f))
                    drawCircle(Color.White, radius = 2.2f, center = Offset(w * 0.28f, h * 0.28f))
                    drawCircle(Color.White, radius = 2.2f, center = Offset(cx, h * 0.24f))
                    drawCircle(Color.White, radius = 2.2f, center = Offset(w * 0.72f, h * 0.28f))
                }
                else -> {
                    // Checkered Pixel Rug
                    drawRoundRect(Color.White, Offset(w * 0.18f, h * 0.28f), Size(w * 0.30f, h * 0.22f), CornerRadius(1.5f, 1.5f))
                    drawRoundRect(subCol, Offset(w * 0.50f, h * 0.28f), Size(w * 0.30f, h * 0.22f), CornerRadius(1.5f, 1.5f))
                    drawRoundRect(subCol, Offset(w * 0.18f, h * 0.50f), Size(w * 0.30f, h * 0.22f), CornerRadius(1.5f, 1.5f))
                    drawRoundRect(Color.White, Offset(w * 0.50f, h * 0.50f), Size(w * 0.30f, h * 0.22f), CornerRadius(1.5f, 1.5f))
                }
            }
        }
        "SKIN" -> {
            // Distinct Texture Swatch Preview per Skin (Scales, Stripes, Galaxy Stars, Circuit, Cracking Magma, Plaid)
            drawCircle(subCol, radius = w * 0.32f, center = Offset(cx, cy))
            when (variantIndex % 6) {
                0 -> {
                    // Dragon Scales / Polka Dots
                    drawCircle(Color.White, radius = 2f, center = Offset(cx - 3f, cy - 2f))
                    drawCircle(Color.White, radius = 2f, center = Offset(cx + 3f, cy - 2f))
                    drawCircle(Color(0xFFFACC15), radius = 2f, center = Offset(cx, cy + 3f))
                }
                1 -> {
                    // Tiger / Zebra Stripes
                    drawLine(Color.White, Offset(w * 0.24f, h * 0.34f), Offset(w * 0.76f, h * 0.42f), strokeWidth = 2f)
                    drawLine(Color.White, Offset(w * 0.24f, h * 0.56f), Offset(w * 0.76f, h * 0.64f), strokeWidth = 2f)
                }
                2 -> {
                    // Cyber Circuit Traces
                    drawLine(Color(0xFF00F2FE), Offset(w * 0.25f, cy), Offset(cx, cy), strokeWidth = 1.8f)
                    drawLine(Color(0xFF00F2FE), Offset(cx, cy), Offset(cx, h * 0.25f), strokeWidth = 1.8f)
                    drawCircle(Color.White, radius = 2.2f, center = Offset(cx, cy))
                }
                3 -> {
                    // Diamond Harlequin / Carbon Weave
                    val d = Path().apply {
                        moveTo(cx, h * 0.22f)
                        lineTo(w * 0.76f, cy)
                        lineTo(cx, h * 0.78f)
                        lineTo(w * 0.24f, cy)
                        close()
                    }
                    drawPath(d, color = Color.White, style = Stroke(width = 1.6f))
                }
                4 -> {
                    // Swirl / Galaxy Spiral
                    drawArc(Color.White, 0f, 260f, false, Offset(w * 0.26f, h * 0.26f), Size(w * 0.48f, h * 0.48f), style = Stroke(width = 1.8f))
                }
                else -> {
                    // Checkered / Brick Grid Texture
                    drawLine(Color.White, Offset(cx, h * 0.22f), Offset(cx, h * 0.78f), strokeWidth = 1.6f)
                    drawLine(Color.White, Offset(w * 0.22f, cy), Offset(w * 0.78f, cy), strokeWidth = 1.6f)
                }
            }
        }
        "AURA" -> {
            when (variantIndex % 5) {
                0 -> {
                    // Gyroscope Rings
                    drawOval(Color.White, Offset(w * 0.16f, h * 0.34f), Size(w * 0.68f, h * 0.32f), style = Stroke(width = 1.6f))
                    drawOval(subCol, Offset(w * 0.34f, h * 0.16f), Size(w * 0.32f, h * 0.68f), style = Stroke(width = 1.6f))
                }
                1 -> {
                    // Rising Saiyan Flame / Dragon Fire
                    val flame = Path().apply {
                        moveTo(cx, h * 0.14f)
                        lineTo(w * 0.76f, h * 0.76f)
                        lineTo(cx, h * 0.62f)
                        lineTo(w * 0.24f, h * 0.76f)
                        close()
                    }
                    drawPath(flame, color = Color(0xFFFACC15))
                }
                2 -> {
                    // Lightning Bolt / Thunder Aura
                    val bolt = Path().apply {
                        moveTo(w * 0.60f, h * 0.14f)
                        lineTo(w * 0.32f, cy)
                        lineTo(w * 0.56f, cy)
                        lineTo(w * 0.36f, h * 0.84f)
                    }
                    drawPath(bolt, color = Color.White, style = Stroke(width = 2f))
                }
                3 -> {
                    // Hexagram Rune / Snowflake
                    drawCircle(Color.White, radius = w * 0.28f, center = Offset(cx, cy), style = Stroke(width = 1.5f))
                    drawLine(Color.White, Offset(cx, h * 0.18f), Offset(cx, h * 0.82f), strokeWidth = 1.5f)
                    drawLine(Color.White, Offset(w * 0.18f, cy), Offset(w * 0.82f, cy), strokeWidth = 1.5f)
                }
                else -> {
                    // Equalizer Bars / Matrix Rain
                    drawRoundRect(Color.White, Offset(w * 0.24f, h * 0.42f), Size(3f, h * 0.36f), CornerRadius(1f, 1f))
                    drawRoundRect(Color(0xFFFACC15), Offset(cx - 1.5f, h * 0.22f), Size(3f, h * 0.56f), CornerRadius(1f, 1f))
                    drawRoundRect(Color.White, Offset(w * 0.68f, h * 0.34f), Size(3f, h * 0.44f), CornerRadius(1f, 1f))
                }
            }
        }
        "MEJA/PC" -> {
            drawRoundRect(Color(0xFF0F172A), Offset(w * 0.20f, h * 0.18f), Size(w * 0.60f, h * 0.36f), CornerRadius(2.5f, 2.5f))
            drawRoundRect(subCol, Offset(w * 0.20f, h * 0.18f), Size(w * 0.60f, h * 0.36f), CornerRadius(2.5f, 2.5f), style = Stroke(width = 1.5f))
            drawRoundRect(Color(0xFFFACC15), Offset(w * 0.14f, h * 0.62f), Size(w * 0.72f, h * 0.14f), CornerRadius(2f, 2f))
        }
        "SOFA" -> {
            drawRoundRect(subCol, Offset(w * 0.20f, h * 0.24f), Size(w * 0.60f, h * 0.34f), CornerRadius(3f, 3f))
            drawRoundRect(Color.White, Offset(w * 0.14f, h * 0.46f), Size(w * 0.72f, h * 0.28f), CornerRadius(3f, 3f))
            drawCircle(Color(0xFFFACC15), radius = 2.4f, center = Offset(cx, h * 0.44f))
        }
        "DEKOR" -> {
            drawRoundRect(Color(0xFF38BDF8), Offset(w * 0.20f, h * 0.18f), Size(w * 0.60f, h * 0.56f), CornerRadius(3f, 3f))
            drawRoundRect(Color(0xFFFACC15), Offset(w * 0.20f, h * 0.18f), Size(w * 0.60f, h * 0.56f), CornerRadius(3f, 3f), style = Stroke(width = 1.6f))
            drawCircle(Color(0xFFFEF08A), radius = 2.5f, center = Offset(w * 0.62f, h * 0.34f))
        }
        "PET" -> {
            drawCircle(subCol, radius = w * 0.14f, center = Offset(w * 0.28f, h * 0.28f))
            drawCircle(subCol, radius = w * 0.14f, center = Offset(w * 0.72f, h * 0.28f))
            drawCircle(Color.White, radius = w * 0.26f, center = Offset(cx, cy + 1f))
            drawCircle(Color(0xFF0F172A), radius = 1.4f, center = Offset(cx - 2.5f, cy))
            drawCircle(Color(0xFF0F172A), radius = 1.4f, center = Offset(cx + 2.5f, cy))
        }
        "LANTAI" -> {
            drawRoundRect(Color.White, Offset(w * 0.18f, h * 0.24f), Size(w * 0.30f, h * 0.26f), CornerRadius(2f, 2f))
            drawRoundRect(subCol, Offset(w * 0.52f, h * 0.24f), Size(w * 0.30f, h * 0.26f), CornerRadius(2f, 2f))
            drawRoundRect(subCol, Offset(w * 0.18f, h * 0.52f), Size(w * 0.30f, h * 0.26f), CornerRadius(2f, 2f))
            drawRoundRect(Color.White, Offset(w * 0.52f, h * 0.52f), Size(w * 0.30f, h * 0.26f), CornerRadius(2f, 2f))
        }
        "LAMPU" -> {
            drawLine(Color.White, Offset(cx, h * 0.12f), Offset(cx, h * 0.36f), strokeWidth = 1.6f)
            drawCircle(Color(0xFFFEF08A), radius = w * 0.22f, center = Offset(cx, h * 0.52f))
            drawCircle(Color.White, radius = w * 0.10f, center = Offset(cx - 1.5f, h * 0.48f))
        }
        "DINDING" -> {
            drawLine(Color.White.copy(alpha = 0.7f), Offset(w * 0.32f, h * 0.16f), Offset(w * 0.32f, h * 0.62f), strokeWidth = 1.6f)
            drawLine(Color.White.copy(alpha = 0.7f), Offset(w * 0.68f, h * 0.16f), Offset(w * 0.68f, h * 0.62f), strokeWidth = 1.6f)
            drawRoundRect(subCol, Offset(w * 0.16f, h * 0.60f), Size(w * 0.68f, h * 0.18f), CornerRadius(2f, 2f))
        }
        "ANIME" -> {
            when (variantIndex % 4) {
                0 -> {
                    // Flared Kimono / Haori Silhouette
                    val haori = Path().apply {
                        moveTo(w * 0.30f, h * 0.18f)
                        lineTo(w * 0.70f, h * 0.18f)
                        lineTo(w * 0.90f, h * 0.56f)
                        lineTo(w * 0.74f, h * 0.80f)
                        lineTo(w * 0.26f, h * 0.80f)
                        lineTo(w * 0.10f, h * 0.56f)
                        close()
                    }
                    drawPath(haori, color = subCol)
                    drawLine(Color.White, Offset(cx, h * 0.18f), Offset(cx, h * 0.80f), strokeWidth = 1.6f)
                }
                1 -> {
                    // Flaming / Wings of Freedom Cloak
                    val cloak = Path().apply {
                        moveTo(cx, h * 0.16f)
                        lineTo(w * 0.86f, h * 0.76f)
                        lineTo(cx, h * 0.64f)
                        lineTo(w * 0.14f, h * 0.76f)
                        close()
                    }
                    drawPath(cloak, color = Color.White)
                    drawCircle(Color(0xFFFACC15), radius = 2.4f, center = Offset(cx, h * 0.42f))
                }
                2 -> {
                    // Soul Rings / Gudoudama Halo + Robe
                    drawCircle(Color(0xFFFACC15), radius = w * 0.32f, center = Offset(cx, cy), style = Stroke(width = 1.6f))
                    drawRoundRect(subCol, Offset(w * 0.30f, h * 0.28f), Size(w * 0.40f, h * 0.48f), CornerRadius(3f, 3f))
                }
                else -> {
                    // Heavy Xuan Ruler / Katana Crossed on Back
                    drawLine(Color(0xFFFACC15), Offset(w * 0.20f, h * 0.78f), Offset(w * 0.80f, h * 0.20f), strokeWidth = 2.6f)
                    drawRoundRect(subCol, Offset(w * 0.28f, h * 0.28f), Size(w * 0.44f, h * 0.46f), CornerRadius(3f, 3f))
                }
            }
        }
        "HERO" -> {
            when (variantIndex % 3) {
                0 -> {
                    // Arc Reactor / Hero Chest Plate
                    val armor = Path().apply {
                        moveTo(w * 0.20f, h * 0.22f)
                        lineTo(w * 0.80f, h * 0.22f)
                        lineTo(w * 0.72f, h * 0.78f)
                        lineTo(cx, h * 0.84f)
                        lineTo(w * 0.28f, h * 0.78f)
                        close()
                    }
                    drawPath(armor, color = subCol)
                    drawCircle(Color(0xFF38BDF8), radius = w * 0.15f, center = Offset(cx, h * 0.46f))
                    drawCircle(Color.White, radius = w * 0.08f, center = Offset(cx, h * 0.46f))
                }
                1 -> {
                    // Scalloped Bat Cape Silhouette
                    val bat = Path().apply {
                        moveTo(cx, h * 0.20f)
                        lineTo(w * 0.88f, h * 0.48f)
                        lineTo(w * 0.74f, h * 0.78f)
                        lineTo(cx, h * 0.64f)
                        lineTo(w * 0.26f, h * 0.78f)
                        lineTo(w * 0.12f, h * 0.48f)
                        close()
                    }
                    drawPath(bat, color = Color(0xFF0F172A))
                    drawOval(Color(0xFFFACC15), Offset(w * 0.36f, h * 0.38f), Size(w * 0.28f, h * 0.16f))
                }
                else -> {
                    // Spider Pincer Legs / Hero Star Crest
                    drawLine(Color(0xFFFACC15), Offset(w * 0.14f, h * 0.24f), Offset(w * 0.86f, h * 0.76f), strokeWidth = 1.6f)
                    drawLine(Color(0xFFFACC15), Offset(w * 0.86f, h * 0.24f), Offset(w * 0.14f, h * 0.76f), strokeWidth = 1.6f)
                    drawCircle(subCol, radius = w * 0.22f, center = Offset(cx, cy))
                }
            }
        }
        "FANTASI" -> {
            // High Fantasy Cape / Winged Armor / Lich Robe
            val cape = Path().apply {
                moveTo(w * 0.26f, h * 0.20f)
                lineTo(w * 0.74f, h * 0.20f)
                lineTo(w * 0.86f, h * 0.78f)
                lineTo(cx, h * 0.68f)
                lineTo(w * 0.14f, h * 0.78f)
                close()
            }
            drawPath(cape, color = subCol)
            drawCircle(Color(0xFFFACC15), radius = 2.5f, center = Offset(cx, h * 0.34f))
        }
        "SENJATA" -> {
            // Diagonal Katana / Mjolnir / Bow Silhouette
            drawLine(Color.White, Offset(w * 0.22f, h * 0.76f), Offset(w * 0.78f, h * 0.20f), strokeWidth = 2.4f)
            drawCircle(Color(0xFFFACC15), radius = 2.6f, center = Offset(w * 0.36f, h * 0.62f))
        }
        "KEPALA" -> {
            // Mask / Headband / Crown Silhouette
            drawRoundRect(Color.White, Offset(w * 0.22f, h * 0.32f), Size(w * 0.56f, h * 0.34f), CornerRadius(4f, 4f))
            drawCircle(Color(0xFFEF4444), radius = 2f, center = Offset(w * 0.38f, cy))
            drawCircle(Color(0xFF38BDF8), radius = 2f, center = Offset(w * 0.62f, cy))
        }
        else -> {
            val r = w * 0.28f
            drawCircle(color = lipCol, radius = r, center = Offset(cx, cy + 1.5f))
            drawCircle(
                brush = Brush.linearGradient(listOf(Color.White, subCol, mainCol)),
                radius = r,
                center = Offset(cx, cy)
            )
            drawCircle(color = Color.White, radius = r * 0.35f, center = Offset(cx - r * 0.28f, cy - r * 0.28f))
        }
    }

    // Top Specular Gloss Pill on Badge
    drawRoundRect(
        color = Color.White.copy(alpha = 0.42f),
        topLeft = Offset(3f, 2.2f),
        size = Size(w - 6f, 2.5f),
        cornerRadius = CornerRadius(2f, 2f)
    )
}


