package com.example

import com.example.data.model.AlertSeverity
import com.example.data.repository.BalasoreRepository
import com.example.data.repository.DefaultData
import com.example.ui.viewmodel.BalasoreViewModel
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class BalasoreDashboardWeatherWidgetTest {

    @Test
    fun test3DayForecastDataIntegrity() {
        val forecast = BalasoreRepository.forecast3Days
        assertEquals(3, forecast.size)

        // Day 1: Tomorrow
        val day1 = forecast[0]
        assertEquals("Tomorrow", day1.dayLabel)
        assertEquals(31, day1.highTempC)
        assertEquals(24, day1.lowTempC)
        assertTrue(day1.rainProbability > 0)
        assertTrue(day1.hourlySlots.isNotEmpty())

        // Day 2: Friday
        val day2 = forecast[1]
        assertEquals("Friday", day2.dayLabel)
        assertEquals(29, day2.highTempC)
        assertEquals(23, day2.lowTempC)
        assertEquals("⚡", day2.weatherIcon)
        assertTrue(day2.rainProbability >= 80)

        // Day 3: Saturday
        val day3 = forecast[2]
        assertEquals("Saturday", day3.dayLabel)
        assertEquals(33, day3.highTempC)
        assertEquals(25, day3.lowTempC)
        assertEquals("☀️", day3.weatherIcon)
    }

    @Test
    fun testSevereWeatherAlertsPresent() {
        val alerts = DefaultData.getInitialWeatherAlerts()
        assertTrue(alerts.isNotEmpty())
        val severeAlert = alerts.firstOrNull { it.severity == AlertSeverity.WARNING || it.severity == AlertSeverity.CYCLONE_ALERT }
        assertNotNull(severeAlert)
        assertTrue(severeAlert!!.actionableInstructions.isNotEmpty())
    }

    @Test
    fun testViewModelTemperatureUnitToggle() {
        val viewModel = BalasoreViewModel()
        assertFalse(viewModel.uiState.value.isFahrenheit)

        viewModel.toggleTemperatureUnit()
        assertTrue(viewModel.uiState.value.isFahrenheit)

        viewModel.toggleTemperatureUnit()
        assertFalse(viewModel.uiState.value.isFahrenheit)
    }

    @Test
    fun testViewModelSelectForecastDay() {
        val viewModel = BalasoreViewModel()
        assertEquals(0, viewModel.uiState.value.selectedForecastDayIndex)

        viewModel.selectForecastDay(1)
        assertEquals(1, viewModel.uiState.value.selectedForecastDayIndex)

        viewModel.selectForecastDay(2)
        assertEquals(2, viewModel.uiState.value.selectedForecastDayIndex)
    }

    @Test
    fun testViewModelAcknowledgeSevereAlert() {
        val viewModel = BalasoreViewModel()
        val firstAlertId = viewModel.uiState.value.weatherAlerts.first().id
        val initialStatus = viewModel.uiState.value.weatherAlerts.first().isAcknowledged
        assertFalse(initialStatus)

        viewModel.acknowledgeAlert(firstAlertId)
        val afterAcknowledge = viewModel.uiState.value.weatherAlerts.first { it.id == firstAlertId }.isAcknowledged
        assertTrue(afterAcknowledge)
    }
}
