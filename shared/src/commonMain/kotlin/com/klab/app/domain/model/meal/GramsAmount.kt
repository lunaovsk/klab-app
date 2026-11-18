package com.klab.app.domain.model.meal

class GramsAmount(
    val amount: Float
) {
    init {
        require(amount.isFinite() && amount > 0f) {
            throw IllegalArgumentException("Grams amount must be positive")
        }
    }
}

