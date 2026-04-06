package com.muse.systemui.ui

import android.service.notification.StatusBarNotification
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.muse.infiniteui.components.InfiniteUIGlassCard
import com.muse.infiniteui.components.MagicalBackground
import com.muse.infiniteui.components.ParticleEffect
import com.muse.systemui.MuseNotificationListenerService
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun LockScreen(onUnlock: () -> Unit) {
    val notifications by MuseNotificationListenerService.notifications.collectAsState()
    var currentTime by remember { mutableStateOf(getCurrentTime()) }
    var currentDate by remember { mutableStateOf(getCurrentDate()) }
    var dragOffset by remember { mutableStateOf(0f) }
    
    // Update time every minute
    LaunchedEffect(Unit) {
        while (true) {
            kotlinx.coroutines.delay(60000)
            currentTime = getCurrentTime()
            currentDate = getCurrentDate()
        }
    }
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectDragGestures(
                    onDragEnd = {
                        if (dragOffset > 300) {
                            onUnlock()
                        }
                        dragOffset = 0f
                    }
                ) { change, dragAmount ->
                    change.consume()
                    dragOffset += dragAmount.y
                }
            }
    ) {
        // Magical background
        MagicalBackground()
        ParticleEffect()
        
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Spacer(modifier = Modifier.height(48.dp))
            
            // Time and date
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = currentTime,
                    fontSize = 72.sp,
                    color = Color.White,
                    style = MaterialTheme.typography.displayLarge
                )
                
                Text(
                    text = currentDate,
                    fontSize = 20.sp,
                    color = Color.White.copy(alpha = 0.8f),
                    style = MaterialTheme.typography.titleMedium
                )
            }
            
            // Lock screen notifications
            if (notifications.isNotEmpty()) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(vertical = 24.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "${notifications.size} notifications",
                        color = Color.White.copy(alpha = 0.7f),
                        style = MaterialTheme.typography.bodyMedium
                    )
                    
                    notifications.take(3).forEach { notification ->
                        LockScreenNotification(notification)
                    }
                }
            } else {
                Spacer(modifier = Modifier.weight(1f))
            }
            
            // Unlock hint
            Text(
                text = "Swipe up to unlock",
                color = Color.White.copy(alpha = 0.6f),
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(bottom = 32.dp)
            )
        }
    }
}

@Composable
fun LockScreenNotification(notification: StatusBarNotification) {
    InfiniteUIGlassCard(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = notification.notification.extras.getString("android.title") ?: "Notification",
                    color = Color.White,
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 1
                )
                
                Text(
                    text = notification.notification.extras.getString("android.text") ?: "",
                    color = Color.White.copy(alpha = 0.7f),
                    style = MaterialTheme.typography.bodySmall,
                    maxLines = 2
                )
            }
        }
    }
}

private fun getCurrentTime(): String {
    val formatter = SimpleDateFormat("HH:mm", Locale.getDefault())
    return formatter.format(Date())
}

private fun getCurrentDate(): String {
    val formatter = SimpleDateFormat("EEEE, MMMM d", Locale.getDefault())
    return formatter.format(Date())
}
