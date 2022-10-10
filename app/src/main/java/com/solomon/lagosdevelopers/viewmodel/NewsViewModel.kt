package com.solomon.lagosdevelopers.viewmodel

import androidx.lifecycle.*
import com.solomon.data.db.NewsEntity
import com.solomon.data.model.repository.Repository
import com.solomon.data.model.response.NewsResponse
import com.solomon.data.model.service.ServiceModule
import com.solomon.data.utils.ResponseFromServer
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

class NewsViewModel @Inject constructor(
    private val repository: Repository
    ) : ViewModel(), ViewModelHelpers {

    private val _isLoading by lazy { MutableLiveData<Boolean>() }
    val isLoading: LiveData<Boolean> by lazy { _isLoading }

    private val _loading = MutableStateFlow(false)
    val loading = _loading.asStateFlow()
    private val _data = MutableStateFlow<Pair<List<NewsEntity>, String?>>(Pair(emptyList(), null))
    val data = _data.asStateFlow()

    val error = MutableLiveData<Throwable?>()
    val errorWatcher: LiveData<Throwable?>
        get() = error

    //val getAllDevelopers = repository.getDevelopers().asLiveData()

    fun getAllNews(): LiveData<NewsResponse?> {
        val getAllNewsResponse = MutableLiveData<NewsResponse?>()
        viewModelScope.launch {
            try {
                when (val response = repository.getNews(
                )) {
                    is ResponseFromServer.Success -> {
                        getAllNewsResponse.value = response.data
                    }
                    is ResponseFromServer.Error -> {
                        setError(throwable = Throwable(response.failureData.toString()))
                    }
                    is ResponseFromServer.Exception -> {
                        setError(throwable = Throwable(response.exception.message))
                    }
                }
            } catch (t: Throwable) {
                setError(throwable = ServiceModule.getUserFriendlyException(t))
            }
        }
        return getAllNewsResponse
    }

    fun getAllNewsInfo() {
        viewModelScope.launch {
            repository.getNewsInfo()
                .onStart {
                    _loading.value = true
                }.catch { e: Throwable ->
                    setNewsData(message = e.message ?: "Something went wrong.")
                }.collectLatest {
                    setNewsData(it)
                    Timber.tag("Articles").e(it.toString())
                }
        }
    }

    private fun setNewsData(newsList: List<NewsEntity> = emptyList(), message: String? = null) {
        _data.value = Pair(newsList, message)
        _loading.value = false
    }

    override fun setError(throwable: Throwable) {
        throwable.printStackTrace()
        error.value = throwable
    }

    fun setLoadingState(state: Boolean) {
        _isLoading.postValue(state)
    }
}