package com.example.ziyara.presentation.favorites

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Sort
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.ziyara.data.local.entity.PlaceEntity
import java.util.Locale

enum class SortType { DEFAULT, PRICE_LOW_HIGH, PRICE_HIGH_LOW, RATING_LOW_HIGH, RATING_HIGH_LOW }

private fun calculateRating(id: Int): Double {
    val calculated = 4.0 + ((id * 3) % 10) / 10.0
    return if (calculated > 4.9) 4.8 else calculated
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoritesScreen(
    favoritePlaces: List<PlaceEntity>,
    onPlaceClick: (Int) -> Unit,
    onBackClick: () -> Unit,
    onClearAllClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    var sortType by remember { mutableStateOf(SortType.DEFAULT) }
    var showSortMenu by remember { mutableStateOf(false) }
    var showClearDialog by remember { mutableStateOf(false) }

    val sortedPlaces = remember(favoritePlaces, sortType) {
        when (sortType) {
            SortType.DEFAULT -> favoritePlaces
            SortType.PRICE_LOW_HIGH -> favoritePlaces.sortedBy { it.ticketPrice }
            SortType.PRICE_HIGH_LOW -> favoritePlaces.sortedByDescending { it.ticketPrice }
            SortType.RATING_LOW_HIGH -> favoritePlaces.sortedBy { calculateRating(it.id) }
            SortType.RATING_HIGH_LOW -> favoritePlaces.sortedByDescending { calculateRating(it.id) }
        }
    }

    if (showClearDialog) {
        AlertDialog(
            onDismissRequest = { showClearDialog = false },
            title = { Text("Clear All Favorites") },
            text = { Text("Are you sure you want to remove all places from your favorites?") },
            confirmButton = {
                TextButton(onClick = { onClearAllClick(); showClearDialog = false }) {
                    Text("Clear All", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(onClick = { showClearDialog = false }) { Text("Cancel") }
            }
        )
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title = { Text("Favorite Places", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    if (favoritePlaces.isNotEmpty()) {
                        Box {
                            IconButton(onClick = { showSortMenu = true }) {
                                Icon(imageVector = Icons.Default.Sort, contentDescription = "Sort")
                            }
                            DropdownMenu(expanded = showSortMenu, onDismissRequest = { showSortMenu = false }) {
                                val options = listOf("Default" to SortType.DEFAULT, "Price: Low to High" to SortType.PRICE_LOW_HIGH, "Price: High to Low" to SortType.PRICE_HIGH_LOW, "Rating: Low to High" to SortType.RATING_LOW_HIGH, "Rating: High to Low" to SortType.RATING_HIGH_LOW)
                                options.forEach { (text, type) ->
                                    DropdownMenuItem(text = { Text(text) }, onClick = { sortType = type; showSortMenu = false })
                                }
                            }
                        }
                        IconButton(onClick = { showClearDialog = true }) {
                            Icon(imageVector = Icons.Default.Delete, contentDescription = "Clear All", tint = MaterialTheme.colorScheme.error)
                        }
                    }
                }
            )
        }
    ) { paddingValues ->
        if (sortedPlaces.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize().padding(paddingValues), contentAlignment = Alignment.Center) {
                Text("No favorite places yet.", color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(top = paddingValues.calculateTopPadding()),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                items(sortedPlaces, key = { it.id }) { place ->
                    FavoritePlaceItem(place = place, onPlaceClick = onPlaceClick)
                }
            }
        }
    }
}

@Composable
fun FavoritePlaceItem(place: PlaceEntity, onPlaceClick: (Int) -> Unit, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.fillMaxWidth().clickable { onPlaceClick(place.id) },
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface) // 🌟 هنا التغيير الأساسي
    ) {
        Row(modifier = Modifier.fillMaxWidth().padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current).data(place.imageUrl).crossfade(true).build(),
                contentDescription = null,
                modifier = Modifier.size(90.dp).clip(RoundedCornerShape(16.dp)),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(place.name, color = MaterialTheme.colorScheme.onSurface, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                Text("📍 ${place.governorate}", color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f), fontSize = 12.sp)
                Spacer(modifier = Modifier.height(10.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Surface(color = MaterialTheme.colorScheme.primary, shape = RoundedCornerShape(8.dp)) {
                        Text(
                            text = if (place.ticketPrice == 0.0) "Free" else "${place.ticketPrice.toInt()} EGP",
                            color = MaterialTheme.colorScheme.onPrimary,
                            fontSize = 11.sp,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                    Text("⭐ ${calculateRating(place.id)}", color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                }
            }
        }
    }
}