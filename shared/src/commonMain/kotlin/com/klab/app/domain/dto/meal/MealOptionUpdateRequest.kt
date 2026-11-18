package com.klab.app.domain.dto.meal

data class MealOptionUpdateRequest(
    val id: Long,
    val mealId: Long,
    val title: String? = null,
    val displayOrder: Int? = null
)
