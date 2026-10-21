package com.klab.app.infra.mapper

import com.klab.app.domain.model.food.Food
import com.klab.app.infra.local.FoodEntity

fun FoodEntity.toDomain(): Food {
    return Food(
        id = this.id,
        description = this.description,
        group = this.group,
        amountPer100g = this.macrosEntity.toDomain()
    )
}
