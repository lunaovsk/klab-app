package com.klab.app.infra.mapper

import com.klab.app.domain.dto.MacrosDTO
import com.klab.app.domain.dto.meal.MealPlanRequest
import com.klab.app.domain.dto.meal.MealPlanUpdateRequest
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

fun Macros.toDTO(): MacrosDTO {
    return MacrosDTO(
        calorie = this.calorie,
        carb = this.carb,
        protein = this.protein,
        fat = this.fat
    )
}

fun MacrosDTO.toDomain(): Macros {
    return Macros(
        calorie = this.calorie,
        carb = this.carb,
        protein = this.protein,
        fat = this.fat
    )
}

fun MealPlanRequest.toMacros(): Macros {
    return Macros(
        calorie = this.calories,
        carb = (this.calories * this.carbosPerc / 100f) / 4f,
        protein = (this.calories * this.proteinPerc / 100f) / 4f,
        fat = (this.calories * this.fatPerc / 100f) / 9f
    )
}

fun MealPlanUpdateRequest.toMacrosOrNull(): Macros? {
    val cal = this.calories ?: return null
    val carb = this.carbosPerc ?: return null
    val prot = this.proteinPerc ?: return null
    val fat = this.fatPerc ?: return null
    return Macros(
        calorie = cal,
        carb = (cal * carb / 100f) / 4f,
        protein = (cal * prot / 100f) / 4f,
        fat = (cal * fat / 100f) / 9f
    )
}
