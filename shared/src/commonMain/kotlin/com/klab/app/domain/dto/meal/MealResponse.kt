package com.klab.app.domain.dto.meal

import com.klab.app.domain.dto.MacrosDTO
import com.klab.app.domain.model.enumModel.MealType

data class MealResponse(
    val id: Long,
    val name: String,
    val type: MealType,
    val displayOrder: Int,
    val goal: MacrosDTO,
    val options: Set<MealOptionResponse> = emptySet()
)
