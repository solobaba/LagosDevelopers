package com.solomon.lagosdevelopers.di

import com.solomon.data.model.service.ServiceModule
import com.solomon.lagosdevelopers.App
import com.solomon.lagosdevelopers.di.module.AppModule
import com.solomon.lagosdevelopers.di.module.NetworkModule
import com.solomon.lagosdevelopers.di.module.ViewModelModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AndroidSupportInjectionModule::class,
        AppModule::class,
        ViewModelModule::class,
        NetworkModule::class,
        ServiceModule::class
    ]
)
interface AppComponent : AndroidInjector<App> {

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance application: App): AppComponent
    }
}