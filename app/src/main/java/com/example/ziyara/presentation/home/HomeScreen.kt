package com.example.ziyara.presentation.home

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.ziyara.data.local.entity.PlaceEntity
import com.example.ziyara.presentation.PlaceUiState
import com.example.ziyara.utils.AppStrings

@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    currentLanguage: String,
    onLanguageChange: (String) -> Unit,
    onNavigateToSettings: () -> Unit,
    onPlaceClick: (Int) -> Unit
) {
    val selectedCategory by viewModel.selectedCategory.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val featuredPlace by viewModel.featuredPlace.collectAsState()
    val uiState by viewModel.uiState.collectAsState()

    val focusManager = LocalFocusManager.current
    val categories = remember {
        listOf("All" to "🌍", "Temple" to "🏛️", "Tomb" to "🪦", "Museum" to "🖼️", "Pyramid" to "🛕", "Oasis" to "🌴", "Historical Fortress" to "🏰", "Market" to "🧺", "National Park" to "🌳", "Nature" to "🌴")
    }

    val brandGreen = Color(0xFF0F4C43)
    val goldAccent = Color(0xFFD4A373)

    LazyVerticalGrid(
        columns = androidx.compose.foundation.lazy.grid.GridCells.Fixed(1),
        modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background),
        contentPadding = PaddingValues(bottom = 90.dp),
        horizontalArrangement = Arrangement.spacedBy(14.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item(span = { GridItemSpan(maxLineSpan) }) {
            Box(modifier = Modifier.fillMaxWidth().background(brandGreen, shape = RoundedCornerShape(bottomStart = 32.dp, bottomEnd = 32.dp)).statusBarsPadding().padding(start = 20.dp, end = 20.dp, top = 20.dp, bottom = 32.dp)) {
                Column {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                        Column {
                            Text(AppStrings.getZiyara(currentLanguage), color = Color.White, fontWeight = FontWeight.Bold, fontSize = 22.sp)
                            Text(AppStrings.getDiscoverEgypt(currentLanguage), color = Color.White.copy(alpha = 0.7f), fontSize = 13.sp)
                        }
                        IconButton(onClick = onNavigateToSettings, modifier = Modifier.background(Color.White.copy(alpha = 0.15f), shape = RoundedCornerShape(16.dp)).size(40.dp)) {
                            Icon(Icons.Default.Settings, contentDescription = "Settings", tint = Color.White, modifier = Modifier.size(20.dp))
                        }
                    }
                    Spacer(modifier = Modifier.height(24.dp))
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { viewModel.updateSearchQuery(it) },
                        placeholder = { Text(AppStrings.getSearchPlaceholder(currentLanguage), color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f), fontSize = 14.sp) },
                        leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)) },
                        trailingIcon = { if (searchQuery.isNotEmpty()) { IconButton(onClick = { viewModel.updateSearchQuery(""); focusManager.clearFocus() }) { Icon(Icons.Default.Clear, contentDescription = null, tint = MaterialTheme.colorScheme.onSurface) } } },
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                        modifier = Modifier.fillMaxWidth().height(54.dp),
                        shape = RoundedCornerShape(27.dp),
                        colors = OutlinedTextFieldDefaults.colors(focusedContainerColor = MaterialTheme.colorScheme.surface, unfocusedContainerColor = MaterialTheme.colorScheme.surface)
                    )
                }
            }
        }

        item(span = { GridItemSpan(maxLineSpan) }) {
            featuredPlace?.let { place ->
                Card(modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp), shape = RoundedCornerShape(24.dp), colors = CardDefaults.cardColors(containerColor = brandGreen)) {
                    Box(modifier = Modifier.fillMaxWidth().heightIn(min = 140.dp)) {
                        Column(modifier = Modifier.fillMaxWidth().padding(20.dp).padding(end = 100.dp), verticalArrangement = Arrangement.Center) {
                            Text(AppStrings.getFeaturedTitle(currentLanguage), color = goldAccent, fontWeight = FontWeight.SemiBold, fontSize = 11.sp)
                            Text(place.name, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 22.sp, maxLines = 2)
                        }
                        Button(onClick = { onPlaceClick(place.id) }, colors = ButtonDefaults.buttonColors(containerColor = goldAccent), modifier = Modifier.align(Alignment.BottomEnd).padding(16.dp), shape = RoundedCornerShape(12.dp)) {
                            Text(AppStrings.getExploreButton(currentLanguage), color = Color.White, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        item(span = { GridItemSpan(maxLineSpan) }) {
            Column(modifier = Modifier.padding(top = 8.dp)) {
                Text(text = AppStrings.getExploreByType(currentLanguage), fontWeight = FontWeight.Bold, fontSize = 18.sp, color = MaterialTheme.colorScheme.onBackground, modifier = Modifier.padding(horizontal = 20.dp))
                Spacer(modifier = Modifier.height(12.dp))
                CategorySelector(categories, selectedCategory, goldAccent, { viewModel.selectCategory(it) })
            }
        }

        when (uiState) {
            is PlaceUiState.Success -> {
                val placesList = (uiState as PlaceUiState.Success).places
                items(placesList, key = { it.id }) { place ->
                    PlaceCard(place, onClick = { onPlaceClick(place.id) }, onFavoriteClick = { viewModel.toggleFavorite(place) })
                }
            }
            else -> {}
        }
    }
}

@Composable
fun CategorySelector(categories: List<Pair<String, String>>, selectedCategory: String, goldAccent: Color, onCategorySelected: (String) -> Unit) {
    LazyRow(horizontalArrangement = Arrangement.spacedBy(10.dp), contentPadding = PaddingValues(horizontal = 20.dp)) {
        items(categories) { (category, emoji) ->
            val isSelected = category == selectedCategory
            val backgroundColor by animateColorAsState(if (isSelected) goldAccent else MaterialTheme.colorScheme.surfaceVariant, label = "bg")
            val contentColor = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurfaceVariant

            Surface(color = backgroundColor, shape = RoundedCornerShape(20.dp), modifier = Modifier.clickable { onCategorySelected(category) }) {
                Row(modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp), verticalAlignment = Alignment.CenterVertically) {
                    Text("$emoji ", fontSize = 14.sp)
                    Text(category, color = contentColor, fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
                }
            }
        }
    }
}

@Composable
fun PlaceCard(place: PlaceEntity, onClick: () -> Unit, onFavoriteClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 8.dp).clickable { onClick() },
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column {
            Box(modifier = Modifier.fillMaxWidth().height(130.dp)) {
                AsyncImage(model = place.imageUrl, contentDescription = null, modifier = Modifier.fillMaxSize(), contentScale = ContentScale.Crop)
                IconButton(onClick = onFavoriteClick, modifier = Modifier.align(Alignment.TopEnd).padding(8.dp)) {
                    Icon(if (place.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder, contentDescription = null, tint = if (place.isFavorite) Color.Red else Color.White)
                }
            }
            Column(modifier = Modifier.padding(12.dp)) {
                Text(place.name, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = MaterialTheme.colorScheme.onSurface)
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                    Text("📍 ${place.governorate}", color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f), fontSize = 11.sp, modifier = Modifier.weight(1f))
                    Text("⭐ 4.5", color = Color(0xFFD4A373), fontWeight = FontWeight.Bold, fontSize = 11.sp)
                }
            }
        }
    }
}