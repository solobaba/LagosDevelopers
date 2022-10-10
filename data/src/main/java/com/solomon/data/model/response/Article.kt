package com.solomon.data.model.response

import android.os.Parcelable
import com.solomon.data.db.NewsEntity
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Article(
    val author: String,
    val title: String,
    val description: String,
    val imageUrl: String,
    val content: String,
    val fullArticleUrl: String
) : Parcelable

fun NewsData.toModel(): Article =
    Article(
        author = author.orEmpty(),
        title = title.orEmpty(),
        description = description.orEmpty(),
        imageUrl = urlToImage.orEmpty(),
        content = content.orEmpty(),
        fullArticleUrl = url.orEmpty()
    )

fun List<NewsEntity>.toModel(): List<Article> =
    map{
        Article(
            author = it.author.orEmpty(),
            title = it.title,
            description = it.description.orEmpty(),
            imageUrl = it.imageUrl.orEmpty(),
            content = it.content.orEmpty(),
            fullArticleUrl = it.fullArticleUrl
        )
    }