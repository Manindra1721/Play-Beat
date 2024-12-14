package com.example.playbeat.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.playbeat.ui.screens.*
import com.google.firebase.auth.FirebaseAuth

object Destinations {
    const val LOGIN = "login"
    const val HOME = "home"
    const val PROFILE = "profile"
    const val REMINDERS = "reminders"
    const val SIGN_UP = "sign_up"
    const val SETTINGS = "settings"
}

@Composable
fun MainNavHost(
    navController: NavHostController,
    startDestination: String = if (FirebaseAuth.getInstance().currentUser != null) Destinations.HOME else Destinations.LOGIN
) {
    val auth = FirebaseAuth.getInstance()

    NavHost(navController = navController, startDestination = startDestination) {
        composable(Destinations.LOGIN) {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(Destinations.HOME) {
                        popUpTo(Destinations.LOGIN) { inclusive = true }
                    }
                },
                onSignUp = {
                    navController.navigate(Destinations.SIGN_UP)
                }
            )
        }
        composable(Destinations.HOME) {
            HomeScreen(navController = navController)
        }
        composable(Destinations.PROFILE) {
            ProfileScreen(
                onLogout = {
                    auth.signOut()
                    navController.navigate(Destinations.LOGIN) {
                        popUpTo(Destinations.HOME) { inclusive = true }
                    }
                }
            )
        }


        composable(Destinations.SIGN_UP) {
            SignUpScreen(
                onSignUpSuccess = {
                    navController.navigate(Destinations.HOME) {
                        popUpTo(Destinations.SIGN_UP) { inclusive = true }
                    }
                },
                navController = navController
            )
        }
        composable(Destinations.REMINDERS) {
            RemindersScreen()
        }
    }
}
