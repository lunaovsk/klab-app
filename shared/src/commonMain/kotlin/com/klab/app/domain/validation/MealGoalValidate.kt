
package com.klab.app.domain.validation

import com.klab.app.domain.model.food.Macros

class MealGoalValidate {

    companion object {
        const val TOLERANCE = 0.10f
    }

    fun isWithinGoal(total: Macros, goal: Macros): Boolean {
        val minimum = goal.calorie * (1f - TOLERANCE)
        val maximum = goal.calorie * (1f + TOLERANCE)

        return total.calorie in minimum..maximum
    }

    fun validateMaximum(total: Macros, goal: Macros) {
        val maximum = goal.calorie * (1f + TOLERANCE)

        if (total.calorie > maximum) {
            throw IllegalArgumentException(
                "A opção ultrapassa o limite calórico permitido."
            )
        }
    }

    fun validateCompletedOption(total: Macros, goal: Macros) {
        if (!isWithinGoal(total, goal)) {
            throw IllegalArgumentException(
                "Todas as opções precisam estar dentro da meta."
            )
        }
    }

}
