package com.klab.app.service.usecase

import com.klab.app.domain.dto.MacrosDTO
import com.klab.app.domain.dto.meal.MealDistribRequest
import com.klab.app.domain.dto.meal.MealResponse
import com.klab.app.domain.model.meal.Meal
import com.klab.app.domain.repository.MealPlanRepository
import com.klab.app.domain.repository.MealRepository
import com.klab.app.domain.validation.MealDistributionValidate
import com.klab.app.infra.mapper.toDTO
import com.klab.app.infra.mapper.toDomain
import com.klab.app.infra.mapper.toResponse

class MealService(
    private val mealRepository: MealRepository,
    private val mealPlanRepository: MealPlanRepository,
    private val distributionValidator: MealDistributionValidate
) {

    suspend fun createMeals(planId: Long, requests: List<MealDistribRequest>): List<MealResponse> {
        distributionValidator.validate(requests)

        val plan = mealPlanRepository.findById(planId)
            ?: throw NoSuchElementException("Planejamento não encontrado: $planId")

        return requests.map { request ->
            val savedMeal = mealRepository.add(request.toDomain(plan.goal), planId)
            savedMeal.toResponse()
        }
    }

    suspend fun previewMacros(planId: Long, request: MealDistribRequest): MacrosDTO {
        val plan = mealPlanRepository.findById(planId)
            ?: throw NoSuchElementException("Planejamento não encontrado: $planId")

        return plan.goal.calculateForPortion(amount = request.percentage.toFloat()).toDTO()
    }

    suspend fun getMealsByPlanId(planId: Long): List<MealResponse> {
        return mealRepository.findByPlanId(planId).map(Meal::toResponse)
    }
}