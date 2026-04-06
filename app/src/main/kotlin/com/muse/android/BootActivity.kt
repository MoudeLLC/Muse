package com.muse.android

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.muse.infiniteui.theme.InfiniteUITheme
import kotlinx.coroutines.delay

class BootActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            InfiniteUITheme {
                BootScreen {
                    // Navigate to main activity after boot
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                }
            }
        }
    }
}

@Composable
fun BootScreen(onBootComplete: () -> Unit) {
    var progress by remember { mutableStateOf(0f) }
    
    LaunchedEffect(Unit) {
        // Simulate boot process
        for (i in 0..100) {
            delay(30)
            progress = i / 100f
        }
        delay(500)
        onBootComplete()
    }
    
    val infiniteTransition = rememberInfiniteTransition(label = "boot")
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "rotation"
    )
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF0F172A),
                        Color(0xFF1E1B4B),
                        Color(0xFF312E81)
                    )
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(32.dp)
        ) {
            // Animated logo
            Canvas(modifier = Modifier.size(120.dp)) {
                val center = Offset(size.width / 2, size.height / 2)
                val radius = size.minDimension / 2
                
                // Outer rotating ring
                drawCircle(
                    brush = Brush.sweepGradient(
                        colors = listOf(
                            Color(0xFF6366F1),
                            Color(0xFF8B5CF6),
                            Color(0xFFEC4899),
                            Color(0xFF6366F1)
                        ),
                        center = center
                    ),
                    radius = radius,
                    center = center,
                    style = Stroke(width = 8f)
                )
                
                // Inner glow
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            Color(0xFF8B5CF6).copy(alpha = 0.6f),
                            Color.Transparent
                        ),
                        center = center
                    ),
                    radius = radius * 0.7f,
                    center = center
                )
            }
            
            // Muse OS text
            Text(
                "MuseOS",
                color = Color.White,
                fontSize = 36.sp,
                fontWeight = FontWeight.Bold
            )
            
            // Loading text
            Text(
                "Initializing...",
                color = Color.White.copy(alpha = 0.7f),
                fontSize = 16.sp
            )
            
            // Progress bar
            Box(
                modifier = Modifier
                    .width(200.dp)
                    .height(4.dp)
                    .background(Color.White.copy(alpha = 0.2f))
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .fillMaxWidth(progress)
                        .background(
                            Brush.horizontalGradient(
                                colors = listOf(
                                    Color(0xFF6366F1),
                                    Color(0xFF8B5CF6),
                                    Color(0xFFEC4899)
                                )
                            )
                        )
                )
            }
            
            Text(
                "${(progress * 100).toInt()}%",
                color = Color.White.copy(alpha = 0.5f),
                fontSize = 14.sp
            )
        }
    }
}
