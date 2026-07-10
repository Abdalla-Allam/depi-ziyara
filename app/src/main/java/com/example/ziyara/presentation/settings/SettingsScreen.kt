package com.example.ziyara.presentation.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.runtime.CompositionLocalProvider

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    currentLanguage: String,
    onLanguageChange: (String) -> Unit,
    onBackClick: () -> Unit
) {
    // Brand colors
    val darkGreen = Color(0xFF0F4C43)
    val lightBeige = Color(0xFFF7F4EB)
    val goldAccent = Color(0xFFD4A373)

    // Set UI direction to RTL if Arabic is selected
    val layoutDirection = if (currentLanguage == "AR") LayoutDirection.Rtl else LayoutDirection.Ltr

    CompositionLocalProvider(LocalLayoutDirection provides layoutDirection) {
        Scaffold(
            containerColor = lightBeige,
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = if (currentLanguage == "AR") "الإعدادات" else "Settings",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = onBackClick) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back",
                                tint = Color.White
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent),
                    modifier = Modifier
                        .clip(RoundedCornerShape(bottomStart = 32.dp, bottomEnd = 32.dp))
                        .background(darkGreen)
                )
            }
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                // Language selection card
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Column {
                            Text(
                                text = if (currentLanguage == "AR") "لغة التطبيق" else "App Language",
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp,
                                color = darkGreen
                            )
                            Text(
                                text = if (currentLanguage == "AR") "اختر لغتك المفضلة" else "Choose your preferred language",
                                fontSize = 12.sp,
                                color = Color.Gray
                            )
                        }

                        // Language toggle buttons
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Button(
                                onClick = { onLanguageChange("EN") },
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (currentLanguage == "EN") goldAccent else Color.LightGray.copy(alpha = 0.2f),
                                    contentColor = if (currentLanguage == "EN") Color.White else darkGreen
                                )
                            ) {
                                Text("English", fontWeight = FontWeight.Bold)
                            }

                            Button(
                                onClick = { onLanguageChange("AR") },
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (currentLanguage == "AR") goldAccent else Color.LightGray.copy(alpha = 0.2f),
                                    contentColor = if (currentLanguage == "AR") Color.White else darkGreen
                                )
                            ) {
                                Text("العربية", fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }
    }
}