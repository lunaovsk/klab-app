package com.klab.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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

@Composable
fun PlanningOverviewScreen(
    planId: Long,
    onMealClick: (Long) -> Unit = {},
    modifier: Modifier = Modifier
) {

    val mealService: MealService = koinInject()
    val viewModel: PlanningViewModel = viewModel(factory = PlanningViewModel.factory(mealService))
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(planId) { viewModel.loadMeals(planId) }

    val totalOptions = uiState.meals.sumOf { meal -> meal.options.size }
    val totalCalories = uiState.meals.sumOf { meal -> meal.goal.calorie.toDouble() }
    val meals = uiState.meals

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
                Text(
                    text = "Meu planejamento",
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
                        MaterialTheme.colorScheme.surface,
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
                    text = "$totalCalories",
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
            }
        }
    }
}
