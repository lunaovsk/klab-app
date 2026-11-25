package com.klab.app.domain.validation

class PercentageValidate : MealPlanValidate<List<Int>> {

    override fun validate(request: List<Int>) {
        require(request.all { it in 0..100 }) { "Os percentuais devem estar entre 0 e 100." }
        require(request.sum() == 100) { "A soma dos percentuais deve totalizar 100%." }
    }
}
