package com.klab.app.domain.validation

import com.klab.app.domain.dto.response.MealPlanResponse

class MealPlanPdfValidate : MealPlanValidate<MealPlanResponse> {

    override fun validate(request: MealPlanResponse) {
        require(request.meals.isNotEmpty()) { "O planejamento não possui refeições." }
        require(request.meals.any { it.options.isNotEmpty() }) {
            "O planejamento ainda não possui opções salvas."
        }
    }
}
