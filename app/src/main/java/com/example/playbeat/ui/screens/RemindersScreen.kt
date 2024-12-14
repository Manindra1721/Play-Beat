package com.example.playbeat.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun RemindersScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Reminders", fontSize = 24.sp)
        Spacer(modifier = Modifier.height(16.dp))

        // Placeholder reminder buttons
        Button(onClick = { /* Add water reminder logic */ }, modifier = Modifier.fillMaxWidth()) {
            Text("Set Water Reminder")
        }
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = { /* Add exercise reminder logic */ }, modifier = Modifier.fillMaxWidth()) {
            Text("Set Exercise Reminder")
        }
    }
}
