package com.example

import com.example.data.repository.UniqueFeaturesRepository
import com.example.ui.viewmodel.BalasoreViewModel
import com.example.ui.viewmodel.UniqueFeatureSheetType
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class BalasoreUniqueFeaturesTest {

    @Test
    fun testRepositoryUniqueFeaturesDataPresent() {
        // 1. Horseshoe Crab
        val crabSightings = UniqueFeaturesRepository.horseshoeCrabSightings
        assertTrue("Crab sightings should not be empty", crabSightings.isNotEmpty())
        assertTrue("Guidelines should not be empty", UniqueFeaturesRepository.intertidalEcoGuidelines.isNotEmpty())
        assertEquals("Tachypleus gigas (Indo-Pacific Horseshoe Crab)", crabSightings[0].species)

        // 2. Defense Trail
        val milestones = UniqueFeaturesRepository.defenseMilestones
        assertTrue("Defense milestones should not be empty", milestones.isNotEmpty())
        assertNotNull(UniqueFeaturesRepository.maritimeExclusionZone)

        // 3. Remuna Prasad & Artisans
        val prasad = UniqueFeaturesRepository.prasadStatus
        assertNotNull(prasad)
        assertEquals("Khirachora Gopinatha Temple, Remuna", prasad.templeName)
        val artisans = UniqueFeaturesRepository.balasoreArtisans
        assertTrue("Artisans list should not be empty", artisans.isNotEmpty())

        // 4. Cyclone Shelter & Disaster Kit
        val detailedShelters = UniqueFeaturesRepository.detailedCycloneShelters
        assertTrue("Cyclone shelters should not be empty", detailedShelters.isNotEmpty())
        val kitItems = UniqueFeaturesRepository.defaultEmergencyKitItems
        assertTrue("Kit items should not be empty", kitItems.isNotEmpty())

        // 5. Harbor Catch & Rates
        val catchRates = UniqueFeaturesRepository.harborCatchRates
        assertTrue("Catch rates should not be empty", catchRates.isNotEmpty())
        val landingBells = UniqueFeaturesRepository.harborLandingBells
        assertTrue("Landing bells should not be empty", landingBells.isNotEmpty())

        // 6. Elephant Corridor & Eco Passport
        assertNotNull(UniqueFeaturesRepository.elephantCorridorAlert)
        val stamps = UniqueFeaturesRepository.ecoPassportStamps
        assertTrue("Passport stamps should not be empty", stamps.isNotEmpty())
    }

    @Test
    fun testViewModelFeatureSheetOpenAndClose() {
        val viewModel = BalasoreViewModel()
        assertNull(viewModel.uiState.value.activeFeatureSheet)

        viewModel.openFeatureSheet(UniqueFeatureSheetType.HORSESHOE_CRAB)
        assertEquals(UniqueFeatureSheetType.HORSESHOE_CRAB, viewModel.uiState.value.activeFeatureSheet)

        viewModel.openFeatureSheet(UniqueFeatureSheetType.CYCLONE_RESILIENCE)
        assertEquals(UniqueFeatureSheetType.CYCLONE_RESILIENCE, viewModel.uiState.value.activeFeatureSheet)

        viewModel.closeFeatureSheet()
        assertNull(viewModel.uiState.value.activeFeatureSheet)
    }

    @Test
    fun testViewModelToggleEmergencyKitItem() {
        val viewModel = BalasoreViewModel()
        val firstItemId = viewModel.uiState.value.emergencyKitItems.first().id
        val initialChecked = viewModel.uiState.value.emergencyKitItems.first().isChecked

        viewModel.toggleEmergencyKitItem(firstItemId)
        val afterToggle = viewModel.uiState.value.emergencyKitItems.first().isChecked
        assertEquals(!initialChecked, afterToggle)

        viewModel.toggleEmergencyKitItem(firstItemId)
        assertEquals(initialChecked, viewModel.uiState.value.emergencyKitItems.first().isChecked)
    }

    @Test
    fun testViewModelToggleEcoPassportStamp() {
        val viewModel = BalasoreViewModel()
        val firstStampId = viewModel.uiState.value.ecoPassportStamps.first().id
        val initialCheckedIn = viewModel.uiState.value.ecoPassportStamps.first().isCheckedIn

        viewModel.toggleEcoPassportStamp(firstStampId)
        val afterToggle = viewModel.uiState.value.ecoPassportStamps.first().isCheckedIn
        assertEquals(!initialCheckedIn, afterToggle)
    }

    @Test
    fun testViewModelLogHorseshoeCrabSighting() {
        val viewModel = BalasoreViewModel()
        val initialCount = viewModel.uiState.value.horseshoeCrabSightings.size

        viewModel.logHorseshoeCrabSighting(
            reporter = "Marine Volunteer",
            sector = "Sector 3 - Balaramgadi Estuary",
            species = "Carcinoscorpius rotundicauda (Mangrove Horseshoe Crab)",
            count = 4
        )

        val updatedList = viewModel.uiState.value.horseshoeCrabSightings
        assertEquals(initialCount + 1, updatedList.size)
        val newEntry = updatedList.first()
        assertEquals("Sector 3 - Balaramgadi Estuary", newEntry.beachSector)
        assertEquals(4, newEntry.count)
    }
}
