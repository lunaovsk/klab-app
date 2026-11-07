package com.klab.app.domain.dto.meal

import com.klab.app.domain.model.enumModel.MealType

data class MealDistribRequest (
    val name: String,
    val type: MealType,
    val percentage: Int,
    val displayOrder: Int
)