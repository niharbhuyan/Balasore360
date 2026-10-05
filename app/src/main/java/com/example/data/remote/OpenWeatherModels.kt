package com.example.data.remote

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class OpenWeatherCurrentResponse(
    @Json(name = "coord") val coord: OpenWeatherCoord? = null,
    @Json(name = "weather") val weather: List<OpenWeatherCondition> = emptyList(),
    @Json(name = "base") val base: String? = null,
    @Json(name = "main") val main: OpenWeatherMain? = null,
    @Json(name = "visibility") val visibility: Int? = null,
    @Json(name = "wind") val wind: OpenWeatherWind? = null,
    @Json(name = "clouds") val clouds: OpenWeatherClouds? = null,
    @Json(name = "dt") val dt: Long? = null,
    @Json(name = "sys") val sys: OpenWeatherSys? = null,
    @Json(name = "timezone") val timezone: Int? = null,
    @Json(name = "id") val id: Long? = null,
    @Json(name = "name") val name: String? = null,
    @Json(name = "cod") val cod: Int? = null
)

@JsonClass(generateAdapter = true)
data class OpenWeatherForecastResponse(
    @Json(name = "cod") val cod: String? = null,
    @Json(name = "message") val message: Int? = null,
    @Json(name = "cnt") val cnt: Int? = null,
    @Json(name = "list") val list: List<OpenWeatherForecastItem> = emptyList(),
    @Json(name = "city") val city: OpenWeatherCity? = null
)

@JsonClass(generateAdapter = true)
data class OpenWeatherForecastItem(
    @Json(name = "dt") val dt: Long? = null,
    @Json(name = "main") val main: OpenWeatherMain? = null,
    @Json(name = "weather") val weather: List<OpenWeatherCondition> = emptyList(),
    @Json(name = "clouds") val clouds: OpenWeatherClouds? = null,
    @Json(name = "wind") val wind: OpenWeatherWind? = null,
    @Json(name = "visibility") val visibility: Int? = null,
    @Json(name = "pop") val pop: Double? = null,
    @Json(name = "dt_txt") val dtTxt: String? = null
)

@JsonClass(generateAdapter = true)
data class OpenWeatherCoord(
    @Json(name = "lon") val lon: Double? = null,
    @Json(name = "lat") val lat: Double? = null
)

@JsonClass(generateAdapter = true)
data class OpenWeatherCondition(
    @Json(name = "id") val id: Int? = null,
    @Json(name = "main") val main: String? = null,
    @Json(name = "description") val description: String? = null,
    @Json(name = "icon") val icon: String? = null
)

@JsonClass(generateAdapter = true)
data class OpenWeatherMain(
    @Json(name = "temp") val temp: Double? = null,
    @Json(name = "feels_like") val feelsLike: Double? = null,
    @Json(name = "temp_min") val tempMin: Double? = null,
    @Json(name = "temp_max") val tempMax: Double? = null,
    @Json(name = "pressure") val pressure: Int? = null,
    @Json(name = "humidity") val humidity: Int? = null,
    @Json(name = "sea_level") val seaLevel: Int? = null,
    @Json(name = "grnd_level") val grndLevel: Int? = null
)

@JsonClass(generateAdapter = true)
data class OpenWeatherWind(
    @Json(name = "speed") val speed: Double? = null,
    @Json(name = "deg") val deg: Int? = null,
    @Json(name = "gust") val gust: Double? = null
)

@JsonClass(generateAdapter = true)
data class OpenWeatherClouds(
    @Json(name = "all") val all: Int? = null
)

@JsonClass(generateAdapter = true)
data class OpenWeatherSys(
    @Json(name = "type") val type: Int? = null,
    @Json(name = "id") val id: Long? = null,
    @Json(name = "country") val country: String? = null,
    @Json(name = "sunrise") val sunrise: Long? = null,
    @Json(name = "sunset") val sunset: Long? = null
)

@JsonClass(generateAdapter = true)
data class OpenWeatherCity(
    @Json(name = "id") val id: Long? = null,
    @Json(name = "name") val name: String? = null,
    @Json(name = "coord") val coord: OpenWeatherCoord? = null,
    @Json(name = "country") val country: String? = null,
    @Json(name = "population") val population: Int? = null,
    @Json(name = "timezone") val timezone: Int? = null,
    @Json(name = "sunrise") val sunrise: Long? = null,
    @Json(name = "sunset") val sunset: Long? = null
)

/**
 * Domain-specific 3-day forecast summary model for Balasore
 */
data class OpenWeatherThreeDayForecast(
    val dayIndex: Int, // 1 = Today, 2 = Tomorrow, 3 = Day After
    val dateLabel: String,
    val dayName: String,
    val odiaDayName: String,
    val tempMax: Double,
    val tempMin: Double,
    val avgHumidity: Int,
    val windSpeedKmh: Double,
    val conditionMain: String,
    val conditionDesc: String,
    val iconCode: String,
    val popPercent: Int,
    val hourlySlots: List<OpenWeatherForecastItem> = emptyList()
)

/**
 * Domain-specific 7-day daily forecast summary model for Balasore
 */
data class OpenWeatherDailyForecast(
    val dayIndex: Int, // 1 to 7 (1 = Today)
    val dateLabel: String,
    val dayName: String,
    val odiaDayName: String,
    val tempMax: Double,
    val tempMin: Double,
    val avgHumidity: Int,
    val windSpeedKmh: Double,
    val conditionMain: String,
    val conditionDesc: String,
    val iconCode: String,
    val popPercent: Int,
    val uvIndexEstimate: Int = 6,
    val hourlySlots: List<OpenWeatherForecastItem> = emptyList()
)

typealias OpenWeatherSevenDayForecast = OpenWeatherDailyForecast

