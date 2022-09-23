package com.solomon.lagosdevelopers.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.solomon.lagosdevelopers.model.response.DevelopersItem
import kotlinx.coroutines.flow.Flow

@Dao
interface DevelopersDao {
    // Query to fetch all the data from the database
    @Query("SELECT * FROM developersEntity")

    // Kotlin flow is an asynchronous stream of values
    fun getDevelopers(): Flow<List<DevelopersItem>>

    // Method for getting all items from the DB by id
    @Query("SELECT * FROM developersEntity ORDER BY id DESC")
    fun getAllDevelopers(): List<DevelopersItem>

    // Method for inserting items into our DB
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDevelopers(developersItem: List<DevelopersItem>)

    // Method for deleting all items from the DB
    @Query("DELETE FROM developersEntity")
    suspend fun deleteAllDevelopers()
}