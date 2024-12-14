package com.example.playbeat.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.graphics.Color

@Composable
fun SettingsScreen() {
    var notificationsEnabled by remember { mutableStateOf(true) }
    var darkModeEnabled by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Settings",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF0073e6)
        )
        Spacer(modifier = Modifier.height(24.dp))

        // Notification settings
        Text(text = "Notifications", fontSize = 18.sp)
        Switch(
            checked = notificationsEnabled,
            onCheckedChange = { notificationsEnabled = it },
            modifier = Modifier.padding(vertical = 8.dp)
        )

        // Dark Mode settings
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "Dark Mode", fontSize = 18.sp)
        Switch(
            checked = darkModeEnabled,
            onCheckedChange = { darkModeEnabled = it },
            modifier = Modifier.padding(vertical = 8.dp)
        )

        // Privacy Settings Placeholder
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = "Privacy Settings",
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color.Gray
        )
        Text(text = "Manage your privacy options here", fontSize = 14.sp, color = Color.Gray)
    }
}
