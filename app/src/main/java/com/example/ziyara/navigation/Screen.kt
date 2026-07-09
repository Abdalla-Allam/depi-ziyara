package com.example.ziyara.navigation


import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.ziyara.presentation.details.PlaceDetailsScreen
import com.example.ziyara.presentation.favorites.FavoritesScreen
import com.example.ziyara.presentation.home.HomeScreen
import com.example.ziyara.presentation.home.HomeViewModel
import com.example.ziyara.presentation.maps.MapsScreen

sealed class Screen(val route: String) {

    object Home : Screen("home")

    object Favorites : Screen("favorites")

    object Details : Screen("details/{placeId}") {
        fun createRoute(placeId: Int) = "details/$placeId"
    }

    object Maps : Screen("maps/{placeId}") {
        fun createRoute(placeId: Int) = "maps/$placeId"
    }
}

@Composable
fun AppNavigation(homeViewModel: HomeViewModel) {
    val navController = rememberNavController()

    NavHost(
        navController = navController, startDestination = Screen.Home.route
    ) {
        // Home
        composable(route = Screen.Home.route) {
            HomeScreen(
                viewModel = homeViewModel, onPlaceClick = { placeId ->
                    navController.navigate(Screen.Details.createRoute(placeId))
                })
        }

        // Details
        composable(
            route = Screen.Details.route,
            arguments = listOf(navArgument("placeId") { type = NavType.IntType })
        ) { backStackEntry ->
            val placeId = backStackEntry.arguments?.getInt("placeId") ?: -1
            PlaceDetailsScreen(
                placeId = placeId, onBackClick = { navController.popBackStack() })
        }

        // Favorites
        composable(route = Screen.Favorites.route) {
            FavoritesScreen(
                onBackClick = { navController.popBackStack() })
        }

        // Maps
        composable(
            route = Screen.Maps.route,
            arguments = listOf(navArgument("placeId") { type = NavType.IntType })
        ) { backStackEntry ->
            val placeId = backStackEntry.arguments?.getInt("placeId") ?: -1
            MapsScreen()
        }
    }
}