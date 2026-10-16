package com.klab.app.infra.local

import androidx.room.ColumnInfo

class MacrosEntity (

    @ColumnInfo(name = "calories")val goalCalories: Float,
    @ColumnInfo(name = "carbohydrate")val goalCarbohydrate: Float,
    @ColumnInfo(name = "protein")val goalProtein: Float,
    @ColumnInfo(name = "fat")val goalFat: Float

) {
}