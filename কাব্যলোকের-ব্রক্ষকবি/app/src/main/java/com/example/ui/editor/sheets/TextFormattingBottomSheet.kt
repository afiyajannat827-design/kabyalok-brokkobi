package com.example.ui.editor.sheets

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.FormatAlignLeft
import androidx.compose.material.icons.automirrored.filled.FormatAlignRight
import androidx.compose.material.icons.filled.FormatAlignCenter
import androidx.compose.material.icons.filled.FormatBold
import androidx.compose.material.icons.filled.FormatItalic
import androidx.compose.material.icons.filled.FormatStrikethrough
import androidx.compose.material.icons.filled.FormatUnderlined
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight

import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.GoldPrimary
import com.example.utils.toBengaliNumerals

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TextFormattingBottomSheet(
    initialIsBold: Boolean,
    initialIsItalic: Boolean,
    initialIsUnderline: Boolean,
    initialIsStrikethrough: Boolean,
    initialTextAlign: String, // "LEFT", "CENTER", "RIGHT"
    initialLineBreakMode: String, // "LINE_BY_LINE", "OFF", "BY_WORD"
    initialFontSize: Int,
    initialLineSpacing: Float,
    onApply: (
        isBold: Boolean,
        isItalic: Boolean,
        isUnderline: Boolean,
        isStrikethrough: Boolean,
        textAlign: String,
        lineBreakMode: String,
        fontSize: Int,
        lineSpacing: Float
    ) -> Unit,
    onDismiss: () -> Unit
) {
    var isBold by remember { mutableStateOf(initialIsBold) }
    var isItalic by remember { mutableStateOf(initialIsItalic) }
    var isUnderline by remember { mutableStateOf(initialIsUnderline) }
    var isStrikethrough by remember { mutableStateOf(initialIsStrikethrough) }
    var textAlign by remember { mutableStateOf(initialTextAlign) }
    var lineBreakMode by remember { mutableStateOf(initialLineBreakMode) }
    var fontSize by remember { mutableIntStateOf(initialFontSize) }
    var lineSpacing by remember { mutableFloatStateOf(initialLineSpacing) }

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = MaterialTheme.colorScheme.surface,
        shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp)
        ) {
            Text(
                text = "টেক্সট ফরম্যাটিং",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontFamily = FontFamily.Serif,
                    fontWeight = FontWeight.Bold,
                    color = GoldPrimary,
                    fontSize = 20.sp
                )
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Style Toggles: B / I / U / S
            Text(
                text = "টেক্সট স্টাইল",
                style = MaterialTheme.typography.labelLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                FormattingIconButton(
                    isSelected = isBold,
                    onClick = { isBold = !isBold }
                ) {
                    Icon(Icons.Default.FormatBold, contentDescription = "Bold")
                }
                FormattingIconButton(
                    isSelected = isItalic,
                    onClick = { isItalic = !isItalic }
                ) {
                    Icon(Icons.Default.FormatItalic, contentDescription = "Italic")
                }
                FormattingIconButton(
                    isSelected = isUnderline,
                    onClick = { isUnderline = !isUnderline }
                ) {
                    Icon(Icons.Default.FormatUnderlined, contentDescription = "Underline")
                }
                FormattingIconButton(
                    isSelected = isStrikethrough,
                    onClick = { isStrikethrough = !isStrikethrough }
                ) {
                    Icon(Icons.Default.FormatStrikethrough, contentDescription = "Strikethrough")
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Alignment: Left / Center / Right
            Text(
                text = "এলাইনমেন্ট",
                style = MaterialTheme.typography.labelLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                FormattingIconButton(
                    isSelected = textAlign == "LEFT",
                    onClick = { textAlign = "LEFT" }
                ) {
                    Icon(Icons.AutoMirrored.Filled.FormatAlignLeft, contentDescription = "Left")
                }
                FormattingIconButton(
                    isSelected = textAlign == "CENTER",
                    onClick = { textAlign = "CENTER" }
                ) {
                    Icon(Icons.Default.FormatAlignCenter, contentDescription = "Center")
                }
                FormattingIconButton(
                    isSelected = textAlign == "RIGHT",
                    onClick = { textAlign = "RIGHT" }
                ) {
                    Icon(Icons.AutoMirrored.Filled.FormatAlignRight, contentDescription = "Right")
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Auto Line Break
            Text(
                text = "অটো লাইন ব্রেক",
                style = MaterialTheme.typography.labelLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            )
            Spacer(modifier = Modifier.height(8.dp))
            val breakModes = listOf(
                Pair("LINE_BY_LINE", "লাইন বাই লাইন"),
                Pair("OFF", "বন্ধ"),
                Pair("BY_WORD", "শব্দ অনুযায়ী")
            )
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(breakModes) { mode ->
                    val isSelected = lineBreakMode == mode.first
                    Box(
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(
                                if (isSelected) GoldPrimary else MaterialTheme.colorScheme.surfaceVariant
                            )
                            .clickable { lineBreakMode = mode.first }
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                    ) {
                        Text(
                            text = mode.second,
                            style = TextStyle(
                                fontSize = 13.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                color = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Font Size
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "ফন্ট সাইজ",
                    style = MaterialTheme.typography.labelLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                )
                Text(
                    text = "${fontSize.toBengaliNumerals()} px",
                    style = TextStyle(fontWeight = FontWeight.Bold, color = GoldPrimary)
                )
            }
            Slider(
                value = fontSize.toFloat(),
                onValueChange = { fontSize = (it.toInt() / 4) * 4 },
                valueRange = 12f..32f,
                steps = 4,
                colors = SliderDefaults.colors(
                    thumbColor = GoldPrimary,
                    activeTrackColor = GoldPrimary
                )
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Line Spacing
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "লাইন স্পেসিং",
                    style = MaterialTheme.typography.labelLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                )
                Text(
                    text = "${String.format("%.1f", lineSpacing).toBengaliNumerals()}x",
                    style = TextStyle(fontWeight = FontWeight.Bold, color = GoldPrimary)
                )
            }
            Slider(
                value = lineSpacing,
                onValueChange = { lineSpacing = (it * 10).toInt() / 10f },
                valueRange = 1.0f..2.0f,
                steps = 9,
                colors = SliderDefaults.colors(
                    thumbColor = GoldPrimary,
                    activeTrackColor = GoldPrimary
                )
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Bottom Apply Button
            Button(
                onClick = {
                    onApply(
                        isBold,
                        isItalic,
                        isUnderline,
                        isStrikethrough,
                        textAlign,
                        lineBreakMode,
                        fontSize,
                        lineSpacing
                    )
                    onDismiss()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = GoldPrimary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                )
            ) {
                Text(
                    text = "সম্পন্ন",
                    style = TextStyle(
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                )
            }
        }
    }
}

@Composable
fun FormattingIconButton(
    isSelected: Boolean,
    onClick: () -> Unit,
    content: @Composable () -> Unit
) {
    Box(
        modifier = Modifier
            .size(46.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(if (isSelected) GoldPrimary else MaterialTheme.colorScheme.surfaceVariant)
            .border(
                width = if (isSelected) 1.5.dp else 0.dp,
                color = if (isSelected) GoldPrimary else Color.Transparent,
                shape = RoundedCornerShape(12.dp)
            )
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        content()
    }
}
