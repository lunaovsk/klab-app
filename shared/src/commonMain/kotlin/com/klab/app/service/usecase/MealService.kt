package com.klab.app.service.usecase

import com.klab.app.domain.dto.meal.MealDistribRequest
import com.klab.app.domain.dto.meal.MealPlanRequest
import com.klab.app.domain.model.food.Macros
import com.klab.app.domain.model.meal.Meal
import com.klab.app.domain.repository.MealPlanRepository
import com.klab.app.domain.repository.MealRepository
import com.klab.app.domain.validation.MealDistributionValidate

class MealService(
    private val mealRepository: MealRepository,
    private val mealPlanRepository: MealPlanRepository,
    private val distributionValidator: MealDistributionValidate
) {

    suspend fun createMeals(planId: Long, requests: List<MealDistribRequest>): List<Long> {

        distributionValidator.validate(requests)

        val plan = mealPlanRepository.findById(planId) ?: throw NoSuchElementException("Planejamento nao encontrado: $planId")

        return requests.map { request ->

            val meal = Meal(
                name = request.name,
                type = request.type,
                displayOrder = request.displayOrder,
                goal = plan.goal.calculateForPortion(amount = request.percentage.toFloat()),
            )

            mealRepository.add(meal, planId)
        }
    }
    suspend fun previewMacros (planId: Long, request: MealDistribRequest): Macros{
        val plan = mealPlanRepository.findById(planId) ?: throw NoSuchElementException("Planejamento nao encontrado: $planId")

        return plan.goal.calculateForPortion(amount = request.percentage.toFloat())
    }
    suspend fun getMealsByPlanId(planId: Long): List<Meal> {
        return mealRepository.findByPlanId(planId)
    }
}