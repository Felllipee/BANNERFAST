@file:OptIn(ExperimentalLayoutApi::class)
package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import androidx.compose.ui.geometry.Offset

@Composable
fun TextEditorControls(
    state: TextEditorState,
    onStateChange: (TextEditorState) -> Unit
) {
    var selectedTab by remember { mutableStateOf("FONTES") }
    val tabs = listOf("ESTILOS", "FONTES", "CORES", "EFEITOS", "AJUSTES", "ALINHAR")

    Column(modifier = Modifier.fillMaxWidth()) {
        ScrollableTabRow(
            selectedTabIndex = tabs.indexOf(selectedTab),
            containerColor = Color.Transparent,
            edgePadding = 16.dp,
            indicator = {},
            divider = {}
        ) {
            tabs.forEach { tab ->
                val isSelected = selectedTab == tab
                Text(
                    text = tab,
                    color = if (isSelected) Color(0xFF00E5FF) else Color.White.copy(alpha = 0.5f),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .clickable { selectedTab = tab }
                        .padding(horizontal = 12.dp, vertical = 8.dp)
                )
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))

        when (selectedTab) {
            "ESTILOS" -> StylesTab(state, onStateChange)
            "FONTES" -> FontsTab(state, onStateChange)
            "CORES" -> ColorsTab(state, onStateChange)
            "EFEITOS" -> EffectsTab(state, onStateChange)
            "AJUSTES" -> AdjustmentsTab(state, onStateChange)
            "ALINHAR" -> AlignmentTab(state, onStateChange)
        }
    }
}

@Composable
fun StylesTab(state: TextEditorState, onStateChange: (TextEditorState) -> Unit) {
    val styles = listOf("Anime Neon", "Cyberpunk", "Cinema", "Terror", "Fantasia", "Sci-Fi", "Luxo", "Glow Azul", "Blood Red", "Premium", "Blockbuster", "Hollywood")
    FlowRow(modifier = Modifier.padding(horizontal = 16.dp), horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        styles.forEach { style ->
            Box(
                modifier = Modifier
                    .border(1.dp, if (state.currentStylePreset == style) Color(0xFF00E5FF) else Color.White.copy(alpha = 0.2f), RoundedCornerShape(8.dp))
                    .clickable { 
                        // Appy preset logic here
                        var newState = state.copy(currentStylePreset = style)
                        when(style) {
                            "Anime Neon" -> newState = newState.copy(color1 = Color(0xFFFF00FF), glowColor = Color(0xFFFF00FF), glowIntensity = 20f, fontFamily = FontFamily.Cursive)
                            "Cyberpunk" -> newState = newState.copy(color1 = Color(0xFF00FFFF), color2 = Color(0xFFFF00FF), useGradient = true, glowColor = Color(0xFF00FFFF), glowIntensity = 15f)
                            "Terror" -> newState = newState.copy(color1 = Color(0xFF8B0000), outlineColor = Color.Black, outlineThickness = 2f, fontFamily = FontFamily.Serif)
                            "Sci-Fi" -> newState = newState.copy(color1 = Color(0xFF00E5FF), glowColor = Color(0xFF00E5FF), glowIntensity = 25f, letterSpacing = 4f, fontFamily = FontFamily.Monospace)
                            "Glow Azul" -> newState = newState.copy(color1 = Color.White, glowColor = Color.Blue, glowIntensity = 30f)
                            "Blood Red" -> newState = newState.copy(color1 = Color.Red, shadowColor = Color.Black, shadowBlur = 10f)
                            "Cinema" -> newState = newState.copy(color1 = Color.White, letterSpacing = 2f, fontFamily = FontFamily.SansSerif, shadowColor = Color.Black, shadowBlur = 8f)
                            "Fantasia" -> newState = newState.copy(color1 = Color(0xFFFFD700), fontFamily = FontFamily.Cursive, shadowColor = Color.Black, shadowBlur = 5f)
                        }
                        onStateChange(newState)
                    }
                    .padding(horizontal = 12.dp, vertical = 8.dp)
            ) {
                Text(text = style, color = Color.White, fontSize = 12.sp)
            }
        }
    }
}

@Composable
fun FontsTab(state: TextEditorState, onStateChange: (TextEditorState) -> Unit) {
    val categories = listOf("Cinemática", "Ação", "Terror", "Anime", "Fantasia", "Ficção Científica", "Super-heróis", "Neon", "Graffiti", "Elegante", "Minimalista", "Cyberpunk", "Retrô", "Medieval", "Futurista")
    Column {
        Row(modifier = Modifier.horizontalScroll(rememberScrollState()).padding(horizontal = 16.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            categories.forEach { cat ->
                Text(
                    text = cat,
                    color = if (state.fontCategory == cat) Color(0xFF00E5FF) else Color.Gray,
                    fontSize = 14.sp,
                    modifier = Modifier.clickable { onStateChange(state.copy(fontCategory = cat)) }.padding(8.dp)
                )
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        // Mocking 80+ fonts by showing generic names
        FlowRow(modifier = Modifier.padding(horizontal = 16.dp), horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            (1..12).forEach { i ->
                val fontName = "${state.fontCategory} Font $i"
                Box(
                    modifier = Modifier
                        .border(1.dp, Color.White.copy(alpha = 0.2f), RoundedCornerShape(8.dp))
                        .clickable { 
                            // In a real app we'd load the actual font.
                            val newFamily = when (state.fontCategory) {
                                "Elegante", "Medieval", "Terror" -> FontFamily.Serif
                                "Anime", "Fantasia", "Graffiti" -> FontFamily.Cursive
                                "Ficção Científica", "Cyberpunk", "Futurista" -> FontFamily.Monospace
                                else -> FontFamily.SansSerif
                            }
                            onStateChange(state.copy(fontFamily = newFamily))
                        }
                        .padding(horizontal = 12.dp, vertical = 8.dp)
                ) {
                    Text(text = fontName, color = Color.White, fontSize = 14.sp, fontFamily = state.fontFamily)
                }
            }
        }
    }
}

@Composable
fun ColorsTab(state: TextEditorState, onStateChange: (TextEditorState) -> Unit) {
    val palettes = listOf(
        Color(0xFF00E5FF), Color(0xFFA855F7), Color(0xFF8B0000), Color(0xFF10B981), 
        Color(0xFFFFD700), Color.White, Color.Black, Color(0xFFF97316)
    )
    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        Text("Cores Prontas", color = Color.White, fontSize = 12.sp)
        Spacer(modifier = Modifier.height(8.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            palettes.forEach { color ->
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(color)
                        .border(2.dp, if (state.color1 == color) Color.White else Color.Transparent, CircleShape)
                        .clickable { onStateChange(state.copy(color1 = color, useGradient = false)) }
                )
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(checked = state.useGradient, onCheckedChange = { onStateChange(state.copy(useGradient = it)) })
            Text("Usar Gradiente", color = Color.White, fontSize = 12.sp)
        }
    }
}

@Composable
fun EffectsTab(state: TextEditorState, onStateChange: (TextEditorState) -> Unit) {
    val effects = listOf("Glow Neon", "Contorno", "Sombra", "Reflexo", "Brilho", "Metal", "Vidro (Glass)", "Fogo", "Gelo", "Elétrico", "Holográfico", "Cromo", "Ouro", "Prata", "Textura de tinta", "Efeito pincel", "Efeito spray", "3D", "Letras rachadas", "Letras desgastadas", "Letras luminosas")
    FlowRow(modifier = Modifier.padding(horizontal = 16.dp), horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        effects.forEach { effect ->
            Box(
                modifier = Modifier
                    .border(1.dp, if (state.currentEffect == effect) Color(0xFF00E5FF) else Color.White.copy(alpha = 0.2f), RoundedCornerShape(8.dp))
                    .clickable { 
                        var newState = state.copy(currentEffect = effect)
                        when(effect) {
                            "Glow Neon" -> newState = newState.copy(glowIntensity = 25f, shadowBlur = 0f)
                            "Contorno" -> newState = newState.copy(outlineThickness = 2f, outlineColor = Color.White)
                            "Sombra" -> newState = newState.copy(shadowBlur = 10f, shadowOffsetX = 5f, shadowOffsetY = 5f)
                            "3D" -> newState = newState.copy(shadowBlur = 0f, shadowOffsetX = 10f, shadowOffsetY = 10f, shadowColor = Color.DarkGray)
                            "Ouro" -> newState = newState.copy(color1 = Color(0xFFFFD700), color2 = Color(0xFFB8860B), useGradient = true, shadowBlur = 5f)
                        }
                        onStateChange(newState)
                    }
                    .padding(horizontal = 12.dp, vertical = 8.dp)
            ) {
                Text(text = effect, color = Color.White, fontSize = 12.sp)
            }
        }
    }
}

@Composable
fun AdjustmentsTab(state: TextEditorState, onStateChange: (TextEditorState) -> Unit) {
    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        AdjustmentSlider("Tamanho", state.fontSize, 12f, 120f) { onStateChange(state.copy(fontSize = it)) }
        AdjustmentSlider("Espaçamento Letras", state.letterSpacing, -10f, 20f) { onStateChange(state.copy(letterSpacing = it)) }
        AdjustmentSlider("Espessura (Weight)", state.fontWeight.weight.toFloat(), 100f, 900f) { onStateChange(state.copy(fontWeight = FontWeight(it.toInt()))) }
        AdjustmentSlider("Opacidade", state.opacity, 0f, 1f) { onStateChange(state.copy(opacity = it)) }
        AdjustmentSlider("Glow Intensidade", state.glowIntensity, 0f, 50f) { onStateChange(state.copy(glowIntensity = it)) }
        AdjustmentSlider("Sombra Blur", state.shadowBlur, 0f, 30f) { onStateChange(state.copy(shadowBlur = it)) }
        AdjustmentSlider("Contorno Espessura", state.outlineThickness, 0f, 10f) { onStateChange(state.copy(outlineThickness = it)) }
    }
}

@Composable
fun AlignmentTab(state: TextEditorState, onStateChange: (TextEditorState) -> Unit) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
        val alignments = listOf("Top", "Center", "Bottom", "Left", "Right")
        alignments.forEach { align ->
            Button(onClick = {
                // We mock alignment by setting offset to 0 or specific values based on parent.
                // For simplicity, just reset offset here.
                onStateChange(state.copy(offset = Offset.Zero)) 
            }, colors = ButtonDefaults.buttonColors(containerColor = Color.DarkGray)) {
                Text(align, fontSize = 12.sp)
            }
        }
    }
}

@Composable
fun AdjustmentSlider(label: String, value: Float, min: Float, max: Float, onValueChange: (Float) -> Unit) {
    Column {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(label, color = Color.White.copy(alpha = 0.7f), fontSize = 12.sp)
            Text(String.format("%.1f", value), color = Color(0xFF00E5FF), fontSize = 12.sp)
        }
        Slider(
            value = value,
            onValueChange = onValueChange,
            valueRange = min..max,
            colors = SliderDefaults.colors(activeTrackColor = Color(0xFF00E5FF), thumbColor = Color(0xFF00E5FF))
        )
    }
}


