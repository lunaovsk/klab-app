package com.klab.app.database

import androidx.room.Room
import androidx.room.RoomDatabase
import com.klab.app.di.appDatabaseBuilder
import com.klab.app.infra.database.KlabDatabase
import java.io.File

class BuildDesktop {

    fun getDatabaseBuilder(): RoomDatabase.Builder<KlabDatabase> {
        val dbFile = File(System.getProperty("user.home"), "klab.db")
        return Room.databaseBuilder<KlabDatabase>(
            name = dbFile.absolutePath
        )
    }

    fun initDatabase() {
        appDatabaseBuilder = getDatabaseBuilder()
    }
}
