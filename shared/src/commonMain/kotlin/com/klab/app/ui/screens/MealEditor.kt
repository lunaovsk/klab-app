package com.klab.app.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.klab.app.service.usecase.MealItemService
import com.klab.app.service.usecase.MealOptionService
import com.klab.app.service.usecase.MealService
import com.klab.app.ui.components.BackButton
import com.klab.app.ui.components.FoodQuantityDialog
import com.klab.app.ui.components.MainButton
import com.klab.app.ui.components.SectionTitle
import com.klab.app.ui.components.SubText
import com.klab.app.ui.components.cards.MealGoalCard
import com.klab.app.ui.components.cards.MealOptionCard
import com.klab.app.ui.navigation.RegisterBackNavigation
import com.klab.app.ui.theme.*
import com.klab.app.ui.viewModel.meal.MealEditorViewModel
import org.koin.compose.koinInject

@Composable
fun MealEditorScreen(
    mealId: Long,
    onBack: () -> Unit,
    onSearchFoods: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    val mealService: MealService = koinInject()
    val mealOptionService: MealOptionService = koinInject()
    val mealItemService: MealItemService = koinInject()

    val viewModel: MealEditorViewModel = viewModel(
        factory = MealEditorViewModel.factory(mealService, mealOptionService, mealItemService)
    )

    val uiState by viewModel.uiState.collectAsState()
    val data = uiState.data

    var showExitDialog by remember {
        mutableStateOf(false)
    }

    val canNavigateBack = !uiState.isLoading && !uiState.isSaving

    val handleBack: () -> Unit = {

        if (canNavigateBack) {

            if (uiState.drafts.isNotEmpty()) {
                showExitDialog = true
            } else {
                onBack()
            }
        }
    }

    RegisterBackNavigation(
        onBack = handleBack
    )

    LaunchedEffect(mealId) {
        viewModel.load(mealId)
    }

    MaterialTheme {
        Column(
            modifier = modifier
                .fillMaxSize()
                .background(BackgroundColor)
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {

            BackButton(
                onClick = handleBack,
                enabled = canNavigateBack
            )

            if (uiState.isLoading && data == null) {
                CircularProgressIndicator(color = GreenDetails)
            }

            uiState.error?.let { error ->
                SubText(
                    text = error,
                    color = Errors
                )

                TextButton(
                    onClick = viewModel::clearError
                ) {
                    Text("Fechar")
                }
            }

            if (data != null) {

                SectionTitle(
                    text = data.meal.name,
                    fontSize = 24.sp
                )

                SubText(
                    text = "Monte diferentes opções e salve " +
                            "quando todas estiverem completas."
                )

                MealGoalCard(
                    goal = data.meal.goal
                )

                data.options.forEach { option ->
                    key(option.id) {
                        MealOptionCard(
                            option = option,
                            goal = data.meal.goal,
                            enabled = false,
                            onSearchFoods = {},
                            onRemoveFood = {},
                            onRemoveOption = {},
                            onEditFood = { _, _ -> }
                        )
                    }
                }

                uiState.drafts.forEachIndexed { index, draft ->

                    val draftId = draft.first
                    val items = draft.second

                    val preview = uiState.draftPreviews[draftId]
                    val isPreviewLoading =
                        draftId in uiState.previewLoadingIds

                    key(draftId) {

                        var editingFoodId by remember { mutableStateOf<Long?>(null) }

                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(
                                containerColor = Outlines
                            )
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp),
                                verticalArrangement =
                                    Arrangement.spacedBy(12.dp)
                            ) {

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement =
                                        Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = "Nova opção",
                                        style = MaterialTheme
                                            .typography.titleMedium
                                    )

                                    TextButton(
                                        onClick = { viewModel.removeDraft(draftId) },
                                        enabled = !uiState.isSaving
                                    ) {
                                        Text(text = "Excluir", color = Errors)
                                    }
                                }

                                if (isPreviewLoading) {
                                    LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
                                }

                                if (items.isEmpty()) {
                                    SubText(
                                        text = "Nenhum alimento adicionado."
                                    )
                                }

                                preview?.items?.forEach { details ->

                                    Column(
                                        verticalArrangement =
                                            Arrangement.spacedBy(4.dp)
                                    ) {
                                        Text(
                                            text = details.food.description,
                                            color = MainTexts
                                        )

                                        SubText(
                                            text = "${details.item.amountInGrams} g"
                                        )
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement =
                                                Arrangement.End
                                        ) {
                                            TextButton(
                                                onClick = {
                                                    editingFoodId =
                                                        details.item.foodId
                                                },
                                                enabled = !uiState.isSaving &&
                                                        !isPreviewLoading
                                            ) {
                                                Text("Editar", color = Button)
                                            }

                                            TextButton(
                                                onClick = {
                                                    viewModel.removeDraftFood(
                                                        mealId = mealId,
                                                        draftId = draftId,
                                                        foodId =
                                                            details.item.foodId
                                                    )
                                                },
                                                enabled = !uiState.isSaving &&
                                                        !isPreviewLoading
                                            ) {
                                                Text(
                                                    text = "Remover",
                                                    color = Errors
                                                )
                                            }
                                        }

                                    }

                                    HorizontalDivider(color=BorderLines)
                                }

                                if (preview != null && !isPreviewLoading) {

                                    Text(
                                        text = "Total: ${preview.total.calorie} kcal",
                                        style = MaterialTheme
                                            .typography.titleSmall
                                    )

                                    Text(
                                        text = if (preview.isWithinGoal) {
                                            "Opção completa"
                                        } else {
                                            "Opção fora da meta!"
                                        },
                                        color = if (preview.isWithinGoal) {
                                            GreenDetails
                                        } else {
                                            Errors
                                        }
                                    )
                                }

                                MainButton(
                                    text = "Pesquisar alimentos",
                                    onClick = {
                                        onSearchFoods(draftId)
                                    },
                                    enabled = !uiState.isSaving &&
                                            !isPreviewLoading,
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }
                        }

                        val editingDetails =
                            preview?.items?.firstOrNull {
                                it.item.foodId == editingFoodId
                            }

                        editingDetails?.let { details ->
                            FoodQuantityDialog(
                                food = details.food,
                                initialGrams =
                                    details.item.amountInGrams,
                                isEditing = true,
                                onDismiss = {
                                    editingFoodId = null
                                },
                                onConfirm = { grams ->
                                    viewModel.updateDraftFood(
                                        mealId = mealId,
                                        draftId = draftId,
                                        foodId = details.item.foodId,
                                        grams = grams
                                    )

                                    editingFoodId = null
                                }
                            )
                        }
                    }
                }

                if (uiState.isSaving) {
                    LinearProgressIndicator(
                        modifier = Modifier.fillMaxWidth(),
                        color = GreenDetails,
                        trackColor = BorderLines
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement =
                        Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedButton(
                        onClick = {
                            viewModel.addOptionDraft()
                        },
                        enabled = !uiState.isSaving &&
                                !uiState.isLoading,
                        modifier = Modifier.weight(1f).height(56.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = WhiteColor,
                            contentColor = Button
                        ),
                        border = BorderStroke(1.dp, Button)
                    ) {
                        Text("+ Adicionar")
                    }

                    MainButton(
                        text = if (uiState.isSaving) {
                            "Salvando..."
                        } else {
                            "Salvar"
                        },
                        onClick = {
                            viewModel.saveAllDrafts(mealId)
                        },
                        enabled = uiState.canSave,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }

        if (showExitDialog) {
            AlertDialog(
                onDismissRequest = {
                    showExitDialog = false
                },
                title = { Text("Opções não salvas") },
                text = {
                    Text("Existem opções em construção se você sair elas serão descartadas.")
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            showExitDialog = false
                            onBack()
                        }
                    ) {
                        Text(text="Descartar e sair", color = Errors)
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = {
                            showExitDialog = false
                        }
                    ) {
                        Text(text="Continuar editando", color = Button)
                    }
                }
            )
        }
    }
}