package com.example.music_api

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.core.view.WindowCompat
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.music_api.ui.screens.ArtistInfoScreen
import com.example.music_api.ui.screens.ArtistTracksScreen
import com.example.music_api.ui.screens.StartScreen
import com.example.music_api.ui.theme.MusicSearchTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, true)
        setContent {
            MusicSearchTheme(darkTheme = false, dynamicColor = false) {
                Surface(
                    modifier = Modifier.Companion.fillMaxSize(),
                    color = Color.Companion.White
                ) {
                    val navController = rememberNavController()
                    NavHost(
                        navController = navController,
                        startDestination = "start"
                    ) {
                        composable("start") { StartScreen(navController) }
                        composable("artist_info") { backStackEntry ->
                            ArtistInfoScreen(onBack = { navController.popBackStack() })
                        }
                        composable("artist_tracks") { backStackEntry ->
                            ArtistTracksScreen(onBack = { navController.popBackStack() })
                        }
                    }
                }
            }
        }
    }
}