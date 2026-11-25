package com.klab.app.service.usecase

import com.klab.app.domain.dto.request.MealItemRequest
import com.klab.app.domain.dto.response.MealItemResponse
import com.klab.app.domain.dto.request.MealItemUpdateRequest
import com.klab.app.domain.model.food.Food
import com.klab.app.domain.model.meal.MealItem
import com.klab.app.domain.model.meal.MealOption
import com.klab.app.domain.repository.FoodRepository
import com.klab.app.domain.repository.MealItemRepository
import com.klab.app.domain.repository.MealOptionRepository
import com.klab.app.domain.validation.EntityValidate
import com.klab.app.infra.mapper.toDomain
import com.klab.app.infra.mapper.toGramsAmount
import com.klab.app.infra.mapper.toResponse

class MealItemService(
    private val mealItemRepository: MealItemRepository,
    private val mealOptionRepository: MealOptionRepository,
    private val foodRepository: FoodRepository,
    private val optionExistsValidator: EntityValidate<MealOption>,
    private val itemExistsValidator: EntityValidate<MealItem>,
    private val foodExistsValidator: EntityValidate<Food>
) {

    suspend fun create(request: MealItemRequest): MealItemResponse {
        val option = optionExistsValidator.validateExists(mealOptionRepository.findOptionWithItems(request.optionId), request.optionId)
        val food = foodExistsValidator.validateExists(foodRepository.findById(request.foodId), request.foodId)
        val item = request.toDomain(food)
        option.addItem(item)
        val savedItem = mealItemRepository.insert(item, request.optionId)
        return savedItem.toResponse()
    }

    suspend fun findById(id: Long): MealItemResponse {
        val item = itemExistsValidator.validateExists(mealItemRepository.findById(id), id)
        return item.toResponse()
    }

    suspend fun findByOptionId(optionId: Long): List<MealItemResponse> {
        return mealItemRepository.findByOptionId(optionId).map(MealItem::toResponse)
    }

    suspend fun update(request: MealItemUpdateRequest): MealItemResponse {
        val item = itemExistsValidator.validateExists(mealItemRepository.findById(request.id), request.id)
        item.defineAmountInGrams(request.toGramsAmount())
        mealItemRepository.update(item, request.optionId)
        return item.toResponse()
    }

    suspend fun delete(optionId: Long, itemId: Long) {
        val option = optionExistsValidator.validateExists(mealOptionRepository.findOptionWithItems(optionId), optionId)
        option.removeItem(itemId)
        mealItemRepository.deleteById(itemId)
    }
}
