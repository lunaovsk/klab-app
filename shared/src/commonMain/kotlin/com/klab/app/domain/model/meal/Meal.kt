package com.klab.app.domain.model.meal

import com.klab.app.domain.model.food.Macros
import com.klab.app.domain.model.enumModel.MealType

class Meal(
    val id: Long = 0L,
    name: String,
    type: MealType,
    displayOrder: Int,
    goal: Macros,
    val options: MutableSet<MealOption> = mutableSetOf()
) {
    var name: String = name
        private set

    var type: MealType = type
        private set

    var displayOrder: Int = displayOrder
        private set

    var goal: Macros = goal
        private set

    init {
        require(name.isNotBlank()) { "Meal name must be non-blank" }
        require(displayOrder > 0) { "Meal order must be positive" }
        require(goal.calorie >= 0) { "Meal goal must be positive" }
    }

    fun addOption(option: MealOption): Meal {
        this.options.add(option)
        return this
    }

    fun removeOption(idOption: Long): Meal {
        val removed = this.options.removeAll { it.id == idOption }
        require(removed) { "Option not found with id: $idOption" }
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

    fun getAllowedCalorieRange(optionCalories: Float): Boolean {
        val maxGoal = this.goal.calorie * 1.10f
        val minGoal = this.goal.calorie * 0.90f
        return optionCalories in minGoal..maxGoal
    }

    fun update(newName: String? = null, newType: MealType? = null, newDisplayOrder: Int? = null, newGoal: Macros? = null) {
        if (!newName.isNullOrBlank()) {
            this.name = newName.trim()
        }
        if (newType != null) {
            this.type = newType
        }
        if (newDisplayOrder != null) {
            require(newDisplayOrder > 0) { "Meal order must be positive" }
            this.displayOrder = newDisplayOrder
        }
        if (newGoal != null) {
            require(newGoal.calorie >= 0) { "Meal goal must be positive" }
            this.goal = newGoal
        }
    }
}