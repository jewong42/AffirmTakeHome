package com.jewong.takehome.network.service.zomato

import com.jewong.takehome.network.model.zomato.ZomatoResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

// Note: You could use this or define your own.
interface ZomatoRestaurantService {
    @Deprecated("Use headless version instead.")
    @GET("search")
    fun getZomatoRestaurants(@Header(value = "user-key") token: String,
                             @Query("lat") latitude: Double,
                             @Query("lon") longitude: Double,
                             @Query("start") offset: Int = 0): Call<ZomatoResponse>

    @GET("search")
    fun getZomatoRestaurants(@Query("lat") latitude: Double,
                             @Query("lon") longitude: Double,
                             @Query("start") offset: Int = 0): Call<ZomatoResponse>
}