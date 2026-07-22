package com.example.ui.screens

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight

data class TextEditorState(
    val text: String = "",
    val offset: Offset = Offset.Zero,
    val scale: Float = 1f,
    val rotation: Float = 0f,
    val fontFamily: FontFamily = FontFamily.Default,
    val fontCategory: String = "Cinemática",
    val fontSize: Float = 48f,
    val letterSpacing: Float = -2f,
    val lineSpacing: Float = 1f,
    val fontWeight: FontWeight = FontWeight.Black,
    val scaleY: Float = 1f,
    
    val color1: Color = Color(0xFF00E5FF),
    val color2: Color = Color.Transparent,
    val useGradient: Boolean = false,
    
    val outlineColor: Color = Color.Transparent,
    val outlineThickness: Float = 0f,
    
    val shadowColor: Color = Color.Black,
    val shadowBlur: Float = 0f,
    val shadowOffsetX: Float = 0f,
    val shadowOffsetY: Float = 0f,
    
    val glowColor: Color = Color(0xFF00E5FF),
    val glowIntensity: Float = 0f,
    
    val opacity: Float = 1f,
    
    val currentStylePreset: String = "Nenhum",
    val currentEffect: String = "Nenhum"
)
