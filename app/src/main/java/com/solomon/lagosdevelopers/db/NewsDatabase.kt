package com.solomon.lagosdevelopers.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [NewsEntity::class], version = 3)
abstract class NewsDatabase : RoomDatabase() {
    abstract fun newsDao(): NewsDao
}

