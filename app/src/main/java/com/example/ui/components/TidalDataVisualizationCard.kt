package com.example.ui.components

import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CloudDownload
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Layers
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Storage
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.filled.Water
import androidx.compose.material.icons.filled.Waves
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.data.local.TidalLocalStorage
import com.example.data.model.AppLanguage
import com.example.data.model.TidalPhase
import com.example.data.model.TidalTimeframe
import com.example.data.model.TidalTrendPoint
import com.example.data.model.TidalTrendSummary
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Recharts-style Data Visualization Card for Historical Balasore Tidal Data & Sea-Recession Trends.
 * Directly retrieved from Android Local Storage (TidalLocalStorage) with multi-series Area and Line charts,
 * Cartesian grid, interactive scrubber tooltip, series toggles, and dual rendering engine support.
 */
@Composable
fun TidalDataVisualizationCard(
    language: AppLanguage = AppLanguage.ENGLISH,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val tidalStorage = remember { TidalLocalStorage.getInstance(context) }

    var selectedTimeframe by remember { mutableStateOf(TidalTimeframe.HOURLY_24H) }
    var trendPoints by remember { mutableStateOf(tidalStorage.getTidalData(selectedTimeframe)) }
    var summary by remember { mutableStateOf(tidalStorage.getSummary()) }
    var isWebRechartsMode by remember { mutableStateOf(false) }

    // Recharts Series toggles
    var showWaterLevelSeries by remember { mutableStateOf(true) }
    var showRecessionSeries by remember { mutableStateOf(true) }

    // Interactive scrubber index (-1 means default to latest point)
    var selectedPointIndex by remember { mutableIntStateOf(trendPoints.size - 1) }

    // Sync when timeframe changes
    LaunchedEffect(selectedTimeframe) {
        trendPoints = tidalStorage.getTidalData(selectedTimeframe)
        selectedPointIndex = (trendPoints.size - 1).coerceAtLeast(0)
        summary = tidalStorage.getSummary()
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("balasore_tidal_recharts_section"),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF0B1329)),
        border = BorderStroke(1.dp, Color(0xFF1E293B)),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Column(modifier = Modifier.padding(18.dp)) {

            // 1. Header Bar with Recharts Badge & Local Storage Indicator
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
                            .background(
                                Brush.linearGradient(
                                    listOf(Color(0xFF0284C7), Color(0xFF06B6D4))
                                )
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Analytics,
                            contentDescription = "Tidal Data Visualization",
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "HISTORICAL TIDAL ANALYTICS",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF38BDF8),
                                letterSpacing = 1.sp
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Surface(
                                shape = RoundedCornerShape(4.dp),
                                color = Color(0xFF0284C7).copy(alpha = 0.3f),
                                border = BorderStroke(1.dp, Color(0xFF38BDF8).copy(alpha = 0.5f))
                            ) {
                                Text(
                                    text = "Recharts",
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFFE0F2FE),
                                    modifier = Modifier.padding(horizontal = 5.dp, vertical = 1.dp)
                                )
                            }
                        }
                        Text(
                            text = if (language == AppLanguage.ODIA) "ବାଲେଶ୍ୱର ଜୁଆର ଓ ସମୁଦ୍ର ଅପସାରଣ ତଥ୍ୟ" else "Balasore Tides & Sea-Recession",
                            fontSize = 17.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color.White
                        )
                    }
                }

                // Local Storage Tag
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = Color(0xFF10B981).copy(alpha = 0.15f),
                    border = BorderStroke(1.dp, Color(0xFF10B981).copy(alpha = 0.4f))
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = Color(0xFF10B981),
                            modifier = Modifier.size(6.dp)
                        ) {}
                        Spacer(modifier = Modifier.width(5.dp))
                        Text(
                            text = "Local Storage",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF6EE7B7)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // 2. High-Impact Metrics Strip (Local Storage Aggregates)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Metric 1: Current Water Level
                Surface(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp),
                    color = Color(0xFF0F172A),
                    border = BorderStroke(1.dp, Color(0xFF334155))
                ) {
                    Column(modifier = Modifier.padding(10.dp)) {
                        Text(
                            text = "🌊 Water Level",
                            fontSize = 10.sp,
                            color = Color(0xFF94A3B8),
                            fontWeight = FontWeight.Medium
                        )
                        Spacer(modifier = Modifier.height(3.dp))
                        Text(
                            text = String.format(Locale.US, "%.2f m", summary.currentHeightMeters),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF38BDF8)
                        )
                        Text(
                            text = "Min: %.2fm".format(summary.lowestTideLevelMeters),
                            fontSize = 9.sp,
                            color = Color(0xFF64748B)
                        )
                    }
                }

                // Metric 2: Sea-Recession Distance
                Surface(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp),
                    color = Color(0xFF0F172A),
                    border = BorderStroke(1.dp, Color(0xFF334155))
                ) {
                    Column(modifier = Modifier.padding(10.dp)) {
                        Text(
                            text = "🏖️ Vanishing Sea",
                            fontSize = 10.sp,
                            color = Color(0xFF94A3B8),
                            fontWeight = FontWeight.Medium
                        )
                        Spacer(modifier = Modifier.height(3.dp))
                        Text(
                            text = String.format(Locale.US, "%.2f km", summary.currentRecessionKm),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFF59E0B)
                        )
                        Text(
                            text = "Peak: %.2fkm".format(summary.maxRecessionObservedKm),
                            fontSize = 9.sp,
                            color = Color(0xFF64748B)
                        )
                    }
                }

                // Metric 3: Lunar Cycle & Records
                Surface(
                    modifier = Modifier.weight(1.1f),
                    shape = RoundedCornerShape(12.dp),
                    color = Color(0xFF0F172A),
                    border = BorderStroke(1.dp, Color(0xFF334155))
                ) {
                    Column(modifier = Modifier.padding(10.dp)) {
                        Text(
                            text = "🌕 Lunar Phase",
                            fontSize = 10.sp,
                            color = Color(0xFF94A3B8),
                            fontWeight = FontWeight.Medium
                        )
                        Spacer(modifier = Modifier.height(3.dp))
                        Text(
                            text = "Spring Tide",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFE2E8F0)
                        )
                        Text(
                            text = "${summary.totalObservationsCount} Records in DB",
                            fontSize = 9.sp,
                            color = Color(0xFF10B981),
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // 3. Timeframe Selector & Mode Controls
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Timeframe Chips
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    TidalTimeframe.entries.forEach { timeframe ->
                        val isSelected = selectedTimeframe == timeframe
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = if (isSelected) Color(0xFF0284C7) else Color(0xFF1E293B),
                            border = BorderStroke(
                                1.dp,
                                if (isSelected) Color(0xFF38BDF8) else Color(0xFF334155)
                            ),
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .clickable { selectedTimeframe = timeframe }
                        ) {
                            Text(
                                text = timeframe.label,
                                fontSize = 11.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                color = if (isSelected) Color.White else Color(0xFFCBD5E1),
                                modifier = Modifier.padding(horizontal = 9.dp, vertical = 5.dp)
                            )
                        }
                    }
                }

                // Engine Switcher: Native Compose vs Web Recharts
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = if (isWebRechartsMode) Color(0xFF7C3AED).copy(alpha = 0.25f) else Color(0xFF1E293B),
                    border = BorderStroke(1.dp, if (isWebRechartsMode) Color(0xFFA78BFA) else Color(0xFF334155)),
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .clickable { isWebRechartsMode = !isWebRechartsMode }
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 7.dp, vertical = 5.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = if (isWebRechartsMode) Icons.Default.Layers else Icons.Default.Tune,
                            contentDescription = "Toggle Visualization Engine",
                            tint = if (isWebRechartsMode) Color(0xFFC4B5FD) else Color(0xFF94A3B8),
                            modifier = Modifier.size(13.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = if (isWebRechartsMode) "Web Recharts" else "Native Canvas",
                            fontSize = 10.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isWebRechartsMode) Color(0xFFDDD6FE) else Color(0xFF94A3B8)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Recharts Interactive Legend (Click to Toggle Series)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Legend 1: Tide Water Level (m)
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .clickable { showWaterLevelSeries = !showWaterLevelSeries }
                        .padding(horizontal = 6.dp, vertical = 3.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .background(
                                if (showWaterLevelSeries) Color(0xFF38BDF8) else Color.Gray,
                                RoundedCornerShape(2.dp)
                            )
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Tide Water Level (m)",
                        fontSize = 11.sp,
                        fontWeight = if (showWaterLevelSeries) FontWeight.Bold else FontWeight.Normal,
                        color = if (showWaterLevelSeries) Color(0xFFE0F2FE) else Color(0xFF64748B)
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                // Legend 2: Sea Recession Distance (km)
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .clickable { showRecessionSeries = !showRecessionSeries }
                        .padding(horizontal = 6.dp, vertical = 3.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .background(
                                if (showRecessionSeries) Color(0xFFF59E0B) else Color.Gray,
                                CircleShape
                            )
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Sea Recession (km)",
                        fontSize = 11.sp,
                        fontWeight = if (showRecessionSeries) FontWeight.Bold else FontWeight.Normal,
                        color = if (showRecessionSeries) Color(0xFFFEF3C7) else Color(0xFF64748B)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // 4. THE CHART CONTAINER
            if (isWebRechartsMode) {
                // Embedded Web Recharts Visualization (SVG/HTML5 ResponsiveContainer)
                WebRechartsChart(
                    dataPoints = trendPoints,
                    showWaterLevel = showWaterLevelSeries,
                    showRecession = showRecessionSeries,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(240.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFF060B18))
                )
            } else {
                // High-Performance Native Jetpack Compose Recharts-Fidelity Canvas
                NativeRechartsCanvasChart(
                    dataPoints = trendPoints,
                    showWaterLevel = showWaterLevelSeries,
                    showRecession = showRecessionSeries,
                    selectedIndex = selectedPointIndex,
                    onSelectIndex = { selectedPointIndex = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(230.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFF060B18))
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // 5. Interactive Inspection Tooltip / Card for Selected Data Point
            val activePoint = trendPoints.getOrNull(selectedPointIndex) ?: trendPoints.lastOrNull()
            if (activePoint != null) {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    color = Color(0xFF0F172A),
                    border = BorderStroke(1.dp, Color(0xFF334155))
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "TIME: ${activePoint.timeLabel}",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = Color(0xFF38BDF8)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Surface(
                                    shape = RoundedCornerShape(4.dp),
                                    color = Color(activePoint.phase.badgeColor).copy(alpha = 0.25f),
                                    border = BorderStroke(1.dp, Color(activePoint.phase.badgeColor))
                                ) {
                                    Text(
                                        text = activePoint.phase.label,
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(activePoint.phase.badgeColor),
                                        modifier = Modifier.padding(horizontal = 5.dp, vertical = 1.dp)
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = activePoint.phaseDescription,
                                fontSize = 11.sp,
                                color = Color(0xFFE2E8F0),
                                fontWeight = FontWeight.Medium
                            )
                            Text(
                                text = "🦺 ${activePoint.safetyStatus}",
                                fontSize = 9.5.sp,
                                color = Color(0xFF94A3B8)
                            )
                        }

                        // Point Stats
                        Column(horizontalAlignment = Alignment.End) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "Height: ",
                                    fontSize = 10.sp,
                                    color = Color(0xFF94A3B8)
                                )
                                Text(
                                    text = "%.2fm".format(activePoint.tideHeightMeters),
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF38BDF8)
                                )
                            }
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "Recession: ",
                                    fontSize = 10.sp,
                                    color = Color(0xFF94A3B8)
                                )
                                Text(
                                    text = "%.2fkm".format(activePoint.recessionDistanceKm),
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFFF59E0B)
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // 6. Local Storage Data Management Bar (Sync & Reset)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Storage,
                        contentDescription = "Local Storage",
                        tint = Color(0xFF64748B),
                        modifier = Modifier.size(13.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "balasore_tidal_local_storage",
                        fontSize = 9.sp,
                        fontFamily = FontFamily.Monospace,
                        color = Color(0xFF64748B)
                    )
                }

                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    // Quick Action: Add simulated live observation to Local Storage
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = Color(0xFF1E293B),
                        border = BorderStroke(1.dp, Color(0xFF334155)),
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .clickable {
                                val sdf = SimpleDateFormat("hh:mm a", Locale.US)
                                val newLabel = sdf.format(Date())
                                val randomHeight = (0.35 + Math.random() * 3.8)
                                val randomRecession = ((5.2 - randomHeight).coerceIn(0.0, 5.2))
                                val newPoint = TidalTrendPoint(
                                    id = "tide_${System.currentTimeMillis()}",
                                    timestamp = System.currentTimeMillis(),
                                    timeLabel = newLabel,
                                    tideHeightMeters = randomHeight,
                                    recessionDistanceKm = randomRecession,
                                    phase = if (randomRecession > 4.0) TidalPhase.LOW_TIDE_PEAK else if (randomHeight > 3.0) TidalPhase.HIGH_TIDE else TidalPhase.RECEDING,
                                    phaseDescription = "Live Logged via Local Storage • ${String.format(Locale.US, "%.1f", randomRecession)} km seabed exposed",
                                    lunarCondition = "Active Astronomical Tide",
                                    safetyStatus = if (randomRecession > 3.5) "Prime Walk Safe" else "Embankment Recommended"
                                )
                                tidalStorage.addObservation(newPoint, selectedTimeframe)
                                trendPoints = tidalStorage.getTidalData(selectedTimeframe)
                                selectedPointIndex = trendPoints.size - 1
                                summary = tidalStorage.getSummary()
                            }
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 7.dp, vertical = 3.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "+ Log Reading",
                                fontSize = 9.5.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF38BDF8)
                            )
                        }
                    }

                    // Reset Storage
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = Color(0xFF1E293B),
                        border = BorderStroke(1.dp, Color(0xFF334155)),
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .clickable {
                                tidalStorage.resetToDefaults()
                                trendPoints = tidalStorage.getTidalData(selectedTimeframe)
                                selectedPointIndex = trendPoints.size - 1
                                summary = tidalStorage.getSummary()
                            }
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 7.dp, vertical = 3.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Refresh,
                                contentDescription = "Reset Storage",
                                tint = Color(0xFF94A3B8),
                                modifier = Modifier.size(11.dp)
                            )
                            Spacer(modifier = Modifier.width(3.dp))
                            Text(
                                text = "Reset",
                                fontSize = 9.5.sp,
                                fontWeight = FontWeight.Medium,
                                color = Color(0xFF94A3B8)
                            )
                        }
                    }
                }
            }
        }
    }
}

/**
 * High-Performance Native Jetpack Compose Canvas Chart matching Recharts AreaChart & LineChart architecture.
 * Implements Cartesian grid, cubic Bézier splines, dual vertical scales, and drag scrubber tooltip.
 */
@Composable
fun NativeRechartsCanvasChart(
    dataPoints: List<TidalTrendPoint>,
    showWaterLevel: Boolean,
    showRecession: Boolean,
    selectedIndex: Int,
    onSelectIndex: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    if (dataPoints.isEmpty()) return

    Box(
        modifier = modifier
            .pointerInput(dataPoints.size) {
                detectTapGestures { offset ->
                    val paddingLeft = 40.dp.toPx()
                    val paddingRight = 36.dp.toPx()
                    val chartW = (size.width - paddingLeft - paddingRight).coerceAtLeast(1f)
                    val relX = (offset.x - paddingLeft).coerceIn(0f, chartW)
                    val step = chartW / (dataPoints.size - 1).coerceAtLeast(1)
                    val idx = ((relX + step / 2) / step).toInt().coerceIn(0, dataPoints.size - 1)
                    onSelectIndex(idx)
                }
            }
            .pointerInput(dataPoints.size) {
                detectDragGestures { change, _ ->
                    val paddingLeft = 40.dp.toPx()
                    val paddingRight = 36.dp.toPx()
                    val chartW = (size.width - paddingLeft - paddingRight).coerceAtLeast(1f)
                    val relX = (change.position.x - paddingLeft).coerceIn(0f, chartW)
                    val step = chartW / (dataPoints.size - 1).coerceAtLeast(1)
                    val idx = ((relX + step / 2) / step).toInt().coerceIn(0, dataPoints.size - 1)
                    onSelectIndex(idx)
                }
            }
    ) {
        androidx.compose.foundation.Canvas(modifier = Modifier.fillMaxSize()) {
            val w = size.width
            val h = size.height

            val padLeft = 40.dp.toPx()
            val padRight = 36.dp.toPx()
            val padTop = 20.dp.toPx()
            val padBottom = 26.dp.toPx()

            val chartW = w - padLeft - padRight
            val chartH = h - padTop - padBottom

            // Scales
            val maxTideHeight = 5.0f // 0m to 5.0m
            val maxRecession = 6.0f  // 0km to 6.0km

            // 1. Cartesian Grid Lines (Recharts <CartesianGrid strokeDasharray="3 3" />)
            val gridDashes = PathEffect.dashPathEffect(floatArrayOf(6f, 6f), 0f)
            val numGridLines = 4
            for (i in 0..numGridLines) {
                val y = padTop + (chartH / numGridLines) * i
                drawLine(
                    color = Color(0xFF1E293B),
                    start = Offset(padLeft, y),
                    end = Offset(w - padRight, y),
                    strokeWidth = 1.dp.toPx(),
                    pathEffect = gridDashes
                )
            }

            val n = dataPoints.size
            if (n < 2) return@Canvas

            val stepX = chartW / (n - 1)

            // Calculate point coordinates
            val tidePoints = ArrayList<Offset>(n)
            val recessionPoints = ArrayList<Offset>(n)

            for (i in 0 until n) {
                val x = padLeft + i * stepX
                val pt = dataPoints[i]

                val tideY = padTop + chartH * (1f - (pt.tideHeightMeters.toFloat() / maxTideHeight).coerceIn(0f, 1f))
                val recY = padTop + chartH * (1f - (pt.recessionDistanceKm.toFloat() / maxRecession).coerceIn(0f, 1f))

                tidePoints.add(Offset(x, tideY))
                recessionPoints.add(Offset(x, recY))
            }

            // 2. Series A: Tide Water Level (m) -> Area Chart with Linear Gradient Fill
            if (showWaterLevel) {
                val areaPath = Path().apply {
                    moveTo(tidePoints.first().x, padTop + chartH)
                    lineTo(tidePoints.first().x, tidePoints.first().y)
                    // Smooth Bézier spline
                    for (i in 0 until n - 1) {
                        val p0 = tidePoints[i]
                        val p1 = tidePoints[i + 1]
                        val cx = (p0.x + p1.x) / 2f
                        cubicTo(cx, p0.y, cx, p1.y, p1.x, p1.y)
                    }
                    lineTo(tidePoints.last().x, padTop + chartH)
                    close()
                }

                drawPath(
                    path = areaPath,
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFF0284C7).copy(alpha = 0.50f),
                            Color(0xFF06B6D4).copy(alpha = 0.05f)
                        ),
                        startY = padTop,
                        endY = padTop + chartH
                    )
                )

                // Spline Line Stroke
                val linePath = Path().apply {
                    moveTo(tidePoints.first().x, tidePoints.first().y)
                    for (i in 0 until n - 1) {
                        val p0 = tidePoints[i]
                        val p1 = tidePoints[i + 1]
                        val cx = (p0.x + p1.x) / 2f
                        cubicTo(cx, p0.y, cx, p1.y, p1.x, p1.y)
                    }
                }
                drawPath(
                    path = linePath,
                    color = Color(0xFF38BDF8),
                    style = Stroke(width = 2.5.dp.toPx(), cap = StrokeCap.Round)
                )
            }

            // 3. Series B: Sea Recession Distance (km) -> Line Chart with Anchor Nodes
            if (showRecession) {
                val recPath = Path().apply {
                    moveTo(recessionPoints.first().x, recessionPoints.first().y)
                    for (i in 0 until n - 1) {
                        val p0 = recessionPoints[i]
                        val p1 = recessionPoints[i + 1]
                        val cx = (p0.x + p1.x) / 2f
                        cubicTo(cx, p0.y, cx, p1.y, p1.x, p1.y)
                    }
                }

                drawPath(
                    path = recPath,
                    color = Color(0xFFF59E0B),
                    style = Stroke(width = 2.5.dp.toPx(), cap = StrokeCap.Round)
                )

                // Anchor Node Dots
                for (p in recessionPoints) {
                    drawCircle(
                        color = Color(0xFF0B1329),
                        radius = 4.5.dp.toPx(),
                        center = p
                    )
                    drawCircle(
                        color = Color(0xFFF59E0B),
                        radius = 3.dp.toPx(),
                        center = p
                    )
                }
            }

            // 4. Interactive Scrubber Cursor (Vertical line at selected index)
            if (selectedIndex in 0 until n) {
                val scrubX = tidePoints[selectedIndex].x
                val scrubDashes = PathEffect.dashPathEffect(floatArrayOf(8f, 6f), 0f)

                drawLine(
                    color = Color(0xFF94A3B8),
                    start = Offset(scrubX, padTop),
                    end = Offset(scrubX, padTop + chartH),
                    strokeWidth = 1.5.dp.toPx(),
                    pathEffect = scrubDashes
                )

                // Highlighted Dots on Active Series
                if (showWaterLevel) {
                    val pTide = tidePoints[selectedIndex]
                    drawCircle(
                        color = Color(0xFF38BDF8).copy(alpha = 0.35f),
                        radius = 9.dp.toPx(),
                        center = pTide
                    )
                    drawCircle(
                        color = Color.White,
                        radius = 5.dp.toPx(),
                        center = pTide
                    )
                    drawCircle(
                        color = Color(0xFF0284C7),
                        radius = 3.5.dp.toPx(),
                        center = pTide
                    )
                }

                if (showRecession) {
                    val pRec = recessionPoints[selectedIndex]
                    drawCircle(
                        color = Color(0xFFF59E0B).copy(alpha = 0.35f),
                        radius = 9.dp.toPx(),
                        center = pRec
                    )
                    drawCircle(
                        color = Color.White,
                        radius = 5.dp.toPx(),
                        center = pRec
                    )
                    drawCircle(
                        color = Color(0xFFD97706),
                        radius = 3.5.dp.toPx(),
                        center = pRec
                    )
                }
            }
        }

        // X-Axis Time Labels Strip
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(start = 36.dp, end = 32.dp, bottom = 4.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            val step = (dataPoints.size / 4).coerceAtLeast(1)
            for (i in dataPoints.indices step step) {
                val pt = dataPoints[i]
                Text(
                    text = pt.timeLabel,
                    fontSize = 9.sp,
                    color = if (i == selectedIndex) Color(0xFF38BDF8) else Color(0xFF64748B),
                    fontWeight = if (i == selectedIndex) FontWeight.Bold else FontWeight.Normal
                )
            }
        }

        // Y-Axis Indicators (Left: Water Level, Right: Recession)
        Column(
            modifier = Modifier
                .align(Alignment.CenterStart)
                .padding(start = 6.dp, top = 16.dp, bottom = 26.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text("5m", fontSize = 8.5.sp, color = Color(0xFF38BDF8), fontWeight = FontWeight.Bold)
            Text("2.5m", fontSize = 8.5.sp, color = Color(0xFF38BDF8))
            Text("0m", fontSize = 8.5.sp, color = Color(0xFF38BDF8))
        }

        Column(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .padding(end = 6.dp, top = 16.dp, bottom = 26.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.End
        ) {
            Text("6km", fontSize = 8.5.sp, color = Color(0xFFF59E0B), fontWeight = FontWeight.Bold)
            Text("3km", fontSize = 8.5.sp, color = Color(0xFFF59E0B))
            Text("0km", fontSize = 8.5.sp, color = Color(0xFFF59E0B))
        }
    }
}

/**
 * Embedded Web Recharts Visualization (SVG/HTML5 ResponsiveContainer Engine).
 * Renders self-contained SVG responsive area and line charts styled to match Recharts specifications.
 */
@Composable
fun WebRechartsChart(
    dataPoints: List<TidalTrendPoint>,
    showWaterLevel: Boolean,
    showRecession: Boolean,
    modifier: Modifier = Modifier
) {
    val htmlContent = remember(dataPoints, showWaterLevel, showRecession) {
        generateRechartsHtml(dataPoints, showWaterLevel, showRecession)
    }

    AndroidView(
        factory = { ctx ->
            WebView(ctx).apply {
                webViewClient = WebViewClient()
                settings.javaScriptEnabled = true
                settings.loadWithOverviewMode = true
                settings.useWideViewPort = true
                setBackgroundColor(0x00000000)
            }
        },
        update = { webView ->
            webView.loadDataWithBaseURL(null, htmlContent, "text/html", "UTF-8", null)
        },
        modifier = modifier
    )
}

/**
 * Self-contained HTML5/SVG interactive Recharts layout for WebView rendering.
 */
private fun generateRechartsHtml(
    points: List<TidalTrendPoint>,
    showWaterLevel: Boolean,
    showRecession: Boolean
): String {
    val w = 380
    val h = 200
    val padL = 35
    val padR = 30
    val padT = 20
    val padB = 30
    val chartW = w - padL - padR
    val chartH = h - padT - padB

    val maxTide = 5.0
    val maxRec = 6.0

    val n = points.size.coerceAtLeast(1)
    val stepX = if (n > 1) chartW.toDouble() / (n - 1) else chartW.toDouble()

    // Build SVG paths
    val tidePoints = points.mapIndexed { i, p ->
        val x = padL + i * stepX
        val y = padT + chartH * (1.0 - (p.tideHeightMeters / maxTide).coerceIn(0.0, 1.0))
        Pair(x, y)
    }

    val recPoints = points.mapIndexed { i, p ->
        val x = padL + i * stepX
        val y = padT + chartH * (1.0 - (p.recessionDistanceKm / maxRec).coerceIn(0.0, 1.0))
        Pair(x, y)
    }

    val tidePathD = if (tidePoints.isNotEmpty()) {
        val d = StringBuilder("M ${tidePoints[0].first} ${tidePoints[0].second}")
        for (i in 1 until tidePoints.size) {
            d.append(" L ${tidePoints[i].first} ${tidePoints[i].second}")
        }
        d.toString()
    } else ""

    val tideAreaD = if (tidePoints.isNotEmpty()) {
        "$tidePathD L ${tidePoints.last().first} ${padT + chartH} L ${tidePoints.first().first} ${padT + chartH} Z"
    } else ""

    val recPathD = if (recPoints.isNotEmpty()) {
        val d = StringBuilder("M ${recPoints[0].first} ${recPoints[0].second}")
        for (i in 1 until recPoints.size) {
            d.append(" L ${recPoints[i].first} ${recPoints[i].second}")
        }
        d.toString()
    } else ""

    return """
        <!DOCTYPE html>
        <html>
        <head>
          <meta name="viewport" content="width=device-width, initial-scale=1.0, user-scalable=no">
          <style>
            * { margin:0; padding:0; box-sizing: border-box; }
            body { background: #060B18; font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif; color: #E2E8F0; overflow:hidden; }
            .grid-line { stroke: #1E293B; stroke-dasharray: 4 4; stroke-width: 1; }
            .tide-area { fill: url(#tideGradient); }
            .tide-line { stroke: #38BDF8; stroke-width: 2.5; fill: none; }
            .rec-line { stroke: #F59E0B; stroke-width: 2.5; fill: none; }
            .rec-node { fill: #F59E0B; stroke: #060B18; stroke-width: 2; }
            .axis-text { font-size: 8px; fill: #64748B; }
          </style>
        </head>
        <body>
          <svg width="100%" height="100%" viewBox="0 0 $w $h">
            <defs>
              <linearGradient id="tideGradient" x1="0" y1="0" x2="0" y2="1">
                <stop offset="5%" stop-color="#0284C7" stop-opacity="0.6"/>
                <stop offset="95%" stop-color="#06B6D4" stop-opacity="0.05"/>
              </linearGradient>
            </defs>

            <!-- Grid Lines -->
            <line x1="$padL" y1="${padT}" x2="${w - padR}" y2="${padT}" class="grid-line" />
            <line x1="$padL" y1="${padT + chartH * 0.33}" x2="${w - padR}" y2="${padT + chartH * 0.33}" class="grid-line" />
            <line x1="$padL" y1="${padT + chartH * 0.66}" x2="${w - padR}" y2="${padT + chartH * 0.66}" class="grid-line" />
            <line x1="$padL" y1="${padT + chartH}" x2="${w - padR}" y2="${padT + chartH}" class="grid-line" />

            ${if (showWaterLevel) "<path d='$tideAreaD' class='tide-area'/><path d='$tidePathD' class='tide-line'/>" else ""}
            ${if (showRecession) "<path d='$recPathD' class='rec-line'/>" else ""}

            <!-- Recession Anchor Nodes -->
            ${if (showRecession) recPoints.joinToString("") { "<circle cx='${it.first}' cy='${it.second}' r='3.5' class='rec-node'/>" } else ""}

            <!-- Y Axis Left -->
            <text x="6" y="${padT + 4}" class="axis-text" fill="#38BDF8">5m</text>
            <text x="6" y="${padT + chartH * 0.5}" class="axis-text" fill="#38BDF8">2.5m</text>
            <text x="6" y="${padT + chartH}" class="axis-text" fill="#38BDF8">0m</text>

            <!-- Y Axis Right -->
            <text x="${w - 24}" y="${padT + 4}" class="axis-text" fill="#F59E0B">6km</text>
            <text x="${w - 24}" y="${padT + chartH * 0.5}" class="axis-text" fill="#F59E0B">3km</text>
            <text x="${w - 24}" y="${padT + chartH}" class="axis-text" fill="#F59E0B">0km</text>

            <!-- X Axis Labels -->
            ${points.filterIndexed { idx, _ -> idx % 2 == 0 }.joinToString("") { p ->
                val idx = points.indexOf(p)
                val x = padL + idx * stepX
                "<text x='$x' y='${h - 8}' text-anchor='middle' class='axis-text'>${p.timeLabel}</text>"
            }}
          </svg>
        </body>
        </html>
    """.trimIndent()
}
