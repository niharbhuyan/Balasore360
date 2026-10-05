package com.example.data.remote

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class AstrologyHoroscopeResponse(
    @Json(name = "data")
    val data: HoroscopeDataDto?,
    @Json(name = "status")
    val status: Int? = 200,
    @Json(name = "success")
    val success: Boolean? = true
)

@JsonClass(generateAdapter = true)
data class HoroscopeDataDto(
    @Json(name = "date")
    val date: String? = null,
    @Json(name = "horoscope_data")
    val horoscopeData: String? = null
)

/**
 * Secondary Free Astrology API response model.
 */
@JsonClass(generateAdapter = true)
data class OhmandaHoroscopeResponse(
    @Json(name = "sign")
    val sign: String? = null,
    @Json(name = "date")
    val date: String? = null,
    @Json(name = "horoscope")
    val horoscope: String? = null
)
