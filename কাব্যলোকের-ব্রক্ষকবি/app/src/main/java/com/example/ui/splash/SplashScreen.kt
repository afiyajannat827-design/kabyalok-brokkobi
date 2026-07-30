package com.example.ui.splash

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.theme.DarkBackground
import com.example.ui.theme.GoldDark
import com.example.ui.theme.GoldLight
import com.example.ui.theme.GoldPrimary
import com.example.ui.theme.SoftLavender
import kotlinx.coroutines.delay
import kotlin.random.Random

private data class Particle(
    val xRatio: Float,
    var yRatio: Float,
    val radiusPx: Float,
    val speed: Float,
    val maxAlpha: Float
)

@Composable
fun SplashScreen(
    onSplashFinished: () -> Unit
) {
    // Animations State
    val iconScale = remember { Animatable(0f) }
    val iconAlpha = remember { Animatable(0f) }
    val pathProgress = remember { Animatable(0f) }
    val titleOffsetY = remember { Animatable(40f) }
    val titleAlpha = remember { Animatable(0f) }
    val subtitleAlpha = remember { Animatable(0f) }

    val infiniteTransition = rememberInfiniteTransition(label = "splash_infinite")

    // Rotating concentric rings
    val ringRotation1 by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(tween(12000, easing = LinearEasing)),
        label = "ring_rot_1"
    )
    val ringRotation2 by infiniteTransition.animateFloat(
        initialValue = 360f,
        targetValue = 0f,
        animationSpec = infiniteRepeatable(tween(8000, easing = LinearEasing)),
        label = "ring_rot_2"
    )

    // Pulse scale for rings
    val ringPulse by infiniteTransition.animateFloat(
        initialValue = 0.95f,
        targetValue = 1.05f,
        animationSpec = infiniteRepeatable(
            tween(2000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "ring_pulse"
    )

    // Background gradient shift
    val bgGradientShift by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            tween(6000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "bg_shift"
    )

    // Pulsing progress dots
    val dot1Alpha by infiniteTransition.animateFloat(
        initialValue = 0.2f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(600, delayMillis = 0), repeatMode = RepeatMode.Reverse),
        label = "d1"
    )
    val dot2Alpha by infiniteTransition.animateFloat(
        initialValue = 0.2f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(600, delayMillis = 200), repeatMode = RepeatMode.Reverse),
        label = "d2"
    )
    val dot3Alpha by infiniteTransition.animateFloat(
        initialValue = 0.2f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(600, delayMillis = 400), repeatMode = RepeatMode.Reverse),
        label = "d3"
    )

    // Particle system
    val particles = remember {
        List(25) {
            Particle(
                xRatio = Random.nextFloat(),
                yRatio = Random.nextFloat(),
                radiusPx = Random.nextFloat() * 6f + 2f,
                speed = Random.nextFloat() * 0.002f + 0.001f,
                maxAlpha = Random.nextFloat() * 0.7f + 0.3f
            )
        }
    }

    LaunchedEffect(Unit) {
        // Step 1: Icon spring bounce in (0 -> 1000ms)
        iconScale.animateTo(
            targetValue = 1f,
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness = Spring.StiffnessLow
            )
        )
        iconAlpha.animateTo(1f, animationSpec = tween(500))

        // Step 2: Cursive heart path draw animation (500 -> 1200ms)
        pathProgress.animateTo(1f, animationSpec = tween(800, easing = FastOutSlowInEasing))

        // Step 3: Title slide up & fade in (1000 -> 1600ms)
        titleOffsetY.animateTo(0f, animationSpec = tween(600, easing = FastOutSlowInEasing))
        titleAlpha.animateTo(1f, animationSpec = tween(600))

        // Step 4: Subtitle fade in (1500 -> 2000ms)
        subtitleAlpha.animateTo(1f, animationSpec = tween(500))

        // Total delay ~2.8s total then navigate to Home
        delay(900)
        onSplashFinished()
    }

    // Dynamic Navy + Lavender + Gold shifting background
    val bgBrush = Brush.radialGradient(
        colors = listOf(
            Color(0xFF1E1730).copy(alpha = 0.8f + bgGradientShift * 0.2f),
            DarkBackground,
            Color(0xFF070A0E)
        ),
        center = Offset(500f, 600f),
        radius = 1200f
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(bgBrush),
        contentAlignment = Alignment.Center
    ) {
        // Floating glowing particles canvas
        Canvas(modifier = Modifier.fillMaxSize()) {
            particles.forEach { p ->
                p.yRatio -= p.speed
                if (p.yRatio < 0f) p.yRatio = 1f

                val xPx = p.xRatio * size.width
                val yPx = p.yRatio * size.height
                val currentAlpha = (p.yRatio * p.maxAlpha).coerceIn(0f, 1f)

                drawCircle(
                    color = GoldLight.copy(alpha = currentAlpha),
                    radius = p.radiusPx,
                    center = Offset(xPx, yPx)
                )
            }
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(24.dp)
        ) {
            // Icon & Concentric Rings Container
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.size(220.dp)
            ) {
                // Outer Concentric Rings Canvas
                Canvas(
                    modifier = Modifier
                        .fillMaxSize()
                        .scale(ringPulse)
                ) {
                    val centerPx = Offset(size.width / 2f, size.height / 2f)

                    // Outer Ring 1
                    rotate(ringRotation1, centerPx) {
                        drawCircle(
                            color = GoldLight.copy(alpha = 0.25f),
                            radius = size.width * 0.46f,
                            style = Stroke(
                                width = 1.8.dp.toPx(),
                                pathEffect = androidx.compose.ui.graphics.PathEffect.dashPathEffect(
                                    floatArrayOf(40f, 20f), 0f
                                )
                            )
                        )
                    }

                    // Middle Ring 2
                    rotate(ringRotation2, centerPx) {
                        drawCircle(
                            color = SoftLavender.copy(alpha = 0.35f),
                            radius = size.width * 0.38f,
                            style = Stroke(
                                width = 1.5.dp.toPx(),
                                pathEffect = androidx.compose.ui.graphics.PathEffect.dashPathEffect(
                                    floatArrayOf(20f, 15f), 0f
                                )
                            )
                        )
                    }

                    // Inner Ring 3
                    drawCircle(
                        color = GoldPrimary.copy(alpha = 0.20f),
                        radius = size.width * 0.30f,
                        style = Stroke(width = 1.dp.toPx())
                    )
                }

                // Feather Icon with spring bounce & scale
                Image(
                    painter = painterResource(id = R.drawable.feather_icon_1785408656880),
                    contentDescription = stringResource(R.string.app_name),
                    modifier = Modifier
                        .size(130.dp)
                        .scale(iconScale.value)
                        .alpha(iconAlpha.value)
                        .clip(CircleShape)
                )

                // Cursive heart path animation drawn on top
                Canvas(modifier = Modifier.fillMaxSize()) {
                    if (pathProgress.value > 0f) {
                        val w = size.width
                        val h = size.height

                        val heartPath = Path().apply {
                            moveTo(w * 0.35f, h * 0.70f)
                            cubicTo(
                                w * 0.45f, h * 0.85f,
                                w * 0.55f, h * 0.85f,
                                w * 0.62f, h * 0.72f
                            )
                            cubicTo(
                                w * 0.68f, h * 0.62f,
                                w * 0.78f, h * 0.62f,
                                w * 0.78f, h * 0.72f
                            )
                            cubicTo(
                                w * 0.78f, h * 0.82f,
                                w * 0.62f, h * 0.92f,
                                w * 0.62f, h * 0.92f
                            )
                        }

                        drawPath(
                            path = heartPath,
                            brush = Brush.horizontalGradient(
                                listOf(GoldLight, GoldPrimary, SoftLavender)
                            ),
                            style = Stroke(
                                width = 3.dp.toPx(),
                                cap = StrokeCap.Round,
                                join = StrokeJoin.Round,
                                pathEffect = androidx.compose.ui.graphics.PathEffect.dashPathEffect(
                                    floatArrayOf(heartPath.getBounds().width * pathProgress.value, 1000f), 0f
                                )
                            )
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(28.dp))

            // App Name (Gradient Bengali Text)
            Box(
                modifier = Modifier
                    .offset { IntOffset(0, titleOffsetY.value.toInt()) }
                    .alpha(titleAlpha.value)
            ) {
                Text(
                    text = stringResource(R.string.app_name),
                    style = TextStyle(
                        fontFamily = FontFamily.Serif,
                        fontWeight = FontWeight.Bold,
                        fontSize = 30.sp,
                        textAlign = TextAlign.Center,
                        brush = Brush.horizontalGradient(
                            listOf(GoldLight, GoldPrimary, SoftLavender)
                        )
                    )
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Subtitle
            Text(
                text = stringResource(R.string.app_subtitle),
                style = TextStyle(
                    fontFamily = FontFamily.Serif,
                    fontStyle = FontStyle.Italic,
                    fontWeight = FontWeight.Normal,
                    fontSize = 16.sp,
                    color = Color.White.copy(alpha = 0.8f),
                    textAlign = TextAlign.Center
                ),
                modifier = Modifier.alpha(subtitleAlpha.value)
            )

            Spacer(modifier = Modifier.height(48.dp))

            // Thin Golden Progress Line & 3 Pulsing Dots
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth(0.6f)
                    .alpha(subtitleAlpha.value)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(2.dp)
                        .background(
                            Brush.horizontalGradient(
                                listOf(
                                    Color.Transparent,
                                    GoldPrimary,
                                    SoftLavender,
                                    Color.Transparent
                                )
                            )
                        )
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(6.dp)
                            .clip(CircleShape)
                            .background(GoldLight.copy(alpha = dot1Alpha))
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Box(
                        modifier = Modifier
                            .size(6.dp)
                            .clip(CircleShape)
                            .background(GoldPrimary.copy(alpha = dot2Alpha))
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Box(
                        modifier = Modifier
                            .size(6.dp)
                            .clip(CircleShape)
                            .background(SoftLavender.copy(alpha = dot3Alpha))
                    )
                }
            }
        }
    }
}
