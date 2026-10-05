package com.example.data.emergency

import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.roundToInt
import kotlin.math.sin
import kotlin.math.sqrt

enum class EmergencyCategory(val label: String, val odiaLabel: String, val iconEmoji: String) {
    HOSPITAL("Hospitals & Medical", "ଡାକ୍ତରଖାନା ଓ ଚିକିତ୍ସା", "🏥"),
    POLICE("Police Stations", "ଥାନା ଓ ସୁରକ୍ଷା", "🚨"),
    CYCLONE_SHELTER("Cyclone Shelters", "ବାତ୍ୟା ଆଶ୍ରୟସ୍ଥଳୀ", "🛡️"),
    FIRE_RESCUE("Fire & Disaster Response", "ଅଗ୍ନିଶମ ଓ ବିପର୍ଯ୍ୟୟ", "🚒")
}

data class EmergencyServiceLocation(
    val id: String,
    val name: String,
    val odiaName: String,
    val category: EmergencyCategory,
    val latitude: Double,
    val longitude: Double,
    val address: String,
    val odiaAddress: String,
    val phone: String,
    val is24x7: Boolean = true,
    val capacityOrBeds: String = "",
    val facilities: List<String> = emptyList(),
    val landmarkNear: String = ""
) {
    /**
     * Calculates great-circle distance in kilometers using the Haversine formula.
     */
    fun distanceFrom(userLat: Double, userLng: Double): Double {
        val r = 6371.0 // Earth radius in km
        val dLat = Math.toRadians(latitude - userLat)
        val dLng = Math.toRadians(longitude - userLng)
        val a = sin(dLat / 2) * sin(dLat / 2) +
                cos(Math.toRadians(userLat)) * cos(Math.toRadians(latitude)) *
                sin(dLng / 2) * sin(dLng / 2)
        val c = 2 * atan2(sqrt(a), sqrt(1 - a))
        return r * c
    }

    /**
     * Formats distance cleanly (e.g. "750 m" or "2.4 km").
     */
    fun formattedDistance(userLat: Double, userLng: Double): String {
        val distKm = distanceFrom(userLat, userLng)
        return if (distKm < 1.0) {
            "${(distKm * 1000).roundToInt()} m"
        } else {
            String.format(java.util.Locale.US, "%.1f km", distKm)
        }
    }

    /**
     * Calculates compass cardinal direction from user coordinate to this location.
     */
    fun compassBearing(userLat: Double, userLng: Double): String {
        val dLng = Math.toRadians(longitude - userLng)
        val lat1 = Math.toRadians(userLat)
        val lat2 = Math.toRadians(latitude)
        val y = sin(dLng) * cos(lat2)
        val x = cos(lat1) * sin(lat2) - sin(lat1) * cos(lat2) * cos(dLng)
        var degrees = Math.toDegrees(atan2(y, x))
        degrees = (degrees + 360) % 360

        return when {
            degrees in 22.5..67.5 -> "North-East (ଉତ୍ତର-ପୂର୍ବ)"
            degrees in 67.5..112.5 -> "East (ପୂର୍ବ)"
            degrees in 112.5..157.5 -> "South-East (ଦକ୍ଷିଣ-ପୂର୍ବ)"
            degrees in 157.5..202.5 -> "South (ଦକ୍ଷିଣ)"
            degrees in 202.5..247.5 -> "South-West (ଦକ୍ଷିଣ-ପଶ୍ଚିମ)"
            degrees in 247.5..292.5 -> "West (ପଶ୍ଚିମ)"
            degrees in 292.5..337.5 -> "North-West (ଉତ୍ତର-ପଶ୍ଚିମ)"
            else -> "North (ଉତ୍ତର)"
        }
    }

    /**
     * Estimated driving arrival time in minutes (based on 32 km/h average coastal city speed).
     */
    fun estimatedDrivingTimeMinutes(userLat: Double, userLng: Double): Int {
        val distKm = distanceFrom(userLat, userLng)
        val mins = (distKm / 32.0 * 60.0).roundToInt()
        return mins.coerceAtLeast(1)
    }
}
