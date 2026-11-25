package com.klab.app.domain.dto.request

import com.klab.app.domain.model.enumModel.MealType

data class MealUpdateRequest(
    val id: Long,
    val planId: Long,
    val name: String? = null,
    val type: MealType? = null,
    val percentage: Int? = null,
    val displayOrder: Int? = null
)
