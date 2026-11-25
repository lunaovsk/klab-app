package com.klab.app.infra.mapper

import com.klab.app.domain.dto.request.MealItemRequest
import com.klab.app.domain.dto.response.MealItemResponse
import com.klab.app.domain.dto.request.MealItemUpdateRequest
import com.klab.app.domain.model.food.Food
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
    return MealItem(
        id = this.mealItemEntity.mealId,
        amountInGrams = GramsAmount(this.mealItemEntity.amountInGrams),
        foodId = this.mealItemEntity.foodId,
        food = this.foodEntity.toDomain()
    )
}

fun MealItemRequest.toDomain(food: Food? = null): MealItem {
    return MealItem(
        amountInGrams = GramsAmount(this.amountInGrams),
        foodId = this.foodId,
        food = food
    )
}

fun MealItem.withGeneratedId(generatedId: Long): MealItem {
    return MealItem(
        id = generatedId,
        amountInGrams = this.amountInGrams,
        foodId = this.foodId,
        food = this.food
    )
}

fun MealItemUpdateRequest.toGramsAmount(): GramsAmount {
    return GramsAmount(this.amountInGrams)
}

fun MealItem.toResponse(): MealItemResponse {
    return MealItemResponse(
        id = this.id,
        amountInGrams = this.amountInGrams.amount,
        foodId = this.foodId,
        food = this.food
    )
}
