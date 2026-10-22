package com.klab.app.infra.local

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tb_plan")
class MealPlanEntity (

    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    @ColumnInfo(name = "created_at") val createdAt: String,
    @ColumnInfo(name = "is_active") val isActive: Boolean = true,
    @Embedded(prefix = "goal_") val macrosEntity: MacrosEntity

)
