package com.klab.app.domain.validation

import com.klab.app.domain.dto.meal.MealPlanRequest

class DailyGoalsPlan(
    private val percentageValidator: PercentageValidate
) : MealPlanValidate<MealPlanRequest> {

    override fun validate(request: MealPlanRequest) {
        percentageValidator.validate(listOf(request.carbosPerc, request.proteinPerc, request.fatPerc))
    }

    fun validateForPreview(request: MealPlanRequest) {
        if (request.calories <= 0f) {
            throw IllegalArgumentException("Set the calorie goal.")
        }

        percentageValidator.validateRange(
            listOf(request.carbosPerc, request.proteinPerc, request.fatPerc)
        )
    }
}