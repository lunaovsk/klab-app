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
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.klab.app.domain.model.food.Macros
import com.klab.app.domain.model.meal.MealPlan
import com.klab.app.service.usecase.MealPlanService
import com.klab.app.ui.components.*
import com.klab.app.ui.theme.*
import kotlinx.coroutines.launch
import kotlinx.datetime.TimeZone
import kotlinx.datetime.todayIn
import kotlin.time.Clock

@Composable
@Preview
fun DailyGoalsScreen(
    mealPlanService: MealPlanService? = null,
    onContinue: () -> Unit = {}
) {
    val coroutineScope = rememberCoroutineScope()

    var title by remember { mutableStateOf("") }
    var dailyCalories by remember { mutableStateOf("") }
    var carbohydrates by remember { mutableStateOf("") }
    var proteins by remember { mutableStateOf("") }
    var lipids by remember { mutableStateOf("") }

    val calories = dailyCalories.toFloatOrNull() ?: 0f

    val carbohydratesPercentage = carbohydrates.toIntOrNull() ?: 0
    val proteinsPercentage = proteins.toIntOrNull() ?: 0
    val lipidsPercentage = lipids.toIntOrNull() ?: 0

    val totalPercentage =
        carbohydratesPercentage + proteinsPercentage + lipidsPercentage

    val isDistributionValid = totalPercentage == 100

    val isFormValid =
        title.isNotBlank() &&
                calories > 0f &&
                dailyCalories.isNotBlank() &&
                carbohydrates.toIntOrNull() != null &&
                proteins.toIntOrNull() != null &&
                lipids.toIntOrNull() != null &&
                carbohydratesPercentage in 0..100 &&
                proteinsPercentage in 0..100 &&
                lipidsPercentage in 0..100 &&
                isDistributionValid

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
                text = "Metas diárias",
                fontSize = 24.sp
            )

            SubText(
                text = "Defina a meta energética e a distribuição dos macronutrientes."
            )

            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Título do planejamento") },
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MainTexts,
                    unfocusedBorderColor = MainTexts.copy(alpha = 0.35f),
                    focusedTextColor = MainTexts,
                    unfocusedTextColor = MainTexts
                )
            )

            NumericInput(
                label = "Meta calórica diária (kcal)",
                value = dailyCalories,
                onValueChange = { dailyCalories = it },
                placeholder = "Ex.: 2000",
                unit = "kcal/dia"
            )

            SectionTitle(
                text = "Distribuição de macronutrientes",
                fontSize = 18.sp
            )

            MacronutrientCard(
                name = "Carboidratos",
                percentage = carbohydrates,
                onPercentageChange = { carbohydrates = it },
                grams = "--",
                nutrientColor = Carbohydrates
            )

            MacronutrientCard(
                name = "Proteínas",
                percentage = proteins,
                onPercentageChange = { proteins = it },
                grams = "--",
                nutrientColor = Proteins
            )

            MacronutrientCard(
                name = "Lipídios",
                percentage = lipids,
                onPercentageChange = { lipids = it },
                grams = "--",
                nutrientColor = Lipids
            )

            if (!isDistributionValid) {
                SubText(
                    text = if (totalPercentage < 100) {
                        "A distribuição precisa completar 100%."
                    } else {
                        "A distribuição ultrapassou 100%"
                    },
                    color = Errors
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            MainButton(
                text = "Continuar",
                onClick = {
                    coroutineScope.launch {
                        mealPlanService?.create(
                            MealPlan(
                                title = title,
                                createdAt = Clock.System.todayIn(TimeZone.currentSystemDefault()),
                                goal = Macros(
                                    calorie = calories,
                                    carb = carbohydratesPercentage.toFloat(),
                                    protein = proteinsPercentage.toFloat(),
                                    fat = lipidsPercentage.toFloat()
                                )
                            )
                        )
                        onContinue()
                    }
                },
                enabled = isFormValid,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}
