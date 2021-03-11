package com.jewong.takehome.network.dagger

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.jewong.takehome.network.mapper.RestaurantMapper
import dagger.Module
import dagger.Provides
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import javax.inject.Named
import javax.inject.Singleton

@Module
class NetworkModule {

    private fun provideOkHttpClient(name: String, value: String): OkHttpClient {
        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BODY)
        val httpClient = OkHttpClient.Builder()
        httpClient.addInterceptor(logging)
        httpClient.addInterceptor(Interceptor { chain ->
            val request = chain.request()
                .newBuilder()
                .addHeader(name, value)
                .build()
            chain.proceed(request)
        })
        return httpClient.build()
    }

    @Provides
    @Singleton
    @Named("yelpRetrofit")
    fun provideYelpRetrofit(): Retrofit {
        val okHttpClient = provideOkHttpClient("Authorization", YELP_TOKEN)
        val contentType = "application/json".toMediaType()
        return Retrofit.Builder()
            .addConverterFactory(Json(JsonConfiguration(ignoreUnknownKeys = true, isLenient = true)).asConverterFactory(contentType))
            .baseUrl(YELP_API_BASE_URL)
            .client(okHttpClient)
            .build()
    }

    @Provides
    @Singleton
    @Named("zomatoRetrofit")
    fun provideZomatoRetrofit(): Retrofit {
        val okHttpClient = provideOkHttpClient("user-key", ZOMATO_TOKEN)
        val contentType = "application/json".toMediaType()
        return Retrofit.Builder()
            .addConverterFactory(Json(JsonConfiguration(ignoreUnknownKeys = true, isLenient = true)).asConverterFactory(contentType))
            .baseUrl(ZOMATO_API_BASE_URL)
            .client(okHttpClient)
            .build()
    }

    @Provides
    @Singleton
    fun provideRestaurantMapper(): RestaurantMapper {
        return RestaurantMapper()
    }

    companion object {
        private const val YELP_API_BASE_URL = "https://api.yelp.com/"
        private const val ZOMATO_API_BASE_URL = "https://developers.zomato.com/api/v2.1/"
        private const val ZOMATO_TOKEN = "267d43cc8c37370c833845613671f303"
        private const val YELP_TOKEN = "Bearer itoMaM6DJBtqD54BHSZQY9WdWR5xI_CnpZdxa3SG5i7N0M37VK1HklDDF4ifYh8SI-P2kI_mRj5KRSF4_FhTUAkEw322L8L8RY6bF1UB8jFx3TOR0-wW6Tk0KftNXXYx"
    }
}