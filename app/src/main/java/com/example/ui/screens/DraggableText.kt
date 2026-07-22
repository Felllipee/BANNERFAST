package com.example.ui.screens

import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.offset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.sp
import kotlin.math.roundToInt

@Composable
fun DraggableText(
    state: TextEditorState,
    onStateChange: (TextEditorState) -> Unit
) {
    val textStyle = TextStyle(
        color = state.color1,
        fontSize = state.fontSize.sp,
        fontWeight = state.fontWeight,
        fontStyle = FontStyle.Italic,
        letterSpacing = state.letterSpacing.sp,
        fontFamily = state.fontFamily,
        shadow = if (state.shadowBlur > 0 || state.glowIntensity > 0) {
            Shadow(
                color = if (state.glowIntensity > 0) state.glowColor else state.shadowColor,
                blurRadius = if (state.glowIntensity > 0) state.glowIntensity else state.shadowBlur,
                offset = androidx.compose.ui.geometry.Offset(state.shadowOffsetX, state.shadowOffsetY)
            )
        } else null
    )

    Text(
        text = state.text.uppercase(),
        style = textStyle,
        modifier = Modifier
            .offset { IntOffset(state.offset.x.roundToInt(), state.offset.y.roundToInt()) }
            .graphicsLayer(
                scaleX = state.scale,
                scaleY = state.scale * state.scaleY,
                rotationZ = state.rotation,
                alpha = state.opacity
            )
            .pointerInput(Unit) {
                detectTransformGestures { _, pan, zoom, rotation ->
                    onStateChange(
                        state.copy(
                            scale = state.scale * zoom,
                            rotation = state.rotation + rotation,
                            offset = state.offset + pan
                        )
                    )
                }
            }
    )
}
