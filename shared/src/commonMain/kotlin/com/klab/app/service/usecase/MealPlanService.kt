package com.klab.app.service.usecase

import com.klab.app.domain.model.meal.MealPlan
import com.klab.app.domain.repository.MealPlanRepository

class MealPlanService (
    private val mealPlanRepository: MealPlanRepository,
) {

    suspend fun create(mealPlan: MealPlan): MealPlan {
        val saved = mealPlanRepository.insert(mealPlan);
        val plan = mealPlanRepository.findById(saved)?: throw Exception("plan not found")
        return MealPlan(plan.id, plan.title, plan.createdAt, plan.goal, plan.meals, plan.isActive)
    }

}