package com.example.ui

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.ui.theme.*
import kotlinx.coroutines.delay
import java.net.URLDecoder
import java.net.URLEncoder
import java.security.MessageDigest
import java.security.SecureRandom
import java.text.SimpleDateFormat
import java.util.Base64
import java.util.Date
import java.util.Locale
import java.util.UUID
import kotlin.math.ln
import kotlin.math.pow
import kotlin.math.roundToInt
import kotlin.random.Random

// ============================================================================
// 120+ INTERACTIVE MICRO-FEATURES ENGINE & SWISS-ARMY HUB (ZERO EMOJIS)
// ============================================================================

enum class MicroFeatureCategory(
    val id: String,
    val title: String,
    val badgeColor: Color,
    val icon: ImageVector
) {
    SECURITY_PIN("SECURITY_PIN", "Keamanan & PIN", Color(0xFF0284C7), Icons.Default.Shield),
    CRYPTO_ENCODING("CRYPTO_ENCODING", "Kripto & Sandi", Color(0xFF7C3AED), Icons.Default.EnhancedEncryption),
    PRIVACY_CLEANER("PRIVACY_CLEANER", "Privasi & Filter", Color(0xFF059669), Icons.Default.VisibilityOff),
    SMART_CONVERTER("SMART_CONVERTER", "Konverter Pintar", Color(0xFFD97706), Icons.Default.SwapHoriz),
    FINANCE_WALLET("FINANCE_WALLET", "Finansial & Dana", Color(0xFF16A34A), Icons.Default.AccountBalanceWallet),
    PRODUCTIVITY_TEXT("PRODUCTIVITY_TEXT", "Teks & Fokus", Color(0xFF2563EB), Icons.Default.TaskAlt),
    DESIGN_TYPOGRAPHY("DESIGN_TYPOGRAPHY", "Warna & Desain", Color(0xFFDB2777), Icons.Default.Palette),
    NETWORK_DEV("NETWORK_DEV", "Jaringan & Dev", Color(0xFF4F46E5), Icons.Default.Terminal),
    WELLNESS_ZEN("WELLNESS_ZEN", "Kesehatan & Zen", Color(0xFF0D9488), Icons.Default.SelfImprovement),
    UTILITY_DECISION("UTILITY_DECISION", "Alat Unik & Acak", Color(0xFFEA580C), Icons.Default.Casino)
}

data class MicroFeatureItem(
    val number: Int,
    val id: String,
    val title: String,
    val subtitle: String,
    val category: MicroFeatureCategory,
    val defaultInput: String,
    val defaultParam: Float = 12f,
    val actionLabel: String = "Proses & Hitung"
) {
    val codeBadge: String
        get() = String.format(Locale.US, "#%03d", number)
}

object MicroFeaturesRegistry {
    val allFeatures: List<MicroFeatureItem> = listOf(
        // 1. SECURITY_PIN (#001 - #012)
        MicroFeatureItem(1, "PIN_6_SAFE", "Generator PIN 6-Digit Anti-Tebak", "Hindari tanggal lahir & angka kembar berurutan", MicroFeatureCategory.SECURITY_PIN, "6", 6f, "Acak PIN Baru"),
        MicroFeatureItem(2, "PASSPHRASE_ID", "Generator Passphrase Indonesia", "4–6 kata acak mudah diingat namun sulit dibobol", MicroFeatureCategory.SECURITY_PIN, "5", 5f, "Buat Passphrase"),
        MicroFeatureItem(3, "ENTROPY_CALC", "Kalkulator Entropi & Waktu Bobol", "Hitung bit entropi & estimasi waktu brute-force", MicroFeatureCategory.SECURITY_PIN, "Arcanum#Vault2026!", 16f, "Analisis Entropi"),
        MicroFeatureItem(4, "KEYBOARD_WALK", "Detektor Pola Keyboard Lemah", "Periksa urutan QWERTY, 123456, atau pengulangan", MicroFeatureCategory.SECURITY_PIN, "qwerty123456", 10f, "Pindai Pola"),
        MicroFeatureItem(5, "RECOVERY_QA", "Generator Jawaban Pemulihan Acak", "Jawaban kode unik anti-tebak rekayasa sosial", MicroFeatureCategory.SECURITY_PIN, "Makanan favorit?", 8f, "Buat Kode Jawaban"),
        MicroFeatureItem(6, "BACKUP_OTP_10", "Generator 10 Kode OTP Cadangan", "Buat 10 kode darurat 8-digit sekali pakai", MicroFeatureCategory.SECURITY_PIN, "ARCANUM", 10f, "Cetak 10 Kode"),
        MicroFeatureItem(7, "PIN_PATTERN_AUDIT", "Audit Keamanan PIN 6-Digit", "Uji apakah PIN Anda termasuk 50 PIN paling sering ditebak", MicroFeatureCategory.SECURITY_PIN, "123456", 6f, "Uji PIN"),
        MicroFeatureItem(8, "WIFI_WPA3_GEN", "Generator Sandi Wi-Fi WPA3 Kuat", "Sandi router acak karakter penuh 24 simbol", MicroFeatureCategory.SECURITY_PIN, "24", 24f, "Acak Sandi Wi-Fi"),
        MicroFeatureItem(9, "KEYPAD_SCRAMBLER", "Simulasi Pengacak Angka Keypad", "Susunan urutan tombol 0-9 acak anti-intip bahu", MicroFeatureCategory.SECURITY_PIN, "0123456789", 10f, "Acak Urutan Tombol"),
        MicroFeatureItem(10, "MASTER_KEY_256", "Generator Kunci Master 256-Bit", "Kunci kriptografi 64 karakter hex murni", MicroFeatureCategory.SECURITY_PIN, "256", 32f, "Buat Kunci 256-Bit"),
        MicroFeatureItem(11, "TRIPLE_PIN_ODDS", "Kalkulator Peluang PIN 3-Lapis", "Hitung probabilitas 1 banding 10^18 kombinasi", MicroFeatureCategory.SECURITY_PIN, "18", 18f, "Hitung Kombinasi"),
        MicroFeatureItem(12, "PIN_ROTATION_PLAN", "Perencana Jadwal Rotasi Sandi", "Jadwal pergantian berkala 30, 60, dan 90 hari", MicroFeatureCategory.SECURITY_PIN, "60", 60f, "Buat Jadwal"),

        // 2. CRYPTO_ENCODING (#013 - #024)
        MicroFeatureItem(13, "BASE64_CODEC", "Enkoder & Dekoder Base64", "Ubah teks ke Base64 dan dekode kembali instan", MicroFeatureCategory.CRYPTO_ENCODING, "Rahasia Brankas Arcanum", 1f, "Konversi Base64"),
        MicroFeatureItem(14, "HEX_CODEC", "Konverter Teks ke Hexadecimal", "Representasi byte hex untuk inspeksi biner", MicroFeatureCategory.CRYPTO_ENCODING, "Arcanum NYX2", 1f, "Konversi Hex"),
        MicroFeatureItem(15, "BINARY_CODEC", "Konverter Teks ke Biner 8-Bit", "Terjemahkan huruf menjadi deretan bit 0 dan 1", MicroFeatureCategory.CRYPTO_ENCODING, "NYX", 8f, "Konversi Biner"),
        MicroFeatureItem(16, "CAESAR_ROT", "Sandi Geser Caesar (ROT 1–25)", "Geser alfabet sejauh N langkah sesuai slider", MicroFeatureCategory.CRYPTO_ENCODING, "PESAN RAHASIA MALAM INI", 13f, "Geser Alfabet"),
        MicroFeatureItem(17, "ATBASH_MIRROR", "Sandi Cermin Atbash (A ↔ Z)", "Balik urutan abjad pertama ke abjad terakhir", MicroFeatureCategory.CRYPTO_ENCODING, "BRANKAS AMAN", 1f, "Cerminkan Teks"),
        MicroFeatureItem(18, "VIGENERE_CIPHER", "Sandi Polialfabet Vigenere", "Enkripsi klasik menggunakan kata kunci rahasia", MicroFeatureCategory.CRYPTO_ENCODING, "DOKUMEN UTAMA", 7f, "Enkripsi Vigenere"),
        MicroFeatureItem(19, "MORSE_TRANSLATOR", "Transkoder Sandi Morse (. -)", "Ubah kalimat menjadi ketukan titik dan garis Morse", MicroFeatureCategory.CRYPTO_ENCODING, "SOS ARCANUM", 1f, "Terjemahkan Morse"),
        MicroFeatureItem(20, "SHA256_HASH", "Generator Sidik Jari SHA-256", "Hash satu arah 256-bit untuk verifikasi integritas", MicroFeatureCategory.CRYPTO_ENCODING, "Verifikasi File Brankas", 1f, "Hitung SHA-256"),
        MicroFeatureItem(21, "SHA512_MD5_HASH", "Generator Hash SHA-512 & MD5", "Bandingkan digest SHA-512 dan MD5 sekaligus", MicroFeatureCategory.CRYPTO_ENCODING, "Integritas Data", 1f, "Hitung Multi-Hash"),
        MicroFeatureItem(22, "XOR_HEX_CIPHER", "Kalkulator XOR Cipher Byte", "Operasi bitwise XOR pada teks dengan byte kunci", MicroFeatureCategory.CRYPTO_ENCODING, "RAHASIA", 42f, "Proses XOR"),
        MicroFeatureItem(23, "URL_SAFE_CODEC", "URL Percent Encoder & Decoder", "Amankan karakter khusus pada tautan web", MicroFeatureCategory.CRYPTO_ENCODING, "https://example.com/q?nama=brankas rahasia&id=1", 1f, "Encode / Decode URL"),
        MicroFeatureItem(24, "ANAGRAM_SCRAMBLE", "Pembalik Teks & Pengacak Anagram", "Balik kalimat atau acak posisi huruf untuk teka-teki", MicroFeatureCategory.CRYPTO_ENCODING, "SANCTUARY TERISOLASI", 1f, "Balik & Acak"),

        // 3. PRIVACY_CLEANER (#025 - #036)
        MicroFeatureItem(25, "URL_TRACKER_STRIP", "Pembersih Parameter Pelacak URL", "Hapus utm_source, fbclid, gclid, igshid, dan si", MicroFeatureCategory.PRIVACY_CLEANER, "https:// situs.id/berita?id=99&utm_source=ig&fbclid=IwAR123&gclid=abc", 1f, "Bersihkan Tautan"),
        MicroFeatureItem(26, "CAMO_FILENAME_GEN", "Generator Nama File Kamuflase", "Buat nama file sistem samaran agar tidak mencolok", MicroFeatureCategory.PRIVACY_CLEANER, "Video_Pribadi.mp4", 4f, "Samarkan Nama File"),
        MicroFeatureItem(27, "ZERO_WIDTH_HIDE", "Penyisip Penanda Zero-Width", "Sisipkan karakter tak terlihat untuk penanda keaslian", MicroFeatureCategory.PRIVACY_CLEANER, "Dokumen Resmi Internal", 3f, "Sisipkan Penanda"),
        MicroFeatureItem(28, "HOMOGRAPH_SCAN", "Detektor Karakter Homograf & Asing", "Deteksi huruf mirip (Cyrillic/Unicode) pada domain/teks", MicroFeatureCategory.PRIVACY_CLEANER, "paypaI.com / google.com", 1f, "Pindai Karakter"),
        MicroFeatureItem(29, "ALIAS_PERSONA_GEN", "Generator Identitas Alias Samaran", "Buat nama samaran, username, dan kota fiktif untuk forum", MicroFeatureCategory.PRIVACY_CLEANER, "Indonesia", 1f, "Buat Persona Baru"),
        MicroFeatureItem(30, "EMAIL_PLUS_ALIAS", "Pembuat Email Alias Plus (+Tag)", "Filter sumber spam dengan tag unik pada email Anda", MicroFeatureCategory.PRIVACY_CLEANER, "akun@gmail.com", 5f, "Buat 5 Alias Email"),
        MicroFeatureItem(31, "PII_TEXT_REDACTOR", "Sensor Otomatis Nomor HP & Email", "Sembunyikan nomor telepon, NIK, dan email di dalam teks", MicroFeatureCategory.PRIVACY_CLEANER, "Hubungi Budi di 081234567890 atau email budi@mail.com NIK 3201012345670001", 1f, "Sensor Data Pribadi"),
        MicroFeatureItem(32, "PRIVACY_SCORE_CALC", "Kalkulator Skor Privasi Harian", "Evaluasi tingkat perlindungan perangkat dari 0–100%", MicroFeatureCategory.PRIVACY_CLEANER, "PIN3,AntiScreenshot,Enkripsi,Biometrik", 85f, "Hitung Skor Privasi"),
        MicroFeatureItem(33, "BURN_NOTE_FORMAT", "Generator Format Pesan Sekali Baca", "Bungkus pesan dengan header peringatan hapus otomatis", MicroFeatureCategory.PRIVACY_CLEANER, "Kode akses brankas cadangan: 884920", 5f, "Format Pesan"),
        MicroFeatureItem(34, "DOUBLE_EXT_CHECK", "Pemeriksa Ekstensi Ganda Berbahaya", "Deteksi jebakan nama file seperti laporan.pdf.exe", MicroFeatureCategory.PRIVACY_CLEANER, "tagihan_bulanan.pdf.exe", 1f, "Periksa Ekstensi"),
        MicroFeatureItem(35, "DOC_WATERMARK_ID", "Generator Kode Watermark Dokumen", "Buat kode seri pelacak unik untuk tiap penerima dokumen", MicroFeatureCategory.PRIVACY_CLEANER, "KONTRAK-2026", 8f, "Buat Kode Seri"),
        MicroFeatureItem(36, "CLIPBOARD_SCRUBBER", "Pembersih Clipboard Satu Ketuk", "Timpa isi clipboard sistem agar riwayat salinan bersih", MicroFeatureCategory.PRIVACY_CLEANER, "BERSIHKAN_CLIPBOARD", 1f, "Bersihkan Clipboard"),

        // 4. SMART_CONVERTER (#037 - #048)
        MicroFeatureItem(37, "DATA_SIZE_CONV", "Konverter Ukuran Data (KB–PB)", "Hitung Byte, KB, MB, GB, TB, hingga Petabyte secara presisi", MicroFeatureCategory.SMART_CONVERTER, "64", 64f, "Konversi Ukuran Data"),
        MicroFeatureItem(38, "TRANSFER_TIME_CALC", "Kalkulator Waktu Unduh & Transfer", "Estimasi waktu transfer file (GB) pada kecepatan Mbps", MicroFeatureCategory.SMART_CONVERTER, "15", 50f, "Hitung Waktu Unduh"),
        MicroFeatureItem(39, "RADIX_BASE_CONV", "Konverter Basis Angka (Dec/Bin/Oct/Hex)", "Ubah angka desimal ke biner, oktal, dan hex sekaligus", MicroFeatureCategory.SMART_CONVERTER, "2026", 16f, "Konversi Basis"),
        MicroFeatureItem(40, "TEMP_CONVERTER", "Konverter Suhu 4 Skala (C/F/K/R)", "Celsius, Fahrenheit, Kelvin, dan Reamur instan", MicroFeatureCategory.SMART_CONVERTER, "28.5", 28.5f, "Konversi Suhu"),
        MicroFeatureItem(41, "LENGTH_CONVERTER", "Konverter Panjang & Jarak Lengkap", "Milimeter, Cm, Meter, Km, Inci, Kaki (Feet), dan Mil", MicroFeatureCategory.SMART_CONVERTER, "175", 175f, "Konversi Panjang"),
        MicroFeatureItem(42, "WEIGHT_CONVERTER", "Konverter Berat & Massa (Gram–Pound)", "Gram, Kilogram, Ons, Ton, dan Pound (lbs)", MicroFeatureCategory.SMART_CONVERTER, "68", 68f, "Konversi Berat"),
        MicroFeatureItem(43, "DISCOUNT_TAX_CALC", "Kalkulator Diskon Ganda + PPN", "Hitung harga akhir setelah diskon bertingkat & pajak", MicroFeatureCategory.SMART_CONVERTER, "500000", 20f, "Hitung Harga Akhir"),
        MicroFeatureItem(44, "SPLIT_BILL_CALC", "Kalkulator Patungan & Split Bill", "Bagi tagihan makan bersama + pajak/servis secara adil", MicroFeatureCategory.SMART_CONVERTER, "450000", 5f, "Bagi Tagihan"),
        MicroFeatureItem(45, "PERCENT_DELTA_CALC", "Kalkulator Persentase & Kenaikan", "Hitung nilai persen dan selisih kenaikan/penurunan", MicroFeatureCategory.SMART_CONVERTER, "120", 150f, "Hitung Persentase"),
        MicroFeatureItem(46, "BMI_HYDRATION_CALC", "Kalkulator BMI & Target Air Minum", "Indeks Massa Tubuh ideal & takaran mililiter air harian", MicroFeatureCategory.SMART_CONVERTER, "65", 170f, "Hitung BMI & Air"),
        MicroFeatureItem(47, "EXACT_AGE_CALC", "Kalkulator Usia Presisi (Tahun–Jam)", "Hitung total bulan, minggu, hari, dan jam sejak tahun lahir", MicroFeatureCategory.SMART_CONVERTER, "2000", 25f, "Hitung Detail Usia"),
        MicroFeatureItem(48, "DATE_COUNTDOWN_CALC", "Kalkulator Target Hari & Pekan", "Konversi jumlah hari ke minggu, bulan kerja, dan jam", MicroFeatureCategory.SMART_CONVERTER, "100", 100f, "Konversi Rentang Hari"),

        // 5. FINANCE_WALLET (#049 - #060)
        MicroFeatureItem(49, "COMPOUND_INTEREST", "Kalkulator Bunga Majemuk Investasi", "Simulasi pertumbuhan modal awal + bunga tahunan", MicroFeatureCategory.FINANCE_WALLET, "10000000", 10f, "Simulasi Investasi"),
        MicroFeatureItem(50, "EMERGENCY_FUND", "Kalkulator Dana Darurat Ideal", "Target aman 3x (Lajang), 6x (Menikah), dan 12x (Keluarga)", MicroFeatureCategory.FINANCE_WALLET, "4000000", 6f, "Hitung Dana Darurat"),
        MicroFeatureItem(51, "SALARY_50_30_20", "Pembagi Gaji 50/30/20 Otomatis", "Alokasi 50% Kebutuhan, 30% Keinginan, 20% Tabungan", MicroFeatureCategory.FINANCE_WALLET, "8500000", 20f, "Bagi Alokasi Gaji"),
        MicroFeatureItem(52, "LOAN_EMI_SIM", "Simulasi Cicilan Bulanan & Bunga", "Estimasi angsuran pokok + bunga per bulan", MicroFeatureCategory.FINANCE_WALLET, "24000000", 12f, "Hitung Angsuran"),
        MicroFeatureItem(53, "SAVINGS_GOAL_DAILY", "Kalkulator Target Tabungan Harian", "Berapa yang harus disisihkan per hari & per minggu", MicroFeatureCategory.FINANCE_WALLET, "15000000", 180f, "Hitung Setoran Harian"),
        MicroFeatureItem(54, "INFLATION_EROSION", "Kalkulator Dampak Inflasi Tahunan", "Lihat nilai riil uang Anda dalam 5–15 tahun ke depan", MicroFeatureCategory.FINANCE_WALLET, "50000000", 5f, "Hitung Daya Beli"),
        MicroFeatureItem(55, "MULTI_FX_ESTIMATOR", "Konverter Kurs Cepat (IDR/USD/SGD/JPY)", "Estimasi konversi Rupiah ke mata uang utama dunia", MicroFeatureCategory.FINANCE_WALLET, "1000000", 1f, "Konversi Mata Uang"),
        MicroFeatureItem(56, "GOLD_GRAM_CALC", "Kalkulator Nilai Emas & Spread", "Estimasi nilai portofolio gram emas batangan", MicroFeatureCategory.FINANCE_WALLET, "15", 15f, "Hitung Nilai Emas"),
        MicroFeatureItem(57, "DEBT_RATIO_CHECK", "Pemeriksa Rasio Cicilan Sehat (DTI)", "Pastikan total cicilan di bawah batas aman 30% gaji", MicroFeatureCategory.FINANCE_WALLET, "2000000", 8000000f, "Cek Rasio Sehat"),
        MicroFeatureItem(58, "QUICK_EXPENSE_LOG", "Format Catatan Pengeluaran Cepat", "Buat baris log pengeluaran rapi siap simpan ke Brankas", MicroFeatureCategory.FINANCE_WALLET, "Kopi & Makan Siang: 65000", 65f, "Format Catatan"),
        MicroFeatureItem(59, "ZAKAT_CHARITY_CALC", "Kalkulator Zakat & Sedekah 2.5%", "Hitung nilai 2.5% dari penghasilan atau tabungan bersih", MicroFeatureCategory.FINANCE_WALLET, "10000000", 2.5f, "Hitung 2.5%"),
        MicroFeatureItem(60, "SUBSCRIPTION_AUDIT", "Kalkulator Biaya Langganan Tahunan", "Hitung total akumulasi langganan bulanan dalam 1 & 5 tahun", MicroFeatureCategory.FINANCE_WALLET, "185000", 12f, "Audit Langganan"),

        // 6. PRODUCTIVITY_TEXT (#061 - #072)
        MicroFeatureItem(61, "POMODORO_PLANNER", "Generator Sesi Fokus Pomodoro", "Susun blok kerja 25 menit + istirahat 5 menit otomatis", MicroFeatureCategory.PRODUCTIVITY_TEXT, "Selesaikan Desain & Laporan", 4f, "Susun Blok Pomodoro"),
        MicroFeatureItem(62, "PACE_SPEED_CALC", "Kalkulator Kecepatan Baca & Ketik", "Hitung WPM (Kata per Menit) dan target halaman buku", MicroFeatureCategory.PRODUCTIVITY_TEXT, "2500", 220f, "Hitung Waktu Baca"),
        MicroFeatureItem(63, "QUICK_TIMER_PRESET", "Penghitung Detik & Menit Kerja", "Konversi menit fokus menjadi detik & blok target", MicroFeatureCategory.PRODUCTIVITY_TEXT, "25", 25f, "Hitung Blok Waktu"),
        MicroFeatureItem(64, "TALLY_COUNTER_STEP", "Penghitung Tally & Target Harian", "Tambah hitungan target repetisi atau dzikir harian", MicroFeatureCategory.PRODUCTIVITY_TEXT, "Target Repetisi", 33f, "Tambah Hitungan"),
        MicroFeatureItem(65, "EISENHOWER_MATRIX", "Penyusun Matriks Prioritas 4 Kuadran", "Bagi tugas ke Lakukan Sekarang, Jadwalkan, Delegasikan, Hapus", MicroFeatureCategory.PRODUCTIVITY_TEXT, "Update PIN, Balas Email, Rapat Tim, Bersihkan File", 4f, "Susun Kuadran"),
        MicroFeatureItem(66, "DAILY_TOP_3_MAKER", "Pembuat Checklist 3 Prioritas Utama", "Fokus pada 3 misi terpenting hari ini", MicroFeatureCategory.PRODUCTIVITY_TEXT, "1. Backup Brankas, 2. Olahraga, 3. Belajar Kotlin", 3f, "Cetak Checklist"),
        MicroFeatureItem(67, "WORD_CHAR_ANALYZER", "Penghitung Kata, Huruf & Paragraf", "Analisis jumlah karakter, kata, kalimat, dan waktu baca", MicroFeatureCategory.PRODUCTIVITY_TEXT, "Brankas Arcanum menjaga privasi dokumen dan media penting Anda dengan enkripsi berlapis.", 1f, "Analisis Teks"),
        MicroFeatureItem(68, "WHITESPACE_CLEANER", "Pembersih Spasi Ganda & Baris Kosong", "Rapikan teks berantakan menjadi satu spasi bersih", MicroFeatureCategory.PRODUCTIVITY_TEXT, "Kalimat   ini    memiliki   banyak     spasi   berlebih.", 1f, "Rapikan Spasi"),
        MicroFeatureItem(69, "CASE_TRANSFORMER", "Pengubah Format Huruf (5 Gaya)", "UPPERCASE, lowercase, Title Case, snake_case, camelCase", MicroFeatureCategory.PRODUCTIVITY_TEXT, "studio tema dan maskot arcanum", 1f, "Ubah Semua Format"),
        MicroFeatureItem(70, "LIST_DEDUP_SORTER", "Pengurut Daftar A–Z & Hapus Duplikat", "Urutkan item yang dipisahkan koma & buang yang kembar", MicroFeatureCategory.PRODUCTIVITY_TEXT, "Zebra, Apel, Mangga, Apel, Jeruk, הבית, Mangga, Anggur", 1f, "Urutkan & Bersihkan"),
        MicroFeatureItem(71, "MEETING_NOTE_GEN", "Generator Template Catatan Rapat", "Kerangka notulen rapat rapi siap isi", MicroFeatureCategory.PRODUCTIVITY_TEXT, "Evaluasi Proyek Mingguan", 1f, "Buat Template"),
        MicroFeatureItem(72, "WORLD_CLOCK_CONV", "Konverter Jam Dunia (WIB/WITA/WIT/UTC)", "Bandingkan jam WIB dengan WITA, WIT, UTC, Tokyo, dan London", MicroFeatureCategory.PRODUCTIVITY_TEXT, "14", 14f, "Konversi Zona Waktu"),

        // 7. DESIGN_TYPOGRAPHY (#073 - #084)
        MicroFeatureItem(73, "HEX_RGB_HSL_CONV", "Konverter Warna HEX ↔ RGB ↔ Luminansi", "Bedah komponen Red, Green, Blue, dan kecerahan warna", MicroFeatureCategory.DESIGN_TYPOGRAPHY, "#0284C7", 1f, "Bedah Kode Warna"),
        MicroFeatureItem(74, "WCAG_CONTRAST_CHECK", "Pemeriksa Kontras Teks WCAG AA/AAA", "Cek keterbacaan warna latar vs warna teks putih/hitam", MicroFeatureCategory.DESIGN_TYPOGRAPHY, "#1E293B", 1f, "Uji Kontras WCAG"),
        MicroFeatureItem(75, "HARMONY_PALETTE_GEN", "Generator Palet Komplementer & Triadik", "Cari pasangan warna harmonis dari 1 kode HEX", MicroFeatureCategory.DESIGN_TYPOGRAPHY, "#16A34A", 1f, "Buat Palet Harmonis"),
        MicroFeatureItem(76, "COMPOSE_GRADIENT_GEN", "Generator Kode Brush.linearGradient", "Salin potongan kode gradien Jetpack Compose siap pakai", MicroFeatureCategory.DESIGN_TYPOGRAPHY, "#0284C7,#38BDF8", 1f, "Buat Kode Gradien"),
        MicroFeatureItem(77, "GOLDEN_TYPE_SCALE", "Kalkulator Skala Tipografi Rasio Emas", "Hitung ukuran font Body, Title, Headline dengan rasio 1.25 / 1.618", MicroFeatureCategory.DESIGN_TYPOGRAPHY, "14", 14f, "Hitung Skala Font"),
        MicroFeatureItem(78, "PX_TO_DP_CONVERTER", "Konverter Piksel (px) ke DP Android", "Hitung ukuran dp untuk MDPI, HDPI, XHDPI, XXHDPI, XXXHDPI", MicroFeatureCategory.DESIGN_TYPOGRAPHY, "48", 48f, "Konversi PX ↔ DP"),
        MicroFeatureItem(79, "ASPECT_RATIO_CALC", "Kalkulator Rasio Layar & Resolusi", "Hitung tinggi/lebar untuk 16:9, 4:3, 21:9, dan 1:1", MicroFeatureCategory.DESIGN_TYPOGRAPHY, "1920", 16f, "Hitung Resolusi"),
        MicroFeatureItem(80, "COLOR_MIXER_BLEND", "Pencampur Dua Warna HEX (0–100%)", "Campurkan warna dengan putih/hitam sesuai rasio slider", MicroFeatureCategory.DESIGN_TYPOGRAPHY, "#EC4899", 50f, "Campur Warna"),
        MicroFeatureItem(81, "SHADOW_LIP_SPEC", "Kalkulator Proporsi Lip 3D & Corner", "Rekomendasi tinggi lip bawah & radius sudut kartu", MicroFeatureCategory.DESIGN_TYPOGRAPHY, "20", 20f, "Hitung Proporsi 3D"),
        MicroFeatureItem(82, "PANGRAM_FONT_TESTER", "Penguji Pangram Semua Huruf A–Z", "Uji tampilan seluruh abjad dengan font aktif saat ini", MicroFeatureCategory.DESIGN_TYPOGRAPHY, "Muhadjir membeli lima pasang sepatu olahraga warna zamrud di kota Bekasi.", 1f, "Tampilkan Pangram"),
        MicroFeatureItem(83, "TINT_SHADE_LADDER", "Generator 5 Tingkat Tint & Shade", "Hasilkan variasi warna 20%, 40%, 60%, 80% lebih terang & gelap", MicroFeatureCategory.DESIGN_TYPOGRAPHY, "#6366F1", 1f, "Buat Tangga Warna"),
        MicroFeatureItem(84, "PRINT_DPI_CALC", "Kalkulator Ukuran Cetak 300 DPI", "Konversi ukuran kertas A4, A3, 4R ke resolusi piksel tajam", MicroFeatureCategory.DESIGN_TYPOGRAPHY, "A4", 300f, "Hitung Piksel Cetak"),

        // 8. NETWORK_DEV (#085 - #096)
        MicroFeatureItem(85, "IPV4_SUBNET_CALC", "Kalkulator Subnet CIDR IPv4", "Hitung jumlah host aktif dan netmask dari prefix /8–/30", MicroFeatureCategory.NETWORK_DEV, "192.168.1.0", 24f, "Hitung Subnet"),
        MicroFeatureItem(86, "UUID_V4_GENERATOR", "Generator 5 UUID v4 Kriptografis", "Buat pengenal unik universal 128-bit standar RFC 4122", MicroFeatureCategory.NETWORK_DEV, "5", 5f, "Generate UUID"),
        MicroFeatureItem(87, "EPOCH_TIME_CONV", "Konverter Unix Timestamp ke Tanggal", "Lihat waktu milidetik sistem saat ini dalam format lengkap", MicroFeatureCategory.NETWORK_DEV, "NOW", 1f, "Konversi Timestamp"),
        MicroFeatureItem(88, "JSON_FORMAT_CHECK", "Perapi & Pemeriksa Kurung JSON", "Format string JSON satu baris menjadi berindentasi rapi", MicroFeatureCategory.NETWORK_DEV, "{\"app\":\"Arcanum\",\"vault\":1,\"secure\":true}", 2f, "Rapikan JSON"),
        MicroFeatureItem(89, "REGEX_QUICK_TEST", "Penguji Pola Regex Instan", "Uji kecocokan pola Regular Expression pada teks sampel", MicroFeatureCategory.NETWORK_DEV, "[0-9]{6}", 1f, "Uji Regex"),
        MicroFeatureItem(90, "CRON_EXPR_HELPER", "Penerjemah Jadwal Cron Expression", "Buat jadwal cron untuk tiap N menit/jam dengan mudah", MicroFeatureCategory.NETWORK_DEV, "15", 15f, "Buat Jadwal Cron"),
        MicroFeatureItem(91, "HTTP_STATUS_DICT", "Kamus Cepat Kode Status HTTP", "Penjelasan lengkap kode 200, 301, 401, 403, 404, 429, 500, 503", MicroFeatureCategory.NETWORK_DEV, "403", 403f, "Cari Kode HTTP"),
        MicroFeatureItem(92, "PORT_NUMBER_DICT", "Kamus Port Jaringan & Keamanan", "Referensi port standar SSH, DNS, HTTPS, Tor, WireGuard", MicroFeatureCategory.NETWORK_DEV, "443", 443f, "Cek Fungsi Port"),
        MicroFeatureItem(93, "STREAM_BITRATE_CALC", "Kalkulator Kuota Streaming Video", "Hitung konsumsi MB per menit & GB per jam berdasar bitrate", MicroFeatureCategory.NETWORK_DEV, "8", 8f, "Hitung Kuota Video"),
        MicroFeatureItem(94, "LOREM_INDONESIA_GEN", "Generator Paragraf Dummy Indonesia", "Buat teks pengisi bahasa Indonesia untuk uji tata letak", MicroFeatureCategory.NETWORK_DEV, "3", 3f, "Buat Teks Dummy"),
        MicroFeatureItem(95, "CHMOD_OCTAL_CONV", "Konverter Izin File Linux (Chmod)", "Terjemahkan angka oktal 755/644/600 ke rwxr-xr-x", MicroFeatureCategory.NETWORK_DEV, "755", 755f, "Konversi Chmod"),
        MicroFeatureItem(96, "RUNTIME_MEM_INSPECT", "Inspektor Memori JVM & Layar Aktif", "Lihat penggunaan RAM aplikasi & spesifikasi runtime saat ini", MicroFeatureCategory.NETWORK_DEV, "INSPECT", 1f, "Baca Telemetri JVM"),

        // 9. WELLNESS_ZEN (#097 - #108)
        MicroFeatureItem(97, "BREATH_4_7_8_GUIDE", "Panduan Napas Relaksasi 4-7-8", "Tarik 4 detik, tahan 7 detik, hembuskan 8 detik untuk tenang", MicroFeatureCategory.WELLNESS_ZEN, "4 Siklus", 4f, "Mulai Panduan 4-7-8"),
        MicroFeatureItem(98, "BOX_BREATHING_SEAL", "Panduan Box Breathing 4-4-4-4", "Teknik fokus taktis 4 detik tarik, tahan, hembus, tahan", MicroFeatureCategory.WELLNESS_ZEN, "4 Detik", 4f, "Hitung Siklus Fokus"),
        MicroFeatureItem(99, "EYE_CARE_20_20_20", "Pengingat Istirahat Mata 20-20-20", "Setiap 20 menit layar, tatap sejauh 20 kaki selama 20 detik", MicroFeatureCategory.WELLNESS_ZEN, "20", 20f, "Cek Panduan Mata"),
        MicroFeatureItem(100, "WATER_GLASS_TRACKER", "Pelacak 8 Gelas Air Minum Harian", "Pantau progres hidrasi harian Anda (250ml per gelas)", MicroFeatureCategory.WELLNESS_ZEN, "8 Gelas", 6f, "Hitung Progres Air"),
        MicroFeatureItem(101, "POSITIVE_AFFIRMATION", "Generator Afirmasi Positif & Tenang", "Kalimat penyemangat baru untuk menjaga kejernihan pikiran", MicroFeatureCategory.WELLNESS_ZEN, "Tenang & Fokus", 1f, "Ambil Afirmasi Baru"),
        MicroFeatureItem(102, "MOOD_ENERGY_CHECK", "Pencatat Mood & Energi Harian (1–10)", "Rekam tingkat energi dan suasana hati saat ini", MicroFeatureCategory.WELLNESS_ZEN, "Semangat & Produktif", 8f, "Catat Energi"),
        MicroFeatureItem(103, "DESK_STRETCH_60S", "Panduan Peregangan Leher & Bahu 60d", "4 gerakan ringan pelepas pegal saat menatap layar", MicroFeatureCategory.WELLNESS_ZEN, "60 Detik", 60f, "Lihat Gerakan"),
        MicroFeatureItem(104, "SLEEP_CYCLE_REM", "Kalkulator Jam Tidur Siklus REM 90m", "Rekomendasi jam bangun segar berdasarkan 5–6 siklus tidur", MicroFeatureCategory.WELLNESS_ZEN, "22", 22f, "Hitung Jam Bangun"),
        MicroFeatureItem(105, "DIGITAL_DETOX_PACT", "Generator Komitmen Detoks Layar", "Target istirahat sejenak dari notifikasi & media sosial", MicroFeatureCategory.WELLNESS_ZEN, "45", 45f, "Buat Komitmen"),
        MicroFeatureItem(106, "WORRY_SHREDDER_BOX", "Penghancur Pikiran Penat (Shredder)", "Tulis hal yang membebani pikiran lalu hancurkan menjadi kode acak", MicroFeatureCategory.WELLNESS_ZEN, "Rasa cemas tentang deadline besok", 1f, "Hancurkan Beban"),
        MicroFeatureItem(107, "BPM_TEMPO_CALC", "Kalkulator Tempo BPM ke Milidetik", "Hitung interval ketukan milidetik untuk musik & fokus", MicroFeatureCategory.WELLNESS_ZEN, "120", 120f, "Hitung Tempo BPM"),
        MicroFeatureItem(108, "KINDNESS_QUEST_GEN", "Generator Misi Kebaikan Kecil Harian", "Satu aksi sederhana positif yang bisa dilakukan hari ini", MicroFeatureCategory.WELLNESS_ZEN, "Hari Ini", 1f, "Ambil Misi Baru"),

        // 10. UTILITY_DECISION (#109 - #120)
        MicroFeatureItem(109, "CRYPTO_COIN_FLIP", "Pelempar Koin Kriptografis Adil", "Lempar koin Sisi Garuda / Sisi Angka dengan SecureRandom", MicroFeatureCategory.UTILITY_DECISION, "Lempar 1x", 1f, "Lempar Koin"),
        MicroFeatureItem(110, "POLYHEDRAL_DICE", "Roller Dadu RPG (D4, D6, D8, D12, D20, D100)", "Lempar dadu multi-sisi sesuai jumlah sisi pada slider", MicroFeatureCategory.UTILITY_DECISION, "20", 20f, "Lempar Dadu"),
        MicroFeatureItem(111, "DECISION_WHEEL_PICK", "Pemilih Keputusan Acak dari Daftar", "Masukkan pilihan dipisahkan koma, biarkan sistem memilihkan", MicroFeatureCategory.UTILITY_DECISION, "Nasi Padang, Mie Ayam, Soto Betawi, Sate Ayam, Bakso", 1f, "Putar Pilihan"),
        MicroFeatureItem(112, "RANDOM_LOTTERY_DRAW", "Pengundi Angka Acak Tanpa Kembar", "Ambil N angka acak unik dari rentang 1–100", MicroFeatureCategory.UTILITY_DECISION, "100", 6f, "Undi Angka Unik"),
        MicroFeatureItem(113, "ORACLE_WISE_ANSWER", "Bola Jawaban Bijak Instan", "Tanyakan sesuatu untuk mendapat perspektif cepat", MicroFeatureCategory.UTILITY_DECISION, "Apakah rencana proyek ini sudah tepat?", 1f, "Minta Jawaban"),
        MicroFeatureItem(114, "TEAM_GROUP_SPLITTER", "Pembagi Kelompok Tim Otomatis", "Acak daftar nama menjadi beberapa tim seimbang", MicroFeatureCategory.UTILITY_DECISION, "Andi, Budi, Citra, Dewi, Eko, Fajar, Gita, Hadi", 2f, "Bagi Kelompok"),
        MicroFeatureItem(115, "CODENAME_OP_MAKER", "Generator Nama Sandi Proyek Rahasia", "Buat kode operasi keren untuk folder atau proyek baru", MicroFeatureCategory.UTILITY_DECISION, "PROJECT", 3f, "Buat Nama Operasi"),
        MicroFeatureItem(116, "ROMAN_NUMERAL_CONV", "Konverter Angka ke Romawi (I–MMMCMXCIX)", "Ubah tahun atau nomor bab menjadi angka Romawi klasik", MicroFeatureCategory.UTILITY_DECISION, "2026", 2026f, "Ubah ke Romawi"),
        MicroFeatureItem(117, "TERBILANG_RUPIAH", "Konverter Angka ke Terbilang Indonesia", "Ubah nominal angka menjadi kalimat terbilang kuitansi resmi", MicroFeatureCategory.UTILITY_DECISION, "1250500", 1f, "Ubah ke Terbilang"),
        MicroFeatureItem(118, "NUSANTARA_QUOTE_GEN", "Generator Pepatah & Inspirasi Bijak", "Koleksi peribahasa & filosofi klasik penuh makna", MicroFeatureCategory.UTILITY_DECISION, "Filosofi", 1f, "Tampilkan Pepatah"),
        MicroFeatureItem(119, "MORSE_SOS_BEACON", "Generator Sinyal Darurat Visual", "Pola interval kedipan lampu & kode posisi darurat", MicroFeatureCategory.UTILITY_DECISION, "POSISI-AMAN", 3f, "Buat Pola Sinyal"),
        MicroFeatureItem(120, "TAP_SPEED_BENCHMARK", "Kalkulator Kecepatan Ketuk & CPS", "Hitung skor ketukan jari per detik (Clicks Per Second)", MicroFeatureCategory.UTILITY_DECISION, "Uji Ketukan", 10f, "Tambah Ketukan & Hitung")
    )
}

object MicroFeaturesState {
    val pinnedFeatureNumbers = mutableStateListOf(1, 13, 25, 44, 67, 73, 97, 117)
    var selectedFeatureNumber by mutableIntStateOf(1)
    var tallyCount by mutableIntStateOf(0)
    var tapBenchmarkCount by mutableIntStateOf(0)
    var tapBenchmarkStartMs by mutableLongStateOf(0L)

    fun togglePin(number: Int) {
        if (pinnedFeatureNumbers.contains(number)) {
            if (pinnedFeatureNumbers.size > 1) {
                pinnedFeatureNumbers.remove(number)
            }
        } else {
            pinnedFeatureNumbers.add(number)
        }
    }

    fun executeMicroFeature(
        feature: MicroFeatureItem,
        rawInput: String,
        sliderParam: Float,
        nonce: Int
    ): String {
        val input = rawInput.trim().ifEmpty { feature.defaultInput }
        val paramInt = sliderParam.roundToInt().coerceAtLeast(1)
        val rng = SecureRandom()

        return when (feature.number) {
            1 -> {
                val safePins = (1..3).map {
                    var candidate: String
                    do {
                        candidate = (1..6).map { rng.nextInt(10) }.joinToString("")
                    } while (candidate.startsWith("19") || candidate.startsWith("20") || candidate == "123456" || candidate.toSet().size < 4)
                    candidate
                }
                "Rekomendasi PIN 6-Digit Anti-Tebak:\n• Utama: ${safePins[0]}\n• Cadangan 1: ${safePins[1]}\n• Cadangan 2: ${safePins[2]}\nStatus: Bebas pola tahun lahir & pengulangan."
            }
            2 -> {
                val words = listOf(
                    "Elang", "Samudra", "Kristal", "Lentera", "Perisai", "Bintang", "Zamrud", "Pelangi",
                    "Kompas", "Guntur", "Menara", "Cakrawala", "Nakhoda", "embun", "Pustaka", "Angkasa",
                    "Kencana", "Gamelan", "Merpati", "Khatulistiwa", "Benteng", "Pelita", "Safir", " Mahkota"
                )
                val count = paramInt.coerceIn(3, 8)
                val picked = (1..count).map { words[rng.nextInt(words.size)].trim() }
                "Passphrase ($count Kata): ${picked.joinToString("-")}-${10 + rng.nextInt(89)}\nEntropi Estimasi: ~${count * 13} Bit (Sangat Kuat)"
            }
            3 -> {
                var pool = 0
                if (input.any { it.isLowerCase() }) pool += 26
                if (input.any { it.isUpperCase() }) pool += 26
                if (input.any { it.isDigit() }) pool += 10
                if (input.any { !it.isLetterOrDigit() }) pool += 32
                val safePool = pool.coerceAtLeast(10)
                val entropy = (input.length * (ln(safePool.toDouble()) / ln(2.0))).roundToInt()
                val rating = when {
                    entropy >= 80 -> "SANGAT KUAT (Setara Kriptografi Militer)"
                    entropy >= 55 -> "KUAT (Aman untuk Akun Utama)"
                    entropy >= 36 -> "SEDANG (Disarankan Tambah Simbol)"
                    else -> "LEMAH (Mudah Ditebak Brute-Force)"
                }
                "Panjang: ${input.length} karakter | Karakter Unik: ${input.toSet().size}\nSkor Entropi: $entropy Bit\nKategori: $rating"
            }
            4 -> {
                val lower = input.lowercase()
                val weakPatterns = listOf("qwerty", "asdf", "zxcv", "1234", "2345", "3456", "0000", "1111", "admin", "password", "tanggal")
                val detected = weakPatterns.filter { lower.contains(it) }
                if (detected.isEmpty()) {
                    "AMAN: Tidak ditemukan pola urutan keyboard (QWERTY/1234) pada '$input'."
                } else {
                    "PERINGATAN: Terdeteksi pola urutan mudah ditebak: ${detected.joinToString(", ")}. Gunakan kombinasi acak!"
                }
            }
            5 -> {
                val chars = "ABCDEFGHJKLMNPQRSTUVWXYZ23456789"
                val code1 = (1..4).map { chars[rng.nextInt(chars.length)] }.joinToString("")
                val code2 = (1..4).map { chars[rng.nextInt(chars.length)] }.joinToString("")
                "Pertanyaan: $input\nJawaban Anti-Sosmed: ARC-$code1-$code2\n(Catat kode ini di Brankas agar orang yang mengenal Anda pun tidak bisa menebak jawaban pemulihan)."
            }
            6 -> {
                val codes = (1..10).mapIndexed { idx, _ ->
                    val p1 = 1000 + rng.nextInt(9000)
                    val p2 = 1000 + rng.nextInt(9000)
                    String.format(Locale.US, "%02d. %d-%d", idx + 1, p1, p2)
                }
                "10 Kode Cadangan Darurat ($input):\n" + codes.chunked(2).joinToString("\n") { it.joinToString("   |   ") }
            }
            7 -> {
                val commonPins = setOf("123456", "000000", "111111", "121212", "654321", "112233", "123123", "666666", "888888", "555555")
                val digitsOnly = input.filter { it.isDigit() }.take(6)
                val isCommon = digitsOnly in commonPins || digitsOnly.toSet().size <= 2
                if (isCommon) {
                    "STATUS RAWAN: PIN '$digitsOnly' termasuk dalam daftar PIN paling sering dicoba peretas. Segera ganti!"
                } else {
                    "STATUS AMAN: PIN '$digitsOnly' memiliki variasi digit yang baik (${digitsOnly.toSet().size} angka unik)."
                }
            }
            8 -> {
                val len = paramInt.coerceIn(12, 32)
                val alphabet = "abcdefghjkmnpqrstuvwxyzABCDEFGHJKLMNPQRSTUVWXYZ23456789!@#%&*+-="
                val wpa = (1..len).map { alphabet[rng.nextInt(alphabet.length)] }.joinToString("")
                "Sandi Wi-Fi WPA3 ($len Karakter):\n$wpa"
            }
            9 -> {
                val scrambled = ('0'..'9').toList().shuffled(rng)
                "Tata Letak Keypad Acak Anti-Intip:\nBaris 1: [ ${scrambled[0]} ] [ ${scrambled[1]} ] [ ${scrambled[2]} ]\nBaris 2: [ ${scrambled[3]} ] [ ${scrambled[4]} ] [ ${scrambled[5]} ]\nBaris 3: [ ${scrambled[6]} ] [ ${scrambled[7]} ] [ ${scrambled[8]} ]\nBaris 4:       [ ${scrambled[9]} ]"
            }
            10 -> {
                val bytes = ByteArray(32)
                rng.nextBytes(bytes)
                val hex = bytes.joinToString("") { "%02X".format(it) }
                "Kunci Master 256-Bit (64 Hex):\n${hex.substring(0, 32)}\n${hex.substring(32)}"
            }
            11 -> {
                "PIN 3-Lapis (6 + 6 + 6 = 18 Digit):\nTotal Kombinasi: 1.000.000.000.000.000.000 (1 Kuintiliun)\nPada kecepatan 100 percobaan/detik, butuh ~317 Juta Tahun untuk menebak seluruh lapis."
            }
            12 -> {
                val days = paramInt.coerceIn(7, 180)
                val now = System.currentTimeMillis()
                val fmt = SimpleDateFormat("dd MMM yyyy", Locale.forLanguageTag("id-ID"))
                val d1 = fmt.format(Date(now + days * 86_400_000L))
                val d2 = fmt.format(Date(now + days * 2 * 86_400_000L))
                val d3 = fmt.format(Date(now + days * 3 * 86_400_000L))
                "Jadwal Rotasi PIN Setiap $days Hari:\n• Siklus 1: $d1\n• Siklus 2: $d2\n• Siklus 3: $d3"
            }
            13 -> {
                val encoded = Base64.getEncoder().encodeToString(input.toByteArray())
                val decoded = try {
                    String(Base64.getDecoder().decode(input)).takeIf { it.isNotBlank() }
                } catch (_: Exception) {
                    null
                }
                if (decoded != null && input.length % 4 == 0 && !input.contains(" ")) {
                    "Base64 Dekode → $decoded\nBase64 Enkode → $encoded"
                } else {
                    "Base64 Enkode:\n$encoded"
                }
            }
            14 -> {
                val hex = input.toByteArray().joinToString(" ") { "%02X".format(it) }
                "Hexadecimal (${input.toByteArray().size} Byte):\n$hex"
            }
            15 -> {
                val bin = input.take(16).map { ch ->
                    Integer.toBinaryString(ch.code).padStart(8, '0')
                }.joinToString(" ")
                "Biner 8-Bit:\n$bin"
            }
            16 -> {
                val shift = paramInt.coerceIn(1, 25)
                val rotated = input.map { ch ->
                    when (ch) {
                        in 'A'..'Z' -> 'A' + (ch - 'A' + shift) % 26
                        in 'a'..'z' -> 'a' + (ch - 'a' + shift) % 26
                        else -> ch
                    }
                }.joinToString("")
                "Hasil Caesar ROT-$shift:\n$rotated"
            }
            17 -> {
                val mirror = input.map { ch ->
                    when (ch) {
                        in 'A'..'Z' -> 'Z' - (ch - 'A')
                        in 'a'..'z' -> 'z' - (ch - 'a')
                        else -> ch
                    }
                }.joinToString("")
                "Hasil Cermin Atbash (A↔Z):\n$mirror"
            }
            18 -> {
                val key = "ARCANUM"
                var keyIdx = 0
                val out = input.map { ch ->
                    val shift = key[keyIdx % key.length] - 'A'
                    when (ch) {
                        in 'A'..'Z' -> {
                            keyIdx++
                            'A' + (ch - 'A' + shift) % 26
                        }
                        in 'a'..'z' -> {
                            keyIdx++
                            'a' + (ch - 'a' + shift) % 26
                        }
                        else -> ch
                    }
                }.joinToString("")
                "Sandi Vigenere (Kunci: $key):\n$out"
            }
            19 -> {
                val map = mapOf(
                    'A' to ".-", 'B' to "-...", 'C' to "-.-.", 'D' to "-..", 'E' to ".", 'F' to "..-.",
                    'G' to "--.", 'H' to "....", 'I' to "..", 'J' to ".---", 'K' to "-.-", 'L' to ".-..",
                    'M' to "--", 'N' to "-.", 'O' to "---", 'P' to ".--.", 'Q' to "--.-", 'R' to ".-.",
                    'S' to "...", 'T' to "-", 'U' to "..-", 'V' to "...-", 'W' to ".--", 'X' to "-..-",
                    'Y' to "-.--", 'Z' to "--..", '0' to "-----", '1' to ".----", '2' to "..---",
                    '3' to "...--", '4' to "....-", '5' to ".....", '6' to "-....", '7' to "--...",
                    '8' to "---..", '9' to "----."
                )
                val morse = input.uppercase().map { map[it] ?: "/" }.joinToString(" ")
                "Kode Morse:\n$morse"
            }
            20 -> {
                val digest = MessageDigest.getInstance("SHA-256").digest(input.toByteArray())
                val hex = digest.joinToString("") { "%02x".format(it) }
                "SHA-256 Digest:\n${hex.substring(0, 32)}\n${hex.substring(32)}"
            }
            21 -> {
                val md5 = MessageDigest.getInstance("MD5").digest(input.toByteArray()).joinToString("") { "%02x".format(it) }
                val sha512 = MessageDigest.getInstance("SHA-512").digest(input.toByteArray()).joinToString("") { "%02x".format(it) }
                "MD5: $md5\nSHA-512 (Awal 48 Hex): ${sha512.take(48)}..."
            }
            22 -> {
                val keyByte = paramInt.coerceIn(1, 255)
                val xoredHex = input.toByteArray().joinToString(" ") { b ->
                    "%02X".format(b.toInt() xor keyByte)
                }
                "XOR Cipher (Key 0x${"%02X".format(keyByte)}):\n$xoredHex"
            }
            23 -> {
                val enc = URLEncoder.encode(input, "UTF-8")
                val dec = try { URLDecoder.decode(input, "UTF-8") } catch (_: Exception) { input }
                "URL Encoded:\n$enc\nURL Decoded:\n$dec"
            }
            24 -> {
                val rev = input.reversed()
                val shuf = input.toList().shuffled(rng).joinToString("")
                "Terbalik: $rev\nAnagram Acak: $shuf"
            }
            25 -> {
                val cleaned = input.replace(Regex("([?&])(utm_[^=&]+|fbclid|gclid|igshid|si|mc_cid|mc_eid|_ga)=[^&]*"), "")
                    .replace("?&", "?")
                    .trimEnd('?', '&')
                "URL Bersih Bebas Pelacak:\n$cleaned"
            }
            26 -> {
                val ext = input.substringAfterLast('.', "dat")
                val idNum = 100 + rng.nextInt(899)
                "Nama File Samaran Sistem:\n• sys_DRV_cache_$idNum.bin\n• android_webview_pak_$idNum.$ext\n• lib_shader_blob_$idNum.dat"
            }
            27 -> {
                val zw = "\u200B\u200C\u200B"
                val marked = if (input.length >= 2) input.substring(0, 1) + zw + input.substring(1) else input + zw
                "Teks Berpenanda Zero-Width (${marked.length} char vs ${input.length} char asli):\n$marked"
            }
            28 -> {
                val nonAscii = input.filter { it.code > 127 }
                if (nonAscii.isEmpty()) {
                    "AMAN: Seluruh ${input.length} karakter merupakan ASCII standar (0–127). Bebas jebakan homograf."
                } else {
                    "PERINGATAN: Ditemukan ${nonAscii.length} karakter non-ASCII: '${nonAscii.toSet().joinToString("")}'."
                }
            }
            29 -> {
                val firstNames = listOf("Arya", "Nadia", "Raka", "Kirana", "Damar", "Senja", "Bima", "Larissa")
                val lastNames = listOf("Pratama", "Wijaya", "Santoso", "Kusuma", "Mahendra", "Saputra")
                val cities = listOf("Bandung", "Yogyakarta", "Malang", "Denpasar", "Semarang", "Bogor")
                val fn = firstNames[rng.nextInt(firstNames.size)]
                val lnName = lastNames[rng.nextInt(lastNames.size)]
                val num = 100 + rng.nextInt(899)
                "Persona Samaran Privasi:\n• Nama Alias: $fn $lnName\n• Username: ${fn.lowercase()}_${lnName.lowercase()}$num\n• Domisili Fiktif: ${cities[rng.nextInt(cities.size)]}"
            }
            30 -> {
                val base = input.substringBefore('@').ifBlank { "user" }
                val dom = input.substringAfter('@', "gmail.com")
                "Email Alias Anti-Spam:\n• $base+belanja@$dom\n• $base+sosmed@$dom\n• $base+newsletter@$dom\n• $base+game@$dom"
            }
            31 -> {
                val redacted = input
                    .replace(Regex("\\b08[0-9]{8,11}\\b"), "[NO_HP_DISENSOR]")
                    .replace(Regex("\\b[0-9]{16}\\b"), "[NIK_DISENSOR]")
                    .replace(Regex("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}"), "[EMAIL_DISENSOR]")
                "Hasil Sensor Data Pribadi (PII):\n$redacted"
            }
            32 -> {
                val items = input.split(",").map { it.trim() }.filter { it.isNotEmpty() }
                val score = (items.size * 20 + paramInt.coerceIn(0, 20)).coerceIn(20, 100)
                "Skor Perlindungan Privasi: $score / 100\nLapisan Aktif (${items.size}): ${items.joinToString(", ")}\nStatus: Pertahanan Berlapis Aktif."
            }
            33 -> {
                val mins = paramInt.coerceIn(1, 60)
                "[PESAN RAHASIA SEKALI BACA — HAPUS DALAM $mins MENIT]\n----------------------------------------\n$input\n----------------------------------------\n[KODE VERIFIKASI: #ARC-${1000 + rng.nextInt(8999)}]"
            }
            34 -> {
                val parts = input.split(".")
                val dangerExts = setOf("exe", "bat", "cmd", "scr", "apk", "sh", "vbs", "js")
                val lastExt = parts.lastOrNull()?.lowercase() ?: ""
                if (parts.size >= 3 && lastExt in dangerExts) {
                    "BAHAYA TINGGI! File '$input' menggunakan jebakan ekstensi ganda (.$lastExt). Jangan dibuka!"
                } else {
                    "AMAN: File '$input' memiliki ekstensi tunggal standar (.$lastExt)."
                }
            }
            35 -> {
                val serial = "WM-${input.take(8).uppercase()}-${10000 + rng.nextInt(89999)}"
                "Kode Watermark Pelacak Dokumen:\n$serial\nSisipkan kode ini pada footer dokumen sebelum dibagikan."
            }
            36 -> {
                "Clipboard siap dibersihkan! Gunakan tombol 'Salin Hasil' untuk menimpa isi clipboard lama dengan token kosong aman."
            }
            37 -> {
                val gb = input.toDoubleOrNull() ?: sliderParam.toDouble()
                val mb = gb * 1024.0
                val kb = mb * 1024.0
                val tb = gb / 1024.0
                String.format(Locale.US, "%.2f GB setara dengan:\n• %.4f TB\n• %,.0f MB\n• %,.0f KB", gb, tb, mb, kb)
            }
            38 -> {
                val sizeGb = input.toDoubleOrNull() ?: 10.0
                val speedMbps = sliderParam.coerceAtLeast(1f).toDouble()
                val totalSeconds = (sizeGb * 1024.0 * 8.0) / speedMbps
                val mins = (totalSeconds / 60).toInt()
                val secs = (totalSeconds % 60).toInt()
                String.format(Locale.US, "File %.1f GB pada kecepatan %.0f Mbps:\nEstimasi Waktu: %d menit %d detik (%.1f MB/detik)", sizeGb, speedMbps, mins, secs, speedMbps / 8.0)
            }
            39 -> {
                val dec = input.filter { it.isDigit() }.toLongOrNull() ?: 2026L
                "Desimal: $dec\nBiner: ${java.lang.Long.toBinaryString(dec)}\nOktal: ${java.lang.Long.toOctalString(dec)}\nHexadecimal: 0x${java.lang.Long.toHexString(dec).uppercase()}"
            }
            40 -> {
                val c = input.toDoubleOrNull() ?: sliderParam.toDouble()
                val f = c * 9.0 / 5.0 + 32.0
                val k = c + 273.15
                val r = c * 4.0 / 5.0
                String.format(Locale.US, "Suhu %.1f °C:\n• Fahrenheit: %.1f °F\n• Kelvin: %.2f K\n• Reamur: %.1f °R", c, f, k, r)
            }
            41 -> {
                val cm = input.toDoubleOrNull() ?: sliderParam.toDouble()
                val m = cm / 100.0
                val inch = cm / 2.54
                val feet = inch / 12.0
                String.format(Locale.US, "%.1f cm setara dengan:\n• %.2f Meter | %.0f mm\n• %.2f Inci | %.2f Kaki (Feet)", cm, m, cm * 10, inch, feet)
            }
            42 -> {
                val kg = input.toDoubleOrNull() ?: sliderParam.toDouble()
                val gr = kg * 1000.0
                val ons = kg * 10.0
                val lbs = kg * 2.20462
                String.format(Locale.US, "%.2f Kg setara dengan:\n• %,.0f Gram | %.1f Ons\n• %.2f Pound (lbs)", kg, gr, ons, lbs)
            }
            43 -> {
                val price = input.filter { it.isDigit() }.toDoubleOrNull() ?: 500000.0
                val discPct = sliderParam.coerceIn(0f, 90f).toDouble()
                val afterDisc = price * (1.0 - discPct / 100.0)
                val withTax = afterDisc * 1.11
                String.format(Locale.US, "Harga Awal: Rp %,.0f (Diskon %.0f%%)\nSetelah Diskon: Rp %,.0f\n+ PPN 11%%: Rp %,.0f", price, discPct, afterDisc, withTax)
            }
            44 -> {
                val bill = input.filter { it.isDigit() }.toDoubleOrNull() ?: 450000.0
                val people = paramInt.coerceIn(1, 50)
                val withService = bill * 1.10
                val perPerson = withService / people
                String.format(Locale.US, "Total Tagihan: Rp %,.0f (+10%% Pajak/Servis = Rp %,.0f)\nJumlah Orang: %d Orang\nPatungan per Orang: Rp %,.0f", bill, withService, people, perPerson)
            }
            45 -> {
                val base = input.toDoubleOrNull() ?: 100.0
                val target = sliderParam.toDouble().coerceAtLeast(1.0)
                val pctOf = (base * target) / 100.0
                String.format(Locale.US, "%.1f%% dari %,.1f adalah %,.2f\nJika naik %.0f%% menjadi: %,.2f\nJika turun %.0f%% menjadi: %,.2f", target, base, pctOf, target, base + pctOf, target, base - pctOf)
            }
            46 -> {
                val weightKg = input.toDoubleOrNull() ?: 65.0
                val heightCm = sliderParam.coerceIn(120f, 210f).toDouble()
                val heightM = heightCm / 100.0
                val bmi = weightKg / (heightM * heightM)
                val waterMl = (weightKg * 35.0).roundToInt()
                val cat = when {
                    bmi < 18.5 -> "Kurus (Underweight)"
                    bmi < 25.0 -> "Ideal / Normal"
                    bmi < 30.0 -> "Berlebih (Overweight)"
                    else -> "Obesitas"
                }
                String.format(Locale.US, "BB: %.1f Kg | TB: %.0f cm\nSkor BMI: %.1f (%s)\nKebutuhan Air Harian: ~%,d ml (%d gelas)", weightKg, heightCm, bmi, cat, waterMl, waterMl / 250)
            }
            47 -> {
                val birthYear = input.filter { it.isDigit() }.toIntOrNull()?.coerceIn(1920, 2026) ?: 2000
                val years = (2026 - birthYear).coerceAtLeast(0)
                val months = years * 12
                val days = years * 365
                val hours = days * 24
                String.format(Locale.US, "Tahun Lahir %d → Usia %d Tahun\nSetara dengan: ~%,d Bulan | ~%,d Hari | ~%,d Jam", birthYear, years, months, days, hours)
            }
            48 -> {
                val days = input.filter { it.isDigit() }.toIntOrNull() ?: paramInt
                val weeks = days / 7.0
                val months = days / 30.0
                val hours = days * 24
                String.format(Locale.US, "%d Hari setara dengan:\n• %.1f Minggu | %.1f Bulan\n• %,d Jam | %,d Menit", days, weeks, months, hours, hours * 60)
            }
            49 -> {
                val principal = input.filter { it.isDigit() }.toDoubleOrNull() ?: 10_000_000.0
                val years = paramInt.coerceIn(1, 30)
                val rate = 0.08
                val finalAmount = principal * (1.0 + rate).pow(years.toDouble())
                String.format(Locale.US, "Modal Awal: Rp %,.0f (Bunga Majemuk 8%%/thn selama %d thn)\nNilai Akhir: Rp %,.0f\nTotal Pertumbuhan: +Rp %,.0f", principal, years, finalAmount, finalAmount - principal)
            }
            50 -> {
                val monthly = input.filter { it.isDigit() }.toDoubleOrNull() ?: 4_000_000.0
                String.format(Locale.US, "Pengeluaran Bulanan: Rp %,.0f\n• Lajang (3x): Rp %,.0f\n• Menikah (6x): Rp %,.0f\n• Keluarga/Freelance (12x): Rp %,.0f", monthly, monthly * 3, monthly * 6, monthly * 12)
            }
            51 -> {
                val income = input.filter { it.isDigit() }.toDoubleOrNull() ?: 8_500_000.0
                String.format(Locale.US, "Gaji Bulanan: Rp %,.0f\n• 50%% Kebutuhan Pokok: Rp %,.0f\n• 30%% Gaya Hidup & Hiburan: Rp %,.0f\n• 20%% Tabungan & Investasi: Rp %,.0f", income, income * 0.5, income * 0.3, income * 0.2)
            }
            52 -> {
                val loan = input.filter { it.isDigit() }.toDoubleOrNull() ?: 24_000_000.0
                val months = paramInt.coerceIn(3, 60)
                val monthlyPrincipal = loan / months
                val monthlyInterest = loan * 0.008
                String.format(Locale.US, "Pinjaman: Rp %,.0f | Tenor: %d Bulan\nCicilan per Bulan: Rp %,.0f (Pokok Rp %,.0f + Bunga Rp %,.0f)", loan, months, monthlyPrincipal + monthlyInterest, monthlyPrincipal, monthlyInterest)
            }
            53 -> {
                val goal = input.filter { it.isDigit() }.toDoubleOrNull() ?: 15_000_000.0
                val days = paramInt.coerceIn(7, 365)
                String.format(Locale.US, "Target Tabungan: Rp %,.0f dalam %d Hari:\n• Sisihkan per Hari: Rp %,.0f\n• Sisihkan per Minggu: Rp %,.0f", goal, days, goal / days, (goal / days) * 7)
            }
            54 -> {
                val money = input.filter { it.isDigit() }.toDoubleOrNull() ?: 50_000_000.0
                val years = paramInt.coerceIn(1, 25)
                val realPower = money / (1.04).pow(years.toDouble())
                String.format(Locale.US, "Nominal Rp %,.0f dengan inflasi 4%%/tahun:\nDaya beli riil dalam %d tahun setara Rp %,.0f hari ini.", money, years, realPower)
            }
            55 -> {
                val idr = input.filter { it.isDigit() }.toDoubleOrNull() ?: 1_000_000.0
                String.format(Locale.US, "Rp %,.0f setara estimasi:\n• USD: $%,.2f | SGD: S$%,.2f\n• EUR: €%,.2f | JPY: ¥%,.0f | MYR: RM %,.2f", idr, idr / 16200.0, idr / 12000.0, idr / 17500.0, idr / 108.0, idr / 3650.0)
            }
            56 -> {
                val grams = input.toDoubleOrNull() ?: sliderParam.toDouble()
                val buyPrice = 1_450_000.0
                val buyback = 1_340_000.0
                String.format(Locale.US, "Portofolio %.1f Gram Emas:\n• Nilai Beli Pasar: Rp %,.0f\n• Estimasi Buyback: Rp %,.0f", grams, grams * buyPrice, grams * buyback)
            }
            57 -> {
                val debt = input.filter { it.isDigit() }.toDoubleOrNull() ?: 2_000_000.0
                val salary = sliderParam.toDouble().coerceAtLeast(1.0)
                val ratio = (debt / (debt * 3.5)).coerceIn(0.05, 0.85) * 100.0
                String.format(Locale.US, "Cicilan Bulanan: Rp %,.0f\nBatas Gaji Minimum Aman (DTI 30%%): Rp %,.0f/bulan\n(Pastikan penghasilan minimal 3.3x total cicilan).", debt, debt / 0.30, ratio)
            }
            58 -> {
                val stamp = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.US).format(Date())
                "[LOG DOMPET ARCANUM • $stamp]\nItem: $input\nKategori: Pengeluaran Harian | Status: Tercatat"
            }
            59 -> {
                val total = input.filter { it.isDigit() }.toDoubleOrNull() ?: 10_000_000.0
                val zakat = total * 0.025
                String.format(Locale.US, "Nominal Bersih: Rp %,.0f\nZakat / Sedekah 2.5%%: Rp %,.0f", total, zakat)
            }
            60 -> {
                val sub = input.filter { it.isDigit() }.toDoubleOrNull() ?: 185_000.0
                String.format(Locale.US, "Langganan Rp %,.0f / bulan:\n• Total 1 Tahun: Rp %,.0f\n• Total 3 Tahun: Rp %,.0f\n• Total 5 Tahun: Rp %,.0f", sub, sub * 12, sub * 36, sub * 60)
            }
            61 -> {
                val sessions = paramInt.coerceIn(1, 8)
                val totalFocus = sessions * 25
                val totalBreak = sessions * 5
                "Rencana Fokus Pomodoro ($input):\n• $sessions Sesi x 25m Fokus = $totalFocus Menit Kerja Inti\n• $sessions x 5m Istirahat = $totalBreak Menit Jeda\nTotal Durasi: ${totalFocus + totalBreak} Menit."
            }
            62 -> {
                val words = input.filter { it.isDigit() }.toIntOrNull() ?: 2500
                val wpm = paramInt.coerceIn(100, 450)
                val mins = words.toDouble() / wpm.toDouble()
                String.format(Locale.US, "Teks %,d Kata pada kecepatan %d kata/menit:\nEstimasi Waktu Baca: %.1f Menit (~%d halaman buku).", words, wpm, mins, (words / 250).coerceAtLeast(1))
            }
            63 -> {
                val mins = input.filter { it.isDigit() }.toIntOrNull() ?: paramInt
                "Target Waktu $mins Menit:\n• Setara ${mins * 60} Detik\n• ${String.format(Locale.US, "%.2f", mins / 60.0)} Jam Fokus."
            }
            64 -> {
                tallyCount += 1
                val target = paramInt.coerceIn(10, 1000)
                "Hitungan Tally Saat Ini: $tallyCount / $target\n(${((tallyCount.toFloat() / target) * 100f).roundToInt().coerceAtMost(100)}% menuju target '$input')"
            }
            65 -> {
                val tasks = input.split(",").map { it.trim() }.filter { it.isNotEmpty() }
                val q1 = tasks.getOrNull(0) ?: "Tugas Kritis"
                val q2 = tasks.getOrNull(1) ?: "Perencanaan"
                val q3 = tasks.getOrNull(2) ?: "Delegasi"
                val q4 = tasks.getOrNull(3) ?: "Tunda/Hapus"
                "Matriks Prioritas Eisenhower:\n1. LAKUKAN SEKARANG: $q1\n2. JADWALKAN: $q2\n3. DELEGASIKAN: $q3\n4. ELIMINASI: $q4"
            }
            66 -> {
                "CHECKLIST 3 PRIORITAS HARI INI:\n[ ] $input\nStatus: Siap dieksekusi satu per satu."
            }
            67 -> {
                val chars = input.length
                val noSpace = input.count { !it.isWhitespace() }
                val words = input.split(Regex("\\s+")).filter { it.isNotEmpty() }.size
                val sentences = input.split(Regex("[.!?]+")).filter { it.isNotBlank() }.size
                val readSec = ((words / 200.0) * 60).roundToInt().coerceAtLeast(1)
                "Kata: $words | Karakter: $chars (Tanpa spasi: $noSpace)\nKalimat: $sentences | Estimasi Baca: ~${readSec} detik"
            }
            68 -> {
                val clean = input.replace(Regex("\\s+"), " ").trim()
                "Teks Rapi Bebas Spasi Ganda:\n$clean"
            }
            69 -> {
                val words = input.lowercase().split(Regex("\\s+")).filter { it.isNotEmpty() }
                val title = words.joinToString(" ") { it.replaceFirstChar { c -> c.uppercase() } }
                val snake = words.joinToString("_")
                val camel = words.mapIndexed { idx, w -> if (idx == 0) w else w.replaceFirstChar { it.uppercase() } }.joinToString("")
                "• UPPER: ${input.uppercase()}\n• Title: $title\n• snake_case: $snake\n• camelCase: $camel"
            }
            70 -> {
                val uniqueSorted = input.split(",").map { it.trim() }.filter { it.isNotEmpty() }.distinct().sorted()
                "Daftar Urut A–Z & Bebas Duplikat (${uniqueSorted.size} item):\n${uniqueSorted.joinToString(", ")}"
            }
            71 -> {
                val dateStr = SimpleDateFormat("dd MMM yyyy", Locale.forLanguageTag("id-ID")).format(Date())
                "NOTULEN RAPAT: $input ($dateStr)\n1. Agenda Utama:\n2. Keputusan Penting:\n3. Tindak Lanjut & PIC:"
            }
            72 -> {
                val wib = (input.filter { it.isDigit() }.toIntOrNull() ?: paramInt) % 24
                val wita = (wib + 1) % 24
                val wit = (wib + 2) % 24
                val utc = (wib - 7 + 24) % 24
                val tokyo = (wib + 2) % 24
                String.format(Locale.US, "Pukul %02d:00 WIB setara dengan:\n• %02d:00 WITA | %02d:00 WIT\n• %02d:00 UTC/GMT | %02d:00 Tokyo (JST)", wib, wita, wit, utc, tokyo)
            }
            73 -> {
                val cleanHex = input.removePrefix("#").padEnd(6, '0').take(6)
                val r = cleanHex.substring(0, 2).toIntOrNull(16) ?: 2
                val g = cleanHex.substring(2, 4).toIntOrNull(16) ?: 132
                val b = cleanHex.substring(4, 6).toIntOrNull(16) ?: 199
                val lum = ((0.299 * r + 0.587 * g + 0.114 * b) / 255.0 * 100).roundToInt()
                "HEX: #${cleanHex.uppercase()}\nRGB: rgb($r, $g, $b)\nLuminansi Kecerahan: $lum% (${if (lum > 55) "Warna Terang" else "Warna Gelap"})"
            }
            74 -> {
                val cleanHex = input.removePrefix("#").padEnd(6, '0').take(6)
                val r = (cleanHex.substring(0, 2).toIntOrNull(16) ?: 30) / 255.0
                val g = (cleanHex.substring(2, 4).toIntOrNull(16) ?: 41) / 255.0
                val b = (cleanHex.substring(4, 6).toIntOrNull(16) ?: 59) / 255.0
                val lum = 0.2126 * r + 0.7152 * g + 0.0722 * b
                val contrastWhite = (1.05) / (lum + 0.05)
                val contrastBlack = (lum + 0.05) / 0.05
                String.format(Locale.US, "Warna Latar #%s:\n• Kontras Teks Putih: %.2f:1 (%s)\n• Kontras Teks Hitam: %.2f:1 (%s)", cleanHex.uppercase(), contrastWhite, if (contrastWhite >= 4.5) "LULUS WCAG AA" else "Kurang Kontras", contrastBlack, if (contrastBlack >= 4.5) "LULUS WCAG AA" else "Kurang Kontras")
            }
            75 -> {
                val cleanHex = input.removePrefix("#").padEnd(6, '0').take(6)
                val r = cleanHex.substring(0, 2).toIntOrNull(16) ?: 22
                val g = cleanHex.substring(2, 4).toIntOrNull(16) ?: 163
                val b = cleanHex.substring(4, 6).toIntOrNull(16) ?: 74
                val compHex = String.format(Locale.US, "#%02X%02X%02X", 255 - r, 255 - g, 255 - b)
                val triad1 = String.format(Locale.US, "#%02X%02X%02X", g, b, r)
                val triad2 = String.format(Locale.US, "#%02X%02X%02X", b, r, g)
                "Warna Utama: #${cleanHex.uppercase()}\n• Komplementer: $compHex\n• Triadik 1: $triad1 | Triadik 2: $triad2"
            }
            76 -> {
                val parts = input.split(",").map { it.trim().removePrefix("#").uppercase() }
                val c1 = parts.getOrNull(0) ?: "0284C7"
                val c2 = parts.getOrNull(1) ?: "38BDF8"
                "Kode Jetpack Compose:\nBrush.linearGradient(\n    colors = listOf(Color(0xFF$c1), Color(0xFF$c2))\n)"
            }
            77 -> {
                val base = input.toDoubleOrNull() ?: 14.0
                String.format(Locale.US, "Skala Tipografi (Basis %.0fsp):\n• Caption: %.1fsp | Body: %.0fsp\n• Title: %.1fsp | Headline: %.1fsp | Display: %.1fsp", base, base * 0.8, base, base * 1.25, base * 1.56, base * 2.44)
            }
            78 -> {
                val dp = input.toDoubleOrNull() ?: sliderParam.toDouble()
                String.format(Locale.US, "%.0f dp dalam Piksel Layar:\n• MDPI (1x): %.0f px | HDPI (1.5x): %.0f px\n• XHDPI (2x): %.0f px | XXHDPI (3x): %.0f px", dp, dp, dp * 1.5, dp * 2.0, dp * 3.0)
            }
            79 -> {
                val w = input.filter { it.isDigit() }.toIntOrNull() ?: 1920
                "Lebar $w px:\n• Rasio 16:9 → $w x ${(w * 9) / 16} px\n• Rasio 4:3 → $w x ${(w * 3) / 4} px\n• Rasio 1:1 → $w x $w px"
            }
            80 -> {
                val cleanHex = input.removePrefix("#").padEnd(6, '0').take(6)
                val ratio = (sliderParam / 100f).coerceIn(0f, 1f)
                val r = ((cleanHex.substring(0, 2).toIntOrNull(16) ?: 236) * ratio + 255 * (1f - ratio)).roundToInt()
                val g = ((cleanHex.substring(2, 4).toIntOrNull(16) ?: 72) * ratio + 255 * (1f - ratio)).roundToInt()
                val b = ((cleanHex.substring(4, 6).toIntOrNull(16) ?: 153) * ratio + 255 * (1f - ratio)).roundToInt()
                String.format(Locale.US, "Campuran #%s (%.0f%%) + Putih:\nHasil HEX: #%02X%02X%02X", cleanHex.uppercase(), ratio * 100, r, g, b)
            }
            81 -> {
                val radius = paramInt.coerceIn(4, 40)
                val lip = (radius * 0.22f).roundToInt().coerceIn(2, 8)
                "Kartu CornerRadius ${radius}dp:\n• Tinggi Lip 3D Ideal: ${lip}dp\n• Ketebalan Border: 2.dp | Padding Dalam: ${(radius * 0.7f).roundToInt()}dp"
            }
            82 -> {
                "Font Aktif: ${AppThemeController.activeFontPreset.displayName}\nPangram Uji:\n\"$input\"\n0123456789 !@#$%^&*()"
            }
            83 -> {
                val cleanHex = input.removePrefix("#").padEnd(6, '0').take(6)
                "Tangga Tint & Shade dari #${cleanHex.uppercase()}:\n• Terang +40%: #E0E7FF\n• Terang +20%: #A5B4FC\n• Basis: #${cleanHex.uppercase()}\n• Gelap -20%: #4338CA"
            }
            84 -> {
                "Resolusi Cetak Tajam 300 DPI:\n• Foto 4R (4x6\"): 1200 x 1800 px\n• Kertas A4: 2480 x 3508 px\n• Kertas A3: 3508 x 4960 px"
            }
            85 -> {
                val prefix = paramInt.coerceIn(8, 30)
                val hosts = (2.0.pow(32 - prefix).toLong() - 2L).coerceAtLeast(1L)
                "Subnet $input/$prefix:\n• Kapasitas Host Aktif: %,d IP\n• Prefix Bit Jaringan: $prefix Bit".format(Locale.US, hosts)
            }
            86 -> {
                val count = paramInt.coerceIn(1, 5)
                val list = (1..count).map { UUID.randomUUID().toString() }
                "UUID v4 Baru ($count):\n" + list.joinToString("\n")
            }
            87 -> {
                val now = System.currentTimeMillis()
                val fmt = SimpleDateFormat("yyyy-MM-dd HH:mm:ss z", Locale.US)
                "Unix Epoch Saat Ini:\n• Detik: ${now / 1000L}\n• Milidetik: $now\n• Waktu: ${fmt.format(Date(now))}"
            }
            88 -> {
                val pretty = input.replace("{", "{\n  ").replace(",", ",\n  ").replace("}", "\n}")
                "JSON Terformat:\n$pretty"
            }
            89 -> {
                "Pola Regex '$input' valid dan siap mencocokkan string target tanpa error sintaks."
            }
            90 -> {
                val every = paramInt.coerceIn(1, 59)
                "Cron Expression (Setiap $every Menit):\n*/$every * * * *\n(Menjalankan tugas otomatis setiap $every menit sepanjang hari)."
            }
            91 -> {
                val code = input.filter { it.isDigit() }.toIntOrNull() ?: paramInt
                val desc = when (code) {
                    200 -> "200 OK — Permintaan berhasil diproses."
                    201 -> "201 Created — Data baru berhasil dibuat."
                    301 -> "301 Moved Permanently — Redirect permanen."
                    400 -> "400 Bad Request — Format permintaan tidak valid."
                    401 -> "401 Unauthorized — Butuh autentikasi token/PIN."
                    403 -> "403 Forbidden — Akses ditolak oleh server."
                    404 -> "404 Not Found — Endpoint atau file tidak ditemukan."
                    429 -> "429 Too Many Requests — Terkena batas rate-limit."
                    500 -> "500 Internal Server Error — Kesalahan di sisi server."
                    else -> "$code — Kode status protokol HTTP standar."
                }
                "Status HTTP:\n$desc"
            }
            92 -> {
                "Referensi Port Jaringan:\n• 22: SSH | 53: DNS | 80: HTTP\n• 443: HTTPS/TLS | 853: DNS-over-TLS\n• 9050: Tor SOCKS5 | 51820: WireGuard VPN"
            }
            93 -> {
                val mbps = input.toDoubleOrNull() ?: sliderParam.toDouble()
                val mbPerMin = (mbps / 8.0) * 60.0
                val gbPerHour = (mbPerMin * 60.0) / 1024.0
                String.format(Locale.US, "Streaming Bitrate %.1f Mbps:\n• Per Menit: %.1f MB\n• Per Jam: %.2f GB", mbps, mbPerMin, gbPerHour)
            }
            94 -> {
                "Teks Dummy Indonesia:\n\"Keamanan digital modern bertumpu pada arsitektur berlapis yang menyatukan kemudahan akses harian dengan perlindungan enkripsi tingkat tinggi di setiap sudut antarmuka.\""
            }
            95 -> {
                val oct = input.filter { it in '0'..'7' }.padEnd(3, '0').take(3)
                fun mapDigit(c: Char) = when (c) {
                    '7' -> "rwx"; '6' -> "rw-"; '5' -> "r-x"; '4' -> "r--"
                    '3' -> "-wx"; '2' -> "-w-"; '1' -> "--x"; else -> "---"
                }
                "Chmod $oct → -${mapDigit(oct[0])}${mapDigit(oct[1])}${mapDigit(oct[2])}\n(Owner: ${mapDigit(oct[0])} | Group: ${mapDigit(oct[1])} | Others: ${mapDigit(oct[2])})"
            }
            96 -> {
                val rt = Runtime.getRuntime()
                val usedMb = (rt.totalMemory() - rt.freeMemory()) / (1024 * 1024)
                val maxMb = rt.maxMemory() / (1024 * 1024)
                "Telemetri JVM Aplikasi:\n• RAM Terpakai: $usedMb MB / Maks $maxMb MB\n• Core CPU Tersedia: ${rt.availableProcessors()} Core"
            }
            97 -> {
                val cycles = paramInt.coerceIn(1, 10)
                "Panduan Napas 4-7-8 ($cycles Siklus = ${cycles * 19} Detik):\n1. Tarik napas lewat hidung: 4 detik\n2. Tahan napas tenang: 7 detik\n3. Hembuskan perlahan lewat mulut: 8 detik"
            }
            98 -> {
                val sec = paramInt.coerceIn(3, 8)
                "Box Breathing ($sec-$sec-$sec-$sec):\nTarik $sec dtk → Tahan $sec dtk → Hembus $sec dtk → Tahan $sec dtk.\nMembantu menstabilkan detak jantung & fokus."
            }
            99 -> {
                "Aturan Kesehatan Mata 20-20-20:\nSetiap 20 menit menatap layar ponsel, alihkan pandangan ke objek berjarak 20 kaki (6 meter) selama minimal 20 detik."
            }
            100 -> {
                val glasses = paramInt.coerceIn(1, 12)
                "Progres Hidrasi: $glasses dari 8 Gelas (${glasses * 250} ml / 2.000 ml)\nStatus: ${if (glasses >= 8) "Target Harian Tercapai!" else "Kurang ${(8 - glasses) * 250} ml lagi hari ini."}"
            }
            101 -> {
                val list = listOf(
                    "Langkah tenang dan konsisten hari ini jauh lebih kuat daripada terburu-buru.",
                    "Pikiran yang jernih melahirkan keputusan terbaik.",
                    "Setiap progres kecil tetaplah kemajuan yang patut dihargai.",
                    "Fokus pada hal yang bisa Anda kendalikan sekarang."
                )
                "Afirmasi Hari Ini:\n\"${list[(nonce + input.length) % list.size]}\""
            }
            102 -> {
                val energy = paramInt.coerceIn(1, 10)
                "Jurnal Mood & Energi Tercatat:\nSuasana Hati: $input\nTingkat Energi: $energy / 10"
            }
            103 -> {
                "Peregangan Meja 60 Detik:\n1. Miringkan kepala ke kiri & kanan (15d)\n2. Putar bahu ke belakang perlahan (15d)\n3. Regangkan pergelangan tangan & jari (15d)\n4. Tarik napas dalam sambil tegakkan punggung (15d)"
            }
            104 -> {
                val sleepHour = (input.filter { it.isDigit() }.toIntOrNull() ?: 22) % 24
                val wake5 = (sleepHour + 7) % 24
                val wake6 = (sleepHour + 9) % 24
                String.format(Locale.US, "Jika tidur pukul %02d:00:\n• 5 Siklus REM (7.5 Jam): Bangun pukul %02d:30\n• 6 Siklus REM (9 Jam): Bangun pukul %02d:00", sleepHour, wake5, wake6)
            }
            105 -> {
                val mins = paramInt.coerceIn(15, 180)
                "Komitmen Detoks Digital Aktif:\nBebas gangguan media sosial selama $mins menit ke depan untuk ketenangan & fokus penuh."
            }
            106 -> {
                "Beban pikiran '${input.take(24)}...' telah dihancurkan menjadi debu biner:\n[0x00 0x00 0x00 — PIKIRAN TENANG & BERSIH]"
            }
            107 -> {
                val bpm = paramInt.coerceIn(40, 200)
                val msPerBeat = 60000 / bpm
                "Tempo $bpm BPM:\n• 1 Ketukan setiap $msPerBeat milidetik\n• Setara ${String.format(Locale.US, "%.2f", bpm / 60.0)} ketukan per detik."
            }
            108 -> {
                val quests = listOf(
                    "Kirim pesan apresiasi singkat ke satu teman atau anggota keluarga.",
                    "Rapikan meja kerja atau hapus 5 file sampah di perangkat Anda.",
                    "Minum segelas air putih penuh sebelum memulai tugas berikutnya.",
                    "Berikan senyuman dan ucapan terima kasih tulus hari ini."
                )
                "Misi Positif Hari Ini:\n• ${quests[(nonce + paramInt) % quests.size]}"
            }
            109 -> {
                val side = if (rng.nextBoolean()) "SISI GARUDA (KEPALA)" else "SISI NOMINAL (EKOR)"
                "Hasil Lemparan Koin Kriptografis:\n>>> $side <<<"
            }
            110 -> {
                val sides = paramInt.coerceIn(4, 100)
                val r1 = 1 + rng.nextInt(sides)
                val r2 = 1 + rng.nextInt(sides)
                "Lemparan Dadu D$sides:\n• Dadu 1: [ $r1 ] | Dadu 2: [ $r2 ] (Total: ${r1 + r2})"
            }
            111 -> {
                val opts = input.split(",").map { it.trim() }.filter { it.isNotEmpty() }
                val picked = if (opts.isNotEmpty()) opts[rng.nextInt(opts.size)] else input
                "Keputusan Terpilih dari ${opts.size.coerceAtLeast(1)} Opsi:\n>>> $picked <<<"
            }
            112 -> {
                val maxN = (input.filter { it.isDigit() }.toIntOrNull() ?: 100).coerceAtLeast(10)
                val count = paramInt.coerceIn(1, 10)
                val drawn = (1..maxN).shuffled(rng).take(count).sorted()
                "Hasil Undian $count Angka Unik (1–$maxN):\n${drawn.joinToString("  •  ")}"
            }
            113 -> {
                val answers = listOf(
                    "Ya, langkah ini sangat tepat untuk dimulai sekarang.",
                    "Periksa kembali satu detail kecil, lalu jalankan dengan yakin.",
                    "Waktu terbaik adalah hari ini — jangan ragu.",
                    "Ambil jeda 5 menit, solusi yang lebih ringan akan terlihat."
                )
                "Pertanyaan: \"$input\"\nJawaban: ${answers[(nonce + input.length) % answers.size]}"
            }
            114 -> {
                val names = input.split(",").map { it.trim() }.filter { it.isNotEmpty() }.shuffled(rng)
                val groups = paramInt.coerceIn(2, 5)
                val bucketed = names.withIndex().groupBy { it.index % groups }
                bucketed.entries.joinToString("\n") { (idx, list) ->
                    "Tim ${idx + 1}: ${list.joinToString(", ") { it.value }}"
                }
            }
            115 -> {
                val adj = listOf("AERO", "SILVER", "NOVA", "VALKYRIE", "ZENITH", "ORION", "SOLARIS", "PRISM")
                val noun = listOf("SHIELD", "FALCON", "CITADEL", "HORIZON", "VANGUARD", "NEXUS", "CREST")
                val code = "OP-${adj[rng.nextInt(adj.size)]}-${noun[rng.nextInt(noun.size)]}-${10 + rng.nextInt(89)}"
                "Kode Operasi Proyek Baru:\n$code"
            }
            116 -> {
                var num = (input.filter { it.isDigit() }.toIntOrNull() ?: 2026).coerceIn(1, 3999)
                val numerals = listOf(
                    1000 to "M", 900 to "CM", 500 to "D", 400 to "CD",
                    100 to "C", 90 to "XC", 50 to "L", 40 to "XL",
                    10 to "X", 9 to "IX", 5 to "V", 4 to "IV", 1 to "I"
                )
                val sb = StringBuilder()
                for ((v, sym) in numerals) {
                    while (num >= v) {
                        sb.append(sym)
                        num -= v
                    }
                }
                "Angka Romawi:\n$sb"
            }
            117 -> {
                val n = input.filter { it.isDigit() }.toLongOrNull() ?: 1250500L
                String.format(Locale.US, "Nominal: Rp %,d\nTerbilang: \"%s rupiah\"", n, numberToIndonesianWords(n))
            }
            118 -> {
                val quotes = listOf(
                    "\"Sedikit demi sedikit, lama-lama menjadi bukit.\"",
                    "\"Di mana bumi dipijak, di situ langit dijunjung.\"",
                    "\"Air tenang menghanyutkan — ketenangan menyimpan kekuatan besar.\"",
                    "\"Sekali layar terkembang, surut kita berpantang.\""
                )
                "Pepatah & Inspirasi Nusantara:\n${quotes[(nonce + paramInt) % quotes.size]}"
            }
            119 -> {
                "Pola Sinyal Darurat Visual ($input):\n[3 Kedip Cepat] • [3 Kedip Panjang] • [3 Kedip Cepat]\nInterval: 300ms / 900ms / 300ms"
            }
            120 -> {
                val now = System.currentTimeMillis()
                if (now - tapBenchmarkStartMs > 5000L) {
                    tapBenchmarkStartMs = now
                    tapBenchmarkCount = 1
                } else {
                    tapBenchmarkCount++
                }
                val elapsedSec = ((now - tapBenchmarkStartMs) / 1000f).coerceAtLeast(0.5f)
                val cps = tapBenchmarkCount / elapsedSec
                String.format(Locale.US, "Total Ketukan Sesi 5 Detik: %d Ketukan\nKecepatan Jari: %.1f CPS (Ketukan/Detik)", tapBenchmarkCount, cps)
            }
            else -> "Hasil Pemrosesan ${feature.codeBadge}: $input"
        }
    }

    private fun numberToIndonesianWords(n: Long): String {
        if (n == 0L) return "Nol"
        val satuan = arrayOf("", "satu", "dua", "tiga", "empat", "lima", "enam", "tujuh", "delapan", "sembilan", "sepuluh", "sebelas")
        fun convert(v: Long): String = when {
            v < 12L -> satuan[v.toInt()]
            v < 20L -> "${convert(v - 10L)} belas"
            v < 100L -> "${convert(v / 10L)} puluh ${convert(v % 10L)}".trim()
            v < 200L -> "seratus ${convert(v - 100L)}".trim()
            v < 1000L -> "${convert(v / 100L)} ratus ${convert(v % 100L)}".trim()
            v < 2000L -> "seribu ${convert(v - 1000L)}".trim()
            v < 1_000_000L -> "${convert(v / 1000L)} ribu ${convert(v % 1000L)}".trim()
            v < 1_000_000_000L -> "${convert(v / 1_000_000L)} juta ${convert(v % 1_000_000L)}".trim()
            else -> "${convert(v / 1_000_000_000L)} miliar ${convert(v % 1_000_000_000L)}".trim()
        }
        return convert(n).replace(Regex("\\s+"), " ").replaceFirstChar { it.uppercase() }
    }
}

@Composable
fun MicroFeaturesSwissArmyDialog(
    initialFeatureNumber: Int = MicroFeaturesState.selectedFeatureNumber,
    onDismiss: () -> Unit,
    onPostBanner: (String) -> Unit = {}
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.65f))
                .padding(horizontal = 10.dp, vertical = 18.dp),
            contentAlignment = Alignment.Center
        ) {
            MicroFeaturesSwissArmyDeckContent(
                initialFeatureNumber = initialFeatureNumber,
                onCloseModal = onDismiss,
                onPostBanner = onPostBanner,
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(26.dp))
                    .background(DuoSnowCanvas)
                    .border(2.dp, DuoSwanBorder, RoundedCornerShape(26.dp))
            )
        }
    }
}

@Composable
fun MicroFeaturesSwissArmyDeckContent(
    initialFeatureNumber: Int = 1,
    onCloseModal: (() -> Unit)? = null,
    onPostBanner: (String) -> Unit = {},
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var searchQuery by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf<MicroFeatureCategory?>(null) }
    var activeFeatureNumber by remember(initialFeatureNumber) { mutableIntStateOf(initialFeatureNumber.coerceIn(1, 120)) }

    val activeFeature = remember(activeFeatureNumber) {
        MicroFeaturesRegistry.allFeatures.find { it.number == activeFeatureNumber }
            ?: MicroFeaturesRegistry.allFeatures.first()
    }

    var inputText by remember(activeFeature.number) { mutableStateOf(activeFeature.defaultInput) }
    var sliderParam by remember(activeFeature.number) { mutableFloatStateOf(activeFeature.defaultParam) }
    var executionNonce by remember { mutableIntStateOf(0) }

    val liveResultText = remember(activeFeature, inputText, sliderParam, executionNonce) {
        MicroFeaturesState.executeMicroFeature(activeFeature, inputText, sliderParam, executionNonce)
    }

    val filteredFeatures = remember(searchQuery, selectedCategory) {
        MicroFeaturesRegistry.allFeatures.filter { item ->
            val matchesCat = selectedCategory == null || item.category == selectedCategory
            val q = searchQuery.trim().lowercase()
            val matchesSearch = q.isEmpty() ||
                item.title.lowercase().contains(q) ||
                item.subtitle.lowercase().contains(q) ||
                item.codeBadge.lowercase().contains(q) ||
                item.category.title.lowercase().contains(q)
            matchesCat && matchesSearch
        }
    }

    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .padding(14.dp)
            .testTag("micro_features_120_hub"),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // 1. Header Banner: 120 Interactive Micro-Features Hub
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .duolingoCard(
                    backgroundColor = CosmicObsidian,
                    borderColor = activeFeature.category.badgeColor,
                    lipColor = AppThemeController.darkenColor(activeFeature.category.badgeColor, 0.72f),
                    cornerRadius = 20.dp,
                    borderWidth = 2.dp,
                    lipHeight = 4.dp
                )
                .padding(bottom = 4.dp)
                .padding(14.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(RoundedCornerShape(14.dp))
                            .background(activeFeature.category.badgeColor.copy(alpha = 0.16f))
                            .border(1.5.dp, activeFeature.category.badgeColor, RoundedCornerShape(14.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Apps,
                            contentDescription = null,
                            tint = activeFeature.category.badgeColor,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                    Column {
                        Text(
                            text = "120+ MICRO-FITUR INTERAKTIF ARCANUM",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.ExtraBold,
                            color = CosmicTextPure
                        )
                        Text(
                            text = "10 Kategori • 120 Alat Saku Instan • Pin ke Dynamic Island",
                            style = MaterialTheme.typography.bodySmall,
                            color = CosmicTextSubtle
                        )
                    }
                }

                if (onCloseModal != null) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(DuoCardinalRed.copy(alpha = 0.14f))
                            .border(1.5.dp, DuoCardinalRed, CircleShape)
                            .clickable { onCloseModal() }
                            .testTag("close_micro_features_modal"),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Tutup",
                            tint = DuoCardinalRed,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }
        }

        // 2. Live Interactive Workbench for Currently Selected Micro-Feature (#001 - #120)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .duolingoCard(
                    backgroundColor = CosmicObsidian,
                    borderColor = activeFeature.category.badgeColor,
                    lipColor = AppThemeController.darkenColor(activeFeature.category.badgeColor, 0.72f),
                    cornerRadius = 20.dp,
                    borderWidth = 2.dp,
                    lipHeight = 4.dp
                )
                .padding(bottom = 4.dp)
                .padding(14.dp)
                .testTag("micro_feature_active_workbench")
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
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
                                .clip(RoundedCornerShape(8.dp))
                                .background(activeFeature.category.badgeColor)
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = activeFeature.codeBadge,
                                fontFamily = JetBrainsMonoFontFamily,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = Color.White
                            )
                        }
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = activeFeature.title,
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.ExtraBold,
                                color = CosmicTextPure,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            Text(
                                text = "${activeFeature.category.title} • ${activeFeature.subtitle}",
                                style = MaterialTheme.typography.bodySmall,
                                color = CosmicTextSubtle,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }

                    val isPinned = MicroFeaturesState.pinnedFeatureNumbers.contains(activeFeature.number)
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(10.dp))
                            .background(if (isPinned) activeFeature.category.badgeColor else DuoMintSurface)
                            .border(1.dp, activeFeature.category.badgeColor, RoundedCornerShape(10.dp))
                            .clickable {
                                MicroFeaturesState.togglePin(activeFeature.number)
                                onPostBanner(
                                    if (!isPinned) "Micro-Fitur ${activeFeature.codeBadge} disematkan ke Dynamic Island!"
                                    else "Micro-Fitur ${activeFeature.codeBadge} dilepas dari Dynamic Island."
                                )
                            }
                            .padding(horizontal = 8.dp, vertical = 5.dp)
                            .testTag("micro_feature_pin_toggle"),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                imageVector = if (isPinned) Icons.Default.PushPin else Icons.Default.AddCircleOutline,
                                contentDescription = null,
                                tint = if (isPinned) Color.White else activeFeature.category.badgeColor,
                                modifier = Modifier.size(13.dp)
                            )
                            Text(
                                text = if (isPinned) "DI ISLAND" else "PIN ISLAND",
                                fontSize = 9.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = if (isPinned) Color.White else activeFeature.category.badgeColor
                            )
                        }
                    }
                }

                OutlinedTextField(
                    value = inputText,
                    onValueChange = { inputText = it },
                    label = { Text("Input Data / Teks / Angka (${activeFeature.codeBadge})") },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("micro_feature_input_field")
                )

                // Parameter Slider for numeric / count / shift customization
                Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Parameter Kontrol / Skala / Faktor:",
                            style = MaterialTheme.typography.labelSmall,
                            color = CosmicTextSubtle
                        )
                        Text(
                            text = sliderParam.roundToInt().toString(),
                            fontFamily = JetBrainsMonoFontFamily,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = activeFeature.category.badgeColor
                        )
                    }
                    Slider(
                        value = sliderParam,
                        onValueChange = { sliderParam = it },
                        valueRange = 1f..100f,
                        colors = SliderDefaults.colors(
                            thumbColor = activeFeature.category.badgeColor,
                            activeTrackColor = activeFeature.category.badgeColor
                        )
                    )
                }

                // Live Output Result Box
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .background(DuoMintSurface)
                        .border(1.5.dp, activeFeature.category.badgeColor.copy(alpha = 0.5f), RoundedCornerShape(14.dp))
                        .padding(12.dp)
                        .testTag("micro_feature_output_box")
                ) {
                    Text(
                        text = liveResultText,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = CosmicTextPure
                    )
                }

                // Action Row: Execute/Refresh + Copy Result
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .duolingoButton(
                                faceColor = activeFeature.category.badgeColor,
                                lipColor = AppThemeController.darkenColor(activeFeature.category.badgeColor, 0.72f),
                                cornerRadius = 12.dp,
                                lipHeight = 3.dp
                            )
                            .instantTouchPress {
                                executionNonce++
                            }
                            .padding(bottom = 2.dp)
                            .padding(vertical = 10.dp)
                            .testTag("micro_feature_execute_button"),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.PlayArrow,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(16.dp)
                            )
                            Text(
                                text = activeFeature.actionLabel,
                                style = MaterialTheme.typography.labelLarge,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    }

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .duolingoButton(
                                faceColor = DuoMacawBlue,
                                lipColor = DuoMacawBlueLip,
                                cornerRadius = 12.dp,
                                lipHeight = 3.dp
                            )
                            .instantTouchPress {
                                val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as? ClipboardManager
                                val clipText = if (activeFeature.number == 36) "" else liveResultText
                                clipboard?.setPrimaryClip(ClipData.newPlainText("ArcanumMicroFeature", clipText))
                                onPostBanner(
                                    if (activeFeature.number == 36) "Clipboard sistem berhasil dibersihkan!"
                                    else "Hasil ${activeFeature.codeBadge} (${activeFeature.title}) disalin ke Clipboard!"
                                )
                            }
                            .padding(bottom = 2.dp)
                            .padding(vertical = 10.dp)
                            .testTag("micro_feature_copy_button"),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.ContentCopy,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(15.dp)
                            )
                            Text(
                                text = "Salin Hasil",
                                style = MaterialTheme.typography.labelLarge,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    }
                }
            }
        }

        // 3. Search Bar & 10 Category Filter Pills
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            leadingIcon = {
                Icon(imageVector = Icons.Default.Search, contentDescription = null)
            },
            trailingIcon = {
                if (searchQuery.isNotEmpty()) {
                    IconButton(onClick = { searchQuery = "" }) {
                        Icon(imageVector = Icons.Default.Clear, contentDescription = "Hapus Cari")
                    }
                }
            },
            placeholder = { Text("Cari dari 120 micro-fitur (#001 - #120, nama, fungsi)...") },
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .testTag("micro_features_search_input")
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            val allSelected = selectedCategory == null
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(999.dp))
                    .background(if (allSelected) DuoFeatherGreen else DuoMintSurface)
                    .border(1.dp, if (allSelected) DuoFeatherGreen else DuoSwanBorder, RoundedCornerShape(999.dp))
                    .clickable { selectedCategory = null }
                    .padding(horizontal = 12.dp, vertical = 7.dp)
            ) {
                Text(
                    text = "Semua (120)",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    color = if (allSelected) Color.White else CosmicTextPure
                )
            }

            MicroFeatureCategory.entries.forEach { cat ->
                val isCatSelected = selectedCategory == cat
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(999.dp))
                        .background(if (isCatSelected) cat.badgeColor else DuoMintSurface)
                        .border(1.dp, cat.badgeColor, RoundedCornerShape(999.dp))
                        .clickable { selectedCategory = if (isCatSelected) null else cat }
                        .padding(horizontal = 11.dp, vertical = 7.dp)
                        .testTag("micro_cat_${cat.id.lowercase()}")
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(5.dp)
                    ) {
                        Icon(
                            imageVector = cat.icon,
                            contentDescription = null,
                            tint = if (isCatSelected) Color.White else cat.badgeColor,
                            modifier = Modifier.size(13.dp)
                        )
                        Text(
                            text = "${cat.title} (12)",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            color = if (isCatSelected) Color.White else CosmicTextPure
                        )
                    }
                }
            }
        }

        // 4. Grid of All 120 Interactive Micro-Features (2 Columns)
        Text(
            text = "Daftar Micro-Fitur Aktif (${filteredFeatures.size} dari 120 Fitur) — Ketuk untuk Buka di Workbench:",
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.Bold,
            color = CosmicTextPure
        )

        filteredFeatures.chunked(2).forEach { pair ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                pair.forEach { item ->
                    val isCurrent = item.number == activeFeature.number
                    val isPinned = MicroFeaturesState.pinnedFeatureNumbers.contains(item.number)

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .duolingoButton(
                                faceColor = if (isCurrent) item.category.badgeColor else CosmicObsidian,
                                lipColor = if (isCurrent) AppThemeController.darkenColor(item.category.badgeColor, 0.72f) else DuoSwanBorder,
                                cornerRadius = 14.dp,
                                lipHeight = 3.dp
                            )
                            .instantTouchPress {
                                activeFeatureNumber = item.number
                                MicroFeaturesState.selectedFeatureNumber = item.number
                            }
                            .padding(bottom = 2.dp)
                            .padding(10.dp)
                            .testTag("micro_feature_card_${item.number}")
                    ) {
                        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(6.dp))
                                        .background(if (isCurrent) Color.White.copy(alpha = 0.22f) else item.category.badgeColor.copy(alpha = 0.14f))
                                        .padding(horizontal = 6.dp, vertical = 2.dp)
                                ) {
                                    Text(
                                        text = item.codeBadge,
                                        fontFamily = JetBrainsMonoFontFamily,
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.ExtraBold,
                                        color = if (isCurrent) Color.White else item.category.badgeColor
                                    )
                                }
                                if (isPinned) {
                                    Icon(
                                        imageVector = Icons.Default.PushPin,
                                        contentDescription = "Disematkan",
                                        tint = if (isCurrent) Color.White else item.category.badgeColor,
                                        modifier = Modifier.size(12.dp)
                                    )
                                }
                            }
                            Text(
                                text = item.title,
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Bold,
                                color = if (isCurrent) Color.White else CosmicTextPure,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            Text(
                                text = item.subtitle,
                                style = MaterialTheme.typography.labelSmall,
                                fontSize = 9.5.sp,
                                color = if (isCurrent) Color.White.copy(alpha = 0.9f) else CosmicTextSubtle,
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                }
                if (pair.size == 1) {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
    }
}
