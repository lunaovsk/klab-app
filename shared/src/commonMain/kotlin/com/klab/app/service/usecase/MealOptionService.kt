package com.klab.app.service.usecase

import com.klab.app.domain.dto.request.MealOptionRequest
import com.klab.app.domain.dto.request.MealOptionUpdateRequest
import com.klab.app.domain.dto.response.MealEditorOption
import com.klab.app.domain.dto.response.MealOptionResponse
import com.klab.app.domain.dto.response.MealResponse
import com.klab.app.domain.model.food.Food
import com.klab.app.domain.model.meal.Meal
import com.klab.app.domain.model.meal.MealOption
import com.klab.app.domain.repository.MealOptionRepository
import com.klab.app.domain.repository.MealRepository
import com.klab.app.domain.validation.EntityValidate
import com.klab.app.infra.mapper.toDomain
import com.klab.app.infra.mapper.toDraftDomain
import com.klab.app.infra.mapper.toEditorOption
import com.klab.app.infra.mapper.toResponse

class MealOptionService(
    private val optionRepository: MealOptionRepository,
    private val mealRepository: MealRepository,
    private val mealExistsValidator: EntityValidate<Meal>,
    private val optionExistsValidator: EntityValidate<MealOption>
) {

    suspend fun create(request: MealOptionRequest): MealOptionResponse {
        val meal = mealExistsValidator.validateExists(mealRepository.findMealWithOptions(request.mealId), request.mealId)
        val option = request.toDomain()
        meal.addOption(option)
        val savedOption = optionRepository.insert(option, request.mealId)
        return savedOption.toResponse()
    }

    suspend fun findById(id: Long): MealOptionResponse {
        val option = optionExistsValidator.validateExists(optionRepository.findOptionWithItems(id), id)
        return option.toResponse()
    }

    suspend fun findByMealId(mealId: Long): List<MealOptionResponse> {
        return optionRepository.findByMealId(mealId).map(MealOption::toResponse)
    }

    suspend fun update(request: MealOptionUpdateRequest): MealOptionResponse {
        val option = optionExistsValidator.validateExists(optionRepository.findOptionWithItems(request.id), request.id)
        option.update(request.title, request.displayOrder)
        optionRepository.update(option, request.mealId)
        return option.toResponse()
    }

    fun previewDraft(
        draftId: Long,
        meal: MealResponse,
        items: List<Pair<Food, Float>>
    ): MealEditorOption {
        val domainMeal = meal.toDomain()
        val draftOption = items.toDraftDomain()
        return draftOption.toEditorOption(domainMeal, overrideId = draftId)
    }

    suspend fun delete(mealId: Long, optionId: Long) {
        val meal = mealExistsValidator.validateExists(mealRepository.findMealWithOptions(mealId), mealId)
        meal.removeOption(optionId)
        optionRepository.deleteById(optionId)
    }
}
