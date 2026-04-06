package com.muse.infiniteui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

/**
 * InfiniteUI Theme System
 * Advanced UI with glassmorphism, magical animations, and lightning effects
 * Features: Blur effects, smooth animations, circular designs, vision-inspired aesthetics
 */

// InfiniteUI Advanced Color Palette with Magical Effects
private val InfiniteLightColors = lightColorScheme(
    primary = Color(0xFF6366F1), // Indigo magic
    onPrimary = Color.White,
    primaryContainer = Color(0xFFE0E7FF),
    secondary = Color(0xFF8B5CF6), // Purple lightning
    onSecondary = Color.White,
    tertiary = Color(0xFF06B6D4), // Cyan glow
    background = Color(0xFFF8FAFC),
    surface = Color(0xFFFFFFFF),
    onSurface = Color(0xFF0F172A),
    surfaceVariant = Color(0xFFF1F5F9),
    outline = Color(0xFFE2E8F0)
)

private val InfiniteDarkColors = darkColorScheme(
    primary = Color(0xFF818CF8), // Bright indigo
    onPrimary = Color(0xFF1E1B4B),
    primaryContainer = Color(0xFF4338CA),
    secondary = Color(0xFFA78BFA), // Bright purple
    onSecondary = Color(0xFF3B0764),
    tertiary = Color(0xFF22D3EE), // Bright cyan
    background = Color(0xFF0F172A), // Deep dark
    surface = Color(0xFF1E293B),
    onSurface = Color(0xFFF1F5F9),
    surfaceVariant = Color(0xFF334155),
    outline = Color(0xFF475569)
)

// Magical accent colors for effects
object InfiniteColors {
    val LightningPurple = Color(0xFF8B5CF6)
    val LightningBlue = Color(0xFF3B82F6)
    val LightningCyan = Color(0xFF06B6D4)
    val GlowPink = Color(0xFFEC4899)
    val GlowOrange = Color(0xFFF97316)
    
    // Glass effect colors
    val GlassLight = Color(0x40FFFFFF)
    val GlassDark = Color(0x20000000)
    val GlassBorder = Color(0x30FFFFFF)
}

@Composable
fun InfiniteUITheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) InfiniteDarkColors else InfiniteLightColors
    
    MaterialTheme(
        colorScheme = colorScheme,
        typography = InfiniteUITypography,
        shapes = InfiniteUIShapes,
        content = content
    )
}
