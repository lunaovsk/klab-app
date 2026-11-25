package com.klab.app.domain.dto.response

import com.klab.app.domain.model.food.Food

data class MealItemResponse(
    val id: Long,
    val amountInGrams: Float,
    val foodId: Long,
    val food: Food? = null
)

