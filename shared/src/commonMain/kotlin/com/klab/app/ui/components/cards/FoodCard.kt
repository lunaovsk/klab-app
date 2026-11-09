package com.klab.app.ui.components.cards

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.klab.app.domain.model.food.Food
import com.klab.app.ui.theme.*

@Composable
fun FoodResultItem(
    food: Food,
    isSelected: Boolean,
    onSelect: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(
                MaterialTheme.colorScheme.surface,
                RoundedCornerShape(16.dp)
            )
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {

        Column(
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = food.description,
                color = MainTexts,
                style = MaterialTheme.typography.titleMedium
            )

            Text(
                text = food.group,
                color = SecondaryTexts,
                style = MaterialTheme.typography.bodySmall
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            NutrientValue(
                label = "Calorias",
                value = "${food.amountPer100g.calorie} kcal"
            )

            NutrientValue(
                label = "Carboidratos",
                value = "${food.amountPer100g.carb} g"
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            NutrientValue(
                label = "Proteínas",
                value = "${food.amountPer100g.protein} g"
            )

            NutrientValue(
                label = "Lipídios",
                value = "${food.amountPer100g.fat} g"
            )
        }

        TextButton(
            onClick = onSelect,
            modifier = Modifier.align(androidx.compose.ui.Alignment.End)
        ) {
            Text(
                text = if (isSelected) "Adicionado ✓" else "+ Adicionar",
                color = GreenDetails
            )
        }
    }
}

@Composable
private fun NutrientValue(
    label: String,
    value: String
) {
    Column(
        modifier = Modifier.widthIn(min = 100.dp),
        verticalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        Text(
            text = label,
            color = SecondaryTexts,
            style = MaterialTheme.typography.bodySmall
        )

        Text(
            text = value,
            color = MainTexts,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}