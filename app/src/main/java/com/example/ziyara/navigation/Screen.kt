package com.example.ziyara.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.navArgument
import com.example.ziyara.presentation.home.HomeScreen
import com.example.ziyara.presentation.home.HomeViewModel
import com.example.ziyara.presentation.favorites.FavoritesScreen

sealed class Screen(val route: String) {
    object WelcomeScreen : Screen("welcome")
    object HomeScreen : Screen("home")
    object MapScreen : Screen("map")
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

    val darkGreen = Color(0xFF0F4C43)
    val goldAccent = Color(0xFFD4A373)

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val bottomNavItems = listOf(
        Triple(Screen.HomeScreen, Icons.Default.Home, "Home"),
        Triple(Screen.MapScreen, Icons.Default.LocationOn, "Map"),
        Triple(Screen.Favorites, Icons.Default.Favorite, "Favorites")
    )

    Scaffold { innerPadding ->
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            NavHost(
                navController = navController,
                startDestination = Screen.HomeScreen.route,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = innerPadding.calculateBottomPadding())
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

                composable(route = Screen.WelcomeScreen.route) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("Welcome Screen Placeholder")
                    }
                }

                composable(route = Screen.MapScreen.route) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("Map Screen Placeholder")
                    }
                }

                composable(
                    route = Screen.Details.route,
                    arguments = listOf(navArgument("placeId") { type = NavType.IntType })
                ) { backStackEntry ->
                    val placeId = backStackEntry.arguments?.getInt("placeId") ?: -1
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("Details Screen for Place ID: $placeId")
                    }
                }
            }

            if (currentRoute != Screen.WelcomeScreen.route && currentRoute?.startsWith("details") == false) {
                NavigationBar(
                    containerColor = Color.Transparent,
                    contentColor = darkGreen,
                    windowInsets = WindowInsets(0, 0, 0, 0),
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.BottomCenter)
                        .shadow(
                            elevation = 16.dp,
                            shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
                            clip = true
                        )
                        .background(
                            color = Color.White,
                            shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
                        )
                ) {
                    bottomNavItems.forEach { (screen, icon, label) ->
                        val isSelected = currentRoute == screen.route
                        NavigationBarItem(
                            selected = isSelected,
                            onClick = {
                                if (currentRoute != screen.route) {
                                    navController.navigate(screen.route) {
                                        popUpTo(navController.graph.findStartDestination().id) {
                                            saveState = true
                                        }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                }
                            },
                            icon = {
                                Icon(
                                    imageVector = icon,
                                    contentDescription = label
                                )
                            },
                            label = { Text(text = label) },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = darkGreen,
                                selectedTextColor = darkGreen,
                                indicatorColor = goldAccent.copy(alpha = 0.25f),
                                unselectedIconColor = Color.Gray,
                                unselectedTextColor = Color.Gray
                            )
                        )
                    }
                }
            }
        }
    }
}