package com.example.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

// Raw Color Constants for Palettes
val RawBentoCanvas = Color(0xFFF3F6FA) // Soft tinted slate-blue canvas
val RawBentoCardWhite = Color(0xFFFFFFFF)
val RawBentoDarkTile = Color(0xFF1A1C1E) // Dark hero card from Bento design

val BentoPrimaryBlue = Color(0xFF2563EB) // Vibrant royal blue
val BentoPrimaryBlueDark = Color(0xFF60A5FA)
val RawBentoBlueLight = Color(0xFFEFF6FF) // blue-50
val RawBentoBluePill = Color(0xFFDBEAFE) // blue-100
val BentoBlueText = Color(0xFF1E40AF) // blue-800

val RawBentoSlate900 = Color(0xFF0F172A)
val RawBentoSlate700 = Color(0xFF334155)
val RawBentoSlate500 = Color(0xFF64748B)
val RawBentoSlate400 = Color(0xFF94A3B8)
val RawBentoSlate100 = Color(0xFFF1F5F9)
val RawBentoBorder = Color(0xFFF1F5F9)
val RawBentoBorderHover = Color(0xFFE2E8F0)
val BentoGold = Color(0xFFF59E0B)

// Alert & Accent Tiles
val BentoRedText = Color(0xFFDC2626)
val BentoRedBg = Color(0xFFFEE2E2)
val BentoAmberText = Color(0xFFD97706)
val BentoAmberBg = Color(0xFFFEF3C7)
val BentoGreenText = Color(0xFF059669)
val BentoGreenBg = Color(0xFFD1FAE5)

// Dynamic Bento Palette for Light & Dark Modes
data class BentoThemePalette(
    val canvas: Color,
    val cardBackground: Color,
    val cardSurfaceVariant: Color,
    val textPrimary: Color,
    val textSecondary: Color,
    val textMuted: Color,
    val textPlaceholder: Color,
    val border: Color,
    val borderHover: Color,
    val blueLight: Color,
    val bluePill: Color,
    val darkTile: Color,
    val isDark: Boolean
)

val LightBentoPalette = BentoThemePalette(
    canvas = RawBentoCanvas,
    cardBackground = RawBentoCardWhite,
    cardSurfaceVariant = RawBentoSlate100,
    textPrimary = RawBentoSlate900,
    textSecondary = RawBentoSlate700,
    textMuted = RawBentoSlate500,
    textPlaceholder = RawBentoSlate400,
    border = RawBentoBorder,
    borderHover = RawBentoBorderHover,
    blueLight = RawBentoBlueLight,
    bluePill = RawBentoBluePill,
    darkTile = RawBentoDarkTile,
    isDark = false
)

val DarkBentoPalette = BentoThemePalette(
    canvas = Color(0xFF0B1120), // Deep midnight navy canvas
    cardBackground = Color(0xFF1E293B), // Slate 800 card
    cardSurfaceVariant = Color(0xFF0F172A),
    textPrimary = Color(0xFFF8FAFC), // White slate 50
    textSecondary = Color(0xFFCBD5E1), // Slate 300
    textMuted = Color(0xFF94A3B8), // Slate 400
    textPlaceholder = Color(0xFF64748B), // Slate 500
    border = Color(0xFF334155), // Slate 700 border
    borderHover = Color(0xFF475569),
    blueLight = Color(0xFF1E293B),
    bluePill = Color(0xFF1E3A8A),
    darkTile = Color(0xFF020617),
    isDark = true
)

val LocalBentoPalette = staticCompositionLocalOf { LightBentoPalette }

// Composable Getters for Seamless Light/Dark Adaptation Across All Screens
val BentoCanvas: Color @Composable get() = LocalBentoPalette.current.canvas
val BentoCardWhite: Color @Composable get() = LocalBentoPalette.current.cardBackground
val BentoSlate900: Color @Composable get() = LocalBentoPalette.current.textPrimary
val BentoSlate700: Color @Composable get() = LocalBentoPalette.current.textSecondary
val BentoSlate500: Color @Composable get() = LocalBentoPalette.current.textMuted
val BentoSlate400: Color @Composable get() = LocalBentoPalette.current.textPlaceholder
val BentoSlate100: Color @Composable get() = LocalBentoPalette.current.cardSurfaceVariant
val BentoBorder: Color @Composable get() = LocalBentoPalette.current.border
val BentoBorderHover: Color @Composable get() = LocalBentoPalette.current.borderHover
val BentoBlueLight: Color @Composable get() = LocalBentoPalette.current.blueLight
val BentoBluePill: Color @Composable get() = LocalBentoPalette.current.bluePill
val BentoDarkTile: Color @Composable get() = LocalBentoPalette.current.darkTile

// Aliases for Material 3 Color Schemes
val BalasorePrimary = BentoPrimaryBlue
val BalasoreOnPrimary = Color.White
val BalasorePrimaryContainer = RawBentoBluePill
val BalasoreOnPrimaryContainer = BentoBlueText

val BalasoreSecondary = RawBentoDarkTile
val BalasoreOnSecondary = Color.White
val BalasoreSecondaryContainer = RawBentoBlueLight
val BalasoreOnSecondaryContainer = BentoPrimaryBlue

val BalasoreTertiary = BentoAmberText
val BalasoreOnTertiary = Color.White
val BalasoreTertiaryContainer = BentoAmberBg
val BalasoreOnTertiaryContainer = BentoAmberText

val BalasoreBackground = RawBentoCanvas
val BalasoreOnBackground = RawBentoSlate900
val BalasoreSurface = RawBentoCardWhite
val BalasoreOnSurface = RawBentoSlate900
val BalasoreSurfaceVariant = RawBentoBlueLight
val BalasoreOnSurfaceVariant = RawBentoSlate500
val BalasoreOutline = RawBentoBorderHover

// Dark Theme Variants
val BalasorePrimaryDark = Color(0xFF60A5FA)
val BalasoreOnPrimaryDark = Color(0xFF0F172A)
val BalasorePrimaryContainerDark = Color(0xFF1E3A8A)
val BalasoreSecondaryDark = Color(0xFF94A3B8)
val BalasoreOnSecondaryDark = Color(0xFF0F172A)
val BalasoreTertiaryDark = Color(0xFFFBBF24)
val BalasoreOnTertiaryDark = Color(0xFF78350F)
val BalasoreBackgroundDark = Color(0xFF0B1120)
val BalasoreOnBackgroundDark = Color(0xFFF8FAFC)
val BalasoreSurfaceDark = Color(0xFF1E293B)
val BalasoreOnSurfaceDark = Color(0xFFF8FAFC)
val BalasoreSurfaceVariantDark = Color(0xFF0F172A)
val BalasoreOnSurfaceVariantDark = Color(0xFF94A3B8)
val BalasoreOutlineDark = Color(0xFF334155)

val AlertCyclone = BentoRedText
val AlertWarning = BentoAmberText
val AlertNormal = BentoGreenText
val TideReceding = BentoPrimaryBlue
val TideIncoming = BentoAmberText

