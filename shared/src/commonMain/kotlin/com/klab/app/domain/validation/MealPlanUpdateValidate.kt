package com.klab.app.domain.validation

import com.klab.app.domain.dto.request.MealPlanUpdateRequest

class MealPlanUpdateValidate(
    private val percentageValidator: MealPlanValidate<List<Int>>
) : MealPlanValidate<MealPlanUpdateRequest> {

    override fun validate(request: MealPlanUpdateRequest) {
        val hasGoalFields = request.calories != null ||
            request.carbosPerc != null ||
            request.proteinPerc != null ||
            request.fatPerc != null

        if (!hasGoalFields) return

        requireNotNull(request.calories) { "Calories must be provided to update the goal." }
        val carb = requireNotNull(request.carbosPerc) { "Carbohydrate percentage must be provided." }
        val prot = requireNotNull(request.proteinPerc) { "Protein percentage must be provided." }
        val fat = requireNotNull(request.fatPerc) { "Fat percentage must be provided." }

        percentageValidator.validate(listOf(carb, prot, fat))
    }
}
