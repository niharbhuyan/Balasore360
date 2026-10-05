package com.example.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.OdiaHoroscopeEntity
import com.example.data.model.AppLanguage
import com.example.data.repository.OdiaHoroscopeRepository
import com.example.ui.theme.AmberGold
import com.example.ui.theme.EmeraldGreen
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * Premier Odia Culture & Daily Horoscope (ଦୈନିକ ରାଶିଫଳ) Section.
 *
 * Provides the Odia community with:
 *  1. Live Daily Horoscope fetched from Astrology API & enriched with Vedic telemetry.
 *  2. Interactive 12-Rasi selector with Career, Finance, Health, Family predictions.
 *  3. Real-time Shubha Bela, Rahu Kala, Amrita Bela & Auspicious Chanting Mantras.
 *  4. Odia Panjika with Tithi, Nakshatra, Chandrashtama & Suryodaya/Suryasta.
 *  5. Balasore Temple schedules (Remuna, Emami Jagannath, Panchalingeswar).
 *  6. Baleswari Proverbs & Fakir Mohan Senapati literary heritage.
 *  7. Automated 30s live update engine with countdown timer and manual sync.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OdiaCultureScreen(
    language: AppLanguage = AppLanguage.ODIA,
    onClose: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    BackHandler { onClose() }

    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val repository = remember { OdiaHoroscopeRepository.getInstance(context) }

    val horoscopes by repository.horoscopesFlow.collectAsState(initial = emptyList())
    val isRefreshing by repository.isRefreshing.collectAsState()
    val lastSyncTime by repository.lastSyncTime.collectAsState()
    val syncMessage by repository.syncMessage.collectAsState()

    // Sub-sections inside Culture: 0 = Daily Horoscope, 1 = Odia Panjika, 2 = Balasore Temples, 3 = Language & Literature
    var cultureTab by remember { mutableIntStateOf(0) }
    var selectedSignKey by remember { mutableStateOf("mesha") }

    // Auto-update countdown state
    var isAutoUpdateEnabled by remember { mutableStateOf(true) }
    var secondsUntilNextUpdate by remember { mutableIntStateOf(30) }
    var updateCycleCount by remember { mutableIntStateOf(1) }

    // Initialize repository on first composition
    LaunchedEffect(Unit) {
        repository.ensureInitialized()
        repository.refreshDailyHoroscopes()
    }

    // Auto-update timer loop
    LaunchedEffect(isAutoUpdateEnabled) {
        if (!isAutoUpdateEnabled) return@LaunchedEffect
        while (true) {
            delay(1000)
            if (secondsUntilNextUpdate > 1) {
                secondsUntilNextUpdate -= 1
            } else {
                secondsUntilNextUpdate = 30
                updateCycleCount += 1
                repository.refreshDailyHoroscopes()
            }
        }
    }

    // Selected horoscope entity
    val activeHoroscope = horoscopes.find { it.signKey == selectedSignKey }
        ?: horoscopes.firstOrNull()

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .testTag("odia_culture_screen"),
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "🪔",
                                fontSize = 18.sp
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = if (language == AppLanguage.ODIA) "ଓଡ଼ିଆ ସଂସ୍କୃତି ଓ ରାଶିଫଳ" else "Odia Culture & Horoscope",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.ExtraBold,
                                    color = Color(0xFF451A03)
                                )
                            )
                        }
                        Text(
                            text = if (language == AppLanguage.ODIA) "ଦୈନିକ ପାଞ୍ଜି, ଜ୍ୟୋତିଷ ଓ ବାଲେଶ୍ୱର ଐତିହ୍ୟ" else "Daily Panjika, Astrology API & Balasore Heritage",
                            fontSize = 11.sp,
                            color = Color(0xFF9A3412)
                        )
                    }
                },
                navigationIcon = {
                    IconButton(
                        onClick = onClose,
                        modifier = Modifier.testTag("culture_screen_back_btn")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color(0xFF451A03)
                        )
                    }
                },
                actions = {
                    // Auto-Update indicator chip
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = if (isAutoUpdateEnabled) Color(0xFFFEF3C7) else Color(0xFFF1F5F9),
                        border = BorderStroke(1.dp, if (isAutoUpdateEnabled) Color(0xFFF59E0B) else Color(0xFFCBD5E1)),
                        modifier = Modifier
                            .clickable { isAutoUpdateEnabled = !isAutoUpdateEnabled }
                            .testTag("culture_auto_update_toggle")
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
                                repository.refreshDailyHoroscopes()
                                Toast.makeText(
                                    context,
                                    if (language == AppLanguage.ODIA) "ରାଶିଫଳ ଅପଡେଟ୍ ହୋଇଛି" else "Horoscope updated",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        },
                        modifier = Modifier.testTag("culture_refresh_button")
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
            // Hero Cultural Header Card with Tithi & Panjika Summary
            item {
                CulturalHeroBanner(
                    language = language,
                    lastSyncTime = lastSyncTime,
                    isRefreshing = isRefreshing
                )
            }

            // Section Navigation Tabs (Horoscope, Panjika, Temples, Language)
            item {
                CultureSectionTabs(
                    selectedTab = cultureTab,
                    onSelectTab = { cultureTab = it },
                    language = language
                )
            }

            // Tab Content Rendering
            when (cultureTab) {
                0 -> {
                    // Daily Horoscope (ଦୈନିକ ରାଶିଫଳ)
                    item {
                        RasiSelectorCarousel(
                            horoscopes = horoscopes,
                            selectedSignKey = selectedSignKey,
                            onSelectSign = { selectedSignKey = it },
                            language = language
                        )
                    }

                    if (activeHoroscope != null) {
                        item {
                            ActiveHoroscopeCard(
                                horoscope = activeHoroscope,
                                language = language
                            )
                        }

                        item {
                            AuspiciousTimingsCard(
                                horoscope = activeHoroscope,
                                language = language
                            )
                        }

                        item {
                            RemedialMantraCard(
                                horoscope = activeHoroscope,
                                language = language,
                                context = context
                            )
                        }
                    }
                }
                1 -> {
                    // Odia Panjika & Shubha Muhurat
                    item {
                        OdiaPanjikaSection(language = language)
                    }
                }
                2 -> {
                    // Balasore Temples & Prasad Companion
                    item {
                        BalasoreTemplesSection(language = language)
                    }
                }
                3 -> {
                    // Baleswari Language & Literature
                    item {
                        BaleswariLiteratureSection(language = language)
                    }
                }
            }
        }
    }
}

/**
 * Cultural Hero Banner with traditional Odia aesthetic.
 */
@Composable
fun CulturalHeroBanner(
    language: AppLanguage,
    lastSyncTime: String,
    isRefreshing: Boolean
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                Brush.verticalGradient(
                    listOf(
                        Color(0xFF78350F),
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
                        text = "ଜୟ ଜଗନ୍ନାଥ • JAY JAGANNATH",
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
                            text = if (isRefreshing) "SYNCING..." else "LIVE API: $lastSyncTime",
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFD1FAE5)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = if (language == AppLanguage.ODIA) "ଓଡ଼ିଆ ପାଞ୍ଜି ଓ ଦୈନିକ ରାଶିଫଳ" else "Odia Cultural Panjika & Daily Horoscope",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Black,
                    color = Color.White
                )
            )

            Text(
                text = if (language == AppLanguage.ODIA)
                    "ଜ୍ୟୋତିଷ ବିଜ୍ଞାନ, ବୈଦିକ ଗ୍ରହଚଳନ ଓ ଶୁଭ ବେଳା ଅନୁସାରେ ୧୨ଟି ରାଶିର ସଠିକ୍ ଦୈନିକ ବିବରଣୀ।"
                else
                    "Real-time astrology API predictions, planetary transits, auspicious Shubha Bela & sacred mantras.",
                fontSize = 12.sp,
                color = Color(0xFFFED7AA),
                lineHeight = 16.sp,
                modifier = Modifier.padding(top = 4.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Quick Panjika Snapshot Pills
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                CulturalHeaderPill(
                    icon = "📅",
                    title = "ତିଥି (Tithi)",
                    value = "ଶୁକ୍ଳ ଦ୍ୱାଦଶୀ",
                    modifier = Modifier.weight(1f)
                )
                CulturalHeaderPill(
                    icon = "⭐",
                    title = "ନକ୍ଷତ୍ର (Star)",
                    value = "ଶ୍ରବଣା ନକ୍ଷତ୍ର",
                    modifier = Modifier.weight(1f)
                )
                CulturalHeaderPill(
                    icon = "☀️",
                    title = "ସୂର୍ଯ୍ୟୋଦୟ (Dawn)",
                    value = "୦୫:୪୮ AM",
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
fun CulturalHeaderPill(
    icon: String,
    title: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(10.dp),
        color = Color(0xFF431407).copy(alpha = 0.55f),
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
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier.padding(top = 2.dp)
            )
        }
    }
}

/**
 * Culture sub-section tabs.
 */
@Composable
fun CultureSectionTabs(
    selectedTab: Int,
    onSelectTab: (Int) -> Unit,
    language: AppLanguage
) {
    val tabs = listOf(
        "🔮 ରାଶିଫଳ (Horoscope)",
        "📜 ପାଞ୍ଜି (Panjika)",
        "🛕 ମନ୍ଦିର (Temples)",
        "📖 ସାହିତ୍ୟ (Heritage)"
    )

    ScrollableTabRow(
        selectedTabIndex = selectedTab,
        containerColor = Color(0xFFFFFBEB),
        contentColor = Color(0xFFB45309),
        edgePadding = 16.dp,
        modifier = Modifier.testTag("culture_section_tabs")
    ) {
        tabs.forEachIndexed { index, title ->
            Tab(
                selected = selectedTab == index,
                onClick = { onSelectTab(index) },
                text = {
                    Text(
                        text = title,
                        fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Normal,
                        fontSize = 12.sp,
                        color = if (selectedTab == index) Color(0xFF9A3412) else Color(0xFF78350F)
                    )
                }
            )
        }
    }
}

/**
 * Horizontal Carousel of 12 Odia Zodiac Signs.
 */
@Composable
fun RasiSelectorCarousel(
    horoscopes: List<OdiaHoroscopeEntity>,
    selectedSignKey: String,
    onSelectSign: (String) -> Unit,
    language: AppLanguage
) {
    Column(modifier = Modifier.padding(top = 16.dp)) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = if (language == AppLanguage.ODIA) "ଆପଣଙ୍କ ରାଶି ଚୟନ କରନ୍ତୁ" else "Select Your Zodiac Sign",
                style = MaterialTheme.typography.titleSmall.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF78350F)
                )
            )
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = Color(0xFFFEF3C7)
            ) {
                Text(
                    text = "୧୨ ରାଶି (12 Signs)",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF92400E),
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(horoscopes) { rasi ->
                val isSelected = rasi.signKey == selectedSignKey
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = if (isSelected) Color(0xFF9A3412) else Color.White,
                    border = BorderStroke(
                        width = if (isSelected) 2.dp else 1.dp,
                        color = if (isSelected) Color(0xFFF97316) else Color(0xFFFED7AA)
                    ),
                    shadowElevation = if (isSelected) 4.dp else 1.dp,
                    modifier = Modifier
                        .clickable { onSelectSign(rasi.signKey) }
                        .testTag("rasi_chip_${rasi.signKey}")
                ) {
                    Column(
                        modifier = Modifier
                            .width(82.dp)
                            .padding(vertical = 12.dp, horizontal = 6.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = rasi.symbol,
                            fontSize = 26.sp
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = rasi.signNameOr,
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.ExtraBold,
                                color = if (isSelected) Color.White else Color(0xFF451A03),
                                fontSize = 14.sp
                            )
                        )
                        Text(
                            text = rasi.signNameEn,
                            fontSize = 10.sp,
                            color = if (isSelected) Color(0xFFFED7AA) else Color(0xFF9A3412)
                        )
                    }
                }
            }
        }
    }
}

/**
 * Detailed Horoscope Card for the selected sign.
 */
@Composable
fun ActiveHoroscopeCard(
    horoscope: OdiaHoroscopeEntity,
    language: AppLanguage
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .testTag("active_horoscope_card"),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.5.dp, Color(0xFFFED7AA))
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            // Header Row: Symbol + Odia & English Name + Element & Planet
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = CircleShape,
                    color = Color(0xFFFFFBEB),
                    border = BorderStroke(2.dp, Color(0xFFF59E0B)),
                    modifier = Modifier.size(56.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(text = horoscope.symbol, fontSize = 28.sp)
                    }
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "${horoscope.signNameOr} ରାଶି",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Black,
                                color = Color(0xFF78350F)
                            )
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "(${horoscope.signNameEn})",
                            fontSize = 13.sp,
                            color = Color(0xFF9A3412),
                            fontWeight = FontWeight.SemiBold
                        )
                    }

                    Row(
                        modifier = Modifier.padding(top = 2.dp),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = Color(0xFFFEF3C7)
                        ) {
                            Text(
                                text = horoscope.elementOr,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF92400E),
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }

                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = Color(0xFFEFF6FF)
                        ) {
                            Text(
                                text = "ସ୍ୱାମୀ: ${horoscope.rulingPlanetOr}",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF1E40AF),
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Main Horoscope Prediction Text
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = Color(0xFFFFFDF5),
                border = BorderStroke(1.dp, Color(0xFFFDE68A)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.AutoAwesome,
                            contentDescription = null,
                            tint = Color(0xFFD97706),
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = if (language == AppLanguage.ODIA) "ଆଜିର ସାମଗ୍ରିକ ଫଳାଫଳ (Overall Prediction)" else "Today's Cosmic Prediction",
                            style = MaterialTheme.typography.titleSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF92400E)
                            )
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = horoscope.predictionOr,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontSize = 13.5.sp,
                            lineHeight = 21.sp,
                            color = Color(0xFF451A03)
                        )
                    )

                    if (!horoscope.predictionEn.isNullOrBlank()) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "[Astrology API Feed]: ${horoscope.predictionEn}",
                            fontSize = 11.sp,
                            color = Color(0xFF78350F).copy(alpha = 0.8f),
                            lineHeight = 15.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 4 Key Domain Forecasts (Career, Finance, Health, Family)
            Text(
                text = if (language == AppLanguage.ODIA) "ବିଭାଗୀୟ ରାଶି ଫଳାଫଳ" else "Specific Domain Forecasts",
                style = MaterialTheme.typography.titleSmall.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF78350F)
                )
            )

            Spacer(modifier = Modifier.height(8.dp))

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                ForecastAspectRow(
                    icon = "💼",
                    title = "କର୍ମକ୍ଷେତ୍ର ଓ ବ୍ୟବସାୟ (Career)",
                    desc = horoscope.careerFinanceOr,
                    bgColor = Color(0xFFF0FDF4),
                    borderColor = Color(0xFFBBF7D0)
                )
                ForecastAspectRow(
                    icon = "🩺",
                    title = "ସ୍ୱାସ୍ଥ୍ୟ ଓ ଆରୋଗ୍ୟ (Health)",
                    desc = horoscope.healthOr,
                    bgColor = Color(0xFFEFF6FF),
                    borderColor = Color(0xFFBFDBFE)
                )
                ForecastAspectRow(
                    icon = "👨‍👩‍👧",
                    title = "ପରିବାର ଓ ସମ୍ପର୍କ (Family)",
                    desc = horoscope.familyOr,
                    bgColor = Color(0xFFFAF5FF),
                    borderColor = Color(0xFFE9D5FF)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Lucky Metas Grid (Lucky Number, Color, Gemstone, Deity)
            Text(
                text = if (language == AppLanguage.ODIA) "ଶୁଭ ଉପାଦାନ (Lucky Elements)" else "Auspicious Elements",
                style = MaterialTheme.typography.titleSmall.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF78350F)
                )
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                LuckyElementBox(
                    label = "ଶୁଭ ସଂଖ୍ୟା",
                    value = horoscope.luckyNumber.toString(),
                    icon = "🔢",
                    modifier = Modifier.weight(1f)
                )
                LuckyElementBox(
                    label = "ଶୁଭ ରଙ୍ଗ",
                    value = horoscope.luckyColor,
                    icon = "🎨",
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                LuckyElementBox(
                    label = "ଶୁଭ ରତ୍ନ",
                    value = horoscope.luckyGemstone,
                    icon = "💎",
                    modifier = Modifier.weight(1f)
                )
                LuckyElementBox(
                    label = "ଆରାଧ୍ୟ ଦେବତା",
                    value = horoscope.worshipDeity,
                    icon = "🛕",
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
fun ForecastAspectRow(
    icon: String,
    title: String,
    desc: String,
    bgColor: Color,
    borderColor: Color
) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = bgColor,
        border = BorderStroke(1.dp, borderColor),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(10.dp),
            verticalAlignment = Alignment.Top
        ) {
            Text(text = icon, fontSize = 16.sp)
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text(
                    text = title,
                    fontSize = 11.5.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1E293B)
                )
                Text(
                    text = desc,
                    fontSize = 12.sp,
                    color = Color(0xFF334155),
                    lineHeight = 17.sp,
                    modifier = Modifier.padding(top = 2.dp)
                )
            }
        }
    }
}

@Composable
fun LuckyElementBox(
    label: String,
    value: String,
    icon: String,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = Color(0xFFFFFBEB),
        border = BorderStroke(1.dp, Color(0xFFFDE68A)),
        modifier = modifier
    ) {
        Column(modifier = Modifier.padding(10.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = icon, fontSize = 12.sp)
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = label,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF92400E)
                )
            }
            Text(
                text = value,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF451A03),
                maxLines = 2,
                modifier = Modifier.padding(top = 2.dp)
            )
        }
    }
}

/**
 * Auspicious & Inauspicious Timings Card.
 */
@Composable
fun AuspiciousTimingsCard(
    horoscope: OdiaHoroscopeEntity,
    language: AppLanguage
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
            .testTag("auspicious_timings_card"),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.5.dp, Color(0xFFBBF7D0))
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Schedule,
                    contentDescription = null,
                    tint = Color(0xFF15803D),
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = if (language == AppLanguage.ODIA) "ଆଜିର ଶୁଭ ଓ ଅଶୁଭ ସମୟ (Daily Muhurat)" else "Daily Auspicious & Inauspicious Hours",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF14532D)
                    )
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Shubha Bela (Auspicious)
            TimingRow(
                badge = "ଶୁଭ ବେଳା",
                badgeColor = Color(0xFF16A34A),
                time = horoscope.shubhaBela,
                desc = "ନୂତନ କାର୍ଯ୍ୟ ଆରମ୍ଭ ଓ ଯାତ୍ରା ପାଇଁ ଅତ୍ୟନ୍ତ ଉପଯୁକ୍ତ।"
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Amrita Bela
            TimingRow(
                badge = "ଅମୃତ ବେଳା",
                badgeColor = Color(0xFF0284C7),
                time = horoscope.amritaBela,
                desc = "ସର୍ବଶ୍ରେଷ୍ଠ ମୁହୂର୍ତ୍ତ, ଧନପ୍ରାପ୍ତି ଓ ଧାର୍ମିକ କାର୍ଯ୍ୟ ପାଇଁ ଶୁଭ।"
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Rahu Kala (Inauspicious)
            TimingRow(
                badge = "ରାହୁ କାଳ",
                badgeColor = Color(0xFFDC2626),
                time = horoscope.rahuKala,
                desc = "ଅଶୁଭ ସମୟ। ଶୁଭ କାର୍ଯ୍ୟ, ଯାତ୍ରା କିମ୍ବା ନୂତନ କ୍ରୟରୁ ନିବୃତ୍ତ ରୁହନ୍ତୁ।"
            )
        }
    }
}

@Composable
fun TimingRow(
    badge: String,
    badgeColor: Color,
    time: String,
    desc: String
) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = badgeColor.copy(alpha = 0.08f),
        border = BorderStroke(1.dp, badgeColor.copy(alpha = 0.25f)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(10.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = badgeColor
                ) {
                    Text(
                        text = badge,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }

                Text(
                    text = time,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1E293B)
                )
            }

            Text(
                text = desc,
                fontSize = 11.sp,
                color = Color(0xFF475569),
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}

/**
 * Remedial Chanting Mantra Card.
 */
@Composable
fun RemedialMantraCard(
    horoscope: OdiaHoroscopeEntity,
    language: AppLanguage,
    context: Context
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
            .testTag("remedial_mantra_card"),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFFBEB)),
        border = BorderStroke(1.5.dp, Color(0xFFF59E0B))
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = "🕉️", fontSize = 20.sp)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (language == AppLanguage.ODIA) "ଆଜିର ଶୁଭ ମନ୍ତ୍ର ଜପ" else "Remedial Chanting Mantra",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF78350F)
                        )
                    )
                }

                Button(
                    onClick = {
                        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                        val clip = ClipData.newPlainText("Odia Mantra", horoscope.chantingMantra)
                        clipboard.setPrimaryClip(clip)
                        Toast.makeText(context, "ମନ୍ତ୍ର କପି ହୋଇଛି (Mantra copied)", Toast.LENGTH_SHORT).show()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFB45309)),
                    shape = RoundedCornerShape(10.dp),
                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                    modifier = Modifier.height(32.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.ContentCopy,
                        contentDescription = "Copy Mantra",
                        tint = Color.White,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = "କପି", fontSize = 11.sp, color = Color.White)
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Surface(
                shape = RoundedCornerShape(12.dp),
                color = Color.White,
                border = BorderStroke(1.dp, Color(0xFFFDE68A)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier.padding(14.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = horoscope.chantingMantra,
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Black,
                            color = Color(0xFFB45309),
                            fontSize = 18.sp,
                            textAlign = TextAlign.Center
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "ପ୍ରତ୍ୟହ ସ୍ନାନ ପରେ ୧୦୮ ଥର ଏହି ମନ୍ତ୍ର ଜପ କଲେ ଗ୍ରହ ଦୋଷ ଖଣ୍ଡନ ହୋଇ ଶାନ୍ତି ମିଳିଥାଏ।",
                fontSize = 11.sp,
                color = Color(0xFF92400E)
            )
        }
    }
}

/**
 * Odia Panjika details view.
 */
@Composable
fun OdiaPanjikaSection(language: AppLanguage) {
    Column(modifier = Modifier.padding(16.dp)) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            border = BorderStroke(1.5.dp, Color(0xFFFED7AA))
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                Text(
                    text = "📜 ଶ୍ରୀ ଜଗନ୍ନାଥ ପାଞ୍ଜି ଓ କାଳଦର୍ଶୀ",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Black,
                        color = Color(0xFF78350F)
                    )
                )

                Spacer(modifier = Modifier.height(12.dp))

                PanjikaItemRow("ଓଡ଼ିଆ ସାଲ", "୧୪୩୨ ସାଲ (Odia Sal 1432)")
                PanjikaItemRow("ମାସ", "ଆଶ୍ୱିନ ମାସ (Ashwina Masa)")
                PanjikaItemRow("ପକ୍ଷ", "ଶୁକ୍ଳ ପକ୍ଷ (Shukla Paksha)")
                PanjikaItemRow("ତିଥି", "ଦ୍ୱାଦଶୀ (Dwadashi)")
                PanjikaItemRow("ନକ୍ଷତ୍ର", "ଶ୍ରବଣା (Shravana)")
                PanjikaItemRow("ଯୋଗ", "ସିଦ୍ଧି ଯୋଗ (Siddhi Yoga)")
                PanjikaItemRow("କରଣ", "ବବ କରଣ (Bava Karana)")
                PanjikaItemRow("ଚନ୍ଦ୍ର ରାଶି", "ମକର ରାଶି (Makara Rasi)")
                PanjikaItemRow("ଚନ୍ଦ୍ରାଷ୍ଟମ", "ମିଥୁନ ରାଶି (Gemini caution)")
            }
        }
    }
}

@Composable
fun PanjikaItemRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 5.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = label, fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF78350F))
        Text(text = value, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1E293B))
    }
}

/**
 * Balasore Temples & Rituals Section.
 */
@Composable
fun BalasoreTemplesSection(language: AppLanguage) {
    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        TempleCard(
            name = "ଖିରଚୋରା ଗୋପୀନାଥ ମନ୍ଦିର (ରେମୁଣା)",
            englishName = "Khirachora Gopinatha Temple, Remuna",
            bhog = "ପ୍ରସିଦ୍ଧ ଅମୃତ କେଳି ଖିରି ଭୋଗ (Kshira Bhog)",
            timing = "ସକାଳ ୦୬:୦୦ - ରାତ୍ରି ୦୯:୦୦",
            significance = "ଶ୍ରୀ ଚୈତନ୍ୟ ମହାପ୍ରଭୁ ଓ ମାଧବେନ୍ଦ୍ର ପୁରୀଙ୍କ ପବିତ୍ର ସ୍ମୃତିସ୍ଥଳୀ।"
        )
        TempleCard(
            name = "ଇମାମି ଜଗନ୍ନାଥ ମନ୍ଦିର (ବାଲେଶ୍ୱର)",
            englishName = "Emami Jagannath Temple, Balasore",
            bhog = "ମହାପ୍ରସାଦ ଓ ଅନ୍ନଭୋଗ (Mahaprasad)",
            timing = "ସକାଳ ୦୫:୩୦ - ରାତ୍ରି ୦୮:୩୦",
            significance = "ବାଲେଶ୍ୱରର ନୂତନ ଶ୍ରୀକ୍ଷେତ୍ର ଭାବରେ ପ୍ରସିଦ୍ଧ କଳାକୃତିପୂର୍ଣ୍ଣ ମନ୍ଦିର।"
        )
        TempleCard(
            name = "ପଞ୍ଚଲିଙ୍ଗେଶ୍ୱର ମହାଦେବ (ନୀଳଗିରି)",
            englishName = "Panchalingeswar Temple, Nilagiri",
            bhog = "ଜଳାଭିଷେକ ଓ ବେଲପତ୍ର ଭୋଗ",
            timing = "ସକାଳ ୦୬:୦୦ - ସନ୍ଧ୍ୟା ୦୬:୩୦",
            significance = "ପାହାଡ଼ର ପ୍ରାକୃତିକ ଝରଣା ମଧ୍ୟରେ ୫ଟି ସ୍ୱୟଂଭୂ ଶିବଲିଙ୍ଗ।"
        )
    }
}

@Composable
fun TempleCard(
    name: String,
    englishName: String,
    bhog: String,
    timing: String,
    significance: String
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, Color(0xFFFED7AA))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = name, style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = Color(0xFF78350F)))
            Text(text = englishName, fontSize = 11.sp, color = Color(0xFF9A3412))
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "ଭୋଗ: $bhog", fontSize = 12.sp, color = Color(0xFF1E293B))
            Text(text = "ଦର୍ଶନ ସମୟ: $timing", fontSize = 12.sp, color = Color(0xFF1E293B))
            Text(text = significance, fontSize = 11.5.sp, color = Color(0xFF64748B), modifier = Modifier.padding(top = 4.dp))
        }
    }
}

/**
 * Baleswari Literature & Proverbs Section.
 */
@Composable
fun BaleswariLiteratureSection(language: AppLanguage) {
    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFFFFBEB)),
            border = BorderStroke(1.5.dp, Color(0xFFF59E0B))
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "ଭ୍ୟାସକବି ଫକୀର ମୋହନ ସେନାପତିଙ୍କ ଭୂମି",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = Color(0xFF78350F))
                )
                Text(
                    text = "ବାଲେଶ୍ୱର ଓଡ଼ିଆ ଭାଷା ଆନ୍ଦୋଳନର ପ୍ରାଣକେନ୍ଦ୍ର। 'ରେବତୀ' ଓ 'ଛ' ମାଣ ଆଠ ଗୁଣ୍ଠ'ର ସ୍ରଷ୍ଟା ଫକୀର ମୋହନଙ୍କ ଏହି ପବିତ୍ର ମାଟି ଓଡ଼ିଆ ଭାଷାକୁ ଅମର କରିଛି।",
                    fontSize = 12.sp,
                    color = Color(0xFF451A03),
                    lineHeight = 18.sp,
                    modifier = Modifier.padding(top = 6.dp)
                )
            }
        }

        Text(
            text = "ବାଲେଶ୍ୱରୀୟ ଢଗ ଓ ଲୋକକଥା (Baleswari Proverbs)",
            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold, color = Color(0xFF78350F))
        )

        ProverbCard(
            proverb = "\"ହାତୀ ମଲେ ଲକ୍ଷେ, ଜିଇଁଲେ ଲକ୍ଷେ\"",
            meaning = "ଗୁଣୀ ବ୍ୟକ୍ତି ସବୁ ପରିସ୍ଥିତିରେ ଆଦରଣୀୟ।"
        )
        ProverbCard(
            proverb = "\"ବାଲେଶ୍ୱର ମାଟି, ଭାଷା ଓ କାଠି\"",
            meaning = "ସ୍ୱାଭିମାନ ଓ ନିଷ୍ଠାପର କାର୍ଯ୍ୟର ପ୍ରତୀକ।"
        )
    }
}

@Composable
fun ProverbCard(proverb: String, meaning: String) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = Color.White,
        border = BorderStroke(1.dp, Color(0xFFFED7AA)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(text = proverb, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color(0xFF9A3412))
            Text(text = "ଅର୍ଥ: $meaning", fontSize = 11.5.sp, color = Color(0xFF475569), modifier = Modifier.padding(top = 2.dp))
        }
    }
}
