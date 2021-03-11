package com.jewong.takehome.network.repository

import com.jewong.takehome.data.repository.JWCallback
import com.jewong.takehome.data.model.Restaurant
import com.jewong.takehome.data.repository.RestaurantRepository
import com.jewong.takehome.network.mapper.RestaurantMapper
import com.jewong.takehome.data.model.RestaurantRequest
import com.jewong.takehome.network.model.yelp.YelpResponse
import com.jewong.takehome.network.service.yelp.YelpRestaurantService
import com.jewong.takehome.network.model.zomato.ZomatoResponse
import com.jewong.takehome.network.service.zomato.ZomatoRestaurantService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.logging.Logger
import javax.inject.Inject

class RestaurantRepositoryImpl @Inject constructor(
    private val yelpRestaurantService: YelpRestaurantService,
    private val zomatoRestaurantService: ZomatoRestaurantService,
    private val restaurantMapper: RestaurantMapper
): RestaurantRepository {

    override fun getYelpRestaurants(request: RestaurantRequest, callback: JWCallback<List<Restaurant>>)  {
        Logger.getLogger("Jerry").warning("getYelpRestaurants")
        yelpRestaurantService.getYelpRestaurants(request.latitude, request.longitude, request.offset)
            .enqueue(object : Callback<YelpResponse> {
                override fun onResponse(call: Call<YelpResponse>, response: Response<YelpResponse>) {
                    val list = mutableListOf<Restaurant>()
                    response.body()?.restaurants?.let { restaurants ->
                        for (restaurant in restaurants) {
                            list.add(restaurantMapper.toRestaurant(restaurant))
                        }
                    }
                    callback.onResponse(list)
                }

                override fun onFailure(call: Call<YelpResponse>, t: Throwable) {
                    callback.onFailure(t)
                }
            })
    }

    override fun getZomatoRestaurants(request: RestaurantRequest, callback: JWCallback<List<Restaurant>>)  {
        Logger.getLogger("Jerry").warning("getZomatoRestaurants")
        zomatoRestaurantService.getZomatoRestaurants(request.latitude, request.longitude, request.offset)
            .enqueue(object : Callback<ZomatoResponse> {
                override fun onResponse(call: Call<ZomatoResponse>, response: Response<ZomatoResponse>) {
                    val list = mutableListOf<Restaurant>()
                    response.body()?.restaurants?.let { restaurants ->
                        for (restaurant in restaurants) {
                            list.add(restaurantMapper.toRestaurant(restaurant))
                        }
                    }
                    callback.onResponse(list)
                }

                override fun onFailure(call: Call<ZomatoResponse>, t: Throwable) {
                    callback.onFailure(t)
                }
            })
    }

}