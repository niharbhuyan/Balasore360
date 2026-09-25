package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(
    primary = OceanBlue,
    onPrimary = Color.White,
    primaryContainer = Color(0xFFE0F2FE),
    onPrimaryContainer = OceanBlueDark,
    secondary = CoralOrange,
    onSecondary = Color.White,
    background = BentoSlate50,
    onBackground = BentoSlate900,
    surface = Color.White,
    onSurface = BentoSlate900,
    surfaceVariant = BentoSlate100,
    onSurfaceVariant = BentoSlate700
)

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF38BDF8),
    onPrimary = BentoSlate900,
    primaryContainer = OceanBlueDark,
    onPrimaryContainer = Color(0xFFE0F2FE),
    secondary = CoralOrange,
    onSecondary = Color.White,
    background = BentoSlate900,
    onBackground = BentoSlate50,
    surface = BentoSlate800,
    onSurface = BentoSlate50,
    surfaceVariant = BentoSlate700,
    onSurfaceVariant = BentoSlate200
)

/**
 * High-Contrast OLED Dark Color Scheme for night-time reading.
 * Uses pure pitch black (#000000) for zero screen glow and maximum OLED power efficiency,
 * crisp #FFFFFF typography for maximum readability, and warm amber/soothing cyan accents
 * to significantly reduce eye strain during prolonged night reading.
 */
private val HighContrastDarkColorScheme = darkColorScheme(
    primary = Color(0xFF38BDF8),
    onPrimary = Color.Black,
    primaryContainer = Color(0xFF075985),
    onPrimaryContainer = Color(0xFFE0F2FE),
    secondary = Color(0xFFFBBF24), // Soothing warm amber for zero blue-light fatigue
    onSecondary = Color.Black,
    secondaryContainer = Color(0xFF78350F),
    onSecondaryContainer = Color(0xFFFEF3C7),
    background = Color(0xFF000000), // AMOLED Pitch Black
    onBackground = Color(0xFFFFFFFF), // 21:1 Contrast
    surface = Color(0xFF0B1120), // Deep contrasting surface
    onSurface = Color(0xFFFFFFFF),
    surfaceVariant = Color(0xFF1E293B),
    onSurfaceVariant = Color(0xFFF1F5F9),
    outline = Color(0xFF64748B),
    outlineVariant = Color(0xFF334155)
)

data class BentoPalette(
    val isDark: Boolean = false,
    val isHighContrast: Boolean = false,
    val gold: Color = BentoGold,
    val tideIncoming: Color = TideIncoming,
    val tideReceding: Color = TideReceding,
    val cardBackground: Color = if (isDark) {
        if (isHighContrast) Color(0xFF0F172A) else BentoSlate800
    } else Color.White,
    val cardBorder: Color = if (isDark) {
        if (isHighContrast) Color(0xFF38BDF8).copy(alpha = 0.4f) else BentoSlate700
    } else BentoBorder,
    val textPrimary: Color = if (isDark) Color(0xFFFFFFFF) else BentoSlate900,
    val textSecondary: Color = if (isDark) Color(0xFFCBD5E1) else BentoSlate600,
    val textMuted: Color = if (isDark) Color(0xFF94A3B8) else BentoSlate500,
    val nightHighlightBg: Color = if (isHighContrast) Color(0xFF172554) else if (isDark) Color(0xFF1E293B) else Color(0xFFE0F2FE)
)

val LocalBentoPalette = androidx.compose.runtime.staticCompositionLocalOf { BentoPalette() }

@Composable
fun Balasore360Theme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    isHighContrast: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        isHighContrast -> HighContrastDarkColorScheme
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }
    val bentoPalette = BentoPalette(
        isDark = darkTheme || isHighContrast,
        isHighContrast = isHighContrast
    )

    androidx.compose.runtime.CompositionLocalProvider(LocalBentoPalette provides bentoPalette) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            content = content
        )
    }
}
