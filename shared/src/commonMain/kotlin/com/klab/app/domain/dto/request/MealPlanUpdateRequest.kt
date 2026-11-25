package com.klab.app.domain.dto.request

data class MealPlanUpdateRequest(
    val id: Long,
    val title: String? = null,
    val calories: Float? = null,
    val carbosPerc: Int? = null,
    val proteinPerc: Int? = null,
    val fatPerc: Int? = null
)
