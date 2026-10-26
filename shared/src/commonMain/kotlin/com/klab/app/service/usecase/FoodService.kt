package com.klab.app.service.usecase

import androidx.paging.PagingData
import com.klab.app.domain.model.food.Food
import com.klab.app.domain.repository.FoodRepository
import com.klab.app.domain.validation.FoodValidate
import kotlinx.coroutines.flow.Flow

class FoodService(
    private val foodRepository: FoodRepository,
    private val stringValidator: FoodValidate<String>
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
        return foodRepository.findById(foodId)
            ?: throw NoSuchElementException("No food found with id $foodId")
    }

    suspend fun listAllGroups(): List<String> {
        return foodRepository.listAllGroups()
    }

}
