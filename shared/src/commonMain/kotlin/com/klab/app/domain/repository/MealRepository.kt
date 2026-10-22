package com.klab.app.domain.repository

import com.klab.app.domain.model.meal.Meal

interface MealRepository {

    suspend fun add(meal: Meal, planId: Long): Long
    suspend fun findById(id: Long): Meal?
    suspend fun findByPlanId(planId: Long): List<Meal>
    suspend fun findMealWithOptions(id: Long): Meal?
    suspend fun update(meal: Meal, planId: Long): Int
    suspend fun delete(meal: Meal, planId: Long): Int
    suspend fun deleteById(id: Long): Int

}
