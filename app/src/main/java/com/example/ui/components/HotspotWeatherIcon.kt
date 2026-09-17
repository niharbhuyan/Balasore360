package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material.icons.filled.Thermostat
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest

/**
 * Composable function that displays real-time remote weather iconography for tourism hotspots
 * in the Balasore itinerary using the Coil library.
 */
@Composable
fun HotspotWeatherIcon(
    iconUrl: String,
    weatherCondition: String,
    modifier: Modifier = Modifier,
    iconSize: Dp = 36.dp
) {
    val context = LocalContext.current

    SubcomposeAsyncImage(
        model = ImageRequest.Builder(context)
            .data(iconUrl)
            .crossfade(true)
            .build(),
        contentDescription = "Weather condition: $weatherCondition",
        contentScale = ContentScale.Fit,
        modifier = modifier
            .size(iconSize)
            .testTag("hotspot_weather_icon"),
        loading = {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.size(iconSize)
            ) {
                CircularProgressIndicator(
                    strokeWidth = 2.dp,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(iconSize * 0.5f)
                )
            }
        },
        error = {
            // Contextual fallback icon if network is unavailable
            val fallbackVector = when {
                weatherCondition.contains("Rain", ignoreCase = true) ||
                weatherCondition.contains("Shower", ignoreCase = true) -> Icons.Default.Cloud
                weatherCondition.contains("Cloud", ignoreCase = true) ||
                weatherCondition.contains("Breeze", ignoreCase = true) -> Icons.Default.Cloud
                else -> Icons.Default.WbSunny
            }
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(iconSize)
                    .clip(CircleShape)
                    .background(Color(0xFFFFFAEB))
            ) {
                Icon(
                    imageVector = fallbackVector,
                    contentDescription = weatherCondition,
                    tint = Color(0xFFD97706),
                    modifier = Modifier.size(iconSize * 0.7f)
                )
            }
        }
    )
}

/**
 * Enhanced badge showing real-time remote weather icon via Coil alongside temperature & condition pill.
 */
@Composable
fun HotspotWeatherBadge(
    iconUrl: String,
    weatherCondition: String,
    tempC: Double,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.testTag("hotspot_weather_badge"),
        shape = RoundedCornerShape(12.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.7f),
        border = androidx.compose.foundation.BorderStroke(
            0.5.dp,
            MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
        )
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            HotspotWeatherIcon(
                iconUrl = iconUrl,
                weatherCondition = weatherCondition,
                iconSize = 24.dp
            )
            Spacer(modifier = Modifier.width(6.dp))
            Column {
                Text(
                    text = "${tempC.toInt()}°C",
                    style = MaterialTheme.typography.labelMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                )
                Text(
                    text = weatherCondition,
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontSize = 9.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    ),
                    maxLines = 1
                )
            }
        }
    }
}
