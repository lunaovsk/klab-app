
package com.klab.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.klab.app.domain.model.food.Food
import com.klab.app.service.usecase.FoodService
import com.klab.app.ui.components.BackButton
import com.klab.app.ui.components.FoodGroupSelector
import com.klab.app.ui.components.FoodQuantityDialog
import com.klab.app.ui.components.FoodSearchField
import com.klab.app.ui.components.cards.FoodResultItem
import com.klab.app.ui.screens.enums.SearchModeFood
import com.klab.app.ui.theme.*
import com.klab.app.ui.viewModel.food.FoodSearchViewModel
import org.koin.compose.koinInject


@Composable
fun FoodSearchScreen(
    onBack: () -> Unit,
    mode: SearchModeFood = SearchModeFood.SEARCH_ONLY,
    onFoodsSelected: (List<Pair<Food, Float>>) -> Unit = {},
    modifier: Modifier = Modifier
) {
    val foodService: FoodService = koinInject()

    val viewModel: FoodSearchViewModel = viewModel(
        factory = FoodSearchViewModel.factory(foodService)
    )

    val query by viewModel.query.collectAsState()
    val foods = viewModel.foods.collectAsLazyPagingItems()
    val groups by viewModel.groups.collectAsState()
    val selectedGroup by viewModel.selectedGroup.collectAsState()

    val isSelectionMode =
        mode == SearchModeFood.SELECT_FOR_MEAL

    val selectedFoods = remember {
        mutableStateListOf<Pair<Food, Float>>()
    }

    var foodToConfigure by remember {
        mutableStateOf<Food?>(null)
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(BackgroundColor)
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        Column(
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            BackButton(
                onClick = onBack,
            )

            Text(
                text = "Pesquisar alimentos",
                color = MainTexts,
                style = MaterialTheme.typography.headlineSmall
            )

            Text(
                text = if (isSelectionMode) {
                    "Selecione os alimentos para montar sua refeição."
                } else {
                    "Consulte os alimentos e seus valores nutricionais."
                },
                color = SecondaryTexts,
                style = MaterialTheme.typography.bodyMedium
            )
        }

        FoodSearchField(
            value = query,
            onValueChange = viewModel::onSearchChange
        )
        FoodGroupSelector(
            groups = groups,
            selectedGroup = selectedGroup,
            onGroupSelected = viewModel::onGroupChange,
            modifier = Modifier.fillMaxWidth()
        )

        Text(
            text = "Resultados",
            color = MainTexts,
            style = MaterialTheme.typography.titleMedium
        )

        selectedGroup?.let { group ->
            InputChip(
                selected = true,
                onClick = {
                    viewModel.onGroupChange(null)
                },
                label = { Text(group) },
                trailingIcon = { Text("×") },
                colors = InputChipDefaults.inputChipColors(
                    selectedContainerColor = Outlines,
                    selectedLabelColor = Button,
                    selectedTrailingIconColor = Button
                )

            )
        }

        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            if (foods.loadState.refresh is LoadState.Loading) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(
                            color = GreenDetails
                        )
                    }
                }
            }

            if (foods.loadState.refresh is LoadState.Error) {
                item {
                    Column(
                        verticalArrangement =
                            Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "Erro ao pesquisar alimentos.",
                            color = Errors
                        )

                        TextButton(
                            onClick = { foods.retry() }
                        ) {
                            Text("Tentar novamente")
                        }
                    }
                }
            }

            if (
                foods.loadState.refresh is LoadState.NotLoading &&
                foods.itemCount == 0
            ) {
                item {
                    Text(
                        text = if (query.isBlank()) {
                            "Digite o nome de um alimento para pesquisar."
                        } else {
                            "Nenhum alimento encontrado."
                        },
                        color = SecondaryTexts
                    )
                }
            }

            items(
                count = foods.itemCount,
                key = { index ->
                    foods.peek(index)?.id?.toString()
                        ?: "placeholder-$index"
                }
            ) { index ->

                val food = foods[index]

                if (food != null) {

                    val isSelected = selectedFoods.any {
                        it.first.id == food.id
                    }

                    FoodResultItem(
                        food = food,
                        isSelected = isSelected,
                        showSelectionAction = isSelectionMode,
                        onSelect = {
                            if (isSelectionMode) {
                                foodToConfigure = food
                            }
                        }
                    )
                }
            }

            if (foods.loadState.append is LoadState.Loading) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(
                            color = GreenDetails
                        )
                    }
                }
            }

            if (foods.loadState.append is LoadState.Error) {
                item {
                    TextButton(
                        onClick = { foods.retry() }
                    ) {
                        Text("Erro ao carregar mais. Tentar novamente")
                    }
                }
            }
        }

        if (isSelectionMode) {
            Button(
                onClick = {
                    onFoodsSelected(selectedFoods.toList())
                },
                enabled = selectedFoods.isNotEmpty(),
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Button,
                    contentColor = WhiteColor
                )
            ) {
                Text(text = "Adicionar ${selectedFoods.size} alimento (s)")
            }
        }
    }

    if (isSelectionMode) {
        foodToConfigure?.let { food ->

            val existingSelection = selectedFoods.find {
                it.first.id == food.id
            }

            FoodQuantityDialog(
                food = food,
                initialGrams = existingSelection?.second ?: 100f,
                onDismiss = {
                    foodToConfigure = null
                },
                onConfirm = { grams ->

                    val selection = food to grams

                    val index = selectedFoods.indexOfFirst {
                        it.first.id == food.id
                    }

                    if (index >= 0) {
                        selectedFoods[index] = selection
                    } else {
                        selectedFoods.add(selection)
                    }

                    foodToConfigure = null
                }
            )
        }
    }
}
