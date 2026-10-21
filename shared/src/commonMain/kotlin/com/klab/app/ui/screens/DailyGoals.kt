
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.klab.app.ui.components.MainButton
import com.klab.app.ui.components.*
import com.klab.app.ui.theme.*
import kotlin.math.roundToInt

@Composable
@Preview
fun DailyGoalsScreen(
    onContinue: () -> Unit
) {
    var dailyCalories by remember {
        mutableStateOf("2000")
    }

    var carbohydrates by remember {
        mutableStateOf("50")
    }

    var proteins by remember {
        mutableStateOf("20")
    }

    var lipids by remember {
        mutableStateOf("30")
    }

    val calories = dailyCalories.toDoubleOrNull() ?: 0.0

    val carbohydratesPercentage =
        carbohydrates.toIntOrNull() ?: 0

    val proteinsPercentage =
        proteins.toIntOrNull() ?: 0

    val lipidsPercentage =
        lipids.toIntOrNull() ?: 0

    val totalPercentage =
        carbohydratesPercentage +
                proteinsPercentage +
                lipidsPercentage

    val isDistributionValid = totalPercentage == 100

    val isFormValid =
        calories > 0 &&
                isDistributionValid &&
                carbohydrates.isNotEmpty() &&
                proteins.isNotEmpty() &&
                lipids.isNotEmpty()

    fun calculateGrams(
        percentage: Int,
        caloriesPerGram: Int
    ): String {
        if (calories <= 0) return "0"

        val grams =
            calories * percentage / 100.0 / caloriesPerGram

        val tenths = (grams * 10).roundToInt()

        return if (tenths % 10 == 0) {
            (tenths / 10).toString()
        } else {
            "${tenths / 10},${tenths % 10}"
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundColor)
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Metas nutricionais",
            color = MainTexts,
            fontSize = 23.sp,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = "Etapa 1 de 3 · Definição das metas diárias",
            color = SecondaryTexts,
            fontSize = 13.sp
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Color.White,
                    androidx.compose.foundation.shape
                        .RoundedCornerShape(16.dp)
                )
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            SectionTitle(text = "Meta calórica diária")

            NumericInput(
                value = dailyCalories,
                onValueChange = { dailyCalories = it },
                label = "Quantidade de calorias",
                unit = "kcal/dia"
            )
        }

        SectionTitle(text = "Distribuição dos macronutrientes")

        MacronutrientCard(
            name = "Carboidratos",
            percentage = carbohydrates,
            onPercentageChange = { carbohydrates = it },
            grams = calculateGrams(
                carbohydratesPercentage, 4
            ),
            nutrientColor = Carbohydrates
        )

        MacronutrientCard(
            name = "Proteínas",
            percentage = proteins,
            onPercentageChange = { proteins = it },
            grams = calculateGrams(proteinsPercentage, 4),
            nutrientColor = Proteins
        )

        MacronutrientCard(
            name = "Lipídios",
            percentage = lipids,
            onPercentageChange = { lipids = it },
            grams = calculateGrams(lipidsPercentage, 9),
            nutrientColor = Lipids
        )

        Column(
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = "Total dos percentuais: $totalPercentage%",
                color = if (isDistributionValid) GreenDetails
                else Errors,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium
            )

            if (!isDistributionValid) {
                Text(
                    text = "A soma dos percentuais deve ser 100%.",
                    color = Errors,
                    fontSize = 13.sp
                )
            }

            if (dailyCalories.isNotEmpty() && calories <= 0) {
                Text(
                    text = "Informe uma meta calórica maior que zero.",
                    color = Errors,
                    fontSize = 13.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(4.dp))

        MainButton(
            text = "Continuar para refeições →",
            onClick = onContinue,
            modifier = Modifier.fillMaxWidth(),
            enabled = isFormValid
        )
    }
}
