package com.muse.launcher.ui

import android.content.Context
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import kotlin.math.abs

class GestureHandler(private val context: Context) {
    
    private val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
    
    /**
     * Handle swipe up gesture (open app drawer)
     */
    fun handleSwipeUp(onComplete: () -> Unit) {
        provideHapticFeedback()
        onComplete()
    }
    
    /**
     * Handle swipe down gesture (open notification panel)
     */
    fun handleSwipeDown(onComplete: () -> Unit) {
        provideHapticFeedback()
        onComplete()
    }
    
    /**
     * Handle swipe left gesture (next page)
     */
    fun handleSwipeLeft(onComplete: () -> Unit) {
        onComplete()
    }
    
    /**
     * Handle swipe right gesture (previous page)
     */
    fun handleSwipeRight(onComplete: () -> Unit) {
        onComplete()
    }
    
    /**
     * Handle pinch gesture (show overview)
     */
    fun handlePinch(onComplete: () -> Unit) {
        provideHapticFeedback()
        onComplete()
    }
    
    /**
     * Handle long press gesture (customization mode)
     */
    fun handleLongPress(position: Offset, onComplete: (Offset) -> Unit) {
        provideHapticFeedback(duration = 50)
        onComplete(position)
    }
    
    /**
     * Provide haptic feedback
     */
    private fun provideHapticFeedback(duration: Long = 20) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            vibrator.vibrate(
                VibrationEffect.createOneShot(duration, VibrationEffect.DEFAULT_AMPLITUDE)
            )
        } else {
            @Suppress("DEPRECATION")
            vibrator.vibrate(duration)
        }
    }
}

@Composable
fun rememberGestureHandler(): GestureHandler {
    val context = LocalContext.current
    return remember { GestureHandler(context) }
}

/**
 * Modifier for detecting swipe gestures
 */
fun Modifier.detectSwipeGestures(
    onSwipeUp: () -> Unit = {},
    onSwipeDown: () -> Unit = {},
    onSwipeLeft: () -> Unit = {},
    onSwipeRight: () -> Unit = {}
): Modifier = this.pointerInput(Unit) {
    detectDragGestures(
        onDragEnd = {
            // Gesture completed
        }
    ) { change, dragAmount ->
        change.consume()
        
        val (x, y) = dragAmount
        
        // Determine swipe direction based on drag amount
        when {
            abs(y) > abs(x) -> {
                // Vertical swipe
                if (y < -50) {
                    onSwipeUp()
                } else if (y > 50) {
                    onSwipeDown()
                }
            }
            abs(x) > abs(y) -> {
                // Horizontal swipe
                if (x < -50) {
                    onSwipeLeft()
                } else if (x > 50) {
                    onSwipeRight()
                }
            }
        }
    }
}

/**
 * Modifier for detecting pinch gestures
 */
fun Modifier.detectPinchGesture(
    onPinch: () -> Unit
): Modifier = this.pointerInput(Unit) {
    detectTransformGestures { _, _, zoom, _ ->
        if (zoom < 0.8f) {
            onPinch()
        }
    }
}

/**
 * Modifier for detecting long press
 */
fun Modifier.detectLongPressGesture(
    onLongPress: (Offset) -> Unit
): Modifier = this.pointerInput(Unit) {
    detectTapGestures(
        onLongPress = { offset ->
            onLongPress(offset)
        }
    )
}
