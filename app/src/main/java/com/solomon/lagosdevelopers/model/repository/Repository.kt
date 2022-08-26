package com.solomon.lagosdevelopers.model.repository

import com.solomon.lagosdevelopers.model.response.LagosDevelopersResponse
import com.solomon.lagosdevelopers.model.service.ResponseFromServer
import com.solomon.lagosdevelopers.model.service.Service
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class Repository(private val coroutineDispatcher: CoroutineDispatcher = Dispatchers.IO) {
    suspend fun getLagosDevelopers(
    ): ResponseFromServer<LagosDevelopersResponse?> {
        return withContext(coroutineDispatcher) {
            try {
                val response = Service.createDisputeApiService()
                    .fetchLagosDevelopers()
                val result = response.body()
                if (response.isSuccessful && result != null) {
                    ResponseFromServer.Success(data = result)
                } else {
                    ResponseFromServer.Error(failureData = null)
                }
            } catch (t: Throwable) {
                ResponseFromServer.Exception(exception = Service.getUserFriendlyException(t))
            }
        }
    }
}