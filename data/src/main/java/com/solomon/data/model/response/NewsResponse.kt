package com.solomon.data.model.response

import java.io.Serializable

data class NewsResponse(
    val articles: List<NewsData> = listOf(),
    val status: String? = "N/A",
    val totalResults: Int? = 0
): Serializable

data class NewsData(
    val author: String? = "",
    val content: String? = "N/A",
    val description: String? = "N/A",
    val publishedAt: String? = "N/A",
    val source: Source,
    val title: String? = "N/A",
    val url: String? = "N/A",
    val urlToImage: String? = "N/A"
): Serializable

data class Source(
    val id: Any,
    val name: String? = "N/A"
): Serializable