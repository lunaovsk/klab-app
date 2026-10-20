package com.klab.app.infra.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.klab.app.infra.local.MealEntity
import com.klab.app.infra.local.relations.MealsWithOptions

@Dao
interface MealDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun add(mealEntity: MealEntity): Long

    @Query("SELECT * FROM tb_meal WHERE id = :id")
    suspend fun findById(id: Long): MealEntity?

    @Query("SELECT * FROM tb_meal WHERE fk_plan_id = :planId")
    suspend fun findByPlanId(planId: Long): List<MealEntity>

    @Transaction
    @Query("SELECT * FROM tb_meal WHERE id = :id")
    suspend fun findMealWithOptions(id: Long): MealsWithOptions?

    @Update
    suspend fun update(mealEntity: MealEntity): Int

    @Delete
    suspend fun delete(mealEntity: MealEntity): Int

    @Query("DELETE FROM tb_meal WHERE id = :id")
    suspend fun deleteById(id: Long): Int
}
