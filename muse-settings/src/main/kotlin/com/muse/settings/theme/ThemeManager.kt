package com.muse.settings.theme

import android.content.Context
import android.content.Intent
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

/**
 * Theme manager for MuseOS
 * Manages theme preferences and broadcasts changes to all components
 */
class ThemeManager private constructor(private val context: Context) {
    
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "theme_preferences")
    private val scope = CoroutineScope(Dispatchers.IO)
    
    companion object {
        @Volatile
        private var instance: ThemeManager? = null
        
        fun getInstance(context: Context): ThemeManager {
            return instance ?: synchronized(this) {
                instance ?: ThemeManager(context.applicationContext).also { instance = it }
            }
        }
        
        // Preference keys
        private val COLOR_SCHEME = stringPreferencesKey("color_scheme")
        private val GLASSMORPHISM_INTENSITY = floatPreferencesKey("glassmorphism_intensity")
        private val PARTICLE_EFFECTS = booleanPreferencesKey("particle_effects")
        private val ANIMATIONS = booleanPreferencesKey("animations")
        
        // Broadcast action for theme changes
        const val ACTION_THEME_CHANGED = "com.muse.settings.THEME_CHANGED"
    }
    
    /**
     * Get current color scheme (light, dark, auto)
     */
    fun getColorScheme(): String {
        var result = "auto"
        scope.launch {
            result = context.dataStore.data.map { preferences ->
                preferences[COLOR_SCHEME] ?: "auto"
            }.first()
        }
        return result
    }
    
    /**
     * Set color scheme and broadcast change
     */
    fun setColorScheme(scheme: String) {
        scope.launch {
            context.dataStore.edit { preferences ->
                preferences[COLOR_SCHEME] = scheme
            }
            broadcastThemeChange()
        }
    }
    
    /**
     * Get glassmorphism intensity (0.0 to 1.0)
     */
    fun getGlassmorphismIntensity(): Float {
        var result = 0.5f
        scope.launch {
            result = context.dataStore.data.map { preferences ->
                preferences[GLASSMORPHISM_INTENSITY] ?: 0.5f
            }.first()
        }
        return result
    }
    
    /**
     * Set glassmorphism intensity and broadcast change
     */
    fun setGlassmorphismIntensity(intensity: Float) {
        scope.launch {
            context.dataStore.edit { preferences ->
                preferences[GLASSMORPHISM_INTENSITY] = intensity.coerceIn(0f, 1f)
            }
            broadcastThemeChange()
        }
    }
    
    /**
     * Get particle effects enabled state
     */
    fun getParticleEffectsEnabled(): Boolean {
        var result = true
        scope.launch {
            result = context.dataStore.data.map { preferences ->
                preferences[PARTICLE_EFFECTS] ?: true
            }.first()
        }
        return result
    }
    
    /**
     * Set particle effects enabled and broadcast change
     */
    fun setParticleEffectsEnabled(enabled: Boolean) {
        scope.launch {
            context.dataStore.edit { preferences ->
                preferences[PARTICLE_EFFECTS] = enabled
            }
            broadcastThemeChange()
        }
    }
    
    /**
     * Get animations enabled state
     */
    fun getAnimationsEnabled(): Boolean {
        var result = true
        scope.launch {
            result = context.dataStore.data.map { preferences ->
                preferences[ANIMATIONS] ?: true
            }.first()
        }
        return result
    }
    
    /**
     * Set animations enabled and broadcast change
     */
    fun setAnimationsEnabled(enabled: Boolean) {
        scope.launch {
            context.dataStore.edit { preferences ->
                preferences[ANIMATIONS] = enabled
            }
            broadcastThemeChange()
        }
    }
    
    /**
     * Broadcast theme change to all MuseOS components
     */
    private fun broadcastThemeChange() {
        val intent = Intent(ACTION_THEME_CHANGED)
        context.sendBroadcast(intent)
    }
}
