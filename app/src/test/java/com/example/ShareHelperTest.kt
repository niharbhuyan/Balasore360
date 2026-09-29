package com.example

import com.example.data.model.AlertSeverity
import com.example.data.model.BalasoreWeatherAlert
import com.example.data.model.BalasoreZone
import com.example.data.model.WeatherAlertCategory
import com.example.data.remote.EmergencyAlertDto
import com.example.util.ShareHelper
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class ShareHelperTest {

    @Test
    fun testBuildNewsShareText() {
        val (subject, text) = ShareHelper.buildNewsShareText(
            title = "OTDC Launches Sea-Facing Walkway at Chandipur",
            snippet = "New eco-friendly walkway inaugurated with night illumination.",
            source = "Sambad Balasore",
            category = "Civic",
            odiaTitle = "ଚାନ୍ଦିପୁରରେ ନୂତନ ୱାକୱେ ଉଦଘାଟିତ"
        )

        assertEquals("Balasore News: OTDC Launches Sea-Facing Walkway at Chandipur", subject)
        assertTrue(text.contains("OTDC Launches Sea-Facing Walkway"))
        assertTrue(text.contains("ଚାନ୍ଦିପୁରରେ ନୂତନ ୱାକୱେ ଉଦଘାଟିତ"))
        assertTrue(text.contains("Category: Civic"))
        assertTrue(text.contains("Source: Sambad Balasore"))
        assertTrue(text.contains("Shared via Balasore 360 App"))
    }

    @Test
    fun testBuildNewsShareTextWithoutOdiaTitle() {
        val (subject, text) = ShareHelper.buildNewsShareText(
            title = "Balasore Flower Exhibition Begins Today",
            snippet = "Over 150 flower varieties on display at Gandhi Smriti Park.",
            source = "Prameya",
            category = "Events",
            odiaTitle = ""
        )

        assertEquals("Balasore News: Balasore Flower Exhibition Begins Today", subject)
        assertTrue(text.contains("Over 150 flower varieties"))
        assertTrue(text.contains("Prameya"))
        assertTrue(!text.contains("🇮🇳"))
    }

    @Test
    fun testBuildEmergencyAlertTextCyclone() {
        val alert = EmergencyAlertDto(
            id = "alert-101",
            title = "Severe Cyclone Alert - Coastal Warning",
            odiaTitle = "ବାଲେଶ୍ୱର ଉପକୂଳରେ ବାତ୍ୟା ସତର୍କତା",
            type = "CYCLONE_ALERT",
            severity = "CRITICAL",
            summary = "Deep depression intensified into severe cyclonic storm.",
            details = "Expected landfall near Chandipur coastline with wind gusting up to 95 kmph.",
            affectedArea = "Chandipur, Balaramgadi, Kasafal",
            windSpeedKmph = 95,
            waterLevelMeters = null,
            dangerLevelMeters = null,
            actionRequired = "Fishermen strictly prohibited from venturing into sea. Move to cyclone shelters.",
            emergencyHelpline = "06782-262244"
        )

        val (subject, text) = ShareHelper.buildEmergencyAlertText(alert)

        assertTrue(subject.contains("BALASORE EMERGENCY ALERT"))
        assertTrue(text.contains("LIVE CYCLONE WARNING"))
        assertTrue(text.contains("95 km/h gusts"))
        assertTrue(text.contains("06782-262244"))
        assertTrue(text.contains("ବାଲେଶ୍ୱର ଉପକୂଳରେ ବାତ୍ୟା ସତର୍କତା"))
        assertTrue(text.contains("Shared via Balasore 360 App"))
    }

    @Test
    fun testBuildEmergencyAlertTextRiverSpike() {
        val alert = EmergencyAlertDto(
            id = "alert-102",
            title = "Jalaka River Spiking Above Danger Level",
            odiaTitle = "",
            type = "RIVER_FLOOD",
            severity = "HIGH_PRIORITY",
            summary = "Water level rising rapidly at Mathani gauge station.",
            details = "Catchment areas in Mayurbhanj received over 120mm rainfall in last 12 hours.",
            affectedArea = "Basta & Balasore Sadar",
            windSpeedKmph = null,
            waterLevelMeters = 6.45,
            dangerLevelMeters = 5.50,
            actionRequired = "Basta & riverbanks residents on high alert.",
            emergencyHelpline = "1077"
        )

        val (subject, text) = ShareHelper.buildEmergencyAlertText(alert)

        assertTrue(subject.contains("Jalaka River Spiking Above Danger Level"))
        assertTrue(text.contains("RIVER SPIKE ALERT"))
        assertTrue(text.contains("6.45m (Danger: 5.5m)"))
        assertTrue(text.contains("1077"))
        assertTrue(text.contains("Shared via Balasore 360 App"))
    }

    @Test
    fun testBuildWeatherAlertText() {
        val now = System.currentTimeMillis()
        val weatherAlert = BalasoreWeatherAlert(
            id = "w-alert-01",
            title = "Squally Wind & Heavy Rainfall Warning",
            odiaTitle = "ପ୍ରବଳ ବର୍ଷା ଓ ଝଡ଼ ପବନ ସତର୍କତା",
            summary = "Squally winds 50-60 kmph with heavy rainfall across Balasore coastal belt.",
            detailedDescription = "A convective depression off the Bay of Bengal brings torrential rains and gale winds.",
            category = WeatherAlertCategory.HEAVY_RAINFALL,
            severity = AlertSeverity.WARNING,
            affectedZones = listOf(
                BalasoreZone.COASTAL_CHANDIPUR,
                BalasoreZone.TALASARI_SUBARNAREKHA
            ),
            validFromMillis = now,
            validUntilMillis = now + 4 * 3600 * 1000L,
            actionableInstructions = listOf(
                "Avoid venturing near sea beaches",
                "Keep emergency torches and power banks charged",
                "Stay indoors during lightning strikes"
            ),
            issuingAuthority = "IMD Meteorological Centre Bhubaneswar",
            emergencyContact = "06782-262244"
        )

        val (subject, text) = ShareHelper.buildWeatherAlertText(weatherAlert, "Valid for next 4 hours")

        assertTrue(subject.contains("Squally Wind & Heavy Rainfall Warning"))
        assertTrue(text.contains("Severity: WARNING (ORANGE)"))
        assertTrue(text.contains("Validity: Valid for next 4 hours"))
        assertTrue(text.contains("Chandipur & Balaramgadi Coast"))
        assertTrue(text.contains("Avoid venturing near sea beaches"))
        assertTrue(text.contains("06782-262244"))
        assertTrue(text.contains("IMD Meteorological Centre Bhubaneswar"))
        assertTrue(text.contains("Shared via Balasore 360 App"))
    }
}
