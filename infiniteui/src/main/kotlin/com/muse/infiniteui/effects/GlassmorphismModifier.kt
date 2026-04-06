package com.muse.infiniteui.effects

import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Glassmorphism Effect Modifier
 * Creates beautiful blurred glass effect with transparency
 */
fun Modifier.glassmorphism(
    blurRadius: Dp = 16.dp,
    backgroundColor: Color = Color(0x40FFFFFF),
    borderColor: Color = Color(0x30FFFFFF)
): Modifier = this.then(
    Modifier.drawBehind {
        // Draw glass background with gradient
        drawRect(
            brush = Brush.verticalGradient(
                colors = listOf(
                    backgroundColor.copy(alpha = 0.7f),
                    backgroundColor.copy(alpha = 0.5f)
                )
            )
        )
        
        // Draw border
        drawRect(
            color = borderColor,
            style = androidx.compose.ui.graphics.drawscope.Stroke(width = 1.dp.toPx())
        )
    }
)

/**
 * Frosted Glass Effect
 * More intense blur for background elements
 */
fun Modifier.frostedGlass(
    blurRadius: Dp = 24.dp,
    tint: Color = Color(0x20FFFFFF)
): Modifier = this
    .blur(blurRadius)
    .drawBehind {
        drawRect(color = tint)
    }
