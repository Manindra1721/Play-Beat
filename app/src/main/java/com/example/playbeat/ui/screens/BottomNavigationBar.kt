package com.example.playbeat.ui.screens

import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.size
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.playbeat.ui.navigation.Destinations
import com.example.playbeat.R

@Composable
fun BottomNavigationBar(navController: NavController) {
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = currentBackStackEntry?.destination?.route

    // Elevation transition effect for a modern sleek look
    val elevation by animateDpAsState(
        targetValue = if (currentDestination == Destinations.HOME) 10.dp else 4.dp,
        animationSpec = tween(durationMillis = 300), label = ""
    )

    // Premium blue gradient background
    val backgroundColor = lerp(
        start = Color(0xFF1a1a1a),
        stop = Color(0xFFe6e6e6),
        fraction = 0.4f
    )

    NavigationBar(
        containerColor = backgroundColor,
        tonalElevation = elevation,
        modifier = Modifier
            .height(60.dp)  // Reduced height for a sleek design
            .fillMaxWidth()
            .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)) // Rounded top corners for sleekness
    ) {


        NavigationItem(
            iconRes = R.drawable.home,
            label = "Home",
            isSelected = currentDestination == Destinations.HOME,
            onClick = { navController.navigateIfNotCurrent(Destinations.HOME) }
        )
        NavigationItem(
            iconRes = R.drawable.notification,
            label = "Reminders",
            isSelected = currentDestination == Destinations.REMINDERS,
            onClick = { navController.navigateIfNotCurrent(Destinations.REMINDERS) }
        )

        NavigationItem(
            iconRes = R.drawable.person,
            label = "Settings",
            isSelected = currentDestination == Destinations.PROFILE,
            onClick = { navController.navigateIfNotCurrent(Destinations.PROFILE) }
        )
    }
}

@Composable
fun RowScope.NavigationItem(
    iconRes: Int,
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val selectedColor = Color.White
    val unselectedColor = Color(0xFFd9d9d9)
    val iconSize by animateDpAsState(
        targetValue = if (isSelected) 40.dp else 28.dp,
        animationSpec = tween(durationMillis = 300), label = ""
    )

    NavigationBarItem(
        icon = {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(
                    painter = painterResource(id = iconRes),
                    contentDescription = null,
                    tint = if (isSelected) selectedColor else unselectedColor,
                    modifier = Modifier.size(iconSize)
                )
                Spacer(modifier = Modifier.height(4.dp))  // Space between icon and text
                Text(
                    text = label,
                    color = if (isSelected) selectedColor else unselectedColor,
                    style = TextStyle(
                        fontSize = 7.sp,  // Further reduced font size to avoid wrapping
                        fontWeight = if (isSelected) FontWeight.Medium else FontWeight.Light,
                        letterSpacing = 0.7.sp // Adjusted letter-spacing
                    )
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
            indicatorColor = Color.Transparent  // No background for selected items
        )
    )
}

fun NavController.navigateIfNotCurrent(destination: String) {
    if (currentBackStackEntry?.destination?.route != destination) {
        navigate(destination)
    }
}
