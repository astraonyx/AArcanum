package com.example.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.material.icons.filled.ColorLens
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.SecurityConfig
import com.example.ui.theme.AppColorPalettePreset
import com.example.ui.theme.AppThemeController
import com.example.ui.theme.AppVisualTheme
import com.example.ui.theme.CosmicObsidian
import com.example.ui.theme.CosmicTextPure
import com.example.ui.theme.CosmicTextSubtle
import com.example.ui.theme.CosmicVoid
import com.example.ui.theme.DuoBeeYellow
import com.example.ui.theme.DuoBeeYellowLip
import com.example.ui.theme.DuoBeetlePurple
import com.example.ui.theme.DuoBeetlePurpleLip
import com.example.ui.theme.DuoCardinalRed
import com.example.ui.theme.DuoCardinalRedLip
import com.example.ui.theme.DuoEelInk
import com.example.ui.theme.DuoFeatherGreen
import com.example.ui.theme.DuoFeatherGreenLip
import com.example.ui.theme.DuoFeatherGreenSoft
import com.example.ui.theme.DuoFoxOrange
import com.example.ui.theme.DuoHareLip
import com.example.ui.theme.DuoMacawBlue
import com.example.ui.theme.DuoMacawBlueLip
import com.example.ui.theme.DuoMintSurface
import com.example.ui.theme.DuoPureWhite
import com.example.ui.theme.DuoSwanBorder
import androidx.compose.material.icons.filled.Apps
import androidx.compose.material.icons.filled.OpenInNew
import com.example.ui.theme.AppFontPreset
import com.example.ui.theme.DynamicIslandColorPreset
import com.example.ui.theme.DynamicIslandLayoutMode
import com.example.ui.theme.DynamicIslandShapeStyle
import com.example.ui.theme.JetBrainsMonoFontFamily
import com.example.ui.theme.MascotAccessory
import com.example.ui.theme.MascotActivityPose
import com.example.ui.theme.SpaceGroteskFontFamily
import com.example.ui.theme.duolingoButton
import com.example.ui.theme.duolingoCard
import kotlin.math.roundToInt

@Composable
fun ThemeAndMascotStudioScreen(
    securityConfig: SecurityConfig,
    isMascotOverlayVisible: Boolean,
    onSetMascotOverlayVisible: (Boolean) -> Unit,
    onSelectVisualTheme: (String) -> Unit,
    onSelectColorPalette: (paletteId: String, customPrimaryHex: String?, customAccentHex: String?) -> Unit,
    onUpdateMascotStudio: (
        accessoryId: String?,
        poseId: String?,
        cosmicAuraEnabled: Boolean?,
        skinId: String?,
        outfitId: String?,
        glovesId: String?
    ) -> Unit,
    onUpdateDynamicIslandConfig: (
        shapeId: String?,
        presetId: String?,
        bgHex: String?,
        borderHex: String?,
        glowHex: String?
    ) -> Unit = { _, _, _, _, _ -> },
    modifier: Modifier = Modifier
) {
    // 0 = Bento Glow & Dynamic Island Customizer, 1 = Studio & Pose Maskot, 2 = Photobooth 3D
    var activeSubTab by remember { mutableIntStateOf(0) }

    val activeTheme = AppThemeController.userSelectedTheme
    val activePalette = AppThemeController.activePalette
    val activePose = AppThemeController.mascotActivityPose
    val activeAccessory = AppThemeController.mascotAccessory
    val activeSkin = AppThemeController.mascotSkin
    val activeOutfit = AppThemeController.mascotOutfit
    val activeGloves = AppThemeController.mascotGloves

    val activeIslandShape = AppThemeController.dynamicIslandShape
    val activeIslandPreset = AppThemeController.dynamicIslandPreset

    // Dynamic Island Custom Color States (Background, Neon Border, Bento Glow Light)
    var islandBgHexInput by remember(securityConfig.dynamicIslandBgHex, activeIslandPreset) {
        mutableStateOf(AppThemeController.dynamicIslandBgHex.ifBlank { "#0F172A" })
    }
    var islandBorderHexInput by remember(securityConfig.dynamicIslandBorderHex, activeIslandPreset) {
        mutableStateOf(AppThemeController.dynamicIslandBorderHex.ifBlank { "#6366F1" })
    }
    var islandGlowHexInput by remember(securityConfig.dynamicIslandGlowHex, activeIslandPreset) {
        mutableStateOf(AppThemeController.dynamicIslandGlowHex.ifBlank { "#38BDF8" })
    }
    // 0 = Warna Latar, 1 = Garis Tepi Neon, 2 = Gradien Cahaya Bento Glow
    var activeIslandColorChannel by remember { mutableIntStateOf(1) }

    val activeChannelHex = when (activeIslandColorChannel) {
        0 -> islandBgHexInput
        1 -> islandBorderHexInput
        else -> islandGlowHexInput
    }
    val parsedChannelColor = remember(activeChannelHex) {
        AppThemeController.parseHexColor(activeChannelHex, Color(0xFF6366F1))
    }
    var islandChannelR by remember(activeIslandColorChannel, parsedChannelColor) {
        mutableFloatStateOf(parsedChannelColor.red * 255f)
    }
    var islandChannelG by remember(activeIslandColorChannel, parsedChannelColor) {
        mutableFloatStateOf(parsedChannelColor.green * 255f)
    }
    var islandChannelB by remember(activeIslandColorChannel, parsedChannelColor) {
        mutableFloatStateOf(parsedChannelColor.blue * 255f)
    }

    // Custom RGB Sliders State initialized from current customPrimaryHex
    val initialCustomColor = remember(securityConfig.customPrimaryHex) {
        AppThemeController.parseHexColor(securityConfig.customPrimaryHex, Color(0xFFEF4444))
    }
    var customRed by remember(initialCustomColor) { mutableFloatStateOf(initialCustomColor.red * 255f) }
    var customGreen by remember(initialCustomColor) { mutableFloatStateOf(initialCustomColor.green * 255f) }
    var customBlue by remember(initialCustomColor) { mutableFloatStateOf(initialCustomColor.blue * 255f) }
    var customHexInput by remember(securityConfig.customPrimaryHex) {
        mutableStateOf(securityConfig.customPrimaryHex.ifBlank { "#EF4444" })
    }
    var customAccentHexInput by remember(securityConfig.customAccentHex) {
        mutableStateOf(securityConfig.customAccentHex.ifBlank { "#3B82F6" })
    }

    val liveCustomColor = Color(
        red = (customRed.roundToInt().coerceIn(0, 255)) / 255f,
        green = (customGreen.roundToInt().coerceIn(0, 255)) / 255f,
        blue = (customBlue.roundToInt().coerceIn(0, 255)) / 255f,
        alpha = 1f
    )

    fun rgbToHex(r: Int, g: Int, b: Int): String {
        return String.format("#%02X%02X%02X", r.coerceIn(0, 255), g.coerceIn(0, 255), b.coerceIn(0, 255))
    }

    val liveIslandBgColor = AppThemeController.parseHexColor(islandBgHexInput, Color(0xFF0F172A))
    val liveIslandBorderColor = AppThemeController.parseHexColor(islandBorderHexInput, Color(0xFF6366F1))
    val liveIslandGlowColor = AppThemeController.parseHexColor(islandGlowHexInput, Color(0xFF38BDF8))

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp, vertical = 14.dp)
            .testTag("theme_and_mascot_studio_screen"),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // 1. COMPACT MINIMALIST HEADER & SUB-TAB BAR
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .duolingoCard(
                    backgroundColor = DuoPureWhite,
                    borderColor = Color(0xFF6366F1),
                    lipColor = Color(0xFF4338CA),
                    cornerRadius = 18.dp,
                    borderWidth = 2.dp,
                    lipHeight = 4.dp
                )
                .padding(bottom = 4.dp)
                .padding(horizontal = 12.dp, vertical = 10.dp)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(34.dp)
                                .duolingoButton(
                                    faceColor = Color(0xFF6366F1),
                                    lipColor = Color(0xFF4338CA),
                                    cornerRadius = 10.dp,
                                    lipHeight = 3.dp
                                )
                                .padding(bottom = 3.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Palette,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                        Column {
                            Text(
                                text = "Studio Bento Glow & Dynamic Island",
                                style = MaterialTheme.typography.titleSmall,
                                fontFamily = SpaceGroteskFontFamily,
                                fontWeight = FontWeight.Bold,
                                color = CosmicTextPure
                            )
                            Text(
                                text = "${activeTheme.displayName} • ${activeIslandShape.displayName} • ${activePalette.displayName}",
                                style = MaterialTheme.typography.labelSmall,
                                color = CosmicTextSubtle,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }

                    Box(
                        modifier = Modifier
                            .duolingoButton(
                                faceColor = Color(0xFF6366F1),
                                lipColor = Color(0xFF4338CA),
                                cornerRadius = 8.dp,
                                lipHeight = 2.dp
                            )
                            .padding(bottom = 2.dp)
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "BENTO GLOW LOCKED",
                            fontFamily = JetBrainsMonoFontFamily,
                            fontSize = 8.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }

                // Compact Segmented Sub-Tab Switcher
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    val tabs = listOf(
                        Triple(0, "Tema & Island", Icons.Default.ColorLens),
                        Triple(1, "Kamar & Lemari", Icons.Default.Face),
                        Triple(2, "Photobooth 3D", Icons.Default.AutoAwesome)
                    )
                    tabs.forEach { (idx, label, icon) ->
                        val selected = activeSubTab == idx
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(36.dp)
                                .then(
                                    if (selected) {
                                        Modifier.duolingoButton(
                                            faceColor = Color(0xFF6366F1),
                                            lipColor = Color(0xFF4338CA),
                                            cornerRadius = 11.dp,
                                            lipHeight = 3.dp
                                        )
                                    } else {
                                        Modifier.duolingoCard(
                                            backgroundColor = DuoMintSurface,
                                            borderColor = DuoSwanBorder,
                                            lipColor = DuoHareLip,
                                            cornerRadius = 11.dp,
                                            borderWidth = 1.5.dp,
                                            lipHeight = 2.dp
                                        )
                                    }
                                )
                                .instantTouchPress { activeSubTab = idx }
                                .padding(bottom = 2.dp)
                                .testTag("theme_studio_subtab_$idx"),
                            contentAlignment = Alignment.Center
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(5.dp)
                            ) {
                                Icon(
                                    imageVector = icon,
                                    contentDescription = null,
                                    tint = if (selected) Color.White else DuoEelInk,
                                    modifier = Modifier.size(14.dp)
                                )
                                Text(
                                    text = label,
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = if (selected) Color.White else DuoEelInk
                                )
                            }
                        }
                    }
                }
            }
        }

        GsapSharedMorphContainer(
            targetState = activeSubTab,
            modifier = Modifier.fillMaxWidth(),
            label = "theme_studio_gsap_subtab"
        ) { currentSubTab ->
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
        if (currentSubTab == 0) {
            // =========================================================================
            // SUB-TAB 0: GSAP MOTION STUDIO + ARSITEKTUR TEMA + DYNAMIC ISLAND + PALET WARNA
            // =========================================================================

            // Card GSAP: Studio GSAP Motion & Fisika Animasi 3D
            GsapMotionStudioCard()

            // Card A1: 6 Gaya Arsitektur Tema Bebas + Info Sanctuary Terkunci Bento Grid Glow
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .duolingoCard(
                        backgroundColor = CosmicObsidian,
                        borderColor = DuoSwanBorder,
                        lipColor = DuoSwanBorder,
                        cornerRadius = 20.dp,
                        borderWidth = 2.dp,
                        lipHeight = 4.dp
                    )
                    .padding(bottom = 4.dp)
                    .padding(14.dp)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "1. Pilih Gaya Arsitektur Tema (6 Gaya Bebas)",
                                style = MaterialTheme.typography.titleSmall,
                                fontFamily = SpaceGroteskFontFamily,
                                fontWeight = FontWeight.Bold,
                                color = CosmicTextPure
                            )
                            Text(
                                text = "Pilih bebas dari 6 gaya tema untuk seluruh aplikasi utama. Ruang terisolasi BlackBox Sanctuary dikunci khusus pada Bento Grid Glow.",
                                style = MaterialTheme.typography.bodySmall,
                                color = CosmicTextSubtle
                            )
                        }
                        Box(
                            modifier = Modifier
                                .duolingoButton(
                                    faceColor = AppThemeController.resolvedPrimaryColor,
                                    lipColor = AppThemeController.resolvedPrimaryLipColor,
                                    cornerRadius = 10.dp,
                                    lipHeight = 3.dp
                                )
                                .padding(bottom = 2.dp)
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = activeTheme.badgeText,
                                fontFamily = JetBrainsMonoFontFamily,
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    }

                    AppVisualTheme.entries.chunked(2).forEach { rowThemes ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            rowThemes.forEach { themeItem ->
                                val isSelected = activeTheme == themeItem
                                val cardFace = when {
                                    isSelected && themeItem == AppVisualTheme.BENTO_GLOW -> Color(0xFF6366F1)
                                    isSelected -> AppThemeController.resolvedPrimaryColor
                                    else -> DuoMintSurface
                                }
                                val cardLip = when {
                                    isSelected && themeItem == AppVisualTheme.BENTO_GLOW -> Color(0xFF4338CA)
                                    isSelected -> AppThemeController.resolvedPrimaryLipColor
                                    else -> DuoSwanBorder
                                }
                                val textColor = if (isSelected) Color.White else CosmicTextPure
                                val subColor = if (isSelected) Color.White.copy(alpha = 0.88f) else CosmicTextSubtle

                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .duolingoButton(
                                            faceColor = cardFace,
                                            lipColor = cardLip,
                                            cornerRadius = 14.dp,
                                            lipHeight = 4.dp
                                        )
                                        .instantTouchPress {
                                            AppThemeController.activeTheme = themeItem
                                            onSelectVisualTheme(themeItem.id)
                                        }
                                        .padding(bottom = 3.dp)
                                        .padding(vertical = 10.dp, horizontal = 10.dp)
                                        .testTag("studio_select_theme_${themeItem.id.lowercase()}")
                                ) {
                                    Column(verticalArrangement = Arrangement.spacedBy(3.dp)) {
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Text(
                                                text = themeItem.displayName,
                                                style = MaterialTheme.typography.labelLarge,
                                                fontFamily = SpaceGroteskFontFamily,
                                                fontWeight = FontWeight.Bold,
                                                color = textColor,
                                                maxLines = 1,
                                                overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
                                            )
                                            if (isSelected) {
                                                Icon(
                                                    imageVector = Icons.Default.CheckCircle,
                                                    contentDescription = null,
                                                    tint = Color.White,
                                                    modifier = Modifier.size(14.dp)
                                                )
                                            }
                                        }
                                        Text(
                                            text = themeItem.badgeText,
                                            fontFamily = JetBrainsMonoFontFamily,
                                            fontSize = 9.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = subColor
                                        )
                                        Text(
                                            text = themeItem.subtitle,
                                            style = MaterialTheme.typography.labelSmall,
                                            fontSize = 10.sp,
                                            color = subColor,
                                            maxLines = 2,
                                            overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
                                        )
                                    }
                                }
                            }
                        }
                    }

                    // Exclusive Sanctuary Lock Badge Notice
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .duolingoCard(
                                backgroundColor = Color(0xFF0F172A),
                                borderColor = Color(0xFF6366F1),
                                lipColor = Color(0xFF4338CA),
                                cornerRadius = 12.dp,
                                borderWidth = 1.5.dp,
                                lipHeight = 3.dp
                            )
                            .padding(bottom = 2.dp)
                            .padding(horizontal = 10.dp, vertical = 8.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Lock,
                                contentDescription = null,
                                tint = Color(0xFF818CF8),
                                modifier = Modifier.size(16.dp)
                            )
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "BLACKBOX SANCTUARY: KHUSUS BENTO GRID GLOW",
                                    fontFamily = JetBrainsMonoFontFamily,
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFFC7D2FE)
                                )
                                Text(
                                    text = "Ruang terisolasi Sanctuary otomatis mengunci visual ke Bento Grid Glow, sedangkan Brankas, Game, dan Pengaturan bebas memakai ke-6 tema di atas.",
                                    style = MaterialTheme.typography.labelSmall,
                                    fontSize = 10.sp,
                                    color = Color(0xFF94A3B8)
                                )
                            }
                        }
                    }
                }
            }

            // Card A1.5: App-Wide Expressive Font Switcher & Scale Studio (8 Gaya Font Tidak Kaku)
            val currentFontPreset = AppThemeController.activeFontPreset
            val currentFontScale = AppThemeController.fontScaleMultiplier
            var showMicroHubFromStudio by remember { mutableStateOf(false) }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .duolingoCard(
                        backgroundColor = CosmicObsidian,
                        borderColor = DuoMacawBlue,
                        lipColor = DuoMacawBlueLip,
                        cornerRadius = 20.dp,
                        borderWidth = 2.dp,
                        lipHeight = 4.dp
                    )
                    .padding(bottom = 4.dp)
                    .padding(14.dp)
                    .testTag("studio_font_switcher_card")
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Pilihan Font Aplikasi (6 Gaya Eksklusif)",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = CosmicTextPure
                            )
                            Text(
                                text = "Pilih antara Normal Google, Serif, Italian, Bold, Minecraft (8-Bit), atau The Cavas (Modern & Elegant).",
                                style = MaterialTheme.typography.bodySmall,
                                color = CosmicTextSubtle
                            )
                        }
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(DuoMacawBlue)
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = currentFontPreset.badgeText,
                                fontFamily = JetBrainsMonoFontFamily,
                                fontSize = 8.5.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    }

                    // 6 Exclusive Font Family Cards in 2-Column Grid
                    val fontContext = androidx.compose.ui.platform.LocalContext.current
                    AppFontPreset.entries.chunked(2).forEach { fontRow ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            fontRow.forEach { fontOption ->
                                val isFontSelected = currentFontPreset == fontOption
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .duolingoButton(
                                            faceColor = if (isFontSelected) DuoMacawBlue else DuoMintSurface,
                                            lipColor = if (isFontSelected) DuoMacawBlueLip else DuoSwanBorder,
                                            cornerRadius = 14.dp,
                                            lipHeight = 3.dp
                                        )
                                        .instantTouchPress {
                                            AppThemeController.selectFontPreset(fontOption, fontContext)
                                        }
                                        .padding(bottom = 2.dp)
                                        .padding(10.dp)
                                        .testTag("studio_font_option_${fontOption.id.lowercase()}")
                                ) {
                                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Text(
                                                text = fontOption.displayName,
                                                fontFamily = fontOption.headlineFont,
                                                fontSize = 12.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = if (isFontSelected) Color.White else CosmicTextPure,
                                                maxLines = 1
                                            )
                                            if (isFontSelected) {
                                                Icon(
                                                    imageVector = Icons.Default.CheckCircle,
                                                    contentDescription = null,
                                                    tint = Color.White,
                                                    modifier = Modifier.size(14.dp)
                                                )
                                            }
                                        }
                                        if (fontOption == AppFontPreset.THE_CAVAS) {
                                            androidx.compose.foundation.Image(
                                                painter = androidx.compose.ui.res.painterResource(id = com.example.R.drawable.the_cavas_font_preview),
                                                contentDescription = "The Cavas Modern & Elegant Font Specimen",
                                                contentScale = androidx.compose.ui.layout.ContentScale.Crop,
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .height(44.dp)
                                                    .clip(RoundedCornerShape(8.dp))
                                                    .border(1.dp, DuoSwanBorder, RoundedCornerShape(8.dp))
                                            )
                                        }
                                        Text(
                                            text = fontOption.samplePreview,
                                            fontFamily = fontOption.headlineFont,
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Medium,
                                            color = if (isFontSelected) Color.White.copy(alpha = 0.92f) else DuoMacawBlue,
                                            maxLines = 1
                                        )
                                        Text(
                                            text = fontOption.subtitle,
                                            fontFamily = fontOption.bodyFont,
                                            fontSize = 9.sp,
                                            color = if (isFontSelected) Color.White.copy(alpha = 0.85f) else CosmicTextSubtle,
                                            maxLines = 2
                                        )
                                    }
                                }
                            }
                        }
                    }

                    // Font Size Scale Slider (85% - 125%)
                    Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Skala Ukuran Font Global:",
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Bold,
                                color = CosmicTextPure
                            )
                            Text(
                                text = "${(currentFontScale * 100).roundToInt()}%",
                                fontFamily = JetBrainsMonoFontFamily,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = DuoMacawBlue
                            )
                        }
                        Slider(
                            value = currentFontScale,
                            onValueChange = { AppThemeController.fontScaleMultiplier = it },
                            valueRange = 0.85f..1.25f,
                            colors = SliderDefaults.colors(
                                thumbColor = DuoMacawBlue,
                                activeTrackColor = DuoMacawBlue
                            )
                        )
                    }

                    // 120+ Interactive Micro-Features Hub Quick Launcher Banner inside Theme Tab
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .duolingoButton(
                                faceColor = Color(0xFF059669),
                                lipColor = Color(0xFF047857),
                                cornerRadius = 14.dp,
                                lipHeight = 4.dp
                            )
                            .instantTouchPress {
                                showMicroHubFromStudio = true
                            }
                            .padding(bottom = 3.dp)
                            .padding(horizontal = 12.dp, vertical = 10.dp)
                            .testTag("studio_open_120_micro_features_button")
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                modifier = Modifier.weight(1f)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Apps,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(20.dp)
                                )
                                Column {
                                    Text(
                                        text = "Buka 120+ Micro-Fitur Interaktif (Swiss-Army Hub)",
                                        style = MaterialTheme.typography.labelLarge,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                    )
                                    Text(
                                        text = "10 Kategori • Kalkulator, Kripto, Privasi, Konverter, Finansial, Zen & Pin ke Dynamic Island",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = Color.White.copy(alpha = 0.9f)
                                    )
                                }
                            }
                            Icon(
                                imageVector = Icons.Default.OpenInNew,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }
            }

            if (showMicroHubFromStudio) {
                MicroFeaturesSwissArmyDialog(
                    onDismiss = { showMicroHubFromStudio = false }
                )
            }

            // Card A2: Dynamic Island Shape & Bento Glow Color Customizer
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .duolingoCard(
                        backgroundColor = CosmicObsidian,
                        borderColor = liveIslandBorderColor,
                        lipColor = AppThemeController.darkenColor(liveIslandBorderColor, 0.72f),
                        cornerRadius = 20.dp,
                        borderWidth = 2.dp,
                        lipHeight = 4.dp
                    )
                    .padding(bottom = 4.dp)
                    .padding(14.dp)
                    .testTag("dynamic_island_customizer_card")
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "2. Kustomisasi Bentuk & Cahaya Dynamic Island",
                                style = MaterialTheme.typography.titleSmall,
                                fontFamily = SpaceGroteskFontFamily,
                                fontWeight = FontWeight.Bold,
                                color = CosmicTextPure
                            )
                            Text(
                                text = "Ubah geometri kapsul (Kapsul Pil, Hexagon Cyber, Kotak Bento, Notch Menyatu) serta warna latar, garis tepi neon, dan gradien cahaya Bento Glow.",
                                style = MaterialTheme.typography.bodySmall,
                                color = CosmicTextSubtle
                            )
                        }
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(liveIslandBorderColor.copy(alpha = 0.16f))
                                .border(1.dp, liveIslandBorderColor, RoundedCornerShape(8.dp))
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = activeIslandShape.badgeText,
                                fontFamily = JetBrainsMonoFontFamily,
                                fontSize = 8.sp,
                                fontWeight = FontWeight.Bold,
                                color = liveIslandBorderColor
                            )
                        }
                    }

                    // Live Interactive Preview of the Dynamic Island Shape & Glow
                    val previewIslandShape = remember(activeIslandShape) {
                        buildDynamicIslandShape(activeIslandShape, isExpanded = false)
                    }
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                            .background(Color(0xFF090D16))
                            .border(1.5.dp, Color(0xFF1E293B), RoundedCornerShape(16.dp))
                            .padding(vertical = 14.dp, horizontal = 16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Text(
                                text = "PRATINJAU LANGSUNG DYNAMIC ISLAND • BENTO GLOW",
                                fontFamily = JetBrainsMonoFontFamily,
                                fontSize = 8.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF94A3B8)
                            )
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth(0.92f)
                                    .height(42.dp)
                                    .clip(previewIslandShape)
                                    .background(
                                        Brush.horizontalGradient(
                                            colors = listOf(
                                                liveIslandBgColor,
                                                Color(
                                                    red = (liveIslandBgColor.red * 0.78f + liveIslandGlowColor.red * 0.22f).coerceIn(0f, 1f),
                                                    green = (liveIslandBgColor.green * 0.78f + liveIslandGlowColor.green * 0.22f).coerceIn(0f, 1f),
                                                    blue = (liveIslandBgColor.blue * 0.78f + liveIslandGlowColor.blue * 0.22f).coerceIn(0f, 1f),
                                                    alpha = 1f
                                                ),
                                                liveIslandBgColor
                                            )
                                        )
                                    )
                                    .border(
                                        width = 2.dp,
                                        brush = Brush.horizontalGradient(
                                            colors = listOf(
                                                liveIslandBorderColor,
                                                liveIslandGlowColor,
                                                liveIslandBorderColor
                                            )
                                        ),
                                        shape = previewIslandShape
                                    )
                                    .padding(horizontal = 12.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .size(10.dp)
                                                .clip(CircleShape)
                                                .background(liveIslandGlowColor)
                                        )
                                        Text(
                                            text = activeIslandShape.displayName.uppercase(),
                                            fontFamily = JetBrainsMonoFontFamily,
                                            fontSize = 9.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color.White
                                        )
                                    }
                                    Text(
                                        text = "${islandBgHexInput.uppercase()} • ${islandBorderHexInput.uppercase()}",
                                        fontFamily = JetBrainsMonoFontFamily,
                                        fontSize = 8.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = liveIslandGlowColor
                                    )
                                }
                            }
                        }
                    }

                    // Non-Overlap Layout Mode Selector (Anti-Tutup / Dorong Bawah, Ramping 34dp, Pita Lipat)
                    Text(
                        text = "MODE ANTI-TUTUP & POSISI DYNAMIC ISLAND (TIDAK MENUTUPI LAYAR)",
                        fontFamily = JetBrainsMonoFontFamily,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        color = CosmicTextPure
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        DynamicIslandLayoutMode.entries.forEach { modeItem ->
                            val isModeSelected = AppThemeController.dynamicIslandLayoutMode == modeItem
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .duolingoButton(
                                        faceColor = if (isModeSelected) liveIslandBorderColor else DuoMintSurface,
                                        lipColor = if (isModeSelected) AppThemeController.darkenColor(liveIslandBorderColor, 0.72f) else DuoSwanBorder,
                                        cornerRadius = 12.dp,
                                        lipHeight = 3.dp
                                    )
                                    .instantTouchPress {
                                        AppThemeController.dynamicIslandLayoutMode = modeItem
                                        AppThemeController.isDynamicIslandFolded = (modeItem == DynamicIslandLayoutMode.FOLDABLE_RIBBON)
                                    }
                                    .padding(bottom = 2.dp)
                                    .padding(horizontal = 8.dp, vertical = 8.dp)
                                    .testTag("dynamic_island_layout_${modeItem.id.lowercase()}"),
                                contentAlignment = Alignment.Center
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text(
                                        text = modeItem.displayName,
                                        style = MaterialTheme.typography.labelSmall,
                                        fontWeight = FontWeight.Bold,
                                        color = if (isModeSelected) Color.White else CosmicTextPure,
                                        maxLines = 1
                                    )
                                    Text(
                                        text = modeItem.shortBadge,
                                        fontFamily = JetBrainsMonoFontFamily,
                                        fontSize = 7.5.sp,
                                        color = if (isModeSelected) Color.White.copy(alpha = 0.9f) else CosmicTextSubtle
                                    )
                                }
                            }
                        }
                    }

                    // 10 Unique Sculptural Shapes Selector
                    Text(
                        text = "PILIH MODEL BENTUK UNIK DYNAMIC ISLAND (10 DESAIN ARSITEKTURAL)",
                        fontFamily = JetBrainsMonoFontFamily,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        color = CosmicTextPure
                    )
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        DynamicIslandShapeStyle.entries.chunked(2).forEach { rowShapes ->
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                rowShapes.forEach { shapeOption ->
                                    val isSelected = activeIslandShape == shapeOption
                                    Box(
                                        modifier = Modifier
                                            .weight(1f)
                                            .then(
                                                if (isSelected) {
                                                    Modifier.duolingoButton(
                                                        faceColor = liveIslandBorderColor,
                                                        lipColor = AppThemeController.darkenColor(liveIslandBorderColor, 0.72f),
                                                        cornerRadius = 14.dp,
                                                        lipHeight = 4.dp
                                                    )
                                                } else {
                                                    Modifier.duolingoCard(
                                                        backgroundColor = DuoPureWhite,
                                                        borderColor = DuoSwanBorder,
                                                        lipColor = DuoHareLip,
                                                        cornerRadius = 14.dp,
                                                        borderWidth = 1.5.dp,
                                                        lipHeight = 3.dp
                                                    )
                                                }
                                            )
                                            .instantTouchPress {
                                                AppThemeController.dynamicIslandShape = shapeOption
                                                onUpdateDynamicIslandConfig(shapeOption.id, null, null, null, null)
                                            }
                                            .padding(bottom = 3.dp)
                                            .padding(horizontal = 10.dp, vertical = 9.dp)
                                            .testTag("dynamic_island_shape_${shapeOption.id.lowercase()}")
                                    ) {
                                        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                            Row(
                                                modifier = Modifier.fillMaxWidth(),
                                                horizontalArrangement = Arrangement.SpaceBetween,
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                DynamicIslandShapeSilhouetteVector(
                                                    shapeStyle = shapeOption,
                                                    tint = if (isSelected) Color.White else liveIslandBorderColor,
                                                    modifier = Modifier.size(width = 34.dp, height = 16.dp)
                                                )
                                                Text(
                                                    text = if (isSelected) "AKTIF" else shapeOption.badgeText,
                                                    fontFamily = JetBrainsMonoFontFamily,
                                                    fontSize = 8.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    color = if (isSelected) Color.White else liveIslandBorderColor
                                                )
                                            }
                                            Text(
                                                text = shapeOption.displayName,
                                                style = MaterialTheme.typography.labelMedium,
                                                fontFamily = SpaceGroteskFontFamily,
                                                fontWeight = FontWeight.Bold,
                                                color = if (isSelected) Color.White else DuoEelInk,
                                                maxLines = 1
                                            )
                                            Text(
                                                text = shapeOption.subtitle,
                                                style = MaterialTheme.typography.labelSmall,
                                                fontSize = 9.sp,
                                                color = if (isSelected) Color.White.copy(alpha = 0.9f) else CosmicTextSubtle,
                                                maxLines = 2
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }

                    // 6 Dynamic Island Bento Glow Presets
                    Text(
                        text = "PRESET WARNA & CAHAYA BENTO GLOW DYNAMIC ISLAND",
                        fontFamily = JetBrainsMonoFontFamily,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        color = CosmicTextPure
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        DynamicIslandColorPreset.entries.forEach { preset ->
                            val isSelected = activeIslandPreset == preset
                            Box(
                                modifier = Modifier
                                    .then(
                                        if (isSelected) {
                                            Modifier.duolingoButton(
                                                faceColor = preset.borderColor,
                                                lipColor = AppThemeController.darkenColor(preset.borderColor, 0.72f),
                                                cornerRadius = 12.dp,
                                                lipHeight = 3.dp
                                            )
                                        } else {
                                            Modifier.duolingoCard(
                                                backgroundColor = DuoPureWhite,
                                                borderColor = preset.borderColor,
                                                lipColor = AppThemeController.darkenColor(preset.borderColor, 0.72f),
                                                cornerRadius = 12.dp,
                                                borderWidth = 1.5.dp,
                                                lipHeight = 2.dp
                                            )
                                        }
                                    )
                                    .instantTouchPress {
                                        if (preset == DynamicIslandColorPreset.CUSTOM) {
                                            onUpdateDynamicIslandConfig(
                                                null,
                                                DynamicIslandColorPreset.CUSTOM.id,
                                                islandBgHexInput,
                                                islandBorderHexInput,
                                                islandGlowHexInput
                                            )
                                        } else {
                                            AppThemeController.applyDynamicIslandPreset(preset)
                                            islandBgHexInput = AppThemeController.dynamicIslandBgHex
                                            islandBorderHexInput = AppThemeController.dynamicIslandBorderHex
                                            islandGlowHexInput = AppThemeController.dynamicIslandGlowHex
                                            onUpdateDynamicIslandConfig(
                                                null,
                                                preset.id,
                                                islandBgHexInput,
                                                islandBorderHexInput,
                                                islandGlowHexInput
                                            )
                                        }
                                    }
                                    .padding(bottom = 2.dp)
                                    .padding(horizontal = 10.dp, vertical = 7.dp)
                                    .testTag("dynamic_island_preset_${preset.id.lowercase()}")
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(14.dp)
                                            .clip(CircleShape)
                                            .background(preset.bgColor)
                                            .border(1.5.dp, if (isSelected) Color.White else preset.glowColor, CircleShape)
                                    )
                                    Text(
                                        text = preset.displayName,
                                        style = MaterialTheme.typography.labelSmall,
                                        fontWeight = FontWeight.Bold,
                                        color = if (isSelected) Color.White else DuoEelInk
                                    )
                                }
                            }
                        }
                    }

                    // Custom RGB & HEX Controls for Background, Neon Border, and Bento Glow Gradient
                    Text(
                        text = "RACIK WARNA KUSTOM DYNAMIC ISLAND (LATAR, TEPI NEON, CAHAYA GLOW)",
                        fontFamily = JetBrainsMonoFontFamily,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        color = CosmicTextPure
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        val channels = listOf(
                            Triple(0, "Warna Latar", liveIslandBgColor),
                            Triple(1, "Tepi Neon", liveIslandBorderColor),
                            Triple(2, "Cahaya Glow", liveIslandGlowColor)
                        )
                        channels.forEach { (chIdx, chTitle, chColor) ->
                            val selected = activeIslandColorChannel == chIdx
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .height(34.dp)
                                    .then(
                                        if (selected) {
                                            Modifier.duolingoButton(
                                                faceColor = chColor,
                                                lipColor = AppThemeController.darkenColor(chColor, 0.7f),
                                                cornerRadius = 10.dp,
                                                lipHeight = 3.dp
                                            )
                                        } else {
                                            Modifier.duolingoCard(
                                                backgroundColor = DuoMintSurface,
                                                borderColor = chColor,
                                                lipColor = DuoHareLip,
                                                cornerRadius = 10.dp,
                                                borderWidth = 1.5.dp,
                                                lipHeight = 2.dp
                                            )
                                        }
                                    )
                                    .instantTouchPress { activeIslandColorChannel = chIdx }
                                    .padding(bottom = 2.dp)
                                    .testTag("island_color_channel_$chIdx"),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = chTitle,
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = if (selected) Color.White else DuoEelInk
                                )
                            }
                        }
                    }

                    // RGB Sliders for the active Dynamic Island channel
                    val updateActiveIslandHex: (String) -> Unit = { newHex ->
                        when (activeIslandColorChannel) {
                            0 -> islandBgHexInput = newHex
                            1 -> islandBorderHexInput = newHex
                            else -> islandGlowHexInput = newHex
                        }
                    }
                    Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                        Slider(
                            value = islandChannelR,
                            onValueChange = {
                                islandChannelR = it
                                updateActiveIslandHex(
                                    rgbToHex(islandChannelR.roundToInt(), islandChannelG.roundToInt(), islandChannelB.roundToInt())
                                )
                            },
                            valueRange = 0f..255f,
                            colors = SliderDefaults.colors(
                                thumbColor = Color(0xFFEF4444),
                                activeTrackColor = Color(0xFFEF4444)
                            ),
                            modifier = Modifier.height(24.dp)
                        )
                        Slider(
                            value = islandChannelG,
                            onValueChange = {
                                islandChannelG = it
                                updateActiveIslandHex(
                                    rgbToHex(islandChannelR.roundToInt(), islandChannelG.roundToInt(), islandChannelB.roundToInt())
                                )
                            },
                            valueRange = 0f..255f,
                            colors = SliderDefaults.colors(
                                thumbColor = Color(0xFF16A34A),
                                activeTrackColor = Color(0xFF16A34A)
                            ),
                            modifier = Modifier.height(24.dp)
                        )
                        Slider(
                            value = islandChannelB,
                            onValueChange = {
                                islandChannelB = it
                                updateActiveIslandHex(
                                    rgbToHex(islandChannelR.roundToInt(), islandChannelG.roundToInt(), islandChannelB.roundToInt())
                                )
                            },
                            valueRange = 0f..255f,
                            colors = SliderDefaults.colors(
                                thumbColor = Color(0xFF2563EB),
                                activeTrackColor = Color(0xFF2563EB)
                            ),
                            modifier = Modifier.height(24.dp)
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        OutlinedTextField(
                            value = islandBgHexInput,
                            onValueChange = { islandBgHexInput = it },
                            label = { Text("Latar", fontSize = 10.sp) },
                            singleLine = true,
                            modifier = Modifier
                                .weight(1f)
                                .testTag("island_bg_hex_input")
                        )
                        OutlinedTextField(
                            value = islandBorderHexInput,
                            onValueChange = { islandBorderHexInput = it },
                            label = { Text("Tepi Neon", fontSize = 10.sp) },
                            singleLine = true,
                            modifier = Modifier
                                .weight(1f)
                                .testTag("island_border_hex_input")
                        )
                        OutlinedTextField(
                            value = islandGlowHexInput,
                            onValueChange = { islandGlowHexInput = it },
                            label = { Text("Glow", fontSize = 10.sp) },
                            singleLine = true,
                            modifier = Modifier
                                .weight(1f)
                                .testTag("island_glow_hex_input")
                        )
                    }

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(44.dp)
                            .duolingoButton(
                                faceColor = liveIslandBorderColor,
                                lipColor = AppThemeController.darkenColor(liveIslandBorderColor, 0.72f),
                                cornerRadius = 14.dp,
                                lipHeight = 4.dp
                            )
                            .instantTouchPress {
                                AppThemeController.dynamicIslandPreset = DynamicIslandColorPreset.CUSTOM
                                AppThemeController.dynamicIslandBgHex = islandBgHexInput
                                AppThemeController.dynamicIslandBorderHex = islandBorderHexInput
                                AppThemeController.dynamicIslandGlowHex = islandGlowHexInput
                                onUpdateDynamicIslandConfig(
                                    activeIslandShape.id,
                                    DynamicIslandColorPreset.CUSTOM.id,
                                    islandBgHexInput,
                                    islandBorderHexInput,
                                    islandGlowHexInput
                                )
                            }
                            .padding(bottom = 4.dp)
                            .testTag("apply_custom_dynamic_island_button"),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Terapkan Bentuk & Warna Dynamic Island",
                            style = MaterialTheme.typography.labelLarge,
                            fontFamily = SpaceGroteskFontFamily,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }
            }

            // Card B: Color Palette Presets (Green -> Red, Blue, Purple, Gold, Pink, Emerald, Custom)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .duolingoCard(
                        backgroundColor = CosmicObsidian,
                        borderColor = DuoFeatherGreen,
                        lipColor = DuoFeatherGreenLip,
                        cornerRadius = 20.dp,
                        borderWidth = 2.dp,
                        lipHeight = 4.dp
                    )
                    .padding(bottom = 4.dp)
                    .padding(14.dp)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "3. Palet Aksen Bento Glow Aplikasi",
                                style = MaterialTheme.typography.titleSmall,
                                fontFamily = SpaceGroteskFontFamily,
                                fontWeight = FontWeight.Bold,
                                color = CosmicTextPure
                            )
                            Text(
                                text = "Ubah warna aksen pendaran Bento Glow dari Hijau ke Merah Karmin, Biru Samudra, Ungu, Emas, atau Custom.",
                                style = MaterialTheme.typography.bodySmall,
                                color = CosmicTextSubtle
                            )
                        }
                    }

                    // 2-Column Grid of Color Palette Swatches
                    val presets = AppColorPalettePreset.entries
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        presets.chunked(2).forEach { rowItems ->
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                rowItems.forEach { preset ->
                                    val isSelected = activePalette == preset
                                    val swatchPrimary = if (preset == AppColorPalettePreset.CUSTOM) {
                                        liveCustomColor
                                    } else {
                                        preset.primaryColor
                                    }
                                    val swatchLip = if (preset == AppColorPalettePreset.CUSTOM) {
                                        AppThemeController.darkenColor(liveCustomColor, 0.74f)
                                    } else {
                                        preset.primaryLipColor
                                    }

                                    Box(
                                        modifier = Modifier
                                            .weight(1f)
                                            .then(
                                                if (isSelected) {
                                                    Modifier.duolingoButton(
                                                        faceColor = swatchPrimary,
                                                        lipColor = swatchLip,
                                                        cornerRadius = 14.dp,
                                                        lipHeight = 4.dp
                                                    )
                                                } else {
                                                    Modifier.duolingoCard(
                                                        backgroundColor = DuoPureWhite,
                                                        borderColor = swatchPrimary,
                                                        lipColor = swatchLip,
                                                        cornerRadius = 14.dp,
                                                        borderWidth = 2.dp,
                                                        lipHeight = 3.dp
                                                    )
                                                }
                                            )
                                            .instantTouchPress {
                                                if (preset == AppColorPalettePreset.CUSTOM) {
                                                    onSelectColorPalette(
                                                        preset.id,
                                                        customHexInput,
                                                        customAccentHexInput
                                                    )
                                                } else {
                                                    onSelectColorPalette(preset.id, null, null)
                                                }
                                            }
                                            .padding(bottom = 3.dp)
                                            .padding(horizontal = 10.dp, vertical = 9.dp)
                                            .testTag("palette_preset_${preset.id.lowercase()}")
                                    ) {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                                        ) {
                                            // Dual color circle swatch
                                            Box(
                                                modifier = Modifier
                                                    .size(24.dp)
                                                    .clip(CircleShape)
                                                    .background(if (isSelected) Color.White else swatchPrimary)
                                                    .border(
                                                        width = 2.dp,
                                                        color = if (isSelected) swatchLip else Color.White,
                                                        shape = CircleShape
                                                    ),
                                                contentAlignment = Alignment.Center
                                            ) {
                                                if (isSelected) {
                                                    Icon(
                                                        imageVector = Icons.Default.CheckCircle,
                                                        contentDescription = null,
                                                        tint = swatchPrimary,
                                                        modifier = Modifier.size(16.dp)
                                                    )
                                                }
                                            }
                                            Column(modifier = Modifier.weight(1f)) {
                                                Text(
                                                    text = preset.displayName,
                                                    style = MaterialTheme.typography.labelMedium,
                                                    fontWeight = FontWeight.Bold,
                                                    color = if (isSelected) Color.White else DuoEelInk,
                                                    maxLines = 1
                                                )
                                                Text(
                                                    text = preset.shortBadge,
                                                    fontFamily = JetBrainsMonoFontFamily,
                                                    fontSize = 8.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    color = if (isSelected) Color.White.copy(alpha = 0.9f) else CosmicTextSubtle,
                                                    maxLines = 1
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // Card C: Free Custom Color Picker (RGB Sliders + HEX Code + Live Button Preview)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .duolingoCard(
                        backgroundColor = CosmicObsidian,
                        borderColor = liveCustomColor,
                        lipColor = AppThemeController.darkenColor(liveCustomColor, 0.74f),
                        cornerRadius = 20.dp,
                        borderWidth = 2.dp,
                        lipHeight = 4.dp
                    )
                    .padding(bottom = 4.dp)
                    .padding(14.dp)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Tune,
                                contentDescription = null,
                                tint = liveCustomColor,
                                modifier = Modifier.size(20.dp)
                            )
                            Text(
                                text = "4. Racik Aksen Custom Bebas (RGB & HEX)",
                                style = MaterialTheme.typography.titleSmall,
                                fontFamily = SpaceGroteskFontFamily,
                                fontWeight = FontWeight.Bold,
                                color = CosmicTextPure
                            )
                        }
                        Box(
                            modifier = Modifier
                                .size(28.dp)
                                .clip(CircleShape)
                                .background(liveCustomColor)
                                .border(2.dp, DuoEelInk, CircleShape)
                        )
                    }

                    // RGB Sliders
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text(
                            text = "Merah (R: ${customRed.roundToInt()})",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFEF4444)
                        )
                        Slider(
                            value = customRed,
                            onValueChange = {
                                customRed = it
                                customHexInput = rgbToHex(customRed.roundToInt(), customGreen.roundToInt(), customBlue.roundToInt())
                            },
                            valueRange = 0f..255f,
                            colors = SliderDefaults.colors(
                                thumbColor = Color(0xFFEF4444),
                                activeTrackColor = Color(0xFFEF4444)
                            ),
                            modifier = Modifier.height(26.dp)
                        )

                        Text(
                            text = "Hijau (G: ${customGreen.roundToInt()})",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF16A34A)
                        )
                        Slider(
                            value = customGreen,
                            onValueChange = {
                                customGreen = it
                                customHexInput = rgbToHex(customRed.roundToInt(), customGreen.roundToInt(), customBlue.roundToInt())
                            },
                            valueRange = 0f..255f,
                            colors = SliderDefaults.colors(
                                thumbColor = Color(0xFF16A34A),
                                activeTrackColor = Color(0xFF16A34A)
                            ),
                            modifier = Modifier.height(26.dp)
                        )

                        Text(
                            text = "Biru (B: ${customBlue.roundToInt()})",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF2563EB)
                        )
                        Slider(
                            value = customBlue,
                            onValueChange = {
                                customBlue = it
                                customHexInput = rgbToHex(customRed.roundToInt(), customGreen.roundToInt(), customBlue.roundToInt())
                            },
                            valueRange = 0f..255f,
                            colors = SliderDefaults.colors(
                                thumbColor = Color(0xFF2563EB),
                                activeTrackColor = Color(0xFF2563EB)
                            ),
                            modifier = Modifier.height(26.dp)
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OutlinedTextField(
                            value = customHexInput,
                            onValueChange = { hex ->
                                customHexInput = hex
                                val parsed = AppThemeController.parseHexColor(hex, liveCustomColor)
                                customRed = parsed.red * 255f
                                customGreen = parsed.green * 255f
                                customBlue = parsed.blue * 255f
                            },
                            label = { Text("HEX Utama (#RRGGBB)") },
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = liveCustomColor,
                                unfocusedBorderColor = DuoSwanBorder
                            ),
                            modifier = Modifier
                                .weight(1f)
                                .testTag("custom_palette_hex_input")
                        )

                        OutlinedTextField(
                            value = customAccentHexInput,
                            onValueChange = { customAccentHexInput = it },
                            label = { Text("HEX Aksen Kedua") },
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = liveCustomColor,
                                unfocusedBorderColor = DuoSwanBorder
                            ),
                            modifier = Modifier.weight(1f)
                        )
                    }

                    // Apply Custom Palette Button
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(46.dp)
                            .duolingoButton(
                                faceColor = liveCustomColor,
                                lipColor = AppThemeController.darkenColor(liveCustomColor, 0.72f),
                                cornerRadius = 14.dp,
                                lipHeight = 4.dp
                            )
                            .instantTouchPress {
                                onSelectColorPalette(
                                    AppColorPalettePreset.CUSTOM.id,
                                    customHexInput,
                                    customAccentHexInput
                                )
                            }
                            .padding(bottom = 4.dp)
                            .testTag("apply_custom_palette_button"),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Terapkan Palet Warna Custom ($customHexInput)",
                            style = MaterialTheme.typography.labelLarge,
                            fontFamily = SpaceGroteskFontFamily,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }
            }
        } else if (currentSubTab == 1) {
            // =========================================================================
            // SUB-TAB 1: KAMAR INTERAKTIF MASKOT, LEMARI GRID RINGKAS, AURA, EMOSI, INTERAKSI & KAMAR MODULAR
            // =========================================================================
            var showAdvancedSandbox by remember { mutableStateOf(false) }

            MascotWardrobeStudioCard(
                activeAccessory = activeAccessory,
                activeSkin = activeSkin,
                activeOutfit = activeOutfit,
                activeGloves = activeGloves,
                isMascotOverlayVisible = isMascotOverlayVisible,
                onSetMascotOverlayVisible = onSetMascotOverlayVisible,
                onSelectAccessory = { acc ->
                    onUpdateMascotStudio(acc.id, null, null, null, null, null)
                },
                onSelectSkin = { skin ->
                    onUpdateMascotStudio(null, null, null, skin.id, null, null)
                },
                onSelectOutfit = { outfit ->
                    onUpdateMascotStudio(null, null, null, null, outfit.id, null)
                },
                onSelectGloves = { gloves ->
                    onUpdateMascotStudio(null, null, null, null, null, gloves.id)
                },
                onApplyPreset = { preset ->
                    AppThemeController.applyCostumePreset(preset)
                    onUpdateMascotStudio(
                        preset.accessory.id,
                        null,
                        true,
                        preset.skin.id,
                        preset.outfit.id,
                        preset.gloves.id
                    )
                },
                onRandomizeAll = {
                    AppThemeController.randomizeCostume()
                    onUpdateMascotStudio(
                        AppThemeController.mascotAccessory.id,
                        null,
                        true,
                        AppThemeController.mascotSkin.id,
                        AppThemeController.mascotOutfit.id,
                        AppThemeController.mascotGloves.id
                    )
                },
                onResetDefault = {
                    AppThemeController.resetCostumeToDefault()
                    onUpdateMascotStudio(
                        MascotAccessory.NONE.id,
                        null,
                        true,
                        com.example.ui.theme.MascotBodySkin.DEFAULT_MOOD.id,
                        com.example.ui.theme.MascotUpperOutfit.NONE.id,
                        com.example.ui.theme.MascotHandGloves.DEFAULT_HANDS.id
                    )
                }
            )

            // Compact Collapsible Toggle for 360° Cursor & Form Sandbox
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .duolingoCard(
                        backgroundColor = DuoPureWhite,
                        borderColor = DuoSwanBorder,
                        lipColor = DuoHareLip,
                        cornerRadius = 14.dp,
                        borderWidth = 1.5.dp,
                        lipHeight = 3.dp
                    )
                    .instantTouchPress { showAdvancedSandbox = !showAdvancedSandbox }
                    .padding(bottom = 3.dp)
                    .padding(horizontal = 12.dp, vertical = 9.dp)
                    .testTag("toggle_mascot_360_sandbox")
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(
                            imageVector = if (showAdvancedSandbox) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                            contentDescription = null,
                            tint = DuoEelInk,
                            modifier = Modifier.size(18.dp)
                        )
                        Text(
                            text = if (showAdvancedSandbox) "Tutup Lab Kursor 360° & Tester Form" else "Buka Lab Kursor 360° & Tester Form (Opsional)",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            color = DuoEelInk
                        )
                    }
                    Text(
                        text = if (showAdvancedSandbox) "RINGKASKAN" else "BUKA",
                        fontFamily = JetBrainsMonoFontFamily,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF6366F1)
                    )
                }
            }

            if (showAdvancedSandbox) {
                AbundantMascotAssetAndFormSandboxCard(
                    activePose = activePose,
                    activeAccessory = activeAccessory,
                    onSelectPoseAndAccessory = { pose, accessory ->
                        onUpdateMascotStudio(accessory.id, pose.id, true, null, null, null)
                    }
                )
            }
        } else {
            // =========================================================================
            // SUB-TAB 2: PHOTOBOOTH STIKER MASKOT & KARTU PROFIL 3D HOLOGRAPHIC FOIL
            // =========================================================================
            MascotPhotoboothAndHoloProfileStudio()
        }
            }
        }

        Spacer(modifier = Modifier.height(64.dp))
    }
}

@Composable
private fun DynamicIslandShapeSilhouetteVector(
    shapeStyle: DynamicIslandShapeStyle,
    tint: Color,
    modifier: Modifier = Modifier
) {
    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height
        val stroke = Stroke(width = 2.dp.toPx())
        when (shapeStyle) {
            DynamicIslandShapeStyle.PILL_CAPSULE -> {
                drawRoundRect(
                    color = tint,
                    topLeft = Offset(1f, 1f),
                    size = Size(w - 2f, h - 2f),
                    cornerRadius = CornerRadius(h / 2f, h / 2f),
                    style = stroke
                )
            }
            DynamicIslandShapeStyle.CYBER_HEXAGON -> {
                val cut = h * 0.42f
                val path = Path().apply {
                    moveTo(cut, 1f)
                    lineTo(w - cut, 1f)
                    lineTo(w - 1f, h / 2f)
                    lineTo(w - cut, h - 1f)
                    lineTo(cut, h - 1f)
                    lineTo(1f, h / 2f)
                    close()
                }
                drawPath(path = path, color = tint, style = stroke)
            }
            DynamicIslandShapeStyle.BENTO_CHAMFER_BOX -> {
                drawRoundRect(
                    color = tint,
                    topLeft = Offset(1f, 1f),
                    size = Size(w - 2f, h - 2f),
                    cornerRadius = CornerRadius(4.dp.toPx(), 4.dp.toPx()),
                    style = stroke
                )
            }
            DynamicIslandShapeStyle.TOP_NOTCH_DOCK -> {
                val path = Path().apply {
                    moveTo(1f, 1f)
                    lineTo(w - 1f, 1f)
                    lineTo(w - 4f, h - 1f)
                    lineTo(4f, h - 1f)
                    close()
                }
                drawPath(path = path, color = tint, style = stroke)
            }
            DynamicIslandShapeStyle.CLOUD_PUFF_RIBBON -> {
                drawRoundRect(
                    color = tint,
                    topLeft = Offset(2f, 3f),
                    size = Size(w - 4f, h - 4f),
                    cornerRadius = CornerRadius(h * 0.45f, h * 0.45f),
                    style = stroke
                )
                drawCircle(color = tint, radius = h * 0.25f, center = Offset(w * 0.35f, h * 0.35f), style = stroke)
                drawCircle(color = tint, radius = h * 0.28f, center = Offset(w * 0.65f, h * 0.35f), style = stroke)
            }
            DynamicIslandShapeStyle.RETRO_CASSETTE_TAPE -> {
                drawRoundRect(
                    color = tint,
                    topLeft = Offset(1f, 1f),
                    size = Size(w - 2f, h - 2f),
                    cornerRadius = CornerRadius(3.dp.toPx(), 3.dp.toPx()),
                    style = stroke
                )
                drawCircle(color = tint, radius = h * 0.22f, center = Offset(w * 0.32f, h * 0.5f), style = stroke)
                drawCircle(color = tint, radius = h * 0.22f, center = Offset(w * 0.68f, h * 0.5f), style = stroke)
            }
            DynamicIslandShapeStyle.ROYAL_SCROLL_CREST -> {
                drawRoundRect(
                    color = tint,
                    topLeft = Offset(4f, 2f),
                    size = Size(w - 8f, h - 4f),
                    cornerRadius = CornerRadius(4.dp.toPx(), 4.dp.toPx()),
                    style = stroke
                )
                drawLine(tint, Offset(2f, 1f), Offset(2f, h - 1f), strokeWidth = 2.dp.toPx())
                drawLine(tint, Offset(w - 2f, 1f), Offset(w - 2f, h - 1f), strokeWidth = 2.dp.toPx())
            }
            DynamicIslandShapeStyle.ORGANIC_LEAF_POD -> {
                drawRoundRect(
                    color = tint,
                    topLeft = Offset(1f, 1f),
                    size = Size(w - 2f, h - 2f),
                    cornerRadius = CornerRadius(h * 0.6f, h * 0.2f),
                    style = stroke
                )
                drawLine(tint, Offset(4f, h - 3f), Offset(w - 4f, 3f), strokeWidth = 1.5.dp.toPx())
            }
            DynamicIslandShapeStyle.STEAMPUNK_BRASS_GAUGE -> {
                drawRoundRect(
                    color = tint,
                    topLeft = Offset(2f, 2f),
                    size = Size(w - 4f, h - 4f),
                    cornerRadius = CornerRadius(2.dp.toPx(), 2.dp.toPx()),
                    style = stroke
                )
                drawCircle(color = tint, radius = 1.8f, center = Offset(5f, 5f))
                drawCircle(color = tint, radius = 1.8f, center = Offset(w - 5f, 5f))
                drawCircle(color = tint, radius = 1.8f, center = Offset(5f, h - 5f))
                drawCircle(color = tint, radius = 1.8f, center = Offset(w - 5f, h - 5f))
            }
            DynamicIslandShapeStyle.CRYSTAL_PRISM_SHARD -> {
                val path = Path().apply {
                    moveTo(w * 0.22f, 1f)
                    lineTo(w * 0.78f, 1f)
                    lineTo(w - 1f, h * 0.5f)
                    lineTo(w * 0.78f, h - 1f)
                    lineTo(w * 0.22f, h - 1f)
                    lineTo(1f, h * 0.5f)
                    close()
                }
                drawPath(path = path, color = tint, style = stroke)
            }
        }
    }
}
