package com.muse.launcher

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.muse.infiniteui.theme.InfiniteUITheme
import com.muse.gaxialai.GaxialAI
import com.muse.launcher.data.AppRepository
import com.muse.launcher.data.WidgetManager
import com.muse.launcher.ui.HomeScreen
import com.muse.launcher.ui.AppDrawer

class MuseLauncherActivity : ComponentActivity() {
    
    private lateinit var gaxialAI: GaxialAI
    private lateinit var appRepository: AppRepository
    private lateinit var widgetManager: WidgetManager
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Initialize components
        initializeComponents()
        
        // Set up Compose UI
        setupCompose()
    }
    
    private fun initializeComponents() {
        // Initialize GaxialAI engine
        gaxialAI = GaxialAI.getInstance(this)
        gaxialAI.initialize()
        
        // Initialize app repository
        appRepository = AppRepository(this)
        
        // Initialize widget manager
        widgetManager = WidgetManager(this)
    }
    
    private fun setupCompose() {
        setContent {
            InfiniteUITheme {
                MuseLauncherScreen(
                    appRepository = appRepository,
                    widgetManager = widgetManager,
                    gaxialAI = gaxialAI
                )
            }
        }
    }
    
    override fun onStart() {
        super.onStart()
        widgetManager.startListening()
    }
    
    override fun onStop() {
        super.onStop()
        widgetManager.stopListening()
    }
}

@Composable
fun MuseLauncherScreen(
    appRepository: AppRepository,
    widgetManager: WidgetManager,
    gaxialAI: GaxialAI
) {
    var showAppDrawer by remember { mutableStateOf(false) }
    
    Box(modifier = Modifier.fillMaxSize()) {
        // Home screen
        HomeScreen(
            appRepository = appRepository,
            widgetManager = widgetManager,
            gaxialAI = gaxialAI,
            onOpenAppDrawer = { showAppDrawer = true }
        )
        
        // App drawer overlay
        if (showAppDrawer) {
            AppDrawer(
                appRepository = appRepository,
                gaxialAI = gaxialAI,
                onClose = { showAppDrawer = false }
            )
        }
    }
}
