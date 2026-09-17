package com.example.data.model

/**
 * Historical timeframes supported for Balasore tidal analysis and sea-recession tracking.
 */
enum class TidalTimeframe(val label: String, val description: String) {
    HOURLY_24H("24H Cycle", "Today's continuous semi-diurnal harmonic tidal oscillations"),
    HISTORICAL_7D("7-Day Trend", "Daily high vs. low tide ranges & seabed recession envelope"),
    MONTHLY_30D("30-Day Lunar", "Spring-Neap lunar cycle & maximum vanishing sea records")
}

/**
 * Tidal movement phases along Chandipur and Balaramgadi coastline.
 */
enum class TidalPhase(val label: String, val badgeColor: Long) {
    HIGH_TIDE("High Tide", 0xFF0284C7),
    RECEDING("Vanishing (Receding)", 0xFF0D9488),
    LOW_TIDE_PEAK("Peak Low Tide", 0xFFD97706),
    INCOMING("Returning (Incoming)", 0xFFE11D48)
}

/**
 * Granular observation data point representing tidal water level and sea recession distance.
 */
data class TidalTrendPoint(
    val id: String,
    val timestamp: Long,
    val timeLabel: String,
    val tideHeightMeters: Double,
    val recessionDistanceKm: Double,
    val phase: TidalPhase,
    val phaseDescription: String,
    val lunarCondition: String = "Spring Tide (Purnima)",
    val safetyStatus: String = "Safe for Seabed Exploration"
)

/**
 * Summary metrics computed from local storage for user insights.
 */
data class TidalTrendSummary(
    val currentHeightMeters: Double,
    val currentRecessionKm: Double,
    val maxRecessionObservedKm: Double,
    val lowestTideLevelMeters: Double,
    val highestTideLevelMeters: Double,
    val totalObservationsCount: Int,
    val lastStorageSyncTimestamp: Long
)
