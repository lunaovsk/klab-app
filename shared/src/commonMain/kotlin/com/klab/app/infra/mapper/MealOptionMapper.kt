package com.klab.app.infra.mapper

import com.klab.app.domain.dto.request.MealOptionRequest
import com.klab.app.domain.dto.response.MealEditorOption
import com.klab.app.domain.dto.response.MealItemDetailsResponse
import com.klab.app.domain.dto.response.MealOptionResponse
import com.klab.app.domain.model.food.Food
import com.klab.app.domain.model.meal.GramsAmount
import com.klab.app.domain.model.meal.Meal
import com.klab.app.domain.model.meal.MealItem
import com.klab.app.domain.model.meal.MealOption
import com.klab.app.infra.local.MealOptionEntity
import com.klab.app.infra.local.relations.ItemWithFood
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
        items = this.items.map(ItemWithFood::toDomain).toMutableSet()
    )
}

fun MealOptionRequest.toDomain(): MealOption {
    return MealOption(
        title = this.title.trim(),
        displayOrder = this.displayOrder
    )
}

fun MealOption.toResponse(): MealOptionResponse {
    return MealOptionResponse(
        id = this.id,
        title = this.title,
        displayOrder = this.displayOrder,
        items = this.items.map(MealItem::toResponse).toSet()
    )
}

fun List<Pair<Food, Float>>.toDraftDomain(): MealOption {
    val draftItems = this.map { (food, grams) ->
        MealItem(
            amountInGrams = GramsAmount(grams),
            foodId = food.id,
            food = food
        )
    }.toMutableSet()

    return MealOption(
        title = "Rascunho",
        displayOrder = 1,
        items = draftItems
    )
}

fun MealOption.toEditorOption(meal: Meal, overrideId: Long = this.id): MealEditorOption {
    val details = this.items.map { item ->
        val food = requireNotNull(item.food) { "Food not found for meal item ${item.id}." }
        MealItemDetailsResponse(
            item = item.toResponse(),
            food = food
        )
    }
    val total = this.calculateMacros()
    return MealEditorOption(
        id = overrideId,
        title = this.title,
        displayOrder = this.displayOrder,
        items = details,
        total = total.toDTO(),
        isWithinGoal = this.items.isNotEmpty() && meal.getAllowedCalorieRange(total.calorie)
    )
}
