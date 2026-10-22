package com.klab.app.domain.repository

import com.klab.app.domain.model.meal.MealItem

interface MealItemRepository {

    suspend fun insert(mealItem: MealItem, optionId: Long): Long
    suspend fun findById(id: Long): MealItem?
    suspend fun findByOptionId(optionId: Long): List<MealItem>
    suspend fun findItemWithFood(id: Long): MealItem?
    suspend fun update(mealItem: MealItem, optionId: Long): Int
    suspend fun delete(mealItem: MealItem, optionId: Long): Int
    suspend fun deleteById(id: Long): Int

}
