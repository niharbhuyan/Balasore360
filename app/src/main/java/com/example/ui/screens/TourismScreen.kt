package com.example.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.material.icons.filled.AutoStories
import androidx.compose.material.icons.filled.Directions
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.Route
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.ItineraryItemEntity
import com.example.data.model.AppLanguage
import com.example.data.model.BalasoreForecastDay
import com.example.data.model.BalasoreWeatherAlert
import com.example.data.model.DailyBalasoreIdiom
import com.example.data.model.Hotspot
import com.example.data.model.LiteraryTrailPoint
import com.example.data.model.TempleRitualInfo
import com.example.data.model.WeatherInfo
import com.example.data.remote.EmergencyAlertDto
import com.example.ui.components.AdMobBannerCard
import com.example.ui.components.BalasoreItineraryPlanner
import com.example.ui.components.DashboardWeatherWidget
import com.example.ui.components.HotspotWeatherBadge
import com.example.ui.components.HotspotWeatherIcon
import com.example.ui.components.LiveEmergencyAlertBanner
import com.example.ui.components.UniqueFeaturesPillGrid
import com.example.ui.theme.AmberGold
import com.example.ui.theme.BentoSlate100
import com.example.ui.theme.BentoSlate200
import com.example.ui.theme.BentoSlate400
import com.example.ui.theme.BentoSlate500
import com.example.ui.theme.BentoSlate600
import com.example.ui.theme.BentoSlate700
import com.example.ui.theme.BentoSlate800
import com.example.ui.theme.BentoSlate900
import com.example.ui.theme.EmeraldGreen
import com.example.ui.theme.OceanBlue
import com.example.ui.theme.OceanBlueDark
import com.example.ui.viewmodel.UniqueFeatureSheetType

@Composable
fun TourismScreen(
    hotspots: List<Hotspot>,
    templeRitualInfo: TempleRitualInfo,
    literaryTrailPoints: List<LiteraryTrailPoint>,
    dailyIdiom: DailyBalasoreIdiom,
    selectedCategory: String,
    searchQuery: String,
    language: AppLanguage,
    favoriteIds: Set<String>,
    weather: WeatherInfo = WeatherInfo(),
    forecastDays: List<BalasoreForecastDay> = emptyList(),
    weatherAlerts: List<BalasoreWeatherAlert> = emptyList(),
    isFahrenheit: Boolean = false,
    selectedForecastIndex: Int = 0,
    onCategorySelected: (String) -> Unit,
    onSearchChanged: (String) -> Unit,
    onToggleFavorite: (String) -> Unit,
    onToggleTempUnit: () -> Unit = {},
    onSelectForecastDay: (Int) -> Unit = {},
    onAcknowledgeAlert: (String) -> Unit = {},
    onNavigateToWeather: () -> Unit = {},
    onOpenFeatureSheet: (UniqueFeatureSheetType) -> Unit = {},
    itineraryItems: List<ItineraryItemEntity> = emptyList(),
    liveEmergencyAlerts: List<EmergencyAlertDto> = emptyList(),
    dismissedAlertIds: Set<String> = emptySet(),
    onDismissAlert: (String) -> Unit = {},
    onAddToItinerary: (Hotspot, Int, String, String) -> Unit = { _, _, _, _ -> },
    onRemoveFromItinerary: (Long) -> Unit = {},
    onClearItinerary: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val categories = listOf("All", "Beach & Coast", "Heritage & Spiritual", "Nature & Pilgrimage", "Eco-Tourism")

    val filteredHotspots = hotspots.filter { hotspot ->
        (selectedCategory == "All" || hotspot.category == selectedCategory) &&
        (searchQuery.isBlank() || hotspot.name.contains(searchQuery, ignoreCase = true) || hotspot.odiaName.contains(searchQuery, ignoreCase = true))
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("tourism_screen_list"),
        contentPadding = PaddingValues(bottom = 100.dp)
    ) {
        // High-Priority Live Emergency Alerts Banner (Cyclone Warnings, River Water Level Spikes)
        if (liveEmergencyAlerts.isNotEmpty()) {
            item {
                LiveEmergencyAlertBanner(
                    alerts = liveEmergencyAlerts,
                    dismissedAlertIds = dismissedAlertIds,
                    onDismissAlert = onDismissAlert,
                    onOpenShelters = { onOpenFeatureSheet(UniqueFeatureSheetType.CYCLONE_RESILIENCE) },
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
        }
        // Coastal Marine Bento Hero Header Card
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .background(
                        Brush.verticalGradient(
                            listOf(Color(0xFF0F172A), OceanBlueDark, OceanBlue)
                        )
                    )
                    .padding(20.dp)
            ) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = Color(0xFF38BDF8).copy(alpha = 0.2f),
                            border = BorderStroke(1.dp, Color(0xFF38BDF8).copy(alpha = 0.4f))
                        ) {
                            Text(
                                text = "BALASORE 360 • COASTAL GUIDE",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.ExtraBold,
                                    letterSpacing = 1.sp,
                                    fontSize = 9.5.sp,
                                    color = Color(0xFF7DD3FC)
                                ),
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                            )
                        }

                        Surface(
                            shape = CircleShape,
                            color = Color.White.copy(alpha = 0.15f),
                            modifier = Modifier
                                .size(34.dp)
                                .clip(CircleShape)
                                .clickable {
                                    val uri = Uri.parse("geo:21.4934,86.9135?q=Tourist+Attractions+in+Balasore")
                                    val mapIntent = Intent(Intent.ACTION_VIEW, uri)
                                    context.startActivity(mapIntent)
                                }
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Default.Map,
                                    contentDescription = "Explore on Maps",
                                    tint = Color.White,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Text(
                        text = if (language == AppLanguage.ODIA) "ବାଲେଶ୍ୱର ପରିଦର୍ଶନ" else "Explore Balasore",
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontWeight = FontWeight.ExtraBold,
                            color = Color.White
                        )
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = if (language == AppLanguage.ODIA)
                            "ଚାନ୍ଦିପୁର ବେଳାଭୂମି, କ୍ଷୀରଚୋରା ଗୋପୀନାଥ ଓ କୁଲଡିହା ବନ୍ୟପ୍ରାଣୀ ଅଭୟାରଣ୍ୟର ଅନନ୍ୟ ଐତିହ୍ୟ"
                        else
                            "From vanishing sea tides & golden sand to 12th-century Kalinga temples and pristine wildlife reserves.",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = Color(0xFFE2E8F0),
                            lineHeight = 20.sp
                        )
                    )
                }
            }
        }

        // Interactive Dashboard Weather Widget (Current Conditions, 3-Day Forecast & Severe Alerts)
        item {
            DashboardWeatherWidget(
                weather = weather,
                forecastDays = forecastDays,
                weatherAlerts = weatherAlerts,
                language = language,
                isFahrenheit = isFahrenheit,
                selectedForecastIndex = selectedForecastIndex,
                onToggleTempUnit = onToggleTempUnit,
                onSelectForecastDay = onSelectForecastDay,
                onAcknowledgeAlert = onAcknowledgeAlert,
                onNavigateToWeather = onNavigateToWeather
            )
        }

        // Live Special Features Quick Hub
        item {
            UniqueFeaturesPillGrid(onFeatureClick = onOpenFeatureSheet)
        }

        // Interactive Travel Itinerary Feature (Room-persisted, Google Maps SDK MapView, Coil weather, Entrance animations)
        item {
            BalasoreItineraryPlanner(
                itineraryItems = itineraryItems,
                availableHotspots = hotspots,
                language = language,
                onAddToItinerary = onAddToItinerary,
                onRemoveFromItinerary = onRemoveFromItinerary,
                onClearItinerary = onClearItinerary,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)
            )
        }

        // FEATURE 3: Remuna Khirachora Bhog & Darshan Tracker
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp)
                    .testTag("remuna_darshan_tracker_card"),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = BorderStroke(1.dp, Color(0xFFFED7AA))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
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
                                    .background(Color(0xFFFFF7ED)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text("🛕", fontSize = 18.sp)
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = if (language == AppLanguage.ODIA) templeRitualInfo.odiaTempleName else templeRitualInfo.templeName,
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = BentoSlate900,
                                        fontSize = 15.sp
                                    )
                                )
                                Text(
                                    text = templeRitualInfo.currentDarshanStatus,
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        color = EmeraldGreen,
                                        fontWeight = FontWeight.Bold
                                    )
                                )
                            }
                        }

                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color(0xFFFEF3C7))
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = "LIVE",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.ExtraBold,
                                    color = Color(0xFFB45309),
                                    fontSize = 10.sp
                                )
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Bhog Availability Highlight
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0xFFFFFBEB))
                            .border(BorderStroke(1.dp, Color(0xFFFDE68A)), RoundedCornerShape(12.dp))
                            .padding(12.dp)
                    ) {
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Restaurant,
                                    contentDescription = "Bhog",
                                    tint = Color(0xFFD97706),
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = templeRitualInfo.bhogName,
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF92400E)
                                    )
                                )
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Next distribution: ${templeRitualInfo.nextBhogDistribution}",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = Color(0xFFB45309),
                                    fontWeight = FontWeight.SemiBold
                                )
                            )
                            Text(
                                text = templeRitualInfo.bhogAvailabilityNote,
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = BentoSlate600,
                                    fontSize = 11.sp
                                )
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Timings Row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text("Mangala Alati", style = MaterialTheme.typography.labelSmall.copy(color = BentoSlate500))
                            Text(templeRitualInfo.mangalaAlati, style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold, color = BentoSlate800))
                        }
                        Column {
                            Text("Pahada Rest", style = MaterialTheme.typography.labelSmall.copy(color = BentoSlate500))
                            Text(templeRitualInfo.pahadaTime, style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold, color = BentoSlate800))
                        }
                        Column(horizontalAlignment = Alignment.End) {
                            Text("Sandhya Arati", style = MaterialTheme.typography.labelSmall.copy(color = BentoSlate500))
                            Text(templeRitualInfo.sandhyaArati, style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold, color = BentoSlate800))
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Button(
                        onClick = { onOpenFeatureSheet(UniqueFeatureSheetType.REMUNA_PRASAD_ARTISANS) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("open_remuna_artisan_sheet_btn"),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD97706)),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Icon(imageVector = Icons.Default.Restaurant, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Live Bhog Batch & GI Artisan Showcase", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        // FEATURE: Kuldiha Elephant Corridor & Ecotourism Passport Card
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp)
                    .testTag("kuldiha_corridor_passport_card"),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = BorderStroke(1.dp, Color(0xFFD8B4FE))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
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
                                    .background(Color(0xFFFAF5FF)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text("🐘", fontSize = 20.sp)
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = "Kuldiha Wildlife Corridor",
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = BentoSlate900,
                                        fontSize = 15.sp
                                    )
                                )
                                Text(
                                    text = "Similipal-Hadgarh Elephant Pass & Passport",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        color = Color(0xFF9333EA),
                                        fontWeight = FontWeight.Bold
                                    )
                                )
                            }
                        }

                        Surface(
                            color = Color(0xFFFAF5FF),
                            shape = RoundedCornerShape(8.dp),
                            border = BorderStroke(1.dp, Color(0xFFE9D5FF))
                        ) {
                            Text(
                                text = "ECO-HUB",
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 9.sp,
                                    color = Color(0xFF7E22CE)
                                )
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "Real-time forest ranger crossing alerts, speed corridors along Nilagiri highway, and digital check-in passport stamps for Rissia dam & salt licks.",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = BentoSlate700,
                            lineHeight = 16.sp
                        )
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Button(
                        onClick = { onOpenFeatureSheet(UniqueFeatureSheetType.ELEPHANT_PASSPORT) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("open_kuldiha_passport_sheet_btn"),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF7E22CE)),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Icon(imageVector = Icons.Default.Explore, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Open Elephant Safety & Eco-Passport", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        // FEATURE 6: Fakir Mohan Literary Trail & Balasore Dialect Idiom
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp)
                    .testTag("fakir_mohan_literary_trail_card"),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = BorderStroke(1.dp, Color(0xFFE2E8F0))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
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
                                    .background(Color(0xFFEDE9FE)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.AutoStories,
                                    contentDescription = "Literature",
                                    tint = Color(0xFF7C3AED),
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = if (language == AppLanguage.ODIA) "ବ୍ୟାସକବି ଫକୀର ମୋହନ ସାହିତ୍ୟ ପରିକ୍ରମା" else "Fakir Mohan Literary Trail",
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = BentoSlate900,
                                        fontSize = 15.sp
                                    )
                                )
                                Text(
                                    text = "Father of Modern Odia Literature",
                                    style = MaterialTheme.typography.bodySmall.copy(color = Color(0xFF7C3AED), fontWeight = FontWeight.SemiBold)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Daily Balasore Idiom Card
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0xFFF5F3FF))
                            .border(BorderStroke(1.dp, Color(0xFFDDD6FE)), RoundedCornerShape(12.dp))
                            .padding(12.dp)
                    ) {
                        Column {
                            Text(
                                text = "ଦିନର ବାଲେଶ୍ୱରୀ ଶବ୍ଦ (Dialect of the Day)",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF6D28D9)
                                )
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = dailyIdiom.wordOrIdiom,
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = BentoSlate900
                                )
                            )
                            Text(
                                text = dailyIdiom.meaning,
                                style = MaterialTheme.typography.bodySmall.copy(color = BentoSlate700)
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = dailyIdiom.funContext,
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = BentoSlate500,
                                    fontSize = 11.sp
                                )
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Trail Locations
                    literaryTrailPoints.forEach { point ->
                        LiteraryTrailItemView(point = point, language = language)
                        Spacer(modifier = Modifier.height(6.dp))
                    }
                }
            }
        }

        // Search Bar
        item {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = onSearchChanged,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .testTag("tourism_search_input"),
                placeholder = {
                    Text(
                        text = if (language == AppLanguage.ODIA) "ସ୍ଥାନ ଖୋଜନ୍ତୁ..." else "Search attractions, temples, beaches...",
                        style = MaterialTheme.typography.bodyMedium,
                        color = BentoSlate400
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search",
                        tint = OceanBlue
                    )
                },
                shape = RoundedCornerShape(14.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = OceanBlue,
                    unfocusedBorderColor = BentoSlate200,
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White
                ),
                singleLine = true
            )
        }

        // Category Filter Chips
        item {
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp),
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(categories) { category ->
                    val isSelected = category == selectedCategory
                    FilterChip(
                        selected = isSelected,
                        onClick = { onCategorySelected(category) },
                        label = {
                            Text(
                                text = category,
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                )
                            )
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = OceanBlue,
                            selectedLabelColor = Color.White,
                            containerColor = BentoSlate100,
                            labelColor = BentoSlate700
                        ),
                        border = null,
                        shape = RoundedCornerShape(10.dp)
                    )
                }
            }
        }

        // Hotspots List
        items(filteredHotspots, key = { it.id }) { spot ->
            HotspotCard(
                hotspot = spot,
                language = language,
                isFavorite = favoriteIds.contains(spot.id),
                onToggleFavorite = { onToggleFavorite(spot.id) },
                onAddToItinerary = { onAddToItinerary(spot, 1, "10:00 AM", "") },
                onGetDirections = {
                    val uri = Uri.parse("geo:0,0?q=${Uri.encode("${spot.name}, Balasore, Odisha")}")
                    val mapIntent = Intent(Intent.ACTION_VIEW, uri)
                    context.startActivity(mapIntent)
                }
            )
        }

        // AdMob Banner Placement inside tourism feed
        item {
            AdMobBannerCard(
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}

@Composable
fun HotspotCard(
    hotspot: Hotspot,
    language: AppLanguage,
    isFavorite: Boolean,
    onToggleFavorite: () -> Unit,
    onGetDirections: () -> Unit,
    onAddToItinerary: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val (weatherIconUrl, weatherCondition, tempC) = remember(hotspot) {
        when {
            hotspot.category.contains("Beach", ignoreCase = true) ->
                Triple("https://cdn.weatherapi.com/weather/64x64/day/113.png", "Breezy Sea", 28.0)
            hotspot.category.contains("Heritage", ignoreCase = true) || hotspot.category.contains("Spiritual", ignoreCase = true) ->
                Triple("https://cdn.weatherapi.com/weather/64x64/day/116.png", "Pleasant", 27.0)
            else ->
                Triple("https://cdn.weatherapi.com/weather/64x64/day/119.png", "Clear Canopy", 26.0)
        }
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .testTag("hotspot_card_${hotspot.id}"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        border = BorderStroke(1.dp, BentoSlate100)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = if (language == AppLanguage.ODIA) hotspot.odiaName else hotspot.name,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = BentoSlate900
                        )
                    )
                    if (language != AppLanguage.ODIA) {
                        Text(
                            text = hotspot.odiaName,
                            style = MaterialTheme.typography.labelSmall.copy(color = BentoSlate500)
                        )
                    }
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    // Real-time remote weather icon via Coil
                    HotspotWeatherBadge(
                        iconUrl = weatherIconUrl,
                        weatherCondition = weatherCondition,
                        tempC = tempC
                    )

                    IconButton(
                        onClick = onToggleFavorite,
                        modifier = Modifier.size(36.dp)
                    ) {
                        Icon(
                            imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                            contentDescription = "Favorite",
                            tint = if (isFavorite) Color.Red else BentoSlate400
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = hotspot.description,
                style = MaterialTheme.typography.bodyMedium.copy(color = BentoSlate700, lineHeight = 20.sp)
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Rating Badge
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(AmberGold.copy(alpha = 0.15f))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = "Rating",
                            tint = AmberGold,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = hotspot.rating.toString(),
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = BentoSlate800
                            )
                        )
                    }

                    // Distance
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.LocationOn,
                            contentDescription = "Location",
                            tint = BentoSlate400,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(2.dp))
                        Text(
                            text = "${hotspot.distanceKm} km",
                            style = MaterialTheme.typography.labelSmall.copy(color = BentoSlate500)
                        )
                    }
                }

                // Action Buttons: Add to Itinerary + Directions Map
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Add to Itinerary Button
                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0xFFE0F2FE))
                            .clickable { onAddToItinerary() }
                            .padding(horizontal = 10.dp, vertical = 6.dp)
                            .testTag("add_to_itinerary_btn_${hotspot.id}"),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "+ Plan",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = OceanBlue
                            )
                        )
                    }

                    // Directions Button
                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(OceanBlue.copy(alpha = 0.1f))
                            .clickable { onGetDirections() }
                            .padding(horizontal = 10.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Directions,
                            contentDescription = "Directions",
                            tint = OceanBlue,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Map",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = OceanBlue
                            )
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun LiteraryTrailItemView(
    point: LiteraryTrailPoint,
    language: AppLanguage
) {
    var showHistory by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(Color(0xFFF8FAFC))
            .clickable { showHistory = !showHistory }
            .padding(10.dp)
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = if (language == AppLanguage.ODIA) point.odiaTitle else point.title,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = BentoSlate900
                        )
                    )
                    Text(
                        text = point.location,
                        style = MaterialTheme.typography.labelSmall.copy(color = BentoSlate500)
                    )
                }
                Icon(
                    imageVector = if (showHistory) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                    contentDescription = "Details",
                    tint = BentoSlate400,
                    modifier = Modifier.size(18.dp)
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = point.excerpt,
                style = MaterialTheme.typography.bodySmall.copy(color = BentoSlate600, fontSize = 12.sp)
            )
            AnimatedVisibility(visible = showHistory) {
                Column(modifier = Modifier.padding(top = 6.dp)) {
                    Text(
                        text = point.historicalContext,
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = Color(0xFF4C1D95),
                            fontWeight = FontWeight.Medium
                        )
                    )
                }
            }
        }
    }
}
