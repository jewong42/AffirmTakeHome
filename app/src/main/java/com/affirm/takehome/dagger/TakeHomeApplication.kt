package com.affirm.takehome.dagger

import dagger.android.AndroidInjector
import dagger.android.DaggerApplication

class TakeHomeApplication : DaggerApplication() {

    private lateinit var appComponent: AppComponent

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        appComponent = DaggerAppComponent.builder()
            .application(this)
            .build()
        return appComponent
    }
}