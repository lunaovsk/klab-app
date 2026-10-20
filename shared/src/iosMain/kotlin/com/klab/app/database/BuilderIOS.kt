package com.klab.app.database

import androidx.room.Room
import androidx.room.RoomDatabase
import com.klab.app.infra.database.KlabDatabase
import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSUserDomainMask

class BuilderIOS {

    fun getDatabaseBuilder (): RoomDatabase.Builder<KlabDatabase> {
        val dbFilePath = documentDirectory() + "/klab.db"
        return Room.databaseBuilder<KlabDatabase>(
            name = dbFilePath
        )
    }

    @OptIn(ExperimentalForeignApi::class)
    private fun documentDirectory (): String {
        val documentDirectory = NSFileManager.defaultManager.URLForDirectory(
            directory = NSDocumentDirectory,
            inDomain = NSUserDomainMask,
            appropriateForURL = null,
            create = true,
            error = null
        )
        return requireNotNull(documentDirectory?.path)
    }
}