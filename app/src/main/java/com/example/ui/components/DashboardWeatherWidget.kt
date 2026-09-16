package com.example.ui.components

import android.content.Intent
import android.net.Uri
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.*
import com.example.ui.theme.*

/**
 * Interactive Weather Widget for the Balasore 360 Dashboard.
 *
 * Displays:
 * 1. Current conditions with live Celsius/Fahrenheit toggle and coastal micro-metrics
 * 2. 3-day forecast for Balasore with interactive daily selection & hourly projections
 * 3. Severe weather alerts with expandable warnings, affected zones, and direct emergency hotline
 */
@Composable
fun DashboardWeatherWidget(
    weather: WeatherInfo,
    forecastDays: List<BalasoreForecastDay>,
    weatherAlerts: List<BalasoreWeatherAlert>,
    language: AppLanguage,
    isFahrenheit: Boolean,
    selectedForecastIndex: Int,
    onToggleTempUnit: () -> Unit,
    onSelectForecastDay: (Int) -> Unit,
    onAcknowledgeAlert: (String) -> Unit,
    onNavigateToWeather: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var isAlertExpanded by remember { mutableStateOf(false) }

    fun formatTemp(celsius: Int): String {
        return if (isFahrenheit) {
            val f = Math.round(celsius * 1.8 + 32).toInt()
            "$f°F"
        } else {
            "$celsius°C"
        }
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .testTag("dashboard_weather_widget"),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, BentoSlate200),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp)
                .animateContentSize()
        ) {
            // 1. Top Bar: Location + Live Badge + Unit Toggle
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFE0F2FE)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.LocationOn,
                            contentDescription = "Location",
                            tint = OceanBlue,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "Balasore, Odisha",
                                style = MaterialTheme.typography.titleSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = BentoSlate900
                                )
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Surface(
                                shape = RoundedCornerShape(4.dp),
                                color = Color(0xFFDCFCE7),
                                border = BorderStroke(0.5.dp, Color(0xFF86EFAC))
                            ) {
                                Text(
                                    text = "LIVE",
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = Color(0xFF166534),
                                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                                )
                            }
                        }
                        Text(
                            text = if (language == AppLanguage.ODIA) "ବାଲେଶ୍ୱର ଉପକୂଳ ପାଣିପାଗ" else "Bay of Bengal Coastal Radar",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = BentoSlate500,
                                fontSize = 11.sp
                            )
                        )
                    }
                }

                // Interactive °C / °F Unit Toggle Switch
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = BentoSlate100,
                    border = BorderStroke(1.dp, BentoSlate200),
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .clickable { onToggleTempUnit() }
                        .testTag("dashboard_weather_unit_toggle")
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = if (!isFahrenheit) OceanBlue else Color.Transparent,
                            modifier = Modifier.size(24.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(
                                    text = "°C",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (!isFahrenheit) Color.White else BentoSlate600
                                )
                            }
                        }
                        Surface(
                            shape = CircleShape,
                            color = if (isFahrenheit) OceanBlue else Color.Transparent,
                            modifier = Modifier.size(24.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(
                                    text = "°F",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isFahrenheit) Color.White else BentoSlate600
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // 2. Main Current Condition Hero Strip
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(18.dp))
                    .background(
                        Brush.horizontalGradient(
                            listOf(Color(0xFF0284C7), Color(0xFF0369A1), Color(0xFF075985))
                        )
                    )
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = formatTemp(weather.tempCelsius),
                            style = MaterialTheme.typography.headlineLarge.copy(
                                fontWeight = FontWeight.ExtraBold,
                                color = Color.White,
                                fontSize = 34.sp
                            )
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = weather.condition,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontWeight = FontWeight.SemiBold,
                                color = Color(0xFFE0F2FE)
                            ),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            text = "High/Low: ${weather.highLow}",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = Color(0xFFBAE6FD),
                                fontSize = 11.sp
                            )
                        )
                    }

                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            text = "⛅",
                            fontSize = 38.sp
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = Color.White.copy(alpha = 0.2f),
                            modifier = Modifier.clickable { onNavigateToWeather() }
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Full Radar",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(12.dp)
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // 3. Four Micro-Metrics Bento Tiles
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                MicroWeatherTile(
                    icon = Icons.Default.WaterDrop,
                    iconTint = Color(0xFF0284C7),
                    label = "Humidity",
                    value = weather.humidity,
                    modifier = Modifier.weight(1f)
                )
                MicroWeatherTile(
                    icon = Icons.Default.Air,
                    iconTint = Color(0xFF16A34A),
                    label = "Wind",
                    value = weather.windSpeedKmh,
                    modifier = Modifier.weight(1f)
                )
                MicroWeatherTile(
                    icon = Icons.Default.Thermostat,
                    iconTint = Color(0xFFEA580C),
                    label = "AQI Air",
                    value = "54 (Good)",
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Chandipur Vanishing Tide Notice Banner
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = Color(0xFFF0F9FF),
                border = BorderStroke(1.dp, Color(0xFFBAE6FD)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "🌊", fontSize = 16.sp)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = weather.tideStatus,
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = Color(0xFF0369A1),
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 11.5.sp
                        ),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // 4. SEVERE WEATHER ALERT SECTION (Active Warning)
            val activeAlert = weatherAlerts.firstOrNull { it.isActive() && !it.isAcknowledged }
                ?: weatherAlerts.firstOrNull { !it.isAcknowledged }

            if (activeAlert != null) {
                SevereWeatherAlertCard(
                    alert = activeAlert,
                    language = language,
                    isExpanded = isAlertExpanded,
                    onToggleExpand = { isAlertExpanded = !isAlertExpanded },
                    onAcknowledge = { onAcknowledgeAlert(activeAlert.id) },
                    onCallHelpline = { phone ->
                        val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$phone"))
                        context.startActivity(intent)
                    }
                )
            } else {
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = Color(0xFFF0FDF4),
                    border = BorderStroke(1.dp, Color(0xFFBBF7D0)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = null,
                            tint = Color(0xFF16A34A),
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (language == AppLanguage.ODIA)
                                "ସମସ୍ତ ସାମୁଦ୍ରିକ ଓ ପାଣିପାଗ ସ୍ଥିତି ସୁରକ୍ଷିତ ଅଛି"
                            else
                                "No Active Severe Alerts • Coastline Normal",
                            style = MaterialTheme.typography.bodySmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF166534),
                                fontSize = 11.sp
                            )
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 5. 3-DAY FORECAST SECTION FOR BALASORE
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.CalendarMonth,
                        contentDescription = null,
                        tint = OceanBlue,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (language == AppLanguage.ODIA) "୩-ଦିନିଆ ପାଣିପାଗ ପୂର୍ବାନୁମାନ" else "3-Day Balasore Forecast",
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = FontWeight.ExtraBold,
                            color = BentoSlate900
                        )
                    )
                }
                Text(
                    text = "Tap day for hourly details",
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = BentoSlate400,
                        fontSize = 10.sp
                    )
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // 3 Day Selector Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                forecastDays.take(3).forEachIndexed { index, day ->
                    val isSelected = index == selectedForecastIndex
                    Surface(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(16.dp))
                            .clickable { onSelectForecastDay(index) }
                            .testTag("forecast_day_tab_$index"),
                        shape = RoundedCornerShape(16.dp),
                        color = if (isSelected) Color(0xFFE0F2FE) else BentoSlate100,
                        border = BorderStroke(
                            1.5.dp,
                            if (isSelected) OceanBlue else BentoSlate200
                        )
                    ) {
                        Column(
                            modifier = Modifier.padding(10.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = if (language == AppLanguage.ODIA) day.odiaDayLabel.split(" ").first() else day.dayLabel,
                                style = MaterialTheme.typography.labelMedium.copy(
                                    fontWeight = if (isSelected) FontWeight.ExtraBold else FontWeight.SemiBold,
                                    color = if (isSelected) OceanBlue else BentoSlate700
                                )
                            )
                            Text(
                                text = day.dateFormatted,
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontSize = 10.sp,
                                    color = BentoSlate500
                                )
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(text = day.weatherIcon, fontSize = 24.sp)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "${formatTemp(day.highTempC)} / ${formatTemp(day.lowTempC)}",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = BentoSlate900,
                                    fontSize = 11.sp
                                )
                            )
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = if (day.rainProbability > 50) Color(0xFFDCFCE7) else Color(0xFFF1F5F9),
                                modifier = Modifier.padding(top = 4.dp)
                            ) {
                                Text(
                                    text = "💧${day.rainProbability}%",
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (day.rainProbability > 50) Color(0xFF15803D) else BentoSlate600,
                                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                                )
                            }
                        }
                    }
                }
            }

            // Selected Day Detailed Breakdown (Hourly Slots + Marine Advice)
            val selectedDay = forecastDays.getOrNull(selectedForecastIndex) ?: forecastDays.firstOrNull()
            if (selectedDay != null) {
                Spacer(modifier = Modifier.height(12.dp))
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = Color(0xFFF8FAFC),
                    border = BorderStroke(1.dp, BentoSlate200),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = if (language == AppLanguage.ODIA) selectedDay.odiaCondition else selectedDay.condition,
                                style = MaterialTheme.typography.bodySmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = BentoSlate800
                                )
                            )
                            Text(
                                text = "Wind: ${selectedDay.windSummary}",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = BentoSlate500,
                                    fontSize = 10.sp
                                )
                            )
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        // Marine / Coastal note
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Info,
                                contentDescription = null,
                                tint = OceanBlue,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = selectedDay.marineNotice,
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = Color(0xFF0369A1),
                                    fontSize = 10.5.sp
                                )
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // Hourly timeline horizontal scroll
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .horizontalScroll(rememberScrollState()),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            selectedDay.hourlySlots.forEach { slot ->
                                Surface(
                                    shape = RoundedCornerShape(12.dp),
                                    color = Color.White,
                                    border = BorderStroke(1.dp, BentoSlate200)
                                ) {
                                    Column(
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp),
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        Text(
                                            text = slot.timeLabel,
                                            fontSize = 9.sp,
                                            fontWeight = FontWeight.SemiBold,
                                            color = BentoSlate500
                                        )
                                        Text(text = slot.conditionEmoji, fontSize = 16.sp)
                                        Text(
                                            text = formatTemp(slot.tempC),
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = BentoSlate900
                                        )
                                        Text(
                                            text = "${slot.popPercentage}% rain",
                                            fontSize = 8.5.sp,
                                            color = Color(0xFF0284C7)
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

/**
 * Micro tile for Humidity, Wind, and AQI
 */
@Composable
fun MicroWeatherTile(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    iconTint: Color,
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(14.dp),
        color = BentoSlate100,
        border = BorderStroke(0.5.dp, BentoSlate200)
    ) {
        Column(
            modifier = Modifier.padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = iconTint,
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = label,
                fontSize = 9.sp,
                fontWeight = FontWeight.SemiBold,
                color = BentoSlate500
            )
            Text(
                text = value,
                fontSize = 11.sp,
                fontWeight = FontWeight.ExtraBold,
                color = BentoSlate900,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

/**
 * Severe Weather Alert Interactive Card
 */
@Composable
fun SevereWeatherAlertCard(
    alert: BalasoreWeatherAlert,
    language: AppLanguage,
    isExpanded: Boolean,
    onToggleExpand: () -> Unit,
    onAcknowledge: () -> Unit,
    onCallHelpline: (String) -> Unit
) {
    val bgBrush = when (alert.severity) {
        AlertSeverity.CYCLONE_ALERT -> Brush.horizontalGradient(listOf(Color(0xFFFEF2F2), Color(0xFFFEE2E2)))
        AlertSeverity.WARNING -> Brush.horizontalGradient(listOf(Color(0xFFFFFBEB), Color(0xFFFEF3C7)))
        AlertSeverity.ADVISORY -> Brush.horizontalGradient(listOf(Color(0xFFFFFBEB), Color(0xFFFEF9C3)))
        else -> Brush.horizontalGradient(listOf(Color(0xFFF0F9FF), Color(0xFFE0F2FE)))
    }

    val accentBorder = when (alert.severity) {
        AlertSeverity.CYCLONE_ALERT -> Color(0xFFF87171)
        AlertSeverity.WARNING -> Color(0xFFFBBF24)
        else -> Color(0xFF38BDF8)
    }

    Surface(
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.2.dp, accentBorder),
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .testTag("severe_weather_alert_banner")
    ) {
        Box(
            modifier = Modifier
                .background(bgBrush)
                .padding(12.dp)
        ) {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        modifier = Modifier.weight(1f),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = alert.category.emoji, fontSize = 20.sp)
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Surface(
                                shape = RoundedCornerShape(4.dp),
                                color = alert.severity.badgeBg
                            ) {
                                Text(
                                    text = if (language == AppLanguage.ODIA) alert.severity.odiaLabel else alert.severity.levelName,
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = alert.severity.badgeText,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = if (language == AppLanguage.ODIA) alert.odiaTitle else alert.title,
                                style = MaterialTheme.typography.bodySmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = BentoSlate900
                                ),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }

                    IconButton(
                        onClick = onToggleExpand,
                        modifier = Modifier.size(28.dp)
                    ) {
                        Icon(
                            imageVector = if (isExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                            contentDescription = if (isExpanded) "Collapse" else "Expand",
                            tint = BentoSlate700
                        )
                    }
                }

                Text(
                    text = alert.summary,
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = BentoSlate700,
                        fontSize = 11.sp
                    ),
                    modifier = Modifier.padding(top = 4.dp)
                )

                AnimatedVisibility(
                    visible = isExpanded,
                    enter = fadeIn(),
                    exit = fadeOut()
                ) {
                    Column(modifier = Modifier.padding(top = 10.dp)) {
                        HorizontalDivider(
                            color = accentBorder.copy(alpha = 0.5f),
                            thickness = 0.8.dp
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = alert.detailedDescription,
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = BentoSlate700,
                                fontSize = 11.sp,
                                lineHeight = 16.sp
                            )
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        // Affected Balasore Localities
                        Text(
                            text = "Affected Localities:",
                            fontWeight = FontWeight.Bold,
                            fontSize = 10.5.sp,
                            color = BentoSlate800
                        )
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 4.dp)
                                .horizontalScroll(rememberScrollState()),
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            alert.affectedZones.forEach { zone ->
                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = Color.White.copy(alpha = 0.85f),
                                    border = BorderStroke(0.5.dp, BentoSlate300)
                                ) {
                                    Text(
                                        text = if (language == AppLanguage.ODIA) zone.odiaName else zone.zoneName,
                                        fontSize = 9.5.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = BentoSlate800,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // Action Buttons: Call Helpline + Acknowledge
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Button(
                                onClick = { onCallHelpline("06782-262244") },
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFDC2626)),
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier.weight(1f),
                                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Phone,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Control Room", fontSize = 10.5.sp, fontWeight = FontWeight.Bold)
                            }

                            OutlinedButton(
                                onClick = onAcknowledge,
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier.weight(1f),
                                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = null,
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Dismiss", fontSize = 10.5.sp)
                            }
                        }
                    }
                }
            }
        }
    }
}
