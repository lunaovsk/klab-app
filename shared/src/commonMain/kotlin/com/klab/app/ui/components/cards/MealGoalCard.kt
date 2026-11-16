package com.klab.app.ui.components.cards

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.klab.app.domain.dto.MacrosDTO
import com.klab.app.ui.components.SectionTitle
import com.klab.app.ui.components.SubText
import com.klab.app.ui.theme.*
import kotlin.math.round

@Composable
fun MealGoalCard(
    goal: MacrosDTO,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = GreenDetails.copy(alpha = 0.15f)
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            SectionTitle(
                text = "Meta da refeição",
                fontSize = 18.sp
            )

            SectionTitle(
                text = "${goal.calorie.formatGoal()} kcal",
                fontSize = 24.sp
            )

            GoalNutrient(
                name = "Carboidratos",
                value = goal.carb,
                color = Carbohydrates
            )

            GoalNutrient(
                name = "Proteínas",
                value = goal.protein,
                color = Proteins
            )

            GoalNutrient(
                name = "Lipídios",
                value = goal.fat,
                color = Lipids
            )
        }
    }
}

@Composable
private fun GoalNutrient(
    name: String,
    value: Float,
    color: Color
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        SubText(
            text = name,
            color = SecondaryTexts
        )

        SubText(
            text = "${value.formatGoal()} g",
            color = color
        )
    }
}

private fun Float.formatGoal(): String =
    (round(this * 10f) / 10f).toString()
