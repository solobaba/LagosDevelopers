package com.solomon.lagosdevelopers.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "developersEntity")
data class DevelopersEntity (
    //@PrimaryKey(autoGenerate = true) val id: Int? = 0,
    @PrimaryKey val developerID: Int? = 0,
    val developerImage: String? = "N/A",
    val developerUrl: String? = "N/A",
    val developerType: String? = "N/A"
)