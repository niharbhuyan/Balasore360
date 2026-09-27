package com.example.data.maps

import com.google.android.gms.maps.model.LatLng

/**
 * Representation of a key geographical zone in Balasore district
 * for pre-caching offline map tiles.
 */
data class OfflineKeyArea(
    val id: String,
    val nameEn: String,
    val nameOr: String,
    val description: String,
    val center: LatLng,
    val minLat: Double,
    val maxLat: Double,
    val minLng: Double,
    val maxLng: Double,
    val minZoom: Int = 12,
    val maxZoom: Int = 16,
    val estimatedTiles: Int,
    val estimatedSizeMb: Double,
    val tag: String,
    val keyLandmarks: List<String>
)

/**
 * Persisted state of an offline map pack.
 */
data class OfflineMapPack(
    val areaId: String,
    val isDownloaded: Boolean = false,
    val isDownloading: Boolean = false,
    val downloadProgress: Float = 0f, // 0.0 to 1.0
    val downloadedTiles: Int = 0,
    val totalTiles: Int = 0,
    val sizeOnDiskBytes: Long = 0L,
    val lastUpdatedTimestamp: Long = 0L
)

/**
 * Point of Interest pre-cached for offline navigation.
 */
data class OfflineNavPoint(
    val id: String,
    val nameEn: String,
    val nameOr: String,
    val category: NavPointCategory,
    val location: LatLng,
    val address: String,
    val emergencyPhone: String,
    val is24x7: Boolean = true
)

enum class NavPointCategory {
    HOSPITAL,
    POLICE_DEFENSE,
    CYCLONE_SHELTER,
    TRANSIT,
    FIRE_RESCUE,
    HERITAGE_TOURISM
}

/**
 * Offline turn-by-turn route step.
 */
data class OfflineTurnStep(
    val instructionEn: String,
    val instructionOr: String,
    val distanceMeters: Int,
    val roadName: String,
    val turnIcon: String // "straight", "right", "left", "u_turn", "destination"
)

/**
 * Computed offline route between two points.
 */
data class OfflineNavRoute(
    val origin: LatLng,
    val destination: LatLng,
    val destinationName: String,
    val totalDistanceKm: Double,
    val estimatedDurationMin: Int,
    val pathPoints: List<LatLng>,
    val steps: List<OfflineTurnStep>
)
