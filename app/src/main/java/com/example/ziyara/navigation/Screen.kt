package com.example.ziyara.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.ziyara.presentation.home.HomeScreen
import com.example.ziyara.presentation.home.HomeViewModel

sealed class Screen(val route: String) {
    object HomeScreen : Screen("home")
    object Favorites : Screen("favorites")
    object Details : Screen("details/{placeId}") {
        fun createRoute(placeId: Int) = "details/$placeId"
    }
}

@Composable
fun AppNavigation(
    navController: NavHostController,
    homeViewModel: HomeViewModel
) {
    NavHost(
        navController = navController,
        startDestination = Screen.HomeScreen.route
    ) {
        composable(route = Screen.HomeScreen.route) {
            HomeScreen(
                viewModel = homeViewModel,
                onPlaceClick = { placeId ->
                    navController.navigate(Screen.Details.createRoute(placeId))
                }
            )
        }
    }
}