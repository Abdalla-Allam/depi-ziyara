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

enum class SortType {
    DEFAULT, PRICE_LOW_HIGH, PRICE_HIGH_LOW, RATING_LOW_HIGH, RATING_HIGH_LOW
}

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
    val lightBeige = Color(0xFFF7F4EB)
    val darkGreen = Color(0xFF0F4C43)
    val goldAccent = Color(0xFFD4A373)

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
            containerColor = Color.White,
            title = { Text("Clear All Favorites", color = darkGreen, fontWeight = FontWeight.Bold) },
            text = { Text("Are you sure you want to remove all places from your favorites?", color = Color.Gray) },
            confirmButton = {
                TextButton(onClick = {
                    onClearAllClick()
                    showClearDialog = false
                }) {
                    Text("Clear All", color = Color.Red, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showClearDialog = false }) {
                    Text("Cancel", color = darkGreen)
                }
            }
        )
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = lightBeige,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Favorite Places",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                },
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
                            DropdownMenu(
                                expanded = showSortMenu,
                                onDismissRequest = { showSortMenu = false },
                                modifier = Modifier.background(Color.White)
                            ) {
                                DropdownMenuItem(
                                    text = { Text("Default", color = darkGreen) },
                                    onClick = { sortType = SortType.DEFAULT; showSortMenu = false }
                                )
                                DropdownMenuItem(
                                    text = { Text("Price: Low to High", color = darkGreen) },
                                    onClick = { sortType = SortType.PRICE_LOW_HIGH; showSortMenu = false }
                                )
                                DropdownMenuItem(
                                    text = { Text("Price: High to Low", color = darkGreen) },
                                    onClick = { sortType = SortType.PRICE_HIGH_LOW; showSortMenu = false }
                                )
                                DropdownMenuItem(
                                    text = { Text("Rating: Low to High", color = darkGreen) },
                                    onClick = { sortType = SortType.RATING_LOW_HIGH; showSortMenu = false }
                                )
                                DropdownMenuItem(
                                    text = { Text("Rating: High to Low", color = darkGreen) },
                                    onClick = { sortType = SortType.RATING_HIGH_LOW; showSortMenu = false }
                                )
                            }
                        }
                        IconButton(onClick = { showClearDialog = true }) {

                            Icon(imageVector = Icons.Default.Delete, contentDescription = "Clear All", tint = darkGreen)
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = lightBeige,
                    titleContentColor = darkGreen,
                    navigationIconContentColor = darkGreen,
                    actionIconContentColor = darkGreen
                )
            )
        }
    ) { paddingValues ->
        if (sortedPlaces.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "No favorite places yet.",
                        color = darkGreen,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Explore the home screen and add some!",
                        color = Color.Gray,
                        fontSize = 14.sp
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = paddingValues.calculateTopPadding()),
                contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 100.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {

                items(sortedPlaces, key = { it.id }, contentType = { "FavoritePlaceItem" }) { place ->
                    FavoritePlaceItem(
                        place = place,
                        onPlaceClick = onPlaceClick,
                        darkGreen = darkGreen,
                        goldAccent = goldAccent
                    )
                }
            }
        }
    }
}

@Composable
fun FavoritePlaceItem(
    place: PlaceEntity,
    onPlaceClick: (Int) -> Unit,
    darkGreen: Color,
    goldAccent: Color,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    val imageRequest = remember(place.imageUrl) {
        ImageRequest.Builder(context)
            .data(place.imageUrl)
            .crossfade(true)
            .build()
    }

    val displayRating = remember(place.id) {
        String.format(Locale.US, "%.1f", calculateRating(place.id))
    }

    val ticketText = remember(place.ticketPrice) {
        if (place.ticketPrice == 0.0) "Free" else "${place.ticketPrice.toInt()} EGP"
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onPlaceClick(place.id) },
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = imageRequest,
                contentDescription = place.name,
                modifier = Modifier
                    .size(90.dp)
                    .clip(RoundedCornerShape(16.dp)),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = place.name,
                    color = darkGreen,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "📍 ${place.governorate}",
                    color = Color.Gray,
                    fontSize = 12.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        color = darkGreen,
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = ticketText,
                            color = Color.White,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }

                    Text(
                        text = "⭐ $displayRating",
                        color = goldAccent,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp
                    )
                }
            }
        }
    }
}