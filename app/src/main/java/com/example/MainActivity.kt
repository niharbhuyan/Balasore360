package com.example

import android.app.Activity
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.automirrored.filled.Article
import androidx.compose.material.icons.automirrored.outlined.Article
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.outlined.AutoAwesome
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material.icons.filled.Emergency
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.outlined.Cloud
import androidx.compose.material.icons.outlined.Emergency
import androidx.compose.material.icons.outlined.Explore
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.icons.filled.BrightnessAuto
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.viewmodel.ThemeMode
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.admob.AdMobManager
import com.example.data.model.AppLanguage
import com.example.ui.screens.EssentialsScreen
import com.example.ui.screens.HotspotsMapView
import com.example.ui.screens.NewsScreen
import com.example.ui.screens.SpecialFeaturesScreen
import com.example.ui.screens.TourismScreen
import com.example.ui.screens.WeatherScreen
import com.example.ui.theme.Balasore360Theme
import com.example.ui.theme.BentoSlate100
import com.example.ui.theme.BentoSlate200
import com.example.ui.theme.BentoSlate400
import com.example.ui.theme.BentoSlate50
import com.example.ui.theme.BentoSlate500
import com.example.ui.theme.BentoSlate800
import com.example.ui.theme.BentoSlate900
import com.example.ui.theme.OceanBlue
import com.example.ui.theme.OceanBlueDark
import com.example.ui.viewmodel.BalasoreViewModel
import com.example.ui.viewmodel.UniqueFeatureSheetType
import com.example.ui.components.CycloneResilienceSheet
import com.example.ui.components.DefenseTrailSheet
import com.example.ui.components.ElephantPassportSheet
import com.example.ui.components.HarborCatchRatesSheet
import com.example.ui.components.HorseshoeCrabSheet
import com.example.ui.components.RemunaPrasadArtisansSheet
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import com.example.ui.features.coastal.ChandipurTideTimerSheet
import com.example.ui.features.coastal.CycloneShelterFinderSheet
import com.example.ui.features.heritage.BalasoreFoodTrailSheet
import com.example.ui.features.heritage.HeritageAudioSnippetsSheet
import com.example.ui.features.transit.TransitFareEstimatorSheet
import com.example.ui.features.transit.KuldihaSafariCompanionSheet
import com.example.ui.features.journal.TravelJournalSheet
import com.example.ui.features.emergency.BloodAndDialysisDirectorySheet
import com.example.ui.features.coastal.KasafalRedCrabsSheet
import com.example.ui.features.artisans.NilagiriStoneSabaiSheet
import com.example.ui.features.coastal.BalaramgadiEstuarySheet
import com.example.ui.features.heritage.ColonialHeritageWalkSheet
import com.example.ui.features.coastal.TalasariAuctionMonitorSheet
import com.example.ui.features.heritage.BaleswariyaDialectProverbsSheet
import com.example.ui.features.agro.PanBarajaAgroSheet
import com.example.ui.features.coastal.BichitrapurMangroveSheet
import com.example.ui.features.heritage.ChandaneswarChadakSheet
import com.example.ui.features.nature.PanchalingeswarStreamSheet
import com.example.ui.features.coastal.DarkSkyBioluminescenceSheet
import com.example.ui.features.artisans.BellMetalArtisansSheet
import com.example.ui.features.heritage.BuddhistJainCircuitSheet
import com.example.ui.features.agro.HeirloomRiceAgroSheet
import com.example.ui.features.heritage.LakhannathZamindariSheet
import com.example.ui.features.coastal.BudhabalangaRiverAnglingSheet
import com.example.ui.features.resilience.CycloneOralHistorySheet
import com.example.ui.features.health.DoctorsDirectorySheet
import com.example.ui.features.health.MedicineStoresDirectorySheet
import com.example.ui.features.health.PolyclinicDirectorySheet
import com.example.ui.features.health.PathologyLabDirectorySheet
import com.example.ui.features.coastal.RiverFloodTelemetrySheet
import com.example.ui.features.artisans.LacquerCraftArtisansSheet

import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material.icons.filled.SystemUpdate
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import com.example.util.PlayStoreUpdateManager
import com.example.util.UpdateUIState

class MainActivity : ComponentActivity() {

    private val viewModel: BalasoreViewModel by viewModels()
    private var updateManager: PlayStoreUpdateManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        try {
            updateManager = PlayStoreUpdateManager(this)
        } catch (t: Throwable) {
            Log.w("MainActivity", "PlayStoreUpdateManager safe fallback: ${t.message}")
        }

        try {
            viewModel.initDatabase(this)
        } catch (t: Throwable) {
            Log.e("MainActivity", "Safe initDatabase catch: ${t.message}")
        }

        // Crash-proof AdMob initialization
        try {
            AdMobManager.initialize(applicationContext)
        } catch (t: Throwable) {
            Log.e("MainActivity", "Safe AdMob init catch: ${t.message}")
        }

        setContent {
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()
            val isDarkTheme = when (uiState.themeMode) {
                ThemeMode.SYSTEM -> isSystemInDarkTheme()
                ThemeMode.LIGHT -> false
                ThemeMode.DARK -> true
            }
            Balasore360Theme(darkTheme = isDarkTheme) {
                BalasoreApp(
                    viewModel = viewModel,
                    updateManager = updateManager
                )
            }
        }
    }

    override fun onResume() {
        super.onResume()
        updateManager?.checkPendingUpdateCompletion()
    }

    override fun onDestroy() {
        super.onDestroy()
        updateManager?.cleanup()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BalasoreApp(
    viewModel: BalasoreViewModel,
    updateManager: PlayStoreUpdateManager? = null
) {
    val uiState by viewModel.uiState.collectAsState()
    val updateState = updateManager?.updateState?.collectAsState()?.value ?: UpdateUIState.Idle

    // Activity Result Launcher for Google Play In-App Updates
    val updateLauncher = androidx.activity.compose.rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartIntentSenderForResult()
    ) { result ->
        if (result.resultCode != Activity.RESULT_OK) {
            Log.w("BalasoreApp", "Update flow failed or cancelled: ${result.resultCode}")
        }
    }

    // Auto-check for updates on app launch
    LaunchedEffect(updateManager) {
        updateManager?.checkForUpdates(launcher = updateLauncher, autoStartFlexible = true)
    }

    // System Back Press handling: dismiss active bottom sheet first, then switch back to Explore tab
    BackHandler(enabled = uiState.activeFeatureSheet != null || uiState.selectedTab != 0) {
        if (uiState.activeFeatureSheet != null) {
            viewModel.closeFeatureSheet()
        } else if (uiState.selectedTab != 0) {
            viewModel.selectTab(0)
        }
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .testTag("balasore_main_scaffold"),
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Balasore 360",
                                style = MaterialTheme.typography.titleLarge.copy(
                                    fontWeight = FontWeight.ExtraBold,
                                    fontSize = 20.sp,
                                    color = OceanBlueDark
                                )
                            )
                            Text(
                                text = "ବାଲେଶ୍ୱର • ଚାନ୍ଦିପୁର ସ୍ପନ୍ଦନ",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontSize = 10.sp,
                                    color = BentoSlate500
                                )
                            )
                        }

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            // Theme Mode Toggle Button
                            ThemeTogglePill(
                                currentMode = uiState.themeMode,
                                onCycleTheme = { viewModel.cycleThemeMode() }
                            )

                            // Language Toggle Pills
                            Row(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(20.dp))
                                    .background(MaterialTheme.colorScheme.surfaceVariant)
                                    .padding(2.dp),
                                horizontalArrangement = Arrangement.spacedBy(2.dp)
                            ) {
                                LanguagePill(
                                    title = "EN",
                                    isSelected = uiState.language == AppLanguage.ENGLISH,
                                    onClick = { viewModel.setLanguage(AppLanguage.ENGLISH) }
                                )
                                LanguagePill(
                                    title = "ଓଡ଼ି",
                                    isSelected = uiState.language == AppLanguage.ODIA,
                                    onClick = { viewModel.setLanguage(AppLanguage.ODIA) }
                                )
                                LanguagePill(
                                    title = "HI",
                                    isSelected = uiState.language == AppLanguage.HINDI,
                                    onClick = { viewModel.setLanguage(AppLanguage.HINDI) }
                                )
                            }
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        },
        bottomBar = {
            NavigationBar(
                modifier = Modifier.testTag("balasore_bottom_nav"),
                containerColor = MaterialTheme.colorScheme.surface,
                tonalElevation = 8.dp
            ) {
                NavigationBarItem(
                    selected = uiState.selectedTab == 0,
                    onClick = { viewModel.selectTab(0) },
                    icon = {
                        Icon(
                            imageVector = if (uiState.selectedTab == 0) Icons.Default.Explore else Icons.Outlined.Explore,
                            contentDescription = "Hotspots"
                        )
                    },
                    label = {
                        Text(
                            text = if (uiState.language == AppLanguage.ODIA) "ସ୍ଥାନ" else "Explore",
                            fontSize = 10.5.sp,
                            maxLines = 1
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = MaterialTheme.colorScheme.primary,
                        selectedTextColor = MaterialTheme.colorScheme.primary,
                        indicatorColor = MaterialTheme.colorScheme.primaryContainer,
                        unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                        unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                    )
                )

                NavigationBarItem(
                    selected = uiState.selectedTab == 1,
                    onClick = { viewModel.selectTab(1) },
                    icon = {
                        Icon(
                            imageVector = if (uiState.selectedTab == 1) Icons.Default.AutoAwesome else Icons.Outlined.AutoAwesome,
                            contentDescription = "Special Features"
                        )
                    },
                    label = {
                        Text(
                            text = if (uiState.language == AppLanguage.ODIA) "ସ୍ୱତନ୍ତ୍ର" else "Special",
                            fontSize = 10.5.sp,
                            maxLines = 1
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = MaterialTheme.colorScheme.primary,
                        selectedTextColor = MaterialTheme.colorScheme.primary,
                        indicatorColor = MaterialTheme.colorScheme.primaryContainer,
                        unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                        unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                    )
                )

                NavigationBarItem(
                    selected = uiState.selectedTab == 2,
                    onClick = { viewModel.selectTab(2) },
                    icon = {
                        Icon(
                            imageVector = if (uiState.selectedTab == 2) Icons.AutoMirrored.Filled.Article else Icons.AutoMirrored.Outlined.Article,
                            contentDescription = "News"
                        )
                    },
                    label = {
                        Text(
                            text = if (uiState.language == AppLanguage.ODIA) "ଖବର" else "News",
                            fontSize = 10.5.sp,
                            maxLines = 1
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = MaterialTheme.colorScheme.primary,
                        selectedTextColor = MaterialTheme.colorScheme.primary,
                        indicatorColor = MaterialTheme.colorScheme.primaryContainer,
                        unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                        unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                    )
                )

                NavigationBarItem(
                    selected = uiState.selectedTab == 3,
                    onClick = { viewModel.selectTab(3) },
                    icon = {
                        Icon(
                            imageVector = if (uiState.selectedTab == 3) Icons.Default.Cloud else Icons.Outlined.Cloud,
                            contentDescription = "Weather"
                        )
                    },
                    label = {
                        Text(
                            text = if (uiState.language == AppLanguage.ODIA) "ପାଣିପାଗ" else "Weather",
                            fontSize = 10.5.sp,
                            maxLines = 1
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = MaterialTheme.colorScheme.primary,
                        selectedTextColor = MaterialTheme.colorScheme.primary,
                        indicatorColor = MaterialTheme.colorScheme.primaryContainer,
                        unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                        unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                    )
                )

                NavigationBarItem(
                    selected = uiState.selectedTab == 4,
                    onClick = { viewModel.selectTab(4) },
                    icon = {
                        Icon(
                            imageVector = if (uiState.selectedTab == 4) Icons.Default.Emergency else Icons.Outlined.Emergency,
                            contentDescription = "Essentials"
                        )
                    },
                    label = {
                        Text(
                            text = if (uiState.language == AppLanguage.ODIA) "ସହାୟତା" else "Essentials",
                            fontSize = 10.5.sp,
                            maxLines = 1
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = MaterialTheme.colorScheme.primary,
                        selectedTextColor = MaterialTheme.colorScheme.primary,
                        indicatorColor = MaterialTheme.colorScheme.primaryContainer,
                        unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                        unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                    )
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Prominent banner when flexible background update has finished downloading
            if (updateState is UpdateUIState.Downloaded) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = androidx.compose.material3.CardDefaults.cardColors(containerColor = Color(0xFFDCFCE7)),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF86EFAC))
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(
                                imageVector = Icons.Default.SystemUpdate,
                                contentDescription = null,
                                tint = Color(0xFF166534),
                                modifier = Modifier.size(22.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Text(
                                    text = "Update Ready to Install",
                                    style = MaterialTheme.typography.labelLarge.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF166534)
                                    )
                                )
                                Text(
                                    text = "Restart app to apply the newest features.",
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        color = Color(0xFF15803D),
                                        fontSize = 11.sp
                                    )
                                )
                            }
                        }
                        Button(
                            onClick = { updateManager?.completeUpdate() },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF166534)),
                            shape = RoundedCornerShape(8.dp),
                            contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Text("Restart", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            Surface(
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colorScheme.background
            ) {
                when (uiState.selectedTab) {
                    0 -> TourismScreen(
                        hotspots = uiState.hotspots,
                        templeRitualInfo = uiState.templeRitualInfo,
                        literaryTrailPoints = uiState.literaryTrailPoints,
                        dailyIdiom = uiState.dailyIdiom,
                        selectedCategory = uiState.selectedHotspotCategory,
                        searchQuery = uiState.hotspotSearchQuery,
                        language = uiState.language,
                        favoriteIds = uiState.favoriteHotspotIds,
                        weather = uiState.weather,
                        forecastDays = uiState.forecastDays,
                        weatherAlerts = uiState.weatherAlerts,
                        isFahrenheit = uiState.isFahrenheit,
                        selectedForecastIndex = uiState.selectedForecastDayIndex,
                        onCategorySelected = { viewModel.setHotspotCategory(it) },
                        onSearchChanged = { viewModel.setHotspotSearchQuery(it) },
                        onToggleFavorite = { viewModel.toggleFavoriteHotspot(it) },
                        onToggleTempUnit = { viewModel.toggleTemperatureUnit() },
                        onSelectForecastDay = { viewModel.selectForecastDay(it) },
                        onAcknowledgeAlert = { viewModel.acknowledgeAlert(it) },
                        onNavigateToWeather = { viewModel.selectTab(3) },
                        onNavigateToSpecialFeatures = { viewModel.selectTab(1) },
                        onOpenFeatureSheet = { viewModel.openFeatureSheet(it) },
                        itineraryItems = uiState.itineraryItems,
                        liveEmergencyAlerts = uiState.liveEmergencyAlerts,
                        dismissedAlertIds = uiState.dismissedAlertIds,
                        onDismissAlert = { viewModel.dismissEmergencyAlert(it) },
                        onAddToItinerary = { spot, day, time, notes ->
                            viewModel.addToItinerary(spot, day, time, notes)
                        },
                        onRemoveFromItinerary = { viewModel.removeFromItinerary(it) },
                        onClearItinerary = { viewModel.clearItinerary() },
                        dailyPulse = uiState.dailyPulse,
                        onRefreshDailyPulse = { viewModel.refreshDailyPulse() }
                    )
                    1 -> SpecialFeaturesScreen(
                        dailyPulse = uiState.dailyPulse,
                        language = uiState.language,
                        onRefreshDailyPulse = { viewModel.refreshDailyPulse() },
                        onOpenFeatureSheet = { viewModel.openFeatureSheet(it) }
                    )
                    2 -> NewsScreen(
                        articles = uiState.newsArticles,
                        riverGauges = uiState.riverGauges,
                        cycloneShelters = uiState.cycloneShelters,
                        selectedCategory = uiState.selectedNewsCategory,
                        searchQuery = uiState.newsSearchQuery,
                        language = uiState.language,
                        bookmarkedIds = uiState.bookmarkedIds,
                        isRefreshing = uiState.isRefreshing,
                        onCategorySelected = { viewModel.setNewsCategory(it) },
                        onSearchQueryChanged = { viewModel.setNewsSearchQuery(it) },
                        onToggleBookmark = { viewModel.toggleBookmark(it) },
                        onRefresh = { viewModel.refreshAll() },
                        onOpenFeatureSheet = { viewModel.openFeatureSheet(it) }
                    )
                    3 -> WeatherScreen(
                        weather = uiState.weather,
                        tidalClock = uiState.tidalClock,
                        drdoAdvisories = uiState.drdoAdvisories,
                        language = uiState.language,
                        lastKnownTide = uiState.lastKnownTideForecast,
                        onOpenFeatureSheet = { viewModel.openFeatureSheet(it) }
                    )
                    4 -> EssentialsScreen(
                        emergencyContacts = uiState.emergencyContacts,
                        transitList = uiState.transitList,
                        seafoodCatches = uiState.seafoodCatches,
                        language = uiState.language,
                        updateState = updateState,
                        themeMode = uiState.themeMode,
                        onThemeModeChange = { viewModel.setThemeMode(it) },
                        onCheckForUpdates = { updateManager?.checkForUpdates(updateLauncher, autoStartFlexible = false) },
                        onTriggerUpdate = { updateManager?.startUpdate(updateLauncher) },
                        onCompleteUpdate = { updateManager?.completeUpdate() },
                        onOpenFeatureSheet = { viewModel.openFeatureSheet(it) }
                    )
                }
            }

            // Active Unique Feature Modal Sheet
            when (uiState.activeFeatureSheet) {
                UniqueFeatureSheetType.HORSESHOE_CRAB -> {
                    HorseshoeCrabSheet(
                        sightings = uiState.horseshoeCrabSightings,
                        guidelines = uiState.intertidalEcoGuidelines,
                        onLogSighting = { reporter, sector, species, count ->
                            viewModel.logHorseshoeCrabSighting(reporter, sector, species, count)
                        },
                        onDismiss = { viewModel.closeFeatureSheet() }
                    )
                }
                UniqueFeatureSheetType.DEFENSE_TRAIL -> {
                    DefenseTrailSheet(
                        milestones = uiState.defenseMilestones,
                        exclusionZone = uiState.maritimeExclusionZone,
                        onDismiss = { viewModel.closeFeatureSheet() }
                    )
                }
                UniqueFeatureSheetType.REMUNA_PRASAD_ARTISANS -> {
                    RemunaPrasadArtisansSheet(
                        prasadStatus = uiState.prasadStatus,
                        artisans = uiState.balasoreArtisans,
                        onDismiss = { viewModel.closeFeatureSheet() }
                    )
                }
                UniqueFeatureSheetType.CYCLONE_RESILIENCE -> {
                    CycloneResilienceSheet(
                        shelters = uiState.detailedCycloneShelters,
                        kitItems = uiState.emergencyKitItems,
                        onToggleKitItem = { viewModel.toggleEmergencyKitItem(it) },
                        onDismiss = { viewModel.closeFeatureSheet() }
                    )
                }
                UniqueFeatureSheetType.HARBOR_CATCH_RATES -> {
                    HarborCatchRatesSheet(
                        catchRates = uiState.harborCatchRates,
                        landingBells = uiState.harborLandingBells,
                        onDismiss = { viewModel.closeFeatureSheet() }
                    )
                }
                UniqueFeatureSheetType.ELEPHANT_PASSPORT -> {
                    ElephantPassportSheet(
                        corridorAlert = uiState.elephantCorridorAlert,
                        passportStamps = uiState.ecoPassportStamps,
                        onToggleStamp = { viewModel.toggleEcoPassportStamp(it) },
                        onDismiss = { viewModel.closeFeatureSheet() }
                    )
                }
                UniqueFeatureSheetType.CHANDIPUR_TIDE_TIMER -> {
                    ModalBottomSheet(
                        onDismissRequest = { viewModel.closeFeatureSheet() },
                        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
                    ) {
                        ChandipurTideTimerSheet(
                            onClose = { viewModel.closeFeatureSheet() },
                            lastKnownTide = uiState.lastKnownTideForecast
                        )
                    }
                }
                UniqueFeatureSheetType.CYCLONE_SHELTER_FINDER -> {
                    ModalBottomSheet(
                        onDismissRequest = { viewModel.closeFeatureSheet() },
                        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
                    ) {
                        CycloneShelterFinderSheet(
                            onClose = { viewModel.closeFeatureSheet() }
                        )
                    }
                }
                UniqueFeatureSheetType.BALASORE_FOOD_TRAIL -> {
                    ModalBottomSheet(
                        onDismissRequest = { viewModel.closeFeatureSheet() },
                        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
                    ) {
                        BalasoreFoodTrailSheet(
                            onClose = { viewModel.closeFeatureSheet() }
                        )
                    }
                }
                UniqueFeatureSheetType.BILINGUAL_HERITAGE_AUDIO -> {
                    ModalBottomSheet(
                        onDismissRequest = { viewModel.closeFeatureSheet() },
                        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
                    ) {
                        HeritageAudioSnippetsSheet(
                            onClose = { viewModel.closeFeatureSheet() }
                        )
                    }
                }
                UniqueFeatureSheetType.TRANSIT_FARE_ESTIMATOR -> {
                    ModalBottomSheet(
                        onDismissRequest = { viewModel.closeFeatureSheet() },
                        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
                    ) {
                        TransitFareEstimatorSheet(
                            onClose = { viewModel.closeFeatureSheet() }
                        )
                    }
                }
                UniqueFeatureSheetType.KULDIHA_SAFARI_COMPANION -> {
                    ModalBottomSheet(
                        onDismissRequest = { viewModel.closeFeatureSheet() },
                        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
                    ) {
                        KuldihaSafariCompanionSheet(
                            onClose = { viewModel.closeFeatureSheet() }
                        )
                    }
                }
                UniqueFeatureSheetType.TRAVEL_JOURNAL -> {
                    ModalBottomSheet(
                        onDismissRequest = { viewModel.closeFeatureSheet() },
                        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
                    ) {
                        TravelJournalSheet(
                            journalRepo = viewModel.travelJournalRepo,
                            onClose = { viewModel.closeFeatureSheet() }
                        )
                    }
                }
                UniqueFeatureSheetType.BLOOD_AND_DIALYSIS_DIRECTORY -> {
                    ModalBottomSheet(
                        onDismissRequest = { viewModel.closeFeatureSheet() },
                        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
                    ) {
                        BloodAndDialysisDirectorySheet(
                            onClose = { viewModel.closeFeatureSheet() }
                        )
                    }
                }
                UniqueFeatureSheetType.KASAFAL_RED_CRABS -> {
                    ModalBottomSheet(
                        onDismissRequest = { viewModel.closeFeatureSheet() },
                        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
                    ) {
                        KasafalRedCrabsSheet(
                            onClose = { viewModel.closeFeatureSheet() }
                        )
                    }
                }
                UniqueFeatureSheetType.NILAGIRI_STONE_SABAI -> {
                    ModalBottomSheet(
                        onDismissRequest = { viewModel.closeFeatureSheet() },
                        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
                    ) {
                        NilagiriStoneSabaiSheet(
                            onClose = { viewModel.closeFeatureSheet() }
                        )
                    }
                }
                UniqueFeatureSheetType.BALARAMGADI_ESTUARY -> {
                    ModalBottomSheet(
                        onDismissRequest = { viewModel.closeFeatureSheet() },
                        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
                    ) {
                        BalaramgadiEstuarySheet(
                            onClose = { viewModel.closeFeatureSheet() }
                        )
                    }
                }
                UniqueFeatureSheetType.COLONIAL_HERITAGE_WALK -> {
                    ModalBottomSheet(
                        onDismissRequest = { viewModel.closeFeatureSheet() },
                        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
                    ) {
                        ColonialHeritageWalkSheet(
                            onClose = { viewModel.closeFeatureSheet() }
                        )
                    }
                }
                UniqueFeatureSheetType.TALASARI_AUCTION_MONITOR -> {
                    ModalBottomSheet(
                        onDismissRequest = { viewModel.closeFeatureSheet() },
                        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
                    ) {
                        TalasariAuctionMonitorSheet(
                            onClose = { viewModel.closeFeatureSheet() }
                        )
                    }
                }
                UniqueFeatureSheetType.BALESWARIYA_DIALECT_PROVERBS -> {
                    ModalBottomSheet(
                        onDismissRequest = { viewModel.closeFeatureSheet() },
                        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
                    ) {
                        BaleswariyaDialectProverbsSheet(
                            onClose = { viewModel.closeFeatureSheet() }
                        )
                    }
                }
                UniqueFeatureSheetType.PAN_BARAJA_AGRO -> {
                    ModalBottomSheet(
                        onDismissRequest = { viewModel.closeFeatureSheet() },
                        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
                    ) {
                        PanBarajaAgroSheet(
                            onClose = { viewModel.closeFeatureSheet() }
                        )
                    }
                }
                UniqueFeatureSheetType.BICHITRAPUR_MANGROVE_BOATING -> {
                    ModalBottomSheet(
                        onDismissRequest = { viewModel.closeFeatureSheet() },
                        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
                    ) {
                        BichitrapurMangroveSheet(
                            onDismiss = { viewModel.closeFeatureSheet() }
                        )
                    }
                }
                UniqueFeatureSheetType.CHANDANESWAR_CHADAK_MELA -> {
                    ModalBottomSheet(
                        onDismissRequest = { viewModel.closeFeatureSheet() },
                        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
                    ) {
                        ChandaneswarChadakSheet(
                            onDismiss = { viewModel.closeFeatureSheet() }
                        )
                    }
                }
                UniqueFeatureSheetType.PANCHALINGESWAR_STREAM_SAFETY -> {
                    ModalBottomSheet(
                        onDismissRequest = { viewModel.closeFeatureSheet() },
                        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
                    ) {
                        PanchalingeswarStreamSheet(
                            onDismiss = { viewModel.closeFeatureSheet() }
                        )
                    }
                }
                UniqueFeatureSheetType.DARK_SKY_BIOLUMINESCENCE -> {
                    ModalBottomSheet(
                        onDismissRequest = { viewModel.closeFeatureSheet() },
                        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
                    ) {
                        DarkSkyBioluminescenceSheet(
                            onDismiss = { viewModel.closeFeatureSheet() }
                        )
                    }
                }
                UniqueFeatureSheetType.BELL_METAL_ARTISANS -> {
                    ModalBottomSheet(
                        onDismissRequest = { viewModel.closeFeatureSheet() },
                        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
                    ) {
                        BellMetalArtisansSheet(
                            onDismiss = { viewModel.closeFeatureSheet() }
                        )
                    }
                }
                UniqueFeatureSheetType.BUDDHIST_JAIN_CIRCUIT -> {
                    ModalBottomSheet(
                        onDismissRequest = { viewModel.closeFeatureSheet() },
                        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
                    ) {
                        BuddhistJainCircuitSheet(
                            onDismiss = { viewModel.closeFeatureSheet() }
                        )
                    }
                }
                UniqueFeatureSheetType.HEIRLOOM_RICE_AGRO -> {
                    ModalBottomSheet(
                        onDismissRequest = { viewModel.closeFeatureSheet() },
                        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
                    ) {
                        HeirloomRiceAgroSheet(
                            onDismiss = { viewModel.closeFeatureSheet() }
                        )
                    }
                }
                UniqueFeatureSheetType.LAKHANNATH_ZAMINDARI -> {
                    ModalBottomSheet(
                        onDismissRequest = { viewModel.closeFeatureSheet() },
                        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
                    ) {
                        LakhannathZamindariSheet(
                            onDismiss = { viewModel.closeFeatureSheet() }
                        )
                    }
                }
                UniqueFeatureSheetType.BUDHABALANGA_RIVER_ANGLING -> {
                    ModalBottomSheet(
                        onDismissRequest = { viewModel.closeFeatureSheet() },
                        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
                    ) {
                        BudhabalangaRiverAnglingSheet(
                            onDismiss = { viewModel.closeFeatureSheet() }
                        )
                    }
                }
                UniqueFeatureSheetType.CYCLONE_ORAL_HISTORY -> {
                    ModalBottomSheet(
                        onDismissRequest = { viewModel.closeFeatureSheet() },
                        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
                    ) {
                        CycloneOralHistorySheet(
                            onDismiss = { viewModel.closeFeatureSheet() }
                        )
                    }
                }
                UniqueFeatureSheetType.DOCTORS_DIRECTORY -> {
                    ModalBottomSheet(
                        onDismissRequest = { viewModel.closeFeatureSheet() },
                        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
                    ) {
                        DoctorsDirectorySheet(
                            onDismiss = { viewModel.closeFeatureSheet() }
                        )
                    }
                }
                UniqueFeatureSheetType.MEDICINE_STORES_DIRECTORY -> {
                    ModalBottomSheet(
                        onDismissRequest = { viewModel.closeFeatureSheet() },
                        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
                    ) {
                        MedicineStoresDirectorySheet(
                            onDismiss = { viewModel.closeFeatureSheet() }
                        )
                    }
                }
                UniqueFeatureSheetType.POLYCLINIC_DIRECTORY -> {
                    ModalBottomSheet(
                        onDismissRequest = { viewModel.closeFeatureSheet() },
                        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
                    ) {
                        PolyclinicDirectorySheet(
                            onDismiss = { viewModel.closeFeatureSheet() }
                        )
                    }
                }
                UniqueFeatureSheetType.PATHOLOGY_LAB_DIRECTORY -> {
                    ModalBottomSheet(
                        onDismissRequest = { viewModel.closeFeatureSheet() },
                        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
                    ) {
                        PathologyLabDirectorySheet(
                            onDismiss = { viewModel.closeFeatureSheet() }
                        )
                    }
                }
                UniqueFeatureSheetType.RIVER_FLOOD_TELEMETRY -> {
                    ModalBottomSheet(
                        onDismissRequest = { viewModel.closeFeatureSheet() },
                        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
                    ) {
                        RiverFloodTelemetrySheet(
                            onDismiss = { viewModel.closeFeatureSheet() }
                        )
                    }
                }
                UniqueFeatureSheetType.MATI_MANISHA_ARTISANS -> {
                    ModalBottomSheet(
                        onDismissRequest = { viewModel.closeFeatureSheet() },
                        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
                    ) {
                        LacquerCraftArtisansSheet(
                            onDismiss = { viewModel.closeFeatureSheet() }
                        )
                    }
                }
                UniqueFeatureSheetType.BALASORE_MAP_EXPLORER -> {
                    ModalBottomSheet(
                        onDismissRequest = { viewModel.closeFeatureSheet() },
                        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
                    ) {
                        HotspotsMapView(
                            language = uiState.language,
                            onClose = { viewModel.closeFeatureSheet() }
                        )
                    }
                }
                null -> {}
            }
        }
    }
}

@Composable
fun LanguagePill(
    title: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .background(if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent)
            .clickable { onClick() }
            .padding(horizontal = 8.dp, vertical = 4.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.labelSmall.copy(
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                color = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant,
                fontSize = 10.sp
            )
        )
    }
}

@Composable
fun ThemeTogglePill(
    currentMode: ThemeMode,
    onCycleTheme: () -> Unit,
    modifier: Modifier = Modifier
) {
    val (icon, tint, label) = when (currentMode) {
        ThemeMode.SYSTEM -> Triple(
            Icons.Default.BrightnessAuto,
            MaterialTheme.colorScheme.primary,
            "Auto"
        )
        ThemeMode.LIGHT -> Triple(
            Icons.Default.LightMode,
            Color(0xFFD97706),
            "Light"
        )
        ThemeMode.DARK -> Triple(
            Icons.Default.DarkMode,
            Color(0xFF38BDF8),
            "Dark"
        )
    }

    Surface(
        onClick = onCycleTheme,
        shape = RoundedCornerShape(20.dp),
        color = MaterialTheme.colorScheme.surfaceVariant,
        tonalElevation = 1.dp,
        modifier = modifier
            .testTag("theme_toggle_button")
            .height(26.dp)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 7.dp, vertical = 3.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = "Theme mode: $label (Tap to switch)",
                tint = tint,
                modifier = Modifier.size(14.dp)
            )
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall.copy(
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            )
        }
    }
}
