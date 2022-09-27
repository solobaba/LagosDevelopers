package com.solomon.lagosdevelopers.viewmodel

import androidx.lifecycle.*
import com.google.gson.Gson
import com.solomon.lagosdevelopers.model.repository.Repository
import com.solomon.lagosdevelopers.model.response.DevelopersItem
import com.solomon.lagosdevelopers.utils.Resource
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

class DevelopersViewModel @Inject constructor(
    private val repository: Repository
    ) : ViewModel(), ViewModelHelpers {

    private val _loading = MutableStateFlow(false)
    private val _data = MutableStateFlow<Pair<List<DevelopersItem>, String?>>(Pair(emptyList(), null))
    val data = _data.asStateFlow()

    private val error = MutableLiveData<Throwable?>()
    val errorWatcher: LiveData<Throwable?>
        get() = error

    private val progressDialog = MutableLiveData<Pair<Boolean, String>>()
    val progressDialogLive: LiveData<Pair<Boolean, String>>
        get() = progressDialog

    private var _movieResponse = MutableLiveData<List<DevelopersItem>>()
    val movieResponse: LiveData<List<DevelopersItem>> = _movieResponse


    val getAllDevelopers = repository.getDevelopers().asLiveData()

//    fun getAllCardTransactions(): LiveData<LagosDevelopersResponse?> {
//        val getAllCardTransactions = MutableLiveData<LagosDevelopersResponse?>()
//        viewModelScope.launch {
//            try {
//                when (val response = repository.getLagosDevelopers(
//                )) {
//                    is ResponseFromServer.Success -> {
//                        getAllCardTransactions.value = response.data
//                    }
//                    is ResponseFromServer.Error -> {
//                        setError(throwable = Throwable(response.failureData.toString()))
//                    }
//                    is ResponseFromServer.Exception -> {
//                        setError(throwable = Throwable(response.exception.message))
//                    }
//                }
//            } catch (t: Throwable) {
//                setError(throwable = ServiceModule.getUserFriendlyException(t))
//            }
//        }
//        return getAllCardTransactions
//    }

    init {
        getAllDevelopers()
    }

    private fun getAllDevelopers() {
        viewModelScope.launch {
            repository.getDevelopersInfo().collect {
                _movieResponse.postValue(it)
                Timber.tag("DevelopersList").e(Gson().toJson(it))
            }
//                .onStart {
//                    _loading.value = true
//                }.catch { e: Throwable ->
//                    setNewsData(message = e.message ?: "Something went wrong.")
//                }.collectLatest {
//                    setNewsData(it)
//                    Timber.tag("DevelopersList").e(it.toString())
//                }
        }
    }

    private fun setNewsData(newsList: List<DevelopersItem> = emptyList(), message: String? = null) {
        _data.value = Pair(newsList, message)
        _loading.value = false
    }

    override fun setError(throwable: Throwable) {
        throwable.printStackTrace()
        error.value = throwable
    }

    override fun showProgress(show: Boolean, message: String) {
        progressDialog.value = Pair(show, message)
    }
}