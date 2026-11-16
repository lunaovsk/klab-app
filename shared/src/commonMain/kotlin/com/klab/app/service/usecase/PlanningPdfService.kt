
package com.klab.app.service.usecase

import com.klab.app.infra.pdf.PlanningPdfBuilder

class PlanningPdfService(
    private val mealPlanService: MealPlanService,
    private val mealService: MealService,
    private val mealOptionService: MealOptionService,
    private val pdfBuilder: PlanningPdfBuilder
) {

    suspend fun generate(planId: Long): ByteArray {

        val plan = mealPlanService.findById(planId)

        val meals = mealService.getMealsByPlanId(planId)

        require(meals.isNotEmpty()) {
            "O planejamento não possui refeições."
        }

        val mealDetails = meals.map { meal ->
            mealOptionService.getMealEditor(meal.id)
        }

        require(mealDetails.any { it.options.isNotEmpty() }) {
            "O planejamento ainda não possui opções salvas."
        }

        return pdfBuilder.build(
            planName = plan.title,
            dailyCalories = plan.goal.calorie,
            meals = mealDetails
        )
    }
}
