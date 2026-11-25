package com.klab.app.ui.viewModel.meal

import com.klab.app.domain.dto.response.MealPlanResponse
import com.klab.app.domain.dto.response.MealResponse

data class MealPlanState(
    val meals: List<MealResponse> = emptyList(),
    val planName: String = "",
    val dailyCalories: Float = 0f,
    val isLoading: Boolean = false,
    val error: String? = null,
    val createdPlan: MealPlanResponse? = null
)