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
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.size.Precision
import com.example.ziyara.data.local.entity.PlaceEntity
import com.example.ziyara.presentation.PlaceUiState

@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    onPlaceClick: (Int) -> Unit //compose bta3y
) {
    val selectedCategory by viewModel.selectedCategory.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val featuredPlace by viewModel.featuredPlace.collectAsState()
    val uiState by viewModel.uiState.collectAsState()

    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    val categories = remember {
        listOf(
            "All" to "🌍",
            "Temple" to "🏛️",
            "Tomb" to "🪦",
            "Museum" to "🖼️",
            "Pyramid" to "🛕",
            "Oasis" to "🌴",
            "Historical Fortress" to "🏰",
            "Market" to "🧺",
            "National Park" to "🌳",
            "Nature" to "🌴"
        )
    }

    val darkGreen = Color(0xFF0F4C43)
    val lightBeige = Color(0xFFF7F4EB)
    val goldAccent = Color(0xFFD4A373)

    LazyVerticalGrid(
        columns = GridCells.Fixed(1),
        modifier = Modifier
            .fillMaxSize()
            .background(lightBeige),
        contentPadding = PaddingValues(bottom = 90.dp),
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
                    .statusBarsPadding()
                    .padding(start = 20.dp, end = 20.dp, top = 20.dp, bottom = 32.dp)
            ) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Ziyara",
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 22.sp
                            )
                            Text(
                                text = "Discover Egypt",
                                color = Color.White.copy(alpha = 0.7f),
                                fontSize = 13.sp
                            )
                        }

                        Surface(
                            color = Color.White.copy(alpha = 0.15f),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Text(
                                text = "EN | عر",
                                color = Color.White,
                                modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp),
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { viewModel.updateSearchQuery(it) },
                        placeholder = { Text("Search historical sites, museums, parks...", color = Color.Gray, fontSize = 14.sp) },
                        leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = Color.Gray) },
                        trailingIcon = {
                            if (searchQuery.isNotEmpty()) {
                                IconButton(onClick = {
                                    viewModel.updateSearchQuery("")
                                    focusManager.clearFocus()
                                }) {
                                    Icon(Icons.Default.Clear, contentDescription = null, tint = Color.Gray)
                                }
                            }
                        },
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                        keyboardActions = KeyboardActions(onSearch = {
                            keyboardController?.hide()
                            focusManager.clearFocus()
                        }),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(54.dp),
                        shape = RoundedCornerShape(27.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.Black,
                            unfocusedTextColor = Color.Black,
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
            featuredPlace?.let { place ->
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
                            Text(place.name, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 22.sp, lineHeight = 26.sp, maxLines = 2, overflow = TextOverflow.Ellipsis)
                            Spacer(modifier = Modifier.height(6.dp))
                            Text("Ready to step into history? Tap to unlock the story! ✨", color = Color.White.copy(alpha = 0.8f), fontSize = 12.sp, fontWeight = FontWeight.Normal)
                        }
                        Button(
                            onClick = { onPlaceClick(place.id) },
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
                }
                Spacer(modifier = Modifier.height(12.dp))

                CategorySelector(
                    categories = categories,
                    selectedCategory = selectedCategory,
                    darkGreen = darkGreen,
                    goldAccent = goldAccent,
                    onCategorySelected = { viewModel.selectCategory(it) },
                    modifier = Modifier.padding(horizontal = 20.dp)
                )
            }
        }

        when (uiState) {
            is PlaceUiState.Loading -> {
                item(span = { GridItemSpan(maxLineSpan) }) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 40.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = darkGreen)
                    }
                }
            }
            is PlaceUiState.Success -> {
                val placesList = (uiState as PlaceUiState.Success).places

                item(span = { GridItemSpan(maxLineSpan) }) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 20.dp, end = 20.dp, top = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Popular Landmarks", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = darkGreen)
                        Text("${placesList.size} found", color = Color.Gray, fontSize = 14.sp)
                    }
                }

                items(placesList, key = { place -> place.id }) { place ->
                    PlaceCard(
                        place = place,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp),
                        onClick = { onPlaceClick(place.id) },
                        onFavoriteClick = { viewModel.toggleFavorite(place) }
                    )
                }
            }
            is PlaceUiState.Error -> {
                item(span = { GridItemSpan(maxLineSpan) }) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 40.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("Something went wrong", color = Color.Red, fontSize = 14.sp)
                    }
                }
            }
        }
    }
}

@Composable
fun CategorySelector(
    categories: List<Pair<String, String>>,
    selectedCategory: String,
    darkGreen: Color,
    goldAccent: Color,
    onCategorySelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        modifier = modifier
    ) {
        items(categories, key = { it.first }) { (category, emoji) ->
            val isSelected = category == selectedCategory
            val backgroundColor by animateColorAsState(if (isSelected) goldAccent else Color.White, label = "categoryBg")
            val contentColor by animateColorAsState(if (isSelected) Color.White else darkGreen, label = "categoryContent")

            Surface(
                color = backgroundColor,
                shape = RoundedCornerShape(20.dp),
                shadowElevation = 1.dp,
                modifier = Modifier
                    .height(40.dp)
                    .graphicsLayer {
                        shape = RoundedCornerShape(20.dp)
                        clip = true
                    }
                    .clickable { onCategorySelected(category) }
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

@Composable
fun PlaceCard(
    place: PlaceEntity,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    onFavoriteClick: () -> Unit
) {
    val context = LocalContext.current
    val density = LocalDensity.current

    val ticketText = remember(place.ticketPrice) {
        if (place.ticketPrice == 0.0) "Free" else "${place.ticketPrice.toInt()} EGP"
    }


    val displayRating = remember(place.id) {
        val calculated = 4.0 + ((place.id * 3) % 10) / 10.0
        if (calculated > 4.9) "4.8" else String.format("%.1f", calculated)
    }

    val widthPx = remember { with(density) { 320.dp.roundToPx() } }
    val heightPx = remember { with(density) { 130.dp.roundToPx() } }

    val imageRequest = remember(place.imageUrl) {
        ImageRequest.Builder(context)
            .data(place.imageUrl)
            .crossfade(true)
            .size(widthPx, heightPx)
            .precision(Precision.EXACT)
            .build()
    }

    Card(
        modifier = modifier
            .height(230.dp)
            .graphicsLayer {
                shape = RoundedCornerShape(20.dp)
                clip = true
            }
            .clickable { onClick() },
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
                    model = imageRequest,
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
                        text = ticketText,
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
                    Text(text = "⭐ $displayRating", color = Color(0xFFD4A373), fontWeight = FontWeight.Bold, fontSize = 11.sp)
                }
            }
        }
    }
}