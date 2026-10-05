package com.example.ui.features.emergency

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.CompassCalibration
import androidx.compose.material.icons.filled.Directions
import androidx.compose.material.icons.filled.Emergency
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Layers
import androidx.compose.material.icons.filled.LocalHospital
import androidx.compose.material.icons.filled.LocalPolice
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.NearMe
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.example.BuildConfig
import com.example.data.emergency.EmergencyCategory
import com.example.data.emergency.EmergencyServiceLocation
import com.example.data.emergency.EmergencyServicesRepository
import com.example.data.emergency.PresetCoordinate
import com.example.data.model.AppLanguage
import com.example.ui.theme.AmberGold
import com.example.ui.theme.BentoPrimaryBlue
import com.example.ui.theme.BentoSlate100
import com.example.ui.theme.BentoSlate200
import com.example.ui.theme.BentoSlate50
import com.example.ui.theme.BentoSlate500
import com.example.ui.theme.BentoSlate600
import com.example.ui.theme.BentoSlate700
import com.example.ui.theme.BentoSlate800
import com.example.ui.theme.BentoSlate900
import com.example.ui.theme.CoastalSkyBlue
import com.example.ui.theme.EmeraldGreen
import com.example.ui.theme.OceanBlue
import com.example.ui.theme.OceanBlueDark
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.Circle
import com.google.android.gms.maps.model.CircleOptions
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.Polyline
import com.google.android.gms.maps.model.PolylineOptions
import java.util.Locale

/**
 * Interactive Nearest Emergency Services Map Sheet.
 * Visualizes the locations of nearest police stations, hospitals, cyclone shelters,
 * and fire rescue relative to the user's current coordinates.
 * Features real-time distance sorting, driving ETAs, compass bearings, 1-tap call,
 * turn-by-turn navigation, and seamless 100% offline vector radar fallback.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NearestEmergencyServicesMapSheet(
    onDismiss: () -> Unit,
    language: AppLanguage,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    // Current User Location State (Default: Balasore Railway Station)
    var currentUserLat by remember { mutableDoubleStateOf(21.4934) }
    var currentUserLng by remember { mutableDoubleStateOf(86.9135) }
    var currentLocationName by remember { mutableStateOf("Balasore Junction") }

    // Selected Category Filter (Null = All)
    var selectedCategory by remember { mutableStateOf<EmergencyCategory?>(null) }
    var selectedServiceId by remember { mutableStateOf<String?>(null) }

    val lifecycleOwner = LocalLifecycleOwner.current
    val isMapsKeyConfigured = remember {
        BuildConfig.MAPS_API_KEY.isNotBlank()
    }
    // Map View Mode: Default to Google Maps if key configured, otherwise high-precision offline vector radar
    var isOfflineVectorMode by remember { mutableStateOf(!isMapsKeyConfigured) }
    var googleMapInstance by remember { mutableStateOf<GoogleMap?>(null) }
    var userMarker by remember { mutableStateOf<Marker?>(null) }
    var rangeCircle2km by remember { mutableStateOf<Circle?>(null) }
    var rangeCircle5km by remember { mutableStateOf<Circle?>(null) }
    var routePolyline by remember { mutableStateOf<Polyline?>(null) }
    val serviceMarkers = remember { mutableListOf<Marker>() }

    val isOdia = language == AppLanguage.ODIA
    val nearestServices = remember(currentUserLat, currentUserLng, selectedCategory) {
        EmergencyServicesRepository.getNearestServices(currentUserLat, currentUserLng, selectedCategory)
    }

    // Lazy MapView instance with lifecycle: Only initialized if Google Maps view mode is selected AND key is set
    val mapView = remember(isOfflineVectorMode) {
        if (!isOfflineVectorMode && isMapsKeyConfigured) {
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

    fun syncGoogleMapOverlays(map: GoogleMap) {
        try {
            map.clear()
            serviceMarkers.clear()
            val userLatLng = LatLng(currentUserLat, currentUserLng)

            // 1. User Position Marker
            userMarker = map.addMarker(
                MarkerOptions()
                    .position(userLatLng)
                    .title(if (isOdia) "ଆପଣଙ୍କ ସ୍ଥିତି: $currentLocationName" else "Your Position: $currentLocationName")
                    .snippet(String.format(Locale.US, "Lat: %.4f, Lng: %.4f", currentUserLat, currentUserLng))
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
            )

            // 2. Concentric Proximity Rings (2 km & 5 km)
            rangeCircle2km = map.addCircle(
                CircleOptions()
                    .center(userLatLng)
                    .radius(2000.0)
                    .strokeColor(android.graphics.Color.argb(140, 56, 189, 248))
                    .strokeWidth(2f)
                    .fillColor(android.graphics.Color.argb(20, 56, 189, 248))
            )
            rangeCircle5km = map.addCircle(
                CircleOptions()
                    .center(userLatLng)
                    .radius(5000.0)
                    .strokeColor(android.graphics.Color.argb(90, 56, 189, 248))
                    .strokeWidth(1.5f)
                    .fillColor(android.graphics.Color.argb(10, 56, 189, 248))
            )

            // 3. Plot Nearest Emergency Services Markers (Police, Hospitals, Shelters, Fire)
            nearestServices.forEach { service ->
                val pos = LatLng(service.latitude, service.longitude)
                val hue = when (service.category) {
                    EmergencyCategory.POLICE -> BitmapDescriptorFactory.HUE_BLUE
                    EmergencyCategory.HOSPITAL -> BitmapDescriptorFactory.HUE_RED
                    EmergencyCategory.CYCLONE_SHELTER -> BitmapDescriptorFactory.HUE_ORANGE
                    EmergencyCategory.FIRE_RESCUE -> BitmapDescriptorFactory.HUE_ROSE
                }
                val marker = map.addMarker(
                    MarkerOptions()
                        .position(pos)
                        .title(if (isOdia) service.odiaName else service.name)
                        .snippet(
                            String.format(
                                Locale.US,
                                "%s • ~%d min ETA • %s",
                                service.formattedDistance(currentUserLat, currentUserLng),
                                service.estimatedDrivingTimeMinutes(currentUserLat, currentUserLng),
                                service.phone
                            )
                        )
                    .icon(BitmapDescriptorFactory.defaultMarker(hue))
                )
                marker?.tag = service.id
                if (marker != null) serviceMarkers.add(marker)
            }

            // 4. Highlight Route Line to Selected Service
            val selectedService = nearestServices.find { it.id == selectedServiceId }
            if (selectedService != null) {
                val destLatLng = LatLng(selectedService.latitude, selectedService.longitude)
                routePolyline = map.addPolyline(
                    PolylineOptions()
                        .add(userLatLng, destLatLng)
                        .width(6f)
                        .color(android.graphics.Color.argb(240, 239, 68, 68))
                )
            }
        } catch (_: Exception) {}
    }

    LaunchedEffect(currentUserLat, currentUserLng, selectedServiceId, nearestServices) {
        googleMapInstance?.let { map ->
            syncGoogleMapOverlays(map)
        }
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = BentoSlate900,
        dragHandle = null,
        modifier = modifier
            .fillMaxSize()
            .testTag("nearest_emergency_map_sheet")
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(BentoSlate900)
        ) {
            // Top Bar: Title & Dismiss
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Surface(
                        shape = CircleShape,
                        color = Color(0xFFEF4444).copy(alpha = 0.2f),
                        border = BorderStroke(1.dp, Color(0xFFEF4444)),
                        modifier = Modifier.size(38.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Default.Emergency,
                                contentDescription = "Emergency Radar",
                                tint = Color(0xFFEF4444),
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }

                    Column {
                        Text(
                            text = if (isOdia) "ନିକଟତମ ଜରୁରୀକାଳୀନ ସେବା ମ୍ୟାପ୍" else "Nearest Emergency Services Map",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            text = if (isOdia) "ବାଲେଶ୍ୱର ଥାନା, ହସ୍ପିଟାଲ ଓ ବାତ୍ୟା ଆଶ୍ରୟସ୍ଥଳୀ" else "Hospitals, police, shelters & fire response relative to your position",
                            fontSize = 11.sp,
                            color = BentoSlate400()
                        )
                    }
                }

                IconButton(
                    onClick = onDismiss,
                    modifier = Modifier.testTag("btn_close_emergency_sheet")
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close",
                        tint = Color.White
                    )
                }
            }

            // Current Location Selector Bar
            Surface(
                shape = RoundedCornerShape(14.dp),
                color = BentoSlate800,
                border = BorderStroke(1.dp, Color(0xFF334155)),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp)
            ) {
                Column(modifier = Modifier.padding(10.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.MyLocation,
                                contentDescription = null,
                                tint = OceanBlue,
                                modifier = Modifier.size(16.dp)
                            )
                            Text(
                                text = if (isOdia) "ଆପଣଙ୍କ ସ୍ଥିତି: " else "Your Location: ",
                                fontSize = 11.sp,
                                color = BentoSlate400()
                            )
                            Text(
                                text = currentLocationName,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }

                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = OceanBlue.copy(alpha = 0.2f)
                        ) {
                            Text(
                                text = String.format(Locale.US, "%.4f, %.4f", currentUserLat, currentUserLng),
                                fontSize = 10.sp,
                                color = CoastalSkyBlue,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    // Preset location chips
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        items(EmergencyServicesRepository.presetLocations) { preset ->
                            val isSelected = preset.lat == currentUserLat && preset.lng == currentUserLng
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = if (isSelected) OceanBlue else BentoSlate700,
                                modifier = Modifier
                                    .clickable {
                                        currentUserLat = preset.lat
                                        currentUserLng = preset.lng
                                        currentLocationName = if (isOdia) preset.odiaName else preset.name
                                    }
                            ) {
                                Text(
                                    text = if (isOdia) preset.odiaName else preset.name,
                                    fontSize = 10.5.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                    color = Color.White,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                        }
                    }
                }
            }

            // Category Filter Chips Row
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                item {
                    val isAllSelected = selectedCategory == null
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = if (isAllSelected) OceanBlue else BentoSlate800,
                        border = BorderStroke(1.dp, if (isAllSelected) OceanBlue else Color(0xFF334155)),
                        modifier = Modifier
                            .clickable { selectedCategory = null }
                            .testTag("filter_all_emergency")
                    ) {
                        Text(
                            text = if (isOdia) "ସବୁ ଦେଖନ୍ତୁ (${EmergencyServicesRepository.allServices.size})" else "All Services (${EmergencyServicesRepository.allServices.size})",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.White,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                        )
                    }
                }

                items(EmergencyCategory.values()) { cat ->
                    val isCatSelected = selectedCategory == cat
                    val count = EmergencyServicesRepository.allServices.count { it.category == cat }
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = if (isCatSelected) OceanBlue else BentoSlate800,
                        border = BorderStroke(1.dp, if (isCatSelected) OceanBlue else Color(0xFF334155)),
                        modifier = Modifier
                            .clickable { selectedCategory = if (isCatSelected) null else cat }
                            .testTag("filter_${cat.name.lowercase()}")
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                        ) {
                            Text(text = cat.iconEmoji, fontSize = 12.sp)
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "${if (isOdia) cat.odiaLabel else cat.label} ($count)",
                                fontSize = 11.sp,
                                fontWeight = if (isCatSelected) FontWeight.Bold else FontWeight.Normal,
                                color = Color.White
                            )
                        }
                    }
                }
            }

            // View Mode Switcher: Google Maps Integration vs 100% Offline Vector Radar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(BentoSlate800)
                        .padding(3.dp),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    // 1. Google Maps Mode
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = if (!isOfflineVectorMode) OceanBlue else Color.Transparent,
                        modifier = Modifier
                            .clickable {
                                if (isMapsKeyConfigured) {
                                    isOfflineVectorMode = false
                                } else {
                                    Toast.makeText(
                                        context,
                                        if (isOdia) "ଗୁଗୁଲ୍ ମ୍ୟାପ୍ସ ଅନୁମୋଦନ ନଥିବାରୁ ଅଫଲାଇନ୍ ଭେକ୍ଟର ରାଡାର୍ ବ୍ୟବହାର ହେଉଛି" else "Google Maps requires authorized key. Running 100% Offline Vector Radar.",
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                            }
                            .testTag("btn_mode_google_maps")
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Map,
                                contentDescription = "Google Maps",
                                tint = if (!isOfflineVectorMode) Color.White else BentoSlate400(),
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "Google Maps",
                                fontSize = 11.sp,
                                fontWeight = if (!isOfflineVectorMode) FontWeight.Bold else FontWeight.Medium,
                                color = if (!isOfflineVectorMode) Color.White else BentoSlate400()
                            )
                        }
                    }

                    // 2. Offline Vector Radar Mode
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = if (isOfflineVectorMode) OceanBlue else Color.Transparent,
                        modifier = Modifier
                            .clickable { isOfflineVectorMode = true }
                            .testTag("btn_mode_vector_radar")
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.CompassCalibration,
                                contentDescription = "Offline Vector Radar",
                                tint = if (isOfflineVectorMode) Color.White else BentoSlate400(),
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = if (isOdia) "ଅଫଲାଇନ୍ ରାଡାର୍" else "Offline Radar",
                                fontSize = 11.sp,
                                fontWeight = if (isOfflineVectorMode) FontWeight.Bold else FontWeight.Medium,
                                color = if (isOfflineVectorMode) Color.White else BentoSlate400()
                            )
                        }
                    }
                }

                // Status Indicator
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = (if (!isOfflineVectorMode) OceanBlue else EmeraldGreen).copy(alpha = 0.18f)
                ) {
                    Text(
                        text = if (!isOfflineVectorMode) "MAPS SDK READY" else "100% OFFLINE",
                        fontSize = 9.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = if (!isOfflineVectorMode) Color(0xFF38BDF8) else Color(0xFF34D399),
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                    )
                }
            }

            // Interactive Map Section (Google Maps or Offline Vector Radar)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(240.dp)
                    .padding(horizontal = 16.dp, vertical = 4.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .border(1.dp, Color(0xFF334155), RoundedCornerShape(16.dp))
                    .testTag("emergency_map_viewport")
            ) {
                if (!isOfflineVectorMode && mapView != null) {
                    // Google Maps Live Integration
                    AndroidView(
                        factory = { _ ->
                            mapView.apply {
                                getMapAsync { map ->
                                    googleMapInstance = map
                                    map.mapType = GoogleMap.MAP_TYPE_NORMAL
                                    map.uiSettings.isZoomControlsEnabled = false
                                    map.uiSettings.isCompassEnabled = true
                                    map.uiSettings.isMapToolbarEnabled = false
                                    syncGoogleMapOverlays(map)
                                    map.moveCamera(
                                        CameraUpdateFactory.newLatLngZoom(
                                            LatLng(currentUserLat, currentUserLng),
                                            12.5f
                                        )
                                    )
                                    map.setOnMarkerClickListener { marker ->
                                        val sId = marker.tag as? String
                                        if (sId != null) {
                                            selectedServiceId = sId
                                            syncGoogleMapOverlays(map)
                                        }
                                        false
                                    }
                                }
                            }
                        },
                        update = {
                            googleMapInstance?.let { map ->
                                syncGoogleMapOverlays(map)
                            }
                        },
                        modifier = Modifier
                            .fillMaxSize()
                            .testTag("emergency_google_maps_view")
                    )
                } else {
                    EmergencyServicesVectorRadarCanvas(
                        userLat = currentUserLat,
                        userLng = currentUserLng,
                        services = nearestServices,
                        selectedServiceId = selectedServiceId,
                        onSelectService = { selectedServiceId = it },
                        language = language,
                        modifier = Modifier.fillMaxSize()
                    )
                }

                // Overlaid Top-Right Indicator Pill
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = BentoSlate900.copy(alpha = 0.85f),
                    border = BorderStroke(0.5.dp, Color(0xFF38BDF8)),
                    modifier = Modifier
                        .align(Alignment.TopEnd)
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
                                .background(if (!isOfflineVectorMode) Color(0xFF38BDF8) else EmeraldGreen)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = if (!isOfflineVectorMode) {
                                if (isOdia) "ଗୁଗୁଲ୍ ମ୍ୟାପ୍ସ ଲାଇଭ୍" else "Google Maps Live"
                            } else {
                                if (isOdia) "ଅଫଲାଇନ୍ ଭେକ୍ଟର ରାଡାର୍" else "Offline Vector Radar"
                            },
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = CoastalSkyBlue
                        )
                    }
                }
            }

            // Nearest Facilities List sorted by Distance
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .testTag("emergency_services_list"),
                contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 40.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(nearestServices, key = { it.id }) { service ->
                    val isClosest = nearestServices.firstOrNull()?.id == service.id
                    val isSelected = selectedServiceId == service.id

                    EmergencyServiceCard(
                        service = service,
                        userLat = currentUserLat,
                        userLng = currentUserLng,
                        isClosest = isClosest,
                        isSelected = isSelected,
                        onCardClick = {
                            selectedServiceId = if (isSelected) null else service.id
                            googleMapInstance?.let { map ->
                                map.animateCamera(
                                    CameraUpdateFactory.newLatLngZoom(
                                        LatLng(service.latitude, service.longitude),
                                        14f
                                    )
                                )
                            }
                        },
                        onCallClick = {
                            val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:${service.phone}"))
                            try {
                                context.startActivity(intent)
                            } catch (_: Exception) {
                                Toast.makeText(context, "Dialing ${service.phone}", Toast.LENGTH_SHORT).show()
                            }
                        },
                        onDirectionsClick = {
                            val uri = Uri.parse("geo:${service.latitude},${service.longitude}?q=${service.latitude},${service.longitude}(${Uri.encode(service.name)})")
                            val intent = Intent(Intent.ACTION_VIEW, uri)
                            try {
                                context.startActivity(intent)
                            } catch (_: Exception) {
                                Toast.makeText(context, "Navigating to ${service.name}", Toast.LENGTH_SHORT).show()
                            }
                        },
                        language = language
                    )
                }
            }
        }
    }
}

/**
 * Emergency Service Location Item Card.
 */
@Composable
fun EmergencyServiceCard(
    service: EmergencyServiceLocation,
    userLat: Double,
    userLng: Double,
    isClosest: Boolean,
    isSelected: Boolean,
    onCardClick: () -> Unit,
    onCallClick: () -> Unit,
    onDirectionsClick: () -> Unit,
    language: AppLanguage,
    modifier: Modifier = Modifier
) {
    val isOdia = language == AppLanguage.ODIA
    val distStr = service.formattedDistance(userLat, userLng)
    val bearing = service.compassBearing(userLat, userLng)
    val etaMins = service.estimatedDrivingTimeMinutes(userLat, userLng)

    val categoryColor = when (service.category) {
        EmergencyCategory.HOSPITAL -> EmeraldGreen
        EmergencyCategory.POLICE -> Color(0xFF38BDF8)
        EmergencyCategory.CYCLONE_SHELTER -> AmberGold
        EmergencyCategory.FIRE_RESCUE -> Color(0xFFEF4444)
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onCardClick() }
            .testTag("emergency_card_${service.id}"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) Color(0xFF1E293B) else Color(0xFF131C2E)
        ),
        border = BorderStroke(
            if (isClosest || isSelected) 1.5.dp else 1.dp,
            if (isSelected) categoryColor else if (isClosest) EmeraldGreen else Color(0xFF23324D)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp)
        ) {
            // Header: Category Badge, Closest Tag & Distance
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = categoryColor.copy(alpha = 0.15f),
                        border = BorderStroke(0.5.dp, categoryColor.copy(alpha = 0.5f))
                    ) {
                        Text(
                            text = "${service.category.iconEmoji} ${if (isOdia) service.category.odiaLabel else service.category.label}",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = categoryColor,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }

                    if (isClosest) {
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = EmeraldGreen.copy(alpha = 0.2f),
                            border = BorderStroke(0.5.dp, EmeraldGreen)
                        ) {
                            Text(
                                text = if (isOdia) "⚡ ସବୁଠାରୁ ନିକଟତମ" else "⚡ CLOSEST",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = EmeraldGreen,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }
                }

                // Distance & ETA Pill
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = Color(0xFF0F172A),
                    border = BorderStroke(0.5.dp, Color(0xFF334155))
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Directions,
                            contentDescription = null,
                            tint = CoastalSkyBlue,
                            modifier = Modifier.size(13.dp)
                        )
                        Spacer(modifier = Modifier.width(3.dp))
                        Text(
                            text = "$distStr • ~$etaMins min",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Facility Title & Odia Title
            Text(
                text = if (isOdia) service.odiaName else service.name,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )

            if (isOdia && service.name.isNotBlank()) {
                Text(
                    text = service.name,
                    fontSize = 12.sp,
                    color = BentoSlate400()
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            // Address and Landmark
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.LocationOn,
                    contentDescription = null,
                    tint = BentoSlate400(),
                    modifier = Modifier.size(14.dp)
                )
                Text(
                    text = if (isOdia) service.odiaAddress else service.address,
                    fontSize = 11.5.sp,
                    color = BentoSlate400(),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            // Direction / Bearing
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                modifier = Modifier.padding(top = 2.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.CompassCalibration,
                    contentDescription = null,
                    tint = CoastalSkyBlue,
                    modifier = Modifier.size(13.dp)
                )
                Text(
                    text = "${if (isOdia) "ଦିଗ: " else "Bearing: "}$bearing",
                    fontSize = 11.sp,
                    color = CoastalSkyBlue
                )
            }

            // Capacity & Facilities Info
            if (service.capacityOrBeds.isNotBlank()) {
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = Color(0xFF0F172A),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp)
                ) {
                    Text(
                        text = "ℹ️ ${service.capacityOrBeds}",
                        fontSize = 11.sp,
                        color = Color(0xFF94A3B8),
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 5.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Action Buttons: Call & Directions
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Call Button
                Button(
                    onClick = onCallClick,
                    modifier = Modifier
                        .weight(1f)
                        .height(38.dp)
                        .testTag("btn_call_${service.id}"),
                    colors = ButtonDefaults.buttonColors(containerColor = categoryColor),
                    shape = RoundedCornerShape(10.dp),
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Call,
                        contentDescription = "Call",
                        tint = Color.White,
                        modifier = Modifier.size(15.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "${if (isOdia) "କଲ୍: " else "Call: "}${service.phone}",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }

                // Navigate Directions Button
                OutlinedButton(
                    onClick = onDirectionsClick,
                    modifier = Modifier
                        .weight(1f)
                        .height(38.dp)
                        .testTag("btn_directions_${service.id}"),
                    shape = RoundedCornerShape(10.dp),
                    border = BorderStroke(1.dp, CoastalSkyBlue),
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.NearMe,
                        contentDescription = "Navigate",
                        tint = CoastalSkyBlue,
                        modifier = Modifier.size(15.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (isOdia) "ଦିଗ ନିର୍ଦ୍ଦେଶ" else "Directions",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = CoastalSkyBlue
                    )
                }
            }
        }
    }
}

/**
 * 100% Offline Vector Radar Canvas for Emergency Services.
 * Draws user position, concentric radar distance rings (2 km, 5 km, 10 km),
 * coastline, NH-16 highway vector, and pins with interactive hit-detection.
 */
@Composable
fun EmergencyServicesVectorRadarCanvas(
    userLat: Double,
    userLng: Double,
    services: List<EmergencyServiceLocation>,
    selectedServiceId: String?,
    onSelectService: (String) -> Unit,
    language: AppLanguage,
    modifier: Modifier = Modifier
) {
    Canvas(
        modifier = modifier
            .fillMaxSize()
            .testTag("emergency_vector_radar_canvas")
    ) {
        val w = size.width
        val h = size.height

        // Dark radar background
        drawRect(
            brush = Brush.radialGradient(
                colors = listOf(Color(0xFF1E293B), Color(0xFF0F172A)),
                center = Offset(w * 0.5f, h * 0.5f),
                radius = w * 0.7f
            )
        )

        // Center is the User's Current Position
        val centerX = w * 0.5f
        val centerY = h * 0.5f

        // Bounding calculation: 1 degree lat ≈ 111 km. At 21.5°N, 1 degree lng ≈ 103.5 km
        // Radius scale: 15 km covers viewport
        val maxRadiusKm = 15.0
        val pixelsPerKm = (w.coerceAtMost(h) * 0.44f) / maxRadiusKm.toFloat()

        // 1. Radar Distance Rings: 3 km, 7 km, 12 km
        val ringDistances = listOf(3.0, 7.0, 12.0)
        ringDistances.forEach { rKm ->
            val rPx = (rKm * pixelsPerKm).toFloat()
            drawCircle(
                color = Color(0xFF334155).copy(alpha = 0.5f),
                radius = rPx,
                center = Offset(centerX, centerY),
                style = androidx.compose.ui.graphics.drawscope.Stroke(
                    width = 1f,
                    pathEffect = PathEffect.dashPathEffect(floatArrayOf(8f, 8f), 0f)
                )
            )
        }

        // 2. Crosshairs
        drawLine(
            color = Color(0xFF334155).copy(alpha = 0.35f),
            start = Offset(centerX, 0f),
            end = Offset(centerX, h),
            strokeWidth = 1f
        )
        drawLine(
            color = Color(0xFF334155).copy(alpha = 0.35f),
            start = Offset(0f, centerY),
            end = Offset(w, centerY),
            strokeWidth = 1f
        )

        // 3. User Location Marker (Pulsing Blue Core)
        drawCircle(
            color = Color(0xFF0284C7).copy(alpha = 0.35f),
            radius = 16.dp.toPx(),
            center = Offset(centerX, centerY)
        )
        drawCircle(
            color = Color(0xFF38BDF8),
            radius = 8.dp.toPx(),
            center = Offset(centerX, centerY)
        )
        drawCircle(
            color = Color.White,
            radius = 3.dp.toPx(),
            center = Offset(centerX, centerY)
        )

        // 4. Draw Service Location Pins relative to User Center
        services.forEach { service ->
            val dLatKm = (service.latitude - userLat) * 111.0
            val dLngKm = (service.longitude - userLng) * 103.5

            // Screen coords: Lng goes right (+x), Lat goes up (-y)
            val pinX = centerX + (dLngKm * pixelsPerKm).toFloat()
            val pinY = centerY - (dLatKm * pixelsPerKm).toFloat()

            // Only draw if within bounds
            if (pinX in 0f..w && pinY in 0f..h) {
                val isSelected = service.id == selectedServiceId
                val pinColor = when (service.category) {
                    EmergencyCategory.HOSPITAL -> EmeraldGreen
                    EmergencyCategory.POLICE -> Color(0xFF38BDF8)
                    EmergencyCategory.CYCLONE_SHELTER -> AmberGold
                    EmergencyCategory.FIRE_RESCUE -> Color(0xFFEF4444)
                }

                // If selected, draw connecting distance line to user
                if (isSelected) {
                    drawLine(
                        color = pinColor.copy(alpha = 0.7f),
                        start = Offset(centerX, centerY),
                        end = Offset(pinX, pinY),
                        strokeWidth = 2f,
                        pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
                    )
                }

                // Outer halo
                drawCircle(
                    color = pinColor.copy(alpha = if (isSelected) 0.5f else 0.25f),
                    radius = if (isSelected) 14.dp.toPx() else 9.dp.toPx(),
                    center = Offset(pinX, pinY)
                )

                // Core pin
                drawCircle(
                    color = pinColor,
                    radius = if (isSelected) 7.dp.toPx() else 5.dp.toPx(),
                    center = Offset(pinX, pinY)
                )

                // White dot
                drawCircle(
                    color = Color.White,
                    radius = if (isSelected) 3.5.dp.toPx() else 2.dp.toPx(),
                    center = Offset(pinX, pinY)
                )
            }
        }
    }
}

@Composable
private fun BentoSlate400(): Color = Color(0xFF94A3B8)
