package com.jewong.takehome.data.usecase

import com.jewong.takehome.data.repository.JWCallback
import com.jewong.takehome.data.model.Restaurant
import com.jewong.takehome.data.model.RestaurantRequest
import com.jewong.takehome.data.repository.RestaurantRepository
import javax.inject.Inject

class RestaurantUseCase @Inject constructor(
    private val restaurantRepository: RestaurantRepository
) {

    fun getYelpRestaurants(request: RestaurantRequest, callback: JWCallback<List<Restaurant>>) {
        return restaurantRepository.getYelpRestaurants(request, callback)
    }

    fun getZomatoRestaurants(request: RestaurantRequest, callback: JWCallback<List<Restaurant>>) {
        return restaurantRepository.getZomatoRestaurants(request, callback)
    }
}