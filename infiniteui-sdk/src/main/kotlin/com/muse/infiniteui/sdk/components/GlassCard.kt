package com.muse.infiniteui.sdk.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * GlassCard - Advanced glassmorphism card component
 * 
 * Features:
 * - Real blur effect (glassmorphism)
 * - Customizable transparency
 * - Border glow animation
 * - Hover/press effects
 * - Multiple elevation levels
 * - Gradient backgrounds
 * - Shimmer effect option
 * - Frosted glass variants
 * - Click handling
 * - Nested glass support
 * 
 * @param modifier Modifier for customization
 * @param onClick Optional click handler
 * @param blurRadius Blur intensity (0.dp to 32.dp)
 * @param backgroundColor Base background color
 * @param borderColor Border color
 * @param borderWidth Border width
 * @param shape Card shape
 * @param elevation Elevation level
 * @param shimmer Enable shimmer effect
 * @param glowBorder Enable animated glow border
 * @param frosted Use frosted glass variant
 * @param content Card content
 */
@Composable
fun GlassCard(
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    blurRadius: Dp = 16.dp,
    backgroundColor: Color = Color(0x40FFFFFF),
    borderColor: Color = Color(0x30FFFFFF),
    borderWidth: Dp = 1.dp,
    shape: Shape = RoundedCornerShape(24.dp),
    elevation: GlassElevation = GlassElevation.Medium,
    shimmer: Boolean = false,
    glowBorder: Boolean = false,
    frosted: Boolean = false,
    content: @Composable ColumnScope.() -> Unit
) {
    var isPressed by remember { mutableStateOf(false) }
    
    // Scale animation on press
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.98f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "card_scale"
    )
    
    // Glow animation
    val infiniteTransition = rememberInfiniteTransition(label = "glow")
    val glowAlpha by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "glow_alpha"
    )
    
    // Shimmer animation
    val shimmerOffset by infiniteTransition.animateFloat(
        initialValue = -1f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "shimmer_offset"
    )
    
    Box(
        modifier = modifier.scale(scale)
    ) {
        // Glow layer (if enabled)
        if (glowBorder) {
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .padding(-4.dp)
                    .blur(8.dp)
                    .border(
                        width = 2.dp,
                        brush = Brush.linearGradient(
                            colors = listOf(
                                Color(0xFF8B5CF6).copy(alpha = glowAlpha * 0.6f),
                                Color(0xFF3B82F6).copy(alpha = glowAlpha * 0.6f),
                                Color(0xFF06B6D4).copy(alpha = glowAlpha * 0.6f)
                            )
                        ),
                        shape = shape
                    )
            )
        }
        
        // Main glass card
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(shape)
                .then(
                    if (frosted) {
                        Modifier.blur(blurRadius * 1.5f)
                    } else {
                        Modifier.blur(blurRadius)
                    }
                )
                .background(
                    brush = if (shimmer) {
                        Brush.linearGradient(
                            colors = listOf(
                                backgroundColor,
                                backgroundColor.copy(alpha = backgroundColor.alpha * 1.3f),
                                backgroundColor
                            )
                        )
                    } else {
                        Brush.verticalGradient(
                            colors = listOf(
                                backgroundColor.copy(alpha = backgroundColor.alpha * 1.2f),
                                backgroundColor.copy(alpha = backgroundColor.alpha * 0.8f)
                            )
                        )
                    }
                )
                .border(
                    width = borderWidth,
                    color = borderColor,
                    shape = shape
                )
                .then(
                    if (onClick != null) {
                        Modifier.clickable {
                            isPressed = true
                            onClick()
                        }
                    } else {
                        Modifier
                    }
                )
        ) {
            // Shadow overlay for elevation
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                Color.Black.copy(
                                    alpha = when (elevation) {
                                        GlassElevation.Low -> 0.05f
                                        GlassElevation.Medium -> 0.1f
                                        GlassElevation.High -> 0.15f
                                    }
                                )
                            )
                        )
                    )
            )
            
            // Content
            Column(
                modifier = Modifier.padding(20.dp),
                content = content
            )
        }
    }
}

enum class GlassElevation {
    Low, Medium, High
}

/**
 * Nested glass card for layered effects
 */
@Composable
fun NestedGlassCard(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    GlassCard(
        modifier = modifier,
        blurRadius = 8.dp,
        backgroundColor = Color(0x20FFFFFF),
        borderColor = Color(0x20FFFFFF),
        shape = RoundedCornerShape(16.dp),
        content = content
    )
}

/**
 * Frosted glass card variant
 */
@Composable
fun FrostedGlassCard(
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit
) {
    GlassCard(
        modifier = modifier,
        onClick = onClick,
        blurRadius = 24.dp,
        backgroundColor = Color(0x30FFFFFF),
        frosted = true,
        content = content
    )
}

/**
 * Usage Examples:
 * 
 * // Basic glass card
 * GlassCard {
 *     Text("Content")
 * }
 * 
 * // Card with shimmer effect
 * GlassCard(
 *     shimmer = true,
 *     glowBorder = true
 * ) {
 *     Text("Magical Content")
 * }
 * 
 * // Clickable card
 * GlassCard(
 *     onClick = { /* action */ },
 *     elevation = GlassElevation.High
 * ) {
 *     Text("Click me")
 * }
 * 
 * // Frosted variant
 * FrostedGlassCard {
 *     Text("Frosted content")
 * }
 * 
 * // Nested cards
 * GlassCard {
 *     Text("Outer card")
 *     NestedGlassCard {
 *         Text("Inner card")
 *     }
 * }
 */
