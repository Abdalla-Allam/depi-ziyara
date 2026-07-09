package com.example.ziyara.presentation.home

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.ziyara.data.local.entity.PlaceEntity

@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    onPlaceClick: (Int) -> Unit //compose bta3y
) {
    val selectedCategory by viewModel.selectedCategory.collectAsState()
    val filteredPlaces by viewModel.filteredPlaces.collectAsState()
    var searchQuery by remember { mutableStateOf("") }

    val categories = remember {
        listOf(
            "All" to "🌍",
            "Pharaonic" to "🏛️",
            "Islamic" to "🕌",
            "Museums" to "🖼️",
            "Nature" to "🌴"
        )
    }

    val darkGreen = Color(0xFF0F4C43)
    val lightBeige = Color(0xFFF7F4EB)
    val goldAccent = Color(0xFFD4A373)

    val displayedPlaces = remember(searchQuery, filteredPlaces) {
        if (searchQuery.isBlank()) {
            filteredPlaces
        } else {
            filteredPlaces.filter {
                it.name.contains(searchQuery, ignoreCase = true) ||
                        it.governorate.contains(searchQuery, ignoreCase = true)
            }
        }
    }

    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = 160.dp),
        modifier = Modifier
            .fillMaxSize()
            .background(lightBeige),
        contentPadding = PaddingValues(bottom = 24.dp),
        horizontalArrangement = Arrangement.spacedBy(14.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {

        item(span = { GridItemSpan(maxLineSpan) }) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        darkGreen,
                        shape = RoundedCornerShape(bottomStart = 32.dp, bottomEnd = 32.dp)
                    )
                    .padding(start = 20.dp, end = 20.dp, top = 24.dp, bottom = 32.dp)
            ) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(48.dp)
                                    .clip(RoundedCornerShape(24.dp))
                                    .background(goldAccent),
                                contentAlignment = Alignment.Center
                            ) {
                                Text("Z", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 20.sp)
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text("Ziyara", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 20.sp)
                                Text("Discover Egypt", color = Color.White.copy(alpha = 0.7f), fontSize = 12.sp)
                            }
                        }

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Surface(
                                color = Color.White.copy(alpha = 0.15f),
                                shape = RoundedCornerShape(16.dp)
                            ) {
                                Text("EN | عر", color = Color.White, modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp), fontSize = 12.sp)
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Icon(Icons.Default.Notifications, contentDescription = null, tint = Color.White)
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        placeholder = { Text("Search historical sites, museums, parks...", color = Color.Gray, fontSize = 14.sp) },
                        leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = Color.Gray) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(54.dp),
                        shape = RoundedCornerShape(27.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White,
                            focusedBorderColor = Color.Transparent,
                            unfocusedBorderColor = Color.Transparent
                        )
                    )
                }
            }
        }

        item(span = { GridItemSpan(maxLineSpan) }) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = darkGreen)
            ) {
                Box(modifier = Modifier.fillMaxWidth().heightIn(min = 140.dp)) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp)
                            .padding(end = 100.dp),
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text("✦ FEATURED DESTINATION", color = goldAccent, fontWeight = FontWeight.SemiBold, fontSize = 11.sp)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text("Discover\nAncient Egypt", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 22.sp, lineHeight = 26.sp)
                    }
                    Button(
                        onClick = {},
                        colors = ButtonDefaults.buttonColors(containerColor = goldAccent),
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .padding(16.dp),
                        shape = RoundedCornerShape(12.dp),
                        contentPadding = PaddingValues(horizontal = 14.dp, vertical = 6.dp)
                    ) {
                        Text("Explore ›", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    }
                }
            }
        }

        item(span = { GridItemSpan(maxLineSpan) }) {
            Column(modifier = Modifier.padding(top = 8.dp)) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Explore by Type", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = darkGreen)
                    Text("See all", color = Color.Gray, fontSize = 14.sp)
                }
                Spacer(modifier = Modifier.height(12.dp))

                LazyRow(
                    contentPadding = PaddingValues(horizontal = 20.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(categories) { (category, emoji) ->
                        val isSelected = category == selectedCategory
                        val backgroundColor by animateColorAsState(if (isSelected) goldAccent else Color.White, label = "")
                        val contentColor by animateColorAsState(if (isSelected) Color.White else darkGreen, label = "")

                        Surface(
                            color = backgroundColor,
                            shape = RoundedCornerShape(20.dp),
                            modifier = Modifier
                                .clickable { viewModel.selectCategory(category) }
                                .height(40.dp),
                            shadowElevation = 1.dp
                        ) {
                            Row(
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(horizontal = 16.dp)
                            ) {
                                Text(text = "$emoji ", fontSize = 14.sp)
                                Text(
                                    text = category,
                                    color = contentColor,
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 13.sp
                                )
                            }
                        }
                    }
                }
            }
        }

        item(span = { GridItemSpan(maxLineSpan) }) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 20.dp, end = 20.dp, top = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Popular Landmarks", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = darkGreen)
                Text("${displayedPlaces.size} found", color = Color.Gray, fontSize = 14.sp)
            }
        }

        items(displayedPlaces, key = { it.id }) { place ->
            val index = displayedPlaces.indexOf(place)
            PlaceCard(
                place = place,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = if (index % 2 == 0) 20.dp else 0.dp,
                        end = if (index % 2 != 0) 20.dp else 0.dp
                    ),
                onClick = { onPlaceClick(place.id) },
                onFavoriteClick = { viewModel.toggleFavorite(place) }
            )
        }
    }
}

@Composable
fun PlaceCard(
    place: PlaceEntity,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    onFavoriteClick: () -> Unit
) {
    Card(
        modifier = modifier
            .clickable { onClick() }
            .height(230.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(130.dp)
            ) {
                AsyncImage(
                    model = place.imageUrl,
                    contentDescription = place.name,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )

                IconButton(
                    onClick = onFavoriteClick,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(8.dp)
                        .background(Color.White.copy(alpha = 0.85f), shape = RoundedCornerShape(12.dp))
                        .size(32.dp)
                ) {
                    Icon(
                        imageVector = if (place.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = null,
                        tint = if (place.isFavorite) Color.Red else Color.Gray,
                        modifier = Modifier.size(18.dp)
                    )
                }

                Surface(
                    color = Color(0xFF0F4C43),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(8.dp)
                ) {
                    Text(
                        text = place.ticketPrice,
                        color = Color.White,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    text = place.name,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = Color(0xFF0F4C43)
                )
                Spacer(modifier = Modifier.height(6.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "📍 ${place.governorate}",
                        color = Color.Gray,
                        fontSize = 11.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f)
                    )
                    Text(text = "⭐ 4.8", color = Color(0xFFD4A373), fontWeight = FontWeight.Bold, fontSize = 11.sp)
                }
            }
        }
    }
}