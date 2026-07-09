package com.example.ziyara.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.ziyara.presentation.home.HomeScreen
import com.example.ziyara.presentation.home.HomeViewModel
import com.example.ziyara.presentation.favorites.FavoritesScreen

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
    val allPlaces by homeViewModel.places.collectAsState(initial = emptyList())
    val favoritePlaces = allPlaces.filter { it.isFavorite }

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

        composable(route = Screen.Favorites.route) {
            FavoritesScreen(
                favoritePlaces = favoritePlaces,
                onPlaceClick = { placeId ->
                    navController.navigate(Screen.Details.createRoute(placeId))
                },
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }

        composable(
            route = Screen.Details.route,
            arguments = listOf(navArgument("placeId") { type = NavType.IntType })
        ) { backStackEntry ->
            val placeId = backStackEntry.arguments?.getInt("placeId") ?: -1
        }
    }
}