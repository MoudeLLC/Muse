package com.muse.android

import android.app.Application
import com.muse.os.MuseOS
import com.muse.gaxialai.GaxialAI

/**
 * Muse Application
 * Main application class initializing MuseOS and GaxialAI
 */
class MuseApplication : Application() {
    
    override fun onCreate() {
        super.onCreate()
        
        // Initialize MuseOS
        MuseOS.initialize(this)
        
        // Initialize GaxialAI
        GaxialAI.getInstance(this).initialize()
    }
}
