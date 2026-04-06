package com.muse.launcher.ai

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.muse.launcher.data.LaunchContext
import com.muse.launcher.data.UsageStats
import com.muse.launcher.data.TimeSlot
import java.util.Calendar

class UsageTracker(private val context: Context) {
    
    private val prefs: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    private val gson = Gson()
    
    companion object {
        private const val PREFS_NAME = "usage_tracker"
        private const val KEY_USAGE_DATA = "usage_data"
    }
    
    /**
     * Record app usage with context
     */
    fun recordUsage(packageName: String, timestamp: Long, context: LaunchContext) {
        val usageMap = loadUsageData().toMutableMap()
        
        val currentStats = usageMap[packageName] ?: UsageStats(
            packageName = packageName,
            totalLaunchCount = 0,
            lastLaunchTime = 0,
            averageLaunchesPerDay = 0f,
            preferredTimeSlots = emptyList()
        )
        
        val updatedStats = currentStats.copy(
            totalLaunchCount = currentStats.totalLaunchCount + 1,
            lastLaunchTime = timestamp,
            preferredTimeSlots = updateTimeSlots(currentStats.preferredTimeSlots, context.timeOfDay)
        )
        
        usageMap[packageName] = updatedStats
        saveUsageData(usageMap)
    }
    
    /**
     * Get usage statistics for the last N days
     */
    fun getUsageStats(days: Int): List<UsageStats> {
        val usageMap = loadUsageData()
        val cutoffTime = System.currentTimeMillis() - (days * 24 * 60 * 60 * 1000L)
        
        return usageMap.values
            .filter { it.lastLaunchTime >= cutoffTime }
            .sortedByDescending { it.totalLaunchCount }
    }
    
    /**
     * Get most used apps
     */
    fun getMostUsedApps(limit: Int): List<String> {
        return loadUsageData().values
            .sortedByDescending { it.totalLaunchCount }
            .take(limit)
            .map { it.packageName }
    }
    
    /**
     * Get apps used at specific time of day
     */
    fun getAppsForTimeOfDay(hour: Int): List<String> {
        return loadUsageData().values
            .filter { stats ->
                stats.preferredTimeSlots.any { slot ->
                    hour >= slot.startHour && hour < slot.endHour
                }
            }
            .sortedByDescending { stats ->
                stats.preferredTimeSlots
                    .filter { slot -> hour >= slot.startHour && hour < slot.endHour }
                    .sumOf { it.launchCount }
            }
            .map { it.packageName }
    }
    
    /**
     * Clear old usage data
     */
    fun clearOldData(daysToKeep: Int) {
        val cutoffTime = System.currentTimeMillis() - (daysToKeep * 24 * 60 * 60 * 1000L)
        val usageMap = loadUsageData()
        
        val filtered = usageMap.filterValues { it.lastLaunchTime >= cutoffTime }
        saveUsageData(filtered)
    }
    
    // Private helper methods
    
    private fun loadUsageData(): Map<String, UsageStats> {
        val json = prefs.getString(KEY_USAGE_DATA, null) ?: return emptyMap()
        val type = object : TypeToken<Map<String, UsageStats>>() {}.type
        return try {
            gson.fromJson(json, type)
        } catch (e: Exception) {
            emptyMap()
        }
    }
    
    private fun saveUsageData(data: Map<String, UsageStats>) {
        val json = gson.toJson(data)
        prefs.edit().putString(KEY_USAGE_DATA, json).apply()
    }
    
    private fun updateTimeSlots(currentSlots: List<TimeSlot>, hour: Int): List<TimeSlot> {
        val slotSize = 4 // 4-hour time slots
        val slotStart = (hour / slotSize) * slotSize
        val slotEnd = slotStart + slotSize
        
        val existingSlot = currentSlots.find { it.startHour == slotStart }
        
        return if (existingSlot != null) {
            currentSlots.map { slot ->
                if (slot.startHour == slotStart) {
                    slot.copy(launchCount = slot.launchCount + 1)
                } else {
                    slot
                }
            }
        } else {
            currentSlots + TimeSlot(slotStart, slotEnd, 1)
        }
    }
}
