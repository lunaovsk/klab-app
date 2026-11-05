
package com.klab.app.ui.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.klab.app.ui.screens.*

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .windowInsetsPadding(
                WindowInsets.safeDrawing.only(WindowInsetsSides.Top)
            )
    ) {
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
                    onContinue = {
                        navController.navigate(
                            AppRoutes.MEAL_DISTRI
                        )
                    }
                )
            }
            composable(AppRoutes.MEAL_DISTRI) {
                MealDistributionScreen(
                    onContinue = {}
                )
            }
        }
    }
}
