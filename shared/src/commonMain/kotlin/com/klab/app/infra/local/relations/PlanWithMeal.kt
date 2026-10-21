package com.klab.app.infra.local.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.klab.app.infra.local.MealEntity
import com.klab.app.infra.local.MealPlanEntity

class PlanWithMeal (

    @Embedded
    val plan: MealPlanEntity,
    @Relation(parentColumn = "id", entityColumn = "fk_plan_id")
    val entity: Set<MealEntity>

) {
}