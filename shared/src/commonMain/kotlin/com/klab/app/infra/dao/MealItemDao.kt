package com.klab.app.infra.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.klab.app.infra.local.MealItemEntity
import com.klab.app.infra.local.relations.ItemWithFood

@Dao
interface MealItemDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(mealItemEntity: MealItemEntity): Long

    @Query("SELECT * FROM tb_meal_item WHERE meal_item_id = :id")
    suspend fun findById(id: Long): MealItemEntity?

    @Query("SELECT * FROM tb_meal_item WHERE fk_meal_option_id = :optionId")
    suspend fun findByOptionId(optionId: Long): List<MealItemEntity>

    @Transaction
    @Query("SELECT * FROM tb_meal_item WHERE meal_item_id = :id")
    suspend fun findItemWithFood(id: Long): ItemWithFood?

    @Update
    suspend fun update(mealItemEntity: MealItemEntity): Int

    @Delete
    suspend fun delete(mealItemEntity: MealItemEntity): Int

    @Query("DELETE FROM tb_meal_item WHERE meal_item_id = :id")
    suspend fun deleteById(id: Long): Int
}
