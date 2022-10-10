package com.solomon.data.model.service

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.ConnectException
import java.net.SocketException
import java.net.SocketTimeoutException
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
object ServiceModule {
    private const val BASE_URL = "https://newsapi.org/v2/"
    val API_KEY = "2d021085c2e64c23927ff485d9f4299b"
    val COUNTRY_CODE = "us"

    fun createDisputeApiService(): Api {
        return getRetrofitService().create(Api::class.java)
    }

    @Provides
    @Singleton
    fun getRetrofitService(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .client(getClient())
            .build()
    }

    private fun getClient(): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        return OkHttpClient.Builder()
            .readTimeout(200, TimeUnit.SECONDS)
            .connectTimeout(200, TimeUnit.SECONDS)
            .addInterceptor(loggingInterceptor)
            .build()
    }

        @JvmStatic
        fun getUserFriendlyException(t: Throwable): Throwable {
            t.printStackTrace()
            return when (t) {
                is ConnectException ->
                    Throwable("Connection error, Check your internet connection")
                is SocketTimeoutException ->
                    Throwable("Connection taking too long. Please try again")
                is NullPointerException -> {
                    if ((t.message ?: "").contains("setCloseOnTouchOutside")) {
                        Throwable("Please Turn on your location and try again.")
                    } else {
                        Throwable("An Exception Occurred")
                    }
                }
                is SocketException -> {
                    Throwable("Connection Aborted, Check your internet connection")
                }
                else ->
                    Throwable("Unknown Error")
            }
        }
}