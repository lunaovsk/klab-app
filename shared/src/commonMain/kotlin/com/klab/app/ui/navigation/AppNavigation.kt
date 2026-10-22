
package com.klab.app.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.klab.app.ui.screens.*

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = AppRoutes.HOME
    ) {
        composable(AppRoutes.HOME) {
            HomeScreen(
                onStartPlanning = {
                    navController.navigate(
                        AppRoutes.DAILY_GOALS
                    )
                }
            )
        }

        composable(AppRoutes.DAILY_GOALS) {
            DailyGoalsScreen(
                onContinue = {}
            )
        }
    }
}
