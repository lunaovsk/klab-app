package com.klab.app.infra.database

import androidx.room.RoomDatabaseConstructor

@Suppress("KotlinNoActualForExpect")
expect object  KlabDatabaseConstructor : RoomDatabaseConstructor<KlabDatabase> {
    override fun initialize(): KlabDatabase
}