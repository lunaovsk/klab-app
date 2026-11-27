package com.klab.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.klab.app.service.usecase.MealService
import com.klab.app.ui.components.cards.PlanningCard
import com.klab.app.ui.theme.*
import com.klab.app.ui.viewModel.meal.PlanningViewModel
import org.koin.compose.koinInject
import androidx.compose.runtime.getValue
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.klab.app.service.usecase.MealPlanService
import com.klab.app.ui.components.BackButton
import com.klab.app.ui.components.MainButton
import com.klab.app.ui.components.SubText
import com.klab.app.ui.navigation.RegisterBackNavigation

@Composable
fun PlanningOverviewScreen(
    planId: Long,
    onMealClick: (Long) -> Unit = {},
    onConcludePlan: () -> Unit = {},
    onBack: () -> Unit = {},
    canNavigateBack: Boolean = true,
    modifier: Modifier = Modifier
) {

    val mealService: MealService = koinInject()
    val mealPlanService: MealPlanService = koinInject()

    val viewModel: PlanningViewModel = viewModel(factory = PlanningViewModel.factory(mealPlanService,mealService))
    val uiState by viewModel.uiState.collectAsState()

    val handleBack: () -> Unit = {
        if (canNavigateBack && !uiState.isLoading) {
            onBack()
        }
    }

    RegisterBackNavigation(
        onBack = handleBack
    )

    val meals = uiState.meals
    val totalOptions = meals.sumOf { meal -> meal.options.size }
    val planName = uiState.planName
    val dailyCalories = uiState.dailyCalories

    val lifecycleOwner = LocalLifecycleOwner.current

    DisposableEffect(lifecycleOwner, planId) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                viewModel.loadPlan(planId)
                viewModel.loadMeals(planId)
            }
        }

        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    MaterialTheme {
        Column(
            modifier = modifier
                .fillMaxSize()
                .background(BackgroundColor)
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                BackButton(
                    onClick = handleBack,
                    enabled = canNavigateBack && !uiState.isLoading
                )
                Text(
                    text = "$planName",
                    color = MainTexts,
                    style = MaterialTheme.typography.headlineSmall
                )

                Text(
                    text = "Resumo do seu plano alimentar",
                    color = SecondaryTexts,
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Outlines,
                        shape = androidx.compose.foundation.shape.RoundedCornerShape(16.dp)
                    )
                    .padding(18.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(
                    text = "Meta diária",
                    color = SecondaryTexts,
                    style = MaterialTheme.typography.bodySmall
                )

                Text(
                    text = "$dailyCalories",
                    color = GreenDetails,
                    style = MaterialTheme.typography.headlineMedium
                )

                Text(
                    text = "${meals.size} refeições · $totalOptions opções cadastradas",
                    color = SecondaryTexts,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text(
                    text = "Suas refeições",
                    color = MainTexts,
                    style = MaterialTheme.typography.titleMedium
                )
                meals.forEach { meal ->
                    PlanningCard(
                        title = meal.name,
                        optionsCount = meal.options.size,
                        calories = meal.goal.calorie.toString(),
                        onClick = { onMealClick(meal.id) }
                    )
                }

                uiState.error?.let { error ->
                    SubText(
                        text = error,
                        color = Errors
                    )
                }

                MainButton(
                    text = "Concluir",
                    onClick = onConcludePlan,
                    enabled = meals.isNotEmpty() && meals.all { it.options.isNotEmpty() },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}
