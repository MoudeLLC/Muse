package com.muse.settings.ui

import android.bluetooth.BluetoothAdapter
import android.content.Context
import android.net.wifi.WifiManager
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
 * Network settings screen
 * Manages WiFi, Bluetooth, and mobile data settings
 */
@Composable
fun NetworkSettingsScreen(
    onBackPressed: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val wifiManager = remember { context.getSystemService(Context.WIFI_SERVICE) as? WifiManager }
    val bluetoothAdapter = remember { BluetoothAdapter.getDefaultAdapter() }
    
    var wifiEnabled by remember { mutableStateOf(wifiManager?.isWifiEnabled ?: false) }
    var bluetoothEnabled by remember { mutableStateOf(bluetoothAdapter?.isEnabled ?: false) }
    // Mobile data state requires system permissions to read/write
    // Using connectivity manager to check if mobile data is available
    var mobileDataEnabled by remember {
        mutableStateOf(
            try {
                val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as android.net.ConnectivityManager
                val activeNetwork = connectivityManager.activeNetworkInfo
                activeNetwork?.type == android.net.ConnectivityManager.TYPE_MOBILE
            } catch (e: Exception) {
                false
            }
        )
    }
    
    Box(modifier = modifier.fillMaxSize()) {
        MagicalBackground()
        
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // Header with back button
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
                    text = "Network & Internet",
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
            
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // WiFi
                item {
                    SettingToggleCard(
                        title = "WiFi",
                        description = if (wifiEnabled) "Connected" else "Disabled",
                        icon = Icons.Default.Settings,
                        checked = wifiEnabled,
                        onCheckedChange = { enabled ->
                            wifiEnabled = enabled
                            wifiManager?.isWifiEnabled = enabled
                        }
                    )
                }
                
                // Bluetooth
                item {
                    SettingToggleCard(
                        title = "Bluetooth",
                        description = if (bluetoothEnabled) "On" else "Off",
                        icon = Icons.Default.Settings,
                        checked = bluetoothEnabled,
                        onCheckedChange = { enabled ->
                            bluetoothEnabled = enabled
                            if (enabled) {
                                bluetoothAdapter?.enable()
                            } else {
                                bluetoothAdapter?.disable()
                            }
                        }
                    )
                }
                
                // Mobile Data
                item {
                    SettingToggleCard(
                        title = "Mobile Data",
                        description = if (mobileDataEnabled) "On" else "Off",
                        icon = Icons.Default.Settings,
                        checked = mobileDataEnabled,
                        onCheckedChange = { enabled ->
                            mobileDataEnabled = enabled
                            // Note: Requires system permissions to actually toggle
                        }
                    )
                }
            }
        }
    }
}
