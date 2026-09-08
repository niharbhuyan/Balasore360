package com.example.data.remote

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class BalasoreMarineWeatherResponse(
    @field:Json(name = "stationId") val stationId: String? = null,
    @field:Json(name = "stationName") val stationName: String? = null,
    @field:Json(name = "latitude") val latitude: Double? = null,
    @field:Json(name = "longitude") val longitude: Double? = null,
    @field:Json(name = "tideState") val tideState: String? = null,
    @field:Json(name = "tideDescription") val tideDescription: String? = null,
    @field:Json(name = "nextTideTransition") val nextTideTransition: String? = null,
    @field:Json(name = "alertLevel") val alertLevel: String? = null,
    @field:Json(name = "alertTitle") val alertTitle: String? = null,
    @field:Json(name = "alertMessage") val alertMessage: String? = null,
    @field:Json(name = "coastalAdvisory") val coastalAdvisory: String? = null,
    @field:Json(name = "seaCondition") val seaCondition: String? = null
)
