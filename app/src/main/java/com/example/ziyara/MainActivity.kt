package com.example.ziyara

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModelProvider
import com.example.ziyara.data.local.AppDatabase
import com.example.ziyara.data.repository.PlaceRepository
import com.example.ziyara.presentation.home.HomeScreen
import com.example.ziyara.presentation.home.HomeViewModel
import com.example.ziyara.presentation.home.HomeViewModelFactory
import com.example.ziyara.ui.theme.ZiyaraTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val database = AppDatabase.getDatabase(applicationContext)
        val repository = PlaceRepository(database.placeDao())
        val factory = HomeViewModelFactory(repository)
        val homeViewModel = ViewModelProvider(this, factory)[HomeViewModel::class.java]

        setContent {
            ZiyaraTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    HomeScreen(
                        viewModel = homeViewModel,
                        onPlaceClick = { placeId -> }
                    )
                }
            }
        }
    }
}