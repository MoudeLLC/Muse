package com.muse.infiniteui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import com.muse.infiniteui.theme.InfiniteColors
import kotlin.math.cos
import kotlin.math.sin

/**
 * Magical Animated Background
 * Moving gradients and particles for visionary UI
 */
@Composable
fun MagicalBackground(
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "background")
    
    val offset1 by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(20000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "offset1"
    )
    
    val offset2 by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(15000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "offset2"
    )
    
    Canvas(modifier = modifier.fillMaxSize()) {
        // Moving gradient orbs
        val centerX = size.width / 2
        val centerY = size.height / 2
        val radius = size.minDimension / 3
        
        // First orb
        val x1 = centerX + radius * cos(Math.toRadians(offset1.toDouble())).toFloat()
        val y1 = centerY + radius * sin(Math.toRadians(offset1.toDouble())).toFloat()
        
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(
                    InfiniteColors.LightningPurple.copy(alpha = 0.3f),
                    Color.Transparent
                ),
                center = Offset(x1, y1),
                radius = 300f
            ),
            radius = 300f,
            center = Offset(x1, y1)
        )
        
        // Second orb
        val x2 = centerX + radius * cos(Math.toRadians(offset2.toDouble() + 120)).toFloat()
        val y2 = centerY + radius * sin(Math.toRadians(offset2.toDouble() + 120)).toFloat()
        
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(
                    InfiniteColors.LightningCyan.copy(alpha = 0.3f),
                    Color.Transparent
                ),
                center = Offset(x2, y2),
                radius = 250f
            ),
            radius = 250f,
            center = Offset(x2, y2)
        )
    }
}
