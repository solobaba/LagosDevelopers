package com.solomon.data.model.repository

import com.google.gson.Gson
import com.solomon.data.db.NewsDatabase
import com.solomon.data.db.NewsEntity
import com.solomon.data.model.response.NewsData
import com.solomon.data.model.service.Api
import com.solomon.data.model.service.ServiceModule.API_KEY
import com.solomon.data.model.service.ServiceModule.COUNTRY_CODE
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onStart
import timber.log.Timber
import javax.inject.Inject

class Repository @Inject constructor(
    private val api: Api,
    db: NewsDatabase
    ) {

    private val coroutineDispatcher: CoroutineDispatcher = Dispatchers.IO
    private val newsDao = db.newsDao()

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
                    date = it.publishedAt.orEmpty(),
                    name = it.source.name.orEmpty()
                )
            )
            Timber.tag("muta").e(mutable.toString())
        }
        Timber.tag("aftermuta").e("here")
        return mutable
    }
}