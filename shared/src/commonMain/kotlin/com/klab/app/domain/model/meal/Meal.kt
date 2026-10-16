package com.klab.app.domain.model.meal

import com.klab.app.domain.model.food.Macros
import com.klab.app.domain.model.enumModel.MealType

class Meal (

    val id: Long,
    val name: String,
    val type: MealType,
    val displayOrder: Int,
    val goal: Macros,
    val options: MutableSet<MealOption>

) {
    init {
        require(displayOrder > 0) { throw(IllegalArgumentException("Meal order must be positive")) }
        require(goal.calorie >= 0) { throw(IllegalArgumentException("Meal order must be positive")) }
    }

    fun addOption(name: String, type: MealType): Meal {
        TODO("Not yet implemented")
    }

    fun removeOption(idOption: Long) {
        TODO("Not yet implemented")
    }

    fun getGoalDifference(goalId: Long): Macros {
        TODO("Not yet implemented")
    }

    fun calculateGoalDifference(goal: Macros): Macros {
        TODO("Not yet implemented")
    }

    fun getAllowedCalorieRange(goalMeal: Float): Boolean {
        val maxGoal = goalMeal * 1.10f; val minGoal = goalMeal * 0.90f
        return goal.calorie in minGoal..maxGoal
    }
}