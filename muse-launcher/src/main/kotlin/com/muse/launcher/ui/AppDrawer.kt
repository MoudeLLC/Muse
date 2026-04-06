package com.muse.launcher.ui

import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.graphics.drawable.toBitmap
import com.muse.gaxialai.GaxialAI
import com.muse.infiniteui.components.InfiniteUIGlassCard
import com.muse.infiniteui.components.MagicalBackground
import com.muse.launcher.data.AppInfo
import com.muse.launcher.data.AppRepository
import kotlinx.coroutines.launch

@Composable
fun AppDrawer(
    appRepository: AppRepository,
    gaxialAI: GaxialAI,
    onClose: () -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    
    var allApps by remember { mutableStateOf<List<AppInfo>>(emptyList()) }
    var filteredApps by remember { mutableStateOf<List<AppInfo>>(emptyList()) }
    var searchQuery by remember { mutableStateOf("") }
    var suggestedApps by remember { mutableStateOf<List<AppInfo>>(emptyList()) }
    
    // Load apps
    LaunchedEffect(Unit) {
        scope.launch {
            val apps = appRepository.loadInstalledApps()
            allApps = apps
            filteredApps = apps
            suggestedApps = apps.take(4) // TODO: Get AI suggestions
        }
    }
    
    // Filter apps when search query changes
    LaunchedEffect(searchQuery) {
        filteredApps = if (searchQuery.isBlank()) {
            allApps
        } else {
            allApps.filter { app ->
                app.label.contains(searchQuery, ignoreCase = true)
            }
        }
    }
    
    Box(modifier = Modifier.fillMaxSize()) {
        // Magical background
        MagicalBackground()
        
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // Search bar
            InfiniteUIGlassCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            ) {
                TextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = { Text("Search apps...") },
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.3f),
                        unfocusedContainerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.2f)
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                )
            }
            
            // AI Suggestions section
            if (searchQuery.isBlank() && suggestedApps.isNotEmpty()) {
                InfiniteUIGlassCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "AI Suggestions",
                            style = MaterialTheme.typography.titleMedium
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            suggestedApps.forEach { app ->
                                AppIcon(
                                    app = app,
                                    onClick = {
                                        launchApp(context, app)
                                        onClose()
                                    }
                                )
                            }
                        }
                    }
                }
            }
            
            // All apps grid
            Text(
                text = "All Apps",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            
            LazyVerticalGrid(
                columns = GridCells.Fixed(4),
                contentPadding = PaddingValues(8.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(filteredApps) { app ->
                    AppIcon(
                        app = app,
                        onClick = {
                            launchApp(context, app)
                            onClose()
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun AppIcon(
    app: AppInfo,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .clickable(onClick = onClick)
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // App icon
        Image(
            bitmap = app.icon.toBitmap(64, 64).asImageBitmap(),
            contentDescription = app.label,
            modifier = Modifier.size(48.dp)
        )
        
        Spacer(modifier = Modifier.height(4.dp))
        
        // App label
        Text(
            text = app.label,
            style = MaterialTheme.typography.bodySmall,
            maxLines = 1
        )
    }
}

private fun launchApp(context: android.content.Context, app: AppInfo) {
    val intent = context.packageManager.getLaunchIntentForPackage(app.packageName)
    intent?.let {
        it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(it)
    }
}
