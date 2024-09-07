package com.calisthenics.routine.common

import androidx.navigation.NavController

class NavigationUtil {
    companion object {
        fun navigate(navController: NavController, nextScreen: String) {
            navController.navigate(nextScreen) {
                // popUpTo(nextScreen) { inclusive = true }
            }
        }
    }
}