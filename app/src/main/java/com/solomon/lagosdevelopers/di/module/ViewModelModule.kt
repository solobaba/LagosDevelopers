package com.solomon.lagosdevelopers.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.solomon.lagosdevelopers.di.ViewModelFactory
import com.solomon.lagosdevelopers.di.ViewModelKey
import com.solomon.lagosdevelopers.viewmodel.DevelopersViewModel
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
    @ViewModelKey(DevelopersViewModel::class)
    internal abstract fun bindDevelopersViewModel(viewModel: DevelopersViewModel): ViewModel
}