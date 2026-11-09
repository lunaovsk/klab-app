
package com.klab.app.ui.components.cards

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.klab.app.ui.components.PercentageInput
import com.klab.app.ui.components.SubText
import com.klab.app.ui.theme.*

@Composable
fun MealCard(
    title: String,
    onTitleChange: (String) -> Unit,
    percentage: String,
    onPercentageChange: (String) -> Unit,
    calories: String = "--",
    carbohydrates: String = "--",
    proteins: String = "--",
    lipids: String = "--",
    availableTitles: List<String>,
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
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {

            Column(
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                SubText(
                    text = "Refeição",
                    color = MainTexts
                )

                MealTitleSelector(
                    title = title,
                    onTitleChange = onTitleChange,
                    availableTitles = availableTitles
                )
            }

            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                SubText(
                    text = "Percentual da refeição",
                    color = MainTexts
                )

                PercentageInput(
                    value = percentage,
                    onValueChange = onPercentageChange,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                SubText(
                    text = "Metas da refeição",
                    color = MainTexts,
                    fontSize = 15.sp,
                    fontWeight = androidx.compose.ui.text.font.FontWeight.SemiBold
                )

                NutrientText(
                    name = "Valor energético",
                    value = "$calories kcal"
                )

                NutrientText(
                    name = "Carboidratos",
                    value = "$carbohydrates g"
                )

                NutrientText(
                    name = "Proteínas",
                    value = "$proteins g"
                )

                NutrientText(
                    name = "Lipídios",
                    value = "$lipids g"
                )
            }
        }
    }
}

@Composable
private fun MealTitleSelector(
    title: String,
    onTitleChange: (String) -> Unit,
    availableTitles: List<String>
) {
    var expanded by remember { mutableStateOf(false) }

    Box {
        OutlinedTextField(
            value = title,
            onValueChange = {},
            readOnly = true,
            modifier = Modifier.fillMaxWidth(),
            placeholder = {
                Text("Selecione uma refeição")
            },
            trailingIcon = {
                Text("⌄")
            },
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MainTexts,
                unfocusedBorderColor = MainTexts.copy(alpha = 0.35f)
            )
        )

        Box(
            modifier = Modifier
                .matchParentSize()
                .clickable { expanded = true }
        )

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            availableTitles.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option) },
                    onClick = {
                        onTitleChange(option)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
private fun NutrientText(
    name: String,
    value: String
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
            text = value,
            color = MainTexts
        )
    }
}
