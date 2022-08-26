package com.solomon.lagosdevelopers.viewmodel

interface ViewModelHelpers {
    fun setError(throwable: Throwable)
    fun showProgress(show: Boolean, message: String = "Processing...")
}