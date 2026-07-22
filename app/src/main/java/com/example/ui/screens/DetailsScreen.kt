package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.viewmodel.MainViewModel
import java.text.NumberFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailsScreen(movieId: Int, isSeries: Boolean, viewModel: MainViewModel, navController: NavController) {
    val movie by viewModel.selectedMovie.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    LaunchedEffect(movieId) {
        viewModel.getMovieDetails(movieId, isSeries)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(movie?.title ?: "") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent),
                modifier = Modifier.background(Brush.verticalGradient(listOf(Color.Black.copy(alpha=0.8f), Color.Transparent)))
            )
        }
    ) { paddingValues ->
        if (isLoading || movie == null) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            val m = movie!!
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {
                // Backdrop Image
                Box(modifier = Modifier.fillMaxWidth().aspectRatio(16f / 9f)) {
                    AsyncImage(
                        model = "https://image.tmdb.org/t/p/w1280${m.backdropPath}",
                        contentDescription = "Backdrop",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                    Box(modifier = Modifier
                        .fillMaxSize()
                        .background(Brush.verticalGradient(listOf(Color.Transparent, MaterialTheme.colorScheme.background)))
                    )
                }

                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = m.title,
                        style = MaterialTheme.typography.headlineLarge,
                        fontWeight = FontWeight.Black,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Título original: ${m.originalTitle}", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Badge { Text(m.releaseDate.take(4)) }
                        Badge { Text("⭐ ${String.format("%.1f", m.voteAverage)}") }
                        if (m.runtime > 0) Badge { Text("${m.runtime} min") }
                        if (m.country.isNotBlank()) Badge { Text(m.country) }
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                    Text(m.overview, style = MaterialTheme.typography.bodyMedium)
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("Gênero: ${m.genres.joinToString(", ")}", style = MaterialTheme.typography.bodySmall)
                    if (m.director.isNotBlank()) Text("Diretor: ${m.director}", style = MaterialTheme.typography.bodySmall)
                    if (m.budget > 0) Text("Orçamento: ${NumberFormat.getCurrencyInstance(Locale.US).format(m.budget)}", style = MaterialTheme.typography.bodySmall)
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("Elenco", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Text(m.cast.joinToString(", "), style = MaterialTheme.typography.bodySmall)
                    
                    Spacer(modifier = Modifier.height(32.dp))
                    
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                        Button(
                            onClick = { navController.navigate("create_banner/${m.id}/${isSeries}") },
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary, contentColor = Color.Black)
                        ) {
                            Text("Criar Banner", fontWeight = FontWeight.Bold)
                        }
                        Button(
                            onClick = { navController.navigate("create_video/${m.id}/${isSeries}") },
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary, contentColor = Color.Black)
                        ) {
                            Text("Criar Vídeo", fontWeight = FontWeight.Bold)
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(32.dp))
                }
            }
        }
    }
}
