package com.muse.infiniteui.theme

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp

/**
 * InfiniteUI Shape System
 * Circular and smooth rounded shapes for magical aesthetic
 */
val InfiniteUIShapes = Shapes(
    extraSmall = RoundedCornerShape(12.dp),
    small = RoundedCornerShape(16.dp),
    medium = RoundedCornerShape(24.dp),
    large = RoundedCornerShape(32.dp),
    extraLarge = RoundedCornerShape(40.dp)
)

// Additional circular shapes for special components
object InfiniteShapes {
    val Circle = CircleShape
    val Pill = RoundedCornerShape(50)
    val SuperRounded = RoundedCornerShape(48.dp)
}
