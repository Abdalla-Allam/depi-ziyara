package com.example.ziyara.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// Light theme colors only
private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF0F4C43),
    background = Color(0xFFF7F4EB),
    surface = Color.White,
    onBackground = Color.Black,
    onSurface = Color.Black,
    error = Color(0xFFB00020),
    onPrimary = Color.White
)

@Composable
fun ZiyaraTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = LightColorScheme,
        typography = Typography,
        content = content
    )
}