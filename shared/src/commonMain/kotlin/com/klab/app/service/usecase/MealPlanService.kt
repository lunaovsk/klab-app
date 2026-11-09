package com.klab.app.service.usecase

import com.klab.app.domain.dto.MacrosDTO
import com.klab.app.domain.dto.meal.MealPlanRequest
import com.klab.app.domain.dto.meal.MealPlanResponse
import com.klab.app.domain.model.food.Macros
import com.klab.app.domain.repository.MealPlanRepository
import com.klab.app.domain.validation.DailyGoalsPlan
import com.klab.app.infra.mapper.toDTO
import com.klab.app.infra.mapper.toDomain
import com.klab.app.infra.mapper.toResponse

class MealPlanService(
    private val mealPlanRepository: MealPlanRepository,
    private val dailyGoalsValidator: DailyGoalsPlan
) {

    suspend fun create(request: MealPlanRequest): MealPlanResponse {
        dailyGoalsValidator.validate(request)
        val savedPlan = mealPlanRepository.insert(request.toDomain())
        return savedPlan.toResponse()
    }

    suspend fun findById(id: Long): MealPlanResponse {
        val plan = mealPlanRepository.findMealPlanById(id)
            ?: throw NoSuchElementException("Planejamento não encontrado com o id: $id")
        return plan.toResponse()
    }

    fun previewMacros(request: MealPlanRequest): MacrosDTO {
        dailyGoalsValidator.validateForPreview(request)
        return Macros.fromCalories(
            calories = request.calories,
            carbPercentage = request.carbosPerc,
            proteinPercentage = request.proteinPerc,
            fatPercentage = request.fatPerc
        ).toDTO()
    }
}