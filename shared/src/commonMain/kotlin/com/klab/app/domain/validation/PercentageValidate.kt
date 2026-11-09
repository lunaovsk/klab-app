package com.klab.app.domain.validation

class PercentageValidate {

    fun validateRange(percentages: List<Int>) {
        if (percentages.any { it !in 0..100 }) {
            throw IllegalArgumentException("Os percentuais devem estar entre 0 e 100.")
        }
    }

    fun validateTotal(percentages: List<Int>) {
        if (percentages.sum() != 100) {
            throw IllegalArgumentException("A soma dos percentuais deve totalizar 100%.")
        }
    }

    fun validate(percentages: List<Int>) {
        validateRange(percentages)
        validateTotal(percentages)
    }
}