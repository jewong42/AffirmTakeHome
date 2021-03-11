package com.jewong.takehome.network.model.zomato

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ZomatoResponse (
    @SerialName("restaurants") val restaurants: List<ZomatoRestaurant> = listOf()
)