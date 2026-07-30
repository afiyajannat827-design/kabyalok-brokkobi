package com.example.utils

import android.content.Context
import android.os.Environment
import android.widget.Toast
import com.example.data.database.PoemNote
import org.json.JSONArray
import org.json.JSONObject
import java.io.File
import java.io.FileOutputStream

object BackupRestoreHelper {

    fun exportToJson(context: Context, notes: List<PoemNote>): String? {
        if (notes.isEmpty()) {
            Toast.makeText(context, "ব্যাকআপ নেওয়ার মতো কোনো নোট নেই", Toast.LENGTH_SHORT).show()
            return null
        }

        try {
            val jsonArray = JSONArray()
            for (note in notes) {
                val obj = JSONObject().apply {
                    put("id", note.id)
                    put("title", note.title)
                    put("content", note.content)
                    put("stanzaCount", note.stanzaCount)
                    put("wordCount", note.wordCount)
                    put("category", note.category)
                    put("isPinned", note.isPinned)
                    put("isLocked", note.isLocked)
                    put("isArchived", note.isArchived)
                    put("isTrash", note.isTrash)
                    put("isHidden", note.isHidden)
                    put("groupId", note.groupId ?: JSONObject.NULL)
                    put("isBold", note.isBold)
                    put("isItalic", note.isItalic)
                    put("isUnderline", note.isUnderline)
                    put("isStrikethrough", note.isStrikethrough)
                    put("textAlign", note.textAlign)
                    put("lineBreakMode", note.lineBreakMode)
                    put("fontSizeSp", note.fontSizeSp)
                    put("lineSpacingMultiplier", note.lineSpacingMultiplier)
                    put("titleColorHex", note.titleColorHex)
                    put("textColorHex", note.textColorHex)
                    put("fontFamilyName", note.fontFamilyName)
                    put("fontCategory", note.fontCategory)
                    put("bgThemeName", note.bgThemeName)
                    put("createdAt", note.createdAt)
                    put("updatedAt", note.updatedAt)
                }
                jsonArray.put(obj)
            }

            val jsonString = jsonArray.toString(2)
            val fileName = "Brokkobi_Backup_${System.currentTimeMillis()}.json"
            val downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
            if (!downloadsDir.exists()) downloadsDir.mkdirs()

            val file = File(downloadsDir, fileName)
            FileOutputStream(file).use { out ->
                out.write(jsonString.toByteArray())
            }

            Toast.makeText(context, "ব্যাকআপ ফাইল সংরক্ষিত হয়েছে: Downloads/$fileName", Toast.LENGTH_LONG).show()
            return jsonString
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(context, "ব্যাকআপে ত্রুটি: ${e.localizedMessage}", Toast.LENGTH_SHORT).show()
            return null
        }
    }

    fun parseJsonToNotes(jsonString: String): List<PoemNote> {
        val notes = mutableListOf<PoemNote>()
        try {
            val jsonArray = JSONArray(jsonString)
            for (i in 0 until jsonArray.length()) {
                val obj = jsonArray.getJSONObject(i)
                val note = PoemNote(
                    id = obj.optInt("id", 0),
                    title = obj.optString("title", "শিরোনামহীন"),
                    content = obj.optString("content", ""),
                    stanzaCount = obj.optInt("stanzaCount", 0),
                    wordCount = obj.optInt("wordCount", 0),
                    category = obj.optString("category", "কবিতা"),
                    isPinned = obj.optBoolean("isPinned", false),
                    isLocked = obj.optBoolean("isLocked", false),
                    isArchived = obj.optBoolean("isArchived", false),
                    isTrash = obj.optBoolean("isTrash", false),
                    isHidden = obj.optBoolean("isHidden", false),
                    groupId = if (obj.isNull("groupId")) null else obj.optInt("groupId"),
                    isBold = obj.optBoolean("isBold", false),
                    isItalic = obj.optBoolean("isItalic", false),
                    isUnderline = obj.optBoolean("isUnderline", false),
                    isStrikethrough = obj.optBoolean("isStrikethrough", false),
                    textAlign = obj.optString("textAlign", "LEFT"),
                    lineBreakMode = obj.optString("lineBreakMode", "LINE_BY_LINE"),
                    fontSizeSp = obj.optInt("fontSizeSp", 18),
                    lineSpacingMultiplier = obj.optDouble("lineSpacingMultiplier", 1.6).toFloat(),
                    titleColorHex = obj.optString("titleColorHex", "#D4A017"),
                    textColorHex = obj.optString("textColorHex", "#E2E8F0"),
                    fontFamilyName = obj.optString("fontFamilyName", "Serif"),
                    fontCategory = obj.optString("fontCategory", "আধুনিক"),
                    bgThemeName = obj.optString("bgThemeName", "Classic"),
                    createdAt = obj.optLong("createdAt", System.currentTimeMillis()),
                    updatedAt = obj.optLong("updatedAt", System.currentTimeMillis())
                )
                notes.add(note)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return notes
    }
}
