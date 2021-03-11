package com.jewong.takehome.data.repository

import com.jewong.takehome.data.model.Restaurant
import com.jewong.takehome.data.model.RestaurantRequest

interface RestaurantRepository {

    fun getYelpRestaurants(request: RestaurantRequest, callback: JWCallback<List<Restaurant>>)

    fun getZomatoRestaurants(request: RestaurantRequest, callback: JWCallback<List<Restaurant>>)
}