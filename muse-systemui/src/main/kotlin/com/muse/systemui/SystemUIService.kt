package com.muse.systemui

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.PixelFormat
import android.os.Build
import android.os.IBinder
import android.view.Gravity
import android.view.WindowManager
import androidx.compose.ui.platform.ComposeView
import com.muse.infiniteui.theme.InfiniteUITheme
import com.muse.systemui.ui.StatusBar

class SystemUIService : Service() {
    
    private lateinit var windowManager: WindowManager
    private var statusBarView: ComposeView? = null
    
    companion object {
        private const val NOTIFICATION_ID = 1001
        private const val CHANNEL_ID = "muse_systemui"
    }
    
    override fun onCreate() {
        super.onCreate()
        windowManager = getSystemService(WINDOW_SERVICE) as WindowManager
        createNotificationChannel()
    }
    
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        startForeground(NOTIFICATION_ID, createNotification())
        createStatusBarOverlay()
        return START_STICKY
    }
    
    override fun onBind(intent: Intent?): IBinder? = null
    
    override fun onDestroy() {
        super.onDestroy()
        removeStatusBarOverlay()
    }
    
    private fun createStatusBarOverlay() {
        if (statusBarView != null) return
        
        val params = WindowManager.LayoutParams(
            WindowManager.LayoutParams.MATCH_PARENT,
            getStatusBarHeight(),
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
            } else {
                @Suppress("DEPRECATION")
                WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY
            },
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or
            WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL or
            WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN or
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            PixelFormat.TRANSLUCENT
        ).apply {
            gravity = Gravity.TOP
        }
        
        statusBarView = ComposeView(this).apply {
            setContent {
                InfiniteUITheme {
                    StatusBar(
                        onTap = { showNotificationPanel() }
                    )
                }
            }
        }
        
        try {
            windowManager.addView(statusBarView, params)
        } catch (e: Exception) {
            // Handle permission denied
            stopSelf()
        }
    }
    
    private fun removeStatusBarOverlay() {
        statusBarView?.let {
            try {
                windowManager.removeView(it)
            } catch (e: Exception) {
                // View already removed
            }
            statusBarView = null
        }
    }
    
    private fun showNotificationPanel() {
        try {
            // Expand status bar to show notifications
            val statusBarManager = getSystemService(Context.STATUS_BAR_SERVICE) as? android.app.StatusBarManager
            statusBarManager?.let {
                // This requires system signature permission, so it may not work on non-system apps
                // Alternative: Show custom notification panel overlay
                android.util.Log.i("SystemUIService", "Notification panel requested")
            }
        } catch (e: Exception) {
            android.util.Log.e("SystemUIService", "Failed to show notification panel", e)
        }
    }
    
    private fun getStatusBarHeight(): Int {
        val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
        return if (resourceId > 0) {
            resources.getDimensionPixelSize(resourceId)
        } else {
            75 // Default height in pixels
        }
    }
    
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Muse SystemUI",
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                description = "Muse SystemUI Service"
                setShowBadge(false)
            }
            
            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }
    
    private fun createNotification(): Notification {
        val builder = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Notification.Builder(this, CHANNEL_ID)
        } else {
            @Suppress("DEPRECATION")
            Notification.Builder(this)
        }
        
        return builder
            .setContentTitle("Muse SystemUI")
            .setContentText("System UI is running")
            .setSmallIcon(android.R.drawable.ic_menu_info_details)
            .setOngoing(true)
            .build()
    }
}
