package com.example

import com.example.data.admob.AdMobManager
import com.example.data.repository.BalasoreRepository
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class ExampleUnitTest {

    @Test
    fun verifyAdMobPublisherAndAppId() {
        assertEquals("ca-app-pub-4880243637225183~4956380952", AdMobManager.ADMOB_APP_ID)
        assertEquals("pub-4880243637225183", AdMobManager.PUBLISHER_ID)
        assertNotNull(AdMobManager.TEST_BANNER_AD_UNIT_ID)
    }

    @Test
    fun verifyHotspotsData() {
        val hotspots = BalasoreRepository.hotspots
        assertTrue(hotspots.isNotEmpty())
        val chandipur = hotspots.find { it.id == "chandipur" }
        assertNotNull(chandipur)
        assertEquals("Chandipur Beach", chandipur?.name)
    }

    @Test
    fun verifyEmergencyContacts() {
        val contacts = BalasoreRepository.emergencyContacts
        assertTrue(contacts.isNotEmpty())
        val police = contacts.find { it.number == "112" }
        assertNotNull(police)
    }

    @Test
    fun verifyNewUniqueFeaturesData() {
        // 1. Tidal Clock & DRDO
        val tidalClock = BalasoreRepository.tidalClock
        assertNotNull(tidalClock)
        assertEquals(4.8, tidalClock.distanceRecededKm, 0.01)
        assertTrue(tidalClock.safeWalkMinutesRemaining > 0)

        val drdo = BalasoreRepository.drdoAdvisories
        assertTrue(drdo.isNotEmpty())

        // 2. Remuna Khirachora Bhog
        val remuna = BalasoreRepository.templeRitualInfo
        assertNotNull(remuna)
        assertEquals("12:30 PM (Mid-day) & 07:30 PM (Sandhya)", remuna.nextBhogDistribution)

        // 3. Literary Trail
        val trail = BalasoreRepository.literaryTrailPoints
        assertTrue(trail.isNotEmpty())

        // 4. River Gauges & Cyclone Shelters
        val gauges = BalasoreRepository.riverGauges
        assertTrue(gauges.isNotEmpty())
        val shelters = BalasoreRepository.cycloneShelters
        assertTrue(shelters.isNotEmpty())

        // 5. Seafood Catch
        val seafood = BalasoreRepository.seafoodCatches
        assertTrue(seafood.isNotEmpty())
    }
}
