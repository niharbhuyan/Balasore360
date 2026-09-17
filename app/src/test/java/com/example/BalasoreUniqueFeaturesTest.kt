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

    @Test
    fun testDailyUpdateEnginePulseCalculation() {
        val pulse = com.example.data.daily.DailyUpdateEngine.getDailyPulse()
        assertNotNull(pulse)
        assertTrue("Formatted date should not be blank", pulse.formattedDate.isNotBlank())
        assertTrue("Proverb odia text should not be blank", pulse.proverbOfTheDay.odiaText.isNotBlank())
        assertTrue("Proverb translation should not be blank", pulse.proverbOfTheDay.englishTranslation.isNotBlank())
        assertTrue("Proverbs bank must have at least 14 proverbs", com.example.data.daily.DailyUpdateEngine.proverbsLibrary.size >= 14)
        assertTrue("Red crab emergence window should be present", pulse.redCrabEveningWindow.isNotBlank())
        assertNotNull(pulse.auctionCountdownText)
    }

    @Test
    fun testViewModelNewFeatureSheetsNavigation() {
        val viewModel = BalasoreViewModel()

        val newTypes = listOf(
            UniqueFeatureSheetType.KASAFAL_RED_CRABS,
            UniqueFeatureSheetType.NILAGIRI_STONE_SABAI,
            UniqueFeatureSheetType.BALARAMGADI_ESTUARY,
            UniqueFeatureSheetType.COLONIAL_HERITAGE_WALK,
            UniqueFeatureSheetType.TALASARI_AUCTION_MONITOR,
            UniqueFeatureSheetType.BALESWARIYA_DIALECT_PROVERBS,
            UniqueFeatureSheetType.PAN_BARAJA_AGRO,
            // 10 Next-Gen Features
            UniqueFeatureSheetType.BICHITRAPUR_MANGROVE_BOATING,
            UniqueFeatureSheetType.CHANDANESWAR_CHADAK_MELA,
            UniqueFeatureSheetType.PANCHALINGESWAR_STREAM_SAFETY,
            UniqueFeatureSheetType.DARK_SKY_BIOLUMINESCENCE,
            UniqueFeatureSheetType.BELL_METAL_ARTISANS,
            UniqueFeatureSheetType.BUDDHIST_JAIN_CIRCUIT,
            UniqueFeatureSheetType.HEIRLOOM_RICE_AGRO,
            UniqueFeatureSheetType.LAKHANNATH_ZAMINDARI,
            UniqueFeatureSheetType.BUDHABALANGA_RIVER_ANGLING,
            UniqueFeatureSheetType.CYCLONE_ORAL_HISTORY
        )

        for (sheetType in newTypes) {
            viewModel.openFeatureSheet(sheetType)
            assertEquals(sheetType, viewModel.uiState.value.activeFeatureSheet)
            viewModel.closeFeatureSheet()
            assertNull(viewModel.uiState.value.activeFeatureSheet)
        }
    }

    @Test
    fun testExtendedDailyPulseFields() {
        val pulse = com.example.data.daily.DailyUpdateEngine.getDailyPulse()
        assertTrue("Bichitrapur boating window should be present", pulse.bichitrapurBoatingWindow.isNotBlank())
        assertTrue("Moon phase name should not be blank", pulse.moonPhaseName.isNotBlank())
        assertTrue("Bioluminescence likelihood should not be blank", pulse.bioluminescenceLikelihood.isNotBlank())
        assertTrue("Panchalingeswar flow status should not be blank", pulse.panchalingeswarFlowStatus.isNotBlank())
        assertTrue("Chandaneswar daily ritual should not be blank", pulse.chandaneswarDailyRitual.isNotBlank())
        assertTrue("Budhabalanga angling window should not be blank", pulse.budhabalangaAnglingWindow.isNotBlank())
        assertTrue("Heirloom paddy stage should not be blank", pulse.heirloomPaddyGrowthStage.isNotBlank())
    }

    @Test
    fun testViewModelRefreshDailyPulse() {
        val viewModel = BalasoreViewModel()
        assertNotNull(viewModel.uiState.value.dailyPulse)
        viewModel.refreshDailyPulse()
        assertNotNull(viewModel.uiState.value.dailyPulse)
    }

    @Test
    fun testHealthcareRepositoryDataPresent() {
        val doctors = com.example.data.repository.HealthcareRepository.doctorsList
        assertTrue("Doctors list should not be empty", doctors.isNotEmpty())
        assertTrue("At least 5 doctors available", doctors.size >= 5)
        assertEquals("Dr. Asit Kumar Mohanty", doctors[0].name)
        assertTrue("Phone numbers must be valid", doctors.all { it.phone.isNotBlank() })

        val stores = com.example.data.repository.HealthcareRepository.medicineStoresList
        assertTrue("Medicine stores should not be empty", stores.isNotEmpty())
        assertTrue("Should have 24x7 stores", stores.any { it.is24x7 })
        assertTrue("Should have Jan Aushadhi generic stores", stores.any { it.isJanAushadhiGeneric })

        val clinics = com.example.data.repository.HealthcareRepository.polyclinicsList
        assertTrue("Polyclinics should not be empty", clinics.isNotEmpty())
        assertTrue("Clinics should have facilities listed", clinics.all { it.facilities.isNotEmpty() })
    }

    @Test
    fun testHealthcareDailyPulseFields() {
        val pulse = com.example.data.daily.DailyUpdateEngine.getDailyPulse()
        assertTrue("Doctors on duty status should not be blank", pulse.todayOnDutyEmergencyDoctors.isNotBlank())
        assertTrue("Active pharmacies count should be >= 1", pulse.activePharmacies24x7Count >= 1)
        assertTrue("Emergency pharmacy duty should not be blank", pulse.medicineStoreEmergencyDuty.isNotBlank())
        assertTrue("Polyclinic sample status should not be blank", pulse.polyclinicSampleCollectionStatus.isNotBlank())
        assertTrue("Anti venom advisory should not be blank", pulse.antiVenomStockAdvisory.isNotBlank())
        assertTrue("Specialist OPD status should not be blank", pulse.todaySpecialistOPDStatus.isNotBlank())
    }

    @Test
    fun testHealthcareSheetNavigation() {
        val viewModel = BalasoreViewModel()
        val medicalSheets = listOf(
            UniqueFeatureSheetType.DOCTORS_DIRECTORY,
            UniqueFeatureSheetType.MEDICINE_STORES_DIRECTORY,
            UniqueFeatureSheetType.POLYCLINIC_DIRECTORY,
            UniqueFeatureSheetType.PATHOLOGY_LAB_DIRECTORY
        )

        for (sheetType in medicalSheets) {
            viewModel.openFeatureSheet(sheetType)
            assertEquals(sheetType, viewModel.uiState.value.activeFeatureSheet)
            viewModel.closeFeatureSheet()
            assertNull(viewModel.uiState.value.activeFeatureSheet)
        }
    }

    @Test
    fun testDoctorStatusAndStoreCalculations() {
        val firstDoc = com.example.data.repository.HealthcareRepository.doctorsList.first()
        val docStatus = com.example.data.repository.HealthcareRepository.getDoctorDailyStatus(firstDoc)
        assertTrue("Doctor status must have text", docStatus.isNotBlank())

        val store24x7 = com.example.data.repository.HealthcareRepository.medicineStoresList.first { it.is24x7 }
        assertTrue("24x7 store must always be open now", com.example.data.repository.HealthcareRepository.isStoreOpenNow(store24x7))

        val labStatus = com.example.data.repository.HealthcareRepository.getDiagnosticCollectionStatus()
        assertTrue("Lab diagnostic status must contain text", labStatus.isNotBlank())
    }

    @Test
    fun testPathologyLabRepositoryAndStatus() {
        val labs = com.example.data.repository.HealthcareRepository.pathologyLabsList
        assertTrue("Pathology labs list should not be empty", labs.isNotEmpty())
        assertTrue("At least 5 labs present", labs.size >= 5)

        val firstLab = labs.first()
        assertEquals("Dr. Lal PathLabs - Balasore Reference Lab", firstLab.name)
        assertTrue("Should have popular packages", firstLab.popularPackages.isNotEmpty())
        assertTrue("Should have phone number", firstLab.phone.isNotBlank())

        val dailyLabStatus = com.example.data.repository.HealthcareRepository.getPathologyLabDailyStatus(firstLab)
        assertTrue("Daily lab status must not be blank", dailyLabStatus.isNotBlank())

        val homeCollectionStatus = com.example.data.repository.HealthcareRepository.isHomeCollectionActiveNow()
        assertTrue("Home collection status must not be blank", homeCollectionStatus.isNotBlank())

        val emergencyLab = labs.first { it.id == "path_5" }
        assertTrue(emergencyLab.openingHours.contains("24 Hours"))
    }
}
