package com.example.playbeat.ui.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.playbeat.ui.navigation.MainNavHost
import com.example.playbeat.ui.navigation.Destinations

@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry?.destination?.route

    // List of screens that should show the bottom navigation bar
    val screensWithBottomNavBar = listOf(
        Destinations.HOME,
        Destinations.PROFILE,

        Destinations.REMINDERS,
        Destinations.SETTINGS
    )

    Scaffold(
        bottomBar = {
            if (currentRoute in screensWithBottomNavBar) {
                BottomNavigationBar(navController = navController)
            }
        }
    ) { innerPadding ->
        MainNavHost(
            navController = navController
        )
    }
}
