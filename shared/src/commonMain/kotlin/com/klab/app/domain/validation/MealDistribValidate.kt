package com.klab.app.domain.validation

import com.klab.app.domain.dto.meal.MealDistribRequest

class MealDistribValidate : MealPlanValidate<List<MealDistribRequest>> {

    override fun validate(request: List<MealDistribRequest>) {
        require(request.isNotEmpty()) { "Adicione pelo menos uma refeição." }
        require(request.none { it.name.trim().isBlank() }) { "Defina o nome de todas as refeições." }
    }
}

