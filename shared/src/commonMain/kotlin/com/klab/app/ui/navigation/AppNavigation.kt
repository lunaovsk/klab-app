package com.klab.app.ui.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.savedstate.read
import com.klab.app.service.usecase.FoodService
import com.klab.app.service.usecase.MealPlanService
import com.klab.app.ui.screens.*

@Composable
fun AppNavigation(
    foodService: FoodService? = null,
    mealPlanService: MealPlanService? = null
) {
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
                        navController.navigate(AppRoutes.DAILY_GOALS)
                    }
                )
            }

            composable(AppRoutes.DAILY_GOALS) {
                DailyGoalsScreen(
                    onContinue = { planId ->
                        navController.navigate("meal_distribution/$planId")
                    }
                )
            }
            composable(AppRoutes.MEAL_DISTRI,
                    arguments = listOf(
                    navArgument("planId") {
                        type = NavType.LongType
                    }
                    )
            ) {backStackEntry ->

                val planId = backStackEntry.arguments
                    ?.read { getLongOrNull("planId") }
                    ?: return@composable

                MealDistributionScreen(
                    planId = planId,
                    onContinue = { navController.navigate("planning/$planId")}
                )
            }
            composable(AppRoutes.PLANNING_OVERVIEW,arguments = listOf(
                navArgument("planId") { type = NavType.LongType }
            )) {backStackEntry ->

                val planId = backStackEntry.arguments
                    ?.read { getLongOrNull("planId") }
                    ?: return@composable
                PlanningOverviewScreen(
                    planId = planId,
                    onMealClick = {}
                )
            }

            composable(AppRoutes.SEARCH_FOOD) {
                FoodSearchScreen(
                    onBack = {
                        navController.popBackStack()
                    },
                    onFoodsSelected = { foods ->
                        navController.previousBackStackEntry
                            ?.savedStateHandle
                            ?.set("selected_food_ids", foods.map { it.food.id})

                        navController.popBackStack()
                    }

                )
            }

        }
    }
}
