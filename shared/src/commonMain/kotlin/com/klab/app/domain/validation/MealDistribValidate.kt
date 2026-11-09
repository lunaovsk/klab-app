
package com.klab.app.domain.validation

import com.klab.app.domain.dto.meal.MealDistribRequest

class MealDistributionValidate(
    private val percentageValidator: PercentageValidate
) : MealPlanValidate<List<MealDistribRequest>> {

    override fun validate(requests: List<MealDistribRequest>) {
        require(requests.isNotEmpty()) { "Adicione pelo menos uma refeição." }
        require(requests.none { it.name.isBlank() }) { "Defina o nome de todas as refeições." }
        percentageValidator.validate(requests.map { it.percentage })
    }
}