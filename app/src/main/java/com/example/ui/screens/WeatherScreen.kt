package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Air
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Navigation
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Thermostat
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.Water
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.ChandipurTideEntity
import com.example.data.model.AppLanguage
import com.example.data.model.DrdoAdvisory
import com.example.data.model.TidalClockData
import com.example.data.model.WeatherInfo
import com.example.ui.components.AdMobBannerCard
import com.example.ui.theme.AmberGold
import com.example.ui.theme.BentoSlate100
import com.example.ui.theme.BentoSlate200
import com.example.ui.theme.BentoSlate500
import com.example.ui.theme.BentoSlate600
import com.example.ui.theme.BentoSlate700
import com.example.ui.theme.BentoSlate800
import com.example.ui.theme.BentoSlate900
import com.example.ui.theme.EmeraldGreen
import com.example.ui.theme.OceanBlue
import com.example.ui.theme.OceanBlueDark
import com.example.ui.viewmodel.UniqueFeatureSheetType
import kotlin.math.sin

@Composable
fun WeatherScreen(
    weather: WeatherInfo,
    tidalClock: TidalClockData,
    drdoAdvisories: List<DrdoAdvisory>,
    language: AppLanguage,
    onOpenFeatureSheet: (UniqueFeatureSheetType) -> Unit = {},
    lastKnownTide: ChandipurTideEntity? = null,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("weather_screen_list"),
        contentPadding = PaddingValues(bottom = 100.dp)
    ) {
        // 1. Hero Weather Card
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .background(
                        Brush.verticalGradient(
                            listOf(Color(0xFF0284C7), Color(0xFF0369A1))
                        )
                    )
                    .padding(24.dp)
            ) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Balasore City & Coast",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            )
                            Text(
                                text = if (language == AppLanguage.ODIA) "ବାଲେଶ୍ୱର ପାଣିପାଗ ସୂଚନା" else "Bay of Bengal Coastal Zone",
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    color = Color.White.copy(alpha = 0.8f)
                                )
                            )
                        }

                        Icon(
                            imageVector = Icons.Default.Cloud,
                            contentDescription = "Weather",
                            tint = Color.White,
                            modifier = Modifier.size(44.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(18.dp))

                    Row(verticalAlignment = Alignment.Bottom) {
                        Text(
                            text = "${weather.tempCelsius}°",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontSize = 54.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = Color.White,
                                lineHeight = 54.sp
                            )
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.padding(bottom = 6.dp)) {
                            Text(
                                text = weather.condition,
                                style = MaterialTheme.typography.bodyLarge.copy(
                                    fontWeight = FontWeight.SemiBold,
                                    color = Color.White
                                )
                            )
                            Text(
                                text = "H: ${weather.highLow}",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = Color.White.copy(alpha = 0.8f)
                                )
                            )
                        }
                    }
                }
            }
        }

        // 24-Hour Coastal & Marine Horizon Row
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp)
                    .testTag("marine_horizon_card"),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = BorderStroke(1.dp, BentoSlate100)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = if (language == AppLanguage.ODIA) "୨୪-ଘଣ୍ଟାର ଉପକୂଳ କ୍ଷିତିଜ ପୂର୍ବାନୁମାନ" else "24-Hour Coastal & Marine Horizon",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = BentoSlate900
                            )
                        )
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = Color(0xFFE0F2FE)
                        ) {
                            Text(
                                text = "BAY OF BENGAL",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.ExtraBold,
                                    fontSize = 9.sp,
                                    color = OceanBlue
                                ),
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    val hourlyForecasts = listOf(
                        Triple("Now", "31°C", "0.4m (Vanishing)"),
                        Triple("14:00", "32°C", "0.6m (Receding)"),
                        Triple("16:00", "30°C", "1.4m (Incoming)"),
                        Triple("18:00", "28°C", "2.8m (High Tide)"),
                        Triple("20:00", "27°C", "3.2m (Peak Surge)"),
                        Triple("22:00", "26°C", "2.2m (Slack)"),
                        Triple("00:00", "25°C", "1.1m (Ebb)"),
                        Triple("02:00", "24°C", "0.5m (Low Tide)")
                    )

                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        contentPadding = PaddingValues(vertical = 4.dp)
                    ) {
                        items(hourlyForecasts) { (time, temp, tide) ->
                            Surface(
                                shape = RoundedCornerShape(14.dp),
                                color = if (time == "Now") Color(0xFF0284C7) else Color(0xFFF8FAFC),
                                border = BorderStroke(1.dp, if (time == "Now") Color(0xFF0284C7) else Color(0xFFE2E8F0)),
                                modifier = Modifier.width(96.dp)
                            ) {
                                Column(
                                    modifier = Modifier.padding(10.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(
                                        text = time,
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 11.sp,
                                            color = if (time == "Now") Color.White else BentoSlate500
                                        )
                                    )
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Icon(
                                        imageVector = if (time in listOf("18:00", "20:00", "22:00", "00:00", "02:00")) Icons.Default.Water else Icons.Default.WbSunny,
                                        contentDescription = null,
                                        tint = if (time == "Now") Color(0xFFBAE6FD) else if (time in listOf("18:00", "20:00", "22:00", "00:00", "02:00")) OceanBlue else AmberGold,
                                        modifier = Modifier.size(20.dp)
                                    )
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Text(
                                        text = temp,
                                        style = MaterialTheme.typography.bodySmall.copy(
                                            fontWeight = FontWeight.ExtraBold,
                                            color = if (time == "Now") Color.White else BentoSlate900
                                        )
                                    )
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(
                                        text = tide,
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            fontSize = 9.sp,
                                            fontWeight = FontWeight.Medium,
                                            color = if (time == "Now") Color(0xFFE0F2FE) else BentoSlate600
                                        ),
                                        maxLines = 1
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // 2. FEATURE 1: Chandipur Live Tidal Clock & Safe Walk Window
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp)
                    .testTag("chandipur_tidal_clock_card"),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = BorderStroke(1.dp, Color(0xFFBAE6FD))
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    // Header Row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFFE0F2FE)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Water,
                                    contentDescription = "Tide",
                                    tint = OceanBlue,
                                    modifier = Modifier.size(22.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = if (language == AppLanguage.ODIA) "ଚାନ୍ଦିପୁର ଜୀବନ୍ତ ଟାଇଡାଲ୍ କ୍ଲକ୍" else "Chandipur Live Tidal Clock",
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = BentoSlate900
                                    )
                                )
                                Text(
                                    text = if (language == AppLanguage.ODIA) tidalClock.odiaPhase else tidalClock.currentPhase,
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        color = OceanBlueDark,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                )
                            }
                        }

                        // Receded Distance Badge
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color(0xFFF0FDF4))
                                .padding(horizontal = 10.dp, vertical = 5.dp)
                        ) {
                            Text(
                                text = "-${tidalClock.distanceRecededKm} KM",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.ExtraBold,
                                    color = Color(0xFF15803D)
                                )
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Safe Walk Countdown Banner
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(14.dp))
                            .background(Color(0xFFEFF6FF))
                            .padding(horizontal = 14.dp, vertical = 10.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.AccessTime,
                                    contentDescription = "Safe Walk Window",
                                    tint = OceanBlue,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = if (language == AppLanguage.ODIA) "ସମୁଦ୍ରରେ ଚାଲିବା ସମୟ: ${tidalClock.safeWalkMinutesRemaining} ମିନିଟ୍ ବାକି"
                                    else "Safe Walk Window: ${tidalClock.safeWalkMinutesRemaining} mins left",
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = BentoSlate800
                                    )
                                )
                            }
                            Text(
                                text = "Safe Zone",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = EmeraldGreen,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Dynamic Animated Chandipur Tide Wave Canvas
                    DynamicChandipurTideCanvas(
                        safeWalkMinutesRemaining = tidalClock.safeWalkMinutesRemaining,
                        recededDistanceKm = 4.2,
                        modifier = Modifier.testTag("dynamic_chandipur_tide_canvas")
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    // Tide Times Summary Row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(
                                text = "Low Tide (Vanishing)",
                                style = MaterialTheme.typography.labelSmall.copy(color = BentoSlate500)
                            )
                            Text(
                                text = tidalClock.lowTideTime,
                                style = MaterialTheme.typography.bodySmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = OceanBlue
                                )
                            )
                        }
                        Column(horizontalAlignment = Alignment.End) {
                            Text(
                                text = "Next High Tide",
                                style = MaterialTheme.typography.labelSmall.copy(color = BentoSlate500)
                            )
                            Text(
                                text = tidalClock.highTideTime,
                                style = MaterialTheme.typography.bodySmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = BentoSlate700
                                )
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Biodiversity Sighting Badges
                    Text(
                        text = if (language == AppLanguage.ODIA) "ସମୁଦ୍ର ଶଯ୍ୟାରେ ଦୃଶ୍ୟମାନ ଜୀବବିବିଧତା" else "Exposed Seabed Biodiversity Sightings",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = BentoSlate700
                        )
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    tidalClock.biodiversitySightings.forEach { sighting ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 3.dp),
                            verticalAlignment = Alignment.Top
                        ) {
                            Text("🦀", fontSize = 12.sp)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = sighting,
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = BentoSlate600,
                                    lineHeight = 16.sp
                                )
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Button(
                        onClick = { onOpenFeatureSheet(UniqueFeatureSheetType.HORSESHOE_CRAB) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("open_horseshoe_crab_btn"),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0284C7)),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text("🦀", fontSize = 14.sp)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Living Fossil Bio-Radar & Seabed Guide", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        // Room Database: Last Known Tide Forecast (Offline Mode)
        if (lastKnownTide != null) {
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 6.dp)
                        .testTag("offline_last_known_tide_card"),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFF0F9FF)),
                    border = BorderStroke(1.dp, Color(0xFFBAE6FD))
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Surface(
                                    shape = RoundedCornerShape(6.dp),
                                    color = Color(0xFF0284C7)
                                ) {
                                    Text(
                                        text = "ROOM DB • OFFLINE",
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.ExtraBold,
                                        color = Color.White,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = if (language == AppLanguage.ODIA) "ସଂରକ୍ଷିତ ଜୁଆର ପୂର୍ବାନୁମାନ" else "Last Known Tide Forecast",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = BentoSlate900
                                )
                            }
                            Text(
                                text = "Offline Cached",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color(0xFF0369A1)
                            )
                        }
                        Spacer(modifier = Modifier.height(10.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text("Low Tide (Max Recede)", fontSize = 10.5.sp, color = BentoSlate600)
                                Text("${lastKnownTide.lowTideTime} (${lastKnownTide.recededDistanceKm} km)", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0369A1))
                            }
                            Column(horizontalAlignment = Alignment.End) {
                                Text("Next High Tide", fontSize = 10.5.sp, color = BentoSlate600)
                                Text(lastKnownTide.nextHighTideTime, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0369A1))
                            }
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = lastKnownTide.tidalForecastSummary,
                            fontSize = 11.5.sp,
                            color = BentoSlate700,
                            lineHeight = 16.sp
                        )
                    }
                }
            }
        }

        // 3. FEATURE 2: DRDO ITR Launch Alerts & Coastal Fishermen Advisories
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp)
                    .testTag("drdo_coastal_advisory_card"),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = BorderStroke(1.dp, BentoSlate100)
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
                                    .size(40.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFFFEF3C7)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Security,
                                    contentDescription = "DRDO Advisory",
                                    tint = AmberGold,
                                    modifier = Modifier.size(22.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = if (language == AppLanguage.ODIA) "ଆଇଟିଆର ଚାନ୍ଦିପୁର କ୍ଷେପଣାସ୍ତ୍ର ବୁଲେଟିନ" else "DRDO ITR Coastal Advisories",
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = BentoSlate900
                                    )
                                )
                                Text(
                                    text = "Defense Testing & Fishermen Clearance",
                                    style = MaterialTheme.typography.bodySmall.copy(color = BentoSlate500)
                                )
                            }
                        }

                        // Cleared Status
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color(0xFFDCFCE7))
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = "ACTIVE",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.ExtraBold,
                                    color = Color(0xFF166534),
                                    fontSize = 10.sp
                                )
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    drdoAdvisories.forEach { advisory ->
                        DrdoAdvisoryItem(advisory = advisory, language = language)
                        Spacer(modifier = Modifier.height(8.dp))
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    Button(
                        onClick = { onOpenFeatureSheet(UniqueFeatureSheetType.DEFENSE_TRAIL) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("open_defense_trail_btn"),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0F172A)),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text("🚀", fontSize = 14.sp)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Kalam Defense Trail & Maritime Radar", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        // 4. Coastal Sea Conditions & Metrics
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = BorderStroke(1.dp, BentoSlate100)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = if (language == AppLanguage.ODIA) "ସମୁଦ୍ର କୂଳ ନିରାପତ୍ତା ସୂଚନା" else "Coastal Sea Conditions",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = BentoSlate900
                            )
                        )
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(Color(0xFFDCFCE7))
                                .padding(horizontal = 8.dp, vertical = 3.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = "Safe",
                                tint = EmeraldGreen,
                                modifier = Modifier.size(12.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "NORMAL",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF166534)
                                )
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = weather.seaCondition,
                        style = MaterialTheme.typography.bodyMedium.copy(color = BentoSlate700)
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        WeatherMetricItem(
                            icon = Icons.Default.Air,
                            label = "Wind",
                            value = weather.windSpeedKmh
                        )
                        WeatherMetricItem(
                            icon = Icons.Default.Water,
                            label = "Humidity",
                            value = weather.humidity
                        )
                        WeatherMetricItem(
                            icon = Icons.Default.WbSunny,
                            label = "UV Index",
                            value = "Moderate (5)"
                        )
                    }
                }
            }
        }

        // Coastal Cyclone Resilience & Shelter Compass Card
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp)
                    .testTag("weather_cyclone_shelter_card"),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = BorderStroke(1.dp, Color(0xFFBBF7D0))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(38.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFFF0FDF4)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text("🌀", fontSize = 20.sp)
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = "Coastal Resilience & Cyclone Shelters",
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = BentoSlate900,
                                        fontSize = 15.sp
                                    )
                                )
                                Text(
                                    text = "Offline GPS Shelters & Disaster Kit Checklist",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        color = Color(0xFF16A34A),
                                        fontWeight = FontWeight.Bold
                                    )
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "Access nearest multi-purpose elevated cyclone shelters with independent solar backup, verified capacity, and offline survival pack essentials.",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = BentoSlate700,
                            lineHeight = 16.sp
                        )
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Button(
                        onClick = { onOpenFeatureSheet(UniqueFeatureSheetType.CYCLONE_RESILIENCE) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("open_cyclone_resilience_btn"),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF16A34A)),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Icon(imageVector = Icons.Default.Shield, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Open Shelter Finder & Disaster Kit", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        // AdMob Banner Placement in Weather
        item {
            AdMobBannerCard(
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}

@Composable
fun DrdoAdvisoryItem(
    advisory: DrdoAdvisory,
    language: AppLanguage
) {
    var expanded by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0xFFF8FAFC))
            .border(BorderStroke(1.dp, BentoSlate100), RoundedCornerShape(12.dp))
            .clickable { expanded = !expanded }
            .padding(12.dp)
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = if (language == AppLanguage.ODIA) advisory.odiaTitle else advisory.title,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = BentoSlate900
                        )
                    )
                    Text(
                        text = advisory.dateOrTime,
                        style = MaterialTheme.typography.labelSmall.copy(color = OceanBlue)
                    )
                }

                Icon(
                    imageVector = if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                    contentDescription = "Expand",
                    tint = BentoSlate500,
                    modifier = Modifier.size(20.dp)
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = advisory.fishermenAdvisory,
                style = MaterialTheme.typography.bodySmall.copy(
                    color = BentoSlate700,
                    fontSize = 12.sp
                )
            )

            AnimatedVisibility(visible = expanded) {
                Column(modifier = Modifier.padding(top = 10.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "Zone: ",
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold)
                        )
                        Text(
                            text = advisory.seaZone,
                            style = MaterialTheme.typography.labelSmall.copy(color = BentoSlate600)
                        )
                    }

                    advisory.heritageMilestone?.let { milestone ->
                        Spacer(modifier = Modifier.height(6.dp))
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color(0xFFFEF9C3))
                                .padding(8.dp)
                        ) {
                            Text(
                                text = "🚀 $milestone",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = Color(0xFF854D0E),
                                    fontWeight = FontWeight.Medium
                                )
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun WeatherMetricItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    value: String
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = OceanBlue,
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium.copy(
                fontWeight = FontWeight.Bold,
                color = BentoSlate800
            )
        )
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall.copy(color = BentoSlate500)
        )
    }
}

/**
 * Interactive Animated Chandipur Vanishing Sea Tidal Simulation Canvas.
 * Models the semi-diurnal harmonic tides with flowing dual wave fronts,
 * golden sand seabed layer, dynamic vanishing sea distance indicator (-5 km),
 * and pulsating safety beacon.
 */
@Composable
fun DynamicChandipurTideCanvas(
    safeWalkMinutesRemaining: Int,
    recededDistanceKm: Double,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "tide_wave_anim")
    val wavePhase by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = (2 * Math.PI).toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 3600, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "wave_phase"
    )
    val pulseGlow by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 0.85f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse_glow"
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(115.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFF0F172A))
            .padding(horizontal = 14.dp, vertical = 10.dp)
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val w = size.width
            val h = size.height

            // 1. Golden Sand Seabed Layer (Intertidal seabed mudflat)
            val sandPath = Path().apply {
                moveTo(0f, h)
                lineTo(w, h)
                lineTo(w, h * 0.72f)
                lineTo(0f, h * 0.82f)
                close()
            }
            drawPath(
                path = sandPath,
                color = Color(0xFFD4A373).copy(alpha = 0.45f)
            )

            // Baseline representing sea-shore boundary
            drawLine(
                color = Color(0xFF475569).copy(alpha = 0.5f),
                start = Offset(0f, h * 0.52f),
                end = Offset(w, h * 0.52f),
                strokeWidth = 1.dp.toPx(),
                cap = StrokeCap.Round
            )

            // 2. Secondary Deep Tide Wave (Background flow)
            val bgPath = Path()
            val points = 80
            val midY = h * 0.48f
            for (i in 0..points) {
                val x = (i.toFloat() / points) * w
                val angle = (i.toFloat() / points) * (2.2f * Math.PI.toFloat()) - (wavePhase * 0.8f)
                val y = midY + (sin(angle) * (h * 0.22f))
                if (i == 0) bgPath.moveTo(x, y) else bgPath.lineTo(x, y)
            }
            bgPath.lineTo(w, h)
            bgPath.lineTo(0f, h)
            bgPath.close()

            drawPath(
                path = bgPath,
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF0284C7).copy(alpha = 0.25f),
                        Color(0xFF0369A1).copy(alpha = 0.40f)
                    ),
                    startY = 0f,
                    endY = h
                )
            )

            // 3. Primary Harmonic Ocean Wave (Foreground flow)
            val fgPath = Path()
            val fgStrokePath = Path()
            for (i in 0..points) {
                val x = (i.toFloat() / points) * w
                val angle = (i.toFloat() / points) * (2 * Math.PI.toFloat()) + wavePhase
                val y = midY + (sin(angle) * (h * 0.26f))
                if (i == 0) {
                    fgPath.moveTo(x, y)
                    fgStrokePath.moveTo(x, y)
                } else {
                    fgPath.lineTo(x, y)
                    fgStrokePath.lineTo(x, y)
                }
            }
            fgPath.lineTo(w, h)
            fgPath.lineTo(0f, h)
            fgPath.close()

            drawPath(
                path = fgPath,
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF38BDF8).copy(alpha = 0.35f),
                        Color(0xFF0284C7).copy(alpha = 0.65f),
                        Color(0xFF0F172A)
                    ),
                    startY = midY - (h * 0.26f),
                    endY = h
                )
            )
            drawPath(
                path = fgStrokePath,
                brush = Brush.horizontalGradient(
                    colors = listOf(Color(0xFF38BDF8), Color(0xFF7DD3FC), Color(0xFF0284C7))
                ),
                style = Stroke(width = 2.5.dp.toPx(), cap = StrokeCap.Round)
            )

            // 4. Chandipur Vanishing Sea Marker (Dynamic position based on receded distance)
            val normalizedRecede = (recededDistanceKm / 5.0).coerceIn(0.15, 0.85).toFloat()
            val markerX = w * normalizedRecede
            val markerAngle = (markerX / w) * (2 * Math.PI.toFloat()) + wavePhase
            val markerY = midY + (sin(markerAngle) * (h * 0.26f))

            // Pulsing water beacon glow
            drawCircle(
                color = Color(0xFF38BDF8).copy(alpha = pulseGlow * 0.5f),
                radius = 12.dp.toPx(),
                center = Offset(markerX, markerY)
            )
            // Solid center pin
            drawCircle(
                color = Color.White,
                radius = 5.dp.toPx(),
                center = Offset(markerX, markerY)
            )
            drawCircle(
                color = Color(0xFF0284C7),
                radius = 3.dp.toPx(),
                center = Offset(markerX, markerY)
            )

            // Dashed vertical guide to seabed
            drawLine(
                color = Color(0xFF38BDF8).copy(alpha = 0.6f),
                start = Offset(markerX, markerY),
                end = Offset(markerX, h),
                strokeWidth = 1.5.dp.toPx(),
                cap = StrokeCap.Round
            )
        }

        // Overlay Labels inside Canvas Box
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopStart),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Shoreline (0 km)",
                style = MaterialTheme.typography.labelSmall.copy(
                    fontSize = 10.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF94A3B8)
                )
            )
            Text(
                text = "Vanishing Seabed (-5 km)",
                style = MaterialTheme.typography.labelSmall.copy(
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF38BDF8)
                )
            )
        }

        // Bottom status badge
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomStart),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Tide Level: -${recededDistanceKm} km Out",
                style = MaterialTheme.typography.labelSmall.copy(
                    fontSize = 10.5.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFF1F5F9)
                )
            )
            Text(
                text = if (safeWalkMinutesRemaining > 0) "Safe Walk: ${safeWalkMinutesRemaining}m" else "High Tide Warning",
                style = MaterialTheme.typography.labelSmall.copy(
                    fontSize = 10.5.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (safeWalkMinutesRemaining > 0) Color(0xFF4ADE80) else Color(0xFFF87171)
                )
            )
        }
    }
}
