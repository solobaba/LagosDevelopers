//package com.solomon.lagosdevelopers.di
//
//import com.solomon.lagosdevelopers.App
//import com.solomon.lagosdevelopers.di.module.AppModule
//import com.solomon.lagosdevelopers.di.module.NetworkModule
//import com.solomon.lagosdevelopers.di.module.ViewModelModule
//import com.solomon.lagosdevelopers.model.service.ServiceModule
//import dagger.BindsInstance
//import dagger.Component
//import dagger.Module
//import dagger.android.AndroidInjector
//import dagger.android.support.AndroidSupportInjectionModule
//import dagger.hilt.InstallIn
//import javax.inject.Singleton
//
//@InstallIn
//@Module(
//    includes = [
//        AndroidSupportInjectionModule::class,
//        AppModule::class,
//        ViewModelModule::class,
//        NetworkModule::class,
//        ServiceModule::class
//    ]
//)
//
//interface AppComponent : AndroidInjector<App> {
//
//    @Component.Factory
//    interface Factory {
//        fun create(@BindsInstance application: App): AppComponent
//    }
//}