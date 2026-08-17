package com.balasore360.nearby

/** Coordinates are supplied by trusted directory data; no user location is stored here. */
data class NearbyItem(
    val id: String,
    val name: String,
    val category: String,
    val latitude: Double,
    val longitude: Double,
    val rating: Double = 0.0
)

object NearbyDistance {
    fun meters(fromLatitude: Double, fromLongitude: Double, itemLatitude: Double, itemLongitude: Double): Double {
        val earthRadius = 6_371_000.0
        val dLat = Math.toRadians(itemLatitude - fromLatitude)
        val dLon = Math.toRadians(itemLongitude - fromLongitude)
        val a = kotlin.math.sin(dLat / 2) * kotlin.math.sin(dLat / 2) +
            kotlin.math.cos(Math.toRadians(fromLatitude)) * kotlin.math.cos(Math.toRadians(itemLatitude)) *
            kotlin.math.sin(dLon / 2) * kotlin.math.sin(dLon / 2)
        return earthRadius * 2 * kotlin.math.atan2(kotlin.math.sqrt(a), kotlin.math.sqrt(1 - a))
    }
}
