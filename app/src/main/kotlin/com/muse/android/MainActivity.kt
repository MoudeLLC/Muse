package com.muse.android

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.foundation.shape.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.*
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.muse.infiniteui.theme.InfiniteUITheme
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            InfiniteUITheme {
                MuseOSHomeScreen(
                    onAppClick = { appName ->
                        if (appName == "Genna AI") {
                            startActivity(Intent(this, com.muse.android.assistant.GennaActivity::class.java))
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun MuseOSHomeScreen(onAppClick: (String) -> Unit) {
    var quickSettingsExpanded by remember { mutableStateOf(false) }
    var appDrawerOpen by remember { mutableStateOf(false) }
    val currentTime = remember { mutableStateOf(SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date())) }
    
    LaunchedEffect(Unit) {
        while(true) {
            kotlinx.coroutines.delay(60000)
            currentTime.value = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date())
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        // Dark creative background with gradient
        DarkCreativeBackground()
        
        // Main home screen content
        Column(modifier = Modifier.fillMaxSize()) {
            // Status bar
            StatusBar(
                time = currentTime.value,
                onStatusBarClick = { quickSettingsExpanded = !quickSettingsExpanded }
            )
            
            Spacer(modifier = Modifier.weight(1f))
            
            // Home screen apps (favorites)
            HomeScreenApps(onAppClick = onAppClick)
            
            Spacer(modifier = Modifier.height(32.dp))
            
            // Dock with main apps
            AppDock(
                onAppDrawerClick = { appDrawerOpen = true },
                onAppClick = onAppClick
            )
        }
        
        // Quick settings panel
        AnimatedVisibility(
            visible = quickSettingsExpanded,
            enter = slideInVertically() + fadeIn(),
            exit = slideOutVertically() + fadeOut()
        ) {
            QuickSettingsPanel(onDismiss = { quickSettingsExpanded = false })
        }
        
        // App drawer
        AnimatedVisibility(
            visible = appDrawerOpen,
            enter = slideInVertically(initialOffsetY = { it }) + fadeIn(),
            exit = slideOutVertically(targetOffsetY = { it }) + fadeOut()
        ) {
            AppDrawer(
                onDismiss = { appDrawerOpen = false },
                onAppClick = { app ->
                    appDrawerOpen = false
                    onAppClick(app)
                }
            )
        }
    }
}

@Composable
fun DarkCreativeBackground() {
    val infiniteTransition = rememberInfiniteTransition(label = "background")
    val offset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(20000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "offset"
    )
    
    Canvas(modifier = Modifier.fillMaxSize()) {
        // Dark base
        drawRect(Color(0xFF0A0E27))
        
        // Animated gradient blobs
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(
                    Color(0xFF6366F1).copy(alpha = 0.3f),
                    Color.Transparent
                ),
                center = Offset(size.width * 0.2f, offset % size.height)
            ),
            radius = 300f,
            center = Offset(size.width * 0.2f, offset % size.height)
        )
        
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(
                    Color(0xFF8B5CF6).copy(alpha = 0.25f),
                    Color.Transparent
                ),
                center = Offset(size.width * 0.8f, size.height - (offset % size.height))
            ),
            radius = 350f,
            center = Offset(size.width * 0.8f, size.height - (offset % size.height))
        )
        
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(
                    Color(0xFF06B6D4).copy(alpha = 0.2f),
                    Color.Transparent
                ),
                center = Offset(size.width * 0.5f, size.height * 0.3f + (offset * 0.5f) % (size.height * 0.4f))
            ),
            radius = 250f,
            center = Offset(size.width * 0.5f, size.height * 0.3f + (offset * 0.5f) % (size.height * 0.4f))
        )
    }
}

@Composable
fun StatusBar(time: String, onStatusBarClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onStatusBarClick() }
            .background(Color.Black.copy(alpha = 0.3f))
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = time,
            color = Color.White,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium
        )
        
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Icon(Icons.Default.Wifi, contentDescription = "WiFi", tint = Color.White, modifier = Modifier.size(18.dp))
            Icon(Icons.Default.SignalCellularAlt, contentDescription = "Signal", tint = Color.White, modifier = Modifier.size(18.dp))
            Icon(Icons.Default.BatteryFull, contentDescription = "Battery", tint = Color.White, modifier = Modifier.size(18.dp))
        }
    }
}

@Composable
fun QuickSettingsPanel(onDismiss: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF1E1B4B).copy(alpha = 0.98f),
                        Color(0xFF312E81).copy(alpha = 0.95f)
                    )
                )
            )
            .clickable { onDismiss() }
            .padding(24.dp)
    ) {
        Text(
            "Quick Settings",
            color = Color.White,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
        
        Spacer(modifier = Modifier.height(24.dp))
        
        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(listOf(
                "WiFi" to Icons.Default.Wifi,
                "Bluetooth" to Icons.Default.Bluetooth,
                "Airplane" to Icons.Default.AirplanemodeActive,
                "Location" to Icons.Default.LocationOn,
                "Flashlight" to Icons.Default.FlashlightOn,
                "Auto Rotate" to Icons.Default.ScreenRotation
            )) { (name, icon) ->
                QuickSettingTile(name = name, icon = icon)
            }
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        // Brightness slider
        Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Brightness6, contentDescription = null, tint = Color.White)
                Spacer(modifier = Modifier.width(12.dp))
                Text("Brightness", color = Color.White)
            }
            Slider(
                value = 0.7f,
                onValueChange = {},
                colors = SliderDefaults.colors(
                    thumbColor = Color(0xFF8B5CF6),
                    activeTrackColor = Color(0xFF8B5CF6)
                )
            )
        }
    }
}

@Composable
fun QuickSettingTile(name: String, icon: ImageVector) {
    Column(
        modifier = Modifier
            .aspectRatio(1f)
            .background(
                Color.White.copy(alpha = 0.1f),
                RoundedCornerShape(16.dp)
            )
            .clickable { }
            .padding(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            icon,
            contentDescription = name,
            tint = Color(0xFF8B5CF6),
            modifier = Modifier.size(28.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            name,
            color = Color.White,
            fontSize = 11.sp,
            maxLines = 1
        )
    }
}

@Composable
fun HomeScreenApps(onAppClick: (String) -> Unit) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(4),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp),
        horizontalArrangement = Arrangement.spacedBy(20.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        items(listOf(
            "Genna AI" to Icons.Default.Psychology,
            "Camera" to Icons.Default.CameraAlt,
            "Gallery" to Icons.Default.Photo,
            "Messages" to Icons.Default.Message,
            "Phone" to Icons.Default.Phone,
            "Contacts" to Icons.Default.Contacts,
            "Settings" to Icons.Default.Settings,
            "Browser" to Icons.Default.Language
        )) { (name, icon) ->
            CircularAppIcon(name = name, icon = icon, onClick = { onAppClick(name) })
        }
    }
}

@Composable
fun CircularAppIcon(name: String, icon: ImageVector, onClick: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable { onClick() }
    ) {
        Box(
            modifier = Modifier
                .size(64.dp)
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(
                            Color(0xFF6366F1),
                            Color(0xFF8B5CF6),
                            Color(0xFFEC4899)
                        )
                    ),
                    shape = CircleShape
                )
                .border(2.dp, Color.White.copy(alpha = 0.2f), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                icon,
                contentDescription = name,
                tint = Color.White,
                modifier = Modifier.size(32.dp)
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            name,
            color = Color.White,
            fontSize = 12.sp,
            maxLines = 1
        )
    }
}

@Composable
fun AppDock(onAppDrawerClick: () -> Unit, onAppClick: (String) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 32.dp, vertical = 16.dp)
            .background(
                Color.Black.copy(alpha = 0.4f),
                RoundedCornerShape(24.dp)
            )
            .padding(vertical = 12.dp, horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        DockIcon(Icons.Default.Phone, "Phone") { onAppClick("Phone") }
        DockIcon(Icons.Default.Message, "Messages") { onAppClick("Messages") }
        DockIcon(Icons.Default.Language, "Browser") { onAppClick("Browser") }
        
        // App drawer button
        Box(
            modifier = Modifier
                .size(56.dp)
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            Color(0xFF8B5CF6),
                            Color(0xFF6366F1)
                        )
                    ),
                    shape = CircleShape
                )
                .clickable { onAppDrawerClick() },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                Icons.Default.Apps,
                contentDescription = "App Drawer",
                tint = Color.White,
                modifier = Modifier.size(28.dp)
            )
        }
    }
}

@Composable
fun DockIcon(icon: ImageVector, name: String, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .size(56.dp)
            .background(Color.White.copy(alpha = 0.1f), CircleShape)
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Icon(
            icon,
            contentDescription = name,
            tint = Color.White,
            modifier = Modifier.size(28.dp)
        )
    }
}

@Composable
fun AppDrawer(onDismiss: () -> Unit, onAppClick: (String) -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF0F172A),
                        Color(0xFF1E293B)
                    )
                )
            )
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "All Apps",
                    color = Color.White,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
                IconButton(onClick = onDismiss) {
                    Icon(Icons.Default.Close, contentDescription = "Close", tint = Color.White)
                }
            }
            
            // App grid
            LazyVerticalGrid(
                columns = GridCells.Fixed(4),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp),
                horizontalArrangement = Arrangement.spacedBy(20.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                items(listOf(
                    "Genna AI" to Icons.Default.Psychology,
                    "Camera" to Icons.Default.CameraAlt,
                    "Gallery" to Icons.Default.Photo,
                    "Messages" to Icons.Default.Message,
                    "Phone" to Icons.Default.Phone,
                    "Contacts" to Icons.Default.Contacts,
                    "Settings" to Icons.Default.Settings,
                    "Browser" to Icons.Default.Language,
                    "Calendar" to Icons.Default.CalendarMonth,
                    "Clock" to Icons.Default.AccessTime,
                    "Calculator" to Icons.Default.Calculate,
                    "Files" to Icons.Default.Folder,
                    "Music" to Icons.Default.MusicNote,
                    "Videos" to Icons.Default.VideoLibrary,
                    "Maps" to Icons.Default.Map,
                    "Weather" to Icons.Default.Cloud
                )) { (name, icon) ->
                    CircularAppIcon(name = name, icon = icon, onClick = { onAppClick(name) })
                }
            }
        }
    }
}
