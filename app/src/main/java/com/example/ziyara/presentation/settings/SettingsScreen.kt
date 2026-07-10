package com.example.ziyara.presentation.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onBackClick: () -> Unit
) {
    // باليت الألوان الأساسية الخاصة بالتطبيق
    val darkGreen = Color(0xFF0F4C43)
    val lightBeige = Color(0xFFF7F4EB)
    val goldAccent = Color(0xFFD4A373)

    // حالات تجريبية مؤقتة للـ UI
    var isDarkMode by remember { mutableStateOf(false) }
    var selectedLanguage by remember { mutableStateOf("EN") }

    // ديناميكية عكس الألوان بناءً على حالة الـ Dark Mode 🌙
    val backgroundColor = if (isDarkMode) darkGreen else lightBeige
    val cardColor = if (isDarkMode) Color(0xFF16594F) else Color.White
    val primaryTextColor = if (isDarkMode) lightBeige else darkGreen
    val secondaryTextColor = if (isDarkMode) Color.White.copy(alpha = 0.6f) else Color.Gray

    Scaffold(
        containerColor = backgroundColor, // ضفنا لون الخلفية هنا عشان يتناسق مع كيرف الهيدر 🚀
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Settings",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack, // غيرنا الكلمة لسهم شيك 🚀
                            contentDescription = "Back",
                            tint = goldAccent
                        )
                    }
                },
                // خلينا لون الـ AppBar الأساسي شفاف واديناه الـ Modifier بتاع الكيرف واللون
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

            // 1) كارد التحكم في الـ Dark Mode 🌙
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = cardColor),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text("Dark Mode", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = primaryTextColor)
                        Text("Switch between light and dark theme", fontSize = 12.sp, color = secondaryTextColor)
                    }
                    Switch(
                        checked = isDarkMode,
                        onCheckedChange = { isDarkMode = it },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = darkGreen,
                            checkedTrackColor = goldAccent,
                            uncheckedThumbColor = Color.Gray,
                            uncheckedTrackColor = Color.LightGray.copy(alpha = 0.5f)
                        )
                    )
                }
            }

            // 2) كارد التحكم في اللغة 🌍
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = cardColor),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Column {
                        Text("App Language", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = primaryTextColor)
                        Text("Choose your preferred language", fontSize = 12.sp, color = secondaryTextColor)
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        // زرار اللغة الإنجليزية
                        Button(
                            onClick = { selectedLanguage = "EN" },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (selectedLanguage == "EN") goldAccent else Color.LightGray.copy(alpha = 0.2f),
                                contentColor = if (selectedLanguage == "EN") Color.White else primaryTextColor
                            )
                        ) {
                            Text("English", fontWeight = FontWeight.Bold)
                        }

                        // زرار اللغة العربية
                        Button(
                            onClick = { selectedLanguage = "AR" },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (selectedLanguage == "AR") goldAccent else Color.LightGray.copy(alpha = 0.2f),
                                contentColor = if (selectedLanguage == "AR") Color.White else primaryTextColor
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