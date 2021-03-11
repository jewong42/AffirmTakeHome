package com.jewong.takehome.network.dagger

import com.jewong.takehome.data.repository.RestaurantRepository
import com.jewong.takehome.network.repository.RestaurantRepositoryImpl
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
interface RepositoryModule {

    @Binds
    @Singleton
    fun bindRestaurantRepository(restaurantRepository: RestaurantRepositoryImpl): RestaurantRepository
}