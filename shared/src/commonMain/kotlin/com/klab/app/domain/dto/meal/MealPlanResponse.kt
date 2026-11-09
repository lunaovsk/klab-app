package com.klab.app.domain.dto.meal

import com.klab.app.domain.dto.MacrosDTO
import kotlinx.datetime.LocalDate

data class MealPlanResponse(
    val id: Long,
    val title: String,
    val createdAt: LocalDate,
    val goal: MacrosDTO,
    val meals: Set<MealResponse> = emptySet(),
    val isActive: Boolean
)
