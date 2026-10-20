package com.klab.app.infra.database

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.util.newStringBuilder
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import com.klab.app.infra.dao.FoodDao
import com.klab.app.infra.dao.MealDao
import com.klab.app.infra.dao.MealItemDao
import com.klab.app.infra.dao.MealOptionDao
import com.klab.app.infra.dao.MealPlanDao
import com.klab.app.infra.local.FoodEntity
import com.klab.app.infra.local.MealEntity
import com.klab.app.infra.local.MealItemEntity
import com.klab.app.infra.local.MealOptionEntity
import com.klab.app.infra.local.MealPlanEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO

@Database(
    entities = [MealPlanEntity::class, MealEntity::class,
        MealOptionEntity::class, MealItemEntity::class, FoodEntity::class],
    version = 1)
@ConstructedBy(KlabDatabaseConstructor::class)
abstract class KlabDatabase : RoomDatabase() {

    abstract fun mealPlanDao(): MealPlanDao
    abstract fun mealDao(): MealDao
    abstract fun mealOptionDao(): MealOptionDao
    abstract fun mealItemDao(): MealItemDao
    abstract fun foodDao(): FoodDao

}
    fun getRoomDatabase(builder: RoomDatabase.Builder<KlabDatabase>): KlabDatabase {
        return builder
            .setDriver(BundledSQLiteDriver())
            .setQueryCoroutineContext(Dispatchers.IO)
            .build()
    }

