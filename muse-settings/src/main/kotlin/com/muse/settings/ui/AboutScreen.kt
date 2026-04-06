package com.muse.settings.ui

import android.os.Build
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.muse.infiniteui.components.InfiniteUIGlassCard
import com.muse.infiniteui.components.MagicalBackground

/**
 * About screen with MuseOS branding
 * NO Google or Android branding - pure MuseOS identity
 */
@Composable
fun AboutScreen(
    onBackPressed: () -> Unit,
    modifier: Modifier = Modifier
) {
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
                    text = "About MuseOS",
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
            
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // MuseOS Logo and Name
                item {
                    InfiniteUIGlassCard(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = "MuseOS Logo",
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(64.dp)
                            )
                            
                            Spacer(modifier = Modifier.height(16.dp))
                            
                            Text(
                                text = "MuseOS",
                                style = MaterialTheme.typography.displaySmall,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                            
                            Spacer(modifier = Modifier.height(8.dp))
                            
                            Text(
                                text = "The Magical Operating System",
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
                            )
                        }
                    }
                }
                
                // Version Information
                item {
                    AboutInfoCard(
                        title = "MuseOS Version",
                        value = "1.0.0 Alpha",
                        icon = Icons.Default.Info
                    )
                }
                
                item {
                    AboutInfoCard(
                        title = "Build Number",
                        value = "2026.04.05.001",
                        icon = Icons.Default.Build
                    )
                }
                
                item {
                    AboutInfoCard(
                        title = "Build Type",
                        value = "Development",
                        icon = Icons.Default.Build
                    )
                }
                
                // Device Information
                item {
                    AboutInfoCard(
                        title = "Device Model",
                        value = "${Build.MANUFACTURER} ${Build.MODEL}",
                        icon = Icons.Default.Phone
                    )
                }
                
                item {
                    AboutInfoCard(
                        title = "Kernel Version",
                        value = System.getProperty("os.version") ?: "Unknown",
                        icon = Icons.Default.Info
                    )
                }
                
                item {
                    AboutInfoCard(
                        title = "API Level",
                        value = Build.VERSION.SDK_INT.toString(),
                        icon = Icons.Default.Info
                    )
                }
                
                // MuseOS Features
                item {
                    InfiniteUIGlassCard(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(20.dp)
                        ) {
                            Text(
                                text = "Powered By",
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.onSurface,
                                fontWeight = FontWeight.Bold
                            )
                            
                            Spacer(modifier = Modifier.height(12.dp))
                            
                            FeatureItem("InfiniteUI - Magical Design System")
                            FeatureItem("GaxialAI - On-Device Intelligence")
                            FeatureItem("Muse Launcher - Smart App Management")
                            FeatureItem("Muse SystemUI - Beautiful Interface")
                        }
                    }
                }
                
                // Copyright
                item {
                    Text(
                        text = "© 2026 MuseOS Project\nAll rights reserved",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 16.dp)
                    )
                }
            }
        }
    }
}

/**
 * Individual about info card
 */
@Composable
fun AboutInfoCard(
    title: String,
    value: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    modifier: Modifier = Modifier
) {
    InfiniteUIGlassCard(
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(28.dp)
            )
            
            Spacer(modifier = Modifier.width(16.dp))
            
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
                
                Spacer(modifier = Modifier.height(4.dp))
                
                Text(
                    text = value,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

/**
 * Feature list item
 */
@Composable
fun FeatureItem(text: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(vertical = 4.dp)
    ) {
        Icon(
            imageVector = Icons.Default.Check,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(20.dp)
        )
        
        Spacer(modifier = Modifier.width(12.dp))
        
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}
