package com.jewong.takehome.network.service.yelp

import com.jewong.takehome.network.model.yelp.YelpResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

// Note: You could use this or define your own.
interface YelpRestaurantService {
    @Deprecated("Use headless version instead.")
    @GET("v3/businesses/search")
    fun getYelpRestaurants(@Header(value = "Authorization") token: String,
                           @Query("latitude") latitude: Double,
                           @Query("longitude") longitude: Double,
                           @Query("offset") offset: Int = 0): Call<YelpResponse>

    @GET("v3/businesses/search")
    fun getYelpRestaurants(@Query("latitude") latitude: Double,
                           @Query("longitude") longitude: Double,
                           @Query("offset") offset: Int = 0): Call<YelpResponse>
}