package com.jewong.takehome.network.mapper

import com.jewong.takehome.data.model.Restaurant
import com.jewong.takehome.network.model.yelp.YelpRestaurant
import com.jewong.takehome.network.model.zomato.ZomatoRestaurant

class RestaurantMapper {

    fun toRestaurant(yelpRestaurant: YelpRestaurant): Restaurant {
        return with(yelpRestaurant) {
            Restaurant(id, name, image, rating.toString())
        }
    }

    fun toRestaurant(zomatoRestaurant: ZomatoRestaurant?): Restaurant {
        return if (zomatoRestaurant?.restaurantDetail == null) Restaurant()
        else with(zomatoRestaurant.restaurantDetail) {
            Restaurant(id, name, image, userRating.rating)
        }
    }
}