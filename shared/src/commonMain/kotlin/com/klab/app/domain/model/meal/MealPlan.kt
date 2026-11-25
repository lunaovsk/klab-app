package com.klab.app.domain.model.meal

import com.klab.app.domain.model.food.Macros
import kotlinx.datetime.LocalDate

class MealPlan(
    val id: Long = 0L,
    title: String,
    val createdAt: LocalDate,
    goal: Macros,
    val meals: MutableSet<Meal> = mutableSetOf(),
    isActive: Boolean = true
) {
    var title: String = title
        private set

    var goal: Macros = goal
        private set

    var isActive: Boolean = isActive
        private set

    init {
        require(title.isNotBlank()) { "Title must be non-blank" }
        require(goal.calorie > 0) { "Goal must be positive" }
    }

    fun addMeal(meal: Meal): MealPlan {
        val currentTotalCalories = this.meals.sumOf { it.goal.calorie.toDouble() }.toFloat()
        val maxGoal = this.goal.calorie * 1.10f
        require(currentTotalCalories + meal.goal.calorie <= maxGoal) {
            "The sum of the meal goals exceeds the plan's daily calorie target."
        }
        this.meals.add(meal)
        return this
    }

    fun removeMeal(mealId: Long): MealPlan {
        val removed = this.meals.removeAll { it.id == mealId }
        require(removed) {
            "Meal not found with id: $mealId"
        }
        return this
    }

    fun desactivate() {
        this.isActive = false
    }

    fun isGoalReached(consumed: Macros): Boolean {
        val minGoal = this.goal.calorie * 0.90f
        val maxGoal = this.goal.calorie * 1.10f
        return consumed.calorie in minGoal..maxGoal
    }

    fun update(newTitle: String? = null, newGoal: Macros? = null) {
        if (!newTitle.isNullOrBlank()) {
            this.title = newTitle.trim()
        }
        if (newGoal != null) {
            require(newGoal.calorie > 0) { "Goal must be positive" }
            this.goal = newGoal
        }
    }
}
