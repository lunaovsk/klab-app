package com.klab.app.domain.dto.meal

data class MealItemUpdateRequest(
    val id: Long,
    val optionId: Long,
    val amountInGrams: Float
)
