package com.muse.settings.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.muse.infiniteui.components.InfiniteUIGlassCard
import com.muse.infiniteui.components.MagicalBackground
import com.muse.settings.theme.ThemeManager

/**
 * Theme customization screen
 * Allows users to customize color scheme, glassmorphism, particles, and animations
 */
@Composable
fun ThemeCustomizationScreen(
    onBackPressed: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val themeManager = remember { ThemeManager.getInstance(context) }
    
    var colorScheme by remember { mutableStateOf(themeManager.getColorScheme()) }
    var glassmorphismIntensity by remember { mutableStateOf(themeManager.getGlassmorphismIntensity()) }
    var particleEffectsEnabled by remember { mutableStateOf(themeManager.getParticleEffectsEnabled()) }
    var animationsEnabled by remember { mutableStateOf(themeManager.getAnimationsEnabled()) }
    
    Box(modifier = modifier.fillMaxSize()) {
        MagicalBackground()
        
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // Header
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 24.dp)
            ) {
                IconButton(onClick = onBackPressed) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
                
                Spacer(modifier = Modifier.width(8.dp))
                
                Text(
                    text = "Theme Customization",
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
            
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Color Scheme Selection
                item {
                    InfiniteUIGlassCard(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(20.dp)
                        ) {
                            Text(
                                text = "Color Scheme",
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            
                            Spacer(modifier = Modifier.height(16.dp))
                            
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceEvenly
                            ) {
                                ColorSchemeOption(
                                    name = "Light",
                                    icon = Icons.Default.Settings,
                                    isSelected = colorScheme == "light",
                                    onClick = {
                                        colorScheme = "light"
                                        themeManager.setColorScheme("light")
                                    }
                                )
                                
                                ColorSchemeOption(
                                    name = "Dark",
                                    icon = Icons.Default.Settings,
                                    isSelected = colorScheme == "dark",
                                    onClick = {
                                        colorScheme = "dark"
                                        themeManager.setColorScheme("dark")
                                    }
                                )
                                
                                ColorSchemeOption(
                                    name = "Auto",
                                    icon = Icons.Default.Settings,
                                    isSelected = colorScheme == "auto",
                                    onClick = {
                                        colorScheme = "auto"
                                        themeManager.setColorScheme("auto")
                                    }
                                )
                            }
                        }
                    }
                }
                
                // Glassmorphism Intensity
                item {
                    SettingSliderCard(
                        title = "Glassmorphism Intensity",
                        description = when {
                            glassmorphismIntensity < 0.3f -> "Subtle"
                            glassmorphismIntensity < 0.7f -> "Medium"
                            else -> "Strong"
                        },
                        icon = Icons.Default.Settings,
                        value = glassmorphismIntensity,
                        onValueChange = { newValue ->
                            glassmorphismIntensity = newValue
                            themeManager.setGlassmorphismIntensity(newValue)
                        }
                    )
                }
                
                // Particle Effects Toggle
                item {
                    SettingToggleCard(
                        title = "Particle Effects",
                        description = if (particleEffectsEnabled) "Magical particles enabled" else "Disabled for performance",
                        icon = Icons.Default.Star,
                        checked = particleEffectsEnabled,
                        onCheckedChange = { enabled ->
                            particleEffectsEnabled = enabled
                            themeManager.setParticleEffectsEnabled(enabled)
                        }
                    )
                }
                
                // Animations Toggle
                item {
                    SettingToggleCard(
                        title = "Animations",
                        description = if (animationsEnabled) "Smooth transitions enabled" else "Reduced motion",
                        icon = Icons.Default.Settings,
                        checked = animationsEnabled,
                        onCheckedChange = { enabled ->
                            animationsEnabled = enabled
                            themeManager.setAnimationsEnabled(enabled)
                        }
                    )
                }
                
                // Live Preview Section
                item {
                    InfiniteUIGlassCard(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(20.dp)
                        ) {
                            Text(
                                text = "Live Preview",
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            
                            Spacer(modifier = Modifier.height(12.dp))
                            
                            Text(
                                text = "Changes apply immediately across all MuseOS apps",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                            )
                            
                            Spacer(modifier = Modifier.height(16.dp))
                            
                            // Preview card
                            InfiniteUIGlassCard(
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Settings,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.primary,
                                        modifier = Modifier.size(32.dp)
                                    )
                                    
                                    Spacer(modifier = Modifier.width(16.dp))
                                    
                                    Text(
                                        text = "This is how your theme looks!",
                                        style = MaterialTheme.typography.bodyLarge,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

/**
 * Color scheme selection option
 */
@Composable
fun ColorSchemeOption(
    name: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .clickable(onClick = onClick)
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(64.dp)
                .clip(CircleShape)
                .background(
                    if (isSelected) 
                        MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)
                    else 
                        MaterialTheme.colorScheme.surface.copy(alpha = 0.2f)
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = name,
                tint = if (isSelected) 
                    MaterialTheme.colorScheme.primary 
                else 
                    MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                modifier = Modifier.size(32.dp)
            )
        }
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Text(
            text = name,
            style = MaterialTheme.typography.bodyMedium,
            color = if (isSelected) 
                MaterialTheme.colorScheme.primary 
            else 
                MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
        )
    }
}
