package com.klab.app.service.usecase

import com.klab.app.domain.dto.response.MealPlanResponse
import com.klab.app.domain.validation.MealPlanValidate
import com.klab.app.infra.pdf.PlanningPdfBuilder

class PlanningPdfService(
    private val mealPlanService: MealPlanService,
    private val pdfValidator: MealPlanValidate<MealPlanResponse>,
    private val pdfBuilder: PlanningPdfBuilder
) {

    suspend fun generate(planId: Long): ByteArray {
        val plan = mealPlanService.findById(planId)
        pdfValidator.validate(plan)
        return pdfBuilder.build(plan)
    }
}
