package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.DirectionsWalk
import androidx.compose.material.icons.automirrored.filled.ShowChart
import androidx.compose.material.icons.filled.Air
import androidx.compose.material.icons.filled.BeachAccess
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Thermostat
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.filled.Water
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material.icons.filled.WbTwilight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.DailyForecastEntity
import com.example.data.local.ReviewEntity
import com.example.data.local.UserEntity
import com.example.data.local.WeatherCacheEntity
import com.example.ui.components.ReviewsSection
import com.example.ui.theme.AlertCyclone
import com.example.ui.theme.AlertNormal
import com.example.ui.theme.AlertWarning
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
import com.example.ui.theme.TideIncoming
import com.example.ui.theme.TideReceding
import kotlinx.coroutines.flow.Flow
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun WeatherScreen(
    weather: WeatherCacheEntity?,
    dailyForecasts: List<DailyForecastEntity> = emptyList(),
    currentUser: UserEntity? = null,
    onOpenAuth: () -> Unit = {},
    getReviewsForWeather: (String) -> Flow<List<ReviewEntity>> = { kotlinx.coroutines.flow.emptyFlow() },
    onSubmitWeatherReview: (weatherId: String, alertTitle: String, rating: Int, comment: String, guestName: String?) -> Unit = { _, _, _, _, _ -> },
    modifier: Modifier = Modifier
) {
    if (weather == null) {
        LazyColumn(
            modifier = modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp, 80.dp, 16.dp, 90.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                Text(
                    text = "Loading Balasore meteorological data...",
                    style = MaterialTheme.typography.bodyMedium,
                    color = BentoSlate500
                )
            }
        }
        return
    }

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp, 8.dp, 16.dp, 90.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Bento Weather Primary Tile
        item {
            BentoWeatherHeroCard(weather = weather)
        }

        // Coastal Alert Bento Card
        item {
            BentoCoastalAlertCard(weather = weather)
        }

        // Chandipur Vanishing Sea Tide Bento Card
        item {
            BentoChandipurTideCard(weather = weather)
        }

        // Atmospheric & Marine Parameters Grid
        item {
            BentoAtmosphericMetricsCard(weather = weather)
        }

        // 7-Day Weather Forecast cached in Room
        if (dailyForecasts.isNotEmpty()) {
            item {
                BentoDailyForecastCard(forecasts = dailyForecasts)
            }
        }

        // Tourism Weather Advisory Card
        item {
            BentoTourismAdvisoryCard(weather = weather)
        }

        // Community Weather Reports & Alert Feedback
        item {
            ReviewsSection(
                targetType = "WEATHER",
                targetId = "balasore_weather_today",
                targetTitle = "Live Weather Alerts & Ground Reports",
                reviewsFlow = getReviewsForWeather("balasore_weather_today"),
                currentUser = currentUser,
                onOpenAuth = onOpenAuth,
                onSubmitReview = { rating, comment, guestName ->
                    onSubmitWeatherReview("balasore_weather_today", "Balasore Weather Alerts", rating, comment, guestName)
                }
            )
        }
    }
}

/**
 * Bento Primary Weather Tile: Vibrant Royal Blue with rounded-3xl (28.dp)
 */
@Composable
fun BentoWeatherHeroCard(
    weather: WeatherCacheEntity,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(containerColor = BentoPrimaryBlue),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
        modifier = modifier
            .fillMaxWidth()
            .testTag("current_weather_card")
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column {
                    Text(
                        text = "Balasore City & Coast",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = Color.White
                    )
                    Text(
                        text = "North Bay of Bengal • 21.49°N 86.91°E",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.White.copy(alpha = 0.8f)
                    )
                }

                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = Color.White.copy(alpha = 0.2f),
                    border = BorderStroke(0.5.dp, Color.White.copy(alpha = 0.3f))
                ) {
                    Text(
                        text = "LIVE RADAR",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp,
                            fontSize = 10.sp
                        ),
                        color = Color.White,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.Bottom) {
                    Text(
                        text = "${weather.temperature.toInt()}°",
                        style = MaterialTheme.typography.displayLarge.copy(
                            fontWeight = FontWeight.Light,
                            fontSize = 58.sp
                        ),
                        color = Color.White
                    )
                    Text(
                        text = "C",
                        style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                        color = Color.White.copy(alpha = 0.9f),
                        modifier = Modifier.padding(bottom = 12.dp)
                    )
                }

                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = weather.weatherDescription,
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = Color.White
                    )
                    Text(
                        text = "Feels like ${weather.apparentTemperature.toInt()}°C",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.White.copy(alpha = 0.8f)
                    )
                    Text(
                        text = "H: ${weather.maxTemp.toInt()}°  L: ${weather.minTemp.toInt()}°",
                        style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold),
                        color = Color.White.copy(alpha = 0.9f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            val timeStr = SimpleDateFormat("h:mm a, dd MMM", Locale.getDefault()).format(Date(weather.lastUpdated))
            Text(
                text = "Last updated: $timeStr",
                style = MaterialTheme.typography.bodySmall,
                fontSize = 11.sp,
                color = Color.White.copy(alpha = 0.7f)
            )
        }
    }
}

/**
 * Coastal Alert Bento Card
 */
@Composable
fun BentoCoastalAlertCard(
    weather: WeatherCacheEntity,
    modifier: Modifier = Modifier
) {
    val isAlert = weather.alertLevel != "NORMAL"
    val badgeBg = if (isAlert) BentoRedBg else BentoGreenBg
    val borderCol = if (isAlert) BentoRedBg else BentoBorder
    val iconCol = if (isAlert) BentoRedText else BentoGreenText
    val iconSymbol = if (isAlert) "⚠️" else "✅"

    Card(
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = BentoCardWhite),
        border = BorderStroke(1.dp, borderCol),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = modifier
            .fillMaxWidth()
            .testTag("coastal_alert_card")
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.Top
        ) {
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = badgeBg,
                modifier = Modifier.size(40.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(iconSymbol, fontSize = 18.sp)
                }
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = weather.alertTitle,
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                    color = BentoSlate900
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = weather.alertMessage,
                    style = MaterialTheme.typography.bodySmall,
                    color = BentoSlate500
                )
            }
        }
    }
}

/**
 * Chandipur Tide Bento Card
 */
@Composable
fun BentoChandipurTideCard(
    weather: WeatherCacheEntity,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = BentoCardWhite),
        border = BorderStroke(1.dp, BentoBorder),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = modifier
            .fillMaxWidth()
            .testTag("chandipur_tide_card")
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = BentoBlueLight,
                        modifier = Modifier.size(36.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text("🌊", fontSize = 16.sp)
                        }
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "Chandipur Vanishing Sea",
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                        color = BentoSlate900
                    )
                }

                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = if (weather.tideState == "LOW_TIDE") BentoBlueLight else BentoRedBg
                ) {
                    Text(
                        text = weather.tideState.replace("_", " "),
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 10.sp,
                            letterSpacing = 0.5.sp
                        ),
                        color = if (weather.tideState == "LOW_TIDE") BentoPrimaryBlue else BentoRedText,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = weather.tideDescription,
                style = MaterialTheme.typography.bodyMedium,
                color = BentoSlate500
            )

            Spacer(modifier = Modifier.height(12.dp))

            Surface(
                shape = RoundedCornerShape(16.dp),
                color = BentoBlueLight,
                border = BorderStroke(1.dp, BentoBorder),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.DirectionsWalk,
                        contentDescription = null,
                        tint = BentoPrimaryBlue,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (weather.tideState == "LOW_TIDE") {
                            "Safe Seabed Walking: Ideal window to stroll out 1-4 km towards vanishing horizon."
                        } else {
                            "Lifeguard Alert: Watch water line; return towards beach promenade."
                        },
                        style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium),
                        color = BentoSlate700
                    )
                }
            }
        }
    }
}

/**
 * Atmospheric & Marine Parameters Grid: 6 Bento Tiles
 */
@Composable
fun BentoAtmosphericMetricsCard(
    weather: WeatherCacheEntity,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = BentoCardWhite),
        border = BorderStroke(1.dp, BentoBorder),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Text(
                text = "Atmospheric & Marine Parameters",
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                color = BentoSlate900
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                BentoMetricItem(
                    icon = Icons.Default.Air,
                    label = "Wind Speed",
                    value = "${weather.windSpeed.toInt()} km/h",
                    sub = "Gusts: ${weather.windGusts.toInt()} km/h",
                    modifier = Modifier.weight(1f)
                )
                BentoMetricItem(
                    icon = Icons.Default.WaterDrop,
                    label = "Humidity",
                    value = "${weather.humidity}%",
                    sub = "Coastal air",
                    modifier = Modifier.weight(1f)
                )
                BentoMetricItem(
                    icon = Icons.Default.WbSunny,
                    label = "UV Index",
                    value = "${weather.uvIndex.toInt()}",
                    sub = if (weather.uvIndex > 7) "Very High" else "Moderate",
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                BentoMetricItem(
                    icon = Icons.Default.WbSunny,
                    label = "Sunrise",
                    value = weather.sunrise,
                    sub = "Bay horizon",
                    modifier = Modifier.weight(1f)
                )
                BentoMetricItem(
                    icon = Icons.Default.WbTwilight,
                    label = "Sunset",
                    value = weather.sunset,
                    sub = "Golden hour",
                    modifier = Modifier.weight(1f)
                )
                BentoMetricItem(
                    icon = Icons.Default.Thermostat,
                    label = "Max / Min",
                    value = "${weather.maxTemp.toInt()}° / ${weather.minTemp.toInt()}°",
                    sub = "Today",
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
fun BentoMetricItem(
    icon: ImageVector,
    label: String,
    value: String,
    sub: String,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = BentoBlueLight,
        border = BorderStroke(1.dp, BentoBorder),
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.padding(10.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Icon(imageVector = icon, contentDescription = null, tint = BentoPrimaryBlue, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = label, style = MaterialTheme.typography.bodySmall, fontSize = 10.sp, color = BentoSlate500)
            Text(text = value, style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold), color = BentoSlate900)
            Text(text = sub, style = MaterialTheme.typography.bodySmall, fontSize = 10.sp, color = BentoSlate400)
        }
    }
}

@Composable
fun BentoTourismAdvisoryCard(
    weather: WeatherCacheEntity,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = BentoCardWhite),
        border = BorderStroke(1.dp, BentoBorder),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = BentoBlueLight,
                    modifier = Modifier.size(36.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text("🏖️", fontSize = 16.sp)
                    }
                }
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = "Balasore Traveler Weather Guide",
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                    color = BentoSlate900
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            BentoTravelTip(
                spot = "Chandipur & Talasari Beach",
                tip = "Best visiting time: 6:00 AM - 9:30 AM and after 4:00 PM when solar heat subsides."
            )
            Spacer(modifier = Modifier.height(8.dp))
            BentoTravelTip(
                spot = "Panchalingeswar & Nilagiri Hills",
                tip = "Mountain stream has pleasant cool water. Wear sturdy non-slip footwear on forest steps."
            )
            Spacer(modifier = Modifier.height(8.dp))
            BentoTravelTip(
                spot = "Remuna & Emami Jagannath",
                tip = "Evening aarti hours (6:00 PM – 8:00 PM) offer beautiful illuminations and cooler temperatures."
            )
        }
    }
}

@Composable
fun BentoTravelTip(spot: String, tip: String) {
    Column {
        Text(text = spot, style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold), color = BentoPrimaryBlue)
        Text(text = tip, style = MaterialTheme.typography.bodySmall, color = BentoSlate500)
    }
}

@Composable
fun BentoDailyForecastCard(
    forecasts: List<DailyForecastEntity>,
    modifier: Modifier = Modifier
) {
    if (forecasts.isEmpty()) return

    val fiveDayForecasts = remember(forecasts) { forecasts.take(5) }
    var selectedDayIndex by remember { mutableStateOf(0) }
    val activeItem = fiveDayForecasts.getOrNull(selectedDayIndex) ?: fiveDayForecasts.firstOrNull()

    Card(
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = BentoCardWhite),
        border = BorderStroke(1.dp, BentoBorder),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = modifier
            .fillMaxWidth()
            .testTag("daily_forecast_card")
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = BentoBlueLight,
                        modifier = Modifier.size(36.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ShowChart,
                                contentDescription = null,
                                tint = BentoPrimaryBlue,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = "5-Day Weather Forecast",
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                            color = BentoSlate900
                        )
                        Text(
                            text = "Interactive temperature curve & activity planner",
                            style = MaterialTheme.typography.bodySmall,
                            fontSize = 11.sp,
                            color = BentoSlate500
                        )
                    }
                }

                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = BentoGreenBg
                ) {
                    Text(
                        text = "5-DAY TREND",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 0.5.sp,
                            fontSize = 9.sp
                        ),
                        color = BentoGreenText,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Chart Visualization
            WeatherForecastChart(
                forecasts = fiveDayForecasts,
                selectedIndex = selectedDayIndex,
                onSelectDay = { selectedDayIndex = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(170.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(BentoBlueLight.copy(alpha = 0.4f))
                    .padding(horizontal = 8.dp, vertical = 10.dp)
            )

            // Selected Day Planner Detail Pill
            if (activeItem != null) {
                Spacer(modifier = Modifier.height(10.dp))
                Surface(
                    shape = RoundedCornerShape(14.dp),
                    color = BentoCardWhite,
                    border = BorderStroke(1.dp, BentoPrimaryBlue.copy(alpha = 0.25f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 12.dp, vertical = 10.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "${activeItem.dayOfWeek} Activity Planning:",
                                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                                    color = BentoSlate900
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = activeItem.weatherDescription,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = BentoPrimaryBlue,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                            val recommendation = when {
                                activeItem.weatherCode in 51..67 || activeItem.weatherCode in 80..82 || activeItem.weatherCode >= 95 ->
                                    "🌧️ Rain expected. Indoor day recommended: Visit Remuna Khirachora Gopinath or Emami Jagannath temple."
                                activeItem.maxTemp >= 34.0 ->
                                    "☀️ Hot afternoon (${activeItem.maxTemp.toInt()}°C). Best visiting Chandipur / Talasari beach early morning or after 4 PM."
                                else ->
                                    "🌤️ Pleasant coastal weather (${activeItem.maxTemp.toInt()}°C)! Ideal for Panchalingeswar hill trek or beach strolls."
                            }
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = recommendation,
                                style = MaterialTheme.typography.bodySmall,
                                fontSize = 11.sp,
                                color = BentoSlate700
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // 5-Day Horizontal Selection Cards
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(fiveDayForecasts.size) { index ->
                    val item = fiveDayForecasts[index]
                    DailyForecastItem(
                        item = item,
                        isSelected = index == selectedDayIndex,
                        onClick = { selectedDayIndex = index }
                    )
                }
            }
        }
    }
}

/**
 * Clean visual line and area chart showing 5-day Max and Min temperatures
 * with smooth bezier curves, gradient fills, and data points.
 */
@Composable
fun WeatherForecastChart(
    forecasts: List<DailyForecastEntity>,
    selectedIndex: Int,
    onSelectDay: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    if (forecasts.isEmpty()) return

    val maxTemps = remember(forecasts) { forecasts.map { it.maxTemp.toFloat() } }
    val minTemps = remember(forecasts) { forecasts.map { it.minTemp.toFloat() } }

    val highest = remember(maxTemps) { (maxTemps.maxOrNull() ?: 35f) + 2f }
    val lowest = remember(minTemps) { (minTemps.minOrNull() ?: 20f) - 2f }
    val tempRange = (highest - lowest).coerceAtLeast(1f)

    Box(
        modifier = modifier.testTag("forecast_temperature_chart")
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val width = size.width
            val height = size.height
            val n = forecasts.size
            if (n < 2) return@Canvas

            val stepX = width / (n - 1)
            val paddingY = 24.dp.toPx()
            val availableHeight = height - (paddingY * 2)

            fun getY(temp: Float): Float {
                val ratio = (temp - lowest) / tempRange
                return height - paddingY - (ratio * availableHeight)
            }

            // Draw horizontal subtle grid guidelines
            val gridLines = 3
            for (i in 0..gridLines) {
                val lineY = paddingY + (i * availableHeight / gridLines)
                drawLine(
                    color = Color.Black.copy(alpha = 0.05f),
                    start = Offset(0f, lineY),
                    end = Offset(width, lineY),
                    strokeWidth = 1.dp.toPx()
                )
            }

            // Generate Path for Max Temps Area and Line
            val maxPath = Path()
            val maxAreaPath = Path()
            val maxPoints = mutableListOf<Offset>()

            for (i in 0 until n) {
                val x = i * stepX
                val y = getY(maxTemps[i])
                val pt = Offset(x, y)
                maxPoints.add(pt)

                if (i == 0) {
                    maxPath.moveTo(pt.x, pt.y)
                    maxAreaPath.moveTo(pt.x, height)
                    maxAreaPath.lineTo(pt.x, pt.y)
                } else {
                    val prev = maxPoints[i - 1]
                    val cx = (prev.x + pt.x) / 2f
                    maxPath.cubicTo(cx, prev.y, cx, pt.y, pt.x, pt.y)
                    maxAreaPath.cubicTo(cx, prev.y, cx, pt.y, pt.x, pt.y)
                }
            }
            maxAreaPath.lineTo(maxPoints.last().x, height)
            maxAreaPath.close()

            // Draw Max Temp subtle gradient fill
            drawPath(
                path = maxAreaPath,
                brush = Brush.verticalGradient(
                    colors = listOf(
                        BentoPrimaryBlue.copy(alpha = 0.28f),
                        BentoPrimaryBlue.copy(alpha = 0.02f)
                    )
                ),
                style = Fill
            )

            // Draw Max Temp Line
            drawPath(
                path = maxPath,
                color = BentoPrimaryBlue,
                style = Stroke(width = 3.dp.toPx(), cap = StrokeCap.Round)
            )

            // Generate Path for Min Temps Line
            val minPath = Path()
            val minPoints = mutableListOf<Offset>()

            for (i in 0 until n) {
                val x = i * stepX
                val y = getY(minTemps[i])
                val pt = Offset(x, y)
                minPoints.add(pt)

                if (i == 0) {
                    minPath.moveTo(pt.x, pt.y)
                } else {
                    val prev = minPoints[i - 1]
                    val cx = (prev.x + pt.x) / 2f
                    minPath.cubicTo(cx, prev.y, cx, pt.y, pt.x, pt.y)
                }
            }

            // Draw Min Temp Line (Cool Cyan)
            drawPath(
                path = minPath,
                color = Color(0xFF0284C7),
                style = Stroke(
                    width = 2.dp.toPx(),
                    cap = StrokeCap.Round
                )
            )

            // Draw points and labels
            for (i in 0 until n) {
                val maxPt = maxPoints[i]
                val minPt = minPoints[i]
                val isSelected = i == selectedIndex

                // Highlight selected column indicator
                if (isSelected) {
                    drawLine(
                        color = BentoPrimaryBlue.copy(alpha = 0.35f),
                        start = Offset(maxPt.x, 0f),
                        end = Offset(maxPt.x, height),
                        strokeWidth = 2.dp.toPx()
                    )
                }

                // Max Temp Point
                drawCircle(
                    color = if (isSelected) Color(0xFFEA580C) else BentoPrimaryBlue,
                    radius = if (isSelected) 6.dp.toPx() else 4.dp.toPx(),
                    center = maxPt
                )
                drawCircle(
                    color = Color.White,
                    radius = if (isSelected) 3.dp.toPx() else 2.dp.toPx(),
                    center = maxPt
                )

                // Min Temp Point
                drawCircle(
                    color = Color(0xFF0284C7),
                    radius = if (isSelected) 5.dp.toPx() else 3.5.dp.toPx(),
                    center = minPt
                )
                drawCircle(
                    color = Color.White,
                    radius = if (isSelected) 2.5.dp.toPx() else 1.8.dp.toPx(),
                    center = minPt
                )

                // Draw Text labels via nativeCanvas
                drawContext.canvas.nativeCanvas.apply {
                    val textPaint = android.graphics.Paint().apply {
                        color = android.graphics.Color.DKGRAY
                        textSize = 28f
                        textAlign = android.graphics.Paint.Align.CENTER
                        isAntiAlias = true
                        typeface = android.graphics.Typeface.create(android.graphics.Typeface.DEFAULT, android.graphics.Typeface.BOLD)
                    }

                    // Max temp text above point
                    textPaint.color = if (isSelected) android.graphics.Color.parseColor("#EA580C") else android.graphics.Color.parseColor("#1D4ED8")
                    drawText("${maxTemps[i].toInt()}°", maxPt.x, maxPt.y - 12f, textPaint)

                    // Min temp text below point
                    textPaint.textSize = 24f
                    textPaint.color = android.graphics.Color.parseColor("#0284C7")
                    drawText("${minTemps[i].toInt()}°", minPt.x, minPt.y + 30f, textPaint)
                }
            }
        }

        // Invisible touch targets for selecting days on the chart
        Row(
            modifier = Modifier.fillMaxSize()
        ) {
            for (i in 0 until forecasts.size) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxSize()
                        .clickable { onSelectDay(i) }
                )
            }
        }
    }
}

@Composable
private fun DailyForecastItem(
    item: DailyForecastEntity,
    isSelected: Boolean = false,
    onClick: () -> Unit = {}
) {
    val iconEmoji = when {
        item.weatherCode in 0..1 -> "☀️"
        item.weatherCode in 2..3 -> "⛅"
        item.weatherCode in 45..48 -> "🌫️"
        item.weatherCode in 51..67 -> "🌧️"
        item.weatherCode in 80..82 -> "🌦️"
        item.weatherCode >= 95 -> "⛈️"
        else -> "🌤️"
    }

    Surface(
        shape = RoundedCornerShape(16.dp),
        color = if (isSelected) BentoPrimaryBlue.copy(alpha = 0.12f) else BentoBlueLight.copy(alpha = 0.6f),
        border = BorderStroke(
            if (isSelected) 1.5.dp else 1.dp,
            if (isSelected) BentoPrimaryBlue else BentoBorder
        ),
        modifier = Modifier
            .width(96.dp)
            .clip(RoundedCornerShape(16.dp))
            .clickable { onClick() }
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = item.dayOfWeek,
                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                color = if (isSelected) BentoPrimaryBlue else BentoSlate900
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = iconEmoji,
                fontSize = 24.sp
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = "${item.maxTemp.toInt()}° / ${item.minTemp.toInt()}°",
                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                color = BentoPrimaryBlue
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = item.weatherDescription,
                style = MaterialTheme.typography.bodySmall,
                fontSize = 9.sp,
                color = BentoSlate500,
                maxLines = 1
            )
        }
    }
}
