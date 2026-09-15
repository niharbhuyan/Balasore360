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
}
