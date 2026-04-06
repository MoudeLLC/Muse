package com.muse.systemui.ui

import android.content.Context
import android.media.AudioManager
import android.provider.Settings
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.muse.infiniteui.components.InfiniteUIGlassCard
import com.muse.infiniteui.components.MagicalBackground

@Composable
fun ControlCenter(onDismiss: () -> Unit) {
    val context = LocalContext.current
    val audioManager = remember { context.getSystemService(Context.AUDIO_SERVICE) as AudioManager }
    
    var brightness by remember { mutableStateOf(getBrightness(context)) }
    var volume by remember { mutableStateOf(getVolume(audioManager)) }
    
    Box(modifier = Modifier.fillMaxSize()) {
        MagicalBackground()
        
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Control Center",
                    style = MaterialTheme.typography.headlineMedium,
                    color = Color.White
                )
                
                IconButton(onClick = onDismiss) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close",
                        tint = Color.White
                    )
                }
            }
            
            // Brightness control
            InfiniteUIGlassCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = "Brightness",
                            tint = Color.White
                        )
                        Text(
                            text = "Brightness",
                            color = Color.White,
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    Slider(
                        value = brightness,
                        onValueChange = { newValue ->
                            brightness = newValue
                            setBrightness(context, newValue)
                        },
                        valueRange = 0f..1f,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
            
            // Volume control
            InfiniteUIGlassCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = "Volume",
                            tint = Color.White
                        )
                        Text(
                            text = "Volume",
                            color = Color.White,
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    Slider(
                        value = volume,
                        onValueChange = { newValue ->
                            volume = newValue
                            setVolume(audioManager, newValue)
                        },
                        valueRange = 0f..1f,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
            
            // Media controls
            InfiniteUIGlassCard(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Media Controls",
                        color = Color.White,
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(onClick = { /* Previous track */ }) {
                            Icon(
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = "Previous",
                                tint = Color.White,
                                modifier = Modifier.size(32.dp)
                            )
                        }
                        
                        IconButton(onClick = { /* Play/Pause */ }) {
                            Icon(
                                imageVector = Icons.Default.PlayArrow,
                                contentDescription = "Play",
                                tint = Color.White,
                                modifier = Modifier.size(48.dp)
                            )
                        }
                        
                        IconButton(onClick = { /* Next track */ }) {
                            Icon(
                                imageVector = Icons.Default.ArrowForward,
                                contentDescription = "Next",
                                tint = Color.White,
                                modifier = Modifier.size(32.dp)
                            )
                        }
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Quick settings
            QuickSettings()
        }
    }
}

// Helper functions

private fun getBrightness(context: Context): Float {
    return try {
        val brightness = Settings.System.getInt(
            context.contentResolver,
            Settings.System.SCREEN_BRIGHTNESS
        )
        brightness / 255f
    } catch (e: Exception) {
        0.5f
    }
}

private fun setBrightness(context: Context, value: Float) {
    try {
        val brightness = (value * 255).toInt()
        Settings.System.putInt(
            context.contentResolver,
            Settings.System.SCREEN_BRIGHTNESS,
            brightness
        )
    } catch (e: Exception) {
        // Handle error - requires WRITE_SETTINGS permission
    }
}

private fun getVolume(audioManager: AudioManager): Float {
    return try {
        val currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC)
        val maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC)
        currentVolume.toFloat() / maxVolume.toFloat()
    } catch (e: Exception) {
        0.5f
    }
}

private fun setVolume(audioManager: AudioManager, value: Float) {
    try {
        val maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC)
        val volume = (value * maxVolume).toInt()
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, volume, 0)
    } catch (e: Exception) {
        // Handle error
    }
}
