package com.klab.app.domain.validation

import com.klab.app.domain.dto.meal.MealPlanRequest

class DailyGoalsPlan : MealPlanValidate<MealPlanRequest> {

    override fun validate(request: MealPlanRequest) {
        require(request.title.trim().isNotBlank()) { "Informe o nome do planejamento." }
        require(request.calories > 0f) { "Defina uma meta calórica maior que zero." }
    }
}
