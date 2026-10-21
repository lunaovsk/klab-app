package com.klab.app.infra.mapper

import com.klab.app.domain.model.meal.GramsAmount
import com.klab.app.domain.model.meal.MealItem
import com.klab.app.infra.local.MealItemEntity
import com.klab.app.infra.local.relations.ItemWithFood

fun MealItemEntity.toDomain(): MealItem {
    return MealItem(
        id = this.mealId,
        amountInGrams = GramsAmount(this.amountInGrams),
        foodId = this.foodId
    )
}

fun MealItem.toEntity(optionId: Long): MealItemEntity {
    return MealItemEntity(
        mealId = this.id,
        foodId = this.foodId,
        optionId = optionId,
        amountInGrams = this.amountInGrams.amount
    )
}

fun ItemWithFood.toDomain(): MealItem {
    return this.mealItemEntity.toDomain()
}
