package com.klab.app.infra.mapper

import com.klab.app.domain.dto.meal.MealPlanRequest
import com.klab.app.domain.dto.meal.MealPlanResponse
import com.klab.app.domain.model.food.Macros
import com.klab.app.domain.model.meal.Meal
import com.klab.app.domain.model.meal.MealPlan
import com.klab.app.infra.local.MealEntity
import com.klab.app.infra.local.MealPlanEntity
import com.klab.app.infra.local.relations.PlanWithMeal
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.todayIn
import kotlin.time.Clock

fun MealPlan.toEntity(): MealPlanEntity {
    return MealPlanEntity(
        id = this.id,
        title = this.title,
        createdAt = this.createdAt.toString(),
        isActive = this.isActive,
        macrosEntity = this.goal.toEntity()
    )
}

fun MealPlanEntity.toDomain(): MealPlan {
    return MealPlan(
        id = this.id,
        title = this.title,
        createdAt = LocalDate.parse(this.createdAt),
        goal = this.macrosEntity.toDomain(),
        meals = mutableSetOf(),
        isActive = this.isActive
    )
}

fun PlanWithMeal.toDomain(): MealPlan {
    return MealPlan(
        id = this.plan.id,
        title = this.plan.title,
        createdAt = LocalDate.parse(this.plan.createdAt),
        goal = this.plan.macrosEntity.toDomain(),
        meals = this.entity.map(MealEntity::toDomain).toMutableSet(),
        isActive = this.plan.isActive
    )
}

fun MealPlanRequest.toDomain(): MealPlan {
    return MealPlan(
        title = this.title.trim(),
        createdAt = Clock.System.todayIn(TimeZone.currentSystemDefault()),
        goal = Macros.fromCalories(
            calories = this.calories,
            carbPercentage = this.carbosPerc,
            proteinPercentage = this.proteinPerc,
            fatPercentage = this.fatPerc
        )
    )
}

fun MealPlan.toResponse(): MealPlanResponse {
    return MealPlanResponse(
        id = this.id,
        title = this.title,
        createdAt = this.createdAt,
        goal = this.goal.toDTO(),
        meals = this.meals.map(Meal::toResponse).toSet(),
        isActive = this.isActive
    )
}
