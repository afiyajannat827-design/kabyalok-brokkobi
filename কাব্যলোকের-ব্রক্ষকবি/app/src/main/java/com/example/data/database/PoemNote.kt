package com.example.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "poem_notes")
data class PoemNote(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String,
    val content: String,
    val stanzaCount: Int = 0,
    val wordCount: Int = 0,
    val category: String = "কবিতা", // "কবিতা", "গান", "গদ্য", "অসম্পূর্ণ", "পছন্দনীয়"
    val isPinned: Boolean = false,
    val isLocked: Boolean = false,
    val pinCode: String? = null,
    val isArchived: Boolean = false,
    val isTrash: Boolean = false,
    val isHidden: Boolean = false,
    val groupId: Int? = null,
    // Text formatting fields
    val isBold: Boolean = false,
    val isItalic: Boolean = false,
    val isUnderline: Boolean = false,
    val isStrikethrough: Boolean = false,
    val textAlign: String = "LEFT", // "LEFT", "CENTER", "RIGHT"
    val lineBreakMode: String = "LINE_BY_LINE", // "LINE_BY_LINE", "OFF", "BY_WORD"
    val fontSizeSp: Int = 18,
    val lineSpacingMultiplier: Float = 1.6f,
    val titleColorHex: String = "#D4A017",
    val textColorHex: String = "#E2E8F0",
    val fontFamilyName: String = "Serif",
    val fontCategory: String = "আধুনিক",
    val bgThemeName: String = "Classic",
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)

