package com.klab.app.domain.validation

import com.klab.app.domain.dto.meal.MealPlanRequest

class DailyGoalsPlan(
    private val percentageValidator: PercentageValidate
) : MealPlanValidate<MealPlanRequest> {

    override fun validate(request: MealPlanRequest) {
        require(request.title.trim().isNotBlank()) { "Informe o nome do planejamento." }
        require(request.calories > 0f) { "Defina uma meta calórica maior que zero." }
        percentageValidator.validate(listOf(request.carbosPerc, request.proteinPerc, request.fatPerc))
    }

    fun validateForPreview(request: MealPlanRequest) {
        require(request.calories > 0f) { "Defina uma meta calórica maior que zero." }
        percentageValidator.validateRange(
            listOf(request.carbosPerc, request.proteinPerc, request.fatPerc)
        )
    }
}