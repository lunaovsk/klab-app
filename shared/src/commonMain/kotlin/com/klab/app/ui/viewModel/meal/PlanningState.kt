package com.klab.app.ui.viewModel.meal

import com.klab.app.domain.model.meal.Meal

data class PlanningState(
    val isLoading: Boolean = false,
    val meals: List<Meal> = emptyList(),
    val error: String? = null
)