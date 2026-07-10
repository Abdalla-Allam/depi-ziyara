package com.example.ziyara.navigation

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.example.ziyara.presentation.PlaceUiState
import com.example.ziyara.presentation.details.PlaceDetailsScreen
import com.example.ziyara.presentation.home.HomeScreen
import com.example.ziyara.presentation.home.HomeViewModel
import com.example.ziyara.presentation.favorites.FavoritesScreen
import com.example.ziyara.presentation.maps.MapsScreen
import com.example.ziyara.presentation.settings.SettingsScreen

sealed class Screen(val route: String) {
    object WelcomeScreen : Screen("welcome")
    object HomeScreen : Screen("home")
    object MapScreen : Screen("map")
    object Favorites : Screen("favorites")
    object Settings : Screen("settings")
    object Details : Screen("details/{placeId}") {
        fun createRoute(placeId: Int) = "details/$placeId"
    }
}

@Composable
fun AppNavigation(
    navController: NavHostController,
    homeViewModel: HomeViewModel,
    currentLanguage: String,
    onLanguageChange: (String) -> Unit,
    onNavigateToSettings: () -> Unit // 🌟 ده البراميتر اللي كان ناقص
) {

    val uiState by homeViewModel.uiState.collectAsState()

    val darkGreen = Color(0xFF0F4C43)

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    when (val state = uiState) {
        is PlaceUiState.Loading -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
        is PlaceUiState.Success -> {
            val allPlaces = state.places
            val favoritePlaces = allPlaces.filter { it.isFavorite }

            Scaffold { innerPadding ->
                Box(modifier = Modifier.fillMaxSize()) {
                    NavHost(
                        navController = navController,
                        startDestination = Screen.HomeScreen.route,
                        modifier = Modifier.fillMaxSize().padding(bottom = innerPadding.calculateBottomPadding())
                    ) {
                        composable(route = Screen.HomeScreen.route) {
                            HomeScreen(
                                viewModel = homeViewModel,
                                currentLanguage = currentLanguage,
                                onLanguageChange = onLanguageChange,
                                onNavigateToSettings = onNavigateToSettings, // 🌟 التمرير الصح
                                onPlaceClick = { placeId ->
                                    navController.navigate(Screen.Details.createRoute(placeId))
                                }
                            )
                        }

                        composable(route = Screen.Favorites.route) {
                            FavoritesScreen(
                                favoritePlaces = favoritePlaces,
                                onPlaceClick = { navController.navigate(Screen.Details.createRoute(it)) },
                                onBackClick = { navController.popBackStack() },
                                onClearAllClick = { homeViewModel.clearAllFavorites() }
                            )
                        }

                        composable(route = Screen.MapScreen.route) {
                            MapsScreen(homeViewModel)
                        }

                        composable(route = Screen.Settings.route) {
                            SettingsScreen(
                                currentLanguage = currentLanguage,
                                onLanguageChange = onLanguageChange,
                                onBackClick = { navController.popBackStack() }
                            )
                        }

                        composable(
                            route = Screen.Details.route,
                            arguments = listOf(navArgument("placeId") { type = NavType.IntType })
                        ) { backStackEntry ->
                            val placeId = backStackEntry.arguments?.getInt("placeId") ?: -1
                            PlaceDetailsScreen(
                                placeId = placeId,
                                viewModel = homeViewModel,
                                onBackClick = { navController.popBackStack() }
                            )
                        }
                    }

                    if (currentRoute != Screen.WelcomeScreen.route &&
                        currentRoute?.startsWith("details") == false &&
                        currentRoute != Screen.Settings.route) {
                        NavigationBar(
                            containerColor = Color.White,
                            contentColor = darkGreen,
                            modifier = Modifier.fillMaxWidth().align(Alignment.BottomCenter).shadow(16.dp, RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
                        ) {
                            val items = listOf(Screen.HomeScreen, Screen.MapScreen, Screen.Favorites)
                            val icons = listOf(Icons.Default.Home, Icons.Default.LocationOn, Icons.Default.Favorite)

                            items.forEachIndexed { index, screen ->
                                NavigationBarItem(
                                    selected = currentRoute == screen.route,
                                    onClick = {
                                        navController.navigate(screen.route) {
                                            popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                                            launchSingleTop = true
                                            restoreState = true
                                        }
                                    },
                                    icon = { Icon(icons[index], contentDescription = null) },
                                    label = { Text(screen.route.replaceFirstChar { it.uppercase() }) },
                                    colors = NavigationBarItemDefaults.colors(
                                        selectedIconColor = darkGreen,
                                        unselectedIconColor = Color.Gray,
                                        selectedTextColor = darkGreen,
                                        unselectedTextColor = Color.Gray,
                                        indicatorColor = Color.Transparent
                                    )
                                )
                            }
                        }
                    }
                }
            }
        }
        is PlaceUiState.Error -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Error: ${state.message}")
            }
        }
    }
}