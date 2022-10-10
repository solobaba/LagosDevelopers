package com.solomon.data.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [NewsEntity::class], version = 4)
abstract class NewsDatabase : RoomDatabase() {
    abstract fun newsDao(): NewsDao
}

