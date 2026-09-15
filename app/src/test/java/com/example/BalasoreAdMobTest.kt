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
        // Calling initialize with application context in test environment must be safe
        val context = androidx.test.core.app.ApplicationProvider.getApplicationContext<android.content.Context>()
        AdMobManager.initialize(context)
        // Should not crash
        assertNotNull(AdMobManager.activeBannerAdUnitId)
    }
}
