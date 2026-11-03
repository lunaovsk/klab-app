package com.klab.app.domain.dto.meal

data class MealPlanRequest(
    val title: String,
    val calories: Float,
    val carbosPerc: Int,
    val proteinPerc: Int,
    val fatPerc: Int
)