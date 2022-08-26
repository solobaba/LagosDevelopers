package com.solomon.lagosdevelopers.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.solomon.lagosdevelopers.model.repository.Repository

class ViewModelFactoryForAny(private val viewModelName: String) : ViewModelProvider.Factory {

    private val repository: Repository = Repository()

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when (viewModelName) {
            "Repository" -> DevelopersViewModel(repository)
            else -> throw Throwable("The class $viewModelName is not an Instance of viewmodel")
        } as T
    }
}