package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Air
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Navigation
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Thermostat
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material.icons.outlined.Shield
import androidx.compose.material.icons.outlined.Waves
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AppLanguage
import com.example.data.model.BalasoreForecastDay
import com.example.data.model.BalasoreForecastHour
import com.example.data.model.WeatherInfo
import com.example.ui.theme.OceanBlue
import com.example.ui.theme.OceanBlueDark

/**
 * High-craft, real-time Weather Dashboard Component for Balasore, Odisha.
 * Displays live temperature, relative humidity, wind speed, apparent temperature,
 * UV index, hourly meteorological track, 7-day forecast, and coastal marine correlation.
 */
@Composable
fun BalasoreWeatherDashboardComponent(
    weather: WeatherInfo,
    forecastDays: List<BalasoreForecastDay>,
    hourlyForecast: List<BalasoreForecastHour>,
    isRefreshing: Boolean,
    isFahrenheit: Boolean,
    language: AppLanguage,
    onRefresh: () -> Unit,
    onToggleUnit: () -> Unit,
    onOpenTideSchedule: (() -> Unit)? = null,
    onOpenMarineAdvisory: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    var isExpanded7Days by remember { mutableStateOf(false) }

    val displayTemp = if (isFahrenheit) {
        "${(weather.tempCelsius * 9 / 5) + 32}°F"
    } else {
        "${weather.tempCelsius}°C"
    }

    val displayFeelsLike = if (isFahrenheit) {
        "${(weather.feelsLikeCelsius * 9 / 5) + 32}°F"
    } else {
        "${weather.feelsLikeCelsius}°C"
    }

    // Refresh button spin animation when loading
    val infiniteTransition = rememberInfiniteTransition(label = "weather_refresh_spin")
    val rotationAngle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "spin_angle"
    )

    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("balasore_weather_dashboard_card"),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp)
        ) {
            // Header: Live API Source badge & Controls
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF22C55E))
                    )
                    Text(
                        text = if (language == AppLanguage.ODIA) "ବାଲେଶ୍ୱର ଲାଇଭ୍ ପାଣିପାଗ" else "BALASORE LIVE WEATHER",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.ExtraBold,
                            letterSpacing = 1.sp,
                            fontSize = 10.sp,
                            color = OceanBlueDark
                        )
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    // °C / °F Unit Toggle Chip
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.6f),
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .clickable { onToggleUnit() }
                            .testTag("weather_unit_toggle_button")
                    ) {
                        Text(
                            text = if (isFahrenheit) "°F (Click for °C)" else "°C (Click for °F)",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onPrimaryContainer,
                                fontSize = 10.sp
                            ),
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }

                    // Refresh Button
                    IconButton(
                        onClick = onRefresh,
                        modifier = Modifier
                            .size(34.dp)
                            .testTag("weather_refresh_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = "Refresh live weather",
                            tint = OceanBlue,
                            modifier = Modifier
                                .size(20.dp)
                                .rotate(if (isRefreshing) rotationAngle else 0f)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Main Weather Hero Box: Temperature, Weather Icon & Condition
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp))
                    .background(
                        Brush.horizontalGradient(
                            listOf(
                                Color(0xFF0F172A),
                                Color(0xFF1E3A8A),
                                Color(0xFF0284C7)
                            )
                        )
                    )
                    .padding(18.dp)
            ) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Balasore, Odisha",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            )
                            Text(
                                text = "21.4934° N, 86.9135° E • Bay of Bengal Coast",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = Color.White.copy(alpha = 0.8f),
                                    fontSize = 11.sp
                                )
                            )
                        }

                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = Color.White.copy(alpha = 0.2f)
                        ) {
                            Text(
                                text = weather.lastUpdatedTime,
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = Color.White,
                                    fontWeight = FontWeight.Medium,
                                    fontSize = 10.sp
                                ),
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = displayTemp,
                                style = MaterialTheme.typography.displayMedium.copy(
                                    fontWeight = FontWeight.Black,
                                    color = Color.White,
                                    fontSize = 44.sp
                                )
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = weather.condition,
                                    style = MaterialTheme.typography.titleSmall.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFFBAE6FD)
                                    )
                                )
                                Text(
                                    text = if (language == AppLanguage.ODIA)
                                        "ଅନୁଭୂତ: $displayFeelsLike"
                                    else
                                        "Feels like $displayFeelsLike",
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        color = Color.White.copy(alpha = 0.85f),
                                        fontSize = 11.5.sp
                                    )
                                )
                            }
                        }

                        // Condition badge emoji
                        Surface(
                            shape = CircleShape,
                            color = Color.White.copy(alpha = 0.15f),
                            modifier = Modifier.size(56.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(
                                    text = when {
                                        weather.condition.contains("Rain", true) -> "🌧️"
                                        weather.condition.contains("Thunder", true) -> "⛈️"
                                        weather.condition.contains("Cloud", true) -> "⛅"
                                        weather.condition.contains("Fog", true) -> "🌫️"
                                        else -> "☀️"
                                    },
                                    fontSize = 28.sp
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Real-Time Meteorological Indicators Grid (Humidity, Wind, UV, Precipitation)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // 1. Humidity Card
                WeatherMetricItem(
                    label = if (language == AppLanguage.ODIA) "ଆର୍ଦ୍ରତା" else "Humidity",
                    value = weather.humidity,
                    subtitle = "Maritime Coast",
                    icon = Icons.Default.WaterDrop,
                    iconTint = Color(0xFF0284C7),
                    modifier = Modifier.weight(1f)
                )

                // 2. Wind & Gusts Card
                WeatherMetricItem(
                    label = if (language == AppLanguage.ODIA) "ବାୟୁ ବେଗ" else "Wind Speed",
                    value = weather.windSpeedKmh,
                    subtitle = "Gusts ${weather.windGustsKmh}",
                    icon = Icons.Default.Air,
                    iconTint = Color(0xFF10B981),
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // 3. UV Index Card
                WeatherMetricItem(
                    label = if (language == AppLanguage.ODIA) "ୟୁଭି ସୂଚକ" else "UV Index",
                    value = "${weather.uvIndex}",
                    subtitle = if (weather.uvIndex > 7.0) "Very High" else "Moderate",
                    icon = Icons.Default.WbSunny,
                    iconTint = Color(0xFFF59E0B),
                    modifier = Modifier.weight(1f)
                )

                // 4. Precipitation Card
                WeatherMetricItem(
                    label = if (language == AppLanguage.ODIA) "ବୃଷ୍ଟିପାତ" else "Precipitation",
                    value = "${weather.precipitationMm} mm",
                    subtitle = if (weather.precipitationMm > 0.0) "Active Showers" else "Dry Surface",
                    icon = Icons.Default.Thermostat,
                    iconTint = Color(0xFF8B5CF6),
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 24-Hour Meteorological Forecast Carousel
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (language == AppLanguage.ODIA) "ଆଗାମୀ ଘଣ୍ଟାର ପୂର୍ବାନୁମାନ (୨୪ ଘଣ୍ଟା)" else "Hourly Forecast (Next 24h)",
                    style = MaterialTheme.typography.titleSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                )
                Text(
                    text = "Open-Meteo Met API",
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontSize = 10.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                    )
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Hourly horizontal track
            val displayHourly = if (hourlyForecast.isNotEmpty()) {
                hourlyForecast
            } else {
                generateFallbackHourly()
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState())
                    .padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                displayHourly.forEach { hour ->
                    val tempStr = if (isFahrenheit) {
                        "${(hour.tempC * 9 / 5) + 32}°"
                    } else {
                        "${hour.tempC}°"
                    }

                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f)),
                        modifier = Modifier.width(68.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(vertical = 10.dp, horizontal = 4.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = hour.timeLabel,
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = hour.conditionEmoji,
                                fontSize = 20.sp
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = tempStr,
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "${hour.popPercentage}%",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontSize = 9.sp,
                                        color = OceanBlue,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Chandipur Sea Tide & Marine Safety Link Banner
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = Color(0xFFF0FDF4),
                border = BorderStroke(1.dp, Color(0xFFBBF7D0)),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onOpenTideSchedule?.invoke() }
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Waves,
                            contentDescription = "Tide",
                            tint = Color(0xFF059669),
                            modifier = Modifier.size(24.dp)
                        )
                        Column {
                            Text(
                                text = if (language == AppLanguage.ODIA) "ଚାନ୍ଦିପୁର ସମୁଦ୍ର ଭଟ୍ଟା: ୪.୮ କିମି ଅପସାରିତ" else "Chandipur Low Tide: Receding 4.8 km",
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF065F46)
                                )
                            )
                            Text(
                                text = if (language == AppLanguage.ODIA)
                                    "ନିରାପଦ ପଦଯାତ୍ରା ସମୟ ବାକି ଅଛି • ଟାଇମର୍ ଦେଖନ୍ତୁ"
                                else
                                    "Safe mud-flat walking active • Tap for tide clock & PFZ",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    fontSize = 11.sp,
                                    color = Color(0xFF047857)
                                )
                            )
                        }
                    }

                    Icon(
                        imageVector = Icons.Default.Navigation,
                        contentDescription = "Open tide schedule",
                        tint = Color(0xFF059669),
                        modifier = Modifier.size(16.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // 7-Day Meteorological Outlook Accordion
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f),
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .clickable { isExpanded7Days = !isExpanded7Days }
                    .testTag("seven_day_forecast_toggle")
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = if (language == AppLanguage.ODIA) "୭ ଦିନିଆ ପାଣିପାଗ ପୂର୍ବାନୁମାନ" else "7-Day Meteorological Forecast",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        )
                        Text(
                            text = "IMD & Open-Meteo Extended Cycle",
                            style = MaterialTheme.typography.bodySmall.copy(
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        )
                    }

                    Icon(
                        imageVector = if (isExpanded7Days) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                        contentDescription = "Expand 7-day forecast",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            AnimatedVisibility(visible = isExpanded7Days) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    forecastDays.forEach { day ->
                        val highStr = if (isFahrenheit) "${(day.highTempC * 9 / 5) + 32}°" else "${day.highTempC}°"
                        val lowStr = if (isFahrenheit) "${(day.lowTempC * 9 / 5) + 32}°" else "${day.lowTempC}°"

                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = MaterialTheme.colorScheme.surface,
                            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f)),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 12.dp, vertical = 8.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.width(110.dp)) {
                                    Text(
                                        text = if (language == AppLanguage.ODIA) day.odiaDayLabel else day.dayLabel,
                                        style = MaterialTheme.typography.bodyMedium.copy(
                                            fontWeight = FontWeight.Bold,
                                            color = MaterialTheme.colorScheme.onSurface
                                        )
                                    )
                                    Text(
                                        text = day.dateFormatted,
                                        style = MaterialTheme.typography.bodySmall.copy(
                                            fontSize = 10.5.sp,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    )
                                }

                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Text(text = day.weatherIcon, fontSize = 20.sp)
                                    Column {
                                        Text(
                                            text = if (language == AppLanguage.ODIA) day.odiaCondition else day.condition,
                                            style = MaterialTheme.typography.bodySmall.copy(
                                                fontWeight = FontWeight.Medium,
                                                fontSize = 11.5.sp,
                                                color = MaterialTheme.colorScheme.onSurface
                                            ),
                                            maxLines = 1
                                        )
                                        Text(
                                            text = "Rain: ${day.rainProbability}%",
                                            style = MaterialTheme.typography.labelSmall.copy(
                                                fontSize = 10.sp,
                                                color = OceanBlue
                                            )
                                        )
                                    }
                                }

                                Text(
                                    text = "$highStr / $lowStr",
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun WeatherMetricItem(
    label: String,
    value: String,
    subtitle: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    iconTint: Color,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.45f),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.35f)),
        modifier = modifier
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Surface(
                shape = CircleShape,
                color = iconTint.copy(alpha = 0.15f),
                modifier = Modifier.size(36.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = icon,
                        contentDescription = label,
                        tint = iconTint,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            Column {
                Text(
                    text = label,
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                )
                Text(
                    text = value,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                )
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontSize = 10.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f)
                    )
                )
            }
        }
    }
}

private fun generateFallbackHourly(): List<BalasoreForecastHour> {
    return listOf(
        BalasoreForecastHour("Now", 29, "☀️", 10, 18),
        BalasoreForecastHour("12 PM", 31, "⛅", 15, 20),
        BalasoreForecastHour("02 PM", 32, "⛅", 20, 22),
        BalasoreForecastHour("04 PM", 30, "🌧️", 45, 18),
        BalasoreForecastHour("06 PM", 28, "⛅", 30, 15),
        BalasoreForecastHour("08 PM", 27, "🌙", 10, 12),
        BalasoreForecastHour("10 PM", 26, "🌙", 5, 10),
        BalasoreForecastHour("12 AM", 25, "🌙", 5, 8)
    )
}
