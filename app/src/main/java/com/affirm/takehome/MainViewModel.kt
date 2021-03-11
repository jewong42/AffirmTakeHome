package com.affirm.takehome

import android.location.Location
import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.jewong.takehome.data.repository.JWCallback
import com.jewong.takehome.data.model.Restaurant
import com.jewong.takehome.data.model.RestaurantRequest
import com.jewong.takehome.data.usecase.RestaurantUseCase
import javax.inject.Inject

class MainViewModel @Inject constructor(
    private val restaurantUseCase: RestaurantUseCase
) : ViewModel() {

    val restaurants = MutableLiveData<MutableSet<Restaurant>>(mutableSetOf())
    val yelpOffset = MutableLiveData(0)
    val zomatoOffset = MutableLiveData(0)
    val loadingVisibility = MutableLiveData(View.GONE)
    val showError = MutableLiveData("")
    val yesCount = MutableLiveData(0)
    val noCount = MutableLiveData(0)
    private var instance = YELP

    fun getRestaurants(location: Location) {
        if (loadingVisibility.value == View.VISIBLE) return
        loadingVisibility.value = View.VISIBLE
        if (instance == YELP) getYelpRestaurants(location)
        else getZomatoRestaurants(location)
    }

    private fun getYelpRestaurants(location: Location) {
        with(location) {
            val request = RestaurantRequest(latitude, longitude, yelpOffset.value ?: 0)
            restaurantUseCase.getYelpRestaurants(request, object : JWCallback<List<Restaurant>> {
                override fun onResponse(response: List<Restaurant>) {
                    yelpOffset.value = yelpOffset.value?.plus(response.size) ?: response.size
                    restaurants.value?.addAll(response)
                    restaurants.value = restaurants.value
                    updateState(true)
                }

                override fun onFailure(throwable: Throwable) {
                    showError.value = throwable.localizedMessage
                    updateState(false)
                }
            })
        }
    }

    private fun getZomatoRestaurants(location: Location) {
        with(location) {
            val request = RestaurantRequest(latitude, longitude, zomatoOffset.value ?: 0)
            restaurantUseCase.getZomatoRestaurants(request, object : JWCallback<List<Restaurant>> {
                override fun onResponse(response: List<Restaurant>) {
                    zomatoOffset.value = zomatoOffset.value?.plus(response.size) ?: response.size
                    restaurants.value?.addAll(response)
                    restaurants.value = restaurants.value
                    updateState(true)
                }

                override fun onFailure(throwable: Throwable) {
                    showError.value = throwable.localizedMessage
                    updateState(false)
                }
            })
        }
    }

    private fun updateState(alternateInstance: Boolean) {
        loadingVisibility.value = View.GONE
        if (alternateInstance) instance = if (instance == YELP) ZOMATO else YELP
    }

    fun incrementYesCount() {
        yesCount.value = (yesCount.value ?: 0) + 1
    }

    fun incrementNoCount() {
        noCount.value = (noCount.value ?: 0) + 1
    }

    companion object {
        private const val YELP = "YELP"
        private const val ZOMATO = "ZOMATO"
    }
}