package com.solomon.carbonapp.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.solomon.carbonapp.di.ViewModelFactory
import com.solomon.carbonapp.di.ViewModelKey
import com.solomon.carbonapp.viewmodel.NewsViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import javax.inject.Singleton

@Module
abstract class ViewModelModule {

    @Binds
    internal abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @Singleton
    @ViewModelKey(NewsViewModel::class)
    internal abstract fun bindDevelopersViewModel(viewModel: NewsViewModel): ViewModel
}