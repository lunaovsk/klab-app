package com.klab.app.infra.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey


@Entity(tableName = "tb_meal_option",
    foreignKeys = [
        ForeignKey(
            entity = MealEntity::class,
            parentColumns = ["id"],
            childColumns = ["fk_meal_id"],
            onDelete = ForeignKey.CASCADE
        )
    ])
class MealOptionEntity (

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo("option_id") val optionId: Long = 0,
    val title: String,
    @ColumnInfo("display_order") val displayOrder: Int,
    @ColumnInfo("fk_meal_id") val fkMealId: Long

    ) {
}