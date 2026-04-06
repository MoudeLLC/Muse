package com.muse.infiniteui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.unit.dp
import com.muse.infiniteui.effects.*
import com.muse.infiniteui.theme.InfiniteColors

/**
 * InfiniteUI Advanced Components
 * Magical UI with glassmorphism, animations, and lightning effects
 */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InfiniteUITopBar(
    title: String,
    modifier: Modifier = Modifier
) {
    val glowAlpha by rememberMagicalGlow()
    
    TopAppBar(
        title = { 
            Text(
                text = title,
                style = MaterialTheme.typography.headlineLarge,
                modifier = Modifier.shimmerEffect()
            )
        },
        modifier = modifier
            .glassmorphism()
            .drawBehind {
                // Magical glow at the bottom
                drawRect(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent,
                            InfiniteColors.LightningPurple.copy(alpha = glowAlpha * 0.2f)
                        )
                    )
                )
            },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.Transparent
        )
    )
}

@Composable
fun InfiniteUIGlassCard(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    val pulseScale by rememberPulseAnimation()
    
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
            .scale(pulseScale)
            .glassmorphism(
                backgroundColor = InfiniteColors.GlassLight,
                borderColor = InfiniteColors.GlassBorder
            ),
        shape = MaterialTheme.shapes.extraLarge,
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            InfiniteColors.GlassLight,
                            InfiniteColors.GlassLight.copy(alpha = 0.5f)
                        )
                    )
                )
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                content = content
            )
        }
    }
}

@Composable
fun InfiniteUICard(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    InfiniteUIGlassCard(modifier = modifier, content = content)
}

@Composable
fun InfiniteUIMagicalButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val glowAlpha by rememberMagicalGlow()
    
    Button(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .height(64.dp),
        shape = CircleShape,
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.horizontalGradient(
                        colors = listOf(
                            InfiniteColors.LightningPurple.copy(alpha = glowAlpha),
                            InfiniteColors.LightningBlue.copy(alpha = glowAlpha),
                            InfiniteColors.LightningCyan.copy(alpha = glowAlpha)
                        )
                    ),
                    shape = CircleShape
                )
                .blur(8.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = text,
                style = MaterialTheme.typography.labelLarge,
                color = Color.White
            )
        }
    }
}

@Composable
fun InfiniteUIButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    InfiniteUIMagicalButton(text = text, onClick = onClick, modifier = modifier)
}

@Composable
fun InfiniteUICircularElement(
    size: Int = 100,
    color: Color = InfiniteColors.LightningPurple,
    modifier: Modifier = Modifier
) {
    val rotation by rememberRotatingGradient()
    
    Box(
        modifier = modifier
            .size(size.dp)
            .clip(CircleShape)
            .background(
                brush = Brush.sweepGradient(
                    colors = listOf(
                        color,
                        color.copy(alpha = 0.5f),
                        Color.Transparent,
                        color
                    )
                )
            )
            .blur(16.dp)
    )
}

@Composable
fun InfiniteUIContent() {
    Box(modifier = Modifier.fillMaxSize()) {
        // Background magical elements
        InfiniteUICircularElement(
            size = 200,
            color = InfiniteColors.LightningPurple,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .offset(x = 50.dp, y = (-50).dp)
        )
        
        InfiniteUICircularElement(
            size = 150,
            color = InfiniteColors.LightningCyan,
            modifier = Modifier
                .align(Alignment.BottomStart)
                .offset(x = (-30).dp, y = 30.dp)
        )
        
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            InfiniteUIGlassCard {
                Text(
                    text = "Welcome to InfiniteUI",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.shimmerEffect()
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "Experience magical, smooth, and visionary design",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            
            InfiniteUIMagicalButton(
                text = "Explore Magic",
                onClick = { }
            )
        }
    }
}
