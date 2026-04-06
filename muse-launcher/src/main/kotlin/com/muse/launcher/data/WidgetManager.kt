package com.muse.launcher.data

import android.appwidget.AppWidgetHost
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProviderInfo
import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class WidgetManager(private val context: Context) {
    
    private val appWidgetManager = AppWidgetManager.getInstance(context)
    private val appWidgetHost = AppWidgetHost(context, WIDGET_HOST_ID)
    private val prefs: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    private val gson = Gson()
    
    companion object {
        private const val WIDGET_HOST_ID = 1024
        private const val PREFS_NAME = "widget_config"
        private const val KEY_WIDGETS = "widgets"
    }
    
    /**
     * Get all available widgets from installed apps
     */
    fun getAvailableWidgets(): List<WidgetInfo> {
        return appWidgetManager.installedProviders.map { provider ->
            WidgetInfo(
                provider = provider.provider,
                label = provider.loadLabel(context.packageManager),
                previewImage = provider.loadPreviewImage(context, 0),
                minWidth = provider.minWidth,
                minHeight = provider.minHeight
            )
        }
    }
    
    /**
     * Add a widget to the home screen
     */
    fun addWidget(widgetInfo: WidgetInfo, position: Position, size: Size): Int {
        val widgetId = appWidgetHost.allocateAppWidgetId()
        
        val canBind = appWidgetManager.bindAppWidgetIdIfAllowed(
            widgetId,
            widgetInfo.provider
        )
        
        if (!canBind) {
            // Request permission to bind widget
            // This would need to be handled in the Activity
            return -1
        }
        
        val placement = WidgetPlacement(widgetId, position, size)
        saveWidgetPlacement(placement)
        
        return widgetId
    }
    
    /**
     * Remove a widget
     */
    fun removeWidget(widgetId: Int) {
        appWidgetHost.deleteAppWidgetId(widgetId)
        removeWidgetPlacement(widgetId)
    }
    
    /**
     * Resize a widget
     */
    fun resizeWidget(widgetId: Int, newSize: Size) {
        val placements = loadWidgetConfiguration()
        val updated = placements.map { placement ->
            if (placement.widgetId == widgetId) {
                placement.copy(size = newSize)
            } else {
                placement
            }
        }
        saveWidgetConfiguration(updated)
    }
    
    /**
     * Move a widget to a new position
     */
    fun moveWidget(widgetId: Int, newPosition: Position) {
        val placements = loadWidgetConfiguration()
        val updated = placements.map { placement ->
            if (placement.widgetId == widgetId) {
                placement.copy(position = newPosition)
            } else {
                placement
            }
        }
        saveWidgetConfiguration(updated)
    }
    
    /**
     * Save widget configuration to SharedPreferences
     */
    fun saveWidgetConfiguration() {
        // Configuration is saved automatically in add/remove/resize/move operations
    }
    
    /**
     * Load widget configuration from SharedPreferences
     */
    fun loadWidgetConfiguration(): List<WidgetPlacement> {
        val json = prefs.getString(KEY_WIDGETS, null) ?: return emptyList()
        val type = object : TypeToken<List<WidgetPlacement>>() {}.type
        return try {
            gson.fromJson(json, type)
        } catch (e: Exception) {
            emptyList()
        }
    }
    
    /**
     * Get widget provider info
     */
    fun getWidgetInfo(widgetId: Int): AppWidgetProviderInfo? {
        return appWidgetManager.getAppWidgetInfo(widgetId)
    }
    
    /**
     * Start listening for widget updates
     */
    fun startListening() {
        appWidgetHost.startListening()
    }
    
    /**
     * Stop listening for widget updates
     */
    fun stopListening() {
        appWidgetHost.stopListening()
    }
    
    // Private helper methods
    
    private fun saveWidgetPlacement(placement: WidgetPlacement) {
        val placements = loadWidgetConfiguration().toMutableList()
        placements.add(placement)
        saveWidgetConfiguration(placements)
    }
    
    private fun removeWidgetPlacement(widgetId: Int) {
        val placements = loadWidgetConfiguration().filter { it.widgetId != widgetId }
        saveWidgetConfiguration(placements)
    }
    
    private fun saveWidgetConfiguration(placements: List<WidgetPlacement>) {
        val json = gson.toJson(placements)
        prefs.edit().putString(KEY_WIDGETS, json).apply()
    }
}
