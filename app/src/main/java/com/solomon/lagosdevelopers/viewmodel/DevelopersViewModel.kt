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

class DevelopersViewModel @Inject constructor(
    private val repository: Repository
    ) : ViewModel(), ViewModelHelpers {

    private val error = MutableLiveData<Throwable?>()
    val errorWatcher: LiveData<Throwable?>
        get() = error

    private val progressDialog = MutableLiveData<Pair<Boolean, String>>()
    val progressDialogLive: LiveData<Pair<Boolean, String>>
        get() = progressDialog

    fun getAllCardTransactions(
    ): LiveData<LagosDevelopersResponse?> {
        val getAllCardTransactions = MutableLiveData<LagosDevelopersResponse?>()
        viewModelScope.launch {
            try {
                when (val response = repository.getLagosDevelopers(
                )) {
                    is ResponseFromServer.Success -> {
                        getAllCardTransactions.value = response.data
                    }
                    is ResponseFromServer.Error -> {
                        setError(throwable = Throwable(response.failureData.toString()))
                    }
                    is ResponseFromServer.Exception -> {
                        setError(throwable = Throwable(response.exception.message))
                    }
                }
            } catch (t: Throwable) {
                setError(throwable = Service.getUserFriendlyException(t))
            }
        }
        return getAllCardTransactions
    }

    override fun setError(throwable: Throwable) {
        throwable.printStackTrace()
        error.value = throwable
    }

    override fun showProgress(show: Boolean, message: String) {
        progressDialog.value = Pair(show, message)
    }
}