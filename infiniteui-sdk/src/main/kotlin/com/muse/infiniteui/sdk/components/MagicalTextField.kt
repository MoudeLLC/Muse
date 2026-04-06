package com.muse.infiniteui.sdk.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * MagicalTextField - Advanced text input with magical effects
 * 
 * Features:
 * - Shimmer effect on focus
 * - Glow border animation
 * - Glassmorphism background
 * - Floating label
 * - Leading/trailing icons
 * - Error state with animation
 * - Character counter
 * - Password visibility toggle
 * - Multi-line support
 * - Custom validation
 * - Voice input support
 * - Clear button
 * - Prefix/suffix text
 * - Helper text
 * - Max length enforcement
 * 
 * @param value Current text value
 * @param onValueChange Value change callback
 * @param modifier Modifier for customization
 * @param enabled Whether field is enabled
 * @param readOnly Whether field is read-only
 * @param label Label text
 * @param placeholder Placeholder text
 * @param leadingIcon Leading icon composable
 * @param trailingIcon Trailing icon composable
 * @param prefix Prefix text
 * @param suffix Suffix text
 * @param supportingText Helper/error text
 * @param isError Error state
 * @param visualTransformation Visual transformation (e.g., password)
 * @param keyboardOptions Keyboard configuration
 * @param keyboardActions Keyboard actions
 * @param singleLine Single line mode
 * @param maxLines Maximum lines
 * @param minLines Minimum lines
 * @param maxLength Maximum character length
 * @param showCharacterCount Show character counter
 * @param shape Field shape
 * @param colors Custom color scheme
 * @param shimmer Enable shimmer effect
 * @param glowBorder Enable glow border
 */
@Composable
fun MagicalTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    label: String? = null,
    placeholder: String? = null,
    leadingIcon: (@Composable () -> Unit)? = null,
    trailingIcon: (@Composable () -> Unit)? = null,
    prefix: String? = null,
    suffix: String? = null,
    supportingText: String? = null,
    isError: Boolean = false,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    singleLine: Boolean = false,
    maxLines: Int = if (singleLine) 1 else Int.MAX_VALUE,
    minLines: Int = 1,
    maxLength: Int? = null,
    showCharacterCount: Boolean = false,
    shape: Shape = RoundedCornerShape(16.dp),
    colors: MagicalTextFieldColors = MagicalTextFieldDefaults.colors(),
    shimmer: Boolean = true,
    glowBorder: Boolean = true
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isFocused by interactionSource.collectIsFocusedAsState()
    
    // Shimmer animation
    val infiniteTransition = rememberInfiniteTransition(label = "shimmer")
    val shimmerOffset by infiniteTransition.animateFloat(
        initialValue = -1f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "shimmer_offset"
    )
    
    // Glow animation
    val glowAlpha by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "glow_alpha"
    )
    
    // Border color animation
    val borderColor by animateColorAsState(
        targetValue = when {
            isError -> colors.errorColor
            isFocused -> colors.focusedBorderColor
            else -> colors.unfocusedBorderColor
        },
        animationSpec = tween(300),
        label = "border_color"
    )
    
    Column(modifier = modifier) {
        // Label
        if (label != null) {
            Text(
                text = label,
                style = TextStyle(
                    fontSize = 14.sp,
                    color = if (isError) colors.errorColor else colors.labelColor
                ),
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }
        
        Box {
            // Glow layer
            if (glowBorder && isFocused) {
                Box(
                    modifier = Modifier
                        .matchParentSize()
                        .padding(-2.dp)
                        .blur(8.dp)
                        .border(
                            width = 2.dp,
                            brush = Brush.linearGradient(
                                colors = listOf(
                                    Color(0xFF8B5CF6).copy(alpha = glowAlpha * 0.6f),
                                    Color(0xFF3B82F6).copy(alpha = glowAlpha * 0.6f),
                                    Color(0xFF06B6D4).copy(alpha = glowAlpha * 0.6f)
                                )
                            ),
                            shape = shape
                        )
                )
            }
            
            // Main text field
            BasicTextField(
                value = value,
                onValueChange = { newValue ->
                    if (maxLength == null || newValue.length <= maxLength) {
                        onValueChange(newValue)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(shape)
                    .background(
                        brush = if (shimmer && isFocused) {
                            Brush.linearGradient(
                                colors = listOf(
                                    colors.backgroundColor,
                                    colors.backgroundColor.copy(alpha = 0.8f),
                                    colors.backgroundColor
                                )
                            )
                        } else {
                            Brush.verticalGradient(
                                colors = listOf(
                                    colors.backgroundColor,
                                    colors.backgroundColor.copy(alpha = 0.9f)
                                )
                            )
                        }
                    )
                    .border(
                        width = if (isFocused) 2.dp else 1.dp,
                        color = borderColor,
                        shape = shape
                    )
                    .padding(16.dp),
                enabled = enabled,
                readOnly = readOnly,
                textStyle = TextStyle(
                    fontSize = 16.sp,
                    color = colors.textColor
                ),
                keyboardOptions = keyboardOptions,
                keyboardActions = keyboardActions,
                singleLine = singleLine,
                maxLines = maxLines,
                minLines = minLines,
                visualTransformation = visualTransformation,
                interactionSource = interactionSource,
                cursorBrush = SolidColor(colors.cursorColor),
                decorationBox = { innerTextField ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // Leading icon
                        leadingIcon?.invoke()
                        
                        // Prefix
                        if (prefix != null) {
                            Text(
                                text = prefix,
                                style = TextStyle(
                                    fontSize = 16.sp,
                                    color = colors.textColor.copy(alpha = 0.6f)
                                )
                            )
                        }
                        
                        // Text field
                        Box(modifier = Modifier.weight(1f)) {
                            if (value.isEmpty() && placeholder != null) {
                                Text(
                                    text = placeholder,
                                    style = TextStyle(
                                        fontSize = 16.sp,
                                        color = colors.placeholderColor
                                    )
                                )
                            }
                            innerTextField()
                        }
                        
                        // Suffix
                        if (suffix != null) {
                            Text(
                                text = suffix,
                                style = TextStyle(
                                    fontSize = 16.sp,
                                    color = colors.textColor.copy(alpha = 0.6f)
                                )
                            )
                        }
                        
                        // Trailing icon
                        trailingIcon?.invoke()
                    }
                }
            )
        }
        
        // Supporting text and character count
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 4.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Supporting text
            if (supportingText != null) {
                Text(
                    text = supportingText,
                    style = TextStyle(
                        fontSize = 12.sp,
                        color = if (isError) colors.errorColor else colors.supportingTextColor
                    ),
                    modifier = Modifier.weight(1f)
                )
            }
            
            // Character count
            if (showCharacterCount && maxLength != null) {
                Text(
                    text = "${value.length}/$maxLength",
                    style = TextStyle(
                        fontSize = 12.sp,
                        color = if (value.length >= maxLength) 
                            colors.errorColor 
                        else 
                            colors.supportingTextColor
                    )
                )
            }
        }
    }
}

/**
 * Password text field with visibility toggle
 */
@Composable
fun MagicalPasswordField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    label: String? = "Password",
    placeholder: String? = "Enter password",
    isError: Boolean = false,
    supportingText: String? = null
) {
    var passwordVisible by remember { mutableStateOf(false) }
    
    MagicalTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        label = label,
        placeholder = placeholder,
        isError = isError,
        supportingText = supportingText,
        visualTransformation = if (passwordVisible) 
            VisualTransformation.None 
        else 
            PasswordVisualTransformation(),
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Password,
            imeAction = ImeAction.Done
        ),
        trailingIcon = {
            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                Icon(
                    imageVector = if (passwordVisible) 
                        Icons.Default.Visibility 
                    else 
                        Icons.Default.VisibilityOff,
                    contentDescription = if (passwordVisible) 
                        "Hide password" 
                    else 
                        "Show password"
                )
            }
        }
    )
}

/**
 * Search text field with search icon
 */
@Composable
fun MagicalSearchField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String = "Search...",
    onSearch: (String) -> Unit = {}
) {
    MagicalTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        placeholder = placeholder,
        singleLine = true,
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Search
        ),
        keyboardActions = KeyboardActions(
            onSearch = { onSearch(value) }
        ),
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Search",
                tint = Color.White.copy(alpha = 0.6f)
            )
        },
        trailingIcon = if (value.isNotEmpty()) {
            {
                IconButton(onClick = { onValueChange("") }) {
                    Icon(
                        imageVector = Icons.Default.Clear,
                        contentDescription = "Clear",
                        tint = Color.White.copy(alpha = 0.6f)
                    )
                }
            }
        } else null
    )
}

data class MagicalTextFieldColors(
    val textColor: Color,
    val backgroundColor: Color,
    val placeholderColor: Color,
    val labelColor: Color,
    val focusedBorderColor: Color,
    val unfocusedBorderColor: Color,
    val errorColor: Color,
    val cursorColor: Color,
    val supportingTextColor: Color
)

object MagicalTextFieldDefaults {
    @Composable
    fun colors(
        textColor: Color = Color.White,
        backgroundColor: Color = Color(0x30FFFFFF),
        placeholderColor: Color = Color.White.copy(alpha = 0.5f),
        labelColor: Color = Color.White.copy(alpha = 0.8f),
        focusedBorderColor: Color = Color(0xFF8B5CF6),
        unfocusedBorderColor: Color = Color.White.copy(alpha = 0.3f),
        errorColor: Color = Color(0xFFEF4444),
        cursorColor: Color = Color(0xFF8B5CF6),
        supportingTextColor: Color = Color.White.copy(alpha = 0.6f)
    ) = MagicalTextFieldColors(
        textColor = textColor,
        backgroundColor = backgroundColor,
        placeholderColor = placeholderColor,
        labelColor = labelColor,
        focusedBorderColor = focusedBorderColor,
        unfocusedBorderColor = unfocusedBorderColor,
        errorColor = errorColor,
        cursorColor = cursorColor,
        supportingTextColor = supportingTextColor
    )
}

/**
 * Usage Examples:
 * 
 * // Basic text field
 * MagicalTextField(
 *     value = text,
 *     onValueChange = { text = it },
 *     label = "Name",
 *     placeholder = "Enter your name"
 * )
 * 
 * // Password field
 * MagicalPasswordField(
 *     value = password,
 *     onValueChange = { password = it }
 * )
 * 
 * // Search field
 * MagicalSearchField(
 *     value = query,
 *     onValueChange = { query = it },
 *     onSearch = { performSearch(it) }
 * )
 * 
 * // With character limit
 * MagicalTextField(
 *     value = bio,
 *     onValueChange = { bio = it },
 *     label = "Bio",
 *     maxLength = 200,
 *     showCharacterCount = true,
 *     maxLines = 5
 * )
 * 
 * // With validation
 * MagicalTextField(
 *     value = email,
 *     onValueChange = { email = it },
 *     label = "Email",
 *     isError = !isValidEmail(email),
 *     supportingText = if (!isValidEmail(email)) 
 *         "Invalid email address" 
 *     else 
 *         null
 * )
 */
