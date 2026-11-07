package com.klab.app.service.usecase

import com.klab.app.domain.dto.meal.MealPlanRequest
import com.klab.app.domain.model.food.Macros
import com.klab.app.domain.model.meal.MealPlan
import com.klab.app.domain.repository.MealPlanRepository
import com.klab.app.domain.validation.DailyGoalsPlan
import kotlinx.datetime.TimeZone
import kotlinx.datetime.todayIn
import kotlin.time.Clock

class MealPlanService (
    private val mealPlanRepository: MealPlanRepository,
    private val percentageValidator: DailyGoalsPlan
) {

    suspend fun create(request: MealPlanRequest): MealPlan {

        percentageValidator.validate(request)

        val goal = calculateMacros(request)
        val mealPlan = MealPlan(
            title = request.title,
            createdAt = Clock.System.todayIn(TimeZone.currentSystemDefault()),
            goal = goal
        )
        val saved = mealPlanRepository.insert(mealPlan);
        val plan = mealPlanRepository.findById(saved)?: throw Exception("plan not found")
        return MealPlan(plan.id, plan.title, plan.createdAt, plan.goal, plan.meals, plan.isActive)
    }
    private fun calculateMacros(request: MealPlanRequest): Macros {
        return Macros.fromCalories(calories = request.calories,
            carbPercentage = request.carbosPerc,
            proteinPercentage = request.proteinPerc,
            fatPercentage = request.fatPerc
        )
    }
    fun previewMacros (request: MealPlanRequest): Macros{
        percentageValidator.validateForPreview(request)
        return calculateMacros(request)
    }

}