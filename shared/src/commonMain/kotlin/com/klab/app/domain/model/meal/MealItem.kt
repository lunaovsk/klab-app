package com.klab.app.domain.model.meal

import com.klab.app.domain.model.food.Food
import com.klab.app.domain.model.food.Macros

class MealItem(
    val id: Long = 0L,
    amountInGrams: GramsAmount,
    val foodId: Long,
    val food: Food? = null
) {
    var amountInGrams: GramsAmount = amountInGrams
        private set

    init {
        require(foodId > 0L) { "Food id must be positive" }
    }

    fun defineAmountInGrams(newAmount: GramsAmount) {
        this.amountInGrams = newAmount
    }

    fun calculateMacros(
        foodMacros: Macros = requireNotNull(this.food) { "Food is required to calculate macros" }.amountPer100g
    ): Macros {
        return foodMacros.calculateForPortion(this.amountInGrams.amount)
    }
}
