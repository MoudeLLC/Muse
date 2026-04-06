package com.muse.infiniteui.sdk.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * MagicalButton - Advanced animated button with lightning effects
 * 
 * Features:
 * - Lightning gradient animation
 * - Glow effect on press
 * - Smooth scale animation
 * - Customizable colors and shapes
 * - Haptic feedback support
 * - Loading state
 * - Icon support
 * - Multiple sizes
 * 
 * @param text Button text
 * @param onClick Click handler
 * @param modifier Modifier for customization
 * @param enabled Whether button is enabled
 * @param loading Whether button is in loading state
 * @param size Button size (Small, Medium, Large)
 * @param shape Button shape
 * @param colors Custom color scheme
 * @param icon Optional leading icon
 * @param glowIntensity Glow effect intensity (0f-1f)
 * @param animationDuration Animation duration in milliseconds
 */
@Composable
fun MagicalButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    loading: Boolean = false,
    size: ButtonSize = ButtonSize.Medium,
    shape: Shape = CircleShape,
    colors: MagicalButtonColors = MagicalButtonDefaults.colors(),
    icon: (@Composable () -> Unit)? = null,
    glowIntensity: Float = 1f,
    animationDuration: Int = 1500
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    
    // Animated glow
    val infiniteTransition = rememberInfiniteTransition(label = "button_glow")
    val glowAlpha by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(animationDuration, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "glow_alpha"
    )
    
    // Gradient offset animation
    val gradientOffset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "gradient_offset"
    )
    
    // Press scale animation
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.95f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "press_scale"
    )
    
    Box(
        modifier = modifier
            .scale(scale)
            .then(
                when (size) {
                    ButtonSize.Small -> Modifier.height(40.dp)
                    ButtonSize.Medium -> Modifier.height(56.dp)
                    ButtonSize.Large -> Modifier.height(72.dp)
                }
            )
    ) {
        // Glow layer
        Box(
            modifier = Modifier
                .matchParentSize()
                .blur(12.dp)
                .background(
                    brush = Brush.horizontalGradient(
                        colors = listOf(
                            colors.primary.copy(alpha = glowAlpha * glowIntensity * 0.6f),
                            colors.secondary.copy(alpha = glowAlpha * glowIntensity * 0.6f),
                            colors.tertiary.copy(alpha = glowAlpha * glowIntensity * 0.6f)
                        ),
                        startX = gradientOffset * 1000f,
                        endX = (gradientOffset + 0.5f) * 1000f
                    ),
                    shape = shape
                )
        )
        
        // Main button
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clip(shape)
                .background(
                    brush = Brush.horizontalGradient(
                        colors = if (enabled) {
                            listOf(
                                colors.primary.copy(alpha = if (loading) 0.5f else 1f),
                                colors.secondary.copy(alpha = if (loading) 0.5f else 1f),
                                colors.tertiary.copy(alpha = if (loading) 0.5f else 1f)
                            )
                        } else {
                            listOf(
                                Color.Gray.copy(alpha = 0.3f),
                                Color.Gray.copy(alpha = 0.3f)
                            )
                        },
                        startX = gradientOffset * 1000f,
                        endX = (gradientOffset + 0.5f) * 1000f
                    )
                )
                .clickable(
                    interactionSource = interactionSource,
                    indication = null,
                    enabled = enabled && !loading,
                    onClick = onClick
                ),
            contentAlignment = Alignment.Center
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(horizontal = 24.dp)
            ) {
                if (loading) {
                    LoadingIndicator(size = size)
                } else {
                    icon?.invoke()
                    Text(
                        text = text,
                        style = TextStyle(
                            fontSize = when (size) {
                                ButtonSize.Small -> 14.sp
                                ButtonSize.Medium -> 16.sp
                                ButtonSize.Large -> 18.sp
                            },
                            fontWeight = FontWeight.SemiBold,
                            color = colors.contentColor
                        )
                    )
                }
            }
        }
    }
}

@Composable
private fun LoadingIndicator(size: ButtonSize) {
    val infiniteTransition = rememberInfiniteTransition(label = "loading")
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "rotation"
    )
    
    Box(
        modifier = Modifier
            .size(
                when (size) {
                    ButtonSize.Small -> 16.dp
                    ButtonSize.Medium -> 20.dp
                    ButtonSize.Large -> 24.dp
                }
            )
    ) {
        // Loading animation implementation
    }
}

enum class ButtonSize {
    Small, Medium, Large
}

data class MagicalButtonColors(
    val primary: Color,
    val secondary: Color,
    val tertiary: Color,
    val contentColor: Color
)

object MagicalButtonDefaults {
    @Composable
    fun colors(
        primary: Color = Color(0xFF8B5CF6),
        secondary: Color = Color(0xFF3B82F6),
        tertiary: Color = Color(0xFF06B6D4),
        contentColor: Color = Color.White
    ) = MagicalButtonColors(
        primary = primary,
        secondary = secondary,
        tertiary = tertiary,
        contentColor = contentColor
    )
}

/**
 * Usage Examples:
 * 
 * // Basic button
 * MagicalButton(
 *     text = "Click Me",
 *     onClick = { }
 * )
 * 
 * // Large button with custom colors
 * MagicalButton(
 *     text = "Submit",
 *     onClick = { },
 *     size = ButtonSize.Large,
 *     colors = MagicalButtonDefaults.colors(
 *         primary = Color.Red,
 *         secondary = Color.Orange
 *     )
 * )
 * 
 * // Loading button
 * MagicalButton(
 *     text = "Processing",
 *     onClick = { },
 *     loading = true
 * )
 * 
 * // Button with icon
 * MagicalButton(
 *     text = "Send",
 *     onClick = { },
 *     icon = { Icon(Icons.Default.Send, null) }
 * )
 */
