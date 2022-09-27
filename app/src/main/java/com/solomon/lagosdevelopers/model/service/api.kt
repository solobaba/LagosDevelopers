package com.solomon.lagosdevelopers.model.service

import com.solomon.lagosdevelopers.model.response.LagosDevelopersResponse
import retrofit2.Response
import retrofit2.http.GET

interface Api {
    @GET("search/users?q=lagos&page=1")
    suspend fun fetchLagosDevelopers(
    ): LagosDevelopersResponse
}