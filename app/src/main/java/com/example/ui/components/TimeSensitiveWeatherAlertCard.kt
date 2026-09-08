package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AlertSeverity
import com.example.data.model.BalasoreWeatherAlert
import com.example.ui.theme.BentoAmberBg
import com.example.ui.theme.BentoAmberText
import com.example.ui.theme.BentoBlueLight
import com.example.ui.theme.BentoBorder
import com.example.ui.theme.BentoCardWhite
import com.example.ui.theme.BentoGreenBg
import com.example.ui.theme.BentoGreenText
import com.example.ui.theme.BentoPrimaryBlue
import com.example.ui.theme.BentoRedBg
import com.example.ui.theme.BentoRedText
import com.example.ui.theme.BentoSlate400
import com.example.ui.theme.BentoSlate500
import com.example.ui.theme.BentoSlate700
import com.example.ui.theme.BentoSlate900
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * UI Component displaying a time-sensitive weather & marine alert for the Balasore region.
 *
 * Features:
 * - Dynamic countdown to validity window expiration
 * - Interactive expand/collapse for safety guidelines & actions
 * - Clear severity color coding (SAFE, ADVISORY, WARNING, CYCLONE, TIDAL)
 * - Specific Balasore locality tags (e.g. Chandipur, Talasari, Remuna)
 * - Emergency Helpline direct action
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun TimeSensitiveWeatherAlertCard(
    alert: BalasoreWeatherAlert,
    modifier: Modifier = Modifier,
    initialExpanded: Boolean = false,
    onEmergencyCallClick: ((phoneNumber: String) -> Unit)? = null,
    onAcknowledgeAlert: ((alertId: String) -> Unit)? = null
) {
    var isExpanded by remember { mutableStateOf(initialExpanded) }
    var currentTimeMillis by remember { mutableLongStateOf(System.currentTimeMillis()) }

    // Update countdown ticker periodically every 30 seconds
    LaunchedEffect(Unit) {
        while (true) {
            delay(30_000L)
            currentTimeMillis = System.currentTimeMillis()
        }
    }

    val isUrgent = alert.severity.isUrgent
    val isExpired = alert.isExpired(currentTimeMillis)
    val isActive = alert.isActive(currentTimeMillis)

    val timeRemainingText = alert.getFormattedTimeRemaining(currentTimeMillis)

    // Calculate progression percentage through the alert's active validity window
    val totalWindowDuration = (alert.validUntilMillis - alert.validFromMillis).coerceAtLeast(1L)
    val elapsedDuration = (currentTimeMillis - alert.validFromMillis).coerceIn(0L, totalWindowDuration)
    val progressFraction = (elapsedDuration.toFloat() / totalWindowDuration.toFloat()).coerceIn(0f, 1f)

    val borderStroke = when {
        isExpired -> BorderStroke(1.dp, BentoBorder)
        alert.severity == AlertSeverity.CYCLONE_ALERT -> BorderStroke(1.5.dp, BentoRedText.copy(alpha = 0.5f))
        alert.severity == AlertSeverity.WARNING -> BorderStroke(1.5.dp, BentoAmberText.copy(alpha = 0.5f))
        else -> BorderStroke(1.dp, BentoBorder)
    }

    val containerBg = when {
        isExpired -> BentoCardWhite.copy(alpha = 0.85f)
        alert.severity == AlertSeverity.CYCLONE_ALERT -> Color(0xFFFFF5F5)
        alert.severity == AlertSeverity.WARNING -> Color(0xFFFFFDF5)
        alert.severity == AlertSeverity.HIGH_TIDE -> Color(0xFFF0F9FF)
        else -> BentoCardWhite
    }

    Card(
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = containerBg),
        border = borderStroke,
        elevation = CardDefaults.cardElevation(defaultElevation = if (isUrgent && !isExpired) 2.dp else 1.dp),
        modifier = modifier
            .fillMaxWidth()
            .testTag("time_sensitive_weather_alert_${alert.id}")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp)
        ) {
            // Header Row: Severity Badge + Countdown Ticker
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Severity Badge with Icon
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = alert.severity.badgeBg,
                    border = BorderStroke(1.dp, alert.severity.badgeText.copy(alpha = 0.25f))
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(
                            text = alert.category.emoji,
                            fontSize = 14.sp
                        )
                        Text(
                            text = alert.severity.levelName,
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.ExtraBold,
                                letterSpacing = 0.6.sp
                            ),
                            color = alert.severity.badgeText
                        )
                    }
                }

                // Time validity pill
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = if (isUrgent && !isExpired) BentoRedBg.copy(alpha = 0.7f) else BentoBlueLight,
                    border = BorderStroke(0.5.dp, BentoBorder)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.AccessTime,
                            contentDescription = "Alert validity",
                            tint = if (isUrgent && !isExpired) BentoRedText else BentoPrimaryBlue,
                            modifier = Modifier.size(13.dp)
                        )
                        Text(
                            text = timeRemainingText,
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 10.sp
                            ),
                            color = if (isUrgent && !isExpired) BentoRedText else BentoPrimaryBlue
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Alert Title + Odia Title
            Column {
                Text(
                    text = alert.title,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        lineHeight = 22.sp
                    ),
                    color = BentoSlate900
                )
                if (alert.odiaTitle.isNotBlank()) {
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = alert.odiaTitle,
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontWeight = FontWeight.Medium,
                            fontSize = 12.sp
                        ),
                        color = BentoSlate500
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Concise Summary Description
            Text(
                text = alert.summary,
                style = MaterialTheme.typography.bodyMedium.copy(
                    lineHeight = 20.sp
                ),
                color = BentoSlate700
            )

            // Active Window Timeframe Progress Indicator
            if (isActive && !isExpired) {
                Spacer(modifier = Modifier.height(14.dp))
                Column(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        val timeFormat = remember { SimpleDateFormat("h:mm a, dd MMM", Locale.getDefault()) }
                        Text(
                            text = "Starts: ${timeFormat.format(Date(alert.validFromMillis))}",
                            style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                            color = BentoSlate400
                        )
                        Text(
                            text = "Ends: ${timeFormat.format(Date(alert.validUntilMillis))}",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontSize = 10.sp,
                                fontWeight = FontWeight.SemiBold
                            ),
                            color = BentoSlate500
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    LinearProgressIndicator(
                        progress = { progressFraction },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(5.dp)
                            .clip(RoundedCornerShape(3.dp)),
                        color = if (isUrgent) BentoRedText else BentoPrimaryBlue,
                        trackColor = BentoBorder,
                    )
                }
            }

            // Affected Balasore Zones Chips
            if (alert.affectedZones.isNotEmpty()) {
                Spacer(modifier = Modifier.height(12.dp))
                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    alert.affectedZones.forEach { zone ->
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = BentoBlueLight,
                            border = BorderStroke(1.dp, BentoBorder)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.LocationOn,
                                    contentDescription = null,
                                    tint = BentoPrimaryBlue,
                                    modifier = Modifier.size(11.dp)
                                )
                                Text(
                                    text = zone.zoneName,
                                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                                    color = BentoSlate700
                                )
                            }
                        }
                    }
                }
            }

            // Expand / Collapse Actionable Protocols Button
            Spacer(modifier = Modifier.height(12.dp))
            HorizontalDivider(color = BentoBorder, thickness = 1.dp)
            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .clickable { isExpanded = !isExpanded }
                    .padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Shield,
                        contentDescription = null,
                        tint = if (isUrgent) BentoRedText else BentoPrimaryBlue,
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = if (isExpanded) "Hide Safety Guidelines" else "View Safety Protocols (${alert.actionableInstructions.size} steps)",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = if (isUrgent) BentoRedText else BentoPrimaryBlue
                        )
                    )
                }

                Icon(
                    imageVector = if (isExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                    contentDescription = if (isExpanded) "Collapse" else "Expand",
                    tint = BentoSlate500,
                    modifier = Modifier.size(20.dp)
                )
            }

            // Expanded Details Section
            AnimatedVisibility(
                visible = isExpanded,
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp)
                ) {
                    // Detailed advisory narrative
                    if (alert.detailedDescription.isNotBlank()) {
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = BentoCardWhite,
                            border = BorderStroke(1.dp, BentoBorder),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Text(
                                    text = "Meteorological Analysis",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = BentoSlate900
                                    )
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = alert.detailedDescription,
                                    style = MaterialTheme.typography.bodySmall.copy(lineHeight = 18.sp),
                                    color = BentoSlate700
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(10.dp))
                    }

                    // Actionable Safety Checkpoints
                    if (alert.actionableInstructions.isNotEmpty()) {
                        Text(
                            text = "ACTIONABLE SAFETY PROTOCOLS",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.ExtraBold,
                                letterSpacing = 1.sp,
                                fontSize = 9.sp
                            ),
                            color = BentoSlate500
                        )
                        Spacer(modifier = Modifier.height(6.dp))

                        alert.actionableInstructions.forEachIndexed { index, instruction ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 3.dp),
                                verticalAlignment = Alignment.Top,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Surface(
                                    shape = CircleShape,
                                    color = if (isUrgent) BentoRedBg else BentoBlueLight,
                                    modifier = Modifier.size(18.dp)
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Text(
                                            text = "${index + 1}",
                                            style = MaterialTheme.typography.labelSmall.copy(
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 9.sp
                                            ),
                                            color = if (isUrgent) BentoRedText else BentoPrimaryBlue
                                        )
                                    }
                                }
                                Text(
                                    text = instruction,
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        lineHeight = 17.sp,
                                        fontWeight = FontWeight.Medium
                                    ),
                                    color = BentoSlate900,
                                    modifier = Modifier.weight(1f)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                    }

                    // Authority Source & Emergency Contact Row
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = BentoBlueLight.copy(alpha = 0.7f),
                        border = BorderStroke(1.dp, BentoBorder),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(10.dp)) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Info,
                                    contentDescription = null,
                                    tint = BentoSlate500,
                                    modifier = Modifier.size(13.dp)
                                )
                                Text(
                                    text = "Issued by: ${alert.issuingAuthority}",
                                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                                    color = BentoSlate500
                                )
                            }

                            if (alert.emergencyContact.isNotBlank()) {
                                Spacer(modifier = Modifier.height(6.dp))
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Phone,
                                            contentDescription = null,
                                            tint = BentoPrimaryBlue,
                                            modifier = Modifier.size(13.dp)
                                        )
                                        Text(
                                            text = alert.emergencyContact,
                                            style = MaterialTheme.typography.labelSmall.copy(
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 10.sp
                                            ),
                                            color = BentoSlate700
                                        )
                                    }

                                    if (onEmergencyCallClick != null) {
                                        Surface(
                                            shape = RoundedCornerShape(6.dp),
                                            color = BentoPrimaryBlue,
                                            modifier = Modifier
                                                .clickable { onEmergencyCallClick("06782-262244") }
                                                .padding(horizontal = 8.dp, vertical = 4.dp)
                                        ) {
                                            Text(
                                                text = "Call Control",
                                                style = MaterialTheme.typography.labelSmall.copy(
                                                    fontWeight = FontWeight.Bold,
                                                    fontSize = 9.sp
                                                ),
                                                color = Color.White
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
