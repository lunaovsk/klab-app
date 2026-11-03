
package com.klab.app.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.klab.app.service.usecase.MealPlanService
import com.klab.app.ui.screens.*
import com.klab.app.ui.viewModel.MealPlanFactory
import com.klab.app.ui.viewModel.MealPlanViewModel
import org.koin.compose.koinInject

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val mealPlanService: MealPlanService = koinInject()
    val factory = remember(mealPlanService) {
        MealPlanFactory(mealPlanService)
    }

    val mealPlanViewModel: MealPlanViewModel = viewModel(
        factory = factory
    )

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
                viewModel = mealPlanViewModel,
                onContinue = {}
            )
        }
    }
}
