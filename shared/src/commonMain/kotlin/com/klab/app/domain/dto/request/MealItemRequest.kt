package com.klab.app.domain.dto.request

data class MealItemRequest(
    val optionId: Long,
    val foodId: Long,
    val amountInGrams: Float
)
