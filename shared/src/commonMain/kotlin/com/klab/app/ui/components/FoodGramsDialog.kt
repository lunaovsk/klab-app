package com.klab.app.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.klab.app.domain.model.food.Food
import com.klab.app.ui.theme.*
import kotlin.math.round

@Composable
fun FoodQuantityDialog(
    food: Food,
    initialGrams: Float = 100f,
    onDismiss: () -> Unit,
    onConfirm: (Float) -> Unit
) {
    var grams by remember(food.id) {
        mutableStateOf(initialGrams.toString())
    }

    val quantity = grams.toFloatOrNull()
        ?.takeIf { it > 0 }

    val macros = quantity?.let {
        val factor = it / 100f

        listOf(
            "Calorias" to food.amountPer100g.calorie * factor,
            "Carboidratos" to food.amountPer100g.carb * factor,
            "Proteínas" to food.amountPer100g.protein * factor,
            "Lipídios" to food.amountPer100g.fat * factor
        )
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Quantidade do alimento") },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = food.description,
                    color = MainTexts,
                    style = MaterialTheme.typography.titleMedium
                )

                OutlinedTextField(
                    value = grams,
                    onValueChange = { value ->
                        if (value.matches(Regex("^\\d*\\.?\\d*$"))) {
                            grams = value
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Quantidade em gramas") },
                    suffix = { Text("g") },
                    singleLine = true
                )

                HorizontalDivider()

                Text(
                    text = "Valores nutricionais",
                    style = MaterialTheme.typography.titleSmall,
                    color = MainTexts
                )

                if (macros != null) {
                    macros.forEach { (name, value) ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(name, color = SecondaryTexts)
                            Text(
                                text = "${round(value * 100) / 100}",
                                color = MainTexts
                            )
                        }
                    }
                } else {
                    Text(
                        text = "Informe uma quantidade válida para calcular.",
                        color = SecondaryTexts
                    )
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = { quantity?.let(onConfirm) },
                enabled = quantity != null
            ) {
                Text("Adicionar")
            }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancelar") }
        },
        containerColor = BackgroundColor
    )
}