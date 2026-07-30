package com.example.utils

import android.content.Context
import android.content.Intent
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.graphics.pdf.PdfDocument
import android.net.Uri
import android.os.Environment
import android.text.Layout
import android.text.StaticLayout
import android.text.TextPaint
import android.widget.Toast
import androidx.core.content.FileProvider
import com.example.data.database.PoemNote
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object PdfExporter {

    private const val PAGE_WIDTH = 595 // A4 width in points (72 dpi)
    private const val PAGE_HEIGHT = 842 // A4 height in points (72 dpi)
    private const val MARGIN = 40

    fun exportSingleNotePdf(context: Context, note: PoemNote) {
        exportNotesToPdf(context, listOf(note), "Poem_${note.title.replace("\\s+".toRegex(), "_")}")
    }

    fun exportAllNotesPdf(context: Context, notes: List<PoemNote>) {
        if (notes.isEmpty()) {
            Toast.makeText(context, "এক্সপোর্ট করার মতো কোনো নোট নেই", Toast.LENGTH_SHORT).show()
            return
        }
        exportNotesToPdf(context, notes, "All_Poems_Backup")
    }

    private fun exportNotesToPdf(context: Context, notes: List<PoemNote>, baseFileName: String) {
        val pdfDocument = PdfDocument()
        val exportDateStr = SimpleDateFormat("dd MMMM, yyyy - hh:mm a", Locale("bn")).format(Date())

        val titlePaint = TextPaint().apply {
            color = Color.parseColor("#D4A017") // Gold primary
            textSize = 20f
            isAntiAlias = true
            typeface = Typeface.create(Typeface.SERIF, Typeface.BOLD)
        }

        val datePaint = TextPaint().apply {
            color = Color.parseColor("#64748B")
            textSize = 10f
            isAntiAlias = true
            typeface = Typeface.SERIF
        }

        val bodyPaint = TextPaint().apply {
            color = Color.parseColor("#1E293B")
            textSize = 14f
            isAntiAlias = true
            typeface = Typeface.SERIF
        }

        val headerPaint = TextPaint().apply {
            color = Color.parseColor("#D4A017")
            textSize = 12f
            isAntiAlias = true
            typeface = Typeface.create(Typeface.SERIF, Typeface.BOLD)
        }

        val linePaint = Paint().apply {
            color = Color.parseColor("#D4A017")
            strokeWidth = 1.5f
            isAntiAlias = true
        }

        val footerPaint = TextPaint().apply {
            color = Color.parseColor("#94A3B8")
            textSize = 9f
            isAntiAlias = true
            typeface = Typeface.SERIF
        }

        var currentPageNum = 1
        var pageInfo = PdfDocument.PageInfo.Builder(PAGE_WIDTH, PAGE_HEIGHT, currentPageNum).create()
        var page = pdfDocument.startPage(pageInfo)
        var canvas = page.canvas

        fun drawHeaderAndFooter(c: Canvas, pageIndex: Int) {
            // Header
            c.drawText("কবিতা নোট (ব্রককোলি)", MARGIN.toFloat(), MARGIN.toFloat(), headerPaint)
            c.drawText(exportDateStr, (PAGE_WIDTH - MARGIN - 180).toFloat(), MARGIN.toFloat(), datePaint)
            c.drawLine(MARGIN.toFloat(), (MARGIN + 10).toFloat(), (PAGE_WIDTH - MARGIN).toFloat(), (MARGIN + 10).toFloat(), linePaint)

            // Footer
            val footerText = "পৃষ্ঠা - ${pageIndex.toBengaliNumerals()}"
            c.drawText(footerText, (PAGE_WIDTH / 2 - 20).toFloat(), (PAGE_HEIGHT - MARGIN + 15).toFloat(), footerPaint)
        }

        drawHeaderAndFooter(canvas, currentPageNum)
        var currentY = MARGIN + 35

        for ((index, note) in notes.withIndex()) {
            if (index > 0 && currentY > MARGIN + 100) {
                // Finish previous page and start new page for each note
                pdfDocument.finishPage(page)
                currentPageNum++
                pageInfo = PdfDocument.PageInfo.Builder(PAGE_WIDTH, PAGE_HEIGHT, currentPageNum).create()
                page = pdfDocument.startPage(pageInfo)
                canvas = page.canvas
                drawHeaderAndFooter(canvas, currentPageNum)
                currentY = MARGIN + 35
            }

            // Note Title
            val titleText = if (note.title.isBlank()) "শিরোনামহীন" else note.title
            val noteDateText = formatBengaliDateTime(note.updatedAt)

            canvas.drawText(titleText, MARGIN.toFloat(), currentY.toFloat(), titlePaint)
            currentY += 22
            canvas.drawText(noteDateText, MARGIN.toFloat(), currentY.toFloat(), datePaint)
            currentY += 20

            // Note Content StaticLayout
            val contentWidth = PAGE_WIDTH - (MARGIN * 2)
            bodyPaint.textSize = note.fontSizeSp.toFloat().coerceIn(12f, 22f)
            val staticLayout = StaticLayout.Builder
                .obtain(note.content, 0, note.content.length, bodyPaint, contentWidth)
                .setAlignment(
                    when (note.textAlign) {
                        "CENTER" -> Layout.Alignment.ALIGN_CENTER
                        "RIGHT" -> Layout.Alignment.ALIGN_OPPOSITE
                        else -> Layout.Alignment.ALIGN_NORMAL
                    }
                )
                .setLineSpacing(0f, note.lineSpacingMultiplier)
                .build()

            // Check if staticLayout fits on current page
            if (currentY + staticLayout.height > PAGE_HEIGHT - MARGIN - 30) {
                // If content is very long, draw row by row or split page
                pdfDocument.finishPage(page)
                currentPageNum++
                pageInfo = PdfDocument.PageInfo.Builder(PAGE_WIDTH, PAGE_HEIGHT, currentPageNum).create()
                page = pdfDocument.startPage(pageInfo)
                canvas = page.canvas
                drawHeaderAndFooter(canvas, currentPageNum)
                currentY = MARGIN + 35
            }

            canvas.save()
            canvas.translate(MARGIN.toFloat(), currentY.toFloat())
            staticLayout.draw(canvas)
            canvas.restore()

            currentY += staticLayout.height + 40
        }

        pdfDocument.finishPage(page)

        // Save PDF to Downloads or Documents directory
        try {
            val fileName = "${baseFileName}_${System.currentTimeMillis()}.pdf"
            val downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
            if (!downloadsDir.exists()) downloadsDir.mkdirs()

            val file = File(downloadsDir, fileName)
            FileOutputStream(file).use { out ->
                pdfDocument.writeTo(out)
            }
            pdfDocument.close()

            Toast.makeText(context, "PDF সংরক্ষিত হয়েছে: Downloads/$fileName", Toast.LENGTH_LONG).show()

            // Trigger view / share file intent
            val fileUri: Uri = FileProvider.getUriForFile(
                context,
                "${context.packageName}.fileprovider",
                file
            )
            val openIntent = Intent(Intent.ACTION_VIEW).apply {
                setDataAndType(fileUri, "application/pdf")
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }
            context.startActivity(Intent.createChooser(openIntent, "PDF ওপেন করুন"))

        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(context, "PDF এক্সপোর্টে ত্রুটি: ${e.localizedMessage}", Toast.LENGTH_SHORT).show()
            try { pdfDocument.close() } catch (_: Exception) {}
        }
    }
}
