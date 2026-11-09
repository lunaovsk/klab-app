package com.klab.app.domain.validation

interface MealPlanValidate <T> {
    fun validate(request: T)
}