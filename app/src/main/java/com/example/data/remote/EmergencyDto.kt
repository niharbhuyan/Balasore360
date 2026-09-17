package com.example.data.remote

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class LiveEmergencyAlertsResponse(
    @Json(name = "status") val status: String,
    @Json(name = "issuedAt") val issuedAt: String,
    @Json(name = "activeCount") val activeCount: Int,
    @Json(name = "alerts") val alerts: List<EmergencyAlertDto>
)

@JsonClass(generateAdapter = true)
data class EmergencyAlertDto(
    @Json(name = "id") val id: String,
    @Json(name = "title") val title: String,
    @Json(name = "odiaTitle") val odiaTitle: String,
    @Json(name = "type") val type: String, // "CYCLONE", "RIVER_SPIKE", "TIDAL_SURGE"
    @Json(name = "severity") val severity: String, // "HIGH_PRIORITY", "CRITICAL", "ADVISORY"
    @Json(name = "summary") val summary: String,
    @Json(name = "details") val details: String,
    @Json(name = "affectedArea") val affectedArea: String,
    @Json(name = "waterLevelMeters") val waterLevelMeters: Double? = null,
    @Json(name = "dangerLevelMeters") val dangerLevelMeters: Double? = null,
    @Json(name = "windSpeedKmph") val windSpeedKmph: Int? = null,
    @Json(name = "actionRequired") val actionRequired: String,
    @Json(name = "emergencyHelpline") val emergencyHelpline: String = "06782-262244"
)
