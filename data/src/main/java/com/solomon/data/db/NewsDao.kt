package com.solomon.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface NewsDao {
    // Query to fetch all the data from the
    // SQLite database
    @Query("SELECT * FROM news")
    // Kotlin flow is an asynchronous stream of values
    fun getAllNews(): Flow<List<NewsEntity>>

    @Query("SELECT * FROM news ORDER BY date DESC")
    // Kotlin flow is an asynchronous stream of values
    suspend fun getAllNewsTwo(): List<NewsEntity>

    // If a new data is inserted with same primary key
    // It will get ignore.
    // This ensures that there is always a latest
    // data in the database
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    // The fetching of data should NOT be done on the
    // Main thread. Hence coroutine is used
    // If it is executing on one one thread, it may suspend
    // its execution there, and resume in another one
    suspend fun insertNews(news: List<NewsEntity>)

    // Once the device comes online, the cached data
    // need to be replaced, i.e. delete it
    // Again it will use coroutine to achieve this task
    @Query("DELETE FROM news")
    suspend fun deleteAllNews()
}