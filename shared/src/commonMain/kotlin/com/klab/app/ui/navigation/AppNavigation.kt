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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.savedstate.read
import com.klab.app.service.usecase.MealItemService
import com.klab.app.service.usecase.MealOptionService
import com.klab.app.service.usecase.MealService
import com.klab.app.ui.screens.*
import com.klab.app.ui.screens.enums.SearchModeFood
import com.klab.app.ui.viewModel.meal.MealEditorViewModel
import org.koin.compose.koinInject

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
                        navController.navigate(AppRoutes.DAILY_GOALS)
                    }
                )
            }

            composable(AppRoutes.DAILY_GOALS) {
                DailyGoalsScreen(
                    onContinue = { planId ->
                        navController.navigate(AppRoutes.mealDistribution(planId))
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
                    onContinue = { navController.navigate(AppRoutes.planningOverview(planId))}
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
                    onMealClick = { mealId ->
                        navController.navigate(AppRoutes.mealEditor(mealId))
                    },
                    onConcludePlan = {
                        navController.navigate(AppRoutes.pdfPreview(planId)) }
                )
            }

            composable(route = AppRoutes.MEAL_EDITOR,
                arguments = listOf(
                    navArgument("mealId") { type = NavType.LongType }
                )
            ) { backStackEntry ->

                val mealId = backStackEntry.arguments
                    ?.read { getLongOrNull("mealId") }
                    ?: return@composable


                MealEditorScreen(
                    mealId = mealId,
                    onBack = {
                        navController.popBackStack()
                    },
                    onSearchFoods = { draftId ->
                        navController.navigate(
                            AppRoutes.searchFoodOption(mealId, draftId)
                        )
                    }
                )

            }

            composable(
                route = AppRoutes.SEARCH_FOOD_OPTION,
                arguments = listOf(
                    navArgument("mealId") {
                        type = NavType.LongType
                    },
                    navArgument("optionId") {
                        type = NavType.LongType
                    }
                )
            ) { backStackEntry ->

                val mealId = backStackEntry.arguments
                    ?.read { getLongOrNull("mealId") }
                    ?: return@composable

                val draftId = backStackEntry.arguments
                    ?.read { getLongOrNull("optionId") }
                    ?: return@composable

                val editorEntry = navController.previousBackStackEntry
                    ?: return@composable

                val mealService: MealService = koinInject()
                val mealOptionService: MealOptionService = koinInject()
                val mealItemService: MealItemService = koinInject()

                val editorViewModel: MealEditorViewModel = viewModel(
                    viewModelStoreOwner = editorEntry,
                    factory = MealEditorViewModel.factory(mealService, mealOptionService, mealItemService)
                )

                FoodSearchScreen(
                    mode = SearchModeFood.SELECT_FOR_MEAL,

                    onBack = { navController.popBackStack() },

                    onFoodsSelected = { selectedFoods ->
                        editorViewModel.addFoodsToDraft(
                            mealId = mealId,
                            draftId = draftId,
                            foods = selectedFoods
                        )

                        navController.popBackStack()
                    }
                )
            }

            composable(AppRoutes.SEARCH_FOOD) {
                FoodSearchScreen(
                    mode = SearchModeFood.SEARCH_ONLY,
                    onBack = {
                        navController.popBackStack()
                    }
                )
            }

            composable(
                route = AppRoutes.PDF_PREVIEW,
                arguments = listOf(
                    navArgument("planId") {
                        type = NavType.LongType
                    }
                )
            ) { backStackEntry ->

                val planId = backStackEntry.arguments
                    ?.read { getLongOrNull("planId") }
                    ?: return@composable

                PdfPreviewScreen(
                    planId = planId,
                    onBack = {
                        navController.navigate(AppRoutes.HOME) {
                            popUpTo(AppRoutes.HOME) {
                                inclusive = false
                            }

                            launchSingleTop = true
                        }
                    }
                )
            }

        }
    }
}
