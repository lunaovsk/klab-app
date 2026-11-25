package com.klab.app.domain.dto.request

data class MealOptionUpdateRequest(
    val id: Long,
    val mealId: Long,
    val title: String? = null,
    val displayOrder: Int? = null
)
