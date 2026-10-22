package com.klab.app.domain.model.meal

import com.klab.app.domain.model.food.Macros
import kotlinx.datetime.LocalDate

class MealPlan(
    val id: Long,
    val title: String,
    val createdAt: LocalDate,
    val goal: Macros,
    val meals: MutableSet<Meal>,
    isActive: Boolean = true
) {
    var isActive: Boolean = isActive
        private set

    init {
        require(title.isNotBlank()) { throw(IllegalArgumentException("Title must be non-blank")) }
        require(goal.calorie > 0) { throw(IllegalArgumentException("Goal must be positive")) }
    }

    fun addMeal (type: String, dailyGoalPercentage: Float): MealPlan {
        TODO("Not yet implemented")
    }

    fun removeMeal (mealId: Long): MealPlan {
        TODO("Not yet implemented")
    }

    fun desactivate () {
        this.isActive = false
    }

    fun calculateDailyTotal(selectedOptions: Map<Long, Long>): Macros {
        TODO("Not yet implemented")
    }

    fun isGoalReached(consumed: Macros): Boolean {
        TODO("Not yet implemented")
    }
}
