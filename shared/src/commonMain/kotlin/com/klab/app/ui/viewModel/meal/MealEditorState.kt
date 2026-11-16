package com.klab.app.ui.viewModel.meal

import com.klab.app.domain.dto.meal.MealEditorData
import com.klab.app.domain.dto.meal.MealEditorOption

data class MealEditorState(
    val isLoading: Boolean = false,
    val isSaving: Boolean = false,
    val data: MealEditorData? = null,

    val drafts: List<Pair<Long, List<Pair<Long, Float>>>> = emptyList(),

    val draftPreviews: Map<Long, MealEditorOption> = emptyMap(),
    val previewLoadingIds: Set<Long> = emptySet(),
    val error: String? = null
) {
    val canSave: Boolean
        get() = !isLoading &&
                !isSaving &&
                drafts.isNotEmpty() &&
                previewLoadingIds.isEmpty() &&
                drafts.all { (draftId, items) ->
                    items.isNotEmpty() &&
                            draftPreviews[draftId]?.isWithinGoal == true
                }
}