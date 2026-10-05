package com.example.ui.features.maps

import java.util.Locale
import android.os.Bundle
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AltRoute
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CloudDownload
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Directions
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Emergency
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Layers
import androidx.compose.material.icons.filled.LocalHospital
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Navigation
import androidx.compose.material.icons.filled.NearMe
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material.icons.filled.WifiOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
import com.example.data.maps.BalasoreOfflineMapManager
import com.example.data.maps.BalasoreOfflineTileProvider
import com.example.data.maps.CacheTargetLandmark
import com.example.data.maps.LandmarkCacheStatus
import com.example.data.maps.LandmarkType
import com.example.data.maps.MapCacheManager
import com.example.data.maps.NavPointCategory
import com.example.data.maps.OfflineKeyArea
import com.example.data.maps.OfflineNavPoint
import com.example.data.maps.OfflineNavRoute
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.TextButton
import com.example.data.model.AppLanguage
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.Polyline
import com.google.android.gms.maps.model.PolylineOptions
import com.google.android.gms.maps.model.TileOverlay
import com.google.android.gms.maps.model.TileOverlayOptions
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * Material Design 3 Offline Map Tile & Outage Navigation Sheet.
 *
 * Implements pre-cached offline tile overlays with Google Maps SDK for Balasore's
 * key areas, turn-by-turn emergency routing without network access, and a live
 * Network Outage Simulator.
 */
@Composable
fun BalasoreOfflineMapTileSheet(
    language: AppLanguage = AppLanguage.ENGLISH,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val coroutineScope = rememberCoroutineScope()
    val mapManager = remember { BalasoreOfflineMapManager.getInstance(context) }

    // Reactive State from Manager
    val packStates by mapManager.packStates.collectAsState()
    val isSimulatedOutage by mapManager.isSimulatedOutage.collectAsState()
    val overallDownloading by mapManager.overallDownloading.collectAsState()
    val overallProgress by mapManager.overallProgress.collectAsState()

    val isActuallyOnline = remember(isSimulatedOutage) { mapManager.isDeviceOnline() }

    // Map UI State
    var googleMapInstance by remember { mutableStateOf<GoogleMap?>(null) }
    var currentMapType by remember { mutableIntStateOf(GoogleMap.MAP_TYPE_NORMAL) }
    var tileOverlayInstance by remember { mutableStateOf<TileOverlay?>(null) }
    var routePolylineInstance by remember { mutableStateOf<Polyline?>(null) }
    val poiMarkers = remember { mutableListOf<Marker>() }
    var simulatedNavMarker by remember { mutableStateOf<Marker?>(null) }

    var selectedAreaId by remember { mutableStateOf(mapManager.keyAreas.first().id) }
    var selectedCategory by remember { mutableStateOf<NavPointCategory?>(null) }
    var activeNavRoute by remember { mutableStateOf<OfflineNavRoute?>(null) }
    var selectedPoi by remember { mutableStateOf<OfflineNavPoint?>(null) }
    var isSimulatingDrive by remember { mutableStateOf(false) }
    var showTurnByTurnSteps by remember { mutableStateOf(false) }
    var activeTab by remember { mutableIntStateOf(0) } // 0: Map & Nav, 1: Download Manager, 2: Emergency Directory, 3: Landmark Cache

    // MapCacheManager Service State
    val mapCacheManager = remember { MapCacheManager.getInstance(context) }
    val cacheStatusMap by mapCacheManager.cacheStatusFlow.collectAsState()
    val isPrefetchingAllLandmarks by mapCacheManager.isPrefetching.collectAsState()
    val prefetchLandmarksProgress by mapCacheManager.overallProgress.collectAsState()
    val currentLandmarkTask by mapCacheManager.currentTaskName.collectAsState()
    val cacheSummary = remember(cacheStatusMap, isPrefetchingAllLandmarks, prefetchLandmarksProgress) {
        mapCacheManager.getSummaryStatistics()
    }
    var landmarkFilterType by remember { mutableStateOf<LandmarkType?>(null) }

    // Simulated user position (Defaults to Balasore Central)
    val userCurrentLocation = remember { mutableStateOf(LatLng(21.4934, 86.9324)) }

    val isMapsKeyConfigured = remember {
        com.example.BuildConfig.MAPS_API_KEY.isNotBlank()
    }
    // Default to 100% Offline Vector Tile Provider (Mapbox/Mapsforge style)
    var useOfflineVectorEngine by remember { mutableStateOf(true) }

    // MapView instance: Only created if user explicitly selects Google Maps and key is configured
    val mapView = remember(useOfflineVectorEngine) {
        if (!useOfflineVectorEngine && isMapsKeyConfigured) {
            MapView(context).apply {
                onCreate(Bundle())
            }
        } else {
            null
        }
    }

    // Lifecycle Observer for MapView
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

    // Attach or refresh TileOverlay on the map
    fun configureTileOverlay(map: GoogleMap, isOutage: Boolean) {
        tileOverlayInstance?.remove()
        val provider = BalasoreOfflineTileProvider(context, isEmergencyHighContrast = isOutage)
        val options = TileOverlayOptions()
            .tileProvider(provider)
            .fadeIn(true)
            .zIndex(100f)
        tileOverlayInstance = map.addTileOverlay(options)
    }

    // Refresh markers for current filter
    fun updateMapMarkers(map: GoogleMap, category: NavPointCategory?) {
        poiMarkers.forEach { it.remove() }
        poiMarkers.clear()

        val filtered = if (category == null) {
            mapManager.offlinePois
        } else {
            mapManager.offlinePois.filter { it.category == category }
        }

        for (poi in filtered) {
            val hue = when (poi.category) {
                NavPointCategory.HOSPITAL -> BitmapDescriptorFactory.HUE_RED
                NavPointCategory.POLICE_DEFENSE -> BitmapDescriptorFactory.HUE_AZURE
                NavPointCategory.CYCLONE_SHELTER -> BitmapDescriptorFactory.HUE_ORANGE
                NavPointCategory.TRANSIT -> BitmapDescriptorFactory.HUE_GREEN
                NavPointCategory.FIRE_RESCUE -> BitmapDescriptorFactory.HUE_VIOLET
                NavPointCategory.HERITAGE_TOURISM -> BitmapDescriptorFactory.HUE_YELLOW
            }

            val marker = map.addMarker(
                MarkerOptions()
                    .position(poi.location)
                    .title(if (language == AppLanguage.ODIA) poi.nameOr else poi.nameEn)
                    .snippet("${poi.address} • Tel: ${poi.emergencyPhone}")
                    .icon(BitmapDescriptorFactory.defaultMarker(hue))
            )
            marker?.let {
                it.tag = poi
                poiMarkers.add(it)
            }
        }
    }

    // Draw route on map
    fun drawRouteOnMap(map: GoogleMap, route: OfflineNavRoute) {
        routePolylineInstance?.remove()
        simulatedNavMarker?.remove()

        routePolylineInstance = map.addPolyline(
            PolylineOptions()
                .addAll(route.pathPoints)
                .color(android.graphics.Color.rgb(6, 182, 212)) // Cyan
                .width(10f)
                .geodesic(true)
        )

        simulatedNavMarker = map.addMarker(
            MarkerOptions()
                .position(route.origin)
                .title("Your Offline Vehicle / Position")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN))
        )

        // Animate camera to include route
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(route.destination, 13.5f))
    }

    // Simulated Navigation Loop
    LaunchedEffect(isSimulatingDrive, activeNavRoute) {
        val route = activeNavRoute
        if (isSimulatingDrive && route != null && googleMapInstance != null) {
            val pts = route.pathPoints
            for (i in 0 until pts.size - 1) {
                val start = pts[i]
                val end = pts[i + 1]
                val steps = 15
                for (s in 0..steps) {
                    if (!isSimulatingDrive) break
                    val fraction = s.toFloat() / steps
                    val curLat = start.latitude + (end.latitude - start.latitude) * fraction
                    val curLng = start.longitude + (end.longitude - start.longitude) * fraction
                    val currentPos = LatLng(curLat, curLng)
                    userCurrentLocation.value = currentPos
                    simulatedNavMarker?.position = currentPos
                    googleMapInstance?.animateCamera(CameraUpdateFactory.newLatLng(currentPos))
                    delay(300)
                }
            }
            isSimulatingDrive = false
        }
    }

    // Whenever outage mode flips, update TileOverlay style
    LaunchedEffect(isSimulatedOutage, googleMapInstance) {
        googleMapInstance?.let { map ->
            configureTileOverlay(map, isSimulatedOutage)
        }
    }

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .testTag("balasore_offline_map_tile_sheet"),
        color = Color(0xFF0F172A) // Slate 900
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 14.dp, vertical = 8.dp)
        ) {
            // 1. Header Bar with Outage Simulator Toggle
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        shape = CircleShape,
                        color = if (isSimulatedOutage) Color(0xFFDC2626).copy(alpha = 0.2f) else Color(0xFF059669).copy(alpha = 0.2f),
                        border = BorderStroke(1.dp, if (isSimulatedOutage) Color(0xFFEF4444) else Color(0xFF10B981)),
                        modifier = Modifier.size(38.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = if (isSimulatedOutage) Icons.Default.WifiOff else Icons.Default.Map,
                                contentDescription = "Offline Map Icon",
                                tint = if (isSimulatedOutage) Color(0xFFF87171) else Color(0xFF34D399),
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = if (language == AppLanguage.ODIA) "ଅଫଲାଇନ୍ ମ୍ୟାପ୍ ଓ ନାଭିଗେସନ୍" else "Balasore Offline Maps & Tiles",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            )
                        }
                        Text(
                            text = if (isSimulatedOutage) {
                                if (language == AppLanguage.ODIA) "ବିପର୍ଯ୍ୟୟ ମୋଡ୍: ୧୦୦% ଅଫଲାଇନ୍ କ୍ୟାସ୍ ଟାଇଲ୍" else "Emergency Outage: 100% Pre-cached Tiles"
                            } else {
                                if (language == AppLanguage.ODIA) "ଗୁଗୁଲ୍ ମ୍ୟାପ୍ସ API ପ୍ରି-କ୍ୟାସିଂ ଇଞ୍ଜିନ୍" else "Google Maps API Pre-caching Engine"
                            },
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = if (isSimulatedOutage) Color(0xFFFCA5A5) else Color(0xFF94A3B8),
                                fontSize = 11.sp
                            )
                        )
                    }
                }

                // Close Button
                OutlinedButton(
                    onClick = onDismiss,
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFF94A3B8)),
                    border = BorderStroke(1.dp, Color(0xFF334155)),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
                    modifier = Modifier.height(34.dp)
                ) {
                    Text("Close", fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                }
            }

            // 2. Network Outage Simulator Banner & Quick Toggle
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (isSimulatedOutage) Color(0xFF450A0A) else Color(0xFF1E293B)
                ),
                border = BorderStroke(
                    1.dp,
                    if (isSimulatedOutage) Color(0xFFEF4444) else Color(0xFF334155)
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 6.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        modifier = Modifier.weight(1f),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = if (isSimulatedOutage) Icons.Default.Warning else Icons.Default.Wifi,
                            contentDescription = "Outage Mode",
                            tint = if (isSimulatedOutage) Color(0xFFF87171) else Color(0xFF38BDF8),
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text(
                                text = if (isSimulatedOutage) {
                                    if (language == AppLanguage.ODIA) "ସିମୁଲେଟେଡ୍ ନେଟୱାର୍କ ବିଚ୍ଛିନ୍ନତା (ସମ୍ପୂର୍ଣ୍ଣ ଅଫଲାଇନ୍)" else "Simulated Network Blackout (Outage Active)"
                                } else {
                                    if (language == AppLanguage.ODIA) "ଅଫଲାଇନ୍ ଟାଇଲ୍ ପରୀକ୍ଷା ମୋଡ୍" else "Offline Outage Simulator"
                                },
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = if (isSimulatedOutage) Color(0xFFFEE2E2) else Color.White,
                                    fontSize = 12.5.sp
                                )
                            )
                            Text(
                                text = if (isSimulatedOutage) {
                                    "Serving tiles strictly from device disk. Zero internet required."
                                } else {
                                    "Toggle to test offline navigation during cyclone network cutoffs"
                                },
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = if (isSimulatedOutage) Color(0xFFFCA5A5) else Color(0xFF94A3B8),
                                    fontSize = 10.sp
                                ),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }

                    Switch(
                        checked = isSimulatedOutage,
                        onCheckedChange = { mapManager.toggleSimulatedOutage() },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = Color.White,
                            checkedTrackColor = Color(0xFFDC2626),
                            uncheckedThumbColor = Color(0xFF94A3B8),
                            uncheckedTrackColor = Color(0xFF334155)
                        ),
                        modifier = Modifier.testTag("simulate_network_outage_toggle")
                    )
                }
            }

            // 3. Tab Navigation (Map & Nav / Download Packs / Emergency POIs / Landmark Cache)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                TabButton(
                    title = if (language == AppLanguage.ODIA) "🗺️ ମ୍ୟାପ୍" else "🗺️ Live",
                    selected = activeTab == 0,
                    onClick = { activeTab = 0 },
                    modifier = Modifier.weight(1f)
                )
                TabButton(
                    title = if (language == AppLanguage.ODIA) "📥 ପ୍ୟାକ୍" else "📥 Packs",
                    selected = activeTab == 1,
                    onClick = { activeTab = 1 },
                    badge = "${mapManager.getTotalDownloadedTiles()}",
                    modifier = Modifier.weight(1f)
                )
                TabButton(
                    title = if (language == AppLanguage.ODIA) "🏥 ଜରୁରୀ" else "🏥 POIs",
                    selected = activeTab == 2,
                    onClick = { activeTab = 2 },
                    modifier = Modifier.weight(1f)
                )
                TabButton(
                    title = if (language == AppLanguage.ODIA) "🛡️ ସ୍ମାରକୀ" else "🛡️ Landmarks",
                    selected = activeTab == 3,
                    onClick = { activeTab = 3 },
                    badge = "${cacheSummary.cachedLandmarksCount}/${cacheSummary.totalLandmarksCount}",
                    modifier = Modifier.weight(1.15f)
                )
            }

            // Main Content Area based on Tab
            when (activeTab) {
                0 -> {
                    // TAB 0: Interactive Google Map with Offline TileOverlay & Routing HUD
                    Column(modifier = Modifier.fillMaxSize()) {
                        // Key Areas Quick Selector Chips
                        LazyRow(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 2.dp),
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            items(mapManager.keyAreas) { area ->
                                val isDownloaded = packStates[area.id]?.isDownloaded == true
                                val isSelected = selectedAreaId == area.id
                                FilterChip(
                                    selected = isSelected,
                                    onClick = {
                                        selectedAreaId = area.id
                                        googleMapInstance?.animateCamera(
                                            CameraUpdateFactory.newLatLngZoom(area.center, 13.2f)
                                        )
                                    },
                                    label = {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            if (isDownloaded) {
                                                Icon(
                                                    imageVector = Icons.Default.CheckCircle,
                                                    contentDescription = "Cached",
                                                    tint = Color(0xFF10B981),
                                                    modifier = Modifier.size(12.dp)
                                                )
                                                Spacer(modifier = Modifier.width(4.dp))
                                            }
                                            Text(
                                                text = if (language == AppLanguage.ODIA) area.nameOr else area.nameEn,
                                                fontSize = 11.sp,
                                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                            )
                                        }
                                    },
                                    colors = FilterChipDefaults.filterChipColors(
                                        selectedContainerColor = Color(0xFF0284C7),
                                        selectedLabelColor = Color.White,
                                        containerColor = Color(0xFF1E293B),
                                        labelColor = Color(0xFFCBD5E1)
                                    ),
                                    border = BorderStroke(
                                        1.dp,
                                        if (isSelected) Color(0xFF38BDF8) else Color(0xFF334155)
                                    )
                                )
                            }
                        }

                        // Category Filter for POIs
                        LazyRow(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 4.dp),
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            item {
                                FilterChip(
                                    selected = selectedCategory == null,
                                    onClick = {
                                        selectedCategory = null
                                        googleMapInstance?.let { updateMapMarkers(it, null) }
                                    },
                                    label = { Text("All Safe POIs", fontSize = 10.sp) },
                                    colors = FilterChipDefaults.filterChipColors(
                                        selectedContainerColor = Color(0xFF334155),
                                        selectedLabelColor = Color.White,
                                        containerColor = Color(0xFF1E293B),
                                        labelColor = Color(0xFF94A3B8)
                                    )
                                )
                            }
                            items(NavPointCategory.values()) { cat ->
                                val isSelected = selectedCategory == cat
                                FilterChip(
                                    selected = isSelected,
                                    onClick = {
                                        selectedCategory = if (isSelected) null else cat
                                        googleMapInstance?.let { updateMapMarkers(it, selectedCategory) }
                                    },
                                    label = {
                                        Text(
                                            text = cat.name.replace("_", " ").lowercase().replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() },
                                            fontSize = 10.sp
                                        )
                                    },
                                    colors = FilterChipDefaults.filterChipColors(
                                        selectedContainerColor = Color(0xFF0284C7),
                                        selectedLabelColor = Color.White,
                                        containerColor = Color(0xFF1E293B),
                                        labelColor = Color(0xFF94A3B8)
                                    )
                                )
                            }
                        }

                        // Map Provider Mode Switcher: 100% Offline Vector Tiles vs Google Maps SDK
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(Color(0xFF1E293B))
                                    .padding(2.dp),
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = if (useOfflineVectorEngine) Color(0xFF0284C7) else Color.Transparent,
                                    modifier = Modifier
                                        .clickable { useOfflineVectorEngine = true }
                                        .testTag("btn_tile_provider_offline_vector")
                                ) {
                                    Row(
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Map,
                                            contentDescription = "Vector Tiles",
                                            tint = if (useOfflineVectorEngine) Color.White else Color(0xFF94A3B8),
                                            modifier = Modifier.size(13.dp)
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(
                                            text = if (language == AppLanguage.ODIA) "ଅଫଲାଇନ୍ ଭେକ୍ଟର ଟାଇଲ୍" else "Offline Vector Tiles",
                                            fontSize = 10.5.sp,
                                            fontWeight = if (useOfflineVectorEngine) FontWeight.Bold else FontWeight.Normal,
                                            color = if (useOfflineVectorEngine) Color.White else Color(0xFF94A3B8)
                                        )
                                    }
                                }

                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = if (!useOfflineVectorEngine) Color(0xFF0284C7) else Color.Transparent,
                                    modifier = Modifier
                                        .clickable {
                                            if (isMapsKeyConfigured) {
                                                useOfflineVectorEngine = false
                                            } else {
                                                android.widget.Toast.makeText(
                                                    context,
                                                    if (language == AppLanguage.ODIA)
                                                        "ଗୁଗୁଲ୍ ମ୍ୟାପ୍ସ ଅନୁମୋଦନ ନଥିବାରୁ ଅଫଲାଇନ୍ ଭେକ୍ଟର ଟାଇଲ୍ ବ୍ୟବହାର ହେଉଛି"
                                                    else
                                                        "Google Maps requires authorized key. Running 100% Offline Vector Tiles.",
                                                    android.widget.Toast.LENGTH_LONG
                                                ).show()
                                            }
                                        }
                                        .testTag("btn_tile_provider_google_maps")
                                ) {
                                    Row(
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Layers,
                                            contentDescription = "Google Maps",
                                            tint = if (!useOfflineVectorEngine) Color.White else Color(0xFF94A3B8),
                                            modifier = Modifier.size(13.dp)
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(
                                            text = "Google Maps",
                                            fontSize = 10.5.sp,
                                            fontWeight = if (!useOfflineVectorEngine) FontWeight.Bold else FontWeight.Normal,
                                            color = if (!useOfflineVectorEngine) Color.White else Color(0xFF94A3B8)
                                        )
                                    }
                                }
                            }

                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = if (useOfflineVectorEngine) Color(0xFF10B981).copy(alpha = 0.2f) else Color(0xFF0284C7).copy(alpha = 0.2f)
                            ) {
                                Text(
                                    text = if (useOfflineVectorEngine) "MAPSFORGE VECTOR" else "MAPS SDK",
                                    fontSize = 8.5.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = if (useOfflineVectorEngine) Color(0xFF34D399) else Color(0xFF38BDF8),
                                    modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp)
                                )
                            }
                        }

                        // Map Viewport (Offline Vector Tiles or Google Maps)
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f)
                                .clip(RoundedCornerShape(14.dp))
                                .border(1.dp, Color(0xFF334155), RoundedCornerShape(14.dp))
                                .testTag("offline_map_tile_viewport")
                        ) {
                            val activePois = if (selectedCategory == null) {
                                mapManager.offlinePois
                            } else {
                                mapManager.offlinePois.filter { it.category == selectedCategory }
                            }

                            if (!useOfflineVectorEngine && mapView != null) {
                                AndroidView(
                                    factory = {
                                        mapView.apply {
                                            getMapAsync { map ->
                                                googleMapInstance = map
                                                map.mapType = currentMapType
                                                map.uiSettings.isZoomControlsEnabled = false
                                                map.uiSettings.isCompassEnabled = true
                                                map.uiSettings.isMapToolbarEnabled = false

                                                // Default camera on Balasore Central
                                                val initialPos = LatLng(21.4934, 86.9324)
                                                map.moveCamera(CameraUpdateFactory.newLatLngZoom(initialPos, 13f))

                                                // Configure TileOverlay
                                                configureTileOverlay(map, isSimulatedOutage)

                                                // Populate Markers
                                                updateMapMarkers(map, selectedCategory)

                                                // Marker Click: Set POI and calculate route
                                                map.setOnMarkerClickListener { marker ->
                                                    val poi = marker.tag as? OfflineNavPoint
                                                    if (poi != null) {
                                                        selectedPoi = poi
                                                        val route = mapManager.calculateOfflineRoute(
                                                            origin = userCurrentLocation.value,
                                                            destinationPoi = poi
                                                        )
                                                        activeNavRoute = route
                                                        drawRouteOnMap(map, route)
                                                    }
                                                    false
                                                }
                                            }
                                        }
                                    },
                                    modifier = Modifier.fillMaxSize()
                                )
                            } else {
                                BalasoreOfflineVectorTileCanvas(
                                    userLocation = userCurrentLocation.value,
                                    landmarks = activePois,
                                    selectedPoi = selectedPoi,
                                    onSelectPoi = { poi ->
                                        selectedPoi = poi
                                        val route = mapManager.calculateOfflineRoute(
                                            origin = userCurrentLocation.value,
                                            destinationPoi = poi
                                        )
                                        activeNavRoute = route
                                    },
                                    activeRoute = activeNavRoute,
                                    isOutageMode = isSimulatedOutage,
                                    language = language,
                                    modifier = Modifier.fillMaxSize()
                                )
                            }

                            // Overlaid Top Action Controls (Layer Switcher, Re-center, Tile Refresh)
                            Row(
                                modifier = Modifier
                                    .align(Alignment.TopEnd)
                                    .padding(8.dp),
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                // Layer Switcher
                                Surface(
                                    shape = CircleShape,
                                    color = Color(0xFF0F172A).copy(alpha = 0.85f),
                                    border = BorderStroke(1.dp, Color(0xFF334155)),
                                    modifier = Modifier.size(34.dp)
                                ) {
                                    IconButton(
                                        onClick = {
                                            currentMapType = when (currentMapType) {
                                                GoogleMap.MAP_TYPE_NORMAL -> GoogleMap.MAP_TYPE_SATELLITE
                                                GoogleMap.MAP_TYPE_SATELLITE -> GoogleMap.MAP_TYPE_TERRAIN
                                                else -> GoogleMap.MAP_TYPE_NORMAL
                                            }
                                            googleMapInstance?.mapType = currentMapType
                                        }
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Layers,
                                            contentDescription = "Map Style",
                                            tint = Color(0xFF38BDF8),
                                            modifier = Modifier.size(18.dp)
                                        )
                                    }
                                }

                                // Re-center on Balasore Town
                                Surface(
                                    shape = CircleShape,
                                    color = Color(0xFF0F172A).copy(alpha = 0.85f),
                                    border = BorderStroke(1.dp, Color(0xFF334155)),
                                    modifier = Modifier.size(34.dp)
                                ) {
                                    IconButton(
                                        onClick = {
                                            googleMapInstance?.animateCamera(
                                                CameraUpdateFactory.newLatLngZoom(LatLng(21.4934, 86.9324), 13.5f)
                                            )
                                        }
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.NearMe,
                                            contentDescription = "Center Balasore",
                                            tint = Color(0xFF34D399),
                                            modifier = Modifier.size(18.dp)
                                        )
                                    }
                                }
                            }

                            // Overlaid Tile Cache Watermark Pill
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = Color(0xFF0F172A).copy(alpha = 0.85f),
                                border = BorderStroke(0.5.dp, Color(0xFF0284C7)),
                                modifier = Modifier
                                    .align(Alignment.BottomStart)
                                    .padding(8.dp)
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(6.dp)
                                            .clip(CircleShape)
                                            .background(if (isSimulatedOutage) Color(0xFFEF4444) else Color(0xFF10B981))
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = if (isSimulatedOutage) "OFFLINE CACHE TILE OVERLAY ACTIVE" else "PRE-CACHED TILE OVERLAY READY",
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (isSimulatedOutage) Color(0xFFFCA5A5) else Color(0xFF7DD3FC)
                                    )
                                }
                            }
                        }

                        // Bottom Navigation Card if a Route is active
                        activeNavRoute?.let { route ->
                            Spacer(modifier = Modifier.height(8.dp))
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp),
                                colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B)),
                                border = BorderStroke(1.dp, Color(0xFF0284C7))
                            ) {
                                Column(modifier = Modifier.padding(10.dp)) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Column(modifier = Modifier.weight(1f)) {
                                            Text(
                                                text = "Navigate to ${route.destinationName}",
                                                style = MaterialTheme.typography.bodyMedium.copy(
                                                    fontWeight = FontWeight.Bold,
                                                    color = Color.White
                                                ),
                                                maxLines = 1,
                                                overflow = TextOverflow.Ellipsis
                                            )
                                            Text(
                                                text = "${route.totalDistanceKm.format(1)} km • ~${route.estimatedDurationMin} mins (Safe Route)",
                                                style = MaterialTheme.typography.bodySmall.copy(
                                                    color = Color(0xFF38BDF8),
                                                    fontSize = 11.sp
                                                )
                                            )
                                        }

                                        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                            // Simulate Drive button
                                            Button(
                                                onClick = { isSimulatingDrive = !isSimulatingDrive },
                                                colors = ButtonDefaults.buttonColors(
                                                    containerColor = if (isSimulatingDrive) Color(0xFFDC2626) else Color(0xFF0284C7)
                                                ),
                                                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                                                shape = RoundedCornerShape(8.dp),
                                                modifier = Modifier.height(32.dp)
                                            ) {
                                                Icon(
                                                    imageVector = if (isSimulatingDrive) Icons.Default.Stop else Icons.Default.PlayArrow,
                                                    contentDescription = "Simulate Drive",
                                                    modifier = Modifier.size(16.dp)
                                                )
                                                Spacer(modifier = Modifier.width(4.dp))
                                                Text(
                                                    text = if (isSimulatingDrive) "Stop" else "Simulate",
                                                    fontSize = 11.sp,
                                                    fontWeight = FontWeight.Bold
                                                )
                                            }

                                            // Steps Toggle
                                            IconButton(
                                                onClick = { showTurnByTurnSteps = !showTurnByTurnSteps },
                                                modifier = Modifier.size(32.dp)
                                            ) {
                                                Icon(
                                                    imageVector = if (showTurnByTurnSteps) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                                                    contentDescription = "Toggle Steps",
                                                    tint = Color(0xFF94A3B8)
                                                )
                                            }
                                        }
                                    }

                                    // Expandable Turn-by-Turn Steps
                                    AnimatedVisibility(visible = showTurnByTurnSteps) {
                                        Column(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(top = 8.dp)
                                        ) {
                                            HorizontalDivider(color = Color(0xFF334155))
                                            Spacer(modifier = Modifier.height(6.dp))
                                            route.steps.forEachIndexed { index, step ->
                                                Row(
                                                    modifier = Modifier
                                                        .fillMaxWidth()
                                                        .padding(vertical = 3.dp),
                                                    verticalAlignment = Alignment.CenterVertically
                                                ) {
                                                    Surface(
                                                        shape = CircleShape,
                                                        color = Color(0xFF0284C7).copy(alpha = 0.2f),
                                                        modifier = Modifier.size(20.dp)
                                                    ) {
                                                        Box(contentAlignment = Alignment.Center) {
                                                            Text(
                                                                text = "${index + 1}",
                                                                fontSize = 9.sp,
                                                                fontWeight = FontWeight.Bold,
                                                                color = Color(0xFF38BDF8)
                                                            )
                                                        }
                                                    }
                                                    Spacer(modifier = Modifier.width(8.dp))
                                                    Column(modifier = Modifier.weight(1f)) {
                                                        Text(
                                                            text = if (language == AppLanguage.ODIA) step.instructionOr else step.instructionEn,
                                                            style = MaterialTheme.typography.bodySmall.copy(
                                                                color = Color(0xFFE2E8F0),
                                                                fontSize = 11.sp
                                                            )
                                                        )
                                                        Text(
                                                            text = "${step.roadName} • ${step.distanceMeters} m",
                                                            style = MaterialTheme.typography.bodySmall.copy(
                                                                color = Color(0xFF64748B),
                                                                fontSize = 9.sp
                                                            )
                                                        )
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                1 -> {
                    // TAB 1: Download & Pre-cache Manager
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        item {
                            // Overall Download Banner
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(14.dp),
                                colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B)),
                                border = BorderStroke(1.dp, Color(0xFF0284C7))
                            ) {
                                Column(modifier = Modifier.padding(14.dp)) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Column {
                                            Text(
                                                text = if (language == AppLanguage.ODIA) "ସମ୍ପୂର୍ଣ୍ଣ ବାଲେଶ୍ୱର ଅଫଲାଇନ୍ ପ୍ୟାକ୍" else "Full Balasore Offline Bundle",
                                                style = MaterialTheme.typography.titleMedium.copy(
                                                    fontWeight = FontWeight.Bold,
                                                    color = Color.White
                                                )
                                            )
                                            Text(
                                                text = "Pre-caches all 8 key areas (~24 MB) across zoom 12-15",
                                                style = MaterialTheme.typography.bodySmall.copy(
                                                    color = Color(0xFF94A3B8),
                                                    fontSize = 11.sp
                                                )
                                            )
                                        }

                                        Button(
                                            onClick = { mapManager.downloadAllKeyAreas() },
                                            enabled = !overallDownloading,
                                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0284C7)),
                                            shape = RoundedCornerShape(10.dp)
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.CloudDownload,
                                                contentDescription = "Download All",
                                                modifier = Modifier.size(16.dp)
                                            )
                                            Spacer(modifier = Modifier.width(6.dp))
                                            Text(
                                                text = if (overallDownloading) "Caching..." else "Download All",
                                                fontSize = 12.sp,
                                                fontWeight = FontWeight.Bold
                                            )
                                        }
                                    }

                                    if (overallDownloading) {
                                        Spacer(modifier = Modifier.height(10.dp))
                                        LinearProgressIndicator(
                                            progress = { overallProgress },
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .height(6.dp)
                                                .clip(RoundedCornerShape(3.dp)),
                                            color = Color(0xFF38BDF8),
                                            trackColor = Color(0xFF334155)
                                        )
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(
                                            text = "Downloading key areas: ${(overallProgress * 100).toInt()}% complete",
                                            style = MaterialTheme.typography.bodySmall.copy(
                                                color = Color(0xFF7DD3FC),
                                                fontSize = 10.sp
                                            )
                                        )
                                    }

                                    // Storage stats row
                                    Spacer(modifier = Modifier.height(10.dp))
                                    HorizontalDivider(color = Color(0xFF334155))
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text(
                                            text = "Storage: ${mapManager.getTotalStorageUsedMb().format(1)} MB (${mapManager.getTotalDownloadedTiles()} tiles)",
                                            style = MaterialTheme.typography.bodySmall.copy(
                                                color = Color(0xFFCBD5E1),
                                                fontSize = 11.sp
                                            )
                                        )
                                        Text(
                                            text = "Clear All",
                                            style = MaterialTheme.typography.bodySmall.copy(
                                                color = Color(0xFFF87171),
                                                fontSize = 11.sp,
                                                fontWeight = FontWeight.Bold
                                            ),
                                            modifier = Modifier.clickable { mapManager.deleteAllPacks() }
                                        )
                                    }
                                }
                            }
                        }

                        item {
                            Text(
                                text = "Key Geographical Sectors (${mapManager.keyAreas.size} Areas)",
                                style = MaterialTheme.typography.labelLarge.copy(
                                    color = Color(0xFF94A3B8),
                                    fontWeight = FontWeight.SemiBold
                                ),
                                modifier = Modifier.padding(vertical = 4.dp)
                            )
                        }

                        // List of Key Areas with individual download buttons
                        items(mapManager.keyAreas) { area ->
                            val pack = packStates[area.id]
                            val isDownloaded = pack?.isDownloaded == true
                            val isDownloading = pack?.isDownloading == true
                            val progress = pack?.downloadProgress ?: 0f

                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp),
                                colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B)),
                                border = BorderStroke(
                                    1.dp,
                                    if (isDownloaded) Color(0xFF10B981).copy(alpha = 0.5f) else Color(0xFF334155)
                                )
                            ) {
                                Column(modifier = Modifier.padding(12.dp)) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Column(modifier = Modifier.weight(1f)) {
                                            Row(verticalAlignment = Alignment.CenterVertically) {
                                                Text(
                                                    text = if (language == AppLanguage.ODIA) area.nameOr else area.nameEn,
                                                    style = MaterialTheme.typography.bodyMedium.copy(
                                                        fontWeight = FontWeight.Bold,
                                                        color = Color.White
                                                    )
                                                )
                                                Spacer(modifier = Modifier.width(6.dp))
                                                Surface(
                                                    shape = RoundedCornerShape(4.dp),
                                                    color = Color(0xFF0284C7).copy(alpha = 0.25f)
                                                ) {
                                                    Text(
                                                        text = area.tag,
                                                        fontSize = 9.sp,
                                                        color = Color(0xFF38BDF8),
                                                        modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                                                    )
                                                }
                                            }
                                            Text(
                                                text = area.description,
                                                style = MaterialTheme.typography.bodySmall.copy(
                                                    color = Color(0xFF94A3B8),
                                                    fontSize = 11.sp
                                                ),
                                                maxLines = 2,
                                                overflow = TextOverflow.Ellipsis
                                            )
                                            Text(
                                                text = "Zoom 12-15 • ~${area.estimatedTiles} tiles • ~${area.estimatedSizeMb} MB",
                                                style = MaterialTheme.typography.bodySmall.copy(
                                                    color = Color(0xFF64748B),
                                                    fontSize = 10.sp
                                                )
                                            )
                                        }

                                        Spacer(modifier = Modifier.width(8.dp))

                                        // Action Button
                                        if (isDownloaded) {
                                            Row(verticalAlignment = Alignment.CenterVertically) {
                                                Surface(
                                                    shape = CircleShape,
                                                    color = Color(0xFF10B981).copy(alpha = 0.2f),
                                                    modifier = Modifier.size(28.dp)
                                                ) {
                                                    Box(contentAlignment = Alignment.Center) {
                                                        Icon(
                                                            imageVector = Icons.Default.CheckCircle,
                                                            contentDescription = "Cached",
                                                            tint = Color(0xFF10B981),
                                                            modifier = Modifier.size(16.dp)
                                                        )
                                                    }
                                                }
                                                Spacer(modifier = Modifier.width(6.dp))
                                                IconButton(
                                                    onClick = { mapManager.deleteAreaPack(area.id) },
                                                    modifier = Modifier.size(28.dp)
                                                ) {
                                                    Icon(
                                                        imageVector = Icons.Default.Delete,
                                                        contentDescription = "Delete Pack",
                                                        tint = Color(0xFF64748B),
                                                        modifier = Modifier.size(16.dp)
                                                    )
                                                }
                                            }
                                        } else {
                                            Button(
                                                onClick = { mapManager.downloadAreaPack(area.id) },
                                                enabled = !isDownloading,
                                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0284C7)),
                                                shape = RoundedCornerShape(8.dp),
                                                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                                                modifier = Modifier.height(32.dp)
                                            ) {
                                                Icon(
                                                    imageVector = Icons.Default.Download,
                                                    contentDescription = "Download",
                                                    modifier = Modifier.size(14.dp)
                                                )
                                                Spacer(modifier = Modifier.width(4.dp))
                                                Text(
                                                    text = if (isDownloading) "${(progress * 100).toInt()}%" else "Cache",
                                                    fontSize = 11.sp,
                                                    fontWeight = FontWeight.Bold
                                                )
                                            }
                                        }
                                    }

                                    if (isDownloading) {
                                        Spacer(modifier = Modifier.height(6.dp))
                                        LinearProgressIndicator(
                                            progress = { progress },
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .height(4.dp)
                                                .clip(RoundedCornerShape(2.dp)),
                                            color = Color(0xFF38BDF8),
                                            trackColor = Color(0xFF334155)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                2 -> {
                    // TAB 2: Emergency Safe POIs Directory (Pre-cached for offline access)
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        item {
                            Text(
                                text = "Pre-cached Safe Havens & Emergency Hubs",
                                style = MaterialTheme.typography.labelLarge.copy(
                                    color = Color(0xFF94A3B8),
                                    fontWeight = FontWeight.SemiBold
                                ),
                                modifier = Modifier.padding(vertical = 4.dp)
                            )
                        }

                        items(mapManager.offlinePois) { poi ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        selectedPoi = poi
                                        activeTab = 0
                                        val route = mapManager.calculateOfflineRoute(
                                            origin = userCurrentLocation.value,
                                            destinationPoi = poi
                                        )
                                        activeNavRoute = route
                                        googleMapInstance?.let { drawRouteOnMap(it, route) }
                                    },
                                shape = RoundedCornerShape(12.dp),
                                colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B)),
                                border = BorderStroke(1.dp, Color(0xFF334155))
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(12.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Row(
                                        modifier = Modifier.weight(1f),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Surface(
                                            shape = CircleShape,
                                            color = when (poi.category) {
                                                NavPointCategory.HOSPITAL -> Color(0xFFEF4444).copy(alpha = 0.2f)
                                                NavPointCategory.CYCLONE_SHELTER -> Color(0xFFF59E0B).copy(alpha = 0.2f)
                                                NavPointCategory.POLICE_DEFENSE -> Color(0xFF38BDF8).copy(alpha = 0.2f)
                                                NavPointCategory.FIRE_RESCUE -> Color(0xFFA855F7).copy(alpha = 0.2f)
                                                else -> Color(0xFF10B981).copy(alpha = 0.2f)
                                            },
                                            modifier = Modifier.size(36.dp)
                                        ) {
                                            Box(contentAlignment = Alignment.Center) {
                                                Icon(
                                                    imageVector = when (poi.category) {
                                                        NavPointCategory.HOSPITAL -> Icons.Default.LocalHospital
                                                        NavPointCategory.CYCLONE_SHELTER -> Icons.Default.Shield
                                                        NavPointCategory.POLICE_DEFENSE -> Icons.Default.Security
                                                        NavPointCategory.FIRE_RESCUE -> Icons.Default.Emergency
                                                        else -> Icons.Default.LocationOn
                                                    },
                                                    contentDescription = poi.category.name,
                                                    tint = when (poi.category) {
                                                        NavPointCategory.HOSPITAL -> Color(0xFFF87171)
                                                        NavPointCategory.CYCLONE_SHELTER -> Color(0xFFFBBF24)
                                                        NavPointCategory.POLICE_DEFENSE -> Color(0xFF38BDF8)
                                                        NavPointCategory.FIRE_RESCUE -> Color(0xFFC084FC)
                                                        else -> Color(0xFF34D399)
                                                    },
                                                    modifier = Modifier.size(18.dp)
                                                )
                                            }
                                        }

                                        Spacer(modifier = Modifier.width(10.dp))

                                        Column {
                                            Text(
                                                text = if (language == AppLanguage.ODIA) poi.nameOr else poi.nameEn,
                                                style = MaterialTheme.typography.bodyMedium.copy(
                                                    fontWeight = FontWeight.Bold,
                                                    color = Color.White
                                                )
                                            )
                                            Text(
                                                text = poi.address,
                                                style = MaterialTheme.typography.bodySmall.copy(
                                                    color = Color(0xFF94A3B8),
                                                    fontSize = 11.sp
                                                )
                                            )
                                            Text(
                                                text = "Emergency Tel: ${poi.emergencyPhone} • 24x7 Ready",
                                                style = MaterialTheme.typography.bodySmall.copy(
                                                    color = Color(0xFF38BDF8),
                                                    fontSize = 10.sp,
                                                    fontWeight = FontWeight.SemiBold
                                                )
                                            )
                                        }
                                    }

                                    // Navigate Offline Button
                                    Button(
                                        onClick = {
                                            selectedPoi = poi
                                            activeTab = 0
                                            val route = mapManager.calculateOfflineRoute(
                                                origin = userCurrentLocation.value,
                                                destinationPoi = poi
                                            )
                                            activeNavRoute = route
                                            googleMapInstance?.let { drawRouteOnMap(it, route) }
                                        },
                                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0284C7)),
                                        shape = RoundedCornerShape(8.dp),
                                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                                        modifier = Modifier.height(30.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Directions,
                                            contentDescription = "Navigate",
                                            modifier = Modifier.size(12.dp)
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text("Route", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                    }
                                }
                            }
                        }
                    }
                }

                3 -> {
                    // TAB 3: MapCacheManager Landmark Tile Cache (Shelters, Tourist Spots, Emergency Hubs)
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        item {
                            // Summary Banner Card
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp),
                                colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B)),
                                border = BorderStroke(1.dp, Color(0xFF334155))
                            ) {
                                Column(modifier = Modifier.padding(12.dp)) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Column {
                                            Text(
                                                text = if (language == AppLanguage.ODIA) "ସ୍ମାରକୀ ମ୍ୟାପ୍ ଟାଇଲ୍ କ୍ୟାସ୍" else "Landmark Map Tiles Cache",
                                                style = MaterialTheme.typography.titleSmall.copy(
                                                    fontWeight = FontWeight.Bold,
                                                    color = Color.White
                                                )
                                            )
                                            Text(
                                                text = "Pre-fetches map tiles for Google Maps offline display",
                                                style = MaterialTheme.typography.bodySmall.copy(
                                                    color = Color(0xFF94A3B8),
                                                    fontSize = 11.sp
                                                )
                                            )
                                        }

                                        Surface(
                                            shape = RoundedCornerShape(6.dp),
                                            color = Color(0xFF10B981).copy(alpha = 0.2f),
                                            border = BorderStroke(1.dp, Color(0xFF10B981).copy(alpha = 0.4f))
                                        ) {
                                            Text(
                                                text = "${cacheSummary.cachedLandmarksCount}/${cacheSummary.totalLandmarksCount} Ready",
                                                fontSize = 10.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = Color(0xFF34D399),
                                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                                            )
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(10.dp))

                                    // Quick Stats Row
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        Surface(
                                            shape = RoundedCornerShape(8.dp),
                                            color = Color(0xFF0F172A),
                                            modifier = Modifier.weight(1f)
                                        ) {
                                            Column(modifier = Modifier.padding(8.dp)) {
                                                Text("Cached Tiles", fontSize = 10.sp, color = Color(0xFF94A3B8))
                                                Text("${cacheSummary.totalCachedTiles}", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color.White)
                                            }
                                        }
                                        Surface(
                                            shape = RoundedCornerShape(8.dp),
                                            color = Color(0xFF0F172A),
                                            modifier = Modifier.weight(1f)
                                        ) {
                                            Column(modifier = Modifier.padding(8.dp)) {
                                                Text("Disk Used", fontSize = 10.sp, color = Color(0xFF94A3B8))
                                                Text(cacheSummary.totalSizeFormatted, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color(0xFF38BDF8))
                                            }
                                        }
                                        Surface(
                                            shape = RoundedCornerShape(8.dp),
                                            color = Color(0xFF0F172A),
                                            modifier = Modifier.weight(1f)
                                        ) {
                                            Column(modifier = Modifier.padding(8.dp)) {
                                                Text("Shelters Ready", fontSize = 10.sp, color = Color(0xFF94A3B8))
                                                Text("${cacheSummary.sheltersCachedCount}/14", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color(0xFFF59E0B))
                                            }
                                        }
                                    }

                                    if (isPrefetchingAllLandmarks) {
                                        Spacer(modifier = Modifier.height(10.dp))
                                        Column {
                                            Row(
                                                modifier = Modifier.fillMaxWidth(),
                                                horizontalArrangement = Arrangement.SpaceBetween
                                            ) {
                                                Text(
                                                    text = currentLandmarkTask ?: "Caching tiles...",
                                                    fontSize = 11.sp,
                                                    color = Color(0xFF38BDF8),
                                                    maxLines = 1,
                                                    overflow = TextOverflow.Ellipsis,
                                                    modifier = Modifier.weight(1f)
                                                )
                                                Text(
                                                    text = "${(prefetchLandmarksProgress * 100).toInt()}%",
                                                    fontSize = 11.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    color = Color.White
                                                )
                                            }
                                            Spacer(modifier = Modifier.height(4.dp))
                                            LinearProgressIndicator(
                                                progress = { prefetchLandmarksProgress },
                                                modifier = Modifier.fillMaxWidth().height(6.dp).clip(RoundedCornerShape(3.dp)),
                                                color = Color(0xFF38BDF8),
                                                trackColor = Color(0xFF334155)
                                            )
                                            Spacer(modifier = Modifier.height(6.dp))
                                            OutlinedButton(
                                                onClick = { mapCacheManager.cancelPrefetch() },
                                                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFFF87171)),
                                                border = BorderStroke(1.dp, Color(0xFFEF4444)),
                                                modifier = Modifier.fillMaxWidth().height(30.dp),
                                                contentPadding = PaddingValues(0.dp)
                                            ) {
                                                Text("Cancel Pre-fetching", fontSize = 11.sp)
                                            }
                                        }
                                    } else {
                                        // Bulk Actions Row
                                        Spacer(modifier = Modifier.height(10.dp))
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                                        ) {
                                            Button(
                                                onClick = { mapCacheManager.prefetchAllShelters() },
                                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD97706)),
                                                shape = RoundedCornerShape(8.dp),
                                                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
                                                modifier = Modifier.weight(1f).height(32.dp)
                                            ) {
                                                Text("🛡️ Shelters (14)", fontSize = 10.5.sp, fontWeight = FontWeight.Bold)
                                            }
                                            Button(
                                                onClick = { mapCacheManager.prefetchAllTouristSpots() },
                                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0284C7)),
                                                shape = RoundedCornerShape(8.dp),
                                                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
                                                modifier = Modifier.weight(1f).height(32.dp)
                                            ) {
                                                Text("🏖️ Tourism (12)", fontSize = 10.5.sp, fontWeight = FontWeight.Bold)
                                            }
                                            Button(
                                                onClick = { mapCacheManager.prefetchAllLandmarks() },
                                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF10B981)),
                                                shape = RoundedCornerShape(8.dp),
                                                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
                                                modifier = Modifier.weight(1f).height(32.dp)
                                            ) {
                                                Text("🌐 Cache All", fontSize = 10.5.sp, fontWeight = FontWeight.Bold)
                                            }
                                        }
                                    }
                                }
                            }
                        }

                        // Filter Chips
                        item {
                            LazyRow(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                item {
                                    FilterChip(
                                        selected = landmarkFilterType == null,
                                        onClick = { landmarkFilterType = null },
                                        label = { Text("All (${mapCacheManager.allLandmarks.size})", fontSize = 11.sp) },
                                        colors = FilterChipDefaults.filterChipColors(
                                            selectedContainerColor = Color(0xFF0284C7),
                                            selectedLabelColor = Color.White,
                                            containerColor = Color(0xFF1E293B),
                                            labelColor = Color(0xFFCBD5E1)
                                        )
                                    )
                                }
                                item {
                                    FilterChip(
                                        selected = landmarkFilterType == LandmarkType.CYCLONE_SHELTER,
                                        onClick = { landmarkFilterType = LandmarkType.CYCLONE_SHELTER },
                                        label = { Text("Cyclone Shelters (14)", fontSize = 11.sp) },
                                        colors = FilterChipDefaults.filterChipColors(
                                            selectedContainerColor = Color(0xFFD97706),
                                            selectedLabelColor = Color.White,
                                            containerColor = Color(0xFF1E293B),
                                            labelColor = Color(0xFFCBD5E1)
                                        )
                                    )
                                }
                                item {
                                    FilterChip(
                                        selected = landmarkFilterType == LandmarkType.TOURIST_SPOT || landmarkFilterType == LandmarkType.COASTAL_BEACH || landmarkFilterType == LandmarkType.HERITAGE_TEMPLE,
                                        onClick = { landmarkFilterType = LandmarkType.TOURIST_SPOT },
                                        label = { Text("Tourist & Heritage (12)", fontSize = 11.sp) },
                                        colors = FilterChipDefaults.filterChipColors(
                                            selectedContainerColor = Color(0xFF0284C7),
                                            selectedLabelColor = Color.White,
                                            containerColor = Color(0xFF1E293B),
                                            labelColor = Color(0xFFCBD5E1)
                                        )
                                    )
                                }
                                item {
                                    FilterChip(
                                        selected = landmarkFilterType == LandmarkType.EMERGENCY_HUB,
                                        onClick = { landmarkFilterType = LandmarkType.EMERGENCY_HUB },
                                        label = { Text("Emergency Hubs (5)", fontSize = 11.sp) },
                                        colors = FilterChipDefaults.filterChipColors(
                                            selectedContainerColor = Color(0xFFDC2626),
                                            selectedLabelColor = Color.White,
                                            containerColor = Color(0xFF1E293B),
                                            labelColor = Color(0xFFCBD5E1)
                                        )
                                    )
                                }
                            }
                        }

                        // List of Landmarks with Caching Controls
                        val displayedLandmarks = mapCacheManager.allLandmarks.filter { lm ->
                            when (landmarkFilterType) {
                                null -> true
                                LandmarkType.TOURIST_SPOT -> lm.type == LandmarkType.TOURIST_SPOT || lm.type == LandmarkType.COASTAL_BEACH || lm.type == LandmarkType.HERITAGE_TEMPLE
                                else -> lm.type == landmarkFilterType
                            }
                        }

                        items(displayedLandmarks, key = { it.id }) { landmark ->
                            val status = cacheStatusMap[landmark.id]
                            val isCached = status?.isCached == true
                            val isPrefetchingThis = status?.isPrefetching == true
                            val progress = status?.progressPercent ?: 0f

                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp),
                                colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B)),
                                border = BorderStroke(
                                    1.dp,
                                    if (isCached) Color(0xFF10B981).copy(alpha = 0.5f) else Color(0xFF334155)
                                )
                            ) {
                                Column(modifier = Modifier.padding(12.dp)) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Column(modifier = Modifier.weight(1f)) {
                                            Row(verticalAlignment = Alignment.CenterVertically) {
                                                Text(
                                                    text = if (language == AppLanguage.ODIA) landmark.nameOr else landmark.nameEn,
                                                    style = MaterialTheme.typography.bodyMedium.copy(
                                                        fontWeight = FontWeight.Bold,
                                                        color = Color.White
                                                    )
                                                )
                                                Spacer(modifier = Modifier.width(6.dp))
                                                Surface(
                                                    shape = RoundedCornerShape(4.dp),
                                                    color = when (landmark.type) {
                                                        LandmarkType.CYCLONE_SHELTER -> Color(0xFFD97706).copy(alpha = 0.25f)
                                                        LandmarkType.EMERGENCY_HUB -> Color(0xFFDC2626).copy(alpha = 0.25f)
                                                        else -> Color(0xFF0284C7).copy(alpha = 0.25f)
                                                    }
                                                ) {
                                                    Text(
                                                        text = when (landmark.type) {
                                                            LandmarkType.CYCLONE_SHELTER -> "SHELTER"
                                                            LandmarkType.COASTAL_BEACH -> "BEACH"
                                                            LandmarkType.HERITAGE_TEMPLE -> "TEMPLE"
                                                            LandmarkType.EMERGENCY_HUB -> "EMERGENCY"
                                                            else -> "TOURISM"
                                                        },
                                                        fontSize = 8.5.sp,
                                                        fontWeight = FontWeight.Bold,
                                                        color = when (landmark.type) {
                                                            LandmarkType.CYCLONE_SHELTER -> Color(0xFFF59E0B)
                                                            LandmarkType.EMERGENCY_HUB -> Color(0xFFF87171)
                                                            else -> Color(0xFF38BDF8)
                                                        },
                                                        modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                                                    )
                                                }
                                            }

                                            Text(
                                                text = landmark.description,
                                                style = MaterialTheme.typography.bodySmall.copy(
                                                    color = Color(0xFF94A3B8),
                                                    fontSize = 11.sp
                                                ),
                                                maxLines = 2,
                                                overflow = TextOverflow.Ellipsis
                                            )

                                            Spacer(modifier = Modifier.height(3.dp))
                                            Row(verticalAlignment = Alignment.CenterVertically) {
                                                if (isCached) {
                                                    Icon(
                                                        imageVector = Icons.Default.CheckCircle,
                                                        contentDescription = null,
                                                        tint = Color(0xFF10B981),
                                                        modifier = Modifier.size(12.dp)
                                                    )
                                                    Spacer(modifier = Modifier.width(3.dp))
                                                    Text(
                                                        text = "${status?.cachedTilesCount} tiles • ${status?.sizeFormatted} (Ready Offline)",
                                                        fontSize = 10.sp,
                                                        color = Color(0xFF34D399),
                                                        fontWeight = FontWeight.SemiBold
                                                    )
                                                } else {
                                                    Text(
                                                        text = "Not Cached • ~${status?.totalTilesTarget ?: 25} tiles needed",
                                                        fontSize = 10.sp,
                                                        color = Color(0xFF64748B)
                                                    )
                                                }
                                            }
                                        }

                                        Spacer(modifier = Modifier.width(8.dp))

                                        // Action Button
                                        if (isPrefetchingThis) {
                                            CircularProgressIndicator(
                                                modifier = Modifier.size(24.dp),
                                                strokeWidth = 2.dp,
                                                color = Color(0xFF38BDF8)
                                            )
                                        } else if (isCached) {
                                            Row(verticalAlignment = Alignment.CenterVertically) {
                                                TextButton(
                                                    onClick = { mapCacheManager.prefetchLandmark(landmark.id) },
                                                    contentPadding = PaddingValues(horizontal = 6.dp, vertical = 2.dp)
                                                ) {
                                                    Text("Re-Cache", fontSize = 11.sp, color = Color(0xFF38BDF8))
                                                }
                                                IconButton(
                                                    onClick = { mapCacheManager.removeLandmarkCache(landmark.id) },
                                                    modifier = Modifier.size(28.dp)
                                                ) {
                                                    Icon(
                                                        imageVector = Icons.Default.Delete,
                                                        contentDescription = "Delete Cache",
                                                        tint = Color(0xFF64748B),
                                                        modifier = Modifier.size(16.dp)
                                                    )
                                                }
                                            }
                                        } else {
                                            Button(
                                                onClick = { mapCacheManager.prefetchLandmark(landmark.id) },
                                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0284C7)),
                                                shape = RoundedCornerShape(8.dp),
                                                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                                                modifier = Modifier.height(30.dp)
                                            ) {
                                                Icon(
                                                    imageVector = Icons.Default.CloudDownload,
                                                    contentDescription = null,
                                                    modifier = Modifier.size(12.dp)
                                                )
                                                Spacer(modifier = Modifier.width(4.dp))
                                                Text("Cache", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                            }
                                        }
                                    }

                                    if (isPrefetchingThis) {
                                        Spacer(modifier = Modifier.height(6.dp))
                                        LinearProgressIndicator(
                                            progress = { progress },
                                            modifier = Modifier.fillMaxWidth().height(4.dp).clip(RoundedCornerShape(2.dp)),
                                            color = Color(0xFF38BDF8),
                                            trackColor = Color(0xFF334155)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun TabButton(
    title: String,
    selected: Boolean,
    onClick: () -> Unit,
    badge: String? = null,
    modifier: Modifier = Modifier
) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(8.dp),
        color = if (selected) Color(0xFF0284C7) else Color(0xFF1E293B),
        border = BorderStroke(1.dp, if (selected) Color(0xFF38BDF8) else Color(0xFF334155)),
        modifier = modifier.height(36.dp)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 8.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = title,
                fontSize = 11.5.sp,
                fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium,
                color = if (selected) Color.White else Color(0xFF94A3B8)
            )
            badge?.let {
                Spacer(modifier = Modifier.width(4.dp))
                Surface(
                    shape = RoundedCornerShape(4.dp),
                    color = Color(0xFF0F172A).copy(alpha = 0.6f)
                ) {
                    Text(
                        text = it,
                        fontSize = 8.sp,
                        color = Color(0xFF38BDF8),
                        modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                    )
                }
            }
        }
    }
}

private fun Double.format(digits: Int): String = "%.${digits}f".format(this)
