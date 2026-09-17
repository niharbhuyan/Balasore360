package com.example

import com.example.data.model.TidalPhase
import com.example.data.model.TidalTimeframe
import com.example.data.model.TidalTrendPoint
import com.example.data.model.TidalTrendSummary
import org.json.JSONArray
import org.json.JSONObject
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Unit tests for Historical Balasore Tidal & Sea-Recession data models, local storage serialization,
 * and statistical aggregations for Recharts data visualization.
 */
class BalasoreTidalRechartsTest {

    @Test
    fun testTidalTimeframes() {
        val timeframes = TidalTimeframe.entries
        assertEquals(3, timeframes.size)
        assertTrue(timeframes.contains(TidalTimeframe.HOURLY_24H))
        assertTrue(timeframes.contains(TidalTimeframe.HISTORICAL_7D))
        assertTrue(timeframes.contains(TidalTimeframe.MONTHLY_30D))

        assertEquals("24H Cycle", TidalTimeframe.HOURLY_24H.label)
        assertEquals("7-Day Trend", TidalTimeframe.HISTORICAL_7D.label)
        assertEquals("30-Day Lunar", TidalTimeframe.MONTHLY_30D.label)
    }

    @Test
    fun testTidalPhasesAndBadgeColors() {
        val phases = TidalPhase.entries
        assertEquals(4, phases.size)

        val lowTide = TidalPhase.LOW_TIDE_PEAK
        assertEquals("Peak Low Tide", lowTide.label)
        assertEquals(0xFFD97706, lowTide.badgeColor)

        val highTide = TidalPhase.HIGH_TIDE
        assertEquals("High Tide", highTide.label)
        assertEquals(0xFF0284C7, highTide.badgeColor)
    }

    @Test
    fun testTidalTrendPointCreationAndMetrics() {
        val point = TidalTrendPoint(
            id = "test_tide_1",
            timestamp = 1726567200000L,
            timeLabel = "03:00 PM",
            tideHeightMeters = 0.40,
            recessionDistanceKm = 5.10,
            phase = TidalPhase.LOW_TIDE_PEAK,
            phaseDescription = "Maximum Vanishing Sea • 5.1 km seabed exposed",
            lunarCondition = "Purnima Spring Tide",
            safetyStatus = "Prime Walk Window"
        )

        assertEquals("test_tide_1", point.id)
        assertEquals("03:00 PM", point.timeLabel)
        assertEquals(0.40, point.tideHeightMeters, 0.001)
        assertEquals(5.10, point.recessionDistanceKm, 0.001)
        assertEquals(TidalPhase.LOW_TIDE_PEAK, point.phase)
        assertTrue(point.phaseDescription.contains("5.1 km"))
        assertTrue(point.recessionDistanceKm > 4.5) // Vanishing sea reaches over 4.5 km in Balasore
    }

    @Test
    fun testTidalDataFormattingAndParsing() {
        val samplePoint = TidalTrendPoint(
            id = "t1",
            timestamp = 1000L,
            timeLabel = "09:00 AM",
            tideHeightMeters = 4.10,
            recessionDistanceKm = 0.05,
            phase = TidalPhase.HIGH_TIDE,
            phaseDescription = "High Tide Peak",
            lunarCondition = "Spring Tide",
            safetyStatus = "Safe Promenade"
        )

        assertEquals("09:00 AM", samplePoint.timeLabel)
        assertEquals(4.10, samplePoint.tideHeightMeters, 0.001)
        assertEquals(0.05, samplePoint.recessionDistanceKm, 0.001)
        assertEquals("HIGH_TIDE", samplePoint.phase.name)
        assertEquals("High Tide Peak", samplePoint.phaseDescription)
    }

    @Test
    fun testTidalTrendSummaryCalculation() {
        val points = listOf(
            TidalTrendPoint("p1", 1L, "06:00 AM", 2.40, 1.90, TidalPhase.INCOMING, ""),
            TidalTrendPoint("p2", 2L, "09:00 AM", 4.10, 0.05, TidalPhase.HIGH_TIDE, ""),
            TidalTrendPoint("p3", 3L, "12:00 PM", 2.15, 2.20, TidalPhase.RECEDING, ""),
            TidalTrendPoint("p4", 4L, "03:00 PM", 0.40, 5.10, TidalPhase.LOW_TIDE_PEAK, ""),
            TidalTrendPoint("p5", 5L, "06:00 PM", 2.70, 1.45, TidalPhase.INCOMING, "")
        )

        val lowestTide = points.minOf { it.tideHeightMeters }
        val highestTide = points.maxOf { it.tideHeightMeters }
        val maxRecession = points.maxOf { it.recessionDistanceKm }
        val latest = points.last()

        val summary = TidalTrendSummary(
            currentHeightMeters = latest.tideHeightMeters,
            currentRecessionKm = latest.recessionDistanceKm,
            maxRecessionObservedKm = maxRecession,
            lowestTideLevelMeters = lowestTide,
            highestTideLevelMeters = highestTide,
            totalObservationsCount = points.size,
            lastStorageSyncTimestamp = System.currentTimeMillis()
        )

        assertEquals(0.40, summary.lowestTideLevelMeters, 0.001)
        assertEquals(4.10, summary.highestTideLevelMeters, 0.001)
        assertEquals(5.10, summary.maxRecessionObservedKm, 0.001)
        assertEquals(2.70, summary.currentHeightMeters, 0.001)
        assertEquals(1.45, summary.currentRecessionKm, 0.001)
        assertEquals(5, summary.totalObservationsCount)
    }
}
