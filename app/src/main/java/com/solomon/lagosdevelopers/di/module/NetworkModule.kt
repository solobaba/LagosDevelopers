package com.solomon.lagosdevelopers.di.module

import androidx.room.Room
import com.solomon.lagosdevelopers.App
import com.solomon.lagosdevelopers.db.DevelopersDatabase
import com.solomon.lagosdevelopers.model.repository.Repository
import com.solomon.lagosdevelopers.model.service.Api
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.Dispatchers
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {
    @Provides
    @Singleton
    fun provideDatabase(app: App) : DevelopersDatabase =
        Room.databaseBuilder(app, DevelopersDatabase::class.java, "developer_database")
            //.allowMainThreadQueries()
            .fallbackToDestructiveMigration()
            .build()

    @Provides
    fun providesApiService(retrofit: Retrofit): Api {
        return retrofit.create(Api::class.java)
    }

    @Provides
    @Singleton
    fun provideRepository(api: Api, db: DevelopersDatabase): Repository
            = Repository(api, db)

    @Provides
    @Singleton
    fun provideCoroutineDispatcherIO() = Dispatchers.IO
}