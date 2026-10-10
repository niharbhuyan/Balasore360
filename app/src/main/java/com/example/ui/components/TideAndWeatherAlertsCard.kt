package com.example.ui.components

import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.material.icons.filled.Alarm
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material.icons.automirrored.filled.DirectionsWalk
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Waves
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AppLanguage
import com.example.data.notification.TideAndWeatherNotificationManager
import com.example.ui.theme.AmberGold
import com.example.ui.theme.BentoSlate200
import com.example.ui.theme.BentoSlate500
import com.example.ui.theme.BentoSlate600
import com.example.ui.theme.BentoSlate700
import com.example.ui.theme.BentoSlate800
import com.example.ui.theme.BentoSlate900
import com.example.ui.theme.EmeraldGreen
import com.example.ui.theme.OceanBlue
import com.example.ui.theme.OceanBlueDark

/**
 * Interactive card component to manage the local notification system
 * for Chandipur Beach high tide warnings and weather updates using WorkManager.
 */
@Composable
fun TideAndWeatherAlertsCard(
    language: AppLanguage = AppLanguage.ENGLISH,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    var isTideEnabled by remember { mutableStateOf(TideAndWeatherNotificationManager.isTideAlertsEnabled(context)) }
    var isWalkWindowEnabled by remember { mutableStateOf(TideAndWeatherNotificationManager.isWalkWindowAlertsEnabled(context)) }
    var isWeatherEnabled by remember { mutableStateOf(TideAndWeatherNotificationManager.isWeatherAlertsEnabled(context)) }
    var advanceWarningMinutes by remember { mutableIntStateOf(TideAndWeatherNotificationManager.getAdvanceWarningMinutes(context)) }
    var showAdvancedSettings by remember { mutableStateOf(false) }

    // Android 13+ Notification Permission Launcher
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            Toast.makeText(context, "Notification permission granted!", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, "Notifications disabled. Enable in device Settings for alerts.", Toast.LENGTH_LONG).show()
        }
    }

    LaunchedEffect(Unit) {
        // Ensure channels are created and WorkManager is running
        TideAndWeatherNotificationManager.createNotificationChannels(context)
        TideAndWeatherNotificationManager.schedulePeriodicTideAndWeatherChecks(context, intervalMinutes = 15)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
        }
    }

    val workerInfo by TideAndWeatherNotificationManager.observeWorkInfo(context).collectAsState(initial = null)

    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("tide_weather_alerts_card"),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            // Header Row with Icon and Title
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
                        color = MaterialTheme.colorScheme.primaryContainer,
                        modifier = Modifier.size(40.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Default.NotificationsActive,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(22.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = if (language == AppLanguage.ODIA) "ଚାନ୍ଦିପୁର ଜୁଆର ଓ ପାଣିପାଗ ସତର୍କତା" else "Tide & Weather Alerts",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface,
                                fontSize = 16.sp
                            )
                        )
                        Text(
                            text = "WorkManager Local Notification Engine",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                fontSize = 11.sp
                            )
                        )
                    }
                }

                // WorkManager Active Pill
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = EmeraldGreen.copy(alpha = 0.15f),
                    border = BorderStroke(1.dp, EmeraldGreen.copy(alpha = 0.4f))
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = EmeraldGreen,
                            modifier = Modifier.size(6.dp)
                        ) {}
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "WorkManager 15m",
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            color = EmeraldGreen
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Setting 1: Chandipur High Tide Warnings Toggle
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    modifier = Modifier.weight(1f),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Waves,
                        contentDescription = null,
                        tint = OceanBlue,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = if (language == AppLanguage.ODIA) "ଚାନ୍ଦିପୁର ଜୁଆର ପ୍ରତ୍ୟାବର୍ତ୍ତନ ଚେତାବନୀ" else "Chandipur High Tide Alerts",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = if (language == AppLanguage.ODIA) "ସମୁଦ୍ର ମାଡ଼ି ଆସିବା ପୂର୍ବରୁ ସତର୍କ ସୂଚନା" else "Alerts before sea rushes back across 5 km seabed",
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                Switch(
                    checked = isTideEnabled,
                    onCheckedChange = { checked ->
                        isTideEnabled = checked
                        TideAndWeatherNotificationManager.setTideAlertsEnabled(context, checked)
                    },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = Color.White,
                        checkedTrackColor = OceanBlue
                    ),
                    modifier = Modifier.testTag("tide_alerts_switch")
                )
            }

            // Advance Warning Threshold Selector (When Tide Alert is ON)
            AnimatedVisibility(visible = isTideEnabled) {
                Column(modifier = Modifier.padding(top = 8.dp, start = 30.dp)) {
                    Text(
                        text = "Advance Notice Time Before High Tide:",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        listOf(15, 30, 45, 60).forEach { mins ->
                            val isSelected = advanceWarningMinutes == mins
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
                                modifier = Modifier.clickable {
                                    advanceWarningMinutes = mins
                                    TideAndWeatherNotificationManager.setAdvanceWarningMinutes(context, mins)
                                }
                            ) {
                                Text(
                                    text = "${mins}m before",
                                    fontSize = 10.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                    color = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))
            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
            Spacer(modifier = Modifier.height(10.dp))

            // Setting 2: Chandipur 30-Min Safe Walk Window Alerts Toggle
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    modifier = Modifier.weight(1f),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.DirectionsWalk,
                        contentDescription = null,
                        tint = EmeraldGreen,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = if (language == AppLanguage.ODIA) "ଚାନ୍ଦିପୁର ସମୁଦ୍ର ଚାଲିବା ୩୦ ମିନିଟ୍ ପୂର୍ବ ଆଲର୍ଟ" else "Safe Walk Window Alert (30m Prior)",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = if (language == AppLanguage.ODIA) "ଜୁଆର ତଥ୍ୟ ଅନୁସାରେ ସମୁଦ୍ର ଖାଲି ହେବା ୩୦ ମି ପୂର୍ବରୁ ପୁସ୍ ଆଲର୍ଟ" else "Telemetry alert 30 mins before receding sea walk window begins",
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                Switch(
                    checked = isWalkWindowEnabled,
                    onCheckedChange = { checked ->
                        isWalkWindowEnabled = checked
                        TideAndWeatherNotificationManager.setWalkWindowAlertsEnabled(context, checked)
                        if (checked) {
                            com.example.data.service.ChandipurSafeWalkService.scheduleNextWalkWindowAlert(context)
                        } else {
                            com.example.data.service.ChandipurSafeWalkService.cancelAlert(context)
                        }
                    },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = Color.White,
                        checkedTrackColor = EmeraldGreen
                    ),
                    modifier = Modifier.testTag("walk_window_alerts_switch")
                )
            }

            Spacer(modifier = Modifier.height(10.dp))
            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
            Spacer(modifier = Modifier.height(10.dp))

            // Setting 3: Weather Updates Toggle
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    modifier = Modifier.weight(1f),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Cloud,
                        contentDescription = null,
                        tint = AmberGold,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = if (language == AppLanguage.ODIA) "ବାଲେଶ୍ୱର ପାଣିପାଗ ଓ ବାତ୍ୟା ସୂଚନା" else "Balasore Weather Alerts",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = if (language == AppLanguage.ODIA) "କାଳବୈଶାଖୀ, ବର୍ଷା ଓ ସାମୁଦ୍ରିକ ପବନ ଅପଡେଟ୍" else "Kalbaisakhi storms, squalls, & temperature updates",
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                Switch(
                    checked = isWeatherEnabled,
                    onCheckedChange = { checked ->
                        isWeatherEnabled = checked
                        TideAndWeatherNotificationManager.setWeatherAlertsEnabled(context, checked)
                    },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = Color.White,
                        checkedTrackColor = AmberGold
                    ),
                    modifier = Modifier.testTag("weather_alerts_switch")
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Interactive Test Action Buttons Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Test High Tide Alert Button
                OutlinedButton(
                    onClick = {
                        TideAndWeatherNotificationManager.testTriggerHighTideWarning(context)
                        Toast.makeText(context, "Dispatched Test High Tide Warning!", Toast.LENGTH_SHORT).show()
                    },
                    modifier = Modifier
                        .weight(1f)
                        .testTag("test_high_tide_button"),
                    shape = RoundedCornerShape(10.dp),
                    border = BorderStroke(1.dp, OceanBlue),
                    contentPadding = PaddingValues(horizontal = 6.dp, vertical = 6.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Waves,
                        contentDescription = null,
                        modifier = Modifier.size(13.dp),
                        tint = OceanBlue
                    )
                    Spacer(modifier = Modifier.width(3.dp))
                    Text(
                        text = "High Tide",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = OceanBlue
                    )
                }

                // Test Safe Walk 30m Advance Alert Button
                OutlinedButton(
                    onClick = {
                        com.example.data.service.ChandipurSafeWalkService.triggerImmediateTestAlert(context)
                        Toast.makeText(context, "🌊 Sent 30m Advance Safe Walk Alert!", Toast.LENGTH_SHORT).show()
                    },
                    modifier = Modifier
                        .weight(1f)
                        .testTag("test_safe_walk_30m_button"),
                    shape = RoundedCornerShape(10.dp),
                    border = BorderStroke(1.dp, EmeraldGreen),
                    contentPadding = PaddingValues(horizontal = 6.dp, vertical = 6.dp)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.DirectionsWalk,
                        contentDescription = null,
                        modifier = Modifier.size(13.dp),
                        tint = EmeraldGreen
                    )
                    Spacer(modifier = Modifier.width(3.dp))
                    Text(
                        text = "30m Walk Alert",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = EmeraldGreen
                    )
                }

                // Test Weather Alert Button
                OutlinedButton(
                    onClick = {
                        TideAndWeatherNotificationManager.testTriggerWeatherUpdate(context)
                        Toast.makeText(context, "Dispatched Test Weather Alert!", Toast.LENGTH_SHORT).show()
                    },
                    modifier = Modifier
                        .weight(1f)
                        .testTag("test_weather_button"),
                    shape = RoundedCornerShape(10.dp),
                    border = BorderStroke(1.dp, AmberGold),
                    contentPadding = PaddingValues(horizontal = 6.dp, vertical = 6.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Cloud,
                        contentDescription = null,
                        modifier = Modifier.size(13.dp),
                        tint = AmberGold
                    )
                    Spacer(modifier = Modifier.width(3.dp))
                    Text(
                        text = "Weather",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = AmberGold
                    )
                }

                // Run WorkManager Now
                Button(
                    onClick = {
                        TideAndWeatherNotificationManager.triggerImmediateTideAndWeatherCheck(context)
                        Toast.makeText(context, "WorkManager running evaluation in background...", Toast.LENGTH_SHORT).show()
                    },
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0F172A)),
                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = "Sync",
                        modifier = Modifier.size(14.dp),
                        tint = Color.White
                    )
                }
            }
        }
    }
}
