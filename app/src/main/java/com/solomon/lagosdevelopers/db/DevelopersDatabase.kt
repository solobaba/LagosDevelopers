package com.solomon.lagosdevelopers.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.solomon.lagosdevelopers.model.response.DevelopersItem

@Database(entities = [DevelopersItem::class], version = 3)
abstract class DevelopersDatabase : RoomDatabase() {
    abstract fun developersDao(): DevelopersDao
}

