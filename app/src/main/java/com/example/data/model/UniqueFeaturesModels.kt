package com.example.data.model

/**
 * 1. Horseshoe Crab & Intertidal Marine Biodiversity Radar
 */
data class HorseshoeCrabSighting(
    val id: String,
    val reporterName: String,
    val beachSector: String, // e.g. "Chandipur Sandbar (3.2 km seaward)"
    val distanceOutKm: Double,
    val species: String, // "Tachypleus gigas (Indo-Pacific Horseshoe Crab)" or "Carcinoscorpius rotundicauda (Mangrove Horseshoe Crab)"
    val count: Int,
    val condition: String, // "Active Mating Pair", "Nesting near sand ripple", "Safely returned to sea"
    val reportedAgo: String,
    val verifiedByFMU: Boolean = true
)

data class IntertidalEcoGuideline(
    val id: String,
    val title: String,
    val odiaTitle: String,
    val description: String,
    val isDo: Boolean // true = DO, false = DON'T
)

/**
 * 2. ITR Chandipur & Dr. APJ Abdul Kalam Defense Trail
 */
data class DefenseTrailMilestone(
    val id: String,
    val year: String,
    val missileName: String,
    val testRange: String,
    val significance: String,
    val rangeKm: String,
    val drKalamMemoir: String,
    val isSeaTest: Boolean = true
)

data class MaritimeExclusionZone(
    val zoneName: String,
    val odiaName: String,
    val coordinates: String,
    val radiusNauticalMiles: Int,
    val activeHours: String,
    val sirenAlert: String,
    val coastalPatrolStatus: String,
    val fishermenHelpline: String = "06782-272000"
)

/**
 * 3. Remuna "Amrita Keli" Prasad & Artisan Showcase
 */
data class PrasadStatus(
    val templeName: String = "Khirachora Gopinatha Temple",
    val nextBatchTime: String = "12:30 PM (Mid-day)",
    val batchPreparation: String = "Simmering in sacred wood-fired hearths (Kudua earthenware)",
    val estimatedKuduAvailable: Int = 180,
    val tokenDistributionOpen: Boolean = true,
    val recipeHeritage: String = "Over 500-year-old unbroken tradition of condensed cow milk, pure ghee, and cardamom offering to Madana Mohana.",
    val currentRitual: String = "Bhog Arpan & Arati in progress"
)

data class BalasoreArtisan(
    val id: String,
    val artisanName: String,
    val craftTitle: String,
    val odiaCraftTitle: String,
    val villageOrCluster: String,
    val description: String,
    val phone: String,
    val experienceYears: Int,
    val giTagged: Boolean = true
)

/**
 * 4. Cyclone Shelter & Coastal Resilience Hub
 */
data class DetailedCycloneShelter(
    val id: String,
    val name: String,
    val block: String,
    val capacity: Int,
    val elevationMeters: Double,
    val solarBackup: Boolean,
    val distanceKm: Double,
    val nodalOfficer: String,
    val phone: String,
    val latitude: Double,
    val longitude: Double
)

data class EmergencyKitItem(
    val id: String,
    val name: String,
    val odiaName: String,
    val category: String,
    var isChecked: Boolean = false,
    val reason: String
)

/**
 * 5. Balaramgadi Harbor Fresh Catch & Rate Board
 */
data class HarborCatchRate(
    val id: String,
    val fishName: String,
    val odiaName: String,
    val wholesalePriceKg: String,
    val retailPriceKg: String,
    val arrivalTime: String,
    val dockLanding: String,
    val freshnessRating: String, // "★★★★★ (Caught 3 hrs ago)"
    val buyingTip: String
)

data class HarborLandingBell(
    val fleetName: String,
    val harbor: String,
    val expectedDockTime: String,
    val catchSummary: String,
    val status: String // "Docking Now", "Arriving in 45m", "Departing on Tides"
)

/**
 * 6. Kuldiha Elephant Corridor & Digital Eco-Passport
 */
data class ElephantCorridorAlert(
    val sector: String,
    val alertLevel: String, // "YELLOW (Caution)", "ORANGE (Herd on Road)", "GREEN (Clear)"
    val herdCount: Int,
    val lastSpottedTime: String,
    val highwaySpeedLimitKmh: Int = 20,
    val advisoryText: String,
    val forestControlPhone: String = "06782-262270"
)

data class EcoPassportStamp(
    val id: String,
    val title: String,
    val odiaTitle: String,
    val location: String,
    val elevation: String,
    val keyAttraction: String,
    val isCheckedIn: Boolean = false
)
