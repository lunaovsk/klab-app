
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview

import com.klab.app.ui.components.*
import com.klab.app.ui.theme.*

@Composable
@Preview
fun DailyGoalsScreen(onContinue: () -> Unit = {}) {
    var dailyCalories by remember { mutableStateOf("2000") }
    var carbohydrates by remember { mutableStateOf("50") }
    var proteins by remember { mutableStateOf("20") }
    var lipids by remember { mutableStateOf("30") }

    val calories = dailyCalories.toDoubleOrNull() ?: 0.0

    val carbohydratesPercentage = carbohydrates.toIntOrNull() ?: 0
    val proteinsPercentage = proteins.toIntOrNull() ?: 0
    val lipidsPercentage = lipids.toIntOrNull() ?: 0

    val totalPercentage =
        carbohydratesPercentage + proteinsPercentage + lipidsPercentage

    val isDistributionValid = totalPercentage == 100

    val isFormValid =
        calories > 0 &&
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
                onClick = onContinue,
                enabled = isFormValid,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}