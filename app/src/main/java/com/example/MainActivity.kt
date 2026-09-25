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
import androidx.compose.foundation.BorderStroke
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Article
import androidx.compose.material.icons.automirrored.outlined.Article
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.outlined.AutoAwesome
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material.icons.filled.Emergency
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Sync
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
import androidx.compose.material.icons.filled.NightlightRound
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
import androidx.compose.ui.platform.LocalContext
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
import com.example.ui.components.AppWatermarkOverlay
import com.example.ui.components.CycloneResilienceSheet
import com.example.ui.components.DefenseTrailSheet
import com.example.ui.components.ElephantPassportSheet
import com.example.ui.features.civic.CitizenCivicEyeSheet
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
import com.example.ui.features.health.PrivateHospitalDirectorySheet
import com.example.ui.features.coastal.RiverFloodTelemetrySheet
import com.example.ui.features.artisans.LacquerCraftArtisansSheet
import com.example.ui.features.coastal.SaltPanHeritageSheet
import com.example.ui.features.coastal.HilsaMigrationSheet
import com.example.ui.features.nature.KuldihaElephantCorridorSheet
import com.example.ui.features.heritage.ChhenaGajaHotBatchSheet
import com.example.ui.features.transit.RiverFerryScheduleSheet
import com.example.ui.features.heritage.OdiaSahityaRevivalSheet
import com.example.ui.features.ai.AskBalasoreChatbotSheet
import com.example.ui.features.coastal.IncoisSeaAdvisorySheet
import com.example.ui.features.heritage.BaleswarPanjikaCalendarSheet
import com.example.ui.features.heritage.RaibaniaAudioStoryWalkSheet
import com.example.ui.features.emergency.BloodDonorBedPulseSheet
import com.example.ui.features.transit.BalasoreTotoFareCardSheet
import com.example.ui.features.civic.CampusCareerBoardSheet
import com.example.ui.features.resilience.AutoUpdateCenterSheet
import com.example.ui.features.transit.BalasoreJunctionRadarSheet
import com.example.ui.features.agro.BaleswariPaanKrushiMandiSheet
import com.example.ui.features.health.NightChemistSanjeevaniSheet
import com.example.ui.features.heritage.BaleswarMahotsavChadakSheet
import com.example.ui.features.artisans.NilagiriStoneArtisanSheet
import com.example.ui.features.civic.TpnodlWatcoMonitorSheet
import com.example.ui.features.coastal.TalasariMangroveExplorerSheet
import com.example.ui.features.civic.MoSevaKendraCitizenSheet
import com.example.ui.features.coastal.MarineFishermenSafetySheet
import com.example.ui.features.nature.KuldihaEcoCampSafariSheet
import com.example.ui.features.health.FmMchMedicalCollegeOpdSheet
import com.example.ui.features.artisans.SabaiGrassMissionShaktiSheet
import com.example.ui.features.heritage.BalasoreMaritimeColonialSheet
import com.example.ui.features.civic.BalasoreStudentCareerScholarshipSheet
import com.example.ui.features.coastal.DrdoChandipurSafetyRadarSheet
import com.example.ui.features.nature.SubarnarekhaSluiceFloodRadarSheet
import com.example.ui.features.artisans.NilagiriChhauCulturalGuildSheet
import com.example.ui.features.health.DhhBloodBankLiveRadarSheet
import com.example.ui.features.agro.PanBarajaAquaShrimpSheet
import com.example.ui.features.civic.BalasoreCourtLegalAidSheet
import com.example.ui.features.transit.Nh16HighwayPatrolTraumaSheet
import com.example.ui.features.nature.PanchalingeswarKuldihaSafariSheet
import com.example.ui.features.transit.SerBalasoreRailwayJunctionSheet
import com.example.ui.features.civic.NocciIndustrialB2bSkillSheet
import com.example.ui.features.coastal.TalsariBichitrapurMangrovePilotSheet
import com.example.ui.features.heritage.FakirMohanBhashaLiteratureSheet
import com.example.ui.features.heritage.BaleswariCuisineSweetHeritageSheet
import com.example.ui.features.civic.TpnodlWatcoUtilityHotlineSheet
import com.example.ui.components.LanguageToggleSwitch
import com.example.ui.features.agro.DhanMandiMspProcurementSheet
import com.example.ui.features.artisans.NilagiriGraniteStonewareGuildSheet
import com.example.ui.features.emergency.BahanagaNhaiRapidTraumaNetworkSheet
import com.example.ui.features.heritage.BhusandeswarChandaneswarPilgrimageSheet
import com.example.ui.features.civic.SeniorCitizenSevaSetuSheet
import com.example.ui.features.civic.BalasoreHigherEducationHubSheet
import com.example.ui.features.transit.BalasoreMoBusCrutNavigatorSheet
import com.example.ui.features.coastal.AquaShrimpHatcheryRadarSheet
import com.example.ui.features.resilience.CoastalCycloneShelterNetworkSheet
import com.example.ui.features.heritage.MaritimeForeignLogeTrailSheet
import com.example.ui.features.health.RuralMobileTelemedicineNetworkSheet
import com.example.ui.features.agro.BaleswariBetelPaddyCooperativeSheet
import com.example.ui.features.transit.CoastalCrutInterdistrictBusRadarSheet
import com.example.ui.features.nature.HorseshoeCrabIntertidalProtectionSheet
import com.example.ui.features.civic.CitizenFeedbackSheet
import androidx.compose.material.icons.filled.Feedback
import androidx.compose.material3.ExtendedFloatingActionButton

import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material.icons.filled.SystemUpdate
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import com.example.data.sync.SyncManager
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

        // Schedule WorkManager periodic data sync and cache maintenance
        try {
            SyncManager.schedulePeriodicSync(applicationContext)
        } catch (t: Throwable) {
            Log.w("MainActivity", "SyncManager schedulePeriodicSync fallback: ${t.message}")
        }

        setContent {
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()
            val isDarkTheme = when (uiState.themeMode) {
                ThemeMode.SYSTEM -> isSystemInDarkTheme()
                ThemeMode.LIGHT -> false
                ThemeMode.DARK -> true
                ThemeMode.HIGH_CONTRAST_DARK -> true
            }
            val isHighContrast = uiState.themeMode == ThemeMode.HIGH_CONTRAST_DARK
            Balasore360Theme(darkTheme = isDarkTheme, isHighContrast = isHighContrast) {
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
    val context = LocalContext.current
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
                            // Citizen Feedback & Suggestions Button (Firestore 'feedback' collection)
                            Surface(
                                shape = CircleShape,
                                color = MaterialTheme.colorScheme.surfaceVariant,
                                modifier = Modifier
                                    .size(28.dp)
                                    .clip(CircleShape)
                                    .clickable { viewModel.openFeatureSheet(UniqueFeatureSheetType.CITIZEN_FEEDBACK_PORTAL) }
                                    .testTag("top_bar_feedback_btn")
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(
                                        imageVector = Icons.Default.Feedback,
                                        contentDescription = "Submit Feedback or Report",
                                        tint = MaterialTheme.colorScheme.primary,
                                        modifier = Modifier.size(15.dp)
                                    )
                                }
                            }

                            // Auto-Update Quick Access Indicator Pill
                            Surface(
                                shape = RoundedCornerShape(20.dp),
                                color = if (uiState.isRefreshing) Color(0xFFE0F2FE) else MaterialTheme.colorScheme.surfaceVariant,
                                border = BorderStroke(1.dp, if (uiState.isRefreshing) Color(0xFF38BDF8) else Color.Transparent),
                                modifier = Modifier
                                    .clip(RoundedCornerShape(20.dp))
                                    .clickable { viewModel.openFeatureSheet(UniqueFeatureSheetType.BALASORE_AUTO_UPDATE_CENTER) }
                                    .testTag("top_bar_auto_update_btn")
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 7.dp, vertical = 4.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(7.dp)
                                            .clip(CircleShape)
                                            .background(if (uiState.isHourlyAutoRefreshEnabled) Color(0xFF16A34A) else Color(0xFFEF4444))
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Icon(
                                        imageVector = Icons.Default.Sync,
                                        contentDescription = "Auto Update",
                                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                        modifier = Modifier.size(12.dp)
                                    )
                                }
                            }

                            // Theme Mode Toggle Button
                            ThemeTogglePill(
                                currentMode = uiState.themeMode,
                                onCycleTheme = { viewModel.cycleThemeMode() }
                            )

                            // Dynamic Language Toggle Switch (English ⇄ Odia)
                            LanguageToggleSwitch(
                                currentLanguage = uiState.language,
                                onLanguageSelected = { viewModel.setLanguage(it) },
                                compact = true
                            )
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
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { viewModel.openFeatureSheet(UniqueFeatureSheetType.ASK_BALASORE_AI) },
                icon = {
                    Icon(
                        imageVector = Icons.Default.AutoAwesome,
                        contentDescription = "Ask Balasore AI",
                        tint = Color.White
                    )
                },
                text = {
                    Text(
                        text = if (uiState.language == AppLanguage.ODIA) "ବାଲେଶ୍ୱର AI" else "Ask Balasore AI",
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                },
                containerColor = Color(0xFF0284C7),
                modifier = Modifier.testTag("fab_ask_balasore_ai")
            )
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
                Box(modifier = Modifier.fillMaxSize()) {
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
                            onOpenFeatureSheet = { viewModel.openFeatureSheet(it) },
                            onLanguageSelected = { viewModel.setLanguage(it) }
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
                            onOpenFeatureSheet = { viewModel.openFeatureSheet(it) },
                            themeMode = uiState.themeMode,
                            onToggleNightMode = { viewModel.toggleHighContrastNightMode() }
                        )
                        3 -> WeatherScreen(
                            weather = uiState.weather,
                            forecastDays = uiState.forecastDays,
                            hourlyForecast = uiState.hourlyForecast,
                            isWeatherLoading = uiState.isWeatherLoading,
                            isFahrenheit = uiState.isFahrenheit,
                            onRefreshWeather = { viewModel.fetchRealTimeWeather() },
                            onToggleUnit = { viewModel.toggleTemperatureUnit() },
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
                            isWatermarkEnabled = uiState.isWatermarkEnabled,
                            watermarkStyle = uiState.watermarkStyle,
                            watermarkOpacity = uiState.watermarkOpacity,
                            onWatermarkToggle = { viewModel.setWatermarkEnabled(it) },
                            onWatermarkStyleChange = { viewModel.setWatermarkStyle(it) },
                            onWatermarkOpacityChange = { viewModel.setWatermarkOpacity(it) },
                            isHourlyAutoRefreshEnabled = uiState.isHourlyAutoRefreshEnabled,
                            lastHourlyRefreshTimestamp = uiState.lastHourlyRefreshTimestamp,
                            nextHourlyRefreshMinutesRemaining = uiState.nextHourlyRefreshMinutesRemaining,
                            autoRefreshCycleCount = uiState.autoRefreshCycleCount,
                            onHourlyAutoRefreshToggle = { viewModel.setHourlyAutoRefreshEnabled(it) },
                            onTriggerManualHourlyRefresh = {
                                viewModel.triggerHourlyAutoRefresh()
                                // Also trigger WorkManager background sync cache
                                SyncManager.triggerImmediateSync(context)
                            },
                            onCheckForUpdates = { updateManager?.checkForUpdates(updateLauncher, autoStartFlexible = false) },
                            onTriggerUpdate = { updateManager?.startUpdate(updateLauncher) },
                            onCompleteUpdate = { updateManager?.completeUpdate() },
                            onOpenFeatureSheet = { viewModel.openFeatureSheet(it) }
                        )
                    }

                    // Full App Watermark Overlay (Displays App Name & Logo across entire app)
                    AppWatermarkOverlay(
                        enabled = uiState.isWatermarkEnabled,
                        style = uiState.watermarkStyle,
                        opacity = uiState.watermarkOpacity
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
                UniqueFeatureSheetType.PRIVATE_HOSPITAL_DIRECTORY -> {
                    ModalBottomSheet(
                        onDismissRequest = { viewModel.closeFeatureSheet() },
                        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
                    ) {
                        PrivateHospitalDirectorySheet(
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
                UniqueFeatureSheetType.SALT_PAN_HERITAGE -> {
                    ModalBottomSheet(
                        onDismissRequest = { viewModel.closeFeatureSheet() },
                        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
                    ) {
                        SaltPanHeritageSheet(
                            onDismiss = { viewModel.closeFeatureSheet() }
                        )
                    }
                }
                UniqueFeatureSheetType.HILSA_MIGRATION -> {
                    ModalBottomSheet(
                        onDismissRequest = { viewModel.closeFeatureSheet() },
                        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
                    ) {
                        HilsaMigrationSheet(
                            onDismiss = { viewModel.closeFeatureSheet() }
                        )
                    }
                }
                UniqueFeatureSheetType.KULDIHA_ELEPHANT_CORRIDOR -> {
                    ModalBottomSheet(
                        onDismissRequest = { viewModel.closeFeatureSheet() },
                        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
                    ) {
                        KuldihaElephantCorridorSheet(
                            onDismiss = { viewModel.closeFeatureSheet() }
                        )
                    }
                }
                UniqueFeatureSheetType.CHHENA_GAJA_HOT_BATCH -> {
                    ModalBottomSheet(
                        onDismissRequest = { viewModel.closeFeatureSheet() },
                        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
                    ) {
                        ChhenaGajaHotBatchSheet(
                            onDismiss = { viewModel.closeFeatureSheet() }
                        )
                    }
                }
                UniqueFeatureSheetType.RIVER_FERRY_SCHEDULE -> {
                    ModalBottomSheet(
                        onDismissRequest = { viewModel.closeFeatureSheet() },
                        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
                    ) {
                        RiverFerryScheduleSheet(
                            onDismiss = { viewModel.closeFeatureSheet() }
                        )
                    }
                }
                UniqueFeatureSheetType.ODIA_SAHITYA_REVIVAL -> {
                    ModalBottomSheet(
                        onDismissRequest = { viewModel.closeFeatureSheet() },
                        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
                    ) {
                        OdiaSahityaRevivalSheet(
                            onDismiss = { viewModel.closeFeatureSheet() }
                        )
                    }
                }
                UniqueFeatureSheetType.ASK_BALASORE_AI -> {
                    ModalBottomSheet(
                        onDismissRequest = { viewModel.closeFeatureSheet() },
                        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
                    ) {
                        AskBalasoreChatbotSheet(
                            chatMessages = uiState.chatMessages,
                            isLoading = uiState.isChatLoading,
                            selectedModel = uiState.selectedChatModel,
                            selectedRole = uiState.selectedChatRole,
                            isSearchGroundingEnabled = uiState.isChatSearchGrounding,
                            isMapsGroundingEnabled = uiState.isChatMapsGrounding,
                            language = uiState.language,
                            onSendMessage = { viewModel.sendChatMessage(it) },
                            onSelectModel = { viewModel.setChatModel(it) },
                            onSelectRole = { viewModel.setChatRole(it) },
                            onToggleSearchGrounding = { viewModel.toggleChatSearchGrounding() },
                            onToggleMapsGrounding = { viewModel.toggleChatMapsGrounding() },
                            onClearChat = { viewModel.clearChat() },
                            onGenerateMusic = { prompt -> viewModel.generateLyriaMusic(prompt, isShortClip = true) },
                            onClose = { viewModel.closeFeatureSheet() }
                        )
                    }
                }
                UniqueFeatureSheetType.INCOIS_OCEAN_ADVISORY -> {
                    ModalBottomSheet(
                        onDismissRequest = { viewModel.closeFeatureSheet() },
                        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
                    ) {
                        IncoisSeaAdvisorySheet(
                            language = uiState.language,
                            onClose = { viewModel.closeFeatureSheet() }
                        )
                    }
                }
                UniqueFeatureSheetType.ODIA_PANJIKA_CALENDAR -> {
                    ModalBottomSheet(
                        onDismissRequest = { viewModel.closeFeatureSheet() },
                        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
                    ) {
                        BaleswarPanjikaCalendarSheet(
                            language = uiState.language,
                            onClose = { viewModel.closeFeatureSheet() }
                        )
                    }
                }
                UniqueFeatureSheetType.RAIBANIA_AUDIO_WALK -> {
                    ModalBottomSheet(
                        onDismissRequest = { viewModel.closeFeatureSheet() },
                        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
                    ) {
                        RaibaniaAudioStoryWalkSheet(
                            language = uiState.language,
                            onGenerateLyriaTrack = { prompt, isClip ->
                                viewModel.generateLyriaMusic(prompt, isClip)
                            },
                            isMusicGenerating = uiState.isMusicGenerating,
                            onClose = { viewModel.closeFeatureSheet() }
                        )
                    }
                }
                UniqueFeatureSheetType.BLOOD_AND_BED_PULSE -> {
                    ModalBottomSheet(
                        onDismissRequest = { viewModel.closeFeatureSheet() },
                        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
                    ) {
                        BloodDonorBedPulseSheet(
                            language = uiState.language,
                            onClose = { viewModel.closeFeatureSheet() }
                        )
                    }
                }
                UniqueFeatureSheetType.CITIZEN_CIVIC_EYE -> {
                    ModalBottomSheet(
                        onDismissRequest = { viewModel.closeFeatureSheet() },
                        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
                    ) {
                        CitizenCivicEyeSheet(
                            language = uiState.language,
                            onClose = { viewModel.closeFeatureSheet() }
                        )
                    }
                }
                UniqueFeatureSheetType.TOTO_AUTO_FARE_CARD -> {
                    ModalBottomSheet(
                        onDismissRequest = { viewModel.closeFeatureSheet() },
                        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
                    ) {
                        BalasoreTotoFareCardSheet(
                            language = uiState.language,
                            onClose = { viewModel.closeFeatureSheet() }
                        )
                    }
                }
                UniqueFeatureSheetType.BALASORE_CAMPUS_CAREER_BOARD -> {
                    ModalBottomSheet(
                        onDismissRequest = { viewModel.closeFeatureSheet() },
                        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
                    ) {
                        CampusCareerBoardSheet(
                            language = uiState.language,
                            onClose = { viewModel.closeFeatureSheet() }
                        )
                    }
                }
                UniqueFeatureSheetType.BALASORE_AUTO_UPDATE_CENTER -> {
                    ModalBottomSheet(
                        onDismissRequest = { viewModel.closeFeatureSheet() },
                        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
                    ) {
                        AutoUpdateCenterSheet(
                            language = uiState.language,
                            dailyPulse = uiState.dailyPulse,
                            isRefreshing = uiState.isRefreshing,
                            isHourlyAutoRefreshEnabled = uiState.isHourlyAutoRefreshEnabled,
                            autoUpdateFrequencyMinutes = uiState.autoUpdateFrequencyMinutes,
                            nextHourlyRefreshMinutesRemaining = uiState.nextHourlyRefreshMinutesRemaining,
                            autoRefreshCycleCount = uiState.autoRefreshCycleCount,
                            lastSyncStatusMessage = uiState.lastSyncStatusMessage,
                            appVersionInstalled = uiState.appVersionInstalled,
                            appVersionLatest = uiState.appVersionLatest,
                            isUpdateCheckLoading = uiState.isUpdateCheckLoading,
                            onForceSyncAll = { viewModel.forceSyncAllData() },
                            onToggleAutoRefresh = { viewModel.setHourlyAutoRefreshEnabled(it) },
                            onSetFrequency = { viewModel.setAutoUpdateFrequency(it) },
                            onCheckForUpdates = { viewModel.checkForAppUpdates() },
                            onClose = { viewModel.closeFeatureSheet() }
                        )
                    }
                }
                UniqueFeatureSheetType.BALASORE_JUNCTION_RADAR -> {
                    BalasoreJunctionRadarSheet(
                        language = uiState.language,
                        onClose = { viewModel.closeFeatureSheet() }
                    )
                }
                UniqueFeatureSheetType.BALESWARI_PAAN_KRUSHI_MANDI -> {
                    BaleswariPaanKrushiMandiSheet(
                        language = uiState.language,
                        onClose = { viewModel.closeFeatureSheet() }
                    )
                }
                UniqueFeatureSheetType.NIGHT_CHEMIST_SANJEEVANI -> {
                    NightChemistSanjeevaniSheet(
                        language = uiState.language,
                        onClose = { viewModel.closeFeatureSheet() }
                    )
                }
                UniqueFeatureSheetType.BALESWAR_MAHOTSAV_CHADAK -> {
                    BaleswarMahotsavChadakSheet(
                        language = uiState.language,
                        onClose = { viewModel.closeFeatureSheet() }
                    )
                }
                UniqueFeatureSheetType.NILAGIRI_STONE_ARTISAN -> {
                    NilagiriStoneArtisanSheet(
                        language = uiState.language,
                        onClose = { viewModel.closeFeatureSheet() }
                    )
                }
                UniqueFeatureSheetType.TPNODL_WATCO_MONITOR -> {
                    TpnodlWatcoMonitorSheet(
                        language = uiState.language,
                        onClose = { viewModel.closeFeatureSheet() }
                    )
                }
                UniqueFeatureSheetType.TALASARI_MANGROVE_EXPLORER -> {
                    TalasariMangroveExplorerSheet(
                        language = uiState.language,
                        onClose = { viewModel.closeFeatureSheet() }
                    )
                }
                UniqueFeatureSheetType.MO_SEVA_KENDRA_CITIZEN -> {
                    MoSevaKendraCitizenSheet(
                        language = uiState.language,
                        onClose = { viewModel.closeFeatureSheet() }
                    )
                }
                UniqueFeatureSheetType.MARINE_FISHERMEN_SAFETY -> {
                    MarineFishermenSafetySheet(
                        language = uiState.language,
                        onClose = { viewModel.closeFeatureSheet() }
                    )
                }
                UniqueFeatureSheetType.KULDIHA_ECO_CAMP_SAFARI -> {
                    KuldihaEcoCampSafariSheet(
                        language = uiState.language,
                        onClose = { viewModel.closeFeatureSheet() }
                    )
                }
                UniqueFeatureSheetType.FM_MCH_MEDICAL_COLLEGE_OPD -> {
                    FmMchMedicalCollegeOpdSheet(
                        language = uiState.language,
                        onClose = { viewModel.closeFeatureSheet() }
                    )
                }
                UniqueFeatureSheetType.SABAI_GRASS_MISSION_SHAKTI -> {
                    SabaiGrassMissionShaktiSheet(
                        language = uiState.language,
                        onClose = { viewModel.closeFeatureSheet() }
                    )
                }
                UniqueFeatureSheetType.BALASORE_MARITIME_COLONIAL -> {
                    BalasoreMaritimeColonialSheet(
                        language = uiState.language,
                        onClose = { viewModel.closeFeatureSheet() }
                    )
                }
                UniqueFeatureSheetType.BALASORE_STUDENT_CAREER_SCHOLARSHIP -> {
                    BalasoreStudentCareerScholarshipSheet(
                        language = uiState.language,
                        onClose = { viewModel.closeFeatureSheet() }
                    )
                }
                UniqueFeatureSheetType.DRDO_CHANDIPUR_SAFETY_RADAR -> {
                    DrdoChandipurSafetyRadarSheet(
                        language = uiState.language,
                        onClose = { viewModel.closeFeatureSheet() }
                    )
                }
                UniqueFeatureSheetType.SUBARNAREKHA_SLUICE_FLOOD_RADAR -> {
                    SubarnarekhaSluiceFloodRadarSheet(
                        language = uiState.language,
                        onClose = { viewModel.closeFeatureSheet() }
                    )
                }
                UniqueFeatureSheetType.NILAGIRI_CHHAU_CULTURAL_GUILD -> {
                    NilagiriChhauCulturalGuildSheet(
                        language = uiState.language,
                        onClose = { viewModel.closeFeatureSheet() }
                    )
                }
                UniqueFeatureSheetType.DHH_BLOOD_BANK_LIVE_RADAR -> {
                    DhhBloodBankLiveRadarSheet(
                        language = uiState.language,
                        onClose = { viewModel.closeFeatureSheet() }
                    )
                }
                UniqueFeatureSheetType.PAN_BARAJA_AQUA_SHRIMP_DESK -> {
                    PanBarajaAquaShrimpSheet(
                        language = uiState.language,
                        onClose = { viewModel.closeFeatureSheet() }
                    )
                }
                UniqueFeatureSheetType.BALASORE_COURT_LEGAL_AID_DESK -> {
                    BalasoreCourtLegalAidSheet(
                        language = uiState.language,
                        onClose = { viewModel.closeFeatureSheet() }
                    )
                }
                UniqueFeatureSheetType.NH16_HIGHWAY_PATROL_TRAUMA_SOS -> {
                    Nh16HighwayPatrolTraumaSheet(
                        language = uiState.language,
                        onClose = { viewModel.closeFeatureSheet() }
                    )
                }
                UniqueFeatureSheetType.PANCHALINGESWAR_KULDIHA_SAFARI -> {
                    PanchalingeswarKuldihaSafariSheet(
                        language = uiState.language,
                        onClose = { viewModel.closeFeatureSheet() }
                    )
                }
                UniqueFeatureSheetType.SER_BALASORE_RAILWAY_JUNCTION -> {
                    SerBalasoreRailwayJunctionSheet(
                        language = uiState.language,
                        onClose = { viewModel.closeFeatureSheet() }
                    )
                }
                UniqueFeatureSheetType.NOCCI_INDUSTRIAL_B2B_SKILL -> {
                    NocciIndustrialB2bSkillSheet(
                        language = uiState.language,
                        onClose = { viewModel.closeFeatureSheet() }
                    )
                }
                UniqueFeatureSheetType.TALSARI_BICHITRAPUR_MANGROVE_PILOT -> {
                    TalsariBichitrapurMangrovePilotSheet(
                        language = uiState.language,
                        onClose = { viewModel.closeFeatureSheet() }
                    )
                }
                UniqueFeatureSheetType.FAKIR_MOHAN_BHASHA_LITERATURE -> {
                    FakirMohanBhashaLiteratureSheet(
                        language = uiState.language,
                        onClose = { viewModel.closeFeatureSheet() }
                    )
                }
                UniqueFeatureSheetType.BALESWARI_CUISINE_SWEET_HERITAGE -> {
                    BaleswariCuisineSweetHeritageSheet(
                        language = uiState.language,
                        onClose = { viewModel.closeFeatureSheet() }
                    )
                }
                UniqueFeatureSheetType.TPNODL_WATCO_UTILITY_HOTLINE -> {
                    TpnodlWatcoUtilityHotlineSheet(
                        language = uiState.language,
                        onClose = { viewModel.closeFeatureSheet() }
                    )
                }
                UniqueFeatureSheetType.DHAN_MANDI_MSP_PROCUREMENT -> {
                    DhanMandiMspProcurementSheet(
                        language = uiState.language,
                        onClose = { viewModel.closeFeatureSheet() }
                    )
                }
                UniqueFeatureSheetType.NILAGIRI_GRANITE_STONEWARE_GUILD -> {
                    NilagiriGraniteStonewareGuildSheet(
                        language = uiState.language,
                        onClose = { viewModel.closeFeatureSheet() }
                    )
                }
                UniqueFeatureSheetType.BAHANAGA_NHAI_RAPID_TRAUMA_NETWORK -> {
                    BahanagaNhaiRapidTraumaNetworkSheet(
                        language = uiState.language,
                        onClose = { viewModel.closeFeatureSheet() }
                    )
                }
                UniqueFeatureSheetType.BHUSANDESWAR_CHANDANESWAR_PILGRIMAGE -> {
                    BhusandeswarChandaneswarPilgrimageSheet(
                        language = uiState.language,
                        onClose = { viewModel.closeFeatureSheet() }
                    )
                }
                UniqueFeatureSheetType.SENIOR_CITIZEN_SEVA_SETU -> {
                    SeniorCitizenSevaSetuSheet(
                        language = uiState.language,
                        onClose = { viewModel.closeFeatureSheet() }
                    )
                }
                UniqueFeatureSheetType.BALASORE_HIGHER_EDUCATION_HUB -> {
                    BalasoreHigherEducationHubSheet(
                        language = uiState.language,
                        onClose = { viewModel.closeFeatureSheet() }
                    )
                }
                UniqueFeatureSheetType.BALASORE_MO_BUS_CRUT_NAVIGATOR -> {
                    BalasoreMoBusCrutNavigatorSheet(
                        language = uiState.language,
                        onClose = { viewModel.closeFeatureSheet() }
                    )
                }
                UniqueFeatureSheetType.AQUA_SHRIMP_HATCHERY_RADAR -> {
                    AquaShrimpHatcheryRadarSheet(
                        dailyPulse = uiState.dailyPulse,
                        language = uiState.language,
                        onDismiss = { viewModel.closeFeatureSheet() }
                    )
                }
                UniqueFeatureSheetType.COASTAL_CYCLONE_SHELTER_NETWORK -> {
                    CoastalCycloneShelterNetworkSheet(
                        dailyPulse = uiState.dailyPulse,
                        language = uiState.language,
                        onDismiss = { viewModel.closeFeatureSheet() }
                    )
                }
                UniqueFeatureSheetType.MARITIME_FOREIGN_LOGE_TRAIL -> {
                    MaritimeForeignLogeTrailSheet(
                        dailyPulse = uiState.dailyPulse,
                        language = uiState.language,
                        onDismiss = { viewModel.closeFeatureSheet() }
                    )
                }
                UniqueFeatureSheetType.RURAL_MOBILE_TELEMEDICINE_NETWORK -> {
                    RuralMobileTelemedicineNetworkSheet(
                        dailyPulse = uiState.dailyPulse,
                        language = uiState.language,
                        onDismiss = { viewModel.closeFeatureSheet() }
                    )
                }
                UniqueFeatureSheetType.BALESWARI_BETEL_PADDY_COOPERATIVE -> {
                    BaleswariBetelPaddyCooperativeSheet(
                        dailyPulse = uiState.dailyPulse,
                        language = uiState.language,
                        onDismiss = { viewModel.closeFeatureSheet() }
                    )
                }
                UniqueFeatureSheetType.COASTAL_CRUT_INTERDISTRICT_BUS_RADAR -> {
                    CoastalCrutInterdistrictBusRadarSheet(
                        dailyPulse = uiState.dailyPulse,
                        language = uiState.language,
                        onDismiss = { viewModel.closeFeatureSheet() }
                    )
                }
                UniqueFeatureSheetType.HORSESHOE_CRAB_INTERTIDAL_PROTECTION -> {
                    HorseshoeCrabIntertidalProtectionSheet(
                        dailyPulse = uiState.dailyPulse,
                        language = uiState.language,
                        onDismiss = { viewModel.closeFeatureSheet() }
                    )
                }
                UniqueFeatureSheetType.CITIZEN_FEEDBACK_PORTAL -> {
                    CitizenFeedbackSheet(
                        language = uiState.language,
                        onDismiss = { viewModel.closeFeatureSheet() }
                    )
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
        ThemeMode.HIGH_CONTRAST_DARK -> Triple(
            Icons.Default.NightlightRound,
            Color(0xFFFBBF24),
            "Night"
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
