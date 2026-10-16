package com.klab.app.infra.local.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.klab.app.infra.local.FoodEntity
import com.klab.app.infra.local.MealItemEntity

class ItemWithFood (

    @Embedded
    val mealItemEntity: MealItemEntity,
    @Relation(parentColumn = "fk_food_id", entityColumn = "id")
    val foodEntity: FoodEntity


    ) {
}