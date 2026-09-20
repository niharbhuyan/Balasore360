package com.example

import com.example.ui.viewmodel.BalasoreViewModel
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class BalasoreHourlyRefreshTest {

    @Test
    fun testDefaultHourlyAutoRefreshConfiguration() {
        val viewModel = BalasoreViewModel()
        val state = viewModel.uiState.value

        assertTrue("Hourly auto-refresh should be enabled by default", state.isHourlyAutoRefreshEnabled)
        assertEquals("Initial countdown minutes should be 60", 60, state.nextHourlyRefreshMinutesRemaining)
        assertEquals("Initial cycle count should be 0", 0, state.autoRefreshCycleCount)
        assertTrue("Last refresh timestamp should be recent", state.lastHourlyRefreshTimestamp > 0)
    }

    @Test
    fun testToggleHourlyAutoRefresh() {
        val viewModel = BalasoreViewModel()

        viewModel.setHourlyAutoRefreshEnabled(false)
        assertFalse(viewModel.uiState.value.isHourlyAutoRefreshEnabled)
        assertEquals(0, viewModel.uiState.value.nextHourlyRefreshMinutesRemaining)

        viewModel.setHourlyAutoRefreshEnabled(true)
        assertTrue(viewModel.uiState.value.isHourlyAutoRefreshEnabled)
        assertEquals(60, viewModel.uiState.value.nextHourlyRefreshMinutesRemaining)
    }

    @Test
    fun testTriggerHourlyAutoRefreshIncrementsCycle() {
        val viewModel = BalasoreViewModel()
        val initialCycles = viewModel.uiState.value.autoRefreshCycleCount

        viewModel.triggerHourlyAutoRefresh()

        val updatedState = viewModel.uiState.value
        assertEquals("Cycle count should increment", initialCycles + 1, updatedState.autoRefreshCycleCount)
        assertEquals("Countdown reset to 60", 60, updatedState.nextHourlyRefreshMinutesRemaining)
    }
}
