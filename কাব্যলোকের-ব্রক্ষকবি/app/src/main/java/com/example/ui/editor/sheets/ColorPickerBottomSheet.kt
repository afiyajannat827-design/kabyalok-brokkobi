package com.example.ui.editor.sheets

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight

import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.GoldPrimary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ColorPickerBottomSheet(
    initialTitleColorHex: String,
    initialTextColorHex: String,
    onApplyColors: (titleHex: String, textHex: String) -> Unit,
    onDismiss: () -> Unit
) {
    var selectedTab by remember { mutableIntStateOf(0) } // 0: Title, 1: Content Text
    var titleHex by remember { mutableStateOf(initialTitleColorHex) }
    var textHex by remember { mutableStateOf(initialTextColorHex) }

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    val swatches = listOf(
        "#D4A017", "#FFD700", "#E2E8F0", "#FFFFFF",
        "#FF6B6B", "#4ECDC4", "#45B7D1", "#96CEB4",
        "#FFEEAD", "#D4A5A5", "#9B59B6", "#3498DB",
        "#1ABC9C", "#2ECC71", "#E67E22", "#E74C3C",
        "#34495E", "#7F8C8D", "#1A1A2E", "#161C26"
    )

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
                text = "রং নির্বাচন করুন",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontFamily = FontFamily.Serif,
                    fontWeight = FontWeight.Bold,
                    color = GoldPrimary,
                    fontSize = 20.sp
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Tab Switch: Title vs Content
            TabRow(
                selectedTabIndex = selectedTab,
                containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                contentColor = GoldPrimary,
                modifier = Modifier.clip(RoundedCornerShape(16.dp))
            ) {
                Tab(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    text = {
                        Text(
                            text = "টাইটেল রঙ",
                            style = TextStyle(
                                fontWeight = FontWeight.Bold,
                                color = if (selectedTab == 0) GoldPrimary else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        )
                    }
                )
                Tab(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    text = {
                        Text(
                            text = "লেখার রঙ",
                            style = TextStyle(
                                fontWeight = FontWeight.Bold,
                                color = if (selectedTab == 1) GoldPrimary else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        )
                    }
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Live Preview Card
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(18.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant)
                    .padding(16.dp)
            ) {
                Column {
                    Text(
                        text = "লাইভ প্রিভিউ শিরোনাম",
                        style = TextStyle(
                            fontFamily = FontFamily.Serif,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            color = parseHexColor(titleHex)
                        )
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "এখানে আপনার কবিতার পঙক্তির রং পরিবর্তিত হয়ে প্রাকদর্শন হবে।",
                        style = TextStyle(
                            fontFamily = FontFamily.Serif,
                            fontSize = 13.sp,
                            color = parseHexColor(textHex)
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // 6-column Grid of Color Swatches
            val activeHex = if (selectedTab == 0) titleHex else textHex

            LazyVerticalGrid(
                columns = GridCells.Fixed(6),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.height(140.dp)
            ) {
                items(swatches) { hex ->
                    val color = parseHexColor(hex)
                    val isSelected = activeHex.equals(hex, ignoreCase = true)

                    Box(
                        modifier = Modifier
                            .aspectRatio(1f)
                            .clip(CircleShape)
                            .background(
                                Brush.radialGradient(
                                    colors = listOf(color, color.copy(alpha = 0.85f))
                                )
                            )
                            .border(
                                width = if (isSelected) 3.dp else 1.dp,
                                color = if (isSelected) GoldPrimary else Color.White.copy(alpha = 0.2f),
                                shape = CircleShape
                            )
                            .clickable {
                                if (selectedTab == 0) titleHex = hex else textHex = hex
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        if (isSelected) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = "Selected",
                                tint = if (color.isDarkColor()) Color.White else Color.Black,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // HEX Input Row
            OutlinedTextField(
                value = if (selectedTab == 0) titleHex else textHex,
                onValueChange = { input ->
                    if (selectedTab == 0) titleHex = input else textHex = input
                },
                label = { Text("কাস্টম HEX কোড (যেমন: #D4A017)") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Bottom Apply Button
            Button(
                onClick = {
                    onApplyColors(titleHex, textHex)
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
                    style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 16.sp)
                )
            }
        }
    }
}

fun parseHexColor(hex: String): Color {
    return try {
        val clean = hex.removePrefix("#")
        val colorInt = if (clean.length == 6) {
            (0xFF000000 or clean.toLong(16)).toInt()
        } else {
            clean.toLong(16).toInt()
        }
        Color(colorInt)
    } catch (e: Exception) {
        GoldPrimary
    }
}

fun Color.isDarkColor(): Boolean {
    val luminance = 0.299 * red + 0.587 * green + 0.114 * blue
    return luminance < 0.5
}
