package com.klab.app.database

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import com.klab.app.infra.database.KlabDatabase

class BuildAndroid {

    fun getDatabaseBuilder(context: Context): RoomDatabase.Builder<KlabDatabase> {

        val appContext = context.applicationContext
        val dbFile = appContext.getDatabasePath("klab.db")
        return Room.databaseBuilder<KlabDatabase>(
            context = appContext,
            name = dbFile.absolutePath
        )
    }

}