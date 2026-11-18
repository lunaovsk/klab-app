package com.klab.app.domain.model.meal

import com.klab.app.domain.model.food.Macros
import com.klab.app.domain.model.enumModel.MealType

class Meal (

    val id: Long = 0L,
    val name: String,
    val type: MealType,
    val displayOrder: Int,
    val goal: Macros,
    val options: MutableSet<MealOption> = mutableSetOf()

) {
    init {
        require(name.isNotBlank()) { throw(IllegalArgumentException("Meal name must be non-blank")) }
        require(displayOrder > 0) { throw(IllegalArgumentException("Meal order must be positive")) }
        require(goal.calorie >= 0) { throw(IllegalArgumentException("Meal goal must be positive")) }
    }

    fun addOption(option: MealOption): Meal {
        this.options.add(option)
        return this
    }

    fun removeOption(idOption: Long): Meal {
        val removed = this.options.removeAll { it.id == idOption }
        require(removed) { throw IllegalArgumentException("Option not found with id: $idOption") }
        return this
    }

    fun calculateGoalDifference(consumed: Macros): Macros {
        return Macros(
            calorie = (this.goal.calorie - consumed.calorie).coerceAtLeast(0f),
            carb = (this.goal.carb - consumed.carb).coerceAtLeast(0f),
            protein = (this.goal.protein - consumed.protein).coerceAtLeast(0f),
            fat = (this.goal.fat - consumed.fat).coerceAtLeast(0f)
        )
    }

    fun getAllowedCalorieRange(goalMeal: Float): Boolean {
        val maxGoal = goalMeal * 1.10f; val minGoal = goalMeal * 0.90f
        return goal.calorie in minGoal..maxGoal
    }

    fun update(name: String? = null, type: MealType? = null, displayOrder: Int? = null, goal: Macros? = null, options: MutableSet<MealOption>? = null): Meal {
        return Meal(
            id = this.id,
            name = if (!name.isNullOrBlank()) name.trim() else this.name,
            type = type ?: this.type,
            displayOrder = displayOrder ?: this.displayOrder,
            goal = goal ?: this.goal,
            options = options ?: this.options
        )
    }
}