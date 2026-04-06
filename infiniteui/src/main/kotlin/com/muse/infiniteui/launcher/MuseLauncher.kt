package com.muse.infiniteui.launcher

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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
import com.muse.infiniteui.theme.InfiniteUITheme
import com.muse.infiniteui.components.MagicalBackground
import com.muse.infiniteui.components.InfiniteUIGlassCard

/**
 * MuseLauncher - System Launcher for MuseOS
 * Real working launcher that displays installed apps
 */
class MuseLauncher : ComponentActivity() {
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        setContent {
            InfiniteUITheme {
                Box(modifier = Modifier.fillMaxSize()) {
                    MagicalBackground()
                    LauncherScreen()
                }
            }
        }
    }
}

data class AppInfo(
    val name: String,
    val packageName: String,
    val icon: android.graphics.drawable.Drawable?
)

@Composable
fun LauncherScreen() {
    val context = LocalContext.current
    val packageManager = context.packageManager
    
    // Get all installed apps
    val apps = remember {
        val intent = Intent(Intent.ACTION_MAIN, null).apply {
            addCategory(Intent.CATEGORY_LAUNCHER)
        }
        
        packageManager.queryIntentActivities(intent, PackageManager.MATCH_ALL)
            .map { resolveInfo ->
                AppInfo(
                    name = resolveInfo.loadLabel(packageManager).toString(),
                    packageName = resolveInfo.activityInfo.packageName,
                    icon = resolveInfo.loadIcon(packageManager)
                )
            }
            .sortedBy { it.name }
    }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Search bar
        InfiniteUIGlassCard(
            modifier = Modifier.padding(bottom = 16.dp)
        ) {
            Text(
                text = "Search apps...",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
        }
        
        // App grid
        LazyVerticalGrid(
            columns = GridCells.Fixed(4),
            contentPadding = PaddingValues(8.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(apps) { app ->
                AppIcon(app = app) {
                    // Launch app
                    val launchIntent = packageManager.getLaunchIntentForPackage(app.packageName)
                    launchIntent?.let { context.startActivity(it) }
                }
            }
        }
    }
}

@Composable
fun AppIcon(app: AppInfo, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .clickable(onClick = onClick)
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // App icon
        app.icon?.let { drawable ->
            val bitmap = remember { drawable.toBitmap().asImageBitmap() }
            androidx.compose.foundation.Image(
                bitmap = bitmap,
                contentDescription = app.name,
                modifier = Modifier.size(56.dp)
            )
        }
        
        Spacer(modifier = Modifier.height(8.dp))
        
        // App name
        Text(
            text = app.name,
            style = MaterialTheme.typography.bodySmall,
            maxLines = 2,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}
