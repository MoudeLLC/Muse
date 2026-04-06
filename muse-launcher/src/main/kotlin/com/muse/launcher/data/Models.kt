package com.muse.launcher.data

import android.graphics.drawable.Drawable
import android.content.ComponentName

// App Information
data class AppInfo(
    val packageName: String,
    val label: String,
    val icon: Drawable,
    val isSystemApp: Boolean,
    val installTime: Long,
    val lastUpdateTime: Long
)

// Widget Information
data class WidgetInfo(
    val provider: ComponentName,
    val label: String,
    val previewImage: Drawable?,
    val minWidth: Int,
    val minHeight: Int
)

data class WidgetPlacement(
    val widgetId: Int,
    val position: Position,
    val size: Size
)

// Layout Models
data class HomePage(
    val id: String,
    val widgets: List<WidgetPlacement>,
    val shortcuts: List<AppShortcut>
)

data class AppShortcut(
    val appInfo: AppInfo,
    val position: Position
)

data class Position(
    val x: Int,
    val y: Int
)

data class Size(
    val width: Int,
    val height: Int
)

// AI Tracking Models
data class LaunchContext(
    val timestamp: Long,
    val timeOfDay: Int,
    val dayOfWeek: Int,
    val location: String?,
    val previousApp: String?
)

data class UsageStats(
    val packageName: String,
    val totalLaunchCount: Int,
    val lastLaunchTime: Long,
    val averageLaunchesPerDay: Float,
    val preferredTimeSlots: List<TimeSlot>
)

data class TimeSlot(
    val startHour: Int,
    val endHour: Int,
    val launchCount: Int
)
