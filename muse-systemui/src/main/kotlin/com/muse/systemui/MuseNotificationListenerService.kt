package com.muse.systemui

import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class MuseNotificationListenerService : NotificationListenerService() {
    
    companion object {
        private val _notifications = MutableStateFlow<List<StatusBarNotification>>(emptyList())
        val notifications: StateFlow<List<StatusBarNotification>> = _notifications.asStateFlow()
        
        private var instance: MuseNotificationListenerService? = null
        
        fun getInstance(): MuseNotificationListenerService? = instance
    }
    
    override fun onCreate() {
        super.onCreate()
        instance = this
    }
    
    override fun onDestroy() {
        super.onDestroy()
        instance = null
    }
    
    override fun onNotificationPosted(sbn: StatusBarNotification) {
        updateNotifications()
    }
    
    override fun onNotificationRemoved(sbn: StatusBarNotification) {
        updateNotifications()
    }
    
    override fun onListenerConnected() {
        super.onListenerConnected()
        updateNotifications()
    }
    
    private fun updateNotifications() {
        try {
            val activeNotifications = activeNotifications?.toList() ?: emptyList()
            _notifications.value = activeNotifications
        } catch (e: Exception) {
            // Handle error
        }
    }
    
    fun dismissNotification(key: String) {
        try {
            cancelNotification(key)
        } catch (e: Exception) {
            // Handle error
        }
    }
    
    fun dismissAllNotifications() {
        try {
            cancelAllNotifications()
        } catch (e: Exception) {
            // Handle error
        }
    }
}
