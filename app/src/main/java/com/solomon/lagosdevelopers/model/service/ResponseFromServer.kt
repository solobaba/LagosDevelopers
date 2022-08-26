package com.solomon.lagosdevelopers.model.service

sealed class ResponseFromServer<out T>  {
    data class Success<out T>(val data: T) : ResponseFromServer<T>()
    data class Error<T>(val failureData: T) : ResponseFromServer<T>()
    data class Exception(val exception: Throwable) : ResponseFromServer<Nothing>()
}