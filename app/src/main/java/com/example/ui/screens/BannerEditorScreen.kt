package com.example.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.ui.theme.NeonBlue
import com.example.viewmodel.MainViewModel

@Composable
fun BannerEditorScreen(movieId: Int, isSeries: Boolean, viewModel: MainViewModel, navController: NavController) {
    val movie by viewModel.selectedMovie.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    LaunchedEffect(movieId) {
        viewModel.getMovieDetails(movieId, isSeries)
    }

    if (isLoading || movie == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator(color = NeonBlue)
        }
        return
    }

    val m = movie!!

    var textState by remember(m.title) { 
        mutableStateOf(TextEditorState(text = m.title)) 
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        // Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp)
                .statusBarsPadding(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.clickable { navController.popBackStack() }
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    text = "Editor de Banner",
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Medium
                )
            }
            
            Button(
                onClick = { /* Save action */ },
                colors = ButtonDefaults.buttonColors(containerColor = NeonBlue),
                shape = RoundedCornerShape(50),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                modifier = Modifier.height(36.dp)
            ) {
                Text(
                    text = "SALVAR",
                    color = Color.Black,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
            }
        }

        // Main Editor Canvas
        Box(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .fillMaxWidth()
                .clip(RoundedCornerShape(24.dp))
                .border(1.dp, NeonBlue.copy(alpha = 0.1f), RoundedCornerShape(24.dp))
        ) {
            // Background Image
            Image(
                painter = rememberAsyncImagePainter("https://image.tmdb.org/t/p/w1280${m.backdropPath}"),
                contentDescription = "Backdrop",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

            // Gradients
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.4f), Color.Black),
                            startY = 0f,
                            endY = 1500f
                        )
                    )
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.horizontalGradient(
                            colors = listOf(Color.Black.copy(alpha = 0.6f), Color.Transparent, Color.Transparent)
                        )
                    )
            )

            // Logo Overlay
            Row(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(24.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(NeonBlue),
                    contentAlignment = Alignment.Center
                ) {
                    // Play icon shape simulation
                    Box(
                        modifier = Modifier
                            .offset(x = 1.dp)
                            .size(10.dp, 12.dp)
                            .background(Color.Black) // Simplification for play icon
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "FASTPLAYER",
                    color = Color.White,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Black,
                    letterSpacing = (-0.5).sp
                )
            }

            // Banner Content
            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(32.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .background(Color(0xFFEAB308), RoundedCornerShape(4.dp))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = "IMDb ${String.format("%.1f", m.voteAverage)}",
                            color = Color.Black,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "${m.releaseDate.take(4)} • ${m.genres.joinToString(" / ").uppercase()}",
                        color = Color.White.copy(alpha = 0.7f),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        letterSpacing = 2.sp
                    )
                }
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Box(modifier = Modifier.fillMaxWidth().height(120.dp), contentAlignment = Alignment.CenterStart) {
                    DraggableText(state = textState, onStateChange = { textState = it })
                }
                
                Spacer(modifier = Modifier.height(12.dp))
                
                Text(
                    text = m.overview,
                    color = Color.White.copy(alpha = 0.8f),
                    fontSize = 12.sp,
                    modifier = Modifier.fillMaxWidth(0.8f),
                    maxLines = 2
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Avatars
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    m.castProfiles.take(4).forEachIndexed { index, profilePath ->
                        if (profilePath.isNotEmpty()) {
                            Image(
                                painter = rememberAsyncImagePainter("https://image.tmdb.org/t/p/w185$profilePath"),
                                contentDescription = m.cast.getOrNull(index),
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFF1E293B))
                                    .border(2.dp, NeonBlue.copy(alpha = 0.4f), CircleShape),
                                contentScale = ContentScale.Crop
                            )
                        } else {
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFF1E293B))
                                    .border(2.dp, NeonBlue.copy(alpha = 0.4f), CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = m.cast.getOrNull(index)?.take(1) ?: "",
                                    color = Color.White,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
                
                Spacer(modifier = Modifier.height(24.dp))
                
                Text(
                    text = "JÁ DISPONÍVEL NO FASTPLAYER EM HD",
                    color = NeonBlue,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 2.sp
                )
            }
        }

        // Bottom Controls
        Surface(
            modifier = Modifier.fillMaxWidth().height(300.dp),
            color = Color(0xFF121212),
            shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, Color.White.copy(alpha = 0.05f))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
                    .navigationBarsPadding()
            ) {
                TextEditorControls(state = textState, onStateChange = { textState = it })
            }
        }
    }
}


