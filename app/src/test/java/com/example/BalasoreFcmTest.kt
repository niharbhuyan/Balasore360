package com.example

import com.example.data.fcm.BalasoreNotificationHelper
import com.example.data.fcm.FcmManager
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class BalasoreFcmTest {

    @Test
    fun `verify FCM topic strings match Balasore specifications`() {
        assertEquals("balasore_weather_alerts", FcmManager.TOPIC_WEATHER_ALERTS)
        assertEquals("balasore_breaking_news", FcmManager.TOPIC_BREAKING_NEWS)
    }

    @Test
    fun `verify Notification Channel IDs match FCM topics for consistent routing`() {
        assertEquals(FcmManager.TOPIC_WEATHER_ALERTS, BalasoreNotificationHelper.CHANNEL_ID_WEATHER)
        assertEquals(FcmManager.TOPIC_BREAKING_NEWS, BalasoreNotificationHelper.CHANNEL_ID_NEWS)
    }

    @Test
    fun `verify notification intent keys and IDs are well defined`() {
        assertEquals("target_tab", BalasoreNotificationHelper.EXTRA_TARGET_TAB)
        assertEquals("article_id", BalasoreNotificationHelper.EXTRA_ARTICLE_ID)
        assertTrue(BalasoreNotificationHelper.NOTIFICATION_ID_WEATHER_BASE > 0)
        assertTrue(BalasoreNotificationHelper.NOTIFICATION_ID_NEWS_BASE > 0)
        assertTrue(BalasoreNotificationHelper.NOTIFICATION_ID_WEATHER_BASE != BalasoreNotificationHelper.NOTIFICATION_ID_NEWS_BASE)
    }

    @Test
    fun `verify state flows in FcmManager are initialized`() {
        assertNotNull(FcmManager.weatherAlertsEnabled)
        assertNotNull(FcmManager.breakingNewsEnabled)
        assertNotNull(FcmManager.fcmToken)
    }
}
