package com.muse.settings.ui

import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.muse.infiniteui.components.InfiniteUIGlassCard
import com.muse.infiniteui.components.MagicalBackground
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * App info data model
 */
data class AppInfo(
    val packageName: String,
    val appName: String,
    val isSystemApp: Boolean
)

/**
 * Apps settings screen
 * Displays installed apps and allows management
 */
@Composable
fun AppsSettingsScreen(
    onBackPressed: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    
    var apps by remember { mutableStateOf<List<AppInfo>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var searchQuery by remember { mutableStateOf("") }
    
    // Load apps
    LaunchedEffect(Unit) {
        scope.launch {
            apps = loadInstalledApps(context)
            isLoading = false
        }
    }
    
    val filteredApps = remember(apps, searchQuery) {
        if (searchQuery.isEmpty()) {
            apps
        } else {
            apps.filter { it.appName.contains(searchQuery, ignoreCase = true) }
        }
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
                modifier = Modifier.padding(bottom = 16.dp)
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
                    text = "Apps",
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
            
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
                    leadingIcon = {
                        Icon(Icons.Default.Search, contentDescription = "Search")
                    },
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.3f),
                        unfocusedContainerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.2f)
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
            }
            
            // Apps list
            if (isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(filteredApps) { app ->
                        AppInfoCard(
                            app = app,
                            onClick = { /* Open app details */ }
                        )
                    }
                }
            }
        }
    }
}

/**
 * Individual app info card
 */
@Composable
fun AppInfoCard(
    app: AppInfo,
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
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Settings,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(32.dp)
            )
            
            Spacer(modifier = Modifier.width(16.dp))
            
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = app.appName,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                
                Text(
                    text = if (app.isSystemApp) "System app" else "User app",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
            }
            
            Icon(
                imageVector = Icons.Default.ArrowForward,
                contentDescription = "Details",
                tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
            )
        }
    }
}

/**
 * Load installed apps from PackageManager
 */
private suspend fun loadInstalledApps(context: Context): List<AppInfo> = withContext(Dispatchers.IO) {
    val packageManager = context.packageManager
    val packages = packageManager.getInstalledApplications(PackageManager.GET_META_DATA)
    
    packages.map { appInfo ->
        AppInfo(
            packageName = appInfo.packageName,
            appName = appInfo.loadLabel(packageManager).toString(),
            isSystemApp = (appInfo.flags and ApplicationInfo.FLAG_SYSTEM) != 0
        )
    }.sortedBy { it.appName }
}
