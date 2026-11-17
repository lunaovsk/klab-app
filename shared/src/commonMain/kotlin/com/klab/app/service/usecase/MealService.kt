package com.klab.app.service.usecase

import com.klab.app.domain.dto.MacrosDTO
import com.klab.app.domain.dto.meal.MealDistribRequest
import com.klab.app.domain.dto.meal.MealResponse
import com.klab.app.domain.model.meal.Meal
import com.klab.app.domain.repository.MealPlanRepository
import com.klab.app.domain.repository.MealRepository
import com.klab.app.domain.validation.MealPlanValidate
import com.klab.app.infra.mapper.toDTO
import com.klab.app.infra.mapper.toDomain
import com.klab.app.infra.mapper.toResponse

class MealService(
    private val mealRepository: MealRepository,
    private val mealPlanRepository: MealPlanRepository,
    private val distributionValidator: MealPlanValidate<List<MealDistribRequest>>,
    private val percentageValidator: MealPlanValidate<List<Int>>
) {

    suspend fun createMeals(planId: Long, requests: List<MealDistribRequest>): List<MealResponse> {
        distributionValidator.validate(requests)
        percentageValidator.validate(requests.map { it.percentage })
        val plan = mealPlanRepository.findById(planId)
            ?: throw NoSuchElementException("Meal plan not found with id: $planId")
        return requests.map { request ->
            val mealGoal = plan.goal.calculateForPortion(request.percentage.toFloat())
            mealGoal.isValid()
            val meal = request.toDomain(mealGoal)
            require(meal.getAllowedCalorieRange(mealGoal.calorie)) {
                throw IllegalArgumentException("Meal calories are outside the allowed +-10% range of its goal.")
            }
            plan.addMeal(meal)
            val savedMeal = mealRepository.add(meal, planId)
            savedMeal.toResponse()
        }
    }

    suspend fun previewMacros(planId: Long, request: MealDistribRequest): MacrosDTO {
        val plan = mealPlanRepository.findById(planId)
            ?: throw NoSuchElementException("Planejamento não encontrado: $planId")
        val mealGoal = plan.goal.calculateForPortion(amount = request.percentage.toFloat())
        mealGoal.isValid()
        return mealGoal.toDTO()
    }

    suspend fun getMealsByPlanId(planId: Long): List<MealResponse> {
        val meals = mealRepository.findByPlanId(planId)

        return meals.map { meal ->
            mealRepository.findMealWithOptions(meal.id)
                ?.toResponse()
                ?: throw NoSuchElementException("Refeição não encontrada.")
        }
    }
}