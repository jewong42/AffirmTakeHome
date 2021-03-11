package com.affirm.takehome.dagger

import android.app.Application
import com.jewong.takehome.network.dagger.ApiModule
import com.jewong.takehome.network.dagger.NetworkModule
import com.jewong.takehome.network.dagger.RepositoryModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AndroidSupportInjectionModule::class,
        AndroidInjectionModule::class,
        NetworkModule::class,
        ApiModule::class,
        RepositoryModule::class,
        ActivityModule::class,
        AppModule::class]
)
interface AppComponent : AndroidInjector<TakeHomeApplication> {

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun application(application: Application): Builder

        fun build(): AppComponent
    }
}