package com.solomon.carbonapp.di.module

import com.solomon.carbonapp.view.ui.MainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class AppModule {

    //@ActivityScoped
    @ContributesAndroidInjector
    abstract fun contributeMainActivity(): MainActivity
}