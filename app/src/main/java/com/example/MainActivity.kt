package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.data.MovieRepository
import com.example.data.local.AppDatabase
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.example.ui.screens.DetailsScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.theme.MyApplicationTheme
import com.example.viewmodel.MainViewModel
import com.example.viewmodel.MainViewModelFactory

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    
    val database = AppDatabase.getDatabase(this)
    val repository = MovieRepository(database.movieDao())
    val factory = MainViewModelFactory(repository)

    setContent {
      MyApplicationTheme {
        Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
            val navController = rememberNavController()
            val viewModel: MainViewModel = viewModel(factory = factory)
            
            NavHost(navController = navController, startDestination = "splash") {
                composable("splash") {
                    com.example.ui.screens.SplashScreen(navController)
                }
                composable("home") {
                    HomeScreen(viewModel, navController)
                }
                composable(
                    "details/{movieId}/{isSeries}",
                    arguments = listOf(
                        navArgument("movieId") { type = NavType.IntType },
                        navArgument("isSeries") { type = NavType.BoolType }
                    )
                ) { backStackEntry ->
                    val movieId = backStackEntry.arguments?.getInt("movieId") ?: return@composable
                    val isSeries = backStackEntry.arguments?.getBoolean("isSeries") ?: false
                    DetailsScreen(movieId, isSeries, viewModel, navController)
                }
                composable(
                    "create_banner/{movieId}/{isSeries}",
                    arguments = listOf(
                        navArgument("movieId") { type = NavType.IntType },
                        navArgument("isSeries") { type = NavType.BoolType }
                    )
                ) { backStackEntry ->
                    val movieId = backStackEntry.arguments?.getInt("movieId") ?: return@composable
                    val isSeries = backStackEntry.arguments?.getBoolean("isSeries") ?: false
                    com.example.ui.screens.BannerEditorScreen(movieId, isSeries, viewModel, navController)
                }
                composable(
                    "create_video/{movieId}/{isSeries}",
                    arguments = listOf(
                        navArgument("movieId") { type = NavType.IntType },
                        navArgument("isSeries") { type = NavType.BoolType }
                    )
                ) { backStackEntry ->
                    val movieId = backStackEntry.arguments?.getInt("movieId") ?: return@composable
                    val isSeries = backStackEntry.arguments?.getBoolean("isSeries") ?: false
                    com.example.ui.screens.CreateVideoScreen(movieId, isSeries, viewModel, navController)
                }
            }
        }
      }
    }
  }
}
