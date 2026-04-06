package com.muse.settings.ui

import android.content.Context
import android.media.AudioManager
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
 * Sound settings screen
 * Manages volume, ringtone, and notification sounds
 */
@Composable
fun SoundSettingsScreen(
    onBackPressed: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val audioManager = remember { context.getSystemService(Context.AUDIO_SERVICE) as AudioManager }
    
    var ringtoneVolume by remember {
        mutableStateOf(
            audioManager.getStreamVolume(AudioManager.STREAM_RING) /
                    audioManager.getStreamMaxVolume(AudioManager.STREAM_RING).toFloat()
        )
    }
    var mediaVolume by remember {
        mutableStateOf(
            audioManager.getStreamVolume(AudioManager.STREAM_MUSIC) /
                    audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC).toFloat()
        )
    }
    var notificationVolume by remember {
        mutableStateOf(
            audioManager.getStreamVolume(AudioManager.STREAM_NOTIFICATION) /
                    audioManager.getStreamMaxVolume(AudioManager.STREAM_NOTIFICATION).toFloat()
        )
    }
    var alarmVolume by remember {
        mutableStateOf(
            audioManager.getStreamVolume(AudioManager.STREAM_ALARM) /
                    audioManager.getStreamMaxVolume(AudioManager.STREAM_ALARM).toFloat()
        )
    }
    
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
                    text = "Sound & Vibration",
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
            
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Ringtone Volume
                item {
                    SettingSliderCard(
                        title = "Ringtone Volume",
                        description = "${(ringtoneVolume * 100).toInt()}%",
                        icon = Icons.Default.Phone,
                        value = ringtoneVolume,
                        onValueChange = { newValue ->
                            ringtoneVolume = newValue
                            val maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_RING)
                            audioManager.setStreamVolume(
                                AudioManager.STREAM_RING,
                                (newValue * maxVolume).toInt(),
                                0
                            )
                        }
                    )
                }
                
                // Media Volume
                item {
                    SettingSliderCard(
                        title = "Media Volume",
                        description = "${(mediaVolume * 100).toInt()}%",
                        icon = Icons.Default.Settings,
                        value = mediaVolume,
                        onValueChange = { newValue ->
                            mediaVolume = newValue
                            val maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC)
                            audioManager.setStreamVolume(
                                AudioManager.STREAM_MUSIC,
                                (newValue * maxVolume).toInt(),
                                0
                            )
                        }
                    )
                }
                
                // Notification Volume
                item {
                    SettingSliderCard(
                        title = "Notification Volume",
                        description = "${(notificationVolume * 100).toInt()}%",
                        icon = Icons.Default.Settings,
                        value = notificationVolume,
                        onValueChange = { newValue ->
                            notificationVolume = newValue
                            val maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_NOTIFICATION)
                            audioManager.setStreamVolume(
                                AudioManager.STREAM_NOTIFICATION,
                                (newValue * maxVolume).toInt(),
                                0
                            )
                        }
                    )
                }
                
                // Alarm Volume
                item {
                    SettingSliderCard(
                        title = "Alarm Volume",
                        description = "${(alarmVolume * 100).toInt()}%",
                        icon = Icons.Default.Settings,
                        value = alarmVolume,
                        onValueChange = { newValue ->
                            alarmVolume = newValue
                            val maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_ALARM)
                            audioManager.setStreamVolume(
                                AudioManager.STREAM_ALARM,
                                (newValue * maxVolume).toInt(),
                                0
                            )
                        }
                    )
                }
            }
        }
    }
}
