package com.klab.app.domain.repository

import com.klab.app.domain.model.meal.MealPlan

interface MealPlanRepository {

    suspend fun insert(mealPlan: MealPlan): Long
    suspend fun findById(id: Long): MealPlan?
    suspend fun findAll(): List<MealPlan>
    suspend fun findMealPlanById(id: Long): MealPlan?
    suspend fun update(mealPlan: MealPlan): Int
    suspend fun delete(mealPlan: MealPlan): Int
    suspend fun deleteById(id: Long): Int

}
