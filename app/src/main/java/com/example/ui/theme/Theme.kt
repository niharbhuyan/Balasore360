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

@Composable
fun Balasore360Theme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
