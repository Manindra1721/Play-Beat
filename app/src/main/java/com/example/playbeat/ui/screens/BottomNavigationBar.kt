package com.example.playbeat.ui.screens

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.playbeat.R
import com.example.playbeat.ui.navigation.Destinations


@Composable
fun BottomNavigationBar(
    navController: NavController,
) {
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = currentBackStackEntry?.destination?.route

    // Elevation transition effect for a modern sleek look
    val elevation by animateDpAsState(
        targetValue = if (currentDestination == Destinations.HOME) 10.dp else 4.dp,
        animationSpec = tween(durationMillis = 300)
    )

    val gradientColors = listOf(Color(0xFF000000), Color(0xFF020101)) // Customizable gradient
    val backgroundBrush = Brush.horizontalGradient(colors = gradientColors)

    NavigationBar(
        containerColor = Color.Transparent,
        tonalElevation = elevation,
        modifier = Modifier
            .height(90.dp)
            .fillMaxWidth()
            .background(brush = backgroundBrush)
    ) {
        // Home Navigation Item
        NavigationItem(
            iconRes = R.drawable.home,

            isSelected = currentDestination == Destinations.HOME,
            onClick = { navController.navigateIfNotCurrent(Destinations.HOME) }
        )

        // Now Playing Navigation Item
        NavigationItem(
            iconRes = R.drawable.songs,

            isSelected = currentDestination?.startsWith(Destinations.PLAYER) == true,
            onClick = { navController.navigateIfNotCurrent(Destinations.PLAYER) }
        )

        // Profile Navigation Item
        NavigationItem(
            iconRes = R.drawable.person,

            isSelected = currentDestination == Destinations.PROFILE,
            onClick = { navController.navigateIfNotCurrent(Destinations.PROFILE) }
        )
    }
}

@Composable
fun RowScope.NavigationItem(
    iconRes: Int,

    isSelected: Boolean,
    onClick: () -> Unit
) {
    val selectedColor = Color.White
    val unselectedColor = Color(0xD8DCDCDC)

    // Animating icon size change based on selected state
    val iconSize by animateDpAsState(
        targetValue = if (isSelected) 36.dp else 28.dp,
        animationSpec = tween(durationMillis = 300)
    )

    NavigationBarItem(
        icon = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Spacer(modifier = Modifier.height(6.dp))
                // Icon with dynamic size and color
                Icon(
                    painter = painterResource(id = iconRes),
                    contentDescription = null,
                    tint = if (isSelected) selectedColor else unselectedColor,
                    modifier = Modifier.size(iconSize)
                )


            }
        },
        selected = isSelected,
        onClick = { onClick() },
        colors = NavigationBarItemDefaults.colors(
            selectedIconColor = selectedColor,
            unselectedIconColor = unselectedColor,
            selectedTextColor = selectedColor,
            unselectedTextColor = unselectedColor,
            indicatorColor = Color.Transparent // No background for selected items
        )
    )
}

// Extension function for navigating to a destination if it's not already the current one
fun NavController.navigateIfNotCurrent(destination: String) {
    if (currentBackStackEntry?.destination?.route != destination) {
        navigate(destination)
    }
}

