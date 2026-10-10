package com.example

import com.example.data.service.ChandipurSafeWalkService
import com.example.data.service.ChandipurTideAlarmReceiver
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Unit tests for Chandipur Safe Walk Background Service,
 * 30-minute advance alert timing calculations, and alarm intent actions.
 */
class ChandipurSafeWalkServiceTest {

    @Test
    fun testAlarmReceiverActionConstants() {
        assertEquals("com.example.action.CHANDIPUR_SAFE_WALK_ALARM", ChandipurTideAlarmReceiver.ACTION_TIDE_ALARM)
        assertEquals("com.example.action.TRIGGER_SAFE_WALK_30M", ChandipurTideAlarmReceiver.ACTION_TRIGGER_SAFE_WALK_30M)
        assertEquals("extra_minutes_until_open", ChandipurTideAlarmReceiver.EXTRA_MINUTES_UNTIL_OPEN)
        assertEquals("extra_receded_km", ChandipurTideAlarmReceiver.EXTRA_RECEDED_KM)
        assertEquals("extra_window_duration_hours", ChandipurTideAlarmReceiver.EXTRA_WINDOW_DURATION_HOURS)
    }

    @Test
    fun testUpcomingWalkWindowDataStructure() {
        val now = System.currentTimeMillis()
        val windowOpen = now + 45 * 60 * 1000L
        val alertTrigger = windowOpen - 30 * 60 * 1000L

        val window = ChandipurSafeWalkService.UpcomingWalkWindow(
            windowOpenEpochMs = windowOpen,
            alertTriggerEpochMs = alertTrigger,
            minutesUntilAlert = 15L,
            windowOpenMinuteOfDay = 720,
            estimatedRecessionKm = 4.8,
            safeWindowDurationHours = 3.5,
            telemetrySource = "Tidal Telemetry Test"
        )

        assertEquals(15L, window.minutesUntilAlert)
        assertEquals(30 * 60 * 1000L, window.windowOpenEpochMs - window.alertTriggerEpochMs)
        assertEquals(4.8, window.estimatedRecessionKm, 0.001)
        assertEquals(3.5, window.safeWindowDurationHours, 0.001)
        assertTrue(window.telemetrySource.contains("Telemetry"))
    }
}
