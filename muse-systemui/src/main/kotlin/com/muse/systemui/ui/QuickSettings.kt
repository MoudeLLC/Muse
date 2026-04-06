package com.muse.systemui.ui

import android.bluetooth.BluetoothAdapter
import android.content.Context
import android.net.wifi.WifiManager
import android.provider.Settings
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.muse.infiniteui.components.InfiniteUIGlassCard

@Composable
fun QuickSettings() {
    val context = LocalContext.current
    
    var wifiEnabled by remember { mutableStateOf(isWifiEnabled(context)) }
    var bluetoothEnabled by remember { mutableStateOf(isBluetoothEnabled()) }
    var airplaneModeEnabled by remember { mutableStateOf(isAirplaneModeEnabled(context)) }
    
    val tiles = remember {
        listOf(
            QuickSettingsTile(
                id = "wifi",
                label = "WiFi",
                icon = Icons.Default.Settings,
                isActive = wifiEnabled,
                action = {
                    wifiEnabled = !wifiEnabled
                    toggleWifi(context, wifiEnabled)
                }
            ),
            QuickSettingsTile(
                id = "bluetooth",
                label = "Bluetooth",
                icon = Icons.Default.Settings,
                isActive = bluetoothEnabled,
                action = {
                    bluetoothEnabled = !bluetoothEnabled
                    toggleBluetooth(bluetoothEnabled)
                }
            ),
            QuickSettingsTile(
                id = "airplane",
                label = "Airplane",
                icon = Icons.Default.Settings,
                isActive = airplaneModeEnabled,
                action = {
                    airplaneModeEnabled = !airplaneModeEnabled
                    toggleAirplaneMode(context, airplaneModeEnabled)
                }
            ),
            QuickSettingsTile(
                id = "flashlight",
                label = "Flashlight",
                icon = Icons.Default.Settings,
                isActive = false,
                action = {
                    // Toggle flashlight using camera flash
                    try {
                        val cameraManager = context.getSystemService(Context.CAMERA_SERVICE) as android.hardware.camera2.CameraManager
                        val cameraId = cameraManager.cameraIdList[0]
                        cameraManager.setTorchMode(cameraId, !false) // Toggle state
                    } catch (e: Exception) {
                        android.util.Log.e("QuickSettings", "Failed to toggle flashlight", e)
                    }
                }
            ),
            QuickSettingsTile(
                id = "rotation",
                label = "Rotation",
                icon = Icons.Default.Settings,
                isActive = false,
                action = {
                    // Toggle auto-rotation
                    try {
                        val currentRotation = android.provider.Settings.System.getInt(
                            context.contentResolver,
                            android.provider.Settings.System.ACCELEROMETER_ROTATION,
                            0
                        )
                        android.provider.Settings.System.putInt(
                            context.contentResolver,
                            android.provider.Settings.System.ACCELEROMETER_ROTATION,
                            if (currentRotation == 0) 1 else 0
                        )
                    } catch (e: Exception) {
                        android.util.Log.e("QuickSettings", "Failed to toggle rotation", e)
                    }
                }
            ),
            QuickSettingsTile(
                id = "location",
                label = "Location",
                icon = Icons.Default.LocationOn,
                isActive = false,
                action = {
                    // Open location settings (requires system permission to toggle directly)
                    try {
                        val intent = android.content.Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                        intent.flags = android.content.Intent.FLAG_ACTIVITY_NEW_TASK
                        context.startActivity(intent)
                    } catch (e: Exception) {
                        android.util.Log.e("QuickSettings", "Failed to open location settings", e)
                    }
                }
            )
        )
    }
    
    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        contentPadding = PaddingValues(16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(tiles) { tile ->
            QuickSettingTile(tile = tile)
        }
    }
}

@Composable
fun QuickSettingTile(tile: QuickSettingsTile) {
    InfiniteUIGlassCard(
        modifier = Modifier
            .aspectRatio(1f)
            .clickable(onClick = tile.action)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = tile.icon,
                contentDescription = tile.label,
                tint = if (tile.isActive) Color(0xFF8B5CF6) else Color.White,
                modifier = Modifier.size(32.dp)
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = tile.label,
                style = MaterialTheme.typography.bodySmall,
                color = Color.White
            )
        }
    }
}

data class QuickSettingsTile(
    val id: String,
    val label: String,
    val icon: ImageVector,
    val isActive: Boolean,
    val action: () -> Unit
)

// Helper functions

private fun isWifiEnabled(context: Context): Boolean {
    return try {
        val wifiManager = context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
        wifiManager.isWifiEnabled
    } catch (e: Exception) {
        false
    }
}

private fun toggleWifi(context: Context, enable: Boolean) {
    try {
        val wifiManager = context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
        @Suppress("DEPRECATION")
        wifiManager.isWifiEnabled = enable
    } catch (e: Exception) {
        // Handle error - may need user permission on newer Android versions
    }
}

private fun isBluetoothEnabled(): Boolean {
    return try {
        val bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
        bluetoothAdapter?.isEnabled ?: false
    } catch (e: Exception) {
        false
    }
}

private fun toggleBluetooth(enable: Boolean) {
    try {
        val bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
        if (enable) {
            @Suppress("DEPRECATION")
            bluetoothAdapter?.enable()
        } else {
            @Suppress("DEPRECATION")
            bluetoothAdapter?.disable()
        }
    } catch (e: Exception) {
        // Handle error - may need user permission
    }
}

private fun isAirplaneModeEnabled(context: Context): Boolean {
    return try {
        Settings.Global.getInt(
            context.contentResolver,
            Settings.Global.AIRPLANE_MODE_ON,
            0
        ) != 0
    } catch (e: Exception) {
        false
    }
}

private fun toggleAirplaneMode(context: Context, enable: Boolean) {
    try {
        Settings.Global.putInt(
            context.contentResolver,
            Settings.Global.AIRPLANE_MODE_ON,
            if (enable) 1 else 0
        )
    } catch (e: Exception) {
        // Handle error - requires WRITE_SETTINGS permission
    }
}
