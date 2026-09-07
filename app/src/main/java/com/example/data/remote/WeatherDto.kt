package com.example.data.remote

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class OpenMeteoResponse(
    @field:Json(name = "latitude") val latitude: Double? = null,
    @field:Json(name = "longitude") val longitude: Double? = null,
    @field:Json(name = "timezone") val timezone: String? = null,
    @field:Json(name = "current") val current: CurrentWeatherDto? = null,
    @field:Json(name = "daily") val daily: DailyWeatherDto? = null
)

@JsonClass(generateAdapter = true)
data class CurrentWeatherDto(
    @field:Json(name = "time") val time: String? = null,
    @field:Json(name = "temperature_2m") val temperature: Double? = null,
    @field:Json(name = "relative_humidity_2m") val relativeHumidity: Int? = null,
    @field:Json(name = "apparent_temperature") val apparentTemperature: Double? = null,
    @field:Json(name = "is_day") val isDay: Int? = null,
    @field:Json(name = "precipitation") val precipitation: Double? = null,
    @field:Json(name = "weather_code") val weatherCode: Int? = null,
    @field:Json(name = "wind_speed_10m") val windSpeed: Double? = null,
    @field:Json(name = "wind_gusts_10m") val windGusts: Double? = null
)

@JsonClass(generateAdapter = true)
data class DailyWeatherDto(
    @field:Json(name = "time") val time: List<String>? = null,
    @field:Json(name = "weather_code") val weatherCode: List<Int>? = null,
    @field:Json(name = "temperature_2m_max") val temperatureMax: List<Double>? = null,
    @field:Json(name = "temperature_2m_min") val temperatureMin: List<Double>? = null,
    @field:Json(name = "sunrise") val sunrise: List<String>? = null,
    @field:Json(name = "sunset") val sunset: List<String>? = null,
    @field:Json(name = "uv_index_max") val uvIndexMax: List<Double>? = null
)
