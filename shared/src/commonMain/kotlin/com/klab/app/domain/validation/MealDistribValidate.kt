
package com.klab.app.domain.validation

import com.klab.app.domain.dto.meal.MealDistribRequest

class MealDistributionValidate(
    private val percentageValidator: PercentageValidate
) : MealPlanValidate<List<MealDistribRequest>> {

    override fun validate(requests: List<MealDistribRequest>) {

        if (requests.isEmpty()) {
            throw IllegalArgumentException("Add at least one meal.")
        }

        if (requests.any { it.name.isBlank() }) {
            throw IllegalArgumentException("Define all meals.")
        }

        percentageValidator.validate(requests.map { it.percentage })
    }
}