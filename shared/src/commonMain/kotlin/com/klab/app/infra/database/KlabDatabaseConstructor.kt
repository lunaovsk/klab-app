package com.klab.app.infra.database

import androidx.room.RoomDatabaseConstructor

@Suppress("KotlinNoActualForExpect")
expect object  AppDatabaseConstructor : RoomDatabaseConstructor<KlabDatabase> {
    override fun initialize(): KlabDatabase
}