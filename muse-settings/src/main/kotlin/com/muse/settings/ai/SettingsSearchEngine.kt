package com.muse.settings.ai

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import com.muse.gaxialai.GaxialAI
import com.muse.settings.ui.SettingSearchResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * AI-powered settings search engine
 * Uses GaxialAI for natural language understanding and ranking
 */
class SettingsSearchEngine(private val gaxialAI: GaxialAI) {
    
    // Settings index with keywords
    private val settingsIndex = listOf(
        SettingSearchResult(
            title = "WiFi Settings",
            category = "Network & Internet",
            explanation = "Connect to WiFi networks, manage saved networks, and configure WiFi preferences",
            icon = Icons.Default.Settings,
            route = "network"
        ),
        SettingSearchResult(
            title = "Bluetooth",
            category = "Network & Internet",
            explanation = "Pair and manage Bluetooth devices, control Bluetooth connectivity",
            icon = Icons.Default.Settings,
            route = "network"
        ),
        SettingSearchResult(
            title = "Mobile Data",
            category = "Network & Internet",
            explanation = "Enable or disable mobile data, monitor data usage",
            icon = Icons.Default.Settings,
            route = "network"
        ),
        SettingSearchResult(
            title = "Brightness",
            category = "Display",
            explanation = "Adjust screen brightness level for better visibility and battery life",
            icon = Icons.Default.Settings,
            route = "display"
        ),
        SettingSearchResult(
            title = "Screen Timeout",
            category = "Display",
            explanation = "Set how long the screen stays on when inactive",
            icon = Icons.Default.Settings,
            route = "display"
        ),
        SettingSearchResult(
            title = "Font Size",
            category = "Display",
            explanation = "Change the size of text displayed on screen",
            icon = Icons.Default.Settings,
            route = "display"
        ),
        SettingSearchResult(
            title = "Volume Controls",
            category = "Sound & Vibration",
            explanation = "Adjust ringtone, media, notification, and alarm volumes",
            icon = Icons.Default.Settings,
            route = "sound"
        ),
        SettingSearchResult(
            title = "App Management",
            category = "Apps",
            explanation = "View installed apps, manage permissions, and uninstall applications",
            icon = Icons.Default.Settings,
            route = "apps"
        ),
        SettingSearchResult(
            title = "Theme Customization",
            category = "Appearance",
            explanation = "Customize color scheme, glassmorphism effects, particles, and animations",
            icon = Icons.Default.Settings,
            route = "theme"
        ),
        SettingSearchResult(
            title = "System Information",
            category = "System",
            explanation = "View CPU, RAM, storage, battery, and network information",
            icon = Icons.Default.Info,
            route = "system_info"
        ),
        SettingSearchResult(
            title = "About MuseOS",
            category = "System",
            explanation = "View MuseOS version, build information, and device details",
            icon = Icons.Default.Info,
            route = "about"
        )
    )
    
    /**
     * Search settings using AI-powered natural language understanding
     */
    suspend fun search(query: String): List<SettingSearchResult> = withContext(Dispatchers.Default) {
        if (query.isBlank()) return@withContext emptyList()
        
        // Use GaxialAI to understand the query
        val aiUnderstanding = try {
            gaxialAI.processNaturalLanguage(
                "User is searching for a setting: '$query'. " +
                "What setting are they looking for? Respond with keywords only."
            )
        } catch (e: Exception) {
            query // Fallback to original query
        }
        
        // Search with both original query and AI understanding
        val searchTerms = (query + " " + aiUnderstanding).lowercase()
        
        // Rank results by relevance
        settingsIndex
            .map { setting ->
                val score = calculateRelevanceScore(setting, searchTerms)
                Pair(setting, score)
            }
            .filter { it.second > 0 }
            .sortedByDescending { it.second }
            .map { it.first }
            .take(5)
    }
    
    /**
     * Calculate relevance score for a setting
     */
    private fun calculateRelevanceScore(setting: SettingSearchResult, searchTerms: String): Int {
        var score = 0
        val terms = searchTerms.split(" ").filter { it.length > 2 }
        
        for (term in terms) {
            // Title match (highest weight)
            if (setting.title.contains(term, ignoreCase = true)) {
                score += 10
            }
            
            // Category match
            if (setting.category.contains(term, ignoreCase = true)) {
                score += 5
            }
            
            // Explanation match
            if (setting.explanation.contains(term, ignoreCase = true)) {
                score += 3
            }
        }
        
        return score
    }
    
    /**
     * Get contextual AI suggestions based on common queries
     */
    fun getContextualSuggestions(): List<String> {
        return listOf(
            "How do I connect to WiFi?",
            "How to change screen brightness?",
            "Where can I adjust volume?",
            "How to customize the theme?",
            "Show me battery information",
            "How to manage installed apps?",
            "What is my device model?",
            "How to enable Bluetooth?"
        )
    }
}
