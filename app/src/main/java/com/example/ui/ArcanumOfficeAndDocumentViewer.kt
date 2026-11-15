package com.example.ui

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.Typeface
import android.graphics.pdf.PdfDocument
import android.graphics.pdf.PdfRenderer
import android.os.Bundle
import android.os.CancellationSignal
import android.os.ParcelFileDescriptor
import android.print.PageRange
import android.print.PrintAttributes
import android.print.PrintDocumentAdapter
import android.print.PrintDocumentInfo
import android.print.PrintManager
import android.util.Xml
import android.widget.VideoView
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.NavigateBefore
import androidx.compose.material.icons.automirrored.filled.NavigateNext
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.GridOn
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.PictureAsPdf
import androidx.compose.material.icons.filled.Print
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Slideshow
import androidx.compose.material.icons.filled.ZoomIn
import androidx.compose.material.icons.filled.ZoomOutMap
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import coil.compose.AsyncImage
import com.example.data.VaultCategory
import com.example.data.VaultItemEntity
import com.example.ui.theme.JetBrainsMonoFontFamily
import com.example.ui.theme.VaultAmber
import com.example.ui.theme.VaultCharcoal
import com.example.ui.theme.VaultCoral
import com.example.ui.theme.VaultLime
import com.example.ui.theme.VaultMintCard
import com.example.ui.theme.VaultObsidian
import com.example.ui.theme.VaultPureWhite
import com.example.ui.theme.VaultSlate
import java.io.ByteArrayInputStream
import java.io.File
import java.io.FileOutputStream
import java.util.Locale
import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream
import java.util.zip.ZipOutputStream
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.xmlpull.v1.XmlPullParser

enum class ArcanumDocFormat(
    val badgeLabel: String,
    val accentColor: Color
) {
    PDF("PDF READER", Color(0xFFFF3B30)),
    WORD("WORD DOCX", Color(0xFF007AFF)),
    EXCEL("EXCEL SHEET", Color(0xFF34C759)),
    POWERPOINT("PPTX SLIDES", Color(0xFFFF9500)),
    TEXT_CODE("TEXT & CODE", Color(0xFF5AC8FA));

    companion object {
        fun fromExtension(ext: String): ArcanumDocFormat = when (ext.lowercase().trim().removePrefix(".")) {
            "pdf" -> PDF
            "docx", "doc", "rtf", "odt" -> WORD
            "xlsx", "xls", "csv", "tsv", "ods" -> EXCEL
            "pptx", "ppt", "odp" -> POWERPOINT
            else -> TEXT_CODE
        }
    }
}

sealed class DocxBlock {
    data class Heading(val text: String, val level: Int, val sectionIndex: Int) : DocxBlock()
    data class Paragraph(
        val text: String,
        val isBold: Boolean = false,
        val isItalic: Boolean = false,
        val isBullet: Boolean = false,
        val sectionIndex: Int = 0
    ) : DocxBlock()
    data class Table(val rows: List<List<String>>, val sectionIndex: Int = 0) : DocxBlock()
}

data class ParsedWordDocument(
    val title: String,
    val blocks: List<DocxBlock>,
    val sectionTitles: List<String>,
    val wordCount: Int
)

data class ParsedExcelSheet(
    val sheetName: String,
    val rows: List<List<String>>,
    val maxColumns: Int
)

data class ParsedExcelWorkbook(
    val sheets: List<ParsedExcelSheet>
)

data class ParsedPptxSlide(
    val slideNumber: Int,
    val title: String,
    val bulletPoints: List<String>,
    val speakerNotes: String
)

data class ParsedPptxDeck(
    val slides: List<ParsedPptxSlide>
)

data class ExternalFilePreviewRequest(
    val tempFile: File,
    val displayName: String,
    val extension: String,
    val mimeType: String,
    val sizeBytes: Long,
    val sourceUriString: String
)

/**
 * Mesin Parser OpenXML & Dokumen Universal Tanpa Dependensi Eksternal Berat:
 * Mendukung:
 * - `.docx` (`word/document.xml` untuk Heading, Paragraf, Bullet, dan Tabel `w:tbl`)
 * - `.xlsx` (`xl/workbook.xml`, `xl/sharedStrings.xml`, `xl/worksheets/sheet*.xml`) & `.csv`/`.tsv`
 * - `.pptx` (`ppt/slides/slide*.xml` & `ppt/notesSlides/notesSlide*.xml`)
 * - `.doc`, `.rtf`, `.txt`, `.md`, `.json`, `.xml`, `.html`, `.kt`, `.py`, `.js`
 */
object ArcanumOfficeDocumentParser {

    fun parseWordOrTextDocument(file: File, fallbackTitle: String, fallbackPreview: String): ParsedWordDocument {
        val ext = file.extension.lowercase()
        if (!file.exists() || file.length() <= 0L) {
            return buildPlainTextDocument(fallbackTitle, fallbackPreview)
        }
        if (ext == "docx") {
            val entries = readZipEntries(file)
            val docXml = entries["word/document.xml"]
            if (docXml != null) {
                return parseDocxXmlBytes(docXml, fallbackTitle)
            }
        }
        val rawText = try {
            if (ext == "doc" || ext == "rtf") {
                extractReadableTextFromBinaryDoc(file)
            } else {
                file.inputStream().bufferedReader(Charsets.UTF_8).use { reader ->
                    val sb = StringBuilder()
                    val buf = CharArray(8192)
                    var total = 0
                    while (total < 300_000) {
                        val r = reader.read(buf)
                        if (r <= 0) break
                        sb.append(buf, 0, r)
                        total += r
                    }
                    sb.toString()
                }
            }
        } catch (_: Exception) {
            fallbackPreview
        }
        return buildPlainTextDocument(fallbackTitle, rawText.ifBlank { fallbackPreview })
    }

    private fun parseDocxXmlBytes(xmlBytes: ByteArray, fallbackTitle: String): ParsedWordDocument {
        val blocks = mutableListOf<DocxBlock>()
        val sectionTitles = mutableListOf<String>()
        var currentSectionIdx = 0
        var totalWords = 0

        try {
            val parser = Xml.newPullParser()
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false)
            parser.setInput(ByteArrayInputStream(xmlBytes), "UTF-8")

            var inTable = false
            val currentTableRows = mutableListOf<List<String>>()
            var currentRowCells = mutableListOf<String>()
            val currentCellText = StringBuilder()

            var inParagraph = false
            var pStyleVal = ""
            var isBulletP = false
            var pBold = false
            var pItalic = false
            val pBuilder = StringBuilder()

            var eventType = parser.eventType
            while (eventType != XmlPullParser.END_DOCUMENT) {
                val rawName = parser.name ?: ""
                val localName = rawName.substringAfterLast(':')

                when (eventType) {
                    XmlPullParser.START_TAG -> {
                        when (localName) {
                            "tbl" -> {
                                inTable = true
                                currentTableRows.clear()
                            }
                            "tr" -> {
                                if (inTable) {
                                    currentRowCells = mutableListOf()
                                }
                            }
                            "tc" -> {
                                if (inTable) {
                                    currentCellText.setLength(0)
                                }
                            }
                            "p" -> {
                                inParagraph = true
                                pStyleVal = ""
                                isBulletP = false
                                pBold = false
                                pItalic = false
                                pBuilder.setLength(0)
                            }
                            "pStyle" -> {
                                val v = getAttrValue(parser, "val")
                                if (v != null) pStyleVal = v
                            }
                            "numPr", "numId" -> {
                                if (inParagraph) isBulletP = true
                            }
                            "b" -> if (inParagraph) pBold = true
                            "i" -> if (inParagraph) pItalic = true
                            "tab" -> {
                                if (inTable) currentCellText.append("   ")
                                else if (inParagraph) pBuilder.append("    ")
                            }
                            "br", "cr" -> {
                                if (inTable) currentCellText.append("\n")
                                else if (inParagraph) pBuilder.append("\n")
                            }
                            "t" -> {
                                val txt = parser.nextText()
                                if (inTable) {
                                    if ( currentCellText.isNotEmpty() && !currentCellText.endsWith(" ") && !currentCellText.endsWith("\n")) {
                                        currentCellText.append("")
                                    }
                                    currentCellText.append(txt)
                                } else if (inParagraph) {
                                    pBuilder.append(txt)
                                }
                            }
                        }
                    }
                    XmlPullParser.END_TAG -> {
                        when (localName) {
                            "tc" -> {
                                if (inTable) {
                                    currentRowCells.add(currentCellText.toString().trim())
                                }
                            }
                            "tr" -> {
                                if (inTable && currentRowCells.isNotEmpty()) {
                                    currentTableRows.add(currentRowCells.toList())
                                }
                            }
                            "tbl" -> {
                                if (inTable && currentTableRows.isNotEmpty()) {
                                    blocks.add(DocxBlock.Table(currentTableRows.toList(), currentSectionIdx))
                                }
                                inTable = false
                            }
                            "p" -> {
                                if (inParagraph && !inTable) {
                                    val text = pBuilder.toString().trim()
                                    if (text.isNotEmpty()) {
                                        totalWords += text.split(Regex("\\s+")).size
                                        val lowerStyle = pStyleVal.lowercase()
                                        when {
                                            lowerStyle.contains("heading1") || lowerStyle.contains("title") || lowerStyle == "h1" -> {
                                                currentSectionIdx = sectionTitles.size
                                                sectionTitles.add(text.take(40))
                                                blocks.add(DocxBlock.Heading(text, 1, currentSectionIdx))
                                            }
                                            lowerStyle.contains("heading2") || lowerStyle.contains("subtitle") || lowerStyle == "h2" -> {
                                                currentSectionIdx = sectionTitles.size
                                                sectionTitles.add(text.take(40))
                                                blocks.add(DocxBlock.Heading(text, 2, currentSectionIdx))
                                            }
                                            lowerStyle.contains("heading3") || lowerStyle == "h3" -> {
                                                blocks.add(DocxBlock.Heading(text, 3, currentSectionIdx))
                                            }
                                            else -> {
                                                blocks.add(
                                                    DocxBlock.Paragraph(
                                                        text = text,
                                                        isBold = pBold,
                                                        isItalic = pItalic,
                                                        isBullet = isBulletP,
                                                        sectionIndex = currentSectionIdx
                                                    )
                                                )
                                            }
                                        }
                                    }
                                } else if (inParagraph && inTable) {
                                    if (currentCellText.isNotEmpty()) currentCellText.append(" ")
                                }
                                inParagraph = false
                            }
                        }
                    }
                }
                eventType = parser.next()
            }
        } catch (_: Exception) {
        }

        if (blocks.isEmpty()) {
            return buildPlainTextDocument(fallbackTitle, "Dokumen Word ($fallbackTitle) kosong atau tidak memiliki teks.")
        }
        if (sectionTitles.isEmpty()) {
            sectionTitles.add("Bagian 1")
        }
        return ParsedWordDocument(
            title = fallbackTitle,
            blocks = blocks,
            sectionTitles = sectionTitles,
            wordCount = totalWords.coerceAtLeast(1)
        )
    }

    private fun buildPlainTextDocument(title: String, rawText: String): ParsedWordDocument {
        val blocks = mutableListOf<DocxBlock>()
        val sectionTitles = mutableListOf<String>()
        var currentSec = 0
        var words = 0

        val lines = rawText.replace("\r\n", "\n").split("\n")
        for (line in lines) {
            val trimmed = line.trim()
            if (trimmed.isEmpty()) continue
            words += trimmed.split(Regex("\\s+")).size
            when {
                trimmed.startsWith("# ") -> {
                    val h = trimmed.removePrefix("# ").trim()
                    currentSec = sectionTitles.size
                    sectionTitles.add(h.take(36))
                    blocks.add(DocxBlock.Heading(h, 1, currentSec))
                }
                trimmed.startsWith("## ") -> {
                    val h = trimmed.removePrefix("## ").trim()
                    currentSec = sectionTitles.size
                    sectionTitles.add(h.take(36))
                    blocks.add(DocxBlock.Heading(h, 2, currentSec))
                }
                trimmed.startsWith("### ") -> {
                    val h = trimmed.removePrefix("### ").trim()
                    blocks.add(DocxBlock.Heading(h, 3, currentSec))
                }
                trimmed.startsWith("- ") || trimmed.startsWith("* ") || trimmed.startsWith("• ") -> {
                    blocks.add(
                        DocxBlock.Paragraph(
                            text = trimmed.drop(2).trim(),
                            isBullet = true,
                            sectionIndex = currentSec
                        )
                    )
                }
                else -> {
                    blocks.add(
                        DocxBlock.Paragraph(
                            text = line,
                            sectionIndex = currentSec
                        )
                    )
                }
            }
        }
        if (sectionTitles.isEmpty()) {
            sectionTitles.add("Halaman Utama")
        }
        if (blocks.isEmpty()) {
            blocks.add(DocxBlock.Paragraph(rawText.ifBlank { title }, sectionIndex = 0))
        }
        return ParsedWordDocument(
            title = title,
            blocks = blocks,
            sectionTitles = sectionTitles,
            wordCount = words.coerceAtLeast(1)
        )
    }

    private fun extractReadableTextFromBinaryDoc(file: File): String {
        val bytes = file.readBytes()
        val sb = StringBuilder()
        var currentRun = StringBuilder()
        for (b in bytes) {
            val c = b.toInt() and 0xFF
            if (c in 32..126 || c == 10 || c == 13 || c == 9) {
                currentRun.append(c.toChar())
            } else {
                if (currentRun.length >= 5) {
                    sb.append(currentRun).append("\n")
                }
                currentRun.setLength(0)
            }
        }
        if (currentRun.length >= 5) sb.append(currentRun)
        return sb.toString().trim()
    }

    fun parseExcelWorkbook(file: File, fallbackName: String, fallbackPreview: String): ParsedExcelWorkbook {
        val ext = file.extension.lowercase()
        if (!file.exists() || file.length() <= 0L) {
            return parseDelimitedTextToWorkbook(fallbackName, fallbackPreview, ',')
        }
        if (ext == "csv") {
            val content = try { file.readText(Charsets.UTF_8) } catch (_: Exception) { fallbackPreview }
            return parseDelimitedTextToWorkbook(fallbackName, content, ',')
        }
        if (ext == "tsv") {
            val content = try { file.readText(Charsets.UTF_8) } catch (_: Exception) { fallbackPreview }
            return parseDelimitedTextToWorkbook(fallbackName, content, '\t')
        }
        if (ext == "xlsx") {
            val entries = readZipEntries(file)
            if (entries.isNotEmpty()) {
                val sharedStrings = parseXlsxSharedStrings(entries["xl/sharedStrings.xml"])
                val sheetNames = parseXlsxSheetNames(entries["xl/workbook.xml"])
                val worksheetKeys = entries.keys
                    .filter { it.startsWith("xl/worksheets/sheet") && it.endsWith(".xml") }
                    .sortedBy { key ->
                        key.substringAfter("sheet").substringBefore(".xml").toIntOrNull() ?: 999
                    }

                val parsedSheets = worksheetKeys.mapIndexed { idx, key ->
                    val sName = sheetNames.getOrNull(idx) ?: "Sheet ${idx + 1}"
                    val rows = parseXlsxWorksheet(entries[key] ?: ByteArray(0), sharedStrings)
                    val maxCols = rows.maxOfOrNull { it.size }?.coerceAtLeast(1) ?: 1
                    ParsedExcelSheet(
                        sheetName = sName,
                        rows = rows.map { row ->
                            if (row.size < maxCols) row + List(maxCols - row.size) { "" } else row
                        },
                        maxColumns = maxCols
                    )
                }.filter { it.rows.isNotEmpty() }

                if (parsedSheets.isNotEmpty()) {
                    return ParsedExcelWorkbook(parsedSheets)
                }
            }
        }
        val fallbackText = try { extractReadableTextFromBinaryDoc(file) } catch (_: Exception) { fallbackPreview }
        return parseDelimitedTextToWorkbook(fallbackName, fallbackText.ifBlank { fallbackPreview }, ',')
    }

    private fun parseDelimitedTextToWorkbook(name: String, content: String, delimiter: Char): ParsedExcelWorkbook {
        val lines = content.replace("\r\n", "\n").split("\n").filter { it.isNotBlank() }
        val rows = lines.map { line ->
            parseCsvLine(line, delimiter)
        }.ifEmpty {
            listOf(listOf("Kolom A", "Kolom B"), listOf(name, "Data Kosong"))
        }
        val maxCols = rows.maxOfOrNull { it.size }?.coerceAtLeast(1) ?: 1
        val normalized = rows.map { r ->
            if (r.size < maxCols) r + List(maxCols - r.size) { "" } else r
        }
        return ParsedExcelWorkbook(
            listOf(
                ParsedExcelSheet(
                    sheetName = name.take(24).ifBlank { "Sheet 1" },
                    rows = normalized,
                    maxColumns = maxCols
                )
            )
        )
    }

    private fun parseCsvLine(line: String, delimiter: Char): List<String> {
        val cells = mutableListOf<String>()
        val sb = StringBuilder()
        var inQuotes = false
        var i = 0
        while (i < line.length) {
            val ch = line[i]
            when {
                ch == '"' -> {
                    if (inQuotes && i + 1 < line.length && line[i + 1] == '"') {
                        sb.append('"')
                        i++
                    } else {
                        inQuotes = !inQuotes
                    }
                }
                ch == delimiter && !inQuotes -> {
                    cells.add(sb.toString().trim())
                    sb.setLength(0)
                }
                else -> sb.append(ch)
            }
            i++
        }
        cells.add(sb.toString().trim())
        return cells
    }

    private fun parseXlsxSharedStrings(xmlBytes: ByteArray?): List<String> {
        if (xmlBytes == null || xmlBytes.isEmpty()) return emptyList()
        val list = mutableListOf<String>()
        try {
            val parser = Xml.newPullParser()
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false)
            parser.setInput(ByteArrayInputStream(xmlBytes), "UTF-8")
            var inSi = false
            val sb = StringBuilder()
            var event = parser.eventType
            while (event != XmlPullParser.END_DOCUMENT) {
                val local = (parser.name ?: "").substringAfterLast(':')
                when (event) {
                    XmlPullParser.START_TAG -> {
                        if (local == "si") {
                            inSi = true
                            sb.setLength(0)
                        } else if (local == "t" && inSi) {
                            sb.append(parser.nextText())
                        }
                    }
                    XmlPullParser.END_TAG -> {
                        if (local == "si" && inSi) {
                            list.add(sb.toString())
                            inSi = false
                        }
                    }
                }
                event = parser.next()
            }
        } catch (_: Exception) {
        }
        return list
    }

    private fun parseXlsxSheetNames(xmlBytes: ByteArray?): List<String> {
        if (xmlBytes == null || xmlBytes.isEmpty()) return emptyList()
        val names = mutableListOf<String>()
        try {
            val parser = Xml.newPullParser()
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false)
            parser.setInput(ByteArrayInputStream(xmlBytes), "UTF-8")
            var event = parser.eventType
            while (event != XmlPullParser.END_DOCUMENT) {
                val local = (parser.name ?: "").substringAfterLast(':')
                if (event == XmlPullParser.START_TAG && local == "sheet") {
                    val n = getAttrValue(parser, "name")
                    if (!n.isNullOrBlank()) names.add(n)
                }
                event = parser.next()
            }
        } catch (_: Exception) {
        }
        return names
    }

    private fun parseXlsxWorksheet(xmlBytes: ByteArray, sharedStrings: List<String>): List<List<String>> {
        if (xmlBytes.isEmpty()) return emptyList()
        val rows = mutableListOf<List<String>>()
        try {
            val parser = Xml.newPullParser()
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false)
            parser.setInput(ByteArrayInputStream(xmlBytes), "UTF-8")

            var currentRowMap = sortedMapOf<Int, String>()
            var currentColIdx = 0
            var cellType = ""
            var cellValue = ""
            var cellFormula = ""

            var event = parser.eventType
            while (event != XmlPullParser.END_DOCUMENT) {
                val local = (parser.name ?: "").substringAfterLast(':')
                when (event) {
                    XmlPullParser.START_TAG -> {
                        when (local) {
                            "row" -> {
                                currentRowMap = sortedMapOf()
                                currentColIdx = 0
                            }
                            "c" -> {
                                val ref = getAttrValue(parser, "r") ?: ""
                                val parsedCol = parseExcelColIndex(ref)
                                if (parsedCol >= 0) currentColIdx = parsedCol
                                cellType = getAttrValue(parser, "t") ?: ""
                                cellValue = ""
                                cellFormula = ""
                            }
                            "v", "t" -> {
                                cellValue += parser.nextText()
                            }
                            "f" -> {
                                cellFormula = parser.nextText()
                            }
                        }
                    }
                    XmlPullParser.END_TAG -> {
                        when (local) {
                            "c" -> {
                                val resolved = when (cellType) {
                                    "s" -> {
                                        val sIdx = cellValue.trim().toIntOrNull()
                                        if (sIdx != null && sIdx in sharedStrings.indices) sharedStrings[sIdx] else cellValue
                                    }
                                    "b" -> if (cellValue.trim() == "1") "TRUE" else "FALSE"
                                    else -> {
                                        if (cellValue.isNotBlank()) cellValue
                                        else if (cellFormula.isNotBlank()) "=$cellFormula"
                                        else ""
                                    }
                                }
                                currentRowMap[currentColIdx] = resolved
                                currentColIdx++
                            }
                            "row" -> {
                                if (currentRowMap.isNotEmpty()) {
                                    val maxCol = (currentRowMap.keys.maxOrNull() ?: 0).coerceAtMost(50)
                                    val rowList = (0..maxCol).map { col -> currentRowMap[col] ?: "" }
                                    rows.add(rowList)
                                }
                            }
                        }
                    }
                }
                event = parser.next()
            }
        } catch (_: Exception) {
        }
        return rows
    }

    private fun parseExcelColIndex(cellRef: String): Int {
        val letters = cellRef.takeWhile { it.isLetter() }.uppercase()
        if (letters.isEmpty()) return -1
        var col = 0
        for (ch in letters) {
            col = col * 26 + (ch - 'A' + 1)
        }
        return col - 1
    }

    fun excelColumnLabel(index: Int): String {
        var n = index + 1
        val sb = StringBuilder()
        while (n > 0) {
            val rem = (n - 1) % 26
            sb.insert(0, ('A'.code + rem).toChar())
            n = (n - 1) / 26
        }
        return sb.toString()
    }

    fun parsePptxDeck(file: File, fallbackTitle: String, fallbackPreview: String): ParsedPptxDeck {
        if (file.exists() && file.length() > 0L && file.extension.equals("pptx", ignoreCase = true)) {
            val entries = readZipEntries(file)
            val slideKeys = entries.keys
                .filter { it.startsWith("ppt/slides/slide") && it.endsWith(".xml") }
                .sortedBy { key ->
                    key.substringAfter("slide").substringBefore(".xml").toIntOrNull() ?: 999
                }

            if (slideKeys.isNotEmpty()) {
                val slides = slideKeys.mapIndexed { idx, slideKey ->
                    val slideNum = idx + 1
                    val paragraphs = parsePptxSlideParagraphs(entries[slideKey] ?: ByteArray(0))
                    val notesKey = "ppt/notesSlides/notesSlide$slideNum.xml"
                    val notesParagraphs = entries[notesKey]?.let { parsePptxSlideParagraphs(it) } ?: emptyList()

                    val title = paragraphs.firstOrNull()?.takeIf { it.isNotBlank() } ?: "Slide $slideNum"
                    val bullets = if (paragraphs.size > 1) paragraphs.drop(1) else listOf("Presentasi $fallbackTitle")
                    ParsedPptxSlide(
                        slideNumber = slideNum,
                        title = title,
                        bulletPoints = bullets,
                        speakerNotes = notesParagraphs.joinToString(" • ")
                    )
                }
                return ParsedPptxDeck(slides)
            }
        }

        val fallbackLines = fallbackPreview.lines().map { it.trim() }.filter { it.isNotEmpty() }
        return ParsedPptxDeck(
            listOf(
                ParsedPptxSlide(
                    slideNumber = 1,
                    title = fallbackTitle,
                    bulletPoints = fallbackLines.ifEmpty { listOf("Slide Presentasi $fallbackTitle") },
                    speakerNotes = "Catatan presenter tersedia di mode pratinjau."
                )
            )
        )
    }

    private fun parsePptxSlideParagraphs(xmlBytes: ByteArray): List<String> {
        if (xmlBytes.isEmpty()) return emptyList()
        val paragraphs = mutableListOf<String>()
        try {
            val parser = Xml.newPullParser()
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false)
            parser.setInput(ByteArrayInputStream(xmlBytes), "UTF-8")

            var inP = false
            val pText = StringBuilder()
            var event = parser.eventType
            while (event != XmlPullParser.END_DOCUMENT) {
                val local = (parser.name ?: "").substringAfterLast(':')
                when (event) {
                    XmlPullParser.START_TAG -> {
                        if (local == "p") {
                            inP = true
                            pText.setLength(0)
                        } else if (local == "t" && inP) {
                            pText.append(parser.nextText())
                        }
                    }
                    XmlPullParser.END_TAG -> {
                        if (local == "p" && inP) {
                            val clean = pText.toString().trim()
                            if (clean.isNotEmpty()) {
                                paragraphs.add(clean)
                            }
                            inP = false
                        }
                    }
                }
                event = parser.next()
            }
        } catch (_: Exception) {
        }
        return paragraphs
    }

    private fun readZipEntries(file: File): Map<String, ByteArray> {
        val map = mutableMapOf<String, ByteArray>()
        try {
            file.inputStream().use { fis ->
                ZipInputStream(fis).use { zis ->
                    var entry: ZipEntry? = zis.nextEntry
                    while (entry != null) {
                        if (!entry.isDirectory) {
                            val name = entry.name.replace('\\', '/').trimStart('/')
                            if (name.endsWith(".xml") || name.endsWith(".rels")) {
                                map[name] = zis.readBytes()
                            }
                        }
                        zis.closeEntry()
                        entry = zis.nextEntry
                    }
                }
            }
        } catch (_: Exception) {
        }
        return map
    }

    private fun getAttrValue(parser: XmlPullParser, localAttrName: String): String? {
        for (i in 0 until parser.attributeCount) {
            val aName = parser.getAttributeName(i).substringAfterLast(':')
            if (aName.equals(localAttrName, ignoreCase = true)) {
                return parser.getAttributeValue(i)
            }
        }
        return null
    }
}

/**
 * Generator File Sampel Bundled (PDF, DOCX, XLSX, PPTX) Asli & Valid:
 * Memastikan setiap instalasi atau update APK menyediakan dokumen nyata yang dapat diuji
 * dan otomatis menimpa (overwrite) versi lama tanpa menimbulkan duplikat atau konflik database.
 */
object ArcanumOfficeSampleGenerator {

    fun writeSamplePdfFile(targetFile: File) {
        val pdf = PdfDocument()
        try {
            val titles = listOf(
                "ARCANUM VAULT • PANDUAN EKSEKUTIF NYX2",
                "ARSITEKTUR KEAMANAN 3-LAPIS & STEGANOGRAFI",
                "PROTOKOL BUKA DENGAN ARCANUM & ANTI-KONFLIK APK"
            )
            val bodies = listOf(
                listOf(
                    "1. Ringkasan Eksekutif Arcanum",
                    "Arcanum melindungi dokumen PDF, Word (DOCX), Excel (XLSX), PowerPoint (PPTX),",
                    "foto, video, dan arsip ZIP menggunakan kontainer enkripsi biner NYX2.",
                    "",
                    "2. Fitur Universal Document & Media Viewer:",
                    "• Render Halaman PDF resolusi tinggi dengan Pinch-to-Zoom (1x - 4x).",
                    "• Pembaca dokumen Word (.docx) lengkap dengan struktur Heading & Tabel.",
                    "• Tabel Spreadsheet Excel (.xlsx / .csv) multi-sheet dengan inspektor sel.",
                    "• Dek Slide Presentasi PowerPoint (.pptx) 16:9 dengan catatan pembicara.",
                    "• Mode Baca Gelap Otomatis (Dark Reading Mode) & Cetak / Ekspor Halaman."
                ),
                listOf(
                    "1. Isolasi 4 Ruang Berbeda:",
                    "• PIN 111111 (3x) -> Brankas Utama (#1..#10)",
                    "• PIN 222222 (3x) -> Ruang Pengaturan & Kriptografi",
                    "• PIN 333333 (3x) -> Ruang Game (Fun Zone)",
                    "• PIN 444444 (3x) -> Ruang Terisolasi Sanctuary (Chrome Browser + Kas)",
                    "",
                    "2. Proteksi Zero-Bypass Saat Buka File Eksternal:",
                    "Saat membuka file dari manajer berkas luar lewat 'Buka Dengan Arcanum',",
                    "file langsung ditampilkan di layar pratinjau terisolasi. Ketika tombol Back",
                    "ditekan, aplikasi otomatis mengunci kembali ke layar PIN Lapis 1."
                ),
                listOf(
                    "1. Kebijakan Auto-Overwrite Saat Update Aplikasi:",
                    "• Setiap media bawaan atau file bernama sama yang masuk saat update APK",
                    "  otomatis menimpa (overwrite) file versi lama tanpa menggandakan baris DB.",
                    "• Skema Room Database dijaga 100% kompatibel lintas pembaruan APK.",
                    "",
                    "2. Verifikasi Integritas SHA-256 & Padme Padding:",
                    "• Seluruh kontainer .nyx menyertakan tag integritas heksadesimal.",
                    "• Gunakan tombol Cetak / Ekspor di pojok kanan atas untuk mencetak halaman."
                )
            )

            val bgPaint = Paint(Paint.ANTI_ALIAS_FLAG)
            val headerPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                color = android.graphics.Color.parseColor("#0A84FF")
                typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
                textSize = 16f
            }
            val bodyPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                color = android.graphics.Color.parseColor("#1C1C1E")
                typeface = Typeface.create(Typeface.SANS_SERIF, Typeface.NORMAL)
                textSize = 12.5f
            }
            val badgePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                color = android.graphics.Color.parseColor("#E5F1FF")
            }

            for (i in titles.indices) {
                val pageInfo = PdfDocument.PageInfo.Builder(595, 842, i + 1).create()
                val page = pdf.startPage(pageInfo)
                val canvas: Canvas = page.canvas

                bgPaint.color = android.graphics.Color.WHITE
                canvas.drawRect(0f, 0f, 595f, 842f, bgPaint)

                canvas.drawRoundRect(RectF(36f, 36f, 559f, 96f), 14f, 14f, badgePaint)
                canvas.drawText(titles[i], 52f, 72f, headerPaint)

                var y = 136f
                for (line in bodies[i]) {
                    if (line.startsWith("1.") || line.startsWith("2.")) {
                        bodyPaint.typeface = Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD)
                        bodyPaint.color = android.graphics.Color.parseColor("#004080")
                    } else {
                        bodyPaint.typeface = Typeface.create(Typeface.SANS_SERIF, Typeface.NORMAL)
                        bodyPaint.color = android.graphics.Color.parseColor("#1C1C1E")
                    }
                    canvas.drawText(line, 48f, y, bodyPaint)
                    y += 26f
                }

                bodyPaint.textSize = 10f
                bodyPaint.color = android.graphics.Color.parseColor("#6C6C70")
                canvas.drawText("Arcanum NYX2 Executive PDF • Halaman ${i + 1} dari ${titles.size}", 48f, 800f, bodyPaint)
                bodyPaint.textSize = 12.5f

                pdf.finishPage(page)
            }

            FileOutputStream(targetFile).use { fos ->
                pdf.writeTo(fos)
            }
        } finally {
            pdf.close()
        }
    }

    fun writeSampleDocxFile(targetFile: File) {
        val documentXml = """
            <?xml version="1.0" encoding="UTF-8" standalone="yes"?>
            <w:document xmlns:w="http://schemas.openxmlformats.org/wordprocessingml/2006/main">
              <w:body>
                <w:p>
                  <w:pPr><w:pStyle w:val="Heading1"/></w:pPr>
                  <w:r><w:t>Laporan Audit Keamanan &amp; Arsitektur Arcanum NYX2</w:t></w:r>
                </w:p>
                <w:p>
                  <w:r><w:b/><w:t>Status Dokumen: Terverifikasi • Pembaruan Otomatis Anti-Gagal APK</w:t></w:r>
                </w:p>
                <w:p>
                  <w:r><w:t>Dokumen Word (.docx) ini dirender secara langsung oleh mesin OpenXML Arcanum tanpa memerlukan aplikasi luar. Anda dapat mencari kata, memperbesar tampilan dengan cubitan dua jari (Pinch-to-Zoom), atau mengganti tema baca gelap/terang.</w:t></w:r>
                </w:p>
                <w:p>
                  <w:pPr><w:pStyle w:val="Heading2"/></w:pPr>
                  <w:r><w:t>1. Daftar Kapabilitas Pembaca Dokumen</w:t></w:r>
                </w:p>
                <w:p>
                  <w:pPr><w:numPr><w:ilvl w:val="0"/><w:numId w:val="1"/></w:numPr></w:pPr>
                  <w:r><w:t>Dukungan penuh dokumen PDF, Word (DOCX), Excel (XLSX/CSV), dan PowerPoint (PPTX).</w:t></w:r>
                </w:p>
                <w:p>
                  <w:pPr><w:numPr><w:ilvl w:val="0"/><w:numId w:val="1"/></w:numPr></w:pPr>
                  <w:r><w:t>Integrasi menu Buka Dengan Arcanum dari File Manager, WhatsApp, atau Telegram dengan penguncian PIN otomatis saat menekan tombol Back.</w:t></w:r>
                </w:p>
                <w:p>
                  <w:pPr><w:numPr><w:ilvl w:val="0"/><w:numId w:val="1"/></w:numPr></w:pPr>
                  <w:r><w:t>Sinkronisasi pintar saat update aplikasi: otomatis menimpa (overwrite) media lama bernama sama tanpa duplikasi.</w:t></w:r>
                </w:p>
                <w:p>
                  <w:pPr><w:pStyle w:val="Heading2"/></w:pPr>
                  <w:r><w:t>2. Tabel Matriks Ruang &amp; Otorisasi PIN</w:t></w:r>
                </w:p>
                <w:tbl>
                  <w:tr>
                    <w:tc><w:p><w:r><w:b/><w:t>Nama Ruang</w:t></w:r></w:p></w:tc>
                    <w:tc><w:p><w:r><w:b/><w:t>Kombinasi PIN Default</w:t></w:r></w:p></w:tc>
                    <w:tc><w:p><w:r><w:b/><w:t>Fungsi Utama</w:t></w:r></w:p></w:tc>
                  </w:tr>
                  <w:tr>
                    <w:tc><w:p><w:r><w:t>Brankas Utama #1</w:t></w:r></w:p></w:tc>
                    <w:tc><w:p><w:r><w:t>111111 - 111111 - 111111</w:t></w:r></w:p></w:tc>
                    <w:tc><w:p><w:r><w:t>Penyimpanan Media, Dokumen Office, Stego &amp; Mori</w:t></w:r></w:p></w:tc>
                  </w:tr>
                  <w:tr>
                    <w:tc><w:p><w:r><w:t>Ruang Pengaturan</w:t></w:r></w:p></w:tc>
                    <w:tc><w:p><w:r><w:t>222222 - 222222 - 222222</w:t></w:r></w:p></w:tc>
                    <w:tc><w:p><w:r><w:t>Konfigurasi PIN, Tema iOS, Radar &amp; ZIP Scanner</w:t></w:r></w:p></w:tc>
                  </w:tr>
                  <w:tr>
                    <w:tc><w:p><w:r><w:t>Ruang Fun Zone</w:t></w:r></w:p></w:tc>
                    <w:tc><w:p><w:r><w:t>333333 - 333333 - 333333</w:t></w:r></w:p></w:tc>
                    <w:tc><w:p><w:r><w:t>Arcade Mini-Games &amp; Kamera Kucing</w:t></w:r></w:p></w:tc>
                  </w:tr>
                  <w:tr>
                    <w:tc><w:p><w:r><w:t>Sanctuary 444</w:t></w:r></w:p></w:tc>
                    <w:tc><w:p><w:r><w:t>444444 - 444444 - 444444</w:t></w:r></w:p></w:tc>
                    <w:tc><w:p><w:r><w:t>Browser Chrome Privat, Candlestick Kas &amp; MT Bridge</w:t></w:r></w:p></w:tc>
                  </w:tr>
                </w:tbl>
              </w:body>
            </w:document>
        """.trimIndent()

        writeZipMapToFile(
            targetFile,
            mapOf(
                "[Content_Types].xml" to """<?xml version="1.0" encoding="UTF-8"?><Types xmlns="http://schemas.openxmlformats.org/package/2006/content-types"><Default Extension="xml" ContentType="application/xml"/></Types>""",
                "word/document.xml" to documentXml
            )
        )
    }

    fun writeSampleXlsxFile(targetFile: File) {
        val workbookXml = """
            <?xml version="1.0" encoding="UTF-8" standalone="yes"?>
            <workbook xmlns="http://schemas.openxmlformats.org/spreadsheetml/2006/main">
              <sheets>
                <sheet name="Ringkasan Kas" sheetId="1" r:id="rId1" xmlns:r="http://schemas.openxmlformats.org/officeDocument/2006/relationships"/>
                <sheet name="Inventaris Media" sheetId="2" r:id="rId2" xmlns:r="http://schemas.openxmlformats.org/officeDocument/2006/relationships"/>
              </sheets>
            </workbook>
        """.trimIndent()

        val sharedStrings = listOf(
            "Kode Akun", "Kategori Anggaran", "Target Bulanan (IDR)", "Realisasi (IDR)", "Status",
            "KAS-01", "Cadangan Darurat Brankas", "AMAN",
            "KAS-02", "Investasi Emas & Obligasi", "OPTIMAL",
            "KAS-03", "Operasional Harian & Cloud", "TERKENDALI",
            "KAS-04", "Dana Pendidikan & Riset", " AKTIF",
            "Format Berkas", "Mesin Pembaca", "Dukungan Overwrite",
            "PDF (.pdf)", "Android PdfRenderer HD", "Aktif Otomatis",
            "Word (.docx)", "OpenXML Document Engine", "Aktif Otomatis",
            "Excel (.xlsx/.csv)", "Multi-Sheet Grid Engine", "Aktif Otomatis",
            "PowerPoint (.pptx)", "16:9 Slide Deck Engine", "Aktif Otomatis"
        )

        val sharedStringsXml = buildString {
            append("""<?xml version="1.0" encoding="UTF-8" standalone="yes"?><sst xmlns="http://schemas.openxmlformats.org/spreadsheetml/2006/main" count="${sharedStrings.size}" uniqueCount="${sharedStrings.size}">""")
            for (s in sharedStrings) {
                val escaped = s.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;")
                append("<si><t>$escaped</t></si>")
            }
            append("</sst>")
        }

        val sheet1Xml = """
            <?xml version="1.0" encoding="UTF-8" standalone="yes"?>
            <worksheet xmlns="http://schemas.openxmlformats.org/spreadsheetml/2006/main">
              <sheetData>
                <row r="1">
                  <c r="A1" t="s"><v>0</v></c>
                  <c r="B1" t="s"><v>1</v></c>
                  <c r="C1" t="s"><v>2</v></c>
                  <c r="D1" t="s"><v>3</v></c>
                  <c r="E1" t="s"><v>4</v></c>
                </row>
                <row r="2">
                  <c r="A2" t="s"><v>5</v></c>
                  <c r="B2" t="s"><v>6</v></c>
                  <c r="C2"><v>25000000</v></c>
                  <c r="D2"><v>25000000</v></c>
                  <c r="E2" t="s"><v>7</v></c>
                </row>
                <row r="3">
                  <c r="A3" t="s"><v>8</v></c>
                  <c r="B3" t="s"><v>9</v></c>
                  <c r="C3"><v>40000000</v></c>
                  <c r="D3"><v>42500000</v></c>
                  <c r="E3" t="s"><v>10</v></c>
                </row>
                <row r="4">
                  <c r="A4" t="s"><v>11</v></c>
                  <c r="B4" t="s"><v>12</v></c>
                  <c r="C4"><v>12000000</v></c>
                  <c r="D4"><v>10850000</v></c>
                  <c r="E4" t="s"><v>13</v></c>
                </row>
                <row r="5">
                  <c r="A5" t="s"><v>14</v></c>
                  <c r="B5" t="s"><v>15</v></c>
                  <c r="C5"><v>18000000</v></c>
                  <c r="D5"><v>18000000</v></c>
                  <c r="E5" t="s"><v>16</v></c>
                </row>
              </sheetData>
            </worksheet>
        """.trimIndent()

        val sheet2Xml = """
            <?xml version="1.0" encoding="UTF-8" standalone="yes"?>
            <worksheet xmlns="http://schemas.openxmlformats.org/spreadsheetml/2006/main">
              <sheetData>
                <row r="1">
                  <c r="A1" t="s"><v>17</v></c>
                  <c r="B1" t="s"><v>18</v></c>
                  <c r="C1" t="s"><v>19</v></c>
                </row>
                <row r="2">
                  <c r="A2" t="s"><v>20</v></c>
                  <c r="B2" t="s"><v>21</v></c>
                  <c r="C2" t="s"><v>22</v></c>
                </row>
                <row r="3">
                  <c r="A3" t="s"><v>23</v></c>
                  <c r="B3" t="s"><v>24</v></c>
                  <c r="C3" t="s"><v>25</v></c>
                </row>
                <row r="4">
                  <c r="A4" t="s"><v>26</v></c>
                  <c r="B4" t="s"><v>27</v></c>
                  <c r="C4" t="s"><v>28</v></c>
                </row>
                <row r="5">
                  <c r="A5" t="s"><v>29</v></c>
                  <c r="B5" t="s"><v>30</v></c>
                  <c r="C5" t="s"><v>31</v></c>
                </row>
              </sheetData>
            </worksheet>
        """.trimIndent()

        writeZipMapToFile(
            targetFile,
            mapOf(
                "[Content_Types].xml" to """<?xml version="1.0" encoding="UTF-8"?><Types xmlns="http://schemas.openxmlformats.org/package/2006/content-types"><Default Extension="xml" ContentType="application/xml"/></Types>""",
                "xl/workbook.xml" to workbookXml,
                "xl/sharedStrings.xml" to sharedStringsXml,
                "xl/worksheets/sheet1.xml" to sheet1Xml,
                "xl/worksheets/sheet2.xml" to sheet2Xml
            )
        )
    }

    fun writeSamplePptxFile(targetFile: File) {
        fun buildSlideXml(title: String, bullets: List<String>): String = buildString {
            append("""<?xml version="1.0" encoding="UTF-8" standalone="yes"?><p:sld xmlns:a="http://schemas.openxmlformats.org/drawingml/2006/main" xmlns:p="http://schemas.openxmlformats.org/presentationml/2006/main"><p:cSld><p:spTree>""")
            append("<p:sp><p:txBody><a:p><a:r><a:t>${title.replace("&", "&amp;")}</a:t></a:r></a:p></p:txBody></p:sp>")
            for (b in bullets) {
                append("<p:sp><p:txBody><a:p><a:r><a:t>${b.replace("&", "&amp;")}</a:t></a:r></a:p></p:txBody></p:sp>")
            }
            append("</p:spTree></p:cSld></p:sld>")
        }

        val slide1 = buildSlideXml(
            "ARCANUM NYX2 • UNIVERSAL DOCUMENT & MEDIA SUITE",
            listOf(
                "Buka PDF, DOCX, Excel (XLSX/CSV), dan PowerPoint (PPTX) langsung di dalam Brankas.",
                "Dilengkapi Pinch-to-Zoom 4x, Pencarian Kata, Lompat Halaman, & Mode Baca Gelap.",
                "Mendukung Cetak & Ekspor Halaman ke PDF sistem Android."
            )
        )
        val slide2 = buildSlideXml(
            "INTEGRASI 'BUKA DENGAN ARCANUM' ZERO-BYPASS",
            listOf(
                "Pilih 'Arcanum' saat membuka file dokumen atau media dari aplikasi luar.",
                "Layar Pratinjau Terisolasi langsung menampilkan isi file secara instan.",
                "Saat menekan tombol Back atau Tutup, aplikasi wajib masuk ke Gerbang PIN Lapis 1."
            )
        )
        val slide3 = buildSlideXml(
            "SINKRONISASI MEDIA & ANTI-GAGAL UPDATE APK",
            listOf(
                "Deteksi otomatis file bernama sama di brankas yang sama.",
                "Menimpa (overwrite) file versi lama secara mulus tanpa duplikasi database.",
                "Menjaga keutuhan Room Database & DataStore di setiap pembaruan versi aplikasi."
            )
        )

        writeZipMapToFile(
            targetFile,
            mapOf(
                "[Content_Types].xml" to """<?xml version="1.0" encoding="UTF-8"?><Types xmlns="http://schemas.openxmlformats.org/package/2006/content-types"><Default Extension="xml" ContentType="application/xml"/></Types>""",
                "ppt/slides/slide1.xml" to slide1,
                "ppt/slides/slide2.xml" to slide2,
                "ppt/slides/slide3.xml" to slide3
            )
        )
    }

    private fun writeZipMapToFile(targetFile: File, entries: Map<String, String>) {
        FileOutputStream(targetFile).use { fos ->
            ZipOutputStream(fos).use { zos ->
                for ((entryName, xmlContent) in entries) {
                    val entry = ZipEntry(entryName)
                    zos.putNextEntry(entry)
                    zos.write(xmlContent.toByteArray(Charsets.UTF_8))
                    zos.closeEntry()
                }
            }
        }
    }
}

/**
 * Komponen Utama Pembaca Dokumen Universal (PDF, DOCX, XLSX/CSV, PPTX, TXT/Code):
 * Digunakan baik di dalam Brankas (`VaultMediaViewerOverlay`) maupun saat membuka file
 * dari luar aplikasi lewat menu "Buka Dengan Arcanum" (`ArcanumExternalFileQuickPreviewOverlay`).
 */
@Composable
fun ArcanumUniversalDocumentViewport(
    item: VaultItemEntity,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val plainFile = remember(item.internalPlainPath) { File(item.internalPlainPath) }
    val docFormat = remember(item.extension) { ArcanumDocFormat.fromExtension(item.extension) }

    var isDarkReadingMode by remember { mutableStateOf(true) }
    var zoomScale by remember { mutableFloatStateOf(1f) }
    var panOffsetX by remember { mutableFloatStateOf(0f) }
    var isSearchBarVisible by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }
    var currentPageOrSectionIndex by remember(item.id, item.internalPlainPath) { mutableIntStateOf(0) }
    var printFeedbackMessage by remember { mutableStateOf<String?>(null) }

    // Parsed states loaded asynchronously on Dispatchers.IO
    var pdfBitmaps by remember(plainFile, docFormat) { mutableStateOf<List<Bitmap>>(emptyList()) }
    var parsedWordDoc by remember(plainFile, docFormat) { mutableStateOf<ParsedWordDocument?>(null) }
    var parsedWorkbook by remember(plainFile, docFormat) { mutableStateOf<ParsedExcelWorkbook?>(null) }
    var parsedPptxDeck by remember(plainFile, docFormat) { mutableStateOf<ParsedPptxDeck?>(null) }
    var isLoading by remember(plainFile, docFormat) { mutableStateOf(true) }

    LaunchedEffect(plainFile, docFormat, item.rawContentPreview) {
        isLoading = true
        withContext(Dispatchers.IO) {
            when (docFormat) {
                ArcanumDocFormat.PDF -> {
                    val rendered = mutableListOf<Bitmap>()
                    if (plainFile.exists() && plainFile.length() > 64L) {
                        try {
                            ParcelFileDescriptor.open(plainFile, ParcelFileDescriptor.MODE_READ_ONLY)?.use { pfd ->
                                PdfRenderer(pfd).use { renderer ->
                                    val count = renderer.pageCount.coerceAtMost(25)
                                    for (i in 0 until count) {
                                        renderer.openPage(i).use { page ->
                                            val targetW = (page.width * 1.6f).toInt().coerceIn(600, 1600)
                                            val targetH = (page.height * (targetW.toFloat() / page.width.coerceAtLeast(1))).toInt().coerceAtLeast(400)
                                            val bmp = Bitmap.createBitmap(targetW, targetH, Bitmap.Config.ARGB_8888)
                                            val canvas = Canvas(bmp)
                                            canvas.drawColor(android.graphics.Color.WHITE)
                                            page.render(bmp, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY)
                                            rendered.add(bmp)
                                        }
                                    }
                                }
                            }
                        } catch (_: Exception) {
                        }
                    }
                    pdfBitmaps = rendered
                    if (rendered.isEmpty()) {
                        parsedWordDoc = ArcanumOfficeDocumentParser.parseWordOrTextDocument(
                            plainFile,
                            "${item.name}.${item.extension}",
                            item.rawContentPreview
                        )
                    }
                }
                ArcanumDocFormat.WORD, ArcanumDocFormat.TEXT_CODE -> {
                    parsedWordDoc = ArcanumOfficeDocumentParser.parseWordOrTextDocument(
                        plainFile,
                        "${item.name}.${item.extension}",
                        item.rawContentPreview
                    )
                }
                ArcanumDocFormat.EXCEL -> {
                    parsedWorkbook = ArcanumOfficeDocumentParser.parseExcelWorkbook(
                        plainFile,
                        item.name,
                        item.rawContentPreview
                    )
                }
                ArcanumDocFormat.POWERPOINT -> {
                    parsedPptxDeck = ArcanumOfficeDocumentParser.parsePptxDeck(
                        plainFile,
                        item.name,
                        item.rawContentPreview
                    )
                }
            }
        }
        isLoading = false
    }

    // Note: Do NOT manually call bmp.recycle() in DisposableEffect for Compose-bound Bitmaps,
    // because RenderNode / RecordingCanvas / BitmapPainter may still draw the bitmap during
    // exit transitions or recomposition frames. Android's NativeAllocationRegistry frees them safely.

    val totalJumpUnits = when (docFormat) {
        ArcanumDocFormat.PDF -> pdfBitmaps.size.coerceAtLeast(1)
        ArcanumDocFormat.WORD, ArcanumDocFormat.TEXT_CODE -> (parsedWordDoc?.sectionTitles?.size ?: 1).coerceAtLeast(1)
        ArcanumDocFormat.EXCEL -> (parsedWorkbook?.sheets?.size ?: 1).coerceAtLeast(1)
        ArcanumDocFormat.POWERPOINT -> (parsedPptxDeck?.slides?.size ?: 1).coerceAtLeast(1)
    }

    val listState = rememberLazyListState()

    val surfaceBg = if (isDarkReadingMode) Color(0xFF10131C) else Color(0xFFF7F5EE)
    val cardBg = if (isDarkReadingMode) Color(0xFF181C28) else Color(0xFFFFFFFF)
    val primaryText = if (isDarkReadingMode) Color(0xFFF2F5FA) else Color(0xFF181A20)
    val secondaryText = if (isDarkReadingMode) Color(0xFF9EA6B8) else Color(0xFF5A6070)
    val borderTint = if (isDarkReadingMode) docFormat.accentColor.copy(alpha = 0.45f) else Color(0xFFD6D9E2)

    Card(
        modifier = modifier
            .fillMaxSize()
            .testTag("universal_document_viewer_card"),
        shape = RoundedCornerShape(26.dp),
        colors = CardDefaults.cardColors(containerColor = surfaceBg),
        border = BorderStroke(1.5.dp, borderTint)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // 1. TOP UNIVERSAL VIEWER CONTROL BAR (Format Badge, Search, Zoom, Dark/Paper Mode, Print/Export)
            Surface(
                color = if (isDarkReadingMode) VaultObsidian else Color(0xFFEDEFF5),
                shape = RoundedCornerShape(18.dp),
                border = BorderStroke(1.dp, docFormat.accentColor.copy(alpha = 0.4f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Format Badge + Page/Sheet Counter
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Surface(
                                color = docFormat.accentColor.copy(alpha = 0.18f),
                                shape = RoundedCornerShape(10.dp),
                                border = BorderStroke(1.dp, docFormat.accentColor)
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 9.dp, vertical = 5.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(5.dp)
                                ) {
                                    Icon(
                                        imageVector = when (docFormat) {
                                            ArcanumDocFormat.PDF -> Icons.Default.PictureAsPdf
                                            ArcanumDocFormat.EXCEL -> Icons.Default.GridOn
                                            ArcanumDocFormat.POWERPOINT -> Icons.Default.Slideshow
                                            else -> Icons.Default.Description
                                        },
                                        contentDescription = docFormat.badgeLabel,
                                        tint = docFormat.accentColor,
                                        modifier = Modifier.size(14.dp)
                                    )
                                    Text(
                                        text = docFormat.badgeLabel,
                                        fontFamily = JetBrainsMonoFontFamily,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.ExtraBold,
                                        color = docFormat.accentColor
                                    )
                                }
                            }

                            Text(
                                text = when (docFormat) {
                                    ArcanumDocFormat.PDF -> "Hal ${currentPageOrSectionIndex + 1}/$totalJumpUnits"
                                    ArcanumDocFormat.EXCEL -> "Sheet ${currentPageOrSectionIndex + 1}/$totalJumpUnits"
                                    ArcanumDocFormat.POWERPOINT -> "Slide ${currentPageOrSectionIndex + 1}/$totalJumpUnits"
                                    else -> "${parsedWordDoc?.wordCount ?: 0} kata"
                                },
                                fontFamily = JetBrainsMonoFontFamily,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = primaryText,
                                maxLines = 1
                            )
                        }

                        // Action Pills: Search, Zoom Cycle, Dark/Paper Mode, Print/Export
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            // Search Toggle
                            Surface(
                                modifier = Modifier
                                    .size(34.dp)
                                    .clip(CircleShape)
                                    .clickable { isSearchBarVisible = !isSearchBarVisible }
                                    .testTag("doc_viewer_search_toggle"),
                                color = if (isSearchBarVisible) docFormat.accentColor else VaultSlate.copy(alpha = 0.35f)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(
                                        imageVector = Icons.Default.Search,
                                        contentDescription = "Cari Kata di Dokumen",
                                        tint = if (isSearchBarVisible) Color.White else primaryText,
                                        modifier = Modifier.size(17.dp)
                                    )
                                }
                            }

                            // Zoom Cycle Pill (1x -> 1.5x -> 2x -> 3x -> 1x)
                            Surface(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(50))
                                    .clickable {
                                        zoomScale = when {
                                            zoomScale < 1.4f -> 1.5f
                                            zoomScale < 1.9f -> 2.0f
                                            zoomScale < 2.8f -> 3.0f
                                            else -> 1.0f
                                        }
                                        if (zoomScale == 1.0f) panOffsetX = 0f
                                    }
                                    .testTag("doc_viewer_zoom_button"),
                                color = if (zoomScale > 1.05f) VaultLime.copy(alpha = 0.22f) else VaultSlate.copy(alpha = 0.35f),
                                border = BorderStroke(1.dp, if (zoomScale > 1.05f) VaultLime else Color.Transparent)
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 9.dp, vertical = 6.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(3.dp)
                                ) {
                                    Icon(
                                        imageVector = if (zoomScale > 1.05f) Icons.Default.ZoomOutMap else Icons.Default.ZoomIn,
                                        contentDescription = "Zoom Dokumen",
                                        tint = if (zoomScale > 1.05f) VaultLime else primaryText,
                                        modifier = Modifier.size(14.dp)
                                    )
                                    Text(
                                        text = String.format(Locale.US, "%.1fx", zoomScale),
                                        fontFamily = JetBrainsMonoFontFamily,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.ExtraBold,
                                        color = if (zoomScale > 1.05f) VaultLime else primaryText
                                    )
                                }
                            }

                            // Dark Reading Mode vs Paper Light Mode Toggle
                            Surface(
                                modifier = Modifier
                                    .size(34.dp)
                                    .clip(CircleShape)
                                    .clickable { isDarkReadingMode = !isDarkReadingMode }
                                    .testTag("doc_viewer_dark_mode_toggle"),
                                color = VaultSlate.copy(alpha = 0.35f)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(
                                        imageVector = if (isDarkReadingMode) Icons.Default.DarkMode else Icons.Default.LightMode,
                                        contentDescription = if (isDarkReadingMode) "Mode Baca Gelap Aktif" else "Mode Kertas Terang Aktif",
                                        tint = if (isDarkReadingMode) VaultAmber else Color(0xFF007AFF),
                                        modifier = Modifier.size(17.dp)
                                    )
                                }
                            }

                            // Print / Export PDF Button
                            Surface(
                                modifier = Modifier
                                    .size(34.dp)
                                    .clip(CircleShape)
                                    .clickable {
                                        val ok = triggerDocumentPrintOrExport(
                                            context = context,
                                            item = item,
                                            docFormat = docFormat,
                                            plainFile = plainFile,
                                            parsedWordDoc = parsedWordDoc,
                                            parsedWorkbook = parsedWorkbook,
                                            parsedPptxDeck = parsedPptxDeck
                                        )
                                        printFeedbackMessage = if (ok) {
                                            "Dialog Cetak / Simpan PDF dibuka untuk ${item.name}.${item.extension}"
                                        } else {
                                            "Dokumen siap diekspor dari menu Ekspor di atas."
                                        }
                                    }
                                    .testTag("doc_viewer_print_button"),
                                color = docFormat.accentColor.copy(alpha = 0.22f)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(
                                        imageVector = Icons.Default.Print,
                                        contentDescription = "Cetak atau Ekspor Halaman PDF",
                                        tint = docFormat.accentColor,
                                        modifier = Modifier.size(17.dp)
                                    )
                                }
                            }
                        }
                    }

                    // Print feedback banner if triggered
                    if (printFeedbackMessage != null) {
                        Text(
                            text = printFeedbackMessage!!,
                            fontSize = 11.sp,
                            color = VaultLime,
                            fontWeight = FontWeight.SemiBold
                        )
                    }

                    // Expandable Search Bar inside document
                    AnimatedVisibility(visible = isSearchBarVisible) {
                        Surface(
                            color = cardBg,
                            shape = RoundedCornerShape(12.dp),
                            border = BorderStroke(1.dp, docFormat.accentColor.copy(alpha = 0.6f)),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 12.dp, vertical = 8.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Search,
                                    contentDescription = null,
                                    tint = docFormat.accentColor,
                                    modifier = Modifier.size(16.dp)
                                )
                                Box(modifier = Modifier.weight(1f)) {
                                    if (searchQuery.isEmpty()) {
                                        Text(
                                            text = "Cari kata atau angka di dalam dokumen...",
                                            fontSize = 12.sp,
                                            color = secondaryText
                                        )
                                    }
                                    BasicTextField(
                                        value = searchQuery,
                                        onValueChange = { searchQuery = it },
                                        singleLine = true,
                                        textStyle = TextStyle(
                                            color = primaryText,
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Medium
                                        ),
                                        cursorBrush = SolidColor(docFormat.accentColor),
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .testTag("doc_viewer_search_input")
                                    )
                                }
                                if (searchQuery.isNotEmpty()) {
                                    Icon(
                                        imageVector = Icons.Default.Close,
                                        contentDescription = "Hapus Pencarian",
                                        tint = secondaryText,
                                        modifier = Modifier
                                            .size(16.dp)
                                            .clickable { searchQuery = "" }
                                    )
                                }
                            }
                        }
                    }

                    // Page / Sheet / Slide / Section Quick-Jump Bar
                    if (totalJumpUnits > 1) {
                        LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            contentPadding = PaddingValues(vertical = 2.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            items(totalJumpUnits) { idx ->
                                val isSelected = idx == currentPageOrSectionIndex
                                val chipLabel = when (docFormat) {
                                    ArcanumDocFormat.PDF -> "Hal ${idx + 1}"
                                    ArcanumDocFormat.EXCEL -> parsedWorkbook?.sheets?.getOrNull(idx)?.sheetName ?: "Sheet ${idx + 1}"
                                    ArcanumDocFormat.POWERPOINT -> "Slide ${idx + 1}"
                                    else -> parsedWordDoc?.sectionTitles?.getOrNull(idx) ?: "Bagian ${idx + 1}"
                                }
                                Surface(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(50))
                                        .clickable {
                                            currentPageOrSectionIndex = idx
                                            if (docFormat == ArcanumDocFormat.PDF) {
                                                scope.launch {
                                                    listState.animateScrollToItem(idx)
                                                }
                                            }
                                        }
                                        .testTag("doc_viewer_jump_chip_$idx"),
                                    color = if (isSelected) docFormat.accentColor else cardBg,
                                    border = BorderStroke(
                                        1.dp,
                                        if (isSelected) docFormat.accentColor else borderTint
                                    )
                                ) {
                                    Text(
                                        text = chipLabel,
                                        fontSize = 11.sp,
                                        fontWeight = if (isSelected) FontWeight.ExtraBold else FontWeight.Medium,
                                        color = if (isSelected) Color.White else primaryText,
                                        modifier = Modifier.padding(horizontal = 11.dp, vertical = 5.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // 2. DOCUMENT VIEWPORT BODY WITH PINCH-TO-ZOOM GESTURE
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(18.dp))
                    .background(cardBg)
                    .pointerInput(docFormat) {
                        detectTransformGestures { _, pan, zoom, _ ->
                            val nextScale = (zoomScale * zoom).coerceIn(1f, 4f)
                            zoomScale = nextScale
                            if (nextScale > 1.02f) {
                                val maxDx = (size.width * (nextScale - 1f)) / 2f
                                panOffsetX = (panOffsetX + pan.x).coerceIn(-maxDx, maxDx)
                            } else {
                                panOffsetX = 0f
                            }
                        }
                    }
            ) {
                if (isLoading) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(
                            text = "Membuka & merender dokumen ${docFormat.badgeLabel}...",
                            color = secondaryText,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                } else {
                    when {
                        docFormat == ArcanumDocFormat.PDF && pdfBitmaps.isNotEmpty() -> {
                            PdfPagesViewport(
                                bitmaps = pdfBitmaps,
                                isDarkReadingMode = isDarkReadingMode,
                                zoomScale = zoomScale,
                                panOffsetX = panOffsetX,
                                listState = listState,
                                onCurrentPageChanged = { currentPageOrSectionIndex = it }
                            )
                        }
                        docFormat == ArcanumDocFormat.EXCEL && parsedWorkbook != null -> {
                            val activeSheet = parsedWorkbook!!.sheets.getOrElse(
                                currentPageOrSectionIndex.coerceIn(0, (parsedWorkbook!!.sheets.size - 1).coerceAtLeast(0))
                            ) {
                                ParsedExcelSheet("Sheet 1", emptyList(), 1)
                            }
                            ExcelSpreadsheetViewport(
                                sheet = activeSheet,
                                searchQuery = searchQuery,
                                isDarkReadingMode = isDarkReadingMode,
                                zoomScale = zoomScale,
                                primaryText = primaryText,
                                secondaryText = secondaryText,
                                accentColor = docFormat.accentColor
                            )
                        }
                        docFormat == ArcanumDocFormat.POWERPOINT && parsedPptxDeck != null -> {
                            val slides = parsedPptxDeck!!.slides
                            val safeIdx = currentPageOrSectionIndex.coerceIn(0, (slides.size - 1).coerceAtLeast(0))
                            PptxSlideDeckViewport(
                                slides = slides,
                                activeSlideIndex = safeIdx,
                                onSelectSlide = { currentPageOrSectionIndex = it },
                                searchQuery = searchQuery,
                                isDarkReadingMode = isDarkReadingMode,
                                zoomScale = zoomScale,
                                primaryText = primaryText,
                                secondaryText = secondaryText
                            )
                        }
                        else -> {
                            val doc = parsedWordDoc ?: ParsedWordDocument(
                                title = item.name,
                                blocks = listOf(DocxBlock.Paragraph(item.rawContentPreview)),
                                sectionTitles = listOf("Bagian 1"),
                                wordCount = 1
                            )
                            WordAndTextDocumentViewport(
                                document = doc,
                                searchQuery = searchQuery,
                                isDarkReadingMode = isDarkReadingMode,
                                zoomScale = zoomScale,
                                panOffsetX = panOffsetX,
                                primaryText = primaryText,
                                secondaryText = secondaryText,
                                accentColor = docFormat.accentColor
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun PdfPagesViewport(
    bitmaps: List<Bitmap>,
    isDarkReadingMode: Boolean,
    zoomScale: Float,
    panOffsetX: Float,
    listState: androidx.compose.foundation.lazy.LazyListState,
    onCurrentPageChanged: (Int) -> Unit
) {
    LaunchedEffect(listState.firstVisibleItemIndex) {
        onCurrentPageChanged(listState.firstVisibleItemIndex)
    }

    // Smart Dark Reading Mode color matrix for PDF pages (inverts white paper to dark obsidian while keeping contrast crisp)
    val darkInvertFilter = remember(isDarkReadingMode) {
        if (isDarkReadingMode) {
            val matrix = ColorMatrix(
                floatArrayOf(
                    -0.88f, 0f, 0f, 0f, 240f,
                    0f, -0.88f, 0f, 0f, 242f,
                    0f, 0f, -0.85f, 0f, 250f,
                    0f, 0f, 0f, 1f, 0f
                )
            )
            ColorFilter.colorMatrix(matrix)
        } else null
    }

    LazyColumn(
        state = listState,
        contentPadding = PaddingValues(12.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier
            .fillMaxSize()
            .graphicsLayer {
                scaleX = zoomScale
                scaleY = zoomScale
                translationX = panOffsetX
            }
    ) {
        itemsIndexed(bitmaps) { idx, bmp ->
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Halaman PDF #${idx + 1} dari ${bitmaps.size}",
                        fontFamily = JetBrainsMonoFontFamily,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFFF3B30)
                    )
                    Text(
                        text = if (isDarkReadingMode) "Mode Baca Gelap" else "Mode Kertas Asli",
                        fontSize = 10.sp,
                        color = Color.Gray
                    )
                }
                if (!bmp.isRecycled) {
                    val pageImageBitmap = remember(bmp) { bmp.asImageBitmap() }
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                    ) {
                        Image(
                            bitmap = pageImageBitmap,
                            contentDescription = "Halaman PDF ${idx + 1}",
                            contentScale = ContentScale.FillWidth,
                            colorFilter = darkInvertFilter,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun WordAndTextDocumentViewport(
    document: ParsedWordDocument,
    searchQuery: String,
    isDarkReadingMode: Boolean,
    zoomScale: Float,
    panOffsetX: Float,
    primaryText: Color,
    secondaryText: Color,
    accentColor: Color
) {
    val cleanQuery = searchQuery.trim()
    val filteredBlocks = remember(document.blocks, cleanQuery) {
        if (cleanQuery.isBlank()) {
            document.blocks
        } else {
            document.blocks.filter { block ->
                when (block) {
                    is DocxBlock.Heading -> block.text.contains(cleanQuery, ignoreCase = true)
                    is DocxBlock.Paragraph -> block.text.contains(cleanQuery, ignoreCase = true)
                    is DocxBlock.Table -> block.rows.any { r -> r.any { c -> c.contains(cleanQuery, ignoreCase = true) } }
                }
            }
        }
    }

    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        modifier = Modifier
            .fillMaxSize()
            .graphicsLayer {
                scaleX = zoomScale
                scaleY = zoomScale
                translationX = panOffsetX
            }
    ) {
        if (cleanQuery.isNotBlank()) {
            item {
                Surface(
                    color = accentColor.copy(alpha = 0.15f),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Ditemukan ${filteredBlocks.size} blok yang cocok dengan \"$cleanQuery\"",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = accentColor,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                    )
                }
            }
        }

        itemsIndexed(filteredBlocks) { _, block ->
            when (block) {
                is DocxBlock.Heading -> {
                    val fontSize = when (block.level) {
                        1 -> 19.sp
                        2 -> 16.sp
                        else -> 14.sp
                    }
                    Column(modifier = Modifier.padding(top = 6.dp, bottom = 2.dp)) {
                        Text(
                            text = highlightQueryAnnotated(block.text, cleanQuery, accentColor),
                            fontSize = fontSize,
                            fontWeight = FontWeight.ExtraBold,
                            color = if (block.level == 1) accentColor else primaryText
                        )
                        if (block.level == 1) {
                            Spacer(modifier = Modifier.height(4.dp))
                            HorizontalDivider(color = accentColor.copy(alpha = 0.35f))
                        }
                    }
                }
                is DocxBlock.Paragraph -> {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        if (block.isBullet) {
                            Text(
                                text = "•",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = accentColor
                            )
                        }
                        Text(
                            text = highlightQueryAnnotated(block.text, cleanQuery, accentColor),
                            fontSize = 13.5.sp,
                            lineHeight = 20.sp,
                            fontWeight = if (block.isBold) FontWeight.Bold else FontWeight.Normal,
                            fontStyle = if (block.isItalic) FontStyle.Italic else FontStyle.Normal,
                            color = primaryText,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
                is DocxBlock.Table -> {
                    val hScroll = rememberScrollState()
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        border = BorderStroke(1.dp, accentColor.copy(alpha = 0.4f)),
                        color = if (isDarkReadingMode) Color(0xFF121622) else Color(0xFFF3F5FA),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .horizontalScroll(hScroll)
                                .padding(8.dp)
                        ) {
                            block.rows.forEachIndexed { rIdx, rowCells ->
                                val isHeader = rIdx == 0
                                Row(
                                    modifier = Modifier
                                        .background(
                                            if (isHeader) accentColor.copy(alpha = 0.2f)
                                            else if (rIdx % 2 == 1) Color.White.copy(alpha = 0.03f)
                                            else Color.Transparent
                                        )
                                        .padding(vertical = 6.dp, horizontal = 4.dp),
                                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                                ) {
                                    rowCells.forEach { cellText ->
                                        Text(
                                            text = highlightQueryAnnotated(cellText, cleanQuery, accentColor),
                                            fontSize = 12.sp,
                                            fontWeight = if (isHeader) FontWeight.ExtraBold else FontWeight.Medium,
                                            color = if (isHeader) accentColor else primaryText,
                                            modifier = Modifier.widthIn(min = 110.dp, max = 220.dp)
                                        )
                                    }
                                }
                                if (rIdx < block.rows.lastIndex) {
                                    HorizontalDivider(color = secondaryText.copy(alpha = 0.2f))
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ExcelSpreadsheetViewport(
    sheet: ParsedExcelSheet,
    searchQuery: String,
    isDarkReadingMode: Boolean,
    zoomScale: Float,
    primaryText: Color,
    secondaryText: Color,
    accentColor: Color
) {
    var selectedCellCoord by remember(sheet.sheetName) { mutableStateOf("A1") }
    var selectedCellValue by remember(sheet.sheetName) {
        mutableStateOf(sheet.rows.firstOrNull()?.firstOrNull() ?: "")
    }
    val cleanQuery = searchQuery.trim()

    // Compute quick numeric summary (SUM & AVG) across the active sheet
    val numericStats = remember(sheet.rows) {
        val nums = mutableListOf<Double>()
        sheet.rows.forEach { r ->
            r.forEach { cell ->
                val cleaned = cell.replace(",", "").replace("Rp", "").replace("$", "").trim()
                cleaned.toDoubleOrNull()?.let { nums.add(it) }
            }
        }
        if (nums.isNotEmpty()) {
            val sum = nums.sum()
            val avg = sum / nums.size
            "SUM: ${String.format(Locale.US, "%,.0f", sum)} • AVG: ${String.format(Locale.US, "%,.1f", avg)} (${nums.size} sel angka)"
        } else {
            "${sheet.rows.size} Baris × ${sheet.maxColumns} Kolom"
        }
    }

    val horizontalScrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Formula / Active Cell Inspector Bar + Sheet Numeric Summary
        Surface(
            color = if (isDarkReadingMode) Color(0xFF111622) else Color(0xFFEEF3EE),
            shape = RoundedCornerShape(12.dp),
            border = BorderStroke(1.dp, accentColor.copy(alpha = 0.45f)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)) {
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
                        Surface(
                            color = accentColor,
                            shape = RoundedCornerShape(6.dp)
                        ) {
                            Text(
                                text = selectedCellCoord,
                                fontFamily = JetBrainsMonoFontFamily,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = Color.White,
                                modifier = Modifier.padding(horizontal = 7.dp, vertical = 2.dp)
                            )
                        }
                        Text(
                            text = selectedCellValue.ifBlank { "(Sel Kosong)" },
                            fontFamily = JetBrainsMonoFontFamily,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = primaryText,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
                Spacer(modifier = Modifier.height(3.dp))
                Text(
                    text = numericStats,
                    fontFamily = JetBrainsMonoFontFamily,
                    fontSize = 10.sp,
                    color = accentColor
                )
            }
        }

        // 2D Spreadsheet Grid (Column Headers A, B, C... + Row Numbers 1, 2, 3...)
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .border(1.dp, accentColor.copy(alpha = 0.35f), RoundedCornerShape(12.dp))
                .horizontalScroll(horizontalScrollState)
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxHeight()
                    .graphicsLayer {
                        scaleX = zoomScale
                        scaleY = zoomScale
                    }
            ) {
                // Column Letter Header Row (A, B, C, D...)
                item {
                    Row(
                        modifier = Modifier
                            .background(if (isDarkReadingMode) Color(0xFF1F2738) else Color(0xFFDCE6DC))
                    ) {
                        Box(
                            modifier = Modifier
                                .width(44.dp)
                                .height(32.dp)
                                .border(0.5.dp, secondaryText.copy(alpha = 0.3f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "#",
                                fontFamily = JetBrainsMonoFontFamily,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = secondaryText
                            )
                        }
                        for (c in 0 until sheet.maxColumns) {
                            val colLetter = ArcanumOfficeDocumentParser.excelColumnLabel(c)
                            Box(
                                modifier = Modifier
                                    .width(132.dp)
                                    .height(32.dp)
                                    .border(0.5.dp, secondaryText.copy(alpha = 0.3f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = colLetter,
                                    fontFamily = JetBrainsMonoFontFamily,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = accentColor
                                )
                            }
                        }
                    }
                }

                // Data Rows
                itemsIndexed(sheet.rows) { rIdx, rowCells ->
                    val isFirstRowHeader = rIdx == 0
                    val rowMatchesSearch = cleanQuery.isNotBlank() &&
                        rowCells.any { it.contains(cleanQuery, ignoreCase = true) }

                    Row(
                        modifier = Modifier.background(
                            when {
                                rowMatchesSearch -> accentColor.copy(alpha = 0.18f)
                                isFirstRowHeader -> accentColor.copy(alpha = 0.1f)
                                rIdx % 2 == 1 -> if (isDarkReadingMode) Color(0xFF141926) else Color(0xFFF6F8F6)
                                else -> Color.Transparent
                            }
                        )
                    ) {
                        // Row number cell
                        Box(
                            modifier = Modifier
                                .width(44.dp)
                                .height(36.dp)
                                .border(0.5.dp, secondaryText.copy(alpha = 0.25f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "${rIdx + 1}",
                                fontFamily = JetBrainsMonoFontFamily,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = secondaryText
                            )
                        }

                        for (cIdx in 0 until sheet.maxColumns) {
                            val cellText = rowCells.getOrNull(cIdx) ?: ""
                            val coord = "${ArcanumOfficeDocumentParser.excelColumnLabel(cIdx)}${rIdx + 1}"
                            val isSelected = selectedCellCoord == coord
                            Box(
                                modifier = Modifier
                                    .width(132.dp)
                                    .height(36.dp)
                                    .border(
                                        width = if (isSelected) 1.5.dp else 0.5.dp,
                                        color = if (isSelected) accentColor else secondaryText.copy(alpha = 0.25f)
                                    )
                                    .clickable {
                                        selectedCellCoord = coord
                                        selectedCellValue = cellText
                                    }
                                    .padding(horizontal = 8.dp),
                                contentAlignment = Alignment.CenterStart
                            ) {
                                Text(
                                    text = highlightQueryAnnotated(cellText, cleanQuery, accentColor),
                                    fontSize = 11.5.sp,
                                    fontWeight = if (isFirstRowHeader || isSelected) FontWeight.ExtraBold else FontWeight.Normal,
                                    color = if (isFirstRowHeader) accentColor else primaryText,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun PptxSlideDeckViewport(
    slides: List<ParsedPptxSlide>,
    activeSlideIndex: Int,
    onSelectSlide: (Int) -> Unit,
    searchQuery: String,
    isDarkReadingMode: Boolean,
    zoomScale: Float,
    primaryText: Color,
    secondaryText: Color
) {
    val activeSlide = slides.getOrElse(activeSlideIndex) {
        ParsedPptxSlide(1, "Slide 1", emptyList(), "")
    }
    val accentOrange = Color(0xFFFF9500)
    val cleanQuery = searchQuery.trim()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(14.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // 16:9 Presentation Slide Canvas Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(16f / 10f)
                .graphicsLayer {
                    scaleX = zoomScale
                    scaleY = zoomScale
                },
            shape = RoundedCornerShape(22.dp),
            border = BorderStroke(2.dp, accentOrange.copy(alpha = 0.65f)),
            elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        if (isDarkReadingMode) {
                            Brush.linearGradient(
                                listOf(Color(0xFF141829), Color(0xFF1F172B), Color(0xFF0E1320))
                            )
                        } else {
                            Brush.linearGradient(
                                listOf(Color(0xFFFFFAF2), Color(0xFFFFF3E0), Color(0xFFFFFFFF))
                            )
                        }
                    )
                    .padding(20.dp)
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Surface(
                                color = accentOrange.copy(alpha = 0.2f),
                                shape = RoundedCornerShape(50)
                            ) {
                                Text(
                                    text = "SLIDE ${activeSlide.slideNumber} DARI ${slides.size}",
                                    fontFamily = JetBrainsMonoFontFamily,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = accentOrange,
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                                )
                            }
                            Text(
                                text = "ARCANUM KEYNOTE DECK",
                                fontFamily = JetBrainsMonoFontFamily,
                                fontSize = 9.sp,
                                color = secondaryText
                            )
                        }

                        Text(
                            text = highlightQueryAnnotated(activeSlide.title, cleanQuery, accentOrange),
                            fontSize = 17.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = primaryText,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )

                        HorizontalDivider(color = accentOrange.copy(alpha = 0.35f))

                        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                            activeSlide.bulletPoints.take(5).forEach { point ->
                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                    verticalAlignment = Alignment.Top
                                ) {
                                    Text(
                                        text = "▸",
                                        color = accentOrange,
                                        fontWeight = FontWeight.ExtraBold,
                                        fontSize = 13.sp
                                    )
                                    Text(
                                        text = highlightQueryAnnotated(point, cleanQuery, accentOrange),
                                        color = primaryText,
                                        fontSize = 12.5.sp,
                                        lineHeight = 17.sp
                                    )
                                }
                            }
                        }
                    }

                    // Bottom Slide Navigation Controls
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            modifier = Modifier
                                .clip(RoundedCornerShape(50))
                                .clickable(enabled = activeSlideIndex > 0) {
                                    onSelectSlide((activeSlideIndex - 1).coerceAtLeast(0))
                                },
                            color = if (activeSlideIndex > 0) accentOrange.copy(alpha = 0.22f) else Color.Gray.copy(alpha = 0.12f)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.NavigateBefore,
                                    contentDescription = "Slide Sebelumnya",
                                    tint = if (activeSlideIndex > 0) accentOrange else secondaryText,
                                    modifier = Modifier.size(16.dp)
                                )
                                Text(
                                    text = "Sebelumnya",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (activeSlideIndex > 0) accentOrange else secondaryText
                                )
                            }
                        }

                        Surface(
                            modifier = Modifier
                                .clip(RoundedCornerShape(50))
                                .clickable(enabled = activeSlideIndex < slides.lastIndex) {
                                    onSelectSlide((activeSlideIndex + 1).coerceAtMost(slides.lastIndex))
                                },
                            color = if (activeSlideIndex < slides.lastIndex) accentOrange else Color.Gray.copy(alpha = 0.12f)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Berikutnya",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (activeSlideIndex < slides.lastIndex) Color.White else secondaryText
                                )
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.NavigateNext,
                                    contentDescription = "Slide Berikutnya",
                                    tint = if (activeSlideIndex < slides.lastIndex) Color.White else secondaryText,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }
                    }
                }
            }
        }

        // Speaker Notes & Full Slide Text Card
        if (activeSlide.speakerNotes.isNotBlank() || activeSlide.bulletPoints.size > 5) {
            Surface(
                color = if (isDarkReadingMode) VaultObsidian else Color(0xFFF3F4F8),
                shape = RoundedCornerShape(14.dp),
                border = BorderStroke(1.dp, accentOrange.copy(alpha = 0.35f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(
                        text = "CATATAN PEMBICARA & DETAIL SLIDE #${activeSlide.slideNumber}",
                        fontFamily = JetBrainsMonoFontFamily,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = accentOrange
                    )
                    Text(
                        text = activeSlide.speakerNotes.ifBlank {
                            activeSlide.bulletPoints.joinToString("\n• ", prefix = "• ")
                        },
                        fontSize = 12.sp,
                        color = primaryText
                    )
                }
            }
        }
    }
}

private fun highlightQueryAnnotated(
    text: String,
    query: String,
    highlightColor: Color
): AnnotatedString {
    if (query.isBlank()) return AnnotatedString(text)
    return buildAnnotatedString {
        var start = 0
        val lowerText = text.lowercase()
        val lowerQ = query.lowercase()
        while (start < text.length) {
            val idx = lowerText.indexOf(lowerQ, start)
            if (idx < 0) {
                append(text.substring(start))
                break
            }
            if (idx > start) {
                append(text.substring(start, idx))
            }
            withStyle(
                SpanStyle(
                    background = highlightColor.copy(alpha = 0.35f),
                    color = Color.White,
                    fontWeight = FontWeight.ExtraBold
                )
            ) {
                append(text.substring(idx, idx + query.length))
            }
            start = idx + query.length
        }
    }
}

private fun triggerDocumentPrintOrExport(
    context: Context,
    item: VaultItemEntity,
    docFormat: ArcanumDocFormat,
    plainFile: File,
    parsedWordDoc: ParsedWordDocument?,
    parsedWorkbook: ParsedExcelWorkbook?,
    parsedPptxDeck: ParsedPptxDeck?
): Boolean {
    return try {
        val printManager = context.getSystemService(Context.PRINT_SERVICE) as? PrintManager ?: return false
        val jobName = "Arcanum_${item.name}_${item.extension}"

        val linesToPrint = when (docFormat) {
            ArcanumDocFormat.WORD, ArcanumDocFormat.TEXT_CODE -> {
                parsedWordDoc?.blocks?.flatMap { b ->
                    when (b) {
                        is DocxBlock.Heading -> listOf("", "=== ${b.text} ===")
                        is DocxBlock.Paragraph -> listOf(if (b.isBullet) "• ${b.text}" else b.text)
                        is DocxBlock.Table -> b.rows.map { r -> r.joinToString(" | ") }
                    }
                } ?: listOf(item.rawContentPreview)
            }
            ArcanumDocFormat.EXCEL -> {
                parsedWorkbook?.sheets?.flatMap { s ->
                    listOf("=== SHEET: ${s.sheetName} ===") + s.rows.map { r -> r.joinToString("  |  ") }
                } ?: listOf(item.rawContentPreview)
            }
            ArcanumDocFormat.POWERPOINT -> {
                parsedPptxDeck?.slides?.flatMap { sl ->
                    listOf("=== SLIDE ${sl.slideNumber}: ${sl.title} ===") + sl.bulletPoints.map { "• $it" }
                } ?: listOf(item.rawContentPreview)
            }
            ArcanumDocFormat.PDF -> emptyList()
        }

        val adapter = object : PrintDocumentAdapter() {
            override fun onLayout(
                oldAttributes: PrintAttributes?,
                newAttributes: PrintAttributes?,
                cancellationSignal: CancellationSignal?,
                callback: LayoutResultCallback?,
                extras: Bundle?
            ) {
                if (cancellationSignal?.isCanceled == true) {
                    callback?.onLayoutCancelled()
                    return
                }
                val info = PrintDocumentInfo.Builder("${item.name}.pdf")
                    .setContentType(PrintDocumentInfo.CONTENT_TYPE_DOCUMENT)
                    .setPageCount(PrintDocumentInfo.PAGE_COUNT_UNKNOWN)
                    .build()
                callback?.onLayoutFinished(info, true)
            }

            override fun onWrite(
                pages: Array<out PageRange>?,
                destination: ParcelFileDescriptor?,
                cancellationSignal: CancellationSignal?,
                callback: WriteResultCallback?
            ) {
                if (destination == null) {
                    callback?.onWriteFailed("Destination null")
                    return
                }
                try {
                    if (docFormat == ArcanumDocFormat.PDF && plainFile.exists() && plainFile.length() > 64L) {
                        plainFile.inputStream().use { input ->
                            FileOutputStream(destination.fileDescriptor).use { output ->
                                input.copyTo(output)
                            }
                        }
                    } else {
                        val pdfDoc = PdfDocument()
                        val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                            color = android.graphics.Color.BLACK
                            textSize = 11f
                        }
                        val chunkedPages = linesToPrint.ifEmpty { listOf(item.name) }.chunked(42)
                        chunkedPages.forEachIndexed { pIdx, pageLines ->
                            val pInfo = PdfDocument.PageInfo.Builder(595, 842, pIdx + 1).create()
                            val p = pdfDoc.startPage(pInfo)
                            var y = 48f
                            for (ln in pageLines) {
                                p.canvas.drawText(ln.take(95), 40f, y, paint)
                                y += 18f
                            }
                            pdfDoc.finishPage(p)
                        }
                        FileOutputStream(destination.fileDescriptor).use { out ->
                            pdfDoc.writeTo(out)
                        }
                        pdfDoc.close()
                    }
                    callback?.onWriteFinished(arrayOf(PageRange.ALL_PAGES))
                } catch (e: Exception) {
                    callback?.onWriteFailed(e.message)
                }
            }
        }

        printManager.print(jobName, adapter, PrintAttributes.Builder().build())
        true
    } catch (_: Exception) {
        false
    }
}

/**
 * Layar Pratinjau Terisolasi untuk Fitur "Buka Dengan Arcanum" (`Intent.ACTION_VIEW`):
 * - Langsung membuka dan menampilkan file eksternal (PDF, DOCX, Excel, PPTX, TXT, Gambar, Video, Audio).
 * - Ketika pengguna menekan tombol Back atau tombol Tutup, sistem WAJIB memanggil `onCloseAndRequirePinLock()`
 *   yang otomatis mengunci sesi ke Gerbang PIN Lapis 1 (`AuthStage.PIN_LAYER_1`) sehingga 0% celah bypass brankas!
 * - Menyediakan tombol "Simpan & Timpa ke Brankas" untuk mengenkripsi file tersebut ke Brankas #1 (auto-overwrite jika sudah ada).
 */
@Composable
fun ArcanumExternalFileQuickPreviewOverlay(
    request: ExternalFilePreviewRequest,
    onSaveAndOverwriteToVault: () -> Unit,
    onCloseAndRequirePinLock: () -> Unit
) {
    // Hardware Back press MUST route directly to PIN Layer 1 lock screen (Zero Bypass)
    BackHandler(enabled = true) {
        onCloseAndRequirePinLock()
    }

    val extLower = request.extension.lowercase()
    val category = remember(extLower) {
        when (extLower) {
            "jpg", "jpeg", "png", "webp", "gif", "bmp", "heic" -> VaultCategory.IMAGE
            "mp4", "mkv", "mov", "webm", "3gp", "m4v" -> VaultCategory.VIDEO
            "mp3", "wav", "flac", "m4a", "aac", "ogg" -> VaultCategory.AUDIO
            else -> VaultCategory.DOCUMENT
        }
    }

    val transientEntity = remember(request) {
        VaultItemEntity(
            id = -901,
            vaultProfileId = 1,
            name = request.displayName.substringBeforeLast('.', request.displayName),
            extension = request.extension,
            category = category.name,
            internalPlainPath = request.tempFile.absolutePath,
            internalNyxPath = "",
            sizeBytes = request.sizeBytes,
            paddedSizeBytes = request.sizeBytes,
            isEncrypted = false,
            cipherAlgorithm = "EXTERNAL_PREVIEW_ISOLATED",
            checksumPreview = "OPEN_WITH_ARCANUM",
            rawContentPreview = "Pratinjau File Eksternal: ${request.displayName}",
            encryptedHexPreview = "",
            mediaUriString = request.sourceUriString
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF070A12))
            .statusBarsPadding()
            .navigationBarsPadding()
            .testTag("external_file_quick_preview_overlay")
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // Top iOS Glass Bar with Security Notice + Save to Vault + Close->PIN Lock
            Surface(
                color = Color(0xFF141926),
                shape = RoundedCornerShape(22.dp),
                border = BorderStroke(1.5.dp, Color(0xFF0A84FF).copy(alpha = 0.55f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 12.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
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
                            Surface(
                                modifier = Modifier
                                    .size(38.dp)
                                    .clip(CircleShape)
                                    .instantTouchPress { onCloseAndRequirePinLock() }
                                    .testTag("external_preview_back_to_pin_button"),
                                color = Color(0xFFFF3B30).copy(alpha = 0.2f),
                                border = BorderStroke(1.dp, Color(0xFFFF3B30))
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                        contentDescription = "Tutup & Kunci PIN",
                                        tint = Color(0xFFFF3B30),
                                        modifier = Modifier.size(19.dp)
                                    )
                                }
                            }

                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = request.displayName,
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = Color.White,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Text(
                                    text = "Buka Dengan Arcanum • Tekan Back otomatis masuk Gerbang PIN",
                                    fontFamily = JetBrainsMonoFontFamily,
                                    fontSize = 10.sp,
                                    color = VaultLime,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            // Save & Overwrite to Vault Button
                            Surface(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(50))
                                    .instantTouchPress { onSaveAndOverwriteToVault() }
                                    .testTag("external_preview_save_to_vault_button"),
                                color = Color(0xFF34C759)
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(5.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Download,
                                        contentDescription = "Simpan ke Brankas",
                                        tint = Color.Black,
                                        modifier = Modifier.size(15.dp)
                                    )
                                    Text(
                                        text = "Simpan ke Brankas",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.ExtraBold,
                                        color = Color.Black
                                    )
                                }
                            }

                            // Lock to PIN Gate Button
                            Surface(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(50))
                                    .instantTouchPress { onCloseAndRequirePinLock() }
                                    .testTag("external_preview_lock_pin_button"),
                                color = VaultCharcoal,
                                border = BorderStroke(1.dp, VaultAmber)
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Lock,
                                        contentDescription = "Masuk PIN",
                                        tint = VaultAmber,
                                        modifier = Modifier.size(14.dp)
                                    )
                                    Text(
                                        text = "Kunci PIN",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = VaultAmber
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Main Preview Viewport (Image, Video/Audio, or Universal Document PDF/DOCX/XLSX/PPTX)
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                when (category) {
                    VaultCategory.IMAGE -> {
                        var imgZoom by remember { mutableFloatStateOf(1f) }
                        Card(
                            modifier = Modifier.fillMaxSize(),
                            shape = RoundedCornerShape(24.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.Black)
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .pointerInput(Unit) {
                                        detectTransformGestures { _, _, zoom, _ ->
                                            imgZoom = (imgZoom * zoom).coerceIn(1f, 4f)
                                        }
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                AsyncImage(
                                    model = request.tempFile,
                                    contentDescription = request.displayName,
                                    contentScale = ContentScale.Fit,
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .graphicsLayer {
                                            scaleX = imgZoom
                                            scaleY = imgZoom
                                        }
                                )
                            }
                        }
                    }
                    VaultCategory.VIDEO, VaultCategory.AUDIO -> {
                        Card(
                            modifier = Modifier.fillMaxSize(),
                            shape = RoundedCornerShape(24.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.Black)
                        ) {
                            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                                AndroidView(
                                    factory = { ctx ->
                                        VideoView(ctx).apply {
                                            setVideoPath(request.tempFile.absolutePath)
                                            setOnPreparedListener { mp ->
                                                mp.isLooping = true
                                                start()
                                            }
                                        }
                                    },
                                    modifier = Modifier.fillMaxSize()
                                )
                            }
                        }
                    }
                    else -> {
                        ArcanumUniversalDocumentViewport(
                            item = transientEntity,
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }
            }
        }
    }
}
