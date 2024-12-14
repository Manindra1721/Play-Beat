package com.example.playbeat

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.playbeat.ui.theme.PlayBeatTheme
import com.example.playbeat.ui.screens.MainScreen


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PlayBeatTheme  {
                MainScreen() // Launch the main screen with persistent bottom navigation
            }
        }
    }
}