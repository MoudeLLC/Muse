package com.muse.launcher.utils

import android.util.Log

/**
 * Centralized error handling and logging for Muse Launcher
 */
object ErrorHandler {
    const val TAG = "MuseLauncher"
    
    /**
     * Handle app loading failures
     */
    fun handleAppLoadingError(error: Exception) {
        Log.e(TAG, "Failed to load apps", error)
        // Fallback: Continue with empty list
    }
    
    /**
     * Handle widget loading failures
     */
    fun handleWidgetLoadingError(error: Exception) {
        Log.e(TAG, "Failed to load widgets", error)
        // Fallback: Continue without widgets
    }
    
    /**
     * Handle AI suggestion failures
     */
    fun handleAISuggestionError(error: Exception) {
        Log.w(TAG, "AI suggestions unavailable, using fallback", error)
        // Fallback: Use rule-based suggestions
    }
    
    /**
     * Handle gesture recognition failures
     */
    fun handleGestureError(error: Exception) {
        Log.w(TAG, "Gesture recognition error", error)
        // Fallback: Ignore gesture
    }
    
    /**
     * Handle persistence failures
     */
    fun handlePersistenceError(error: Exception) {
        Log.e(TAG, "Failed to save launcher state", error)
        // Fallback: Continue without saving
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
