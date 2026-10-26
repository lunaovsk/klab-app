package com.klab.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import com.klab.app.domain.model.food.Food
import com.klab.app.service.usecase.FoodService
import com.klab.app.ui.components.MainButton
import com.klab.app.ui.components.SectionTitle
import com.klab.app.ui.components.SubText
import com.klab.app.ui.theme.BackgroundColor
import com.klab.app.ui.theme.BorderLines
import com.klab.app.ui.theme.MainTexts
import com.klab.app.ui.theme.Outlines
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

@Composable
@Preview
fun SearchFoodScreen(
    foodService: FoodService? = null,
    onCreatePlan: () -> Unit = {}
) {
    var nameQuery by remember { mutableStateOf("") }
    var groupFilter by remember { mutableStateOf("") }
    var nutrientFilter by remember { mutableStateOf("") }

    val foodsFlow: Flow<PagingData<Food>> = remember(nameQuery, groupFilter, foodService) {
        when {
            foodService == null -> flowOf(PagingData.empty())
            nameQuery.trim().isNotBlank() -> foodService.search(nameQuery)
            groupFilter.trim().isNotBlank() -> foodService.getByGroup(groupFilter)
            else -> flowOf(PagingData.empty())
        }
    }

    val lazyFoodItems = foodsFlow.collectAsLazyPagingItems()

    val textFieldColors = OutlinedTextFieldDefaults.colors(
        focusedBorderColor = MainTexts,
        unfocusedBorderColor = MainTexts.copy(alpha = 0.35f),
        focusedTextColor = MainTexts,
        unfocusedTextColor = MainTexts
    )

    MaterialTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(BackgroundColor)
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                SectionTitle(
                    text = "Alimentos",
                    fontSize = 24.sp
                )

                MainButton(
                    text = "+ Criar Planejamento",
                    onClick = onCreatePlan
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedTextField(
                    value = nameQuery,
                    onValueChange = { nameQuery = it },
                    modifier = Modifier.weight(1f),
                    label = { Text("Nome") },
                    placeholder = { Text("Buscar por nome...") },
                    singleLine = true,
                    colors = textFieldColors
                )

                OutlinedTextField(
                    value = groupFilter,
                    onValueChange = { groupFilter = it },
                    modifier = Modifier.weight(1f),
                    label = { Text("Grupo") },
                    placeholder = { Text("Filtrar por grupo...") },
                    singleLine = true,
                    colors = textFieldColors
                )

                OutlinedTextField(
                    value = nutrientFilter,
                    onValueChange = { nutrientFilter = it },
                    modifier = Modifier.weight(1f),
                    label = { Text("Nutriente") },
                    placeholder = { Text("Filtrar por nutriente...") },
                    singleLine = true,
                    colors = textFieldColors
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .clip(RoundedCornerShape(12.dp))
                    .border(1.dp, BorderLines, RoundedCornerShape(12.dp))
                    .background(Color.White)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Outlines)
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Alimento",
                        modifier = Modifier.weight(2.5f),
                        fontWeight = FontWeight.SemiBold,
                        color = MainTexts,
                        fontSize = 14.sp
                    )
                    Text(
                        text = "Grupo",
                        modifier = Modifier.weight(1.5f),
                        fontWeight = FontWeight.SemiBold,
                        color = MainTexts,
                        fontSize = 14.sp
                    )
                    Text(
                        text = "Kcal",
                        modifier = Modifier.weight(1f),
                        fontWeight = FontWeight.SemiBold,
                        color = MainTexts,
                        fontSize = 14.sp
                    )
                    Text(
                        text = "Carb (g)",
                        modifier = Modifier.weight(1f),
                        fontWeight = FontWeight.SemiBold,
                        color = MainTexts,
                        fontSize = 14.sp
                    )
                    Text(
                        text = "Prot (g)",
                        modifier = Modifier.weight(1f),
                        fontWeight = FontWeight.SemiBold,
                        color = MainTexts,
                        fontSize = 14.sp
                    )
                    Text(
                        text = "Lip (g)",
                        modifier = Modifier.weight(1f),
                        fontWeight = FontWeight.SemiBold,
                        color = MainTexts,
                        fontSize = 14.sp
                    )
                }

                HorizontalDivider(color = BorderLines)

                if (lazyFoodItems.itemCount == 0) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        SubText(text = "Nenhum alimento encontrado.")
                    }
                } else {
                    LazyColumn(modifier = Modifier.fillMaxSize()) {
                        items(count = lazyFoodItems.itemCount) { index ->
                            val food = lazyFoodItems[index]
                            if (food != null) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 16.dp, vertical = 12.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = food.description,
                                        modifier = Modifier.weight(2.5f),
                                        color = MainTexts,
                                        fontSize = 14.sp
                                    )
                                    SubText(
                                        text = food.group,
                                        modifier = Modifier.weight(1.5f),
                                        fontSize = 13.sp
                                    )
                                    Text(
                                        text = "${food.amountPer100g.calorie}",
                                        modifier = Modifier.weight(1f),
                                        color = MainTexts,
                                        fontSize = 14.sp
                                    )
                                    Text(
                                        text = "${food.amountPer100g.carb}",
                                        modifier = Modifier.weight(1f),
                                        color = MainTexts,
                                        fontSize = 14.sp
                                    )
                                    Text(
                                        text = "${food.amountPer100g.protein}",
                                        modifier = Modifier.weight(1f),
                                        color = MainTexts,
                                        fontSize = 14.sp
                                    )
                                    Text(
                                        text = "${food.amountPer100g.fat}",
                                        modifier = Modifier.weight(1f),
                                        color = MainTexts,
                                        fontSize = 14.sp
                                    )
                                }
                                HorizontalDivider(color = BorderLines)
                            }
                        }
                    }
                }
            }
        }
    }
}
