package com.klab.app.infra.database

import androidx.room.RoomDatabaseConstructor

@Suppress("KotlinNoActualForExpect")
expect object  AppDatabaseConstructor : RoomDatabaseConstructor<Database> {
    override fun initialize(): Database
}