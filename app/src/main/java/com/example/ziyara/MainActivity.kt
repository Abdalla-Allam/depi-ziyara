package com.example.ziyara

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.rememberNavController
import com.example.ziyara.data.local.AppDatabase
import com.example.ziyara.data.repository.PlaceRepository
import com.example.ziyara.navigation.AppNavigation
import com.example.ziyara.navigation.Screen
import com.example.ziyara.presentation.home.HomeViewModel
import com.example.ziyara.presentation.home.HomeViewModelFactory
import com.example.ziyara.ui.theme.ZiyaraTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Show the splash screen until data is ready
        val splashScreen = installSplashScreen()
        enableEdgeToEdge()

        var isDataReady = false
        splashScreen.setKeepOnScreenCondition { !isDataReady }

        // Set up database and viewmodel
        val database = AppDatabase.getDatabase(applicationContext)
        val repository = PlaceRepository(database.placeDao())
        val factory = HomeViewModelFactory(repository, applicationContext)
        val homeViewModel: HomeViewModel by viewModels { factory }

        setContent {
            // Apply the app's custom theme
            ZiyaraTheme {
                val navController = rememberNavController()
                val uiState by homeViewModel.uiState.collectAsState()

                // Check if data is loaded to hide the splash screen
                isDataReady = uiState !is com.example.ziyara.presentation.PlaceUiState.Loading

                // Handle language switching
                var appLanguage by rememberSaveable { mutableStateOf("EN") }
                val layoutDirection = if (appLanguage == "AR") LayoutDirection.Rtl else LayoutDirection.Ltr

                // Wrap app in direction provider to swap UI for Arabic
                CompositionLocalProvider(LocalLayoutDirection provides layoutDirection) {
                    AppNavigation(
                        navController = navController,
                        homeViewModel = homeViewModel,
                        currentLanguage = appLanguage,
                        onLanguageChange = { appLanguage = it },
                        onNavigateToSettings = { navController.navigate(Screen.Settings.route) }
                    )
                }
            }
        }
    }
}