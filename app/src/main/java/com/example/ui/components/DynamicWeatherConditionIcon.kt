package com.example.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material.icons.filled.Thunderstorm
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
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
 * Dynamically fetches and renders high-definition weather icons based on
 * meteorological conditions reported by the live Weather API.
 * Includes animated atmospheric glow and graceful vector fallback.
 */
@Composable
fun DynamicWeatherConditionIcon(
    iconUrl: String,
    condition: String,
    weatherCode: Int,
    isDay: Boolean = true,
    size: Dp = 64.dp,
    showLiveBadge: Boolean = true,
    modifier: Modifier = Modifier
) {
    // Dynamic glow colors based on meteorological condition
    val (glowStart, glowEnd, fallbackEmoji) = when {
        weatherCode in 95..99 || condition.contains("Thunder", true) -> {
            Triple(Color(0xFF7C3AED), Color(0xFFF59E0B), "⛈️")
        }
        weatherCode in 51..67 || weatherCode in 80..82 || condition.contains("Rain", true) -> {
            Triple(Color(0xFF0284C7), Color(0xFF0EA5E9), "🌧️")
        }
        weatherCode in 45..48 || condition.contains("Fog", true) || condition.contains("Mist", true) -> {
            Triple(Color(0xFF64748B), Color(0xFF94A3B8), "🌫️")
        }
        weatherCode == 0 || (weatherCode == 1 && isDay) || condition.contains("Sunny", true) || condition.contains("Clear", true) -> {
            Triple(Color(0xFFF59E0B), Color(0xFFF97316), if (isDay) "☀️" else "🌙")
        }
        else -> {
            Triple(Color(0xFF38BDF8), Color(0xFF60A5FA), "⛅")
        }
    }

    val infiniteTransition = rememberInfiniteTransition(label = "weather_icon_pulse")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 0.96f,
        targetValue = 1.04f,
        animationSpec = infiniteRepeatable(
            animation = tween(2200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse_scale"
    )

    Box(
        modifier = modifier
            .size(size)
            .testTag("dynamic_weather_condition_icon"),
        contentAlignment = Alignment.Center
    ) {
        // Ambient meteorological atmosphere aura
        Box(
            modifier = Modifier
                .size(size)
                .scale(pulseScale)
                .clip(CircleShape)
                .background(
                    Brush.radialGradient(
                        colors = listOf(
                            glowStart.copy(alpha = 0.45f),
                            glowEnd.copy(alpha = 0.15f),
                            Color.Transparent
                        )
                    )
                )
        )

        // Core container
        Surface(
            modifier = Modifier
                .size(size * 0.88f)
                .clip(CircleShape)
                .border(1.dp, glowStart.copy(alpha = 0.35f), CircleShape),
            shape = CircleShape,
            color = Color.White.copy(alpha = 0.18f)
        ) {
            Box(
                modifier = Modifier.size(size * 0.88f),
                contentAlignment = Alignment.Center
            ) {
                if (iconUrl.isNotBlank()) {
                    SubcomposeAsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(iconUrl)
                            .crossfade(true)
                            .build(),
                        contentDescription = condition,
                        contentScale = ContentScale.Fit,
                        modifier = Modifier
                            .size(size * 0.72f)
                            .testTag("weather_condition_image"),
                        loading = {
                            Box(contentAlignment = Alignment.Center) {
                                Text(
                                    text = fallbackEmoji,
                                    fontSize = (size.value * 0.42f).sp
                                )
                            }
                        },
                        error = {
                            Box(contentAlignment = Alignment.Center) {
                                Text(
                                    text = fallbackEmoji,
                                    fontSize = (size.value * 0.42f).sp
                                )
                            }
                        }
                    )
                } else {
                    Text(
                        text = fallbackEmoji,
                        fontSize = (size.value * 0.42f).sp
                    )
                }
            }
        }

        // Optional subtle "LIVE API" telemetry pip badge
        if (showLiveBadge && size >= 48.dp) {
            Surface(
                shape = RoundedCornerShape(6.dp),
                color = Color(0xFF0F172A).copy(alpha = 0.85f),
                border = androidx.compose.foundation.BorderStroke(0.5.dp, Color(0xFF38BDF8)),
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .offset(x = 4.dp, y = 2.dp)
            ) {
                Text(
                    text = "LIVE",
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontSize = 8.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF38BDF8)
                    ),
                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                )
            }
        }
    }
}
