package com.klab.app.domain.dto.meal

data class MealEditorData(
    val meal: MealResponse,
    val options: List<MealEditorOption>
)
