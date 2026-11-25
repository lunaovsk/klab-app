package com.klab.app.domain.dto.response

import com.klab.app.domain.dto.MacrosDTO

data class MealEditorOption(
    val id: Long,
    val title: String,
    val displayOrder: Int,
    val items: List<MealItemDetailsResponse>,
    val total: MacrosDTO,
    val isWithinGoal: Boolean
)