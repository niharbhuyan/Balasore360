package com.example.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R

/**
 * Watermark presentation styles across Balasore 360.
 */
enum class WatermarkStyle {
    CENTER_EMBLEM,
    DIAGONAL_TILES,
    CORNER_STAMP
}

/**
 * Configurable opacity levels for the full-app watermark.
 */
enum class WatermarkOpacity(val alpha: Float, val label: String) {
    SUBTLE(0.045f, "Subtle (4.5%)"),
    MEDIUM(0.085f, "Standard (8.5%)"),
    PROMINENT(0.14f, "Prominent (14%)")
}

/**
 * Full-app branding watermark displaying the official App Name & Logo.
 * Designed to be non-intrusive, aesthetically refined, and touch-transparent.
 */
@Composable
fun AppWatermarkOverlay(
    enabled: Boolean = true,
    style: WatermarkStyle = WatermarkStyle.CENTER_EMBLEM,
    opacity: WatermarkOpacity = WatermarkOpacity.MEDIUM,
    modifier: Modifier = Modifier
) {
    if (!enabled) return

    Box(
        modifier = modifier
            .fillMaxSize()
            .testTag("app_watermark_container"),
        contentAlignment = Alignment.Center
    ) {
        when (style) {
            WatermarkStyle.CENTER_EMBLEM -> {
                CenterEmblemWatermark(opacity = opacity.alpha)
            }
            WatermarkStyle.DIAGONAL_TILES -> {
                DiagonalTilesWatermark(opacity = opacity.alpha)
            }
            WatermarkStyle.CORNER_STAMP -> {
                CornerStampWatermark(opacity = opacity.alpha)
            }
        }
    }
}

/**
 * Elegant large centered emblem watermark with app logo, primary title,
 * and regional Odia scripture.
 */
@Composable
fun CenterEmblemWatermark(
    opacity: Float,
    modifier: Modifier = Modifier
) {
    val brandColor = MaterialTheme.colorScheme.onSurface

    Box(
        modifier = modifier
            .fillMaxSize()
            .alpha(opacity)
            .testTag("app_watermark_emblem"),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .rotate(-14f)
                .padding(24.dp)
        ) {
            // Circular Crest Border surrounding the official logo
            Box(
                modifier = Modifier
                    .size(150.dp)
                    .border(3.dp, brandColor.copy(alpha = 0.5f), CircleShape)
                    .padding(6.dp)
                    .border(1.dp, brandColor.copy(alpha = 0.3f), CircleShape)
                    .padding(8.dp),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_app_icon_circle),
                    contentDescription = "Balasore 360 Logo Watermark",
                    modifier = Modifier
                        .size(110.dp)
                        .clip(CircleShape)
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Primary App Name
            Text(
                text = "BALASORE 360",
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.Black,
                    letterSpacing = 4.sp,
                    fontSize = 26.sp,
                    color = brandColor
                ),
                textAlign = TextAlign.Center
            )

            // Odia Script Name
            Text(
                text = "ବାଲେଶ୍ୱର ୩୬୦",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 2.sp,
                    fontSize = 18.sp,
                    color = brandColor
                ),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(4.dp))

            // Official subtitle ribbon
            Text(
                text = "• CHANDIPUR • BAY OF BENGAL • ODISHA •",
                style = MaterialTheme.typography.labelSmall.copy(
                    fontWeight = FontWeight.SemiBold,
                    letterSpacing = 2.5.sp,
                    fontSize = 9.sp,
                    color = brandColor
                ),
                textAlign = TextAlign.Center
            )
        }
    }
}

/**
 * Repeating diagonal watermark bands across the screen, reminiscent of official certificates.
 */
@Composable
fun DiagonalTilesWatermark(
    opacity: Float,
    modifier: Modifier = Modifier
) {
    val brandColor = MaterialTheme.colorScheme.onSurface

    Box(
        modifier = modifier
            .fillMaxSize()
            .alpha(opacity)
            .testTag("app_watermark_diagonal"),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .rotate(-25f)
                .fillMaxWidth(1.6f),
            verticalArrangement = Arrangement.spacedBy(50.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            for (i in 0..7) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    WatermarkTileItem(brandColor = brandColor)
                    WatermarkTileItem(brandColor = brandColor)
                }
            }
        }
    }
}

@Composable
private fun WatermarkTileItem(brandColor: Color) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_app_icon_circle),
            contentDescription = null,
            modifier = Modifier
                .size(24.dp)
                .clip(CircleShape)
        )
        Text(
            text = "BALASORE 360  •  ବାଲେଶ୍ୱର",
            style = MaterialTheme.typography.labelMedium.copy(
                fontWeight = FontWeight.ExtraBold,
                letterSpacing = 2.sp,
                fontSize = 13.sp,
                color = brandColor
            )
        )
    }
}

/**
 * Minimalist corner stamp watermark badge positioned cleanly near the bottom.
 */
@Composable
fun CornerStampWatermark(
    opacity: Float,
    modifier: Modifier = Modifier
) {
    val brandColor = MaterialTheme.colorScheme.onSurface

    Box(
        modifier = modifier
            .fillMaxSize()
            .alpha(opacity)
            .padding(bottom = 16.dp, end = 16.dp)
            .testTag("app_watermark_corner"),
        contentAlignment = Alignment.BottomEnd
    ) {
        Row(
            modifier = Modifier
                .clip(RoundedCornerShape(16.dp))
                .border(1.dp, brandColor.copy(alpha = 0.4f), RoundedCornerShape(16.dp))
                .padding(horizontal = 12.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_app_icon_circle),
                contentDescription = null,
                modifier = Modifier
                    .size(22.dp)
                    .clip(CircleShape)
            )
            Column {
                Text(
                    text = "Balasore 360",
                    style = MaterialTheme.typography.labelMedium.copy(
                        fontWeight = FontWeight.Black,
                        fontSize = 11.sp,
                        color = brandColor
                    )
                )
                Text(
                    text = "ବାଲେଶ୍ୱର ୩୬୦",
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 9.sp,
                        color = brandColor
                    )
                )
            }
        }
    }
}
