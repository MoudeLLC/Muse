package com.muse.infiniteui.effects

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import kotlin.math.sin
import kotlin.random.Random

/**
 * Lightning Effect
 * Animated lightning bolts and electrical effects
 */
@Composable
fun LightningEffect(
    modifier: Modifier = Modifier,
    color: Color = Color(0xFF8B5CF6),
    intensity: Float = 1f
) {
    val infiniteTransition = rememberInfiniteTransition(label = "lightning")
    
    val animatedValue by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "lightning_animation"
    )
    
    Canvas(modifier = modifier.fillMaxSize()) {
        drawLightningBolt(
            color = color,
            progress = animatedValue,
            intensity = intensity
        )
    }
}

private fun DrawScope.drawLightningBolt(
    color: Color,
    progress: Float,
    intensity: Float
) {
    val path = Path()
    val startX = size.width * 0.5f
    val startY = 0f
    
    path.moveTo(startX, startY)
    
    // Create jagged lightning path
    var currentX = startX
    var currentY = startY
    val segments = 8
    
    for (i in 1..segments) {
        val nextY = (size.height / segments) * i
        val nextX = currentX + (Random.nextFloat() - 0.5f) * 100f * intensity
        
        path.lineTo(nextX, nextY * progress)
        currentX = nextX
        currentY = nextY
    }
    
    // Draw lightning with glow
    drawPath(
        path = path,
        brush = Brush.verticalGradient(
            colors = listOf(
                color.copy(alpha = 0.8f),
                color.copy(alpha = 0.3f)
            )
        )
    )
}

/**
 * Magical Glow Effect
 * Pulsating glow around elements
 */
@Composable
fun rememberMagicalGlow(): State<Float> {
    val infiniteTransition = rememberInfiniteTransition(label = "glow")
    
    return infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "glow_animation"
    )
}
