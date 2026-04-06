package com.muse.settings.ui

import android.content.Context
import android.provider.Settings
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.muse.infiniteui.components.InfiniteUIGlassCard
import com.muse.infiniteui.components.MagicalBackground

/**
 * Display settings screen
 * Manages brightness, screen timeout, and font size
 */
@Composable
fun DisplaySettingsScreen(
    onBackPressed: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    
    var brightness by remember { 
        mutableStateOf(
            try {
                Settings.System.getInt(context.contentResolver, Settings.System.SCREEN_BRIGHTNESS) / 255f
            } catch (e: Exception) {
                0.5f
            }
        )
    }
    var screenTimeout by remember { mutableStateOf(30) } // seconds
    var fontSize by remember { mutableStateOf(1.0f) } // scale
    
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
                    text = "Display",
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
            
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Brightness
                item {
                    SettingSliderCard(
                        title = "Brightness",
                        description = "${(brightness * 100).toInt()}%",
                        icon = Icons.Default.Settings,
                        value = brightness,
                        onValueChange = { newValue ->
                            brightness = newValue
                            try {
                                Settings.System.putInt(
                                    context.contentResolver,
                                    Settings.System.SCREEN_BRIGHTNESS,
                                    (newValue * 255).toInt()
                                )
                            } catch (e: Exception) {
                                // Requires WRITE_SETTINGS permission
                            }
                        }
                    )
                }
                
                // Screen Timeout
                item {
                    SettingSliderCard(
                        title = "Screen Timeout",
                        description = "${screenTimeout}s",
                        icon = Icons.Default.Settings,
                        value = screenTimeout / 300f, // Max 5 minutes
                        onValueChange = { newValue ->
                            screenTimeout = (newValue * 300).toInt().coerceAtLeast(15)
                            try {
                                Settings.System.putInt(
                                    context.contentResolver,
                                    Settings.System.SCREEN_OFF_TIMEOUT,
                                    screenTimeout * 1000
                                )
                            } catch (e: Exception) {
                                // Requires WRITE_SETTINGS permission
                            }
                        }
                    )
                }
                
                // Font Size
                item {
                    SettingSliderCard(
                        title = "Font Size",
                        description = when {
                            fontSize < 0.85f -> "Small"
                            fontSize < 1.15f -> "Normal"
                            fontSize < 1.3f -> "Large"
                            else -> "Extra Large"
                        },
                        icon = Icons.Default.Settings,
                        value = (fontSize - 0.7f) / 0.6f, // Range 0.7 to 1.3
                        onValueChange = { newValue ->
                            fontSize = 0.7f + (newValue * 0.6f)
                            // Apply font scale system-wide
                        }
                    )
                }
            }
        }
    }
}
