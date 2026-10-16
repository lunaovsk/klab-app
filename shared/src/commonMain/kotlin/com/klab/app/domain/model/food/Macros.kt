package com.klab.app.domain.model.food

class Macros (
    val calorie: Float,
    val carb: Float,
    val protein: Float,
    val fat: Float

) {
    fun calculateForPortion(calorie: Float, carb: Float, protein: Float, fat: Float) {
        TODO("Not yet implemented")
    }

    fun isValid(calorie: Float, carb: Float, protein: Float, fat: Float) {
        if (calorie < 0 && carb < 0 && protein < 0 && fat < 0 ) {
            throw(IllegalArgumentException("Values must be more than 0."))
        }
    }
}
