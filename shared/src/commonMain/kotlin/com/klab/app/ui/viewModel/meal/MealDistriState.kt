package com.klab.app.ui.viewModel.meal

import com.klab.app.domain.model.food.Macros

data class MealDistriState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val createdMeals: List<Long>? = null,
    val mealMacros: Map<Int, Macros> = emptyMap()
)