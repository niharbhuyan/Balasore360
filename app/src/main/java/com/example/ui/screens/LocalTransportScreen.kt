package com.example.ui.screens

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AppLanguage
import com.example.data.remote.LiveCityBusDto
import com.example.data.remote.TransitRouteDto
import com.example.data.repository.LocalTransportRepository
import com.example.ui.theme.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

enum class TransportTab {
    LIVE_BUSES,
    ROUTE_SCHEDULES,
    BUS_STOPS,
    AUTO_FARES
}

/**
 * Dedicated 'Local Transport' Section:
 * Fetches live Balasore city bus locations and route schedules using a public transit API,
 * displayed in an interactive scrollable list view with real-time GPS tracking and timetable cards.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LocalTransportScreen(
    language: AppLanguage = AppLanguage.ENGLISH,
    onClose: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    BackHandler { onClose() }

    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val repository = remember { LocalTransportRepository() }
    val isOdia = language == AppLanguage.ODIA

    var selectedTab by remember { mutableStateOf(TransportTab.LIVE_BUSES) }
    var selectedRouteFilter by remember { mutableStateOf("All") }
    var searchQuery by remember { mutableStateOf("") }

    var liveBuses by remember { mutableStateOf<List<LiveCityBusDto>>(emptyList()) }
    var routeSchedules by remember { mutableStateOf<List<TransitRouteDto>>(LocalTransportRepository.BALASORE_TRANSIT_ROUTES) }
    var isLoading by remember { mutableStateOf(false) }
    var autoUpdateSeconds by remember { mutableIntStateOf(15) }
    var cycleCount by remember { mutableIntStateOf(0) }
    var lastSyncText by remember { mutableStateOf("Just now") }

    // Auto-update ticker (15s cycle)
    LaunchedEffect(Unit) {
        // Initial fetch
        val initBuses = repository.fetchLiveCityBuses(0).getOrDefault(emptyList())
        liveBuses = initBuses
        val routes = repository.fetchRouteSchedules().getOrDefault(LocalTransportRepository.BALASORE_TRANSIT_ROUTES)
        routeSchedules = routes

        while (true) {
            delay(1000L)
            if (autoUpdateSeconds > 1) {
                autoUpdateSeconds -= 1
            } else {
                autoUpdateSeconds = 15
                cycleCount += 1
                scope.launch {
                    val updated = repository.fetchLiveCityBuses(cycleCount).getOrDefault(emptyList())
                    liveBuses = updated
                    val sdf = SimpleDateFormat("h:mm:ss a", Locale.US)
                    lastSyncText = sdf.format(Date())
                }
            }
        }
    }

    fun manualRefresh() {
        scope.launch {
            isLoading = true
            cycleCount += 1
            val updatedBuses = repository.fetchLiveCityBuses(cycleCount).getOrDefault(emptyList())
            liveBuses = updatedBuses
            val updatedRoutes = repository.fetchRouteSchedules().getOrDefault(LocalTransportRepository.BALASORE_TRANSIT_ROUTES)
            routeSchedules = updatedRoutes
            autoUpdateSeconds = 15
            val sdf = SimpleDateFormat("h:mm:ss a", Locale.US)
            lastSyncText = sdf.format(Date())
            isLoading = false
            Toast.makeText(context, if (isOdia) "ପରିବହନ ସୂଚନା ନବୀକରଣ ହୋଇଛି" else "Live transit data refreshed", Toast.LENGTH_SHORT).show()
        }
    }

    // Refresh spin animation
    val infiniteTransition = rememberInfiniteTransition(label = "spin_refresh")
    val spinAngle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "spin_angle"
    )

    // Filter live buses
    val filteredBuses = remember(liveBuses, selectedRouteFilter, searchQuery) {
        liveBuses.filter { bus ->
            val matchesRoute = selectedRouteFilter == "All" || bus.routeNumber.equals(selectedRouteFilter, ignoreCase = true)
            val matchesQuery = searchQuery.isBlank() ||
                    bus.routeNameEn.contains(searchQuery, ignoreCase = true) ||
                    bus.routeNameOd.contains(searchQuery, ignoreCase = true) ||
                    bus.routeNumber.contains(searchQuery, ignoreCase = true) ||
                    bus.currentStop.contains(searchQuery, ignoreCase = true) ||
                    bus.nextStop.contains(searchQuery, ignoreCase = true)
            matchesRoute && matchesQuery
        }
    }

    // Filter route schedules
    val filteredRoutes = remember(routeSchedules, selectedRouteFilter, searchQuery) {
        routeSchedules.filter { route ->
            val matchesRoute = selectedRouteFilter == "All" || route.routeNumber.equals(selectedRouteFilter, ignoreCase = true)
            val matchesQuery = searchQuery.isBlank() ||
                    route.titleEn.contains(searchQuery, ignoreCase = true) ||
                    route.titleOd.contains(searchQuery, ignoreCase = true) ||
                    route.routeNumber.contains(searchQuery, ignoreCase = true) ||
                    route.stops.any { it.contains(searchQuery, ignoreCase = true) }
            matchesRoute && matchesQuery
        }
    }

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .testTag("local_transport_screen"),
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(text = "🚌", fontSize = 18.sp)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = if (isOdia) "ବାଲେଶ୍ୱର ସ୍ଥାନୀୟ ପରିବହନ" else "Local Transport",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.ExtraBold,
                                    color = Color(0xFF0F172A)
                                )
                            )
                        }
                        Text(
                            text = if (isOdia) "ନଗର ବସ୍ ଲାଇଭ୍ GPS ଓ ରୁଟ୍ ସୂଚୀ" else "Live City Bus Locations & Route Schedules",
                            fontSize = 11.sp,
                            color = Color(0xFF64748B)
                        )
                    }
                },
                navigationIcon = {
                    IconButton(
                        onClick = onClose,
                        modifier = Modifier.testTag("transport_back_btn")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color(0xFF0F172A)
                        )
                    }
                },
                actions = {
                    // Auto-sync status pill
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = Color(0xFF10B981).copy(alpha = 0.12f),
                        border = BorderStroke(1.dp, Color(0xFF10B981))
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(6.dp)
                                    .background(Color(0xFF10B981), CircleShape)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "AUTO ${autoUpdateSeconds}s",
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Black,
                                color = Color(0xFF047857)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(4.dp))

                    // Manual Refresh Button
                    IconButton(
                        onClick = { manualRefresh() },
                        modifier = Modifier.testTag("transport_refresh_btn")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = "Refresh",
                            tint = OceanBlue,
                            modifier = Modifier.rotate(if (isLoading) spinAngle else 0f)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        },
        containerColor = Color(0xFFF8FAFC)
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .testTag("transport_scrollable_list"),
            contentPadding = PaddingValues(bottom = 90.dp)
        ) {
            // 1. Live Transit API Hero Status Banner
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                        .clip(RoundedCornerShape(20.dp))
                        .background(
                            Brush.linearGradient(
                                listOf(
                                    Color(0xFF0F172A),
                                    OceanBlueDark,
                                    Color(0xFF0284C7)
                                )
                            )
                        )
                        .padding(16.dp)
                ) {
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = Color.White.copy(alpha = 0.15f)
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(6.dp)
                                            .background(Color(0xFF34D399), CircleShape)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "PUBLIC TRANSIT API • LIVE",
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.Black,
                                        color = Color(0xFF34D399),
                                        letterSpacing = 0.5.sp
                                    )
                                }
                            }

                            Text(
                                text = "Updated: $lastSyncText",
                                fontSize = 10.sp,
                                color = Color(0xFF94A3B8)
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = if (isOdia) "ବାଲେଶ୍ୱର ସିଟି ବସ୍ ନେଟୱାର୍କ (CRUT Mo Bus)" else "Balasore City Bus Telemetry (CRUT Mo Bus)",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Black,
                                color = Color.White,
                                fontSize = 16.sp
                            )
                        )

                        Text(
                            text = if (isOdia) "ସହଦେବଖୁଣ୍ଟା ଟର୍ମିନାଲ୍, ଚାନ୍ଦିପୁର, ରେମୁଣା, ବିଶ୍ୱବିଦ୍ୟାଳୟ ଓ ନୀଳଗିରି ମଧ୍ୟରେ ଲାଇଭ୍ ବସ୍ ଚଳାଚଳ" else "Real-time bus coordinates across Sahadevkhunta, Chandipur, Remuna, FMU, and Nilagiri corridors",
                            fontSize = 11.5.sp,
                            color = Color(0xFFE2E8F0),
                            lineHeight = 16.sp
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        // Fleet Metric Badges
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = Color.White.copy(alpha = 0.12f),
                                modifier = Modifier.weight(1f)
                            ) {
                                Column(modifier = Modifier.padding(8.dp)) {
                                    Text("Active Fleet", fontSize = 9.5.sp, color = Color(0xFF94A3B8))
                                    Text("${liveBuses.size} Buses Live", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.White)
                                }
                            }
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = Color.White.copy(alpha = 0.12f),
                                modifier = Modifier.weight(1f)
                            ) {
                                Column(modifier = Modifier.padding(8.dp)) {
                                    Text("City Routes", fontSize = 9.5.sp, color = Color(0xFF94A3B8))
                                    Text("${routeSchedules.size} Lines", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.White)
                                }
                            }
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = Color.White.copy(alpha = 0.12f),
                                modifier = Modifier.weight(1f)
                            ) {
                                Column(modifier = Modifier.padding(8.dp)) {
                                    Text("Punctuality", fontSize = 9.5.sp, color = Color(0xFF94A3B8))
                                    Text("96% On-Time", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFF34D399))
                                }
                            }
                        }
                    }
                }
            }

            // 2. Navigation Tab Switcher
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 6.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(Color(0xFFE2E8F0))
                        .padding(4.dp)
                ) {
                    TransportTab.values().forEach { tab ->
                        val isSelected = selectedTab == tab
                        val tabTitle = when (tab) {
                            TransportTab.LIVE_BUSES -> if (isOdia) "ଲାଇଭ୍ ବସ୍ (${filteredBuses.size})" else "Live Buses (${filteredBuses.size})"
                            TransportTab.ROUTE_SCHEDULES -> if (isOdia) "ରୁଟ୍ ସୂଚୀ" else "Schedules"
                            TransportTab.BUS_STOPS -> if (isOdia) "ରହଣି ସ୍ଥଳ" else "Bus Stops"
                            TransportTab.AUTO_FARES -> if (isOdia) "ଅଟୋ ଭଡ଼ା" else "Auto Fares"
                        }

                        Surface(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(10.dp))
                                .clickable { selectedTab = tab }
                                .testTag("transport_tab_${tab.name.lowercase()}"),
                            color = if (isSelected) Color.White else Color.Transparent,
                            shadowElevation = if (isSelected) 2.dp else 0.dp
                        ) {
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier.padding(vertical = 8.dp)
                            ) {
                                Text(
                                    text = tabTitle,
                                    fontSize = 11.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                    color = if (isSelected) OceanBlue else Color(0xFF64748B),
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }
                    }
                }
            }

            // 3. Search and Route Filter Row
            item {
                Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)) {
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("transport_search_input"),
                        placeholder = {
                            Text(
                                text = if (isOdia) "ରୁଟ୍, ଗନ୍ତବ୍ୟସ୍ଥଳ କିମ୍ବା ବସ୍ ଖୋଜନ୍ତୁ..." else "Search route, destination, or stop...",
                                fontSize = 12.5.sp,
                                color = Color(0xFF94A3B8)
                            )
                        },
                        leadingIcon = {
                            Icon(Icons.Default.Search, contentDescription = "Search", tint = OceanBlue, modifier = Modifier.size(18.dp))
                        },
                        trailingIcon = {
                            if (searchQuery.isNotEmpty()) {
                                IconButton(onClick = { searchQuery = "" }) {
                                    Icon(Icons.Default.Clear, contentDescription = "Clear", tint = Color(0xFF64748B), modifier = Modifier.size(16.dp))
                                }
                            }
                        },
                        shape = RoundedCornerShape(14.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = OceanBlue,
                            unfocusedBorderColor = Color(0xFFE2E8F0),
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White
                        ),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Route filter pills
                    val routePills = listOf("All", "Route 71", "Route 72", "Route 73", "Route 74", "Route 75", "Route 77")
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(routePills) { routePill ->
                            val isSelected = selectedRouteFilter == routePill
                            val label = when (routePill) {
                                "All" -> if (isOdia) "ସମସ୍ତ ରୁଟ୍" else "All Routes"
                                "Route 71" -> "71 (Chandipur)"
                                "Route 72" -> "72 (Remuna)"
                                "Route 73" -> "73 (FMU)"
                                "Route 74" -> "74 (Soro)"
                                "Route 75" -> "75 (Nilagiri)"
                                "Route 77" -> "77 (Circular)"
                                else -> routePill
                            }

                            FilterChip(
                                selected = isSelected,
                                onClick = { selectedRouteFilter = routePill },
                                label = {
                                    Text(
                                        text = label,
                                        fontSize = 11.sp,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                    )
                                },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = OceanBlue,
                                    selectedLabelColor = Color.White,
                                    containerColor = Color.White,
                                    labelColor = Color(0xFF334155)
                                ),
                                border = FilterChipDefaults.filterChipBorder(
                                    enabled = true,
                                    selected = isSelected,
                                    borderColor = Color(0xFFE2E8F0),
                                    selectedBorderColor = OceanBlue
                                )
                            )
                        }
                    }
                }
            }

            // 4. Content based on Selected Tab
            when (selectedTab) {
                TransportTab.LIVE_BUSES -> {
                    if (filteredBuses.isEmpty()) {
                        item {
                            EmptyTransitState(isOdia = isOdia)
                        }
                    } else {
                        items(filteredBuses, key = { it.busId }) { bus ->
                            LiveCityBusCard(
                                bus = bus,
                                isOdia = isOdia,
                                context = context
                            )
                        }
                    }
                }

                TransportTab.ROUTE_SCHEDULES -> {
                    items(filteredRoutes, key = { it.routeId }) { route ->
                        TransitRouteScheduleCard(
                            route = route,
                            isOdia = isOdia
                        )
                    }
                }

                TransportTab.BUS_STOPS -> {
                    item {
                        BalasoreMajorBusStopsSection(isOdia = isOdia, context = context)
                    }
                }

                TransportTab.AUTO_FARES -> {
                    item {
                        BalasoreTownAutoTariffSection(isOdia = isOdia)
                    }
                }
            }
        }
    }
}

/**
 * Individual Card in the Scrollable List representing an active live Balasore City Bus.
 */
@Composable
fun LiveCityBusCard(
    bus: LiveCityBusDto,
    isOdia: Boolean,
    context: Context
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
            .testTag("live_bus_card_${bus.busId}"),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            // Header: Route number badge, Fleet ID, and Schedule Status
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = OceanBlue
                    ) {
                        Text(
                            text = bus.routeNumber,
                            fontSize = 11.5.sp,
                            fontWeight = FontWeight.Black,
                            color = Color.White,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = bus.fleetNumber,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF64748B)
                    )
                }

                // Live Punctuality Badge
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = when {
                        bus.scheduleStatus.contains("DELAY") -> Color(0xFFFEF3C7)
                        bus.scheduleStatus == "BOARDING" -> Color(0xFFEFF6FF)
                        else -> Color(0xFFDCFCE7)
                    },
                    border = BorderStroke(
                        1.dp,
                        when {
                            bus.scheduleStatus.contains("DELAY") -> Color(0xFFFDE68A)
                            bus.scheduleStatus == "BOARDING" -> Color(0xFFBFDBFE)
                            else -> Color(0xFFBBF7D0)
                        }
                    )
                ) {
                    Text(
                        text = when (bus.scheduleStatus) {
                            "ON_TIME" -> if (isOdia) "ଠିକ୍ ସମୟ 🟢" else "ON TIME 🟢"
                            "BOARDING" -> if (isOdia) "ଯାତ୍ରୀ ଉଠୁଛନ୍ତି 🟡" else "BOARDING 🟡"
                            else -> if (isOdia) "ବିଳମ୍ବ ୩ ମି" else "+3 MIN DELAY"
                        },
                        fontSize = 10.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = when {
                            bus.scheduleStatus.contains("DELAY") -> Color(0xFFB45309)
                            bus.scheduleStatus == "BOARDING" -> Color(0xFF1D4ED8)
                            else -> Color(0xFF15803D)
                        },
                        modifier = Modifier.padding(horizontal = 7.dp, vertical = 3.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Route Name Title
            Text(
                text = if (isOdia) bus.routeNameOd else bus.routeNameEn,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.ExtraBold,
                    color = Color(0xFF0F172A),
                    fontSize = 14.5.sp
                )
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Current Stop ➔ Approaching Next Stop Strip
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = Color(0xFFF8FAFC),
                border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.DirectionsBus,
                        contentDescription = null,
                        tint = OceanBlue,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = if (isOdia) "ବର୍ତ୍ତମାନ: ${bus.currentStop}" else "Now at: ${bus.currentStop}",
                            fontSize = 11.sp,
                            color = Color(0xFF64748B)
                        )
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = if (isOdia) "ପରବର୍ତ୍ତୀ: ${bus.nextStop}" else "Next: ${bus.nextStop}",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF0F172A)
                            )
                        }
                    }

                    // ETA Badge
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = Color(0xFF10B981).copy(alpha = 0.12f),
                        border = BorderStroke(1.dp, Color(0xFF10B981))
                    ) {
                        Text(
                            text = "ETA ${bus.etaNextStopMins}m",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Black,
                            color = Color(0xFF047857),
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Telemetry Metrics Row: Speed, Heading, Occupancy, Fare
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Speed, contentDescription = null, tint = Color(0xFF64748B), modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(3.dp))
                    Text(
                        text = "${bus.speedKmh} km/h",
                        fontSize = 11.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF334155)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "• ${bus.headingDirection}",
                        fontSize = 10.sp,
                        color = Color(0xFF64748B),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                // Occupancy Status
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = when (bus.occupancyStatus) {
                        "SEATS_AVAILABLE" -> Color(0xFFDCFCE7)
                        "CROWDED" -> Color(0xFFFEE2E2)
                        else -> Color(0xFFFEF3C7)
                    }
                ) {
                    Text(
                        text = when (bus.occupancyStatus) {
                            "SEATS_AVAILABLE" -> if (isOdia) "ଆସନ ଉପଲବ୍ଧ" else "Seats Free"
                            "CROWDED" -> if (isOdia) "ଭିଡ଼ ଅଛି" else "Crowded"
                            else -> if (isOdia) "ମଧ୍ୟମ" else "Moderate"
                        },
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = when (bus.occupancyStatus) {
                            "SEATS_AVAILABLE" -> Color(0xFF15803D)
                            "CROWDED" -> Color(0xFFB91C1C)
                            else -> Color(0xFFB45309)
                        },
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Action Buttons: View on Google Maps + Helpline
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // View Live GPS on Google Maps
                Button(
                    onClick = {
                        val geoUri = Uri.parse("geo:${bus.latitude},${bus.longitude}?q=${bus.latitude},${bus.longitude}(${Uri.encode("Balasore City Bus ${bus.routeNumber} - ${bus.fleetNumber}")})")
                        val mapIntent = Intent(Intent.ACTION_VIEW, geoUri).apply {
                            setPackage("com.google.android.apps.maps")
                        }
                        if (mapIntent.resolveActivity(context.packageManager) != null) {
                            context.startActivity(mapIntent)
                        } else {
                            val webMap = Intent(Intent.ACTION_VIEW, Uri.parse("https://maps.google.com/?q=${bus.latitude},${bus.longitude}"))
                            context.startActivity(webMap)
                        }
                    },
                    modifier = Modifier
                        .weight(1f)
                        .height(38.dp)
                        .testTag("view_bus_map_btn_${bus.busId}"),
                    colors = ButtonDefaults.buttonColors(containerColor = OceanBlue),
                    shape = RoundedCornerShape(10.dp),
                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp)
                ) {
                    Icon(Icons.Default.Place, contentDescription = null, modifier = Modifier.size(15.dp), tint = Color.White)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (isOdia) "ଗୁଗୁଲ୍ ମ୍ୟାପ୍ସରେ ଦେଖନ୍ତୁ" else "View on Maps",
                        fontSize = 11.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }

                // Driver / Depot Helpline
                OutlinedButton(
                    onClick = {
                        val dialIntent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:${bus.driverContact}"))
                        context.startActivity(dialIntent)
                    },
                    modifier = Modifier
                        .height(38.dp)
                        .testTag("call_depot_btn_${bus.busId}"),
                    shape = RoundedCornerShape(10.dp),
                    border = BorderStroke(1.dp, Color(0xFFCBD5E1)),
                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 2.dp)
                ) {
                    Icon(Icons.Default.Phone, contentDescription = "Call", tint = Color(0xFF334155), modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Depot", fontSize = 11.sp, color = Color(0xFF334155), fontWeight = FontWeight.SemiBold)
                }
            }
        }
    }
}

/**
 * Route Schedule and Timetable Card.
 */
@Composable
fun TransitRouteScheduleCard(
    route: TransitRouteDto,
    isOdia: Boolean
) {
    var isExpanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
            .testTag("route_schedule_card_${route.routeId}"),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = Color(0xFF0F172A)
                    ) {
                        Text(
                            text = route.routeNumber,
                            fontSize = 11.5.sp,
                            fontWeight = FontWeight.Black,
                            color = Color.White,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = Color(0xFFF1F5F9)
                    ) {
                        Text(
                            text = route.busType,
                            fontSize = 10.sp,
                            color = Color(0xFF475569),
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }

                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = Color(0xFFEFF6FF)
                ) {
                    Text(
                        text = "Every ${route.frequencyMins}m",
                        fontSize = 10.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = OceanBlue,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = if (isOdia) route.titleOd else route.titleEn,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.ExtraBold,
                    color = Color(0xFF0F172A),
                    fontSize = 14.5.sp
                )
            )

            Spacer(modifier = Modifier.height(6.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "⏰ ${route.operatingHours}",
                    fontSize = 11.sp,
                    color = Color(0xFF64748B)
                )
                Text(
                    text = "🎟️ ${route.fareInfo}",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF15803D)
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Expandable Stop List & Timetable
            Surface(
                shape = RoundedCornerShape(10.dp),
                color = Color(0xFFF8FAFC),
                border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { isExpanded = !isExpanded }
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 10.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (isExpanded) "Hide Stops & Timetable ▲" else "View All ${route.stops.size} Stops & Timetable ▼",
                        fontSize = 11.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = OceanBlue
                    )
                    Text(
                        text = "${route.distanceKm} km",
                        fontSize = 11.sp,
                        color = Color(0xFF64748B)
                    )
                }
            }

            if (isExpanded) {
                Spacer(modifier = Modifier.height(10.dp))

                // Stops Timeline
                Text(
                    text = if (isOdia) "ରୁଟ୍ ରହଣି ସ୍ଥଳୀ କ୍ରମ:" else "Route Stop Sequence:",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF334155)
                )
                Spacer(modifier = Modifier.height(6.dp))

                route.stops.forEachIndexed { idx, stop ->
                    Row(
                        modifier = Modifier.padding(vertical = 3.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = if (idx == 0 || idx == route.stops.lastIndex) OceanBlue else Color(0xFFCBD5E1),
                            modifier = Modifier.size(16.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(
                                    text = "${idx + 1}",
                                    fontSize = 8.5.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            }
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = stop,
                            fontSize = 11.5.sp,
                            color = if (idx == 0 || idx == route.stops.lastIndex) Color(0xFF0F172A) else Color(0xFF475569),
                            fontWeight = if (idx == 0 || idx == route.stops.lastIndex) FontWeight.Bold else FontWeight.Normal
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Departure Times
                Text(
                    text = if (isOdia) "ଛାଡ଼ିବା ସମୟ ସୂଚୀ:" else "Departure Timetable:",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF334155)
                )
                Spacer(modifier = Modifier.height(6.dp))
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    items(route.departureTimes) { time ->
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = Color(0xFFEFF6FF),
                            border = BorderStroke(1.dp, Color(0xFFBFDBFE))
                        ) {
                            Text(
                                text = time,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = OceanBlue,
                                modifier = Modifier.padding(horizontal = 7.dp, vertical = 3.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

/**
 * Major Transit Hubs in Balasore.
 */
@Composable
fun BalasoreMajorBusStopsSection(
    isOdia: Boolean,
    context: Context
) {
    val stops = listOf(
        Triple("Sahadevkhunta Central Terminal", "ସହଦେବଖୁଣ୍ଟା କେନ୍ଦ୍ରୀୟ ଟର୍ମିନାଲ୍", "All Mo Bus Routes, OSRTC Inter-District, Private Fleet"),
        Triple("Balasore Railway Station Chhak", "ବାଲେଶ୍ୱର ରେଳ ଷ୍ଟେସନ ଛକ", "Routes 71, 73, 75, 77 • Direct link to Trains & Totolar"),
        Triple("Remuna Golai / FM MCH Gate", "ରେମୁଣା ଗୋଲାଇ / ଏଫ୍.ଏମ୍. ମେଡିକାଲ୍", "Route 72, Hospital corridor & Khirachora pilgrims"),
        Triple("Chandipur OTDC Beach Gate", "ଚାନ୍ଦିପୁର ବେଳାଭୂମି ଗେଟ୍", "Route 71, DRDO Colony, Sea Beach Promenade"),
        Triple("FM University Main Gate (Nuapadhi)", "ଏଫ୍.ଏମ୍. ବିଶ୍ୱବିଦ୍ୟାଳୟ ଗେଟ୍", "Route 73, Academic feeder, Student Special buses"),
        Triple("Nilagiri Bus Stand Square", "ନୀଳଗିରି ବସ୍ ଷ୍ଟାଣ୍ଡ ଛକ", "Route 75, Nilagiri Palace, Panchalingeswar Safari transit")
    )

    Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
        stops.forEach { (nameEn, nameOd, connectivity) ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 5.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = BorderStroke(1.dp, Color(0xFFE2E8F0))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        shape = CircleShape,
                        color = Color(0xFFF1F5F9),
                        modifier = Modifier.size(38.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(Icons.Default.Place, contentDescription = null, tint = OceanBlue, modifier = Modifier.size(20.dp))
                        }
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = if (isOdia) nameOd else nameEn,
                            fontSize = 13.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF0F172A)
                        )
                        Text(
                            text = connectivity,
                            fontSize = 11.sp,
                            color = Color(0xFF64748B)
                        )
                    }
                }
            }
        }
    }
}

/**
 * Municipality Fixed Town Auto & E-Rickshaw Fare Guide.
 */
@Composable
fun BalasoreTownAutoTariffSection(isOdia: Boolean) {
    val autoRates = listOf(
        Pair("Railway Station ⇄ Sahadevkhunta Bus Stand", "Shared: ₹15 • Reserve: ₹50"),
        Pair("Railway Station ⇄ Remuna Khirachora Temple", "Shared: ₹25 • Reserve: ₹120"),
        Pair("Railway Station ⇄ Chandipur Beach", "Shared: ₹35 • Reserve: ₹220"),
        Pair("Station Chhak ⇄ Cinema Bazar / Town Hall", "Shared: ₹10 • Reserve: ₹40"),
        Pair("Sahadevkhunta ⇄ FM University Nuapadhi", "Shared: ₹20 • Reserve: ₹100"),
        Pair("Town Ring (Motiganj ⇄ OT Road ⇄ Station)", "Shared: ₹10 Flat • Toto: ₹10")
    )

    Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFFFFBEB)),
            border = BorderStroke(1.dp, Color(0xFFFDE68A))
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Text(
                    text = if (isOdia) "ବାଲେଶ୍ୱର ପୌରପାଳିକା ନିର୍ଦ୍ଧାରିତ ଅଟୋ ଓ ଟୋଟୋ ଭଡ଼ା" else "Municipality Approved Auto & Toto Tariff Chart",
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    color = Color(0xFF92400E)
                )
                Text(
                    text = "Standard rates for shared auto-rickshaws, battery e-rickshaws (Toto), and private reserve.",
                    fontSize = 11.sp,
                    color = Color(0xFFB45309)
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        autoRates.forEach { (corridor, fare) ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = BorderStroke(1.dp, Color(0xFFE2E8F0))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(corridor, fontSize = 12.5.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0F172A))
                    }
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = Color(0xFFF0FDF4),
                        border = BorderStroke(1.dp, Color(0xFFBBF7D0))
                    ) {
                        Text(
                            text = fare,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF15803D),
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun EmptyTransitState(isOdia: Boolean) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(32.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("🚌", fontSize = 48.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = if (isOdia) "କୌଣସି ବସ୍ ମିଳିଲା ନାହିଁ" else "No matching buses found",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF334155)
            )
            Text(
                text = if (isOdia) "ଅନ୍ୟ ରୁଟ୍ ଚୟନ କରନ୍ତୁ" else "Try clearing your search query or route filter",
                fontSize = 12.sp,
                color = Color(0xFF64748B)
            )
        }
    }
}
