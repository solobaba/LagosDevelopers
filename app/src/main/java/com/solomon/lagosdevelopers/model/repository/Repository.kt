package com.solomon.lagosdevelopers.model.repository

import com.google.gson.Gson
import com.solomon.lagosdevelopers.db.NewsDatabase
import com.solomon.lagosdevelopers.db.NewsEntity
import com.solomon.lagosdevelopers.model.response.*
import com.solomon.lagosdevelopers.model.service.Api
import com.solomon.lagosdevelopers.model.service.ServiceModule
import com.solomon.lagosdevelopers.model.service.ServiceModule.API_KEY
import com.solomon.lagosdevelopers.model.service.ServiceModule.COUNTRY_CODE
import com.solomon.lagosdevelopers.utils.ResponseFromServer
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

class Repository @Inject constructor(
    private val api: Api,
    db: NewsDatabase
    ) {

    private val coroutineDispatcher: CoroutineDispatcher = Dispatchers.IO
    private val newsDao = db.newsDao()

//    fun getDevelopers() = networkBoundResource(
//        query = {
//            developersDao.getDevelopers()
//        },
//        fetch = {
//            delay(2000)
//            api
//        },
//        saveFetchResult = { developers ->
//            db.withTransaction {
//                developersDao.deleteAllDevelopers()
//                developersDao.insertDevelopers(emptyList())
//            }
//        }
//    )

    fun getNewsInfo() = flow {
        val result = api.getTopHeadlinesTwo(COUNTRY_CODE, API_KEY)

        val c = result.articles.count()
        Timber.tag("count").e(c.toString())

        val r = result.articles.toNewsList()
        Timber.tag("result").e(Gson().toJson(r))

        newsDao.insertNews(r)
        Timber.tag("insert").e(Gson().toJson(r))

        val news = newsDao.getAllNewsTwo()
        emit(news)
    }.onStart {
        val news = newsDao.getAllNewsTwo()
        emit(news)
    }.catch { e ->
        e.printStackTrace()
    }

    suspend fun getNews(): ResponseFromServer<NewsResponse?> {
        return withContext(coroutineDispatcher) {
            try {
                val response = ServiceModule.createDisputeApiService()
                    .getTopHeadlines(COUNTRY_CODE, API_KEY)
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

    private fun List<NewsData>.toNewsList() : List<NewsEntity> {
        val mutable = mutableListOf<NewsEntity>()
        for (it in this) {
            mutable.add(
                NewsEntity(
                    author = it.author.orEmpty(),
                    title = it.title.orEmpty(),
                    description = it.description.orEmpty(),
                    imageUrl = it.urlToImage.orEmpty(),
                    content = it.content.orEmpty(),
                    fullArticleUrl = it.url.orEmpty(),
                    date = it.publishedAt.orEmpty()
                )
            )
            Timber.tag("muta").e(mutable.toString())
        }
        Timber.tag("aftermuta").e("here")
        return mutable
    }
}