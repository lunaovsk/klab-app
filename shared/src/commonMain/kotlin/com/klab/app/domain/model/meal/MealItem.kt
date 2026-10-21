package com.klab.app.domain.model.meal

class MealItem (
    val id: Long,
    val amountInGrams: GramsAmount,
    val foodId: Long,

    ) {
    fun defineAmountInGrams(amountInGrams: GramsAmount) {
        TODO("Not yet implemented")
    }
}