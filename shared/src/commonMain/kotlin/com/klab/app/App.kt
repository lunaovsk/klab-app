package com.klab.app

import androidx.compose.runtime.*
import androidx.compose.ui.tooling.preview.Preview
import com.klab.app.di.appDatabaseBuilder
import com.klab.app.di.appModule
import com.klab.app.infra.dao.FoodDao
import com.klab.app.infra.database.KlabDatabase
import com.klab.app.infra.database.seed.tbcaSeed
import com.klab.app.service.usecase.FoodService
import com.klab.app.service.usecase.MealPlanService
import com.klab.app.ui.navigation.AppNavigation
import org.koin.compose.KoinApplication
import org.koin.compose.koinInject

@Composable
@Preview
fun App() {
    val builder = appDatabaseBuilder
    if (builder != null) {
        KoinApplication(
            application = {
                modules(appModule(builder))
            }
        ) {
            val foodService: FoodService = koinInject()
            val mealPlanService: MealPlanService = koinInject()

            val database: KlabDatabase = koinInject()
            val foodDao: FoodDao = koinInject()

            LaunchedEffect(Unit) {
                tbcaSeed(database, foodDao)
            }

            AppNavigation(
                foodService = foodService,
                mealPlanService = mealPlanService
            )
        }
    } else {
        AppNavigation()
    }
}
