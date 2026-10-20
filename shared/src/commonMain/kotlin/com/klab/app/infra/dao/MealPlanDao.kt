package com.klab.app.infra.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.klab.app.infra.local.MealPlanEntity
import com.klab.app.infra.local.relations.PlanWithMeal

@Dao
interface MealPlanDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(mealPlanEntity: MealPlanEntity): Long

    @Query("SELECT * FROM tb_plan WHERE id = :id")
    suspend fun findById(id: Long): MealPlanEntity?

    @Query("SELECT * FROM tb_plan")
    suspend fun findAll(): List<MealPlanEntity>

    @Transaction
    @Query("SELECT * FROM tb_plan WHERE id = :id")
    suspend fun findMealPlanById(id: Long): PlanWithMeal?

    @Update
    suspend fun update(mealPlanEntity: MealPlanEntity): Int

    @Delete
    suspend fun delete(mealPlanEntity: MealPlanEntity): Int

    @Query("DELETE FROM tb_plan WHERE id = :id")
    suspend fun deleteById(id: Long): Int
}
