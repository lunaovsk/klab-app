package com.klab.app.domain.model.food

class Macros (
    val calorie: Float,
    val carb: Float,
    val protein: Float,
    val fat: Float

) {


    fun calculateForPortion(amount: Float): Macros {
        val value = amount / 100f
        return Macros(
            calorie = this.calorie * value,
            carb = this.carb * value,
            protein = this.protein * value,
            fat = this.fat * value
        )
    }

    fun sumNutrients(other: Macros): Macros {
        return Macros(
            calorie = this.calorie + other.calorie,
            carb = this.carb + other.carb,
            protein = this.protein + other.protein,
            fat = this.fat + other.fat
        )
    }

    fun isValid() {
        if (this.calorie < 0f || this.carb < 0f || this.protein < 0f || this.fat < 0f ) {
            throw(IllegalArgumentException("Values must be more than 0."))
        }
    }
}
