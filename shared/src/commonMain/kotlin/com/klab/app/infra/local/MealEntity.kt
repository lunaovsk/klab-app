package com.klab.app.infra.local

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "tb_meal",
    foreignKeys = [
        ForeignKey(
            entity = MealPlanEntity::class,
            parentColumns = ["id"],
            childColumns = ["fk_plan_id"],
            onDelete = ForeignKey.CASCADE)
    ]
)
class MealEntity (

    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo("fk_plan_id") val fkPlanId: Long,
    val name: String,
    val type: String,
    val order: Int,
    @Embedded(prefix = "goal_meal_") val macrosEntity: MacrosEntity

) {
}