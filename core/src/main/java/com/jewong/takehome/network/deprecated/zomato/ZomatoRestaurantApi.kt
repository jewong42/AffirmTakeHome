package com.jewong.takehome.network.deprecated.zomato

import com.jewong.takehome.network.model.zomato.ZomatoResponse
import com.jewong.takehome.network.service.zomato.ZomatoRestaurantService
import retrofit2.Call

@Suppress("unused", "DEPRECATION")
@Deprecated("Use RestaurantApi instead.")
class ZomatoRestaurantApi(private val restaurantService: ZomatoRestaurantService) {
    fun getRestaurants(latitude: Double, longitude: Double, offset: Int): Call<ZomatoResponse> {
        return restaurantService.getZomatoRestaurants(USER_KEY, latitude, longitude, offset)
    }

    companion object {
        private const val USER_KEY = "267d43cc8c37370c833845613671f303"
    }
}