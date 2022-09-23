package com.solomon.lagosdevelopers.di.module

import com.solomon.lagosdevelopers.view.ui.MainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class AppModule {

    //@ActivityScoped
    @ContributesAndroidInjector
    abstract fun contributeMainActivity(): MainActivity
}