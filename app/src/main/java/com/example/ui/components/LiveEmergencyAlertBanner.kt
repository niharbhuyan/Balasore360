package com.example.ui.components

import android.content.Intent
import android.net.Uri
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.TextButton
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Emergency
import androidx.compose.material.icons.filled.Flood
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.filled.Waves
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.remote.EmergencyAlertDto

/**
 * High-priority composable UI banner that displays live emergency alerts
 * (e.g., cyclone warnings or river water level spikes) fetched from the backend,
 * with animated transitions and dismiss action.
 */
@Composable
fun LiveEmergencyAlertBanner(
    alerts: List<EmergencyAlertDto>,
    dismissedAlertIds: Set<String>,
    onDismissAlert: (String) -> Unit,
    onOpenShelters: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val activeAlerts = alerts.filterNot { it.id in dismissedAlertIds }

    if (activeAlerts.isEmpty()) return

    Column(
        modifier = modifier
            .fillMaxWidth()
            .testTag("live_emergency_alerts_container")
            .animateContentSize(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        activeAlerts.forEach { alert ->
            EmergencyAlertCard(
                alert = alert,
                onDismiss = { onDismissAlert(alert.id) },
                onOpenShelters = onOpenShelters
            )
        }
    }
}

@Composable
fun EmergencyAlertCard(
    alert: EmergencyAlertDto,
    onDismiss: () -> Unit,
    onOpenShelters: () -> Unit = {}
) {
    val context = LocalContext.current
    var isExpanded by remember { mutableStateOf(false) }
    var showDetailsDialog by remember { mutableStateOf(false) }

    val isCyclone = alert.type.contains("CYCLONE", ignoreCase = true)
    val isRiverSpike = alert.type.contains("RIVER", ignoreCase = true)

    // Vibrant, high-priority emergency color palette
    val containerBg = if (isCyclone) Color(0xFFFEF2F2) else Color(0xFFEFF6FF)
    val borderColor = if (isCyclone) Color(0xFFFCA5A5) else Color(0xFF93C5FD)
    val accentColor = if (isCyclone) Color(0xFFDC2626) else Color(0xFF2563EB)
    val badgeBg = if (isCyclone) Color(0xFFEF4444) else Color(0xFF3B82F6)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp)
            .testTag("emergency_alert_banner_${alert.id}"),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = containerBg),
        border = BorderStroke(1.5.dp, borderColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp)
        ) {
            // Header: Warning Icon + Title + Severity Pill + Dismiss Button
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    modifier = Modifier.weight(1f),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .size(34.dp)
                            .clip(CircleShape)
                            .background(accentColor)
                    ) {
                        Icon(
                            imageVector = when {
                                isCyclone -> Icons.Default.Warning
                                isRiverSpike -> Icons.Default.Flood
                                else -> Icons.Default.Emergency
                            },
                            contentDescription = "Alert icon",
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    Column {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Surface(
                                shape = RoundedCornerShape(4.dp),
                                color = badgeBg
                            ) {
                                Text(
                                    text = if (isCyclone) "LIVE CYCLONE ALERT" else "RIVER SPIKE ALERT",
                                    color = Color.White,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Black,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                            if (alert.windSpeedKmph != null) {
                                Surface(
                                    shape = RoundedCornerShape(4.dp),
                                    color = Color(0xFF7F1D1D)
                                ) {
                                    Text(
                                        text = "${alert.windSpeedKmph} km/h Gusts",
                                        color = Color.White,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                                    )
                                }
                            }
                            if (alert.waterLevelMeters != null && alert.dangerLevelMeters != null) {
                                Surface(
                                    shape = RoundedCornerShape(4.dp),
                                    color = Color(0xFF1E3A8A)
                                ) {
                                    Text(
                                        text = "${alert.waterLevelMeters}m (Danger: ${alert.dangerLevelMeters}m)",
                                        color = Color.White,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(3.dp))

                        Text(
                            text = alert.title,
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.ExtraBold,
                                color = if (isCyclone) Color(0xFF7F1D1D) else Color(0xFF1E3A8A),
                                fontSize = 14.sp
                            )
                        )
                    }
                }

                // Dismiss Action Button
                IconButton(
                    onClick = onDismiss,
                    modifier = Modifier
                        .size(32.dp)
                        .testTag("dismiss_emergency_alert_button_${alert.id}")
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Dismiss live emergency alert",
                        tint = accentColor.copy(alpha = 0.8f),
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Summary description
            Text(
                text = alert.summary,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontSize = 12.sp,
                    color = Color(0xFF334155),
                    lineHeight = 16.sp
                )
            )

            // Action required / Advisory callout
            Spacer(modifier = Modifier.height(6.dp))
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = if (isCyclone) Color(0xFFFFE4E6) else Color(0xFFDBEAFE),
                border = BorderStroke(0.5.dp, borderColor)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 10.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Action: ${alert.actionRequired}",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.SemiBold,
                            color = if (isCyclone) Color(0xFF991B1B) else Color(0xFF1E40AF),
                            fontSize = 11.sp
                        )
                    )
                }
            }

            // Quick action buttons: View Details + Emergency Helpline Dial + Shelter / More Info
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // View Details Button to open full scrollable dialog
                androidx.compose.material3.Button(
                    onClick = { showDetailsDialog = true },
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = accentColor),
                    contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                    modifier = Modifier
                        .height(32.dp)
                        .testTag("view_details_button_${alert.id}")
                ) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(13.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "View Details",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }

                OutlinedButton(
                    onClick = {
                        val dialIntent = Intent(Intent.ACTION_DIAL).apply {
                            data = Uri.parse("tel:${alert.emergencyHelpline}")
                        }
                        context.startActivity(dialIntent)
                    },
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = accentColor),
                    border = BorderStroke(1.dp, accentColor),
                    contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 8.dp, vertical = 4.dp),
                    modifier = Modifier
                        .height(32.dp)
                        .testTag("emergency_helpline_button_${alert.id}")
                ) {
                    Icon(
                        imageVector = Icons.Default.Phone,
                        contentDescription = null,
                        modifier = Modifier.size(13.dp)
                    )
                    Spacer(modifier = Modifier.width(3.dp))
                    Text(
                        text = "Helpline",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                if (isCyclone) {
                    OutlinedButton(
                        onClick = onOpenShelters,
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = accentColor),
                        border = BorderStroke(1.dp, accentColor),
                        contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 8.dp, vertical = 4.dp),
                        modifier = Modifier
                            .height(32.dp)
                            .testTag("open_shelters_button_${alert.id}")
                    ) {
                        Text(
                            text = "Shelters",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }

    // Mini-dialog displaying full scrollable text for coastal defense and emergency advisories
    if (showDetailsDialog) {
        val scrollState = rememberScrollState()
        AlertDialog(
            onDismissRequest = { showDetailsDialog = false },
            modifier = Modifier.testTag("emergency_alert_details_dialog_${alert.id}"),
            icon = {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(42.dp)
                        .clip(CircleShape)
                        .background(accentColor)
                ) {
                    Icon(
                        imageVector = if (isCyclone) Icons.Default.Warning else Icons.Default.Flood,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                }
            },
            title = {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = alert.title,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    )
                    if (alert.odiaTitle.isNotBlank()) {
                        Spacer(modifier = Modifier.height(3.dp))
                        Text(
                            text = alert.odiaTitle,
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = accentColor,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 12.sp
                            )
                        )
                    }
                }
            },
            text = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .verticalScroll(scrollState)
                        .padding(vertical = 4.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // Advisory Severity & Status Header
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = containerBg,
                        border = BorderStroke(1.dp, borderColor),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(10.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = "ADVISORY SEVERITY",
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = accentColor,
                                    letterSpacing = 0.5.sp
                                )
                                Text(
                                    text = alert.severity,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF1E293B)
                                )
                            }
                            if (alert.windSpeedKmph != null) {
                                Surface(
                                    shape = RoundedCornerShape(6.dp),
                                    color = Color(0xFF7F1D1D)
                                ) {
                                    Text(
                                        text = "${alert.windSpeedKmph} km/h Gusts",
                                        color = Color.White,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                                    )
                                }
                            }
                            if (alert.waterLevelMeters != null && alert.dangerLevelMeters != null) {
                                Surface(
                                    shape = RoundedCornerShape(6.dp),
                                    color = Color(0xFF1E3A8A)
                                ) {
                                    Text(
                                        text = "Level: ${alert.waterLevelMeters}m / Danger: ${alert.dangerLevelMeters}m",
                                        color = Color.White,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                                    )
                                }
                            }
                        }
                    }

                    // Affected Areas
                    if (alert.affectedArea.isNotBlank()) {
                        Column {
                            Text(
                                text = "AFFECTED COASTAL & BASIN ZONES",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = alert.affectedArea,
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 12.sp,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            )
                        }
                    }

                    // Full Long-Form Details (Coastal Defense & Cyclone Advisory)
                    Column {
                        Text(
                            text = "OFFICIAL DEFENSE & METEOROLOGICAL ADVISORY",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = alert.details.ifBlank { alert.summary },
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontSize = 12.5.sp,
                                color = Color(0xFF334155),
                                lineHeight = 18.sp
                            )
                        )
                    }

                    // Actionable Mandates
                    if (alert.actionRequired.isNotBlank()) {
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = if (isCyclone) Color(0xFFFFE4E6) else Color(0xFFDBEAFE),
                            border = BorderStroke(0.5.dp, borderColor),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(10.dp)) {
                                Text(
                                    text = "SAFETY INSTRUCTIONS & MANDATES",
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = if (isCyclone) Color(0xFF991B1B) else Color(0xFF1E40AF),
                                    letterSpacing = 0.5.sp
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = alert.actionRequired,
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        fontWeight = FontWeight.Medium,
                                        fontSize = 11.5.sp,
                                        color = if (isCyclone) Color(0xFF7F1D1D) else Color(0xFF172554)
                                    )
                                )
                            }
                        }
                    }

                    // Control Room Helpline Contact
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 10.dp, vertical = 8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Emergency Control Room:",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Medium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = alert.emergencyHelpline,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = accentColor
                            )
                        }
                    }
                }
            },
            confirmButton = {
                androidx.compose.material3.Button(
                    onClick = {
                        val dialIntent = Intent(Intent.ACTION_DIAL).apply {
                            data = Uri.parse("tel:${alert.emergencyHelpline}")
                        }
                        context.startActivity(dialIntent)
                    },
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = accentColor)
                ) {
                    Icon(
                        imageVector = Icons.Default.Phone,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Call Control Room",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showDetailsDialog = false },
                    modifier = Modifier.testTag("close_alert_details_dialog_${alert.id}")
                ) {
                    Text(
                        text = "Close",
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        )
    }
}
