package com.klab.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.klab.app.domain.dto.meal.MealDistribRequest
import com.klab.app.domain.dto.meal.MealPlanRequest
import com.klab.app.domain.model.enumModel.MealType
import com.klab.app.service.usecase.MealService
import com.klab.app.ui.components.MainButton
import com.klab.app.ui.components.NumericInput
import com.klab.app.ui.components.SectionTitle
import com.klab.app.ui.components.SubText
import com.klab.app.ui.components.cards.MealCard
import com.klab.app.ui.theme.BackgroundColor
import com.klab.app.ui.theme.Errors
import com.klab.app.ui.viewModel.MealDistributionViewModel
import org.koin.compose.koinInject


private data class MealOption(
    val title: String,
    val type: MealType
)

private data class Meal(
    val title: String = "",
    val percentage: String = ""
)
@Composable
@Preview
fun MealDistributionScreen(planId: Long, onContinue: () -> Unit = {}) {
    val mealService: MealService = koinInject()

    val viewModel: MealDistributionViewModel = viewModel(
        factory = MealDistributionViewModel.factory(mealService)
    )
    val mealOptions = listOf(
        MealOption("Café da manhã", MealType.BREAKFAST),
        MealOption("Lanche da manhã", MealType.SNACK),
        MealOption("Almoço", MealType.LUNCH),
        MealOption("Lanche da tarde", MealType.SNACK),
        MealOption("Jantar", MealType.DINNER),
        MealOption("Ceia", MealType.OTHER)
    )

    val uiState by viewModel.uiState.collectAsState()

    var mealCount by remember { mutableStateOf("") }

    val meals = remember { mutableStateListOf<Meal>() }

    val totalPercentage = meals.sumOf { it.percentage.toIntOrNull() ?: 0 }

    val isDistributionValid = totalPercentage == 100

    val count = mealCount.toIntOrNull()
    val isMealCountValid = count != null && count in 1..6

    val isFormValid =
        mealCount.toIntOrNull() in 1..6 &&
                meals.isNotEmpty() &&
                meals.all {
                    it.title.isNotBlank() &&
                            (it.percentage.toIntOrNull() in 0..100)
                } &&
                isDistributionValid

    LaunchedEffect(uiState.createdMeals) {
        if (uiState.createdMeals != null) {
            viewModel.consumeCreatedMeals()
            onContinue()
        }
    }

    MaterialTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .background(BackgroundColor)
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {

            SectionTitle(
                text = "Distribuição das refeições",
                fontSize = 24.sp
            )

            SubText(text = "Defina a quantidade de refeições diárias e como sua meta será distribuída entre elas.")

            NumericInput(
                label = "Quantidade de refeições",
                value = mealCount,
                onValueChange = { newValue ->
                    mealCount = newValue

                    val count = newValue.toIntOrNull()

                    if (count != null && count in 1..6) {

                        while (meals.size < count) {
                            meals.add(Meal())
                        }

                        while (meals.size > count) {
                            meals.removeAt(meals.lastIndex)
                        }
                    }
                },
                placeholder = "Ex.: 3",
                unit = "refeições"
            )

            if (mealCount.isNotEmpty() && !isMealCountValid) {
                SubText(
                    text = "Informe uma quantidade entre 1 e 6.",
                    color = Errors
                )
            }

            if (meals.isNotEmpty()) {

                SectionTitle(
                    text = "Percentual por refeição",
                    fontSize = 18.sp
                )

                meals.forEachIndexed { index, meal ->
                    val availableTitles = mealOptions.filter { option ->
                            option.title == meal.title || meals.none { it.title == option.title }
                        }
                        .map { it.title }
                    val mealMacros = uiState.mealMacros[index]


                    MealCard(
                        title = meal.title,
                        onTitleChange = { newTitle -> meals[index] = meal.copy(
                                title = newTitle
                            )
                        },
                        percentage = meal.percentage,
                        onPercentageChange = { newPercentage ->
                            meals[index] = meal.copy(percentage = newPercentage)

                            val percentage = newPercentage.toIntOrNull()

                            if (percentage != null && percentage in 1..100) {


                                viewModel.previewMacros(
                                    planId = planId,
                                    request = MealDistribRequest(
                                        name = meal.title,
                                        type = mealOptions.firstOrNull { it.title == meal.title }?.type?: MealType.OTHER,
                                        percentage = percentage,
                                        displayOrder = index + 1
                                    ),
                                        index = index
                                    )
                                }
                        },
                        calories = mealMacros?.calorie?.toString() ?: "--",
                        carbohydrates = mealMacros?.carb?.toString() ?: "--",
                        proteins = mealMacros?.protein?.toString() ?: "--",
                        lipids = mealMacros?.fat?.toString() ?: "--",
                        availableTitles = availableTitles
                    )
                }

                if (!isDistributionValid) {
                    SubText(
                        text = if (totalPercentage < 100) {
                            "A distribuição precisa completar 100%."
                        } else {
                            "A distribuição ultrapassou 100%."
                        },
                        color = Errors
                    )
                }
            }

            uiState.error?.let { error ->
                SubText(
                    text = error,
                    color = Errors
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            MainButton(
                text = if (uiState.isLoading) {
                    "Salvando..."
                } else {
                    "Continuar"
                },
                onClick = {

                    val requests = meals.mapIndexed { index, meal ->
                        val selectedOption = mealOptions.first { it.title == meal.title }
                        MealDistribRequest(
                            name = meal.title,
                            type = selectedOption.type,
                            percentage = meal.percentage.toInt(),
                            displayOrder = index + 1
                        )
                    }

                    viewModel.createMeals(
                        planId = planId,
                        requests = requests
                    )
                },
                enabled = isFormValid && !uiState.isLoading,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}