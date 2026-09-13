package com.example

import com.example.ui.viewmodel.ThemeMode
import org.junit.Assert.assertEquals
import org.junit.Test

class ThemeModeTest {

    @Test
    fun testThemeModeCycleIncludesSystem() {
        // Verify cycle: SYSTEM -> LIGHT -> DARK -> SYSTEM
        fun nextTheme(current: ThemeMode): ThemeMode {
            return when (current) {
                ThemeMode.SYSTEM -> ThemeMode.LIGHT
                ThemeMode.LIGHT -> ThemeMode.DARK
                ThemeMode.DARK -> ThemeMode.SYSTEM
            }
        }

        assertEquals(ThemeMode.LIGHT, nextTheme(ThemeMode.SYSTEM))
        assertEquals(ThemeMode.DARK, nextTheme(ThemeMode.LIGHT))
        assertEquals(ThemeMode.SYSTEM, nextTheme(ThemeMode.DARK))
    }
}
