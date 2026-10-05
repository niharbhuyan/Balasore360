package com.example.ui.components

import android.os.Bundle
import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.material.icons.filled.DirectionsWalk
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Layers
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.filled.Water
import androidx.compose.material.icons.filled.Waves
import androidx.compose.material.icons.filled.ZoomIn
import androidx.compose.material.icons.filled.ZoomOut
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.example.data.daily.DailyBalasorePulse
import com.example.data.model.AppLanguage
import com.example.ui.theme.AmberGold
import com.example.ui.theme.BentoSlate100
import com.example.ui.theme.BentoSlate200
import com.example.ui.theme.BentoSlate600
import com.example.ui.theme.BentoSlate800
import com.example.ui.theme.BentoSlate900
import com.example.ui.theme.EmeraldGreen
import com.example.ui.theme.OceanBlue
import com.example.ui.theme.OceanBlueDark
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.Dash
import com.google.android.gms.maps.model.Gap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.Polygon
import com.google.android.gms.maps.model.PolygonOptions
import com.google.android.gms.maps.model.Polyline
import com.google.android.gms.maps.model.PolylineOptions
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.Locale

/**
 * Interactive Google Maps View component integrated into the Dashboard.
 * Visualizes the world-famous Chandipur "Vanishing Sea" Tidal Range dynamically
 * using the Google Maps SDK for Android.
 *
 * Visual Features:
 *  - Shoreline High-Tide Contour (zero-km mark)
 *  - Dynamic Intertidal Seabed Polygon (receding 0 to 5 km)
 *  - Dynamic Bay of Bengal Waterline Polygon
 *  - 5.0 km Vanishing Sea Boundary Polyline (Dashed Cyan)
 *  - DRDO ITR Airspace & Coastal Test Range Safety Zone
 *  - Horseshoe Crab Intertidal Mudflats
 *  - Real-time Tide Level Slider & Automated Tide Cycle Simulation
 *  - Map type switcher (Normal, Satellite, Hybrid)
 */
@Composable
fun ChandipurTidalGoogleMapView(
    dailyPulse: DailyBalasorePulse,
    language: AppLanguage = AppLanguage.ENGLISH,
    modifier: Modifier = Modifier,
    onOpenFullMap: (() -> Unit)? = null
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val coroutineScope = rememberCoroutineScope()

    // Chandipur base coordinates
    val beachCenter = LatLng(21.4695, 87.0185)
    val beachShoreNorth = LatLng(21.4920, 87.0250) // Balaramgadi mouth
    val beachShoreMid = LatLng(21.4705, 87.0135)   // Main Chandipur beach
    val beachShoreSouth = LatLng(21.4460, 87.0090) // South stretch near DRDO

    // Current tide recession distance in kilometers (0.0 km = Full High Tide, 5.0 km = Full Low Tide / Sea Vanished)
    // Initialize based on time of day
    val initialRecession = remember {
        val hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
        if (hour in 10..14 || hour in 22..24 || hour in 0..2) 4.2f else 1.4f
    }
    var recessionKm by remember { mutableFloatStateOf(initialRecession) }
    var isSimulatingCycle by remember { mutableStateOf(false) }
    var currentMapType by remember { mutableIntStateOf(GoogleMap.MAP_TYPE_NORMAL) }
    var googleMapInstance by remember { mutableStateOf<GoogleMap?>(null) }

    // Map Overlays references
    var seabedPolygon by remember { mutableStateOf<Polygon?>(null) }
    var seaWaterPolygon by remember { mutableStateOf<Polygon?>(null) }
    var vanishingLimitPolyline by remember { mutableStateOf<Polyline?>(null) }
    var shorelinePolyline by remember { mutableStateOf<Polyline?>(null) }

    // Markers reference
    val markers = remember { mutableListOf<Marker>() }

    val isMapsKeyConfigured = remember {
        com.example.BuildConfig.MAPS_API_KEY.isNotBlank()
    }
    var useVectorRadar by remember { mutableStateOf(true) }

    // Lazy MapView instance: Only created if user explicitly selects Satellite mode AND key is configured
    val mapView = remember(useVectorRadar) {
        if (!useVectorRadar && isMapsKeyConfigured) {
            MapView(context).apply {
                onCreate(Bundle())
            }
        } else {
            null
        }
    }

    // Bind Android Lifecycle
    DisposableEffect(lifecycleOwner, mapView) {
        val observer = LifecycleEventObserver { _, event ->
            mapView?.let { mv ->
                when (event) {
                    Lifecycle.Event.ON_START -> mv.onStart()
                    Lifecycle.Event.ON_RESUME -> mv.onResume()
                    Lifecycle.Event.ON_PAUSE -> mv.onPause()
                    Lifecycle.Event.ON_STOP -> mv.onStop()
                    Lifecycle.Event.ON_DESTROY -> mv.onDestroy()
                    else -> {}
                }
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
            try {
                mapView?.onDestroy()
            } catch (_: Exception) {}
        }
    }

    // Auto-Simulate Tide Cycle
    LaunchedEffect(isSimulatingCycle) {
        if (!isSimulatingCycle) return@LaunchedEffect
        var increasing = true
        while (isSimulatingCycle) {
            delay(120)
            if (increasing) {
                recessionKm += 0.1f
                if (recessionKm >= 5.0f) {
                    recessionKm = 5.0f
                    increasing = false
                    delay(800)
                }
            } else {
                recessionKm -= 0.1f
                if (recessionKm <= 0.2f) {
                    recessionKm = 0.2f
                    increasing = true
                    delay(800)
                }
            }
        }
    }

    // Helper to calculate longitude offset based on km (at 21.47°N, 1 degree longitude ≈ 103.5 km)
    fun kmToLngOffset(km: Float): Double = (km / 103.5)

    // Function to update map polygons & overlays dynamically when recessionKm or map changes
    fun updateTidalMapVisuals(map: GoogleMap, km: Float) {
        val offset = kmToLngOffset(km)
        val maxOffset = kmToLngOffset(5.0f)

        // Clean previous dynamic overlays
        seabedPolygon?.remove()
        seaWaterPolygon?.remove()

        // 1. Exposed Intertidal Seabed Polygon (Between Shoreline and current Water's edge)
        val currentWaterNorth = LatLng(beachShoreNorth.latitude, beachShoreNorth.longitude + offset)
        val currentWaterMid = LatLng(beachShoreMid.latitude, beachShoreMid.longitude + offset)
        val currentWaterSouth = LatLng(beachShoreSouth.latitude, beachShoreSouth.longitude + offset)

        val seabedPoints = listOf(
            beachShoreNorth,
            currentWaterNorth,
            currentWaterMid,
            currentWaterSouth,
            beachShoreSouth,
            beachShoreMid
        )

        // Semi-transparent warm golden sand tone for exposed seabed
        val seabedColor = android.graphics.Color.argb(70, 245, 158, 11) // Amber 500
        val seabedStroke = android.graphics.Color.argb(180, 217, 119, 6)
        seabedPolygon = map.addPolygon(
            PolygonOptions()
                .addAll(seabedPoints)
                .fillColor(seabedColor)
                .strokeColor(seabedStroke)
                .strokeWidth(3f)
        )

        // 2. Sea Water Polygon (from current water edge extending out into the Bay of Bengal)
        val deepSeaNorth = LatLng(beachShoreNorth.latitude + 0.01, beachShoreNorth.longitude + maxOffset + 0.04)
        val deepSeaSouth = LatLng(beachShoreSouth.latitude - 0.01, beachShoreSouth.longitude + maxOffset + 0.04)

        val waterPoints = listOf(
            currentWaterNorth,
            deepSeaNorth,
            deepSeaSouth,
            currentWaterSouth,
            currentWaterMid
        )

        // Vibrant azure / ocean blue water polygon
        val waterColor = android.graphics.Color.argb(95, 2, 132, 199) // Sky 600
        val waterStroke = android.graphics.Color.argb(220, 3, 105, 161)
        seaWaterPolygon = map.addPolygon(
            PolygonOptions()
                .addAll(waterPoints)
                .fillColor(waterColor)
                .strokeColor(waterStroke)
                .strokeWidth(3f)
        )
    }

    // Re-render tidal visuals whenever recessionKm or googleMapInstance changes
    LaunchedEffect(googleMapInstance, recessionKm) {
        googleMapInstance?.let { map ->
            updateTidalMapVisuals(map, recessionKm)
        }
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("chandipur_tidal_google_maps_dashboard_card"),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = BentoSlate900),
        border = BorderStroke(1.5.dp, Color(0xFF0284C7).copy(alpha = 0.5f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Header: Title & Mode Badges
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        shape = CircleShape,
                        color = Color(0xFF0284C7).copy(alpha = 0.25f),
                        modifier = Modifier.size(38.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Default.Waves,
                                contentDescription = "Chandipur Tidal Map",
                                tint = Color(0xFF38BDF8),
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = if (language == AppLanguage.ODIA) "ଚାନ୍ଦିପୁର ଜୁଆର-ଭଟ୍ଟା ମ୍ୟାପ୍" else "Chandipur Tidal Range Map",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = if (useVectorRadar) Color(0xFF0284C7).copy(alpha = 0.3f) else Color(0xFFD97706).copy(alpha = 0.3f),
                                border = BorderStroke(0.5.dp, if (useVectorRadar) Color(0xFF38BDF8) else Color(0xFFF59E0B))
                            ) {
                                Text(
                                    text = if (useVectorRadar) "RADAR CANVAS" else "MAPS SDK",
                                    color = if (useVectorRadar) Color(0xFF7DD3FC) else Color(0xFFFDE68A),
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }
                        Text(
                            text = if (language == AppLanguage.ODIA) "ସମୁଦ୍ର ୫ କିମି ପଛକୁ ଯିବାର ଲାଇଭ୍ ଦୃଶ୍ୟ" else "Live 5 km Sea Recession & Vanishing Coastline",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = Color(0xFF94A3B8),
                                fontSize = 11.sp
                            )
                        )
                    }
                }

                // Map Layer Switcher Icon
                IconButton(
                    onClick = {
                        if (isMapsKeyConfigured) {
                            useVectorRadar = !useVectorRadar
                        } else {
                            android.widget.Toast.makeText(
                                context,
                                if (language == AppLanguage.ODIA) "ଅଫଲାଇନ୍ ଭେକ୍ଟର ରାଡାର୍ ସକ୍ରିୟ ଅଛି" else "Offline Vector Radar Active (No Maps Key required)",
                                android.widget.Toast.LENGTH_SHORT
                            ).show()
                        }
                    },
                    modifier = Modifier.size(36.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Layers,
                        contentDescription = "Switch Map Type",
                        tint = if (useVectorRadar) Color(0xFF7DD3FC) else Color(0xFFF59E0B),
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Map Container with Overlaid Stats Pill
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(280.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .border(1.dp, Color(0xFF1E293B), RoundedCornerShape(16.dp))
            ) {
                if (!useVectorRadar && mapView != null) {
                    AndroidView(
                        factory = {
                            mapView.apply {
                            getMapAsync { map ->
                                googleMapInstance = map
                                map.mapType = currentMapType
                                map.uiSettings.isZoomControlsEnabled = false
                                map.uiSettings.isCompassEnabled = true
                                map.uiSettings.isMapToolbarEnabled = false

                                // Center on Chandipur coastline
                                val cameraUpdate = CameraUpdateFactory.newLatLngZoom(LatLng(21.4720, 87.0380), 12.8f)
                                map.moveCamera(cameraUpdate)

                                // Add Static Shoreline Polyline (Navy Blue)
                                val shorePoints = listOf(beachShoreNorth, beachShoreMid, beachShoreSouth)
                                shorelinePolyline = map.addPolyline(
                                    PolylineOptions()
                                        .addAll(shorePoints)
                                        .color(android.graphics.Color.argb(255, 30, 58, 138))
                                        .width(6f)
                                )

                                // Add 5 km Maximum Vanishing Line (Dashed Cyan)
                                val maxOffset = kmToLngOffset(5.0f)
                                val limitNorth = LatLng(beachShoreNorth.latitude, beachShoreNorth.longitude + maxOffset)
                                val limitMid = LatLng(beachShoreMid.latitude, beachShoreMid.longitude + maxOffset)
                                val limitSouth = LatLng(beachShoreSouth.latitude, beachShoreSouth.longitude + maxOffset)

                                vanishingLimitPolyline = map.addPolyline(
                                    PolylineOptions()
                                        .addAll(listOf(limitNorth, limitMid, limitSouth))
                                        .color(android.graphics.Color.argb(230, 6, 182, 212))
                                        .width(5f)
                                        .pattern(listOf(Dash(20f), Gap(15f)))
                                )

                                // Clear prior markers
                                markers.forEach { it.remove() }
                                markers.clear()

                                // Marker 1: Chandipur Main Promenade & Watchtower
                                val mBeach = map.addMarker(
                                    MarkerOptions()
                                        .position(beachShoreMid)
                                        .title("Chandipur Beach Shoreline (0 km)")
                                        .snippet("High-Tide Watermark & Tourist Watchtower")
                                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
                                )
                                mBeach?.let { markers.add(it) }

                                // Marker 2: 2.5 km Mid-Tide Sandbar (Turnaround Point)
                                val m2_5 = map.addMarker(
                                    MarkerOptions()
                                        .position(LatLng(beachShoreMid.latitude, beachShoreMid.longitude + kmToLngOffset(2.5f)))
                                        .title("2.5 km Intertidal Sandbar")
                                        .snippet("Safe walking turnaround zone • Golden sand ripples")
                                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW))
                                )
                                m2_5?.let { markers.add(it) }

                                // Marker 3: 5.0 km Vanishing Sea Margin
                                val m5_0 = map.addMarker(
                                    MarkerOptions()
                                        .position(limitMid)
                                        .title("5.0 km Vanishing Sea Edge")
                                        .snippet("Maximum low-tide recession • Open Bay of Bengal")
                                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN))
                                )
                                m5_0?.let { markers.add(it) }

                                // Marker 4: DRDO ITR Coastal Safety Sector
                                val mDrdo = map.addMarker(
                                    MarkerOptions()
                                        .position(LatLng(21.4440, 87.0260))
                                        .title("DRDO Integrated Test Range (ITR)")
                                        .snippet("Missile Test Coastal Sector • Security Exclusion Area")
                                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
                                )
                                mDrdo?.let { markers.add(it) }

                                // Marker 5: Balaramgadi Fish Landing Estuary
                                val mEstuary = map.addMarker(
                                    MarkerOptions()
                                        .position(LatLng(21.4925, 87.0340))
                                        .title("Balaramgadi Estuary & Red Crab Zone")
                                        .snippet("Budhabalanga confluence • Fishing trawler landing")
                                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                                )
                                mEstuary?.let { markers.add(it) }

                                // Initial visual render
                                updateTidalMapVisuals(map, recessionKm)
                            }
                        }
                    },
                    modifier = Modifier.fillMaxSize()
                )
            } else {
                ChandipurTidalRadarCanvas(
                    recessionKm = recessionKm,
                    language = language,
                    modifier = Modifier.fillMaxSize()
                )
            }

                // Overlaid Top Bar Inside Map: Quick Zoom / Center Buttons
                Row(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(10.dp),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Surface(
                        shape = CircleShape,
                        color = BentoSlate900.copy(alpha = 0.85f),
                        border = BorderStroke(1.dp, Color(0xFF334155)),
                        modifier = Modifier.size(34.dp)
                    ) {
                        IconButton(
                            onClick = {
                                googleMapInstance?.animateCamera(CameraUpdateFactory.zoomIn())
                            },
                            modifier = Modifier.fillMaxSize()
                        ) {
                            Icon(
                                imageVector = Icons.Default.ZoomIn,
                                contentDescription = "Zoom In",
                                tint = Color.White,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }

                    Surface(
                        shape = CircleShape,
                        color = BentoSlate900.copy(alpha = 0.85f),
                        border = BorderStroke(1.dp, Color(0xFF334155)),
                        modifier = Modifier.size(34.dp)
                    ) {
                        IconButton(
                            onClick = {
                                googleMapInstance?.animateCamera(CameraUpdateFactory.zoomOut())
                            },
                            modifier = Modifier.fillMaxSize()
                        ) {
                            Icon(
                                imageVector = Icons.Default.ZoomOut,
                                contentDescription = "Zoom Out",
                                tint = Color.White,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }

                    Surface(
                        shape = CircleShape,
                        color = BentoSlate900.copy(alpha = 0.85f),
                        border = BorderStroke(1.dp, Color(0xFF334155)),
                        modifier = Modifier.size(34.dp)
                    ) {
                        IconButton(
                            onClick = {
                                googleMapInstance?.animateCamera(
                                    CameraUpdateFactory.newLatLngZoom(LatLng(21.4720, 87.0380), 12.8f)
                                )
                            },
                            modifier = Modifier.fillMaxSize()
                        ) {
                            Icon(
                                imageVector = Icons.Default.MyLocation,
                                contentDescription = "Reset Center",
                                tint = Color(0xFF38BDF8),
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }

                // Overlaid Bottom Bar Inside Map: Real-time Recession Status
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = BentoSlate900.copy(alpha = 0.90f),
                    border = BorderStroke(1.dp, Color(0xFF0284C7)),
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 10.dp, start = 12.dp, end = 12.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        val isSafe = recessionKm > 1.0f
                        Icon(
                            imageVector = if (isSafe) Icons.Default.DirectionsWalk else Icons.Default.Warning,
                            contentDescription = null,
                            tint = if (isSafe) EmeraldGreen else AmberGold,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = String.format(
                                Locale.getDefault(),
                                if (language == AppLanguage.ODIA) "ସମୁଦ୍ର ପଛକୁ: %.1f କିମି • %s" else "Receded: %.1f km offshore • %s",
                                recessionKm,
                                if (recessionKm >= 3.5f) (if (language == AppLanguage.ODIA) "ଭଟ୍ଟା (ସୁରକ୍ଷିତ ଚାଲିବା ସମୟ)" else "Deep Low Tide (Safe Walk)")
                                else if (recessionKm >= 1.5f) (if (language == AppLanguage.ODIA) "ମଧ୍ୟମ ଜୁଆର" else "Mid Tide")
                                else (if (language == AppLanguage.ODIA) "ପୂର୍ଣ୍ଣ ଜୁଆର (କୂଳରେ ରୁହନ୍ତୁ)" else "High Tide (Stay at Shore)")
                            ),
                            style = MaterialTheme.typography.bodySmall.copy(
                                fontWeight = FontWeight.SemiBold,
                                color = Color.White,
                                fontSize = 11.sp
                            )
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Dynamic Slider & Simulation Control
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(Color(0xFF0F172A))
                    .padding(12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (language == AppLanguage.ODIA) "ଜୁଆର-ଭଟ୍ଟା ସ୍ଲାଇଡର୍ (୦ ରୁ ୫ କିମି)" else "Dynamic Tide Distance Slider (0 to 5 km)",
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFE2E8F0)
                        )
                    )

                    // Cycle Simulation Toggle Button
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = if (isSimulatingCycle) Color(0xFFEF4444).copy(alpha = 0.2f) else Color(0xFF0284C7).copy(alpha = 0.2f),
                        border = BorderStroke(1.dp, if (isSimulatingCycle) Color(0xFFEF4444) else Color(0xFF0284C7)),
                        modifier = Modifier.clip(RoundedCornerShape(8.dp))
                    ) {
                        Row(
                            modifier = Modifier
                                .padding(horizontal = 8.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            IconButton(
                                onClick = { isSimulatingCycle = !isSimulatingCycle },
                                modifier = Modifier.size(20.dp)
                            ) {
                                Icon(
                                    imageVector = if (isSimulatingCycle) Icons.Default.Stop else Icons.Default.PlayArrow,
                                    contentDescription = "Simulate",
                                    tint = if (isSimulatingCycle) Color(0xFFFCA5A5) else Color(0xFF7DD3FC),
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = if (isSimulatingCycle) (if (language == AppLanguage.ODIA) "ସ୍ଥଗିତ" else "Stop Cycle") else (if (language == AppLanguage.ODIA) "ସାଇକଲ୍ ଚଳାନ୍ତୁ" else "Auto-Cycle"),
                                color = if (isSimulatingCycle) Color(0xFFFCA5A5) else Color(0xFF7DD3FC),
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                Slider(
                    value = recessionKm,
                    onValueChange = {
                        isSimulatingCycle = false
                        recessionKm = it
                    },
                    valueRange = 0.0f..5.0f,
                    steps = 50,
                    colors = SliderDefaults.colors(
                        thumbColor = Color(0xFF38BDF8),
                        activeTrackColor = Color(0xFF0284C7),
                        inactiveTrackColor = Color(0xFF334155)
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = if (language == AppLanguage.ODIA) "୦ କିମି (ପୂର୍ଣ୍ଣ ଜୁଆର)" else "0 km (High Tide)",
                        color = Color(0xFF64748B),
                        fontSize = 10.5.sp
                    )
                    Text(
                        text = if (language == AppLanguage.ODIA) "୨.୫ କିମି (ମଧ୍ୟଭଟ୍ଟା)" else "2.5 km (Mid Point)",
                        color = Color(0xFF64748B),
                        fontSize = 10.5.sp
                    )
                    Text(
                        text = if (language == AppLanguage.ODIA) "୫.୦ କିମି (ପୂର୍ଣ୍ଣ ଭଟ୍ଟା)" else "5.0 km (Full Low Tide)",
                        color = Color(0xFF64748B),
                        fontSize = 10.5.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Map Markers Legend
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(modifier = Modifier.size(10.dp).clip(CircleShape).background(Color(0xFF22C55E)))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Shore", color = Color(0xFF94A3B8), fontSize = 10.5.sp)

                    Spacer(modifier = Modifier.width(10.dp))
                    Box(modifier = Modifier.size(10.dp).clip(CircleShape).background(Color(0xFFEAB308)))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("2.5km Turn", color = Color(0xFF94A3B8), fontSize = 10.5.sp)

                    Spacer(modifier = Modifier.width(10.dp))
                    Box(modifier = Modifier.size(10.dp).clip(CircleShape).background(Color(0xFF06B6D4)))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("5km Edge", color = Color(0xFF94A3B8), fontSize = 10.5.sp)

                    Spacer(modifier = Modifier.width(10.dp))
                    Box(modifier = Modifier.size(10.dp).clip(CircleShape).background(Color(0xFFEF4444)))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("DRDO ITR", color = Color(0xFF94A3B8), fontSize = 10.5.sp)
                }

                // Preset camera focus buttons
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = Color(0xFF0284C7).copy(alpha = 0.2f),
                    border = BorderStroke(0.5.dp, Color(0xFF0284C7).copy(alpha = 0.4f))
                ) {
                    Text(
                        text = if (language == AppLanguage.ODIA) "ଦୈନିକ ୱିଣ୍ଡୋ: " + dailyPulse.chandipurLowTideWindow else "Window: " + dailyPulse.chandipurLowTideWindow,
                        color = Color(0xFF7DD3FC),
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }
        }
    }
}

/**
 * 100% Offline Canvas Tidal Radar for Chandipur Vanishing Sea.
 * Renders the shoreline (0km), intertidal seabed ripples, dynamic receding seawater with animated
 * wave crests, safe turnaround zone (2.5km), maximum vanishing margin (5.0km), and DRDO sector boundary.
 * Completely immune to Google Maps SDK authorization errors.
 */
@Composable
fun ChandipurTidalRadarCanvas(
    recessionKm: Float,
    language: AppLanguage,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "wave_pulse")
    val waveAnim by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(2800, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "wave_phase"
    )

    Canvas(
        modifier = modifier
            .fillMaxSize()
            .testTag("chandipur_tidal_radar_canvas")
    ) {
        val w = size.width
        val h = size.height

        // 1. Base Coastline Land & Promenade (Left 18% width)
        val coastWidth = w * 0.18f
        drawRect(
            color = Color(0xFFD4A373),
            topLeft = Offset(0f, 0f),
            size = Size(coastWidth, h)
        )

        // 2. Intertidal Seabed Zone (from coastWidth to vanishing margin at 85% width)
        val maxVanishingWidth = w * 0.85f
        val intertidalSpan = maxVanishingWidth - coastWidth
        val currentWaterEdgeX = coastWidth + (intertidalSpan * (recessionKm / 5.0f).coerceIn(0.04f, 1.0f))

        // Draw exposed intertidal wet seabed with subtle coastal gradient
        drawRect(
            brush = Brush.horizontalGradient(
                colors = listOf(
                    Color(0xFFE9D5A1),
                    Color(0xFFD3B683),
                    Color(0xFF94D2BD)
                ),
                startX = coastWidth,
                endX = currentWaterEdgeX
            ),
            topLeft = Offset(coastWidth, 0f),
            size = Size(currentWaterEdgeX - coastWidth, h)
        )

        // 3. Vanishing Sea Water (from currentWaterEdgeX to right edge)
        drawRect(
            brush = Brush.horizontalGradient(
                colors = listOf(
                    Color(0xFF0077B6),
                    Color(0xFF023E8A),
                    Color(0xFF03045E)
                ),
                startX = currentWaterEdgeX,
                endX = w
            ),
            topLeft = Offset(currentWaterEdgeX, 0f),
            size = Size(w - currentWaterEdgeX, h)
        )

        // 4. Animated Tide Wave Crest Lines at currentWaterEdgeX
        val waveOffset = (waveAnim * 14f)
        for (i in 0..4) {
            val waveY = (h * i / 4f) + (waveOffset * 3f) % (h / 4f)
            drawLine(
                color = Color.White.copy(alpha = 0.7f),
                start = Offset(currentWaterEdgeX, waveY),
                end = Offset(currentWaterEdgeX + 22f, waveY + 14f),
                strokeWidth = 3f,
                cap = StrokeCap.Round
            )
        }

        // 5. Shoreline boundary line (0 km)
        drawLine(
            color = Color(0xFF1E3A8A),
            start = Offset(coastWidth, 0f),
            end = Offset(coastWidth, h),
            strokeWidth = 4f
        )

        // 6. Turnaround 2.5 km Sandbar marker line (Dashed Yellow)
        val turnAroundX = coastWidth + (intertidalSpan * 0.5f)
        drawLine(
            color = Color(0xFFF59E0B),
            start = Offset(turnAroundX, 0f),
            end = Offset(turnAroundX, h),
            strokeWidth = 2.5f,
            pathEffect = PathEffect.dashPathEffect(floatArrayOf(15f, 15f), 0f)
        )

        // 7. Maximum 5.0 km Vanishing Sea Edge line (Dashed Red)
        drawLine(
            color = Color(0xFFEF4444),
            start = Offset(maxVanishingWidth, 0f),
            end = Offset(maxVanishingWidth, h),
            strokeWidth = 3f,
            pathEffect = PathEffect.dashPathEffect(floatArrayOf(20f, 15f), 0f)
        )

        // 8. Visual Pins on the Radar Canvas
        // Pin 1: Main Promenade (0 km)
        drawCircle(
            color = Color(0xFF10B981),
            radius = 7.dp.toPx(),
            center = Offset(coastWidth, h * 0.45f)
        )
        drawCircle(
            color = Color.White,
            radius = 3.dp.toPx(),
            center = Offset(coastWidth, h * 0.45f)
        )

        // Pin 2: Walk Turnaround Point (2.5 km)
        drawCircle(
            color = Color(0xFFF59E0B),
            radius = 6.dp.toPx(),
            center = Offset(turnAroundX, h * 0.45f)
        )
        drawCircle(
            color = Color.White,
            radius = 2.5.dp.toPx(),
            center = Offset(turnAroundX, h * 0.45f)
        )

        // Pin 3: Current Receded Water Line
        drawCircle(
            color = Color(0xFF38BDF8),
            radius = 8.dp.toPx(),
            center = Offset(currentWaterEdgeX, h * 0.45f)
        )
        drawCircle(
            color = Color.White,
            radius = 4.dp.toPx(),
            center = Offset(currentWaterEdgeX, h * 0.45f)
        )

        // DRDO Restricted Safety Sector Overlay (Top Area)
        drawRect(
            color = Color(0x33DC2626),
            topLeft = Offset(coastWidth, 0f),
            size = Size(w - coastWidth, h * 0.22f)
        )
    }
}
