package com.example.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Translate
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AppLanguage

/**
 * High-visibility, dynamic interactive Language Toggle Switch.
 * Instantly flips UI strings, descriptions, headings, and menus
 * between English and Odia (ଓଡ଼ିଆ) without app restart.
 */
@Composable
fun LanguageToggleSwitch(
    currentLanguage: AppLanguage,
    onLanguageSelected: (AppLanguage) -> Unit,
    modifier: Modifier = Modifier,
    compact: Boolean = true
) {
    val isOdia = currentLanguage == AppLanguage.ODIA
    val interactionSource = remember { MutableInteractionSource() }

    Surface(
        shape = RoundedCornerShape(24.dp),
        color = MaterialTheme.colorScheme.surfaceVariant,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)),
        tonalElevation = 2.dp,
        modifier = modifier
            .clip(RoundedCornerShape(24.dp))
            .testTag("dynamic_language_toggle_switch")
    ) {
        Row(
            modifier = Modifier
                .padding(2.dp)
                .wrapContentHeight(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            // English Button
            LanguageSegmentButton(
                label = if (compact) "EN" else "English",
                subLabel = if (!compact) "EN" else null,
                iconText = "🇺🇸",
                isSelected = !isOdia,
                onClick = { onLanguageSelected(AppLanguage.ENGLISH) },
                compact = compact,
                testTag = "lang_btn_en"
            )

            // Odia (ଓଡ଼ିଆ) Button
            LanguageSegmentButton(
                label = if (compact) "ଓଡ଼ିଆ" else "ଓଡ଼ିଆ (Odia)",
                subLabel = if (!compact) "OD" else null,
                iconText = "🇮🇳",
                isSelected = isOdia,
                onClick = { onLanguageSelected(AppLanguage.ODIA) },
                compact = compact,
                testTag = "lang_btn_od"
            )
        }
    }
}

@Composable
private fun LanguageSegmentButton(
    label: String,
    subLabel: String?,
    iconText: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    compact: Boolean,
    testTag: String
) {
    val backgroundColor by animateColorAsState(
        targetValue = if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent,
        label = "lang_bg_anim"
    )
    val contentColor by animateColorAsState(
        targetValue = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant,
        label = "lang_text_anim"
    )

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(backgroundColor)
            .clickable { onClick() }
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
            Text(
                text = label,
                style = MaterialTheme.typography.labelMedium.copy(
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                    color = contentColor,
                    fontSize = if (compact) 11.sp else 13.sp
                )
            )
        }
    }
}

/**
 * Large banner style toggle for settings or header cards
 */
@Composable
fun LanguageSwitcherBannerCard(
    currentLanguage: AppLanguage,
    onLanguageSelected: (AppLanguage) -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.25f)),
        modifier = modifier
            .fillMaxWidth()
            .testTag("language_switcher_banner_card")
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 10.dp),
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
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primary),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Translate,
                        contentDescription = "Language",
                        tint = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.size(20.dp)
                    )
                }
                Column {
                    Text(
                        text = if (currentLanguage == AppLanguage.ODIA) "ଭାଷା ପରିବର୍ତ୍ତନ (Dynamic Language)" else "App Language / ଭାଷା ଚୟନ",
                        style = MaterialTheme.typography.labelLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    )
                    Text(
                        text = if (currentLanguage == AppLanguage.ODIA) "ତୁରନ୍ତ ଇଂରାଜୀ ଏବଂ ଓଡ଼ିଆ ମଧ୍ୟରେ ପରିବର୍ତ୍ତନ କରନ୍ତୁ" else "Switch dynamically between English & Odia",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.width(8.dp))

            LanguageToggleSwitch(
                currentLanguage = currentLanguage,
                onLanguageSelected = onLanguageSelected,
                compact = true
            )
        }
    }
}
