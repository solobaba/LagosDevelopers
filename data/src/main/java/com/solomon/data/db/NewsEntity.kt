package com.solomon.data.db

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "news")
data class NewsEntity (
    @PrimaryKey val title: String = "",
    val author: String = "N/A",
    val description: String = "N/A",
    val imageUrl: String = "N/A",
    val content: String = "N/A",
    val fullArticleUrl: String = "N/A",
    val date: String = "N/A",
    val name: String = "N/A"
): Parcelable