package com.klab.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.compose.collectAsLazyPagingItems
import com.klab.app.domain.model.food.Food
import com.klab.app.service.usecase.FoodService
import com.klab.app.ui.components.FoodSearchField
import com.klab.app.ui.components.cards.FoodResultItem
import com.klab.app.ui.theme.*
import com.klab.app.ui.viewModel.food.FoodSearchViewModel
import org.koin.compose.koinInject

@Composable
fun FoodSearchScreen(
    onBack: () -> Unit,
    onFoodsSelected: (List<Food>) -> Unit,
    modifier: Modifier = Modifier
) {
    val foodService: FoodService = koinInject()

    val viewModel: FoodSearchViewModel = viewModel(
        factory = FoodSearchViewModel.factory(foodService)
    )

    val query by viewModel.query.collectAsState()
    val foods = viewModel.foods.collectAsLazyPagingItems()

    val selectedFoods = remember {
        mutableStateListOf<Food>()
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
            TextButton(
                onClick = onBack,
                contentPadding = PaddingValues(0.dp)
            ) {
                Text(
                    text = "← Voltar",
                    color = GreenDetails
                )
            }

            Text(
                text = "Pesquisar alimentos",
                color = MainTexts,
                style = MaterialTheme.typography.headlineSmall
            )

            Text(
                text = "Encontre os alimentos para montar sua refeição.",
                color = SecondaryTexts,
                style = MaterialTheme.typography.bodyMedium
            )
        }

        FoodSearchField(
            value = query,
            onValueChange = viewModel::onSearchChange
        )

        Text(
            text = "Resultados",
            color = MainTexts,
            style = MaterialTheme.typography.titleMedium
        )

        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            items(
                count = foods.itemCount,
                key = { index ->
                    foods[index]?.id ?: index
                }
            ) { index ->

                val food = foods[index]

                if (food != null) {
                    val isSelected = selectedFoods.any {
                        it.id == food.id
                    }

                    FoodResultItem(
                        food = food,
                        isSelected = isSelected,
                        onSelect = {
                            if (isSelected) {
                                selectedFoods.removeAll {
                                    it.id == food.id
                                }
                            } else {
                                selectedFoods.add(food)
                            }
                        }
                    )
                }
            }

            if (foods.loadState.append is androidx.paging.LoadState.Loading) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        contentAlignment = androidx.compose.ui.Alignment.Center
                    ) {
                        CircularProgressIndicator(
                            color = GreenDetails
                        )
                    }
                }
            }
        }

        Button(
            onClick = {
                onFoodsSelected(selectedFoods.toList())
            },
            enabled = selectedFoods.isNotEmpty(),
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = GreenDetails
            )
        ) {
            Text(
                text = "Adicionar ${selectedFoods.size} alimentos"
            )
        }
    }
}