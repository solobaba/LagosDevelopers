package com.solomon.lagosdevelopers.model.repository

import androidx.room.withTransaction
import com.google.gson.Gson
import com.solomon.lagosdevelopers.db.DevelopersDatabase
import com.solomon.lagosdevelopers.model.response.DevelopersItem
import com.solomon.lagosdevelopers.model.response.LagosDevelopersResponse
import com.solomon.lagosdevelopers.model.service.Api
import com.solomon.lagosdevelopers.model.service.ServiceModule
import com.solomon.lagosdevelopers.utils.Resource
import com.solomon.lagosdevelopers.utils.ResponseFromServer
import com.solomon.lagosdevelopers.utils.networkBoundResource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

class Repository @Inject constructor(
    private val api: Api,
    private val db: DevelopersDatabase
    ) {

    private val coroutineDispatcher: CoroutineDispatcher = Dispatchers.IO
    private val developersDao = db.developersDao()

    fun getDevelopers() = networkBoundResource(
        query = {
            developersDao.getDevelopers()
        },
        fetch = {
            delay(2000)
            api
        },
        saveFetchResult = { developers ->
            db.withTransaction {
                developersDao.deleteAllDevelopers()
                developersDao.insertDevelopers(emptyList())
            }
        }
    )

    fun getDevelopersInfo() = flow {
        val result = api.fetchLagosDevelopers()

        //val c = result.items.count()
        //Timber.tag("count").e(c.toString())

        //val r = result.items.toNewsList()
        //Timber.tag("result").e(Gson().toJson(r))

        //developersDao.insertDevelopers(r)
        //Timber.tag("insert").e(Gson().toJson(r))

        val devs = developersDao.getAllDevelopers()
        emit(devs)
    }.onStart {
        val devs = developersDao.getAllDevelopers()
        emit(devs)
    }

    suspend fun getLagosDevelopers(): ResponseFromServer<LagosDevelopersResponse?> {
        return withContext(coroutineDispatcher) {
            try {
                val response = ServiceModule.createDisputeApiService()
                    .fetchLagosDevelopers()
                val result = response.body()
                if (response.isSuccessful && result != null) {
                    ResponseFromServer.Success(data = result)
                } else {
                    ResponseFromServer.Error(failureData = null)
                }
            } catch (t: Throwable) {
                ResponseFromServer.Exception(exception = ServiceModule.getUserFriendlyException(t))
            }
        }
    }

    private fun List<DevelopersItem>.toNewsList() : List<DevelopersItem> {
        val mutable = mutableListOf<DevelopersItem>()
        for (it in this) {
            mutable.add(
                DevelopersItem(
                    it.id,
                    it.avatar_url.orEmpty(),
                    it.url.orEmpty(),
                    it.type.orEmpty()
//                    developerID = it.id,
//                    developerImage = it.avatar_url.orEmpty(),
//                    developerUrl = it.url.orEmpty(),
//                    developerType = it.type.orEmpty()
                )
            )
            Timber.tag("muta").e(mutable.toString())
        }
        Timber.tag("aftermuta").e("here")
        return mutable
    }
}