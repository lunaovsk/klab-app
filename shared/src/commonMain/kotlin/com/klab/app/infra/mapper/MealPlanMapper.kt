package com.klab.app.infra.mapper

import com.klab.app.domain.model.meal.MealPlan
import com.klab.app.infra.local.MealEntity
import com.klab.app.infra.local.MealPlanEntity
import com.klab.app.infra.local.relations.PlanWithMeal
import kotlinx.datetime.LocalDate

fun MealPlan.toEntity(): MealPlanEntity {
    return MealPlanEntity(
        id = this.id,
        title = this.title,
        createdAt = this.createdAt.toString(),
        macrosEntity = this.goal.toEntity()
    )
}

fun MealPlanEntity.toDomain(): MealPlan {
    return MealPlan(
        id = this.id,
        title = this.title,
        createdAt = LocalDate.parse(this.createdAt),
        goal = this.macrosEntity.toDomain(),
        meals = mutableSetOf()
    )
}

fun PlanWithMeal.toDomain(): MealPlan {
    return MealPlan(
        id = this.plan.id,
        title = this.plan.title,
        createdAt = LocalDate.parse(this.plan.createdAt),
        goal = this.plan.macrosEntity.toDomain(),
        meals = this.entity.map(MealEntity::toDomain).toMutableSet()
    )
}
