package com.solomon.lagosdevelopers.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.solomon.lagosdevelopers.model.repository.Repository
import com.solomon.lagosdevelopers.model.response.LagosDevelopersResponse
import com.solomon.lagosdevelopers.model.service.ResponseFromServer
import com.solomon.lagosdevelopers.model.service.Service
import kotlinx.coroutines.launch
import javax.inject.Inject

class DeveloperDetailsViewModel @Inject constructor(
    ) : ViewModel() {
}