package com.klab.app.domain.dto.request

data class MealOptionRequest(
    val mealId: Long,
    val title: String,
    val displayOrder: Int
)
