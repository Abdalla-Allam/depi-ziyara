package com.example.ziyara

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.BlendMode.Companion.Screen
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.ziyara.presentation.home.HomeScreen

sealed class Screen(val route: String) {
    object HomeScreen : Screen("home")
    object Favorites : Screen("favorites")
    object Details : Screen("details/{placeId}") {
        fun createRoute(placeId: Int) = "details/$placeId"
    }
}

class Navigation {
    @Composable
    fun Nav(){
        val navController = rememberNavController()
        NavHost(navController = navController, startDestination = Screen.HomeScreen.route){
           composable(route="Home Screen"){
               HomeScreen()
           }
        }
    }

}