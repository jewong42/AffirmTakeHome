package com.jewong.takehome.network.dagger

import com.jewong.takehome.network.service.yelp.YelpRestaurantService
import com.jewong.takehome.network.service.zomato.ZomatoRestaurantService
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import javax.inject.Named
import javax.inject.Singleton

@Module(includes = [NetworkModule::class])
class ApiModule {

    @Provides
    @Singleton
    fun provideZomatoRestaurantService(@Named("zomatoRetrofit") retrofit: Retrofit): ZomatoRestaurantService {
        return retrofit.create(ZomatoRestaurantService::class.java)
    }

    @Provides
    @Singleton
    fun provideYelpRestaurantService(@Named("yelpRetrofit") retrofit: Retrofit): YelpRestaurantService {
        return retrofit.create(YelpRestaurantService::class.java)
    }
}