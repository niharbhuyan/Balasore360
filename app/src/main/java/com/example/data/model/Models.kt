package com.example.data.model

enum class AppLanguage {
    ODIA, ENGLISH, HINDI
}

data class Hotspot(
    val id: String,
    val name: String,
    val odiaName: String,
    val category: String,
    val description: String,
    val location: String,
    val rating: Double,
    val isFeatured: Boolean = false,
    val timing: String = "6:00 AM - 6:00 PM",
    val distanceKm: Double = 12.5
)

data class NewsArticle(
    val id: String,
    val title: String,
    val odiaTitle: String,
    val snippet: String,
    val odiaSnippet: String,
    val category: String,
    val timeAgo: String,
    val source: String = "Balasore Live Bureau",
    val isBookmarked: Boolean = false
)

data class WeatherInfo(
    val tempCelsius: Int = 29,
    val condition: String = "Partly Cloudy",
    val highLow: String = "32° / 25°",
    val humidity: String = "74%",
    val windSpeedKmh: String = "18 km/h",
    val tideStatus: String = "Low Tide at 02:45 PM (Receding 5 km)",
    val seaCondition: String = "Safe for Beach Walkers",
    val cycloneAlert: String? = null
)

// 1. Chandipur Tidal Clock & Biodiversity
data class TidalClockData(
    val currentPhase: String = "Receding Sea (Low Tide)",
    val odiaPhase: String = "ଭଟ୍ଟା ସମୟ (ସମୁଦ୍ର ଅପସାରିତ)",
    val distanceRecededKm: Double = 4.6,
    val safeWalkMinutesRemaining: Int = 145, // Minutes until water starts rushing back
    val highTideTime: String = "08:15 PM",
    val lowTideTime: String = "02:30 PM",
    val tidalCoefficient: String = "Spring Tide (Extreme range)",
    val biodiversitySightings: List<String> = listOf(
        "Rare Horseshoe Crabs (Tachypleus gigas) active on outer sandbar",
        "Red Ghost Crabs near casuarina dunes",
        "Golden conch shells exposed at 3.5 km mark"
    )
)

// 2. DRDO ITR Coastal Advisories & Heritage
data class DrdoAdvisory(
    val id: String,
    val title: String,
    val odiaTitle: String,
    val dateOrTime: String,
    val status: String, // "CLEARED FOR FISHING", "COASTAL RESTRICTION", "UPCOMING TRIAL"
    val seaZone: String,
    val fishermenAdvisory: String,
    val sonicBoomWarning: Boolean = false,
    val heritageMilestone: String? = null
)

// 3. Remuna Khirachora Bhog & Darshan
data class TempleRitualInfo(
    val templeName: String = "Khirachora Gopinatha Temple, Remuna",
    val odiaTempleName: String = "କ୍ଷୀରଚୋରା ଗୋପୀନାଥ ମନ୍ଦିର, ରେମୁଣା",
    val nextBhogDistribution: String = "12:15 PM & 07:30 PM",
    val bhogName: String = "Sacred Amruta Keli (କ୍ଷୀର ଭୋଗ)",
    val bhogAvailabilityNote: String = "Fresh earthen pots (Kudua) prepared twice daily by temple servitors",
    val currentDarshanStatus: String = "Open for Devotees",
    val mangalaAlati: String = "05:30 AM",
    val sandhyaArati: String = "06:45 PM",
    val pahadaTime: String = "01:00 PM - 04:00 PM",
    val pilgrimageCircuit: List<String> = listOf(
        "Remuna Gopinatha (9 km)",
        "Panchalingeswar Spring (30 km)",
        "Emami Jagannath Temple (6 km)",
        "Bhusandeswar Shiva (82 km)"
    )
)

// 4. River Gauge & Flood Warning Telemetry
data class RiverGauge(
    val riverName: String,
    val odiaRiverName: String,
    val stationName: String,
    val currentLevelMeters: Double,
    val warningLevelMeters: Double,
    val dangerLevelMeters: Double,
    val status: String, // "NORMAL", "ALERT", "DANGER"
    val trend: String // "Rising", "Falling", "Steady"
)

data class CycloneShelter(
    val name: String,
    val block: String,
    val capacityPeople: Int,
    val nodalContact: String,
    val phone: String
)

// 5. Seafood Catch & Harbor Wholesale Rates
data class SeafoodCatch(
    val id: String,
    val name: String,
    val odiaName: String,
    val harbor: String,
    val priceRangeKg: String,
    val qualityGrade: String,
    val freshness: String = "Morning Harbor Landed",
    val peakSeason: String
)

// 6. Fakir Mohan Literary Trail & Daily Idiom
data class LiteraryTrailPoint(
    val id: String,
    val title: String,
    val odiaTitle: String,
    val location: String,
    val excerpt: String,
    val historicalContext: String
)

data class DailyBalasoreIdiom(
    val wordOrIdiom: String,
    val odiaScript: String,
    val meaning: String,
    val literaryWork: String,
    val funContext: String
)

data class EmergencyContact(
    val id: String,
    val name: String,
    val odiaName: String,
    val number: String,
    val category: String,
    val is24x7: Boolean = true,
    val description: String = ""
)

data class TransitSchedule(
    val id: String,
    val routeName: String,
    val origin: String,
    val destination: String,
    val time: String,
    val type: String, // Train or Bus
    val status: String = "On Time"
)
