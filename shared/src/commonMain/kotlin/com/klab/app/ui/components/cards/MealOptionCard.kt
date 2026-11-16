
package com.klab.app.ui.components.cards

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.klab.app.domain.dto.MacrosDTO
import com.klab.app.domain.dto.meal.MealEditorOption
import com.klab.app.domain.dto.meal.MealItemDetailsResponse
import com.klab.app.ui.components.FoodQuantityDialog
import com.klab.app.ui.components.MainButton
import com.klab.app.ui.components.SubText
import com.klab.app.ui.theme.*
import kotlin.math.round

@Composable
fun MealOptionCard(
    option: MealEditorOption,
    goal: MacrosDTO,
    enabled: Boolean,
    onSearchFoods: () -> Unit,
    onRemoveFood: (Long) -> Unit,
    onRemoveOption: () -> Unit,
    onEditFood: (Long, Float) -> Unit,
    modifier: Modifier = Modifier
) {
    var editingItem by remember(option.id) {
        mutableStateOf<MealItemDetailsResponse?>(null)
    }

    val progress = if (goal.calorie > 0f) {
        (option.total.calorie / goal.calorie)
            .coerceIn(0f, 1f)
    } else {
        0f
    }

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = GreenDetails.copy(alpha = 0.15f)
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = option.title,
                    color = MainTexts,
                    style = MaterialTheme.typography.titleMedium
                )

                TextButton(
                    onClick = onRemoveOption,
                    enabled = enabled
                ) {
                    Text("Excluir", color = Errors)
                }
            }

            HorizontalDivider()

            if (option.items.isEmpty()) {
                SubText(
                    text = "Nenhum alimento adicionado nesta opção."
                )
            } else {
                option.items.forEach { details ->
                    key(details.item.id) {
                        MealOptionFoodItem(
                            details = details,
                            enabled = enabled,
                            onEdit = {
                                editingItem = details
                            },
                            onRemove = {
                                onRemoveFood(details.item.id)
                            }
                        )
                    }

                    HorizontalDivider()
                }
            }

            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "Resumo nutricional",
                    color = MainTexts,
                    style = MaterialTheme.typography.titleSmall
                )

                NutrientRow(
                    name = "Valor energético",
                    value = "${option.total.calorie.formatOption()} / " +
                            "${goal.calorie.formatOption()} kcal"
                )

                LinearProgressIndicator(
                    progress = { progress },
                    modifier = Modifier.fillMaxWidth(),
                    color = GreenDetails
                )

                SubText(
                    text = if (option.isWithinGoal) {
                        "Dentro da meta calórica"
                    } else {
                        "Meta calórica ainda não atingida"
                    },
                    color = if (option.isWithinGoal) {
                        GreenDetails
                    } else {
                        SecondaryTexts
                    }
                )

                NutrientRow(
                    name = "Carboidratos",
                    value = "${option.total.carb.formatOption()} / " +
                            "${goal.carb.formatOption()} g"
                )

                NutrientRow(
                    name = "Proteínas",
                    value = "${option.total.protein.formatOption()} / " +
                            "${goal.protein.formatOption()} g"
                )

                NutrientRow(
                    name = "Lipídios",
                    value = "${option.total.fat.formatOption()} / " +
                            "${goal.fat.formatOption()} g"
                )
            }

            MainButton(
                text = "Pesquisar alimentos",
                onClick = onSearchFoods,
                enabled = enabled,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }

    editingItem?.let { details ->
        FoodQuantityDialog(
            food = details.food,
            initialGrams = details.item.amountInGrams,
            isEditing = true,
            onDismiss = {
                editingItem = null
            },
            onConfirm = { newGrams ->
                onEditFood(details.item.id, newGrams)
                editingItem = null
            }
        )
    }
}

@Composable
private fun MealOptionFoodItem(
    details: MealItemDetailsResponse,
    enabled: Boolean,
    onEdit: () -> Unit,
    onRemove: () -> Unit
) {
    val macros = details.food.amountPer100g
        .calculateForPortion(details.item.amountInGrams)

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Text(
            text = details.food.description,
            color = MainTexts,
            style = MaterialTheme.typography.bodyMedium
        )

        SubText(
            text = "${details.item.amountInGrams.formatOption()} g • " +
                    "${macros.calorie.formatOption()} kcal"
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            TextButton(
                onClick = onEdit,
                enabled = enabled
            ) {
                Text("Editar", color = GreenDetails)
            }

            TextButton(
                onClick = onRemove,
                enabled = enabled
            ) {
                Text("Remover", color = Errors)
            }
        }
    }
}

@Composable
private fun NutrientRow(
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

private fun Float.formatOption(): String =
    (round(this * 10f) / 10f).toString()
