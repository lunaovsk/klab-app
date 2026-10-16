package com.klab.app.infra.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "tb_meal_item",
    foreignKeys = [
        ForeignKey(
            entity = FoodEntity::class,
            parentColumns = ["id"],
            childColumns = ["fk_food_id"],
            onDelete = ForeignKey.RESTRICT),
        ForeignKey(
            entity = MealOptionEntity::class,
            parentColumns = ["option_id"],
            childColumns = ["fk_meal_option_id"],
            onDelete = ForeignKey.CASCADE
        )
    ])

class MealItemEntity (

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "meal_item_id") val mealId: Long  = 0,
    @ColumnInfo("fk_food_id") val foodId: Long,
    @ColumnInfo("fk_meal_option_id") val optionId: Long,
    @ColumnInfo("amount_grams") val amountInGrams: Float

) {

}