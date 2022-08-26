package com.solomon.lagosdevelopers

import android.app.Application

class App : Application() {
    val applicationComponent: ApplicationComponent by lazy {
        DaggerApplicationComponent.factory().create(applicationContext)
    }
}