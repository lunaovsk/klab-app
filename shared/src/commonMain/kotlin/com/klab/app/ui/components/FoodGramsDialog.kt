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
    isEditing: Boolean = false,
    onDismiss: () -> Unit,
    onConfirm: (Float) -> Unit
) {
    var grams by remember(food.id, initialGrams) {
        mutableStateOf(initialGrams.toString())
    }

    val quantity = grams.replace(',', '.')
        .toFloatOrNull()
        ?.takeIf { it.isFinite() && it > 0f }

    val macros = quantity?.let {
        food.amountPer100g.calculateForPortion(it)
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                if (isEditing) {
                    "Editar quantidade"
                } else {
                    "Quantidade do alimento"
                }
            )
        },
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
                        if (value.matches(Regex("""^\d*[.,]?\d*$"""))) {
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
                    listOf(
                        "Calorias" to macros.calorie,
                        "Carboidratos" to macros.carb,
                        "Proteínas" to macros.protein,
                        "Lipídios" to macros.fat
                    ).forEach { (name, value) ->
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
                Text(if (isEditing) "Salvar" else "Adicionar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        },
        containerColor = BackgroundColor
    )
}
