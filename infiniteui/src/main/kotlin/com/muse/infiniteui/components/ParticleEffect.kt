package com.muse.infiniteui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import kotlin.random.Random

/**
 * Particle Effect System
 * Floating particles for magical atmosphere
 */

data class Particle(
    var x: Float,
    var y: Float,
    val speed: Float,
    val size: Float,
    val color: Color
)

@Composable
fun ParticleEffect(
    modifier: Modifier = Modifier,
    particleCount: Int = 30,
    colors: List<Color> = listOf(
        Color(0x408B5CF6),
        Color(0x403B82F6),
        Color(0x4006B6D4)
    )
) {
    var particles by remember {
        mutableStateOf(
            List(particleCount) {
                Particle(
                    x = Random.nextFloat(),
                    y = Random.nextFloat(),
                    speed = Random.nextFloat() * 0.5f + 0.2f,
                    size = Random.nextFloat() * 4f + 2f,
                    color = colors.random()
                )
            }
        )
    }
    
    LaunchedEffect(Unit) {
        while (true) {
            withFrameMillis {
                particles = particles.map { particle ->
                    particle.copy(
                        y = (particle.y - particle.speed * 0.001f).let {
                            if (it < 0f) 1f else it
                        }
                    )
                }
            }
        }
    }
    
    Canvas(modifier = modifier.fillMaxSize()) {
        particles.forEach { particle ->
            drawCircle(
                color = particle.color,
                radius = particle.size,
                center = Offset(
                    x = particle.x * size.width,
                    y = particle.y * size.height
                )
            )
        }
    }
}
