package com.klab.app.ui.components.cards

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.klab.app.domain.model.food.Food
import com.klab.app.ui.theme.*

@Composable
fun FoodResultItem(
    food: Food,
    isSelected: Boolean,
    onSelect: () -> Unit,
    showSelectionAction: Boolean = true,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(WhiteColor, RoundedCornerShape(16.dp))
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {

        Column {
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
            if (showSelectionAction) {
                TextButton(
                    onClick = onSelect
                ) {
                    Text(
                        text = if (isSelected) {
                            "Selecionado"
                        } else {
                            "+ Adicionar"
                        },
                        color = Button
                    )
                }
            }
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