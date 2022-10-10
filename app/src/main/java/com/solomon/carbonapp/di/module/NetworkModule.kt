package com.solomon.carbonapp.di.module

import androidx.room.Room
import com.solomon.carbonapp.App
import com.solomon.data.db.NewsDatabase
import com.solomon.data.model.repository.Repository
import com.solomon.data.model.service.Api
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.Dispatchers
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
class NetworkModule {
    @Provides
    @Singleton
    fun provideDatabase(app: App) : NewsDatabase =
        Room.databaseBuilder(app, NewsDatabase::class.java, "news_database")
            .fallbackToDestructiveMigration()
            .build()

    @Singleton
    @Provides
    fun providesApiService(retrofit: Retrofit): Api {
        return retrofit.create(Api::class.java)
    }

    @Singleton
    @Provides
    fun provideRepository(api: Api, db: NewsDatabase): Repository
            = Repository(api, db)

    @Provides
    fun provideCoroutineDispatcherIO() = Dispatchers.IO
}