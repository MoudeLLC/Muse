package com.muse.settings.utils

import android.util.Log

/**
 * Centralized error handling and logging for Muse Settings
 */
object ErrorHandler {
    const val TAG = "MuseSettings"
    
    /**
     * Handle settings provider failures
     */
    fun handleSettingsProviderError(error: Exception) {
        Log.e(TAG, "Settings provider error", error)
        // Fallback: Use default values
    }
    
    /**
     * Handle system info retrieval failures
     */
    fun handleSystemInfoError(error: Exception) {
        Log.w(TAG, "System info retrieval error", error)
        // Fallback: Show "N/A" or cached values
    }
    
    /**
     * Handle AI assistant failures
     */
    fun handleAIAssistantError(error: Exception) {
        Log.w(TAG, "AI assistant unavailable, using fallback", error)
        // Fallback: Use basic search
    }
    
    /**
     * Handle theme application failures
     */
    fun handleThemeError(error: Exception) {
        Log.e(TAG, "Theme application failed", error)
        // Fallback: Use default theme
    }
    
    /**
     * Handle permission denial
     */
    fun handlePermissionDenied(permission: String) {
        Log.w(TAG, "Permission denied: $permission")
        // Fallback: Show explanation to user
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
