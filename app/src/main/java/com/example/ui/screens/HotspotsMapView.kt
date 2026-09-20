package com.example.ui.screens

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Directions
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Layers
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.NearMe
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
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
import com.example.data.local.HotspotEntity
import com.example.data.model.AppLanguage
import com.example.data.model.Hotspot
import com.example.data.repository.DefaultData
import com.example.ui.theme.AmberGold
import com.example.ui.theme.BentoCardWhite
import com.example.ui.theme.BentoPrimaryBlue
import com.example.ui.theme.BentoSlate200
import com.example.ui.theme.BentoSlate500
import com.example.ui.theme.BentoSlate600
import com.example.ui.theme.BentoSlate700
import com.example.ui.theme.BentoSlate900
import com.example.ui.theme.EmeraldGreen
import com.example.ui.theme.OceanBlue
import com.example.ui.theme.OceanBlueDark
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions

/**
 * Unified data representation for Google Maps markers in Balasore.
 * Represents either a Tourist Hotspot or a Cyclone Shelter.
 */
sealed class BalasoreMapItem {
    abstract val id: String
    abstract val name: String
    abstract val odiaName: String
    abstract val latitude: Double
    abstract val longitude: Double
    abstract val distanceKm: Double
    abstract val isShelter: Boolean

    data class TouristSpot(
        override val id: String,
        override val name: String,
        override val odiaName: String,
        val category: String,
        val shortDesc: String,
        val timings: String,
        val highlights: String,
        override val latitude: Double,
        override val longitude: Double,
        override val distanceKm: Double
    ) : BalasoreMapItem() {
        override val isShelter: Boolean get() = false
    }

    data class CycloneShelterSpot(
        override val id: String,
        override val name: String,
        override val odiaName: String,
        val block: String,
        val capacity: Int,
        val phone: String,
        val locationDesc: String,
        val elevationMeters: Double,
        val hasSolarBackup: Boolean,
        override val latitude: Double,
        override val longitude: Double,
        override val distanceKm: Double
    ) : BalasoreMapItem() {
        override val isShelter: Boolean get() = true
    }
}

/**
 * Pre-defined coordinates and attributes for Cyclone Shelters in Balasore district.
 */
val defaultCycloneSheltersMapData = listOf(
    BalasoreMapItem.CycloneShelterSpot(
        id = "cs_1",
        name = "Chandipur Coastal Multipurpose Shelter",
        odiaName = "ଚାନ୍ଦିପୁର ବହୁମୁଖୀ ବାତ୍ୟା ଆଶ୍ରୟସ୍ଥଳ",
        block = "Chandipur Coastal",
        capacity = 1800,
        phone = "06782262234",
        locationDesc = "Near Marine Police Station, Chandipur",
        elevationMeters = 9.2,
        hasSolarBackup = true,
        latitude = 21.4682,
        longitude = 87.0163,
        distanceKm = 1.2
    ),
    BalasoreMapItem.CycloneShelterSpot(
        id = "cs_2",
        name = "Bhograi Coastal Disaster Shelter",
        odiaName = "ଭୋଗରାଇ ଉପକୂଳ ବାତ୍ୟା ଆଶ୍ରୟସ୍ଥଳ",
        block = "Bhograi",
        capacity = 2400,
        phone = "06781238210",
        locationDesc = "Talasari Beach Embankment, Bhograi",
        elevationMeters = 9.5,
        hasSolarBackup = true,
        latitude = 21.5892,
        longitude = 87.4582,
        distanceKm = 48.0
    ),
    BalasoreMapItem.CycloneShelterSpot(
        id = "cs_3",
        name = "Remuna High School Disaster Shelter",
        odiaName = "ରେମୁଣା ଉଚ୍ଚ ବିଦ୍ୟାଳୟ ଆଶ୍ରୟ କେନ୍ଦ୍ର",
        block = "Remuna",
        capacity = 1400,
        phone = "06782252115",
        locationDesc = "Near Khirachora Gopinath Temple, Remuna",
        elevationMeters = 8.0,
        hasSolarBackup = true,
        latitude = 21.5284,
        longitude = 86.8647,
        distanceKm = 9.5
    ),
    BalasoreMapItem.CycloneShelterSpot(
        id = "cs_4",
        name = "Kasafal Fishermen Disaster Safe Haven",
        odiaName = "କସାଫଳ ମତ୍ସ୍ୟଜୀବୀ ବାତ୍ୟା ଆଶ୍ରୟସ୍ଥଳ",
        block = "Chandipur / Sadar",
        capacity = 1600,
        phone = "06782260100",
        locationDesc = "Kasafal Sea Mouth, Balasore",
        elevationMeters = 8.8,
        hasSolarBackup = true,
        latitude = 21.5200,
        longitude = 87.1100,
        distanceKm = 14.2
    ),
    BalasoreMapItem.CycloneShelterSpot(
        id = "cs_5",
        name = "Bahanaga Relief Multipurpose Center",
        odiaName = "ବାହାନଗା ବିପର୍ଯ୍ୟୟ ରିଲିଫ କେନ୍ଦ୍ର",
        block = "Bahanaga",
        capacity = 1500,
        phone = "06782275420",
        locationDesc = "Bahanaga Bazar, NH-16",
        elevationMeters = 7.5,
        hasSolarBackup = true,
        latitude = 21.3150,
        longitude = 86.8050,
        distanceKm = 24.5
    ),
    BalasoreMapItem.CycloneShelterSpot(
        id = "cs_6",
        name = "Baliapal Subarnarekha Basin Shelter",
        odiaName = "ବାଲିଆପାଳ ସୁବର୍ଣ୍ଣରେଖା ଅବବାହିକା ଆଶ୍ରୟସ୍ଥଳ",
        block = "Baliapal",
        capacity = 2100,
        phone = "06781254300",
        locationDesc = "Chaumukh River Embankment, Baliapal",
        elevationMeters = 9.0,
        hasSolarBackup = true,
        latitude = 21.6420,
        longitude = 87.2890,
        distanceKm = 36.8
    ),
    BalasoreMapItem.CycloneShelterSpot(
        id = "cs_7",
        name = "Soro High School Elevated Cyclone Shelter",
        odiaName = "ସୋରୋ ଉଚ୍ଚ ବିଦ୍ୟାଳୟ ବାତ୍ୟା ଆଶ୍ରୟ କେନ୍ଦ୍ର",
        block = "Soro",
        capacity = 1300,
        phone = "06782281200",
        locationDesc = "Soro Town, Balasore",
        elevationMeters = 7.8,
        hasSolarBackup = true,
        latitude = 21.2850,
        longitude = 86.6920,
        distanceKm = 38.0
    ),
    BalasoreMapItem.CycloneShelterSpot(
        id = "cs_8",
        name = "Nilagiri Hill-Slope Disaster Relief Center",
        odiaName = "ନୀଳଗିରି ପାହାଡ଼ ତଳ ବିପର୍ଯ୍ୟୟ କେନ୍ଦ୍ର",
        block = "Nilagiri",
        capacity = 1200,
        phone = "06782233215",
        locationDesc = "Near Nilagiri Palace, Nilagiri",
        elevationMeters = 18.5,
        hasSolarBackup = true,
        latitude = 21.4550,
        longitude = 86.7620,
        distanceKm = 22.0
    ),
    BalasoreMapItem.CycloneShelterSpot(
        id = "cs_9",
        name = "Jaleswar Subarnarekha Multi-purpose Shelter",
        odiaName = "ଜଳେଶ୍ୱର ସୁବର୍ଣ୍ଣରେଖା ବହୁମୁଖୀ ଆଶ୍ରୟସ୍ଥଳ",
        block = "Jaleswar",
        capacity = 1700,
        phone = "06781222400",
        locationDesc = "Station Road, Jaleswar",
        elevationMeters = 8.2,
        hasSolarBackup = true,
        latitude = 21.8020,
        longitude = 87.2150,
        distanceKm = 52.0
    ),
    BalasoreMapItem.CycloneShelterSpot(
        id = "cs_10",
        name = "Simulia Canal Embankment Relief Shelter",
        odiaName = "ସିମୁଳିଆ କେନାଲ ବନ୍ଧ ରିଲିଫ ଆଶ୍ରୟସ୍ଥଳ",
        block = "Simulia",
        capacity = 1100,
        phone = "06782294100",
        locationDesc = "Simulia Block Headquarters",
        elevationMeters = 7.2,
        hasSolarBackup = false,
        latitude = 21.1550,
        longitude = 86.5820,
        distanceKm = 46.0
    )
)

/**
 * Builds Tourist Hotspot map items from DefaultData repository.
 */
fun buildTouristSpotsMapData(): List<BalasoreMapItem.TouristSpot> {
    val initialHotspots = DefaultData.getInitialHotspots()
    return initialHotspots.map { spot ->
        BalasoreMapItem.TouristSpot(
            id = spot.id,
            name = spot.name,
            odiaName = spot.odiaName,
            category = spot.category,
            shortDesc = spot.shortDescription,
            timings = spot.timings,
            highlights = spot.highlights,
            latitude = spot.latitude,
            longitude = spot.longitude,
            distanceKm = spot.distanceKmFromBls.toDouble()
        )
    }
}

/**
 * Google Maps view integrating real-time markers for cyclone shelters and major tourist hotspots in Balasore.
 * Includes a smooth camera zoom animation to center the view whenever a marker or card is clicked.
 */
@Composable
fun HotspotsMapView(
    hotspots: List<HotspotEntity> = emptyList(),
    selectedHotspot: HotspotEntity? = null,
    onSelectHotspot: (HotspotEntity?) -> Unit = {},
    onViewHotspotDetails: (HotspotEntity) -> Unit = {},
    language: AppLanguage = AppLanguage.ENGLISH,
    onClose: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    // Markers state
    val touristSpots = remember { buildTouristSpotsMapData() }
    val cycloneShelters = remember { defaultCycloneSheltersMapData }

    var selectedCategory by remember { mutableStateOf("All") }
    var selectedItem by remember { mutableStateOf<BalasoreMapItem?>(null) }
    var googleMapInstance by remember { mutableStateOf<GoogleMap?>(null) }
    var currentMapType by remember { mutableStateOf(GoogleMap.MAP_TYPE_NORMAL) }

    // Map marker tracking
    val markerToItemMap = remember { mutableStateMapOf<Marker, BalasoreMapItem>() }

    // Filtered items
    val filteredItems = remember(selectedCategory, touristSpots, cycloneShelters) {
        when (selectedCategory) {
            "All" -> touristSpots + cycloneShelters
            "Tourist Hotspots" -> touristSpots
            "Cyclone Shelters" -> cycloneShelters
            "Beach" -> touristSpots.filter { it.category.contains("Beach", ignoreCase = true) }
            "Temple" -> touristSpots.filter { it.category.contains("Temple", ignoreCase = true) }
            "Nature & Heritage" -> touristSpots.filter { !it.category.contains("Beach", ignoreCase = true) && !it.category.contains("Temple", ignoreCase = true) }
            else -> touristSpots + cycloneShelters
        }
    }

    // Initialize MapView lifecycle
    val mapView = remember {
        MapView(context).apply {
            onCreate(Bundle())
        }
    }

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

    /**
     * Helper to perform smooth camera zoom animation to a specified LatLng with desired zoom level.
     */
    fun animateCameraToLocation(lat: Double, lng: Double, zoom: Float = 15.5f) {
        googleMapInstance?.let { map ->
            val target = LatLng(lat, lng)
            val cameraUpdate = CameraUpdateFactory.newLatLngZoom(target, zoom)
            map.animateCamera(cameraUpdate, 900, null)
        }
    }

    // Refresh markers on the Google Map whenever filteredItems or map instance changes
    LaunchedEffect(googleMapInstance, filteredItems) {
        val map = googleMapInstance ?: return@LaunchedEffect
        map.clear()
        markerToItemMap.clear()

        filteredItems.forEach { item ->
            val latLng = LatLng(item.latitude, item.longitude)
            val markerOptions = MarkerOptions().position(latLng)
                .title(item.name)
                .snippet(
                    if (item is BalasoreMapItem.CycloneShelterSpot) {
                        "Cyclone Shelter (Cap: ${item.capacity}) • ${item.block}"
                    } else if (item is BalasoreMapItem.TouristSpot) {
                        "${item.category} • ${item.shortDesc.take(45)}..."
                    } else ""
                )

            if (item.isShelter) {
                // High-visibility Orange/Red marker for cyclone emergency shelters
                markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
            } else {
                // Azure / Blue marker for tourist destinations and heritage sites
                markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
            }

            val marker = map.addMarker(markerOptions)
            if (marker != null) {
                markerToItemMap[marker] = item
            }
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .testTag("balasore_google_map_container")
    ) {
        // 1. Interactive Google Map View via AndroidView
        AndroidView(
            factory = {
                mapView.apply {
                    getMapAsync { map ->
                        googleMapInstance = map
                        map.mapType = currentMapType
                        map.uiSettings.isZoomControlsEnabled = false
                        map.uiSettings.isCompassEnabled = true
                        map.uiSettings.isMapToolbarEnabled = true
                        map.uiSettings.isMyLocationButtonEnabled = false

                        // Center view on Balasore district town
                        val centerBalasore = LatLng(21.4934, 86.9135)
                        map.moveCamera(CameraUpdateFactory.newLatLngZoom(centerBalasore, 11.2f))

                        // Smooth camera zoom animation on marker click:
                        map.setOnMarkerClickListener { clickedMarker ->
                            val item = markerToItemMap[clickedMarker]
                            if (item != null) {
                                selectedItem = item
                                // Smooth camera zoom animation centering the view on the clicked location
                                animateCameraToLocation(item.latitude, item.longitude, 15.5f)
                            }
                            // Show standard info window as well
                            false
                        }

                        map.setOnMapClickListener {
                            selectedItem = null
                        }
                    }
                }
            },
            modifier = Modifier.fillMaxSize()
        )

        // 2. Top Header & Category Filter Overlay
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopCenter)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color.Black.copy(alpha = 0.75f), Color.Transparent)
                    )
                )
                .padding(top = 16.dp, bottom = 8.dp)
        ) {
            // Header Row with Title, Counts & Close
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            shape = CircleShape,
                            color = OceanBlue,
                            modifier = Modifier.size(28.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Default.Explore,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (language == AppLanguage.ODIA) "ବାଲେଶ୍ୱର ମାନଚିତ୍ର" else "Balasore Interactive Map",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.ExtraBold,
                                color = Color.White
                            )
                        )
                    }
                    Text(
                        text = "${cycloneShelters.size} Shelters • ${touristSpots.size} Hotspots Active",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = Color.White.copy(alpha = 0.85f),
                            fontSize = 11.sp
                        ),
                        modifier = Modifier.padding(start = 36.dp)
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    // Map Type Toggle Button (Normal / Satellite)
                    IconButton(
                        onClick = {
                            val nextType = if (currentMapType == GoogleMap.MAP_TYPE_NORMAL) {
                                GoogleMap.MAP_TYPE_HYBRID
                            } else {
                                GoogleMap.MAP_TYPE_NORMAL
                            }
                            currentMapType = nextType
                            googleMapInstance?.mapType = nextType
                        },
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = 0.2f))
                    ) {
                        Icon(
                            imageVector = Icons.Default.Layers,
                            contentDescription = "Toggle Map Type",
                            tint = Color.White,
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    IconButton(
                        onClick = onClose,
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = 0.2f))
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close Map",
                            tint = Color.White,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Filter Chips Carousel
            val categories = listOf(
                "All",
                "Tourist Hotspots",
                "Cyclone Shelters",
                "Beach",
                "Temple",
                "Nature & Heritage"
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                categories.forEach { category ->
                    val isSelected = selectedCategory == category
                    FilterChip(
                        selected = isSelected,
                        onClick = {
                            selectedCategory = category
                            selectedItem = null
                        },
                        label = {
                            Text(
                                text = category,
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                                )
                            )
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            containerColor = Color.White.copy(alpha = 0.2f),
                            labelColor = Color.White,
                            selectedContainerColor = if (category == "Cyclone Shelters") AmberGold else OceanBlue,
                            selectedLabelColor = Color.White
                        ),
                        border = BorderStroke(
                            1.dp,
                            if (isSelected) Color.White else Color.White.copy(alpha = 0.3f)
                        )
                    )
                }
            }
        }

        // 3. Floating Map Controls (Zoom In, Zoom Out, Recenter)
        Column(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .padding(end = 16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // Recenter Balasore Town
            FloatingActionButton(
                onClick = {
                    animateCameraToLocation(21.4934, 86.9135, 11.2f)
                    Toast.makeText(context, "Centered on Balasore Town", Toast.LENGTH_SHORT).show()
                },
                modifier = Modifier
                    .size(44.dp)
                    .testTag("map_recenter_button"),
                shape = CircleShape,
                containerColor = Color.White,
                contentColor = OceanBlueDark,
                elevation = FloatingActionButtonDefaults.elevation(4.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.MyLocation,
                    contentDescription = "Center Balasore",
                    modifier = Modifier.size(20.dp)
                )
            }

            // Smooth Zoom In (+)
            FloatingActionButton(
                onClick = {
                    googleMapInstance?.animateCamera(CameraUpdateFactory.zoomIn(), 400, null)
                },
                modifier = Modifier
                    .size(44.dp)
                    .testTag("map_zoom_in_button"),
                shape = CircleShape,
                containerColor = Color.White,
                contentColor = BentoSlate900,
                elevation = FloatingActionButtonDefaults.elevation(4.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Zoom In",
                    modifier = Modifier.size(20.dp)
                )
            }

            // Smooth Zoom Out (-)
            FloatingActionButton(
                onClick = {
                    googleMapInstance?.animateCamera(CameraUpdateFactory.zoomOut(), 400, null)
                },
                modifier = Modifier
                    .size(44.dp)
                    .testTag("map_zoom_out_button"),
                shape = CircleShape,
                containerColor = Color.White,
                contentColor = BentoSlate900,
                elevation = FloatingActionButtonDefaults.elevation(4.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Remove,
                    contentDescription = "Zoom Out",
                    modifier = Modifier.size(20.dp)
                )
            }
        }

        // 4. Bottom Information Card when a Marker is Clicked
        AnimatedVisibility(
            visible = selectedItem != null,
            enter = slideInVertically(initialOffsetY = { it }) + fadeIn(),
            exit = slideOutVertically(targetOffsetY = { it }) + fadeOut(),
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(16.dp)
        ) {
            selectedItem?.let { item ->
                Card(
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    border = BorderStroke(
                        1.5.dp,
                        if (item.isShelter) AmberGold.copy(alpha = 0.5f) else BentoPrimaryBlue.copy(alpha = 0.3f)
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(12.dp, RoundedCornerShape(24.dp))
                        .testTag("selected_marker_detail_card")
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(18.dp)
                    ) {
                        // Header Row: Type Badge + Distance + Close
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = if (item.isShelter) AmberGold.copy(alpha = 0.15f) else OceanBlue.copy(alpha = 0.12f),
                                border = BorderStroke(
                                    1.dp,
                                    if (item.isShelter) AmberGold.copy(alpha = 0.4f) else OceanBlue.copy(alpha = 0.3f)
                                )
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = if (item.isShelter) Icons.Default.Shield else Icons.Default.Place,
                                        contentDescription = null,
                                        tint = if (item.isShelter) AmberGold else OceanBlue,
                                        modifier = Modifier.size(13.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = if (item.isShelter) "CYCLONE SHELTER" else (item as BalasoreMapItem.TouristSpot).category.uppercase(),
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            fontWeight = FontWeight.ExtraBold,
                                            fontSize = 10.sp,
                                            color = if (item.isShelter) AmberGold else OceanBlue
                                        )
                                    )
                                }
                            }

                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Surface(
                                    shape = RoundedCornerShape(6.dp),
                                    color = BentoSlate200.copy(alpha = 0.6f)
                                ) {
                                    Text(
                                        text = "${item.distanceKm} km from town",
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            color = BentoSlate700,
                                            fontWeight = FontWeight.SemiBold,
                                            fontSize = 10.sp
                                        ),
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                                    )
                                }

                                Spacer(modifier = Modifier.width(6.dp))

                                IconButton(
                                    onClick = { selectedItem = null },
                                    modifier = Modifier.size(24.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Close,
                                        contentDescription = "Dismiss",
                                        tint = BentoSlate500,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        // Title & Odia Name
                        Text(
                            text = item.name,
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = BentoSlate900
                            )
                        )
                        Text(
                            text = item.odiaName,
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = OceanBlue,
                                fontWeight = FontWeight.Medium
                            )
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        // Detail specs depending on Shelter or Tourist Spot
                        if (item is BalasoreMapItem.CycloneShelterSpot) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = Color(0xFFFEF3C7),
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Column(modifier = Modifier.padding(8.dp)) {
                                        Text("Capacity", fontSize = 10.sp, color = BentoSlate600)
                                        Text("${item.capacity} People", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = BentoSlate900)
                                    }
                                }
                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = Color(0xFFF1F5F9),
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Column(modifier = Modifier.padding(8.dp)) {
                                        Text("Block / Elev", fontSize = 10.sp, color = BentoSlate600)
                                        Text("${item.block} • +${item.elevationMeters}m", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = BentoSlate900, maxLines = 1)
                                    }
                                }
                            }
                        } else if (item is BalasoreMapItem.TouristSpot) {
                            Text(
                                text = item.shortDesc,
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = BentoSlate600,
                                    fontSize = 12.sp
                                ),
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis
                            )
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // Action Buttons: Directions, Call / Details, Smooth Zoom
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            // Directions via Google Maps Intent
                            Button(
                                onClick = {
                                    val uri = Uri.parse("geo:${item.latitude},${item.longitude}?q=${item.latitude},${item.longitude}(${Uri.encode(item.name)})")
                                    val mapIntent = Intent(Intent.ACTION_VIEW, uri)
                                    try {
                                        context.startActivity(mapIntent)
                                    } catch (_: Exception) {
                                        val webUri = Uri.parse("https://www.google.com/maps/search/?api=1&query=${item.latitude},${item.longitude}")
                                        context.startActivity(Intent(Intent.ACTION_VIEW, webUri))
                                    }
                                },
                                modifier = Modifier.weight(1f),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (item.isShelter) AmberGold else OceanBlue
                                ),
                                shape = RoundedCornerShape(12.dp),
                                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Directions,
                                    contentDescription = null,
                                    modifier = Modifier.size(16.dp),
                                    tint = Color.White
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Directions", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.White)
                            }

                            // If Cyclone shelter: direct Call button
                            if (item is BalasoreMapItem.CycloneShelterSpot && item.phone.isNotBlank()) {
                                OutlinedButton(
                                    onClick = {
                                        val dialIntent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:${item.phone}"))
                                        context.startActivity(dialIntent)
                                    },
                                    modifier = Modifier.weight(1f),
                                    shape = RoundedCornerShape(12.dp),
                                    border = BorderStroke(1.dp, EmeraldGreen),
                                    colors = ButtonDefaults.outlinedButtonColors(contentColor = EmeraldGreen),
                                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 8.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Call,
                                        contentDescription = null,
                                        modifier = Modifier.size(15.dp),
                                        tint = EmeraldGreen
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("Call Control", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                }
                            } else {
                                // Smooth Zoom In & Center Button
                                OutlinedButton(
                                    onClick = {
                                        animateCameraToLocation(item.latitude, item.longitude, 16.5f)
                                    },
                                    modifier = Modifier.weight(1f),
                                    shape = RoundedCornerShape(12.dp),
                                    border = BorderStroke(1.dp, BentoPrimaryBlue),
                                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 8.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.NearMe,
                                        contentDescription = null,
                                        modifier = Modifier.size(15.dp),
                                        tint = OceanBlue
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("Zoom Closer", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = OceanBlue)
                                }
                            }
                        }
                    }
                }
            }
        }

        // 5. Quick Horizontal Carousel at bottom when no marker is selected
        if (selectedItem == null) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.6f))
                        )
                    )
                    .padding(bottom = 16.dp, top = 8.dp)
            ) {
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(filteredItems, key = { it.id }) { item ->
                        Surface(
                            shape = RoundedCornerShape(14.dp),
                            color = Color.White,
                            border = BorderStroke(
                                1.dp,
                                if (item.isShelter) AmberGold.copy(alpha = 0.5f) else BentoSlate200
                            ),
                            shadowElevation = 3.dp,
                            modifier = Modifier
                                .width(210.dp)
                                .clickable {
                                    selectedItem = item
                                    // Smooth camera zoom animation centering the view on the selected card
                                    animateCameraToLocation(item.latitude, item.longitude, 15.5f)
                                }
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Surface(
                                        shape = RoundedCornerShape(6.dp),
                                        color = if (item.isShelter) AmberGold.copy(alpha = 0.15f) else OceanBlue.copy(alpha = 0.12f)
                                    ) {
                                        Text(
                                            text = if (item.isShelter) "SHELTER" else (item as BalasoreMapItem.TouristSpot).category,
                                            fontSize = 9.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = if (item.isShelter) AmberGold else OceanBlue,
                                            modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp)
                                        )
                                    }
                                    Text(
                                        text = "${item.distanceKm} km",
                                        fontSize = 10.sp,
                                        color = BentoSlate500,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = item.name,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = BentoSlate900,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Text(
                                    text = "Tap to zoom on map",
                                    fontSize = 10.sp,
                                    color = OceanBlue
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
