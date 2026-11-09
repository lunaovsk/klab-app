package com.klab.app.ui.viewModel.meal

import com.klab.app.domain.dto.MacrosDTO
import com.klab.app.domain.dto.meal.MealResponse

data class MealDistriState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val createdMeals: List<MealResponse>? = null,
    val mealMacros: Map<Int, MacrosDTO> = emptyMap()
)