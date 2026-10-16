package com.klab.app.domain.model.meal

import kotlin.uuid.Uuid

class MealItem (
    val id: Long,
    val amountInGrams: GramsAmount,
    val foodId: Uuid,


    ) {
    fun defineAmountInGrams(amountInGrams: GramsAmount) {
        TODO("Not yet implemented")
    }
}