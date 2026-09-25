package com.example

import com.example.ui.viewmodel.BalasoreViewModel
import com.example.ui.viewmodel.ThemeMode
import org.junit.Assert.assertEquals
import org.junit.Test

class ThemeModeTest {

    @Test
    fun testThemeModeCycleIncludesSystem() {
        // Verify cycle: SYSTEM -> LIGHT -> DARK -> HIGH_CONTRAST_DARK -> SYSTEM
        fun nextTheme(current: ThemeMode): ThemeMode {
            return when (current) {
                ThemeMode.SYSTEM -> ThemeMode.LIGHT
                ThemeMode.LIGHT -> ThemeMode.DARK
                ThemeMode.DARK -> ThemeMode.HIGH_CONTRAST_DARK
                ThemeMode.HIGH_CONTRAST_DARK -> ThemeMode.SYSTEM
            }
        }

        assertEquals(ThemeMode.LIGHT, nextTheme(ThemeMode.SYSTEM))
        assertEquals(ThemeMode.DARK, nextTheme(ThemeMode.LIGHT))
        assertEquals(ThemeMode.HIGH_CONTRAST_DARK, nextTheme(ThemeMode.DARK))
        assertEquals(ThemeMode.SYSTEM, nextTheme(ThemeMode.HIGH_CONTRAST_DARK))
    }

    @Test
    fun testViewModelThemeModeToggle() {
        val viewModel = BalasoreViewModel()
        assertEquals(ThemeMode.SYSTEM, viewModel.uiState.value.themeMode)

        viewModel.cycleThemeMode()
        assertEquals(ThemeMode.LIGHT, viewModel.uiState.value.themeMode)

        viewModel.cycleThemeMode()
        assertEquals(ThemeMode.DARK, viewModel.uiState.value.themeMode)

        viewModel.cycleThemeMode()
        assertEquals(ThemeMode.HIGH_CONTRAST_DARK, viewModel.uiState.value.themeMode)

        viewModel.cycleThemeMode()
        assertEquals(ThemeMode.SYSTEM, viewModel.uiState.value.themeMode)

        viewModel.setThemeMode(ThemeMode.DARK)
        assertEquals(ThemeMode.DARK, viewModel.uiState.value.themeMode)
    }
}
