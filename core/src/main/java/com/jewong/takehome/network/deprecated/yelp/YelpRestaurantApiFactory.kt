package com.jewong.takehome.network.deprecated.yelp

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.jewong.takehome.network.service.yelp.YelpRestaurantService
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit

private const val YELP_API_BASE_URL = "https://api.yelp.com/"

@Suppress("unused", "DEPRECATION", "EXPERIMENTAL_API_USAGE")
@Deprecated("No longer needed as creation is now being handled by dagger.")
class YelpRestaurantApiFactory {
    companion object {
        fun create(): YelpRestaurantApi {
            val logging = HttpLoggingInterceptor()
            logging.setLevel(HttpLoggingInterceptor.Level.BODY)
            val httpClient = OkHttpClient.Builder()
            httpClient.addInterceptor(logging)

            val contentType = "application/json".toMediaType()

            val retrofit = Retrofit.Builder()
                .addConverterFactory(Json(JsonConfiguration(ignoreUnknownKeys = true, isLenient = true)).asConverterFactory(contentType))
                .baseUrl(YELP_API_BASE_URL)
                .client(httpClient.build())
                .build()

            return YelpRestaurantApi(retrofit.create(YelpRestaurantService::class.java))
        }
    }
}