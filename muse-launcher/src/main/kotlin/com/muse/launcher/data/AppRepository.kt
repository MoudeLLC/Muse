package com.muse.launcher.data

import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AppRepository(private val context: Context) {
    
    private val packageManager: PackageManager = context.packageManager
    private var cachedApps: List<AppInfo>? = null
    
    /**
     * Load all installed applications that can be launched
     */
    suspend fun loadInstalledApps(includeSystemApps: Boolean = false): List<AppInfo> = withContext(Dispatchers.IO) {
        // Return cached if available
        cachedApps?.let { return@withContext it }
        
        val mainIntent = Intent(Intent.ACTION_MAIN, null).apply {
            addCategory(Intent.CATEGORY_LAUNCHER)
        }
        
        val apps = packageManager.queryIntentActivities(mainIntent, 0)
            .mapNotNull { resolveInfo ->
                try {
                    val packageName = resolveInfo.activityInfo.packageName
                    val appInfo = packageManager.getApplicationInfo(packageName, 0)
                    val isSystemApp = (appInfo.flags and ApplicationInfo.FLAG_SYSTEM) != 0
                    
                    // Filter system apps if requested
                    if (!includeSystemApps && isSystemApp) {
                        return@mapNotNull null
                    }
                    
                    AppInfo(
                        packageName = packageName,
                        label = resolveInfo.loadLabel(packageManager).toString(),
                        icon = resolveInfo.loadIcon(packageManager),
                        isSystemApp = isSystemApp,
                        installTime = packageManager.getPackageInfo(packageName, 0).firstInstallTime,
                        lastUpdateTime = packageManager.getPackageInfo(packageName, 0).lastUpdateTime
                    )
                } catch (e: Exception) {
                    null
                }
            }
            .sortedBy { it.label.lowercase() }
        
        cachedApps = apps
        apps
    }
    
    /**
     * Get a specific app by package name
     */
    suspend fun getApp(packageName: String): AppInfo? = withContext(Dispatchers.IO) {
        try {
            val appInfo = packageManager.getApplicationInfo(packageName, 0)
            val isSystemApp = (appInfo.flags and ApplicationInfo.FLAG_SYSTEM) != 0
            
            AppInfo(
                packageName = packageName,
                label = appInfo.loadLabel(packageManager).toString(),
                icon = appInfo.loadIcon(packageManager),
                isSystemApp = isSystemApp,
                installTime = packageManager.getPackageInfo(packageName, 0).firstInstallTime,
                lastUpdateTime = packageManager.getPackageInfo(packageName, 0).lastUpdateTime
            )
        } catch (e: Exception) {
            null
        }
    }
    
    /**
     * Clear the app cache (call when apps are installed/uninstalled)
     */
    fun clearCache() {
        cachedApps = null
    }
    
    /**
     * Search apps by query
     */
    suspend fun searchApps(query: String): List<AppInfo> = withContext(Dispatchers.IO) {
        val allApps = loadInstalledApps()
        if (query.isBlank()) return@withContext allApps
        
        allApps.filter { app ->
            app.label.contains(query, ignoreCase = true) ||
            app.packageName.contains(query, ignoreCase = true)
        }
    }
}
