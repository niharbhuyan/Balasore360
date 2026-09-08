package com.example.data.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Air
import androidx.compose.material.icons.filled.Thunderstorm
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.filled.Water
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.ui.theme.BentoAmberBg
import com.example.ui.theme.BentoAmberText
import com.example.ui.theme.BentoGreenBg
import com.example.ui.theme.BentoGreenText
import com.example.ui.theme.BentoPrimaryBlue
import com.example.ui.theme.BentoRedBg
import com.example.ui.theme.BentoRedText

/**
 * Severity level of meteorological and marine alerts for the Balasore district.
 */
enum class AlertSeverity(
    val levelName: String,
    val odiaLabel: String,
    val badgeBg: Color,
    val badgeText: Color,
    val isUrgent: Boolean
) {
    SAFE(
        levelName = "SAFE & CLEAR",
        odiaLabel = "ସୁରକ୍ଷିତ ପାଣିପାଗ",
        badgeBg = BentoGreenBg,
        badgeText = BentoGreenText,
        isUrgent = false
    ),
    ADVISORY(
        levelName = "ADVISORY (YELLOW)",
        odiaLabel = "ସତର୍କ ସୂଚନା (ହଳଦିଆ)",
        badgeBg = BentoAmberBg,
        badgeText = BentoAmberText,
        isUrgent = false
    ),
    WARNING(
        levelName = "WARNING (ORANGE)",
        odiaLabel = "ଚେତାବନୀ (କମଳା)",
        badgeBg = BentoAmberBg,
        badgeText = BentoAmberText,
        isUrgent = true
    ),
    CYCLONE_ALERT(
        levelName = "CYCLONE ALERT (RED)",
        odiaLabel = "ବାତ୍ୟା ସତର୍କତା (ନାଲି)",
        badgeBg = BentoRedBg,
        badgeText = BentoRedText,
        isUrgent = true
    ),
    HIGH_TIDE(
        levelName = "TIDAL INGRESS",
        odiaLabel = "ଜୁଆର ସତର୍କତା",
        badgeBg = Color(0xFFE0F2FE),
        badgeText = BentoPrimaryBlue,
        isUrgent = false
    )
}

/**
 * Affected sub-regions or geographic clusters in Balasore.
 */
enum class BalasoreZone(val zoneName: String, val odiaName: String) {
    COASTAL_CHANDIPUR("Chandipur & Balaramgadi Coast", "ଚାନ୍ଦିପୁର ଓ ବଳରାମଗଡ଼ି ଉପକୂଳ"),
    TALASARI_SUBARNAREKHA("Talasari & Subarnarekha Estuary", "ତାଳସାରୀ ଓ ସୁବର୍ଣ୍ଣରେଖା ମୁହାଣ"),
    BALASORE_TOWN("Balasore Municipality & Remuna", "ବାଲେଶ୍ୱର ସହର ଓ ରେମୁଣା"),
    NILAGIRI_HILLS("Nilagiri & Kuldiha Forest Foothills", "ନୀଳଗିରି ଓ କୁଳଡିହା ପାଦଦେଶ"),
    KASAFAL_BAHANAGA("Kasafal & Bahanaga Coastline", "କାସାଫାଳ ଓ ବାହାନଗା ତଟବର୍ତ୍ତୀ"),
    DISTRICT_WIDE("Entire Balasore District", "ସମଗ୍ର ବାଲେଶ୍ୱର ଜିଲ୍ଲା")
}

/**
 * Category of the atmospheric/marine hazard.
 */
enum class WeatherAlertCategory(val title: String, val iconVector: ImageVector, val emoji: String) {
    CYCLONE("Tropical Depression / Cyclone", Icons.Default.Air, "🌀"),
    THUNDERSTORM("Severe Thunderstorm & Lightning (କାଳବୈଶାଖୀ)", Icons.Default.Thunderstorm, "⚡"),
    HIGH_WIND("Gale Wind & Storm Surge", Icons.Default.Air, "💨"),
    HEAVY_RAINFALL("Heavy Coastal Downpour", Icons.Default.WaterDrop, "🌧️"),
    TIDAL_SURGE("Chandipur High Tidal Ingress", Icons.Default.Water, "🌊"),
    HEATWAVE("Severe Summer Heatwave (ଗ୍ରୀଷ୍ମ ପ୍ରବାହ)", Icons.Default.WbSunny, "☀️"),
    MARINE_SAFETY("Deep-Sea Fishermen Advisory", Icons.Default.Warning, "⚓")
}

/**
 * Data Model for Time-Sensitive Weather & Marine Alerts in Balasore.
 *
 * Tracks issuance time, validity window (validFrom to validUntil),
 * real-time expiration checks, affected Balasore localities, safety protocols,
 * and meteorological authority attribution (e.g. IMD Bhubaneswar, INCOIS).
 */
data class BalasoreWeatherAlert(
    val id: String,
    val title: String,
    val odiaTitle: String,
    val summary: String,
    val detailedDescription: String,
    val category: WeatherAlertCategory,
    val severity: AlertSeverity,
    val affectedZones: List<BalasoreZone>,
    val issuingAuthority: String = "IMD Bhubaneswar / Regional Meteorological Centre",
    val validFromMillis: Long,
    val validUntilMillis: Long,
    val issuedAtMillis: Long = System.currentTimeMillis(),
    val actionableInstructions: List<String> = emptyList(),
    val emergencyContact: String = "Balasore District Control Room: 06782-262244",
    val isAcknowledged: Boolean = false
) {
    /**
     * Checks if the alert is currently active given the current clock time.
     */
    fun isActive(currentTimeMillis: Long = System.currentTimeMillis()): Boolean {
        return currentTimeMillis in validFromMillis..validUntilMillis
    }

    /**
     * Checks if the alert has expired.
     */
    fun isExpired(currentTimeMillis: Long = System.currentTimeMillis()): Boolean {
        return currentTimeMillis > validUntilMillis
    }

    /**
     * Checks if the alert is scheduled for a future window.
     */
    fun isUpcoming(currentTimeMillis: Long = System.currentTimeMillis()): Boolean {
        return currentTimeMillis < validFromMillis
    }

    /**
     * Formats remaining time into a human-friendly string (e.g. "Expires in 3 hrs 45 mins").
     */
    fun getFormattedTimeRemaining(currentTimeMillis: Long = System.currentTimeMillis()): String {
        if (isExpired(currentTimeMillis)) return "Alert Expired"
        if (isUpcoming(currentTimeMillis)) {
            val diffMs = validFromMillis - currentTimeMillis
            val diffHrs = diffMs / (1000 * 60 * 60)
            val diffMins = (diffMs / (1000 * 60)) % 60
            return "Active in ${if (diffHrs > 0) "${diffHrs}h " else ""}${diffMins}m"
        }
        val diffMs = validUntilMillis - currentTimeMillis
        val diffHrs = diffMs / (1000 * 60 * 60)
        val diffMins = (diffMs / (1000 * 60)) % 60
        return if (diffHrs > 0) {
            "Valid for next ${diffHrs}h ${diffMins}m"
        } else {
            "Valid for next ${diffMins} mins"
        }
    }
}
