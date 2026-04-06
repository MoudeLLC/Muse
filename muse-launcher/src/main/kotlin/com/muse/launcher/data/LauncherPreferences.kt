package com.muse.launcher.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "launcher_prefs")

class LauncherPreferences(private val context: Context) {
    
    private val gson = Gson()
    
    companion object {
        private val KEY_HOME_PAGES = stringPreferencesKey("home_pages")
        private val KEY_CURRENT_PAGE = intPreferencesKey("current_page")
        private val KEY_GRID_SIZE = intPreferencesKey("grid_size")
        private val KEY_ICON_SIZE = intPreferencesKey("icon_size")
    }
    
    /**
     * Save home pages configuration
     */
    suspend fun saveHomePages(pages: List<HomePage>) {
        context.dataStore.edit { prefs ->
            val json = gson.toJson(pages)
            prefs[KEY_HOME_PAGES] = json
        }
    }
    
    /**
     * Load home pages configuration
     */
    fun loadHomePages(): Flow<List<HomePage>> {
        return context.dataStore.data.map { prefs ->
            val json = prefs[KEY_HOME_PAGES] ?: return@map getDefaultHomePages()
            try {
                val type = object : TypeToken<List<HomePage>>() {}.type
                gson.fromJson(json, type)
            } catch (e: Exception) {
                getDefaultHomePages()
            }
        }
    }
    
    /**
     * Save current page index
     */
    suspend fun saveCurrentPage(page: Int) {
        context.dataStore.edit { prefs: MutablePreferences ->
            prefs[KEY_CURRENT_PAGE] = page
        }
    }
    
    /**
     * Load current page index
     */
    fun loadCurrentPage(): Flow<Int> {
        return context.dataStore.data.map { prefs ->
            prefs[KEY_CURRENT_PAGE] ?: 0
        }
    }
    
    /**
     * Save grid size (number of columns)
     */
    suspend fun saveGridSize(size: Int) {
        context.dataStore.edit { prefs: MutablePreferences ->
            prefs[KEY_GRID_SIZE] = size
        }
    }
    
    /**
     * Load grid size
     */
    fun loadGridSize(): Flow<Int> {
        return context.dataStore.data.map { prefs ->
            prefs[KEY_GRID_SIZE] ?: 4 // Default 4 columns
        }
    }
    
    /**
     * Save icon size
     */
    suspend fun saveIconSize(size: Int) {
        context.dataStore.edit { prefs: MutablePreferences ->
            prefs[KEY_ICON_SIZE] = size
        }
    }
    
    /**
     * Load icon size
     */
    fun loadIconSize(): Flow<Int> {
        return context.dataStore.data.map { prefs ->
            prefs[KEY_ICON_SIZE] ?: 48 // Default 48dp
        }
    }
    
    /**
     * Clear all preferences
     */
    suspend fun clearAll() {
        context.dataStore.edit { prefs ->
            prefs.clear()
        }
    }
    
    // Private helper methods
    
    private fun getDefaultHomePages(): List<HomePage> {
        return listOf(
            HomePage(
                id = "page_0",
                widgets = emptyList(),
                shortcuts = emptyList()
            ),
            HomePage(
                id = "page_1",
                widgets = emptyList(),
                shortcuts = emptyList()
            ),
            HomePage(
                id = "page_2",
                widgets = emptyList(),
                shortcuts = emptyList()
            )
        )
    }
}
