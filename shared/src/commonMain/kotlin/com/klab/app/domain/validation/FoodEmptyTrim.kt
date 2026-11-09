package com.klab.app.domain.validation

class FoodEmptyTrim : FoodValidate<String> {

    override fun validateFood(food: String): String {
        val sanitized = food.trim()
        require(sanitized.isNotBlank()) { "Search term must not be blank" }
        return sanitized
    }

}
