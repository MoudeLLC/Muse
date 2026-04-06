package com.muse.settings.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.muse.gaxialai.GaxialAI
import com.muse.infiniteui.components.InfiniteUIGlassCard
import com.muse.infiniteui.components.MagicalBackground
import com.muse.infiniteui.theme.InfiniteUITheme
import com.muse.settings.navigation.SettingsRoute

/**
 * Settings category data model
 */
data class SettingsCategory(
    val title: String,
    val description: String,
    val icon: ImageVector,
    val route: String
)

/**
 * Main settings screen with all categories
 * Displays settings in beautiful InfiniteUI glass cards
 */
@Composable
fun MainSettingsScreen(
    gaxialAI: GaxialAI,
    onBackPressed: () -> Unit,
    onNavigateToCategory: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val categories = remember {
        listOf(
            SettingsCategory(
                title = "Network & Internet",
                description = "WiFi, Bluetooth, Mobile data",
                icon = Icons.Default.Settings,
                route = SettingsRoute.Network.route
            ),
            SettingsCategory(
                title = "Display",
                description = "Brightness, Screen timeout, Font size",
                icon = Icons.Default.Settings,
                route = SettingsRoute.Display.route
            ),
            SettingsCategory(
                title = "Sound & Vibration",
                description = "Volume, Ringtone, Notifications",
                icon = Icons.Default.Settings,
                route = SettingsRoute.Sound.route
            ),
            SettingsCategory(
                title = "Apps",
                description = "Manage installed applications",
                icon = Icons.Default.Settings,
                route = SettingsRoute.Apps.route
            ),
            SettingsCategory(
                title = "Theme Customization",
                description = "Colors, Effects, Animations",
                icon = Icons.Default.Settings,
                route = SettingsRoute.Theme.route
            ),
            SettingsCategory(
                title = "System Information",
                description = "CPU, RAM, Storage, Battery",
                icon = Icons.Default.Info,
                route = SettingsRoute.SystemInfo.route
            ),
            SettingsCategory(
                title = "About MuseOS",
                description = "Version, Build, Device info",
                icon = Icons.Default.Phone,
                route = SettingsRoute.About.route
            ),
            SettingsCategory(
                title = "AI Assistant",
                description = "Search settings with AI help",
                icon = Icons.Default.Settings,
                route = SettingsRoute.AIAssistant.route
            )
        )
    }
    
    Box(modifier = modifier.fillMaxSize()) {
        // Magical background
        MagicalBackground()
        
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // Header
            Text(
                text = "MuseOS Settings",
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(bottom = 24.dp)
            )
            
            // Categories list
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(categories) { category ->
                    SettingsCategoryCard(
                        category = category,
                        onClick = { onNavigateToCategory(category.route) }
                    )
                }
            }
        }
    }
}

/**
 * Individual settings category card with InfiniteUI styling
 */
@Composable
fun SettingsCategoryCard(
    category: SettingsCategory,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    InfiniteUIGlassCard(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Icon
            Icon(
                imageVector = category.icon,
                contentDescription = category.title,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(32.dp)
            )
            
            Spacer(modifier = Modifier.width(16.dp))
            
            // Text content
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = category.title,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                
                Spacer(modifier = Modifier.height(4.dp))
                
                Text(
                    text = category.description,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
            }
            
            // Arrow icon
            Icon(
                imageVector = Icons.Default.ArrowForward,
                contentDescription = "Navigate",
                tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                modifier = Modifier.size(24.dp)
            )
        }
    }
}
