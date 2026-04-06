package com.muse.launcher.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.muse.gaxialai.GaxialAI
import com.muse.launcher.ai.AppSuggestionEngine
import com.muse.launcher.data.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class HomeScreenViewModel(
    private val appRepository: AppRepository,
    private val widgetManager: WidgetManager,
    private val suggestionEngine: AppSuggestionEngine,
    private val preferences: LauncherPreferences
) : ViewModel() {
    
    private val _homePages = MutableStateFlow<List<HomePage>>(emptyList())
    val homePages: StateFlow<List<HomePage>> = _homePages.asStateFlow()
    
    private val _currentPage = MutableStateFlow(0)
    val currentPage: StateFlow<Int> = _currentPage.asStateFlow()
    
    private val _suggestedApps = MutableStateFlow<List<AppInfo>>(emptyList())
    val suggestedApps: StateFlow<List<AppInfo>> = _suggestedApps.asStateFlow()
    
    private val _allApps = MutableStateFlow<List<AppInfo>>(emptyList())
    val allApps: StateFlow<List<AppInfo>> = _allApps.asStateFlow()
    
    init {
        loadHomePages()
        loadCurrentPage()
        loadApps()
        loadSuggestions()
    }
    
    private fun loadHomePages() {
        viewModelScope.launch {
            preferences.loadHomePages().collect { pages ->
                _homePages.value = pages
            }
        }
    }
    
    private fun loadCurrentPage() {
        viewModelScope.launch {
            preferences.loadCurrentPage().collect { page ->
                _currentPage.value = page
            }
        }
    }
    
    private fun loadApps() {
        viewModelScope.launch {
            val apps = appRepository.loadInstalledApps()
            _allApps.value = apps
        }
    }
    
    private fun loadSuggestions() {
        viewModelScope.launch {
            val context = suggestionEngine.getCurrentContext()
            val apps = _allApps.value
            if (apps.isNotEmpty()) {
                val suggestions = suggestionEngine.getSuggestions(context, apps, limit = 4)
                _suggestedApps.value = suggestions
            }
        }
    }
    
    fun setCurrentPage(page: Int) {
        viewModelScope.launch {
            _currentPage.value = page
            preferences.saveCurrentPage(page)
        }
    }
    
    fun addWidget(widgetInfo: WidgetInfo, position: Position, size: Size) {
        val widgetId = widgetManager.addWidget(widgetInfo, position, size)
        if (widgetId > 0) {
            // Update home page with new widget
            val currentPages = _homePages.value.toMutableList()
            val pageIndex = _currentPage.value
            
            if (pageIndex < currentPages.size) {
                val page = currentPages[pageIndex]
                val updatedPage = page.copy(
                    widgets = page.widgets + WidgetPlacement(widgetId, position, size)
                )
                currentPages[pageIndex] = updatedPage
                
                viewModelScope.launch {
                    _homePages.value = currentPages
                    preferences.saveHomePages(currentPages)
                }
            }
        }
    }
    
    fun removeWidget(widgetId: Int) {
        widgetManager.removeWidget(widgetId)
        
        // Update home pages
        val currentPages = _homePages.value.map { page ->
            page.copy(widgets = page.widgets.filter { it.widgetId != widgetId })
        }
        
        viewModelScope.launch {
            _homePages.value = currentPages
            preferences.saveHomePages(currentPages)
        }
    }
    
    fun moveWidget(widgetId: Int, newPosition: Position) {
        widgetManager.moveWidget(widgetId, newPosition)
        
        // Update home pages
        val currentPages = _homePages.value.map { page ->
            page.copy(
                widgets = page.widgets.map { widget ->
                    if (widget.widgetId == widgetId) {
                        widget.copy(position = newPosition)
                    } else {
                        widget
                    }
                }
            )
        }
        
        viewModelScope.launch {
            _homePages.value = currentPages
            preferences.saveHomePages(currentPages)
        }
    }
    
    fun refreshSuggestions() {
        loadSuggestions()
    }
}
