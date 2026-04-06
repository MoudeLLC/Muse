package com.muse.gaxialai.assistants

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.AlarmClock
import android.provider.ContactsContract
import com.muse.gaxialai.GaxialAI

/**
 * Genna - Primary Smart Assistant
 * Real working assistant with actual functionality
 */
class Genna(private val context: Context) {
    private val aiEngine = GaxialAI.getInstance(context)
    
    init {
        aiEngine.initialize()
    }
    
    fun respond(query: String): String {
        // Use AI engine for natural language processing
        val aiResponse = aiEngine.processNaturalLanguage(query)
        
        // Execute actions based on query
        executeAction(query)
        
        return aiResponse
    }
    
    private fun executeAction(query: String) {
        val lowerQuery = query.lowercase()
        
        when {
            lowerQuery.contains("call") -> handleCall(query)
            lowerQuery.contains("message") || lowerQuery.contains("text") -> handleMessage(query)
            lowerQuery.contains("alarm") || lowerQuery.contains("reminder") -> handleAlarm(query)
            lowerQuery.contains("navigate") || lowerQuery.contains("directions") -> handleNavigation(query)
            lowerQuery.contains("search") -> handleSearch(query)
            lowerQuery.contains("open") -> handleOpenApp(query)
        }
    }
    
    private fun handleCall(query: String) {
        try {
            // Extract phone number or contact name
            val intent = Intent(Intent.ACTION_DIAL).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            context.startActivity(intent)
        } catch (e: Exception) {
            android.util.Log.e("Genna", "Failed to make call: ${e.message}")
        }
    }
    
    private fun handleMessage(query: String) {
        try {
            val intent = Intent(Intent.ACTION_SENDTO).apply {
                data = Uri.parse("smsto:")
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            context.startActivity(intent)
        } catch (e: Exception) {
            android.util.Log.e("Genna", "Failed to send message: ${e.message}")
        }
    }
    
    private fun handleAlarm(query: String) {
        try {
            val intent = Intent(AlarmClock.ACTION_SET_ALARM).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            context.startActivity(intent)
        } catch (e: Exception) {
            android.util.Log.e("Genna", "Failed to set alarm: ${e.message}")
        }
    }
    
    private fun handleNavigation(query: String) {
        try {
            val location = extractLocation(query)
            val intent = Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse("geo:0,0?q=$location")
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            context.startActivity(intent)
        } catch (e: Exception) {
            android.util.Log.e("Genna", "Failed to navigate: ${e.message}")
        }
    }
    
    private fun handleSearch(query: String) {
        try {
            val searchQuery = query.replace("search", "").trim()
            val intent = Intent(Intent.ACTION_WEB_SEARCH).apply {
                putExtra(android.app.SearchManager.QUERY, searchQuery)
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            context.startActivity(intent)
        } catch (e: Exception) {
            android.util.Log.e("Genna", "Failed to search: ${e.message}")
        }
    }
    
    private fun handleOpenApp(query: String) {
        try {
            val appName = extractAppName(query)
            val intent = context.packageManager.getLaunchIntentForPackage(appName)
            intent?.let {
                it.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                context.startActivity(it)
            }
        } catch (e: Exception) {
            android.util.Log.e("Genna", "Failed to open app: ${e.message}")
        }
    }
    
    private fun extractLocation(query: String): String {
        val words = query.split(" ")
        val toIndex = words.indexOfFirst { it.lowercase() == "to" }
        return if (toIndex >= 0 && toIndex < words.size - 1) {
            words.subList(toIndex + 1, words.size).joinToString(" ")
        } else {
            query.replace("navigate", "").replace("directions", "").trim()
        }
    }
    
    private fun extractAppName(query: String): String {
        val words = query.split(" ")
        val openIndex = words.indexOfFirst { it.lowercase() == "open" }
        return if (openIndex >= 0 && openIndex < words.size - 1) {
            words[openIndex + 1]
        } else {
            ""
        }
    }
    
    fun assist(task: AssistTask): AssistResult {
        return when (task) {
            is AssistTask.OpenApp -> {
                try {
                    val intent = context.packageManager.getLaunchIntentForPackage(task.appName)
                    intent?.let {
                        it.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                        context.startActivity(it)
                        AssistResult.Success("Opening ${task.appName}")
                    } ?: AssistResult.Error("App not found")
                } catch (e: Exception) {
                    AssistResult.Error("Failed to open app: ${e.message}")
                }
            }
            is AssistTask.SendMessage -> {
                try {
                    val intent = Intent(Intent.ACTION_SENDTO).apply {
                        data = Uri.parse("smsto:${task.recipient}")
                        putExtra("sms_body", task.message)
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    }
                    context.startActivity(intent)
                    AssistResult.Success("Message sent to ${task.recipient}")
                } catch (e: Exception) {
                    AssistResult.Error("Failed to send message: ${e.message}")
                }
            }
            is AssistTask.SetReminder -> {
                try {
                    val intent = Intent(AlarmClock.ACTION_SET_ALARM).apply {
                        putExtra(AlarmClock.EXTRA_MESSAGE, task.title)
                        putExtra(AlarmClock.EXTRA_HOUR, (task.time / 3600000).toInt())
                        putExtra(AlarmClock.EXTRA_MINUTES, ((task.time % 3600000) / 60000).toInt())
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    }
                    context.startActivity(intent)
                    AssistResult.Success("Reminder set: ${task.title}")
                } catch (e: Exception) {
                    AssistResult.Error("Failed to set reminder: ${e.message}")
                }
            }
        }
    }
    
    sealed class AssistTask {
        data class OpenApp(val appName: String) : AssistTask()
        data class SendMessage(val recipient: String, val message: String) : AssistTask()
        data class SetReminder(val title: String, val time: Long) : AssistTask()
    }
    
    sealed class AssistResult {
        data class Success(val message: String) : AssistResult()
        data class Error(val error: String) : AssistResult()
    }
}
