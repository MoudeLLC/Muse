package com.muse.android.assistant

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.muse.gaxialai.GaxialAI
import com.muse.gaxialai.assistants.Genna
import com.muse.infiniteui.theme.InfiniteUITheme
import com.muse.infiniteui.components.MagicalBackground
import com.muse.infiniteui.components.InfiniteUIGlassCard
import com.muse.infiniteui.components.InfiniteUITopBar
import kotlinx.coroutines.launch

/**
 * GennaActivity - AI Assistant Interface
 * Real working voice and text assistant
 */
class GennaActivity : ComponentActivity() {
    
    private lateinit var speechRecognizer: SpeechRecognizer
    private lateinit var genna: Genna
    
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            startVoiceRecognition()
        }
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Initialize AI
        GaxialAI.getInstance(this).initialize()
        genna = Genna(this)
        
        // Initialize speech recognizer
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this)
        
        setContent {
            InfiniteUITheme {
                Box(modifier = Modifier.fillMaxSize()) {
                    MagicalBackground()
                    AssistantScreen(
                        genna = genna,
                        onVoiceInput = { checkPermissionAndStartVoice() }
                    )
                }
            }
        }
    }
    
    private fun checkPermissionAndStartVoice() {
        when {
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.RECORD_AUDIO
            ) == PackageManager.PERMISSION_GRANTED -> {
                startVoiceRecognition()
            }
            else -> {
                requestPermissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
            }
        }
    }
    
    private fun startVoiceRecognition() {
        val intent = android.content.Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, "en-US")
        }
        speechRecognizer.startListening(intent)
    }
    
    override fun onDestroy() {
        super.onDestroy()
        speechRecognizer.destroy()
    }
}

data class Message(
    val text: String,
    val isUser: Boolean,
    val timestamp: Long = System.currentTimeMillis()
)

@Composable
fun AssistantScreen(
    genna: Genna,
    onVoiceInput: () -> Unit
) {
    var messages by remember { mutableStateOf(listOf<Message>()) }
    var inputText by remember { mutableStateOf("") }
    var isProcessing by remember { mutableStateOf(false) }
    val listState = rememberLazyListState()
    val scope = rememberCoroutineScope()
    
    // Welcome message
    LaunchedEffect(Unit) {
        messages = listOf(
            Message(
                text = "Hi! I'm Genna, your AI assistant. How can I help you today?",
                isUser = false
            )
        )
    }
    
    Column(modifier = Modifier.fillMaxSize()) {
        InfiniteUITopBar(title = "Genna Assistant")
        
        // Messages list
        LazyColumn(
            state = listState,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(messages) { message ->
                MessageBubble(message = message)
            }
            
            if (isProcessing) {
                item {
                    ProcessingIndicator()
                }
            }
        }
        
        // Input area
        InfiniteUIGlassCard(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextField(
                    value = inputText,
                    onValueChange = { inputText = it },
                    modifier = Modifier.weight(1f),
                    placeholder = { Text("Ask me anything...") },
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.3f),
                        unfocusedContainerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.3f)
                    )
                )
                
                Spacer(modifier = Modifier.width(8.dp))
                
                // Voice input button
                IconButton(onClick = onVoiceInput) {
                    Icon(
                        imageVector = Icons.Default.Mic,
                        contentDescription = "Voice input",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
                
                // Send button
                IconButton(
                    onClick = {
                        if (inputText.isNotBlank()) {
                            val userMessage = inputText
                            inputText = ""
                            
                            // Add user message
                            messages = messages + Message(userMessage, true)
                            
                            // Process with AI
                            isProcessing = true
                            scope.launch {
                                try {
                                    val response = genna.respond(userMessage)
                                    messages = messages + Message(response, false)
                                } catch (e: Exception) {
                                    messages = messages + Message(
                                        "Sorry, I encountered an error: ${e.message}",
                                        false
                                    )
                                } finally {
                                    isProcessing = false
                                    // Scroll to bottom
                                    listState.animateScrollToItem(messages.size - 1)
                                }
                            }
                        }
                    },
                    enabled = inputText.isNotBlank() && !isProcessing
                ) {
                    Icon(
                        imageVector = Icons.Default.Send,
                        contentDescription = "Send",
                        tint = if (inputText.isNotBlank()) 
                            MaterialTheme.colorScheme.primary 
                        else 
                            MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
                    )
                }
            }
        }
    }
}

@Composable
fun MessageBubble(message: Message) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (message.isUser) Arrangement.End else Arrangement.Start
    ) {
        InfiniteUIGlassCard(
            modifier = Modifier.widthIn(max = 280.dp)
        ) {
            Text(
                text = message.text,
                style = MaterialTheme.typography.bodyMedium,
                color = if (message.isUser) 
                    MaterialTheme.colorScheme.onPrimaryContainer 
                else 
                    MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@Composable
fun ProcessingIndicator() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Start
    ) {
        InfiniteUIGlassCard(
            modifier = Modifier.widthIn(max = 100.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                repeat(3) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(8.dp),
                        strokeWidth = 2.dp
                    )
                }
            }
        }
    }
}
