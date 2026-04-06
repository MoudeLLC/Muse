package com.muse.systemui.utils

import android.util.Log

/**
 * Centralized error handling and logging for Muse SystemUI
 */
object ErrorHandler {
    const val TAG = "MuseSystemUI"
    
    /**
     * Handle overlay creation failures
     */
    fun handleOverlayError(error: Exception) {
        Log.e(TAG, "Failed to create overlay", error)
        // Fallback: Retry or show notification
    }
    
    /**
     * Handle notification listener failures
     */
    fun handleNotificationListenerError(error: Exception) {
        Log.e(TAG, "Notification listener error", error)
        // Fallback: Continue without notifications
    }
    
    /**
     * Handle system state monitoring failures
     */
    fun handleSystemStateError(error: Exception) {
        Log.w(TAG, "System state monitoring error", error)
        // Fallback: Use cached values
    }
    
    /**
     * Handle AI analysis failures
     */
    fun handleAIAnalysisError(error: Exception) {
        Log.w(TAG, "AI analysis unavailable, using fallback", error)
        // Fallback: Show notifications without AI features
    }
    
    /**
     * Handle control failures (WiFi, Bluetooth, etc.)
     */
    fun handleControlError(error: Exception) {
        Log.e(TAG, "Control operation failed", error)
        // Fallback: Show error to user
    }
    
    /**
     * Safe execution wrapper
     */
    inline fun <T> safe(fallback: T, block: () -> T): T {
        return try {
            block()
        } catch (e: Exception) {
            Log.e(TAG, "Operation failed, using fallback", e)
            fallback
        }
    }
}
