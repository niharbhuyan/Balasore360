package com.example

import com.example.ui.components.WatermarkOpacity
import com.example.ui.components.WatermarkStyle
import com.example.ui.viewmodel.BalasoreViewModel
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Assert.assertFalse
import org.junit.Test

class BalasoreWatermarkTest {

    @Test
    fun testDefaultWatermarkConfiguration() {
        val viewModel = BalasoreViewModel()
        val state = viewModel.uiState.value

        // Verify watermark is active by default across full app
        assertTrue("Watermark should be enabled by default", state.isWatermarkEnabled)
        assertEquals("Default watermark style should be CENTER_EMBLEM", WatermarkStyle.CENTER_EMBLEM, state.watermarkStyle)
        assertEquals("Default watermark opacity should be MEDIUM (8.5%)", WatermarkOpacity.MEDIUM, state.watermarkOpacity)
    }

    @Test
    fun testWatermarkToggle() {
        val viewModel = BalasoreViewModel()

        viewModel.setWatermarkEnabled(false)
        assertFalse(viewModel.uiState.value.isWatermarkEnabled)

        viewModel.setWatermarkEnabled(true)
        assertTrue(viewModel.uiState.value.isWatermarkEnabled)
    }

    @Test
    fun testWatermarkStyleTransitions() {
        val viewModel = BalasoreViewModel()

        viewModel.setWatermarkStyle(WatermarkStyle.DIAGONAL_TILES)
        assertEquals(WatermarkStyle.DIAGONAL_TILES, viewModel.uiState.value.watermarkStyle)

        viewModel.setWatermarkStyle(WatermarkStyle.CORNER_STAMP)
        assertEquals(WatermarkStyle.CORNER_STAMP, viewModel.uiState.value.watermarkStyle)

        viewModel.setWatermarkStyle(WatermarkStyle.CENTER_EMBLEM)
        assertEquals(WatermarkStyle.CENTER_EMBLEM, viewModel.uiState.value.watermarkStyle)
    }

    @Test
    fun testWatermarkStyleCycling() {
        val viewModel = BalasoreViewModel()
        viewModel.setWatermarkStyle(WatermarkStyle.CENTER_EMBLEM)

        viewModel.cycleWatermarkStyle()
        assertEquals(WatermarkStyle.DIAGONAL_TILES, viewModel.uiState.value.watermarkStyle)

        viewModel.cycleWatermarkStyle()
        assertEquals(WatermarkStyle.CORNER_STAMP, viewModel.uiState.value.watermarkStyle)

        viewModel.cycleWatermarkStyle()
        assertEquals(WatermarkStyle.CENTER_EMBLEM, viewModel.uiState.value.watermarkStyle)
    }

    @Test
    fun testWatermarkOpacityLevels() {
        val viewModel = BalasoreViewModel()

        viewModel.setWatermarkOpacity(WatermarkOpacity.SUBTLE)
        assertEquals(WatermarkOpacity.SUBTLE, viewModel.uiState.value.watermarkOpacity)
        assertEquals(0.045f, WatermarkOpacity.SUBTLE.alpha, 0.001f)

        viewModel.setWatermarkOpacity(WatermarkOpacity.MEDIUM)
        assertEquals(WatermarkOpacity.MEDIUM, viewModel.uiState.value.watermarkOpacity)
        assertEquals(0.085f, WatermarkOpacity.MEDIUM.alpha, 0.001f)

        viewModel.setWatermarkOpacity(WatermarkOpacity.PROMINENT)
        assertEquals(WatermarkOpacity.PROMINENT, viewModel.uiState.value.watermarkOpacity)
        assertEquals(0.14f, WatermarkOpacity.PROMINENT.alpha, 0.001f)
    }
}
