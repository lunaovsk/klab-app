package com.klab.app.domain.dto.response

import com.klab.app.domain.model.food.Food

data class MealItemDetailsResponse(
    val item: MealItemResponse,
    val food: Food
)
