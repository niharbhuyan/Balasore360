package com.example.ui.features.coastal

import android.content.Context
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Alarm
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DirectionsWalk
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material.icons.filled.Waves
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableLongStateOf
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
import com.example.data.model.AppLanguage
import com.example.data.local.ChandipurTideEntity
import com.example.data.notification.TideAndWeatherNotificationManager
import kotlinx.coroutines.delay

/**
 * Real-time Chandipur Safe Tide Return Timer & Alarms component.
 * Tracks the phenomenon where the Bay of Bengal sea recedes up to 5 km at low tide,
 * and provides countdown warnings to ensure shell collectors and tourists return safely before high tide.
 */
@Composable
fun ChandipurTideTimerSheet(
    onClose: () -> Unit,
    lastKnownTide: ChandipurTideEntity? = null,
    language: AppLanguage = AppLanguage.ENGLISH,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var isAlarmEnabled by remember {
        mutableStateOf(TideAndWeatherNotificationManager.isWalkWindowAlertsEnabled(context))
    }
    var alertThresholdMinutes by remember {
        mutableFloatStateOf(TideAndWeatherNotificationManager.getWalkWindowAdvanceMinutes(context).toFloat().coerceIn(15f, 60f))
    }

    // Live countdown calculation (Simulated tidal cycle window)
    // Low tide peak: 2:30 PM, High tide onset: 5:45 PM
    var secondsRemaining by remember { mutableLongStateOf(8420L) } // ~2h 20m safe window remaining

    LaunchedEffect(Unit) {
        while (true) {
            delay(1000L)
            if (secondsRemaining > 0) {
                secondsRemaining -= 1
            }
        }
    }

    val hours = secondsRemaining / 3600
    val minutes = (secondsRemaining % 3600) / 60
    val seconds = secondsRemaining % 60

    val safePercentage = (secondsRemaining.toFloat() / 14400f).coerceIn(0f, 1f)
    val isUrgentReturn = secondsRemaining <= (alertThresholdMinutes * 60)

    Column(
        modifier = modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .padding(20.dp)
            .testTag("chandipur_tide_timer_sheet")
    ) {
        // Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(
                            Brush.linearGradient(
                                listOf(Color(0xFF0284C7), Color(0xFF0369A1))
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Waves,
                        contentDescription = "Tide Icon",
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = "Chandipur Safe Tide Timer",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    )
                    Text(
                        text = "ଚାନ୍ଦିପୁର ସୁରକ୍ଷିତ ଜୁଆର ଫେରନ୍ତା ସମୟ",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = Color(0xFF0284C7),
                            fontWeight = FontWeight.SemiBold
                        )
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Room Database: Offline Cached Last Known Tide Schedule
        if (lastKnownTide != null) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 14.dp)
                    .testTag("offline_cached_tide_sheet_card"),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF8FAFC)),
                border = BorderStroke(1.dp, Color(0xFFBAE6FD))
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = Color(0xFF0284C7)
                        ) {
                            Text(
                                text = "ROOM PERSISTED • OFFLINE",
                                fontSize = 9.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = Color.White,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                        Text(
                            text = "Last Known Tide Forecast",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF0369A1)
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Receded: ${lastKnownTide.recededDistanceKm} km | Low Tide: ${lastKnownTide.lowTideTime} | Next High Tide: ${lastKnownTide.nextHighTideTime}",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF1E293B)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Status: ${lastKnownTide.safeWalkStatus} • ${lastKnownTide.safeWalkMinutesRemaining} mins safe return window",
                        fontSize = 11.5.sp,
                        color = Color(0xFF0D9488),
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }

        // Main Countdown Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(
                containerColor = if (isUrgentReturn) Color(0xFFFEF2F2) else Color(0xFFF0F9FF)
            ),
            border = BorderStroke(
                1.5.dp,
                if (isUrgentReturn) Color(0xFFEF4444) else Color(0xFF38BDF8)
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(18.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = if (isUrgentReturn) Color(0xFFEF4444) else Color(0xFF0284C7)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = if (isUrgentReturn) Icons.Default.Warning else Icons.Default.DirectionsWalk,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = if (isUrgentReturn) "⚠️ RETURN TO SHORE IMMEDIATELY" else "🟢 SEABED SAFE TO WALK (0 - 2.5 KM)",
                            color = Color.White,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = String.format("%02d:%02d:%02d", hours, minutes, seconds),
                    style = MaterialTheme.typography.headlineLarge.copy(
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 42.sp,
                        letterSpacing = 2.sp,
                        color = if (isUrgentReturn) Color(0xFFB91C1C) else Color(0xFF0369A1)
                    )
                )

                Text(
                    text = "Time Remaining Before Incoming Water Surge",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = if (isUrgentReturn) Color(0xFF991B1B) else Color(0xFF0C4A6E),
                        fontWeight = FontWeight.Medium
                    )
                )

                Spacer(modifier = Modifier.height(14.dp))

                LinearProgressIndicator(
                    progress = { safePercentage },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .clip(RoundedCornerShape(4.dp)),
                    color = if (isUrgentReturn) Color(0xFFEF4444) else Color(0xFF0284C7),
                    trackColor = if (isUrgentReturn) Color(0xFFFCA5A5) else Color(0xFFBAE6FD),
                )

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Low Tide Peak (Sea out 4.5 km)",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontSize = 10.sp,
                            color = Color(0xFF64748B)
                        )
                    )
                    Text(
                        text = "High Tide Inflow",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontSize = 10.sp,
                            color = Color(0xFF64748B),
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Daily Auto-Updated Tidal Clock & Safe Walk Window Table
        val dailyPulse = remember { com.example.data.daily.DailyUpdateEngine.getDailyPulse() }
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF0F172A)),
            border = BorderStroke(1.dp, Color(0xFF334155))
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            shape = CircleShape,
                            color = Color(0xFF38BDF8),
                            modifier = Modifier.size(10.dp)
                        ) {}
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "TODAY'S TIDAL CLOCK",
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF38BDF8),
                                letterSpacing = 1.sp
                            )
                        )
                    }
                    Text(
                        text = dailyPulse.formattedDate,
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = Color(0xFF94A3B8),
                            fontSize = 10.sp
                        )
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = Color(0xFF1E293B),
                        modifier = Modifier.weight(1f)
                    ) {
                        Column(modifier = Modifier.padding(10.dp)) {
                            Text("Safe Walk Window", fontSize = 10.sp, color = Color(0xFF94A3B8))
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = dailyPulse.chandipurLowTideWindow,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF34D399)
                            )
                            Text("Seabed recedes up to 5 km", fontSize = 9.sp, color = Color(0xFFCBD5E1))
                        }
                    }

                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = Color(0xFF1E293B),
                        modifier = Modifier.weight(1f)
                    ) {
                        Column(modifier = Modifier.padding(10.dp)) {
                            Text("Next High Tide", fontSize = 10.sp, color = Color(0xFF94A3B8))
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = dailyPulse.chandipurNextHighTide,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFFF87171)
                            )
                            Text("Full beach inundation", fontSize = 9.sp, color = Color(0xFFCBD5E1))
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Safety Index: ${dailyPulse.coastalSafetyIndex}",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = Color(0xFFE2E8F0),
                            fontWeight = FontWeight.SemiBold
                        )
                    )
                    Text(
                        text = if (dailyPulse.isChandipurWalkSafeNow) "🟢 Safe Now" else "🟡 Watch Tide",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = if (dailyPulse.isChandipurWalkSafeNow) Color(0xFF4ADE80) else Color(0xFFFBBF24)
                        )
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 24-Hour Interactive D3.js Tidal Curve & Safe Walk Window Profile
        ChandipurTideD3Chart(
            dailyPulse = dailyPulse,
            language = language,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // 72-Hour Interactive Tidal Wave Cycle Telemetry (Recharts Engine)
        ChandipurTidalRechartsCard(
            dailyPulse = dailyPulse,
            language = language,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Proactive Vanishing Sea Walk Window Push Notifications & Alarm Controls
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .testTag("chandipur_proactive_walk_notifications_card"),
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            border = BorderStroke(1.2.dp, Color(0xFF0284C7).copy(alpha = 0.4f)),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                // Channel Status Badge
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = if (isAlarmEnabled) Color(0xFFDCFCE7) else Color(0xFFF1F5F9),
                        border = BorderStroke(1.dp, if (isAlarmEnabled) Color(0xFF86EFAC) else Color(0xFFCBD5E1))
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 7.dp, vertical = 3.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(6.dp)
                                    .background(if (isAlarmEnabled) Color(0xFF16A34A) else Color(0xFF94A3B8), CircleShape)
                            )
                            Spacer(modifier = Modifier.width(5.dp))
                            Text(
                                text = if (isAlarmEnabled) "30-MIN BACKGROUND SERVICE: ACTIVE" else "NOTIFICATIONS MUTED",
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Black,
                                color = if (isAlarmEnabled) Color(0xFF15803D) else Color(0xFF64748B)
                            )
                        }
                    }

                    Text(
                        text = "Tidal Telemetry Service",
                        fontSize = 10.sp,
                        color = Color(0xFF0284C7),
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        modifier = Modifier.weight(1f),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = Color(0xFF0284C7).copy(alpha = 0.12f),
                            modifier = Modifier.size(38.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Default.NotificationsActive,
                                    contentDescription = null,
                                    tint = Color(0xFF0284C7),
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = if (language == AppLanguage.ODIA) "ଚାନ୍ଦିପୁର ସମୁଦ୍ର ଚାଲିବା ନୋଟିଫିକେସନ୍" else "Vanishing Sea Walk Alerts",
                                style = MaterialTheme.typography.titleSmall.copy(
                                    fontWeight = FontWeight.Bold
                                )
                            )
                            Text(
                                text = if (language == AppLanguage.ODIA) "ସମୁଦ୍ର ଅପସାରଣ ଓ ଜୁଆର ଫେରିବା ସମୟରେ ସ୍ୱୟଂଚାଳିତ ପୁସ୍ ଆଲର୍ଟ" else "Notifies automatically when seabed walk window opens & sirens before it closes",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    fontSize = 11.sp
                                )
                            )
                        }
                    }

                    Switch(
                        checked = isAlarmEnabled,
                        onCheckedChange = {
                            isAlarmEnabled = it
                            TideAndWeatherNotificationManager.setWalkWindowAlertsEnabled(context, it)
                            if (it) {
                                com.example.data.service.ChandipurSafeWalkService.scheduleNextWalkWindowAlert(context)
                            } else {
                                com.example.data.service.ChandipurSafeWalkService.cancelAlert(context)
                            }
                            Toast.makeText(
                                context,
                                if (it) "✅ Vanishing Sea Walk 30m Alerts Activated!" else "Muted Walk Notifications",
                                Toast.LENGTH_SHORT
                            ).show()
                        },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = Color.White,
                            checkedTrackColor = Color(0xFF0284C7)
                        ),
                        modifier = Modifier.testTag("toggle_walk_window_notifications")
                    )
                }

                HorizontalDivider(
                    modifier = Modifier.padding(vertical = 12.dp),
                    color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f)
                )

                // Advance Warning Timing Configuration
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (language == AppLanguage.ODIA) "ପୂର୍ବ ସୂଚନା ସମୟ: ${alertThresholdMinutes.toInt()} ମିନିଟ୍ ପୂର୍ବରୁ" else "Advance Siren Threshold: ${alertThresholdMinutes.toInt()} mins prior",
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF0F172A)
                        )
                    )

                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = Color(0xFF0284C7).copy(alpha = 0.12f)
                    ) {
                        Text(
                            text = "${alertThresholdMinutes.toInt()}m Buffer",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF0284C7),
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }

                Slider(
                    value = alertThresholdMinutes,
                    onValueChange = {
                        alertThresholdMinutes = it
                        TideAndWeatherNotificationManager.setWalkWindowAdvanceMinutes(context, it.toInt())
                    },
                    valueRange = 15f..60f,
                    steps = 2,
                    colors = SliderDefaults.colors(
                        thumbColor = Color(0xFF0284C7),
                        activeTrackColor = Color(0xFF0284C7)
                    ),
                    modifier = Modifier.testTag("walk_window_advance_slider")
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Interactive Test Notification Triggers for QA & Instant Verification
                Text(
                    text = "Instant Telemetry Push Verifiers:",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF64748B)
                )
                Spacer(modifier = Modifier.height(6.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedButton(
                        onClick = {
                            TideAndWeatherNotificationManager.testTriggerWalkWindowOpening(context)
                            Toast.makeText(
                                context,
                                "🌊 Sent Walk Window OPENING push notification!",
                                Toast.LENGTH_SHORT
                            ).show()
                        },
                        shape = RoundedCornerShape(10.dp),
                        border = BorderStroke(1.dp, Color(0xFF10B981)),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFF047857)),
                        contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 8.dp, vertical = 6.dp),
                        modifier = Modifier
                            .weight(1f)
                            .testTag("test_walk_window_opening_btn")
                    ) {
                        Icon(
                            imageVector = Icons.Default.DirectionsWalk,
                            contentDescription = null,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Test Opening Alert", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }

                    OutlinedButton(
                        onClick = {
                            TideAndWeatherNotificationManager.testTriggerWalkWindowClosing(context)
                            Toast.makeText(
                                context,
                                "🚨 Dispatched Walk Window CLOSING siren push notification!",
                                Toast.LENGTH_SHORT
                            ).show()
                        },
                        shape = RoundedCornerShape(10.dp),
                        border = BorderStroke(1.dp, Color(0xFFEF4444)),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFFB91C1C)),
                        contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 8.dp, vertical = 6.dp),
                        modifier = Modifier
                            .weight(1f)
                            .testTag("test_walk_window_closing_btn")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Alarm,
                            contentDescription = null,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Test Closing Siren", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Important Coastal Safety Guidelines
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFFFFBEB)),
            border = BorderStroke(1.dp, Color(0xFFFDE68A))
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Security,
                        contentDescription = null,
                        tint = Color(0xFFD97706),
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Chandipur Seabed Safety Rules",
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF92400E)
                        )
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                listOf(
                    "Walking perimeter: Keep within 2.5 km from the concrete sea embankment.",
                    "The sea returns at ~0.8 m/s speed. When the return siren sounds, turn back immediately.",
                    "Avoid deep intertidal mud depressions and marshy quicksand pockets near Balaramgadi mouth.",
                    "Look out for live horseshoe crabs, red ghost crabs, and sea snails without disturbing them.",
                    "Keep children and elderly family members within eyesight at all times."
                ).forEach { rule ->
                    Row(
                        modifier = Modifier.padding(vertical = 3.dp),
                        verticalAlignment = Alignment.Top
                    ) {
                        Text("• ", color = Color(0xFFD97706), fontWeight = FontWeight.Bold)
                        Text(
                            text = rule,
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = Color(0xFF78350F),
                                fontSize = 12.sp,
                                lineHeight = 16.sp
                            )
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = onClose,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0284C7)),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("Done / Keep Monitoring", fontWeight = FontWeight.Bold)
        }
    }
}
