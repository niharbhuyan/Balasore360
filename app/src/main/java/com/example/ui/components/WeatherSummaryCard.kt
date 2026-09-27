package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AppLanguage
import com.example.data.model.BalasoreForecastDay
import com.example.data.model.WeatherInfo
import com.example.ui.theme.*

/**
 * Dedicated 'Weather Summary' card prominently featured in the main Balasore 360 dashboard.
 * Utilizes the Open-Meteo public weather API to display:
 * - Current temperature & condition
 * - Real-time relative humidity (%)
 * - 3-day forecast outlook for Balasore district (Today, Tomorrow, Day After)
 * - Interactive Celsius / Fahrenheit toggle and instant refresh trigger
 */
@Composable
fun WeatherSummaryCard(
    weather: WeatherInfo,
    forecastDays: List<BalasoreForecastDay>,
    language: AppLanguage,
    isFahrenheit: Boolean,
    onToggleTempUnit: () -> Unit,
    onRefresh: () -> Unit,
    onNavigateToWeather: () -> Unit,
    modifier: Modifier = Modifier
) {
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
            .padding(horizontal = 16.dp, vertical = 6.dp)
            .testTag("weather_summary_card"),
        shape = RoundedCornerShape(22.dp),
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
            // Header Bar
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFE0F2FE)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.WbSunny,
                            contentDescription = "Weather Icon",
                            tint = Color(0xFF0284C7),
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = if (language == AppLanguage.ODIA) "ବାଲେଶ୍ୱର ପାଣିପାଗ ସାରାଂଶ" else "Weather Summary",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.ExtraBold,
                                    fontSize = 16.sp,
                                    color = BentoSlate900
                                )
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = Color(0xFFDCFCE7),
                                border = BorderStroke(0.5.dp, Color(0xFF86EFAC))
                            ) {
                                Text(
                                    text = "LIVE",
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF166534),
                                    modifier = Modifier.padding(horizontal = 5.dp, vertical = 1.dp)
                                )
                            }
                        }
                        Text(
                            text = if (language == AppLanguage.ODIA) "୨୧.୪୯° ଉ, ୮୬.୯୧° ପୂ • ପବ୍ଲିକ API" else "21.49°N, 86.91°E • Open-Meteo API",
                            style = MaterialTheme.typography.bodySmall.copy(
                                fontSize = 11.sp,
                                color = BentoSlate500
                            )
                        )
                    }
                }

                // Temp Unit Toggle Pill
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = Color(0xFFF1F5F9),
                    border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
                    modifier = Modifier
                        .clickable { onToggleTempUnit() }
                        .testTag("summary_temp_unit_toggle")
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "°C",
                            fontSize = 11.sp,
                            fontWeight = if (!isFahrenheit) FontWeight.ExtraBold else FontWeight.Normal,
                            color = if (!isFahrenheit) OceanBlue else BentoSlate400
                        )
                        Text(
                            text = " | ",
                            fontSize = 10.sp,
                            color = BentoSlate400
                        )
                        Text(
                            text = "°F",
                            fontSize = 11.sp,
                            fontWeight = if (isFahrenheit) FontWeight.ExtraBold else FontWeight.Normal,
                            color = if (isFahrenheit) OceanBlue else BentoSlate400
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Main Metrics Highlight Banner
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(
                        Brush.horizontalGradient(
                            listOf(Color(0xFF0284C7), Color(0xFF0369A1))
                        )
                    )
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Row(verticalAlignment = Alignment.Bottom) {
                            Text(
                                text = formatTemp(weather.tempCelsius),
                                style = MaterialTheme.typography.displaySmall.copy(
                                    fontWeight = FontWeight.Black,
                                    color = Color.White
                                )
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "(${weather.condition})",
                                style = MaterialTheme.typography.titleSmall.copy(
                                    color = Color(0xFFE0F2FE),
                                    fontWeight = FontWeight.SemiBold
                                ),
                                modifier = Modifier.padding(bottom = 6.dp)
                            )
                        }
                        Text(
                            text = if (language == AppLanguage.ODIA)
                                "ଅନୁଭୂତ: ${formatTemp(weather.feelsLikeCelsius)} • ବାୟୁ: ${weather.windSpeedKmh}"
                            else
                                "Feels like: ${formatTemp(weather.feelsLikeCelsius)} • Wind: ${weather.windSpeedKmh}",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = Color(0xFFBAE6FD),
                                fontSize = 11.5.sp
                            )
                        )
                    }

                    // Prominent Humidity Badge
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = Color.White.copy(alpha = 0.18f),
                        border = BorderStroke(1.dp, Color.White.copy(alpha = 0.35f))
                    ) {
                        Column(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.WaterDrop,
                                    contentDescription = "Humidity",
                                    tint = Color(0xFF7DD3FC),
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = weather.humidity,
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.ExtraBold,
                                        color = Color.White,
                                        fontSize = 15.sp
                                    )
                                )
                            }
                            Text(
                                text = if (language == AppLanguage.ODIA) "ଆର୍ଦ୍ରତା" else "Humidity",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontSize = 10.sp,
                                    color = Color(0xFFE0F2FE)
                                )
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // 3-Day Forecast Section
            Text(
                text = if (language == AppLanguage.ODIA) "୩-ଦିନିଆ ପାଣିପାଗ ପୂର୍ବାନୁମାନ" else "3-Day Forecast for Balasore",
                style = MaterialTheme.typography.labelLarge.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    color = BentoSlate800
                )
            )

            Spacer(modifier = Modifier.height(8.dp))

            val displayForecast = forecastDays.take(3).ifEmpty {
                listOf(
                    BalasoreForecastDay(
                        id = "day_1",
                        dayLabel = if (language == AppLanguage.ODIA) "ଆଜି" else "Today",
                        dateFormatted = "Day 1",
                        odiaDayLabel = "ଆଜି",
                        highTempC = weather.tempCelsius + 3,
                        lowTempC = weather.tempCelsius - 4,
                        condition = weather.condition,
                        odiaCondition = "ଆଂଶିକ ମେଘୁଆ",
                        weatherIcon = "partly_cloudy",
                        rainProbability = 15,
                        windSummary = "18 km/h",
                        humidity = "74%",
                        uvIndex = "6",
                        marineNotice = "Safe"
                    ),
                    BalasoreForecastDay(
                        id = "day_2",
                        dayLabel = if (language == AppLanguage.ODIA) "ଆସନ୍ତାକାଲି" else "Tomorrow",
                        dateFormatted = "Day 2",
                        odiaDayLabel = "ଆସନ୍ତାକାଲି",
                        highTempC = weather.tempCelsius + 2,
                        lowTempC = weather.tempCelsius - 5,
                        condition = "Partly Cloudy",
                        odiaCondition = "ଆଂଶିକ ମେଘୁଆ",
                        weatherIcon = "partly_cloudy",
                        rainProbability = 20,
                        windSummary = "16 km/h",
                        humidity = "72%",
                        uvIndex = "6",
                        marineNotice = "Safe"
                    ),
                    BalasoreForecastDay(
                        id = "day_3",
                        dayLabel = if (language == AppLanguage.ODIA) "ପରଦିନ" else "Day After",
                        dateFormatted = "Day 3",
                        odiaDayLabel = "ପରଦିନ",
                        highTempC = weather.tempCelsius + 4,
                        lowTempC = weather.tempCelsius - 3,
                        condition = "Sunny & Clear",
                        odiaCondition = "ଖରାଟିଆ",
                        weatherIcon = "clear_day",
                        rainProbability = 10,
                        windSummary = "14 km/h",
                        humidity = "68%",
                        uvIndex = "7",
                        marineNotice = "Safe"
                    )
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                displayForecast.forEachIndexed { index, forecastDay ->
                    Surface(
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp),
                        color = Color(0xFFF8FAFC),
                        border = BorderStroke(1.dp, Color(0xFFE2E8F0))
                    ) {
                        Column(
                            modifier = Modifier.padding(8.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = when (index) {
                                    0 -> if (language == AppLanguage.ODIA) "ଆଜି" else "Today"
                                    1 -> if (language == AppLanguage.ODIA) "ଆସନ୍ତାକାଲି" else "Tomorrow"
                                    else -> forecastDay.dayLabel.take(3)
                                },
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 11.sp,
                                    color = BentoSlate700
                                )
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Icon(
                                imageVector = when {
                                    forecastDay.condition.contains("Rain", ignoreCase = true) -> Icons.Default.Thunderstorm
                                    forecastDay.condition.contains("Cloud", ignoreCase = true) -> Icons.Default.Cloud
                                    else -> Icons.Default.WbSunny
                                },
                                contentDescription = forecastDay.condition,
                                tint = when {
                                    forecastDay.condition.contains("Rain", ignoreCase = true) -> Color(0xFF0284C7)
                                    forecastDay.condition.contains("Cloud", ignoreCase = true) -> Color(0xFF64748B)
                                    else -> Color(0xFFD97706)
                                },
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "${formatTemp(forecastDay.highTempC)} / ${formatTemp(forecastDay.lowTempC)}",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 10.5.sp,
                                    color = BentoSlate900
                                )
                            )
                            Text(
                                text = "🌧️ ${forecastDay.rainProbability}%",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    fontSize = 9.5.sp,
                                    color = Color(0xFF0284C7)
                                )
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Footer Bar: Last Updated & Deep Dive
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .clickable { onRefresh() }
                        .testTag("weather_summary_refresh_btn")
                ) {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = "Refresh",
                        tint = OceanBlue,
                        modifier = Modifier.size(13.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = if (language == AppLanguage.ODIA) "ଅପଡେଟ୍ କରନ୍ତୁ" else "Refresh Live API",
                        fontSize = 10.5.sp,
                        color = OceanBlue,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .clickable { onNavigateToWeather() }
                        .testTag("weather_summary_view_more_btn")
                ) {
                    Text(
                        text = if (language == AppLanguage.ODIA) "ସମ୍ପୂର୍ଣ୍ଣ ପୂର୍ବାନୁମାନ ଓ ଜୁଆର ରାଡାର" else "7-Day & Tidal Radar",
                        fontSize = 11.sp,
                        color = OceanBlue,
                        fontWeight = FontWeight.Bold
                    )
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = "View more",
                        tint = OceanBlue,
                        modifier = Modifier.size(14.dp)
                    )
                }
            }
        }
    }
}
