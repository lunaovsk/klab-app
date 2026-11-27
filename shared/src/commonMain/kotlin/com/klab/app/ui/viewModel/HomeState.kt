package com.klab.app.ui.viewModel

import com.klab.app.domain.dto.response.MealPlanResponse

data class HomeState(
    val plans: List<MealPlanResponse> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)