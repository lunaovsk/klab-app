package com.klab.app.service.usecase

import com.klab.app.domain.dto.MacrosDTO
import com.klab.app.domain.dto.meal.MealPlanRequest
import com.klab.app.domain.dto.meal.MealPlanResponse
import com.klab.app.domain.dto.meal.MealPlanUpdateRequest
import com.klab.app.domain.model.meal.MealPlan
import com.klab.app.domain.repository.MealPlanRepository
import com.klab.app.domain.validation.EntityValidate
import com.klab.app.domain.validation.MealPlanValidate
import com.klab.app.infra.mapper.toDTO
import com.klab.app.infra.mapper.toDomain
import com.klab.app.infra.mapper.toMacros
import com.klab.app.infra.mapper.toMacrosOrNull
import com.klab.app.infra.mapper.toResponse

class MealPlanService(
    private val mealPlanRepository: MealPlanRepository,
    private val percentageValidator: MealPlanValidate<List<Int>>,
    private val planExistsValidator: EntityValidate<MealPlan>
) {

    suspend fun create(request: MealPlanRequest): MealPlanResponse {
        percentageValidator.validate(listOf(request.carbosPerc, request.proteinPerc, request.fatPerc))
        val plan = request.toDomain()
        plan.goal.isValid()
        val savedPlan = mealPlanRepository.insert(plan)
        return savedPlan.toResponse()
    }

    suspend fun findById(id: Long): MealPlanResponse {
        val plan = planExistsValidator.validateExists(mealPlanRepository.findMealPlanById(id), id)
        return plan.toResponse()
    }

    suspend fun findAll(): List<MealPlanResponse> {
        return mealPlanRepository.findAll().map { it.toResponse() }
    }

    suspend fun findAllActive(): List<MealPlanResponse> {
        return mealPlanRepository.findAll()
            .filter { it.isActive }
            .map { it.toResponse() }
    }

    fun previewMacros(request: MealPlanRequest): MacrosDTO {
        val macros = request.toMacros()
        macros.isValid()
        return macros.toDTO()
    }

    suspend fun delete(id: Long) {
        val plan = planExistsValidator.validateExists(mealPlanRepository.findMealPlanById(id), id)
        plan.desactivate()
        mealPlanRepository.update(plan)
    }

    suspend fun update(request: MealPlanUpdateRequest): MealPlanResponse {
        val plan = planExistsValidator.validateExists(mealPlanRepository.findMealPlanById(request.id), request.id)
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
