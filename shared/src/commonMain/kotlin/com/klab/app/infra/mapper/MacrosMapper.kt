package com.klab.app.infra.mapper

import com.klab.app.domain.model.food.Macros
import com.klab.app.infra.local.MacrosEntity

fun Macros.toEntity(): MacrosEntity {
    return MacrosEntity(
        goalCalories = this.calorie,
        goalCarbohydrate = this.carb,
        goalProtein = this.protein,
        goalFat = this.fat
    )
}

fun MacrosEntity.toDomain(): Macros {
    return Macros(
        calorie = this.goalCalories,
        carb = this.goalCarbohydrate,
        protein = this.goalProtein,
        fat = this.goalFat
    )
}
