package com.klab.app.ui.viewModel.meal

import com.klab.app.domain.dto.meal.MealResponse

data class PlanningState(
    val isLoading: Boolean = false,
    val meals: List<MealResponse> = emptyList(),
    val error: String? = null
)