package com.klab.app.infra.mapper

import com.klab.app.domain.dto.meal.MealDistribRequest
import com.klab.app.domain.dto.meal.MealResponse
import com.klab.app.domain.model.enumModel.MealType
import com.klab.app.domain.model.food.Macros
import com.klab.app.domain.model.meal.Meal
import com.klab.app.domain.model.meal.MealOption
import com.klab.app.infra.local.MealEntity
import com.klab.app.infra.local.MealOptionEntity
import com.klab.app.infra.local.relations.MealsWithOptions

fun MealEntity.toDomain(): Meal {
    return Meal(
        id = this.id,
        name = this.name,
        type = MealType.valueOf(this.type),
        displayOrder = this.order,
        goal = this.macrosEntity.toDomain(),
        options = mutableSetOf()
    )
}

fun Meal.toEntity(planId: Long): MealEntity {
    return MealEntity(
        id = this.id,
        fkPlanId = planId,
        name = this.name,
        type = this.type.name,
        order = this.displayOrder,
        macrosEntity = this.goal.toEntity()
    )
}

fun MealsWithOptions.toDomain(): Meal {
    return Meal(
        id = this.meal.id,
        name = this.meal.name,
        type = MealType.valueOf(this.meal.type),
        displayOrder = this.meal.order,
        goal = this.meal.macrosEntity.toDomain(),
        options = this.options.map(MealOptionEntity::toDomain).toMutableSet()
    )
}

fun MealDistribRequest.toDomain(planGoal: Macros): Meal {
    return Meal(
        name = this.name.trim(),
        type = this.type,
        displayOrder = this.displayOrder,
        goal = planGoal.calculateForPortion(this.percentage.toFloat())
    )
}

fun Meal.toResponse(): MealResponse {
    return MealResponse(
        id = this.id,
        name = this.name,
        type = this.type,
        displayOrder = this.displayOrder,
        goal = this.goal.toDTO(),
        options = this.options.map(MealOption::toResponse).toSet()
    )
}
