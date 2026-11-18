package com.klab.app.domain.model.meal

import com.klab.app.domain.model.food.Macros

class MealItem(
    val id: Long = 0L,
    val amountInGrams: GramsAmount,
    val foodId: Long
) {
    init {
        require(foodId > 0L) {
            throw IllegalArgumentException("Food id must be positive")
        }
    }

    fun defineAmountInGrams(amountInGrams: GramsAmount): MealItem {
        return MealItem(
            id = this.id,
            amountInGrams = amountInGrams,
            foodId = this.foodId
        )
    }

    fun calculateMacros(foodMacros: Macros): Macros {
        return foodMacros.calculateForPortion(this.amountInGrams.amount)
    }
}
