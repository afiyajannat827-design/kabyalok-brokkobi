package com.example.ui.components

import android.os.Build
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.ui.theme.DarkGlassSurface
import com.example.ui.theme.GlassBorderDark
import com.example.ui.theme.GlassBorderLight
import com.example.ui.theme.GlassGlowGold
import com.example.ui.theme.LightGlassSurface

@Composable
fun GlassCard(
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(16.dp),
    blurRadius: Dp = 12.dp,
    borderWidth: Dp = 1.dp,
    content: @Composable () -> Unit
) {
    val backgroundColor = MaterialTheme.colorScheme.surface
    val borderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)

    val glassHighlightBrush = Brush.linearGradient(
        colors = listOf(
            GlassGlowGold,
            Color.Transparent,
            Color.Transparent
        ),
        start = Offset(0f, 0f),
        end = Offset(300f, 300f)
    )

    Box(
        modifier = modifier
            .clip(shape)
            .background(backgroundColor, shape)
            .background(glassHighlightBrush, shape)
            .border(borderWidth, borderColor, shape)
    ) {
        content()
    }
}

fun Modifier.glassSurface(
    shape: Shape = RoundedCornerShape(16.dp),
    isDark: Boolean = false
): Modifier = this
    .clip(shape)
    .background(
        if (isDark) DarkGlassSurface else LightGlassSurface,
        shape
    )
    .border(
        1.dp,
        if (isDark) GlassBorderDark else GlassBorderLight,
        shape
    )
