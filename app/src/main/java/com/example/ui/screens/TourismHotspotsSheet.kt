package com.example.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.animation.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
import com.example.data.model.RailwayStationNode
import com.example.data.model.TrainStatusMarker
import com.example.ui.components.BALASORE_TOURIST_LOCATIONS
import com.example.ui.components.BALASORE_TRANSIT_ORIGINS
import com.example.ui.components.TouristLocationItem
import com.example.ui.components.calculateHaversineDistanceKm
import com.example.ui.theme.AmberGold
import com.example.ui.theme.BentoSlate100
import com.example.ui.theme.BentoSlate200
import com.example.ui.theme.BentoSlate600
import com.example.ui.theme.BentoSlate700
import com.example.ui.theme.BentoSlate800
import com.example.ui.theme.BentoSlate900
import com.example.ui.theme.EmeraldGreen
import com.example.ui.theme.OceanBlue
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
import com.example.ui.components.BalasoreDistrictVectorMap
import com.example.data.maps.BalasoreOfflineTileProvider
import java.util.Locale

/**
 * Dedicated Full-Featured Tourism Hotspots Sheet & Interactive Transit Map Explorer.
 *
 * Integrates:
 *  - Google Maps API MapView with markers for all Balasore tourist locations, temples, and landmarks
 *  - Interactive local bus route overlays (Mo Bus 101, 102, 103, OSRTC) with clickable polylines for schedules
 *  - Live train status markers and railway corridor overlay along the Howrah-Bhubaneswar line
 *  - Dynamic category & layer filter chips
 *  - Direct turn-by-turn Google Maps navigation intents
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TourismHotspotsSheet(
    language: AppLanguage = AppLanguage.ENGLISH,
    onClose: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    BackHandler { onClose() }

    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    // Active Map Mode: "ALL_OVERLAYS", "BUS_ROUTES", "TRAIN_STATUS", "HOTSPOTS"
    var activeMode by remember { mutableStateOf("ALL_OVERLAYS") }

    // Sub-category filter for Hotspots: "ALL", "TEMPLE", "BEACH", "LANDMARK", "NATURE"
    var selectedCategoryFilter by remember { mutableStateOf("ALL") }

    // Origin selector for live distance calculations
    var selectedOrigin by remember { mutableStateOf(BALASORE_TRANSIT_ORIGINS.first()) }

    var googleMapInstance by remember { mutableStateOf<GoogleMap?>(null) }
    var currentMapType by remember { mutableIntStateOf(GoogleMap.MAP_TYPE_NORMAL) }

    // Selections
    var selectedBusRoute by remember { mutableStateOf<BusRouteOverlay?>(null) }
    var selectedTrain by remember { mutableStateOf<TrainStatusMarker?>(null) }
    var selectedStation by remember { mutableStateOf<RailwayStationNode?>(null) }
    var selectedHotspot by remember {
        mutableStateOf<TouristLocationItem?>(BALASORE_TOURIST_LOCATIONS.firstOrNull())
    }

    var showBusTimetableDialog by remember { mutableStateOf<BusRouteOverlay?>(null) }

    val hotspotMarkersMap = remember { mutableMapOf<String, Marker>() }
    val busStopMarkersMap = remember { mutableMapOf<String, Marker>() }
    val trainMarkersMap = remember { mutableMapOf<String, Marker>() }
    val stationMarkersMap = remember { mutableMapOf<String, Marker>() }
    val busPolylinesMap = remember { mutableMapOf<String, Polyline>() }
    var railPolylineInstance by remember { mutableStateOf<Polyline?>(null) }

    // Filtered list of locations
    val filteredHotspots = remember(selectedCategoryFilter) {
        if (selectedCategoryFilter == "ALL") {
            BALASORE_TOURIST_LOCATIONS
        } else {
            BALASORE_TOURIST_LOCATIONS.filter { it.categoryGroup == selectedCategoryFilter }
        }
    }

    // Default center coordinates: Balasore District (21.4934° N, 86.9135° E)
    val defaultCenter = LatLng(21.4934, 86.9135)

    // Map Engine configuration: Default to 100% offline vector map
    val isMapsKeyConfigured = remember {
        BuildConfig.MAPS_API_KEY.isNotBlank()
    }
    var useVectorEngine by remember { mutableStateOf(true) }

    // Lazy MapView instance: Only created if user explicitly selects Google Maps and key is configured
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

    fun syncMapOverlays(map: GoogleMap) {
        map.clear()
        hotspotMarkersMap.clear()
        busStopMarkersMap.clear()
        trainMarkersMap.clear()
        stationMarkersMap.clear()
        busPolylinesMap.clear()
        railPolylineInstance = null

        val showHotspots = activeMode == "ALL_OVERLAYS" || activeMode == "HOTSPOTS"
        val showBusRoutes = activeMode == "ALL_OVERLAYS" || activeMode == "BUS_ROUTES"
        val showTrains = activeMode == "ALL_OVERLAYS" || activeMode == "TRAIN_STATUS"

        // 0. Base raster tile overlay for seamless rendering without server authorization dependence
        try {
            map.addTileOverlay(
                TileOverlayOptions()
                    .tileProvider(BalasoreOfflineTileProvider(context))
                    .zIndex(1f)
            )
        } catch (_: Exception) {}

        // 1. Railway corridor & live train markers
        if (showTrains) {
            val railOptions = PolylineOptions()
                .addAll(BalasoreTransitDataSource.BALASORE_RAIL_CORRIDOR_POINTS)
                .color(0xFF475569.toInt())
                .width(9f)
                .zIndex(4f)
                .clickable(true)

            railPolylineInstance = map.addPolyline(railOptions)

            BalasoreTransitDataSource.BALASORE_RAILWAY_STATIONS.forEach { stn ->
                val marker = map.addMarker(
                    MarkerOptions()
                        .position(LatLng(stn.lat, stn.lng))
                        .title("${stn.nameEn} (${stn.stationCode})")
                        .snippet("${stn.platformsCount} Platforms • ${stn.upcomingTrainsCount} Trains")
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
                )
                if (marker != null) stationMarkersMap[stn.id] = marker
            }

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
                if (marker != null) trainMarkersMap[train.id] = marker
            }
        }

        // 2. Bus Routes Polylines & Stops
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

                if (isSelected || activeMode == "BUS_ROUTES") {
                    route.stops.filter { it.isTransferHub || isSelected }.forEach { stop ->
                        val stopMarker = map.addMarker(
                            MarkerOptions()
                                .position(LatLng(stop.lat, stop.lng))
                                .title("🚏 ${stop.nameEn} (${route.routeNumber})")
                                .snippet("+${stop.scheduledDepartureOffsetMins}m from origin • ${stop.landmarkInfo}")
                                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN))
                        )
                        if (stopMarker != null) busStopMarkersMap[stop.id] = stopMarker
                    }
                }
            }
        }

        // 3. Tourism Hotspots
        if (showHotspots) {
            filteredHotspots.forEach { spot ->
                val markerHue = when (spot.categoryGroup) {
                    "TEMPLE" -> BitmapDescriptorFactory.HUE_ORANGE
                    "BEACH" -> BitmapDescriptorFactory.HUE_AZURE
                    "LANDMARK" -> BitmapDescriptorFactory.HUE_VIOLET
                    "NATURE" -> BitmapDescriptorFactory.HUE_GREEN
                    else -> BitmapDescriptorFactory.HUE_RED
                }

                val marker = map.addMarker(
                    MarkerOptions()
                        .position(LatLng(spot.latitude, spot.longitude))
                        .title(if (language == AppLanguage.ODIA) spot.nameOr else spot.nameEn)
                        .snippet("${spot.categoryEn} • ${spot.nearestTransitStop}")
                        .icon(BitmapDescriptorFactory.defaultMarker(markerHue))
                )
                if (marker != null) hotspotMarkersMap[spot.id] = marker
            }
        }
    }

    LaunchedEffect(activeMode, selectedCategoryFilter, selectedBusRoute, selectedTrain, googleMapInstance) {
        googleMapInstance?.let { map ->
            syncMapOverlays(map)
        }
    }

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .testTag("tourism_hotspots_sheet"),
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(text = "🗺️", fontSize = 18.sp)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = if (language == AppLanguage.ODIA) "ବାଲେଶ୍ୱର ପର୍ଯ୍ୟଟନ ଓ ଯାତାୟାତ ମ୍ୟାପ୍" else "Balasore Transit & Explore Map",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.ExtraBold,
                                    color = Color(0xFF0F172A)
                                )
                            )
                        }
                        Text(
                            text = if (language == AppLanguage.ODIA)
                                "ବସ୍ ରୁଟ୍, ଟ୍ରେନ୍ ଓ ପର୍ଯ୍ୟଟନ ସ୍ଥଳୀ ଇଣ୍ଟରାକ୍ଟିଭ୍ ମାର୍କର୍"
                            else
                                "Tap bus lines & train pins for live schedule details",
                            fontSize = 11.sp,
                            color = Color(0xFF64748B)
                        )
                    }
                },
                navigationIcon = {
                    IconButton(
                        onClick = onClose,
                        modifier = Modifier.testTag("hotspots_sheet_back_btn")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color(0xFF0F172A)
                        )
                    }
                },
                actions = {
                    // Map Engine Switcher Button (Vector Map vs Google Maps)
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = if (useVectorEngine) OceanBlue.copy(alpha = 0.12f) else BentoSlate100,
                        border = BorderStroke(1.dp, if (useVectorEngine) OceanBlue else BentoSlate200),
                        modifier = Modifier
                            .clickable { useVectorEngine = !useVectorEngine }
                            .padding(end = 4.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = if (useVectorEngine) "🗺️ Vector Map" else "🌐 Google Maps",
                                fontSize = 11.5.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (useVectorEngine) OceanBlue else BentoSlate800
                            )
                        }
                    }

                    // Map Type Toggle (When Google Maps is active)
                    if (!useVectorEngine) {
                        IconButton(
                            onClick = {
                                currentMapType = if (currentMapType == GoogleMap.MAP_TYPE_NORMAL) {
                                    GoogleMap.MAP_TYPE_HYBRID
                                } else {
                                    GoogleMap.MAP_TYPE_NORMAL
                                }
                                googleMapInstance?.mapType = currentMapType
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Layers,
                                contentDescription = "Map Layers",
                                tint = BentoSlate800
                            )
                        }
                    }

                    // Recenter on Balasore button
                    IconButton(
                        onClick = {
                            googleMapInstance?.animateCamera(
                                CameraUpdateFactory.newLatLngZoom(defaultCenter, 10.5f)
                            )
                        },
                        modifier = Modifier.testTag("recenter_map_btn")
                    ) {
                        Icon(
                            imageVector = Icons.Default.MyLocation,
                            contentDescription = "Recenter",
                            tint = OceanBlue
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White
                )
            )
        },
        containerColor = Color(0xFFF8FAFC)
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            if (useVectorEngine) {
                // Offline 2D Vector Canvas Map (Zero network/API key requirement, 100% resilient)
                BalasoreDistrictVectorMap(
                    activeExploreMode = activeMode,
                    selectedHotspotCategory = selectedCategoryFilter,
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
                // 1. Google Maps View
                AndroidView(
                    modifier = Modifier
                        .fillMaxSize()
                        .testTag("tourism_hotspots_google_map"),
                    factory = { _ ->
                        mapView.apply {
                            getMapAsync { map ->
                                googleMapInstance = map
                                map.uiSettings.isZoomControlsEnabled = false
                                map.uiSettings.isCompassEnabled = true
                                map.uiSettings.isMapToolbarEnabled = true
                                map.mapType = currentMapType
                                map.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultCenter, 10.5f))

                                syncMapOverlays(map)

                                // Polyline click listener for bus routes
                                map.setOnPolylineClickListener { polyline ->
                                    val matchedRoute = BalasoreTransitDataSource.BALASORE_BUS_ROUTES.find { route ->
                                        busPolylinesMap[route.id]?.id == polyline.id
                                    }
                                    if (matchedRoute != null) {
                                        selectedBusRoute = matchedRoute
                                        selectedTrain = null
                                        selectedHotspot = null

                                        val builder = LatLngBounds.Builder()
                                        matchedRoute.pathPoints.forEach { builder.include(it) }
                                        map.animateCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), 70))
                                    }
                                }

                                // Marker click listener
                                map.setOnMarkerClickListener { marker ->
                                    val matchedTrain = BalasoreTransitDataSource.LIVE_TRAIN_STATUSES.find {
                                        trainMarkersMap[it.id]?.id == marker.id
                                    }
                                    if (matchedTrain != null) {
                                        selectedTrain = matchedTrain
                                        selectedBusRoute = null
                                        selectedHotspot = null
                                        marker.showInfoWindow()
                                        return@setOnMarkerClickListener true
                                    }

                                    val matchedStop = BalasoreTransitDataSource.BALASORE_BUS_ROUTES.flatMap { it.stops }.find {
                                        busStopMarkersMap[it.id]?.id == marker.id
                                    }
                                    if (matchedStop != null) {
                                        val parent = BalasoreTransitDataSource.BALASORE_BUS_ROUTES.find { route ->
                                            route.stops.any { it.id == matchedStop.id }
                                        }
                                        if (parent != null) {
                                            selectedBusRoute = parent
                                            selectedTrain = null
                                            selectedHotspot = null
                                        }
                                        marker.showInfoWindow()
                                        return@setOnMarkerClickListener true
                                    }

                                    val matchedSpot = BALASORE_TOURIST_LOCATIONS.find {
                                        hotspotMarkersMap[it.id]?.id == marker.id
                                    }
                                    if (matchedSpot != null) {
                                        selectedHotspot = matchedSpot
                                        selectedBusRoute = null
                                        selectedTrain = null
                                        marker.showInfoWindow()
                                        return@setOnMarkerClickListener true
                                    }

                                    false
                                }
                            }
                        }
                    }
                )

                // Key guidance banner if Google Maps API key is not configured
                if (!isMapsKeyConfigured) {
                    Surface(
                        modifier = Modifier
                            .align(Alignment.TopCenter)
                            .fillMaxWidth()
                            .padding(top = 64.dp, start = 12.dp, end = 12.dp),
                        shape = RoundedCornerShape(10.dp),
                        color = Color(0xFFFEF3C7).copy(alpha = 0.96f),
                        border = BorderStroke(1.dp, Color(0xFFF59E0B)),
                        shadowElevation = 3.dp
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Info,
                                contentDescription = null,
                                tint = Color(0xFFB45309),
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Google Maps API Key not set in Secrets. Offline Vector Map is available.",
                                fontSize = 10.5.sp,
                                color = Color(0xFF92400E),
                                modifier = Modifier.weight(1f)
                            )
                            TextButton(
                                onClick = { useVectorEngine = true },
                                contentPadding = PaddingValues(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = "Use Vector",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFFB45309)
                                )
                            }
                        }
                    }
                }
            }

            // 2. Top Segmented Mode Bar (All, Bus Routes, Train Status, Hotspots)
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.TopCenter)
                    .padding(8.dp),
                shape = RoundedCornerShape(16.dp),
                color = Color.White.copy(alpha = 0.95f),
                shadowElevation = 3.dp
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState())
                        .padding(horizontal = 8.dp, vertical = 6.dp),
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val modes = listOf(
                        Triple("ALL_OVERLAYS", "All Overlays", "🗺️"),
                        Triple("BUS_ROUTES", "Bus Routes (4)", "🚌"),
                        Triple("TRAIN_STATUS", "Train Status (5)", "🚆"),
                        Triple("HOTSPOTS", "Places & Temples (20)", "🛕")
                    )

                    modes.forEach { (modeKey, label, emoji) ->
                        val isSelected = activeMode == modeKey
                        FilterChip(
                            selected = isSelected,
                            onClick = {
                                activeMode = modeKey
                                if (modeKey == "BUS_ROUTES" && selectedBusRoute == null) {
                                    selectedBusRoute = BalasoreTransitDataSource.BALASORE_BUS_ROUTES.first()
                                } else if (modeKey == "TRAIN_STATUS" && selectedTrain == null) {
                                    selectedTrain = BalasoreTransitDataSource.LIVE_TRAIN_STATUSES.first()
                                }
                            },
                            label = {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(text = emoji, fontSize = 11.sp)
                                    Spacer(modifier = Modifier.width(3.dp))
                                    Text(text = label, fontSize = 11.sp, fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium)
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
                            shape = RoundedCornerShape(10.dp)
                        )
                    }
                }
            }

            // 3. Floating Zoom Controls
            Column(
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .padding(end = 12.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Surface(
                    shape = CircleShape,
                    color = Color.White.copy(alpha = 0.95f),
                    shadowElevation = 4.dp,
                    modifier = Modifier
                        .size(38.dp)
                        .clickable { googleMapInstance?.animateCamera(CameraUpdateFactory.zoomIn()) }
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(imageVector = Icons.Default.ZoomIn, contentDescription = "Zoom In", tint = BentoSlate800)
                    }
                }

                Surface(
                    shape = CircleShape,
                    color = Color.White.copy(alpha = 0.95f),
                    shadowElevation = 4.dp,
                    modifier = Modifier
                        .size(38.dp)
                        .clickable { googleMapInstance?.animateCamera(CameraUpdateFactory.zoomOut()) }
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(imageVector = Icons.Default.ZoomOut, contentDescription = "Zoom Out", tint = BentoSlate800)
                    }
                }
            }

            // 4. Bottom Interactive Dossier Sheet
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .background(
                        Color.White.copy(alpha = 0.98f),
                        RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
                    )
                    .border(1.dp, BentoSlate200, RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
                    .padding(top = 8.dp, bottom = 16.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(width = 36.dp, height = 4.dp)
                        .clip(CircleShape)
                        .background(BentoSlate200)
                        .align(Alignment.CenterHorizontally)
                )

                Spacer(modifier = Modifier.height(6.dp))

                if (selectedBusRoute != null) {
                    // === BUS ROUTE SCHEDULE DETAILS ===
                    val route = selectedBusRoute!!
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                                Text(text = "🚌", fontSize = 20.sp)
                                Spacer(modifier = Modifier.width(8.dp))
                                Column {
                                    Text(
                                        text = route.routeNameEn,
                                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold, color = BentoSlate900),
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                    Text(
                                        text = "${route.distanceKm} km • Fare: ${route.fareStandard} • Every 20 mins",
                                        fontSize = 11.sp,
                                        color = BentoSlate600
                                    )
                                }
                            }

                            Button(
                                onClick = { showBusTimetableDialog = route },
                                colors = ButtonDefaults.buttonColors(containerColor = Color(route.colorArgb)),
                                shape = RoundedCornerShape(8.dp),
                                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp)
                            ) {
                                Text(text = "Timetable", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.White)
                            }
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        // Stop Pills
                        LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            items(route.stops) { s ->
                                Surface(shape = RoundedCornerShape(8.dp), color = Color(0xFFF1F5F9)) {
                                    Text(
                                        text = "🚏 ${s.nameEn} (+${s.scheduledDepartureOffsetMins}m)",
                                        fontSize = 10.5.sp,
                                        color = BentoSlate800,
                                        modifier = Modifier.padding(horizontal = 7.dp, vertical = 4.dp)
                                    )
                                }
                            }
                        }
                    }
                } else if (selectedTrain != null) {
                    // === LIVE TRAIN STATUS ===
                    val train = selectedTrain!!
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                                Text(text = "🚆", fontSize = 20.sp)
                                Spacer(modifier = Modifier.width(8.dp))
                                Column {
                                    Text(
                                        text = "${train.trainNumber} ${train.trainNameEn}",
                                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold, color = BentoSlate900),
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                    Text(
                                        text = "${train.statusText} • ${train.platform}",
                                        fontSize = 11.sp,
                                        color = if (train.statusType == "DELAYED") Color(0xFFB45309) else Color(0xFF166534),
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }

                            Surface(shape = RoundedCornerShape(8.dp), color = Color(0xFFDC2626)) {
                                Text(
                                    text = "${train.speedKmh} km/h",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "${train.origin} ➔ ${train.destination} • Balasore: ${train.scheduledTimeAtBalasore}",
                            fontSize = 11.sp,
                            color = BentoSlate700
                        )
                    }
                } else if (selectedHotspot != null) {
                    // === HOTSPOT INFO ===
                    val spot = selectedHotspot!!
                    val liveDistanceKm = calculateHaversineDistanceKm(
                        selectedOrigin.latitude,
                        selectedOrigin.longitude,
                        spot.latitude,
                        spot.longitude
                    )
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                                Text(text = spot.highlightEmoji, fontSize = 20.sp)
                                Spacer(modifier = Modifier.width(8.dp))
                                Column {
                                    Text(text = spot.nameEn, style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold, color = BentoSlate900), maxLines = 1)
                                    Text(text = "${spot.categoryEn} • ⭐ ${spot.rating}", fontSize = 10.5.sp, color = OceanBlue)
                                }
                            }
                            Surface(shape = RoundedCornerShape(10.dp), color = OceanBlue) {
                                Text(
                                    text = String.format(Locale.US, "%.1f km", liveDistanceKm),
                                    color = Color.White,
                                    fontSize = 11.5.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(text = "Transit: ${spot.transitRoutes.joinToString(" • ")} • Fare: ${spot.estimatedTransitFare}", fontSize = 10.5.sp, color = BentoSlate700)
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Bottom Quick Selector Row (Bus Routes + Hotspots)
                LazyRow(
                    modifier = Modifier.fillMaxWidth(),
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(BalasoreTransitDataSource.BALASORE_BUS_ROUTES) { route ->
                        val isSelected = selectedBusRoute?.id == route.id
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = if (isSelected) Color(route.colorArgb) else BentoSlate100,
                            modifier = Modifier.clickable {
                                selectedBusRoute = route
                                selectedTrain = null
                                selectedHotspot = null
                            }
                        ) {
                            Text(
                                text = "🚌 ${route.routeNumber}",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isSelected) Color.White else BentoSlate800,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                            )
                        }
                    }

                    items(filteredHotspots) { spot ->
                        val isSelected = selectedHotspot?.id == spot.id
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = if (isSelected) Color(0xFFEFF6FF) else BentoSlate100,
                            modifier = Modifier.clickable {
                                selectedHotspot = spot
                                selectedBusRoute = null
                                selectedTrain = null
                            }
                        ) {
                            Text(
                                text = "${spot.highlightEmoji} ${spot.nameEn.take(16)}",
                                fontSize = 11.sp,
                                color = if (isSelected) OceanBlue else BentoSlate800,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                            )
                        }
                    }
                }
            }
        }
    }

    // Full Schedule Dialog
    if (showBusTimetableDialog != null) {
        val route = showBusTimetableDialog!!
        AlertDialog(
            onDismissRequest = { showBusTimetableDialog = null },
            title = {
                Text(text = "🚌 ${route.routeNumber}: ${route.originName} ⇄ ${route.destinationName}", fontSize = 14.sp, fontWeight = FontWeight.Bold)
            },
            text = {
                Column(modifier = Modifier.fillMaxWidth().height(340.dp).verticalScroll(rememberScrollState())) {
                    Text(text = "Frequency: ${route.frequency} • Fare: ${route.fareStandard} (AC: ${route.fareAc})", fontSize = 11.sp, color = BentoSlate700)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = "Departures from ${route.originName}:", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    Text(text = route.departuresSummary.joinToString(", "), fontSize = 11.sp, color = Color(0xFF0369A1))
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(text = "Stops Sequence:", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    route.stops.forEach { s ->
                        Text(text = "• ${s.nameEn} (+${s.scheduledDepartureOffsetMins}m)", fontSize = 11.sp, color = BentoSlate800)
                    }
                }
            },
            confirmButton = {
                Button(onClick = { showBusTimetableDialog = null }) { Text("Close") }
            }
        )
    }
}
