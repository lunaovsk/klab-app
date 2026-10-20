package com.klab.app.infra.dao

import androidx.room.ConstructedBy
import androidx.room.Database
import com.klab.app.infra.local.FoodEntity
import com.klab.app.infra.local.MealEntity
import com.klab.app.infra.local.MealItemEntity
import com.klab.app.infra.local.MealOptionEntity
import com.klab.app.infra.local.MealPlanEntity

@Database(
    entities = [MealPlanEntity::class, MealEntity::class,
        MealOptionEntity::class, MealItemEntity::class, FoodEntity::class],
    version = 1)
@ConstructedBy(AppDatabaseConstructor::class)
abstract class Database {

}