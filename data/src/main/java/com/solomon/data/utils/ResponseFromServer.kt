package com.solomon.data.utils

sealed class ResponseFromServer<out T>  {
    data class Success<out T>(val data: T) : ResponseFromServer<T>()
    data class Error<T>(val failureData: T) : ResponseFromServer<T>()
    data class Exception(val exception: Throwable) : ResponseFromServer<Nothing>()
}

sealed class Resource<T>(
    val data: T? = null,
    val error: Throwable? = null
) {
    class Success<T>(data: T) : Resource<T>(data)
    class Loading<T>(data: T? = null) : Resource<T>(data)
    class Error<T>(throwable: Throwable, data: T? = null) : Resource<T>(data, throwable)
    data class Exception(val exception: Throwable) : Resource<Nothing>()
}