package com.example.ui.screens

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Article
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.NotificationsOff
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
import androidx.core.content.ContextCompat
import com.example.data.local.NewsArticleEntity
import com.example.data.local.WeatherCacheEntity
import com.example.ui.theme.BentoAmberBg
import com.example.ui.theme.BentoAmberText
import com.example.ui.theme.BentoBlueLight
import com.example.ui.theme.BentoBluePill
import com.example.ui.theme.BentoBlueText
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

/**
 * Local Alerts & Notification Opt-In Bottom Sheet.
 * Displays real-time local alerts for Balasore (weather warnings, breaking civic news)
 * and allows users to manage their Firebase Cloud Messaging push subscriptions.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LocalAlertsBottomSheet(
    isOpen: Boolean,
    weather: WeatherCacheEntity?,
    breakingNews: List<NewsArticleEntity>,
    weatherAlertsEnabled: Boolean,
    breakingNewsEnabled: Boolean,
    fcmToken: String?,
    onClose: () -> Unit,
    onToggleWeatherAlerts: (Boolean) -> Unit,
    onToggleBreakingNews: (Boolean) -> Unit,
    onSimulateWeatherAlert: () -> Unit,
    onSimulateBreakingNews: () -> Unit,
    onViewWeatherDetails: () -> Unit = {},
    onReadArticle: (NewsArticleEntity) -> Unit = {}
) {
    if (!isOpen) return

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val context = LocalContext.current

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
    }

    ModalBottomSheet(
        onDismissRequest = onClose,
        sheetState = sheetState,
        containerColor = BentoCardWhite,
        shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
        dragHandle = {
            Box(
                modifier = Modifier
                    .padding(vertical = 12.dp)
                    .width(44.dp)
                    .height(4.dp)
                    .background(BentoBorder, RoundedCornerShape(2.dp))
            )
        }
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            contentPadding = PaddingValues(bottom = 36.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Header Section
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = BentoRedBg,
                            modifier = Modifier.size(46.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Default.NotificationsActive,
                                    contentDescription = "Alerts",
                                    tint = BentoRedText,
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                        }
                        Column {
                            Text(
                                text = "Balasore Alert Center",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = BentoSlate900
                            )
                            Text(
                                text = "ବାଲେଶ୍ୱର ସତର୍କତା ଓ ପୁଶ୍ ବିଜ୍ଞପ୍ତି (FCM Push)",
                                style = MaterialTheme.typography.bodySmall,
                                color = BentoSlate500
                            )
                        }
                    }

                    IconButton(
                        onClick = onClose,
                        modifier = Modifier.testTag("close_alerts_sheet_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close",
                            tint = BentoSlate700
                        )
                    }
                }
            }

            // Connection Status & Token Card
            item {
                Surface(
                    shape = RoundedCornerShape(14.dp),
                    color = BentoBlueLight,
                    border = BorderStroke(1.dp, BentoBorder),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(8.dp)
                                        .background(if (fcmToken != null) BentoGreenText else BentoAmberText, CircleShape)
                                )
                                Text(
                                    text = "Firebase Cloud Messaging (FCM)",
                                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                                    color = BentoSlate900
                                )
                            }

                            Text(
                                text = if (fcmToken != null) "Connected & Subscribed" else "Active Listener",
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                color = if (fcmToken != null) BentoGreenText else BentoAmberText
                            )
                        }

                        if (fcmToken != null) {
                            Spacer(modifier = Modifier.height(8.dp))
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(BentoCardWhite, RoundedCornerShape(8.dp))
                                    .border(1.dp, BentoBorder, RoundedCornerShape(8.dp))
                                    .padding(horizontal = 10.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "Token: ${fcmToken.take(14)}...${fcmToken.takeLast(6)}",
                                    style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                                    color = BentoSlate500,
                                    modifier = Modifier.weight(1f)
                                )
                                Text(
                                    text = "Copy Token",
                                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                    color = BentoPrimaryBlue,
                                    modifier = Modifier
                                        .clickable {
                                            val clipboard = context.getSystemService(android.content.Context.CLIPBOARD_SERVICE) as? android.content.ClipboardManager
                                            val clip = android.content.ClipData.newPlainText("FCM Registration Token", fcmToken)
                                            clipboard?.setPrimaryClip(clip)
                                            android.widget.Toast.makeText(context, "FCM Token copied to clipboard", android.widget.Toast.LENGTH_SHORT).show()
                                        }
                                        .padding(4.dp)
                                )
                            }
                        }
                    }
                }
            }

            // Notification Permission Banner (Android 13+)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU && !hasNotificationPermission) {
                item {
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = BentoAmberBg),
                        border = BorderStroke(1.dp, BentoAmberText.copy(alpha = 0.3f)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.NotificationsOff,
                                    contentDescription = "Permission Required",
                                    tint = BentoAmberText,
                                    modifier = Modifier.size(20.dp)
                                )
                                Text(
                                    text = "System Notifications Disabled",
                                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                    color = BentoAmberText
                                )
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "To receive critical Bay of Bengal cyclone warnings and breaking civic bulletins in your notification shade, please grant notification access.",
                                style = MaterialTheme.typography.bodySmall,
                                color = BentoSlate700
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                            Button(
                                onClick = {
                                    permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                                },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = BentoPrimaryBlue,
                                    contentColor = Color.White
                                ),
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier.testTag("grant_notification_permission_button")
                            ) {
                                Text("Enable Notifications", fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }

            // SECTION 1: Active Local Alerts in Balasore
            item {
                Text(
                    text = "ACTIVE BALASORE ALERTS",
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.2.sp
                    ),
                    color = BentoSlate500,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }

            // Weather Warning Alert Banner
            val isWeatherAlertActive = weather != null && weather.alertLevel != "SAFE" && weather.alertLevel != "NORMAL"
            if (isWeatherAlertActive && weather != null) {
                item {
                    val isUrgent = weather.alertLevel == "URGENT" || weather.alertLevel == "CRITICAL"
                    Card(
                        shape = RoundedCornerShape(18.dp),
                        colors = CardDefaults.cardColors(containerColor = if (isUrgent) BentoRedBg else BentoAmberBg),
                        border = BorderStroke(1.dp, if (isUrgent) BentoRedText.copy(alpha = 0.3f) else BentoAmberText.copy(alpha = 0.3f)),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                onClose()
                                onViewWeatherDetails()
                            }
                            .testTag("active_weather_alert_card")
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = if (isUrgent) BentoRedText else BentoAmberText
                                ) {
                                    Text(
                                        text = "⚡ ${weather.alertLevel} COASTAL ADVISORY",
                                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                        color = Color.White,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                    )
                                }
                                Text(
                                    text = "Chandipur / Bay of Bengal",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = BentoSlate700
                                )
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = weather.alertTitle,
                                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                color = BentoSlate900
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = weather.alertMessage,
                                style = MaterialTheme.typography.bodySmall,
                                color = BentoSlate700
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.End
                            ) {
                                Text(
                                    text = "View Weather Details →",
                                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                                    color = BentoPrimaryBlue
                                )
                            }
                        }
                    }
                }
            }

            // Breaking News Articles
            if (breakingNews.isNotEmpty()) {
                item {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        breakingNews.take(2).forEach { article ->
                            Card(
                                shape = RoundedCornerShape(16.dp),
                                colors = CardDefaults.cardColors(containerColor = BentoCardWhite),
                                border = BorderStroke(1.dp, BentoBorder),
                                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        onClose()
                                        onReadArticle(article)
                                    }
                                    .testTag("active_breaking_news_${article.id}")
                            ) {
                                Column(modifier = Modifier.padding(14.dp)) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Surface(
                                            shape = RoundedCornerShape(6.dp),
                                            color = BentoRedBg
                                        ) {
                                            Text(
                                                text = "BREAKING",
                                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                                color = BentoRedText,
                                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                            )
                                        }
                                        Text(
                                            text = article.publishedAt,
                                            style = MaterialTheme.typography.labelSmall,
                                            color = BentoSlate400
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Text(
                                        text = article.title,
                                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                        color = BentoSlate900
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = article.summary,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = BentoSlate500,
                                        maxLines = 2
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // All Clear message if no alerts
            if (!isWeatherAlertActive && breakingNews.isEmpty()) {
                item {
                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = BentoGreenBg,
                        border = BorderStroke(1.dp, BentoGreenText.copy(alpha = 0.25f)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = "All Clear",
                                tint = BentoGreenText,
                                modifier = Modifier.size(24.dp)
                            )
                            Column {
                                Text(
                                    text = "All Clear in Balasore",
                                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                    color = BentoSlate900
                                )
                                Text(
                                    text = "No severe weather storm warnings or emergency district advisories at this moment.",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = BentoSlate700
                                )
                            }
                        }
                    }
                }
            }

            // SECTION 2: Opt-In Subscriptions
            item {
                HorizontalDivider(color = BentoBorder, modifier = Modifier.padding(vertical = 4.dp))
                Text(
                    text = "NOTIFICATION PREFERENCES & OPT-IN",
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.2.sp
                    ),
                    color = BentoSlate500,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }

            // Opt-in Switch 1: Severe Weather & Marine Alerts
            item {
                Card(
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = BentoCardWhite),
                    border = BorderStroke(1.dp, BentoBorder),
                    elevation = CardDefaults.cardElevation(defaultElevation = 0.5.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            modifier = Modifier.weight(1f),
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            verticalAlignment = Alignment.Top
                        ) {
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = BentoBluePill,
                                modifier = Modifier.size(40.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(
                                        imageVector = Icons.Default.Cloud,
                                        contentDescription = "Weather Alerts",
                                        tint = BentoPrimaryBlue,
                                        modifier = Modifier.size(22.dp)
                                    )
                                }
                            }

                            Column {
                                Text(
                                    text = "Severe Weather & Marine Warnings",
                                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                                    color = BentoSlate900
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = "Bay of Bengal cyclonic storms, squalls, and Chandipur beach high tide safety notices.",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = BentoSlate500
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "Topic: balasore_weather_alerts",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = BentoPrimaryBlue
                                )
                            }
                        }

                        Spacer(modifier = Modifier.width(8.dp))

                        Switch(
                            checked = weatherAlertsEnabled,
                            onCheckedChange = { onToggleWeatherAlerts(it) },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = Color.White,
                                checkedTrackColor = BentoPrimaryBlue,
                                uncheckedThumbColor = BentoSlate400,
                                uncheckedTrackColor = BentoBorder
                            ),
                            modifier = Modifier.testTag("switch_weather_alerts_optin")
                        )
                    }
                }
            }

            // Opt-in Switch 2: Breaking News & Civic Bulletins
            item {
                Card(
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = BentoCardWhite),
                    border = BorderStroke(1.dp, BentoBorder),
                    elevation = CardDefaults.cardElevation(defaultElevation = 0.5.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            modifier = Modifier.weight(1f),
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            verticalAlignment = Alignment.Top
                        ) {
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = BentoRedBg,
                                modifier = Modifier.size(40.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Filled.Article,
                                        contentDescription = "Breaking News",
                                        tint = BentoRedText,
                                        modifier = Modifier.size(22.dp)
                                    )
                                }
                            }

                            Column {
                                Text(
                                    text = "Breaking News & Civic Wire",
                                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                                    color = BentoSlate900
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = "District Magistrate emergency orders, transport updates, road closures, and city bulletins.",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = BentoSlate500
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "Topic: balasore_breaking_news",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = BentoPrimaryBlue
                                )
                            }
                        }

                        Spacer(modifier = Modifier.width(8.dp))

                        Switch(
                            checked = breakingNewsEnabled,
                            onCheckedChange = { onToggleBreakingNews(it) },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = Color.White,
                                checkedTrackColor = BentoPrimaryBlue,
                                uncheckedThumbColor = BentoSlate400,
                                uncheckedTrackColor = BentoBorder
                            ),
                            modifier = Modifier.testTag("switch_breaking_news_optin")
                        )
                    }
                }
            }

            // SECTION 3: Push Simulator / Verification Actions
            item {
                HorizontalDivider(color = BentoBorder, modifier = Modifier.padding(vertical = 4.dp))
                Text(
                    text = "INSTANT ALERT SIMULATOR",
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.2.sp
                    ),
                    color = BentoSlate500,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }

            item {
                Card(
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = BentoBlueLight),
                    border = BorderStroke(1.dp, BentoBorder),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Test Local Push Notification Dispatch",
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                            color = BentoSlate900
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Dispatch a localized mock push payload to verify system tray delivery, audio alerts, and instant Room database synchronization.",
                            style = MaterialTheme.typography.bodySmall,
                            color = BentoSlate700
                        )
                        Spacer(modifier = Modifier.height(12.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            OutlinedButton(
                                onClick = onSimulateWeatherAlert,
                                shape = RoundedCornerShape(12.dp),
                                border = BorderStroke(1.dp, BentoPrimaryBlue),
                                modifier = Modifier
                                    .weight(1f)
                                    .testTag("simulate_weather_alert_button")
                            ) {
                                Text("⚡ Weather Alert", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            }

                            OutlinedButton(
                                onClick = onSimulateBreakingNews,
                                shape = RoundedCornerShape(12.dp),
                                border = BorderStroke(1.dp, BentoPrimaryBlue),
                                modifier = Modifier
                                    .weight(1f)
                                    .testTag("simulate_breaking_news_button")
                            ) {
                                Text("📰 Breaking News", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }
    }
}
