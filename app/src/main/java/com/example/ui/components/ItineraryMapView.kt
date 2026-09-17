package com.example.ui.components

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Directions
import androidx.compose.material.icons.filled.Layers
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.Navigation
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Route
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
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
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.ItineraryItemEntity
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min
import kotlin.math.sqrt

enum class MapThemeStyle {
    STANDARD,
    SATELLITE,
    COASTAL_TERRAIN
}

/**
 * Interactive Itinerary Map component displaying pinned locations of saved tourism hotspots
 * from the Balasore itinerary with interactive pins, route paths, pan/zoom, and directions.
 */
@Composable
fun ItineraryMapView(
    itineraryItems: List<ItineraryItemEntity>,
    selectedItem: ItineraryItemEntity?,
    onSelectItem: (ItineraryItemEntity) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var mapTheme by remember { mutableStateOf(MapThemeStyle.STANDARD) }

    // Map Pan and Zoom State
    var zoomScale by remember { mutableFloatStateOf(1f) }
    var panOffset by remember { mutableStateOf(Offset.Zero) }

    val transformableState = rememberTransformableState { zoomChange, offsetChange, _ ->
        zoomScale = (zoomScale * zoomChange).coerceIn(0.7f, 3.5f)
        panOffset += offsetChange
    }

    // Geographical bounding box for Balasore District
    val minLat = 21.30
    val maxLat = 21.68
    val minLng = 86.68
    val maxLng = 87.16

    Card(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .border(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f), RoundedCornerShape(16.dp))
            .testTag("itinerary_map_view_container"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .transformable(state = transformableState)
        ) {
            // Interactive District Canvas Map
            Canvas(
                modifier = Modifier
                    .fillMaxSize()
                    .testTag("itinerary_canvas_map")
                    .pointerInput(itineraryItems, zoomScale, panOffset) {
                        detectTapGestures { tapOffset ->
                            // Hit test for pinned hotspots
                            val canvasWidth = size.width
                            val canvasHeight = size.height
                            if (canvasWidth > 0 && canvasHeight > 0) {
                                var closestItem: ItineraryItemEntity? = null
                                var minDistance = Float.MAX_VALUE

                                itineraryItems.forEach { item ->
                                    val screenPoint = geoToScreen(
                                        lat = item.latitude,
                                        lng = item.longitude,
                                        minLat = minLat,
                                        maxLat = maxLat,
                                        minLng = minLng,
                                        maxLng = maxLng,
                                        width = canvasWidth.toFloat(),
                                        height = canvasHeight.toFloat(),
                                        zoom = zoomScale,
                                        pan = panOffset
                                    )
                                    val dist = (tapOffset - screenPoint).getDistance()
                                    if (dist < 48f && dist < minDistance) {
                                        minDistance = dist
                                        closestItem = item
                                    }
                                }

                                closestItem?.let { onSelectItem(it) }
                            }
                        }
                    }
            ) {
                drawBalasoreDistrictMap(
                    itineraryItems = itineraryItems,
                    selectedItem = selectedItem,
                    theme = mapTheme,
                    minLat = minLat,
                    maxLat = maxLat,
                    minLng = minLng,
                    maxLng = maxLng,
                    zoom = zoomScale,
                    pan = panOffset
                )
            }

            // Top Status Bar Overlay
            Surface(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .fillMaxWidth()
                    .padding(8.dp),
                shape = RoundedCornerShape(10.dp),
                color = when (mapTheme) {
                    MapThemeStyle.SATELLITE -> Color(0xCC0F172A)
                    else -> MaterialTheme.colorScheme.surface.copy(alpha = 0.92f)
                },
                shadowElevation = 2.dp
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 10.dp, vertical = 6.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Map,
                            contentDescription = null,
                            tint = when (mapTheme) {
                                MapThemeStyle.SATELLITE -> Color(0xFF38BDF8)
                                else -> MaterialTheme.colorScheme.primary
                            },
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Balasore Itinerary Map",
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = when (mapTheme) {
                                    MapThemeStyle.SATELLITE -> Color.White
                                    else -> MaterialTheme.colorScheme.onSurface
                                }
                            )
                        )
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        // Badge showing total pinned stops
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = when (mapTheme) {
                                MapThemeStyle.SATELLITE -> Color(0xFF1E293B)
                                else -> MaterialTheme.colorScheme.primaryContainer
                            }
                        ) {
                            Text(
                                text = "${itineraryItems.size} Stops",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = when (mapTheme) {
                                        MapThemeStyle.SATELLITE -> Color(0xFF7DD3FC)
                                        else -> MaterialTheme.colorScheme.onPrimaryContainer
                                    }
                                ),
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }

                        // Style Toggle Button
                        IconButton(
                            onClick = {
                                mapTheme = when (mapTheme) {
                                    MapThemeStyle.STANDARD -> MapThemeStyle.COASTAL_TERRAIN
                                    MapThemeStyle.COASTAL_TERRAIN -> MapThemeStyle.SATELLITE
                                    MapThemeStyle.SATELLITE -> MapThemeStyle.STANDARD
                                }
                            },
                            modifier = Modifier.size(26.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Layers,
                                contentDescription = "Toggle Map Style",
                                tint = when (mapTheme) {
                                    MapThemeStyle.SATELLITE -> Color.White
                                    else -> MaterialTheme.colorScheme.onSurfaceVariant
                                },
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }
            }

            // Map Controls (Zoom In, Zoom Out, Recenter)
            Column(
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .padding(end = 8.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Surface(
                    shape = CircleShape,
                    color = when (mapTheme) {
                        MapThemeStyle.SATELLITE -> Color(0xCC1E293B)
                        else -> MaterialTheme.colorScheme.surface.copy(alpha = 0.95f)
                    },
                    shadowElevation = 3.dp
                ) {
                    IconButton(
                        onClick = { zoomScale = (zoomScale + 0.3f).coerceAtMost(3.5f) },
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Zoom In",
                            tint = when (mapTheme) {
                                MapThemeStyle.SATELLITE -> Color.White
                                else -> MaterialTheme.colorScheme.onSurface
                            },
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }

                Surface(
                    shape = CircleShape,
                    color = when (mapTheme) {
                        MapThemeStyle.SATELLITE -> Color(0xCC1E293B)
                        else -> MaterialTheme.colorScheme.surface.copy(alpha = 0.95f)
                    },
                    shadowElevation = 3.dp
                ) {
                    IconButton(
                        onClick = { zoomScale = (zoomScale - 0.3f).coerceAtLeast(0.7f) },
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Remove,
                            contentDescription = "Zoom Out",
                            tint = when (mapTheme) {
                                MapThemeStyle.SATELLITE -> Color.White
                                else -> MaterialTheme.colorScheme.onSurface
                            },
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }

                Surface(
                    shape = CircleShape,
                    color = when (mapTheme) {
                        MapThemeStyle.SATELLITE -> Color(0xCC1E293B)
                        else -> MaterialTheme.colorScheme.surface.copy(alpha = 0.95f)
                    },
                    shadowElevation = 3.dp
                ) {
                    IconButton(
                        onClick = {
                            zoomScale = 1f
                            panOffset = Offset.Zero
                        },
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.MyLocation,
                            contentDescription = "Recenter",
                            tint = when (mapTheme) {
                                MapThemeStyle.SATELLITE -> Color(0xFF38BDF8)
                                else -> MaterialTheme.colorScheme.primary
                            },
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }

            // Bottom Selected Hotspot Pill Overlay
            if (selectedItem != null) {
                selectedItem.let { item ->
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = when (mapTheme) {
                            MapThemeStyle.SATELLITE -> Color(0xF00F172A)
                            else -> MaterialTheme.colorScheme.surface.copy(alpha = 0.96f)
                        },
                        shadowElevation = 6.dp,
                        border = androidx.compose.foundation.BorderStroke(
                            1.dp,
                            when (mapTheme) {
                                MapThemeStyle.SATELLITE -> Color(0xFF334155)
                                else -> MaterialTheme.colorScheme.outlineVariant
                            }
                        ),
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .fillMaxWidth()
                            .padding(8.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 10.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(
                                modifier = Modifier.weight(1f),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    contentAlignment = Alignment.Center,
                                    modifier = Modifier
                                        .size(30.dp)
                                        .clip(CircleShape)
                                        .background(
                                            when (item.category.lowercase()) {
                                                "beach" -> Color(0xFF0284C7)
                                                "temple" -> Color(0xFFD97706)
                                                "wildlife" -> Color(0xFF059669)
                                                else -> Color(0xFF7C3AED)
                                            }
                                        )
                                ) {
                                    val itemIndex = itineraryItems.indexOfFirst { it.id == item.id }
                                    Text(
                                        text = "${if (itemIndex >= 0) itemIndex + 1 else "📍"}",
                                        color = Color.White,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 12.sp
                                    )
                                }

                                Spacer(modifier = Modifier.width(8.dp))

                                Column {
                                    Text(
                                        text = item.hotspotName,
                                        style = MaterialTheme.typography.labelMedium.copy(
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 12.sp,
                                            color = when (mapTheme) {
                                                MapThemeStyle.SATELLITE -> Color.White
                                                else -> MaterialTheme.colorScheme.onSurface
                                            }
                                        ),
                                        maxLines = 1
                                    )
                                    Text(
                                        text = "Day ${item.dayNumber} • ${item.timeSlot} • ${item.weatherCondition} (${item.tempC.toInt()}°C)",
                                        style = MaterialTheme.typography.bodySmall.copy(
                                            fontSize = 10.sp,
                                            color = when (mapTheme) {
                                                MapThemeStyle.SATELLITE -> Color(0xFF94A3B8)
                                                else -> MaterialTheme.colorScheme.onSurfaceVariant
                                            }
                                        ),
                                        maxLines = 1
                                    )
                                }
                            }

                            Button(
                                onClick = {
                                    openInGoogleMaps(
                                        context,
                                        item.latitude,
                                        item.longitude,
                                        item.hotspotName
                                    )
                                },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = when (item.category.lowercase()) {
                                        "beach" -> Color(0xFF0284C7)
                                        "temple" -> Color(0xFFD97706)
                                        "wildlife" -> Color(0xFF059669)
                                        else -> Color(0xFF7C3AED)
                                    }
                                ),
                                shape = RoundedCornerShape(8.dp),
                                contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 8.dp, vertical = 4.dp),
                                modifier = Modifier.height(30.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Navigation,
                                    contentDescription = "Navigate",
                                    modifier = Modifier.size(12.dp),
                                    tint = Color.White
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "Directions",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

/**
 * Draws the high-performance 2D geo-projection of Balasore District with Bay of Bengal,
 * Subarnarekha and Budhabalanga river streams, connecting itinerary routes, and hotspot markers.
 */
private fun DrawScope.drawBalasoreDistrictMap(
    itineraryItems: List<ItineraryItemEntity>,
    selectedItem: ItineraryItemEntity?,
    theme: MapThemeStyle,
    minLat: Double,
    maxLat: Double,
    minLng: Double,
    maxLng: Double,
    zoom: Float,
    pan: Offset
) {
    val width = size.width
    val height = size.height

    // Background Land & Ocean Gradients
    val landColor = when (theme) {
        MapThemeStyle.SATELLITE -> Color(0xFF1E293B)
        MapThemeStyle.COASTAL_TERRAIN -> Color(0xFFF1F5E9)
        MapThemeStyle.STANDARD -> Color(0xFFF8FAFC)
    }
    val seaColor = when (theme) {
        MapThemeStyle.SATELLITE -> Color(0xFF0F172A)
        MapThemeStyle.COASTAL_TERRAIN -> Color(0xFFBAE6FD)
        MapThemeStyle.STANDARD -> Color(0xFFE0F2FE)
    }

    // Fill Land base
    drawRect(color = landColor)

    // Bay of Bengal Coastline on the East/Southeast
    val coastX = width * 0.78f * zoom + pan.x
    val oceanPath = Path().apply {
        moveTo(coastX, 0f)
        cubicTo(
            coastX - 30f * zoom, height * 0.35f,
            coastX + 20f * zoom, height * 0.7f,
            coastX - 15f * zoom, height
        )
        lineTo(width, height)
        lineTo(width, 0f)
        close()
    }
    drawPath(path = oceanPath, color = seaColor)

    // Intertidal mudflat zone (Chandipur receding tide area)
    val mudflatColor = when (theme) {
        MapThemeStyle.SATELLITE -> Color(0xFF334155)
        else -> Color(0xFFE2E8F0)
    }
    val mudflatPath = Path().apply {
        moveTo(coastX - 10f * zoom, height * 0.45f)
        lineTo(coastX + 45f * zoom, height * 0.52f)
        lineTo(coastX + 35f * zoom, height * 0.65f)
        lineTo(coastX - 8f * zoom, height * 0.60f)
        close()
    }
    drawPath(path = mudflatPath, color = mudflatColor.copy(alpha = 0.7f))

    // Subarnarekha River stream flowing North-East to Bay of Bengal
    val riverColor = when (theme) {
        MapThemeStyle.SATELLITE -> Color(0xFF38BDF8)
        else -> Color(0xFF7DD3FC)
    }
    val subarnarekhaPath = Path().apply {
        val startPt = geoToScreen(21.65, 86.85, minLat, maxLat, minLng, maxLng, width, height, zoom, pan)
        val midPt = geoToScreen(21.55, 87.00, minLat, maxLat, minLng, maxLng, width, height, zoom, pan)
        val endPt = geoToScreen(21.58, 87.12, minLat, maxLat, minLng, maxLng, width, height, zoom, pan)
        moveTo(startPt.x, startPt.y)
        quadraticTo(midPt.x, midPt.y, endPt.x, endPt.y)
    }
    drawPath(path = subarnarekhaPath, color = riverColor, style = Stroke(width = 3f * zoom, cap = StrokeCap.Round))

    // Budhabalanga River through Balasore town to Balaramgadi mouth
    val budhabalangaPath = Path().apply {
        val startPt = geoToScreen(21.52, 86.88, minLat, maxLat, minLng, maxLng, width, height, zoom, pan)
        val midPt = geoToScreen(21.49, 86.95, minLat, maxLat, minLng, maxLng, width, height, zoom, pan)
        val endPt = geoToScreen(21.46, 87.03, minLat, maxLat, minLng, maxLng, width, height, zoom, pan)
        moveTo(startPt.x, startPt.y)
        quadraticTo(midPt.x, midPt.y, endPt.x, endPt.y)
    }
    drawPath(path = budhabalangaPath, color = riverColor, style = Stroke(width = 2.5f * zoom, cap = StrokeCap.Round))

    // Kuldiha Wildlife Forest Reserve Green Region (West)
    val forestColor = when (theme) {
        MapThemeStyle.SATELLITE -> Color(0xFF064E3B)
        else -> Color(0xFFD1FAE5)
    }
    val forestCenter = geoToScreen(21.42, 86.72, minLat, maxLat, minLng, maxLng, width, height, zoom, pan)
    drawCircle(
        color = forestColor.copy(alpha = 0.6f),
        radius = 45f * zoom,
        center = forestCenter
    )

    // Connecting Itinerary Route Paths (Sequence order: Stop 1 -> Stop 2 -> ...)
    if (itineraryItems.size >= 2) {
        val routePath = Path()
        itineraryItems.forEachIndexed { index, item ->
            val pt = geoToScreen(item.latitude, item.longitude, minLat, maxLat, minLng, maxLng, width, height, zoom, pan)
            if (index == 0) {
                routePath.moveTo(pt.x, pt.y)
            } else {
                routePath.lineTo(pt.x, pt.y)
            }
        }

        // Draw outer glow/shadow for route
        drawPath(
            path = routePath,
            color = Color(0xFF0284C7).copy(alpha = 0.25f),
            style = Stroke(width = 8f * zoom, cap = StrokeCap.Round)
        )

        // Draw dashed itinerary line
        drawPath(
            path = routePath,
            color = Color(0xFF0284C7),
            style = Stroke(
                width = 3.5f * zoom,
                cap = StrokeCap.Round,
                pathEffect = PathEffect.dashPathEffect(floatArrayOf(14f * zoom, 8f * zoom), 0f)
            )
        )
    }

    // Render Hotspot Pins
    itineraryItems.forEachIndexed { index, item ->
        val screenPos = geoToScreen(item.latitude, item.longitude, minLat, maxLat, minLng, maxLng, width, height, zoom, pan)
        val isSelected = selectedItem?.id == item.id

        val pinColor = when (item.category.lowercase()) {
            "beach" -> Color(0xFF0284C7)
            "temple" -> Color(0xFFD97706)
            "wildlife" -> Color(0xFF059669)
            else -> Color(0xFF7C3AED)
        }

        // Pulse ring if selected
        if (isSelected) {
            drawCircle(
                color = pinColor.copy(alpha = 0.35f),
                radius = 24f * zoom,
                center = screenPos
            )
            drawCircle(
                color = pinColor.copy(alpha = 0.6f),
                radius = 18f * zoom,
                center = screenPos,
                style = Stroke(width = 2.5f)
            )
        }

        // Pin shadow
        drawCircle(
            color = Color.Black.copy(alpha = 0.25f),
            radius = 12f * zoom,
            center = screenPos + Offset(0f, 3f * zoom)
        )

        // Pin Outer Circle
        drawCircle(
            color = if (isSelected) Color.White else pinColor,
            radius = 12f * zoom,
            center = screenPos
        )

        // Pin Inner Circle
        drawCircle(
            color = if (isSelected) pinColor else Color.White,
            radius = 9.5f * zoom,
            center = screenPos
        )

        // Draw pin sequence number
        val textPaint = android.graphics.Paint().apply {
            color = if (isSelected) android.graphics.Color.WHITE else pinColor.hashCode()
            textSize = 11f * zoom
            textAlign = android.graphics.Paint.Align.CENTER
            isFakeBoldText = true
            isAntiAlias = true
        }
        drawContext.canvas.nativeCanvas.drawText(
            "${index + 1}",
            screenPos.x,
            screenPos.y + (4f * zoom),
            textPaint
        )
    }
}

/**
 * Projects geographical coordinates (latitude/longitude) into canvas screen space.
 */
private fun geoToScreen(
    lat: Double,
    lng: Double,
    minLat: Double,
    maxLat: Double,
    minLng: Double,
    maxLng: Double,
    width: Float,
    height: Float,
    zoom: Float,
    pan: Offset
): Offset {
    val paddingPercent = 0.12f
    val usableWidth = width * (1f - (paddingPercent * 2))
    val usableHeight = height * (1f - (paddingPercent * 2))

    val normX = ((lng - minLng) / (maxLng - minLng)).coerceIn(0.0, 1.0).toFloat()
    val normY = (1f - ((lat - minLat) / (maxLat - minLat)).coerceIn(0.0, 1.0)).toFloat()

    val centerX = width / 2f
    val centerY = height / 2f

    val rawX = (width * paddingPercent) + (normX * usableWidth)
    val rawY = (height * paddingPercent) + (normY * usableHeight)

    val zoomedX = centerX + (rawX - centerX) * zoom + pan.x
    val zoomedY = centerY + (rawY - centerY) * zoom + pan.y

    return Offset(zoomedX, zoomedY)
}

/**
 * Launches native Google Maps or external web directions intent.
 */
fun openInGoogleMaps(context: Context, latitude: Double, longitude: Double, label: String) {
    try {
        val uri = Uri.parse("geo:$latitude,$longitude?q=$latitude,$longitude($label)")
        val intent = Intent(Intent.ACTION_VIEW, uri).apply {
            setPackage("com.google.android.apps.maps")
        }
        context.startActivity(intent)
    } catch (_: Throwable) {
        val fallbackUri = Uri.parse("https://www.google.com/maps/search/?api=1&query=$latitude,$longitude")
        context.startActivity(Intent(Intent.ACTION_VIEW, fallbackUri))
    }
}
