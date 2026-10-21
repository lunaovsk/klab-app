package com.klab.app.infra.mapper

import com.klab.app.domain.model.meal.MealOption
import com.klab.app.infra.local.MealItemEntity
import com.klab.app.infra.local.MealOptionEntity
import com.klab.app.infra.local.relations.OptionsWithItem

fun MealOptionEntity.toDomain(): MealOption {
    return MealOption(
        id = this.optionId,
        title = this.title,
        displayOrder = this.displayOrder,
        items = mutableSetOf()
    )
}

fun MealOption.toEntity(mealId: Long): MealOptionEntity {
    return MealOptionEntity(
        optionId = this.id,
        title = this.title,
        displayOrder = this.displayOrder,
        fkMealId = mealId
    )
}

fun OptionsWithItem.toDomain(): MealOption {
    return MealOption(
        id = this.option.optionId,
        title = this.option.title,
        displayOrder = this.option.displayOrder,
        items = this.items.map(MealItemEntity::toDomain).toMutableSet()
    )
}
