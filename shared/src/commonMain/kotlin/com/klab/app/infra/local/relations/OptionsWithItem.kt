package com.klab.app.infra.local.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.klab.app.infra.local.MealItemEntity
import com.klab.app.infra.local.MealOptionEntity

class OptionsWithItem (

    @Embedded val option: MealOptionEntity,
    @Relation(parentColumn = "option_id", entityColumn = "fk_meal_option_id")
    val items: List<MealItemEntity>

) {
}