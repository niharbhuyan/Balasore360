package com.example.ui.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material.icons.filled.Translate
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AppLanguage
import com.example.ui.theme.OceanBlue
import com.example.ui.theme.SlateGray

import android.content.res.Configuration
import androidx.compose.ui.platform.LocalConfiguration
import java.util.Locale

/**
 * CompositionLocal providing the current application language across the Compose hierarchy.
 * Default is [AppLanguage.ENGLISH].
 */
val LocalAppLanguage = compositionLocalOf { AppLanguage.ENGLISH }

/**
 * CompositionLocal providing a callback to update the application language.
 */
val LocalAppLanguageUpdater = compositionLocalOf<(AppLanguage) -> Unit> { {} }

/**
 * CompositionLocal providing a quick toggle callback (English ⇄ Odia).
 */
val LocalAppLanguageToggle = compositionLocalOf<() -> Unit> { {} }

/**
 * Provides the application locale/language state to downstream composables via [CompositionLocalProvider].
 * Enables any child composable to access [LocalAppLanguage.current] and dynamic toggling without prop-drilling.
 */
@Composable
fun ProvideAppLanguage(
    currentLanguage: AppLanguage,
    onLanguageChange: (AppLanguage) -> Unit,
    content: @Composable () -> Unit
) {
    val currentConfig = LocalConfiguration.current
    val localizedConfig = remember(currentLanguage, currentConfig) {
        val config = Configuration(currentConfig)
        val targetLocale = if (currentLanguage == AppLanguage.ODIA) Locale("or") else Locale.ENGLISH
        Locale.setDefault(targetLocale)
        config.setLocale(targetLocale)
        config
    }

    val toggleAction = remember(currentLanguage, onLanguageChange) {
        {
            val nextLanguage = if (currentLanguage == AppLanguage.ODIA) {
                AppLanguage.ENGLISH
            } else {
                AppLanguage.ODIA
            }
            onLanguageChange(nextLanguage)
        }
    }

    CompositionLocalProvider(
        LocalConfiguration provides localizedConfig,
        LocalAppLanguage provides currentLanguage,
        LocalAppLanguageUpdater provides onLanguageChange,
        LocalAppLanguageToggle provides toggleAction,
        content = content
    )
}

/**
 * Visual presentation style for the [LanguageSwitcher] component.
 */
enum class LanguageSwitcherStyle {
    /** Pill button showing current language with animated toggle action */
    BUTTON,
    /** Ultra-compact pill suitable for top app bars */
    COMPACT_PILL,
    /** Two-segment switch showing both English and Odia options */
    SEGMENTED,
    /** Elevated Chip style with border */
    CHIP,
    /** Compact icon button with language badge */
    ICON_BADGE
}

/**
 * Primary `LanguageSwitcher` composable button.
 * Toggles the app's internal locale state between English and Odia (ଓଡ଼ିଆ) dynamically.
 *
 * Can be used with [CompositionLocalProvider] ([LocalAppLanguage] and [LocalAppLanguageToggle])
 * or configured directly with [currentLanguage] and [onLanguageChanged].
 *
 * @param modifier Modifier for styling and layout positioning
 * @param currentLanguage The active language, defaults to [LocalAppLanguage.current]
 * @param onLanguageChanged Optional callback when language changes; if null, invokes [LocalAppLanguageToggle.current]
 * @param style Presentation style variant (BUTTON, COMPACT_PILL, SEGMENTED, CHIP, ICON_BADGE)
 * @param compact Compact sizing for top bars and dense toolbars
 * @param showIcon Whether to display the globe/translation icon
 */
@Composable
fun LanguageSwitcher(
    modifier: Modifier = Modifier,
    currentLanguage: AppLanguage = LocalAppLanguage.current,
    onLanguageChanged: ((AppLanguage) -> Unit)? = null,
    style: LanguageSwitcherStyle = LanguageSwitcherStyle.BUTTON,
    compact: Boolean = false,
    showIcon: Boolean = true
) {
    val localUpdater = LocalAppLanguageUpdater.current
    val localToggle = LocalAppLanguageToggle.current
    val haptic = LocalHapticFeedback.current

    val handleToggle: () -> Unit = {
        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
        val nextLanguage = if (currentLanguage == AppLanguage.ODIA) {
            AppLanguage.ENGLISH
        } else {
            AppLanguage.ODIA
        }

        if (onLanguageChanged != null) {
            onLanguageChanged(nextLanguage)
        } else {
            localToggle()
        }
    }

    val handleSelect: (AppLanguage) -> Unit = { selectedLang ->
        haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
        if (onLanguageChanged != null) {
            onLanguageChanged(selectedLang)
        } else {
            localUpdater(selectedLang)
        }
    }

    when (style) {
        LanguageSwitcherStyle.BUTTON -> {
            LanguageSwitcherButton(
                currentLanguage = currentLanguage,
                onToggle = handleToggle,
                compact = compact,
                showIcon = showIcon,
                modifier = modifier
            )
        }

        LanguageSwitcherStyle.COMPACT_PILL -> {
            LanguageSwitcherCompactPill(
                currentLanguage = currentLanguage,
                onToggle = handleToggle,
                modifier = modifier
            )
        }

        LanguageSwitcherStyle.SEGMENTED -> {
            LanguageSwitcherSegmented(
                currentLanguage = currentLanguage,
                onSelect = handleSelect,
                compact = compact,
                modifier = modifier
            )
        }

        LanguageSwitcherStyle.CHIP -> {
            LanguageSwitcherChip(
                currentLanguage = currentLanguage,
                onToggle = handleToggle,
                showIcon = showIcon,
                modifier = modifier
            )
        }

        LanguageSwitcherStyle.ICON_BADGE -> {
            LanguageSwitcherIconBadge(
                currentLanguage = currentLanguage,
                onToggle = handleToggle,
                modifier = modifier
            )
        }
    }
}

/**
 * State-based overload for explicit state configuration.
 */
@Composable
fun LanguageSwitcher(
    currentLanguage: AppLanguage,
    onLanguageSelected: (AppLanguage) -> Unit,
    modifier: Modifier = Modifier,
    compact: Boolean = false,
    style: LanguageSwitcherStyle = LanguageSwitcherStyle.BUTTON
) {
    LanguageSwitcher(
        modifier = modifier,
        currentLanguage = currentLanguage,
        onLanguageChanged = onLanguageSelected,
        style = style,
        compact = compact
    )
}

/**
 * Simple toggle-based overload for explicit state configuration.
 */
@Composable
fun LanguageSwitcher(
    currentLanguage: AppLanguage,
    onToggle: () -> Unit,
    modifier: Modifier = Modifier,
    compact: Boolean = false,
    style: LanguageSwitcherStyle = LanguageSwitcherStyle.BUTTON
) {
    LanguageSwitcher(
        modifier = modifier,
        currentLanguage = currentLanguage,
        onLanguageChanged = { onToggle() },
        style = style,
        compact = compact
    )
}

/**
 * Material 3 Interactive Button variant for `LanguageSwitcher`.
 */
@Composable
private fun LanguageSwitcherButton(
    currentLanguage: AppLanguage,
    onToggle: () -> Unit,
    compact: Boolean,
    showIcon: Boolean,
    modifier: Modifier = Modifier
) {
    val isOdia = currentLanguage == AppLanguage.ODIA
    val nextLanguageLabel = if (isOdia) "English" else "ଓଡ଼ିଆ"
    val currentLabel = if (isOdia) "ଓଡ଼ିଆ" else "EN"

    val backgroundColor by animateColorAsState(
        targetValue = if (isOdia) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant,
        animationSpec = spring(stiffness = Spring.StiffnessMediumLow),
        label = "lang_btn_bg"
    )

    val contentColor by animateColorAsState(
        targetValue = if (isOdia) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurfaceVariant,
        animationSpec = spring(stiffness = Spring.StiffnessMediumLow),
        label = "lang_btn_content"
    )

    val borderColor by animateColorAsState(
        targetValue = if (isOdia) MaterialTheme.colorScheme.primary.copy(alpha = 0.5f) else MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.6f),
        label = "lang_btn_border"
    )

    Surface(
        onClick = onToggle,
        shape = RoundedCornerShape(20.dp),
        color = backgroundColor,
        border = BorderStroke(1.dp, borderColor),
        tonalElevation = if (isOdia) 2.dp else 1.dp,
        modifier = modifier
            .defaultMinSize(minWidth = 48.dp, minHeight = 48.dp)
            .testTag("language_switcher_button")
            .semantics {
                role = Role.Button
                contentDescription = if (isOdia) {
                    "Language switcher: Odia active. Tap to switch to English"
                } else {
                    "Language switcher: English active. Tap to switch to Odia (ଓଡ଼ିଆ)"
                }
            }
    ) {
        Row(
            modifier = Modifier
                .padding(
                    horizontal = if (compact) 10.dp else 14.dp,
                    vertical = if (compact) 6.dp else 8.dp
                ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            if (showIcon) {
                Icon(
                    imageVector = Icons.Default.Translate,
                    contentDescription = null,
                    tint = contentColor,
                    modifier = Modifier.size(if (compact) 15.dp else 18.dp)
                )
            }

            // Current Active Language Text with vertical slide transition
            AnimatedContent(
                targetState = currentLanguage,
                transitionSpec = {
                    (slideInVertically { height -> height } + fadeIn(tween(200)))
                        .togetherWith(slideOutVertically { height -> -height } + fadeOut(tween(200)))
                },
                label = "lang_text_anim"
            ) { targetLang ->
                Text(
                    text = if (targetLang == AppLanguage.ODIA) "ଓଡ଼ିଆ" else "English",
                    style = MaterialTheme.typography.labelMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = contentColor,
                        fontSize = if (compact) 11.5.sp else 13.sp
                    )
                )
            }

            // Swap icon showing toggle hint
            Icon(
                imageVector = Icons.Default.SwapHoriz,
                contentDescription = null,
                tint = contentColor.copy(alpha = 0.7f),
                modifier = Modifier.size(if (compact) 13.dp else 15.dp)
            )

            // Next Language Preview Badge
            Surface(
                shape = RoundedCornerShape(10.dp),
                color = if (isOdia) MaterialTheme.colorScheme.primary.copy(alpha = 0.15f) else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.08f)
            ) {
                Text(
                    text = nextLanguageLabel,
                    fontSize = if (compact) 9.5.sp else 10.5.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = contentColor,
                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                )
            }
        }
    }
}

/**
 * Compact pill switcher designed for dense TopAppBars.
 */
@Composable
private fun LanguageSwitcherCompactPill(
    currentLanguage: AppLanguage,
    onToggle: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isOdia = currentLanguage == AppLanguage.ODIA

    Surface(
        onClick = onToggle,
        shape = RoundedCornerShape(16.dp),
        color = if (isOdia) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
        border = BorderStroke(
            1.dp,
            if (isOdia) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
        ),
        tonalElevation = 2.dp,
        modifier = modifier
            .defaultMinSize(minWidth = 48.dp, minHeight = 48.dp)
            .testTag("language_switcher_compact_pill")
            .semantics {
                role = Role.Button
                contentDescription = "Toggle language between English and Odia. Currently ${if (isOdia) "Odia" else "English"}"
            }
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Language,
                contentDescription = null,
                tint = if (isOdia) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(14.dp)
            )

            AnimatedContent(
                targetState = currentLanguage,
                transitionSpec = {
                    fadeIn(tween(150)).togetherWith(fadeOut(tween(150)))
                },
                label = "compact_lang_anim"
            ) { targetLang ->
                Text(
                    text = if (targetLang == AppLanguage.ODIA) "ଓଡ଼ିଆ" else "EN",
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.ExtraBold,
                        color = if (isOdia) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 11.sp
                    )
                )
            }
        }
    }
}

/**
 * Segmented switch variant showing two options side-by-side with animated sliding indicator.
 */
@Composable
private fun LanguageSwitcherSegmented(
    currentLanguage: AppLanguage,
    onSelect: (AppLanguage) -> Unit,
    compact: Boolean,
    modifier: Modifier = Modifier
) {
    val isOdia = currentLanguage == AppLanguage.ODIA

    Surface(
        shape = RoundedCornerShape(24.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.7f),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f)),
        tonalElevation = 1.dp,
        modifier = modifier
            .clip(RoundedCornerShape(24.dp))
            .testTag("language_switcher_segmented")
    ) {
        Row(
            modifier = Modifier.padding(3.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            // English Option
            SegmentItem(
                label = if (compact) "EN" else "English",
                isSelected = !isOdia,
                onClick = { onSelect(AppLanguage.ENGLISH) },
                compact = compact,
                testTag = "lang_segment_en"
            )

            // Odia (ଓଡ଼ିଆ) Option
            SegmentItem(
                label = if (compact) "ଓଡ଼ିଆ" else "ଓଡ଼ିଆ (Odia)",
                isSelected = isOdia,
                onClick = { onSelect(AppLanguage.ODIA) },
                compact = compact,
                testTag = "lang_segment_od"
            )
        }
    }
}

@Composable
private fun SegmentItem(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    compact: Boolean,
    testTag: String
) {
    val bgColor by animateColorAsState(
        targetValue = if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent,
        animationSpec = spring(stiffness = Spring.StiffnessMediumLow),
        label = "segment_bg"
    )

    val textColor by animateColorAsState(
        targetValue = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant,
        animationSpec = spring(stiffness = Spring.StiffnessMediumLow),
        label = "segment_text"
    )

    Box(
        modifier = Modifier
            .defaultMinSize(minWidth = if (compact) 36.dp else 48.dp, minHeight = if (compact) 32.dp else 38.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(bgColor)
            .clickable(onClick = onClick)
            .padding(
                horizontal = if (compact) 8.dp else 12.dp,
                vertical = if (compact) 4.dp else 6.dp
            )
            .testTag(testTag),
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            if (isSelected) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = null,
                    tint = textColor,
                    modifier = Modifier.size(11.dp)
                )
            }
            Text(
                text = label,
                style = MaterialTheme.typography.labelMedium.copy(
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                    color = textColor,
                    fontSize = if (compact) 11.sp else 12.5.sp
                )
            )
        }
    }
}

/**
 * Filter Chip style variant for language switching.
 */
@Composable
private fun LanguageSwitcherChip(
    currentLanguage: AppLanguage,
    onToggle: () -> Unit,
    showIcon: Boolean,
    modifier: Modifier = Modifier
) {
    val isOdia = currentLanguage == AppLanguage.ODIA

    FilterChip(
        selected = isOdia,
        onClick = onToggle,
        label = {
            Text(
                text = if (isOdia) "Language: ଓଡ଼ିଆ" else "Language: English",
                fontWeight = FontWeight.SemiBold,
                fontSize = 12.sp
            )
        },
        leadingIcon = if (showIcon) {
            {
                Icon(
                    imageVector = Icons.Default.Translate,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp)
                )
            }
        } else null,
        trailingIcon = {
            Icon(
                imageVector = Icons.Default.SwapHoriz,
                contentDescription = null,
                modifier = Modifier.size(14.dp)
            )
        },
        modifier = modifier
            .defaultMinSize(minHeight = 48.dp)
            .testTag("language_switcher_chip")
    )
}

/**
 * Minimalist icon button with localized badge.
 */
@Composable
private fun LanguageSwitcherIconBadge(
    currentLanguage: AppLanguage,
    onToggle: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isOdia = currentLanguage == AppLanguage.ODIA

    IconButton(
        onClick = onToggle,
        modifier = modifier
            .defaultMinSize(minWidth = 48.dp, minHeight = 48.dp)
            .testTag("language_switcher_icon_badge")
            .semantics {
                contentDescription = "Switch language. Currently ${if (isOdia) "Odia" else "English"}"
            }
    ) {
        BadgedBox(
            badge = {
                Badge(
                    containerColor = if (isOdia) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary,
                    contentColor = if (isOdia) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSecondary
                ) {
                    Text(
                        text = if (isOdia) "ଓଡ଼ି" else "EN",
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        ) {
            Icon(
                imageVector = Icons.Default.Translate,
                contentDescription = "Translate",
                tint = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

/**
 * Large banner style card with title, description, and the `LanguageSwitcher` button.
 * Perfect for Settings, Profile, or Essentials screens.
 */
@Composable
fun LanguageSwitcherBanner(
    modifier: Modifier = Modifier,
    currentLanguage: AppLanguage = LocalAppLanguage.current,
    onLanguageSelected: ((AppLanguage) -> Unit)? = null
) {
    val localUpdater = LocalAppLanguageUpdater.current
    val isOdia = currentLanguage == AppLanguage.ODIA

    Surface(
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.35f),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.25f)),
        modifier = modifier
            .fillMaxWidth()
            .testTag("language_switcher_banner")
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.weight(1f)
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primary),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Translate,
                        contentDescription = "Language",
                        tint = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.size(22.dp)
                    )
                }

                Column {
                    Text(
                        text = if (isOdia) "ଭାଷା ପରିବର୍ତ୍ତନ (Language)" else "App Language / ଭାଷା ଚୟନ",
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    )
                    Text(
                        text = if (isOdia) "ତୁରନ୍ତ ଇଂରାଜୀ ଏବଂ ଓଡ଼ିଆ ମଧ୍ୟରେ ପରିବର୍ତ୍ତନ କରନ୍ତୁ" else "Dynamically toggle UI between English & Odia",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontSize = 11.5.sp
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.width(8.dp))

            LanguageSwitcher(
                currentLanguage = currentLanguage,
                onLanguageChanged = onLanguageSelected ?: localUpdater,
                style = LanguageSwitcherStyle.BUTTON,
                compact = true
            )
        }
    }
}
