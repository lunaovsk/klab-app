package com.klab.app.domain.model.meal

import com.klab.app.domain.model.food.Macros
import kotlinx.datetime.LocalDate

class MealPlan(
    val id: Long = 0L,
    val title: String,
    val createdAt: LocalDate,
    val goal: Macros,
    val meals: MutableSet<Meal> = mutableSetOf(),
    isActive: Boolean = true
) {
    var isActive: Boolean = isActive
        private set

    init {
        require(title.isNotBlank()) { throw(IllegalArgumentException("Title must be non-blank")) }
        require(goal.calorie > 0) { throw(IllegalArgumentException("Goal must be positive")) }
    }

    fun addMeal(meal: Meal): MealPlan {
        val currentTotalCalories = this.meals.sumOf { it.goal.calorie.toDouble() }.toFloat()
        val maxGoal = this.goal.calorie * 1.10f
        require(currentTotalCalories + meal.goal.calorie <= maxGoal) {
            throw(IllegalArgumentException("The sum of the meal goals exceeds the plan's daily calorie target."))
        }
        this.meals.add(meal)
        return this
    }

    fun removeMeal(mealId: Long): MealPlan {
        val removed = this.meals.removeAll { it.id == mealId }
        require(removed) {
            throw(IllegalArgumentException("Meal not found with id: $mealId"))
        }
        return this
    }

    fun desactivate () {
        this.isActive = false
    }

    fun isGoalReached(consumed: Macros): Boolean {
        val minGoal = this.goal.calorie * 0.90f
        val maxGoal = this.goal.calorie * 1.10f
        return consumed.calorie in minGoal..maxGoal
    }

    fun update(title: String? = null, goal: Macros? = null, meals: MutableSet<Meal>? = null): MealPlan {
        return MealPlan(
            id = this.id,
            title = if (!title.isNullOrBlank()) title.trim() else this.title,
            createdAt = this.createdAt,
            goal = goal ?: this.goal,
            meals = meals ?: this.meals,
            isActive = this.isActive
        )
    }
}
