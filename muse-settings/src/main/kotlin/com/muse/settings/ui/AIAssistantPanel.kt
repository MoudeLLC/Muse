package com.muse.settings.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.muse.gaxialai.GaxialAI
import com.muse.infiniteui.components.InfiniteUIGlassCard
import com.muse.infiniteui.components.MagicalBackground
import com.muse.settings.ai.SettingsSearchEngine
import kotlinx.coroutines.launch

/**
 * AI assistant panel for settings
 * Provides intelligent search and contextual help
 */
@Composable
fun AIAssistantPanel(
    gaxialAI: GaxialAI,
    onBackPressed: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scope = rememberCoroutineScope()
    val searchEngine = remember { SettingsSearchEngine(gaxialAI) }
    
    var searchQuery by remember { mutableStateOf("") }
    var searchResults by remember { mutableStateOf<List<SettingSearchResult>>(emptyList()) }
    var aiSuggestions by remember { mutableStateOf<List<String>>(emptyList()) }
    var isSearching by remember { mutableStateOf(false) }
    
    // Load AI suggestions on start
    LaunchedEffect(Unit) {
        aiSuggestions = searchEngine.getContextualSuggestions()
    }
    
    Box(modifier = modifier.fillMaxSize()) {
        MagicalBackground()
        
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // Header
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 16.dp)
            ) {
                IconButton(onClick = onBackPressed) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
                
                Spacer(modifier = Modifier.width(8.dp))
                
                Text(
                    text = "AI Assistant",
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
            
            // Search bar
            InfiniteUIGlassCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            ) {
                TextField(
                    value = searchQuery,
                    onValueChange = { query ->
                        searchQuery = query
                        if (query.isNotEmpty()) {
                            isSearching = true
                            scope.launch {
                                searchResults = searchEngine.search(query)
                                isSearching = false
                            }
                        } else {
                            searchResults = emptyList()
                        }
                    },
                    placeholder = { Text("Ask me anything about settings...") },
                    leadingIcon = {
                        Icon(Icons.Default.Search, contentDescription = "AI Search")
                    },
                    trailingIcon = {
                        if (isSearching) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(24.dp),
                                strokeWidth = 2.dp
                            )
                        }
                    },
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.3f),
                        unfocusedContainerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.2f)
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
            }
            
            // Results or suggestions
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                if (searchQuery.isEmpty()) {
                    // Show AI suggestions
                    item {
                        Text(
                            text = "Suggested Questions",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                    }
                    
                    items(aiSuggestions) { suggestion ->
                        SuggestionCard(
                            suggestion = suggestion,
                            onClick = { searchQuery = suggestion }
                        )
                    }
                } else {
                    // Show search results
                    items(searchResults) { result ->
                        SearchResultCard(result = result)
                    }
                    
                    if (searchResults.isEmpty() && !isSearching) {
                        item {
                            InfiniteUIGlassCard(
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(20.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Search,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                                        modifier = Modifier.size(48.dp)
                                    )
                                    
                                    Spacer(modifier = Modifier.height(16.dp))
                                    
                                    Text(
                                        text = "No results found",
                                        style = MaterialTheme.typography.bodyLarge,
                                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                                    )
                                    
                                    Text(
                                        text = "Try asking in a different way",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

/**
 * Suggestion card
 */
@Composable
fun SuggestionCard(
    suggestion: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    InfiniteUIGlassCard(
        modifier = modifier
            .fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Info,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(24.dp)
            )
            
            Spacer(modifier = Modifier.width(16.dp))
            
            Text(
                text = suggestion,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.weight(1f)
            )
            
            IconButton(onClick = onClick) {
                Icon(
                    imageVector = Icons.Default.ArrowForward,
                    contentDescription = "Use suggestion",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

/**
 * Search result card
 */
@Composable
fun SearchResultCard(
    result: SettingSearchResult,
    modifier: Modifier = Modifier
) {
    InfiniteUIGlassCard(
        modifier = modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = result.icon,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(28.dp)
                )
                
                Spacer(modifier = Modifier.width(16.dp))
                
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = result.title,
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    
                    Text(
                        text = result.category,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            Text(
                text = result.explanation,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
            )
        }
    }
}

/**
 * Setting search result data model
 */
data class SettingSearchResult(
    val title: String,
    val category: String,
    val explanation: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector,
    val route: String
)
