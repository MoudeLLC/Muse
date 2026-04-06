package com.muse.launcher.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import com.muse.gaxialai.GaxialAI
import com.muse.infiniteui.components.MagicalBackground
import com.muse.infiniteui.components.ParticleEffect
import com.muse.infiniteui.components.InfiniteUIGlassCard
import com.muse.launcher.data.AppRepository
import com.muse.launcher.data.WidgetManager
import com.muse.launcher.data.AppInfo
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomeScreen(
    appRepository: AppRepository,
    widgetManager: WidgetManager,
    gaxialAI: GaxialAI,
    onOpenAppDrawer: () -> Unit
) {
    val scope = rememberCoroutineScope()
    val pagerState = rememberPagerState(pageCount = { 3 })
    var suggestedApps by remember { mutableStateOf<List<AppInfo>>(emptyList()) }
    
    // Load AI-suggested apps
    LaunchedEffect(Unit) {
        scope.launch {
            // TODO: Get AI suggestions from GaxialAI
            // For now, load recent apps
            suggestedApps = appRepository.loadInstalledApps().take(4)
        }
    }
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectTapGestures(
                    onLongPress = {
                        // TODO: Show widget selection
                    }
                )
            }
    ) {
        // Magical background with particles
        MagicalBackground()
        ParticleEffect()
        
        // Home screen pages
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize()
        ) { page ->
            HomeScreenPage(
                page = page,
                suggestedApps = if (page == 0) suggestedApps else emptyList(),
                widgetManager = widgetManager,
                onOpenAppDrawer = onOpenAppDrawer
            )
        }
    }
}

@Composable
fun HomeScreenPage(
    page: Int,
    suggestedApps: List<AppInfo>,
    widgetManager: WidgetManager,
    onOpenAppDrawer: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        // AI Suggestions section (only on first page)
        if (page == 0 && suggestedApps.isNotEmpty()) {
            InfiniteUIGlassCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "AI Suggestions",
                        style = androidx.compose.material3.MaterialTheme.typography.titleMedium
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    // Display suggested apps
                    suggestedApps.forEach { app ->
                        Text(
                            text = app.label,
                            modifier = Modifier.padding(vertical = 4.dp)
                        )
                    }
                }
            }
        }
        
        // Widgets area
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            // TODO: Render widgets from widgetManager
            Text(
                text = "Page ${page + 1}",
                modifier = Modifier.align(Alignment.Center)
            )
        }
        
        // App drawer indicator
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "Swipe up for apps")
        }
    }
}
