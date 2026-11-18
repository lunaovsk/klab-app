package com.klab.app.service.usecase

import com.klab.app.domain.dto.MacrosDTO
import com.klab.app.domain.dto.meal.MealDistribRequest
import com.klab.app.domain.dto.meal.MealResponse
import com.klab.app.domain.dto.meal.MealUpdateRequest
import com.klab.app.domain.model.food.Macros
import com.klab.app.domain.model.meal.Meal
import com.klab.app.domain.model.meal.MealPlan
import com.klab.app.domain.repository.MealPlanRepository
import com.klab.app.domain.repository.MealRepository
import com.klab.app.domain.validation.EntityValidate
import com.klab.app.domain.validation.MealPlanValidate
import com.klab.app.infra.mapper.toDTO
import com.klab.app.infra.mapper.toDomain
import com.klab.app.infra.mapper.toResponse

class MealService(
    private val mealRepository: MealRepository,
    private val mealPlanRepository: MealPlanRepository,
    private val percentageValidator: MealPlanValidate<List<Int>>,
    private val planExistsValidator: EntityValidate<MealPlan>,
    private val mealExistsValidator: EntityValidate<Meal>
) {

    suspend fun createMeals(planId: Long, requests: List<MealDistribRequest>): List<MealResponse> {
        percentageValidator.validate(requests.map { it.percentage })
        val plan = planExistsValidator.validateExists(mealPlanRepository.findById(planId), planId)

        return requests.map { request ->
            val mealGoal = calculateValidMealGoal(plan.goal, request.percentage)
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
        val plan = planExistsValidator.validateExists(mealPlanRepository.findById(planId), planId)
        return calculateValidMealGoal(plan.goal, request.percentage).toDTO()
    }

    suspend fun getMealsByPlanId(planId: Long): List<MealResponse> {
        val meals = mealRepository.findByPlanId(planId)
        return meals.map { meal ->
            mealExistsValidator.validateExists(mealRepository.findMealWithOptions(meal.id), meal.id).toResponse()
        }
    }

    suspend fun findById(id: Long): MealResponse {
        val meal = mealExistsValidator.validateExists(mealRepository.findMealWithOptions(id), id)
        return meal.toResponse()
    }

    suspend fun update(request: MealUpdateRequest): MealResponse {
        val meal = mealExistsValidator.validateExists(mealRepository.findMealWithOptions(request.id), request.id)
        val newGoal = request.percentage?.let { perc ->
            val plan = planExistsValidator.validateExists(mealPlanRepository.findById(request.planId), request.planId)
            calculateValidMealGoal(plan.goal, perc)
        }
        val updatedMeal = meal.update(
            name = request.name,
            type = request.type,
            displayOrder = request.displayOrder,
            goal = newGoal
        )
        mealRepository.update(updatedMeal, request.planId)
        return updatedMeal.toResponse()
    }

    suspend fun delete(id: Long) {
        mealExistsValidator.validateDeleted(mealRepository.deleteById(id), id)
    }

    private fun calculateValidMealGoal(planGoal: Macros, percentage: Int): Macros {
        val mealGoal = planGoal.calculateForPortion(percentage.toFloat())
        mealGoal.isValid()
        return mealGoal
    }
}
