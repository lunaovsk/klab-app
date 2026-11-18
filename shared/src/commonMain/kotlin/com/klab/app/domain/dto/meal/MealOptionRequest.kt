package com.klab.app.domain.dto.meal

data class MealOptionRequest(
    val mealId: Long,
    val title: String,
    val displayOrder: Int
)
