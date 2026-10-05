package com.example

import com.example.data.model.AppLanguage
import com.example.ui.viewmodel.BalasoreViewModel
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Unit Test Suite for Language Toggle Feature and Auto-Update Engines
 * across ViewModel and Local String Resource mappings.
 */
class LanguageToggleUnitTest {

    @Test
    fun testLanguageSelection_EnglishToOdia() {
        val viewModel = BalasoreViewModel()

        // Default initial language
        assertEquals(AppLanguage.ENGLISH, viewModel.uiState.value.language)

        // Switch to Odia
        viewModel.setLanguage(AppLanguage.ODIA)
        assertEquals(AppLanguage.ODIA, viewModel.uiState.value.language)

        // Switch back to English
        viewModel.setLanguage(AppLanguage.ENGLISH)
        assertEquals(AppLanguage.ENGLISH, viewModel.uiState.value.language)
    }

    @Test
    fun testLanguageToggle_Bidirectional() {
        val viewModel = BalasoreViewModel()
        assertEquals(AppLanguage.ENGLISH, viewModel.uiState.value.language)

        // Toggle 1: English -> Odia
        viewModel.toggleLanguage()
        assertEquals(AppLanguage.ODIA, viewModel.uiState.value.language)

        // Toggle 2: Odia -> English
        viewModel.toggleLanguage()
        assertEquals(AppLanguage.ENGLISH, viewModel.uiState.value.language)

        // Toggle 3: English -> Odia
        viewModel.toggleLanguage()
        assertEquals(AppLanguage.ODIA, viewModel.uiState.value.language)
    }

    @Test
    fun testAutoUpdateEngines_ActiveState() {
        val viewModel = BalasoreViewModel()

        // 1. Hourly Auto-Refresh is active by default
        assertTrue("Hourly auto-refresh should be enabled by default", viewModel.uiState.value.isHourlyAutoRefreshEnabled)
        assertTrue("Minutes remaining should be <= 60", viewModel.uiState.value.nextHourlyRefreshMinutesRemaining in 1..60)

        // 2. News Auto-Update Engine is active
        assertTrue("News auto-update should be enabled by default", viewModel.uiState.value.isNewsAutoUpdateEnabled)
        assertTrue("News auto-update interval should be >= 10s", viewModel.uiState.value.newsAutoUpdateIntervalSeconds >= 10)

        // 3. Proactive Walk-Window Safety Alerts are active
        assertTrue("Walk window alerts should be active by default", viewModel.uiState.value.isWalkWindowAlertsEnabled)
        assertEquals(30, viewModel.uiState.value.walkWindowAdvanceMinutes)

        // 4. Test manual trigger updates cycle count
        val initialCycle = viewModel.uiState.value.autoRefreshCycleCount
        viewModel.triggerHourlyAutoRefresh()
        assertTrue("Cycle count should advance on refresh", viewModel.uiState.value.autoRefreshCycleCount > initialCycle)
    }
}
