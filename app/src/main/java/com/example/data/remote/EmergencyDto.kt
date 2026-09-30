package com.example.data.remote

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class LiveEmergencyAlertsResponse(
    @property:Json(name = "status") val status: String,
    @property:Json(name = "issuedAt") val issuedAt: String,
    @property:Json(name = "activeCount") val activeCount: Int,
    @property:Json(name = "alerts") val alerts: List<EmergencyAlertDto>
)

@JsonClass(generateAdapter = true)
data class EmergencyAlertDto(
    @property:Json(name = "id") val id: String,
    @property:Json(name = "title") val title: String,
    @property:Json(name = "odiaTitle") val odiaTitle: String,
    @property:Json(name = "type") val type: String, // "CYCLONE", "RIVER_SPIKE", "TIDAL_SURGE"
    @property:Json(name = "severity") val severity: String, // "HIGH_PRIORITY", "CRITICAL", "ADVISORY"
    @property:Json(name = "summary") val summary: String,
    @property:Json(name = "details") val details: String,
    @property:Json(name = "affectedArea") val affectedArea: String,
    @property:Json(name = "waterLevelMeters") val waterLevelMeters: Double? = null,
    @property:Json(name = "dangerLevelMeters") val dangerLevelMeters: Double? = null,
    @property:Json(name = "windSpeedKmph") val windSpeedKmph: Int? = null,
    @property:Json(name = "actionRequired") val actionRequired: String,
    @property:Json(name = "emergencyHelpline") val emergencyHelpline: String = "06782-262244"
)
