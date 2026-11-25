package com.klab.app.domain.dto.response

data class MealOptionResponse(
    val id: Long,
    val title: String,
    val displayOrder: Int,
    val items: Set<MealItemResponse> = emptySet()
)
