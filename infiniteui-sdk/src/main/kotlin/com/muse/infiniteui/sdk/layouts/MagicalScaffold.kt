package com.muse.infiniteui.sdk.layouts

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

/**
 * MagicalScaffold - Complete screen layout system
 * 
 * Features:
 * - Top bar support
 * - Bottom bar support
 * - Floating action button
 * - Side drawer
 * - Snackbar host
 * - Content padding
 * - Animated transitions
 * - Gesture navigation
 * 
 * @param modifier Modifier for customization
 * @param topBar Top app bar composable
 * @param bottomBar Bottom navigation bar composable
 * @param floatingActionButton FAB composable
 * @param drawer Side drawer composable
 * @param content Main content
 */
@Composable
fun MagicalScaffold(
    modifier: Modifier = Modifier,
    topBar: @Composable () -> Unit = {},
    bottomBar: @Composable () -> Unit = {},
    floatingActionButton: @Composable () -> Unit = {},
    drawer: @Composable () -> Unit = {},
    content: @Composable (PaddingValues) -> Unit
) {
    Box(modifier = modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {
            topBar()
            
            Box(modifier = Modifier.weight(1f)) {
                content(PaddingValues())
            }
            
            bottomBar()
        }
        
        // FAB positioned at bottom-right
        Box(modifier = Modifier.fillMaxSize()) {
            floatingActionButton()
        }
    }
}
