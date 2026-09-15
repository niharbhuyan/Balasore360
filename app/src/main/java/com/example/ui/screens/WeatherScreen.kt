package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Air
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material.icons.filled.Thermostat
import androidx.compose.material.icons.filled.Water
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.example.data.model.WeatherInfo
import com.example.ui.components.AdMobBannerCard
import com.example.ui.theme.AmberGold
import com.example.ui.theme.BentoSlate100
import com.example.ui.theme.BentoSlate500
import com.example.ui.theme.BentoSlate700
import com.example.ui.theme.BentoSlate800
import com.example.ui.theme.BentoSlate900
import com.example.ui.theme.EmeraldGreen
import com.example.ui.theme.OceanBlue
import com.example.ui.theme.OceanBlueDark

@Composable
fun WeatherScreen(
    weather: WeatherInfo,
    language: AppLanguage,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("weather_screen_list"),
        contentPadding = PaddingValues(bottom = 100.dp)
    ) {
        // Hero Weather Card
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
                            modifier = Modifier.size(48.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    Row(verticalAlignment = Alignment.Bottom) {
                        Text(
                            text = "${weather.tempCelsius}°",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontSize = 56.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = Color.White,
                                lineHeight = 56.sp
                            )
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.padding(bottom = 8.dp)) {
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

        // Chandipur Vanishing Tide Telemetry Card
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp)
                    .testTag("tide_card"),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF0FDF4)),
                border = BorderStroke(1.dp, Color(0xFFBBF7D0))
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFDCFCE7)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Water,
                            contentDescription = "Tide",
                            tint = EmeraldGreen,
                            modifier = Modifier.size(24.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(14.dp))

                    Column {
                        Text(
                            text = if (language == AppLanguage.ODIA) "ଚାନ୍ଦିପୁର ଜୁଆର-ଭଟ୍ଟା ସ୍ଥିତି" else "Chandipur Tide Telemetry",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF166534)
                            )
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = weather.tideStatus,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = Color(0xFF15803D),
                                fontWeight = FontWeight.Medium
                            )
                        )
                        Text(
                            text = "Walk safe on the sea bed while the water recedes up to 5 km",
                            style = MaterialTheme.typography.labelSmall.copy(color = Color(0xFF16A34A))
                        )
                    }
                }
            }
        }

        // Marine Safety & Cyclone Watch
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

        // AdMob Banner Placement in Weather
        item {
            AdMobBannerCard(
                modifier = Modifier.padding(top = 8.dp)
            )
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
