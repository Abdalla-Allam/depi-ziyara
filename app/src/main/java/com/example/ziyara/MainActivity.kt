package com.example.ziyara

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room
import com.example.ziyara.data.local.AppDatabase
import com.example.ziyara.data.repository.PlaceRepository
import com.example.ziyara.presentation.home.HomeScreen
import com.example.ziyara.presentation.home.HomeViewModel
import com.example.ziyara.presentation.home.HomeViewModelFactory
import com.example.ziyara.ui.theme.ZiyaraTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // build database direct to avoid getinstance error
        val database = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "ziyara_database"
        ).fallbackToDestructiveMigration().build()

        val repository = PlaceRepository(database.placeDao())
        val factory = HomeViewModelFactory(repository)
        val homeViewModel = ViewModelProvider(this, factory)[HomeViewModel::class.java]

        setContent {
            ZiyaraTheme {
                // navigation state
                var selectedTab by remember { mutableStateOf("home") }

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = {
                        // bottom navigation setup
                        NavigationBar {
                            NavigationBarItem(
                                selected = selectedTab == "home",
                                onClick = { selectedTab = "home" },
                                label = { Text("Home") },
                                icon = { Icon(Icons.Default.Home, contentDescription = "Home") }
                            )
                            NavigationBarItem(
                                selected = selectedTab == "favorites",
                                onClick = { selectedTab = "favorites" },
                                label = { Text("Favorites") },
                                icon = { Icon(Icons.Default.Favorite, contentDescription = "Favorites") }
                            )
                        }
                    }
                ) { innerPadding ->
                    Surface(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding),
                        color = MaterialTheme.colorScheme.background
                    ) {
                        // handling screens switching
                        when (selectedTab) {
                            "home" -> {
                                HomeScreen(
                                    viewModel = homeViewModel,
                                    onPlaceClick = { placeId -> selectedTab = "favorites" }
                                )
                            }
                            "favorites" -> {
                            }
                        }
                    }
                }
            }
        }
    }
}