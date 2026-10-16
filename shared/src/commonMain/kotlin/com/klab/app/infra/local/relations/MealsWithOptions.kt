package com.klab.app.infra.local.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.klab.app.infra.local.MealEntity
import com.klab.app.infra.local.MealOptionEntity

class MealsWithOptions (

    @Embedded
    val meal: MealEntity,
    @Relation(parentColumn = "id", entityColumn = "fk_meal_id")
    val options: List<MealOptionEntity>

) {
}