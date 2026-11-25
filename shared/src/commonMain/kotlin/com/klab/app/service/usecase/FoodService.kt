package com.klab.app.service.usecase

import androidx.paging.PagingData
import com.klab.app.domain.model.food.Food
import com.klab.app.domain.repository.FoodRepository
import com.klab.app.domain.validation.EntityValidate
import com.klab.app.domain.validation.FoodValidate
import kotlinx.coroutines.flow.Flow

class FoodService(
    private val foodRepository: FoodRepository,
    private val stringValidator: FoodValidate<String>,
    private val foodExistsValidator: EntityValidate<Food>
) {

    fun search(foodItens: String): Flow<PagingData<Food>> {
        val validQuery = stringValidator.validateFood(foodItens)
        return foodRepository.searchByDescription(validQuery)
    }

    fun getByGroup(groupName: String): Flow<PagingData<Food>> {
        val validGroup = stringValidator.validateFood(groupName)
        return foodRepository.findByGroup(validGroup)
    }

    suspend fun getById(foodId: Long): Food {
        return foodExistsValidator.validateExists(foodRepository.findById(foodId), foodId)
    }

    suspend fun listAllGroups(): List<String> {
        return foodRepository.listAllGroups()
    }

    fun searchDescriptionByGroup(query: String, group: String): Flow<PagingData<Food>> {
        val validQuery = stringValidator.validateFood(query)
        val validGroup = stringValidator.validateFood(group)
        return foodRepository.searchDescriptionByGroup(query = validQuery, group = validGroup)
    }

}
