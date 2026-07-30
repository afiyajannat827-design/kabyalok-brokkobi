package com.example.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun PenNibPlusFabIcon(
    tint: Color = MaterialTheme.colorScheme.onPrimary,
    modifier: Modifier = Modifier.size(28.dp)
) {
    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height
        val strokeW = 2.2.dp.toPx()

        // Pen Nib Body
        val nibPath = Path().apply {
            moveTo(w * 0.25f, h * 0.75f)
            lineTo(w * 0.20f, h * 0.40f)
            quadraticTo(w * 0.20f, h * 0.20f, w * 0.45f, h * 0.15f)
            quadraticTo(w * 0.70f, h * 0.20f, w * 0.70f, h * 0.40f)
            lineTo(w * 0.65f, h * 0.75f)
            close()
        }
        drawPath(
            path = nibPath,
            color = tint,
            style = Stroke(width = strokeW, cap = StrokeCap.Round, join = StrokeJoin.Round)
        )

        // Pen nib slit & breath hole
        drawLine(
            color = tint,
            start = Offset(w * 0.45f, h * 0.42f),
            end = Offset(w * 0.45f, h * 0.75f),
            strokeWidth = strokeW,
            cap = StrokeCap.Round
        )
        drawCircle(
            color = tint,
            radius = strokeW * 1.2f,
            center = Offset(w * 0.45f, h * 0.42f)
        )

        // Plus badge top right
        drawLine(
            color = tint,
            start = Offset(w * 0.70f, h * 0.25f),
            end = Offset(w * 0.90f, h * 0.25f),
            strokeWidth = strokeW * 1.2f,
            cap = StrokeCap.Round
        )
        drawLine(
            color = tint,
            start = Offset(w * 0.80f, h * 0.15f),
            end = Offset(w * 0.80f, h * 0.35f),
            strokeWidth = strokeW * 1.2f,
            cap = StrokeCap.Round
        )
    }
}

@Composable
fun TiltedThumbtackIcon(
    isPinned: Boolean = false,
    tint: Color = MaterialTheme.colorScheme.primary,
    modifier: Modifier = Modifier.size(24.dp)
) {
    val rotation by animateFloatAsState(
        targetValue = if (isPinned) -15f else 0f,
        animationSpec = tween(300, easing = FastOutSlowInEasing),
        label = "thumbtack_rotation"
    )

    Canvas(modifier = modifier.rotate(rotation)) {
        val w = size.width
        val h = size.height
        val strokeW = 2.dp.toPx()

        val headPath = Path().apply {
            moveTo(w * 0.35f, h * 0.20f)
            lineTo(w * 0.65f, h * 0.20f)
            lineTo(w * 0.58f, h * 0.40f)
            lineTo(w * 0.75f, h * 0.58f)
            lineTo(w * 0.25f, h * 0.58f)
            lineTo(w * 0.42f, h * 0.40f)
            close()
        }

        drawPath(
            path = headPath,
            color = tint,
            style = if (isPinned) Fill else Stroke(width = strokeW, join = StrokeJoin.Round)
        )

        // Pin needle
        drawLine(
            color = tint,
            start = Offset(w * 0.50f, h * 0.58f),
            end = Offset(w * 0.50f, h * 0.88f),
            strokeWidth = strokeW * 1.1f,
            cap = StrokeCap.Round
        )
    }
}

@Composable
fun LockIcon(
    isLocked: Boolean = false,
    tint: Color = MaterialTheme.colorScheme.primary,
    modifier: Modifier = Modifier.size(24.dp)
) {
    val surfaceColor = MaterialTheme.colorScheme.surface
    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height
        val strokeW = 2.dp.toPx()

        // Shackle
        val shacklePath = Path().apply {
            moveTo(w * 0.30f, h * 0.48f)
            lineTo(w * 0.30f, h * 0.32f)
            cubicTo(w * 0.30f, h * 0.15f, w * 0.70f, h * 0.15f, w * 0.70f, h * 0.32f)
            lineTo(w * 0.70f, if (isLocked) h * 0.48f else h * 0.38f)
        }
        drawPath(
            path = shacklePath,
            color = tint,
            style = Stroke(width = strokeW, cap = StrokeCap.Round)
        )

        // Lock Body
        val bodyPath = Path().apply {
            addRoundRect(
                androidx.compose.ui.geometry.RoundRect(
                    left = w * 0.20f,
                    top = h * 0.48f,
                    right = w * 0.80f,
                    bottom = h * 0.88f,
                    cornerRadius = androidx.compose.ui.geometry.CornerRadius(w * 0.10f)
                )
            )
        }
        drawPath(
            path = bodyPath,
            color = tint,
            style = if (isLocked) Fill else Stroke(width = strokeW)
        )

        // Keyhole
        val keyholeColor = if (isLocked) surfaceColor else tint
        drawCircle(
            color = keyholeColor,
            radius = strokeW * 1.2f,
            center = Offset(w * 0.50f, h * 0.64f)
        )
        drawLine(
            color = keyholeColor,
            start = Offset(w * 0.50f, h * 0.64f),
            end = Offset(w * 0.50f, h * 0.76f),
            strokeWidth = strokeW * 1.1f,
            cap = StrokeCap.Round
        )
    }
}

@Composable
fun AsymmetricHamburgerIcon(
    tint: Color = MaterialTheme.colorScheme.onSurface,
    modifier: Modifier = Modifier.size(24.dp)
) {
    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height
        val strokeW = 2.dp.toPx()

        // Top line (long)
        drawLine(
            color = tint,
            start = Offset(w * 0.15f, h * 0.28f),
            end = Offset(w * 0.85f, h * 0.28f),
            strokeWidth = strokeW,
            cap = StrokeCap.Round
        )
        // Middle line (medium)
        drawLine(
            color = tint,
            start = Offset(w * 0.15f, h * 0.50f),
            end = Offset(w * 0.65f, h * 0.50f),
            strokeWidth = strokeW,
            cap = StrokeCap.Round
        )
        // Bottom line (short)
        drawLine(
            color = tint,
            start = Offset(w * 0.15f, h * 0.72f),
            end = Offset(w * 0.45f, h * 0.72f),
            strokeWidth = strokeW,
            cap = StrokeCap.Round
        )
    }
}

@Composable
fun ThemeToggleAnimatedIcon(
    isDark: Boolean,
    onToggle: () -> Unit,
    tint: Color = MaterialTheme.colorScheme.primary,
    modifier: Modifier = Modifier.size(28.dp)
) {
    val rotation by animateFloatAsState(
        targetValue = if (isDark) 180f else 0f,
        animationSpec = tween(500, easing = FastOutSlowInEasing),
        label = "theme_toggle_rotation"
    )

    Box(
        modifier = modifier
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onToggle
            )
            .rotate(rotation),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.size(24.dp)) {
            val w = size.width
            val h = size.height
            val strokeW = 2.dp.toPx()

            if (isDark) {
                // Moon
                val moonPath = Path().apply {
                    moveTo(w * 0.75f, h * 0.20f)
                    cubicTo(w * 0.30f, h * 0.20f, w * 0.20f, h * 0.70f, w * 0.65f, h * 0.85f)
                    cubicTo(w * 0.35f, h * 0.85f, w * 0.35f, h * 0.35f, w * 0.75f, h * 0.20f)
                }
                drawPath(path = moonPath, color = tint, style = Fill)
            } else {
                // Sun
                drawCircle(color = tint, radius = w * 0.22f, style = Stroke(width = strokeW))
                for (i in 0 until 8) {
                    val angle = Math.toRadians((i * 45).toDouble())
                    val startX = (w * 0.50f + Math.cos(angle) * w * 0.32f).toFloat()
                    val startY = (h * 0.50f + Math.sin(angle) * h * 0.32f).toFloat()
                    val endX = (w * 0.50f + Math.cos(angle) * w * 0.44f).toFloat()
                    val endY = (h * 0.50f + Math.sin(angle) * h * 0.44f).toFloat()
                    drawLine(
                        color = tint,
                        start = Offset(startX, startY),
                        end = Offset(endX, endY),
                        strokeWidth = strokeW,
                        cap = StrokeCap.Round
                    )
                }
            }
        }
    }
}
