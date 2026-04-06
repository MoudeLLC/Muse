package com.muse.infiniteui.effects

import androidx.compose.animation.core.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.drawscope.translate
import kotlin.math.sin

/**
 * Smooth Animation Effects
 * Magical moving UI animations
 */

/**
 * Floating Animation
 * Makes elements float up and down smoothly
 */
fun Modifier.floatingAnimation(): Modifier = composed {
    val infiniteTransition = rememberInfiniteTransition(label = "float")
    val offsetY = infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 20f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "float_y"
    )
    
    this.then(Modifier.drawBehind {
        translate(top = offsetY.value) {
            // Content will be translated
        }
    })
}

/**
 * Shimmer Effect
 * Moving gradient shimmer across elements
 */
fun Modifier.shimmerEffect(
    colors: List<Color> = listOf(
        Color(0xFF8B5CF6),
        Color(0xFF3B82F6),
        Color(0xFF06B6D4)
    )
): Modifier = composed {
    val infiniteTransition = rememberInfiniteTransition(label = "shimmer")
    val offset = infiniteTransition.animateFloat(
        initialValue = -1f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "shimmer_offset"
    )
    
    this.drawBehind {
        val brush = Brush.linearGradient(
            colors = colors,
            start = androidx.compose.ui.geometry.Offset(
                x = size.width * offset.value,
                y = 0f
            ),
            end = androidx.compose.ui.geometry.Offset(
                x = size.width * (offset.value + 0.5f),
                y = size.height
            )
        )
        drawRect(brush = brush, alpha = 0.3f)
    }
}

/**
 * Rotating Gradient Background
 * Magical rotating gradient effect
 */
@Composable
fun rememberRotatingGradient(): State<Float> {
    val infiniteTransition = rememberInfiniteTransition(label = "rotate_gradient")
    
    return infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(10000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "rotation"
    )
}

/**
 * Pulse Animation
 * Pulsating scale effect
 */
@Composable
fun rememberPulseAnimation(): State<Float> {
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    
    return infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.05f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse_scale"
    )
}

/**
 * Wave Effect
 * Smooth wave motion
 */
fun Modifier.waveEffect(): Modifier = composed {
    val infiniteTransition = rememberInfiniteTransition(label = "wave")
    val phase = infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(3000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "wave_phase"
    )
    
    this.drawBehind {
        val waveHeight = 10f
        val frequency = 0.02f
        
        for (x in 0..size.width.toInt() step 5) {
            val y = waveHeight * sin((x * frequency + phase.value) * Math.PI / 180).toFloat()
            drawCircle(
                color = Color(0x40FFFFFF),
                radius = 2f,
                center = androidx.compose.ui.geometry.Offset(x.toFloat(), size.height / 2 + y)
            )
        }
    }
}
