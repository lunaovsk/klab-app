package com.klab.app.infra.local

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tb_tbca")
class FoodEntity (

    @PrimaryKey val id: Long = 0,
    val description: String,
    val group: String,
    @Embedded(prefix = "food_") val macrosEntity: MacrosEntity
)
{
}