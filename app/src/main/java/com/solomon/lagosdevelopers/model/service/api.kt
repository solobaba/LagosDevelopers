package com.solomon.lagosdevelopers.model.service

import com.solomon.lagosdevelopers.model.response.NewsResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface Api {
    @GET("top-headlines")
    suspend fun getTopHeadlinesTwo(
        @Query("country") countryCode: String,
        @Query("apiKey") apiKey: String
    ): NewsResponse

    @GET("top-headlines")
    suspend fun getTopHeadlines(
        @Query("country") countryCode: String,
        @Query("apiKey") apiKey: String
    ): Response<NewsResponse>
}