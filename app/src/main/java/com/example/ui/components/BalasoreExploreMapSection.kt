package com.example.ui.components

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.DirectionsWalk
import androidx.compose.material.icons.automirrored.filled.OpenInNew
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AltRoute
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Directions
import androidx.compose.material.icons.filled.DirectionsBus
import androidx.compose.material.icons.filled.DirectionsTransit
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Fullscreen
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Layers
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.Navigation
import androidx.compose.material.icons.filled.NearMe
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Train
import androidx.compose.material.icons.filled.ZoomIn
import androidx.compose.material.icons.filled.ZoomOut
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.example.data.model.AppLanguage
import com.example.data.model.BalasoreTransitDataSource
import com.example.data.model.BusRouteOverlay
import com.example.data.model.BusStopPoint
import com.example.data.model.RailwayStationNode
import com.example.data.model.TrainStatusMarker
import com.example.ui.theme.AmberGold
import com.example.ui.theme.BentoSlate100
import com.example.ui.theme.BentoSlate200
import com.example.ui.theme.BentoSlate600
import com.example.ui.theme.BentoSlate700
import com.example.ui.theme.BentoSlate800
import com.example.ui.theme.BentoSlate900
import com.example.ui.theme.EmeraldGreen
import com.example.ui.theme.OceanBlue
import com.example.ui.theme.OceanBlueDark
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.Polyline
import com.google.android.gms.maps.model.PolylineOptions
import com.google.android.gms.maps.model.TileOverlayOptions
import com.example.BuildConfig
import com.example.data.maps.BalasoreOfflineTileProvider
import java.util.Locale

/**
 * Data structure representing a transit reference origin for distance calculations.
 */
data class TransitOrigin(
    val id: String,
    val nameEn: String,
    val nameOr: String,
    val latitude: Double,
    val longitude: Double,
    val iconEmoji: String
)

val BALASORE_TRANSIT_ORIGINS = listOf(
    TransitOrigin("station", "Balasore Rly Station", "ବାଲେଶ୍ୱର ରେଳ ଷ୍ଟେସନ", 21.4934, 86.9135, "🚉"),
    TransitOrigin("sahadevkhunta", "Sahadevkhunta Bus Stand", "ସହଦେବଖୁଣ୍ଟା ବସ୍ ଷ୍ଟାଣ୍ଡ", 21.4880, 86.9320, "🚌"),
    TransitOrigin("remuna_golei", "Remuna Golei NH-16", "ରେମୁଣା ଗୋଲେଇ ଛକ", 21.5180, 86.8920, "🚩"),
    TransitOrigin("chandipur", "Chandipur Sea Beach", "ଚାନ୍ଦିପୁର ବେଳାଭୂମି", 21.4695, 87.0185, "🏖️")
)

/**
 * Interactive 'Explore' section using Google Maps API that:
 *  - Plots local tourism hotspots, sacred temples, and historic landmarks with clickable markers.
 *  - Overlays local bus routes (Mo Bus 101, 102, 103, OSRTC) with interactive polylines and stops.
 *  - Overlays railway corridor and live train status markers along the Howrah-Bhubaneswar mainline.
 *  - Allows users to tap on routes and train markers for detailed schedules, fares, and arrival timings.
 */
@Composable
fun BalasoreExploreMapSection(
    language: AppLanguage = AppLanguage.ENGLISH,
    modifier: Modifier = Modifier,
    onOpenFullScreenMap: () -> Unit = {},
    onAddToItinerary: ((TouristLocationItem) -> Unit)? = null
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    // Explore Layer Modes:
    // "ALL_OVERLAYS": Hotspots + Bus Routes + Trains
    // "BUS_ROUTES": Focus on Bus Routes and Bus Stops
    // "TRAIN_STATUS": Focus on Railway line and Live Trains
    // "HOTSPOTS": Focus on Temples, Beaches, Landmarks
    var activeExploreMode by remember { mutableStateOf("ALL_OVERLAYS") }

    // Sub-category filter for Hotspots mode: "ALL", "TEMPLE", "BEACH", "LANDMARK", "NATURE"
    var selectedHotspotCategory by remember { mutableStateOf("ALL") }

    // Origin selector for live distance calculations
    var selectedOrigin by remember { mutableStateOf(BALASORE_TRANSIT_ORIGINS.first()) }

    // Selected Route / Train / Hotspot for detailed sheet
    var selectedBusRoute by remember { mutableStateOf<BusRouteOverlay?>(null) }
    var selectedTrain by remember { mutableStateOf<TrainStatusMarker?>(null) }
    var selectedStation by remember { mutableStateOf<RailwayStationNode?>(null) }
    var selectedHotspot by remember {
        mutableStateOf<TouristLocationItem?>(BALASORE_TOURIST_LOCATIONS.firstOrNull())
    }

    // Modal dialog for complete bus schedule timetable
    var showFullBusScheduleDialog by remember { mutableStateOf<BusRouteOverlay?>(null) }

    // Google Maps references
    var googleMapInstance by remember { mutableStateOf<GoogleMap?>(null) }
    var currentMapType by remember { mutableIntStateOf(GoogleMap.MAP_TYPE_NORMAL) }

    val hotspotMarkersMap = remember { mutableMapOf<String, Marker>() }
    val busStopMarkersMap = remember { mutableMapOf<String, Marker>() }
    val trainMarkersMap = remember { mutableMapOf<String, Marker>() }
    val stationMarkersMap = remember { mutableMapOf<String, Marker>() }
    val busPolylinesMap = remember { mutableMapOf<String, Polyline>() }
    var railPolylineInstance by remember { mutableStateOf<Polyline?>(null) }

    // Filtered list of hotspots
    val filteredHotspots = remember(selectedHotspotCategory) {
        if (selectedHotspotCategory == "ALL") {
            BALASORE_TOURIST_LOCATIONS
        } else {
            BALASORE_TOURIST_LOCATIONS.filter { it.categoryGroup == selectedHotspotCategory }
        }
    }

    // Map Engine configuration: Default to 100% offline district vector map
    var useVectorEngine by remember { mutableStateOf(true) }
    val isMapsKeyConfigured = remember {
        BuildConfig.MAPS_API_KEY.isNotBlank()
    }

    // Lazy MapView instance with lifecycle: Only initialized if user explicitly switches to Google Maps satellite
    val mapView = remember(useVectorEngine) {
        if (!useVectorEngine && isMapsKeyConfigured) {
            MapView(context).apply {
                onCreate(Bundle())
            }
        } else {
            null
        }
    }

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

    // Core function to refresh all map overlays based on active mode & selections
    fun refreshMapOverlays(map: GoogleMap) {
        map.clear()
        hotspotMarkersMap.clear()
        busStopMarkersMap.clear()
        trainMarkersMap.clear()
        stationMarkersMap.clear()
        busPolylinesMap.clear()
        railPolylineInstance = null

        val showHotspots = activeExploreMode == "ALL_OVERLAYS" || activeExploreMode == "HOTSPOTS"
        val showBusRoutes = activeExploreMode == "ALL_OVERLAYS" || activeExploreMode == "BUS_ROUTES"
        val showTrains = activeExploreMode == "ALL_OVERLAYS" || activeExploreMode == "TRAIN_STATUS"

        // 0. Base raster tile overlay for seamless rendering without server authorization dependence
        try {
            map.addTileOverlay(
                TileOverlayOptions()
                    .tileProvider(BalasoreOfflineTileProvider(context))
                    .zIndex(1f)
            )
        } catch (_: Exception) {}

        // 1. Draw Railway Corridor & Stations (if Train or All mode)
        if (showTrains) {
            val railOptions = PolylineOptions()
                .addAll(BalasoreTransitDataSource.BALASORE_RAIL_CORRIDOR_POINTS)
                .color(0xFF475569.toInt()) // Slate charcoal for rail line
                .width(9f)
                .zIndex(4f)
                .clickable(true)

            railPolylineInstance = map.addPolyline(railOptions)

            // Railway Stations
            BalasoreTransitDataSource.BALASORE_RAILWAY_STATIONS.forEach { stn ->
                val marker = map.addMarker(
                    MarkerOptions()
                        .position(LatLng(stn.lat, stn.lng))
                        .title("${stn.nameEn} (${stn.stationCode})")
                        .snippet("${stn.platformsCount} Platforms • ${stn.upcomingTrainsCount} Trains")
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
                )
                if (marker != null) {
                    stationMarkersMap[stn.id] = marker
                }
            }

            // Live Train Status Markers
            BalasoreTransitDataSource.LIVE_TRAIN_STATUSES.forEach { train ->
                val marker = map.addMarker(
                    MarkerOptions()
                        .position(LatLng(train.currentLat, train.currentLng))
                        .title("🚆 ${train.trainNumber} ${train.trainNameEn}")
                        .snippet("${train.statusText} • Speed: ${train.speedKmh} km/h")
                        .icon(
                            BitmapDescriptorFactory.defaultMarker(
                                if (train.statusType == "DELAYED") BitmapDescriptorFactory.HUE_YELLOW
                                else BitmapDescriptorFactory.HUE_ROSE
                            )
                        )
                )
                if (marker != null) {
                    trainMarkersMap[train.id] = marker
                }
            }
        }

        // 2. Draw Local Bus Routes & Stops (if Bus or All mode)
        if (showBusRoutes) {
            BalasoreTransitDataSource.BALASORE_BUS_ROUTES.forEach { route ->
                val isSelected = selectedBusRoute?.id == route.id
                val polylineOptions = PolylineOptions()
                    .addAll(route.pathPoints)
                    .color(if (isSelected) 0xFF0284C7.toInt() else route.colorArgb)
                    .width(if (isSelected) 14f else 8f)
                    .zIndex(if (isSelected) 12f else 6f)
                    .clickable(true)

                val polyline = map.addPolyline(polylineOptions)
                busPolylinesMap[route.id] = polyline

                // If this route is selected or in Bus mode, add key stop markers
                if (isSelected || activeExploreMode == "BUS_ROUTES") {
                    route.stops.filter { it.isTransferHub || isSelected }.forEach { stop ->
                        val stopMarker = map.addMarker(
                            MarkerOptions()
                                .position(LatLng(stop.lat, stop.lng))
                                .title("🚏 ${stop.nameEn} (${route.routeNumber})")
                                .snippet("+${stop.scheduledDepartureOffsetMins}m from origin • ${stop.landmarkInfo}")
                                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN))
                        )
                        if (stopMarker != null) {
                            busStopMarkersMap[stop.id] = stopMarker
                        }
                    }
                }
            }
        }

        // 3. Draw Tourism Hotspots, Temples & Landmarks (if Hotspot or All mode)
        if (showHotspots) {
            filteredHotspots.forEach { spot ->
                val markerHue = when (spot.categoryGroup) {
                    "TEMPLE" -> BitmapDescriptorFactory.HUE_ORANGE
                    "BEACH" -> BitmapDescriptorFactory.HUE_AZURE
                    "LANDMARK" -> BitmapDescriptorFactory.HUE_VIOLET
                    "NATURE" -> BitmapDescriptorFactory.HUE_GREEN
                    else -> BitmapDescriptorFactory.HUE_YELLOW
                }

                val marker = map.addMarker(
                    MarkerOptions()
                        .position(LatLng(spot.latitude, spot.longitude))
                        .title(if (language == AppLanguage.ODIA) spot.nameOr else spot.nameEn)
                        .snippet("${spot.categoryEn} • ${spot.nearestTransitStop}")
                        .icon(BitmapDescriptorFactory.defaultMarker(markerHue))
                )
                if (marker != null) {
                    hotspotMarkersMap[spot.id] = marker
                }
            }
        }
    }

    // Reactively refresh map whenever active mode, selections or filters change
    LaunchedEffect(
        activeExploreMode,
        selectedHotspotCategory,
        selectedBusRoute,
        selectedTrain,
        googleMapInstance
    ) {
        googleMapInstance?.let { map ->
            refreshMapOverlays(map)
        }
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
            .testTag("balasore_explore_map_section"),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, BentoSlate200),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp, bottom = 14.dp)
        ) {
            // 1. Header with Title & Fullscreen Button
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    Surface(
                        shape = CircleShape,
                        color = OceanBlue.copy(alpha = 0.12f),
                        modifier = Modifier.size(42.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Default.Explore,
                                contentDescription = "Explore",
                                tint = OceanBlue,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = if (language == AppLanguage.ODIA) "ବାଲେଶ୍ୱର ଏକ୍ସପ୍ଲୋରର୍ ମ୍ୟାପ୍" else "Balasore Transit & Explore",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.ExtraBold,
                                    color = BentoSlate900
                                )
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = Color(0xFFE0F2FE)
                            ) {
                                Text(
                                    text = "Live Maps",
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF0369A1),
                                    modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp)
                                )
                            }
                        }
                        Text(
                            text = if (language == AppLanguage.ODIA)
                                "ବସ୍ ରୁଟ୍, ଟ୍ରେନ୍ ସ୍ଥିତି ଓ ପର୍ଯ୍ୟଟନ ସ୍ଥଳୀର ଇଣ୍ଟରାକ୍ଟିଭ୍ ମ୍ୟାପ୍"
                            else
                                "Tap routes & train markers for live schedules & timetable",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = BentoSlate600,
                                fontSize = 11.5.sp
                            ),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }

                // Full screen button
                IconButton(
                    onClick = onOpenFullScreenMap,
                    modifier = Modifier
                        .size(36.dp)
                        .background(BentoSlate100, CircleShape)
                        .testTag("explore_section_fullscreen_btn")
                ) {
                    Icon(
                        imageVector = Icons.Default.Fullscreen,
                        contentDescription = "Full Screen Map",
                        tint = BentoSlate700,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // 2. Primary Explore Mode Segmented Bar (All Overlays, Bus Routes, Train Status, Places)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                val modes = listOf(
                    Triple("ALL_OVERLAYS", if (language == AppLanguage.ODIA) "ସବୁ (ବସ୍, ଟ୍ରେନ୍ ଓ ସ୍ଥଳୀ)" else "All Overlays", "🗺️"),
                    Triple("BUS_ROUTES", if (language == AppLanguage.ODIA) "ବସ୍ ରୁଟ୍ (${BalasoreTransitDataSource.BALASORE_BUS_ROUTES.size})" else "Bus Routes (${BalasoreTransitDataSource.BALASORE_BUS_ROUTES.size})", "🚌"),
                    Triple("TRAIN_STATUS", if (language == AppLanguage.ODIA) "ଲାଇଭ୍ ଟ୍ରେନ୍ (${BalasoreTransitDataSource.LIVE_TRAIN_STATUSES.size})" else "Live Trains (${BalasoreTransitDataSource.LIVE_TRAIN_STATUSES.size})", "🚆"),
                    Triple("HOTSPOTS", if (language == AppLanguage.ODIA) "ସ୍ଥଳୀ ଓ ମନ୍ଦିର (${BALASORE_TOURIST_LOCATIONS.size})" else "Hotspots (${BALASORE_TOURIST_LOCATIONS.size})", "🛕")
                )

                modes.forEach { (modeKey, label, emoji) ->
                    val isSelected = activeExploreMode == modeKey
                    FilterChip(
                        selected = isSelected,
                        onClick = {
                            activeExploreMode = modeKey
                            // If switching to bus routes, select first route
                            if (modeKey == "BUS_ROUTES" && selectedBusRoute == null) {
                                selectedBusRoute = BalasoreTransitDataSource.BALASORE_BUS_ROUTES.first()
                                selectedTrain = null
                                selectedHotspot = null
                            } else if (modeKey == "TRAIN_STATUS" && selectedTrain == null) {
                                selectedTrain = BalasoreTransitDataSource.LIVE_TRAIN_STATUSES.first()
                                selectedBusRoute = null
                                selectedHotspot = null
                            }
                        },
                        label = {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(text = emoji, fontSize = 12.sp)
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = label,
                                    fontSize = 11.5.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                                )
                            }
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = when (modeKey) {
                                "BUS_ROUTES" -> Color(0xFF0284C7)
                                "TRAIN_STATUS" -> Color(0xFFDC2626)
                                "HOTSPOTS" -> Color(0xFFD97706)
                                else -> OceanBlue
                            },
                            selectedLabelColor = Color.White,
                            containerColor = BentoSlate100,
                            labelColor = BentoSlate800
                        ),
                        border = FilterChipDefaults.filterChipBorder(
                            enabled = true,
                            selected = isSelected,
                            borderColor = if (isSelected) OceanBlue else BentoSlate200
                        ),
                        shape = RoundedCornerShape(12.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // 3. Quick Route / Category Selector Pills
            if (activeExploreMode == "BUS_ROUTES" || activeExploreMode == "ALL_OVERLAYS") {
                // Bus Route Selector row
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState())
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (language == AppLanguage.ODIA) "ବସ୍ ରୁଟ୍ ବାଛନ୍ତୁ:" else "Tap Route:",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = BentoSlate600
                    )

                    BalasoreTransitDataSource.BALASORE_BUS_ROUTES.forEach { route ->
                        val isSelected = selectedBusRoute?.id == route.id
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = if (isSelected) Color(route.colorArgb) else BentoSlate100,
                            modifier = Modifier
                                .clickable {
                                    selectedBusRoute = route
                                    selectedTrain = null
                                    selectedHotspot = null

                                    // Zoom camera to fit route bounds
                                    val builder = LatLngBounds.Builder()
                                    route.pathPoints.forEach { builder.include(it) }
                                    googleMapInstance?.animateCamera(
                                        CameraUpdateFactory.newLatLngBounds(builder.build(), 70)
                                    )
                                }
                                .padding(vertical = 2.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 5.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "🚌 ${route.routeNumber}",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isSelected) Color.White else BentoSlate800
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "(${route.fareStandard})",
                                    fontSize = 9.5.sp,
                                    color = if (isSelected) Color.White.copy(alpha = 0.9f) else Color(0xFF0369A1),
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }
                    }
                }
            } else if (activeExploreMode == "TRAIN_STATUS") {
                // Train Selector row
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState())
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (language == AppLanguage.ODIA) "ଟ୍ରେନ୍ ବାଛନ୍ତୁ:" else "Live Trains:",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = BentoSlate600
                    )

                    BalasoreTransitDataSource.LIVE_TRAIN_STATUSES.forEach { train ->
                        val isSelected = selectedTrain?.id == train.id
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = if (isSelected) Color(0xFFDC2626) else BentoSlate100,
                            modifier = Modifier
                                .clickable {
                                    selectedTrain = train
                                    selectedBusRoute = null
                                    selectedHotspot = null

                                    googleMapInstance?.animateCamera(
                                        CameraUpdateFactory.newLatLngZoom(
                                            LatLng(train.currentLat, train.currentLng),
                                            13.0f
                                        )
                                    )
                                }
                                .padding(vertical = 2.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 5.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "🚆 ${train.trainNumber}",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isSelected) Color.White else BentoSlate800
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = train.platform,
                                    fontSize = 9.5.sp,
                                    color = if (isSelected) Color.White.copy(alpha = 0.9f) else BentoSlate600
                                )
                            }
                        }
                    }
                }
            } else if (activeExploreMode == "HOTSPOTS") {
                // Hotspot Category Pills
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState())
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    val subCats = listOf(
                        Triple("ALL", "All Spots", "🗺️"),
                        Triple("TEMPLE", "Temples", "🛕"),
                        Triple("BEACH", "Beaches", "🏖️"),
                        Triple("LANDMARK", "Landmarks", "🏛️"),
                        Triple("NATURE", "Eco/Safari", "🌿")
                    )

                    subCats.forEach { (catKey, label, emoji) ->
                        val isSelected = selectedHotspotCategory == catKey
                        FilterChip(
                            selected = isSelected,
                            onClick = { selectedHotspotCategory = catKey },
                            label = { Text("$emoji $label", fontSize = 11.sp) },
                            shape = RoundedCornerShape(10.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // 4. Map Engine Switcher (Offline Vector District Map vs Google Maps SDK)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = BentoSlate100,
                    border = BorderStroke(1.dp, BentoSlate200)
                ) {
                    Row(
                        modifier = Modifier.padding(3.dp),
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = if (useVectorEngine) OceanBlue else Color.Transparent,
                            modifier = Modifier
                                .clickable { useVectorEngine = true }
                                .padding(horizontal = 10.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = if (language == AppLanguage.ODIA) "🗺️ ଭେକ୍ଟର (ଅଫଲାଇନ୍)" else "🗺️ Vector Map (Offline)",
                                fontSize = 11.5.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (useVectorEngine) Color.White else BentoSlate800
                            )
                        }

                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = if (!useVectorEngine) OceanBlue else Color.Transparent,
                            modifier = Modifier
                                .clickable { useVectorEngine = false }
                                .padding(horizontal = 10.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = "🌐 Google Maps",
                                fontSize = 11.5.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (!useVectorEngine) Color.White else BentoSlate800
                            )
                        }
                    }
                }

                if (!isMapsKeyConfigured && !useVectorEngine) {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = Color(0xFFFEF3C7),
                        border = BorderStroke(1.dp, Color(0xFFF59E0B))
                    ) {
                        Text(
                            text = "Key Required",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFB45309),
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // 5. Interactive Map View Container (Supports both Vector Canvas & Google Maps SDK)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(340.dp)
                    .padding(horizontal = 16.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .border(1.dp, BentoSlate200, RoundedCornerShape(20.dp))
                    .testTag("explore_section_map_view_container")
            ) {
                if (useVectorEngine) {
                    // Offline 2D Vector Canvas Map (Zero network/API key requirement, 100% resilient)
                    BalasoreDistrictVectorMap(
                        activeExploreMode = activeExploreMode,
                        selectedHotspotCategory = selectedHotspotCategory,
                        selectedHotspot = selectedHotspot,
                        onSelectHotspot = { spot ->
                            selectedHotspot = spot
                            selectedBusRoute = null
                            selectedTrain = null
                        },
                        selectedBusRoute = selectedBusRoute,
                        onSelectBusRoute = { route ->
                            selectedBusRoute = route
                            selectedTrain = null
                            selectedHotspot = null
                        },
                        selectedTrain = selectedTrain,
                        onSelectTrain = { train ->
                            selectedTrain = train
                            selectedBusRoute = null
                            selectedHotspot = null
                        },
                        selectedStation = selectedStation,
                        onSelectStation = { stn ->
                            selectedStation = stn
                            selectedTrain = BalasoreTransitDataSource.LIVE_TRAIN_STATUSES.firstOrNull { it.platform.isNotEmpty() }
                            selectedBusRoute = null
                            selectedHotspot = null
                        },
                        language = language,
                        modifier = Modifier.fillMaxSize()
                    )
                } else if (mapView != null) {
                    // Embedded Google Maps SDK MapView (Only active when key is configured)
                    AndroidView(
                        factory = { _ ->
                            mapView.apply {
                                getMapAsync { map ->
                                    googleMapInstance = map
                                    map.uiSettings.isZoomControlsEnabled = false
                                    map.uiSettings.isCompassEnabled = true
                                    map.uiSettings.isMapToolbarEnabled = true
                                    map.mapType = currentMapType

                                    // Initial Center on Balasore
                                    val initialCenter = LatLng(21.4934, 86.9135)
                                    map.moveCamera(CameraUpdateFactory.newLatLngZoom(initialCenter, 10.5f))

                                    refreshMapOverlays(map)

                                    // Polyline click listener: Allows users to tap on routes for schedule details!
                                    map.setOnPolylineClickListener { polyline ->
                                        val matchedRoute = BalasoreTransitDataSource.BALASORE_BUS_ROUTES.find { route ->
                                            busPolylinesMap[route.id]?.id == polyline.id
                                        }
                                        if (matchedRoute != null) {
                                            selectedBusRoute = matchedRoute
                                            selectedTrain = null
                                            selectedHotspot = null

                                            // Zoom to route bounds
                                            val builder = LatLngBounds.Builder()
                                            matchedRoute.pathPoints.forEach { builder.include(it) }
                                            map.animateCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), 70))
                                        } else if (railPolylineInstance?.id == polyline.id) {
                                            // Railway line clicked: Focus on Balasore Junction
                                            activeExploreMode = "TRAIN_STATUS"
                                            selectedTrain = BalasoreTransitDataSource.LIVE_TRAIN_STATUSES.first()
                                            selectedBusRoute = null
                                        }
                                    }

                                    // Marker click listener
                                    map.setOnMarkerClickListener { marker ->
                                        // 1. Check if it's a Train Status marker
                                        val matchedTrain = BalasoreTransitDataSource.LIVE_TRAIN_STATUSES.find {
                                            trainMarkersMap[it.id]?.id == marker.id
                                        }
                                        if (matchedTrain != null) {
                                            selectedTrain = matchedTrain
                                            selectedBusRoute = null
                                            selectedHotspot = null
                                            map.animateCamera(
                                                CameraUpdateFactory.newLatLngZoom(
                                                    LatLng(matchedTrain.currentLat, matchedTrain.currentLng),
                                                    13f
                                                )
                                            )
                                            marker.showInfoWindow()
                                            return@setOnMarkerClickListener true
                                        }

                                        // 2. Check if it's a Railway Station
                                        val matchedStation = BalasoreTransitDataSource.BALASORE_RAILWAY_STATIONS.find {
                                            stationMarkersMap[it.id]?.id == marker.id
                                        }
                                        if (matchedStation != null) {
                                            selectedStation = matchedStation
                                            selectedTrain = BalasoreTransitDataSource.LIVE_TRAIN_STATUSES.firstOrNull { it.platform.isNotEmpty() }
                                            selectedBusRoute = null
                                            selectedHotspot = null
                                            marker.showInfoWindow()
                                            return@setOnMarkerClickListener true
                                        }

                                        // 3. Check if it's a Bus Stop
                                        val matchedStop = BalasoreTransitDataSource.BALASORE_BUS_ROUTES.flatMap { it.stops }.find {
                                            busStopMarkersMap[it.id]?.id == marker.id
                                        }
                                        if (matchedStop != null) {
                                            val parentRoute = BalasoreTransitDataSource.BALASORE_BUS_ROUTES.find { route ->
                                                route.stops.any { it.id == matchedStop.id }
                                            }
                                            if (parentRoute != null) {
                                                selectedBusRoute = parentRoute
                                                selectedTrain = null
                                                selectedHotspot = null
                                            }
                                            marker.showInfoWindow()
                                            return@setOnMarkerClickListener true
                                        }

                                        // 4. Check if it's a Tourism Hotspot
                                        val matchedSpot = BALASORE_TOURIST_LOCATIONS.find {
                                            hotspotMarkersMap[it.id]?.id == marker.id
                                        }
                                        if (matchedSpot != null) {
                                            selectedHotspot = matchedSpot
                                            selectedBusRoute = null
                                            selectedTrain = null
                                            map.animateCamera(
                                                CameraUpdateFactory.newLatLngZoom(
                                                    LatLng(matchedSpot.latitude, matchedSpot.longitude),
                                                    13f
                                                )
                                            )
                                            marker.showInfoWindow()
                                            return@setOnMarkerClickListener true
                                        }

                                        false
                                    }
                                }
                            }
                        },
                        modifier = Modifier.fillMaxSize()
                    )

                    // Overlay Controls (Recenter, Layer Switcher, Zoom +/-) for Google Maps
                    Column(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(8.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        // Recenter
                        Surface(
                            shape = CircleShape,
                            color = Color.White.copy(alpha = 0.95f),
                            shadowElevation = 3.dp,
                            modifier = Modifier
                                .size(34.dp)
                                .clickable {
                                    googleMapInstance?.animateCamera(
                                        CameraUpdateFactory.newLatLngZoom(LatLng(21.4934, 86.9135), 11f)
                                    )
                                }
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(imageVector = Icons.Default.MyLocation, contentDescription = "Recenter", tint = OceanBlue, modifier = Modifier.size(18.dp))
                            }
                        }

                        // Map Type Toggle
                        Surface(
                            shape = CircleShape,
                            color = Color.White.copy(alpha = 0.95f),
                            shadowElevation = 3.dp,
                            modifier = Modifier
                                .size(34.dp)
                                .clickable {
                                    currentMapType = if (currentMapType == GoogleMap.MAP_TYPE_NORMAL) GoogleMap.MAP_TYPE_HYBRID else GoogleMap.MAP_TYPE_NORMAL
                                    googleMapInstance?.mapType = currentMapType
                                }
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(imageVector = Icons.Default.Layers, contentDescription = "Layers", tint = BentoSlate800, modifier = Modifier.size(18.dp))
                            }
                        }

                        // Zoom In
                        Surface(
                            shape = CircleShape,
                            color = Color.White.copy(alpha = 0.95f),
                            shadowElevation = 3.dp,
                            modifier = Modifier
                                .size(34.dp)
                                .clickable { googleMapInstance?.animateCamera(CameraUpdateFactory.zoomIn()) }
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(imageVector = Icons.Default.ZoomIn, contentDescription = "Zoom In", tint = BentoSlate800, modifier = Modifier.size(18.dp))
                            }
                        }

                        // Zoom Out
                        Surface(
                            shape = CircleShape,
                            color = Color.White.copy(alpha = 0.95f),
                            shadowElevation = 3.dp,
                            modifier = Modifier
                                .size(34.dp)
                                .clickable { googleMapInstance?.animateCamera(CameraUpdateFactory.zoomOut()) }
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(imageVector = Icons.Default.ZoomOut, contentDescription = "Zoom Out", tint = BentoSlate800, modifier = Modifier.size(18.dp))
                            }
                        }
                    }

                    // Key Setup Guidance Banner on Google Maps if key not configured
                    if (!isMapsKeyConfigured) {
                        Surface(
                            modifier = Modifier
                                .align(Alignment.TopCenter)
                                .fillMaxWidth()
                                .padding(8.dp),
                            shape = RoundedCornerShape(10.dp),
                            color = Color(0xFFFEF3C7).copy(alpha = 0.96f),
                            border = BorderStroke(1.dp, Color(0xFFF59E0B)),
                            shadowElevation = 3.dp
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Info,
                                    contentDescription = null,
                                    tint = Color(0xFFB45309),
                                    modifier = Modifier.size(15.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "Google Maps API Key required for cloud tiles. Switch to Vector Map anytime.",
                                    fontSize = 10.sp,
                                    color = Color(0xFF92400E),
                                    modifier = Modifier.weight(1f)
                                )
                                TextButton(
                                    onClick = { useVectorEngine = true },
                                    contentPadding = PaddingValues(horizontal = 6.dp, vertical = 2.dp)
                                ) {
                                    Text(
                                        text = "Use Vector",
                                        fontSize = 10.5.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFFB45309)
                                    )
                                }
                            }
                        }
                    }
                }

                // Bottom Left Badge: Hint for tapping routes
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = Color(0xDD0F172A),
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(8.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = when (activeExploreMode) {
                                "BUS_ROUTES" -> "🚌 Tap any bus line for schedule"
                                "TRAIN_STATUS" -> "🚆 Tap train markers for live status"
                                "HOTSPOTS" -> "🛕 Tap markers for distance & transit"
                                else -> "🗺️ Tap bus lines & train pins"
                            },
                            fontSize = 10.5.sp,
                            color = Color.White,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // 5. Dynamic Interactive Dossier Card: Shows Bus Schedule OR Train Status OR Hotspot Info
            if (selectedBusRoute != null) {
                // === BUS ROUTE SCHEDULE & TIMETABLE CARD ===
                val route = selectedBusRoute!!
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .testTag("bus_route_schedule_card"),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFF0F9FF)),
                    border = BorderStroke(1.5.dp, Color(0xFFBAE6FD)),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp)
                    ) {
                        // Title + Deselect Button
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.Top
                        ) {
                            Row(
                                modifier = Modifier.weight(1f),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Surface(
                                    shape = CircleShape,
                                    color = Color(route.colorArgb),
                                    modifier = Modifier.size(38.dp)
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Icon(imageVector = Icons.Default.DirectionsBus, contentDescription = null, tint = Color.White, modifier = Modifier.size(20.dp))
                                    }
                                }
                                Spacer(modifier = Modifier.width(10.dp))
                                Column {
                                    Text(
                                        text = if (language == AppLanguage.ODIA) route.routeNameOr else route.routeNameEn,
                                        style = MaterialTheme.typography.titleSmall.copy(
                                            fontWeight = FontWeight.ExtraBold,
                                            color = BentoSlate900,
                                            fontSize = 14.sp
                                        )
                                    )
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Surface(
                                            shape = RoundedCornerShape(6.dp),
                                            color = Color(0xFFDCFCE7)
                                        ) {
                                            Text(
                                                text = "🟢 Active • On Time",
                                                fontSize = 10.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = Color(0xFF166534),
                                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                            )
                                        }
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(
                                            text = route.operator,
                                            fontSize = 10.5.sp,
                                            color = BentoSlate600
                                        )
                                    }
                                }
                            }

                            // Dismiss route selection button
                            IconButton(
                                onClick = { selectedBusRoute = null },
                                modifier = Modifier.size(28.dp)
                            ) {
                                Icon(imageVector = Icons.Default.Close, contentDescription = "Close", tint = BentoSlate600, modifier = Modifier.size(18.dp))
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // Schedule & Distance Bento Row
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            // Distance & Time
                            Surface(
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(12.dp),
                                color = Color.White,
                                border = BorderStroke(1.dp, BentoSlate200)
                            ) {
                                Column(modifier = Modifier.padding(8.dp)) {
                                    Text(text = "📏 Distance & Time", fontSize = 9.5.sp, color = BentoSlate600)
                                    Text(text = "${route.distanceKm} km • ~${route.durationMins}m", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = BentoSlate900)
                                }
                            }

                            // Fare
                            Surface(
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(12.dp),
                                color = Color.White,
                                border = BorderStroke(1.dp, BentoSlate200)
                            ) {
                                Column(modifier = Modifier.padding(8.dp)) {
                                    Text(text = "💳 Fare (Std / AC)", fontSize = 9.5.sp, color = BentoSlate600)
                                    Text(text = "${route.fareStandard} / ${route.fareAc}", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = EmeraldGreen)
                                }
                            }

                            // Frequency
                            Surface(
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(12.dp),
                                color = Color.White,
                                border = BorderStroke(1.dp, BentoSlate200)
                            ) {
                                Column(modifier = Modifier.padding(8.dp)) {
                                    Text(text = "🕒 Frequency", fontSize = 9.5.sp, color = BentoSlate600)
                                    Text(text = "Every 20m", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0369A1))
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // Upcoming Scheduled Departures Strip
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = Color.White,
                            border = BorderStroke(1.dp, BentoSlate200),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(10.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(imageVector = Icons.Default.Schedule, contentDescription = null, tint = Color(0xFF0284C7), modifier = Modifier.size(15.dp))
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(
                                            text = if (language == AppLanguage.ODIA) "ଦୈନିକ ବସ୍ ଚଳାଚଳ ସମୟସୂଚୀ:" else "Daily Bus Departures:",
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = BentoSlate800
                                        )
                                    }
                                    Text(
                                        text = "${route.operatingHours}",
                                        fontSize = 10.sp,
                                        color = BentoSlate600
                                    )
                                }

                                Spacer(modifier = Modifier.height(6.dp))

                                // Horizontal departure chips
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .horizontalScroll(rememberScrollState()),
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    route.departuresSummary.take(8).forEachIndexed { idx, timeStr ->
                                        Surface(
                                            shape = RoundedCornerShape(8.dp),
                                            color = if (idx == 0) Color(0xFF0284C7) else BentoSlate100
                                        ) {
                                            Text(
                                                text = if (idx == 0) "Next: $timeStr" else timeStr,
                                                fontSize = 10.5.sp,
                                                fontWeight = if (idx == 0) FontWeight.Bold else FontWeight.Medium,
                                                color = if (idx == 0) Color.White else BentoSlate800,
                                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                            )
                                        }
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // Major Stops Timeline Preview
                        Text(
                            text = if (language == AppLanguage.ODIA) "ରୁଟ୍ ଷ୍ଟପ୍ ସମୟ ତାଲିକା:" else "Route Stops & Timings (${route.stops.size} stops):",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = BentoSlate700
                        )
                        Spacer(modifier = Modifier.height(4.dp))

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .horizontalScroll(rememberScrollState()),
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            route.stops.forEachIndexed { index, stop ->
                                Surface(
                                    shape = RoundedCornerShape(10.dp),
                                    color = Color.White,
                                    border = BorderStroke(1.dp, BentoSlate200),
                                    modifier = Modifier.width(135.dp)
                                ) {
                                    Column(modifier = Modifier.padding(8.dp)) {
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween
                                        ) {
                                            Text(
                                                text = "Stop ${index + 1}",
                                                fontSize = 9.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = Color(0xFF0284C7)
                                            )
                                            Text(
                                                text = "+${stop.scheduledDepartureOffsetMins}m",
                                                fontSize = 9.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = EmeraldGreen
                                            )
                                        }
                                        Spacer(modifier = Modifier.height(2.dp))
                                        Text(
                                            text = if (language == AppLanguage.ODIA) stop.nameOr else stop.nameEn,
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.SemiBold,
                                            color = BentoSlate900,
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis
                                        )
                                        Text(
                                            text = stop.landmarkInfo,
                                            fontSize = 9.sp,
                                            color = BentoSlate600,
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis
                                        )
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Action Buttons: View Complete Timetable + Directions
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Button(
                                onClick = { showFullBusScheduleDialog = route },
                                modifier = Modifier
                                    .weight(1f)
                                    .height(38.dp)
                                    .testTag("view_full_bus_schedule_btn"),
                                colors = ButtonDefaults.buttonColors(containerColor = Color(route.colorArgb)),
                                shape = RoundedCornerShape(10.dp),
                                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp)
                            ) {
                                Icon(imageVector = Icons.Default.Schedule, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = if (language == AppLanguage.ODIA) "ସମ୍ପୂର୍ଣ୍ଣ ସମୟସାରଣୀ" else "Full Timetable",
                                    fontSize = 11.5.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            }

                            OutlinedButton(
                                onClick = {
                                    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                    val text = buildString {
                                        append("${route.routeNameEn}\n")
                                        append("Distance: ${route.distanceKm} km • Fare: ${route.fareStandard} (AC: ${route.fareAc})\n")
                                        append("Frequency: ${route.frequency}\n")
                                        append("Stops:\n")
                                        route.stops.forEach { s ->
                                            append(" - ${s.nameEn} (+${s.scheduledDepartureOffsetMins} mins)\n")
                                        }
                                    }
                                    clipboard.setPrimaryClip(ClipData.newPlainText("Bus Schedule", text))
                                    Toast.makeText(context, "Bus schedule copied to clipboard!", Toast.LENGTH_SHORT).show()
                                },
                                modifier = Modifier
                                    .height(38.dp)
                                    .testTag("copy_bus_schedule_btn"),
                                shape = RoundedCornerShape(10.dp),
                                border = BorderStroke(1.dp, BentoSlate200),
                                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp)
                            ) {
                                Icon(imageVector = Icons.Default.ContentCopy, contentDescription = null, tint = BentoSlate700, modifier = Modifier.size(15.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(text = "Copy", fontSize = 11.5.sp, color = BentoSlate800)
                            }
                        }
                    }
                }
            } else if (selectedTrain != null) {
                // === LIVE TRAIN STATUS & DEPARTURE CARD ===
                val train = selectedTrain!!
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .testTag("train_status_schedule_card"),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFFEF2F2)),
                    border = BorderStroke(1.5.dp, Color(0xFFFECACA)),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp)
                    ) {
                        // Title + Deselect
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.Top
                        ) {
                            Row(
                                modifier = Modifier.weight(1f),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Surface(
                                    shape = CircleShape,
                                    color = Color(0xFFDC2626),
                                    modifier = Modifier.size(40.dp)
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Icon(imageVector = Icons.Default.Train, contentDescription = null, tint = Color.White, modifier = Modifier.size(22.dp))
                                    }
                                }
                                Spacer(modifier = Modifier.width(10.dp))
                                Column {
                                    Text(
                                        text = "${train.trainNumber} ${if (language == AppLanguage.ODIA) train.trainNameOr else train.trainNameEn}",
                                        style = MaterialTheme.typography.titleSmall.copy(
                                            fontWeight = FontWeight.ExtraBold,
                                            color = BentoSlate900,
                                            fontSize = 14.sp
                                        )
                                    )
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Surface(
                                            shape = RoundedCornerShape(6.dp),
                                            color = if (train.statusType == "DELAYED") Color(0xFFFEF3C7) else Color(0xFFDCFCE7)
                                        ) {
                                            Text(
                                                text = train.statusText,
                                                fontSize = 10.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = if (train.statusType == "DELAYED") Color(0xFFB45309) else Color(0xFF166534),
                                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                            )
                                        }
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(
                                            text = train.platform,
                                            fontSize = 10.5.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color(0xFFDC2626)
                                        )
                                    }
                                }
                            }

                            IconButton(
                                onClick = { selectedTrain = null },
                                modifier = Modifier.size(28.dp)
                            ) {
                                Icon(imageVector = Icons.Default.Close, contentDescription = "Close", tint = BentoSlate600, modifier = Modifier.size(18.dp))
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // Train Specs Grid
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = Color.White,
                            border = BorderStroke(1.dp, BentoSlate200),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(
                                modifier = Modifier.padding(10.dp),
                                verticalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Column {
                                        Text(text = "Origin ➔ Destination", fontSize = 9.5.sp, color = BentoSlate600)
                                        Text(text = "${train.origin} ➔ ${train.destination}", fontSize = 11.5.sp, fontWeight = FontWeight.Bold, color = BentoSlate900)
                                    }
                                    Column(horizontalAlignment = Alignment.End) {
                                        Text(text = "Balasore Timing", fontSize = 9.5.sp, color = BentoSlate600)
                                        Text(text = train.scheduledTimeAtBalasore, fontSize = 11.5.sp, fontWeight = FontWeight.Bold, color = Color(0xFFDC2626))
                                    }
                                }

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Column {
                                        Text(text = "Next Stop", fontSize = 9.5.sp, color = BentoSlate600)
                                        Text(text = train.nextStopName, fontSize = 11.sp, color = BentoSlate800)
                                    }
                                    Column(horizontalAlignment = Alignment.End) {
                                        Text(text = "Live Speed", fontSize = 9.5.sp, color = BentoSlate600)
                                        Text(text = "${train.speedKmh} km/h", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0369A1))
                                    }
                                }

                                // Coach layout info
                                Text(
                                    text = "Coach Layout: ${train.coachPositionInfo}",
                                    fontSize = 10.sp,
                                    color = BentoSlate600,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // Action Buttons: Copy train status
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Button(
                                onClick = {
                                    val gmmIntentUri = Uri.parse("geo:21.4934,86.9135?q=Balasore+Railway+Station")
                                    context.startActivity(Intent(Intent.ACTION_VIEW, gmmIntentUri))
                                },
                                modifier = Modifier
                                    .weight(1f)
                                    .height(38.dp)
                                    .testTag("navigate_balasore_station_btn"),
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFDC2626)),
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                Icon(imageVector = Icons.Default.Place, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(text = "Station Platform Map", fontSize = 11.5.sp, fontWeight = FontWeight.Bold, color = Color.White)
                            }

                            OutlinedButton(
                                onClick = {
                                    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                    val text = "${train.trainNumber} ${train.trainNameEn}\nStatus: ${train.statusText}\nPlatform: ${train.platform}\nBalasore Time: ${train.scheduledTimeAtBalasore}\nNext Stop: ${train.nextStopName}"
                                    clipboard.setPrimaryClip(ClipData.newPlainText("Train Status", text))
                                    Toast.makeText(context, "Train status copied!", Toast.LENGTH_SHORT).show()
                                },
                                modifier = Modifier.height(38.dp),
                                shape = RoundedCornerShape(10.dp),
                                border = BorderStroke(1.dp, BentoSlate200)
                            ) {
                                Icon(imageVector = Icons.Default.ContentCopy, contentDescription = null, tint = BentoSlate700, modifier = Modifier.size(14.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(text = "Copy", fontSize = 11.sp, color = BentoSlate800)
                            }
                        }
                    }
                }
            } else if (selectedHotspot != null) {
                // === TOURISM HOTSPOT DISTANCE & TRANSIT CARD ===
                val spot = selectedHotspot!!
                val liveDistanceKm = remember(spot, selectedOrigin) {
                    calculateHaversineDistanceKm(
                        selectedOrigin.latitude,
                        selectedOrigin.longitude,
                        spot.latitude,
                        spot.longitude
                    )
                }
                val estimatedMinutes = remember(liveDistanceKm, spot.transitTravelTimeMins) {
                    val roadSpeedKmH = if (liveDistanceKm > 40) 45.0 else 30.0
                    ((liveDistanceKm / roadSpeedKmH) * 60).toInt().coerceAtLeast(8)
                }

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .testTag("explore_marker_transit_info_card"),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFF8FAFC)),
                    border = BorderStroke(1.5.dp, Color(0xFFE2E8F0)),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.Top
                        ) {
                            Row(
                                modifier = Modifier.weight(1f),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Surface(
                                    shape = CircleShape,
                                    color = Color.White,
                                    border = BorderStroke(1.dp, BentoSlate200),
                                    modifier = Modifier.size(40.dp)
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Text(text = spot.highlightEmoji, fontSize = 20.sp)
                                    }
                                }
                                Spacer(modifier = Modifier.width(10.dp))
                                Column {
                                    Text(
                                        text = if (language == AppLanguage.ODIA) spot.nameOr else spot.nameEn,
                                        style = MaterialTheme.typography.titleSmall.copy(
                                            fontWeight = FontWeight.Bold,
                                            color = BentoSlate900,
                                            fontSize = 14.5.sp
                                        ),
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(
                                            text = if (language == AppLanguage.ODIA) spot.categoryOr else spot.categoryEn,
                                            fontSize = 10.5.sp,
                                            color = OceanBlue,
                                            fontWeight = FontWeight.Bold
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(text = "⭐ ${spot.rating}", fontSize = 10.5.sp, color = BentoSlate700)
                                    }
                                }
                            }

                            // Distance Badge
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = OceanBlue
                            ) {
                                Column(
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(
                                        text = String.format(Locale.US, "%.1f km", liveDistanceKm),
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.ExtraBold,
                                        color = Color.White
                                    )
                                    Text(
                                        text = "~$estimatedMinutes mins",
                                        fontSize = 9.5.sp,
                                        color = Color.White.copy(alpha = 0.9f)
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        // Transit Dossier
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = Color.White,
                            border = BorderStroke(1.dp, BentoSlate200),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(10.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(imageVector = Icons.Default.DirectionsBus, contentDescription = null, tint = Color(0xFF0284C7), modifier = Modifier.size(15.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(text = spot.transitRoutes.joinToString(" • "), fontSize = 11.sp, color = BentoSlate800)
                                }
                                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                    Text(text = "🚏 ${spot.nearestTransitStop}", fontSize = 10.sp, color = BentoSlate600)
                                    Text(text = spot.estimatedTransitFare, fontSize = 10.5.sp, fontWeight = FontWeight.Bold, color = EmeraldGreen)
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Button(
                                onClick = {
                                    val gmmIntentUri = Uri.parse("google.navigation:q=${spot.latitude},${spot.longitude}")
                                    val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri).apply { setPackage("com.google.android.apps.maps") }
                                    try {
                                        context.startActivity(mapIntent)
                                    } catch (_: Exception) {
                                        context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("geo:${spot.latitude},${spot.longitude}?q=${Uri.encode(spot.nameEn)}")))
                                    }
                                },
                                modifier = Modifier.weight(1f).height(38.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = OceanBlue),
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                Icon(imageVector = Icons.Default.Navigation, contentDescription = null, modifier = Modifier.size(16.dp), tint = Color.White)
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(text = "Directions in Maps", fontSize = 11.5.sp, fontWeight = FontWeight.Bold, color = Color.White)
                            }

                            OutlinedButton(
                                onClick = {
                                    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                    val text = "${spot.nameEn}\nDistance: ${String.format(Locale.US, "%.1f km", liveDistanceKm)}\nTransit: ${spot.transitRoutes.joinToString(", ")}\nNearest Stop: ${spot.nearestTransitStop}\nFare: ${spot.estimatedTransitFare}"
                                    clipboard.setPrimaryClip(ClipData.newPlainText("Transit Info", text))
                                    Toast.makeText(context, "Transit guide copied!", Toast.LENGTH_SHORT).show()
                                },
                                modifier = Modifier.height(38.dp),
                                shape = RoundedCornerShape(10.dp),
                                border = BorderStroke(1.dp, BentoSlate200)
                            ) {
                                Icon(imageVector = Icons.Default.ContentCopy, contentDescription = null, modifier = Modifier.size(14.dp), tint = BentoSlate700)
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(text = "Copy", fontSize = 11.sp, color = BentoSlate800)
                            }
                        }
                    }
                }
            }
        }
    }

    // Modal Dialog: Full Bus Schedule Timetable with All Daily Departures & Stop Matrix
    if (showFullBusScheduleDialog != null) {
        val route = showFullBusScheduleDialog!!
        AlertDialog(
            onDismissRequest = { showFullBusScheduleDialog = null },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = "🚌", fontSize = 20.sp)
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(
                            text = "${route.routeNumber}: ${route.originName} ⇄ ${route.destinationName}",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, fontSize = 14.5.sp)
                        )
                        Text(
                            text = "${route.operator} • ${route.distanceKm} km • ${route.durationMins} mins",
                            fontSize = 11.sp,
                            color = BentoSlate600
                        )
                    }
                }
            },
            text = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(380.dp)
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // Fare & Frequency
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = Color(0xFFF1F5F9),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(10.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = "Fare: ${route.fareStandard} (AC: ${route.fareAc})", fontSize = 11.5.sp, fontWeight = FontWeight.Bold, color = EmeraldGreen)
                            Text(text = route.frequency, fontSize = 11.sp, color = Color(0xFF0369A1))
                        }
                    }

                    // Daily Departures Matrix
                    Text(text = "🕒 Complete Daily Departures from ${route.originName}:", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = BentoSlate900)
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        items(route.departuresSummary) { depTime ->
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = Color(0xFFE0F2FE)
                            ) {
                                Text(
                                    text = depTime,
                                    fontSize = 10.5.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = Color(0xFF0369A1),
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    // All Stops Chain
                    Text(text = "🚏 All Route Stops & Timing Offsets:", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = BentoSlate900)
                    route.stops.forEachIndexed { idx, stop ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Surface(
                                shape = CircleShape,
                                color = if (idx == 0 || idx == route.stops.lastIndex) Color(route.colorArgb) else BentoSlate200,
                                modifier = Modifier.size(24.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Text(
                                        text = "${idx + 1}",
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (idx == 0 || idx == route.stops.lastIndex) Color.White else BentoSlate800
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = stop.nameEn,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = BentoSlate900
                                )
                                Text(
                                    text = stop.landmarkInfo,
                                    fontSize = 10.sp,
                                    color = BentoSlate600
                                )
                            }
                            Text(
                                text = "+${stop.scheduledDepartureOffsetMins}m",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = EmeraldGreen
                            )
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = { showFullBusScheduleDialog = null },
                    colors = ButtonDefaults.buttonColors(containerColor = OceanBlue)
                ) {
                    Text("Close")
                }
            }
        )
    }
}
