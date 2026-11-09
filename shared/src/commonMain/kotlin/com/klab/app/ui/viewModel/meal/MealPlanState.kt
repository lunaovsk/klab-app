package com.klab.app.ui.viewModel.meal

import com.klab.app.domain.dto.meal.MealPlanResponse

data class MealPlanState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val createdPlan: MealPlanResponse? = null
)