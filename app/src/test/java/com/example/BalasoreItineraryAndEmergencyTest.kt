package com.example

import com.example.data.local.ItineraryItemEntity
import com.example.data.model.AppLanguage
import com.example.data.model.Hotspot
import com.example.data.remote.EmergencyAlertDto
import com.example.ui.viewmodel.BalasoreViewModel
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class BalasoreItineraryAndEmergencyTest {

    @Test
    fun testItineraryItemEntityCreationAndProperties() {
        val item = ItineraryItemEntity(
            id = 1L,
            hotspotId = "chandipur_beach",
            hotspotName = "Chandipur Vanishing Sea Beach",
            odiaName = "ଚାନ୍ଦିପୁର ବେଳାଭୂମି",
            category = "Beach & Coast",
            dayNumber = 1,
            timeSlot = "02:30 PM",
            notes = "Observe the sea receding 5 km",
            latitude = 21.4674,
            longitude = 87.0242,
            weatherCondition = "Breezy Sea",
            weatherIconUrl = "https://cdn.weatherapi.com/weather/64x64/day/113.png",
            tempC = 28.0
        )

        assertEquals("chandipur_beach", item.hotspotId)
        assertEquals(1, item.dayNumber)
        assertEquals("02:30 PM", item.timeSlot)
        assertEquals(21.4674, item.latitude, 0.001)
        assertEquals(87.0242, item.longitude, 0.001)
        assertEquals(28.0, item.tempC, 0.1)
    }

    @Test
    fun testEmergencyAlertDismissalInViewModel() {
        val viewModel = BalasoreViewModel()

        assertTrue(viewModel.uiState.value.dismissedAlertIds.isEmpty())

        // Dismiss alert 1
        viewModel.dismissEmergencyAlert("ALERT-CYC-01")
        assertTrue(viewModel.uiState.value.dismissedAlertIds.contains("ALERT-CYC-01"))
        assertEquals(1, viewModel.uiState.value.dismissedAlertIds.size)

        // Dismiss alert 2
        viewModel.dismissEmergencyAlert("ALERT-RIV-02")
        assertTrue(viewModel.uiState.value.dismissedAlertIds.contains("ALERT-CYC-01"))
        assertTrue(viewModel.uiState.value.dismissedAlertIds.contains("ALERT-RIV-02"))
        assertEquals(2, viewModel.uiState.value.dismissedAlertIds.size)
    }

    @Test
    fun testEmergencyAlertDtoAttributes() {
        val alert = EmergencyAlertDto(
            id = "ALERT-TEST-01",
            title = "High Tide & Rough Sea Alert",
            odiaTitle = "ଉଚ୍ଚ ଜୁଆର ଏବଂ ଅଶାନ୍ତ ସମୁଦ୍ର ଚେତାବନୀ",
            type = "TIDAL_SURGE",
            severity = "HIGH_PRIORITY",
            summary = "Waves expected to reach 3.5 meters along Chandipur coast.",
            details = "Fishermen advised not to venture into deep sea for the next 24 hours.",
            affectedArea = "Chandipur & Talasari Coastal Belt",
            waterLevelMeters = 3.8,
            dangerLevelMeters = 3.5,
            actionRequired = "Evacuate low-lying fishing hamlets and secure dry boats."
        )

        assertEquals("HIGH_PRIORITY", alert.severity)
        assertEquals("TIDAL_SURGE", alert.type)
        assertEquals("Chandipur & Talasari Coastal Belt", alert.affectedArea)
        assertEquals(3.8, alert.waterLevelMeters!!, 0.01)
    }

    @Test
    fun testHotspotCreationWithOdiaNames() {
        val spot = Hotspot(
            id = "remuna_temple",
            name = "Khirachora Gopinatha Temple",
            odiaName = "କ୍ଷୀରଚୋରା ଗୋପୀନାଥ ମନ୍ଦିର",
            category = "Heritage & Spiritual",
            description = "Ancient 12th-century shrine famous for Amrita Keli condensed milk offering.",
            location = "Remuna, 9 km from Balasore town",
            rating = 4.8,
            distanceKm = 9.2
        )

        assertNotNull(spot.id)
        assertEquals("Khirachora Gopinatha Temple", spot.name)
        assertEquals("କ୍ଷୀରଚୋରା ଗୋପୀନାଥ ମନ୍ଦିର", spot.odiaName)
        assertEquals(4.8, spot.rating, 0.01)
    }
}
