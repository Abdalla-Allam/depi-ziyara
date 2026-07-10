package com.example.ziyara.presentation.details

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import coil.compose.AsyncImage
import com.example.ziyara.navigation.Screen
import com.example.ziyara.presentation.home.HomeViewModel

@Composable
fun PlaceDetailsScreen(
    placeId: Int,
    viewModel: HomeViewModel,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,

) {
    val allPlaces by viewModel.places.collectAsState(initial = emptyList())
    val place = allPlaces.find { it.id == placeId }

    if (place == null) {
        Box(
            modifier = Modifier.fillMaxSize().background(Color(0xFF142222)),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(color = Color(0xFFD4A373))
        }
        return
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF142222))
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(380.dp)
                ) {
                    AsyncImage(
                        model = place.imageUrl,
                        contentDescription = place.name,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(340.dp)
                            .clip(RoundedCornerShape(bottomStart = 32.dp, bottomEnd = 32.dp)),
                        contentScale = ContentScale.Crop
                    )

                    IconButton(
                        onClick = onBackClick,
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .padding(top = 48.dp, start = 24.dp)
                            .background(Color.Black.copy(alpha = 0.4f), CircleShape)
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }

                    IconButton(
                        onClick = { /* Share action */ },
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(top = 48.dp, end = 24.dp)
                            .background(Color.Black.copy(alpha = 0.4f), CircleShape)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Share,
                            contentDescription = "Share",
                            tint = Color.White
                        )
                    }

                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .padding(end = 32.dp, bottom = 16.dp)
                    ) {
                        FloatingActionButton(
                            onClick = { viewModel.toggleFavorite(place) },
                            shape = CircleShape,
                            containerColor = if (place.isFavorite) Color(0xFFF25252) else Color.White,
                            contentColor = if (place.isFavorite) Color.White else Color.Gray,
                            elevation = FloatingActionButtonDefaults.elevation(4.dp),
                            modifier = Modifier.size(56.dp)
                        ) {
                            Icon(
                                imageVector = if (place.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                contentDescription = "Favorite",
                                modifier = Modifier.size(28.dp)
                            )
                        }
                    }
                }
            }

            item {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .padding(horizontal = 16.dp)
                        .padding(bottom = 24.dp),
                    shape = RoundedCornerShape(28.dp),
                    color = Color.White
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp)
                    ) {
                        Surface(
                            color = Color(0xFFFDF4E7),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(
                                text = place.category.uppercase(),
                                color = Color(0xFFD4A373),
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = place.name,
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1A1A1A)
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // جعلنا التوزيع انسيابياً (Weight) لكي لا تنضغط العناصر أفقياً على الشاشات الصغيرة
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Box(modifier = Modifier.weight(1f)) {
                                InfoBadge(text = place.governorate, iconText = "📍", containerColor = Color(0xFFE8F5E9), contentColor = Color(0xFF2E7D32))
                            }
                            Box(modifier = Modifier.weight(1.2f)) {
                                InfoBadge(text = "9:00 AM - 11:00 PM", iconText = "🕒", containerColor = Color(0xFFFFF3E0), contentColor = Color(0xFFE65100))
                            }
                            Box(modifier = Modifier.weight(0.8f)) {
                                InfoBadge(
                                    text = if (place.ticketPrice == 0.0) "Free" else "${place.ticketPrice.toInt()} EGP",
                                    iconText = "🎟️",
                                    containerColor = Color(0xFFF5F5F5),
                                    contentColor = Color(0xFF616161)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            repeat(5) {
                                Text(text = "⭐", fontSize = 14.sp)
                            }
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(text = "4.7", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                            Text(text = " / 5.0", color = Color.Gray, fontSize = 14.sp)
                        }

                        Spacer(modifier = Modifier.height(24.dp))
                        Spacer(modifier = Modifier.height(24.dp))

                        Text(
                            text = "About This Place",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = place.description,
                            fontSize = 15.sp,
                            color = Color(0xFF555555),
                            lineHeight = 22.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun InfoBadge(
    text: String,
    iconText: String,
    containerColor: Color,
    contentColor: Color
) {
    Surface(
        color = containerColor,
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 6.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(text = iconText, fontSize = 11.sp)
            Spacer(modifier = Modifier.width(2.dp))
            Text(
                text = text,
                color = contentColor,
                fontSize = 11.sp,
                fontWeight = FontWeight.Medium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}