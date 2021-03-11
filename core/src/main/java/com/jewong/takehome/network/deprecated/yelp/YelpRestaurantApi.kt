package com.jewong.takehome.network.deprecated.yelp

import com.jewong.takehome.network.model.yelp.YelpResponse
import com.jewong.takehome.network.service.yelp.YelpRestaurantService
import retrofit2.Call

@Suppress("unused", "DEPRECATION")
@Deprecated("Use RestaurantApi instead.")
class YelpRestaurantApi(private val yelpRestaurantService: YelpRestaurantService) {
    fun getRestaurants(latitude: Double, longitude: Double, offset: Int): Call<YelpResponse> {
        return yelpRestaurantService.getYelpRestaurants(TOKEN, latitude, longitude, offset)
    }

    companion object {
        private const val TOKEN =
            "Bearer itoMaM6DJBtqD54BHSZQY9WdWR5xI_CnpZdxa3SG5i7N0M37VK1HklDDF4ifYh8SI-P2kI_mRj5KRSF4_FhTUAkEw322L8L8RY6bF1UB8jFx3TOR0-wW6Tk0KftNXXYx"
    }
}