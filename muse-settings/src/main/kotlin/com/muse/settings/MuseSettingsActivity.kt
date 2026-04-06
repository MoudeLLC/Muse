package com.muse.settings

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.muse.gaxialai.GaxialAI
import com.muse.infiniteui.theme.InfiniteUITheme
import com.muse.settings.navigation.SettingsNavHost

/**
 * Main settings activity for MuseOS
 * Provides comprehensive system settings with AI assistance
 */
class MuseSettingsActivity : ComponentActivity() {
    
    private lateinit var gaxialAI: GaxialAI
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Initialize GaxialAI for AI assistant features
        gaxialAI = GaxialAI.getInstance(applicationContext)
        
        setContent {
            InfiniteUITheme {
                SettingsApp(
                    gaxialAI = gaxialAI,
                    onBackPressed = { finish() }
                )
            }
        }
    }
    
    override fun onDestroy() {
        super.onDestroy()
        // Cleanup if needed
    }
}

@Composable
fun SettingsApp(
    gaxialAI: GaxialAI,
    onBackPressed: () -> Unit
) {
    SettingsNavHost(
        gaxialAI = gaxialAI,
        onBackPressed = onBackPressed,
        modifier = Modifier.fillMaxSize()
    )
}
