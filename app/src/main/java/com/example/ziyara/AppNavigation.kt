package com.example.ziyara

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun AppNavigation() {
    // Tracks the current active screen route
    var currentRoute by remember { mutableStateOf("map_screen") }

    // Stores the ID of the selected location to pass it to the details screen
    var selectedPlaceId by remember { mutableStateOf("") }

    // Custom backstack list to handle back navigation manually without external libraries
    val backStack = remember { mutableStateListOf<String>() }

    // Navigates to a new screen and saves the current one to the history
    val navigateTo: (String) -> Unit = { route ->
        backStack.add(currentRoute)
        currentRoute = route
    }

    // Restores the previous screen from the backstack if available
    val navigateBack: () -> Unit = {
        if (backStack.isNotEmpty()) {
            currentRoute = backStack.removeAt(backStack.lastIndex)
        }
    }

    // Routing logic based on the current state value
    when (currentRoute) {
        "map_screen" -> {
            MapScreenPlaceholder(
                onNavigateToDetails = { id ->
                    selectedPlaceId = id
                    navigateTo("details_screen")
                },
                onNavigateToFavorites = {
                    navigateTo("favorites_screen")
                }
            )
        }
        "favorites_screen" -> {
            FavoritesScreenPlaceholder(onBack = navigateBack)
        }
        "details_screen" -> {
            DetailsScreenPlaceholder(placeId = selectedPlaceId, onBack = navigateBack)
        }
    }
}

// TODO: Replace these placeholders with the actual screen composables designed by the team

@Composable
fun MapScreenPlaceholder(onNavigateToDetails: (String) -> Unit, onNavigateToFavorites: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Main Map Screen")
        Spacer(modifier = Modifier.height(20.dp))
        Button(onClick = { onNavigateToDetails("alex_library_01") }) {
            Text(text = "Test: Go to Place Details")
        }
        Spacer(modifier = Modifier.height(10.dp))
        Button(onClick = onNavigateToFavorites) {
            Text(text = "Go to Favorites Screen")
        }
    }
}

@Composable
fun FavoritesScreenPlaceholder(onBack: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Favorite Places Screen")
        Spacer(modifier = Modifier.height(20.dp))
        Button(onClick = { onBack() }) {
            Text(text = "Go Back")
        }
    }
}

@Composable
fun DetailsScreenPlaceholder(placeId: String, onBack: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Place Details Screen")
        Text(text = "Passed Place ID: $placeId")
        Spacer(modifier = Modifier.height(20.dp))
        Button(onClick = { onBack() }) {
            Text(text = "Go Back")
        }
    }
}