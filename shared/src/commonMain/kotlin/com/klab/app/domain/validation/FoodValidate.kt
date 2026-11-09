package com.klab.app.domain.validation

interface FoodValidate<T> {
    fun validateFood(food: T): T
}
