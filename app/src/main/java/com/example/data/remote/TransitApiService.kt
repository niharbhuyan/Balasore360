package com.example.data.remote

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import java.util.concurrent.TimeUnit

interface TransitApiService {

    @GET("api/v1/transit/balasore/live")
    suspend fun getLiveCityBuses(
        @Query("route") routeNumber: String? = null
    ): PublicTransitResponse

    @GET("api/v1/transit/balasore/routes")
    suspend fun getRouteSchedules(): PublicTransitResponse

    companion object {
        // Balasore Smart Mobility Public Transit API Endpoint
        private const val BASE_URL = "https://transit.balasore.gov.in/"

        fun create(): TransitApiService {
            val logging = HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BASIC
            }

            val client = OkHttpClient.Builder()
                .connectTimeout(8, TimeUnit.SECONDS)
                .readTimeout(8, TimeUnit.SECONDS)
                .addInterceptor(logging)
                .build()

            val moshi = Moshi.Builder()
                .addLast(KotlinJsonAdapterFactory())
                .build()

            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(MoshiConverterFactory.create(moshi))
                .build()
                .create(TransitApiService::class.java)
        }
    }
}
