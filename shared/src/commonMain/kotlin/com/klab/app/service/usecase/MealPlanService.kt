package com.klab.app.service.usecase

import com.klab.app.domain.dto.meal.MealPlanRequest
import com.klab.app.domain.model.food.Macros
import com.klab.app.domain.model.meal.MealPlan
import com.klab.app.domain.repository.MealPlanRepository
import kotlinx.datetime.TimeZone
import kotlinx.datetime.todayIn
import kotlin.time.Clock

class MealPlanService (
    private val mealPlanRepository: MealPlanRepository,
) {

    suspend fun create(request: MealPlanRequest): MealPlan {
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
        require(request.calories>0)
        require(request.carbosPerc+request.proteinPerc+request.fatPerc==100)

        //cada grama de carb e protein possuem 4kcal e lipidios possuem 9kcal
        return Macros(
            calorie = request.calories,
            carb = (request.calories*request.carbosPerc/100)/4,
            protein = (request.calories*request.proteinPerc/100)/4,
            fat = (request.calories*request.fatPerc/100)/9
        )
    }
    fun previewMacros (request: MealPlanRequest): Macros{
        return calculateMacros(request)
    }

}