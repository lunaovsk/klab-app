package com.klab.app.infra.mapper

import com.klab.app.domain.model.enumModel.MealType
import com.klab.app.domain.model.meal.Meal
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
