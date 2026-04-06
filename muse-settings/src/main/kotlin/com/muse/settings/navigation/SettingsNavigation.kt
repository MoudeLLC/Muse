package com.muse.settings.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.muse.gaxialai.GaxialAI

/**
 * Navigation routes for settings screens
 */
sealed class SettingsRoute(val route: String) {
    object Main : SettingsRoute("main")
    object Network : SettingsRoute("network")
    object Display : SettingsRoute("display")
    object Sound : SettingsRoute("sound")
    object Apps : SettingsRoute("apps")
    object About : SettingsRoute("about")
    object Theme : SettingsRoute("theme")
    object SystemInfo : SettingsRoute("system_info")
    object AIAssistant : SettingsRoute("ai_assistant")
}

/**
 * Settings navigation host with InfiniteUI styling
 */
@Composable
fun SettingsNavHost(
    navController: NavHostController = rememberNavController(),
    gaxialAI: GaxialAI,
    onBackPressed: () -> Unit,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = SettingsRoute.Main.route,
        modifier = modifier
    ) {
        composable(SettingsRoute.Main.route) {
            com.muse.settings.ui.MainSettingsScreen(
                gaxialAI = gaxialAI,
                onBackPressed = onBackPressed,
                onNavigateToCategory = { route ->
                    navController.navigate(route)
                }
            )
        }
        
        composable(SettingsRoute.Network.route) {
            com.muse.settings.ui.NetworkSettingsScreen(
                onBackPressed = { navController.popBackStack() }
            )
        }
        
        composable(SettingsRoute.Display.route) {
            com.muse.settings.ui.DisplaySettingsScreen(
                onBackPressed = { navController.popBackStack() }
            )
        }
        
        composable(SettingsRoute.Sound.route) {
            com.muse.settings.ui.SoundSettingsScreen(
                onBackPressed = { navController.popBackStack() }
            )
        }
        
        composable(SettingsRoute.Apps.route) {
            com.muse.settings.ui.AppsSettingsScreen(
                onBackPressed = { navController.popBackStack() }
            )
        }
        
        composable(SettingsRoute.About.route) {
            com.muse.settings.ui.AboutScreen(
                onBackPressed = { navController.popBackStack() }
            )
        }
        
        composable(SettingsRoute.Theme.route) {
            com.muse.settings.ui.ThemeCustomizationScreen(
                onBackPressed = { navController.popBackStack() }
            )
        }
        
        composable(SettingsRoute.SystemInfo.route) {
            com.muse.settings.ui.SystemInfoScreen(
                onBackPressed = { navController.popBackStack() }
            )
        }
        
        composable(SettingsRoute.AIAssistant.route) {
            com.muse.settings.ui.AIAssistantPanel(
                gaxialAI = gaxialAI,
                onBackPressed = { navController.popBackStack() }
            )
        }
    }
}
