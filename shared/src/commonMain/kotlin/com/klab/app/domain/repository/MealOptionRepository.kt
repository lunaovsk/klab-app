package com.klab.app.domain.repository

import com.klab.app.domain.model.meal.MealOption

interface MealOptionRepository {

    suspend fun insert(mealOption: MealOption, mealId: Long): Long
    suspend fun findById(id: Long): MealOption?
    suspend fun findByMealId(mealId: Long): List<MealOption>
    suspend fun findOptionWithItems(id: Long): MealOption?
    suspend fun update(mealOption: MealOption, mealId: Long): Int
    suspend fun delete(mealOption: MealOption, mealId: Long): Int
    suspend fun deleteById(id: Long): Int

}
