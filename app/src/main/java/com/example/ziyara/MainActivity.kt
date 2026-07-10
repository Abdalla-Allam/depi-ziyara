package com.example.ziyara

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.navigation.compose.rememberNavController
import com.example.ziyara.data.local.AppDatabase
import com.example.ziyara.data.repository.PlaceRepository
import com.example.ziyara.navigation.AppNavigation
import com.example.ziyara.presentation.home.HomeViewModel
import com.example.ziyara.presentation.home.HomeViewModelFactory
import com.example.ziyara.presentation.maps.MapsScreen
import com.example.ziyara.ui.theme.ZiyaraTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        val database = AppDatabase.getDatabase(applicationContext)
        val repository = PlaceRepository(database.placeDao())
        val factory = HomeViewModelFactory(repository,applicationContext)
        val homeViewModel: HomeViewModel by viewModels { factory }

        setContent {
            ZiyaraTheme {
                val navController = rememberNavController()
                AppNavigation(
                    navController = navController,
                    homeViewModel = homeViewModel
                )
            }
        }
    }
}