package com.example.ui.screens

import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.animation.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.StarBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
import com.example.data.local.OdiaFestivalEntity
import com.example.data.model.AppLanguage
import com.example.data.repository.OdiaFestivalRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * Odia Festivals Section for Balasore 360.
 *
 * Displays upcoming local festivals and important cultural dates in Balasore,
 * powered by a local Room database for seamless offline access. Features
 * dynamic countdown timers, category filtering, search, favorite starring,
 * and automated live updates.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OdiaFestivalsScreen(
    language: AppLanguage = AppLanguage.ODIA,
    onClose: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    BackHandler { onClose() }

    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val repository = remember { OdiaFestivalRepository.getInstance(context) }

    val allFestivals by repository.allFestivalsFlow.collectAsState(initial = emptyList())
    val isRefreshing by repository.isRefreshing.collectAsState()
    val lastSyncTime by repository.lastSyncTime.collectAsState()

    // Filter & Search states
    var searchQuery by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("All") }

    // Auto-update countdown timer state (30 seconds)
    var isAutoUpdateEnabled by remember { mutableStateOf(true) }
    var secondsUntilNextUpdate by remember { mutableIntStateOf(30) }
    var autoUpdateCycles by remember { mutableIntStateOf(1) }

    // Ensure database is populated on initial launch
    LaunchedEffect(Unit) {
        repository.ensureInitialized()
    }

    // Auto-update loop
    LaunchedEffect(isAutoUpdateEnabled) {
        if (!isAutoUpdateEnabled) return@LaunchedEffect
        while (true) {
            delay(1000)
            if (secondsUntilNextUpdate > 1) {
                secondsUntilNextUpdate -= 1
            } else {
                secondsUntilNextUpdate = 30
                autoUpdateCycles += 1
                repository.refreshFestivalDates()
            }
        }
    }

    // Filtered festivals list
    val displayedFestivals = remember(allFestivals, searchQuery, selectedCategory) {
        allFestivals.filter { festival ->
            val matchesCategory = when (selectedCategory) {
                "All", "ସମସ୍ତ" -> true
                "Starred", "ପସନ୍ଦ ⭐" -> festival.isStarred
                else -> festival.category.equals(selectedCategory, ignoreCase = true)
            }

            val matchesSearch = if (searchQuery.isBlank()) {
                true
            } else {
                festival.titleEn.contains(searchQuery, ignoreCase = true) ||
                        festival.titleOr.contains(searchQuery, ignoreCase = true) ||
                        festival.venueEn.contains(searchQuery, ignoreCase = true) ||
                        festival.venueOr.contains(searchQuery, ignoreCase = true)
            }

            matchesCategory && matchesSearch
        }
    }

    // Nearest upcoming festival for the Spotlight banner
    val nearestUpcoming = remember(allFestivals) {
        allFestivals.minByOrNull { it.daysRemaining }
    }

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .testTag("odia_festivals_screen"),
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(text = "🏮", fontSize = 18.sp)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = if (language == AppLanguage.ODIA) "ଓଡ଼ିଆ ପର୍ବପର୍ବାଣି" else "Odia Festivals & Dates",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.ExtraBold,
                                    color = Color(0xFF431407)
                                )
                            )
                        }
                        Text(
                            text = if (language == AppLanguage.ODIA) "ବାଲେଶ୍ୱର ଜିଲ୍ଲାର ପାରମ୍ପରିକ ଯାତ୍ରା ଓ ମେଳା" else "Balasore Local Festivals & Melas • Room Cached",
                            fontSize = 11.sp,
                            color = Color(0xFF9A3412)
                        )
                    }
                },
                navigationIcon = {
                    IconButton(
                        onClick = onClose,
                        modifier = Modifier.testTag("festivals_back_btn")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color(0xFF431407)
                        )
                    }
                },
                actions = {
                    // Auto-update countdown badge
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = if (isAutoUpdateEnabled) Color(0xFFFEF3C7) else Color(0xFFF1F5F9),
                        border = BorderStroke(1.dp, if (isAutoUpdateEnabled) Color(0xFFF59E0B) else Color(0xFFCBD5E1)),
                        modifier = Modifier
                            .clickable { isAutoUpdateEnabled = !isAutoUpdateEnabled }
                            .testTag("festival_auto_update_toggle")
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(7.dp)
                                    .background(
                                        if (isAutoUpdateEnabled) Color(0xFF16A34A) else Color(0xFF94A3B8),
                                        CircleShape
                                    )
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = if (isAutoUpdateEnabled) "${secondsUntilNextUpdate}s" else "PAUSED",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isAutoUpdateEnabled) Color(0xFFB45309) else Color(0xFF64748B)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(6.dp))

                    // Manual refresh button
                    IconButton(
                        onClick = {
                            coroutineScope.launch {
                                secondsUntilNextUpdate = 30
                                repository.refreshFestivalDates()
                                Toast.makeText(
                                    context,
                                    if (language == AppLanguage.ODIA) "ପର୍ବ ତାଲିକା ଅପଡେଟ୍ ହୋଇଛି" else "Festival dates updated",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        },
                        modifier = Modifier.testTag("festival_refresh_button")
                    ) {
                        if (isRefreshing) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(20.dp),
                                color = Color(0xFFB45309),
                                strokeWidth = 2.dp
                            )
                        } else {
                            Icon(
                                imageVector = Icons.Default.Sync,
                                contentDescription = "Refresh",
                                tint = Color(0xFFB45309)
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFFFFFBEB)
                )
            )
        },
        containerColor = Color(0xFFFFFDF5)
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(bottom = 40.dp)
        ) {
            // Hero Cultural Banner
            item {
                FestivalsHeroBanner(
                    language = language,
                    lastSyncTime = lastSyncTime,
                    totalCount = allFestivals.size,
                    starredCount = allFestivals.count { it.isStarred }
                )
            }

            // Nearest Upcoming Festival Spotlight Card
            if (nearestUpcoming != null && searchQuery.isBlank() && selectedCategory == "All") {
                item {
                    UpcomingFestivalSpotlight(
                        festival = nearestUpcoming,
                        language = language,
                        onToggleStar = { id, starred ->
                            coroutineScope.launch { repository.toggleStar(id, starred) }
                        }
                    )
                }
            }

            // Search Bar
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("festivals_search_input"),
                        placeholder = {
                            Text(
                                text = if (language == AppLanguage.ODIA) "ପର୍ବ, ମେଳା କିମ୍ବା ସ୍ଥାନ ଖୋଜନ୍ତୁ..." else "Search festival, mela or temple venue...",
                                fontSize = 13.sp,
                                color = Color(0xFF94A3B8)
                            )
                        },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = null,
                                tint = Color(0xFFB45309)
                            )
                        },
                        trailingIcon = {
                            if (searchQuery.isNotEmpty()) {
                                IconButton(onClick = { searchQuery = "" }) {
                                    Icon(
                                        imageVector = Icons.Default.Close,
                                        contentDescription = "Clear",
                                        tint = Color(0xFF64748B)
                                    )
                                }
                            }
                        },
                        singleLine = true,
                        shape = RoundedCornerShape(14.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFFF59E0B),
                            unfocusedBorderColor = Color(0xFFFED7AA),
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White
                        )
                    )
                }
            }

            // Category Filter Chips Carousel
            item {
                FestivalsCategoryFilters(
                    selectedCategory = selectedCategory,
                    onSelectCategory = { selectedCategory = it },
                    language = language
                )
            }

            // Section Header with count
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 6.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (language == AppLanguage.ODIA) "ଆଗାମୀ ପର୍ବପର୍ବାଣି (${displayedFestivals.size})" else "Upcoming Festivals (${displayedFestivals.size})",
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF78350F)
                        )
                    )

                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = Color(0xFFFEF3C7)
                    ) {
                        Text(
                            text = "ROOM DB CACHED",
                            fontSize = 9.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color(0xFFB45309),
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }
            }

            // Empty state if no festivals found
            if (displayedFestivals.isEmpty()) {
                item {
                    FestivalsEmptyState(
                        searchQuery = searchQuery,
                        onReset = {
                            searchQuery = ""
                            selectedCategory = "All"
                        },
                        language = language
                    )
                }
            } else {
                // Festival Cards
                items(
                    items = displayedFestivals,
                    key = { it.id }
                ) { festival ->
                    FestivalItemCard(
                        festival = festival,
                        language = language,
                        context = context,
                        onToggleStar = { id, starred ->
                            coroutineScope.launch { repository.toggleStar(id, starred) }
                        }
                    )
                }
            }
        }
    }
}

/**
 * Festivals Hero Banner with Odia Cultural Branding.
 */
@Composable
fun FestivalsHeroBanner(
    language: AppLanguage,
    lastSyncTime: String,
    totalCount: Int,
    starredCount: Int
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                Brush.verticalGradient(
                    listOf(
                        Color(0xFF7C2D12),
                        Color(0xFF9A3412),
                        Color(0xFFC2410C)
                    )
                )
            )
            .padding(horizontal = 16.dp, vertical = 18.dp)
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = Color.White.copy(alpha = 0.20f)
                ) {
                    Text(
                        text = "ଉତ୍କଳୀୟ ସଂସ୍କୃତି • BALASORE FESTIVAL CALENDAR",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFFEF3C7),
                            fontSize = 10.sp
                        ),
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }

                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = Color(0xFF10B981).copy(alpha = 0.25f),
                    border = BorderStroke(1.dp, Color(0xFF6EE7B7))
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(5.dp)
                                .background(Color(0xFF6EE7B7), CircleShape)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "OFFLINE READY • $lastSyncTime",
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFD1FAE5)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = if (language == AppLanguage.ODIA) "ବାଲେଶ୍ୱର ପର୍ବପର୍ବାଣି ଓ ଯାତ୍ରା" else "Balasore Festivals & Cultural Dates",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Black,
                    color = Color.White
                )
            )

            Text(
                text = if (language == AppLanguage.ODIA)
                    "ଚନ୍ଦନେଶ୍ୱର ଚଡ଼କ, ବଳରାମଗଡ଼ି ବୋଇତ ବନ୍ଦାଣ, ରେମୁଣା ଚନ୍ଦନ ଯାତ୍ରା ଓ ନୀଳଗିରି ମକର ମେଳାର ଲାଇଭ୍ କ୍ୟାଲେଣ୍ଡର।"
                else
                    "Local temple melas, maritime boat voyages, harvest carnivals, and cultural dates cached offline in Room.",
                fontSize = 12.sp,
                color = Color(0xFFFED7AA),
                lineHeight = 16.sp,
                modifier = Modifier.padding(top = 4.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Quick Stats Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FestivalsStatPill(
                    icon = "🎪",
                    title = if (language == AppLanguage.ODIA) "ମୋଟ ପର୍ବ" else "Total Festivals",
                    value = "$totalCount Events",
                    modifier = Modifier.weight(1f)
                )
                FestivalsStatPill(
                    icon = "⭐",
                    title = if (language == AppLanguage.ODIA) "ପସନ୍ଦ ତାଲିକା" else "Saved Starred",
                    value = "$starredCount Saved",
                    modifier = Modifier.weight(1f)
                )
                FestivalsStatPill(
                    icon = "⏳",
                    title = if (language == AppLanguage.ODIA) "କାଉଣ୍ଟଡାଉନ୍" else "Countdown",
                    value = "Live Days",
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
fun FestivalsStatPill(
    icon: String,
    title: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(10.dp),
        color = Color(0xFF451A03).copy(alpha = 0.55f),
        border = BorderStroke(1.dp, Color(0xFFF97316).copy(alpha = 0.4f)),
        modifier = modifier
    ) {
        Column(modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = icon, fontSize = 11.sp)
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = title,
                    fontSize = 9.sp,
                    color = Color(0xFFFED7AA),
                    fontWeight = FontWeight.Medium
                )
            }
            Text(
                text = value,
                fontSize = 11.5.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier.padding(top = 2.dp)
            )
        }
    }
}

/**
 * Nearest Upcoming Festival Spotlight Banner.
 */
@Composable
fun UpcomingFestivalSpotlight(
    festival: OdiaFestivalEntity,
    language: AppLanguage,
    onToggleStar: (Long, Boolean) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .testTag("upcoming_festival_spotlight"),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF7ED)),
        border = BorderStroke(1.5.dp, Color(0xFFF97316))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = Color(0xFFEA580C)
                ) {
                    Text(
                        text = "🔥 NEAREST UPCOMING FESTIVAL",
                        fontSize = 9.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                    )
                }

                // Days Remaining Pill
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = Color(0xFFDC2626),
                    border = BorderStroke(1.dp, Color(0xFFFCA5A5))
                ) {
                    Text(
                        text = if (language == AppLanguage.ODIA) "ଆଉ ${festival.daysRemaining} ଦିନ" else "${festival.daysRemaining} Days Left",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Black,
                        color = Color.White,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = if (language == AppLanguage.ODIA) festival.titleOr else festival.titleEn,
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Black,
                    color = Color(0xFF7C2D12)
                )
            )

            Row(
                modifier = Modifier.padding(top = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.LocationOn,
                    contentDescription = null,
                    tint = Color(0xFFEA580C),
                    modifier = Modifier.size(15.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = if (language == AppLanguage.ODIA) festival.venueOr else festival.venueEn,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF9A3412)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = if (language == AppLanguage.ODIA) festival.significanceOr else festival.significanceEn,
                fontSize = 12.sp,
                color = Color(0xFF451A03),
                lineHeight = 17.sp,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = Color(0xFFFED7AA)
                ) {
                    Text(
                        text = "🗓️ ${festival.eventDate} • ${festival.odiaTithi}",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF7C2D12),
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }

                IconButton(
                    onClick = { onToggleStar(festival.id, festival.isStarred) },
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        imageVector = if (festival.isStarred) Icons.Default.Star else Icons.Outlined.StarBorder,
                        contentDescription = "Star",
                        tint = if (festival.isStarred) Color(0xFFD97706) else Color(0xFF9A3412)
                    )
                }
            }
        }
    }
}

/**
 * Filter chips for Festival Categories.
 */
@Composable
fun FestivalsCategoryFilters(
    selectedCategory: String,
    onSelectCategory: (String) -> Unit,
    language: AppLanguage
) {
    val categories = listOf(
        "All",
        "Temple Festival",
        "Maritime & Beach",
        "Folk & Fair",
        "Harvest & Agro",
        "Heritage & Memorial",
        "Starred"
    )

    LazyRow(
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.testTag("festivals_category_chips")
    ) {
        items(categories) { cat ->
            val isSelected = selectedCategory.equals(cat, ignoreCase = true)
            val label = when (cat) {
                "All" -> if (language == AppLanguage.ODIA) "ସମସ୍ତ" else "All"
                "Temple Festival" -> if (language == AppLanguage.ODIA) "ମନ୍ଦିର ପର୍ବ" else "Temple Festivals"
                "Maritime & Beach" -> if (language == AppLanguage.ODIA) "ସାମୁଦ୍ରିକ" else "Maritime"
                "Folk & Fair" -> if (language == AppLanguage.ODIA) "ମେଳା ଓ ଯାତ୍ରା" else "Folk & Fairs"
                "Harvest & Agro" -> if (language == AppLanguage.ODIA) "କୃଷି ପର୍ବ" else "Harvest"
                "Heritage & Memorial" -> if (language == AppLanguage.ODIA) "ସ୍ମୃତି ସଭା" else "Memorial"
                "Starred" -> if (language == AppLanguage.ODIA) "ପସନ୍ଦ ⭐" else "Starred ⭐"
                else -> cat
            }

            FilterChip(
                selected = isSelected,
                onClick = { onSelectCategory(cat) },
                label = {
                    Text(
                        text = label,
                        fontSize = 11.5.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                    )
                },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = Color(0xFFB45309),
                    selectedLabelColor = Color.White,
                    containerColor = Color.White,
                    labelColor = Color(0xFF78350F)
                ),
                border = FilterChipDefaults.filterChipBorder(
                    enabled = true,
                    selected = isSelected,
                    borderColor = Color(0xFFFED7AA),
                    selectedBorderColor = Color(0xFFB45309)
                )
            )
        }
    }
}

/**
 * Individual Festival Item Card.
 */
@Composable
fun FestivalItemCard(
    festival: OdiaFestivalEntity,
    language: AppLanguage,
    context: Context,
    onToggleStar: (Long, Boolean) -> Unit
) {
    var isExpanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
            .clickable { isExpanded = !isExpanded }
            .testTag("festival_item_card_${festival.id}"),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(
            width = if (festival.isStarred) 1.8.dp else 1.dp,
            color = if (festival.isStarred) Color(0xFFF59E0B) else Color(0xFFFED7AA)
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Header Row: Month / Tithi tag + Days Remaining Pill
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = Color(0xFFFFFBEB),
                    border = BorderStroke(1.dp, Color(0xFFFDE68A))
                ) {
                    Text(
                        text = "🗓️ ${festival.monthPeriodOr} • ${festival.odiaTithi}",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF92400E),
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                    )
                }

                // Days remaining counter badge
                val countdownColor = when {
                    festival.daysRemaining <= 30 -> Color(0xFFDC2626)
                    festival.daysRemaining <= 90 -> Color(0xFFEA580C)
                    else -> Color(0xFF0284C7)
                }
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = countdownColor.copy(alpha = 0.12f),
                    border = BorderStroke(1.dp, countdownColor)
                ) {
                    Text(
                        text = if (language == AppLanguage.ODIA) "ଆଉ ${festival.daysRemaining} ଦିନ" else "${festival.daysRemaining}d left",
                        fontSize = 10.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = countdownColor,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Title Row with Star Icon
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = if (language == AppLanguage.ODIA) festival.titleOr else festival.titleEn,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.ExtraBold,
                            color = Color(0xFF451A03),
                            fontSize = 16.sp
                        )
                    )
                    Text(
                        text = if (language == AppLanguage.ODIA) festival.titleEn else festival.titleOr,
                        fontSize = 11.5.sp,
                        color = Color(0xFF9A3412)
                    )
                }

                IconButton(
                    onClick = { onToggleStar(festival.id, festival.isStarred) },
                    modifier = Modifier.size(36.dp)
                ) {
                    Icon(
                        imageVector = if (festival.isStarred) Icons.Default.Star else Icons.Outlined.StarBorder,
                        contentDescription = "Star",
                        tint = if (festival.isStarred) Color(0xFFF59E0B) else Color(0xFF94A3B8)
                    )
                }
            }

            // Venue location
            Row(
                modifier = Modifier.padding(top = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.LocationOn,
                    contentDescription = null,
                    tint = Color(0xFFD97706),
                    modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = if (language == AppLanguage.ODIA) festival.venueOr else festival.venueEn,
                    fontSize = 12.sp,
                    color = Color(0xFF64748B),
                    fontWeight = FontWeight.Medium
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Brief significance
            Text(
                text = if (language == AppLanguage.ODIA) festival.significanceOr else festival.significanceEn,
                fontSize = 12.sp,
                color = Color(0xFF334155),
                lineHeight = 17.sp,
                maxLines = if (isExpanded) 10 else 2,
                overflow = TextOverflow.Ellipsis
            )

            // Expanded Telemetry (Rituals, Transit, Prasad)
            AnimatedVisibility(
                visible = isExpanded,
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically()
            ) {
                Column(modifier = Modifier.padding(top = 12.dp)) {
                    Divider(color = Color(0xFFFED7AA), thickness = 0.8.dp)

                    Spacer(modifier = Modifier.height(8.dp))

                    FestivalDetailItem(
                        icon = "✨",
                        label = if (language == AppLanguage.ODIA) "ପ୍ରମୁଖ ପରମ୍ପରା ଓ ନୀତିକାନ୍ତି" else "Key Rituals",
                        value = if (language == AppLanguage.ODIA) festival.ritualsOr else festival.ritualsEn
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    FestivalDetailItem(
                        icon = "🚌",
                        label = if (language == AppLanguage.ODIA) "ଯାତ୍ରୀ ଯାତାୟାତ ସୂଚନା" else "Pilgrim Transit",
                        value = if (language == AppLanguage.ODIA) festival.specialTransitOr else festival.specialTransitEn
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    FestivalDetailItem(
                        icon = "🍯",
                        label = if (language == AppLanguage.ODIA) "ପବିତ୍ର ଭୋଗ ଓ ପ୍ରସାଦ" else "Prasad Specialty",
                        value = festival.prasadSpecialty
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Action Row: Toggle details + Share
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (isExpanded) "▲ ଅଳ୍ପ ଦେଖନ୍ତୁ (Less)" else "▼ ସମ୍ପୂର୍ଣ୍ଣ ବିବରଣୀ (Details)",
                    fontSize = 11.5.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFB45309)
                )

                // Share button
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = Color(0xFFFFFBEB),
                    border = BorderStroke(1.dp, Color(0xFFFDE68A)),
                    modifier = Modifier.clickable {
                        val shareText = "🏮 ${festival.titleOr} (${festival.titleEn})\n" +
                                "📍 ସ୍ଥାନ: ${festival.venueOr}\n" +
                                "🗓️ ତାରିଖ: ${festival.eventDate} (${festival.odiaTithi})\n" +
                                "⏳ ଆଉ ${festival.daysRemaining} ଦିନ ବାକି ଅଛି!\n\n" +
                                "✨ ନୀତିକାନ୍ତି: ${festival.ritualsOr}\n" +
                                "🍯 ଭୋଗ: ${festival.prasadSpecialty}\n\n" +
                                "Explore more on Balasore 360 App."
                        val sendIntent = Intent().apply {
                            action = Intent.ACTION_SEND
                            putExtra(Intent.EXTRA_TEXT, shareText)
                            type = "text/plain"
                        }
                        context.startActivity(Intent.createChooser(sendIntent, "Share Festival Info"))
                    }
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Share,
                            contentDescription = "Share",
                            tint = Color(0xFF92400E),
                            modifier = Modifier.size(13.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = if (language == AppLanguage.ODIA) "ସେୟାର୍" else "Share",
                            fontSize = 10.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF92400E)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun FestivalDetailItem(icon: String, label: String, value: String) {
    Surface(
        shape = RoundedCornerShape(10.dp),
        color = Color(0xFFFFFBEB),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(8.dp),
            verticalAlignment = Alignment.Top
        ) {
            Text(text = icon, fontSize = 13.sp)
            Spacer(modifier = Modifier.width(6.dp))
            Column {
                Text(
                    text = label,
                    fontSize = 10.5.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF78350F)
                )
                Text(
                    text = value,
                    fontSize = 11.5.sp,
                    color = Color(0xFF334155),
                    lineHeight = 16.sp,
                    modifier = Modifier.padding(top = 1.dp)
                )
            }
        }
    }
}

/**
 * Empty search state.
 */
@Composable
fun FestivalsEmptyState(
    searchQuery: String,
    onReset: () -> Unit,
    language: AppLanguage
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, Color(0xFFFED7AA))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "🏮", fontSize = 36.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = if (language == AppLanguage.ODIA) "କୌଣସି ପର୍ବ ମିଳିଲା ନାହିଁ" else "No Festivals Found",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF78350F)
                )
            )
            Text(
                text = if (language == AppLanguage.ODIA)
                    "'$searchQuery' ପାଇଁ କୌଣସି ଫଳାଫଳ ନାହିଁ। ଦୟାକରି ଫିଲ୍ଟର ବଦଳାନ୍ତୁ।"
                else
                    "No results matching '$searchQuery'. Try resetting filters.",
                fontSize = 12.sp,
                color = Color(0xFF64748B),
                modifier = Modifier.padding(top = 4.dp)
            )
            Spacer(modifier = Modifier.height(14.dp))
            Button(
                onClick = onReset,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFB45309)),
                shape = RoundedCornerShape(10.dp)
            ) {
                Text(text = if (language == AppLanguage.ODIA) "ସମସ୍ତ ପର୍ବ ଦେଖନ୍ତୁ" else "Reset All Filters")
            }
        }
    }
}
