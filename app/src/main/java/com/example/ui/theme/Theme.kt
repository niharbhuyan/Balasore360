package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.platform.LocalContext
import com.example.ui.viewmodel.ThemeMode

private val DarkColorScheme = darkColorScheme(
    primary = BalasorePrimaryDark,
    onPrimary = BalasoreOnPrimaryDark,
    primaryContainer = BalasorePrimaryContainerDark,
    secondary = BalasoreSecondaryDark,
    onSecondary = BalasoreOnSecondaryDark,
    tertiary = BalasoreTertiaryDark,
    onTertiary = BalasoreOnTertiaryDark,
    background = BalasoreBackgroundDark,
    onBackground = BalasoreOnBackgroundDark,
    surface = BalasoreSurfaceDark,
    onSurface = BalasoreOnSurfaceDark,
    surfaceVariant = BalasoreSurfaceVariantDark,
    onSurfaceVariant = BalasoreOnSurfaceVariantDark,
    outline = BalasoreOutlineDark
)

private val LightColorScheme = lightColorScheme(
    primary = BalasorePrimary,
    onPrimary = BalasoreOnPrimary,
    primaryContainer = BalasorePrimaryContainer,
    onPrimaryContainer = BalasoreOnPrimaryContainer,
    secondary = BalasoreSecondary,
    onSecondary = BalasoreOnSecondary,
    secondaryContainer = BalasoreSecondaryContainer,
    onSecondaryContainer = BalasoreOnSecondaryContainer,
    tertiary = BalasoreTertiary,
    onTertiary = BalasoreOnTertiary,
    tertiaryContainer = BalasoreTertiaryContainer,
    onTertiaryContainer = BalasoreOnTertiaryContainer,
    background = BalasoreBackground,
    onBackground = BalasoreOnBackground,
    surface = BalasoreSurface,
    onSurface = BalasoreOnSurface,
    surfaceVariant = BalasoreSurfaceVariant,
    onSurfaceVariant = BalasoreOnSurfaceVariant,
    outline = BalasoreOutline
)

@Composable
fun MyApplicationTheme(
    themeMode: ThemeMode = ThemeMode.SYSTEM,
    darkTheme: Boolean = when (themeMode) {
        ThemeMode.LIGHT -> false
        ThemeMode.DARK -> true
        ThemeMode.SYSTEM -> isSystemInDarkTheme()
    },
    dynamicColor: Boolean = false, // Prefer tailored Balasore branding
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    val bentoPalette = if (darkTheme) DarkBentoPalette else LightBentoPalette

    CompositionLocalProvider(LocalBentoPalette provides bentoPalette) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            content = content
        )
    }
}

