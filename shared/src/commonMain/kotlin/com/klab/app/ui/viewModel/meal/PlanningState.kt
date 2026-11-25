package com.klab.app.ui.viewModel.meal

import com.klab.app.domain.dto.response.MealResponse

data class PlanningState(
    val isLoading: Boolean = false,
    val planName: String = "",
    val dailyCalories: Float = 0f,
    val meals: List<MealResponse> = emptyList(),
    val error: String? = null,
    val isExporting: Boolean = false,
    val exportedPdfPath: String? = null,
    val exportError: String? = null

)