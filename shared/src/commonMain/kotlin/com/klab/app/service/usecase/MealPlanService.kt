package com.klab.app.service.usecase

import com.klab.app.domain.dto.MacrosDTO
import com.klab.app.domain.dto.meal.MealPlanRequest
import com.klab.app.domain.dto.meal.MealPlanResponse
import com.klab.app.domain.dto.meal.MealPlanUpdateRequest
import com.klab.app.domain.repository.MealPlanRepository
import com.klab.app.domain.validation.MealPlanValidate
import com.klab.app.infra.mapper.toDTO
import com.klab.app.infra.mapper.toDomain
import com.klab.app.infra.mapper.toMacros
import com.klab.app.infra.mapper.toMacrosOrNull
import com.klab.app.infra.mapper.toResponse

class MealPlanService(
    private val mealPlanRepository: MealPlanRepository,
    private val dailyGoalsValidator: MealPlanValidate<MealPlanRequest>,
    private val percentageValidator: MealPlanValidate<List<Int>>
) {

    suspend fun create(request: MealPlanRequest): MealPlanResponse {
        dailyGoalsValidator.validate(request)
        percentageValidator.validate(listOf(request.carbosPerc, request.proteinPerc, request.fatPerc))
        val plan = request.toDomain()
        plan.goal.isValid()
        val savedPlan = mealPlanRepository.insert(plan)
        return savedPlan.toResponse()
    }

    suspend fun findById(id: Long): MealPlanResponse {
        val plan = mealPlanRepository.findMealPlanById(id)
            ?: throw NoSuchElementException("Planejamento não encontrado com o id: $id")
        return plan.toResponse()
    }

    fun previewMacros(request: MealPlanRequest): MacrosDTO {
        val macros = request.toMacros()
        macros.isValid()
        return macros.toDTO()
    }

    suspend fun delete(id: Long) {
        val plan = mealPlanRepository.findMealPlanById(id)
            ?: throw NoSuchElementException("Planejamento não encontrado com o id: $id")
        plan.desactivate()
        mealPlanRepository.update(plan)
    }

    suspend fun update(request: MealPlanUpdateRequest): MealPlanResponse {
        val plan = mealPlanRepository.findMealPlanById(request.id)
            ?: throw NoSuchElementException("Planejamento não encontrado com o id: ${request.id}")

        val newGoal = request.toMacrosOrNull()
        newGoal?.isValid()

        val updatedPlan = plan.update(
            title = request.title,
            goal = newGoal
        )

        mealPlanRepository.update(updatedPlan)
        return updatedPlan.toResponse()
    }
}