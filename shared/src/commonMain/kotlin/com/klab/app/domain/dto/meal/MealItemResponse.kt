package com.klab.app.domain.dto.meal

data class MealItemResponse(
    val id: Long,
    val amountInGrams: Float,
    val foodId: Long
)
