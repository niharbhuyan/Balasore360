package com.example.data.remote

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * Live GPS Telemetry for a Balasore City Bus (CRUT Mo Bus / Regional Feeder).
 */
@JsonClass(generateAdapter = true)
data class LiveCityBusDto(
    @Json(name = "bus_id") val busId: String,
    @Json(name = "fleet_number") val fleetNumber: String,
    @Json(name = "route_number") val routeNumber: String,
    @Json(name = "route_name_en") val routeNameEn: String,
    @Json(name = "route_name_od") val routeNameOd: String,
    @Json(name = "origin_en") val originEn: String,
    @Json(name = "destination_en") val destinationEn: String,
    @Json(name = "latitude") val latitude: Double,
    @Json(name = "longitude") val longitude: Double,
    @Json(name = "speed_kmh") val speedKmh: Double,
    @Json(name = "heading_degrees") val headingDegrees: Int,
    @Json(name = "heading_direction") val headingDirection: String,
    @Json(name = "current_stop") val currentStop: String,
    @Json(name = "next_stop") val nextStop: String,
    @Json(name = "eta_next_stop_mins") val etaNextStopMins: Int,
    @Json(name = "occupancy_status") val occupancyStatus: String, // SEATS_AVAILABLE, MODERATE, CROWDED
    @Json(name = "schedule_status") val scheduleStatus: String, // ON_TIME, DELAYED, BOARDING
    @Json(name = "bus_type") val busType: String, // AC Electric, Standard CNG, Mini Feeder
    @Json(name = "fare_range") val fareRange: String,
    @Json(name = "driver_contact") val driverContact: String = "+91 94371 88200",
    @Json(name = "last_ping_timestamp") val lastPingTimestamp: Long = System.currentTimeMillis()
)

/**
 * Route Schedule and Stop Sequence for Balasore Transit Lines.
 */
@JsonClass(generateAdapter = true)
data class TransitRouteDto(
    @Json(name = "route_id") val routeId: String,
    @Json(name = "route_number") val routeNumber: String,
    @Json(name = "title_en") val titleEn: String,
    @Json(name = "title_od") val titleOd: String,
    @Json(name = "origin") val origin: String,
    @Json(name = "destination") val destination: String,
    @Json(name = "operating_hours") val operatingHours: String,
    @Json(name = "frequency_mins") val frequencyMins: Int,
    @Json(name = "fare_info") val fareInfo: String,
    @Json(name = "bus_type") val busType: String,
    @Json(name = "stops") val stops: List<String> = emptyList(),
    @Json(name = "departure_times") val departureTimes: List<String> = emptyList(),
    @Json(name = "distance_km") val distanceKm: Double = 15.0
)

/**
 * Transit Response wrapper for Public Transit API queries.
 */
@JsonClass(generateAdapter = true)
data class PublicTransitResponse(
    @Json(name = "status") val status: String? = "success",
    @Json(name = "city") val city: String? = "Balasore",
    @Json(name = "live_buses") val liveBuses: List<LiveCityBusDto> = emptyList(),
    @Json(name = "routes") val routes: List<TransitRouteDto> = emptyList(),
    @Json(name = "timestamp") val timestamp: Long = System.currentTimeMillis()
)
