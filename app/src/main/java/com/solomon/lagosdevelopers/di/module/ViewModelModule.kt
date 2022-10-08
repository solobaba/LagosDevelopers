package com.solomon.lagosdevelopers.di.module

import androidx.lifecycle.ViewModel
import com.solomon.lagosdevelopers.viewmodel.DevelopersViewModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@InstallIn(ViewModelComponent::class)
@Module
abstract class ViewModelModule {

    @Provides
    @ViewModelScoped
    internal abstract fun bindDevelopersViewModel(viewModel: DevelopersViewModel): ViewModel
}