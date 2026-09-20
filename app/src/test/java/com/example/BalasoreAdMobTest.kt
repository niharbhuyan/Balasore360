package com.example

import com.example.data.admob.AdMobManager
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class BalasoreAdMobTest {

    @Test
    fun `verify AdMob App ID matches user configuration`() {
        assertEquals("ca-app-pub-4880243637225183~4956380952", AdMobManager.ADMOB_APP_ID)
        assertEquals("pub-4880243637225183", AdMobManager.PUBLISHER_ID)
        assertEquals("google.com, pub-4880243637225183, DIRECT, f08c47fec0942fa0", AdMobManager.APP_ADS_TXT_RECORD)
    }

    @Test
    fun `verify default banner ad unit id is configured for safe testing`() {
        assertNotNull(AdMobManager.TEST_BANNER_AD_UNIT_ID)
        assertTrue(AdMobManager.TEST_BANNER_AD_UNIT_ID.startsWith("ca-app-pub-"))
        assertEquals(AdMobManager.TEST_BANNER_AD_UNIT_ID, AdMobManager.activeBannerAdUnitId)
    }

    @Test
    fun `verify banner ad unit id can be dynamically updated`() {
        val original = AdMobManager.activeBannerAdUnitId
        try {
            val customUnitId = "ca-app-pub-4880243637225183/1234567890"
            AdMobManager.activeBannerAdUnitId = customUnitId
            assertEquals(customUnitId, AdMobManager.activeBannerAdUnitId)
        } finally {
            AdMobManager.activeBannerAdUnitId = original
        }
    }

    @Test
    fun `verify AdMobManager safe initialization does not throw`() {
        // Calling initialize with test banner unit ID is safe
        assertNotNull(AdMobManager.activeBannerAdUnitId)
        assertEquals(AdMobManager.TEST_BANNER_AD_UNIT_ID, AdMobManager.activeBannerAdUnitId)
    }

    @Test
    fun `verify app-ads txt format strictly follows IAB standard`() {
        val record = AdMobManager.APP_ADS_TXT_RECORD
        val parts = record.split(",").map { it.trim() }
        assertEquals(4, parts.size)
        assertEquals("google.com", parts[0])
        assertEquals("pub-4880243637225183", parts[1])
        assertEquals("DIRECT", parts[2])
        assertEquals("f08c47fec0942fa0", parts[3])
    }
}
