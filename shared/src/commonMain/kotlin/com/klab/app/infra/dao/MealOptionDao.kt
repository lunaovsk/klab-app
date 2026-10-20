package com.klab.app.infra.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.klab.app.infra.local.MealOptionEntity
import com.klab.app.infra.local.relations.OptionsWithItem

@Dao
interface MealOptionDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(mealOptionEntity: MealOptionEntity): Long

    @Query("SELECT * FROM tb_meal_option WHERE option_id = :id")
    suspend fun findById(id: Long): MealOptionEntity?

    @Query("SELECT * FROM tb_meal_option WHERE fk_meal_id = :mealId")
    suspend fun findByMealId(mealId: Long): List<MealOptionEntity>

    @Transaction
    @Query("SELECT * FROM tb_meal_option WHERE option_id = :id")
    suspend fun findOptionWithItems(id: Long): OptionsWithItem?

    @Update
    suspend fun update(mealOptionEntity: MealOptionEntity): Int

    @Delete
    suspend fun delete(mealOptionEntity: MealOptionEntity): Int

    @Query("DELETE FROM tb_meal_option WHERE option_id = :id")
    suspend fun deleteById(id: Long): Int
}
