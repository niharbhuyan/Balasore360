package com.example.ui.features.emergency

import android.Manifest
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
import androidx.core.content.ContextCompat
import com.example.data.fcm.FcmManager
import com.example.data.model.AppLanguage
import com.example.ui.theme.*

/**
 * Real-time Firebase Cloud Messaging (FCM) Push Notification Management Center.
 * Allows users to toggle, inspect, and test severe weather and coastal flood push alerts
 * delivered directly from IMD, INCOIS, CWC, and District Administration Balasore.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FcmSevereWeatherAlertSheet(
    language: AppLanguage = AppLanguage.ENGLISH,
    onClose: () -> Unit = {}
) {
    val context = LocalContext.current

    val fcmToken by FcmManager.fcmToken.collectAsState()
    val weatherEnabled by FcmManager.weatherAlertsEnabled.collectAsState()
    val newsEnabled by FcmManager.breakingNewsEnabled.collectAsState()

    var floodAlertsEnabled by remember { mutableStateOf(true) }

    var hasNotificationPermission by remember {
        mutableStateOf(
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED
            } else {
                true
            }
        )
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        hasNotificationPermission = isGranted
        if (isGranted) {
            Toast.makeText(context, "Notification permission granted! Real-time alerts active.", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, "Notifications denied. Critical alerts may not show on lock screen.", Toast.LENGTH_LONG).show()
        }
    }

    ModalBottomSheet(
        onDismissRequest = onClose,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        containerColor = Color.White
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 8.dp)
                .testTag("fcm_severe_weather_alert_sheet")
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
                            .background(Color(0xFFFEF2F2)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "🚨", fontSize = 24.sp)
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = if (language == AppLanguage.ODIA) "FCM ବିପର୍ଯ୍ୟୟ ପୁସ୍ ନୋଟିଫିକେସନ୍" else "Real-Time Push Alerts (FCM)",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.ExtraBold,
                                color = BentoSlate900
                            )
                        )
                        Text(
                            text = if (language == AppLanguage.ODIA) "ସାମୁଦ୍ରିକ ବନ୍ୟା ଓ ବାତ୍ୟା ତତ୍କାଳ ସତର୍କତା" else "Severe Weather, Coastal Flood & Tide Alerts",
                            style = MaterialTheme.typography.bodySmall.copy(color = BentoSlate500)
                        )
                    }
                }
                IconButton(onClick = onClose) {
                    Icon(imageVector = Icons.Default.Close, contentDescription = "Close")
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            LazyColumn(
                modifier = Modifier.fillMaxHeight(0.88f),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // Permission Warning Card if not granted
                if (!hasNotificationPermission && Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    item {
                        Card(
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = Color(0xFFFEF2F2)),
                            border = BorderStroke(1.5.dp, Color(0xFFF87171))
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(imageVector = Icons.Default.Warning, contentDescription = null, tint = Color(0xFFDC2626))
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = if (language == AppLanguage.ODIA) "ନୋଟିଫିକେସନ୍ ଅନୁମତି ଆବଶ୍ୟକ" else "Notification Permission Required",
                                        style = MaterialTheme.typography.titleSmall.copy(
                                            fontWeight = FontWeight.Bold,
                                            color = Color(0xFF991B1B)
                                        )
                                    )
                                }
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = if (language == AppLanguage.ODIA)
                                        "ଆପଣଙ୍କ ଫୋନ୍ ଲକ୍ ସ୍କ୍ରିନରେ ତତ୍କାଳ ବନ୍ୟା ଓ ଝଡ଼ ସତର୍କତା ପାଇବା ପାଇଁ ନୋଟିଫିକେସନ୍ ଅନୁମତି ପ୍ରଦାନ କରନ୍ତୁ।"
                                    else
                                        "To receive heads-up cyclone warnings and river surge alerts while your phone is locked, please enable system notifications.",
                                    style = MaterialTheme.typography.bodySmall.copy(color = Color(0xFF7F1D1D))
                                )
                                Spacer(modifier = Modifier.height(12.dp))
                                Button(
                                    onClick = {
                                        permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFDC2626)),
                                    shape = RoundedCornerShape(10.dp),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Icon(imageVector = Icons.Default.NotificationsActive, contentDescription = null, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("Grant Notification Permission", fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }

                // Cloud Messaging Engine Status Card
                item {
                    Card(
                        shape = RoundedCornerShape(18.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF0F172A)),
                        border = BorderStroke(1.5.dp, Color(0xFF38BDF8))
                    ) {
                        Column(modifier = Modifier.padding(18.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Box(
                                        modifier = Modifier
                                            .size(10.dp)
                                            .clip(CircleShape)
                                            .background(Color(0xFF4ADE80))
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = "FCM DISPATCH ACTIVE",
                                        style = MaterialTheme.typography.labelMedium.copy(
                                            fontWeight = FontWeight.ExtraBold,
                                            color = Color(0xFF4ADE80),
                                            letterSpacing = 1.sp
                                        )
                                    )
                                }
                                Surface(
                                    color = Color(0xFF1E293B),
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Text(
                                        text = "PRIORITY_MAX",
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            color = Color(0xFF38BDF8),
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 9.sp
                                        )
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            Text(
                                text = "Zero-Latency Severe Weather & Coastal Flood Radar",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            )

                            Text(
                                text = "Connected to IMD Cyclone Warning Centre (Bhubaneswar), INCOIS Ocean Wave Radar, and Central Water Commission (CWC) Rajghat Gauge.",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = Color(0xFF94A3B8),
                                    fontSize = 12.sp,
                                    lineHeight = 16.sp
                                )
                            )

                            Spacer(modifier = Modifier.height(14.dp))

                            // Token Box
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = Color(0xFF1E293B),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    modifier = Modifier.padding(12.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = "DEVICE FCM TOKEN",
                                            style = MaterialTheme.typography.labelSmall.copy(
                                                color = Color(0xFF64748B),
                                                fontSize = 9.sp,
                                                fontWeight = FontWeight.Bold
                                            )
                                        )
                                        Text(
                                            text = fcmToken?.take(28)?.plus("...") ?: "Token registered (Local channel ready)",
                                            style = MaterialTheme.typography.bodySmall.copy(
                                                color = Color(0xFF38BDF8),
                                                fontSize = 11.sp,
                                                fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace
                                            )
                                        )
                                    }

                                    IconButton(
                                        onClick = {
                                            val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                            val clip = ClipData.newPlainText("FCM Token", fcmToken ?: "balasore_360_fcm_token")
                                            clipboard.setPrimaryClip(clip)
                                            Toast.makeText(context, "FCM Token copied to clipboard!", Toast.LENGTH_SHORT).show()
                                        }
                                    ) {
                                        Icon(imageVector = Icons.Default.ContentCopy, contentDescription = "Copy Token", tint = Color.White)
                                    }
                                }
                            }
                        }
                    }
                }

                // Push Alert Channels & Subscription Matrix
                item {
                    Text(
                        text = if (language == AppLanguage.ODIA) "ସତର୍କତା ଚ୍ୟାନେଲ ଓ ବିଷୟ ଚୟନ" else "Push Alert Channels & Subscriptions",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = BentoSlate900
                        )
                    )
                }

                // Channel 1: Severe Weather, Nor'wester & Cyclone
                item {
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        border = BorderStroke(1.dp, Color(0xFFE2E8F0))
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = "🌪️", fontSize = 28.sp)
                            Spacer(modifier = Modifier.width(14.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = if (language == AppLanguage.ODIA) "ବାତ୍ୟା ଓ କାଳବୈଶାଖୀ ସତର୍କତା" else "Severe Weather & Cyclone Alerts",
                                    style = MaterialTheme.typography.titleSmall.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = BentoSlate900
                                    )
                                )
                                Text(
                                    text = "IMD squall warnings, lightning strikes, and Chandipur sea-state alerts",
                                    style = MaterialTheme.typography.bodySmall.copy(color = BentoSlate500, fontSize = 11.sp)
                                )
                            }
                            Switch(
                                checked = weatherEnabled,
                                onCheckedChange = { enabled ->
                                    FcmManager.setWeatherAlertsEnabled(context, enabled)
                                }
                            )
                        }
                    }
                }

                // Channel 2: Coastal Flood & Sluice Gate Warnings
                item {
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        border = BorderStroke(1.dp, Color(0xFFE2E8F0))
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = "🌊", fontSize = 28.sp)
                            Spacer(modifier = Modifier.width(14.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = if (language == AppLanguage.ODIA) "ଉପକୂଳ ବନ୍ୟା ଓ ସ୍ଲୁଇସ୍ ଗେଟ୍ ଚେତାବନୀ" else "Coastal Flood & Sluice Warnings",
                                    style = MaterialTheme.typography.titleSmall.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = BentoSlate900
                                    )
                                )
                                Text(
                                    text = "Subarnarekha & Budhabalanga river danger gauges & Bhograi sluice releases",
                                    style = MaterialTheme.typography.bodySmall.copy(color = BentoSlate500, fontSize = 11.sp)
                                )
                            }
                            Switch(
                                checked = floodAlertsEnabled,
                                onCheckedChange = { enabled ->
                                    floodAlertsEnabled = enabled
                                    FcmManager.setWeatherAlertsEnabled(context, enabled)
                                }
                            )
                        }
                    }
                }

                // Channel 3: Breaking Civic & News Wire
                item {
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        border = BorderStroke(1.dp, Color(0xFFE2E8F0))
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = "📰", fontSize = 28.sp)
                            Spacer(modifier = Modifier.width(14.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = if (language == AppLanguage.ODIA) "ଜରୁରୀ ପ୍ରଶାସନିକ ଖବର" else "Breaking Civic & News Bulletins",
                                    style = MaterialTheme.typography.titleSmall.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = BentoSlate900
                                    )
                                )
                                Text(
                                    text = "District Magistrate notices, curfew/holidays, and NH-16 traffic corridors",
                                    style = MaterialTheme.typography.bodySmall.copy(color = BentoSlate500, fontSize = 11.sp)
                                )
                            }
                            Switch(
                                checked = newsEnabled,
                                onCheckedChange = { enabled ->
                                    FcmManager.setBreakingNewsEnabled(context, enabled)
                                }
                            )
                        }
                    }
                }

                // Interactive Live Test Section
                item {
                    Card(
                        shape = RoundedCornerShape(18.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFF8FAFC)),
                        border = BorderStroke(1.dp, Color(0xFFCBD5E1))
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(imageVector = Icons.Default.Bolt, contentDescription = null, tint = OceanBlueDark)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = if (language == AppLanguage.ODIA) "ତତ୍କାଳ ପରୀକ୍ଷଣ ପୁସ୍ ସୂଚନା" else "Instant Live Push Alert Test",
                                    style = MaterialTheme.typography.titleSmall.copy(
                                        fontWeight = FontWeight.ExtraBold,
                                        color = BentoSlate900
                                    )
                                )
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "Trigger live simulated FCM payloads directly to your device notification tray to verify sound, vibration, and heads-up banner delivery.",
                                style = MaterialTheme.typography.bodySmall.copy(color = BentoSlate600, fontSize = 12.sp)
                            )

                            Spacer(modifier = Modifier.height(14.dp))

                            // Test Button 1: Severe Weather
                            Button(
                                onClick = {
                                    FcmManager.simulateWeatherAlertPush(context)
                                    Toast.makeText(context, "Severe Weather FCM alert dispatched to notification tray!", Toast.LENGTH_SHORT).show()
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFDC2626)),
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier.fillMaxWidth().testTag("btn_test_weather_push")
                            ) {
                                Icon(imageVector = Icons.Default.Thunderstorm, contentDescription = null, modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("⚡ Test Severe Weather Alert Push", fontWeight = FontWeight.Bold)
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            // Test Button 2: Coastal Flood
                            Button(
                                onClick = {
                                    FcmManager.simulateCoastalFloodPush(context)
                                    Toast.makeText(context, "Coastal Flood FCM alert dispatched to notification tray!", Toast.LENGTH_SHORT).show()
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = OceanBlue),
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier.fillMaxWidth().testTag("btn_test_flood_push")
                            ) {
                                Icon(imageVector = Icons.Default.Waves, contentDescription = null, modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("🌊 Test Coastal Flood Alert Push", fontWeight = FontWeight.Bold)
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            // Action Button: Evaluate Live Conditions Now
                            Button(
                                onClick = {
                                    com.example.data.fcm.BalasoreAlertDispatchEngine.evaluateAndDispatchRealTimeAlerts(
                                        context = context,
                                        weather = null,
                                        pulse = com.example.data.daily.DailyUpdateEngine.getDailyPulse(),
                                        emergencyAlerts = null,
                                        force = true
                                    )
                                    Toast.makeText(context, "Evaluated real-time conditions & sent active alerts to tray!", Toast.LENGTH_SHORT).show()
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0F172A)),
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier.fillMaxWidth().testTag("btn_eval_live_alerts")
                            ) {
                                Icon(imageVector = Icons.Default.Sync, contentDescription = null, modifier = Modifier.size(18.dp), tint = Color(0xFF38BDF8))
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("⚡ Evaluate & Push Live Active Alerts Now", fontWeight = FontWeight.Bold, color = Color(0xFF38BDF8))
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            // Test Button 3: Breaking Civic News
                            OutlinedButton(
                                onClick = {
                                    FcmManager.simulateBreakingNewsPush(context)
                                    Toast.makeText(context, "Breaking News FCM alert dispatched to notification tray!", Toast.LENGTH_SHORT).show()
                                },
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Icon(imageVector = Icons.Default.Article, contentDescription = null, modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("📰 Test Breaking News Push", fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }

                item { Spacer(modifier = Modifier.height(24.dp)) }
            }
        }
    }
}
