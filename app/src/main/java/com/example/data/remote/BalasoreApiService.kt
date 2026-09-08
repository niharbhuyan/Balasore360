package com.example.data.remote

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import java.util.concurrent.TimeUnit

/**
 * Retrofit interface to fetch news, breaking news alerts, and local coastal weather telemetry
 * from the Balasore District and Marine APIs.
 */
interface BalasoreApiService {

    @GET("api/v1/balasore/news")
    suspend fun getBalasoreNews(
        @Query("category") category: String? = null,
        @Query("page") page: Int = 1,
        @Query("limit") limit: Int = 20
    ): BalasoreNewsResponse

    @GET("api/v1/balasore/news/breaking")
    suspend fun getBreakingNews(): BalasoreNewsResponse

    @GET("api/v1/balasore/weather/marine-observatory")
    suspend fun getMarineObservatoryData(): BalasoreMarineWeatherResponse

    companion object {
        const val BASE_URL = "https://api.balasore360.local/"

        fun create(): BalasoreApiService {
            val logging = HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BASIC
            }

            val client = OkHttpClient.Builder()
                .connectTimeout(15, TimeUnit.SECONDS)
                .readTimeout(15, TimeUnit.SECONDS)
                .addInterceptor(BalasoreMockApiInterceptor())
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
                .create(BalasoreApiService::class.java)
        }
    }
}
