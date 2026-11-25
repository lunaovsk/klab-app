package com.klab.app.infra.mapper

import com.klab.app.domain.dto.request.MealDistribRequest
import com.klab.app.domain.dto.response.MealEditorData
import com.klab.app.domain.dto.response.MealResponse
import com.klab.app.domain.model.enumModel.MealType
import com.klab.app.domain.model.food.Macros
import com.klab.app.domain.model.meal.Meal
import com.klab.app.domain.model.meal.MealOption
import com.klab.app.infra.local.MealEntity
import com.klab.app.infra.local.relations.MealsWithOptions
import com.klab.app.infra.local.relations.OptionsWithItem

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
        options = this.options.map(OptionsWithItem::toDomain).toMutableSet()
    )
}

fun MealDistribRequest.toDomain(mealGoal: Macros): Meal {
    return Meal(
        name = this.name.trim(),
        type = this.type,
        displayOrder = this.displayOrder,
        goal = mealGoal
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

fun MealResponse.toDomain(): Meal {
    return Meal(
        id = this.id,
        name = this.name,
        type = this.type,
        displayOrder = this.displayOrder,
        goal = this.goal.toDomain()
    )
}

fun Meal.toEditorData(): MealEditorData {
    val editorOptions = this.options
        .sortedBy { it.displayOrder }
        .map { it.toEditorOption(this) }
    return MealEditorData(
        meal = this.toResponse(),
        options = editorOptions
    )
}
