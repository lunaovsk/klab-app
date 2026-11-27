package com.klab.app.ui.viewModel.meal

import com.klab.app.domain.dto.response.MealPlanResponse
import com.klab.app.domain.dto.response.MealResponse
import com.klab.app.ui.screens.enums.PlanPeriodFilter

data class MealPlanState(
    val meals: List<MealResponse> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val savedPlan: MealPlanResponse? = null,
    val plans: List<MealPlanResponse> = emptyList(),
    val filteredPlans: List<MealPlanResponse> = emptyList(),
    val searchQuery: String = "",
    val selectedFilter: PlanPeriodFilter = PlanPeriodFilter.ALL,
    val currentPlanId: Long? = null,
    val planTitle: String = "",
    val caloriesInput: String = "",
    val carbohydratesInput: String = "",
    val proteinsInput: String = "",
    val lipidsInput: String = ""
)