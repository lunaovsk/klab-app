package com.klab.app.domain.dto.response

data class MealEditorData(
    val meal: MealResponse,
    val options: List<MealEditorOption>
)
