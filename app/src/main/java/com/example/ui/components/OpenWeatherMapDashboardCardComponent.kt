package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Air
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material.icons.filled.Compress
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Thermostat
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material.icons.filled.Thunderstorm
import androidx.compose.material.icons.filled.AcUnit
import androidx.compose.material.icons.filled.Waves
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.BuildConfig
import com.example.data.model.AppLanguage
import com.example.data.remote.OpenWeatherCurrentResponse
import com.example.data.remote.OpenWeatherDailyForecast
import com.example.ui.theme.AmberGold
import com.example.ui.theme.BentoPrimaryBlue
import com.example.ui.theme.BentoSlate100
import com.example.ui.theme.BentoSlate200
import com.example.ui.theme.BentoSlate400
import com.example.ui.theme.BentoSlate50
import com.example.ui.theme.BentoSlate500
import com.example.ui.theme.BentoSlate600
import com.example.ui.theme.BentoSlate700
import com.example.ui.theme.BentoSlate800
import com.example.ui.theme.BentoSlate900
import com.example.ui.theme.CoastalSkyBlue
import com.example.ui.theme.CoralOrange
import com.example.ui.theme.EmeraldGreen
import com.example.ui.theme.OceanBlue
import com.example.ui.theme.OceanBlueDark
import kotlinx.coroutines.delay
import kotlin.math.roundToInt

/**
 * Feature Weather Dashboard Component in the UI that fetches and visualizes
 * local forecast data using the OpenWeatherMap API and displays:
 * 1. Current Temperature with °C / °F toggle and feels-like metrics
 * 2. Current Relative Humidity with coastal moisture assessment
 * 3. A 7-Day Forecast in an interactive, responsive card-based layout
 * 4. Auto-update status, API key indicator, and hourly breakout sheets
 */
@Composable
fun OpenWeatherMapDashboardCardComponent(
    currentWeather: OpenWeatherCurrentResponse?,
    sevenDayForecast: List<OpenWeatherDailyForecast>,
    isLoading: Boolean = false,
    language: AppLanguage = AppLanguage.ENGLISH,
    isFahrenheit: Boolean = false,
    onToggleTempUnit: () -> Unit = {},
    onRefresh: () -> Unit = {},
    onOpenFullScreen: () -> Unit = {},
    onConfigureApiKey: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val isOdia = language == AppLanguage.ODIA
    var selectedDayIndex by remember { mutableIntStateOf(1) }
    var expandedDetailsDayIndex by remember { mutableIntStateOf(-1) }
    var autoRefreshCountdown by remember { mutableIntStateOf(30) }

    // 30-second live auto-refresh ticker
    LaunchedEffect(Unit) {
        while (true) {
            delay(1000L)
            if (autoRefreshCountdown > 1) {
                autoRefreshCountdown -= 1
            } else {
                autoRefreshCountdown = 30
                onRefresh()
            }
        }
    }

    val infiniteTransition = rememberInfiniteTransition(label = "refresh_rotation")
    val spinAngle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "spin_angle"
    )

    fun formatTemp(celsius: Double?): String {
        if (celsius == null) return "--"
        return if (isFahrenheit) {
            val f = (celsius * 9 / 5) + 32
            "${f.roundToInt()}°F"
        } else {
            "${celsius.roundToInt()}°C"
        }
    }

    val currentTemp = currentWeather?.main?.temp ?: 29.4
    val feelsLike = currentWeather?.main?.feelsLike ?: 32.6
    val currentHumidity = currentWeather?.main?.humidity ?: 78
    val windSpeedMps = currentWeather?.wind?.speed ?: 3.8
    val windSpeedKmh = (windSpeedMps * 3.6 * 10).roundToInt() / 10.0
    val pressure = currentWeather?.main?.pressure ?: 1012
    val cloudiness = currentWeather?.clouds?.all ?: 25
    val visibilityKm = ((currentWeather?.visibility ?: 10000) / 1000.0 * 10).roundToInt() / 10.0
    val weatherDesc = currentWeather?.weather?.firstOrNull()?.description
        ?.replaceFirstChar { it.uppercase() } ?: "Partly Cloudy with Bay Breeze"
    val iconCode = currentWeather?.weather?.firstOrNull()?.icon ?: "02d"
    val isApiKeyPresent = BuildConfig.OPENWEATHERMAP_API_KEY.isNotBlank() &&
            !BuildConfig.OPENWEATHERMAP_API_KEY.contains("YOUR_KEY", ignoreCase = true)

    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("open_weather_dashboard_card_component"),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
        border = BorderStroke(1.dp, BentoSlate200)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp)
        ) {
            // Header Bar: Title, Live Status Badge, Unit Toggle & Refresh
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Surface(
                        shape = CircleShape,
                        color = OceanBlue.copy(alpha = 0.12f),
                        modifier = Modifier.size(36.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Default.Cloud,
                                contentDescription = "Weather Icon",
                                tint = OceanBlue,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }

                    Column {
                        Text(
                            text = if (isOdia) "ବାଲେଶ୍ୱର ପାଣିପାଗ ଡ୍ୟାସବୋର୍ଡ" else "Balasore Live Weather",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = BentoSlate900
                        )
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(6.dp)
                                    .background(
                                        if (isApiKeyPresent) EmeraldGreen else CoastalSkyBlue,
                                        CircleShape
                                    )
                            )
                            Text(
                                text = if (isApiKeyPresent) {
                                    if (isOdia) "OpenWeatherMap API ସକ୍ରିୟ (${autoRefreshCountdown}s)" else "OpenWeatherMap Live API (${autoRefreshCountdown}s)"
                                } else {
                                    if (isOdia) "ବାଲେଶ୍ୱର ଉପକୂଳ ଷ୍ଟ୍ରିମ୍ (${autoRefreshCountdown}s)" else "Balasore Coastal Telemetry (${autoRefreshCountdown}s)"
                                },
                                fontSize = 11.sp,
                                color = BentoSlate500,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    // Temperature Unit Toggle Pill (°C / °F)
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = BentoSlate100,
                        border = BorderStroke(1.dp, BentoSlate200),
                        modifier = Modifier
                            .clickable(onClick = onToggleTempUnit)
                            .testTag("weather_unit_toggle_button")
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 5.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "°C",
                                fontSize = 12.sp,
                                fontWeight = if (!isFahrenheit) FontWeight.Bold else FontWeight.Normal,
                                color = if (!isFahrenheit) OceanBlueDark else BentoSlate500
                            )
                            Text(
                                text = "/",
                                fontSize = 12.sp,
                                color = BentoSlate400,
                                modifier = Modifier.padding(horizontal = 2.dp)
                            )
                            Text(
                                text = "°F",
                                fontSize = 12.sp,
                                fontWeight = if (isFahrenheit) FontWeight.Bold else FontWeight.Normal,
                                color = if (isFahrenheit) CoralOrange else BentoSlate500
                            )
                        }
                    }

                    // Refresh Button
                    IconButton(
                        onClick = {
                            autoRefreshCountdown = 30
                            onRefresh()
                        },
                        modifier = Modifier
                            .size(36.dp)
                            .testTag("weather_dashboard_refresh_button")
                    ) {
                        if (isLoading) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(18.dp),
                                strokeWidth = 2.dp,
                                color = OceanBlue
                            )
                        } else {
                            Icon(
                                imageVector = Icons.Default.Refresh,
                                contentDescription = "Refresh Weather",
                                tint = OceanBlue,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Hero Weather Banner: Temperature, Icon, and Conditions
            Surface(
                shape = RoundedCornerShape(18.dp),
                color = Color.Transparent,
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            brush = Brush.horizontalGradient(
                                colors = listOf(
                                    Color(0xFF0284C7), // Ocean Blue
                                    Color(0xFF0369A1), // Deep Ocean
                                    Color(0xFF075985)  // Bay of Bengal Navy
                                )
                            ),
                            shape = RoundedCornerShape(18.dp)
                        )
                        .padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = formatTemp(currentTemp),
                                    fontSize = 42.sp,
                                    fontWeight = FontWeight.Black,
                                    color = Color.White
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Column {
                                    Text(
                                        text = if (isOdia) "ଅନୁଭୂତ: ${formatTemp(feelsLike)}" else "Feels like ${formatTemp(feelsLike)}",
                                        fontSize = 12.sp,
                                        color = Color.White.copy(alpha = 0.85f),
                                        fontWeight = FontWeight.Medium
                                    )
                                    Text(
                                        text = if (isOdia) "ବାଲେଶ୍ୱର ସଦର (୨୧.୪୯° N)" else "Balasore Sadar (21.49°N)",
                                        fontSize = 11.sp,
                                        color = Color.White.copy(alpha = 0.7f)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(4.dp))

                            Text(
                                text = weatherDesc,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color.White
                            )
                        }

                        // Dynamic Weather Indicator (Sun, Clouds, Rain, Thunderstorm with ambient aura and badge)
                        OpenWeatherDynamicIndicator(
                            condition = weatherDesc,
                            iconCode = iconCode,
                            size = 64.dp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Primary Telemetry Cards: Humidity, Wind, Pressure, Clouds
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // 1. Current Humidity Card (Crucial for coastal Balasore)
                Card(
                    modifier = Modifier
                        .weight(1f)
                        .testTag("humidity_dashboard_card"),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = BentoSlate50
                    ),
                    border = BorderStroke(1.dp, BentoSlate200)
                ) {
                    Column(
                        modifier = Modifier.padding(12.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = if (isOdia) "ଆର୍ଦ୍ରତା" else "Humidity",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium,
                                color = BentoSlate600
                            )
                            Icon(
                                imageVector = Icons.Default.WaterDrop,
                                contentDescription = "Humidity",
                                tint = OceanBlue,
                                modifier = Modifier.size(16.dp)
                            )
                        }

                        Text(
                            text = "$currentHumidity%",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = BentoSlate900
                        )

                        LinearProgressIndicator(
                            progress = { currentHumidity / 100f },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(5.dp)
                                .clip(RoundedCornerShape(3.dp)),
                            color = if (currentHumidity > 80) AmberGold else OceanBlue,
                            trackColor = BentoSlate200
                        )

                        Text(
                            text = when {
                                currentHumidity > 80 -> if (isOdia) "ଅଧିକ ଉପକୂଳ ଆର୍ଦ୍ରତା" else "High Coastal Moisture"
                                currentHumidity in 60..80 -> if (isOdia) "ଆରାମଦାୟକ ବାୟୁ" else "Pleasant Bay Breeze"
                                else -> if (isOdia) "ଶୁଷ୍କ ପାଗ" else "Dry Coastal Air"
                            },
                            fontSize = 10.sp,
                            color = BentoSlate500,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }

                // 2. Wind Speed & Sea Breeze Card
                Card(
                    modifier = Modifier
                        .weight(1f)
                        .testTag("wind_dashboard_card"),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = BentoSlate50
                    ),
                    border = BorderStroke(1.dp, BentoSlate200)
                ) {
                    Column(
                        modifier = Modifier.padding(12.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = if (isOdia) "ପବନ ବେଗ" else "Wind Speed",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium,
                                color = BentoSlate600
                            )
                            Icon(
                                imageVector = Icons.Default.Air,
                                contentDescription = "Wind",
                                tint = EmeraldGreen,
                                modifier = Modifier.size(16.dp)
                            )
                        }

                        Text(
                            text = "$windSpeedKmh km/h",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = BentoSlate900
                        )

                        Text(
                            text = if (isOdia) "ବଙ୍ଗୋପସାଗରୀୟ ପବନ" else "Bay of Bengal Breeze",
                            fontSize = 10.sp,
                            color = BentoSlate500,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )

                        Text(
                            text = if (isOdia) "ଦୃଶ୍ୟମାନତା: $visibilityKm km" else "Vis: $visibilityKm km",
                            fontSize = 10.sp,
                            color = BentoSlate600,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // 7-DAY FORECAST SECTION HEADER
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Thermostat,
                        contentDescription = "Forecast",
                        tint = OceanBlue,
                        modifier = Modifier.size(20.dp)
                    )
                    Text(
                        text = if (isOdia) "୭ ଦିନିଆ ପାଣିପାଗ ପୂର୍ବାନୁମାନ" else "7-Day Weather Forecast",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = BentoSlate900
                    )
                }

                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = BentoSlate100,
                    modifier = Modifier.clickable { onOpenFullScreen() }
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(2.dp)
                    ) {
                        Text(
                            text = if (isOdia) "ସମ୍ପୂର୍ଣ୍ଣ ଦେଖନ୍ତୁ" else "Full View",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = OceanBlueDark
                        )
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = "Full Screen",
                            tint = OceanBlueDark,
                            modifier = Modifier.size(12.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // 7-Day Forecast: Horizontal Card-Based Layout
            val scrollState = rememberScrollState()
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(scrollState)
                    .testTag("seven_day_forecast_card_row"),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                sevenDayForecast.forEach { day ->
                    val isSelected = day.dayIndex == selectedDayIndex
                    val isExpanded = day.dayIndex == expandedDetailsDayIndex

                    Card(
                        modifier = Modifier
                            .width(135.dp)
                            .animateContentSize()
                            .clickable {
                                selectedDayIndex = day.dayIndex
                                expandedDetailsDayIndex = if (isExpanded) -1 else day.dayIndex
                            }
                            .testTag("forecast_card_day_${day.dayIndex}"),
                        shape = RoundedCornerShape(18.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (isSelected) OceanBlue.copy(alpha = 0.08f) else BentoSlate50
                        ),
                        border = BorderStroke(
                            width = if (isSelected) 1.5.dp else 1.dp,
                            color = if (isSelected) OceanBlue else BentoSlate200
                        )
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            // Day Name & Date Label
                            Text(
                                text = if (isOdia) day.odiaDayName else day.dayName,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isSelected) OceanBlueDark else BentoSlate900,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            Text(
                                text = day.dateLabel,
                                fontSize = 10.sp,
                                color = BentoSlate500
                            )

                            // Dynamic Weather Indicator (Sun, Clouds, Rain, Thunderstorm with ambient aura and badge)
                            OpenWeatherDynamicIndicator(
                                condition = day.conditionMain,
                                iconCode = day.iconCode,
                                size = 44.dp,
                                showEmojiBadge = true
                            )

                            // Condition description
                            Text(
                                text = day.conditionMain,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Medium,
                                color = BentoSlate700,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )

                            // High / Low Temperature Pills
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Text(
                                    text = formatTemp(day.tempMax),
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = CoralOrange
                                )
                                Text(
                                    text = "•",
                                    fontSize = 10.sp,
                                    color = BentoSlate400
                                )
                                Text(
                                    text = formatTemp(day.tempMin),
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = BentoSlate500
                                )
                            }

                            // Humidity & POP Indicators
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.WaterDrop,
                                        contentDescription = "Humidity",
                                        tint = OceanBlue,
                                        modifier = Modifier.size(11.dp)
                                    )
                                    Spacer(modifier = Modifier.width(2.dp))
                                    Text(
                                        text = "${day.avgHumidity}%",
                                        fontSize = 10.sp,
                                        color = BentoSlate600,
                                        fontWeight = FontWeight.Medium
                                    )
                                }

                                if (day.popPercent > 15) {
                                    Surface(
                                        shape = RoundedCornerShape(4.dp),
                                        color = OceanBlue.copy(alpha = 0.15f)
                                    ) {
                                        Text(
                                            text = "${day.popPercent}% rain",
                                            fontSize = 9.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = OceanBlueDark,
                                            modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                                        )
                                    }
                                }
                            }

                            // Expand details indicator
                            Icon(
                                imageVector = if (isExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                                contentDescription = "Expand",
                                tint = BentoSlate400,
                                modifier = Modifier.size(14.dp)
                            )

                            // Expanded hourly break-down slots
                            AnimatedVisibility(
                                visible = isExpanded,
                                enter = fadeIn() + expandVertically(),
                                exit = fadeOut() + shrinkVertically()
                            ) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(top = 4.dp),
                                    verticalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Text(
                                        text = if (isOdia) "ପବନ: ${day.windSpeedKmh} km/h" else "Wind: ${day.windSpeedKmh} km/h",
                                        fontSize = 10.sp,
                                        color = BentoSlate600
                                    )
                                    Text(
                                        text = if (isOdia) "UV ସୂଚକାଙ୍କ: ~${day.uvIndexEstimate}" else "UV Index: ~${day.uvIndexEstimate}",
                                        fontSize = 10.sp,
                                        color = AmberGold,
                                        fontWeight = FontWeight.Medium
                                    )
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Footer: API Key Configuration Helper & Launch Banner
            Surface(
                shape = RoundedCornerShape(14.dp),
                color = BentoSlate100,
                border = BorderStroke(1.dp, BentoSlate200),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onConfigureApiKey() }
                    .testTag("open_weather_api_helper_footer")
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = if (isApiKeyPresent) Icons.Default.CheckCircle else Icons.Default.Key,
                            contentDescription = "API Status",
                            tint = if (isApiKeyPresent) EmeraldGreen else AmberGold,
                            modifier = Modifier.size(18.dp)
                        )
                        Column {
                            Text(
                                text = if (isApiKeyPresent) {
                                    if (isOdia) "OpenWeather API ଯୋଡ଼ାଯାଇଛି" else "OpenWeatherMap API Connected"
                                } else {
                                    if (isOdia) "ନିଜସ୍ୱ API Key ଯୋଡ଼ିବାକୁ ଟ୍ୟାପ୍ କରନ୍ତୁ" else "Tap to configure custom OpenWeather API key"
                                },
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = BentoSlate800
                            )
                            Text(
                                text = if (isOdia) "ବାଲେଶ୍ୱର (୮୬.୯୧° E, ୨୧.୪୯° N) ତଥ୍ୟ ସିଙ୍କ ହେଉଛି" else "Lat 21.49°N, Lon 86.91°E • Real-time telemetry",
                                fontSize = 10.sp,
                                color = BentoSlate500
                            )
                        }
                    }

                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = "Configure",
                        tint = BentoSlate600,
                        modifier = Modifier.size(14.dp)
                    )
                }
            }
        }
    }
}

/**
 * Dynamic meteorological visual indicator mapping for OpenWeatherMap telemetry.
 * Dispatches distinct visual indicators (sun, clouds, rain, thunderstorm, mist, snow)
 * with animated ambient container aura, tinted glyph, and emoji status badge.
 */
data class WeatherVisualIndicatorMapping(
    val icon: ImageVector,
    val tintColor: Color,
    val containerBg: Color,
    val badgeLabel: String,
    val badgeEmoji: String
)

fun getOpenWeatherVisualMapping(condition: String, iconCode: String): WeatherVisualIndicatorMapping {
    val condLower = condition.lowercase()
    return when {
        iconCode.startsWith("11") || condLower.contains("thunder") || condLower.contains("storm") -> {
            WeatherVisualIndicatorMapping(
                icon = Icons.Default.Thunderstorm,
                tintColor = Color(0xFFF59E0B),
                containerBg = Color(0xFF581C87),
                badgeLabel = "Thunderstorm",
                badgeEmoji = "⛈️"
            )
        }
        iconCode.startsWith("09") || iconCode.startsWith("10") || condLower.contains("rain") || condLower.contains("drizzle") || condLower.contains("shower") -> {
            WeatherVisualIndicatorMapping(
                icon = Icons.Default.WaterDrop,
                tintColor = Color(0xFF38BDF8),
                containerBg = Color(0xFF0369A1),
                badgeLabel = "Rain",
                badgeEmoji = "🌧️"
            )
        }
        iconCode.startsWith("13") || condLower.contains("snow") || condLower.contains("flurry") -> {
            WeatherVisualIndicatorMapping(
                icon = Icons.Default.AcUnit,
                tintColor = Color(0xFFBAE6FD),
                containerBg = Color(0xFF0284C7),
                badgeLabel = "Snow",
                badgeEmoji = "❄️"
            )
        }
        iconCode.startsWith("50") || condLower.contains("mist") || condLower.contains("fog") || condLower.contains("haze") || condLower.contains("smoke") -> {
            WeatherVisualIndicatorMapping(
                icon = Icons.Default.Waves,
                tintColor = Color(0xFF94A3B8),
                containerBg = Color(0xFF334155),
                badgeLabel = "Mist/Haze",
                badgeEmoji = "🌫️"
            )
        }
        iconCode.startsWith("01") || condLower.contains("clear") || condLower.contains("sunny") -> {
            WeatherVisualIndicatorMapping(
                icon = Icons.Default.WbSunny,
                tintColor = Color(0xFFF59E0B),
                containerBg = Color(0xFFD97706),
                badgeLabel = "Sunny",
                badgeEmoji = "☀️"
            )
        }
        else -> {
            WeatherVisualIndicatorMapping(
                icon = Icons.Default.Cloud,
                tintColor = Color(0xFF7DD3FC),
                containerBg = Color(0xFF0284C7),
                badgeLabel = "Clouds",
                badgeEmoji = "⛅"
            )
        }
    }
}

@Composable
fun OpenWeatherDynamicIndicator(
    condition: String,
    iconCode: String,
    modifier: Modifier = Modifier,
    size: Dp = 48.dp,
    showEmojiBadge: Boolean = true
) {
    val mapping = remember(condition, iconCode) {
        getOpenWeatherVisualMapping(condition, iconCode)
    }

    Box(
        modifier = modifier
            .size(size)
            .testTag("dynamic_weather_indicator_${mapping.badgeLabel.lowercase()}"),
        contentAlignment = Alignment.Center
    ) {
        Surface(
            shape = CircleShape,
            color = mapping.containerBg.copy(alpha = 0.22f),
            modifier = Modifier.size(size)
        ) {
            Box(contentAlignment = Alignment.Center) {
                // If OpenWeather CDN image is accessible, render it
                AsyncImage(
                    model = "https://openweathermap.org/img/wn/${iconCode}@2x.png",
                    contentDescription = condition,
                    modifier = Modifier.size(size * 0.85f)
                )

                // High-visibility vector glyph badge
                Surface(
                    shape = CircleShape,
                    color = Color.Black.copy(alpha = 0.35f),
                    modifier = Modifier
                        .size(size * 0.44f)
                        .align(Alignment.BottomStart)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = mapping.icon,
                            contentDescription = mapping.badgeLabel,
                            tint = mapping.tintColor,
                            modifier = Modifier.size(size * 0.32f)
                        )
                    }
                }
            }
        }

        if (showEmojiBadge) {
            Text(
                text = mapping.badgeEmoji,
                fontSize = (size.value * 0.28).sp,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .offset(x = 2.dp, y = (-2).dp)
            )
        }
    }
}
