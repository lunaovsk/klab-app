package com.klab.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.klab.app.service.usecase.MealPlanService
import com.klab.app.ui.components.BackButton
import com.klab.app.ui.components.PlanPeriodSelector
import com.klab.app.ui.components.SectionTitle
import com.klab.app.ui.components.SubText
import com.klab.app.ui.components.TextInput
import com.klab.app.ui.components.cards.MealPlanCard
import com.klab.app.ui.theme.*
import com.klab.app.ui.viewModel.meal.MealPlanViewModel
import org.koin.compose.koinInject

@Composable
fun MealPlansScreen(
    onBack: () -> Unit,
    onPlanClick: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    val mealPlanService: MealPlanService = koinInject()

    val viewModel: MealPlanViewModel = viewModel(
        factory = MealPlanViewModel.factory(mealPlanService)
    )

    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadPlans()
    }

    MaterialTheme {

        Column(
            modifier = modifier
                .fillMaxSize()
                .background(BackgroundColor)
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            BackButton(
                onClick = onBack,
                enabled = !uiState.isLoading
            )

            Column(
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                SectionTitle(
                    text = "Meus planejamentos",
                    fontSize = 24.sp
                )

                SubText(
                    text = "Consulte e gerencie seus planos alimentares."
                )
            }

            TextInput(
                value = uiState.searchQuery,
                onValueChange = viewModel::onSearchChange,
                label = "Pesquisar planejamento",
                placeholder = "Nome do planejamento"
            )

            PlanPeriodSelector(
                selectedFilter = uiState.selectedFilter,
                onFilterSelected = viewModel::onFilterChange,
                modifier = Modifier.fillMaxWidth()
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                SectionTitle(
                    text = "Resultados",
                    fontSize = 18.sp
                )

                SubText(
                    text = "${uiState.filteredPlans.size} planos"
                )
            }

            when {

                uiState.isLoading -> {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(
                            color = GreenDetails
                        )
                    }
                }

                uiState.error != null -> {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        SubText(
                            text = uiState.error
                                ?: "Erro ao carregar planejamentos.",
                            color = Errors
                        )

                        TextButton(
                            onClick = viewModel::loadPlans
                        ) {
                            Text("Tentar novamente")
                        }
                    }
                }

                uiState.filteredPlans.isEmpty() -> {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        contentAlignment = Alignment.Center
                    ) {
                        SubText(
                            text = if (uiState.plans.isEmpty()) {
                                "Nenhum planejamento cadastrado."
                            } else {
                                "Nenhum planejamento encontrado."
                            }
                        )
                    }
                }

                else -> {
                    LazyColumn(
                        modifier = Modifier.weight(1f),
                        verticalArrangement =
                            Arrangement.spacedBy(12.dp)
                    ) {
                        items(
                            items = uiState.filteredPlans,
                            key = { it.id }
                        ) { plan ->

                            MealPlanCard(
                                plan = plan,
                                onClick = {
                                    onPlanClick(plan.id)
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}