package com.klab.app.domain.dto.request

data class MealItemUpdateRequest(
    val id: Long,
    val optionId: Long,
    val amountInGrams: Float
)
