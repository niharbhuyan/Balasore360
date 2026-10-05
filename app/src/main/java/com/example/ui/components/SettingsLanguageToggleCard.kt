package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Autorenew
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material.icons.filled.Translate
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.AppLanguage
import com.example.ui.theme.AmberGold
import com.example.ui.theme.BentoPrimaryBlue
import com.example.ui.theme.BentoSlate100
import com.example.ui.theme.BentoSlate200
import com.example.ui.theme.BentoSlate400
import com.example.ui.theme.BentoSlate50
import com.example.ui.theme.BentoSlate500
import com.example.ui.theme.BentoSlate600
import com.example.ui.theme.BentoSlate700
import com.example.ui.theme.BentoSlate800
import com.example.ui.theme.BentoSlate900
import com.example.ui.theme.CoastalSkyBlue
import com.example.ui.theme.EmeraldGreen
import com.example.ui.theme.OceanBlue
import com.example.ui.theme.OceanBlueDark

/**
 * Settings Screen Language Toggle Component.
 * Enables switching between English and Odia using ViewModel state and local Android string resources.
 * Also highlights automatic updates across all features (weather, transit, tides, civic desk).
 */
@Composable
fun SettingsLanguageToggleCard(
    currentLanguage: AppLanguage,
    onLanguageChange: (AppLanguage) -> Unit,
    onToggleLanguage: () -> Unit,
    isAutoUpdateActive: Boolean = true,
    modifier: Modifier = Modifier
) {
    val isOdia = currentLanguage == AppLanguage.ODIA

    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("settings_language_toggle_card"),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        border = BorderStroke(1.dp, BentoSlate200)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            // Header: Icon, Section Title & Subtitle via Local String Resources
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Surface(
                        shape = CircleShape,
                        color = OceanBlue.copy(alpha = 0.12f),
                        modifier = Modifier.size(44.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Default.Translate,
                                contentDescription = "Language Translation",
                                tint = OceanBlue,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }

                    Column {
                        Text(
                            text = stringResource(R.string.settings_language_title),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = BentoSlate900
                        )
                        Text(
                            text = stringResource(R.string.settings_language_desc),
                            fontSize = 12.sp,
                            color = BentoSlate500,
                            lineHeight = 16.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Two-Segmented Language Switcher (English ⇄ ଓଡ଼ିଆ)
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = BentoSlate100,
                border = BorderStroke(1.dp, BentoSlate200),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(4.dp),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    // English Option Button
                    val isEnglishSelected = currentLanguage == AppLanguage.ENGLISH
                    val englishBg by animateColorAsState(
                        targetValue = if (isEnglishSelected) OceanBlue else Color.Transparent,
                        animationSpec = tween(250),
                        label = "english_bg"
                    )
                    val englishTextColor by animateColorAsState(
                        targetValue = if (isEnglishSelected) Color.White else BentoSlate700,
                        animationSpec = tween(250),
                        label = "english_text"
                    )

                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = englishBg,
                        modifier = Modifier
                            .weight(1f)
                            .height(46.dp)
                            .clickable { onLanguageChange(AppLanguage.ENGLISH) }
                            .testTag("language_switch_english")
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            if (isEnglishSelected) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = "Selected",
                                    tint = Color.White,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                            }
                            Text(
                                text = stringResource(R.string.lang_english),
                                fontWeight = if (isEnglishSelected) FontWeight.Bold else FontWeight.Medium,
                                fontSize = 14.sp,
                                color = englishTextColor
                            )
                        }
                    }

                    // Odia Option Button (ଓଡ଼ିଆ)
                    val isOdiaSelected = currentLanguage == AppLanguage.ODIA
                    val odiaBg by animateColorAsState(
                        targetValue = if (isOdiaSelected) OceanBlue else Color.Transparent,
                        animationSpec = tween(250),
                        label = "odia_bg"
                    )
                    val odiaTextColor by animateColorAsState(
                        targetValue = if (isOdiaSelected) Color.White else BentoSlate700,
                        animationSpec = tween(250),
                        label = "odia_text"
                    )

                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = odiaBg,
                        modifier = Modifier
                            .weight(1f)
                            .height(46.dp)
                            .clickable { onLanguageChange(AppLanguage.ODIA) }
                            .testTag("language_switch_odia")
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            if (isOdiaSelected) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = "Selected",
                                    tint = Color.White,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                            }
                            Text(
                                text = stringResource(R.string.lang_odia),
                                fontWeight = if (isOdiaSelected) FontWeight.Bold else FontWeight.Medium,
                                fontSize = 15.sp,
                                color = odiaTextColor
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Quick Toggle Row with Switch & Label
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(BentoSlate50)
                    .border(BorderStroke(1.dp, BentoSlate200), RoundedCornerShape(14.dp))
                    .clickable { onToggleLanguage() }
                    .padding(horizontal = 14.dp, vertical = 10.dp)
                    .testTag("language_toggle_row"),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.SwapHoriz,
                        contentDescription = "Toggle",
                        tint = OceanBlue,
                        modifier = Modifier.size(20.dp)
                    )
                    Column {
                        Text(
                            text = stringResource(R.string.current_language_label),
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = BentoSlate800
                        )
                        Text(
                            text = stringResource(R.string.language_toggle_label),
                            fontSize = 11.sp,
                            color = BentoSlate500
                        )
                    }
                }

                // Interactive Switch synced with ViewModel
                Switch(
                    checked = isOdia,
                    onCheckedChange = { onToggleLanguage() },
                    modifier = Modifier.testTag("language_toggle_switch"),
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = Color.White,
                        checkedTrackColor = OceanBlue,
                        uncheckedThumbColor = BentoSlate500,
                        uncheckedTrackColor = BentoSlate200
                    )
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Automatic Updates Live Status Banner (All features auto-updating)
            Surface(
                shape = RoundedCornerShape(14.dp),
                color = if (isAutoUpdateActive) EmeraldGreen.copy(alpha = 0.08f) else BentoSlate100,
                border = BorderStroke(
                    1.dp,
                    if (isAutoUpdateActive) EmeraldGreen.copy(alpha = 0.3f) else BentoSlate200
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("auto_updates_status_banner")
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Surface(
                        shape = CircleShape,
                        color = if (isAutoUpdateActive) EmeraldGreen.copy(alpha = 0.15f) else BentoSlate200,
                        modifier = Modifier.size(32.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Default.Sync,
                                contentDescription = "Auto Update",
                                tint = if (isAutoUpdateActive) EmeraldGreen else BentoSlate500,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }

                    Column(modifier = Modifier.weight(1f)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(6.dp)
                                    .background(if (isAutoUpdateActive) EmeraldGreen else BentoSlate400, CircleShape)
                            )
                            Text(
                                text = stringResource(R.string.auto_update_heading),
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isAutoUpdateActive) BentoSlate900 else BentoSlate700
                            )
                        }
                        Text(
                            text = stringResource(R.string.auto_update_description),
                            fontSize = 11.sp,
                            color = BentoSlate600,
                            lineHeight = 15.sp
                        )
                    }

                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = if (isAutoUpdateActive) EmeraldGreen else BentoSlate400
                    ) {
                        Text(
                            text = if (isOdia) "ସକ୍ରିୟ" else "LIVE",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }
            }
        }
    }
}
