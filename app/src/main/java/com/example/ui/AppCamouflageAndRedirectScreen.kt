package com.example.ui

import android.app.Activity
import android.app.ActivityManager
import android.app.Service
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.graphics.PixelFormat
import android.graphics.RectF
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.net.Uri
import android.os.Build
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.provider.Settings
import android.text.Editable
import android.text.InputFilter
import android.text.InputType
import android.text.TextWatcher
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import android.widget.Button as AndroidButton
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import kotlinx.coroutines.delay
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.Canvas as ComposeCanvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Backspace
import androidx.compose.material.icons.automirrored.filled.OpenInNew
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AppShortcut
import androidx.compose.material.icons.filled.Apps
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.LockOpen
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.RestartAlt
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.SportsEsports
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.MainActivity
import com.example.data.SecurityConfig
import com.example.ui.theme.AppThemeController
import com.example.ui.theme.CosmicBorderHairline
import com.example.ui.theme.CosmicGlass
import com.example.ui.theme.CosmicObsidian
import com.example.ui.theme.CosmicTextMuted
import com.example.ui.theme.CosmicTextPure
import com.example.ui.theme.CosmicTextSubtle
import com.example.ui.theme.DuoBeeYellow
import com.example.ui.theme.DuoBeeYellowLip
import com.example.ui.theme.DuoBeeYellowSoft
import com.example.ui.theme.DuoBeetlePurple
import com.example.ui.theme.DuoBeetlePurpleLip
import com.example.ui.theme.DuoBeetlePurpleSoft
import com.example.ui.theme.DuoCardinalRed
import com.example.ui.theme.DuoCardinalRedLip
import com.example.ui.theme.DuoCardinalRedSoft
import com.example.ui.theme.DuoFeatherGreen
import com.example.ui.theme.DuoFeatherGreenLip
import com.example.ui.theme.DuoFeatherGreenSoft
import com.example.ui.theme.DuoFoxOrange
import com.example.ui.theme.DuoFoxOrangeLip
import com.example.ui.theme.DuoFoxOrangeSoft
import com.example.ui.theme.DuoHareLip
import com.example.ui.theme.DuoMacawBlue
import com.example.ui.theme.DuoMacawBlueLip
import com.example.ui.theme.DuoMacawBlueSoft
import com.example.ui.theme.DuoPureWhite
import com.example.ui.theme.DuoSwanBorder
import com.example.ui.theme.JetBrainsMonoFontFamily
import com.example.ui.theme.StarlightCyan
import com.example.ui.theme.VaultCoral
import com.example.ui.theme.VaultLime
import com.example.ui.theme.VaultObsidian
import com.example.ui.theme.VaultPureWhite
import com.example.ui.theme.duolingoButton
import com.example.ui.theme.duolingoCard
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.math.roundToInt

/**
 * DRAFT FEATURE FLAG:
 * Diatur ke `false` agar fitur Kamuflase Aplikasi, Auto-Buka Aplikasi Lain & System Overlay Maskot
 * disembunyikan (di-hide) dari Tab & Navigasi tanpa menghapus kode draft-nya.
 * Ubah ke `true` kapan saja jika ingin melanjutkan/mengaktifkan kembali fitur ini.
 */
const val DRAFT_APP_CAMOUFLAGE_FEATURE_ENABLED: Boolean = false

/**
 * Saat fitur Kamuflase berstatus Draft (`DRAFT_APP_CAMOUFLAGE_FEATURE_ENABLED == false`),
 * pastikan overlay sistem dimatikan dan alias ikon penyamaran dinonaktifkan agar aplikasi berjalan normal.
 */
fun ensureDefaultArcanumLauncherIdentityWhenDraftDisabled(context: Context) {
    if (DRAFT_APP_CAMOUFLAGE_FEATURE_ENABLED) return
    try {
        SystemMascotOverlayManager.dismissSystemOverlay(context)
    } catch (_: Exception) {
    }
    try {
        val pm = context.packageManager
        val pkg = context.packageName
        val allAliases = listOf(
            "com.example.DisguiseCalculatorAlias",
            "com.example.DisguiseNotesAlias",
            "com.example.DisguiseWeatherAlias",
            "com.example.DisguiseBrowserAlias"
        )
        for (aliasName in allAliases) {
            val comp = ComponentName(pkg, aliasName)
            if (pm.getComponentEnabledSetting(comp) == PackageManager.COMPONENT_ENABLED_STATE_ENABLED) {
                pm.setComponentEnabledSetting(
                    comp,
                    PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                    PackageManager.DONT_KILL_APP
                )
            }
        }
    } catch (_: Exception) {
    }
}

enum class DisguisePresetOption(
    val id: String,
    val appTitle: String,
    val subtitle: String,
    val aliasClassName: String?,
    val iconId: String,
    val colorHex: String,
    val defaultTargetAppId: String
) {
    CALCULATOR(
        id = "CALCULATOR",
        appTitle = "Kalkulator Pro",
        subtitle = "Ikon & nama Kalkulator Ilmiah standar",
        aliasClassName = "com.example.DisguiseCalculatorAlias",
        iconId = "CALC",
        colorHex = "#0EA5E9",
        defaultTargetAppId = "BUILTIN_CALCULATOR"
    ),
    NOTES(
        id = "NOTES",
        appTitle = "Catatan Harian",
        subtitle = "Ikon & nama buku memo catatan kerja",
        aliasClassName = "com.example.DisguiseNotesAlias",
        iconId = "NOTES",
        colorHex = "#F59E0B",
        defaultTargetAppId = "BUILTIN_NOTES"
    ),
    WEATHER(
        id = "WEATHER",
        appTitle = "Cuaca Live",
        subtitle = "Ikon & nama radar prakiraan cuaca",
        aliasClassName = "com.example.DisguiseWeatherAlias",
        iconId = "WEATHER",
        colorHex = "#0284C7",
        defaultTargetAppId = "BUILTIN_WEATHER"
    ),
    BROWSER(
        id = "BROWSER",
        appTitle = "Browser Cepat",
        subtitle = "Ikon & nama peramban web ringan",
        aliasClassName = "com.example.DisguiseBrowserAlias",
        iconId = "BROWSER",
        colorHex = "#3B82F6",
        defaultTargetAppId = "BUILTIN_BROWSER"
    ),
    DEFAULT_ARCANUM(
        id = "DEFAULT",
        appTitle = "Arcanum",
        subtitle = "Ikon & nama asli Arcanum Vault",
        aliasClassName = null,
        iconId = "SHIELD",
        colorHex = "#58CC02",
        defaultTargetAppId = "BUILTIN_CALCULATOR"
    );

    companion object {
        fun fromId(id: String): DisguisePresetOption =
            entries.find { it.id.equals(id, ignoreCase = true) } ?: CALCULATOR
    }
}

enum class CustomDisguiseIconOption(
    val id: String,
    val label: String,
    val vector: ImageVector,
    val glyphSymbol: String
) {
    CALC("CALC", "Kalkulator", Icons.Default.Calculate, "+="),
    NOTES("NOTES", "Catatan", Icons.Default.Description, "NT"),
    WEATHER("WEATHER", "Cuaca", Icons.Default.WbSunny, "WX"),
    BROWSER("BROWSER", "Browser", Icons.Default.Language, "WB"),
    CALENDAR("CALENDAR", "Kalender", Icons.Default.CalendarMonth, "31"),
    SCANNER("SCANNER", "Scanner", Icons.Default.QrCodeScanner, "QR"),
    MUSIC("MUSIC", "Musik", Icons.Default.MusicNote, "FM"),
    CAMERA("CAMERA", "Kamera", Icons.Default.PhotoCamera, "HD"),
    GAME("GAME", "Game", Icons.Default.SportsEsports, "GX"),
    SHOP("SHOP", "Toko", Icons.Default.ShoppingBag, "SH"),
    SHIELD("SHIELD", "Perisai", Icons.Default.Security, "VA");

    companion object {
        fun fromId(id: String): CustomDisguiseIconOption =
            entries.find { it.id.equals(id, ignoreCase = true) } ?: CALC
    }
}

data class CamouflageTargetAppItem(
    val id: String,
    val label: String,
    val subtitle: String,
    val isBuiltIn: Boolean,
    val builtInIconId: String = "CALC",
    val builtInColorHex: String = "#0EA5E9",
    val installedPackageName: String? = null,
    val installedIconBitmap: ImageBitmap? = null
)

private val BUILT_IN_TARGET_APPS = listOf(
    CamouflageTargetAppItem(
        id = "BUILTIN_CALCULATOR",
        label = "Kalkulator Ilmiah Pro",
        subtitle = "Aplikasi Bawaan Interaktif • Hitung matematika nyata + Riwayat",
        isBuiltIn = true,
        builtInIconId = "CALC",
        builtInColorHex = "#0EA5E9"
    ),
    CamouflageTargetAppItem(
        id = "BUILTIN_NOTES",
        label = "Catatan & Memo Harian",
        subtitle = "Aplikasi Bawaan Interaktif • Tulis & simpan daftar catatan harian",
        isBuiltIn = true,
        builtInIconId = "NOTES",
        builtInColorHex = "#F59E0B"
    ),
    CamouflageTargetAppItem(
        id = "BUILTIN_WEATHER",
        label = "Cuaca & Radar Live",
        subtitle = "Aplikasi Bawaan Interaktif • Suhu kota, kelembapan & prakiraan 7 hari",
        isBuiltIn = true,
        builtInIconId = "WEATHER",
        builtInColorHex = "#0284C7"
    ),
    CamouflageTargetAppItem(
        id = "BUILTIN_BROWSER",
        label = "Browser Web Cepat",
        subtitle = "Aplikasi Bawaan Interaktif • Pencarian cepat & pembaca berita",
        isBuiltIn = true,
        builtInIconId = "BROWSER",
        builtInColorHex = "#3B82F6"
    )
)

private fun parseColorSafe(hex: String, fallback: Color = DuoMacawBlue): Color {
    return try {
        val clean = hex.trim().removePrefix("#")
        val value = clean.toLong(16)
        Color(0xFF000000L or value)
    } catch (_: Exception) {
        fallback
    }
}

private fun drawableToImageBitmap(drawable: Drawable, sizePx: Int = 96): ImageBitmap {
    val bmp = Bitmap.createBitmap(sizePx, sizePx, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bmp)
    drawable.setBounds(0, 0, sizePx, sizePx)
    drawable.draw(canvas)
    return bmp.asImageBitmap()
}

private fun createCustomDisguiseBitmap(
    colorHex: String,
    glyphText: String,
    sizePx: Int = 192
): Bitmap {
    val bmp = Bitmap.createBitmap(sizePx, sizePx, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bmp)
    val bgPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = parseColorSafe(colorHex).toArgb()
        style = Paint.Style.FILL
    }
    val rect = RectF(12f, 12f, (sizePx - 12).toFloat(), (sizePx - 12).toFloat())
    canvas.drawRoundRect(rect, 44f, 44f, bgPaint)

    val borderPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = android.graphics.Color.WHITE
        alpha = 90
        style = Paint.Style.STROKE
        strokeWidth = 6f
    }
    canvas.drawRoundRect(rect, 44f, 44f, borderPaint)

    val textPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = android.graphics.Color.WHITE
        textSize = sizePx * 0.42f
        textAlign = Paint.Align.CENTER
        isFakeBoldText = true
    }
    val fm = textPaint.fontMetrics
    val centerY = sizePx / 2f - (fm.ascent + fm.descent) / 2f
    canvas.drawText(glyphText.take(2), sizePx / 2f, centerY, textPaint)
    return bmp
}

/**
 * Applies the selected disguise icon & title to:
 * 1. Recent Tasks / Window Title (`setTaskDescription`)
 * 2. Launcher `<activity-alias>` components (`DONT_KILL_APP`) when `applyLauncherAlias = true`
 *    so the actual Arcanum app icon & name on the launcher changes to match the chosen app.
 */
fun syncSystemDisguiseIdentity(
    context: Context,
    identityMode: String,
    presetId: String,
    customAppName: String,
    customIconId: String,
    customColorHex: String,
    targetAppLabel: String,
    targetAppId: String = "",
    applyLauncherAlias: Boolean = false
) {
    val effectiveLabel = when (identityMode.uppercase()) {
        "CUSTOM" -> customAppName.trim().ifBlank { "Kalkulator Pro" }
        "MATCH_TARGET_APP" -> targetAppLabel.trim().ifBlank { "Kalkulator Ilmiah Pro" }
        else -> DisguisePresetOption.fromId(presetId).appTitle
    }
    val effectiveColorHex = when (identityMode.uppercase()) {
        "CUSTOM" -> customColorHex
        "MATCH_TARGET_APP" -> "#0EA5E9"
        else -> DisguisePresetOption.fromId(presetId).colorHex
    }
    val effectiveIconOption = when (identityMode.uppercase()) {
        "CUSTOM" -> CustomDisguiseIconOption.fromId(customIconId)
        "MATCH_TARGET_APP" -> when {
            targetAppId.contains("NOTES", ignoreCase = true) ||
                targetAppLabel.contains("Catatan", ignoreCase = true) ||
                targetAppLabel.contains("Note", ignoreCase = true) -> CustomDisguiseIconOption.NOTES
            targetAppId.contains("WEATHER", ignoreCase = true) ||
                targetAppLabel.contains("Cuaca", ignoreCase = true) ||
                targetAppLabel.contains("Weather", ignoreCase = true) -> CustomDisguiseIconOption.WEATHER
            targetAppId.contains("BROWSER", ignoreCase = true) ||
                targetAppLabel.contains("Chrome", ignoreCase = true) ||
                targetAppLabel.contains("Browser", ignoreCase = true) -> CustomDisguiseIconOption.BROWSER
            else -> CustomDisguiseIconOption.CALC
        }
        else -> CustomDisguiseIconOption.fromId(DisguisePresetOption.fromId(presetId).iconId)
    }

    // 1. Update Activity title & Recent Apps TaskDescription immediately
    try {
        val activity = context as? Activity
        if (activity != null) {
            activity.title = effectiveLabel
            val iconBmp = createCustomDisguiseBitmap(
                colorHex = effectiveColorHex,
                glyphText = effectiveIconOption.glyphSymbol,
                sizePx = 128
            )
            @Suppress("DEPRECATION")
            val taskDesc = ActivityManager.TaskDescription(
                effectiveLabel,
                iconBmp,
                parseColorSafe(effectiveColorHex).toArgb()
            )
            activity.setTaskDescription(taskDesc)
        }
    } catch (_: Exception) {
    }

    // 2. Enable matching launcher activity-alias ONLY when applying camouflage so editing in Settings never resets the Activity
    if (applyLauncherAlias) {
        try {
            val pm = context.packageManager
            val pkg = context.packageName
            val allAliases = listOf(
                "com.example.DisguiseCalculatorAlias",
                "com.example.DisguiseNotesAlias",
                "com.example.DisguiseWeatherAlias",
                "com.example.DisguiseBrowserAlias"
            )
            val targetAlias = when {
                identityMode.equals("PRESET", ignoreCase = true) ->
                    DisguisePresetOption.fromId(presetId).aliasClassName
                effectiveIconOption == CustomDisguiseIconOption.NOTES ->
                    "com.example.DisguiseNotesAlias"
                effectiveIconOption == CustomDisguiseIconOption.WEATHER ->
                    "com.example.DisguiseWeatherAlias"
                effectiveIconOption == CustomDisguiseIconOption.BROWSER ->
                    "com.example.DisguiseBrowserAlias"
                else ->
                    "com.example.DisguiseCalculatorAlias"
            }

            for (aliasName in allAliases) {
                val comp = ComponentName(pkg, aliasName)
                val newState = if (aliasName == targetAlias) {
                    PackageManager.COMPONENT_ENABLED_STATE_ENABLED
                } else {
                    PackageManager.COMPONENT_ENABLED_STATE_DISABLED
                }
                if (pm.getComponentEnabledSetting(comp) != newState) {
                    pm.setComponentEnabledSetting(comp, newState, PackageManager.DONT_KILL_APP)
                }
            }
        } catch (_: Exception) {
        }
    }
}

/**
 * Membuka halaman Pengaturan Sistem Android "Tampilkan di atas aplikasi lain" (Appear on top / Draw over other apps)
 * langsung mengarah ke paket aplikasi ini.
 */
fun openSystemOverlayPermissionSettings(context: Context) {
    try {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val intent = Intent(
                Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                Uri.parse("package:${context.packageName}")
            ).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(intent)
            Toast.makeText(
                context,
                "Aktifkan sakelar 'Tampilkan di atas aplikasi lain' lalu kembali ke aplikasi!",
                Toast.LENGTH_LONG
            ).show()
            return
        }
    } catch (_: Exception) {
    }
    try {
        val fallback = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        context.startActivity(fallback)
    } catch (_: Exception) {
        openAppDetailsPopupPermissionSettings(context)
    }
}

/**
 * Membuka halaman Detail Info Aplikasi & Izin Lainnya (untuk mengaktifkan Pop-Up Latar Belakang di Xiaomi/MIUI, OPPO, Vivo, Realme, Samsung).
 */
fun openAppDetailsPopupPermissionSettings(context: Context) {
    try {
        val miuiIntent = Intent("miui.intent.action.APP_PERM_EDITOR").apply {
            setClassName(
                "com.miui.securitycenter",
                "com.miui.permcenter.permissions.PermissionsEditorActivity"
            )
            putExtra("extra_pkgname", context.packageName)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        context.startActivity(miuiIntent)
        return
    } catch (_: Exception) {
    }
    try {
        val intent = Intent(
            Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
            Uri.parse("package:${context.packageName}")
        ).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        context.startActivity(intent)
        Toast.makeText(
            context,
            "Izinkan 'Tampilkan di atas aplikasi lain' & 'Pop-up latar belakang'!",
            Toast.LENGTH_LONG
        ).show()
    } catch (_: Exception) {
    }
}

/**
 * Layanan Android Persisten (`Service`) yang menjaga Jendela Floating Mascot & Input PIN Overlay
 * tetap hidup di atas aplikasi eksternal (`PKG:...`) saat `MainActivity` berpindah ke latar belakang.
 */
class CamouflageMascotOverlayService : Service() {
    override fun onBind(intent: Intent?): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val action = intent?.getStringExtra(EXTRA_ACTION).orEmpty()
        if (action == ACTION_DISMISS) {
            SystemMascotOverlayManager.removeOverlayWindowOnly(applicationContext)
            stopSelf()
            return START_NOT_STICKY
        }
        val targetLabel = intent?.getStringExtra(EXTRA_TARGET_LABEL)
            ?.takeIf { it.isNotBlank() }
            ?: SystemMascotOverlayManager.lastTargetAppLabel
        SystemMascotOverlayManager.attachOrUpdateOverlayWindow(
            context = applicationContext,
            targetAppLabel = targetLabel
        )
        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    companion object {
        const val EXTRA_ACTION = "extra_overlay_action"
        const val EXTRA_TARGET_LABEL = "extra_target_label"
        const val ACTION_SHOW = "ACTION_SHOW_MASCOT_OVERLAY"
        const val ACTION_DISMISS = "ACTION_DISMISS_MASCOT_OVERLAY"
    }
}

/**
 * Custom Canvas View untuk menggambar Maskot 3D Kawaii Arcanum di dalam jendela `TYPE_APPLICATION_OVERLAY`
 * sehingga tampak hidup, mencolok, dan mudah diketuk di atas aplikasi asli mana pun.
 */
private class FloatingMascotBubbleCanvasView(context: Context) : View(context) {
    private val density = context.resources.displayMetrics.density
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val bodyRect = RectF()
    private val tabRect = RectF()
    private val pillRect = RectF()
    private val crownPath = Path()

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val w = width.toFloat()
        val h = height.toFloat()
        if (w <= 0f || h <= 0f) return

        val cx = w * 0.5f
        val cy = h * 0.45f
        val r = minOf(w, h) * 0.39f

        // 1. Outer Cosmic Glow Ring
        paint.style = Paint.Style.FILL
        paint.color = android.graphics.Color.parseColor("#4D58CC02")
        canvas.drawCircle(cx, cy, r * 1.18f, paint)

        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 2.2f * density
        paint.color = android.graphics.Color.parseColor("#FF58CC02")
        canvas.drawCircle(cx, cy, r * 1.08f, paint)

        // 2. Dark Rounded Shield Pod Backdrop
        paint.style = Paint.Style.FILL
        paint.color = android.graphics.Color.parseColor("#F20F172A")
        canvas.drawCircle(cx, cy, r * 1.02f, paint)

        // 3. 3D Kawaii Folder Mascot Lip (Shadow)
        val bw = r * 1.28f
        val bh = r * 1.02f
        bodyRect.set(cx - bw / 2f, cy - bh / 2f + 3f * density, cx + bw / 2f, cy + bh / 2f + 4.5f * density)
        paint.color = android.graphics.Color.parseColor("#FF46A302")
        canvas.drawRoundRect(bodyRect, 11f * density, 11f * density, paint)

        // Folder Top Tab
        tabRect.set(cx - bw * 0.44f, cy - bh * 0.66f, cx - bw * 0.02f, cy - bh * 0.25f)
        paint.color = android.graphics.Color.parseColor("#FF58CC02")
        canvas.drawRoundRect(tabRect, 6f * density, 6f * density, paint)

        // 4. Folder Main Face
        bodyRect.set(cx - bw / 2f, cy - bh / 2f, cx + bw / 2f, cy + bh / 2f)
        paint.color = android.graphics.Color.parseColor("#FF58CC02")
        canvas.drawRoundRect(bodyRect, 11f * density, 11f * density, paint)

        // Top Gloss Highlight
        bodyRect.set(cx - bw * 0.40f, cy - bh * 0.40f, cx + bw * 0.40f, cy - bh * 0.22f)
        paint.color = android.graphics.Color.parseColor("#55FFFFFF")
        canvas.drawRoundRect(bodyRect, 5f * density, 5f * density, paint)

        // 5. Golden Royal Crown on Top
        val crownBaseY = cy - bh * 0.50f
        val crownW = bw * 0.56f
        val crownH = bh * 0.34f
        crownPath.reset()
        crownPath.moveTo(cx - crownW / 2f, crownBaseY)
        crownPath.lineTo(cx - crownW * 0.42f, crownBaseY - crownH)
        crownPath.lineTo(cx - crownW * 0.15f, crownBaseY - crownH * 0.42f)
        crownPath.lineTo(cx, crownBaseY - crownH * 1.15f)
        crownPath.lineTo(cx + crownW * 0.15f, crownBaseY - crownH * 0.42f)
        crownPath.lineTo(cx + crownW * 0.42f, crownBaseY - crownH)
        crownPath.lineTo(cx + crownW / 2f, crownBaseY)
        crownPath.close()
        paint.color = android.graphics.Color.parseColor("#FFFFC800")
        canvas.drawPath(crownPath, paint)

        // 6. Kawaii Eyes + Sparkles + Cheeks
        val eyeOffsetX = bw * 0.21f
        val eyeY = cy - bh * 0.03f
        val eyeR = r * 0.16f
        paint.color = android.graphics.Color.parseColor("#FF0F172A")
        canvas.drawCircle(cx - eyeOffsetX, eyeY, eyeR, paint)
        canvas.drawCircle(cx + eyeOffsetX, eyeY, eyeR, paint)

        paint.color = android.graphics.Color.WHITE
        canvas.drawCircle(cx - eyeOffsetX - eyeR * 0.28f, eyeY - eyeR * 0.28f, eyeR * 0.38f, paint)
        canvas.drawCircle(cx + eyeOffsetX - eyeR * 0.28f, eyeY - eyeR * 0.28f, eyeR * 0.38f, paint)

        // Pink Blush Cheeks
        paint.color = android.graphics.Color.parseColor("#99FF6B9D")
        canvas.drawCircle(cx - bw * 0.33f, eyeY + eyeR * 1.15f, eyeR * 0.65f, paint)
        canvas.drawCircle(cx + bw * 0.33f, eyeY + eyeR * 1.15f, eyeR * 0.65f, paint)

        // Happy Smile Arc
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 2.0f * density
        paint.strokeCap = Paint.Cap.ROUND
        paint.color = android.graphics.Color.parseColor("#FF0F172A")
        bodyRect.set(cx - bw * 0.14f, eyeY + eyeR * 0.4f, cx + bw * 0.14f, eyeY + eyeR * 1.75f)
        canvas.drawArc(bodyRect, 15f, 150f, false, paint)

        // 7. Bottom Pill Badge "TAP PIN"
        paint.style = Paint.Style.FILL
        val pillW = w * 0.90f
        val pillH = 18f * density
        val pillTop = h - pillH - 2f * density
        pillRect.set((w - pillW) / 2f, pillTop, (w + pillW) / 2f, pillTop + pillH)
        paint.color = android.graphics.Color.parseColor("#FF1CB0F6")
        canvas.drawRoundRect(pillRect, pillH / 2f, pillH / 2f, paint)

        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 1.3f * density
        paint.color = android.graphics.Color.WHITE
        canvas.drawRoundRect(pillRect, pillH / 2f, pillH / 2f, paint)

        paint.style = Paint.Style.FILL
        paint.color = android.graphics.Color.WHITE
        paint.textSize = 9.5f * density
        paint.textAlign = Paint.Align.CENTER
        paint.isFakeBoldText = true
        val fm = paint.fontMetrics
        val textY = pillRect.centerY() - (fm.ascent + fm.descent) / 2f
        canvas.drawText("TAP PIN", cx, textY, paint)
    }
}

/**
 * System-Wide Floating Mascot & Input PIN Overlay Manager (`WindowManager` `TYPE_APPLICATION_OVERLAY`).
 * Mengapung langsung di atas aplikasi eksternal yang dibuka secara kilat:
 * - Klik Maskot -> Buka Input PIN Overlay (lengkap dengan Keypad 0-9 bawaan + input teks) tanpa keluar dari aplikasi yang sedang dibuka.
 * - Jika PIN SALAH -> Overlay langsung dimatikan/menghilang dan pengguna tetap berada di aplikasi tujuan!
 * - Jika PIN BENAR -> Overlay ditutup & langsung membuka Arcanum Vault / Ruang Pengaturan!
 */
object SystemMascotOverlayManager {
    private var overlayRootView: View? = null
    private val mainHandler = Handler(Looper.getMainLooper())

    @Volatile
    var lastTargetAppLabel: String = "Aplikasi Tujuan"
        private set

    @Volatile
    private var activePinVerifier: ((String) -> Boolean)? = null

    @Volatile
    private var cachedSecurityConfig: SecurityConfig = SecurityConfig()

    @Volatile
    private var pendingVerifiedPinForMainActivity: String? = null

    fun updateCachedSecurityConfig(config: SecurityConfig) {
        cachedSecurityConfig = config
    }

    fun consumePendingVerifiedPin(): String? {
        val pin = pendingVerifiedPinForMainActivity
        pendingVerifiedPinForMainActivity = null
        return pin
    }

    private fun verifyPinAgainstCachedConfig(submittedPin: String): Boolean {
        val clean = submittedPin.uppercase().trim()
        if (clean.isEmpty()) return false
        val cfg = cachedSecurityConfig

        val (sp1, sp2, sp3) = cfg.getSettingsPins()
        if (clean.equals(sp1, ignoreCase = true) ||
            clean.equals(sp2, ignoreCase = true) ||
            clean.equals(sp3, ignoreCase = true)
        ) {
            return true
        }

        val (fp1, fp2, fp3) = cfg.getFunZonePins()
        if (clean.equals(fp1, ignoreCase = true) ||
            clean.equals(fp2, ignoreCase = true) ||
            clean.equals(fp3, ignoreCase = true)
        ) {
            return true
        }

        val maxV = cfg.vaultCount.coerceIn(1, 10)
        for (vId in 1..maxV) {
            val (vp1, vp2, vp3) = cfg.getPinsForVault(vId)
            if (clean.equals(vp1, ignoreCase = true) ||
                clean.equals(vp2, ignoreCase = true) ||
                clean.equals(vp3, ignoreCase = true)
            ) {
                return true
            }
        }
        return false
    }

    fun canDrawSystemOverlays(context: Context): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Settings.canDrawOverlays(context)
        } else {
            true
        }
    }

    internal fun removeOverlayWindowOnly(context: Context) {
        mainHandler.removeCallbacksAndMessages(null)
        val view = overlayRootView ?: return
        overlayRootView = null
        try {
            val wm = context.applicationContext.getSystemService(Context.WINDOW_SERVICE) as? WindowManager
            wm?.removeViewImmediate(view)
        } catch (_: Exception) {
            try {
                val wm = context.applicationContext.getSystemService(Context.WINDOW_SERVICE) as? WindowManager
                wm?.removeView(view)
            } catch (_: Exception) {
            }
        }
    }

    fun dismissSystemOverlay(context: Context) {
        val appCtx = context.applicationContext
        removeOverlayWindowOnly(appCtx)
        try {
            appCtx.stopService(Intent(appCtx, CamouflageMascotOverlayService::class.java))
        } catch (_: Exception) {
        }
    }

    fun showSystemMascotOverlay(
        context: Context,
        targetAppLabel: String,
        securityConfig: SecurityConfig? = null,
        onVerifyPin: ((String) -> Boolean)? = null
    ): Boolean {
        if (!canDrawSystemOverlays(context)) return false
        val appCtx = context.applicationContext
        if (securityConfig != null) {
            cachedSecurityConfig = securityConfig
        }
        if (onVerifyPin != null) {
            activePinVerifier = onVerifyPin
        }
        lastTargetAppLabel = targetAppLabel.ifBlank { "Aplikasi Tujuan" }

        // Start persistent background overlay service so Android keeps the window alive across app switches
        try {
            val serviceIntent = Intent(appCtx, CamouflageMascotOverlayService::class.java).apply {
                putExtra(CamouflageMascotOverlayService.EXTRA_ACTION, CamouflageMascotOverlayService.ACTION_SHOW)
                putExtra(CamouflageMascotOverlayService.EXTRA_TARGET_LABEL, lastTargetAppLabel)
            }
            appCtx.startService(serviceIntent)
        } catch (_: Exception) {
        }

        // Attach immediately AND schedule post-transition re-assertions so the overlay stays on top of the external app
        attachOrUpdateOverlayWindow(appCtx, lastTargetAppLabel)
        mainHandler.postDelayed({
            if (canDrawSystemOverlays(appCtx)) {
                ensureOverlayAttachedOnTop(appCtx, lastTargetAppLabel)
            }
        }, 220L)
        mainHandler.postDelayed({
            if (canDrawSystemOverlays(appCtx)) {
                ensureOverlayAttachedOnTop(appCtx, lastTargetAppLabel)
            }
        }, 550L)
        mainHandler.postDelayed({
            if (canDrawSystemOverlays(appCtx)) {
                ensureOverlayAttachedOnTop(appCtx, lastTargetAppLabel)
            }
        }, 1050L)
        return true
    }

    internal fun ensureOverlayAttachedOnTop(context: Context, targetAppLabel: String) {
        val appCtx = context.applicationContext
        if (!canDrawSystemOverlays(appCtx)) return
        val existing = overlayRootView
        if (existing == null || !existing.isAttachedToWindow) {
            attachOrUpdateOverlayWindow(appCtx, targetAppLabel)
        } else {
            try {
                existing.bringToFront()
                existing.invalidate()
            } catch (_: Exception) {
            }
        }
    }

    internal fun attachOrUpdateOverlayWindow(
        context: Context,
        targetAppLabel: String
    ) {
        if (!canDrawSystemOverlays(context)) return
        val appCtx = context.applicationContext
        val existing = overlayRootView
        if (existing != null) {
            try {
                val wm = appCtx.getSystemService(Context.WINDOW_SERVICE) as? WindowManager
                wm?.removeViewImmediate(existing)
            } catch (_: Exception) {
            }
            overlayRootView = null
        }

        try {
            val wm = appCtx.getSystemService(Context.WINDOW_SERVICE) as? WindowManager ?: return
            val density = appCtx.resources.displayMetrics.density
            fun dp(v: Int): Int = (v * density).roundToInt()

            val windowType = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
            } else {
                @Suppress("DEPRECATION")
                WindowManager.LayoutParams.TYPE_PHONE
            }

            val params = WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                windowType,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or
                    WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL or
                    WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH or
                    WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN,
                PixelFormat.TRANSLUCENT
            ).apply {
                gravity = Gravity.BOTTOM or Gravity.END
                x = dp(16)
                y = dp(92)
            }

            val rootContainer = LinearLayout(appCtx).apply {
                orientation = LinearLayout.VERTICAL
                gravity = Gravity.END
            }

            // 1. Floating 3D Kawaii Mascot Bubble View
            val mascotBubble = FloatingMascotBubbleCanvasView(appCtx)

            // 2. Expandable In-Place PIN Input Card + Built-In 0-9 Keypad
            val pinCard = LinearLayout(appCtx).apply {
                orientation = LinearLayout.VERTICAL
                visibility = View.GONE
                setPadding(dp(16), dp(14), dp(16), dp(14))
                background = GradientDrawable().apply {
                    shape = GradientDrawable.RECTANGLE
                    cornerRadius = dp(22).toFloat()
                    setColor(android.graphics.Color.parseColor("#F70F172A"))
                    setStroke(dp(2), android.graphics.Color.parseColor("#58CC02"))
                }
            }

            val headerRow = LinearLayout(appCtx).apply {
                orientation = LinearLayout.HORIZONTAL
                gravity = Gravity.CENTER_VERTICAL
            }
            val titleTv = TextView(appCtx).apply {
                text = "PIN OVERLAY ARCANUM"
                textSize = 13.5f
                setTextColor(android.graphics.Color.parseColor("#58CC02"))
                paint.isFakeBoldText = true
            }
            headerRow.addView(
                titleTv,
                LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f)
            )

            val subTv = TextView(appCtx).apply {
                text = "Melayang di atas $targetAppLabel.\n• PIN Benar = Masuk Arcanum\n• PIN Salah = Overlay Mati & Tetap di $targetAppLabel"
                textSize = 10.5f
                setTextColor(android.graphics.Color.parseColor("#CBD5E1"))
                setPadding(0, dp(4), 0, dp(8))
            }

            val pinSlotIndicatorTv = TextView(appCtx).apply {
                text = "_  _  _  _  _  _"
                textSize = 16f
                gravity = Gravity.CENTER
                setTextColor(android.graphics.Color.parseColor("#38BDF8"))
                paint.isFakeBoldText = true
                setPadding(dp(8), dp(6), dp(8), dp(6))
                background = GradientDrawable().apply {
                    shape = GradientDrawable.RECTANGLE
                    cornerRadius = dp(12).toFloat()
                    setColor(android.graphics.Color.parseColor("#1E293B"))
                    setStroke(dp(1), android.graphics.Color.parseColor("#334155"))
                }
            }

            fun updateSlotIndicator(currentPin: String) {
                val slots = (0 until 6).joinToString("  ") { idx ->
                    if (idx < currentPin.length) "*" else "_"
                }
                pinSlotIndicatorTv.text = slots
            }

            val pinInput = EditText(appCtx).apply {
                hint = "Ketik atau tekan tombol angka..."
                setHintTextColor(android.graphics.Color.parseColor("#64748B"))
                setTextColor(android.graphics.Color.WHITE)
                textSize = 13f
                gravity = Gravity.CENTER
                inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                filters = arrayOf(InputFilter.LengthFilter(6))
                setPadding(dp(10), dp(8), dp(10), dp(8))
                background = GradientDrawable().apply {
                    shape = GradientDrawable.RECTANGLE
                    cornerRadius = dp(10).toFloat()
                    setColor(android.graphics.Color.parseColor("#0F172A"))
                    setStroke(dp(1), android.graphics.Color.parseColor("#38BDF8"))
                }
            }

            fun executePinSubmission(rawPin: String) {
                val candidate = rawPin.trim()
                val callbackResult = activePinVerifier?.invoke(candidate) ?: false
                val configResult = verifyPinAgainstCachedConfig(candidate)
                val isPinValid = callbackResult || configResult

                // Baik benar maupun salah, langsung matikan overlay sistem!
                dismissSystemOverlay(appCtx)

                if (isPinValid) {
                    pendingVerifiedPinForMainActivity = candidate.uppercase()
                    try {
                        val bringToFront = Intent(appCtx, MainActivity::class.java).apply {
                            addFlags(
                                Intent.FLAG_ACTIVITY_NEW_TASK or
                                    Intent.FLAG_ACTIVITY_REORDER_TO_FRONT or
                                    Intent.FLAG_ACTIVITY_SINGLE_TOP or
                                    Intent.FLAG_ACTIVITY_CLEAR_TOP
                            )
                            putExtra("EXTRA_OVERLAY_VERIFIED_PIN", candidate.uppercase())
                        }
                        appCtx.startActivity(bringToFront)
                    } catch (_: Exception) {
                    }
                }
            }

            pinInput.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    updateSlotIndicator(s?.toString().orEmpty())
                }
                override fun afterTextChanged(s: Editable?) {}
            })

            // Built-in Tactile 0-9 Keypad inside the System Overlay (No soft keyboard needed!)
            val keypadContainer = LinearLayout(appCtx).apply {
                orientation = LinearLayout.VERTICAL
                setPadding(0, dp(8), 0, dp(4))
            }

            val keypadRows = listOf(
                listOf("1", "2", "3"),
                listOf("4", "5", "6"),
                listOf("7", "8", "9"),
                listOf("CLR", "0", "DEL")
            )

            for (rowKeys in keypadRows) {
                val rowLayout = LinearLayout(appCtx).apply {
                    orientation = LinearLayout.HORIZONTAL
                    setPadding(0, dp(3), 0, dp(3))
                }
                for (keyLabel in rowKeys) {
                    val keyBtn = TextView(appCtx).apply {
                        text = keyLabel
                        textSize = 14f
                        gravity = Gravity.CENTER
                        setTextColor(android.graphics.Color.WHITE)
                        paint.isFakeBoldText = true
                        setPadding(0, dp(9), 0, dp(9))
                        background = GradientDrawable().apply {
                            shape = GradientDrawable.RECTANGLE
                            cornerRadius = dp(10).toFloat()
                            val bgHex = when (keyLabel) {
                                "CLR" -> "#7F1D1D"
                                "DEL" -> "#334155"
                                else -> "#1E293B"
                            }
                            setColor(android.graphics.Color.parseColor(bgHex))
                            setStroke(dp(1), android.graphics.Color.parseColor("#475569"))
                        }
                        setOnClickListener {
                            val current = pinInput.text?.toString().orEmpty()
                            when (keyLabel) {
                                "CLR" -> pinInput.setText("")
                                "DEL" -> if (current.isNotEmpty()) {
                                    pinInput.setText(current.dropLast(1))
                                    pinInput.setSelection(pinInput.text?.length ?: 0)
                                }
                                else -> if (current.length < 6) {
                                    val next = current + keyLabel
                                    pinInput.setText(next)
                                    pinInput.setSelection(next.length)
                                }
                            }
                        }
                    }
                    rowLayout.addView(
                        keyBtn,
                        LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f).apply {
                            marginStart = dp(3)
                            marginEnd = dp(3)
                        }
                    )
                }
                keypadContainer.addView(
                    rowLayout,
                    LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                    )
                )
            }

            val btnRow = LinearLayout(appCtx).apply {
                orientation = LinearLayout.HORIZONTAL
                setPadding(0, dp(8), 0, 0)
            }

            val cancelBtn = AndroidButton(appCtx).apply {
                text = "Tutup"
                textSize = 12f
                isAllCaps = false
                setTextColor(android.graphics.Color.WHITE)
                background = GradientDrawable().apply {
                    shape = GradientDrawable.RECTANGLE
                    cornerRadius = dp(12).toFloat()
                    setColor(android.graphics.Color.parseColor("#334155"))
                }
                setOnClickListener {
                    pinInput.setText("")
                    pinCard.visibility = View.GONE
                    params.flags = params.flags or WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                    try {
                        wm.updateViewLayout(rootContainer, params)
                    } catch (_: Exception) {
                    }
                }
            }

            val unlockBtn = AndroidButton(appCtx).apply {
                text = "Masuk Arcanum"
                textSize = 12f
                isAllCaps = false
                setTextColor(android.graphics.Color.WHITE)
                background = GradientDrawable().apply {
                    shape = GradientDrawable.RECTANGLE
                    cornerRadius = dp(12).toFloat()
                    setColor(android.graphics.Color.parseColor("#58CC02"))
                }
                setOnClickListener {
                    executePinSubmission(pinInput.text?.toString().orEmpty())
                }
            }

            btnRow.addView(
                cancelBtn,
                LinearLayout.LayoutParams(0, dp(42), 0.9f).apply {
                    marginEnd = dp(6)
                }
            )
            btnRow.addView(
                unlockBtn,
                LinearLayout.LayoutParams(0, dp(42), 1.3f)
            )

            pinCard.addView(headerRow)
            pinCard.addView(subTv)
            pinCard.addView(
                pinSlotIndicatorTv,
                LinearLayout.LayoutParams(dp(256), LinearLayout.LayoutParams.WRAP_CONTENT).apply {
                    bottomMargin = dp(6)
                }
            )
            pinCard.addView(
                pinInput,
                LinearLayout.LayoutParams(dp(256), LinearLayout.LayoutParams.WRAP_CONTENT)
            )
            pinCard.addView(
                keypadContainer,
                LinearLayout.LayoutParams(dp(256), LinearLayout.LayoutParams.WRAP_CONTENT)
            )
            pinCard.addView(
                btnRow,
                LinearLayout.LayoutParams(dp(256), LinearLayout.LayoutParams.WRAP_CONTENT)
            )

            // Drag + Click handling on the floating mascot bubble
            var initialX = 0
            var initialY = 0
            var touchX = 0f
            var touchY = 0f
            var moved = false

            mascotBubble.setOnTouchListener { _, event ->
                when (event.actionMasked) {
                    MotionEvent.ACTION_DOWN -> {
                        initialX = params.x
                        initialY = params.y
                        touchX = event.rawX
                        touchY = event.rawY
                        moved = false
                        true
                    }
                    MotionEvent.ACTION_MOVE -> {
                        val dx = (touchX - event.rawX).roundToInt()
                        val dy = (touchY - event.rawY).roundToInt()
                        if (kotlin.math.abs(dx) > dp(6) || kotlin.math.abs(dy) > dp(6)) {
                            moved = true
                            params.x = (initialX + dx).coerceAtLeast(0)
                            params.y = (initialY + dy).coerceAtLeast(0)
                            try {
                                wm.updateViewLayout(rootContainer, params)
                            } catch (_: Exception) {
                            }
                        }
                        true
                    }
                    MotionEvent.ACTION_UP -> {
                        if (!moved) {
                            val willShow = pinCard.visibility != View.VISIBLE
                            pinCard.visibility = if (willShow) View.VISIBLE else View.GONE
                            if (willShow) {
                                params.flags = params.flags and WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE.inv()
                            } else {
                                params.flags = params.flags or WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                            }
                            try {
                                wm.updateViewLayout(rootContainer, params)
                            } catch (_: Exception) {
                            }
                        }
                        true
                    }
                    else -> false
                }
            }

            rootContainer.addView(
                pinCard,
                LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                ).apply {
                    bottomMargin = dp(8)
                }
            )
            rootContainer.addView(
                mascotBubble,
                LinearLayout.LayoutParams(dp(84), dp(86))
            )

            wm.addView(rootContainer, params)
            overlayRootView = rootContainer
        } catch (_: Exception) {
        }
    }
}

/**
 * Secara kilat membuka aplikasi eksternal yang dipilih dan menyalakan System Floating Mascot Overlay di atasnya.
 * PENTING: Jika izin `Settings.canDrawOverlays` belum aktif, fungsi ini TIDAK akan membuka aplikasi eksternal
 * tanpa overlay (mengembalikan `false` agar aplikasi memaksa pengguna mengaktifkan izin overlay terlebih dahulu).
 */
fun launchExternalAppFlashWithMascotOverlay(
    context: Context,
    targetAppId: String,
    targetAppLabel: String,
    securityConfig: SecurityConfig? = null,
    onSubmitOverlayPin: (String) -> Boolean
): Boolean {
    if (!targetAppId.startsWith("PKG:", ignoreCase = true)) return false
    if (!SystemMascotOverlayManager.canDrawSystemOverlays(context)) {
        return false
    }
    val pkgName = targetAppId.removePrefix("PKG:").trim()
    if (pkgName.isBlank()) return false
    return try {
        val launchIntent = context.packageManager.getLaunchIntentForPackage(pkgName)?.apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED)
        }
        if (launchIntent != null) {
            SystemMascotOverlayManager.showSystemMascotOverlay(
                context = context,
                targetAppLabel = targetAppLabel,
                securityConfig = securityConfig,
                onVerifyPin = onSubmitOverlayPin
            )
            context.startActivity(launchIntent)
            true
        } else {
            false
        }
    } catch (_: Exception) {
        false
    }
}

/**
 * Tab Baru di Ruang Pengaturan Terisolasi:
 * "Kamuflase" — Ganti Ikon & Nama Custom, Pilih Aplikasi Tujuan Redirect, dan Aktifkan Overlay PIN Maskot.
 */
@Composable
fun AppCamouflageAndRedirectScreen(
    securityConfig: SecurityConfig,
    onUpdateCamouflageConfig: (
        isRedirectArmed: Boolean?,
        identityMode: String?,
        presetId: String?,
        customAppName: String?,
        customIconId: String?,
        customColorHex: String?,
        targetAppId: String?,
        targetAppLabel: String?,
        bannerText: String?
    ) -> Unit,
    onApplyAndRestartToCamouflage: (
        identityMode: String,
        presetId: String,
        customAppName: String,
        customIconId: String,
        customColorHex: String,
        targetAppId: String,
        targetAppLabel: String
    ) -> Unit
) {
    val context = LocalContext.current

    var isRedirectArmed by remember(securityConfig.isCamouflageRedirectArmed) {
        mutableStateOf(securityConfig.isCamouflageRedirectArmed)
    }
    var identityMode by remember(securityConfig.camouflageIdentityMode) {
        mutableStateOf(securityConfig.camouflageIdentityMode.ifBlank { "PRESET" })
    }
    var selectedPresetId by remember(securityConfig.camouflagePresetId) {
        mutableStateOf(securityConfig.camouflagePresetId.ifBlank { "CALCULATOR" })
    }
    var customAppName by remember(securityConfig.camouflageCustomAppName) {
        mutableStateOf(securityConfig.camouflageCustomAppName.ifBlank { "Kalkulator Pro" })
    }
    var customIconId by remember(securityConfig.camouflageCustomIconId) {
        mutableStateOf(securityConfig.camouflageCustomIconId.ifBlank { "CALC" })
    }
    var customColorHex by remember(securityConfig.camouflageCustomColorHex) {
        mutableStateOf(securityConfig.camouflageCustomColorHex.ifBlank { "#0EA5E9" })
    }
    var selectedTargetAppId by remember(securityConfig.camouflageTargetAppId) {
        mutableStateOf(securityConfig.camouflageTargetAppId.ifBlank { "BUILTIN_CALCULATOR" })
    }
    var selectedTargetAppLabel by remember(securityConfig.camouflageTargetAppLabel) {
        mutableStateOf(securityConfig.camouflageTargetAppLabel.ifBlank { "Kalkulator Ilmiah Pro" })
    }

    var targetAppFilterTab by remember { mutableIntStateOf(0) } // 0 = Semua (Bawaan + HP), 1 = Bawaan, 2 = Terinstal di HP
    var installedAppsList by remember { mutableStateOf<List<CamouflageTargetAppItem>>(emptyList()) }
    var searchAppQuery by remember { mutableStateOf("") }

    var hasOverlayPerm by remember {
        mutableStateOf(SystemMascotOverlayManager.canDrawSystemOverlays(context))
    }
    var showForceOverlayPermissionDialog by remember { mutableStateOf(false) }
    var pendingFlashLaunchAfterPermission by remember { mutableStateOf(false) }

    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner, context) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                hasOverlayPerm = SystemMascotOverlayManager.canDrawSystemOverlays(context)
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    LaunchedEffect(securityConfig) {
        SystemMascotOverlayManager.updateCachedSecurityConfig(securityConfig)
    }

    // Live polling watcher so the exact instant the user turns on "Display over other apps" in Android Settings,
    // we detect it and auto-launch the external app with the floating mascot overlay if pending!
    LaunchedEffect(context) {
        while (true) {
            val currentPerm = SystemMascotOverlayManager.canDrawSystemOverlays(context)
            if (currentPerm != hasOverlayPerm) {
                hasOverlayPerm = currentPerm
            }
            delay(350L)
        }
    }

    LaunchedEffect(hasOverlayPerm, pendingFlashLaunchAfterPermission) {
        if (hasOverlayPerm && pendingFlashLaunchAfterPermission) {
            pendingFlashLaunchAfterPermission = false
            showForceOverlayPermissionDialog = false
            syncSystemDisguiseIdentity(
                context = context,
                identityMode = identityMode,
                presetId = selectedPresetId,
                customAppName = customAppName,
                customIconId = customIconId,
                customColorHex = customColorHex,
                targetAppLabel = selectedTargetAppLabel,
                targetAppId = selectedTargetAppId,
                applyLauncherAlias = true
            )
            if (selectedTargetAppId.startsWith("PKG:", ignoreCase = true)) {
                SystemMascotOverlayManager.showSystemMascotOverlay(
                    context = context,
                    targetAppLabel = selectedTargetAppLabel,
                    securityConfig = securityConfig
                )
            }
            onApplyAndRestartToCamouflage(
                identityMode,
                selectedPresetId,
                customAppName,
                customIconId,
                customColorHex,
                selectedTargetAppId,
                selectedTargetAppLabel
            )
        }
    }

    if (showForceOverlayPermissionDialog) {
        ForceSystemOverlayPermissionDialog(
            targetAppLabel = selectedTargetAppLabel,
            hasOverlayPermission = hasOverlayPerm,
            isPendingAutoLaunch = pendingFlashLaunchAfterPermission,
            onOpenOverlaySettings = {
                openSystemOverlayPermissionSettings(context)
            },
            onOpenPopupDetailsSettings = {
                openAppDetailsPopupPermissionSettings(context)
            },
            onDismiss = {
                pendingFlashLaunchAfterPermission = false
                showForceOverlayPermissionDialog = false
            }
        )
    }

    // Load installed launcher apps from device PackageManager
    LaunchedEffect(context) {
        withContext(Dispatchers.IO) {
            try {
                val pm = context.packageManager
                val mainIntent = Intent(Intent.ACTION_MAIN, null).apply {
                    addCategory(Intent.CATEGORY_LAUNCHER)
                }
                val resolved = pm.queryIntentActivities(mainIntent, 0)
                val myPkg = context.packageName
                val parsed = resolved
                    .mapNotNull { info ->
                        val pkgName = info.activityInfo?.packageName ?: return@mapNotNull null
                        if (pkgName == myPkg) return@mapNotNull null
                        val label = info.loadLabel(pm)?.toString()?.trim().takeUnless { it.isNullOrBlank() } ?: pkgName
                        val iconBmp = try {
                            drawableToImageBitmap(info.loadIcon(pm), 84)
                        } catch (_: Exception) {
                            null
                        }
                        CamouflageTargetAppItem(
                            id = "PKG:$pkgName",
                            label = label,
                            subtitle = "Aplikasi HP Terinstal • $pkgName",
                            isBuiltIn = false,
                            installedPackageName = pkgName,
                            installedIconBitmap = iconBmp
                        )
                    }
                    .distinctBy { it.id }
                    .sortedBy { it.label.lowercase() }
                installedAppsList = parsed
            } catch (_: Exception) {
            }
        }
    }

    val allTargetApps = remember(installedAppsList) {
        BUILT_IN_TARGET_APPS + installedAppsList
    }

    val filteredTargetApps = remember(allTargetApps, targetAppFilterTab, searchAppQuery) {
        allTargetApps.filter { item ->
            val tabMatch = when (targetAppFilterTab) {
                1 -> item.isBuiltIn
                2 -> !item.isBuiltIn
                else -> true
            }
            val queryMatch = searchAppQuery.isBlank() ||
                item.label.contains(searchAppQuery, ignoreCase = true) ||
                item.subtitle.contains(searchAppQuery, ignoreCase = true)
            tabMatch && queryMatch
        }
    }

    val effectiveDisguiseTitle = when (identityMode) {
        "CUSTOM" -> customAppName.ifBlank { "Kalkulator Pro" }
        "MATCH_TARGET_APP" -> selectedTargetAppLabel.ifBlank { "Kalkulator Ilmiah Pro" }
        else -> DisguisePresetOption.fromId(selectedPresetId).appTitle
    }

    val effectiveDisguiseColor = when (identityMode) {
        "CUSTOM" -> parseColorSafe(customColorHex)
        "MATCH_TARGET_APP" -> DuoMacawBlue
        else -> parseColorSafe(DisguisePresetOption.fromId(selectedPresetId).colorHex)
    }

    val effectiveDisguiseIcon = when (identityMode) {
        "CUSTOM" -> CustomDisguiseIconOption.fromId(customIconId).vector
        "MATCH_TARGET_APP" -> Icons.Default.Apps
        else -> CustomDisguiseIconOption.fromId(DisguisePresetOption.fromId(selectedPresetId).iconId).vector
    }

    val colorSwatches = remember {
        listOf(
            "#0EA5E9" to "Biru Langit",
            "#58CC02" to "Hijau Zamrud",
            "#F59E0B" to "Kuning Amber",
            "#EF4444" to "Merah Karmin",
            "#8B5CF6" to "Ungu Kosmik",
            "#EC4899" to "Pink Neon",
            "#14B8A6" to "Toska Siber",
            "#334155" to "Abu Gelap"
        )
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .testTag("app_camouflage_and_redirect_screen"),
        contentPadding = PaddingValues(start = 18.dp, end = 18.dp, top = 12.dp, bottom = 120.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // 1. Header Card + Live Preview Identity + Redirect Master Switch
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .duolingoCard(
                        backgroundColor = DuoPureWhite,
                        borderColor = DuoMacawBlue,
                        lipColor = DuoMacawBlueLip,
                        cornerRadius = 24.dp,
                        borderWidth = 2.dp,
                        lipHeight = 5.dp
                    )
                    .padding(bottom = 5.dp)
                    .padding(16.dp)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Surface(
                                color = if (isRedirectArmed) DuoFeatherGreen else DuoFoxOrange,
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text(
                                    text = if (isRedirectArmed) {
                                        "MODE KAMUFLASE & REDIRECT AKTIF"
                                    } else {
                                        "MODE KAMUFLASE SIAP DIAKTIFKAN"
                                    },
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = DuoPureWhite,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                )
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "Kamuflase Ikon, Nama & Auto-Buka App",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.ExtraBold,
                                color = VaultObsidian
                            )
                            Text(
                                text = "Ubah ikon & nama aplikasi, alihkan otomatis ke aplikasi lain saat dibuka, dan masuk ke Arcanum lewat PIN Overlay Maskot.",
                                style = MaterialTheme.typography.bodySmall,
                                color = CosmicTextSubtle
                            )
                        }
                    }

                    // Live Preview Banner: How the app looks & where it redirects
                    Surface(
                        color = DuoMacawBlueSoft,
                        shape = RoundedCornerShape(18.dp),
                        border = androidx.compose.foundation.BorderStroke(1.5.dp, DuoMacawBlue.copy(alpha = 0.45f)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            // Disguised Icon Preview
                            val matchedTargetItem = allTargetApps.find { it.id == selectedTargetAppId }
                            Box(
                                modifier = Modifier
                                    .size(56.dp)
                                    .clip(RoundedCornerShape(16.dp))
                                    .background(effectiveDisguiseColor)
                                    .border(2.dp, DuoPureWhite, RoundedCornerShape(16.dp)),
                                contentAlignment = Alignment.Center
                            ) {
                                if (identityMode == "MATCH_TARGET_APP" && matchedTargetItem?.installedIconBitmap != null) {
                                    Image(
                                        bitmap = matchedTargetItem.installedIconBitmap,
                                        contentDescription = effectiveDisguiseTitle,
                                        modifier = Modifier.size(44.dp)
                                    )
                                } else {
                                    Icon(
                                        imageVector = effectiveDisguiseIcon,
                                        contentDescription = effectiveDisguiseTitle,
                                        tint = DuoPureWhite,
                                        modifier = Modifier.size(30.dp)
                                    )
                                }
                            }

                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "Nama Tampil: $effectiveDisguiseTitle",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = VaultObsidian
                                )
                                Text(
                                    text = "Saat Dibuka -> Otomatis Buka: $selectedTargetAppLabel",
                                    style = MaterialTheme.typography.bodySmall,
                                    fontWeight = FontWeight.Bold,
                                    color = DuoMacawBlueLip
                                )
                                Text(
                                    text = "Ketuk Maskot Melayang -> Input PIN -> Masuk Arcanum",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = CosmicTextSubtle
                                )
                            }

                            Switch(
                                checked = isRedirectArmed,
                                onCheckedChange = { armed ->
                                    if (armed &&
                                        selectedTargetAppId.startsWith("PKG:", ignoreCase = true) &&
                                        !SystemMascotOverlayManager.canDrawSystemOverlays(context)
                                    ) {
                                        showForceOverlayPermissionDialog = true
                                    }
                                    isRedirectArmed = armed
                                    syncSystemDisguiseIdentity(
                                        context = context,
                                        identityMode = identityMode,
                                        presetId = selectedPresetId,
                                        customAppName = customAppName,
                                        customIconId = customIconId,
                                        customColorHex = customColorHex,
                                        targetAppLabel = selectedTargetAppLabel
                                    )
                                    onUpdateCamouflageConfig(
                                        armed,
                                        identityMode,
                                        selectedPresetId,
                                        customAppName,
                                        customIconId,
                                        customColorHex,
                                        selectedTargetAppId,
                                        selectedTargetAppLabel,
                                        if (armed) {
                                            "Pengalihan otomatis ke '$selectedTargetAppLabel' diaktifkan!"
                                        } else {
                                            "Pengalihan otomatis dinonaktifkan (kembali ke gerbang standar)."
                                        }
                                    )
                                },
                                colors = SwitchDefaults.colors(
                                    checkedThumbColor = DuoPureWhite,
                                    checkedTrackColor = DuoFeatherGreen
                                ),
                                modifier = Modifier.testTag("camouflage_redirect_master_switch")
                            )
                        }
                    }
                }
            }
        }

        // 2. Section 1: Pilih Mode Ikon & Nama Penyamaran (Preset / Custom / Ikuti Aplikasi Tujuan)
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .duolingoCard(
                        backgroundColor = DuoPureWhite,
                        borderColor = DuoSwanBorder,
                        lipColor = DuoHareLip,
                        cornerRadius = 22.dp,
                        borderWidth = 2.dp,
                        lipHeight = 4.dp
                    )
                    .padding(bottom = 4.dp)
                    .padding(16.dp)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Palette,
                            contentDescription = null,
                            tint = DuoBeetlePurple,
                            modifier = Modifier.size(22.dp)
                        )
                        Column {
                            Text(
                                text = "1. Ikon & Nama Penyamaran Aplikasi",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.ExtraBold,
                                color = VaultObsidian
                            )
                            Text(
                                text = "Pilih Preset, ketik Nama Custom bebas, atau samakan dengan Aplikasi Tujuan.",
                                style = MaterialTheme.typography.bodySmall,
                                color = CosmicTextSubtle
                            )
                        }
                    }

                    // 3 Mode Selector Pills
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        listOf(
                            "PRESET" to "Preset",
                            "CUSTOM" to "Custom Bebas",
                            "MATCH_TARGET_APP" to "Ikuti App Tujuan"
                        ).forEach { (modeKey, modeLabel) ->
                            val selected = identityMode.equals(modeKey, ignoreCase = true)
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .height(42.dp)
                                    .then(
                                        if (selected) {
                                            Modifier.duolingoButton(
                                                faceColor = DuoBeetlePurple,
                                                lipColor = DuoBeetlePurpleLip,
                                                cornerRadius = 12.dp,
                                                lipHeight = 3.dp
                                            )
                                        } else {
                                            Modifier.duolingoCard(
                                                backgroundColor = DuoPureWhite,
                                                borderColor = DuoSwanBorder,
                                                lipColor = DuoHareLip,
                                                cornerRadius = 12.dp,
                                                borderWidth = 1.5.dp,
                                                lipHeight = 3.dp
                                            )
                                        }
                                    )
                                    .clickable {
                                        identityMode = modeKey
                                        syncSystemDisguiseIdentity(
                                            context = context,
                                            identityMode = identityMode,
                                            presetId = selectedPresetId,
                                            customAppName = customAppName,
                                            customIconId = customIconId,
                                            customColorHex = customColorHex,
                                            targetAppLabel = selectedTargetAppLabel
                                        )
                                        onUpdateCamouflageConfig(
                                            null, modeKey, null, null, null, null, null, null,
                                            "Mode identitas kamuflase diatur ke: $modeLabel"
                                        )
                                    }
                                    .padding(bottom = 3.dp)
                                    .testTag("camouflage_identity_mode_$modeKey"),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = modeLabel,
                                    style = MaterialTheme.typography.labelMedium,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = if (selected) DuoPureWhite else VaultObsidian,
                                    maxLines = 1
                                )
                            }
                        }
                    }

                    when (identityMode.uppercase()) {
                        "PRESET" -> {
                            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                DisguisePresetOption.entries.forEach { preset ->
                                    val isSelected = selectedPresetId.equals(preset.id, ignoreCase = true)
                                    val presetColor = parseColorSafe(preset.colorHex)
                                    val presetIcon = CustomDisguiseIconOption.fromId(preset.iconId).vector
                                    Surface(
                                        color = if (isSelected) DuoBeetlePurpleSoft else DuoPureWhite,
                                        shape = RoundedCornerShape(14.dp),
                                        border = androidx.compose.foundation.BorderStroke(
                                            width = if (isSelected) 2.dp else 1.dp,
                                            color = if (isSelected) DuoBeetlePurple else DuoSwanBorder
                                        ),
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clickable {
                                                selectedPresetId = preset.id
                                                syncSystemDisguiseIdentity(
                                                    context = context,
                                                    identityMode = "PRESET",
                                                    presetId = preset.id,
                                                    customAppName = customAppName,
                                                    customIconId = customIconId,
                                                    customColorHex = customColorHex,
                                                    targetAppLabel = selectedTargetAppLabel
                                                )
                                                onUpdateCamouflageConfig(
                                                    null, "PRESET", preset.id, null, null, null, null, null,
                                                    "Ikon & Nama aplikasi diubah ke preset: ${preset.appTitle}"
                                                )
                                            }
                                            .testTag("disguise_preset_item_${preset.id}")
                                    ) {
                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(12.dp),
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                                        ) {
                                            Box(
                                                modifier = Modifier
                                                    .size(42.dp)
                                                    .clip(RoundedCornerShape(12.dp))
                                                    .background(presetColor),
                                                contentAlignment = Alignment.Center
                                            ) {
                                                Icon(
                                                    imageVector = presetIcon,
                                                    contentDescription = preset.appTitle,
                                                    tint = DuoPureWhite,
                                                    modifier = Modifier.size(22.dp)
                                                )
                                            }
                                            Column(modifier = Modifier.weight(1f)) {
                                                Text(
                                                    text = preset.appTitle,
                                                    style = MaterialTheme.typography.titleSmall,
                                                    fontWeight = FontWeight.ExtraBold,
                                                    color = VaultObsidian
                                                )
                                                Text(
                                                    text = preset.subtitle,
                                                    style = MaterialTheme.typography.bodySmall,
                                                    color = CosmicTextSubtle
                                                )
                                            }
                                            if (isSelected) {
                                                Icon(
                                                    imageVector = Icons.Default.CheckCircle,
                                                    contentDescription = "Aktif",
                                                    tint = DuoBeetlePurple
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }

                        "CUSTOM" -> {
                            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                                OutlinedTextField(
                                    value = customAppName,
                                    onValueChange = {
                                        customAppName = it.take(28)
                                    },
                                    label = { Text("Nama Aplikasi Custom Bebas") },
                                    placeholder = { Text("Contoh: Kamus Inggris / Catatan Kuliah") },
                                    singleLine = true,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .testTag("camouflage_custom_name_input"),
                                    shape = RoundedCornerShape(14.dp)
                                )

                                Text(
                                    text = "Pilih Simbol Ikon Custom:",
                                    style = MaterialTheme.typography.labelMedium,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = VaultObsidian
                                )

                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .horizontalScroll(rememberScrollState()),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    CustomDisguiseIconOption.entries.forEach { iconOpt ->
                                        val isSelected = customIconId.equals(iconOpt.id, ignoreCase = true)
                                        Surface(
                                            color = if (isSelected) parseColorSafe(customColorHex) else DuoPureWhite,
                                            shape = RoundedCornerShape(12.dp),
                                            border = androidx.compose.foundation.BorderStroke(
                                                width = if (isSelected) 2.dp else 1.dp,
                                                color = if (isSelected) VaultObsidian else DuoSwanBorder
                                            ),
                                            modifier = Modifier
                                                .clickable {
                                                    customIconId = iconOpt.id
                                                }
                                                .testTag("custom_disguise_icon_${iconOpt.id}")
                                        ) {
                                            Row(
                                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp),
                                                verticalAlignment = Alignment.CenterVertically,
                                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                                            ) {
                                                Icon(
                                                    imageVector = iconOpt.vector,
                                                    contentDescription = iconOpt.label,
                                                    tint = if (isSelected) DuoPureWhite else VaultObsidian,
                                                    modifier = Modifier.size(18.dp)
                                                )
                                                Text(
                                                    text = iconOpt.label,
                                                    style = MaterialTheme.typography.labelMedium,
                                                    fontWeight = FontWeight.Bold,
                                                    color = if (isSelected) DuoPureWhite else VaultObsidian
                                                )
                                            }
                                        }
                                    }
                                }

                                Text(
                                    text = "Pilih Warna Ikon Custom:",
                                    style = MaterialTheme.typography.labelMedium,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = VaultObsidian
                                )

                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .horizontalScroll(rememberScrollState()),
                                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                                ) {
                                    colorSwatches.forEach { (hex, label) ->
                                        val isSelected = customColorHex.equals(hex, ignoreCase = true)
                                        Box(
                                            modifier = Modifier
                                                .size(38.dp)
                                                .clip(CircleShape)
                                                .background(parseColorSafe(hex))
                                                .border(
                                                    width = if (isSelected) 3.dp else 1.dp,
                                                    color = if (isSelected) VaultObsidian else DuoSwanBorder,
                                                    shape = CircleShape
                                                )
                                                .clickable {
                                                    customColorHex = hex
                                                },
                                            contentAlignment = Alignment.Center
                                        ) {
                                            if (isSelected) {
                                                Icon(
                                                    imageVector = Icons.Default.Check,
                                                    contentDescription = label,
                                                    tint = DuoPureWhite,
                                                    modifier = Modifier.size(18.dp)
                                                )
                                            }
                                        }
                                    }
                                }

                                Button(
                                    onClick = {
                                        syncSystemDisguiseIdentity(
                                            context = context,
                                            identityMode = "CUSTOM",
                                            presetId = selectedPresetId,
                                            customAppName = customAppName,
                                            customIconId = customIconId,
                                            customColorHex = customColorHex,
                                            targetAppLabel = selectedTargetAppLabel,
                                            targetAppId = selectedTargetAppId,
                                            applyLauncherAlias = false
                                        )
                                        onUpdateCamouflageConfig(
                                            null, "CUSTOM", null, customAppName, customIconId, customColorHex, null, null,
                                            "Nama & Ikon Custom '${customAppName.ifBlank { "Kalkulator Pro" }}' disimpan!"
                                        )
                                    },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(44.dp)
                                        .testTag("save_custom_disguise_identity_button"),
                                    shape = RoundedCornerShape(12.dp),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = DuoBeetlePurple,
                                        contentColor = DuoPureWhite
                                    )
                                ) {
                                    Text("Simpan Nama & Ikon Custom", fontWeight = FontWeight.ExtraBold)
                                }
                            }
                        }

                        else -> {
                            // MATCH_TARGET_APP
                            Surface(
                                color = DuoFeatherGreenSoft,
                                shape = RoundedCornerShape(14.dp),
                                border = androidx.compose.foundation.BorderStroke(1.5.dp, DuoFeatherGreen),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(
                                    modifier = Modifier.padding(12.dp),
                                    verticalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Text(
                                        text = "Ikon & Nama Arcanum Otomatis Mengikuti: '$selectedTargetAppLabel'",
                                        style = MaterialTheme.typography.titleSmall,
                                        fontWeight = FontWeight.ExtraBold,
                                        color = VaultObsidian
                                    )
                                    Text(
                                        text = "Tanpa membuat shortcut terpisah: ikon & nama aplikasi Arcanum di Launcher akan otomatis berubah menyesuaikan aplikasi tujuan '$selectedTargetAppLabel' yang Anda pilih di bawah.",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = CosmicTextSubtle
                                    )
                                    Button(
                                        onClick = {
                                            syncSystemDisguiseIdentity(
                                                context = context,
                                                identityMode = "MATCH_TARGET_APP",
                                                presetId = selectedPresetId,
                                                customAppName = selectedTargetAppLabel,
                                                customIconId = customIconId,
                                                customColorHex = customColorHex,
                                                targetAppLabel = selectedTargetAppLabel,
                                                targetAppId = selectedTargetAppId,
                                                applyLauncherAlias = false
                                            )
                                            onUpdateCamouflageConfig(
                                                null, "MATCH_TARGET_APP", null, selectedTargetAppLabel, null, null, null, null,
                                                "Identitas aplikasi Arcanum disamakan dengan: $selectedTargetAppLabel"
                                            )
                                        },
                                        modifier = Modifier.fillMaxWidth(),
                                        shape = RoundedCornerShape(12.dp),
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = DuoFeatherGreen,
                                            contentColor = DuoPureWhite
                                        )
                                    ) {
                                        Icon(Icons.Default.CheckCircle, contentDescription = null, modifier = Modifier.size(16.dp))
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text("Samakan Ikon & Nama dengan '$selectedTargetAppLabel'", fontWeight = FontWeight.ExtraBold)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // 3. Section 2: Pilih Aplikasi Tujuan Saat Dibuka (Gabungan Aplikasi Bawaan + Aplikasi HP Terinstal)
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .duolingoCard(
                        backgroundColor = DuoPureWhite,
                        borderColor = DuoSwanBorder,
                        lipColor = DuoHareLip,
                        cornerRadius = 22.dp,
                        borderWidth = 2.dp,
                        lipHeight = 4.dp
                    )
                    .padding(bottom = 4.dp)
                    .padding(16.dp)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Apps,
                            contentDescription = null,
                            tint = DuoMacawBlue,
                            modifier = Modifier.size(22.dp)
                        )
                        Column {
                            Text(
                                text = "2. Pilih Aplikasi Tujuan Saat Dibuka",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.ExtraBold,
                                color = VaultObsidian
                            )
                            Text(
                                text = "Saat aplikasi dibuka, otomatis langsung membuka aplikasi yang dipilih di bawah ini dengan Overlay Maskot di atasnya.",
                                style = MaterialTheme.typography.bodySmall,
                                color = CosmicTextSubtle
                            )
                        }
                    }

                    // Filter Chips: Semua / Aplikasi Bawaan / Aplikasi HP Terinstal
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        listOf(
                            0 to "Semua (${allTargetApps.size})",
                            1 to "Bawaan (${BUILT_IN_TARGET_APPS.size})",
                            2 to "Aplikasi HP (${installedAppsList.size})"
                        ).forEach { (tabIdx, label) ->
                            val selected = targetAppFilterTab == tabIdx
                            Surface(
                                color = if (selected) DuoMacawBlue else DuoPureWhite,
                                shape = RoundedCornerShape(10.dp),
                                border = androidx.compose.foundation.BorderStroke(
                                    1.dp,
                                    if (selected) DuoMacawBlueLip else DuoSwanBorder
                                ),
                                modifier = Modifier
                                    .weight(1f)
                                    .clickable { targetAppFilterTab = tabIdx }
                            ) {
                                Text(
                                    text = label,
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = if (selected) DuoPureWhite else VaultObsidian,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.padding(vertical = 8.dp, horizontal = 4.dp),
                                    maxLines = 1
                                )
                            }
                        }
                    }

                    OutlinedTextField(
                        value = searchAppQuery,
                        onValueChange = { searchAppQuery = it },
                        placeholder = { Text("Cari aplikasi tujuan (Kalkulator, Catatan, Chrome, dll)...") },
                        leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("camouflage_search_target_app_input"),
                        shape = RoundedCornerShape(12.dp)
                    )

                    Column(
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        filteredTargetApps.take(18).forEach { appItem ->
                            val isSelected = selectedTargetAppId == appItem.id
                            Surface(
                                color = if (isSelected) DuoFeatherGreenSoft else DuoPureWhite,
                                shape = RoundedCornerShape(14.dp),
                                border = androidx.compose.foundation.BorderStroke(
                                    width = if (isSelected) 2.dp else 1.dp,
                                    color = if (isSelected) DuoFeatherGreen else DuoSwanBorder
                                ),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        selectedTargetAppId = appItem.id
                                        selectedTargetAppLabel = appItem.label
                                        // Otomatis sesuaikan nama & ikon kamuflase mengikuti aplikasi yang dipilih
                                        identityMode = "MATCH_TARGET_APP"
                                        customAppName = appItem.label
                                        if (!appItem.isBuiltIn && !SystemMascotOverlayManager.canDrawSystemOverlays(context)) {
                                            showForceOverlayPermissionDialog = true
                                        }
                                        syncSystemDisguiseIdentity(
                                            context = context,
                                            identityMode = "MATCH_TARGET_APP",
                                            presetId = selectedPresetId,
                                            customAppName = appItem.label,
                                            customIconId = customIconId,
                                            customColorHex = customColorHex,
                                            targetAppLabel = appItem.label,
                                            targetAppId = appItem.id,
                                            applyLauncherAlias = false
                                        )
                                        onUpdateCamouflageConfig(
                                            null,
                                            "MATCH_TARGET_APP",
                                            null,
                                            appItem.label,
                                            null,
                                            null,
                                            appItem.id,
                                            appItem.label,
                                            "Aplikasi kamuflase dipilih: ${appItem.label} (Ikon & Auto-Buka Kilat disiapkan)"
                                        )
                                    }
                                    .testTag("target_app_option_${appItem.id}")
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(12.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                                ) {
                                    if (appItem.installedIconBitmap != null) {
                                        Image(
                                            bitmap = appItem.installedIconBitmap,
                                            contentDescription = appItem.label,
                                            modifier = Modifier
                                                .size(42.dp)
                                                .clip(RoundedCornerShape(10.dp))
                                        )
                                    } else {
                                        val iconVec = CustomDisguiseIconOption.fromId(appItem.builtInIconId).vector
                                        Box(
                                            modifier = Modifier
                                                .size(42.dp)
                                                .clip(RoundedCornerShape(12.dp))
                                                .background(parseColorSafe(appItem.builtInColorHex)),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Icon(
                                                imageVector = iconVec,
                                                contentDescription = appItem.label,
                                                tint = DuoPureWhite,
                                                modifier = Modifier.size(22.dp)
                                            )
                                        }
                                    }

                                    Column(modifier = Modifier.weight(1f)) {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                                        ) {
                                            Text(
                                                text = appItem.label,
                                                style = MaterialTheme.typography.titleSmall,
                                                fontWeight = FontWeight.ExtraBold,
                                                color = VaultObsidian,
                                                maxLines = 1,
                                                overflow = TextOverflow.Ellipsis
                                            )
                                            Surface(
                                                color = if (appItem.isBuiltIn) DuoMacawBlueSoft else DuoBeeYellowSoft,
                                                shape = RoundedCornerShape(6.dp)
                                            ) {
                                                Text(
                                                    text = if (appItem.isBuiltIn) "BAWAAN" else "TERINSTAL",
                                                    style = MaterialTheme.typography.labelSmall,
                                                    fontSize = 9.sp,
                                                    fontWeight = FontWeight.ExtraBold,
                                                    color = if (appItem.isBuiltIn) DuoMacawBlueLip else DuoFoxOrangeLip,
                                                    modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp)
                                                )
                                            }
                                        }
                                        Text(
                                            text = appItem.subtitle,
                                            style = MaterialTheme.typography.bodySmall,
                                            color = CosmicTextSubtle,
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis
                                        )
                                    }

                                    if (isSelected) {
                                        Icon(
                                            imageVector = Icons.Default.CheckCircle,
                                            contentDescription = "Terpilih",
                                            tint = DuoFeatherGreenLip
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // 4. Section 3: Izin Overlay Sistem Wajib + Tombol "Simpan, Ubah Ikon & Buka Kilat Aplikasi Pilihan"
        item {
            val isExternalTarget = selectedTargetAppId.startsWith("PKG:", ignoreCase = true)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .duolingoCard(
                        backgroundColor = DuoFeatherGreenSoft,
                        borderColor = DuoFeatherGreen,
                        lipColor = DuoFeatherGreenLip,
                        cornerRadius = 22.dp,
                        borderWidth = 2.dp,
                        lipHeight = 5.dp
                    )
                    .padding(bottom = 5.dp)
                    .padding(16.dp)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(
                        text = "3. Aktifkan Buka Kilat & Overlay Maskot PIN",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.ExtraBold,
                        color = VaultObsidian
                    )
                    Text(
                        text = "• Ikon & nama Arcanum di Launcher berubah mengikuti '$selectedTargetAppLabel' (bukan bikin shortcut).\n" +
                            "• Saat dibuka dari Launcher, otomatis SECARA KILAT langsung membuka '$selectedTargetAppLabel'.\n" +
                            "• Di atas aplikasi tersebut muncul Overlay Maskot 3D Arcanum. Saat diklik, muncul Overlay Input PIN (lengkap dengan Keypad 0-9):\n" +
                            "  - Jika PIN SALAH: Overlay langsung dimatikan/hilang dan tetap di '$selectedTargetAppLabel'.\n" +
                            "  - Jika PIN BENAR: Otomatis langsung masuk ke dalam Arcanum!",
                        style = MaterialTheme.typography.bodySmall,
                        color = VaultObsidian
                    )

                    // Mandatory System Overlay & Pop-Up Permission Card for External Installed Apps
                    Surface(
                        color = if (hasOverlayPerm) DuoPureWhite else DuoCardinalRedSoft,
                        shape = RoundedCornerShape(14.dp),
                        border = androidx.compose.foundation.BorderStroke(
                            width = 2.dp,
                            color = if (hasOverlayPerm) DuoFeatherGreen else DuoCardinalRed
                        ),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = if (hasOverlayPerm) {
                                            "Izin Overlay Sistem ('Tampilkan di Atas Aplikasi Lain') AKTIF"
                                        } else {
                                            "WAJIB AKTIFKAN: Tampilkan di Atas Aplikasi Lain & Pop-Up"
                                        },
                                        style = MaterialTheme.typography.labelLarge,
                                        fontWeight = FontWeight.ExtraBold,
                                        color = if (hasOverlayPerm) DuoFeatherGreenLip else DuoCardinalRedLip
                                    )
                                    Text(
                                        text = if (hasOverlayPerm) {
                                            "Maskot 3D & Keypad PIN siap melayang di atas '$selectedTargetAppLabel' dan aplikasi lain."
                                        } else {
                                            "Tanpa izin ini, Android memblokir Maskot saat membuka aplikasi asli '$selectedTargetAppLabel'. Klik tombol di bawah untuk mengizinkan!"
                                        },
                                        style = MaterialTheme.typography.labelSmall,
                                        color = VaultObsidian
                                    )
                                }
                            }

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Button(
                                    onClick = {
                                        openSystemOverlayPermissionSettings(context)
                                    },
                                    modifier = Modifier.weight(1f),
                                    shape = RoundedCornerShape(10.dp),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = if (hasOverlayPerm) DuoMacawBlue else DuoCardinalRed,
                                        contentColor = DuoPureWhite
                                    )
                                ) {
                                    Text(
                                        text = if (hasOverlayPerm) "Pengaturan Overlay" else "1. Aktifkan Izin Overlay (Wajib)",
                                        style = MaterialTheme.typography.labelSmall,
                                        fontWeight = FontWeight.ExtraBold
                                    )
                                }

                                OutlinedButton(
                                    onClick = {
                                        openAppDetailsPopupPermissionSettings(context)
                                    },
                                    modifier = Modifier.weight(1f),
                                    shape = RoundedCornerShape(10.dp)
                                ) {
                                    Text(
                                        text = "2. Izin Pop-Up Latar",
                                        style = MaterialTheme.typography.labelSmall,
                                        fontWeight = FontWeight.ExtraBold,
                                        color = VaultObsidian
                                    )
                                }
                            }

                            // Direct Test Button: Spawn the Floating Mascot Overlay right now
                            OutlinedButton(
                                onClick = {
                                    val permNow = SystemMascotOverlayManager.canDrawSystemOverlays(context)
                                    hasOverlayPerm = permNow
                                    if (!permNow) {
                                        showForceOverlayPermissionDialog = true
                                    } else {
                                        SystemMascotOverlayManager.showSystemMascotOverlay(
                                            context = context,
                                            targetAppLabel = selectedTargetAppLabel,
                                            securityConfig = securityConfig
                                        )
                                        Toast.makeText(
                                            context,
                                            "Overlay Maskot 3D ditampilkan di kanan bawah layar! Ketuk maskot untuk tes PIN.",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("test_spawn_system_mascot_overlay_button"),
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                Text(
                                    text = "Paksa Munculkan & Tes Overlay Maskot Sekarang",
                                    style = MaterialTheme.typography.labelMedium,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = DuoMacawBlueLip
                                )
                            }
                        }
                    }

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(54.dp)
                            .duolingoButton(
                                faceColor = if (isExternalTarget && !hasOverlayPerm) DuoFoxOrange else DuoFeatherGreen,
                                lipColor = if (isExternalTarget && !hasOverlayPerm) DuoFoxOrangeLip else DuoFeatherGreenLip,
                                cornerRadius = 16.dp,
                                lipHeight = 4.dp
                            )
                            .clickable {
                                val currentPerm = SystemMascotOverlayManager.canDrawSystemOverlays(context)
                                hasOverlayPerm = currentPerm
                                if (isExternalTarget && !currentPerm) {
                                    // Jangan buka aplikasi asli tanpa overlay! Paksa buka dialog izin & auto-launch setelah diizinkan!
                                    pendingFlashLaunchAfterPermission = true
                                    showForceOverlayPermissionDialog = true
                                    return@clickable
                                }
                                syncSystemDisguiseIdentity(
                                    context = context,
                                    identityMode = identityMode,
                                    presetId = selectedPresetId,
                                    customAppName = customAppName,
                                    customIconId = customIconId,
                                    customColorHex = customColorHex,
                                    targetAppLabel = selectedTargetAppLabel,
                                    targetAppId = selectedTargetAppId,
                                    applyLauncherAlias = true
                                )
                                if (isExternalTarget) {
                                    SystemMascotOverlayManager.showSystemMascotOverlay(
                                        context = context,
                                        targetAppLabel = selectedTargetAppLabel,
                                        securityConfig = securityConfig
                                    )
                                }
                                onApplyAndRestartToCamouflage(
                                    identityMode,
                                    selectedPresetId,
                                    customAppName,
                                    customIconId,
                                    customColorHex,
                                    selectedTargetAppId,
                                    selectedTargetAppLabel
                                )
                            }
                            .padding(bottom = 4.dp)
                            .testTag("apply_and_restart_camouflage_button"),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.RestartAlt,
                                contentDescription = null,
                                tint = DuoPureWhite,
                                modifier = Modifier.size(22.dp)
                            )
                            Text(
                                text = if (isExternalTarget && !hasOverlayPerm) {
                                    "Izinkan Overlay & Buka Kilat '$selectedTargetAppLabel'"
                                } else {
                                    "Simpan, Ubah Ikon & Buka Kilat '$selectedTargetAppLabel'"
                                },
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.ExtraBold,
                                color = DuoPureWhite
                            )
                        }
                    }
                }
            }
        }
    }
}

/**
 * Dialog Paksa Izin Overlay Sistem ("Tampilkan di atas aplikasi lain" + "Pop-up latar belakang").
 * Muncul otomatis saat pengguna memilih aplikasi eksternal (`PKG:...`) namun izin overlay sistem belum aktif.
 * Begitu pengguna mengaktifkan izin di Pengaturan Android, aplikasi mendeteksinya secara real-time!
 */
@Composable
fun ForceSystemOverlayPermissionDialog(
    targetAppLabel: String,
    hasOverlayPermission: Boolean,
    isPendingAutoLaunch: Boolean,
    onOpenOverlaySettings: () -> Unit,
    onOpenPopupDetailsSettings: () -> Unit,
    onDismiss: () -> Unit
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(0.92f)
                .duolingoCard(
                    backgroundColor = DuoPureWhite,
                    borderColor = DuoCardinalRed,
                    lipColor = DuoCardinalRedLip,
                    cornerRadius = 24.dp,
                    borderWidth = 2.5.dp,
                    lipHeight = 6.dp
                )
                .padding(bottom = 6.dp)
                .padding(18.dp)
                .testTag("force_system_overlay_permission_dialog")
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Surface(
                    color = DuoCardinalRedSoft,
                    shape = RoundedCornerShape(12.dp),
                    border = androidx.compose.foundation.BorderStroke(1.5.dp, DuoCardinalRed)
                ) {
                    Text(
                        text = "WAJIB AKTIFKAN IZIN OVERLAY & POP-UP",
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.ExtraBold,
                        color = DuoCardinalRedLip,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                    )
                }

                Text(
                    text = "Agar Maskot Muncul di Atas '$targetAppLabel'",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.ExtraBold,
                    color = VaultObsidian,
                    textAlign = TextAlign.Center
                )

                Text(
                    text = "Android memblokir tampilan Maskot di atas aplikasi lain kecuali Anda mengaktifkan izin sistem berikut:\n\n" +
                        "1. Ketuk tombol merah di bawah, cari aplikasi ini, lalu aktifkan sakelar 'Tampilkan di atas aplikasi lain' (Appear on top / Draw over other apps).\n" +
                        "2. Untuk HP Xiaomi/MIUI, OPPO, Vivo, Realme, atau Samsung: buka juga 'Izin Pop-up Latar Belakang' dan izinkan Tampilkan Jendela Pop-Up.",
                    style = MaterialTheme.typography.bodySmall,
                    color = VaultObsidian
                )

                if (isPendingAutoLaunch) {
                    Surface(
                        color = DuoMacawBlueSoft,
                        shape = RoundedCornerShape(12.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, DuoMacawBlue),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "DETEKSI OTOMATIS AKTIF: Begitu Anda menyalakan sakelar izin di Setting, aplikasi '$targetAppLabel' + Overlay Maskot akan langsung terbuka otomatis!",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = DuoMacawBlueLip,
                            modifier = Modifier.padding(10.dp),
                            textAlign = TextAlign.Center
                        )
                    }
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .duolingoButton(
                            faceColor = DuoFeatherGreen,
                            lipColor = DuoFeatherGreenLip,
                            cornerRadius = 14.dp,
                            lipHeight = 4.dp
                        )
                        .clickable { onOpenOverlaySettings() }
                        .padding(bottom = 4.dp)
                        .testTag("dialog_open_overlay_settings_button"),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "1. Buka Setting: Tampilkan di Atas App Lain",
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.ExtraBold,
                        color = DuoPureWhite
                    )
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(46.dp)
                        .duolingoButton(
                            faceColor = DuoMacawBlue,
                            lipColor = DuoMacawBlueLip,
                            cornerRadius = 14.dp,
                            lipHeight = 4.dp
                        )
                        .clickable { onOpenPopupDetailsSettings() }
                        .padding(bottom = 4.dp)
                        .testTag("dialog_open_popup_details_button"),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "2. Buka Izin Pop-Up Latar Belakang (MIUI/OEM)",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.ExtraBold,
                        color = DuoPureWhite
                    )
                }

                OutlinedButton(
                    onClick = onDismiss,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = if (hasOverlayPermission) "Selesai" else "Nanti Saja",
                        fontWeight = FontWeight.Bold,
                        color = VaultObsidian
                    )
                }
            }
        }
    }
}

/**
 * Host Layar Penuh untuk Aplikasi Tujuan Kamuflase + Peluncur Kilat Aplikasi Asli + Overlay Maskot Melayang + Input PIN Overlay.
 *
 * - Saat aplikasi dibuka dari Launcher dan `isCamouflageRedirectArmed == true`:
 *   1. Jika aplikasi tujuan adalah aplikasi HP terinstal (`PKG:...`):
 *      - Jika izin `Settings.canDrawOverlays` SUDAH AKTIF: secara kilat langsung membuka aplikasi tersebut (`launchExternalAppFlashWithMascotOverlay`) + menyalakan System Floating Mascot Overlay di atasnya!
 *      - Jika izin `Settings.canDrawOverlays` BELUM AKTIF: memaksa pengguna mengaktifkan izin di Setting terlebih dahulu (tidak pernah membuka aplikasi eksternal tanpa overlay!), lalu otomatis membuka aplikasi tujuan + overlay begitu izin diaktifkan!
 *   2. Di atas aplikasi tujuan muncul Maskot Arcanum Melayang.
 *   3. Ketika Maskot diklik, muncul Popup Input PIN Overlay tepat di atas aplikasi tanpa keluar aplikasi.
 *   4. Jika PIN SALAH -> Overlay PIN & Maskot langsung dimatikan/menghilang, dan tetap berada di aplikasi tujuan!
 *   5. Jika PIN BENAR -> Otomatis masuk ke dalam Arcanum Vault / Ruang Pengaturan!
 */
@Composable
fun CamouflageTargetAppHostWithMascotPinOverlay(
    securityConfig: SecurityConfig,
    isMascotDismissedByWrongPin: Boolean,
    onSubmitOverlayPin: (String) -> Boolean,
    onSecretResummonMascot: () -> Unit
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    var isPinPopupOpen by remember { mutableStateOf(false) }
    var pinInputText by remember { mutableStateOf("") }
    var showPinChars by remember { mutableStateOf(false) }

    var hasOverlayPerm by remember {
        mutableStateOf(SystemMascotOverlayManager.canDrawSystemOverlays(context))
    }

    // Draggable offset for the Floating Mascot Overlay
    var mascotOffsetX by remember { mutableFloatStateOf(0f) }
    var mascotOffsetY by remember { mutableFloatStateOf(0f) }

    // Secret owner triple-tap counter on top title bar in case mascot was dismissed by wrong PIN
    var secretTapCount by remember { mutableIntStateOf(0) }
    var lastTapTimeMs by remember { mutableLongStateOf(0L) }

    val targetId = securityConfig.camouflageTargetAppId
    val targetLabel = securityConfig.camouflageTargetAppLabel.ifBlank { "Kalkulator Ilmiah Pro" }

    LaunchedEffect(securityConfig) {
        SystemMascotOverlayManager.updateCachedSecurityConfig(securityConfig)
    }

    DisposableEffect(lifecycleOwner, context) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                hasOverlayPerm = SystemMascotOverlayManager.canDrawSystemOverlays(context)
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    // Poll every 350ms so the instant the user turns on "Display over other apps" in Android Settings,
    // `hasOverlayPerm` becomes true and triggers the automatic flash-launch + overlay!
    LaunchedEffect(context) {
        while (true) {
            val nowPerm = SystemMascotOverlayManager.canDrawSystemOverlays(context)
            if (nowPerm != hasOverlayPerm) {
                hasOverlayPerm = nowPerm
            }
            delay(350L)
        }
    }

    // Sync system task description with the active disguise identity
    LaunchedEffect(
        securityConfig.camouflageIdentityMode,
        securityConfig.camouflagePresetId,
        securityConfig.camouflageCustomAppName,
        securityConfig.camouflageTargetAppLabel
    ) {
        syncSystemDisguiseIdentity(
            context = context,
            identityMode = securityConfig.camouflageIdentityMode,
            presetId = securityConfig.camouflagePresetId,
            customAppName = securityConfig.camouflageCustomAppName,
            customIconId = securityConfig.camouflageCustomIconId,
            customColorHex = securityConfig.camouflageCustomColorHex,
            targetAppLabel = securityConfig.camouflageTargetAppLabel,
            targetAppId = securityConfig.camouflageTargetAppId,
            applyLauncherAlias = false
        )
    }

    // SECARA KILAT langsung buka aplikasi asli yang dipilih + nyalakan System Floating Mascot Overlay begitu `hasOverlayPerm` aktif!
    LaunchedEffect(targetId, isMascotDismissedByWrongPin, hasOverlayPerm) {
        if (!isMascotDismissedByWrongPin && targetId.startsWith("PKG:", ignoreCase = true)) {
            if (hasOverlayPerm) {
                launchExternalAppFlashWithMascotOverlay(
                    context = context,
                    targetAppId = targetId,
                    targetAppLabel = targetLabel,
                    securityConfig = securityConfig,
                    onSubmitOverlayPin = onSubmitOverlayPin
                )
            }
        } else if (isMascotDismissedByWrongPin) {
            SystemMascotOverlayManager.dismissSystemOverlay(context)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .testTag("camouflage_target_app_host")
    ) {
        // 1. LAYER BAWAH: Aplikasi Tujuan yang Sedang Dibuka (Tetap berjalan penuh tanpa keluar aplikasi!)
        when {
            targetId.equals("BUILTIN_NOTES", ignoreCase = true) -> {
                BuiltInNotesCamouflageApp(
                    appTitle = targetLabel,
                    onHeaderSecretTap = {
                        val now = System.currentTimeMillis()
                        secretTapCount = if (now - lastTapTimeMs < 700L) secretTapCount + 1 else 1
                        lastTapTimeMs = now
                        if (secretTapCount >= 3) {
                            secretTapCount = 0
                            onSecretResummonMascot()
                        }
                    }
                )
            }
            targetId.equals("BUILTIN_WEATHER", ignoreCase = true) -> {
                BuiltInWeatherCamouflageApp(
                    appTitle = targetLabel,
                    onHeaderSecretTap = {
                        val now = System.currentTimeMillis()
                        secretTapCount = if (now - lastTapTimeMs < 700L) secretTapCount + 1 else 1
                        lastTapTimeMs = now
                        if (secretTapCount >= 3) {
                            secretTapCount = 0
                            onSecretResummonMascot()
                        }
                    }
                )
            }
            targetId.equals("BUILTIN_BROWSER", ignoreCase = true) -> {
                BuiltInBrowserCamouflageApp(
                    appTitle = targetLabel,
                    onHeaderSecretTap = {
                        val now = System.currentTimeMillis()
                        secretTapCount = if (now - lastTapTimeMs < 700L) secretTapCount + 1 else 1
                        lastTapTimeMs = now
                        if (secretTapCount >= 3) {
                            secretTapCount = 0
                            onSecretResummonMascot()
                        }
                    }
                )
            }
            targetId.startsWith("PKG:", ignoreCase = true) -> {
                val pkgName = targetId.removePrefix("PKG:").trim()
                InstalledDeviceAppBridgeWorkspace(
                    packageName = pkgName,
                    appLabel = targetLabel,
                    hasOverlayPermission = hasOverlayPerm,
                    onRequestOverlayPermission = {
                        openSystemOverlayPermissionSettings(context)
                    },
                    onRequestPopupPermission = {
                        openAppDetailsPopupPermissionSettings(context)
                    },
                    onLaunchExternalWithOverlay = {
                        if (!SystemMascotOverlayManager.canDrawSystemOverlays(context)) {
                            openSystemOverlayPermissionSettings(context)
                        } else {
                            launchExternalAppFlashWithMascotOverlay(
                                context = context,
                                targetAppId = targetId,
                                targetAppLabel = targetLabel,
                                securityConfig = securityConfig,
                                onSubmitOverlayPin = onSubmitOverlayPin
                            )
                        }
                    },
                    onHeaderSecretTap = {
                        val now = System.currentTimeMillis()
                        secretTapCount = if (now - lastTapTimeMs < 700L) secretTapCount + 1 else 1
                        lastTapTimeMs = now
                        if (secretTapCount >= 3) {
                            secretTapCount = 0
                            onSecretResummonMascot()
                        }
                    }
                )
            }
            else -> {
                BuiltInCalculatorCamouflageApp(
                    appTitle = targetLabel,
                    onHeaderSecretTap = {
                        val now = System.currentTimeMillis()
                        secretTapCount = if (now - lastTapTimeMs < 700L) secretTapCount + 1 else 1
                        lastTapTimeMs = now
                        if (secretTapCount >= 3) {
                            secretTapCount = 0
                            onSecretResummonMascot()
                        }
                    }
                )
            }
        }

        // 2. LAYER ATAS 1: Floating Mascot Overlay (Hanya tampil jika belum di-dismiss karena salah PIN)
        AnimatedVisibility(
            visible = !isMascotDismissedByWrongPin,
            enter = fadeIn() + scaleIn(),
            exit = fadeOut() + scaleOut(),
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 18.dp, bottom = 84.dp)
                .offset { IntOffset(mascotOffsetX.roundToInt(), mascotOffsetY.roundToInt()) }
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .pointerInput(Unit) {
                        detectDragGestures { change, dragAmount ->
                            change.consume()
                            mascotOffsetX = (mascotOffsetX + dragAmount.x).coerceIn(-700f, 120f)
                            mascotOffsetY = (mascotOffsetY + dragAmount.y).coerceIn(-1200f, 160f)
                        }
                    }
                    .clickable {
                        pinInputText = ""
                        isPinPopupOpen = !isPinPopupOpen
                    }
                    .testTag("camouflage_floating_mascot_overlay_button")
            ) {
                Surface(
                    color = VaultObsidian.copy(alpha = 0.88f),
                    shape = RoundedCornerShape(24.dp),
                    border = androidx.compose.foundation.BorderStroke(2.dp, DuoFeatherGreen),
                    shadowElevation = 10.dp
                ) {
                    Box(
                        modifier = Modifier
                            .size(72.dp)
                            .padding(4.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CosmicDragonAvatar(
                            mood = MascotMood.SUCCESS_LEAP,
                            size = 62.dp,
                            externalCheerKey = AppThemeController.poseTriggerNonce
                        )
                    }
                }
            }
        }

        // 3. LAYER ATAS 2: Popup Input PIN Overlay (Tanpa keluar dari aplikasi yang sedang dibuka!)
        AnimatedVisibility(
            visible = isPinPopupOpen && !isMascotDismissedByWrongPin,
            enter = fadeIn() + scaleIn(),
            exit = fadeOut() + scaleOut(),
            modifier = Modifier.align(Alignment.Center)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .duolingoCard(
                        backgroundColor = VaultObsidian.copy(alpha = 0.96f),
                        borderColor = DuoFeatherGreen,
                        lipColor = DuoFeatherGreenLip,
                        cornerRadius = 24.dp,
                        borderWidth = 2.dp,
                        lipHeight = 5.dp
                    )
                    .padding(bottom = 5.dp)
                    .padding(18.dp)
                    .testTag("camouflage_mascot_pin_overlay_card")
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
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
                            Icon(
                                imageVector = Icons.Default.Security,
                                contentDescription = null,
                                tint = DuoFeatherGreen,
                                modifier = Modifier.size(20.dp)
                            )
                            Text(
                                text = "Otorisasi Overlay Maskot",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.ExtraBold,
                                color = DuoPureWhite
                            )
                        }
                        IconButton(
                            onClick = {
                                isPinPopupOpen = false
                                pinInputText = ""
                            },
                            modifier = Modifier.size(28.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Tutup",
                                tint = DuoPureWhite.copy(alpha = 0.7f),
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }

                    Text(
                        text = "Masukkan PIN Brankas atau PIN Pengaturan tanpa keluar dari $targetLabel.",
                        style = MaterialTheme.typography.bodySmall,
                        color = DuoPureWhite.copy(alpha = 0.75f),
                        textAlign = TextAlign.Center
                    )

                    OutlinedTextField(
                        value = pinInputText,
                        onValueChange = {
                            pinInputText = it.uppercase().take(6)
                        },
                        placeholder = { Text("Masukkan 6 Karakter PIN...") },
                        singleLine = true,
                        visualTransformation = if (showPinChars) {
                            VisualTransformation.None
                        } else {
                            PasswordVisualTransformation()
                        },
                        trailingIcon = {
                            IconButton(onClick = { showPinChars = !showPinChars }) {
                                Icon(
                                    imageVector = if (showPinChars) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                    contentDescription = "Lihat PIN",
                                    tint = DuoPureWhite.copy(alpha = 0.8f)
                                )
                            }
                        },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = DuoPureWhite,
                            unfocusedTextColor = DuoPureWhite,
                            focusedBorderColor = DuoFeatherGreen,
                            unfocusedBorderColor = DuoSwanBorder.copy(alpha = 0.4f),
                            focusedContainerColor = Color(0xFF0F172A),
                            unfocusedContainerColor = Color(0xFF0F172A)
                        ),
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("camouflage_overlay_pin_input")
                    )

                    // Quick Compact Keypad inside Overlay
                    val keypadRows = listOf(
                        listOf("1", "2", "3"),
                        listOf("4", "5", "6"),
                        listOf("7", "8", "9"),
                        listOf("CLR", "0", "DEL")
                    )
                    Column(
                        verticalArrangement = Arrangement.spacedBy(6.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        keypadRows.forEach { rowKeys ->
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                rowKeys.forEach { keyLabel ->
                                    Surface(
                                        color = Color(0xFF1E293B),
                                        shape = RoundedCornerShape(10.dp),
                                        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF334155)),
                                        modifier = Modifier
                                            .weight(1f)
                                            .height(40.dp)
                                            .clickable {
                                                when (keyLabel) {
                                                    "CLR" -> pinInputText = ""
                                                    "DEL" -> if (pinInputText.isNotEmpty()) {
                                                        pinInputText = pinInputText.dropLast(1)
                                                    }
                                                    else -> if (pinInputText.length < 6) {
                                                        pinInputText += keyLabel
                                                    }
                                                }
                                            }
                                    ) {
                                        Box(contentAlignment = Alignment.Center) {
                                            Text(
                                                text = keyLabel,
                                                style = MaterialTheme.typography.titleSmall,
                                                fontWeight = FontWeight.ExtraBold,
                                                color = DuoPureWhite
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedButton(
                            onClick = {
                                isPinPopupOpen = false
                                pinInputText = ""
                            },
                            modifier = Modifier
                                .weight(1f)
                                .height(44.dp),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text("Batal", color = DuoPureWhite, fontWeight = FontWeight.Bold)
                        }

                        Button(
                            onClick = {
                                val candidate = pinInputText.trim()
                                isPinPopupOpen = false
                                pinInputText = ""
                                // Jika benar -> auto masuk Arcanum; Jika salah -> overlay maskot langsung hilang & app tetap terbuka!
                                onSubmitOverlayPin(candidate)
                            },
                            modifier = Modifier
                                .weight(1f)
                                .height(44.dp)
                                .testTag("camouflage_overlay_pin_submit_button"),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = DuoFeatherGreen,
                                contentColor = VaultObsidian
                            )
                        ) {
                            Icon(Icons.Default.LockOpen, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Masuk", fontWeight = FontWeight.ExtraBold)
                        }
                    }
                }
            }
        }
    }
}

/**
 * Aplikasi Tujuan Bawaan 1: Kalkulator Ilmiah Pro (100% Fungsional)
 */
@Composable
private fun BuiltInCalculatorCamouflageApp(
    appTitle: String,
    onHeaderSecretTap: () -> Unit
) {
    var expression by remember { mutableStateOf("") }
    var displayResult by remember { mutableStateOf("0") }
    val historyList = remember { mutableStateListOf<String>() }

    fun evaluateSimpleMath(expr: String): String {
        val clean = expr.replace("×", "*").replace("÷", "/").trim()
        if (clean.isEmpty()) return "0"
        return try {
            val tokens = Regex("(?<=[-+*/])|(?=[-+*/])").split(clean).map { it.trim() }.filter { it.isNotEmpty() }
            if (tokens.isEmpty()) return "0"
            var acc = tokens[0].toDoubleOrNull() ?: return "0"
            var i = 1
            while (i + 1 < tokens.size) {
                val op = tokens[i]
                val nextVal = tokens[i + 1].toDoubleOrNull() ?: break
                when (op) {
                    "+" -> acc += nextVal
                    "-" -> acc -= nextVal
                    "*" -> acc *= nextVal
                    "/" -> acc = if (nextVal != 0.0) acc / nextVal else 0.0
                }
                i += 2
            }
            if (acc % 1.0 == 0.0) acc.toLong().toString() else "%.4f".format(acc).trimEnd('0').trimEnd('.')
        } catch (_: Exception) {
            "0"
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0F172A))
            .statusBarsPadding()
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        // Top Header Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onHeaderSecretTap() }
                .padding(vertical = 6.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Calculate,
                    contentDescription = null,
                    tint = Color(0xFF38BDF8),
                    modifier = Modifier.size(24.dp)
                )
                Text(
                    text = appTitle,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.White
                )
            }
            Text(
                text = "RAD •DEG",
                style = MaterialTheme.typography.labelSmall,
                color = Color(0xFF94A3B8)
            )
        }

        // Display Screen
        Surface(
            color = Color(0xFF1E293B),
            shape = RoundedCornerShape(22.dp),
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(vertical = 10.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(18.dp),
                verticalArrangement = Arrangement.Bottom,
                horizontalAlignment = Alignment.End
            ) {
                historyList.takeLast(3).forEach { hist ->
                    Text(
                        text = hist,
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFF64748B)
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = expression.ifBlank { "0" },
                    style = MaterialTheme.typography.titleLarge,
                    color = Color(0xFF94A3B8),
                    maxLines = 1
                )
                Text(
                    text = displayResult,
                    fontSize = 42.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.White,
                    maxLines = 1
                )
            }
        }

        // Keypad Grid
        val buttons = listOf(
            listOf("C", "( )", "%", "÷"),
            listOf("7", "8", "9", "×"),
            listOf("4", "5", "6", "-"),
            listOf("1", "2", "3", "+"),
            listOf("0", ".", "DEL", "=")
        )
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            buttons.forEach { row ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    row.forEach { key ->
                        val isOperator = key in listOf("÷", "×", "-", "+", "=")
                        val bgColor = when (key) {
                            "=" -> Color(0xFF0EA5E9)
                            "C" -> Color(0xFFEF4444)
                            else -> if (isOperator) Color(0xFF334155) else Color(0xFF1E293B)
                        }
                        Surface(
                            color = bgColor,
                            shape = RoundedCornerShape(18.dp),
                            modifier = Modifier
                                .weight(1f)
                                .height(62.dp)
                                .clickable {
                                    when (key) {
                                        "C" -> {
                                            expression = ""
                                            displayResult = "0"
                                        }
                                        "DEL" -> {
                                            if (expression.isNotEmpty()) {
                                                expression = expression.dropLast(1)
                                                displayResult = evaluateSimpleMath(expression)
                                            }
                                        }
                                        "=" -> {
                                            val res = evaluateSimpleMath(expression)
                                            if (expression.isNotBlank()) {
                                                historyList.add("$expression = $res")
                                            }
                                            displayResult = res
                                            expression = res
                                        }
                                        else -> {
                                            expression += key
                                            displayResult = evaluateSimpleMath(expression)
                                        }
                                    }
                                }
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(
                                    text = key,
                                    fontSize = 22.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

/**
 * Aplikasi Tujuan Bawaan 2: Catatan & Memo Harian Interaktif
 */
@Composable
private fun BuiltInNotesCamouflageApp(
    appTitle: String,
    onHeaderSecretTap: () -> Unit
) {
    val notes = remember {
        mutableStateListOf(
            "Daftar Belanja Mingguan" to "Beras 5kg, Kopi bubuk, Telur ayam, Susu UHT 2 kotak, Sabun cuci.",
            "Jadwal Rapat & Tugas" to "Senin 09:00 - Review laporan bulanan\nRabu 14:00 - Presentasi desain proyek",
            "Target Olahraga" to "Lari pagi 3 km setiap Selasa & Jumat, Push-up 30x."
        )
    }
    var newTitle by remember { mutableStateOf("") }
    var newBody by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFFFBEB))
            .statusBarsPadding()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onHeaderSecretTap() },
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Description,
                    contentDescription = null,
                    tint = Color(0xFFD97706),
                    modifier = Modifier.size(26.dp)
                )
                Text(
                    text = appTitle,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color(0xFF78350F)
                )
            }
            Text(
                text = "${notes.size} Catatan",
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFB45309)
            )
        }

        Surface(
            color = Color.White,
            shape = RoundedCornerShape(16.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFFDE68A)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(12.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = newTitle,
                    onValueChange = { newTitle = it },
                    placeholder = { Text("Judul catatan baru...") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp)
                )
                OutlinedTextField(
                    value = newBody,
                    onValueChange = { newBody = it },
                    placeholder = { Text("Tulis isi memo atau daftar belanja...") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp)
                )
                Button(
                    onClick = {
                        if (newTitle.isNotBlank() || newBody.isNotBlank()) {
                            notes.add(0, (newTitle.ifBlank { "Catatan Baru" }) to newBody)
                            newTitle = ""
                            newBody = ""
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFF59E0B),
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Tambah Catatan", fontWeight = FontWeight.Bold)
                }
            }
        }

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.weight(1f)
        ) {
            items(notes) { (title, body) ->
                Surface(
                    color = Color(0xFFFEF3C7),
                    shape = RoundedCornerShape(14.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFFCD34D)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(14.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = title,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color(0xFF78350F)
                        )
                        Text(
                            text = body,
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color(0xFF92400E)
                        )
                    }
                }
            }
        }
    }
}

/**
 * Aplikasi Tujuan Bawaan 3: Cuaca & Radar Live Interaktif
 */
@Composable
private fun BuiltInWeatherCamouflageApp(
    appTitle: String,
    onHeaderSecretTap: () -> Unit
) {
    val cities = remember {
        listOf(
            Triple("Jakarta", "31°C", "Cerah Berawan • Kelembapan 68%"),
            Triple("Bandung", "24°C", "Sejuk Berkabut • Kelembapan 78%"),
            Triple("Surabaya", "33°C", "Cerah Terik • Kelembapan 62%"),
            Triple("Yogyakarta", "29°C", "Berawan Lembut • Kelembapan 71%"),
            Triple("Denpasar", "30°C", "Angin Pantai Cerah • UV Index 7")
        )
    }
    var selectedCityIdx by remember { mutableIntStateOf(0) }
    val currentCity = cities[selectedCityIdx]

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color(0xFF0284C7), Color(0xFF0F172A))
                )
            )
            .statusBarsPadding()
            .padding(18.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onHeaderSecretTap() },
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.WbSunny,
                    contentDescription = null,
                    tint = Color(0xFFFACC15),
                    modifier = Modifier.size(26.dp)
                )
                Text(
                    text = appTitle,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.White
                )
            }
            Text(
                text = "LIVE RADAR",
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFBAE6FD)
            )
        }

        Surface(
            color = Color.White.copy(alpha = 0.14f),
            shape = RoundedCornerShape(24.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(
                    text = currentCity.first,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.White
                )
                Text(
                    text = currentCity.second,
                    fontSize = 56.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color(0xFFFACC15)
                )
                Text(
                    text = currentCity.third,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White.copy(alpha = 0.9f)
                )
            }
        }

        Text(
            text = "Pilih Kota Pemantauan:",
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )

        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(cities.indices.toList()) { idx ->
                val city = cities[idx]
                Surface(
                    color = if (selectedCityIdx == idx) Color.White.copy(alpha = 0.25f) else Color.White.copy(alpha = 0.1f),
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { selectedCityIdx = idx }
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = city.first,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Text(
                                text = city.third,
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.White.copy(alpha = 0.75f)
                            )
                        }
                        Text(
                            text = city.second,
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color(0xFFFACC15)
                        )
                    }
                }
            }
        }
    }
}

/**
 * Aplikasi Tujuan Bawaan 4: Browser Web Cepat Interaktif
 */
@Composable
private fun BuiltInBrowserCamouflageApp(
    appTitle: String,
    onHeaderSecretTap: () -> Unit
) {
    var urlQuery by remember { mutableStateOf("https://id.wikipedia.org/wiki/Teknologi") }
    var activeTopic by remember { mutableStateOf("Teknologi & Sains Modern") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8FAFC))
            .statusBarsPadding()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onHeaderSecretTap() },
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Language,
                contentDescription = null,
                tint = Color(0xFF2563EB),
                modifier = Modifier.size(24.dp)
            )
            Text(
                text = appTitle,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.ExtraBold,
                color = Color(0xFF0F172A)
            )
        }

        OutlinedTextField(
            value = urlQuery,
            onValueChange = { urlQuery = it },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(50)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            listOf("Teknologi", "Olahraga", "Ekonomi", "Kuliner").forEach { topic ->
                Surface(
                    color = Color(0xFFE2E8F0),
                    shape = RoundedCornerShape(50),
                    modifier = Modifier.clickable {
                        activeTopic = "Berita Utama: $topic Hari Ini"
                        urlQuery = "https://id.wikipedia.org/wiki/$topic"
                    }
                ) {
                    Text(
                        text = topic,
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1E293B),
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                    )
                }
            }
        }

        Surface(
            color = Color.White,
            shape = RoundedCornerShape(18.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFCBD5E1)),
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(18.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    text = activeTopic,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color(0xFF0F172A)
                )
                Text(
                    text = "Sumber: $urlQuery",
                    style = MaterialTheme.typography.labelSmall,
                    color = Color(0xFF64748B)
                )
                Text(
                    text = "Perkembangan inovasi digital dan komputasi modern terus meningkatkan efisiensi kerja sehari-hari. Artikel ringkasan ini dimuat dalam mode baca cepat hemat kuota.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xFF334155)
                )
            }
        }
    }
}

/**
 * Bridge Workspace untuk Aplikasi HP Terinstal:
 * - Jika izin `Settings.canDrawOverlays` BELUM aktif: memaksa pengguna mengaktifkan "Tampilkan di atas aplikasi lain" & Pop-up di Setting terlebih dahulu (agar aplikasi eksternal tidak pernah terbuka tanpa overlay Maskot!).
 * - Begitu izin diaktifkan di Setting, aplikasi eksternal + Overlay Maskot langsung terbuka otomatis!
 */
@Composable
private fun InstalledDeviceAppBridgeWorkspace(
    packageName: String,
    appLabel: String,
    hasOverlayPermission: Boolean,
    onRequestOverlayPermission: () -> Unit,
    onRequestPopupPermission: () -> Unit,
    onLaunchExternalWithOverlay: () -> Unit,
    onHeaderSecretTap: () -> Unit
) {
    val context = LocalContext.current
    var appIconBitmap by remember(packageName) { mutableStateOf<ImageBitmap?>(null) }

    LaunchedEffect(packageName) {
        withContext(Dispatchers.IO) {
            try {
                val iconDrawable = context.packageManager.getApplicationIcon(packageName)
                appIconBitmap = drawableToImageBitmap(iconDrawable, 128)
            } catch (_: Exception) {
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0F172A))
            .statusBarsPadding()
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onHeaderSecretTap() },
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = appLabel,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.ExtraBold,
                color = Color.White
            )
            Text(
                text = packageName,
                style = MaterialTheme.typography.labelSmall,
                color = Color(0xFF94A3B8)
            )
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(14.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            if (appIconBitmap != null) {
                Image(
                    bitmap = appIconBitmap!!,
                    contentDescription = appLabel,
                    modifier = Modifier
                        .size(84.dp)
                        .clip(RoundedCornerShape(22.dp))
                )
            } else {
                Box(
                    modifier = Modifier
                        .size(84.dp)
                        .clip(RoundedCornerShape(22.dp))
                        .background(Color(0xFF1E293B)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Apps,
                        contentDescription = appLabel,
                        tint = Color(0xFF38BDF8),
                        modifier = Modifier.size(44.dp)
                    )
                }
            }

            Text(
                text = appLabel,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.ExtraBold,
                color = Color.White,
                textAlign = TextAlign.Center
            )

            if (!hasOverlayPermission) {
                Surface(
                    color = Color(0xFF1E293B),
                    shape = RoundedCornerShape(20.dp),
                    border = androidx.compose.foundation.BorderStroke(2.dp, DuoCardinalRed),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Surface(
                            color = DuoCardinalRed,
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(
                                text = "WAJIB AKTIFKAN IZIN OVERLAY DI ATAS APLIKASI LAIN",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.ExtraBold,
                                color = Color.White,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                                textAlign = TextAlign.Center
                            )
                        }

                        Text(
                            text = "Agar Maskot Arcanum & Input PIN melayang di atas '$appLabel', aktifkan izin 'Tampilkan di atas aplikasi lain' di Pengaturan Android sekarang.",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color(0xFFE2E8F0),
                            textAlign = TextAlign.Center
                        )

                        Text(
                            text = "Begitu sakelar diaktifkan di Setting, '$appLabel' + Overlay Maskot akan langsung terbuka otomatis!",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF38BDF8),
                            textAlign = TextAlign.Center
                        )

                        Button(
                            onClick = onRequestOverlayPermission,
                            shape = RoundedCornerShape(14.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = DuoFeatherGreen,
                                contentColor = Color.White
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp)
                                .testTag("bridge_enable_overlay_permission_button")
                        ) {
                            Icon(Icons.AutoMirrored.Filled.OpenInNew, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "1. Buka Setting: Tampilkan di Atas App Lain",
                                fontWeight = FontWeight.ExtraBold
                            )
                        }

                        OutlinedButton(
                            onClick = onRequestPopupPermission,
                            shape = RoundedCornerShape(14.dp),
                            border = androidx.compose.foundation.BorderStroke(1.5.dp, Color(0xFF38BDF8)),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(44.dp)
                                .testTag("bridge_enable_popup_permission_button")
                        ) {
                            Text(
                                text = "2. Buka Izin Pop-Up Latar Belakang (Xiaomi/OPPO/Vivo)",
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF38BDF8)
                            )
                        }
                    }
                }
            } else {
                Text(
                    text = "Izin Overlay Sistem Aktif • Maskot 3D & Keypad PIN Siaga di Atas '$appLabel'",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xFF4ADE80),
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )

                Button(
                    onClick = { onLaunchExternalWithOverlay() },
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF0EA5E9),
                        contentColor = Color.White
                    ),
                    modifier = Modifier
                        .fillMaxWidth(0.88f)
                        .height(50.dp)
                        .testTag("bridge_launch_external_with_overlay_button")
                ) {
                    Icon(Icons.AutoMirrored.Filled.OpenInNew, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Buka Kilat $appLabel + Overlay Maskot", fontWeight = FontWeight.ExtraBold)
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}
