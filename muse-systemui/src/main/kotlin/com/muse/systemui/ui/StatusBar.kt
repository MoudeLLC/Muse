package com.muse.systemui.ui

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.muse.infiniteui.effects.glassmorphism
import com.muse.libicon.MuseIcons
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun StatusBar(onTap: () -> Unit) {
    val context = LocalContext.current
    
    var batteryLevel by remember { mutableStateOf(100) }
    var isCharging by remember { mutableStateOf(false) }
    var currentTime by remember { mutableStateOf(getCurrentTime()) }
    
    // Update time every minute
    LaunchedEffect(Unit) {
        while (true) {
            kotlinx.coroutines.delay(60000) // 1 minute
            currentTime = getCurrentTime()
        }
    }
    
    // Monitor battery status
    DisposableEffect(context) {
        val batteryReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                val level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0)
                val scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, 100)
                val status = intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1)
                
                batteryLevel = (level * 100) / scale
                isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING ||
                            status == BatteryManager.BATTERY_STATUS_FULL
            }
        }
        
        val filter = IntentFilter(Intent.ACTION_BATTERY_CHANGED)
        context.registerReceiver(batteryReceiver, filter)
        
        onDispose {
            context.unregisterReceiver(batteryReceiver)
        }
    }
    
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0x40000000),
                        Color(0x20000000)
                    )
                )
            )
            .glassmorphism()
            .clickable(onClick = onTap)
            .padding(horizontal = 16.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Left side - Time
            Text(
                text = currentTime,
                color = Color.White,
                fontSize = 14.sp
            )
            
            // Right side - Status icons
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // WiFi icon
                Icon(
                    imageVector = MuseIcons.Wifi,
                    contentDescription = "WiFi",
                    tint = Color.White,
                    modifier = Modifier.size(18.dp)
                )
                
                // Signal icon
                Icon(
                    imageVector = MuseIcons.Signal,
                    contentDescription = "Signal",
                    tint = Color.White,
                    modifier = Modifier.size(18.dp)
                )
                
                // Battery icon
                Row(
                    horizontalArrangement = Arrangement.spacedBy(2.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = if (isCharging) MuseIcons.BatteryCharging else MuseIcons.Battery,
                        contentDescription = "Battery",
                        tint = if (batteryLevel < 20) Color.Red else Color.White,
                        modifier = Modifier.size(18.dp)
                    )
                    
                    Text(
                        text = "$batteryLevel%",
                        color = Color.White,
                        fontSize = 12.sp
                    )
                }
            }
        }
    }
}

private fun getCurrentTime(): String {
    val formatter = SimpleDateFormat("HH:mm", Locale.getDefault())
    return formatter.format(Date())
}
