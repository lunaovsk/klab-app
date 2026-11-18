package com.klab.app.domain.dto.meal

data class MealItemRequest(
    val optionId: Long,
    val foodId: Long,
    val amountInGrams: Float
)
