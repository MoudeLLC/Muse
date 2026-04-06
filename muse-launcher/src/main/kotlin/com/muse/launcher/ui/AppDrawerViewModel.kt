package com.muse.launcher.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.muse.gaxialai.GaxialAI
import com.muse.launcher.ai.AppSuggestionEngine
import com.muse.launcher.data.AppInfo
import com.muse.launcher.data.AppRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class AppDrawerViewModel(
    private val appRepository: AppRepository,
    private val suggestionEngine: AppSuggestionEngine
) : ViewModel() {
    
    private val _allApps = MutableStateFlow<List<AppInfo>>(emptyList())
    val allApps: StateFlow<List<AppInfo>> = _allApps.asStateFlow()
    
    private val _filteredApps = MutableStateFlow<List<AppInfo>>(emptyList())
    val filteredApps: StateFlow<List<AppInfo>> = _filteredApps.asStateFlow()
    
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()
    
    private val _suggestedApps = MutableStateFlow<List<AppInfo>>(emptyList())
    val suggestedApps: StateFlow<List<AppInfo>> = _suggestedApps.asStateFlow()
    
    init {
        loadApps()
        observeSearchQuery()
    }
    
    private fun loadApps() {
        viewModelScope.launch {
            val apps = appRepository.loadInstalledApps()
            _allApps.value = apps
            _filteredApps.value = apps
            loadSuggestions(apps)
        }
    }
    
    private fun loadSuggestions(apps: List<AppInfo>) {
        viewModelScope.launch {
            val context = suggestionEngine.getCurrentContext()
            val suggestions = suggestionEngine.getSuggestions(context, apps, limit = 4)
            _suggestedApps.value = suggestions
        }
    }
    
    private fun observeSearchQuery() {
        viewModelScope.launch {
            _searchQuery
                .debounce(300) // Wait 300ms after user stops typing
                .collect { query ->
                    filterApps(query)
                }
        }
    }
    
    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }
    
    private fun filterApps(query: String) {
        val filtered = if (query.isBlank()) {
            _allApps.value
        } else {
            _allApps.value.filter { app ->
                app.label.contains(query, ignoreCase = true) ||
                app.packageName.contains(query, ignoreCase = true)
            }
        }
        _filteredApps.value = filtered
    }
    
    fun launchApp(appInfo: AppInfo) {
        // Record app launch for AI learning
        val context = com.muse.launcher.data.LaunchContext(
            timestamp = System.currentTimeMillis(),
            timeOfDay = java.util.Calendar.getInstance().get(java.util.Calendar.HOUR_OF_DAY),
            dayOfWeek = java.util.Calendar.getInstance().get(java.util.Calendar.DAY_OF_WEEK),
            location = null,
            previousApp = null
        )
        suggestionEngine.recordAppLaunch(appInfo, context)
    }
    
    fun refreshApps() {
        appRepository.clearCache()
        loadApps()
    }
}
