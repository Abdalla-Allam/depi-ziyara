package com.example.ziyara.navigation

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.example.ziyara.data.OnboardingPreferences
import com.example.ziyara.presentation.PlaceUiState
import com.example.ziyara.presentation.home.HomeScreen
import com.example.ziyara.presentation.home.HomeViewModel
import com.example.ziyara.presentation.favorites.FavoritesScreen
import com.example.ziyara.presentation.Onboarding.OnboardingScreen
import kotlinx.coroutines.launch

// Define our app screens
sealed class Screen(val route: String) {
    object Onboarding : Screen("onboarding")
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
    navController: NavHostController, homeViewModel: HomeViewModel
) {
    val uiState by homeViewModel.uiState.collectAsState()
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val onboardingCompleted by OnboardingPreferences.isOnboardingCompleted(context)
        .collectAsState(initial = null)

    val darkGreen = Color(0xFF0F4C43)
    val goldAccent = Color(0xFFD4A373)

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val hideBottomBarRoutes =
        listOf(Screen.Onboarding.route, Screen.WelcomeScreen.route)

    // لسه بننتظر نعرف حالة الـ onboarding أو الداتا لسه بتتحمل
    if (onboardingCompleted == null || uiState is PlaceUiState.Loading) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }

    when (val state = uiState) {

        is PlaceUiState.Success -> {
            val allPlaces = state.places
            val favoritePlaces = allPlaces.filter { it.isFavorite }

            val startDestination = if (onboardingCompleted == true) {
                Screen.HomeScreen.route
            } else {
                Screen.Onboarding.route
            }

            Scaffold { innerPadding ->
                Box(modifier = Modifier.fillMaxSize()) {
                    NavHost(
                        navController = navController,
                        startDestination = startDestination,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(bottom = innerPadding.calculateBottomPadding())
                    ) {
                        composable(route = Screen.Onboarding.route) {
                            OnboardingScreen(
                                onFinished = {
                                    scope.launch {
                                        OnboardingPreferences.setOnboardingCompleted(context)
                                        navController.navigate(Screen.HomeScreen.route) {
                                            popUpTo(Screen.Onboarding.route) { inclusive = true }
                                        }
                                    }
                                })
                        }

                        composable(route = Screen.HomeScreen.route) {
                            HomeScreen(viewModel = homeViewModel) { placeId ->
                                navController.navigate(Screen.Details.createRoute(placeId))
                            }
                        }
                        composable(route = Screen.Favorites.route) {
                            FavoritesScreen(
                                favoritePlaces,
                                { navController.navigate(Screen.Details.createRoute(it)) },
                                { navController.popBackStack() })
                        }
                        composable(route = Screen.MapScreen.route) {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) { Text("Map Screen") }
                        }
                        composable(
                            route = Screen.Details.route,
                            arguments = listOf(navArgument("placeId") { type = NavType.IntType })
                        ) { backStackEntry ->
                            val placeId = backStackEntry.arguments?.getInt("placeId") ?: -1
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) { Text("Details: $placeId") }
                        }
                    }

                    // Bottom Navigation bar logic
                    if (currentRoute !in hideBottomBarRoutes && currentRoute?.startsWith("details") == false) {
                        NavigationBar(
                            containerColor = Color.White,
                            contentColor = darkGreen,
                            modifier = Modifier
                                .fillMaxWidth()
                                .align(Alignment.BottomCenter)
                                .shadow(16.dp, RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
                        ) {
                            val items =
                                listOf(Screen.HomeScreen, Screen.MapScreen, Screen.Favorites)
                            val icons = listOf(
                                Icons.Default.Home, Icons.Default.LocationOn, Icons.Default.Favorite
                            )
                            items.forEachIndexed { index, screen ->
                                NavigationBarItem(
                                    selected = currentRoute == screen.route,
                                    onClick = {
                                        navController.navigate(screen.route) {
                                            popUpTo(
                                                navController.graph.findStartDestination().id
                                            ) { saveState = true }; launchSingleTop =
                                            true; restoreState = true
                                        }
                                    },
                                    icon = { Icon(icons[index], contentDescription = null) },
                                    label = { Text(screen.route.replaceFirstChar { it.uppercase() }) },
                                    colors = NavigationBarItemDefaults.colors(
                                        selectedIconColor = darkGreen,
                                        unselectedIconColor = Color.Gray
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

        else -> Unit
    }
}