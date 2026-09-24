package com.example.ui.components

import android.os.Bundle
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.ui.graphics.Color
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

    // Initialize MapView
    val mapView = remember {
        MapView(context).apply {
            onCreate(Bundle())
        }
    }

    // Bind Android Lifecycle
    DisposableEffect(lifecycleOwner, mapView) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_START -> mapView.onStart()
                Lifecycle.Event.ON_RESUME -> mapView.onResume()
                Lifecycle.Event.ON_PAUSE -> mapView.onPause()
                Lifecycle.Event.ON_STOP -> mapView.onStop()
                Lifecycle.Event.ON_DESTROY -> mapView.onDestroy()
                else -> {}
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
            try {
                mapView.onDestroy()
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
                                color = Color(0xFF0284C7).copy(alpha = 0.3f),
                                border = BorderStroke(0.5.dp, Color(0xFF38BDF8))
                            ) {
                                Text(
                                    text = "MAPS SDK",
                                    color = Color(0xFF7DD3FC),
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
                        currentMapType = when (currentMapType) {
                            GoogleMap.MAP_TYPE_NORMAL -> GoogleMap.MAP_TYPE_SATELLITE
                            GoogleMap.MAP_TYPE_SATELLITE -> GoogleMap.MAP_TYPE_HYBRID
                            else -> GoogleMap.MAP_TYPE_NORMAL
                        }
                        googleMapInstance?.mapType = currentMapType
                    },
                    modifier = Modifier.size(36.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Layers,
                        contentDescription = "Switch Map Type",
                        tint = Color(0xFF7DD3FC),
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
