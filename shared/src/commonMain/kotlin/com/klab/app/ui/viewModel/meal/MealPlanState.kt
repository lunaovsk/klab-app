package com.klab.app.ui.viewModel.meal

import com.klab.app.domain.model.meal.MealPlan

data class MealPlanState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val createdPlan: MealPlan? = null
)