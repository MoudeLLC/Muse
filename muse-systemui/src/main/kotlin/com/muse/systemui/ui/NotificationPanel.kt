package com.muse.systemui.ui

import android.service.notification.StatusBarNotification
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.muse.infiniteui.components.InfiniteUIGlassCard
import com.muse.infiniteui.components.MagicalBackground
import com.muse.infiniteui.components.InfiniteUIMagicalButton
import com.muse.systemui.MuseNotificationListenerService

@Composable
fun NotificationPanel(
    onDismiss: () -> Unit
) {
    val notifications by MuseNotificationListenerService.notifications.collectAsState()
    val groupedNotifications = remember(notifications) {
        notifications.groupBy { it.packageName }
    }
    
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
                    text = "Notifications",
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
            
            // Notifications list
            if (notifications.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No notifications",
                        color = Color.White.copy(alpha = 0.6f)
                    )
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    groupedNotifications.forEach { (packageName, notifs) ->
                        item {
                            NotificationGroup(
                                packageName = packageName,
                                notifications = notifs,
                                onDismiss = { key ->
                                    MuseNotificationListenerService.getInstance()
                                        ?.dismissNotification(key)
                                }
                            )
                        }
                    }
                    
                    item {
                        Spacer(modifier = Modifier.height(16.dp))
                        InfiniteUIMagicalButton(
                            text = "Clear All",
                            onClick = {
                                MuseNotificationListenerService.getInstance()
                                    ?.dismissAllNotifications()
                            },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun NotificationGroup(
    packageName: String,
    notifications: List<StatusBarNotification>,
    onDismiss: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(notifications.size == 1) }
    
    InfiniteUIGlassCard(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Group header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = notifications.firstOrNull()?.notification?.extras
                            ?.getString("android.title") ?: "Notification",
                        style = MaterialTheme.typography.titleMedium,
                        color = Color.White
                    )
                    if (notifications.size > 1) {
                        Text(
                            text = "${notifications.size} notifications",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.White.copy(alpha = 0.7f)
                        )
                    }
                }
                
                if (notifications.size > 1) {
                    TextButton(onClick = { expanded = !expanded }) {
                        Text(
                            text = if (expanded) "Collapse" else "Expand",
                            color = Color.White
                        )
                    }
                }
            }
            
            // Notification content
            if (expanded) {
                notifications.forEach { notification ->
                    Spacer(modifier = Modifier.height(8.dp))
                    NotificationItem(
                        notification = notification,
                        onDismiss = { onDismiss(notification.key) }
                    )
                }
            }
        }
    }
}

@Composable
fun NotificationItem(
    notification: StatusBarNotification,
    onDismiss: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Top
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = notification.notification.extras.getString("android.text") ?: "",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.White.copy(alpha = 0.9f)
            )
        }
        
        IconButton(onClick = onDismiss) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "Dismiss",
                tint = Color.White.copy(alpha = 0.7f),
                modifier = Modifier.size(16.dp)
            )
        }
    }
}
