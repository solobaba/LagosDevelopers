package com.solomon.lagosdevelopers.service

import com.google.common.truth.Truth.assertThat
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.solomon.lagosdevelopers.model.service.Api
import com.solomon.lagosdevelopers.model.service.ServiceModule.API_KEY
import com.solomon.lagosdevelopers.model.service.ServiceModule.COUNTRY_CODE
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiServiceTest {

    @Mock
    lateinit var mockWebServer: MockWebServer
    @Mock
    lateinit var apiService: Api
    lateinit var gson: Gson

    @Before
    fun setup() {
        gson = GsonBuilder().create()
        mockWebServer = MockWebServer()
        apiService = Retrofit.Builder()
            .baseUrl(mockWebServer.url("https://newsapi.org/v2/"))
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(Api::class.java)
    }

    @After
    fun deconstruct() {
        mockWebServer.shutdown()
    }

    @Test
    fun validateNewsData_return_success() {
        runBlocking {
            val mockResponse = MockResponse()
            mockWebServer.enqueue(
                mockResponse.setBody("{ }")
            )
            val response = apiService.getTopHeadlines(COUNTRY_CODE, API_KEY)
            val request = mockWebServer.takeRequest()

            assertThat(request.path).isEqualTo("top-headlines")
            assertThat(response).isNotNull()
        }
    }
}