package com.example.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Air
import androidx.compose.material.icons.filled.BeachAccess
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material.icons.filled.CloudQueue
import androidx.compose.material.icons.filled.Grain
import androidx.compose.material.icons.filled.NightsStay
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Thunderstorm
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material.icons.filled.WbCloudy
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material.icons.filled.WbTwilight
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.DailyForecastEntity
import com.example.data.local.WeatherCacheEntity
import com.example.ui.theme.BentoBlueLight
import com.example.ui.theme.BentoBorder
import com.example.ui.theme.BentoCardWhite
import com.example.ui.theme.BentoPrimaryBlue
import com.example.ui.theme.BentoSlate400
import com.example.ui.theme.BentoSlate500
import com.example.ui.theme.BentoSlate700
import com.example.ui.theme.BentoSlate900
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Weather Icon Set mapping WMO weather codes and conditions to curated
 * Material Symbols and contextual colors.
 */
object WeatherIconSet {
    data class WeatherVisual(
        val icon: ImageVector,
        val label: String,
        val tint: Color,
        val backgroundGradient: Brush
    )

    fun getVisualForCode(code: Int, isDay: Boolean = true): WeatherVisual {
        return when (code) {
            0 -> if (isDay) {
                WeatherVisual(
                    icon = Icons.Default.WbSunny,
                    label = "Clear Sky",
                    tint = Color(0xFFF59E0B), // Warm amber
                    backgroundGradient = Brush.verticalGradient(listOf(Color(0xFF1E40AF), Color(0xFF3B82F6)))
                )
            } else {
                WeatherVisual(
                    icon = Icons.Default.NightsStay,
                    label = "Clear Night",
                    tint = Color(0xFFE0E7FF),
                    backgroundGradient = Brush.verticalGradient(listOf(Color(0xFF0F172A), Color(0xFF1E293B)))
                )
            }
            1, 2 -> WeatherVisual(
                icon = Icons.Default.WbCloudy,
                label = "Partly Cloudy",
                tint = Color(0xFFFDE68A),
                backgroundGradient = Brush.verticalGradient(listOf(Color(0xFF1D4ED8), Color(0xFF60A5FA)))
            )
            3 -> WeatherVisual(
                icon = Icons.Default.Cloud,
                label = "Overcast",
                tint = Color(0xFFE2E8F0),
                backgroundGradient = Brush.verticalGradient(listOf(Color(0xFF334155), Color(0xFF64748B)))
            )
            45, 48 -> WeatherVisual(
                icon = Icons.Default.Grain,
                label = "Coastal Fog",
                tint = Color(0xFFCBD5E1),
                backgroundGradient = Brush.verticalGradient(listOf(Color(0xFF475569), Color(0xFF94A3B8)))
            )
            51, 53, 55 -> WeatherVisual(
                icon = Icons.Default.WaterDrop,
                label = "Light Drizzle",
                tint = Color(0xFF93C5FD),
                backgroundGradient = Brush.verticalGradient(listOf(Color(0xFF1E3A8A), Color(0xFF2563EB)))
            )
            61, 63, 65 -> WeatherVisual(
                icon = Icons.Default.BeachAccess,
                label = "Rain Showers",
                tint = Color(0xFF67E8F9),
                backgroundGradient = Brush.verticalGradient(listOf(Color(0xFF0C4A6E), Color(0xFF0284C7)))
            )
            80, 81, 82 -> WeatherVisual(
                icon = Icons.Default.WaterDrop,
                label = "Coastal Downpour",
                tint = Color(0xFF38BDF8),
                backgroundGradient = Brush.verticalGradient(listOf(Color(0xFF0F172A), Color(0xFF0369A1)))
            )
            95, 96, 99 -> WeatherVisual(
                icon = Icons.Default.Thunderstorm,
                label = "Thunderstorm / Kalbaisakhi",
                tint = Color(0xFFFBBF24),
                backgroundGradient = Brush.verticalGradient(listOf(Color(0xFF1E1B4B), Color(0xFF4338CA)))
            )
            else -> WeatherVisual(
                icon = Icons.Default.CloudQueue,
                label = "Coastal Weather",
                tint = Color(0xFFBAE6FD),
                backgroundGradient = Brush.verticalGradient(listOf(Color(0xFF1E40AF), Color(0xFF3B82F6)))
            )
        }
    }
}

/**
 * Real-Time Weather Widget Component:
 * Displays current temperature and conditions for Balasore using a curated
 * weather icon set, atmospheric metrics, and a 3-day forecast summary.
 */
@Composable
fun RealtimeWeatherWidget(
    weather: WeatherCacheEntity?,
    dailyForecasts: List<DailyForecastEntity>,
    onRefresh: () -> Unit,
    onViewFullForecast: () -> Unit = {},
    isRefreshing: Boolean = false,
    modifier: Modifier = Modifier
) {
    val currentTemp = weather?.temperature?.toInt() ?: 29
    val apparentTemp = weather?.apparentTemperature?.toInt() ?: 32
    val weatherDesc = weather?.weatherDescription ?: "Mainly Clear Coastal Sky"
    val visual = WeatherIconSet.getVisualForCode(weather?.weatherCode ?: 1, isDay = true)

    // Radar pulse animation
    val infiniteTransition = rememberInfiniteTransition(label = "radar_pulse")
    val pulseAlpha by infiniteTransition.animateFloat(
        initialValue = 0.4f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse_alpha"
    )

    Card(
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = modifier
            .fillMaxWidth()
            .testTag("realtime_weather_widget")
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(visual.backgroundGradient)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                // Top Row: Location Header + Live Radar & Refresh Action
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Text(
                                text = "Balasore, Odisha",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 0.2.sp
                                ),
                                color = Color.White
                            )
                            // Live radar pulsing dot
                            Box(
                                modifier = Modifier
                                    .size(8.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFF22C55E).copy(alpha = pulseAlpha))
                            )
                        }
                        Text(
                            text = "North Bay of Bengal Coast • 21.49°N, 86.91°E",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.White.copy(alpha = 0.8f)
                        )
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            shape = RoundedCornerShape(20.dp),
                            color = Color.White.copy(alpha = 0.18f),
                            border = BorderStroke(0.5.dp, Color.White.copy(alpha = 0.3f))
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Text(
                                    text = "LIVE",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 10.sp,
                                        letterSpacing = 0.8.sp
                                    ),
                                    color = Color.White
                                )
                            }
                        }

                        Spacer(modifier = Modifier.width(4.dp))

                        IconButton(
                            onClick = onRefresh,
                            modifier = Modifier
                                .size(36.dp)
                                .testTag("weather_widget_refresh_button")
                        ) {
                            if (isRefreshing) {
                                CircularProgressIndicator(
                                    color = Color.White,
                                    strokeWidth = 2.dp,
                                    modifier = Modifier.size(16.dp)
                                )
                            } else {
                                Icon(
                                    imageVector = Icons.Default.Refresh,
                                    contentDescription = "Refresh Real-time Weather",
                                    tint = Color.White,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

                // Hero Weather Metric Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Row(verticalAlignment = Alignment.Bottom) {
                            Text(
                                text = "$currentTemp°",
                                style = MaterialTheme.typography.displayLarge.copy(
                                    fontWeight = FontWeight.Light,
                                    fontSize = 62.sp
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

                        Text(
                            text = "Feels like $apparentTemp°C",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.White.copy(alpha = 0.85f)
                        )
                    }

                    Column(horizontalAlignment = Alignment.End) {
                        Surface(
                            shape = CircleShape,
                            color = Color.White.copy(alpha = 0.15f),
                            border = BorderStroke(1.dp, Color.White.copy(alpha = 0.25f)),
                            modifier = Modifier.size(64.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = visual.icon,
                                    contentDescription = visual.label,
                                    tint = visual.tint,
                                    modifier = Modifier.size(36.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = weatherDesc,
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                            color = Color.White,
                            textAlign = TextAlign.End,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        if (weather != null) {
                            Text(
                                text = "H: ${weather.maxTemp.toInt()}° • L: ${weather.minTemp.toInt()}°",
                                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium),
                                color = Color.White.copy(alpha = 0.85f)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Atmospheric Micro-Metrics Row
                if (weather != null) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        WidgetMetricPill(
                            icon = Icons.Default.Air,
                            label = "Wind",
                            value = "${weather.windSpeed.toInt()} km/h",
                            modifier = Modifier.weight(1f)
                        )
                        WidgetMetricPill(
                            icon = Icons.Default.WaterDrop,
                            label = "Humidity",
                            value = "${weather.humidity}%",
                            modifier = Modifier.weight(1f)
                        )
                        WidgetMetricPill(
                            icon = Icons.Default.WbSunny,
                            label = "UV Index",
                            value = "${weather.uvIndex.toInt()}",
                            modifier = Modifier.weight(1f)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // 3-Day Forecast Summary Section
                ThreeDayForecastSummary(
                    forecasts = dailyForecasts.take(3),
                    onViewFullForecast = onViewFullForecast
                )
            }
        }
    }
}

/**
 * 3-Day Forecast Summary Strip
 */
@Composable
fun ThreeDayForecastSummary(
    forecasts: List<DailyForecastEntity>,
    onViewFullForecast: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(20.dp),
        color = Color.Black.copy(alpha = 0.22f),
        border = BorderStroke(0.5.dp, Color.White.copy(alpha = 0.2f)),
        modifier = modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "3-DAY COASTAL FORECAST",
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp,
                        fontSize = 10.sp
                    ),
                    color = Color.White.copy(alpha = 0.9f)
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.clickable(onClick = onViewFullForecast)
                ) {
                    Text(
                        text = "Full 7-Day",
                        style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium),
                        fontSize = 11.sp,
                        color = Color.White.copy(alpha = 0.85f)
                    )
                    Spacer(modifier = Modifier.width(2.dp))
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = "View 7 day forecast",
                        tint = Color.White.copy(alpha = 0.85f),
                        modifier = Modifier.size(12.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            if (forecasts.isEmpty()) {
                // Fallback 3-day sample if database is synchronizing
                val fallbackDays = listOf(
                    Triple("Today", 0, Pair(34, 25)),
                    Triple("Tomorrow", 1, Pair(33, 24)),
                    Triple("Day 3", 61, Pair(31, 23))
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    fallbackDays.forEach { (label, code, temps) ->
                        DayForecastSummaryCard(
                            dayLabel = label,
                            weatherCode = code,
                            maxTemp = temps.first,
                            minTemp = temps.second,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            } else {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    forecasts.forEachIndexed { index, forecast ->
                        val dayLabel = when (index) {
                            0 -> "Today"
                            1 -> "Tomorrow"
                            else -> formatDayOfWeek(forecast.date)
                        }
                        DayForecastSummaryCard(
                            dayLabel = dayLabel,
                            weatherCode = forecast.weatherCode,
                            maxTemp = forecast.maxTemp.toInt(),
                            minTemp = forecast.minTemp.toInt(),
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun DayForecastSummaryCard(
    dayLabel: String,
    weatherCode: Int,
    maxTemp: Int,
    minTemp: Int,
    modifier: Modifier = Modifier
) {
    val visual = WeatherIconSet.getVisualForCode(weatherCode, isDay = true)

    Surface(
        shape = RoundedCornerShape(14.dp),
        color = Color.White.copy(alpha = 0.12f),
        border = BorderStroke(0.5.dp, Color.White.copy(alpha = 0.2f)),
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.padding(vertical = 10.dp, horizontal = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = dayLabel,
                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                fontSize = 11.sp,
                color = Color.White,
                maxLines = 1
            )

            Spacer(modifier = Modifier.height(6.dp))

            Icon(
                imageVector = visual.icon,
                contentDescription = visual.label,
                tint = visual.tint,
                modifier = Modifier.size(24.dp)
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "$maxTemp° / $minTemp°",
                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold),
                fontSize = 11.sp,
                color = Color.White
            )

            Text(
                text = visual.label.substringBefore("/").trim(),
                style = MaterialTheme.typography.bodySmall,
                fontSize = 9.sp,
                color = Color.White.copy(alpha = 0.8f),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
fun WidgetMetricPill(
    icon: ImageVector,
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = Color.White.copy(alpha = 0.15f),
        modifier = modifier
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = Color.White.copy(alpha = 0.9f),
                modifier = Modifier.size(13.dp)
            )
            Column {
                Text(
                    text = label,
                    style = MaterialTheme.typography.bodySmall,
                    fontSize = 9.sp,
                    color = Color.White.copy(alpha = 0.75f)
                )
                Text(
                    text = value,
                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                    fontSize = 10.5.sp,
                    color = Color.White
                )
            }
        }
    }
}

private fun formatDayOfWeek(dateStr: String): String {
    return try {
        val parser = SimpleDateFormat("yyyy-MM-dd", Locale.US)
        val date = parser.parse(dateStr) ?: Date()
        val formatter = SimpleDateFormat("EEE", Locale.US)
        formatter.format(date)
    } catch (_: Exception) {
        dateStr.takeLast(2)
    }
}
